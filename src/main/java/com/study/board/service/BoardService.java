package com.study.board.service;

import com.study.file.service.FileService;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.board.repository.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private FileService fileService;

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    public void boardSave(BoardDTO dto, MultipartFile file) throws IOException {
        log.info("########### BoardService boardSave() start ###########");


        BoardEntity entity = BoardEntity.toSaveEntity(dto); //dto에서 entity로 변환

        BoardEntity saveBoard = boardRepository.save(entity);

        // 2. 파일 있으면 저장
        if (file != null && !file.isEmpty()) {
            fileService.saveFile(file, saveBoard);
        }

    }

    public List<BoardDTO> boardList() {
        log.info("########### BoardService boardList() start ###########");

        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDto(boardEntity));
        }

        return boardDTOList;
    }

    public BoardDTO getBoardById(Long id) {
        Optional<BoardEntity> optional = boardRepository.findById(id);
        return optional.map(BoardDTO::toBoardDto).orElse(null);
    }



}
