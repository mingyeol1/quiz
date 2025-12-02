package com.daily.quiz.service;

import com.daily.quiz.Repository.MemberRepository;
import com.daily.quiz.domain.Member;
import com.daily.quiz.dto.SignUpDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void signUp(SignUpDTO dto){
        Member member = new Member();
        member.signUp(dto.getUsername(), dto.getPassword(), dto.getNickname());

        memberRepository.save(member);
    }

}
