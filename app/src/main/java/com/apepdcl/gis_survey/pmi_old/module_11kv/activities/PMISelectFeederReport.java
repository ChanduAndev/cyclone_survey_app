package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.module_11KV.ArcGISDatabase;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;

import java.util.List;

public class PMISelectFeederReport extends AppCompatActivity {

    private Button btn_next;
    private Spinner sp_ssname1,sp_feedername1;
    private String ssName,feederName,feederId,maxCount,sessionId,feederCode,ssCode,subName,feedeNameList;
    private ArcGISDatabase arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private String FEEDER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_feeder_report);
    }
}