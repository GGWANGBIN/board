package com.board.board.service;

import com.board.board.dto.MemberDTO;
import com.board.board.entity.Board;
import com.board.board.entity.Member;
import com.board.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // 회원가입 처리
    public void memberRegister(MemberDTO memberDTO) {

        Member member = Member.toMemberEntity(memberDTO);

        memberRepository.save(member);
    }

    public void memberUpdate(Member member) {

        memberRepository.save(member);
    }

    public MemberDTO login(MemberDTO memberDTO) {

        Optional<Member> byUserid = memberRepository.findByUserid(memberDTO.getUserid());
        if (byUserid.isPresent()) {

            Member member = byUserid.get();
            if (member.getPwd().equals(memberDTO.getPwd())) {
                MemberDTO dto = MemberDTO.toMemberDTO(member);
                return dto;
            } else {

                return null;
            }
        } else {

            return null;
        }
    }

    public Page<Member> findAllMember(Pageable pageable) {

        return memberRepository.findAll(pageable);
    }

    public Page<Member> memberSearchList(String searchKeyword, Pageable pageable) {

        return memberRepository.findByUseridContaining(searchKeyword, pageable);
    }

    public Member memberView(Integer id) {

        return memberRepository.findById(id).get();
    }


    public void memberDelete(Integer id) {

        memberRepository.deleteById(id);
    }
}
