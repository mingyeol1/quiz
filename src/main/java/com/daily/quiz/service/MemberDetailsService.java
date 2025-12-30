package com.daily.quiz.service;

import com.daily.quiz.Repository.MemberRepository;
import com.daily.quiz.domain.Member;
import com.daily.quiz.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> findMember = memberRepository.findByUsernameWithRoles(username);

        Member member = findMember.orElseThrow(() -> new UsernameNotFoundException("존재하는 회원 x"));


        return new MemberSecurityDTO(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getNickname(),
                member.getRoleSet().stream().map(memberRole ->
                        new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                        .collect(Collectors.toList())
        );
    }
}
