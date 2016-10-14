package com.nandity.djmaps.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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
import com.nandity.djmaps.util.MapGeocoding;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 地图
 * https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyBXHFdePMRIYef0pgjvCbYBbXt8bew_3SQ
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText mLonEdt, mLatEdt;
    private Switch stClickAdd;
    private Button mSureBtn, btnList, btnSave;
    private double mLatitude, mLongitude;
    private List<LatLng> mList;
    private MapApplication app = MapApplication.getInstance();
    private int myPlan;
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        List<Integer> linkplain = SQLUtils.getplans(app);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getIntent().getIntExtra("plan", -1) != -1) {
            myPlan = getIntent().getIntExtra("plan", -1);
        } else {
            if (linkplain.size() == 0) {
                myPlan = 1;
            }
        }
        mLatEdt = (EditText) findViewById(R.id.lat_edt);
        mLonEdt = (EditText) findViewById(R.id.lon_edt);
        mSureBtn = (Button) findViewById(R.id.sure_btn);
        btnList = (Button) findViewById(R.id.list_btn);
        btnSave = (Button) findViewById(R.id.save_btn);
        stClickAdd = (Switch) findViewById(R.id.st_add);
        initDate();
    }


    /**
     * 设置监听
     */
    private void initListener() {
        mSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (stClickAdd.isChecked()) {
                    addPoint(latLng.latitude + "", latLng.longitude + "");
                    setPolyline();
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentMarker = marker;
                final String lo = marker.getPosition().latitude + "";
                final String la = marker.getPosition().longitude + "";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String address = MapGeocoding.requestByGet(app, lo, la);
                            Message message = Message.obtain();
                            message.obj = address;
                            message.what = 1;
                            message.arg1 = 1;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
                return false;
            }
        });

    }

    /**
     * 画线
     */
    private void setPolyline() {
        mMap.clear();
        PolylineOptions rectOptions = new PolylineOptions();
        rectOptions.color(Color.WHITE);
        SQLiteDatabase db = SQLUtils.getSQLiteDatabase(app);
        // 根据时间查询数据
        Cursor cursor = db.query("mapinfo", null, "plan=?", new String[]{myPlan + ""},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int point1 = cursor.getInt(cursor.getColumnIndex("point"));
                String lo = cursor.getString(cursor.getColumnIndex("lo"));
                String la = cursor.getString(cursor.getColumnIndex("la"));
                setMarker(point1, lo, la);
                rectOptions.add(new LatLng(Double.parseDouble(lo), Double.parseDouble(la)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        // Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String address = (String) msg.obj;
                    currentMarker.setTitle("第" + currentMarker.getTag() + "个点：" + address);
                    currentMarker.showInfoWindow();
                    mLonEdt.setText(currentMarker.getPosition().latitude + "");
                    mLatEdt.setText(currentMarker.getPosition().longitude + "");
                    break;
            }
        }
    };


    /**
     * 标记点
     *
     * @param point
     * @param lo
     * @param la
     */
    private void setMarker(final int point, final String lo, final String la) {
//
        double mLongitude1 = Double.valueOf(lo);
        double mLatitude1 = Double.valueOf(la);
        LatLng MELBOURNE = new LatLng(mLongitude1, mLatitude1);
        Marker melbourne = mMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("第" + point + "点" + ":" + mLongitude1 + "," + mLatitude1)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        melbourne.setTag(point);
    }

    /**
     * 添加点到数据库
     *
     * @param lo
     * @param la
     */
    private void addPoint(String lo, String la) {
        List<Integer> listpoints = SQLUtils.getpoint(app, myPlan);
        if (listpoints.size() != 0) {
            SQLUtils.insertPoint(app, myPlan, listpoints.get(listpoints.size() - 1) + 1, lo, la);
        } else {
            SQLUtils.insertPoint(app, myPlan, 1, lo, la);
        }
    }

    private void initDate() {
        mList = new ArrayList<>();
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        mLongitude = 29.616351;
        mLatitude = 106.516677;
        LatLng sydney = new LatLng(mLongitude, mLatitude);
        mLonEdt.setText(29.616351 + "");
        mLatEdt.setText(106.516677 + "");
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        mMap.setTrafficEnabled(true);
//        mMap.setBuildingsEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        setPolyline();
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        initListener();
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
                        double mLongitude1 = Double.valueOf(mLonEdt.getText().toString());
                        double mLatitude1 = Double.valueOf(mLatEdt.getText().toString());
                        LatLng MELBOURNE = new LatLng(mLongitude1, mLatitude1);
                        mList.add(MELBOURNE);
                        addPoint(mLonEdt.getText().toString(), mLatEdt.getText().toString());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
        builder.setTitle("提示"); // 设置标题
        builder.setMessage("是否添加坐标?"); // 设置内容
        builder.setIcon(R.mipmap.main_map_pressed);// 设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
    }

//    /**
//     * 獲取本地
//     * @param context
//     * @return
//     */
//    public String getLocation(Context context){
//        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
//        //List<String> lp = lm.getAllProviders();
//        Criteria criteria = new Criteria();
//        criteria.setCostAllowed(false);
//        //设置位置服务免费
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
//        //getBestProvider 只有允许访问调用活动的位置供应商将被返回
//        String  providerName = lm.getBestProvider(criteria, true);
//
//        if (providerName != null)
//        {
//            Location location = lm.getLastKnownLocation(providerName);
//            if(location!=null){
//                //获取维度信息
//                double latitude = location.getLatitude();
//                //获取经度信息
//                double longitude = location.getLongitude();
//                return latitude+","+longitude;
//            }
//        }
//        return "";
//    }

}
