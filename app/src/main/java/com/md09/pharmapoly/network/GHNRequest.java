package com.md09.pharmapoly.network;

import static com.md09.pharmapoly.utils.Constants.GHN_URL;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GHNRequest {
    public final static String SHOPID = "191515";

    public final static String TokenGHN = "ef55b0b5-eb41-11ee-a6e6-e60958111f48";
    private GHNService ghnServicesInterface;

    public GHNRequest() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("ShopId", SHOPID)
                        .addHeader("Token",TokenGHN)
                        .build();
                return chain.proceed(request);
            }
        });

        ghnServicesInterface = new Retrofit.Builder()
                .baseUrl(GHN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(GHNService.class);
    }
    public GHNService callAPI() {
        return  ghnServicesInterface;
    }
}
