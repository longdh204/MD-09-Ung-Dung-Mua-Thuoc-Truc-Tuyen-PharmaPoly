package com.md09.pharmapoly.network;

import android.content.Context;

import com.md09.pharmapoly.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private ApiService requestInterface;

    public RetrofitClient() {
        OkHttpClient client = SafeOkHttpClient.getSafeOkHttpClient();

        requestInterface = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public ApiService callAPI() {
        return requestInterface;
    }
}
