package com.apepdcl.gis_survey.PMI.rectification.offline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.PMI.models.OfflineDataFor11;

import java.util.ArrayList;

public class OfflineRectifySurvey extends AppCompatActivity implements OfflineSurveyAdapter.OnItemClickListener {
    private OfflineSurveyAdapter adapter;
    private ArrayList<OfflineDataFor11> model;
    private RecyclerView rv_11;
    private String SELECTED_FEEDER_ID = "", SELECTED_VOLTAGE_LEVEL = "";
    private RelativeLayout logout_layout, profile_layout;
    PmiDatabase pmiDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_11_kv_feeder_rectify);
        pmiDatabase = new PmiDatabase(OfflineRectifySurvey.this);
        rv_11 = findViewById(R.id.rv_11);
        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);
        rv_11.setLayoutManager(new LinearLayoutManager(this));
        SELECTED_FEEDER_ID = getIntent().getStringExtra("SELECTED_FEEDER_ID");
        SELECTED_VOLTAGE_LEVEL = getIntent().getStringExtra("SELECTED_VOLTAGE_LEVEL");
        model = pmiDatabase.getTotalOfflineDataToSurvey(SELECTED_FEEDER_ID);
        System.out.println("SIZE_OF_LIST::::::" + model.size() + " " + SELECTED_FEEDER_ID + " " + SELECTED_VOLTAGE_LEVEL);
        setOfflineAdapterData();
        profile_layout.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        logout_layout.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }


    private void setOfflineAdapterData() {
        adapter = new OfflineSurveyAdapter(OfflineRectifySurvey.this, model, OfflineRectifySurvey.this);
        rv_11.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(OfflineRectifySurvey.this, Select11PoleRectifyOffline.class);
        intent.putExtra("SELECTED_POLE_NO", model.get(position).getPOLE_NO());
        intent.putExtra("SELECTED_FEEDER_ID", model.get(position).getFEEDER_ID());
        intent.putExtra("SELECTED_VOLTAGE_LEVEL", SELECTED_VOLTAGE_LEVEL);
        startActivity(intent);
    }
}
