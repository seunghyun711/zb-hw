package com.spring.jpa.board.repository;


import com.spring.jpa.board.entity.Board;
import com.spring.jpa.board.entity.BoardHit;
import com.spring.jpa.board.entity.BoardType;
import com.spring.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHitsRepository extends JpaRepository<BoardHit, Long> {
    long  countByBoardAndUser(Board board, User user);
}
