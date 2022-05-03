package com.whatscooking.application;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddRecipe extends AppCompatActivity {

    EditText titlehere;
    EditText inglist;
    EditText steplist;
    Button addimg;
    Button save;
    ImageView viewimg;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        titlehere = (EditText) findViewById(R.id.titlehere);

        addimg = (Button) findViewById(R.id.addimg);
        viewimg = findViewById(R.id.viewimg);
        save = findViewById(R.id.save);


        save.setOnClickListener(view -> {
            mGetContent.launch("image/*");
        });



    }

    /*Trouble with errors*/

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
        uri -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                viewimg.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                this.image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error selecting Image", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    );



    public void updateText (View vT){
        titlehere.setText(titlehere.getText());
        inglist.setText(inglist.getText());
        steplist.setText(steplist.getText());
    }
}
