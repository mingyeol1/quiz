package com.daily.quiz.service;

import com.daily.quiz.Repository.MemberRepository;
import com.daily.quiz.Repository.QuizAnswerRepository;
import com.daily.quiz.Repository.QuizSessionRepository;
import com.daily.quiz.Repository.WordRepository;
import com.daily.quiz.domain.QuizAnswer;
import com.daily.quiz.domain.QuizSession;
import com.daily.quiz.domain.Word;
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

    //사용자가 정답(memberAnswer를 입력하면 정답을 체크하는 로직)
    //answerId는 해당 word가 저장된 answer를 검색
    @Transactional
    public Map<String, Object> quizSubmit(Long answerId, String memberAnswer) {
        QuizAnswer quizAnswer = quizAnswerRepository.findById(answerId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 문제입니다."));

        String correctAnswer = quizAnswer.getWord().getAnswer(); // 정답이 여러개일경우 ,로 구분해서 처리
        //대소문자가 들어울 수 있으니 모두 소문자 처리
        String userAnswer = memberAnswer.trim().toLowerCase();

        //  정답이 여러개일 경우
        boolean isCorrect = Arrays.stream(correctAnswer.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .anyMatch(ans -> ans.equals(userAnswer));

        //맞았는지 틀렸는지 여부
        quizAnswer.setIsCorrect(isCorrect);
        //내가 쓴 정답 입력
        quizAnswer.setMemberAnswer(memberAnswer);
        //맞춘시간대 입력
        quizAnswer.setAnsweredAt(LocalDateTime.now());

        Map<String, Object> result = new HashMap<>();
        result.put("isCorrect", isCorrect);
        result.put("correctAnswer", correctAnswer);

        return result;
    }

}
