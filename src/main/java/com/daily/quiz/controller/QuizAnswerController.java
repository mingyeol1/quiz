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
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import com.daily.quiz.dto.GeneratedQuizQuestion;
import com.daily.quiz.dto.MemberSecurityDTO;
import com.daily.quiz.dto.SaveQuizRequestDTO;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class QuizAnswerController {
    private final QuizAnswerService quizAnswerService;
    private final QuizSessionService quizSessionService;

    @GetMapping("/quizStart")
    public String QuizStartGet(Model model, Authentication authentication){
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberSecurityDTO memberSecurityDTO) {
            model.addAttribute("userNickname", memberSecurityDTO.getNickname());
            model.addAttribute("memberId", memberSecurityDTO.getId());
        } else {
            model.addAttribute("userNickname", "비 회원자 입니다.");
            model.addAttribute("memberId", null);
        }
        return "/quiz/quizStart";
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

    @GetMapping("/quiz/play/dynamic")
    public String playDynamicQuiz(@RequestParam int count, Model model, Authentication authentication) {
        model.addAttribute("questionCount", count);
        model.addAttribute("isDynamic", true); // A flag to tell the template
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberSecurityDTO memberSecurityDTO) {
            model.addAttribute("memberId", memberSecurityDTO.getId());
        } else {
            model.addAttribute("memberId", null);
        }
        return "/quiz/play";
    }

//    @PostMapping("/quiz/submit")
//    public String quizSubmit(@RequestParam Long answerId,@RequestParam String memberAnswer){
//        quizAnswerService.quizSubmit(answerId,memberAnswer);
//
//        return "/quiz/result";
//    }

    @GetMapping("/quiz/result")
    public String quizResult(@RequestParam Long sessionId, Authentication authentication, Model model) {
        // 세션 정보 조회
        QuizSession session = quizSessionService.findById(sessionId);

        // 권한 확인: 비회원 세션이거나, 로그인한 사용자가 세션의 주인이어야 함
        if (session.getMember() != null) {
            if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof MemberSecurityDTO)) {
                return "redirect:/member/signIn"; // 비로그인 사용자는 내역을 볼 수 없음
            }
            MemberSecurityDTO memberDetails = (MemberSecurityDTO) authentication.getPrincipal();
            if (!session.getMember().getId().equals(memberDetails.getId())) {
                // 자신의 퀴즈 결과만 볼 수 있음 (추후 필요시 관리자 예외 처리)
                // 여기서는 간단히 홈으로 리다이렉트
                return "redirect:/";
            }
        }

        List<QuizAnswer> answers = quizAnswerService.findBySessionIdWithWord(sessionId);
        int correctCount = (int) answers.stream().filter(QuizAnswer::isCorrect).count();

        model.addAttribute("session", session);
        model.addAttribute("answers", answers);
        model.addAttribute("correctCount", correctCount);

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

//    @PostMapping("/api/quiz/submit-answer")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestBody Map<String, String> payload) {
//        //JSON으로 값을 받아오기 때문에 Long타입으로 변환.
//        Long answerId = Long.parseLong(payload.get("answerId"));
//        String memberAnswer = payload.get("memberAnswer");
//
//        Map<String, Object> result = quizAnswerService.quizSubmit(answerId, memberAnswer);
//
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/api/quiz/generate/{questionCount}")
    @ResponseBody
    public ResponseEntity<List<GeneratedQuizQuestion>> generateUnsavedQuiz(
            @PathVariable int questionCount
    ) {
        List<GeneratedQuizQuestion> questions = quizAnswerService.generateUnsavedQuizQuestions(questionCount);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/api/quiz/check-answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUnsavedAnswer(@RequestBody Map<String, String> payload) {
        Long wordId = Long.parseLong(payload.get("wordId"));
        String memberAnswer = payload.get("memberAnswer");

        Map<String, Object> result = quizAnswerService.checkUnsavedQuizAnswer(wordId, memberAnswer);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/quiz/save-results")
    @ResponseBody
    public ResponseEntity<?> saveQuizResults(@RequestBody SaveQuizRequestDTO request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof MemberSecurityDTO memberSecurityDTO)) {
            // 비로그인 사용자인 경우, 권한 없음 에러 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 요청에 포함된 memberId와 현재 로그인한 사용자의 ID가 일치하는지 확인
        if (!memberSecurityDTO.getId().equals(request.getMemberId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("자신의 퀴즈 결과만 저장할 수 있습니다.");
        }

        try {
            QuizSession newSession = quizAnswerService.saveQuizResults(request);
            Map<String, Long> response = Map.of("sessionId", newSession.getId());
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
