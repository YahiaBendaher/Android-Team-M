package edu.polytech.filrouge_teamM;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    public final static int FRAGMENT_ID = 1;
    private Notifiable notifiable;
    private ReportMapController controller;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private static final LatLng POLYTECH_SOPHIA = new LatLng(43.615, 7.071);
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    public MapFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        notifiable.onFragmentDisplayed(FRAGMENT_ID);
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

        checkLocationPermissionAndCenter();
    }

    private void checkLocationPermissionAndCenter() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            centerOnFallback();
        } else {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null && isValidLocation(location.getLatitude(), location.getLongitude())) {
                            Log.d("MapFragment", "Current location received: " + location.getLatitude() + ", " + location.getLongitude());
                            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f));
                        } else {
                            Log.d("MapFragment", "Location unavailable or invalid, fallback to Polytech");
                            centerOnFallback();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("MapFragment", "Error getting location", e);
                        centerOnFallback();
                    });
        }
    }

    private boolean isValidLocation(double latitude, double longitude) {
        if (latitude == 0.0 && longitude == 0.0) {
            return false;
        }
        return longitude > -20 && longitude < 40;
    }

    private void centerOnFallback() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(POLYTECH_SOPHIA, 15f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermissionAndCenter();
            }
        }
    }
}
