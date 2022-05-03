package com.whatscooking.application.utilities.api.modal.recipe;

import androidx.annotation.NonNull;

public class AccountRecipesModal {

    public final String accountId;

    public AccountRecipesModal(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    @NonNull
    @Override
    public String toString() {
        return "AccountRecipesModal{" +
            "accountId='" + accountId + '\'' +
            '}';
    }
}
