package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.module_11KV.ArcGISMapActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitService;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITappingPointResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITappingPointResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITappingPointktsReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITappingPointsDataReqVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMILineTypeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner sp_linetype,sp_tappingpoint;
    private LinearLayout lyt_tapingpoint;
    private Button btn_polenext,btn_taping;
    private String lineType,tappingPoint,dtrCode,ltCircuit,maxCount;
    private TextView tv_surveyedpole;
    private PMIRetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String sessionId,gLat,gLong,feederId;
    private PMIArcGISDatabase arcGISDatabase;
    private List<String> poleNumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmiline_type);
        try {
            setContentView(R.layout.activity_linetype);
            sp_linetype = findViewById(R.id.sp_linetype);
            sp_tappingpoint = findViewById(R.id.sp_tappingpoint);
            btn_polenext = findViewById(R.id.btn_polenext);
            btn_taping = findViewById(R.id.btn_taping);
            lyt_tapingpoint = findViewById(R.id.lyt_tapingpoint);
            tv_surveyedpole = findViewById(R.id.tv_surveyedpole);
            lyt_tapingpoint.setVisibility(View.GONE);
            btn_taping.setVisibility(View.GONE);
            arcGISDatabase = new PMIArcGISDatabase(PMILineTypeActivity.this);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            sp_linetype.setOnItemSelectedListener(this);
            sp_tappingpoint.setOnItemSelectedListener(this);
            btn_polenext.setOnClickListener(this);
            btn_taping.setOnClickListener(this);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PMILineTypeActivity.this);
            sessionId = sharedPreferences.getString(MobileConstants.SESSION_ID, "");
            Intent intent = getIntent();
            dtrCode = intent.getStringExtra("DTRCODE");
            ltCircuit = intent.getStringExtra("CIRCUIT_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            gLat = intent.getStringExtra("GLAT");
            gLong = intent.getStringExtra("GLONG");

            if ("NO_DATA".equalsIgnoreCase(maxCount)) {
                tv_surveyedpole.setText("0");
            } else {
                tv_surveyedpole.setText(maxCount);
            }
            poleNumList = new ArrayList<>();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("LT Survey ");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(PMILineTypeActivity.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            // setSupportActionBar(toolbar);
        }catch (Exception e){}
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.sp_linetype:
                lineType=sp_linetype.getSelectedItem().toString();
                if("Through Line".equalsIgnoreCase(lineType)){
                    btn_polenext.setVisibility(View.VISIBLE);
                    lyt_tapingpoint.setVisibility(View.GONE);
                    btn_taping.setVisibility(View.GONE);
                    sp_tappingpoint.setSelection(0);

                }/*else if("Spur Line".equalsIgnoreCase(lineType)){
                    lyt_tapingpoint.setVisibility(View.VISIBLE);
                    btn_taping.setVisibility(View.VISIBLE);
                    btn_polenext.setVisibility(View.GONE);
                }*/
                break;
            case R.id.sp_tappingpoint:
                tappingPoint=sp_tappingpoint.getSelectedItem().toString();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_polenext:
                if("Select".equalsIgnoreCase(lineType)){
                    Toast.makeText(PMILineTypeActivity.this,"Select LineType",Toast.LENGTH_SHORT).show();
                }else if("Through Line".equalsIgnoreCase(lineType)){
                    Intent intent = new Intent(PMILineTypeActivity.this, ArcGISMapActivity.class);
                    intent.putExtra("DTRCODE",dtrCode);
                    intent.putExtra("CIRCUIT_CODE",ltCircuit);
                    intent.putExtra("MAX_COUNT", maxCount);
                    intent.putExtra("GLAT",gLat);
                    intent.putExtra("GLONG",gLong);
                    startActivity(intent);
                    finish();
                }else if("Spur Line".equalsIgnoreCase(lineType)){
                    if(ConnectivityChecking.checkConnection(PMILineTypeActivity.this)){
                        getTappingPoleNumbers();
                    }else {
                        Toast.makeText(PMILineTypeActivity.this,MobileConstants.INTERNET,Toast.LENGTH_SHORT).show();

                    }

                }

                break;

            case R.id.btn_taping:
                if("Select".equalsIgnoreCase(tappingPoint)){
                    Toast.makeText(PMILineTypeActivity.this,"Select Taping Point",Toast.LENGTH_SHORT).show();
                }else {
                    if(ConnectivityChecking.checkConnection(PMILineTypeActivity.this)){
                        getTappingData();
                    }else {
                        Toast.makeText(PMILineTypeActivity.this,MobileConstants.INTERNET,Toast.LENGTH_SHORT).show();

                    }
                }

                break;
        }
    }
    public void getTappingPoleNumbers(){
        progressDialog.show();
        List<LoginDataResponse> loginDataResponses=arcGISDatabase.getUserMasterData();
        PMITappingPointktsReqVo surveyedDataReqVo=new PMITappingPointktsReqVo();
        surveyedDataReqVo.setDTR_STRUCTURE(dtrCode);
        surveyedDataReqVo.setLT_CKTCODE(ltCircuit);
        surveyedDataReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMILineTypeActivity.this));
        surveyedDataReqVo.setVERSION(MobileConstants.APP_VERSION);
        surveyedDataReqVo.setSESSION_ID(sessionId);
        surveyedDataReqVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
        Gson gson = new Gson();
        String json = gson.toJson(surveyedDataReqVo);
        Log.v("DATA===>>", "" + json);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<PMITappingPointResponseVo> responseVoCall = retrofitService.getTappingPointData(surveyedDataReqVo);
        responseVoCall.enqueue(new Callback<PMITappingPointResponseVo>() {
            @Override
            public void onResponse(Call<PMITappingPointResponseVo> call, Response<PMITappingPointResponseVo> response) {
                progressDialog.dismiss();
                if(response.body() !=null) {
                    PMITappingPointResponseVo tappingPointResponseVo= response.body();
                    if(MobileConstants.TRUE.equalsIgnoreCase(tappingPointResponseVo.getSTATUS())){

                        if ("NO_DATA".equalsIgnoreCase(tappingPointResponseVo.getERR_MSG())) {
                            Toast.makeText(PMILineTypeActivity.this, "No Tapping Poles Found", Toast.LENGTH_SHORT).show();

                        } else {
                            List<PMITappingPointResponseDataVo> masterResponseDataVos = tappingPointResponseVo.getRESPONSE();
                            poleNumList.clear();
                            for (PMITappingPointResponseDataVo vo : masterResponseDataVos) {
                                poleNumList.add(vo.getPOLE_NO() + "-" + vo.getTAPPING_LOCNAME());
                            }
                            lyt_tapingpoint.setVisibility(View.VISIBLE);
                            btn_taping.setVisibility(View.VISIBLE);
                            btn_polenext.setVisibility(View.GONE);
                            poleNumList.add("Select");
                            Collections.reverse(poleNumList);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMILineTypeActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, poleNumList);
                            sp_tappingpoint.setAdapter(adapter);
                        }


                    }else {
                        Toast.makeText(PMILineTypeActivity.this,tappingPointResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(PMILineTypeActivity.this,"Server Busy Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMITappingPointResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMILineTypeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getTappingData(){

        progressDialog.show();
        List<LoginDataResponse> loginDataResponses=arcGISDatabase.getUserMasterData();
        PMITappingPointsDataReqVo tappingPointsDataReqVo=new PMITappingPointsDataReqVo();
        tappingPointsDataReqVo.setDTR_STRUCTURE(dtrCode);
        tappingPointsDataReqVo.setLT_CKTCODE(ltCircuit);
        tappingPointsDataReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMILineTypeActivity.this));
        tappingPointsDataReqVo.setVERSION(MobileConstants.APP_VERSION);
        tappingPointsDataReqVo.setSESSION_ID(sessionId);
        tappingPointsDataReqVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
        if (tappingPoint.contains("-")) {
            String[] poleNum = tappingPoint.split("-");
            String tapingPoleNum = poleNum[0]; // 004
            // String ssName = poleNum[1];
            tappingPointsDataReqVo.setPOLE_NO(Integer.parseInt(tapingPoleNum));
        } else {
            tappingPointsDataReqVo.setPOLE_NO(Integer.parseInt(tappingPoint));
        }

        Gson gson = new Gson();
        String json = gson.toJson(tappingPointsDataReqVo);
        Log.v("DATA===>>", "" + json);


        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<PMISurveyedResponseVo> responseVoCall = retrofitService.getTappingData(tappingPointsDataReqVo);
        responseVoCall.enqueue(new Callback<PMISurveyedResponseVo>() {
            @Override
            public void onResponse(Call<PMISurveyedResponseVo> call, Response<PMISurveyedResponseVo> response) {
                progressDialog.dismiss();
                if(response.body() !=null) {
                    PMISurveyedResponseVo surveyedResponseVo= response.body();
                    if(MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.getSTATUS())){

                        if ("NO_DATA".equalsIgnoreCase(surveyedResponseVo.getERR_MSG())) {
                            Intent intent = new Intent(PMILineTypeActivity.this, ArcGISMapActivity.class);
                            intent.putExtra("DTRCODE",dtrCode);
                            intent.putExtra("CIRCUIT_CODE",ltCircuit);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("GLAT",gLat);
                            intent.putExtra("GLONG",gLong);
                            startActivity(intent);
                            finish();
                        } else {
                            arcGISDatabase.clearSurveyData();
                            List<PMISurveyedResponseDataVo> surveyedResponseDataVoList=surveyedResponseVo.getRESPONSE();
                            for (PMISurveyedResponseDataVo vo : surveyedResponseDataVoList) {
                                PMISurveyDataVo surveyVo = new PMISurveyDataVo();
                                surveyVo.setPOLE_NO(Integer.parseInt(vo.getPOLE_NO()));
                                surveyVo.setFEEDER_ID(vo.getFEEDER_ID());
                                surveyVo.setLAT(vo.getGLAT());
                                surveyVo.setLAN(vo.getGLONG());
                                surveyVo.setSURVEY_FLAG("Y");
                                arcGISDatabase.addSurveyData(surveyVo);
                            }
                            Intent intent = new Intent(PMILineTypeActivity.this, ArcGISMapActivity.class);
                            intent.putExtra("DTRCODE",dtrCode);
                            intent.putExtra("CIRCUIT_CODE",ltCircuit);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("GLAT",gLat);
                            intent.putExtra("GLONG",gLong);
                            startActivity(intent);
                            finish();
                        }



                    }else {
                        Toast.makeText(PMILineTypeActivity.this,surveyedResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(PMILineTypeActivity.this,"Server Busy Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMISurveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMILineTypeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PMILineTypeActivity.this,PMIHomeActivity.class);
        startActivity(intent);
        finish();
    }
}