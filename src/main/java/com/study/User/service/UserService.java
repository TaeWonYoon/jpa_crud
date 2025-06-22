package com.study.User.service;

import com.study.User.entity.UserEntity;
import com.study.User.model.UserDTO;
import com.study.User.repository.UserRepository;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화용

    @Transactional
    public void saveUser(UserDTO userDTO) {

        // Entity로 변환
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginId(userDTO.getLoginId());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword())); // 비밀번호 암호화
        userEntity.setName(userDTO.getName());
        userEntity.setLevel(userDTO.getLevel() == 0 ? 1 : userDTO.getLevel()); // 기본 level=1
        userEntity.setCreatedDate(LocalDateTime.now());
        userEntity.setDeleteAt("N");

        userRepository.save(userEntity);
    }
}
