package com.board.board.controller;

import com.board.board.dto.MemberDTO;
import com.board.board.entity.Board;
import com.board.board.entity.Member;
import com.board.board.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원가입 페이지 요청
    @GetMapping("/member/register") //localhost:8080/board/write
    public String boardWriteForm() {

        return "memberregister";
    }


    @PostMapping("/member/registerpro")
    public String registerPro(@ModelAttribute MemberDTO memberDTO, Model model) {

        memberService.memberRegister(memberDTO);

        model.addAttribute("message", "회원가입 완료.");
        model.addAttribute("searchUrl", "/board/index");

        return "message";
    }

    // 로그인 페이지 요청
    @GetMapping("/member/login")
    public String login() {

        return "memberlogin";
    }

    @GetMapping("/member/modify/{id}")
    public String modifyMember(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("member", memberService.memberView(id));

        return "membermodify";
    }

    @GetMapping("/member/individualmodify/{id}")
    public String IndividualModifyMember(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("member", memberService.memberView(id));

        return "individualmodify";
    }


    @PostMapping("/member/update/{id}")
    public String modifyMemberPro(@PathVariable("id") Integer id, Member member, Model model,HttpSession session) {

        Member memberTemp = memberService.memberView(id);
        memberTemp.setName(member.getName());
        memberTemp.setUserid(member.getUserid());

        memberService.memberUpdate(memberTemp);

        model.addAttribute("message", "회원정보 수정 완료.");

        String loginmember = (String) session.getAttribute("loginUserid");

        System.out.println(loginmember);

            model.addAttribute("searchUrl", "/member/management");




        return "message";

    }@PostMapping("/member/individualupdate/{id}")
    public String individualUpdatePro(@PathVariable("id") Integer id, Member member, Model model,HttpSession session) {

        Member memberTemp = memberService.memberView(id);
        memberTemp.setName(member.getName());
        memberTemp.setUserid(member.getUserid());
        memberTemp.setPwd(member.getPwd());

        memberService.memberUpdate(memberTemp);

        model.addAttribute("message", "회원정보 수정 완료.");

        String loginmember = (String) session.getAttribute("loginUserid");

        System.out.println(loginmember);


            model.addAttribute("searchUrl", "/board/index");




        return "message";

    }


    @PostMapping("/member/loginpro")
    public String loginPro(@ModelAttribute MemberDTO memberDTO, Model model, HttpSession session,
                           @Valid Member member, BindingResult bindingResult) {

        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            session.setAttribute("loginUserid",loginResult.getUserid());
            session.setAttribute("loginid",loginResult.getId());

            String loginUserid = loginResult.getUserid();
            int loginid = loginResult.getId();

        //    System.out.println(loginUserid);

            model.addAttribute("message", loginUserid + "님 환영합니다!");
            model.addAttribute("searchUrl", "/board/index");
            return "message";
        } else {
            model.addAttribute("message", "아이디 또는 비밀번호가 맞지 않습니다.");
            model.addAttribute("searchUrl", "/member/login");
            return "message";
        }

    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session, Model model) {

        session.invalidate();

        model.addAttribute("message", "로그아웃 되었습니다.");
        model.addAttribute("searchUrl", "/board/index");
        return "message";
    }

    @GetMapping("/member/management")
    public String findAllMember(Model model,
                                @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                String searchKeyword) {

        Page<Member> list = null;

        if(searchKeyword == null) {
            list = memberService.findAllMember(pageable);
        } else {
            list = memberService.memberSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "memberlist";
    }

    @GetMapping("/member/delete/{id}")
    public String boardDelete(@PathVariable("id") Integer id, Member member, Model model) {

        memberService.memberDelete(id);

        model.addAttribute("message", "해당회원을 삭제하였습니다.");
        model.addAttribute("searchUrl", "/member/management");

        return "message";
    }




}
