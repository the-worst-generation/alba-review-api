package com.alba.review.albareview.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    @Value("${security.oauth2.client.registration.kakao.client-id}")
    public static String clientId;

    @Value("${security.oauth2.client.registration.kakao.redirect_uri}")
    public static String redirect_uri;
}
