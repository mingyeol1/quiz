package com.daily.quiz.dto;

import com.daily.quiz.domain.Word;
import lombok.Getter;

@Getter
public class GeneratedQuizQuestion {
    private final Long wordId; // 정답 확인을 위해 Word의 ID를 가집니다.
    private final int questionOrder;
    private final String word;
    private final String example;
    private final String translation;

    public GeneratedQuizQuestion(Word word, int order) {
        this.wordId = word.getId();
        this.questionOrder = order;
        this.word = word.getWord();
        this.example = word.getExample();
        this.translation = word.getTranslation();
    }
}
