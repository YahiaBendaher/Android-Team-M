package edu.polytech.filrouge_teamM.report.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.model.Status;

public class HomeIssueAdapter extends ArrayAdapter<Issue> {

    public HomeIssueAdapter(@NonNull Context context, @NonNull List<Issue> issues) {
        super(context, 0, issues);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_issue, parent, false);
        }

        Issue issue = getItem(position);

        if (issue != null) {
            TextView titleView = convertView.findViewById(R.id.item_home_issue_title);
            TextView locationView = convertView.findViewById(R.id.item_home_issue_location);
            TextView statusView = convertView.findViewById(R.id.item_home_issue_status);

            titleView.setText(issue.getCategory());
            locationView.setText(issue.getLocation());
            statusView.setText(issue.getFrenchStatus());
            statusView.setTextColor(getStatusTextColor(issue.getStatus()));
        }

        return convertView;
    }

    private int getStatusTextColor(Status status) {
        if (status == null) return 0xFF64748B;
        switch (status) {
            case REGISTERED: 
                return 0xFFEA580C;
            case TAKEN_IN_CHARGE: 
                return 0xFF2563EB;
            case IN_PROGRESS: 
                return 0xFF8B5CF6;
            case RESOLVED: 
                return 0xFF10B981;
            default: 
                return 0xFF64748B;
        }
    }
}
