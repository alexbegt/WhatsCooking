package com.whatscooking.application.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.whatscooking.application.BaseActivity;
import com.whatscooking.application.R;
import com.whatscooking.application.utilities.RequestQueueHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseActivity {

    private EditText etFirstName;
    private String firstName;

    private EditText etLastName;
    private String lastName;

    private EditText etEmail;
    private String email;

    private EditText etUsername;
    private String username;

    private EditText etPassword;
    private String password;

//    private EditText etConfirmPassword;
//    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

//        etFirstName = findViewById(R.id.etFirstName);
//        etLastName = findViewById(R.id.etLastName);
//        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPasword);

//        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        Button login = findViewById(R.id.btnSignUpLogin);
        Button signUp = findViewById(R.id.btnSignUp);

        login.setOnClickListener(v -> {
            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(i);

            finish();
        });

        signUp.setOnClickListener(v -> {
            firstName = etFirstName.getText().toString().trim();
            lastName = etLastName.getText().toString().trim();

            email = etEmail.getText().toString().toLowerCase().trim();
            username = etUsername.getText().toString().toLowerCase().trim();

            password = etPassword.getText().toString().trim();
            //confirmPassword = etConfirmPassword.getText().toString().trim();

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
            request.put(KEY_FIRST_NAME, firstName);
            request.put(KEY_LAST_NAME, lastName);

            request.put(KEY_EMAIL, email);
            request.put(KEY_USERNAME, username);

            request.put(KEY_PASSWORD, password);

            request.put(KEY_APPLICATION, APPLICATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST,
            REGISTER_URL,
            request,
            response -> {
                pDialog.dismiss();

                try {
                    //Check if user got registered successfully
                    if (response.getInt(KEY_STATUS) == 0) {
                        //Set the user session
                        //session.loginUser(email, fullName);

                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                        //loadDashboard();
                    } else if (response.getInt(KEY_STATUS) == 1) {
                        etPassword.setError("Password doesn't meet standards!");
                        etPassword.requestFocus();
                    } else if (response.getInt(KEY_STATUS) == 2) {
                        etUsername.setError("Username already in use!");
                        etUsername.requestFocus();
                    } else if (response.getInt(KEY_STATUS) == 3) {
                        etEmail.setError("Email already in use!");
                        etEmail.requestFocus();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            error -> {
                pDialog.dismiss();

                error.printStackTrace();

                if (error.getMessage() == null) {
                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        );

        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHelper.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     *
     * @return if the inputs are valid or not
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(firstName)) {
            etFirstName.setError("First Name cannot be empty");
            etFirstName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(lastName)) {
            etLastName.setError("Last Name cannot be empty");
            etLastName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(email)) {
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(username)) {
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(password)) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        // REMOVE DUE TO NOT BEING IN FINAL UI DESIGN
//        if (KEY_EMPTY.equals(confirmPassword)) {
//            etConfirmPassword.setError("Confirm Password cannot be empty");
//            etConfirmPassword.requestFocus();
//            return false;
//        }
//
//        if (!password.equals(confirmPassword)) {
//            etConfirmPassword.setError("Password and Confirm Password does not match");
//            etConfirmPassword.requestFocus();
//
//            return false;
//        }

        return true;
    }
}
