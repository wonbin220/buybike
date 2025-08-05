package com.buybike.app.controller;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.Member;
import com.buybike.app.domain.MemberFormDto;
import com.buybike.app.domain.Pagination;
import com.buybike.app.service.MemberService;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 신규 회원 등록페이지 출력하기
    @GetMapping(value = "/add")
    public String requestAddMemberForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
            return "redirect:/";
        }
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/addMember";
    }
    // 신규 회원 등록하기
    @PostMapping("/add")
    public String submitAddNewMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/addMember";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/addMember";
        }
        return "redirect:/memberList";
    }

    // 회원 목록 페이지 출력하기
    @GetMapping("/list")
    public String list(Model model, Pagination pagination) throws Exception {
        int page = (int) pagination.getPage();
        int size = (int) pagination.getSize();
        PageInfo<Member> memberPageInfo = memberService.page(page, size);
        log.info("memberPageInfo: {}", memberPageInfo);
        model.addAttribute("memberPageInfo", memberPageInfo);

        // Uri 빌더
        String memberPageUri = UriComponentsBuilder.fromPath("/member/memberList")
                .queryParam("size", memberPageInfo.getSize())
                .queryParam("count", memberPageInfo.getPageSize())
                .build()
                .toUriString();

        model.addAttribute("memberPageUri", memberPageUri);
        log.info("memberPageUri: {}", memberPageUri);
        return "member/memberList";
    }

    // 회원 상세 화면
    @GetMapping("/memberView/{num}")
    public String view(@PathVariable("num") Long num, Model model) throws Exception {
        // 데이터 요청
        Member member = memberService.select(num);
        // 모델 등록
        model.addAttribute("member", member);
        return "member/memberView";
    }

    // 회원 정보 수정 페이지 출력하기
    @GetMapping(value= "/update/{memberId}")
    public String requestUpdateMemberForm(@PathVariable(name = "memberId") String memberId, Model model) {

        Member member = memberService.getMemberById(memberId);
        model.addAttribute("memberFormDto", member);
        return "member/updateMember";
    }

    // 회원 정보 수정하기
    @PostMapping(value = "/update")
    public String submitUpdateMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/updateMember";
        }
        try {
            // 기존 회원 조회
            Member existingMember = memberService.getMemberById(memberFormDto.getMemberId());
            if (existingMember == null) {
                model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
                return "member/updateMember";
            }
            // 필요한 필드만 업데이트
            existingMember.setMemberName(memberFormDto.getMemberName());
            existingMember.setEmail(memberFormDto.getEmail());
            existingMember.setPhone(memberFormDto.getPhone());
            existingMember.setAddress(memberFormDto.getAddress());
            // 비밀번호 변경이 필요한 경우만 처리
            if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().isEmpty()) {
                existingMember.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
            }
            memberService.updateMember(existingMember);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/updateMember";
        }
        return "redirect:/member/list";
    }
    // 회원 정보 삭제하기
    @GetMapping("/delete/{memberId}")
    public String deleteMember(@PathVariable(name = "memberId") String memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/logout";
    }
    // 회원 가입 및 인증 시 인사말 페이지로 이동하기
    @GetMapping
    public String requestMain() {
        return "redirect:/";
    }
}
