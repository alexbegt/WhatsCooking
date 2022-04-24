package com.whatscooking.application.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.whatscooking.application.R;
import com.whatscooking.application.utilities.RequestQueueHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseLoginActivity {

    private EditText etConfirmPassword;
    private EditText etFullName;

    private String confirmPassword;
    private String fullName;

    private final String REGISTER_URL = "https://whatscookingapp.azurewebsites.net/api/login/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPasword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etFullName = findViewById(R.id.etFullName);

        Button login = findViewById(R.id.btnSignUpLogin);
        Button signUp = findViewById(R.id.btnSignUp);

        login.setOnClickListener(v -> {
            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(i);

            finish();
        });

        signUp.setOnClickListener(v -> {
            email = etEmail.getText().toString().toLowerCase().trim();
            password = etPassword.getText().toString().trim();
            confirmPassword = etConfirmPassword.getText().toString().trim();
            fullName = etFullName.getText().toString().trim();

            if (validateInputs()) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        displayLoader("Signing Up.. Please wait...");

        JSONObject request = new JSONObject();

        try {
            //Populate the request parameters
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_FULL_NAME, fullName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST,
            REGISTER_URL,
            request,
            (Response.Listener<JSONObject>) response -> {
                pDialog.dismiss();

                try {
                    //Check if user got registered successfully
                    if (response.getInt(KEY_STATUS) == 0) {
                        //Set the user session
                        session.loginUser(email, fullName);

                        loadDashboard();
                    } else if (response.getInt(KEY_STATUS) == 2) {
                        etEmail.setError("Email already in use!");
                        etEmail.requestFocus();
                    } else if (response.getInt(KEY_STATUS) == 1) {
                        etPassword.setError("Password doesn't meet standards!");
                        etPassword.requestFocus();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            (Response.ErrorListener) error -> {
                pDialog.dismiss();

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        );

        RequestQueueHelper.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     *
     * @return if the inputs are valid or not
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(fullName)) {
            etFullName.setError("Full Name cannot be empty");
            etFullName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(email)) {
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(password)) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(confirmPassword)) {
            etConfirmPassword.setError("Confirm Password cannot be empty");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Password and Confirm Password does not match");
            etConfirmPassword.requestFocus();

            return false;
        }

        return true;
    }
}
