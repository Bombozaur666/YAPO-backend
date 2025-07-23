package com.example.YAPO.models.plant;

import com.example.YAPO.models.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private Plant plant;

    @Column
    private boolean visible = true;

    @Column
    private Date noteDate = new Date();

    @Column
    private String comment;
}
