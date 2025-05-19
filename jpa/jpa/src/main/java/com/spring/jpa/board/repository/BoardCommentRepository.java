package com.spring.jpa.board.repository;


import com.spring.jpa.board.entity.Board;
import com.spring.jpa.board.entity.BoardComment;
import com.spring.jpa.board.entity.BoardLike;
import com.spring.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    List<BoardComment> findByUser(User user);

}
