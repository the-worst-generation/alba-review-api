package com.alba.review.albareview.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Sex {
    MAN("M"),
    WOMAN("W");
    private final String key;
}
