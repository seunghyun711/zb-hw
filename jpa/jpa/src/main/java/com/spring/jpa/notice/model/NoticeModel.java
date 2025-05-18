package com.spring.jpa.notice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoticeModel {
    private int id;
    private String title;
    private String contents;
    private LocalDateTime regDate;
}
