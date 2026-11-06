package com.apepdcl.gis_survey.estimates.sou;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apepdcl.gis.app.R;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        url=getIntent().getStringExtra("URL");
        castingViews();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void castingViews() {
        webView = findViewById(R.id.web_view);
        // Enable JavaScript (optional but often required for modern sites)
        webView.getSettings().setJavaScriptEnabled(true);

        // Ensure links open inside WebView instead of default browser
        webView.setWebViewClient(new android.webkit.WebViewClient());

        // Load your URL
        webView.loadUrl(url);
    }
}