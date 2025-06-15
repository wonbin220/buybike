package com.buybike.app.repository;

import com.buybike.app.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{

    private final MemberRepository memberRepository;


    @Override
    public Member findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Long insert(Member member) {
        return memberRepository.insert(member);
    }

    @Override
    public int update(Member member) {
        return memberRepository.update(member);
    }

    @Override
    public int deleteById(Long id) {
        return memberRepository.deleteById(id);
    }
}
