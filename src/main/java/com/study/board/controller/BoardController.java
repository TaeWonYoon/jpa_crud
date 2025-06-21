package com.study.board.controller;

import com.study.board.model.BoardDTO;
import com.study.board.service.BoardService;
import com.study.file.model.FileDTO;
import com.study.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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
    public String list(Model model) {
        log.info("########### BoardController GET list() start ###########");
        log.info("boardService.boardList() == {} ", boardService.boardList());
        model.addAttribute("boardList", boardService.boardList());

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
    public String write(BoardDTO boardDTO, @RequestParam("file") MultipartFile file) throws IOException {
        log.info("########### BoardController POST write() start ###########");
        boardService.boardSave(boardDTO, file);
        return "redirect:/board/list";
    }

    // 글 상세 조회
    @GetMapping("/view/{id}")
    public String boardView(@PathVariable Long id, Model model) {
        log.info("########### BoardController GET boardView() start ###########");
        BoardDTO board = boardService.getBoardById(id);

        if (board == null) {
            return "redirect:/board/list";
        }

        FileDTO boardFile = fileService.getFileByTableAndId("board", id.toString());

        model.addAttribute("board", board);
        model.addAttribute("boardFile", boardFile);

        return "board/view";  // templates/board/view.html 뷰 이름
    }

    /*
    // 글 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "board/edit";
    }

    // 글 수정 처리
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Board board) {
        board.setId(id);
        boardService.save(board);
        return "redirect:/board/list";
    }

    // 글 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.deleteById(id);
        return "redirect:/board/list";
    }
     */
}
