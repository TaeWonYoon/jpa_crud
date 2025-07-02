package com.study.admin.introduction.service;


import com.study.User.entity.UserEntity;
import com.study.admin.introduction.entity.IntroductionEntity;
import com.study.admin.introduction.model.IntroductionDTO;
import com.study.admin.introduction.repository.IntroductionRepository;

import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public void introductionSave(IntroductionDTO introductionDTO) throws IOException {
        log.info("########### IntroductionService introductionSave() start ###########");
        IntroductionEntity entity = IntroductionEntity.toSaveEntity(introductionDTO); //dto에서 entity로 변환

        IntroductionEntity saveIntroduction = introductionRepository.save(entity);

    }

    public IntroductionDTO getBoardById(Long id) {
        log.info("########### IntroductionService getBoardById() start ###########");
        Optional<IntroductionEntity> optional = introductionRepository.findByIdAndDeleteAt(id, "N");
        return optional.map(IntroductionDTO::toIntroductionDto).orElse(null);
    }
}
