package com.daily.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizResultDTO {
    private Long wordId;
    private String memberAnswer;
    private boolean correct;
    private int questionOrder;
}
