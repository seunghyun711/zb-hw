package com.spring.jpa.user.service;

import com.spring.jpa.board.model.ServiceResult;
import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.entity.UserPoint;
import com.spring.jpa.user.model.UserPointInput;
import com.spring.jpa.user.model.UserPointType;
import com.spring.jpa.user.repository.UserPointRepository;
import com.spring.jpa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService{

    private final UserPointRepository userPointRepository;
    private final UserRepository userRepository;

    @Override
    public ServiceResult addPoint(String email, UserPointInput userPointInput) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("회원 정보가 존재하지 않습니다.");
        }
        User user = optionalUser.get();

        UserPoint userPoint = UserPoint.builder()
                .user(user)
                .userPointType(userPointInput.getUserPointType())
                .point(userPointInput.getUserPointType().getValue())
                .build();

        userPointRepository.save(userPoint);
        return null;
    }
}
