package edu.polytech.filrouge_teamM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HighwayFactory implements IssueFactory {

    @Override
    public Issue createIssue(String title, String description, String location, String category, String size, String dangerLevel, String context) {
        Date now = new Date();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(now);
        String hour = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now);
        
        String actualDanger = "Élevé";
        int imageResId = R.drawable.eleve;

        return new HighwayIssue(title, description, location, date, hour, actualDanger, 0.0f, imageResId, "Enregistré", category, size, context);
    }
}