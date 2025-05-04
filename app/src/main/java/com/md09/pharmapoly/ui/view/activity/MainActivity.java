package com.md09.pharmapoly.ui.view.activity;

import static com.md09.pharmapoly.utils.Constants.CANCELED_KEY;
import static com.md09.pharmapoly.utils.Constants.NOTIFICATION_READ_ALL_KEY;
import static com.md09.pharmapoly.utils.Constants.ORDER_KEY;
import static com.md09.pharmapoly.utils.Constants.PRODUCT_ADDED_TO_CART_KEY;
import static com.md09.pharmapoly.utils.Constants.USER_PROFILE_UPDATED_KEY;
import static com.md09.pharmapoly.utils.Constants.setLocale;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.md09.pharmapoly.utils.SharedPrefHelper;
import com.md09.pharmapoly.viewmodel.CartViewModel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_navigation_main;
    private ViewPagerBottomNavigationMainAdapter bottom_navigation_main_adapter;
    private ViewPager2 view_pager_main;
    private CartViewModel cartViewModel;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);

        InitUI();
        loadLocale();

        new SharedPrefHelper(this).resetBooleanState(ORDER_KEY);
        new SharedPrefHelper(this).resetBooleanState(PRODUCT_ADDED_TO_CART_KEY);
        new SharedPrefHelper(this).resetBooleanState(USER_PROFILE_UPDATED_KEY);
        new SharedPrefHelper(this).resetBooleanState(CANCELED_KEY);
        new SharedPrefHelper(this).resetBooleanState(NOTIFICATION_READ_ALL_KEY);

        SetupBottomNavigation();
        cartViewModel.FetchCartData(this);
        cartViewModel.GetCart().observe(this, new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                UpdateCartBadge(cart.getCartItems().size());
            }
        });
        FloatingActionButton fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(view -> {
            // Tạo một Intent để mở liên kết Zalo
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://zalo.me/0967197293"));
            startActivity(intent);
        });
        checkAndRequestNotificationPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "fcm_channel",
                    "Thông báo",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }
    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Đã cấp quyền thông báo", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Bạn cần cấp quyền để nhận thông báo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadLocale() {
        String langCode = new SharedPrefHelper(this).getLanguage();
        setLocale(this,langCode);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (new SharedPrefHelper(this).getBooleanState(PRODUCT_ADDED_TO_CART_KEY,false) ||
                new SharedPrefHelper(this).getBooleanState(ORDER_KEY,false)) {
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
