package com.vanessameerkamp.vmworktimer.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.listener_fragment.CustomerListListener;


public class CustomerListFragment extends Fragment {


    public ListView listViewCustomer;
    public FloatingActionButton abtnAddCustomer;

    CustomerListListener customerListListener;

    public CustomerListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);

        listViewCustomer = view.findViewById(R.id.listViewCustomer);
        abtnAddCustomer = view.findViewById(R.id.abtnAddCustomer);

        customerListListener = new CustomerListListener(this);

        abtnAddCustomer.setOnClickListener(customerListListener);

        listViewCustomer.setOnItemClickListener(customerListListener);

        //registerForContextMenu(listViewCustomer);

        return view;
    }

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_menu_customer, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        return customerListListener.onContextItemSelected(item);
    }
     */

    @Override
    public void onResume() {
        super.onResume();
        customerListListener.onResume();
    }
}