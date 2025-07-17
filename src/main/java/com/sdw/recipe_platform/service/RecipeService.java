package com.sdw.recipe_platform.service;

import com.sdw.recipe_platform.dto.AddIngredientDto;
import com.sdw.recipe_platform.dto.RecipeDto;
import com.sdw.recipe_platform.dto.RecipeResponseDto;
import com.sdw.recipe_platform.model.Ingredient;
import com.sdw.recipe_platform.model.Recipe;
import com.sdw.recipe_platform.model.RecipeIngredient;
import com.sdw.recipe_platform.model.RecipeIngredientId;
import com.sdw.recipe_platform.repository.IngredientRepository;
import com.sdw.recipe_platform.repository.RecipeIngredientRepository;
import com.sdw.recipe_platform.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeResponseDto create(RecipeDto dto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        Recipe saved = recipeRepository.save(recipe);

        return new RecipeResponseDto(saved.getId(), saved.getTitle(), saved.getDescription());
    }

    public void addIngredient(Long recipeId, AddIngredientDto dto) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NoSuchElementException("레시피를 찾을 수 없습니다."));
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new NoSuchElementException("재료를 찾을 수 없습니다."));

        RecipeIngredientId id = new RecipeIngredientId(recipeId, ingredient.getId());
        if (recipeIngredientRepository.existsById(id)) {
            throw new IllegalStateException("이미 추가된 재료 입니다.");
        }

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(id);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);

        recipe.getRecipeIngredients().add(recipeIngredient);

        recipeRepository.save(recipe);

    }
}
