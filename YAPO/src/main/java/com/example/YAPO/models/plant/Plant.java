package com.example.YAPO.models.plant;

import com.example.YAPO.models.User;
import com.example.YAPO.models.enums.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private User user;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private Localization localization;

    @JsonManagedReference
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<PlantUpdate> plantHistory;

    @JsonManagedReference
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<Note> notes;

    @JsonManagedReference
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @JsonManagedReference
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<PhotoGallery> photoGallery;

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
