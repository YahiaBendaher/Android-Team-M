package edu.polytech.filrouge_teamM.factory;

import edu.polytech.filrouge_teamM.model.Issue;

public interface AccidentFactory {
    Issue createIssue(String title, String description);
}