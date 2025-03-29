package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.PRODUCT_ADDED_TO_CART_KEY;
import static com.md09.pharmapoly.utils.Constants.formatCurrency;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.Adapters.QuestionAdapter;
import com.md09.pharmapoly.Adapters.ReviewAdapter;
import com.md09.pharmapoly.Models.CartItem;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.Models.ProductReview;
import com.md09.pharmapoly.Models.Question;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.ApiClient;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.SuccessMessageBottomSheet;
import com.md09.pharmapoly.utils.ProgressDialogHelper;
import com.md09.pharmapoly.utils.PurchaseBottomSheet;
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
    private TextView productName, productPrice, productRating, productShortDescription, productSpecification, productOriginCountry, productManufacturer, productReviewCount, productReviewCount2, productCategory, productBrand;
    private ImageView productImage;
    private RecyclerView userReviewRecyclerView;
    private List<ProductReview> reviewList;
    private String token, productId;
    private RetrofitClient retrofitClient;
    private ProgressBar progress5, progress4, progress3, progress2, progress1;
    private TextView percentage5, percentage4, percentage3, percentage2, percentage1;
    private ReviewAdapter reviewAdapter;
    private SharedPrefHelper sharedPrefHelper;
    private Product product;
    private Button showProductDetailsButton;
    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;
    private Button showMoreReviewsButton;
    private LinearLayout btn_purchase;
    private ImageView backBtn;
    private boolean isProductAdded = false;
    private PurchaseBottomSheet purchaseBottomSheet;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        btn_purchase = findViewById(R.id.btn_purchase);
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
        backBtn = findViewById(R.id.backBtn);

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
        fetchProductData("67d3df615a228bc7cc5bb8f5");
        fetchProductData("67d3ec3a5a228bc7cc5bbbab");
        fetchProductData("67d3ef595a228bc7cc5bbc9a");
        fetchProductData("67d3f1df5a228bc7cc5bbd20");

        // nút xem thêm toàn bộ thông tin trong product details
        TextView showProductDetailsButton = findViewById(R.id.showProductDetails);
        showProductDetailsButton.setOnClickListener(v -> {
            if (product != null) {
                Intent intent = new Intent(ProductDetail.this, ProductDetailAllActivity.class);
                // Gửi thêm thông tin qua Intent
                intent.putExtra("product", product);
                intent.putExtra("brand_description", product.getBrand().getDescription());
                intent.putExtra("product_type_name", product.getProduct_type().getName());
                intent.putExtra("sections", new ArrayList<>(product.getSections()));
                startActivity(intent);
            } else {
                Log.d("ProductDetailActivity", "Product is null");
            }
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });

// Khởi tạo RecyclerView và Adapter
        questionRecyclerView = findViewById(R.id.questionRecyclerView);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionList = new ArrayList<>();  // Khởi tạo lại danh sách câu hỏi
        questionAdapter = new QuestionAdapter(this, questionList);
        questionRecyclerView.setAdapter(questionAdapter);

// Fetch dữ liệu câu hỏi từ API
        String productId = getIntent().getStringExtra("product_id");
        fetchProductQuestions(productId, token, questionAdapter);

        Button ratingButton = findViewById(R.id.ratingButton); // giả sử bạn có một nút đánh giá
        ratingButton.setOnClickListener(v -> {
            showRatingDialog(); // Hiển thị dialog đánh giá khi người dùng nhấn nút
        });

        Button showProductDetailsButton1 = findViewById(R.id.btnShowMoreQuestions);  // Tìm nút trong layout

        showProductDetailsButton1.setOnClickListener(v -> {
            if (product != null) {
                // Mở QuestionDetailActivity khi người dùng nhấn "Xem thêm"
                Intent intent = new Intent(ProductDetail.this, QuestionDetailActivity.class);

                // Truyền danh sách câu hỏi qua Intent
                intent.putExtra("product_id", product.get_id());
                intent.putExtra("product_name", product.getName());
                intent.putExtra("questions", new ArrayList<>(questionList));  // Truyền câu hỏi

                startActivity(intent);  // Mở Activity mới
            } else {
                Log.d("ProductDetailActivity", "Product is null");
            }
        });
