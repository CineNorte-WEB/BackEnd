package com.tave.camchelin.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tave.camchelin.domain.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
@RequiredArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtServiceImpl implements JwtService{


    //== jwt.yml에 설정된 값 가져오기 ==//
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long accessTokenValidityInSeconds;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    //== 1 ==//
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;


    //메서드
    @Override
    public String createAccessToken(String email) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000))
                .withClaim(USERNAME_CLAIM, email)
                .sign(Algorithm.HMAC512(secret));
    }

    @Override
    public String createRefreshToken(String email) {
        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds * 1000))
                .sign(Algorithm.HMAC512(secret));

        userRepository.findByEmail(email).ifPresent(user -> {
            user.updateRefreshToken(refreshToken); // RefreshToken 필드에 값 설정
            userRepository.save(user); // DB에 저장
        });

        return refreshToken;
    }

    @Override
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        users -> users.updateRefreshToken(refreshToken),
                        () -> new Exception("회원 조회 실패")
                );
    }

    @Override
    public void destroyRefreshToken(String email) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        users -> users.destroyRefreshToken(),
                        () -> new Exception("회원 조회 실패")
                );
    }

    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);
    }

    @Override
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        // 요청 헤더에서 refreshToken을 추출
        String tokenFromHeader = Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""))
                .orElse(null);
        if (tokenFromHeader != null) {
            // 만약 accessToken이 null이면 바로 tokenFromHeader를 반환
            String token = extractAccessToken(request).orElse(null);
            if (!isTokenValid(token)) {
                log.info("accessToken이 없으므로, 헤더에서 추출한 refreshToken을 반환합니다.");
                return Optional.of(tokenFromHeader);
            }

            Optional<String> storedRefreshToken = getRefreshTokenFromUserEntity(request);
            // 저장된 refreshToken이 유효한지 확인하고 사용
            if (storedRefreshToken.isPresent() && storedRefreshToken.get().equals(tokenFromHeader)) {
                return storedRefreshToken;
            }
        }
        return Optional.empty();
    }

    // 사용자의 `refreshToken`을 DB에서 가져오는 메서드
    private Optional<String> getRefreshTokenFromUserEntity(HttpServletRequest request) {
        // JWT에서 AccessToken 추출
        String token = extractAccessToken(request).orElse(null);
        if (token != null && isTokenValid(token)) {
            // 유효한 토큰에서 이메일 추출
            Optional<String> emailOptional = extractEmail(token);
            if (emailOptional.isPresent()) {
                String email = emailOptional.get();
                // DB에서 사용자 조회 후 RefreshToken 반환
                return userRepository.findByEmail(email)
                        .map(user -> {
                            log.info("DB에서 조회된 RefreshToken: {}", user.getRefreshToken());
                            return user.getRefreshToken();
                        });
            } else {
                log.warn("토큰에서 이메일을 추출하지 못했습니다.");
            }
        } else {
            log.warn("유효하지 않은 토큰 또는 토큰 없음");
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(
                    JWT.require(Algorithm.HMAC512(secret))
                            .build()
                            .verify(accessToken)
                            .getClaim(USERNAME_CLAIM)
                            .asString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public long getExpiration(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token)
                    .getExpiresAt()
                    .getTime();
        } catch (Exception e) {
            log.error("토큰 만료 시간 추출 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    @Override
    public void addTokenToBlacklist(String accessToken) {
        try {
            // 토큰 만료 시간 계산
            long expiration = getExpiration(accessToken);
            long currentTime = System.currentTimeMillis();
            long ttl = expiration - currentTime;

            // Redis에 블랙리스트 추가
            if (ttl > 0) {
                String redisKey = "blacklist:" + accessToken;
                redisTemplate.opsForValue().set(redisKey, "true", ttl, TimeUnit.MILLISECONDS);
                log.info("토큰 {} 블랙리스트에 추가됨 (TTL: {}ms)", accessToken, ttl);
            } else {
                log.info("토큰 {}은 이미 만료되어 블랙리스트에 추가되지 않음", accessToken);
            }
        } catch (IllegalArgumentException e) {
            log.error("블랙리스트 등록 실패: {}", e.getMessage());
        }
    }

    @Override
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다.", e.getMessage());
            return false;
        }
    }
}
