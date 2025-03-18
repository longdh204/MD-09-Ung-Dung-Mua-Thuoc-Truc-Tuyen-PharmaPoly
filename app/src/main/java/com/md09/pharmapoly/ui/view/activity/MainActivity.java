package com.md09.pharmapoly.ui.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.md09.pharmapoly.Models.Cart;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.ui.components.ViewPagerBottomNavigationMainAdapter;
import com.md09.pharmapoly.ui.view.fragment.CartFragment;
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.viewmodel.CartViewModel;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation_main;
    private ViewPagerBottomNavigationMainAdapter bottom_navigation_main_adapter;
    private ViewPager2 view_pager_main;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);

        InitUI();
        SetupBottomNavigation();
        cartViewModel.FetchCartData(this);
        cartViewModel.GetCart().observe(this, new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                UpdateCartBadge(cart.getCartItems().size());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (new SharedPrefHelper(this).isProductAddedToCart()) {
            cartViewModel.FetchCartData(this);
        }
    }
    public void UpdateCartBadge(int cartCount) {
        BadgeDrawable badge = bottom_navigation_main.getOrCreateBadge(R.id.cart);
        if (cartCount > 0) {
            badge.setVisible(true);
            badge.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow_a20));
            badge.setBadgeTextColor(ContextCompat.getColor(this, R.color.white_FFF));
            badge.setNumber(cartCount);
        } else {
            badge.setVisible(false);
        }
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

        cartViewModel.FetchCartData(this);
    }

    private void InitUI() {
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        bottom_navigation_main = findViewById(R.id.bottomNavigationMain);
        view_pager_main = findViewById(R.id.viewPagerMain);
        bottom_navigation_main_adapter = new ViewPagerBottomNavigationMainAdapter(this);
    }
}
