package com.alba.review.albareview.controller;

import com.alba.review.albareview.domain.user.DTO.SignInRequestDTO;
import com.alba.review.albareview.service.AuthService;
import com.alba.review.albareview.config.auth.DTO.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //프론트로부터 인가 코드를 받고 카카오 accessToken을 return
    @GetMapping("/auth/kakao/login/{code}") //인가코드를 기반으로 DB에 저장 -> jwt 리턴
    public ResponseEntity<String> getJwtValue(@PathVariable String code) {
        return authService.getJwtValue(code);
    }
    @GetMapping("/oauth/test")
    public String test(){
        return "test";
    }

    @PostMapping("/auth/signIn")
    public ResponseEntity<Long> signIn(@RequestBody SignInRequestDTO signInRequestDTO, Principal principal){
        return authService.signIn(principal.getName(),signInRequestDTO);
    }

}
