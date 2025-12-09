package com.daily.quiz.controller;

import com.daily.quiz.domain.QuizAnswer;
import com.daily.quiz.domain.QuizSession;
import com.daily.quiz.dto.QuizQuestion;
import com.daily.quiz.service.QuizAnswerService;
import com.daily.quiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        model.addAttribute("session", session);
        //questions를 모델에 추가하지 않고 Front에서 API를 통해 불러옴.
        // List<QuizAnswer> questions = quizAnswerService.findBySessionId(sessionId);
        // model.addAttribute("questions", questions);

        return "/quiz/play";
    }

//    @PostMapping("/quiz/submit")
//    public String quizSubmit(@RequestParam Long answerId,@RequestParam String memberAnswer){
//        quizAnswerService.quizSubmit(answerId,memberAnswer);
//
//        return "/quiz/result";
//    }

    @GetMapping("/quiz/result")
    public String quizResult() {
        return "quiz/result";
    }

    // API
    @GetMapping("/api/quiz/{sessionId}/questions")
    @ResponseBody
    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(@PathVariable Long sessionId) {
        List<QuizAnswer> questions = quizAnswerService.findBySessionId(sessionId);
        List<QuizQuestion> questionDTOs = questions.stream()
                .map(QuizQuestion::new) // QuizAnswer를 QuizQuestion DTO로 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionDTOs);
    }

    @PostMapping("/api/quiz/submit-answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestBody Map<String, String> payload) {
        Long answerId = Long.parseLong(payload.get("answerId"));
        String memberAnswer = payload.get("memberAnswer");

        Map<String, Object> result = quizAnswerService.quizSubmit(answerId, memberAnswer);

        return ResponseEntity.ok(result);
    }

}
