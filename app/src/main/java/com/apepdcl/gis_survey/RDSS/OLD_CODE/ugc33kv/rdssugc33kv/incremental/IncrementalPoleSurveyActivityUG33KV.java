package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.incremental;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.FeederMasterActivityUG33KV;

import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyResponseDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.TruncDataRequestVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncrementalPoleSurveyActivityUG33KV extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Button btn_polenext,btn_taping;
    private Spinner sp_loadmap,sp_tappingpoint;
    private String loadMap,ssName,feederName,feederCode,tappingpoint,maxCount,userName,sessionId,work_order;
    private TextView tv_surveyedpole;
    // private TextInputEditText edt_frompole,edt_topole;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<SurveyVoUG33KV> surveyVoUG33KVList;
    private int surveyedCount=0;
    private ProgressDialog progressDialog;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private List<String> poleNumList;
    private LinearLayout lyt_tapingpoint,lyt_surveytype;
    private int flag=0;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incrementalpolesurvey_ugc_33kv);
        try {
            sp_loadmap = (Spinner) findViewById(R.id.sp_loadmap);
            sp_tappingpoint = (Spinner) findViewById(R.id.sp_tappingpoint);
            tv_surveyedpole = findViewById(R.id.tv_surveyedpole);
            lyt_tapingpoint = (LinearLayout) findViewById(R.id.lyt_tapingpoint);
            lyt_surveytype = (LinearLayout) findViewById(R.id.lyt_surveytype);
            btn_polenext = (Button) findViewById(R.id.btn_polenext);
            btn_taping = (Button) findViewById(R.id.btn_taping);
            lyt_tapingpoint.setVisibility(View.GONE);
            btn_taping.setVisibility(View.GONE);
            btn_polenext.setOnClickListener(this);
            btn_taping.setOnClickListener(this);
            getSupportActionBar().setTitle("33Kv IncrementalLineSurvey");

            Intent intent = getIntent();
            ssName = intent.getStringExtra("SSNAME");
            feederName = intent.getStringExtra("FEEDER_NAME");
            feederCode = intent.getStringExtra("FEEDER_CODE");
            work_order = intent.getStringExtra("WORKORDER");
            maxCount= intent.getStringExtra("MAX_COUNT");

            progressDialog = new ProgressDialog(IncrementalPoleSurveyActivityUG33KV.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);

            arcGISDatabase = new ArcGISDatabaseUG33KV(IncrementalPoleSurveyActivityUG33KV.this);
            surveyVoUG33KVList = arcGISDatabase.getSurveyData("");
            surveyedCount = arcGISDatabase.getObjectId();
            poleNumList = new ArrayList<>();

            // tv_surveyedpole.setText(String.valueOf(surveyedCount));
            if (maxCount == null) {
                tv_surveyedpole.setText("0");
            } else {
                tv_surveyedpole.setText(maxCount);
            }

            sp_loadmap.setOnItemSelectedListener(this);
            sp_tappingpoint.setOnItemSelectedListener(this);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstantsUG33KV.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(IncrementalPoleSurveyActivityUG33KV.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }





    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.sp_loadmap:
                loadMap=sp_loadmap.getSelectedItem().toString();
                if("Through Line".equalsIgnoreCase(loadMap)){
                    lyt_tapingpoint.setVisibility(View.GONE);
                    btn_polenext.setVisibility(View.VISIBLE);
                    // lyt_surveytype.setVisibility(View.GONE);
                    btn_taping.setVisibility(View.GONE);
                    sp_tappingpoint.setSelection(0);
                }
                break;

            case R.id.sp_tappingpoint:
                tappingpoint=sp_tappingpoint.getSelectedItem().toString();
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
        Intent intent = new Intent(IncrementalPoleSurveyActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
    public void getTappingPointData(){
        try {
            LastSurveyRequestVoUG33KV lastSurveyRequestVoUG33KV = new LastSurveyRequestVoUG33KV();
            //lastSurveyRequestVo.setFEEDER_ID(feederCode);
            lastSurveyRequestVoUG33KV.setFEEDER_ID(feederCode);
            lastSurveyRequestVoUG33KV.setUSER_ID(userName);
            lastSurveyRequestVoUG33KV.setVERSION(MobileConstantsUG33KV.APP_VERSION);
            lastSurveyRequestVoUG33KV.setSESSION_ID(sessionId);
            lastSurveyRequestVoUG33KV.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(IncrementalPoleSurveyActivityUG33KV.this));
            Gson gson=new Gson();
            String json=gson.toJson(lastSurveyRequestVoUG33KV);
            Log.v("DATA===>>", "" + json);

            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<LastSurveyResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.getTappingData(lastSurveyRequestVoUG33KV);
            responseVoCall.enqueue(new Callback<LastSurveyResponseVoUG33KV>() {
                @Override
                public void onResponse(Call<LastSurveyResponseVoUG33KV> call, Response<LastSurveyResponseVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        LastSurveyResponseVoUG33KV response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            // arcGISDatabase.clearSurveyedData();
                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this, "No Tapping Poles Found", Toast.LENGTH_SHORT).show();

                            } else {
                                List<LastSurveyResponseDataVoUG33KV> masterResponseDataVos = response_vo.getRESPONSE();
                                for (LastSurveyResponseDataVoUG33KV vo : masterResponseDataVos) {
                                    poleNumList.add(vo.getPOLE_NO() + "-" + vo.getTAPPING_LOCNAME());
                                }
                                btn_polenext.setVisibility(View.GONE);
                                // lyt_surveytype.setVisibility(View.GONE);
                                btn_taping.setVisibility(View.VISIBLE);
                                lyt_tapingpoint.setVisibility(View.VISIBLE);
                                poleNumList.add("Select");
                                Collections.reverse(poleNumList);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalPoleSurveyActivityUG33KV.this,
                                        android.R.layout.simple_spinner_dropdown_item, poleNumList);
                                sp_tappingpoint.setAdapter(adapter);
                            }
                        } else {
                            // Toast.makeText(PoleSurveyActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), PoleSurveyActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IncrementalPoleSurveyActivityUG33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstantsUG33KV.APK_DOWNLOAD_LINK);
                                                            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                                                            startActivity(intent);
                                                        }
                                                    })

                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_polenext:
                if (ConnectivityCheckingUG33KV.checkConnection(IncrementalPoleSurveyActivityUG33KV.this)) {
                    getData();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.btn_taping:
                if (ConnectivityCheckingUG33KV.checkConnection(IncrementalPoleSurveyActivityUG33KV.this)) {
                    if(!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                        getTapingPolesData();
                    }else {
                        Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this,"Select Tapping Point",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    public void getData(){
        if("Select".equals(loadMap)){
            Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this,"Select Survey Type",Toast.LENGTH_LONG).show();
        }/*else if("Select".equals(loadMap)){
            Toast.makeText(PoleSurveyActivity.this,"Select Survey Type",Toast.LENGTH_LONG).show();
        }*/else {

            if ("Through Line".equals(loadMap)) {
                Intent intent = new Intent(IncrementalPoleSurveyActivityUG33KV.this, IncrementalArcGISMapActivityUG33KV.class);
                intent.putExtra("SSNAME", ssName);
                intent.putExtra("FEEDER_NAME", feederName);
                intent.putExtra("FEEDER_CODE", feederCode);
                intent.putExtra("MAX_COUNT",tv_surveyedpole.getText().toString());
                intent.putExtra("WORKORDER",work_order);
                startActivity(intent);
                finish();
            } else if("Spur Line".equalsIgnoreCase(loadMap)){
                // if(!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                getTappingPointData();
               /* }else {
                  Toast.makeText(PoleSurveyActivity.this,"Select Tapping Point",Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }
    public void getTapingPolesData(){
        try {

            TruncDataRequestVoUG33KV truncDataRequestVoUG33KV = new TruncDataRequestVoUG33KV();
            truncDataRequestVoUG33KV.setFEEDER_ID(feederCode);
            truncDataRequestVoUG33KV.setUSER_ID(userName);
            truncDataRequestVoUG33KV.setVERSION(MobileConstantsUG33KV.APP_VERSION);
            truncDataRequestVoUG33KV.setSESSION_ID(sessionId);
            truncDataRequestVoUG33KV.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(IncrementalPoleSurveyActivityUG33KV.this));

            //String ehtSS = tappingpoint;
            if (tappingpoint.contains("-")) {
                String[] poleNum = tappingpoint.split("-");
                String tapingPoleNum = poleNum[0]; // 004
                String ssName = poleNum[1];
                truncDataRequestVoUG33KV.setTRUCK_POLE_NO(Integer.parseInt(tapingPoleNum));
            } else {
                truncDataRequestVoUG33KV.setTRUCK_POLE_NO(Integer.parseInt(tappingpoint));
            }


            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<LastSurveyResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.getTrncPoleData(truncDataRequestVoUG33KV);
            responseVoCall.enqueue(new Callback<LastSurveyResponseVoUG33KV>() {
                @Override
                public void onResponse(Call<LastSurveyResponseVoUG33KV> call, Response<LastSurveyResponseVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        LastSurveyResponseVoUG33KV response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Intent intent = new Intent(IncrementalPoleSurveyActivityUG33KV.this, IncrementalArcGISMapActivityUG33KV.class);
                                intent.putExtra("SSNAME", ssName);
                                intent.putExtra("FEEDER_NAME", feederName);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                                intent.putExtra("TAPPING_POINT", tappingpoint);
                                intent.putExtra("SURVEY_TYPE", loadMap);
                                intent.putExtra("WORKORDER",work_order);
                                startActivity(intent);
                                startActivity(intent);
                                finish();
                            } else {
                                List<LastSurveyResponseDataVoUG33KV> masterResponseDataVos = response_vo.getRESPONSE();
                                arcGISDatabase.clearIncrementalSurveyedData();
                                for (LastSurveyResponseDataVoUG33KV vo : masterResponseDataVos) {
                                    IncrementalSurveyVoUG33KV surveyVo = new IncrementalSurveyVoUG33KV();
                                    surveyVo.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVo.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVo.setLat(vo.getGLAT());
                                    surveyVo.setLan(vo.getGLONG());
                                    surveyVo.setWorkOrder(vo.getWO_ID());
                                    surveyVo.setPastSurveyFlag("Y");
                                    arcGISDatabase.addIncrementalSurveyData(surveyVo);
                                }
                                Intent intent = new Intent(IncrementalPoleSurveyActivityUG33KV.this, IncrementalArcGISMapActivityUG33KV.class);
                                intent.putExtra("SSNAME", ssName);
                                intent.putExtra("FEEDER_NAME", feederName);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                                intent.putExtra("TAPPING_POINT", tappingpoint);
                                intent.putExtra("SURVEY_TYPE", loadMap);
                                intent.putExtra("WORKORDER",work_order);
                                startActivity(intent);
                                finish();

                            }
                        } else {
                            // Toast.makeText(PoleSurveyActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), PoleSurveyActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IncrementalPoleSurveyActivityUG33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstantsUG33KV.APK_DOWNLOAD_LINK);
                                                            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                                                            startActivity(intent);
                                                        }
                                                    })

                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(IncrementalPoleSurveyActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
