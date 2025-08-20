package com.buybike.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final BoardMapper boardMapper; // 게시글 작성자 확인용

    /**
     * 게시글의 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentList(Long boardNo, String currentUserId) {
        List<Comment> comments = commentMapper.findByBoardNo(boardNo);
        String boardWriterId = boardMapper.select(boardNo.intValue()).getMemberId(); // intValue()는 상황에 맞게 수정

        comments.forEach(comment -> {
            // 삭제된 댓글 처리
            if (comment.isDeleted()) {
                comment.setDisplayContent("삭제된 댓글입니다.");
            }
            // 비밀 댓글 처리
            else if (comment.isSecret()) {
                // 현재 사용자가 (게시글 작성자) 또는 (댓글 작성자)인 경우에만 내용 표시
                if (currentUserId != null && (currentUserId.equals(boardWriterId) || currentUserId.equals(comment.getWriterId()))) {
                    comment.setDisplayContent(comment.getContent());
                } else {
                    comment.setDisplayContent("비밀 댓글입니다.");
                }
            }
            // 일반 댓글
            else {
                comment.setDisplayContent(comment.getContent());
            }
        });
        return comments;
    }

    /**
     * 댓글 작성
     */
    @Transactional
    public void createComment(Comment comment) {
        // 대댓글인 경우
        if (comment.getParentNo() != null) {
            CommentDto parent = commentMapper.findByCommentNo(comment.getParentNo());
            // 부모 댓글의 depth + 1, 단 5를 초과할 수 없음
            int newDepth = parent.getDepth() + 1;
            if (newDepth > 5) {
                // 6단계 이상이면 부모의 부모를 찾아 같은 레벨로 등록 (혹은 최상위로)
                // 여기서는 요구사항에 따라 최상위 댓글로 처리
                comment.setDepth(1);
                comment.setParentNo(null);
            } else {
                comment.setDepth(newDepth);
            }
        } else {
            // 최상위 댓글
            comment.setDepth(1);
        }
        commentMapper.save(comment);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long commentNo) {
        // 자식 댓글이 있는지 확인할 필요 없이, 논리적 삭제로 처리하면
        // "삭제된 댓글입니다"라고 표시되고 자식 댓글은 그대로 유지됩니다.
        commentMapper.softDelete(commentNo);
    }
}