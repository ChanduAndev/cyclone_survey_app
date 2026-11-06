package com.apepdcl.gis_survey.activitys;

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
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.DTRSurveyRequestDataVo;
import com.apepdcl.gis_survey.vo.DTRSurveyRequestVo;
import com.apepdcl.gis_survey.vo.DTRSurveyVo;
import com.apepdcl.gis_survey.vo.LTCktsResponseDataVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.SuveyedResponseVo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.apepdcl.gis_survey.HomeActivity;

public class TransmitDTRDataActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount;
    private Button btn_transmit;
    private ArcGISDatabase arcGISDatabase;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String SurveyFlag="";
    private SharedPreferences preferences;
    private String userName,sessionId;
    private List<DTRSurveyVo> dtrSurveyVoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit_dtrdata);
        try {
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabase(this);
            dtrSurveyVoList = arcGISDatabase.getDTRSurveyData();
            if (dtrSurveyVoList.size() > 0) {
                tv_escount.setText("No.of Surveyed DTR" + ":(" + dtrSurveyVoList.size() + ")");
            }
            progressDialog = new ProgressDialog(TransmitDTRDataActivity.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);
            // getSupportActionBar().setTitle("33Kv LineSurvey");
            preferences = PreferenceManager.getDefaultSharedPreferences(TransmitDTRDataActivity.this);
            // userName = preferences.getString(MobileConstants.USERNAME, "");
            userName=preferences.getString(MobileConstants.USERNAME,"");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitDTRDataActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("LT Survey ");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(TransmitDTRDataActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
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
                if (ConnectivityChecking.checkConnection(TransmitDTRDataActivity.this)) {
                    if(dtrSurveyVoList.size()>0){
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
            List<LTCktsResponseDataVo> ltCktsResponseDataVos=arcGISDatabase.getDTRMasterData("");
            List<LoginDataResponse> loginDataResponses=arcGISDatabase.getUserMasterData();
            // LTSurveyRequestVo ltSurveyRequestVo=new LTSurveyRequestVo();
            DTRSurveyRequestVo dtrSurveyRequestVo=new DTRSurveyRequestVo();
            List<DTRSurveyRequestDataVo> surveyedRequestDataVosList=new ArrayList<>();

           /* List<LTSurveyRequestDataVo> surveyedRequestDataVosList=new ArrayList<>();
            ltSurveyRequestVo.setIMEI_NO(ConnectivityChecking.getIMEI_NO(TransmitDTRDataActivity.this));
            ltSurveyRequestVo.setVERSION(MobileConstants.APP_VERSION);
            ltSurveyRequestVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
            ltSurveyRequestVo.setOFFICE_ID(loginDataResponses.get(0).getOFFICE_ID());
            ltSurveyRequestVo.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());
            ltSurveyRequestVo.setCREATED_BY(userName);*/

            dtrSurveyRequestVo.setIMEI_NO(ConnectivityChecking.getIMEI_NO(TransmitDTRDataActivity.this));
            dtrSurveyRequestVo.setVERSION(MobileConstants.APP_VERSION);
            dtrSurveyRequestVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
            dtrSurveyRequestVo.setOFFICE_ID(loginDataResponses.get(0).getOFFICE_ID());
            dtrSurveyRequestVo.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());
            dtrSurveyRequestVo.setCREATED_BY(userName);
            dtrSurveyRequestVo.setDTR_CODE(dtrSurveyVoList.get(0).getDTR_STRUCTURE());
            for(DTRSurveyVo dtrSurveyVo:dtrSurveyVoList){
                DTRSurveyRequestDataVo ltSurveyRequestDataVo=new DTRSurveyRequestDataVo();

                ltSurveyRequestDataVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
                ltSurveyRequestDataVo.setCREATED_BY(userName);
                //ltSurveyRequestDataVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());
                ltSurveyRequestDataVo.setIME_NO(ConnectivityChecking.getIMEI_NO(TransmitDTRDataActivity.this));
                ltSurveyRequestDataVo.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());
                ltSurveyRequestDataVo.setDTR_MAKE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_MAKE()));
                ltSurveyRequestDataVo.setDTR_SL_NO(dtrSurveyVo.getDTR_SERIALNO());
                ltSurveyRequestDataVo.setDTR_STRUC_CODE(dtrSurveyVo.getDTR_STRUCTURE());
                ltSurveyRequestDataVo.setDTR_LOAD_TYPE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_LOAD_TYPE()));
                ltSurveyRequestDataVo.setDTR_PHASE(dtrSurveyVo.getDTR_PHASE());
                ltSurveyRequestDataVo.setDTR_CAPACITY(dtrSurveyVo.getDTR_CAPACITY());
                ltSurveyRequestDataVo.setDTR_LOCATION(CommanUtilits.isChecking(dtrSurveyVo.getDTR_LOCATION()));
                ltSurveyRequestDataVo.setDTR_STRUC_TYPE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_STRUCTURE_TYPE()));
                ltSurveyRequestDataVo.setDTR_ABSWITCH(CommanUtilits.isChecking(dtrSurveyVo.getDTR_ABSWITCH()));
                ltSurveyRequestDataVo.setDTR_ABSWTICH_TYPE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_ABSWTICH_TYPE()));
                ltSurveyRequestDataVo.setDTR_HGFUSE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_HGFUSE()));


                ltSurveyRequestDataVo.setABS_COUNDUCTOR_SIZE(dtrSurveyVo.getCONDUCTOR_SIZE());
                ltSurveyRequestDataVo.setHGFUSE_TO_DTR_BUSBAR(CommanUtilits.isChecking(dtrSurveyVo.getHGFUSE_DTR()));
                ltSurveyRequestDataVo.setHGF_COUNDUCTOR_SIZE(dtrSurveyVo.getHGFUSE_DTR_CONDUCTOR_SIZE());
                ltSurveyRequestDataVo.setEARTH_PITS(dtrSurveyVo.getEARTH_PITS());
                ltSurveyRequestDataVo.setCI_PIPES(dtrSurveyVo.getCIPIPES());
                ltSurveyRequestDataVo.setGI_PIPES(dtrSurveyVo.getGIPIPES());
                //ltSurveyRequestDataVo.setDTR_MTR(dtrSurveyVo.getDTR_METER());

                ltSurveyRequestDataVo.setDTR_POLE_SUPPORT(CommanUtilits.isChecking(dtrSurveyVo.getDTR_POLE_SUPPORT()));
                ltSurveyRequestDataVo.setNOOF_STAY(dtrSurveyVo.getNOOF_STAY());
                ltSurveyRequestDataVo.setNOOF_STURT(dtrSurveyVo.getNOOF_STURT());
                ltSurveyRequestDataVo.setLTBUS_CABLE_SIZE(dtrSurveyVo.getLT_BUSBAR_CABLESIZE());
                ltSurveyRequestDataVo.setLTBUS_CONDUCTOR_SIZE(dtrSurveyVo.getLT_BUSBAR_CONDUTORSIZE());
                ltSurveyRequestDataVo.setNOOF_PININSULATORS(dtrSurveyVo.getNOOF_PININSULATORS());
                ltSurveyRequestDataVo.setNOOF_DISCINSULATORS(dtrSurveyVo.getNOOF_DISCINSULATORS());

                ltSurveyRequestDataVo.setABSWITCH_TO_HGFUSE_BUSBAR(dtrSurveyVo.getBUSBAR_TYPE());


                ltSurveyRequestDataVo.setNO_OF_DTR_METERS(CommanUtilits.isChecking(dtrSurveyVo.getNOOF_DTRMETERS()));


                ltSurveyRequestDataVo.setMTR1_SL_NO(dtrSurveyVo.getDTR_METER_NUM());
                ltSurveyRequestDataVo.setMTR1_MAKE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_METER_MAKE()));
                ltSurveyRequestDataVo.setMTR1_CAPACITY(dtrSurveyVo.getDTR_METER_CAPACITY());
                ltSurveyRequestDataVo.setMTR1_TYPE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_METER_TYPE()));

                ltSurveyRequestDataVo.setMTR2_SL_NO(dtrSurveyVo.getDTR_MTR2_NUM());
                ltSurveyRequestDataVo.setMTR2_MAKE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_MTR2_MAKE()));
                ltSurveyRequestDataVo.setMTR2_CAPACITY(dtrSurveyVo.getDTR_MTR2_CAPACITY());
                ltSurveyRequestDataVo.setMTR2_TYPE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_MTR2_TYPE()));

                ltSurveyRequestDataVo.setMTR3_SL_NO(dtrSurveyVo.getDTR_MTR3_NUM());
                ltSurveyRequestDataVo.setMTR3_MAKE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_MTR3_MAKE()));
                ltSurveyRequestDataVo.setMTR3_CAPACITY(dtrSurveyVo.getDTR_MTR3_CAPACITY());
                ltSurveyRequestDataVo.setMTR3_TYPE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_MTR3_TYPE()));

                ltSurveyRequestDataVo.setMODEM1_AVL(CommanUtilits.isChecking(dtrSurveyVo.getMODEM()));
                ltSurveyRequestDataVo.setMODEM1_SLNO(dtrSurveyVo.getMODEM_NUM());
                ltSurveyRequestDataVo.setMODEM1_MAKE(CommanUtilits.isChecking(dtrSurveyVo.getMODEM_MAKE()));
                ltSurveyRequestDataVo.setDTR_POLE(CommanUtilits.isChecking(dtrSurveyVo.getDTR_POLE()));
                ltSurveyRequestDataVo.setPIN(CommanUtilits.isChecking(dtrSurveyVo.getPIN()));
                ltSurveyRequestDataVo.setCUT(CommanUtilits.isChecking(dtrSurveyVo.getCUT()));
                ltSurveyRequestDataVo.setLT_FUSE(CommanUtilits.isChecking(dtrSurveyVo.getLT_FUSESET()));
                ltSurveyRequestDataVo.setLT_BUS_BAR(CommanUtilits.isChecking(dtrSurveyVo.getLT_BUSBAR()));
                ltSurveyRequestDataVo.setLTBUS_CONDUCTOR_SIZE(dtrSurveyVo.getLT_BUSBAR_CONDUTORSIZE());
                ltSurveyRequestDataVo.setLTBUS_CABLE_SIZE(dtrSurveyVo.getLT_BUSBAR_CABLESIZE());
                ltSurveyRequestDataVo.setGLAT(dtrSurveyVo.getLAT());
                ltSurveyRequestDataVo.setGLONG(dtrSurveyVo.getLAN());
                ltSurveyRequestDataVo.setDTR_LANDMARK(dtrSurveyVo.getREMARKS());
                ltSurveyRequestDataVo.setMODEM2_AVL(dtrSurveyVo.getMODEM2());
                ltSurveyRequestDataVo.setMODEM2_SLNO(dtrSurveyVo.getMODEM2_NUM());
                ltSurveyRequestDataVo.setMODEM2_MAKE(dtrSurveyVo.getMODEM2_MAKE());
                ltSurveyRequestDataVo.setMODEM3_AVL(dtrSurveyVo.getMODEM3());
                ltSurveyRequestDataVo.setMODEM3_SLNO(dtrSurveyVo.getMODEM3_NUM());
                ltSurveyRequestDataVo.setMODEM3_MAKE(dtrSurveyVo.getMODEM3_MAKE());
                ltSurveyRequestDataVo.setNO_OF_LTCKT(dtrSurveyVo.getNOOF_LTCIRCUITS());

                //NEW
                ltSurveyRequestDataVo.setSTRUCTURE_DEFECTS(dtrSurveyVo.getSTRUCTURE_DEFECTS());
                ltSurveyRequestDataVo.setDTR_FENCING(dtrSurveyVo.getDTR_FENCING());
                ltSurveyRequestDataVo.setDTR_AB_DEFECTS(dtrSurveyVo.getDTR_AB_DEFECTS());
                ltSurveyRequestDataVo.setHG_FUSE_DEFECTS(dtrSurveyVo.getHG_FUSE_DEFECTS());
                ltSurveyRequestDataVo.setLT_FUSE_DEFECTS(dtrSurveyVo.getLT_FUSE_DEFECTS());
                ltSurveyRequestDataVo.setNUM_EARTH(dtrSurveyVo.getNUM_EARTH());
                ltSurveyRequestDataVo.setNUM_LT_BUSHINGS(dtrSurveyVo.getNUM_LT_BUSHINGS());
                ltSurveyRequestDataVo.setNUM_HT_BUSHINGS(dtrSurveyVo.getNUM_HT_BUSHINGS());
                ltSurveyRequestDataVo.setNUM_PG_LUG(dtrSurveyVo.getNUM_PG_LUG());
                ltSurveyRequestDataVo.setXLPE_DEFECTS(dtrSurveyVo.getXLPE_DEFECTS());
                ltSurveyRequestDataVo.setNUM_ARRESTER(dtrSurveyVo.getNUM_ARRESTER());
                ltSurveyRequestDataVo.setOIL_LEAK(dtrSurveyVo.getOIL_LEAK());
                ltSurveyRequestDataVo.setOTHER_DEFECTS(dtrSurveyVo.getOTHER_DEFECTS());

                ltSurveyRequestDataVo.setREMARKS(dtrSurveyVo.getSURVEY_REMARKS());

                //ADDED ON 26-07-2024
                ltSurveyRequestDataVo.setBODY_EARTH(dtrSurveyVo.getBODY_EARTH());
                ltSurveyRequestDataVo.setNEUTRAL_EARTH(dtrSurveyVo.getNEUTRAL_EARTH());
               // System.out.println("BODY EARTH IN TRANSMIT : "+dtrSurveyVo.getBODY_EARTH());
               // System.out.println("NEUTRAL EARTH IN TRANSMIT : "+dtrSurveyVo.getNEUTRAL_EARTH());

                //OLD CODE IMAGE
                ltSurveyRequestDataVo.setIMAGE_1(dtrSurveyVo.getIMAGE_1()!=null ? ConvertImage.convertBase64(dtrSurveyVo.getIMAGE_1()):"N");
                ltSurveyRequestDataVo.setIMAGE_2(dtrSurveyVo.getIMAGE_2()!=null ? ConvertImage.convertBase64(dtrSurveyVo.getIMAGE_2()):"N");
                ltSurveyRequestDataVo.setIMAGE_3(dtrSurveyVo.getIMAGE_3()!=null ? ConvertImage.convertBase64(dtrSurveyVo.getIMAGE_3()):"N");

                //NEW CODE IMAGE
               /* ltSurveyRequestDataVo.setIMAGE_1(dtrSurveyVo.getIMAGE_1()!=null ? dtrSurveyVo.getIMAGE_1():"N");
                ltSurveyRequestDataVo.setIMAGE_2(dtrSurveyVo.getIMAGE_2()!=null ? dtrSurveyVo.getIMAGE_2():"N");
                ltSurveyRequestDataVo.setIMAGE_3(dtrSurveyVo.getIMAGE_3()!=null ? dtrSurveyVo.getIMAGE_3():"N");*/


                surveyedRequestDataVosList.add(ltSurveyRequestDataVo);


            }
            dtrSurveyRequestVo.setDTR_REQUST(surveyedRequestDataVosList);
            Gson gson = new Gson();
            String json = gson.toJson(dtrSurveyRequestVo);

           // Log.v("DATA===>>", "" + json);
           // 4728,21600


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.postDTRSurveyData(dtrSurveyRequestVo);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearDTRSurveyData();
                            Intent intent = new Intent(TransmitDTRDataActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(TransmitDTRDataActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransmitDTRDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDTRDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDTRDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransmitDTRDataActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}



