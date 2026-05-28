package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReportSentFragment extends Fragment {
    private static final String ARG_ISSUE = "created_issue";
    private Notifiable notifiable;
    private Menuable menuable;
    private Issue createdIssue;

    public static ReportSentFragment newInstance(Issue issue) {
        ReportSentFragment fragment = new ReportSentFragment();
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
        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            createdIssue = getArguments().getParcelable(ARG_ISSUE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_sent, container, false);

        view.findViewById(R.id.btn_view_report).setOnClickListener(v -> {
            if (notifiable != null && createdIssue != null) {
                // Action code 8 matches the detail view in ControlActivity
                notifiable.onDataChange(0, createdIssue, 8, null);
            }
        });

        view.findViewById(R.id.btn_go_home).setOnClickListener(v -> {
            if (menuable != null) {
                menuable.onMenuChange(ControlActivity.TAB_HOME);
            }
        });

        return view;
    }
}
