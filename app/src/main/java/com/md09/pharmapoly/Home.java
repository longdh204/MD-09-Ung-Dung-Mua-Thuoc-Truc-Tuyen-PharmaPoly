package com.md09.pharmapoly;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.md09.pharmapoly.Adapters.CategoryAdapter;
import com.md09.pharmapoly.Models.Category;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menuIcon = findViewById(R.id.menu_icon);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view); // Sửa kiểu dữ liệu
        AppCompatButton headButton = findViewById(R.id.head_button);

        // ... (Code xử lý nut thông báo)
        ImageView bellIcon = findViewById(R.id.bell_icon);
        bellIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, NotificationsActivity.class);
            startActivity(intent);
        });


        // Xử lý sự kiện khi bấm vào BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) { // ID của menu item
                //selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_mess) {
                //selectedFragment = new ListenFragment();
            } else if (itemId == R.id.nav_cart) {
                //selectedFragment = new CartFragment();
            } else if (itemId == R.id.nav_profile) {
                //selectedFragment = new ProfileFragment();
            }

            // ... (Code thay thế Fragment, nếu có)

            return true; // Xác nhận item đã được xử lý
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

// Xử lý khi click icon menu
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Xử lý sự kiện khi click vào các mục trong menu
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