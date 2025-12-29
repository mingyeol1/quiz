package com.daily.quiz.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

@Getter
@Setter
public class MemberSecurityDTO extends User {

    private Long id;
    private String username;
    private String password;
    private String nickname;


    public MemberSecurityDTO(Long id,
                             String username,
                             @Nullable String password,
                             String nickname,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;

    }
}
