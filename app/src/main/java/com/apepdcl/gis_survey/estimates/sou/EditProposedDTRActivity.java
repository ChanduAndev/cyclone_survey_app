package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_11_DATA_AVAILABLE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_33_DATA_AVAILABLE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_LT_DATA_AVAILABLE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.DTR_HT_MaterialModel;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProposedDTRActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LinearLayout layout_dtr_phase_11kv, layout_dtr_capacity, layout_dtr_material, layout_dtr_location_11kv, layout_dtr_apartment_11kv, layout_no_of_lt_circuits_11, layout_meter_11kv, layout_dtr_mounting, loader_layout;
    private Spinner sp_dtr_phase_11, sp_dtr_capacity_phase1_11, sp_dtr_capacity_phase2_11, sp_dtr_material_11, sp_dtr_apartment_11, sp_no_of_lt_circuits_11, sp_meter_available_11, sp_dtr_mounting_11;
    NewModel DATA_MODEL;
    EditText dtr_location_name;
    ArcGISDatabaseEstimates databaseEstimates;

    String conductorSAPCode11KVDTR = "", survey_code_dtr = "", referenceCode = "", DATA_ID = "";


    TextView btn_update;
    private RetrofitServiceEstimates retrofitService;
    RelativeLayout back_layout, close_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_proposed_dtractivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DATA_MODEL = (NewModel) getIntent().getSerializableExtra("DATA_MODEL");
        System.out.println("DTR_STRUCTURE_CODE-->" + DATA_MODEL.getDTR_STRUCTURE_CODE());
        databaseEstimates = new ArcGISDatabaseEstimates(this);
        viewCasting();
        spinnerClick();
        setPreviousData();
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDTRApi();
            }
        });
    }

    private void spinnerClick() {
        sp_dtr_capacity_phase2_11.setOnItemSelectedListener(this);
        sp_dtr_capacity_phase1_11.setOnItemSelectedListener(this);
        sp_dtr_apartment_11.setOnItemSelectedListener(this);
        sp_no_of_lt_circuits_11.setOnItemSelectedListener(this);
        sp_dtr_phase_11.setOnItemSelectedListener(this);
        sp_dtr_mounting_11.setOnItemSelectedListener(this);
    }

    private void setPreviousData() {
        List<String> dtr_phase = new ArrayList<>();
        if (DATA_MODEL.getDTR_PHASE().equalsIgnoreCase("I")) {

            dtr_phase.add("I");
            dtr_phase.add("III");
        } else {
            dtr_phase.add("III");
            dtr_phase.add("I");
        }
        SpinnerAdapter<String> adapterSurvey = new SpinnerAdapter<String>(this,  dtr_phase);
        sp_dtr_phase_11.setAdapter(adapterSurvey);

        List<String> dtr_capacity = new ArrayList<>();


        String phase = DATA_MODEL.getDTR_PHASE() != null ? DATA_MODEL.getDTR_PHASE().trim() : "";
        String capacity = DATA_MODEL.getDTR_CAPACITY() != null ? DATA_MODEL.getDTR_CAPACITY().trim() : "";
        if ("I".equalsIgnoreCase(phase)) {
            List<String> capacityList = databaseEstimates.getDTRCapacities("I");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, capacityList);
            sp_dtr_capacity_phase1_11.setAdapter(adapter);
        } else if ("III".equalsIgnoreCase(phase)) {
            List<String> capacityList = databaseEstimates.getDTRCapacities("III");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, capacityList);
            sp_dtr_capacity_phase2_11.setAdapter(adapter);
        }


        // Phase I → only 16 KVA
