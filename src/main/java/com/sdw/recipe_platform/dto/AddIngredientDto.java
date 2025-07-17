package com.sdw.recipe_platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddIngredientDto {
    @NotNull
    private Long ingredientId;

    @NotNull
    private String quantity;
}
