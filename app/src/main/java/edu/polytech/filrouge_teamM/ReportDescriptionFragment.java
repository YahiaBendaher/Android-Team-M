package edu.polytech.filrouge_teamM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class ReportDescriptionFragment extends Fragment {
    public final static int FRAGMENT_ID = 2;
    private final String TAG = "teamM " + getClass().getSimpleName();
    private static final String ARG_ADDRESS = "address";
    private Notifiable notifiable;
    private String address;

    private String selectedCategory = "Obstacle";
    private String selectedContext = "Urbain";
    private String selectedSize = "Moyen";
    private String selectedDangerLevel = "Modéré";

    private EditText issueTypeEditText;
    private EditText issueCommentEditText;
    private EditText currentTargetEditText;

    private final ActivityResultLauncher<Intent> voiceLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (matches != null && !matches.isEmpty() && currentTargetEditText != null) {
                        currentTargetEditText.setText(matches.get(0));
                    }
                }
            }
    );

    public static ReportDescriptionFragment newInstance(String address) {
        ReportDescriptionFragment fragment = new ReportDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    public ReportDescriptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            address = getArguments().getString(ARG_ADDRESS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        notifiable.onFragmentDisplayed(FRAGMENT_ID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_description, container, false);

        issueTypeEditText = view.findViewById(R.id.issueTypeEditText);
        issueCommentEditText = view.findViewById(R.id.issueCommentEditText);
        TextView textAddress = view.findViewById(R.id.text_address_summary);
        Button btnValidate = view.findViewById(R.id.btn_validate);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        View btnBack = view.findViewById(R.id.btn_back);
        ImageView micIconType = view.findViewById(R.id.mic_icon_type);
        ImageView micIconComment = view.findViewById(R.id.mic_icon_comment);

        if (address != null) {
            textAddress.setText("Localisation : " + address);
        }

        setupCategoryButtons(view);
        setupContextButtons(view);
        setupSizeButtons(view);
        setupDangerButtons(view);

        micIconType.setOnClickListener(v -> startVoiceRecognition(issueTypeEditText));
        micIconComment.setOnClickListener(v -> startVoiceRecognition(issueCommentEditText));

        btnValidate.setOnClickListener(v -> {
            String title = issueTypeEditText.getText().toString();
            String comment = issueCommentEditText.getText().toString();

            if (title.isEmpty()) {
                title = selectedCategory;
            }

            String description = comment;
            if (description.isEmpty()) {
                description = "Aucun commentaire";
            }

            IssueFactory factory;
            if ("Autoroute".equalsIgnoreCase(selectedContext)) {
                factory = new HighwayFactory();
            } else {
                factory = new UrbanFactory();
            }

            Issue newIssue = factory.createIssue(title, description, address, selectedCategory, selectedSize, selectedDangerLevel, selectedContext);

            if (notifiable != null) {
                notifiable.onDataChange(FRAGMENT_ID, newIssue, 0, newIssue.getSafetyProtocol());
            }
        });

        btnCancel.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void startVoiceRecognition(EditText target) {
        currentTargetEditText = target;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parlez en français pour remplir le champ...");

        try {
            voiceLauncher.launch(intent);
        } catch (Exception e) {
            Log.e(TAG, "Reconnaissance vocale non supportée sur cet appareil.");
        }
    }

    private void setupCategoryButtons(View view) {
        int[] ids = {R.id.btn_cat_dechet, R.id.btn_cat_obstacle, R.id.btn_cat_branche, R.id.btn_cat_carton, R.id.btn_cat_verre, R.id.btn_cat_mobilier, R.id.btn_cat_autre};
        for (int id : ids) {
            Button btn = view.findViewById(id);
            btn.setOnClickListener(v -> {
                selectedCategory = btn.getText().toString();
                updateButtonsUI(view, ids, id);
            });
        }
        updateButtonsUI(view, ids, R.id.btn_cat_obstacle);
    }

    private void setupContextButtons(View view) {
        int[] ids = {R.id.urbanContextButton, R.id.highwayContextButton};
        for (int id : ids) {
            Button btn = view.findViewById(id);
            btn.setOnClickListener(v -> {
                selectedContext = btn.getText().toString();
                updateButtonsUI(view, ids, id);
            });
        }
        updateButtonsUI(view, ids, R.id.urbanContextButton);
    }

    private void setupSizeButtons(View view) {
        int[] ids = {R.id.btn_size_petit, R.id.btn_size_moyen, R.id.btn_size_grand};
        for (int id : ids) {
            Button btn = view.findViewById(id);
            btn.setOnClickListener(v -> {
                selectedSize = btn.getText().toString();
                updateButtonsUI(view, ids, id);
            });
        }
        updateButtonsUI(view, ids, R.id.btn_size_moyen);
    }

    private void setupDangerButtons(View view) {
        int[] ids = {R.id.btn_danger_faible, R.id.btn_danger_modere, R.id.btn_danger_eleve};
        for (int id : ids) {
            Button btn = view.findViewById(id);
            btn.setOnClickListener(v -> {
                selectedDangerLevel = btn.getText().toString();
                updateButtonsUI(view, ids, id);
            });
        }
        updateButtonsUI(view, ids, R.id.btn_danger_modere);
    }

    private void updateButtonsUI(View view, int[] groupIds, int selectedId) {
        for (int id : groupIds) {
            Button btn = view.findViewById(id);
            if (id == selectedId) {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#2563FF")));
                btn.setTextColor(Color.WHITE);
            } else {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
                btn.setTextColor(Color.parseColor("#111827"));
            }
        }
    }
}