package com.sdw.recipe_platform.repository;

import com.sdw.recipe_platform.model.RecipeIngredient;
import com.sdw.recipe_platform.model.RecipeIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {

}
