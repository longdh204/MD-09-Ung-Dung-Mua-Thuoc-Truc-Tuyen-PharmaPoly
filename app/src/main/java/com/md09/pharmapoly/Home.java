package com.md09.pharmapoly;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.md09.pharmapoly.Adapters.CategoryAdapter;
import com.md09.pharmapoly.Adapters.ProductAdapter;
import com.md09.pharmapoly.Adapters.SliderAdapter;
import com.md09.pharmapoly.Models.Category;
import com.md09.pharmapoly.data.model.ApiResponse;
import com.md09.pharmapoly.data.model.Product;
import com.md09.pharmapoly.network.ApiClient;
import com.md09.pharmapoly.network.ApiService;
import com.md09.pharmapoly.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private String token;
    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DrawerLayout drawerLayout;

    private ViewPager2 viewPager2;
    private SliderAdapter sliderAdapter;
    private CircleIndicator3 circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        ProductAdapter adapter = new ProductAdapter(this, productList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // Hiển thị 2 cột
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(20)); // Thêm khoảng cách giữa các item
//        recyclerView.setAdapter(adapter);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter và gắn vào RecyclerView
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);  // Gắn productList vào adapter
        recyclerView.setAdapter(productAdapter);  // Đảm bảo rằng adapter đã được gắn

        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
        token = sharedPrefHelper.getToken();
        if (token != null) {
            Log.d("Token", token);
        }else{
            Log.d("Token", "Token is null");
        }
        fetchProductData("67b3433a1cb34414424e753c");  // Gọi API để lấy dữ liệu
        fetchProductData("67c07eae88a021ab2137d14e");  // Gọi API để lấy dữ liệu
        fetchProductData("67c07f7c88a021ab2137d179");  // Gọi API để lấy dữ liệu
        fetchProductData("67c080fe88a021ab2137d1d7");  // Gọi API để lấy dữ liệu
        fetchProductData("67c2bac612ae5bc6e9990212");  // Gọi API để lấy dữ liệu
        fetchProductData("67c2ba6612ae5bc6e9990209");  // Gọi API để lấy dữ liệu


        viewPager2 = findViewById(R.id.viewPagerSlider);
        circleIndicator = findViewById(R.id.dotsIndicator);

// Tạo danh sách các hình ảnh drawable trong ứng dụng thay vì URL
        List<Integer> sliderImageIds = new ArrayList<>();
        sliderImageIds.add(R.drawable.bn1);  // Thay thế với ID hình ảnh trong drawable
        sliderImageIds.add(R.drawable.bn2);
        sliderImageIds.add(R.drawable.bn3);
        sliderImageIds.add(R.drawable.bn4);
        sliderImageIds.add(R.drawable.bn5);
        sliderImageIds.add(R.drawable.bn6);

// Gán Adapter cho ViewPager2
        sliderAdapter = new SliderAdapter(sliderImageIds);
        viewPager2.setAdapter(sliderAdapter);

// Kết nối CircleIndicator3 với ViewPager2
        circleIndicator.setViewPager(viewPager2);

        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menuIcon = findViewById(R.id.menu_icon);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ImageView bellIcon = findViewById(R.id.bell_icon);
        bellIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, NotificationsActivity.class);
            startActivity(intent);
        });

        recyclerViewCategory = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(this, 4));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Cần mua thuốc", R.drawable.ic_medicine));
        categoryList.add(new Category("Khám bác sĩ", R.drawable.ic_doctor));
        categoryList.add(new Category("Bảo hiểm", R.drawable.ic_insurance));
        categoryList.add(new Category("Thông tin", R.drawable.ic_calendar));
        categoryList.add(new Category("Ghi chú", R.drawable.ic_list1));
        categoryList.add(new Category("Đơn thuốc", R.drawable.ic_list2));
        categoryList.add(new Category("Lịch hẹn", R.drawable.ic_list3));

        categoryAdapter = new CategoryAdapter(this, categoryList);
        recyclerViewCategory.setAdapter(categoryAdapter);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_notification) {
                Toast.makeText(Home.this, "Thông báo", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_healthcare) {
                Toast.makeText(Home.this, "Thực phẩm chức năng", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_pharmacy) {
                Toast.makeText(Home.this, "Dược mỹ phẩm", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void fetchProductData(String productId) {
        String productUrl = "https://pharmapoly-server.onrender.com/api/product/" + productId;  // Dùng productId để lấy sản phẩm theo ID
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Gọi API để lấy thông tin sản phẩm
        Call<ApiResponse<Product>> productCall = apiService.getProduct(productUrl, "Bearer " + token);
        productCall.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Product> apiResponse = response.body();
                    Product product = apiResponse.getData();

                    // In ra để kiểm tra imageUrl sau khi lấy từ API
                    Log.d("APIResponse", "Product fetched: " + product.toString());

                    // Kiểm tra xem imageUrl có giá trị không
                    String imageUrl = product.getImageUrl();
                    if (imageUrl != null) {
                        Log.d("APIResponse", "Image URL: " + imageUrl);
                    }

                    // Cập nhật RecyclerView với sản phẩm
                    productList.add(product);
                    productAdapter.notifyDataSetChanged();  // Cập nhật danh sách sản phẩm
                    Toast.makeText(Home.this, "Lấy dữ liệu thành công", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("APIError", "Failed to fetch product: " + response.message());
                    Toast.makeText(Home.this, "Failed to fetch product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Log.e("APIError", "Error fetching product: " + t.getMessage());
                Toast.makeText(Home.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
//    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
//        private final int space;
//
//        public SpaceItemDecoration(int space) {
//            this.space = space;
//        }
////
////        @Override
////        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
////            outRect.left = space / 2;
////            outRect.right = space / 2;
////            outRect.bottom = space;
////            if (parent.getChildLayoutPosition(view) < 2) {
////                outRect.top = space;
////            }
////        }
//    }

}

