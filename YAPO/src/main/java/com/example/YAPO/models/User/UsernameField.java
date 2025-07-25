package com.example.YAPO.models.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsernameField {
    @NotBlank
    @NotNull
    private String username;
}
