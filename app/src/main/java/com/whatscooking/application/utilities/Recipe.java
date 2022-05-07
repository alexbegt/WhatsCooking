package com.whatscooking.application.utilities;

import androidx.annotation.NonNull;

public class Recipe {

    private final int recipeId;
    private final String ingredients;
    private final String instructions;
    private final String recipeName;
    private final String imageData;
    private final String category;
    private final int categoryId;
    private final int authorId;

    public Recipe(int recipeId, String ingredients, String instructions, String recipeName, String imageData, String category, int categoryId, int authorId) {
        this.recipeId = recipeId;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.recipeName = recipeName;
        this.imageData = imageData;
        this.category = category;
        this.categoryId = categoryId;
        this.authorId = authorId;
    }

    public int getUniqueId() {
        return this.recipeId;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public String getCategory() {
        return this.category;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public String getImageData() {
        return this.imageData;
    }

    public int getAuthorId() {
        return this.authorId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
            "uniqueId=" + this.recipeId +
            ", ingredients='" + this.ingredients + '\'' +
            ", instructions='" + this.instructions + '\'' +
            ", recipeName='" + this.recipeName + '\'' +
            ", imageData='" + this.imageData + '\'' +
            ", category=" + this.category + '\'' +
            ", categoryId=" + this.categoryId + '\'' +
            ", authorId=" + this.authorId +
            '}';
    }
}
