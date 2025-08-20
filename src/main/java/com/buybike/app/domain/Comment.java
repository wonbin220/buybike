package com.buybike.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Comment {
    private Long commentNo;
    private Long boardNo;
    private Long parentNo;
    private int depth;
    private String content;
    private String writerId;
    private boolean isDeleted;
    private boolean isSecret;
    private LocalDateTime createdAt;

    // 비밀 댓글, 삭제된 댓글 등을 화면에 표시할 때 사용할 필드
    private String displayContent;
}
