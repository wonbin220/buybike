package com.buybike.app.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;



@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Smarteditor {
    private MultipartFile filedata;
    private String callback;
    private String callback_func;
}
