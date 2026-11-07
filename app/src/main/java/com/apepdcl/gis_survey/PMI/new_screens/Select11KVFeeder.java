package com.apepdcl.gis_survey.PMI.new_screens;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.DtrModel;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_11KV;
import com.apepdcl.gis_survey.PMI.models.Feeder33Model;
import com.apepdcl.gis_survey.PMI.models.FeederModel;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
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
 * Created by SOUBHAGYA SWAIN on 02-08-2025.
 */
public class Select11KVFeeder extends AppCompatActivity implements RV11KVAdapter.OnItemClickListener {

    RecyclerView rv_11;
    RelativeLayout logout_layout;
    ImageView back_layout;
    String selectedSSCode11, selectedFeederId11, selectedFeederName11;
    public static ArrayList<PoleModel11KV> polesList11;
    private RetrofitServicePMI retrofitServicePmi;
    ProgressDialog progressDialog;
    private TextView proceed_button;

    private List<FeederModel> feederList = new ArrayList<>();

    private Map<String, String> sectionMap = new LinkedHashMap<>();
    private Map<String, String> subStationMap = new LinkedHashMap<>();
    private Map<String, String> feederMap = new LinkedHashMap<>();

    private String selectedSecId = "";
    private String selectedSubStationId = "";
    private String selectedFeederId = "";

    TextView tv_section,feeder_name,ss_name;
    private List<DtrModel> dtrList = new ArrayList<>();

    private Map<String, DtrModel> dtrMap = new LinkedHashMap<>();


    private DtrModel selectedDtr;

