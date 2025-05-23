package com.spring.jpa.user.service;

import com.spring.jpa.board.model.ServiceResult;
import com.spring.jpa.common.exception.BizException;
import com.spring.jpa.logs.service.LogService;
import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.entity.UserInterest;
import com.spring.jpa.user.model.*;
import com.spring.jpa.user.repository.UserCustomRepository;
import com.spring.jpa.user.repository.UserInterestRepository;
import com.spring.jpa.user.repository.UserRepository;
import com.spring.jpa.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final UserInterestRepository userInterestRepository;

    @Override
    public UserSummary getUserStatusCount() {
        long usingUserCount = userRepository.countByStatus(UserStatus.Using);
        long stopUserCount = userRepository.countByStatus(UserStatus.Stop);
        long totalUserCount = userRepository.count();

        return UserSummary.builder()
                .usingUserCount(usingUserCount)
                .stopUserCount(stopUserCount)
                .totalUserCount(totalUserCount)
                .build();
    }

    @Override
    public List<User> getTodayUsers() {
        LocalDateTime t = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(t.getYear(), t.getMonth(), t.getDayOfMonth(), 0, 0);
        LocalDateTime endDate = startDate.plusDays(1);

        return userRepository.findToday(startDate, endDate);
    }

    @Override
    public List<UserNoticeCount> getUserNoticeCount() {
        return userCustomRepository.findUserNoticeCount();
    }

    @Override
    public List<UserLogCount> getUserLogCount() {
        return userCustomRepository.findUserLogCount();
    }

    @Override
    public List<UserLogCount> getBestLikeUser() {
        return userCustomRepository.findUserLikeBest();
    }

    @Override
    public ServiceResult addInterestUser(String email, Long id) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("회원 정보가 존재하지 않습니다.");
        }
        User user = optionalUser.get();

        Optional<User> optionalInterestUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("관심사용자에 추가할 회원정보가 존재하지 않습니다.");
        }
        User interestUser = optionalInterestUser.get();

        // 자기 자신 추가하는 경우
        if (user.getId() == interestUser.getId()) {
            return ServiceResult.fail("자기 자신은 추가할 수 없습니다.");
        }

        if (userInterestRepository.countByUserAndInterestUser(user, interestUser) > 0) {
            return ServiceResult.fail("이미 관심사용자 목록에 추가하셨습니다.,");
        }

        UserInterest userInterest = UserInterest.builder()
                .user(user)
                .interestUser(interestUser)
                .regDate(LocalDateTime.now())
                .build();

        userInterestRepository.save(userInterest);

        return ServiceResult.success();

    }

    @Override
    public ServiceResult removeInterestUser(String email, Long id) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("회원 정보가 존재하지 않습니다.");
        }
        User user = optionalUser.get();

        Optional<UserInterest> optionalUserInterest = userInterestRepository.findById(id);


        if (!optionalUserInterest.isPresent()) {
            return ServiceResult.fail("삭제할 정보가 없습니다.");
        }

        UserInterest userInterest = optionalUserInterest.get();

        if (userInterest.getUser().getId() != user.getId()) {
            return ServiceResult.fail("본인의 관심사 정보만 삭제할 수 있습니다.");
        }

        userInterestRepository.delete(userInterest);
        return ServiceResult.success();
    }

    @Override
    public User login(UserLogin userLogin) {
        Optional<User> optionalUser = userRepository.findByEmail(userLogin.getEmail());
        if (!optionalUser.isPresent()) {
            throw new BizException("회원 정보가 존재하지 않습니다.");
        }
        User user = optionalUser.get();

        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new BizException(("일치하는 정보가 없습니다."));
        }


        return user;

    }
}
