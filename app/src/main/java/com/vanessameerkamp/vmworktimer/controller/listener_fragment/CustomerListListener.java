package com.vanessameerkamp.vmworktimer.controller.listener_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import com.vanessameerkamp.vmworktimer.controller.adapter.CustomerListAdapter;
import com.vanessameerkamp.vmworktimer.controller.database.CustomerDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.ProjectDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.WorkTimeDataSource;
import com.vanessameerkamp.vmworktimer.controller.helper.CustomerComparator;
import com.vanessameerkamp.vmworktimer.model.Customer;
import com.vanessameerkamp.vmworktimer.model.Mode;
import com.vanessameerkamp.vmworktimer.view.activity.CustomerDetailActivity;
import com.vanessameerkamp.vmworktimer.view.activity.MainActivity;
import com.vanessameerkamp.vmworktimer.view.fragment.CustomerListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerListListener implements View.OnClickListener, AdapterView.OnItemClickListener {

    CustomerListFragment customerListFragment;

    public List<Customer> customerList = new ArrayList<>();
    public CustomerListAdapter customerListAdapter;

    CustomerDataSource customerDataSource;
    ProjectDataSource projectDataSource;
    WorkTimeDataSource workTimeDataSource;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public CustomerListListener(CustomerListFragment customerListFragment) {

        this.customerListFragment = customerListFragment;

        customerDataSource = new CustomerDataSource(customerListFragment.getContext());
        projectDataSource = new ProjectDataSource(customerListFragment.getContext());
        workTimeDataSource = new WorkTimeDataSource(customerListFragment.getContext());

        sharedPreferences = customerListFragment.getActivity().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setListViewAdapter();
    }

    public void onResume() {
        refreshListView();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == customerListFragment.abtnAddCustomer.getId()) {
            Intent openCreateActivity = new Intent(customerListFragment.getActivity(), CustomerDetailActivity.class);

            editor.putString("customerMode", Mode.CREATE.toString());
            editor.apply();

            customerListFragment.startActivity(openCreateActivity);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

        Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);

        Intent openDetailActivity = new Intent(customerListFragment.getActivity(), CustomerDetailActivity.class);

        editor.putString("customerMode", Mode.SHOW.toString());
        editor.apply();

        openDetailActivity.putExtra("customer", selectedCustomer);

        customerListFragment.startActivity(openDetailActivity);
    }

    /*
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Customer selectedCustomer = customerList.get(info.position);

        switch (item.getItemId()) {
            case R.id.cmEdit:
                Intent openEditActivity = new Intent(customerListFragment.getActivity(), CustomerDetailActivity.class);

                editor.putString("customerMode", Mode.EDIT.toString());
                editor.apply();

                openEditActivity.putExtra("customer", selectedCustomer);

                customerListFragment.startActivity(openEditActivity);
                return true;
            case R.id.cmDelete:

                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(customerListFragment.getContext());

                alert.setTitle(R.string.delete_dialog_header)
                        .setMessage(R.string.confirm_customer_deletion)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            customerDataSource.deleteCustomer(selectedCustomer);
                            refreshListView();

                            List<Project> projectList = projectDataSource.getAllCustomerProjects(selectedCustomer.getId());
                            for (Project project : projectList) {
                                List<WorkTime> workTimeList = workTimeDataSource.getProjectWorkTimes(project.getId());
                                workTimeDataSource.deleteWorkTimes(workTimeList);
                            }
                            projectDataSource.deleteProjects(projectList);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                return true;
        }

        return false;
    }
     */

    private void setListViewAdapter() {

        customerList = customerDataSource.getAllCustomers();

        Collections.sort(customerList, new CustomerComparator());

        customerListAdapter = new CustomerListAdapter(customerListFragment.getContext(), customerList);

        customerListFragment.listViewCustomer.setAdapter(customerListAdapter);
    }

    private void refreshListView() {

        customerList.clear();

        customerList.addAll(customerDataSource.getAllCustomers());

        Collections.sort(customerList, new CustomerComparator());

        customerListAdapter.notifyDataSetChanged();
    }
}
