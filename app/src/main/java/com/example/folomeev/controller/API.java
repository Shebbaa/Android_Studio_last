package com.example.folomeev.controller;

import com.example.folomeev.data.ChangePasswordToken;
import com.example.folomeev.data.Email;
import com.example.folomeev.data.ResponseUser;
import com.example.folomeev.data.User;
import com.example.folomeev.data.ChangePasswordToken;
import com.example.folomeev.data.ResponseUser;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
public interface API {
    @POST("signup")
    Call<ResponseUser> signUpByEmailAndPassword(@Header("apikey") String apiKey, @Body User user);

    @POST("token")
    Call<ResponseUser> login(@Query ("grant_type") String grant_type, @Header("apikey") String apikey, @Body User user);

    @POST("recover")
    Call<Void> sendCode(@Header("apikey") String apikey, @Body Email email);

    @POST("verify")
    Call<ResponseUser> verifyCode(@Header("apikey") String apikey, @Body com.example.folomeev.data.ChangePasswordToken changePasswordToken);

    @PUT("user")
    Call<Void> updatePassword(@Header("apikey") String apikey, @Header("Authorization") String token, @Body User user);
}
