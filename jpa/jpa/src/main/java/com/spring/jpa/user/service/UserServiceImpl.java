package com.spring.jpa.user.service;

import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.model.UserLogCount;
import com.spring.jpa.user.model.UserNoticeCount;
import com.spring.jpa.user.model.UserStatus;
import com.spring.jpa.user.model.UserSummary;
import com.spring.jpa.user.repository.UserCustomRepository;
import com.spring.jpa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;

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
}
