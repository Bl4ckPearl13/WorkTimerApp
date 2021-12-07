package com.vanessameerkamp.vmworktimer.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.listener_fragment.WorkTimeListListener;


public class WorkTimeListFragment extends Fragment {

    public ListView listViewWorkTime;

    WorkTimeListListener workTimeListListener;

    public WorkTimeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_time_list, container, false);

        listViewWorkTime = view.findViewById(R.id.listViewWorkTime);

        workTimeListListener = new WorkTimeListListener(this);

        listViewWorkTime.setOnItemClickListener(workTimeListListener);

        //registerForContextMenu(listViewWorkTime);

        return view;
    }

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getActivity().getMenuInflater().inflate(R.menu.context_menu_work_time, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return workTimeListListener.onContextItemSelected(item);
    }
     */

    @Override
    public void onResume() {
        super.onResume();
        workTimeListListener.onResume();
    }
}