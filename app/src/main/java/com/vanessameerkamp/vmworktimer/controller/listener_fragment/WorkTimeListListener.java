package com.vanessameerkamp.vmworktimer.controller.listener_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import com.vanessameerkamp.vmworktimer.controller.adapter.WorkTimeListAdapter;
import com.vanessameerkamp.vmworktimer.controller.database.WorkTimeDataSource;
import com.vanessameerkamp.vmworktimer.model.Mode;
import com.vanessameerkamp.vmworktimer.model.WorkTime;
import com.vanessameerkamp.vmworktimer.view.activity.MainActivity;
import com.vanessameerkamp.vmworktimer.view.activity.WorkTimeDetailActivity;
import com.vanessameerkamp.vmworktimer.view.fragment.WorkTimeListFragment;

import java.util.ArrayList;
import java.util.List;

public class WorkTimeListListener implements AdapterView.OnItemClickListener {

    WorkTimeListFragment workTimeListFragment;

    public List<WorkTime> workTimeList = new ArrayList<>();
    public WorkTimeListAdapter workTimeListAdapter;

    WorkTimeDataSource workTimeDataSource;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public WorkTimeListListener(WorkTimeListFragment workTimeListFragment) {
        this.workTimeListFragment = workTimeListFragment;

        workTimeDataSource = new WorkTimeDataSource(workTimeListFragment.getContext());

        sharedPreferences = workTimeListFragment.getActivity().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setListViewAdapter();
    }

    public void onResume() {
        refreshListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

        WorkTime selectedWorkTime = (WorkTime) parent.getItemAtPosition(position);

        Intent openDetailActivity = new Intent(workTimeListFragment.getActivity(), WorkTimeDetailActivity.class);

        editor.putString("workTimeMode", Mode.SHOW.toString());
        editor.apply();

        openDetailActivity.putExtra("workTime", selectedWorkTime);

        workTimeListFragment.startActivity(openDetailActivity);
    }

    /*
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        WorkTime selectedWorkTime = workTimeList.get(info.position);

        switch (item.getItemId()) {
            case R.id.cmEdit:
                Intent openEditActivity = new Intent(workTimeListFragment.getActivity(), WorkTimeDetailActivity.class);

                editor.putString("workTimeMode", Mode.EDIT.toString());
                editor.apply();

                openEditActivity.putExtra("workTime", selectedWorkTime);

                workTimeListFragment.startActivity(openEditActivity);
                return true;
            case R.id.cmDelete:

                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(workTimeListFragment.getContext());

                alert.setTitle(R.string.delete_dialog_header)
                        .setMessage(R.string.confirm_work_time_deletion)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            workTimeDataSource.deleteWorkTime(selectedWorkTime);
                            refreshListView();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                return true;
        }

        return false;
    }
     */

    private void setListViewAdapter() {

        workTimeList = workTimeDataSource.getAllWorkTimes();

        workTimeListAdapter = new WorkTimeListAdapter(workTimeListFragment.getContext(), workTimeList);
        workTimeListFragment.listViewWorkTime.setAdapter(workTimeListAdapter);
    }

    private void refreshListView() {

        workTimeList.clear();

        workTimeList.addAll(workTimeDataSource.getAllWorkTimes());

        workTimeListAdapter.notifyDataSetChanged();
    }
}
