package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.List;

public class ReportListFragment extends Fragment implements ClickableIssue<Issue> {
    public final static int FRAGMENT_ID = 3;
    private Notifiable notifiable;
    private List<Issue> issues;
    private IssueAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        if (notifiable != null) notifiable.onFragmentDisplayed(FRAGMENT_ID);
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
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        ListView listView = view.findViewById(R.id.listViewSignalements);
        issues = new java.util.ArrayList<>(ReportMapModel.getInstance().getIssues());
        java.util.Collections.reverse(issues);
        adapter = new IssueAdapter(this, issues);
        listView.setAdapter(adapter);
        return view;
    }

    public void rafraichir() {
        issues.clear();
        issues.addAll(ReportMapModel.getInstance().getIssues());
        java.util.Collections.reverse(issues);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        rafraichir();
    }

    @Override
    public void onRatingBarChange(int itemIndex, float value, IssueAdapter adapter, List<Issue> items) {
        Issue issue = items.get(itemIndex);
        issue.setRating(value);
        adapter.notifyDataSetChanged();
        if (notifiable != null) notifiable.onDataChange(FRAGMENT_ID, issue, 2, value);
    }

    @Override
    public void onClickItem(List<Issue> items, int itemIndex) {
        Issue issue = items.get(itemIndex);
        if (notifiable != null) {
            notifiable.onDataChange(FRAGMENT_ID, issue, 1, null);
        }
    }

    @Override
    public void onStatusArrowClick(List<Issue> items, int itemIndex) {
        Issue issue = items.get(itemIndex);
        showStatusPopup(issue, itemIndex);
    }

    private void showStatusPopup(Issue issue, int position) {
        ListView listView = requireView().findViewById(R.id.listViewSignalements);
        View anchorView = listView.getChildAt(position - listView.getFirstVisiblePosition());
        if (anchorView == null) anchorView = listView;

        PopupMenu popupMenu = new PopupMenu(requireContext(), anchorView);
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
