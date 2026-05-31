package edu.polytech.filrouge_teamM.report.creation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.navigation.Notifiable;

public class ReportNewFragment extends Fragment {
    public final static int FRAGMENT_ID = 2;
    private Notifiable notifiable;

    public ReportNewFragment() {
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_new, container, false);

        Button btnTakePhoto = view.findViewById(R.id.btn_take_photo);
        Button btnSkipPhoto = view.findViewById(R.id.btn_skip_photo);

        btnTakePhoto.setOnClickListener(v -> {
            if (notifiable != null) {
                notifiable.onDataChange(FRAGMENT_ID, null, Notifiable.ACTION_START_CAMERA, null);
            }
        });

        btnSkipPhoto.setOnClickListener(v -> {
            if (notifiable != null) {
                notifiable.onDataChange(FRAGMENT_ID, null, Notifiable.ACTION_OPEN_LOCATION_WITHOUT_PHOTO, null);
            }
        });

        return view;
    }
}
