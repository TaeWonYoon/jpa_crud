package com.study.admin.member.controller;

import com.study.User.entity.UserEntity;
import com.study.User.model.UserDTO;
import com.study.admin.member.service.WithdrawalService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/withdrawal")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    private static final Logger log = LoggerFactory.getLogger(WithdrawalController.class);

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
        log.info("########### WithdrawalController GET list() start ###########");
        Map<String, Object> result = withdrawalService.userList(searchType,keyword, level,startDate, endDate, pageable);
        model.addAttribute("withdrawalList", result.get("withdrawalList"));
        model.addAttribute("page", result.get("page"));
        model.addAttribute("keyword", keyword);
        model.addAttribute("level", level);

        return "admin/withdrawal/list";
    }

    //상세조회
    @GetMapping("/view/{id}")
    public String WithdrawalView(@PathVariable Long id, Model model) {
        log.info("########### WithdrawalController GET WithdrawalView() start ###########");

        UserDTO withdrawal = withdrawalService.getWithdrawalById(id); //게시글 조회 (deleteAt 'N'인 항목만)
        if (withdrawal == null) {
            return "redirect:/admin/withdrawal/list";
        }

        log.info("Withdrawal={}", withdrawal);
        model.addAttribute("withdrawal", withdrawal);

        return "admin/withdrawal/view";  // templates/board/view.html 뷰 이름
    }

    // 회원복구
    @PutMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        log.info("########### WithdrawalController GetMapping update() start ############");
        UserEntity result = withdrawalService.updateWithdrawal(id);
        if(result != null) {
            redirectAttributes.addFlashAttribute("msg", "회원 복구 처리가 완료되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("msg", "회원복구 실패.");
        }

        return "redirect:/admin/withdrawal/list";
    }

}
