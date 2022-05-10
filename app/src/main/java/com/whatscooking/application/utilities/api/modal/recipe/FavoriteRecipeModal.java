package com.whatscooking.application.utilities.api.modal.recipe;

import androidx.annotation.NonNull;

public class FavoriteRecipeModal {

    private final String recipeId;
    private final String authorId;

    public FavoriteRecipeModal(String recipeId, String authorId) {
        this.recipeId = recipeId;
        this.authorId = authorId;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    @NonNull
    @Override
    public String toString() {
        return "FavoriteRecipeModal{" +
            "recipeId='" + recipeId + '\'' +
            ", authorId='" + authorId + '\'' +
            '}';
    }
}
