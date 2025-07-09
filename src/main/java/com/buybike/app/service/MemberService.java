package com.buybike.app.service;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.Member;
import com.buybike.app.repository.MemberMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberMapper memberMapper;


    public List<Member> getAllMemberList() {
        return memberMapper.list();
    }

    public Long saveMember(Member member) { // 회원 정보 저장하기
        validateDuplicateMember(member);
        return memberMapper.insert(member);
    }

    public int updateMember(Member member) { // 회원 정보 저장하기
        return memberMapper.update(member);
    }

    public Member getMemberById(String memberId) { // 회원 정보 가져오기
        Member member = memberMapper.findByMemberId(memberId);
        return member;
    }
    public void deleteMember(String memberId) { // 회원 삭제하기
        Member member = memberMapper.findByMemberId(memberId);
        memberMapper.deleteById(member.getMemberId());
    }

    public boolean findByMemberId(String memberId) {
        return memberMapper.findByMemberId(memberId) != null;
    }

    public Member select(Long num) throws Exception {
        return memberMapper.select(num);
    }



    private void validateDuplicateMember(Member member) { // 회원 id 중복 체크하기
        Member findMember = memberMapper.findByMemberId(member.getMemberId());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // 인증 시 회원 정보 가져오기

    @Override
    public UserDetails loadUserByUsername(String id) throws
            UsernameNotFoundException {
        Member member = memberMapper.findByMemberId(id);
        if(member == null) {
            throw new UsernameNotFoundException(id);
        }
        return User.builder()
                .username(member.getMemberId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    // PageHelper를 사용한 페이징 처리
    public PageInfo<Member> page(int page, int size) throws Exception {
        // PageHelper.startPage(page, size); (현재번호, 페이지당 데이터 수)
        PageHelper.startPage(page, size);
        List<Member> list = memberMapper.list();

        // PageInfo(리스트, 노출 페이지 수)
        PageInfo<Member> pageInfo = new PageInfo<>(list, 10);
        return pageInfo;
    }
}