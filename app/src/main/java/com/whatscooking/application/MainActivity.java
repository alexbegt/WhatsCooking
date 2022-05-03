package com.whatscooking.application;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.whatscooking.application.utilities.user.SessionHandler;
import com.whatscooking.application.utilities.user.User;

public class MainActivity extends AppCompatActivity {

    private SessionHandler session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_new);

        session = new SessionHandler(getApplicationContext());

        User user = session.getUserDetails();

//        TextView welcomeText = findViewById(R.id.welcomeText);
//
//        welcomeText.setText("Welcome " + user.getFirstName() + " " + user.getLastName() + ", your session will expire on " + user.getSessionExpirationDate());
//
//        Button logoutBtn = findViewById(R.id.btnSignOut);
//
//        logoutBtn.setOnClickListener(v -> {
//            session.logoutUser();
//
//            Intent i = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(i);
//            finish();
//        });
    }


}
