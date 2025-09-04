package com.buybike.app.domain;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Long num;
    private String memberId;
    private String memberName;
    private String password;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime regDt;

    @Enumerated(EnumType.STRING)
    private Role role; // 사용자 권한 (USER, ADMIN, OPERATOR)

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setMemberId(memberFormDto.getMemberId());
        member.setMemberName(memberFormDto.getMemberName());
        member.setPhone(memberFormDto.getPhone());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }
}
