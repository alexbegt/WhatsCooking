package com.whatscooking.application.utilities;

import com.whatscooking.application.utilities.api.modal.LoginModal;
import com.whatscooking.application.utilities.api.modal.RegisterModal;
import com.whatscooking.application.utilities.api.response.LoginResponse;
import com.whatscooking.application.utilities.api.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("api/login")
    Call<LoginResponse> loginUser(@Body LoginModal loginModal);

    @POST("api/register")
    Call<RegisterResponse> registerUser(@Body RegisterModal registerModal);
}
