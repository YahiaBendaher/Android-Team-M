package edu.polytech.filrouge_teamM;

import android.util.Log;

public class EmergencyService implements IssueObserver {
    private static final String TAG = "teamM";
    private static EmergencyService instance;

    private EmergencyService() {}

    public static EmergencyService getInstance() {
        if (instance == null) instance = new EmergencyService();
        return instance;
    }

    public void registerIssue(Issue issue) {
        issue.addObserver(this);
    }

    @Override
    public void onStatusChanged(Issue issue) {
        Log.d(TAG, "Le nouveau statut de incident est : " + issue.getStatus().name());
    }

    @Override
    public void onPriorityChanged(Issue issue) {
        Log.d(TAG, "La priorité a changé : " + issue.getPriority().name());
    }
}