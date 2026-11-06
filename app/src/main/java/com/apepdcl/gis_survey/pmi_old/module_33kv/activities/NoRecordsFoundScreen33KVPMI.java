package com.apepdcl.gis_survey.pmi_old.module_33kv.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;

public class NoRecordsFoundScreen33KVPMI extends AppCompatActivity {
    TextView no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_records_found_screen33_kvpmi);
        no_data=findViewById(R.id.no_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Defects Noticed Under PMI-33KV");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(NoRecordsFoundScreen33KVPMI.this, PMIHomeActivity.class);
                startActivity(playIntent);
                finish();

            }
        });
    }
}