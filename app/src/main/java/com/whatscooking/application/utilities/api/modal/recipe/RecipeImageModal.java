package com.whatscooking.application.utilities.api.modal.recipe;

import androidx.annotation.NonNull;

public class RecipeImageModal {

    public final String recipeId;

    public RecipeImageModal(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecipeImageModal{" +
            "recipeId='" + recipeId + '\'' +
            '}';
    }
}
