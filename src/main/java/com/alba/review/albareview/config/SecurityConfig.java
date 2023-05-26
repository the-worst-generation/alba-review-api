package com.alba.review.albareview.config;

import com.alba.review.albareview.config.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //== 접근 제한 ==//
        http.csrf().disable()
                .sessionManagement()//세션 사용 X
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 접근 권한 설정부
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // 열어두어야 CORS Preflight 막을 수 있음
                .antMatchers("/", "/h2/**", "/auth/**").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()

                // JWT 토큰 예외처리부
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }

}