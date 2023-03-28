package com.alba.review.albareview.domain.user.dto;

import com.alba.review.albareview.domain.user.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.management.relation.Role;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String name;
    private String email;
    private String picture;
    private String role;
    private Sex sex;
    private String address;
    private String phoneNumber;
    private int age;
}
