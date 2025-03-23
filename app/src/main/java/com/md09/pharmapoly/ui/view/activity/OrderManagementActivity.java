package com.md09.pharmapoly.ui.view.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.md09.pharmapoly.Adapters.OrderStatusAdapter;
import com.md09.pharmapoly.Adapters.ViewPagerBottomNavigationMyOrders;
import com.md09.pharmapoly.R;

import java.util.Arrays;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity {

    public ViewPager2 viewPager2_my_orders;
    private BottomNavigationView bn_my_orders;
    private View indicator;
    private int selectedPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_management);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        InitUI();

        indicator = findViewById(R.id.indicator);
        ViewPagerBottomNavigationMyOrders bottomNavigationAdapter = new ViewPagerBottomNavigationMyOrders(this);
        viewPager2_my_orders.setAdapter(bottomNavigationAdapter);

        RecyclerView rvOrderStatus = findViewById(R.id.rvOrderStatus);
        List<String> orderStatuses = Arrays.asList(
                getString(R.string.processing),
                getString(R.string.shipping),
                getString(R.string.delivered),
                getString(R.string.exchange_return),
                getString(R.string.cancelled));

        OrderStatusAdapter adapter = new OrderStatusAdapter(orderStatuses, (itemView, position) -> {
            //viewPager2_my_orders.setCurrentItem(position, true);
            selectedPosition = position;
            moveIndicator(itemView, 200);
        });

        rvOrderStatus.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvOrderStatus.setAdapter(adapter);
        rvOrderStatus.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                View selectedItem = recyclerView.getLayoutManager().findViewByPosition(selectedPosition);
                moveIndicator(selectedItem,100);
            }
        });

        rvOrderStatus.post(() -> moveIndicator(rvOrderStatus.getChildAt(0),200));
        //BottomNavigationManager();
    }

    private void moveIndicator(View selectedItem, long moveDuration) {
        if (selectedItem == null) return;

        int[] location = new int[2];
        selectedItem.getLocationOnScreen(location);
        int itemX = location[0];
        int itemWidth = selectedItem.getWidth();

        ValueAnimator positionAnimator = ValueAnimator.ofFloat(indicator.getX(), itemX);
        positionAnimator.setDuration(moveDuration);
        positionAnimator.addUpdateListener(animation -> indicator.setX((float) animation.getAnimatedValue()));
        positionAnimator.start();

        ValueAnimator widthAnimator = ValueAnimator.ofInt(indicator.getLayoutParams().width, itemWidth);
        widthAnimator.setDuration(200);
        widthAnimator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams params = indicator.getLayoutParams();
            params.width = (int) animation.getAnimatedValue();
            indicator.setLayoutParams(params);
        });
        widthAnimator.start();

        if (indicator.getVisibility() == View.INVISIBLE) {
            indicator.setVisibility(View.VISIBLE);
        }
    }



//    private void BottomNavigationManager() {
//        ViewPagerBottomNavigationMyOrders bottomNavigationAdapter = new ViewPagerBottomNavigationMyOrders(this);
//        viewPager2_my_orders.setAdapter(bottomNavigationAdapter);
//        bn_my_orders.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if(item.getItemId() == R.id._processing) {
//                    viewPager2_my_orders.setCurrentItem(0);
//                } else if(item.getItemId() == R.id._shipping) {
//                    viewPager2_my_orders.setCurrentItem(1);
//                } else if (item.getItemId() == R.id._delivered) {
//                    viewPager2_my_orders.setCurrentItem(2);
//                } else if (item.getItemId() == R.id._return) {
//                    viewPager2_my_orders.setCurrentItem(3);
//                } else if (item.getItemId() == R.id._cancelled) {
//                    viewPager2_my_orders.setCurrentItem(4);
//                }
//                return true;
//            }
//        });
//        viewPager2_my_orders.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                switch (position) {
//                    case 0:
//                        bn_my_orders.setSelectedItemId(R.id._processing);
//                        break;
//                    case 1:
//                        bn_my_orders.setSelectedItemId(R.id._shipping);
//                        break;
//                    case 2:
//                        bn_my_orders.setSelectedItemId(R.id._delivered);
//                        break;
//                    case 3:
//                        bn_my_orders.setSelectedItemId(R.id._return);
//                        break;
//                    case 4:
//                        bn_my_orders.setSelectedItemId(R.id._cancelled);
//                        break;
//                }
//            }
//        });
//    }

    private void InitUI() {
        viewPager2_my_orders = findViewById(R.id.viewPager2_my_orders);
        //bn_my_orders = findViewById(R.id.bn_my_orders);
    }
}