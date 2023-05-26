package com.alba.review.albareview.controller;

import com.alba.review.albareview.domain.user.DTO.LoginRequestDTO;
import com.alba.review.albareview.domain.user.DTO.SignInRequestDTO;
import com.alba.review.albareview.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/kakao/signIn")
    public ResponseEntity<String> kakaoSignIn(@RequestBody Map<String,String> code){
        return authService.kakaoSignIn(code.get("code"));
    }
    @PostMapping("/auth/kakao/logIn") //인가코드를 기반으로 DB에 저장 -> jwt 리턴
    public ResponseEntity<String> getJwtValue(@RequestBody Map<String,String> code) {
        return authService.kakaoLogIn(code.get("code"));
    }
    @GetMapping("/oauth/test")
    public String test(){
        return "test";
    }

    //소셜로 인증 끝내고 custom 회원가입
    @PostMapping("/auth/signIn")
    public ResponseEntity<String> signIn(@RequestBody SignInRequestDTO signInRequestDTO, Principal principal){
        return authService.customSignIn(principal.getName(),signInRequestDTO);
    }

}
