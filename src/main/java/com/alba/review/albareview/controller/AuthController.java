package com.alba.review.albareview.controller;

import com.alba.review.albareview.service.AuthService;
import com.alba.review.albareview.config.auth.DTO.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //프론트로부터 인가 코드를 받고 카카오 accessToken을 return
    @GetMapping("/auth/kakao/login/{code}")
    public ResponseEntity<String> kakaoLogin(@PathVariable String code) throws IOException {
        return authService.kakaoLogin(code);
    }
    @GetMapping("/oauth/test")
    public String test(){
        return "test";
    }
    @GetMapping("test")
    public String success(HttpServletRequest request){
        System.out.println(request.toString());
        return "성공";
    }
}
