package com.daily.quiz.dto;

import com.daily.quiz.domain.QuizAnswer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizQuestion {
    private Long id;
    private int questionOrder;
    private String word;
    private String example;
    private String translation;

    public QuizQuestion(QuizAnswer quizAnswer) {
        this.id = quizAnswer.getId();
        this.questionOrder = quizAnswer.getQuestionOrder();
        this.word = quizAnswer.getWord().getWord();
        this.example = quizAnswer.getWord().getExample();
        this.translation = quizAnswer.getWord().getTranslation();
    }
}
