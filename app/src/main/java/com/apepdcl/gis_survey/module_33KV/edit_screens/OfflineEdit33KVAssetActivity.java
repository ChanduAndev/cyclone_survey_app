package com.apepdcl.gis_survey.module_33KV.edit_screens;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.module_11KV.edit_screens.OfflineEdit11KVAssetActivity;
import com.apepdcl.gis_survey.module_33KV.ArcGISDatabase;
import com.apepdcl.gis_survey.module_33KV.vo.SurveyVo;
import com.apepdcl.gis_survey.utilits.GpsTraker;

import java.util.ArrayList;

public class OfflineEdit33KVAssetActivity extends AppCompatActivity implements Edit33KVAdapterAssetMapping.OnItemClickListener {
    private ProgressDialog progressDialog;
    Edit33KVAdapterAssetMapping adapter;
    ArrayList<SurveyVo> surveyVos = new ArrayList<>();
    private RecyclerView edit_rv;

    private RelativeLayout back_layout, close_layout;

    private ArcGISDatabase arcGISDatabase;

    GpsTraker gpsTraker;
    private String FEEDER_ID = "";
    public static String POLE_NO = "", LAT = "", LAN = "", FEEDER_ID_ASSET = "", ID = "", FROM_TYPE = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_33kv_edit_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edit_rv = findViewById(R.id.edit_rv);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        arcGISDatabase = new ArcGISDatabase(this);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        gpsTraker = new GpsTraker(OfflineEdit33KVAssetActivity.this);
        gpsTraker.getLocation();
        FEEDER_ID = getIntent().getStringExtra("FEEDER_ID");
        surveyVos = arcGISDatabase.getSurveyDataByDistance(
                gpsTraker.getLatitude(),
                gpsTraker.getLongitude(),
                FEEDER_ID
        );
        setRecyclerView();
    }

    private void setRecyclerView() {
        edit_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        edit_rv.setLayoutManager(layoutManager);
        edit_rv.setFocusable(false);
        adapter = new Edit33KVAdapterAssetMapping(surveyVos, this, this);
        edit_rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, SurveyVo model) {
        showCustomEditDialog(model);
    }

    @Override
    public void onUpdateLocationClick(int pos, SurveyVo model) {
        POLE_NO = String.valueOf(model.getPole_no());
        LAT = model.getLat();
        LAN = model.getLan();
        FEEDER_ID_ASSET = model.getFeeder_id();
        ID = model.getID();
        FROM_TYPE = "OFFLINE_33KV";
        getOnBackPressedDispatcher().onBackPressed();
    }

    private void editSourcePole(SurveyVo model) {
        Intent intent = new Intent(this, Edit33KVPoleAttributesAsset.class);
        intent.putExtra("FEEDER_ID", model.getFeeder_id());
        intent.putExtra("POLE_NO", model.getPole_no());
        intent.putExtra("DATA_MODEL", model);
        intent.putExtra("FROM", "SOURCE");
        startActivity(intent);

    }

    private void editPoleAttributes(SurveyVo model) {
        Intent intent = new Intent(this, Edit33KVPoleAttributesAsset.class);
        intent.putExtra("FEEDER_ID", model.getFeeder_id());
        intent.putExtra("POLE_NO", model.getPole_no());
        intent.putExtra("DATA_MODEL", model);
        intent.putExtra("FROM", "POLE");
        startActivity(intent);

    }

    private void showCustomEditDialog(SurveyVo model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_options_asset, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();


        // Find views
        LinearLayout optionAll = dialogView.findViewById(R.id.option_all);
        LinearLayout optionPole = dialogView.findViewById(R.id.option_pole);
        LinearLayout optionDTR = dialogView.findViewById(R.id.option_source);
        LinearLayout optionSourcePole = dialogView.findViewById(R.id.option_source_pole);
        LinearLayout option_boq = dialogView.findViewById(R.id.option_boq);
        LinearLayout option_httvr = dialogView.findViewById(R.id.option_httvr);

        optionPole.setOnClickListener(v -> {
            editPoleAttributes(model);
            dialog.dismiss();
        });

        optionSourcePole.setOnClickListener(v -> {
            editSourcePole(model);
            dialog.dismiss();
        });


        dialog.show();
    }
}
