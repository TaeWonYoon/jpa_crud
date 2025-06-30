package com.study.admin.repository;

import com.study.User.entity.UserEntity;
import com.study.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {



}
