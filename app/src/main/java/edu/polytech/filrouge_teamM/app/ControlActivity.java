package edu.polytech.filrouge_teamM.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.IOException;

import edu.polytech.filrouge_teamM.home.HomeFragment;
import edu.polytech.filrouge_teamM.report.model.Issue;
import edu.polytech.filrouge_teamM.map.MapFragment;
import edu.polytech.filrouge_teamM.navigation.MenuFragment;
import edu.polytech.filrouge_teamM.navigation.Menuable;
import edu.polytech.filrouge_teamM.navigation.Notifiable;
import edu.polytech.filrouge_teamM.report.creation.PhotoConfirmationFragment;
import edu.polytech.filrouge_teamM.report.creation.Picturable;
import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.report.creation.ReportDescriptionFragment;
import edu.polytech.filrouge_teamM.report.detail.ReportDetailFragment;
import edu.polytech.filrouge_teamM.report.list.ReportListFragment;
import edu.polytech.filrouge_teamM.report.creation.ReportLocationFragment;
import edu.polytech.filrouge_teamM.map.ReportMapModel;
import edu.polytech.filrouge_teamM.report.creation.ReportNewFragment;
import edu.polytech.filrouge_teamM.report.creation.ReportSentFragment;
import edu.polytech.filrouge_teamM.report.creation.ReportSummaryFragment;
import edu.polytech.filrouge_teamM.report.tracking.TrackingFragment;

public class ControlActivity extends AppCompatActivity implements Menuable, Notifiable, Picturable {
    public static final int TAB_HOME = 0;
    public static final int TAB_MAP = 1;
    public static final int TAB_REPORT = 2;
    public static final int TAB_LIST = 3;
    public static final int TAB_TRACKING = 4;

    private static final String DATA_MENU_NUMBER = "num";
    private static final String DATA_PENDING_PICTURE = "pending_picture";
    private static final String DATA_PENDING_CAMERA_PHOTO = "pending_camera_photo";
    private final String TAG = "teamM " + getClass().getSimpleName();
    private Fragment mainFragment;
    private MenuFragment menu;

