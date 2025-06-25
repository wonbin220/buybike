package com.buybike.app.domain;

import lombok.*;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PageList<T> {

    private T data;
    private Pageable pageable;
}
