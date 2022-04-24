package com.whatscooking.application.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.whatscooking.application.R;
import com.whatscooking.application.utilities.RequestQueueHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseLoginActivity {

    private final String LOGIN_URL = "https://whatscookingapp.azurewebsites.net/api/login/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (session.isLoggedIn()) {
            loadDashboard();
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPasword);

        Button register = findViewById(R.id.btnLoginSignUp);
        Button login = findViewById(R.id.btnLogin);

        //Launch Registration screen when Register Button is clicked
        register.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
            finish();
        });

        login.setOnClickListener(v -> {
            //Retrieve the data entered in the edit texts
            email = etEmail.getText().toString().toLowerCase().trim();
            password = etPassword.getText().toString().trim();
            if (validateInputs()) {
                login();
            }
        });
    }

    private void login() {
        displayLoader("Signing In.. Please wait...");

        JSONObject request = new JSONObject();

        try {
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST,
            LOGIN_URL,
            request, (Response.Listener<JSONObject>) response -> {
            pDialog.dismiss();
            try {
                if (response.getInt(KEY_STATUS) == 0) {
                    session.loginUser(email, response.getString(KEY_FULL_NAME));

                    loadDashboard();
                } else {
                    Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, (Response.ErrorListener) error -> {
            pDialog.dismiss();

            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueueHelper.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     *
     * @return
     */
    private boolean validateInputs() {
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

        return true;
    }
}
