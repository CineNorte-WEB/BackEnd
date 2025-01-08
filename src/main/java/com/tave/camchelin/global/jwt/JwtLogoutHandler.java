package com.tave.camchelin.global.jwt;

import com.tave.camchelin.domain.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // AccessToken 가져오기
        log.info("로그아웃 시작");
        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        log.info("로그아웃 처리 중, 유효한 accessToken 발견: {}", accessToken);
        if (accessToken != null && jwtService.isTokenValid(accessToken)) {
            // 블랙리스트 처리 또는 Redis에서 토큰 삭제
            long accessTokenExpiration = jwtService.getExpiration(accessToken); // 만료시간 가져오기
            redisTemplate.opsForValue().set("blacklist:" + accessToken, "logout", accessTokenExpiration, TimeUnit.MILLISECONDS); // 블랙리스트 추가
            log.info("accessToken 블랙리스트에 추가됨: {}", accessToken);  // 로그 추가
        } else {
            log.info("유효하지 않거나 null인 accessToken");
        }

        String refreshToken = jwtService.extractRefreshToken(request).orElse(null);

        if (refreshToken != null && jwtService.isTokenValid(refreshToken)) {
            log.info("로그아웃 처리 중, 유효한 refreshToken 발견: {}", refreshToken);  // 로그 추가

            // 사용자 조회
            userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
                // refreshToken 필드를 null로 설정하여 무효화 처리
                user.destroyRefreshToken();
                userRepository.save(user); // 변경된 엔티티 저장
                log.info("refreshToken 삭제됨: {}", user.getRefreshToken());  // 로그 추가
            });
        } else {
            log.info("유효하지 않거나 null인 refreshToken");
        }
        log.info("로그아웃 완료");
        // 세션 및 인증 정보 무효화
        SecurityContextHolder.clearContext(); // Spring Security Context 초기화
    }
}