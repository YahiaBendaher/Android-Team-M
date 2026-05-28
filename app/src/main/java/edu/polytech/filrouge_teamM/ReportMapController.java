package edu.polytech.filrouge_teamM;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportMapController {
    private final Context context;
    private final ReportMapModel model;
    private GoogleMap googleMap;
    private final Map<Marker, Issue> markerIssueMap = new HashMap<>();

    public ReportMapController(Context context, ReportMapModel model) {
        this.context = context;
        this.model = model;
    }

    public void initMap(GoogleMap map) {
        this.googleMap = map;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        setupInfoWindow();
        setupListeners();
        displayReports();
    }

    private void setupInfoWindow() {
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Issue issue = markerIssueMap.get(marker);
                if (issue == null) return null;

                View view = LayoutInflater.from(context).inflate(R.layout.item_map_info_window, null);

                TextView title = view.findViewById(R.id.info_title);
                TextView description = view.findViewById(R.id.info_description);
                TextView danger = view.findViewById(R.id.info_danger);
                TextView status = view.findViewById(R.id.info_status);
                TextView address = view.findViewById(R.id.info_address);

                title.setText(issue.getTitle());

                String desc = issue.getDescription();
                description.setText(desc != null && !desc.isEmpty() ? desc : "Description non renseignée");

                danger.setText("Danger : " + issue.getDangerLevel());
                status.setText("Statut : " + issue.getFrenchStatus());

                String addr = issue.getLocation();
                address.setText("Adresse : " + (addr != null && !addr.isEmpty() ? addr : "Adresse non disponible"));

                return view;
            }
        });
    }

    private void setupListeners() {
        googleMap.setOnMarkerClickListener(marker -> {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }
            return true;
        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.hideInfoWindow();
            }

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Issue issue = markerIssueMap.get(marker);
                if (issue != null) {
                    double lat = marker.getPosition().latitude;
                    double lng = marker.getPosition().longitude;
                    String address = getAddressFromCoordinates(lat, lng);
                    
                    model.updateReportLocation(issue, lat, lng, address);
                    marker.showInfoWindow();
                }
            }
        });
    }

    private String getAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.FRANCE);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String line = address.getAddressLine(0);
                return line != null ? line : "Position déplacée sur la carte";
            }
        } catch (IOException e) {
            return "Position déplacée sur la carte";
        }
        return "Position déplacée sur la carte";
    }

    public void displayReports() {
        googleMap.clear();
        markerIssueMap.clear();

        for (Issue issue : model.getIssues()) {
            LatLng pos = new LatLng(issue.getLatitude(), issue.getLongitude());
            
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColor(issue.getStatus()))));

            markerIssueMap.put(marker, issue);
        }
    }

    private float getMarkerColor(Status status) {
        if (status == null) return BitmapDescriptorFactory.HUE_ORANGE;
        switch (status) {
            case TAKEN_IN_CHARGE: return BitmapDescriptorFactory.HUE_BLUE;
            case IN_PROGRESS: return BitmapDescriptorFactory.HUE_VIOLET;
            case RESOLVED: return BitmapDescriptorFactory.HUE_GREEN;
            case REGISTERED:
            default: return BitmapDescriptorFactory.HUE_ORANGE;
        }
    }
}
