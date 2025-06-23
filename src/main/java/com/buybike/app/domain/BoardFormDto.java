package com.buybike.app.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardFormDto  {
    private Long boardId;
    private String title;
    private String content;
    private Member memberId; // 게시글 작성자의 ID
    private int hits; // 조회수
    private LocalDateTime regDt;
    private String fileName; // 업로드된 파일 이름
    private MultipartFile boardImage; // 게시판 이미지 URL
    public Board toEntity() {
        Board build = Board.builder()
                .boardId(boardId)
                .title(title)
                .content(content)
                .member(memberId) // Member 객체로 설정
                .Hits(hits)
                .regDt(regDt != null ? regDt : LocalDateTime.now()) // 현재 시간으로 설정
                .fileName(fileName)
                .boardImage(boardImage)
                .build();
        return build;
    }

    public BoardFormDto(Long boardId, String title, String content, Member memberId, int hits, LocalDateTime regDt) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.memberId = memberId; // 게시글 작성자의 ID
        this.hits = hits; // 조회수
        this.regDt = regDt != null ? regDt : LocalDateTime.now(); // 현재 시간으로 설정
    }
}


