package com.alba.review.albareview.domain.company;

import com.alba.review.albareview.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CompanyEntity extends BaseEntity {

    @NotNull
    @Column(name = "id", unique = true, length = 60)
    private String name;

    @NotNull
    @Column(name = "position", unique = true)
    private Point point;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "rating")
    private BigDecimal rating;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "favorite")
    private int favorite;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "dislike")
    private int dislike;

}
