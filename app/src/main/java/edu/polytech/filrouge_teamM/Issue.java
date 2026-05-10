package edu.polytech.filrouge_teamM;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public abstract class Issue implements Parcelable, IssueObservable {
    private String title;
    private String description;
    private String location;
    private String date;
    private String hour;
    private String dangerLevel;
    private float rating;
    private int priorityImageResId;
    private String category;
    private String size;
    private String context;

    // Enums
    private Status status;
    private Priority priority;

    private transient List<IssueObserver> observers = new ArrayList<>();

    // Constructeur principal
    public Issue(String title, String description, String location, String date, String hour,
                 String dangerLevel, float rating, int priorityImageResId, Status status, Priority priority,
                 String category, String size, String context) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.hour = hour;
        this.dangerLevel = dangerLevel;
        this.rating = rating;
        this.priorityImageResId = priorityImageResId;
        this.status = status;
        this.priority = priority;
        this.category = category;
        this.size = size;
        this.context = context;
        this.observers = new ArrayList<>();
    }

    // Constructeur Parcelable
    protected Issue(Parcel in) {
        title = in.readString();
        description = in.readString();
        location = in.readString();
        date = in.readString();
        hour = in.readString();
        dangerLevel = in.readString();
        rating = in.readFloat();
        priorityImageResId = in.readInt();
        status = Status.valueOf(in.readString());
        priority = Priority.valueOf(in.readString());
        category = in.readString();
        size = in.readString();
        context = in.readString();
        this.observers = new ArrayList<>();
    }

    public abstract String getSafetyProtocol();

    // Observer
    @Override
    public void addObserver(IssueObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(IssueObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {}

    protected void notifyStatusChanged() {
        for (IssueObserver obs : observers) {
            obs.onStatusChanged(this);
        }
    }

    protected void notifyPriorityChanged() {
        for (IssueObserver obs : observers) {
            obs.onPriorityChanged(this);
        }
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getHour() { return hour; }
    public String getDangerLevel() { return dangerLevel; }
    public float getRating() { return status.getRating(); }
    public int getPriorityImageResId() { return priorityImageResId; }
    public String getCategory() { return category; }
    public String getSize() { return size; }
    public String getContext() { return context; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }

    // Pour compatibilité avec ancien code (String)
    public String getStatusString() { return status.name(); }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setDate(String date) { this.date = date; }
    public void setHour(String hour) { this.hour = hour; }
    public void setPriorityImageResId(int resId) { this.priorityImageResId = resId; }
    public void setCategory(String category) { this.category = category; }
    public void setSize(String size) { this.size = size; }
    public void setContext(String context) { this.context = context; }

    public void setRating(float rating) {
        this.rating = rating;
        Status newStatus = Status.fromRating(rating);
        if (newStatus != this.status) {
            setStatus(newStatus);
        }
    }

    public void setStatus(Status status) {
        if (this.status != status) {
            this.status = status;
            this.rating = status.getRating();
            notifyStatusChanged();
        }
    }

    public void setPriority(Priority priority) {
        if (this.priority != priority) {
            this.priority = priority;
            notifyPriorityChanged();
        }
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
        dest.writeString(status.name());
        dest.writeString(priority.name());
        dest.writeString(category);
        dest.writeString(size);
        dest.writeString(context);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}