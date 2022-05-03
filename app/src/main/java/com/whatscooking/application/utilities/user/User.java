package com.whatscooking.application.utilities.user;

import androidx.annotation.NonNull;

public class User {

    String username;
    String email;
    String firstName;
    String lastName;
    String accountId;
//    Date sessionExpirationDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

//    public void setSessionExpirationDate(Date sessionExpirationDate) {
//        this.sessionExpirationDate = sessionExpirationDate;
//    }
//
//    public Date getSessionExpirationDate() {
//        return sessionExpirationDate;
//    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", accountId='" + accountId + '\'' +
//            ", sessionExpirationDate=" + sessionExpirationDate +
            '}';
    }
}
