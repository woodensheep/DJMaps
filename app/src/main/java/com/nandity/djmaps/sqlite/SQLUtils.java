package com.nandity.djmaps.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lemon on 2016/10/12.
 */
public class SQLUtils {
    /**
     * 获取到SQLiteDatabase的实例
     */
    public static SQLiteDatabase getSQLiteDatabase(Context context) {
        SQLiteOpenHelper dbHelper = new MapSQLiteHelper(context, "dlmaps.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db;
    }

    public static void insertPoint(Context context, int plan, int point, String lo, String la) {
        SQLiteDatabase db = getSQLiteDatabase(context);
        ContentValues values = new ContentValues();
        values.put("plan", plan);
        values.put("point", point);
        values.put("lo", lo);
        values.put("la", la);
        long id = db.insert("mapinfo", null, values);
        values.clear();
    }

    public static void deletePoint(Context context, int plan) {
        SQLiteDatabase db = getSQLiteDatabase(context);
        db.delete("mapinfo", "plan= ?", new String[]{plan + ""});
    }

    public static void deletePoint(Context context, int plan,int point) {
        SQLiteDatabase db = getSQLiteDatabase(context);
        ContentValues values = new ContentValues();
        int i=getID(context,plan);
        db.delete("mapinfo", "plan<= ?", new String[]{i + ""});
    }

    public static void updatePoint(Context context, int plan, int point, String lo, String la) {
        SQLiteDatabase db = getSQLiteDatabase(context);
        ContentValues values = new ContentValues();
        values.put("lo", lo);
        values.put("la", la);
        int id = getID(context, plan);
        db.update("mapinfo", values, "id = ?", new String[]{id + ""});
    }


    public static int getID(Context context, int plan) {
        int id = 0;
        SQLiteDatabase db = getSQLiteDatabase(context);
        ContentValues values = new ContentValues();
        Cursor cursor = db.query("mapinfo", null, "plan=?", new String[]{plan + ""},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id++;
            } while (cursor.moveToNext());
            cursor.close();
        }
        return id;
    }


    public static List<Integer> getpoint(Context context, int plan) {
        int a=0;
        List<Integer> list1=new ArrayList<>();
        SQLiteDatabase db = getSQLiteDatabase(context);
        // 根据时间查询数据
        Cursor cursor = db.query("mapinfo", null, "plan=?", new String[]{plan + ""},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                a = Integer.parseInt(cursor.getString(cursor.getColumnIndex("point")));
                list1.add(a);
            } while (cursor.moveToNext());
            cursor.close();
        }
        removeDuplicate(list1);
        return list1;
    }

    public static int getplanNumber(Context context) {
        List<Integer> list1=new ArrayList<>();
        int a=0;
        SQLiteDatabase db = getSQLiteDatabase(context);
        // 根据时间查询数据
        Cursor cursor = db.query("mapinfo", null, null, null,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                a = Integer.parseInt(cursor.getString(cursor.getColumnIndex("plan")));
                list1.add(a);
            } while(cursor.moveToNext());
            cursor.close();
        }
        removeDuplicate(list1);
        return list1.size();
    }

    public static List<Integer> getplans(Context context) {
        List<Integer> list1=new ArrayList<>();
        int a=0;
        SQLiteDatabase db = getSQLiteDatabase(context);
        // 根据时间查询数据
        Cursor cursor = db.query("mapinfo", null, null, null,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                a = Integer.parseInt(cursor.getString(cursor.getColumnIndex("plan")));
                list1.add(a);
            } while(cursor.moveToNext());
            cursor.close();
        }
        return removeDuplicate(list1);
    }

    public   static   List  removeDuplicate(List list)   {
        HashSet h  =   new  HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

}
