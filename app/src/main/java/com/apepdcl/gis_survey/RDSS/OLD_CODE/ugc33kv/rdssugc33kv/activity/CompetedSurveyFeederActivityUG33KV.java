package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.CompletedMapActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompetedSurveyFeederActivityUG33KV extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_next;
    private Spinner sp_cmpssname,sp_cmpfeedername;
    private String ssName,feederName,feederId,maxCount,sessionId;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedsuervey_feeder_ugc_33kv);
        sp_cmpssname=findViewById(R.id.sp_cmpssname);
        sp_cmpfeedername=findViewById(R.id.sp_cmpfeedername);

       // getSupportActionBar().setTitle("RDSS 33kV UG Cable Survey");
        sp_cmpssname.setOnItemSelectedListener(this);
        sp_cmpfeedername.setOnItemSelectedListener(this);
        arcGISDatabase=new ArcGISDatabaseUG33KV(this);
/*
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("11KVPole Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PoleSurveyActivity.this, SurveyDataActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });*/



        ehtName=new ArrayList<>();
        //ehtName.add("Select");
        ehtName=arcGISDatabase.getCmpEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(CompetedSurveyFeederActivityUG33KV.this,
                android.R.layout.simple_spinner_item,ehtName);
        sp_cmpssname.setAdapter(ehtSSadapter);



        btn_next=(Button)findViewById(R.id.btn_next);
        progressDialog=new ProgressDialog(CompetedSurveyFeederActivityUG33KV.this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId=preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS,"");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(CompetedSurveyFeederActivityUG33KV.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if("Select".equals(ssName)){
                    Toast.makeText(CompetedSurveyFeederActivityUG33KV.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(CompetedSurveyFeederActivityUG33KV.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else {
                    if (ConnectivityCheckingUG33KV.checkConnection(CompetedSurveyFeederActivityUG33KV.this)) {
                       // getSurveyData();

                       // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName));

                        String[] feederDta = feederName.split("-");
                        String feederCode = feederDta[0];
                        Intent intent = new Intent(CompetedSurveyFeederActivityUG33KV.this, CompletedMapActivityUG33KV.class);
                        intent.putExtra("FEEDER_CODE",feederCode);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }


                    /*Intent intent = new Intent(MainActivity.this, PoleSurveyActivity.class);
                    intent.putExtra("SSNAME", ssName);
                    intent.putExtra("FEEDER_NAME", feederName);
                    startActivity(intent);
                    finish();*/
                }
            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.sp_cmpssname:
                feederNameList=new ArrayList<>();
                ssName=sp_cmpssname.getSelectedItem().toString();
                if(!"Select".equalsIgnoreCase(ssName)) {


                    String[] ss = ssName.split("-");
                    String ssCode = ss[0]; // 004
                    //String ssName = ss[1];
                    //Collections.reverse(feederNameList);
                    feederNameList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM CMPEHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                    feederNameList.add("Select");
                    Collections.reverse(feederNameList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompetedSurveyFeederActivityUG33KV.this,
                            android.R.layout.simple_spinner_item, feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);
                    //Collections.reverse(feederNameList);
                    /*feederNameList = arcGISDatabase.getComplaint(String.format("SELECT FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_NAME='%s'",ssName));
                    feederNameList.add("Select");
                    Collections.reverse(feederNameList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompetedSurveyFeederActivity.this,
                            android.R.layout.simple_spinner_item,feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);*/
                }else {
                    feederNameList.add("Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompetedSurveyFeederActivityUG33KV.this,
                            android.R.layout.simple_spinner_item,feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);
                }
                break;
            case R.id.sp_cmpfeedername:
                feederName=sp_cmpfeedername.getSelectedItem().toString();

                break;

                default:

                    break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CompetedSurveyFeederActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
}
