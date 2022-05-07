package com.whatscooking.application.recipes;

import android.os.Bundle;

import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;

public class SavedRecipesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);
    }
}
