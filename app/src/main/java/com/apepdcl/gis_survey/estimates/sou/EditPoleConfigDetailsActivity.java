package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.LINE_EXT_DTR_LATTITUDE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.LINE_EXT_DTR_LOCATION_NO;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.LINE_EXT_DTR_LONGITUDE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.LINE_EXT_DTR_STRUCTURE_CODE;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.models.DTRFeederMaster;
import com.apepdcl.gis_survey.estimates.models.DTR_HT_MaterialModel;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.List;

public class EditPoleConfigDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String voltage_level = "", EST_NO = "", GIS_ID = "", EST_NATURE = "", PURPOSE = "", wo_no = "",
            REG_COUNT = "", BTN_VALUE = "", from = "", btnClick = "", TYPE = "", MAX_LENGTH = "", POLE_CONFIG = "";
    private boolean IS_EDIT = false;
    NewModel DATA_MODEL;
    private Spinner sp_pole_config, sp_dtr_capacity_phase1_11, sp_dtr_capacity_phase2_11, sp_dtr_survey_11, sp_dtr_phase_11, sp_dtr_mounting_11, sp_dtr_apartment_11_existing,
            sp_dtr_material_11, sp_dtr_apartment_11, sp_no_of_lt_circuits_11, sp_dtr_shifted_11, existing_dtr_sp_dtr_phase_11, existing_dtr_sp_dtr_capacity_phase1_11, existing_dtr_sp_dtr_capacity_phase2_11;

    private LinearLayout layout_dtr_survey;
    private LinearLayout proposed_layout_dtr_details_11kv;
    private LinearLayout layout_dtr_phase_11kv;
    private LinearLayout layout_dtr_capacity;
    private LinearLayout existing_dtr_particulars_11kv;
    private LinearLayout existing_dtr_layout_dtr_make_11kv;
    private LinearLayout layout_dtr_material;
    private LinearLayout layout_dtr_location_11kv;
    private LinearLayout layout_dtr_apartment_11kv;
    private LinearLayout layout_no_of_lt_circuits_11;
    private LinearLayout layout_meter_11kv;
    private LinearLayout layout_dtr_make_11kv;
    private LinearLayout sap_33_material_layout_11_meter;
    private LinearLayout existing_layout_dtr_details_11kv;
    private LinearLayout existing_main_lt_layout_11kv;
    private LinearLayout layout_dtr_sl_no_11kv;
    private LinearLayout layout_dtr_phase_desc_11kv;
    private LinearLayout layout_dtr_mounting;
    private LinearLayout layout_dtr_details;
    private LinearLayout existing_dtr_details_11kv;
    private LinearLayout existing_layout_dtr_make_11kv;
    private LinearLayout existing_dtr_layout_dtr_capacity;
    private LinearLayout layout_dtr_shifted;
    private LinearLayout layout_dtr_shifted_remark;
    private LinearLayout existing_dtr_layout_dtr_sl_no_11kv;


    List<String> dtr_phase = new ArrayList<>();


    private ArcGISDatabaseEstimates databaseEstimates;
    private TextView existing_dtr_structure_code_11kv, tv_edit_dtr_details, existing_sp_dtr_phase_11;
    TextView tv_dtr_phase, tv_dtr_capacity, tv_dtr_slno, tv_dtr_make, tv_dtr_location_name;
    private String survey_code_dtr = "", DTA_ID = "", conductorSAPCode11KVDTR = "", isItApartmentDTR = "";
    SearchableSpinner searchableSpinner;
    EditText dtr_location_name, dtr_make_name, et_dtr_make, et_dtr_sl_no,
            existing_dtr_dtr_location_name, existing_dtr_et_dtr_make, existing_dtr_et_dtr_sl_no,
            et_service_released_11kv, et_service_released_33kv;

    boolean IS_DTR_EDIT = false;
    ArrayList<NewModel> list33, list11, listLT, allList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pole_configuration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseEstimates = new ArcGISDatabaseEstimates(this);
        searchableSpinner = new SearchableSpinner(this);
        list11 = new ArrayList<>();
        list33 = new ArrayList<>();
        listLT = new ArrayList<>();
        allList = new ArrayList<>();

        list33 = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
        list11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);

        listLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);
        allList = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);
        viewCasting();
        sp_pole_config.setOnItemSelectedListener(this);
        sp_dtr_survey_11.setOnItemSelectedListener(this);
        sp_dtr_phase_11.setOnItemSelectedListener(this);
        sp_dtr_material_11.setOnItemSelectedListener(this);
        sp_dtr_mounting_11.setOnItemSelectedListener(this);
        sp_dtr_apartment_11_existing.setOnItemSelectedListener(this);
        sp_dtr_capacity_phase2_11.setOnItemSelectedListener(this);
        sp_dtr_capacity_phase1_11.setOnItemSelectedListener(this);
        sp_dtr_apartment_11.setOnItemSelectedListener(this);
        sp_no_of_lt_circuits_11.setOnItemSelectedListener(this);
        sp_dtr_shifted_11.setOnItemSelectedListener(this);
        existing_dtr_sp_dtr_phase_11.setOnItemSelectedListener(this);
        existing_dtr_sp_dtr_capacity_phase1_11.setOnItemSelectedListener(this);
        existing_dtr_sp_dtr_capacity_phase2_11.setOnItemSelectedListener(this);


        voltage_level = getIntent().getStringExtra("BTN_VALUE");
        EST_NO = getIntent().getStringExtra("EST_NO");
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        EST_NATURE = getIntent().getStringExtra("EST_NATURE");
        PURPOSE = getIntent().getStringExtra("PURPOSE");
        wo_no = getIntent().getStringExtra("WORK_ORDER");
        REG_COUNT = getIntent().getStringExtra("REG_COUNT");
        BTN_VALUE = getIntent().getStringExtra("BTN_VALUE");
        from = getIntent().getStringExtra("FROM");
        btnClick = getIntent().getStringExtra("BTN_VALUE");
        TYPE = getIntent().getStringExtra("TYPE");
        MAX_LENGTH = getIntent().getStringExtra("MAX_LENGTH");
        IS_EDIT = getIntent().getBooleanExtra("IS_EDIT", false);
        if (IS_EDIT) {
            DATA_MODEL = (NewModel) getIntent().getSerializableExtra("DATA_MODEL");
        }
        assert DATA_MODEL != null;
        POLE_CONFIG = DATA_MODEL.getPOLE_CONFIG();
        System.out.println("POLE_CONFIG_EDIT_CONFIG" + POLE_CONFIG);
        setPoleConfiguration();


        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
            dtr_location_name.addTextChangedListener(new TextWatcher() {
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
        }

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

        existing_dtr_et_dtr_sl_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (!text.toString().isEmpty()) {
                    tv_dtr_slno.setText(text);
                }

            }
        });


