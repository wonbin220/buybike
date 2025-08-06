package com.buybike.app.repository;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface BoardMapper {
    // List<Board> findAll();
    // Board findById(@Param("id") String id);
    // Long insert(Board board);
    // Long update.html(Board board);
    // Long deleteById(@Param("id") String id);

    // int getTotalBoardCount();

    //
    // List<Board> findAllWithPaging(@Param("offset") int offset,
    //                               @Param("limit") int limit,
    //                               @Param("sortField") String sortField,
    //                               @Param("sortDir") String sortDir);
    //
    // List<Map<String, Object>> getListBoard(PageList<?> pageList);

    // int getListBoardCount(Board board);


    // 목록
    public List<Board> list() throws Exception;
    // 페이징 목록
    public List<Board> page(Pagination pagination) throws Exception;
    // 데이터 수
    public long count() throws Exception;
    // 조회
    public Board select(Integer no) throws Exception;
    // 등록
    public boolean insert(Board board) throws Exception;
    // 수정
    //public boolean update(Board board) throws Exception;
    public int update(Board board) throws Exception;
    // 삭제
    public boolean delete(Integer no) throws Exception;


    // 게시글 ID로 조회
    public Board findById(String id) throws Exception;
}


