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
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;

public class ReportDescriptionFragment extends Fragment {
    public final static int FRAGMENT_ID = 2;
    private final String TAG = "teamM " + getClass().getSimpleName();
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lng";
    private static final String ARG_PICTURE = "picturePath";
    private Notifiable notifiable;
    private String address;
    private double latitude;
    private double longitude;
    private String picturePath;

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

    public static ReportDescriptionFragment newInstance(String address, double lat, double lng, String picturePath) {
        ReportDescriptionFragment fragment = new ReportDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, address);
        args.putDouble(ARG_LAT, lat);
        args.putDouble(ARG_LNG, lng);
        args.putString(ARG_PICTURE, picturePath);
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
            latitude = getArguments().getDouble(ARG_LAT);
            longitude = getArguments().getDouble(ARG_LNG);
            picturePath = getArguments().getString(ARG_PICTURE);
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
        Button btnValidate = view.findViewById(R.id.btn_validate);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        View btnBack = view.findViewById(R.id.btn_back);
        ImageView micIconType = view.findViewById(R.id.mic_icon_type);
        ImageView micIconComment = view.findViewById(R.id.mic_icon_comment);

        TextView textPhotoStatus = view.findViewById(R.id.text_photo_status);
        ImageView imagePreview = view.findViewById(R.id.image_photo_preview);


        if (picturePath != null && !picturePath.isEmpty()) {
            textPhotoStatus.setText(getString(R.string.photo_added));
            imagePreview.setVisibility(View.VISIBLE);
            Picasso.get().load(new File(picturePath)).into(imagePreview);
        } else {
            textPhotoStatus.setText(getString(R.string.no_photo));
            imagePreview.setVisibility(View.GONE);
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

            Bundle data = new Bundle();
            data.putString("title", title);
            data.putString("description", description);
            data.putString("address", address);
            data.putDouble("lat", latitude);
            data.putDouble("lng", longitude);
            data.putString("category", selectedCategory);
            data.putString("size", selectedSize);
            data.putString("danger", selectedDangerLevel);
            data.putString("context", selectedContext);
            data.putString("picture", picturePath);

            if (notifiable != null) {
                notifiable.onDataChange(FRAGMENT_ID, data, 6, null);
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
