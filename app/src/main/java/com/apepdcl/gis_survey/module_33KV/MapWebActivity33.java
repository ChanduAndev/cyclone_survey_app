package com.apepdcl.gis_survey.module_33KV;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apepdcl.gis.app.R;

public class MapWebActivity33 extends AppCompatActivity {

    String FEEDER_CODE;
    WebView webView;
    ImageView iv_back, close_layout;
    String LOAD_URL,LOAD_URL_PRE_PROD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_web33);
        getSupportActionBar().hide();

        FEEDER_CODE = getIntent().getStringExtra("FEEDER_CODE");
        System.out.println("FEEDER_CODE : "+FEEDER_CODE);



        //LOCAL
        //  LOAD_URL = "http://10.150.250.20:8080/GIS/gisMapJHCae_mobile?feederId="+FEEDER_CODE+"&feeder_name=11KV%20BHIMADOLE%20TOWN%2024%20HRS&selectedOption=ASSIGNED&radioVal=JHC-ASS&mapfrom=dash";

        //PRE_PRODUCTION
         LOAD_URL_PRE_PROD = "https://pregis.apeasternpower.com/gisMapAERdss_mobile?feederId="+FEEDER_CODE+"&feeder_name=DENDULURU&radioVal=JHC&type=33KV";

        //LIVE
        LOAD_URL = "https://gis.apeasternpower.com/gisMapAERdss_mobile?feederId="+FEEDER_CODE+"&feeder_name=DENDULURU&radioVal=JHC&type=33KV";


        System.out.println("LOAD_URL : "+LOAD_URL);

        iv_back = findViewById(R.id.back_layout);
       // close_layout = findViewById(R.id.close_layout);

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