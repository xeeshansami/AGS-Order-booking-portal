package com.agsadil.agssalesandroidclientorderdocter.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agsadil.agssalesandroidclientorderdocter.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

/**
 * Lets the user pick a geographic point by tapping / long-pressing / dragging a
 * marker on a Google Map. The chosen latitude & longitude are returned to the
 * caller via setResult().
 *
 * Intent extras (optional, both Strings):
 *   "lat"  - initial latitude to centre on
 *   "lng"  - initial longitude to centre on
 *
 * Result extras (Strings): "lat", "lng"
 */
public class MapPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_LAT = "lat";
    public static final String EXTRA_LNG = "lng";
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    // Default fallback location (Sukkur, Pakistan) when nothing else is available.
    private static final LatLng DEFAULT_LOCATION = new LatLng(27.7052, 68.8574);

    private GoogleMap map;
    private Marker marker;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng selectedLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_picker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setSubtitle("Pick Customer Location");
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // Read any pre-existing coordinates passed in by the caller.
        try {
            String lat = getIntent().getStringExtra(EXTRA_LAT);
            String lng = getIntent().getStringExtra(EXTRA_LNG);
            if (lat != null && !lat.trim().isEmpty() && lng != null && !lng.trim().isEmpty()) {
                selectedLatLng = new LatLng(Double.parseDouble(lat.trim()), Double.parseDouble(lng.trim()));
            }
        } catch (Exception ignored) {
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button confirm = findViewById(R.id.btnConfirmLocation);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLatLng == null) {
                    Toast.makeText(MapPickerActivity.this,
                            "Tap on the map to choose a location first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent result = new Intent();
                result.putExtra(EXTRA_LAT, String.format(Locale.US, "%.6f", selectedLatLng.latitude));
                result.putExtra(EXTRA_LNG, String.format(Locale.US, "%.6f", selectedLatLng.longitude));
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        // Place / move marker on tap.
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                placeMarker(latLng, false);
            }
        });
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                placeMarker(latLng, false);
            }
        });

        // Keep the selection in sync while the marker is dragged.
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker m) {
            }

            @Override
            public void onMarkerDrag(@NonNull Marker m) {
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker m) {
                selectedLatLng = m.getPosition();
            }
        });

        if (selectedLatLng != null) {
            placeMarker(selectedLatLng, true);
        } else {
            enableMyLocationAndCenter();
        }
    }

    private void placeMarker(LatLng latLng, boolean animate) {
        selectedLatLng = latLng;
        if (map == null) return;
        if (marker == null) {
            marker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .title("Customer Location"));
        } else {
            marker.setPosition(latLng);
        }
        if (marker != null) {
            marker.showInfoWindow();
        }
        if (animate) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocationAndCenter() {
        if (hasLocationPermission()) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
                            placeMarker(here, true);
                        } else {
                            placeMarker(DEFAULT_LOCATION, true);
                        }
                    })
                    .addOnFailureListener(this, e -> placeMarker(DEFAULT_LOCATION, true));
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            // Show a sensible default until permission is granted.
            placeMarker(DEFAULT_LOCATION, true);
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocationAndCenter();
        }
    }
}
