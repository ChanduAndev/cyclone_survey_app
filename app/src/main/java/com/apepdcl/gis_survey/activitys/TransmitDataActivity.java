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
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.LTImageRequest;
import com.apepdcl.gis_survey.LTImagesModel;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.lt_edit_screens.EditLocationRequest;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LTCktsResponseDataVo;
import com.apepdcl.gis_survey.vo.LTSurveyRequestDataVo;
import com.apepdcl.gis_survey.vo.LTSurveyRequestVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.MultiRequestVO;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.apepdcl.gis_survey.vo.SuveyedResponseVo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.apepdcl.gis_survey.HomeActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TransmitDataActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount, tv_image_count, tv_edit_survey;
    private Button btn_transmit, btn_transmit_images;
    private ArcGISDatabase arcGISDatabase;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String SurveyFlag = "";
    private SharedPreferences preferences;
    private String userName, sessionId, FeederId;
    private List<SurveyDataVo> surveyVoList, imagesList, surveyVoList_LAT_LAN, surveyVoListEditPoleDetails, surveyVoList_Source_pole;
    int imageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitdata);
        try {
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            tv_image_count = (TextView) findViewById(R.id.tv_image_count);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            tv_edit_survey = findViewById(R.id.tv_edit_survey);
            btn_transmit_images = (Button) findViewById(R.id.btn_transmit_images);
            btn_transmit.setOnClickListener(this);
            btn_transmit_images.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabase(this);
            surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM SURVEY_TABLE WHERE SURVEY_FLAG='N'");
            surveyVoList_LAT_LAN = arcGISDatabase.getAllSurveyDataLatLan();
            surveyVoListEditPoleDetails = arcGISDatabase.getEditPoleSurveyData();
            surveyVoList_Source_pole = arcGISDatabase.getEditSourcePoleSurveyData();
            imagesList = arcGISDatabase.getLTImageData("");
            if (surveyVoList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + surveyVoList.size() + ")");
            } else if (surveyVoList_LAT_LAN.size() > 0) {
                tv_edit_survey.setText("No.of Edit Surveyed Lat lans" + ":(" + surveyVoList_LAT_LAN.size() + ")");
            } else if (surveyVoListEditPoleDetails.size() > 0) {
                tv_edit_survey.setText("No.of Edit Surveyed Pole Details" + ":(" + surveyVoListEditPoleDetails.size() + ")");
            } else if (surveyVoList_Source_pole.size() > 0) {
                tv_edit_survey.setText("No.of Edit Surveyed Source Pole " + ":(" + surveyVoList_Source_pole.size() + ")");
            } else {
                tv_escount.setText("No.of Surveyed Poles (0)");
            }

            for (int i = 0; i < imagesList.size(); i++) {
                if (!imagesList.get(i).getIMAGE_1().equals("N")) {
                    imageCount++;
                }
                if (!imagesList.get(i).getIMAGE_2().equals("N")) {
                    imageCount++;
                }
                if (!imagesList.get(i).getIMAGE_3().equals("N")) {
                    imageCount++;
                }
            }

            if (imagesList.size() > 0) {
                tv_image_count.setText("No.of Images" + ":(" + imageCount + ")");
            } else {
                tv_image_count.setText("No.of Images:(0)");
            }

            progressDialog = new ProgressDialog(TransmitDataActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            // getSupportActionBar().setTitle("33Kv LineSurvey");
            preferences = PreferenceManager.getDefaultSharedPreferences(TransmitDataActivity.this);
            // userName = preferences.getString(MobileConstants.USERNAME, "");
            userName = preferences.getString(MobileConstants.USERNAME, "");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitDataActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("LT Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(TransmitDataActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            //  setSupportActionBar(toolbar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_transmit:
                //  if (ConnectivityChecking.checkConnection(TransmitDataActivity.this)) {
                if (surveyVoList.size() > 0) {
                    transmitData();
                } else if (surveyVoList_LAT_LAN.size() > 0) {
                    transmitEDITData();
                } else if (surveyVoListEditPoleDetails.size() > 0) {
                    transmitEditPolesData();
                } else if (surveyVoList_Source_pole.size() > 0) {
                    transmitEditSourcePolesData();
                } else {
                    Toast.makeText(this, "NO Dat to Transmit", Toast.LENGTH_SHORT).show();
                }

               /* }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }*/

                break;
            case R.id.btn_transmit_images:
                if (surveyVoList.size() > 0) {
                    Toast.makeText(this, "Please Transmit Pole Data First..", Toast.LENGTH_SHORT).show();
                } else {
                    if (imagesList.size() > 0) {
                        if (imageCount > 0) {
                            transmitImagesData();
                        } else {
                            Toast.makeText(this, "No Data to Transmit", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "No Data to Transmit", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
        }
    }

    private void transmitImagesData() {
        try {
            progressDialog.show();
            btn_transmit_images.setVisibility(View.GONE);

            LTImageRequest ltSurveyRequestVo = new LTImageRequest();
            List<LTImagesModel> surveyedRequestDataVosList = new ArrayList<>();
            ltSurveyRequestVo.setFEEDER_ID("01222301");

            for (SurveyDataVo surveyDataVo : imagesList) {
                LTImagesModel ltSurveyRequestDataVo = new LTImagesModel();
                ltSurveyRequestDataVo.setDTR_STRUCTURE(surveyDataVo.getDTR_STRUCTURE());
                ltSurveyRequestDataVo.setLT_CKT_CODE(surveyDataVo.getLT_CIRCUIT());
                ltSurveyRequestDataVo.setPOLE_NO(String.valueOf(surveyDataVo.getPOLE_NO()));
                ltSurveyRequestDataVo.setIMAGE_1(surveyDataVo.getIMAGE_1().equals("N") ? "N" : ConvertImage.convertBase64(surveyDataVo.getIMAGE_1()));
                ltSurveyRequestDataVo.setIMAGE_2(surveyDataVo.getIMAGE_2().equals("N") ? "N" : ConvertImage.convertBase64(surveyDataVo.getIMAGE_2()));
                ltSurveyRequestDataVo.setIMAGE_3(surveyDataVo.getIMAGE_3().equals("N") ? "N" : ConvertImage.convertBase64(surveyDataVo.getIMAGE_3()));
                surveyedRequestDataVosList.add(ltSurveyRequestDataVo);

            }
            ltSurveyRequestVo.setImagesList(surveyedRequestDataVosList);
            Gson gson = new Gson();
            String json = gson.toJson(ltSurveyRequestVo);

            Log.v("DATA===>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.postLTImageData(ltSurveyRequestVo);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();
                        Log.v("DATA===>>", "LT" + response_vo.getSTATUS());
                        // http://10.150.250.166:8080/LtGis/rest/ltsurvey/gisltposting1
                        // http://10.150.250.166:8080/LtGis/rest/ltsurvey/gisltposting


                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearLTImageData();
                            imagesList.clear();
                            Intent intent = new Intent(TransmitDataActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(TransmitDataActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            btn_transmit_images.setVisibility(View.VISIBLE);
                            Toast.makeText(TransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        btn_transmit_images.setVisibility(View.VISIBLE);
                        Toast.makeText(TransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    btn_transmit_images.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transmitData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            List<LTCktsResponseDataVo> ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData("");
            List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
            LTSurveyRequestVo ltSurveyRequestVo = new LTSurveyRequestVo();
            List<LTSurveyRequestDataVo> surveyedRequestDataVosList = new ArrayList<>();
            ltSurveyRequestVo.setIMEI_NO(ConnectivityChecking.getIMEI_NO(TransmitDataActivity.this));
            ltSurveyRequestVo.setVERSION(MobileConstants.APP_VERSION);
            ltSurveyRequestVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
            ltSurveyRequestVo.setOFFICE_ID(loginDataResponses.get(0).getOFFICE_ID());

            ltSurveyRequestVo.setCREATED_BY(userName);

            for (SurveyDataVo surveyDataVo : surveyVoList) {
                LTSurveyRequestDataVo ltSurveyRequestDataVo = new LTSurveyRequestDataVo();
                ltSurveyRequestDataVo.setCREATED_BY(userName);
                ltSurveyRequestDataVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
                ltSurveyRequestDataVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());
                ltSurveyRequestDataVo.setIME_NO(ConnectivityChecking.getIMEI_NO(TransmitDataActivity.this));
                ltSurveyRequestDataVo.setFEEDER_ID(surveyDataVo.getFEEDER_ID());
                ltSurveyRequestDataVo.setGPS_ACCURACY(surveyDataVo.getGPS_ACCURACY());
                FeederId = surveyDataVo.getFEEDER_ID();
                ltSurveyRequestDataVo.setDTR_STRUCTURE(surveyDataVo.getDTR_STRUCTURE());
                ltSurveyRequestDataVo.setLT_CKT_CODE(surveyDataVo.getLT_CIRCUIT());
                ltSurveyRequestDataVo.setEND_POLE(CommanUtilits.isChecking(surveyDataVo.getIS_END_POLE()));
                ltSurveyRequestDataVo.setLINE_TYPE(CommanUtilits.isChecking(surveyDataVo.getLINE_TYPE()));
                ltSurveyRequestDataVo.setLINE_CONFIG(CommanUtilits.isChecking(surveyDataVo.getLINE_CONFIGURATION()));
                ltSurveyRequestDataVo.setIS_OTHERSCS(CommanUtilits.isChecking(surveyDataVo.getIS_SERVICES_OTHERTHAN_SL()));
                ltSurveyRequestDataVo.setIS_STREETLIGHT(CommanUtilits.isChecking(surveyDataVo.getIS_STREETLIGHT_AV()));
                //ltSurveyRequestDataVo.setDTR_STRUCTURE("");
                ltSurveyRequestDataVo.setNO_AGL_SCS(surveyDataVo.getNO_AGL_SERVICES());
                ltSurveyRequestDataVo.setNO_AQUA_SCS(surveyDataVo.getNO_AQUA_SERVICES());
                ltSurveyRequestDataVo.setNO_INDUSTRIAL_SCS(surveyDataVo.getNO_INDUSTRIAL_SERVICES());
                ltSurveyRequestDataVo.setPOLE_NO(String.valueOf(surveyDataVo.getPOLE_NO()));
                ltSurveyRequestDataVo.setFROM_POLE(String.valueOf(surveyDataVo.getFROM_POLE()));
                ltSurveyRequestDataVo.setPOLE_TYPE(CommanUtilits.isChecking(surveyDataVo.getPOLE_TYPE()));
                ltSurveyRequestDataVo.setPOLE_HEIGHT(surveyDataVo.getPOLE_HEIGHT());
               /* @SerializedName("POLE_SUPPORT")
                private String POLE_SUPPORT;*/
                ltSurveyRequestDataVo.setPOLE_SUPPORT(CommanUtilits.isChecking(surveyDataVo.getSTRUT_STAY()));
                ltSurveyRequestDataVo.setPIN_CUT(CommanUtilits.isChecking(surveyDataVo.getPIN_CUT()));
                ltSurveyRequestDataVo.setIS_DOUBLEFEEDING(CommanUtilits.isChecking(surveyDataVo.getIS_DOUBLE_FEEDING()));
                ltSurveyRequestDataVo.setTAPPING_POINT(CommanUtilits.isChecking(surveyDataVo.getTAPPING_POINT()));
                ltSurveyRequestDataVo.setTAPPING_LOCNAME(CommanUtilits.isChecking(surveyDataVo.getTAPPING_LOCATION()));
                ltSurveyRequestDataVo.setTAPPING_POINTS_NO(surveyDataVo.getNO_OF_SPURLINES());
                ltSurveyRequestDataVo.setCONDUCTOR_SIZE(CommanUtilits.isChecking(surveyDataVo.getCONDUCTOR_SIZE()));
                ltSurveyRequestDataVo.setIS_MULTICKT(CommanUtilits.isChecking(surveyDataVo.getIS_MULTI_CIRCUIT()));
                /*@SerializedName("CKT_NO")
                private String CKT_NO;*/
                ltSurveyRequestDataVo.setCKT_NO(surveyDataVo.getNO_OF_CIRCUITS());

                /*@SerializedName("CKT_TYPE")
                private String CKT_TYPE;*/
                ltSurveyRequestDataVo.setCKT_TYPE(CommanUtilits.isChecking(surveyDataVo.getMULTI_VOLTAGE()));
                ltSurveyRequestDataVo.setNO_33KV(surveyDataVo.getNO_OF33KV_CKTS());
                ltSurveyRequestDataVo.setNO_11KV(surveyDataVo.getNO_OF11KV_CKTS());
                ltSurveyRequestDataVo.setNO_6_3KV(surveyDataVo.getNO_OF6_3KV_VOLTAGE_CKTS());
                ltSurveyRequestDataVo.setNO_LT(surveyDataVo.getNO_OF_LT_CKTS());
                ltSurveyRequestDataVo.setPOLE_PAINT_CODE(CommanUtilits.isChecking(surveyDataVo.getPOLE_PAINTING_CODE()));
                ltSurveyRequestDataVo.setGLAT(surveyDataVo.getLAT());
                ltSurveyRequestDataVo.setGLONG(surveyDataVo.getLAN());
                ltSurveyRequestDataVo.setREMARKS(CommanUtilits.isChecking(surveyDataVo.getREMARKS()));
                ltSurveyRequestDataVo.setLOCATION_TYPE(CommanUtilits.isChecking(surveyDataVo.getLOCATION_TYPE()));
                ltSurveyRequestDataVo.setSERVICE_PILLERTYPE(CommanUtilits.isChecking(surveyDataVo.getSERVICE_PILLERTYPE()));
                ltSurveyRequestDataVo.setFEEDERPILLET_OUTGN_CKT(surveyDataVo.getFEEDERPILLET_OUTGN_CKT());
                ltSurveyRequestDataVo.setCABLE_SIZE(CommanUtilits.isChecking(surveyDataVo.getCABLE_SIZE()));
                ltSurveyRequestDataVo.setNO_CABLES(surveyDataVo.getNO_CABLES());

                ltSurveyRequestDataVo.setIS_CROSSINGS_AV(surveyDataVo.getIS_CROSSINGS_AV());
                ltSurveyRequestDataVo.setLT_CROSS(surveyDataVo.getNOOF_LT_CROSSINGS());
                ltSurveyRequestDataVo.setLT_6_3KV_CROSS(surveyDataVo.getNOOF_6_3KV_CROSSINGS());
                ltSurveyRequestDataVo.setLT_11KV_CROSS(surveyDataVo.getNOOF_11KV_CROSSINGS());
                ltSurveyRequestDataVo.setLT_33KV_CROSS(surveyDataVo.getNOOF_33KV_CROSSINGS());
                ltSurveyRequestDataVo.setTC_CROSS(surveyDataVo.getNOOF_TC_CROSSINGS());
                ltSurveyRequestDataVo.setTM_CROSS(surveyDataVo.getNOOF_TM_CROSSINGS());

                //NEW
                ltSurveyRequestDataVo.setDEFFECTS_AVAILABLE(surveyDataVo.getDEFFECTS_AVAILABLE());
                ltSurveyRequestDataVo.setPOLE_DEFFECTS(surveyDataVo.getPOLE_DEFFECTS());
                ltSurveyRequestDataVo.setCONDUCTOR_DEFFECTS(surveyDataVo.getCONDUCTOR_DEFFECTS());
                ltSurveyRequestDataVo.setINSULATOR_DEFFECTS(surveyDataVo.getINSULATOR_DEFFECTS());
                ltSurveyRequestDataVo.setCROSSARM_DEFFECTS(surveyDataVo.getCROSSARM_DEFFECTS());
                ltSurveyRequestDataVo.setSTRUT_DEFFECTS(surveyDataVo.getSTRUT_DEFFECTS());
                ltSurveyRequestDataVo.setSTRAY_DEFFECTS(surveyDataVo.getSTRAY_DEFFECTS());
                ltSurveyRequestDataVo.setHTLT_CLEARANCE(surveyDataVo.getHTLT_CLEARANCE());
                ltSurveyRequestDataVo.setINTERMEDIATE_POLE(surveyDataVo.getINTERMEDIATE_POLE());
                ltSurveyRequestDataVo.setRESTRINGING(surveyDataVo.getRESTRINGING());
                ltSurveyRequestDataVo.setDEFFECTS_AVAILABLE(surveyDataVo.getDEFFECTS_AVAILABLE());
                ltSurveyRequestDataVo.setOTHER_DEFFECTS(surveyDataVo.getOTHER_DEFECTS());
                ltSurveyRequestDataVo.setNUM_SCS(surveyDataVo.getNUM_SCS());
                ltSurveyRequestDataVo.setNUM_OTHER_CABLES(surveyDataVo.getNUM_OTHER_CABLES());

                //MULTI CKT


                if (surveyDataVo.getLINE_TYPE().equalsIgnoreCase("OverHead")) {
                    if (surveyDataVo.getIS_MULTI_CIRCUIT().equalsIgnoreCase("Yes")) {
                        ArrayList<MultiRequestVO> mc_list = new ArrayList<>();
                        if (!surveyDataVo.getMC_33KV_FEEDER_ID_1().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("33KV");
                            String selectedFeeder = surveyDataVo.getMC_33KV_FEEDER_ID_1();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_33KV_POLE_1());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_33KV_NOT_IN_LIST_1());
                            mc_list.add(vo);
                        }
                        if (!surveyDataVo.getMC_33KV_FEEDER_ID_2().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("33KV");
                            String selectedFeeder = surveyDataVo.getMC_33KV_FEEDER_ID_2();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_33KV_POLE_2());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_33KV_NOT_IN_LIST_2());
                            mc_list.add(vo);
                        }
                        if (!surveyDataVo.getMC_33KV_FEEDER_ID_3().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("33KV");
                            String selectedFeeder = surveyDataVo.getMC_33KV_FEEDER_ID_3();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_33KV_POLE_3());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_33KV_NOT_IN_LIST_3());
                            mc_list.add(vo);
                        }

                        if (!surveyDataVo.getMC_11KV_FEEDER_ID_1().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("11KV");
                            String selectedFeeder = surveyDataVo.getMC_11KV_FEEDER_ID_1();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_11KV_POLE_1());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_11KV_NOT_IN_LIST_1());
                            mc_list.add(vo);
                        }
                        if (!surveyDataVo.getMC_11KV_FEEDER_ID_2().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("11KV");
                            String selectedFeeder = surveyDataVo.getMC_11KV_FEEDER_ID_2();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_11KV_POLE_2());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_11KV_NOT_IN_LIST_2());
                            mc_list.add(vo);
                        }
                        if (!surveyDataVo.getMC_11KV_FEEDER_ID_3().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("11KV");
                            String selectedFeeder = surveyDataVo.getMC_11KV_FEEDER_ID_2();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_11KV_POLE_3());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_11KV_NOT_IN_LIST_3());
                            mc_list.add(vo);
                        }

                        if (!surveyDataVo.getMC_LT_FEEDER_ID_1().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("LT");
                            String selectedFeeder = surveyDataVo.getMC_LT_FEEDER_ID_1();
                       /* String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_LT_POLE_1());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_LT_NOT_IN_LIST_1());
                            mc_list.add(vo);
                        }
                        if (!surveyDataVo.getMC_LT_FEEDER_ID_2().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("LT");
                            String selectedFeeder = surveyDataVo.getMC_LT_FEEDER_ID_2();
                        /*String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_LT_POLE_2());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_LT_NOT_IN_LIST_2());
                            mc_list.add(vo);
                        }
                        if (!surveyDataVo.getMC_LT_FEEDER_ID_3().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("LT");
                            String selectedFeeder = surveyDataVo.getMC_LT_FEEDER_ID_2();
                       /* String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyDataVo.getMC_LT_POLE_3());
                            vo.setMANUAL_ENTRY(surveyDataVo.getMC_LT_NOT_IN_LIST_3());
                            mc_list.add(vo);
                        }

                        ltSurveyRequestDataVo.setMC(mc_list);

                    }
                }

                //JHC PHASE 1
                ltSurveyRequestDataVo.setPOLE_SURVEY_TYPE(surveyDataVo.getPOLE_SURVEY_TYPE());
                ltSurveyRequestDataVo.setLINE_SURVEY_TYPE(surveyDataVo.getLINE_SURVEY_TYPE());

                //CURRENT LOCATION
                ltSurveyRequestDataVo.setCURRENT_LAT(surveyDataVo.getCURRENT_LAT());
                ltSurveyRequestDataVo.setCURRENT_LONG(surveyDataVo.getCURRENT_LONG());

                //ADDED ON 26-07-2024
                ltSurveyRequestDataVo.setSTL_METER(CommanUtilits.isChecking(surveyDataVo.getSTL_METER()));
                ltSurveyRequestDataVo.setSTL_SWITCH(CommanUtilits.isChecking(surveyDataVo.getSTL_SWITCH()));
                ltSurveyRequestDataVo.setSTL_SOURCE(CommanUtilits.isChecking(surveyDataVo.getSTL_SOURCE()));


                ltSurveyRequestDataVo.setPOLE_CONCRETE(surveyDataVo.getPOLE_CONCRETE());
                if (DashBoard.IS_RDSS_USER){
                    ltSurveyRequestDataVo.setRDSS_SCHEME_TYPE(surveyDataVo.getRDSS_SCHEME_TYPE());
                    ltSurveyRequestDataVo.setADDRESS_1(surveyDataVo.getADDRESS_1());
                    ltSurveyRequestDataVo.setADDRESS_2(surveyDataVo.getADDRESS_2());
                }else {
                    ltSurveyRequestDataVo.setRDSS_SCHEME_TYPE(null);
                    ltSurveyRequestDataVo.setADDRESS_1(null);
                    ltSurveyRequestDataVo.setADDRESS_2(null);
                }



                surveyedRequestDataVosList.add(ltSurveyRequestDataVo);

            }
            ltSurveyRequestVo.setFEEDER_ID(FeederId);
            ltSurveyRequestVo.setLT_REQUST(surveyedRequestDataVosList);
            Gson gson = new Gson();
            String json = gson.toJson(ltSurveyRequestVo);

            // Log.v("DATA===>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.postSurveyData(ltSurveyRequestVo);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {

                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();
                        // http://10.150.250.166:8080/LtGis/rest/ltsurvey/gisltposting1
                        // http://10.150.250.166:8080/LtGis/rest/ltsurvey/gisltposting


                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            surveyVoList.clear();
                            arcGISDatabase.clearSurveyData();
                            tv_escount.setText("No.of Surveyed Poles (0)");

                            if (imageCount == 0) {
                                Toast.makeText(TransmitDataActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TransmitDataActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                transmitImagesData();
                            }

                        } else {
                            Toast.makeText(TransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transmitEDITData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            List<SurveyDataVo> list = surveyVoList_LAT_LAN;
            JsonArray dataArray = new JsonArray();

            for (SurveyDataVo vo : surveyVoList_LAT_LAN) {
                JsonObject obj = new JsonObject();
                obj.addProperty("FEEDER_ID", vo.getFEEDER_ID());
                obj.addProperty("GLONG", vo.getLAN());
                obj.addProperty("GLAT", vo.getLAT());
                obj.addProperty("MODE", "LOCATION");
                obj.addProperty("ID", vo.getID());

                dataArray.add(obj);
            }


            Gson gson = new Gson();
            String json = gson.toJson(dataArray);

            Log.v("DATA===>=====>>>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.updateLatLan(dataArray);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();

                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearEditLatLanData();
                            Toast.makeText(TransmitDataActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TransmitDataActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(TransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void transmitEditPolesData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            List<SurveyDataVo> list = surveyVoListEditPoleDetails;
            JsonArray dataArray = new JsonArray();

            for (SurveyDataVo vo : surveyVoListEditPoleDetails) {
                JsonObject obj = new JsonObject();
                obj.addProperty("FEEDER_ID", vo.getFEEDER_ID());
                obj.addProperty("POLE_TYPE", vo.getPOLE_TYPE());
                obj.addProperty("POLE_HEIGHT", vo.getPOLE_HEIGHT());
                obj.addProperty("POLE_SUPPORT", vo.getSTRUT_STAY());
                obj.addProperty("INSULATOR_TYPE", vo.getTYPE_OF_INSULATOR());
                obj.addProperty("IS_DOUBLEFEEDING", vo.getIS_DOUBLE_FEEDING());
                obj.addProperty("POLE_PAINT_CODE", vo.getPOLE_PAINTING_CODE());
                obj.addProperty("POLE_CONCRETE", vo.getPOLE_CONCRETE());
                obj.addProperty("PIN_CUT", vo.getPIN_CUT());
                obj.addProperty("LINE_CONFIG", vo.getLINE_CONFIGURATION());
                obj.addProperty("MODE", "POLE");
                obj.addProperty("ID", vo.getID());

                dataArray.add(obj);
            }


            Gson gson = new Gson();
            String json = gson.toJson(dataArray);

            Log.v("DATA===>=====>>>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.updateLatLan(dataArray);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();

                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearEditPoleDetailsData();
                            Toast.makeText(TransmitDataActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TransmitDataActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(TransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transmitEditSourcePolesData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            List<SurveyDataVo> list = surveyVoList_Source_pole;
            JsonArray dataArray = new JsonArray();

            for (SurveyDataVo vo : surveyVoList_Source_pole) {
                JsonObject obj = new JsonObject();
                obj.addProperty("FEEDER_ID", vo.getFEEDER_ID());
                obj.addProperty("MODE", "SOURCE");
                obj.addProperty("ID", vo.getID());
                obj.addProperty("FROM_POLE", vo.getFROM_POLE());

                dataArray.add(obj);
            }


            Gson gson = new Gson();
            String json = gson.toJson(dataArray);

            Log.v("DATA===>=====>>>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.updateLatLan(dataArray);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();

                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearEditSourcePoleData();
                            Toast.makeText(TransmitDataActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TransmitDataActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(TransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransmitDataActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}



