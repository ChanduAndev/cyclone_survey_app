package com.apepdcl.gis_survey.PMI.new_screens;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.PmiHome;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_11KV;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_33KV;
import com.apepdcl.gis_survey.PMI.models.Feeder33Model;
import com.apepdcl.gis_survey.PMI.models.FeederModel;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;
import com.apepdcl.gis_survey.PMI.models.PoleModelLT;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 04-08-2025.
 */
public class Select33KVFeeder extends AppCompatActivity implements RV33KVAdapter.OnItemClickListener {
    ProgressDialog progressDialog;
    RecyclerView rv_33;
    ImageView logout_layout;
    RelativeLayout profile_layout;
    private RetrofitServicePMI retrofitServicePmi;
    String selectedSSCode33, selectedFeederId33, selectedFeederName33;
    public static ArrayList<PoleModel33KV> polesList33;
    private List<Feeder33Model> feederList = new ArrayList<>();
    private Map<String, String> sectionMap = new LinkedHashMap<>();
    private Map<String, String> subStationMap = new LinkedHashMap<>();
    private Map<String, String> feederMap = new LinkedHashMap<>();
    TextView tv_section,feeder_name,ss_name,proceed_button;
    private String selectedSecId = "";
    private String selectedSubStationId = "";
    private String selectedFeederId = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_33_kv_feeder);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        rv_33 = findViewById(R.id.rv_33);
        logout_layout = findViewById(R.id.logout_layout);
        tv_section = findViewById(R.id.tv_section);
        ss_name = findViewById(R.id.ss_name);
        feeder_name = findViewById(R.id.feeder_name);
        proceed_button = findViewById(R.id.proceed_button);

        proceed_button.setOnClickListener(v -> {
            get33KVPolesData();
        });





        //profile_layout = findViewById(R.id.profile_layout);
        polesList33 = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(Select33KVFeeder.this);
        rv_33.setLayoutManager(layoutManager1);
        rv_33.setFocusable(false);
        RV33KVAdapter adapter1 = new RV33KVAdapter(PMINewHome.feederMaster33, Select33KVFeeder.this, Select33KVFeeder.this);
        rv_33.setAdapter(adapter1);
        rv_33.setNestedScrollingEnabled(false);

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getFeederMasterData();

//        profile_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

    }

    @Override
    public void onItemClick(FEEDER_MASTER_33KV model) {
        selectedSSCode33 = model.getEHT_SS_CODE();
        selectedFeederId33 = model.getFEEDER_CODE_33KV();
        selectedFeederName33 = model.getFEEDER_NAME_33KV();
        get33KVPolesData();
    }


    private void parseFeederResponse(JsonArray response) {

        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Feeder33Model>>() {}.getType();

        feederList = gson.fromJson(response, listType);

        if (feederList == null) {
            feederList = new ArrayList<>();
        }


        loadSections();
    }

    private void loadSections() {

        sectionMap.clear();

        for (Feeder33Model model : feederList) {

            if (model.getDIVISION_NAME()!= null && model.getDev_id() != null) {

                sectionMap.putIfAbsent(
                        model.getDIVISION_NAME(),
                        model.getDev_id()
                );
            }
        }

        ArrayList<String> sectionNames = new ArrayList<>(sectionMap.keySet());

        tv_section.setOnClickListener(v -> {

            SearchableSpinner searchableSpinner = new SearchableSpinner(Select33KVFeeder.this);
            searchableSpinner.setWindowTitle("Select Section");

            searchableSpinner.setSpinnerListItems(sectionNames);
            searchableSpinner.show();
            searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
                selectedSecId = sectionMap.get(selectedString);
                tv_section.setText(selectedString);
                selectedSubStationId = "";
                selectedFeederId = "";
                loadSubStations();
            });

        });
    }

    private void loadSubStations() {

        subStationMap.clear();

        for (Feeder33Model model : feederList) {

            if (selectedSecId.equals(model.getDev_id())
                    && model.getEHT_SS_NAME() != null
                    && model.getEHT_SS_CODE() != null) {

                subStationMap.putIfAbsent(
                        model.getEHT_SS_NAME(),
                        model.getEHT_SS_CODE()
                );
            }
        }

        ArrayList<String> subStationNames = new ArrayList<>(subStationMap.keySet());

        ss_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchableSpinner searchableSpinner = new SearchableSpinner(Select33KVFeeder.this);
                searchableSpinner.setWindowTitle("Select 33/11KV SS");

                searchableSpinner.setSpinnerListItems(subStationNames);
                searchableSpinner.show();

                searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
                    selectedSubStationId = subStationMap.get(selectedString);
                    ss_name.setText(selectedString);
                    selectedFeederId = "";
                    loadFeeders();
                });
            }
        });

    }

    private void loadFeeders() {

        feederMap.clear();

        for (Feeder33Model model : feederList) {

            if (selectedSecId.equals(model.getDev_id())
                    && selectedSubStationId.equals(model.getEHT_SS_CODE())
                    && model.getFEEDER_NAME_33KV() != null
                    && model.getFEEDER_CODE_33KV() != null) {

                feederMap.putIfAbsent(
                        model.getFEEDER_NAME_33KV(),
                        model.getFEEDER_CODE_33KV()
                );
            }
        }

        ArrayList<String> feederNames = new ArrayList<>(feederMap.keySet());

        feeder_name.setOnClickListener(v -> {

            SearchableSpinner searchableSpinner = new SearchableSpinner(Select33KVFeeder.this);
            searchableSpinner.setWindowTitle("Select 33KV Feeder");

            searchableSpinner.setSpinnerListItems(feederNames);
            searchableSpinner.show();

            searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
                selectedFeederId = feederMap.get(selectedString);
                selectedFeederName33=selectedString;
                feeder_name.setText(selectedString);

            });


        });
    }

    private void getFeederMasterData() {
        polesList33.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("DTR_STRUCTURE", "123");
        requestObject.addProperty("LT_CKTCODE", "321");
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getArnabFeederMasterData(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();




                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");


                            //JsonArray dtr_array = object.getAsJsonArray("DTR");

                            JsonArray EHT_SS_MASTER = object.getAsJsonArray("33KV_FEEDER_MASTER");

                            parseFeederResponse(EHT_SS_MASTER);

//

                        } else {
                            progressDialog.cancel();
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(Select33KVFeeder.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(Select33KVFeeder.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Select33KVFeeder.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Select33KVFeeder.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void get33KVPolesData() {
        polesList33.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId);
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPoles33(requestObject);


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
                                model.setPOLE_STRUCTURE(array33.get(i).getAsJsonObject().get("POLE_STRUCTURE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_STRUCTURE").getAsString());
                                polesList33.add(model);
                            }
                            progressDialog.dismiss();
                            Intent intent = new Intent(Select33KVFeeder.this, PmiMap.class);
                            intent.putExtra("ID", selectedFeederId);
                            intent.putExtra("NAME", selectedFeederName33);
                            intent.putExtra("SELECTED_VOLTAGE", "33KV");
                            intent.putExtra("FROM", "SURVEY");

                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Select33KVFeeder.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Select33KVFeeder.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                CommanUtilits.netWorkError(Select33KVFeeder.this);
                //Toast.makeText(Select33KVFeeder.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
