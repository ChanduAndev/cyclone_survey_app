package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginDataResponseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;

import java.util.List;

public class TransmitDataHomeActivityUG33KV extends AppCompatActivity implements View.OnClickListener {
    // private TextView tv_escount;
    private Button btn_transmit,btn_transmit_incremental_data;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<SurveyVoUG33KV> surveyVoUG33KVList;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private String SurveyFlag="";
    private SharedPreferences preferences;
    private String userName,sessionId;
    private List<LoginDataResponseUG33KV> dataResponseList;
    private List<FeederDataVoUG33KV> feederDataVoUG33KVS;
    private List<IncrementalSurveyVoUG33KV> incrementalSurveyVoUG33KVList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitdatahome_ugc_33kv);
        try {

            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit_incremental_data= (Button) findViewById(R.id.btn_transmit_incremental_data);
            btn_transmit.setOnClickListener(this);
            btn_transmit_incremental_data.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabaseUG33KV(this);
            surveyVoUG33KVList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            incrementalSurveyVoUG33KVList =arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            getSupportActionBar().setTitle("RDSS 33kV UG Cable Survey");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstantsUG33KV.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitDataHomeActivityUG33KV.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_transmit:
                if(surveyVoUG33KVList.size()>0) {
                    Intent intentPf=new Intent(TransmitDataHomeActivityUG33KV.this,TransmitDataActivityUG33KV.class);
                    startActivity(intentPf);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_transmit_incremental_data:
                if(incrementalSurveyVoUG33KVList.size()>0) {
                    Intent intentPf=new Intent(TransmitDataHomeActivityUG33KV.this, IncrementalTransmitDataActivityUG33KV.class);
                    startActivity(intentPf);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransmitDataHomeActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
}



