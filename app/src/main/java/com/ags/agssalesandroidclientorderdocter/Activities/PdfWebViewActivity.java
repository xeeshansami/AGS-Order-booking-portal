package com.ags.agssalesandroidclientorderdocter.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.OnConnectionCallback;
import com.ags.agssalesandroidclientorderdocter.Utils.Utils;
import com.ags.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;

public class PdfWebViewActivity extends AppCompatActivity {
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_web_view);
        utils=new Utils(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setSubtitle("AGS - Manual Flow");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pdfNetCheck();
    }
    public void pdfNetCheck() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    loadPDF();
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    utils.alertBox(PdfWebViewActivity.this, "Internet Connections", "Poor connection, please check your internet connection", "ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            view.dismiss();
                        }
                    });
                }
            }).execute();
        } else {
            utils.alertBox(this, "Internet Connections", "Network not available please check", "Setting", "Cancel", "Exit", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    view.dismiss();
                }
            }, new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    finish();
                    view.dismiss();
                }
            });
        }
    }
    public void loadPDF(){
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // Stop webview from launching a browser when redirecting a url
        webView.setWebViewClient(new webViewCallBack());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setUserAgentString(DEFAULT_USER_AGENT);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setAppCachePath("");
        webSettings.setDomStorageEnabled(true);
        webView.clearCache(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setSaveFormData(true);
        String pdf = "http://agssukkur.com/download/ags_android_mobile.pdf";
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);
    }

    public void closePdfWebView(View view) {
        finish();
    }

    private class webViewCallBack extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }
}
