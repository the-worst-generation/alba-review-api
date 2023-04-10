package com.alba.review.albareview.domain.user;

import jdk.vm.ci.meta.Local;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.security.DenyAll;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "profile_picture")
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public User update(String nickname, String profilePicture, Sex sex, LocalDate birthDate) {
        this.nickname = nickname;
        this.profilePicture = profilePicture;
        this.sex = sex;
        this.birthDate = birthDate;
        return this;
    }
    public User signInCustom(Sex sex, String nickname, LocalDate birthDate){
        this.sex = sex;
        this.nickname = nickname;
        this.birthDate = birthDate;
        return this;
    }
    public String getRoleKey(){
        return this.role.getKey();
    }

    public User signInAuth(String name, String profilePicture) {
        this.name = name;
        this.profilePicture = profilePicture;
        return this;
    }
}
