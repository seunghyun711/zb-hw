package com.spring.jpa.user.repository;

import com.spring.jpa.user.entity.UserLoginHistory;
import com.spring.jpa.user.entity.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, Long> {
}
