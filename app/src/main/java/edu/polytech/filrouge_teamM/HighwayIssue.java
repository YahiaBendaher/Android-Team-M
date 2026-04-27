package edu.polytech.filrouge_teamM;

import android.os.Parcel;

public class HighwayIssue extends Issue {
    public HighwayIssue(String title, String description, String location, String date, String hour, String dangerLevel, float rating, int priorityImageResId, String status, String category, String size, String context) {
        super(title, description, location, date, hour, dangerLevel, rating, priorityImageResId, status, category, size, context);
    }

    protected HighwayIssue(Parcel in) {
        super(in);
    }

    @Override
    public String getSafetyProtocol() {
        return "Rester derrière la barrière de sécurité et ne pas intervenir sur la voie.";
    }

    public static final Creator<HighwayIssue> CREATOR = new Creator<HighwayIssue>() {
        @Override
        public HighwayIssue createFromParcel(Parcel in) {
            return new HighwayIssue(in);
        }

        @Override
        public HighwayIssue[] newArray(int size) {
            return new HighwayIssue[size];
        }
    };
}