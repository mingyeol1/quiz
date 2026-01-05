package com.daily.quiz.dto;

import com.daily.quiz.domain.QuizSession;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuizSessionInfoDTO {
    private Long sessionId;
    private LocalDateTime createdAt;
    private int totalCount;
    private int correctCount;

    public QuizSessionInfoDTO(QuizSession session, int correctCount) {
        this.sessionId = session.getId();
        this.createdAt = session.getCreatedAt();
        this.totalCount = session.getTotalCount();
        this.correctCount = correctCount;
    }

    public QuizSessionInfoDTO(Long sessionId, LocalDateTime createdAt, int totalCount, Long correctCount) {
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.totalCount = totalCount;
        this.correctCount = (correctCount == null) ? 0 : correctCount.intValue();
    }
}
