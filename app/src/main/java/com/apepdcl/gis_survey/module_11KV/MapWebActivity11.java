package com.apepdcl.gis_survey.module_11KV;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis.app.R;

public class MapWebActivity11 extends AppCompatActivity {

    String FEEDER_CODE;
    WebView webView;
    RelativeLayout iv_back, close_layout;
    String LOAD_URL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_web_activity);
        getSupportActionBar().hide();

        FEEDER_CODE = getIntent().getStringExtra("FEEDER_CODE");
        System.out.println("FEEDER_CODE : "+FEEDER_CODE);



        //LOCAL
      //  LOAD_URL = "http://10.150.250.20:8080/GIS/gisMapJHCae_mobile?feederId="+FEEDER_CODE+"&feeder_name=11KV%20BHIMADOLE%20TOWN%2024%20HRS&selectedOption=ASSIGNED&radioVal=JHC-ASS&mapfrom=dash";

        //PRE_PRODUCTION
       // LOAD_URL = "https://pregis.apeasternpower.com/gisMapJHCae_mobile?feederId="+FEEDER_CODE+"&feeder_name=11KV%20MAMIDIPALLI%20RURAL&selectedOption=ASSIGNED&radioVal=JHC-ASS&mapfrom=dash";

        //LIVE
        LOAD_URL = "https://gis.apeasternpower.com/gisMapJHCae_mobile?feederId="+FEEDER_CODE+"&feeder_name=11KV%20MAMIDIPALLI%20RURAL&selectedOption=ASSIGNED&radioVal=JHC-ASS&mapfrom=dash";


        System.out.println("LOAD_URL : "+LOAD_URL);

        iv_back = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);

        webView = findViewById(R.id.web_view);
        webView.loadUrl(LOAD_URL);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setGeolocationEnabled(true); // Enable geolocation

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false); // Allow geolocation requests
            }
        });

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.setWebViewClient(new WebViewClient());

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
