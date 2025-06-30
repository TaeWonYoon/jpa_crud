package com.study.User.controller;

import com.study.User.model.UserDTO;
import com.study.User.service.UserService;
import com.study.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // 회원가입 폼
    @GetMapping("/register")
    public String registerForm() {
        log.info("########### UserController GET register() start ###########");
        return "user/register";
    }

    //회원가입
    @PostMapping("/register")
    public String register(@ModelAttribute UserDTO userDTO) {
        log.info("########### UserController POST register() start ###########");
        userService.saveUser(userDTO);
        return "redirect:/user/login"; // 회원가입 후 로그인 페이지로 이동
    }

    //아이디 중복 체크
    @GetMapping("/checkId")
    @ResponseBody
    public Map<String, Boolean> checkIdDuplicate(@RequestParam String loginId) {
        log.info("########### UserController GET checkIdDuplicate() start ###########");
        boolean exists = userService.isLoginIdDuplicate(loginId);
        return Collections.singletonMap("exists", exists);
    }

    // 로그인 폼
    @GetMapping("/login")
    public String loginForm() {
        log.info("########### UserController GET loginForm() start ###########");
        return "user/login";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO,
                        HttpSession session,
                        Model model) {
        log.info("########### UserController POST login() start ###########");

        // 서비스에서 로그인 처리
        try {
            UserDTO loginUser = userService.login(userDTO);
            session.setAttribute("loginUser", loginUser); // 세션 저장

            log.info("session={}", session.getAttribute("loginUser"));
            return "redirect:/"; // 로그인 성공 시 홈으로
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/login"; // 실패 시 로그인 폼으로
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("########### UserController GET Logout() start ###########");

        session.invalidate();  // 세션 무효화 (로그아웃)
        return "redirect:/";   // 홈으로 리다이렉트
    }


}
