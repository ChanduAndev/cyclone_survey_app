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
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyedRequestDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyedRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginDataResponseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SuveyedResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.WoStatusMasterVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;

import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncrementalTransmitDataActivityUG33KV extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount;
    private Button btn_transmit;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<IncrementalSurveyVoUG33KV> surveyVoList;
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
        setContentView(R.layout.activity_incrementaltransmitdata_ugc_33kv);
        try {
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabaseUG33KV(this);
            surveyVoList = arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            if (surveyVoList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + surveyVoList.size() + ")");
            }
            progressDialog = new ProgressDialog(IncrementalTransmitDataActivityUG33KV.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);
            getSupportActionBar().setTitle("RDSS 33kV UG Cable Survey");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstantsUG33KV.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(IncrementalTransmitDataActivityUG33KV.this, LoginActivity.class);
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
                if (ConnectivityCheckingUG33KV.checkConnection(IncrementalTransmitDataActivityUG33KV.this)) {
                    if(surveyVoList.size()>0){
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
            IncrementalSurveyedRequestVoUG33KV surveyedRequestVo = new IncrementalSurveyedRequestVoUG33KV();
            List<IncrementalSurveyedRequestDataVoUG33KV> surveyedRequestDataVosList = new ArrayList<>();
            List<IncrementalFeederDataVoUG33KV> incrementalFeederDataVoUG33KVList =new ArrayList<>();
            incrementalFeederDataVoUG33KVList =arcGISDatabase.getIncrementalFeerData();

            List<WoStatusMasterVoUG33KV> woStatusMasterVosListUG33KV =new ArrayList<>();
            woStatusMasterVosListUG33KV =arcGISDatabase.getWoStatusMasterData();
            dataResponseList = arcGISDatabase.getUserMasterData();
            surveyedRequestVo.setUSER_ID(userName);
            surveyedRequestVo.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(IncrementalTransmitDataActivityUG33KV.this));
            surveyedRequestVo.setSESSION_ID(sessionId);
            surveyedRequestVo.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVo.setIS_SURVEY_COM("N");
            surveyedRequestVo.setFEEDER_ID(surveyVoList.get(0).getFeeder_id());
            WoStatusMasterVoUG33KV woStatusMasterVoUG33KV =new WoStatusMasterVoUG33KV();
            woStatusMasterVoUG33KV.setFEEDER_ID(woStatusMasterVosListUG33KV.get(0).getFEEDER_ID());
            woStatusMasterVoUG33KV.setWO_ID(woStatusMasterVosListUG33KV.get(0).getWO_ID());
            woStatusMasterVoUG33KV.setWO_STATUS(woStatusMasterVosListUG33KV.get(0).getWO_STATUS());
            List<WoStatusMasterVoUG33KV> masterVosList=new ArrayList<>();
            masterVosList.add(woStatusMasterVoUG33KV);
            for (IncrementalSurveyVoUG33KV surveyVo : surveyVoList) {
                IncrementalSurveyedRequestDataVoUG33KV requestDataVo = new IncrementalSurveyedRequestDataVoUG33KV();
                requestDataVo.setFEEDER_ID(surveyVo.getFeeder_id());
                requestDataVo.setVOLTAGE_LEVEL("33kv");
                requestDataVo.setPOLE_NO(surveyVo.getPole_no());
                requestDataVo.setFROM_POLE(surveyVo.getFrom_pole());
                requestDataVo.setPOLE_TYPE(CommanUtilitsUG33KV.isChecking(surveyVo.getPole_type()));
                requestDataVo.setPOLE_HEIGHT(surveyVo.getPole_height());
                // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
                requestDataVo.setPOLE_SUPPORT(CommanUtilitsUG33KV.isChecking(surveyVo.getPole_support()));
                requestDataVo.setTAPPING_POINT(CommanUtilitsUG33KV.isChecking(surveyVo.getTapping_point()));
                requestDataVo.setCONDUCTOR_SIZE(surveyVo.getConductor_size());
                requestDataVo.setPOLE_PAINT_CODE(CommanUtilitsUG33KV.isChecking(surveyVo.getPole_paintingcode()));
                requestDataVo.setSEC_CODE(dataResponseList.get(0).getOFFICE_ID());
                requestDataVo.setCREATED_BY(userName);
                requestDataVo.setGLAT(surveyVo.getLat());
                requestDataVo.setGLONG(surveyVo.getLan());
                requestDataVo.setNO_CKT(CommanUtilitsUG33KV.isChecking(surveyVo.getIsMultiCircuit()));
                requestDataVo.setCKT_TYPE(CommanUtilitsUG33KV.isChecking(surveyVo.getCircuitType()));
                requestDataVo.setIME_NO(CommanUtilitsUG33KV.getIMEI_NO(IncrementalTransmitDataActivityUG33KV.this));
                requestDataVo.setPOLE_CONFIG(CommanUtilitsUG33KV.isChecking(surveyVo.getPole_configuration()));
                requestDataVo.setPIN_CUT(CommanUtilitsUG33KV.isChecking(surveyVo.getPinpoint()));
                requestDataVo.setSS_NAME(CommanUtilitsUG33KV.isChecking(surveyVo.getSs_name()));
                requestDataVo.setPOLE_STRUCTURE(CommanUtilitsUG33KV.isChecking(surveyVo.getPoleStructure()));
                requestDataVo.setDIFF_VOLTAGES(CommanUtilitsUG33KV.isChecking(surveyVo.getDiffVoltage()));

                if (surveyVo.getIsEndPole() != null) {
                    requestDataVo.setEND_POLE(CommanUtilitsUG33KV.isChecking(surveyVo.getIsEndPole()));
                } else {
                    requestDataVo.setEND_POLE("");
                }
                // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
                requestDataVo.setLINE_TYPE(CommanUtilitsUG33KV.isChecking(surveyVo.getLineType()));
                requestDataVo.setCKT_NO(surveyVo.getNoofCircuits());
                requestDataVo.setSS_ID(surveyVo.getSsId());
                requestDataVo.setSTJOINT_RMU(CommanUtilitsUG33KV.isChecking(surveyVo.getStjoint_rmu()));
                requestDataVo.setRMU_TYPE(CommanUtilitsUG33KV.isChecking(surveyVo.getRmu_type()));
                requestDataVo.setTAPPING_POINTS(surveyVo.getNoofTappings());
                requestDataVo.setTAPPING_LOCNAME(CommanUtilitsUG33KV.isChecking(surveyVo.getTappingLocation()));
                requestDataVo.setREMARKS(CommanUtilitsUG33KV.isChecking(surveyVo.getRemarks()));
                requestDataVo.setWO_ID(surveyVo.getWorkOrder());
                requestDataVo.setWO_STATUS(surveyVo.getWorkOrderStatus());
                requestDataVo.setIS_FEEDER_TYPE(surveyVo.getIsFeederType());
                requestDataVo.setHT_SCNO(surveyVo.getHtServiceNumber());
                surveyedRequestDataVosList.add(requestDataVo);
            }
            surveyedRequestVo.setSURVEY_DATA(surveyedRequestDataVosList);
            surveyedRequestVo.setWORK_ORDER(masterVosList);

            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVo);

            Log.v("DATA===>>", "" + json);

            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<SuveyedResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.incrementalTransmitData(surveyedRequestVo);
            responseVoCall.enqueue(new Callback<SuveyedResponseVoUG33KV>() {
                @Override
                public void onResponse(Call<SuveyedResponseVoUG33KV> call, Response<SuveyedResponseVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVoUG33KV response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);

                        Log.v("TransmitDataRES===>>", "" + json);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearIncrementalSurveyedData();
                            Intent intent = new Intent(IncrementalTransmitDataActivityUG33KV.this, FeederMasterActivityUG33KV.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(IncrementalTransmitDataActivityUG33KV.this, "Data Transmited Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(IncrementalTransmitDataActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(IncrementalTransmitDataActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(IncrementalTransmitDataActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IncrementalTransmitDataActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
}



