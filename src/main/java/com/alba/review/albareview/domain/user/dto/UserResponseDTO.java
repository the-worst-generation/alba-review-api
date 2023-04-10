package com.alba.review.albareview.domain.user.dto;

import com.alba.review.albareview.domain.user.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.management.relation.Role;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String name;
    private String email;
    private String profilePicture;
    private String role;
    private Sex sex;
    private String nickname;
    private LocalDate birthDate;
}
