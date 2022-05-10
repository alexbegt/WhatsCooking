package com.whatscooking.application.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;
import com.whatscooking.application.utilities.Recipe;
import com.whatscooking.application.utilities.RecipeFeedAdapter;
import com.whatscooking.application.utilities.api.RetrofitAPI;
import com.whatscooking.application.utilities.api.response.ErrorResponse;
import com.whatscooking.application.utilities.api.response.recipe.RecipesResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedPageActivity extends BaseActivity {

    private final ArrayList<Recipe> recipeArrayList = new ArrayList<>();

    private ProgressBar recipesLoadingProcess;
    private TextView recipesLoading;
    private Button retryLoadingBtn;
    private ListView recipeList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_page);

        recipeList = findViewById(R.id.recipes);
        recipesLoadingProcess = findViewById(R.id.recipesLoadingProcess);
        recipesLoading = findViewById(R.id.recipesLoading);

        retryLoadingBtn = findViewById(R.id.btnRetry);
        retryLoadingBtn.setOnClickListener(view -> fetchRecipes());

        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            fetchRecipes();
        });

        fetchRecipes();

        recipeList.setOnItemClickListener((parent, view, position, id) -> {
            Recipe recipe = recipeArrayList.get(position);

            if (recipe != null) {
                Intent i = new Intent(getApplicationContext(), RecipeCardActivity.class);

                Bundle bundle = new Bundle();

                bundle.putInt("recipe_id", recipe.getUniqueId());
                bundle.putString("recipe_title", recipe.getRecipeName());
                bundle.putString("recipe_ingredients", recipe.getIngredients());
                bundle.putString("recipe_steps", recipe.getInstructions());
                bundle.putString("recipe_category_tag", recipe.getCategoryTag());
                i.putExtras(bundle);

                startActivity(i);
            }
        });

        Button savedRecipes = findViewById(R.id.btnSavedRecipes);
        savedRecipes.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SavedRecipesActivity.class);
            startActivity(i);
        });

        Button myRecipesBtn = findViewById(R.id.btnMyRecipes);
        myRecipesBtn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MyRecipesActivity.class);
            startActivity(i);
        });
    }

    public void fetchRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<RecipesResponse> call = retrofitAPI.getRecipes();

        recipesLoadingProcess.setVisibility(View.VISIBLE);
        recipesLoading.setVisibility(View.VISIBLE);
        recipesLoading.setText(R.string.loading_recipes);
        retryLoadingBtn.setVisibility(View.INVISIBLE);
        recipeArrayList.clear();
        recipeList.setAdapter(null);

        call.enqueue(new Callback<RecipesResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipesResponse> call, @NonNull Response<RecipesResponse> response) {
                if (response.isSuccessful()) {
                    recipesLoadingProcess.setVisibility(View.INVISIBLE);
                    recipesLoading.setVisibility(View.INVISIBLE);
                    retryLoadingBtn.setVisibility(View.INVISIBLE);

                    if (response.body() != null) {
                        RecipesResponse responseFromAPI = response.body();

                        if (responseFromAPI.getRecipes() != null) {
                            recipeArrayList.addAll(Arrays.asList(responseFromAPI.getRecipes()));

                            if (recipeArrayList.size() != 0) {
                                RecipeFeedAdapter recipeFeedAdapter = new RecipeFeedAdapter(FeedPageActivity.this, recipeArrayList);
                                recipeList.setAdapter(recipeFeedAdapter);

                                return;
                            }
                        }
                    }

                    recipesLoading.setVisibility(View.VISIBLE);
                    recipesLoading.setText(R.string.no_recipes_found);
                    retryLoadingBtn.setVisibility(View.VISIBLE);

                    Log.w("Recipe Loading", "No recipes found: Internal Server Error");
                } else {
                    if (response.errorBody() != null) {
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(
                                response.errorBody().string(),
                                ErrorResponse.class);

                            Log.w("Recipe Loading error", errorResponse.getMessage());

                            Toast.makeText(getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.w("Recipe Loading error", e.getMessage());

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    } else {
                        Log.w("Recipe Loading error", response.message());

                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }

                    recipesLoadingProcess.setVisibility(View.INVISIBLE);
                    recipesLoading.setText(R.string.failed_to_load);
                    retryLoadingBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipesResponse> call, @NonNull Throwable t) {
                recipesLoadingProcess.setVisibility(View.INVISIBLE);
                recipesLoading.setText(R.string.failed_to_load);
                retryLoadingBtn.setVisibility(View.VISIBLE);

                Log.w("Recipe Loading error", t.getMessage());

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
