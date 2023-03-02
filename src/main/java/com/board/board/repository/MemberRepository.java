package com.board.board.repository;

import com.board.board.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByUserid(String userid);

    Page<Member> findByUseridContaining(String searchKeyword, Pageable pageable);
}
