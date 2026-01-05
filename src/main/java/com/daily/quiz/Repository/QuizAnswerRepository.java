package com.daily.quiz.Repository;

import com.daily.quiz.domain.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {

    List<QuizAnswer> findByQuizSessionId(Long sessionId);

    @Query("SELECT qa FROM QuizAnswer qa JOIN FETCH qa.word WHERE qa.quizSession.id = :sessionId ORDER BY qa.questionOrder ASC")
    List<QuizAnswer> findBySessionIdWithWord(@Param("sessionId") Long sessionId);

    int countByQuizSessionIdAndCorrect(Long sessionId, boolean correct);
}
