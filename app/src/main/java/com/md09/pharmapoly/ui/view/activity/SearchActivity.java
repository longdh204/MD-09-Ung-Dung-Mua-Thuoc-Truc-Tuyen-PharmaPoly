package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.md09.pharmapoly.Models.SearchResponse;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Button prevPageBtn, nextPageBtn;
    private SearchView searchView;  // Sử dụng SearchView từ AndroidX
    private int currentPage = 1;
    private String searchKeyword = "";
    private SharedPrefHelper sharedPrefHelper; // SharedPrefHelper để lấy token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        prevPageBtn = findViewById(R.id.prevPageBtn);
        nextPageBtn = findViewById(R.id.nextPageBtn);
        searchView = findViewById(R.id.searchView);  // Sử dụng đúng SearchView

        sharedPrefHelper = new SharedPrefHelper(this);  // Khởi tạo SharedPrefHelper

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        // Lấy EditText bên trong SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);  // Truy cập EditText bên trong SearchView

        // Thiết lập nội dung cho EditText bên trong SearchView
        searchEditText.setHint("Tìm kiếm sản phẩm...");

        // Thiết lập listener cho SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchKeyword = query;
                    currentPage = 1;  // Reset to page 1
                    fetchSearchResults(searchKeyword, currentPage);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Pagination Buttons
        prevPageBtn.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                fetchSearchResults(searchKeyword, currentPage);
            }
        });

        nextPageBtn.setOnClickListener(v -> {
            currentPage++;
            fetchSearchResults(searchKeyword, currentPage);
        });
    }

    private void fetchSearchResults(String keyword, int page) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        // Lấy token từ SharedPreferences
        String token = sharedPrefHelper.getToken();
        if (token != null) {
            // Truyền token vào header Authorization
            Call<ApiResponse<SearchResponse>> call = apiService.searchProducts(keyword, page, 10, "Bearer " + token);
            call.enqueue(new Callback<ApiResponse<SearchResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<SearchResponse>> call, Response<ApiResponse<SearchResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<SearchResponse> apiResponse = response.body();
                        List<Product> products = apiResponse.getData().getProducts();
                        if (products != null && !products.isEmpty()) {
                            productAdapter.Update(products);
                        } else {
                            Toast.makeText(SearchActivity.this, "Không có sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            // In chi tiết lỗi
                            String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Không xác định";
                            Log.e("API_ERROR", "Response Error: " + errorMessage);
                            Toast.makeText(SearchActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.e("API_ERROR", "Error parsing error body", e);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<SearchResponse>> call, Throwable t) {
                    Log.e("API_ERROR", "Error: " + t.getMessage());
                    Toast.makeText(SearchActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(SearchActivity.this, "Token không hợp lệ. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
        }
    }
}
