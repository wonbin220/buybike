package com.buybike.app.service;

import com.buybike.app.domain.Board;
import com.buybike.app.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public List<Board> getAllBoardList() {
        return boardRepository.findAll();
    }

    @Override
    public Board getBoardById(Long boardId) {
        Board boardById = boardRepository.findById(boardId);
        return boardById;
    }

    @Override
    public void setNewBoard(Board board) {
        boardRepository.insert(board);
    }

}
