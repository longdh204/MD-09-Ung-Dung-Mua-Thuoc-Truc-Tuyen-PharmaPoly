package com.md09.pharmapoly.ui.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Button prevPageBtn, nextPageBtn;
    //private SearchView searchView;
    private EditText searchView;
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
    private String keyword = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        InitUI();

        sharedPrefHelper = new SharedPrefHelper(this);
        // Hiển thị lịch sử tìm kiếm khi mở màn hình tìm kiếm
        loadSearchHistory();

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (!TextUtils.isEmpty(query)) {
//                    fetchSearchResults(query);  // Gọi hàm tìm kiếm khi người dùng submit query
//                    saveSearchHistory(query);  // Lưu lịch sử tìm kiếm
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = searchView.getText().toString().trim();
                    if (!TextUtils.isEmpty(query)) {
                        fetchSearchResults(query,-1);
                        saveSearchHistory(query);
                    }
                    return true;
                }
                return false;
            }
        });
        imgback.setOnClickListener(v -> {
            finish();
        });
        searchView.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

//        prevPageBtn.setOnClickListener(v -> {
//            if (currentPage > 0) {
//                currentPage--;
//                displayPage(currentPage);
//            }
//        });
//
//        nextPageBtn.setOnClickListener(v -> {
//            if ((currentPage + 1) * ITEMS_PER_PAGE < totalProducts.size()) {
//                currentPage++;
//                displayPage(currentPage);
//            }
//        });

        prevPageBtn.setOnClickListener(v -> {
            currentPage--;
            fetchSearchResults(keyword, currentPage);
        });

        nextPageBtn.setOnClickListener(v -> {
            currentPage++;
            fetchSearchResults(keyword, currentPage);
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
        imgback = findViewById(R.id.imgback);
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
//                searchView.setQuery(keyword, false);
                searchView.setText(keyword);
                searchView.setSelection(keyword.length());
                fetchSearchResults(keyword,-1);
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

    private void fetchSearchResults(String keyword, int page) {
        this.keyword = keyword;
        if (page == -1) {
            page = 1;
        }
        currentPage = page;
        String token = sharedPrefHelper.getToken();  // Lấy token từ SharedPreferences
        if (token != null && !token.isEmpty()) {

            new RetrofitClient()
                    .callAPI()
                    .searchProducts(keyword, page, 8, "Bearer " + new SharedPrefHelper(this).getToken())
                    .enqueue(new Callback<ApiResponse<SearchResponse>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<SearchResponse>> call, Response<ApiResponse<SearchResponse>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ApiResponse<SearchResponse> apiResponse = response.body();
                                SearchResponse searchData = apiResponse.getData();

                                if (searchData != null) {
                                    List<Product> allProducts = searchData.getProducts();

                                    if (allProducts != null && !allProducts.isEmpty()) {
                                        totalProducts = allProducts;
                                        updateCategoriesAndBrands(allProducts);  // Nếu cần xử lý category, brand thì cũng dễ lấy luôn
//                                        displayPage(currentPage);

                                        productAdapter.Update(searchData.getProducts());
//                                        updatePaginationButtons();
                                        pageInfoTextView.setText("" + searchData.getCurrentPage() + " / " + searchData.getTotalPages());
                                        updatePaginationButtons(searchData.isHasPrevPage(), searchData.isHasNextPage());
                                    } else {
                                        Toast.makeText(SearchActivity.this, "Không tìm thấy sản phẩm phù hợp", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(SearchActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<SearchResponse>> call, Throwable t) {
                            // Handle lỗi
                        }
                    });

        }
        else {
            Toast.makeText(SearchActivity.this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
    private void updatePaginationButtons(boolean hasPrevPage, boolean hasNextPage) {
        // Ẩn/Hiện nút Prev
        if (hasPrevPage) {
            prevPageBtn.setVisibility(View.VISIBLE);  // Hiển thị nút Prev
        } else {
            prevPageBtn.setVisibility(View.INVISIBLE);  // Ẩn nút Prev
        }

        // Ẩn/Hiện nút Next
        if (hasNextPage) {
            nextPageBtn.setVisibility(View.VISIBLE);  // Hiển thị nút Next
        } else {
            nextPageBtn.setVisibility(View.INVISIBLE);  // Ẩn nút Next
        }
    }

    private void updateCategoriesAndBrands(List<Product> products) {
        categories.clear();  // Xóa các category cũ
        brands.clear();  // Xóa các brand cũ

//        for (Product product : products) {
//            categories.add(product.getCategory().getName());  // Thêm category vào set
//            brands.add(product.getBrand().getName());  // Thêm brand vào set
//        }
        for (Product product : products) {
            if (product != null) {
                if (product.getCategory() != null && product.getCategory().getName() != null) {
                    categories.add(product.getCategory().getName());
                }

                if (product.getBrand() != null && product.getBrand().getName() != null) {
                    brands.add(product.getBrand().getName());
                }
            }
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
//        int startIndex = pageIndex * ITEMS_PER_PAGE;
//        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalProducts.size());
//
//        List<Product> currentPageProducts = totalProducts.subList(startIndex, endIndex);
//        productAdapter.Update(currentPageProducts);
//        updatePaginationButtons();
//        pageInfoTextView.setText("" + (currentPage + 1) + " / " + getTotalPages());
    }

    private void updatePaginationButtons() {
        prevPageBtn.setEnabled(currentPage > 0);
        nextPageBtn.setEnabled((currentPage + 1) * ITEMS_PER_PAGE < totalProducts.size());
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) totalProducts.size() / ITEMS_PER_PAGE);
    }
}
