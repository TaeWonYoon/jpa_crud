package com.study.User.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;               // 글 번호 (PK)
    private String loginId;          // 제목
    private String password;        // 내용
    private String name;         // 작성자
    private int level;         // 수정자
    private String deleteAt;        // 삭제여부 N, Y
    private LocalDateTime createdDate;  // 작성일시 created_date
    private LocalDateTime updatedDate;  // 수정일시 updated_date
}
