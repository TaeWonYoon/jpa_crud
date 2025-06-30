package com.study.User.repository;

import com.study.User.entity.UserEntity;
import com.study.User.model.UserDTO;
import com.study.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // loginId가 존재하는지 여부를 반환 count > 0
    boolean existsByLoginId(String loginId);


    //로그인 where login_id={logInd} ANd delete_at =  'N'
    Optional<UserEntity> findByLoginIdAndDeleteAt(String loginId, String deleteAt);

}
