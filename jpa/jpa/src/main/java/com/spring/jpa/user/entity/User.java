package com.spring.jpa.user.entity;

import com.spring.jpa.user.model.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updateDate;

    @Column
    private UserStatus status;

    @Column
    private boolean lockYn;
}
