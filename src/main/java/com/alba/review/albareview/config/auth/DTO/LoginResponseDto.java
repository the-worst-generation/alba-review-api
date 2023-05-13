package com.alba.review.albareview.config.auth.DTO;

import com.alba.review.albareview.domain.user.Role;
import com.alba.review.albareview.domain.user.Sex;
import com.alba.review.albareview.domain.user.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private String email;
    private String profilePicture;
    private String nickname;
    private Sex sex;
    private LocalDate birthDate;
    private SocialType socialType;
    private boolean loginSuccess;

}
