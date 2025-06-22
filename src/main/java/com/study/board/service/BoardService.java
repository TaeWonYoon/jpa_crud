package com.study.board.service;

import com.study.file.entity.FileEntity;
import com.study.file.repository.FileRepository;
import com.study.file.service.FileService;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    public void boardSave(BoardDTO dto, MultipartFile file) throws IOException {
        log.info("########### BoardService boardSave() start ###########");


        BoardEntity entity = BoardEntity.toSaveEntity(dto); //dto에서 entity로 변환

        BoardEntity saveBoard = boardRepository.save(entity);

        // 2. 파일 있으면 저장
        if (file != null && !file.isEmpty()) {
            fileService.saveFile(file, saveBoard);
        }

    }

    public Map<String, Object> boardList(String title, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.info("########### BoardService boardList() start ###########");

        /* 리스트 방식 페이징
        Page<BoardEntity> boardEntityPage = boardRepository.findAll(pageable);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityPage.getContent()) { //getContent는 기존 BoardEntity 타입
            boardDTOList.add(BoardDTO.toBoardDto(boardEntity));
        }
        return boardDTOList;
        */


        // LocalDate → LocalDateTime 변환
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        // 검색 조건을 이용한 페이지 조회
        Page<BoardEntity> page = boardRepository.findBySearchConditions(title, startDateTime, endDateTime, pageable);

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
        Optional<BoardEntity> optional = boardRepository.findById(id);
        return optional.map(BoardDTO::toBoardDto).orElse(null);
    }

    //게시글 수정(첨부 파일)
    @Transactional
    public void updateBoard(Long id, BoardDTO dto, MultipartFile file) {
        log.info("########### BoardService updateBoard() start ###########");

        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));
        log.info("파일 업로드 시작 준비중!! 보드 내용 확인 ={}", board);
        int result = fileService.fileEditUpload(id, file); //첨부파일 분기처리 * 게시글 업로드 시 기존 첨부파일이 있으면 삭제 후 등록 없으면 그냥 등록
        log.info("fileResult={}", result);
        // 게시글 내용 수정
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        boardRepository.save(board);
    }

    @Transactional
    public void increaseViews(Long id) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));

        board.setViews(board.getViews() + 1);
    }
}
