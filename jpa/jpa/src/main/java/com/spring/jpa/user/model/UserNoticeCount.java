package com.spring.jpa.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserNoticeCount {
    private long id;
    private String email;
    private String username;

    private long noticeCount;
}
