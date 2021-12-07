package com.vanessameerkamp.vmworktimer.controller.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.ActionBar;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.adapter.ProjectListAdapter;
import com.vanessameerkamp.vmworktimer.controller.database.CustomerDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.ProjectDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.WorkTimeDataSource;
import com.vanessameerkamp.vmworktimer.model.Customer;
import com.vanessameerkamp.vmworktimer.model.Mode;
import com.vanessameerkamp.vmworktimer.model.Project;
import com.vanessameerkamp.vmworktimer.model.WorkTime;
import com.vanessameerkamp.vmworktimer.view.activity.CustomerDetailActivity;
import com.vanessameerkamp.vmworktimer.view.activity.MainActivity;
import com.vanessameerkamp.vmworktimer.view.activity.ProjectDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetailActivityListener implements View.OnClickListener, AdapterView.OnItemClickListener {

    CustomerDetailActivity customerDetailActivity;
    Mode mode;

    ProjectListAdapter projectListAdapter;
    List<Project> projectList = new ArrayList<>();
    Customer currentCustomer = null;

    CustomerDataSource customerDataSource;
    ProjectDataSource projectDataSource;
    WorkTimeDataSource workTimeDataSource;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ActionBar actionBar;

    public CustomerDetailActivityListener(CustomerDetailActivity customerDetailActivity) {
        this.customerDetailActivity = customerDetailActivity;

        customerDataSource = new CustomerDataSource(customerDetailActivity);
        projectDataSource = new ProjectDataSource(customerDetailActivity);
        workTimeDataSource = new WorkTimeDataSource(customerDetailActivity);

        sharedPreferences = customerDetailActivity.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        actionBar = customerDetailActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.customer_detail_view_title);

        mode = customerDetailActivity.mode;

        Bundle bundle = customerDetailActivity.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getParcelable("customer") != null) {
                currentCustomer = (Customer) bundle.getParcelable("customer");
            }
        }

        setListViewAdapter();

        customizeGuiForMode();

        customerDetailActivity.btnAllCustomerProjects.setEnabled(false);
    }

    public void onResume() {

        refreshListView();
        customerDetailActivity.btnAllCustomerProjects.setEnabled(false);
    }

    public void onPrepareOptionsMenu(Menu menu) {

        if (sharedPreferences.contains("customerMode")) {
            mode = Mode.valueOf(sharedPreferences.getString("customerMode", "SHOW"));
        }

        switch (mode) {
            case CREATE:
                customerDetailActivity.getMenuInflater().inflate(R.menu.options_menu_save, menu);
                actionBar.setTitle(R.string.customer_detail_view_create);
                break;
            case EDIT:
                customerDetailActivity.getMenuInflater().inflate(R.menu.options_menu_save, menu);
                actionBar.setTitle(R.string.customer_detail_view_edit);
                break;
            case SHOW:
                customerDetailActivity.getMenuInflater().inflate(R.menu.options_menu_edit_delete, menu);
                actionBar.setTitle(R.string.customer_detail_view_title);
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (mode == Mode.EDIT) {
                    editor.putString("customerMode", Mode.SHOW.toString());
                    editor.apply();
                    mode = Mode.SHOW;
                    customerDetailActivity.invalidateOptionsMenu();
                    customizeGuiForMode();
                } else {
                    customerDetailActivity.finish();
                }
                break;
            case R.id.omEdit:
                editor.putString("customerMode", Mode.EDIT.toString());
                editor.apply();
                mode = Mode.EDIT;
                customerDetailActivity.invalidateOptionsMenu();
                customizeGuiForMode();
                break;
            case R.id.omDelete:
                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(customerDetailActivity);
                alert.setTitle(R.string.delete_dialog_header)
                        .setMessage(R.string.confirm_customer_deletion)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            customerDataSource.deleteCustomer(currentCustomer);
                            customerDetailActivity.finish();

                            List<Project> projectList = projectDataSource.getAllCustomerProjects(currentCustomer.getId());
                            for (Project project : projectList) {
                                List<WorkTime> workTimeList = workTimeDataSource.getProjectWorkTimes(project.getId());
                                workTimeDataSource.deleteWorkTimes(workTimeList);
                            }
                            projectDataSource.deleteProjects(projectList);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                break;
            case R.id.omSave:
                if (!customerDetailActivity.etCustomerName.getText().toString().isEmpty()) {

                    if (currentCustomer != null) {
                        customerDataSource.updateCustomer(getCustomerInput());
                    } else {
                        currentCustomer = customerDataSource.insertCustomer(getCustomerInput());
                    }
                    editor.putString("customerMode", Mode.SHOW.toString());
                    editor.apply();
                    mode = Mode.SHOW;
                    customerDetailActivity.invalidateOptionsMenu();
                    customizeGuiForMode();
                } else {
                    android.app.AlertDialog.Builder warning = new AlertDialog.Builder(customerDetailActivity);

                    warning.setTitle(R.string.title_customer_name_missing)
                            .setMessage(R.string.msg_customer_name_missing)
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> { })
                            .show();
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnAllCustomerProjects:
                refreshListView(projectDataSource.getAllCustomerProjects(currentCustomer.getId()));
                customerDetailActivity.btnAllCustomerProjects.setEnabled(false);
                customerDetailActivity.btnOpenCustomerProjects.setEnabled(true);
                customerDetailActivity.btnClosedCustomerProjects.setEnabled(true);
                break;
            case R.id.btnOpenCustomerProjects:
                refreshListView(projectDataSource.getOpenCustomerProjects(currentCustomer.getId()));
                customerDetailActivity.btnAllCustomerProjects.setEnabled(true);
                customerDetailActivity.btnOpenCustomerProjects.setEnabled(false);
                customerDetailActivity.btnClosedCustomerProjects.setEnabled(true);
                break;
            case R.id.btnClosedCustomerProjects:
                refreshListView(projectDataSource.getClosedCustomerProjects(currentCustomer.getId()));
                customerDetailActivity.btnAllCustomerProjects.setEnabled(true);
                customerDetailActivity.btnOpenCustomerProjects.setEnabled(true);
                customerDetailActivity.btnClosedCustomerProjects.setEnabled(false);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

        Project selectedProject = (Project) parent.getItemAtPosition(position);

        Intent openDetailActivity = new Intent(customerDetailActivity, ProjectDetailActivity.class);

        editor.putString("projectMode", Mode.SHOW.toString());
        editor.apply();

        openDetailActivity.putExtra("project", selectedProject);

        customerDetailActivity.startActivity(openDetailActivity);
    }

    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Project selectedProject = projectList.get(info.position);

        switch (item.getItemId()) {
            case R.id.cmEdit:
                Intent openEditActivity = new Intent(customerDetailActivity, CustomerDetailActivity.class);

                editor.putString("projectMode", Mode.EDIT.toString());
                editor.apply();

                openEditActivity.putExtra("project", selectedProject);

                customerDetailActivity.startActivity(openEditActivity);
                return true;
            case R.id.cmDelete:

                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(customerDetailActivity);

                alert.setTitle(R.string.delete_dialog_header)
                        .setMessage(R.string.confirm_project_deletion)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            projectDataSource.deleteProject(selectedProject);
                            refreshListView();

                            List<WorkTime> workTimeList = workTimeDataSource.getProjectWorkTimes(selectedProject.getId());
                            workTimeDataSource.deleteWorkTimes(workTimeList);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                return true;
        }
        return false;
    }

    private void setListViewAdapter() {

        if (currentCustomer != null) {
            projectList = projectDataSource.getAllCustomerProjects(currentCustomer.getId());
        }

        projectListAdapter = new ProjectListAdapter(customerDetailActivity, projectList);

        customerDetailActivity.lvCustomerProjects.setAdapter(projectListAdapter);
    }

    private void customizeGuiForMode() {
        switch (mode) {
            case CREATE:
                setInputFieldsEditable(true);
                showListView(false);
                break;
            case EDIT:
                setInputFieldsEditable(true);
                showCustomerDetails();
                showListView(false);
                break;
            case SHOW:
                setInputFieldsEditable(false);
                showCustomerDetails();
                showListView(true);
                break;
        }
    }

    private void showListView(boolean show) {
        if (show) {
            customerDetailActivity.txtvCustomerProjects.setVisibility(View.VISIBLE);
            customerDetailActivity.btnAllCustomerProjects.setVisibility(View.VISIBLE);
            customerDetailActivity.btnOpenCustomerProjects.setVisibility(View.VISIBLE);
            customerDetailActivity.btnClosedCustomerProjects.setVisibility(View.VISIBLE);
            customerDetailActivity.lvCustomerProjects.setVisibility(View.VISIBLE);
        } else {
            customerDetailActivity.txtvCustomerProjects.setVisibility(View.INVISIBLE);
            customerDetailActivity.btnAllCustomerProjects.setVisibility(View.INVISIBLE);
            customerDetailActivity.btnOpenCustomerProjects.setVisibility(View.INVISIBLE);
            customerDetailActivity.btnClosedCustomerProjects.setVisibility(View.INVISIBLE);
            customerDetailActivity.lvCustomerProjects.setVisibility(View.INVISIBLE);
        }
    }

    private void setInputFieldsEditable(boolean editable) {
        customerDetailActivity.etCustomerName.setEnabled(editable);
        customerDetailActivity.etCustomerAddress.setEnabled(editable);
        customerDetailActivity.etCustomerPhone.setEnabled(editable);
        customerDetailActivity.etCustomerEmail.setEnabled(editable);
        customerDetailActivity.etCustomerContact.setEnabled(editable);
    }

    private void showCustomerDetails() {
        customerDetailActivity.etCustomerName.setText(currentCustomer.getName());
        customerDetailActivity.etCustomerAddress.setText(currentCustomer.getAddress());
        customerDetailActivity.etCustomerPhone.setText(currentCustomer.getPhone());
        customerDetailActivity.etCustomerEmail.setText(currentCustomer.getEmail());
        customerDetailActivity.etCustomerContact.setText(currentCustomer.getContact());
    }

    private Customer getCustomerInput() {

        int id = -1;

        if (currentCustomer != null) {
            id = currentCustomer.getId();
        }

        String name = customerDetailActivity.etCustomerName.getText().toString();
        String address = customerDetailActivity.etCustomerAddress.getText().toString();
        String phone = customerDetailActivity.etCustomerPhone.getText().toString();
        String email = customerDetailActivity.etCustomerEmail.getText().toString();
        String contact = customerDetailActivity.etCustomerContact.getText().toString();

        if (id == -1) {
            currentCustomer = new Customer(name, address, phone, email, contact);
        } else {
            currentCustomer = new Customer(id, name, address, phone, email, contact);
        }

        return currentCustomer;
    }

    private void refreshListView() {

        projectList.clear();

        if (currentCustomer != null) {
            projectList.addAll(projectDataSource.getAllCustomerProjects(currentCustomer.getId()));
        }

        if (projectList.size() > 0) {
            projectListAdapter.notifyDataSetChanged();
        }
    }

    private void refreshListView(List<Project> projects) {

        projectList.clear();

        projectList.addAll(projects);

        if (projectList.size() > 0) {
            projectListAdapter.notifyDataSetChanged();
        }
    }
}
