package com.spring.jpa.user.controller;

import com.spring.jpa.common.exception.BizException;
import com.spring.jpa.common.model.ResponseResult;
import com.spring.jpa.notice.model.ResponseError;
import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.model.UserLogin;
import com.spring.jpa.user.model.UserLoginToken;
import com.spring.jpa.user.service.UserService;
import com.spring.jpa.util.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiLoginController {
    private final UserService userService;
    /**
     * 회원 로그인 히스토리 기능을 구현하는 api
     */
//    @PostMapping("/api/login")
//    public ResponseEntity<?> login(@RequestBody @Valid UserLogin userLogin, Errors errors) {
//        if (errors.hasErrors()) {
//            return ResponseResult.fail("입력값이 정확하지 않습니다.", ResponseError.of(errors.getAllErrors()));
//        }
//
//        User user = null;
//        try {
//            user = userService.login(userLogin);
//        } catch (BizException e) {
//            return ResponseResult.fail(e.getMessage());
//        }
//
//        UserLoginToken userLoginToken = JwtUtils.createToken(user);
//
//        if (userLoginToken == null) {
//            return ResponseResult.fail("JWT 생성이 실패하였습니다.");
//        }
//
//        return ResponseResult.success(userLoginToken);
//    }

    /**
     * 로그인 시 에러가 발생하는 경우 로그에 기록하는 api
     */
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLogin userLogin, Errors errors) {
        log.info("# 로그인");
        if (errors.hasErrors()) {
            return ResponseResult.fail("입력값이 정확하지 않습니다.", ResponseError.of(errors.getAllErrors()));
        }

        User user = null;
        try {
            user = userService.login(userLogin);
        } catch (BizException e) {
            log.info("로그인 예외: " + e.getMessage());
            return ResponseResult.fail(e.getMessage());
        }

        UserLoginToken userLoginToken = JwtUtils.createToken(user);

        if (userLoginToken == null) {
            log.info("JWT 생성 에러: ");
            return ResponseResult.fail("JWT 생성이 실패하였습니다.");
        }

        return ResponseResult.success(userLoginToken);
    }


}
