package edu.polytech.filrouge_teamM.report.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.List;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.model.Priority;
import edu.polytech.filrouge_teamM.report.model.Status;

public class IssueAdapter extends ArrayAdapter<Issue> {
    private List<Issue> items;
    private LayoutInflater mInflater;
    private ClickableIssue<Issue> callBackFragment;
    private boolean isActionEnabled = true;

    public IssueAdapter(@NonNull ClickableIssue<Issue> callBackFragment, List<Issue> items) {
        super(callBackFragment.getContext(), 0, items);
        this.items = items;
        this.callBackFragment = callBackFragment;
        this.mInflater = LayoutInflater.from(callBackFragment.getContext());
    }

    public void setActionEnabled(boolean enabled) {
        this.isActionEnabled = enabled;
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
        TextView statusArrow = convertView.findViewById(R.id.statusArrow);
        View statusContainer = convertView.findViewById(R.id.statusContainer);

        priorityImage.setVisibility(View.GONE);

        title.setText(issue.getTitle());
        
        String desc = issue.getDescription();
        description.setText((desc == null || desc.trim().isEmpty()) ? "Aucune description" : desc);

        String loc = issue.getLocation();
        location.setText((loc == null || loc.trim().isEmpty()) ? "Adresse non renseignée" : loc);

        dateTime.setText(issue.getDate() + " " + issue.getHour());
        
        statusText.setText(issue.getFrenchStatus());
        updateStatusColor(statusText, statusArrow, issue.getStatus());

        dangerText.setText("Danger : " + issue.getDangerLevel());
        updateDangerColor(dangerText, issue.getPriority());

        ratingBar.setRating(issue.getPriorityRating());
        ratingBar.setIsIndicator(!isActionEnabled);

        View contentArea = convertView.findViewById(R.id.contentArea);
        contentArea.setOnClickListener(v -> callBackFragment.onClickItem(items, position));

        if (isActionEnabled) {
            View.OnClickListener statusClickListener = v -> callBackFragment.onStatusArrowClick(statusContainer, items, position);
            statusContainer.setOnClickListener(statusClickListener);
            statusText.setOnClickListener(statusClickListener);
            statusArrow.setOnClickListener(statusClickListener);
            statusArrow.setVisibility(View.VISIBLE);
            statusContainer.setClickable(true);
        } else {
            statusContainer.setOnClickListener(null);
            statusText.setOnClickListener(null);
            statusArrow.setOnClickListener(null);
            statusArrow.setVisibility(View.GONE);
            statusContainer.setClickable(false);
        }

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser && isActionEnabled) {
                callBackFragment.onRatingBarChange(position, rating, this, items);
            }
        });

        return convertView;
    }

    private void updateDangerColor(TextView textView, Priority priority) {
        if (priority == null) return;
        switch (priority) {
            case LOW:
                textView.setTextColor(0xFF4CAF50);
                break;
            case MEDIUM:
                textView.setTextColor(0xFFFF9800);
                break;
            case HIGH:
            case CRITICAL:
                textView.setTextColor(0xFFF44336);
                break;
        }
    }

    private void updateStatusColor(TextView textView, TextView arrow, Status status) {
        if (status == null) return;
        int color;
        switch (status) {
            case REGISTERED:
                color = 0xFFEA580C;
                break;
            case TAKEN_IN_CHARGE:
                color = 0xFF2563EB;
                break;
            case IN_PROGRESS:
                color = 0xFF8B5CF6;
                break;
            case RESOLVED:
                color = 0xFF10B981;
                break;
            default:
                color = 0xFF2563EB;
                break;
        }
        textView.setTextColor(color);
        if (arrow != null) arrow.setTextColor(color);
    }
}
