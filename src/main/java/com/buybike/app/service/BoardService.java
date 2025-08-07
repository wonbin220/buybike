package com.buybike.app.service;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.BoardFormDto;
import com.buybike.app.repository.BoardMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private  FileService fileService; // 파일 업로드/삭제 서비스

    // public int getTotalBoardCount() {
    //     return boardMapper.getTotalBoardCount();
    // }
    //
    // // 페이징 및 정렬된 게시글 목록 조회
    // public List<Board> listAll(int pageNum, int pageSize, String sortField, String sortDir) {
    //     int offset = (pageNum - 1) * pageSize;
    //     return boardMapper.findAllWithPaging(offset, pageSize, sortField, sortDir);
    // }
    //
    // public Page<Map<String, Object>> getListBoard(Board board, Pageable pageable) {
    //
    //     // 빌더 패턴으로 data, pageable 파라미터에 데이터 주입
    //     PageList<?> pageList = PageList.builder()
    //             .data(board)
    //             .pageable(pageable)
    //             .build();
    //
    //     List<Map<String, Object>> content = boardMapper.getListBoard(pageList);
    //     int total = boardMapper.getListBoardCount(board);
    //
    //     return new PageImpl<>(content, pageable, total);
    // }


    public List<Board> list() throws Exception {
        return boardMapper.list();
    }

    public Board select(Integer no) throws Exception {
        return boardMapper.select(no);
    }

    public boolean insert(Board board) throws Exception {
        return boardMapper.insert(board);
    }

     public boolean update(BoardFormDto boardFormDto) throws Exception {
         return boardMapper.update(boardFormDto.toEntity());
     }

    public boolean delete(Integer no) throws Exception {
        return boardMapper.delete(no);
    }

    // PageHelper를 사용한 페이징 처리
    public PageInfo<Board> page(int page, int size) throws Exception {
        // PageHelper.startPage(page, size); (현재번호, 페이지당 데이터 수)
        PageHelper.startPage(page, size);
        List<Board> list = boardMapper.list();

        // PageInfo(리스트, 노출 페이지 수)
        PageInfo<Board> pageInfo = new PageInfo<>(list, 10);
        return pageInfo;
    }
}