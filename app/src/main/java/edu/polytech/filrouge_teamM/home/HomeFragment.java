package edu.polytech.filrouge_teamM.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.map.ReportMapModel;
import edu.polytech.filrouge_teamM.app.ControlActivity;
import edu.polytech.filrouge_teamM.navigation.Menuable;
import edu.polytech.filrouge_teamM.navigation.Notifiable;
import edu.polytech.filrouge_teamM.report.adapter.HomeIssueAdapter;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.model.Status;

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
        if (getView() != null) {
            refreshHome(getView());
        }
    }

    public void refreshHome(View view) {
        List<Issue> allIssues = ReportMapModel.getInstance().getIssues();
        List<Issue> myReports = ReportMapModel.getInstance().getMyIssues();

        int totalCount = allIssues.size();
        int inProgressCount = 0;
        int resolvedCount = 0;

        for (Issue issue : allIssues) {
            if (issue.getStatus() == Status.IN_PROGRESS || issue.getStatus() == Status.TAKEN_IN_CHARGE) {
                inProgressCount++;
            } else if (issue.getStatus() == Status.RESOLVED) {
                resolvedCount++;
            }
        }

        ((TextView) view.findViewById(R.id.txtTotal)).setText(String.valueOf(totalCount));
        ((TextView) view.findViewById(R.id.txtEnCours)).setText(String.valueOf(inProgressCount));
        ((TextView) view.findViewById(R.id.txtTraites)).setText(String.valueOf(resolvedCount));

        LinearLayout myReportsContainer = view.findViewById(R.id.home_my_reports_container);
        TextView emptyMyReportsText = view.findViewById(R.id.home_empty_my_reports_text);
        
        myReportsContainer.removeAllViews();
        
        if (myReports.isEmpty()) {
            myReportsContainer.setVisibility(View.GONE);
            emptyMyReportsText.setVisibility(View.VISIBLE);
        } else {
            myReportsContainer.setVisibility(View.VISIBLE);
            emptyMyReportsText.setVisibility(View.GONE);
            
            List<Issue> displayedMyReports = new ArrayList<>();
            for (int i = myReports.size() - 1; i >= 0 && displayedMyReports.size() < 5; i--) {
                displayedMyReports.add(myReports.get(i));
            }
            
            HomeIssueAdapter myAdapter = new HomeIssueAdapter(requireContext(), displayedMyReports);
            for (int i = 0; i < myAdapter.getCount(); i++) {
                final Issue issue = myAdapter.getItem(i);
                View itemView = myAdapter.getView(i, null, myReportsContainer);
                itemView.setOnClickListener(v -> {
                    if (notifiable != null) {
                        notifiable.onDataChange(FRAGMENT_ID, issue, Notifiable.ACTION_OPEN_DETAIL, null);
                    }
                });
                myReportsContainer.addView(itemView);
            }
        }

        LinearLayout recentReportsContainer = view.findViewById(R.id.home_recent_reports_container);
        TextView emptyRecentReportsText = view.findViewById(R.id.home_empty_recent_reports_text);

        recentReportsContainer.removeAllViews();

        if (allIssues.isEmpty()) {
            recentReportsContainer.setVisibility(View.GONE);
            emptyRecentReportsText.setVisibility(View.VISIBLE);
        } else {
            recentReportsContainer.setVisibility(View.VISIBLE);
            emptyRecentReportsText.setVisibility(View.GONE);
            
            List<Issue> displayedRecentReports = new ArrayList<>();
            int start = Math.max(0, allIssues.size() - 3);
            for (int i = allIssues.size() - 1; i >= start; i--) {
                displayedRecentReports.add(allIssues.get(i));
            }

            HomeIssueAdapter recentAdapter = new HomeIssueAdapter(requireContext(), displayedRecentReports);
            for (int i = 0; i < recentAdapter.getCount(); i++) {
                final Issue issue = recentAdapter.getItem(i);
                View itemView = recentAdapter.getView(i, null, recentReportsContainer);
                itemView.setOnClickListener(v -> {
                    if (notifiable != null) {
                        notifiable.onDataChange(FRAGMENT_ID, issue, Notifiable.ACTION_OPEN_DETAIL, null);
                    }
                });
                recentReportsContainer.addView(itemView);
            }
        }
    }
}
