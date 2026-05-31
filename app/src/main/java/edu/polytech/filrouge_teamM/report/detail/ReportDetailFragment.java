package edu.polytech.filrouge_teamM.report.detail;

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

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.model.Priority;
import edu.polytech.filrouge_teamM.report.model.Status;

public class ReportDetailFragment extends Fragment {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_detail, container, false);

        view.findViewById(R.id.btnRetour).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() == null) return view;
        Issue issue = getArguments().getParcelable(ARG_ISSUE);
        if (issue == null) return view;

        ImageView detailImage = view.findViewById(R.id.logo_detail);
        ImageView iconeNoPhoto = view.findViewById(R.id.iconeNoPhoto);

        if (issue.getPicture() != null && !issue.getPicture().isEmpty()) {
            File file = new File(issue.getPicture());
            if (file.exists()) {
                iconeNoPhoto.setVisibility(View.GONE);
                detailImage.setVisibility(View.VISIBLE);
                Picasso.get().load(file).into(detailImage);
            } else {
                detailImage.setVisibility(View.GONE);
                iconeNoPhoto.setVisibility(View.VISIBLE);
            }
        } else {
            detailImage.setVisibility(View.GONE);
            iconeNoPhoto.setVisibility(View.VISIBLE);
        }

        TextView txtStatut = view.findViewById(R.id.txtStatutBadge);
        txtStatut.setText(issue.getFrenchStatus());
        txtStatut.setTextColor(getStatusTextColor(issue.getStatus()));
        txtStatut.setBackgroundColor(getStatusBackgroundColor(issue.getStatus()));

        ((TextView) view.findViewById(R.id.txtTitre)).setText(issue.getTitle() != null ? issue.getTitle() : "Sans titre");
        ((TextView) view.findViewById(R.id.txtCategorie)).setText(issue.getCategory());
        ((TextView) view.findViewById(R.id.txtTaille)).setText(issue.getSize());

        TextView txtDanger = view.findViewById(R.id.txtDanger);
        txtDanger.setText(issue.getDangerLevel());
        txtDanger.setTextColor(getDangerColor(issue.getPriority()));

        String context = issue.getContext();
        ((TextView) view.findViewById(R.id.txtContexte)).setText(context != null && !context.isEmpty() ? context : "Non renseigné");

        String description = issue.getDescription();
        ((TextView) view.findViewById(R.id.txtDescription)).setText(description != null && !description.isEmpty() ? description : "Aucun commentaire");

        String location = issue.getLocation();
        ((TextView) view.findViewById(R.id.txtLocalisation)).setText(location != null && !location.isEmpty() ? location : "Adresse non renseignée");

        ((TextView) view.findViewById(R.id.txtDate)).setText(issue.getDate() + " à " + issue.getHour());

        String protocol = issue.getSafetyProtocol();
        ((TextView) view.findViewById(R.id.txtProtocole)).setText(protocol != null && !protocol.isEmpty() ? protocol : "Aucun protocole particulier");

        return view;
    }

    private int getStatusTextColor(Status status) {
        if (status == null) return 0xFF64748B;
        switch (status) {
            case REGISTERED: return 0xFFB45309;
            case TAKEN_IN_CHARGE: return 0xFF1D4ED8;
            case IN_PROGRESS: return 0xFF6D28D9;
            case RESOLVED: return 0xFF065F46;
            default: return 0xFF64748B;
        }
    }

    private int getStatusBackgroundColor(Status status) {
        if (status == null) return 0xFFE2E8F0;
        switch (status) {
            case REGISTERED: return 0xFFFEF3C7;
            case TAKEN_IN_CHARGE: return 0xFFDBEAFE;
            case IN_PROGRESS: return 0xFFEDE9FE;
            case RESOLVED: return 0xFFD1FAE5;
            default: return 0xFFE2E8F0;
        }
    }

    private int getDangerColor(Priority priority) {
        if (priority == null) return 0xFF64748B;
        switch (priority) {
            case LOW: return 0xFF16A34A;
            case MEDIUM: return 0xFFD97706;
            case HIGH:
            case CRITICAL: return 0xFFDC2626;
            default: return 0xFF64748B;
        }
    }
}
