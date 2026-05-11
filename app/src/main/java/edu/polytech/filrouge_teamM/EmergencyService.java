package edu.polytech.filrouge_teamM;

import android.util.Log;

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

    private String getFrenchStatus(Status status) {
        switch (status) {
            case REPORTED: return "Signalé";
            case CONFIRMED: return "Confirmé";
            case ON_SITE: return "Pris en charge";
            case CLEARING: return "En cours";
            case RESOLVED: return "Traité";
            default: return status.name();
        }
    }

    private String getFrenchPriority(Priority priority) {
        switch (priority) {
            case LOW: return "Faible";
            case MEDIUM: return "Modéré";
            case HIGH: return "Élevé";
            case CRITICAL: return "Critique";
            default: return priority.name();
        }
    }
}
