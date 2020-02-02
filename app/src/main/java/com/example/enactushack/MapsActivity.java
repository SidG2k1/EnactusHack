package com.example.enactushack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Button btnMoment;
    private LatLng devicePos;

    private List<Moment> moments = new ArrayList<>();

    private void addMoment(String message, LatLng pos) {


        //Code for custom info for each marker
        CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd MMMM yyyy");

        InfoWindowData info = new InfoWindowData();
        info.setImage("img1");
        info.setMoment(message);

        String date = formatter.format(new Date());
        Marker marker = mMap.addMarker(new MarkerOptions().position(pos)
                .title(message)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_hidden)));
        marker.setTag(info);
        marker.hideInfoWindow();
        Moment moment = new Moment(message, date, "moiz", pos, marker);


        moments.add(moment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnMoment = findViewById(R.id.btnCam);
        btnMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
                dialog.setTitle("create your moment");
                dialog.setMessage("type here");

                final EditText input = new EditText(MapsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                dialog.setView(input);

                dialog.setPositiveButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addMoment(input.getText().toString(), devicePos);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location l : locationResult.getLocations()) {
                    devicePos = new LatLng(l.getLatitude(), l.getLongitude());
                    //double dist = getDistanceFromMarker(devicePos, moment);
                    for (Moment moment : moments) {
                        double dist = getDistanceFromMarker(devicePos, moment.pos);
                        if (dist < 2 && !moment.found) {
                            moment.marker.showInfoWindow();
                            //moment.marker.setVisible(false);
                            moment.found = true;
                            // TODO: hide marker and show card

                        }

                        if (dist >= 2 && moment.found) {
                            moment.found = false;
                            moment.marker.hideInfoWindow();
                            //moment.marker.setVisible(true);
                        }
                    }
                }
            }
        };

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest lr = new LocationRequest();
        lr.setInterval(300);
        lr.setFastestInterval(100);
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client.requestLocationUpdates(lr, locationCallback, Looper.getMainLooper());

    }

    private double getDistanceFromMarker(LatLng start, LatLng end) {
        double dlat = Math.abs(end.latitude - start.latitude);
        double dlong = Math.abs(end.longitude - start.longitude);
        return Math.sqrt((dlat * dlat) + (dlong * dlong)) * 10000;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        addMoment("chilling with the homies", new LatLng(43.008839, -81.273155));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20f));

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMarkerClickListener(this);

        enableMyLocation();

        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        } catch (Resources.NotFoundException e) {
            System.out.println(e);
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);

        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20f));
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        double dist = getDistanceFromMarker(devicePos, marker.getPosition());
        return dist > 2;
    }
}
