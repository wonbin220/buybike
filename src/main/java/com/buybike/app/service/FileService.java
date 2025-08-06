package com.buybike.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    // 파일이 업로드될 경로입니다. 실제 환경에 맞게 수정해야 합니다.
    // 예: "C:/upload/" 또는 "/var/www/upload/"
    private final String uploadPath = "C:/upload/";

    /**
     * MultipartFile을 서버에 저장하고, 고유한 파일 이름을 반환합니다.
     * @param multipartFile 업로드할 파일
     * @return 저장된 고유 파일 이름
     * @throws IOException 파일 저장 실패 시
     */
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        // 파일 이름 충돌을 방지하기 위해 UUID를 사용합니다.
        String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        File uploadDir = new File(uploadPath);
        // 업로드 디렉토리가 없으면 생성합니다.
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File dest = new File(uploadPath + savedFileName);
        multipartFile.transferTo(dest); // 파일을 지정된 경로에 저장합니다.

        return savedFileName;
    }

    /**
     * 파일 이름으로 서버에 저장된 파일을 삭제합니다.
     * @param fileName 삭제할 파일의 이름
     */
    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }
        File file = new File(uploadPath + fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}