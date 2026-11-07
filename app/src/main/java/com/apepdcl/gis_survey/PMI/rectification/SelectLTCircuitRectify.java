package com.apepdcl.gis_survey.PMI.rectification;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.DTR;
import com.apepdcl.gis_survey.PMI.models.DtrModel;
import com.apepdcl.gis_survey.PMI.models.FeederModel;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;
import com.apepdcl.gis_survey.PMI.models.PoleModelLT;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.PMI.new_screens.RVLTAdapter;
import com.apepdcl.gis_survey.PMI.new_screens.SelectLTCircuit;
import com.apepdcl.gis_survey.WebGis;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.PrepareEstimate;
import com.apepdcl.gis_survey.estimates.models.DTRFeederMaster;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.leo.searchablespinner.SearchableSpinner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 17-03-2026.
 */
public class SelectLTCircuitRectify extends AppCompatActivity implements RVLTAdapterRectify.OnItemClickListener, PoleFeederAdapterLT.OnItemClickListener {

    ProgressDialog progressDialog;
    private BottomSheetDialog poleFeederBottomSheet;
    RecyclerView rv_33;
    ImageView logout_layout;
    ImageView profile_layout;
    private RetrofitServicePMI retrofitServicePmi;
    public static ArrayList<PoleModelLT> polesListLT;
    public static ArrayList<PoleModelLT> aiPoleListLT;
    public static ArrayList<PoleModelLT> nonAiPoleListLT;

    private static ArrayList<PoleNosModelLT> poleNoListLT;

    private LinearLayout new_layout_lt_1;
    TextView new_dtr_code_lt_1;
    String FROM = "";
    LinearLayout main_lt_layout;

    TextView home_heading;
    String selectedDtrCode, selectedLTCircuitCode, selectedDTRLocation;
    TabLayout tabLayout;
    ScrollView directRectification;

