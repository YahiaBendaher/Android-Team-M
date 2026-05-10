package edu.polytech.filrouge_teamM;

public enum Status {
    REPORTED(1.0f),
    CONFIRMED(2.0f),
    ON_SITE(3.0f),
    CLEARING(4.0f),
    RESOLVED(5.0f);

    private final float rating;

    Status(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public static Status fromRating(float rating) {
        for (Status s : values()) {
            if (Math.abs(s.rating - rating) < 0.1f) {
                return s;
            }
        }
        return REPORTED;
    }
}