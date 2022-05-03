package com.whatscooking.application.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;

public class FeedPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_page);
        
        Button savedRecipes = (Button) findViewById(R.id.btnSavedRecipes);

        savedRecipes.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SavedRecipesActivity.class);
            startActivity(i);
        });

        Button myRecipesBtn = (Button) findViewById(R.id.btnMyRecipes);

        myRecipesBtn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MyRecipesActivity.class);
            startActivity(i);
        });
    }
}
