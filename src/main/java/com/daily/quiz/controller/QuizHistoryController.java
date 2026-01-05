package com.daily.quiz.controller;

import com.daily.quiz.dto.MemberSecurityDTO;
import com.daily.quiz.dto.QuizSessionInfoDTO;
import com.daily.quiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class QuizHistoryController {

    private final QuizSessionService quizSessionService;

    @GetMapping("/quiz/history")
    public String quizHistory(Authentication authentication, Model model,
                              @RequestParam(defaultValue = "0") int page) {
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof MemberSecurityDTO)) {
            // 비로그인 사용자는 로그인 페이지로 리다이렉트 또는 에러 메시지
            return "redirect:/member/signIn";
        }

        MemberSecurityDTO memberDetails = (MemberSecurityDTO) authentication.getPrincipal();
        Long memberId = memberDetails.getId();
        Pageable pageable = PageRequest.of(page, 5); // 페이지 크기 5로 고정

        Page<QuizSessionInfoDTO> historyPage = quizSessionService.findInfoByMemberId(memberId, pageable);
        model.addAttribute("historyPage", historyPage);

        return "quiz/history";
    }
}