    private TextView proceed_button;
    LinearLayout layout_dtr_details_1;

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
    TextView tv_dtr_lt_ckt_1,tv_dtr_slno_1,tv_dtr_make_1,tv_dtr_capacity_1,tv_dtr_phase_1,btn_next;
    private DtrModel selectedDtr;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_lt_circuit_rectification);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        FROM=getIntent().getStringExtra("FROM");

        rv_33 = findViewById(R.id.rv_33);
        logout_layout = findViewById(R.id.layout_back);
        profile_layout = findViewById(R.id.layout_back);
        new_layout_lt_1 = findViewById(R.id.new_layout_lt_1);
        new_dtr_code_lt_1 = findViewById(R.id.new_dtr_code_lt_1);
        main_lt_layout = findViewById(R.id.main_lt_layout);
        home_heading = findViewById(R.id.home_heading);
        tabLayout = findViewById(R.id.tab_layout_lt);
        ss_name = findViewById(R.id.ss_name);
        tv_section = findViewById(R.id.tv_section);
        feeder_name = findViewById(R.id.feeder_name);
        proceed_button = findViewById(R.id.proceed_button);
        tv_dtr_lt_ckt_1 = findViewById(R.id.tv_dtr_lt_ckt_1);
        tv_dtr_slno_1 = findViewById(R.id.tv_dtr_slno_1);
        tv_dtr_make_1 = findViewById(R.id.tv_dtr_make_1);
        tv_dtr_capacity_1 = findViewById(R.id.tv_dtr_capacity_1);
        tv_dtr_phase_1 = findViewById(R.id.tv_dtr_phase_1);
        btn_next = findViewById(R.id.btn_next);
        directRectification =findViewById(R.id.direct_rectification_scroll_lt);
        layout_dtr_details_1 = findViewById(R.id.layout_dtr_details_1);
        polesListLT = new ArrayList<>();
        aiPoleListLT = new ArrayList<>();
        nonAiPoleListLT = new ArrayList<>();
        poleNoListLT = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(SelectLTCircuitRectify.this);
        rv_33.setLayoutManager(layoutManager1);
        rv_33.setFocusable(false);
        RVLTAdapterRectify adapter1 = new RVLTAdapterRectify(PMINewHome.dtrMaster, SelectLTCircuitRectify.this, SelectLTCircuitRectify.this);
        rv_33.setAdapter(adapter1);
        rv_33.setNestedScrollingEnabled(false);

        tabLayout.addTab(tabLayout.newTab().setText("Surveyed Locations Rectification"));
        tabLayout.addTab(tabLayout.newTab().setText("Direct Rectification Update"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    rv_33.setVisibility(View.VISIBLE);
                    directRectification.setVisibility(View.GONE);

                } else  {
                    rv_33.setVisibility(View.GONE);
                    directRectification.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        btn_next.setOnClickListener(v -> {
            getLTPolesDataOther();
        });

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDTRDetails();
            }
        });


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

        new_dtr_code_lt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDTRNearByDialog(new_dtr_code_lt_1, "1");



            }
        });
        getFeederMasterData();
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
                            Toast.makeText(SelectLTCircuitRectify.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(SelectLTCircuitRectify.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SelectLTCircuitRectify.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectLTCircuitRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
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

        SearchableSpinner searchableSpinner = new SearchableSpinner(SelectLTCircuitRectify.this);
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
    private void openDTRNearByDialog(TextView edt_text, String dtr_view) {

        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        payDialog.setContentView(R.layout.dtr_master_dialog);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);
        LinearLayout layout_dtr_meters = payDialog.findViewById(R.id.layout_dtr_meters);
        Spinner sp_dtr_meter = payDialog.findViewById(R.id.sp_dtr_meter);

        // Default visibility because Nearby DTRs is checked
        layout_dtr_meters.setVisibility(View.VISIBLE);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rb_nearby) {
                layout_dtr_meters.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.rb_total) {
                layout_dtr_meters.setVisibility(View.GONE);
            }
        });


        title.setText("Select DTR !!");

        payDialog.show();

        back_to_home.setOnClickListener(v -> {

            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = payDialog.findViewById(selectedId);
            String selectedOption = radioButton.getText().toString().trim();

            if (selectedOption.equalsIgnoreCase("Nearby DTRs")) {

                if (sp_dtr_meter.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(this, "Please select meters", Toast.LENGTH_SHORT).show();
                    return;
                }

                String SelectedMeters = sp_dtr_meter.getSelectedItem().toString();
                payDialog.dismiss();

                switch (SelectedMeters) {

                    case "50":
                        openDTRSpinner("SELECT NEARBY DTR",
                                EstimatesHome.NEAR_BY_50_DTR_MODELS,
                                EstimatesHome.nearby50DTRNames,
                                edt_text, dtr_view);
                        break;

                    case "100":
                        openDTRSpinner("SELECT NEARBY DTR",
                                EstimatesHome.NEAR_BY_100_DTR_MODELS,
                                EstimatesHome.nearby100DTRNames,
                                edt_text, dtr_view);
                        break;

                    case "200":
                        openDTRSpinner("SELECT NEARBY DTR",
                                EstimatesHome.NEAR_BY_200_DTR_MODELS,
                                EstimatesHome.nearby200DTRNames,
                                edt_text, dtr_view);
                        break;

                    case "500":
                        openDTRSpinner("SELECT NEARBY DTR",
                                EstimatesHome.NEAR_BY_500_DTR_MODELS,
                                EstimatesHome.nearby500DTRNames,
                                edt_text, dtr_view);
                        break;

                    case "1000":
                        openDTRSpinner("SELECT NEARBY DTR",
                                EstimatesHome.NEAR_BY_1000_DTR_MODELS,
                                EstimatesHome.nearby1000DTRNames,
                                edt_text, dtr_view);
                        break;
                }

            } else {
                payDialog.dismiss();
                openDTRSpinner("SELECT TOTAL DTR",
                        EstimatesHome.TOTAL_DTR_MODELS,
                        EstimatesHome.totalDTRNames,
                        edt_text, dtr_view);
            }
        });


        cancel_button.setOnClickListener(v -> payDialog.dismiss());
    }

    private void openDTRSpinner(String title,
                                List<DTRFeederMaster> dtrModels,
                                ArrayList<String> spinnerList,
                                TextView edt_text, String dtr_view) {

        SearchableSpinner searchableSpinner = new SearchableSpinner(this);
        searchableSpinner.setWindowTitle(title);

        final HashMap<String, DTRFeederMaster> modelMap = new HashMap<>();

        for (DTRFeederMaster model : dtrModels) {
            String displayText = model.getDTR_LOCATION() + "~" + model.getDTR_STRUCTURE_CODE();
            modelMap.put(displayText, model);
        }

        searchableSpinner.setSpinnerListItems(spinnerList);
        searchableSpinner.show();

        searchableSpinner.setOnItemSelectListener((position, selectedString) -> {

        });
    }



    @Override
    public void onItemClick(DTR model) {
        selectedDtrCode = model.getDTR_STRUCTURE_CODE();
        selectedLTCircuitCode = model.getFEEDER_ID();
        selectedDTRLocation = model.getDTR_LOCATION();
        //getLTPolesData();
        // showAIDialog();
        getPoleCount();
    }

    private void getLTPolesDataOther() {
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

                            Intent intent = new Intent(SelectLTCircuitRectify.this, PmiMap.class);
                            intent.putExtra("ID",  selectedDtrCode);
                            intent.putExtra("NAME", selectedDtrCode);
                            intent.putExtra("SELECTED_VOLTAGE", "LT");
                            intent.putExtra("FROM", "RECT");
                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(SelectLTCircuitRectify.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SelectLTCircuitRectify.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectLTCircuitRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getPoleCount() {
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedDtrCode);
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
                            poleNoListLT.clear();
                            for (int i = 0; i < array.size(); i++) {
                                PoleNosModelLT model = new PoleNosModelLT();
                                model.setFEEDER_ID(array.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setPOLE_ID(array.get(i).getAsJsonObject().get("POLE_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_ID").getAsString());
                                model.setPOLE_NO(array.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                poleNoListLT.add(model);
                            }


                            progressDialog.dismiss();
                            showPoleFeederBottomSheet(poleNoListLT);
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
                        Toast.makeText(SelectLTCircuitRectify.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SelectLTCircuitRectify.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                CommanUtilits.netWorkError(SelectLTCircuitRectify.this);
                //Toast.makeText(Select11KVFeederRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showPoleFeederBottomSheet(
            List<PoleNosModelLT> poleFeederList) {

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

        PoleFeederAdapterLT adapter = new PoleFeederAdapterLT(this, poleFeederList, SelectLTCircuitRectify.this);

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

    private void getLTPolesData() {
        polesListLT.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("DTR_STRUCTURE", selectedDtrCode);
        requestObject.addProperty("LT_CKTCODE", selectedLTCircuitCode);
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPolesLTRectify(requestObject);


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
                                model.setDEFECT_TYPE(array.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
                                model.setDEFECT_GROUP(array.get(i).getAsJsonObject().get("DEFECT_GROUP").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("DEFECT_GROUP").getAsString());
                                model.setIS_RECTIFIED(array.get(i).getAsJsonObject().get("RECTIFIED").isJsonNull() ? "N" : array.get(i).getAsJsonObject().get("RECTIFIED").getAsString());
                                model.setAI_PENDING("N");
                                //model.setRECT_ID(array.get(i).getAsJsonObject().get("RECT_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("RECT_ID").getAsString());
                                polesListLT.add(model);
                            }

                            // showSelectSurveyTypeDialog();

                            Intent intent = new Intent(SelectLTCircuitRectify.this, SelectLTPoleRectify.class);
                            intent.putExtra("LT_CKT_CODE", selectedLTCircuitCode);
                            intent.putExtra("DTR_CODE", selectedDtrCode);
                            intent.putExtra("DTR_LOCATION", selectedDTRLocation);
                            startActivity(intent);
                            finish();

                            //showAIDialog();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(SelectLTCircuitRectify.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SelectLTCircuitRectify.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectLTCircuitRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getLtPolesData(PoleNosModelLT model) {
        polesListLT.clear();
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("POLE_ID", model.getPOLE_ID());
        requestObject.addProperty("VOLTAGE_LEVEL", "LT");
//        requestObject.addProperty("DTR_STRUCTURE", selectedDtrCode);
//        requestObject.addProperty("LT_CKTCODE", selectedLTCircuitCode);
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPolesLTRectifyNew(requestObject);



        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();

                    if (MobileConstants.TRUE.equalsIgnoreCase(
                            surveyedResponseVo.get("STATUS").getAsString())) {

                        try {

                            // RESPONSE is directly a JSON Array
                            JsonArray responseArray =
                                    surveyedResponseVo.getAsJsonArray("RESPONSE");

                            if (responseArray != null) {

                                for (int i = 0; i < responseArray.size(); i++) {

                                    JsonObject poleObject =
                                            responseArray.get(i).getAsJsonObject();

                                    PoleModelLT model = new PoleModelLT();

                                    model.setGLAT(getJsonValue(poleObject, "GLAT"));
                                    model.setPOLE_ID(getJsonValue(poleObject, "REF_ID"));
                                    model.setPOLE_NO(getJsonValue(poleObject, "POLE_NO"));
                                    model.setGLONG(getJsonValue(poleObject, "GLONG"));

                                    model.setLT_CKT_CODE(
                                            getJsonValue(poleObject, "LT_CKT_CODE"));

                                    model.setPOLE_HEIGHT(
                                            getJsonValue(poleObject, "POLE_HEIGHT"));

                                    model.setPOLE_SUPPORT(
                                            getJsonValue(poleObject, "POLE_SUPPORT"));

                                    model.setPOLE_TYPE(
                                            getJsonValue(poleObject, "POLE_TYPE"));

                                    model.setDEFECT_TYPE(
                                            getJsonValue(poleObject, "DEFECT_TYPE"));

                                    model.setGRP_ID(
                                            getJsonValue(poleObject, "GRP_ID"));

                                    model.setAI_FLAG(
                                            getJsonValue(poleObject, "AI_FLAG"));

                                    model.setDEFECT_SUB_TYPE(
                                            getJsonValue(poleObject, "DEFECT_SUB_TYPE"));

                                    model.setIS_RECTIFIED(
                                            getJsonValue(poleObject, "RECTIFIED", "N"));

                                    model.setIMG_URL(
                                            getJsonValue(poleObject, "IMG_URL"));

                                    model.setAI_PENDING("N");

                                    // Add records to NON AI list
                                    nonAiPoleListLT.add(model);
                                }
                            }

                            progressDialog.dismiss();

                            Intent intent = new Intent(
                                    SelectLTCircuitRectify.this,
                                    SelectLTPoleRectify.class
                            );

                            intent.putExtra("LT_CKT_CODE", selectedLTCircuitCode);
                            intent.putExtra("DTR_CODE", selectedDtrCode);
                            intent.putExtra("DTR_LOCATION", selectedDTRLocation);

                            startActivity(intent);
                            finish();

                        } catch (Exception e) {

                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    } else {

                        progressDialog.dismiss();

                        Toast.makeText(
                                SelectLTCircuitRectify.this,
                                surveyedResponseVo.has("ERR_MSG")
                                        ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                        : "No pole data found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {

                    progressDialog.dismiss();

                    Toast.makeText(
                            SelectLTCircuitRectify.this,
                            "Server Busy Try Again",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressDialog.dismiss();

                CommanUtilits.netWorkError(SelectLTCircuitRectify.this);
            }
        });


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



    @Override
    public void onItemClick(PoleNosModelLT model) {
        getLtPolesData(model);
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

            SearchableSpinner searchableSpinner = new SearchableSpinner(SelectLTCircuitRectify.this);
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
                SearchableSpinner searchableSpinner = new SearchableSpinner(SelectLTCircuitRectify.this);
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

            SearchableSpinner searchableSpinner = new SearchableSpinner(SelectLTCircuitRectify.this);
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
                            Toast.makeText(SelectLTCircuitRectify.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(SelectLTCircuitRectify.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SelectLTCircuitRectify.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectLTCircuitRectify.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
