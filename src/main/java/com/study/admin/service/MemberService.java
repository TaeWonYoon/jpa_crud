package com.study.admin.service;

import com.study.User.controller.UserController;
import com.study.User.entity.UserEntity;
import com.study.User.model.UserDTO;
import com.study.admin.repository.MemberRepository;
import com.study.board.entity.BoardEntity;
import com.study.board.model.BoardDTO;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ObjectUtils;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화용


    public Map<String, Object> userList(
             String searchType
            , String keyword
            , Integer level
            , LocalDate startDate
            , LocalDate endDate
            , Pageable pageable) {
        log.info("########### MemberService userList() start ###########");

        //검색 조건 파라미터
        String loginId = null;
        String name = null;
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        if(!StringUtils.isEmpty(searchType)) {
            //검색 타입에 따른 키워드 넣어주기
            switch (searchType) {
                case "loginId":
                    loginId = keyword;
                    keyword = "";
                    break;
                case "name":
                    name = keyword;
                    keyword = "";
                    break;
                default:
                break;
            }
        }
        log.info("loginId={}", loginId);
        log.info("name={}", name);
        // 검색 조건을 이용한 페이지 조회
        Page<UserEntity> page = memberRepository.findBySearchConditions(loginId, name, level, startDateTime, endDateTime, keyword, pageable);
        // DTO 변환
        List<UserDTO> memberList = page.stream()
                .map(UserDTO::toUserDto)
                .collect(Collectors.toList());
        // 결과 리턴
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", memberList);
        result.put("page", page);

        return result;

    }

    public UserDTO getMemberById(Long id){
        log.info("########### BoardService getBoardById() start ###########");
        UserEntity user = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원이 없습니다."));

        return UserDTO.toUserDto(user);
    }
}
