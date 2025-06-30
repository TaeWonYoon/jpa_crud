package com.study.admin.controller;

import com.study.User.model.UserDTO;
import com.study.admin.service.MemberService;
import com.study.board.model.BoardDTO;
import com.study.file.model.FileDTO;
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

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    // 회원가입 폼
    @GetMapping("/list")
    public String list(
                        @RequestParam(required = false) String searchType
                        , @RequestParam(required = false) String keyword
                        , @RequestParam(required = false) Integer level
                        , @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate
                        , @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
                        , @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
                        , Model model) {
        log.info("########### MemberController GET list() start ###########");
        log.info("level={}", level);
        Map<String, Object> result = memberService.userList(searchType,keyword, level,startDate, endDate, pageable);
        model.addAttribute("memberList", result.get("memberList"));
        model.addAttribute("page", result.get("page"));
        model.addAttribute("keyword", keyword);
        model.addAttribute("level", level);

        return "admin/member/list";
    }

    @GetMapping("/view/{id}")
    public String memberView(@PathVariable Long id, Model model) {
        log.info("########### MemberController GET memberView() start ###########");

        UserDTO member = memberService.getMemberById(id); //게시글 조회 (deleteAt 'N'인 항목만)
        if (member == null) {
            return "redirect:/admin/member/list";
        }

        log.info("member={}", member);
        model.addAttribute("member", member);

        return "admin/member/view";  // templates/board/view.html 뷰 이름
    }

}
