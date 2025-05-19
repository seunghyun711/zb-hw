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
public class ApiBoardController {
    private final BoardService boardService;
    private final BoardTypeRepository boardTypeRepository;
    private final BoardRepository boardRepository;

    /**
     * 게시판 타입을 추가하는 api
     * 동일한 게시판 제목이 있는 경우 status:200, result:false, message에 이미 존재하는 게시판이라는 메시지 리턴
     * 게시판 이름은 필수항목에 대한 부분 체크
     */
    @PostMapping("/api/board/type")
    public ResponseEntity<?> addBoardType(@RequestBody BoardTypeInput boardTypeInput, Errors errors) {
        if (errors.hasErrors()) {
            List<ResponseError> responseErrors = ResponseError.of(errors.getAllErrors());

            return new ResponseEntity<>(ResponseMessage.fail("입력값이 정확하지 않습니다.", responseErrors), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.addBoard(boardTypeInput);

        if (!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 게시판 타입명을 수정하는 api
     * 게시판명이 동일한 경우 "수정할 이름이 동일한 게시판명입니다." 리턴
     */
    @PutMapping("/api/board/type/{id}")
    public ResponseEntity<?> updateBoardType(@PathVariable("id") Long id, @RequestBody BoardTypeInput boardTypeInput, Errors errors) {
        if (errors.hasErrors()) {
            List<ResponseError> responseErrors = ResponseError.of(errors.getAllErrors());

            return new ResponseEntity<>(ResponseMessage.fail("입력값이 정확하지 않습니다.", responseErrors), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.updateBoard(id, boardTypeInput);

        if (!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 게시판 타입 삭제하는 api
     * 삭제시 게시글이 있는 경우 삭제 불가
     */
    @DeleteMapping("/api/board/type/{id}")
    public ResponseEntity<?> deleteBoardType(@PathVariable("id") Long id) {

        ServiceResult result = boardService.deleteBoard(id);

        if (!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 게시판 타입의 목록을 리턴하는 api
     */
    @GetMapping("/api/board/type")
    public ResponseEntity<?> boardType() {
        List<BoardType> boardTypeList = boardService.getAllBoardType();

        return ResponseEntity.ok().body(ResponseMessage.success(boardTypeList));
    }

    /**
     * 게시판 타입의 사용여부를 설정하는 api
     */
    @PatchMapping("/api/board/type/{id}/using")
    public ResponseEntity<?> usingBoardType(@PathVariable("id") Long id, @RequestBody BoardTypeUsing boardTypeUsing) {

        ServiceResult result = boardService.setBoardTypeUsing(id, boardTypeUsing);

        if (!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 게시판별 작성한 게시글의 개수를 리턴하는 api
     * 현재 사용가능한 게시판에 대해 게시글의 개수를 리턴
     */
    @GetMapping("/api/board/type/count")
    public ResponseEntity<?> boardTypeCount() {
        List<BoardTypeCount> list = boardService.getBoardTypeCount();

        return ResponseEntity.ok().body(list);
    }

    /**
     * 게시된 게시글을 최상단에 배치하는 api
     */
    @PatchMapping("/api/board/{id}/top")
    public ResponseEntity<?> boardPostTop(@PathVariable("id") Long id) {
        ServiceResult result = boardService.setBoardTop(id, true);

        return ResponseEntity.ok().body(result);

    }

    /**
     * 최상단에 게시된 게시글을 최상단에서 해제하는 api
     */
    @PatchMapping("/api/board/{id}/top/clear")
    public ResponseEntity<?> boardPostTopClear(@PathVariable("id") Long id) {
        ServiceResult result = boardService.setBoardTop(id, false);

        return ResponseEntity.ok().body(result);
    }

    /**
     * 게시글의 게시기간을 시작일과 종료일로 설정하는 api
     */
    @PatchMapping("/api/board/{id}/publish")
    public ResponseEntity<?> boardPeriod(@PathVariable("id") Long id, @RequestBody BoardPeriod boardPeriod) {
        ServiceResult result = boardService.setBoardPeriod(id, boardPeriod);

        if (!result.isResult()) {
            return ResponseResult.fail(result.getMessage());
        }
        return ResponseResult.success();
    }

    /**
     * 게시글의 조회수를 증가시키는 api
     * 동일 사용자 게시글 조회수 증가를 방지하는 부분의 로직도 구현할 것
     * jwt 인증을 통과한 사용자에 대해 진행
     */
    @PutMapping("/api/board/{id}/hits")
    public ResponseEntity<?> boardHits(@PathVariable("id") Long id,
                                       @RequestHeader("hong") String token) {

        String email = "";
        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 일치하지 않습니다.");
        }

        ServiceResult result = boardService.setBoardHits(id, email);
        if (result.isResult()) {
            return ResponseResult.fail(result.getMessage());
        }
        return ResponseResult.success();
    }

    /**
     * 게시글 좋아요 기능 api
     */
    @PutMapping("/api/board/{id}/like")
    public ResponseEntity<?> boardLike(@PathVariable("id") Long id,
                                       @RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }
        ServiceResult result = boardService.setBoardLike(id, email);
        return ResponseResult.result(result);
    }

    /**
     * 게시글의 좋아요한 내용을 취소하는 api
     */
    @PutMapping("/api/board/{id}/unlike")
    public ResponseEntity<?> boardUnLike(@PathVariable("id") Long id,
                                       @RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }
        ServiceResult result = boardService.setBoardUnLike(id, email);
        return ResponseResult.result(result);
    }

    /**
     * 게시된 게시글에 대해 문제가 있는 게시글을 신고하는 api
     */
    @PutMapping("/api/board/{id}/badreport")
    public ResponseEntity<?> boardBadReport(@PathVariable("id") Long id,
                                            @RequestHeader("hong") String token,
                                            @RequestBody BoardBadReportInput boardBadReportInput) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.addBadReport(id, email, boardBadReportInput);
        return ResponseResult.result(result);
    }

}

