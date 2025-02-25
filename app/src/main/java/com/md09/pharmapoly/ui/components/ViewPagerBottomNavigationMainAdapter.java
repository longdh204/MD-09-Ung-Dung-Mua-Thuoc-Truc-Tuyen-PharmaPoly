package com.md09.pharmapoly.ui.components;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.md09.pharmapoly.ui.view.fragment.CartFragment;
import com.md09.pharmapoly.ui.view.fragment.HomeFragment;
import com.md09.pharmapoly.ui.view.fragment.NotificationFragment;
import com.md09.pharmapoly.ui.view.fragment.ProfileFragment;

public class ViewPagerBottomNavigationMainAdapter extends FragmentStateAdapter {

    public ViewPagerBottomNavigationMainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new HomeFragment();
            case 1: return new NotificationFragment();
            case 2: return new CartFragment();
            case 3: return new ProfileFragment();
            default:break;
        }
        return new HomeFragment();
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}
