package com.example.YAPO.models.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @NotNull
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @Size(min = 8, max = 256)
    @NotBlank
    @NotNull
    private String password;

    @Column(nullable = false, unique = true)
    @Email
    @NotNull
    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column()
    private Date registrationDate = new Date();

    @Column()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean locked = false;

    @Column()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean enabled = false;
}
