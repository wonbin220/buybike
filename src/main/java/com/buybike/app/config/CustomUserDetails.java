package com.buybike.app.config;

import com.buybike.app.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    // Thymeleaf에서 #authentication.principal.memberId 로 접근 가능
    public String getMemberId() {
        return member.getMemberId();
    }

    // Thymeleaf에서 #authentication.principal.memberName 로 접근 가능
    public String getMemberName() {
        return member.getMemberName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한을 반환합니다. 예시로 단일 권한을 하드코딩했지만,
        // 실제로는 Member 엔티티의 권한 정보를 반환해야 합니다.
        // return Collections.singletonList(new SimpleGrantedAuthority(member.getRole()));
        return Collections.emptyList(); // 권한이 없다면 비어있는 리스트 반환
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        // 로그인 시 사용할 ID를 반환합니다. memberId를 사용합니다.
        return member.getMemberId();
    }

    // 계정 만료 여부 (true: 만료되지 않음)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명(비밀번호) 만료 여부 (true: 만료되지 않음)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 (true: 활성화됨)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
