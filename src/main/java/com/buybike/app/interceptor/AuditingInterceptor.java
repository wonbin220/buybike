package com.buybike.app.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class AuditingInterceptor implements HandlerInterceptor {
    private String user;
    private String boardId;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().endsWith("boards/add") && request.getMethod().equals("POST")) {
            user = request.getRemoteUser();
            boardId = request.getParameterValues("boardId")[0];
        }
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception arg3) throws Exception {
        if (request.getRequestURI().endsWith("boards/add")) {
            log.warn(String.format("신규 게시글 등록 ID : %s, 접근자 : %s, 접근시각 : %s", boardId, user, getCurrentTime()));
        }
    }

    private String getCurrentTime() {
        DateFormat fomatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return fomatter.format(calendar.getTime());
    }
}
