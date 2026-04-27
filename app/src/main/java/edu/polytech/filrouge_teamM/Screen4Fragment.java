package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class Screen4Fragment extends Fragment implements ClickableIssue<Issue> {
    public final static int FRAGMENT_ID = 3;
    private Notifiable notifiable;
    private List<Issue> issues;
    private IssueAdapter adapter;

    public Screen4Fragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (notifiable != null) {
            notifiable.onFragmentDisplayed(FRAGMENT_ID);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Notifiable.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen4, container, false);

        ListView listView = view.findViewById(R.id.listViewSignalements);

        issues = new ArrayList<>();
        issues.add(new UrbanIssue("Déchet", "Sacs poubelles abandonnés", "Rue de Rivoli, Paris", "04/04/2026", "15:51", "Faible", 2.0f, R.drawable.faible, "Enregistré", "Déchet", "Moyen", "Urbain"));
        issues.add(new UrbanIssue("Obstacle", "Gros carton sur la chaussée", "Champs-Élysées, Paris", "03/04/2026", "14:30", "Élevé", 4.0f, R.drawable.eleve, "Pris en charge", "Obstacle", "Grand", "Urbain"));
        issues.add(new UrbanIssue("Embouteillage", "Accident mineur bloquant une voie", "Quai d'Orsay, Paris", "04/04/2026", "09:15", "Moyen", 3.0f, R.drawable.moyen, "Traité", "Obstacle", "Moyen", "Urbain"));
        issues.add(new UrbanIssue("Verre/Débris", "Bouteilles brisées sur la piste cyclable", "Rue de Rivoli, Paris", "03/04/2026", "16:15", "Élevé", 5.0f, R.drawable.eleve, "En cours", "Verre/Débris", "Petit", "Urbain"));
        issues.add(new UrbanIssue("Branche", "Branche d'arbre cassée", "Boulevard Saint-Michel, Paris", "02/04/2026", "10:20", "Moyen", 3.0f, R.drawable.moyen, "Enregistré", "Branche", "Moyen", "Urbain"));
        issues.add(new UrbanIssue("Mobilier", "Banc public dégradé", "Rue du Faubourg Saint-Antoine, Paris", "04/04/2026", "10:30", "Moyen", 2.0f, R.drawable.moyen, "Pris en charge", "Mobilier", "Grand", "Urbain"));
        issues.add(new UrbanIssue("Nid-de-poule", "Trou profond sur la route", "Rue de Vaugirard, Paris", "01/04/2026", "11:45", "Moyen", 4.0f, R.drawable.moyen, "Traité", "Obstacle", "Moyen", "Urbain"));
        issues.add(new UrbanIssue("Travaux", "Signalisation de travaux mal placée", "Boulevard Haussmann, Paris", "06/04/2026", "07:30", "Faible", 1.0f, R.drawable.faible, "En cours", "Obstacle", "Moyen", "Urbain"));
        issues.add(new UrbanIssue("Inondation", "Bouche d'égout obstruée", "Quai de la Gare, Paris", "05/04/2026", "08:00", "Élevé", 4.0f, R.drawable.eleve, "Enregistré", "Obstacle", "Moyen", "Urbain"));
        issues.add(new HighwayIssue("Brouillard dense", "Visibilité très réduite", "Périphérique, Paris", "07/04/2026", "06:00", "Élevé", 5.0f, R.drawable.eleve, "En cours", "Autre", "Grand", "Autoroute"));

        adapter = new IssueAdapter(this, issues);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onRatingBarChange(int itemIndex, float value, IssueAdapter adapter, List<Issue> items) {
        Issue issue = items.get(itemIndex);
        issue.setRating(value);
        adapter.notifyDataSetChanged();
        if (notifiable != null) {
            notifiable.onDataChange(FRAGMENT_ID, issue, 2, value);
        }
    }

    @Override
    public void onClickItem(List<Issue> items, int itemIndex) {
        if (notifiable != null) {
            notifiable.onDataChange(FRAGMENT_ID, items.get(itemIndex), 1, null);
        }
    }
}