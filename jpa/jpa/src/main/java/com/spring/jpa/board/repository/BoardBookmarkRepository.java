package com.spring.jpa.board.repository;


import com.spring.jpa.board.entity.BoardBookmark;
import com.spring.jpa.board.entity.BoardScrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardBookmarkRepository extends JpaRepository<BoardBookmark, Long> {
}
