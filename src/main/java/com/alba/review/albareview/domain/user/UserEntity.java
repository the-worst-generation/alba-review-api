package com.alba.review.albareview.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "email", unique = true, length = 50)
    private String email;

    @NotNull
    @Column(name = "profile_picture", length = 100)
    private String profilePicture;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", length = 15)
    private SocialType socialType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 10)
    private Role role;

    @Column(name = "nickname", unique = true, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", length = 2)
    private Sex sex;

    @Column(name = "birth_date")
    private Timestamp birthDate;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdDate;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp modifiedDate;

    public void toEntityCustomData(Timestamp birthDate, String nickname, Sex sex) {
        this.birthDate = birthDate;
        this.nickname = nickname;
        this.sex = sex;
    }

}
