package com.alba.review.albareview.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //== 접근 제한 ==//
        http.csrf().disable().authorizeRequests()
                .antMatchers("/oauth/test").permitAll() //로그인 화면 접근 가능
                .antMatchers("/oauth/kakao/accessToken").permitAll()
                .antMatchers("/").permitAll(); //메인 화면 접근 가능;
    }

}