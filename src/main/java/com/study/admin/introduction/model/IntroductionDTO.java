package com.study.admin.introduction.model;

import com.study.admin.introduction.entity.IntroductionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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

    public static IntroductionDTO toIntroductionDto(IntroductionEntity entity) {
        IntroductionDTO introductionDTO = new IntroductionDTO();
        introductionDTO.setId(entity.getId());
        introductionDTO.setTitle(entity.getTitle());
        introductionDTO.setContent(entity.getContent());
        introductionDTO.setCreatedId(entity.getCreatedId());
        introductionDTO.setCreatedName(entity.getCreatedName());
        introductionDTO.setUseAt(entity.getUseAt());
        introductionDTO.setSort(entity.getSort());
        introductionDTO.setDeleteAt("N");
        return introductionDTO;
    }
}
