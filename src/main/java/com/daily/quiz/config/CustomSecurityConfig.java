package com.daily.quiz.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // CSRF 보안 기능 비활성화
        http
                .csrf(csrf-> csrf.disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()))
        ;

        
        //로그인 페이지 및 로그인 성공 후 페이지 설정
        http.formLogin(from -> { //폼 기반 로그인 활성화
            from.loginPage("/signIn")
                    //로그인 성공시 메인페이지 이동
                    .defaultSuccessUrl("/");
        });


        
        return http.build();
    }


    // 정적 리소스 필터링을 위한 웹 보안 커스터마이저 빈 등록
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 필터링 제외 (static 경로아래 있는 녀석들 실행 시 로그인 인증에서 제외됨)
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }
}
