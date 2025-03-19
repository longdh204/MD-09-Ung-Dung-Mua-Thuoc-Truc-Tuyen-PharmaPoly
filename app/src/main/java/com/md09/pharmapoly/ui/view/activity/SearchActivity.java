package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.md09.pharmapoly.Models.SearchResponse;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.Models.Product;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.network.RetrofitClient;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Button prevPageBtn, nextPageBtn;
    private SearchView searchView;
    private int currentPage = 0;  // Trang hiện tại, bắt đầu từ 0
    private String searchKeyword = "";
    private List<Product> totalProducts = new ArrayList<>();
    private static final int ITEMS_PER_PAGE = 8;  // Mỗi trang sẽ hiển thị 8 sản phẩm
    private SharedPrefHelper sharedPrefHelper;
    private TextView pageInfoTextView; // TextView để hiển thị thông tin trang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        prevPageBtn = findViewById(R.id.prevPageBtn);
        nextPageBtn = findViewById(R.id.nextPageBtn);
        searchView = findViewById(R.id.searchView);
        pageInfoTextView = findViewById(R.id.pageInfoTextView); // Khởi tạo TextView

        // Sử dụng GridLayoutManager với 2 item mỗi hàng
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        sharedPrefHelper = new SharedPrefHelper(this);

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHint("Tìm kiếm sản phẩm...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchKeyword = query;
                    currentPage = 0;  // Reset to page 0
                    fetchSearchResults(searchKeyword);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        prevPageBtn.setOnClickListener(v -> { //"Prev"
            if (currentPage > 0) {
                currentPage--;  // Giảm trang hiện tại
                displayPage(currentPage);  // Tải lại dữ liệu
            }
        });
        nextPageBtn.setOnClickListener(v -> { //"Next"
            if ((currentPage + 1) * ITEMS_PER_PAGE < totalProducts.size()) {
                currentPage++;  // Tăng trang hiện tại
                displayPage(currentPage);  // Tải lại dữ liệu
            }
        });

    }
    private void fetchSearchResults(String keyword) {
        String token = sharedPrefHelper.getToken();  // Lấy token từ SharedPreferences
        if (token != null && !token.isEmpty()) {
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            // Gửi request tìm kiếm với tham số phân trang không có phân trang (giới hạn số sản phẩm trong 1 lần)
            Call<ApiResponse<SearchResponse>> call = apiService.searchProducts(keyword, 1, 100, "Bearer " + token);  // Lấy tất cả sản phẩm
            call.enqueue(new Callback<ApiResponse<SearchResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<SearchResponse>> call, Response<ApiResponse<SearchResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<SearchResponse> apiResponse = response.body();
                        List<Product> allProducts = apiResponse.getData().getProducts();
                        // Kiểm tra nếu có sản phẩm
                        if (allProducts != null && !allProducts.isEmpty()) {
                            totalProducts = allProducts;  // Lưu tất cả sản phẩm vào danh sách
                            displayPage(currentPage);  // Hiển thị trang hiện tại
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
    private void displayPage(int pageIndex) {
        // Xác định chỉ số bắt đầu và kết thúc của sản phẩm trong trang này
        int startIndex = pageIndex * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalProducts.size());
        // Lấy danh sách sản phẩm trong trang hiện tại
        List<Product> currentPageProducts = totalProducts.subList(startIndex, endIndex);
        // Cập nhật RecyclerView với sản phẩm của trang này
        productAdapter.Update(currentPageProducts);
        // Cập nhật các nút phân trang
        updatePaginationButtons();
        // Cập nhật TextView với số trang hiện tại
        pageInfoTextView.setText("" + (currentPage + 1) + " / " + getTotalPages());
    }
    private void updatePaginationButtons() {
        // Kiểm tra nếu có trang trước
        prevPageBtn.setEnabled(currentPage > 0);
        // Kiểm tra nếu có trang tiếp theo
        nextPageBtn.setEnabled((currentPage + 1) * ITEMS_PER_PAGE < totalProducts.size());
    }
    private int getTotalPages() {
        return (int) Math.ceil((double) totalProducts.size() / ITEMS_PER_PAGE);
    }
}
