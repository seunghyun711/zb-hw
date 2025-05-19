package com.spring.jpa.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.jpa.board.entity.BoardBadReport;
import com.spring.jpa.board.entity.BoardType;
import com.spring.jpa.board.model.*;
import com.spring.jpa.board.repository.BoardRepository;
import com.spring.jpa.board.repository.BoardTypeRepository;
import com.spring.jpa.board.service.BoardService;
import com.spring.jpa.common.model.ResponseResult;
import com.spring.jpa.notice.model.ResponseError;
import com.spring.jpa.user.model.ResponseMessage;
import com.spring.jpa.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiAdminBoardController {
    private final BoardService boardService;
    private final BoardTypeRepository boardTypeRepository;
    private final BoardRepository boardRepository;

    /**
     * 게시글의 신고하기 목록을 조회하는 api
     */
    @GetMapping("/api/admin/board/badreport")
    public ResponseEntity<?> badReport() {
        List<BoardBadReport> list = boardService.badReportList();

        return ResponseResult.success(list);

    }

}

