package com.vanessameerkamp.vmworktimer.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.model.Customer;

import java.util.List;

public class CustomerListAdapter extends ArrayAdapter<Customer> {

    private List<Customer> customerList;
    private LayoutInflater layoutInflater;

    public CustomerListAdapter(@NonNull Context context, List<Customer> customerList) {

        super(context, R.layout.row_layout_customer, customerList);

        this.customerList = customerList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView;
        if (convertView == null) {
            rowView = layoutInflater.inflate(R.layout.row_layout_customer, parent, false);
        } else {
            rowView = convertView;
        }

        Customer currentCustomer = customerList.get(position);

        TextView txtvCustomerName = (TextView) rowView.findViewById(R.id.txtvCustomerName);
        TextView txtvCustomerPhone = (TextView) rowView.findViewById(R.id.txtvCustomerPhone);
        TextView txtvCustomerMail = (TextView) rowView.findViewById(R.id.txtvCustomerEmail);

        txtvCustomerName.setText(currentCustomer.getName());

        if (currentCustomer.getPhone().isEmpty()) {
            txtvCustomerPhone.setText(R.string.msg_no_phone);
        } else {
            txtvCustomerPhone.setText(currentCustomer.getPhone());
        }

        if (currentCustomer.getEmail().isEmpty()) {
            txtvCustomerMail.setText(R.string.msg_no_email);
        } else {
            txtvCustomerMail.setText(currentCustomer.getEmail());
        }

        return rowView;
    }
}
