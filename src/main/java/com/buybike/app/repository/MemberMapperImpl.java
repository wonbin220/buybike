package com.buybike.app.repository;

import com.buybike.app.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
@RequiredArgsConstructor
public class MemberMapperImpl implements MemberMapper {

    private final MemberMapper memberMapper;


    @Override
    public List<Member> list() {
        return memberMapper.list();
    }

    @Override
    public Member findByMemberId(String memberId) {
        return memberMapper.findByMemberId(memberId);
    }

    @Override
    public Member findById(String memberId) {
        return memberMapper.findById(memberId);
    }

    @Override
    public List<Member> findAll() {
        return memberMapper.findAll();
    }

    @Override
    public Long insert(Member member) {
        return memberMapper.insert(member);
    }

    @Override
    public int update(Member member) {
        return memberMapper.update(member);
    }

    @Override
    public int deleteById(String memberId) {
        return memberMapper.deleteById(memberId);
    }
}