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

import java.util.List;

public class history extends AppCompatActivity {
    HistoryDB historyDB = new HistoryDB(this, null, null, 1);
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        final List<String> history = historyDB.dataBaseToString();
        if(history.size()>0) {
            ArrayAdapter myDapter = new  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, history);
            ListView myList = (ListView) findViewById(R.id.listViewHistory);
            myList.setAdapter(myDapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = history.get(position);
                    Intent intent = new Intent (view.getContext(), MainActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    finish();
                }
            });
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}