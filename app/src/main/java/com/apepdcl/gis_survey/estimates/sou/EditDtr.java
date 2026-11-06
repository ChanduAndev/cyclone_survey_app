package com.apepdcl.gis_survey.estimates.sou;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.DTRFeederMaster;
import com.apepdcl.gis_survey.estimates.models.DTRMakeMaster;
import com.apepdcl.gis_survey.estimates.models.DTR_HT_MaterialModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDtr extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LinearLayout layout_dtr_details, existing_main_lt_layout_11kv, loader_layout, layout_dtr_material,
            existing_dtr_layout_dtr_capacity, existing_dtr_layout_dtr_make_11kv, existing_dtr_particulars_11kv, layout_dtr_survey, proposed_layout_dtr_details_11kv, existing_dtr_layout_dtr_phase_11kv;
    NewModel DATA_MODEL;

    RelativeLayout iv_back, close_layout;
    Button btn_update;
    private RetrofitServiceEstimates retrofitService;
    EditText existing_dtr_dtr_location_name, existing_dtr_et_dtr_sl_no;
    ArcGISDatabaseEstimates databaseEstimates;
    TextView existing_dtr_structure_code_11kv, tv_dtr_phase, tv_dtr_capacity, tv_dtr_location_name, tv_dtr_make, tv_dtr_slno, tv_edit_dtr_details;
    TextView dtr_phase_1, dtr_capacity_1, dtr_location_1, dtr_make_1, dtr_sl_no_1;
    Spinner sp_dtr_apartment_11, sp_no_of_lt_circuits_11, sp_meter_available_11, existing_dtr_sp_dtr_phase_11, sp_dtr_material_11,
            existing_dtr_sp_dtr_capacity_phase1_11, existing_dtr_sp_dtr_capacity_phase2_11, sp_existing_dtr_et_dtr_make, sp_dtr_survey_11;

    private boolean isUpdating = false;

    AutoCompleteTextView actv_existing_dtr_make;

    String conductorSAPCode11KV = "", referenceCode = "", dtrSurveyCode = "",DATA_ID="";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dtr);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Objects.requireNonNull(getSupportActionBar()).hide();
        viewCasting();
        DATA_MODEL = (NewModel) getIntent().getSerializableExtra("DATA_MODEL");
        existing_dtr_sp_dtr_capacity_phase1_11.setOnItemSelectedListener(EditDtr.this);
        existing_dtr_sp_dtr_capacity_phase2_11.setOnItemSelectedListener(EditDtr.this);

        sp_dtr_survey_11.setOnItemSelectedListener(EditDtr.this);

        setupSurveySpinner();

        iv_back.setOnClickListener(v -> onBackPressed());
        close_layout.setOnClickListener(v -> onBackPressed());

        databaseEstimates = new ArcGISDatabaseEstimates(EditDtr.this);


        tv_edit_dtr_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_dtr_details.setVisibility(View.GONE);
