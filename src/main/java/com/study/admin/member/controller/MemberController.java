package com.study.admin.member.controller;

import com.study.User.entity.UserEntity;
import com.study.User.model.UserDTO;
import com.study.admin.member.service.MemberService;
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

        LocalDate startDateChange = null;
        LocalDate endDateChange = null;

        Map<String, Object> result = memberService.userList(searchType,keyword, level,startDate, endDate, pageable);
        model.addAttribute("memberList", result.get("memberList"));
        model.addAttribute("page", result.get("page"));
        model.addAttribute("keyword", keyword);
        model.addAttribute("level", level);

        return "admin/member/list";
    }

    //상세조회
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

    // 회원정보 수정 처리
    @PutMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @ModelAttribute UserDTO userDTO,
                       RedirectAttributes redirectAttributes) {
        log.info("########### MemberController PostMapping edit() start ###########");
        UserEntity result = memberService.updateMember(id, userDTO);
        
        if(result != null) {
            redirectAttributes.addFlashAttribute("msg", "회원 정보가 수정되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("msg", "회원 정보가 실패.");
        }
        return "redirect:/admin/member/view/" + id;
    }

    // 회원탈퇴
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        log.info("########### MemberController GetMapping delete() start ############");
        UserEntity result = memberService.deleteMember(id);
        if(result != null) {
            redirectAttributes.addFlashAttribute("msg", "회원탈퇴 처리가 완료되었습니다..");
        } else {
            redirectAttributes.addFlashAttribute("msg", "회원탈퇴 실패.");
        }

        return "redirect:/admin/member/list";
    }

}
