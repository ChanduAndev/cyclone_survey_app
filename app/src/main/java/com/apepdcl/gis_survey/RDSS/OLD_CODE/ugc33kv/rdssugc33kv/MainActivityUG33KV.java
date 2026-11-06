package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.Toast;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.FeederMasterActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.PoleSurveyActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyResponseDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyResponseSSDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SSMasterVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityUG33KV extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_next;
    private Spinner sp_ssname, sp_feedername;
    private String ssName, feederName, feederId, maxCount, sessionId, feederCode, ssCode, subName, feedeNameList;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ugc_33kv);
        sp_ssname = findViewById(R.id.sp_ssname);
        sp_feedername = findViewById(R.id.sp_feedername);
        // getSupportActionBar().setTitle("RDSS 33kV UG Cable Survey");
        sp_ssname.setOnItemSelectedListener(this);
        sp_feedername.setOnItemSelectedListener(this);
        arcGISDatabase = new ArcGISDatabaseUG33KV(this);
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


        ehtName = new ArrayList<>();
        //ehtName.add("Select");
        ehtName = arcGISDatabase.getEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(MainActivityUG33KV.this,
                android.R.layout.simple_spinner_dropdown_item, ehtName);
        sp_ssname.setAdapter(ehtSSadapter);


        btn_next = (Button) findViewById(R.id.btn_next);
        progressDialog = new ProgressDialog(MainActivityUG33KV.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId = preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS, "");
        if (sessionId == null || sessionId.isEmpty()) {
            Intent intent = new Intent(MainActivityUG33KV.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("Select".equals(ssName)) {
                    Toast.makeText(MainActivityUG33KV.this, "Select SS Name", Toast.LENGTH_LONG).show();
                } else if ("Select".equalsIgnoreCase(feederName)) {
                    Toast.makeText(MainActivityUG33KV.this, "Select Feeder Name", Toast.LENGTH_LONG).show();
                } else {
                    if (ConnectivityCheckingUG33KV.checkConnection(MainActivityUG33KV.this)) {
                        getSurveyData();
                    } else {
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
        try {
            switch (adapterView.getId()) {

                case R.id.sp_ssname:
                    feederNameList = new ArrayList<>();
                    ssName = sp_ssname.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(ssName)) {
                        String[] ss = ssName.split("-");
                        ssCode = ss[0]; // 004
                        subName = ss[1];
                        //Collections.reverse(feederNameList);
                        feederNameList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                        feederNameList.add("Select");
                        Collections.reverse(feederNameList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivityUG33KV.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername.setAdapter(adapter);
                    } else {
                        feederNameList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivityUG33KV.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_feedername:
                    feederName = sp_feedername.getSelectedItem().toString();

                    break;


                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getSurveyData() {
        // FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV
        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName ));
        try {
            progressDialog.show();
            String[] feederDta = feederName.split("-");
            feederCode = feederDta[0];
            feedeNameList = feederDta[1];
            LastSurveyRequestVoUG33KV requestVo = new LastSurveyRequestVoUG33KV();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(MainActivityUG33KV.this));
            requestVo.setSESSION_ID(sessionId);
            requestVo.setVERSION(MobileConstantsUG33KV.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);
            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<LastSurveyResponseSSDataVoUG33KV> responseVoCall = retrofitServiceUG33KV.getLastSurveyData(requestVo);
            responseVoCall.enqueue(new Callback<LastSurveyResponseSSDataVoUG33KV>() {
                @Override
                public void onResponse(Call<LastSurveyResponseSSDataVoUG33KV> call, Response<LastSurveyResponseSSDataVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {


                        Log.v("DATA===>>", "" + json);
                        LastSurveyResponseSSDataVoUG33KV response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearSurveyedData();
                            arcGISDatabase.clearssMasterData();
                            List<SSMasterVoUG33KV> ssList = response_vo.getSS_MASTER();
                            if (ssList != null && ssList.size() > 0) {
                                for (SSMasterVoUG33KV ssMasterVoUG33KV : ssList) {
                                    arcGISDatabase.addSSMasterData(ssMasterVoUG33KV);
                                }
                            }

                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Intent intent = new Intent(MainActivityUG33KV.this, PoleSurveyActivityUG33KV.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("FEEDER_NAME", feedeNameList);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                startActivity(intent);
                                finish();
                            } else {
                                arcGISDatabase.clearSurveyedData();
                                List<LastSurveyResponseDataVoUG33KV> masterResponseDataVos = response_vo.getRESPONSE();
                                for (LastSurveyResponseDataVoUG33KV vo : masterResponseDataVos) {
                                    SurveyVoUG33KV surveyVoUG33KV = new SurveyVoUG33KV();
                                    surveyVoUG33KV.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVoUG33KV.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVoUG33KV.setLat(vo.getGLAT());
                                    surveyVoUG33KV.setLan(vo.getGLONG());
                                    surveyVoUG33KV.setPastSurveyFlag("Y");
                                    arcGISDatabase.addSurveyData(surveyVoUG33KV);
                                }
                                Intent intent = new Intent(MainActivityUG33KV.this, PoleSurveyActivityUG33KV.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("FEEDER_NAME", feedeNameList);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), MainActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivityUG33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstantsUG33KV.APK_DOWNLOAD_LINK);
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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
                                Toast.makeText(MainActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(MainActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseSSDataVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(MainActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
}
