package com.vanessameerkamp.vmworktimer.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.listener.WorkTimeDetailActivityListener;
import com.vanessameerkamp.vmworktimer.model.Mode;

public class WorkTimeDetailActivity extends AppCompatActivity {

    public EditText etWorkTimeDuration, etWorkTimeStart, etWorkTimeEnd, etWorkTimeDescription;

    public Spinner spinnerWorkTimeProject;

    WorkTimeDetailActivityListener workTimeListener;

    SharedPreferences sharedPreferences;

    public Mode mode = Mode.SHOW;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time);

        sharedPreferences = this.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.contains("workTimeMode")) {
            mode = Mode.valueOf(sharedPreferences.getString("workTimeMode", "SHOW"));
        }

        initializeViews();

        workTimeListener = new WorkTimeDetailActivityListener(this);

        setViewListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        workTimeListener.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        workTimeListener.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return workTimeListener.onOptionsItemSelected(item);
    }

    private void initializeViews() {

        etWorkTimeDuration = findViewById(R.id.etWorkTimeDuration);
        etWorkTimeStart = findViewById(R.id.etWorkTimeStart);
        etWorkTimeEnd = findViewById(R.id.etWorkTimeEnd);
        etWorkTimeDescription = findViewById(R.id.etWorkTimeDescription);

        spinnerWorkTimeProject = findViewById(R.id.spinnerWorkTimeProject);
    }

    private void setViewListeners() {
        spinnerWorkTimeProject.setOnItemSelectedListener(workTimeListener);
    }
}
