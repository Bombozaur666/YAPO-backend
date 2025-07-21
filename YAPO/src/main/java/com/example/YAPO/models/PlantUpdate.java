package com.example.YAPO.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PlantUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String oldValue;

    @Column
    private String newValue;
}
