package com.nandity.djmaps.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nandity.djmaps.R;
import com.nandity.djmaps.adapter.StartAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        listview = (ListView) findViewById(R.id.id_list);
        //这里设置三个列表项，每个列表项对应一个图片和对应的文字
        //图片数组
        int image[] = new int[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
        //文字数组
        String text[] = {"连连看","斗地主","爱消除"};
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        //将一个图片和一个文字放入一个map集合中，并将map集合依次加入到list集合中
        for(int i=0;i<image.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("text", text[i]);
            map.put("image", image[i]);
            list.add(map);
        }

        //MyAdapter是自定义的适配器
        StartAdapter myadapter = new StartAdapter(this, R.layout.start_item, list);
        listview.setAdapter(myadapter);

        //点击列表项的响应事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(StartActivity.this, "点击了第"+(arg2+1)+"项，内容："+arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void newBtn(View view) {
        Intent intent= new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}


