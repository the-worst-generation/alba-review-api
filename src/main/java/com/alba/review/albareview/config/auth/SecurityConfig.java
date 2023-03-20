package com.alba.review.albareview.config.auth;

import com.alba.review.albareview.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
//권한 설정해주는 class
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOauth2UserService customOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/","/loginFail").permitAll() //이 url은 모든 애들 다 허가
                .antMatchers("/test").hasRole(Role.USER.name()) //이 api에 접근하려면 USER권한 있어야함
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(customOauth2UserService) //로그인 성공시 진행되어야 하는 서비스 -> DB 에 값 저장
                .and()
                .successHandler(new CustomOauth2SuccessHandler());
                //.defaultSuccessUrl("/auth/member"); //인증된 사용자가 이동할 곳 --> 회원 정보 입력 화면 및 api

    }
}
