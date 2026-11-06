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
import android.widget.TextView;
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
import com.apepdcl.gis_survey.vo.IncrementalLTSurveyRequestDataVo;
import com.apepdcl.gis_survey.vo.IncrementalLTSurveyRequestVo;
import com.apepdcl.gis_survey.vo.IncrementalSurveyDataVo;
import com.apepdcl.gis_survey.vo.LTCktsDataVo;
import com.apepdcl.gis_survey.vo.LTWoLockReqVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.SuveyedResponseVo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.apepdcl.gis_survey.HomeActivity;

public class IncrementalTransmitDataActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount;
    private Button btn_transmit;
    private ArcGISDatabase arcGISDatabase;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String SurveyFlag="";
    private SharedPreferences preferences;
    private String userName,sessionId,feederId,woId,woStatus,ltCkt,dtrCode;
    private List<IncrementalSurveyDataVo> incrementalSurveyDataVoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incr_transmitdata);
        try {
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabase(this);
            incrementalSurveyDataVoList = arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTALSURVEY_TABLE WHERE SURVEY_FLAG='N'");
            if (incrementalSurveyDataVoList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + incrementalSurveyDataVoList.size() + ")");
            }
            progressDialog = new ProgressDialog(IncrementalTransmitDataActivity.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);
           // getSupportActionBar().setTitle("33Kv LineSurvey");
            preferences = PreferenceManager.getDefaultSharedPreferences(IncrementalTransmitDataActivity.this);
           // userName = preferences.getString(MobileConstants.USERNAME, "");
            userName=preferences.getString(MobileConstants.USERNAME,"");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(IncrementalTransmitDataActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("GISLT Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(IncrementalTransmitDataActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
          //  setSupportActionBar(toolbar);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_transmit:
                if (ConnectivityChecking.checkConnection(IncrementalTransmitDataActivity.this)) {
                    if(incrementalSurveyDataVoList.size()>0){
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
           // List<LTCktsResponseDataVo> ltCktsResponseDataVos=arcGISDatabase.getDTRMasterData("");
            List<LTCktsDataVo> ltCktsResponseDataVos=arcGISDatabase.getIncrementalDTRMasterData("");
            List<LoginDataResponse> loginDataResponses=arcGISDatabase.getUserMasterData();
            IncrementalLTSurveyRequestVo ltSurveyRequestVo=new IncrementalLTSurveyRequestVo();
            List<IncrementalLTSurveyRequestDataVo> surveyedRequestDataVosList=new ArrayList<>();
            ltSurveyRequestVo.setIMEI_NO(ConnectivityChecking.getIMEI_NO(IncrementalTransmitDataActivity.this));
            ltSurveyRequestVo.setVERSION(MobileConstants.APP_VERSION);
            ltSurveyRequestVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
            ltSurveyRequestVo.setOFFICE_ID(loginDataResponses.get(0).getOFFICE_ID());
            //ltSurveyRequestVo.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());
            ltSurveyRequestVo.setCREATED_BY(userName);

            /*LTWoLockReqVo ltWoLockReqVo=new LTWoLockReqVo();
            List<WoStatusMasterVo> woStatusMasterVoList=arcGISDatabase.getWoStatusMasterData();
            ltWoLockReqVo.setFEEDER_ID(woStatusMasterVoList.get(0).getFEEDER_ID());
            ltWoLockReqVo.setWO_ID(woStatusMasterVoList.get(0).getWO_ID());
            ltWoLockReqVo.setWO_STATUS(woStatusMasterVoList.get(0).getWO_STATUS());
            ltWoLockReqVo.setLT_DTR_CKT_CODE(woStatusMasterVoList.get(0).getLT_CKT());
            ltWoLockReqVo.setLT_DTR_CODE(woStatusMasterVoList.get(0).getDTR_STRCODE());
            //ltWoLockReqVo.setUPDATED_BY();
            List<LTWoLockReqVo> masterVosList=new ArrayList<>();
            masterVosList.add(ltWoLockReqVo);*/
            for(IncrementalSurveyDataVo surveyDataVo:incrementalSurveyDataVoList){
                IncrementalLTSurveyRequestDataVo incrementalLTSurveyRequestDataVo=new IncrementalLTSurveyRequestDataVo();
                incrementalLTSurveyRequestDataVo.setCREATED_BY(userName);
                incrementalLTSurveyRequestDataVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
                incrementalLTSurveyRequestDataVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());
                incrementalLTSurveyRequestDataVo.setIME_NO(ConnectivityChecking.getIMEI_NO(IncrementalTransmitDataActivity.this));
                incrementalLTSurveyRequestDataVo.setFEEDER_ID(surveyDataVo.getFEEDER_ID());
                feederId=surveyDataVo.getFEEDER_ID();
                incrementalLTSurveyRequestDataVo.setDTR_STRUCTURE(surveyDataVo.getDTR_STRUCTURE());
                incrementalLTSurveyRequestDataVo.setLT_CKT_CODE(surveyDataVo.getLT_CIRCUIT());
                incrementalLTSurveyRequestDataVo.setEND_POLE(CommanUtilits.isChecking(surveyDataVo.getIS_END_POLE()));
                incrementalLTSurveyRequestDataVo.setLINE_TYPE(CommanUtilits.isChecking(surveyDataVo.getLINE_TYPE()));
                incrementalLTSurveyRequestDataVo.setLINE_CONFIG(CommanUtilits.isChecking(surveyDataVo.getLINE_CONFIGURATION()));
                incrementalLTSurveyRequestDataVo.setIS_OTHERSCS(CommanUtilits.isChecking(surveyDataVo.getIS_SERVICES_OTHERTHAN_SL()));
                incrementalLTSurveyRequestDataVo.setIS_STREETLIGHT(CommanUtilits.isChecking(surveyDataVo.getIS_STREETLIGHT_AV()));
                //ltSurveyRequestDataVo.setDTR_STRUCTURE("");
                incrementalLTSurveyRequestDataVo.setNO_AGL_SCS(surveyDataVo.getNO_AGL_SERVICES());
                incrementalLTSurveyRequestDataVo.setNO_AQUA_SCS(surveyDataVo.getNO_AQUA_SERVICES());
                incrementalLTSurveyRequestDataVo.setNO_INDUSTRIAL_SCS(surveyDataVo.getNO_INDUSTRIAL_SERVICES());
                incrementalLTSurveyRequestDataVo.setPOLE_NO(String.valueOf(surveyDataVo.getPOLE_NO()));
                incrementalLTSurveyRequestDataVo.setFROM_POLE(String.valueOf(surveyDataVo.getFROM_POLE()));
                incrementalLTSurveyRequestDataVo.setPOLE_TYPE(CommanUtilits.isChecking(surveyDataVo.getPOLE_TYPE()));
                incrementalLTSurveyRequestDataVo.setPOLE_HEIGHT(surveyDataVo.getPOLE_HEIGHT());
               /* @SerializedName("POLE_SUPPORT")
                private String POLE_SUPPORT;*/
                incrementalLTSurveyRequestDataVo.setPOLE_SUPPORT(CommanUtilits.isChecking(surveyDataVo.getSTRUT_STAY()));
                incrementalLTSurveyRequestDataVo.setPIN_CUT(CommanUtilits.isChecking(surveyDataVo.getPIN_CUT()));
                incrementalLTSurveyRequestDataVo.setIS_DOUBLEFEEDING(CommanUtilits.isChecking(surveyDataVo.getIS_DOUBLE_FEEDING()));
                incrementalLTSurveyRequestDataVo.setTAPPING_POINT(CommanUtilits.isChecking(surveyDataVo.getTAPPING_POINT()));
                incrementalLTSurveyRequestDataVo.setTAPPING_LOCNAME(CommanUtilits.isChecking(surveyDataVo.getTAPPING_LOCATION()));
                incrementalLTSurveyRequestDataVo.setTAPPING_POINTS_NO(surveyDataVo.getNO_OF_SPURLINES());
                incrementalLTSurveyRequestDataVo.setCONDUCTOR_SIZE(CommanUtilits.isChecking(surveyDataVo.getCONDUCTOR_SIZE()));
                incrementalLTSurveyRequestDataVo.setIS_MULTICKT(CommanUtilits.isChecking(surveyDataVo.getIS_MULTI_CIRCUIT()));
                /*@SerializedName("CKT_NO")
                private String CKT_NO;*/
                incrementalLTSurveyRequestDataVo.setCKT_NO(surveyDataVo.getNO_OF_CIRCUITS());

                /*@SerializedName("CKT_TYPE")
                private String CKT_TYPE;*/
                incrementalLTSurveyRequestDataVo.setCKT_TYPE(CommanUtilits.isChecking(surveyDataVo.getMULTI_VOLTAGE()));
                incrementalLTSurveyRequestDataVo.setNO_33KV(surveyDataVo.getNO_OF33KV_CKTS());
                incrementalLTSurveyRequestDataVo.setNO_11KV(surveyDataVo.getNO_OF11KV_CKTS());
                incrementalLTSurveyRequestDataVo.setNO_6_3KV(surveyDataVo.getNO_OF6_3KV_VOLTAGE_CKTS());
                incrementalLTSurveyRequestDataVo.setNO_LT(surveyDataVo.getNO_OF_LT_CKTS());
                incrementalLTSurveyRequestDataVo.setPOLE_PAINT_CODE(CommanUtilits.isChecking(surveyDataVo.getPOLE_PAINTING_CODE()));
                incrementalLTSurveyRequestDataVo.setGLAT(surveyDataVo.getLAT());
                incrementalLTSurveyRequestDataVo.setGLONG(surveyDataVo.getLAN());
                incrementalLTSurveyRequestDataVo.setREMARKS(CommanUtilits.isChecking(surveyDataVo.getREMARKS()));
                incrementalLTSurveyRequestDataVo.setLOCATION_TYPE(CommanUtilits.isChecking(surveyDataVo.getLOCATION_TYPE()));
                incrementalLTSurveyRequestDataVo.setSERVICE_PILLERTYPE(CommanUtilits.isChecking(surveyDataVo.getSERVICE_PILLERTYPE()));
                incrementalLTSurveyRequestDataVo.setFEEDERPILLET_OUTGN_CKT(surveyDataVo.getFEEDERPILLET_OUTGN_CKT());
                incrementalLTSurveyRequestDataVo.setCABLE_SIZE(CommanUtilits.isChecking(surveyDataVo.getCABLE_SIZE()));
                incrementalLTSurveyRequestDataVo.setNO_CABLES(surveyDataVo.getNO_CABLES());

                incrementalLTSurveyRequestDataVo.setIS_CROSSINGS_AV(surveyDataVo.getIS_CROSSINGS_AV());
                incrementalLTSurveyRequestDataVo.setLT_CROSS(surveyDataVo.getNOOF_LT_CROSSINGS());
                incrementalLTSurveyRequestDataVo.setLT_6_3KV_CROSS(surveyDataVo.getNOOF_6_3KV_CROSSINGS());
                incrementalLTSurveyRequestDataVo.setLT_11KV_CROSS(surveyDataVo.getNOOF_11KV_CROSSINGS());
                incrementalLTSurveyRequestDataVo.setLT_33KV_CROSS(surveyDataVo.getNOOF_33KV_CROSSINGS());
                incrementalLTSurveyRequestDataVo.setTC_CROSS(surveyDataVo.getNOOF_TC_CROSSINGS());
                incrementalLTSurveyRequestDataVo.setTM_CROSS(surveyDataVo.getNOOF_TM_CROSSINGS());
                incrementalLTSurveyRequestDataVo.setWO_STATUS(surveyDataVo.getWORKORDER_STATUS());
                incrementalLTSurveyRequestDataVo.setWO_ID(surveyDataVo.getWORK_ORDERID());
                woId=surveyDataVo.getWORK_ORDERID();
                woStatus=surveyDataVo.getWORKORDER_STATUS();
                ltCkt=surveyDataVo.getLT_CIRCUIT();
                dtrCode=surveyDataVo.getDTR_STRUCTURE();
                incrementalLTSurveyRequestDataVo.setWO_END_POLE(surveyDataVo.getIS_SURVEY_COMPLETED());


                /* ltSurveyRequestDataVo.setDTR_METER_AVAILABLE(surveyDataVo.getDTR_METER_AVILABLE());
                ltSurveyRequestDataVo.setDTR_NO_OF_METERS(surveyDataVo.getDTR_NO_OF_METERS());*/
                surveyedRequestDataVosList.add(incrementalLTSurveyRequestDataVo);

            }

            LTWoLockReqVo ltWoLockReqVo=new LTWoLockReqVo();
           // List<WoStatusMasterVo> woStatusMasterVoList=arcGISDatabase.getWoStatusMasterData();
            ltWoLockReqVo.setFEEDER_ID(feederId);
            ltWoLockReqVo.setWO_ID(woId);
            ltWoLockReqVo.setWO_STATUS(woStatus);
            ltWoLockReqVo.setLT_DTR_CKT_CODE(ltCkt);
            ltWoLockReqVo.setLT_DTR_CODE(dtrCode);
            //ltWoLockReqVo.setUPDATED_BY();
            List<LTWoLockReqVo> masterVosList=new ArrayList<>();
            masterVosList.add(ltWoLockReqVo);



            ltSurveyRequestVo.setFEEDER_ID(feederId);
            ltSurveyRequestVo.setLT_REQUST(surveyedRequestDataVosList);
            ltSurveyRequestVo.setWORK_ORDER(masterVosList);
            Gson gson = new Gson();
            String json = gson.toJson(ltSurveyRequestVo);

          //  Log.v("INCRDATA===>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.posIncrementalLtSurveyData(ltSurveyRequestVo);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();
                       // http://10.150.250.166:8080/LtGis/rest/ltsurvey/gisltposting1
                       // http://10.150.250.166:8080/LtGis/rest/ltsurvey/gisltposting


                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearIncrementalSurveyData();
                            Intent intent = new Intent(IncrementalTransmitDataActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(IncrementalTransmitDataActivity.this, "Data Transmited Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(IncrementalTransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(IncrementalTransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(IncrementalTransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IncrementalTransmitDataActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}



