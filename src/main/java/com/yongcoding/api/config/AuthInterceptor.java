package com.yongcoding.api.config;

import com.yongcoding.api.exception.Unauthorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    // handler 가기 전에 실행되는 메소드 (return false시 컨트롤러까지 가지않음)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        log.info(">> preHandle");

        String accessToken = request.getParameter("accessToken");
        if (accessToken != null && accessToken.equals("testToken")) {
            return true;
        }

        throw new Unauthorized();
    }

    // handler 가고 난 후에 실행되는 메소드
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info(">> postHandle");
    }

    // 요청이 완료된 후(뷰까지 보낸 후)에 실행되는 메소드
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        log.info(">> afterCompletion");
    }
}
