package edu.polytech.filrouge_teamM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class ReportLocationFragment extends Fragment {
    public final static int FRAGMENT_ID = 2;
    private final String TAG = "teamM " + getClass().getSimpleName();
    private Notifiable notifiable;
    private EditText inputAddress;

    private final ActivityResultLauncher<Intent> voiceLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (matches != null && !matches.isEmpty() && inputAddress != null) {
                        inputAddress.setText(matches.get(0));
                    }
                }
            }
    );

    public ReportLocationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_report_location, container, false);

        inputAddress = view.findViewById(R.id.input_address);
        Button btnConfirm = view.findViewById(R.id.btn_confirm_location);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        View btnBack = view.findViewById(R.id.btn_back);
        ImageView micIcon = view.findViewById(R.id.mic_icon_location);

        micIcon.setOnClickListener(v -> startVoiceRecognition());

        btnConfirm.setOnClickListener(v -> {
            String address = inputAddress.getText().toString();
            if (address.trim().isEmpty()) {
                address = "Adresse non renseignée";
            }
            notifiable.onDataChange(FRAGMENT_ID, address, 4, null);
        });

        btnCancel.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parlez en français pour indiquer la localisation...");

        try {
            voiceLauncher.launch(intent);
        } catch (Exception e) {
            Log.e(TAG, "Reconnaissance vocale non supportée.");
        }
    }
}