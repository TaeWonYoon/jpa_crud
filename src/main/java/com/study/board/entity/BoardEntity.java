package com.study.board.entity;

import com.study.User.entity.UserEntity;
import com.study.board.model.BoardDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "board")
@NoArgsConstructor
public class BoardEntity {

    @Id // pk 컬럼, 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_inc
    private Long id;

    @Column(length = 45, nullable = false)
    private String title; //제목

    @Column(length = 1000, nullable = false)
    private String content; //내용

    /*
    @Column
    private int createdId; //등록자 아이디
*/
    @Column
    private int updatedId; //수정자 아이디

    @Column
    private int views;  // 조회수

    @Column
    private String deleteAt; //삭제 여부 N, Y

    @Column
    private LocalDateTime createdDate;  // 작성일시 created_date

    @Column
    private LocalDateTime updatedDate;  // 수정일시 updated_date

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_id", referencedColumnName = "id")
    private UserEntity user;  // 작성자

    @PrePersist //처음 저장될 때 실행
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate //수정될때 실행
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    public static BoardEntity toSaveEntity(BoardDTO dto) {
        BoardEntity entity = new BoardEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setViews(dto.getViews());
        return entity;
    }


}
