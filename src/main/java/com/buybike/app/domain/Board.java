package com.buybike.app.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class Board {

    private Long boardId;
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    private User user;
    private LocalDateTime regDt;
    private LocalDateTime modDt;
    private String fileName;
    private MultipartFile boardImage; // 게시판 이미지
}
