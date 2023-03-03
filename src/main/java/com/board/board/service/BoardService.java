package com.board.board.service;

import com.board.board.entity.Board;
import com.board.board.entity.Comment;
import com.board.board.repository.BoardRepository;
import com.board.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    // 글 작성 처리
    public void boardWrite(Board board) {

        boardRepository.save(board);
    }

    // 댓글 작성 처리
    public void commentWrite(Comment comment) {

        commentRepository.save(comment);
    }

    @Transactional
    public void commentEdit(Comment comment) {

        Comment EComment = commentRepository.findById(comment.getId()).get();

        EComment.setComment_content(comment.getComment_content());


    }



    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchListByTitle(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    public Page<Board> boardSearchListByWriter(String searchKeyword, Pageable pageable) {

        return boardRepository.findByWriterContaining(searchKeyword, pageable);
    }

    public Page<Board> boardSearchListByContent(String searchKeyword, Pageable pageable) {

        return boardRepository.findByContentContaining(searchKeyword, pageable);
    }


    // 특정 게시글 불러오기
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }


    // 특정 게시글 삭제
    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }

    public int updateHit(Integer id) {

        return boardRepository.updateHit(id);
    }



}
