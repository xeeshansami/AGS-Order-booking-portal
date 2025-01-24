package com.ags.agssalesandroidclientorderdocter.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ags.agssalesandroidclientorderdocter.fragments.HistoryFragment;
import com.ags.agssalesandroidclientorderdocter.fragments.OrderBooking;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderBooking();
            case 1:
                return new HistoryFragment();
            default:
                return new OrderBooking();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}