package com.daily.quiz.domain;

import com.daily.quiz.dto.SignUpDTO;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //유저의 로그인 id / id 값은 고유해야함
    @Column(unique = true)
    private String username;
    //유저의 패스워드
    private String password;
    //유저의 닉네임 / 닉네임값도 고유하도록 만듦.
    @Column(unique = true)
    private String nickname;


    //회원가입 로직
    public SignUpDTO signUp(String username, String password, String nickname){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        return new SignUpDTO(username, password, nickname);
    }
}
