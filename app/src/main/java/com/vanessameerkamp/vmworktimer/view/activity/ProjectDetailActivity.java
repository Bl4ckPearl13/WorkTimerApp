package com.vanessameerkamp.vmworktimer.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.listener.ProjectDetailActivityListener;
import com.vanessameerkamp.vmworktimer.model.Mode;

public class ProjectDetailActivity extends AppCompatActivity {

    public Button btnStartProject, btnEndProject;
    public EditText etProjectName, etProjectDuration, etProjectState, etProjectDescription;
    public Spinner spinnerCustomer;
    public ListView lvProjectWorkTimes;
    public TextView txtvProjectWorkTimes, txtvProjectTotalDuration;
    public TextInputLayout textInputLayoutProjectState;

    ProjectDetailActivityListener projectListener;

    SharedPreferences sharedPreferences;

    public Mode mode = Mode.SHOW;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        sharedPreferences = this.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.contains("projectMode")) {
            mode = Mode.valueOf(sharedPreferences.getString("projectMode", "SHOW"));
        }

        initializeViews();

        projectListener = new ProjectDetailActivityListener(this);

        setViewListeners();

        registerForContextMenu(lvProjectWorkTimes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        projectListener.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        projectListener.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return projectListener.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_work_time, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return projectListener.onContextItemSelected(item);
    }

    private void initializeViews() {

        btnStartProject = findViewById(R.id.btnStartProject);
        btnEndProject = findViewById(R.id.btnEndProject);

        etProjectName = findViewById(R.id.etProjectName);
        etProjectDuration = findViewById(R.id.etProjectDuration);
        etProjectState = findViewById(R.id.etProjectState);
        etProjectDescription = findViewById(R.id.etProjectDescription);

        spinnerCustomer = findViewById(R.id.spinnerCustomer);

        lvProjectWorkTimes = findViewById(R.id.lvProjectWorkTimes);

        txtvProjectWorkTimes = findViewById(R.id.txtvProjectWorkTimes);
        txtvProjectTotalDuration = findViewById(R.id.txtvProjectTotalDuration);

        textInputLayoutProjectState = findViewById(R.id.textInputLayoutProjectState);
    }

    private void setViewListeners() {

        btnStartProject.setOnClickListener(projectListener);
        btnEndProject.setOnClickListener(projectListener);

        spinnerCustomer.setOnItemSelectedListener(projectListener);

        lvProjectWorkTimes.setOnItemClickListener(projectListener);
    }
}
