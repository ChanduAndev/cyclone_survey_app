package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_CURRENT_LOCATION_LINE_EXTENSION_11;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_CURRENT_LOCATION_LINE_EXTENSION_33;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_INTERMEDIATE_POLE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_11_DATA_AVAILABLE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_33_DATA_AVAILABLE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_LT_DATA_AVAILABLE;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.models.DTR_HT_MaterialModel;
import com.apepdcl.gis_survey.estimates.models.MaterialMasterModel;
import com.apepdcl.gis_survey.estimates.models.PoleDistanceModel;
import com.apepdcl.gis_survey.estimates.models.PoleHeightModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.models.location.DATA;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPoleDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner sp_pole_support_33, sp_no_8_poles_33kv, sp_sp_pole_desc_33_8_pole_support, sp_pole_type_33, sp_lineType_33, sp_service_released_11kv,
            sp_pole_height_33, sp_pole_configuration_33, sp_no_9_poles_33kv, sp_sp_pole_desc_33__9_1_pole_support, sp_conductor_33, sp_sp_pole_desc_33, sp_connecting_point,
            sp_conductor_size_33, sp_pole_concrete_33kv, sp_pole_structure_33kv, sp_pole_pin_cut_point_33kv, sp_no_of_stays_33kv, sp_type_of_nature_11kv,
            sp_capacity_httvr, sp_material_httvr, sp_ht_service_no_11kv;
    private LinearLayout layout_sp_no_of_pole_support_33, layout_sp_no_of_pole_stay_support_33, layout_sp_pole_desc_33_8_pole_support, loader_layout, layout_service_released_no_11kv,
            layout_sp_pole_desc_33_pole_support, layout_conductor_size_33, layout_conductor_33, layout_line_type_33kv, layout_ht_sc_no_no_11kv,
            layout_pole_structure_33kv, layout_pole_concrete_33kv, layout_sp_pole_type_33, layout_sp_pole_height_33, layout_sp_pole_desc_33,
            layout_pole_support_33kv, layout_sp_pole_cut_pin_point_33kv, layout_sp_pole_cut_pin_point, layout_connecting_point, layout_distance,
            layout_source_location, layout_source_pole, layout_distance_prv, layout_pole_details, layout_httr_pole_details, layout_material_httvr, layout_capacity_httvr,
            layout_ht_service_no_list, layout_service_released;
    CheckBox ch_33kv_8_stuart_poles, ch_33kv_9_stuart_poles, ch_33kv_pole_desc;
    TextView tv_select_pole_no_text, tv_select_pole_no_text_stay_33, tv_select_9m_pole_no_33kv, tv_select_8m_pole_no_33kv, source_pole, pole_no,
            last_pole_distance, current_distance_prv, last_pole_distance_prv, home_heading;

    String EST_NO, GIS_ID, BTN_VALUE, wo_no, EST_NATURE = "", poleType33 = "", PURPOSE = "", REG_COUNT = "", from = "", btnClick = "",
            TYPE = "", SELECTED_SOURCE_POLE_LOCATION = "", MAX_LENGTH = "";
    ArrayList<NewModel> list33, list11, listLT, allList;
    ArcGISDatabaseEstimates databaseEstimates;
    TextView btn_add, from_pole;
    EditText et_service_released_11kv, et_ht_sc_no_11kv;

    boolean IS_EDIT;
    NewModel DATA_MODEL;
    private double distanceInFloat;
    private float distance;
    private String sapCodeForHttvrmaterial = "", referenceCode = "", survey_code = "",DATA_ID="";

    private RetrofitServiceEstimates retrofitService;
    RelativeLayout back_layout, close_layout;
    double selected_pole_lattitude = 0.0;
    double selected_pole_longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_pole_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IS_EDIT = getIntent().getBooleanExtra("IS_EDIT", false);
        if (IS_EDIT) {
            DATA_MODEL = (NewModel) getIntent().getSerializableExtra("DATA_MODEL");
        }
        databaseEstimates = new ArcGISDatabaseEstimates(this);


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


        list33 = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
        list11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);

