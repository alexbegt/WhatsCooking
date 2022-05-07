package com.whatscooking.application.utilities.api.modal.registration;

import androidx.annotation.NonNull;

public class LoginModal {

    private final String username;
    private final String password;

    public LoginModal(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginModal{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
