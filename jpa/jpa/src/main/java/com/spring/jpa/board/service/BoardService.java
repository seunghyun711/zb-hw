package com.spring.jpa.board.service;

import com.spring.jpa.board.entity.Board;
import com.spring.jpa.board.entity.BoardBadReport;
import com.spring.jpa.board.entity.BoardComment;
import com.spring.jpa.board.entity.BoardType;
import com.spring.jpa.board.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {
    ServiceResult setBoardTop(Long id, boolean flag);

    BoardType getByName(String boardName);

    ServiceResult addBoard(BoardTypeInput boardTypeInput);

    ServiceResult updateBoard(long id, BoardTypeInput boardTypeInput);

    ServiceResult deleteBoard(Long id);

    List<BoardType> getAllBoardType();

    ServiceResult setBoardTypeUsing(Long id, BoardTypeUsing boardTypeUsing);

    List<BoardTypeCount> getBoardTypeCount();


    ServiceResult setBoardPeriod(Long id, BoardPeriod boardPeriod);

    ServiceResult setBoardHits(Long id, String email);

    ServiceResult setBoardLike(Long id, String email);

    ServiceResult setBoardUnLike(Long id, String email);

    ServiceResult addBadReport(Long id, String email, BoardBadReportInput boardBadReportInput);

    List<BoardBadReport> badReportList();

    ServiceResult scrapBoard(Long id, String email);

    ServiceResult removeScrap(long id, String email);

    ServiceResult addBookmark(Long id, String email);

    ServiceResult removeBookmark(Long id, String email);

    List<Board> postList(String email);

    List<BoardComment> commentList(String email);
}
