package com.alba.review.albareview.domain.tag;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class TagEntity {
    @Id
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "content", unique = true, length = 50)
    private String content;
}
