package com.apepdcl.gis_survey.PMI.rectification;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_11KV;
import com.apepdcl.gis_survey.PMI.models.OfflineDataFor11;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.PMI.new_screens.RV11KVAdapter;
import com.apepdcl.gis_survey.PMI.rectification.offline.OfflineRectifySurvey;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 23-03-2026.
 */
public class Select11KVFeederRectify extends AppCompatActivity implements RV11KVAdapterRectify.OnItemClickListener, PoleFeederAdapter.OnItemClickListener {

    RecyclerView rv_11;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;
    String selectedSSCode11, selectedFeederId11, selectedFeederName11;
    public static ArrayList<PoleModel11KV> polesList11;
    public static ArrayList<PoleModel11KV> aiPoleList11;
    public static ArrayList<PoleModel11KV> nonAiPoleList11;
    private static ArrayList<PoleNosModel11> poleNoList11;
    private RetrofitServicePMI retrofitServicePmi;
    ProgressDialog progressDialog;
    private ArrayList<OfflineDataFor11> offlineDataFor11s;
    PmiDatabase pmiDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_11_kv_feeder_rectify);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        rv_11 = findViewById(R.id.rv_11);
        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);
        polesList11 = new ArrayList<>();
        aiPoleList11 = new ArrayList<>();
        nonAiPoleList11 = new ArrayList<>();
        poleNoList11 = new ArrayList<>();
        offlineDataFor11s = new ArrayList<>();
        pmiDatabase = new PmiDatabase(Select11KVFeederRectify.this);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(Select11KVFeederRectify.this);
        rv_11.setLayoutManager(layoutManager1);
        rv_11.setFocusable(false);
        RV11KVAdapterRectify adapter1 = new RV11KVAdapterRectify(PMINewHome.feederMaster11, Select11KVFeederRectify.this, Select11KVFeederRectify.this);
        rv_11.setAdapter(adapter1);
        rv_11.setNestedScrollingEnabled(false);


        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClick(FEEDER_MASTER_11KV model) {
        selectedSSCode11 = model.getSUB_STATION_ID();
        selectedFeederId11 = model.getFEEDER_ID();
        selectedFeederName11 = model.getFEEDER_NAME();
        if (ConnectivityChecking.checkConnection(Select11KVFeederRectify.this)) {
            getPoleCount();
            //getPoleCountOffline();
        } else {
            ArrayList<OfflineDataFor11> data = pmiDatabase.getTotalOfflineDataToSurvey(model.getFEEDER_ID());
            if (data.isEmpty()) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();

            }else {
                Intent intent = new Intent(Select11KVFeederRectify.this, OfflineRectifySurvey.class);
                intent.putExtra("SELECTED_FEEDER_ID", selectedFeederId11);
                intent.putExtra("SELECTED_VOLTAGE_LEVEL", "11 KV");
                startActivity(intent);
            }
        }

        //get11KVPolesData();

    }

    private void get11KVPolesData(PoleNosModel11 model) {
        polesList11.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("POLE_ID", model.getPOLE_ID());
        requestObject.addProperty("VOLTAGE_LEVEL", "11KV");
//        requestObject.addProperty("FEEDER_ID", selectedFeederId11);
//        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPoles11RectifyNew(requestObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();

                    if (MobileConstants.TRUE.equalsIgnoreCase(
                            surveyedResponseVo.get("STATUS").getAsString())) {

                        try {

                            // RESPONSE is now directly a JSON Array
                            JsonArray responseArray =
                                    surveyedResponseVo.getAsJsonArray("RESPONSE");

                            if (responseArray != null) {

                                for (int i = 0; i < responseArray.size(); i++) {

                                    JsonObject poleObject =
                                            responseArray.get(i).getAsJsonObject();

                                    PoleModel11KV model = new PoleModel11KV();

                                    model.setFEEDER_ID(getJsonValue(poleObject, "FEEDER_ID"));
                                    model.setGLAT(getJsonValue(poleObject, "GLAT"));
                                    model.setPOLE_ID(getJsonValue(poleObject, "REF_ID"));
                                    model.setPOLE_NO(getJsonValue(poleObject, "POLE_NO"));
                                    model.setGLONG(getJsonValue(poleObject, "GLONG"));
                                    model.setPOLE_CONFIG(getJsonValue(poleObject, "POLE_CONFIG"));
                                    model.setPOLE_HEIGHT(getJsonValue(poleObject, "POLE_HEIGHT"));
                                    model.setPOLE_SUPPORT(getJsonValue(poleObject, "POLE_SUPPORT"));
                                    model.setPOLE_TYPE(getJsonValue(poleObject, "POLE_TYPE"));
                                    model.setDEFECT_TYPE(getJsonValue(poleObject, "DEFECT_TYPE"));
                                    model.setGRP_ID(getJsonValue(poleObject, "GRP_ID"));
                                    model.setAI_FLAG(getJsonValue(poleObject, "AI_FLAG"));
                                    model.setDEFECT_SUB_TYPE(getJsonValue(poleObject, "DEFECT_SUB_TYPE"));

                                    model.setIS_RECTIFIED(
                                            getJsonValue(poleObject, "RECTIFIED", "N"));

                                    model.setIMG_URL(getJsonValue(poleObject, "IMG_URL"));

                                    model.setAI_PENDING("N");

                                    // Add all records to one list
                                    nonAiPoleList11.add(model);
                                }
                            }

                            progressDialog.dismiss();

                            Intent intent = new Intent(
                                    Select11KVFeederRectify.this,
                                    Select11PoleRectify.class
                            );

                            intent.putExtra("FEEDER_ID", selectedFeederId11);
                            intent.putExtra("FEEDER_NAME", selectedFeederName11);
                            intent.putExtra("SS_CODE", selectedSSCode11);

                            startActivity(intent);
                            finish();

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    } else {

                        progressDialog.dismiss();

                        Toast.makeText(
                                Select11KVFeederRectify.this,
                                surveyedResponseVo.has("ERR_MSG")
                                        ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                        : "No pole data found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {

                    progressDialog.dismiss();

                    Toast.makeText(
                            Select11KVFeederRectify.this,
                            "Server Busy Try Again",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressDialog.dismiss();

                CommanUtilits.netWorkError(Select11KVFeederRectify.this);
            }
        });


//        responseVoCall.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                if (response.body() != null) {
//
//                    JsonObject surveyedResponseVo = response.body();
//                    // Log.v("FEEDER==", "" + surveyedResponseVo);
//                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
//                        try {
//                            JsonObject responseObject = surveyedResponseVo.getAsJsonObject("RESPONSE");
//                            if (responseObject.has("AI")
//                                    && !responseObject.get("AI").isJsonNull()
//                                    && responseObject.get("AI").isJsonArray()) {
//
//                                JsonArray aiArray = responseObject.getAsJsonArray("AI");
//
//
//                                for (int i = 0; i < aiArray.size(); i++) {
//                                    PoleModel11KV model = new PoleModel11KV();
//                                    model.setFEEDER_ID(aiArray.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
//                                    model.setGLAT(aiArray.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("GLAT").getAsString());
//                                    model.setPOLE_ID(aiArray.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("REF_ID").getAsString());
//                                    model.setPOLE_NO(aiArray.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_NO").getAsString());
//                                    model.setGLONG(aiArray.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("GLONG").getAsString());
//                                    model.setPOLE_CONFIG(aiArray.get(i).getAsJsonObject().get("POLE_CONFIG").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_CONFIG").getAsString());
//                                    model.setPOLE_HEIGHT(aiArray.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
//                                    model.setPOLE_SUPPORT(aiArray.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
//                                    model.setPOLE_TYPE(aiArray.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
//                                    model.setDEFECT_TYPE(aiArray.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
//                                    //model.setDEFECT_GROUP(aiArray.get(i).getAsJsonObject().get("DEFECT_GROUP").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("DEFECT_GROUP").getAsString());
//                                    model.setGRP_ID(aiArray.get(i).getAsJsonObject().get("GRP_ID").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("GRP_ID").getAsString());
//                                    model.setAI_FLAG(aiArray.get(i).getAsJsonObject().get("AI_FLAG").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("AI_FLAG").getAsString());
//                                    model.setDEFECT_SUB_TYPE(aiArray.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").getAsString());
//                                    model.setIS_RECTIFIED(aiArray.get(i).getAsJsonObject().get("RECTIFIED").isJsonNull() ? "N" : aiArray.get(i).getAsJsonObject().get("RECTIFIED").getAsString());
//                                    model.setIMG_URL(aiArray.get(i).getAsJsonObject().get("IMG_URL").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("IMG_URL").getAsString());
//                                    model.setAI_PENDING("N");
//                                    aiPoleList11.add(model);
//                                }
//
//
//                            }
//
//                            // Process NON AI array
//                            if (responseObject.has("NON AI")
//                                    && !responseObject.get("NON AI").isJsonNull()
//                                    && responseObject.get("NON AI").isJsonArray()) {
//
//                                JsonArray nonAiArray = responseObject.getAsJsonArray("NON AI");
//
//                                for (int i = 0; i < nonAiArray.size(); i++) {
//                                    PoleModel11KV model = new PoleModel11KV();
//                                    model.setFEEDER_ID(nonAiArray.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
//                                    model.setGLAT(nonAiArray.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("GLAT").getAsString());
//                                    model.setPOLE_ID(nonAiArray.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("REF_ID").getAsString());
//                                    model.setPOLE_NO(nonAiArray.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_NO").getAsString());
//                                    model.setGLONG(nonAiArray.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("GLONG").getAsString());
//                                    model.setPOLE_CONFIG(nonAiArray.get(i).getAsJsonObject().get("POLE_CONFIG").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_CONFIG").getAsString());
//                                    model.setPOLE_HEIGHT(nonAiArray.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
//                                    model.setPOLE_SUPPORT(nonAiArray.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
//                                    model.setPOLE_TYPE(nonAiArray.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
//                                    model.setDEFECT_TYPE(nonAiArray.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
//                                    //model.setDEFECT_GROUP(nonAiArray.get(i).getAsJsonObject().get("DEFECT_GROUP").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("DEFECT_GROUP").getAsString());
//                                    model.setGRP_ID(nonAiArray.get(i).getAsJsonObject().get("GRP_ID").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("GRP_ID").getAsString());
//                                    model.setAI_FLAG(nonAiArray.get(i).getAsJsonObject().get("AI_FLAG").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("AI_FLAG").getAsString());
//                                    model.setDEFECT_SUB_TYPE(nonAiArray.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").getAsString());
//                                    model.setIS_RECTIFIED(nonAiArray.get(i).getAsJsonObject().get("RECTIFIED").isJsonNull() ? "N" : nonAiArray.get(i).getAsJsonObject().get("RECTIFIED").getAsString());
//                                    model.setIMG_URL(nonAiArray.get(i).getAsJsonObject().get("IMG_URL").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("IMG_URL").getAsString());
//                                    model.setAI_PENDING("N");
//                                    nonAiPoleList11.add(model);
//                                }
//                            }
//
//
//                            progressDialog.dismiss();
//                           // showSelectSurveyTypeDialog();
//                            Intent intent = new Intent(Select11KVFeederRectify.this, Select11PoleRectify.class);
//                            intent.putExtra("FEEDER_ID",  selectedFeederId11);
//                            intent.putExtra("FEEDER_NAME", selectedFeederName11);
//                            intent.putExtra("SS_CODE", selectedSSCode11);
//                            intent.putExtra("POLE_ID", model.getPOLE_ID());
//                            startActivity(intent);
//                            finish();
//
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//
//                        progressDialog.dismiss();
//                        Toast.makeText(Select11KVFeederRectify.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } else {
//                    progressDialog.dismiss();
//                    Toast.makeText(Select11KVFeederRectify.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                progressDialog.dismiss();
//                CommanUtilits.netWorkError(Select11KVFeederRectify.this);
//                //Toast.makeText(Select11KVFeederRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

    }


    private String getJsonValue(JsonObject object, String key) {
        return getJsonValue(object, key, "");
    }

    private String getJsonValue(JsonObject object, String key, String defaultValue) {

        if (object == null || !object.has(key) || object.get(key).isJsonNull()) {
            return defaultValue;
        }

        return object.get(key).getAsString();
    }


    private void getPoleCount() {
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId11);
        //requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getFeederWisePoleNo(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            poleNoList11.clear();
                            for (int i = 0; i < array.size(); i++) {
                                PoleNosModel11 model = new PoleNosModel11();
                                model.setFEEDER_ID(array.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setPOLE_ID(array.get(i).getAsJsonObject().get("POLE_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_ID").getAsString());
                                model.setPOLE_NO(array.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                poleNoList11.add(model);
                            }


                            progressDialog.dismiss();
                            showPoleFeederBottomSheet(poleNoList11);
                            //openAlertDialog("Do you want to survey offline Or Online", poleNoList11);

                            // showSelectSurveyTypeDialog();
//                            Intent intent = new Intent(Select11KVFeederRectify.this, Select11PoleRectify.class);
//                            intent.putExtra("FEEDER_ID",  selectedFeederId11);
//                            intent.putExtra("FEEDER_NAME", selectedFeederName11);
//                            intent.putExtra("SS_CODE", selectedSSCode11);
//                            intent.putExtra("SS_CODE", selectedSSCode11);
//                            startActivity(intent);
//                            finish();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(Select11KVFeederRectify.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Select11KVFeederRectify.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                CommanUtilits.netWorkError(Select11KVFeederRectify.this);
                //Toast.makeText(Select11KVFeederRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void getPoleCountOffline() {

        progressDialog.show();

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId11);
        requestObject.addProperty("VOLTAGE_LEVEL", "11KV");

        retrofitServicePmi = RetrofitClientInstancePMI
                .getRetrofitInstance()
                .create(RetrofitServicePMI.class);

        Call<JsonObject> responseVoCall =
                retrofitServicePmi.getDataForOffline(requestObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(
                    Call<JsonObject> call,
                    Response<JsonObject> response) {

                progressDialog.dismiss();

                if (response.body() == null) {

                    Toast.makeText(
                            Select11KVFeederRectify.this,
                            "Server Busy Try Again",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                JsonObject surveyedResponseVo = response.body();

                try {

                    // =====================================================
                    // STATUS
                    // =====================================================

                    boolean status =
                            surveyedResponseVo.has("STATUS")
                                    && !surveyedResponseVo.get("STATUS").isJsonNull()
                                    && surveyedResponseVo.get("STATUS").getAsBoolean();

                    if (!status) {

                        String errorMsg =
                                getJsonString(
                                        surveyedResponseVo,
                                        "ERR_MSG",
                                        "No Data Found"
                                );

                        Toast.makeText(
                                Select11KVFeederRectify.this,
                                errorMsg,
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }


                    // =====================================================
                    // RESPONSE
                    // =====================================================

                    if (!surveyedResponseVo.has("RESPONSE")
                            || surveyedResponseVo.get("RESPONSE").isJsonNull()
                            || !surveyedResponseVo.get("RESPONSE").isJsonObject()) {

                        Toast.makeText(
                                Select11KVFeederRectify.this,
                                "No Pole Data Found",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    JsonObject responseObject =
                            surveyedResponseVo.getAsJsonObject("RESPONSE");


                    boolean dataExist =
                            pmiDatabase.isRectificationDataExists(selectedFeederId11);

                    if (dataExist) {
                        boolean deleted =
                                pmiDatabase.deleteRectificationData(selectedFeederId11);
                    }


                    // =====================================================
                    // LOOP POLES
                    // =====================================================

                    for (Map.Entry<String, JsonElement> entry
                            : responseObject.entrySet()) {

                        String poleNo = entry.getKey();

                        JsonElement poleElement = entry.getValue();

                        if (poleElement == null
                                || poleElement.isJsonNull()
                                || !poleElement.isJsonObject()) {

                            continue;
                        }

                        JsonObject poleObject =
                                poleElement.getAsJsonObject();


                        // =================================================
                        // AI DATA
                        // =================================================

                        processOfflineRectificationArray(
                                poleObject,
                                "AI",
                                poleNo,
                                "AI"
                        );


                        // =================================================
                        // NON AI DATA
                        // =================================================

                        processOfflineRectificationArray(
                                poleObject,
                                "NON AI",
                                poleNo,
                                "NON AI"
                        );
                    }


                } catch (Exception e) {

                    Log.e(
                            "PMI_OFFLINE",
                            "Error processing offline pole data",
                            e
                    );

                    Toast.makeText(
                            Select11KVFeederRectify.this,
                            "Unable to process pole data",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }


            @Override
            public void onFailure(
                    Call<JsonObject> call,
                    Throwable t) {

                progressDialog.dismiss();

                Log.e(
                        "PMI_OFFLINE",
                        "getDataForOffline API failed",
                        t
                );

                CommanUtilits.netWorkError(
                        Select11KVFeederRectify.this
                );
            }
        });
    }
    private void processOfflineRectificationArray(
            JsonObject poleObject,
            String jsonKey,
            String poleNo,
            String defaultAiFlag) {

        // =============================================================
        // Check whether key exists
        // =============================================================

        if (!poleObject.has(jsonKey)
                || poleObject.get(jsonKey).isJsonNull()
                || !poleObject.get(jsonKey).isJsonArray()) {

            return;
        }

        JsonArray dataArray =
                poleObject.getAsJsonArray(jsonKey);


        // =============================================================
        // Process every defect
        // =============================================================

        for (JsonElement element : dataArray) {

            if (element == null
                    || element.isJsonNull()
                    || !element.isJsonObject()) {

                continue;
            }

            JsonObject obj =
                    element.getAsJsonObject();


            // =========================================================
            // IMPORTANT:
            // Create a NEW model for every API record
            // =========================================================

            OfflineDataFor11 model =
                    createOfflineModel(
                            obj,
                            poleNo,
                            defaultAiFlag
                    );


            // =========================================================
            // INSERT INTO SQLITE
            // =========================================================

            boolean inserted =
                    pmiDatabase.insertOfflineRectification(model);
            // =========================================================
            // LOG
            // =========================================================

            Log.d(
                    "PMI_OFFLINE",
                    "INSERT RESULT = " + inserted
                            + " | TYPE = " + jsonKey
                            + " | POLE = " + model.getPOLE_NO()
                            + " | POLE_ID = " + model.getPOLE_ID()
                            + " | GRP_ID = " + model.getGRP_ID()
                            + " | AI_FLAG = " + model.getAI_FLAG()
                            + " | DEFECT_TYPE = " + model.getDEFECT_TYPE()
                            + " | DEFECT_SUB_TYPE = " + model.getDEFECT_SUB_TYPE()
            );
        }
    }

    private OfflineDataFor11 createOfflineModel(
            JsonObject obj,
            String poleNo,
            String defaultAiFlag) {

        OfflineDataFor11 model =
                new OfflineDataFor11();


        // =============================================================
        // AI FLAG
        // =============================================================

        String aiFlag =
                getJsonString(
                        obj,
                        "AI_FLAG",
                        defaultAiFlag
                );

        if (aiFlag == null || aiFlag.trim().isEmpty()) {
            aiFlag = defaultAiFlag;
        }

        model.setAI_FLAG(
                aiFlag.trim()
        );


        // =============================================================
        // POLE NUMBER
        // =============================================================

        String apiPoleNo =
                getJsonString(
                        obj,
                        "POLE_NO",
                        poleNo
                );

        if (apiPoleNo == null || apiPoleNo.trim().isEmpty()) {
            apiPoleNo = poleNo;
        }

        model.setPOLE_NO(
                apiPoleNo.trim()
        );


        // =============================================================
        // SURVEY STATUS
        // =============================================================

        model.setCOMPLETE_SURVEY("N");


        // =============================================================
        // DEFECT INFORMATION
        // =============================================================

        model.setDEFECT_TYPE(
                getJsonString(
                        obj,
                        "DEFECT_TYPE",
                        ""
                ).trim()
        );

        model.setDEFECT_SUB_TYPE(
                getJsonString(
                        obj,
                        "DEFECT_SUB_TYPE",
                        ""
                ).trim()
        );


        // =============================================================
        // POLE INFORMATION
        // =============================================================

        model.setPOLE_SUPPORT(
                getJsonString(
                        obj,
                        "POLE_SUPPORT",
                        ""
                ).trim()
        );

        model.setSHORT_PATH(
                getJsonString(
                        obj,
                        "SHORT_PATH",
                        ""
                ).trim()
        );

        model.setFEEDER_ID(
                getJsonString(
                        obj,
                        "FEEDER_ID",
                        selectedFeederId11
                ).trim()
        );

        model.setIMG_URL(
                getJsonString(
                        obj,
                        "IMG_URL",
                        ""
                ).trim()
        );

        model.setGRP_ID(
                getJsonString(
                        obj,
                        "GRP_ID",
                        ""
                ).trim()
        );

        model.setRECTIFIED(
                getJsonString(
                        obj,
                        "RECTIFIED",
                        ""
                ).trim()
        );

        model.setPOLE_CONFIG(
                getJsonString(
                        obj,
                        "POLE_CONFIG",
                        ""
                ).trim()
        );

        model.setPOLE_HEIGHT(
                getJsonString(
                        obj,
                        "POLE_HEIGHT",
                        ""
                ).trim()
        );

        model.setPOLE_TYPE(
                getJsonString(
                        obj,
                        "POLE_TYPE",
                        ""
                ).trim()
        );


        // =============================================================
        // POLE ID
        // =============================================================

        model.setPOLE_ID(
                getJsonString(
                        obj,
                        "REF_ID",
                        ""
                ).trim()
        );


        // =============================================================
        // LOCATION
        // =============================================================

        model.setGLAT(
                getJsonString(
                        obj,
                        "GLAT",
                        ""
                ).trim()
        );

        model.setGLONG(
                getJsonString(
                        obj,
                        "GLONG",
                        ""
                ).trim()
        );


        return model;
    }

    private String getJsonString(
            JsonObject object,
            String key,
            String defaultValue) {

        try {

            if (object != null
                    && object.has(key)
                    && !object.get(key).isJsonNull()) {

                return object.get(key).getAsString();
            }

        } catch (Exception e) {

            Log.e(
                    "PMI_JSON",
                    "Error reading key: " + key,
                    e
            );
        }

        return defaultValue;
    }


    private void openAlertDialog(String message, List<PoleNosModel11> poleFeederList) {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        LinearLayout estimate_details = view.findViewById(R.id.estimate_details);
        estimate_details.setVisibility(View.GONE);
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        // Set Data
        title.setText("Pay Attension!!");
        msg.setText(message);

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
        okBtn.setText("Online");
        cancelBtn.setText("Offline");

        // Click action
        okBtn.setOnClickListener(v -> {
            showPoleFeederBottomSheet(poleFeederList);
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Select11KVFeederRectify.this, OfflineRectifySurvey.class);
            intent.putExtra("SELECTED_FEEDER_ID", selectedFeederId11);
            intent.putExtra("SELECTED_VOLTAGE_LEVEL", "11 KV");
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

    private void showPoleFeederBottomSheet(
            List<PoleNosModel11> poleFeederList) {

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(
                R.layout.bottomsheet_pole_feeder,
                null
        );

        bottomSheetDialog.setContentView(view);

        RecyclerView rvPoleFeeder =
                view.findViewById(R.id.rvPoleFeeder);

        rvPoleFeeder.setLayoutManager(
                new LinearLayoutManager(this)
        );

        PoleFeederAdapter adapter = new PoleFeederAdapter(this, poleFeederList,Select11KVFeederRectify.this);

        rvPoleFeeder.setAdapter(adapter);

        bottomSheetDialog.show();
    }


    @Override
    public void onItemClick(PoleNosModel11 model) {
        get11KVPolesData(model);
    }
}
