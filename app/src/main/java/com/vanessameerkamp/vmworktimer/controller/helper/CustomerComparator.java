package com.vanessameerkamp.vmworktimer.controller.helper;

import com.vanessameerkamp.vmworktimer.model.Customer;

import java.util.Comparator;

public class CustomerComparator implements Comparator<Customer> {

    @Override
    public int compare(Customer firstCustomer, Customer secondCustomer) {
        return firstCustomer.getName().compareTo(secondCustomer.getName());
    }
}
