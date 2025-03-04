package com.md09.pharmapoly.network;

import com.md09.pharmapoly.Models.Brand;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.data.model.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    // Đảm bảo rằng getProductReviews trả về đúng kiểu dữ liệu
    @GET
    Call<ApiResponse<List<ProductReview>>> getProductReviews(@Url String url, @Header("Authorization") String token);

    @GET("product/top-rated/{limit}")
    Call<ApiResponse<List<Product>>> getTopRatedProducts(
            @Path("limit") int limit,
            @Header("Authorization") String token);
//    @GET("product/{id}/reviews")
//    Call<ApiResponse<List<ProductReview>>>
//    getProductReviews(
//            @Path("id") String productId,
//            @Header("Authorization") String token);
    @GET("brand/{id}")
    Call<ApiResponse<Brand>>
    getBrandDetails(
            @Path("id") String brandId,
            @Header("Authorization") String token);

    @GET("product/{id}")
    Call<ApiResponse<Product>>
    getProduct(
            @Path("id") String id,
            @Header("Authorization") String token);
    @GET("product/{id}/details")
    Call<ApiResponse<Product>>
    getProductDetails(
            @Path("id") String id,
            @Header("Authorization") String token);
    @POST("user/check-phone")
    Call<ApiResponse<Void>>
    checkPhone(
            @Body RequestBody request);

    @POST("user/login")
    Call<ApiResponse<User>>
    login(@Body RequestBody request);

    @POST("user/create-account")
    Call<ApiResponse<Void>>
    createAccount(@Body RequestBody request);
}


