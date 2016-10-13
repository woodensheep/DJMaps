package com.nandity.djmaps.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.nandity.djmaps.sqlite.MapSQLiteHelper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        app = MapApplication.getInstance();
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
            dialog();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    /**
     * 画线
     */
    private void setPolyline() {
        mMap.clear();
        PolylineOptions rectOptions = new PolylineOptions();
        SQLiteDatabase db = SQLUtils.getSQLiteDatabase(app);
        ContentValues values = new ContentValues();
        // 根据时间查询数据
        Cursor cursor = db.query("mapinfo", null,"plan=?" , new String[]{app.getPlanNumber() + ""},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int point1 = cursor.getInt(cursor.getColumnIndex("point"));
                String lo = cursor.getString(cursor.getColumnIndex("lo"));
                String la = cursor.getString(cursor.getColumnIndex("la"));
                setMarker(point1,lo,la);
                rectOptions.add(new LatLng(Double.parseDouble(lo), Double.parseDouble(la)));
            }while (cursor.moveToNext());
            cursor.close();
        }
        // Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);
    }

    private void setMarker(int point, String lo, String la){
        double mLongitude1 = Double.valueOf(lo);
        double mLatitude1 = Double.valueOf(la);
        LatLng MELBOURNE = new LatLng(mLongitude1, mLatitude1);
        Marker melbourne = mMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title(point+":"+lo+","+la)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    private void initDate() {
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
        mLonEdt.setText(29.616351 + "");
        mLatEdt.setText(106.516677 + "");
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        setPolyline();
//        mMap.addMarker(new MarkerOptions().position(mList.get(0)).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * dialog
     */
    private void dialog() {
        // 先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        Toast.makeText(MapsActivity.this, "nihao", Toast.LENGTH_SHORT).show();
                        double mLongitude1 = Double.valueOf(mLonEdt.getText().toString());
                        double mLatitude1 = Double.valueOf(mLatEdt.getText().toString());
                        LatLng MELBOURNE = new LatLng(mLongitude1, mLatitude1);
                        mList.add(MELBOURNE);
                        SQLUtils.insertPoint(app,app.getPlanNumber(),SQLUtils.getpointNumber(app,1),mLonEdt.getText().toString(),mLatEdt.getText().toString());
                        Marker melbourne = mMap.addMarker(new MarkerOptions()
                                .position(MELBOURNE)
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(MELBOURNE));
                        setPolyline();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        // dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(app); // 先得到构造器
        builder.setTitle("提示"); // 设置标题
        builder.setMessage("是否添加坐标?"); // 设置内容
        builder.setIcon(R.mipmap.main_map_pressed);// 设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
    }

}
