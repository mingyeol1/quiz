package com.daily.quiz.Repository;

import com.daily.quiz.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    //로그인한 사용자 권한 확인
    @Query("select m from Member m left join fetch m.roleSet where m.username = :username")
    Optional<Member> findByUsernameWithRoles(@Param("username") String username);
}
