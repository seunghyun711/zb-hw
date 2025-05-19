package com.spring.jpa.user.service;

import com.spring.jpa.board.model.ServiceResult;
import com.spring.jpa.user.model.UserPointInput;
import com.spring.jpa.user.model.UserPointType;

public interface PointService {
    ServiceResult addPoint(String email, UserPointInput userPointInput);
}
