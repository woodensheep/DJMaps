package com.nandity.djmaps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandity.djmaps.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 11540 on 2016/10/13.
 */

public class StartAdapter extends BaseAdapter {
    private Context context;//上下文
    private int resourceId;//列表项的布局文件
    private List<HashMap<String, Object>> list; //数据源
    public StartAdapter(Context context,int resourceId,List<HashMap<String,Object>> list)
    {
        this.context = context;
        this.resourceId = resourceId;
        this.list = list;
    }
    public  StartAdapter(Context context,List<HashMap<String,Object>> list){
        this.context = context;

        this.list = list;
    }
    //得到列表项的个数，即数据源的数据条数
    @Override
    public int getCount() {
        int count = 0;
        if(list!=null)
            return list.size();
        return count;
    }

    //得到对应position位置列表项的内容，即数据源list对应位置的数据
    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    //得到列表项的id
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //提高对对象的复用
        View view = null;
        if(convertView!=null)
        {
            view = convertView;
        }
        else
        {
            view = LayoutInflater.from(context).inflate(resourceId, parent,false);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder==null)
        {
            holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.id_textview);
            holder.image = (ImageView) view.findViewById(R.id.id_image);
            view.setTag(holder);
        }
        //给相应位置的图片和文字赋内容
        holder.textView.setText(list.get(position).get("text").toString());
        holder.image.setImageResource((Integer) list.get(position).get("image"));

        //返回视图，这里的视图是一整个列表项的视图
        return view;


    }

    class ViewHolder
    {
        TextView textView;
        ImageView image;
    }


}

