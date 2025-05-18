package com.spring.jpa.notice.repository;

import com.spring.jpa.notice.entity.NoticeLike;
import com.spring.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeLikeRepository extends JpaRepository<NoticeLike, Long> {
    List<NoticeLike> findByUser(User user);
}
