package com.buybike.app.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("memberSecurity")
public class MemberSecurity {
    public boolean checkMemberId(Authentication authentication, String memberId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        // 자신의 정보에만 접근 가능
        String currentUserId = authentication.getName();
        return currentUserId.equals(memberId);
    }
}