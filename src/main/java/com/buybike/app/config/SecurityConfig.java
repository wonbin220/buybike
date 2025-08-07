package com.buybike.app.config;

import com.buybike.app.controller.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    protected UserDetailsService users() {
//        UserDetails admin = Member.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin1234"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(admin);
//    }



    // 정적 리소스는 Spring Security 필터링을 무시하도록 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/BuyBike/css/**", "/BuyBike/js/**", "/BuyBike/smarteditor/**", "/BuyBike/smarteditor2/**", "/img/**");
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http, LoginSuccessHandler loginSuccessHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                // webSecurityCustomizer에서 처리하므로 여기서는 제거하거나 유지해도 됩니다.
                                // 명확성을 위해 핵심 경로만 남깁니다.
                                .requestMatchers("/", "/login", "/member/add").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")                                    // 사용자 정의 로그인 페이지
                                .loginProcessingUrl("/login")
                                .successHandler(loginSuccessHandler)                    // 로그인 성공 핸들러 등록
                                // .defaultSuccessUrl("/board/list", true) // 관리자 로그인 성공 후 이동 페이지
                                // .defaultSuccessUrl("/board/list", true) // 로그인 성공 후 이동할 페이지
                                .failureUrl("/loginfailed")          // 로그인 실패 후 이동 페이지
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                )
                .csrf(csrf -> csrf
                        // ⭐️ 스마트에디터 이미지 업로드 경로에 대한 CSRF 비활성화
                        .ignoringRequestMatchers("/smarteditorMultiImageUpload")
                )
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                );// X-Frame-Options 헤더 설정
        return http.build();
    }
}
