package com.spring.jpa.notice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.spring.jpa.board.model.ServiceResult;
import com.spring.jpa.common.model.ResponseResult;
import com.spring.jpa.notice.entity.Notice;
import com.spring.jpa.notice.entity.NoticeLike;
import com.spring.jpa.notice.model.NoticeResponse;
import com.spring.jpa.notice.model.ResponseError;
import com.spring.jpa.notice.repository.NoticeLikeRepository;
import com.spring.jpa.notice.repository.NoticeRepository;
import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.exception.ExistsEmailException;
import com.spring.jpa.user.exception.PasswordNotMatchException;
import com.spring.jpa.user.exception.UserNotFoundException;
import com.spring.jpa.user.model.*;
import com.spring.jpa.user.repository.UserRepository;
import com.spring.jpa.user.service.UserService;
import com.spring.jpa.util.JwtUtils;
import com.spring.jpa.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiUserInterestController {
    private final UserService userService;

    /**
     * 관심사용자에 등록하는 api
     */
    @PutMapping("/api/user/{id}/interest")
    public ResponseEntity<?> interestUser(@PathVariable("id") Long id,
                                          @RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = userService.addInterestUser(email, id);
        return ResponseResult.result(result);
    }

    /**
     * 관심사용자에서 관심사용자를 삭제하는 api
     */
    public ResponseEntity<?> deleteInterestUser(@PathVariable("id") Long id, @RequestHeader("hong") String token){
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = userService.removeInterestUser(email, id);
        return ResponseResult.result(result);
    }


}
