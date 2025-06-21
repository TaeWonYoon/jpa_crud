package com.study.board.controller;

import com.study.board.model.BoardDTO;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;

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
    public String write(BoardDTO boardDTO) {
        log.info("########### BoardController POST write() start ###########");
        boardService.boardSave(boardDTO);
        return "redirect:/board/list";
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