//        if (phase.equalsIgnoreCase("I") && capacity.equalsIgnoreCase("16 KVA")) {
//            dtr_capacity.add("16 KVA");
//            sp_dtr_capacity_phase1_11.setVisibility(View.VISIBLE);
//
//            ArrayAdapter<String> adapterCapacity1 = new ArrayAdapter<>(
//                    this, android.R.layout.simple_dropdown_item_1line, dtr_capacity);
//            sp_dtr_capacity_phase1_11.setAdapter(adapterCapacity1);
//
//            // Phase III → all higher capacities
//        }
//        else if (phase.equalsIgnoreCase("III")) {
//            dtr_capacity.add("25 KVA");
//            dtr_capacity.add("40 KVA");
//            dtr_capacity.add("63 KVA");
//            dtr_capacity.add("100 KVA");
//            dtr_capacity.add("160 KVA");
//            dtr_capacity.add("315 KVA");
//            dtr_capacity.add("500 KVA");
//
//            // put the selected one first
//            if (dtr_capacity.contains(capacity)) {
//                dtr_capacity.remove(capacity);
//                dtr_capacity.add(0, capacity);
//            }
//            sp_dtr_capacity_phase2_11.setVisibility(View.VISIBLE);
//            ArrayAdapter<String> adapterCapacity2 = new ArrayAdapter<>(
//                    this, android.R.layout.simple_dropdown_item_1line, dtr_capacity);
//            sp_dtr_capacity_phase2_11.setAdapter(adapterCapacity2);
//        }

        List<String> LT_AVAILABLE = new ArrayList<>();
        if (DATA_MODEL.getLT_AVAILABLE().equalsIgnoreCase("Yes")) {
            LT_AVAILABLE.add("Yes");
            LT_AVAILABLE.add("No");

        } else {
            LT_AVAILABLE.add("No");
            LT_AVAILABLE.add("Yes");
        }
        SpinnerAdapter<String> adapterLT = new SpinnerAdapter<String>(this,  LT_AVAILABLE);
        sp_dtr_apartment_11.setAdapter(adapterLT);
        dtr_location_name.setText(DATA_MODEL.getDTR_LOCATION());

