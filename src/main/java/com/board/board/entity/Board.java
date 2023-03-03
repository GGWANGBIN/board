package com.board.board.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;
    private String content;
    @Column
    private String writer;
    @CreatedDate
    private LocalDateTime create_date;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int hit;


}
