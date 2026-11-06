package com.apepdcl.gis_survey.incremental;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.IncrementalSurveyDataVo;
import com.apepdcl.gis_survey.vo.IncrementalSurveyedDataReqVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.SurveyedResponseDataVo;
import com.apepdcl.gis_survey.vo.SurveyedResponseVo;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.apepdcl.gis_survey.HomeActivity;

public class IncrementalTopTenActivity extends AppCompatActivity {
    private Button btn_next;
    private String dtrCode, ltCktCode, ssName, feederName, feederId, maxCount, sessionId, feederCode, ssCode, subName, feedeNameList, work_order, feerdeCode, feeder_Name;
    private ArcGISDatabase arcGISDatabase;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incrementaltopten);
        //sp_ssname=findViewById(R.id.sp_ssname);
        // sp_feedername=findViewById(R.id.sp_feedername);
        // sp_workorder=findViewById(R.id.sp_workorder);
       // getSupportActionBar().setTitle("33Kv IncrementalLineSurvey");
        //  sp_ssname.setOnItemSelectedListener(this);
        // sp_feedername.setOnItemSelectedListener(this);
        // sp_workorder.setOnItemSelectedListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Incremental GISLT Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(IncrementalTopTenActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        arcGISDatabase = new ArcGISDatabase(this);


        btn_next = (Button) findViewById(R.id.btn_next);
        progressDialog = new ProgressDialog(IncrementalTopTenActivity.this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId = preferences.getString(MobileConstants.SESSION_ID, "");

        Intent intentData = getIntent();




        feederCode = intentData.getStringExtra("FEEDER_CODE");
        dtrCode = intentData.getStringExtra("DTRCODE");
        ltCktCode = intentData.getStringExtra("CIRCUIT_CODE");
        work_order = intentData.getStringExtra("WORKORDER");
        /*  ssName = intentData.getStringExtra("SSNAME");
        feederName = intentData.getStringExtra("FEEDER_NAME");
        feederCode = intentData.getStringExtra("FEEDER_CODE");
       // maxCount = intentData.getStringExtra("MAX_COUNT");
        work_order = intentData.getStringExtra("WORKORDER");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(com.epdcl.gissurvey.incremental.IncrementalTopTenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
        if (sessionId == null || sessionId.isEmpty()) {
            Intent intent = new Intent(IncrementalTopTenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ConnectivityChecking.checkConnection(IncrementalTopTenActivity.this)) {

                    incrementalSurveyedData();

                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    public void incrementalSurveyedData() {
        progressDialog.show();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        IncrementalSurveyedDataReqVo surveyedDataReqVo = new IncrementalSurveyedDataReqVo();
        List<String> poleNumber = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT TAPPING_POLE FROM INCREMENTAL_DTR_TABLE WHERE WORK_ORDERID='%s'", work_order));
        /*if (dtrCode.contains("-")) {
            String[] dtrcode = dtrCode.split("-");
            dtr_Code= dtrcode[0]; // 004
            // String ssName = poleNum[1];
            surveyedDataReqVo.setDTR_STRUCTURE(dtr_Code);
        } else {
            surveyedDataReqVo.setDTR_STRUCTURE(dtrCode);
        }*/

        surveyedDataReqVo.setLT_CKTCODE(ltCktCode);
        surveyedDataReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(IncrementalTopTenActivity.this));
        surveyedDataReqVo.setVERSION(MobileConstants.APP_VERSION);
        surveyedDataReqVo.setSESSION_ID(sessionId);
        surveyedDataReqVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
        surveyedDataReqVo.setPOLE_NO(poleNumber.get(0));
        surveyedDataReqVo.setWO_ID(work_order);
        surveyedDataReqVo.setDTR_STRUCTURE(dtrCode);

        Gson gson = new Gson();
        String str = gson.toJson(surveyedDataReqVo);
       // Log.v("incrementalDATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<SurveyedResponseVo> responseVoCall = retrofitService.getIncrementalsurveyedData(surveyedDataReqVo);
        responseVoCall.enqueue(new Callback<SurveyedResponseVo>() {
            @Override
            public void onResponse(Call<SurveyedResponseVo> call, Response<SurveyedResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    SurveyedResponseVo surveyedResponseVo = response.body();
                    Gson gson = new Gson();
                    String str = gson.toJson(surveyedResponseVo);
                  //  Log.v("SurveyedResponseVo===>>", "" + str);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.getSTATUS())) {
                        arcGISDatabase.clearIncrementalSurveyData();
                        if ("NO_DATA".equalsIgnoreCase(surveyedResponseVo.getERR_MSG())) {
                            Intent intent = new Intent(IncrementalTopTenActivity.this, IncrementalLineTypeActivity.class);
                           /* intent.putExtra("DTRCODE", dtr_Code);
                            intent.putExtra("CIRCUIT_CODE", ltCircuit);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("WO_ID", lt_workorder);*/


                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("DTRCODE", dtrCode);
                            intent.putExtra("CIRCUIT_CODE", ltCktCode);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("WORKORDER", work_order);



                            startActivity(intent);
                            finish();
                        } else {

                            List<SurveyedResponseDataVo> surveyedResponseDataVoList = surveyedResponseVo.getRESPONSE();
                            for (SurveyedResponseDataVo vo : surveyedResponseDataVoList) {
                                IncrementalSurveyDataVo surveyVo = new IncrementalSurveyDataVo();
                                surveyVo.setPOLE_NO(Integer.parseInt(vo.getPOLE_NO()));
                                surveyVo.setFEEDER_ID(vo.getFEEDER_ID());
                                surveyVo.setLAT(vo.getGLAT());
                                surveyVo.setLAN(vo.getGLONG());
                                surveyVo.setSURVEY_FLAG("Y");
                                arcGISDatabase.addIncrementalSurveyData(surveyVo);
                            }
                            Intent intent = new Intent(IncrementalTopTenActivity.this, IncrementalLineTypeActivity.class);
                            /*intent.putExtra("DTRCODE", dtr_Code);
                            intent.putExtra("CIRCUIT_CODE", ltCircuit);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("WO_ID", lt_workorder);*/
                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("DTRCODE", dtrCode);
                            intent.putExtra("CIRCUIT_CODE", ltCktCode);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("WORKORDER", work_order);
                            startActivity(intent);
                            startActivity(intent);
                            finish();
                        }


                    } else {
                        Toast.makeText(IncrementalTopTenActivity.this, surveyedResponseVo.getERR_MSG().toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(IncrementalTopTenActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SurveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(IncrementalTopTenActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IncrementalTopTenActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
