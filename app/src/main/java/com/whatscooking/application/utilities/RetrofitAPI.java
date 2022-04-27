package com.whatscooking.application.utilities;

import com.whatscooking.application.utilities.api.modal.LoginModal;
import com.whatscooking.application.utilities.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    // as we are making a post request to post a data
    // so we are annotating it with post
    // and along with that we are passing a parameter as users
    @POST("login")
    //on below line we are creating a method to post our data.
    Call<LoginResponse> loginUser(@Body LoginModal loginModal);
}
