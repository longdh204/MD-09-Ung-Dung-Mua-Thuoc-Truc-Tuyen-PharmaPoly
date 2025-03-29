package com.md09.pharmapoly.network;

import android.content.Context;

import com.md09.pharmapoly.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private ApiService requestInterface;
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public RetrofitClient() {
        OkHttpClient client = SafeOkHttpClient.getSafeOkHttpClient();

        requestInterface = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }
    public static ApiService getVietQrApiService() {
        Retrofit vietQrRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.vietqr.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return vietQrRetrofit.create(ApiService.class);
    }
    public ApiService callAPI() {
        return requestInterface;
    }
    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openai.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}