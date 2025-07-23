package com.example.YAPO.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateField {
    @NotNull
    @NotBlank
    private String fieldName;
    @NotNull
    @NotBlank
    private String fieldValue;
}
