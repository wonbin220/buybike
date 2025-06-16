package com.buybike.app.repository;

import com.buybike.app.domain.Board;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository {
    List<Board> getAllBoardList();
    Board getBoardById(Long boardId);
    void setNewBoard(Board board);
}
