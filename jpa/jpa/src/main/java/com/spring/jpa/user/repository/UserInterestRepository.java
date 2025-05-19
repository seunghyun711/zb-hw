package com.spring.jpa.user.repository;

import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.entity.UserInterest;
import com.spring.jpa.user.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    long countByUserAndInterestUser(User user, User interestUser);
}
