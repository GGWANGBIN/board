package com.board.board.dto;

import com.board.board.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private Integer id;
    private String userid;
    private String pwd;
    private String name;

    public static MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setUserid(member.getUserid());
        memberDTO.setPwd(member.getPwd());
        memberDTO.setName(member.getName());
        return memberDTO;
    }
}
