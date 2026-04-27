package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class ReportNewFragment extends Fragment {
    public final static int FRAGMENT_ID = 2;
    private Notifiable notifiable;
    private Menuable menuable;

    public ReportNewFragment() {
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
        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_new, container, false);

        Button btnTakePhoto = view.findViewById(R.id.btn_take_photo);
        Button btnSkipPhoto = view.findViewById(R.id.btn_skip_photo);
        TextView btnClose = view.findViewById(R.id.btn_close);

        btnTakePhoto.setOnClickListener(v -> {
            notifiable.onDataChange(FRAGMENT_ID, null, 3, null);
        });

        btnSkipPhoto.setOnClickListener(v -> {
            notifiable.onDataChange(FRAGMENT_ID, null, 3, null);
        });

        btnClose.setOnClickListener(v -> {
            menuable.onMenuChange(0);
        });

        return view;
    }
}