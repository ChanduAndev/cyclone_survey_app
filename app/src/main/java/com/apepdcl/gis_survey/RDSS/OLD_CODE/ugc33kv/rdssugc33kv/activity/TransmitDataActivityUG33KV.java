package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginDataResponseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyedRequestDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyedRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SuveyedResponseVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;

import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransmitDataActivityUG33KV extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount;
    private Button btn_transmit;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<SurveyVoUG33KV> surveyVoUG33KVList;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private String SurveyFlag="";
    private SharedPreferences preferences;
    private String userName,sessionId;
    private List<LoginDataResponse> dataResponseList;
    private List<FeederDataVoUG33KV> feederDataVoUG33KVS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitdata_ugc_33kv);
        try {
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabaseUG33KV(this);
            surveyVoUG33KVList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            if (surveyVoUG33KVList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + surveyVoUG33KVList.size() + ")");
            }
            progressDialog = new ProgressDialog(TransmitDataActivityUG33KV.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);
            getSupportActionBar().setTitle("RDSS 33kV UG Cable Survey");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstantsUG33KV.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitDataActivityUG33KV.this, LoginActivity.class);
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
                if (ConnectivityCheckingUG33KV.checkConnection(TransmitDataActivityUG33KV.this)) {
                    if(surveyVoUG33KVList.size()>0){
                        transmitData();

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
    public void transmitData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            SurveyedRequestVoUG33KV surveyedRequestVoUG33KV = new SurveyedRequestVoUG33KV();
            List<SurveyedRequestDataVoUG33KV> surveyedRequestDataVosListUG33KV = new ArrayList<>();
            dataResponseList = arcGISDatabase.getUserMasterData();
            surveyedRequestVoUG33KV.setUSER_ID(userName);
            surveyedRequestVoUG33KV.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(TransmitDataActivityUG33KV.this));
            surveyedRequestVoUG33KV.setSESSION_ID(sessionId);
            surveyedRequestVoUG33KV.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVoUG33KV.setIS_SURVEY_COM("N");
            surveyedRequestVoUG33KV.setFEEDER_ID(surveyVoUG33KVList.get(0).getFeeder_id());

            for (SurveyVoUG33KV surveyVoUG33KV : surveyVoUG33KVList) {
                SurveyedRequestDataVoUG33KV requestDataVo = new SurveyedRequestDataVoUG33KV();
                requestDataVo.setFEEDER_ID(surveyVoUG33KV.getFeeder_id());
                requestDataVo.setVOLTAGE_LEVEL("33kv");
                requestDataVo.setPOLE_NO(surveyVoUG33KV.getPole_no());
                requestDataVo.setFROM_POLE(surveyVoUG33KV.getFrom_pole());
                requestDataVo.setPOLE_TYPE(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getPole_type()));
                requestDataVo.setPOLE_HEIGHT(surveyVoUG33KV.getPole_height());
                // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
                requestDataVo.setPOLE_SUPPORT(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getPole_support()));
                requestDataVo.setTAPPING_POINT(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getTapping_point()));
                requestDataVo.setCONDUCTOR_SIZE(surveyVoUG33KV.getConductor_size());
                requestDataVo.setPOLE_PAINT_CODE(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getPole_paintingcode()));
                requestDataVo.setSEC_CODE(dataResponseList.get(0).getOFFICE_ID());
                requestDataVo.setCREATED_BY(userName);
                requestDataVo.setGLAT(surveyVoUG33KV.getLat());
                requestDataVo.setGLONG(surveyVoUG33KV.getLan());
                requestDataVo.setNO_CKT(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getIsMultiCircuit()));
                requestDataVo.setCKT_TYPE(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getCircuitType()));
                requestDataVo.setIME_NO(CommanUtilitsUG33KV.getIMEI_NO(TransmitDataActivityUG33KV.this));
                requestDataVo.setPOLE_CONFIG(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getPole_configuration()));
                requestDataVo.setPIN_CUT(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getPinpoint()));
                requestDataVo.setSS_NAME(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getSs_name()));
                requestDataVo.setPOLE_STRUCTURE(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getPoleStructure()));
                requestDataVo.setDIFF_VOLTAGES(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getDiffVoltage()));

                if (surveyVoUG33KV.getIsEndPole() != null) {
                    requestDataVo.setEND_POLE(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getIsEndPole()));
                } else {
                    requestDataVo.setEND_POLE("");
                }
                // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
                requestDataVo.setLINE_TYPE(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getLineType()));
                requestDataVo.setCKT_NO(surveyVoUG33KV.getNoofCircuits());
                requestDataVo.setSS_ID(surveyVoUG33KV.getSsId());
                requestDataVo.setSTJOINT_RMU(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getStjoint_rmu()));
                requestDataVo.setRMU_TYPE(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getRmu_type()));
                requestDataVo.setTAPPING_POINTS(surveyVoUG33KV.getNoofTappings());
                requestDataVo.setTAPPING_LOCNAME(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getTappingLocation()));
                requestDataVo.setREMARKS(CommanUtilitsUG33KV.isChecking(surveyVoUG33KV.getRemarks()));
                requestDataVo.setWO_ID(surveyVoUG33KV.getWoId());
                requestDataVo.setIS_FEEDER_TYPE(surveyVoUG33KV.getIsFeederType());
                requestDataVo.setHT_SCNO(surveyVoUG33KV.getHtServiceNumber());
                requestDataVo.setSURVEY_TYPE(surveyVoUG33KV.getSurvaytype());

                System.out.println("POLE SURVEY TYPE : "+ surveyVoUG33KV.getPoleSurveyType());
                System.out.println("RMU SURVEY TYPE : "+ surveyVoUG33KV.getRmuSurveyType());
                //NEW
                requestDataVo.setPOLE_SURVEY_TYPE(surveyVoUG33KV.getPoleSurveyType());
                requestDataVo.setRMU_SURVEY_TYPE(surveyVoUG33KV.getRmuSurveyType());


                surveyedRequestDataVosListUG33KV.add(requestDataVo);
            }
            surveyedRequestVoUG33KV.setSURVEY_DATA(surveyedRequestDataVosListUG33KV);
            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVoUG33KV);

            Log.v("DATA===>>", "" + json);


            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<SuveyedResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.transmitData(surveyedRequestVoUG33KV);
            responseVoCall.enqueue(new Callback<SuveyedResponseVoUG33KV>() {
                @Override
                public void onResponse(Call<SuveyedResponseVoUG33KV> call, Response<SuveyedResponseVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVoUG33KV response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearSurveyedData();
                            Intent intent = new Intent(TransmitDataActivityUG33KV.this, FeederMasterActivityUG33KV.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(TransmitDataActivityUG33KV.this, "Data Transmited Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransmitDataActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransmitDataActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
}



