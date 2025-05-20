package com.spring.jpa.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.spring.jpa.board.entity.Board;
import com.spring.jpa.board.entity.BoardComment;
import com.spring.jpa.board.model.ServiceResult;
import com.spring.jpa.board.service.BoardService;
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
import com.spring.jpa.user.service.PointService;
import com.spring.jpa.util.JwtUtils;
import com.spring.jpa.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.spi.ResultSetReturn;
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
public class ApiUserController {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeLikeRepository noticeLikeRepository;
    private final BoardService boardService;
    private final PointService pointService;

    // 사용자 등록시 입력값이 유효하지 않은 경우 예외 발생
    @GetMapping("/v1/api/user")
    public ResponseEntity<?> addUserV1(@RequestBody @Valid UserInput user, Errors errors){
        // 사용자 정의 에러 모델 사용
        List<ResponseError> responseErrors = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrors.add(ResponseError.of((FieldError) e));
            });

            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    // 사용자 입력을 받아 저장하는 api
    @GetMapping("/v2/api/user")
    public ResponseEntity<?> addUserV2(@RequestBody @Valid UserInput userInput, Errors errors){
        // 사용자 정의 에러 모델 사용
        List<ResponseError> responseErrors = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrors.add(ResponseError.of((FieldError) e));
            });

            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .username(userInput.getUsername())
                .password(userInput.getPassword())
                .phone(userInput.getPhone())
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 정보 수정 api
     * 사용자 정보가 없는 경우 UserNotFoundException 발생
     * 에러 메시지는 "사용자 정보가 없습니다.
     * 수정 정보는 연락처만 수정 가능, 수정 일자는 현재 날짜
     */
    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserUpdate userUpdate, Errors errors) {

        // 사용자 정의 에러 모델 사용
        List<ResponseError> responseErrors = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrors.add(ResponseError.of((FieldError) e));
            });

            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        user.setPhone(userUpdate.getPhone());
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 정보 조회 API
     * 사용자의 정보 조회
     * 보안상 사용자의 비밀번호, 가입일, 회원정보 수정일은 응답에 포함하지 않는다.
     */
    @GetMapping("/api/user/{id}")
    public UserResponse getUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        UserResponse userResponse = UserResponse.of(user);

        return userResponse;
    }

    /**
     * 작성한 공지사항 목록 조회 api
     * 보안상 삭제한 아이디와 삭제 일자는 응답에 포함하지 않는다.
     * 작성자 정보를 응답에  모두 포함하지 않고, 작성자의 아이디와 이름만 응답에 포함한다.
     */
    @GetMapping("/api/user/{id}/notice")
    public List<NoticeResponse> userNotice(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        List<Notice> noticeList = noticeRepository.findByUser(user);

        List<NoticeResponse> noticeResponses = new ArrayList<>();
        noticeList.stream().forEach((e) -> {
            noticeResponses.add(NoticeResponse.of(e));
        });

        return noticeResponses;
    }

    /**
     * 사용자 등록시 이미 존재하는 이메일(이메일은 유일)인 경우 예외를 발생시키는 API
     * 동일한 이메일에 가입된 회원정보가 존재하는 경우 ExistsEmailException 발생
     */
    @PostMapping("/v3/api/user")
    public ResponseEntity<?> addUserV3(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .username(userInput.getUsername())
                .phone(userInput.getPhone())
                .password(userInput.getPassword())
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 비밀번호 변경 api
     * 이전 비밀번호와 일치하는 경우 수정 가능
     * 일치하지 않는 경우 PasswordNotMatchException 발생
     * 발생 메시지는 "비밀번호가 일치하지 않습니다."
     */
    @PatchMapping("/api/user/{id}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable("id") Long id,
                                                @RequestBody UserInputPassword userInputPassword, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByIdAndPassword(id, userInputPassword.getPassword())
                .orElseThrow(() -> new PasswordNotMatchException("비밀번호가 일치하지 않습니다."));

        user.setPassword(userInputPassword.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }

    /**
     * 회원가입 시 비밀번호를 암호화하여 저장하난 api
     */
    @PostMapping("/v4/api/user")
    public ResponseEntity<?> addUserV4(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("이미 존재하는 이메일입니다.");
        }



        User user = User.builder()
                .email(userInput.getEmail())
                .username(userInput.getUsername())
                .phone(userInput.getPhone())
                .password(getEncryptPassword(userInput.getPassword()))
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 회원 탈퇴 api
     * 회원정보가 존재하지 않은 경우 예외처리
     * 사용자가 등록한 공지사항이 존재하는 경우 회원삭제 불가
     */
    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        try {
            userRepository.delete(user);
        } catch (DataIntegrityViolationException e) {
            String message = "제약조건에 문제가 발생했습니다.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            String message = "회원탈퇴 중 문제가 발생했습니다.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();

    }

    /**
     * 사용자 이메일을 찾는 api
     * 이름과 전화번호에 해당하는 이메일을 찾는다.
     */
    @GetMapping("/api/user")
    public ResponseEntity<UserResponse> findUser(@RequestBody UserInputFind userInputFind) {
        User user = userRepository.findByUsernameAndPhone(userInputFind.getUsername(), userInputFind.getPhone())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        UserResponse userResponse = UserResponse.of(user);

        return ResponseEntity.ok().body(userResponse);
    }

    /**
     * 사용자 비밀번호 초기화 요청 api
     * 아이디에 대한 정보 조회 후 비밀번호를 초기화한 이후에 이를 문자 전송하는 로직 호출
     * 초기화 비밀번호는 문자열 10자로 설정함
     */
    @GetMapping("/api/user/{id}/password/reset")
    public ResponseEntity<?> requestUserPassword(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        // 비밀번호 초기화
        String resetPassword = getResetPassword();
        String resetEncryptPassword = getEncryptPassword(resetPassword);
        user.setPassword(resetEncryptPassword);

        userRepository.save(user);

        String message = String.format("[%s]님의 임시 비밀번호는 [%s]로 초기화 되었습니다.", user.getUsername(), resetPassword);
        sendSMS(message);

        return ResponseEntity.ok().build();
    }

    /**
     * 좋아요를 누른 공지사항을 보는 api
     *
     * @return
     */
    @GetMapping("/api/user/{id}/notice/like")
    public List<NoticeLike> likeNotice(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        List<NoticeLike> noticeLikeList = noticeLikeRepository.findByUser(user);

        return noticeLikeList;
    }

    /**
     * 사용자 이메일과 비밀번호를 통해 JWT를 발행하는 api
     * jwt 토큰 발행 시 사용자 정보가 유효하지 않을 때 예외 발생
     * 사용자 정보가 존재하지 않는 경우(UserNotFoundException)에 대해 예외 발생
     * 비밀번호가 일치하지 않는 경우(PasswordNotMatchException)에 대해 예외 발생
     */
//    @PostMapping("/api/user/login")
//    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {
//
//        // 사용자 정의 에러 모델 사용
//        List<ResponseError> responseErrors = new ArrayList<>();
//
//        if (errors.hasErrors()) {
//            errors.getAllErrors().forEach((e) -> {
//                responseErrors.add(ResponseError.of((FieldError) e));
//            });
//
//            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
//        }
//
//        User user = userRepository.findByEmail(userLogin.getEmail())
//                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
//
//        if (PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
//            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
//        }
//
//        return ResponseEntity.ok().build();
//
//    }

    /**
     * 사용자 이메일과 비밀번호를 통해 jwt 토큰을 발행하는 api
     */
//    @PostMapping("/api/user/login")
//    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {
//
//        // 사용자 정의 에러 모델 사용
//        List<ResponseError> responseErrors = new ArrayList<>();
//
//        if (errors.hasErrors()) {
//            errors.getAllErrors().forEach((e) -> {
//                responseErrors.add(ResponseError.of((FieldError) e));
//            });
//
//            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
//        }
//
//        User user = userRepository.findByEmail(userLogin.getEmail())
//                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
//
//        log.info("userLogin.getPassword : {}", userLogin.getPassword());
//        log.info("user.getPassword : {}", user.getPassword());
//
//        if (PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
//            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
//        }
//
//        // 토큰 발행 시점
//        String token = JWT.create()
//                .withExpiresAt(new Date())
//                .withClaim("user_id", user.getId())
//                .withSubject(user.getUsername())
//                .withIssuer(user.getEmail())
//                .sign(Algorithm.HMAC512("hong".getBytes()));
//
//        return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());
//
//    }

    /**
     * 토큰 발행시 발행 유효기간을 1개월로 저장하는 API
     */
    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {

        // 사용자 정의 에러 모델 사용
        List<ResponseError> responseErrors = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrors.add(ResponseError.of((FieldError) e));
            });

            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        log.info("userLogin.getPassword : {}", userLogin.getPassword());
        log.info("user.getPassword : {}", user.getPassword());

        if (PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expireDate = java.sql.Timestamp.valueOf(expiredDateTime);

        // 토큰 발행 시점
        String token = JWT.create()
                .withExpiresAt(expireDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getUsername())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("hong".getBytes()));

        return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());

    }

    /**
     * jwt 재발행 api
     * 이미 발행된 jwt 토큰을 통해 토큰을 재밣행하는 로직
     * 정상적인 회원에 대해 재발행 진행
     */

    @PatchMapping("/api/user/login")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("F-TOKEN");
        String email = "";
        try {
            email = JWT.require(Algorithm.HMAC512("hong".getBytes()))
                    .build()
                    .verify(token)
                    .getIssuer();
        } catch (SignatureVerificationException e) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expireDate = java.sql.Timestamp.valueOf(expiredDateTime);

        String newToken = JWT.create()
                .withExpiresAt(expireDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getUsername())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("hong".getBytes()));

        return ResponseEntity.ok().body(UserLoginToken.builder().token(newToken).build());
    }

    /**
     * jwt 삭제 api
     */
    @DeleteMapping("/api/user/login")
    public ResponseEntity<?> removeToken(@RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 세션, 쿠키 삭제
        // 클라이언트 쿠키 / 로컬 스토리지 / 세션 스토리지
        // 블랙 리스트 작성

        return ResponseEntity.ok().build();
    }

    void sendSMS(String message) {
        System.out.println("[문자 메시지 전송]");
        System.out.println(message);
    }

    private String getResetPassword() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    @ExceptionHandler(value = {ExistsEmailException.class, PasswordNotMatchException.class})
    public ResponseEntity<?> ExistsEmailExceptionHandler(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> UserNotFoundExceptionHandler(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 내가 작성한 게시글 목록을 리턴하는 api
     */
    @GetMapping("/api/user/board/post")
    public ResponseEntity<?> myPost(@RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail("토큰 정보가 일치하지 않습니다.");
        }
        List<Board> list = boardService.postList(email);
        return ResponseResult.success(list);
    }

    /**
     * 내가 작성한 게시글의 코멘트 목록을 리턴하는 api
     */
    @GetMapping("/api/user/board/comment")
    public ResponseEntity<?> myComments(@RequestHeader("hong") String token) {
        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail("토큰 정보가 일치하지 않습니다.");
        }

        List<BoardComment> list = boardService.commentList(email);
        return ResponseResult.success(list);
    }

    /**
     * 사용자의 포인트 정보를 만들고 게시글을 작성할 경우, 포인트를 누적하는 api
     */
    @PostMapping("/api/user/point")
    public ResponseEntity<?> userPoint(@RequestHeader("hong") String token,
                                       @RequestBody UserPointInput userPointInput) {

        String email = "";

        try {
            email = JwtUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail("토큰 정보가 일치하지 않습니다.");
        }

        ServiceResult result = pointService.addPoint(email, userPointInput);
        return ResponseResult.result(result);

    }


}
