package edu.polytech.filrouge_teamM;

import java.util.ArrayList;
import java.util.List;

public class ReportMapModel {
    private static ReportMapModel instance;
    private final List<Issue> issues;

    private ReportMapModel() {
        issues = new ArrayList<>();
        initializeMocks();
    }

    public static ReportMapModel getInstance() {
        if (instance == null) {
            instance = new ReportMapModel();
        }
        return instance;
    }

    private void initializeMocks() {
        issues.add(new UrbanIssue("Obstacle sur la chaussée", "Devant Polytech", "930 Route des Colles, 06903 Sophia Antipolis",
                43.6150, 7.0710, "15/05/2024", "10:30", "Élevé", 4.0f, R.drawable.eleve,
                Status.REPORTED, Priority.HIGH, "Obstacle", "Grand", "Urbain"));

        issues.add(new UrbanIssue("Déchets encombrants", "Sacs poubelles sur le trottoir", "Route des Colles, Sophia Antipolis",
                43.6162, 7.0730, "15/05/2024", "11:00", "Faible", 2.0f, R.drawable.faible,
                Status.ON_SITE, Priority.LOW, "Déchet", "Moyen", "Urbain"));

        issues.add(new UrbanIssue("Trou dans la chaussée", "Nid de poule dangereux", "Campus SophiaTech, Sophia Antipolis",
                43.6142, 7.0695, "15/05/2024", "11:15", "Moyen", 3.0f, R.drawable.moyen,
                Status.CONFIRMED, Priority.MEDIUM, "Obstacle", "Moyen", "Urbain"));

        issues.add(new UrbanIssue("Verre brisé", "Bouteilles cassées sur la piste", "Valbonne Sophia Antipolis",
                43.6200, 7.0650, "15/05/2024", "12:00", "Élevé", 5.0f, R.drawable.eleve,
                Status.CLEARING, Priority.HIGH, "Verre/Débris", "Petit", "Urbain"));

        issues.add(new UrbanIssue("Branche cassée", "Entrave le passage", "Sophia Antipolis, Route des Dolines",
                43.6175, 7.0585, "15/05/2024", "14:20", "Moyen", 3.0f, R.drawable.moyen,
                Status.RESOLVED, Priority.MEDIUM, "Branche", "Moyen", "Urbain"));

        for (Issue issue : issues) {
            EmergencyService.getInstance().registerIssue(issue);
        }
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    public void updateReportLocation(Issue issue, double latitude, double longitude) {
        issue.setCoordinates(latitude, longitude);
    }

    public void updateReportLocation(Issue issue, double latitude, double longitude, String address) {
        issue.setCoordinates(latitude, longitude);
        if (address != null && !address.isEmpty()) {
            issue.setLocation(address);
        }
    }
}
