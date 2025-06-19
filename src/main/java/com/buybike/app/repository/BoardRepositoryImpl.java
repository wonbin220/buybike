package com.buybike.app.repository;

import com.buybike.app.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardRepository boardRepository;

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Override
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public Long insert(Board board) {
        return boardRepository.insert(board);
    }

    @Override
    public Long update(Board board) {
        return boardRepository.update(board);
    }

    @Override
    public Long deleteById(Long boardId) {
        return boardRepository.deleteById(boardId);
    }
}
