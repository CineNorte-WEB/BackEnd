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
        log.info("로그아웃 시작");

        // AccessToken 처리
        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        if (accessToken != null && jwtService.isTokenValid(accessToken)) {
            long accessTokenExpiration = jwtService.getExpiration(accessToken);
            long currentTime = System.currentTimeMillis();
            long ttl = accessTokenExpiration - currentTime;

            if (ttl > 0) {
                redisTemplate.opsForValue().set("blacklist:" + accessToken, "logout", ttl, TimeUnit.MILLISECONDS);
                log.info("토큰 {} 블랙리스트에 추가됨 (TTL: {}ms)", accessToken, ttl);
            } else {
                log.warn("토큰 {}은 이미 만료됨", accessToken);
            }
        } else {
            log.info("유효하지 않거나 null인 accessToken");
        }

        // RefreshToken 처리
        String refreshToken = jwtService.extractRefreshToken(request).orElse(null);
        if (refreshToken != null && jwtService.isTokenValid(refreshToken)) {
            userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
                user.destroyRefreshToken();
                userRepository.save(user);
                log.info("refreshToken 삭제됨: {}", refreshToken);
            });
        } else {
            log.info("유효하지 않거나 null인 refreshToken");
        }

        // Spring Security Context 초기화
        SecurityContextHolder.clearContext();
        log.info("로그아웃 완료");
    }
}