package com.example.listapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HistoryDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="siteHistory.db";
    private static final String TABLE_SITES= "siteHistory";
    private static final String COLUMN_ID= "id";
    private static final String COLUMN_NAME= "url";


    public HistoryDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_SITES+ "("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITES);
        onCreate(db);
    }
    public void addUrl(Website webView) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, webView.getUrl());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SITES, null, values);
        db.close();
    }
    public void deleteUrl(String urlName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM" + TABLE_SITES + "WHERE" + COLUMN_NAME+"=\""+urlName+"\";");
    }
    public List<String> dataBaseToString() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SITES;
        List<String> dbString = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.moveToFirst()) {
            do {
                int urlIndex = c.getColumnIndex(COLUMN_NAME);
                if (urlIndex != -1 && c.getString(urlIndex) != null) {
                    String bsString = c.getString(urlIndex);
                    dbString.add(bsString);
                }
            } while (c.moveToNext());
            c.close();
        }
        return dbString;
    }
    public void clearDatabaseHistory() {
        SQLiteDatabase db = getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_SITES;
        db.execSQL(clearDBQuery);
    }
}
