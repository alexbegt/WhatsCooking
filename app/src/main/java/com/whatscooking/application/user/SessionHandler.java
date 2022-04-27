package com.whatscooking.application.user;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class SessionHandler {

    private static final String PREF_NAME = "UserSession";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_ACCOUNT_ID = "account_id";

    private static final String KEY_EXPIRES = "expires";

    private static final String KEY_EMPTY = "";

    private final SharedPreferences.Editor mEditor;
    private final SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param email user email
     */
    public void loginUser(String username, String email, String firstName, String lastName, String accountId) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_EMAIL, email);

        mEditor.putString(KEY_FIRST_NAME, firstName);
        mEditor.putString(KEY_LAST_NAME, lastName);

        mEditor.putString(KEY_ACCOUNT_ID, accountId);

        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return if the user is logged in
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /*
        If shared preferences does not have a value then user is not logged in
         */
        if (millis == 0) {
            return false;
        }

        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }

        User user = new User();

        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setEmail(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));

        user.setFirstName(mPreferences.getString(KEY_FIRST_NAME, KEY_EMPTY));
        user.setLastName(mPreferences.getString(KEY_LAST_NAME, KEY_EMPTY));

        user.setAccountId(mPreferences.getString(KEY_ACCOUNT_ID, KEY_EMPTY));

        user.setSessionExpirationDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}
