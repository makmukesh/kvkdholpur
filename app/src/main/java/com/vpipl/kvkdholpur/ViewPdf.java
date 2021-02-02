package com.vpipl.kvkdholpur;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


public class ViewPdf extends Activity {

    LinearLayout viewepaper_back;
    String pdfurl;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        try
        {
            Intent intent = getIntent();
            pdfurl = intent.getStringExtra("pdfurl");
           /* viewepaper_back = (LinearLayout)findViewById(R.id.viewepaper_back);
            viewepaper_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });*/

            /*getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
            WebView htmlWebView = (WebView)findViewById(R.id.webView_viewGenealogy);
            htmlWebView.setWebViewClient(new CustomWebViewClient());
            WebSettings webSetting = htmlWebView.getSettings();
            webSetting.setJavaScriptEnabled(true);
            webSetting.setDisplayZoomControls(true);
            htmlWebView.loadUrl(""+pdfurl);*/

            WebView webView = (WebView) findViewById(R.id.webView_viewGenealogy);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(pdfurl);
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
    }

}
