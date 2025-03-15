package com.md09.pharmapoly.network;

import com.md09.pharmapoly.Models.Brand;
import com.md09.pharmapoly.Models.Cart;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.Models.PageData;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.Models.Question;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.data.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @POST("refresh-token")
    Call<ApiResponse<User>> refreshToken(@Body Map<String, String> refreshTokenRequest);
    @GET("user/cart")
    Call<ApiResponse<Cart>> cart(
            @Header("Authorization") String token
    );
    @FormUrlEncoded
    @POST("cart-item/update")
    Call<ApiResponse<CartItem>> updateCartItem(
            @Header("Authorization") String token,
            @Field("cart_item_id") String cartItemId,
            @Field("new_quantity") int newQuantity
    );

    @DELETE("cart-item/remove")
    Call<ApiResponse<Cart>> removeCartItem(
            @Query("cart_item_id") String cartItemId,
            @Header("Authorization") String token
    );
    @GET("category/{id}/products")
    Call<ApiResponse<PageData<List<Product>>>> getProductsByCategory(
            @Path("id") String categoryId,
            @Header("Authorization") String token
    );

    @POST("product-review/create")
    Call<ApiResponse<Void>> submitReview(
            @Header("Authorization") String token,
            @Body ProductReview productReview
    );

    @GET("product/{productId}/questions")
    Call<ApiResponse<List<Question>>> getProductQuestions(
            @Path("productId") String productId,
            @Header("Authorization") String token);
    @GET("api/product/{productId}/details")
    Call<Product> getProductDetails(@Path("productId") String productId);
    @GET
    Call<ApiResponse<List<ProductReview>>> getProductReviews(@Url String url, @Header("Authorization") String token);

//    @GET("product/top-rated/{limit}")
//    Call<ApiResponse<List<Product>>> getTopRatedProducts(
//            @Path("limit") int limit,
//            @Header("Authorization") String token
//    );
    @GET("product/top-rated")
    Call<ApiResponse<PageData<List<Product>>>> getTopRatedProducts(
            @Query("page") int page,
            @Query("limit") int limit,
            @Header("Authorization") String token
    );

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

    @PUT("user/update-profile")
    Call<ApiResponse<User>>
    updateProfile(
            @Header("Authorization") String token,
            @Body RequestBody request);

    @PUT("user/change-password")
    Call<ApiResponse<Void>>
    changePassword(
            @Header("Authorization") String token,
            @Body RequestBody request);
}


