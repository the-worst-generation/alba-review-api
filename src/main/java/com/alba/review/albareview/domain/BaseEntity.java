package com.alba.review.albareview.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdDate;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp modifiedDate;
}
