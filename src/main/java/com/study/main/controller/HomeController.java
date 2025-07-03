package com.study.main.controller;

import com.study.admin.boards.controller.BoardsController;
import com.study.admin.boards.service.BoardsService;
import com.study.admin.introduction.model.IntroductionDTO;
import com.study.board.model.BoardDTO;
import com.study.main.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomeService homeService;

    @GetMapping("/")
    public String main(Model model) {
        log.info("########### HomeController GET list() start ###########");
        List<BoardDTO> freeList = homeService.boardList("free");
        log.info("freeList={}", freeList);

        List<BoardDTO> faqList = homeService.boardList("faq");
        log.info("faqList={}", faqList);

        List<IntroductionDTO> introductionList = homeService.introductionList();
        log.info("introductionList={}", introductionList);

        model.addAttribute("freeList", freeList);
        model.addAttribute("faqList", faqList);
        model.addAttribute("introductionList", introductionList);

        return "/index";

    }


}
