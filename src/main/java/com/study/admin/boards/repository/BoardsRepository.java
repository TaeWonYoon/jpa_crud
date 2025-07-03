package com.study.admin.boards.repository;

import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardsRepository extends JpaRepository<BoardEntity, Long> {

    @Query("SELECT b FROM BoardEntity b WHERE "
            + "b.deleteAt ='N' AND "
            + "(:title IS NULL OR b.title LIKE %:title%) AND "
            + "(:createdName IS NULL OR b.createdName LIKE %:createdName%) AND "
            + "(:keyword IS NULL OR (b.title LIKE %:keyword% OR b.createdName LIKE %:keyword%)) AND "
            + "(:registTy IS NULL OR b.registTy LIKE %:registTy%) AND "
            + "(:startDateTime IS NULL OR b.createdDate >= :startDateTime) AND "
            + "(:endDateTime IS NULL OR b.createdDate <= :endDateTime)")
    Page<BoardEntity> findBySearchConditions(String title,
                                             String createdName,
                                             LocalDateTime startDateTime,
                                             LocalDateTime endDateTime,
                                             String registTy,
                                             String keyword,
                                             Pageable pageable);

    //상세조회
    Optional<BoardEntity> findByIdAndDeleteAt(Long id, String deleteAt);


    //SELECT * FROM board WHERE delete_at = 'N' AND regist_ty = 'free or faq' ORDER BY id DESC LIMIT 3;
    List<BoardEntity> findTop5ByDeleteAtAndRegistTyOrderByIdDesc(String deleteAt, String registTy);

}
