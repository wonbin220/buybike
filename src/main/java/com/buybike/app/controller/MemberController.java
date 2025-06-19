package com.buybike.app.controller;

import com.buybike.app.domain.Member;
import com.buybike.app.domain.MemberFormDto;
import com.buybike.app.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;
    // 신규 회원 등록페이지 출력하기
    @GetMapping(value = "/add")
    public String requestAddMemberForm(Model model) {
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
        return "redirect:/members";
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
        return "redirect:/members";
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
