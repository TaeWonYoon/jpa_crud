package com.study.main.service;

import com.study.admin.boards.repository.BoardsRepository;
import com.study.admin.introduction.entity.IntroductionEntity;
import com.study.admin.introduction.model.IntroductionDTO;
import com.study.admin.introduction.repository.IntroductionRepository;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.file.model.FileDTO;
import com.study.file.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeService {

    private static final Logger log = LoggerFactory.getLogger(HomeService.class);

    @Autowired
    private BoardsRepository boardsRepository;

    @Autowired
    private IntroductionRepository introductionRepository;

    @Autowired
    private FileRepository fileRepository;

    public List<BoardDTO> boardList(String registTy) {
        log.info("########### mainService boardList() start ###########");
        List<BoardEntity> boardEntity = boardsRepository.findTop5ByDeleteAtAndRegistTyOrderByIdDesc("N",registTy);

        // Entity → DTO 변환
        List<BoardDTO> boardList = boardEntity.stream()
        .map(BoardDTO::toBoardDto)
        .collect(Collectors.toList());

        return boardList;
    }


    public List<IntroductionDTO> introductionList() {
        log.info("########### mainService introductionList() start ###########");

        List<IntroductionEntity> introductions = introductionRepository.findTop3ByDeleteAtAndUseAtOrderBySortAsc("N", "Y");

        List<Long> introIds = introductions.stream()
                .map(IntroductionEntity::getId)
                .collect(Collectors.toList());

        Map<Long, List<FileDTO>> filesMap = fileRepository.findByTableNameAndTableIdInAndDeleteAt("introduction", introIds, "N")
                .stream()
                .map(FileDTO::toFileDto)
                .collect(Collectors.groupingBy(FileDTO::getTableId));

        List<IntroductionDTO> introductionDTOs = introductions.stream()
                .map(intro -> {
                    IntroductionDTO dto = IntroductionDTO.toIntroductionDto(intro);
                    dto.setFiles(filesMap.getOrDefault(intro.getId(), Collections.emptyList()));
                    return dto;
                })
                .collect(Collectors.toList());

        return introductionDTOs;
    }
}
