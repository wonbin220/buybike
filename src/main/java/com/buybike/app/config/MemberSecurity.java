package com.buybike.app.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("memberSecurity")
public class MemberSecurity {
    public boolean checkMemberId(Authentication authentication, String memberId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        // ADMIN 권한이 있으면 항상 접근 가능
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }
        // 자신의 정보에만 접근 가능
        String currentUserId = authentication.getName();
        return currentUserId.equals(memberId);
    }
}
