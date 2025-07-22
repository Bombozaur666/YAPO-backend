package com.example.YAPO.models.plant;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Plant plant;

    @Column()
    private String note;

    @Column
    private Date noteDate = new Date();
}
