package com.board.board.controller;

import com.board.board.dto.CommentDTO;
import com.board.board.entity.Board;
import com.board.board.entity.Comment;
import com.board.board.repository.BoardRepository;
import com.board.board.service.BoardService;
import com.board.board.service.CommentService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @Autowired
    private BoardService boardService;

    private final CommentService commentService;


    @GetMapping("/")
    public String home() {

        return "redirect:/board/index";
    }

    @GetMapping("/board/index")
    public String boardIndex() {

        return "boardindex";
    }



    @GetMapping("/board/write") //localhost:8080/board/write
    public String boardWriteForm() {

        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model) {

            boardService.boardWrite(board);

            model.addAttribute("message", "글 작성 완료.");
            model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @PostMapping("/comment/update")
    public String commentUpdatePro(Comment comment, Model model,HttpServletRequest request) {

        boardService.commentEdit(comment);

        model.addAttribute("message", "댓글수정 완료.");
        String referer = request.getHeader("Referer");
        model.addAttribute("searchUrl", referer);

        return "message";
    }


/*
    @GetMapping("/board/commentview")
    @ResponseBody
    public String readComment(HttpServletRequest request, HttpSession session) {

        String parentseq = request.getParameter("parentseq");

        System.out.println("!!!"+parentseq);

        int transParentseq = Integer.parseInt(parentseq);

        String loginuser = (String) session.getAttribute("loginUserid");

        List<Comment> commentList = commentService.getCommentList(transParentseq);

        JSONArray jsonArr = new JSONArray();

        if(commentList != null) {
            for(Comment comment : commentList) {
                JSONObject jsonObj = new JSONObject();

                LocalDateTime nomalTime = comment.getCreate_date();
                String writedate = nomalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println(writedate);

                jsonObj.put("id", comment.getId());
                jsonObj.put("comment_content", comment.getComment_content());
                jsonObj.put("writer", comment.getWriter());
                jsonObj.put("create_date", writedate);
                jsonObj.put("login", loginuser);

                jsonArr.add(jsonObj);

                System.out.println("갓진석" + jsonArr);
            }
        }

        return jsonArr.toString();

    }


 */

    @PostMapping("/board/commentwrite")
    @ResponseBody
    public String commentWritePro(@ModelAttribute CommentDTO commentDTO, Comment comment, Model model) {

        boardService.commentWrite(comment);

        model.addAttribute("success","success");


        return "success";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword, String searchType) {

        Page<Board> list = null;

        if(searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else if(searchKeyword != null && searchType.equals("title")){
            list = boardService.boardSearchListByTitle(searchKeyword, pageable);
        } else if(searchKeyword != null && searchType.equals("writer")){
            list = boardService.boardSearchListByWriter(searchKeyword, pageable);
        } else if(searchKeyword != null && searchType.equals("content")){
            list = boardService.boardSearchListByContent(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchType",searchType);
        model.addAttribute("searchKeyword",searchKeyword);

        return "boardlist";
    }

    @GetMapping("/board/view")
    public String boardView (Model model, Integer id, HttpSession session, HttpServletRequest request
                            , HttpServletResponse response) {


        List<Comment> commentList = commentService.getCommentList(id);

        System.out.println(commentList);



        String loginuser = (String) session.getAttribute("loginUserid");

        if(loginuser != null) {

        /* 조회수 로직 */
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("["+ id.toString() +"]")) {
                this.boardService.updateHit(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 1 * 1); 							// 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            this.boardService.updateHit(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 1 * 1); 								// 쿠키 시간
            response.addCookie(newCookie);
        }

        }



        model.addAttribute("board", boardService.boardView(id));
        model.addAttribute("comment", commentService.getCommentList(id));

        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {

        boardService.boardDelete(id);

        model.addAttribute("message", "글 삭제 완료.");
        model.addAttribute("searchUrl", "/board/list");
        
        return "message";
    }

    @GetMapping("/comment/delete")
    public String commentDelete(Integer id,Model model, HttpServletRequest  request) {

        commentService.commentDelete(id);

        model.addAttribute("message", "댓글 삭제 완료.");
        String referer = request.getHeader("Referer");
        model.addAttribute("searchUrl", referer);



        return "message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,Model model) {

        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model) {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.boardWrite(boardTemp);

        model.addAttribute("message", "글 수정 완료.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }
}
