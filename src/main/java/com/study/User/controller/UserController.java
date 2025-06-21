package com.study.User.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

 /*
    @Autowired
    private BoardService boardService;


    @GetMapping("/list")
    public String list() {
        log.info("########### BoardController GET list() start ###########");
        return "board/list";
    }


    // 글 작성 폼
    @GetMapping("/register")
    public String registerForm() {
        log.info("########### BoardController GET writeForm() start ###########");
        return "board/write";
    }


    // 글 작성 처리
    @PostMapping("/register")
    public String register(UserDTO UserDTO) {
        log.info("########### BoardController POST write() start ###########");
        boardService.save(boardDTO);
        return "redirect:/board/list";
    }


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
