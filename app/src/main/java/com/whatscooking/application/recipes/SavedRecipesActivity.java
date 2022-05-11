package com.whatscooking.application.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.whatscooking.application.R;
import com.whatscooking.application.utilities.api.RetrofitAPI;
import com.whatscooking.application.utilities.api.modal.recipe.AccountRecipesModal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        handleCallback(retrofitAPI.getFavoriteRecipesByAccount(new AccountRecipesModal(session.getUserDetails().getAccountId())));
    }
}
