package com.whatscooking.application.utilities.api.response.recipe;

import androidx.annotation.NonNull;

public class RecipeImageResponse {

    private final String imageData;

    public RecipeImageResponse(String imageData) {
        this.imageData = imageData;
    }

    public String getImageData() {
        return this.imageData;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecipeImageResponse{" +
            "imageData='" + imageData + '\'' +
            '}';
    }
}
