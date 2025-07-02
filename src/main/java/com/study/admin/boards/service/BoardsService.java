package com.study.admin.boards.service;

import com.study.User.entity.UserEntity;
import com.study.User.repository.UserRepository;
import com.study.admin.boards.repository.BoardsRepository;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.file.service.FileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardsService {

    private static final Logger log = LoggerFactory.getLogger(BoardsService.class);

    @Autowired
    private BoardsRepository boardsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    public Map<String, Object> boardList(String searchType, String keyword,LocalDate startDate, LocalDate endDate, String registTy,Pageable pageable) {
        log.info("########### BoardsService boardList() start ###########");

        // LocalDate → LocalDateTime 변환
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        String title = "";
        String createdName = "";
        if(!StringUtils.isEmpty(searchType)) {
            //검색 타입에 따른 키워드 넣어주기
            switch (searchType) {
                case "title":
                    title = keyword;
                    keyword = "";
                    break;
                case "createdName":
                    createdName = keyword;
                    keyword = "";
                    break;
                default:
                    break;
            }
        }

        // 검색 조건을 이용한 페이지 조회
        Page<BoardEntity> page = boardsRepository.findBySearchConditions(title, createdName, startDateTime, endDateTime, registTy, keyword, pageable);

        // DTO 변환
        List<BoardDTO> boardDTOList = page.stream()
                .map(BoardDTO::toBoardDto)
                .collect(Collectors.toList());

        // 결과 리턴
        Map<String, Object> result = new HashMap<>();
        result.put("boardList", boardDTOList);
        result.put("page", page);

        return result;

    }

    public BoardDTO getBoardById(Long id) {
        log.info("########### BoardsService getBoardById() start ###########");
        Optional<BoardEntity> optional = boardsRepository.findByIdAndDeleteAt(id, "N");
        return optional.map(BoardDTO::toBoardDto).orElse(null);
    }

    public BoardEntity deleteBoard(Long id){
        log.info("########### BoardsService deleteBoard() start ###########");
        BoardEntity board = boardsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원이 없습니다."));

        board.setDeleteAt("Y");

        return boardsRepository.save(board);
    }

    public void boardSave(BoardDTO boardDTO) throws IOException {
        log.info("########### BoardsService boardSave() start ###########");
        boardDTO.setRegistTy("faq"); // free -> 자유게시글 , faq -> faq게시글
        BoardEntity entity = BoardEntity.toSaveEntity(boardDTO); //dto에서 entity로 변환

        // user 객체 설정
        UserEntity user = userRepository.getReferenceById(boardDTO.getCreatedId()); // lazy
        entity.setUser(user);

        BoardEntity saveBoard = boardsRepository.save(entity);

    }

    //게시글 수정
    @Transactional
    public void updateBoard(Long id, BoardDTO dto, MultipartFile file) {
        log.info("########### BoardService updateBoard() start ###########");

        BoardEntity board = boardsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));
        // 게시글 내용 수정
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setUpdatedId(dto.getUpdatedId());
        boardsRepository.save(board);
    }
}
