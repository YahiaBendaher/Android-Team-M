package edu.polytech.filrouge_teamM.model;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Issue implements Parcelable {

    private String title;
    private String description;
    private String location;
    private String date;
    private String hour;
    private String dangerLevel;
    private float rating;
    private int priorityImageResId;
    private String status;

    public Issue(String title, String description, String location, String date,
                 String hour, String dangerLevel, float rating,
                 int priorityImageResId, String status) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.hour = hour;
        this.dangerLevel = dangerLevel;
        this.rating = rating;
        this.priorityImageResId = priorityImageResId;
        this.status = status;
    }

    protected Issue(Parcel in) {
        title = in.readString();
        description = in.readString();
        location = in.readString();
        date = in.readString();
        hour = in.readString();
        dangerLevel = in.readString();
        rating = in.readFloat();
        priorityImageResId = in.readInt();
        status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeString(hour);
        dest.writeString(dangerLevel);
        dest.writeFloat(rating);
        dest.writeInt(priorityImageResId);
        dest.writeString(status);
    }

    public abstract String getSafetyProtocol();

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPriorityImageResId() {
        return priorityImageResId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}