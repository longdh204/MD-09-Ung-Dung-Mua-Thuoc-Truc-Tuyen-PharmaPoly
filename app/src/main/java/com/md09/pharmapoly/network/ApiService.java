package com.md09.pharmapoly.network;

import com.google.gson.JsonObject;
import com.md09.pharmapoly.Models.Bank;
import com.md09.pharmapoly.Models.BankResponse;
import com.md09.pharmapoly.Models.Brand;
import com.md09.pharmapoly.Models.Cart;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.Models.ChatRequest;
import com.md09.pharmapoly.Models.ChatResponse;
import com.md09.pharmapoly.Models.District;
import com.md09.pharmapoly.Models.DistrictRequest;
import com.md09.pharmapoly.Models.GHNResponse;
import com.md09.pharmapoly.Models.Notification;
import com.md09.pharmapoly.Models.Order;
import com.md09.pharmapoly.Models.PageData;
import com.md09.pharmapoly.Models.PageDataClone;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.Models.Province;
import com.md09.pharmapoly.Models.Question;
import com.md09.pharmapoly.Models.SearchResponse;
import com.md09.pharmapoly.Models.UserAddress;
import com.md09.pharmapoly.Models.Ward;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.data.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @GET("provinces")
    Call<GHNResponse<ArrayList<Province>>> getListProvince(@Header("Authorization") String token);

    @POST("districts")
    Call<GHNResponse<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest,@Header("Authorization") String token);

    @GET("wards")
    Call<GHNResponse<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id,@Header("Authorization") String token);
    @GET("v2/banks")
    Call<BankResponse> getBanks();
    @DELETE("orders/delete-payment-status/{userId}")
    Call<Void> deletePaymentStatus(@Header("Authorization") String token);
    @GET("product/{productId}/images")
        // Đảm bảo URL là đúng với API của bạn
    Call<ApiResponse<List<String>>> getProductImages(
            @Path("productId") String productId,
            @Header("Authorization") String token);

    @GET("product/most-reviewed")
    Call<ApiResponse<PageDataClone<List<Product>>>> getMostReviewProducts(
            @Query("page") int page,
            @Query("limit") int limit,
            @Header("Authorization") String token
    );
    @POST("orders/{id}/cancel")
    Call<ApiResponse<Order>> cancelOrder(
            @Path("id") String orderId,
            @Header("Authorization") String token
    );
    @POST("orders/{id}/return")
    Call<ApiResponse<Order>> returnOrder(
            @Path("id") String orderId,
            @Header("Authorization") String token
    );
    @POST("orders/create")
    Call<ApiResponse<Map<String, Object>>> createOrders(
            @Body Map<String, Object> data,
            @Header("Authorization") String token
    );
    @GET("orders/payment_status/:order_id")
    Call<ApiResponse<Void>> getPaymentStatusStatus(
            @Path("order_id") String order_id,
            @Header("Authorization") String token
    );
    @GET("orders/{id}/detail")
    Call<ApiResponse<Order>> getOrderDetail(
            @Path("id") String orderId,
            @Header("Authorization") String token
    );
    @GET("orders")
    Call<ApiResponse<List<Order>>> getOrders(
            @Query("group") String group,
            @Header("Authorization") String token
    );
    @POST("calculate-shipping-fee")
    Call<GHNResponse<Object>> calculateShippingFee(
            @Header("Authorization") String token,
            @Body Map<String, String> params
    );

//    @GET("search")
//    Call<ApiResponse<SearchResponse>> searchProducts(
//            @Query("keyword") String keyword,
//            @Query("page") int page,
//            @Query("limit") int limit,
//            @Header("Authorization") String token
//    );
    @GET("search")
    Call<ApiResponse<SearchResponse>> searchProducts(
        @Query("keyword") String keyword,
        @Query("page") int page,
        @Query("limit") int limit,
        @Header("Authorization") String token
    );
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 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"
    })
    @POST("v1/chat/completions")
    Call<ChatResponse> getChatResponse(@Body ChatRequest request);

    @POST("refresh-token")
    Call<ApiResponse<User>> refreshToken(@Body Map<String, String> refreshTokenRequest);
    @PUT("user/address/update")
    Call<ApiResponse<UserAddress>> updateAddress(
            @Body UserAddress userAddress,
            @Header("Authorization") String token
    );
    @GET("user/cart")
    Call<ApiResponse<Cart>> cart(
            @Header("Authorization") String token
    );
    @GET("user/notification")
    Call<ApiResponse<List<Notification>>> getNotification(
            @Header("Authorization") String token
    );
    @GET("user/notification/read-all")
    Call<ApiResponse<List<Notification>>> notificationReadAll(
            @Header("Authorization") String token
    );
    @GET("user/notification/unread-count")
    Call<ApiResponse<Integer>> notificationUnreadCount(
            @Header("Authorization") String token
    );
    @PUT("user/notification/read/{id}")
    Call<ApiResponse<Notification>> readNotification(
            @Path("id") String id,
            @Header("Authorization") String token
    );
    @POST("cart-item/add")
    Call<ApiResponse<CartItem>> addProductToCart(
            @Body CartItem cartItem,
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
            @Header("Authorization") String token, // Thêm header token
            @Body ProductReview productReview
    );

    @GET("product/{productId}/questions")
    Call<ApiResponse<List<Question>>> getProductQuestions(
            @Path("productId") String productId,
            @Header("Authorization") String token);
    @GET("api/product/{productId}/details")
    Call<Product> getProductDetails(@Path("productId") String productId);
    @GET("api/product/{productId}/reviews")
    Call<ApiResponse<List<ProductReview>>> getProductReviews(
            @Path("productId") String productId,
            @Header("Authorization") String token);

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

    @GET("product/related")
    Call<ApiResponse<List<Product>>> getRelatedProducts(
            @Query("product_id") String productId,
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

    @Multipart
    @PUT("user/update-profile")
    Call<ApiResponse<User>> updateProfile(
            @Header("Authorization") String token,
            @Part MultipartBody.Part avatar,
            @Part("full_name") RequestBody fullName,
            @Part("shipping_phone_number") RequestBody phoneNumber,
            @Part("gender") RequestBody gender,
            @Part("date_of_birth") RequestBody dateOfBirth
    );


    @PUT("user/change-password")
    Call<ApiResponse<Void>>
    changePassword(
            @Header("Authorization") String token,
            @Body RequestBody request);
}


