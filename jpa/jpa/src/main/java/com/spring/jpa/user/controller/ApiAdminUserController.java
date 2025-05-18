package com.spring.jpa.user.controller;

import com.spring.jpa.notice.repository.NoticeRepository;
import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.entity.UserLoginHistory;
import com.spring.jpa.user.exception.UserNotFoundException;
import com.spring.jpa.user.model.*;
import com.spring.jpa.user.repository.UserLoginHistoryRepository;
import com.spring.jpa.user.repository.UserRepository;
import com.spring.jpa.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ApiAdminUserController {
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final UserLoginHistoryRepository userLoginHistoryRepository;
    private final UserService userService;

    /**
     * 사용자 목록과 사용자 수를 응답 결과로 하는 api
     */
//    @GetMapping("/api/admin/user")
//    public ResponseMessage userList() {
//        List<User> users = userRepository.findAll();
//        long totalUserCount = userRepository.count();
//
//        return ResponseMessage.builder()
//                .totalCount(totalUserCount)
//                .data(users)
//                .build();
//
//    }

    /**
     * 사용자 상세 조회 api
     * ResponseMessage 클래스로 추상화하여 전송
     */
    @GetMapping("/api/admin/user/{id}")
    public ResponseEntity<?> userDetail(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(ResponseMessage.success(user));
    }

    /**
     * 사용자 목록 조회에 대한 검색 api
     * 이메일, 이름, 전화번호에 대한 검색결과를 리턴(각 항목은 or 조건)
     */
    @GetMapping("/api/admin/user/search")
    public ResponseEntity<?> findUser(@RequestBody UserSearch userSearch) {
        // email like '%' || email || '%'
        List<User> userList = userRepository.findByEmailContainsOrPhoneContainsOrUsernameContains(userSearch.getEmail(),
                userSearch.getPhone(),
                userSearch.getUsername());

        return ResponseEntity.ok().body(ResponseMessage.success(userList));

    }

    /**
     * 사용자의 상태를 변경하는 api
     * 사용자 상태 : 정상 | 정지
     * 이에 대한 플래그 값은 사용자 상태 (정상 : Using, 정지 : Stop)
     */
    @PatchMapping("/api/admin/user/{id}/status")
    public ResponseEntity<?> userStatus(@PathVariable("id") Long id, @RequestBody UserStatusInput userStatusInput) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        user.setStatus(userStatusInput.getStatus());
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }

    /**
     * 사용자 정보 삭제 api
     * 작성된 게시글이 있으면 예외 발생 처리
     */
    @DeleteMapping("api/admin/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        User user = optionalUser.get();

        if (noticeRepository.countByUser(user) > 0) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자가 작성한 공지사항이 있습니다."), HttpStatus.BAD_REQUEST);

        }

        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 사용자가 로그인을 했을 때 이에 대한 접속 이력이 저장된다고 했을 때, 이에 대한 접속 이력을 조회하는 api
     * 접속 이력 정보가 있다는 가정하에 api 작성
     * UserLogHistory 엔티티를 통해 구현
     */
    @GetMapping("/api/admin/user/login/history")
    public ResponseEntity<?> userLoginHistory() {
        List<UserLoginHistory> userLoginHistories = userLoginHistoryRepository.findAll();

        return ResponseEntity.ok().body(userLoginHistories);
    }

    /**
     * 사용자의 접속을 제한하는 api
     */
    @PatchMapping("/api/admin/user/{id}/lock")
    public ResponseEntity<?> userLock(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        if (user.isLockYn()) {
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속제한이 된 사용자입니다."), HttpStatus.BAD_REQUEST);
        }

        user.setLockYn(true);
        userRepository.save(user);

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 사용자의 접속 제한을 해제하는 api
     */
    @PatchMapping("/api/admin/user/{id}/unlock")
    public ResponseEntity<?> userUnLock(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        if (!user.isLockYn()) {
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속제한이 해제된 사용자입니다."), HttpStatus.BAD_REQUEST);
        }

        user.setLockYn(false);
        userRepository.save(user);

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 회원 전체수와 상태별 회원수에 대한 정보를 리턴하는 api
     * 서비스 클래스를 이용해 작성
     */
    @GetMapping("/api/admin/user/status/count")
    public ResponseEntity<?> userStatusCount() {
        UserSummary userSummary = userService.getUserStatusCount();
        return ResponseEntity.ok().body(userSummary);
    }

    /**
     * 오늘의 사용자 가입 목록을 리턴하는 api
     */
    @GetMapping("/api/admin/user/today")
    public ResponseEntity<?> todayUser() {
        List<User> users = userService.getTodayUsers();

        return ResponseEntity.ok().body(ResponseMessage.success(users));
    }

    /**
     * 사용자별 공지사항의 게시글 수를 리턴하는 api
     */
    @GetMapping("/api/admin/user/notice/count")
    public ResponseEntity<?> userNoticeCount() {
        List<UserNoticeCount> userNoticeCounts = userService.getUserNoticeCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userNoticeCounts));

    }

    /**
     * 사용자의 게시글 수와 좋아요 수를 리턴하는 api
     */
    @GetMapping("/api/admin/user/log/count")
    public ResponseEntity<?> userLogCount() {
        List<UserLogCount> userNoticeCounts = userService.getUserLogCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userNoticeCounts));
    }

    /**
     * 좋아요를 가장많이 한 사용자 목록(10개)을 리턴하는 api
     */
    @GetMapping("/api/admin/user/like/best")
    public ResponseEntity<?> bestLikeCount() {
        List<UserLogCount> userNoticeCounts = userService.getBestLikeUser();

        return ResponseEntity.ok().body(ResponseMessage.success(userNoticeCounts));
    }
}

