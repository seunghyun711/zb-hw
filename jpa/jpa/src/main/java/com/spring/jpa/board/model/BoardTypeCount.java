package com.spring.jpa.board.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardTypeCount {

    private long id;

    private String boardName;

    private LocalDateTime regDate;

    private boolean usingYN;

    private long boardCount;

    public BoardTypeCount(Object[] o) {
        this.id = ((BigInteger) o[0]).longValue();
        this.boardName = (String) o[1];
        this.regDate = ((Timestamp) o[2]).toLocalDateTime();
        this.usingYN = (Boolean) o[3];
        this.boardCount = ((BigInteger) o[4]).longValue();

    }

    public BoardTypeCount(BigInteger id, String boardName, Timestamp regDate, Boolean usingYn, BigInteger boardCount) {
        this.id = id.longValue();
        this.boardName = boardName;
        this.regDate = regDate.toLocalDateTime();
        this.usingYN = usingYn;
        this.boardCount = boardCount.longValue();
    }
}
