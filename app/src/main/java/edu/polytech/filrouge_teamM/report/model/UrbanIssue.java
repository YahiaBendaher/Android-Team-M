package edu.polytech.filrouge_teamM.report.model;

import android.os.Parcel;

public class UrbanIssue extends Issue {
    public UrbanIssue(String title, String description, String location, double latitude, double longitude,
                      String date, String hour, String dangerLevel, float rating, int priorityImageResId,
                      Status status, Priority priority, String category, String size, String context, String picture) {
        super(title, description, location, latitude, longitude, date, hour, dangerLevel, rating,
                priorityImageResId, status, priority, category, size, context, picture);
    }

    protected UrbanIssue(Parcel in) {
        super(in);
    }

    @Override
    public String getSafetyProtocol() {
        return "Ralentir, sécuriser la zone et éviter de gêner les piétons et la circulation locale.";
    }

    public static final Creator<UrbanIssue> CREATOR = new Creator<UrbanIssue>() {
        @Override
        public UrbanIssue createFromParcel(Parcel in) {
            return new UrbanIssue(in);
        }

        @Override
        public UrbanIssue[] newArray(int size) {
            return new UrbanIssue[size];
        }
    };
}
