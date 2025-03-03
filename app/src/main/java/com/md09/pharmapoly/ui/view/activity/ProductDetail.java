package com.md09.pharmapoly.ui.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.Adapters.ReviewAdapter;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.ApiClient;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private List<Product> productList;
    private TextView productName, productPrice, productRating, productShortDescription, productSpecification,
            productOriginCountry, productManufacturer, productReviewCount, productReviewCount2, productCategory, productBrand;
    private ImageView productImage;
    private RecyclerView userReviewRecyclerView;
    private List<ProductReview> reviewList;
    private String token, productId;
    private ApiService apiService;
    private ProgressBar progress5, progress4, progress3, progress2, progress1;
    private TextView percentage5, percentage4, percentage3, percentage2, percentage1;
    private ReviewAdapter reviewAdapter;
    private SharedPrefHelper sharedPrefHelper;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        progress5 = findViewById(R.id.progress5);
        progress4 = findViewById(R.id.progress4);
        progress3 = findViewById(R.id.progress3);
        progress2 = findViewById(R.id.progress2);
        progress1 = findViewById(R.id.progress1);
        percentage5 = findViewById(R.id.percentage5);
        percentage4 = findViewById(R.id.percentage4);
        percentage3 = findViewById(R.id.percentage3);
        percentage2 = findViewById(R.id.percentage2);
        percentage1 = findViewById(R.id.percentage1);

        // san pham lien quan
        RecyclerView recyclerViewKhac = findViewById(R.id.sanphamlienquan);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewKhac.setLayoutManager(layoutManager);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewKhac.setAdapter(productAdapter);

        ProgressDialogHelper.showLoading(this);
        InitUI();

        sharedPrefHelper = new SharedPrefHelper(this);
        token = "Bearer " + sharedPrefHelper.getToken();
        productId = getIntent().getStringExtra("product_id");

        userReviewRecyclerView = findViewById(R.id.userReview);
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);
        userReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userReviewRecyclerView.setAdapter(reviewAdapter);

        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
        token = sharedPrefHelper.getToken();
        if (token != null) {
            Log.d("Token", token);
        }else{
            Log.d("Token", "Token is null");
        }
        String token = "Bearer " + sharedPrefHelper.getToken();
        fetchProductReviews(productId, token);
        fetchProductDetails(productId, token);
        fetchProductData("67c2bac612ae5bc6e9990212");
        fetchProductData("67c2ba6612ae5bc6e9990209");
    }
    private void updateRatings(int totalReviews, int rating5, int rating4, int rating3, int rating2, int rating1) {
        if (totalReviews == 0) {
            Log.d("ProductDetail", "No reviews to update progress.");
            return;
        }
        Log.d("ProductDetail", "Updating ratings...");
        Log.d("ProductDetail", "Total Reviews: " + totalReviews);
        Log.d("ProductDetail", "Rating 5 stars: " + rating5);
        // Calculate percentages for each rating
        int percentage5Star = (int) ((rating5 * 100.0) / totalReviews);
        int percentage4Star = (int) ((rating4 * 100.0) / totalReviews);
        int percentage3Star = (int) ((rating3 * 100.0) / totalReviews);
        int percentage2Star = (int) ((rating2 * 100.0) / totalReviews);
        int percentage1Star = (int) ((rating1 * 100.0) / totalReviews);
        Log.d("ProductDetail", "Percentage 5 stars: " + percentage5Star + "%");
        Log.d("ProductDetail", "Percentage 4 stars: " + percentage4Star + "%");
        runOnUiThread(() -> {
            progress5.setProgress(percentage5Star);
            progress4.setProgress(percentage4Star);
            progress3.setProgress(percentage3Star);
            progress2.setProgress(percentage2Star);
            progress1.setProgress(percentage1Star);
            percentage5.setText(percentage5Star + "%");
            percentage4.setText(percentage4Star + "%");
            percentage3.setText(percentage3Star + "%");
            percentage2.setText(percentage2Star + "%");
            percentage1.setText(percentage1Star + "%");
        });
    }
    private void FillData(Product product) {
        productName.setText(product.getName());
        productBrand.setText(getString(R.string.brand) + ": " + product.getBrand().getName());
        if (product.getProduct_type() != null) {
            productPrice.setText(product.getPrice() + "/" + product.getProduct_type().getName());
        } else {
            productPrice.setText(product.getPrice() + "/ N/A");
        }
        productRating.setText(String.valueOf(product.getAverage_rating()));
        productReviewCount.setText(product.getReview_count() + " " + getString(R.string.review).toLowerCase());
        productReviewCount2.setText(product.getReview_count() + " " + getString(R.string.reviewr).toLowerCase());
        productCategory.setText(product.getCategory().getName());
        productSpecification.setText(product.getSpecification());
        productOriginCountry.setText(product.getOrigin_country());
        productManufacturer.setText(product.getManufacturer());
        productShortDescription.setText(product.getShort_description());
    }
    private void InitUI() {
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productRating = findViewById(R.id.productRating);
        productShortDescription = findViewById(R.id.productShortDescription);
        productSpecification = findViewById(R.id.productSpecification);
        productBrand = findViewById(R.id.productBrand);
        productOriginCountry = findViewById(R.id.productOriginCountry);
        productManufacturer = findViewById(R.id.productManufacturer);
        productReviewCount = findViewById(R.id.productReviewCount);
        productReviewCount2 = findViewById(R.id.productReviewCount2);
        productCategory = findViewById(R.id.productCategory);
        productImage = findViewById(R.id.productImage);
    }
    private void fetchProductDetails(String productId, String token) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProductDetails(productId, token).enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product productDetails = response.body().getData();
                    FillData(productDetails);

                    if (productDetails.getImages() != null && !productDetails.getImages().isEmpty()) {
                        Picasso.get().load(productDetails.getImages().get(0).getImage_url()).into(productImage);
                    }
                    ProgressDialogHelper.hideLoading();
                } else {
                    Toast.makeText(ProductDetail.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(ProductDetail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchProductReviews(String productId, String token) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String url = "https://pharmapoly-server.onrender.com/api/product/" + productId + "/reviews";
        Call<ApiResponse<List<ProductReview>>> reviewCall = apiService.getProductReviews(url, token);
        reviewCall.enqueue(new Callback<ApiResponse<List<ProductReview>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ProductReview>>> call, Response<ApiResponse<List<ProductReview>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductReview> reviews = response.body().getData();
                    reviewList.clear();
                    if (reviews != null && !reviews.isEmpty()) {
                        reviewList.addAll(reviews);

                        int totalReviews = reviews.size();
                        int rating5 = 0, rating4 = 0, rating3 = 0, rating2 = 0, rating1 = 0;

                        for (ProductReview review : reviews) {
                            float rating = review.getRating();
                            if (rating == 5) rating5++;
                            else if (rating == 4) rating4++;
                            else if (rating == 3) rating3++;
                            else if (rating == 2) rating2++;
                            else if (rating == 1) rating1++;
                        }
                        updateRatings(totalReviews, rating5, rating4, rating3, rating2, rating1);
                        if (userReviewRecyclerView.getAdapter() == null) {
                            reviewAdapter = new ReviewAdapter(ProductDetail.this, reviewList);
                            userReviewRecyclerView.setAdapter(reviewAdapter);
                        } else {
                            userReviewRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    } else {
                        Log.d("APIResponse", "No reviews found.");
                    }
                } else {
                    Toast.makeText(ProductDetail.this, "Failed to fetch reviews", Toast.LENGTH_SHORT).show();
                    Log.e("APIError", "Failed to fetch reviews: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<ProductReview>>> call, Throwable t) {
                Toast.makeText(ProductDetail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("APIError", "Error fetching reviews: " + t.getMessage());
            }
        });
    }
    private void fetchProductData(String productId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Product>> productCall = apiService.getProduct(productId, "Bearer " + token);
        productCall.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                // Log mã trạng thái HTTP và thông điệp phản hồi
                Log.d("APIResponse", "Response code: " + response.code());
                Log.d("APIResponse", "Response message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Product> apiResponse = response.body();
                    Product product = apiResponse.getData();
                    Log.d("APIResponse", "Product fetched: " + product.toString());
                    String imageUrl = product.getImageUrl();
                    if (imageUrl != null) {
                        Log.d("APIResponse", "Image URL: " + imageUrl);
                    }
                    productList.add(product);
                    productAdapter.notifyDataSetChanged();
                    Toast.makeText(ProductDetail.this, "Lấy dữ liệu thành công", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("APIError", "Failed to fetch product: " + response.message());
                    Toast.makeText(ProductDetail.this, "Failed to fetch product", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Log.e("APIError", "Error fetching product: " + t.getMessage());
                Toast.makeText(ProductDetail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}