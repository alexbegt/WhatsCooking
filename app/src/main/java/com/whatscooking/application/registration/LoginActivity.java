package com.whatscooking.application.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;
import com.whatscooking.application.utilities.api.RetrofitAPI;
import com.whatscooking.application.utilities.api.modal.registration.LoginModal;
import com.whatscooking.application.utilities.api.response.ErrorResponse;
import com.whatscooking.application.utilities.api.response.registration.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {

    private EditText etUsername;

    private EditText etPassword;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (session.isLoggedIn()) {
            loadFeed();
        }

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);

        progressBar = findViewById(R.id.progressLoggingIn);

        Button register = findViewById(R.id.btnLoginSignUp);
        Button login = findViewById(R.id.btnLogIn);

        register.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(i);
            finish();
        });

        login.setOnClickListener(v -> {
            LoginModal loginModal = new LoginModal(etUsername.getText().toString().toLowerCase().trim(),
                etPassword.getText().toString().trim());

            if (validateInputs(loginModal)) {
                loginUser(loginModal);
            }
        });
    }

    public void loginUser(LoginModal loginModal) {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<LoginResponse> call = retrofitAPI.loginUser(loginModal);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        LoginResponse responseFromAPI = response.body();

                        if (responseFromAPI.getEmail() != null && responseFromAPI.getFirstName() != null && responseFromAPI.getLastName() != null && responseFromAPI.getAccountId() != null) {
                            session.loginUser(loginModal.getUsername(), responseFromAPI.getEmail(), responseFromAPI.getFirstName(), responseFromAPI.getLastName(), responseFromAPI.getAccountId());

                            loadFeed();
                        } else {
                            Toast.makeText(getApplicationContext(), "Contact Administrator about your account", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.errorBody() != null) {
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(
                                response.errorBody().string(),
                                ErrorResponse.class);

                            Toast.makeText(getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validates inputs and shows error if any
     */
    private boolean validateInputs(LoginModal loginModal) {
        if (KEY_EMPTY.equals(loginModal.getUsername())) {
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(loginModal.getPassword())) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }
}
