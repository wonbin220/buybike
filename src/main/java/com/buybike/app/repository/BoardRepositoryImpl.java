package com.buybike.app.repository;

import com.buybike.app.domain.Board;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private SqlSessionTemplate sql;
    private List<Board> listofBoards = new ArrayList<Board>();

    public BoardRepositoryImpl() {
        Board board1 = new Board();
        board1.setBoardId(1L);
        board1.setTitle("Board 1");
        board1.setContent("Content for Board 1");
        board1.setFileName("bike1.jpg");
        ;
        Board board2 = new Board();
        board2.setBoardId(2L);
        board2.setTitle("Board 2");
        board2.setContent("Content for Board 2");
        board2.setFileName("bike2.jpg");

        Board board3 = new Board();
        board3.setBoardId(3L);
        board3.setTitle("Board 3");
        board3.setContent("Content for Board 3");
        board3.setFileName("bike3.jpg");

        listofBoards.add(board1);
        listofBoards.add(board2);
        listofBoards.add(board3);
    }

    public List<Board> getAllBoardList() {
        return listofBoards;
    }

    @Override
    public Board getBoardById(Long boardId) {
        Board boardInfo = null;
        for (int i = 0; i < listofBoards.size(); i++) {
            Board board = listofBoards.get(i);
            if (board != null && board.getBoardId() != null && board.getBoardId().equals(boardId)) {
                boardInfo = board;
                break;
            }
        }
        if (boardInfo == null)
            throw new IllegalArgumentException("게시판ID가 " + boardId + "인 해당 게시판 글을 찾을 수 없습니다.");
            return boardInfo;

    }

    @Override
    public void setNewBoard(Board board) {
        listofBoards.add(board);
    }
}
