package com.daily.quiz.controller;

import com.daily.quiz.dto.SignUpDTO;
import com.daily.quiz.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

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
        return "/member/signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute SignUpDTO dto){

        memberService.signUp(dto);
        return "redirect:/";
    }

    @GetMapping("/signIn")
    public String signInGet(){

        return "/member/signIn";
    }

    @PostMapping("/signIn")
    public String signIn(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         RedirectAttributes redirectAttributes){
        try {
            memberService.signIn(username, password);
            System.out.println("값이 잘못 입력됨?" + username + password);
        } catch (NoSuchElementException
                 | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            System.out.println("값이 잘못 입력됨?" + username + password);
            return "redirect:/member/signIn";
        }
        return "redirect:/";
    }

}
