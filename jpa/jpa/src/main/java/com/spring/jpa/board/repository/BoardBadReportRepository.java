package com.spring.jpa.board.repository;


import com.spring.jpa.board.entity.Board;
import com.spring.jpa.board.entity.BoardBadReport;
import com.spring.jpa.board.entity.BoardLike;
import com.spring.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardBadReportRepository extends JpaRepository<BoardBadReport, Long> {
}
