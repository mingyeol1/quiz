package com.daily.quiz.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //어느 세션인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private QuizSession quizSession;

    //출제된 단어
    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    //몇 문제를 도전할건지
    private int questionOrder;

    //사용자가 입력한 값
    private String memberAnswer;

    //맞췄는지
    private boolean correct;

    //답변시간
    private LocalDateTime answeredAt;
}
