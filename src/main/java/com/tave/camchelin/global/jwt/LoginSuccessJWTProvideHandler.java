package com.tave.camchelin.global.jwt;

import com.tave.camchelin.domain.users.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository usersRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = extractEmail(authentication);

        String nickname = usersRepository.findByEmail(email)
                .map(user -> user.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));


        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken(email);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        usersRepository.findByEmail(email).ifPresent(
                users -> users.updateRefreshToken(refreshToken)
        );

        log.info( "로그인에 성공합니다. email: {}" , email);
        log.info( "AccessToken 을 발급합니다. AccessToken: {}" , accessToken);
        log.info( "RefreshToken 을 발급합니다. RefreshToken: {}" , refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"success\", \"email\": \"" + email + "\", \"nickname\": \"" + nickname + "\"}");
    }

    private String extractEmail(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
