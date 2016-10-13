package com.nandity.djmaps.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nandity.djmaps.R;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText mLonEdt, mLatEdt;
    private Button mSureBtn;
    private double mLatitude, mLongitude;
    private List<LatLng> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLatEdt = (EditText) findViewById(R.id.lat_edt);
        mLonEdt = (EditText) findViewById(R.id.lon_edt);
        mSureBtn = (Button) findViewById(R.id.sure_btn);
        initDate();
        mSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "nihao", Toast.LENGTH_SHORT).show();
                double mLongitude1 = Double.valueOf(mLonEdt.getText().toString());
                double mLatitude1 = Double.valueOf(mLatEdt.getText().toString());
                LatLng MELBOURNE = new LatLng(mLongitude1, mLatitude1);
                Marker melbourne = mMap.addMarker(new MarkerOptions()
                        .position(MELBOURNE)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(MELBOURNE));
                PolylineOptions rectOptions = new PolylineOptions();


                rectOptions.add(new LatLng(mLongitude1, mLatitude1), new LatLng(mLongitude, mLatitude));
                mLatitude = mLatitude1;
                mLongitude = mLongitude1;

// Get back the mutable Polyline
                Polyline polyline = mMap.addPolyline(rectOptions);
                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {

                    }
                });
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void initDate() {
        mList = new ArrayList<LatLng>();
        mList.add(new LatLng(29.616351, 106.516677));
//    mList.add(new LatLng(29.616145, 106.516688));
    }

    /**
     * Manipulates the map once available.啊实打实的
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

        // Add a marker in Sydney and move the camera
        mLongitude = 29.616351;
        mLatitude = 106.516677;
        LatLng sydney = new LatLng(mLongitude, mLatitude);
        mLonEdt.setText(mList.get(0).longitude+"");
        mLatEdt.setText(mList.get(0).latitude+"");
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        mMap.addMarker(new MarkerOptions().position(mList.get(0)).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mList.get(0)));


    }

}