//        System.out.println("LIST_33KV_SIZE" + " " + list33.size());
//        System.out.println("LIST_11KV_SIZE" + " " + list33);
        // System.out.println("LIST_11KV_SIZE--->" + " " + DATA_MODEL.getSERVICE_TYPE() + " " + DATA_MODEL.getCTPT() + " " + DATA_MODEL.getNUMB_STAYS());


        listLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);
        allList = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);
        //  model = (PurposeModel) getIntent().getSerializableExtra("Model");
        viewCasting();
        spinnerClickEvent();

        et_service_released_11kv.addTextChangedListener(new TextWatcher() {

            boolean isEditing = false;
            final int MAX_LENGTH = 15;
            final String[] VALID_PREFIX = {"HCS","HTA","HTC","HTD","HTN","HTR","HTT"};

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (isEditing) return;

                String input = text.toString().toUpperCase().trim();
                if (input.isEmpty()) return;

                // Force uppercase
                isEditing = true;
                et_service_released_11kv.setText(input);
                et_service_released_11kv.setSelection(input.length());
                isEditing = false;

                int len = input.length();

                // Allow typing until 3 chars
                if (len <= 3) {
                    boolean validPartial = false;
                    for (String p : VALID_PREFIX) {
                        if (p.startsWith(input)) {
                            validPartial = true;
                            break;
                        }
                    }
                    if (!validPartial) {
                        clearWithError();
                    }
                    return;
                }

                // After 3rd position → only digits
                if (!input.substring(3).matches("[0-9]*")) {
                    clearWithError();
                    return;
                }

                // Final validation at full length
                if (len == MAX_LENGTH) {
                    if (!input.matches("^(HCS|HTA|HTC|HTD|HTN|HTR|HTT)[0-9]{12}$")) {
                        clearWithError();
                    }
                }
            }

            private void clearWithError() {
                isEditing = true;
                et_service_released_11kv.setText("");
                et_service_released_11kv.setError(
                        "Invalid HT Registration Number.\n" +
                                "Ex: HTA123456789012"
                );
                isEditing = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        if (TYPE.equalsIgnoreCase("POLE")) {
            home_heading.setText("Edit Pole attributes");
            setPoleType();
            setPinCutForLT();
            setPoleSupportSpinner();
            setPoleConcreatSpinner();
            setPoleStructure();
        } else if (TYPE.equalsIgnoreCase("SOURCE_POLE")) {
            home_heading.setText("Edit Source Pole");
        } else if (TYPE.equalsIgnoreCase("HTTVR")) {
            home_heading.setText("Edit Httvr pole details");
            getPreviousPoleDetails(DATA_MODEL.getLOCATION_ID(), DATA_MODEL.getGIS_ID());
        }
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (TYPE.equalsIgnoreCase("SOURCE_POLE")) {
            layout_distance.setVisibility(View.VISIBLE);
            layout_distance_prv.setVisibility(View.VISIBLE);
            source_pole.setText(DATA_MODEL.getFROM_POLE());
            pole_no.setText(DATA_MODEL.getPOLE_NO());
            setSourceLocation();
            String fromPoleNo = DATA_MODEL.getFROM_POLE();
            String latitude = null;
            String longitude = null;

            if (BTN_VALUE.equalsIgnoreCase("33KV")) {

                for (int i = 0; i < list33.size(); i++) {
                    if (list33.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                        if (list33.get(i).getPOLE_NO().equalsIgnoreCase(fromPoleNo)) {
                            latitude = list33.get(i).getLATITUDE();
                            longitude = list33.get(i).getLONGITUDE();
                            System.out.println("FROM_POLE_LAT" + latitude);
                            System.out.println("FROM_POLE_Long" + longitude);
                            break; // stop once found
                        }
                    }
                }
            } else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
                for (int i = 0; i < list11.size(); i++) {
                    if (list11.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                        if (list11.get(i).getPOLE_NO().equalsIgnoreCase(fromPoleNo)) {
                            latitude = list11.get(i).getLATITUDE();
                            longitude = list11.get(i).getLONGITUDE();
                            System.out.println("FROM_POLE_LAT" + latitude);
                            System.out.println("FROM_POLE_Long" + longitude);
                            break; // stop once found
                        }
                    }
                }
            } else {
                for (int i = 0; i < listLT.size(); i++) {
                    System.out.println("FROM_POLE_LAT-->" + listLT.get(i).getLT_AVAILABLE());
                    if (listLT.get(i).getLT_AVAILABLE().equalsIgnoreCase(DATA_MODEL.getLT_AVAILABLE())) {
                        if (listLT.get(i).getPOLE_NO().equalsIgnoreCase(fromPoleNo)) {
                            latitude = listLT.get(i).getLATITUDE();
                            longitude = listLT.get(i).getLONGITUDE();
                            System.out.println("FROM_POLE_LAT" + latitude);
                            System.out.println("FROM_POLE_Long" + longitude);
                            break; // stop once found
                        }
                    }
                }
            }

            if (DATA_MODEL.getLATITUDE() != null && DATA_MODEL.getLONGITUDE() != null && latitude != null && longitude != null) {
                double distance_prev = getDistanceBetweenPoles33(DATA_MODEL.getLATITUDE(), DATA_MODEL.getLONGITUDE(), Double.parseDouble(latitude), Double.parseDouble(longitude));
                long roundedDistance_prev = Math.round(distance_prev);
                last_pole_distance_prv.setText("" + roundedDistance_prev);
            }

            // showCustomEditDialog(BTN_VALUE);
            layout_sp_pole_height_33.setVisibility(View.GONE);
            layout_sp_pole_type_33.setVisibility(View.GONE);
            layout_pole_support_33kv.setVisibility(View.GONE);
            layout_sp_pole_cut_pin_point_33kv.setVisibility(View.GONE);
            layout_pole_concrete_33kv.setVisibility(View.GONE);
            layout_pole_structure_33kv.setVisibility(View.GONE);

            layout_connecting_point.setVisibility(View.VISIBLE);
            layout_source_pole.setVisibility(View.VISIBLE);
        } else if (TYPE.equalsIgnoreCase("POLE")) {
            layout_pole_details.setVisibility(View.VISIBLE);
        } else if (TYPE.equalsIgnoreCase("HTTVR")) {
            layout_httr_pole_details.setVisibility(View.VISIBLE);
            getCapacitiesForHttvr();
        }


        ch_33kv_8_stuart_poles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_33kv_8_stuart_poles.setChecked(true);
                    sp_no_8_poles_33kv.setVisibility(View.VISIBLE);
                    tv_select_8m_pole_no_33kv.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_33_8_pole_support.setVisibility(View.GONE);
                    loadPoleHeightDataStrut8M33kv("8");

                } else {
                    ch_33kv_8_stuart_poles.setChecked(false);
                    sp_no_8_poles_33kv.setVisibility(View.GONE);
                    tv_select_8m_pole_no_33kv.setVisibility(View.GONE);
                    layout_sp_pole_desc_33_8_pole_support.setVisibility(View.GONE);


                }
            }
        });

        ch_33kv_9_stuart_poles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_33kv_9_stuart_poles.setChecked(true);
                    sp_no_9_poles_33kv.setVisibility(View.VISIBLE);
                    tv_select_9m_pole_no_33kv.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_33_pole_support.setVisibility(View.GONE);
                    loadPoleHeightDataStrut9M33kv("9.1");


                } else {
                    ch_33kv_9_stuart_poles.setChecked(false);
                    sp_no_9_poles_33kv.setVisibility(View.GONE);
                    tv_select_9m_pole_no_33kv.setVisibility(View.GONE);
                    layout_sp_pole_desc_33_pole_support.setVisibility(View.GONE);


                }
            }
        });

        //23-12-24 Chandu Changes
        ch_33kv_pole_desc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_33kv_pole_desc.setChecked(true);
                    loadPoleHeightData(sp_pole_height_33.getSelectedItem().toString(), sp_pole_type_33.getSelectedItem().toString().trim());
                    //layout_sp_pole_desc_33.setVisibility(View.VISIBLE);

                } else {
                    ch_33kv_pole_desc.setChecked(false);
                    layout_sp_pole_desc_33.setVisibility(View.GONE);


                }
            }
        });
        if (MAX_LENGTH != null && !MAX_LENGTH.trim().isEmpty()) {
            try {
                distanceInFloat = Double.parseDouble(MAX_LENGTH.trim());
                //    Log.d("DISTANCE_IN_FLOAT", "onCreate: " + distanceInFloat);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            // Log.e("DISTANCE_IN_FLOAT", "max_length is either null or empty");
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE.equalsIgnoreCase("SOURCE_POLE")) {

                    if (!sp_connecting_point.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        if (BTN_VALUE.equalsIgnoreCase("33KV")) {
                            String poleHeight = "";
                            String poleType = "";
                            String poleConfig = DATA_MODEL.getPOLE_CONFIG();
                            String lineType = DATA_MODEL.getLINE_TYPE();
                            String voltageLevel = DATA_MODEL.getVOLTAGE_LEVEL();

                            // This looks reversed in your code – logging to check
                            //   boolean isRoadCrossing = DATA_MODEL.getCROSSING_TYPE().isEmpty();

                            System.out.println("---- DEBUG START ----");
                            System.out.println("BTN_VALUE: " + BTN_VALUE);
                            System.out.println("lineType: " + lineType);
                            System.out.println("voltageLevel: " + voltageLevel);
                            System.out.println("poleConfig: " + poleConfig);
                            System.out.println("poleType from DATA_MODEL: " + DATA_MODEL.getPOLE_TYPE());
                            System.out.println("poleHeight from DATA_MODEL: " + DATA_MODEL.getPOLE_HEIGHT());
                            System.out.println("CROSSING_TYPE: " + DATA_MODEL.getCROSSING_TYPE());
                            System.out.println("isRoadCrossing (calculated): " + "isRoadCrossing");

                            if (lineType.equalsIgnoreCase("OVERHEAD") &&
                                    !poleConfig.equalsIgnoreCase("EHT SS-33KV BAY") &&
                                    !poleConfig.equalsIgnoreCase("33/11KV SS-33KV BAY") &&
                                    !poleConfig.equalsIgnoreCase("33/11KV SS")) {
                                poleHeight = DATA_MODEL.getPOLE_HEIGHT();
                                poleType = DATA_MODEL.getPOLE_TYPE();
                            }

                            if (lineType.equalsIgnoreCase("UNDERGROUND") &&
                                    !poleConfig.equalsIgnoreCase("UG Cable Route Marker") &&
                                    !poleConfig.equalsIgnoreCase("RMU") &&
                                    !poleConfig.equalsIgnoreCase("33/11KV SS-33KV BAY") &&
                                    !poleConfig.equalsIgnoreCase("EHT SS-33KV BAY") &&
                                    !poleConfig.equalsIgnoreCase("33/11KV SS")) {
                                poleHeight = DATA_MODEL.getPOLE_HEIGHT();
                                poleType = DATA_MODEL.getPOLE_TYPE();
                            }

                            if (!poleConfig.equalsIgnoreCase("EHT SS-33KV BAY") &&
                                    !poleConfig.equalsIgnoreCase("33/11KV SS-33KV BAY")) {

                                List<PoleDistanceModel> model = null;

                                if (lineType.equalsIgnoreCase("UNDERGROUND")) {
                                    model = databaseEstimates.getPoleDistanceDetailsUnderground(lineType, voltageLevel);
                                } else {
                                    if (!poleType.equalsIgnoreCase("TOWER")) {
                                        model = databaseEstimates.getPoleDistanceDetails(
                                                lineType,
                                                poleType,
                                                poleHeight,
                                                voltageLevel,
                                                "NO"
                                        );
                                    } else {
                                        model = databaseEstimates.getPoleDistanceDetailsTowerPole(lineType, voltageLevel, poleType);
                                    }
                                }

                                // Debug model size
                                System.out.println("PoleDistanceModel list null? " + (model == null));
                                System.out.println("PoleDistanceModel size: " + (model != null ? model.size() : 0));
                                System.out.println("distance: " + distance + " | distanceInFloat: " + distanceInFloat);

                                if (model == null || model.isEmpty()) {
                                    if (distance > distanceInFloat) {
                                        System.out.println("Condition met: model empty/null AND distance > distanceInFloat");
                                        runOnUiThread(() -> showDistanceWarningDialog(distance)); // ensure on UI thread
                                        return;
                                    } else {
                                        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                                            callEditSourcePoleAPI();
                                        } else {
                                            CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                                        }

                                        System.out.println("Distance not greater than distanceInFloat");
                                    }
                                } else {
                                    int max = model.get(0).getMAX_DISTANCE();
                                    System.out.println("Max distance from DB: " + max);
                                    if (distance > max) {
                                        System.out.println("Condition met: distance > max");
                                        runOnUiThread(() -> showDistanceWarningForTowerDialog(distance, max));
                                        return;
                                    } else {
                                        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                                            callEditSourcePoleAPI();
                                        } else {
                                            CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                                        }
                                        System.out.println("Distance not greater than max");
                                    }
                                }
                            }

                            System.out.println("---- DEBUG END ----");
                        } else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
                            String poleHeight = "";
                            String poleType = "";
                            String poleConfig = DATA_MODEL.getPOLE_CONFIG();
                            String lineType = DATA_MODEL.getLINE_TYPE();
                            String voltageLevel = DATA_MODEL.getVOLTAGE_LEVEL();
                            //boolean isRoadCrossing = ch_oh_road_crossing.isChecked();

                            if (lineType.equalsIgnoreCase("OVERHEAD") && !poleConfig.equalsIgnoreCase("33/11KV SS-11KV BAY")) {
                                poleHeight = DATA_MODEL.getPOLE_HEIGHT();
                                poleType = DATA_MODEL.getPOLE_TYPE();
                            }

                            //25-02-2025 Chandu Changes
                            if (lineType.equalsIgnoreCase("UNDERGROUND") && !poleConfig.equalsIgnoreCase("UG Cable Route Marker")
                                    && !poleConfig.equalsIgnoreCase("RMU") && !poleConfig.equalsIgnoreCase("33/11KV SS-11KV BAY")) {
                                poleHeight = DATA_MODEL.getPOLE_HEIGHT();
                                poleType = DATA_MODEL.getPOLE_TYPE();
                            }


                            if (!poleConfig.equalsIgnoreCase("33/11KV SS-11KV BAY")) {
                                List<PoleDistanceModel> model = null;

                                if (lineType.equalsIgnoreCase("UNDERGROUND")) {
                                    model = databaseEstimates.getPoleDistanceDetailsUnderground(lineType, voltageLevel);
                                } else {
                                    if (!poleType.equalsIgnoreCase("TOWER")) {
                                        model = databaseEstimates.getPoleDistanceDetails(lineType, poleType, poleHeight, voltageLevel, "NO");
                                    } else {
                                        model = databaseEstimates.getPoleDistanceDetailsTowerPole(lineType, voltageLevel, poleType);

                                    }
                                }

                                if (model == null || model.isEmpty()) {
                                    if (distance > distanceInFloat) {
                                        System.out.println("Condition met: model empty/null AND distance > distanceInFloat");
                                        runOnUiThread(() -> showDistanceWarningDialog(distance)); // ensure on UI thread
                                        return;
                                    } else {
                                        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                                            callEditSourcePoleAPI();
                                        } else {
                                            CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                                        }
                                        System.out.println("Distance not greater than distanceInFloat");
                                    }
                                } else {
                                    int max = model.get(0).getMAX_DISTANCE();
                                    System.out.println("Max distance from DB: " + max);
                                    if (distance > max) {
                                        System.out.println("Condition met: distance > max");
                                        runOnUiThread(() -> showDistanceWarningForTowerDialog(distance, max));
                                        return;
                                    } else {
                                        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                                            callEditSourcePoleAPI();
                                        } else {
                                            CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                                        }
                                    }
                                }

                            }

                        } else {
                            String poleHeight = "";
                            String poleType = "";
                            String poleConfig = "";
                            String lineType = DATA_MODEL.getLINE_TYPE();
                            String voltageLevel = DATA_MODEL.getVOLTAGE_LEVEL();

                            if (lineType.equalsIgnoreCase("UNDERGROUND")) {
                                poleConfig = DATA_MODEL.getPOLE_CONFIG();
                            }

                            if (lineType.equalsIgnoreCase("OVERHEAD")) {
                                poleHeight = DATA_MODEL.getPOLE_HEIGHT();
                                poleType = DATA_MODEL.getPOLE_TYPE();
                            }
                            if (lineType.equalsIgnoreCase("UNDERGROUND") && !poleConfig.equalsIgnoreCase("UG Cable Marker")) {
                                poleHeight = DATA_MODEL.getPOLE_HEIGHT();
                                poleType = DATA_MODEL.getPOLE_TYPE();
                            }


                            List<PoleDistanceModel> model = null;

                            if (lineType.equalsIgnoreCase("UNDERGROUND")) {
                                model = databaseEstimates.getPoleDistanceDetailsUnderground(lineType, voltageLevel);
                            } else {
                                if (!poleType.equalsIgnoreCase("TOWER")) {
                                    model = databaseEstimates.getPoleDistanceDetails(lineType, poleType, poleHeight, voltageLevel, "NO");
                                } else {
                                    model = databaseEstimates.getPoleDistanceDetailsTowerPole(lineType, voltageLevel, poleType);
                                }
                            }


                            // Check if model is null or empty before accessing
                            if (model == null || model.isEmpty()) {
                                if (distance > distanceInFloat) {
                                    showDistanceWarningDialog(distance);
                                    return;
                                } else {
                                    if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                                        callEditSourcePoleAPI();
                                    } else {
                                        CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                                    }
                                }
                            } else {
                                int max = model.get(0).getMAX_DISTANCE();
                                if (distance > max) {
                                    showDistanceWarningForTowerDialog(distance, max);
                                    return;
                                } else {
                                    if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                                        callEditSourcePoleAPI();
                                    } else {
                                        CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(EditPoleDetailsActivity.this, "Please select source location", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (TYPE.equalsIgnoreCase("POLE")) {
                    validateFields();
                } else if (TYPE.equalsIgnoreCase("HTTVR")) {
                    validateHttvrFields();


                }

            }
        });

    }

    private void getPreviousPoleDetails(String locationId, String gisId) {
        loader_layout.setVisibility(View.VISIBLE);
        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", gisId);
        entry.addProperty("LOCATION_ID", locationId);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.htpoleDet(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                JsonObject surveyedResponseVo = response.body();
                if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String status = surveyedResponseVo.get("STATUS").getAsString();
                    if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                        JsonObject dataObj = surveyedResponseVo.getAsJsonObject("DATA");
                        String SURVEY_TYPE = dataObj.has("SERVICE_TYPE") && !dataObj.get("SERVICE_TYPE").isJsonNull()
                                ? dataObj.get("SERVICE_TYPE").getAsString()
                                : null;

                        String REG_NUM = dataObj.has("REG_NUM") && !dataObj.get("REG_NUM").isJsonNull()
                                ? dataObj.get("REG_NUM").getAsString()
                                : null;

                        String CTPT = dataObj.has("CTPT") && !dataObj.get("CTPT").isJsonNull()
                                ? dataObj.get("CTPT").getAsString()
                                : null;

                        String HTSC_NO = dataObj.has("HTSC_NO") && !dataObj.get("HTSC_NO").isJsonNull()
                                ? dataObj.get("HTSC_NO").getAsString()
                                : null;

                        List<String> natureOfService = new ArrayList<>();
                        if (SURVEY_TYPE!=null){
                            if (SURVEY_TYPE.equalsIgnoreCase("LT Service")) {
                                natureOfService.add("LT Service");
                                natureOfService.add("HT Service");
                            } else {
                                natureOfService.add("HT Service");
                                natureOfService.add("LT Service");
                            }
                        }else {
                            natureOfService.add("LT Service");
                            natureOfService.add("HT Service");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditPoleDetailsActivity.this, android.R.layout.simple_dropdown_item_1line, natureOfService);
                        sp_type_of_nature_11kv.setAdapter(adapter);

                        List<String> allCtptValues = Arrays.asList("5/5A", "10/5A", "20/5A", "40/5A", "60/5A", "75/5A");

                        List<String> model = new ArrayList<>();

                        if (CTPT != null && allCtptValues.contains(CTPT)) {
                            model.add(CTPT);
                            for (String val : allCtptValues) {
                                if (!val.equalsIgnoreCase(CTPT)) {
                                    model.add(val);
                                }
                            }
                        } else {
                            // FOR OLD RECORDS CTPT IS NULL SO THAT WE ARE ADDING SAME VALUES
                            model.addAll(allCtptValues);
                        }

                        ArrayAdapter<String> adapterCTPT = new ArrayAdapter<>(
                                EditPoleDetailsActivity.this,
                                android.R.layout.simple_dropdown_item_1line,
                                model
                        );
                        sp_capacity_httvr.setAdapter(adapterCTPT);
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("HT Service")) {
                                if (REG_NUM != null && !REG_NUM.isEmpty()) {
                                    sp_service_released_11kv.setSelection(2, true);
                                    et_service_released_11kv.setText(REG_NUM);


                                } else {

                                }
                            }

                        }

                    }
                } else {
                    openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout.setVisibility(View.GONE);

            }
        });

    }

    private void setPoleStructure() {
        List<String> ugspunList = new ArrayList<>();
        if (DATA_MODEL.getPOLE_STRUCTURE().equalsIgnoreCase("SINGLE-POLE")) {
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("FOUR-POLE");
        } else if (DATA_MODEL.getPOLE_STRUCTURE().equalsIgnoreCase("DOUBLE-POLE")) {
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("FOUR-POLE");
        } else if (DATA_MODEL.getPOLE_STRUCTURE().equalsIgnoreCase("FOUR-POLE")) {
            ugspunList.add("FOUR-POLE");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("DOUBLE-POLE");

        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_structure_33kv.setAdapter(adapterPoleConfig11kv);
    }

    private void setPoleConcreatSpinner() {
        List<String> ugspunList = new ArrayList<>();
        if (DATA_MODEL.getPOLE_CONCRETE().equalsIgnoreCase("Yes")) {
            ugspunList.add("Yes");
            ugspunList.add("No");
        } else {
            ugspunList.add("No");
            ugspunList.add("Yes");
        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_concrete_33kv.setAdapter(adapterPoleConfig11kv);
    }

    private void setPoleSupportSpinner() {
        List<String> ugspunList = new ArrayList<>();
        if (DATA_MODEL.getPOLE_SUPPORT().equalsIgnoreCase("None")) {
            ugspunList.add("NONE");
            ugspunList.add("STRUT");
            ugspunList.add("STAY");
            ugspunList.add("STRUT AND STAY");
        } else if (DATA_MODEL.getPOLE_SUPPORT().equalsIgnoreCase("STRUT")) {
            ugspunList.add("STRUT");
            ugspunList.add("STAY");
            ugspunList.add("STRUT AND STAY");
            ugspunList.add("NONE");
            List<String> count9M = new ArrayList<>();
            ch_33kv_9_stuart_poles.setChecked(true);
            tv_select_9m_pole_no_33kv.setVisibility(View.VISIBLE);
            sp_no_9_poles_33kv.setVisibility(View.VISIBLE);

            // Add numbers 1–4
            count9M.add("1");
            count9M.add("2");
            count9M.add("3");
            count9M.add("4");

            // Move the selected value (NUMB_9M_STRUTS) to the first position
            String selected9M = String.valueOf(DATA_MODEL.getNUMB_9M_STRUTS());
            count9M.remove(selected9M);
            count9M.add(0, selected9M);
//            if (DATA_MODEL.getNUMB_9M_STRUTS() > 0) {
//                ch_33kv_9_stuart_poles.setChecked(true);
//                tv_select_9m_pole_no_33kv.setVisibility(View.VISIBLE);
//                sp_no_9_poles_33kv.setVisibility(View.VISIBLE);
//
//                // Add numbers 1–4
//                count9M.add("1");
//                count9M.add("2");
//                count9M.add("3");
//                count9M.add("4");
//
//                // Move the selected value (NUMB_9M_STRUTS) to the first position
//                String selected = String.valueOf(DATA_MODEL.getNUMB_9M_STRUTS());
//                count9M.remove(selected);
//                count9M.add(0, selected);
//            }

            ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, count9M);
            sp_no_9_poles_33kv.setAdapter(adapterPoleConfig11kv);

            List<String> count8M = new ArrayList<>();
            ch_33kv_8_stuart_poles.setChecked(true);
            tv_select_8m_pole_no_33kv.setVisibility(View.VISIBLE);
            sp_no_8_poles_33kv.setVisibility(View.VISIBLE);

            // Add numbers 1–4
            count8M.add("1");
            count8M.add("2");
            count8M.add("3");
            count8M.add("4");

            // Move the selected value (NUMB_9M_STRUTS) to the first position
            String selected = String.valueOf(DATA_MODEL.getNUMB_8M_STRUTS());
            count8M.remove(selected);
            count8M.add(0, selected);

//            List<String> count8M = new ArrayList<>();
//            if (DATA_MODEL.getNUMB_8M_STRUTS() > 0) {
//                ch_33kv_8_stuart_poles.setChecked(true);
//                tv_select_8m_pole_no_33kv.setVisibility(View.VISIBLE);
//                sp_no_8_poles_33kv.setVisibility(View.VISIBLE);
//
//                // Add numbers 1–4
//                count8M.add("1");
//                count8M.add("2");
//                count8M.add("3");
//                count8M.add("4");
//
//                // Move the selected value (NUMB_9M_STRUTS) to the first position
//                String selected = String.valueOf(DATA_MODEL.getNUMB_8M_STRUTS());
//                count8M.remove(selected);
//                count8M.add(0, selected);
//            }


            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, count8M);
            sp_no_8_poles_33kv.setAdapter(adapter);
        } else if (DATA_MODEL.getPOLE_SUPPORT().equalsIgnoreCase("STAY")) {

            List<String> STAYCOUNT = new ArrayList<>();
            // Add numbers 1–6
            STAYCOUNT.add("1");
            STAYCOUNT.add("2");
            STAYCOUNT.add("3");
            STAYCOUNT.add("4");
            STAYCOUNT.add("5");
            STAYCOUNT.add("6");

            // Move the selected value (NUMB_9M_STRUTS) to the first position
            String selected = String.valueOf(DATA_MODEL.getNUMB_STAYS());
            STAYCOUNT.remove(selected);
            STAYCOUNT.add(0, selected);
//            if (DATA_MODEL.getNUMB_STAYS() > 0) {
//
//
//                // Add numbers 1–6
//                STAYCOUNT.add("1");
//                STAYCOUNT.add("2");
//                STAYCOUNT.add("3");
//                STAYCOUNT.add("4");
//                STAYCOUNT.add("5");
//                STAYCOUNT.add("6");
//
//                // Move the selected value (NUMB_9M_STRUTS) to the first position
//                String selected = String.valueOf(DATA_MODEL.getNUMB_STAYS());
//                STAYCOUNT.remove(selected);
//                STAYCOUNT.add(0, selected);
//            }

            ArrayAdapter<String> stayAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, STAYCOUNT);
            sp_no_of_stays_33kv.setAdapter(stayAdapter);

            ugspunList.add("STAY");
            ugspunList.add("STRUT");
            ugspunList.add("STRUT AND STAY");
            ugspunList.add("NONE");
        } else if (DATA_MODEL.getPOLE_SUPPORT().equalsIgnoreCase("STRUT AND STAY")) {
            List<String> count9M = new ArrayList<>();
            ch_33kv_9_stuart_poles.setChecked(true);
            tv_select_9m_pole_no_33kv.setVisibility(View.VISIBLE);
            sp_no_9_poles_33kv.setVisibility(View.VISIBLE);

            // Add numbers 1–4
            count9M.add("1");
            count9M.add("2");
            count9M.add("3");
            count9M.add("4");

            // Move the selected value (NUMB_9M_STRUTS) to the first position
            String selected9M = String.valueOf(DATA_MODEL.getNUMB_9M_STRUTS());
            count9M.remove(selected9M);
            //count9M.add(0, selected);
//            if (DATA_MODEL.getNUMB_9M_STRUTS() > 0) {
//                ch_33kv_9_stuart_poles.setChecked(true);
//                tv_select_9m_pole_no_33kv.setVisibility(View.VISIBLE);
//                sp_no_9_poles_33kv.setVisibility(View.VISIBLE);
//
//                // Add numbers 1–4
//                count9M.add("1");
//                count9M.add("2");
//                count9M.add("3");
//                count9M.add("4");
//
//                // Move the selected value (NUMB_9M_STRUTS) to the first position
//                String selected = String.valueOf(DATA_MODEL.getNUMB_9M_STRUTS());
//                count9M.remove(selected);
//                //count9M.add(0, selected);
//            }

            ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, count9M);
            sp_no_9_poles_33kv.setAdapter(adapterPoleConfig11kv);


            List<String> count8M = new ArrayList<>();
            ch_33kv_8_stuart_poles.setChecked(true);
            tv_select_8m_pole_no_33kv.setVisibility(View.VISIBLE);
            sp_no_8_poles_33kv.setVisibility(View.VISIBLE);

            // Add numbers 1–4
            count8M.add("1");
            count8M.add("2");
            count8M.add("3");
            count8M.add("4");

            // Move the selected value (NUMB_9M_STRUTS) to the first position
            String selected = String.valueOf(DATA_MODEL.getNUMB_8M_STRUTS());
            count8M.remove(selected);
            count8M.add(0, selected);
