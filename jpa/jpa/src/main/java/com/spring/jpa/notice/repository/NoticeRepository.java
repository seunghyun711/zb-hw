package com.spring.jpa.notice.repository;

import com.spring.jpa.notice.entity.Notice;
import com.spring.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<List<Notice>> findByIdIn(List<Long> idList);

    // 제목동일, 내용동일, 등록시간이 체크시간보다 크다.
    Optional<List<Notice>> findByTitleAndContentsAndRegDateIsGreaterThanEqual(String title, String contents, LocalDateTime regDate);

    int countByTitleAndContentsAndRegDateIsGreaterThanEqual(String title, String contents, LocalDateTime regDate);

    List<Notice> findByUser(User user);

    long countByUser(User user);

}
