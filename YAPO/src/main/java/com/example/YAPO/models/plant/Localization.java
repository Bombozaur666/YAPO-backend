package com.example.YAPO.models.plant;

import com.example.YAPO.models.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "localizations")
public class Localization {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

    @OneToMany(mappedBy = "localization", cascade = CascadeType.DETACH)
    private List<Plant> plants;
}
