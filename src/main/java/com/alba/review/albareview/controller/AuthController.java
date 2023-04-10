package com.alba.review.albareview.controller;

import com.alba.review.albareview.config.auth.CustomOauth2UserService;
import com.alba.review.albareview.domain.user.dto.SignInRequestDTO;
import com.alba.review.albareview.domain.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final CustomOauth2UserService customOauth2UserService;

    //회원 정보 조회
    @GetMapping("/auth/member")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam long id){
        return customOauth2UserService.getUser(id);

    }

    //회원 닉네임 중복인지 확인
    @GetMapping("/auth/nickname/{nickname}/duplicate")
    public ResponseEntity<HashMap> checkDuplicateNickname(@PathVariable String nickname){
        return customOauth2UserService.checkDuplicateNickname(nickname);
    }
    //http://localhost:8080/oauth2/authorization/naver -> 네이버
    //http://localhost:8080/oauth2/authorization/google -> 구글
    @PostMapping("/auth/member")
    public ResponseEntity<Long> signIn(@RequestBody SignInRequestDTO dto){
        return customOauth2UserService.signIn(dto);
    }


}
