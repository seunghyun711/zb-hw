package com.spring.jpa.board.repository;


import com.spring.jpa.board.entity.BoardScrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardScrapRepository extends JpaRepository<BoardScrap, Long> {
}
