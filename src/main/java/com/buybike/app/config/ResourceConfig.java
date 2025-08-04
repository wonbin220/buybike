package com.buybike.app.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
//    @Value("${file.uploadDir}")
//    String fileDir;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/BuyBike/images/**")
//                .addResourceLocations("file:///"+fileDir)
//                .setCachePeriod(60 * 60 * 24 * 365);// 접근 파일 캐싱 시간
//    }



    // 컨트롤러에서 지정한 외부 경로
    private String resourcePath = "file:///C:/upload/"; // Windows

    // 브라우저에서 접근할 URL 경로
    private String uploadPath = "/upload/**";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadPath)
                .addResourceLocations(resourcePath);

        // 정적 리소스(css, js, images 등) 경로
        // 이 설정이 누락되면 페이지의 스타일이나 기능이 제대로 동작하지 않을 수 있습니다.
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
