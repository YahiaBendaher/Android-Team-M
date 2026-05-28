package edu.polytech.filrouge_teamM;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class PhotoConfirmationFragment extends Fragment {
    public final static int FRAGMENT_ID = 5;
    private static final String ARG_PHOTO_PATH = "photo_path";

    private String photoPath;
    private String pendingPhotoPath;
    private Picturable picturable;
    private Notifiable notifiable;
    private ImageView picturePreview;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Permission requise")
                            .setMessage("L’accès à la caméra est nécessaire pour reprendre une photo. Vous pouvez continuer avec la photo actuelle.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success) {
                    photoPath = pendingPhotoPath;
                    updatePreview();
                }
            });

    public static PhotoConfirmationFragment newInstance(String photoPath) {
        PhotoConfirmationFragment fragment = new PhotoConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_PATH, photoPath);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoConfirmationFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Picturable) {
            picturable = (Picturable) requireActivity();
        }
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoPath = getArguments().getString(ARG_PHOTO_PATH);
        }
        if (savedInstanceState != null) {
            photoPath = savedInstanceState.getString("photoPath");
            pendingPhotoPath = savedInstanceState.getString("pendingPhotoPath");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_confirmation, container, false);

        picturePreview = view.findViewById(R.id.picture_preview);
        Button btnUse = view.findViewById(R.id.btn_use_picture);
        Button btnRetake = view.findViewById(R.id.btn_retake_picture);

        btnUse.setOnClickListener(v -> {
            if (photoPath != null && picturable != null) {
                picturable.onPictureTaken(photoPath);
                if (notifiable != null) {
                    notifiable.onDataChange(FRAGMENT_ID, null, 3, null);
                }
            }
        });

        btnRetake.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        updatePreview();

        return view;
    }

    private void launchCamera() {
        try {
            File photoFile = File.createTempFile("IMG_", ".jpg", requireContext().getCacheDir());
            pendingPhotoPath = photoFile.getAbsolutePath();
            Uri photoUri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    photoFile);
            takePictureLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Erreur lors de la création du fichier", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePreview() {
        if (photoPath != null && picturePreview != null) {
            Picasso.get()
                    .load(new File(photoPath))
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(picturePreview);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("photoPath", photoPath);
        outState.putString("pendingPhotoPath", pendingPhotoPath);
    }
}
