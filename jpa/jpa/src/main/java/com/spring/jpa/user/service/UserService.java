package com.spring.jpa.user.service;

import com.spring.jpa.board.model.ServiceResult;
import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.model.UserLogCount;
import com.spring.jpa.user.model.UserLogin;
import com.spring.jpa.user.model.UserNoticeCount;
import com.spring.jpa.user.model.UserSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserSummary getUserStatusCount();

    List<User> getTodayUsers();

    List<UserNoticeCount> getUserNoticeCount();

    List<UserLogCount> getUserLogCount();

    /**
     * 좋아요를 가장 많이 한 사용자 목록 리턴
     */
    List<UserLogCount> getBestLikeUser();

    ServiceResult addInterestUser(String email, Long id);

    ServiceResult removeInterestUser(String email, Long id);

    User login(UserLogin userLogin);
}
