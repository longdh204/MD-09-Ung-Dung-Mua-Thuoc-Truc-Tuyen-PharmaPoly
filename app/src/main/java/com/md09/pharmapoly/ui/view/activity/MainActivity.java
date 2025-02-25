package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.ui.components.ViewPagerBottomNavigationMainAdapter;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation_main;
    private ViewPagerBottomNavigationMainAdapter bottom_navigation_main_adapter;
    private ViewPager2 view_pager_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);

        initUI();
        SetupBottomNavigation();
    }
    private void SetupBottomNavigation() {
        view_pager_main.setAdapter(bottom_navigation_main_adapter);

        bottom_navigation_main.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    view_pager_main.setCurrentItem(0, true);
                    return true;
                } else if (itemId == R.id.notification) {
                    view_pager_main.setCurrentItem(1, true);
                    return true;
                } else if (itemId == R.id.cart) {
                    view_pager_main.setCurrentItem(2, true);
                    return true;
                } else if (itemId == R.id.profile) {
                    view_pager_main.setCurrentItem(3, true);
                    return true;
                }
                return false;
            }
        });
        view_pager_main.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottom_navigation_main.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        bottom_navigation_main.setSelectedItemId(R.id.notification);
                        break;
                    case 2:
                        bottom_navigation_main.setSelectedItemId(R.id.cart);
                        break;
                    case 3:
                        bottom_navigation_main.setSelectedItemId(R.id.profile);
                        break;
                }
            }
        });
    }
    private void initUI() {
        bottom_navigation_main = findViewById(R.id.bottomNavigationMain);
        view_pager_main = findViewById(R.id.viewPagerMain);
        bottom_navigation_main_adapter = new ViewPagerBottomNavigationMainAdapter(this);

    }
}