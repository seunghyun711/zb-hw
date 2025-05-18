package com.spring.jpa.notice.entity;

import com.spring.jpa.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoticeLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn
    @ManyToOne
    private Notice notice;

    @JoinColumn
    @ManyToOne
    private User user;
}
