package com.spring.jpa.user.repository;

import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //    Optional<User> findByEmail(String email);
    int countByEmail(String email);

    Optional<User> findByIdAndPassword(long id, String password);

    Optional<User> findByUsernameAndPhone(String username, String phone);

    Optional<User> findByEmail(String email);

    List<User> findByEmailContainsOrPhoneContainsOrUsernameContains(String email, String phone, String username);

    long countByStatus(UserStatus userStatus);

    User findByRegDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select * from User u where u.regDate between :startDate and :endDate")
    List<User> findToday(LocalDateTime startDate, LocalDateTime endDate);
}
