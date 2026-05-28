package edu.polytech.filrouge_teamM;

public interface IssueObserver {
    void onStatusChanged(Issue issue);
    void onPriorityChanged(Issue issue);
    void onPictureChanged(Issue issue);
}