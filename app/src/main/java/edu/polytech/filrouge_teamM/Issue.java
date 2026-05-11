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
    private int priorityImageResId;
    private String category;
    private String size;
    private String context;
    private Status status;
    private Priority priority;
    private float priorityRating;
    private transient List<IssueObserver> observers;

    public Issue(String title, String description, String location, String date, String hour,
                 String dangerLevel, float rating, int priorityImageResId, Status status, Priority priority,
                 String category, String size, String context) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.hour = hour;
        this.priorityRating = rating;
        this.priorityImageResId = priorityImageResId;
        this.status = status;
        this.priority = priority;
        this.category = category;
        this.size = size;
        this.context = context;
        this.observers = new ArrayList<>();
    }

    protected Issue(Parcel in) {
        title = in.readString();
        description = in.readString();
        location = in.readString();
        date = in.readString();
        hour = in.readString();
        priorityImageResId = in.readInt();
        status = Status.valueOf(in.readString());
        priority = Priority.valueOf(in.readString());
        priorityRating = in.readFloat();
        category = in.readString();
        size = in.readString();
        context = in.readString();
        this.observers = new ArrayList<>();
    }

    public abstract String getSafetyProtocol();

    @Override
    public void addObserver(IssueObserver observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(IssueObserver observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
    }

    protected void notifyStatusChanged() {
        if (observers != null) {
            for (IssueObserver observer : observers) {
                observer.onStatusChanged(this);
            }
        }
    }

    protected void notifyPriorityChanged() {
        if (observers != null) {
            for (IssueObserver observer : observers) {
                observer.onPriorityChanged(this);
            }
        }
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getHour() { return hour; }
    public int getPriorityImageResId() { return priorityImageResId; }
    public String getCategory() { return category; }
    public String getSize() { return size; }
    public String getContext() { return context; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }

    public float getPriorityRating() {
        return priorityRating;
    }

    public void setPriorityRating(float priorityRating) {
        this.priorityRating = priorityRating;
        Priority newPriority = priorityFromRating(priorityRating);

        if (this.priority != newPriority) {
            this.priority = newPriority;
            updatePriorityImage();
            notifyPriorityChanged();
        } else {
            updatePriorityImage();
        }
    }

    private Priority priorityFromRating(float rating) {
        if (rating <= 2.0f) {
            return Priority.LOW;
        } else if (rating <= 3.0f) {
            return Priority.MEDIUM;
        } else if (rating <= 4.0f) {
            return Priority.HIGH;
        } else {
            return Priority.CRITICAL;
        }
    }

    private void updatePriorityImage() {
        if (this.priority != null) {
            switch (this.priority) {
                case LOW:
                    this.priorityImageResId = R.drawable.faible;
                    break;
                case MEDIUM:
                    this.priorityImageResId = R.drawable.moyen;
                    break;
                case HIGH:
                case CRITICAL:
                    this.priorityImageResId = R.drawable.eleve;
                    break;
            }
        }
    }

    public float getRating() {
        return priorityRating;
    }

    public void setRating(float rating) {
        setPriorityRating(rating);
    }

    public String getDangerLevel() {
        if (priority == null) return "Faible";
        switch (priority) {
            case LOW: return "Faible";
            case MEDIUM: return "Modéré";
            case HIGH: return "Élevé";
            case CRITICAL: return "Critique";
            default: return "Faible";
        }
    }

    public String getFrenchStatus() {
        if (status == null) return "Signalé";
        switch (status) {
            case REPORTED: return "Signalé";
            case CONFIRMED: return "Confirmé";
            case ON_SITE: return "Pris en charge";
            case CLEARING: return "En cours";
            case RESOLVED: return "Traité";
            default: return "Signalé";
        }
    }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setDate(String date) { this.date = date; }
    public void setHour(String hour) { this.hour = hour; }
    public void setPriorityImageResId(int priorityImageResId) { this.priorityImageResId = priorityImageResId; }
    public void setCategory(String category) { this.category = category; }
    public void setSize(String size) { this.size = size; }
    public void setContext(String context) { this.context = context; }

    public void setStatus(Status status) {
        if (this.status != status) {
            this.status = status;
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
        dest.writeInt(priorityImageResId);
        dest.writeString(status != null ? status.name() : Status.REPORTED.name());
        dest.writeString(priority != null ? priority.name() : Priority.LOW.name());
        dest.writeFloat(priorityRating);
        dest.writeString(category);
        dest.writeString(size);
        dest.writeString(context);
    }
}
