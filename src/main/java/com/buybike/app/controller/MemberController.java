package com.buybike.app.controller;

import com.buybike.app.domain.*;
import com.buybike.app.service.BoardService;
import com.buybike.app.service.MemberService;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardService boardService;

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
        // 회원 목록 페이지로 리다이렉트하도록 수정합니다.
        return "redirect:/member/list";
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

//        Member member = memberService.getMemberById(memberId);
//        model.addAttribute("memberFormDto", member);
        Member member = memberService.getMemberById(memberId);
        // Member 객체를 MemberFormDto로 변환하거나, Member 객체 그대로 사용
        // 여기서는 Member 객체를 그대로 사용한다고 가정
        model.addAttribute("memberFormDto", member);
        model.addAttribute("memberId", memberId); // 삭제 링크를 위해 추가
        return "member/updateMember";
    }

    // 회원 정보 수정하기(Form 방식)
    @PostMapping(value = "/update")
    public String submitUpdateMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model, Authentication authentication) {
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

            // ADMIN 권한을 가진 사용자만 role 변경 가능
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                if (memberFormDto.getRole() != null) {
                    existingMember.setRole(memberFormDto.getRole());
                }
            }

            memberService.updateMember(existingMember);

            // 수정 후 리다이렉트: ADMIN은 회원 목록으로, 일반 사용자는 메인 페이지로
            if (isAdmin) {
                return "redirect:/member/list";
            } else {
                return "redirect:/";
            }
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("memberId", memberFormDto.getMemberId());
            return "member/updateMember";
        }
    }


    /**
     * 회원 역할 수정 (AJAX 요청 처리)
     * @param roleUpdateDto 수정할 회원 정보 (JSON)
     * @return 수정 결과
     */
    @PostMapping("/updateRole")
    @ResponseBody
    public ResponseEntity<?> updateMemberRole(@RequestBody RoleUpdateDto roleUpdateDto) {
        try {
            Member existingMember = memberService.getMemberById(roleUpdateDto.getMemberId());
            if (existingMember == null) {
                return ResponseEntity.badRequest().body("존재하지 않는 회원입니다.");
            }
            // 역할(Role)만 업데이트
            existingMember.setRole(roleUpdateDto.getRole());
            int result = memberService.updateMember(existingMember);

            if (result > 0) {
                return ResponseEntity.ok().body("역할이 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.internalServerError().body("역할 변경에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("Error updating member role", e);
            return ResponseEntity.internalServerError().body("오류가 발생했습니다.");
        }
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



    /**
     * 특정 회원이 작성한 게시글 목록을 반환합니다. (AJAX 요청 처리)
     * @param memberId 회원 아이디
     * @return 게시글 목록 (JSON)
     */
    @GetMapping("/posts/{memberId}")
    @ResponseBody
    public ResponseEntity<List<Board>> getPostsByMember(@PathVariable("memberId") String memberId) {
        try {
            // BoardService를 통해 memberId로 게시글 목록 조회
            List<Board> posts = boardService.getPostsByMemberId(memberId);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            log.error("Error fetching posts for memberId: {}", memberId, e);
            // 오류 발생 시 서버 내부 오류 응답 반환
            return ResponseEntity.internalServerError().build();
        }
    }
}
