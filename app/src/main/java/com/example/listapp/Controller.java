package com.example.listapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Stack;

public class Controller {
    Activity activity;
    Stack<String> history;
    String url;
    WebView myWebViw;

    public Controller(Activity activity){
        this.activity = activity;
        this.history = new Stack<>();
        setupHomeScreen();
    }

    private void setupHomeScreen(){
        activity.setContentView(R.layout.activity_main);

        EditText input = (EditText) activity.findViewById(R.id.urlinput);

        WebViewClient myWebViewClient = new WebViewClient();
        myWebViw = (WebView) activity.findViewById(R.id.webview);
        WebSettings webSettings = myWebViw.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebViw.setWebViewClient(myWebViewClient);

        input.setOnKeyListener((v, keyCode, event) ->{
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Log.d("INPUT", "url entered: " + input.getText());
                // Perform action on key press
                url = "https://www." + input.getText();

                history.push(url);
                myWebViw.loadUrl(url);
                input.setText("");
                return true;
            }
                return false;

        });

        Button button = (Button) this.activity.findViewById(R.id.button);
        button.setOnClickListener((view -> {
            if (myWebViw.canGoBack()){
                myWebViw.goBack();
            }
        }));
    }

    public void onBackPress(){
        Log.d("ONBACK","Loading previous site if exists");
        if (myWebViw.canGoBack()){
            myWebViw.goBack();
        } else {
            Toast.makeText(activity, activity.getString(R.string.shut_down), Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

}
