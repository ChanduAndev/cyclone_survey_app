package com.apepdcl.gis_survey.RDSS.edit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.apepdcl.gis_survey.PMI.models.EHT_SS_MASTER_ALL;
import com.apepdcl.gis_survey.PMI.models.SS_MASTER_ALL;
import com.apepdcl.gis_survey.RDSS.UGCableProposedMap;
import com.apepdcl.gis_survey.estimates.sou.EditLocationsMap;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.JsonParser;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.models.FeedersData;
import com.apepdcl.gis_survey.RDSS.AssetsHomeRdssUgCable;
import com.apepdcl.gis_survey.RDSS.RdssUGDatabase;
import com.apepdcl.gis_survey.RDSS.RetrofitClientInstanceRDSS;
import com.apepdcl.gis_survey.RDSS.RetrofitServiceRDSS;
import com.apepdcl.gis_survey.RDSS.UGPostingModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPoleAttributesRdss extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int MAX_FEEDERS = 10;
    private LinearLayout[] layout11KV = new LinearLayout[MAX_FEEDERS];
    private LinearLayout[] layout33KV = new LinearLayout[MAX_FEEDERS];
    private TextView[] ssName11KV = new TextView[MAX_FEEDERS];
    private TextView[] feederNo11KV = new TextView[MAX_FEEDERS];
    private TextView[] ssName33KV = new TextView[MAX_FEEDERS];
    private TextView[] feederNo33KV = new TextView[MAX_FEEDERS];

    LinearLayout[] layout_dtr_details_lt = new LinearLayout[10];
    Spinner[] sp_dtr_capacity_lt = new Spinner[10];
    Spinner[] sp_cable_size_lt = new Spinner[10];
    Spinner[] sp_dtr_phase_lt = new Spinner[10];
    EditText[] et_dtr_str_code = new EditText[10];
    EditText[] et_dtr_loc_name = new EditText[10];

    EditText[] remarks_33kv = new EditText[MAX_FEEDERS];
    EditText[] remarks_11kv = new EditText[MAX_FEEDERS];
    EditText[] et_remarks_lt = new EditText[MAX_FEEDERS];

    private TextView[] no_of_cables_33kv = new TextView[MAX_FEEDERS];
    private TextView[] no_of_cables_11kv = new TextView[MAX_FEEDERS];
    private TextView[] no_of_cables_lt = new TextView[MAX_FEEDERS];

    private LinearLayout[] newLayout33KV = new LinearLayout[10];
    private LinearLayout[] newLayout11KV = new LinearLayout[10];

    private Spinner[] sp_cable_size_33kv = new Spinner[10];
    private Spinner[] sp_cable_size_11kv = new Spinner[10];

    private Spinner spWidth, spDepth, spNo33, spNo11, spNoLT, spUtilityType, sp_location_type, sp_rmu_type_rdss, sp_dtr_capacity_rdss, sp_no_of_33, sp_no_of_11;
    private CheckBox chUtilities;
    private EditText etUtility, et_location_remarks, et_ht_sc_rdss;
    private TextView submit;
    private UGPostingModel updatedModel;
    private LinearLayout layout_rmu_type_rdss, layout_dtr_capacity_rdss, layout_ht_sc_no_rdss, layout_source_location, layout_pole_details, source_pole_layout;
    private TextView pole_no, from_pole, thirty_kv, eleven_kv, lt_feeder;
    private String ID = "", LOCATION_ID = "", VOLTAGE_LEVEL = "", protectionType = "", selectedCount_33kv = "", selectedCount_11kv = "", selectedCount_lt = "";
    private String FROM = "";
    RelativeLayout back_layout, close_layout;

    Spinner sp_conductor_size_33, sp_conductor_size_11;
    CheckBox checkbox_gi_pipes, checkbox_dwc_pipes;

    private TextView[] subStationNames, subStation11KVNames;
    private TextView[] feederNos, feeder11KVNos;

    private String[] selectedSSCode33 = new String[10];
    private String[] selectedSSCode11 = new String[10];


    public static double SELECTED_LATTITUDE_33KV = 0.0;
    public static double SELECTED_LONGITUDE_33KV = 0.0;
    public static double SELECTED_LATTITUDE_11KV = 0.0;
    public static double SELECTED_LONGITUDE_11KV = 0.0;

    private ProgressDialog progressDialog;
    private TextView gi_pipe, dwc_pipe;


    private LinearLayout layout_utility_type_rdss, layout_no_of_feeders_33kv, layout_no_of_feeders_11kv, layout_no_of_feeders_lt,
            layout_no_of_feeders_33kv_new, layout_no_of_feeders_11kv_new, layout_no_of_feeders_lt_new;

    RdssUGDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_pole_attributes_rdss);
        database = new RdssUGDatabase(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });
        Arrays.fill(selectedSSCode11, "");
        Arrays.fill(selectedSSCode33, "");
        progressDialog = CommanUtilits.progressDialog(this);
        FROM = getIntent().getStringExtra("FROM");
        ID = getIntent().getStringExtra("ID");
        LOCATION_ID = getIntent().getStringExtra("LOCATION_ID");
        VOLTAGE_LEVEL = getIntent().getStringExtra("VOLTAGE_LEVEL");


        updatedModel = (UGPostingModel) getIntent().getSerializableExtra("DATA_MODEL");
        assert updatedModel != null;
        selectedCount_33kv = updatedModel.getCABLE_33KV();
        selectedCount_11kv = updatedModel.getCABLE_11KV();
        selectedCount_lt = updatedModel.getCABLE_LT();

        initViews();
        setupData();
        submitButtonClick();


        int selectedIndex = Integer.parseInt(selectedCount_33kv);

        for (int i = 0; i < MAX_FEEDERS; i++) {

            if (i == selectedIndex) {
                no_of_cables_33kv[i].setBackgroundResource(R.drawable.bg_selected_page);
                no_of_cables_33kv[i].setTextColor(Color.WHITE);
            } else {
                no_of_cables_33kv[i].setBackground(null); // or bg_unselected_page
                no_of_cables_33kv[i].setTextColor(Color.parseColor("#8A94A6"));
            }
        }

        int selectedIndex_11kv = Integer.parseInt(selectedCount_11kv);

        for (int i = 0; i < MAX_FEEDERS; i++) {

            if (i == selectedIndex_11kv) {
                no_of_cables_11kv[i].setBackgroundResource(R.drawable.bg_selected_page);
                no_of_cables_11kv[i].setTextColor(Color.WHITE);
            } else {
                no_of_cables_11kv[i].setBackground(null); // or bg_unselected_page
                no_of_cables_11kv[i].setTextColor(Color.parseColor("#8A94A6"));
            }
        }

        int selectedIndex_lt = Integer.parseInt(selectedCount_lt);

        for (int i = 0; i < MAX_FEEDERS; i++) {

            if (i == selectedIndex_lt) {
                no_of_cables_lt[i].setBackgroundResource(R.drawable.bg_selected_page);
                no_of_cables_lt[i].setTextColor(Color.WHITE);
            } else {
                no_of_cables_lt[i].setBackground(null); // or bg_unselected_page
                no_of_cables_lt[i].setTextColor(Color.parseColor("#8A94A6"));
            }
        }


        for (int i = 0; i < MAX_FEEDERS; i++) {

            no_of_cables_33kv[i].setOnClickListener(v -> {

                // Reset all TextViews
                for (int j = 0; j < MAX_FEEDERS; j++) {
                    no_of_cables_33kv[j].setBackground(null);
                    no_of_cables_33kv[j].setTextColor(
                            Color.parseColor("#8A94A6")
                    );
                }

                v.setBackgroundResource(R.drawable.bg_selected_page);
                ((TextView) v).setTextColor(Color.WHITE);

                selectedCount_33kv = ((TextView) v).getText().toString();

                updateLayouts(selectedCount_33kv, "33KV");


            });

            no_of_cables_11kv[i].setOnClickListener(v -> {

                // Reset all TextViews
                for (int j = 0; j < MAX_FEEDERS; j++) {
                    no_of_cables_11kv[j].setBackground(null);
                    no_of_cables_11kv[j].setTextColor(
                            Color.parseColor("#8A94A6")
                    );
                }

                v.setBackgroundResource(R.drawable.bg_selected_page);
                ((TextView) v).setTextColor(Color.WHITE);

                selectedCount_11kv = ((TextView) v).getText().toString();

                updateLayouts(selectedCount_11kv, "11KV");


            });

            no_of_cables_lt[i].setOnClickListener(v -> {

                // Reset all TextViews
                for (int j = 0; j < MAX_FEEDERS; j++) {
                    no_of_cables_lt[j].setBackground(null);
                    no_of_cables_lt[j].setTextColor(
                            Color.parseColor("#8A94A6")
                    );
                }

                v.setBackgroundResource(R.drawable.bg_selected_page);
                ((TextView) v).setTextColor(Color.WHITE);

                selectedCount_lt = ((TextView) v).getText().toString();
                updateLayouts(selectedCount_lt, "LT");

            });
        }

        dwc_pipe.setOnClickListener(v -> {
            dwc_pipe.setSelected(true);
            gi_pipe.setSelected(false);
            protectionType = "DWC Pipe";

        });

        gi_pipe.setOnClickListener(v -> {
            dwc_pipe.setSelected(false);
            gi_pipe.setSelected(true);
            protectionType = "GI Pipe";
        });

        if (FROM.equalsIgnoreCase("SOURCE_POLE")) {
            source_pole_layout.setVisibility(View.VISIBLE);
            layout_pole_details.setVisibility(View.GONE);

            String poleNumber = updatedModel.getLOCATION_NO();
            String lattitude = updatedModel.getLON();
            String longi = updatedModel.getLAT();

        }

        if ("33KV".equalsIgnoreCase(VOLTAGE_LEVEL)) {
            sp_conductor_size_33.setVisibility(View.VISIBLE);
        }
        if ("11KV".equalsIgnoreCase(VOLTAGE_LEVEL)) {
            sp_conductor_size_11.setVisibility(View.VISIBLE);
        }


        checkbox_gi_pipes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox_dwc_pipes.setChecked(false);
                }
            }
        });

        checkbox_dwc_pipes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox_gi_pipes.setChecked(false);
                }
            }
        });


        // Step 3: Add click listeners in a loop
        for (int i = 0; i < subStationNames.length; i++) {
            final int index = i;
            subStationNames[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    open33KVSSNearByDialog(subStationNames[index], feederNos[index], String.valueOf(index + 1));
                }
            });
        }


        for (int i = 0; i < feederNos.length; i++) {
            final int index = i;
            feederNos[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subStationNames[index].getText().toString().isEmpty()) {
                        Toast.makeText(
                                EditPoleAttributesRdss.this,
                                "Please Select EHT SS Name For Feeder " + (index + 1),
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        open33KVFeederMasterDialogNew(feederNos[index], selectedSSCode33[index]);
                    }
                }
            });
        }

        for (int i = 0; i < feeder11KVNos.length; i++) {
            final int index = i;
            feeder11KVNos[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subStation11KVNames[index].getText().toString().isEmpty()) {
                        Toast.makeText(
                                EditPoleAttributesRdss.this,
                                "Please Select EHT SS Name For Feeder " + (index + 1),
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        open11KVFeederMasterDialogNew(feeder11KVNos[index], selectedSSCode11[index]);
                    }
                }
            });
        }


        // Step 3: Add click listeners in a loop
        for (int i = 0; i < subStation11KVNames.length; i++) {
            final int index = i;
            subStation11KVNames[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    open11KVSSNearByDialog(subStation11KVNames[index], feeder11KVNos[index], String.valueOf(index + 1));
                }
            });
        }


        chUtilities.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layout_utility_type_rdss.setVisibility(View.VISIBLE);
            } else {
                layout_utility_type_rdss.setVisibility(View.GONE);
            }
        });

        thirty_kv.setOnClickListener(v -> {

            thirty_kv.setSelected(true);
            eleven_kv.setSelected(false);
            lt_feeder.setSelected(false);

            layout_no_of_feeders_33kv_new.setVisibility(View.VISIBLE);
            layout_no_of_feeders_11kv_new.setVisibility(View.GONE);
            layout_no_of_feeders_lt_new.setVisibility(View.GONE);

            layout_no_of_feeders_33kv.setVisibility(View.GONE);
            layout_no_of_feeders_11kv.setVisibility(View.GONE);
            layout_no_of_feeders_lt.setVisibility(View.GONE);
            // selectedCount_33kv = sp_no_of_33.getSelectedItem().toString();

            updateLayouts(selectedCount_33kv, "33KV");
            for (int i = 0; i < 10; i++) {
                newLayout11KV[i].setVisibility(View.GONE);
            }

            for (int i = 0; i < 10; i++) {
                layout_dtr_details_lt[i].setVisibility(View.GONE);
            }

            int selectCount = Integer.parseInt(selectedCount_33kv);
            for (int i = 0; i < selectCount; i++) {
                newLayout33KV[i].setVisibility(View.VISIBLE);
            }



        });
        eleven_kv.setOnClickListener(v -> {

            eleven_kv.setSelected(true);
            thirty_kv.setSelected(false);
            lt_feeder.setSelected(false);

            layout_no_of_feeders_33kv_new.setVisibility(View.GONE);
            layout_no_of_feeders_11kv_new.setVisibility(View.VISIBLE);
            layout_no_of_feeders_lt_new.setVisibility(View.GONE);
            layout_no_of_feeders_33kv.setVisibility(View.GONE);
            layout_no_of_feeders_11kv.setVisibility(View.GONE);
            layout_no_of_feeders_lt.setVisibility(View.GONE);
            //selectedCount_11kv = sp_no_of_11.getSelectedItem().toString();
            updateLayouts(selectedCount_11kv, "11KV");
            int selectCount = Integer.parseInt(selectedCount_11kv);
            for (int i = 0; i < selectCount; i++) {
                newLayout11KV[i].setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < 10; i++) {
                newLayout33KV[i].setVisibility(View.GONE);
            }
            for (int i = 0; i < 10; i++) {
                layout_dtr_details_lt[i].setVisibility(View.GONE);
            }



        });
        lt_feeder.setOnClickListener(v -> {

            lt_feeder.setSelected(true);
            eleven_kv.setSelected(false);
            thirty_kv.setSelected(false);

            layout_no_of_feeders_33kv_new.setVisibility(View.GONE);
            layout_no_of_feeders_11kv_new.setVisibility(View.GONE);
            layout_no_of_feeders_lt_new.setVisibility(View.VISIBLE);
            layout_no_of_feeders_33kv.setVisibility(View.GONE);
            layout_no_of_feeders_11kv.setVisibility(View.GONE);
            layout_no_of_feeders_lt.setVisibility(View.GONE);
            updateLayouts(selectedCount_lt, "LT");
            for (int i = 0; i < 10; i++) {
                newLayout33KV[i].setVisibility(View.GONE);
                newLayout11KV[i].setVisibility(View.GONE);
            }



        });
    }

    private void updateLayouts(String selectedCount, String voltage_level) {

        int count = 0;

        try {
            count = Integer.parseInt(selectedCount);
        } catch (NumberFormatException e) {
            count = 0;
        }

        // Show required 33KV layouts
        for (int i = 0; i < 10; i++) {
            if (voltage_level.equalsIgnoreCase("33KV")) {
                if (i < count) {
                    newLayout33KV[i].setVisibility(View.VISIBLE);
                } else {
                    newLayout33KV[i].setVisibility(View.GONE);
                }
            } else if (voltage_level.equalsIgnoreCase("11KV")) {
                if (i < count) {
                    newLayout11KV[i].setVisibility(View.VISIBLE);
                } else {
                    newLayout11KV[i].setVisibility(View.GONE);
                }
            } else if (voltage_level.equalsIgnoreCase("LT")) {
                if (i < count) {
                    layout_dtr_details_lt[i].setVisibility(View.VISIBLE);
                } else {
                    layout_dtr_details_lt[i].setVisibility(View.GONE);
                }
            }
        }


    }

    private void submitButtonClick() {
        submit.setOnClickListener(v -> {
            if (FROM.equalsIgnoreCase("POLE_RDSS")) {
                // Validate location type
                String locationType = sp_location_type.getSelectedItem().toString();
                if (locationType.equalsIgnoreCase("Select")) {
                    toast("Please Select Location Type");
                    return;
                }

                // RMU Validation
                if (locationType.equalsIgnoreCase("RMU")) {
                    if (sp_rmu_type_rdss.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        toast("Please Select RMU Type");
                        return;
                    }
                    if (et_location_remarks.getText().toString().isEmpty()) {
                        toast("Please Enter Location remarks");
                        return;
                    }
                }

                // DTR Validation
                if (locationType.equalsIgnoreCase("DTR")) {
                    if (sp_dtr_capacity_rdss.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        toast("Please Select DTR Capacity");
                        return;
                    }
                    if (et_location_remarks.getText().toString().isEmpty()) {
                        toast("Please Enter Location remarks");
                        return;
                    }
                }

                // HT Service Validation
                if (locationType.equalsIgnoreCase("HT Service")) {
                    if (et_ht_sc_rdss.getText().toString().isEmpty()) {
                        toast("Please Enter HT Service Number");
                        return;
                    }
                    if (et_location_remarks.getText().toString().isEmpty()) {
                        toast("Please Enter Location remarks");
                        return;
                    }
                }

                // Spinner Validations
                if (isSelect(spWidth)) {
                    toast("Please Select Width");
                    return;
                }
                if (isSelect(spDepth)) {
                    toast("Please Select Depth");
                    return;
                }
                if (isSelect(spNo33)) {
                    toast("Please Select No of 33KV Cables");
                    return;
                }
                if (isSelect(spNo11)) {
                    toast("Please Select No of 11KV Cables");
                    return;
                }
                if (isSelect(spNoLT)) {
                    toast("Please Select No of LT Cables");
                    return;
                }

                int selected_33kv = Integer.parseInt(
                        sp_no_of_33.getSelectedItem().toString()
                );

                for (int i = 0; i < selected_33kv; i++) {

                    String selectedValue = selectedCount_33kv;

                    if (selectedValue.equalsIgnoreCase("Select")) {

                        Toast.makeText(
                                this,
                                "Please Select Cable Size of 33KV " + (i + 1),
                                Toast.LENGTH_SHORT
                        ).show();

                        return; // Stop further processing
                    }
                }

                int selected_11kv = Integer.parseInt(
                        selectedCount_11kv
                );

                for (int i = 0; i < selected_11kv; i++) {

                    String selectedValue = selectedCount_11kv;

                    if (selectedValue.equalsIgnoreCase("Select")) {

                        Toast.makeText(
                                this,
                                "Please Select Cable Size of 11KV " + (i + 1),
                                Toast.LENGTH_SHORT
                        ).show();

                        return; // Stop further processing
                    }
                }

                // Utility validation
                if (chUtilities.isChecked()) {
                    if (isSelect(spUtilityType)) {
                        toast("Please Select Utility type");
                        return;
                    }
                    if (etUtility.getText().toString().trim().isEmpty()) {
                        toast("Please Enter Utility");
                        return;
                    }
                }

                // Feeder validation using arrays
                if (!validateFeeders(spNo33, ssName33KV, feederNo33KV, "33KV")) return;
                if (!validateFeeders(spNo11, ssName11KV, feederNo11KV, "11KV")) return;

                // ✅ Prepare model
                UGPostingModel model = new UGPostingModel();
                model.setLOCATION_ID(updatedModel.getLOCATION_ID());
                model.setWIDTH(spWidth.getSelectedItem().toString());
                model.setDEPTH(spDepth.getSelectedItem().toString());
                model.setCABLE_33KV(selectedCount_33kv);
                model.setCABLE_11KV(selectedCount_11kv);
                model.setCABLE_LT(selectedCount_lt);
                model.setUTILITY_TYPE(chUtilities.isChecked() ? spUtilityType.getSelectedItem().toString() : "");
                model.setUTILITY(chUtilities.isChecked() ? etUtility.getText().toString() : "");
                model.setLOCATION_TYPE(locationType);
                model.setLOCATION_REMARKS(et_location_remarks.getText().toString());
                model.setID(updatedModel.getID());
                model.setMODE("POLE");

                if (protectionType.equalsIgnoreCase("DWC Pipe")) {
                    model.setPROTECTION_PIPE("DWC Pipe");
                }
                if (protectionType.equalsIgnoreCase("GI Pipe")) {
                    model.setPROTECTION_PIPE("GI Pipe");
                }


                Log.d("UGModelCheck", "updatedModel.getID() = " + updatedModel.getID());
                Log.d("UGModelCheck", "model.getID() after set = " + model.getID());

                model.setSOURCE_LOCATION(from_pole.getText().toString());


                // Add feeders
                ArrayList<FeedersData> feedersList = new ArrayList<>();
                feedersList.addAll(getFeeders(selectedCount_33kv, ssName33KV, feederNo33KV, "33KV", sp_cable_size_33kv,null,null,null,null,remarks_33kv));
                feedersList.addAll(getFeeders(selectedCount_11kv, ssName11KV, feederNo11KV, "11KV", sp_cable_size_11kv,null,null,null,null,remarks_11kv));
                feedersList.addAll(getFeeders(selectedCount_lt, null, null, "LT", sp_cable_size_lt,sp_dtr_phase_lt,sp_dtr_capacity_lt,et_dtr_str_code,et_dtr_loc_name,et_remarks_lt));
                model.setFeedersData(feedersList);


                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(model);
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                progressDialog = new ProgressDialog(EditPoleAttributesRdss.this);
                progressDialog.setMessage("Fetching pole data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                RetrofitServiceRDSS retrofitServiceRDSS =
                        RetrofitClientInstanceRDSS.getRetrofitInstance().create(RetrofitServiceRDSS.class);
                Call<JsonObject> responseVoCall = retrofitServiceRDSS.getEditSurveyLocation(jsonObject);

                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        if (response.body() != null) {

                            JsonObject surveyedResponseVo = response.body();
                            if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                                Toast.makeText(EditPoleAttributesRdss.this,
                                        surveyedResponseVo.get("ERR_MSG").toString(),
                                        Toast.LENGTH_SHORT).show();
                                getOnBackPressedDispatcher().onBackPressed();
                            } else {

                                Toast.makeText(EditPoleAttributesRdss.this,
                                        surveyedResponseVo.get("ERR_MSG").toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(EditPoleAttributesRdss.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(EditPoleAttributesRdss.this, "Try Again " + t.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            } else if (FROM.equalsIgnoreCase("SOURCE_POLE")) {
                // ✅ Prepare model
                UGPostingModel model = new UGPostingModel();
                model.setLOCATION_ID(updatedModel.getLOCATION_ID());
                model.setMODE("POLE");
                model.setSOURCE_LOCATION(from_pole.getText().toString());
                model.setLOCATION_NO(pole_no.getText().toString());
                model.setID(updatedModel.getID());
                ArrayList<FeedersData> feedersList = new ArrayList<>();
                model.setFeedersData(feedersList);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(model);
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                progressDialog = new ProgressDialog(EditPoleAttributesRdss.this);
                progressDialog.setMessage("Fetching pole data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                RetrofitServiceRDSS retrofitServiceRDSS =
                        RetrofitClientInstanceRDSS.getRetrofitInstance().create(RetrofitServiceRDSS.class);
                Call<JsonObject> responseVoCall = retrofitServiceRDSS.getEditSurveyLocation(jsonObject);

                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        if (response.body() != null) {

                            JsonObject surveyedResponseVo = response.body();
                            if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                                Toast.makeText(EditPoleAttributesRdss.this,
                                        surveyedResponseVo.get("ERR_MSG").toString(),
                                        Toast.LENGTH_SHORT).show();
                                getOnBackPressedDispatcher().onBackPressed();
                            } else {

                                Toast.makeText(EditPoleAttributesRdss.this,
                                        surveyedResponseVo.get("ERR_MSG").toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(EditPoleAttributesRdss.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(EditPoleAttributesRdss.this, "Try Again " + t.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            }

        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isSelect(Spinner sp) {
        return sp.getSelectedItem().toString().equalsIgnoreCase("Select");
    }

    private boolean validateFeeders(Spinner countSpinner, TextView[] ssNames, TextView[] feederNos, String label) {
        int count;
        try {
            count = Integer.parseInt(countSpinner.getSelectedItem().toString());
        } catch (NumberFormatException e) {
            count = 0;
        }

        for (int i = 0; i < count; i++) {
            if (ssNames[i].getText().toString().trim().isEmpty()) {
                toast("Please Select " + label + " SS Name " + (i + 1));
                return false;
            }
            if (feederNos[i].getText().toString().trim().isEmpty()) {
                toast("Please Select " + label + " Feeder Name " + (i + 1));
                return false;
            }
        }
        return true;
    }

//    private List<FeedersData> getFeeders(String countSpinner, TextView[] ssNames, TextView[] feederNos, String voltage, Spinner[] spinner,Spinner[] phase,Spinner[] capacity,EditText[] dtr_code,EditText[] dtr_loc_name,EditText[] remarks) {
//        List<FeedersData> list = new ArrayList<>();
//        int count;
//        try {
//            count = Integer.parseInt(countSpinner);
//        } catch (NumberFormatException e) {
//            count = 0;
//        }
//
//        for (int i = 0; i < count; i++) {
//            String[] ssParts = ssNames[i].getText().toString().split("~");
//            String[] fdrParts = feederNos[i].getText().toString().split("~");
//
//            FeedersData data = new FeedersData();
//            data.setVoltage_level(voltage);
//            data.setFDR_INDEX(i + 1);
//          if (voltage.equalsIgnoreCase("33KV") || voltage.equalsIgnoreCase("11KV")){
//              data.setSS_NAME(ssParts.length > 0 ? ssParts[0] : "");
//              data.setSS_ID(ssParts.length > 1 ? ssParts[1] : "");
//              data.setFeeder_name(fdrParts.length > 0 ? fdrParts[0] : "");
//              data.setFeeder_id(fdrParts.length > 1 ? fdrParts[1] : "");
//
//
//              if (voltage.equalsIgnoreCase("33KV")) {
//                  String cableSize = spinner[i].getSelectedItem().toString();
//
//                  String numericValue = cableSize.replaceAll("[^0-9]", "");
//
//                  data.setCABLE_SIZE(numericValue + " SQMM");
//              }
//              if (voltage.equalsIgnoreCase("11KV")) {
//                  String cableSize = spinner[i].getSelectedItem().toString();
//
//                  String numericValue = cableSize.replaceAll("[^0-9]", "");
//
//                  data.setCABLE_SIZE(numericValue + " SQMM");
//              }
//          }else{
//              data.setDTR_PHASE(phase[i].getSelectedItem().toString());
//              data.setDTR_CAPACITY(capacity[i].getSelectedItem().toString());
//              data.setDTR_CODE(dtr_code[i].getText().toString().trim());
//              data.setDTR_LOCATION(dtr_loc_name[i].getText().toString().trim());
//
//          }
//          data.setREMARKS(remarks[i].getText().toString().trim());
//
//
//            list.add(data);
//        }
//
//        return list;
//    }



    private void initViews() {
        for (int i = 0; i < MAX_FEEDERS; i++) {
            int idx = i + 1;
            layout11KV[i] = findViewById(getResId("new_layout_11kv_" + idx));
            layout33KV[i] = findViewById(getResId("new_layout_33kv_" + idx));

            ssName11KV[i] = findViewById(getResId("new_sp_sub_station_name_11_feeder_" + idx));
            feederNo11KV[i] = findViewById(getResId("new_et_feeder_no_" + idx + "_11kv"));

            ssName33KV[i] = findViewById(getResId("new_sp_sub_station_name_33_feeder_" + idx));
            feederNo33KV[i] = findViewById(getResId("new_et_feeder_no_" + idx + "_33kv"));
        }

        for (int i = 0; i < 10; i++) {
            int idx = i + 1;
            layout_dtr_details_lt[i] = findViewById(getResId("layout_dtr_details_lt_" + idx));
            sp_dtr_capacity_lt[i] = findViewById(getResId("sp_dtr_capacity_lt_" + idx));
            sp_dtr_phase_lt[i] = findViewById(getResId("sp_dtr_phase_lt_" + idx));
            et_dtr_str_code[i] = findViewById(getResId("et_dtr_str_code_" + idx));
            et_dtr_loc_name[i] = findViewById(getResId("et_dtr_loc_name_" + idx));
            sp_cable_size_lt[i] = findViewById(getResId("sp_cable_size_lt_" + idx));
            remarks_33kv[i] = findViewById(getResId("remarks_33kv_" + idx));
            remarks_11kv[i] = findViewById(getResId("remarks_11kv_" + idx));
            et_remarks_lt[i] = findViewById(getResId("et_remarks_lt_" + idx));
        }

        for (int i = 0; i < MAX_FEEDERS; i++) {
            no_of_cables_33kv[i] = findViewById(getResId("tv_cable_33kv_" + i));
            no_of_cables_11kv[i] = findViewById(getResId("tv_cable_11kv_" + i));
            no_of_cables_lt[i] = findViewById(getResId("tv_cable_lt_" + i));

        }

        for (int i = 0; i < 10; i++) {

            int idx = i + 1;

            newLayout33KV[i] =
                    findViewById(getResId("new_layout_33kv_" + idx));

            newLayout11KV[i] =
                    findViewById(getResId("new_layout_11kv_" + idx));
        }

        for (int i = 0; i < 10; i++) {
            int idx = i + 1;
            sp_cable_size_33kv[i] = findViewById(getResId("sp_cable_size_" + idx));
            sp_cable_size_11kv[i] = findViewById(getResId("sp_cable_size_" + idx +"_11kv"));

        }

        // Step 1: Initialize individual TextViews using findViewById
        //33KV
        TextView new_sp_sub_station_name_33_feeder_1 = findViewById(R.id.new_sp_sub_station_name_33_feeder_1);
        TextView new_sp_sub_station_name_33_feeder_2 = findViewById(R.id.new_sp_sub_station_name_33_feeder_2);
        TextView new_sp_sub_station_name_33_feeder_3 = findViewById(R.id.new_sp_sub_station_name_33_feeder_3);
        TextView new_sp_sub_station_name_33_feeder_4 = findViewById(R.id.new_sp_sub_station_name_33_feeder_4);
        TextView new_sp_sub_station_name_33_feeder_5 = findViewById(R.id.new_sp_sub_station_name_33_feeder_5);
        TextView new_sp_sub_station_name_33_feeder_6 = findViewById(R.id.new_sp_sub_station_name_33_feeder_6);
        TextView new_sp_sub_station_name_33_feeder_7 = findViewById(R.id.new_sp_sub_station_name_33_feeder_7);
        TextView new_sp_sub_station_name_33_feeder_8 = findViewById(R.id.new_sp_sub_station_name_33_feeder_8);
        TextView new_sp_sub_station_name_33_feeder_9 = findViewById(R.id.new_sp_sub_station_name_33_feeder_9);
        TextView new_sp_sub_station_name_33_feeder_10 = findViewById(R.id.new_sp_sub_station_name_33_feeder_10);

        TextView new_et_feeder_no_1_33kv = findViewById(R.id.new_et_feeder_no_1_33kv);
        TextView new_et_feeder_no_2_33kv = findViewById(R.id.new_et_feeder_no_2_33kv);
        TextView new_et_feeder_no_3_33kv = findViewById(R.id.new_et_feeder_no_3_33kv);
        TextView new_et_feeder_no_4_33kv = findViewById(R.id.new_et_feeder_no_4_33kv);
        TextView new_et_feeder_no_5_33kv = findViewById(R.id.new_et_feeder_no_5_33kv);
        TextView new_et_feeder_no_6_33kv = findViewById(R.id.new_et_feeder_no_6_33kv);
        TextView new_et_feeder_no_7_33kv = findViewById(R.id.new_et_feeder_no_7_33kv);
        TextView new_et_feeder_no_8_33kv = findViewById(R.id.new_et_feeder_no_8_33kv);
        TextView new_et_feeder_no_9_33kv = findViewById(R.id.new_et_feeder_no_9_33kv);
        TextView new_et_feeder_no_10_33kv = findViewById(R.id.new_et_feeder_no_10_33kv);

        lt_feeder = findViewById(R.id.lt_feeder);
        eleven_kv = findViewById(R.id.eleven_kv);
        thirty_kv = findViewById(R.id.thirty_kv);
        layout_no_of_feeders_33kv = findViewById(R.id.layout_no_of_feeders_33kv);
        layout_no_of_feeders_11kv = findViewById(R.id.layout_no_of_feeders_11kv);
        layout_no_of_feeders_lt = findViewById(R.id.layout_no_of_feeders_lt);
        layout_no_of_feeders_33kv_new = findViewById(R.id.layout_no_of_feeders_33kv_new);
        layout_no_of_feeders_11kv_new = findViewById(R.id.layout_no_of_feeders_11kv_new);
        layout_no_of_feeders_lt_new = findViewById(R.id.layout_no_of_feeders_lt_new);
        sp_no_of_33 = findViewById(R.id.sp_no_of_33);
        sp_no_of_11 = findViewById(R.id.sp_no_of_11);
        gi_pipe = findViewById(R.id.gi_pipe);
        dwc_pipe = findViewById(R.id.dwc_pipe);


        //11KV
        TextView new_sp_sub_station_name_11_feeder_1 = findViewById(R.id.new_sp_sub_station_name_11_feeder_1);
        TextView new_sp_sub_station_name_11_feeder_2 = findViewById(R.id.new_sp_sub_station_name_11_feeder_2);
        TextView new_sp_sub_station_name_11_feeder_3 = findViewById(R.id.new_sp_sub_station_name_11_feeder_3);
        TextView new_sp_sub_station_name_11_feeder_4 = findViewById(R.id.new_sp_sub_station_name_11_feeder_4);
        TextView new_sp_sub_station_name_11_feeder_5 = findViewById(R.id.new_sp_sub_station_name_11_feeder_5);
        TextView new_sp_sub_station_name_11_feeder_6 = findViewById(R.id.new_sp_sub_station_name_11_feeder_6);
        TextView new_sp_sub_station_name_11_feeder_7 = findViewById(R.id.new_sp_sub_station_name_11_feeder_7);
        TextView new_sp_sub_station_name_11_feeder_8 = findViewById(R.id.new_sp_sub_station_name_11_feeder_8);
        TextView new_sp_sub_station_name_11_feeder_9 = findViewById(R.id.new_sp_sub_station_name_11_feeder_9);
        TextView new_sp_sub_station_name_11_feeder_10 = findViewById(R.id.new_sp_sub_station_name_11_feeder_10);


        TextView new_et_feeder_no_1_11kv = findViewById(R.id.new_et_feeder_no_1_11kv);
        TextView new_et_feeder_no_2_11kv = findViewById(R.id.new_et_feeder_no_2_11kv);
        TextView new_et_feeder_no_3_11kv = findViewById(R.id.new_et_feeder_no_3_11kv);
        TextView new_et_feeder_no_4_11kv = findViewById(R.id.new_et_feeder_no_4_11kv);
        TextView new_et_feeder_no_5_11kv = findViewById(R.id.new_et_feeder_no_5_11kv);
        TextView new_et_feeder_no_6_11kv = findViewById(R.id.new_et_feeder_no_6_11kv);
        TextView new_et_feeder_no_7_11kv = findViewById(R.id.new_et_feeder_no_7_11kv);
        TextView new_et_feeder_no_8_11kv = findViewById(R.id.new_et_feeder_no_8_11kv);
        TextView new_et_feeder_no_9_11kv = findViewById(R.id.new_et_feeder_no_9_11kv);
        TextView new_et_feeder_no_10_11kv = findViewById(R.id.new_et_feeder_no_10_11kv);


        // Step 2: Put them into arrays
        subStationNames = new TextView[]{
                new_sp_sub_station_name_33_feeder_1,
                new_sp_sub_station_name_33_feeder_2,
                new_sp_sub_station_name_33_feeder_3,
                new_sp_sub_station_name_33_feeder_4,
                new_sp_sub_station_name_33_feeder_5,
                new_sp_sub_station_name_33_feeder_6,
                new_sp_sub_station_name_33_feeder_7,
                new_sp_sub_station_name_33_feeder_8,
                new_sp_sub_station_name_33_feeder_9,
                new_sp_sub_station_name_33_feeder_10,

        };

        subStation11KVNames = new TextView[]{
                new_sp_sub_station_name_11_feeder_1,
                new_sp_sub_station_name_11_feeder_2,
                new_sp_sub_station_name_11_feeder_3,
                new_sp_sub_station_name_11_feeder_4,
                new_sp_sub_station_name_11_feeder_5,
                new_sp_sub_station_name_11_feeder_6,
                new_sp_sub_station_name_11_feeder_7,
                new_sp_sub_station_name_11_feeder_8,
                new_sp_sub_station_name_11_feeder_9,
                new_sp_sub_station_name_11_feeder_10
        };

        feederNos = new TextView[]{
                new_et_feeder_no_1_33kv,
                new_et_feeder_no_2_33kv,
                new_et_feeder_no_3_33kv,
                new_et_feeder_no_4_33kv,
                new_et_feeder_no_5_33kv,
                new_et_feeder_no_6_33kv,
                new_et_feeder_no_7_33kv,
                new_et_feeder_no_8_33kv,
                new_et_feeder_no_9_33kv,
                new_et_feeder_no_10_33kv,

        };
        feeder11KVNos = new TextView[]{
                new_et_feeder_no_1_11kv,
                new_et_feeder_no_2_11kv,
                new_et_feeder_no_3_11kv,
                new_et_feeder_no_4_11kv,
                new_et_feeder_no_5_11kv,
                new_et_feeder_no_6_11kv,
                new_et_feeder_no_7_11kv,
                new_et_feeder_no_8_11kv,
                new_et_feeder_no_9_11kv,
                new_et_feeder_no_10_11kv
        };


        spWidth = findViewById(R.id.sp_width);
        spDepth = findViewById(R.id.sp_depth);
        spNo33 = findViewById(R.id.sp_no_of_33);
        spNo11 = findViewById(R.id.sp_no_of_11);
        spNoLT = findViewById(R.id.sp_no_of_lt);
        spUtilityType = findViewById(R.id.sp_utility_type_rdss);
        etUtility = findViewById(R.id.et_utility);
        chUtilities = findViewById(R.id.ch_utilities);
        et_location_remarks = findViewById(R.id.et_location_remarks);
        submit = findViewById(R.id.submitButton);
        sp_rmu_type_rdss = findViewById(R.id.sp_rmu_type_rdss);
        sp_location_type = findViewById(R.id.sp_location_type);
        layout_utility_type_rdss = findViewById(R.id.layout_utility_type_rdss);
        sp_dtr_capacity_rdss = findViewById(R.id.sp_dtr_capacity_rdss);
        et_ht_sc_rdss = findViewById(R.id.et_ht_sc_rdss);
        layout_rmu_type_rdss = findViewById(R.id.layout_rmu_type_rdss);
        layout_dtr_capacity_rdss = findViewById(R.id.layout_dtr_capacity_rdss);
        layout_ht_sc_no_rdss = findViewById(R.id.layout_ht_sc_no_rdss);
        pole_no = findViewById(R.id.pole_no);
        from_pole = findViewById(R.id.from_pole);
        layout_source_location = findViewById(R.id.layout_source_location);
        layout_pole_details = findViewById(R.id.layout_pole_details);
        //source_pole_layout = findViewById(R.id.source_pole_layout);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);

        spNo33.setOnItemSelectedListener(this);
        spNo11.setOnItemSelectedListener(this);
        spNoLT.setOnItemSelectedListener(this);
        spUtilityType.setOnItemSelectedListener(this);
        sp_location_type.setOnItemSelectedListener(this);
        checkbox_gi_pipes = findViewById(R.id.checkbox_gi_pipes);
        checkbox_dwc_pipes = findViewById(R.id.checkbox_dwc_pipes);
        sp_conductor_size_11 = findViewById(R.id.sp_conductor_size_11);
        sp_conductor_size_33 = findViewById(R.id.sp_conductor_size_33);

        close_layout.setOnClickListener(v -> getOnBackPressedDispatcher());
        back_layout.setOnClickListener(v -> getOnBackPressedDispatcher());


//OLD Code
//        layout_source_location.setOnClickListener(v -> {
//            PopupMenu popupMenu = new PopupMenu(this, from_pole);
//
//            String currentSelected = from_pole.getText().toString().trim();
//
//            Set<String> uniqueLocations = new LinkedHashSet<>();
//
//            // Add "0" always at the first position (if not same as current text)
//            if (!"0".equalsIgnoreCase(currentSelected)) {
//                uniqueLocations.add("0");
//            }
//            ArrayList<UGPostingModel> totalList = database.getUGPostingData();
//            ArrayList<UGPostingModel> currentList = new ArrayList<>();
//            for (int i = 0; i < totalList.size(); i++) {
//                if (totalList.get(i).getVOLTAGE().equalsIgnoreCase(AssetsHomeRdssUgCable.SELECTED_VOLTAGE)) {
//                    if (totalList.get(i).getSS_ID().equalsIgnoreCase(updatedModel.getSS_ID())) {
//                        currentList.add(totalList.get(i));
//                    }
//                }
//            }
//
//            // Add other locations except the currently selected one
//            if (!currentList.isEmpty()) {
//                for (int i = 0; i < currentList.size(); i++) {
//                    String location = currentList.get(i).getLOCATION_NO();
//                    if (!location.equalsIgnoreCase(currentSelected)) {
//                        uniqueLocations.add(location);
//                    }
//                }
//            }
//
//            // Add menu items
//            for (String location : uniqueLocations) {
//                popupMenu.getMenu().add(location);
//            }
//
//            popupMenu.setOnMenuItemClickListener(item -> {
//                from_pole.setText(item.getTitle());
//                return true;
//            });
//
//            popupMenu.show();
//        });

        layout_source_location.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(this, from_pole);

            String currentSelected = from_pole.getText().toString().trim();

            Set<String> uniqueLocations = new LinkedHashSet<>();

            // User selected/current lat lng
            double userLat = Double.parseDouble(updatedModel.getLON());
            double userLng = Double.parseDouble(updatedModel.getLAT());

            // Always add 0 first
            if (!"0".equalsIgnoreCase(currentSelected)) {
                uniqueLocations.add("0");
            }

            ArrayList<UGPostingModel> totalList = database.getUGPostingData();
            ArrayList<UGPostingModel> currentList = new ArrayList<>();

            for (int i = 0; i < totalList.size(); i++) {

                UGPostingModel model = totalList.get(i);


                if (model.getLOCATION_NO().equalsIgnoreCase(updatedModel.getLOCATION_NO())) {
                    continue;
                }

                // Voltage + SS filter
                if (model.getVOLTAGE().equalsIgnoreCase(AssetsHomeRdssUgCable.SELECTED_VOLTAGE)
                        && model.getSS_ID().equalsIgnoreCase(updatedModel.getSS_ID()) && model.getCREATED_BY().equalsIgnoreCase(updatedModel.getCREATED_BY())) {

                    try {

                        double dbLat = Double.parseDouble(model.getLON());
                        double dbLng = Double.parseDouble(model.getLAT());

                        float[] results = new float[1];

                        // Distance in meters
                        android.location.Location.distanceBetween(
                                userLat,
                                userLng,
                                dbLat,
                                dbLng,
                                results
                        );

                        float distanceInMeters = results[0];
                        System.out.println("Distance in Meters:" + distanceInMeters);

                        // Only within 100 meters
                        if (distanceInMeters <= 100) {
                            currentList.add(model);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Add locations except current selected
            for (int i = 0; i < currentList.size(); i++) {

                String location = currentList.get(i).getLOCATION_NO();

                if (!location.equalsIgnoreCase(currentSelected)) {
                    uniqueLocations.add(location);
                }
            }

            // Add menu items
            for (String location : uniqueLocations) {
                popupMenu.getMenu().add(location);
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                from_pole.setText(item.getTitle());
                return true;
            });

            popupMenu.show();
        });
    }


    private List<FeedersData> getFeeders(
            String countSpinner,
            TextView[] ssNames,
            TextView[] feederNos,
            String voltage,
            Spinner[] cableSizeSpinner,
            Spinner[] phase,
            Spinner[] capacity,
            EditText[] dtrCode,
            EditText[] dtrLocation,
            EditText[] remarks) {

        List<FeedersData> list = new ArrayList<>();

        int count;
        try {
            count = Integer.parseInt(countSpinner);
        } catch (NumberFormatException e) {
            count = 0;
        }

        for (int i = 0; i < count; i++) {

            FeedersData data = new FeedersData();
            data.setVoltage_level(voltage);
            data.setFDR_INDEX(i + 1);

            if ("33KV".equalsIgnoreCase(voltage)
                    || "11KV".equalsIgnoreCase(voltage)) {

                String[] ssParts =
                        ssNames[i].getText().toString().trim().split("~");

                String[] fdrParts =
                        feederNos[i].getText().toString().trim().split("~");

                data.setSS_NAME(ssParts.length > 0 ? ssParts[0] : "");
                data.setSS_ID(ssParts.length > 1 ? ssParts[1] : "");

                data.setFeeder_name(fdrParts.length > 0 ? fdrParts[0] : "");
                data.setFeeder_id(fdrParts.length > 1 ? fdrParts[1] : "");

                String cableSize =
                        cableSizeSpinner[i].getSelectedItem().toString();

                String numericValue =
                        cableSize.replaceAll("[^0-9]", "");

                data.setCABLE_SIZE(numericValue + " SQMM");

            } else if ("LT".equalsIgnoreCase(voltage)) {

                data.setDTR_PHASE(
                        phase[i].getSelectedItem().toString());

                data.setDTR_CAPACITY(
                        capacity[i].getSelectedItem().toString());

                data.setDTR_CODE(
                        dtrCode[i].getText().toString().trim());

                data.setDTR_LOCATION(
                        dtrLocation[i].getText().toString().trim());

                String cableSize =
                        cableSizeSpinner[i].getSelectedItem().toString();

                data.setCABLE_SIZE(cableSize);
            }

            data.setREMARKS(
                    remarks[i].getText().toString().trim());

            list.add(data);
        }

        return list;
    }

    private void open33KVSSNearByDialog(TextView edt_text, TextView tv_feeder_name, String number) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog_estimates);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT EHT SS !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


        payDialog.show();

        back_to_home.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = payDialog.findViewById(selectedId);
            String selectedRadioButton = radioButton.getText().toString();
            payDialog.dismiss();
            if (selectedRadioButton.equals(" Nearby EHT SS")) {
                SearchableSpinner searchableSpinner = new SearchableSpinner(EditPoleAttributesRdss.this);
                searchableSpinner.setWindowTitle("SELECT EHT SS");
                ArrayList<String> nearBy33KVList = new ArrayList<>();
                final HashMap<String, EHT_SS_MASTER_ALL> modelMap = new HashMap<>();

                for (EHT_SS_MASTER_ALL model : AssetsHomeRdssUgCable.ehtSsMasterAlls) {
                    String displayText = model.getEHT_SS_NAME() + "~" + model.getEHT_SS_CODE();
                    nearBy33KVList.add(displayText);
                    modelMap.put(displayText, model);
                }
                searchableSpinner.setSpinnerListItems(AssetsHomeRdssUgCable.nearBy33kvSSNames);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        edt_text.setText(selectedString);
                        int index = Integer.parseInt(number) - 1;
                        if (index >= 0 && index < selectedSSCode33.length) {
                            selectedSSCode33[index] = selectedString.split("~")[1];
                        }
                        // selectedSSCode33_1 = selectedString.split("~")[1];
                        EHT_SS_MASTER_ALL selectedModel = modelMap.get(selectedString);
                        if (!selectedString.isEmpty()) {
                            tv_feeder_name.setVisibility(View.VISIBLE);
                        }
                        if (selectedModel != null) {
                            String latitude = selectedModel.getLATITUDE();
                            String longitude = selectedModel.getLONGITUDE();
                            SELECTED_LATTITUDE_33KV = Double.parseDouble(latitude);
                            SELECTED_LONGITUDE_33KV = Double.parseDouble(longitude);
                            tv_feeder_name.setText("");


                        }
                    }
                });

            } else {
                SearchableSpinner searchableSpinner = new SearchableSpinner(EditPoleAttributesRdss.this);
                searchableSpinner.setWindowTitle("SELECT FEEDER");
                ArrayList<String> nearBy33KVList = new ArrayList<>();
                final HashMap<String, EHT_SS_MASTER_ALL> modelMap = new HashMap<>();

                for (EHT_SS_MASTER_ALL model : AssetsHomeRdssUgCable.ehtSsMasterAlls) {
                    String displayText = model.getEHT_SS_NAME() + "~" + model.getEHT_SS_CODE();
                    nearBy33KVList.add(displayText);
                    modelMap.put(displayText, model);
                }

                searchableSpinner.setSpinnerListItems(AssetsHomeRdssUgCable.total33kvSSNames);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        edt_text.setText(selectedString);
                        int index = Integer.parseInt(number) - 1;
                        if (index >= 0 && index < selectedSSCode33.length) {
                            selectedSSCode33[index] = selectedString.split("~")[1];
                        }
                        // selectedSSCode33_1 = selectedString.split("~")[1];
                        EHT_SS_MASTER_ALL selectedModel = modelMap.get(selectedString);
                        if (selectedModel != null) {
                            String latitude = selectedModel.getLATITUDE();
                            String longitude = selectedModel.getLONGITUDE();

                            SELECTED_LATTITUDE_33KV = Double.parseDouble(latitude);
                            SELECTED_LONGITUDE_33KV = Double.parseDouble(longitude);
                            tv_feeder_name.setText("");

                        }

                        if (!selectedString.isEmpty()) {
                            tv_feeder_name.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });


    }

    private void open11KVSSNearByDialog(TextView edt_text, TextView tv_feeder_name, String number) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog_estimates_11kv);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT 33/11KV SS !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" Nearby 33/11KV SS")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(EditPoleAttributesRdss.this);
                    searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                    ArrayList<String> nearBy11KVList = new ArrayList<>();
                    final HashMap<String, SS_MASTER_ALL> modelMap = new HashMap<>();

                    for (SS_MASTER_ALL model : AssetsHomeRdssUgCable.ssMasterAlls) {
                        String displayText = model.getSUB_STATION_NAME() + "~" + model.getSUB_STATION_ID();
                        nearBy11KVList.add(displayText);
                        modelMap.put(displayText, model);
                    }
                    searchableSpinner.setSpinnerListItems(AssetsHomeRdssUgCable.nearBy11kvSSNames);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            int index = Integer.parseInt(number) - 1;
                            if (index >= 0 && index < selectedSSCode11.length) {
                                selectedSSCode11[index] = selectedString.split("~")[1];
                            }
                            SS_MASTER_ALL selectedModel = modelMap.get(selectedString);
                            if (selectedModel != null) {
                                String latitude = selectedModel.getLATITUDE();
                                String longitude = selectedModel.getLONGITUDE();
                                SELECTED_LATTITUDE_11KV = Double.parseDouble(latitude);
                                SELECTED_LONGITUDE_11KV = Double.parseDouble(longitude);
                            }
                            if (!selectedString.isEmpty()) {
                                tv_feeder_name.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(EditPoleAttributesRdss.this);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> nearBy11KVList = new ArrayList<>();
                    final HashMap<String, SS_MASTER_ALL> modelMap = new HashMap<>();

                    for (SS_MASTER_ALL model : AssetsHomeRdssUgCable.ssMasterAlls) {
                        String displayText = model.getSUB_STATION_NAME() + "~" + model.getSUB_STATION_ID();
                        nearBy11KVList.add(displayText);
                        modelMap.put(displayText, model);
                    }
                    searchableSpinner.setSpinnerListItems(AssetsHomeRdssUgCable.total11kvSSNames);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            int index = Integer.parseInt(number) - 1;
                            if (index >= 0 && index < selectedSSCode11.length) {
                                selectedSSCode11[index] = selectedString.split("~")[1];
                            }

                            //selectedSSCode = selectedString.split("~")[1];
                            SS_MASTER_ALL selectedModel = modelMap.get(selectedString);
                            if (selectedModel != null) {
                                String latitude = selectedModel.getLATITUDE();
                                String longitude = selectedModel.getLONGITUDE();
                                SELECTED_LATTITUDE_11KV = Double.parseDouble(latitude);
                                SELECTED_LONGITUDE_11KV = Double.parseDouble(longitude);
                            }
                            if (!selectedString.isEmpty()) {
                                tv_feeder_name.setVisibility(View.VISIBLE);
                            }


                        }
                    });
                }
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });


    }

    private void open33KVFeederMasterDialogNew(TextView edt_text, String selectedSSCode) {
        ArrayList<String> filteredList = new ArrayList<>();
        HashMap<String, Integer> feederPoleMap = new HashMap<>();

        System.out.println("SS_CODE_SELECTED" + selectedSSCode);

        for (int i = 0; i < AssetsHomeRdssUgCable.feeders33KV.size(); i++) {
            if (selectedSSCode.equals(AssetsHomeRdssUgCable.feeders33KV.get(i).getEHT_SS_CODE())) {
                int polesCount = Integer.parseInt(AssetsHomeRdssUgCable.feeders33KV.get(i).getPOLES().trim());
                String feederInfo = AssetsHomeRdssUgCable.feeders33KV.get(i).getFEEDER_NAME_33KV() + "~" +
                        AssetsHomeRdssUgCable.feeders33KV.get(i).getFEEDER_CODE_33KV();

                filteredList.add(feederInfo);
                feederPoleMap.put(feederInfo, polesCount);
            }
        }

        if (filteredList.isEmpty()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("No Feeders Found!")
                    .setMessage("No feeders are available for selection.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            SearchableSpinner searchableSpinner = new SearchableSpinner(EditPoleAttributesRdss.this);
            searchableSpinner.setWindowTitle("SELECT FEEDER");

            searchableSpinner.setSpinnerListItems(filteredList);
            searchableSpinner.show();
            searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
                int polesCount = feederPoleMap.get(selectedString);
                edt_text.setText(selectedString);

//                if (polesCount == 0) {
//                    new androidx.appcompat.app.AlertDialog.Builder(UGCableProposedMap.this)
//                            .setTitle("Pay Attention!")
//                            .setMessage("Survey is not initiated for the feeder")
//                            .setPositiveButton("OK", (dialog, which) -> {
//                                edt_text.setText("");
//                                dialog.dismiss();
//                            })
//                            .show();
//                } else {
//                    edt_text.setText(selectedString);
//                }
            });
        }
    }

    private void open11KVFeederMasterDialogNew(TextView edt_text, String selectedSSCode) {
        ArrayList<String> filteredList = new ArrayList<>();
        HashMap<String, Integer> feederPoleMap = new HashMap<>();

        for (int i = 0; i < AssetsHomeRdssUgCable.feeders11KV.size(); i++) {
            if (selectedSSCode.equals(AssetsHomeRdssUgCable.feeders11KV.get(i).getSUB_STATION_ID())) {
                int polesCount = Integer.parseInt(AssetsHomeRdssUgCable.feeders11KV.get(i).getPOLES().trim());
                String feederInfo = AssetsHomeRdssUgCable.feeders11KV.get(i).getFEEDER_NAME() + "~" +
                        AssetsHomeRdssUgCable.feeders11KV.get(i).getFEEDER_ID();

                filteredList.add(feederInfo);
                feederPoleMap.put(feederInfo, polesCount);
            }
        }

        if (filteredList.isEmpty()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("No Feeders Found!")
                    .setMessage("No feeders are available for selection.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            SearchableSpinner searchableSpinner = new SearchableSpinner(EditPoleAttributesRdss.this);
            searchableSpinner.setWindowTitle("SELECT FEEDER");

            searchableSpinner.setSpinnerListItems(filteredList);
            searchableSpinner.show();
            searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
                int polesCount = feederPoleMap.get(selectedString);

                edt_text.setText(selectedString);

//                if (polesCount == 0) {
//                    new androidx.appcompat.app.AlertDialog.Builder(UGCableProposedMap.this)
//                            .setTitle("Pay Attention!")
//                            .setMessage("Survey is not initiated for the feeder")
//                            .setPositiveButton("OK", (dialog, which) -> {
//                                edt_text.setText("");
//                                dialog.dismiss();
//                            })
//                            .show();
//                } else {
//                    edt_text.setText(selectedString);
//                }
            });
        }
    }

    private int getResId(String name) {
        return getResources().getIdentifier(name, "id", getPackageName());
    }


    private void setupData() {
        if (updatedModel == null) return;

        setupSpinner(spWidth, getWidthList(updatedModel.getWIDTH()));
        setupSpinner(spDepth, getDepthList(updatedModel.getDEPTH()));
        setupSpinner(spNo33, getNumberList(updatedModel.getCABLE_33KV(), 10));
        setupSpinner(spNo11, getNumberList(updatedModel.getCABLE_11KV(), 10));
        setupSpinner(spNoLT, getNumberList(updatedModel.getCABLE_LT(), 5));

        pole_no.setText(updatedModel.getLOCATION_NO());
        from_pole.setText(updatedModel.getSOURCE_LOCATION());

        if (updatedModel.getUTILITY_TYPE() != null && !updatedModel.getUTILITY_TYPE().isEmpty()) {
            chUtilities.setChecked(true);
            setupSpinner(spUtilityType, getUtilityList(updatedModel.getUTILITY_TYPE()));
            etUtility.setText(updatedModel.getUTILITY());

            // manually show the layout since checkbox is checked before listener
            layout_utility_type_rdss.setVisibility(View.VISIBLE);

            chUtilities.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    layout_utility_type_rdss.setVisibility(View.VISIBLE);
                } else {
                    layout_utility_type_rdss.setVisibility(View.GONE);
                }
            });
        }

        if ("11KV".equalsIgnoreCase(AssetsHomeRdssUgCable.SELECTED_VOLTAGE)) {

            String cableSize = updatedModel.getCABLE_SIZE();
            List<String> cableSizeList = new ArrayList<>();

            if ("300 SQMM".equalsIgnoreCase(cableSize)) {

                cableSizeList.add("300 SQMM");
                cableSizeList.add("180 SQMM");

            } else if ("180 SQMM".equalsIgnoreCase(cableSize)) {

                cableSizeList.add("180 SQMM");
                cableSizeList.add("300 SQMM");
            }

            ArrayAdapter<String> adapterPoleConfig11kv =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_dropdown_item_1line,
                            cableSizeList
                    );

            sp_conductor_size_11.setAdapter(adapterPoleConfig11kv);
        }

        if (updatedModel.getPROTECTION_PIPE().equalsIgnoreCase("GI Pipe")) {
            gi_pipe.setSelected(true);
        }
        if (updatedModel.getPROTECTION_PIPE().equalsIgnoreCase("DWC Pipe")) {
            dwc_pipe.setSelected(true);
        }
        populateFeedersData(
                updatedModel.getFeedersData(),
                "33KV",
                ssName33KV,
                feederNo33KV,
                null,
                null,
                null,
                remarks_33kv
        );
        populateFeedersData(
                updatedModel.getFeedersData(),
                "11KV",
                ssName11KV,
                feederNo11KV,
                null,
                null,
                null,
                remarks_11kv
        );

        populateFeedersData(
                updatedModel.getFeedersData(),
                "LT",
                null,
                null,
                et_dtr_str_code,
                et_dtr_loc_name,
                sp_dtr_capacity_lt,
                et_remarks_lt
        );


