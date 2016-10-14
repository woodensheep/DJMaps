package com.nandity.djmaps.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nandity.djmaps.R;
import com.nandity.djmaps.adapter.StartAdapter;
import com.nandity.djmaps.app.MapApplication;
import com.nandity.djmaps.sqlite.SQLUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private MapApplication app = MapApplication.getInstance();
    private ListView listview;
    private Button ibAddPlan;
    private StartAdapter myadapter;
    List<HashMap<String, Object>> list;
    List<Integer> listplan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        listview = (ListView) findViewById(R.id.id_list);
        ibAddPlan= (Button) findViewById(R.id.ivb_addplan);
        list = new ArrayList<HashMap<String, Object>>();
        listplan=SQLUtils.getplans(app);
        //将一个图片和一个文字放入一个map集合中，并将map集合依次加入到list集合中
        for (int i = 0; i < SQLUtils.getplanNumber(app); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", (i + 1) + "");
            map.put("image", R.mipmap.compass_sel);
            map.put("plan",listplan.get(i));
            list.add(map);
        }

        //MyAdapter是自定义的适配器
        myadapter = new StartAdapter(this, R.layout.start_item, list);
        listview.setAdapter(myadapter);

        //点击列表项的响应事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Logger.d((int)arg1.getTag(R.id.tag_second)+"listplan"+listplan.toString());
                Intent intent = new Intent(StartActivity.this, MapsActivity.class);
                intent.putExtra("plan", (int) arg1.getTag(R.id.tag_second));
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.d((int)view.getTag(R.id.tag_second)+"listplan"+listplan.toString());
                dialog((int)view.getTag(R.id.tag_second));
                return true;
            }
        });

        ibAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MapsActivity.class);
                if(listplan.size()!=0) {
                    intent.putExtra("plan", listplan.get(listplan.size() - 1) + 1);
                }
                startActivity(intent);
                finish();
            }
        });

    }




    /**
     * dialog
     */
    private void dialog(final int plan) {
        // 先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        SQLUtils.deletePoint(StartActivity.this,plan);
                        list.clear();
                        listplan=SQLUtils.getplans(app);
                        for (int i = 0; i < listplan.size(); i++) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("title", (i + 1) + "");
                            map.put("image", R.mipmap.compass_sel);
                            map.put("plan",listplan.get(i));
                            list.add(map);
                        }
                        myadapter.notifyDataSetChanged();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        // dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
        builder.setTitle("提示"); // 设置标题
        builder.setMessage("是否删除路径?"); // 设置内容
        builder.setIcon(R.mipmap.main_map_pressed);// 设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
    }
}


