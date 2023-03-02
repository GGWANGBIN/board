package com.board.board.entity;

import com.board.board.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userid;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String pwd;
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    public static Member toMemberEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setUserid(memberDTO.getUserid());
        member.setPwd(memberDTO.getPwd());
        member.setName(memberDTO.getName());
        return member;
    }
}
