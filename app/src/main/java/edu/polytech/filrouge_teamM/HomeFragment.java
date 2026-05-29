package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class HomeFragment extends Fragment {
    public final static int FRAGMENT_ID = 0;
    private Notifiable notifiable;
    private Menuable menuable;

    public HomeFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        }
        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (notifiable != null) {
            notifiable.onFragmentDisplayed(FRAGMENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.btnNouveauSignalement).setOnClickListener(v -> {
            if (menuable != null) menuable.onMenuChange(ControlActivity.TAB_REPORT);
        });

        view.findViewById(R.id.btnTotal).setOnClickListener(v -> {
            if (menuable != null) menuable.onMenuChange(ControlActivity.TAB_LIST);
        });
        view.findViewById(R.id.btnEnCours).setOnClickListener(v -> {
            if (menuable != null) menuable.onMenuChange(ControlActivity.TAB_LIST);
        });
        view.findViewById(R.id.btnTraites).setOnClickListener(v -> {
            if (menuable != null) menuable.onMenuChange(ControlActivity.TAB_LIST);
        });

        view.findViewById(R.id.voirToutMesPrises).setOnClickListener(v -> {
            if (menuable != null) menuable.onMenuChange(ControlActivity.TAB_TRACKING);
        });

        view.findViewById(R.id.voirToutRecents).setOnClickListener(v -> {
            if (menuable != null) menuable.onMenuChange(ControlActivity.TAB_LIST);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        rafraichir(requireView());
    }

    public void rafraichir(View view) {
        List<Issue> toutes = ReportMapModel.getInstance().getIssues();
        List<Issue> mesPrises = ReportMapModel.getInstance().getMyIssues();

        int total = toutes.size();
        int enCours = 0;
        int traites = 0;

        for (Issue issue : toutes) {
            if (issue.getStatus() == Status.IN_PROGRESS || issue.getStatus() == Status.TAKEN_IN_CHARGE) {
                enCours++;
            } else if (issue.getStatus() == Status.RESOLVED) {
                traites++;
            }
        }

        ((TextView) view.findViewById(R.id.txtTotal)).setText(String.valueOf(total));
        ((TextView) view.findViewById(R.id.txtEnCours)).setText(String.valueOf(enCours));
        ((TextView) view.findViewById(R.id.txtTraites)).setText(String.valueOf(traites));

        LinearLayout containerMesPrises = view.findViewById(R.id.containerMesPrises);
        containerMesPrises.removeAllViews();
        for (int i = mesPrises.size() - 1; i >= 0; i--) {
            containerMesPrises.addView(creerItemIssue(mesPrises.get(i)));
        }

        LinearLayout containerRecents = view.findViewById(R.id.containerRecents);
        containerRecents.removeAllViews();
        int debut = Math.max(0, toutes.size() - 3);
        for (int i = toutes.size() - 1; i >= debut; i--) {
            containerRecents.addView(creerItemIssue(toutes.get(i)));
        }
    }

    private View creerItemIssue(Issue issue) {
        androidx.cardview.widget.CardView card = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 12);
        card.setLayoutParams(cardParams);
        card.setRadius(16f);
        card.setCardElevation(0f);
        card.setCardBackgroundColor(0xFFFFFFFF);
        card.setUseCompatPadding(true);

        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(32, 28, 32, 28);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout textes = new LinearLayout(requireContext());
        textes.setOrientation(LinearLayout.VERTICAL);
        textes.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView titreView = new TextView(requireContext());
        titreView.setText(issue.getCategory());
        titreView.setTextColor(0xFF1E2B3C);
        titreView.setTextSize(14f);
        titreView.setTypeface(null, Typeface.BOLD);

        TextView locationView = new TextView(requireContext());
        locationView.setText(issue.getLocation());
        locationView.setTextColor(0xFF64748B);
        locationView.setTextSize(12f);
        LinearLayout.LayoutParams locParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        locParams.setMargins(0, 6, 0, 0);
        locationView.setLayoutParams(locParams);

        textes.addView(titreView);
        textes.addView(locationView);

        TextView statut = new TextView(requireContext());
        statut.setText(issue.getFrenchStatus());
        statut.setTextSize(12f);
        statut.setTextColor(couleurTextStatut(issue.getStatus()));
        statut.setTypeface(null, Typeface.BOLD);
        statut.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        row.addView(textes);
        row.addView(statut);
        card.addView(row);

        card.setOnClickListener(v -> {
            if (notifiable != null) {
                notifiable.onDataChange(FRAGMENT_ID, issue, 1, null);
            }
        });

        return card;
    }

    private int couleurTextStatut(Status status) {
        if (status == null) return 0xFF64748B;
        switch (status) {
            case REGISTERED: return 0xFFB45309;
            case TAKEN_IN_CHARGE: return 0xFF1D4ED8;
            case IN_PROGRESS: return 0xFF6D28D9;
            case RESOLVED: return 0xFF10B981;
            default: return 0xFF64748B;
        }
    }
}