package edu.polytech.filrouge_teamM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UrbanFactory implements IssueFactory {

    @Override
    public Issue createIssue(String title, String description, String location, double latitude, double longitude, String category,
                             String size, String dangerLevel, String context) {
        Date now = new Date();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(now);
        String hour = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now);

        String actualDanger = (dangerLevel == null || dangerLevel.isEmpty()) ? "Faible" : dangerLevel;
        Priority priority = getPriorityFromDanger(actualDanger);
        int imageResId = chooseImage(priority);

        UrbanIssue issue = new UrbanIssue(title, description, location, latitude, longitude, date, hour,
                actualDanger, 0.0f, imageResId,
                Status.REPORTED, priority, category, size, context);

        EmergencyService.getInstance().registerIssue(issue);
        return issue;
    }

    private int chooseImage(Priority priority) {
        switch (priority) {
            case LOW: return R.drawable.faible;
            case HIGH:
            case CRITICAL: return R.drawable.eleve;
            default: return R.drawable.moyen;
        }
    }

    private Priority getPriorityFromDanger(String dangerLevel) {
        if (dangerLevel == null) return Priority.MEDIUM;
        switch (dangerLevel.toLowerCase()) {
            case "faible": return Priority.LOW;
            case "élevé": return Priority.HIGH;
            case "critique": return Priority.CRITICAL;
            default: return Priority.MEDIUM;
        }
    }
}
