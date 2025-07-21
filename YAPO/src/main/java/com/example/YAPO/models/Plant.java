package com.example.YAPO.models;

import com.example.YAPO.models.enums.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "plants")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String name;

    @Column()
    private String species;

    @Column()
    private Date purchaseDate;

    @Column()
    private String purchaseLocalization;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "localization_id", nullable = false)
    private Localization localization;

    @Column()
    @OneToMany(cascade = CascadeType.ALL)
    private List<Note> notes;

    @Column()
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column()
    @OneToMany(cascade = CascadeType.ALL)
    private List<PlantUpdate> plantHistory;

    @Column()
    private Date fertilizationDate;

    @Column
    private boolean alive = true;

    @Column
    private String deathReason;

    @Column()
    private Date wateringDate;

    @Column()
    private Date creationDate = new Date();

    @Column
    private List<PhotoGallery> photoGallery;

    @Column
    private byte[] avatar;

    @Column
    @Enumerated(EnumType.STRING)
    private PlantCondition plantCondition;

    @Column
    @Enumerated(EnumType.STRING)
    private PlantSoil plantSoil;

    @Column
    @Enumerated(EnumType.STRING)
    private PlantWatering plantWatering;

    @Column
    @Enumerated(EnumType.STRING)
    private PlantBerth plantBerth;

    @Column
    @Enumerated(EnumType.STRING)
    private PlantToxicity plantToxicity;

    @Column
    @Enumerated(EnumType.STRING)
    private PlantLifeExpectancy plantLifeExpectancy;

}
