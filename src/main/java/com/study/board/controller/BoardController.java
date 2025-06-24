package com.study.board.controller;

import com.study.User.model.UserDTO;
import com.study.board.model.BoardDTO;
import com.study.board.service.BoardService;
import com.study.file.model.FileDTO;
import com.study.file.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                       @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("########### BoardController GET list() start ###########");
        Map<String, Object> result = boardService.boardList(title, startDate, endDate, pageable);

        model.addAttribute("boardList", result.get("boardList"));
        model.addAttribute("page", result.get("page"));

        // 검색조건 유지
        model.addAttribute("title", title);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "board/list";
    }


    // 글 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        log.info("########### BoardController GET writeForm() start ###########");
        model.addAttribute("board", new BoardDTO());
        return "board/write";
    }


    // 글 작성 처리
    @PostMapping("/write")
    public String write(BoardDTO boardDTO, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        log.info("########### BoardController POST write() start ###########");
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        boardDTO.setCreatedId(loginUser.getId()); //create_id 정보 넣기
        boardService.boardSave(boardDTO, file);
        return "redirect:/board/list";
    }

    // 글 상세 조회
    @GetMapping("/view/{id}")
    public String boardView(@PathVariable Long id, Model model) {
        log.info("########### BoardController GET boardView() start ###########");
        boardService.increaseViews(id); //조회수 증가

        BoardDTO board = boardService.getBoardById(id); //게시글 조회 (deleteAt 'N'인 항목만)
        if (board == null) {
            return "redirect:/board/list";
        }

        FileDTO boardFile = fileService.getFileByTableAndId("board", id.toString());

        model.addAttribute("board", board);
        model.addAttribute("boardFile", boardFile);

        return "board/view";  // templates/board/view.html 뷰 이름
    }


    // 글 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("########### BoardController GET editForm() start ###########");
        BoardDTO board = boardService.getBoardById(id);

        if (board == null) {
            return "redirect:/board/list";
        }

        FileDTO boardFile = fileService.getFileByTableAndId("board", id.toString());

        model.addAttribute("board", board);
        model.addAttribute("boardFile", boardFile);
        return "board/edit";
    }


    // 글 수정 처리
    @PutMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @ModelAttribute BoardDTO boardDTO,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       HttpSession session) {
        log.info("########### BoardController PostMapping edit() start ###########");
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        boardDTO.setUpdatedId(loginUser.getId()); //create_id 정보 넣기
        boardService.updateBoard(id, boardDTO, file);
        return "redirect:/board/view/" + id;
    }

    // 글 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        log.info("########### BoardController GetMapping delete() start ############");
        boardService.deleteBoard(id);
        return "redirect:/board/list";
    }

}
