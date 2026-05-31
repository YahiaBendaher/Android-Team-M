package edu.polytech.filrouge_teamM.report.creation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import java.io.File;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.app.ControlActivity;
import edu.polytech.filrouge_teamM.navigation.Menuable;
import edu.polytech.filrouge_teamM.navigation.Notifiable;
import edu.polytech.filrouge_teamM.report.factory.HighwayFactory;
import edu.polytech.filrouge_teamM.report.factory.IssueFactory;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.report.factory.UrbanFactory;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_summary, container, false);
        data = getArguments();

        TextView typeText = view.findViewById(R.id.summary_type);
        TextView addressText = view.findViewById(R.id.summary_address);
        TextView categoryText = view.findViewById(R.id.summary_category);
        TextView sizeText = view.findViewById(R.id.summary_size);
        TextView dangerText = view.findViewById(R.id.summary_danger);
        TextView contextText = view.findViewById(R.id.summary_context);
        TextView descriptionText = view.findViewById(R.id.summary_description);
        TextView protocolText = view.findViewById(R.id.summary_protocol);
        ImageView imageView = view.findViewById(R.id.summary_image);

        if (data != null) {
            String title = data.getString("title", "Sans titre");
            String address = data.getString("address", "Adresse inconnue");
            String category = data.getString("category", "Non renseignée");
            String size = data.getString("size", "Non renseignée");
            String danger = data.getString("danger", "Non renseigné");
            String contextStr = data.getString("context", "Urbain");
            String description = data.getString("description", "");

            if (typeText != null) typeText.setText(title);
            if (addressText != null) addressText.setText(address);
            if (categoryText != null) categoryText.setText(category);
            if (sizeText != null) sizeText.setText(size);
            if (dangerText != null) dangerText.setText(danger);
            if (contextText != null) contextText.setText(contextStr);
            
            if (descriptionText != null) {
                descriptionText.setText(description == null || description.isEmpty() ? "Aucune description renseignée" : description);
            }

            if (protocolText != null) {
                protocolText.setText(getSafetyProtocolForContext(contextStr));
            }

            String picturePath = data.getString("picture");
            if (picturePath != null && !picturePath.isEmpty()) {
                File file = new File(picturePath);
                if (file.exists()) {
                    Picasso.get().load(file).into(imageView);
                }
            }
        }

        view.findViewById(R.id.btn_send_report).setOnClickListener(v -> sendReport());

        view.findViewById(R.id.btn_back_summary).setOnClickListener(v -> 
                requireActivity().getSupportFragmentManager().popBackStack());

        view.findViewById(R.id.btn_cancel_summary).setOnClickListener(v -> {
            if (menuable != null) menuable.onMenuChange(ControlActivity.TAB_REPORT);
        });

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private String getSafetyProtocolForContext(String context) {
        if ("Autoroute".equalsIgnoreCase(context)) {
            return "Rester derrière la barrière de sécurité et ne pas intervenir sur la voie.";
        } else {
            return "Ralentir, sécuriser la zone et éviter de gêner les piétons et la circulation locale.";
        }
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
            notifiable.onDataChange(0, newIssue, Notifiable.ACTION_SEND_REPORT, null);
        }
    }
}
