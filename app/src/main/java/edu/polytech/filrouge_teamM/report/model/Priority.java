package edu.polytech.filrouge_teamM.report.model;

public enum Priority {
    LOW(1.5f),
    MEDIUM(3.0f),
    HIGH(4.0f),
    CRITICAL(5.0f);

    private final float rating;

    Priority(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }
}
