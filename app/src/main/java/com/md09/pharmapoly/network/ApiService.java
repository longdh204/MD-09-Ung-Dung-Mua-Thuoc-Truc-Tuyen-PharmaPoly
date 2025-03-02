package com.md09.pharmapoly.network;

import com.md09.pharmapoly.Models.Brand;
import com.md09.pharmapoly.Models.Category;
import com.md09.pharmapoly.Models.ProductCategory;
import com.md09.pharmapoly.Models.ProductDetails;
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
    @GET("product/{id}/reviews")
    Call<ApiResponse<List<ProductReview>>>
            getProductReviews(
                    @Path("id") String productId,
                    @Header("Authorization") String token);
    @GET
    Call<ApiResponse<Brand>> getBrandDetails(@Url String url, @Header("Authorization") String token);
    @GET
    Call<ApiResponse<ProductCategory>> getCategoryDetails(@Url String url, @Header("Authorization") String token);
    @GET
    Call<ApiResponse<Product>> getProduct(@Url String url, @Header("Authorization") String token);
    @GET
    Call<ApiResponse<ProductDetails>> getProductDetails(@Url String url, @Header("Authorization") String token);
    @POST("user/check-phone")
    Call<ApiResponse<Void>> checkPhone(@Body RequestBody request);

    @POST("user/login")
    Call<ApiResponse<User>> login(@Body RequestBody request);

    @POST("user/create-account")
    Call<ApiResponse<Void>> createAccount(@Body RequestBody request);


}


