package com.daily.quiz.service;

import com.daily.quiz.Repository.MemberRepository;
import com.daily.quiz.Repository.QuizAnswerRepository;
import com.daily.quiz.Repository.QuizSessionRepository;
import com.daily.quiz.Repository.WordRepository;
import com.daily.quiz.domain.QuizAnswer;
import com.daily.quiz.domain.QuizSession;
import com.daily.quiz.domain.Word;
import com.daily.quiz.dto.GeneratedQuizQuestion;
import com.daily.quiz.dto.QuizResultDTO;
import com.daily.quiz.dto.SaveQuizRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizAnswerService {

    private final QuizAnswerRepository quizAnswerRepository;
    private final WordRepository wordRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final MemberRepository memberRepository;

    public QuizSession createQuizSession(int questionCount, Long memberId) {

        // 1. 세션 생성
        QuizSession session = new QuizSession();
        session.setTotalCount(questionCount);
        //회원 가입을 하지 않아도 문제를 풀 수 있음
        session.setMember(memberId != null ? memberRepository.findById(memberId).orElse(null) : null);

        quizSessionRepository.save(session);

        //몇문제를 뽑을지 가져옴
        Pageable pageable = PageRequest.of(0, questionCount);

        // 2. 랜덤 단어 가져오기
        List<Word> words = wordRepository.findRandomWords(pageable);

        // 3. 문제 생성
        int order = 1;
        List<QuizAnswer> answers = new ArrayList<>();
        for (Word word : words) {
            QuizAnswer answer = new QuizAnswer();
            answer.setQuizSession(session);
            answer.setWord(word);
            answer.setQuestionOrder(order++);
            answers.add(answer);
        }
        quizAnswerRepository.saveAll(answers);

        return session;
    }

    //세션 찾기
    public List<QuizAnswer> findBySessionId(Long sessionId){
        return quizAnswerRepository.findByQuizSessionId(sessionId);
    }

    public List<QuizAnswer> findBySessionIdWithWord(Long sessionId) {
        return quizAnswerRepository.findBySessionIdWithWord(sessionId);
    }

    //문제 생성 메서드
    @Transactional(readOnly = true)
    public List<GeneratedQuizQuestion> generateUnsavedQuizQuestions(int questionCount) {
        Pageable pageable = PageRequest.of(0, questionCount);
        List<Word> words = wordRepository.findRandomWords(pageable);

        List<GeneratedQuizQuestion> questions = new ArrayList<>();
        //최소 1문제
        int order = 1;
        //문제 리스트 생성
        for (Word word : words) {
            questions.add(new GeneratedQuizQuestion(word, order++));
        }

        return questions;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> checkUnsavedQuizAnswer(Long wordId, String memberAnswer) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 단어입니다. ID: " + wordId));

        String correctAnswer = word.getAnswer();
        // 사용자 입력 정규화
        String userNormalized = normalize(memberAnswer);

        boolean isCorrect = Arrays.stream(correctAnswer.split(","))
                .map(String::trim)
                .map(this::normalize)   //현재 메서드 객체를 참조
                .anyMatch(ans -> ans.equals(userNormalized));

        Map<String, Object> result = new HashMap<>();
        result.put("isCorrect", isCorrect);
        result.put("correctAnswer", correctAnswer);

        return result;
    }

    //해당 정답 공백 제거
    private String normalize(String input) {
        return input
                .toLowerCase()
                .replaceAll("\\s+", ""); // 모든 공백 제거
    }

    @Transactional
    public QuizSession saveQuizResults(SaveQuizRequestDTO request) {
        // 1. 회원 정보 확인
        // DTO에 memberId가 null이거나, 존재하지 않는 회원이면 예외 발생
        Long memberId = request.getMemberId();
        if (memberId == null) {
            throw new IllegalArgumentException("로그인이 필요한 기능입니다.");
        }
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다. ID: " + memberId));

        // 2. 세션 생성 및 저장
        QuizSession session = new QuizSession();
        session.setTotalCount(request.getTotalCount());
        session.setMember(member);
        quizSessionRepository.save(session);

        // 3. QuizAnswer 리스트 생성
        List<QuizAnswer> answers = new ArrayList<>();
        for (QuizResultDTO resultDTO : request.getResults()) {
            Word word = wordRepository.findById(resultDTO.getWordId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 단어입니다. ID: " + resultDTO.getWordId()));

            QuizAnswer answer = new QuizAnswer();
            answer.setQuizSession(session);
            answer.setWord(word);
            answer.setQuestionOrder(resultDTO.getQuestionOrder());
            answer.setMemberAnswer(resultDTO.getMemberAnswer());
            answer.setCorrect(resultDTO.isCorrect());
            answer.setAnsweredAt(LocalDateTime.now()); // 저장 시점의 시간 기록
            answers.add(answer);
        }

        // 4. 모든 QuizAnswer 저장
        quizAnswerRepository.saveAll(answers);

        return session;
    }
}
