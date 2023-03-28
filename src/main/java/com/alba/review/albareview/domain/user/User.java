package com.alba.review.albareview.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column
    private Sex sex;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private int age;

    @Builder
    public User(String name, String email, String picture, Role role, Sex sex, String address, String phoneNumber, int age){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.sex = sex;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }

    public User update(String name, String picture, Sex sex, String address, String phoneNumber, int age) {
        this.name = name;
        this.picture = picture;
        this.sex = sex;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
        return this;
    }
    public User signInCustom(Sex sex, String address, String phoneNumber, int age){
        this.sex = sex;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
        return this;
    }
    public String getRoleKey(){
        return this.role.getKey();
    }

    public User signInAuth(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }
}
