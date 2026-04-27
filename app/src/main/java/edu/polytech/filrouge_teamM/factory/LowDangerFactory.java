package edu.polytech.filrouge_teamM.factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.polytech.filrouge_teamM.model.LowDangerIssue;
import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.model.Issue;

public class LowDangerFactory implements AccidentFactory {

    @Override
    public Issue createIssue(String title, String description) {
        Date now = new Date();

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(now);
        String hour = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(now);

        return new LowDangerIssue(
                title,
                description,
                "Localisation non renseignée",
                date,
                hour,
                "Faible",
                1.0f,
                R.drawable.faible,
                "Enregistré"
        );
    }
}