//        populateFeeders(updatedModel.getFeedersData(), "33KV", ssName33KV, feederNo33KV, remarks_33kv);
//        populateFeeders(updatedModel.getFeedersData(), "11KV", ssName11KV, feederNo11KV, remarks_11kv);
//        populateFeedersLT(updatedModel.getFeedersData(), "LT", et_dtr_str_code, et_dtr_loc_name, et_remarks_lt);


    }

    private void populateFeedersData(
            List<FeedersData> feeders,
            String voltage,
            TextView[] ssViews,
            TextView[] feederViews,
            EditText[] dtrCodeViews,
            EditText[] dtrLocationViews,
            Spinner[] dtrCapacity,
            EditText[] remarksViews) {

        if (feeders == null) return;

        List<FeedersData> filtered = new ArrayList<>();

        for (FeedersData f : feeders) {
            if (voltage.equalsIgnoreCase(f.getVoltage_level())) {
                filtered.add(f);
            }
        }

        for (int i = 0; i < filtered.size() && i < MAX_FEEDERS; i++) {

            FeedersData f = filtered.get(i);

            // 33KV / 11KV
            if (ssViews != null && feederViews != null) {

                ssViews[i].setText(
                        (f.getSS_NAME() == null ? "" : f.getSS_NAME()) +
                                "~" +
                                (f.getSS_ID() == null ? "" : f.getSS_ID()));

                feederViews[i].setText(
                        (f.getFeeder_name() == null ? "" : f.getFeeder_name()) +
                                "~" +
                                (f.getFeeder_id() == null ? "" : f.getFeeder_id()));

                feederViews[i].setVisibility(View.VISIBLE);
            }

            // LT
            if (dtrCodeViews != null) {
                dtrCodeViews[i].setText(
                        f.getDTR_CODE() == null ? "" : f.getDTR_CODE());
            }

            if (dtrLocationViews != null) {
                dtrLocationViews[i].setText(
                        f.getDTR_LOCATION() == null ? "" : f.getDTR_LOCATION());
            }

            if (dtrCapacity != null) {
                ArrayList<String> list = new ArrayList<>();

                System.out.println("GET_DTR_CAPACITY----->"+ updatedModel.getDTR_CAPACITY());
                if (updatedModel.getDTR_CAPACITY().equalsIgnoreCase("315 KVA")) {
                    list.add("315 KVA");
                    list.add("500 KVA");
                } else if (updatedModel.getDTR_CAPACITY().equalsIgnoreCase("500 KVA")) {
                    list.add("500 KVA");
                    list.add("315 KVA");

                }else {
                    list.add("315 KVA");
                    list.add("500 KVA");
                }
                ArrayAdapter<String> adapterPoleConfig11kv =
                        new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_dropdown_item_1line,
                                list
                        );

                dtrCapacity[i].setAdapter(adapterPoleConfig11kv);

            }

            // Common Remarks
            if (remarksViews != null) {
                remarksViews[i].setText(
                        f.getREMARKS() == null ? "" : f.getREMARKS());


            }
        }
    }

