package com.whatscooking.application.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.whatscooking.application.R;

public class FeedPageActivity extends AbstractFeedPageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feed_page);

        super.onCreate(savedInstanceState);

        Button savedRecipes = findViewById(R.id.btnSavedRecipes);

        savedRecipes.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SavedRecipesActivity.class);
            startActivity(i);
        });

        Button myRecipesBtn = findViewById(R.id.btnMyRecipes);

        myRecipesBtn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MyRecipesActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void fetchRecipes() {
        handleCallback(getRetrofitAPI().getRecipes());
    }
}
