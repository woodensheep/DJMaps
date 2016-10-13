package com.nandity.djmaps.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lemon on 2016/10/12.
 */
public class MapSQLiteHelper extends SQLiteOpenHelper{

    public MapSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }
    public static final String CREATE_NEWS = "create table news ("
            + "id integer primary key autoincrement, "
            + "plan integer, "
            + "point integer, "
            + "lo text,"
            + "la text)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
