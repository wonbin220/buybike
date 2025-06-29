package com.buybike.app.repository;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.PageList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface BoardMapper {
    List<Board> findAll();
    Board findById(@Param("boardId") Long boardId);
    Long insert(Board board);
    Long update(Board board);
    Long deleteById(@Param("boardId") Long boardId);

    int getTotalBoardCount();


    List<Board> findAllWithPaging(@Param("offset") int offset,
                                  @Param("limit") int limit,
                                  @Param("sortField") String sortField,
                                  @Param("sortDir") String sortDir);

    List<Map<String, Object>> getListBoard(PageList<?> pageList);

    int getListBoardCount(Board board);
}
