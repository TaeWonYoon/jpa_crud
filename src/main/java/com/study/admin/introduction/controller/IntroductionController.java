package com.study.admin.introduction.controller;

import com.study.User.model.UserDTO;
import com.study.admin.boards.service.BoardsService;
import com.study.admin.introduction.model.IntroductionDTO;
import com.study.admin.introduction.service.IntroductionService;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import com.study.file.model.FileDTO;
import com.study.file.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/introduction")
public class IntroductionController {

    @Autowired
    private IntroductionService introductionService;

    @Autowired
    private FileService fileService;

    private static final Logger log = LoggerFactory.getLogger(IntroductionController.class);

    // 게시글 목록 조회
    @GetMapping("list")
    public String list(
                        Model model,
                        @RequestParam(required = false) String searchType,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
                        ) {
        log.info("########### IntroductionController GET list() start ###########");

        Map<String, Object> result = introductionService.introductionList(searchType, keyword, startDate, endDate, pageable);

        model.addAttribute("introductionList", result.get("introductionList"));
        model.addAttribute("page", result.get("page"));

        // 검색조건 유지
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/introduction/list";
    }


    // 글 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        log.info("########### IntroductionController GET writeForm() start ###########");
        model.addAttribute("introduction", new IntroductionDTO());
        return "admin/introduction/write";
    }

    // 글 작성 처리
    @PostMapping("/write")
    public String write(IntroductionDTO introductionDTO, HttpSession session) throws IOException {
        log.info("########### IntroductionController POST write() start ###########");
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        introductionDTO.setCreatedId(loginUser.getId()); //create_id 정보 넣기
        introductionDTO.setCreatedName(loginUser.getName()); //create_id 정보 넣기
        introductionService.introductionSave(introductionDTO);
        return "redirect:/admin/introduction/list";
    }


    // 글 상세 조회
    @GetMapping("/view/{id}")
    public String boardsView(@PathVariable Long id
            , HttpServletRequest request
            , Model model
    ) {
        log.info("########### BoardsController GET boardView() start ###########");

        IntroductionDTO introduction = introductionService.getBoardById(id); //게시글 조회 (deleteAt 'N'인 항목만)

        model.addAttribute("introduction", introduction);


        return "admin/introduction/write";
    }
}
