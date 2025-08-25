package com.buybike.app.controller;

import com.buybike.app.domain.Comment;
import com.buybike.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@RequestParam("boardNo") Long boardNo,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        String currentUserId = (userDetails != null) ? userDetails.getUsername() : null;
        List<Comment> comments = commentService.getCommentList(boardNo, currentUserId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody Comment comment,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        comment.setWriterId(userDetails.getUsername());
        commentService.createComment(comment);
        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    // 댓글 삭제
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentNo") Long commentNo,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        // 실제로는 서비스단에서 댓글 작성자와 현재 사용자가 같은지 확인하는 로직이 추가되어야 합니다.
        // 예: commentService.deleteComment(commentNo, userDetails.getUsername());
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        commentService.deleteComment(commentNo);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @PutMapping("/{commentNo}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentNo,
                                                @RequestBody Comment comment,
                                                Principal principal) {
        try {
            // URL의 commentNo와 현재 로그인한 사용자 정보를 서비스로 전달
            commentService.updateComment(commentNo, comment, principal.getName());
            return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
        } catch (AccessDeniedException e) {
            // 수정 권한이 없는 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // 수정할 댓글이 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // 그 외 서버 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 수정 중 오류가 발생했습니다.");
        }
    }

}