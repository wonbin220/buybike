package com.buybike.app.config;

import com.buybike.app.controller.LoginSuccessHandler;
import com.buybike.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager; // 이 부분을 추가하세요.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MemberService memberService;


    @Autowired
    private ApplicationContext context; // ApplicationContext 주입

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

    // SpEL을 사용하는 AuthorizationManager를 Bean으로 등록
//    @Bean
//    public AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager() {
//        ExpressionAuthorizationManager<RequestAuthorizationContext> authorizationManager =
//                new ExpressionAuthorizationManager<>("hasRole('ADMIN') or @memberSecurity.checkMemberId(authentication, #memberId)");
//        authorizationManager.setApplicationContext(context);
//        return authorizationManager;
//    }
    // SpEL을 사용하는 AuthorizationManager를 생성하는 메소드
    private AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager() {
        WebExpressionAuthorizationManager authorizationManager =
                new WebExpressionAuthorizationManager("hasRole('ADMIN') or @memberSecurity.checkMemberId(authentication, #memberId)");
        authorizationManager.setApplicationContext(context);
        return authorizationManager;
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http, LoginSuccessHandler loginSuccessHandler) throws Exception {
        http
                // CSRF 설정을 하나로 통합합니다.
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/smarteditorMultiImageUpload")
                        // 만약 모든 CSRF를 비활성화하려면 아래 주석을 해제하고 위 라인을 주석 처리하세요.
                        // .disable()
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                // 모든 사용자 접근 가능
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/BuyBike/**").permitAll()
                                .requestMatchers("/", "/member/login", "/member/add").permitAll()
                                // ADMIN 또는 OPERATOR만 접근 가능
                                .requestMatchers("/member/list").hasAnyRole("ADMIN", "OPERATOR")
                                // ADMIN만 접근 가능
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                // 본인 정보 수정, 삭제는 USER도 가능하도록 SpEL 사용
                                // Bean으로 등록한 AuthorizationManager를 사용하도록 수정
                                .requestMatchers("/member/update/{memberId}", "/member/delete/{memberId}")
                                .access(customAuthorizationManager())
                                //.access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or @memberSecurity.checkMemberId(authentication, #memberId)")) // 이 부분을 수정하세요.
                                // 나머지 요청은 인증된 사용자만
                                .anyRequest().authenticated()
                )
                .userDetailsService(memberService) // ⭐️ UserDetailsService로 MemberService를 사용하도록 명시
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")                                    // 사용자 정의 로그인 페이지
                                .loginProcessingUrl("/login")
                                .successHandler(loginSuccessHandler)                    // 로그인 성공 핸들러 등록
                                .failureUrl("/login?error=true")          // 로그인 실패 후 이동 페이지
//                                .failureUrl("/loginfailed")          // 로그인 실패 후 이동 페이지
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                )
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                );// X-Frame-Options 헤더 설정
        return http.build();
    }
}