//                tv_dtr_capacity.setText("");
//                tv_dtr_make.setText("");
//                tv_dtr_phase.setText("");
//                tv_dtr_slno.setText("");
//                tv_dtr_location_name.setText("");
                layout_dtr_details.setVisibility(View.VISIBLE);
                existing_dtr_particulars_11kv.setVisibility(View.VISIBLE);
                existing_dtr_layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
                List<DTRFeederMaster> data = databaseEstimates.getPhaseANDCapacityData(existing_dtr_structure_code_11kv.getText().toString().split("~")[0]);
                if (data != null && !data.isEmpty()) {
                    existing_dtr_dtr_location_name.setText(data.get(0).getDTR_LOCATION());
                } else {
                    //existing_dtr_dtr_location_name.setText("No data found");
                }
                //existing_dtr_dtr_location_name.setText(data.get(0).getDTR_LOCATION());

            }
        });


        setDTRPhaseAdapterExisting();

        setPreviousData();


        existing_dtr_et_dtr_sl_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                tv_dtr_slno.setText(text);

            }
        });


        existing_dtr_dtr_location_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (!text.toString().isEmpty()) {
                    tv_dtr_location_name.setText(text);
                }
            }
        });

        existing_dtr_structure_code_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchableSpinner searchableSpinner = new SearchableSpinner(EditDtr.this);
                searchableSpinner.setWindowTitle("SELECT DTR CODE");
                searchableSpinner.setSpinnerListItems(EstimatesHome.allDtrs);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        String wo_mapped = "";
                        String structureCode = "";
                        String dtr = selectedString.split("~")[0];
                        for (int i = 0; i < EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.size(); i++) {
                            structureCode = EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.get(i).getDTR_STRUCTURE_CODE().split("~")[0];
                            if (dtr.equalsIgnoreCase(structureCode)) {
                                wo_mapped = EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.get(i).getWO_MAPPED();
                                System.out.println("SELECTED_DTR_ID2" + wo_mapped);
                                System.out.println("SELECTED_DTR_ID3" + structureCode);
                            }
                        }
                        if (wo_mapped.equalsIgnoreCase("YES")) {
                            openAlertDialogForDTRProposed(structureCode);

                        } else {
                            existing_dtr_structure_code_11kv.setText(selectedString);
                            layout_dtr_details.setVisibility(View.VISIBLE);
                            proposed_layout_dtr_details_11kv.setVisibility(View.VISIBLE);
                            if (!existing_dtr_structure_code_11kv.getText().toString().isEmpty() &&
                                    existing_dtr_structure_code_11kv.getText().toString() != null) {
                                List<DTRFeederMaster> data = databaseEstimates.getPhaseANDCapacityData(existing_dtr_structure_code_11kv.getText().toString().split("~")[0]);


                                //25-02-2025 Chandu Changes
                                if (data.get(0).getPHASE().equalsIgnoreCase("3")) {
                                    tv_dtr_phase.setText("III");

                                } else if (data.get(0).getPHASE().equalsIgnoreCase("2")) {
                                    tv_dtr_phase.setText("II");

                                } else if (data.get(0).getPHASE().equalsIgnoreCase("1")) {
                                    tv_dtr_phase.setText("I");

                                }
                                System.out.println("DTR_MAKE:::::::"+data.get(0).getDTR_MAKE());
                                if (data.get(0).getDTR_MAKE()==null || data.get(0).getDTR_MAKE().isEmpty()){
                                    setDTRMakes();
                                }


                                if (!data.get(0).getCAPACITY().contains("KVA")) {
                                    tv_dtr_capacity.setText(data.get(0).getCAPACITY() + " " + "KVA");
                                }


                                tv_dtr_location_name.setText(data.get(0).getDTR_LOCATION());
                                tv_dtr_make.setText(data.get(0).getDTR_MAKE());
                                System.out.println("DTR_MAKE_11KV222-->" + data.get(0).getDTR_MAKE());
                                tv_dtr_slno.setText(data.get(0).getDTR_SLNO());
                                if (tv_dtr_slno.getText().toString().isEmpty()) {
                                    //setDTRMakes();
                                }

                                existing_dtr_dtr_location_name.setText(data.get(0).getDTR_LOCATION());


                            } else {
                                layout_dtr_details.setVisibility(View.GONE);
                            }

                        }


                    }
                });
            }
        });


        System.out.println("DATA_MODEL_PRINT" + DATA_MODEL.getDTR_SURVEY_TYPE());

        assert DATA_MODEL != null;
        System.out.println("DATA_MODEL_PRINT" + DATA_MODEL.getPOLE_CONFIG());
        System.out.println("DATA_MODEL_PRINT" + DATA_MODEL.getDTR_SURVEY_TYPE());
        existing_dtr_structure_code_11kv.setText(DATA_MODEL.getDTR_STRUCTURE_CODE() + "~" + DATA_MODEL.getDTR_LOCATION());

        System.out.println("DTR_SURVEY_TYPE:"+DATA_MODEL.getDTR_SURVEY_TYPE());
        if (DATA_MODEL.getPOLE_CONFIG().equalsIgnoreCase("DTR POLE")) {
            settingSurveyTypeData();
        }

        String structureText = existing_dtr_structure_code_11kv.getText().toString();
        String[] parts = structureText.split("~");

        if (parts.length > 0 && !parts[0].isEmpty()) {
            List<DTRFeederMaster> data = databaseEstimates.getPhaseANDCapacityData(parts[0]);


            if (data != null && !data.isEmpty()) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(data);
                Log.d("DTRData", "Fetched Data-->: " + json);

                DTRFeederMaster dtr = data.get(0);

                Log.d("DTRData", "PHASE: " + dtr.getPHASE() + ", CAPACITY: " + dtr.getCAPACITY() + ", MAKE: " + dtr.getDTR_MAKE());

                switch (dtr.getPHASE()) {
                    case "3":
                        tv_dtr_phase.setText("III");
                        break;
                    case "2":
                        tv_dtr_phase.setText("II");
                        break;
                    case "1":
                        tv_dtr_phase.setText("I");
                        break;
                }

                if (!dtr.getCAPACITY().toUpperCase().contains("KVA")) {
                    tv_dtr_capacity.setText(dtr.getCAPACITY() + " KVA");
                } else {
                    tv_dtr_capacity.setText(dtr.getCAPACITY());
                }

                tv_dtr_location_name.setText(dtr.getDTR_LOCATION());
                existing_dtr_dtr_location_name.setText(dtr.getDTR_LOCATION());
                tv_dtr_make.setText(dtr.getDTR_MAKE());
                System.out.println("DTR_MAKE_11KV111-->" + data.get(0).getDTR_MAKE());
                if (data.get(0).getDTR_MAKE() == null || data.get(0).getDTR_MAKE().trim().isEmpty()) {
                    setDTRMakes();
                }

                tv_dtr_slno.setText(dtr.getDTR_SLNO());
                existing_dtr_et_dtr_sl_no.setText(dtr.getDTR_SLNO());

            } else {
                Log.e("DTRData", "No DTR data found for structure code: " + parts[0]);
            }
        } else {
            Log.e("DTRData", "Invalid DTR structure format: " + structureText);
        }


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDTRParticulurs();
            }
        });

        setDTRMakes();
    }

    private void openAlertDialogForDTRProposed(String DTR_CODE) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Pay Attention!!");
        // builder.setMessage("The selected DTR has already been mapped." + " " + DTR_CODE);
        builder.setMessage("The selected DTR Code has already mapped against another Estimate");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", (dialog, which) -> {
            existing_dtr_structure_code_11kv.setText("");
            dialog.dismiss();


        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validateDTRParticulurs() {
        if (existing_dtr_layout_dtr_phase_11kv.getVisibility() == View.VISIBLE && existing_dtr_sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "DTR Phase cannot be empty", Toast.LENGTH_SHORT).show();
            return;

        }
        if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Select")){
            Toast.makeText(this, "Select DTR Survey Type", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedPhase = existing_dtr_sp_dtr_phase_11.getSelectedItem().toString();

        if (existing_dtr_layout_dtr_capacity.getVisibility() == View.VISIBLE && selectedPhase.equalsIgnoreCase("I")) {
            String capacityPhase1 = existing_dtr_sp_dtr_capacity_phase1_11.getSelectedItem().toString();
            if (capacityPhase1.equalsIgnoreCase("Select")) {
                Toast.makeText(this, "DTR Capacity cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (selectedPhase.equalsIgnoreCase("III") && tv_dtr_capacity.getText().toString().trim().isEmpty()) {
            String capacityPhase2 = existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString();
            if (capacityPhase2.equalsIgnoreCase("Select")) {
                Toast.makeText(this, "DTR Capacity cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        if (tv_dtr_make.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "DTR Make cannot be empty", Toast.LENGTH_SHORT).show();
            return;

        }
        if (tv_dtr_slno.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "DTR serial no: cannot be empty", Toast.LENGTH_SHORT).show();
            return;

        }
        if (tv_dtr_location_name.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "DTR Location cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (layout_dtr_material.getVisibility() == View.VISIBLE) {
            if (existing_dtr_sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                if (sp_dtr_material_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(this, "Please select DTR Material", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                if (sp_dtr_material_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(this, "Please select DTR Material", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (ConnectivityChecking.checkConnection(EditDtr.this)) {
            if (DATA_MODEL.getREF_ID() != null && !DATA_MODEL.getREF_ID().isEmpty()) {
                System.out.println("EXECUTING THE BODY OF ELSE");
                updateDTRDetails();
            }

        } else {
            NewModel model = new NewModel();
            model.setDTR_PHASE(tv_dtr_phase.getText().toString().trim());
            model.setDTR_CAPACITY(tv_dtr_capacity.getText().toString().trim());
            model.setDTR_LOCATION(tv_dtr_location_name.getText().toString().trim());
            String dtrSlNo = tv_dtr_slno.getText().toString().trim();
            //model.setDTR_SNO(dtrSlNo.isEmpty() ? sp_existing_dtr_et_dtr_make.getSelectedItem().toString() : dtrSlNo);
            model.setDTR_SNO(tv_dtr_slno.getText().toString().trim());
            model.setDTR_MAKE(tv_dtr_make.getText().toString().trim());
            model.setGIS_ID(DATA_MODEL.getGIS_ID());
            model.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
            model.setDTR_STRUCTURE_CODE(existing_dtr_structure_code_11kv.getText().toString().trim().split("~")[0]);
            model.setEST_NO(DATA_MODEL.getEST_NO());
            model.setPOLE_NO(DATA_MODEL.getPOLE_NO());
            boolean status;
            status = databaseEstimates.updateSurveyData(model);

            if (status) {
                openDialogForCompleteSuccess("DTR Particulars saved successfully");
                getOnBackPressedDispatcher().onBackPressed();
            }
        }


    }


    private void setupSurveySpinner() {
        ArrayAdapter<CharSequence> adapter;
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            adapter = ArrayAdapter.createFromResource(
                    this, R.array.dtr_survey_type_estimates, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(
                    this, R.array.dtr_survey_type, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_dtr_survey_11.setAdapter(adapter);
    }


    private void settingSurveyTypeData() {
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            List<String> dtr_survey_type = new ArrayList<>();
            if (DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Existing")) {
                dtr_survey_type.add("Existing");

//                sp_dtr_survey_11.setSelection(1, true);
            } else if (DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED")) {
                dtr_survey_type.add("Enhancement");
                // sp_dtr_survey_11.setSelection(2, true);
            } else {
                sp_dtr_survey_11.setSelection(0, true);
            }
            ArrayAdapter<String> adapterSurvey = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dtr_survey_type);
            sp_dtr_survey_11.setAdapter(adapterSurvey);


        } else {
            List<String> dtr_survey_type = new ArrayList<>();
            if (DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Existing")) {
                dtr_survey_type.add("Existing");

                //sp_dtr_survey_11.setSelection(1, true);
            } else if (DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed")) {
                dtr_survey_type.add("Proposed");

                //sp_dtr_survey_11.setSelection(2, true);
            } else if (DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED")) {
                dtr_survey_type.add("Enhancement");
                //sp_dtr_survey_11.setSelection(3, true);
            } else {
                sp_dtr_survey_11.setSelection(0, true);
            }
            ArrayAdapter<String> adapterSurvey = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dtr_survey_type);
            sp_dtr_survey_11.setAdapter(adapterSurvey);
        }


        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            if (DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Existing") || DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED")) {
                existing_main_lt_layout_11kv.setVisibility(View.VISIBLE);
            } else {
                existing_main_lt_layout_11kv.setVisibility(View.GONE);
            }
        } else {
            existing_main_lt_layout_11kv.setVisibility(View.VISIBLE);
        }

        if (!existing_dtr_structure_code_11kv.getText().toString().isEmpty()) {
            proposed_layout_dtr_details_11kv.setVisibility(View.VISIBLE);
        } else {
            proposed_layout_dtr_details_11kv.setVisibility(View.GONE);
        }
    }


    private void setDTRMakes() {

        List<DTRMakeMaster> model = databaseEstimates.getDTRMakeMaster();

        if (model != null && !model.isEmpty()) {

            existing_dtr_layout_dtr_make_11kv.setVisibility(View.VISIBLE);

            List<String> dtrMakeList = new ArrayList<>();

            for (DTRMakeMaster item : model) {

                if (item.getDTR_MAKE() != null &&
                        !item.getDTR_MAKE().trim().isEmpty()) {

                    dtrMakeList.add(item.getDTR_MAKE());
                }
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            EditDtr.this,
                            android.R.layout.simple_dropdown_item_1line,
                            dtrMakeList
                    );

            actv_existing_dtr_make.setAdapter(adapter);

            actv_existing_dtr_make.setThreshold(1);

            actv_existing_dtr_make.setOnItemClickListener((parent, view, position, id) -> {

                String selectedValue =
                        parent.getItemAtPosition(position).toString();

                tv_dtr_make.setText(selectedValue);
            });

        } else {

            existing_dtr_layout_dtr_make_11kv.setVisibility(View.GONE);
        }
    }

//    private void setDTRMakes() {
//        List<DTRMakeMaster> model = databaseEstimates.getDTRMakeMaster();
//       // System.out.println("DTR MAKE MASTER SIZE"+model.size());
//        if (!model.isEmpty()) {
//            existing_dtr_layout_dtr_make_11kv.setVisibility(View.VISIBLE);
//            DTRMakeMaster placeholder = new DTRMakeMaster();
//            placeholder.setDTR_MAKE("Select");
//            model.add(0, placeholder);
//            ArrayAdapter<DTRMakeMaster> adapter = new ArrayAdapter<>(EditDtr.this, android.R.layout.simple_dropdown_item_1line, model);
//            sp_existing_dtr_et_dtr_make.setAdapter(adapter);
//            sp_existing_dtr_et_dtr_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if (!sp_existing_dtr_et_dtr_make.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//                        tv_dtr_make.setText(sp_existing_dtr_et_dtr_make.getSelectedItem().toString());
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//
//        } else {
//            existing_dtr_layout_dtr_make_11kv.setVisibility(View.GONE);
//        }
//    }

    private void setDTRPhaseAdapterExisting() {
        List<String> dtr_phase = new ArrayList<>();
        dtr_phase.clear();

        dtr_phase.add("Select");
        dtr_phase.add("I");
        dtr_phase.add("III");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditDtr.this, android.R.layout.simple_dropdown_item_1line, dtr_phase);
        existing_dtr_sp_dtr_phase_11.setAdapter(adapter);
        existing_dtr_sp_dtr_phase_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPhase = parent.getSelectedItem().toString();

                if ("I".equalsIgnoreCase(selectedPhase)) {
                    existing_dtr_layout_dtr_capacity.setVisibility(View.VISIBLE);
                    existing_dtr_sp_dtr_capacity_phase1_11.setVisibility(View.VISIBLE);
                    existing_dtr_sp_dtr_capacity_phase2_11.setVisibility(View.GONE);
                    existing_dtr_sp_dtr_capacity_phase1_11.setSelection(0, true);
                    tv_dtr_phase.setText("I");
                    List<String> capacityList = databaseEstimates.getDTRCapacities("I");
                    capacityList.add(0, "Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditDtr.this, android.R.layout.simple_dropdown_item_1line, capacityList);
                    existing_dtr_sp_dtr_capacity_phase1_11.setAdapter(adapter);


                } else if ("III".equalsIgnoreCase(selectedPhase)) {
                    existing_dtr_layout_dtr_capacity.setVisibility(View.VISIBLE);
                    existing_dtr_sp_dtr_capacity_phase2_11.setVisibility(View.VISIBLE);
                    existing_dtr_sp_dtr_capacity_phase1_11.setVisibility(View.GONE);
                    existing_dtr_sp_dtr_capacity_phase2_11.setSelection(0, true);
                    tv_dtr_phase.setText("III");
                    List<String> capacityList = databaseEstimates.getDTRCapacities("III");
                    capacityList.add(0, "Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditDtr.this, android.R.layout.simple_dropdown_item_1line, capacityList);
                    existing_dtr_sp_dtr_capacity_phase2_11.setAdapter(adapter);
                } else {
                    existing_dtr_layout_dtr_capacity.setVisibility(View.GONE);
                    existing_dtr_sp_dtr_capacity_phase1_11.setVisibility(View.GONE);
                    existing_dtr_sp_dtr_capacity_phase2_11.setVisibility(View.GONE);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void viewCasting() {
        existing_dtr_structure_code_11kv = findViewById(R.id.existing_dtr_structure_code_11kv);
        sp_dtr_apartment_11 = findViewById(R.id.sp_dtr_apartment_11);
        sp_no_of_lt_circuits_11 = findViewById(R.id.sp_no_of_lt_circuits_11);
        sp_meter_available_11 = findViewById(R.id.sp_meter_available_11);
        layout_dtr_details = findViewById(R.id.layout_dtr_details);
        existing_main_lt_layout_11kv = findViewById(R.id.existing_main_lt_layout_11kv);
        tv_dtr_phase = findViewById(R.id.tv_dtr_phase);
        tv_dtr_capacity = findViewById(R.id.tv_dtr_capacity);
        tv_dtr_location_name = findViewById(R.id.tv_dtr_location_name);
        tv_dtr_make = findViewById(R.id.tv_dtr_make);
        tv_dtr_slno = findViewById(R.id.tv_dtr_slno);
        tv_edit_dtr_details = findViewById(R.id.tv_edit_dtr_details);
        existing_dtr_sp_dtr_phase_11 = findViewById(R.id.existing_dtr_sp_dtr_phase_11);
        existing_main_lt_layout_11kv = findViewById(R.id.existing_main_lt_layout_11kv);
        existing_dtr_layout_dtr_capacity = findViewById(R.id.existing_dtr_layout_dtr_capacity);
        existing_dtr_sp_dtr_capacity_phase1_11 = findViewById(R.id.existing_dtr_sp_dtr_capacity_phase1_11);
        existing_dtr_sp_dtr_capacity_phase2_11 = findViewById(R.id.existing_dtr_sp_dtr_capacity_phase2_11);
        sp_existing_dtr_et_dtr_make = findViewById(R.id.sp_existing_dtr_et_dtr_make);
        existing_dtr_layout_dtr_make_11kv = findViewById(R.id.existing_dtr_layout_dtr_make_11kv);
        existing_dtr_particulars_11kv = findViewById(R.id.existing_dtr_particulars_11kv);
        existing_dtr_dtr_location_name = findViewById(R.id.existing_dtr_dtr_location_name);
        existing_dtr_et_dtr_sl_no = findViewById(R.id.existing_dtr_et_dtr_sl_no);
        layout_dtr_survey = findViewById(R.id.layout_dtr_survey);
        sp_dtr_survey_11 = findViewById(R.id.sp_dtr_survey_11);
        proposed_layout_dtr_details_11kv = findViewById(R.id.proposed_layout_dtr_details_11kv);
        btn_update = findViewById(R.id.btn_update);
        loader_layout = findViewById(R.id.loader_layout);
        iv_back = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        existing_dtr_layout_dtr_phase_11kv = findViewById(R.id.existing_dtr_layout_dtr_phase_11kv);
        dtr_phase_1 = findViewById(R.id.dtr_phase_1);
        dtr_capacity_1 = findViewById(R.id.dtr_capacity_1);
        dtr_sl_no_1 = findViewById(R.id.dtr_sl_no_1);
        dtr_make_1 = findViewById(R.id.dtr_make_1);
        dtr_location_1 = findViewById(R.id.dtr_location_1);
        layout_dtr_material = findViewById(R.id.layout_dtr_material);
        sp_dtr_material_11 = findViewById(R.id.sp_dtr_material_11);
        actv_existing_dtr_make = findViewById(R.id.actv_existing_dtr_make);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.existing_dtr_sp_dtr_capacity_phase1_11:
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                    if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                        if (!existing_dtr_sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            tv_dtr_capacity.setText(existing_dtr_sp_dtr_capacity_phase1_11.getSelectedItem().toString().trim());
                            String capacity = existing_dtr_sp_dtr_capacity_phase1_11.getSelectedItem().toString();
                            capacity = capacity.replaceAll("(?i)kva", "").trim();  // removes kVA or KVA in any case
                            System.out.println("CAPACITY--->" + capacity);
                            loadDTRMateeial(capacity);
                        }
                    }
                } else {
                    if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed") || sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                        if (!existing_dtr_sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            tv_dtr_capacity.setText(existing_dtr_sp_dtr_capacity_phase1_11.getSelectedItem().toString().trim());
                            String capacity = existing_dtr_sp_dtr_capacity_phase1_11.getSelectedItem().toString();
                            capacity = capacity.replaceAll("(?i)kva", "").trim();  // removes kVA or KVA in any case
                            System.out.println("CAPACITY--->" + capacity);
                            loadDTRMateeial(capacity);
                        }
                    }
                }

                break;

            case R.id.existing_dtr_sp_dtr_capacity_phase2_11:
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                    if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                        if (!existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            tv_dtr_capacity.setText(existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString().trim());
                            String capacity = existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString();
                            capacity = capacity.replaceAll("(?i)kva", "").trim();  // removes kVA or KVA in any case
                            System.out.println("CAPACITY--->" + capacity);
                            loadDTRMateeial(capacity);
                        }
                    }
                } else {
                    if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed") || sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                        if (!existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            tv_dtr_capacity.setText(existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString().trim());
                            String capacity = existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString();
                            capacity = capacity.replaceAll("(?i)kva", "").trim();  // removes kVA or KVA in any case
                            System.out.println("CAPACITY--->" + capacity);
                            loadDTRMateeial(capacity);
                        }
                    }
                }


//                if (!existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//                    tv_dtr_capacity.setText(existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString().trim());
//                    String capacity = existing_dtr_sp_dtr_capacity_phase2_11.getSelectedItem().toString();
//                    capacity = capacity.replaceAll("(?i)kva", "").trim();  // removes kVA or KVA in any case
//                    System.out.println("CAPACITY--->" + capacity);
//                    loadDTRMateeial(capacity);
//                }
                break;

            case R.id.sp_dtr_survey_11:
                if (!sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                        if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Existing") ||
                                sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                            existing_main_lt_layout_11kv.setVisibility(View.VISIBLE);
                        } else {
                            existing_main_lt_layout_11kv.setVisibility(View.GONE);
                            proposed_layout_dtr_details_11kv.setVisibility(View.GONE);
                        }
                    } else {
                        existing_main_lt_layout_11kv.setVisibility(View.VISIBLE);
                        if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {
                            dtr_capacity_1.setText("Proposed DTR Capacity*");
                            dtr_phase_1.setText("Proposed DTR Phase*");
                            dtr_make_1.setText("Proposed DTR Make*");
                            dtr_sl_no_1.setText("Proposed DTR Serial No*");
                            dtr_location_1.setText("Proposed DTR Location*");
                        }
                    }
                }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void updateDTRDetails() {
        loader_layout.setVisibility(View.VISIBLE);
        Map<String, String> data = new HashMap<>();
        data.put("DTR_PHASE", tv_dtr_phase.getText().toString().trim());
        data.put("DTR_CAPACITY", tv_dtr_capacity.getText().toString().trim());
        data.put("DTR_LOCATION", tv_dtr_location_name.getText().toString().trim());
        data.put("DTR_SNO", tv_dtr_slno.getText().toString().trim());
        data.put("DTR_MAKE", tv_dtr_make.getText().toString().trim());
        data.put("GIS_ID", DATA_MODEL.getGIS_ID());
        data.put("LOCATION_ID", DATA_MODEL.getLOCATION_ID());
        data.put("UPDATE_TYPE", "DTR");
        data.put("REF_ID", DATA_MODEL.getREF_ID());
        data.put("DTR_STRUCTURE_CODE", existing_dtr_structure_code_11kv.getText().toString().trim().split("~")[0]);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject); // Wrap object in array

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

        postingModel.setSAP_CODE(conductorSAPCode11KV);
        postingModel.setREF_ID(referenceCode);
        postingModel.setSURVEY_CODE(dtrSurveyCode);
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
                Toast.makeText(EditDtr.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDTRMateeial(String capacity) {
        System.out.println("TRIGGERED POINT 1:" + capacity);
        List<DTR_HT_MaterialModel> model = databaseEstimates.getDTR_HT("DT", capacity);
        if (!model.isEmpty()) {
            layout_dtr_material.setVisibility(View.VISIBLE);

            System.out.println("QUARY_SIZE-->" + model.size());
            DTR_HT_MaterialModel selectModel = new DTR_HT_MaterialModel();
//            selectModel.setMATERAIL_DESC("Select");
//            selectModel.setMATERIALCODE("");
//            model.add(0, selectModel);

            ArrayAdapter<DTR_HT_MaterialModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, model);
            sp_dtr_material_11.setAdapter(adapter);
            sp_dtr_material_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!sp_dtr_material_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        conductorSAPCode11KV = model.get(position).getMATERIALCODE();
                        referenceCode = model.get(position).getId();
                        dtrSurveyCode = model.get(position).getSURVEY_CODE();

                        DATA_ID=model.get(position).getGROUPCODE();
                          System.out.println("MATERIAL_TYPE" + "DTR MATERIAL" + conductorSAPCode11KV + " "+"DATA_ID:"+DATA_ID);



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

    private void setPreviousData() {
        List<String> dtr_phase = new ArrayList<>();
        if (DATA_MODEL.getDTR_PHASE().equalsIgnoreCase("I")) {

            dtr_phase.add("I");
            dtr_phase.add("III");
        } else {
            dtr_phase.add("III");
            dtr_phase.add("I");
        }
        ArrayAdapter<String> adapterSurvey = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dtr_phase);
        existing_dtr_sp_dtr_phase_11.setAdapter(adapterSurvey);

        List<String> dtr_capacity = new ArrayList<>();


        String phase = DATA_MODEL.getDTR_PHASE() != null ? DATA_MODEL.getDTR_PHASE().trim() : "";
        String capacity = DATA_MODEL.getDTR_CAPACITY() != null ? DATA_MODEL.getDTR_CAPACITY().trim() : "";

        // Phase I → only 16 KVA
        if (phase.equalsIgnoreCase("I") && capacity.equalsIgnoreCase("16 KVA")) {
            dtr_capacity.add("16 KVA");
            existing_dtr_sp_dtr_capacity_phase1_11.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapterCapacity1 = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, dtr_capacity);
            existing_dtr_sp_dtr_capacity_phase1_11.setAdapter(adapterCapacity1);

            // Phase III → all higher capacities
        } else if (phase.equalsIgnoreCase("III")) {
            dtr_capacity.add("25 KVA");
            dtr_capacity.add("40 KVA");
            dtr_capacity.add("63 KVA");
            dtr_capacity.add("100 KVA");
            dtr_capacity.add("160 KVA");
            dtr_capacity.add("315 KVA");
            dtr_capacity.add("500 KVA");

            // put the selected one first
            if (dtr_capacity.contains(capacity)) {
                dtr_capacity.remove(capacity);
                dtr_capacity.add(0, capacity);
            }
            existing_dtr_sp_dtr_capacity_phase2_11.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapterCapacity2 = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, dtr_capacity);
            existing_dtr_sp_dtr_capacity_phase2_11.setAdapter(adapterCapacity2);
        }

    }

    private void openDialogForCompleteSuccess(String msg) {
        new AlertDialog.Builder(EditDtr.this)
                .setTitle("Pay Attention!!")
                .setIcon(R.drawable.app_logo)
                .setMessage(msg)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditDtr.this, SurveyList.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .show();
    }


}
