package com.nandity.djmaps.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lemon on 2016/10/12.
 */
public class SQLUtils {
    /**
     * 获取到SQLiteDatabase的实例
     */
    private static SQLiteDatabase getSQLiteDatabase(Context context) {
        SQLiteOpenHelper dbHelper = new MapSQLiteHelper(context, "dlmaps.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db;
    }

    private static void insertPoint(Context context, int plan, int point, String lo, String la) {
        SQLiteDatabase db = getSQLiteDatabase(context);
        ContentValues values = new ContentValues();
        values.put("plan", plan);
        values.put("point", point);
        values.put("lo", lo);
        values.put("la", la);
        long id = db.insert("mapinfo", null, values);
    }

    private static void deletePoint(Context context, int plan, int point) {
        SQLiteDatabase db = getSQLiteDatabase(context);
        ContentValues values = new ContentValues();
        int id =getID(context,plan,point);
        db.delete("mapinfo", "id = ?", new String[]{id+""});
    }

    private static void updatePoint(Context context, int plan, int point, String lo, String la) {
        SQLiteDatabase db = getSQLiteDatabase(context);
        ContentValues values = new ContentValues();
        values.put("lo", lo);
        values.put("la", la);
        int id =getID(context,plan,point);
        db.update("mapinfo", values, "id = ?", new String[]{id+""});
    }


    private static int getID(Context context, int plan, int point) {
        int id = 0;
        SQLiteDatabase db = getSQLiteDatabase(context);
        ContentValues values = new ContentValues();
        // 根据时间查询数据库
        Cursor cursor = db.query("mapinfo", null, "plan=?", new String[]{plan + ""},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int point1 = cursor.getInt(cursor.getColumnIndex("point"));
            while (cursor != null && point1 != point) {
                cursor.moveToNext();
            }
            id = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
        }
        return id;
    }

}
