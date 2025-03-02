package com.md09.pharmapoly.ui.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.ReviewAdapter;
import com.md09.pharmapoly.Models.Brand;
import com.md09.pharmapoly.Models.Category;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.Models.Section;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.ApiClient;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail extends AppCompatActivity {
    private TextView
            productName,
            productPrice,
            productRating,
            productShortDescription,
            productSpecification,
            productOriginCountry,
            productManufacturer,
            productReviewCount,
            productReviewCount2,
            productCategory,
            productBrand;
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
    private Category category;
    private RetrofitClient retrofitClient;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        ProgressDialogHelper.showLoading(this);
        InitUI();

        RecyclerView recyclerView = findViewById(R.id.userReview);

        productId = getIntent().getStringExtra("product_id");

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
    private void FillData(Product product) {
        productName.setText(product.getName());
        productBrand.setText(getString(R.string.brand) + ": " + product.getBrand().getName());
        productPrice.setText(product.getPrice() + "/" + product.getProduct_type().getName());
        productRating.setText(String.valueOf(product.getAverage_rating()));

        productReviewCount.setText(product.getReview_count() + " " + getString(R.string.review).toLowerCase());
        productReviewCount2.setText(product.getReview_count()+ " " + getString(R.string.reviewr).toLowerCase());

        productCategory.setText(product.getCategory().getName());
        productSpecification.setText(product.getSpecification());
        productOriginCountry.setText(product.getOrigin_country());
        productManufacturer.setText(product.getManufacturer());
        productShortDescription.setText(product.getShort_description());
    }
    private void InitUI() {
        retrofitClient = new RetrofitClient();
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
        retrofitClient.callAPI().getProductDetails(productId, token).enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product productDetails = response.body().getData();

                    FillData(productDetails);


                    if (productDetails.getImages() != null && !productDetails.getImages().isEmpty()) {
                        Picasso.get().load(productDetails.getImages().get(0).getImage_url()).into(productImage);  // Hiển thị ảnh chính
                    }
                    ProgressDialogHelper.hideLoading();
                } else {
                    Toast.makeText(ProductDetail.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                // Xử lý lỗi API
                Toast.makeText(ProductDetail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchProductReviews(String productId, String token) {
        // Gọi API để lấy đánh giá của sản phẩm
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String url = "https://pharmapoly-server.onrender.com/api/product/" + productId + "/reviews";  // URL API với Product ID

        // Gọi API để lấy đánh giá sản phẩm
        Call<ApiResponse<List<ProductReview>>> reviewCall = apiService.getProductReviews(url, token);  // Đảm bảo bạn sử dụng đúng kiểu dữ liệu
        reviewCall.enqueue(new Callback<ApiResponse<List<ProductReview>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ProductReview>>> call, Response<ApiResponse<List<ProductReview>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductReview> reviews = response.body().getData();  // Lấy danh sách đánh giá
                    Log.d("APIResponse", "Fetched reviews: " + reviews.size());  // In số lượng đánh giá

                    // Hiển thị danh sách đánh giá
                    reviewList.clear();
                    if (reviews != null && !reviews.isEmpty()) {
                        reviewList.addAll(reviews);
                        // Kiểm tra nếu adapter đã được gắn vào RecyclerView, nếu chưa thì gắn mới
                        if (userReviewRecyclerView.getAdapter() == null) {
                            ReviewAdapter reviewAdapter = new ReviewAdapter(ProductDetail.this, reviewList);
                            userReviewRecyclerView.setAdapter(reviewAdapter);  // Gắn Adapter vào RecyclerView
                        } else {
                            userReviewRecyclerView.getAdapter().notifyDataSetChanged();  // Cập nhật RecyclerView với dữ liệu mới
                        }

                        // Log thông tin của từng review để kiểm tra
                        for (ProductReview review : reviews) {
                            Log.d("APIResponse", "Review ID: " + review.get_id());
                            Log.d("APIResponse", "Review Rating: " + review.getRating());
                            Log.d("APIResponse", "Review Content: " + review.getReview());
                            Log.d("APIResponse", "Date: " + review.getCreated_at());
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

    private void displaySections(List<Section> sections) {
        // Hiển thị các phần chi tiết (ví dụ: tác dụng, hướng dẫn sử dụng, thành phần,...)
        // Thêm logic xử lý để hiển thị các sections vào RecyclerView hoặc TextView
    }
}

