package com.board.board.service;

import com.board.board.dto.MemberDTO;
import com.board.board.entity.Member;
import com.board.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;


    // 회원가입 처리
    @Transactional
    public void memberRegister(MemberDTO memberDTO) {

        Member member = Member.toMemberEntity(memberDTO);
        member.setPwd(passwordEncoder.encode(member.getPwd()));
        memberRepository.save(member);
    }

    public void memberUpdate(Member member) {

        memberRepository.save(member);
    }

    public MemberDTO login(MemberDTO memberDTO) {

        Optional<Member> byUserid = memberRepository.findByUserid(memberDTO.getUserid());

        if (byUserid.isPresent()) {

            Member member = byUserid.get();

            if(passwordEncoder.matches(memberDTO.getPwd(), member.getPwd())) {
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


    public int idCheck(String userid) {

        memberRepository.findByUserid(userid);

        if(memberRepository.findByUserid(userid).isPresent()) {
            return 1;
        }

        return 0;

    }


}
