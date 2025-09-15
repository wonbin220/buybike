package com.buybike.app.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("memberSecurity")
public class MemberSecurity {

    /**
     * 현재 인증된 사용자의 ID와 요청된 memberId가 일치하는지 확인합니다.
     * ADMIN 역할은 이 검사를 통과하지 않아도 접근이 가능하므로,
     * 이 메소드는 일반 사용자가 자신의 정보에 접근하는 경우를 확인하는 데 사용됩니다.
     *
     * @param authentication 현재 사용자 인증 정보 객체
     * @param memberId       URL 경로에서 추출된 검사 대상 회원 ID
     * @return 현재 로그인한 사용자의 ID와 대상 ID가 일치하면 true, 그렇지 않으면 false
     */
    public boolean checkMemberId(Authentication authentication, String memberId) {
        // 1. 인증 정보가 없거나, 인증되지 않은 사용자인 경우 접근 거부
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 2. Principal 객체를 CustomUserDetails 타입으로 형변환
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;

        // 3. CustomUserDetails에서 현재 로그인한 사용자의 ID(String 타입)를 가져옵니다.
        //    UserDetailsService 구현에 따라 사용자 ID는 getUsername()으로 가져올 수 있습니다.
        String currentUserId = userDetails.getUsername();

        // 4. 현재 로그인한 사용자의 ID와 파라미터로 받은 memberId가 같은지 비교하여 결과를 반환
        return currentUserId != null && currentUserId.equals(memberId);
    }
}