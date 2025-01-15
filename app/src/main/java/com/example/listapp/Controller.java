package com.example.listapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.webkit.WebSettingsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Stack;

public class Controller {
    Activity activity;
    Stack<String> history;
    String url;
    WebView myWebView;
    EditText urlInput;
    ImageView clearUrl;
    ProgressBar progressBar;
    ImageView btnBack, btnForward, btnRefresh, btnHistory, btnMore, btnBookmark, btnShare,btnHomepage;
    boolean dark = false;
    BookmarkDB bookmarkDB;
    HistoryDB historyDB;
    WebHistoryItem webHistoryItem;
    String stopLoading;

    public Controller() {
    }

    public Controller(Activity activity){
        this.activity = activity;
        this.history = new Stack<>();
        setupHomeScreen();

    }

    private void setupHomeScreen() {
        activity.setContentView(R.layout.activity_main);
        urlInput = activity.findViewById(R.id.url_input);
        clearUrl = activity.findViewById(R.id.clear_icon);
        progressBar = activity.findViewById(R.id.progress_bar);
        myWebView = activity.findViewById(R.id.web_view);
        btnBack = activity.findViewById(R.id.web_back);
        btnForward = activity.findViewById(R.id.web_forward);
        btnRefresh = activity.findViewById(R.id.web_refresh);
        btnMore = activity.findViewById(R.id.more);
        btnBookmark = activity.findViewById(R.id.bookmark);
        btnShare = activity.findViewById(R.id.share);
        btnHomepage = activity.findViewById(R.id.homepage);
        bookmarkDB = new BookmarkDB(activity,null,null,1);
        historyDB = new HistoryDB(activity, null,null, 1);
        bookmarkDB.clearDatabase();
        historyDB.clearDatabaseHistory();
        String stopLoading = "check";



        WebViewClient myWebViewClient = new WebViewClient();
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        myWebView.setVerticalScrollBarEnabled(true);
        myWebView.setHorizontalScrollBarEnabled(true);
        myWebView.setWebViewClient(myWebViewClient);
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                urlInput.setText(myWebView.getUrl());

                String currLink = myWebView.getUrl();
                if(currLink != null && !currLink.equals(stopLoading)) {
                    saveDataHistory();
                }
            }
        });
        loadUrl("google.com");
        urlInput.setText(myWebView.getUrl());
        urlInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if( i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE) {
                    loadUrl(urlInput.getText().toString());
                    urlInput.setText(myWebView.getUrl());
                    return true;
                }
                return false;
            }
        });
        clearUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlInput.setText("");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(myWebView.canGoBack()) {
                    myWebView.goBack();
                    urlInput.setText(myWebView.getUrl());
                }
            }
        });
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(myWebView.canGoForward()) {
                    myWebView.goForward();
                    urlInput.setText(myWebView.getUrl());
                }
            }
        });
        btnHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl("google.com/");
                urlInput.setText(myWebView.getUrl());
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myWebView.reload();
                urlInput.setText(myWebView.getUrl());
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BottomSheetDialog sheetDialog = new BottomSheetDialog(activity,
                        R.style.Theme_ListApp);
                View sheetView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.reorderdialog,
                        (LinearLayout) activity.findViewById(R.id.dialog_container));
                sheetView.findViewById(R.id.menucancel_icon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sheetDialog.dismiss();
                    }
                });
                sheetView.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "refresh", Toast.LENGTH_SHORT).show();
                    }
                });
                sheetView.findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent historyIntent = new Intent(activity, history.class);
                        activity.startActivity(historyIntent);
                    }
                });
                sheetView.findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "Setting", Toast.LENGTH_SHORT).show();
                    }
                });
                sheetView.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookMark = new Intent(activity, Bookmark.class);
                        activity.startActivity(bookMark);
                    }
                });
                sheetView.findViewById(R.id.lightDark).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(dark) {
                           WebSettingsCompat.setForceDark(myWebView.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                           dark = true;
                       } else {
                           WebSettingsCompat.setForceDark(myWebView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                           dark = true;
                       }
                    }
                });
                sheetView.findViewById(R.id.sharelink).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "Share", Toast.LENGTH_SHORT).show();
                    }
                });
                sheetDialog.setContentView(sheetView);
                sheetDialog.show();
            }
        });
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBookPressed();
                Toast.makeText(activity, "Page Added in Bookmark", Toast.LENGTH_SHORT).show();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, myWebView.getUrl());
                intent.setType("text/plain");
                activity.startActivity(intent);
            }
        });

    }

    public void loadUrl(String url) {
        boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
        if(matchUrl) {
            myWebView.loadUrl(url);
        }else {
            myWebView.loadUrl("google.com/search?q="+url);
        }
    }
    public void saveData() {
        Website website = new Website( myWebView.getUrl() );
        bookmarkDB.addUrl(website);
    }
    public void saveDataHistory() {
        Website websites = new Website( myWebView.getUrl() );
        historyDB.addUrl(websites);
    }
    public void onBookPressed() {

        Website web = new Website(myWebView.getUrl());
        bookmarkDB.addUrl(web);
      //  Log.d("bookpressed", "clicked");
        saveData();
    }
    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            urlInput.setText(myWebView.getUrl());
            progressBar.setVisibility(WebView.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(WebView.INVISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }

}

