package com.daily.quiz.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class QuizSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //맞춘 사람 로그인을 안했으면 null처리 후 세션에만 저장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //로그인한 사용자 id, 비로그인이면 NULL값 처리
    private int totalCount; //출제 문제 수
    private int correctCount; //맞힌 문제 수
    @CreationTimestamp
    private LocalDateTime createdAt; //시작시간
    private LocalDateTime endedAt;  //종료시간

    @OneToMany(mappedBy = "quizSession", cascade = CascadeType.ALL)
    private List<QuizAnswer> answers = new ArrayList<>();


}
