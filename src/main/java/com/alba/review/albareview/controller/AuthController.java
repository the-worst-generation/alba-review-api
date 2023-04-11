package com.alba.review.albareview.controller;

import com.alba.review.albareview.config.auth.AuthService;
import com.alba.review.albareview.domain.user.dto.SignInRequestDTO;
import com.alba.review.albareview.domain.user.dto.UserResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/oauth/kakao/accessToken")
    public String getAccessTokenByCode(@RequestBody Map<String, String> codeMap) throws IOException {
        return authService.getKakaoToken(codeMap.get("code"));
    }

    @GetMapping("/oauth/test")
    public String test(){
        return "test";
    }
}
