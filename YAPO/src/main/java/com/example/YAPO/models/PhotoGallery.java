package com.example.YAPO.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class PhotoGallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Plant plant;

    @Column
    private Date createdAt = new Date();

    @Column
    private Date date;

    @Column
    private byte[] image;
}
