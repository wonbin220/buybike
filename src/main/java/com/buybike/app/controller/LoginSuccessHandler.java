package com.buybike.app.controller;

import com.buybike.app.domain.Member;
import com.buybike.app.domain.MemberFormDto;
import com.buybike.app.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MemberService memberService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberId = userDetails.getUsername(); // Spring Security에서 username은 보통 ID를 의미

        // DB에서 사용자 전체 정보 조회
        Member member = memberService.getMemberById(memberId);

        // 세션에 저장할 사용자 정보 DTO 생성
        MemberFormDto sessionDto = new MemberFormDto(member.getMemberId(), member.getMemberName());

        // 세션에 사용자 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute("userLoginInfo", sessionDto);

        // 메인 페이지로 리다이렉트
        response.sendRedirect("/BuyBike/board/list");
    }
}
