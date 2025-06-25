package com.buybike.app.service;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.BoardFormDto;
import com.buybike.app.domain.PageList;
import com.buybike.app.repository.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    BoardMapper boardMapper;

    // 게시글 등록
    public Long savePost(BoardFormDto boardDto) {
        Board board = boardDto.toEntity();
        boardMapper.insert(board);
        return board.getBoardId();
    }

    // 전체 게시글 조회
    @Transactional
    public List<BoardFormDto> getBoardList() {
        List<Board> boardList = boardMapper.findAll();
        List<BoardFormDto> boardDtoList = new ArrayList<>();
        for (Board board : boardList) {
            BoardFormDto boardDto = BoardFormDto.builder()
                    .boardId(board.getBoardId())
                    .memberId(board.getMemberId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .regDt(board.getRegDt())
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    // 게시글 단건 조회
    public Board getBoardById(Long boardId) {
        return boardMapper.findById(boardId);
    }

    // 게시글 수정
    public Long updateBoard(BoardFormDto boardDto) {
        Board board = boardDto.toEntity();
        return boardMapper.update(board);
    }

    // 게시글 삭제
    public void deleteBoardById(Long boardId) {
        boardMapper.deleteById(boardId);
    }

    public int getTotalBoardCount() {
        return boardMapper.getTotalBoardCount();
    }

    // 페이징 및 정렬된 게시글 목록 조회
    public List<Board> listAll(int pageNum, int pageSize, String sortField, String sortDir) {
        int offset = (pageNum - 1) * pageSize;
        return boardMapper.findAllWithPaging(offset, pageSize, sortField, sortDir);
    }

    public Page<Map<String, Object>> getListBoard(Board board, Pageable pageable) {

        // 빌더 패턴으로 data, pageable 파라미터에 데이터 주입
        PageList<?> pageList = PageList.builder()
                .data(board)
                .pageable(pageable)
                .build();

        List<Map<String, Object>> content = boardMapper.getListBoard(pageList);
        int total = boardMapper.getListBoardCount(board);

        return new PageImpl<>(content, pageable, total);


    }
}