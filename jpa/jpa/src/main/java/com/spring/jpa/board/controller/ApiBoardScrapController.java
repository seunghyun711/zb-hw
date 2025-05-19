package com.spring.jpa.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
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
public class ApiBoardScrapController {
    private final BoardService boardService;

    /**
     * 게시글에 스크랩을 추가하는 api
     */
    @PutMapping("/api/board/{id}/scrap")
    public ResponseEntity<?> boardScrap(@PathVariable("id") Long id,
                                        @RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }
        return ResponseResult.result(boardService.scrapBoard(id, email));
    }


    /**
     * 게시글의 스크랩을 삭제하는 api
     */
    @DeleteMapping("/api/board/{id}/scrap")
    public ResponseEntity<?> deleteBoardScrap(@PathVariable("id") Long id,
                                              @RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        return ResponseResult.result(boardService.removeScrap(id, email));
    }

}