    private String pendingPicturePath;
    private String pendingCameraPhotoPath;

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCameraCapture();
                } else {
                    showCameraPermissionDeniedDialog();
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success && pendingCameraPhotoPath != null) {
                    openPhotoConfirmation(pendingCameraPhotoPath);
                } else {
                    Toast.makeText(this, "Photo annulée", Toast.LENGTH_SHORT).show();
                }
            });

    private final Fragment[] tabFragments = {
            new HomeFragment(),
            new MapFragment(),
            new ReportNewFragment(),
            new ReportListFragment(),
            new TrackingFragment()
    };
    private int menuNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        if (savedInstanceState == null) {
            menuNumber = TAB_HOME;
        } else {
            pendingPicturePath = savedInstanceState.getString(DATA_PENDING_PICTURE);
            pendingCameraPhotoPath = savedInstanceState.getString(DATA_PENDING_CAMERA_PHOTO);
            menuNumber = savedInstanceState.getInt(DATA_MENU_NUMBER);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.index))) {
            menuNumber = intent.getIntExtra(getString(R.string.index), TAB_HOME);
        }

        Bundle args = new Bundle();
        args.putInt(getString(R.string.index), menuNumber);

        if (savedInstanceState == null) {
            menu = new MenuFragment();
            menu.setArguments(args);
            mainFragment = tabFragments[menuNumber];

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_menu, menu)
                    .replace(R.id.fragment_main, mainFragment)
                    .commit();
        } else {
            menu = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_menu);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this::updateMenuForVisibleFragment);
    }

    private void setMenuIndex(int index) {
        menuNumber = index;
        if (menu != null) {
            menu.setCurrentActivatedIndex(index);
        }
    }

    private void updateMenuForVisibleFragment() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_main);

        if (current instanceof HomeFragment) {
            setMenuIndex(TAB_HOME);
        } else if (current instanceof MapFragment) {
            setMenuIndex(TAB_MAP);
        } else if (current instanceof ReportNewFragment
                || current instanceof PhotoConfirmationFragment
                || current instanceof ReportLocationFragment
                || current instanceof ReportDescriptionFragment
                || current instanceof ReportSummaryFragment
                || current instanceof ReportSentFragment) {
            setMenuIndex(TAB_REPORT);
        } else if (current instanceof ReportListFragment) {
            setMenuIndex(TAB_LIST);
        } else if (current instanceof TrackingFragment) {
            setMenuIndex(TAB_TRACKING);
        }
    }

    @Override
    public void onMenuChange(int index) {
        if (index == TAB_REPORT) {
            this.pendingPicturePath = null;
        }

        menuNumber = index;
        mainFragment = tabFragments[index];

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main, mainFragment)
                .addToBackStack(null)
                .commit();

        setMenuIndex(index);
    }

    @Override
    public void onFragmentDisplayed(int fragmentId) {
        if (menuNumber != fragmentId) {
            setMenuIndex(fragmentId);
        }
    }

    @Override
    public void onClick(int numFragment) {
        Log.d(TAG, "Click on fragment " + numFragment);
    }

    @Override
    public void onPictureTaken(String photopath) {
        this.pendingPicturePath = photopath;
    }

    public String consumePendingPicturePath() {
        String path = pendingPicturePath;
        pendingPicturePath = null;
        return path;
    }

    private void requestCameraCapture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCameraCapture();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCameraCapture() {
        try {
            File photoFile = File.createTempFile("IMG_", ".jpg", getCacheDir());
            pendingCameraPhotoPath = photoFile.getAbsolutePath();

            Uri photoUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    photoFile
            );

            takePictureLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de la création du fichier photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPhotoConfirmation(String photoPath) {
        mainFragment = PhotoConfirmationFragment.newInstance(photoPath);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_main, mainFragment)
                .addToBackStack(null)
                .commit();

        setMenuIndex(TAB_REPORT);
    }

    private void showCameraPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission requise")
                .setMessage("L’accès à la caméra est nécessaire pour ajouter une photo au signalement. Vous pouvez continuer sans photo si vous le souhaitez.")
                .setPositiveButton("Continuer sans photo", (dialog, which) -> {
                    onDataChange(TAB_REPORT, null, Notifiable.ACTION_OPEN_LOCATION_WITHOUT_PHOTO, null);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    @Override
    public void onDataChange(int numFragment, Object object, int actionCode, Object argsAction) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        boolean shouldCommit = false;

        if (actionCode == Notifiable.ACTION_START_CAMERA) {
            requestCameraCapture();
            return;
        } else if (actionCode == Notifiable.ACTION_OPEN_DETAIL || actionCode == Notifiable.ACTION_OPEN_SENT_DETAIL) {
            Issue selectedIssue = (Issue) object;
            mainFragment = ReportDetailFragment.newInstance(selectedIssue);
            shouldCommit = true;
        } else if (actionCode == Notifiable.ACTION_OPEN_LOCATION_WITH_PHOTO) {
            mainFragment = new ReportLocationFragment();
            shouldCommit = true;
        } else if (actionCode == Notifiable.ACTION_OPEN_LOCATION_WITHOUT_PHOTO) {
            this.pendingPicturePath = null;
            mainFragment = new ReportLocationFragment();
            shouldCommit = true;
        } else if (actionCode == Notifiable.ACTION_OPEN_DESCRIPTION) {
            Bundle locationData = (Bundle) object;
            String address = locationData.getString("address");
            double lat = locationData.getDouble("lat");
            double lng = locationData.getDouble("lng");
            mainFragment = ReportDescriptionFragment.newInstance(address, lat, lng, pendingPicturePath);
            shouldCommit = true;
        } else if (actionCode == Notifiable.ACTION_OPEN_SUMMARY) {
            Bundle summaryData = (Bundle) object;
            mainFragment = ReportSummaryFragment.newInstance(summaryData);
            shouldCommit = true;
        } else if (actionCode == Notifiable.ACTION_SEND_REPORT) {
            Issue newIssue = (Issue) object;
            ReportMapModel.getInstance().addIssue(newIssue);
            this.pendingPicturePath = null;
            mainFragment = ReportSentFragment.newInstance(newIssue);
            shouldCommit = true;

            for (Fragment f : getSupportFragmentManager().getFragments()) {
                if (f instanceof HomeFragment) {
                    if (f.getView() != null) {
                        ((HomeFragment) f).refreshHome(f.getView());
                    }
                } else if (f instanceof ReportListFragment) {
                    ((ReportListFragment) f).refreshIssues();
                } else if (f instanceof TrackingFragment) {
                    ((TrackingFragment) f).refreshIssues();
                }
            }
        }

        if (shouldCommit) {
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DATA_MENU_NUMBER, menuNumber);
        outState.putString(DATA_PENDING_PICTURE, pendingPicturePath);
        outState.putString(DATA_PENDING_CAMERA_PHOTO, pendingCameraPhotoPath);
    }
}
