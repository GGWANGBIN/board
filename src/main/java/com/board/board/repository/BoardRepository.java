package com.board.board.repository;

import com.board.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByWriterContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByContentContaining(String searchKeyword, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Board b set b.hit = b.hit + 1 where b.id =:id")
    int updateHit(@Param("id") Integer id);


}
