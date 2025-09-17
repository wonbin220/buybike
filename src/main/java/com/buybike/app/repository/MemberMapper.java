package com.buybike.app.repository;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.Member;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface MemberMapper {
     List<Member> list();
     Optional<Member> findByMemberId(@Param("memberId") String memberId);
     Member findById(@Param("memberId") String memberId);
     List<Member> findAll();
    // 조회
    public Member select(Long num) throws Exception;
    Long insert(Member member);
     int update(Member member);
     int deleteById(@Param("memberId") String memberId);

    // 회원 목록 조회 (게시글 수, 댓글 수 포함)
    Page<Member> findAllWithCounts();

}