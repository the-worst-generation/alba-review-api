package com.alba.review.albareview.domain.user.dto;

import com.alba.review.albareview.domain.user.Sex;
import com.alba.review.albareview.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SignInRequestDTO {

    private String nickname;
    private Sex sex;
    private LocalDate birthDate;

    public User toEntity() {
        return  User.builder()
                .nickname(nickname)
                .sex(sex)
                .birthDate(birthDate)
                .build();
    }
}
