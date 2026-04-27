package edu.polytech.filrouge_teamM.repository;

import java.util.ArrayList;
import java.util.List;

import edu.polytech.filrouge_teamM.model.HighDangerIssue;
import edu.polytech.filrouge_teamM.model.LowDangerIssue;
import edu.polytech.filrouge_teamM.model.MediumDangerIssue;
import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.model.Issue;

public class IssueRepository {

    private static final ArrayList<Issue> ISSUES = new ArrayList<>();

    static {
        ISSUES.add(new LowDangerIssue(
                "Déchet",
                "Sacs poubelles abandonnés",
                "Rue de Rivoli, Paris",
                "04/04/2026",
                "15:51",
                "Faible",
                2.0f,
                R.drawable.faible,
                "Enregistré"
        ));

        ISSUES.add(new HighDangerIssue(
                "Obstacle",
                "Gros carton sur la chaussée",
                "Champs-Élysées, Paris",
                "03/04/2026",
                "14:30",
                "Élevé",
                4.0f,
                R.drawable.eleve,
                "Pris en charge"
        ));

        ISSUES.add(new MediumDangerIssue(
                "Branche",
                "Branche d'arbre cassée",
                "Boulevard Saint-Michel, Paris",
                "02/04/2026",
                "10:20",
                "Modéré",
                3.0f,
                R.drawable.moyen,
                "Enregistré"
        ));
    }

    public static void addIssue(Issue issue) {
        ISSUES.add(0, issue);
    }

    public static List<Issue> getIssues() {
        return new ArrayList<>(ISSUES);
    }
}