//            if (DATA_MODEL.getNUMB_8M_STRUTS() > 0) {
//                ch_33kv_8_stuart_poles.setChecked(true);
//                tv_select_8m_pole_no_33kv.setVisibility(View.VISIBLE);
//                sp_no_8_poles_33kv.setVisibility(View.VISIBLE);
//
//                // Add numbers 1–4
//                count8M.add("1");
//                count8M.add("2");
//                count8M.add("3");
//                count8M.add("4");
//
//                // Move the selected value (NUMB_9M_STRUTS) to the first position
//                String selected = String.valueOf(DATA_MODEL.getNUMB_8M_STRUTS());
//                count8M.remove(selected);
//                count8M.add(0, selected);
//            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, count8M);
            sp_no_8_poles_33kv.setAdapter(adapter);


            List<String> STAYCOUNT = new ArrayList<>();
            // Add numbers 1–6
            STAYCOUNT.add("1");
            STAYCOUNT.add("2");
            STAYCOUNT.add("3");
            STAYCOUNT.add("4");
            STAYCOUNT.add("5");
            STAYCOUNT.add("6");

            // Move the selected value (NUMB_9M_STRUTS) to the first position
            String selectedStay = String.valueOf(DATA_MODEL.getNUMB_STAYS());
            STAYCOUNT.remove(selectedStay);
            STAYCOUNT.add(0, selectedStay);
