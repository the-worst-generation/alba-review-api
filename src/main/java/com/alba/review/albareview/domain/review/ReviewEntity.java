package com.alba.review.albareview.domain.review;

import com.alba.review.albareview.domain.BaseEntity;
import com.alba.review.albareview.domain.company.CompanyEntity;
import com.alba.review.albareview.domain.user.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ReviewEntity extends BaseEntity {

    @NotNull
    @Column(name = "user_id")
    private long user_id;

    @NotNull
    @Column(name = "company_id")
    private long company_id;

    @NotNull
    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "favorite")
    private int favorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private CompanyEntity company;
}
