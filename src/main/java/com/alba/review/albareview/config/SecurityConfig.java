package com.alba.review.albareview.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomService customService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //== 접근 제한 ==//
        http.csrf().disable().authorizeRequests()
                .antMatchers("/oauth/test").permitAll() //로그인 화면 접근 가능
                .antMatchers("/oauth/kakao/accessToken").permitAll()
                .antMatchers("/").permitAll() //메인 화면 접근 가능
                .and()
                .oauth2Login().userInfoEndpoint().userService(customService);
    }

}