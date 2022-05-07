package com.whatscooking.application.utilities.api.modal.recipe;

import androidx.annotation.NonNull;

public class AddRecipeModal {

    private final String ingredients;
    private final String instructions;
    private final String recipeName;
    private final String imageData;
    private final String category;
    private final String authorId;

    public AddRecipeModal(String recipeName, String ingredients, String instructions, String imageData, String category, String authorId) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageData = imageData;
        this.category = category;
        this.authorId = authorId;
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

    public String getRecipeName() {
        return this.recipeName;
    }

    public String getImageData() {
        return this.imageData;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    @NonNull
    @Override
    public String toString() {
        return "AddRecipeModal{" +
            "ingredients='" + this.ingredients + '\'' +
            ", instructions='" + this.instructions + '\'' +
            ", recipeName='" + this.recipeName + '\'' +
            ", imageData='" + this.imageData + '\'' +
            ", category=" + this.category + '\'' +
            ", authorId=" + this.authorId + '\'' +
            '}';
    }
}
