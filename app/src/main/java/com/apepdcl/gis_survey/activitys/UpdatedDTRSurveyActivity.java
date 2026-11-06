package com.apepdcl.gis_survey.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.apepdcl.gis.app.R;

public class UpdatedDTRSurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_dtrsurvey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LT Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent playIntent = new Intent(UpdatedDTRSurveyActivity.this, DTRSelectActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();*/
                onBackPressed();

            }
        });
    }
}