//        System.out.println("DTR_LOCATION-->" + DATA_MODEL.getDTR_LOCATION());
//        System.out.println("DTR_LOCATION-->" + DATA_MODEL.getDTR_CAPACITY());
//        System.out.println("DTR_LOCATION-->" + DATA_MODEL.getLT_AVAILABLE());
//        System.out.println("DTR_LOCATION-->" + DATA_MODEL.getDTR_PHASE());
//        System.out.println("DTR_LOCATION-->" + DATA_MODEL.getCOUNT_CKT());


        List<String> dtr_mounting = new ArrayList<>();
        String mounting = DATA_MODEL.getDTR_MOUNTING() != null ? DATA_MODEL.getDTR_MOUNTING().trim() : "";

        dtr_mounting.add("Pole Mounted");
        dtr_mounting.add("Plinth Mounted");
        dtr_mounting.add("Pillar Mounted");
        dtr_mounting.add("DP Structure");
        if (dtr_mounting.contains(mounting)) {
            dtr_mounting.remove(mounting);
            dtr_mounting.add(0, mounting);
        }

        SpinnerAdapter<String> adapterMounting = new SpinnerAdapter<String>(this,  dtr_mounting);
        sp_dtr_mounting_11.setAdapter(adapterMounting);

        if (DATA_MODEL.getLT_AVAILABLE().equalsIgnoreCase("No")) {
            sp_meter_available_11.setSelection(1, true);
        }

    }

    private void loadDTRMateeial(String capacity) {
        List<DTR_HT_MaterialModel> model = databaseEstimates.getDTR_HT("DT", capacity);
        if (!model.isEmpty()) {
            layout_dtr_material.setVisibility(View.VISIBLE);

            System.out.println("QUARY_FOR_DTR_HT" + model.size());
            DTR_HT_MaterialModel selectModel = new DTR_HT_MaterialModel();
//           // selectModel.setMATERAIL_DESC("Select");
//            selectModel.setMATERIALCODE("");
//            model.add(0, selectModel);

            SpinnerAdapter<DTR_HT_MaterialModel> adapter = new SpinnerAdapter<>(this,  model);
            sp_dtr_material_11.setAdapter(adapter);
            sp_dtr_material_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!sp_dtr_material_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        conductorSAPCode11KVDTR = model.get(position).getMATERIALCODE();
                        survey_code_dtr = model.get(position).getSURVEY_CODE();
                        referenceCode = model.get(position).getId();
                        DATA_ID = model.get(position).getGROUPCODE();
                        System.out.println("DATA_ID------->" + "DTR MATERIAL" + " " + "DATA_ID:" + DATA_ID);


                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_dtr_material.setVisibility(View.GONE);
        }
    }

    private void viewCasting() {
        layout_dtr_phase_11kv = findViewById(R.id.layout_dtr_phase_11kv);
        layout_dtr_capacity = findViewById(R.id.layout_dtr_capacity);
        layout_dtr_material = findViewById(R.id.layout_dtr_material);
        layout_dtr_location_11kv = findViewById(R.id.layout_dtr_location_11kv);
        layout_dtr_apartment_11kv = findViewById(R.id.layout_dtr_apartment_11kv);
        layout_no_of_lt_circuits_11 = findViewById(R.id.layout_no_of_lt_circuits_11);
        layout_meter_11kv = findViewById(R.id.layout_meter_11kv);
        layout_dtr_mounting = findViewById(R.id.layout_dtr_mounting);

        sp_dtr_phase_11 = findViewById(R.id.sp_dtr_phase_11);
        sp_dtr_capacity_phase1_11 = findViewById(R.id.sp_dtr_capacity_phase1_11);
        sp_dtr_capacity_phase2_11 = findViewById(R.id.sp_dtr_capacity_phase2_11);
        sp_dtr_material_11 = findViewById(R.id.sp_dtr_material_11);
        sp_dtr_apartment_11 = findViewById(R.id.sp_dtr_apartment_11);
        sp_no_of_lt_circuits_11 = findViewById(R.id.sp_no_of_lt_circuits_11);
        sp_meter_available_11 = findViewById(R.id.sp_meter_available_11);
        sp_dtr_mounting_11 = findViewById(R.id.sp_dtr_mounting_11);
        dtr_location_name = findViewById(R.id.dtr_location_name);
        btn_update = findViewById(R.id.btn_update);
        loader_layout = findViewById(R.id.loader_layout);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.sp_dtr_capacity_phase1_11:
                if (!sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String capacity = sp_dtr_capacity_phase1_11.getSelectedItem().toString();
                    capacity = capacity.replaceAll("(?i)kva", "").trim();  // removes kVA or KVA in any case
                    System.out.println("CAPACITY--->" + capacity);
                    loadDTRMateeial(capacity);
                    setNoOfLTCKT(sp_dtr_phase_11.getSelectedItem().toString(), sp_dtr_capacity_phase1_11.getSelectedItem().toString());
                }
                break;

            case R.id.sp_dtr_capacity_phase2_11:
                if (!sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String capacity = sp_dtr_capacity_phase2_11.getSelectedItem().toString();
                    capacity = capacity.replaceAll("(?i)kva", "").trim();  // removes kVA or KVA in any case
                    System.out.println("CAPACITY--->" + capacity);
                    loadDTRMateeial(capacity);
                    setNoOfLTCKT(sp_dtr_phase_11.getSelectedItem().toString(), sp_dtr_capacity_phase2_11.getSelectedItem().toString());
                }


                break;

            case R.id.sp_dtr_apartment_11:
                if (sp_dtr_apartment_11.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                    layout_no_of_lt_circuits_11.setVisibility(View.VISIBLE);
                    layout_meter_11kv.setVisibility(View.GONE);
                } else {
                    layout_meter_11kv.setVisibility(View.VISIBLE);
                    layout_no_of_lt_circuits_11.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_dtr_phase_11:
                if (!sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("I")) {
                        layout_dtr_capacity.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase1_11.setSelection(0, true);
                        sp_dtr_capacity_phase1_11.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase2_11.setVisibility(View.GONE);
                        List<String> capacityList = databaseEstimates.getDTRCapacities("I");

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, capacityList);
                        sp_dtr_capacity_phase1_11.setAdapter(adapter);

                    } else if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("III")) {
                        layout_dtr_capacity.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase2_11.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase2_11.setSelection(0, true);
                        sp_dtr_capacity_phase1_11.setVisibility(View.GONE);
                        List<String> capacityList = databaseEstimates.getDTRCapacities("III");
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, capacityList);
                        sp_dtr_capacity_phase2_11.setAdapter(adapter);
                    }
                }
                break;
            case R.id.sp_dtr_mounting_11:
                if (sp_dtr_mounting_11.getSelectedItem().toString().equalsIgnoreCase("Pole Mounted")) {
                    if (sp_dtr_capacity_phase2_11.getVisibility() == View.VISIBLE) {
                        if (sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("63 KVA") ||
                                sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("100 KVA") ||
                                sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("160 KVA") ||
                                sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("315 KVA") ||
                                sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("500 KVA")) {
                            showAlertForDTR("Pay Attention", "Pole mounting is not allowed from DTR capacity > 40 KVA.", sp_dtr_mounting_11);
                        }


                    }
                }
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void setNoOfLTCKT(String phase, String capacity) {

        System.out.println("PHASE-->" + phase);
        System.out.println("CAPACITY-->" + capacity);

        List<String> baseList = new ArrayList<>();
        List<String> count_lt_ckt = new ArrayList<>();

        int maxCount = 1; // default

        // 🔹 Determine maxCount
        if (DATA_MODEL.getLINE_TYPE().equalsIgnoreCase("OverHead")) {
            if (phase.equalsIgnoreCase("I")) {
                maxCount = 1;
            } else if (phase.equalsIgnoreCase("III")) {
                if (capacity.equalsIgnoreCase("160 KVA")
                        || capacity.equalsIgnoreCase("315 KVA")
                        || capacity.equalsIgnoreCase("500 KVA")) {
                    maxCount = 3;
                } else {
                    maxCount = 2;
                }
            }
        } else {
            maxCount = 12;
        }

        // 🔴 Special case: Phase I + 16 KVA → Only Single Feeding
        if (phase.equalsIgnoreCase("I")
                && capacity.equalsIgnoreCase("16 KVA")) {

            count_lt_ckt.add("1 (Single Feeding)");

            SpinnerAdapter<String> adapter = new SpinnerAdapter<>(
                    EditProposedDTRActivity.this,
                    
                    count_lt_ckt
            );
            sp_no_of_lt_circuits_11.setAdapter(adapter);
            return;
        }

        // 🔹 Prepare base list (no duplicates)
        for (int i = 1; i <= maxCount; i++) {
            switch (i) {
                case 1:
                    baseList.add("1 (Single Feeding)");
                    break;
                case 2:
                    baseList.add("2 (Double Feeding A & B)");
                    break;
                case 3:
                    baseList.add("3 (Three Feedings A, B, C)");
                    break;
                default:
                    baseList.add(String.valueOf(i));
                    break;
            }
        }

        // 🔹 Reorder based on COUNT_CKT
        int countCkt = DATA_MODEL.getCOUNT_CKT();

        if (countCkt >= 1 && countCkt <= 3) {

            String priorityItem = null;

            switch (countCkt) {
                case 1:
                    priorityItem = "1 (Single Feeding)";
                    break;
                case 2:
                    priorityItem = "2 (Double Feeding A & B)";
                    break;
                case 3:
                    priorityItem = "3 (Three Feedings A, B, C)";
                    break;
            }

            // Add priority first (if exists)
            if (priorityItem != null && baseList.contains(priorityItem)) {
                count_lt_ckt.add(priorityItem);
            }

            // Add remaining items
            for (String item : baseList) {
                if (!count_lt_ckt.contains(item)) {
                    count_lt_ckt.add(item);
                }
            }

        } else {
            // Default order
            count_lt_ckt.addAll(baseList);
        }

        // 🔹 Set adapter
        SpinnerAdapter<String> adapter = new SpinnerAdapter<>(
                EditProposedDTRActivity.this,
                
                count_lt_ckt
        );

        sp_no_of_lt_circuits_11.setAdapter(adapter);
    }


    private void showAlertForDTR(String title, String message, Spinner spinner) {
        new android.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    spinner.setSelection(0, true);

                    dialog.dismiss();
                })
                .show();
    }

    private void updateDTRApi() {
        if (layout_meter_11kv.getVisibility() == View.VISIBLE && sp_meter_available_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select Is meter connected to DTR", Toast.LENGTH_SHORT).show();
            return;
        } else if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("I") && sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select DTR Phase", Toast.LENGTH_SHORT).show();
            return;
        } else if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("III") && sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select DTR Phase", Toast.LENGTH_SHORT).show();
            return;
        } else if (sp_no_of_lt_circuits_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select number of LT CKT count", Toast.LENGTH_SHORT).show();
        } else {
            updateDTRDetails();
        }

    }

    private void updateDTRMaterial() {
        MaterialPostingModel postingModel = new MaterialPostingModel();
        postingModel.setAPP_VERSION(MobileConstants.APP_VERSION);
        postingModel.setAPP_VERSION2(getResources().getString(R.string.displayVersion));
        postingModel.setCOUNT("1");  // directly use "1"
        postingModel.setDATA_ID(DATA_ID);
        postingModel.setESTIMATE_NO(DATA_MODEL.getEST_NO());
        postingModel.setGIS_ID(DATA_MODEL.getGIS_ID());
        postingModel.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
        postingModel.setLOCATION_NO(DATA_MODEL.getPOLE_NO());
        if (postingModel != null && sp_dtr_material_11 != null && sp_dtr_material_11.getSelectedItem() != null) {
            postingModel.setMATERIAL_DESC(sp_dtr_material_11.getSelectedItem().toString());
        }

        postingModel.setSAP_CODE(conductorSAPCode11KVDTR);
        postingModel.setREF_ID(referenceCode);
        postingModel.setSURVEY_CODE(survey_code_dtr);
        postingModel.setTYPE("TRANSMITTED");
        postingModel.setVOLTAGE_LEVEL(DATA_MODEL.getVOLTAGE_LEVEL());
        postingModel.setMOD_TYPE("EDIT_DTA");

        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(gson.toJsonTree(postingModel));

        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates
                .getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN())
                .create(RetrofitServiceEstimates.class);

        Call<JsonObject> responseVoCall = retrofitService.postingMaterialTransmitted(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();

                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
//                        Toast.makeText(EditPoleDetailsActivity.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(EditPoleDetailsActivity.this, SurveyList.class);
//                        startActivity(intent);
//                        finish();
                        openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString());
                        // getOnBackPressedDispatcher().onBackPressed();
                    } else {
                        openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout.setVisibility(View.GONE);
                Toast.makeText(EditProposedDTRActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialogForCompleteSuccess(String msg) {
        new AlertDialog.Builder(EditProposedDTRActivity.this)
                .setTitle("Pay Attention!!")
                .setIcon(R.drawable.app_logo)
                .setMessage(msg)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditProposedDTRActivity.this, SurveyList.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .show();
    }

    private void updateDTRDetails() {

        loader_layout.setVisibility(View.VISIBLE);
        Map<String, String> data = new HashMap<>();
        data.put("DTR_PHASE", sp_dtr_phase_11.getSelectedItem().toString().trim());
        data.put("DTR_MOUNTING", sp_dtr_mounting_11.getSelectedItem().toString().trim());
        data.put("LT_AVAILABLE", sp_dtr_apartment_11.getSelectedItem().toString().trim());
        if (sp_dtr_capacity_phase1_11.getVisibility() == View.VISIBLE) {
            data.put("DTR_CAPACITY", sp_dtr_capacity_phase1_11.getSelectedItem().toString().trim());
        } else if (sp_dtr_capacity_phase2_11.getVisibility() == View.VISIBLE) {
            data.put("DTR_CAPACITY", sp_dtr_capacity_phase2_11.getSelectedItem().toString().trim());
        }
        data.put("DTR_LOCATION", dtr_location_name.getText().toString().trim());
        data.put("GIS_ID", DATA_MODEL.getGIS_ID());
        data.put("LOCATION_ID", DATA_MODEL.getLOCATION_ID());
        data.put("UPDATE_TYPE", "DTR");
        data.put("REF_ID", DATA_MODEL.getREF_ID());
        data.put("DTR_STRUCTURE_CODE", DATA_MODEL.getDTR_STRUCTURE_CODE());
        String DTR_AVAILABLE="";
        DTR_AVAILABLE = sp_dtr_apartment_11.getSelectedItem().toString().trim();
        if ("YES".equalsIgnoreCase(DTR_AVAILABLE)){
            String selected = sp_no_of_lt_circuits_11.getSelectedItem().toString();
            int count = Integer.parseInt(selected.split(" ")[0]);
            data.put("COUNT_CKT", String.valueOf(count));
        }else {
            data.put("COUNT_CKT", "0");
        }
//        if (layout_no_of_lt_circuits_11.getVisibility() == View.VISIBLE) {
//            String selected = sp_no_of_lt_circuits_11.getSelectedItem().toString();
//            int count = Integer.parseInt(selected.split(" ")[0]);
//            data.put("COUNT_CKT", String.valueOf(count));
//            //data.put("COUNT_CKT", sp_no_of_lt_circuits_11.getSelectedItem().toString().trim());
//        }


        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject); // Wrap object in array
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        // Retrofit call
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.updatedDtrDetails(jsonArray);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            updateDTRMaterial();

//                            Toast.makeText(EditDtr.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(EditDtr.this, SurveyList.class);
//                            startActivity(intent);
//                            finish();
                        } else {
                            openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString());
                        }

                    }

                } else {
                    Log.e("Retrofit", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout.setVisibility(View.GONE);
                Log.e("Retrofit", "Failure: " + t.getMessage());
            }
        });
    }
}