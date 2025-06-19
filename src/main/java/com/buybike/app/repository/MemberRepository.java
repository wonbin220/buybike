package com.buybike.app.repository;

import com.buybike.app.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberRepository {
     Member findByMemberId(@Param("memberId") String memberId);
     Member findById(@Param("memberId") String memberId);
     List<Member> findAll();
     Long insert(Member member);
     int update(Member member);
     int deleteById(@Param("memberId") String memberId);


}