//        String input = et_service_released_11kv.getText().toString();
//        if (input.matches("^[A-Z]{3}[0-9]{13}$")) {
//            // Valid input
//        } else {
//            et_service_released_11kv.setError("Invalid format. Use 3 capital letters followed by 13 digits.");
//        }


        et_dtr_sl_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (!text.toString().isEmpty()) {
                    tv_dtr_slno.setText(text);
                }

            }
        });


        //NEW DTR CHANGES BY CHANDU
        existing_dtr_structure_code_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchableSpinner.setWindowTitle("SELECT DTR CODE");
                updateSearchableSpinnerItems();
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        //System.out.println("SELECTED_DTR_ID" + EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.size());
                        //System.out.println("SELECTED_DTR_ID1" + selectedString);

                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER") &&
                                sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {
                            String wo_mapped = "";
                            String structureCode = "";
                            String dtr = selectedString.split("~")[0];
                            for (int i = 0; i < EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.size(); i++) {
                                structureCode = EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.get(i).getDTR_STRUCTURE_CODE().split("~")[0];
                                if (dtr.equalsIgnoreCase(structureCode)) {
                                    wo_mapped = EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.get(i).getWO_MAPPED();
                                    //System.out.println("SELECTED_DTR_ID2" + wo_mapped);
                                    //System.out.println("SELECTED_DTR_ID3" + structureCode);
                                }
                            }


                            if (wo_mapped.equalsIgnoreCase("YES")) {
                                openAlertDialogForDTRProposed(structureCode);
                                return;
                            } else {
                            }
                        }


                        existing_dtr_structure_code_11kv.setText(selectedString);

                        sp_dtr_phase_11.setSelection(0, true);
                        dtr_location_name.setText("");
                        sp_dtr_mounting_11.setSelection(0, true);
                        // setNoOfLTCKT();
                        sp_dtr_apartment_11_existing.setSelection(0, true);
                        //  setNoOfLTCKTEXT();
                        layout_dtr_make_11kv.setVisibility(View.GONE);
                        layout_dtr_location_11kv.setVisibility(View.GONE);
                        layout_dtr_sl_no_11kv.setVisibility(View.GONE);
                        layout_dtr_capacity.setVisibility(View.GONE);
                        IS_DTR_EDIT = false;
                        existing_dtr_particulars_11kv.setVisibility(View.GONE);
                        sp_dtr_apartment_11.setSelection(0, true);
                        boolean isMatched = false;
                        for (int i = 0; i < list11.size(); i++) {
                            if (list11.get(i).getDTR_STRUCTURE_CODE() != null) {
                                if (list11.get(i).getDTR_STRUCTURE_CODE().equalsIgnoreCase(selectedString.split("~")[0])) {
                                    isMatched = true;
                                }
                            }

                        }

                        if (isMatched && !IS_EDIT) {
                            openAlertDialogForDTR(selectedString.split("~")[0]);

                        }
                        if (!existing_dtr_structure_code_11kv.getText().toString().isEmpty() &&
                                existing_dtr_structure_code_11kv.getText().toString() != null) {
                            List<DTRFeederMaster> data = databaseEstimates.getPhaseANDCapacityData(existing_dtr_structure_code_11kv.getText().toString().split("~")[0]);

                            // System.out.println("DETAILS_OF_DTR:::->" + data.get(0).getDTR_LOCATION());
                            if (!LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                                if (isMatched) {
                                    layout_dtr_details.setVisibility(View.VISIBLE);
                                    tv_edit_dtr_details.setVisibility(View.VISIBLE);
                                } else {
                                    layout_dtr_details.setVisibility(View.VISIBLE);
                                    tv_edit_dtr_details.setVisibility(View.VISIBLE);
                                }
                            }


                            existing_dtr_details_11kv.setVisibility(View.VISIBLE);
                            existing_sp_dtr_phase_11.setText(data.get(0).getPHASE());

                            if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                                layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
                            } else {
                                layout_dtr_phase_11kv.setVisibility(View.GONE);

                            }


