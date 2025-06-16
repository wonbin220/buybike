package com.buybike.app.service;

import com.buybike.app.domain.Board;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {
    List<Board> getAllBoardList();
    Board getBoardById(Long boardId);
    void setNewBoard(Board board);
}
