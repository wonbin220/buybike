package com.buybike.app.controller;

import com.buybike.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
        List<CommentDto> comments = commentService.getCommentList(boardNo, currentUserId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody Comment comment,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        commentDto.setWriterId(userDetails.getUsername());
        commentService.createComment(commentDto);
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
}