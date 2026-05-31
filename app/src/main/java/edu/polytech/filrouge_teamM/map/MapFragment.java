package edu.polytech.filrouge_teamM.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import edu.polytech.filrouge_teamM.R;
import edu.polytech.filrouge_teamM.navigation.Notifiable;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    public final static int FRAGMENT_ID = 1;
    private Notifiable notifiable;
    private ReportMapController controller;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private static final LatLng POLYTECH_SOPHIA = new LatLng(43.615, 7.071);

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    enableMyLocationAndCenter();
                } else {
                    centerOnFallback();
                }
            });

    public MapFragment() {
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
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Notifiable.");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        controller = new ReportMapController(requireContext(), ReportMapModel.getInstance());
        controller.initMap(mMap);

        checkLocationPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (controller != null && mMap != null) {
            controller.displayReports();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocationAndCenter();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void enableMyLocationAndCenter() {
        if (mMap == null) return;
        
        try {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null && isValidLocation(location.getLatitude(), location.getLongitude())) {
                            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f));
                        } else {
                            centerOnFallback();
                        }
                    })
                    .addOnFailureListener(e -> {
                        centerOnFallback();
                    });
        } catch (SecurityException e) {
            centerOnFallback();
        }
    }

    private boolean isValidLocation(double latitude, double longitude) {
        if (latitude == 0.0 && longitude == 0.0) {
            return false;
        }
        return longitude > -20 && longitude < 40;
    }

    private void centerOnFallback() {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(POLYTECH_SOPHIA, 15f));
        }
    }
}
