package com.daily.quiz.service;

import com.daily.quiz.Repository.WordRepository;
import com.daily.quiz.domain.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordService {
    public final WordRepository wordRepository;


    //모든 영단어 및 정답을 가져오는 메서드
    public List<Word> findByAll(){

        return wordRepository.findAll();
    }

    //영단어 검색1
    public Word findByWord(String word){
       Optional<Word> findWord = wordRepository.findByWord(word);

        //word의 값만 추출
       return findWord.orElseThrow(() -> new NoSuchElementException("존재하지 않는 단어입니다."));
    }

    //영단어 검색2
    public Word findByAnswer(String answer){
        Optional<Word> findAnswer = wordRepository.findByAnswer(answer);

        //word 객체 자체를 추출
        return findAnswer.orElseThrow(() -> new NoSuchElementException("존재하지 않는 정답입니다."));
    }
}
