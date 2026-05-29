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
                Picasso.get().load(file).into(detailImage);
            } else {
                detailImage.setVisibility(View.GONE);
            }
        } else {
            detailImage.setVisibility(View.GONE);
        }

        TextView txtStatut = view.findViewById(R.id.txtStatutBadge);
        txtStatut.setText(issue.getFrenchStatus());
        txtStatut.setTextColor(couleurTextStatut(issue.getStatus()));
        txtStatut.setBackgroundColor(couleurBgStatut(issue.getStatus()));

        ((TextView) view.findViewById(R.id.txtCategorie)).setText(issue.getCategory());
        ((TextView) view.findViewById(R.id.txtTaille)).setText(issue.getSize());

        TextView txtDanger = view.findViewById(R.id.txtDanger);
        txtDanger.setText(issue.getDangerLevel());
        txtDanger.setTextColor(couleurDanger(issue.getPriority()));

        ((TextView) view.findViewById(R.id.txtDescription)).setText(issue.getDescription());
        ((TextView) view.findViewById(R.id.txtLocalisation)).setText(issue.getLocation());
        ((TextView) view.findViewById(R.id.txtDate)).setText(issue.getDate() + " à " + issue.getHour());

        return view;
    }

    private int couleurTextStatut(Status status) {
        if (status == null) return 0xFF64748B;
        switch (status) {
            case REGISTERED: return 0xFFB45309;
            case TAKEN_IN_CHARGE: return 0xFF1D4ED8;
            case IN_PROGRESS: return 0xFF6D28D9;
            case RESOLVED: return 0xFF065F46;
            default: return 0xFF64748B;
        }
    }

    private int couleurBgStatut(Status status) {
        if (status == null) return 0xFFE2E8F0;
        switch (status) {
            case REGISTERED: return 0xFFFEF3C7;
            case TAKEN_IN_CHARGE: return 0xFFDBEAFE;
            case IN_PROGRESS: return 0xFFEDE9FE;
            case RESOLVED: return 0xFFD1FAE5;
            default: return 0xFFE2E8F0;
        }
    }

    private int couleurDanger(Priority priority) {
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