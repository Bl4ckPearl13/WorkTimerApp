package com.vanessameerkamp.vmworktimer.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vanessameerkamp.vmworktimer.controller.helper.SQL;
import com.vanessameerkamp.vmworktimer.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDataSource extends DataSource {

    //region Konstanten
    private String[] columns = {
            SQL.COLUMN_CUSTOMER_ID,
            SQL.COLUMN_CUSTOMER_NAME,
            SQL.COLUMN_CUSTOMER_ADDRESS,
            SQL.COLUMN_CUSTOMER_PHONE,
            SQL.COLUMN_CUSTOMER_EMAIL,
            SQL.COLUMN_CUSTOMER_CONTACT
    };
    //endregion

    //region Konstruktor
    public CustomerDataSource(Context context) {
        super(context);
    }
    //endregion

    //region CREATE
    public Customer insertCustomer(Customer customer) {

        ContentValues values = new ContentValues();
        values.put(SQL.COLUMN_CUSTOMER_NAME, customer.getName());
        values.put(SQL.COLUMN_CUSTOMER_ADDRESS, customer.getAddress());
        values.put(SQL.COLUMN_CUSTOMER_PHONE, customer.getPhone());
        values.put(SQL.COLUMN_CUSTOMER_EMAIL, customer.getEmail());
        values.put(SQL.COLUMN_CUSTOMER_CONTACT, customer.getContact());

        openWritable();

        long id = database.insert(SQL.TABLE_CUSTOMER, null, values);
        Cursor cursor = database.query(SQL.TABLE_CUSTOMER, columns, SQL.COLUMN_CUSTOMER_ID + " = " + id,
                null, null, null, null);

        cursor.moveToFirst();
        Customer currentCustomer = cursorToCustomer(cursor);

        cursor.close();
        closeDb();

        return currentCustomer;
    }
    //endregion

    //region READ all
    public List<Customer> getAllCustomers() {

        List<Customer> customerList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_ALL_CUSTOMERS, null);

        customerList = cursorToCustomerList(cursor);

        cursor.close();
        closeDb();

        return customerList;
    }
    //endregion

    //region READ by ID
    public Customer getCustomerById(int id) {

        Customer customer = new Customer();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_CUSTOMER_BY_ID, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            customer = cursorToCustomer(cursor);
        }

        cursor.close();
        closeDb();

        return customer;
    }
    //endregion

    //region UPDATE
    public void updateCustomer(Customer customer) {

        ContentValues values = new ContentValues();
        values.put(SQL.COLUMN_CUSTOMER_NAME, customer.getName());
        values.put(SQL.COLUMN_CUSTOMER_ADDRESS, customer.getAddress());
        values.put(SQL.COLUMN_CUSTOMER_PHONE, customer.getPhone());
        values.put(SQL.COLUMN_CUSTOMER_EMAIL, customer.getEmail());
        values.put(SQL.COLUMN_CUSTOMER_CONTACT, customer.getContact());

        openWritable();

        database.update(SQL.TABLE_CUSTOMER, values, SQL.COLUMN_CUSTOMER_ID + " = " + customer.getId(), null);

        closeDb();
    }
    //endregion

    //region DELETE
    public void deleteCustomer(Customer customer) {

        openWritable();

        database.delete(SQL.TABLE_CUSTOMER, SQL.COLUMN_CUSTOMER_ID + " = " + customer.getId(), null);

        closeDb();
    }
    //endregion

    //region Hilfsmethoden
    private Customer cursorToCustomer(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(SQL.COLUMN_CUSTOMER_ID);
        int idName = cursor.getColumnIndex(SQL.COLUMN_CUSTOMER_NAME);
        int idAddress = cursor.getColumnIndex(SQL.COLUMN_CUSTOMER_ADDRESS);
        int idPhone = cursor.getColumnIndex(SQL.COLUMN_CUSTOMER_PHONE);
        int idEmail = cursor.getColumnIndex(SQL.COLUMN_CUSTOMER_EMAIL);
        int idContact = cursor.getColumnIndex(SQL.COLUMN_CUSTOMER_CONTACT);

        int id = cursor.getInt(idIndex);
        String name = cursor.getString(idName);
        String address = cursor.getString(idAddress);
        String phone = cursor.getString(idPhone);
        String email = cursor.getString(idEmail);
        String contact = cursor.getString(idContact);

        return new Customer(id, name, address, phone, email, contact);
    }

    private List<Customer> cursorToCustomerList(Cursor cursor) {

        List<Customer> customerList = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                customerList.add(cursorToCustomer(cursor));
            } while (cursor.moveToNext());
        }

        return customerList;
    }
    //endregion
}
