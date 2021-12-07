package com.vanessameerkamp.vmworktimer.controller.listener_fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.database.ProjectDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.WorkTimeDataSource;
import com.vanessameerkamp.vmworktimer.controller.helper.WorkTimer;
import com.vanessameerkamp.vmworktimer.model.Duration;
import com.vanessameerkamp.vmworktimer.model.Project;
import com.vanessameerkamp.vmworktimer.model.WorkTime;
import com.vanessameerkamp.vmworktimer.view.fragment.WorkTimerFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkTimerListener implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public WorkTimerFragment workTimerFragment;

    List<Project> openProjects = new ArrayList<>();
    ArrayAdapter<Project> spinnerAdapter;

    WorkTimeDataSource workTimeDataSource;
    ProjectDataSource projectDataSource;

    WorkTime currentWorkTime;
    boolean initializeWorkTime = true;

    public Handler handler;
    public WorkTimer workTimer;
    private Thread workTimerThread;

    public WorkTimerListener(WorkTimerFragment workTimerFragment) {
        this.workTimerFragment = workTimerFragment;

        handler = new Handler();
        workTimer = new WorkTimer(this);
        workTimerThread = new Thread(workTimer);

        workTimeDataSource = new WorkTimeDataSource(workTimerFragment.getContext());
        projectDataSource = new ProjectDataSource(workTimerFragment.getContext());

        setSpinnerAdapter();

        workTimerFragment.btnStop.setEnabled(false);
    }

    public void onResume() {
        refreshSpinner();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSaveWorkDescription:
                if (!initializeWorkTime) {
                    currentWorkTime.setDescription(workTimerFragment.etWorkDescription.getText().toString());
                    workTimeDataSource.updateWorkTime(currentWorkTime);
                }

                workTimerFragment.etWorkDescription.clearFocus();

                InputMethodManager inputMethodManager = (InputMethodManager) workTimerFragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                break;
            case R.id.btnStartPauseTimer:
                if (workTimerFragment.spinnerProject.getSelectedItem() != null) {
                    if (initializeWorkTime) {
                        int projectId = ((Project) workTimerFragment.spinnerProject.getSelectedItem()).getId();
                        String description = workTimerFragment.etWorkDescription.getText().toString();
                        currentWorkTime = new WorkTime(projectId, description, LocalDateTime.now(), null, 0);
                        currentWorkTime = workTimeDataSource.insertWorkTime(currentWorkTime);
                        initializeWorkTime = false;
                    }
                    Thread.State state = workTimerThread.getState();
                    if (Thread.State.NEW.equals(state)) {
                        workTimerThread.start();
                        setButtonMsg("Pause");
                    } else {
                        if (workTimer.isPause()) {
                            workTimer.setPause(false);
                            setButtonMsg("Pause");
                        } else {
                            workTimer.setPause(true);
                            setButtonMsg("Weiter");
                        }
                    }
                    workTimerFragment.btnStop.setEnabled(true);
                } else {
                    android.app.AlertDialog.Builder alert = new AlertDialog.Builder(workTimerFragment.getContext());

                    alert.setTitle(R.string.title_please_create_project)
                            .setMessage(R.string.msg_please_create_project)
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> { })
                            .show();
                }
                break;
            case R.id.btnStop:
                if (!workTimer.isPause()) {
                    workTimer.setPause(true);
                }
                currentWorkTime.setEnd(LocalDateTime.now());
                currentWorkTime.setDuration(workTimer.getDuration());
                workTimeDataSource.updateWorkTime(currentWorkTime);
                initializeWorkTime = true;
                workTimer.reset();
                workTimerFragment.txtvTimer.setText(Duration.convert(workTimer.getDuration()));
                workTimerFragment.etWorkDescription.setText("");
                setButtonMsg("Start");
                workTimerFragment.btnStartPause.setEnabled(true);
                workTimerFragment.btnStop.setEnabled(false);
                break;
        }
    }

    public void onDestroy() {
        workTimerThread.interrupt();
        workTimerThread = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

        if (parent.getChildAt(0) != null) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.rgb(103,103,103));
            ((TextView) parent.getChildAt(0)).setTextSize(18);
        }

        if (!initializeWorkTime) {
            currentWorkTime.setProjectId(((Project) workTimerFragment.spinnerProject.getSelectedItem()).getId());
            workTimeDataSource.updateWorkTime(currentWorkTime);
        }
    }

    private void setSpinnerAdapter() {

        openProjects = projectDataSource.getInProgressProjects();

        Collections.reverse(openProjects);

        spinnerAdapter = new ArrayAdapter<>(workTimerFragment.getContext(), android.R.layout.simple_spinner_item, openProjects);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workTimerFragment.spinnerProject.setAdapter(spinnerAdapter);
    }

    private void refreshSpinner() {

        openProjects.clear();

        openProjects.addAll(projectDataSource.getInProgressProjects());

        Collections.reverse(openProjects);

        spinnerAdapter.notifyDataSetChanged();
    }

    private void setButtonMsg(final String message) {

        if (message == null) {
            workTimerFragment.btnStartPause.setText("");
        } else {
            workTimerFragment.btnStartPause.setText(message);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
