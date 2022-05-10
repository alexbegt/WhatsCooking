package com.whatscooking.application.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;

public class MyRecipesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
    }
    public void toSavedRecipes(View v){
        Intent i = new Intent(getApplicationContext(),SavedRecipesActivity.class);
        startActivity(i);
    }
    public void toFeedPageActivity(View v){
        Intent i = new Intent(getApplicationContext(),FeedPageActivity.class);
        startActivity(i);
    }
}
