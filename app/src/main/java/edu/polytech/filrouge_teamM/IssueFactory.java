package edu.polytech.filrouge_teamM;

public interface IssueFactory {
    Issue createIssue(String title, String description, String location, double latitude, double longitude, String category, String size, String dangerLevel, String context);
}
