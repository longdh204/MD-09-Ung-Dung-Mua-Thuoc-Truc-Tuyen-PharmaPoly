package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.Models.PageData;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nav_Medicine extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private final List<Product> allProducts = new ArrayList<>();
    private int loadedCategoryCount = 0;
    private final List<String> categoryIds = Arrays.asList(
            "67d1a9012ae6c07ce3700873", // Thuốc dị ứng
            "67d1a91f2ae6c07ce3700875", // Thuốc da liễu
            "67d1aa232ae6c07ce3700884", // Miếng dán cao xoa
            "67d1a97c2ae6c07ce3700877", // Cơ xương khớp
            "67d1a9972ae6c07ce370087a", // Thuốc ung thư
            "67d1a9f72ae6c07ce370087f", // Thuốc hô hấp
            "67d1a9e22ae6c07ce370087d", // Thuốc mắt tai mũi họng
            "67d1a9f72ae6c07ce370087f", // Thuốc giải độc khử độc
            "67d1aa422ae6c07ce3700887", // Thuốc bổ vitamin
            "67d1aa552ae6c07ce3700889", // Thuốc giảm đau hạ sốt
            "67d1aa762ae6c07ce370088d", // Thuốc kháng sinh kháng nấm
            "67d1aa8b2ae6c07ce370088f"  // Thuốc hệ thần kinh
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_medicine);

        recyclerView = findViewById(R.id.thucphamchucnang);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(productAdapter);
        loadAllProducts();

        // Ánh xạ các button từ layout
        findViewById(R.id.btnthuocdiung).setOnClickListener(v -> filterProductsByCategory("67d1a9012ae6c07ce3700873"));
        findViewById(R.id.btnthuocdalieu).setOnClickListener(v -> filterProductsByCategory("67d1a91f2ae6c07ce3700875"));
        findViewById(R.id.btncoxuongkhop).setOnClickListener(v -> filterProductsByCategory("67d1a97c2ae6c07ce3700877"));
        findViewById(R.id.btnthuocungthu).setOnClickListener(v -> filterProductsByCategory("67d1a9972ae6c07ce370087a"));
        findViewById(R.id.btnthuochohap).setOnClickListener(v -> filterProductsByCategory("67d1a9f72ae6c07ce370087f"));
        findViewById(R.id.btnthuocmattaimuihong).setOnClickListener(v -> filterProductsByCategory("67d1a9e22ae6c07ce370087d"));

        findViewById(R.id.btnthuocgiaidockhudoc).setOnClickListener(v -> filterProductsByCategory("67d1a9f72ae6c07ce370087f"));
        findViewById(R.id.btnmiengdancaoxoa).setOnClickListener(v -> filterProductsByCategory("67d1aa232ae6c07ce3700884"));
        findViewById(R.id.btnthuocbovitamin).setOnClickListener(v -> filterProductsByCategory("67d1aa422ae6c07ce3700887"));
        findViewById(R.id.btnthuocgiamdauhasot).setOnClickListener(v -> filterProductsByCategory("67d1aa552ae6c07ce3700889"));
        findViewById(R.id.btnthuockhangsinhkhangnam).setOnClickListener(v -> filterProductsByCategory("67d1aa762ae6c07ce370088d"));
        findViewById(R.id.btnthuochethankinh).setOnClickListener(v -> filterProductsByCategory("67d1aa8b2ae6c07ce370088f"));

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
