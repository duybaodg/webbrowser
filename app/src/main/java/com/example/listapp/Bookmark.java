package com.example.listapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Bookmark extends AppCompatActivity {
    BookmarkDB bookmarkDB = new BookmarkDB(this, null, null, 1);
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookmark);
        final List<String> books = bookmarkDB.dataBaseToString();
        if(books.size()>0) {
            ArrayAdapter myDapter = new  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, books);
            ListView myList = (ListView) findViewById(R.id.listViewBookmark);
            myList.setAdapter(myDapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = books.get(position);
                    Intent intent = new Intent (view.getContext(), MainActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}