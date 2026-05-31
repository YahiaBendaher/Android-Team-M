package edu.polytech.filrouge_teamM.map;

import java.util.ArrayList;
import java.util.List;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.model.Priority;
import edu.polytech.filrouge_teamM.report.model.Status;
import edu.polytech.filrouge_teamM.report.model.UrbanIssue;
import edu.polytech.filrouge_teamM.report.observer.EmergencyService;

public class ReportMapModel {
    private static ReportMapModel instance;
    private final List<Issue> issues;
    private final List<Issue> myIssues;

    private ReportMapModel() {
        issues = new ArrayList<>();
        myIssues = new ArrayList<>();
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
                Status.REGISTERED, Priority.HIGH, "Obstacle", "Grand", "Urbain", null));

        issues.add(new UrbanIssue("Déchets encombrants", "Sacs poubelles sur le trottoir", "Route des Colles, Sophia Antipolis",
                43.6162, 7.0730, "15/05/2024", "11:00", "Faible", 1.5f, R.drawable.faible,
                Status.TAKEN_IN_CHARGE, Priority.LOW, "Déchet", "Moyen", "Urbain", null));

        issues.add(new UrbanIssue("Trou dans la chaussée", "Nid de poule dangereux", "Campus SophiaTech, Sophia Antipolis",
                43.6142, 7.0695, "15/05/2024", "11:15", "Modéré", 3.0f, R.drawable.moyen,
                Status.REGISTERED, Priority.MEDIUM, "Obstacle", "Moyen", "Urbain", null));

        issues.add(new UrbanIssue("Verre brisé", "Bouteilles cassées sur la piste", "Valbonne Sophia Antipolis",
                43.6200, 7.0650, "15/05/2024", "12:00", "Élevé", 4.0f, R.drawable.eleve,
                Status.IN_PROGRESS, Priority.HIGH, "Verre/Débris", "Petit", "Urbain", null));

        issues.add(new UrbanIssue("Branche cassée", "Entrave le passage", "Sophia Antipolis, Route des Dolines",
                43.6175, 7.0585, "15/05/2024", "14:20", "Modéré", 3.0f, R.drawable.moyen,
                Status.RESOLVED, Priority.MEDIUM, "Branche", "Moyen", "Urbain", null));

        for (Issue issue : issues) {
            EmergencyService.getInstance().registerIssue(issue);
        }
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public List<Issue> getMyIssues() {
        return myIssues;
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
        myIssues.add(issue);
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
