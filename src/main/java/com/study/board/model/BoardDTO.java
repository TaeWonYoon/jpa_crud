package com.study.board.model;

import com.study.board.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;               // 글 번호 (PK)
    private String title;          // 제목
    private String content;        // 내용
    private Long createdId;         // 작성자
    private String authorName;         // 작성자 이름
    private Long updatedId;         // 수정자
    private int views;         // 조회수
    private String deleteAt;        // 삭제여부 N, Y
    private LocalDateTime createdDate;  // 작성일시 created_date
    private LocalDateTime updatedDate;  // 수정일시 updated_date
    private String registTy; //게시글 타입
    private String createdName; //등록자 이름


    public static BoardDTO toBoardDto(BoardEntity entity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(entity.getId());
        boardDTO.setTitle(entity.getTitle());
        boardDTO.setContent(entity.getContent());
        boardDTO.setCreatedId(entity.getCreatedId());
        boardDTO.setCreatedDate(entity.getCreatedDate());
        boardDTO.setViews(entity.getViews());

        if(entity.getUser() != null) {
            boardDTO.setAuthorName(entity.getUser().getName());
        } else {
            boardDTO.setAuthorName("알 수 없음");
        }

        return boardDTO;
    }
}
