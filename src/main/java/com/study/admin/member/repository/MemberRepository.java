package com.study.admin.member.repository;

import com.study.User.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MemberRepository extends JpaRepository<UserEntity, Long> {


    //관리자 회원 목록
    @Query("SELECT u FROM UserEntity u WHERE "
            + "u.deleteAt ='N' AND "
            + "(:loginId IS NULL OR u.loginId LIKE %:loginId%) AND "
            + "(:keyword IS NULL OR (u.loginId LIKE %:keyword% OR u.name LIKE %:keyword%)) AND "
            + "(:name IS NULL OR u.name LIKE %:name%) AND "
            + "(:level IS NULL OR u.level = :level) AND "
            + "(:startDateTime IS NULL OR u.createdDate >= :startDateTime) AND "
            + "(:endDateTime IS NULL OR u.createdDate <= :endDateTime)")
    Page<UserEntity> findBySearchConditions(String loginId,
                                            String name,
                                            Integer level,
                                            @Param("startDateTime") LocalDateTime startDateTime,
                                            @Param("endDateTime") LocalDateTime endDateTime,
                                            String keyword,
                                            Pageable pageable);
}
