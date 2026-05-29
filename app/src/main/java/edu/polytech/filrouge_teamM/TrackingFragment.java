package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.List;

public class TrackingFragment extends Fragment implements ClickableIssue<Issue> {
    public final static int FRAGMENT_ID = 4;
    private final String TAG = "teamM " + getClass().getSimpleName();
    private Notifiable notifiable;
    private IssueAdapter adapter;
    private List<Issue> myIssues;

    public TrackingFragment() {
        Log.d(TAG, "TrackingFragment created");
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
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        ListView listView = view.findViewById(R.id.listViewSuivi);
        myIssues = new java.util.ArrayList<>(ReportMapModel.getInstance().getMyIssues());
        java.util.Collections.reverse(myIssues);
        adapter = new IssueAdapter(this, myIssues);
        listView.setAdapter(adapter);
        return view;
    }

    public void rafraichir() {
        myIssues.clear();
        myIssues.addAll(ReportMapModel.getInstance().getMyIssues());
        java.util.Collections.reverse(myIssues);
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
    }

}