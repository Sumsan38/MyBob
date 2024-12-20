package com.my.bob.core.domain.refrigerator.entity;

import com.my.bob.core.domain.base.entity.BaseEntity;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bob_recipe_ingredients", schema = "mybob")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeIngredients extends BaseEntity {
    @Id
    @Column(name = "detail_ingredient_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Size(max = 100)
    @Column(name = "amount", length = 100)
    private String amount;
}