//                            if (data.get(0).getPHASE().isEmpty()) {
//                                layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
//                            } else {
//                                layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
//                            }

                            if (!selectedString.split("~")[0].equalsIgnoreCase(LINE_EXT_DTR_STRUCTURE_CODE)) {
                                //NEW DTR SHIFTING CODE FOR EXISTING POLE_SURVEY_TYPE
                                String surveyFrom = LoginActivity.SURVEY_FROM;
                                String locationType = String.valueOf(DATA_MODEL.getIS_EXISTING_LOCATION_NEW());
                                String dtrSurvey = String.valueOf(sp_dtr_survey_11.getSelectedItem());
                                String poleConfig = String.valueOf(DATA_MODEL.getPOLE_CONFIG());

                                if (surveyFrom.equalsIgnoreCase("ESTIMATES")
                                        || surveyFrom.equalsIgnoreCase("WORK_ORDER")) {

                                    if (locationType.equalsIgnoreCase("Existing")) {
                                        if (dtrSurvey.equalsIgnoreCase("Existing")
                                                || dtrSurvey.equalsIgnoreCase("Enhancement")) {
                                            if (poleConfig.equalsIgnoreCase("DTR Pole")
//                                        && !EstimatesMap.LINE_EXT_POLE_CONFIG.equalsIgnoreCase("DTR Pole")
                                            ) {
                                                openWarningAlertForDTR(selectedString);
                                            }
                                        }
                                    } else {
                                        if (dtrSurvey.equalsIgnoreCase("Existing")
                                                || dtrSurvey.equalsIgnoreCase("Enhancement")) {

                                            if (poleConfig.equalsIgnoreCase("DTR Pole")
//                                        && !EstimatesMap.LINE_EXT_POLE_CONFIG.equalsIgnoreCase("DTR Pole")
                                            ) {
                                                existingDTRMappedCheck(selectedString);
                                                //openWarningAlertForDTR(selectedString);
                                            }
                                        }
                                    }
                                }
                            }


                            //25-02-2025 Chandu Changes
                            if (data.get(0).getPHASE().equalsIgnoreCase("3")) {
                                tv_dtr_phase.setText("III");

                            } else if (data.get(0).getPHASE().equalsIgnoreCase("2")) {
                                tv_dtr_phase.setText("II");

                            } else if (data.get(0).getPHASE().equalsIgnoreCase("1")) {
                                tv_dtr_phase.setText("I");

                            }

                            // System.out.println("DETAILS_OF_DTR:::->" + data.get(0).getDTR_LOCATION());


                            if (!data.get(0).getCAPACITY().contains("KVA")) {
                                tv_dtr_capacity.setText(data.get(0).getCAPACITY() + " " + "KVA");
                            }


                            tv_dtr_location_name.setText(data.get(0).getDTR_LOCATION());
                            tv_dtr_make.setText(data.get(0).getDTR_MAKE());
                            tv_dtr_slno.setText(data.get(0).getDTR_SLNO());


                            //NEW CHANGES FOR VERSION 4.3
                            if (layout_dtr_details.getVisibility() == View.VISIBLE) {
                                String capacity = tv_dtr_capacity.getText().toString();
                                // System.out.println("CAPACITY_WORK_ORDER-->" + capacity);
                                loadDTRMateeial(tv_dtr_capacity.getText().toString().replace("KVA", "").trim());
                            }


                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String json = gson.toJson(data);
