package com.study.board.repository;

import com.study.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    @Query("SELECT b FROM BoardEntity b WHERE "
            + "(:title IS NULL OR b.title LIKE %:title%) AND "
            + "(:startDateTime IS NULL OR b.createdDate >= :startDateTime) AND "
            + "(:endDateTime IS NULL OR b.createdDate <= :endDateTime)")
    Page<BoardEntity> findBySearchConditions(@Param("title") String title,
                                             @Param("startDateTime") LocalDateTime startDateTime,
                                             @Param("endDateTime") LocalDateTime endDateTime,
                                             Pageable pageable);
}
