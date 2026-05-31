package edu.polytech.filrouge_teamM.report.factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.polytech.filrouge_teamM.report.observer.EmergencyService;
import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.report.model.HighwayIssue;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.model.Priority;
import edu.polytech.filrouge_teamM.report.model.Status;

public class HighwayFactory implements IssueFactory {

    @Override
    public Issue createIssue(String title, String description, String location, double latitude, double longitude, String category,
                             String size, String dangerLevel, String context, String picture) {
        Date now = new Date();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(now);
        String hour = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now);

        String actualDanger = (dangerLevel == null || dangerLevel.isEmpty()) ? "Élevé" : dangerLevel;
        Priority priority = getPriorityFromDanger(actualDanger);
        int imageResId = chooseImage(priority);
        float rating = priority.getRating();

        HighwayIssue issue = new HighwayIssue(title, description, location, latitude, longitude, date, hour,
                actualDanger, rating, imageResId,
                Status.REGISTERED, priority, category, size, context, picture);

        EmergencyService.getInstance().registerIssue(issue);
        return issue;
    }

    private int chooseImage(Priority priority) {
        if (priority == null) return R.drawable.eleve;
        switch (priority) {
            case LOW: return R.drawable.faible;
            case MEDIUM: return R.drawable.moyen;
            case HIGH:
            case CRITICAL: return R.drawable.eleve;
            default: return R.drawable.eleve;
        }
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
