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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.listener.CustomerDetailActivityListener;
import com.vanessameerkamp.vmworktimer.model.Mode;

public class CustomerDetailActivity extends AppCompatActivity {

    public EditText etCustomerName, etCustomerAddress, etCustomerPhone, etCustomerEmail, etCustomerContact;
    public TextView txtvCustomerProjects;
    public Button btnAllCustomerProjects, btnOpenCustomerProjects, btnClosedCustomerProjects;
    public ListView lvCustomerProjects;

    CustomerDetailActivityListener customerListener;

    SharedPreferences sharedPreferences;

    public Mode mode = Mode.SHOW;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        sharedPreferences = this.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.contains("customerMode")) {
            mode = Mode.valueOf(sharedPreferences.getString("customerMode", "SHOW"));
        }

        initializeViews();

        customerListener = new CustomerDetailActivityListener(this);

        setViewListeners();

        registerForContextMenu(lvCustomerProjects);
    }

    @Override
    protected void onResume() {
        super.onResume();
        customerListener.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        customerListener.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return customerListener.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_project, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return customerListener.onContextItemSelected(item);
    }

    private void initializeViews() {

        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerAddress = findViewById(R.id.etCustomerAddress);
        etCustomerPhone = findViewById(R.id.etProjectName);
        etCustomerEmail = findViewById(R.id.etCustomerEmail);
        etCustomerContact = findViewById(R.id.etWorkTimeStart);

        txtvCustomerProjects = findViewById(R.id.txtvCustomerProjects);

        btnAllCustomerProjects = findViewById(R.id.btnAllCustomerProjects);
        btnOpenCustomerProjects = findViewById(R.id.btnOpenCustomerProjects);
        btnClosedCustomerProjects = findViewById(R.id.btnClosedCustomerProjects);

        lvCustomerProjects = findViewById(R.id.lvCustomerProjects);
    }

    private void setViewListeners() {

        btnAllCustomerProjects.setOnClickListener(customerListener);
        btnOpenCustomerProjects.setOnClickListener(customerListener);
        btnClosedCustomerProjects.setOnClickListener(customerListener);

        lvCustomerProjects.setOnItemClickListener(customerListener);
    }
}
