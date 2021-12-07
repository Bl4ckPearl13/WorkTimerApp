package com.vanessameerkamp.vmworktimer.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.vanessameerkamp.vmworktimer.controller.adapter.FragmentAdapter;
import com.vanessameerkamp.vmworktimer.controller.listener.MainActivityListener;
import com.vanessameerkamp.vmworktimer.R;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES = "settings";

    public TabLayout tabLayout;
    public ViewPager2 viewPager;
    public FragmentAdapter fragmentAdapter;

    TabItem tabWorkTimer;
    TabItem tabWorkTimeList;
    TabItem tabProjectList;
    TabItem tabCustomerList;

    MainActivityListener mainActivityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        mainActivityListener = new MainActivityListener(this);

        setViewListeners();
    }

    private void initializeViews() {

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabWorkTimer = findViewById(R.id.tabWorkTimer);
        tabWorkTimeList = findViewById(R.id.tabWorkTimeList);
        tabProjectList = findViewById(R.id.tabProjectList);
        tabCustomerList = findViewById(R.id.tabCustomerList);
    }

    private void setViewListeners() {

        tabLayout.addOnTabSelectedListener(mainActivityListener);
        viewPager.registerOnPageChangeCallback(mainActivityListener);
    }
}