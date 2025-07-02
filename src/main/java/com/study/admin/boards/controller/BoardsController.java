package com.study.admin.boards.controller;

import com.study.User.entity.UserEntity;
import com.study.User.model.UserDTO;
import com.study.admin.boards.service.BoardsService;
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
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/boards")
public class BoardsController {

    @Autowired
    private BoardsService boardsService;

    @Autowired
    private FileService fileService;

    private static final Logger log = LoggerFactory.getLogger(BoardsController.class);

    // 게시글 목록 조회
    @GetMapping({"/freeList", "faqList"})
    public String list(
                        HttpServletRequest request,
                        Model model,
                        @RequestParam(required = false) String searchType,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false) String registTy
                        ) {
        log.info("########### BoardsController GET list() start ###########");

        String uri = request.getRequestURI();
        log.info("uri={}", uri);

        if(uri.endsWith("freeList")) {
            registTy = "free";
        } else {
            registTy = "faq";
        }

        Map<String, Object> result = boardsService.boardList(searchType, keyword, startDate, endDate, registTy, pageable);

        model.addAttribute("boardList", result.get("boardList"));
        model.addAttribute("page", result.get("page"));

        // 검색조건 유지
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("registTy", registTy);

        return "admin/boards/list";
    }

    // 글 상세 조회
    @GetMapping({"/freeView/{id}", "faqView/{id}"})
    public String boardsView(@PathVariable Long id
                            , HttpServletRequest request
                            , Model model
                            ) {
        log.info("########### BoardsController GET boardView() start ###########");

        BoardDTO board = boardsService.getBoardById(id); //게시글 조회 (deleteAt 'N'인 항목만)
        if (board == null) {
            return "redirect:/board/freeList";
        }

        FileDTO boardFile = fileService.getFileByTableAndId("board", id.toString());

        String uri = request.getRequestURI();

        if (uri.contains("/freeView/")) { //uri에 따른 분기처리
            model.addAttribute("uri", "free");
        } else if (uri.contains("/faqView/")) {
            model.addAttribute("uri", "faq");
        }

        model.addAttribute("board", board);
        model.addAttribute("boardFile", boardFile);

        return "admin/boards/view";  // templates/board/view.html 뷰 이름
    }

    // 글 삭제
    @GetMapping({"/freeDelete/{id}", "faqDelete/{id}"})
    public String boardsDelete(@PathVariable Long id
            , HttpServletRequest request
            , Model model
    ) {
        log.info("########### BoardsController GET boardsDelete() start ###########");

        BoardEntity board = boardsService.deleteBoard(id); // 게시글 삭제 'Y'로 변경

        if (board == null) {
            return "redirect:/admin/boards/freeList";
        }

        String uri = request.getRequestURI();
        String redirectUrl = "";
        if (uri.contains("/freeDelete/")) { //uri에 따른 분기처리
            redirectUrl = "redirect:/admin/boards/freeList";
        } else if (uri.contains("/faqDelete/")) {
            redirectUrl = "redirect:/admin/boards/faqList";
        }

        log.info("redirectUrl={}", redirectUrl);
        return redirectUrl;
    }

    // 글 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        log.info("########### BoardsController GET writeForm() start ###########");
        model.addAttribute("board", new BoardDTO());
        return "admin/boards/write";
    }

    // 글 작성 처리
    @PostMapping("/write")
    public String write(BoardDTO boardDTO, HttpSession session) throws IOException {
        log.info("########### BoardsController POST write() start ###########");
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        boardDTO.setCreatedId(loginUser.getId()); //create_id 정보 넣기
        boardDTO.setCreatedName(loginUser.getName()); //create_id 정보 넣기
        boardsService.boardSave(boardDTO);
        return "redirect:/admin/boards/faqList";
    }

    // 글 수정 폼
    @GetMapping("/faqEdit/{id}")
    public String faqEdit(@PathVariable Long id, Model model) {
        log.info("########### BoardsController GET faqEdit() start ###########");
        BoardDTO board = boardsService.getBoardById(id);

        if (board == null) {
            return "redirect:/board/list";
        }

        FileDTO boardFile = fileService.getFileByTableAndId("board", id.toString());

        model.addAttribute("board", board);
        model.addAttribute("boardFile", boardFile);
        return "admin/boards/edit";
    }

    // 글 수정 처리
    @PutMapping("/faqEdit/{id}")
    public String edit(@PathVariable Long id,
                       @ModelAttribute BoardDTO boardDTO,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       HttpSession session) {
        log.info("########### BoardController PostMapping edit() start ###########");
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        boardDTO.setUpdatedId(loginUser.getId()); //create_id 정보 넣기
        boardsService.updateBoard(id, boardDTO, file);
        return "redirect:/admin/boards/faqView/" + id;
    }



}
