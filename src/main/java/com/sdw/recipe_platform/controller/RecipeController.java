package com.sdw.recipe_platform.controller;

import com.sdw.recipe_platform.dto.AddIngredientDto;
import com.sdw.recipe_platform.dto.RecipeDto;
import com.sdw.recipe_platform.dto.RecipeResponseDto;
import com.sdw.recipe_platform.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping
    public RecipeResponseDto create(@Validated @RequestBody RecipeDto dto) {
        return recipeService.create(dto);
    }

    @PostMapping("/{id}/ingredients/add")
    public ResponseEntity<Void> addIngredient(
            @PathVariable Long id,
            @Validated @RequestBody AddIngredientDto dto
            ) {
        recipeService.addIngredient(id, dto);

        return ResponseEntity.ok()
                .build();
    }
}
