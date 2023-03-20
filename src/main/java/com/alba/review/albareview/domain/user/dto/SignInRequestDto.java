package com.alba.review.albareview.domain.user.dto;

import com.alba.review.albareview.domain.user.Sex;
import com.alba.review.albareview.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class SignInRequestDto {
    private Sex sex;
    private String address;
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",message = "핸드폰 번호의 양식과 맞지 않습니다. 01x-xxx(x)-xxxx")
    private String phoneNumber;
    private int age;

    @Builder
    public SignInRequestDto(Sex sex, String address, String phoneNumber, int age){
        this.sex = sex;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }
    public User toEntity() {
        return  User.builder()
                .address(address)
                .sex(sex)
                .phoneNumber(phoneNumber)
                .age(age)
                .build();
    }
}
