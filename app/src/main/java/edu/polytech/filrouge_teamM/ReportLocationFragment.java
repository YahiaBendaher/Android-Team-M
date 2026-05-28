package edu.polytech.filrouge_teamM;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReportLocationFragment extends Fragment implements OnMapReadyCallback {
    public final static int FRAGMENT_ID = 2;
    private Notifiable notifiable;
    
    private GoogleMap mMap;
    private Marker selectionMarker;
    private FusedLocationProviderClient fusedLocationClient;
    
    private static final LatLng POLYTECH_SOPHIA = new LatLng(43.615, 7.071);
    private static final String DEFAULT_ADDRESS = "930 Route des Colles, Sophia Antipolis";
    
    private double selectedLat = POLYTECH_SOPHIA.latitude;
    private double selectedLng = POLYTECH_SOPHIA.longitude;
    private String selectedAddress = DEFAULT_ADDRESS;

    private View mapContainer;
    private View addressInputContainer;
    private TextView txtCurrentAddress;
    private EditText inputAddress;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getUserLocation();
                } else {
                    centerOnFallback();
                }
            });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Notifiable) {
            notifiable = (Notifiable) requireActivity();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_location, container, false);
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        
        mapContainer = view.findViewById(R.id.map_container_report);
        addressInputContainer = view.findViewById(R.id.address_input_container);
        txtCurrentAddress = view.findViewById(R.id.txt_current_address);
        inputAddress = view.findViewById(R.id.input_address);

        setupButtons(view);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container_report);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container_report, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        return view;
    }

    private void setupButtons(View view) {
        Button btnGpsMode = view.findViewById(R.id.btn_mode_gps);
        Button btnAddressMode = view.findViewById(R.id.btn_mode_address);

        btnGpsMode.setOnClickListener(v -> {
            switchMode(true, btnGpsMode, btnAddressMode);
            checkLocationPermission();
        });
        btnAddressMode.setOnClickListener(v -> switchMode(false, btnGpsMode, btnAddressMode));

        view.findViewById(R.id.btn_confirm_location).setOnClickListener(v -> confirmLocation());
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void switchMode(boolean isGps, Button btnGps, Button btnAddr) {
        mapContainer.setVisibility(isGps ? View.VISIBLE : View.GONE);
        addressInputContainer.setVisibility(isGps ? View.GONE : View.VISIBLE);
        
        btnGps.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(isGps ? R.color.blue_obstrack : R.color.white)));
        btnGps.setTextColor(getResources().getColor(isGps ? R.color.white : R.color.black));
        
        btnAddr.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(isGps ? R.color.white : R.color.blue_obstrack)));
        btnAddr.setTextColor(getResources().getColor(isGps ? R.color.black : R.color.white));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        
        LatLng initialPos = new LatLng(selectedLat, selectedLng);
        selectionMarker = mMap.addMarker(new MarkerOptions()
                .position(initialPos)
                .title("Position choisie")
                .draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPos, 15f));

        mMap.setOnMapClickListener(latLng -> {
            selectedLat = latLng.latitude;
            selectedLng = latLng.longitude;
            selectionMarker.setPosition(latLng);
            updateAddressDisplay(selectedLat, selectedLng);
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng position = marker.getPosition();
                selectedLat = position.latitude;
                selectedLng = position.longitude;
                updateAddressDisplay(selectedLat, selectedLng);
            }
        });

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void updateAddressDisplay(double lat, double lng) {
        selectedAddress = getAddressFromCoordinates(lat, lng);
        txtCurrentAddress.setText(selectedAddress);
    }

    private String getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.FRANCE);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                return addressLine != null ? addressLine : "Position sélectionnée sur la carte";
            }
        } catch (IOException e) {
            return "Position sélectionnée sur la carte";
        }
        return "Position sélectionnée sur la carte";
    }

    private void getUserLocation() {
        try {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }

            fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null && isValidLocation(location.getLatitude(), location.getLongitude())) {
                            Log.d("ReportLocationFragment", "Current location received: " + location.getLatitude() + ", " + location.getLongitude());
                            selectedLat = location.getLatitude();
                            selectedLng = location.getLongitude();
                            LatLng userPos = new LatLng(selectedLat, selectedLng);
                            if (selectionMarker != null) selectionMarker.setPosition(userPos);
                            if (mMap != null) mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userPos, 15f));
                            updateAddressDisplay(selectedLat, selectedLng);
                        } else {
                            Log.d("ReportLocationFragment", "Location unavailable or invalid, fallback to Polytech");
                            centerOnFallback();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ReportLocationFragment", "Error getting location", e);
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
        LatLng fallback = POLYTECH_SOPHIA;
        selectedLat = fallback.latitude;
        selectedLng = fallback.longitude;
        if (selectionMarker != null) selectionMarker.setPosition(fallback);
        if (mMap != null) mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fallback, 15f));
        updateAddressDisplay(selectedLat, selectedLng);
    }

    private void confirmLocation() {
        if (addressInputContainer.getVisibility() == View.VISIBLE) {
            selectedAddress = inputAddress.getText().toString().trim();
            if (selectedAddress.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez saisir une adresse", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        Bundle data = new Bundle();
        data.putString("address", selectedAddress);
        data.putDouble("lat", selectedLat);
        data.putDouble("lng", selectedLng);
        
        if (notifiable != null) {
            notifiable.onDataChange(FRAGMENT_ID, data, 4, null);
        }
    }
}
