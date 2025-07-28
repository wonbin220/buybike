package com.buybike.app.controller;

import com.buybike.app.domain.Smarteditor;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;



@Controller
public class SimpleImageUploader {

    @PostMapping("/singleImageUploader")
    public String simpleImageUploader(HttpServletRequest request, Smarteditor smarteditor) throws UnsupportedEncodingException {
        String callback = smarteditor.getCallback();
        String callback_func = smarteditor.getCallback_func();
        String file_result = "";
        String result = "";
        MultipartFile multiFile = smarteditor.getFiledata();

        try {
            // 파일이 비어있지 않은지 확인
            if (multiFile != null && !multiFile.isEmpty()) {
                // 이미지 파일인지 확인
                if (multiFile.getContentType() != null && multiFile.getContentType().toLowerCase().startsWith("image/")) {
                    // 원본 파일명 가져오기
                    String originalFilename = multiFile.getOriginalFilename();

                    // 업로드 경로 설정
                    String uploadPath = request.getServletContext().getRealPath("/upload");
                    String path = uploadPath + "/smarteditor2/";
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs(); // 디렉토리가 없으면 생성
                    }

                    // 파일명 중복을 피하기 위해 UUID 사용
                    String extension = StringUtils.getFilenameExtension(originalFilename);
                    String savedFileName = UUID.randomUUID().toString() + "." + extension;

                    // 파일 저장
                    multiFile.transferTo(new File(path + savedFileName));

                    // 콜백에 전달할 파일 정보 생성
                    file_result += "&bNewLine=true&sFileName=" + URLEncoder.encode(originalFilename, StandardCharsets.UTF_8)
                            + "&sFileURL=/upload/smarteditor2/" + savedFileName;
                } else {
                    file_result += "&errstr=error_not_image_file";
                }
            } else {
                file_result += "&errstr=error_file_is_empty";
            }
        } catch (IOException e) {
            e.printStackTrace();
            file_result += "&errstr=error_upload_failed";
        }

        // redirect URL 생성
        result = "redirect:" + callback + "?callback_func=" + URLEncoder.encode(callback_func, StandardCharsets.UTF_8) + file_result;
        return result;
    }
}