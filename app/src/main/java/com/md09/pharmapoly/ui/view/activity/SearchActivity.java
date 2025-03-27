package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.md09.pharmapoly.Models.SearchResponse;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.ui.view.fragment.HomeFragment;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Button prevPageBtn, nextPageBtn;
    private SearchView searchView;
    private TextView pageInfoTextView;
    private Set<String> categories = new HashSet<>();
    private Set<String> brands = new HashSet<>();
    private List<Product> totalProducts = new ArrayList<>();
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 8;
    private SharedPrefHelper sharedPrefHelper;

    private HorizontalScrollView categoryScrollView, brandScrollView;
    private LinearLayout categoryLayout, brandLayout;
    private LinearLayout historyLayout;  // Lịch sử tìm kiếm
    private ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        InitUI();

        sharedPrefHelper = new SharedPrefHelper(this);
        // Hiển thị lịch sử tìm kiếm khi mở màn hình tìm kiếm
        loadSearchHistory();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    fetchSearchResults(query);  // Gọi hàm tìm kiếm khi người dùng submit query
                    saveSearchHistory(query);  // Lưu lịch sử tìm kiếm
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        prevPageBtn.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage(currentPage);
            }
        });

        nextPageBtn.setOnClickListener(v -> {
            if ((currentPage + 1) * ITEMS_PER_PAGE < totalProducts.size()) {
                currentPage++;
                displayPage(currentPage);
            }
        });
    }

    private void InitUI() {
        recyclerView = findViewById(R.id.recyclerView);
        prevPageBtn = findViewById(R.id.prevPageBtn);
        nextPageBtn = findViewById(R.id.nextPageBtn);
        searchView = findViewById(R.id.searchView);
        pageInfoTextView = findViewById(R.id.pageInfoTextView);
        categoryScrollView = findViewById(R.id.categoryScrollView);
        brandScrollView = findViewById(R.id.brandScrollView);
        categoryLayout = findViewById(R.id.categoryLayout);
        brandLayout = findViewById(R.id.brandLayout);
        historyLayout = findViewById(R.id.historyLayout);
        imgback = findViewById(R.id.imgback);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(productAdapter);
    }

    // Load lịch sử tìm kiếm từ SharedPreferences và hiển thị
    private void loadSearchHistory() {
        List<String> history = sharedPrefHelper.getSearchHistory();
        historyLayout.removeAllViews();

        for (String keyword : history) {
            Button historyButton = new Button(this);
            historyButton.setText(keyword);

            historyButton.setBackgroundResource(R.drawable.buttonsearch);  // Thay "button_background" bằng tên file hình nền của bạn
            historyButton.setTextColor(getResources().getColor(R.color.black));
//            historyButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/overpass_regular.ttf"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 0, 0, 0);  // Margin: (left, top, right, bottom)
            historyButton.setLayoutParams(params);
            historyButton.setAllCaps(false);  // Hiển thị chữ thường

            historyButton.setOnClickListener(v -> {
                searchView.setQuery(keyword, false);
                fetchSearchResults(keyword);
            });
            historyLayout.addView(historyButton);
        }
    }

    // Lưu lịch sử tìm kiếm vào SharedPreferences
    private void saveSearchHistory(String query) {
        List<String> history = sharedPrefHelper.getSearchHistory();
        if (!history.contains(query)) {
            history.add(0, query);  // Lưu từ khóa vào đầu danh sách
            if (history.size() > 5) {
                history.remove(history.size() - 1);  // Giới hạn lịch sử tìm kiếm là 5
            }
            sharedPrefHelper.saveSearchHistory(history);  // Lưu lại vào SharedPreferences
            loadSearchHistory();  // Cập nhật lịch sử tìm kiếm trên UI
        }
    }

    private void fetchSearchResults(String keyword) {
        String token = sharedPrefHelper.getToken();  // Lấy token từ SharedPreferences
        if (token != null && !token.isEmpty()) {
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

            // Gửi request tìm kiếm với tham số phân trang không có phân trang (giới hạn số sản phẩm trong 1 lần)
            Call<ApiResponse<SearchResponse>> call = apiService.searchProducts(keyword, 1, 100, "Bearer " + token);

            call.enqueue(new Callback<ApiResponse<SearchResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<SearchResponse>> call, Response<ApiResponse<SearchResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<SearchResponse> apiResponse = response.body();
                        List<Product> allProducts = apiResponse.getData().getProducts();

                        if (allProducts != null && !allProducts.isEmpty()) {
                            totalProducts = allProducts;
                            updateCategoriesAndBrands(allProducts);
                            displayPage(currentPage);
                        } else {
                            Toast.makeText(SearchActivity.this, "Không có sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SearchActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<SearchResponse>> call, Throwable t) {
                    Toast.makeText(SearchActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(SearchActivity.this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCategoriesAndBrands(List<Product> products) {
        categories.clear();  // Xóa các category cũ
        brands.clear();  // Xóa các brand cũ

        for (Product product : products) {
            categories.add(product.getCategory().getName());  // Thêm category vào set
            brands.add(product.getBrand().getName());  // Thêm brand vào set
        }

        displayCategoriesAndBrands();
    }

    private void displayCategoriesAndBrands() {
        categoryLayout.removeAllViews();  // Xóa các view cũ
        brandLayout.removeAllViews();     // Xóa các view cũ

        // Hiển thị categories
        for (String category : categories) {
            Button categoryButton = new Button(this);
            categoryButton.setText(category);
            categoryButton.setOnClickListener(v -> filterByCategory(category));
            categoryLayout.addView(categoryButton);

            categoryButton.setBackgroundResource(R.drawable.buttonsearch);  // Thay "button_background" bằng tên file hình nền của bạn
            categoryButton.setTextColor(getResources().getColor(R.color.black));
//            historyButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/overpass_regular.ttf"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 0, 0, 0);  // Margin: (left, top, right, bottom)
            categoryButton.setLayoutParams(params);
            categoryButton.setPadding(20, 10, 20, 10);
            categoryButton.setAllCaps(false);  // Hiển thị chữ thường
        }

        // Hiển thị brands
        for (String brand : brands) {
            Button brandButton = new Button(this);
            brandButton.setText(brand);
            brandButton.setOnClickListener(v -> filterByBrand(brand));
            brandLayout.addView(brandButton);

            brandButton.setBackgroundResource(R.drawable.buttonsearch);  // Thay "button_background" bằng tên file hình nền của bạn
            brandButton.setTextColor(getResources().getColor(R.color.black));
//            historyButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/overpass_regular.ttf"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 0, 0, 0);  // Margin: (left, top, right, bottom)
            brandButton.setLayoutParams(params);
            brandButton.setPadding(20, 10, 20, 10);
            brandButton.setAllCaps(false);  // Hiển thị chữ thường
        }
    }

    private void filterByCategory(String category) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : totalProducts) {
            if (product.getCategory().getName().equals(category)) {
                filteredProducts.add(product);
            }
        }
        productAdapter.Update(filteredProducts);  // Cập nhật danh sách sản phẩm
    }

    private void filterByBrand(String brand) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : totalProducts) {
            if (product.getBrand().getName().equals(brand)) {
                filteredProducts.add(product);
            }
        }
        productAdapter.Update(filteredProducts);  // Cập nhật danh sách sản phẩm
    }

    private void displayPage(int pageIndex) {
        int startIndex = pageIndex * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalProducts.size());

        List<Product> currentPageProducts = totalProducts.subList(startIndex, endIndex);
        productAdapter.Update(currentPageProducts);
        updatePaginationButtons();
        pageInfoTextView.setText("" + (currentPage + 1) + " / " + getTotalPages());
    }

    private void updatePaginationButtons() {
        prevPageBtn.setEnabled(currentPage > 0);
        nextPageBtn.setEnabled((currentPage + 1) * ITEMS_PER_PAGE < totalProducts.size());
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) totalProducts.size() / ITEMS_PER_PAGE);
    }
}
