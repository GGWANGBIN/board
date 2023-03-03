package com.board.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
