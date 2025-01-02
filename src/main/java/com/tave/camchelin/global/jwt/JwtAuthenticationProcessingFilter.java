package com.tave.camchelin.global.jwt;

import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import com.tave.camchelin.global.user.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();//5

    private final String LOGIN_URL = "/login";//1
    private final String LOGOUT_URL = "/logout";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(LOGIN_URL) || request.getRequestURI().equals(LOGOUT_URL)) {
            filterChain.doFilter(request, response);
            return;//안해주면 아래로 내려가서 계속 필터를 진행하게됨
        }

        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null); //2

        String accessToken = jwtService
                .extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        log.info("========================================");
        log.info("refreshToken : {}", refreshToken);
        log.info("accessToken : {}", accessToken);
        log.info("========================================");

        if (accessToken != null && refreshToken != null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
            return;
        }

        if (refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);//3
            return;
        }

        if (accessToken != null) {
            // Redis 블랙리스트에서 AccessToken 확인
            if (redisTemplate.opsForValue().get("blacklist:" + accessToken) != null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("This token is logged out");
                return;
            }
        }
        checkAccessTokenAndAuthentication(request, response, filterChain);//4
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(
                accessToken -> jwtService.extractEmail(accessToken).ifPresent(
                        email -> userRepository.findByEmail(email).ifPresent(
                                user -> saveAuthentication(user)
                        )
                )
        );

        filterChain.doFilter(request,response);
    }


    private void saveAuthentication(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();//5
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        log.info("AccessToken 재발급");
        userRepository.findByRefreshToken(refreshToken).ifPresent(
                users -> {
                    // 새 AccessToken 발급
                    String newAccessToken = jwtService.createAccessToken(users.getEmail());
                    // 새 AccessToken을 응답 헤더에 추가
                    jwtService.sendAccessToken(response, newAccessToken);
                    log.info("newAccessToken : {}", newAccessToken);
                }
        );
    }
}
