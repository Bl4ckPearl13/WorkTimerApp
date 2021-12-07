package com.vanessameerkamp.vmworktimer.controller.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.adapter.WorkTimeListAdapter;
import com.vanessameerkamp.vmworktimer.controller.database.CustomerDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.ProjectDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.WorkTimeDataSource;
import com.vanessameerkamp.vmworktimer.controller.helper.CustomerComparator;
import com.vanessameerkamp.vmworktimer.model.Customer;
import com.vanessameerkamp.vmworktimer.model.Duration;
import com.vanessameerkamp.vmworktimer.model.Mode;
import com.vanessameerkamp.vmworktimer.model.Project;
import com.vanessameerkamp.vmworktimer.model.State;
import com.vanessameerkamp.vmworktimer.model.WorkTime;
import com.vanessameerkamp.vmworktimer.view.activity.MainActivity;
import com.vanessameerkamp.vmworktimer.view.activity.ProjectDetailActivity;
import com.vanessameerkamp.vmworktimer.view.activity.WorkTimeDetailActivity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectDetailActivityListener implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    ProjectDetailActivity projectDetailActivity;
    Mode mode;

    WorkTimeListAdapter workTimeListAdapter;
    List<WorkTime> workTimeList = new ArrayList<>();
    List<Customer> customerList = new ArrayList<>();
    Project currentProject = null;

    ProjectDataSource projectDataSource;
    CustomerDataSource customerDataSource;
    WorkTimeDataSource workTimeDataSource;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ActionBar actionBar;

    public ProjectDetailActivityListener(ProjectDetailActivity projectDetailActivity) {
        this.projectDetailActivity = projectDetailActivity;

        projectDataSource = new ProjectDataSource(projectDetailActivity);
        customerDataSource = new CustomerDataSource(projectDetailActivity);
        workTimeDataSource = new WorkTimeDataSource(projectDetailActivity);

        sharedPreferences = projectDetailActivity.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        actionBar = projectDetailActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.project_detail_view_title);

        mode = projectDetailActivity.mode;

        Bundle bundle = projectDetailActivity.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getParcelable("project") != null) {
                currentProject = bundle.getParcelable("project");
            }
        }

        setListViewAdapter();

        setSpinnerAdapter();

        customizeGuiForMode();

        customizeButtonsForState();
    }

    public void onResume() {

        refreshListView();
    }

    public void onPrepareOptionsMenu(Menu menu) {

        if (sharedPreferences.contains("projectMode")) {
            mode = Mode.valueOf(sharedPreferences.getString("projectMode", "SHOW"));
        }

        switch (mode) {
            case CREATE:
                projectDetailActivity.getMenuInflater().inflate(R.menu.options_menu_save, menu);
                actionBar.setTitle(R.string.project_detail_view_create);
                break;
            case EDIT:
                projectDetailActivity.getMenuInflater().inflate(R.menu.options_menu_save, menu);
                actionBar.setTitle(R.string.project_detail_view_edit);
                break;
            case SHOW:
                projectDetailActivity.getMenuInflater().inflate(R.menu.options_menu_edit_delete, menu);
                actionBar.setTitle(R.string.project_detail_view_title);
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (mode == Mode.EDIT) {
                    editor.putString("projectMode", Mode.SHOW.toString());
                    editor.apply();
                    mode = Mode.SHOW;
                    projectDetailActivity.invalidateOptionsMenu();
                    customizeGuiForMode();
                } else {
                    projectDetailActivity.finish();
                }
                break;
            case R.id.omEdit:
                editor.putString("projectMode", Mode.EDIT.toString());
                editor.apply();
                mode = Mode.EDIT;
                projectDetailActivity.invalidateOptionsMenu();
                customizeGuiForMode();
                break;
            case R.id.omDelete:
                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(projectDetailActivity);
                alert.setTitle(R.string.delete_dialog_header)
                        .setMessage(R.string.confirm_project_deletion)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            projectDataSource.deleteProject(currentProject);
                            projectDetailActivity.finish();

                            List<WorkTime> workTimeList = workTimeDataSource.getProjectWorkTimes(currentProject.getId());
                            workTimeDataSource.deleteWorkTimes(workTimeList);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                break;
            case R.id.omSave:

                if (!projectDetailActivity.etProjectName.getText().toString().isEmpty()) {

                    if (projectDetailActivity.spinnerCustomer.getSelectedItem() != null) {

                        try {
                            if (!projectDetailActivity.etProjectDuration.getText().toString().equals("")) {
                                Duration.parseDuration(projectDetailActivity.etProjectDuration.getText().toString());
                            }

                            if (currentProject != null) {
                                projectDataSource.updateProject(getProjectInput());
                            } else {
                                currentProject = projectDataSource.insertProject(getProjectInput());
                            }
                            editor.putString("projectMode", Mode.SHOW.toString());
                            editor.apply();
                            mode = Mode.SHOW;
                            projectDetailActivity.invalidateOptionsMenu();
                            customizeGuiForMode();

                        } catch (Exception e) {
                            android.app.AlertDialog.Builder message = new AlertDialog.Builder(projectDetailActivity);

                            message.setTitle(R.string.wrong_number_input)
                                    .setMessage(R.string.msg_hour_input_format)
                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                        projectDetailActivity.etProjectDuration.setText("");
                                    })
                                    .show();
                        }
                    } else {
                        android.app.AlertDialog.Builder warning = new AlertDialog.Builder(projectDetailActivity);

                        warning.setTitle(R.string.title_please_create_customer)
                                .setMessage(R.string.msg_please_create_customer)
                                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                })
                                .show();
                    }
                } else {
                    android.app.AlertDialog.Builder warning = new AlertDialog.Builder(projectDetailActivity);

                    warning.setTitle(R.string.title_project_name_missing)
                            .setMessage(R.string.msg_project_name_missing)
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            })
                            .show();
                }

                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnStartProject:

                if (currentProject.getState() == 0) {
                    android.app.AlertDialog.Builder alert = new AlertDialog.Builder(projectDetailActivity);
                    alert.setTitle(R.string.really_start_project)
                            .setMessage(R.string.project_state_in_progress_not_reversable)
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                currentProject.setState(State.inProgress.ordinal());
                                currentProject.setStart(LocalDateTime.now());
                                projectDataSource.updateProject(currentProject);
                                projectDetailActivity.etProjectState.setText(State.inProgress.toString());

                                customizeButtonsForState();
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else {
                    currentProject.setState(State.inProgress.ordinal());
                    currentProject.setStart(LocalDateTime.now());
                    projectDataSource.updateProject(currentProject);
                    projectDetailActivity.etProjectState.setText(State.inProgress.toString());

                    customizeButtonsForState();
                }
                break;
            case R.id.btnEndProject:
                currentProject.setState(State.closed.ordinal());
                currentProject.setEnd(LocalDateTime.now());
                projectDataSource.updateProject(currentProject);
                projectDetailActivity.etProjectState.setText(State.closed.toString());

                customizeButtonsForState();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

        WorkTime selectedWorkTime = (WorkTime) parent.getItemAtPosition(position);

        Intent openDetailActivity = new Intent(projectDetailActivity, WorkTimeDetailActivity.class);

        editor.putString("workTimeMode", Mode.SHOW.toString());
        editor.apply();

        openDetailActivity.putExtra("workTime", selectedWorkTime);

        projectDetailActivity.startActivity(openDetailActivity);
    }

    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        WorkTime selectedWorkTime = workTimeList.get(info.position);

        switch (item.getItemId()) {
            case R.id.cmEdit:
                Intent openEditActivity = new Intent(projectDetailActivity, WorkTimeDetailActivity.class);

                editor.putString("workTimeMode", Mode.EDIT.toString());
                editor.apply();

                openEditActivity.putExtra("workTime", selectedWorkTime);

                projectDetailActivity.startActivity(openEditActivity);
                return true;
            case R.id.cmDelete:

                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(projectDetailActivity);

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.rgb(103,103,103));
        ((TextView) adapterView.getChildAt(0)).setTextSize(18);
    }

    private void setListViewAdapter() {

        if (currentProject != null) {
            workTimeList = workTimeDataSource.getProjectWorkTimes(currentProject.getId());
        }

        workTimeListAdapter = new WorkTimeListAdapter(projectDetailActivity, workTimeList);

        projectDetailActivity.lvProjectWorkTimes.setAdapter(workTimeListAdapter);
    }

    private void setSpinnerAdapter() {

        customerList = customerDataSource.getAllCustomers();

        Collections.sort(customerList, new CustomerComparator());

        ArrayAdapter<Customer> spinnerAdapter = new ArrayAdapter<>(projectDetailActivity, android.R.layout.simple_spinner_item, customerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectDetailActivity.spinnerCustomer.setAdapter(spinnerAdapter);
    }

    private void customizeGuiForMode() {
        switch (mode) {
            case CREATE:
                setInputFieldsEditable(true);
                showListViewAndButtons(false);
                break;
            case EDIT:
                setInputFieldsEditable(true);
                showProjectDetails();
                showListViewAndButtons(false);
                break;
            case SHOW:
                setInputFieldsEditable(false);
                showProjectDetails();
                showListViewAndButtons(true);
                break;
        }
    }

    private void customizeButtonsForState() {

        int state = 0;

        if (currentProject != null) {
            state = currentProject.getState();
        }

        switch (state) {
            case 0:
            case 2:
                projectDetailActivity.btnStartProject.setEnabled(true);
                projectDetailActivity.btnEndProject.setEnabled(false);
                break;
            case 1:
                projectDetailActivity.btnStartProject.setEnabled(false);
                projectDetailActivity.btnEndProject.setEnabled(true);
                break;
        }
    }

    private void showListViewAndButtons(boolean show) {

        if (show) {

            projectDetailActivity.txtvProjectWorkTimes.setVisibility(View.VISIBLE);
            projectDetailActivity.txtvProjectTotalDuration.setVisibility(View.VISIBLE);
            projectDetailActivity.lvProjectWorkTimes.setVisibility(View.VISIBLE);
            projectDetailActivity.btnStartProject.setVisibility(View.VISIBLE);
            projectDetailActivity.btnEndProject.setVisibility(View.VISIBLE);

            projectDetailActivity.textInputLayoutProjectState.setVisibility(View.VISIBLE);

        } else {
            projectDetailActivity.txtvProjectWorkTimes.setVisibility(View.INVISIBLE);
            projectDetailActivity.txtvProjectTotalDuration.setVisibility(View.INVISIBLE);
            projectDetailActivity.lvProjectWorkTimes.setVisibility(View.INVISIBLE);
            projectDetailActivity.btnStartProject.setVisibility(View.INVISIBLE);
            projectDetailActivity.btnEndProject.setVisibility(View.INVISIBLE);

            projectDetailActivity.textInputLayoutProjectState.setVisibility(View.INVISIBLE);
        }
    }

    private void setInputFieldsEditable(boolean editable) {
        projectDetailActivity.etProjectName.setEnabled(editable);
        projectDetailActivity.etProjectDuration.setEnabled(editable);
        projectDetailActivity.etProjectDescription.setEnabled(editable);

        projectDetailActivity.etProjectState.setEnabled(false);

        projectDetailActivity.spinnerCustomer.setEnabled(editable);
    }

    private void showProjectDetails() {

        projectDetailActivity.etProjectName.setText(currentProject.getName());

        projectDetailActivity.etProjectDuration.setText(Duration.convert(currentProject.getDuration()));

        State state = State.values()[currentProject.getState()];
        projectDetailActivity.etProjectState.setText(state.toString());

        projectDetailActivity.etProjectDescription.setText(currentProject.getDescription());

        Customer projectCustomer = customerDataSource.getCustomerById(currentProject.getCustomerId());
        int customerIndex = 0;

        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getId() == projectCustomer.getId()) {
                customerIndex = i;
            }
        }

        projectDetailActivity.spinnerCustomer.setSelection(customerIndex);
    }

    private Project getProjectInput() {

        int id = -1;
        LocalDateTime start = null;
        LocalDateTime end = null;
        int state = State.open.ordinal();

        if (currentProject != null) {
            id = currentProject.getId();
            start = currentProject.getStart();
            end = currentProject.getEnd();
            state = currentProject.getState();
        }

        String name = projectDetailActivity.etProjectName.getText().toString();
        int customerId = ((Customer) projectDetailActivity.spinnerCustomer.getSelectedItem()).getId();

        int duration = Duration.parseDuration(projectDetailActivity.etProjectDuration.getText().toString());

        String description = projectDetailActivity.etProjectDescription.getText().toString();

        if (id == -1) {
            currentProject = new Project(customerId, name, description, start, end, duration, state);
        } else {
            currentProject = new Project(id, customerId, name, description, start, end, duration, state);
        }

        return currentProject;
    }

    private void refreshListView() {

        workTimeList.clear();

        if (currentProject != null) {
            workTimeList.addAll(workTimeDataSource.getProjectWorkTimes(currentProject.getId()));
        }

        workTimeListAdapter.notifyDataSetChanged();

        int totalDuration = getTotalProjectDuration();
        projectDetailActivity.txtvProjectTotalDuration.setText(String.format("(Total: %s)", Duration.convert(totalDuration)));
    }

    private int getTotalProjectDuration() {

        int result = 0;

        for (WorkTime workTime : workTimeList) {
            result += workTime.getDuration();
        }

        return result;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
