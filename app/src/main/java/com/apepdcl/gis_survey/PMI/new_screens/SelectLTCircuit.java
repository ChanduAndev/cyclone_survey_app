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
import com.apepdcl.gis_survey.PMI.PmiHome;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.DTR;
import com.apepdcl.gis_survey.PMI.models.DtrModel;
import com.apepdcl.gis_survey.PMI.models.FeederModel;
import com.apepdcl.gis_survey.PMI.models.PoleModelLT;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.PrepareEstimate;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.NearBy11KVSSModel;
import com.apepdcl.gis_survey.estimates.models.NearBy33KVSSModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 04-08-2025.
 */
public class SelectLTCircuit extends AppCompatActivity implements RVLTAdapter.OnItemClickListener {
    ProgressDialog progressDialog;
    RecyclerView rv_33;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;
    ImageView layout_back;
    private RetrofitServicePMI retrofitServicePmi;
    public static ArrayList<PoleModelLT> polesListLT;

    String selectedDtrCode, selectedLTCircuitCode;
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
        setContentView(R.layout.activity_select_lt_circuit);
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


        rv_33 = findViewById(R.id.rv_33);
        layout_back = findViewById(R.id.layout_back);
        tv_section = findViewById(R.id.tv_section);
        ss_name = findViewById(R.id.ss_name);
        feeder_name = findViewById(R.id.feeder_name);
        proceed_button = findViewById(R.id.proceed_button);
        //logout_layout = findViewById(R.id.logout_layout);
        //profile_layout = findViewById(R.id.profile_layout);
        polesListLT = new ArrayList<>();
        getFeederMasterData();

        /*RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(SelectLTCircuit.this);
        rv_33.setLayoutManager(layoutManager1);
        rv_33.setFocusable(false);
        RVLTAdapter adapter1 = new RVLTAdapter(PMINewHome.dtrMaster, SelectLTCircuit.this, SelectLTCircuit.this);
        rv_33.setAdapter(adapter1);
        rv_33.setNestedScrollingEnabled(false);*/

        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDTRDetails();
            }
        });

        new_dtr_code_lt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDtrList();
            }
        });



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLTPolesData();
            }
        });

    }

    private void parseDtrResponse(JsonArray response) {

        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<DtrModel>>() {}.getType();

        dtrList = gson.fromJson(response, listType);

        if (dtrList == null) {
            dtrList = new ArrayList<>();
        }

        loadDtrList();
    }

    private void loadDtrList() {

        dtrMap.clear();

        for (DtrModel model : dtrList) {

            if (model.getDtrCode() != null) {

                String displayName = model.getDtrCode()
                        + " - "
                        + model.getDtrLocation();

                dtrMap.putIfAbsent(displayName, model);
            }
        }

        ArrayList<String> dtrNames = new ArrayList<>(dtrMap.keySet());

        SearchableSpinner searchableSpinner = new SearchableSpinner(SelectLTCircuit.this);
        searchableSpinner.setWindowTitle("Select DTR");

        searchableSpinner.setSpinnerListItems(dtrNames);
        searchableSpinner.show();

        searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
            selectedDtr = dtrMap.get(selectedString);
            if (selectedDtr != null) {

                selectedDtrCode = selectedDtr.getDtrCode();

                new_dtr_code_lt_1.setText(selectedDtrCode);

                // Display DTR details
                tv_dtr_phase_1.setText(selectedDtr.getPhase());

                tv_dtr_capacity_1.setText(
                        String.valueOf(selectedDtr.getCapacity())
                );

                // Update other fields if available in your API
                tv_dtr_make_1.setText("-");
                tv_dtr_slno_1.setText("-");
                tv_dtr_lt_ckt_1.setText("-");

            }
        });


    }

    private void getDTRDetails() {
        polesListLT.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId);
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
                            layout_dtr_details_1.setVisibility(View.VISIBLE);
                            JsonArray EHT_SS_MASTER = object.getAsJsonArray("DTR");

                            parseDtrResponse(EHT_SS_MASTER);

//

                        } else {
                            progressDialog.cancel();
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(SelectLTCircuit.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(SelectLTCircuit.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SelectLTCircuit.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectLTCircuit.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onItemClick(DTR model) {
        selectedDtrCode = model.getDTR_STRUCTURE_CODE();
        selectedLTCircuitCode = model.getFEEDER_ID();
        getLTPolesData();
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

            SearchableSpinner searchableSpinner = new SearchableSpinner(SelectLTCircuit.this);
            searchableSpinner.setWindowTitle("SELECT SECTION");

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
                SearchableSpinner searchableSpinner = new SearchableSpinner(SelectLTCircuit.this);
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

            SearchableSpinner searchableSpinner = new SearchableSpinner(SelectLTCircuit.this);
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
        polesListLT.clear();
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
                            Toast.makeText(SelectLTCircuit.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(SelectLTCircuit.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SelectLTCircuit.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectLTCircuit.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    private void getLTPolesData() {
        polesListLT.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("DTR_STRUCTURE", selectedDtrCode);
       // requestObject.addProperty("LT_CKTCODE", selectedLTCircuitCode);
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPolesLT(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("RESPONSE");
                            for (int i = 0; i < array.size(); i++) {
                                PoleModelLT model = new PoleModelLT();
                                model.setDTR_CODE(selectedDtrCode);
                                model.setLT_CKT_CODE(array.get(i).getAsJsonObject().get("LT_CKT_CODE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("LT_CKT_CODE").getAsString());
                                model.setGLAT(array.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("GLAT").getAsString());
                                model.setPOLE_ID(array.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("REF_ID").getAsString());
                                model.setPOLE_NO(array.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                model.setGLONG(array.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("GLONG").getAsString());
                                model.setPOLE_CONFIG(array.get(i).getAsJsonObject().get("LINE_CONFIG").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("LINE_CONFIG").getAsString());
                                model.setPOLE_HEIGHT(array.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
                                model.setPOLE_SUPPORT(array.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
                                model.setPOLE_TYPE(array.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
                                //model.setPOLE_STRUCTURE(array.get(i).getAsJsonObject().get("POLE_STRUCTURE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_STRUCTURE").getAsString());
                                polesListLT.add(model);
                            }

                            Intent intent = new Intent(SelectLTCircuit.this, PmiMap.class);
                            intent.putExtra("ID",  selectedDtrCode);
                            intent.putExtra("NAME", selectedDtrCode);
                            intent.putExtra("SELECTED_VOLTAGE", "LT");
                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(SelectLTCircuit.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SelectLTCircuit.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
             progressDialog.dismiss();
                Toast.makeText(SelectLTCircuit.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
