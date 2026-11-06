package com.apepdcl.gis_survey.PMI.rectification;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_33KV;
import com.apepdcl.gis_survey.PMI.models.OfflineDataFor11;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.PMI.rectification.offline.OfflineRectifySurvey;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
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
public class Select33KVFeederRectify extends AppCompatActivity implements RV33KVAdapterRectify.OnItemClickListener,PoleFeederAdapter33.OnItemClickListener {

    ProgressDialog progressDialog;
    RecyclerView rv_33;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;
    private RetrofitServicePMI retrofitServicePmi;
    String selectedSSCode33, selectedFeederId33, selectedFeederName33;
    public static ArrayList<PoleModel33KV> polesList33;
    public static ArrayList<PoleModel33KV> aiPoleList33;
    public static ArrayList<PoleModel33KV> nonAiPoleList33;

    private static ArrayList<PoleNosModel33> poleNoList33;

    private BottomSheetDialog poleFeederBottomSheet;
    PmiDatabase pmiDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_33_kv_feeder_rectify);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        pmiDatabase=new PmiDatabase(Select33KVFeederRectify.this);

        rv_33 = findViewById(R.id.rv_33);
        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);
        polesList33 = new ArrayList<>();
        aiPoleList33 = new ArrayList<>();
        nonAiPoleList33 = new ArrayList<>();
        poleNoList33 = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(Select33KVFeederRectify.this);
        rv_33.setLayoutManager(layoutManager1);
        rv_33.setFocusable(false);
        RV33KVAdapterRectify adapter1 = new RV33KVAdapterRectify(PMINewHome.feederMaster33, Select33KVFeederRectify.this, Select33KVFeederRectify.this);
        rv_33.setAdapter(adapter1);
        rv_33.setNestedScrollingEnabled(false);

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
    public void onItemClick(FEEDER_MASTER_33KV model) {
        selectedSSCode33 = model.getEHT_SS_CODE();
        selectedFeederId33 = model.getFEEDER_CODE_33KV();
        selectedFeederName33 = model.getFEEDER_NAME_33KV();
        if (ConnectivityChecking.checkConnection(Select33KVFeederRectify.this)){
            //get33KVPolesData();
            getPoleCount();
            getPoleCountOffline();
        }else {
            ArrayList<OfflineDataFor11> data = pmiDatabase.getTotalOfflineDataToSurvey(model.getFEEDER_CODE_33KV());
            if (data.isEmpty()) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();

            }else {
                Intent intent = new Intent(Select33KVFeederRectify.this, OfflineRectifySurvey.class);
                intent.putExtra("SELECTED_FEEDER_ID", selectedFeederId33);
                intent.putExtra("SELECTED_VOLTAGE_LEVEL", "33KV");
                startActivity(intent);
            }
        }

    }

    private void getPoleCountOffline() {

        progressDialog.show();

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId33);
        requestObject.addProperty("VOLTAGE_LEVEL", "33KV");

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
                            Select33KVFeederRectify.this,
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
                                Select33KVFeederRectify.this,
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
                                Select33KVFeederRectify.this,
                                "No Pole Data Found",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    JsonObject responseObject =
                            surveyedResponseVo.getAsJsonObject("RESPONSE");


                    boolean dataExist =
                            pmiDatabase.isRectificationDataExists(selectedFeederId33);

                    if (dataExist) {
                        boolean deleted =
                                pmiDatabase.deleteRectificationData(selectedFeederId33);
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
                            Select33KVFeederRectify.this,
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
                        Select33KVFeederRectify.this
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
                        selectedFeederId33
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
    private void get33KVPolesData() {
        polesList33.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId33);
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPoles33Rectify(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {


                            JsonArray array33 = surveyedResponseVo.getAsJsonArray("RESPONSE");
                            for (int i = 0; i < array33.size(); i++) {
                                PoleModel33KV model = new PoleModel33KV();
                                model.setFEEDER_ID(array33.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setGLAT(array33.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("GLAT").getAsString());
                                model.setPOLE_ID(array33.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("REF_ID").getAsString());
                                model.setPOLE_NO(array33.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                model.setGLONG(array33.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("GLONG").getAsString());
                                model.setPOLE_CONFIG(array33.get(i).getAsJsonObject().get("POLE_CONFIG").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_CONFIG").getAsString());
                                model.setPOLE_HEIGHT(array33.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
                                model.setPOLE_SUPPORT(array33.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
                                model.setPOLE_TYPE(array33.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
                                model.setDEFECT_TYPE(array33.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
                                model.setDEFECT_GROUP(array33.get(i).getAsJsonObject().get("DEFECT_GROUP").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_GROUP").getAsString());
                                model.setIS_RECTIFIED(array33.get(i).getAsJsonObject().get("RECTIFIED").isJsonNull() ? "N" : array33.get(i).getAsJsonObject().get("RECTIFIED").getAsString());
                                model.setAI_PENDING("N");
                                polesList33.add(model);
                            }
                            progressDialog.dismiss();
                            //showSelectSurveyTypeDialog();
                            Intent intent = new Intent(Select33KVFeederRectify.this, Select33PoleRectify.class);
                            intent.putExtra("FEEDER_ID",  selectedFeederId33);
                            intent.putExtra("FEEDER_NAME", selectedFeederName33);
                            intent.putExtra("SS_CODE", selectedSSCode33);
                            startActivity(intent);
                            finish();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Select33KVFeederRectify.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Select33KVFeederRectify.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                CommanUtilits.netWorkError(Select33KVFeederRectify.this);
                //Toast.makeText(Select33KVFeeder.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    private void get33KVPolesData(PoleNosModel33 model) {
        polesList33.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("POLE_ID", model.getPOLE_ID());
//        requestObject.addProperty("FEEDER_ID", selectedFeederId11);
//        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPoles33RectifyNew(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonObject responseObject = surveyedResponseVo.getAsJsonObject("RESPONSE");
                            if (responseObject.has("AI")
                                    && !responseObject.get("AI").isJsonNull()
                                    && responseObject.get("AI").isJsonArray()) {

                                JsonArray aiArray = responseObject.getAsJsonArray("AI");


                                for (int i = 0; i < aiArray.size(); i++) {
                                    PoleModel33KV model = new PoleModel33KV();
                                    model.setFEEDER_ID(aiArray.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                    model.setGLAT(aiArray.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("GLAT").getAsString());
                                    model.setPOLE_ID(aiArray.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("REF_ID").getAsString());
                                    model.setPOLE_NO(aiArray.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                    model.setGLONG(aiArray.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("GLONG").getAsString());
                                    model.setPOLE_CONFIG(aiArray.get(i).getAsJsonObject().get("POLE_CONFIG").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_CONFIG").getAsString());
                                    model.setPOLE_HEIGHT(aiArray.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
                                    model.setPOLE_SUPPORT(aiArray.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
                                    model.setPOLE_TYPE(aiArray.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
                                    model.setDEFECT_TYPE(aiArray.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
                                    //model.setDEFECT_GROUP(aiArray.get(i).getAsJsonObject().get("DEFECT_GROUP").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("DEFECT_GROUP").getAsString());
                                    model.setGRP_ID(aiArray.get(i).getAsJsonObject().get("GRP_ID").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("GRP_ID").getAsString());
                                    model.setAI_FLAG(aiArray.get(i).getAsJsonObject().get("AI_FLAG").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("AI_FLAG").getAsString());
                                    model.setDEFECT_SUB_TYPE(aiArray.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").getAsString());
                                    model.setIS_RECTIFIED(aiArray.get(i).getAsJsonObject().get("RECTIFIED").isJsonNull() ? "N" : aiArray.get(i).getAsJsonObject().get("RECTIFIED").getAsString());
                                    model.setIMG_URL(aiArray.get(i).getAsJsonObject().get("IMG_URL").isJsonNull() ? "" : aiArray.get(i).getAsJsonObject().get("IMG_URL").getAsString());
                                    model.setAI_PENDING("N");
                                    aiPoleList33.add(model);
                                }


                            }

                            // Process NON AI array
                            if (responseObject.has("NON AI")
                                    && !responseObject.get("NON AI").isJsonNull()
                                    && responseObject.get("NON AI").isJsonArray()) {

                                JsonArray nonAiArray = responseObject.getAsJsonArray("NON AI");

                                for (int i = 0; i < nonAiArray.size(); i++) {
                                    PoleModel33KV model = new PoleModel33KV();
                                    model.setFEEDER_ID(nonAiArray.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                    model.setGLAT(nonAiArray.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("GLAT").getAsString());
                                    model.setPOLE_ID(nonAiArray.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("REF_ID").getAsString());
                                    model.setPOLE_NO(nonAiArray.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                    model.setGLONG(nonAiArray.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("GLONG").getAsString());
                                    model.setPOLE_CONFIG(nonAiArray.get(i).getAsJsonObject().get("POLE_CONFIG").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_CONFIG").getAsString());
                                    model.setPOLE_HEIGHT(nonAiArray.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
                                    model.setPOLE_SUPPORT(nonAiArray.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
                                    model.setPOLE_TYPE(nonAiArray.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
                                    model.setDEFECT_TYPE(nonAiArray.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
                                    //model.setDEFECT_GROUP(nonAiArray.get(i).getAsJsonObject().get("DEFECT_GROUP").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("DEFECT_GROUP").getAsString());
                                    model.setGRP_ID(nonAiArray.get(i).getAsJsonObject().get("GRP_ID").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("GRP_ID").getAsString());
                                    model.setAI_FLAG(nonAiArray.get(i).getAsJsonObject().get("AI_FLAG").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("AI_FLAG").getAsString());
                                    model.setDEFECT_SUB_TYPE(nonAiArray.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").getAsString());
                                    model.setIS_RECTIFIED(nonAiArray.get(i).getAsJsonObject().get("RECTIFIED").isJsonNull() ? "N" : nonAiArray.get(i).getAsJsonObject().get("RECTIFIED").getAsString());
                                    model.setIMG_URL(nonAiArray.get(i).getAsJsonObject().get("IMG_URL").isJsonNull() ? "" : nonAiArray.get(i).getAsJsonObject().get("IMG_URL").getAsString());
                                    model.setAI_PENDING("N");
                                    nonAiPoleList33.add(model);
                                }
                            }


                            progressDialog.dismiss();
                            // showSelectSurveyTypeDialog();
                            Intent intent = new Intent(Select33KVFeederRectify.this, Select33PoleRectify.class);
                            intent.putExtra("FEEDER_ID",  selectedFeederId33);
                            intent.putExtra("FEEDER_NAME", selectedFeederName33);
                            intent.putExtra("SS_CODE", selectedSSCode33);
                            intent.putExtra("POLE_ID", model.getPOLE_ID());
                            startActivity(intent);
                            finish();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(Select33KVFeederRectify.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Select33KVFeederRectify.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                CommanUtilits.netWorkError(Select33KVFeederRectify.this);
                //Toast.makeText(Select11KVFeederRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getPoleCount() {
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId33);
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
                            poleNoList33.clear();
                            for (int i = 0; i < array.size(); i++) {
                                PoleNosModel33 model = new PoleNosModel33();
                                model.setFEEDER_ID(array.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setPOLE_ID(array.get(i).getAsJsonObject().get("POLE_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_ID").getAsString());
                                model.setPOLE_NO(array.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                poleNoList33.add(model);
                            }


                            progressDialog.dismiss();
                            showPoleFeederBottomSheet(poleNoList33);
                            openAlertDialog("Do you want to survey offline Or Online", poleNoList33);
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
                        Toast.makeText(Select33KVFeederRectify.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Select33KVFeederRectify.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                CommanUtilits.netWorkError(Select33KVFeederRectify.this);
                //Toast.makeText(Select11KVFeederRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    private void openAlertDialog(String message, List<PoleNosModel33> poleFeederList) {
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
            Intent intent = new Intent(Select33KVFeederRectify.this, OfflineRectifySurvey.class);
            intent.putExtra("SELECTED_FEEDER_ID", selectedFeederId33);
            intent.putExtra("SELECTED_VOLTAGE_LEVEL", "33KV");
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
            List<PoleNosModel33> poleFeederList) {

        // Dismiss existing dialog if already showing
        if (poleFeederBottomSheet != null
                && poleFeederBottomSheet.isShowing()) {
            poleFeederBottomSheet.dismiss();
        }

        poleFeederBottomSheet = new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(
                R.layout.bottomsheet_pole_feeder,
                null
        );

        poleFeederBottomSheet.setContentView(view);

        RecyclerView rvPoleFeeder =
                view.findViewById(R.id.rvPoleFeeder);

        rvPoleFeeder.setLayoutManager(
                new LinearLayoutManager(this)
        );

        PoleFeederAdapter33 adapter = new PoleFeederAdapter33(this, poleFeederList,Select33KVFeederRectify.this);

        rvPoleFeeder.setAdapter(adapter);

        poleFeederBottomSheet.show();
    }

    @Override
    protected void onDestroy() {

        if (poleFeederBottomSheet != null
                && poleFeederBottomSheet.isShowing()) {
            poleFeederBottomSheet.dismiss();
        }

        poleFeederBottomSheet = null;

        super.onDestroy();
    }


    @Override
    public void onItemClick(PoleNosModel33 model) {
        get33KVPolesData(model);

    }
}
