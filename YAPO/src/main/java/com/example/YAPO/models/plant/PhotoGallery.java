package com.example.YAPO.models.plant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photo_gallery")
public class PhotoGallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Plant plant;

    @Column
    private Boolean visible = true;

    @Column
    private Date createdAt = new Date();

    @Column
    private Date date;

    @Column
    private byte[] image;

    @Column
    private String title;

    @Column
    private String description;
}
