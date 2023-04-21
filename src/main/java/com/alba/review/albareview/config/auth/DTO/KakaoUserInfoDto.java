package com.alba.review.albareview.config.auth.DTO;

import com.alba.review.albareview.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KakaoUserInfoDto {
    private String email;
    private String profilePicture;
}
