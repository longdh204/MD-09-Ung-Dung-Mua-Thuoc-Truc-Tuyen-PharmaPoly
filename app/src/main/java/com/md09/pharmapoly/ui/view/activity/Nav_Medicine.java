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
            "67d4015fb0aaf7c883554d5e", // Thuốc dị ứng
            "67d40169b0aaf7c883554d60", // Thuốc da liễu
            "67d40198b0aaf7c883554d6c", // Miếng dán cao xoa
            "67d4016eb0aaf7c883554d62", // Cơ xương khớp
            "67d40173b0aaf7c883554d64", // Thuốc ung thư
            "67d40179b0aaf7c883554d66", // Thuốc hô hấp
            "67d40181b0aaf7c883554d68", // Thuốc mắt tai mũi họng
            "67d40186b0aaf7c883554d6a", // Thuốc giải độc khử độc
            "67d401a0b0aaf7c883554d6e", // Thuốc bổ vitamin
            "67d401a8b0aaf7c883554d70", // Thuốc giảm đau hạ sốt
            "67d401aeb0aaf7c883554d72", // Thuốc kháng sinh kháng nấm
            "67d401b3b0aaf7c883554d74"  // Thuốc hệ thần kinh
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
        findViewById(R.id.btnthuocdiung).setOnClickListener(v -> filterProductsByCategory("67d4015fb0aaf7c883554d5e"));
        findViewById(R.id.btnthuocdalieu).setOnClickListener(v -> filterProductsByCategory("67d40169b0aaf7c883554d60"));
        findViewById(R.id.btncoxuongkhop).setOnClickListener(v -> filterProductsByCategory("67d4016eb0aaf7c883554d62"));
        findViewById(R.id.btnthuocungthu).setOnClickListener(v -> filterProductsByCategory("67d40173b0aaf7c883554d64"));
        findViewById(R.id.btnthuochohap).setOnClickListener(v -> filterProductsByCategory("67d40179b0aaf7c883554d66"));
        findViewById(R.id.btnthuocmattaimuihong).setOnClickListener(v -> filterProductsByCategory("67d40181b0aaf7c883554d68"));

        findViewById(R.id.btnthuocgiaidockhudoc).setOnClickListener(v -> filterProductsByCategory("67d40186b0aaf7c883554d6a"));
        findViewById(R.id.btnmiengdancaoxoa).setOnClickListener(v -> filterProductsByCategory("67d40198b0aaf7c883554d6c"));
        findViewById(R.id.btnthuocbovitamin).setOnClickListener(v -> filterProductsByCategory("67d401a0b0aaf7c883554d6e"));
        findViewById(R.id.btnthuocgiamdauhasot).setOnClickListener(v -> filterProductsByCategory("67d401a8b0aaf7c883554d70"));
        findViewById(R.id.btnthuockhangsinhkhangnam).setOnClickListener(v -> filterProductsByCategory("67d401aeb0aaf7c883554d72"));
        findViewById(R.id.btnthuochethankinh).setOnClickListener(v -> filterProductsByCategory("67d401b3b0aaf7c883554d74"));

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
