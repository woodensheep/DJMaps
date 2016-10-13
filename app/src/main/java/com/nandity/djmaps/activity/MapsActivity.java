package com.nandity.djmaps.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.nandity.djmaps.app.MapApplication;
import com.nandity.djmaps.sqlite.SQLUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText mLonEdt, mLatEdt;
    private Button mSureBtn;
    private double mLatitude, mLongitude;
    private List<LatLng> mList;
    private MapApplication app;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        app = MapApplication.getInstance();
        app.addPlan();
        mLatEdt = (EditText) findViewById(R.id.lat_edt);
        mLonEdt = (EditText) findViewById(R.id.lon_edt);
        mSureBtn = (Button) findViewById(R.id.sure_btn);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initDate();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "nihao", Toast.LENGTH_SHORT).show();
                double mLongitude1 = Double.valueOf(mLonEdt.getText().toString());
                double mLatitude1 = Double.valueOf(mLatEdt.getText().toString());
                LatLng MELBOURNE = new LatLng(mLongitude1, mLatitude1);
                mList.add(MELBOURNE);
                SQLUtils.insertPoint(app,app.getPlanNumber(),mList.size(),mLonEdt.getText().toString(),mLatEdt.getText().toString());
                Marker melbourne = mMap.addMarker(new MarkerOptions()
                        .position(MELBOURNE)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(MELBOURNE));
                setPolyline();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    /**
     * 画线
     */
    private void setPolyline() {
        PolylineOptions rectOptions = new PolylineOptions();
        SQLiteDatabase db = SQLUtils.getSQLiteDatabase(app);
        ContentValues values = new ContentValues();
        // 根据时间查询数据
        Cursor cursor = db.query("mapinfo", new String[]{"plan"},"plan" , new String[]{app.getPlanNumber() + ""},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int point1 = cursor.getInt(cursor.getColumnIndex("point"));
            while (cursor != null) {
                String lo = cursor.getString(cursor.getColumnIndex("lo"));
                String la = cursor.getString(cursor.getColumnIndex("la"));
                rectOptions.add(new LatLng(Double.parseDouble(lo), Double.parseDouble(la)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        // Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);
    }

    private void initDate() {
        mList = new ArrayList<LatLng>();
        mList.add(new LatLng(29.616351, 106.516677));
        SQLUtils.insertPoint(app, app.getPlanNumber(), mList.size(), 29.616351 + "", 106.516677 + "");
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
        mLonEdt.setText(mList.get(0).latitude + "");
        mLatEdt.setText(mList.get(0).longitude + "");
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
