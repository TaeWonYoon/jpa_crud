package com.study.admin.boards.service;

import com.study.User.entity.UserEntity;
import com.study.admin.boards.repository.BoardsRepository;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardsService {

    private static final Logger log = LoggerFactory.getLogger(BoardsService.class);

    @Autowired
    private BoardsRepository boardsRepository;

    public Map<String, Object> boardList(String title, LocalDate startDate, LocalDate endDate, String registTy,Pageable pageable) {
        log.info("########### BoardsService boardList() start ###########");

        // LocalDate → LocalDateTime 변환
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        // 검색 조건을 이용한 페이지 조회
        Page<BoardEntity> page = boardsRepository.findBySearchConditions(title, startDateTime, endDateTime, registTy, pageable);

        // DTO 변환
        List<BoardDTO> boardDTOList = page.stream()
                .map(BoardDTO::toBoardDto)
                .collect(Collectors.toList());

        // 결과 리턴
        Map<String, Object> result = new HashMap<>();
        result.put("boardList", boardDTOList);
        result.put("page", page);

        return result;

    }

    public BoardDTO getBoardById(Long id) {
        log.info("########### BoardService getBoardById() start ###########");
        Optional<BoardEntity> optional = boardsRepository.findByIdAndDeleteAt(id, "N");
        return optional.map(BoardDTO::toBoardDto).orElse(null);
    }

    public BoardEntity deleteBoard(Long id){
        log.info("########### MemberService delteMember() start ###########");
        BoardEntity board = boardsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원이 없습니다."));

        board.setDeleteAt("Y");

        return boardsRepository.save(board);
    }
}
