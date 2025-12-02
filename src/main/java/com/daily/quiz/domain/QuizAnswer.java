package com.daily.quiz.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private QuizSession quizSession;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    private int questionOrder;

    private String memberAnswer;

    private Boolean isCorrect;

    private LocalDateTime answeredAt;
}
