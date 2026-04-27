package edu.polytech.filrouge_teamM.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HighDangerIssue extends Issue {

    public HighDangerIssue(String title, String description, String location, String date,
                           String hour, String dangerLevel, float rating,
                           int priorityImageResId, String status) {
        super(title, description, location, date, hour, dangerLevel, rating, priorityImageResId, status);
    }

    protected HighDangerIssue(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<HighDangerIssue> CREATOR =
            new Parcelable.Creator<HighDangerIssue>() {
                @Override
                public HighDangerIssue createFromParcel(Parcel in) {
                    return new HighDangerIssue(in);
                }

                @Override
                public HighDangerIssue[] newArray(int size) {
                    return new HighDangerIssue[size];
                }
            };

    @Override
    public String getSafetyProtocol() {
        return "Danger élevé : intervention prioritaire recommandée.";
    }
}