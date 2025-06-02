package com.buybike.app.service;

import com.buybike.app.domain.Board;

import java.util.List;

public interface BoardService {
    List<Board> getAllBoardList();
    Board getBoardById(Long boardId);
    void setNewBoard(Board board);
}
