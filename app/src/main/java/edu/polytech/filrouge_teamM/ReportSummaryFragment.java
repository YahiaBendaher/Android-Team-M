package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import java.io.File;

public class ReportSummaryFragment extends Fragment {
    private Notifiable notifiable;
    private Menuable menuable;
    private Bundle data;

    public static ReportSummaryFragment newInstance(Bundle data) {
        ReportSummaryFragment fragment = new ReportSummaryFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        }
        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_summary, container, false);
        data = getArguments();

        TextView addressText = view.findViewById(R.id.summary_address);
        TextView categoryText = view.findViewById(R.id.summary_category);
        TextView sizeText = view.findViewById(R.id.summary_size);
        TextView dangerText = view.findViewById(R.id.summary_danger);
        ImageView imageView = view.findViewById(R.id.summary_image);

        if (data != null) {
            addressText.setText(data.getString("address"));
            categoryText.setText(data.getString("category"));
            sizeText.setText(data.getString("size"));
            dangerText.setText(data.getString("danger"));

            String picturePath = data.getString("picture");
            if (picturePath != null && !picturePath.isEmpty()) {
                Picasso.get().load(new File(picturePath)).into(imageView);
            }
        }

        view.findViewById(R.id.btn_send_report).setOnClickListener(v -> {
            sendReport();
        });

        view.findViewById(R.id.btn_back_summary).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        view.findViewById(R.id.btn_cancel_summary).setOnClickListener(v -> {
            if (menuable != null) menuable.onMenuChange(ControlActivity.TAB_REPORT);
        });

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void sendReport() {
        if (data == null) return;

        IssueFactory factory;
        if ("Autoroute".equalsIgnoreCase(data.getString("context"))) {
            factory = new HighwayFactory();
        } else {
            factory = new UrbanFactory();
        }

        Issue newIssue = factory.createIssue(
                data.getString("title"),
                data.getString("description"),
                data.getString("address"),
                data.getDouble("lat"),
                data.getDouble("lng"),
                data.getString("category"),
                data.getString("size"),
                data.getString("danger"),
                data.getString("context"),
                data.getString("picture")
        );

        if (notifiable != null) {
            notifiable.onDataChange(0, newIssue, 7, null);
        }
    }
}
