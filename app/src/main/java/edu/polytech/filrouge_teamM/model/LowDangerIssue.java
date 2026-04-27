package edu.polytech.filrouge_teamM.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LowDangerIssue extends Issue {

    public LowDangerIssue(String title, String description, String location, String date,
                          String hour, String dangerLevel, float rating,
                          int priorityImageResId, String status) {
        super(title, description, location, date, hour, dangerLevel, rating, priorityImageResId, status);
    }

    protected LowDangerIssue(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<LowDangerIssue> CREATOR =
            new Parcelable.Creator<LowDangerIssue>() {
                @Override
                public LowDangerIssue createFromParcel(Parcel in) {
                    return new LowDangerIssue(in);
                }

                @Override
                public LowDangerIssue[] newArray(int size) {
                    return new LowDangerIssue[size];
                }
            };

    @Override
    public String getSafetyProtocol() {
        return "Danger faible : signalement enregistré, intervention non urgente.";
    }
}