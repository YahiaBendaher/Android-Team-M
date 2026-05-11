package edu.polytech.filrouge_teamM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UrbanFactory implements IssueFactory {

    @Override
    public Issue createIssue(String title, String description, String location, String category,
                             String size, String dangerLevel, String context) {
        Date now = new Date();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(now);
        String hour = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now);

        String actualDanger = (dangerLevel == null || dangerLevel.isEmpty()) ? "Faible" : dangerLevel;
        int imageResId = chooseImage(actualDanger);

        // Déterminer la priorité en fonction du danger
        Priority priority = getPriorityFromDanger(actualDanger);

        UrbanIssue issue = new UrbanIssue(title, description, location, date, hour,
                actualDanger, 0.0f, imageResId,
                Status.REPORTED, priority, category, size, context);

        EmergencyService.getInstance().registerIssue(issue);
        return issue;
    }

    private int chooseImage(String dangerLevel) {
        if ("Faible".equalsIgnoreCase(dangerLevel)) return R.drawable.faible;
        else if ("Élevé".equalsIgnoreCase(dangerLevel)) return R.drawable.eleve;
        else return R.drawable.moyen;
    }

    private Priority getPriorityFromDanger(String dangerLevel) {
        switch (dangerLevel.toLowerCase()) {
            case "faible": return Priority.LOW;
            case "moyen": return Priority.MEDIUM;
            case "modéré": return Priority.MEDIUM;
            case "élevé": return Priority.HIGH;
            default: return Priority.MEDIUM;
        }
    }
}