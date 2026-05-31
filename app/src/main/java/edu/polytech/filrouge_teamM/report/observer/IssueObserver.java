package edu.polytech.filrouge_teamM.report.observer;

import edu.polytech.filrouge_teamM.report.model.Issue;

public interface IssueObserver {
    void onStatusChanged(Issue issue);
    void onPriorityChanged(Issue issue);
    void onPictureChanged(Issue issue);
}