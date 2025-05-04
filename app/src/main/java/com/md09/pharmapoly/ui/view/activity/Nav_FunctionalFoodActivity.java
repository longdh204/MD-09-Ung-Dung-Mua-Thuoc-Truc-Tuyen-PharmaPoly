package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.Models.PageData;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.ui.view.activity.cancer.PreventionActivity;
import com.md09.pharmapoly.ui.view.fragment.HomeFragment;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nav_FunctionalFoodActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private final List<Product> allProducts = new ArrayList<>();
    private int loadedCategoryCount = 0;
    private ImageView btn_back, search, cart;

    private final List<String> categoryIds = Arrays.asList(
            "67d3dd545a228bc7cc5bb8eb", // vitamin & khoáng chất
            "67d3ff31b0aaf7c883554c55", // sinh lý & nội tiết tố
            "67d3ff3fb0aaf7c883554c57", // Cải thiện tăng cường chức năng
            "67d3ff46b0aaf7c883554c59", // Hỗ trợ điều trị
            "67d3ff4cb0aaf7c883554c5b", // Hỗ trợ tiêu hóa
            "67d3ff56b0aaf7c883554c5d"  // Thần kinh não
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_functional_food);

        btn_back = findViewById(R.id.btn_back);
        cart = findViewById(R.id.cart);
        search = findViewById(R.id.search);
        search.setOnClickListener(v -> {
            // Mở màn hình tìm kiếm
            startActivity(new Intent(Nav_FunctionalFoodActivity.this, SearchActivity.class));
        });
        cart.setOnClickListener(v -> {
            Intent intent = new Intent(Nav_FunctionalFoodActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("open_cart", true);
            startActivity(intent);
            finish();
        });
        btn_back.setOnClickListener(v -> {
            finish();
        });

        // Thêm code chuyển màn hình cho các button
        findViewById(R.id.nav_duocmypham).setOnClickListener(v -> {
            startActivity(new Intent(Nav_FunctionalFoodActivity.this, Nav_Pharmaceutical_Cosmetics.class));
            finish();
        });

        findViewById(R.id.nav_thuoc).setOnClickListener(v -> {
            startActivity(new Intent(Nav_FunctionalFoodActivity.this, Nav_Medicine.class));
            finish();
        });

        findViewById(R.id.nav_chamsoccanhan).setOnClickListener(v -> {
            startActivity(new Intent(Nav_FunctionalFoodActivity.this, Nav_Personalcare.class));
            finish();
        });

        findViewById(R.id.nav_thietbiyte).setOnClickListener(v -> {
            startActivity(new Intent(Nav_FunctionalFoodActivity.this, Nav_Medical_Equiment.class));
            finish();
        });

        recyclerView = findViewById(R.id.thucphamchucnang);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(productAdapter);
        loadAllProducts();

        // Ánh xạ các button từ layout
        findViewById(R.id.btnvitamin).setOnClickListener(v -> filterProductsByCategory("67d3dd545a228bc7cc5bb8eb"));
        findViewById(R.id.btnsinhlynoitiet).setOnClickListener(v -> filterProductsByCategory("67d3ff31b0aaf7c883554c55"));
        findViewById(R.id.btncaithientangcuong).setOnClickListener(v -> filterProductsByCategory("67d3ff3fb0aaf7c883554c57"));
        findViewById(R.id.btnhotrodieutri).setOnClickListener(v -> filterProductsByCategory("67d3ff46b0aaf7c883554c59"));
        findViewById(R.id.btnhotrotieuhoa).setOnClickListener(v -> filterProductsByCategory("67d3ff4cb0aaf7c883554c5b"));
        findViewById(R.id.btnthankinhnao).setOnClickListener(v -> filterProductsByCategory("67d3ff56b0aaf7c883554c5d"));
    }

    private void loadAllProducts() {
        String token = "Bearer " + new SharedPrefHelper(this).getToken();
        Log.d("LOAD_ALL_PRODUCTS", "Gọi API lấy tất cả sản phẩm");

        allProducts.clear(); // Xóa danh sách cũ trước khi load mới
        final int[] loadedCategories = {0};

        for (String categoryId : categoryIds) {
            RetrofitClient.getRetrofitInstance().create(ApiService.class)
                    .getProductsByCategory(categoryId, token)
                    .enqueue(new Callback<ApiResponse<PageData<List<Product>>>>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse<PageData<List<Product>>>> call,
                                               @NonNull Response<ApiResponse<PageData<List<Product>>>> response) {
                            loadedCategories[0]++;
                            if (response.isSuccessful() && response.body() != null) {
                                PageData<List<Product>> pageData = response.body().getData();
                                if (pageData != null && pageData.getData() != null) {
                                    allProducts.addAll(pageData.getData());
                                    Log.d("LOAD_SUCCESS", "Loaded " + pageData.getData().size() + " sản phẩm.");
                                }
                            }

                            // Khi tất cả category đã load xong, cập nhật RecyclerView
                            if (loadedCategories[0] == categoryIds.size()) {
                                productAdapter.Update(allProducts);
                                Log.d("LOAD_COMPLETE", "Hiển thị tổng cộng: " + allProducts.size() + " sản phẩm");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResponse<PageData<List<Product>>>> call, @NonNull Throwable t) {
                            loadedCategories[0]++;
                            Log.e("LOAD_FAILURE", "Gọi API thất bại", t);
                        }
                    });
        }
    }

    private void loadProductsByCategory(String categoryId) {
        String token = "Bearer " + new SharedPrefHelper(this).getToken();
        Log.d("API_CALL", "Gọi API với Category ID: " + categoryId + " | Token: " + token);

        RetrofitClient.getRetrofitInstance().create(ApiService.class)
                .getProductsByCategory(categoryId, token)
                .enqueue(new Callback<ApiResponse<PageData<List<Product>>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<PageData<List<Product>>>> call,
                                           @NonNull Response<ApiResponse<PageData<List<Product>>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PageData<List<Product>> pageData = response.body().getData();

                            if (pageData != null && pageData.getData() != null) {
                                allProducts.addAll(pageData.getData()); // Thêm trực tiếp vì `data` đã là `List<Product>`
                                Log.d("API_SUCCESS", "Category: " + categoryId + " | Đã tải " + allProducts.size() + " sản phẩm");
                            } else {
                                Log.d("PRODUCTS_DEBUG", "Không có sản phẩm nào cho category: " + categoryId);
                            }
                        } else {
                            Log.d("API_ERROR", "API trả về lỗi: " + response.code());
                        }
                        productAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<PageData<List<Product>>>> call, @NonNull Throwable t) {
                        Log.e("API_FAILURE", "API Call Failed!", t);
                    }
                });
    }

    private void filterProductsByCategory(String categoryId) {
        String token = "Bearer " + new SharedPrefHelper(this).getToken();
        Log.d("FILTER_API", "Gọi API lấy sản phẩm của Category: " + categoryId);

        RetrofitClient.getRetrofitInstance().create(ApiService.class)
                .getProductsByCategory(categoryId, token)
                .enqueue(new Callback<ApiResponse<PageData<List<Product>>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<PageData<List<Product>>>> call,
                                           @NonNull Response<ApiResponse<PageData<List<Product>>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PageData<List<Product>> pageData = response.body().getData();
                            if (pageData != null && pageData.getData() != null) {
                                List<Product> filteredProducts = new ArrayList<>(pageData.getData());
                                productAdapter.Update(filteredProducts);
                                Log.d("FILTER_SUCCESS", "Đã lọc được " + filteredProducts.size() + " sản phẩm.");
                            } else {
                                productAdapter.Update(new ArrayList<>()); // Nếu không có sản phẩm thì xóa danh sách
                                Log.d("FILTER_EMPTY", "Không có sản phẩm nào cho category này.");
                            }
                        } else {
                            Log.e("FILTER_ERROR", "API lỗi hoặc không trả về dữ liệu");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<PageData<List<Product>>>> call, @NonNull Throwable t) {
                        Log.e("FILTER_FAILURE", "Gọi API thất bại", t);
                    }
                });
    }

}
