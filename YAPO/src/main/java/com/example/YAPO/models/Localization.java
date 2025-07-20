package com.example.YAPO.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "localizations")
public class Localization {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
