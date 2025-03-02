package com.md09.pharmapoly.ui.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.ReviewAdapter;
import com.md09.pharmapoly.Models.Brand;
import com.md09.pharmapoly.Models.ProductCategory;
import com.md09.pharmapoly.Models.ProductDetails;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.Models.Section;
import com.md09.pharmapoly.Models.Sections;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.ApiClient;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product_Detail extends AppCompatActivity {
    private TextView productName, productPrice, productRating, productDescription, productSpecification, productOrigin, productManufacturer, productReviewCount, productCategory,productBrand;
    private ImageView productImage;
    //
    private ReviewAdapter reviewAdapter;

    private  ProductReview productReview;
    private RecyclerView userReviewRecyclerView;
    private List<ProductReview> reviewList;
    private String token;
    private String productId;
    private ApiService apiService;

    private Brand brand;
    private ProductCategory category;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        RecyclerView recyclerView = findViewById(R.id.userReview);
        // Ánh xạ các views
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productRating = findViewById(R.id.productRating);

        productDescription = findViewById(R.id.productDescription);
        productSpecification = findViewById(R.id.productSpecification);
        productBrand = findViewById(R.id.productBrand);
        productOrigin = findViewById(R.id.productOrigin);

        productManufacturer = findViewById(R.id.productManufacturer);

        productReviewCount = findViewById(R.id.productReviewCount);

        productCategory = findViewById(R.id.productCategory);
        productImage = findViewById(R.id.productImage);

        // Nhận dữ liệu từ Intent
        productId = getIntent().getStringExtra("product_id");
        String name = getIntent().getStringExtra("product_name");
        String price = getIntent().getStringExtra("product_price");
        String rating = getIntent().getStringExtra("product_rating");
        String imageUrl = getIntent().getStringExtra("product_image_url");
        // Hiển thị dữ liệu cơ bản lên UI
        productName.setText(name);
        productPrice.setText("" + price);
        productRating.setText("" + rating);
        // Tải và hiển thị ảnh sản phẩm bằng Picasso
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(productImage);  // Tải ảnh từ URL và hiển thị
        }
        // Lấy token từ SharedPreferences
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
        token = sharedPrefHelper.getToken();
        if (token != null) {
            Log.d("Token", token);
        }else{
            Log.d("Token", "Token is null");
        }
        String token = "Bearer " + sharedPrefHelper.getToken();
        // Ánh xạ RecyclerView
        // Khởi tạo RecyclerView
        userReviewRecyclerView = findViewById(R.id.userReview);
        reviewList = new ArrayList<>();

        // Khởi tạo Adapter và gán ngay lập tức (dù danh sách đang rỗng)
        reviewAdapter = new ReviewAdapter(this, reviewList);
        userReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userReviewRecyclerView.setAdapter(reviewAdapter);

        // Lấy dữ liệu từ Intent
        productId = getIntent().getStringExtra("product_id");
        token = "Bearer " + new SharedPrefHelper(this).getToken();

        // Gọi API lấy đánh giá
        fetchProductReviews(productId, token);
        // Gọi API để lấy chi tiết sản phẩm
        fetchProductDetails(productId, token);
        // Gọi API để lấy đánh giá của sản phẩm
    }
    private void fetchProductDetails(String productId, String token) {
        // Gọi API để lấy chi tiết sản phẩm
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String url = "https://pharmapoly-server.onrender.com/api/product/" + productId + "/details";  // API chi tiết sản phẩm

        // Gọi API để lấy chi tiết sản phẩm
        Call<ApiResponse<ProductDetails>> productDetailsCall = apiService.getProductDetails(url, token);
        productDetailsCall.enqueue(new Callback<ApiResponse<ProductDetails>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProductDetails>> call, Response<ApiResponse<ProductDetails>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductDetails productDetails = response.body().getData();

                    // Hiển thị thông tin chi tiết
                    productDescription.setText(productDetails.getShort_description());  // Mô tả
                    productSpecification.setText(productDetails.getSpecification());  // Thông số kỹ thuật
                    productOrigin.setText("" + productDetails.getOrigin_country());  // Nguồn gốc
                    productBrand.setText("" + productDetails.getManufacturer());  // Nhà sản xuất
                    productManufacturer.setText("" + productDetails.getBrand());  // Hãng
                    productReviewCount.setText("" + productDetails.getReview_count());  // Số lượng đánh giá
                    productCategory.setText("" + productDetails.getCategory().getName());  // Danh mục sản phẩm

                    fetchBrandDetails(productDetails.getBrand_id(), token);

                    fetchCategoryDetails(productDetails.getCategory_id(), token);
                    // Hiển thị hình ảnh chính (nếu có)
                    if (productDetails.getImages() != null && !productDetails.getImages().isEmpty()) {
                        Picasso.get().load(productDetails.getImages().get(0).getImage_url()).into(productImage);  // Hiển thị ảnh chính
                    }
                } else {
                    Toast.makeText(Product_Detail.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductDetails>> call, Throwable t) {
                // Xử lý lỗi API
                Toast.makeText(Product_Detail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchProductReviews(String productId, String token) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String url = "https://pharmapoly-server.onrender.com/api/product-review/get/" + productId;

        Log.d("APIRequest", "Fetching reviews from: " + url);

        Call<ApiResponse<List<ProductReview>>> reviewCall = apiService.getProductReviews(url, token);

        reviewCall.enqueue(new Callback<ApiResponse<List<ProductReview>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ProductReview>>> call, Response<ApiResponse<List<ProductReview>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductReview> reviews = response.body().getData();

                    if (reviews != null && !reviews.isEmpty()) {
                        Log.d("APIResponse", "Fetched " + reviews.size() + " reviews.");

                        reviewList.clear();
                        reviewList.addAll(reviews);

                        // Cập nhật Adapter ngay sau khi có dữ liệu
                        runOnUiThread(() -> reviewAdapter.notifyDataSetChanged());
                    } else {
                        Log.d("APIResponse", "No reviews found.");
                    }
                } else {
                    Log.e("APIError", "Failed to fetch reviews: " + response.message());
                    Log.e("APIError", "Response Code: " + response.code()); // In lỗi cụ thể
                    Log.e("APIError", "Response Body: " + response.errorBody()); // In chi tiết lỗi
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ProductReview>>> call, Throwable t) {
                Log.e("APIError", "Error fetching reviews: " + t.getMessage());
            }
        });
    }

    private void fetchBrandDetails(String brandId, String token) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String url = "https://pharmapoly-server.onrender.com/api/brand/" + brandId;

        Call<ApiResponse<Brand>> brandCall = apiService.getBrandDetails(url, token);
        brandCall.enqueue(new Callback<ApiResponse<Brand>>() {
            @Override
            public void onResponse(Call<ApiResponse<Brand>> call, Response<ApiResponse<Brand>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Brand brand = response.body().getData();
                    // Hiển thị thông tin Brand
                    productManufacturer.setText("" + brand.getName() + " - " + brand.getDescription());
                } else {
                    Toast.makeText(Product_Detail.this, "Failed to fetch brand details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Brand>> call, Throwable t) {
                // Xử lý lỗi API
                Toast.makeText(Product_Detail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCategoryDetails(String categoryId, String token) {
        // Gọi API để lấy thông tin Category
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String url = "https://pharmapoly-server.onrender.com/api/category/" + categoryId;

        Call<ApiResponse<ProductCategory>> categoryCall = apiService.getCategoryDetails(url, token);
        categoryCall.enqueue(new Callback<ApiResponse<ProductCategory>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProductCategory>> call, Response<ApiResponse<ProductCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductCategory category = response.body().getData();
                    productCategory.setText("Category: " + category.getName());
                } else {
                    Toast.makeText(Product_Detail.this, "Failed to fetch category details", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<ProductCategory>> call, Throwable t) {
                Toast.makeText(Product_Detail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displaySections(List<Section> sections) {
        // Hiển thị các phần chi tiết (ví dụ: tác dụng, hướng dẫn sử dụng, thành phần,...)
        // Thêm logic xử lý để hiển thị các sections vào RecyclerView hoặc TextView
    }
}

