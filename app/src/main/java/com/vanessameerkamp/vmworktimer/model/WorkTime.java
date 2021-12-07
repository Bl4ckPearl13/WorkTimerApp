package com.vanessameerkamp.vmworktimer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class WorkTime implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        public WorkTime createFromParcel(Parcel in) {
            return new WorkTime(in);
        }

        public WorkTime[] newArray(int size) {
            return new WorkTime[size];
        }
    };

    private int id;
    private int projectId;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private int duration;

    public WorkTime() {
    }

    public WorkTime(int projectId, String description, LocalDateTime start, LocalDateTime end, int duration) {
        this.projectId = projectId;
        this.description = description;
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    public WorkTime(int id, int projectId, String description, LocalDateTime start, LocalDateTime end, int duration) {
        this.id = id;
        this.projectId = projectId;
        this.description = description;
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    public WorkTime(Parcel in) {
        this.id = in.readInt();
        this.projectId = in.readInt();
        this.description = in.readString();
        this.start = LocalDateTime.parse(in.readString());

        String endAsString = in.readString();
        if (!endAsString.equals("null")) {
            this.end = LocalDateTime.parse(endAsString);
        }

        this.duration = in.readInt();
    }

    public int getId() {
        return id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeInt(projectId);
        parcel.writeString(description);
        parcel.writeString(start.toString());
        if (end == null) {
            parcel.writeString("null");
        } else {
            parcel.writeString(end.toString());
        }
        parcel.writeInt(duration);
    }

    @Override
    public String toString() {
        return "WorkTime{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", start=" + start +
                ", end=" + end +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                '}';
    }
}
