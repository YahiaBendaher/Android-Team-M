package edu.polytech.filrouge_teamM.report.tracking;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.List;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.map.ReportMapModel;
import edu.polytech.filrouge_teamM.navigation.Notifiable;
import edu.polytech.filrouge_teamM.report.adapter.ClickableIssue;
import edu.polytech.filrouge_teamM.report.adapter.IssueAdapter;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.model.Status;

public class TrackingFragment extends Fragment implements ClickableIssue<Issue> {
    public final static int FRAGMENT_ID = 4;
    private Notifiable notifiable;
    private IssueAdapter adapter;
    private List<Issue> myIssues;
    private ListView listView;
    private LinearLayout emptyStateLayout;

    public TrackingFragment() {
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        
        listView = view.findViewById(R.id.listViewSuivi);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        
        myIssues = new java.util.ArrayList<>(ReportMapModel.getInstance().getMyIssues());
        java.util.Collections.reverse(myIssues);
        
        adapter = new IssueAdapter(this, myIssues);
        adapter.setActionEnabled(true);
        listView.setAdapter(adapter);
        
        updateUI();
        
        return view;
    }

    private void updateUI() {
        if (myIssues == null || myIssues.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    public void refreshIssues() {
        if (myIssues != null) {
            myIssues.clear();
            myIssues.addAll(ReportMapModel.getInstance().getMyIssues());
            java.util.Collections.reverse(myIssues);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            updateUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshIssues();
    }

    @Override
    public void onRatingBarChange(int itemIndex, float value, IssueAdapter adapter, List<Issue> items) {
        Issue issue = items.get(itemIndex);
        issue.setRating(value);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickItem(List<Issue> items, int itemIndex) {
        Issue issue = items.get(itemIndex);
        if (notifiable != null) {
            notifiable.onDataChange(FRAGMENT_ID, issue, Notifiable.ACTION_OPEN_DETAIL, null);
        }
    }

    @Override
    public void onStatusArrowClick(View view, List<Issue> items, int itemIndex) {
        Issue issue = items.get(itemIndex);
        showStatusPopup(issue, view);
    }

    private void showStatusPopup(Issue issue, View anchorView) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchorView, Gravity.END);
        final String[] statusLabels = {"Enregistré", "Pris en charge", "En cours", "Traité"};
        final Status[] statusValues = {Status.REGISTERED, Status.TAKEN_IN_CHARGE, Status.IN_PROGRESS, Status.RESOLVED};

        for (int i = 0; i < statusLabels.length; i++) {
            popupMenu.getMenu().add(0, i, i, statusLabels[i]);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            issue.setStatus(statusValues[id]);
            adapter.notifyDataSetChanged();
            return true;
        });

        popupMenu.show();
    }
}
