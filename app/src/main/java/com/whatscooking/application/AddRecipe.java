package com.whatscooking.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddRecipe extends AppCompatActivity {

    EditText titlehere;
    EditText inglist;
    EditText steplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        titlehere = (EditText) findViewById(R.id.titlehere);


    }

    public void updateText (View vT){
        titlehere.setText(titlehere.getText());
        inglist.setText(inglist.getText());
        steplist.setText(steplist.getText());
    }
}
