package com.daily.quiz.service;

import com.daily.quiz.Repository.MemberRepository;
import com.daily.quiz.domain.Member;
import com.daily.quiz.domain.MemberRole;
import com.daily.quiz.dto.SignUpDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpDTO dto){
        Member member = new Member();
        member.signUp(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getNickname());

        //디폴트 값으로 USER값 저장
        member.addRole(MemberRole.USER);

        memberRepository.save(member);
    }

    public void signIn(String username, String password){
        Optional<Member> member = memberRepository.findByUsername(username);
        Member findMember = member.orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(password , findMember.getPassword())){
            throw new IllegalArgumentException("아이디 및 비밀번호가 다릅니다.");
        }

        System.out.println(findMember.getUsername());
        System.out.println(findMember.getNickname());
    }

}
