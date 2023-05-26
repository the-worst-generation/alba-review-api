package com.alba.review.albareview.domain.user.DTO;

import com.alba.review.albareview.domain.user.Sex;
import com.alba.review.albareview.domain.user.SocialType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SignInRequestDTO {

    @JsonProperty("social_type")
    private SocialType socialType;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 1, max = 7, message = "이름은 1글자 이상 8글자 미만이어야 합니다.")
    @JsonProperty("nickname")
    private String nickname;

    @NotBlank(message = "성별은 필수입니다.")
    @Pattern(regexp = "^[MW]$", message = "성별은 M 또는 W 값만 허용합니다.")
    @JsonProperty("sex")
    private Sex sex;

    @NotBlank
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 생년월일을 입력하세요 (yyyy-MM-dd).")
    @JsonProperty("birth_date")
    private Timestamp birthDate;
}
