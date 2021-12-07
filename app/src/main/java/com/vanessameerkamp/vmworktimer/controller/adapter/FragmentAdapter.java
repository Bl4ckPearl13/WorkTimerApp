package com.vanessameerkamp.vmworktimer.controller.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vanessameerkamp.vmworktimer.view.fragment.CustomerListFragment;
import com.vanessameerkamp.vmworktimer.view.fragment.ProjectListFragment;
import com.vanessameerkamp.vmworktimer.view.fragment.WorkTimeListFragment;
import com.vanessameerkamp.vmworktimer.view.fragment.WorkTimerFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return new WorkTimeListFragment();
            case 2:
                return new ProjectListFragment();
            case 3:
                return new CustomerListFragment();
        }
        return new WorkTimerFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
