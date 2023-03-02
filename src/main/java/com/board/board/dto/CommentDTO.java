package com.board.board.dto;

import com.board.board.entity.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Integer id;
    private String writer;
    private String comment_content;
    private LocalDateTime create_date;
    private Integer parentseq;


}
