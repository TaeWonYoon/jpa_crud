package com.study.User.controller;

import com.study.User.model.UserDTO;
import com.study.User.service.UserService;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // 글 작성 폼
    @GetMapping("/register")
    public String registerForm() {
        log.info("########### UserController GET register() start ###########");
        return "user/register";
    }

    @PostMapping("/user/register")
    public String register(@ModelAttribute UserDTO userDTO) {
        userService.saveUser(userDTO);
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 이동
    }
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
