package com.spring.jpa.notice.model;

import com.spring.jpa.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponse {
    private long id;
    private long regUserId;
    private String regUsername;

    private String title;
    private String contents;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private int hits;
    private int likes;

    public static NoticeResponse of(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .contents(notice.getContents())
                .regDate(notice.getRegDate())
                .regUserId(notice.getUser().getId())
                .regUsername(notice.getUser().getUsername())
                .updateDate(notice.getUpdateDate())
                .hits(notice.getHits())
                .likes(notice.getLikes())
                .build();
    }

}
