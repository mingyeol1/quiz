package com.daily.quiz.Repository;

import com.daily.quiz.domain.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {

    List<QuizAnswer> findByQuizSessionId(Long sessionId);

}