    LinearLayout layout_dtr_details_1;
    TextView new_dtr_code_lt_1,tv_dtr_lt_ckt_1,tv_dtr_slno_1,tv_dtr_make_1,tv_dtr_capacity_1,tv_dtr_phase_1,btn_next;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_11_kv_feeder);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        btn_next = findViewById(R.id.btn_next);
        layout_dtr_details_1 = findViewById(R.id.layout_dtr_details_1);
        new_dtr_code_lt_1 = findViewById(R.id.new_dtr_code_lt_1);
        tv_dtr_lt_ckt_1 = findViewById(R.id.tv_dtr_lt_ckt_1);
        tv_dtr_slno_1 = findViewById(R.id.tv_dtr_slno_1);
        tv_dtr_make_1 = findViewById(R.id.tv_dtr_make_1);
        tv_dtr_capacity_1 = findViewById(R.id.tv_dtr_capacity_1);
        tv_dtr_phase_1 = findViewById(R.id.tv_dtr_phase_1);
        proceed_button = findViewById(R.id.proceed_button);
        tv_section = findViewById(R.id.tv_section);
        ss_name = findViewById(R.id.ss_name);
        feeder_name = findViewById(R.id.feeder_name);

        rv_11 = findViewById(R.id.rv_11);
        //logout_layout = findViewById(R.id.logout_layout);
        back_layout = findViewById(R.id.back_layout);
        polesList11 = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(Select11KVFeeder.this);
        rv_11.setLayoutManager(layoutManager1);
        rv_11.setFocusable(false);
        RV11KVAdapter adapter1 = new RV11KVAdapter(PMINewHome.feederMaster11, Select11KVFeeder.this, Select11KVFeeder.this);
        rv_11.setAdapter(adapter1);
        rv_11.setNestedScrollingEnabled(false);

        getFeederMasterData();


        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get11KVPolesData();
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void parseFeederResponse(JsonArray response) {

        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<FeederModel>>() {}.getType();

        feederList = gson.fromJson(response, listType);

        if (feederList == null) {
            feederList = new ArrayList<>();
        }


        loadSections();
    }

    private void loadSections() {

        sectionMap.clear();

        for (FeederModel model : feederList) {

            if (model.getSecName() != null && model.getSecId() != null) {

                sectionMap.putIfAbsent(
                        model.getSecName(),
                        model.getSecId()
                );
            }
        }

        ArrayList<String> sectionNames = new ArrayList<>(sectionMap.keySet());

        tv_section.setOnClickListener(v -> {

            SearchableSpinner searchableSpinner = new SearchableSpinner(Select11KVFeeder.this);
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

        for (FeederModel model : feederList) {

            if (selectedSecId.equals(model.getSecId())
                    && model.getSubStationName() != null
                    && model.getSubStationId() != null) {

                subStationMap.putIfAbsent(
                        model.getSubStationName(),
                        model.getSubStationId()
                );
            }
        }

        ArrayList<String> subStationNames = new ArrayList<>(subStationMap.keySet());

        ss_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchableSpinner searchableSpinner = new SearchableSpinner(Select11KVFeeder.this);
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

        for (FeederModel model : feederList) {

            if (selectedSecId.equals(model.getSecId())
                    && selectedSubStationId.equals(model.getSubStationId())
                    && model.getFeederName() != null
                    && model.getFeederId() != null) {

                feederMap.putIfAbsent(
                        model.getFeederName(),
                        model.getFeederId()
                );
            }
        }

        ArrayList<String> feederNames = new ArrayList<>(feederMap.keySet());

        feeder_name.setOnClickListener(v -> {

            SearchableSpinner searchableSpinner = new SearchableSpinner(Select11KVFeeder.this);
            searchableSpinner.setWindowTitle("Select 11KV Feeder");

            searchableSpinner.setSpinnerListItems(feederNames);
            searchableSpinner.show();

            searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
                selectedFeederId = feederMap.get(selectedString);
                feeder_name.setText(selectedString);

            });


        });
    }

    private void getFeederMasterData() {
        polesList11.clear();
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

                            JsonArray EHT_SS_MASTER = object.getAsJsonArray("11KV_FEEDER_MASTER");

                            parseFeederResponse(EHT_SS_MASTER);

//

                        } else {
                            progressDialog.cancel();
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(Select11KVFeeder.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(Select11KVFeeder.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Select11KVFeeder.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Select11KVFeeder.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemClick(FEEDER_MASTER_11KV model) {
        selectedSSCode11 = model.getSUB_STATION_ID();
        selectedFeederId11 = model.getFEEDER_ID();
        selectedFeederName11 = model.getFEEDER_NAME();
        get11KVPolesData();

    }

    private void get11KVPolesData() {
        polesList11.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId);
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPoles11(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("RESPONSE");
                            for (int i = 0; i < array.size(); i++) {
                                PoleModel11KV model = new PoleModel11KV();
                                model.setFEEDER_ID(array.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setGLAT(array.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("GLAT").getAsString());
                                model.setPOLE_ID(array.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("REF_ID").getAsString());
                                model.setPOLE_NO(array.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                model.setGLONG(array.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("GLONG").getAsString());
                                model.setPOLE_CONFIG(array.get(i).getAsJsonObject().get("POLE_CONFIG").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_CONFIG").getAsString());
                                model.setPOLE_HEIGHT(array.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
                                model.setPOLE_SUPPORT(array.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
                                model.setPOLE_TYPE(array.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
                                model.setPOLE_STRUCTURE(array.get(i).getAsJsonObject().get("POLE_STRUCTURE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_STRUCTURE").getAsString());
                                polesList11.add(model);
                            }


                            progressDialog.dismiss();
                            Intent intent = new Intent(Select11KVFeeder.this, PmiMap.class);
                            intent.putExtra("ID", selectedFeederId);
                            intent.putExtra("NAME", feeder_name.getText().toString());
                            intent.putExtra("SELECTED_VOLTAGE", "11 KV");
                            intent.putExtra("FROM", "SURVEY");

                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(Select11KVFeeder.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Select11KVFeeder.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                CommanUtilits.netWorkError(Select11KVFeeder.this);
                //Toast.makeText(Select11KVFeeder.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
