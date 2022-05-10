package com.whatscooking.application.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;

public class SavedRecipesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);
    }
    public void toMyRecipesActivity(View v){
        Intent i = new Intent(getApplicationContext(),MyRecipesActivity.class);
        startActivity(i);
    }
    public void toFeedPageActivity(View v){
        Intent i = new Intent(getApplicationContext(),FeedPageActivity.class);
        startActivity(i);
    }
}
