package com.study.User.entity;

import com.study.User.model.UserDTO;
import com.study.board.model.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor
public class UserEntity {

    @Id // pk 컬럼, 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_inc
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginId; // 사용자가 입력한 로그인 아이디

    @Column(nullable = false) //비밀번호
    private String password;

    @Column(nullable = false) //이름
    private String name;

    @Column(nullable = false) //이름
    private int level;

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


    public static UserEntity toSaveEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        return entity;
    }


}
