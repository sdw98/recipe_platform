package com.sdw.recipe_platform.service;

import com.sdw.recipe_platform.dto.*;
import com.sdw.recipe_platform.model.Ingredient;
import com.sdw.recipe_platform.model.Recipe;
import com.sdw.recipe_platform.model.RecipeIngredient;
import com.sdw.recipe_platform.model.RecipeIngredientId;
import com.sdw.recipe_platform.repository.IngredientRepository;
import com.sdw.recipe_platform.repository.RecipeIngredientRepository;
import com.sdw.recipe_platform.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public Page<RecipeResponseDto> list(Pageable pageable) {
        return recipeRepository.findAll(pageable)
                .map(recipe -> new RecipeResponseDto(recipe.getId(), recipe.getTitle(), recipe.getDescription()));

    }

    public RecipeDetailDto get(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("레시피를 찾을 수 없습니다."));
        List<RecipeIngredientDto> ingredientDtos = recipe.getRecipeIngredients().stream()
                .map(
                        recipeIngredient -> new RecipeIngredientDto(
                                recipeIngredient.getIngredient().getId(),
                                recipeIngredient.getIngredient().getName(),
                                recipeIngredient.getQuantity()
                        )
                ).toList();

        return new RecipeDetailDto(recipe.getId(), recipe.getTitle(), recipe.getDescription(), ingredientDtos);
    }

    public RecipeResponseDto create(RecipeDto dto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        Recipe saved = recipeRepository.save(recipe);

        return new RecipeResponseDto(saved.getId(), saved.getTitle(), saved.getDescription());
    }

    public RecipeResponseDto update(Long id, RecipeDto dto) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("레시피가 존재하지 않습니다."));
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());
        Recipe saved = recipeRepository.save(recipe);
        return new RecipeResponseDto(saved.getId(), saved.getTitle(), saved.getDescription());
    }

    public void delete(Long id) {
        recipeRepository.deleteById(id);
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

    public void removeIngredient(Long recipeId, Long ingredientId) {
        RecipeIngredientId id = new RecipeIngredientId(recipeId, ingredientId);
        recipeIngredientRepository.deleteById(id);
    }
}
