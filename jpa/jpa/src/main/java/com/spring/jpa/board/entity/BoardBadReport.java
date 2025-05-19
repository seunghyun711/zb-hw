package com.spring.jpa.board.entity;

import com.spring.jpa.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class BoardBadReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long boardId;

    @Column
    private long boardUserId;

    @Column
    private String boardTitle;

    @Column
    private String boardContents;

    @Column
    private LocalDateTime boardRegDate;

    @Column
    private long userId;

    @Column
    private String username;

    @Column
    private String userEmail;

    @Column
    private LocalDateTime regDate;

    // 신고내용
    @Column
    private String comments;

}