//            if (DATA_MODEL.getNUMB_STAYS() > 0) {
//
//
//                // Add numbers 1–6
//                STAYCOUNT.add("1");
//                STAYCOUNT.add("2");
//                STAYCOUNT.add("3");
//                STAYCOUNT.add("4");
//                STAYCOUNT.add("5");
//                STAYCOUNT.add("6");
//
//                // Move the selected value (NUMB_9M_STRUTS) to the first position
//                String selected = String.valueOf(DATA_MODEL.getNUMB_STAYS());
//                STAYCOUNT.remove(selected);
//                STAYCOUNT.add(0, selected);
//            }

            ArrayAdapter<String> stayAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, STAYCOUNT);
            sp_no_of_stays_33kv.setAdapter(stayAdapter);


            ugspunList.add("STRUT AND STAY");
            ugspunList.add("STAY");
            ugspunList.add("STRUT");
            ugspunList.add("NONE");
        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_support_33.setAdapter(adapterPoleConfig11kv);


    }

    private void validateHttvrFields() {
        if (sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please select Nature of service", Toast.LENGTH_SHORT).show();
            return;
        }
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
            if (sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("HT Service")
                    && DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Proposed")) {
                if (sp_service_released_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(this, "Please Select Service Released", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (sp_service_released_11kv.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                        if (sp_ht_service_no_11kv != null && sp_ht_service_no_11kv.getSelectedItem() != null &&
                                !sp_ht_service_no_11kv.getSelectedItem().toString().isEmpty()) {
                            if (sp_ht_service_no_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                                Toast.makeText(this, "Please Select HT Service Number", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                    } else {
                        if (et_service_released_11kv.getText().toString().trim().isEmpty()) {
                            Toast.makeText(this, "Please Enter Service Number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }


            //OLD
//            if (sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("HT Service")) {
//                if (layout_ht_service_no_list.getVisibility() == View.VISIBLE) {
//                    if (sp_ht_service_no_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//                        Toast.makeText(this, "Please select HTSC number", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                }
//            }
        }


        if (sp_capacity_httvr.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please select Capacity", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (DATA_MODEL.getREF_ID() != null && !DATA_MODEL.getREF_ID().isEmpty()) {
                Map<String, String> data = new HashMap<>();
                data.put("LOCATION_ID", DATA_MODEL.getLOCATION_ID());
                data.put("UPDATE_TYPE", "HT-SERVICE");
                data.put("REF_ID", DATA_MODEL.getREF_ID());
                data.put("VOLTAGE_LEVEL", BTN_VALUE);
                data.put("EST_NO", EST_NO);
                data.put("GIS_ID", GIS_ID);
                data.put("SERVICE_TYPE", sp_type_of_nature_11kv.getSelectedItem().toString());
                data.put("CTPT", sp_capacity_httvr.getSelectedItem().toString());
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    if (sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("HT Service")) {
                            if (layout_service_released.getVisibility() == View.VISIBLE &&
                                    sp_service_released_11kv.getSelectedItem().toString().equalsIgnoreCase("No")) {
                                data.put("REG_NUM", et_service_released_11kv.getText().toString().trim());
                            } else {
                                if (layout_ht_service_no_list.getVisibility() == View.VISIBLE) {
                                    //DO NOT SPLIT WITH ~ VALUE BECAUSE FROM BACKEND SIDE THEY ARE VALIDATING
                                    data.put("HTSC_NO", sp_ht_service_no_11kv.getSelectedItem().toString());
                                }
                        }

                    }
                }


                JsonObject jsonObject = new JsonObject();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    jsonObject.addProperty(entry.getKey(), entry.getValue());
                }

                JsonArray jsonArray = new JsonArray();
                jsonArray.add(jsonObject);
                // Retrofit call

                if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                    callEditHttvrPoleApi(jsonArray);
                } else {
                    System.out.println("HTTVR_CHANGING-->");
                    NewModel model = new NewModel();
                    model.setSERVICE_TYPE(sp_type_of_nature_11kv.getSelectedItem().toString());
                    model.setCTPT(sp_capacity_httvr.getSelectedItem().toString());
                    if (layout_ht_service_no_list.getVisibility() == View.VISIBLE) {
                        model.setHTSC_NO(sp_ht_service_no_11kv.getSelectedItem().toString());
                    }
                    model.setVOLTAGE_LEVEL(BTN_VALUE);
                    model.setPOLE_NO(DATA_MODEL.getPOLE_NO());
                    model.setEST_NO(DATA_MODEL.getEST_NO());

                    boolean status;
                    status = databaseEstimates.updateSurveyData(model);
                    if (status) {
                        System.out.println("HTTVR_CHANGING_HAHA-->");
                        MaterialPostingModel postingModel = new MaterialPostingModel();
                        postingModel.setAPP_VERSION(MobileConstants.APP_VERSION);
                        postingModel.setAPP_VERSION2(getResources().getString(R.string.displayVersion));
                        postingModel.setCOUNT(String.valueOf("1"));
                        postingModel.setDATA_ID(DATA_ID);
                        postingModel.setESTIMATE_NO(DATA_MODEL.getEST_NO());
                        postingModel.setGIS_ID(DATA_MODEL.getGIS_ID());
                        postingModel.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
                        postingModel.setLOCATION_NO(DATA_MODEL.getPOLE_NO());
                        postingModel.setMATERIAL_DESC(sp_material_httvr.getSelectedItem().toString());
                        postingModel.setSAP_CODE(sapCodeForHttvrmaterial);
                        postingModel.setREF_ID(referenceCode);
                        postingModel.setTYPE("TRANSMITTED");
                        postingModel.setVOLTAGE_LEVEL(BTN_VALUE);
                        boolean statusMaterial;
                        statusMaterial = databaseEstimates.updateMaterialData(postingModel);
                        if (statusMaterial) {
                            Toast.makeText(this, "Material Added Successfully", Toast.LENGTH_SHORT).show();
                            getOnBackPressedDispatcher().onBackPressed();
                        }

                    }
                }


            } else {
                System.out.println("HTTVR_CHANGING-->");
                NewModel model = new NewModel();
                model.setSERVICE_TYPE(sp_type_of_nature_11kv.getSelectedItem().toString());
                model.setCTPT(sp_capacity_httvr.getSelectedItem().toString());
                if (layout_ht_service_no_list.getVisibility() == View.VISIBLE) {
                    model.setHTSC_NO(sp_ht_service_no_11kv.getSelectedItem().toString());
                }
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    if (sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("HT Service")) {
                        if (sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("HT Service")) {
                            if (layout_service_released.getVisibility() == View.VISIBLE && sp_service_released_11kv.getSelectedItem().toString().equalsIgnoreCase("No")) {
                                model.setREG_NUM(et_service_released_11kv.getText().toString().trim());
                            } else {
                                if (layout_ht_service_no_list.getVisibility() == View.VISIBLE) {
                                    model.setHTSC_NO(sp_ht_service_no_11kv.getSelectedItem().toString());
                                }
                            }

                        }

                    }
                }
                model.setVOLTAGE_LEVEL(BTN_VALUE);
                model.setPOLE_NO(DATA_MODEL.getPOLE_NO());
                model.setEST_NO(DATA_MODEL.getEST_NO());

                boolean status;
                status = databaseEstimates.updateSurveyData(model);


                if (status) {
                    openDialogForCompleteSuccess("HTTVR pole details saved successfully");
                    //  Toast.makeText(this, "Captured details successfully", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    private void updateHttvrMaterial() {
        MaterialPostingModel postingModel = new MaterialPostingModel();
        postingModel.setAPP_VERSION(MobileConstants.APP_VERSION);
        postingModel.setAPP_VERSION2(getResources().getString(R.string.displayVersion));
        postingModel.setCOUNT("1");  // directly use "1"
        postingModel.setDATA_ID(DATA_ID);
        postingModel.setESTIMATE_NO(DATA_MODEL.getEST_NO());
        postingModel.setGIS_ID(DATA_MODEL.getGIS_ID());
        postingModel.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
        postingModel.setLOCATION_NO(DATA_MODEL.getPOLE_NO());
        postingModel.setMATERIAL_DESC(sp_material_httvr.getSelectedItem().toString());
        postingModel.setSAP_CODE(sapCodeForHttvrmaterial);
        postingModel.setSURVEY_CODE(survey_code);
        postingModel.setREF_ID(referenceCode);
        postingModel.setTYPE("TRANSMITTED");
        System.out.println("SURVEY_CODE" + survey_code);
        postingModel.setVOLTAGE_LEVEL(BTN_VALUE);
        postingModel.setMOD_TYPE("EDIT_HT");



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
                Toast.makeText(EditPoleDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void callEditHttvrPoleApi(JsonArray jsonArray) {
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        loader_layout.setVisibility(View.VISIBLE);
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
                            updateHttvrMaterial();
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

    private void callEditSourcePoleAPI() {
        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
            if (DATA_MODEL.getREF_ID() != null && !DATA_MODEL.getREF_ID().isEmpty()) {
                loader_layout.setVisibility(View.VISIBLE);
                Map<String, String> data = new HashMap<>();
                data.put("LOCATION_ID", DATA_MODEL.getLOCATION_ID());
                data.put("UPDATE_TYPE", "SOURCE");
                data.put("REF_ID", DATA_MODEL.getREF_ID());
                data.put("VOLTAGE_LEVEL", BTN_VALUE);
                data.put("EST_NO", EST_NO);
                data.put("GIS_ID", GIS_ID);
                data.put("SOURCE_POLE", sp_connecting_point.getSelectedItem().toString());
                List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
                JsonObject jsonObject = new JsonObject();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    jsonObject.addProperty(entry.getKey(), entry.getValue());
                }

                JsonArray jsonArray = new JsonArray();
                jsonArray.add(jsonObject);
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
                                    Toast.makeText(EditPoleDetailsActivity.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EditPoleDetailsActivity.this, SurveyList.class);
                                    startActivity(intent);
                                    finish();
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
            } else {
                System.out.println("SOURCE_POLE_CHANGING-->");
                NewModel model = new NewModel();
                model.setFROM_POLE(sp_connecting_point.getSelectedItem().toString());
                model.setVOLTAGE_LEVEL(BTN_VALUE);
                model.setPOLE_NO(DATA_MODEL.getPOLE_NO());
                model.setEST_NO(DATA_MODEL.getEST_NO());
                boolean status;
                status = databaseEstimates.updateSurveyData(model);

                if (status) {
                    openDialogForCompleteSuccess("Source pole saved successfully");
                    //  Toast.makeText(this, "Captured details successfully", Toast.LENGTH_SHORT).show();

                }
            }
        } else {
            NewModel model = new NewModel();
            model.setFROM_POLE(sp_connecting_point.getSelectedItem().toString());
            boolean status;
            status = databaseEstimates.updateSurveyData(model);

            if (status) {
                openDialogForCompleteSuccess("Source pole saved successfully");

            }
        }


    }

    private void showDistanceWarningForTowerDialog(float distance, int maxDistance) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("Warning!");
        int roundedDistance = Math.round(distance);

        SpannableString spannable1 = new SpannableString("Length from source pole = " + roundedDistance + "m\n");
        spannable1.setSpan(new StyleSpan(Typeface.BOLD), 0, spannable1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable1.setSpan(new ForegroundColorSpan(Color.RED), 0, spannable1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //SpannableString spannable2 = new SpannableString("Span length  for TOWER should be below" + " " + maxDistance + "M");
        SpannableString spannable2 = new SpannableString("Pole to Pole Span should be less than" + " " + maxDistance + "M");
        spannable2.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannable2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharSequence message = android.text.TextUtils.concat(spannable1, spannable2);

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();


            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDistanceWarningDialog(float distance) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("Warning!");
        int roundedDistance = Math.round(distance);

        SpannableString spannable1 = new SpannableString("Length from source pole = " + roundedDistance + "m\n");
        spannable1.setSpan(new StyleSpan(Typeface.BOLD), 0, spannable1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable1.setSpan(new ForegroundColorSpan(Color.RED), 0, spannable1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannable2 = new SpannableString("Pole to Pole Span should be less than" + " " + MAX_LENGTH + "M");
        spannable2.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannable2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharSequence message = android.text.TextUtils.concat(spannable1, spannable2);

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setPinCutForLT() {
        List<String> polePin = new ArrayList<>();
        if (BTN_VALUE.equalsIgnoreCase("LT")) {
            if (DATA_MODEL.getPIN_CUT().equalsIgnoreCase("PIN POINT")) {
                polePin.add("PIN POINT");
                polePin.add("CUT POINT");
            } else {
                polePin.add("CUT POINT");
                polePin.add("PIN POINT");
            }

        } else if (BTN_VALUE.equalsIgnoreCase("33KV") || BTN_VALUE.equalsIgnoreCase("11KV")) {
            if (DATA_MODEL.getPIN_CUT().equalsIgnoreCase("PINPOINT")) {
                polePin.clear();
                polePin.add("PINPOINT");
                polePin.add("CUTPOINT");
            } else {
                polePin.add("CUTPOINT");
                polePin.add("PINPOINT");
            }

        }

        ArrayAdapter<String> adapterPoleCon = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, polePin);
        sp_pole_pin_cut_point_33kv.setAdapter(adapterPoleCon);
    }

    private double getDistanceBetweenPoles33(String polLat, String polLon, double fromLat, double fromLon) {
        System.out.println("CURRENT LOCATION LAT : " + fromLat);
        System.out.println("CURRENT LOCATION LON : " + fromLon);
        System.out.println("POLE LOCATION LAT : " + polLat);
        System.out.println("POLE LOCATION LON : " + polLon);
        Location currentStartPoint = new Location("locationM");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationM");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        System.out.println("POLE LOCATION DISTANCE : " + currentDistance);
        return currentDistance;
    }

    private void setSourceLocation() {
        if (BTN_VALUE.equalsIgnoreCase("33KV")) {
            if (list33.isEmpty()) {
                layout_connecting_point.setVisibility(View.GONE);
            } else {
                layout_connecting_point.setVisibility(View.VISIBLE);
                List<String> list_pst = new ArrayList<>();

                List<String> nearPoles = new ArrayList<>();

                String currentLat = DATA_MODEL.getLATITUDE();
                String currentLon = DATA_MODEL.getLONGITUDE();

                for (int i = 0; i < list33.size(); i++) {
                    if (WorkInvolmentsActivity.CLICKED_FEEDER_ID.equalsIgnoreCase(list33.get(i).getFEEDER_ID())) {

                        String poleNo = list33.get(i).getPOLE_NO();

                        // skip same pole
                        if (poleNo != null && poleNo.equalsIgnoreCase(DATA_MODEL.getPOLE_NO())) {
                            continue;
                        }
                        String poleLat = list33.get(i).getLATITUDE();
                        String poleLon = list33.get(i).getLONGITUDE();
                        if (poleLat != null && poleLon != null) {

                            double distance = getDistanceBetweenPoles33(
                                    currentLat,
                                    currentLon,
                                    Double.parseDouble(poleLat),
                                    Double.parseDouble(poleLon)
                            );

                            long roundedDistance = Math.round(distance);

                            System.out.println("ROUNDED_DISTANCE----->" + roundedDistance);

                            // collect poles within 120 meters
                            if (roundedDistance <= 120) {
                                nearPoles.add(list33.get(i).getPOLE_NO());
                            }
                        }
                        // collect all poles
                        list_pst.add(list33.get(i).getPOLE_NO());
                    }
                }
                list_pst.add(0, "0");
                list_pst.add(0, "Select");

                //list_pst.add(0, "0");


                if (IS_EDIT) {
                    list_pst.remove(DATA_MODEL.getFROM_POLE());
                    list_pst.add(DATA_MODEL.getFROM_POLE());
                    list_pst.remove(DATA_MODEL.getPOLE_NO());
                }
                ArrayAdapter<String> adapter_pst = null;

                if (!nearPoles.isEmpty()) {
                    adapter_pst = new ArrayAdapter<String>(EditPoleDetailsActivity.this, android.R.layout.simple_dropdown_item_1line, nearPoles);
                    sp_connecting_point.setAdapter(adapter_pst);
                } else {
                    adapter_pst = new ArrayAdapter<String>(EditPoleDetailsActivity.this, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_connecting_point.setAdapter(adapter_pst);
                }



            }
        }
        else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
            if (list11.isEmpty()) {

                layout_connecting_point.setVisibility(View.GONE);
            } else {
                List<String> list_pst = new ArrayList<>();
                List<String> nearPoles = new ArrayList<>();

                String currentLat = DATA_MODEL.getLATITUDE();
                String currentLon = DATA_MODEL.getLONGITUDE();


                for (int i = 0; i < list11.size(); i++) {
                    if (WorkInvolmentsActivity.CLICKED_FEEDER_ID.equalsIgnoreCase(list11.get(i).getFEEDER_ID())) {

                        String poleNo = list11.get(i).getPOLE_NO();

                        // skip same pole
                        if (poleNo != null && poleNo.equalsIgnoreCase(DATA_MODEL.getPOLE_NO())) {
                            continue;
                        }
                        String poleLat = list11.get(i).getLATITUDE();
                        String poleLon = list11.get(i).getLONGITUDE();


                        if (poleLat != null && poleLon != null) {

                            double distance = getDistanceBetweenPoles33(
                                    currentLat,
                                    currentLon,
                                    Double.parseDouble(poleLat),
                                    Double.parseDouble(poleLon)
                            );

                            long roundedDistance = Math.round(distance);

                            System.out.println("ROUNDED_DISTANCE----->" + roundedDistance);

                            // collect poles within 120 meters
                            if (roundedDistance <= 120) {
                                nearPoles.add(list11.get(i).getPOLE_NO());
                            }
                        }
                        // collect all poles
                        list_pst.add(list11.get(i).getPOLE_NO());


                    }
                }

                if (list_pst.size() > 0) {
                    layout_connecting_point.setVisibility(View.VISIBLE);
                    list_pst.add(0, "0");
                    list_pst.add(0, "Select");

                    if (IS_EDIT) {
                        list_pst.remove(DATA_MODEL.getFROM_POLE());
                        list_pst.add(DATA_MODEL.getFROM_POLE());
                        //NEW DTR CHANGES BY CHANDU
                        list_pst.remove(DATA_MODEL.getPOLE_NO());
                    }
                    Collections.reverse(list_pst);
                    ArrayAdapter<String> adapter_pst = null;
                    if (!nearPoles.isEmpty()) {
                        adapter_pst = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nearPoles);
                        sp_connecting_point.setAdapter(adapter_pst);
                    } else {
                        adapter_pst = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_connecting_point.setAdapter(adapter_pst);
                    }


                    // sp_connecting_point.setSelection(0, true);

                    String fromPole = DATA_MODEL.getFROM_POLE();
                    if (fromPole != null && !fromPole.isEmpty()) {
                        int position = adapter_pst.getPosition(fromPole);
                        if (position >= 0) {
                            sp_connecting_point.post(() -> sp_connecting_point.setSelection(position));
                        }
                    }

                }
            }
        }
        else {
            if (listLT.isEmpty()) {
                layout_connecting_point.setVisibility(View.GONE);
            } else {
                List<String> list_pst = new ArrayList<>();
                List<String> nearPoles = new ArrayList<>();

                String currentLat = DATA_MODEL.getLATITUDE();
                String currentLon = DATA_MODEL.getLONGITUDE();


                for (int i = 0; i < list11.size(); i++) {
                    if (list11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                        String poleNo = list11.get(i).getPOLE_NO();

                        // skip same pole
                        if (poleNo != null && poleNo.equalsIgnoreCase(DATA_MODEL.getPOLE_NO())) {
                            continue;
                        }
                        String poleLat = list11.get(i).getLATITUDE();
                        String poleLon = list11.get(i).getLONGITUDE();

                        if (poleLat != null && poleLon != null) {

                            double distance = getDistanceBetweenPoles33(
                                    currentLat,
                                    currentLon,
                                    Double.parseDouble(poleLat),
                                    Double.parseDouble(poleLon)
                            );

                            long roundedDistance = Math.round(distance);

                            System.out.println("ROUNDED_DISTANCE----->" + roundedDistance);

                            // collect poles within 120 meters
                            if (roundedDistance <= 120) {
                                nearPoles.add(list11.get(i).getPOLE_NO());
                            }
                        }
                    }
                }

                for (int i = 0; i < listLT.size(); i++) {
                    if (DATA_MODEL.getLT_AVAILABLE().equalsIgnoreCase(listLT.get(i).getLT_AVAILABLE())) {
                        String poleNo = listLT.get(i).getPOLE_NO();

                        // skip same pole
                        if (poleNo != null && poleNo.equalsIgnoreCase(DATA_MODEL.getPOLE_NO())) {
                            continue;
                        }
                        String poleLat = listLT.get(i).getLATITUDE();
                        String poleLon = listLT.get(i).getLONGITUDE();

                        if (poleLat != null && poleLon != null) {

                            double distance = getDistanceBetweenPoles33(
                                    currentLat,
                                    currentLon,
                                    Double.parseDouble(poleLat),
                                    Double.parseDouble(poleLon)
                            );

                            long roundedDistance = Math.round(distance);

                            System.out.println("ROUNDED_DISTANCE----->" + roundedDistance);

                            // collect poles within 120 meters
                            if (roundedDistance <= 120) {
                                nearPoles.add(listLT.get(i).getPOLE_NO());
                            }
                        }

                    }

                }
                list_pst.add(0, "0");
                list_pst.add(0, "Select");

                if (IS_EDIT) {
                    list_pst.remove(DATA_MODEL.getFROM_POLE());
                    list_pst.add(DATA_MODEL.getFROM_POLE());
                    //NEW DTR CHANGES BY CHANDU
                    list_pst.remove(DATA_MODEL.getPOLE_NO());
                }


                Collections.reverse(list_pst);
                ArrayAdapter<String> adapter_pst = null;
                if (!nearPoles.isEmpty()) {
                    adapter_pst = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nearPoles);
                    sp_connecting_point.setAdapter(adapter_pst);
                } else {
                    adapter_pst = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_connecting_point.setAdapter(adapter_pst);
                }
            }
        }
    }

    private void validateFields() {
        String poleHeight = "";
        String poleType = sp_pole_type_33.getSelectedItem().toString().trim();
        if (layout_sp_pole_height_33.getVisibility() == View.VISIBLE) {
            poleHeight = sp_pole_height_33.getSelectedItem().toString().trim();
        }

        String poleSupport = sp_pole_support_33.getSelectedItem().toString().trim().toUpperCase();
        String pinCut = sp_pole_pin_cut_point_33kv.getSelectedItem().toString().trim();
        String poleConcrete = sp_pole_concrete_33kv.getSelectedItem().toString().trim();
        String poleStructure = sp_pole_structure_33kv.getSelectedItem().toString().trim();

        if (poleType.equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select Pole type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (poleHeight.equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select Pole height", Toast.LENGTH_SHORT).show();
            return;
        }
        if (poleSupport.equalsIgnoreCase("SELECT")) {
            Toast.makeText(this, "Select Pole support", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check strut/stay combinations
        if (poleSupport.contains("STRUT") || poleSupport.contains("STURT")) {
            if (poleSupport.contains("STAY")) {
                // Case: STRUT/STURT + STAY
                if (!ch_33kv_9_stuart_poles.isChecked() && !ch_33kv_8_stuart_poles.isChecked()) {
                    Toast.makeText(this, "Please select pole support pole type", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                // Case: STRUT/STURT only
                if (!ch_33kv_9_stuart_poles.isChecked() && !ch_33kv_8_stuart_poles.isChecked()) {
                    Toast.makeText(this, "Please select strut pole type", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (pinCut.equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select Pin cut", Toast.LENGTH_SHORT).show();
            return;
        }
        if (poleConcrete.equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select Pole concrete", Toast.LENGTH_SHORT).show();
            return;
        }
        if (poleStructure.equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Select Pole structure", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validateDistance()) {
            return;
        }


        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
            if (DATA_MODEL.getREF_ID() != null && !DATA_MODEL.getREF_ID().isEmpty()) {
                updateDTRDetails();
            } else {
                NewModel model = new NewModel();
                model.setPOLE_TYPE(sp_pole_type_33.getSelectedItem().toString());
                model.setPOLE_HEIGHT(sp_pole_height_33.getSelectedItem().toString());
                model.setPOLE_SUPPORT(sp_pole_support_33.getSelectedItem().toString());
                model.setPIN_CUT(sp_pole_pin_cut_point_33kv.getSelectedItem().toString());
                model.setPOLE_CONCRETE(sp_pole_concrete_33kv.getSelectedItem().toString());
                model.setPOLE_STRUCTURE(sp_pole_structure_33kv.getSelectedItem().toString());
                model.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
                //model.set("UPDATE_TYPE", "POLE");
                model.setREF_ID(DATA_MODEL.getREF_ID());
                model.setVOLTAGE_LEVEL(BTN_VALUE);
                model.setEST_NO(EST_NO);
                model.setGIS_ID(GIS_ID);
                model.setPOLE_NO(DATA_MODEL.getPOLE_NO());
                if (layout_sp_pole_desc_33.getVisibility() == View.VISIBLE) {
                    model.setPOLE_DESC(sp_sp_pole_desc_33.getSelectedItem().toString());
                }
                if (poleSupport.equals("STAY")) {
                    model.setNUMB_STAYS(Integer.parseInt(sp_no_of_stays_33kv.getSelectedItem().toString()));
                } else if (poleSupport.contains("STRUT") || poleSupport.contains("STURT")) {
                    if (ch_33kv_9_stuart_poles.isChecked()) {
                        model.setNUMB_9M_STRUTS(Integer.parseInt(sp_no_9_poles_33kv.getSelectedItem().toString()));
                    }
                    if (ch_33kv_8_stuart_poles.isChecked()) {
                        model.setNUMB_8M_STRUTS(Integer.parseInt(sp_no_8_poles_33kv.getSelectedItem().toString()));
                    }
                    if (poleSupport.contains("STAY")) {
                        model.setNUMB_STAYS(Integer.parseInt(sp_no_of_stays_33kv.getSelectedItem().toString()));
                    }
                }
                if (BTN_VALUE.equalsIgnoreCase("33KV")) {
                    model.setVOLTAGE_LEVEL("33KV");
                } else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
                    model.setVOLTAGE_LEVEL("11KV");
                } else {
                    model.setVOLTAGE_LEVEL("LT");
                }

                boolean status;
                status = databaseEstimates.updateSurveyData(model);

                if (status) {
                    openDialogForCompleteSuccess("Pole details saved successfully");
                    getOnBackPressedDispatcher().onBackPressed();

                }

            }


        } else {
            NewModel model = new NewModel();
            model.setPOLE_TYPE(sp_pole_type_33.getSelectedItem().toString());
            model.setPOLE_HEIGHT(sp_pole_height_33.getSelectedItem().toString());
            model.setPOLE_SUPPORT(sp_pole_support_33.getSelectedItem().toString());
            model.setPIN_CUT(sp_pole_pin_cut_point_33kv.getSelectedItem().toString());
            model.setPOLE_CONCRETE(sp_pole_concrete_33kv.getSelectedItem().toString());
            model.setPOLE_STRUCTURE(sp_pole_structure_33kv.getSelectedItem().toString());
            model.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
            //model.set("UPDATE_TYPE", "POLE");
            model.setREF_ID(DATA_MODEL.getREF_ID());
            model.setVOLTAGE_LEVEL(BTN_VALUE);
            model.setEST_NO(EST_NO);
            model.setGIS_ID(GIS_ID);
            model.setPOLE_NO(DATA_MODEL.getPOLE_NO());
            boolean status;
            status = databaseEstimates.updateSurveyData(model);

            if (status) {
                openDialogForCompleteSuccess("Pole details saved successfully");
                getOnBackPressedDispatcher().onBackPressed();
            }
            //CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
        }


//        updateDTRDetails();
    }


    private void updateDTRDetails() {
        loader_layout.setVisibility(View.VISIBLE);
        Map<String, String> data = new HashMap<>();
        String poleSupport = sp_pole_support_33.getSelectedItem().toString().trim().toUpperCase();
        data.put("POLE_TYPE", sp_pole_type_33.getSelectedItem().toString());
        data.put("POLE_HEIGHT", sp_pole_height_33.getSelectedItem().toString());
        data.put("POLE_SUPPORT", sp_pole_support_33.getSelectedItem().toString());
        data.put("PIN_CUT", sp_pole_pin_cut_point_33kv.getSelectedItem().toString());
        data.put("POLE_CONCRETE", sp_pole_concrete_33kv.getSelectedItem().toString());
        data.put("POLE_STRUCTURE", sp_pole_structure_33kv.getSelectedItem().toString());
        data.put("LOCATION_ID", DATA_MODEL.getLOCATION_ID());
        data.put("UPDATE_TYPE", "POLE");
        data.put("REF_ID", DATA_MODEL.getREF_ID());
        data.put("VOLTAGE_LEVEL", BTN_VALUE);
        data.put("EST_NO", EST_NO);
        data.put("GIS_ID", GIS_ID);
        data.put("POLE_NO", DATA_MODEL.getPOLE_NO());
        if (layout_sp_pole_desc_33.getVisibility() == View.VISIBLE) {
            data.put("POLE_DESC", sp_sp_pole_desc_33.getSelectedItem().toString());
        }
        if (poleSupport.equals("STAY")) {
            data.put("NUMB_STAYS", sp_no_of_stays_33kv.getSelectedItem().toString());
        } else if (poleSupport.contains("STRUT") || poleSupport.contains("STURT")) {
            if (ch_33kv_9_stuart_poles.isChecked()) {
                data.put("NUMB_9M_STRUTS", sp_no_9_poles_33kv.getSelectedItem().toString());
            }
            if (ch_33kv_8_stuart_poles.isChecked()) {
                data.put("NUMB_8M_STRUTS", sp_no_8_poles_33kv.getSelectedItem().toString());
            }
            if (poleSupport.contains("STAY")) {
                data.put("NUMB_STAYS", sp_no_of_stays_33kv.getSelectedItem().toString());
            }
        }
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);
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
                            Toast.makeText(EditPoleDetailsActivity.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(EditPoleDetailsActivity.this, SurveyList.class);
                            startActivity(intent);
                            finish();
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


    private void openDialogForCompleteSuccess(String msg) {
        new AlertDialog.Builder(EditPoleDetailsActivity.this)
                .setTitle("Pay Attention!!")
                .setIcon(R.drawable.app_logo)
                .setMessage(msg)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getOnBackPressedDispatcher().onBackPressed();
                        dialog.dismiss();

                    }
                })
                .show();
    }

    private void setPoleType() {
        List<String> ugpsccList = new ArrayList<>();

        ugpsccList.add("PSCC");
        ugpsccList.add("SPUN");
        ugpsccList.add("TOWER");
        ugpsccList.add("RAIL");
        ugpsccList.add("RS JOIST");
        ugpsccList.add("TUBULAR POLE");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, ugpsccList);

        sp_pole_type_33.setAdapter(adapter);

        String selected = DATA_MODEL.getPOLE_TYPE();

        if (selected != null) {
            int pos = ugpsccList.indexOf(selected);
            if (pos >= 0) {
                sp_pole_type_33.setSelection(pos);
            }
        }
    }
//    private void setPoleType() {
//        List<String> ugpsccList = new ArrayList<>();
////        if (list33.isEmpty() && IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
////            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("PSCC")
////                    || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("SPUN")
////                    || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TOWER")
////                    || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RAIL")
////                    || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RS JOIST")
////                    || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TUBULAR POLE")) {
////                //System.out.println("POLE_TYPE_33KV :" + EstimatesMap.LINE_EXT_POLE_TYPE);
////                if (!EstimatesMap.LINE_EXT_POLE_TYPE.isEmpty()) {
////                    System.out.println("CALLING1");
////                    ugpsccList.add("PSCC");
////                    ugpsccList.add("SPUN");
////                    ugpsccList.add("TOWER");
////                    // ugpsccList.add("SS BAY");
////                    ugpsccList.add("RAIL");
////                    ugpsccList.add("RS JOIST");
////                    ugpsccList.add("TUBULAR POLE");
////                    ugpsccList.remove(EstimatesMap.LINE_EXT_POLE_TYPE);
////                    ugpsccList.add(EstimatesMap.LINE_EXT_POLE_TYPE);
////
////                    Collections.reverse(ugpsccList);
////                } else {
////                    System.out.println("CALLING2");
////                    ugpsccList.add("Select");
////                    ugpsccList.add("PSCC");
////                    ugpsccList.add("SPUN");
////                    ugpsccList.add("TOWER");
////                    // ugpsccList.add("SS BAY");
////                    ugpsccList.add("RAIL");
////                    ugpsccList.add("RS JOIST");
////                    ugpsccList.add("TUBULAR POLE");
////                    Collections.reverse(ugpsccList);
////                }
////            } else {
////                System.out.println("CALLING3");
////                ugpsccList.add("Select");
////                ugpsccList.add("PSCC");
////                ugpsccList.add("SPUN");
////                ugpsccList.add("TOWER");
////                // ugpsccList.add("SS BAY");
////                ugpsccList.add("RAIL");
////                ugpsccList.add("RS JOIST");
////                ugpsccList.add("TUBULAR POLE");
////            }
////        } else {
////            if (EstimatesMap.IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
////                if (!EstimatesMap.LINE_EXT_POLE_TYPE.isEmpty()) {
////                    if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("PSCC")
////                            || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("SPUN")
////                            || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TOWER")
////                            || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RAIL")
////                            || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RS JOIST")
////                            || EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TUBULAR POLE")) {
////
////                        //System.out.println("POLE_TYPE_33KV :" + EstimatesMap.LINE_EXT_POLE_TYPE);
////
////                        ugpsccList.add("PSCC");
////                        ugpsccList.add("SPUN");
////                        ugpsccList.add("TOWER");
////                        //  ugpsccList.add("SS BAY");
////                        ugpsccList.add("RAIL");
////                        ugpsccList.add("RS JOIST");
////                        ugpsccList.add("TUBULAR POLE");
////                        ugpsccList.remove(EstimatesMap.LINE_EXT_POLE_TYPE);
////                        ugpsccList.add(EstimatesMap.LINE_EXT_POLE_TYPE);
////                        Collections.reverse(ugpsccList);
////
////                    } else {
////                        System.out.println("CALLING4");
////                        ugpsccList.add("Select");
////                        ugpsccList.add("PSCC");
////                        ugpsccList.add("SPUN");
////                        ugpsccList.add("TOWER");
////                        //  ugpsccList.add("SS BAY");
////                        ugpsccList.add("RAIL");
////                        ugpsccList.add("RS JOIST");
////                        ugpsccList.add("TUBULAR POLE");
////                    }
////
////
////                } else {
////                    System.out.println("CALLING5");
////                    ugpsccList.add("Select");
////                    ugpsccList.add("PSCC");
////                    ugpsccList.add("SPUN");
////                    ugpsccList.add("TOWER");
////                    //  ugpsccList.add("SS BAY");
////                    ugpsccList.add("RAIL");
////                    ugpsccList.add("RS JOIST");
////                    ugpsccList.add("TUBULAR POLE");
////                }
////            } else {
////                System.out.println("CALLING6");
////                ugpsccList.add("Select");
////                ugpsccList.add("PSCC");
////                ugpsccList.add("SPUN");
////                ugpsccList.add("TOWER");
////                //  ugpsccList.add("SS BAY");
////                ugpsccList.add("RAIL");
////                ugpsccList.add("RS JOIST");
////                ugpsccList.add("TUBULAR POLE");
////            }
////
////
////        }
//
//        System.out.println("CALLING6");
//        //ugpsccList.add("Select");
//        ugpsccList.add("PSCC");
//        ugpsccList.add("SPUN");
//        ugpsccList.add("TOWER");
//        ugpsccList.add("RAIL");
//        ugpsccList.add("RS JOIST");
//        ugpsccList.add("TUBULAR POLE");
//        ugpsccList.add(DATA_MODEL.getPOLE_TYPE());
//        ugpsccList.remove(DATA_MODEL.getPOLE_TYPE());
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugpsccList);
//        sp_pole_type_33.setAdapter(adapter);
//    }

    private void viewCasting() {
        tv_select_pole_no_text = findViewById(R.id.tv_select_pole_no_text);
        tv_select_pole_no_text_stay_33 = findViewById(R.id.tv_select_pole_no_text_stay_33);
        sp_pole_support_33 = findViewById(R.id.sp_pole_support_33);
        sp_pole_type_33 = findViewById(R.id.sp_pole_type_33);
        sp_pole_height_33 = findViewById(R.id.sp_pole_height_33);
        sp_pole_configuration_33 = findViewById(R.id.sp_pole_configuration_33);
        sp_no_8_poles_33kv = findViewById(R.id.sp_no_8_poles_33kv);
        ch_33kv_8_stuart_poles = findViewById(R.id.ch_33kv_8_stuart_poles_33kv);
        ch_33kv_9_stuart_poles = findViewById(R.id.ch_33kv_9_stuart_poles_33kv);
        sp_sp_pole_desc_33_8_pole_support = findViewById(R.id.sp_sp_pole_desc_33_8_pole_support);
        layout_sp_no_of_pole_support_33 = findViewById(R.id.layout_sp_no_of_pole_support_33);
        layout_sp_no_of_pole_stay_support_33 = findViewById(R.id.layout_sp_no_of_pole_stay_support_33);
        layout_sp_pole_desc_33_8_pole_support = findViewById(R.id.layout_sp_pole_desc_33_8_pole_support);
        sp_no_9_poles_33kv = findViewById(R.id.sp_no_9_poles_33kv);
        tv_select_8m_pole_no_33kv = findViewById(R.id.tv_select_8m_pole_no_33kv);
        tv_select_9m_pole_no_33kv = findViewById(R.id.tv_select_9m_pole_no_33kv);
        layout_sp_pole_desc_33_pole_support = findViewById(R.id.layout_sp_pole_desc_33_pole_support);
        sp_sp_pole_desc_33__9_1_pole_support = findViewById(R.id.sp_sp_pole_desc_33_pole_support);
        layout_conductor_size_33 = findViewById(R.id.layout_conductor_size_33);
        layout_conductor_33 = findViewById(R.id.layout_conductor_33);
        layout_line_type_33kv = findViewById(R.id.layout_line_type_33kv);
        // sp_lineType_33 = findViewById(R.id.sp_lineType_33);
        //sp_conductor_33 = findViewById(R.id.sp_conductor_33);
        // sp_conductor_size_33 = findViewById(R.id.sp_conductor_size_33);
        sp_pole_concrete_33kv = findViewById(R.id.sp_pole_concrete_33kv);
        sp_pole_structure_33kv = findViewById(R.id.sp_pole_structure_33kv);
        sp_pole_pin_cut_point_33kv = findViewById(R.id.sp_pole_pin_cut_point_33kv);
        layout_pole_structure_33kv = findViewById(R.id.layout_pole_structure_33kv);
        layout_pole_concrete_33kv = findViewById(R.id.layout_pole_concrete_33kv);
        layout_sp_pole_type_33 = findViewById(R.id.layout_sp_pole_type_33);
        layout_sp_pole_height_33 = findViewById(R.id.layout_sp_pole_height_33);
        layout_sp_pole_desc_33 = findViewById(R.id.layout_sp_pole_desc_33);
        layout_pole_support_33kv = findViewById(R.id.layout_pole_support_33kv);
        layout_sp_pole_cut_pin_point_33kv = findViewById(R.id.layout_sp_pole_cut_pin_point_33kv);
        loader_layout = findViewById(R.id.loader_layout);
        btn_add = findViewById(R.id.btn_add);
        sp_no_of_stays_33kv = findViewById(R.id.sp_no_of_stays_33kv);
        ch_33kv_pole_desc = findViewById(R.id.ch_33kv_pole_desc);
        sp_sp_pole_desc_33 = findViewById(R.id.sp_sp_pole_desc_33);
        sp_connecting_point = findViewById(R.id.sp_connecting_point);
        layout_connecting_point = findViewById(R.id.layout_connecting_point);
        layout_distance = findViewById(R.id.layout_distance);
        layout_source_location = findViewById(R.id.layout_source_location);
        source_pole = findViewById(R.id.from_pole);
        pole_no = findViewById(R.id.pole_no);
        layout_source_pole = findViewById(R.id.layout_source_pole);
        last_pole_distance = findViewById(R.id.last_pole_distance);
        layout_distance_prv = findViewById(R.id.layout_distance_prv);
        last_pole_distance_prv = findViewById(R.id.last_pole_distance_prv);
        current_distance_prv = findViewById(R.id.current_distance_prv);
        close_layout = findViewById(R.id.close_layout);
        back_layout = findViewById(R.id.back_layout);
        layout_pole_details = findViewById(R.id.layout_pole_details);
        layout_httr_pole_details = findViewById(R.id.layout_httr_pole_details);
        sp_type_of_nature_11kv = findViewById(R.id.sp_type_of_nature_11kv);
        sp_capacity_httvr = findViewById(R.id.sp_capacity_httvr);

        layout_capacity_httvr = findViewById(R.id.layout_capacity_httvr);
        layout_ht_service_no_list = findViewById(R.id.layout_ht_service_no_list);
        sp_ht_service_no_11kv = findViewById(R.id.sp_ht_service_no_11kv);
        home_heading = findViewById(R.id.home_heading);
        layout_material_httvr = findViewById(R.id.layout_material_httvr);
        sp_material_httvr = findViewById(R.id.sp_material_httvr);
        layout_service_released_no_11kv = findViewById(R.id.layout_service_released_no_11kv);
        et_service_released_11kv = findViewById(R.id.et_service_released_11kv);
        layout_service_released = findViewById(R.id.layout_service_released);
        sp_service_released_11kv = findViewById(R.id.sp_service_released_11kv);
        layout_ht_sc_no_no_11kv = findViewById(R.id.layout_ht_sc_no_no_11kv);
        et_ht_sc_no_11kv = findViewById(R.id.et_ht_sc_no_11kv);
        from_pole = findViewById(R.id.from_pole);
    }

    private void spinnerClickEvent() {
        sp_pole_support_33.setOnItemSelectedListener(this);
        sp_no_8_poles_33kv.setOnItemSelectedListener(this);
        sp_sp_pole_desc_33_8_pole_support.setOnItemSelectedListener(this);
        sp_pole_type_33.setOnItemSelectedListener(this);
        sp_pole_height_33.setOnItemSelectedListener(this);
        sp_no_9_poles_33kv.setOnItemSelectedListener(this);
        sp_sp_pole_desc_33__9_1_pole_support.setOnItemSelectedListener(this);
        // sp_conductor_33.setOnItemSelectedListener(this);
        // sp_conductor_size_33.setOnItemSelectedListener(this);
        //  sp_lineType_33.setOnItemSelectedListener(this);
        sp_pole_concrete_33kv.setOnItemSelectedListener(this);
        sp_pole_structure_33kv.setOnItemSelectedListener(this);
        sp_pole_pin_cut_point_33kv.setOnItemSelectedListener(this);
        sp_no_of_stays_33kv.setOnItemSelectedListener(this);
        sp_sp_pole_desc_33.setOnItemSelectedListener(this);
        sp_connecting_point.setOnItemSelectedListener(this);
        sp_type_of_nature_11kv.setOnItemSelectedListener(this);
        sp_capacity_httvr.setOnItemSelectedListener(this);

        sp_ht_service_no_11kv.setOnItemSelectedListener(this);
        sp_service_released_11kv.setOnItemSelectedListener(this);
        // sp_pole_configuration_33.setOnItemSelectedListener(this);
    }


    private void loadPoleHeightDataStrut9M33kv(String height) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            //Log.d("STRUAT_POLE_HEIGTH--->", "Fetched Data: " + json);
            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, model);
            sp_sp_pole_desc_33__9_1_pole_support.setAdapter(adapter);
        } else {
            layout_sp_pole_desc_33_pole_support.setVisibility(View.GONE);
        }
    }

    private void loadPoleHeightDataStrut8M33kv(String height) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
        if (!model.isEmpty()) {
            List<String> abList = new ArrayList<>();
            for (int i = 0; i < model.size(); i++) {
                abList = Collections.singletonList(model.get(0).getMATERIALPART1());

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, abList);
            sp_sp_pole_desc_33_8_pole_support.setAdapter(adapter);
        } else {
            layout_sp_pole_desc_33_8_pole_support.setVisibility(View.GONE);
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_pole_support_33:
                if (!sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STURT") ||
                            sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {

                        layout_sp_no_of_pole_support_33.setVisibility(View.VISIBLE);
                        layout_sp_no_of_pole_stay_support_33.setVisibility(View.GONE);
                        tv_select_pole_no_text.setText("Select The Number of STURT Poles*");

                        String poleType = sp_pole_type_33.getSelectedItem() != null ? sp_pole_type_33.getSelectedItem().toString() : "";
                        String poleHeight = sp_pole_height_33.getSelectedItem() != null ? sp_pole_height_33.getSelectedItem().toString() : "";

                    } else if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STAY")) {
                        layout_sp_no_of_pole_support_33.setVisibility(View.GONE);
                        layout_sp_no_of_pole_stay_support_33.setVisibility(View.VISIBLE);
                        tv_select_pole_no_text.setText("No of STAYs*");
                        //

                    } else if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STURT and STAY") ||
                            sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY") ||
                            sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT AND STAY")
                    ) {


                        layout_sp_no_of_pole_support_33.setVisibility(View.VISIBLE);
                        layout_sp_no_of_pole_stay_support_33.setVisibility(View.VISIBLE);
                        tv_select_pole_no_text.setText("Select The Number of STURT Poles*");
                        tv_select_pole_no_text_stay_33.setText("Select The Number of STAY*");
                    } else {
                        layout_sp_no_of_pole_support_33.setVisibility(View.GONE);
                        layout_sp_no_of_pole_stay_support_33.setVisibility(View.GONE);
                        ch_33kv_9_stuart_poles.setChecked(false);
                        ch_33kv_8_stuart_poles.setChecked(false);

                    }
                }
                break;
            case R.id.sp_pole_type_33:
                poleType33 = sp_pole_type_33.getSelectedItem().toString();
                sp_pole_height_33.setSelection(0);

                if (!sp_pole_type_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_sp_pole_height_33.setVisibility(View.VISIBLE);


                }
                System.out.println("POLE_HEIGHT------>" + BTN_VALUE);
                if (BTN_VALUE.equalsIgnoreCase("33KV")) {
                    if ("PSCC".equalsIgnoreCase(poleType33)) {

                        List<String> ugpsccList = new ArrayList<>();
                        if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                            //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                            //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("PSCC")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                    ugpsccList.add("9.1");
                                    ugpsccList.add("11");
                                    ugpsccList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugpsccList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugpsccList);
                                } else {
                                    ugpsccList.add("Select");
                                    ugpsccList.add("9.1");
                                    ugpsccList.add("11");
                                }
                            } else {
                                ugpsccList.add("Select");
                                ugpsccList.add("9.1");
                                ugpsccList.add("11");
                            }
                        } else {
                            if (IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
                                //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                                //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("PSCC")) {
                                    if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                        ugpsccList.add("9.1");
                                        ugpsccList.add("11");
                                        ugpsccList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        ugpsccList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        Collections.reverse(ugpsccList);
                                    } else {
                                        ugpsccList.add("Select");
                                        ugpsccList.add("9.1");
                                        ugpsccList.add("11");
                                    }
                                } else {
                                    ugpsccList.add("Select");
                                    ugpsccList.add("9.1");
                                    ugpsccList.add("11");
                                }
                            } else {
                                System.out.println("CALLING_HEIGHT");
                                ugpsccList.add("9.1");
                                ugpsccList.add("11");
                                ugpsccList.add(DATA_MODEL.getPOLE_HEIGHT());
                                ugpsccList.remove(DATA_MODEL.getPOLE_HEIGHT());
                                Collections.reverse(ugpsccList);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                        sp_pole_height_33.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                            //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                            //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("SPUN")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {
                            if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                                //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                                //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("SPUN")) {
                                    if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                        ugspunList.add("9.5");
                                        ugspunList.add("11");
                                        ugspunList.add("12.5");
                                        ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        Collections.reverse(ugspunList);
                                    }
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {

                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                                ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                                ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                                Collections.reverse(ugspunList);
                            }
                        }


                   /* ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");*/
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("TOWER".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                            //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                            //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TOWER")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+3") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+6") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+9") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+12") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("R+6")) {
                                    ugspunList.add("M+3");
                                    ugspunList.add("M+6");
                                    ugspunList.add("M+9");
                                    ugspunList.add("M+12");
                                    ugspunList.add("R+6");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("M+3");
                                    ugspunList.add("M+6");
                                    ugspunList.add("M+9");
                                    ugspunList.add("M+12");
                                    ugspunList.add("R+6");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("M+3");
                                ugspunList.add("M+6");
                                ugspunList.add("M+9");
                                ugspunList.add("M+12");
                                ugspunList.add("R+6");
                            }
                        } else {
                            if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                                //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                                //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TOWER")) {
                                    if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+3") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+6") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+9") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+12") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("R+6")) {
                                        ugspunList.add("M+3");
                                        ugspunList.add("M+6");
                                        ugspunList.add("M+9");
                                        ugspunList.add("M+12");
                                        ugspunList.add("R+6");
                                        ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        Collections.reverse(ugspunList);
                                    } else {
                                        ugspunList.add("Select");
                                        ugspunList.add("M+3");
                                        ugspunList.add("M+6");
                                        ugspunList.add("M+9");
                                        ugspunList.add("M+12");
                                        ugspunList.add("R+6");
                                    }
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("M+3");
                                    ugspunList.add("M+6");
                                    ugspunList.add("M+9");
                                    ugspunList.add("M+12");
                                    ugspunList.add("R+6");
                                }
                            } else {

                                ugspunList.add("M+3");
                                ugspunList.add("M+6");
                                ugspunList.add("M+9");
                                ugspunList.add("M+12");
                                ugspunList.add("R+6");
                                ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                                ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                                Collections.reverse(ugspunList);
                            }
                        }

                    /*ugspunList.add("Select");
                    ugspunList.add("M+3");
                    ugspunList.add("M+6");
                    ugspunList.add("M+9");
                    ugspunList.add("M+12");
                    ugspunList.add("R+6");*/
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("SS BAY".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                            //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                            //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("SS BAY")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {
                            if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                                if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("SS BAY")) {
                                    if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                        ugspunList.add("9.5");
                                        ugspunList.add("11");
                                        ugspunList.add("12.5");
                                        ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        Collections.reverse(ugspunList);
                                    } else {
                                        ugspunList.add("Select");
                                        ugspunList.add("9.5");
                                        ugspunList.add("11");
                                        ugspunList.add("12.5");
                                    }
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {

                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                                ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                                ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                                Collections.reverse(ugspunList);
                            }
                        }

                   /* ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");*/
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                            //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                            //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RAIL")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {
                            if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                                //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                                //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RAIL")) {
                                    if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                        ugspunList.add("9.5");
                                        ugspunList.add("11");
                                        ugspunList.add("12.5");
                                        ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        Collections.reverse(ugspunList);
                                    } else {

                                        ugspunList.add("Select");
                                        ugspunList.add("9.5");
                                        ugspunList.add("11");
                                        ugspunList.add("12.5");
                                    }
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {

                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                                ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                                ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                                Collections.reverse(ugspunList);
                            }
                        }

                   /* ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");*/
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                            //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                            //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RS JOIST")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                            }
                        } else {
                            if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                                //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                                //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RS JOIST")) {
                                    if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                        ugspunList.add("9.1");
                                        ugspunList.add("11");
                                        ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        Collections.reverse(ugspunList);
                                    } else {
                                        ugspunList.add("Select");
                                        ugspunList.add("9.1");
                                        ugspunList.add("11");
                                    }
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                }
                            } else {
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                                ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                                ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                                Collections.reverse(ugspunList);
                            }
                        }

                    /*ugspunList.add("Select");
                    ugspunList.add("9.1");
                    ugspunList.add("11");*/
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("TUBULAR POLE".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                            //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                            //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TUBULAR POLE")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                            }
                        } else {
                            if (IS_LINE_EXTENSION_33_DATA_AVAILABLE) {
                                //System.out.println("LINE_EXT_POLE_TYPE" + EstimatesMap.LINE_EXT_POLE_TYPE);
                                //System.out.println("LINE_EXT_POLE_HEIGHT" + EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TUBULAR POLE")) {
                                    if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                            EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                        ugspunList.add("9.1");
                                        ugspunList.add("11");
                                        ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                        Collections.reverse(ugspunList);
                                    } else {
                                        ugspunList.add("Select");
                                        ugspunList.add("9.1");
                                        ugspunList.add("11");
                                    }
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                }
                            } else {

                                ugspunList.add("9.1");
                                ugspunList.add("11");
                                ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                                ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                                Collections.reverse(ugspunList);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    }
                } else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
                    if ("PSCC".equalsIgnoreCase(poleType33)) {
                        List<String> ugpsccList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("PSCC")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("8") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                    ugpsccList.add("8");
                                    ugpsccList.add("9.1");
                                    ugpsccList.add("11");
                                    ugpsccList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugpsccList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugpsccList);
                                } else {
                                    ugpsccList.add("Select");
                                    ugpsccList.add("8");
                                    ugpsccList.add("9.1");
                                    ugpsccList.add("11");
                                }
                            } else {
                                ugpsccList.add("Select");
                                ugpsccList.add("8");
                                ugpsccList.add("9.1");
                                ugpsccList.add("11");
                            }
                        } else {

                            ugpsccList.add("8");
                            ugpsccList.add("9.1");
                            ugpsccList.add("11");
                            ugpsccList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugpsccList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugpsccList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                        sp_pole_height_33.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("SPUN")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")
                                ) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }

                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {

                            ugspunList.add("9.5");
                            ugspunList.add("11");
                            ugspunList.add("12.5");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("TOWER".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TOWER")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+3") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+6") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+9") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+12") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("R+6")

                                ) {
                                    ugspunList.add("M+3");
                                    ugspunList.add("M+6");
                                    ugspunList.add("M+9");
                                    ugspunList.add("M+12");
                                    ugspunList.add("R+6");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("M+3");
                                    ugspunList.add("M+6");
                                    ugspunList.add("M+9");
                                    ugspunList.add("M+12");
                                    ugspunList.add("R+6");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("M+3");
                                ugspunList.add("M+6");
                                ugspunList.add("M+9");
                                ugspunList.add("M+12");
                                ugspunList.add("R+6");
                            }
                        } else {

                            ugspunList.add("M+3");
                            ugspunList.add("M+6");
                            ugspunList.add("M+9");
                            ugspunList.add("M+12");
                            ugspunList.add("R+6");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("33/11KV SS-11KV BAY".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("33/11KV SS-11KV BAY")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")

                                ) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {

                            ugspunList.add("9.5");
                            ugspunList.add("11");
                            ugspunList.add("12.5");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();


                        if (IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RAIL")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {

                            ugspunList.add("9.5");
                            ugspunList.add("11");
                            ugspunList.add("12.5");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RS JOIST")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")
                                ) {
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                            }
                        } else {

                            ugspunList.add("9.1");
                            ugspunList.add("11");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("TUBULAR POLE".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TUBULAR POLE")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                            }
                        } else {

                            ugspunList.add("9.1");
                            ugspunList.add("11");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    }
                } else {
                    if ("PSCC".equalsIgnoreCase(poleType33)) {
                        List<String> ugpsccList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_LT_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("PSCC")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("8.0") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("8")) {
                                    ugpsccList.add("8");
                                    ugpsccList.add("9.1");
                                    ugpsccList.add("11");
                                    ugpsccList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugpsccList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugpsccList);
                                } else {
                                    ugpsccList.add("Select");
                                    ugpsccList.add("8");
                                    ugpsccList.add("9.1");
                                    ugpsccList.add("11");
                                }
                            } else {
                                ugpsccList.add("Select");
                                ugpsccList.add("8");
                                ugpsccList.add("9.1");
                                ugpsccList.add("11");
                            }
                        } else {

                            ugpsccList.add("8");
                            ugpsccList.add("9.1");
                            ugpsccList.add("11");
                            ugpsccList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugpsccList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugpsccList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                        sp_pole_height_33.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_LT_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("SPUN")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")
                                ) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }

                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {

                            ugspunList.add("9.5");
                            ugspunList.add("11");
                            ugspunList.add("12.5");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("TOWER".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_LT_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TOWER")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+3") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+6") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+9") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("M+12") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("R+6")

                                ) {
                                    ugspunList.add("M+3");
                                    ugspunList.add("M+6");
                                    ugspunList.add("M+9");
                                    ugspunList.add("M+12");
                                    ugspunList.add("R+6");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("M+3");
                                    ugspunList.add("M+6");
                                    ugspunList.add("M+9");
                                    ugspunList.add("M+12");
                                    ugspunList.add("R+6");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("M+3");
                                ugspunList.add("M+6");
                                ugspunList.add("M+9");
                                ugspunList.add("M+12");
                                ugspunList.add("R+6");
                            }
                        } else {

                            ugspunList.add("M+3");
                            ugspunList.add("M+6");
                            ugspunList.add("M+9");
                            ugspunList.add("M+12");
                            ugspunList.add("R+6");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();


                        if (IS_LINE_EXTENSION_LT_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RAIL")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {

                            ugspunList.add("9.5");
                            ugspunList.add("11");
                            ugspunList.add("12.5");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_LT_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("RS JOIST")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")
                                ) {
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                            }
                        } else {

                            ugspunList.add("9.1");
                            ugspunList.add("11");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("TUBULAR POLE".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_LT_DATA_AVAILABLE) {
                            if (EstimatesMap.LINE_EXT_POLE_TYPE.equalsIgnoreCase("TUBULAR POLE")) {
                                if (EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                        EstimatesMap.LINE_EXT_POLE_HEIGHT.equalsIgnoreCase("11")) {
                                    ugspunList.add("9.1");
                                    ugspunList.add("11");
                                    ugspunList.remove(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    ugspunList.add(EstimatesMap.LINE_EXT_POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                            }
                        } else {

                            ugspunList.add("9.1");
                            ugspunList.add("11");
                            ugspunList.add(DATA_MODEL.getPOLE_HEIGHT());
                            ugspunList.remove(DATA_MODEL.getPOLE_HEIGHT());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    }
                }
                break;

            case R.id.sp_service_released_11kv:
                if (!sp_service_released_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                        if (sp_service_released_11kv.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                            layout_ht_service_no_list.setVisibility(View.VISIBLE);
                            layout_service_released_no_11kv.setVisibility(View.GONE);
                        } else {
                            layout_ht_service_no_list.setVisibility(View.GONE);
                            layout_service_released_no_11kv.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    layout_ht_service_no_list.setVisibility(View.GONE);
                    layout_service_released_no_11kv.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_pole_height_33:

                String selectedHeight = sp_pole_height_33.getSelectedItem().toString().trim();
                if (!selectedHeight.equalsIgnoreCase("Select")) {

                    String poleType = sp_pole_type_33.getSelectedItem() != null ? sp_pole_type_33.getSelectedItem().toString() : "";
                    String poleHeight = sp_pole_height_33.getSelectedItem() != null ? sp_pole_height_33.getSelectedItem().toString() : "";

                    System.out.println("POLE_SURVEY_TYPE-->" + DATA_MODEL.getIS_EXISTING_LOCATION_NEW());

                    if (DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Proposed")) {
                        layout_sp_pole_desc_33.setVisibility(View.VISIBLE);


                    } else {
                        layout_sp_pole_desc_33.setVisibility(View.GONE);

                    }
                    List<String> validHeights = Arrays.asList("8.0", "9.1", "11", "9.5", "12.5");

                    // Log.d("Debugg_Spinner_Height", "Selected Height: " + selectedHeight);

                    if (validHeights.contains(selectedHeight)) {
                        String heightForDatabase = selectedHeight.equals("8.0") ? "8" : selectedHeight;
                        //23-12-24 Chandu Changes
                        if (DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Proposed")) {
                            loadPoleHeightData(heightForDatabase, sp_pole_type_33.getSelectedItem().toString().trim());
                        } else {
                            ch_33kv_pole_desc.setVisibility(View.VISIBLE);
                        }

                    } else {
                        layout_sp_pole_desc_33.setVisibility(View.GONE);
                        //  Log.d("Debugg_Spinner_Height", "Selected height not in the valid list.");
                    }
                } else {
                    layout_sp_pole_desc_33.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_capacity_httvr:
                if (!sp_capacity_httvr.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    getCTPTMaterial(sp_capacity_httvr.getSelectedItem().toString());

                }
                break;
            case R.id.sp_type_of_nature_11kv:
                if (!sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                        if (!sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")
                                && sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("HT Service") &&
                                DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Proposed")) {
                            layout_service_released.setVisibility(View.VISIBLE);
                            if (WorkInvolmentsActivity.htServiceListModels != null || !WorkInvolmentsActivity.htServiceListModels.isEmpty()) {
                                List<String> dropdownList = new ArrayList<>();
                                dropdownList.add("Select");
                                dropdownList.addAll(WorkInvolmentsActivity.htServiceListModels);
                                ArrayAdapter<String> adapter_pst = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dropdownList);

                                sp_ht_service_no_11kv.setAdapter(adapter_pst);

                                layout_ht_service_no_list.setVisibility(View.GONE);
                            } else {
                                layout_ht_service_no_list.setVisibility(View.GONE);
                            }

                        } else if (sp_type_of_nature_11kv.getSelectedItem().toString().equalsIgnoreCase("HT Service") &&
                                DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Existing")) {
                            layout_ht_service_no_list.setVisibility(View.GONE);
                            layout_service_released.setVisibility(View.GONE);
                            layout_service_released_no_11kv.setVisibility(View.GONE);
                            layout_ht_sc_no_no_11kv.setVisibility(View.VISIBLE);

                            if ((DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Existing") && IS_CURRENT_LOCATION_LINE_EXTENSION_11) ||
                                    DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Existing") && IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                                et_ht_sc_no_11kv.setText(EstimatesMap.LINE_EXT_HT_SERVICE_NO);
                                if (!EstimatesMap.LINE_EXT_HT_SERVICE_NO.isEmpty()) {
                                    setNatureOfService();
                                }


                            }


                        } else {
                            layout_ht_service_no_list.setVisibility(View.GONE);
                            layout_service_released.setVisibility(View.GONE);
                            layout_service_released_no_11kv.setVisibility(View.GONE);
                        }

                    }
                } else {
                    sp_capacity_httvr.setSelection(0, true);
                }
                break;


            case R.id.sp_connecting_point:
                if (BTN_VALUE.equalsIgnoreCase("11KV")){
                    if (position!=0){
                        layout_distance.setVisibility(View.VISIBLE);
                        String latitude_curr = null;
                        String longitude_curr = null;
                        SELECTED_SOURCE_POLE_LOCATION = sp_connecting_point.getSelectedItem().toString();
                        if (BTN_VALUE.equalsIgnoreCase("33KV")) {

                            for (int i = 0; i < list33.size(); i++) {
                                if (list33.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                                    if (list33.get(i).getPOLE_NO().equalsIgnoreCase(SELECTED_SOURCE_POLE_LOCATION)) {
                                        latitude_curr = list33.get(i).getLATITUDE();
                                        longitude_curr = list33.get(i).getLONGITUDE();
                                        System.out.println("FROM_POLE_LAT" + latitude_curr);
                                        System.out.println("FROM_POLE_Long" + longitude_curr);
                                        break; // stop once found
                                    }
                                }
                            }
                        }

                        if (BTN_VALUE.equalsIgnoreCase("11KV")) {

                            for (int i = 0; i < list11.size(); i++) {
                                if (list11.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                                    if (list11.get(i).getPOLE_NO().equalsIgnoreCase(SELECTED_SOURCE_POLE_LOCATION)) {
                                        latitude_curr = list11.get(i).getLATITUDE();
                                        longitude_curr = list11.get(i).getLONGITUDE();
                                        System.out.println("FROM_POLE_LAT_11KV" + latitude_curr);
                                        System.out.println("FROM_POLE_Long_11KV" + longitude_curr);
                                        break; // stop once found
                                    }
                                }
                            }
                        }
                        if (DATA_MODEL.getLATITUDE() != null && DATA_MODEL.getLONGITUDE() != null && latitude_curr != null && longitude_curr != null) {
                            double distance_current = getDistanceBetweenPoles33(DATA_MODEL.getLATITUDE(), DATA_MODEL.getLONGITUDE(), Double.parseDouble(latitude_curr), Double.parseDouble(longitude_curr));
                            long roundedDistance_cur = Math.round(distance_current);
                            last_pole_distance.setText("" + roundedDistance_cur);
                            last_pole_distance_prv.setText(""+roundedDistance_cur);
                            from_pole.setText(sp_connecting_point.getSelectedItem().toString());

                            distance = roundedDistance_cur;
                        } else {
                            last_pole_distance.setText("0");
                        }
                    }
                }
                else if (BTN_VALUE.equalsIgnoreCase("33KV")){
                    if (position!=0){
                        layout_distance.setVisibility(View.VISIBLE);
                        String latitude_curr = null;
                        String longitude_curr = null;
                        SELECTED_SOURCE_POLE_LOCATION = sp_connecting_point.getSelectedItem().toString();
                        if (BTN_VALUE.equalsIgnoreCase("33KV")) {

                            for (int i = 0; i < list33.size(); i++) {
                                if (list33.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                                    if (list33.get(i).getPOLE_NO().equalsIgnoreCase(SELECTED_SOURCE_POLE_LOCATION)) {
                                        latitude_curr = list33.get(i).getLATITUDE();
                                        longitude_curr = list33.get(i).getLONGITUDE();
                                        System.out.println("FROM_POLE_LAT" + latitude_curr);
                                        System.out.println("FROM_POLE_Long" + longitude_curr);
                                        break; // stop once found
                                    }
                                }
                            }
                        }

                        if (BTN_VALUE.equalsIgnoreCase("11KV")) {

                            for (int i = 0; i < list11.size(); i++) {
                                if (list11.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                                    if (list11.get(i).getPOLE_NO().equalsIgnoreCase(SELECTED_SOURCE_POLE_LOCATION)) {
                                        latitude_curr = list11.get(i).getLATITUDE();
                                        longitude_curr = list11.get(i).getLONGITUDE();
                                        System.out.println("FROM_POLE_LAT_11KV" + latitude_curr);
                                        System.out.println("FROM_POLE_Long_11KV" + longitude_curr);
                                        break; // stop once found
                                    }
                                }
                            }
                        }
                        if (DATA_MODEL.getLATITUDE() != null && DATA_MODEL.getLONGITUDE() != null && latitude_curr != null && longitude_curr != null) {
                            double distance_current = getDistanceBetweenPoles33(DATA_MODEL.getLATITUDE(), DATA_MODEL.getLONGITUDE(), Double.parseDouble(latitude_curr), Double.parseDouble(longitude_curr));
                            long roundedDistance_cur = Math.round(distance_current);
                            last_pole_distance.setText("" + roundedDistance_cur);
                            last_pole_distance_prv.setText(""+roundedDistance_cur);
                            from_pole.setText(sp_connecting_point.getSelectedItem().toString());

                            distance = roundedDistance_cur;
                        } else {
                            last_pole_distance.setText("0");
                        }
                    }
                }
                else {
                    if (position!=0){
                        layout_distance.setVisibility(View.VISIBLE);
                        String latitude_curr = null;
                        String longitude_curr = null;
                        SELECTED_SOURCE_POLE_LOCATION = sp_connecting_point.getSelectedItem().toString();
                        if (BTN_VALUE.equalsIgnoreCase("33KV")) {

                            for (int i = 0; i < list33.size(); i++) {
                                if (list33.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                                    if (list33.get(i).getPOLE_NO().equalsIgnoreCase(SELECTED_SOURCE_POLE_LOCATION)) {
                                        latitude_curr = list33.get(i).getLATITUDE();
                                        longitude_curr = list33.get(i).getLONGITUDE();
                                        System.out.println("FROM_POLE_LAT" + latitude_curr);
                                        System.out.println("FROM_POLE_Long" + longitude_curr);
                                        break; // stop once found
                                    }
                                }
                            }
                        }

                        if (BTN_VALUE.equalsIgnoreCase("11KV")) {

                            for (int i = 0; i < list11.size(); i++) {
                                if (list11.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                                    if (list11.get(i).getPOLE_NO().equalsIgnoreCase(SELECTED_SOURCE_POLE_LOCATION)) {
                                        latitude_curr = list11.get(i).getLATITUDE();
                                        longitude_curr = list11.get(i).getLONGITUDE();
                                        System.out.println("FROM_POLE_LAT_11KV" + latitude_curr);
                                        System.out.println("FROM_POLE_Long_11KV" + longitude_curr);
                                        break; // stop once found
                                    }
                                }
                            }
                        }

                        if (BTN_VALUE.equalsIgnoreCase("LT")) {

                            for (int i = 0; i < listLT.size(); i++) {
                                if (listLT.get(i).getLT_AVAILABLE().equalsIgnoreCase(DATA_MODEL.getLT_AVAILABLE())) {
                                    if (listLT.get(i).getPOLE_NO().equalsIgnoreCase(SELECTED_SOURCE_POLE_LOCATION)) {
                                        latitude_curr = listLT.get(i).getLATITUDE();
                                        longitude_curr = listLT.get(i).getLONGITUDE();
                                        System.out.println("FROM_POLE_LAT_11KV" + latitude_curr);
                                        System.out.println("FROM_POLE_Long_11KV" + longitude_curr);
                                        break; // stop once found
                                    }
                                }
                            }
                        }
                        if (DATA_MODEL.getLATITUDE() != null && DATA_MODEL.getLONGITUDE() != null && latitude_curr != null && longitude_curr != null) {
                            double distance_current = getDistanceBetweenPoles33(DATA_MODEL.getLATITUDE(), DATA_MODEL.getLONGITUDE(), Double.parseDouble(latitude_curr), Double.parseDouble(longitude_curr));
                            long roundedDistance_cur = Math.round(distance_current);
                            last_pole_distance.setText("" + roundedDistance_cur);
                            last_pole_distance_prv.setText(""+roundedDistance_cur);
                            from_pole.setText(sp_connecting_point.getSelectedItem().toString());

                            distance = roundedDistance_cur;
                        } else {
                            last_pole_distance.setText("0");
                        }
                    }
                }
                break;

        }
    }

    private void setNatureOfService() {
        List<String> natureOfService = new ArrayList<>();
        natureOfService.add("HT Service");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, natureOfService);
        sp_type_of_nature_11kv.setAdapter(adapter);


    }

    private void loadPoleHeightData(String height, String poleType) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height, poleType);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);

            if (DATA_MODEL.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Existing")) {
                if (ch_33kv_pole_desc.isChecked()) {
                    layout_sp_pole_desc_33.setVisibility(View.VISIBLE);
                }
            } else {

            }

            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, model);
            sp_sp_pole_desc_33.setAdapter(adapter);
            sp_sp_pole_desc_33.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_sp_pole_desc_33.setVisibility(View.GONE);
        }
    }

    private void openAlertDialogForPoleTypeCgange(Spinner spinner) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Invalid Option");
        builder.setMessage("If you want to change Tower pole to any other pole please change the location in Edit survey option");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", (dialog, which) -> {
            spinner.setSelection(0, true);
            dialog.dismiss();


        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validateDistance() {
        String fromPoleNo = DATA_MODEL.getFROM_POLE();
        String latitude = null;
        String longitude = null;
        boolean normalDistance = false;
        long roundedDistance_prev;

        if (BTN_VALUE.equalsIgnoreCase("33KV")) {

            for (int i = 0; i < list33.size(); i++) {
                if (list33.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                    if (list33.get(i).getPOLE_NO().equalsIgnoreCase(fromPoleNo)) {
                        latitude = list33.get(i).getLATITUDE();
                        longitude = list33.get(i).getLONGITUDE();
                        System.out.println("FROM_POLE_LAT" + latitude);
                        System.out.println("FROM_POLE_Long" + longitude);
                        break; // stop once found
                    }
                }
            }
        } else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
            for (int i = 0; i < list11.size(); i++) {
                if (list11.get(i).getFEEDER_ID().equalsIgnoreCase(DATA_MODEL.getFEEDER_ID())) {
                    if (list11.get(i).getPOLE_NO().equalsIgnoreCase(fromPoleNo)) {
                        latitude = list11.get(i).getLATITUDE();
                        longitude = list11.get(i).getLONGITUDE();
                        System.out.println("FROM_POLE_LAT" + latitude);
                        System.out.println("FROM_POLE_Long" + longitude);
                        break; // stop once found
                    }
                }
            }
        } else {
            for (int i = 0; i < listLT.size(); i++) {
                System.out.println("FROM_POLE_LAT-->" + listLT.get(i).getLT_AVAILABLE());
                if (listLT.get(i).getLT_AVAILABLE().equalsIgnoreCase(DATA_MODEL.getLT_AVAILABLE())) {
                    if (listLT.get(i).getPOLE_NO().equalsIgnoreCase(fromPoleNo)) {
                        latitude = listLT.get(i).getLATITUDE();
                        longitude = listLT.get(i).getLONGITUDE();
                        System.out.println("FROM_POLE_LAT" + latitude);
                        System.out.println("FROM_POLE_Long" + longitude);
                        break; // stop once found
                    }
                }
            }
        }

        if (DATA_MODEL.getLATITUDE() != null && DATA_MODEL.getLONGITUDE() != null && latitude != null && longitude != null) {
            double distance_prev = getDistanceBetweenPoles33(DATA_MODEL.getLATITUDE(), DATA_MODEL.getLONGITUDE(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            roundedDistance_prev = Math.round(distance_prev);
            last_pole_distance_prv.setText("" + roundedDistance_prev);

            System.out.println("GARAOAPAHGFVBKL->" + roundedDistance_prev);
        } else {
            roundedDistance_prev = 0;
        }

        if (BTN_VALUE.equalsIgnoreCase("33KV")) {
            String poleHeight = "";
            String poleType = "";
            String poleConfig = DATA_MODEL.getPOLE_CONFIG();
            String lineType = DATA_MODEL.getLINE_TYPE();
            String voltageLevel = DATA_MODEL.getVOLTAGE_LEVEL();

            // This looks reversed in your code – logging to check
            //   boolean isRoadCrossing = DATA_MODEL.getCROSSING_TYPE().isEmpty();

            System.out.println("---- DEBUG START ----");
            System.out.println("BTN_VALUE: " + BTN_VALUE);
            System.out.println("lineType: " + lineType);
            System.out.println("voltageLevel: " + voltageLevel);
            System.out.println("poleConfig: " + poleConfig);
            System.out.println("poleType from DATA_MODEL: " + DATA_MODEL.getPOLE_TYPE());
            System.out.println("poleHeight from DATA_MODEL: " + DATA_MODEL.getPOLE_HEIGHT());
            System.out.println("CROSSING_TYPE: " + DATA_MODEL.getCROSSING_TYPE());
            System.out.println("isRoadCrossing (calculated): " + "isRoadCrossing");

            if (lineType.equalsIgnoreCase("OVERHEAD") &&
                    !poleConfig.equalsIgnoreCase("EHT SS-33KV BAY") &&
                    !poleConfig.equalsIgnoreCase("33/11KV SS-33KV BAY") &&
                    !poleConfig.equalsIgnoreCase("33/11KV SS")) {
                poleHeight = sp_pole_height_33.getSelectedItem().toString();
                poleType = sp_pole_type_33.getSelectedItem().toString();
            }

            if (lineType.equalsIgnoreCase("UNDERGROUND") &&
                    !poleConfig.equalsIgnoreCase("UG Cable Route Marker") &&
                    !poleConfig.equalsIgnoreCase("RMU") &&
                    !poleConfig.equalsIgnoreCase("33/11KV SS-33KV BAY") &&
                    !poleConfig.equalsIgnoreCase("EHT SS-33KV BAY") &&
                    !poleConfig.equalsIgnoreCase("33/11KV SS")) {
                poleHeight = sp_pole_height_33.getSelectedItem().toString();
                poleType = sp_pole_type_33.getSelectedItem().toString();
            }

            if (!poleConfig.equalsIgnoreCase("EHT SS-33KV BAY") &&
                    !poleConfig.equalsIgnoreCase("33/11KV SS-33KV BAY")) {

                List<PoleDistanceModel> model = null;

                if (lineType.equalsIgnoreCase("UNDERGROUND")) {
                    model = databaseEstimates.getPoleDistanceDetailsUnderground(lineType, voltageLevel);
                } else {
                    if (!poleType.equalsIgnoreCase("TOWER")) {
                        model = databaseEstimates.getPoleDistanceDetails(
                                lineType,
                                poleType,
                                poleHeight,
                                voltageLevel,
                                "NO"
                        );
                    } else {
                        model = databaseEstimates.getPoleDistanceDetailsTowerPole(lineType, voltageLevel, poleType);
                    }
                }

                // Debug model size
                System.out.println("PoleDistanceModel list null? " + (model == null));
                System.out.println("PoleDistanceModel size: " + (model != null ? model.size() : 0));
                System.out.println("distance: " + roundedDistance_prev + " | distanceInFloat: " + distanceInFloat);


                if (model == null || model.isEmpty() && !sp_pole_type_33.getSelectedItem().toString().equalsIgnoreCase("TOWER")) {
                    if (roundedDistance_prev > distanceInFloat) {
                        System.out.println("Condition met: model empty/null AND distance > distanceInFloat");
                        runOnUiThread(() -> showDistanceWarningDialog((float) roundedDistance_prev)); // ensure on UI thread

                    } else {
                        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                            normalDistance = true;
                        } else {
                            CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                        }

                        System.out.println("Distance not greater than distanceInFloat");
                    }
                } else {
                    int max = model.get(0).getMAX_DISTANCE();
                    System.out.println("Max distance from DB: " + max);
                    if (roundedDistance_prev > max) {
                        System.out.println("Condition met: distance > max");
                        runOnUiThread(() -> showDistanceWarningForTowerDialog((float) roundedDistance_prev, max));

                    } else {
                        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                            normalDistance = true;
                        } else {
                            CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                        }
                        System.out.println("Distance not greater than max");
                    }
                }
            }

            System.out.println("---- DEBUG END ----");
        } else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
            String poleHeight = "";
            String poleType = "";
            String poleConfig = DATA_MODEL.getPOLE_CONFIG();
            String lineType = DATA_MODEL.getLINE_TYPE();
            String voltageLevel = DATA_MODEL.getVOLTAGE_LEVEL();
            //boolean isRoadCrossing = ch_oh_road_crossing.isChecked();

            if (lineType.equalsIgnoreCase("OVERHEAD") && !poleConfig.equalsIgnoreCase("33/11KV SS-11KV BAY")) {
                poleHeight = sp_pole_height_33.getSelectedItem().toString();
                poleType = sp_pole_type_33.getSelectedItem().toString();
            }

            //25-02-2025 Chandu Changes
            if (lineType.equalsIgnoreCase("UNDERGROUND") && !poleConfig.equalsIgnoreCase("UG Cable Route Marker")
                    && !poleConfig.equalsIgnoreCase("RMU") && !poleConfig.equalsIgnoreCase("33/11KV SS-11KV BAY")) {
                poleHeight = sp_pole_height_33.getSelectedItem().toString();
                poleType = sp_pole_type_33.getSelectedItem().toString();
            }


            if (!poleConfig.equalsIgnoreCase("33/11KV SS-11KV BAY")) {
                List<PoleDistanceModel> model = null;

                if (lineType.equalsIgnoreCase("UNDERGROUND")) {
                    model = databaseEstimates.getPoleDistanceDetailsUnderground(lineType, voltageLevel);
                } else {
                    if (!poleType.equalsIgnoreCase("TOWER")) {
                        model = databaseEstimates.getPoleDistanceDetails(lineType, poleType, poleHeight, voltageLevel, "NO");
                    } else {
                        model = databaseEstimates.getPoleDistanceDetailsTowerPole(lineType, voltageLevel, poleType);

                    }
                }

                if (model == null || model.isEmpty()) {
                    if (roundedDistance_prev > distanceInFloat) {
                        System.out.println("Condition met: model empty/null AND distance > distanceInFloat");
                        runOnUiThread(() -> showDistanceWarningDialog((float) roundedDistance_prev)); // ensure on UI thread

                    } else {
                        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                            normalDistance = true;
                        } else {
                            CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                        }
                        System.out.println("Distance not greater than distanceInFloat");
                    }
                } else {
                    int max = model.get(0).getMAX_DISTANCE();
                    System.out.println("Max distance from DB: " + max);
                    if (roundedDistance_prev > max) {
                        System.out.println("Condition met: distance > max");
                        runOnUiThread(() -> showDistanceWarningForTowerDialog((float) roundedDistance_prev, max));

                    } else {
                        if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                            normalDistance = true;
                        } else {
                            CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                        }
                    }
                }

            }

        } else {
            String poleHeight = "";
            String poleType = "";
            String poleConfig = "";
            String lineType = DATA_MODEL.getLINE_TYPE();
            String voltageLevel = DATA_MODEL.getVOLTAGE_LEVEL();

            if (lineType.equalsIgnoreCase("UNDERGROUND")) {
                poleConfig = DATA_MODEL.getPOLE_CONFIG();
            }

            if (lineType.equalsIgnoreCase("OVERHEAD")) {
                poleHeight = sp_pole_height_33.getSelectedItem().toString();
                poleType = sp_pole_type_33.getSelectedItem().toString();
            }
            if (lineType.equalsIgnoreCase("UNDERGROUND") && !poleConfig.equalsIgnoreCase("UG Cable Marker")) {
                poleHeight = sp_pole_height_33.getSelectedItem().toString();
                poleType = sp_pole_type_33.getSelectedItem().toString();
            }


            List<PoleDistanceModel> model = null;

            if (lineType.equalsIgnoreCase("UNDERGROUND")) {
                model = databaseEstimates.getPoleDistanceDetailsUnderground(lineType, voltageLevel);
            } else {
                if (!poleType.equalsIgnoreCase("TOWER")) {
                    model = databaseEstimates.getPoleDistanceDetails(lineType, poleType, poleHeight, voltageLevel, "NO");
                } else {
                    model = databaseEstimates.getPoleDistanceDetailsTowerPole(lineType, voltageLevel, poleType);
                }
            }


            // Check if model is null or empty before accessing
            if (model == null || model.isEmpty()) {
                if (roundedDistance_prev > distanceInFloat) {
                    showDistanceWarningDialog((float) roundedDistance_prev);

                } else {
                    if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                        normalDistance = true;
                    } else {
                        CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                    }
                }
            } else {
                int max = model.get(0).getMAX_DISTANCE();
                if (roundedDistance_prev > max) {
                    showDistanceWarningForTowerDialog((float) roundedDistance_prev, max);
                } else {
                    if (ConnectivityChecking.checkConnection(EditPoleDetailsActivity.this)) {
                        normalDistance = true;
                    } else {
                        CommanUtilits.internetDialog(EditPoleDetailsActivity.this);
                    }
                }
            }
        }
        return normalDistance;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getCTPTMaterial(String capacity) {
        List<DTR_HT_MaterialModel> model = databaseEstimates.getDTR_HT("CTPT", capacity);
        if (!model.isEmpty()) {
            layout_material_httvr.setVisibility(View.VISIBLE);
            System.out.println("CTPT_SIZE" + model.size());

            ArrayAdapter<DTR_HT_MaterialModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, model);
            sp_material_httvr.setAdapter(adapter);
            sp_material_httvr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sapCodeForHttvrmaterial = model.get(position).getMATERIALCODE();
                    referenceCode = model.get(position).getId();
                    survey_code = model.get(position).getSURVEY_CODE();
                    DATA_ID = model.get(position).getGROUPCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_material_httvr.setVisibility(View.GONE);
        }
    }

    private void getCapacitiesForHttvr() {
        List<String> model = databaseEstimates.getCTPTCapacities("CTPT");
        if (!model.isEmpty()) {

            if (DATA_MODEL.getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                model.add(0, "Select");
                layout_capacity_httvr.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, model);
                sp_capacity_httvr.setAdapter(adapter);
                sp_capacity_httvr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getCTPTMaterial(sp_capacity_httvr.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } else if (DATA_MODEL.getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                model.add(0, "Select");
                layout_capacity_httvr.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, model);
                sp_capacity_httvr.setAdapter(adapter);

            }

        } else {
            layout_capacity_httvr.setVisibility(View.GONE);
        }
    }
}