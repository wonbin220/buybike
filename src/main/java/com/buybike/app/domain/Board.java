package com.buybike.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Board {

    private Long boardId;
    private String title;
    private String content;
    private User user;
    private LocalDateTime regDt;
    private LocalDateTime modDt;

    public Board() {
        super();
    }
}
