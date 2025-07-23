package com.example.YAPO.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data
@Table(name = "Users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column()
    private String roles = "user";

    @Column()
    private Date registrationDate = new Date();

    @Column()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean locked = false;

    @Column()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean enabled = true;
}
