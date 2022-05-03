package com.whatscooking.application.recipes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;
import com.whatscooking.application.utilities.api.RetrofitAPI;
import com.whatscooking.application.utilities.api.modal.recipe.AddRecipeModal;
import com.whatscooking.application.utilities.api.response.ErrorResponse;
import com.whatscooking.application.utilities.api.response.recipe.AddRecipeResponse;
import com.whatscooking.application.utilities.user.User;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddRecipeActivity extends BaseActivity {

    private String imageData;
    private ImageView imageView;

    private EditText title;
    private EditText ingredients;
    private EditText steps;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        User user = session.getUserDetails();

        title = findViewById(R.id.etRecipeTitle);
        ingredients = findViewById(R.id.etIngredients);
        steps = findViewById(R.id.etSteps);
        imageView = findViewById(R.id.recipe_image);
        radioGroup = findViewById(R.id.radioGroup);

        Button uploadPictureBtn = findViewById(R.id.btnUploadPicture);
        Button saveBtn = findViewById(R.id.btnSaveRecipe);
        Button discardBtn = findViewById(R.id.btnDiscardRecipe);

        uploadPictureBtn.setOnClickListener(view -> mGetContent.launch("image/*"));

        saveBtn.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(selectedId);

            if (validateInputs()) {
                AddRecipeModal addRecipeModal = new AddRecipeModal(
                    title.getText().toString().trim(),
                    ingredients.getText().toString().trim(),
                    steps.getText().toString().trim(),
                    this.imageData,
                    radioButton.getText().toString().trim(),
                    user.getAccountId()
                );

                if (validateInputs(addRecipeModal)) {
                    addRecipe(addRecipeModal);
                }
            }
        });

        discardBtn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MyRecipesActivity.class);
            startActivity(i);
            finish();
        });
    }

    // As Shreya had issues, implement image processing.
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
        uri -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                this.imageData = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error selecting Image", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    );

    /**
     * Validates inputs and shows error if any
     *
     * @return if the inputs are valid or not
     */
    private boolean validateInputs(AddRecipeModal addRecipeModal) {
        if (KEY_EMPTY.equals(addRecipeModal.getRecipeName())) {
            Toast.makeText(getApplicationContext(), "You must provide a recipe title!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (KEY_EMPTY.equals(addRecipeModal.getInstructions())) {
            Toast.makeText(getApplicationContext(), "You must provide steps for the recipe!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (KEY_EMPTY.equals(addRecipeModal.getIngredients())) {
            Toast.makeText(getApplicationContext(), "You must provide ingredients for the recipe!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (KEY_EMPTY.equals(addRecipeModal.getCategory())) {
            Toast.makeText(getApplicationContext(), "You must select a category!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (KEY_EMPTY.equals(addRecipeModal.getImageData()) || addRecipeModal.getImageData() == null) {
            Toast.makeText(getApplicationContext(), "You must upload a image!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (KEY_EMPTY.equals(addRecipeModal.getAuthorId())) {
            Toast.makeText(getApplicationContext(), "Missing author, please reopen the app!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(title.getText().toString())) {
            Toast.makeText(getApplicationContext(), "You must provide a recipe title!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (KEY_EMPTY.equals(steps.getText().toString())) {
            Toast.makeText(getApplicationContext(), "You must provide steps for the recipe!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (KEY_EMPTY.equals(ingredients.getText().toString())) {
            Toast.makeText(getApplicationContext(), "You must provide ingredients for the recipe!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "You must select a category!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (KEY_EMPTY.equals(this.imageData) || this.imageData == null) {
            Toast.makeText(getApplicationContext(), "You must upload a image!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void addRecipe(AddRecipeModal addRecipeModal) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<AddRecipeResponse> call = retrofitAPI.addRecipe(addRecipeModal);

        call.enqueue(new Callback<AddRecipeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddRecipeResponse> call, @NonNull Response<AddRecipeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AddRecipeResponse responseFromAPI = response.body();

                        if (responseFromAPI.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Contact Administrator about your account", Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toast.makeText(getApplicationContext(), "Internal Server Error Saving Recipe", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(
                                response.errorBody().string(),
                                ErrorResponse.class);

                            Log.w("Add Recipe Error", errorResponse.getMessage());

                            Toast.makeText(getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.w("Add Recipe Error", e.getMessage());

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    } else {
                        Log.w("Add Recipe Error", response.message());

                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddRecipeResponse> call, @NonNull Throwable t) {
                Log.w("Add Recipe Error", t.getMessage());

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
