package com.whatscooking.application;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.whatscooking.application.recipes.FeedPageActivity;
import com.whatscooking.application.utilities.api.RetrofitAPI;
import com.whatscooking.application.utilities.user.SessionHandler;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String KEY_EMPTY = "";

    protected final String URL = "https://whatscookingapp.azurewebsites.net";

    protected SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionHandler(getApplicationContext());
    }

    /**
     * Launch Feed Activity on Successful Sign Up/Sign In
     */
    protected void loadFeed() {
        Intent i = new Intent(getApplicationContext(), FeedPageActivity.class);
        startActivity(i);
        finish();
    }

    protected RetrofitAPI getRetrofitAPI() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .callTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build();

        return new Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAPI.class);
    }
}
