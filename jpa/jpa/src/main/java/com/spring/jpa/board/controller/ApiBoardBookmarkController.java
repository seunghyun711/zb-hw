package com.spring.jpa.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.spring.jpa.board.service.BoardService;
import com.spring.jpa.common.model.ResponseResult;
import com.spring.jpa.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ApiBoardBookmarkController {
    private final BoardService boardService;

    /**
     * 게시글의 북마크를 추가/삭제하는 api
     */
    @PutMapping("/api/board/{id}/bookmark")
    public ResponseEntity<?> boardBookmark(@PathVariable("id") Long id,
                                        @RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }
        return ResponseResult.result(boardService.addBookmark(id, email));

    }

    @DeleteMapping("/api/board/{id}/bookmark")
    public ResponseEntity<?> deleteBookmark(@PathVariable("id") Long id,
                                              @RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        return ResponseResult.result(boardService.removeBookmark(id, email));
    }
}

