package edu.polytech.filrouge_teamM.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MediumDangerIssue extends Issue {

    public MediumDangerIssue(String title, String description, String location, String date,
                             String hour, String dangerLevel, float rating,
                             int priorityImageResId, String status) {
        super(title, description, location, date, hour, dangerLevel, rating, priorityImageResId, status);
    }

    protected MediumDangerIssue(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<MediumDangerIssue> CREATOR =
            new Parcelable.Creator<MediumDangerIssue>() {
                @Override
                public MediumDangerIssue createFromParcel(Parcel in) {
                    return new MediumDangerIssue(in);
                }

                @Override
                public MediumDangerIssue[] newArray(int size) {
                    return new MediumDangerIssue[size];
                }
            };

    @Override
    public String getSafetyProtocol() {
        return "Danger modéré : intervention à planifier selon la disponibilité des agents.";
    }
}