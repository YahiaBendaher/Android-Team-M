package edu.polytech.filrouge_teamM.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import edu.polytech.filrouge_teamM.contract.Notifiable;
import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.model.Issue;

public class Screen1Fragment extends Fragment {

    public final static int FRAGMENT_ID = 0;

    private Notifiable notifiable;
    private static final String ARG_ISSUE = "selected_issue";

    public Screen1Fragment() {
    }

    public static Screen1Fragment newInstance(Issue issue) {
        Screen1Fragment fragment = new Screen1Fragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_ISSUE, issue);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (notifiable != null) {
            notifiable.onFragmentDisplayed(FRAGMENT_ID);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_screen1, container, false);

        TextView titleView = view.findViewById(R.id.labelScreen1Fragment);
        TextView topicView = view.findViewById(R.id.topic);

        titleView.setText("Détail du signalement");

        if (getArguments() != null && getArguments().containsKey(ARG_ISSUE)) {
            Issue issue = getArguments().getParcelable(ARG_ISSUE);

            if (issue != null) {
                String details = "Signalement : " + issue.getTitle() + "\n\n"
                        + "Description : " + issue.getDescription() + "\n\n"
                        + "Localisation : " + issue.getLocation() + "\n"
                        + "Date : " + issue.getDate() + " " + issue.getHour() + "\n"
                        + "Danger : " + issue.getDangerLevel() + "\n"
                        + "Note : " + issue.getRating() + "/5\n"
                        + "Statut : " + issue.getStatus() + "\n\n"
                        + "Protocole : " + issue.getSafetyProtocol();

                topicView.setText(details);
            }
        }

        if (view.findViewById(R.id.button) != null) {
            view.findViewById(R.id.button).setVisibility(View.GONE);
        }

        return view;
    }
}