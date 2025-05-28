package com.buybike.app.repository;

import com.buybike.app.domain.Board;

import java.util.List;

public interface BoardRepository {
    List<Board> getAllBoardList();
    Board getBoardById(Long boardId);
}
