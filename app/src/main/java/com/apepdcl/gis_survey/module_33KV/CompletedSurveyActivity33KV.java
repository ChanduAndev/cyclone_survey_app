package com.apepdcl.gis_survey.module_33KV;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletedSurveyActivity33KV extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button btn_next,btn_web;
    private Spinner sp_cmpssname,sp_cmpfeedername;
    private String ssName,feederName,feederId,maxCount,sessionId;
    private ArcGISDatabase arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_survey_activity33_kv);
        getSupportActionBar().hide();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("33kV Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sp_cmpssname=findViewById(R.id.sp_cmpssname);
        sp_cmpfeedername=findViewById(R.id.sp_cmpfeedername);
        sp_cmpssname.setOnItemSelectedListener(this);
        sp_cmpfeedername.setOnItemSelectedListener(this);
        arcGISDatabase=new ArcGISDatabase(this);

        ehtName=new ArrayList<>();
        //ehtName.add("Select");
        ehtName=arcGISDatabase.getCmpEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(CompletedSurveyActivity33KV.this,
                android.R.layout.simple_dropdown_item_1line,ehtName);
        sp_cmpssname.setAdapter(ehtSSadapter);



        btn_next=(Button)findViewById(R.id.btn_next);
        btn_web=(Button)findViewById(R.id.btn_web);
        progressDialog=new ProgressDialog(CompletedSurveyActivity33KV.this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId=preferences.getString(MobileConstants.SESSION_ID,"");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(CompletedSurveyActivity33KV.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if("Select".equals(ssName)){
                    Toast.makeText(CompletedSurveyActivity33KV.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(CompletedSurveyActivity33KV.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else {
                    if (ConnectivityChecking.checkConnection(CompletedSurveyActivity33KV.this)) {
                        // getSurveyData();

                        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName));

                        String[] feederDta = feederName.split("-");
                        String feederCode = feederDta[0];
                        Intent intent = new Intent(CompletedSurveyActivity33KV.this, CompletedMapActivity33KV.class);
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

        btn_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if("Select".equals(ssName)){
                    Toast.makeText(CompletedSurveyActivity33KV.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(CompletedSurveyActivity33KV.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else {
                    if (ConnectivityChecking.checkConnection(CompletedSurveyActivity33KV.this)) {
                        // getSurveyData();

                        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName));

                        String[] feederDta = feederName.split("-");
                        String feederCode = feederDta[0];
                        Intent intent = new Intent(CompletedSurveyActivity33KV.this, MapWebActivity33.class);
                        intent.putExtra("FEEDER_CODE",feederCode);
                        startActivity(intent);


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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompletedSurveyActivity33KV.this,
                            android.R.layout.simple_dropdown_item_1line, feederNameList);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompletedSurveyActivity33KV.this,
                            android.R.layout.simple_dropdown_item_1line,feederNameList);
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
    public void onNothingSelected(AdapterView<?> parent) {

    }
}