package com.buybike.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
public class SmarteditorMultiImageUpload {

    // 1. 파일을 저장할 외부 경로 (Windows 환경 기준)
    // 실제 서버 환경에 맞게 경로를 수정해야 합니다.
    private final String UPLOAD_PATH = "C:/upload/smarteditor2/";

    @PostMapping("/smarteditorMultiImageUpload")
    public void smarteditorMultiImageUpload(HttpServletRequest request, HttpServletResponse response) {
        // 응답 스트림을 미리 준비합니다.
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String originalFilename = request.getHeader("file-name");

            // 파일명이 없는 경우 에러 처리
            if (originalFilename == null || originalFilename.isEmpty()) {
                writer.print("ERROR: Missing file-name header");
                return;
            }

            // 파일 확장자 검증
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "bmp");

            if (!allowedExtensions.contains(extension)) {
                writer.print("NOTALLOW_" + originalFilename);
                return;
            }

            // 저장할 디렉토리 생성
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 고유한 파일명 생성
            String savedFilename = UUID.randomUUID().toString() + "." + extension;
            File targetFile = new File(uploadDir, savedFilename);

            // 안정적인 파일 스트림 처리 (try-with-resources 사용)
            try (InputStream in = request.getInputStream();
                 OutputStream out = new FileOutputStream(targetFile)) {
                byte[] buffer = new byte[8192]; // 8KB 버퍼
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // 스마트에디터에 콜백 정보 전송
            // getContextPath()를 사용하여 동적으로 컨텍스트 경로를 가져옵니다.
            String contextPath = request.getContextPath();
            String fileUrl = contextPath + "/upload/smarteditor2/" + savedFilename;
            String fileInfo = "&bNewLine=true&sFileName=" + originalFilename + "&sFileURL=" + fileUrl;
            writer.print(fileInfo);

        } catch (IOException e) {
            // 예외 발생 시 로그를 남기고 클라이언트에 에러 메시지 전송
            e.printStackTrace();
            if (writer != null) {
                writer.print("ERROR: " + e.getMessage());
            }
        } finally {
            // PrintWriter 스트림을 안전하게 닫습니다.
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }



//    @RequestMapping(value="smarteditorMultiImageUpload")
//    public void smarteditorMultiImageUpload(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            // 1. 파일 정보 수신
//            String originalFilename = request.getHeader("file-name");
//            String fileSize = request.getHeader("file-size");
//
//            // 2. 파일 확장자 검증
//            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
//            List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "bmp");
//
//            if (!allowedExtensions.contains(extension)) {
//                try (PrintWriter writer = response.getWriter()) {
//                    writer.print("NOTALLOW_" + originalFilename);
//                    writer.flush();
//                }
//                return;
//            }
//
//            // 3. 파일 저장 경로 설정
//            // 웹 애플리케이션 루트의 'upload/smarteditor2' 폴더에 저장
//            String realPath = request.getServletContext().getRealPath("/upload/smarteditor2/");
//            File uploadDir = new File(realPath);
//            if (!uploadDir.exists()) {
//                uploadDir.mkdirs(); // 디렉토리가 없으면 생성
//            }
//
//            // 4. 고유한 파일명 생성
//            String savedFilename = UUID.randomUUID().toString() + "." + extension;
//            File targetFile = new File(uploadDir, savedFilename);
//
//            // 5. 파일 저장 (try-with-resources로 안전하게 스트림 관리)
//            try (InputStream in = request.getInputStream();
//                 OutputStream out = new FileOutputStream(targetFile)) {
//
//                byte[] buffer = new byte[Integer.parseInt(fileSize)];
//                int bytesRead;
//                // 요청 본문(이미지 데이터)을 읽어 파일에 쓴다.
//                while ((bytesRead = in.read(buffer)) != -1) {
//                    out.write(buffer, 0, bytesRead);
//                }
//                out.flush();
//            }
//
//            // 6. 스마트에디터 콜백 처리
//            // 클라이언트에게 파일 정보를 JSON 형태로 반환
//            response.setContentType("text/plain; charset=UTF-8");
//            try (PrintWriter writer = response.getWriter()) {
//                // 웹 접근 가능한 URL 경로 생성
//                String fileUrl = request.getContextPath() + "/upload/smarteditor2/" + savedFilename;
//
//                String fileInfo = "&bNewLine=true&sFileName=" + originalFilename + "&sFileURL=" + fileUrl;
//                writer.print(fileInfo);
//                writer.flush();
//            }
//
//        } catch (IOException e) {
//            // 예외 처리
//            e.printStackTrace();
//            try (PrintWriter writer = response.getWriter()) {
//                writer.print("ERROR");
//                writer.flush();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
}