package com.example.listapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BookmarkDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bookmark.db";
    private static final String TABLE_BOOKMARKS = "bookmark";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "url";

    public BookmarkDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_BOOKMARKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKS);
        onCreate(db);
    }

    public void addUrl(Website website) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, website.getUrl());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_BOOKMARKS, null, values);

        List<String> list_bookmarks = dataBaseToString();
        Log.d("Size", Integer.toString(list_bookmarks.size()));
        for(int i =0; i < list_bookmarks.size(); i++) {
            Log.d("List", list_bookmarks.get(i));

        }
        db.close();
        Log.d("add url", "Click");
    }

    public void deleteUrl(String urlName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BOOKMARKS + " WHERE " + COLUMN_NAME + "=\"" + urlName + "\";");
        db.close();
    }

    public List<String> dataBaseToString() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKMARKS;
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
    public void clearDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_BOOKMARKS;
        db.execSQL(clearDBQuery);
    }
}
