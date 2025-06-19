package com.buybike.app.repository;

import com.buybike.app.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BoardRepository {
    List<Board> findAll();
    Board findById(@Param("boardId") Long boardId);
    Long insert(Board board);
    Long update(Board board);
    Long deleteById(@Param("boardId") Long boardId);
}
