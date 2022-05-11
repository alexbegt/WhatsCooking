package com.whatscooking.application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.whatscooking.application.registration.LoginActivity;

public class LoadingScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        int SPLASH_SCREEN_TIME_OUT = 2000;

        new Handler().postDelayed(() -> {
            Intent i = new Intent(LoadingScreenActivity.this, LoginActivity.class);

            startActivity(i);

            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