//
        // Khởi tạo nút "Xem thêm đánh giá"
        showMoreReviewsButton = findViewById(R.id.showMoreReviewsButton);

        // Khi nhấn nút "Xem thêm đánh giá"
        showMoreReviewsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetail.this, ProductReviewsActivity.class);
            List<ProductReview> reviewList = new ArrayList<>();
            startActivity(intent);
        });


        btn_purchase.setOnClickListener(v -> {

            purchaseBottomSheet = new PurchaseBottomSheet(
                    product,
                    new PurchaseBottomSheet.AddToCartListener() {
                        @Override
                        public void onAddToCart(CartItem cartItem) {
                            AddProductToCart(cartItem);
                        }
                    });
            purchaseBottomSheet.show(getSupportFragmentManager(), "QuantitySelectionDialog");
        });
    }

    private void AddProductToCart(CartItem cartItem) {
        ProgressDialogHelper.showLoading(this);
        retrofitClient.callAPI().addProductToCart(
                cartItem,
                "Bearer " + new SharedPrefHelper(this).getToken())
                .enqueue(new Callback<ApiResponse<CartItem>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                        purchaseBottomSheet.dismiss();
                        ProgressDialogHelper.hideLoading();
                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                            new SharedPrefHelper(ProductDetail.this).setBooleanState(PRODUCT_ADDED_TO_CART_KEY,true);
                            isProductAdded = true;
                            SuccessMessageBottomSheet bottomSheet = SuccessMessageBottomSheet.newInstance(getString(R.string.cart_add_success));
                            bottomSheet.show(getSupportFragmentManager(), "SuccessMessageBottomSheet");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {
                        ProgressDialogHelper.hideLoading();
                        purchaseBottomSheet.dismiss();
                    }
                });
    }
    @Override
    public void finish() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("isProductAdded", isProductAdded);
        setResult(RESULT_OK, resultIntent);
        super.finish();
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
    // filldata
    private void FillData(Product product) {
        productName.setText(product.getName());
        productBrand.setText(getString(R.string.brand) + ": " + product.getBrand().getName());
//        if (product.getProduct_type() != null) {
//            productPrice.setText(formatCurrency(product.getPrice(), "đ") + "/" + product.getProduct_type().getName());
//        } else {
//            productPrice.setText(product.getPrice() + "/ N/A");
//        }
        String type = "";
        if (product.getProduct_type() != null) {
            type = "/" + product.getProduct_type().getName();
        }
        productPrice.setText(
                formatCurrency(product.getPrice(), "đ") + type);

        productRating.setText(String.valueOf(product.getAverage_rating()));
        productReviewCount.setText(product.getReview_count() + " " + getString(R.string.review).toLowerCase());
        productReviewCount2.setText(product.getReview_count() + " " + getString(R.string.reviewr).toLowerCase());
        productCategory.setText(product.getCategory().getName());
        productSpecification.setText(product.getProduct_type().getName() + " " + product.getSpecification());
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

        retrofitClient = new RetrofitClient();
    }

    // lấy product details
    private void fetchProductDetails(String productId, String token) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProductDetails(productId, token).enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                ProgressDialogHelper.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Product productDetails = response.body().getData();
                    FillData(productDetails);

                    if (productDetails.getImages() != null && !productDetails.getImages().isEmpty() && productDetails != null) {
                        product = productDetails;  // Gán dữ liệu vào biến product
                        FillData(productDetails);
                        Log.d("ProductDetailActivity", "Product fetched: " + productDetails);
                        Picasso.get().load(productDetails.getImages().get(0).getImage_url()).into(productImage);
                    }
                } else {
                    Toast.makeText(ProductDetail.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
                    Log.d("ProductDetailActivity", "Product details are null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                ProgressDialogHelper.hideLoading();
                Toast.makeText(ProductDetail.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // fetch review sản phẩm từ người dùng
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

    // hiển thị các sản phẩm khác
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

    // hiển thị câu hỏi lên
    // Phương thức fetchProductQuestions
    private void fetchProductQuestions(String productId, String token, QuestionAdapter questionAdapter) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProductQuestions(productId, token).enqueue(new Callback<ApiResponse<List<Question>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Question>>> call, Response<ApiResponse<List<Question>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> questions = response.body().getData();
                    if (questions != null && !questions.isEmpty()) {
                        // Kiểm tra và log số câu hỏi nhận được
                        Log.d("ProductDetail", "Questions received: " + questions.size());

                        // Xóa dữ liệu cũ và thêm 5 câu hỏi đầu tiên
                        questionList.clear();
                        questionList.addAll(questions.subList(0, Math.min(5, questions.size())));

                        // Cập nhật lại Adapter
                        questionAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("ProductDetail", "No questions available.");
                    }
                } else {
                    Log.e("ProductDetail", "Failed to fetch product questions");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Question>>> call, Throwable t) {
                Log.e("ProductDetail", "Error fetching questions: " + t.getMessage());
            }
        });
    }
    private void showRatingDialog() {
        // Inflate custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_rating, null);

        // Setup star images
        ImageView star1 = dialogView.findViewById(R.id.star1);
        ImageView star2 = dialogView.findViewById(R.id.star2);
        ImageView star3 = dialogView.findViewById(R.id.star3);
        ImageView star4 = dialogView.findViewById(R.id.star4);
        ImageView star5 = dialogView.findViewById(R.id.star5);

        // Setup EditText for review
        EditText reviewText = dialogView.findViewById(R.id.reviewText);

        // Setup the initial rating to 0
        final int[] rating = {0};

        // Set click listeners for the stars
        star1.setOnClickListener(v -> {
            rating[0] = 1;
            updateStars(star1, star2, star3, star4, star5, rating[0]);
        });
        star2.setOnClickListener(v -> {
            rating[0] = 2;
            updateStars(star1, star2, star3, star4, star5, rating[0]);
        });
        star3.setOnClickListener(v -> {
            rating[0] = 3;
            updateStars(star1, star2, star3, star4, star5, rating[0]);
        });
        star4.setOnClickListener(v -> {
            rating[0] = 4;
            updateStars(star1, star2, star3, star4, star5, rating[0]);
        });
        star5.setOnClickListener(v -> {
            rating[0] = 5;
            updateStars(star1, star2, star3, star4, star5, rating[0]);
        });

        // Build the dialog with custom buttons
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(true); // Dialog có thể bị hủy khi bấm ngoài

        // Tạo đối tượng dialog từ builder
        AlertDialog dialog = builder.create();

        // Hiển thị dialog ở cuối màn hình
        dialog.getWindow().setGravity(Gravity.BOTTOM);  // Đặt vị trí ở cuối
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Handle click for 'Gửi' button
        Button sendButton = dialogView.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            String review = reviewText.getText().toString().trim();
            if (rating[0] == 0) {
                Toast.makeText(this, "Vui lòng chọn sao", Toast.LENGTH_SHORT).show();
                return;
            }
            if (review.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }
            submitReview(rating[0], review); // Gửi dữ liệu đánh giá lên server
            dialog.dismiss(); // Đóng dialog sau khi gửi
        });

        // Handle click for 'Hủy' button
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss(); // Đóng dialog khi bấm Hủy
        });

        // Hiển thị dialog
        dialog.show();
    }



    // Update the stars based on rating
    private void updateStars(ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5, int rating) {
        // Reset all stars to empty
        star1.setImageResource(R.drawable.ic_star_empty);
        star2.setImageResource(R.drawable.ic_star_empty);
        star3.setImageResource(R.drawable.ic_star_empty);
        star4.setImageResource(R.drawable.ic_star_empty);
        star5.setImageResource(R.drawable.ic_star_empty);

        // Set stars to filled based on rating
        if (rating >= 1) star1.setImageResource(R.drawable.ic_star);
        if (rating >= 2) star2.setImageResource(R.drawable.ic_star);
        if (rating >= 3) star3.setImageResource(R.drawable.ic_star);
        if (rating >= 4) star4.setImageResource(R.drawable.ic_star);
        if (rating >= 5) star5.setImageResource(R.drawable.ic_star);
    }

    private void submitReview(int rating, String review) {
        String userId = new SharedPrefHelper(this).getUser().get_id(); // Lấy ID người dùng từ Shared Preferences
        String productId = getIntent().getStringExtra("product_id");
        String token = "Bearer " + new SharedPrefHelper(this).getToken(); // Lấy token từ SharedPreferences
        ProductReview productReview = new ProductReview();
        productReview.setUser_id(userId);
        productReview.setProduct_id(productId);
        productReview.setRating(rating);
        productReview.setReview(review);

        Log.d("Review", "User ID: " + userId);
        Log.d("Review", "Product ID: " + productId);
        Log.d("Review", "Rating: " + rating);
        Log.d("Review", "Review: " + review);
        Log.d("Review", "Token: " + token);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.submitReview(token, productReview).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProductDetail.this, "Đánh giá đã được gửi thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetail.this, "Gửi đánh giá thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("APIError", "Failed to submit review: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(ProductDetail.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("APIError", "Error submitting review: " + t.getMessage());
            }
        });
    }
}