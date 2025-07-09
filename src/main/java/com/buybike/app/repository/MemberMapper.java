package com.buybike.app.repository;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberMapper {
     List<Member> list();
     Member findByMemberId(@Param("memberId") String memberId);
     Member findById(@Param("memberId") String memberId);
     List<Member> findAll();
    // 조회
    public Member select(Long num) throws Exception;
    Long insert(Member member);
     int update(Member member);
     int deleteById(@Param("memberId") String memberId);


}