package com.whatscooking.application.utilities.api.response.recipe;

import androidx.annotation.NonNull;

import com.whatscooking.application.utilities.Recipe;

import java.util.Arrays;

public class RecipesResponse {

    private final boolean successful;

    private final Recipe[] recipes;

    private final String message;

    public RecipesResponse(boolean successful, Recipe[] recipes, String message) {
        this.successful = successful;
        this.recipes = recipes;
        this.message = message;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public Recipe[] getRecipes() {
        return this.recipes;
    }

    public String getMessage() {
        return this.message;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecipesResponse{" +
            "successful=" + successful +
            ", recipes=" + Arrays.toString(recipes) +
            ", message='" + message + '\'' +
            '}';
    }
}
