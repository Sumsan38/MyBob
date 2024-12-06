package com.my.bob.v1.recipe.repository;

import com.my.bob.core.domain.recipe.entity.RecipeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeDetailRepository extends JpaRepository<RecipeDetail, Integer> {
}