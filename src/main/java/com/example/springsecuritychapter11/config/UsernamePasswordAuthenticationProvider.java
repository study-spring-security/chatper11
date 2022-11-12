package com.example.springsecuritychapter11.config;

import com.example.springsecuritychapter11.service.AuthenticationServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServiceProxy proxy;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        proxy.sendAuth(username, password); // 프락시로 인증 서버를 호춣한다. SMS를 통해 클라이언트 OTP를 보낸다.

        return new UsernamePasswordAuthenticationToken(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication); // Authentication 의 UsernamePasswordAuthentication 형식을 지원할 이 AuthenticationProvider 를 설계한다.
    }
}
