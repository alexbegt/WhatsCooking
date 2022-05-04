package com.whatscooking.application;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.whatscooking.application.recipes.FeedPageActivity;
import com.whatscooking.application.utilities.user.SessionHandler;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String KEY_EMPTY = "";

    protected static final String KEY_APPLICATION = "application";
    protected static final String APPLICATION = "whats-cooking";

    protected final String URL = "https://whatscookingapp.azurewebsites.net";

    protected SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionHandler(getApplicationContext());
    }

    /**
     * Launch Feed Activity on Successful Sign Up/Sign In
     */
    protected void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), FeedPageActivity.class);
        startActivity(i);
        finish();
    }
}
