package com.nandity.djmaps.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lemon on 2016/10/14.
 * <p/>
 * https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyBXHFdePMRIYef0pgjvCbYBbXt8bew_3SQ
 */
public class MapGeocoding {

    // Get方式请求
    public static String requestByGet(Context context,String lo, String la) throws Exception {

        String path = "https://maps.googleapis.com/maps/api/geocode/json?language=ZH-CN&latlng=" +
                lo +
                "," +
                la +
                "&" +
                "AIzaSyBNV5JrOLcVXjwlCJOSZWazj5uQ2Bga4As";
        //GET 请求
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            //Logger.d(response.body().string());
            JSONObject dataJson = new JSONObject(response.body().string());
            if ("OK".equals(dataJson.get("status"))) {
                JSONArray ja = dataJson.getJSONArray("results");
                JSONObject nbaJson = ja.getJSONObject(0);
                String formatted_address = nbaJson.getString("formatted_address");
                Logger.d(formatted_address);
                return formatted_address;
            }else{
                return "网络不通畅，地理位置显示失败";
            }
        } else {
            return "网络不通畅，地理位置显示失败";
        }
    }

}
