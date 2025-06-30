package com.study.User.model;

import com.study.User.entity.UserEntity;
import com.study.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;               // 글 번호 (PK)
    private String loginId;          // 제목
    private String password;        // 내용
    private String name;         // 작성자
    private int level;         // 수정자
    private String deleteAt;        // 삭제여부 N, Y
    private LocalDateTime createdDate;  // 작성일시 created_date
    private LocalDateTime updatedDate;  // 수정일시 updated_date

    public static UserDTO toUserDto(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setLoginId(entity.getLoginId());
        dto.setPassword(entity.getPassword());
        dto.setName(entity.getName());
        dto.setLevel(entity.getLevel());
        dto.setDeleteAt(entity.getDeleteAt());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }
}
