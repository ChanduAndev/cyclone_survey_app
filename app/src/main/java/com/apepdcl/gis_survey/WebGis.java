package com.apepdcl.gis_survey;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;

public class WebGis extends AppCompatActivity {
    String VOLTAGE_LEVEL;
    String URL;
    WebView webView;
    RelativeLayout iv_back, close_layout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_gis);
        VOLTAGE_LEVEL = getIntent().getStringExtra("VOLTAGE_LEVEL");
        if (VOLTAGE_LEVEL.equalsIgnoreCase("11KV")) {
            if (!LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                URL = "https://gisweb.apeasternpower.com/elevenKvGisMap2025";
            } else {
                URL = "https://gisweb.apeasternpower.com/Total11kv";

            }

        }
        if (VOLTAGE_LEVEL.equalsIgnoreCase("33KV")) {
            URL = "https://gisweb.apeasternpower.com/thirtyThreeKVPage";
        }
        if ("LT".equalsIgnoreCase(VOLTAGE_LEVEL)) {
            URL = "https://gisweb.apeasternpower.com/LTGisMap2025";
        }

        iv_back = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);

        webView = findViewById(R.id.web_view);
        webView.loadUrl(URL);
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
//        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://www.google.com/maps") ||
                        url.startsWith("geo:") ||
                        url.startsWith("intent:")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    } catch (Exception e) {
                        // If Google Maps app not installed, open in browser
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                    return true;
                }
                view.loadUrl(url);
                return false;
            }
        });


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
}
