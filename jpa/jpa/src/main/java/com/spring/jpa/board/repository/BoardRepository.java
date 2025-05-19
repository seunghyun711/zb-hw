package com.spring.jpa.board.repository;

import com.spring.jpa.board.entity.Board;
import com.spring.jpa.board.entity.BoardType;
import com.spring.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    long countByBoardType(BoardType boardType);

    List<Board> findByUser(User user);
}
