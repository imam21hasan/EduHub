package com.example.eduhub;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NoticeActivity extends AppCompatActivity {

    private WebView noticeWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notice);

        noticeWeb=findViewById(R.id.noticeWebView);

        WebSettings webSettings=noticeWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        noticeWeb.setWebViewClient(new WebViewClient());

        noticeWeb.loadUrl("https://www.cstu.ac.bd/notices");



    }
}