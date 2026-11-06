package com.apepdcl.gis_survey.module_33KV;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.module_11KV.ImageList;
import com.apepdcl.gis_survey.module_11KV.ImageRequest;
import com.apepdcl.gis_survey.module_11KV.transmit.TransmitDataActivity;
import com.apepdcl.gis_survey.module_33KV.vo.SurveyVo;
import com.apepdcl.gis_survey.module_33KV.vo.SurveyedRequestDataVo;
import com.apepdcl.gis_survey.module_33KV.vo.SurveyedRequestVo;
import com.apepdcl.gis_survey.module_33KV.vo.SuveyedResponseVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.MultiRequestVO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransmitDataHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_escount, tv_image_numbers;
    private Button btn_transmit;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyVo> surveyVoList, surveyVosEditLatLan33, surveyVosEditPoleDetails33, surveyVosEditSourcePole;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String SurveyFlag = "";
    private SharedPreferences preferences;
    private String userName, sessionId;
    private List<LoginDataResponse> dataResponseList;

    private List<SurveyVo> imageList;
    int imageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit_data_home);
        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("33kV Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            tv_image_numbers = (TextView) findViewById(R.id.tv_image_numbers);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabase(this);
            surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            surveyVosEditLatLan33 = arcGISDatabase.getAllEditSurveyLatLan();
            surveyVosEditPoleDetails33 = arcGISDatabase.getEditPoleDetailsSurveyData();
            surveyVosEditSourcePole = arcGISDatabase.getEditSourcePoleSurveyData();
            imageList = arcGISDatabase.getImageData("");
            if (surveyVoList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + surveyVoList.size() + ")");
            } else if (surveyVosEditLatLan33.size() > 0) {
                tv_escount.setText("No.of Edit Lat Lan Surveyed Poles" + ":(" + surveyVosEditLatLan33.size() + ")");
            } else if (surveyVosEditPoleDetails33.size() > 0) {
                tv_escount.setText("No.of Edit Pole Details Surveyed Poles" + ":(" + surveyVosEditPoleDetails33.size() + ")");
            } else if (surveyVosEditSourcePole.size() > 0) {
                tv_escount.setText("No.of Edit Source Pole Details Surveyed Poles" + ":(" + surveyVosEditSourcePole.size() + ")");
            }
            progressDialog = new ProgressDialog(TransmitDataHomeActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstants.USERNAME, "");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitDataHomeActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }


            for (int i = 0; i < imageList.size(); i++) {
                //  System.out.println("IMAGE 1 : "+imageList.get(i).getIMAGE_1());
                //  System.out.println("IMAGE 2 : "+imageList.get(i).getIMAGE_2());
                //  System.out.println("IMAGE 3 : "+imageList.get(i).getIMAGE_3());
                if (!imageList.get(i).getIMAGE_1().equals("N")) {
                    imageCount++;
                }
                if (!imageList.get(i).getIMAGE_2().equals("N")) {
                    imageCount++;
                }
                if (!imageList.get(i).getIMAGE_3().equals("N")) {
                    imageCount++;
                }
            }

            if (imageList.size() > 0) {
                tv_image_numbers.setText("No.of Images" + ":(" + imageCount + ")");
            } else {
                tv_image_numbers.setText("No.of Images:(0)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_transmit:
                if (ConnectivityChecking.checkConnection(TransmitDataHomeActivity.this)) {
                    if (surveyVoList.size() > 0) {
                        transmitData();
                    } else if (surveyVosEditLatLan33.size() > 0) {
                        transmitEDITData();
                    } else if (surveyVosEditPoleDetails33.size() > 0) {
                        transmitPoleDetailsEDITData();
                    } else if (surveyVosEditSourcePole.size() > 0) {
                        transmitSourcePoleEDITData();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public void transmitEDITData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            List<SurveyVo> list = surveyVosEditLatLan33;
            JsonArray dataArray = new JsonArray();

            for (SurveyVo vo : surveyVosEditLatLan33) {
                JsonObject obj = new JsonObject();
                obj.addProperty("FEEDER_ID", vo.getFeeder_id());
                obj.addProperty("GLONG", vo.getLan());
                obj.addProperty("GLAT", vo.getLat());
                obj.addProperty("MODE", "LOCATION");
                obj.addProperty("ID", vo.getID());

                dataArray.add(obj);
            }


            Gson gson = new Gson();
            String json = gson.toJson(dataArray);

            Log.v("DATA===>=====>>>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.updateMultipleDataByMode(dataArray);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();

                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearEditLatLanData();
                            if (surveyVosEditPoleDetails33.size() > 0) {
                                transmitPoleDetailsEDITData();
                            } else if (surveyVosEditSourcePole.size() > 0) {
                                transmitSourcePoleEDITData();
                            } else {
                                Toast.makeText(TransmitDataHomeActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TransmitDataHomeActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            Toast.makeText(TransmitDataHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void transmitPoleDetailsEDITData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            List<SurveyVo> list = surveyVosEditPoleDetails33;
            JsonArray dataArray = new JsonArray();

            for (SurveyVo vo : surveyVosEditPoleDetails33) {
                JsonObject obj = new JsonObject();
                obj.addProperty("FEEDER_ID", vo.getFeeder_id());
                obj.addProperty("POLE_HEIGHT", vo.getPole_height());
                obj.addProperty("POLE_SUPPORT", vo.getPole_support());
                obj.addProperty("POLE_PAINT_CODE", vo.getPole_paintingcode());
                obj.addProperty("POLE_CONCRETE", vo.getPOLE_CONCRETE());
                obj.addProperty("PIN_CUT", vo.getPinpoint());
                obj.addProperty("POLE_TYPE", vo.getPole_type());
                obj.addProperty("POLE_STRUCTURE", vo.getPoleStructure());
                obj.addProperty("MODE", "POLE");
                obj.addProperty("ID", vo.getID());

                dataArray.add(obj);
            }

            Gson gson = new Gson();
            String json = gson.toJson(dataArray);

            Log.v("DATA===>=====>>>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.updateMultipleDataByMode(dataArray);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();

                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearEditPoleDetails();
                            if (surveyVosEditSourcePole.size() > 0) {
                                transmitSourcePoleEDITData();
                            } else {
                                Toast.makeText(TransmitDataHomeActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TransmitDataHomeActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            Toast.makeText(TransmitDataHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transmitSourcePoleEDITData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            List<SurveyVo> list = surveyVosEditSourcePole;
            JsonArray dataArray = new JsonArray();

            for (SurveyVo vo : surveyVosEditSourcePole) {
                JsonObject obj = new JsonObject();
                obj.addProperty("FEEDER_ID", vo.getFeeder_id());
                obj.addProperty("FROM_POLE", vo.getFrom_pole());
                obj.addProperty("MODE", "SOURCE");
                obj.addProperty("ID", vo.getID());

                dataArray.add(obj);
            }

            Gson gson = new Gson();
            String json = gson.toJson(dataArray);

            Log.v("DATA===>=====>>>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.updateMultipleDataByMode(dataArray);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();

                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearEditSourcePoleData();
                            if (surveyVosEditPoleDetails33.size() > 0) {
                                transmitPoleDetailsEDITData();
                            } else if (surveyVosEditLatLan33.size() > 0) {
                                transmitSourcePoleEDITData();
                            } else {
                                Toast.makeText(TransmitDataHomeActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TransmitDataHomeActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(TransmitDataHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {

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
            SurveyedRequestVo surveyedRequestVo = new SurveyedRequestVo();
            List<SurveyedRequestDataVo> surveyedRequestDataVosList = new ArrayList<>();
            dataResponseList = new com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase(this).getUserMasterData();
            surveyedRequestVo.setUSER_ID(userName);
            surveyedRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(TransmitDataHomeActivity.this));
            surveyedRequestVo.setSESSION_ID(sessionId);
            surveyedRequestVo.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVo.setIS_SURVEY_COM("N");
            surveyedRequestVo.setFEEDER_ID(surveyVoList.get(0).getFeeder_id());


            for (SurveyVo surveyVo : surveyVoList) {
                SurveyedRequestDataVo requestDataVo = new SurveyedRequestDataVo();
                requestDataVo.setFEEDER_ID(surveyVo.getFeeder_id());
                requestDataVo.setVOLTAGE_LEVEL("33kv");
                requestDataVo.setPOLE_NO(surveyVo.getPole_no());
                requestDataVo.setFROM_POLE(surveyVo.getFrom_pole());
                surveyedRequestVo.setGPS_ACCURACY(surveyVo.getGPS_ACCURACY());
                requestDataVo.setPOLE_TYPE(CommanUtilits.isChecking(surveyVo.getPole_type()));
                requestDataVo.setPOLE_HEIGHT(surveyVo.getPole_height());
                // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
                requestDataVo.setPOLE_SUPPORT(CommanUtilits.isChecking(surveyVo.getPole_support()));
                requestDataVo.setTAPPING_POINT(CommanUtilits.isChecking(surveyVo.getTapping_point()));
                requestDataVo.setCONDUCTOR_SIZE(surveyVo.getConductor_size());
                requestDataVo.setPOLE_PAINT_CODE(CommanUtilits.isChecking(surveyVo.getPole_paintingcode()));
                requestDataVo.setSEC_CODE(dataResponseList.get(0).getOFFICE_ID());
                requestDataVo.setCREATED_BY(userName);
                requestDataVo.setGLAT(surveyVo.getLat());
                requestDataVo.setGLONG(surveyVo.getLan());
                requestDataVo.setNO_CKT(CommanUtilits.isChecking(surveyVo.getIsMultiCircuit()));
                requestDataVo.setCKT_TYPE(CommanUtilits.isChecking(surveyVo.getCircuitType()));
                requestDataVo.setIME_NO(CommanUtilits.getIMEI_NO(TransmitDataHomeActivity.this));

                //28-01-2025 New Change
                if (surveyVo.getLineType().equals("OverHead")) {
                    if (surveyVo.getPole_configuration().equalsIgnoreCase("33/11kv SS-Bay")) {
                        requestDataVo.setPOLE_CONFIG(CommanUtilits.isChecking("SS-BAY"));
                        requestDataVo.setSS_ID(surveyVo.getSsId());
                        requestDataVo.setSS_NAME(CommanUtilits.isChecking(surveyVo.getSs_name()));
                    } else if (surveyVo.getPole_configuration().equalsIgnoreCase("EHT SS-Bay")) {
                        requestDataVo.setPOLE_CONFIG(CommanUtilits.isChecking("SS-BAY"));
                    } else {
                        requestDataVo.setPOLE_CONFIG(CommanUtilits.isChecking(surveyVo.getPole_configuration()));
                    }
                }
                requestDataVo.setDEVICE_ID(CommanUtilits.isChecking(surveyVo.getDEVICE_ID()));
                requestDataVo.setPIN_CUT(CommanUtilits.isChecking(surveyVo.getPinpoint()));
                requestDataVo.setPOLE_STRUCTURE(CommanUtilits.isChecking(surveyVo.getPoleStructure()));
                requestDataVo.setDIFF_VOLTAGES(CommanUtilits.isChecking(surveyVo.getDiffVoltage()));

                if (surveyVo.getIsEndPole() != null) {
                    requestDataVo.setEND_POLE(CommanUtilits.isChecking(surveyVo.getIsEndPole()));
                } else {
                    requestDataVo.setEND_POLE("");
                }
                // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
                requestDataVo.setLINE_TYPE(CommanUtilits.isChecking(surveyVo.getLineType()));
                requestDataVo.setCKT_NO(surveyVo.getNoofCircuits());

                requestDataVo.setSTJOINT_RMU(CommanUtilits.isChecking(surveyVo.getStjoint_rmu()));
                requestDataVo.setRMU_TYPE(CommanUtilits.isChecking(surveyVo.getRmu_type()));
                requestDataVo.setTAPPING_POINTS(surveyVo.getNoofTappings());
                requestDataVo.setTAPPING_LOCNAME(CommanUtilits.isChecking(surveyVo.getTappingLocation()));
                requestDataVo.setREMARKS(CommanUtilits.isChecking(surveyVo.getRemarks()));
                requestDataVo.setWO_ID(surveyVo.getWoId());
                requestDataVo.setIS_FEEDER_TYPE(surveyVo.getIsFeederType());
                requestDataVo.setHT_SCNO(surveyVo.getHtServiceNumber());
                requestDataVo.setSURVEY_TYPE(surveyVo.getSurvaytype());

                //MULTI CKT and OTHER CABLES
                requestDataVo.setNO_33KV(surveyVo.getNO_OF33KV_CKTS());
                requestDataVo.setNO_11KV(surveyVo.getNO_OF11KV_CKTS());
                requestDataVo.setNO_LT(surveyVo.getNO_OF_LT_CKTS());

                if (surveyVo.getLineType().equalsIgnoreCase("OverHead")) {

                    //CROSSINGS
                    requestDataVo.setCROSSING_TYPE(surveyVo.getCROSSING_TYPE());
                    requestDataVo.setCROSS_6_3KV_ID(surveyVo.getCROSS_6_3KV_ID());
                    requestDataVo.setCROSS_11KV_ID(surveyVo.getCROSS_11KV_ID());
                    requestDataVo.setCROSS_33KV_ID(surveyVo.getCROSS_33KV_ID());
                    requestDataVo.setCROSS_LT_CKT(surveyVo.getCROSS_LT_CKT());
                    requestDataVo.setCROSS_COMMUN(surveyVo.getCROSS_COMMUN());
                    requestDataVo.setCROSS_EHT(surveyVo.getCROSS_EHT());
                    requestDataVo.setCROSS_REMARKS(surveyVo.getCROSS_REMARKS());

                    //DEFECTS
                    requestDataVo.setDEFFECTS_AVAILABLE(surveyVo.getDEFFECTS_AVAILABLE());
                    requestDataVo.setPOLE_DEFECTS(surveyVo.getPOLE_DEFECTS());
                    requestDataVo.setCONDUCTOR_DEFECTS(surveyVo.getCONDUCTOR_DEFECTS());
                    requestDataVo.setAB_SWITCH_DEFECTS(surveyVo.getAB_SWITCH_DEFECTS());
                    requestDataVo.setINSULATOR_DEFECTS(surveyVo.getINSULATOR_DEFECTS());
                    requestDataVo.setCROSSARM_DEFECTS(surveyVo.getCROSSARM_DEFECTS());
                    requestDataVo.setTOPCLEAT_DEFECTS(surveyVo.getTOPCLEAT_DEFECTS());
                    requestDataVo.setSTRUT_DEFECTS(surveyVo.getSTRUT_DEFECTS());
                    requestDataVo.setSTRAY_DEFECTS(surveyVo.getSTRAY_DEFECTS());
                    requestDataVo.setHTLT_CLEARANCE(surveyVo.getHTLT_CLEARANCE());
                    requestDataVo.setINTERMEDIATE_POLE(surveyVo.getINTERMEDIATE_POLE());
                    requestDataVo.setRESTRINGING(surveyVo.getRESTRINGING());
                    requestDataVo.setTREE_CLEARANCE(surveyVo.getTREE_CLEARANCE());
                    requestDataVo.setOTHER_DEFFECTS(surveyVo.getOTHER_DEFFECTS());


                    //OTHER CABLES
                    requestDataVo.setNUM_OTHER_CABLES(surveyVo.getNUM_OTHER_CABLES());

                    if (surveyVo.getIsMultiCircuit().equalsIgnoreCase("Yes")) {


                        ArrayList<MultiRequestVO> mc_list = new ArrayList<>();
                        if (!surveyVo.getMC_33KV_FEEDER_ID_1().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("33KV");
                            String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_1();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_1());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_1());
                            mc_list.add(vo);
                        }
                        if (!surveyVo.getMC_33KV_FEEDER_ID_2().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("33KV");
                            String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_2();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_2());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_2());
                            mc_list.add(vo);
                        }
                        if (!surveyVo.getMC_33KV_FEEDER_ID_3().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("33KV");
                            String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_3();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_3());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_3());
                            mc_list.add(vo);
                        }

                        if (!surveyVo.getMC_11KV_FEEDER_ID_1().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("11KV");
                            String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_1();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_1());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_1());
                            mc_list.add(vo);
                        }
                        if (!surveyVo.getMC_11KV_FEEDER_ID_2().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("11KV");
                            String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_2();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_2());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_2());
                            mc_list.add(vo);
                        }
                        if (!surveyVo.getMC_11KV_FEEDER_ID_3().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("11KV");
                            String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_2();
                            String[] splitFeederId = selectedFeeder.split("~");
                            selectedFeeder = splitFeederId[0];
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_3());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_3());
                            mc_list.add(vo);
                        }

                        if (!surveyVo.getMC_LT_FEEDER_ID_1().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("LT");
                            String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_1();
                       /* String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_1());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_1());
                            mc_list.add(vo);
                        }
                        if (!surveyVo.getMC_LT_FEEDER_ID_2().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("LT");
                            String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_2();
                        /*String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_2());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_2());
                            mc_list.add(vo);
                        }
                        if (!surveyVo.getMC_LT_FEEDER_ID_3().equals("N")) {
                            MultiRequestVO vo = new MultiRequestVO();
                            vo.setVOLTAGE_LEVEL("LT");
                            String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_2();
                       /* String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                            vo.setFEEDER_ID(selectedFeeder);
                            vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_3());
                            vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_3());
                            mc_list.add(vo);
                        }

                        requestDataVo.setMC(mc_list);

                    }
                }

                requestDataVo.setPOLE_CONCRETE(surveyVo.getPOLE_CONCRETE());
                requestDataVo.setCURRENT_LAT(surveyVo.getCURRENT_LAT());
                requestDataVo.setCURRENT_LONG(surveyVo.getCURRENT_LONG());


                surveyedRequestDataVosList.add(requestDataVo);
            }
            surveyedRequestVo.setSURVEY_DATA(surveyedRequestDataVosList);
            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVo);

            //  Log.v("DATA===>>", "" + json);


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.transmitData(surveyedRequestVo);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            arcGISDatabase.clearSurveyedData();
                            if (imageCount == 0) {
                                Toast.makeText(TransmitDataHomeActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TransmitDataHomeActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                transmitImageData();
                            }


                        } else {
                            Toast.makeText(TransmitDataHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataHomeActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transmitImageData() {
        progressDialog.show();
        //  btn_transmit_images.setVisibility(View.GONE);
        ImageRequest imageRequest = new ImageRequest();
        List<ImageList> list = new ArrayList<>();
        imageRequest.setFEEDER_ID(imageList.get(0).getFeeder_id());
        for (SurveyVo surveyVo : imageList) {
            ImageList requestDataVo = new ImageList();
            requestDataVo.setFEEDER_ID(CommanUtilits.isChecking(surveyVo.getFeeder_id()));
            requestDataVo.setPOLE_NO("" + surveyVo.getPole_no());

            requestDataVo.setIMAGE_1(surveyVo.getIMAGE_1().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_1()));
            requestDataVo.setIMAGE_2(surveyVo.getIMAGE_2().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_2()));
            requestDataVo.setIMAGE_3(surveyVo.getIMAGE_3().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_3()));
            list.add(requestDataVo);
        }
        imageRequest.setImageList(list);
        Gson gson = new Gson();
        String json = gson.toJson(imageRequest);

        // Log.v("DATA===>>", "" + json);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<SuveyedResponseVo> responseVoCall = retrofitService.transmitImageData(imageRequest);
        responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
            @Override
            public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    SuveyedResponseVo response_vo = response.body();
                    progressDialog.dismiss();
                    if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                        arcGISDatabase.clearImageData();
                        Intent intent = new Intent(TransmitDataHomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(TransmitDataHomeActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransmitDataHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TransmitDataHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(TransmitDataHomeActivity.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }
}