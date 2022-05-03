package com.whatscooking.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Feed_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_page);
        Button savedrecipes = (Button) findViewById(R.id.savedrecipesBtn);

        savedrecipes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(),saved_recipes.class);
                startActivity(i);
            }
        });

        Button myrecipes = (Button) findViewById(R.id.myrecipesBtn);

        myrecipes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(),my_recipes.class);
                startActivity(i);
            }
        });
    }
}
