package com.vanessameerkamp.vmworktimer.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.database.ProjectDataSource;
import com.vanessameerkamp.vmworktimer.controller.listener_fragment.WorkTimerListener;


public class WorkTimerFragment extends Fragment {

    public Spinner spinnerProject;
    public EditText etWorkDescription;
    public Button btnSaveWorkDescription, btnStartPause, btnStop;
    public TextView txtvTimer;

    WorkTimerListener workTimerListener;

    ProjectDataSource projectDataSource;

    public WorkTimerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_timer, container, false);

        spinnerProject = view.findViewById(R.id.spinnerProject);

        etWorkDescription = view.findViewById(R.id.etWorkDescription);

        btnSaveWorkDescription = view.findViewById(R.id.btnSaveWorkDescription);
        btnStartPause = view.findViewById(R.id.btnStartPauseTimer);
        btnStop = view.findViewById(R.id.btnStop);

        txtvTimer = view.findViewById(R.id.txtvTimer);

        workTimerListener = new WorkTimerListener(this);

        btnSaveWorkDescription.setOnClickListener(workTimerListener);
        btnStartPause.setOnClickListener(workTimerListener);
        btnStop.setOnClickListener(workTimerListener);

        spinnerProject.setOnItemSelectedListener(workTimerListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        workTimerListener.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workTimerListener.onDestroy();
    }
}