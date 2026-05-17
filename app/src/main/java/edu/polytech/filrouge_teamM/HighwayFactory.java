package edu.polytech.filrouge_teamM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HighwayFactory implements IssueFactory {

    @Override
    public Issue createIssue(String title, String description, String location, double latitude, double longitude, String category,
                             String size, String dangerLevel, String context) {
        Date now = new Date();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(now);
        String hour = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now);

        String actualDanger = (dangerLevel == null || dangerLevel.isEmpty()) ? "Élevé" : dangerLevel;
        Priority priority = getPriorityFromDanger(actualDanger);

        HighwayIssue issue = new HighwayIssue(title, description, location, latitude, longitude, date, hour,
                actualDanger, 5.0f, R.drawable.eleve,
                Status.REPORTED, priority, category, size, context);

        EmergencyService.getInstance().registerIssue(issue);
        return issue;
    }

    private Priority getPriorityFromDanger(String dangerLevel) {
        if (dangerLevel == null) return Priority.HIGH;
        switch (dangerLevel.toLowerCase()) {
            case "faible": return Priority.LOW;
            case "modéré": return Priority.MEDIUM;
            case "élevé": return Priority.HIGH;
            case "critique": return Priority.CRITICAL;
            default: return Priority.HIGH;
        }
    }
}
