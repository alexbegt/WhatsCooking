package com.whatscooking.application.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.whatscooking.application.R;
import com.whatscooking.application.utilities.api.modal.recipe.AccountRecipesModal;

public class SavedRecipesActivity extends AbstractFeedPageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_saved_recipes);

        super.onCreate(savedInstanceState);

        Button myRecipesBtn = findViewById(R.id.btnMyRecipes);

        myRecipesBtn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MyRecipesActivity.class);
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
        handleCallback(getRetrofitAPI().getFavoriteRecipesByAccount(new AccountRecipesModal(session.getUserDetails().getAccountId())));
    }
}
