package com.daily.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WordController {

    //현재 퀴즈에 나오는 word 단어 페이지 이동
    @GetMapping("/word")
    public String getWord(){

        return "/word/word";
    }
}
