package com.study.admin.introduction.service;


import com.study.User.entity.UserEntity;
import com.study.admin.introduction.entity.IntroductionEntity;
import com.study.admin.introduction.model.IntroductionDTO;
import com.study.admin.introduction.repository.IntroductionRepository;

import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.file.service.FileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class IntroductionService {

    private static final Logger log = LoggerFactory.getLogger(IntroductionService.class);

    @Autowired
    private IntroductionRepository introductionRepository;

    @Autowired
    private FileService fileService;

    public Map<String, Object> introductionList(String searchType, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.info("########### IntroductionService introductionList() start ###########");

        // LocalDate → LocalDateTime 변환
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        String title = "";
        String createdName = "";
        if(!StringUtils.isEmpty(searchType)) {
            //검색 타입에 따른 키워드 넣어주기
            switch (searchType) {
                case "title":
                    title = keyword;
                    keyword = "";
                    break;
                case "createdName":
                    createdName = keyword;
                    keyword = "";
                    break;
                default:
                    break;
            }
        }

        // 검색 조건을 이용한 페이지 조회
        Page<IntroductionEntity> page = introductionRepository.findBySearchConditions(title, createdName, startDateTime, endDateTime, keyword, pageable);

        // DTO 변환
        List<IntroductionDTO> introductionDtoList = page.stream()
                .map(IntroductionDTO::toIntroductionDto)
                .collect(Collectors.toList());

        // 결과 리턴
        Map<String, Object> result = new HashMap<>();
        result.put("introductionList", introductionDtoList);
        result.put("page", page);

        return result;

    }

    //소개글 저장
    public IntroductionEntity introductionSave(IntroductionDTO introductionDTO, MultipartFile file) throws IOException {
        log.info("########### IntroductionService introductionSave() start ###########");
        IntroductionEntity entity = IntroductionEntity.toSaveEntity(introductionDTO); //dto에서 entity로 변환

        IntroductionEntity saveIntroduction = introductionRepository.save(entity);


        // 2. 파일 있으면 저장
        if (file != null && !file.isEmpty()) {
            fileService.saveFile(file, saveIntroduction.getId(), "introduction", "introduction");
        }

        return saveIntroduction;
    }

    //소개글 조회
    public IntroductionDTO getBoardById(Long id) {
        log.info("########### IntroductionService getBoardById() start ###########");
        Optional<IntroductionEntity> optional = introductionRepository.findByIdAndDeleteAt(id, "N");
        return optional.map(IntroductionDTO::toIntroductionDto).orElse(null);
    }


    //소개글 수정(첨부 파일)
    @Transactional
    public IntroductionEntity updateIntroduction(Long id, IntroductionDTO dto, MultipartFile file) {
        log.info("########### IntroductionService updateIntroduction() start ###########");

        IntroductionEntity introduction = introductionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));

        log.info("파일 업로드 시작 준비중!! 보드 내용 확인 ={}", introduction);
        int result = fileService.fileEditUpload(id, file, "introduction", "introduction", dto.getUpdatedId().toString()); //첨부파일 분기처리 * 게시글 업로드 시 기존 첨부파일이 있으면 삭제 후 등록 없으면 그냥 등록
        log.info("fileResult={}", result);
        // 소개글 내용 수정
        introduction.setTitle(dto.getTitle());
        introduction.setContent(dto.getContent());
        introduction.setUpdatedId(dto.getUpdatedId());
        introduction.setSort(dto.getSort());
        introduction.setUseAt(dto.getUseAt());
        introduction.setHrefUrl(dto.getHrefUrl());
        
        return introductionRepository.save(introduction);
    }

    public IntroductionEntity deleteIntroduction(Long id) {
        log.info("########### IntroductionService updateIntroduction() start ###########");
        IntroductionEntity introduction = introductionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));

        introduction.setDeleteAt("Y");

        return introductionRepository.save(introduction);
    }
}
