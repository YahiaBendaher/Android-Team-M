package edu.polytech.filrouge_teamM.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.polytech.filrouge_teamM.contract.ClickableIssue;
import edu.polytech.filrouge_teamM.adapter.IssueAdapter;
import edu.polytech.filrouge_teamM.repository.IssueRepository;
import edu.polytech.filrouge_teamM.contract.Notifiable;
import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.model.Issue;

public class Screen4Fragment extends Fragment implements ClickableIssue<Issue> {

    public final static int FRAGMENT_ID = 3;

    private Notifiable notifiable;
    private List<Issue> issues = new ArrayList<>();
    private IssueAdapter adapter;

    public Screen4Fragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        if (notifiable != null) {
            notifiable.onFragmentDisplayed(FRAGMENT_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshIssues();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName()
                    + " ne met pas en œuvre Notifiable.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_screen4, container, false);

        ListView listView = view.findViewById(R.id.listViewSignalements);

        issues = new ArrayList<>(IssueRepository.getIssues());
        adapter = new IssueAdapter(this, issues);

        listView.setAdapter(adapter);

        return view;
    }

    private void refreshIssues() {
        if (adapter == null) {
            return;
        }

        issues.clear();
        issues.addAll(IssueRepository.getIssues());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRatingBarChange(int itemIndex, float value, IssueAdapter adapter, List<Issue> items) {
        Issue issue = items.get(itemIndex);
        issue.setRating(value);
        adapter.notifyDataSetChanged();

        if (notifiable != null) {
            notifiable.onDataChange(FRAGMENT_ID, issue, 2, value);
        }
    }

    @Override
    public void onClickItem(List<Issue> items, int itemIndex) {
        if (notifiable != null) {
            notifiable.onDataChange(FRAGMENT_ID, items.get(itemIndex), 1, null);
        }
    }
}