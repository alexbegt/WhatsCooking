package com.whatscooking.application.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.whatscooking.application.utilities.api.modal.registration.RegisterModal;
import com.whatscooking.application.utilities.api.response.ErrorResponse;
import com.whatscooking.application.utilities.api.response.registration.RegisterResponse;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends BaseActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;

    private ProgressBar progressBar;
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^" +
            "(?=.*[@#$%^&+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{8,}" +                // at least 8 characters
            "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressRegister);

        Button login = findViewById(R.id.btnSignUpLogin);
        Button signUp = findViewById(R.id.btnSignUp);

        login.setOnClickListener(v -> {
            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(i);

            finish();
        });

        signUp.setOnClickListener(v -> {
            RegisterModal registerModal = new RegisterModal(etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                etEmail.getText().toString().toLowerCase().trim(),
                etUsername.getText().toString().toLowerCase().trim(),
                etPassword.getText().toString().trim());

            if (validateInputs(registerModal)) {
                registerUser(registerModal);
            }
        });
    }

    private void registerUser(RegisterModal registerModal) {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<RegisterResponse> call = retrofitAPI.registerUser(registerModal);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        RegisterResponse responseFromAPI = response.body();

                        if (responseFromAPI.getAccountId() != null) {
                            session.loginUser(registerModal.getUsername(),
                                registerModal.getEmail(),
                                registerModal.getFirstName(),
                                registerModal.getLastName(),
                                responseFromAPI.getAccountId()
                            );

                            loadDashboard();
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
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validates inputs and shows error if any
     *
     * @return if the inputs are valid or not
     */
    private boolean validateInputs(RegisterModal registerModal) {
        if (KEY_EMPTY.equals(registerModal.getFirstName())) {
            etFirstName.setError("First Name cannot be empty");
            etFirstName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(registerModal.getLastName())) {
            etLastName.setError("Last Name cannot be empty");
            etLastName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(registerModal.getEmail())) {
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(registerModal.getUsername())) {
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(registerModal.getPassword())) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(registerModal.getEmail().trim()).matches()) {
            etEmail.setError("Invalid Email");
            etEmail.requestFocus();
            return false;
        }

        if (!PASSWORD_PATTERN.matcher(registerModal.getPassword().trim()).matches()) {
            etPassword.setError("Your password must be at least 8 characters and contain a special character.");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }
}
