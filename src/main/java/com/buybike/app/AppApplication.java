package com.buybike.app;

import com.buybike.app.domain.Member;
import com.buybike.app.domain.Role;
import com.buybike.app.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

    // 관리자 정보를 Member 엔티티에 등록
    @Bean
    public CommandLineRunner run(MemberService memberService) {
        return (String[] args) -> {
            if (!memberService.findByMemberId("Admin")) {
                Member member = new Member();
                member.setMemberId("Admin");
                member.setMemberName("관리자");
                member.setPhone("");
                member.setEmail("");
                member.setAddress("");
                String password = new BCryptPasswordEncoder().encode("admin1234");
                member.setPassword(password);
                member.setRole(Role.ADMIN);
                memberService.saveMember(member);
            }
        };
    }
}
