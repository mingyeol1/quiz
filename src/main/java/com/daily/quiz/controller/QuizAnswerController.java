package com.daily.quiz.controller;

import com.daily.quiz.Repository.QuizSessionRepository;
import com.daily.quiz.domain.QuizAnswer;
import com.daily.quiz.domain.QuizSession;
import com.daily.quiz.service.QuizAnswerService;
import com.daily.quiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizAnswerController {
    private final QuizAnswerService quizAnswerService;
    private final QuizSessionService quizSessionService;

    @GetMapping("/quizStart")
    public String QuizStartGet(){

        return "/quiz/quizStart";
    }

    @PostMapping("/quizStart")
    public String QuizStart(@RequestParam int count, @RequestParam(required = false) Long memberId){
        QuizSession session = quizAnswerService.createQuizSession(count, memberId);

        // 생성된 sessionId로 문제 풀이 페이지 이동
        return "redirect:/quiz/play?sessionId=" + session.getId();
    }

    @GetMapping("/quiz/play")
    public String playQuiz(@RequestParam Long sessionId, Model model) {

        QuizSession session = quizSessionService.findById(sessionId);

        List<QuizAnswer> questions = quizAnswerService.findBySessionId(sessionId);

        model.addAttribute("session", session);
        model.addAttribute("questions", questions);

        return "/quiz/play";
    }

    @PostMapping("/quiz/submit")
    public String quizSubmit(@RequestParam Long wordId,@RequestParam String memberAnswer){
        quizAnswerService.quizSubmit(wordId,memberAnswer);

        return "/quiz/result";
    }

}
