package com.whatscooking.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddRecipe extends AppCompatActivity {

    EditText titlehere;
    EditText inglist;
    EditText steplist;
    Button addimg;
    ImageView viewimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        titlehere = (EditText) findViewById(R.id.titlehere);

        addimg = (Button) findViewById(R.id.addimg);
        viewimg = findViewById(R.id.viewimg);

        /*Trouble with errors*/

        viewimg.setOnClickListener(view -> {
            mGetContent.launch(input: "image/*");
        });



    }

    /*Trouble with errors*/

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
        uri -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
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
