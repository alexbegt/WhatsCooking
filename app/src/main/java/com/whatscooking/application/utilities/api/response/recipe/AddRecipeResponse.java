package com.whatscooking.application.utilities.api.response.recipe;

import androidx.annotation.NonNull;

public class AddRecipeResponse {

    private final boolean successful;

    public AddRecipeResponse(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    @NonNull
    @Override
    public String toString() {
        return "AddRecipeResponse{" +
            "successful=" + successful +
            '}';
    }
}
