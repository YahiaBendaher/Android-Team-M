package edu.polytech.filrouge_teamM.report.observer;

import android.util.Log;

import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.model.Priority;
import edu.polytech.filrouge_teamM.report.model.Status;

public class EmergencyService implements IssueObserver {
    private static EmergencyService instance;

    private EmergencyService() {
    }

    public static EmergencyService getInstance() {
        if (instance == null) {
            instance = new EmergencyService();
        }
        return instance;
    }

    public void registerIssue(Issue issue) {
        issue.addObserver(this);
    }

    @Override
    public void onStatusChanged(Issue issue) {
        String statusLabel = getFrenchStatus(issue.getStatus());
        String alert = "Incident '" + issue.getTitle() + "' : Statut modifié en '" + statusLabel + "'";
        Log.d("EmergencyService", alert);
    }

    @Override
    public void onPriorityChanged(Issue issue) {
        String priorityLabel = getFrenchPriority(issue.getPriority());
        String alert = "Incident '" + issue.getTitle() + "' : Priorité modifiée en '" + priorityLabel + "'";
        Log.d("EmergencyService", alert);
    }

    @Override
    public void onPictureChanged(Issue issue) {
        String alert = "Incident '" + issue.getTitle() + "' : Photo mise à jour";
        Log.d("EmergencyService", alert);
    }

    private String getFrenchStatus(Status status) {
        if (status == null) return "Enregistré";
        switch (status) {
            case REGISTERED: return "Enregistré";
            case TAKEN_IN_CHARGE: return "Pris en charge";
            case IN_PROGRESS: return "En cours";
            case RESOLVED: return "Traité";
            default: return status.name();
        }
    }

    private String getFrenchPriority(Priority priority) {
        if (priority == null) return "Faible";
        switch (priority) {
            case LOW: return "Faible";
            case MEDIUM: return "Modéré";
            case HIGH: return "Élevé";
            case CRITICAL: return "Critique";
            default: return priority.name();
        }
    }
}
