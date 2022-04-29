package com.whatscooking.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.whatscooking.application.registration.LoginActivity;
import com.whatscooking.application.user.SessionHandler;
import com.whatscooking.application.user.User;

public class MainActivity extends AppCompatActivity {

    private SessionHandler session;

    EditText titlehere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titlehere = (EditText) findViewById(R.id.titlehere);

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

    public void updateText (View vT){
        titlehere.setText(titlehere.getText());
    }
}
