package com.board.board.service;

import com.board.board.dto.CommentDTO;
import com.board.board.entity.Board;
import com.board.board.entity.Comment;
import com.board.board.repository.BoardRepository;
import com.board.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;


    public List<Comment> getCommentList(int id) {

        return commentRepository.findByParentseqLike(id);
    }


    public void updateComment(Comment comment) {


    }

    public void commentDelete(Integer id) {

        commentRepository.deleteById(id);
    }
}
