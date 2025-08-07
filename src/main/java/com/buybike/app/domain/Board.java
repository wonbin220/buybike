package com.buybike.app.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class Board {

    private Long no;
    private String id; // 게시글 ID
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    private String memberId;
    private int hits; // 조회수
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;
    private String fileName;
    private MultipartFile boardImage; // 게시판 이미지

    public Board() {
        this.id = UUID.randomUUID().toString();

    }


}
