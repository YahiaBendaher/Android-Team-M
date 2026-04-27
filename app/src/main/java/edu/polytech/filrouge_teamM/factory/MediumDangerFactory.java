package edu.polytech.filrouge_teamM.factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.polytech.filrouge_teamM.model.MediumDangerIssue;
import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.model.Issue;

public class MediumDangerFactory implements AccidentFactory {

    @Override
    public Issue createIssue(String title, String description) {
        Date now = new Date();

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(now);
        String hour = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(now);

        return new MediumDangerIssue(
                title,
                description,
                "Localisation non renseignée",
                date,
                hour,
                "Modéré",
                3.0f,
                R.drawable.moyen,
                "Enregistré"
        );
    }
}