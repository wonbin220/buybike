package com.buybike.app.service;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.Comment;
import com.buybike.app.repository.BoardMapper;
import com.buybike.app.repository.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentMapper commentMapper;
    private final BoardMapper boardMapper; // 게시글 작성자 확인용

    /**
     * 게시글의 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentList(Long boardNo, String currentUserId){
        List<Comment> comments = commentMapper.findByBoardNo(boardNo);
        String boardWriterId;

        try {
            // boardMapper.select가 Exception을 던질 수 있으므로 try-catch로 처리
            Board board = boardMapper.select(boardNo.longValue());
            if (board == null) {
                // 게시글이 없는 경우 예외 발생
                throw new IllegalArgumentException("해당 번호의 게시글을 찾을 수 없습니다: " + boardNo);
            }
            boardWriterId = board.getMemberId();
        } catch (Exception e) {
            // 예외 발생 시, 원인을 포함한 런타임 예외로 전환하여 상위로 전달
            throw new RuntimeException("게시글 작성자 정보를 조회하는 중 오류가 발생했습니다.", e);
        }

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
            Comment parent = commentMapper.findByCommentNo(comment.getParentNo());
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

    public void updateComment(Long commentNo, Comment comment, String currentUsername) {
        // 수정할 댓글의 원본 정보를 DB에서 조회합니다.
        // (findById 메소드가 CommentMapper에 정의되어 있어야 합니다.)
        Comment originalComment = commentMapper.findById(commentNo);

        if (originalComment == null) {
            throw new IllegalArgumentException("수정할 댓글을 찾을 수 없습니다.");
        }

        // 댓글 작성자와 현재 로그인한 사용자가 같은지 확인하여 수정 권한을 체크합니다.
        if (!originalComment.getWriterId().equals(currentUsername)) {
            throw new AccessDeniedException("댓글을 수정할 권한이 없습니다.");
        }

        // DTO에 수정할 정보 설정
        comment.setCommentNo(commentNo);

        // Mapper를 통해 DB 업데이트 실행
        commentMapper.update(comment);
    }

    // 특정 게시글의 댓글 수를 조회하는 서비스 메소드
    @Transactional(readOnly = true)
    public Long countCommentsByBoardNo(Long boardNo) {
        return commentMapper.countByBoardNo(boardNo);
    }
}