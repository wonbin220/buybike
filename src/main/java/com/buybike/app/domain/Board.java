package com.buybike.app.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Board {

    private Long boardId;
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    private Member member;
    private int Hits; // 조회수
    private LocalDateTime regDt;
    private String fileName;
    private MultipartFile boardImage; // 게시판 이미지

    public Board(Long boardId, String title, String content, Member member) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.member = member;
        this.Hits = 0; // 초기 조회수는 0
        this.regDt = LocalDateTime.now(); // 현재 시간으로 등록일시 설정
    }
}
