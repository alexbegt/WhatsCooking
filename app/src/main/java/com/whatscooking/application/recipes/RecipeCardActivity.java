package com.whatscooking.application.recipes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;
import com.whatscooking.application.utilities.api.RetrofitAPI;
import com.whatscooking.application.utilities.api.modal.recipe.FavoriteRecipeModal;
import com.whatscooking.application.utilities.api.modal.recipe.RecipeImageModal;
import com.whatscooking.application.utilities.api.response.ErrorResponse;
import com.whatscooking.application.utilities.api.response.recipe.AddRecipeResponse;
import com.whatscooking.application.utilities.api.response.recipe.RecipeImageResponse;
import com.whatscooking.application.utilities.user.User;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeCardActivity extends BaseActivity {

    private ImageView imageView;

    private Button favoriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_card);

        User user = session.getUserDetails();

        Bundle bundle = getIntent().getExtras();

        int recipeId = bundle.getInt("recipe_id");
        String title = bundle.getString("recipe_title");
        String ingredients = bundle.getString("recipe_ingredients");
        String steps = bundle.getString("recipe_steps");
        String categoryTag = bundle.getString("recipe_category_tag");
        int authorId = bundle.getInt("recipe_author_id");

        imageView = findViewById(R.id.recipeImage);
        TextView txtView = findViewById(R.id.txtTitle);
        EditText txtIngredients = findViewById(R.id.txtIngredients);
        EditText txtSteps = findViewById(R.id.txtSteps);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        if (radioGroup != null) {
            RadioButton radioButton = radioGroup.findViewWithTag(categoryTag);

            if (radioButton != null)
                radioButton.setChecked(true);
        }

        RecipeImageModal recipeImageModal = new RecipeImageModal(String.valueOf(recipeId));
        getRecipeImage(recipeImageModal);

        txtView.setText(title);
        txtIngredients.setText(ingredients);
        txtSteps.setText(steps);

        favoriteBtn = findViewById(R.id.btnFavorite);

        favoriteBtn.setOnClickListener(v -> {
            favoriteBtn.setClickable(false);
            
            if (!String.valueOf(authorId).equals(user.getAccountId())) {
                FavoriteRecipeModal favoriteRecipeModal = new FavoriteRecipeModal(String.valueOf(recipeId), user.getAccountId());
                favoriteRecipe(favoriteRecipeModal);
            } else {
                Toast.makeText(getApplicationContext(), "You cannot favorite your own recipe!", Toast.LENGTH_SHORT).show();
            }
        });

        Button backBtn = findViewById(R.id.btnBack);

        backBtn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), FeedPageActivity.class);
            startActivity(i);
        });
    }

    public void getRecipeImage(RecipeImageModal recipeImageModal) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<RecipeImageResponse> call = retrofitAPI.getRecipeImage(recipeImageModal);

        call.enqueue(new Callback<RecipeImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeImageResponse> call, @NonNull Response<RecipeImageResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        RecipeImageResponse responseFromAPI = response.body();

                        if (!KEY_EMPTY.equals(responseFromAPI.getImageData()) && responseFromAPI.getImageData() != null) {
                            byte[] imageBytes = Base64.decode(responseFromAPI.getImageData(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            imageView.setImageBitmap(decodedImage);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (response.errorBody() != null) {
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(
                                response.errorBody().string(),
                                ErrorResponse.class);

                            Log.w("Recipe Card Error", errorResponse.getMessage());

                            Toast.makeText(getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.w("Recipe Card Error", e.getMessage());

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    } else {
                        Log.w("Recipe Card Error", response.message());

                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeImageResponse> call, @NonNull Throwable t) {
                Log.w("Recipe Card Error", t.getMessage());

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void favoriteRecipe(FavoriteRecipeModal favoriteRecipeModal) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<AddRecipeResponse> call = retrofitAPI.addRecipeToFavorites(favoriteRecipeModal);

        call.enqueue(new Callback<AddRecipeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddRecipeResponse> call, @NonNull Response<AddRecipeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AddRecipeResponse responseFromAPI = response.body();

                        if (responseFromAPI.isSuccessful()) {
                            favoriteBtn.setClickable(false);
                            Toast.makeText(getApplicationContext(), "Recipe added to favorites!", Toast.LENGTH_SHORT).show();

                            return;
                        } else {
                            favoriteBtn.setClickable(true);
                        }
                    }

                    Toast.makeText(getApplicationContext(), "Internal Server Error Favorite Recipe", Toast.LENGTH_SHORT).show();
                    favoriteBtn.setClickable(true);
                } else {
                    if (response.errorBody() != null) {
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(
                                response.errorBody().string(),
                                ErrorResponse.class);

                            Log.w("Favorite Recipe Error", errorResponse.getMessage());

                            favoriteBtn.setClickable(!errorResponse.getMessage().contains("Recipe already added to favorites"));

                            Toast.makeText(getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.w("Favorite Recipe Error", e.getMessage());

                            favoriteBtn.setClickable(!Objects.requireNonNull(e.getMessage()).contains("Recipe already added to favorites"));

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    } else {
                        Log.w("Favorite Recipe Error", response.message());

                        favoriteBtn.setClickable(!response.message().contains("Recipe already added to favorites"));

                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddRecipeResponse> call, @NonNull Throwable t) {
                Log.w("Favorite Recipe Error", t.getMessage());

                favoriteBtn.setClickable(!Objects.requireNonNull(t.getMessage()).contains("Recipe Already Added To Favorite"));

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
