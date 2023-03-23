package com.alba.review.albareview.controller;

import com.alba.review.albareview.config.auth.CustomOauth2UserService;
import com.alba.review.albareview.domain.user.dto.SignInRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final CustomOauth2UserService customOauth2UserService;
    @PostMapping("/auth/member")
    public String signIn(@RequestBody SignInRequestDto dto){
        return customOauth2UserService.signIn(dto);
    }
}
