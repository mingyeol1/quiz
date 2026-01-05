package com.daily.quiz.Repository;

import com.daily.quiz.domain.QuizSession;
import com.daily.quiz.dto.QuizSessionInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    @Query("SELECT new com.daily.quiz.dto.QuizSessionInfoDTO(qs.id, qs.createdAt, qs.totalCount, SUM(CASE WHEN qa.correct = true THEN 1 ELSE 0 END)) " +
           "FROM QuizSession qs LEFT JOIN qs.answers qa WHERE qs.member.id = :memberId " +
           "GROUP BY qs.id, qs.createdAt, qs.totalCount ORDER BY qs.createdAt DESC")
    Page<QuizSessionInfoDTO> findQuizHistoryByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT qs FROM QuizSession qs LEFT JOIN FETCH qs.member WHERE qs.id = :sessionId")
    Optional<QuizSession> findByIdWithMember(@Param("sessionId") Long sessionId);
}
