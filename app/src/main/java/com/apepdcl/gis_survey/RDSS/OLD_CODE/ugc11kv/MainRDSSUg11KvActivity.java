package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity.FeederMasterRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity.PoleSurveyRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyResponseDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyResponseSSDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SSMasterVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRDSSUg11KvActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_next;
    private Spinner sp_ssname1,sp_feedername1;
    private String ssName,feederName,feederId,maxCount,sessionId,feederCode,ssCode,subName,feedeNameList;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rdss_ug_11kv);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("11KV UG Cable Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(MainRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
        sp_ssname1=findViewById(R.id.sp_ssname1);
        sp_feedername1=findViewById(R.id.sp_feedername1);
//        getSupportActionBar().setTitle("11KV LineSurvey");
        sp_ssname1.setOnItemSelectedListener(this);
        sp_feedername1.setOnItemSelectedListener(this);
        arcGISDatabaseRDSSUg11Kv =new ArcGISDatabaseRDSSUg11Kv(this);
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
        ehtName= arcGISDatabaseRDSSUg11Kv.getEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(MainRDSSUg11KvActivity.this,
                android.R.layout.simple_spinner_dropdown_item,ehtName);
        sp_ssname1.setAdapter(ehtSSadapter);



        btn_next=(Button)findViewById(R.id.btn_next);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainRDSSUg11KvActivity.this);
        sessionId=preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID,"");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(MainRDSSUg11KvActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("Select".equals(ssName)){
                    Toast.makeText(MainRDSSUg11KvActivity.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(MainRDSSUg11KvActivity.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else {
                    if (ConnectivityCheckingRDSSUg11Kv.checkConnection(MainRDSSUg11KvActivity.this)) {
                        getSurveyData();
                    }else {
                        CommanUtilits.internetDialog(MainRDSSUg11KvActivity.this);
                        //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
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
            switch (adapterView.getId()){

                case R.id.sp_ssname1:
                    feederNameList = new ArrayList<>();
                    ssName = sp_ssname1.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(ssName)) {


                        String[] ss = ssName.split("-");
                        ssCode = ss[0]; // 004
                        subName= ss[1];
                        //Collections.reverse(feederNameList);
                        feederNameList = arcGISDatabaseRDSSUg11Kv.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                        feederNameList.add("Select");
                        Collections.reverse(feederNameList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainRDSSUg11KvActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername1.setAdapter(adapter);
                    } else {
                        feederNameList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainRDSSUg11KvActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername1.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_feedername1:
                    feederName = sp_feedername1.getSelectedItem().toString();
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void getSurveyData(){
        // FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV
        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName ));
        try {
            progressDialog.show();
            String[] feederDta = feederName.split("-");
            feederCode = feederDta[0];
            feedeNameList=feederDta[1];
            LastSurveyRequestVoRDSSUg11Kv requestVo = new LastSurveyRequestVoRDSSUg11Kv();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(MainRDSSUg11KvActivity.this));
            requestVo.setSESSION_ID(sessionId);
            requestVo.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<LastSurveyResponseSSDataVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getLastSurveyData(requestVo);
            responseVoCall.enqueue(new Callback<LastSurveyResponseSSDataVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<LastSurveyResponseSSDataVoRDSSUg11Kv> call, Response<LastSurveyResponseSSDataVoRDSSUg11Kv> response) {

                    if (response.body() != null) {
                        Log.v("DATA===>>", "" + json);
                        LastSurveyResponseSSDataVoRDSSUg11Kv response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearSurveyedData();
                            arcGISDatabaseRDSSUg11Kv.clearssMasterData();
                            List<SSMasterVoRDSSUg11Kv> ssList = response_vo.getSS_MASTER();
                            if (ssList != null && ssList.size() > 0) {
                                for (SSMasterVoRDSSUg11Kv ssMasterVoRDSSUg11Kv : ssList) {
                                    arcGISDatabaseRDSSUg11Kv.addSSMasterData(ssMasterVoRDSSUg11Kv);
                                }
                            }

                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Intent intent = new Intent(MainRDSSUg11KvActivity.this, PoleSurveyRDSSUg11KvActivity.class);
                                intent.putExtra("SSNAME",subName);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                startActivity(intent);
                                finish();
                            } else {
                                arcGISDatabaseRDSSUg11Kv.clearSurveyedData();
                                List<LastSurveyResponseDataVoRDSSUg11Kv> masterResponseDataVos = response_vo.getRESPONSE();
                                for (LastSurveyResponseDataVoRDSSUg11Kv vo : masterResponseDataVos) {
                                    SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv = new SurveyVoRDSSUg11Kv();
                                    surveyVoRDSSUg11Kv.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVoRDSSUg11Kv.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVoRDSSUg11Kv.setLat(vo.getGLAT());
                                    surveyVoRDSSUg11Kv.setLan(vo.getGLONG());
                                    surveyVoRDSSUg11Kv.setPastSurveyFlag("Y");
                                    arcGISDatabaseRDSSUg11Kv.addSurveyData(surveyVoRDSSUg11Kv);
                                }
                                Intent intent = new Intent(MainRDSSUg11KvActivity.this, PoleSurveyRDSSUg11KvActivity.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("MAX_COUNT",response_vo.getSESSION_ID());
                                startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss();
                        } else {
                            // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), MainActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainRDSSUg11KvActivity.this);
                                    alertDialogBuilder.setTitle("A New Version Is Available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    })

                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(MainRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(MainRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseSSDataVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(MainRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
      /*  Intent intent = new Intent(MainActivity.this, FeederMasterActivity.class);
        startActivity(intent);
        finish();*/
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Are you sure you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
