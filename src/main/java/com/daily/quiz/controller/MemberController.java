package com.daily.quiz.controller;

import com.daily.quiz.dto.SignUpDTO;
import com.daily.quiz.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //처음 사이트에 들어오면 보이는 화면
    @GetMapping("/")
    public String mainPage(){
        return "/mainPage";
    }


    @GetMapping("/signUp")
    public String signUpGet(){
        return "/signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute SignUpDTO dto){

        memberService.signUp(dto);
        return "redirect:/";
    }

}
