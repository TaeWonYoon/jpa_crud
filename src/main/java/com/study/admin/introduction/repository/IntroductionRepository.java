package com.study.admin.introduction.repository;

import com.study.admin.introduction.entity.IntroductionEntity;
import com.study.file.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IntroductionRepository extends JpaRepository<IntroductionEntity, Long> {

    @Query("SELECT i FROM IntroductionEntity i WHERE "
            + "i.deleteAt ='N' AND "
            + "(:title IS NULL OR i.title LIKE %:title%) AND "
            + "(:createdName IS NULL OR i.createdName LIKE %:createdName%) AND "
            + "(:keyword IS NULL OR (i.title LIKE %:keyword% OR i.createdName LIKE %:keyword%)) AND "
            + "(:startDateTime IS NULL OR i.createdDate >= :startDateTime) AND "
            + "(:endDateTime IS NULL OR i.createdDate <= :endDateTime)")
    Page<IntroductionEntity> findBySearchConditions(String title,
                                                    String createdName,
                                                    LocalDateTime startDateTime,
                                                    LocalDateTime endDateTime,
                                                    String keyword,
                                                    Pageable pageable);

    //상세조회
    Optional<IntroductionEntity> findByIdAndDeleteAt(Long id, String deleteAt);

    // 삭제 여부(deleteAt = 'N') useAt = 'Y' Sort ASC
    List<IntroductionEntity> findTop3ByDeleteAtAndUseAtOrderBySortAsc(String deleteAt,String useAt);




}
