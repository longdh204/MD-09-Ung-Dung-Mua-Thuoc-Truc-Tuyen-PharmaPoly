package com.md09.pharmapoly.network;

import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.User;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("user/check-phone")
    Call<ApiResponse<Void>> checkPhone(@Body RequestBody request);
    @POST("user/login")
    Call<ApiResponse<User>> login(@Body RequestBody request);
    @POST("user/create-account")
    Call<ApiResponse<Void>> createAccount(@Body RequestBody request);
}
