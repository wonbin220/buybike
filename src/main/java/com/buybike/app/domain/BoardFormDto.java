package com.buybike.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardFormDto  {
    private Long boardId;
    private String title;
    private String content;
    private String memberId; // 게시글 작성자의 ID
    private LocalDateTime regDt;
    private String fileName; // 업로드된 파일 이름
    private String boardImage; // 게시판 이미지 URL

    public BoardFormDto(Long boardId, String title, String content, String memberId, LocalDateTime regDt, String fileName, String boardImage) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.regDt = regDt;
        this.fileName = fileName;
        this.boardImage = boardImage;
    }
}
