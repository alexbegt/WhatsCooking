package com.whatscooking.application.utilities.api.response.registration;

public class LoginResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String accountId;

    public LoginResponse(String firstName, String lastName, String email, String accountId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
