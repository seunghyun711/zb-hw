package com.spring.jpa.common.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.jpa.common.exception.AuthFailException;
import com.spring.jpa.common.model.ResponseResult;
import com.spring.jpa.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class CommonInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("[인터셉터] - preHandler 스타트");

        log.info(request.getMethod());
        log.info(request.getRequestURI());
        log.info(request.getRequestURI());

        if (!validJWT(request)) {
            throw new AuthFailException("인증 정보가 정확하지 않습니다.");
        }

        return true;
    }

    private boolean validJWT(HttpServletRequest request) {
        String token = request.getHeader("hong");

        String email = "";
        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }
}
