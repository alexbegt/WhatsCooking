package com.whatscooking.application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.whatscooking.application.registration.LoginActivity;
import com.whatscooking.application.user.SessionHandler;
import com.whatscooking.application.user.User;

public class MainActivity extends AppCompatActivity {

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        session = new SessionHandler(getApplicationContext());

        User user = session.getUserDetails();

        TextView welcomeText = findViewById(R.id.welcomeText);

        welcomeText.setText("Welcome " + user.getFirstName() + " " + user.getLastName() + ", your session will expire on " + user.getSessionExpirationDate());

        Button logoutBtn = findViewById(R.id.btnSignOut);

        logoutBtn.setOnClickListener(v -> {
            session.logoutUser();

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });
    }
}
