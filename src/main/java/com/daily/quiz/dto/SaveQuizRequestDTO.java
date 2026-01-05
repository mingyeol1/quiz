package com.daily.quiz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveQuizRequestDTO {
    private int totalCount;
    private Long memberId;
    private List<QuizResultDTO> results;
}
