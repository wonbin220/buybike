package com.buybike.app.repository;

import com.buybike.app.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {


    // 특정 게시글의 모든 댓글 조회 (계층 순으로 정렬)
    List<Comment> findByBoardNo(@Param("boardNo") Long boardNo);

    // 댓글 단일 조회
    Comment findByCommentNo(@Param("commentNo") Long commentNo);

    // 댓글 저장
    Long save(Comment comment);

    // 댓글 수정
    Long update(Comment comment);

    // 댓글 번호로 특정 댓글 조회
    Comment findById(Long commentNo);

    // 특정 게시글의 댓글 수 조회
    Integer countByBoardNo(Long boardNo);

    // 댓글 삭제 (is_deleted 플래그 업데이트)
    Long softDelete(@Param("commentNo") Long commentNo);
}
