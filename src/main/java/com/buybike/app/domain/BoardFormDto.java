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
public class BoardFormDto {
    private Long no;
    private String id; // 게시글 ID
    private String title;
    private String content;
    private String memberId; // 게시글 작성자의 ID
    private int hits; // 조회수
    private LocalDateTime createdAt;
    private LocalDateTime updatedAT;
    private String fileName; // 업로드된 파일 이름
    private MultipartFile boardImage; // 게시판 이미지 URL
    private int commentCount; // [추가] 댓글 수를 저장할 필드

    public Board toEntity() {
        Board build = Board.builder()
                .no(no)
                .id(id)
                .title(title)
                .content(content)
                .memberId(memberId)
                .hits(hits)
                .createdAt(createdAt != null ? createdAt : LocalDateTime.now()) // 현재 시간으로 설정
                .fileName(fileName)
                .boardImage(boardImage)
                .commentCount(commentCount)
                .build();
        return build;
    }

    public BoardFormDto(Long no, String id, String title, String content, String memberId,int hits, LocalDateTime createdAt, int commentCount) {
        this.no = no;
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId; // 게시글 작성자의 ID
        this.hits = hits;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now(); // 현재 시간으로 설정
        this.commentCount = commentCount;
    }
}




