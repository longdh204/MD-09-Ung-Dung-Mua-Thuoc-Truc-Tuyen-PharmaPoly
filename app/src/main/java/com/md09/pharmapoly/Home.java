package com.md09.pharmapoly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.navigation.NavigationView;
import com.md09.pharmapoly.Adapters.CategoryAdapter;
import com.md09.pharmapoly.Adapters.SliderAdapter;
import com.md09.pharmapoly.Models.Category;
import com.md09.pharmapoly.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import me.relex.circleindicator.CircleIndicator3;

public class Home extends AppCompatActivity {

    // hiển thị item product từ csdl
    private SharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DrawerLayout drawerLayout;

    private ViewPager2 viewPager2;
    private SliderAdapter sliderAdapter;
    private CircleIndicator3 circleIndicator;  // Sử dụng CircleIndicator3
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo ViewPager2 và CircleIndicator3
        viewPager2 = findViewById(R.id.viewPagerSlider);
        circleIndicator = findViewById(R.id.dotsIndicator);

        // Tạo danh sách các hình ảnh cục bộ hoặc URL cố định
        List<String> sliderImageUrls = new ArrayList<>();
        sliderImageUrls.add("https://cdn.vjshop.vn/tin-tuc/phan-biet-raw-va-jpeg/phan-biet-raw-va-jpeg-14.jpg");
        sliderImageUrls.add("https://cdn.vjshop.vn/tin-tuc/phan-biet-raw-va-jpeg/phan-biet-raw-va-jpeg-14.jpg");
        sliderImageUrls.add("https://cdn.vjshop.vn/tin-tuc/phan-biet-raw-va-jpeg/phan-biet-raw-va-jpeg-14.jpg");

        // Gán Adapter cho ViewPager2
        sliderAdapter = new SliderAdapter(sliderImageUrls);
        viewPager2.setAdapter(sliderAdapter);

        // Kết nối CircleIndicator3 với ViewPager2
        circleIndicator.setViewPager(viewPager2);  // Đây là cách đúng

        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menuIcon = findViewById(R.id.menu_icon);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ImageView bellIcon = findViewById(R.id.bell_icon);
        bellIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, NotificationsActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Cần mua thuốc", R.drawable.ic_medicine));
        categoryList.add(new Category("Khám bác sĩ", R.drawable.ic_doctor));
        categoryList.add(new Category("Bảo hiểm", R.drawable.ic_insurance));
        categoryList.add(new Category("Thông tin", R.drawable.ic_calendar));
        categoryList.add(new Category("Ghi chú", R.drawable.ic_list1));
        categoryList.add(new Category("Đơn thuốc", R.drawable.ic_list2));
        categoryList.add(new Category("Lịch hẹn", R.drawable.ic_list3));

        categoryAdapter = new CategoryAdapter(this, categoryList);
        recyclerView.setAdapter(categoryAdapter);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_notification) {
                    Toast.makeText(Home.this, "Thông báo", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_healthcare) {
                    Toast.makeText(Home.this, "Thực phẩm chức năng", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_pharmacy) {
                    Toast.makeText(Home.this, "Dược mỹ phẩm", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START); // Đóng menu sau khi chọn
                return true;
            }
        });
    }

}