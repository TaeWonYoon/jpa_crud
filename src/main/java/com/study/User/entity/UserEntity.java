package com.study.User.entity;

import com.study.User.model.UserDTO;
import com.study.board.model.BoardDTO;
import com.study.config.SecurityConfig;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name = "user")
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
        SecurityConfig securityConfig = new SecurityConfig();
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginId(dto.getLoginId());
        userEntity.setPassword(securityConfig.passwordEncoder().encode(dto.getPassword()));
        userEntity.setName(dto.getName());
        userEntity.setLevel(dto.getLevel() == 0 ? 1 : dto.getLevel()); // 기본 level=1
        userEntity.setCreatedDate(LocalDateTime.now());
        userEntity.setDeleteAt("N");
        return userEntity;
    }


}
