package com.daily.quiz.Repository;

import com.daily.quiz.domain.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByWord(String word);
    Optional<Word> findByAnswer(String answer);

    @Query("SELECT w FROM Word w ORDER BY function('RAND')")
    List<Word> findRandomWords(Pageable pageable);

}