//    private void populateFeeders(List<FeedersData> feeders, String voltage, TextView[] ssViews, TextView[] feederViews, EditText[] remarks) {
//        if (feeders == null) return;
//
//        List<FeedersData> filtered = new ArrayList<>();
//        for (FeedersData f : feeders) {
//            if (voltage.equalsIgnoreCase(f.getVoltage_level())) filtered.add(f);
//        }
//
//        for (int i = 0; i < filtered.size() && i < MAX_FEEDERS; i++) {
//            FeedersData f = filtered.get(i);
//            ssViews[i].setText(f.getSS_NAME() + "~" + f.getSS_ID());
//            feederViews[i].setText(f.getFeeder_name() + "~" + f.getFeeder_id());
//            feederViews[i].setVisibility(View.VISIBLE);
//
//            if (voltage.equalsIgnoreCase("33KV")) {
//                if (f.getVoltage_level().equalsIgnoreCase("33KV")) {
//                    remarks[i].setText(f.getREMARKS());
//                }
//
//            } else if (voltage.equalsIgnoreCase("11KV") && f.getVoltage_level().equalsIgnoreCase("11KV")) {
//                remarks[i].setText(f.getREMARKS());
//            }
//        }
//    }

//    private void populateFeedersLT(List<FeedersData> feeders, String voltage, EditText[] dtr_code, EditText[] dtr_loc_name, EditText[] remarks) {
//        if (feeders == null) return;
//
//        List<FeedersData> filtered = new ArrayList<>();
//        for (FeedersData f : feeders) {
//            if (voltage.equalsIgnoreCase(f.getVoltage_level())) filtered.add(f);
//        }
//
//        for (int i = 0; i < filtered.size() && i < MAX_FEEDERS; i++) {
//            FeedersData f = filtered.get(i);
//            dtr_code[i].setText(f.getDTR_CODE());
//            dtr_loc_name[i].setText(f.getDTR_LOCATION());
//            remarks[i].setText(f.getREMARKS());
//        }
//    }

    private List<String> getWidthList(String selected) {
        return Arrays.asList(selected.equals("0.6") ? new String[]{"0.6", "1"} : new String[]{"1", "0.6"});
    }

    private List<String> getDepthList(String selected) {
        List<String> all = Arrays.asList("0.9", "1", "1.25", "1.5");
        List<String> ordered = new ArrayList<>();
        if (all.contains(selected)) {
            ordered.add(selected);
            for (String d : all) if (!d.equals(selected)) ordered.add(d);
        }
        return ordered;
    }

    private List<String> getNumberList(String selected, int max) {
        List<String> list = new ArrayList<>(Collections.singletonList(selected));
        for (int i = 0; i <= max; i++) {
            String s = String.valueOf(i);
            if (!s.equals(selected)) list.add(s);
        }
        return list;
    }

    private List<String> getUtilityList(String selected) {
        List<String> all = Arrays.asList("None", "Water pipelines", "Sewage pipelines", "Gas pipelines",
                "Culvert", "NH crossing", "Railway crossing", "Iron ore pipelines", "Road crossing", "220kv/132KV cable crossings");
        List<String> ordered = new ArrayList<>(Collections.singletonList(selected));
        for (String s : all) if (!s.equalsIgnoreCase(selected)) ordered.add(s);
        return ordered;
    }

    private void setupSpinner(Spinner spinner, List<String> list) {
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sp_no_of_33)
            toggleLayouts(layout33KV, spNo33.getSelectedItem().toString());
        if (parent.getId() == R.id.sp_no_of_11)
            toggleLayouts(layout11KV, spNo11.getSelectedItem().toString());
        if (parent.getId() == R.id.sp_location_type) {
            String selectedItem = sp_location_type.getSelectedItem().toString();


            layout_rmu_type_rdss.setVisibility(View.GONE);
            layout_dtr_capacity_rdss.setVisibility(View.GONE);

            if (!selectedItem.equalsIgnoreCase("Select")) {
                if (selectedItem.equalsIgnoreCase("RMU")) {
                    layout_rmu_type_rdss.setVisibility(View.VISIBLE);
                    et_location_remarks.setVisibility(View.VISIBLE);
                    layout_dtr_capacity_rdss.setVisibility(View.GONE);
                    layout_ht_sc_no_rdss.setVisibility(View.GONE);

                } else if (selectedItem.equalsIgnoreCase("DTR")) {
                    layout_dtr_capacity_rdss.setVisibility(View.VISIBLE);
                    et_location_remarks.setVisibility(View.VISIBLE);
                    layout_rmu_type_rdss.setVisibility(View.GONE);
                    layout_ht_sc_no_rdss.setVisibility(View.GONE);
                } else if (selectedItem.equalsIgnoreCase("HT Service")) {
                    layout_dtr_capacity_rdss.setVisibility(View.GONE);
                    layout_rmu_type_rdss.setVisibility(View.GONE);
                    layout_ht_sc_no_rdss.setVisibility(View.VISIBLE);
                    et_location_remarks.setVisibility(View.VISIBLE);
                } else {
                    et_location_remarks.setVisibility(View.GONE);
                }
            }
        }

    }


    private void toggleLayouts(LinearLayout[] layouts, String countStr) {
        int count = Integer.parseInt(countStr);
        for (int i = 0; i < MAX_FEEDERS; i++) {
            layouts[i].setVisibility(i < count ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
