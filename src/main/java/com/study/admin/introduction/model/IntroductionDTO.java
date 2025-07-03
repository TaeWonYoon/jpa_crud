package com.study.admin.introduction.model;

import com.study.admin.introduction.entity.IntroductionEntity;
import com.study.file.model.FileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IntroductionDTO {

    private Long id;
    private String title; //제목
    private String content; //내용
    private Long createdId; //등록자 아이디
    private String createdName; //등록자이름
    private Long updatedId; //수정자 아이디
    private String updatedName; //수정자 이름
    private String deleteAt; //삭제 여부 N, Y
    private LocalDateTime createdDate;  // 작성일시 created_date
    private LocalDateTime updatedDate;  // 수정일시 updated_date
    private String useAt; //사용여부
    private int sort; //정렬순서
    private String hrefUrl; //이동할 url

    private List<FileDTO> files;

    public static IntroductionDTO toIntroductionDto(IntroductionEntity entity) {
        IntroductionDTO dto = new IntroductionDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCreatedId(entity.getCreatedId());
        dto.setCreatedName(entity.getCreatedName());
        dto.setUseAt(entity.getUseAt());
        dto.setSort(entity.getSort());
        dto.setDeleteAt(entity.getDeleteAt());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedId(entity.getUpdatedId());
        dto.setUpdatedName(entity.getUpdatedName());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setHrefUrl(entity.getHrefUrl());
        return dto;
    }
}
