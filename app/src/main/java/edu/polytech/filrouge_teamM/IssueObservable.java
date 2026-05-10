package edu.polytech.filrouge_teamM;

public interface IssueObservable {
    void addObserver(IssueObserver observer);
    void removeObserver(IssueObserver observer);
    void notifyObservers();
}