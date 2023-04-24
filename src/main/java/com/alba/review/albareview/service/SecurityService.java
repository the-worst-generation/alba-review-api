package com.alba.review.albareview.service;

import com.alba.review.albareview.config.auth.DTO.TokenDto;
import com.alba.review.albareview.config.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final JwtProvider jwtProvider;
    public TokenDto login(String email) {
        TokenDto tokenDto = jwtProvider.generateTokenDto(email);
        return tokenDto;
    }
}
