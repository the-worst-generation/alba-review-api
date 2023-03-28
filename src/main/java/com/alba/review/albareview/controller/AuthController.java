package com.alba.review.albareview.controller;

import com.alba.review.albareview.config.auth.CustomOauth2UserService;
import com.alba.review.albareview.domain.user.dto.SignInRequestDTO;
import com.alba.review.albareview.domain.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final CustomOauth2UserService customOauth2UserService;

    @GetMapping("/auth/member")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam long id){
        return customOauth2UserService.getUser(id);

    }
    @PostMapping("/auth/member")
    public ResponseEntity<Long> signIn(@RequestBody SignInRequestDTO dto){
        return customOauth2UserService.signIn(dto);
    }


}
