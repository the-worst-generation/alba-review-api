package com.alba.review.albareview.controller;

import com.alba.review.albareview.config.auth.AuthService;
import com.alba.review.albareview.config.auth.DTO.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //인가코드 받기
    @PostMapping("/oauth/kakao/accessToken")
    public ResponseEntity<String> getAccessTokenByCode(@RequestBody Map<String, String> codeMap) throws IOException {
        return authService.getKakaoToken(codeMap.get("code"));
    }

    @GetMapping("/oauth/kakao/signIn/{code}")
    public LoginResponseDto kakaoLogin(@PathVariable String code){
        return authService.kakaoSignin(code);
    }
    @GetMapping("/oauth/test")
    public String test(){
        return "test";
    }
}
