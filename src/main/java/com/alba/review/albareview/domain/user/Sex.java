package com.alba.review.albareview.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Sex {
    M("M"),
    W("W");
    private final String key;
}
