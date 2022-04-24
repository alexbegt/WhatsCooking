package com.whatscooking.application.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.whatscooking.application.MainActivity;
import com.whatscooking.application.user.SessionHandler;

public abstract class BaseLoginActivity extends AppCompatActivity {

    protected static final String KEY_STATUS = "status";
    protected static final String KEY_MESSAGE = "message";

    protected static final String KEY_EMPTY = "";
    protected static final String KEY_EMAIL = "email";
    protected static final String KEY_PASSWORD = "password";
    protected static final String KEY_FULL_NAME = "full_name";

    protected EditText etEmail;
    protected EditText etPassword;

    protected String email;
    protected String password;

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
        pDialog = new ProgressDialog(BaseLoginActivity.this);
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
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
