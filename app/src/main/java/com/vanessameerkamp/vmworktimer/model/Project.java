package com.vanessameerkamp.vmworktimer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class Project implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    private int id;
    private int customerId;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private int duration;
    private int state;

    public Project() {
    }

    public Project(int customerId, String name, String description, LocalDateTime start, LocalDateTime end, int duration, int state) {
        this.customerId = customerId;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.state = state;
    }

    public Project(int id, int customerId, String name, String description, LocalDateTime start, LocalDateTime end, int duration, int state) {
        this.id = id;
        this.customerId = customerId;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.state = state;
    }

    public Project(Parcel in) {
        this.id = in.readInt();
        this.customerId = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        String startAsString = in.readString();
        if (!startAsString.equals("null")) {
            this.start = LocalDateTime.parse(startAsString);
        }
        String endAsString = in.readString();
        if (!endAsString.equals("null")) {
            this.end = LocalDateTime.parse(endAsString);
        }
        this.duration = in.readInt();
        this.state = in.readInt();
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeInt(customerId);
        parcel.writeString(name);
        parcel.writeString(description);
        if (start == null) {
            parcel.writeString("null");
        } else {
            parcel.writeString(start.toString());
        }
        if (end == null) {
            parcel.writeString("null");
        } else {
            parcel.writeString(end.toString());
        }
        parcel.writeInt(duration);
        parcel.writeInt(state);
    }

    @Override
    public String toString() {
        //Rückgabewert für Spinner
        return name;
    }
}
