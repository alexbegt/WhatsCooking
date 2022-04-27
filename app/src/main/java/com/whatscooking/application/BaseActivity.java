package com.whatscooking.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.whatscooking.application.user.SessionHandler;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String KEY_STATUS = "status";
    protected static final String KEY_MESSAGE = "message";

    protected static final String KEY_EMPTY = "";

    protected static final String KEY_EMAIL = "email";
    protected static final String KEY_USERNAME = "username";
    protected static final String KEY_PASSWORD = "password";

    protected static final String KEY_FIRST_NAME = "first_name";
    protected static final String KEY_LAST_NAME = "last_name";

    protected static final String KEY_APPLICATION = "application";
    protected static final String APPLICATION = "whats-cooking";

    protected final String REGISTER_URL = "https://whatscookingapp.azurewebsites.net/api/register";
    protected final String LOGIN_URL = "http://192.168.1.13:3000/api/login";

    protected ProgressDialog pDialog;

    protected SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionHandler(getApplicationContext());
    }

    /**
     * Display Progress bar while registering
     */
    protected void displayLoader(String message) {
        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
    protected void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
