package com.board.board.entity;

import com.board.board.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String userid;
    private String pwd;
    private String name;

    public static Member toMemberEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setUserid(memberDTO.getUserid());
        member.setPwd(memberDTO.getPwd());
        member.setName(memberDTO.getName());
        return member;
    }
}
