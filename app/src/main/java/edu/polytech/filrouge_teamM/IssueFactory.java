package edu.polytech.filrouge_teamM;

public interface IssueFactory {
    Issue createIssue(String title, String description, String location, String category, String size, String dangerLevel, String context);
}