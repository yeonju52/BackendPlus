package com.lion.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable())   // CRSF 방어 기능 비활성화: 인수로 람다함수 (화살표함수)를 넣어야 함
                .headers(x -> x.frameOptions(y -> y.disable())) // H2-console을 쓰기 위함
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/book/list", "/book/detail",
                                "/mall/detail", "/mall/detail", "/user/register",
                                "/h2-console", "/demo/**", "/error/**",
                                "/img/**", "/js/**", "/css/**").permitAll()
                        .requestMatchers("/book/insert", "/book/yes24",
                                "/order/listAll", "/order/bookState",
                                "/user/delete", "/user/list").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                ).formLogin(auth -> auth
                        .loginPage("/user/login")           // login form
                        .loginProcessingUrl("/user/login")  // 스프링이 낚아 챔. UserDetailService 구현 객체에서 처리해야함
                        .usernameParameter("uid")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/user/loginSuccess", true)  // 로그인 후 해야할 일들
                        .permitAll()
                ).logout(auth -> auth
                        .logoutUrl("/user/logout")
                        .invalidateHttpSession(true)        // 로그아웃시 세션 삭제
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/user/login")
                );
        return http.build();
    }
}
