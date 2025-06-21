package com.study.board.service;

import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.board.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    public void boardSave(BoardDTO dto) {
        log.info("########### BoardService boardSave() start ###########");

        BoardEntity entity = BoardEntity.toSaveEntity(dto); //dto에서 entity로 변환
        boardRepository.save(entity);
    }

    public List<BoardDTO> boardList() {
        log.info("########### BoardService boardList() start ###########");

        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDto(boardEntity));
        }

        return boardDTOList;
    }
}
