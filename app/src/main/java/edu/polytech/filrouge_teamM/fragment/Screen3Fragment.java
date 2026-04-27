package edu.polytech.filrouge_teamM.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.contract.Notifiable;

public class Screen3Fragment extends Fragment {

    public final static int FRAGMENT_ID = 2;
    private final String TAG = "teamM " + getClass().getSimpleName();

    private Notifiable notifiable;

    public Screen3Fragment() {
        Log.d(TAG, "Screen3Fragment created");
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
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName()
                    + " ne met pas en œuvre Notifiable.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_screen3, container, false);

        TextView btnClose = view.findViewById(R.id.btnClose);
        View btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        View btnContinueWithoutPhoto = view.findViewById(R.id.btnContinueWithoutPhoto);

        btnClose.setOnClickListener(v -> requireActivity().onBackPressed());

        btnTakePhoto.setOnClickListener(v -> {
            Toast.makeText(
                    requireContext(),
                    "La prise de photo n'est pas encore disponible.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnContinueWithoutPhoto.setOnClickListener(v -> {
            if (notifiable != null) {
                notifiable.onDataChange(FRAGMENT_ID, null, 3, null);
            }
        });

        return view;
    }
}