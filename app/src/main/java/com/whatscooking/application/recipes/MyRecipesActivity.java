package com.whatscooking.application.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.whatscooking.application.R;
import com.whatscooking.application.utilities.api.modal.recipe.AccountRecipesModal;

public class MyRecipesActivity extends AbstractFeedPageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_recipes);

        super.onCreate(savedInstanceState);

        ImageButton addRecipeBtn = findViewById(R.id.btnAddRecipe);

        addRecipeBtn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), AddRecipeActivity.class);
            startActivity(i);
            finish();
        });

        Button savedRecipes = findViewById(R.id.btnSavedRecipes);

        savedRecipes.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SavedRecipesActivity.class);
            startActivity(i);
            finish();
        });

        Button feedBtn = findViewById(R.id.btnFeed);

        feedBtn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), FeedPageActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    public void fetchRecipes() {
        handleCallback(getRetrofitAPI().getRecipesForAccount(new AccountRecipesModal(session.getUserDetails().getAccountId())));
    }
}
