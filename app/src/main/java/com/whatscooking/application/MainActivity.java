package com.whatscooking.application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.whatscooking.application.utilities.user.SessionHandler;
import com.whatscooking.application.utilities.user.User;

public class MainActivity extends AppCompatActivity {

    private SessionHandler session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_new);

        Button Login = (Button) findViewById(R.id.btnLogIn);

        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(),Feed_page.class);
                startActivity(i);
            }
        });

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
