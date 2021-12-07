package com.vanessameerkamp.vmworktimer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String contact;

    public Customer() {
    }

    public Customer(String name, String address, String phone, String email, String contact) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.contact = contact;
    }

    public Customer(int id, String name, String address, String phone, String email, String contact) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.contact = contact;
    }

    public Customer(Parcel in) {

        this.id = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.contact = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(contact);
    }

    @Override
    public String toString() {
        //Rückgabewert für Spinner
        return name;
    }
}
