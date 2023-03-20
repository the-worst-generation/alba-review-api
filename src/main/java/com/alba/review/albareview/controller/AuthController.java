package com.alba.review.albareview.controller;

import com.alba.review.albareview.config.auth.CustomOauth2UserService;
import com.alba.review.albareview.domain.user.dto.SignInRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final CustomOauth2UserService customOauth2UserService;
    @PostMapping("/auth/member")
    public Long signIn(@RequestBody SignInRequestDto dto){
        return customOauth2UserService.signIn(dto);
    }
    @GetMapping("/test")
    public String hello(){
        return "Hello World!";
    }
    @GetMapping("/")
    public String main(){
        return "main";
    }
    @GetMapping("/loginFail")
    public String fail(){
        return "fail";
    }
}