//                            System.out.println("DTR_DATA_FROM_API:"+json);
//                            System.out.println("DTR_DATA_FROM_API_SL_NO:"+data.get(0).getDTR_SLNO());


                        } else {
                            layout_dtr_details.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });

        //NEW DTR CHANGES BY CHANDU
        tv_edit_dtr_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_DTR_EDIT = true;
                tv_edit_dtr_details.setVisibility(View.GONE);
                layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
                layout_dtr_location_11kv.setVisibility(View.VISIBLE);
                layout_dtr_phase_desc_11kv.setVisibility(View.VISIBLE);
                layout_dtr_mounting.setVisibility(View.VISIBLE);
                layout_dtr_apartment_11kv.setVisibility(View.VISIBLE);
                setISLTAvailableinExistingCase();
                layout_dtr_details.setVisibility(View.VISIBLE);
                existing_layout_dtr_make_11kv.setVisibility(View.GONE);

                setDTRPhaseAdapter(voltage_level);
                sp_dtr_apartment_11.setSelection(0, true);


                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {

                        layout_dtr_phase_11kv.setVisibility(View.GONE);

                        layout_dtr_phase_desc_11kv.setVisibility(View.GONE);
                        if (tv_dtr_make.getText().toString().isEmpty()) {
                            layout_dtr_make_11kv.setVisibility(View.VISIBLE);

                        } else {
                            layout_dtr_make_11kv.setVisibility(View.VISIBLE);
                        }
                        layout_dtr_sl_no_11kv.setVisibility(View.VISIBLE);
                        et_dtr_make.setText(tv_dtr_make.getText().toString());
                        et_dtr_sl_no.setText(tv_dtr_slno.getText().toString());
                        dtr_location_name.setText(tv_dtr_location_name.getText().toString());
                        existing_dtr_dtr_location_name.setText(tv_dtr_location_name.getText().toString());

                        existing_dtr_et_dtr_make.setText(tv_dtr_make.getText().toString());
                        existing_dtr_et_dtr_sl_no.setText(tv_dtr_slno.getText().toString());
                    } else {
                        existing_dtr_layout_dtr_make_11kv.setVisibility(View.VISIBLE);
                        existing_dtr_particulars_11kv.setVisibility(View.VISIBLE);

                        setDTRPhaseAdapterExisting(voltage_level);
                        existing_dtr_dtr_location_name.setText(tv_dtr_location_name.getText().toString());
                        existing_dtr_et_dtr_make.setText(tv_dtr_make.getText().toString());
                        existing_dtr_et_dtr_sl_no.setText(tv_dtr_slno.getText().toString());

                    }
                } else {
                    existing_dtr_layout_dtr_make_11kv.setVisibility(View.VISIBLE);
                    existing_dtr_layout_dtr_make_11kv.setVisibility(View.VISIBLE);
                    existing_dtr_particulars_11kv.setVisibility(View.VISIBLE);
                    setDTRPhaseAdapterExisting(voltage_level);
                    existing_dtr_dtr_location_name.setText(tv_dtr_location_name.getText().toString());
                    existing_dtr_et_dtr_make.setText(tv_dtr_make.getText().toString());
                    existing_dtr_et_dtr_sl_no.setText(tv_dtr_slno.getText().toString());
                }


            }
        });


    }

    private void setDTRPhaseAdapterExisting(String voltage_level) {
        dtr_phase.clear();

        if (voltage_level.equalsIgnoreCase("11KV")) {
            dtr_phase.add("Select");
            dtr_phase.add("I");
            dtr_phase.add("III");
        } else {
            dtr_phase.add("Select");
            dtr_phase.add("I");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dtr_phase);
        existing_dtr_sp_dtr_phase_11.setAdapter(adapter);
        existing_dtr_sp_dtr_phase_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPhase = parent.getSelectedItem().toString();
                setDTRPhaseAdapter(voltage_level);
                sp_dtr_apartment_11.setSelection(0, true);
                sp_dtr_apartment_11_existing.setSelection(0, true);
                if ("I".equalsIgnoreCase(selectedPhase)) {
                    existing_dtr_layout_dtr_capacity.setVisibility(View.VISIBLE);
                    existing_dtr_sp_dtr_capacity_phase1_11.setVisibility(View.VISIBLE);
                    existing_dtr_sp_dtr_capacity_phase2_11.setVisibility(View.GONE);
                    existing_dtr_sp_dtr_capacity_phase1_11.setSelection(0, true);
                    tv_dtr_phase.setText("I");
                } else if ("III".equalsIgnoreCase(selectedPhase)) {
                    existing_dtr_layout_dtr_capacity.setVisibility(View.VISIBLE);
                    existing_dtr_sp_dtr_capacity_phase2_11.setVisibility(View.VISIBLE);
                    existing_dtr_sp_dtr_capacity_phase1_11.setVisibility(View.GONE);
                    existing_dtr_sp_dtr_capacity_phase2_11.setSelection(0, true);
                    tv_dtr_phase.setText("III");
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

    private void setDTRPhaseAdapter(String voltage_level) {
        dtr_phase.clear();

        if (voltage_level.equalsIgnoreCase("11KV")) {
            dtr_phase.add("Select");
            dtr_phase.add("I");
            dtr_phase.add("III");
        } else {
            dtr_phase.add("Select");
            dtr_phase.add("I");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dtr_phase);
        sp_dtr_phase_11.setAdapter(adapter);
    }

    private void setISLTAvailableinExistingCase() {
        List<String> ltAvailable = new ArrayList<>();

        String ltCircuit = EstimatesMap.LINE_EXT_LT_CIRCUIT_AVAILABLE != null ? EstimatesMap.LINE_EXT_LT_CIRCUIT_AVAILABLE.trim() : "";
        String poleConfig = EstimatesMap.LINE_EXT_POLE_CONFIG != null ? EstimatesMap.LINE_EXT_POLE_CONFIG.trim() : "";

        if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement") ||
                sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {
            //System.out.println("LT_CIRCUIT_AVAILABLE ---> " + ltCircuit);
            //System.out.println("POLE_CONFIG ---> " + poleConfig);

            if (!ltCircuit.isEmpty() && poleConfig.equalsIgnoreCase("DTR Pole") && EstimatesMap.IS_LINE_EXTENSION_11_DATA_AVAILABLE
            ) {
                if (ltCircuit.equalsIgnoreCase("YES")) {
                    ltAvailable.add("Yes");
                    //System.out.println("Showing only: Yes");
                } else {
                    ltAvailable.add("No");
                    //System.out.println("Showing only: No");
                }
            } else {
                ltAvailable.add("Select");
                ltAvailable.add("Yes");
                ltAvailable.add("No");
                //System.out.println("Showing: Select, Yes, No");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, ltAvailable);
            sp_dtr_apartment_11.setAdapter(adapter);

            // Optional: auto-select first item (especially useful if only "Yes" or "No")
            if (ltAvailable.isEmpty()) {
                sp_dtr_apartment_11.setSelection(0);
            }
        } else {
            if (!ltCircuit.isEmpty() && poleConfig.equalsIgnoreCase("DTR Pole")
            ) {
                if (ltCircuit.equalsIgnoreCase("YES")) {
                    ltAvailable.add("Yes");
                    //System.out.println("Showing only: Yes");
                } else {
                    ltAvailable.add("No");
                    //System.out.println("Showing only: No");
                }
            } else {
                ltAvailable.add("Select");
                ltAvailable.add("Yes");
                ltAvailable.add("No");
                //System.out.println("Showing: Select, Yes, No");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, ltAvailable);
            sp_dtr_apartment_11_existing.setAdapter(adapter);

            // Optional: auto-select first item (especially useful if only "Yes" or "No")
            if (ltAvailable.isEmpty()) {
                sp_dtr_apartment_11_existing.setSelection(0);
            }
        }
    }

    private double getDistanceBetweenTwoPoles(
            double lat1, double lon1,
            double lat2, double lon2
    ) {
        // Latitude & Longitude are intentionally reversed

        Location start = new Location("start");
        start.setLatitude(lon1);
        start.setLongitude(lat1);

        Location end = new Location("end");
        end.setLatitude(lon2);
        end.setLongitude(lat2);

        return start.distanceTo(end); // meters
    }

    private void existingDTRMappedCheck(String selectedString) {
        if (!selectedString.split("~")[0].isEmpty()) {
            List<DTRFeederMaster> dtrData = databaseEstimates.getPhaseANDCapacityData(selectedString.split("~")[0]);
            String dtrGlat = dtrData.get(0).getGLAT();
            String dtrGlong = dtrData.get(0).getGLONG();
            double distance_new_dtr = 0.0;
            if (DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Proposed")) {
                distance_new_dtr = getDistanceBetweenTwoPoles(Double.parseDouble(dtrGlat), Double.parseDouble(dtrGlong), Double.parseDouble(DATA_MODEL.getLATITUDE()), Double.parseDouble(DATA_MODEL.getLONGITUDE()));
            } else {
                distance_new_dtr = getDistanceBetweenTwoPoles(Double.parseDouble(dtrGlat), Double.parseDouble(dtrGlong), Double.parseDouble(LINE_EXT_DTR_LATTITUDE), Double.parseDouble(LINE_EXT_DTR_LONGITUDE));
            }
            long roundedDistance = Math.round(distance_new_dtr);
            System.out.println("DISTANCE_NEW_DTR::::" + roundedDistance);


            if (dtrData != null && !dtrData.isEmpty()) {
                if (dtrData.get(0).getIS_MAPPED().equalsIgnoreCase("YES")) {
                    //System.out.println("DTR_DETAILS::::"+dtrData.get(0).getFEEDER_ID());
                    openAlertDialogForWrongDtrMapped(dtrData.get(0).getFEEDER_ID(), dtrData.get(0).getFEEDER_NAME(), dtrData.get(0).getMAPPED_LOCATION_NO(), existing_dtr_structure_code_11kv, selectedString, roundedDistance);
                }
            }
        }
    }

    private void setDTRShifted(String selectedString) {
        if (IS_EDIT) {
            //System.out.println("DTR_SURVEY_TYPE_EDIT:" + DATA_MODEL.getDTR_SURVEY_TYPE());
            //System.out.println("DTR_SHIFTED_EDIT:" + DATA_MODEL.getDTR_SHIFTED());
            if (DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Existing") ||
                    DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED")
                    || DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Enhancement")) {
                if (DATA_MODEL.getDTR_SHIFTED() != null) {
                    if (DATA_MODEL.getDTR_SHIFTED().equalsIgnoreCase("YES")) {
                        sp_dtr_shifted_11.setSelection(1, true);
                    } else {
                        sp_dtr_shifted_11.setSelection(2, true);
                    }
                } else {

                }
            } else {
                layout_dtr_shifted.setVisibility(View.GONE);
            }
        } else {
            layout_dtr_shifted.setVisibility(View.VISIBLE);
            List<String> dtrShifted = new ArrayList<>();
            if (selectedString.contains("YES")) {
                dtrShifted.add("Yes");
            } else {
                dtrShifted.add("NO");
            }
            ArrayAdapter<String> adapterpinCut = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dtrShifted);
            sp_dtr_shifted_11.setAdapter(adapterpinCut);

        }


    }

    private void openAlertDialogForWrongDtrMapped(String feeder, String feeder_name, String location_no, TextView textView, String selectedString, long dtr_distance) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Pay Attention");
        if (DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Proposed")) {
            builder.setMessage("The selected DTR Structure Code is mapped at the location " + " " + location_no + " " + "on " + feeder_name + " " + "(" + feeder + ")" + " " + "Which is " + dtr_distance + "m away from the selected location No " + DATA_MODEL.getPOLE_NO() + ". " + "Is DTR Shifted from the original location?");
        } else {
            builder.setMessage("The selected DTR Structure Code is mapped at the location " + " " + location_no + " " + "on " + feeder_name + " " + "(" + feeder + ")" + " " + "Which is " + dtr_distance + "m away from the selected location No " + LINE_EXT_DTR_LOCATION_NO + ". " + "Is DTR Shifted from the original location?");
        }
        builder.setCancelable(false);
        builder.setPositiveButton("Yes,DTR Shifted", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDTRShifted("YES");
                textView.setText(selectedString);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            textView.setText("");
            layout_dtr_details.setVisibility(View.GONE);
            tv_edit_dtr_details.setVisibility(View.GONE);
            dialog.dismiss();
        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openAlertDialogForPoleType() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Pay Attention!!");
        builder.setMessage("Invalid pole survey type");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", (dialog, which) -> {

            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                existing_dtr_structure_code_11kv.setText("");
                onBackPressed();
                sp_dtr_survey_11.setSelection(0, true);
            } else {
                sp_dtr_survey_11.setSelection(0, true);
            }
            dialog.dismiss();
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    //NEW CHANGES BY CHANDU v3.7
    private void openWarningAlertForDTR(String selectedString) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Pay Attention!!");
        builder.setMessage("Is DTR shifted from existing location?");
        builder.setCancelable(false);

        builder.setNegativeButton("NO", (dialog, which) -> {
            setDTRShifted("NO");
            layout_dtr_shifted_remark.setVisibility(View.GONE);
            openAlertDialogForPoleType();
            dialog.dismiss();
        });

        builder.setPositiveButton("YES", (dialog, which) -> {
            setDTRShifted("YES");
            layout_dtr_shifted_remark.setVisibility(View.VISIBLE);
            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                existingDTRMappedCheck(selectedString);
            } else {
                if (DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Existing")) {
                    existingDTRMappedCheck(selectedString);
                }
            }
//            if (sp_is_location_exists.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
//                existingDTRMappedCheck(selectedString);
//            }
            dialog.dismiss();
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openAlertDialogForDTR(String DTR_CODE) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Pay Attention!!");
        builder.setMessage("The DTR you selected has already been surveyed." + " " + DTR_CODE);
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", (dialog, which) -> {
            existing_dtr_structure_code_11kv.setText("");
            dialog.dismiss();


        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
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

    private void updateSearchableSpinnerItems() {

        //System.out.println("CLICKED_FEEDER :" + WorkInvolmentsActivity.CLICKED_FEEDER);

        String selectedFeederId = null;


        switch (WorkInvolmentsActivity.CLICKED_FEEDER) {
            case "FEEDER 1":
                selectedFeederId = WorkInvolmentsActivity.feeder_id1_11kv;
                break;
            case "FEEDER 2":
                selectedFeederId = WorkInvolmentsActivity.feeder_id2_11kv;
                break;
            case "FEEDER 3":
                selectedFeederId = WorkInvolmentsActivity.feeder_id3_11kv;
                break;
            case "FEEDER 4":
                selectedFeederId = WorkInvolmentsActivity.feeder_id4_11kv;
                break;
            case "FEEDER 5":
                selectedFeederId = WorkInvolmentsActivity.feeder_id5_11kv;
                break;

            case "FEEDER 6":
                selectedFeederId = WorkInvolmentsActivity.feeder_id6_11kv;
                break;
            case "FEEDER 7":
                selectedFeederId = WorkInvolmentsActivity.feeder_id7_11kv;
                break;

            case "FEEDER 8":
                selectedFeederId = WorkInvolmentsActivity.feeder_id8_11kv;
                break;

            case "FEEDER 9":
                selectedFeederId = WorkInvolmentsActivity.feeder_id9_11kv;
                break;

            case "FEEDER 10":
                selectedFeederId = WorkInvolmentsActivity.feeder_id10_11kv;
                break;

        }

        EstimatesHome.dtr.clear();

        if (selectedFeederId != null) {
            for (int i = 0; i < EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.size(); i++) {
                if (selectedFeederId.equals(EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.get(i).getFEEDER_ID())) {
                    String dtrCode = EstimatesHome.UN_MAPPED_DTR_WITH_FEEDERS.get(i).getDTR_STRUCTURE_CODE();
                    if (!EstimatesHome.Mapped_DTR.contains(dtrCode)) {
                        EstimatesHome.Mapped_DTR.add(dtrCode);
                    }
                }
            }
        }

        if (selectedFeederId != null) {
            for (int i = 0; i < EstimatesHome.MAPPED_DTR_WITH_FEEDERS.size(); i++) {
                if (selectedFeederId.equals(EstimatesHome.MAPPED_DTR_WITH_FEEDERS.get(i).getFEEDER_ID())) {
                    String dtrCode = EstimatesHome.MAPPED_DTR_WITH_FEEDERS.get(i).getDTR_STRUCTURE_CODE();
                    if (!EstimatesHome.dtr.contains(dtrCode)) {
                        EstimatesHome.dtr.add(dtrCode);
                    }
                }
            }
        }


        if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed") &&
                LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
            searchableSpinner.setSpinnerListItems(EstimatesHome.Mapped_DTR);
        } else if ((sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Existing") ||
                sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")
                        && LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER"))) {
            searchableSpinner.setSpinnerListItems(EstimatesHome.dtr);
        } else {
            searchableSpinner.setSpinnerListItems(EstimatesHome.dtr);
        }
    }

    private void viewCasting() {
        tv_edit_dtr_details = findViewById(R.id.tv_edit_dtr_details);
        existing_dtr_details_11kv = findViewById(R.id.existing_dtr_details_11kv);
        existing_sp_dtr_phase_11 = findViewById(R.id.existing_sp_dtr_phase_11);
        sp_pole_config = findViewById(R.id.sp_pole_config);
        dtr_location_name = findViewById(R.id.dtr_location_name);
        sp_dtr_survey_11 = findViewById(R.id.sp_dtr_survey_11);
        existing_dtr_dtr_location_name = findViewById(R.id.existing_dtr_dtr_location_name);
        existing_dtr_et_dtr_sl_no = findViewById(R.id.existing_dtr_et_dtr_sl_no);
        existing_dtr_et_dtr_make = findViewById(R.id.existing_dtr_et_dtr_make);
        //et_service_released_11kv = findViewById(R.id.et_service_released_11kv);
       //et_service_released_33kv = findViewById(R.id.et_service_released_33kv);
        sp_dtr_mounting_11 = findViewById(R.id.sp_dtr_mounting_11);
        sp_dtr_apartment_11_existing = findViewById(R.id.sp_dtr_apartment_11_existing);
        et_dtr_sl_no = findViewById(R.id.et_dtr_sl_no);
        et_dtr_make = findViewById(R.id.et_dtr_make);
        sp_dtr_phase_11 = findViewById(R.id.sp_dtr_phase_11);
        sp_dtr_material_11 = findViewById(R.id.sp_dtr_material_11);
        sp_dtr_capacity_phase1_11 = findViewById(R.id.sp_dtr_capacity_phase1_11);
        sp_dtr_capacity_phase2_11 = findViewById(R.id.sp_dtr_capacity_phase2_11);
        sp_no_of_lt_circuits_11 = findViewById(R.id.sp_no_of_lt_circuits_11);
        layout_dtr_survey = findViewById(R.id.layout_dtr_survey);
        proposed_layout_dtr_details_11kv = findViewById(R.id.proposed_layout_dtr_details_11kv);
        layout_dtr_phase_11kv = findViewById(R.id.layout_dtr_phase_11kv);
        layout_dtr_capacity = findViewById(R.id.layout_dtr_capacity);
        layout_dtr_material = findViewById(R.id.layout_dtr_material);
        layout_dtr_location_11kv = findViewById(R.id.layout_dtr_location_11kv);
        layout_dtr_apartment_11kv = findViewById(R.id.layout_dtr_apartment_11kv);
        layout_no_of_lt_circuits_11 = findViewById(R.id.layout_no_of_lt_circuits_11);
        layout_meter_11kv = findViewById(R.id.layout_meter_11kv);
        sap_33_material_layout_11_meter = findViewById(R.id.sap_33_material_layout_11_meter);
        sp_dtr_apartment_11 = findViewById(R.id.sp_dtr_apartment_11);
        existing_layout_dtr_details_11kv = findViewById(R.id.existing_layout_dtr_details_11kv);
        existing_main_lt_layout_11kv = findViewById(R.id.existing_main_lt_layout_11kv);
        existing_dtr_structure_code_11kv = findViewById(R.id.existing_dtr_structure_code_11kv);
        layout_dtr_make_11kv = findViewById(R.id.layout_dtr_make_11kv);
        layout_dtr_sl_no_11kv = findViewById(R.id.layout_dtr_sl_no_11kv);
        existing_dtr_particulars_11kv = findViewById(R.id.existing_dtr_particulars_11kv);
        layout_dtr_details = findViewById(R.id.layout_dtr_details);
        existing_sp_dtr_phase_11 = findViewById(R.id.existing_sp_dtr_phase_11);
        tv_dtr_phase = findViewById(R.id.tv_dtr_phase);
        tv_dtr_capacity = findViewById(R.id.tv_dtr_capacity);
        tv_dtr_slno = findViewById(R.id.tv_dtr_slno);
        tv_dtr_make = findViewById(R.id.tv_dtr_make);
        tv_dtr_location_name = findViewById(R.id.tv_dtr_location_name);
        sp_dtr_shifted_11 = findViewById(R.id.sp_dtr_shifted_11);
        layout_dtr_shifted = findViewById(R.id.layout_dtr_shifted);
        layout_dtr_shifted_remark = findViewById(R.id.layout_dtr_shifted_remark);
        layout_dtr_phase_desc_11kv = findViewById(R.id.layout_dtr_phase_desc_11kv);
        layout_dtr_mounting = findViewById(R.id.layout_dtr_mounting);
        layout_dtr_details = findViewById(R.id.layout_dtr_details);
        existing_dtr_layout_dtr_make_11kv = findViewById(R.id.existing_dtr_layout_dtr_make_11kv);
        existing_layout_dtr_make_11kv = findViewById(R.id.existing_layout_dtr_make_11kv);
        existing_dtr_sp_dtr_phase_11 = findViewById(R.id.existing_dtr_sp_dtr_phase_11);
        existing_dtr_layout_dtr_capacity = findViewById(R.id.existing_dtr_layout_dtr_capacity);
        existing_dtr_sp_dtr_capacity_phase1_11 = findViewById(R.id.existing_dtr_sp_dtr_capacity_phase1_11);
        existing_dtr_sp_dtr_capacity_phase2_11 = findViewById(R.id.existing_dtr_sp_dtr_capacity_phase2_11);
        existing_dtr_layout_dtr_sl_no_11kv = findViewById(R.id.existing_dtr_layout_dtr_sl_no_11kv);
    }

    private void setProposedDTRDetails() {
        layout_dtr_survey.setVisibility(View.VISIBLE);
        proposed_layout_dtr_details_11kv.setVisibility(View.VISIBLE);
        layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
        layout_dtr_location_11kv.setVisibility(View.VISIBLE);
        layout_dtr_apartment_11kv.setVisibility(View.VISIBLE);
    }

    private void setEnhancedDTRDetails() {
        layout_dtr_survey.setVisibility(View.VISIBLE);
        proposed_layout_dtr_details_11kv.setVisibility(View.VISIBLE);
        layout_dtr_phase_11kv.setVisibility(View.GONE);
        layout_dtr_location_11kv.setVisibility(View.GONE);
        layout_dtr_apartment_11kv.setVisibility(View.GONE);
        existing_layout_dtr_details_11kv.setVisibility(View.VISIBLE);
        existing_main_lt_layout_11kv.setVisibility(View.VISIBLE);
    }

    private void setPoleConfiguration() {
        List<String> poleConfiguration = new ArrayList<>();
        if (voltage_level.equalsIgnoreCase("11KV")) {
            if (POLE_CONFIG.equalsIgnoreCase("LINE-POLE")) {
                poleConfiguration.clear();
                poleConfiguration.add("Select");
                poleConfiguration.add("Line Pole to DTR Pole");
                poleConfiguration.add("Line Pole to AB Pole");
                poleConfiguration.add("Line Pole to HTTVR Pole");
            } else if (POLE_CONFIG.equalsIgnoreCase("AB-POLE")) {
                poleConfiguration.clear();
                poleConfiguration.add("Select");
                poleConfiguration.add("AB Pole to DTR Pole");
                poleConfiguration.add("AB Pole to Line Pole");
                poleConfiguration.add("AB Pole to HTTVR Pole");
            } else if (POLE_CONFIG.equalsIgnoreCase("DTR POLE")) {
                poleConfiguration.clear();
                poleConfiguration.add("Select");
                poleConfiguration.add("DTR Pole to Line Pole");
                poleConfiguration.add("DTR Pole to AB Pole");
                poleConfiguration.add("DTR Pole to HTTVR Pole");
            }

            ArrayAdapter<String> adapterPoleConfig = new ArrayAdapter<String>(
                    this, android.R.layout.simple_dropdown_item_1line, poleConfiguration);
            sp_pole_config.setAdapter(adapterPoleConfig);

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_pole_config:
                String selectedId = sp_pole_config.getSelectedItem().toString();
                switch (selectedId) {
                    case "Line Pole to DTR Pole":
                        layout_dtr_survey.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case R.id.sp_dtr_survey_11:
                String selectedType = sp_dtr_survey_11.getSelectedItem().toString();
                switch (selectedType) {
                    case "Proposed":
                        setProposedDTRDetails();
                        break;
                    case "Enhancement":
                        setEnhancedDTRDetails();
                        break;

                }
                break;
            case R.id.sp_dtr_phase_11:
                String selectedPhase = sp_dtr_phase_11.getSelectedItem().toString();
                switch (selectedPhase) {
                    case "I":
                        sp_dtr_capacity_phase1_11.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase2_11.setVisibility(View.GONE);
                        break;
                    case "III":
                        sp_dtr_capacity_phase2_11.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase1_11.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.sp_dtr_capacity_phase1_11:
                if (!sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")
                            || sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                        String capacity = sp_dtr_capacity_phase1_11.getSelectedItem().toString();
                        capacity = capacity.replace("KVA", "").trim();
                        loadDTRMateeial(capacity);
                    }
                } else {
                    loadDTRMateeial("");
                }
                break;
            case R.id.sp_dtr_capacity_phase2_11:
                if (!sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed") ||
                            sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                        String capacity = sp_dtr_capacity_phase2_11.getSelectedItem().toString();
                        capacity = capacity.replace("kVA", "").trim();
                        System.out.println("CAPACITY--->" + capacity);
                        loadDTRMateeial(capacity);
                    }
                } else {
                    loadDTRMateeial("");
                }
                break;
            case R.id.sp_dtr_apartment_11:
                if (!sp_dtr_apartment_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    isItApartmentDTR = sp_dtr_apartment_11.getSelectedItem().toString();
                    if (sp_dtr_apartment_11.getSelectedItem().toString().equalsIgnoreCase("Yes") &&
                            sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {
                        layout_no_of_lt_circuits_11.setVisibility(View.VISIBLE);
                        setNoOfLTCKT(voltage_level);
                    } else if (sp_dtr_apartment_11.getSelectedItem().toString().equalsIgnoreCase("Yes") &&
                            (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Existing") ||
                                    sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement"))) {
                        layout_no_of_lt_circuits_11.setVisibility(View.GONE);
                        layout_meter_11kv.setVisibility(View.GONE);

                    } else if (sp_dtr_apartment_11.getSelectedItem().toString().equalsIgnoreCase("No")) {
                        layout_no_of_lt_circuits_11.setVisibility(View.GONE);
                        layout_meter_11kv.setVisibility(View.VISIBLE);

                    } else {
                        layout_no_of_lt_circuits_11.setVisibility(View.GONE);

                        //25-01-2025 NEW CHANGES BY CHANDU

                    }
                } else {
                    layout_no_of_lt_circuits_11.setVisibility(View.GONE);
                    layout_meter_11kv.setVisibility(View.GONE);
                    //25-01-2025 NEW CHANGES BY CHANDU


                }

                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setNoOfLTCKT(String voltage_level) {
        List<String> count_lt_ckt = new ArrayList<>();
        count_lt_ckt.clear();
        count_lt_ckt.add("Select");

        int maxCount = 1; // default

        if (DATA_MODEL.getLINE_TYPE().equalsIgnoreCase("OverHead")) {

            if (layout_dtr_phase_11kv.getVisibility() == View.VISIBLE) {
                String phase = sp_dtr_phase_11.getSelectedItem().toString();

                if (phase.equalsIgnoreCase("I")) {
                    maxCount = 1;

                } else if (phase.equalsIgnoreCase("III")) {
                    String capacity = sp_dtr_capacity_phase2_11.getSelectedItem().toString();

                    if (capacity.equalsIgnoreCase("160 KVA")
                            || capacity.equalsIgnoreCase("315 KVA")
                            || capacity.equalsIgnoreCase("500 KVA")) {
                        maxCount = 3;
                    } else {
                        maxCount = 2;
                    }
                }
            } else {
                if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("I")) {
                    maxCount = 1;
                } else if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("III")) {
                    String capacity = sp_dtr_capacity_phase2_11.getSelectedItem().toString();

                    if (capacity.equalsIgnoreCase("160 KVA")
                            || capacity.equalsIgnoreCase("315 KVA")
                            || capacity.equalsIgnoreCase("500 KVA")) {
                        maxCount = 3;
                    } else {
                        maxCount = 2;
                    }
                }
            }


        } else {
            maxCount = 12;
        }

        for (int i = 1; i <= maxCount; i++) {
            switch (i) {
                case 1:
                    count_lt_ckt.add("1 (Single Feeding)");
                    break;
                case 2:
                    count_lt_ckt.add("2 (Double Feeding A & B)");
                    break;
                case 3:
                    count_lt_ckt.add("3 (Three Feedings A, B, C)");
                    break;
                default:
                    count_lt_ckt.add(String.valueOf(i));
                    break;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, count_lt_ckt);
        sp_no_of_lt_circuits_11.setAdapter(adapter);
    }


    private void loadDTRMateeial(String capacity) {
        if (capacity.equalsIgnoreCase("kVA")) {
            capacity = "KVA";
        }
        List<DTR_HT_MaterialModel> model = databaseEstimates.getDTR_HT("DT", capacity);
        if (!model.isEmpty()) {
            layout_dtr_material.setVisibility(View.VISIBLE);

            // System.out.println("QUARY FOR DTR_HT" + model.size());
            DTR_HT_MaterialModel selectModel = new DTR_HT_MaterialModel();
            selectModel.setMATERAIL_DESC("Select");
            selectModel.setMATERIALCODE("");
            model.add(0, selectModel);

            ArrayAdapter<DTR_HT_MaterialModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, model);
            sp_dtr_material_11.setAdapter(adapter);
            sp_dtr_material_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!sp_dtr_material_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        conductorSAPCode11KVDTR = model.get(position).getMATERIALCODE();
                        survey_code_dtr = model.get(position).getSURVEY_CODE();
                        DTA_ID = model.get(position).getGROUPCODE();
                        //  System.out.println("MATERIAL_TYPE" + "DTR MATERIAL" + DTA_ID);

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
}
