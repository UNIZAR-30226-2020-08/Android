package com.app.guinote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String TABLE_CREATE = "CREATE TABLE auth(_id " +
            "INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT," +
            "copas TEXT, f_carta TEXT,f_tapete TEXT,f_perfil TEXT, token BLOB)";
    private static final String DB_NAME = "auth.sqlite";

    private static final int DB_VERSION = 1;

    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}