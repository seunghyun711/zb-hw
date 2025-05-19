package com.spring.jpa.board.entity;

import com.spring.jpa.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private BoardType boardType;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime regDate;

    @Column
    private boolean topYn;

    @Column
    private LocalDate publishStartDate;

    @Column
    private LocalDate publishEndDate;
}
