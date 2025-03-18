package com.md09.pharmapoly.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.contact_support;
import com.md09.pharmapoly.ui.components.ViewPagerBottomNavigationMainAdapter;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation_main;
    private ViewPagerBottomNavigationMainAdapter bottom_navigation_main_adapter;
    private ViewPager2 view_pager_main;
    private FloatingActionButton btn_fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);
        btn_fab=findViewById(R.id.fab1);
        btn_fab.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, contact_support.class);
            startActivity(intent);
        });
        initUI();
        SetupBottomNavigation();
    }

    private void SetupBottomNavigation() {
        view_pager_main.setAdapter(bottom_navigation_main_adapter);

        // Disable swipe functionality of ViewPager2
        view_pager_main.setUserInputEnabled(false); // This line disables swipe gesture

        bottom_navigation_main.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    view_pager_main.setCurrentItem(0, false); // Switch to Home tab
                    return true;
                } else if (itemId == R.id.notification) {
                    view_pager_main.setCurrentItem(1, false); // Switch to Notification tab
                    return true;
                } else if (itemId == R.id.cart) {
                    view_pager_main.setCurrentItem(2, false); // Switch to Cart tab
                    return true;
                } else if (itemId == R.id.profile) {
                    view_pager_main.setCurrentItem(3, false); // Switch to Profile tab
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
