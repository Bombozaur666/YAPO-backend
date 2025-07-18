package com.example.YAPO.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plants")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column()
    private String name;
}
