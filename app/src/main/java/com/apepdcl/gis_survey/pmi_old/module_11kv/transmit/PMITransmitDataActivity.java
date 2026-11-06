package com.apepdcl.gis_survey.pmi_old.module_11kv.transmit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofit11kv;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitClientInstance11KV;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIImageList;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIImageRequest;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiRequestVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedRequestDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISuveyedResponseVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMITransmitDataActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount, tv_image_numbers;
    private Button btn_transmit, btn_transmit_images;
    private PMI11kvArcGISDatabase arcGISDatabase;
    private List<PMISurveyVo> surveyVoList;
    private List<PMISurveyVo> imageList;
    private PMIRetrofit11kv retrofitService;
    private ProgressDialog progressDialog;
    private String SurveyFlag = "";
    private SharedPreferences preferences;
    private String userName, sessionId;
    private List<LoginDataResponse> dataResponseList;
    int imageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmitransmit_data);
        getSupportActionBar().hide();

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11Kv Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(PMITransmitDataActivity.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });

            progressDialog = CommanUtilits.progressDialog(this);
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            tv_image_numbers = (TextView) findViewById(R.id.tv_image_numbers);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit_images = (Button) findViewById(R.id.btn_transmit_images);
            btn_transmit.setOnClickListener(this);
            btn_transmit_images.setOnClickListener(this);
            arcGISDatabase = new PMI11kvArcGISDatabase(this);
            surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            imageList = arcGISDatabase.getImageData("");
            if (surveyVoList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + surveyVoList.size() + ")");
            } else {
                tv_escount.setText("No.of Surveyed Poles:(0)");
            }

            for (int i = 0; i < imageList.size(); i++) {
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
            //getSupportActionBar().setTitle("11Kv LineSurvey Test");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstants.USERNAME, "");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(PMITransmitDataActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_transmit:
                if (ConnectivityChecking.checkConnection(PMITransmitDataActivity.this)) {
                    if (surveyVoList.size() > 0) {
                        transmitData();
                    } else {
                        Toast.makeText(this, "No Data to transmit..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_transmit_images:
                if (ConnectivityChecking.checkConnection(PMITransmitDataActivity.this)) {
                    if (surveyVoList.size() > 0) {
                        Toast.makeText(this, "Please Transmit Poles data first", Toast.LENGTH_SHORT).show();
                    } else {
                        if (imageList.size() > 0) {
                            if (imageCount > 0) {
                                transmitImageData();
                            } else {
                                Toast.makeText(this, "No Images to transmit..", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, "No Images to transmit..", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private void transmitImageData() {
        progressDialog.show();
        btn_transmit_images.setVisibility(View.GONE);
        PMIImageRequest imageRequest = new PMIImageRequest();
        List<PMIImageList> list = new ArrayList<>();
        imageRequest.setFEEDER_ID(imageList.get(0).getFeeder_id());
        for (PMISurveyVo surveyVo : imageList) {
            PMIImageList requestDataVo = new PMIImageList();
            requestDataVo.setFEEDER_ID(CommanUtilits.isChecking(surveyVo.getFeeder_id()));
            requestDataVo.setPOLE_NO("" + surveyVo.getPole_no());

            try {
                //OLD
                requestDataVo.setIMAGE_1(surveyVo.getIMAGE_1().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_1()));
                requestDataVo.setIMAGE_2(surveyVo.getIMAGE_2().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_2()));
                requestDataVo.setIMAGE_3(surveyVo.getIMAGE_3().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_3()));


                //NEW OPEN CAMERA
                /*requestDataVo.setIMAGE_1(surveyVo.getIMAGE_1().equals("N") ? "N" : surveyVo.getIMAGE_1());
                requestDataVo.setIMAGE_2(surveyVo.getIMAGE_2().equals("N") ? "N" : surveyVo.getIMAGE_2());
                requestDataVo.setIMAGE_3(surveyVo.getIMAGE_3().equals("N") ? "N" : surveyVo.getIMAGE_3());*/

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Images Not Found", Toast.LENGTH_SHORT).show();
                break;
            }

            list.add(requestDataVo);


        }
        imageRequest.setImageList(list);
        Gson gson = new Gson();
        String json = gson.toJson(imageRequest);

        Log.v("DATA===>>", "" + json);
        //  writeToFile(json);
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<PMISuveyedResponseVo> responseVoCall = retrofitService.transmitImageData(imageRequest);
        responseVoCall.enqueue(new Callback<PMISuveyedResponseVo>() {
            @Override
            public void onResponse(Call<PMISuveyedResponseVo> call, Response<PMISuveyedResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    PMISuveyedResponseVo response_vo = response.body();
                    progressDialog.dismiss();
                    if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                        arcGISDatabase.clearImageData();
                        Intent intent = new Intent(PMITransmitDataActivity.this, PMIHomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(PMITransmitDataActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PMITransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PMITransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMISuveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(PMITransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void transmitData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            PMISurveyedRequestVo surveyedRequestVo = new PMISurveyedRequestVo();
            List<PMISurveyedRequestDataVo> surveyedRequestDataVosList = new ArrayList<>();
            dataResponseList = new PMIArcGISDatabase(this).getUserMasterData();
            surveyedRequestVo.setUSER_ID(userName);
            surveyedRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMITransmitDataActivity.this));
            surveyedRequestVo.setSESSION_ID(sessionId);
            surveyedRequestVo.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVo.setIS_SURVEY_COM("N");
            surveyedRequestVo.setFEEDER_ID(surveyVoList.get(0).getFeeder_id());

            for (PMISurveyVo surveyVo : surveyVoList) {
                PMISurveyedRequestDataVo requestDataVo = new PMISurveyedRequestDataVo();
                requestDataVo.setFEEDER_ID(CommanUtilits.isChecking(surveyVo.getFeeder_id()));
                requestDataVo.setVOLTAGE_LEVEL(CommanUtilits.isChecking(surveyVo.getVoltage_level()));
                requestDataVo.setPOLE_NO(surveyVo.getPole_no());
                requestDataVo.setFROM_POLE(surveyVo.getFrom_pole());
                requestDataVo.setPOLE_TYPE(CommanUtilits.isChecking(surveyVo.getPole_type()));
                requestDataVo.setPOLE_HEIGHT(CommanUtilits.isChecking(surveyVo.getPole_height()));
                // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
                requestDataVo.setPOLE_SUPPORT(CommanUtilits.isChecking(surveyVo.getPole_support()));
                requestDataVo.setTAPPING_POINT(CommanUtilits.isChecking(surveyVo.getTapping_point()));
                requestDataVo.setCONDUCTOR_SIZE(CommanUtilits.isChecking(surveyVo.getConductor_size()));
                requestDataVo.setPOLE_PAINT_CODE(CommanUtilits.isChecking(surveyVo.getPole_paintingcode()));
                requestDataVo.setSEC_CODE(CommanUtilits.isChecking(dataResponseList.get(0).getOFFICE_ID()));
                requestDataVo.setCREATED_BY(CommanUtilits.isChecking(userName));
                requestDataVo.setGLAT(surveyVo.getLat());
                requestDataVo.setGLONG(surveyVo.getLan());
                requestDataVo.setNO_CKT(CommanUtilits.isChecking(surveyVo.getIsMultiCircuit()));
                requestDataVo.setCKT_TYPE(CommanUtilits.isChecking(surveyVo.getCircuitType()));
                requestDataVo.setIME_NO(CommanUtilits.isChecking(CommanUtilits.getIMEI_NO(PMITransmitDataActivity.this)));
                requestDataVo.setPOLE_CONFIG(CommanUtilits.isChecking(surveyVo.getPole_configuration()));
                requestDataVo.setPIN_CUT(CommanUtilits.isChecking(surveyVo.getPinpoint()));
                requestDataVo.setSS_NAME(CommanUtilits.isChecking(surveyVo.getSs_name()));
                requestDataVo.setPOLE_STRUCTURE(CommanUtilits.isChecking(surveyVo.getPoleStructure()));
                requestDataVo.setDIFF_VOLTAGES(CommanUtilits.isChecking(surveyVo.getDiffVoltage()));

                if (surveyVo.getIsEndPole() != null) {
                    requestDataVo.setEND_POLE(CommanUtilits.isChecking(surveyVo.getIsEndPole()));
                } else {
                    requestDataVo.setEND_POLE("");
                }
                // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
                requestDataVo.setLINE_TYPE(CommanUtilits.isChecking(surveyVo.getLineType()));
                requestDataVo.setCKT_NO(CommanUtilits.isChecking(surveyVo.getNoofCircuits()));
                requestDataVo.setSS_ID(CommanUtilits.isChecking(surveyVo.getSsId()));
                requestDataVo.setSTJOINT_RMU(CommanUtilits.isChecking(surveyVo.getStjoint_rmu()));

                requestDataVo.setRMU_TYPE(CommanUtilits.isChecking(surveyVo.getRmu_type()));
                requestDataVo.setRMU_MAKE(CommanUtilits.isChecking(surveyVo.getRMU_MAKE()));
                requestDataVo.setRMU_SLNO(CommanUtilits.isChecking(surveyVo.getRMU_SL_NO()));


                requestDataVo.setTAPPING_POINTS(CommanUtilits.isChecking(surveyVo.getNoofTappings()));
                requestDataVo.setTAPPING_LOCNAME(CommanUtilits.isChecking(surveyVo.getTappingLocation()));
                requestDataVo.setREMARKS(CommanUtilits.isChecking(surveyVo.getRemarks()));

                //IS DTR AVAILABLE
                requestDataVo.setDTR_AVAILABLE(CommanUtilits.isChecking(surveyVo.getDTR_AVAILABLE()));
                //New fileds add
                requestDataVo.setDTR_PHASE(CommanUtilits.isChecking(surveyVo.getDtr_phase()));
                requestDataVo.setDTR_CAPACITY(CommanUtilits.isChecking(surveyVo.getDtr_capacity()));
                requestDataVo.setDTR_CODE(CommanUtilits.isChecking(surveyVo.getDtr_code()));
                requestDataVo.setDTR_STRUCTURE(CommanUtilits.isChecking(surveyVo.getDtr_structure()));
                requestDataVo.setDTR_MAKE(CommanUtilits.isChecking(surveyVo.getDtr_make()));
                requestDataVo.setDTR_AB(CommanUtilits.isChecking(surveyVo.getDtr_ab_switch()));
                requestDataVo.setDTR_LOC_NAME(CommanUtilits.isChecking(surveyVo.getDtr_location_name()));
                requestDataVo.setDTR_SNO(CommanUtilits.isChecking(surveyVo.getDtr_scno()));
                requestDataVo.setNO_LT_CKT(CommanUtilits.isChecking(surveyVo.getDtr_no_of_circuit()));
                requestDataVo.setHT_SCNO(CommanUtilits.isChecking(surveyVo.getHt_service_number()));
                requestDataVo.setCAPACITY_RATING(CommanUtilits.isChecking(surveyVo.getPc_line_capacitor()));
                requestDataVo.setRECLOSER_TYPE(CommanUtilits.isChecking(surveyVo.getPc_auto_reclosure()));

                requestDataVo.setTYPE_OF_LOAD(CommanUtilits.isChecking(surveyVo.getType_of_load()));
                requestDataVo.setDTR_LOCATION_TYPE(CommanUtilits.isChecking(surveyVo.getDtr_location_type()));
                requestDataVo.setIS_LTCKT_AVAILABLE(CommanUtilits.isChecking(surveyVo.getIs_ltckt_avalable()));
                requestDataVo.setNO_OF_AB_SWITCHES(CommanUtilits.isChecking(surveyVo.getNo_of_ab_switches()));
                requestDataVo.setWO_ID(surveyVo.getWoId());
                requestDataVo.setIS_FEEDER_TYPE(surveyVo.getIsFeederType());


                //NEW
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
                //requestDataVo.setCROSSING_TYPE(surveyVo.getCROSSING_TYPE());

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


                    //OTHER CABLES
                    requestDataVo.setNUM_OTHER_CABLES(surveyVo.getNUM_OTHER_CABLES());


                    if (surveyVo.getIsMultiCircuit().equalsIgnoreCase("Yes")) {


                        ArrayList<PMIMultiRequestVO> mc_list = new ArrayList<>();
                        if (!surveyVo.getMC_33KV_FEEDER_ID_1().equals("N")) {
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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
                            PMIMultiRequestVO vo = new PMIMultiRequestVO();
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

                //JHC PHASE 1

                if (LoginActivity.isJHC) {
                    requestDataVo.setSURVEY_TYPE(surveyVo.getLINE_SURVEY_TYPE());
                    requestDataVo.setPOLE_SURVEY_TYPE(surveyVo.getPOLE_SURVEY_TYPE());
                    requestDataVo.setDTR_PRO_EXT(surveyVo.getDTR_SURVEY_TYPE());
                    requestDataVo.setLT_SURVEY_TYPE(surveyVo.getLT_SURVEY_TYPE());
                } else {
                    requestDataVo.setSURVEY_TYPE(surveyVo.getSurvaytype());
                }

                requestDataVo.setPOLE_CONCRETE(surveyVo.getPOLE_CONCRETE());
                requestDataVo.setCURRENT_LAT(surveyVo.getCURRENT_LAT());
                requestDataVo.setCURRENT_LONG(surveyVo.getCURRENT_LONG());

                //IMAGES
               /* requestDataVo.setIMAGE_1(surveyVo.getIMAGE_1()!=null ? ConvertImage.convertBase64(surveyVo.getIMAGE_1()):"N");
                requestDataVo.setIMAGE_2(surveyVo.getIMAGE_2()!=null ? ConvertImage.convertBase64(surveyVo.getIMAGE_2()):"N");
                requestDataVo.setIMAGE_3(surveyVo.getIMAGE_3()!=null ? ConvertImage.convertBase64(surveyVo.getIMAGE_3()):"N");*/

                surveyedRequestDataVosList.add(requestDataVo);
            }
            surveyedRequestVo.setSURVEY_DATA(surveyedRequestDataVosList);

            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVo);

            Log.v("DATA===>>", "" + json);
            //  writeToFile(json);
            retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMISuveyedResponseVo> responseVoCall = retrofitService.transmitData(surveyedRequestVo);
            responseVoCall.enqueue(new Callback<PMISuveyedResponseVo>() {
                @Override
                public void onResponse(Call<PMISuveyedResponseVo> call, Response<PMISuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        Log.v("DATA PMI", "RESPONSE" + response.body());
                        PMISuveyedResponseVo response_vo = response.body();
                        progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            surveyVoList.clear();
                            arcGISDatabase.clearSurveyedData();
                            tv_escount.setText("No.of Surveyed Poles:(0)");

                            if (imageCount == 0) {
                                Toast.makeText(PMITransmitDataActivity.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PMITransmitDataActivity.this, PMIHomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                transmitImageData();
                            }

                            /*Intent intent = new Intent(TransmitDataActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();*/

                        } else {
                            Toast.makeText(PMITransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PMITransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMISuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PMITransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(TransmitDataActivity.this, FeederMasterActivity.class);
        startActivity(intent);
        finish();*/
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}