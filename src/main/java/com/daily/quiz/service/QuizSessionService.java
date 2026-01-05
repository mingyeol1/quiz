package com.daily.quiz.service;

import com.daily.quiz.Repository.QuizSessionRepository;
import com.daily.quiz.domain.QuizSession;
import com.daily.quiz.dto.QuizSessionInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizSessionService {
    private final QuizSessionRepository quizSessionRepository;

    public QuizSession findById(Long sessionId){
        Optional<QuizSession> findSession = quizSessionRepository.findByIdWithMember(sessionId);

        return findSession.orElseThrow(() -> new NoSuchElementException("세션이 없습니다."));
    }

    public Page<QuizSessionInfoDTO> findInfoByMemberId(Long memberId, Pageable pageable) {
        return quizSessionRepository.findQuizHistoryByMemberId(memberId, pageable);
    }
}
