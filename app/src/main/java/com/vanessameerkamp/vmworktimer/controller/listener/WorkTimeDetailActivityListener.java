package com.vanessameerkamp.vmworktimer.controller.listener;

import android.app.AlertDialog;
import android.content.Context;
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
import com.vanessameerkamp.vmworktimer.controller.database.ProjectDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.WorkTimeDataSource;
import com.vanessameerkamp.vmworktimer.model.Duration;
import com.vanessameerkamp.vmworktimer.model.Mode;
import com.vanessameerkamp.vmworktimer.model.Project;
import com.vanessameerkamp.vmworktimer.model.State;
import com.vanessameerkamp.vmworktimer.model.WorkTime;
import com.vanessameerkamp.vmworktimer.view.activity.MainActivity;
import com.vanessameerkamp.vmworktimer.view.activity.WorkTimeDetailActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkTimeDetailActivityListener implements AdapterView.OnItemSelectedListener {

    WorkTimeDetailActivity workTimeDetailActivity;
    Mode mode = Mode.SHOW;

    List<Project> projectList = new ArrayList<>();
    WorkTime currentWorkTime = null;

    ArrayAdapter<Project> spinnerAdapter;

    WorkTimeDataSource workTimeDataSource;
    ProjectDataSource projectDataSource;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    DateTimeFormatter formatter;

    ActionBar actionBar;

    boolean workTimeProjectClosed = false;

    public WorkTimeDetailActivityListener(WorkTimeDetailActivity workTimeDetailActivity) {
        this.workTimeDetailActivity = workTimeDetailActivity;

        workTimeDataSource = new WorkTimeDataSource(workTimeDetailActivity);
        projectDataSource = new ProjectDataSource(workTimeDetailActivity);

        sharedPreferences = workTimeDetailActivity.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        actionBar = workTimeDetailActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.work_time_detail_view_title);

        Bundle bundle = workTimeDetailActivity.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getParcelable("workTime") != null) {
                currentWorkTime = bundle.getParcelable("workTime");
            }
        }

        setSpinnerAdapter();

        customizeGuiForMode();
    }

    public void onResume() {

        refreshSpinner();
    }

    public void onPrepareOptionsMenu(Menu menu) {

        if (sharedPreferences.contains("workTimeMode")) {
            mode = Mode.valueOf(sharedPreferences.getString("workTimeMode", "SHOW"));
        }

        switch (mode) {
            case EDIT:
                workTimeDetailActivity.getMenuInflater().inflate(R.menu.options_menu_save, menu);
                actionBar.setTitle(R.string.work_time_detail_view_edit);
                break;
            case SHOW:
                workTimeDetailActivity.getMenuInflater().inflate(R.menu.options_menu_edit_delete, menu);
                actionBar.setTitle(R.string.work_time_detail_view_title);
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (mode == Mode.EDIT) {
                    editor.putString("workTimeMode", Mode.SHOW.toString());
                    editor.apply();
                    mode = Mode.SHOW;
                    workTimeDetailActivity.invalidateOptionsMenu();
                    customizeGuiForMode();
                } else {
                    workTimeDetailActivity.finish();
                }
                break;
            case R.id.omEdit:
                editor.putString("workTimeMode", Mode.EDIT.toString());
                editor.apply();
                mode = Mode.EDIT;
                workTimeDetailActivity.invalidateOptionsMenu();
                customizeGuiForMode();
                break;
            case R.id.omDelete:
                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(workTimeDetailActivity);
                alert.setTitle(R.string.delete_dialog_header)
                        .setMessage(R.string.confirm_work_time_deletion)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            workTimeDataSource.deleteWorkTime(currentWorkTime);
                            workTimeDetailActivity.finish();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                break;
            case R.id.omSave:

                try {
                    if (!workTimeDetailActivity.etWorkTimeStart.getText().toString().isEmpty())
                        LocalDateTime.parse(workTimeDetailActivity.etWorkTimeStart.getText().toString(), formatter);
                    if (!workTimeDetailActivity.etWorkTimeEnd.getText().toString().isEmpty())
                        LocalDateTime.parse(workTimeDetailActivity.etWorkTimeEnd.getText().toString(), formatter);
                    if (!workTimeDetailActivity.etWorkTimeDuration.getText().toString().isEmpty())
                        Duration.parseDuration(workTimeDetailActivity.etWorkTimeDuration.getText().toString());

                    if (currentWorkTime != null) {
                        workTimeDataSource.updateWorkTime(getWorkTimeInput());
                    } else {
                        currentWorkTime = workTimeDataSource.insertWorkTime(getWorkTimeInput());
                    }
                    editor.putString("workTimeMode", Mode.SHOW.toString());
                    editor.apply();
                    mode = Mode.SHOW;
                    workTimeDetailActivity.invalidateOptionsMenu();
                    customizeGuiForMode();

                } catch (Exception e) {
                    android.app.AlertDialog.Builder message = new AlertDialog.Builder(workTimeDetailActivity);

                    message.setTitle(R.string.wrong_date_time_input)
                            .setMessage(R.string.msg_date_time_input_format)
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            })
                            .show();
                }
                break;
        }
        return false;
    }

    private void setSpinnerAdapter() {

        projectList = projectDataSource.getOpenProjects();

        Collections.reverse(projectList);

        spinnerAdapter = new ArrayAdapter<>(workTimeDetailActivity, android.R.layout.simple_spinner_item, projectList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workTimeDetailActivity.spinnerWorkTimeProject.setAdapter(spinnerAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getChildAt(0) != null) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.rgb(103,103,103));
            ((TextView) adapterView.getChildAt(0)).setTextSize(18);
        }
    }

    private void customizeGuiForMode() {
        switch (mode) {
            case EDIT:
                setInputFieldsEditable(true);
                showWorkTimeDetails();
                break;
            case SHOW:
                setInputFieldsEditable(false);
                showWorkTimeDetails();
                break;
        }
    }

    private void setInputFieldsEditable(boolean editable) {
        workTimeDetailActivity.etWorkTimeDuration.setEnabled(editable);
        workTimeDetailActivity.etWorkTimeStart.setEnabled(editable);
        workTimeDetailActivity.etWorkTimeEnd.setEnabled(editable);
        workTimeDetailActivity.etWorkTimeDescription.setEnabled(editable);

        workTimeDetailActivity.spinnerWorkTimeProject.setEnabled(editable);
    }

    private void showWorkTimeDetails() {

        Project workTimeProject = projectDataSource.getProjectById(currentWorkTime.getProjectId());
        int projectIndex = -1;

        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getId() == workTimeProject.getId()) {
                projectIndex = i;
            }
        }

        if (projectIndex == -1) {
            workTimeDetailActivity.spinnerWorkTimeProject.setVisibility(View.INVISIBLE);
            workTimeProjectClosed = true;
        } else {
            workTimeDetailActivity.spinnerWorkTimeProject.setSelection(projectIndex);
        }

        workTimeDetailActivity.etWorkTimeDuration.setText(Duration.convert(currentWorkTime.getDuration()));

        workTimeDetailActivity.etWorkTimeStart.setText(currentWorkTime.getStart().format(formatter));

        if (currentWorkTime.getEnd() != null) {
            workTimeDetailActivity.etWorkTimeEnd.setText(currentWorkTime.getEnd().format(formatter));
        } else {
            workTimeDetailActivity.etWorkTimeEnd.setText("");
        }

        workTimeDetailActivity.etWorkTimeDescription.setText(currentWorkTime.getDescription());
    }

    private WorkTime getWorkTimeInput() {

        int id = -1;
        LocalDateTime start = null;
        LocalDateTime end = null;
        int state = State.open.ordinal();

        if (currentWorkTime != null) {
            id = currentWorkTime.getId();
        }

        int projectId = currentWorkTime.getProjectId();

        if (!workTimeProjectClosed) {
            projectId = ((Project) workTimeDetailActivity.spinnerWorkTimeProject.getSelectedItem()).getId();
        }

        int duration = Duration.parseDuration(workTimeDetailActivity.etWorkTimeDuration.getText().toString());

        start = LocalDateTime.parse(workTimeDetailActivity.etWorkTimeStart.getText().toString(), formatter);

        if (!workTimeDetailActivity.etWorkTimeEnd.getText().toString().isEmpty()) {
            end = LocalDateTime.parse(workTimeDetailActivity.etWorkTimeEnd.getText().toString(), formatter);
        }

        String description = workTimeDetailActivity.etWorkTimeDescription.getText().toString();

        if (id == -1) {
            currentWorkTime = new WorkTime(projectId, description, start, end, duration);
        } else {
            currentWorkTime = new WorkTime(id, projectId, description, start, end, duration);
        }

        return currentWorkTime;
    }

    private void refreshSpinner() {

        projectList.clear();

        projectList.addAll(projectDataSource.getOpenProjects());

        Collections.reverse(projectList);

        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
