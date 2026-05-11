package edu.polytech.filrouge_teamM;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.List;

public class IssueAdapter extends ArrayAdapter<Issue> {
    private List<Issue> items;
    private LayoutInflater mInflater;
    private ClickableIssue<Issue> callBackFragment;

    public IssueAdapter(@NonNull ClickableIssue<Issue> callBackFragment, List<Issue> items) {
        super(callBackFragment.getContext(), 0, items);
        this.items = items;
        this.callBackFragment = callBackFragment;
        this.mInflater = LayoutInflater.from(callBackFragment.getContext());
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Issue getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_issue, parent, false);
        }

        Issue issue = getItem(position);

        ImageView priorityImage = convertView.findViewById(R.id.priorityImage);
        TextView title = convertView.findViewById(R.id.issueTitle);
        TextView description = convertView.findViewById(R.id.issueDescription);
        TextView location = convertView.findViewById(R.id.issueLocation);
        TextView dateTime = convertView.findViewById(R.id.issueDateTime);
        TextView dangerText = convertView.findViewById(R.id.issueDangerText);
        RatingBar ratingBar = convertView.findViewById(R.id.issueRating);
        TextView statusText = convertView.findViewById(R.id.issueStatus);
        ImageView statusArrow = convertView.findViewById(R.id.statusArrow);

        int resId = issue.getPriorityImageResId();
        if (resId != 0) {
            priorityImage.setImageResource(resId);
            priorityImage.setVisibility(View.VISIBLE);
        } else {
            priorityImage.setVisibility(View.GONE);
        }

        title.setText(issue.getTitle());
        description.setText(issue.getDescription());
        location.setText(issue.getLocation());
        dateTime.setText(issue.getDate() + " " + issue.getHour());
        statusText.setText("Statut : " + issue.getStatus().name());

        dangerText.setText(issue.getDangerLevel());
        if (issue.getDangerLevel().equalsIgnoreCase("Faible")) {
            dangerText.setTextColor(0xFF4CAF50);
        } else if (issue.getDangerLevel().equalsIgnoreCase("Moyen") || issue.getDangerLevel().equalsIgnoreCase("Modéré")) {
            dangerText.setTextColor(0xFFFF9800);
        } else if (issue.getDangerLevel().equalsIgnoreCase("Élevé")) {
            dangerText.setTextColor(0xFFF44336);
        }

        ratingBar.setRating(issue.getRating());

        View contentArea = convertView.findViewById(R.id.contentArea);
        contentArea.setOnClickListener(v -> callBackFragment.onClickItem(items, position));

        statusArrow.setOnClickListener(v -> callBackFragment.onStatusArrowClick(items, position));

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                callBackFragment.onRatingBarChange(position, rating, this, items);
            }
        });

        return convertView;
    }
}