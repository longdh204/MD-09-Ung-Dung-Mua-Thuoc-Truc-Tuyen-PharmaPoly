package com.md09.pharmapoly.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.md09.pharmapoly.ui.view.fragment.CancelledFragment;
import com.md09.pharmapoly.ui.view.fragment.DeliveredFragment;
import com.md09.pharmapoly.ui.view.fragment.ProcessingFragment;
import com.md09.pharmapoly.ui.view.fragment.ReturnFragment;
import com.md09.pharmapoly.ui.view.fragment.ShippingFragment;

public class ViewPagerBottomNavigationMyOrders extends FragmentStateAdapter {

    public ViewPagerBottomNavigationMyOrders(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ProcessingFragment();
            case 1: return new ShippingFragment();
            case 2: return new DeliveredFragment();
            case 3: return new ReturnFragment();
            case 4: return new CancelledFragment();

            default:break;
        }
        return new ProcessingFragment();
    }
    @Override
    public int getItemCount() {
        return 5;
    }
}
