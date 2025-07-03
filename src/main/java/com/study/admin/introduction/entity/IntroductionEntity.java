package com.study.admin.introduction.entity;

import com.study.User.entity.UserEntity;
import com.study.admin.introduction.model.IntroductionDTO;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.file.entity.FileEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "introduction")
public class IntroductionEntity {

    @Id // pk 컬럼, 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_inc
    private Long id;

    @Column(length = 45, nullable = false)
    private String title; //제목

    @Column(length = 1000, nullable = false)
    private String content; //내용

    @Column
    private Long createdId; //등록자 아이디

    @Column
    private String createdName; //등록자이름

    @Column
    private Long updatedId; //수정자 아이디

    @Column
    private String updatedName; //수정자이름

    @Column
    private String deleteAt; //삭제 여부 N, Y
    @Column

    private LocalDateTime createdDate;  // 작성일시 created_date
    @Column

    private LocalDateTime updatedDate;  // 수정일시 updated_date

    @PrePersist //처음 저장될 때 실행
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate //수정될때 실행
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    @Column
    private String useAt; //사용여부

    @Column
    private int sort; //사용여부

    @Column
    private String hrefUrl; //이동할 url

    public static IntroductionEntity toSaveEntity(IntroductionDTO introductionDTO) {
        IntroductionEntity introductionEntity = new IntroductionEntity();

        introductionEntity.setTitle(introductionDTO.getTitle());
        introductionEntity.setContent(introductionDTO.getContent());
        introductionEntity.setCreatedId(introductionDTO.getCreatedId());
        introductionEntity.setCreatedName(introductionDTO.getCreatedName());
        introductionEntity.setUseAt(introductionDTO.getUseAt());
        introductionEntity.setSort(introductionDTO.getSort());
        introductionEntity.setDeleteAt("N");
        introductionEntity.setHrefUrl(introductionDTO.getHrefUrl());
        return introductionEntity;
    }
}
