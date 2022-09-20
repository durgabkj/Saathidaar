package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    WebView webview;
    ProgressDialog pDialog;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle bundle = getIntent().getExtras();
        data = bundle.getString("data");

        init();
        listener();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        pDialog = new ProgressDialog(WebViewActivity.this);
        pDialog.setTitle("Document");
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.OFF);
        webview.setScrollbarFadingEnabled(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.setInitialScale(1);
        webview.setHorizontalScrollBarEnabled(false);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        String url = "https://docs.google.com/gview?embedded=true&url="+data;

        if (url.endsWith(".pdf"))
        {
            webview.loadUrl(url);
        } else
        {
            webview.loadUrl(data);
        }

    }

    private void listener() {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });
    }
}