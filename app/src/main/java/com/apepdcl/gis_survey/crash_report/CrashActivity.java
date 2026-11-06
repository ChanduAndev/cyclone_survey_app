package com.apepdcl.gis_survey.crash_report;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis_survey.DashBoard;



/**
 * Created by SOUBHAGYA SWAIN on 07-08-2026.
 */

public class CrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);

        TextView text = new TextView(this);
        text.setPadding(30,30,30,30);

        String report = getIntent().getStringExtra("report");

        text.setText(report);

        scroll.addView(text);

        Button share = new Button(this);
        share.setText("Share Crash Report");

        share.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");

            intent.putExtra(Intent.EXTRA_TEXT, report);

            startActivity(Intent.createChooser(intent,"Share"));
        });

        Button copy = new Button(this);
        copy.setText("Copy");

        copy.setOnClickListener(v->{

            ClipboardManager manager =
                    (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

            manager.setPrimaryClip(
                    ClipData.newPlainText("Crash",report)
            );
        });

        Button restart = new Button(this);
        restart.setText("Restart App");

        restart.setOnClickListener(v->{

            Intent i = new Intent(this, DashBoard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

            finish();
        });

        android.widget.LinearLayout layout =
                new android.widget.LinearLayout(this);

        layout.setOrientation(android.widget.LinearLayout.VERTICAL);

        layout.addView(scroll,
                new android.widget.LinearLayout.LayoutParams(
                        -1,0,1));

        layout.addView(share);
        layout.addView(copy);
        layout.addView(restart);

        setContentView(layout);
    }
}
