package com.apepdcl.gis_survey.estimates.new_module;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.models.ConductorTypeModel;
import com.apepdcl.gis_survey.estimates.models.DTRTypesModel;
import com.apepdcl.gis_survey.estimates.models.MaterialMasterModel;
import com.apepdcl.gis_survey.estimates.models.PoleHeightModel;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Soubhagya Swain on 28,June,2024
 */
public class SurveyDialog extends Dialog implements AdapterView.OnItemSelectedListener {

    RelativeLayout back_layout, close_layout;
    Button btn_clear, btn_add, btn_repeat_pole_estimates;
    String existingselectedItem;
    String poleLat, poleLon;
    String lattitude, longitude, poleType, isMultiCir, lineType, poleHeiht, poleSupport, countOfSTRUT;
    double currentLat, currentLon;
    Activity context;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    Graphic graphic;
    String EST_NO, GIS_ID;
    boolean isEdit, is33KVSelected, is11KVSelected, isDTRSelected, isLTSelected, isMeterSelected;
    ArcGISDatabaseEstimates databaseEstimates;
    ArrayList<NewModel> list33, list11, listLT, allList;
    String poleType33 = "", poleType11 = "", poleTypelt = "";
    Spinner sp_line_survey_type;
    private long randomNumber = 0;
    List<String> voltage_level = new ArrayList<>();
    List<String> poleConfiguration33kv = new ArrayList<>();
    List<String> poleConfiguration11kv = new ArrayList<>();
    List<String> hg_fuse_list = new ArrayList<>();
    List<String> dtr_phase = new ArrayList<>();
    List<String> pole_type_list = new ArrayList<>();
    List<String> poleTypeSurvey = new ArrayList<>();
    Button btn_complete_survey;
    private float distance;

    private boolean is33KVSelectedSurvey = false;
    private boolean is11KVSelectedSurvey = false;
    private boolean isLTSelectedSurvey = false;


    private boolean isCompleted33kVServey = false;
    private boolean isCompleted11kVServey = false;
    private boolean isCompletedLTServey = false;

    //DISTANCE
    double currentDistance, lastPoleDistance;
    TextView last_pole_distance, current_distance;

    private String conductorSAPCode33KV = "";
    private String conductorSAPCode11KV = "";
    private String conductorSAPCodeLT = "";
    private String dtrSapCode = "";
    private String poleSapCode11kv = "";
    private String poleSapCode33kv = "";
    private String poleSapCodeLT = "";
    private String abSwitch = "";
    private String dtrAbSwitch = "";
    private String boxDistribution = "";
    private String vXArmCode33KV = "";
    private String vXArmCode11KV = "";
    private String topCleatCode33KV = "";
    private String topCleatCode11KV = "";
    private String tappingCrossCode33KV = "";
    private String tappingCrossCode11KV = "";
    private String ltCrossArm = "";
    private String tappingVCrossCode11KV = "";
    private String tappingVCrossCode33KV = "";
    private String discInsulator33kv = "";
    private String discInsulator11kv = "";
    private String discInsulatorLT = "";
    private String channel33KV = "";
    private String channel11KV = "";
    private String metalPartCode33kv = "";
    private String metalPartCode11kv = "";
    private String metalPartCodeLT = "";
    private String pinInsulatorCode33kv = "";
    private String pinInsulatorCode11kv = "";
    private String pinInsulatorCodeLT = "";
    private String sideArm33KVCode = "";
    private String sideArm11KVCode = "";
    private String sideArmLtCode = "";
    private String tappingCrossCodeLT = "";
    private String clamp33kvCode = "";
    private String clamp11kvCode = "";
    private String clampLtCode = "";
    private String ltGIPinCode = "";

    private String stayCode33KV = "";
    private String stayCode833KV = "";
    private String stayCode933KV = "";

    private String stayCode11KV = "";
    private String stayCode811KV = "";
    private String stayCode911KV = "";

    private String stayCodeLT = "";
    private String stayCode9LT = "";
    private String stayCode8LT = "";
    ArrayList<NewModel> statusList;
    double conPoleLattitude;
    double conPoleLongitude;

    private String phase = "", capacity = "";


    //CURRENT LOCATION
    double CURRENT_LAT;
    double CURRENT_LONG;

    boolean isFirstPole;
    private String max_length;
    private double distanceInFloat;

    TextView location_no, tv_select_pole_no_text, tv_select_pole_no_text_stay_33, tv_select_pole_no_text_stay_11, tv_select_pole_no_text_stay_lt,
            tv_select_pole_no_text_11, tv_select_pole_no_text_lt, dtr_structure_code_11kv;
    Spinner sp_voltage_level, sp_is_location_exists, sp_is_parent_exists, sp_parent_connecting_point, sp_connecting_point;
    LinearLayout layout_location_exists, layout_parent_exists, layout_parent_connecting_point, layout_connecting_point, survey_33,
            survey_11, survey_lt, layout_sp_no_of_pole_support_33, layout_sp_no_of_pole_support_11, layout_sp_no_of_pole_support_lt, layout_meters,
            layout_sp_no_of_pole_stay_support_33, layout_sp_no_of_pole_stay_support_11, layout_sp_no_of_pole_stay_support_lt, layout_survey_pole_type,
            layout_existing_connecting_point, layout_sp_pole_height_33, layout_sp_pole_height_11, layout_sp_pole_height_lt, layout_conductor_33, layout_conductor_size_33, layout_conductor_11,
            layout_conductor_lt, layout_distance, layout_dtr_survey, layout_dtr_mounting, main_lt_layout_11kv, pole_survey_type;


    CheckBox ch_ext_loc_33kv, ch_ext_loc_11kv, ch_ext_loc_lt;
    EditText tapping_location;

    int poleNumber;
    int pos;
    int posSurveyType;
    int posLineType;
    int posMultiCirCuit;
    LinearLayout layout_sp_voltage_level;

    //33KV START
    Spinner sp_lineType_33, sp_is_multi_circuit_33, sp_pole_type_33, sp_pole_support_33, sp_pole_configuration_33, sp_33kv_multi_ckt_pole_33, sp_no_9_poles_33kv, sp_no_8_poles_33kv,
            sp_survey_pole_type, sp_exsting_connecting_point, sp_pole_height_33, sp_pole_height_11, sp_pole_height_lt, sp_no_of_pin_insulators_33kv, sp_no_of_disc_insulators_33kv, sp_no_of_metal_insulators_33kv, sp_no_of_count_side_arm_33kv, sp_no_of_v_crossing_arms_11kv, sp_no_of_top_cleat_11kv, sp_conductor_33, sp_conductor_11, sp_conductor_lt, sp_no_of_tapping_cross_arms_33kv,
            sp_no_of_stays_33kv, sp_sp_pole_desc_33, sp_conductor_size_33, sp_sp_pole_desc_33__9_1_pole_support, sp_tapping_x_arm_33kv, sp_channel_33kv, sp_side_arm_33kv, sp_type_black_clamps_33kv,
            sp_sturt_type_black_clamps_33kv, sp_sturt_no_of_black_clamps_33kv, sp_sturt_8m_type_black_clamps_33kv, sp_no_of_black_clamps_33kv, sp_main_pole_stay_clamps_33kv,
            sp_pole_ab_switch_type_33kv, sp_type_of_v_crossing_arms_33kv, sp_type_top_cleat_33kv, sp_pole_pin_cut_point_33kv, sp_pin_insulators_type_33kv, sp_sturt_8m_no_of_black_clamps_33kv, sp_type_metal_33kv, sp_no_main_pole_stay_clamps_33kv,
            sp_no_of_top_cleat_33kv, sp_no_of_v_crossing_arms_33kv, sp_type_of_v_crossing_arms_11kv, sp_type_disc_insulators_33kv, sp_no_of_channel_33kv, sp_ded_end_pole_33kv, sp_ded_end_pole_11kv;
    CheckBox ch_multi_33kv_33, ch_multi_11kv_33, ch_multi_lt_33, ch_33kv_9_stuart_poles_33kv, ch_stay_8m_clamps_33kv, ch_33kv_8_stuart_poles_33kv, ch_disc_insulators_33kv, ch_disc_insulators_11kv,
            ch_tapping_cross_arms_33kv, ch_channel_33kv, ch_channel_11kv, ch_top_cleat_33kv, ch_no_of_v_crossing_33kv, ch_pin_insulators_33kv, ch_side_arm_33kv, ch_back_clamps_33kv, ch_stay_clamps_33kv;
    LinearLayout multi_ckt_layout_33, layout_33kv_multi_ckt_pole_33, layout_sp_pole_desc_33, layout_sp_pole_desc_33_pole_support, layout_no_v_crossing_arms_33kv, ch_no_of_desc_insulator_33kv,
            layout_ab_switch_33kv, layout_sp_pole_type_33, layout_v_crossing_33kv, layout_top_cleat_33kv,
            layout_tapping_cross_arm_33kv, layout_channel_33kv, layout_channel_11kv, layout_no_tapping_cross_arms_33kv, layout_type_of_disc_insulator_33kv, layout_type_of_metal_33kv, layout_type_of_disc_insulator_11kv,
            layout_no_channel_33kv, layout_no_of_pin_insulators_type_33kv, layout_no_top_cleat_33kv, layout_no_of_pin_insulators_33kv, layout_materials_33kv,
            layout_type_of_black_clamps_33kv, layout_sturt_type_of_black_clamps_33kv, layout_sturt_8m_type_of_black_clamps_33kv, layout_main_pole_stay_clamps_33kv, layout_line_type_33kv,
            layout_no_of_cut_insulators_33kv, layout_side_arm_33kv, layout_no_side_arm_33kv, layout_ded_end_pole_33kv, layout_ded_end_pole_11kv;
    TextView tv_select_9m_pole_no_33kv, tv_select_8m_pole_no_33kv, tv_fabrication_materials_33kv, tv_insulators_details_33kv, tv_no_stay_clamps_main_pole_33kv, tv_no_of_metal_parts_33kv, tv_stay_clamps_33kv;
    //33KV END

    //11KV START
    Spinner sp_lineType_11, sp_is_multi_circuit_11, sp_pole_type_11, sp_pole_support_11, sp_pole_configuration_11, sp_33kv_multi_ckt_pole_11, sp_11kv_multi_ckt_pole_11, sp_no_of_metal_insulators_11kv, sp_no_of_disc_insulators_11kv, sp_no_of_pin_insulators_11kv, sp_pole_ab_switch_type_11kv,
            sp_no_9_poles_11kv, sp_no_8_poles_11kv, sp_no_stays_11kv, sp_no_of_pole_support_11, sp_dtr_phase_11, sp_tapping_x_arm_11kv, sp_side_arm_11kv, sp_type_black_clamps_11kv, sp_no_main_pole_stay_clamps_11kv,
            sp_dtr_capacity_phase1_11, sp_dtr_capacity_phase2_11, sp_no_of_count_side_arm_11kv, sp_no_of_side_arm_11kv, sp_type_top_cleat_11kv, sp_channel_11kv, sp_type_disc_insulators_11kv, sp_type_metal_11kv,
            sp_sp_pole_desc_11kv_8_sturt, sp_pole_pin_cut_point_11, sp_pin_insulators_type_11kv, sp_no_of_black_clamps_11kv, sp_sturt_type_black_clamps_11kv, sp_sturt_8m_type_black_clamps_11kv,
            sp_dtr_mounting_11, sp_dtr_survey_11, sp_sp_pole_desc_11, sp_conductor_size_11, sp_dtr_phase_desc_11, sp_sp_pole_desc_33_8_pole_support, sp_lt_fuse_distribution_11kv, sp_main_pole_stay_clamps_11kv,
            sp_sp_pole_desc_11kv_stuart_9m, sp_pole_dtr_ab_switch_type_11kv, sp_hg_fuse_set_11kv, sp_lt_fuse_box_distribution_11kv, sp_ci_pipes, sp_gi_pipes, sp_sturt_8m_no_of_black_clamps_11kv,
            sp_no_of_tapping_cross_arms_11kv, sp_sturt_no_of_black_clamps_11kv;
    CheckBox ch_multi_33kv_11, ch_multi_11kv_11, ch_multi_lt_11, ch_33kv_9_stuart_poles_11kv, ch_33kv_8_stuart_poles_11kv, ch_ci_pipe, ch_gi_pipe, ch_top_cleat, ch_stay_clamps_11kv,
            ch_tapping_cross_arms_11kv, ch_no_of_v_crossing_11kv, ch_pin_insulators_11kv, ch_side_arm_11kv, ch_back_clamps_11kv;

    LinearLayout multi_ckt_layout_11, layout_33kv_multi_ckt_pole_11, layout_11kv_multi_ckt_pole_11, layout_pole_configuration, layout_no_of_pin_insulators_type_11kv, layout_type_of_metal_11kv,
            layout_dtr_phase_11kv, layout_top_cleat_11kv, layout_tapping_cross_arm_11kv, layout_v_crossing_11kv,
            layout_conductor_size_11, layout_ab_switch_11kv, layout_dtr_ab_switch_11kv, layout_hg_fuse_set_11kv, layout_no_channel_11kv,
            layout_lt_fuse_set_11kv, layout_lt_box_distribution_11kv, layout_ci_pipe, layout_sp_ci_pipes, layout_gi_pipe, layout_sturt_8m_type_of_black_clamps_11kv, layout_main_pole_stay_clamps_11kv,
            layout_sp_gi_pipes, layout_sp_pole_cut_pin_point_11, layout_sp_pole_cut_pin_point_33kv, layout_no_of_pin_insulators_11kv, layout_materials_11kv,
            layout_no_of_cut_insulators, layout_no_tapping_cross_arms_11kv, layout_no_top_cleat_11kv, layout_no_v_crossing_arms_11kv, layout_line_type_11kv,
            layout_dtr_capacity, layout_sp_pole_desc_11, layout_dtr_phase_desc_11kv, layout_sp_pole_desc_33_8_pole_support, layout_type_of_black_clamps_11kv,
            layout_sp_pole_desc_11kv_stuart_9m, layout_sp_pole_desc_11kv_8_sturt, layout_sp_pole_type_11, layout_side_arm_11kv, layout_no_side_arm_11kv, layout_sturt_type_of_black_clamps_11kv;
    TextView tv_select_8m_pole_no_11kv, tv_select_9m_pole_no_11kv, tv_fabrication_materials_11kv, tv_insulators_details_11kv, tv_no_of_metal_parts_11kv, tv_stay_clamps_11kv;
    //11KV END

    //LT START
    Spinner sp_lineType_lt, sp_is_multi_circuit_lt, sp_pole_type_lt, sp_pole_support_lt, sp_pole_configuration_lt, sp_no_9_poles_lt, sp_no_main_pole_stay_clamps_lt,
            sp_no_8_poles_lt, sp_no_stays_lt, sp_tapping_x_arm_lt, sp_sturt_type_black_clamps_lt, sp_gi_pins_type_lt, sp_no_of_gi_pins_lt, sp_sturt_8m_no_of_black_clamps_lt;
    Spinner sp_33kv_multi_ckt_pole_lt, sp_11kv_multi_ckt_pole_lt, sp_lt_multi_ckt_pole_lt, sp_sp_pole_desc_lt, sp_conductor_size_lt, sp_no_of_metal_insulators_lt, sp_sturt_8m_type_black_clamps_lt,
            sp_sp_pole_desc_lt_stuart_9m, sp_sp_pole_desc_lt_8_sturt, sp_pole_pin_cut_point_lt, sp_ded_end_pole_lt, sp_type_metal_lt, sp_side_arm_lt, sp_type_black_clamps_lt, sp_main_pole_stay_clamps_lt,
            sp_no_of_disc_insulators_lt, sp_no_of_tapping_cross_arms_lt, sp_no_of_pin_insulators_lt, sp_sturt_no_of_black_clamps_lt, sp_type_disc_insulators_lt, sp_pin_insulators_type_lt, sp_no_of_black_clamps_lt;

    CheckBox ch_multi_33kv_lt, ch_multi_11kv_lt, ch_multi_lt_lt, ch_33kv_9_stuart_poles_lt, ch_33kv_8_stuart_poles_lt,
            ch_no_of_v_crossing_lt, ch_top_cleat_lt, ch_tapping_cross_arms_lt, ch_pin_insulators_lt, ch_disc_insulators_lt, ch_side_arm_lt;

    LinearLayout multi_ckt_layout_lt, layout_33kv_multi_ckt_pole_lt, layout_11kv_multi_ckt_pole_lt, layout_lt_multi_ckt_pole_lt, layout_ded_end_pole_lt, layout_main_pole_stay_clamps_lt, layout_type_of_metal_lt,
            layout_sp_pole_desc_lt, layout_conductor_size_lt, layout_sp_pole_desc_lt_stuart_9m, layout_sp_pole_desc_lt_8_sturt, layout_no_of_cut_insulators_lt, layout_sturt_8m_type_of_black_clamps_lt,
            layout_sp_pole_cut_pin_point_lt, layout_top_cleat_lt, layout_v_crossing_lt, layout_tapping_cross_arm_lt, layout_channel_lt, layout_no_tapping_cross_arms_lt, layout_sturt_type_of_black_clamps_lt,
            layout_side_arm_lt, layout_no_v_crossing_arms_lt, layout_no_top_cleat_lt, layout_no_of_pin_insulators_type_lt, layout_type_of_black_clamps_lt, layout_materials_lt,
            layout_no_of_pin_insulators_lt, layout_type_of_disc_insulator_lt, layout_no_side_arm_lt, layout_no_of_gi_pins_type_lt, layout_line_type_lt;

    TextView tv_select_9m_pole_no_lt, tv_select_8m_pole_no_lt, tv_fabrication_materials_lt, tv_insulators_details_lt, tv_no_of_metal_parts_lt;
    //LT END

    public SurveyDialog(@NonNull Activity context, String poleLat, String poleLon,
                        Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic,
                        double currentLat, double currentLon, String EST_NO, boolean is33KVSelected,
                        boolean is11KVSelected, boolean isDTRSelected, boolean isLTSelected,
                        boolean isEdit, double lastPoleDistance, double currentDistance,
                        boolean isFirstPole, String maxLength, boolean isMeterSelected, String GIS_ID) {
        super(context);
        this.context = context;
        this.poleLat = poleLat;
        this.poleLon = poleLon;
        this.currentLat = currentLat;
        this.currentLon = currentLon;
        this.callout = callout;
        this.graphicsOverlay = graphicsOverlay;
        this.graphic = graphic;
        this.EST_NO = EST_NO;
        this.isEdit = isEdit;
        this.is33KVSelected = is33KVSelected;
        this.is11KVSelected = is11KVSelected;
        this.isDTRSelected = isDTRSelected;
        this.isLTSelected = isLTSelected;
        this.lastPoleDistance = lastPoleDistance;
        this.currentDistance = currentDistance;
        this.isFirstPole = isFirstPole;
        this.max_length = maxLength;
        this.isMeterSelected = isMeterSelected;
        this.GIS_ID = GIS_ID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.survey_dialog_new);
        setCancelable(false);

        databaseEstimates = new ArcGISDatabaseEstimates(context);
        location_no = findViewById(R.id.location_no);
        sp_line_survey_type = findViewById(R.id.sp_line_survey_type);
        layout_dtr_survey = findViewById(R.id.layout_dtr_survey);
        layout_sp_voltage_level = findViewById(R.id.layout_sp_voltage_level);
        btn_complete_survey = findViewById(R.id.btn_complete_survey);
        btn_repeat_pole_estimates = findViewById(R.id.btn_repeat_pole_estimates);


        Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
        String json1 = gson1.toJson(statusList);
        Log.d("STATUS_LIST", "onCreate: " + json1);


        list33 = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false,GIS_ID);
        list11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false,GIS_ID);
        listLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false,GIS_ID);
        allList = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true,GIS_ID);


        randomNumber = createRandomUID();
        Log.d("randomNumber", "onItemSelected: " + randomNumber);

        if (max_length != null && !max_length.trim().isEmpty()) {
            try {
                distanceInFloat = Double.parseDouble(max_length.trim());
                Log.d("DISTANCE_IN_FLOAT", "onCreate: " + distanceInFloat);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
           // Log.e("DISTANCE_IN_FLOAT", "max_length is either null or empty");
        }


        current_distance = findViewById(R.id.current_distance);
        last_pole_distance = findViewById(R.id.last_pole_distance);
        survey_33 = findViewById(R.id.survey_33);
        survey_11 = findViewById(R.id.survey_11);
        survey_lt = findViewById(R.id.survey_lt);
        tv_select_pole_no_text = findViewById(R.id.tv_select_pole_no_text);
        tv_select_pole_no_text_stay_lt = findViewById(R.id.tv_select_pole_no_text_stay_lt);
        tv_select_pole_no_text_stay_11 = findViewById(R.id.tv_select_pole_no_text_stay_11);
        tv_select_pole_no_text_stay_33 = findViewById(R.id.tv_select_pole_no_text_stay_33);

        close_layout = findViewById(R.id.close_layout);
        back_layout = findViewById(R.id.back_layout);
        btn_clear = findViewById(R.id.btn_clear);
        btn_add = findViewById(R.id.btn_add);

        layout_existing_connecting_point = findViewById(R.id.layout_existing_connecting_point);
        layout_survey_pole_type = findViewById(R.id.layout_survey_pole_type);
        layout_distance = findViewById(R.id.layout_distance);
        layout_pole_configuration = findViewById(R.id.layout_pole_configuration);
        layout_dtr_phase_11kv = findViewById(R.id.layout_dtr_phase_11kv);
        layout_dtr_phase_desc_11kv = findViewById(R.id.layout_dtr_phase_desc_11kv);
        layout_dtr_capacity = findViewById(R.id.layout_dtr_capacity);
        layout_dtr_mounting = findViewById(R.id.layout_dtr_mounting);
        layout_sp_pole_desc_33 = findViewById(R.id.layout_sp_pole_desc_33);
        sp_sp_pole_desc_33__9_1_pole_support = findViewById(R.id.sp_sp_pole_desc_33_pole_support);
        sp_pole_ab_switch_type_33kv = findViewById(R.id.sp_pole_ab_switch_type_33kv);
        sp_type_of_v_crossing_arms_33kv = findViewById(R.id.sp_type_of_v_crossing_arms_33kv);
        sp_type_of_v_crossing_arms_11kv = findViewById(R.id.sp_type_of_v_crossing_arms_11kv);
        sp_type_disc_insulators_33kv = findViewById(R.id.sp_type_disc_insulators_33kv);
        sp_type_disc_insulators_lt = findViewById(R.id.sp_type_disc_insulators_lt);
        sp_no_of_channel_33kv = findViewById(R.id.sp_no_of_channel_33kv);
        sp_ded_end_pole_33kv = findViewById(R.id.sp_ded_end_pole_33kv);
        sp_ded_end_pole_lt = findViewById(R.id.sp_ded_end_pole_lt);
        sp_ded_end_pole_11kv = findViewById(R.id.sp_ded_end_pole_11kv);
        sp_type_disc_insulators_11kv = findViewById(R.id.sp_type_disc_insulators_11kv);
        sp_type_top_cleat_33kv = findViewById(R.id.sp_type_top_cleat_33kv);
        sp_type_top_cleat_11kv = findViewById(R.id.sp_type_top_cleat_11kv);
        sp_pole_ab_switch_type_11kv = findViewById(R.id.sp_pole_ab_switch_type_11kv);
        sp_pole_dtr_ab_switch_type_11kv = findViewById(R.id.sp_pole_dtr_ab_switch_type_11kv);
        sp_hg_fuse_set_11kv = findViewById(R.id.sp_hg_fuse_set_11kv);
        layout_no_v_crossing_arms_33kv = findViewById(R.id.layout_no_v_crossing_arms_33kv);


        sp_lt_fuse_box_distribution_11kv = findViewById(R.id.sp_lt_fuse_box_distribution_11kv);
        layout_sp_pole_desc_33_pole_support = findViewById(R.id.layout_sp_pole_desc_33_pole_support);
        layout_ab_switch_33kv = findViewById(R.id.layout_ab_switch_33kv);
        layout_sp_pole_type_33 = findViewById(R.id.layout_sp_pole_type_33);
        layout_ab_switch_11kv = findViewById(R.id.layout_ab_switch_11kv);
        layout_dtr_ab_switch_11kv = findViewById(R.id.layout_dtr_ab_switch_11kv);
        layout_hg_fuse_set_11kv = findViewById(R.id.layout_hg_fuse_set_11kv);
        layout_lt_fuse_set_11kv = findViewById(R.id.layout_lt_fuse_set_11kv);
        layout_no_channel_11kv = findViewById(R.id.layout_no_channel_11kv);
        layout_top_cleat_11kv = findViewById(R.id.layout_top_cleat_11kv);
        layout_tapping_cross_arm_11kv = findViewById(R.id.layout_tapping_cross_arm_11kv);
        layout_v_crossing_11kv = findViewById(R.id.layout_v_crossing_11kv);
        layout_v_crossing_33kv = findViewById(R.id.layout_v_crossing_33kv);
        layout_no_top_cleat_33kv = findViewById(R.id.layout_no_top_cleat_33kv);
        layout_no_of_pin_insulators_33kv = findViewById(R.id.layout_no_of_pin_insulators_33kv);
        layout_materials_33kv = findViewById(R.id.layout_materials_33kv);
        layout_materials_11kv = findViewById(R.id.layout_materials_11kv);
        layout_materials_lt = findViewById(R.id.layout_materials_lt);
        layout_type_of_black_clamps_33kv = findViewById(R.id.layout_type_of_black_clamps_33kv);
        layout_sturt_type_of_black_clamps_33kv = findViewById(R.id.layout_sturt_type_of_black_clamps_33kv);
        layout_sturt_type_of_black_clamps_11kv = findViewById(R.id.layout_sturt_type_of_black_clamps_11kv);
        layout_sturt_type_of_black_clamps_lt = findViewById(R.id.layout_sturt_type_of_black_clamps_lt);
        layout_sturt_8m_type_of_black_clamps_33kv = findViewById(R.id.layout_sturt_8m_type_of_black_clamps_33kv);
        layout_sturt_8m_type_of_black_clamps_11kv = findViewById(R.id.layout_sturt_8m_type_of_black_clamps_11kv);
        layout_sturt_8m_type_of_black_clamps_lt = findViewById(R.id.layout_sturt_8m_type_of_black_clamps_lt);
        layout_main_pole_stay_clamps_33kv = findViewById(R.id.layout_main_pole_stay_clamps_33kv);
        layout_line_type_33kv = findViewById(R.id.layout_line_type_33kv);
        layout_line_type_11kv = findViewById(R.id.layout_line_type_11kv);
        layout_line_type_lt = findViewById(R.id.layout_line_type_lt);
        layout_main_pole_stay_clamps_11kv = findViewById(R.id.layout_main_pole_stay_clamps_11kv);
        layout_main_pole_stay_clamps_lt = findViewById(R.id.layout_main_pole_stay_clamps_lt);
        layout_type_of_black_clamps_11kv = findViewById(R.id.layout_type_of_black_clamps_11kv);
        layout_type_of_black_clamps_lt = findViewById(R.id.layout_type_of_black_clamps_lt);
        layout_no_of_cut_insulators_33kv = findViewById(R.id.layout_no_of_cut_insulators_33kv);
        layout_no_of_pin_insulators_lt = findViewById(R.id.layout_no_of_pin_insulators_lt);
        layout_no_of_cut_insulators_lt = findViewById(R.id.layout_no_of_cut_insulators_lt);
        layout_side_arm_33kv = findViewById(R.id.layout_side_arm_33kv);
        layout_side_arm_11kv = findViewById(R.id.layout_side_arm_11kv);
        layout_no_side_arm_33kv = findViewById(R.id.layout_no_side_arm_33kv);
        layout_no_side_arm_lt = findViewById(R.id.layout_no_side_arm_lt);
        layout_no_of_gi_pins_type_lt = findViewById(R.id.layout_no_of_gi_pins_type_lt);
        layout_ded_end_pole_33kv = findViewById(R.id.layout_ded_end_pole_33kv);
        layout_ded_end_pole_11kv = findViewById(R.id.layout_ded_end_pole_11kv);
        layout_ded_end_pole_lt = findViewById(R.id.layout_ded_end_pole_lt);
        layout_no_side_arm_11kv = findViewById(R.id.layout_no_side_arm_11kv);
        layout_top_cleat_33kv = findViewById(R.id.layout_top_cleat_33kv);
        layout_top_cleat_lt = findViewById(R.id.layout_top_cleat_lt);
        layout_side_arm_lt = findViewById(R.id.layout_side_arm_lt);
        layout_no_v_crossing_arms_lt = findViewById(R.id.layout_no_v_crossing_arms_lt);
        layout_no_top_cleat_lt = findViewById(R.id.layout_no_top_cleat_lt);
        layout_no_of_pin_insulators_type_lt = findViewById(R.id.layout_no_of_pin_insulators_type_lt);
        layout_channel_lt = findViewById(R.id.layout_channel_lt);
        layout_no_tapping_cross_arms_lt = findViewById(R.id.layout_no_tapping_cross_arms_lt);
        layout_tapping_cross_arm_lt = findViewById(R.id.layout_tapping_cross_arm_lt);
        layout_v_crossing_lt = findViewById(R.id.layout_v_crossing_lt);
        layout_no_tapping_cross_arms_33kv = findViewById(R.id.layout_no_tapping_cross_arms_33kv);
        layout_type_of_disc_insulator_33kv = findViewById(R.id.layout_type_of_disc_insulator_33kv);
        layout_type_of_disc_insulator_lt = findViewById(R.id.layout_type_of_disc_insulator_lt);
        layout_type_of_metal_33kv = findViewById(R.id.layout_type_of_metal_33kv);
        layout_type_of_metal_11kv = findViewById(R.id.layout_type_of_metal_11kv);
        layout_type_of_metal_lt = findViewById(R.id.layout_type_of_metal_lt);
        layout_type_of_disc_insulator_11kv = findViewById(R.id.layout_type_of_disc_insulator_11kv);
        layout_no_channel_33kv = findViewById(R.id.layout_no_channel_33kv);
        layout_no_of_pin_insulators_type_33kv = findViewById(R.id.layout_no_of_pin_insulators_type_33kv);
        layout_no_of_pin_insulators_type_11kv = findViewById(R.id.layout_no_of_pin_insulators_type_11kv);
        layout_tapping_cross_arm_33kv = findViewById(R.id.layout_tapping_cross_arm_33kv);
        layout_channel_33kv = findViewById(R.id.layout_channel_33kv);
        layout_channel_11kv = findViewById(R.id.layout_channel_11kv);
        layout_lt_box_distribution_11kv = findViewById(R.id.layout_lt_box_distribution_11kv);
        layout_ci_pipe = findViewById(R.id.layout_ci_pipe);
        layout_sp_ci_pipes = findViewById(R.id.layout_sp_ci_pipes);
        layout_gi_pipe = findViewById(R.id.layout_gi_pipe);
        layout_sp_gi_pipes = findViewById(R.id.layout_sp_gi_pipes);
        layout_sp_gi_pipes = findViewById(R.id.layout_sp_gi_pipes);
        layout_sp_pole_cut_pin_point_11 = findViewById(R.id.layout_sp_pole_cut_pin_point_11);
        layout_sp_pole_cut_pin_point_33kv = findViewById(R.id.layout_sp_pole_cut_pin_point_33kv);
        layout_sp_pole_cut_pin_point_lt = findViewById(R.id.layout_sp_pole_cut_pin_point_lt);
        layout_no_of_pin_insulators_11kv = findViewById(R.id.layout_no_of_pin_insulators_11kv);
        layout_no_of_cut_insulators = findViewById(R.id.layout_no_of_cut_insulators);
        layout_no_tapping_cross_arms_11kv = findViewById(R.id.layout_no_tapping_cross_arms_11kv);
        layout_no_top_cleat_11kv = findViewById(R.id.layout_no_top_cleat_11kv);
        layout_no_v_crossing_arms_11kv = findViewById(R.id.layout_no_v_crossing_arms_11kv);
        layout_sp_pole_desc_33_8_pole_support = findViewById(R.id.layout_sp_pole_desc_33_8_pole_support);
        layout_sp_pole_desc_11kv_stuart_9m = findViewById(R.id.layout_sp_pole_desc_11kv_stuart_9m);
        layout_sp_pole_desc_11kv_8_sturt = findViewById(R.id.layout_sp_pole_desc_11kv_8_sturt);
        layout_sp_pole_type_11 = findViewById(R.id.layout_sp_pole_type_11);
        layout_sp_pole_desc_11 = findViewById(R.id.layout_sp_pole_desc_11);
        main_lt_layout_11kv = findViewById(R.id.main_lt_layout_11kv);
        pole_survey_type = findViewById(R.id.pole_survey_type);
        dtr_structure_code_11kv = findViewById(R.id.dtr_structure_code_11kv);

        sp_voltage_level = findViewById(R.id.sp_voltage_level);
        sp_is_location_exists = findViewById(R.id.sp_is_location_exists);
        layout_location_exists = findViewById(R.id.layout_location_exists);
        ch_ext_loc_33kv = findViewById(R.id.ch_ext_loc_33kv);
        ch_ext_loc_11kv = findViewById(R.id.ch_ext_loc_11kv);
        ch_ext_loc_lt = findViewById(R.id.ch_ext_loc_lt);
        layout_parent_exists = findViewById(R.id.layout_parent_exists);
        sp_is_parent_exists = findViewById(R.id.sp_is_parent_exists);
        tapping_location = findViewById(R.id.tapping_location);
        layout_parent_connecting_point = findViewById(R.id.layout_parent_connecting_point);
        sp_parent_connecting_point = findViewById(R.id.sp_parent_connecting_point);
        layout_connecting_point = findViewById(R.id.layout_connecting_point);
        sp_connecting_point = findViewById(R.id.sp_connecting_point);


        sp_survey_pole_type = findViewById(R.id.sp_survey_pole_type);
        sp_exsting_connecting_point = findViewById(R.id.sp_exsting_connecting_point);


        sp_voltage_level.setOnItemSelectedListener(this);

        sp_is_location_exists.setOnItemSelectedListener(this);
        sp_is_parent_exists.setOnItemSelectedListener(this);
        sp_parent_connecting_point.setOnItemSelectedListener(this);
        sp_connecting_point.setOnItemSelectedListener(this);
        sp_sp_pole_desc_33__9_1_pole_support.setOnItemSelectedListener(this);
        sp_pole_ab_switch_type_33kv.setOnItemSelectedListener(this);
        sp_type_of_v_crossing_arms_33kv.setOnItemSelectedListener(this);
        sp_type_of_v_crossing_arms_11kv.setOnItemSelectedListener(this);
        sp_type_disc_insulators_33kv.setOnItemSelectedListener(this);
        sp_type_disc_insulators_lt.setOnItemSelectedListener(this);
        sp_no_of_channel_33kv.setOnItemSelectedListener(this);
        sp_ded_end_pole_33kv.setOnItemSelectedListener(this);
        sp_ded_end_pole_lt.setOnItemSelectedListener(this);
        sp_ded_end_pole_11kv.setOnItemSelectedListener(this);
        sp_type_disc_insulators_11kv.setOnItemSelectedListener(this);
        sp_type_top_cleat_33kv.setOnItemSelectedListener(this);
        sp_type_top_cleat_11kv.setOnItemSelectedListener(this);
        sp_pole_ab_switch_type_11kv.setOnItemSelectedListener(this);
        sp_pole_dtr_ab_switch_type_11kv.setOnItemSelectedListener(this);
        sp_hg_fuse_set_11kv.setOnItemSelectedListener(this);
        sp_lt_fuse_box_distribution_11kv.setOnItemSelectedListener(this);

        sp_survey_pole_type.setOnItemSelectedListener(this);
        sp_exsting_connecting_point.setOnItemSelectedListener(this);

        //33KV START
        sp_lineType_33 = findViewById(R.id.sp_lineType_33);
        sp_is_multi_circuit_33 = findViewById(R.id.sp_is_multi_circuit_33);
        ch_multi_33kv_33 = findViewById(R.id.ch_multi_33kv_33);
        ch_multi_11kv_33 = findViewById(R.id.ch_multi_11kv_33);
        ch_multi_lt_33 = findViewById(R.id.ch_multi_lt_33);
        ch_top_cleat_33kv = findViewById(R.id.ch_top_cleat_33kv);
        ch_tapping_cross_arms_33kv = findViewById(R.id.ch_tapping_cross_arms_33kv);
        ch_tapping_cross_arms_lt = findViewById(R.id.ch_tapping_cross_arms_lt);
        ch_pin_insulators_lt = findViewById(R.id.ch_pin_insulators_lt);
        ch_channel_33kv = findViewById(R.id.ch_channel_33kv);
        ch_channel_11kv = findViewById(R.id.ch_channel_11kv);
        sp_pole_type_33 = findViewById(R.id.sp_pole_type_33);
        sp_pole_support_33 = findViewById(R.id.sp_pole_support_33);
        sp_pole_configuration_33 = findViewById(R.id.sp_pole_configuration_33);
        multi_ckt_layout_33 = findViewById(R.id.multi_ckt_layout_33);
        sp_33kv_multi_ckt_pole_33 = findViewById(R.id.sp_33kv_multi_ckt_pole_33);
        sp_no_9_poles_33kv = findViewById(R.id.sp_no_9_poles_33kv);
        sp_no_8_poles_33kv = findViewById(R.id.sp_no_8_poles_33kv);
        ch_33kv_9_stuart_poles_33kv = findViewById(R.id.ch_33kv_9_stuart_poles_33kv);
        ch_33kv_8_stuart_poles_33kv = findViewById(R.id.ch_33kv_8_stuart_poles_33kv);
        ch_disc_insulators_33kv = findViewById(R.id.ch_disc_insulators_33kv);
        ch_disc_insulators_11kv = findViewById(R.id.ch_disc_insulators_11kv);
        ch_disc_insulators_lt = findViewById(R.id.ch_disc_insulators_lt);

        layout_33kv_multi_ckt_pole_33 = findViewById(R.id.layout_33kv_multi_ckt_pole_33);
        sp_pole_height_33 = findViewById(R.id.sp_pole_height_33);
        sp_conductor_33 = findViewById(R.id.sp_conductor_33);
        sp_no_of_count_side_arm_11kv = findViewById(R.id.sp_no_of_count_side_arm_11kv);
        sp_no_of_side_arm_11kv = findViewById(R.id.sp_no_of_side_arm_11kv);
        sp_no_of_top_cleat_11kv = findViewById(R.id.sp_no_of_top_cleat_11kv);
        sp_no_of_metal_insulators_11kv = findViewById(R.id.sp_no_of_metal_insulators_11kv);
        sp_no_of_disc_insulators_11kv = findViewById(R.id.sp_no_of_disc_insulators_11kv);
        sp_no_of_pin_insulators_11kv = findViewById(R.id.sp_no_of_pin_insulators_11kv);
        sp_no_of_v_crossing_arms_11kv = findViewById(R.id.sp_no_of_v_crossing_arms_11kv);
        sp_conductor_size_33 = findViewById(R.id.sp_conductor_size_33);
        sp_conductor_size_11 = findViewById(R.id.sp_conductor_size_11);
        sp_conductor_size_lt = findViewById(R.id.sp_conductor_size_lt);
        sp_no_of_stays_33kv = findViewById(R.id.sp_no_of_stays_33kv);
        sp_sp_pole_desc_33 = findViewById(R.id.sp_sp_pole_desc_33);
        sp_sp_pole_desc_11 = findViewById(R.id.sp_sp_pole_desc_11);
        sp_sp_pole_desc_lt_8_sturt = findViewById(R.id.sp_sp_pole_desc_lt_8_sturt);
        sp_sp_pole_desc_lt_stuart_9m = findViewById(R.id.sp_sp_pole_desc_lt_stuart_9m);


        tv_select_8m_pole_no_33kv = findViewById(R.id.tv_select_8m_pole_no_33kv);
        tv_select_9m_pole_no_33kv = findViewById(R.id.tv_select_9m_pole_no_33kv);
        tv_fabrication_materials_33kv = findViewById(R.id.tv_fabrication_materials_33kv);
        tv_fabrication_materials_11kv = findViewById(R.id.tv_fabrication_materials_11kv);
        tv_insulators_details_33kv = findViewById(R.id.tv_insulators_details_33kv);
        tv_no_stay_clamps_main_pole_33kv = findViewById(R.id.tv_no_stay_clamps_main_pole_33kv);
        tv_no_of_metal_parts_33kv = findViewById(R.id.tv_no_of_metal_parts_33kv);
        tv_no_of_metal_parts_11kv = findViewById(R.id.tv_no_of_metal_parts_11kv);
        tv_no_of_metal_parts_lt = findViewById(R.id.tv_no_of_metal_parts_lt);
        tv_insulators_details_11kv = findViewById(R.id.tv_insulators_details_11kv);
        tv_insulators_details_lt = findViewById(R.id.tv_insulators_details_lt);
        tv_fabrication_materials_lt = findViewById(R.id.tv_fabrication_materials_lt);


        sp_lineType_33.setOnItemSelectedListener(this);
        sp_is_multi_circuit_33.setOnItemSelectedListener(this);
        sp_pole_type_33.setOnItemSelectedListener(this);
        sp_pole_support_33.setOnItemSelectedListener(this);
        sp_pole_configuration_33.setOnItemSelectedListener(this);
        sp_33kv_multi_ckt_pole_33.setOnItemSelectedListener(this);
        sp_no_9_poles_33kv.setOnItemSelectedListener(this);
        sp_no_8_poles_33kv.setOnItemSelectedListener(this);
        sp_pole_height_33.setOnItemSelectedListener(this);
        sp_conductor_33.setOnItemSelectedListener(this);
        sp_conductor_size_33.setOnItemSelectedListener(this);
        sp_conductor_size_11.setOnItemSelectedListener(this);
        sp_conductor_size_lt.setOnItemSelectedListener(this);
        sp_no_of_stays_33kv.setOnItemSelectedListener(this);
        sp_sp_pole_desc_33.setOnItemSelectedListener(this);
        sp_no_of_side_arm_11kv.setOnItemSelectedListener(this);


        //33KV END

        //11KV START
        sp_lineType_11 = findViewById(R.id.sp_lineType_11);
        sp_is_multi_circuit_11 = findViewById(R.id.sp_is_multi_circuit_11);
        ch_multi_33kv_11 = findViewById(R.id.ch_multi_33kv_11);
        ch_multi_11kv_11 = findViewById(R.id.ch_multi_11kv_11);
        ch_multi_lt_11 = findViewById(R.id.ch_multi_lt_11);
        sp_pole_type_11 = findViewById(R.id.sp_pole_type_11);
        sp_pole_support_11 = findViewById(R.id.sp_pole_support_11);
        sp_pole_configuration_11 = findViewById(R.id.sp_pole_configuration_11);
        multi_ckt_layout_11 = findViewById(R.id.multi_ckt_layout_11);
        layout_33kv_multi_ckt_pole_11 = findViewById(R.id.layout_33kv_multi_ckt_pole_11);
        sp_33kv_multi_ckt_pole_11 = findViewById(R.id.sp_33kv_multi_ckt_pole_11);
        layout_11kv_multi_ckt_pole_11 = findViewById(R.id.layout_11kv_multi_ckt_pole_11);
        sp_11kv_multi_ckt_pole_11 = findViewById(R.id.sp_11kv_multi_ckt_pole_11);
        sp_sturt_8m_no_of_black_clamps_11kv = findViewById(R.id.sp_sturt_8m_no_of_black_clamps_11kv);
        sp_sturt_8m_no_of_black_clamps_33kv = findViewById(R.id.sp_sturt_8m_no_of_black_clamps_33kv);
        sp_sturt_8m_no_of_black_clamps_lt = findViewById(R.id.sp_sturt_8m_no_of_black_clamps_lt);
        sp_no_9_poles_11kv = findViewById(R.id.sp_no_9_poles_11kv);
        sp_no_8_poles_11kv = findViewById(R.id.sp_no_8_poles_11kv);
        sp_no_stays_11kv = findViewById(R.id.sp_no_stays_11kv);
        ch_33kv_8_stuart_poles_11kv = findViewById(R.id.ch_33kv_8_stuart_poles_11kv);
        ch_33kv_9_stuart_poles_11kv = findViewById(R.id.ch_33kv_9_stuart_poles_11kv);
        ch_ci_pipe = findViewById(R.id.ch_ci_pipe);
        ch_gi_pipe = findViewById(R.id.ch_gi_pipe);
        sp_ci_pipes = findViewById(R.id.sp_ci_pipes);
        sp_gi_pipes = findViewById(R.id.sp_gi_pipes);
        ch_top_cleat = findViewById(R.id.ch_top_cleat_11kc);
        ch_tapping_cross_arms_11kv = findViewById(R.id.ch_tapping_cross_arms_11kv);
        ch_no_of_v_crossing_11kv = findViewById(R.id.ch_no_of_v_crossing_11kv);
        ch_no_of_v_crossing_33kv = findViewById(R.id.ch_no_of_v_crossing_33kv);
        ch_pin_insulators_33kv = findViewById(R.id.ch_pin_insulators_33kv);
        ch_back_clamps_33kv = findViewById(R.id.ch_back_clamps_33kv);

        ch_back_clamps_11kv = findViewById(R.id.ch_back_clamps_11kv);
        ch_side_arm_33kv = findViewById(R.id.ch_side_arm_33kv);
        ch_side_arm_11kv = findViewById(R.id.ch_side_arm_11kv);
        ch_side_arm_lt = findViewById(R.id.ch_side_arm_lt);
        ch_pin_insulators_11kv = findViewById(R.id.ch_pin_insulators_11kv);
        sp_tapping_x_arm_33kv = findViewById(R.id.sp_tapping_x_arm_33kv);
        sp_tapping_x_arm_lt = findViewById(R.id.sp_tapping_x_arm_lt);
        sp_channel_33kv = findViewById(R.id.sp_channel_33kv);
        sp_side_arm_33kv = findViewById(R.id.sp_side_arm_33kv);
        sp_type_black_clamps_33kv = findViewById(R.id.sp_type_black_clamps_33kv);
        sp_sturt_type_black_clamps_33kv = findViewById(R.id.sp_sturt_type_black_clamps_33kv);
        sp_sturt_no_of_black_clamps_33kv = findViewById(R.id.sp_sturt_no_of_black_clamps_33kv);
        sp_sturt_type_black_clamps_11kv = findViewById(R.id.sp_sturt_type_black_clamps_11kv);
        sp_sturt_type_black_clamps_lt = findViewById(R.id.sp_sturt_type_black_clamps_lt);
        sp_gi_pins_type_lt = findViewById(R.id.sp_gi_pins_type_lt);
        sp_no_of_gi_pins_lt = findViewById(R.id.sp_no_of_gi_pins_lt);
        sp_sturt_8m_type_black_clamps_33kv = findViewById(R.id.sp_sturt_8m_type_black_clamps_33kv);
        sp_sturt_8m_type_black_clamps_11kv = findViewById(R.id.sp_sturt_8m_type_black_clamps_11kv);
        sp_sturt_8m_type_black_clamps_lt = findViewById(R.id.sp_sturt_8m_type_black_clamps_lt);
        sp_main_pole_stay_clamps_33kv = findViewById(R.id.sp_main_pole_stay_clamps_33kv);
        sp_main_pole_stay_clamps_11kv = findViewById(R.id.sp_main_pole_stay_clamps_11kv);
        sp_main_pole_stay_clamps_lt = findViewById(R.id.sp_main_pole_stay_clamps_lt);
        sp_no_of_black_clamps_33kv = findViewById(R.id.sp_no_of_black_clamps_33kv);
        sp_no_of_black_clamps_11kv = findViewById(R.id.sp_no_of_black_clamps_11kv);
        sp_no_of_black_clamps_lt = findViewById(R.id.sp_no_of_black_clamps_lt);
        sp_type_black_clamps_11kv = findViewById(R.id.sp_type_black_clamps_11kv);
        sp_type_black_clamps_lt = findViewById(R.id.sp_type_black_clamps_lt);
        sp_side_arm_11kv = findViewById(R.id.sp_side_arm_11kv);
        sp_side_arm_lt = findViewById(R.id.sp_side_arm_lt);
        sp_channel_11kv = findViewById(R.id.sp_channel_11kv);
        sp_tapping_x_arm_11kv = findViewById(R.id.sp_tapping_x_arm_11kv);
        sp_no_of_tapping_cross_arms_11kv = findViewById(R.id.sp_no_of_tapping_cross_arms_11kv);
        sp_sturt_no_of_black_clamps_11kv = findViewById(R.id.sp_sturt_no_of_black_clamps_11kv);
        sp_sturt_no_of_black_clamps_lt = findViewById(R.id.sp_sturt_no_of_black_clamps_lt);

        tv_select_9m_pole_no_11kv = findViewById(R.id.tv_select_9m_pole_no_11kv);
        tv_select_8m_pole_no_11kv = findViewById(R.id.tv_select_8m_pole_no_11kv);

        layout_sp_no_of_pole_support_33 = findViewById(R.id.layout_sp_no_of_pole_support_33);
        layout_sp_no_of_pole_support_11 = findViewById(R.id.layout_sp_no_of_pole_support_11);
        layout_sp_no_of_pole_support_lt = findViewById(R.id.layout_sp_no_of_pole_support_lt);
        layout_sp_pole_desc_lt = findViewById(R.id.layout_sp_pole_desc_lt);
        layout_sp_no_of_pole_stay_support_33 = findViewById(R.id.layout_sp_no_of_pole_stay_support_33);
        layout_sp_pole_height_33 = findViewById(R.id.layout_sp_pole_height_33);
        layout_conductor_33 = findViewById(R.id.layout_conductor_33);
        layout_sp_pole_desc_lt_8_sturt = findViewById(R.id.layout_sp_pole_desc_lt_8_sturt);
        layout_sp_pole_desc_lt_stuart_9m = findViewById(R.id.layout_sp_pole_desc_lt_stuart_9m);
        layout_conductor_size_33 = findViewById(R.id.layout_conductor_size_33);
        layout_conductor_size_11 = findViewById(R.id.layout_conductor_size_11);
        layout_conductor_size_lt = findViewById(R.id.layout_conductor_size_lt);
        layout_conductor_11 = findViewById(R.id.layout_conductor_11);
        layout_conductor_lt = findViewById(R.id.layout_conductor_lt);
        layout_sp_pole_height_11 = findViewById(R.id.layout_sp_pole_height_11);
        layout_sp_pole_height_lt = findViewById(R.id.layout_sp_pole_height_lt);
        layout_sp_no_of_pole_stay_support_11 = findViewById(R.id.layout_sp_no_of_pole_stay_support_11);
        layout_sp_no_of_pole_stay_support_lt = findViewById(R.id.layout_sp_no_of_pole_stay_support_lt);
        tv_select_pole_no_text_11 = findViewById(R.id.tv_select_pole_no_text_11);
        sp_pole_height_11 = findViewById(R.id.sp_pole_height_11);
        sp_conductor_11 = findViewById(R.id.sp_conductor_11);
        sp_dtr_phase_11 = findViewById(R.id.sp_dtr_phase_11);
        sp_dtr_phase_desc_11 = findViewById(R.id.sp_dtr_phase_desc_11);
        sp_sp_pole_desc_33_8_pole_support = findViewById(R.id.sp_sp_pole_desc_33_8_pole_support);
        sp_lt_fuse_distribution_11kv = findViewById(R.id.sp_lt_fuse_distribution_11kv);
        sp_sp_pole_desc_11kv_stuart_9m = findViewById(R.id.sp_sp_pole_desc_11kv_stuart_9m);
        sp_dtr_capacity_phase1_11 = findViewById(R.id.sp_dtr_capacity_phase1_11);
        sp_dtr_capacity_phase2_11 = findViewById(R.id.sp_dtr_capacity_phase2_11);
        sp_sp_pole_desc_11kv_8_sturt = findViewById(R.id.sp_sp_pole_desc_11kv_8_sturt);
        sp_dtr_mounting_11 = findViewById(R.id.sp_dtr_mounting_11);
        sp_dtr_survey_11 = findViewById(R.id.sp_dtr_survey_11);
        sp_pole_pin_cut_point_11 = findViewById(R.id.sp_pole_pin_cut_point_11);
        sp_pole_pin_cut_point_33kv = findViewById(R.id.sp_pole_pin_cut_point_33kv);
        sp_pole_pin_cut_point_lt = findViewById(R.id.sp_pole_pin_cut_point_lt);
        sp_pin_insulators_type_33kv = findViewById(R.id.sp_pin_insulators_type_33kv);
        sp_type_metal_33kv = findViewById(R.id.sp_type_metal_33kv);
        sp_type_metal_11kv = findViewById(R.id.sp_type_metal_11kv);
        sp_no_main_pole_stay_clamps_33kv = findViewById(R.id.sp_no_main_pole_stay_clamps_33kv);
        sp_no_main_pole_stay_clamps_11kv = findViewById(R.id.sp_no_main_pole_stay_clamps_11kv);
        sp_no_main_pole_stay_clamps_lt = findViewById(R.id.sp_no_main_pole_stay_clamps_lt);
        sp_type_metal_lt = findViewById(R.id.sp_type_metal_lt);
        sp_pin_insulators_type_lt = findViewById(R.id.sp_pin_insulators_type_lt);
        sp_pin_insulators_type_11kv = findViewById(R.id.sp_pin_insulators_type_11kv);
        sp_no_of_top_cleat_33kv = findViewById(R.id.sp_no_of_top_cleat_33kv);
        sp_no_of_tapping_cross_arms_33kv = findViewById(R.id.sp_no_of_tapping_cross_arms_33kv);
        sp_no_of_tapping_cross_arms_lt = findViewById(R.id.sp_no_of_tapping_cross_arms_lt);
        sp_no_of_v_crossing_arms_33kv = findViewById(R.id.sp_no_of_v_crossing_arms_33kv);
        sp_no_of_count_side_arm_33kv = findViewById(R.id.sp_no_of_count_side_arm_33kv);
        sp_no_of_disc_insulators_33kv = findViewById(R.id.sp_no_of_disc_insulators_33kv);
        sp_no_of_metal_insulators_33kv = findViewById(R.id.sp_no_of_metal_insulators_33kv);
        sp_no_of_metal_insulators_lt = findViewById(R.id.sp_no_of_metal_insulators_lt);
        sp_no_of_pin_insulators_33kv = findViewById(R.id.sp_no_of_pin_insulators_33kv);
        sp_no_of_disc_insulators_lt = findViewById(R.id.sp_no_of_disc_insulators_lt);
        sp_no_of_pin_insulators_lt = findViewById(R.id.sp_no_of_pin_insulators_lt);


        sp_lineType_11.setOnItemSelectedListener(this);
        sp_is_multi_circuit_11.setOnItemSelectedListener(this);
        sp_pole_type_11.setOnItemSelectedListener(this);
        sp_pole_support_11.setOnItemSelectedListener(this);
        sp_pole_configuration_11.setOnItemSelectedListener(this);
        sp_33kv_multi_ckt_pole_11.setOnItemSelectedListener(this);
        sp_11kv_multi_ckt_pole_11.setOnItemSelectedListener(this);
        sp_sturt_8m_no_of_black_clamps_11kv.setOnItemSelectedListener(this);
        sp_sturt_8m_no_of_black_clamps_33kv.setOnItemSelectedListener(this);
        sp_sturt_8m_no_of_black_clamps_lt.setOnItemSelectedListener(this);
        sp_no_stays_11kv.setOnItemSelectedListener(this);
        sp_pole_height_11.setOnItemSelectedListener(this);
        sp_conductor_11.setOnItemSelectedListener(this);
        sp_dtr_phase_11.setOnItemSelectedListener(this);
        sp_dtr_phase_desc_11.setOnItemSelectedListener(this);
        sp_dtr_capacity_phase1_11.setOnItemSelectedListener(this);
        sp_dtr_capacity_phase2_11.setOnItemSelectedListener(this);
        sp_dtr_mounting_11.setOnItemSelectedListener(this);
        sp_dtr_survey_11.setOnItemSelectedListener(this);
        sp_sp_pole_desc_11.setOnItemSelectedListener(this);
        sp_sp_pole_desc_33_8_pole_support.setOnItemSelectedListener(this);
        sp_sp_pole_desc_11kv_stuart_9m.setOnItemSelectedListener(this);
        sp_sp_pole_desc_11kv_8_sturt.setOnItemSelectedListener(this);
        sp_lt_fuse_distribution_11kv.setOnItemSelectedListener(this);
        sp_ci_pipes.setOnItemSelectedListener(this);
        sp_gi_pipes.setOnItemSelectedListener(this);
        sp_pole_pin_cut_point_11.setOnItemSelectedListener(this);
        sp_pole_pin_cut_point_33kv.setOnItemSelectedListener(this);
        sp_pole_pin_cut_point_lt.setOnItemSelectedListener(this);
        sp_no_of_tapping_cross_arms_11kv.setOnItemSelectedListener(this);
        sp_sturt_no_of_black_clamps_11kv.setOnItemSelectedListener(this);
        sp_sturt_no_of_black_clamps_lt.setOnItemSelectedListener(this);
        sp_no_of_top_cleat_33kv.setOnItemSelectedListener(this);
        sp_tapping_x_arm_33kv.setOnItemSelectedListener(this);
        sp_tapping_x_arm_lt.setOnItemSelectedListener(this);
        sp_tapping_x_arm_11kv.setOnItemSelectedListener(this);
        sp_channel_33kv.setOnItemSelectedListener(this);
        sp_channel_11kv.setOnItemSelectedListener(this);
        sp_side_arm_33kv.setOnItemSelectedListener(this);
        sp_type_black_clamps_33kv.setOnItemSelectedListener(this);
        sp_sturt_type_black_clamps_33kv.setOnItemSelectedListener(this);
        sp_sturt_no_of_black_clamps_33kv.setOnItemSelectedListener(this);
        sp_sturt_type_black_clamps_lt.setOnItemSelectedListener(this);
        sp_gi_pins_type_lt.setOnItemSelectedListener(this);
        sp_no_of_gi_pins_lt.setOnItemSelectedListener(this);
        sp_sturt_type_black_clamps_11kv.setOnItemSelectedListener(this);
        sp_sturt_8m_type_black_clamps_33kv.setOnItemSelectedListener(this);
        sp_sturt_8m_type_black_clamps_11kv.setOnItemSelectedListener(this);
        sp_sturt_8m_type_black_clamps_lt.setOnItemSelectedListener(this);
        sp_main_pole_stay_clamps_33kv.setOnItemSelectedListener(this);
        sp_main_pole_stay_clamps_11kv.setOnItemSelectedListener(this);
        sp_main_pole_stay_clamps_lt.setOnItemSelectedListener(this);
        sp_no_of_black_clamps_33kv.setOnItemSelectedListener(this);
        sp_no_of_black_clamps_11kv.setOnItemSelectedListener(this);
        sp_no_of_black_clamps_lt.setOnItemSelectedListener(this);
        sp_type_black_clamps_11kv.setOnItemSelectedListener(this);
        sp_type_black_clamps_lt.setOnItemSelectedListener(this);
        sp_side_arm_11kv.setOnItemSelectedListener(this);
        sp_type_metal_33kv.setOnItemSelectedListener(this);
        sp_no_main_pole_stay_clamps_33kv.setOnItemSelectedListener(this);
        sp_no_main_pole_stay_clamps_11kv.setOnItemSelectedListener(this);
        sp_no_main_pole_stay_clamps_lt.setOnItemSelectedListener(this);
        sp_type_metal_lt.setOnItemSelectedListener(this);
        sp_type_metal_11kv.setOnItemSelectedListener(this);
        sp_side_arm_lt.setOnItemSelectedListener(this);

        sp_no_of_tapping_cross_arms_33kv.setOnItemSelectedListener(this);
        sp_no_of_tapping_cross_arms_lt.setOnItemSelectedListener(this);
        sp_no_of_v_crossing_arms_33kv.setOnItemSelectedListener(this);
        sp_no_of_pin_insulators_33kv.setOnItemSelectedListener(this);
        sp_no_of_pin_insulators_lt.setOnItemSelectedListener(this);
        sp_no_of_disc_insulators_33kv.setOnItemSelectedListener(this);
        sp_no_of_disc_insulators_11kv.setOnItemSelectedListener(this);
        sp_no_of_metal_insulators_33kv.setOnItemSelectedListener(this);
        sp_no_of_metal_insulators_lt.setOnItemSelectedListener(this);
        sp_no_of_count_side_arm_33kv.setOnItemSelectedListener(this);
        sp_no_of_disc_insulators_lt.setOnItemSelectedListener(this);


        //11KV END

        //LT START
        sp_lineType_lt = findViewById(R.id.sp_lineType_lt);
        sp_is_multi_circuit_lt = findViewById(R.id.sp_is_multi_circuit_lt);
        ch_multi_33kv_lt = findViewById(R.id.ch_multi_33kv_lt);
        ch_multi_11kv_lt = findViewById(R.id.ch_multi_11kv_lt);
        ch_multi_lt_lt = findViewById(R.id.ch_multi_lt_lt);
        sp_pole_type_lt = findViewById(R.id.sp_pole_type_lt);
        sp_pole_support_lt = findViewById(R.id.sp_pole_support_lt);
        sp_pole_configuration_lt = findViewById(R.id.sp_pole_configuration_lt);
        multi_ckt_layout_lt = findViewById(R.id.multi_ckt_layout_lt);
        sp_no_9_poles_lt = findViewById(R.id.sp_no_9_poles_lt);
        sp_no_8_poles_lt = findViewById(R.id.sp_no_8_poles_lt);
        sp_no_stays_lt = findViewById(R.id.sp_no_stays_lt);
        sp_sp_pole_desc_lt = findViewById(R.id.sp_sp_pole_desc_lt);

        tv_select_9m_pole_no_lt = findViewById(R.id.tv_select_9m_pole_no_lt);
        tv_select_8m_pole_no_lt = findViewById(R.id.tv_select_8m_pole_no_lt);

        layout_33kv_multi_ckt_pole_lt = findViewById(R.id.layout_33kv_multi_ckt_pole_lt);
        sp_33kv_multi_ckt_pole_lt = findViewById(R.id.sp_33kv_multi_ckt_pole_lt);
        layout_11kv_multi_ckt_pole_lt = findViewById(R.id.layout_11kv_multi_ckt_pole_lt);
        sp_11kv_multi_ckt_pole_lt = findViewById(R.id.sp_11kv_multi_ckt_pole_lt);
        layout_lt_multi_ckt_pole_lt = findViewById(R.id.layout_lt_multi_ckt_pole_lt);
        sp_lt_multi_ckt_pole_lt = findViewById(R.id.sp_lt_multi_ckt_pole_lt);
        tv_select_pole_no_text_lt = findViewById(R.id.tv_select_pole_no_text_lt);
        sp_pole_height_lt = findViewById(R.id.sp_pole_height_lt);
        sp_conductor_lt = findViewById(R.id.sp_conductor_lt);
        ch_33kv_8_stuart_poles_lt = findViewById(R.id.ch_33kv_8_stuart_poles_lt);
        ch_33kv_9_stuart_poles_lt = findViewById(R.id.ch_33kv_9_stuart_poles_lt);
        ch_no_of_v_crossing_lt = findViewById(R.id.ch_no_of_v_crossing_lt);
        ch_top_cleat_lt = findViewById(R.id.ch_top_cleat_lt);


        sp_lineType_lt.setOnItemSelectedListener(this);
        sp_is_multi_circuit_lt.setOnItemSelectedListener(this);
        sp_pole_type_lt.setOnItemSelectedListener(this);
        sp_pole_support_lt.setOnItemSelectedListener(this);
        sp_pole_configuration_lt.setOnItemSelectedListener(this);
        sp_33kv_multi_ckt_pole_lt.setOnItemSelectedListener(this);
        sp_11kv_multi_ckt_pole_lt.setOnItemSelectedListener(this);
        sp_lt_multi_ckt_pole_lt.setOnItemSelectedListener(this);
        sp_no_8_poles_lt.setOnItemSelectedListener(this);
        sp_no_9_poles_lt.setOnItemSelectedListener(this);
        sp_pole_height_lt.setOnItemSelectedListener(this);
        sp_conductor_lt.setOnItemSelectedListener(this);
        sp_line_survey_type.setOnItemSelectedListener(this);
        sp_no_stays_lt.setOnItemSelectedListener(this);
        sp_sp_pole_desc_lt.setOnItemSelectedListener(this);
        sp_sp_pole_desc_lt_8_sturt.setOnItemSelectedListener(this);
        sp_sp_pole_desc_lt_stuart_9m.setOnItemSelectedListener(this);


        isCompleted33kVServey = databaseEstimates.get33kvSurveyStatusNEW(EST_NO,GIS_ID);
        isCompleted11kVServey = databaseEstimates.get11kvSurveyStatusNEW(EST_NO,GIS_ID);
        isCompletedLTServey = databaseEstimates.getLTSurveyStatusNEW(EST_NO,GIS_ID);

        Log.d("COMPLETED_STATUSS", "onCreate: " + isCompleted33kVServey + " " + isCompleted11kVServey + " " + isCompletedLTServey);
        Log.d("COMPLETED_STATUSS_", "onCreate: " + is33KVSelected + " " + is11KVSelected + " " + isLTSelected);


        if (is33KVSelected) {
            if (!isCompleted33kVServey) {
                voltage_level.add("33KV");
            }
        }
        if (is11KVSelected) {
            if (is33KVSelected && isCompleted33kVServey) {
                voltage_level.add("11KV");
                voltage_level.add("6.3KV");
            } else {
                if (!is33KVSelected) {
                    voltage_level.add("11KV");
                }
            }
        }
        if (isMeterSelected) {
            voltage_level.add("Meters");
        }


        if (isLTSelected) {
            if (is33KVSelected && isCompleted33kVServey && is11KVSelected && isCompleted11kVServey) {
                voltage_level.clear();
                voltage_level.add("LT");
            } else if (!isCompleted33kVServey && is33KVSelected) {
                voltage_level.clear();
                voltage_level.add("33KV");
            } else if (!isCompleted11kVServey && is11KVSelected) {
                voltage_level.clear();
                voltage_level.add("11KV");
                voltage_level.add("6.3KV");
            } else {
                voltage_level.clear();
                voltage_level.add("LT");
            }
        }
        if (is33KVSelected && is11KVSelected && isLTSelected) {
            layout_survey_pole_type.setVisibility(View.VISIBLE);
        } else if (is33KVSelected && is11KVSelected) {
            layout_survey_pole_type.setVisibility(View.VISIBLE);
        } else if (is33KVSelected && isLTSelected) {
            layout_survey_pole_type.setVisibility(View.VISIBLE);
        } else if (is11KVSelected && isLTSelected) {
            layout_survey_pole_type.setVisibility(View.VISIBLE);
        } else {
            layout_survey_pole_type.setVisibility(View.GONE);
        }


        dtr_structure_code_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchableSpinner searchableSpinner = new SearchableSpinner(context);
                searchableSpinner.setWindowTitle("SELECT DTR CODE");
                searchableSpinner.setSpinnerListItems(EstimatesHome.dtr);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        dtr_structure_code_11kv.setText(selectedString);
                    }
                });
            }
        });

        //Distance
        int a = (int) Math.round(lastPoleDistance);
        last_pole_distance.setText("" + a);

        int b = (int) Math.round(currentDistance);
        current_distance.setText("" + b);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, voltage_level);
        sp_voltage_level.setAdapter(adapter);
        setPoleTypeAdapter();

        setPoleConfigurationSpinner33kv();
        setPoleConfigurationSpinner11kv();


        if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
            statusList = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false,GIS_ID);

            if (statusList != null && !statusList.isEmpty()) {
                NewModel lastModel = statusList.get(statusList.size() - 1);

                String conductorDesc = lastModel.getCONDUCTOR_DESC();
                String conductor_size = lastModel.getCONDUTCOR_SIZE();
                String LINE_SURVEY_TYPE = lastModel.getLINE_SURVEY_TYPE();
                String LINE_TYPE = lastModel.getLINE_TYPE();
                String POLE_CONFIG = lastModel.getPOLE_CONFIG();
                String POLE_DESC = lastModel.getPOLE_DESC();
                String POLE_HEIGHT = lastModel.getPOLE_HEIGHT();
                String POLE_LOCATION_TYPE = lastModel.getPOLE_LOCATION_TYPE();
                String POLE_SUPPORT = lastModel.getPOLE_SUPPORT();
                String POLE_TYPE = lastModel.getPOLE_TYPE();
                String IS_EXISTING_LOCATION_NEW = lastModel.getIS_EXISTING_LOCATION_NEW();
            }


            Log.d("STATUS_LIST", "onCreate: " + statusList);

            if (!statusList.isEmpty()) {
                btn_repeat_pole_estimates.setVisibility(View.GONE);
            }
        } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
            statusList = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false,GIS_ID);

            Log.d("STATUS_LIST_11KV--->", "onCreate: " + statusList);

            if (!statusList.isEmpty()) {
                btn_repeat_pole_estimates.setVisibility(View.GONE);
            }
        } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
            statusList = databaseEstimates.getNewSurveyData(EST_NO, "LT", false,GIS_ID);

            Log.d("STATUS_LIST", "onCreate: " + statusList);

            if (!statusList.isEmpty()) {
                btn_repeat_pole_estimates.setVisibility(View.GONE);
            }
        }
        btn_repeat_pole_estimates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
                btn_repeat_pole_estimates.startAnimation(animation);
                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    repeatPole33KV();
                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    repeatPole11KV();
                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    repeatPoleLT();
                }
            }
        });


        loadPoleHeightDataStrut9M33kv("9.1");
        loadPoleHeightDataStrut8M33kv("8");
        loadPoleHeightDataStrut9M11kv("9.1");
        loadPoleHeightDataStrut8M11kv("8");
        loadPoleHeightDataStrut8MLT("8");
        loadPoleHeightDataStrut9MLT("9.1");

        ArrayList<NewModel> editList = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true,GIS_ID);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(editList);
       // Log.d("Debugg", "onCreate: " + json);
      //  System.out.println("IS EDIT : "+isEdit);
        if (isEdit) {

            if (editList != null && !editList.isEmpty()) {
                NewModel lastItem = editList.get(editList.size() - 1);
                String lastSurveyType = lastItem.getVOLTAGE_LEVEL();
                int poleNum = Integer.parseInt(lastItem.getPOLE_NO());
                String surveyType = lastItem.getIS_EXISTING_LOCATION_NEW();
                String poleFrom = lastItem.getFROM_POLE();
                String lineType = lastItem.getLINE_TYPE();
                String multiCircuit = lastItem.getIS_MULTI_CKT();

                if (lineType.equalsIgnoreCase("OverHead")) {
                    posLineType = 0;
                } else {
                    posLineType = 1;
                }
                sp_lineType_11.setSelection(posLineType, true);
                if (multiCircuit.equalsIgnoreCase("No")) {
                    posMultiCirCuit = 1;
                } else {
                    posMultiCirCuit = 2;
                }
                sp_is_multi_circuit_11.setSelection(posMultiCirCuit, true);
                // sp_connecting_point.setSelection(Integer.parseInt(poleFrom),true);
                if (surveyType.equalsIgnoreCase("Proposed")) {
                    posSurveyType = 0;
                } else {
                    posSurveyType = 1;
                }


                sp_is_location_exists.setSelection(posSurveyType, true);
                if (lastSurveyType.equalsIgnoreCase("33KV")) {
                    pos = 1;
                } else if (lastSurveyType.equalsIgnoreCase("11KV")) {
                    pos = 2;
                } else {
                    pos = 3;
                }
                sp_voltage_level.setSelection(pos, true);
                location_no.setText("" + poleNum);
                layout_parent_exists.setVisibility(View.GONE);
                survey_33.setVisibility(View.VISIBLE);
                survey_11.setVisibility(View.GONE);
                survey_lt.setVisibility(View.GONE);
                if (list33.isEmpty()) {
                    layout_connecting_point.setVisibility(View.GONE);
                } else {
                    layout_connecting_point.setVisibility(View.VISIBLE);
                    layout_distance.setVisibility(View.VISIBLE);
                    List<String> list_pst = new ArrayList<>();

                    for (int i = 0; i < list33.size(); i++) {
                        list_pst.add(list33.get(i).getPOLE_NO());
                    }
                    // list_pst.add("Select");
                    Collections.reverse(list_pst);
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_connecting_point.setAdapter(adapter_pst);
                    sp_connecting_point.setSelection(0, true);
                }
            }
        }
        //LT END


        ch_ci_pipe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_sp_ci_pipes.setVisibility(View.VISIBLE);
                } else {
                    layout_sp_ci_pipes.setVisibility(View.GONE);
                }
            }
        });

        ch_top_cleat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_top_cleat.setChecked(true);
                    loadTopCleat11KV();
                    layout_no_top_cleat_11kv.setVisibility(View.VISIBLE);
                } else {
                    ch_top_cleat.setChecked(false);
                    layout_no_top_cleat_11kv.setVisibility(View.GONE);
                }
            }
        });

        ch_disc_insulators_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_disc_insulators_33kv.setChecked(true);
                    loadDiscInsulators33KV(true);
                    layout_type_of_disc_insulator_33kv.setVisibility(View.VISIBLE);
                } else {
                    ch_disc_insulators_33kv.setChecked(false);
                    layout_type_of_disc_insulator_33kv.setVisibility(View.GONE);
                }
            }
        });

        ch_disc_insulators_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_disc_insulators_lt.setChecked(true);
                    loadDiscInsulatorsLt();
                    layout_type_of_disc_insulator_lt.setVisibility(View.VISIBLE);
                } else {
                    ch_disc_insulators_lt.setChecked(false);
                    layout_type_of_disc_insulator_lt.setVisibility(View.GONE);
                }
            }
        });


        ch_disc_insulators_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_disc_insulators_11kv.setChecked(true);
                    loadDiscInsulators11KV();

                    layout_type_of_disc_insulator_11kv.setVisibility(View.VISIBLE);
                } else {
                    ch_disc_insulators_11kv.setChecked(false);
                    layout_type_of_disc_insulator_11kv.setVisibility(View.GONE);
                }
            }
        });

        ch_side_arm_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_side_arm_11kv.setChecked(true);
                    loadSideArm11KV();
                    layout_no_side_arm_11kv.setVisibility(View.VISIBLE);
                } else {
                    ch_side_arm_11kv.setChecked(false);
                    layout_no_side_arm_11kv.setVisibility(View.GONE);
                }
            }
        });
        ch_side_arm_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_side_arm_lt.setChecked(true);
                    loadSideArmLT();
                    layout_no_side_arm_lt.setVisibility(View.VISIBLE);
                } else {
                    ch_side_arm_lt.setChecked(false);
                    layout_no_side_arm_lt.setVisibility(View.GONE);
                }
            }
        });

        ch_side_arm_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_side_arm_33kv.setChecked(true);
                    loadSideArm33KV();
                    layout_no_side_arm_33kv.setVisibility(View.VISIBLE);
                } else {
                    ch_side_arm_33kv.setChecked(false);
                    layout_no_side_arm_33kv.setVisibility(View.GONE);
                }
            }
        });

        ch_pin_insulators_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_pin_insulators_lt.setChecked(true);
                    loadPinInsulatorsLT();
                    loadLTGIPins();
                    layout_no_of_pin_insulators_type_lt.setVisibility(View.VISIBLE);
                    layout_no_of_gi_pins_type_lt.setVisibility(View.VISIBLE);
                } else {
                    ch_pin_insulators_lt.setChecked(false);
                    layout_no_of_pin_insulators_type_lt.setVisibility(View.GONE);
                    layout_no_of_gi_pins_type_lt.setVisibility(View.GONE);
                }
            }
        });


        ch_pin_insulators_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_pin_insulators_33kv.setChecked(true);
                    loadPinInsulators33KV();
                    layout_no_of_pin_insulators_type_33kv.setVisibility(View.VISIBLE);
                } else {
                    ch_pin_insulators_33kv.setChecked(false);
                    layout_no_of_pin_insulators_type_33kv.setVisibility(View.GONE);
                }
            }
        });


        ch_back_clamps_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_back_clamps_33kv.setChecked(true);
                    loadClamps33KV(sp_pole_type_33.getSelectedItem().toString(), sp_pole_height_33.getSelectedItem().toString());
                    layout_type_of_black_clamps_33kv.setVisibility(View.VISIBLE);
                } else {
                    ch_back_clamps_33kv.setChecked(false);
                    layout_type_of_black_clamps_33kv.setVisibility(View.GONE);
                }
            }
        });


        ch_back_clamps_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_back_clamps_11kv.setChecked(true);
                    loadClamps33KV(sp_pole_type_11.getSelectedItem().toString(), sp_pole_height_11.getSelectedItem().toString());
                    layout_type_of_black_clamps_11kv.setVisibility(View.VISIBLE);
                } else {
                    ch_back_clamps_11kv.setChecked(false);
                    layout_type_of_black_clamps_11kv.setVisibility(View.GONE);
                }
            }
        });

        ch_pin_insulators_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_pin_insulators_11kv.setChecked(true);
                    loadPinInsulators11KV();
                    layout_no_of_pin_insulators_type_11kv.setVisibility(View.VISIBLE);
                } else {
                    ch_pin_insulators_11kv.setChecked(false);

                    layout_no_of_pin_insulators_type_11kv.setVisibility(View.GONE);
                }
            }
        });

        ch_top_cleat_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_top_cleat_lt.setChecked(true);
                    loadTopCleat33KV();
                    layout_no_top_cleat_lt.setVisibility(View.VISIBLE);
                } else {
                    ch_top_cleat_lt.setChecked(false);
                    layout_no_top_cleat_lt.setVisibility(View.GONE);
                }
            }
        });

        ch_top_cleat_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_top_cleat_33kv.setChecked(true);
                    loadTopCleat33KV();
                    layout_no_top_cleat_33kv.setVisibility(View.VISIBLE);
                } else {
                    ch_top_cleat_33kv.setChecked(false);
                    layout_no_top_cleat_33kv.setVisibility(View.GONE);
                }
            }
        });


        ch_tapping_cross_arms_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_tapping_cross_arms_11kv.setChecked(true);
                    loadTappingCross11KV();
                    layout_no_tapping_cross_arms_11kv.setVisibility(View.VISIBLE);
                } else {
                    ch_tapping_cross_arms_11kv.setChecked(false);
                    layout_no_tapping_cross_arms_11kv.setVisibility(View.GONE);
                }
            }
        });

        ch_tapping_cross_arms_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_tapping_cross_arms_lt.setChecked(true);
                    loadTappingXArmsLT();
                    layout_no_tapping_cross_arms_lt.setVisibility(View.VISIBLE);
                } else {
                    ch_tapping_cross_arms_lt.setChecked(false);
                    layout_no_tapping_cross_arms_lt.setVisibility(View.GONE);
                }
            }
        });

        ch_tapping_cross_arms_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_tapping_cross_arms_33kv.setChecked(true);
                    loadTappingXArms33KV();
                    layout_no_tapping_cross_arms_33kv.setVisibility(View.VISIBLE);
                } else {
                    ch_tapping_cross_arms_33kv.setChecked(false);
                    layout_no_tapping_cross_arms_33kv.setVisibility(View.GONE);
                }
            }
        });

        ch_channel_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_channel_33kv.setChecked(true);
                    loadChannels33KV();
                    layout_no_channel_33kv.setVisibility(View.VISIBLE);
                } else {
                    ch_channel_33kv.setChecked(false);
                    layout_no_channel_33kv.setVisibility(View.GONE);
                }
            }
        });

        ch_channel_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_channel_11kv.setChecked(true);
                    loadChannels11KV();
                    layout_no_channel_11kv.setVisibility(View.VISIBLE);
                } else {
                    ch_channel_11kv.setChecked(false);
                    layout_no_channel_11kv.setVisibility(View.GONE);
                }
            }
        });

        ch_no_of_v_crossing_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_no_of_v_crossing_11kv.setChecked(true);
                    ch_top_cleat.setChecked(true);
                    loadVCrossArms11KV();
                    layout_no_v_crossing_arms_11kv.setVisibility(View.VISIBLE);
                } else {
                    ch_top_cleat.setChecked(false);
                    ch_no_of_v_crossing_11kv.setChecked(false);
                    layout_no_v_crossing_arms_11kv.setVisibility(View.GONE);
                }
            }
        });

        ch_no_of_v_crossing_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_no_of_v_crossing_lt.setChecked(true);
                    loadVCrossArms33KV();
                    layout_no_v_crossing_arms_lt.setVisibility(View.VISIBLE);
                    ch_top_cleat_33kv.setChecked(true);
                } else {
                    ch_top_cleat_33kv.setChecked(false);
                    ch_no_of_v_crossing_lt.setChecked(false);
                    layout_no_v_crossing_arms_lt.setVisibility(View.GONE);
                }
            }
        });

        ch_no_of_v_crossing_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_no_of_v_crossing_33kv.setChecked(true);
                    loadVCrossArms33KV();
                    // layout_top_cleat_33kv.setVisibility(View.VISIBLE);
                    layout_no_v_crossing_arms_33kv.setVisibility(View.VISIBLE);
                    ch_top_cleat_33kv.setChecked(true);
                } else {
                    ch_top_cleat_33kv.setChecked(false);
                    ch_no_of_v_crossing_33kv.setChecked(false);
                    layout_no_v_crossing_arms_33kv.setVisibility(View.GONE);
                }
            }
        });

        ch_gi_pipe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_sp_gi_pipes.setVisibility(View.VISIBLE);
                } else {
                    layout_sp_gi_pipes.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_33kv_33.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_33kv_multi_ckt_pole_33.setVisibility(View.VISIBLE);
                    List<String> list_pst = new ArrayList<>();
                    list_pst.add("Select");

                    for (int i = 0; i < list33.size(); i++) {
                        if (list33.get(i).getIS_MULTI_CKT().equalsIgnoreCase("Yes")
                                && list33.get(i).getMULTI_CKT_33().equalsIgnoreCase("Yes")
                        ) {
                            list_pst.add(list33.get(i).getPOLE_NO());
                        }

                    }
                    list_pst.add("" + poleNumber);
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_33kv_multi_ckt_pole_33.setAdapter(adapter_pst);
                    sp_33kv_multi_ckt_pole_33.setSelection(0, true);
                } else {
                    layout_33kv_multi_ckt_pole_33.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_33kv_11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_33kv_multi_ckt_pole_11.setVisibility(View.VISIBLE);
                    List<String> list_pst = new ArrayList<>();
                    list_pst.add("Select");

                    for (int i = 0; i < list33.size(); i++) {
                        if (list33.get(i).getIS_MULTI_CKT().equalsIgnoreCase("Yes")
                                && list33.get(i).getMULTI_CKT_11().equalsIgnoreCase("Yes")
                        ) {
                            list_pst.add(list33.get(i).getPOLE_NO());
                        }

                    }
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_33kv_multi_ckt_pole_11.setAdapter(adapter_pst);
                    sp_33kv_multi_ckt_pole_11.setSelection(0, true);
                } else {
                    layout_33kv_multi_ckt_pole_11.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_11kv_11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_11kv_multi_ckt_pole_11.setVisibility(View.VISIBLE);
                    List<String> list_pst = new ArrayList<>();
                    list_pst.add("Select");

                    for (int i = 0; i < list11.size(); i++) {
                        if (list11.get(i).getIS_MULTI_CKT().equalsIgnoreCase("Yes")
                                && list11.get(i).getMULTI_CKT_11().equalsIgnoreCase("Yes")
                        ) {
                            list_pst.add(list11.get(i).getPOLE_NO());
                        }

                    }
                    list_pst.add("" + poleNumber);
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_11kv_multi_ckt_pole_11.setAdapter(adapter_pst);
                    sp_11kv_multi_ckt_pole_11.setSelection(0, true);
                } else {
                    layout_11kv_multi_ckt_pole_11.setVisibility(View.GONE);
                }
            }
        });


        ch_33kv_9_stuart_poles_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_33kv_9_stuart_poles_33kv.setChecked(true);

                    sp_no_9_poles_33kv.setVisibility(View.VISIBLE);
                    tv_select_9m_pole_no_33kv.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_33_pole_support.setVisibility(View.VISIBLE);
                    loadPoleHeightDataStrut9M33kv("9.1");
                    loadStrutClamps33KV("PSCC", "9.1", true);


                } else {
                    ch_33kv_9_stuart_poles_33kv.setChecked(false);
                    sp_no_9_poles_33kv.setVisibility(View.GONE);
                    tv_select_9m_pole_no_33kv.setVisibility(View.GONE);
                    layout_sp_pole_desc_33_pole_support.setVisibility(View.GONE);
                    loadStrutClamps33KV("PSCC", "9.1", false);


                }
            }
        });

        ch_33kv_9_stuart_poles_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_33kv_9_stuart_poles_lt.setChecked(true);
                    sp_no_9_poles_lt.setVisibility(View.VISIBLE);
                    tv_select_9m_pole_no_lt.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_lt_stuart_9m.setVisibility(View.VISIBLE);
                    loadPoleHeightDataStrut9MLT("9.1");
                    loadStrutClampsLT("PSCC", "9.1", true);


                } else {
                    ch_33kv_9_stuart_poles_lt.setChecked(false);
                    sp_no_9_poles_lt.setVisibility(View.GONE);
                    tv_select_9m_pole_no_lt.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_lt_stuart_9m.setVisibility(View.GONE);
                    loadStrutClampsLT("PSCC", "9.1", false);


                }
            }
        });

        ch_33kv_8_stuart_poles_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_33kv_8_stuart_poles_lt.setChecked(true);
                    sp_no_9_poles_lt.setVisibility(View.VISIBLE);
                    tv_select_9m_pole_no_lt.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_lt_8_sturt.setVisibility(View.VISIBLE);
                    loadPoleHeightDataStrut8MLT("8");
                    loadStrut8mClampsLT("PSCC", "8", true);
                } else {
                    ch_33kv_8_stuart_poles_lt.setChecked(false);
                    sp_no_9_poles_lt.setVisibility(View.GONE);
                    tv_select_9m_pole_no_lt.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_lt_8_sturt.setVisibility(View.GONE);
                    loadStrut8mClampsLT("PSCC", "8", false);


                }
            }
        });

        ch_33kv_8_stuart_poles_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ch_33kv_8_stuart_poles_33kv.setChecked(true);
                    sp_no_8_poles_33kv.setVisibility(View.VISIBLE);
                    tv_select_8m_pole_no_33kv.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_33_8_pole_support.setVisibility(View.VISIBLE);
                    loadPoleHeightDataStrut8M33kv("8");
                    loadStrut8mClamps33KV("PSCC", "8", true);
                } else {
                    ch_33kv_8_stuart_poles_33kv.setChecked(false);
                    sp_no_8_poles_33kv.setVisibility(View.GONE);
                    tv_select_8m_pole_no_33kv.setVisibility(View.GONE);
                    layout_sp_pole_desc_33_8_pole_support.setVisibility(View.GONE);
                    loadStrut8mClamps33KV("PSCC", "8", false);


                }
            }
        });


        ch_33kv_9_stuart_poles_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sp_no_9_poles_11kv.setVisibility(View.VISIBLE);
                    tv_select_9m_pole_no_11kv.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_11kv_stuart_9m.setVisibility(View.VISIBLE);
                    loadPoleHeightDataStrut9M11kv("9.1");
                    loadStrutClamps11KV("PSCC", "9.1", true);
                } else {
                    sp_no_9_poles_11kv.setVisibility(View.GONE);
                    tv_select_9m_pole_no_11kv.setVisibility(View.GONE);
                    layout_sp_pole_desc_11kv_stuart_9m.setVisibility(View.GONE);
                    loadStrutClamps11KV("PSCC", "9.1", false);


                }
            }
        });


        ch_33kv_8_stuart_poles_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sp_no_8_poles_11kv.setVisibility(View.VISIBLE);
                    tv_select_8m_pole_no_11kv.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_11kv_8_sturt.setVisibility(View.VISIBLE);
                    loadPoleHeightDataStrut8M11kv("8");
                    loadStrut8mClamps11KV("PSCC", "8", true);
                } else {
                    sp_no_8_poles_11kv.setVisibility(View.GONE);
                    tv_select_8m_pole_no_11kv.setVisibility(View.GONE);
                    layout_sp_pole_desc_11kv_8_sturt.setVisibility(View.GONE);
                    loadStrut8mClamps11KV("PSCC", "8", false);

                }
            }
        });

        ch_multi_33kv_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_33kv_multi_ckt_pole_lt.setVisibility(View.VISIBLE);
                    List<String> list_pst = new ArrayList<>();
                    list_pst.add("Select");

                    for (int i = 0; i < list33.size(); i++) {
                        if (list33.get(i).getIS_MULTI_CKT().equalsIgnoreCase("Yes")
                                && list33.get(i).getMULTI_CKT_LT().equalsIgnoreCase("Yes")
                        ) {
                            list_pst.add(list33.get(i).getPOLE_NO());
                        }

                    }
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_33kv_multi_ckt_pole_lt.setAdapter(adapter_pst);
                    sp_33kv_multi_ckt_pole_lt.setSelection(0, true);
                } else {
                    layout_33kv_multi_ckt_pole_lt.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_11kv_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_11kv_multi_ckt_pole_lt.setVisibility(View.VISIBLE);
                    List<String> list_pst = new ArrayList<>();
                    list_pst.add("Select");

                    for (int i = 0; i < list11.size(); i++) {
                        if (list11.get(i).getIS_MULTI_CKT().equalsIgnoreCase("Yes")
                                && list11.get(i).getMULTI_CKT_LT().equalsIgnoreCase("Yes")
                        ) {
                            list_pst.add(list11.get(i).getPOLE_NO());
                        }

                    }
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_11kv_multi_ckt_pole_lt.setAdapter(adapter_pst);
                    sp_11kv_multi_ckt_pole_lt.setSelection(0, true);
                } else {
                    layout_11kv_multi_ckt_pole_lt.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_lt_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_lt_multi_ckt_pole_lt.setVisibility(View.VISIBLE);
                    List<String> list_pst = new ArrayList<>();
                    list_pst.add("Select");
                    for (int i = 0; i < listLT.size(); i++) {
                        if (listLT.get(i).getIS_MULTI_CKT().equalsIgnoreCase("Yes")
                                && listLT.get(i).getMULTI_CKT_LT().equalsIgnoreCase("Yes")
                        ) {
                            list_pst.add(listLT.get(i).getPOLE_NO());
                        }

                    }
                    list_pst.add("" + poleNumber);
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_lt_multi_ckt_pole_lt.setAdapter(adapter_pst);
                    sp_lt_multi_ckt_pole_lt.setSelection(0, true);
                } else {
                    layout_lt_multi_ckt_pole_lt.setVisibility(View.GONE);
                }
            }
        });


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(context, "Please Select Voltage Level", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_is_location_exists.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(context, "Please Select Pole Survey Type", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (sp_is_location_exists.getSelectedItem().toString().equalsIgnoreCase("Existing")
//                        && !ch_ext_loc_33kv.isChecked() && !ch_ext_loc_11kv.isChecked() && !ch_ext_loc_lt.isChecked()
//
//                ) {
//                    Toast.makeText(context, "Please choose at least one voltage level", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    validate33KVSurvey();
                }

                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    validate11KVSurvey();
                }
                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("6.3KV")) {
                    validate63KVSurvey();
                }

                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    validateLTSurvey();
                }
            }
        });


    }

    private void setPoleConfigurationSpinner33kv() {
        ArrayList<NewModel> polesDataAll = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false,GIS_ID);
        if (polesDataAll != null && !polesDataAll.isEmpty()) {
            poleConfiguration33kv.clear();
            poleConfiguration33kv.add("Select");
            poleConfiguration33kv.add("Line-Pole");
            poleConfiguration33kv.add("33/11kv SS-Bay");
            poleConfiguration33kv.add("HT Service-Pole");
            poleConfiguration33kv.add("AB-Pole");
            poleConfiguration33kv.add("PTR");
        } else {
            poleConfiguration33kv.clear();
            poleConfiguration33kv.add("Select");
            poleConfiguration33kv.add("EHT SS-Bay");
            poleConfiguration33kv.add("Line-Pole");
            poleConfiguration33kv.add("33/11kv SS-Bay");
            poleConfiguration33kv.add("HT Service-Pole");
            poleConfiguration33kv.add("AB-Pole");
            poleConfiguration33kv.add("PTR");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, poleConfiguration33kv);
        sp_pole_configuration_33.setAdapter(adapter);
        setPoleTypeAdapter();


    }

    private void setPoleConfigurationSpinner11kv() {
        ArrayList<NewModel> polesDataAll = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false,GIS_ID);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(polesDataAll);
        Log.d("FINDING_11KV_poles==", "onCreate: " + json);
        if (polesDataAll != null && !polesDataAll.isEmpty()) {
            poleConfiguration11kv.clear();
            poleConfiguration11kv.add("Select");
            poleConfiguration11kv.add("Line-Pole");
            poleConfiguration11kv.add("DTR Pole");
            poleConfiguration11kv.add("HT Service-Pole");
            poleConfiguration11kv.add("AB-Pole");
        } else {
            poleConfiguration11kv.clear();
            poleConfiguration11kv.add("Select");
            poleConfiguration11kv.add("Line-Pole");
            poleConfiguration11kv.add("DTR Pole");
            poleConfiguration11kv.add("SS BAY");
            poleConfiguration11kv.add("HT Service-Pole");
            poleConfiguration11kv.add("AB-Pole");
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, poleConfiguration11kv);
        sp_pole_configuration_11.setAdapter(adapter);
        setPoleTypeAdapter();


    }


    private void repeatPole33KV() {
        statusList = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false,GIS_ID);

        if (statusList != null && !statusList.isEmpty()) {
            NewModel lastModel = statusList.get(statusList.size() - 1);

            String conductorDesc = lastModel.getCONDUCTOR_DESC();
            String conductor_size = lastModel.getCONDUTCOR_SIZE();
            String LINE_SURVEY_TYPE = lastModel.getLINE_SURVEY_TYPE();
            String LINE_TYPE = lastModel.getLINE_TYPE();
            String POLE_CONFIG = lastModel.getPOLE_CONFIG();
            String POLE_DESC = lastModel.getPOLE_DESC();
            String POLE_HEIGHT = lastModel.getPOLE_HEIGHT();
            String POLE_LOCATION_TYPE = lastModel.getPOLE_LOCATION_TYPE();
            String POLE_SUPPORT = lastModel.getPOLE_SUPPORT();
            String POLE_TYPE = lastModel.getPOLE_TYPE();
            String IS_EXISTING_LOCATION_NEW = lastModel.getIS_EXISTING_LOCATION_NEW();
            int NUMB_9M_STRUTS = lastModel.getNUMB_9M_STRUTS();
            int NUMB_8M_STRUTS = lastModel.getNUMB_8M_STRUTS();
            int NUMB_STAYS = lastModel.getNUMB_STAYS();

            String EXISTING_LOCATION_33_NEW = lastModel.getEXISTING_LOCATION_33_NEW();
            String EXISTING_LOCATION_11_NEW = lastModel.getEXISTING_LOCATION_11_NEW();
            String EXISTING_LOCATION_LT_NEW = lastModel.getEXISTING_LOCATION_LT_NEW();

            if (IS_EXISTING_LOCATION_NEW != null) {
                if (IS_EXISTING_LOCATION_NEW.equalsIgnoreCase("Proposed")) {
                    sp_is_location_exists.setSelection(1, true);
                } else {
                    if (EXISTING_LOCATION_33_NEW.equalsIgnoreCase("Yes")) {
                        ch_ext_loc_33kv.setChecked(true);
                    }
                    if (EXISTING_LOCATION_11_NEW.equalsIgnoreCase("Yes")) {
                        ch_ext_loc_11kv.setChecked(true);
                    }
                    if (EXISTING_LOCATION_LT_NEW.equalsIgnoreCase("Yes")) {
                        ch_ext_loc_lt.setChecked(true);
                    }
                    sp_is_location_exists.setSelection(2, true);

                }
            }


            if (LINE_SURVEY_TYPE != null) {
                if (LINE_SURVEY_TYPE.equalsIgnoreCase("Proposed")) {
                    sp_line_survey_type.setSelection(1, true);
                } else {
                    sp_line_survey_type.setSelection(2, true);

                }
            }
            if (POLE_LOCATION_TYPE != null) {
                if (POLE_LOCATION_TYPE.equalsIgnoreCase("New Location")) {
                    sp_survey_pole_type.setSelection(0, true);
                }
            }
            if (LINE_TYPE != null) {
                if (LINE_TYPE.equalsIgnoreCase("OverHead")) {
                    sp_lineType_33.setSelection(0, true);
                } else {
                    sp_lineType_33.setSelection(1, true);
                }
            }
            if (POLE_CONFIG != null) {
                if (POLE_CONFIG.equalsIgnoreCase("Line-Pole")) {
                    sp_pole_configuration_33.setSelection(1, true);
                } else if (POLE_CONFIG.equalsIgnoreCase("33/11kv SS-Bay")) {
                    sp_pole_configuration_33.setSelection(2, true);
                } else if (POLE_CONFIG.equalsIgnoreCase("EHS SS-Bay")) {
                    sp_pole_configuration_33.setSelection(3, true);
                } else if (POLE_CONFIG.equalsIgnoreCase("HT Service-Pole")) {
                    sp_pole_configuration_33.setSelection(4, true);
                } else if (POLE_CONFIG.equalsIgnoreCase("AB-Pole")) {
                    sp_pole_configuration_33.setSelection(5, true);
                } else if (POLE_CONFIG.equalsIgnoreCase("PTR")) {
                    sp_pole_configuration_33.setSelection(6, true);
                }
            }

            if (POLE_TYPE != null) {
                List<String> ugpsccList = new ArrayList<>();
                ugpsccList.add("Select");
                ugpsccList.add("PSCC");
                ugpsccList.add("SPUN");
                ugpsccList.add("TOWER");
                ugpsccList.add("SS BAY");
                ugpsccList.add("RAIL");
                ugpsccList.add("RS JOIST");
                ugpsccList.add("TUBULAR POLE");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                sp_pole_type_33.setAdapter(adapter);

                sp_pole_type_33.post(() -> {
                    int poleTypePosition = ugpsccList.indexOf(POLE_TYPE);
                    if (poleTypePosition >= 0) {
                        sp_pole_type_33.setSelection(poleTypePosition, true);
                    }
                });

                if (POLE_HEIGHT != null) {
                    String POLE_HEIGHT1 = lastModel.getPOLE_HEIGHT().trim();
                    if (POLE_TYPE.equalsIgnoreCase("PSCC")) {
                        List<String> ugpsccHeightList = new ArrayList<>();
                        ugpsccHeightList.add("Select");
                        ugpsccHeightList.add("9.1");
                        ugpsccHeightList.add("11");
                        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ugpsccHeightList);
                        sp_pole_height_33.setAdapter(heightAdapter);
                        Log.d("HeightAdapter", "Adapter set with heights: " + ugpsccHeightList);


                        sp_pole_height_33.post(() -> {
                            Log.d("POLE_HEIGHT", "POLE_HEIGHT value: " + POLE_HEIGHT1);
                            int poleHeightPosition = ugpsccHeightList.indexOf(POLE_HEIGHT1);
                            Log.d("PoleHeightIndex", "Index found: " + poleHeightPosition);

                            if (poleHeightPosition >= 0) {
                                sp_pole_height_33.setSelection(poleHeightPosition, true);
                            }
                        });
                    }
                }


            }


            if (POLE_SUPPORT != null) {
                if (POLE_SUPPORT.equalsIgnoreCase("None")) {
                    sp_pole_support_33.setSelection(1, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT")) {
                    sp_pole_support_33.setSelection(2, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STAY")) {
                    sp_pole_support_33.setSelection(3, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT and STAY")) {
                    sp_pole_support_33.setSelection(4, true);
                }
            }

            if (POLE_SUPPORT != null) {
                if (NUMB_9M_STRUTS > 0) {
                    ch_33kv_9_stuart_poles_33kv.setChecked(true);
                }
                if (NUMB_8M_STRUTS > 0) {
                    ch_33kv_8_stuart_poles_33kv.setChecked(true);
                }

                String strut9M = String.valueOf(NUMB_9M_STRUTS);
                String strut8M = String.valueOf(NUMB_8M_STRUTS);

                if (POLE_SUPPORT.equalsIgnoreCase("STRUT")) {
                    if (strut9M.equalsIgnoreCase("1")) {
                        sp_no_9_poles_33kv.setSelection(0, true);
                    } else if (strut9M.equalsIgnoreCase("2")) {
                        sp_no_9_poles_33kv.setSelection(1, true);
                    } else if (strut9M.equalsIgnoreCase("3")) {
                        sp_no_9_poles_33kv.setSelection(2, true);
                    } else if (strut9M.equalsIgnoreCase("4")) {
                        sp_no_9_poles_33kv.setSelection(3, true);
                    }
                    if (strut8M.equalsIgnoreCase("1")) {
                        sp_no_8_poles_33kv.setSelection(0, true);
                    } else if (strut8M.equalsIgnoreCase("2")) {
                        sp_no_8_poles_33kv.setSelection(1, true);
                    } else if (strut8M.equalsIgnoreCase("3")) {
                        sp_no_8_poles_33kv.setSelection(2, true);
                    } else if (strut8M.equalsIgnoreCase("4")) {
                        sp_no_8_poles_33kv.setSelection(3, true);
                    }

                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT and STAY")) {
                    if (strut9M.equalsIgnoreCase("1")) {
                        sp_no_9_poles_33kv.setSelection(0, true);
                    } else if (strut9M.equalsIgnoreCase("2")) {
                        sp_no_9_poles_33kv.setSelection(1, true);
                    } else if (strut9M.equalsIgnoreCase("3")) {
                        sp_no_9_poles_33kv.setSelection(2, true);
                    } else if (strut9M.equalsIgnoreCase("4")) {
                        sp_no_9_poles_33kv.setSelection(3, true);
                    }
                    if (strut8M.equalsIgnoreCase("1")) {
                        sp_no_8_poles_33kv.setSelection(0, true);
                    } else if (strut8M.equalsIgnoreCase("2")) {
                        sp_no_8_poles_33kv.setSelection(1, true);
                    } else if (strut8M.equalsIgnoreCase("3")) {
                        sp_no_8_poles_33kv.setSelection(2, true);
                    } else if (strut8M.equalsIgnoreCase("4")) {
                        sp_no_8_poles_33kv.setSelection(3, true);
                    }
                    String numOfStays = String.valueOf(NUMB_STAYS);
                    if (numOfStays.equalsIgnoreCase("1")) {
                        sp_no_of_stays_33kv.setSelection(0, true);
                    } else if (numOfStays.equalsIgnoreCase("2")) {
                        sp_no_of_stays_33kv.setSelection(1, true);
                    } else if (numOfStays.equalsIgnoreCase("3")) {
                        sp_no_of_stays_33kv.setSelection(2, true);
                    } else if (numOfStays.equalsIgnoreCase("4")) {
                        sp_no_of_stays_33kv.setSelection(3, true);
                    } else if (numOfStays.equalsIgnoreCase("5")) {
                        sp_no_of_stays_33kv.setSelection(4, true);
                    } else if (numOfStays.equalsIgnoreCase("6")) {
                        sp_no_of_stays_33kv.setSelection(5, true);
                    }

                } else if (POLE_SUPPORT.equalsIgnoreCase("STAY")) {
                    String numOfStays = String.valueOf(NUMB_STAYS);
                    if (numOfStays.equalsIgnoreCase("1")) {
                        sp_no_of_stays_33kv.setSelection(0, true);
                    } else if (numOfStays.equalsIgnoreCase("2")) {
                        sp_no_of_stays_33kv.setSelection(1, true);
                    } else if (numOfStays.equalsIgnoreCase("3")) {
                        sp_no_of_stays_33kv.setSelection(2, true);
                    } else if (numOfStays.equalsIgnoreCase("4")) {
                        sp_no_of_stays_33kv.setSelection(3, true);
                    } else if (numOfStays.equalsIgnoreCase("5")) {
                        sp_no_of_stays_33kv.setSelection(4, true);
                    } else if (numOfStays.equalsIgnoreCase("6")) {
                        sp_no_of_stays_33kv.setSelection(5, true);
                    }

                }
            }


            Log.d("NUMB_9M_STRUTS---->", "repeatPole33KV: " + NUMB_9M_STRUTS);

            if (conductor_size != null) {
                if (conductor_size.equalsIgnoreCase("AAA CONDUCTOR")) {
                    sp_conductor_33.setSelection(1, true);
                } else if (conductor_size.equalsIgnoreCase("AB CABLE")) {
                    sp_conductor_33.setSelection(2, true);
                } else if (conductor_size.equalsIgnoreCase("ACSR CONDUCTOR")) {
                    sp_conductor_33.setSelection(3, true);
                }

//                else if (conductor_size.equalsIgnoreCase("XLPE CABLE")) {
//                    sp_conductor_33.setSelection(3, true);
//                } else if (conductor_size.equalsIgnoreCase("PVC CABLE")) {
//                    sp_conductor_33.setSelection(4, true);
//                } else if (conductor_size.equalsIgnoreCase("ACSR CONDUCTOR")) {
//                    sp_conductor_33.setSelection(5, true);
//                }
            }


        }
    }


    private void repeatPole11KV() {
        statusList = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false,GIS_ID);

        if (statusList != null && !statusList.isEmpty()) {
            NewModel lastModel = statusList.get(statusList.size() - 1);

            String conductorDesc = lastModel.getCONDUCTOR_DESC();
            String conductor_size = lastModel.getCONDUTCOR_SIZE();
            String LINE_SURVEY_TYPE = lastModel.getLINE_SURVEY_TYPE();
            String LINE_TYPE = lastModel.getLINE_TYPE();
            String POLE_CONFIG = lastModel.getPOLE_CONFIG();
            String POLE_DESC = lastModel.getPOLE_DESC();
            String POLE_HEIGHT = lastModel.getPOLE_HEIGHT();
            String POLE_LOCATION_TYPE = lastModel.getPOLE_LOCATION_TYPE();
            String POLE_SUPPORT = lastModel.getPOLE_SUPPORT();
            String POLE_TYPE = lastModel.getPOLE_TYPE();
            String IS_EXISTING_LOCATION_NEW = lastModel.getIS_EXISTING_LOCATION_NEW();
            int NUMB_9M_STRUTS = lastModel.getNUMB_9M_STRUTS();
            int NUMB_8M_STRUTS = lastModel.getNUMB_8M_STRUTS();
            int NUMB_STAYS = lastModel.getNUMB_STAYS();
            String DTR_SURVEY_TYPE = lastModel.getDTR_SURVEY_TYPE();
            String DTR_PHASE = lastModel.getDTR_PHASE();
            String DTR_CAPACITY = lastModel.getDTR_CAPACITY();
            String DTR_MOUNTING = lastModel.getDTR_MOUNTING();

            String EXISTING_LOCATION_33_NEW = lastModel.getEXISTING_LOCATION_33_NEW();
            String EXISTING_LOCATION_11_NEW = lastModel.getEXISTING_LOCATION_11_NEW();
            String EXISTING_LOCATION_LT_NEW = lastModel.getEXISTING_LOCATION_LT_NEW();

            Log.d("repeatPole11KV---->", "repeatPole11KV: " + POLE_HEIGHT + " ");

            if (IS_EXISTING_LOCATION_NEW.equalsIgnoreCase("Proposed")) {
                sp_is_location_exists.setSelection(1, true);
            } else {
                if (EXISTING_LOCATION_33_NEW.equalsIgnoreCase("Yes")) {
                    ch_ext_loc_33kv.setChecked(true);
                }
                if (EXISTING_LOCATION_11_NEW.equalsIgnoreCase("Yes")) {
                    ch_ext_loc_11kv.setChecked(true);
                }
                if (EXISTING_LOCATION_LT_NEW.equalsIgnoreCase("Yes")) {
                    ch_ext_loc_lt.setChecked(true);
                }
                sp_is_location_exists.setSelection(2, true);

            }

            if (LINE_SURVEY_TYPE != null) {
                if (LINE_SURVEY_TYPE.equalsIgnoreCase("Proposed")) {
                    sp_line_survey_type.setSelection(1, true);
                } else {
                    sp_line_survey_type.setSelection(2, true);

                }
            }

            if (POLE_LOCATION_TYPE != null) {
                if (POLE_LOCATION_TYPE.equalsIgnoreCase("New Location")) {
                    sp_survey_pole_type.setSelection(0, true);
                } else {
                    sp_survey_pole_type.setSelection(1, true);

                }
            }

            if (LINE_TYPE != null) {
                if (LINE_TYPE.equalsIgnoreCase("OverHead")) {
                    sp_lineType_11.setSelection(0, true);
                } else {
                    sp_lineType_11.setSelection(1, true);
                }
            }


            if (POLE_CONFIG != null) {
                if (POLE_CONFIG.equalsIgnoreCase("SS BAY")) {
                    sp_pole_configuration_11.setSelection(1, true);
                } else if (POLE_CONFIG.equalsIgnoreCase("Line-Pole")) {
                    sp_pole_configuration_11.setSelection(2, true);
                } else if (POLE_CONFIG.equalsIgnoreCase("DTR Pole")) {
                    sp_pole_configuration_11.setSelection(3, true);
                    if (DTR_MOUNTING.equalsIgnoreCase("Pole Mounted")) {
                        sp_dtr_mounting_11.setSelection(1, true);
                    } else if (DTR_MOUNTING.equalsIgnoreCase("Plinth Mounted")) {
                        sp_dtr_mounting_11.setSelection(2, true);
                    } else if (DTR_MOUNTING.equalsIgnoreCase("Pillar Mounted")) {
                        sp_dtr_mounting_11.setSelection(3, true);
                    } else if (DTR_MOUNTING.equalsIgnoreCase("DP Structure")) {
                        sp_dtr_mounting_11.setSelection(4, true);
                    }

                } else if (POLE_CONFIG.equalsIgnoreCase("HT Service-Pole")) {
                    sp_pole_configuration_11.setSelection(4, true);
                } else if (POLE_CONFIG.equalsIgnoreCase("AB-Pole")) {
                    sp_pole_configuration_11.setSelection(5, true);
                }
            }

            if (conductor_size != null) {
                if (conductor_size.equalsIgnoreCase("AAA CONDUCTOR")) {
                    sp_conductor_11.setSelection(1, true);
                }
                if (conductor_size.equalsIgnoreCase("AB CABLE")) {
                    sp_conductor_11.setSelection(2, true);
                }
                if (conductor_size.equalsIgnoreCase("ACSR CONDUCTOR")) {
                    sp_conductor_11.setSelection(3, true);
                }
//                if (conductor_size.equalsIgnoreCase("XLPE CABLE")) {
//                    sp_conductor_11.setSelection(3, true);
//                }
//                if (conductor_size.equalsIgnoreCase("PVC CABLE")) {
//                    sp_conductor_11.setSelection(4, true);
//                }

            }


            if (POLE_TYPE != null) {
                List<String> ugpsccList = new ArrayList<>();
                ugpsccList.add("Select");
                ugpsccList.add("PSCC");
                ugpsccList.add("SPUN");
                ugpsccList.add("TOWER");
                ugpsccList.add("SS BAY");
                ugpsccList.add("RAIL");
                ugpsccList.add("RS JOIST");
                ugpsccList.add("TUBULAR POLE");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                sp_pole_type_11.setAdapter(adapter);

                sp_pole_type_11.post(() -> {
                    int poleTypePosition = ugpsccList.indexOf(POLE_TYPE);
                    if (poleTypePosition >= 0) {
                        sp_pole_type_11.setSelection(poleTypePosition, true);
                    }
                });

//                if (POLE_HEIGHT != null) {
//                    String POLE_HEIGHT1 = lastModel.getPOLE_HEIGHT().trim();
//                    if (POLE_TYPE.equalsIgnoreCase("PSCC")) {
//                        List<String> ugpsccHeightList = new ArrayList<>();
//                        ugpsccHeightList.add("Select");
//                        ugpsccHeightList.add("9.1");
//                        ugpsccHeightList.add("11");
//                        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ugpsccHeightList);
//                        sp_pole_height_33.setAdapter(heightAdapter);
//                        Log.d("HeightAdapter", "Adapter set with heights: " + ugpsccHeightList);
//
//
//                        sp_pole_height_33.post(() -> {
//                            Log.d("POLE_HEIGHT", "POLE_HEIGHT value: " + POLE_HEIGHT1);
//                            int poleHeightPosition = ugpsccHeightList.indexOf(POLE_HEIGHT1);
//                            Log.d("PoleHeightIndex", "Index found: " + poleHeightPosition);
//
//                            if (poleHeightPosition >= 0) {
//                                sp_pole_height_33.setSelection(poleHeightPosition, true);
//                            }
//                        });
//                    }
//                }


            }

            if (POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                if (Objects.requireNonNull(POLE_TYPE).equalsIgnoreCase("SPUN")) {
                    sp_pole_height_11.setSelection(3, true);
                }
            }

            if (POLE_SUPPORT != null) {
                if (POLE_SUPPORT.equalsIgnoreCase("None")) {
                    sp_pole_support_11.setSelection(1, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT")) {
                    sp_pole_support_11.setSelection(2, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STAY")) {
                    sp_pole_support_11.setSelection(3, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT and STAY")) {
                    sp_pole_support_11.setSelection(4, true);
                }
            }


            if (POLE_CONFIG != null) {
                if (NUMB_9M_STRUTS > 0) {
                    ch_33kv_9_stuart_poles_11kv.setChecked(true);
                }
                if (NUMB_8M_STRUTS > 0) {
                    ch_33kv_8_stuart_poles_11kv.setChecked(true);
                }

                String strut9M = String.valueOf(NUMB_9M_STRUTS);
                String strut8M = String.valueOf(NUMB_8M_STRUTS);

                if (POLE_SUPPORT.equalsIgnoreCase("STRUT")) {
                    if (strut9M.equalsIgnoreCase("1")) {
                        sp_no_9_poles_11kv.setSelection(0, true);
                    } else if (strut9M.equalsIgnoreCase("2")) {
                        sp_no_9_poles_11kv.setSelection(1, true);
                    } else if (strut9M.equalsIgnoreCase("3")) {
                        sp_no_9_poles_11kv.setSelection(2, true);
                    } else if (strut9M.equalsIgnoreCase("4")) {
                        sp_no_9_poles_11kv.setSelection(3, true);
                    }
                    if (strut8M.equalsIgnoreCase("1")) {
                        sp_no_8_poles_11kv.setSelection(0, true);
                    } else if (strut8M.equalsIgnoreCase("2")) {
                        sp_no_8_poles_11kv.setSelection(1, true);
                    } else if (strut8M.equalsIgnoreCase("3")) {
                        sp_no_8_poles_11kv.setSelection(2, true);
                    } else if (strut8M.equalsIgnoreCase("4")) {
                        sp_no_8_poles_11kv.setSelection(3, true);
                    }

                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT and STAY")) {
                    if (strut9M.equalsIgnoreCase("1")) {
                        sp_no_9_poles_11kv.setSelection(0, true);
                    } else if (strut9M.equalsIgnoreCase("2")) {
                        sp_no_9_poles_11kv.setSelection(1, true);
                    } else if (strut9M.equalsIgnoreCase("3")) {
                        sp_no_9_poles_11kv.setSelection(2, true);
                    } else if (strut9M.equalsIgnoreCase("4")) {
                        sp_no_9_poles_11kv.setSelection(3, true);
                    }
                    if (strut8M.equalsIgnoreCase("1")) {
                        sp_no_8_poles_11kv.setSelection(0, true);
                    } else if (strut8M.equalsIgnoreCase("2")) {
                        sp_no_8_poles_11kv.setSelection(1, true);
                    } else if (strut8M.equalsIgnoreCase("3")) {
                        sp_no_8_poles_11kv.setSelection(2, true);
                    } else if (strut8M.equalsIgnoreCase("4")) {
                        sp_no_8_poles_11kv.setSelection(3, true);
                    }
                    String numOfStays = String.valueOf(NUMB_STAYS);
                    if (numOfStays.equalsIgnoreCase("1")) {
                        sp_no_stays_11kv.setSelection(0, true);
                    } else if (numOfStays.equalsIgnoreCase("2")) {
                        sp_no_stays_11kv.setSelection(1, true);
                    } else if (numOfStays.equalsIgnoreCase("3")) {
                        sp_no_stays_11kv.setSelection(2, true);
                    } else if (numOfStays.equalsIgnoreCase("4")) {
                        sp_no_stays_11kv.setSelection(3, true);
                    } else if (numOfStays.equalsIgnoreCase("5")) {
                        sp_no_stays_11kv.setSelection(4, true);
                    } else if (numOfStays.equalsIgnoreCase("6")) {
                        sp_no_stays_11kv.setSelection(5, true);
                    }

                } else if (POLE_SUPPORT.equalsIgnoreCase("STAY")) {
                    String numOfStays = String.valueOf(NUMB_STAYS);
                    if (numOfStays.equalsIgnoreCase("1")) {
                        sp_no_stays_11kv.setSelection(0, true);
                    } else if (numOfStays.equalsIgnoreCase("2")) {
                        sp_no_stays_11kv.setSelection(1, true);
                    } else if (numOfStays.equalsIgnoreCase("3")) {
                        sp_no_stays_11kv.setSelection(2, true);
                    } else if (numOfStays.equalsIgnoreCase("4")) {
                        sp_no_stays_11kv.setSelection(3, true);
                    } else if (numOfStays.equalsIgnoreCase("5")) {
                        sp_no_stays_11kv.setSelection(4, true);
                    } else if (numOfStays.equalsIgnoreCase("6")) {
                        sp_no_stays_11kv.setSelection(5, true);
                    }

                }
            }


            if (DTR_SURVEY_TYPE == null) {
                return;
            }

            if (DTR_SURVEY_TYPE.equalsIgnoreCase("Proposed")) {
                sp_dtr_survey_11.setSelection(1, true);

                if (DTR_PHASE.equalsIgnoreCase("I")) {
                    List<String> ugpsccList = new ArrayList<>();
                    ugpsccList.add("Select");
                    ugpsccList.add("I");
                    ugpsccList.add("III");

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_dtr_phase_11.setAdapter(adapter);

                    sp_dtr_phase_11.post(() -> {
                        int poleTypePosition = ugpsccList.indexOf(DTR_PHASE);
                        if (poleTypePosition >= 0) {
                            sp_dtr_phase_11.setSelection(poleTypePosition, true);
                        }
                    });


                    if (DTR_CAPACITY.equalsIgnoreCase("16 kVA")) {
                        sp_dtr_capacity_phase1_11.setSelection(1, true);
                    }
                } else {
                    List<String> ugpsccList = new ArrayList<>();
                    ugpsccList.add("Select");
                    ugpsccList.add("I");
                    ugpsccList.add("III");

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_dtr_phase_11.setAdapter(adapter);

                    sp_dtr_phase_11.post(() -> {
                        int poleTypePosition = ugpsccList.indexOf(DTR_PHASE);
                        if (poleTypePosition >= 0) {
                            sp_dtr_phase_11.setSelection(poleTypePosition, true);
                        }
                    });
                    if (DTR_CAPACITY.equalsIgnoreCase("25 kVA")) {
                        sp_dtr_capacity_phase2_11.setSelection(1, true);
                    } else if (DTR_CAPACITY.equalsIgnoreCase("40 kVA")) {
                        sp_dtr_capacity_phase2_11.setSelection(2, true);
                    } else if (DTR_CAPACITY.equalsIgnoreCase("63 kVA")) {
                        sp_dtr_capacity_phase2_11.setSelection(3, true);
                    } else if (DTR_CAPACITY.equalsIgnoreCase("100 kVA")) {
                        sp_dtr_capacity_phase2_11.setSelection(4, true);
                    } else if (DTR_CAPACITY.equalsIgnoreCase("165 kVA")) {
                        sp_dtr_capacity_phase2_11.setSelection(5, true);
                    } else if (DTR_CAPACITY.equalsIgnoreCase("315 kVA")) {
                        sp_dtr_capacity_phase2_11.setSelection(6, true);
                    } else if (DTR_CAPACITY.equalsIgnoreCase("650 kVA")) {
                        sp_dtr_capacity_phase2_11.setSelection(7, true);
                    }
                    sp_dtr_phase_11.setSelection(2, true);
                }

            } else {
                sp_dtr_survey_11.setSelection(2, true);
            }

            if (DTR_SURVEY_TYPE.equalsIgnoreCase("Proposed")) {
                sp_dtr_survey_11.setSelection(1, true);
                if (DTR_PHASE.equalsIgnoreCase("I")) {
                    sp_dtr_phase_11.setSelection(1, true);
                    if (DTR_CAPACITY.equalsIgnoreCase("16 kVA")) {
                        sp_dtr_capacity_phase1_11.setSelection(1, true);
                    }
                } else {

                    sp_dtr_phase_11.setSelection(2, true);
                }
            } else {
                sp_dtr_survey_11.setSelection(2, true);
            }


            Log.d("NUMB_9M_STRUTS---->", "repeatPole33KV: " + NUMB_9M_STRUTS);


        }
    }

    private void repeatPoleLT() {
        statusList = databaseEstimates.getNewSurveyData(EST_NO, "LT", false,GIS_ID);

        if (statusList != null && !statusList.isEmpty()) {
            NewModel lastModel = statusList.get(statusList.size() - 1);

            String conductorDesc = lastModel.getCONDUCTOR_DESC();
            String conductor_size = lastModel.getCONDUTCOR_SIZE();
            String LINE_SURVEY_TYPE = lastModel.getLINE_SURVEY_TYPE();
            String LINE_TYPE = lastModel.getLINE_TYPE();
            String POLE_CONFIG = lastModel.getPOLE_CONFIG();
            String POLE_DESC = lastModel.getPOLE_DESC();
            String POLE_HEIGHT = lastModel.getPOLE_HEIGHT();
            String POLE_LOCATION_TYPE = lastModel.getPOLE_LOCATION_TYPE();
            String POLE_SUPPORT = lastModel.getPOLE_SUPPORT();
            String POLE_TYPE = lastModel.getPOLE_TYPE();
            String IS_EXISTING_LOCATION_NEW = lastModel.getIS_EXISTING_LOCATION_NEW();
            int NUMB_9M_STRUTS = lastModel.getNUMB_9M_STRUTS();
            int NUMB_8M_STRUTS = lastModel.getNUMB_8M_STRUTS();
            int NUMB_STAYS = lastModel.getNUMB_STAYS();
            String DTR_SURVEY_TYPE = lastModel.getDTR_SURVEY_TYPE();
            String DTR_PHASE = lastModel.getDTR_PHASE();
            String DTR_CAPACITY = lastModel.getDTR_CAPACITY();
            String DTR_MOUNTING = lastModel.getDTR_MOUNTING();

            String EXISTING_LOCATION_33_NEW = lastModel.getEXISTING_LOCATION_33_NEW();
            String EXISTING_LOCATION_11_NEW = lastModel.getEXISTING_LOCATION_11_NEW();
            String EXISTING_LOCATION_LT_NEW = lastModel.getEXISTING_LOCATION_LT_NEW();
            if (IS_EXISTING_LOCATION_NEW.equalsIgnoreCase("Proposed")) {
                sp_is_location_exists.setSelection(1, true);
            } else {
                if (EXISTING_LOCATION_33_NEW.equalsIgnoreCase("Yes")) {
                    ch_ext_loc_33kv.setChecked(true);
                }
                if (EXISTING_LOCATION_11_NEW.equalsIgnoreCase("Yes")) {
                    ch_ext_loc_11kv.setChecked(true);
                }
                if (EXISTING_LOCATION_LT_NEW.equalsIgnoreCase("Yes")) {
                    ch_ext_loc_lt.setChecked(true);
                }
                sp_is_location_exists.setSelection(2, true);

            }

            if (LINE_SURVEY_TYPE.equalsIgnoreCase("Proposed")) {
                sp_line_survey_type.setSelection(1, true);
            } else {
                sp_line_survey_type.setSelection(2, true);

            }
            if (POLE_LOCATION_TYPE != null) {
                if (POLE_LOCATION_TYPE.equalsIgnoreCase("New Location")) {
                    sp_survey_pole_type.setSelection(0, true);
                } else {
                    sp_survey_pole_type.setSelection(1, true);
                }
            }
            if (LINE_TYPE != null) {
                if (LINE_TYPE.equalsIgnoreCase("OverHead")) {
                    sp_lineType_lt.setSelection(0, true);
                } else {
                    sp_lineType_lt.setSelection(1, true);
                }
            }


            if (POLE_CONFIG.equalsIgnoreCase("3-Ph, 5- Wire")) {
                sp_pole_configuration_lt.setSelection(0, true);
            } else if (POLE_CONFIG.equalsIgnoreCase("3-Ph, 4- Wire")) {
                sp_pole_configuration_lt.setSelection(1, true);
            } else if (POLE_CONFIG.equalsIgnoreCase("1-Ph, 3- Wire")) {
                sp_pole_configuration_lt.setSelection(2, true);
            } else if (POLE_CONFIG.equalsIgnoreCase("1-Ph, 2- Wire")) {
                sp_pole_configuration_lt.setSelection(3, true);
            }

            if (POLE_TYPE != null) {
                List<String> ugpsccList = new ArrayList<>();
                ugpsccList.add("Select");
                ugpsccList.add("PSCC");
                ugpsccList.add("SPUN");
                ugpsccList.add("TOWER");
                ugpsccList.add("SS BAY");
                ugpsccList.add("RAIL");
                ugpsccList.add("RS JOIST");
                ugpsccList.add("TUBULAR POLE");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                sp_pole_type_lt.setAdapter(adapter);

                sp_pole_type_lt.post(() -> {
                    int poleTypePosition = ugpsccList.indexOf(POLE_TYPE);
                    if (poleTypePosition >= 0) {
                        sp_pole_type_lt.setSelection(poleTypePosition, true);
                    }
                });

                if (POLE_HEIGHT != null) {
                    String POLE_HEIGHT1 = lastModel.getPOLE_HEIGHT().trim();
                    if (POLE_TYPE.equalsIgnoreCase("PSCC")) {
                        List<String> ugpsccHeightList = new ArrayList<>();
                        ugpsccHeightList.add("Select");
                        ugpsccHeightList.add("9.1");
                        ugpsccHeightList.add("11");
                        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ugpsccHeightList);
                        sp_pole_height_lt.setAdapter(heightAdapter);
                        Log.d("HeightAdapter", "Adapter set with heights: " + ugpsccHeightList);


                        sp_pole_height_lt.post(() -> {
                            Log.d("POLE_HEIGHT", "POLE_HEIGHT value: " + POLE_HEIGHT1);
                            int poleHeightPosition = ugpsccHeightList.indexOf(POLE_HEIGHT1);
                            Log.d("PoleHeightIndex", "Index found: " + poleHeightPosition);

                            if (poleHeightPosition >= 0) {
                                sp_pole_height_lt.setSelection(poleHeightPosition, true);
                            }
                        });
                    }
                }


            }
            Log.d("LT_DATA", "repeatPoleLT: " + POLE_CONFIG + " " + LINE_TYPE + " " + conductor_size);


            if (POLE_SUPPORT != null) {
                if (POLE_SUPPORT.equalsIgnoreCase("None")) {
                    sp_pole_support_lt.setSelection(1, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT")) {
                    sp_pole_support_lt.setSelection(2, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STAY")) {
                    sp_pole_support_lt.setSelection(3, true);
                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT and STAY")) {
                    sp_pole_support_lt.setSelection(4, true);
                }
            }

            if (POLE_SUPPORT != null) {
                if (NUMB_9M_STRUTS > 0) {
                    ch_33kv_9_stuart_poles_lt.setChecked(true);
                }
                if (NUMB_8M_STRUTS > 0) {
                    ch_33kv_8_stuart_poles_lt.setChecked(true);
                }

                String strut9M = String.valueOf(NUMB_9M_STRUTS);
                String strut8M = String.valueOf(NUMB_8M_STRUTS);

                if (POLE_SUPPORT.equalsIgnoreCase("STRUT")) {
                    if (strut9M.equalsIgnoreCase("1")) {
                        sp_no_9_poles_lt.setSelection(0, true);
                    } else if (strut9M.equalsIgnoreCase("2")) {
                        sp_no_9_poles_lt.setSelection(1, true);
                    } else if (strut9M.equalsIgnoreCase("3")) {
                        sp_no_9_poles_lt.setSelection(2, true);
                    } else if (strut9M.equalsIgnoreCase("4")) {
                        sp_no_9_poles_lt.setSelection(3, true);
                    }
                    if (strut8M.equalsIgnoreCase("1")) {
                        sp_no_8_poles_lt.setSelection(0, true);
                    } else if (strut8M.equalsIgnoreCase("2")) {
                        sp_no_8_poles_lt.setSelection(1, true);
                    } else if (strut8M.equalsIgnoreCase("3")) {
                        sp_no_8_poles_lt.setSelection(2, true);
                    } else if (strut8M.equalsIgnoreCase("4")) {
                        sp_no_8_poles_lt.setSelection(3, true);
                    }

                } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT and STAY")) {
                    if (strut9M.equalsIgnoreCase("1")) {
                        sp_no_9_poles_lt.setSelection(0, true);
                    } else if (strut9M.equalsIgnoreCase("2")) {
                        sp_no_9_poles_lt.setSelection(1, true);
                    } else if (strut9M.equalsIgnoreCase("3")) {
                        sp_no_9_poles_lt.setSelection(2, true);
                    } else if (strut9M.equalsIgnoreCase("4")) {
                        sp_no_9_poles_lt.setSelection(3, true);
                    }
                    if (strut8M.equalsIgnoreCase("1")) {
                        sp_no_8_poles_lt.setSelection(0, true);
                    } else if (strut8M.equalsIgnoreCase("2")) {
                        sp_no_8_poles_lt.setSelection(1, true);
                    } else if (strut8M.equalsIgnoreCase("3")) {
                        sp_no_8_poles_lt.setSelection(2, true);
                    } else if (strut8M.equalsIgnoreCase("4")) {
                        sp_no_8_poles_lt.setSelection(3, true);
                    }
                    String numOfStays = String.valueOf(NUMB_STAYS);
                    if (numOfStays.equalsIgnoreCase("1")) {
                        sp_no_stays_lt.setSelection(0, true);
                    } else if (numOfStays.equalsIgnoreCase("2")) {
                        sp_no_stays_lt.setSelection(1, true);
                    } else if (numOfStays.equalsIgnoreCase("3")) {
                        sp_no_stays_lt.setSelection(2, true);
                    } else if (numOfStays.equalsIgnoreCase("4")) {
                        sp_no_stays_lt.setSelection(3, true);
                    } else if (numOfStays.equalsIgnoreCase("5")) {
                        sp_no_stays_lt.setSelection(4, true);
                    } else if (numOfStays.equalsIgnoreCase("6")) {
                        sp_no_stays_lt.setSelection(5, true);
                    }

                } else if (POLE_SUPPORT.equalsIgnoreCase("STAY")) {
                    String numOfStays = String.valueOf(NUMB_STAYS);
                    if (numOfStays.equalsIgnoreCase("1")) {
                        sp_no_stays_lt.setSelection(0, true);
                    } else if (numOfStays.equalsIgnoreCase("2")) {
                        sp_no_stays_lt.setSelection(1, true);
                    } else if (numOfStays.equalsIgnoreCase("3")) {
                        sp_no_stays_lt.setSelection(2, true);
                    } else if (numOfStays.equalsIgnoreCase("4")) {
                        sp_no_stays_lt.setSelection(3, true);
                    } else if (numOfStays.equalsIgnoreCase("5")) {
                        sp_no_stays_lt.setSelection(4, true);
                    } else if (numOfStays.equalsIgnoreCase("6")) {
                        sp_no_stays_lt.setSelection(5, true);
                    }

                }
            }


            Log.d("NUMB_9M_STRUTS---->", "repeatPole33KV: " + NUMB_9M_STRUTS);

            if (conductor_size.equalsIgnoreCase("AAA CONDUCTOR")) {
                sp_conductor_lt.setSelection(1, true);
            } else if (conductor_size.equalsIgnoreCase("AB CABLE")) {
                sp_conductor_lt.setSelection(2, true);
            }
//            else if (conductor_size.equalsIgnoreCase("XLPE CABLE")) {
//                sp_conductor_lt.setSelection(3, true);
//            } else if (conductor_size.equalsIgnoreCase("PVC CABLE")) {
//                sp_conductor_lt.setSelection(4, true);
//            } else if (conductor_size.equalsIgnoreCase("ACSR CONDUCTOR")) {
//                sp_conductor_lt.setSelection(5, true);
//            }


        }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, dtr_phase);
        sp_dtr_phase_11.setAdapter(adapter);
    }

    private void setDTRCapacity(String dtrCapacity) {
        switch (dtrCapacity) {
            case "25 kVA":
                sp_dtr_capacity_phase1_11.setSelection(1, true);
                break;
            case "40 kVA":
                sp_dtr_capacity_phase1_11.setSelection(2, true);
                break;
            case "63 kVA":
                sp_dtr_capacity_phase1_11.setSelection(3, true);
                break;
            case "100 kVA":
                sp_dtr_capacity_phase1_11.setSelection(4, true);
                break;
            case "165 kVA":
                sp_dtr_capacity_phase1_11.setSelection(5, true);
                break;
            case "315 kVA":
                sp_dtr_capacity_phase1_11.setSelection(6, true);
                break;
            case "650 kVA":
                sp_dtr_capacity_phase1_11.setSelection(7, true);
                break;
            default:
                sp_dtr_capacity_phase1_11.setSelection(0, true); // Default or unknown capacity
                break;
        }
    }


    public static class DistanceCalculator {


        private static final double EARTH_RADIUS = 6371.0;

        public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            double lat1Rad = Math.toRadians(lat1);
            double lon1Rad = Math.toRadians(lon1);
            double lat2Rad = Math.toRadians(lat2);
            double lon2Rad = Math.toRadians(lon2);


            double dLat = lat2Rad - lat1Rad;
            double dLon = lon2Rad - lon1Rad;

            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // Distance in kilometers
            return EARTH_RADIUS * c;
        }
    }


    private void validateLTSurvey() {
        if (listLT.isEmpty() && sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select is parent exits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pin cut", Toast.LENGTH_SHORT).show();
            return;

        }

//        if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {
//            if (!ch_no_of_v_crossing_lt.isChecked() && !ch_top_cleat_lt.isChecked() && !ch_tapping_cross_arms_lt.isChecked()) {
//                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//        }

        if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            if (sp_ded_end_pole_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please select is end pole", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Yes")
                && sp_parent_connecting_point.getSelectedItem().toString().equalsIgnoreCase("Select")
        ) {
            Toast.makeText(context, "Please Select parent connecting point", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("No")
                && tapping_location.getText().toString().isEmpty()
        ) {
            Toast.makeText(context, "Please Enter tapping location", Toast.LENGTH_SHORT).show();
            return;
        }


        List<MaterialMasterModel> model = databaseEstimates.getSpecificClamps(sp_pole_height_lt.getSelectedItem().toString());


        if (!model.isEmpty()) {
            if (sp_type_black_clamps_lt != null && sp_type_black_clamps_lt.getSelectedItem() != null &&
                    !sp_type_black_clamps_lt.getSelectedItem().toString().isEmpty()) {
                if (sp_no_of_black_clamps_lt == null || sp_no_of_black_clamps_lt.getSelectedItem() == null ||
                        sp_no_of_black_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(context, "Please Select Number of back clamps", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }


        if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT")
                || sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (ch_33kv_9_stuart_poles_lt.isChecked()) {
                if (!sp_sturt_type_black_clamps_lt.getSelectedItem().toString().isEmpty()) {
                    if (sp_sturt_no_of_black_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please select number of 9.1 Stay clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
            if (ch_33kv_8_stuart_poles_lt.isChecked()) {
                if (!sp_sturt_8m_type_black_clamps_lt.getSelectedItem().toString().isEmpty()) {
                    if (sp_sturt_8m_no_of_black_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please select number of 8 Stay clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }


            List<MaterialMasterModel> stayClampsModel = databaseEstimates.getSpecificSturtClamps(sp_pole_height_lt.getSelectedItem().toString());

            if (!stayClampsModel.isEmpty()) {
                if (sp_main_pole_stay_clamps_lt != null && sp_main_pole_stay_clamps_lt.getSelectedItem() != null &&
                        !sp_main_pole_stay_clamps_lt.getSelectedItem().toString().isEmpty()) {
                    if (sp_no_main_pole_stay_clamps_lt == null || sp_no_main_pole_stay_clamps_lt.getSelectedItem() == null ||
                            sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please select number Stay clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

//            if (sp_main_pole_stay_clamps_lt != null && sp_main_pole_stay_clamps_lt.getSelectedItem() != null &&
//                    !sp_main_pole_stay_clamps_lt.getSelectedItem().toString().isEmpty()) {
//                if (!sp_main_pole_stay_clamps_lt.getSelectedItem().toString().isEmpty()) {
//                    if (sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//                        Toast.makeText(context, "Please select number Stay clamps", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//            }


        }

        //sp_no_of_disc_insulators_33kv


        if (sp_is_multi_circuit_lt.getSelectedItem().toString().equalsIgnoreCase("no")
                && !ch_multi_33kv_lt.isChecked() && !ch_multi_11kv_lt.isChecked() && !ch_multi_lt_lt.isChecked()) {
            Toast.makeText(context, "Please Select other multi circuits", Toast.LENGTH_SHORT).show();
            return;
        }


        if (sp_is_multi_circuit_lt.getSelectedItem().toString().equalsIgnoreCase("no")
                && ch_multi_33kv_lt.isChecked() && sp_33kv_multi_ckt_pole_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select 33KV multi circuit Pole Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_multi_circuit_lt.getSelectedItem().toString().equalsIgnoreCase("no")
                && ch_multi_11kv_lt.isChecked() && sp_11kv_multi_ckt_pole_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select 11KV multi circuit Pole Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_multi_circuit_lt.getSelectedItem().toString().equalsIgnoreCase("no")
                && ch_multi_lt_lt.isChecked() && sp_lt_multi_ckt_pole_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select LT multi circuit Pole Number", Toast.LENGTH_SHORT).show();
            return;
        }


        if (sp_pole_configuration_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pole Configuration", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_conductor_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Conductor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pole Support", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Line Survey Type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            if (!ch_33kv_9_stuart_poles_lt.isChecked() && !ch_33kv_8_stuart_poles_lt.isChecked()) {
                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (!ch_33kv_9_stuart_poles_lt.isChecked() && !ch_33kv_8_stuart_poles_lt.isChecked()) {
                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (distance > distanceInFloat) {
            showDistanceWarningDialog(distance);
            return;
        }

        addPoleToDataBaseLT();

    }


    private void addPoleToDataBaseLT() {
        NewModel model = new NewModel();
        model.setEST_NO(EST_NO);
        model.setVOLTAGE_LEVEL("LT");
        model.setIS_NEW_SURVEY("Y");
        model.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());


        model.setIS_EXISTING_LOCATION_NEW(sp_is_location_exists.getSelectedItem().toString());
        if (sp_is_location_exists.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
            model.setEXISTING_LOCATION_33_NEW(ch_ext_loc_33kv.isChecked() ? "Yes" : "No");
            model.setEXISTING_LOCATION_11_NEW(ch_ext_loc_11kv.isChecked() ? "Yes" : "No");
            model.setEXISTING_LOCATION_LT_NEW(ch_ext_loc_lt.isChecked() ? "Yes" : "No");
        }


        model.setPOLE_NO(String.valueOf(poleNumber));
        model.setLINE_TYPE("OverHead");
        model.setNEW_DATA_ADDED("Yes");
        model.setPIN_CUT(sp_pole_pin_cut_point_lt.getSelectedItem().toString());
        if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            model.setEND_POLE(sp_ded_end_pole_lt.getSelectedItem().toString());
        } else {
            model.setEND_POLE("");

        }

        model.setIS_MULTI_CKT(sp_is_multi_circuit_lt.getSelectedItem().toString());
        if (sp_is_multi_circuit_lt.getSelectedItem().toString().equalsIgnoreCase("no")) {
            model.setMULTI_CKT_33(ch_multi_33kv_lt.isChecked() ? "Yes" : "No");
            model.setMULTI_CKT_11(ch_multi_11kv_lt.isChecked() ? "Yes" : "No");
            model.setMULTI_CKT_LT(ch_multi_lt_lt.isChecked() ? "Yes" : "No");

            if (ch_multi_33kv_lt.isChecked()) {
                model.setMULTI_CKT_33_POLE_NO(sp_33kv_multi_ckt_pole_lt.getSelectedItem().toString());
            }
            if (ch_multi_11kv_lt.isChecked()) {
                model.setMULTI_CKT_11_POLE_NO(sp_11kv_multi_ckt_pole_lt.getSelectedItem().toString());
            }
            if (ch_multi_lt_lt.isChecked()) {
                model.setMULTI_CKT_LT_POLE_NO(sp_lt_multi_ckt_pole_lt.getSelectedItem().toString());
            }
        }

        model.setPOLE_HEIGHT(sp_pole_height_lt.getSelectedItem().toString());
        model.setCONDUCTOR_SIZE(sp_conductor_lt.getSelectedItem().toString());

        if (!sp_pole_height_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            model.setPOLE_DESC(sp_sp_pole_desc_lt.getSelectedItem().toString());
            model.setPOLE_SAP_CODE(poleSapCodeLT);
        }

        if (!sp_conductor_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            String conductorLT = sp_conductor_size_lt.getSelectedItem() != null ? sp_conductor_size_lt.getSelectedItem().toString() : null;
            if (conductorLT != null && !conductorLT.equalsIgnoreCase("Select")) {
                model.setCONDUCTOR_DESC(conductorLT);
                model.setCONDUCTOR_SAP_CODE(conductorSAPCodeLT);
            }
        }

        if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            String poleType = sp_pole_type_lt.getSelectedItem().toString();
            String poleHeight = sp_pole_height_lt.getSelectedItem().toString();
            String stayClampDesc = sp_main_pole_stay_clamps_lt.getSelectedItem().toString();

            if (poleType.equalsIgnoreCase("PSCC")) {
                if (poleHeight.equalsIgnoreCase("9.1")) {
                    model.setSTAYCLAMP_9M_CODE(stayCode9LT);
                    model.setSTAYCLAMP_9M_DESC(stayClampDesc);
                    if (!sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString()));

                    }
                   // System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("11")) {
                    model.setSTAYCLAMP_11M_CODE(stayCodeLT);
                    model.setSTAYCLAMP_11M_DESC(stayClampDesc);
                    if (!sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_11M(Integer.parseInt(sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString()));

                    }
                   // System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("8")) {
                    model.setSTAYCLAMP_8M_CODE(stayCode8LT);
                    model.setSTAYCLAMP_8M_DESC(stayClampDesc);
                    if (!sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString()));

                    }
                  //  System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                }

            } else if (poleType.equalsIgnoreCase("SPUN")) {
                model.setSTAYCLAMP_SPUN_CODE(stayCodeLT);
                model.setSTAYCLAMP_SPUN_DESC(stayClampDesc);
                if (!sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_SPUN(Integer.parseInt(sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString()));

                }
            }

            if (ch_33kv_9_stuart_poles_lt.isChecked()) {
                model.setSTAYCLAMP_9M_CODE(stayCodeLT);
                model.setSTAYCLAMP_9M_DESC(sp_sturt_type_black_clamps_lt.getSelectedItem().toString());

                if (!sp_no_9_poles_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_no_9_poles_lt.getSelectedItem().toString()));

                }

            }

            if (ch_33kv_8_stuart_poles_lt.isChecked()) {
                model.setSTAYCLAMP_8M_CODE(stayCodeLT);
                model.setSTAYCLAMP_8M_DESC(sp_sturt_8m_type_black_clamps_lt.getSelectedItem().toString());

                if (!sp_no_8_poles_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_no_8_poles_lt.getSelectedItem().toString()));

                }

            }

        } else if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            String poleType = sp_pole_type_lt.getSelectedItem().toString();
            String poleHeight = sp_pole_height_lt.getSelectedItem().toString();
            String stayClampDesc = sp_main_pole_stay_clamps_lt.getSelectedItem().toString();

            if (poleType.equalsIgnoreCase("PSCC")) {
                if (poleHeight.equalsIgnoreCase("9.1")) {
                    model.setSTAYCLAMP_9M_CODE(stayCode9LT);
                    model.setSTAYCLAMP_9M_DESC(stayClampDesc);
                    if (!sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString()));

                    }
                  //  System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("11")) {
                    model.setSTAYCLAMP_11M_CODE(stayCodeLT);
                    model.setSTAYCLAMP_11M_DESC(stayClampDesc);
                    if (!sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_11M(Integer.parseInt(sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString()));

                    }
                   // System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("8")) {
                    model.setSTAYCLAMP_8M_CODE(stayCode8LT);
                    model.setSTAYCLAMP_8M_DESC(stayClampDesc);
                    if (!sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString()));

                    }
                  //  System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                }

            } else if (poleType.equalsIgnoreCase("SPUN")) {
                model.setSTAYCLAMP_SPUN_CODE(stayCodeLT);
                model.setSTAYCLAMP_SPUN_DESC(stayClampDesc);
                if (!sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_SPUN(Integer.parseInt(sp_no_main_pole_stay_clamps_lt.getSelectedItem().toString()));

                }
            }

            if (ch_33kv_9_stuart_poles_lt.isChecked()) {
                model.setSTAYCLAMP_9M_CODE(stayCodeLT);
                model.setSTAYCLAMP_9M_DESC(sp_sturt_type_black_clamps_lt.getSelectedItem().toString());

                if (!sp_no_9_poles_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_no_9_poles_lt.getSelectedItem().toString()));

                }

            }

            if (ch_33kv_8_stuart_poles_lt.isChecked()) {
                model.setSTAYCLAMP_8M_CODE(stayCodeLT);
                model.setSTAYCLAMP_8M_DESC(sp_sturt_8m_type_black_clamps_lt.getSelectedItem().toString());

                if (!sp_no_8_poles_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_no_8_poles_lt.getSelectedItem().toString()));

                }

            }

        }

        if (sp_pole_height_lt != null && sp_type_black_clamps_lt.getSelectedItem() != null) {
            model.setBACK_CLAMPS_DESC(sp_type_black_clamps_lt.getSelectedItem().toString());
            model.setBACK_CLAMPS_CODE(clampLtCode);
            if (!sp_no_of_black_clamps_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_BACK_CLAMPS(Integer.parseInt(sp_no_of_black_clamps_lt.getSelectedItem().toString()));
            }
        } else {
            model.setBACK_CLAMPS_DESC("");
            model.setBACK_CLAMPS_CODE("");
            model.setCOUNT_BACK_CLAMPS(0);
        }

        if (ch_side_arm_lt.isChecked()) {
            model.setCOUNT_SIDE_ARM(1);
            model.setSIDEARM_DESC(sp_side_arm_lt.getSelectedItem().toString());
            model.setSIDEARM_CODE(sideArmLtCode);

        } else {
            model.setCOUNT_SIDE_ARM(0);
            model.setSIDEARM_DESC(null);
            model.setSIDEARM_CODE(null);
        }

        if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            model.setMETAL_PARTS_CODE(metalPartCodeLT);
            model.setMETAL_PARTS_DESC(sp_type_metal_lt.getSelectedItem().toString());
            String metalPartsText = tv_no_of_metal_parts_lt.getText() != null ? tv_no_of_metal_parts_lt.getText().toString() : "";
            model.setCOUNT_METALS_PARTS(!metalPartsText.isEmpty() ? Integer.parseInt(metalPartsText) : 0);
        } else {
            model.setMETAL_PARTS_CODE("");
            model.setMETAL_PARTS_DESC("");
            model.setCOUNT_METALS_PARTS(0);
        }
        if (ch_tapping_cross_arms_lt.isChecked()) {
            if (!sp_no_of_tapping_cross_arms_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_V_XARM(Integer.parseInt(sp_no_of_tapping_cross_arms_lt.getSelectedItem().toString()));
                model.setVXARM_DESC(sp_tapping_x_arm_lt.getSelectedItem().toString());
                model.setVXARM_CODE(ltCrossArm);

//                model.setCOUNT_TOP_CLEAT(1);
//                model.setTOPCLEAT_DESC("33KV TOP CLEAT WITH CLAMPS");
//                model.setTOPCLEAT_CODE("FBR30006");

            }
        } else {
            model.setCOUNT_V_XARM(0);
            model.setVXARM_DESC(null);
            model.setVXARM_CODE(null);

            model.setCOUNT_TOP_CLEAT(0);
            model.setTOPCLEAT_DESC(null);
            model.setTOPCLEAT_CODE(null);
        }


        if (ch_pin_insulators_lt.isChecked()) {
            if (!sp_no_of_pin_insulators_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_PIN_INS(Integer.parseInt(sp_no_of_pin_insulators_lt.getSelectedItem().toString()));
                model.setPIN_INS_DESC(sp_pin_insulators_type_lt.getSelectedItem().toString());
                model.setPIN_INS_CODE(pinInsulatorCodeLT);

                model.setGI_PIN_CODE(ltGIPinCode);
                model.setGI_PIN_DESC(sp_gi_pins_type_lt.getSelectedItem().toString());
                model.setCOUNT_GI_PIN(Integer.parseInt(sp_no_of_gi_pins_lt.getSelectedItem().toString()));

            }
        } else {
            model.setCOUNT_PIN_INS(0);
            model.setPIN_INS_DESC(null);
            model.setPIN_INS_CODE(null);
        }

        if (ch_disc_insulators_lt.isChecked()) {
            if (!sp_no_of_disc_insulators_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_DISC_INS(Integer.parseInt(sp_no_of_disc_insulators_lt.getSelectedItem().toString()));
                model.setDISC_INS_DESC(sp_type_disc_insulators_lt.getSelectedItem().toString());
                model.setDISC_INS_CODE(discInsulatorLT);

            }
        } else {
            model.setCOUNT_DISC_INS(0);
            model.setDISC_INS_DESC(null);
            model.setDISC_INS_CODE(null);
        }

        model.setPOLE_TYPE(sp_pole_type_lt.getSelectedItem().toString());
        model.setPOLE_SUPPORT(sp_pole_support_lt.getSelectedItem().toString());
        model.setPOLE_CONFIG(sp_pole_configuration_lt.getSelectedItem().toString());
        model.setPOLE_LOCATION_TYPE(sp_survey_pole_type.getSelectedItem().toString());
//        model.setLATITUDE(poleLat);
//        model.setLONGITUDE(poleLon);

        if (sp_survey_pole_type.getSelectedItem().toString().equalsIgnoreCase("Mapped Location")) {
            model.setPOLE_NO(sp_exsting_connecting_point.getSelectedItem().toString());
            ArrayList<NewModel> latAndLonPole = databaseEstimates.getLatAndLon(EST_NO, sp_exsting_connecting_point.getSelectedItem().toString(),GIS_ID);
            model.setLATITUDE(latAndLonPole.get(0).getLATITUDE());
            model.setLONGITUDE(latAndLonPole.get(0).getLONGITUDE());
            model.setLOCATION_ID(latAndLonPole.get(0).getLOCATION_ID());
            Log.d("MAPPED_LAT_LONGS", "addPoleToDataBase11: " + latAndLonPole.get(0).getLATITUDE() + "----" + latAndLonPole.get(0).getLONGITUDE());

            if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                model.setFROM_POLE(sp_parent_connecting_point.getSelectedItem().toString());
                ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_parent_connecting_point.getSelectedItem().toString(),GIS_ID);
                model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
                model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());
            } else {
                model.setFROM_POLE("0");
                ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, "0",GIS_ID);
                for (int i = 0; i < latAndLon.size(); i++) {
                    model.setFROM_POLE_LATITUDE(latAndLon.get(i).getLATITUDE());
                    model.setFROM_POLE_LONGITUDE(latAndLon.get(i).getLONGITUDE());
                }

                model.setEXI_POLE_NO(sp_exsting_connecting_point.getSelectedItem().toString());
            }


        } else {
            model.setPOLE_NO(String.valueOf(poleNumber));
            model.setLOCATION_ID(Long.toString(randomNumber));
            model.setLONGITUDE(poleLon);
            model.setLATITUDE(poleLat);
            if (poleNumber == 1) {
                model.setFROM_POLE(String.valueOf(0));
                model.setFROM_POLE_LATITUDE(poleLat);
                model.setFROM_POLE_LONGITUDE(poleLon);
            } else if (list33.isEmpty()) {
                model.setFROM_POLE(String.valueOf(0));
                model.setFROM_POLE_LATITUDE(poleLat);
                model.setFROM_POLE_LONGITUDE(poleLon);
            }
        }
        if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            if (ch_33kv_9_stuart_poles_lt.isChecked()) {
                model.setNUMB_9M_STRUTS(Integer.parseInt(sp_no_9_poles_lt.getSelectedItem().toString()));

            }
            if (ch_33kv_8_stuart_poles_lt.isChecked()) {
                model.setNUMB_8M_STRUTS(Integer.parseInt(sp_no_8_poles_lt.getSelectedItem().toString()));

            }
        } else if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STAY")) {
            model.setNUMB_STAYS(Integer.parseInt(sp_no_stays_lt.getSelectedItem().toString()));
        } else if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (ch_33kv_9_stuart_poles_lt.isChecked()) {
                model.setNUMB_9M_STRUTS(Integer.parseInt(sp_no_9_poles_lt.getSelectedItem().toString()));
            }
            if (ch_33kv_8_stuart_poles_lt.isChecked()) {
                model.setNUMB_8M_STRUTS(Integer.parseInt(sp_no_8_poles_lt.getSelectedItem().toString()));
            }
            model.setNUMB_STAYS(Integer.parseInt(sp_no_stays_lt.getSelectedItem().toString()));

        } else {
            model.setNUMB_9M_STRUTS(0);
            model.setNUMB_8M_STRUTS(0);
            model.setNUMB_STAYS(0);
        }

        if (listLT.isEmpty() && sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("No")) {
            model.setTAPPING_LOCATION(tapping_location.getText().toString());
            model.setFROM_POLE(String.valueOf(0));
            model.setFROM_POLE_LATITUDE(poleLat);
            model.setFROM_POLE_LONGITUDE(poleLon);
        } else if (listLT.isEmpty() && sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
            model.setFROM_POLE(sp_parent_connecting_point.getSelectedItem().toString());
            ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_parent_connecting_point.getSelectedItem().toString(),GIS_ID);
            model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
            model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());
        } else {
            String fromPole = sp_connecting_point.getSelectedItem().toString();
            if (fromPole.contains("-")) {
                String[] dtrcode = fromPole.split("-");
                fromPole = dtrcode[1];
            }
            model.setFROM_POLE(fromPole);
            ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, fromPole,GIS_ID);
            model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
            model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());


        }

        boolean status = databaseEstimates.addNewSurveyData(model);
        if (status) {
            if (allList.isEmpty()) {
                EstimatesMap.btn_complete_survey.setVisibility(View.VISIBLE);
            }

            if (sp_is_multi_circuit_lt.getSelectedItem().toString().equalsIgnoreCase("Yes")) {

                if (ch_multi_33kv_lt.isChecked()) {
                    databaseEstimates.updateMultiCircuit33(EST_NO, String.valueOf(poleNumber), sp_33kv_multi_ckt_pole_lt.getSelectedItem().toString(),GIS_ID);
                }
                if (ch_multi_11kv_lt.isChecked()) {
                    databaseEstimates.updateMultiCircuit11(EST_NO, String.valueOf(poleNumber), sp_11kv_multi_ckt_pole_lt.getSelectedItem().toString(),GIS_ID);
                }
                if (ch_multi_lt_lt.isChecked()) {
                    databaseEstimates.updateMultiCircuitLT(EST_NO, String.valueOf(poleNumber), sp_lt_multi_ckt_pole_lt.getSelectedItem().toString(),GIS_ID);
                }
            }

            Toast.makeText(context, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
            dismiss();
            callout.dismiss();
            graphicsOverlay.getGraphics().remove(graphic);
        } else {
            Toast.makeText(context, "Failed to capture Pole location.", Toast.LENGTH_SHORT).show();
        }

    }

    private void validate11KVSurvey() {
        if (list11.isEmpty() && sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select is parent exits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Yes")
                && sp_parent_connecting_point.getSelectedItem().toString().equalsIgnoreCase("Select")
        ) {
            Toast.makeText(context, "Please Select parent connecting point", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("No")
                && tapping_location.getText().toString().isEmpty()
        ) {
            Toast.makeText(context, "Please Enter tapping location", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pin/Cut ", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select is multi circuit", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("No")
                && !ch_multi_33kv_11.isChecked() && !ch_multi_11kv_11.isChecked() && !ch_multi_lt_11.isChecked()) {
            Toast.makeText(context, "Please Select other multi circuits", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            if (sp_ded_end_pole_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please select is end pole", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("No")
                && ch_multi_33kv_11.isChecked() && sp_33kv_multi_ckt_pole_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select 33kv multi circuit pole number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("No")
                && ch_multi_11kv_11.isChecked() && sp_11kv_multi_ckt_pole_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select 11kv multi circuit pole number", Toast.LENGTH_SHORT).show();
            return;
        }


        if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pole Configuration", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("SS Bay")) {
            if (sp_pole_type_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please Select Pole type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sp_pole_height_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please Select Pole Height", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Conductor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pole Support", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Line Survey Type", Toast.LENGTH_SHORT).show();
            return;
        }


      //  System.out.println("Pole_Configuration====" + sp_pole_configuration_33.getSelectedItem().toString());
        if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("SS BAY")) {

        } else {
            List<MaterialMasterModel> model = databaseEstimates.getSpecificClamps(sp_pole_height_11.getSelectedItem().toString());


            if (!model.isEmpty()) {
                if (sp_type_black_clamps_11kv != null && sp_type_black_clamps_11kv.getSelectedItem() != null &&
                        !sp_type_black_clamps_11kv.getSelectedItem().toString().isEmpty()) {
                    if (sp_no_of_black_clamps_11kv == null || sp_no_of_black_clamps_11kv.getSelectedItem() == null ||
                            sp_no_of_black_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please Select Number of back clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }


        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT")
                || sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (ch_33kv_9_stuart_poles_11kv.isChecked()) {
                if (!sp_sturt_type_black_clamps_11kv.getSelectedItem().toString().isEmpty()) {
                    if (sp_sturt_no_of_black_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please select number of 9.1 Stay clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
            if (ch_33kv_8_stuart_poles_11kv.isChecked()) {
                if (!sp_sturt_8m_type_black_clamps_11kv.getSelectedItem().toString().isEmpty()) {
                    if (sp_sturt_8m_no_of_black_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please select number of 8 Stay clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }

          //  System.out.println("Pole_Configuration====" + sp_pole_configuration_11.getSelectedItem().toString());
            if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("SS BAY")) {

            } else {
                List<MaterialMasterModel> stayClampsModel = databaseEstimates.getSpecificSturtClamps(sp_pole_height_11.getSelectedItem().toString());

                if (!stayClampsModel.isEmpty()) {
                    if (sp_main_pole_stay_clamps_11kv != null && sp_main_pole_stay_clamps_11kv.getSelectedItem() != null &&
                            !sp_main_pole_stay_clamps_11kv.getSelectedItem().toString().isEmpty()) {
                        if (sp_no_main_pole_stay_clamps_11kv == null || sp_no_main_pole_stay_clamps_11kv.getSelectedItem() == null ||
                                sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(context, "Please select number Stay clamps", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }


//            if (!sp_main_pole_stay_clamps_11kv.getSelectedItem().toString().isEmpty()) {
//                if (sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//                    Toast.makeText(context, "Please select number Stay clamps", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }


        }

        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            if (!ch_33kv_9_stuart_poles_11kv.isChecked() && !ch_33kv_8_stuart_poles_11kv.isChecked()) {
                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (!ch_33kv_9_stuart_poles_11kv.isChecked() && !ch_33kv_8_stuart_poles_11kv.isChecked()) {
                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
            if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("DTR Pole")) {
                if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(context, "Please Select DTR Survey Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed") ||
                        sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                    if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please Select DTR Phase", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("I")) {
                        if (sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(context, "Please Select DTR Capacity", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("III")) {
                        if (sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(context, "Please Select DTR Capacity", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    if (dtr_structure_code_11kv.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Please Select DTR Structure Code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (sp_dtr_mounting_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(context, "Please Select DTR Mounting Type", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!ch_ci_pipe.isChecked() && !ch_gi_pipe.isChecked()) {
                    Toast.makeText(context, "Please Select At-least one Pipe", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ch_gi_pipe.isChecked()) {
                    if (sp_gi_pipes.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please Select GI Pipe", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (ch_ci_pipe.isChecked()) {
                    if (sp_ci_pipes.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please Select CI Pipe", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                if (distance > distanceInFloat) {
                    showDistanceWarningDialog(distance);
                    return;
                }
            }
        }

        addPoleToDataBase11();

    }

    private void validate63KVSurvey() {
        if (list11.isEmpty() && sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select is parent exits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Yes")
                && sp_parent_connecting_point.getSelectedItem().toString().equalsIgnoreCase("Select")
        ) {
            Toast.makeText(context, "Please Select parent connecting point", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("No")
                && tapping_location.getText().toString().isEmpty()
        ) {
            Toast.makeText(context, "Please Enter tapping location", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select is multi circuit", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("No")
                && !ch_multi_33kv_11.isChecked() && !ch_multi_11kv_11.isChecked() && !ch_multi_lt_11.isChecked()) {
            Toast.makeText(context, "Please Select other multi circuits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("No")
                && ch_multi_33kv_11.isChecked() && sp_33kv_multi_ckt_pole_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select 33kv multi circuit pole number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("No")
                && ch_multi_11kv_11.isChecked() && sp_11kv_multi_ckt_pole_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select 11kv multi circuit pole number", Toast.LENGTH_SHORT).show();
            return;
        }


        if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pole Configuration", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("SS Bay")) {
            if (sp_pole_type_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please Select Pole type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sp_pole_height_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please Select Pole Height", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Conductor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pole Support", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Line Survey Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            if (!ch_33kv_9_stuart_poles_11kv.isChecked() && !ch_33kv_8_stuart_poles_11kv.isChecked()) {
                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (!ch_33kv_9_stuart_poles_11kv.isChecked() && !ch_33kv_8_stuart_poles_11kv.isChecked()) {
                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("6.3KV")) {
            if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("DTR Pole")) {
                if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {
                    if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please Select DTR Phase", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("I")) {
                        if (sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(context, "Please Select DTR Capacity", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                } else {
                    if (dtr_structure_code_11kv.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Please Select DTR Structure Code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (sp_dtr_mounting_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(context, "Please Select DTR Mounting Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(context, "Please Select DTR Survey Type", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!ch_ci_pipe.isChecked() && !ch_gi_pipe.isChecked()) {
                    Toast.makeText(context, "Please Select At-least one Pipe", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ch_gi_pipe.isChecked()) {
                    if (sp_gi_pipes.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please Select GI Pipe", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (ch_ci_pipe.isChecked()) {
                    if (sp_ci_pipes.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please Select CI Pipe", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                if (distance > distanceInFloat) {
                    showDistanceWarningDialog(distance);
                    return;
                }
            }
        }

        addPoleToDataBase11();

    }

    private void addPoleToDataBase11() {
        NewModel model = new NewModel();
        model.setEST_NO(EST_NO);
        model.setVOLTAGE_LEVEL("11KV");
        model.setIS_NEW_SURVEY("Y");
        model.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
        String data11kv = databaseEstimates.getFeederDetails11KV(EST_NO, "11KV",GIS_ID);
        if (data11kv != null && !data11kv.isEmpty()) {
            String[] parts = data11kv.split(",");
            if (parts.length > 1 && parts[0].contains(":") && parts[1].contains(":")) {
                String feederIdPart = parts[0].split(":")[1].trim();
                String feederNamePart = parts[1].split(":")[1].trim();

                model.setFEEDER_ID(feederIdPart);
                model.setFEEDER_NAME(feederNamePart);
            } else {
               // Log.e("Error", "Unexpected data format: " + data11kv);
            }
        } else {
           // Log.e("Error", "data11kv is null or empty.");
        }


//        String data11kv = databaseEstimates.getFeederDetails11KV(EST_NO, "11KV");
//
//        String[] parts = data11kv.split(",");
//        String feederIdPart = parts[0].split(":")[1].trim();
//        String feederNamePart = parts[1].split(":")[1].trim();
//
//        model.setFEEDER_ID(feederIdPart);
//        model.setFEEDER_NAME(feederNamePart);
        model.setLINE_TYPE("OverHead");


        if (sp_survey_pole_type.getSelectedItem().toString().equalsIgnoreCase("Mapped Location")) {
            model.setPOLE_NO(sp_exsting_connecting_point.getSelectedItem().toString());
            ArrayList<NewModel> latAndLonPole = databaseEstimates.getLatAndLon(EST_NO, sp_exsting_connecting_point.getSelectedItem().toString(),GIS_ID);
            model.setLATITUDE(latAndLonPole.get(0).getLATITUDE());
            model.setLONGITUDE(latAndLonPole.get(0).getLONGITUDE());
            model.setLOCATION_ID(latAndLonPole.get(0).getLOCATION_ID());
            Log.d("MAPPED_LAT_LONGS", "addPoleToDataBase11: " + latAndLonPole.get(0).getLATITUDE() + "----" + latAndLonPole.get(0).getLONGITUDE());

            if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                model.setFROM_POLE(sp_parent_connecting_point.getSelectedItem().toString());
                ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_parent_connecting_point.getSelectedItem().toString(),GIS_ID);
                model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
                model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());
            } else {
                model.setFROM_POLE("0");
                ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, "0",GIS_ID);
                for (int i = 0; i < latAndLon.size(); i++) {
                    model.setFROM_POLE_LATITUDE(latAndLon.get(i).getLATITUDE());
                    model.setFROM_POLE_LONGITUDE(latAndLon.get(i).getLONGITUDE());
                }

                model.setEXI_POLE_NO(sp_exsting_connecting_point.getSelectedItem().toString());
            }

//            model.setFROM_POLE(sp_connecting_point.getSelectedItem().toString());
//            ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_connecting_point.getSelectedItem().toString());
//            model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
//            model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());
//            model.setEXI_POLE_NO(sp_exsting_connecting_point.getSelectedItem().toString());
        } else {
            model.setPOLE_NO(String.valueOf(poleNumber));
            model.setLOCATION_ID(Long.toString(randomNumber));
            model.setLONGITUDE(poleLon);
            model.setLATITUDE(poleLat);
            if (poleNumber == 1) {
                model.setFROM_POLE(String.valueOf(0));
                model.setFROM_POLE_LATITUDE(poleLat);
                model.setFROM_POLE_LONGITUDE(poleLon);
            } else if (list33.isEmpty()) {
                model.setFROM_POLE(String.valueOf(0));
                model.setFROM_POLE_LATITUDE(poleLat);
                model.setFROM_POLE_LONGITUDE(poleLon);
            }
        }
        if (sp_is_location_exists.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
            model.setEXISTING_LOCATION_33_NEW(ch_ext_loc_33kv.isChecked() ? "Yes" : "No");
            model.setEXISTING_LOCATION_11_NEW(ch_ext_loc_11kv.isChecked() ? "Yes" : "No");
            model.setEXISTING_LOCATION_LT_NEW(ch_ext_loc_lt.isChecked() ? "Yes" : "No");
        }
        if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
            model.setDTR_STRUCTURE_CODE(dtr_structure_code_11kv.getText().toString().trim());
        }

        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            if (ch_33kv_9_stuart_poles_11kv.isChecked()) {
                model.setNUMB_9M_STRUTS(Integer.parseInt(sp_no_9_poles_11kv.getSelectedItem().toString()));

            }
            if (ch_33kv_8_stuart_poles_11kv.isChecked()) {
                model.setNUMB_8M_STRUTS(Integer.parseInt(sp_no_8_poles_11kv.getSelectedItem().toString()));

            }
        } else if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STAY")) {
            model.setNUMB_STAYS(Integer.parseInt(sp_no_stays_11kv.getSelectedItem().toString()));
        } else if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (ch_33kv_9_stuart_poles_11kv.isChecked()) {
                model.setNUMB_9M_STRUTS(Integer.parseInt(sp_no_9_poles_11kv.getSelectedItem().toString()));
            }
            if (ch_33kv_8_stuart_poles_11kv.isChecked()) {
                model.setNUMB_8M_STRUTS(Integer.parseInt(sp_no_8_poles_11kv.getSelectedItem().toString()));
            }
            model.setNUMB_STAYS(Integer.parseInt(sp_no_stays_11kv.getSelectedItem().toString()));

        } else {
            model.setNUMB_9M_STRUTS(0);
            model.setNUMB_8M_STRUTS(0);
            model.setNUMB_STAYS(0);
        }


        if (!sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("SS Bay")
        ) {
            model.setPOLE_TYPE(sp_pole_type_11.getSelectedItem().toString());
            model.setPOLE_HEIGHT(sp_pole_height_11.getSelectedItem().toString());
            if (!sp_pole_height_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                String poleDesc33kv = sp_sp_pole_desc_11.getSelectedItem() != null ? sp_sp_pole_desc_11.getSelectedItem().toString() : null;

                if (poleDesc33kv != null && !poleDesc33kv.equalsIgnoreCase("Select")) {
                    model.setPOLE_DESC(poleDesc33kv);
                    model.setPOLE_SAP_CODE(poleSapCode11kv);
                }
            }
        } else {

        }


        if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            String poleType = sp_pole_type_11.getSelectedItem().toString();
            String poleHeight = sp_pole_height_11.getSelectedItem().toString();
            String stayClampDesc = sp_main_pole_stay_clamps_11kv.getSelectedItem().toString();

            if (poleType.equalsIgnoreCase("PSCC")) {
                if (poleHeight.equalsIgnoreCase("9.1")) {
                    model.setSTAYCLAMP_9M_CODE(stayCode911KV);
                    model.setSTAYCLAMP_9M_DESC(stayClampDesc);
                    if (!sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString()));

                    }
                  //  System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("11")) {
                    model.setSTAYCLAMP_11M_CODE(stayCode11KV);
                    model.setSTAYCLAMP_11M_DESC(sp_main_pole_stay_clamps_11kv.getSelectedItem().toString());
                    if (!sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_11M(Integer.parseInt(sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString()));

                    }
                  //  System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("8")) {
                    model.setSTAYCLAMP_8M_CODE(stayCode811KV);
                    model.setSTAYCLAMP_8M_DESC(stayClampDesc);
                    if (sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString()));

                    }
                  //  System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                }

            } else if (poleType.equalsIgnoreCase("SPUN")) {
                model.setSTAYCLAMP_SPUN_CODE(stayCode11KV);
                model.setSTAYCLAMP_SPUN_DESC(stayClampDesc);
                if (sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_SPUN(Integer.parseInt(sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString()));

                }
            }

            if (ch_33kv_9_stuart_poles_11kv.isChecked()) {
                model.setSTAYCLAMP_9M_CODE(stayCode11KV);
                model.setSTAYCLAMP_9M_DESC(sp_sturt_type_black_clamps_11kv.getSelectedItem().toString());
                if (!sp_sturt_no_of_black_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_sturt_no_of_black_clamps_11kv.getSelectedItem().toString()));

                }
            }

            if (ch_33kv_8_stuart_poles_11kv.isChecked()) {
                model.setSTAYCLAMP_8M_CODE(stayCode11KV);
                model.setSTAYCLAMP_8M_DESC(sp_sturt_8m_type_black_clamps_11kv.getSelectedItem().toString());

                if (!sp_sturt_8m_no_of_black_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_sturt_8m_no_of_black_clamps_11kv.getSelectedItem().toString()));

                }

            }

        } else if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            String poleType = sp_pole_type_11.getSelectedItem().toString();
            String poleHeight = sp_pole_height_11.getSelectedItem().toString();
            String stayClampDesc = sp_main_pole_stay_clamps_11kv.getSelectedItem().toString();

            if (poleType.equalsIgnoreCase("PSCC")) {
                if (poleHeight.equalsIgnoreCase("9.1")) {
                    model.setSTAYCLAMP_9M_CODE(stayCode911KV);
                    model.setSTAYCLAMP_9M_DESC(stayClampDesc);
                    if (!sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString()));

                    }
                  //  System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("11")) {
                    model.setSTAYCLAMP_11M_CODE(stayCode11KV);
                    model.setSTAYCLAMP_11M_DESC(sp_main_pole_stay_clamps_11kv.getSelectedItem().toString());
                    if (!sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_11M(Integer.parseInt(sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString()));

                    } else {
                        model.setCOUNT__STAYCLAMP_11M(0);
                    }
                   // System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("8")) {
                    model.setSTAYCLAMP_8M_CODE(stayCode811KV);
                    model.setSTAYCLAMP_8M_DESC(stayClampDesc);
                    if (sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString()));

                    } else {
                        model.setCOUNT__STAYCLAMP_8M(0);
                    }
                   // System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                }

            } else if (poleType.equalsIgnoreCase("SPUN")) {
                model.setSTAYCLAMP_SPUN_CODE(stayCode11KV);
                model.setSTAYCLAMP_SPUN_DESC(stayClampDesc);
                if (sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_SPUN(Integer.parseInt(sp_no_main_pole_stay_clamps_11kv.getSelectedItem().toString()));

                } else {
                    model.setCOUNT__STAYCLAMP_SPUN(0);
                }
            }

            if (ch_33kv_9_stuart_poles_11kv.isChecked()) {
                model.setSTAYCLAMP_9M_CODE(stayCode11KV);
                model.setSTAYCLAMP_9M_DESC(sp_sturt_type_black_clamps_11kv.getSelectedItem().toString());
                if (!sp_sturt_no_of_black_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_sturt_no_of_black_clamps_11kv.getSelectedItem().toString()));

                } else {
                    model.setCOUNT__STAYCLAMP_9M(0);
                }
            }

            if (ch_33kv_8_stuart_poles_11kv.isChecked()) {
                model.setSTAYCLAMP_8M_CODE(stayCode11KV);
                model.setSTAYCLAMP_8M_DESC(sp_sturt_8m_type_black_clamps_11kv.getSelectedItem().toString());

                if (!sp_sturt_8m_no_of_black_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_sturt_8m_no_of_black_clamps_11kv.getSelectedItem().toString()));
                } else {
                    model.setCOUNT__STAYCLAMP_8M(0);
                }

            }

        }


        if (ch_back_clamps_11kv.isChecked()) {
            if (sp_pole_height_11 != null && sp_type_black_clamps_11kv.getSelectedItem() != null) {
                model.setBACK_CLAMPS_DESC(sp_type_black_clamps_11kv.getSelectedItem().toString());
                model.setBACK_CLAMPS_CODE(clamp11kvCode);
                if (!sp_no_of_black_clamps_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT_BACK_CLAMPS(Integer.parseInt(sp_no_of_black_clamps_11kv.getSelectedItem().toString()));
                }
            } else {
                model.setBACK_CLAMPS_DESC("");
                model.setBACK_CLAMPS_CODE("");
                model.setCOUNT_BACK_CLAMPS(0);
            }
        } else {
            model.setBACK_CLAMPS_DESC("");
            model.setBACK_CLAMPS_CODE("");
            model.setCOUNT_BACK_CLAMPS(0);
        }


//        if (sp_pole_height_11 != null && sp_type_black_clamps_11kv.getSelectedItem() != null) {
//            model.setBACK_CLAMPS_DESC(sp_type_black_clamps_11kv.getSelectedItem().toString());
//            model.setBACK_CLAMPS_CODE(clamp11kvCode);
//            String selectedData11 = sp_no_of_black_clamps_11kv.getSelectedItem().toString();
//            if (!selectedData11.equalsIgnoreCase("Select")) {
//                model.setCOUNT_BACK_CLAMPS(Integer.parseInt(sp_no_of_black_clamps_11kv.getSelectedItem().toString()));
//            }
//        } else {
//            model.setBACK_CLAMPS_DESC("");
//            model.setBACK_CLAMPS_CODE("");
//            model.setCOUNT_BACK_CLAMPS(0);
//        }

        if (ch_tapping_cross_arms_11kv.isChecked()) {
            if (!sp_no_of_tapping_cross_arms_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_TAPPING_XARM(Integer.parseInt(sp_no_of_tapping_cross_arms_11kv.getSelectedItem().toString()));
                model.setTAPPINGXARM_DESC(sp_tapping_x_arm_11kv.getSelectedItem().toString());
                model.setTAPPINGXARM_CODE(tappingCrossCode11KV);

            }
        } else {
            model.setCOUNT_TAPPING_XARM(0);
            model.setTAPPINGXARM_DESC(null);
            model.setTAPPINGXARM_CODE(null);
        }
        if (ch_no_of_v_crossing_11kv.isChecked()) {
            if (!sp_no_of_v_crossing_arms_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_V_XARM(Integer.parseInt(sp_no_of_v_crossing_arms_11kv.getSelectedItem().toString()));
                model.setVXARM_DESC(sp_type_of_v_crossing_arms_11kv.getSelectedItem().toString());
                model.setVXARM_CODE(vXArmCode11KV);

                model.setCOUNT_TOP_CLEAT(1);
                model.setTOPCLEAT_DESC(sp_type_top_cleat_11kv.getSelectedItem().toString());
                model.setTOPCLEAT_CODE(topCleatCode11KV);

            }
        } else {
            model.setCOUNT_V_XARM(0);
            model.setVXARM_DESC(null);
            model.setVXARM_CODE(null);

            model.setCOUNT_TOP_CLEAT(0);
            model.setTOPCLEAT_DESC(null);
            model.setTOPCLEAT_CODE(null);
        }

        if (ch_top_cleat.isChecked()) {
            model.setCOUNT_TOP_CLEAT(1);
            model.setTOPCLEAT_DESC(sp_type_top_cleat_11kv.getSelectedItem().toString());
            model.setTOPCLEAT_CODE(topCleatCode11KV);
        } else {
            model.setCOUNT_TOP_CLEAT(0);
            model.setTOPCLEAT_DESC("");
            model.setTOPCLEAT_CODE("");
        }


        if (ch_pin_insulators_11kv.isChecked()) {
            if (!sp_no_of_pin_insulators_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_PIN_INS(Integer.parseInt(sp_no_of_pin_insulators_11kv.getSelectedItem().toString()));
                model.setPIN_INS_DESC(sp_pin_insulators_type_11kv.getSelectedItem().toString());
                model.setPIN_INS_CODE(pinInsulatorCode11kv);

            }
        } else {
            model.setCOUNT_PIN_INS(0);
            model.setPIN_INS_DESC(null);
            model.setPIN_INS_CODE(null);
        }

        if (ch_disc_insulators_11kv.isChecked()) {
            if (!sp_no_of_disc_insulators_11kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_DISC_INS(Integer.parseInt(sp_no_of_disc_insulators_11kv.getSelectedItem().toString()));
                model.setDISC_INS_DESC(sp_type_disc_insulators_11kv.getSelectedItem().toString());
                model.setDISC_INS_CODE(discInsulator11kv);

            }
        } else {
            model.setCOUNT_DISC_INS(0);
            model.setDISC_INS_DESC(null);
            model.setDISC_INS_CODE(null);
        }

        if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            model.setMETAL_PARTS_CODE(metalPartCode11kv);
            model.setMETAL_PARTS_DESC(sp_type_metal_11kv.getSelectedItem().toString());
            String metalPartsText = tv_no_of_metal_parts_11kv.getText() != null ? tv_no_of_metal_parts_11kv.getText().toString() : "";
            model.setCOUNT_METALS_PARTS(!metalPartsText.isEmpty() ? Integer.parseInt(metalPartsText) : 0);
        } else {
            model.setMETAL_PARTS_CODE("");
            model.setMETAL_PARTS_DESC("");
            model.setCOUNT_METAL_PARTS(0);
        }


        if (ch_side_arm_11kv.isChecked()) {
            String selectSideArm = sp_no_of_side_arm_11kv.getSelectedItem().toString();
            if (selectSideArm.equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please Select Side Arm", Toast.LENGTH_SHORT).show();
            } else {
                model.setCOUNT_SIDE_ARM(Integer.parseInt(sp_no_of_side_arm_11kv.getSelectedItem().toString()));
            }

            model.setSIDEARM_DESC(sp_side_arm_11kv.getSelectedItem().toString());
            model.setSIDEARM_CODE(sideArm11KVCode);
        } else {
            model.setCOUNT_SIDE_ARM(0);
            model.setSIDEARM_DESC(null);
            model.setSIDEARM_CODE(null);
        }


        if (!sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            String conductor11KV = sp_conductor_size_11.getSelectedItem() != null ? sp_conductor_size_11.getSelectedItem().toString() : null;
            if (conductor11KV != null && !conductor11KV.equalsIgnoreCase("Select")) {
                model.setCONDUCTOR_DESC(conductor11KV);
                model.setCONDUCTOR_SAP_CODE(conductorSAPCode11KV);
            }
        }

        if (!sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("AB-Pole")) {
                String abSwitch11KV = sp_pole_ab_switch_type_11kv.getSelectedItem() != null ? sp_pole_ab_switch_type_11kv.getSelectedItem().toString() : null;
                if (abSwitch11KV != null && !abSwitch11KV.equalsIgnoreCase("Select")) {
                    model.setAB_DESC(abSwitch11KV);
                    model.setAB_SWITCH(abSwitch);
                }
            }

        }

        if (!sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("DTR Pole")) {
                String abSwitch11KV = sp_pole_dtr_ab_switch_type_11kv.getSelectedItem() != null ? sp_pole_dtr_ab_switch_type_11kv.getSelectedItem().toString() : null;
                if (abSwitch11KV != null && !abSwitch11KV.equalsIgnoreCase("Select")) {
                    model.setDTR_AB_DESC(abSwitch11KV);
                    model.setDTR_AB_CODE(dtrAbSwitch);
                }
                String hgFuseSet = sp_hg_fuse_set_11kv.getSelectedItem() != null ? sp_hg_fuse_set_11kv.getSelectedItem().toString() : null;
                if (abSwitch11KV != null && !abSwitch11KV.equalsIgnoreCase("Select")) {

                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                        model.setHG_FUSE_DESC(hgFuseSet);
                        model.setHG_FUSE_CODE("HGF10002");
                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("6.3KV")) {
                        model.setHG_FUSE_DESC(hgFuseSet);
                        model.setHG_FUSE_CODE("HGF10003");
                    }
                }
                if (sp_lt_fuse_distribution_11kv.getSelectedItem().toString().equalsIgnoreCase("Distribution Box")) {
                    String distributionBox = sp_lt_fuse_box_distribution_11kv.getSelectedItem().toString();
                    model.setLT_FUSE_DESC(distributionBox);
                    model.setLT_FUSE_CODE(boxDistribution);

                }

                if (ch_gi_pipe.isChecked()) {
                    model.setGI_DESC("GI EARTH FLAT 25 MM X 4 MM");
                    model.setGI_SAP_CODE("EMT00018");
                    model.setCOUNT_GI(Integer.parseInt(sp_gi_pipes.getSelectedItem().toString()));
                }
                if (ch_ci_pipe.isChecked()) {
                    model.setCI_DESC("CI EARTH PIPE 50MM DIA");
                    model.setCI_SAP_CODE("EMT00003");
                    String selectData = sp_ci_pipes.getSelectedItem().toString();
                    if (!selectData.equalsIgnoreCase("Select")) {
                        model.setCOUNT_CI(Integer.parseInt(sp_ci_pipes.getSelectedItem().toString()));

                    }
                }


            }

        }

        if (!sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            String conductor11KV = sp_dtr_phase_desc_11.getSelectedItem() != null ? sp_dtr_phase_desc_11.getSelectedItem().toString() : null;
            if (conductor11KV != null && !conductor11KV.equalsIgnoreCase("Select")) {
                model.setDTR_DESC(conductor11KV);
                model.setDTR_SAP_CODE(dtrSapCode);
            }
        } else if (!sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            String conductor11KV = sp_dtr_phase_desc_11.getSelectedItem() != null ? sp_dtr_phase_desc_11.getSelectedItem().toString() : null;
            if (conductor11KV != null && !conductor11KV.equalsIgnoreCase("Select")) {
                model.setDTR_DESC(conductor11KV);
                model.setDTR_SAP_CODE(dtrSapCode);
            }
        }


        model.setPOLE_LOCATION_TYPE(sp_survey_pole_type.getSelectedItem().toString());
        model.setCONDUCTOR_SIZE(sp_conductor_11.getSelectedItem().toString());
        model.setIS_EXISTING_LOCATION_NEW(sp_is_location_exists.getSelectedItem().toString());


        if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("DTR Pole")) {
            if (!sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setDTR_PHASE(sp_dtr_phase_11.getSelectedItem().toString());
            }

            if (!sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                    model.setDTR_SURVEY_TYPE("ENHANCED");
                } else {
                    model.setDTR_SURVEY_TYPE(sp_dtr_survey_11.getSelectedItem().toString());

                }
            }
            if (!sp_dtr_mounting_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setDTR_MOUNTING(sp_dtr_mounting_11.getSelectedItem().toString());
            }

        }
        if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("I")) {
            model.setDTR_CAPACITY(sp_dtr_capacity_phase1_11.getSelectedItem().toString());

        } else if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("III")) {
            model.setDTR_CAPACITY(sp_dtr_capacity_phase2_11.getSelectedItem().toString());
        }


        //model.setLINE_TYPE(sp_lineType_11.getSelectedItem().toString());
        model.setIS_MULTI_CKT(sp_is_multi_circuit_11.getSelectedItem().toString());
        if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
            model.setMULTI_CKT_33(ch_multi_33kv_11.isChecked() ? "Yes" : "No");
            model.setMULTI_CKT_11(ch_multi_11kv_11.isChecked() ? "Yes" : "No");
            model.setMULTI_CKT_LT(ch_multi_lt_11.isChecked() ? "Yes" : "No");
            if (ch_multi_33kv_11.isChecked()) {
                model.setMULTI_CKT_33_POLE_NO(sp_33kv_multi_ckt_pole_11.getSelectedItem().toString());
            }
            if (ch_multi_11kv_11.isChecked()) {
                model.setMULTI_CKT_11_POLE_NO(sp_11kv_multi_ckt_pole_11.getSelectedItem().toString());
            }
        }

        model.setPOLE_SUPPORT(sp_pole_support_11.getSelectedItem().toString());
        model.setPOLE_CONFIG(sp_pole_configuration_11.getSelectedItem().toString());


        if (list11.isEmpty() && sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("No")) {
            model.setTAPPING_LOCATION(tapping_location.getText().toString());
            model.setFROM_POLE(String.valueOf(0));
            model.setFROM_POLE_LATITUDE(poleLat);
            model.setFROM_POLE_LONGITUDE(poleLon);
        } else if (list11.isEmpty() && sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
            model.setFROM_POLE(sp_parent_connecting_point.getSelectedItem().toString());
            ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_parent_connecting_point.getSelectedItem().toString(),GIS_ID);
            model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
            model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());
        } else {
            model.setFROM_POLE(sp_connecting_point.getSelectedItem().toString());
            ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_connecting_point.getSelectedItem().toString(),GIS_ID);
            model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
            model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());
        }

        model.setNEW_DATA_ADDED("Yes");
        model.setPIN_CUT(sp_pole_pin_cut_point_11.getSelectedItem().toString());
        if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            model.setEND_POLE(sp_ded_end_pole_11kv.getSelectedItem().toString());
        } else {
            model.setEND_POLE("");

        }


        boolean status = databaseEstimates.addNewSurveyData(model);
        if (status) {
            if (allList.isEmpty()) {
                EstimatesMap.btn_complete_survey.setVisibility(View.VISIBLE);
            }

            if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                if (ch_multi_33kv_11.isChecked()) {
                    databaseEstimates.updateMultiCircuit33(EST_NO, String.valueOf(poleNumber), sp_33kv_multi_ckt_pole_11.getSelectedItem().toString(),GIS_ID);
                }
                if (ch_multi_11kv_11.isChecked()) {
                    databaseEstimates.updateMultiCircuit11(EST_NO, String.valueOf(poleNumber), sp_11kv_multi_ckt_pole_11.getSelectedItem().toString(),GIS_ID);
                }
            }

            Toast.makeText(context, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
            dismiss();
            callout.dismiss();
            graphicsOverlay.getGraphics().remove(graphic);
        } else {
            Toast.makeText(context, "Failed to capture Pole location.", Toast.LENGTH_SHORT).show();
        }

    }

    private void validate33KVSurvey() {
//        if (sp_is_multi_circuit_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select is multi circuit", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (sp_is_multi_circuit_33.getSelectedItem().toString().equalsIgnoreCase("No")
                && !ch_multi_33kv_33.isChecked() && !ch_multi_11kv_33.isChecked() && !ch_multi_lt_33.isChecked()) {
            Toast.makeText(context, "Please Select other multi circuits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_multi_circuit_33.getSelectedItem().toString().equalsIgnoreCase("No")
                && ch_multi_33kv_33.isChecked() && sp_33kv_multi_ckt_pole_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select 33kv multi circuit pole number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pin/Cut Type", Toast.LENGTH_SHORT).show();
            return;

        }


       // System.out.println("Pole_Configuration====" + sp_pole_configuration_33.getSelectedItem().toString());
        if (sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("33/11kv SS-Bay") ||
                sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("EHT SS-Bay")) {

        } else {
            List<MaterialMasterModel> model = databaseEstimates.getSpecificClamps(sp_pole_height_33.getSelectedItem().toString());


            if (!model.isEmpty()) {
                if (sp_type_black_clamps_33kv != null && sp_type_black_clamps_33kv.getSelectedItem() != null &&
                        !sp_type_black_clamps_33kv.getSelectedItem().toString().isEmpty()) {
                    if (sp_no_of_black_clamps_33kv == null || sp_no_of_black_clamps_33kv.getSelectedItem() == null ||
                            sp_no_of_black_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please Select Number of back clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }

        if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT")
                || sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (ch_33kv_9_stuart_poles_33kv.isChecked()) {
                if (!sp_sturt_type_black_clamps_33kv.getSelectedItem().toString().isEmpty()) {
                    if (sp_sturt_no_of_black_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please select number of 9.1 Stay clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
            if (ch_33kv_8_stuart_poles_33kv.isChecked()) {
                if (!sp_sturt_8m_type_black_clamps_33kv.getSelectedItem().toString().isEmpty()) {
                    if (sp_sturt_8m_no_of_black_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(context, "Please select number of 8 Stay clamps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }


           // System.out.println("Pole_Configuration====" + sp_pole_configuration_33.getSelectedItem().toString());
            if (sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("33/11kv SS-Bay") ||
                    sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("EHT SS-Bay")) {

            } else {
                List<MaterialMasterModel> model = databaseEstimates.getSpecificSturtClamps(sp_pole_height_33.getSelectedItem().toString());


                if (!model.isEmpty()) {
                    if (sp_main_pole_stay_clamps_33kv != null && sp_main_pole_stay_clamps_33kv.getSelectedItem() != null &&
                            !sp_main_pole_stay_clamps_33kv.getSelectedItem().toString().isEmpty()) {
                        if (sp_no_main_pole_stay_clamps_33kv == null || sp_no_main_pole_stay_clamps_33kv.getSelectedItem() == null ||
                                sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(context, "Please Select Number of back clamps", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }

//            if (!sp_main_pole_stay_clamps_33kv.getSelectedItem().toString().isEmpty()) {
//                if (sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//                    Toast.makeText(context, "Please select number Stay clamps", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }

        }
        if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            if (sp_ded_end_pole_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please select is end pole", Toast.LENGTH_SHORT).show();
                return;
            }
        }


//        if (sp_no_of_tapping_cross_arms_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select Numbers Of Tapping Cross Arms", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        if (sp_no_of_v_crossing_arms_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select Numbers Of V Crossing Arms", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (sp_no_of_top_cleat_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select Numbers Of Top Cleat ", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (sp_no_of_pin_insulators_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select Numbers Of Pin Insulators", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (sp_no_of_disc_insulators_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select Numbers Of Disc Insulators", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (sp_no_of_metal_insulators_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select Numbers Of Metal Parts", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (sp_no_of_count_side_arm_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
//            Toast.makeText(context, "Please Select Numbers Of Side Arms", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (!sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("33/11kv SS-Bay") &&
                !sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("EHT SS-Bay")) {
            if (sp_pole_type_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please Select Pole type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sp_pole_height_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please Select Pole Height", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        if (sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pole Configuration", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Conductor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Pole Support", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Please Select Line Survey Type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            if (!ch_33kv_9_stuart_poles_33kv.isChecked() && !ch_33kv_8_stuart_poles_33kv.isChecked()) {
                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (!ch_33kv_9_stuart_poles_33kv.isChecked() && !ch_33kv_8_stuart_poles_33kv.isChecked()) {
                Toast.makeText(context, "Please Select At-least one", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (distance > distanceInFloat) {
            showDistanceWarningDialog(distance);
            return;
        }


        addPoleToDataBase33();
    }

    private void addPoleToDataBase33() {
        NewModel model = new NewModel();
        String data = databaseEstimates.getFeederDetails33KV(EST_NO, "33KV",GIS_ID);

        if (data != null && !data.isEmpty()) {
            Log.d("DATATATATATA", "onCreate: " + data);

            String[] parts = data.split(",");
            if (parts.length >= 2 && parts[0].contains(":") && parts[1].contains(":")) {
                // Safely extract feederId and feederName
                String feederIdPart = parts[0].split(":")[1].trim();
                String feederNamePart = parts[1].split(":")[1].trim();

                // Set up the model with extracted data

                model.setEST_NO(EST_NO);
                model.setVOLTAGE_LEVEL("33KV");
                model.setIS_NEW_SURVEY("Y");
                model.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
                model.setFEEDER_ID(feederIdPart);
                model.setFEEDER_NAME(feederNamePart);

                // Further processing with model as needed...

            } else {
               // Log.e("DataFormatError", "Data format is incorrect. Expected format: 'FeederId: VALUE, FeederName: VALUE'");
            }
        } else {
           // Log.e("DataError", "Data is null or empty.");
        }

//        String data = databaseEstimates.getFeederDetails33KV(EST_NO, "33KV");
//
//        String[] parts = data.split(",");
//        String feederIdPart = parts[0].split(":")[1].trim();
//        String feederNamePart = parts[1].split(":")[1].trim();
//
//        Log.d("DATATATATATA", "onCreate: " + data);
//        NewModel model = new NewModel();
//        model.setEST_NO(EST_NO);
//        model.setVOLTAGE_LEVEL("33KV");
//        model.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
//
//        model.setFEEDER_ID(feederIdPart);
//        model.setFEEDER_NAME(feederNamePart);

        model.setIS_EXISTING_LOCATION_NEW(sp_is_location_exists.getSelectedItem().toString());
        if (sp_is_location_exists.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
            model.setEXISTING_LOCATION_33_NEW(ch_ext_loc_33kv.isChecked() ? "Yes" : "No");
            model.setEXISTING_LOCATION_11_NEW(ch_ext_loc_11kv.isChecked() ? "Yes" : "No");
            model.setEXISTING_LOCATION_LT_NEW(ch_ext_loc_lt.isChecked() ? "Yes" : "No");
        }
        model.setLINE_TYPE("OverHead");
        if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            if (ch_33kv_9_stuart_poles_33kv.isChecked()) {
                model.setNUMB_9M_STRUTS(Integer.parseInt(sp_no_9_poles_33kv.getSelectedItem().toString()));

            }
            if (ch_33kv_8_stuart_poles_33kv.isChecked()) {
                model.setNUMB_8M_STRUTS(Integer.parseInt(sp_no_8_poles_33kv.getSelectedItem().toString()));

            }
        } else if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STAY")) {
            model.setNUMB_STAYS(Integer.parseInt(sp_no_of_stays_33kv.getSelectedItem().toString()));
        } else if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            if (ch_33kv_9_stuart_poles_33kv.isChecked()) {
                model.setNUMB_9M_STRUTS(Integer.parseInt(sp_no_9_poles_33kv.getSelectedItem().toString()));
            }
            if (ch_33kv_8_stuart_poles_33kv.isChecked()) {
                model.setNUMB_8M_STRUTS(Integer.parseInt(sp_no_8_poles_33kv.getSelectedItem().toString()));
            }
            model.setNUMB_STAYS(Integer.parseInt(sp_no_of_stays_33kv.getSelectedItem().toString()));

        } else {
            model.setNUMB_9M_STRUTS(0);
            model.setNUMB_8M_STRUTS(0);
            model.setNUMB_STAYS(0);
        }


        model.setCONDUCTOR_SIZE(sp_conductor_33.getSelectedItem().toString());

        if (!sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            String conductor33KV = sp_conductor_size_33.getSelectedItem() != null ? sp_conductor_size_33.getSelectedItem().toString() : null;
            if (conductor33KV != null && !conductor33KV.equalsIgnoreCase("Select")) {
                model.setCONDUCTOR_DESC(conductor33KV);
                model.setCONDUCTOR_SAP_CODE(conductorSAPCode33KV);
            }
        }

        if (!sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            if (sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("AB-Pole")) {
                String abSwitch33KV = sp_pole_ab_switch_type_33kv.getSelectedItem() != null ? sp_pole_ab_switch_type_33kv.getSelectedItem().toString() : null;
                if (abSwitch33KV != null && !abSwitch33KV.equalsIgnoreCase("Select")) {
                    model.setAB_DESC(abSwitch33KV);
                    model.setAB_SWITCH(abSwitch);
                }
            }

        }


        if (sp_survey_pole_type.getSelectedItem().toString().equalsIgnoreCase("Mapped Location")) {
            model.setPOLE_NO(sp_exsting_connecting_point.getSelectedItem().toString());
            ArrayList<NewModel> latAndLonPole = databaseEstimates.getLatAndLon(EST_NO, sp_exsting_connecting_point.getSelectedItem().toString(),GIS_ID);
            model.setLATITUDE(lattitude);
            model.setLONGITUDE(longitude);
            model.setLOCATION_ID(latAndLonPole.get(0).getLOCATION_ID());
            // Log.d("LOCATION_ID_FOR_NEW_LOCATION_INSIDE", "addPoleToDataBase33: " + latAndLonPole.get(0).getLOCATION_ID());
            model.setFROM_POLE(sp_connecting_point.getSelectedItem().toString());
            ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_connecting_point.getSelectedItem().toString(),GIS_ID);
            model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
            model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());
            model.setEXI_POLE_NO(sp_exsting_connecting_point.getSelectedItem().toString());
        } else {
            model.setPOLE_NO(String.valueOf(poleNumber));
            model.setLOCATION_ID(Long.toString(randomNumber));
            //Log.d("LOCATION_ID_FOR_NEW_LOCATION", "addPoleToDataBase33: " + randomNumber);
            model.setLONGITUDE(poleLon);
            model.setLATITUDE(poleLat);
            if (poleNumber == 1) {
                model.setFROM_POLE(String.valueOf(0));
                model.setFROM_POLE_LATITUDE(poleLat);
                model.setFROM_POLE_LONGITUDE(poleLon);
            } else if (list33.isEmpty()) {
                model.setFROM_POLE(String.valueOf(0));
                model.setFROM_POLE_LATITUDE(poleLat);
                model.setFROM_POLE_LONGITUDE(poleLon);
            } else {
                model.setFROM_POLE(sp_connecting_point.getSelectedItem().toString());
                ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_connecting_point.getSelectedItem().toString(),GIS_ID);
                model.setFROM_POLE_LATITUDE(latAndLon.get(0).getLATITUDE());
                model.setFROM_POLE_LONGITUDE(latAndLon.get(0).getLONGITUDE());
            }
        }
        if (!sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("33/11kv SS-Bay") && !sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("EHT SS-Bay")) {
            model.setPOLE_TYPE(sp_pole_type_33.getSelectedItem().toString());
            model.setPOLE_HEIGHT(sp_pole_height_33.getSelectedItem().toString());
            if (!sp_pole_height_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                String poleDesc33kv = sp_sp_pole_desc_33.getSelectedItem() != null ? sp_sp_pole_desc_33.getSelectedItem().toString() : null;

                if (poleDesc33kv != null && !poleDesc33kv.equalsIgnoreCase("Select")) {
                    model.setPOLE_DESC(poleDesc33kv);
                    model.setPOLE_SAP_CODE(poleSapCode33kv);
                }
            }
        } else {

        }

        Log.d("CHECK_BOX_STATUS", "addPoleToDataBase33: " + ch_side_arm_33kv.isChecked());
        String selectedItem = sp_no_of_black_clamps_33kv.getSelectedItem().toString();


        if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
            String poleType = sp_pole_type_33.getSelectedItem().toString();
            String poleHeight = sp_pole_height_33.getSelectedItem().toString();
            String stayClampDesc = sp_main_pole_stay_clamps_33kv.getSelectedItem().toString();

            if (poleType.equalsIgnoreCase("PSCC")) {
                if (poleHeight.equalsIgnoreCase("9.1")) {
                    model.setSTAYCLAMP_9M_CODE(stayCode33KV);
                    model.setSTAYCLAMP_9M_DESC(stayClampDesc);
                    if (!sp_main_pole_stay_clamps_33kv.getSelectedItem().equals("Select")) {
                        model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString()));

                    } else {
                        model.setCOUNT__STAYCLAMP_11M(0);
                    }
                  //  System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("11")) {
                    model.setSTAYCLAMP_11M_CODE(stayCode33KV);
                    model.setSTAYCLAMP_11M_DESC(stayClampDesc);
                    if (!sp_main_pole_stay_clamps_33kv.getSelectedItem().equals("Select")) {
                        model.setCOUNT__STAYCLAMP_11M(Integer.parseInt(sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString()));

                    } else {
                        model.setCOUNT__STAYCLAMP_11M(0);
                    }
                   // System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                }

            } else if (poleType.equalsIgnoreCase("SPUN")) {
                model.setSTAYCLAMP_SPUN_CODE(stayCode33KV);
                model.setSTAYCLAMP_SPUN_DESC(stayClampDesc);
                if (!sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_SPUN(Integer.parseInt(sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString()));

                } else {
                    model.setCOUNT__STAYCLAMP_SPUN(0);
                }

            }

            if (ch_33kv_9_stuart_poles_33kv.isChecked()) {
                model.setSTAYCLAMP_9M_CODE(stayCode933KV);
                model.setSTAYCLAMP_9M_DESC(sp_sturt_type_black_clamps_33kv.getSelectedItem().toString());

                model.setCOUNT__STAYCLAMP_9M(!sp_no_9_poles_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")
                        ? Integer.parseInt(sp_no_9_poles_33kv.getSelectedItem().toString())
                        : 0);

            }

            if (ch_33kv_8_stuart_poles_33kv.isChecked()) {
                model.setSTAYCLAMP_8M_CODE(stayCode833KV);
                model.setSTAYCLAMP_8M_DESC(sp_sturt_8m_type_black_clamps_33kv.getSelectedItem().toString());
                if (!sp_sturt_8m_no_of_black_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_sturt_8m_no_of_black_clamps_33kv.getSelectedItem().toString()));
                }
            }

        } else if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
            String poleType = sp_pole_type_33.getSelectedItem().toString();
            String poleHeight = sp_pole_height_33.getSelectedItem().toString();
            String stayClampDesc = sp_main_pole_stay_clamps_33kv.getSelectedItem().toString();

            if (poleType.equalsIgnoreCase("PSCC")) {
                if (poleHeight.equalsIgnoreCase("9.1")) {
                    model.setSTAYCLAMP_9M_CODE(stayCode33KV);
                    model.setSTAYCLAMP_9M_DESC(stayClampDesc);
                    model.setCOUNT__STAYCLAMP_9M(Integer.parseInt(sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString()));
                   // System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                } else if (poleHeight.equalsIgnoreCase("11")) {
                    model.setSTAYCLAMP_11M_CODE(stayCode33KV);
                    model.setSTAYCLAMP_11M_DESC(stayClampDesc);
                    model.setCOUNT__STAYCLAMP_11M(
                            !sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")
                                    ? Integer.parseInt(sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString())
                                    : 0
                    );
                    model.setCOUNT__STAYCLAMP_11M(Integer.parseInt(sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString()));
                   // System.out.println("SETTING STAYCLAMP_9M_DESC" + stayClampDesc);
                }

            } else if (poleType.equalsIgnoreCase("SPUN")) {
                model.setSTAYCLAMP_SPUN_CODE(stayCode33KV);
                model.setSTAYCLAMP_SPUN_DESC(stayClampDesc);
                if (!sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_SPUN(Integer.parseInt(sp_no_main_pole_stay_clamps_33kv.getSelectedItem().toString()));

                }

            }

            if (ch_33kv_9_stuart_poles_33kv.isChecked()) {
                model.setSTAYCLAMP_9M_CODE(stayCode933KV);
                model.setSTAYCLAMP_9M_DESC(sp_sturt_type_black_clamps_33kv.getSelectedItem().toString());

                model.setCOUNT__STAYCLAMP_9M(!sp_no_9_poles_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")
                        ? Integer.parseInt(sp_no_9_poles_33kv.getSelectedItem().toString())
                        : 0);

            }

            if (ch_33kv_8_stuart_poles_33kv.isChecked()) {
                model.setSTAYCLAMP_8M_CODE(stayCode833KV);
                model.setSTAYCLAMP_8M_DESC(sp_sturt_8m_type_black_clamps_33kv.getSelectedItem().toString());
                if (!sp_sturt_8m_no_of_black_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT__STAYCLAMP_8M(Integer.parseInt(sp_sturt_8m_no_of_black_clamps_33kv.getSelectedItem().toString()));
                }
            }

        }

        if (ch_back_clamps_33kv.isChecked()) {
            if (sp_pole_height_33 != null && sp_type_black_clamps_33kv.getSelectedItem() != null) {
                model.setBACK_CLAMPS_DESC(sp_type_black_clamps_33kv.getSelectedItem().toString());
                model.setBACK_CLAMPS_CODE(clamp33kvCode);
                if (!sp_no_of_black_clamps_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    model.setCOUNT_BACK_CLAMPS(Integer.parseInt(sp_no_of_black_clamps_33kv.getSelectedItem().toString()));
                }

            } else {
                model.setBACK_CLAMPS_DESC("");
                model.setBACK_CLAMPS_CODE("");
                model.setCOUNT_BACK_CLAMPS(0);
            }
        } else {
            model.setBACK_CLAMPS_DESC("");
            model.setBACK_CLAMPS_CODE("");
            model.setCOUNT_BACK_CLAMPS(0);
        }


        if (ch_side_arm_33kv.isChecked()) {
            model.setCOUNT_SIDE_ARM(1);
            model.setSIDEARM_DESC(sp_side_arm_33kv.getSelectedItem().toString());
            model.setSIDEARM_CODE(sideArm33KVCode);
        } else {
            model.setCOUNT_SIDE_ARM(0);
            model.setSIDEARM_DESC(null);
            model.setSIDEARM_CODE(null);
        }


        if (ch_tapping_cross_arms_33kv.isChecked()) {
            if (!sp_no_of_tapping_cross_arms_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_TAPPING_XARM(Integer.parseInt(sp_no_of_tapping_cross_arms_33kv.getSelectedItem().toString()));
                model.setTAPPINGXARM_DESC(sp_tapping_x_arm_33kv.getSelectedItem().toString());
                model.setTAPPINGXARM_CODE(tappingCrossCode33KV);

            }
        } else {
            model.setCOUNT_TAPPING_XARM(0);
            model.setTAPPINGXARM_DESC(null);
            model.setTAPPINGXARM_CODE(null);
        }
        if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            model.setMETAL_PARTS_CODE(metalPartCode33kv);
            if (sp_type_metal_33kv.getSelectedItem() != null) {
                model.setMETAL_PARTS_DESC(sp_type_metal_33kv.getSelectedItem().toString());

            }
            model.setCOUNT_METALS_PARTS(Integer.parseInt(sp_no_of_disc_insulators_33kv.getSelectedItem().toString()));

        } else {
            model.setMETAL_PARTS_CODE("");
            model.setMETAL_PARTS_DESC("");
            model.setCOUNT_METAL_PARTS(0);
        }
        if (ch_no_of_v_crossing_33kv.isChecked()) {
            if (!sp_no_of_v_crossing_arms_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_V_XARM(Integer.parseInt(sp_no_of_v_crossing_arms_33kv.getSelectedItem().toString()));
                model.setVXARM_DESC(sp_type_of_v_crossing_arms_33kv.getSelectedItem().toString());
                model.setVXARM_CODE(vXArmCode33KV);

                model.setCOUNT_TOP_CLEAT(1);
                model.setTOPCLEAT_DESC(sp_type_top_cleat_33kv.getSelectedItem().toString());
                model.setTOPCLEAT_CODE(topCleatCode33KV);

            }
        } else {
            model.setCOUNT_V_XARM(0);
            model.setVXARM_DESC(null);
            model.setVXARM_CODE(null);

            model.setCOUNT_TOP_CLEAT(0);
            model.setTOPCLEAT_DESC(null);
            model.setTOPCLEAT_CODE(null);
        }
        if (ch_top_cleat_33kv.isChecked()) {
            model.setCOUNT_TOP_CLEAT(1);
            model.setTOPCLEAT_DESC(sp_type_top_cleat_33kv.getSelectedItem().toString());
            model.setTOPCLEAT_CODE(topCleatCode33KV);
        } else {
            model.setCOUNT_TOP_CLEAT(0);
            model.setTOPCLEAT_DESC("");
            model.setTOPCLEAT_CODE("");
        }


        if (ch_pin_insulators_33kv.isChecked()) {
            if (!sp_no_of_pin_insulators_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_PIN_INS(Integer.parseInt(sp_no_of_pin_insulators_33kv.getSelectedItem().toString()));
                model.setPIN_INS_DESC(sp_pin_insulators_type_33kv.getSelectedItem().toString());
                model.setPIN_INS_CODE(pinInsulatorCode33kv);

            }
        } else {
            model.setCOUNT_PIN_INS(0);
            model.setPIN_INS_DESC(null);
            model.setPIN_INS_CODE(null);
        }

        if (ch_disc_insulators_33kv.isChecked()) {
            if (!sp_no_of_disc_insulators_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_DISC_INS(Integer.parseInt(sp_no_of_disc_insulators_33kv.getSelectedItem().toString()));
                model.setDISC_INS_DESC(sp_type_disc_insulators_33kv.getSelectedItem().toString());
                model.setDISC_INS_CODE(discInsulator33kv);

            }
        } else {
            model.setCOUNT_DISC_INS(0);
            model.setDISC_INS_DESC(null);
            model.setDISC_INS_CODE(null);
        }


        if (ch_channel_33kv.isChecked()) {
            if (!sp_channel_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                model.setCOUNT_CHANNEL(Integer.parseInt(sp_no_of_channel_33kv.getSelectedItem().toString()));
                model.setCHANNEL_DESC(sp_channel_33kv.getSelectedItem().toString());
                model.setCHANNEL_CODE(channel33KV);

            }
        }


        model.setPOLE_LOCATION_TYPE(sp_survey_pole_type.getSelectedItem().toString());


        //  model.setLINE_TYPE(sp_lineType_33.getSelectedItem().toString());

        model.setIS_MULTI_CKT(sp_is_multi_circuit_33.getSelectedItem().toString());
        if (sp_is_multi_circuit_33.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
            model.setMULTI_CKT_33(ch_multi_33kv_33.isChecked() ? "Yes" : "No");
            model.setMULTI_CKT_11(ch_multi_11kv_33.isChecked() ? "Yes" : "No");
            model.setMULTI_CKT_LT(ch_multi_lt_33.isChecked() ? "Yes" : "No");
            if (ch_multi_33kv_33.isChecked()) {
                model.setMULTI_CKT_33_POLE_NO(sp_33kv_multi_ckt_pole_33.getSelectedItem().toString());
            }
        }
        model.setPOLE_SUPPORT(sp_pole_support_33.getSelectedItem().toString());
        model.setPOLE_CONFIG(sp_pole_configuration_33.getSelectedItem().toString());

        model.setNEW_DATA_ADDED("Yes");
        model.setPIN_CUT(sp_pole_pin_cut_point_33kv.getSelectedItem().toString());
        if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
            model.setEND_POLE(sp_ded_end_pole_33kv.getSelectedItem().toString());
        } else {
            model.setEND_POLE("");

        }


//        model.setLATITUDE(poleLat);
//        model.setLONGITUDE(poleLon);


        boolean status = databaseEstimates.addNewSurveyData(model);
        if (status) {
            if (allList.isEmpty()) {
                EstimatesMap.btn_complete_survey.setVisibility(View.VISIBLE);
            }

            if (sp_is_multi_circuit_33.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                if (ch_multi_33kv_33.isChecked()) {
                    databaseEstimates.updateMultiCircuit33(EST_NO, String.valueOf(poleNumber), sp_33kv_multi_ckt_pole_33.getSelectedItem().toString(),GIS_ID);
                }
            }


            Toast.makeText(context, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
            dismiss();
            callout.dismiss();
            graphicsOverlay.getGraphics().remove(graphic);
        } else {
            Toast.makeText(context, "Failed to capture Pole location.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_voltage_level:

                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {

                    if (!sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {
                            layout_top_cleat_33kv.setVisibility(View.VISIBLE);
                            layout_v_crossing_33kv.setVisibility(View.VISIBLE);
                            layout_tapping_cross_arm_33kv.setVisibility(View.VISIBLE);
                            layout_channel_33kv.setVisibility(View.GONE);
                            layout_side_arm_33kv.setVisibility(View.GONE);
                        } else if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
                            layout_top_cleat_33kv.setVisibility(View.VISIBLE);
                            layout_tapping_cross_arm_33kv.setVisibility(View.VISIBLE);
                            layout_v_crossing_33kv.setVisibility(View.GONE);
                            layout_channel_33kv.setVisibility(View.GONE);
                            layout_side_arm_33kv.setVisibility(View.GONE);

                        }
                    } else {
                        layout_top_cleat_33kv.setVisibility(View.GONE);
                        layout_tapping_cross_arm_33kv.setVisibility(View.GONE);
                        layout_v_crossing_33kv.setVisibility(View.GONE);
                        layout_channel_33kv.setVisibility(View.GONE);
                        layout_side_arm_33kv.setVisibility(View.GONE);
                    }
                    layout_parent_exists.setVisibility(View.GONE);
                    survey_33.setVisibility(View.VISIBLE);
                    survey_11.setVisibility(View.GONE);
                    survey_lt.setVisibility(View.GONE);
                    layout_survey_pole_type.setVisibility(View.VISIBLE);
                    layout_sp_pole_height_33.setVisibility(View.VISIBLE);
                    layout_conductor_33.setVisibility(View.VISIBLE);
                    layout_distance.setVisibility(View.GONE);


                    if (allList.isEmpty()) {
                        poleNumber = 1;
                    } else {
                        int lastPoleNumber = Integer.parseInt(allList.get(allList.size() - 1).getPOLE_NO());

                        poleNumber = lastPoleNumber;

                        for (int i = 0; i < allList.size(); i++) {
                            int currentPoleNumber = Integer.parseInt(allList.get(i).getPOLE_NO());
                            if (currentPoleNumber > poleNumber) {
                                poleNumber = currentPoleNumber;
                            }
                        }

                        poleNumber++;
                        Log.d("LAST_POLE_NUMBER", "onItemSelected: " + lastPoleNumber);
                        ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, String.valueOf(lastPoleNumber),GIS_ID);
                        double lastPoleLat = Double.parseDouble(latAndLon.get(0).getLATITUDE());
                        double lastPoleLng = Double.parseDouble(latAndLon.get(0).getLONGITUDE());
                        Log.d("LAST_POLE_LAT_LNG", "onItemSelected: " + "LATTITUDE" + lastPoleLat + " " + "LONGITUDE" + lastPoleLng);

                        Log.d("Coordinates", "Last Pole: Lat=" + lastPoleLat + ", Lng=" + lastPoleLng);
                        Log.d("Coordinates", "Current: Lat=" + currentLat + ", Lng=" + currentLon);

                        float[] results = new float[1];

                        double distance = distance(lastPoleLat, lastPoleLng, 83.30846706980896, 17.739242041097558);
                        Log.d("========>", "onItemSelected: " + distance);
                        // Location.distanceBetween(17.738007390141785, 83.30954412375323, 17.738909223116934, 83.308443473652, results);

                    }


                    if (list33.isEmpty()) {
                        layout_connecting_point.setVisibility(View.GONE);
                    } else {
                        layout_connecting_point.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.VISIBLE);
                        List<String> list_pst = new ArrayList<>();

                        for (int i = 0; i < list33.size(); i++) {
                            list_pst.add(list33.get(i).getPOLE_NO());
                        }
                        // list_pst.add("Select");
                        Collections.reverse(list_pst);
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_connecting_point.setAdapter(adapter_pst);
                        sp_connecting_point.setSelection(0, true);
                    }
                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    if (!sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {
                            layout_top_cleat_11kv.setVisibility(View.VISIBLE);
                            layout_v_crossing_11kv.setVisibility(View.VISIBLE);
                            layout_tapping_cross_arm_11kv.setVisibility(View.VISIBLE);
                            layout_channel_11kv.setVisibility(View.GONE);
                            layout_side_arm_11kv.setVisibility(View.VISIBLE);
                        } else if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
                            layout_top_cleat_11kv.setVisibility(View.VISIBLE);
                            layout_tapping_cross_arm_11kv.setVisibility(View.VISIBLE);
                            layout_v_crossing_11kv.setVisibility(View.GONE);
                            layout_channel_11kv.setVisibility(View.GONE);
                            layout_side_arm_11kv.setVisibility(View.GONE);
                        }
                    } else {
                        layout_top_cleat_11kv.setVisibility(View.GONE);
                        layout_tapping_cross_arm_11kv.setVisibility(View.GONE);
                        layout_v_crossing_11kv.setVisibility(View.GONE);
                        layout_channel_11kv.setVisibility(View.GONE);
                        layout_side_arm_11kv.setVisibility(View.GONE);
                    }


                    if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("DTR Pole")) {
                        layout_hg_fuse_set_11kv.setVisibility(View.VISIBLE);
                    } else {
                        layout_hg_fuse_set_11kv.setVisibility(View.GONE);
                        layout_ci_pipe.setVisibility(View.GONE);
                        layout_gi_pipe.setVisibility(View.GONE);
                    }
                    setHgFuseSetAdapter();
                    loadABPoleData11KV(sp_voltage_level.getSelectedItem().toString());
                    loadDTRABPoleData11KV();
                    survey_33.setVisibility(View.GONE);
                    survey_11.setVisibility(View.VISIBLE);
                    survey_lt.setVisibility(View.GONE);
                    layout_survey_pole_type.setVisibility(View.VISIBLE);
                    layout_sp_pole_height_11.setVisibility(View.VISIBLE);
                    layout_conductor_11.setVisibility(View.VISIBLE);
                    layout_distance.setVisibility(View.GONE);

                    setDTRPhaseAdapter("11KV");

                    if (list11.isEmpty()) {
                        layout_parent_exists.setVisibility(View.VISIBLE);
                        layout_connecting_point.setVisibility(View.GONE);
                    } else {
                        layout_parent_exists.setVisibility(View.GONE);
                        layout_connecting_point.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.VISIBLE);
                        List<String> list_pst = new ArrayList<>();
                        for (int i = 0; i < list11.size(); i++) {
                            list_pst.add(list11.get(i).getPOLE_NO());
                        }
                        Collections.reverse(list_pst);
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_connecting_point.setAdapter(adapter_pst);
                        sp_connecting_point.setSelection(0, true);
                    }


                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("6.3KV")) {
                    if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {
                        layout_top_cleat_11kv.setVisibility(View.VISIBLE);
                        layout_tapping_cross_arm_11kv.setVisibility(View.GONE);
                        layout_v_crossing_11kv.setVisibility(View.GONE);
                    } else if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
                        layout_top_cleat_11kv.setVisibility(View.VISIBLE);
                        layout_tapping_cross_arm_11kv.setVisibility(View.VISIBLE);
                        layout_v_crossing_11kv.setVisibility(View.VISIBLE);
                    }


                    if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("DTR Pole")) {
                        layout_hg_fuse_set_11kv.setVisibility(View.VISIBLE);
                        setHgFuseSetAdapter();
                    } else {

                        layout_hg_fuse_set_11kv.setVisibility(View.GONE);

                    }
                    survey_33.setVisibility(View.GONE);
                    survey_11.setVisibility(View.VISIBLE);
                    survey_lt.setVisibility(View.GONE);
                    layout_survey_pole_type.setVisibility(View.VISIBLE);
                    layout_sp_pole_height_11.setVisibility(View.VISIBLE);
                    layout_conductor_11.setVisibility(View.VISIBLE);
                    layout_distance.setVisibility(View.GONE);
                    setDTRPhaseAdapter("6.3KV");
                    loadABPoleData63KV(sp_voltage_level.getSelectedItem().toString());
                    loadDTRABPoleData63KV();
                    if (list11.isEmpty()) {
                        layout_parent_exists.setVisibility(View.VISIBLE);
                        layout_connecting_point.setVisibility(View.GONE);
                    } else {
                        layout_parent_exists.setVisibility(View.GONE);
                        layout_connecting_point.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.VISIBLE);
                        List<String> list_pst = new ArrayList<>();
                        for (int i = 0; i < list11.size(); i++) {
                            list_pst.add(list11.get(i).getPOLE_NO());
                        }
                        Collections.reverse(list_pst);
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_connecting_point.setAdapter(adapter_pst);
                        sp_connecting_point.setSelection(0, true);
                    }

                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    survey_33.setVisibility(View.GONE);
                    survey_11.setVisibility(View.GONE);
                    survey_lt.setVisibility(View.VISIBLE);
                    layout_survey_pole_type.setVisibility(View.VISIBLE);
                    layout_sp_pole_height_lt.setVisibility(View.VISIBLE);
                    layout_conductor_lt.setVisibility(View.VISIBLE);
                    layout_distance.setVisibility(View.GONE);


                    if (listLT.isEmpty()) {
                        layout_parent_exists.setVisibility(View.VISIBLE);
                        layout_connecting_point.setVisibility(View.GONE);

                    } else {
                        layout_parent_exists.setVisibility(View.GONE);
                        layout_connecting_point.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.VISIBLE);

                        List<String> list_pst = new ArrayList<>();

                        for (int i = 0; i < list11.size(); i++) {
                            if (list11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                                list_pst.add(list11.get(i).getPOLE_NO());
                            }
                        }

                        for (int i = 0; i < listLT.size(); i++) {
                            list_pst.add(listLT.get(i).getPOLE_NO());
                        }
                        Collections.reverse(list_pst);
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_connecting_point.setAdapter(adapter_pst);
                        sp_connecting_point.setSelection(0, true);
                    }

                    if (!sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {
                            layout_top_cleat_lt.setVisibility(View.VISIBLE);
                            layout_v_crossing_lt.setVisibility(View.GONE);
                            layout_tapping_cross_arm_lt.setVisibility(View.VISIBLE);
                            layout_channel_lt.setVisibility(View.GONE);
                            layout_side_arm_lt.setVisibility(View.VISIBLE);
                        } else if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
                            layout_top_cleat_lt.setVisibility(View.GONE);
                            layout_tapping_cross_arm_lt.setVisibility(View.VISIBLE);
                            layout_v_crossing_lt.setVisibility(View.GONE);
                            layout_channel_lt.setVisibility(View.GONE);
                            layout_side_arm_lt.setVisibility(View.GONE);

                        }
                    } else {
                        layout_top_cleat_lt.setVisibility(View.GONE);
                        layout_tapping_cross_arm_lt.setVisibility(View.GONE);
                        layout_v_crossing_lt.setVisibility(View.GONE);
                        layout_channel_lt.setVisibility(View.GONE);
                        layout_side_arm_lt.setVisibility(View.GONE);
                    }

                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("METERS")) {

                    survey_33.setVisibility(View.GONE);
                    survey_11.setVisibility(View.GONE);
                    survey_lt.setVisibility(View.GONE);
                    layout_survey_pole_type.setVisibility(View.GONE);
                    pole_survey_type.setVisibility(View.GONE);


                } else {
                    survey_33.setVisibility(View.GONE);
                    survey_11.setVisibility(View.GONE);
                    survey_lt.setVisibility(View.GONE);
                    layout_parent_exists.setVisibility(View.GONE);
                    layout_connecting_point.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_is_parent_exists:
                if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                    tapping_location.setVisibility(View.GONE);
                    layout_parent_connecting_point.setVisibility(View.VISIBLE);


                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                        List<String> list_pst = new ArrayList<>();
                        for (int i = 0; i < list33.size(); i++) {
                            if (list33.get(i).getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")) {
                                list_pst.add(list33.get(i).getPOLE_NO());
                            }

                        }
                        list_pst.add("Select");
                        Collections.reverse(list_pst);
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_parent_connecting_point.setAdapter(adapter_pst);
                        sp_parent_connecting_point.setSelection(0, true);
                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("6.3KV")) {
                        List<String> list_pst = new ArrayList<>();
                        for (int i = 0; i < list33.size(); i++) {
                            if (list33.get(i).getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")) {
                                list_pst.add(list33.get(i).getPOLE_NO());
                            }

                        }
                        list_pst.add("Select");
                        Collections.reverse(list_pst);
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_parent_connecting_point.setAdapter(adapter_pst);
                        sp_parent_connecting_point.setSelection(0, true);
                    }

                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                        List<String> list_pst = new ArrayList<>();
                        for (int i = 0; i < list11.size(); i++) {
                            if (list11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                                list_pst.add(list11.get(i).getPOLE_NO());
                            }

                        }
                        list_pst.add("Select");
                        Collections.reverse(list_pst);
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_parent_connecting_point.setAdapter(adapter_pst);
                        sp_parent_connecting_point.setSelection(0, true);
                    }


                } else if (sp_is_parent_exists.getSelectedItem().toString().equalsIgnoreCase("No")) {
                    tapping_location.setVisibility(View.VISIBLE);
                    layout_parent_connecting_point.setVisibility(View.GONE);
                } else {
                    tapping_location.setVisibility(View.GONE);
                    layout_parent_connecting_point.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_is_location_exists:
                if (sp_is_location_exists.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
                    layout_location_exists.setVisibility(View.GONE);
                    List<String> ugpsccList = new ArrayList<>();
                    ugpsccList.add("Select");
                    ugpsccList.add("PSCC");
                    ugpsccList.add("SPUN");
                    // ugpsccList.add("TOWER");
                    ugpsccList.add("SS BAY");
                    ugpsccList.add("RAIL");
                    ugpsccList.add("RS JOIST");
                    ugpsccList.add("TUBULAR POLE");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_type_33.setAdapter(adapter);

                    List<String> ugpsccList11KV = new ArrayList<>();
                    ugpsccList11KV.add("Select");
                    ugpsccList11KV.add("PSCC");
                    ugpsccList11KV.add("SPUN");
                    ugpsccList11KV.add("TOWER");
                    ugpsccList11KV.add("SS BAY");
                    ugpsccList11KV.add("RAIL");
                    ugpsccList11KV.add("RS JOIST");
                    ugpsccList11KV.add("TUBULAR POLE");
                    ArrayAdapter<String> adapter11KV = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_type_11.setAdapter(adapter11KV);


                    List<String> ugpsccListLT = new ArrayList<>();
                    ugpsccListLT.add("Select");
                    ugpsccListLT.add("PSCC");
                    ugpsccListLT.add("SPUN");
                    ugpsccListLT.add("TOWER");
                    ugpsccListLT.add("SS BAY");
                    ugpsccListLT.add("RAIL");
                    ugpsccListLT.add("RS JOIST");
                    ugpsccListLT.add("TUBULAR POLE");
                    ArrayAdapter<String> adapterLT = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_type_lt.setAdapter(adapterLT);

                } else if (sp_is_location_exists.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {
                    List<String> ugpsccList = new ArrayList<>();
                    ugpsccList.add("Select");
                    ugpsccList.add("PSCC");
                    ugpsccList.add("SPUN");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_type_33.setAdapter(adapter);

                    List<String> ugpsccList11KV = new ArrayList<>();
                    ugpsccList11KV.add("Select");
                    ugpsccList11KV.add("PSCC");
                    ugpsccList11KV.add("SPUN");
                    ArrayAdapter<String> adapter11KV = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_type_11.setAdapter(adapter11KV);

                    List<String> ugpsccListLT = new ArrayList<>();
                    ugpsccListLT.add("Select");
                    ugpsccListLT.add("PSCC");
                    ugpsccListLT.add("SPUN");
                    ArrayAdapter<String> adapterLT = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_type_lt.setAdapter(adapterLT);

                    layout_location_exists.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_pole_configuration_33:
                //String selectedItem = sp_pole_configuration_33.getSelectedItem().toString();
                if (sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("33/11kv SS-Bay") ||
                        sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("EHT SS-Bay")) {
                    layout_type_of_black_clamps_33kv.setVisibility(View.GONE);
                    layout_sp_pole_type_33.setVisibility(View.GONE);
                    layout_sp_pole_height_33.setVisibility(View.GONE);
                    layout_sp_pole_desc_33.setVisibility(View.GONE);
                } else {
                    layout_sp_pole_type_33.setVisibility(View.VISIBLE);
                    layout_sp_pole_height_33.setVisibility(View.VISIBLE);
                    layout_sp_pole_desc_33.setVisibility(View.VISIBLE);

                }
                if (sp_pole_configuration_33.getSelectedItem().toString().equalsIgnoreCase("AB-Pole")) {
                    layout_ab_switch_33kv.setVisibility(View.VISIBLE);
                    loadABPoleData33KV();

                } else {
                    layout_ab_switch_33kv.setVisibility(View.GONE);

                }
                break;

            case R.id.sp_line_survey_type:
                if (!sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {

                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
                            ch_no_of_v_crossing_33kv.setChecked(false);
                            ch_top_cleat_33kv.setChecked(false);
                            ch_back_clamps_33kv.setChecked(false);
                        } else {
                            if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {
                                layout_materials_33kv.setVisibility(View.VISIBLE);
                                layout_ded_end_pole_33kv.setVisibility(View.GONE);
                                tv_fabrication_materials_33kv.setVisibility(View.VISIBLE);
                                tv_insulators_details_33kv.setVisibility(View.VISIBLE);
                                ch_no_of_v_crossing_33kv.setChecked(true);

                                if (ch_no_of_v_crossing_33kv.isChecked()) {
                                    layout_no_v_crossing_arms_33kv.setVisibility(View.VISIBLE);
                                } else {
                                    layout_no_v_crossing_arms_33kv.setVisibility(View.GONE);
                                }

                                ch_pin_insulators_33kv.setChecked(true);

                                if (ch_pin_insulators_33kv.isChecked()) {
                                    layout_no_of_pin_insulators_type_33kv.setVisibility(View.VISIBLE);
                                } else {
                                    layout_no_of_pin_insulators_type_33kv.setVisibility(View.GONE);
                                }

                                layout_no_of_pin_insulators_33kv.setVisibility(View.VISIBLE);
                                layout_no_of_cut_insulators_33kv.setVisibility(View.GONE);
                                layout_top_cleat_33kv.setVisibility(View.VISIBLE);
                                layout_v_crossing_33kv.setVisibility(View.VISIBLE);
                                layout_tapping_cross_arm_33kv.setVisibility(View.VISIBLE);
                                layout_channel_33kv.setVisibility(View.GONE);
                                layout_side_arm_33kv.setVisibility(View.GONE);
                            }
                        }
                        layout_parent_exists.setVisibility(View.GONE);
                        survey_33.setVisibility(View.VISIBLE);
                        survey_11.setVisibility(View.GONE);
                        survey_lt.setVisibility(View.GONE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_33.setVisibility(View.VISIBLE);
                        layout_conductor_33.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (list33.isEmpty()) {
                            layout_connecting_point.setVisibility(View.GONE);
                        } else {
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);
                            List<String> list_pst = new ArrayList<>();

                            for (int i = 0; i < list33.size(); i++) {
                                list_pst.add(list33.get(i).getPOLE_NO());
                            }
                            // list_pst.add("Select");
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            sp_connecting_point.setSelection(0, true);
                        }
                        layout_existing_connecting_point.setVisibility(View.GONE);

                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                        survey_33.setVisibility(View.GONE);
                        survey_11.setVisibility(View.VISIBLE);
                        survey_lt.setVisibility(View.GONE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_11.setVisibility(View.VISIBLE);
                        layout_conductor_11.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (list11.isEmpty()) {
                            layout_parent_exists.setVisibility(View.VISIBLE);
                            layout_connecting_point.setVisibility(View.GONE);
                        } else {
                            layout_parent_exists.setVisibility(View.GONE);
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);
                            List<String> list_pst = new ArrayList<>();
                            for (int i = 0; i < list11.size(); i++) {
                                list_pst.add(list11.get(i).getPOLE_NO());
                            }
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            sp_connecting_point.setSelection(0, true);
                        }
                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                        survey_33.setVisibility(View.GONE);
                        survey_11.setVisibility(View.GONE);
                        survey_lt.setVisibility(View.VISIBLE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_lt.setVisibility(View.VISIBLE);
                        layout_conductor_lt.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (listLT.isEmpty()) {
                            layout_parent_exists.setVisibility(View.VISIBLE);
                            layout_connecting_point.setVisibility(View.GONE);

                        } else {
                            layout_parent_exists.setVisibility(View.GONE);
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);

                            List<String> list_pst = new ArrayList<>();

                            for (int i = 0; i < list11.size(); i++) {
                                if (list11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                                    list_pst.add(list11.get(i).getPOLE_NO());
                                }
                            }

                            for (int i = 0; i < listLT.size(); i++) {
                                list_pst.add(listLT.get(i).getPOLE_NO());
                            }
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            sp_connecting_point.setSelection(0, true);
                        }
                    }

                }
                break;


            case R.id.sp_is_multi_circuit_33:
                if (sp_is_multi_circuit_33.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                    multi_ckt_layout_33.setVisibility(View.VISIBLE);
                    ch_multi_33kv_33.setEnabled(false);
                    ch_multi_33kv_33.setVisibility(View.GONE);
                } else {
                    multi_ckt_layout_33.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_is_multi_circuit_11:
                if (sp_is_multi_circuit_11.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                    multi_ckt_layout_11.setVisibility(View.VISIBLE);
                    ch_multi_11kv_11.setEnabled(false);
                    ch_multi_11kv_11.setVisibility(View.GONE);
                } else {
                    multi_ckt_layout_11.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_connecting_point:
                String location = sp_connecting_point.getSelectedItem().toString();
                if (location != null) {
                    ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_connecting_point.getSelectedItem().toString(),GIS_ID);
                    for (int i = 0; i < latAndLon.size(); i++) {
                        conPoleLattitude = Double.parseDouble(latAndLon.get(i).getLONGITUDE());
                        conPoleLongitude = Double.parseDouble(latAndLon.get(i).getLATITUDE());
                    }


                    Location connectingPoleLocation = new Location("ConnectingPole");
                    connectingPoleLocation.setLatitude(conPoleLattitude);
                    connectingPoleLocation.setLongitude(conPoleLongitude);

                    if (allList.size() > 0) {
                        Location currentPoleLocation = new Location("CurrentPole");
                        currentPoleLocation.setLatitude(Double.parseDouble(poleLon));
                        currentPoleLocation.setLongitude(Double.parseDouble(poleLat));
                        distance = currentPoleLocation.distanceTo(connectingPoleLocation);
                        Log.d("currentPoleLocation-->", "onItemSelected: " + connectingPoleLocation + " " + currentPoleLocation);

                        int dis = (int) Math.round(distance);
                        last_pole_distance.setText("" + dis);


                        Log.d("Distance", "Distance between connecting point and last pole: " + distance + " meters");
                    } else {
                        Log.d("Distance", "No last pole available to calculate distance.");
                    }


                }
                break;

            case R.id.sp_is_multi_circuit_lt:
                if (sp_is_multi_circuit_lt.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                    multi_ckt_layout_lt.setVisibility(View.VISIBLE);
                    ch_multi_lt_lt.setEnabled(false);
                    ch_multi_lt_lt.setVisibility(View.GONE);
                } else {
                    multi_ckt_layout_lt.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_dtr_survey_11:
                if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed") ||
                        sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Enhancement")) {
                    layout_distance.setVisibility(View.VISIBLE);
                    layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
                    main_lt_layout_11kv.setVisibility(View.GONE);


                } else if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
                    layout_distance.setVisibility(View.GONE);
                    layout_dtr_phase_11kv.setVisibility(View.GONE);
                    layout_dtr_capacity.setVisibility(View.GONE);
                    main_lt_layout_11kv.setVisibility(View.VISIBLE);
                } else {
                    layout_distance.setVisibility(View.GONE);
                    layout_dtr_phase_11kv.setVisibility(View.GONE);
                    layout_dtr_capacity.setVisibility(View.GONE);
                    main_lt_layout_11kv.setVisibility(View.GONE);

                }
                break;

            case R.id.sp_pole_pin_cut_point_11:
                if (!sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {

                        layout_materials_11kv.setVisibility(View.VISIBLE);
                        tv_insulators_details_11kv.setVisibility(View.VISIBLE);
                        tv_fabrication_materials_11kv.setVisibility(View.VISIBLE);
                        layout_ded_end_pole_11kv.setVisibility(View.GONE);
                        ch_no_of_v_crossing_11kv.setChecked(true);

                        if (ch_no_of_v_crossing_11kv.isChecked()) {
                            layout_no_v_crossing_arms_11kv.setVisibility(View.VISIBLE);
                        } else {
                            layout_no_v_crossing_arms_11kv.setVisibility(View.GONE);
                        }

                        ch_pin_insulators_11kv.setChecked(true);

                        if (ch_pin_insulators_11kv.isChecked()) {
                            layout_no_of_pin_insulators_type_11kv.setVisibility(View.VISIBLE);
                        } else {
                            layout_no_of_pin_insulators_type_11kv.setVisibility(View.GONE);
                        }


                        layout_no_of_pin_insulators_11kv.setVisibility(View.VISIBLE);
                        layout_no_of_cut_insulators.setVisibility(View.GONE);
                        if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("6.3KV")) {
                            layout_top_cleat_11kv.setVisibility(View.VISIBLE);
                            layout_v_crossing_11kv.setVisibility(View.GONE);
                            layout_tapping_cross_arm_11kv.setVisibility(View.GONE);


                        } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                            layout_top_cleat_11kv.setVisibility(View.VISIBLE);
                            layout_v_crossing_11kv.setVisibility(View.VISIBLE);
                            layout_tapping_cross_arm_11kv.setVisibility(View.VISIBLE);
                            layout_channel_11kv.setVisibility(View.GONE);
                            layout_side_arm_11kv.setVisibility(View.VISIBLE);

                        }

                    } else if (sp_pole_pin_cut_point_11.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
                        tv_fabrication_materials_11kv.setVisibility(View.VISIBLE);
                        layout_ded_end_pole_11kv.setVisibility(View.VISIBLE);
                        tv_insulators_details_11kv.setVisibility(View.VISIBLE);
                        loadMetalParts11KV();
                        layout_no_of_pin_insulators_11kv.setVisibility(View.VISIBLE);
                        layout_no_of_cut_insulators.setVisibility(View.VISIBLE);
                        layout_top_cleat_11kv.setVisibility(View.VISIBLE);
                        layout_v_crossing_11kv.setVisibility(View.GONE);
                        layout_tapping_cross_arm_11kv.setVisibility(View.VISIBLE);
                        layout_channel_11kv.setVisibility(View.GONE);
                        layout_side_arm_11kv.setVisibility(View.VISIBLE);

                    }
                } else {
                    layout_materials_11kv.setVisibility(View.GONE);
                    layout_top_cleat_11kv.setVisibility(View.GONE);
                    layout_v_crossing_11kv.setVisibility(View.GONE);
                    layout_tapping_cross_arm_11kv.setVisibility(View.GONE);
                    layout_channel_11kv.setVisibility(View.GONE);
                    layout_side_arm_11kv.setVisibility(View.GONE);
                    layout_ded_end_pole_11kv.setVisibility(View.GONE);
                    tv_fabrication_materials_11kv.setVisibility(View.GONE);
                    layout_ded_end_pole_11kv.setVisibility(View.GONE);
                    tv_insulators_details_11kv.setVisibility(View.GONE);
                    layout_no_of_pin_insulators_11kv.setVisibility(View.GONE);
                    layout_no_of_cut_insulators.setVisibility(View.GONE);


                }

                break;


            case R.id.sp_ded_end_pole_lt:
                String selectedItemLt = sp_ded_end_pole_lt.getSelectedItem().toString();
                if (!selectedItemLt.equalsIgnoreCase("Select")) {
                    layout_materials_lt.setVisibility(View.VISIBLE);
                    if (selectedItemLt.equalsIgnoreCase("Yes")) {
                        layout_materials_lt.setVisibility(View.VISIBLE);
                        ch_tapping_cross_arms_lt.setChecked(true);
                        ch_disc_insulators_lt.setChecked(true);
                        ch_pin_insulators_lt.setChecked(false);
                        ch_top_cleat_lt.setChecked(false);
                        if (ch_tapping_cross_arms_lt.isChecked()) {
                            layout_tapping_cross_arm_lt.setVisibility(View.VISIBLE);
                        } else {
                            layout_tapping_cross_arm_lt.setVisibility(View.GONE);
                        }

                        if (ch_disc_insulators_lt.isChecked()) {
                            layout_type_of_disc_insulator_lt.setVisibility(View.VISIBLE);
                            sp_no_of_disc_insulators_lt.setSelection(3, true);
                            sp_no_of_metal_insulators_lt.setSelection(3, true);
                        } else {
                            layout_type_of_disc_insulator_lt.setVisibility(View.GONE);
                        }
                    } else if (selectedItemLt.equalsIgnoreCase("No")) {
                        ch_tapping_cross_arms_lt.setChecked(true);
                        ch_disc_insulators_lt.setChecked(true);
                        ch_pin_insulators_lt.setChecked(true);
                        ch_top_cleat_lt.setChecked(true);
                        if (ch_tapping_cross_arms_lt.isChecked()) {
                            layout_tapping_cross_arm_lt.setVisibility(View.VISIBLE);
                            sp_no_of_tapping_cross_arms_lt.setSelection(1, true);
                        } else {
                            layout_tapping_cross_arm_lt.setVisibility(View.GONE);
                        }
                        if (ch_disc_insulators_lt.isChecked()) {
                            layout_type_of_disc_insulator_lt.setVisibility(View.VISIBLE);
                            sp_no_of_disc_insulators_lt.setSelection(6, true);
                            sp_no_of_metal_insulators_lt.setSelection(6, true);
                        } else {
                            layout_type_of_disc_insulator_lt.setVisibility(View.GONE);
                        }

                        if (ch_top_cleat_33kv.isChecked()) {
                            layout_no_top_cleat_lt.setVisibility(View.VISIBLE);
                        } else {
                            layout_no_top_cleat_lt.setVisibility(View.GONE);
                        }
                        if (ch_pin_insulators_lt.isChecked()) {
                            layout_no_top_cleat_lt.setVisibility(View.VISIBLE);
                            sp_no_of_pin_insulators_lt.setSelection(0, true);
                        } else {
                            layout_no_top_cleat_lt.setVisibility(View.GONE);
                        }

                    } else {
                        ch_pin_insulators_lt.setChecked(false);
                        ch_top_cleat_lt.setChecked(false);
                        ch_tapping_cross_arms_lt.setChecked(false);
                        ch_disc_insulators_lt.setChecked(false);
                    }
                } else {
                    layout_materials_lt.setVisibility(View.GONE);
                    ch_tapping_cross_arms_lt.setChecked(false);
                    ch_disc_insulators_lt.setChecked(false);
                    ch_pin_insulators_lt.setChecked(false);
                    ch_top_cleat_lt.setChecked(false);
                }
                break;

            case R.id.sp_ded_end_pole_33kv:
                String selectedItem = sp_ded_end_pole_33kv.getSelectedItem().toString();
                if (!selectedItem.equalsIgnoreCase("Select")) {
                    layout_materials_33kv.setVisibility(View.VISIBLE);
                    if (selectedItem.equalsIgnoreCase("Yes")) {
                        layout_materials_33kv.setVisibility(View.VISIBLE);
                        ch_tapping_cross_arms_33kv.setChecked(true);
                        ch_disc_insulators_33kv.setChecked(true);
                        ch_pin_insulators_33kv.setChecked(false);
                        ch_top_cleat_33kv.setChecked(false);
                        if (ch_tapping_cross_arms_33kv.isChecked()) {
                            layout_tapping_cross_arm_33kv.setVisibility(View.VISIBLE);
                        } else {
                            layout_tapping_cross_arm_33kv.setVisibility(View.GONE);
                        }

                        if (ch_disc_insulators_33kv.isChecked()) {
                            layout_type_of_disc_insulator_33kv.setVisibility(View.VISIBLE);
                            sp_no_of_disc_insulators_33kv.setSelection(3, true);
                            sp_no_of_metal_insulators_33kv.setSelection(3, true);
                        } else {
                            layout_type_of_disc_insulator_33kv.setVisibility(View.GONE);
                        }
                    } else if (selectedItem.equalsIgnoreCase("No")) {
                        layout_materials_33kv.setVisibility(View.VISIBLE);
                        ch_tapping_cross_arms_33kv.setChecked(true);
                        ch_disc_insulators_33kv.setChecked(true);
                        ch_pin_insulators_33kv.setChecked(true);
                        ch_top_cleat_33kv.setChecked(true);
                        if (ch_tapping_cross_arms_33kv.isChecked()) {
                            layout_tapping_cross_arm_33kv.setVisibility(View.VISIBLE);
                            sp_no_of_tapping_cross_arms_33kv.setSelection(1, true);
                        } else {
                            layout_tapping_cross_arm_33kv.setVisibility(View.GONE);
                        }
                        if (ch_disc_insulators_33kv.isChecked()) {
                            layout_type_of_disc_insulator_33kv.setVisibility(View.VISIBLE);
                            sp_no_of_disc_insulators_33kv.setSelection(6, true);
                            sp_no_of_metal_insulators_33kv.setSelection(6, true);
                        } else {
                            layout_type_of_disc_insulator_33kv.setVisibility(View.GONE);
                        }

                        if (ch_top_cleat_33kv.isChecked()) {
                            layout_no_top_cleat_33kv.setVisibility(View.VISIBLE);
                        } else {
                            layout_no_top_cleat_33kv.setVisibility(View.GONE);
                        }
                        if (ch_pin_insulators_33kv.isChecked()) {
                            layout_no_top_cleat_33kv.setVisibility(View.VISIBLE);
                            sp_no_of_pin_insulators_33kv.setSelection(0, true);
                        } else {
                            layout_no_top_cleat_33kv.setVisibility(View.GONE);
                        }

                    } else {
                        ch_pin_insulators_33kv.setChecked(false);
                        ch_top_cleat_33kv.setChecked(false);
                        ch_tapping_cross_arms_33kv.setChecked(false);
                        ch_disc_insulators_33kv.setChecked(false);
                    }
                } else {
                    layout_materials_33kv.setVisibility(View.GONE);
                    ch_tapping_cross_arms_33kv.setChecked(false);
                    ch_disc_insulators_33kv.setChecked(false);
                    ch_pin_insulators_33kv.setChecked(false);
                    ch_top_cleat_33kv.setChecked(false);
                }
                break;

            case R.id.sp_no_of_disc_insulators_33kv:
                String noOfDisc = sp_no_of_disc_insulators_33kv.getSelectedItem().toString();
                if (!noOfDisc.equalsIgnoreCase("Select")) {
                    tv_no_of_metal_parts_33kv.setText(noOfDisc);
                }
                break;

            case R.id.sp_no_of_disc_insulators_11kv:
                String noOfDisc11 = sp_no_of_disc_insulators_11kv.getSelectedItem().toString();
                if (!noOfDisc11.equalsIgnoreCase("Select")) {
                    tv_no_of_metal_parts_11kv.setText(noOfDisc11);
                }
                break;

            case R.id.sp_no_of_disc_insulators_lt:
                String noOfDisclt = sp_no_of_disc_insulators_lt.getSelectedItem().toString();
                if (!noOfDisclt.equalsIgnoreCase("Select")) {
                    tv_no_of_metal_parts_lt.setText(noOfDisclt);
                }
                break;


            case R.id.sp_ded_end_pole_11kv:
                String selectedItem1kv = sp_ded_end_pole_11kv.getSelectedItem().toString();
                if (!selectedItem1kv.equalsIgnoreCase("Select")) {
                    layout_materials_11kv.setVisibility(View.VISIBLE);
                    if (selectedItem1kv.equalsIgnoreCase("Yes")) {
                        layout_materials_11kv.setVisibility(View.VISIBLE);
                        ch_tapping_cross_arms_11kv.setChecked(true);
                        ch_disc_insulators_11kv.setChecked(true);
                        ch_pin_insulators_11kv.setChecked(false);
                        ch_top_cleat_33kv.setChecked(false);
                        if (ch_tapping_cross_arms_11kv.isChecked()) {
                            layout_tapping_cross_arm_11kv.setVisibility(View.VISIBLE);
                        } else {
                            layout_tapping_cross_arm_11kv.setVisibility(View.GONE);
                        }

                        if (ch_disc_insulators_11kv.isChecked()) {
                            layout_type_of_disc_insulator_11kv.setVisibility(View.VISIBLE);
                            sp_no_of_disc_insulators_11kv.setSelection(3, true);
                            // tv_no_of_metal_parts_11kv.setText(sp_no_of_disc_insulators_11kv.getSelectedItem().toString());
                            sp_no_of_metal_insulators_11kv.setSelection(3, true);
                        } else {
                            layout_type_of_disc_insulator_11kv.setVisibility(View.GONE);
                        }

                    } else if (selectedItem1kv.equalsIgnoreCase("No")) {
                        layout_materials_11kv.setVisibility(View.VISIBLE);
                        ch_tapping_cross_arms_11kv.setChecked(true);
                        ch_disc_insulators_11kv.setChecked(true);
                        ch_pin_insulators_11kv.setChecked(true);
                        ch_disc_insulators_33kv.setChecked(true);
                        ch_top_cleat.setChecked(true);
                        if (ch_tapping_cross_arms_11kv.isChecked()) {
                            layout_tapping_cross_arm_11kv.setVisibility(View.VISIBLE);
                            sp_no_of_tapping_cross_arms_11kv.setSelection(1, true);
                        } else {
                            layout_tapping_cross_arm_11kv.setVisibility(View.GONE);
                        }
                        if (ch_disc_insulators_33kv.isChecked()) {
                            layout_type_of_disc_insulator_11kv.setVisibility(View.VISIBLE);
                            sp_no_of_disc_insulators_11kv.setSelection(6, true);
                            sp_no_of_metal_insulators_33kv.setSelection(6, true);
                        } else {
                            layout_type_of_disc_insulator_11kv.setVisibility(View.GONE);
                        }

                        if (ch_top_cleat_33kv.isChecked()) {
                            layout_no_top_cleat_11kv.setVisibility(View.VISIBLE);
                        } else {
                            layout_no_top_cleat_11kv.setVisibility(View.GONE);
                        }
                        if (ch_pin_insulators_11kv.isChecked()) {
                            layout_no_top_cleat_11kv.setVisibility(View.VISIBLE);
                            sp_no_of_pin_insulators_33kv.setSelection(0, true);
                        } else {
                            layout_no_top_cleat_11kv.setVisibility(View.GONE);
                        }

                    } else {
                        ch_pin_insulators_11kv.setChecked(false);
                        ch_top_cleat.setChecked(false);
                        ch_tapping_cross_arms_11kv.setChecked(false);
                        ch_disc_insulators_11kv.setChecked(false);
                    }


                } else {
                    layout_materials_11kv.setVisibility(View.GONE);
                }
                break;


            case R.id.sp_pole_pin_cut_point_33kv:
                if (!sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {

                        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
                            layout_materials_33kv.setVisibility(View.VISIBLE);
                            layout_ded_end_pole_33kv.setVisibility(View.GONE);
                            tv_fabrication_materials_33kv.setVisibility(View.VISIBLE);
                            tv_insulators_details_33kv.setVisibility(View.VISIBLE);
                            ch_no_of_v_crossing_33kv.setChecked(false);
                            if (ch_no_of_v_crossing_33kv.isChecked()) {
                                layout_no_v_crossing_arms_33kv.setVisibility(View.VISIBLE);
                            } else {
                                layout_no_v_crossing_arms_33kv.setVisibility(View.GONE);
                            }

                            ch_pin_insulators_33kv.setChecked(false);

                            if (ch_pin_insulators_33kv.isChecked()) {
                                layout_no_of_pin_insulators_type_33kv.setVisibility(View.VISIBLE);
                            } else {
                                layout_no_of_pin_insulators_type_33kv.setVisibility(View.GONE);
                            }
                            ch_tapping_cross_arms_33kv.setChecked(false);

                            layout_no_of_pin_insulators_33kv.setVisibility(View.VISIBLE);
                            layout_no_of_cut_insulators_33kv.setVisibility(View.VISIBLE);
                            layout_top_cleat_33kv.setVisibility(View.VISIBLE);
                            layout_v_crossing_33kv.setVisibility(View.VISIBLE);
                            layout_tapping_cross_arm_33kv.setVisibility(View.VISIBLE);
                            layout_channel_33kv.setVisibility(View.VISIBLE);
                            layout_side_arm_33kv.setVisibility(View.VISIBLE);
                        } else {
                            layout_materials_33kv.setVisibility(View.VISIBLE);
                            layout_ded_end_pole_33kv.setVisibility(View.GONE);
                            tv_fabrication_materials_33kv.setVisibility(View.VISIBLE);
                            tv_insulators_details_33kv.setVisibility(View.VISIBLE);
                            ch_no_of_v_crossing_33kv.setChecked(true);

                            if (ch_no_of_v_crossing_33kv.isChecked()) {
                                layout_no_v_crossing_arms_33kv.setVisibility(View.VISIBLE);
                            } else {
                                layout_no_v_crossing_arms_33kv.setVisibility(View.GONE);
                            }

                            ch_pin_insulators_33kv.setChecked(true);

                            if (ch_pin_insulators_33kv.isChecked()) {
                                layout_no_of_pin_insulators_type_33kv.setVisibility(View.VISIBLE);
                            } else {
                                layout_no_of_pin_insulators_type_33kv.setVisibility(View.GONE);
                            }

                            layout_no_of_pin_insulators_33kv.setVisibility(View.VISIBLE);
                            layout_no_of_cut_insulators_33kv.setVisibility(View.GONE);
                            layout_top_cleat_33kv.setVisibility(View.VISIBLE);
                            layout_v_crossing_33kv.setVisibility(View.VISIBLE);
                            layout_tapping_cross_arm_33kv.setVisibility(View.VISIBLE);
                            layout_channel_33kv.setVisibility(View.GONE);
                            layout_side_arm_33kv.setVisibility(View.GONE);
                        }


                    } else if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
                        tv_fabrication_materials_33kv.setVisibility(View.VISIBLE);
                        tv_insulators_details_33kv.setVisibility(View.VISIBLE);
                        layout_ded_end_pole_33kv.setVisibility(View.VISIBLE);
                        layout_no_of_pin_insulators_33kv.setVisibility(View.VISIBLE);
                        layout_no_of_cut_insulators_33kv.setVisibility(View.VISIBLE);
                        layout_side_arm_33kv.setVisibility(View.GONE);
                        layout_top_cleat_33kv.setVisibility(View.VISIBLE);
                        layout_v_crossing_33kv.setVisibility(View.GONE);
                        layout_tapping_cross_arm_33kv.setVisibility(View.VISIBLE);
                        loadMetalParts33KV();


                    }
                } else {
                    layout_materials_33kv.setVisibility(View.GONE);
                    tv_fabrication_materials_33kv.setVisibility(View.GONE);
                    tv_insulators_details_33kv.setVisibility(View.GONE);
                    layout_ded_end_pole_33kv.setVisibility(View.GONE);
                    layout_no_of_pin_insulators_33kv.setVisibility(View.GONE);
                    layout_no_of_cut_insulators_33kv.setVisibility(View.GONE);
                    layout_side_arm_33kv.setVisibility(View.GONE);
                    layout_top_cleat_33kv.setVisibility(View.GONE);
                    layout_v_crossing_33kv.setVisibility(View.GONE);
                    layout_tapping_cross_arm_33kv.setVisibility(View.GONE);
                }


                break;


            case R.id.sp_pole_configuration_11:
                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    String selectedPoleConfig = sp_pole_configuration_11.getSelectedItem().toString();

                    if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("SS BAY")) {
                        layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
                        layout_sp_pole_type_11.setVisibility(View.GONE);
                        layout_sp_pole_height_11.setVisibility(View.GONE);
                        layout_sp_pole_desc_11.setVisibility(View.GONE);
                        layout_ci_pipe.setVisibility(View.GONE);
                        layout_gi_pipe.setVisibility(View.GONE);
                        layout_type_of_black_clamps_11kv.setVisibility(View.GONE);
                    } else {
                        layout_sp_pole_type_11.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_11.setVisibility(View.VISIBLE);
                        layout_lt_fuse_set_11kv.setVisibility(View.GONE);
                        layout_hg_fuse_set_11kv.setVisibility(View.GONE);
                        layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
                        layout_ab_switch_11kv.setVisibility(View.VISIBLE);
                        layout_dtr_survey.setVisibility(View.GONE);
                        layout_dtr_phase_11kv.setVisibility(View.GONE);
                        layout_dtr_capacity.setVisibility(View.GONE);
                        layout_dtr_mounting.setVisibility(View.GONE);
                        layout_ci_pipe.setVisibility(View.GONE);
                        layout_gi_pipe.setVisibility(View.GONE);
                    }
                    if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("AB-Pole")) {
                        layout_lt_fuse_set_11kv.setVisibility(View.GONE);
                        layout_hg_fuse_set_11kv.setVisibility(View.GONE);
                        layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
                        layout_ab_switch_11kv.setVisibility(View.VISIBLE);
                        layout_dtr_survey.setVisibility(View.GONE);
                        layout_dtr_phase_11kv.setVisibility(View.GONE);
                        layout_dtr_capacity.setVisibility(View.GONE);
                        layout_dtr_mounting.setVisibility(View.GONE);
                        layout_ci_pipe.setVisibility(View.GONE);
                        layout_gi_pipe.setVisibility(View.GONE);
                        loadABPoleData11KV("11KV");
                    } else if (sp_pole_configuration_11.getSelectedItem().toString().equalsIgnoreCase("DTR Pole")) {
                        layout_lt_fuse_set_11kv.setVisibility(View.VISIBLE);
                        layout_hg_fuse_set_11kv.setVisibility(View.VISIBLE);
                        layout_dtr_ab_switch_11kv.setVisibility(View.VISIBLE);
                        layout_dtr_survey.setVisibility(View.VISIBLE);
                        layout_ab_switch_11kv.setVisibility(View.GONE);
                        layout_ci_pipe.setVisibility(View.VISIBLE);
                        layout_gi_pipe.setVisibility(View.VISIBLE);
                        layout_dtr_mounting.setVisibility(View.VISIBLE);

                        loadDTRABPoleData11KV();
                        setDTRPhaseAdapter("11KV");
                        sp_hg_fuse_set_11kv.setSelection(0, true);
                        setHgFuseSetAdapter();
                        if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {

                            layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
                            layout_ab_switch_11kv.setVisibility(View.GONE);
                        } else if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
                            layout_dtr_survey.setVisibility(View.VISIBLE);

                            layout_dtr_phase_11kv.setVisibility(View.GONE);
                            layout_dtr_capacity.setVisibility(View.GONE);
                            layout_dtr_phase_desc_11kv.setVisibility(View.GONE);
                            layout_ab_switch_11kv.setVisibility(View.GONE);
                        }
                    } else {
                        layout_lt_fuse_set_11kv.setVisibility(View.GONE);
                        layout_hg_fuse_set_11kv.setVisibility(View.GONE);
                        layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
                        layout_dtr_survey.setVisibility(View.GONE);
                        layout_dtr_mounting.setVisibility(View.GONE);
                        layout_dtr_capacity.setVisibility(View.GONE);
                        layout_dtr_phase_11kv.setVisibility(View.GONE);
                        layout_ab_switch_11kv.setVisibility(View.GONE);
                        layout_dtr_phase_desc_11kv.setVisibility(View.GONE);

                    }


                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("6.3KV")) {
                    // Reset all layouts first
                    layout_lt_fuse_set_11kv.setVisibility(View.GONE);
                    layout_hg_fuse_set_11kv.setVisibility(View.GONE);
                    layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
                    layout_ab_switch_11kv.setVisibility(View.GONE);
                    layout_dtr_survey.setVisibility(View.GONE);
                    layout_dtr_phase_11kv.setVisibility(View.GONE);
                    layout_dtr_capacity.setVisibility(View.GONE);
                    layout_dtr_mounting.setVisibility(View.GONE);
                    layout_ci_pipe.setVisibility(View.GONE);
                    layout_gi_pipe.setVisibility(View.GONE);
                    layout_dtr_phase_desc_11kv.setVisibility(View.GONE);

                    // Now apply conditions based on selected pole configuration
                    String selectedPoleConfig = sp_pole_configuration_11.getSelectedItem().toString();

                    if (selectedPoleConfig.equalsIgnoreCase("SS BAY")) {
                        layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
                        layout_sp_pole_type_11.setVisibility(View.GONE);
                        layout_sp_pole_height_11.setVisibility(View.GONE);
                    } else {
                        layout_sp_pole_type_11.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_11.setVisibility(View.VISIBLE);
                    }

                    if (selectedPoleConfig.equalsIgnoreCase("AB-Pole")) {
                        layout_lt_fuse_set_11kv.setVisibility(View.GONE);
                        layout_hg_fuse_set_11kv.setVisibility(View.GONE);
                        layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
                        layout_ab_switch_11kv.setVisibility(View.VISIBLE);
                        layout_dtr_survey.setVisibility(View.GONE);
                        layout_dtr_phase_11kv.setVisibility(View.GONE);
                        layout_dtr_capacity.setVisibility(View.GONE);
                        layout_dtr_mounting.setVisibility(View.GONE);

                        loadABPoleData63KV(sp_voltage_level.getSelectedItem().toString());
                    } else if (selectedPoleConfig.equalsIgnoreCase("DTR Pole")) {
                        layout_lt_fuse_set_11kv.setVisibility(View.VISIBLE);
                        layout_hg_fuse_set_11kv.setVisibility(View.VISIBLE);
                        layout_dtr_ab_switch_11kv.setVisibility(View.VISIBLE);
                        layout_dtr_survey.setVisibility(View.VISIBLE);
                        layout_ab_switch_11kv.setVisibility(View.GONE);
                        layout_ci_pipe.setVisibility(View.VISIBLE);
                        layout_gi_pipe.setVisibility(View.VISIBLE);
                        layout_dtr_mounting.setVisibility(View.VISIBLE);

                        loadDTRABPoleData63KV();
                        setDTRPhaseAdapter(sp_voltage_level.getSelectedItem().toString());
                        sp_hg_fuse_set_11kv.setSelection(0, true);
                        setHgFuseSetAdapter();

                        if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Proposed")) {

                            layout_dtr_phase_11kv.setVisibility(View.VISIBLE);
                            layout_ab_switch_11kv.setVisibility(View.GONE);
                        } else if (sp_dtr_survey_11.getSelectedItem().toString().equalsIgnoreCase("Existing")) {
                            layout_dtr_survey.setVisibility(View.VISIBLE);
                            layout_dtr_phase_11kv.setVisibility(View.GONE);
                            layout_dtr_capacity.setVisibility(View.GONE);
                            layout_dtr_phase_desc_11kv.setVisibility(View.GONE);
                            layout_ab_switch_11kv.setVisibility(View.GONE);
                        }
                    } else {
                        layout_lt_fuse_set_11kv.setVisibility(View.GONE);
                        layout_hg_fuse_set_11kv.setVisibility(View.GONE);
                        layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
                        layout_dtr_survey.setVisibility(View.GONE);
                        layout_dtr_mounting.setVisibility(View.GONE);
                        layout_dtr_capacity.setVisibility(View.GONE);
                        layout_dtr_phase_11kv.setVisibility(View.GONE);
                        layout_ab_switch_11kv.setVisibility(View.GONE);
                        layout_dtr_phase_desc_11kv.setVisibility(View.GONE);
                    }
                }

                break;

            case R.id.sp_pole_pin_cut_point_lt:
                if (!sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Pin Point")) {
                        layout_materials_lt.setVisibility(View.VISIBLE);
                        sp_pole_pin_cut_point_lt.setVisibility(View.VISIBLE);
                        layout_ded_end_pole_lt.setVisibility(View.GONE);
                        tv_fabrication_materials_lt.setVisibility(View.VISIBLE);
                        tv_insulators_details_lt.setVisibility(View.VISIBLE);
                        ch_tapping_cross_arms_lt.setChecked(true);
                        loadTappingXArmsLT();
                        if (ch_tapping_cross_arms_lt.isChecked()) {
                            layout_no_tapping_cross_arms_lt.setVisibility(View.VISIBLE);
                        } else {
                            layout_no_tapping_cross_arms_lt.setVisibility(View.GONE);
                        }

                        ch_pin_insulators_lt.setChecked(true);

                        if (ch_pin_insulators_lt.isChecked()) {
                            layout_no_of_pin_insulators_type_lt.setVisibility(View.VISIBLE);
                            layout_no_of_gi_pins_type_lt.setVisibility(View.VISIBLE);
                        } else {
                            layout_no_of_pin_insulators_type_lt.setVisibility(View.GONE);
                            layout_no_of_gi_pins_type_lt.setVisibility(View.GONE);
                        }

                        layout_no_of_pin_insulators_lt.setVisibility(View.VISIBLE);
                        layout_no_of_cut_insulators_lt.setVisibility(View.GONE);
                        layout_top_cleat_lt.setVisibility(View.GONE);
                        layout_v_crossing_lt.setVisibility(View.GONE);
                        layout_tapping_cross_arm_lt.setVisibility(View.VISIBLE);
                        layout_channel_lt.setVisibility(View.GONE);
                        layout_side_arm_lt.setVisibility(View.VISIBLE);


                    } else if (sp_pole_pin_cut_point_lt.getSelectedItem().toString().equalsIgnoreCase("Cut Point")) {
                        tv_fabrication_materials_lt.setVisibility(View.VISIBLE);
                        tv_insulators_details_lt.setVisibility(View.VISIBLE);
                        layout_ded_end_pole_lt.setVisibility(View.VISIBLE);
                        layout_no_of_pin_insulators_lt.setVisibility(View.VISIBLE);
                        layout_no_of_cut_insulators_lt.setVisibility(View.VISIBLE);
                        layout_side_arm_lt.setVisibility(View.GONE);
                        layout_top_cleat_lt.setVisibility(View.GONE);
                        layout_v_crossing_lt.setVisibility(View.GONE);
                        layout_tapping_cross_arm_lt.setVisibility(View.VISIBLE);
                        loadMetalPartsLT();
                    }
                } else {
                    layout_materials_lt.setVisibility(View.GONE);
                    tv_fabrication_materials_lt.setVisibility(View.GONE);
                    tv_insulators_details_lt.setVisibility(View.GONE);
                    layout_ded_end_pole_lt.setVisibility(View.GONE);
                    layout_no_of_pin_insulators_lt.setVisibility(View.GONE);
                    layout_no_of_cut_insulators_lt.setVisibility(View.GONE);
                    layout_side_arm_lt.setVisibility(View.GONE);
                    layout_top_cleat_lt.setVisibility(View.GONE);
                    layout_v_crossing_lt.setVisibility(View.GONE);
                    layout_tapping_cross_arm_lt.setVisibility(View.GONE);
                }


                break;


            case R.id.sp_lt_fuse_distribution_11kv:
                if (sp_lt_fuse_distribution_11kv.getSelectedItem().toString().equalsIgnoreCase("Distribution Box")) {
                    layout_lt_box_distribution_11kv.setVisibility(View.VISIBLE);
                    loadDistributionBoxData();

                } else {
                    layout_lt_box_distribution_11kv.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_dtr_phase_11:
                if (!sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {

                    if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("I")) {
                        phase = "I";
                        //loadDtrDescription(select);
                        layout_dtr_capacity.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase1_11.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase2_11.setVisibility(View.GONE);

                    } else if (sp_dtr_phase_11.getSelectedItem().toString().equalsIgnoreCase("III")) {
                        String select = "III";
                        phase = "III";
                        //loadDtrDescription(select);
                        layout_dtr_capacity.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase2_11.setVisibility(View.VISIBLE);
                        sp_dtr_capacity_phase1_11.setVisibility(View.GONE);
                    } else {
                        sp_dtr_capacity_phase1_11.setVisibility(View.GONE);
                        sp_dtr_capacity_phase2_11.setVisibility(View.GONE);
                        layout_dtr_capacity.setVisibility(View.GONE);
                    }
                } else {
                    layout_dtr_phase_desc_11kv.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_dtr_capacity_phase1_11:
                if (!sp_dtr_capacity_phase1_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_dtr_phase_desc_11kv.setVisibility(View.VISIBLE);
                    String capacity = sp_dtr_capacity_phase1_11.getSelectedItem().toString().trim();
                    if (capacity.equalsIgnoreCase("16 kVA")) {
                        capacity = "16";

                    }
                    loadDtrDescription(phase, capacity);

                } else {
                    layout_dtr_phase_desc_11kv.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_dtr_capacity_phase2_11:
                if (!sp_dtr_capacity_phase2_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_dtr_phase_desc_11kv.setVisibility(View.VISIBLE);
                    String capacity = sp_dtr_capacity_phase2_11.getSelectedItem().toString().trim();
                    if (capacity.equalsIgnoreCase("25 kVA")) {
                        capacity = "25";
                    } else if (capacity.equalsIgnoreCase("40 kVA")) {
                        capacity = "40";
                    } else if (capacity.equalsIgnoreCase("63 kVA")) {
                        capacity = "63";
                    } else if (capacity.equalsIgnoreCase("100 kVA")) {
                        capacity = "100";
                    } else if (capacity.equalsIgnoreCase("165 kVA")) {
                        capacity = "165";
                    } else if (capacity.equalsIgnoreCase("315 kVA")) {
                        capacity = "315";
                    } else if (capacity.equalsIgnoreCase("650 kVA")) {
                        capacity = "650";
                    }
                    loadDtrDescription(phase, capacity);

                } else {
                    layout_dtr_phase_desc_11kv.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_pole_support_33:
                if (!sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
                        layout_sp_no_of_pole_support_33.setVisibility(View.VISIBLE);


                        layout_sp_no_of_pole_stay_support_33.setVisibility(View.GONE);
                        tv_select_pole_no_text.setText("Select The Number of STRUT Poles*");

                        String poleType = sp_pole_type_33.getSelectedItem() != null ? sp_pole_type_33.getSelectedItem().toString() : "";
                        String poleHeight = sp_pole_height_33.getSelectedItem() != null ? sp_pole_height_33.getSelectedItem().toString() : "";

                        if (!poleType.isEmpty()) {
                            loadMainPoleStayClamps33KV(poleType, poleHeight);
                        } else {
                            Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                        }

                    } else if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STAY")) {
                        layout_sp_no_of_pole_support_33.setVisibility(View.GONE);
                        layout_sp_no_of_pole_stay_support_33.setVisibility(View.VISIBLE);
                        tv_select_pole_no_text.setText("No of STAYs*");
                        layout_main_pole_stay_clamps_33kv.setVisibility(View.GONE);

                    } else if (sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
                        String poleType = sp_pole_type_33.getSelectedItem() != null ? sp_pole_type_33.getSelectedItem().toString() : "";
                        String poleHeight = sp_pole_height_33.getSelectedItem() != null ? sp_pole_height_33.getSelectedItem().toString() : "";

                        if (!poleType.isEmpty()) {
                            loadMainPoleStayClamps33KV(poleType, poleHeight);
                        } else {
                            Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                        }

                        layout_sp_no_of_pole_support_33.setVisibility(View.VISIBLE);
                        layout_sp_no_of_pole_stay_support_33.setVisibility(View.VISIBLE);
                        tv_select_pole_no_text.setText("Select The Number of STRUT Poles*");
                        tv_select_pole_no_text_stay_33.setText("Select The Number of STAY*");
                    } else {
                        layout_sp_no_of_pole_support_33.setVisibility(View.GONE);
                        layout_sp_no_of_pole_stay_support_33.setVisibility(View.GONE);
                        layout_main_pole_stay_clamps_33kv.setVisibility(View.GONE);
                        ch_33kv_9_stuart_poles_33kv.setChecked(false);
                        ch_33kv_8_stuart_poles_33kv.setChecked(false);
                        //tv_stay_clamps_33kv.setVisibility(View.GONE);
                    }
                } else {
                    layout_main_pole_stay_clamps_33kv.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_pole_support_11:
                if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
                    layout_sp_no_of_pole_support_11.setVisibility(View.VISIBLE);
                    layout_sp_no_of_pole_stay_support_11.setVisibility(View.GONE);
                    tv_select_pole_no_text_11.setText("Select The Number of STRUT Poles*");


                    String poleType = sp_pole_type_11.getSelectedItem() != null ? sp_pole_type_11.getSelectedItem().toString() : "";
                    String poleHeight = sp_pole_height_11.getSelectedItem() != null ? sp_pole_height_11.getSelectedItem().toString() : "";

                    if (!poleType.isEmpty()) {
                        loadMainPoleStayClamps11KV(poleType, poleHeight);
                    } else {
                        Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                    }
                } else if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STAY")) {
                    layout_sp_no_of_pole_stay_support_11.setVisibility(View.VISIBLE);
                    layout_sp_no_of_pole_support_11.setVisibility(View.GONE);
                    layout_main_pole_stay_clamps_11kv.setVisibility(View.GONE);

                    tv_select_pole_no_text_11.setText(" Number of STAY*");
                } else if (sp_pole_support_11.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
                    layout_sp_no_of_pole_support_11.setVisibility(View.VISIBLE);
                    layout_sp_no_of_pole_stay_support_11.setVisibility(View.VISIBLE);
                    layout_main_pole_stay_clamps_11kv.setVisibility(View.GONE);

                    tv_select_pole_no_text_11.setText("Select The Number of STRUT Poles*");
                    tv_select_pole_no_text_stay_11.setText("Select The Number of STAY*");
                    String poleType = sp_pole_type_11.getSelectedItem() != null ? sp_pole_type_11.getSelectedItem().toString() : "";
                    String poleHeight = sp_pole_height_11.getSelectedItem() != null ? sp_pole_height_11.getSelectedItem().toString() : "";

                    if (!poleType.isEmpty()) {
                        loadMainPoleStayClamps11KV(poleType, poleHeight);
                    } else {
                        Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    layout_sp_no_of_pole_support_11.setVisibility(View.GONE);
                    layout_sp_no_of_pole_stay_support_11.setVisibility(View.GONE);
                    layout_main_pole_stay_clamps_11kv.setVisibility(View.GONE);
                    ch_33kv_9_stuart_poles_11kv.setChecked(false);
                    ch_33kv_8_stuart_poles_11kv.setChecked(false);
                    layout_main_pole_stay_clamps_11kv.setVisibility(View.GONE);

                }
                break;
            case R.id.sp_pole_support_lt:
                if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT")) {
                    layout_sp_no_of_pole_support_lt.setVisibility(View.VISIBLE);
                    layout_sp_no_of_pole_stay_support_lt.setVisibility(View.GONE);
                    tv_select_pole_no_text_lt.setText("Select The Number of STRUT Poles*");

                    String poleType = sp_pole_type_lt.getSelectedItem() != null ? sp_pole_type_lt.getSelectedItem().toString() : "";
                    String poleHeight = sp_pole_height_lt.getSelectedItem() != null ? sp_pole_height_lt.getSelectedItem().toString() : "";

                    if (!poleType.isEmpty()) {
                        loadMainPoleStayClampsLT(poleType, poleHeight);
                    } else {
                        Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                    }
                } else if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STAY")) {
                    layout_sp_no_of_pole_support_lt.setVisibility(View.GONE);
                    layout_sp_no_of_pole_stay_support_lt.setVisibility(View.VISIBLE);
                    tv_select_pole_no_text_lt.setText("Select The Number of STAY*");
                    layout_main_pole_stay_clamps_lt.setVisibility(View.GONE);
                } else if (sp_pole_support_lt.getSelectedItem().toString().equalsIgnoreCase("STRUT and STAY")) {
                    layout_sp_no_of_pole_support_lt.setVisibility(View.VISIBLE);
                    layout_sp_no_of_pole_stay_support_lt.setVisibility(View.VISIBLE);
                    tv_select_pole_no_text_lt.setText("Select The Number of STRUT Poles*");
                    tv_select_pole_no_text_stay_lt.setText("Select The Number of STAY*");
                    layout_main_pole_stay_clamps_lt.setVisibility(View.GONE);

                    String poleType = sp_pole_type_lt.getSelectedItem() != null ? sp_pole_type_lt.getSelectedItem().toString() : "";
                    String poleHeight = sp_pole_height_lt.getSelectedItem() != null ? sp_pole_height_lt.getSelectedItem().toString() : "";

                    if (!poleType.isEmpty()) {
                        loadMainPoleStayClampsLT(poleType, poleHeight);
                    } else {
                        Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    layout_sp_no_of_pole_support_lt.setVisibility(View.GONE);
                    layout_sp_no_of_pole_stay_support_lt.setVisibility(View.GONE);
                    layout_main_pole_stay_clamps_lt.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_survey_pole_type:
                if (sp_survey_pole_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_sp_voltage_level.setVisibility(View.GONE);
                }
                if (sp_survey_pole_type.getSelectedItem().toString().equalsIgnoreCase("Mapped Location")) {
                    layout_existing_connecting_point.setVisibility(View.VISIBLE);
                    layout_sp_voltage_level.setVisibility(View.VISIBLE);
                    if (is33KVSelected && !is11KVSelected && !isLTSelected) {
                        sp_voltage_level.setSelection(voltage_level.indexOf("33KV"), true);
                    } else if (!is33KVSelected && is11KVSelected && !isLTSelected) {
                        sp_voltage_level.setSelection(voltage_level.indexOf("11KV"), true);
                    } else if (!is33KVSelected && !is11KVSelected && isLTSelected) {
                        sp_voltage_level.setSelection(voltage_level.indexOf("LT"), true);
                    } else if (is33KVSelected && is11KVSelected && isLTSelected) {
                        sp_voltage_level.setSelection(voltage_level.indexOf("Select"), true);
                    }
                    Set<Integer> set_pst = new LinkedHashSet<>();

                    for (int i = 0; i < allList.size(); i++) {
                        try {
                            int poleNumber = Integer.parseInt(allList.get(i).getPOLE_NO());
                            set_pst.add(poleNumber);
                        } catch (NumberFormatException e) {
                            // Handle the case where POLE_NO is not a valid integer (optional)
                            e.printStackTrace();
                        }
                    }
                    List<Integer> list_pst_integers = new ArrayList<>(set_pst);
                    Collections.sort(list_pst_integers);
                    List<String> list_pst = new ArrayList<>();
                    for (int num : list_pst_integers) {
                        list_pst.add(String.valueOf(num));
                    }


                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_exsting_connecting_point.setAdapter(adapter_pst);
                    sp_exsting_connecting_point.setSelection(0, true);
                } else if (sp_survey_pole_type.getSelectedItem().toString().equalsIgnoreCase("Mapped Location")) {
                    if (allList.isEmpty()) {
                        poleNumber = 1;
                    } else {
                        int lastPoleNumber = Integer.parseInt(allList.get(allList.size() - 1).getPOLE_NO());

                        poleNumber = lastPoleNumber;

                        for (int i = 0; i < allList.size(); i++) {
                            int currentPoleNumber = Integer.parseInt(allList.get(i).getPOLE_NO());
                            if (currentPoleNumber > poleNumber) {
                                poleNumber = currentPoleNumber;
                            }
                        }

                        poleNumber++;


                    }
                    location_no.setText("" + poleNumber);
                    layout_sp_voltage_level.setVisibility(View.VISIBLE);
                    if (is33KVSelected && !is11KVSelected && !isLTSelected) {
                        sp_voltage_level.setSelection(voltage_level.indexOf("33KV"), true);
                    } else if (!is33KVSelected && is11KVSelected && !isLTSelected) {
                        sp_voltage_level.setSelection(voltage_level.indexOf("11KV"), true);
                    } else if (!is33KVSelected && !is11KVSelected && isLTSelected) {
                        sp_voltage_level.setSelection(voltage_level.indexOf("LT"), true);
                    } else if (is33KVSelected && is11KVSelected && isLTSelected) {
                        sp_voltage_level.setSelection(voltage_level.indexOf("Select"), true); // or choose one like "33KV"
                    }
                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                        layout_parent_exists.setVisibility(View.GONE);
                        survey_33.setVisibility(View.VISIBLE);
                        survey_11.setVisibility(View.GONE);
                        survey_lt.setVisibility(View.GONE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_33.setVisibility(View.VISIBLE);
                        layout_conductor_33.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (list33.isEmpty()) {
                            layout_connecting_point.setVisibility(View.GONE);
                        } else {
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);
                            List<String> list_pst = new ArrayList<>();

                            for (int i = 0; i < list33.size(); i++) {
                                list_pst.add(list33.get(i).getPOLE_NO());
                            }
                            // list_pst.add("Select");
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            // sp_connecting_point.setSelection(0, true);
                        }
                        layout_existing_connecting_point.setVisibility(View.GONE);

                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                        survey_33.setVisibility(View.GONE);
                        survey_11.setVisibility(View.VISIBLE);
                        survey_lt.setVisibility(View.GONE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_11.setVisibility(View.VISIBLE);
                        layout_conductor_11.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (list11.isEmpty()) {
                            layout_parent_exists.setVisibility(View.VISIBLE);
                            layout_connecting_point.setVisibility(View.GONE);
                        } else {
                            layout_parent_exists.setVisibility(View.GONE);
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);
                            List<String> list_pst = new ArrayList<>();
                            for (int i = 0; i < list11.size(); i++) {
                                list_pst.add(list11.get(i).getPOLE_NO());
                            }
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            sp_connecting_point.setSelection(0, true);
                        }
                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                        survey_33.setVisibility(View.GONE);
                        survey_11.setVisibility(View.GONE);
                        survey_lt.setVisibility(View.VISIBLE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_lt.setVisibility(View.VISIBLE);
                        layout_conductor_lt.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (listLT.isEmpty()) {
                            layout_parent_exists.setVisibility(View.VISIBLE);
                            layout_connecting_point.setVisibility(View.GONE);

                        } else {
                            layout_parent_exists.setVisibility(View.GONE);
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);

                            List<String> list_pst = new ArrayList<>();

                            for (int i = 0; i < list11.size(); i++) {
                                if (list11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                                    list_pst.add(list11.get(i).getPOLE_NO());
                                }
                            }

                            for (int i = 0; i < listLT.size(); i++) {
                                list_pst.add(listLT.get(i).getPOLE_NO());
                            }
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            sp_connecting_point.setSelection(0, true);
                        }
                    }
                } else if (sp_survey_pole_type.getSelectedItem().toString().equalsIgnoreCase("New Location")) {
                    layout_existing_connecting_point.setVisibility(View.GONE);
                    if (allList.isEmpty()) {
                        poleNumber = 1;
                    } else {
                        int lastPoleNumber = Integer.parseInt(allList.get(allList.size() - 1).getPOLE_NO());

                        poleNumber = lastPoleNumber;

                        for (int i = 0; i < allList.size(); i++) {
                            int currentPoleNumber = Integer.parseInt(allList.get(i).getPOLE_NO());
                            if (currentPoleNumber > poleNumber) {
                                poleNumber = currentPoleNumber;
                            }
                        }
                        poleNumber++;


                    }
                    location_no.setText("" + poleNumber);
                    layout_sp_voltage_level.setVisibility(View.VISIBLE);
                    sp_voltage_level.setVisibility(View.VISIBLE);


                    if (is33KVSelected && !is11KVSelected && !isLTSelected) {
                        if (isCompleted33kVServey) {
                            sp_voltage_level.setSelection(voltage_level.indexOf("Select"), true);
                        } else {
                            sp_voltage_level.setSelection(voltage_level.indexOf("33KV"), true);
                        }

                    } else if (!is33KVSelected && is11KVSelected && isLTSelected) {
                        if (isCompleted11kVServey) {
                            sp_voltage_level.setSelection(voltage_level.indexOf("LT"), true);
                        } else {
                            sp_voltage_level.setSelection(voltage_level.indexOf("11KV"), true);
                        }
                    } else if (is33KVSelected && is11KVSelected && isLTSelected) {
                        if (isCompleted33kVServey) {
                            sp_voltage_level.setSelection(voltage_level.indexOf("11KV"), true);
                        } else if (isCompleted11kVServey & isCompleted33kVServey) {
                            sp_voltage_level.setSelection(voltage_level.indexOf("LT"), true);
                        }
                    }


                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                        layout_parent_exists.setVisibility(View.GONE);
                        survey_33.setVisibility(View.VISIBLE);
                        survey_11.setVisibility(View.GONE);
                        survey_lt.setVisibility(View.GONE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_33.setVisibility(View.VISIBLE);
                        layout_conductor_33.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (list33.isEmpty()) {
                            layout_connecting_point.setVisibility(View.GONE);
                        } else {
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);
                            List<String> list_pst = new ArrayList<>();

                            for (int i = 0; i < list33.size(); i++) {
                                list_pst.add(list33.get(i).getPOLE_NO());
                            }
                            // list_pst.add("Select");
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            sp_connecting_point.setSelection(0, true);
                        }
                        layout_existing_connecting_point.setVisibility(View.GONE);

                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                        survey_33.setVisibility(View.GONE);
                        survey_11.setVisibility(View.VISIBLE);
                        survey_lt.setVisibility(View.GONE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_11.setVisibility(View.VISIBLE);
                        layout_conductor_11.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (list11.isEmpty()) {
                            layout_parent_exists.setVisibility(View.VISIBLE);
                            layout_connecting_point.setVisibility(View.GONE);
                        } else {
                            layout_parent_exists.setVisibility(View.GONE);
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);
                            List<String> list_pst = new ArrayList<>();
                            for (int i = 0; i < list11.size(); i++) {
                                list_pst.add(list11.get(i).getPOLE_NO());
                            }
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            sp_connecting_point.setSelection(0, true);
                        }
                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                        survey_33.setVisibility(View.GONE);
                        survey_11.setVisibility(View.GONE);
                        survey_lt.setVisibility(View.VISIBLE);
                        layout_survey_pole_type.setVisibility(View.VISIBLE);
                        layout_sp_pole_height_lt.setVisibility(View.VISIBLE);
                        layout_conductor_lt.setVisibility(View.VISIBLE);
                        layout_distance.setVisibility(View.GONE);


                        if (listLT.isEmpty()) {
                            layout_parent_exists.setVisibility(View.VISIBLE);
                            layout_connecting_point.setVisibility(View.GONE);

                        } else {
                            layout_parent_exists.setVisibility(View.GONE);
                            layout_connecting_point.setVisibility(View.VISIBLE);
                            layout_distance.setVisibility(View.VISIBLE);

                            List<String> list_pst = new ArrayList<>();

                            for (int i = 0; i < list11.size(); i++) {
                                if (list11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                                    list_pst.add(list11.get(i).getPOLE_NO());
                                }
                            }

                            for (int i = 0; i < listLT.size(); i++) {
                                list_pst.add(listLT.get(i).getPOLE_NO());
                            }
                            Collections.reverse(list_pst);
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                            sp_connecting_point.setAdapter(adapter_pst);
                            sp_connecting_point.setSelection(0, true);
                        }
                    }

                }
                break;


            case R.id.sp_exsting_connecting_point:
                if (sp_exsting_connecting_point.getSelectedItem() != null) {
                    existingselectedItem = sp_exsting_connecting_point.getSelectedItem().toString();
                    if (existingselectedItem.isEmpty()) {
                        if (allList.isEmpty()) {
                            poleNumber = 1;
                        } else {
                            int lastPoleNumber = Integer.parseInt(allList.get(allList.size() - 1).getPOLE_NO());

                            poleNumber = lastPoleNumber;

                            for (int i = 0; i < allList.size(); i++) {
                                int currentPoleNumber = Integer.parseInt(allList.get(i).getPOLE_NO());
                                if (currentPoleNumber > poleNumber) {
                                    poleNumber = currentPoleNumber;
                                }
                            }

                            poleNumber++;
                        }

                        location_no.setText("" + poleNumber);
                    } else {
                        poleNumber = Integer.parseInt(existingselectedItem);
                        location_no.setText("" + poleNumber);
                        ArrayList<NewModel> latAndLon = databaseEstimates.getLatAndLon(EST_NO, sp_exsting_connecting_point.getSelectedItem().toString(),GIS_ID);
                        ArrayList<NewModel> getData = databaseEstimates.getPoleDetails(EST_NO, sp_exsting_connecting_point.getSelectedItem().toString(),GIS_ID);
                        lattitude = latAndLon.get(0).getLATITUDE();
                        longitude = latAndLon.get(0).getLONGITUDE();
                        lineType = getData.get(0).getLINE_TYPE();
                        poleType = getData.get(0).getPOLE_TYPE();
                        isMultiCir = getData.get(0).getIS_MULTI_CKT();
                        poleHeiht = getData.get(0).getPOLE_HEIGHT();
                        poleSupport = getData.get(0).getPOLE_SUPPORT();
                        countOfSTRUT = getData.get(0).getNo_OF_STRUT();
                        countOfSTRUT = getData.get(0).getNo_OF_STAY();
                        if (poleSupport != null) {
                            if (poleSupport.equalsIgnoreCase("None")) {
                                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                    sp_pole_support_33.setSelection(1, true);
                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                    sp_pole_support_11.setSelection(1, true);

                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                    sp_pole_support_lt.setSelection(1, true);

                                }
                            } else if (poleSupport.equalsIgnoreCase("STRUT")) {
                                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                    sp_pole_support_33.setSelection(1, true);
                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                    sp_pole_support_11.setSelection(1, true);

                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                    sp_pole_support_lt.setSelection(1, true);

                                }
                            }
                        }

                        Log.d("POLE_HEIGHT---->", "onItemSelected: " + poleSupport);
                        Log.d("POLE_SUPPORT---->", "onItemSelected: " + countOfSTRUT);

                        if (poleType != null) {
                            if (poleType.equalsIgnoreCase("PSCC")) {
                                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                    sp_pole_type_33.setSelection(1, true);
                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                    sp_pole_type_11.setSelection(1, true);

                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                    sp_pole_type_lt.setSelection(1, true);

                                }
                            } else if (poleType.equalsIgnoreCase("SPUN")) {
                                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                    sp_pole_type_33.setSelection(2, true);
                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                    sp_pole_type_11.setSelection(2, true);

                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                    sp_pole_type_lt.setSelection(2, true);

                                }
                            }
                        }


                        if (poleHeiht != null) {
                            if (poleHeiht.equalsIgnoreCase("8.0")) {
                                if (poleType.equalsIgnoreCase("PSCC")) {
                                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                        sp_pole_height_33.setSelection(1, true);
                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                        sp_pole_height_11.setSelection(1, true);

                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                        sp_pole_height_lt.setSelection(1, true);

                                    }
                                }
                            } else if (poleHeiht.equalsIgnoreCase("9.1")) {
                                if (poleType.equalsIgnoreCase("PSCC")) {
                                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                        sp_pole_height_33.setSelection(2, true);
                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                        sp_pole_height_11.setSelection(2, true);

                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                        sp_pole_height_lt.setSelection(2, true);

                                    }
                                }
                            } else if (poleHeiht.equalsIgnoreCase("11")) {
                                assert poleType != null;
                                if (poleType.equalsIgnoreCase("PSCC")) {
                                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                        sp_pole_height_33.setSelection(3, true);
                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                        sp_pole_height_11.setSelection(3, true);

                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                        sp_pole_height_lt.setSelection(3, true);

                                    }
                                } else if (poleType.equalsIgnoreCase("SPUN")) {
                                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                        sp_pole_height_33.setSelection(2, true);
                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                        sp_pole_height_11.setSelection(2, true);

                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                        sp_pole_height_lt.setSelection(2, true);

                                    }
                                }
                            } else if (poleHeiht.equalsIgnoreCase("9.5")) {
                                if (poleType.equalsIgnoreCase("SPUN")) {
                                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                        sp_pole_height_33.setSelection(1, true);
                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                        sp_pole_height_11.setSelection(1, true);

                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                        sp_pole_height_lt.setSelection(1, true);

                                    }
                                }
                            } else if (poleHeiht.equalsIgnoreCase("12.5")) {
                                if (poleType.equalsIgnoreCase("SPUN")) {
                                    if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                        sp_pole_height_33.setSelection(3, true);
                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                        sp_pole_height_11.setSelection(3, true);

                                    } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                        sp_pole_height_lt.setSelection(3, true);

                                    }
                                }
                            }
                        }

                        if (lineType != null) {
                            if (lineType.equalsIgnoreCase("OverHead")) {
                                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                    sp_lineType_33.setSelection(0, true);
                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                    sp_lineType_11.setSelection(0, true);

                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                    sp_lineType_lt.setSelection(0, true);

                                }
                            } else {
                                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                                    sp_lineType_33.setSelection(1, true);
                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                                    sp_lineType_11.setSelection(1, true);
                                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                                    sp_lineType_lt.setSelection(1, true);
                                }
                            }
                        }
                        Log.d("LINETYPE", "onItemSelected: " + isMultiCir);
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(getData);
                        Log.d("LATLONG", "onItemSelected: " + json);
                    }
                } else {
                    if (allList.isEmpty()) {
                        poleNumber = 1;
                    } else {
                        int lastPoleNumber = Integer.parseInt(allList.get(allList.size() - 1).getPOLE_NO());

                        poleNumber = lastPoleNumber;

                        for (int i = 0; i < allList.size(); i++) {
                            int currentPoleNumber = Integer.parseInt(allList.get(i).getPOLE_NO());
                            if (currentPoleNumber > poleNumber) {
                                poleNumber = currentPoleNumber;
                            }
                        }

                        poleNumber++;
                    }

                    location_no.setText("" + poleNumber);
                }
                break;

            case R.id.sp_pole_type_33:
                poleType33 = sp_pole_type_33.getSelectedItem().toString();
                sp_pole_height_33.setSelection(0);

                if ("PSCC".equalsIgnoreCase(poleType33)) {
                    List<String> ugpsccList = new ArrayList<>();
                    ugpsccList.add("Select");
                    //ugpsccList.add("8.0");
                    ugpsccList.add("9.1");
                    ugpsccList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_height_33.setAdapter(adapter);
                } else if ("SPUN".equalsIgnoreCase(poleType33)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_33.setAdapter(adapter);

                } else if ("TOWER".equalsIgnoreCase(poleType33)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("M+3");
                    ugspunList.add("M+6");
                    ugspunList.add("M+9");
                    ugspunList.add("M+12");
                    ugspunList.add("R+6");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_33.setAdapter(adapter);

                } else if ("SS BAY".equalsIgnoreCase(poleType33)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_33.setAdapter(adapter);

                } else if ("RAIL".equalsIgnoreCase(poleType33)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_33.setAdapter(adapter);

                } else if ("RS JOIST".equalsIgnoreCase(poleType33)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.1");
                    ugspunList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_33.setAdapter(adapter);

                } else if ("TUBULAR POLE".equalsIgnoreCase(poleType33)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.1");
                    ugspunList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_33.setAdapter(adapter);

                }
                break;
            case R.id.sp_pole_type_11:
                poleType11 = sp_pole_type_11.getSelectedItem().toString();
                sp_pole_height_11.setSelection(0);
                if ("PSCC".equalsIgnoreCase(poleType11)) {
                    List<String> ugpsccList = new ArrayList<>();
                    ugpsccList.add("Select");
                    ugpsccList.add("8");
                    ugpsccList.add("9.1");
                    ugpsccList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_height_11.setAdapter(adapter);
                } else if ("SPUN".equalsIgnoreCase(poleType11)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_11.setAdapter(adapter);

                } else if ("TOWER".equalsIgnoreCase(poleType11)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("M+3");
                    ugspunList.add("M+6");
                    ugspunList.add("M+9");
                    ugspunList.add("M+12");
                    ugspunList.add("R+6");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_11.setAdapter(adapter);

                } else if ("SS BAY".equalsIgnoreCase(poleType11)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_11.setAdapter(adapter);

                } else if ("RAIL".equalsIgnoreCase(poleType11)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_11.setAdapter(adapter);

                } else if ("RS JOIST".equalsIgnoreCase(poleType11)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.1");
                    ugspunList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_11.setAdapter(adapter);

                } else if ("TUBULAR POLE".equalsIgnoreCase(poleType11)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.1");
                    ugspunList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_11.setAdapter(adapter);

                }
                break;
            case R.id.sp_pole_type_lt:
                poleTypelt = sp_pole_type_lt.getSelectedItem().toString();
                sp_pole_height_lt.setSelection(0);
                if ("PSCC".equalsIgnoreCase(poleTypelt)) {
                    List<String> ugpsccList = new ArrayList<>();
                    ugpsccList.add("Select");
                    ugpsccList.add("8");
                    ugpsccList.add("9.1");
                    ugpsccList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                    sp_pole_height_lt.setAdapter(adapter);
                } else if ("SPUN".equalsIgnoreCase(poleTypelt)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_lt.setAdapter(adapter);

                } else if ("TOWER".equalsIgnoreCase(poleTypelt)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("M+3");
                    ugspunList.add("M+6");
                    ugspunList.add("M+9");
                    ugspunList.add("M+12");
                    ugspunList.add("R+6");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_lt.setAdapter(adapter);

                } else if ("SS BAY".equalsIgnoreCase(poleTypelt)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_lt.setAdapter(adapter);

                } else if ("RAIL".equalsIgnoreCase(poleTypelt)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ugspunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_lt.setAdapter(adapter);

                } else if ("RS JOIST".equalsIgnoreCase(poleTypelt)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.1");
                    ugspunList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_lt.setAdapter(adapter);

                } else if ("TUBULAR POLE".equalsIgnoreCase(poleTypelt)) {
                    List<String> ugspunList = new ArrayList<>();
                    ugspunList.add("Select");
                    ugspunList.add("9.5");
                    ugspunList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, ugspunList);
                    sp_pole_height_lt.setAdapter(adapter);

                }
                break;

            case R.id.sp_pole_height_33:
                String selectedHeight = sp_pole_height_33.getSelectedItem().toString().trim();

                if (!selectedHeight.equalsIgnoreCase("Select")) {

                    String poleType = sp_pole_type_33.getSelectedItem() != null ? sp_pole_type_33.getSelectedItem().toString() : "";
                    String poleHeight = sp_pole_height_33.getSelectedItem() != null ? sp_pole_height_33.getSelectedItem().toString() : "";

                    if (!poleType.isEmpty()) {
                        loadClamps33KV(poleType, poleHeight);
                        loadMainPoleStayClamps33KV(poleType, poleHeight);
                    } else {
                        Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                    }


                    layout_sp_pole_desc_33.setVisibility(View.VISIBLE);

                    List<String> validHeights = Arrays.asList("8.0", "9.1", "11", "9.5", "12.5");

                    Log.d("Debugg_Spinner_Height", "Selected Height: " + selectedHeight);

                    if (validHeights.contains(selectedHeight)) {
                        String heightForDatabase = selectedHeight.equals("8.0") ? "8" : selectedHeight;
                        loadPoleHeightData(heightForDatabase, sp_pole_type_33.getSelectedItem().toString().trim());
                    } else {
                        layout_sp_pole_desc_33.setVisibility(View.GONE);
                        Log.d("Debugg_Spinner_Height", "Selected height not in the valid list.");
                    }
                } else {
                    layout_sp_pole_desc_33.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_pole_height_11:
                String selectedHeight11kv = sp_pole_height_11.getSelectedItem().toString().trim();

                if (!selectedHeight11kv.equalsIgnoreCase("Select")) {

                    String poleType = sp_pole_type_11.getSelectedItem() != null ? sp_pole_type_11.getSelectedItem().toString() : "";
                    String poleHeight = sp_pole_height_11.getSelectedItem() != null ? sp_pole_height_11.getSelectedItem().toString() : "";

                    if (!poleType.isEmpty()) {
                        loadClamps11KV(poleType, poleHeight);
                        loadMainPoleStayClamps11KV(poleType, poleHeight);
                    } else {
                        Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                    }


                    layout_sp_pole_desc_11.setVisibility(View.VISIBLE);

                    List<String> validHeights = Arrays.asList("8", "9.1", "11", "9.5", "12.5");

                    Log.d("Debugg_Spinner_Height", "Selected Height: " + selectedHeight11kv);

                    if (validHeights.contains(selectedHeight11kv)) {
                        String heightForDatabase = selectedHeight11kv.equals("8.0") ? "8" : selectedHeight11kv;
                        loadPoleHeightData11KV(heightForDatabase, sp_pole_type_11.getSelectedItem().toString().trim());
                    } else {
                        layout_sp_pole_desc_11.setVisibility(View.GONE);
                        Log.d("Debugg_Spinner_Height", "Selected height not in the valid list.");
                    }
                } else {
                    layout_sp_pole_desc_11.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_pole_height_lt:
                String selectedHeightLt = sp_pole_height_lt.getSelectedItem().toString().trim();

                if (!selectedHeightLt.equalsIgnoreCase("Select")) {

                    String poleType = sp_pole_type_lt.getSelectedItem() != null ? sp_pole_type_lt.getSelectedItem().toString() : "";
                    String poleHeight = sp_pole_height_lt.getSelectedItem() != null ? sp_pole_height_lt.getSelectedItem().toString() : "";

                    if (!poleType.isEmpty()) {
                        loadClampsLT(poleType, poleHeight);
                        loadMainPoleStayClampsLT(poleType, poleHeight);
                    } else {
                        Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                    }


                    layout_sp_pole_desc_lt.setVisibility(View.VISIBLE);

                    List<String> validHeights = Arrays.asList("8", "9.1", "11", "9.5", "12.5");

                    Log.d("Debugg_Spinner_Height", "Selected Height: " + selectedHeightLt);

                    if (validHeights.contains(selectedHeightLt)) {
                        String heightForDatabase = selectedHeightLt.equals("8.0") ? "8" : selectedHeightLt;
                        loadPoleHeightDataLT(heightForDatabase, sp_pole_type_lt.getSelectedItem().toString().trim());
                    } else {
                        layout_sp_pole_desc_lt.setVisibility(View.GONE);
                        Log.d("Debugg_Spinner_Height", "Selected height not in the valid list.");
                    }
                } else {
                    layout_sp_pole_desc_lt.setVisibility(View.GONE);
                }
                break;


            case R.id.sp_conductor_33:
                if (!sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("AAA CONDUCTOR")) {
                        String select = "AAA";

                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                        List<ConductorTypeModel> filteredModels = new ArrayList<>();

                        for (ConductorTypeModel model : models) {
                            if (model.getHT_33KV().equalsIgnoreCase("Y")) {
                                filteredModels.add(model);
                            }
                        }
                        loadConductorSizeSpinner(select, filteredModels);

                    }


                }
                if (sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("AB CABLE")) {
                    String select = "AB";
                    List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                    List<ConductorTypeModel> filteredModels = new ArrayList<>();

                    for (ConductorTypeModel model : models) {
                        if (model.getHT_33KV().equalsIgnoreCase("Y")) {
                            filteredModels.add(model);
                        }
                    }
                    loadConductorSizeSpinner(select, filteredModels);

                }

                if (sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("XLPE CABLE")) {
                    String select = "XLPE";
                    List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                    List<ConductorTypeModel> filteredModels = new ArrayList<>();

                    for (ConductorTypeModel model : models) {
                        if (model.getHT_33KV().equalsIgnoreCase("Y")) {
                            filteredModels.add(model);
                        }
                    }
                    loadConductorSizeSpinner(select, filteredModels);


                }
//                    if (sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("XLPE CABLE")) {
//                        String select = "XLPE";
//                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
//                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                        String json = gson.toJson(models);
//                        Log.d("XLPE_CABLE---->", "Fetched Data: " + json);
//                        for (int i = 0; i < models.size(); i++) {
//                            if (models.get(i).getHT_33KV().equalsIgnoreCase("Y")) {
//                                loadConductorSizeSpinner(select);
//                                layout_conductor_size_33.setVisibility(View.VISIBLE);
//                            } else {
//                                layout_conductor_size_33.setVisibility(View.GONE);
//                            }
//
//                        }
//                    }
                if (sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("PVC CABLE")) {
                    String select = "PVC";
                    List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                    List<ConductorTypeModel> filteredModels = new ArrayList<>();

                    for (ConductorTypeModel model : models) {
                        if (model.getHT_33KV().equalsIgnoreCase("Y")) {
                            filteredModels.add(model);
                        }
                    }
                    loadConductorSizeSpinner(select, filteredModels);

                }
                if (sp_conductor_33.getSelectedItem().toString().equalsIgnoreCase("ACSR CONDUCTOR")) {
                    String select = "ACSR";
                    List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                    List<ConductorTypeModel> filteredModels = new ArrayList<>();
                    for (ConductorTypeModel model : models) {
                        if (model.getHT_33KV().equalsIgnoreCase("Y")) {
                            filteredModels.add(model);
                        }
                    }
                    loadConductorSizeSpinner(select, filteredModels);


                }
                break;


            case R.id.sp_conductor_11:
                if (!sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("AAA CONDUCTOR")) {
                        String select = "AAA";
                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);

                        List<ConductorTypeModel> filteredModels = new ArrayList<>();
                        for (ConductorTypeModel model : models) {
                            if (model.getHT_11KV().equalsIgnoreCase("Y")) {
                                filteredModels.add(model);
                            }
                        }
                        loadConductorSizeSpinner11kV(select, filteredModels);

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(filteredModels);
                        Log.d("CONDUCTOR_TYPE---->", "Fetched Data: " + json);

                    }
                    if (sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("AB CABLE")) {
                        String select = "AB";
                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                        List<ConductorTypeModel> filteredModels = new ArrayList<>();
                        for (ConductorTypeModel model : models) {
                            if (model.getHT_11KV().equalsIgnoreCase("Y")) {
                                filteredModels.add(model);
                            }
                        }
                        loadConductorSizeSpinner11kV(select, filteredModels);
                    }
                    if (sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("XLPE CABLE")) {
                        String select = "XLPE";
                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                        List<ConductorTypeModel> filteredModels = new ArrayList<>();
                        for (ConductorTypeModel model : models) {
                            if (model.getHT_11KV().equalsIgnoreCase("Y")) {
                                filteredModels.add(model);
                            }
                        }
                        loadConductorSizeSpinner11kV(select, filteredModels);
                    }
                    if (sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("PVC CABLE")) {
                        String select = "PVC";
                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                        List<ConductorTypeModel> filteredModels = new ArrayList<>();
                        for (ConductorTypeModel model : models) {
                            if (model.getHT_11KV().equalsIgnoreCase("Y")) {
                                filteredModels.add(model);
                            }
                        }
                        loadConductorSizeSpinner11kV(select, filteredModels);
                    }
                    if (sp_conductor_11.getSelectedItem().toString().equalsIgnoreCase("ACSR CONDUCTOR")) {
                        String select = "ACSR";
                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                        List<ConductorTypeModel> filteredModels = new ArrayList<>();
                        for (ConductorTypeModel model : models) {
                            if (model.getHT_11KV().equalsIgnoreCase("Y")) {
                                filteredModels.add(model);
                            }
                        }
                        loadConductorSizeSpinner11kV(select, filteredModels);
                    }

                }
                break;

            case R.id.sp_conductor_lt:
                if (!sp_conductor_lt.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_conductor_lt.getSelectedItem().toString().equalsIgnoreCase("AAA CONDUCTOR")) {
                        String select = "AAA";
                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                        List<ConductorTypeModel> filteredModels = new ArrayList<>();
                        for (ConductorTypeModel model : models) {
                            if (model.getLT().equalsIgnoreCase("Y")) {
                                filteredModels.add(model);
                            }
                        }
                        loadConductorSizeSpinnerLT(select, filteredModels);

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(models);
                        Log.d("CONDUCTOR_TYPE---->", "Fetched Data: " + json);

                    }
                    if (sp_conductor_lt.getSelectedItem().toString().equalsIgnoreCase("AB CABLE")) {
                        String select = "AB";
                        List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                        List<ConductorTypeModel> filteredModels = new ArrayList<>();
                        for (ConductorTypeModel model : models) {
                            if (model.getLT().equalsIgnoreCase("Y")) {
                                filteredModels.add(model);
                            }
                        }
                        loadConductorSizeSpinnerLT(select, filteredModels);

                    }
                }
                if (sp_conductor_lt.getSelectedItem().toString().equalsIgnoreCase("XLPE CABLE")) {
                    String select = "XLPE";
                    List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                    List<ConductorTypeModel> filteredModels = new ArrayList<>();
                    for (ConductorTypeModel model : models) {
                        if (model.getLT().equalsIgnoreCase("Y")) {
                            filteredModels.add(model);
                        }
                    }
                    loadConductorSizeSpinnerLT(select, filteredModels);
                }
                if (sp_conductor_lt.getSelectedItem().toString().equalsIgnoreCase("PVC CABLE")) {
                    String select = "PVC";
                    List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);
                    List<ConductorTypeModel> filteredModels = new ArrayList<>();
                    for (ConductorTypeModel model : models) {
                        if (model.getLT().equalsIgnoreCase("Y")) {
                            filteredModels.add(model);
                        }
                    }
                    loadConductorSizeSpinnerLT(select, filteredModels);
                }
                if (sp_conductor_lt.getSelectedItem().toString().equalsIgnoreCase("ACSR CONDUCTOR")) {
                    String select = "ACSR";
                    List<ConductorTypeModel> models = databaseEstimates.getConductorTypeData(select);

                    List<ConductorTypeModel> filteredModels = new ArrayList<>();
                    for (ConductorTypeModel model : models) {
                        if (model.getLT().equalsIgnoreCase("Y")) {
                            filteredModels.add(model);
                        }
                    }
                    loadConductorSizeSpinnerLT(select, filteredModels);

                }
                break;


        }

    }

    private void loadDtrDescription(String phase, String capacity) {
        List<DTRTypesModel> model = databaseEstimates.getDTRTypeData(phase, capacity);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            //Log.d("Debugg_DTR_DESC_", "Fetched Data: " + json);
            ArrayAdapter<DTRTypesModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
            sp_dtr_phase_desc_11.setAdapter(adapter);
            sp_dtr_phase_desc_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dtrSapCode = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } else {
            layout_dtr_phase_desc_11kv.setVisibility(View.GONE);
        }

    }

    private void loadConductorSizeSpinnerLT(String select, List<ConductorTypeModel> models) {
        List<ConductorTypeModel> model = databaseEstimates.getConductorTypeData(select);
        if (!models.isEmpty()) {
            layout_conductor_size_lt.setVisibility(View.VISIBLE);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(models);
            Log.d("LTLTLTLTLTLT---", "Fetched Data: " + json);
            ArrayAdapter<ConductorTypeModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, models);
            sp_conductor_size_lt.setAdapter(adapter);
            sp_conductor_size_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    conductorSAPCodeLT = models.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_conductor_size_lt.setVisibility(View.GONE);
        }
    }


    private void loadConductorSizeSpinner11kV(String select, List<ConductorTypeModel> models) {
        List<ConductorTypeModel> model = databaseEstimates.getConductorTypeData(select);
        if (!models.isEmpty()) {
            layout_conductor_size_11.setVisibility(View.VISIBLE);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            //Log.d("Debugg_Spinner_Height", "Fetched Data: " + json);
            ArrayAdapter<ConductorTypeModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, models);
            sp_conductor_size_11.setAdapter(adapter);
            sp_conductor_size_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    conductorSAPCode11KV = models.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_conductor_size_11.setVisibility(View.GONE);
        }
    }


    private void loadConductorSizeSpinner(String select, List<ConductorTypeModel> models) {
        List<ConductorTypeModel> model = databaseEstimates.getConductorTypeData(select);
        if (!models.isEmpty()) {
            layout_conductor_size_33.setVisibility(View.VISIBLE);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(models);
            Log.d("CONDUCTOR_33KV===>", "Fetched Data: " + json);
            ArrayAdapter<ConductorTypeModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, models);
            sp_conductor_size_33.setAdapter(adapter);
            sp_conductor_size_33.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    conductorSAPCode33KV = models.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_conductor_size_33.setVisibility(View.GONE);
        }
    }

    private void loadPoleHeightData(String height, String poleType) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height, poleType);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            //Log.d("Debugg_Spinner_Height", "Fetched Data: " + json);
            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
            sp_sp_pole_desc_33.setAdapter(adapter);
            sp_sp_pole_desc_33.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    poleSapCode33kv = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_sp_pole_desc_33.setVisibility(View.GONE);
        }
    }

    private void loadPoleHeightData11KV(String height, String poleType) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height, poleType);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            //Log.d("Debugg_Spinner_Height", "Fetched Data: " + json);
            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
            sp_sp_pole_desc_11.setAdapter(adapter);
            sp_sp_pole_desc_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    poleSapCode11kv = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_sp_pole_desc_11.setVisibility(View.GONE);
        }
    }

    private void loadPoleHeightDataStrut9M33kv(String height) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            //Log.d("STRUAT_POLE_HEIGTH--->", "Fetched Data: " + json);
            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_sp_pole_desc_33_8_pole_support.setAdapter(adapter);
        } else {
            layout_sp_pole_desc_33_8_pole_support.setVisibility(View.GONE);
        }
//        if (!model.isEmpty()) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String json = gson.toJson(model);
//            // Log.d("STRUAT_POLE_HEIGTH--->", "Fetched Data: " + json);
//
//            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
//            sp_sp_pole_desc_33_8_pole_support.setAdapter(adapter);
//
//            // Set the spinner to the 0th position
//            sp_sp_pole_desc_33_8_pole_support.setSelection(0);
//        } else {
//            layout_sp_pole_desc_33_8_pole_support.setVisibility(View.GONE);
//        }
    }

    private void loadPoleHeightDataStrut9M11kv(String height) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            // Log.d("STRUAT_POLE_HEIGTH--->", "Fetched Data: " + json);
            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
            sp_sp_pole_desc_11kv_stuart_9m.setAdapter(adapter);
        } else {
            layout_sp_pole_desc_11kv_stuart_9m.setVisibility(View.GONE);
        }
    }


    private void loadPoleHeightDataStrut8M11kv(String height) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
        if (!model.isEmpty()) {
            List<String> abList = new ArrayList<>();
            for (int i = 0; i < model.size(); i++) {
                abList = Collections.singletonList(model.get(0).getMATERIALPART1());

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_sp_pole_desc_11kv_8_sturt.setAdapter(adapter);
        } else {
            layout_sp_pole_desc_11kv_8_sturt.setVisibility(View.GONE);
        }

    }


    private void loadPoleHeightDataStrut8MLT(String height) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
        if (!model.isEmpty()) {
            List<String> abList = new ArrayList<>();
            for (int i = 0; i < model.size(); i++) {
                abList = Collections.singletonList(model.get(0).getMATERIALPART1());

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_sp_pole_desc_lt_8_sturt.setAdapter(adapter);
        } else {
            layout_sp_pole_desc_lt_8_sturt.setVisibility(View.GONE);
        }

    }

//    private void loadPoleHeightDataStrut8M11kv(String height) {
//        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
//
//        if (!model.isEmpty()) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String json = gson.toJson(model);
//            // Log.d("STRUAT_POLE_HEIGTH--->", "Fetched Data: " + json);
//
//            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
//            sp_sp_pole_desc_11kv_8_sturt.setAdapter(adapter);
//
//            // Set the spinner to the 0th position
//            sp_sp_pole_desc_11kv_8_sturt.setSelection(0);
//        } else {
//            layout_sp_pole_desc_11kv_8_sturt.setVisibility(View.GONE);
//        }
//
////        if (!model.isEmpty()) {
////            Gson gson = new GsonBuilder().setPrettyPrinting().create();
////            String json = gson.toJson(model);
////            // Log.d("STRUAT_POLE_HEIGTH--->", "Fetched Data: " + json);
////            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
////            sp_sp_pole_desc_11kv_8_sturt.setAdapter(adapter);
////        } else {
////            layout_sp_pole_desc_11kv_8_sturt.setVisibility(View.GONE);
////        }
//    }

    private void loadPoleHeightDataStrut9MLT(String height) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            //Log.d("STRUAT_POLE_HEIGTH--->", "Fetched Data: " + json);
            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
            sp_sp_pole_desc_lt_stuart_9m.setAdapter(adapter);
        } else {
            layout_sp_pole_desc_lt_stuart_9m.setVisibility(View.GONE);
        }
    }

//    private void loadPoleHeightDataStrut8MLT(String height) {
//        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height);
//        if (!model.isEmpty()) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String json = gson.toJson(model);
//            //Log.d("STRUAT_POLE_HEIGTH--->", "Fetched Data: " + json);
//            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
//            sp_sp_pole_desc_lt_8_sturt.setAdapter(adapter);
//        } else {
//            layout_sp_pole_desc_lt_8_sturt.setVisibility(View.GONE);
//        }
//    }

    private void loadPoleHeightDataLT(String height, String poleType) {
        List<PoleHeightModel> model = databaseEstimates.getPoleHeightData(height, poleType);
        if (!model.isEmpty()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(model);
            // Log.d("Debugg_Spinner_Height", "Fetched Data: " + json);
            ArrayAdapter<PoleHeightModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
            sp_sp_pole_desc_lt.setAdapter(adapter);
            sp_sp_pole_desc_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    poleSapCodeLT = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            layout_sp_pole_desc_lt.setVisibility(View.GONE);
        }
    }


    private void loadABPoleData33KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataWithAbSwitch();
        List<String> abList = new ArrayList<>();
       // System.out.println("AB SWITCH SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String materialPart = model.get(i).getMATERIALPART1();
            if (materialPart.contains("AB SWITCH") && materialPart.contains("33KV")) {
                abList.add(materialPart);
            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_pole_ab_switch_type_33kv.setAdapter(adapter);
            sp_pole_ab_switch_type_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    abSwitch = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_ab_switch_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_ab_switch_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    private void loadVCrossArms33KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            String materialCode = model.get(i).getMATERIALCODE();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (groupDesc1.contains("FABRICATION MATERIAL")) {
                    if (voltage_level.equalsIgnoreCase("33KV") && material.equalsIgnoreCase("V CROSS ARMS")) {
                        abList.add(material1);
                        materialCodeMap.put(material1, materialCode);

                        Log.d("V CROSS ARMS", "loadMaterials33KV: " + abList);

                    }

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_of_v_crossing_arms_33kv.setAdapter(adapter);
            sp_type_of_v_crossing_arms_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedMaterialPart1 = abList.get(position);
                    vXArmCode33KV = materialCodeMap.get(selectedMaterialPart1);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_v_crossing_arms_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_v_crossing_arms_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadTopCleat33KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (groupDesc1.contains("FABRICATION MATERIAL")) {
                    if (voltage_level.equalsIgnoreCase("33KV") && material.equalsIgnoreCase("TOP CLEAT")) {
                        abList.add(material1);
                        materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                        Log.d("TOP CLEAT", "loadMaterials33KV: " + abList);

                    }

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_top_cleat_33kv.setAdapter(adapter);
            sp_type_top_cleat_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // topCleatCode33KV = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    topCleatCode33KV = materialCodeMap.get(selectedMaterialPart1);
                    Log.d("TOP_CLEAT_33_CODE", "onItemSelected: " + topCleatCode33KV);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_top_cleat_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_top_cleat_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadTappingXArms33KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());
        HashMap<String, String> materialCodeMap = new HashMap<>();


        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (groupDesc1.contains("FABRICATION MATERIAL")) {
                    if (voltage_level.equalsIgnoreCase("33KV") && material.equalsIgnoreCase("TAPPING CROSS ARMS")) {
                        abList.add(material1);
                        materialCodeMap.put(material1, model.get(i).getMATERIALCODE());

                        Log.d("TAPPING CROSS ARMS", "loadMaterials33KV: " + abList);

                    }

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_tapping_x_arm_33kv.setAdapter(adapter);
            sp_tapping_x_arm_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedMaterialPart1 = abList.get(position);
                    tappingCrossCode33KV = materialCodeMap.get(selectedMaterialPart1);

                    //tappingCrossCode33KV = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_tapping_cross_arms_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_tapping_cross_arms_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadChannels33KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (groupDesc1.contains("FABRICATION MATERIAL")) {
                    if (voltage_level.equalsIgnoreCase("33KV") && material.equalsIgnoreCase("CHANNEL")) {
                        abList.add(material1);
                        materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                        Log.d("CHANNEL", "loadMaterials33KV: " + abList);

                    }

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_channel_33kv.setAdapter(adapter);
            sp_channel_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //channel33KV = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    channel33KV = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_channel_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_channel_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    private void loadSideArm33KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("33KV") && material.equalsIgnoreCase("SIDE ARMS")) {
                    abList.add(material1);
                    Log.d("CHANNEL", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_side_arm_33kv.setAdapter(adapter);
            sp_side_arm_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sideArm33KVCode = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_side_arm_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_side_arm_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadPinInsulators33KV() {
        List<MaterialMasterModel> model = databaseEstimates.getPinInsulatorsFromMaterialData();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            String meterialCode = model.get(i).getMATERIALCODE();
            Log.d("PIN_INSULATORS", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("33KV") && material.equalsIgnoreCase("PIN INSULATORS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, meterialCode);

                    Log.d("PIN_INSULATORS_LIST", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_pin_insulators_type_33kv.setAdapter(adapter);
            // sp_pin_insulators_type_33kv.setSelection(1, true);
            sp_pin_insulators_type_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedMaterialPart1 = abList.get(position);
                    pinInsulatorCode33kv = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_of_pin_insulators_type_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_of_pin_insulators_type_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadDiscInsulators33KV(boolean checked) {
        if (checked) {
            List<MaterialMasterModel> model = databaseEstimates.getDiscInsulators();
            List<String> abList = new ArrayList<>();
            HashMap<String, String> materialCodeMap = new HashMap<>();

            //System.out.println("FABRIC SIZE TOTAL: " + model.size());

            for (int i = 0; i < model.size(); i++) {
                String groupDesc1 = model.get(i).getGROUPDESC1();
                String voltage_level = model.get(i).getVOLTAGE_LEVEL();
                String material = model.get(i).getMATERIAL();
                String material1 = model.get(i).getMATERIALPART1();
                String meterialCode = model.get(i).getMATERIALCODE();
                Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
                if (voltage_level != null) {
                    if (voltage_level.equalsIgnoreCase("33KV") && material.equalsIgnoreCase("DISC INSULATORS")) {
                        abList.add(material1);
                        materialCodeMap.put(material1, meterialCode);

                        Log.d("DISC INSULATORS------>", "loadMaterials33KV: " + abList);

                    }
                }

            }

            if (!abList.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                sp_type_disc_insulators_33kv.setAdapter(adapter);
                sp_type_disc_insulators_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String selectedMaterialPart1 = abList.get(position);
                        discInsulator33kv = materialCodeMap.get(selectedMaterialPart1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                layout_type_of_disc_insulator_33kv.setVisibility(View.VISIBLE);
            } else {
                layout_type_of_disc_insulator_33kv.setVisibility(View.GONE);
            }

            //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
        } else {
            layout_type_of_disc_insulator_33kv.setVisibility(View.GONE);

        }
    }

    private void loadMetalParts33KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMetalParts();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            String materialCode = model.get(i).getMATERIALCODE();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("33KV") && material.equalsIgnoreCase("METAL PARTS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, materialCode);

                    Log.d("METAL_PARTS_33KV------>", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_metal_33kv.setAdapter(adapter);
            sp_type_metal_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String selectedMaterialPart1 = abList.get(position);
                    metalPartCode33kv = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_type_of_metal_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_type_of_metal_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadClamps33KV(String poleType, String poleHeight) {
        List<MaterialMasterModel> model = databaseEstimates.getClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


        if (!poleType.isEmpty()) {
            //System.out.println("FABRIC SIZE TOTAL: " + model.size());

            for (MaterialMasterModel item : model) {
                String itemPoleType = item.getPOLE_TYPE();
                String material = item.getMATERIAL();
                String materialPart1 = item.getMATERIALPART1();
                String voltageLevel = item.getHT_33KV();
                String itemPoleHeight = item.getPOLE_HEIGHT();

                Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material + "POLE_HEIGHT:" + poleHeight);

                if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                    if ("BACK CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                        abList.add(materialPart1);

                        materialCodeMap.put(materialPart1, item.getMATERIALCODE());
                        Log.d("BACK_CLAMPS_33KV------>", "loadMaterials33KV (SPUN): " + abList);
                    }
                } else if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                    if ("BACK CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                        abList.add(materialPart1);
                        materialCodeMap.put(materialPart1, item.getMATERIALCODE());
                        Log.d("BACK_CLAMPS_33KV------>", "loadMaterials33KV (Non-SPUN): " + abList);
                    }
                }
            }

            if (!abList.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                sp_type_black_clamps_33kv.setAdapter(adapter);
                sp_type_black_clamps_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMaterialPart1 = abList.get(position);
                        clamp33kvCode = materialCodeMap.get(selectedMaterialPart1);
                        Log.d("BACK_CLAMP_CODE_33KV", "onItemSelected: " + clamp33kvCode);
                        //clamp33kvCode = model.get(position).getMATERIALCODE();
                        Log.d("BACK_CLAMP_CODE_33KV", "onItemSelected: " + clamp33kvCode);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle case when no selection is made
                    }
                });
                layout_type_of_black_clamps_33kv.setVisibility(View.VISIBLE);
            } else {
                layout_type_of_black_clamps_33kv.setVisibility(View.GONE);
            }

            //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
        } else {
            Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadStrutClamps33KV(String poleType, String poleHeight, boolean isChecked) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        if (isChecked) {
            if (!poleType.isEmpty()) {
                //System.out.println("FABRIC SIZE TOTAL: " + model.size());

                for (MaterialMasterModel item : model) {
                    String itemPoleType = item.getPOLE_TYPE();
                    String material = item.getMATERIAL();
                    String materialPart1 = item.getMATERIALPART1();
                    String voltageLevel = item.getHT_33KV();
                    String itemPoleHeight = item.getPOLE_HEIGHT();

                    Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

                    if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV (SPUN): " + abList);
                        }
                    } else if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV (Non-SPUN): " + abList);
                        }
                    }
                }

                if (!abList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                    sp_sturt_type_black_clamps_33kv.setAdapter(adapter);
                    sp_sturt_type_black_clamps_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String selectedMaterialPart1 = abList.get(position);
                            stayCode933KV = materialCodeMap.get(selectedMaterialPart1);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle case when no selection is made
                        }
                    });
                    layout_sturt_type_of_black_clamps_33kv.setVisibility(View.VISIBLE);
                } else {
                    layout_sturt_type_of_black_clamps_33kv.setVisibility(View.GONE);
                }

                //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
            } else {
                Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            }
        } else {
            layout_sturt_type_of_black_clamps_33kv.setVisibility(View.GONE);
        }


    }


    private void loadStrut8mClamps33KV(String poleType, String poleHeight, boolean isChecked) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        if (isChecked) {
            if (!poleType.isEmpty()) {
                //System.out.println("FABRIC SIZE TOTAL: " + model.size());

                for (MaterialMasterModel item : model) {
                    String itemPoleType = item.getPOLE_TYPE();
                    String material = item.getMATERIAL();
                    String materialPart1 = item.getMATERIALPART1();
                    String voltageLevel = item.getHT_33KV();
                    String itemPoleHeight = item.getPOLE_HEIGHT();

                    Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

                    if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV (SPUN): " + abList);
                        }
                    } else if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV (Non-SPUN): " + abList);
                        }
                    }
                }

                if (!abList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                    sp_sturt_8m_type_black_clamps_33kv.setAdapter(adapter);
                    sp_sturt_8m_type_black_clamps_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedMaterialPart1 = abList.get(position);
                            stayCode833KV = materialCodeMap.get(selectedMaterialPart1);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle case when no selection is made
                        }
                    });
                    layout_sturt_8m_type_of_black_clamps_33kv.setVisibility(View.VISIBLE);
                } else {
                    layout_sturt_8m_type_of_black_clamps_33kv.setVisibility(View.GONE);
                }

                //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
            } else {
                Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            }
        } else {
            layout_sturt_8m_type_of_black_clamps_33kv.setVisibility(View.GONE);
        }


    }

    private void loadMainPoleStayClamps33KV(String poleType, String poleHeight) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


        if (poleType.isEmpty() || "Select".equalsIgnoreCase(sp_pole_support_33.getSelectedItem().toString())) {
            layout_main_pole_stay_clamps_33kv.setVisibility(View.GONE);
            //Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            return;
        }

        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (MaterialMasterModel item : model) {
            String itemPoleType = item.getPOLE_TYPE();
            String material = item.getMATERIAL();
            String materialPart1 = item.getMATERIALPART1();
            String voltageLevel = item.getHT_33KV();
            String itemPoleHeight = item.getPOLE_HEIGHT();

            Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

            boolean isSpunType = "SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType);
            boolean isNonSpunType = poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight);
            boolean isValidMaterial = "STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel);

            if ((isSpunType || isNonSpunType) && isValidMaterial) {
                abList.add(materialPart1);
                materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV: " + abList);
            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_main_pole_stay_clamps_33kv.setAdapter(adapter);
            sp_main_pole_stay_clamps_33kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String selectedMaterialPart1 = abList.get(position);
                    stayCode33KV = materialCodeMap.get(selectedMaterialPart1);
                    Log.d("STAY_CLAMP_CODE_33KV", "onItemSelected: " + stayCode33KV);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle case when no selection is made
                }
            });
            layout_main_pole_stay_clamps_33kv.setVisibility(View.VISIBLE);
        } else {
            layout_main_pole_stay_clamps_33kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    //LT Materials

    private void loadSideArmLT() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("LT") && material.equalsIgnoreCase("SIDE ARMS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                    Log.d("CHANNEL", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_side_arm_lt.setAdapter(adapter);
            sp_side_arm_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //sideArmLtCode = model.get(position).getMATERIALCODE();


                    String selectedMaterialPart1 = abList.get(position);
                    sideArmLtCode = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_side_arm_lt.setVisibility(View.VISIBLE);
        } else {
            layout_no_side_arm_lt.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadPinInsulatorsLT() {
        List<MaterialMasterModel> model = databaseEstimates.getPinInsulatorsFromMaterialData();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("PIN_INSULATORS", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("LT") && material.equalsIgnoreCase("PIN INSULATORS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                    Log.d("PIN_INSULATORS_LIST", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_pin_insulators_type_lt.setAdapter(adapter);
            sp_pin_insulators_type_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // pinInsulatorCodeLT = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    pinInsulatorCodeLT = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_of_pin_insulators_type_lt.setVisibility(View.VISIBLE);
        } else {
            layout_no_of_pin_insulators_type_lt.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadTappingXArmsLT() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (groupDesc1.contains("FABRICATION MATERIAL")) {
                    if (voltage_level.equalsIgnoreCase("LT") && material.equalsIgnoreCase("LT CROSS ARMS")) {
                        abList.add(material1);
                        materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                        Log.d("TAPPING CROSS ARMS", "loadMaterials33KV: " + abList);

                    }

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_tapping_x_arm_lt.setAdapter(adapter);
            sp_tapping_x_arm_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // ltCrossArm = model.get(position).getMATERIALCODE();


                    String selectedMaterialPart1 = abList.get(position);
                    ltCrossArm = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_tapping_cross_arms_lt.setVisibility(View.VISIBLE);
        } else {
            layout_no_tapping_cross_arms_lt.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadDiscInsulatorsLt() {
        List<MaterialMasterModel> model = databaseEstimates.getShackles();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("LT") && material.equalsIgnoreCase("SHACKLES")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                    Log.d("SHACKLES INSULATORS-->", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_disc_insulators_lt.setAdapter(adapter);
            sp_type_disc_insulators_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //discInsulatorLT = model.get(position).getMATERIALCODE();


                    String selectedMaterialPart1 = abList.get(position);
                    discInsulatorLT = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_type_of_disc_insulator_lt.setVisibility(View.VISIBLE);
        } else {
            layout_type_of_disc_insulator_lt.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadMetalPartsLT() {
        List<MaterialMasterModel> model = databaseEstimates.getMetalParts();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("LT") && material.equalsIgnoreCase("METAL PARTS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                    Log.d("METAL_PARTS_33KV------>", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_metal_lt.setAdapter(adapter);
            sp_type_metal_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //metalPartCodeLT = model.get(position).getMATERIALCODE();


                    String selectedMaterialPart1 = abList.get(position);
                    metalPartCodeLT = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_type_of_metal_lt.setVisibility(View.VISIBLE);
        } else {
            layout_type_of_metal_lt.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    private void loadStrutClampsLT(String poleType, String poleHeight, boolean isChecked) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


        if (isChecked) {
            if (!poleType.isEmpty()) {
                //System.out.println("FABRIC SIZE TOTAL: " + model.size());

                for (MaterialMasterModel item : model) {
                    String itemPoleType = item.getPOLE_TYPE();
                    String material = item.getMATERIAL();
                    String materialPart1 = item.getMATERIALPART1();
                    String voltageLevel = item.getLT();
                    String itemPoleHeight = item.getPOLE_HEIGHT();

                    Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

                    if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_11KV------>", "loadMaterials11KV (SPUN): " + abList);
                        }
                    } else if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_11KV------>", "loadMaterials11KV (Non-SPUN): " + abList);
                        }
                    }
                }

                if (!abList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                    sp_sturt_type_black_clamps_lt.setAdapter(adapter);
                    sp_sturt_type_black_clamps_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedMaterialPart1 = abList.get(position);
                            stayCodeLT = materialCodeMap.get(selectedMaterialPart1);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle case when no selection is made
                        }
                    });
                    layout_sturt_type_of_black_clamps_lt.setVisibility(View.VISIBLE);
                } else {
                    layout_sturt_type_of_black_clamps_lt.setVisibility(View.GONE);
                }

                //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
            } else {
                Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            }
        } else {
            layout_sturt_type_of_black_clamps_lt.setVisibility(View.GONE);
        }


    }

    private void loadStrut8mClampsLT(String poleType, String poleHeight, boolean isChecked) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        if (isChecked) {
            if (!poleType.isEmpty()) {
                //System.out.println("FABRIC SIZE TOTAL: " + model.size());

                for (MaterialMasterModel item : model) {
                    String itemPoleType = item.getPOLE_TYPE();
                    String material = item.getMATERIAL();
                    String materialPart1 = item.getMATERIALPART1();
                    String voltageLevel = item.getLT();
                    String itemPoleHeight = item.getPOLE_HEIGHT();

                    Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

                    if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV (SPUN): " + abList);
                        }
                    } else if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV (Non-SPUN): " + abList);
                        }
                    }
                }

                if (!abList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                    sp_sturt_8m_type_black_clamps_lt.setAdapter(adapter);
                    sp_sturt_8m_type_black_clamps_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedMaterialPart1 = abList.get(position);
                            stayCodeLT = materialCodeMap.get(selectedMaterialPart1);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle case when no selection is made
                        }
                    });
                    layout_sturt_8m_type_of_black_clamps_lt.setVisibility(View.VISIBLE);
                } else {
                    layout_sturt_8m_type_of_black_clamps_lt.setVisibility(View.GONE);
                }

                //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
            } else {
                Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            }
        } else {
            layout_sturt_8m_type_of_black_clamps_lt.setVisibility(View.GONE);
        }


    }


    private void loadMainPoleStayClampsLT(String poleType, String poleHeight) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


        if (poleType.isEmpty() || "Select".equalsIgnoreCase(sp_pole_support_lt.getSelectedItem().toString())) {
            layout_main_pole_stay_clamps_lt.setVisibility(View.GONE);
            //Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            return;
        }

        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (MaterialMasterModel item : model) {
            String itemPoleType = item.getPOLE_TYPE();
            String material = item.getMATERIAL();
            String materialPart1 = item.getMATERIALPART1();
            String voltageLevel = item.getLT();
            String itemPoleHeight = item.getPOLE_HEIGHT();

            Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

            boolean isSpunType = "SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType);
            boolean isNonSpunType = poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight);
            boolean isValidMaterial = "STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel);

            if ((isSpunType || isNonSpunType) && isValidMaterial) {
                abList.add(materialPart1);
                materialCodeMap.put(materialPart1, item.getMATERIALCODE());


                Log.d("STAY_CLAMPS_LT------>", "loadMaterialsLT: " + abList);
            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_main_pole_stay_clamps_lt.setAdapter(adapter);
            sp_main_pole_stay_clamps_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedMaterialPart1 = abList.get(position);
                    stayCode11KV = materialCodeMap.get(selectedMaterialPart1);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle case when no selection is made
                }
            });
            layout_main_pole_stay_clamps_lt.setVisibility(View.VISIBLE);
        } else {
            layout_main_pole_stay_clamps_lt.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadClampsLT(String poleType, String poleHeight) {
        List<MaterialMasterModel> model = databaseEstimates.getClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        if (!poleType.isEmpty()) {
            //System.out.println("FABRIC SIZE TOTAL: " + model.size());

            for (MaterialMasterModel item : model) {
                String itemPoleType = item.getPOLE_TYPE();
                String material = item.getMATERIAL();
                String materialPart1 = item.getMATERIALPART1();
                String voltageLevel = item.getLT();
                String itemPoleHeight = item.getPOLE_HEIGHT();

                Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + materialPart1);

                if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                    if ("BACK CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                        abList.add(materialPart1);
                        materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                        Log.d("BACK_CLAMPS_LT------>", "loadMaterialsLT (SPUN): " + abList);
                    }
                }
                if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                    if ("BACK CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                        abList.add(materialPart1);
                        materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                        Log.d("BACK_CLAMPS_LT------>", "loadMaterialsLT (Non-SPUN): " + abList.size());
                    }
                }
            }

            if (abList.size() > 0) {
                layout_type_of_black_clamps_lt.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                sp_type_black_clamps_lt.setAdapter(adapter);
                sp_type_black_clamps_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMaterialPart1 = abList.get(position);
                        clampLtCode = materialCodeMap.get(selectedMaterialPart1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle case when no selection is made
                    }
                });

            } else {
                layout_type_of_black_clamps_lt.setVisibility(View.GONE);
            }

            //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
        } else {
            Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLTGIPins() {
        List<MaterialMasterModel> model = databaseEstimates.getGIPinsForLT();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADGIPINSLT", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("LT") && material.equalsIgnoreCase("GI PINS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                    Log.d("GI PINS------>", "loadGiPinsLT: " + abList);
                }
            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_gi_pins_type_lt.setAdapter(adapter);
            sp_gi_pins_type_lt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // ltGIPinCode = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    ltGIPinCode = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_of_gi_pins_type_lt.setVisibility(View.VISIBLE);
        } else {
            layout_no_of_gi_pins_type_lt.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    //11KV Materials

    private void loadABPoleData11KV(String voltage_level) {
        if (voltage_level == null) {
            Toast.makeText(context, "Select Voltage level", Toast.LENGTH_SHORT).show();
        } else {
            List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataWithAbSwitch();
            List<String> abList = new ArrayList<>();
            //System.out.println("AB SWITCH SIZE TOTAL: " + model.size());

            for (int i = 0; i < model.size(); i++) {
                String materialPart = model.get(i).getMATERIALPART1();
                if (materialPart.contains("AB SWITCH") && materialPart.contains("11KV")) {
                    abList.add(materialPart);
                }
            }

            if (!abList.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                sp_pole_ab_switch_type_11kv.setAdapter(adapter);
                sp_pole_ab_switch_type_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        abSwitch = model.get(position).getMATERIALCODE();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                layout_ab_switch_11kv.setVisibility(View.VISIBLE);
            } else {
                layout_ab_switch_11kv.setVisibility(View.GONE);
            }

            //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
        }

    }

    private void loadABPoleData63KV(String voltage_level) {
        if (voltage_level == null) {
            Toast.makeText(context, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
        } else {
            List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataWithAbSwitch();
            List<String> abList = new ArrayList<>();
            //System.out.println("AB SWITCH SIZE TOTAL: " + model.size());

            for (int i = 0; i < model.size(); i++) {
                String materialPart = model.get(i).getMATERIALPART1();
                if (materialPart.contains("AB SWITCH") && materialPart.contains("6.3KV")) {
                    abList.add(materialPart);
                    Log.d("AB_POLE_DATA_6.3KV", "loadABPoleData63KV: " + materialPart);
                }
            }

            if (!abList.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                sp_pole_ab_switch_type_11kv.setAdapter(adapter);
                sp_pole_ab_switch_type_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        abSwitch = model.get(position).getMATERIALCODE();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                layout_ab_switch_11kv.setVisibility(View.VISIBLE);
            } else {
                layout_ab_switch_11kv.setVisibility(View.GONE);
            }

            //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
        }

    }


    private void loadDTRABPoleData11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataWithAbSwitch();
        List<String> abList = new ArrayList<>();
        //System.out.println("AB SWITCH SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String materialPart = model.get(i).getMATERIALPART1();
            if (materialPart.contains("AB SWITCH") && materialPart.contains("11KV")) {
                abList.add(materialPart);
            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_pole_dtr_ab_switch_type_11kv.setAdapter(adapter);
            sp_pole_dtr_ab_switch_type_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dtrAbSwitch = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_dtr_ab_switch_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    private void loadDTRABPoleData63KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataWithAbSwitch();
        List<String> abList = new ArrayList<>();
        //System.out.println("AB SWITCH SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String materialPart = model.get(i).getMATERIALPART1();
            if (materialPart.contains("AB SWITCH") && materialPart.contains("6.3KV")) {
                abList.add(materialPart);
                //System.out.println("AB SWITCH SIZE TOTAL: " + materialPart);
            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_pole_dtr_ab_switch_type_11kv.setAdapter(adapter);
            sp_pole_dtr_ab_switch_type_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dtrAbSwitch = model.get(position).getMATERIALCODE();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_dtr_ab_switch_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_dtr_ab_switch_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadDistributionBoxData() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataWithDistributionBox();

        if (!model.isEmpty()) {
            ArrayAdapter<MaterialMasterModel> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, model);
            sp_lt_fuse_box_distribution_11kv.setAdapter(adapter);
            sp_lt_fuse_box_distribution_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    boxDistribution = model.get(position).getMATERIALCODE();
                    Log.d("BoxDistributionCode--->", "onItemSelected: " + boxDistribution);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_lt_box_distribution_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_lt_box_distribution_11kv.setVisibility(View.GONE);
        }
    }

    private void loadStrut8mClamps11KV(String poleType, String poleHeight, boolean isChecked) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        if (isChecked) {
            if (!poleType.isEmpty()) {
                //System.out.println("FABRIC SIZE TOTAL: " + model.size());

                for (MaterialMasterModel item : model) {
                    String itemPoleType = item.getPOLE_TYPE();
                    String material = item.getMATERIAL();
                    String materialPart1 = item.getMATERIALPART1();
                    String voltageLevel = item.getHT_33KV();
                    String itemPoleHeight = item.getPOLE_HEIGHT();

                    Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

                    if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV (SPUN): " + abList);
                        }
                    } else if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_33KV------>", "loadMaterials33KV (Non-SPUN): " + abList);
                        }
                    }
                }

                if (!abList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                    sp_sturt_8m_type_black_clamps_11kv.setAdapter(adapter);
                    sp_sturt_8m_type_black_clamps_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedMaterialPart1 = abList.get(position);
                            stayCode11KV = materialCodeMap.get(selectedMaterialPart1);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle case when no selection is made
                        }
                    });
                    layout_sturt_8m_type_of_black_clamps_11kv.setVisibility(View.VISIBLE);
                } else {
                    layout_sturt_8m_type_of_black_clamps_11kv.setVisibility(View.GONE);
                }

                //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
            } else {
                Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            }
        } else {
            layout_sturt_8m_type_of_black_clamps_11kv.setVisibility(View.GONE);
        }


    }


    private void loadStrutClamps11KV(String poleType, String poleHeight, boolean isChecked) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


        if (isChecked) {
            if (!poleType.isEmpty()) {
                //System.out.println("FABRIC SIZE TOTAL: " + model.size());

                for (MaterialMasterModel item : model) {
                    String itemPoleType = item.getPOLE_TYPE();
                    String material = item.getMATERIAL();
                    String materialPart1 = item.getMATERIALPART1();
                    String voltageLevel = item.getHT_11KV();
                    String itemPoleHeight = item.getPOLE_HEIGHT();

                    Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

                    if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_11KV------>", "loadMaterials11KV (SPUN): " + abList);
                        }
                    } else if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                        if ("STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                            abList.add(materialPart1);
                            materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                            Log.d("STAY_CLAMPS_11KV------>", "loadMaterials11KV (Non-SPUN): " + abList);
                        }
                    }
                }

                if (!abList.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                    sp_sturt_type_black_clamps_11kv.setAdapter(adapter);
                    sp_sturt_type_black_clamps_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedMaterialPart1 = abList.get(position);
                            stayCode11KV = materialCodeMap.get(selectedMaterialPart1);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle case when no selection is made
                        }
                    });
                    layout_sturt_type_of_black_clamps_11kv.setVisibility(View.VISIBLE);
                } else {
                    layout_sturt_type_of_black_clamps_11kv.setVisibility(View.GONE);
                }

                //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
            } else {
                Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            }
        } else {
            layout_sturt_type_of_black_clamps_11kv.setVisibility(View.GONE);
        }


    }


    private void loadMainPoleStayClamps11KV(String poleType, String poleHeight) {
        List<MaterialMasterModel> model = databaseEstimates.getSturtClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


        if (poleType.isEmpty() || "Select".equalsIgnoreCase(sp_pole_support_11.getSelectedItem().toString())) {
            layout_main_pole_stay_clamps_11kv.setVisibility(View.GONE);
            //Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
            return;
        }

        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (MaterialMasterModel item : model) {
            String itemPoleType = item.getPOLE_TYPE();
            String material = item.getMATERIAL();
            String materialPart1 = item.getMATERIALPART1();
            String voltageLevel = item.getHT_11KV();
            String itemPoleHeight = item.getPOLE_HEIGHT();

            Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + material);

            boolean isSpunType = "SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType);
            boolean isNonSpunType = poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight);
            boolean isValidMaterial = "STAY CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel);

            if ((isSpunType || isNonSpunType) && isValidMaterial) {
                abList.add(materialPart1);
                materialCodeMap.put(materialPart1, item.getMATERIALCODE());

                Log.d("STAY_CLAMPS_11KV------>", "loadMaterials11KV: " + abList);
            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_main_pole_stay_clamps_11kv.setAdapter(adapter);
            sp_main_pole_stay_clamps_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String selectedMaterialPart1 = abList.get(position);
                    stayCode11KV = materialCodeMap.get(selectedMaterialPart1);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle case when no selection is made
                }
            });
            layout_main_pole_stay_clamps_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_main_pole_stay_clamps_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadClamps11KV(String poleType, String poleHeight) {
        List<MaterialMasterModel> model = databaseEstimates.getClamps();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


        if (!poleType.isEmpty()) {
            //System.out.println("FABRIC SIZE TOTAL: " + model.size());

            for (MaterialMasterModel item : model) {
                String itemPoleType = item.getPOLE_TYPE();
                String material = item.getMATERIAL();
                String materialPart1 = item.getMATERIALPART1();
                String voltageLevel = item.getHT_11KV();
                String itemPoleHeight = item.getPOLE_HEIGHT();

                Log.d("DEBUG_POLE_TYPE", "POLE_TYPE: " + itemPoleType + ", MATERIAL: " + materialPart1);

                if ("SPUN".equalsIgnoreCase(poleType) && poleType.equalsIgnoreCase(itemPoleType)) {
                    if ("BACK CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                        abList.add(materialPart1);
                        materialCodeMap.put(materialPart1, item.getMATERIALCODE());
                        Log.d("BACK_CLAMPS_11KV------>", "loadMaterials11KV (SPUN): " + abList);
                    }
                }
                if (poleType.equalsIgnoreCase(itemPoleType) && poleHeight.equalsIgnoreCase(itemPoleHeight)) {
                    if ("BACK CLAMPS".equalsIgnoreCase(material) && "Y".equalsIgnoreCase(voltageLevel)) {
                        abList.add(materialPart1);
                        materialCodeMap.put(materialPart1, item.getMATERIALCODE());
                        Log.d("BACK_CLAMPS_11KV------>", "loadMaterials11KV (Non-SPUN): " + abList);
                    }
                }
            }

            if (!abList.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
                sp_type_black_clamps_11kv.setAdapter(adapter);
                sp_type_black_clamps_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMaterialPart1 = abList.get(position);
                        clamp11kvCode = materialCodeMap.get(selectedMaterialPart1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle case when no selection is made
                    }
                });
                layout_type_of_black_clamps_11kv.setVisibility(View.VISIBLE);
            } else {
                layout_type_of_black_clamps_11kv.setVisibility(View.GONE);
            }

            //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
        } else {
            Toast.makeText(context, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDiscInsulators11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getDiscInsulators();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("11KV") && material.equalsIgnoreCase("DISC INSULATORS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());

                    Log.d("DISC INSULATORS------>", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_disc_insulators_11kv.setAdapter(adapter);
            sp_type_disc_insulators_11kv.setSelection(1, true);
            sp_type_disc_insulators_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //discInsulator11kv = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    discInsulator11kv = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_type_of_disc_insulator_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_type_of_disc_insulator_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    private void loadVCrossArms11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material1 = model.get(i).getMATERIALPART1();
            String material = model.get(i).getMATERIAL();
            if (groupDesc1.contains("FABRICATION MATERIAL")) {
                if (voltage_level.equalsIgnoreCase("11KV") && material.equalsIgnoreCase("V CROSS ARMS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());

                    Log.d("V CROSS ARMS", "loadMaterials33KV: " + abList);

                }

            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_of_v_crossing_arms_11kv.setAdapter(adapter);
            sp_type_of_v_crossing_arms_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //vXArmCode11KV = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    vXArmCode11KV = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_v_crossing_arms_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_v_crossing_arms_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadMetalParts11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMetalParts();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("11KV") && material.equalsIgnoreCase("METAL PARTS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());


                    Log.d("DISC INSULATORS------>", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_metal_11kv.setAdapter(adapter);
            sp_type_metal_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //metalPartCode11kv = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    metalPartCode11kv = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_type_of_metal_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_type_of_metal_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadTopCleat11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();

        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material1 = model.get(i).getMATERIALPART1();
            String material = model.get(i).getMATERIAL();
            if (groupDesc1.contains("FABRICATION MATERIAL")) {
                if (voltage_level.equalsIgnoreCase("11KV") && material.equalsIgnoreCase("TOP CLEAT")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());
                    Log.d("SIDE ARMS", "loadSIDE ARMS33KV: " + abList);

                }

            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_type_top_cleat_11kv.setAdapter(adapter);
            sp_type_top_cleat_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //topCleatCode11KV = model.get(position).getMATERIALCODE();
                    String selectedMaterialPart1 = abList.get(position);
                    topCleatCode11KV = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_top_cleat_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_top_cleat_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadTappingCross11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material1 = model.get(i).getMATERIALPART1();
            String material = model.get(i).getMATERIAL();
            if (groupDesc1.contains("FABRICATION MATERIAL")) {
                if (voltage_level.equalsIgnoreCase("11KV") && material.equalsIgnoreCase("TAPPING CROSS ARMS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());

                    Log.d("SIDE ARMS", "loadSIDE ARMS11KV: " + abList);

                }

            }
        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_tapping_x_arm_11kv.setAdapter(adapter);
            sp_tapping_x_arm_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //tappingCrossCode11KV = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    tappingCrossCode11KV = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_tapping_cross_arms_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_tapping_cross_arms_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }

    private void loadChannels11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (groupDesc1.contains("FABRICATION MATERIAL")) {
                    if (voltage_level.equalsIgnoreCase("11KV") && material.equalsIgnoreCase("CHANNEL")) {
                        abList.add(material1);
                        materialCodeMap.put(material1, model.get(i).getMATERIALCODE());

                        Log.d("CHANNEL", "loadMaterials33KV: " + abList);

                    }

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_channel_11kv.setAdapter(adapter);
            sp_channel_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //  channel11KV = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    channel11KV = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_channel_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_channel_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    private void loadSideArm11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getMaterialMasterDataForFabricationMaterial();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("LOADCROSSARMS33KV", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (groupDesc1.contains("FABRICATION MATERIAL")) {
                    if (voltage_level.equalsIgnoreCase("11KV") && material.equalsIgnoreCase("SIDE ARMS")) {
                        abList.add(material1);
                        materialCodeMap.put(material1, model.get(i).getMATERIALCODE());

                        Log.d("CHANNEL", "loadMaterials33KV: " + abList);

                    }

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_side_arm_11kv.setAdapter(adapter);
            sp_side_arm_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // sideArm11KVCode = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    sideArm11KVCode = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_side_arm_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_side_arm_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    private void loadPinInsulators11KV() {
        List<MaterialMasterModel> model = databaseEstimates.getPinInsulatorsFromMaterialData();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();
        //System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (int i = 0; i < model.size(); i++) {
            String groupDesc1 = model.get(i).getGROUPDESC1();
            String voltage_level = model.get(i).getVOLTAGE_LEVEL();
            String material = model.get(i).getMATERIAL();
            String material1 = model.get(i).getMATERIALPART1();
            Log.d("PIN_INSULATORS", "loadVCrossArms33KV: " + voltage_level + " " + material + "");
            if (voltage_level != null) {
                if (voltage_level.equalsIgnoreCase("11KV") && material.equalsIgnoreCase("PIN INSULATORS")) {
                    abList.add(material1);
                    materialCodeMap.put(material1, model.get(i).getMATERIALCODE());

                    Log.d("PIN_INSULATORS_LIST", "loadMaterials33KV: " + abList);

                }
            }

        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_pin_insulators_type_11kv.setAdapter(adapter);
            sp_pin_insulators_type_11kv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // pinInsulatorCode11kv = model.get(position).getMATERIALCODE();

                    String selectedMaterialPart1 = abList.get(position);
                    pinInsulatorCode11kv = materialCodeMap.get(selectedMaterialPart1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            layout_no_of_pin_insulators_type_11kv.setVisibility(View.VISIBLE);
        } else {
            layout_no_of_pin_insulators_type_11kv.setVisibility(View.GONE);
        }

        //System.out.println("AB SWITCH SIZE FILTER: " + abList.size());
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private long createRandomUID() {
        UUID uuid = UUID.randomUUID();
        //System.out.println("UUID: " + uuid);
        long randomNumber = uuid.getLeastSignificantBits();
        randomNumber = Math.abs(randomNumber);
        //System.out.println("Random Number: " + randomNumber);
        return randomNumber;
    }

    private double distance(double lat1, double lon1, double lat2, double
            lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);

    }

    private void setHgFuseSetAdapter() {
        hg_fuse_list.clear();
        if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("6.3KV")) {
            hg_fuse_list.add("6.3KV S-PH HG FUSE SETS WITH INSULATORS");
        } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
            hg_fuse_list.add("11KV HG FUSE SETS WITH INSULATORS");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, hg_fuse_list);
        sp_hg_fuse_set_11kv.setAdapter(adapter);
    }

    private void setPoleTypeAdapter() {
        poleTypeSurvey.clear();
        if (allList.isEmpty()) {
            poleTypeSurvey.add("Select");
            poleTypeSurvey.add("New Location");
        } else {
            if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                poleTypeSurvey.add("New Location");
            } else {
                poleTypeSurvey.add("New Location");
                poleTypeSurvey.add("Mapped Location");

            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, poleTypeSurvey);
        sp_survey_pole_type.setAdapter(adapter);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void showDistanceWarningDialog(float distance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Warning!");
        int roundedDistance = Math.round(distance);

        SpannableString spannable1 = new SpannableString("Length from source pole = " + roundedDistance + "m\n");
        spannable1.setSpan(new StyleSpan(Typeface.BOLD), 0, spannable1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable1.setSpan(new ForegroundColorSpan(Color.RED), 0, spannable1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannable2 = new SpannableString("Pole to Pole Span should be less than" + max_length + "M");
        spannable2.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannable2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharSequence message = android.text.TextUtils.concat(spannable1, spannable2);

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dialog.dismiss();
                dismissDialog();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dismiss();
    }


}


//03221A0250@discom
//9440814374
//mobileapp.apepdcl@gmail.com