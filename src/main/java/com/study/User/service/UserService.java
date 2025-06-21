package com.study.User.service;

import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private BoardRepository boardRepository;

    public void save(BoardDTO dto) {
        BoardEntity entity = BoardEntity.toSaveEntity(dto); //dto에서 entity로 변환
        boardRepository.save(entity);
    }
}
