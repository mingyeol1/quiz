package com.daily.quiz.service;

import com.daily.quiz.Repository.QuizSessionRepository;
import com.daily.quiz.domain.QuizSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizSessionService {
    private final QuizSessionRepository quizSessionRepository;

    public QuizSession findById(Long sessionId){
        Optional<QuizSession> findSession = quizSessionRepository.findById(sessionId);

        return findSession.orElseThrow(() -> new NoSuchElementException("세션이 없습니다."));
    }

}
