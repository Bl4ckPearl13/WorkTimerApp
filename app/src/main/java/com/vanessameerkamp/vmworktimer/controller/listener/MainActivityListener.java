package com.vanessameerkamp.vmworktimer.controller.listener;

import android.os.Parcel;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.vanessameerkamp.vmworktimer.controller.adapter.FragmentAdapter;
import com.vanessameerkamp.vmworktimer.view.activity.MainActivity;

public class MainActivityListener extends ViewPager2.OnPageChangeCallback implements TabLayout.OnTabSelectedListener {

    MainActivity mainActivity;

    public MainActivityListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        setFragmentAdapter();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mainActivity.viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageSelected(int position) {
        mainActivity.tabLayout.selectTab(mainActivity.tabLayout.getTabAt(position));
    }

    private void setFragmentAdapter() {

        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        mainActivity.fragmentAdapter = new FragmentAdapter(fragmentManager, mainActivity.getLifecycle());
        mainActivity.viewPager.setAdapter(mainActivity.fragmentAdapter);
    }
}
