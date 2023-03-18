package com.alba.review.albareview.config.auth.dto;

import com.alba.review.albareview.domain.user.User;

import java.io.Serializable;

public class SessionUser implements Serializable {
    private String name;
    private String picture;
    private String email;
    public SessionUser(User user) {
        this.name = user.getName();
        this.picture = user.getPicture();
        this.email = user.getEmail();
    }
}
