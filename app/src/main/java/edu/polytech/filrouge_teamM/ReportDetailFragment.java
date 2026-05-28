package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import java.io.File;

public class ReportDetailFragment extends Fragment {
    private Notifiable notifiable;
    private static final String ARG_ISSUE = "selected_issue";

    public ReportDetailFragment() {}

    public static ReportDetailFragment newInstance(Issue issue) {
        ReportDetailFragment fragment = new ReportDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ISSUE, issue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_detail, container, false);

        ImageView detailImage = view.findViewById(R.id.logo_detail);
        TextView topicView = view.findViewById(R.id.topic);
        TextView protocolView = view.findViewById(R.id.detail_protocol);

        if (getArguments() != null && getArguments().containsKey(ARG_ISSUE)) {
            Issue issue = getArguments().getParcelable(ARG_ISSUE);
            if (issue != null) {
                if (issue.getPicture() != null && !issue.getPicture().isEmpty()) {
                    File file = new File(issue.getPicture());
                    if (file.exists()) {
                        Picasso.get().load(file).into(detailImage);
                    } else if (issue.getPriorityImageResId() != 0) {
                        detailImage.setImageResource(issue.getPriorityImageResId());
                    }
                } else if (issue.getPriorityImageResId() != 0) {
                    detailImage.setImageResource(issue.getPriorityImageResId());
                }

                String details = "Type : " + issue.getTitle() + "\n" +
                        "Description : " + issue.getDescription() + "\n" +
                        "Localisation : " + issue.getLocation() + "\n" +
                        "Catégorie : " + issue.getCategory() + "\n" +
                        "Contexte : " + issue.getContext() + "\n" +
                        "Taille : " + issue.getSize() + "\n" +
                        "Priorité : " + issue.getDangerLevel() + "\n" +
                        "Statut : " + issue.getFrenchStatus() + "\n" +
                        "Date : " + issue.getDate() + " à " + issue.getHour();
                
                topicView.setText(details);
                
                if (protocolView != null) {
                    protocolView.setText(issue.getSafetyProtocol());
                }
            }
        }

        return view;
    }
}
