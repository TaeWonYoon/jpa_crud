package com.study.User.service;

import com.study.User.controller.UserController;
import com.study.User.entity.UserEntity;
import com.study.User.model.UserDTO;
import com.study.User.repository.UserRepository;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화용

    @Transactional
    public void saveUser(UserDTO userDTO) {
        log.info("########### UserService saveUser() start ###########");
        log.info("userDTO={}", userDTO.toString());
        // Entity로 변환
        UserEntity userEntity = UserEntity.toSaveEntity(userDTO);
        log.info("UserEntity={}", userEntity.toString());
        userRepository.save(userEntity);
    }

    public boolean isLoginIdDuplicate(String loginId) {
        log.info("########### UserService isLoginIdDuplicate() start ###########");

        return userRepository.existsByLoginId(loginId);
    }

    public UserDTO login(UserDTO dto) {
        UserEntity userEntity = userRepository.findByLoginIdAndDeleteAt(dto.getLoginId(), "N")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (userEntity == null) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 → DTO 반환
        return new UserDTO(
                userEntity.getId(),
                userEntity.getLoginId(),
                null,  // 비밀번호는 노출 안 함
                userEntity.getName(),
                userEntity.getLevel(),
                userEntity.getDeleteAt(),
                userEntity.getCreatedDate(),
                userEntity.getUpdatedDate()
        );
    }
}
