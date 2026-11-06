package com.apepdcl.gis_survey.pmi_old.module_11kv;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.apepdcl.gis_survey.Constants.MobileConstants;

import net.sourceforge.app.MainActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.ImpData;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIArcGISMapActivity11kv;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIDTRMasterDownloadActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIPoleSurveyActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofit11kv;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitClientInstance11KV;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRMaster_vo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiCktVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiRequestVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedRequestDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISuveyedResponseVo;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMICustomAllDialogs11KV extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final String x, y;
    public Activity c;
    private Button btn_add, btn_clear, btn_topclear;
    private EditText ed_pole_no, ed_frompole;
    private TextInputEditText edt_polepaintingcode, edt_remarks_overhead, edt_remarks_underground, edt_tappinglocation, edt_ugtappinglocation, edt_ugpolepaintingcode;
    public ImpData deligate;
    private PMIRetrofit11kv retrofitService;
    private ProgressDialog progressDialog;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    GraphicsOverlay currentgraphicsOverlay;
    Graphic graphic;
    Graphic currentGraphic;
    LocationDisplay locationDisplay;

    private Spinner sp_oh_ab_switch_dtr_pole, sp_oh_capacity_dtr_pole, sp_oh_phase_dtr_pole, sp_oh_dtr_structure_type_dtr_pole, sp_oh_section_name_dtr_pole, sp_ug_ab_switch_dtr_pole, sp_ug_capacity_dtr_pole, sp_ug_phase_dtr_pole, sp_ug_dtr_structure_type_dtr_pole, sp_ug_section_name_dtr_pole, sp_ugnooftappings, sp_ugtapingpoint, sp_ugpolesupport, sp_ugpoleheight, sp_ugpoletype, sp__underpolestructure, sp_undergisendpole, sp_nooftappings, sp_rmu, sp_straight_rmu, sp_ssname, sp_diffvoltage, sp__polestructure, sp_noofcircuit_underg, sp_cablesize, sp_lineType, sp_poletype, sp_poleheight, sp_noofcircuit, sp_polesupport, sp_tapingpoint, sp_conductorsize, sp_pinpoint, sp_poleconfiguration, sp_ismulticircute, sp_circutetype, sp_isendpole;
    private String ugssname = "", ugnooftappings = "", ugtapingpoint = "", ugpolesupport = "", ugpoleheight = "", ugpoletype = "", underpolestructure = "", undergisendpole = "", ssName = "", nooftappings = "", rmu = "", straight_rmu = "", ssId = "", ssname = "", diffvoltage = "", polestructure = "", noofcircuit_underg = "", cablesize = "", noofcircuit = "", lineType = "", endPole = "", isendpole = "", surveyType, poleType = "", poleHeight = "", noofPoles, feederName = "", poleSupport = "", tapingPoint, taping_Point = "", conductorsize = "", pinpoint = "", poleconfiguration = "", feederCode = "", tappingPoint, ismulticircute = "", circutetype = "";
    private PMI11kvArcGISDatabase arcGISDatabase;
    private List<PMISurveyVo> surveyVosList;
    private int maxSurveyCount, poleCountCheck;
    private LinearLayout lay_oh_dtr_pole_main, lay__ug_dtr_pole_main, lyt_circuit, lyt_overhead, lyt_underground, lyt_polestructure, lyt_diffvoltage, lyt_rmu, lyt_tapping, lyt_underpolestructure, lyt_ugpole, lyt_ugtapping;
    private TextView tv_sunname, tv_feedename;
    private Map mapData;
    private int copyFlag = 0;
    AutoCompleteTextView auto_oh_dtr_structure_dtr_pole;
    private AutoCompleteTextView auto_oh_make_dtr_pole, auto_ug_dtr_structure_dtr_pole, auto_ug_make_dtr_pole;
    private EditText ed_oh_location_name_dtr_pole, ed_ug_location_name_dtr_pole;
    private LinearLayout lay_ug_dtr_pole_main;
    private List<String> dtrsectionList = null;
    private String dtr_pole_ug_section_name;
    private List<String> dtr_ug_structureList = null;
    private List<PMIDTRMaster_vo> lsss;
    private ArrayList<String> dtrsectionList_oh = null;
    private String dtr_pole_oh_section_name;
    private List<String> dtr_oh_structureList = null;
    private Spinner sp_ug_voltage_level;
    private Spinner sp_oh_voltage_level;
    private LinearLayout lay_ab_cable_main;
    private Spinner sp_abc_derpolestructure;
    private Spinner sp_abc_poletype;
    private Spinner sp_abc_poleheight;
    private Spinner sp_abc_tapingpoint;
    private Spinner sp_abc_nooftappings;
    private EditText ed_abc_tapping_location_name;
    private Spinner sp_abc_ab_cable_size;
    private Spinner sp_abc_no_of_cable;
    private String sp_abc_pole_type_str;
    private String sp_oh_phase_dtr_pole_str;
    private String sp_ug_phase_dtr_pole_str;
    private EditText ed_oh_serial_number_dtr_pole;
    private EditText ed_ug_serial_number_dtr_pole;
    private Spinner sp_ug_no_of_lt_circuits;
    private Spinner sp_oh_no_of_lt_circuits;
    private LinearLayout lay_oh_ht_service_number;
    private AutoCompleteTextView auto_oh_ht_service_number, auto_ug_ht_service_number;
    private List<String> oh_ht_servicenoList = null;
    private Spinner sp_abc_dtr_capacity;
    private ArrayList<String> dtr_ug_sectionList = null;
    private LinearLayout lay_oh_line_capacitor_pc, lay_oh_auto_reclosure_pc;
    private Spinner sp_oh_auto_reclosure, sp_oh_line_capacitor;
    private String sp_oh_voltage_level_str;
    private Spinner sp_ug_line_capacitor, sp_ug_auto_reclosure;
    private LinearLayout lay_ug_ht_service_number, lay_ug_line_capacitor, lay_ug_auto_reclosure;
    private LinearLayout lyt_abc_tapping;
    private String abc_tapingpoint;
    private String abc_nooftappings;
    private LinearLayout lay_abc_line_capacitor, lay_abc_auto_reclosure;
    private LinearLayout lay_abc_ht_service_number;
    private Spinner sp_abc_line_capacitor, sp_abc_auto_reclosure;
    private AutoCompleteTextView auto_abc_ht_service_number;
    private List<String> abc_ht_servicenoList = null;
    private String sp_abc_location_typeStr;
    private Spinner sp_abc_dergisendpole;
    private Spinner sp_abc_noofcircuit;
    private Spinner sp_abc_straight_rmu;
    private LinearLayout lyt_abc_rmu;
    private LinearLayout lyt_abc_derpolestructure;
    private Spinner sp_abc_polesupport;
    private LinearLayout lay_abc_dtr_pole_main;
    private Spinner sp_abc_section_name_dtr_pole;
    private AutoCompleteTextView auto_abc_dtr_structure_dtr_pole;
    private Spinner sp_abc_dtr_structure_type_dtr_pole;
    private Spinner sp_abc_phase_dtr_pole;
    private Spinner sp_abc_capacity_dtr_pole;
    private AutoCompleteTextView auto_abc_make_dtr_pole;
    private EditText ed_abc_serial_number_dtr_pole;
    private EditText ed_abc_location_name_dtr_pole;
    private Spinner sp_abc_no_of_lt_circuits;
    private Spinner sp_abc_ab_switch_dtr_pole;
    private Spinner sp_abc_rmu;
    private TextInputEditText edt_abc_tappinglocation;
    private Spinner sp_abc_cablesize;
    private EditText edt_abc_remarks;
    private EditText edt_abc_polepaintingcode;
    private Spinner sp_abc_voltage_level;
    private String abc_isendpole = "";
    private String abc_noofcircuitStr;
    private String abc_straight_rmuStr;
    private LinearLayout lyt_abc_pole;
    private String sp_abc_rmuStr;
    private String sp_abc_derpolestructureStr;
    private String sp_abc_polesupportStr;
    private String dtr_pole_abc_section_name;
    private String sp_abc_phase_dtr_pole_str;
    private String sp_abc_nooftappingsStr;
    private String abc_cablesizeStr;
    private String abc_poleheightStr;
    private List<String> makeArrayList;
    private Spinner sp_oh_is_lt_ckts_available;
    private String oh_is_lt_ckts_availableStr;
    private LinearLayout lay_oh_no_of_lt_circuits;
    private Spinner sp_oh_dtr_location_type, sp_oh_type_of_load;
    private LinearLayout lay_oh_no_of_ab_switches;
    private Spinner sp_oh_no_of_ab_switches;
    private Spinner sp_ug_is_lt_ckts_available;
    private LinearLayout lay_ug_no_of_lt_circuits;
    private Spinner sp_ug_dtr_location_type;
    private Spinner sp_ug_type_of_load;
    private LinearLayout lay_ug_no_of_ab_switches;
    private Spinner sp_ug_no_of_ab_switches;
    private String ug_is_lt_ckts_availableStr;
    private Spinner sp_abc_is_lt_ckts_available, sp_abc_dtr_location_type;
    private Spinner sp_abc_type_of_load, sp_abc_no_of_ab_switches;
    private LinearLayout lay_abc_no_of_lt_circuits, lay_abc_no_of_ab_switches;
    private String abc_is_lt_ckts_availableStr;
    private List<String> makeListDatabase = null;
    private String sp_oh_type_of_loadStr;
    private String sp_ug_type_of_loadStr;
    private String sp_abc_type_of_loadStr;
    private Spinner sp_oh_is_dtr_avalabile;
    private String oh_is_dtr_avalabileStr;
    private LinearLayout lay_oh_is_dtr_avalable;
    private Spinner sp_ug_is_dtr_avalabile;
    private LinearLayout lay_ug_is_dtr_avalable;
    private String ug_is_dtr_avalabileStr;


    private List<PMISurveyVo> surveyVoList;
    private String sp_poleheight_KEY = "N";
    Button btn_repeat_pole;


    Spinner sp_Survey_type;
    private String Survey_type_string = "";

    //DISTANCE
    double currentDistance, lastPoleDistance;
    TextView last_pole_distance, current_distance;


    //DEFECTS
    Spinner sp_oh_defects, sp_pole_defects, sp_conductors, sp_ab_switch_defects, sp_insulators, sp_cross_arm, sp_top_cleat, sp_strut_pole, sp_stray_wire,
            sp_ht_clearance, sp_int_pole, sp_restringing, sp_tree_clearance;
    CheckBox ch_pole_defects, ch_conductors, ch_ab_switch_defects, ch_insulators, ch_cross_arm, ch_top_cleat, ch_strut_pole, ch_stray_wire, ch_ht_clearance, ch_int_pole,
            ch_restringing, ch_tree_clearance, ch_other_defects;
    LinearLayout defects_layout;
    EditText ed_other_defects;
    String defect_selected;

    //CROSSING
    // Spinner sp_crossing;
    CheckBox ch_crossing;

    //DTR CHECKING
    String selected_dtr_code;
    String from;

    //IMAGES
    LinearLayout images_layout;
    public static ImageView image_1, image_2, image_3;
    public static TextView upload_image_1, upload_image_2, upload_image_3;
    public static final int CAMERA_RESULT_CODE_IMAGE_1 = 1;
    public static final int CAMERA_RESULT_CODE_IMAGE_2 = 2;
    public static final int CAMERA_RESULT_CODE_IMAGE_3 = 3;

    public static final int GALLERY_RESULT_CODE_IMAGE_1 = 4;
    public static final int GALLERY_RESULT_CODE_IMAGE_2 = 5;
    public static final int GALLERY_RESULT_CODE_IMAGE_3 = 6;

    public static String image_1_base64, image_2_base64, image_3_base64;
    public static boolean image_1_clicked = false;
    public static boolean image_2_clicked = false;
    public static boolean image_3_clicked = false;

    public static String currentPhotoPath_1,currentPhotoPath_2;


    //MULTI CKT

    LinearLayout lyt_oh_multivoltage;
    private int noof_33kvCkts, noof_11kvCkts, noof_ltCkts;

    //11 KV
    LinearLayout lyt_oh_11kv_vlckts;
    CheckBox ch_oh_11kv_vlckts;
    Spinner sp_oh_no_of_11_feeders;
    String str_no_of_feeders;
    Spinner sp_oh_11_feeder_1, sp_oh_11_feeder_1_pole, sp_oh_11_feeder_2, sp_oh_11_feeder_2_pole, sp_oh_11_feeder_3, sp_oh_11_feeder_3_pole;
    TextView ed_oh_11_feeder_1, ed_oh_11_feeder_2, ed_oh_11_feeder_3;
    TextView ed_oh_11_feeder_1_ns, ed_oh_11_feeder_2_ns, ed_oh_11_feeder_3_ns;
    LinearLayout main_layout_feeder_1, layout_oh_11_feeder_1, main_layout_feeder_2;
    LinearLayout layout_oh_11_feeder_2, main_layout_feeder_3, layout_oh_11_feeder_3;
    TextView get_oh_11_feeder_1, get_oh_11_feeder_2, get_oh_11_feeder_3;
    EditText ed_oh_11_pole_1, ed_oh_11_pole_2, ed_oh_11_pole_3;
    LinearLayout feeder_1_pole_layout_11, feeder_2_pole_layout_11, feeder_3_pole_layout_11;


    //33 KV
    LinearLayout lyt_oh_33kv_vlckts;
    CheckBox ch_oh_33kv_vlckts;
    Spinner sp_oh_no_of_33_feeders;
    Spinner sp_oh_33_feeder_1, sp_oh_33_feeder_1_pole, sp_oh_33_feeder_2, sp_oh_33_feeder_2_pole, sp_oh_33_feeder_3, sp_oh_33_feeder_3_pole;
    TextView ed_oh_33_feeder_1, ed_oh_33_feeder_2, ed_oh_33_feeder_3;
    TextView ed_oh_33_feeder_1_ns, ed_oh_33_feeder_2_ns, ed_oh_33_feeder_3_ns;
    LinearLayout main_layout_feeder_1_33, layout_oh_33_feeder_1, main_layout_feeder_2_33;
    LinearLayout layout_oh_33_feeder_2, main_layout_feeder_3_33, layout_oh_33_feeder_3;
    TextView get_oh_33_feeder_1, get_oh_33_feeder_2, get_oh_33_feeder_3;
    EditText ed_oh_33_pole_1, ed_oh_33_pole_2, ed_oh_33_pole_3;
    LinearLayout feeder_1_pole_layout_33, feeder_2_pole_layout_33, feeder_3_pole_layout_33;

    //LT
    LinearLayout lyt_oh_lt_vlckts;
    CheckBox ch_oh_lt_vlckts;
    Spinner sp_oh_no_of_lt_feeders;
    Spinner sp_oh_lt_feeder_1, sp_oh_lt_feeder_1_pole, sp_oh_lt_feeder_2, sp_oh_lt_feeder_2_pole, sp_oh_lt_feeder_3, sp_oh_lt_feeder_3_pole;
    TextView ed_oh_lt_feeder_1, ed_oh_lt_feeder_2, ed_oh_lt_feeder_3;
    TextView ed_oh_lt_feeder_1_ns, ed_oh_lt_feeder_2_ns, ed_oh_lt_feeder_3_ns;
    LinearLayout main_layout_feeder_1_lt, layout_oh_lt_feeder_1, main_layout_feeder_2_lt;
    LinearLayout layout_oh_lt_feeder_2, main_layout_feeder_3_lt, layout_oh_lt_feeder_3;
    TextView get_oh_lt_feeder_1, get_oh_lt_feeder_2, get_oh_lt_feeder_3;
    EditText ed_oh_lt_pole_1, ed_oh_lt_pole_2, ed_oh_lt_pole_3;
    LinearLayout feeder_1_pole_layout_lt, feeder_2_pole_layout_lt, feeder_3_pole_layout_lt;

    //OTHER CABLES
    Spinner sp_oh_other_cables;
    EditText edt_oh_other_cables;

    //CROSSING
    LinearLayout lyt_oh_crossing;
    CheckBox ch_oh_6_3_crossing, ch_oh_11kv_crossing, ch_oh_33kv_crossing, ch_oh_lt_crossing, ch_oh_road_crossing;
    CheckBox ch_oh_tc_crossing, ch_oh_tm_crossing;
    LinearLayout lyt_oh_6_3_crossing, lyt_oh_11kv_crossing, lyt_oh_33kv_crossing, lyt_oh_lt_crossing;
    LinearLayout lyt_oh_tc_crossing, lyt_oh_tm_crossing;
    TextView edt_oh_6_3_crossing, edt_oh_11kv_crossing, edt_oh_33kv_crossing, edt_oh_lt_crossing;
    Spinner sp_crossing;
    TextInputEditText edt_oh_tc_crossing, edt_oh_tm_crossing;
    EditText ed_crossing_remarks;

    //REPEAT POLE NEW
    String poleHeightRepeat;
    boolean repeatPoleClicked = false;
    String not;

    //JHC PHASE 1
    Spinner sp_pole_survey_type, sp_line_survey_type, sp_oh_dtr_survey_type, sp_oh_lt_survey_type;
    LinearLayout jhc_layout, lyt_oh_dtr_survey_type, lyt_oh_lt_survey_type;

    //POLE CONCRETE
    Spinner sp_pole_concrete;
    String pole_concrete;

    //CURRENT LOCATION
    double CURRENT_LAT;
    double CURRENT_LONG;

    boolean isFirstPole;

    //NEW UNDERGROUND
    Spinner sp_rmu_make;
    EditText ed_rmu_serial_number;

    //NEW IMAGE OPEN CAMERA
    public static Bitmap capturedBitmap1 = null;
    public static boolean isImageCaptured1 = false;
    public static boolean photoClicked1 = false;

    public static Bitmap capturedBitmap2 = null;
    public static boolean isImageCaptured2 = false;
    public static boolean photoClicked2 = false;

    public PMICustomAllDialogs11KV(Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay,
                            Graphic graphic, String feederName, String feederCode, int maxSurveyCount, String tappingPoint,
                            String surveyType, GraphicsOverlay currentgraphicsOverlay, Graphic currentGraphic, String ssName,
                            double lastPoleDistance, double currentDistance, String from,
                            double CURRENT_LAT, double CURRENT_LONG, boolean isFirstPole
    ) {
        super(a);
        this.c = a;
        this.x = x;
        this.y = y;
        this.callout = callout;
        this.graphicsOverlay = graphicsOverlay;
        this.currentgraphicsOverlay = currentgraphicsOverlay;
        this.graphic = graphic;
        this.currentGraphic = currentGraphic;
        this.feederName = feederName;
        this.feederCode = feederCode;
        this.maxSurveyCount = maxSurveyCount;
        this.tappingPoint = tappingPoint;
        this.surveyType = surveyType;
        this.ssName = ssName;
        this.lastPoleDistance = lastPoleDistance;
        this.currentDistance = currentDistance;
        this.from = from;
        this.CURRENT_LAT = CURRENT_LAT;
        this.CURRENT_LONG = CURRENT_LONG;
        this.isFirstPole = isFirstPole;

        isImageCaptured1 = false;
        isImageCaptured2 = false;
        photoClicked1 = false;
        photoClicked2 = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pmicustom_all_dialogs11_kv);
        try {
            setCancelable(false);
            progressDialog = CommanUtilits.progressDialog(c);

            //UNDER GROUND
            sp_rmu_make = findViewById(R.id.sp_rmu_make);
            ed_rmu_serial_number = findViewById(R.id.ed_rmu_serial_number);

            //JHC PHASE 1
            jhc_layout = findViewById(R.id.jhc_layout);
            sp_pole_survey_type = findViewById(R.id.sp_pole_survey_type);
            sp_line_survey_type = findViewById(R.id.sp_line_survey_type);
            sp_oh_dtr_survey_type = findViewById(R.id.sp_oh_dtr_survey_type);
            sp_oh_lt_survey_type = findViewById(R.id.sp_oh_lt_survey_type);
            lyt_oh_dtr_survey_type = findViewById(R.id.lyt_oh_dtr_survey_type);
            lyt_oh_lt_survey_type = findViewById(R.id.lyt_oh_lt_survey_type);

            if (LoginActivity.isJHC) {
                jhc_layout.setVisibility(View.VISIBLE);
            }

            images_layout = findViewById(R.id.images_layout);
            image_1 = findViewById(R.id.image_1);
            image_2 = findViewById(R.id.image_2);
            image_3 = findViewById(R.id.image_3);
            upload_image_1 = findViewById(R.id.upload_image_1);
            upload_image_2 = findViewById(R.id.upload_image_2);
            upload_image_3 = findViewById(R.id.upload_image_3);

            //POLE CONCRETE
            sp_pole_concrete = findViewById(R.id.sp_pole_concrete);

            //CROSSING
            lyt_oh_crossing = findViewById(R.id.lyt_oh_crossing);
            ch_oh_6_3_crossing = findViewById(R.id.ch_oh_6_3_crossing);
            ch_oh_11kv_crossing = findViewById(R.id.ch_oh_11kv_crossing);
            ch_oh_33kv_crossing = findViewById(R.id.ch_oh_33kv_crossing);
            ch_oh_lt_crossing = findViewById(R.id.ch_oh_lt_crossing);
            ch_oh_road_crossing = findViewById(R.id.ch_oh_road_crossing);
            ch_oh_tc_crossing = findViewById(R.id.ch_oh_tc_crossing);
            ch_oh_tm_crossing = findViewById(R.id.ch_oh_tm_crossing);
            lyt_oh_6_3_crossing = findViewById(R.id.lyt_oh_6_3_crossing);
            lyt_oh_11kv_crossing = findViewById(R.id.lyt_oh_11kv_crossing);
            lyt_oh_33kv_crossing = findViewById(R.id.lyt_oh_33kv_crossing);
            lyt_oh_lt_crossing = findViewById(R.id.lyt_oh_lt_crossing);
            lyt_oh_tc_crossing = findViewById(R.id.lyt_oh_tc_crossing);
            lyt_oh_tm_crossing = findViewById(R.id.lyt_oh_tm_crossing);
            edt_oh_6_3_crossing = findViewById(R.id.edt_oh_6_3_crossing);
            edt_oh_11kv_crossing = findViewById(R.id.edt_oh_11kv_crossing);
            edt_oh_33kv_crossing = findViewById(R.id.edt_oh_33kv_crossing);
            edt_oh_lt_crossing = findViewById(R.id.edt_oh_lt_crossing);
            sp_crossing = findViewById(R.id.sp_crossing);
            edt_oh_tc_crossing = findViewById(R.id.edt_oh_tc_crossing);
            edt_oh_tm_crossing = findViewById(R.id.edt_oh_tm_crossing);
            ed_crossing_remarks = findViewById(R.id.ed_crossing_remarks);

            //OTHER CABLES
            sp_oh_other_cables = findViewById(R.id.sp_oh_other_cables);
            edt_oh_other_cables = findViewById(R.id.edt_oh_other_cables);

            //MULTI CKT
            lyt_oh_multivoltage = findViewById(R.id.lyt_oh_multivoltage);
            //MULTI CKT 11KV
            ch_oh_11kv_vlckts = findViewById(R.id.ch_oh_11kv_vlckts);
            lyt_oh_11kv_vlckts = findViewById(R.id.lyt_oh_11kv_vlckts);
            sp_oh_no_of_11_feeders = findViewById(R.id.sp_oh_no_of_11_feeders);
            sp_oh_11_feeder_1 = findViewById(R.id.sp_oh_11_feeder_1);
            sp_oh_11_feeder_1_pole = findViewById(R.id.sp_oh_11_feeder_1_pole);
            sp_oh_11_feeder_2 = findViewById(R.id.sp_oh_11_feeder_2);
            sp_oh_11_feeder_2_pole = findViewById(R.id.sp_oh_11_feeder_2_pole);
            sp_oh_11_feeder_3 = findViewById(R.id.sp_oh_11_feeder_3);
            sp_oh_11_feeder_3_pole = findViewById(R.id.sp_oh_11_feeder_3_pole);
            ed_oh_11_feeder_1 = findViewById(R.id.ed_oh_11_feeder_1);
            ed_oh_11_feeder_2 = findViewById(R.id.ed_oh_11_feeder_2);
            ed_oh_11_feeder_3 = findViewById(R.id.ed_oh_11_feeder_3);
            ed_oh_11_feeder_1_ns = findViewById(R.id.ed_oh_11_feeder_1_ns);
            ed_oh_11_feeder_2_ns = findViewById(R.id.ed_oh_11_feeder_2_ns);
            ed_oh_11_feeder_3_ns = findViewById(R.id.ed_oh_11_feeder_3_ns);
            main_layout_feeder_1 = findViewById(R.id.main_layout_feeder_1);
            layout_oh_11_feeder_1 = findViewById(R.id.layout_oh_11_feeder_1);
            main_layout_feeder_2 = findViewById(R.id.main_layout_feeder_2);
            layout_oh_11_feeder_2 = findViewById(R.id.layout_oh_11_feeder_2);
            main_layout_feeder_3 = findViewById(R.id.main_layout_feeder_3);
            layout_oh_11_feeder_3 = findViewById(R.id.layout_oh_11_feeder_3);
            get_oh_11_feeder_1 = findViewById(R.id.get_oh_11_feeder_1);
            get_oh_11_feeder_2 = findViewById(R.id.get_oh_11_feeder_2);
            get_oh_11_feeder_3 = findViewById(R.id.get_oh_11_feeder_3);
            ed_oh_11_pole_1 = findViewById(R.id.ed_oh_11_pole_1);
            ed_oh_11_pole_2 = findViewById(R.id.ed_oh_11_pole_2);
            ed_oh_11_pole_3 = findViewById(R.id.ed_oh_11_pole_3);
            feeder_1_pole_layout_11 = findViewById(R.id.feeder_1_pole_layout_11);
            feeder_2_pole_layout_11 = findViewById(R.id.feeder_2_pole_layout_11);
            feeder_3_pole_layout_11 = findViewById(R.id.feeder_3_pole_layout_11);

            //33 KV
            ch_oh_33kv_vlckts = findViewById(R.id.ch_oh_33kv_vlckts);
            lyt_oh_33kv_vlckts = findViewById(R.id.lyt_oh_33kv_vlckts);
            sp_oh_no_of_33_feeders = findViewById(R.id.sp_oh_no_of_33_feeders);
            sp_oh_33_feeder_1 = findViewById(R.id.sp_oh_33_feeder_1);
            sp_oh_33_feeder_1_pole = findViewById(R.id.sp_oh_33_feeder_1_pole);
            sp_oh_33_feeder_2 = findViewById(R.id.sp_oh_33_feeder_2);
            sp_oh_33_feeder_2_pole = findViewById(R.id.sp_oh_33_feeder_2_pole);
            sp_oh_33_feeder_3 = findViewById(R.id.sp_oh_33_feeder_3);
            sp_oh_33_feeder_3_pole = findViewById(R.id.sp_oh_33_feeder_3_pole);
            ed_oh_33_feeder_1 = findViewById(R.id.ed_oh_33_feeder_1);
            ed_oh_33_feeder_2 = findViewById(R.id.ed_oh_33_feeder_2);
            ed_oh_33_feeder_3 = findViewById(R.id.ed_oh_33_feeder_3);
            ed_oh_33_feeder_1_ns = findViewById(R.id.ed_oh_33_feeder_1_ns);
            ed_oh_33_feeder_2_ns = findViewById(R.id.ed_oh_33_feeder_2_ns);
            ed_oh_33_feeder_3_ns = findViewById(R.id.ed_oh_33_feeder_3_ns);
            main_layout_feeder_1_33 = findViewById(R.id.main_layout_feeder_1_33);
            layout_oh_33_feeder_1 = findViewById(R.id.layout_oh_33_feeder_1);
            main_layout_feeder_2_33 = findViewById(R.id.main_layout_feeder_2_33);
            layout_oh_33_feeder_2 = findViewById(R.id.layout_oh_33_feeder_2);
            main_layout_feeder_3_33 = findViewById(R.id.main_layout_feeder_3_33);
            layout_oh_33_feeder_3 = findViewById(R.id.layout_oh_33_feeder_3);
            get_oh_33_feeder_1 = findViewById(R.id.get_oh_33_feeder_1);
            get_oh_33_feeder_2 = findViewById(R.id.get_oh_33_feeder_2);
            get_oh_33_feeder_3 = findViewById(R.id.get_oh_33_feeder_3);
            ed_oh_33_pole_1 = findViewById(R.id.ed_oh_33_pole_1);
            ed_oh_33_pole_2 = findViewById(R.id.ed_oh_33_pole_2);
            ed_oh_33_pole_3 = findViewById(R.id.ed_oh_33_pole_3);
            feeder_1_pole_layout_33 = findViewById(R.id.feeder_1_pole_layout_33);
            feeder_2_pole_layout_33 = findViewById(R.id.feeder_2_pole_layout_33);
            feeder_3_pole_layout_33 = findViewById(R.id.feeder_3_pole_layout_33);

            //LT
            ch_oh_lt_vlckts = findViewById(R.id.ch_oh_lt_vlckts);
            lyt_oh_lt_vlckts = findViewById(R.id.lyt_oh_lt_vlckts);
            sp_oh_no_of_lt_feeders = findViewById(R.id.sp_oh_no_of_lt_feeders);
            sp_oh_lt_feeder_1 = findViewById(R.id.sp_oh_lt_feeder_1);
            sp_oh_lt_feeder_1_pole = findViewById(R.id.sp_oh_lt_feeder_1_pole);
            sp_oh_lt_feeder_2 = findViewById(R.id.sp_oh_lt_feeder_2);
            sp_oh_lt_feeder_2_pole = findViewById(R.id.sp_oh_lt_feeder_2_pole);
            sp_oh_lt_feeder_3 = findViewById(R.id.sp_oh_lt_feeder_3);
            sp_oh_lt_feeder_3_pole = findViewById(R.id.sp_oh_lt_feeder_3_pole);
            ed_oh_lt_feeder_1 = findViewById(R.id.ed_oh_lt_feeder_1);
            ed_oh_lt_feeder_2 = findViewById(R.id.ed_oh_lt_feeder_2);
            ed_oh_lt_feeder_3 = findViewById(R.id.ed_oh_lt_feeder_3);
            ed_oh_lt_feeder_1_ns = findViewById(R.id.ed_oh_lt_feeder_1_ns);
            ed_oh_lt_feeder_2_ns = findViewById(R.id.ed_oh_lt_feeder_2_ns);
            ed_oh_lt_feeder_3_ns = findViewById(R.id.ed_oh_lt_feeder_3_ns);
            main_layout_feeder_1_lt = findViewById(R.id.main_layout_feeder_1_lt);
            layout_oh_lt_feeder_1 = findViewById(R.id.layout_oh_lt_feeder_1);
            main_layout_feeder_2_lt = findViewById(R.id.main_layout_feeder_2_lt);
            layout_oh_lt_feeder_2 = findViewById(R.id.layout_oh_lt_feeder_2);
            main_layout_feeder_3_lt = findViewById(R.id.main_layout_feeder_3_lt);
            layout_oh_lt_feeder_3 = findViewById(R.id.layout_oh_lt_feeder_3);
            get_oh_lt_feeder_1 = findViewById(R.id.get_oh_lt_feeder_1);
            get_oh_lt_feeder_2 = findViewById(R.id.get_oh_lt_feeder_2);
            get_oh_lt_feeder_3 = findViewById(R.id.get_oh_lt_feeder_3);
            ed_oh_lt_pole_1 = findViewById(R.id.ed_oh_lt_pole_1);
            ed_oh_lt_pole_2 = findViewById(R.id.ed_oh_lt_pole_2);
            ed_oh_lt_pole_3 = findViewById(R.id.ed_oh_lt_pole_3);
            feeder_1_pole_layout_lt = findViewById(R.id.feeder_1_pole_layout_lt);
            feeder_2_pole_layout_lt = findViewById(R.id.feeder_2_pole_layout_lt);
            feeder_3_pole_layout_lt = findViewById(R.id.feeder_3_pole_layout_lt);


            sp_oh_defects = findViewById(R.id.sp_oh_defects);
            defects_layout = findViewById(R.id.defects_layout);
            ch_pole_defects = findViewById(R.id.ch_pole_defects);
            sp_pole_defects = findViewById(R.id.sp_pole_defects);
            ch_conductors = findViewById(R.id.ch_conductors);
            sp_conductors = findViewById(R.id.sp_conductors);
            ch_ab_switch_defects = findViewById(R.id.ch_ab_switch_defects);
            sp_ab_switch_defects = findViewById(R.id.sp_ab_switch_defects);
            ch_insulators = findViewById(R.id.ch_insulators);
            sp_insulators = findViewById(R.id.sp_insulators);
            ch_cross_arm = findViewById(R.id.ch_cross_arm);
            sp_cross_arm = findViewById(R.id.sp_cross_arm);
            ch_top_cleat = findViewById(R.id.ch_top_cleat);
            sp_top_cleat = findViewById(R.id.sp_top_cleat);
            ch_strut_pole = findViewById(R.id.ch_strut_pole);
            sp_strut_pole = findViewById(R.id.sp_strut_pole);
            ch_stray_wire = findViewById(R.id.ch_stray_wire);
            sp_stray_wire = findViewById(R.id.sp_stray_wire);
            ch_ht_clearance = findViewById(R.id.ch_ht_clearance);
            sp_ht_clearance = findViewById(R.id.sp_ht_clearance);
            ch_int_pole = findViewById(R.id.ch_int_pole);
            sp_int_pole = findViewById(R.id.sp_int_pole);
            ch_restringing = findViewById(R.id.ch_restringing);
            sp_restringing = findViewById(R.id.sp_restringing);
            ch_tree_clearance = findViewById(R.id.ch_tree_clearance);
            sp_tree_clearance = findViewById(R.id.sp_tree_clearance);
            ch_other_defects = findViewById(R.id.ch_other_defects);
            ed_other_defects = findViewById(R.id.ed_other_defects);

            ch_crossing = findViewById(R.id.ch_crossing);
            sp_crossing = findViewById(R.id.sp_crossing);


            current_distance = findViewById(R.id.current_distance);
            last_pole_distance = findViewById(R.id.last_pole_distance);
            btn_clear = (Button) findViewById(R.id.btn_clear);
            btn_add = (Button) findViewById(R.id.btn_add);
            btn_topclear = (Button) findViewById(R.id.btn_topclear);
            ed_pole_no = (EditText) findViewById(R.id.ed_pole_no);
            ed_frompole = (EditText) findViewById(R.id.ed_frompole);
            sp_poletype = (Spinner) findViewById(R.id.sp_poletype);
            sp_poleheight = (Spinner) findViewById(R.id.sp_poleheight);
            //sp_noof_circuits=(Spinner)findViewById(R.id.sp_noof_circuits);
            sp_polesupport = (Spinner) findViewById(R.id.sp_polesupport);
            sp_tapingpoint = (Spinner) findViewById(R.id.sp_tapingpoint);
            sp_conductorsize = (Spinner) findViewById(R.id.sp_conductorsize);
            sp_pinpoint = (Spinner) findViewById(R.id.sp_pinpoint);
            sp_ismulticircute = (Spinner) findViewById(R.id.sp_ismulticircute);
            sp_circutetype = (Spinner) findViewById(R.id.sp_circutetype);
            sp_poleconfiguration = (Spinner) findViewById(R.id.sp_poleconfiguration);
            edt_polepaintingcode = (TextInputEditText) findViewById(R.id.edt_polepaintingcode);
            edt_remarks_overhead = (TextInputEditText) findViewById(R.id.edt_remarks_overhead);
            edt_remarks_underground = (TextInputEditText) findViewById(R.id.edt_remarks_underground);
            edt_tappinglocation = (TextInputEditText) findViewById(R.id.edt_tappinglocation);
            edt_ugtappinglocation = (TextInputEditText) findViewById(R.id.edt_ugtappinglocation);
            edt_ugpolepaintingcode = (TextInputEditText) findViewById(R.id.edt_ugpolepaintingcode);
            sp_isendpole = (Spinner) findViewById(R.id.sp_isendpole);
            lyt_circuit = (LinearLayout) findViewById(R.id.lyt_circuit);
            lyt_overhead = (LinearLayout) findViewById(R.id.lyt_overhead);
            lyt_underground = (LinearLayout) findViewById(R.id.lyt_underground);
            lyt_polestructure = (LinearLayout) findViewById(R.id.lyt_polestructure);
            lyt_diffvoltage = (LinearLayout) findViewById(R.id.lyt_diffvoltage);
            lyt_rmu = (LinearLayout) findViewById(R.id.lyt_rmu);
            lyt_tapping = (LinearLayout) findViewById(R.id.lyt_tapping);
            lyt_underpolestructure = (LinearLayout) findViewById(R.id.lyt_underpolestructure);
            lyt_ugpole = (LinearLayout) findViewById(R.id.lyt_ugpole);
            lyt_ugtapping = (LinearLayout) findViewById(R.id.lyt_ugtapping);


            sp_lineType = (Spinner) findViewById(R.id.sp_lineType);
            sp_noofcircuit = (Spinner) findViewById(R.id.sp_noofcircuit);
            sp_cablesize = (Spinner) findViewById(R.id.sp_cablesize);
            sp_noofcircuit_underg = (Spinner) findViewById(R.id.sp_noofcircuit_underg);
            //edt_ssname=(TextInputEditText)findViewById(R.id.edt_ssname);
            sp__polestructure = (Spinner) findViewById(R.id.sp__polestructure);
            sp_diffvoltage = (Spinner) findViewById(R.id.sp_diffvoltage);

            sp_straight_rmu = (Spinner) findViewById(R.id.sp_straight_rmu);
            sp_rmu = (Spinner) findViewById(R.id.sp_rmu);
            sp_nooftappings = (Spinner) findViewById(R.id.sp_nooftappings);
            sp_undergisendpole = (Spinner) findViewById(R.id.sp_undergisendpole);
            sp__underpolestructure = (Spinner) findViewById(R.id.sp__underpolestructure);
            sp_ugpoletype = (Spinner) findViewById(R.id.sp_ugpoletype);
            sp_ugpoleheight = (Spinner) findViewById(R.id.sp_ugpoleheight);
            sp_ugpolesupport = (Spinner) findViewById(R.id.sp_ugpolesupport);
            sp_ugtapingpoint = (Spinner) findViewById(R.id.sp_ugtapingpoint);
            sp_ugnooftappings = (Spinner) findViewById(R.id.sp_ugnooftappings);


            tv_sunname = (TextView) findViewById(R.id.tv_sunname);
            tv_feedename = (TextView) findViewById(R.id.tv_feedename);
            btn_repeat_pole = (Button) findViewById(R.id.btn_repeat_pole);
//dtr pole ug
            sp_ug_section_name_dtr_pole = (Spinner) findViewById(R.id.sp_ug_section_name_dtr_pole);
            auto_ug_dtr_structure_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_ug_dtr_structure_dtr_pole);
            sp_ug_dtr_structure_type_dtr_pole = (Spinner) findViewById(R.id.sp_ug_dtr_structure_type_dtr_pole);
            sp_ug_phase_dtr_pole = (Spinner) findViewById(R.id.sp_ug_phase_dtr_pole);
            sp_ug_capacity_dtr_pole = (Spinner) findViewById(R.id.sp_ug_capacity_dtr_pole);
            auto_ug_make_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_ug_make_dtr_pole);
            ed_ug_location_name_dtr_pole = (EditText) findViewById(R.id.ed_ug_location_name_dtr_pole);
            sp_ug_ab_switch_dtr_pole = (Spinner) findViewById(R.id.sp_ug_ab_switch_dtr_pole);
            lay_ug_dtr_pole_main = (LinearLayout) findViewById(R.id.lay_ug_dtr_pole_main);
            sp_ug_voltage_level = (Spinner) findViewById(R.id.sp_ug_voltage_level);
            ed_ug_serial_number_dtr_pole = (EditText) findViewById(R.id.ed_ug_serial_number_dtr_pole);
            sp_ug_no_of_lt_circuits = (Spinner) findViewById(R.id.sp_ug_no_of_lt_circuits);
            // sp_ug_struct_stay_detailes = (Spinner) findViewById(R.id.sp_ug_struct_stay_detailes);

            lay_ug_ht_service_number = (LinearLayout) findViewById(R.id.lay_ug_ht_service_number);
            lay_ug_line_capacitor = (LinearLayout) findViewById(R.id.lay_ug_line_capacitor);
            lay_ug_auto_reclosure = (LinearLayout) findViewById(R.id.lay_ug_auto_reclosure);
            sp_ug_auto_reclosure = (Spinner) findViewById(R.id.sp_ug_auto_reclosure);
            sp_ug_line_capacitor = (Spinner) findViewById(R.id.sp_ug_line_capacitor);
            auto_ug_ht_service_number = (AutoCompleteTextView) findViewById(R.id.auto_ug_ht_service_number);


            sp_ug_is_lt_ckts_available = (Spinner) findViewById(R.id.sp_ug_is_lt_ckts_available);
            lay_ug_no_of_lt_circuits = (LinearLayout) findViewById(R.id.lay_ug_no_of_lt_circuits);
            sp_ug_dtr_location_type = (Spinner) findViewById(R.id.sp_ug_dtr_location_type);
            sp_ug_type_of_load = (Spinner) findViewById(R.id.sp_ug_type_of_load);
            lay_ug_no_of_ab_switches = (LinearLayout) findViewById(R.id.lay_ug_no_of_ab_switches);
            sp_ug_no_of_ab_switches = (Spinner) findViewById(R.id.sp_ug_no_of_ab_switches);

            //dtr pole oh
            sp_oh_section_name_dtr_pole = (Spinner) findViewById(R.id.sp_oh_section_name_dtr_pole);
            auto_oh_dtr_structure_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_oh_dtr_structure_dtr_pole);
            sp_oh_dtr_structure_type_dtr_pole = (Spinner) findViewById(R.id.sp_oh_dtr_structure_type_dtr_pole);
            sp_oh_phase_dtr_pole = (Spinner) findViewById(R.id.sp_oh_phase_dtr_pole);
            sp_oh_capacity_dtr_pole = (Spinner) findViewById(R.id.sp_oh_capacity_dtr_pole);
            auto_oh_make_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_oh_make_dtr_pole);
            ed_oh_location_name_dtr_pole = (EditText) findViewById(R.id.ed_oh_location_name_dtr_pole);
            sp_oh_ab_switch_dtr_pole = (Spinner) findViewById(R.id.sp_oh_ab_switch_dtr_pole);
            lay_oh_dtr_pole_main = (LinearLayout) findViewById(R.id.lay_oh_dtr_pole_main);
            sp_oh_voltage_level = (Spinner) findViewById(R.id.sp_oh_voltage_level);
            ed_oh_serial_number_dtr_pole = (EditText) findViewById(R.id.ed_oh_serial_number_dtr_pole);
            sp_oh_no_of_lt_circuits = (Spinner) findViewById(R.id.sp_oh_no_of_lt_circuits);
            lay_oh_ht_service_number = (LinearLayout) findViewById(R.id.lay_oh_ht_service_number);
            auto_oh_ht_service_number = (AutoCompleteTextView) findViewById(R.id.auto_oh_ht_service_number);
            lay_oh_line_capacitor_pc = (LinearLayout) findViewById(R.id.lay_oh_line_capacitor_pc);
            lay_oh_auto_reclosure_pc = (LinearLayout) findViewById(R.id.lay_oh_auto_reclosure_pc);
            sp_oh_auto_reclosure = (Spinner) findViewById(R.id.sp_oh_auto_reclosure);
            sp_oh_line_capacitor = (Spinner) findViewById(R.id.sp_oh_line_capacitor);
            sp_oh_is_lt_ckts_available = (Spinner) findViewById(R.id.sp_oh_is_lt_ckts_available);
            lay_oh_no_of_lt_circuits = (LinearLayout) findViewById(R.id.lay_oh_no_of_lt_circuits);
            sp_oh_dtr_location_type = (Spinner) findViewById(R.id.sp_oh_dtr_location_type);
            sp_oh_type_of_load = (Spinner) findViewById(R.id.sp_oh_type_of_load);
            lay_oh_no_of_ab_switches = (LinearLayout) findViewById(R.id.lay_oh_no_of_ab_switches);
            sp_oh_no_of_ab_switches = (Spinner) findViewById(R.id.sp_oh_no_of_ab_switches);
            sp_oh_is_dtr_avalabile = (Spinner) findViewById(R.id.sp_oh_is_dtr_avalabile);
            lay_oh_is_dtr_avalable = (LinearLayout) findViewById(R.id.lay_oh_is_dtr_avalable);

            sp_ug_is_dtr_avalabile = (Spinner) findViewById(R.id.sp_ug_is_dtr_avalabile);
            lay_ug_is_dtr_avalable = (LinearLayout) findViewById(R.id.lay_ug_is_dtr_avalable);

            //AB Cable

            lay_ab_cable_main = (LinearLayout) findViewById(R.id.lay_ab_cable_main);
            sp_abc_voltage_level = (Spinner) findViewById(R.id.sp_abc_voltage_level);
            sp_abc_dergisendpole = (Spinner) findViewById(R.id.sp_abc_dergisendpole);
            sp_abc_noofcircuit = (Spinner) findViewById(R.id.sp_abc_noofcircuit);
            sp_abc_straight_rmu = (Spinner) findViewById(R.id.sp_abc_straight_rmu);
            sp_abc_line_capacitor = (Spinner) findViewById(R.id.sp_abc_line_capacitor);
            lay_abc_line_capacitor = (LinearLayout) findViewById(R.id.lay_abc_line_capacitor);
            lay_abc_auto_reclosure = (LinearLayout) findViewById(R.id.lay_abc_auto_reclosure);
            lay_abc_ht_service_number = (LinearLayout) findViewById(R.id.lay_abc_ht_service_number);
            sp_abc_line_capacitor = (Spinner) findViewById(R.id.sp_abc_line_capacitor);
            sp_abc_auto_reclosure = (Spinner) findViewById(R.id.sp_abc_auto_reclosure);
            auto_abc_ht_service_number = (AutoCompleteTextView) findViewById(R.id.auto_abc_ht_service_number);
            lyt_abc_rmu = (LinearLayout) findViewById(R.id.lyt_abc_rmu);
            sp_abc_rmu = (Spinner) findViewById(R.id.sp_abc_rmu);
            lyt_abc_derpolestructure = (LinearLayout) findViewById(R.id.lyt_abc_derpolestructure);
            sp_abc_derpolestructure = (Spinner) findViewById(R.id.sp_abc_derpolestructure);
            sp_abc_poletype = (Spinner) findViewById(R.id.sp_abc_poletype);
            sp_abc_poleheight = (Spinner) findViewById(R.id.sp_abc_poleheight);
            // sp_abc_struct_stay_detailes=(Spinner)findViewById(R.id.sp_abc_struct_stay_detailes);
            sp_abc_polesupport = (Spinner) findViewById(R.id.sp_abc_polesupport);
            lay_abc_dtr_pole_main = (LinearLayout) findViewById(R.id.lay_abc_dtr_pole_main);
            sp_abc_section_name_dtr_pole = (Spinner) findViewById(R.id.sp_abc_section_name_dtr_pole);
            auto_abc_dtr_structure_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_abc_dtr_structure_dtr_pole);
            sp_abc_dtr_structure_type_dtr_pole = (Spinner) findViewById(R.id.sp_abc_dtr_structure_type_dtr_pole);
            sp_abc_phase_dtr_pole = (Spinner) findViewById(R.id.sp_abc_phase_dtr_pole);
            sp_abc_capacity_dtr_pole = (Spinner) findViewById(R.id.sp_abc_capacity_dtr_pole);
            auto_abc_make_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_abc_make_dtr_pole);
            ed_abc_serial_number_dtr_pole = (EditText) findViewById(R.id.ed_abc_serial_number_dtr_pole);
            ed_abc_location_name_dtr_pole = (EditText) findViewById(R.id.ed_abc_location_name_dtr_pole);
            sp_abc_no_of_lt_circuits = (Spinner) findViewById(R.id.sp_abc_no_of_lt_circuits);
            sp_abc_ab_switch_dtr_pole = (Spinner) findViewById(R.id.sp_abc_ab_switch_dtr_pole);
            sp_abc_tapingpoint = (Spinner) findViewById(R.id.sp_abc_tapingpoint);
            sp_abc_nooftappings = (Spinner) findViewById(R.id.sp_abc_nooftappings);
            edt_abc_tappinglocation = (TextInputEditText) findViewById(R.id.edt_abc_tappinglocation);
            sp_abc_cablesize = (Spinner) findViewById(R.id.sp_abc_cablesize);
            edt_abc_remarks = (EditText) findViewById(R.id.edt_abc_remarks);
            edt_abc_polepaintingcode = (EditText) findViewById(R.id.edt_abc_polepaintingcode);
            lyt_abc_tapping = (LinearLayout) findViewById(R.id.lyt_abc_tapping);
            lyt_abc_pole = (LinearLayout) findViewById(R.id.lyt_abc_pole);

            sp_abc_is_lt_ckts_available = (Spinner) findViewById(R.id.sp_abc_is_lt_ckts_available);
            lay_abc_no_of_lt_circuits = (LinearLayout) findViewById(R.id.lay_abc_no_of_lt_circuits);
            sp_abc_dtr_location_type = (Spinner) findViewById(R.id.sp_abc_dtr_location_type);
            sp_abc_type_of_load = (Spinner) findViewById(R.id.sp_abc_type_of_load);
            lay_abc_no_of_ab_switches = (LinearLayout) findViewById(R.id.lay_abc_no_of_ab_switches);
            sp_abc_no_of_ab_switches = (Spinner) findViewById(R.id.sp_abc_no_of_ab_switches);
            /*SURVEY_TYPE*/
            sp_Survey_type = (Spinner) findViewById(R.id.sp_Survey_type);


            ed_pole_no.setEnabled(false);
            ed_frompole.setEnabled(false);

            btn_clear.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            btn_topclear.setOnClickListener(this);
            btn_repeat_pole.setOnClickListener(this);


            sp_poletype.setOnItemSelectedListener(this);
            sp_poleheight.setOnItemSelectedListener(this);
//        sp_noof_circuits.setOnItemSelectedListener(this);
            sp_polesupport.setOnItemSelectedListener(this);
            sp_tapingpoint.setOnItemSelectedListener(this);
            sp_conductorsize.setOnItemSelectedListener(this);
            sp_pinpoint.setOnItemSelectedListener(this);
            sp_poleconfiguration.setOnItemSelectedListener(this);
            sp_ismulticircute.setOnItemSelectedListener(this);
            sp_circutetype.setOnItemSelectedListener(this);
            sp_isendpole.setOnItemSelectedListener(this);
            sp_lineType.setOnItemSelectedListener(this);
            sp_noofcircuit.setOnItemSelectedListener(this);
            sp_cablesize.setOnItemSelectedListener(this);
            sp_Survey_type.setOnItemSelectedListener(this);
            sp_noofcircuit_underg.setOnItemSelectedListener(this);
            sp__polestructure.setOnItemSelectedListener(this);
            sp_diffvoltage.setOnItemSelectedListener(this);
            sp_oh_type_of_load.setOnItemSelectedListener(this);
            sp_oh_is_dtr_avalabile.setOnItemSelectedListener(this);

            sp_straight_rmu.setOnItemSelectedListener(this);
            sp_rmu.setOnItemSelectedListener(this);
            sp_rmu_make.setOnItemSelectedListener(this);
            sp_nooftappings.setOnItemSelectedListener(this);
            sp_undergisendpole.setOnItemSelectedListener(this);
            sp__underpolestructure.setOnItemSelectedListener(this);
            sp_ugpoletype.setOnItemSelectedListener(this);
            sp_ugpoleheight.setOnItemSelectedListener(this);
            sp_ugpolesupport.setOnItemSelectedListener(this);
            sp_ugtapingpoint.setOnItemSelectedListener(this);
            sp_ugnooftappings.setOnItemSelectedListener(this);
            sp_abc_tapingpoint.setOnItemSelectedListener(this);

            sp_oh_section_name_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_dtr_structure_type_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_phase_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_capacity_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_ab_switch_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_voltage_level.setOnItemSelectedListener(this);
            sp_oh_is_lt_ckts_available.setOnItemSelectedListener(this);

            sp_ug_section_name_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_dtr_structure_type_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_phase_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_capacity_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_ab_switch_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_is_dtr_avalabile.setOnItemSelectedListener(this);
            sp_abc_dergisendpole.setOnItemSelectedListener(this);
            sp_abc_noofcircuit.setOnItemSelectedListener(this);
            sp_abc_straight_rmu.setOnItemSelectedListener(this);
            sp_abc_derpolestructure.setOnItemSelectedListener(this);
            sp_abc_polesupport.setOnItemSelectedListener(this);
            sp_abc_poleheight.setOnItemSelectedListener(this);
            sp_abc_section_name_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_dtr_structure_type_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_phase_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_capacity_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_ab_switch_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_tapingpoint.setOnItemSelectedListener(this);
            sp_abc_nooftappings.setOnItemSelectedListener(this);
            sp_abc_cablesize.setOnItemSelectedListener(this);
            sp_abc_is_lt_ckts_available.setOnItemSelectedListener(this);
            sp_ug_is_lt_ckts_available.setOnItemSelectedListener(this);
            sp_oh_is_lt_ckts_available.setOnItemSelectedListener(this);
            sp_ug_type_of_load.setOnItemSelectedListener(this);
            sp_abc_type_of_load.setOnItemSelectedListener(this);
            sp_oh_other_cables.setOnItemSelectedListener(this);
            sp_pole_concrete.setOnItemSelectedListener(this);

            //JHC PHASE 1
            sp_oh_dtr_survey_type.setOnItemSelectedListener(this);
            sp_oh_lt_survey_type.setOnItemSelectedListener(this);


            arcGISDatabase = new PMI11kvArcGISDatabase(c);
            surveyVosList = arcGISDatabase.getSurveyData("");
            int count = arcGISDatabase.getObjectId();
            poleCountCheck = arcGISDatabase.getObjectIdCount();
            if (poleCountCheck == 0) {
                //  if(""surveyType)
                if (from.equals("int")) {
                    ed_frompole.setText(String.valueOf(PMIPoleSurveyActivity.other_pole));
                    ed_pole_no.setText(String.valueOf(count + 1));
                } else {
                    int polenum = maxSurveyCount + 1;
                    if (surveyType.equalsIgnoreCase("Spur Line") && PMIArcGISMapActivity11kv.SPUR_FIRST_POLE){
                        ed_frompole.setText(PMIArcGISMapActivity11kv.TAPPING_POINT);
                    }else {
                        ed_frompole.setText(String.valueOf(count));
                    }
                    ed_pole_no.setText(String.valueOf(polenum));
                }

            } else {
                if (from.equals("int")) {
                    ed_frompole.setText(String.valueOf(PMIPoleSurveyActivity.other_pole));
                    ed_pole_no.setText(String.valueOf(count + 1));
                }
                else {
                    if (surveyType.equalsIgnoreCase("Spur Line")&& PMIArcGISMapActivity11kv.SPUR_FIRST_POLE){
                        ed_frompole.setText(PMIArcGISMapActivity11kv.TAPPING_POINT);
                    }else {
                        ed_frompole.setText(String.valueOf(count));
                    }

                    ed_pole_no.setText(String.valueOf(count + 1));

                }

            }
            lyt_underground.setVisibility(View.GONE);

            lyt_polestructure.setVisibility(View.GONE);
            lyt_diffvoltage.setVisibility(View.GONE);
            lyt_rmu.setVisibility(View.GONE);
            lyt_tapping.setVisibility(View.GONE);
            lyt_underpolestructure.setVisibility(View.GONE);
            lyt_ugpole.setVisibility(View.GONE);
            lyt_ugtapping.setVisibility(View.GONE);
            lay_ug_dtr_pole_main.setVisibility(View.GONE);
            lay_oh_dtr_pole_main.setVisibility(View.GONE);
            lay_oh_no_of_lt_circuits.setVisibility(View.GONE);
            lay_oh_no_of_ab_switches.setVisibility(View.GONE);
            lay_oh_is_dtr_avalable.setVisibility(View.GONE);
            lay_ug_is_dtr_avalable.setVisibility(View.GONE);

            lay_ab_cable_main.setVisibility(View.GONE);
            lay_oh_ht_service_number.setVisibility(View.GONE);
            lay_oh_line_capacitor_pc.setVisibility(View.GONE);
            lay_oh_line_capacitor_pc.setVisibility(View.GONE);
            lay_ug_ht_service_number.setVisibility(View.GONE);
            lyt_abc_tapping.setVisibility(View.GONE);
            lay_abc_ht_service_number.setVisibility(View.GONE);
            lay_abc_line_capacitor.setVisibility(View.GONE);
            lay_abc_auto_reclosure.setVisibility(View.GONE);
            lay_abc_no_of_ab_switches.setVisibility(View.GONE);
            lay_abc_no_of_lt_circuits.setVisibility(View.GONE);
            lay_ug_no_of_ab_switches.setVisibility(View.GONE);
            lay_ug_no_of_lt_circuits.setVisibility(View.GONE);


            tv_sunname.setText(ssName);
            Log.v("SSNAME-->", ssName);
            tv_feedename.setText(feederName);
            mapData = new HashMap();


            surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM ARCGIS_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM ARCGIS_TABLE_REPEAT)-1");
            if (surveyVoList.size() > 0) {
                btn_repeat_pole.setVisibility(View.GONE);
            } else {
                btn_repeat_pole.setVisibility(View.GONE);
            }


            makeListDatabase = arcGISDatabase.getComplaint("SELECT DTR_MAKE_M FROM MAKE_MASTER_TABLE");
            ArrayAdapter adapterMake_oh = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, makeListDatabase);//c.getResources().getStringArray(R.array.dtr_make)
            auto_oh_make_dtr_pole.setAdapter(adapterMake_oh);
            auto_oh_make_dtr_pole.setThreshold(1);
            ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, makeListDatabase);
            auto_ug_make_dtr_pole.setAdapter(adapterMake_ug);
            auto_ug_make_dtr_pole.setThreshold(1);
            ArrayAdapter adapterMake_abc = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, makeListDatabase);
            auto_abc_make_dtr_pole.setAdapter(adapterMake_abc);
            auto_abc_make_dtr_pole.setThreshold(1);
            makeArrayList = makeListDatabase;
            String CONDUCTOR_TX = "CONDUCTOR";

           /* List<String> conductorsizeListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CONDUCTOR_TX + "'" + " ORDER BY CKT_VALUE");
            conductorsizeListDatabe.add("Select");
            Collections.reverse(conductorsizeListDatabe);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, conductorsizeListDatabe);
            sp_conductorsize.setAdapter(adapter);*/

            //NO Of Cables
            String CABLE_TX = "CABLE";
            List<String> cableListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX + "'" + " ORDER BY CKT_VALUE");
            cableListDatabe.add("Select");
            Collections.reverse(cableListDatabe);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, cableListDatabe);
            sp_cablesize.setAdapter(adapter1);

            //NO Of Cables
            String CABLE_TX1 = "CABLE";
            List<String> abc_cableListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX1 + "'" + " ORDER BY CKT_VALUE");
            abc_cableListDatabe.add("Select");
            Collections.reverse(abc_cableListDatabe);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_cableListDatabe);
            sp_abc_cablesize.setAdapter(adapter2);
            edt_tappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
            edt_ugtappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
            edt_abc_tappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
            int a = (int) Math.round(lastPoleDistance);
            last_pole_distance.setText("" + a);

            int b = (int) Math.round(currentDistance);
            current_distance.setText("" + b);



            //CROSSING
            ch_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_crossing.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_6_3_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_6_3_crossing.setVisibility(View.VISIBLE);
                       /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList11kv);
                        edt_oh_6_3_crossing.setAdapter(adapter_feeder);
                        edt_oh_6_3_crossing.setThreshold(5);*/


                        edt_oh_6_3_crossing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                open11KVFeederMasterDialog(edt_oh_6_3_crossing);
                            }
                        });

                    } else {
                        lyt_oh_6_3_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_11kv_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_11kv_crossing.setVisibility(View.VISIBLE);
                        /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList11kv);
                        edt_oh_11kv_crossing.setAdapter(adapter_feeder);
                        edt_oh_11kv_crossing.setThreshold(5);*/



                        edt_oh_11kv_crossing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                open11KVFeederMasterDialog(edt_oh_11kv_crossing);
                            }
                        });

                    } else {
                        lyt_oh_11kv_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_33kv_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_33kv_crossing.setVisibility(View.VISIBLE);
                        /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList33kv);
                        edt_oh_33kv_crossing.setAdapter(adapter_feeder);
                        edt_oh_33kv_crossing.setThreshold(5);*/



                        edt_oh_33kv_crossing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                open33KVFeederMasterDialog(edt_oh_33kv_crossing);
                            }
                        });

                    } else {
                        lyt_oh_33kv_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_lt_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_lt_crossing.setVisibility(View.VISIBLE);
                        /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederListLT);
                        edt_oh_lt_crossing.setAdapter(adapter_feeder);
                        edt_oh_lt_crossing.setThreshold(5);*/

                        SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                        searchableSpinner.setWindowTitle("SELECT DTR CODE");
                        searchableSpinner.setSpinnerListItems(PMIDTRMasterDownloadActivity.feederListLT);
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                edt_oh_lt_crossing.setText(selectedString);
                            }
                        });

                        edt_oh_lt_crossing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchableSpinner.show();
                            }
                        });

                    } else {
                        lyt_oh_lt_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_road_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_crossing.setVisibility(View.VISIBLE);
                    } else {
                        sp_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_tc_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_tc_crossing.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_tc_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_tm_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_tm_crossing.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_tm_crossing.setVisibility(View.GONE);
                    }
                }
            });


            //DEFECTS
            defect_selected = sp_oh_defects.getSelectedItem().toString();
            sp_oh_defects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    defect_selected = sp_oh_defects.getSelectedItem().toString();

                    /*if (defect_selected.equalsIgnoreCase("Yes")) {
                        defects_layout.setVisibility(View.VISIBLE);
                        images_layout.setVisibility(View.VISIBLE);
                    } else {
                        defects_layout.setVisibility(View.GONE);
                        if (poleconfiguration.equals("Line-Pole") || poleconfiguration.equals("Select")) {
                            images_layout.setVisibility(View.GONE);
                        } else {
                            images_layout.setVisibility(View.VISIBLE);
                        }

                    }*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ch_pole_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_pole_defects.setVisibility(View.VISIBLE);
                    } else {
                        sp_pole_defects.setVisibility(View.GONE);
                    }
                }
            });

            ch_conductors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_conductors.setVisibility(View.VISIBLE);
                    } else {
                        sp_conductors.setVisibility(View.GONE);
                    }
                }
            });

            ch_ab_switch_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_ab_switch_defects.setVisibility(View.VISIBLE);
                    } else {
                        sp_ab_switch_defects.setVisibility(View.GONE);
                    }
                }
            });

            ch_insulators.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_insulators.setVisibility(View.VISIBLE);
                    } else {
                        sp_insulators.setVisibility(View.GONE);
                    }
                }
            });

            ch_cross_arm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_cross_arm.setVisibility(View.VISIBLE);
                    } else {
                        sp_cross_arm.setVisibility(View.GONE);
                    }
                }
            });

            ch_top_cleat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_top_cleat.setVisibility(View.VISIBLE);
                    } else {
                        sp_top_cleat.setVisibility(View.GONE);
                    }
                }
            });

            ch_strut_pole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_strut_pole.setVisibility(View.VISIBLE);
                    } else {
                        sp_strut_pole.setVisibility(View.GONE);
                    }
                }
            });

            ch_stray_wire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_stray_wire.setVisibility(View.VISIBLE);
                    } else {
                        sp_stray_wire.setVisibility(View.GONE);
                    }
                }
            });

            ch_ht_clearance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_ht_clearance.setVisibility(View.VISIBLE);
                    } else {
                        sp_ht_clearance.setVisibility(View.GONE);
                    }
                }
            });

            ch_int_pole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_int_pole.setVisibility(View.VISIBLE);
                    } else {
                        sp_int_pole.setVisibility(View.GONE);
                    }
                }
            });

            ch_restringing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_restringing.setVisibility(View.VISIBLE);
                    } else {
                        sp_restringing.setVisibility(View.GONE);
                    }
                }
            });

            ch_tree_clearance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_tree_clearance.setVisibility(View.VISIBLE);
                    } else {
                        sp_tree_clearance.setVisibility(View.GONE);
                    }
                }
            });

            ch_other_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ed_other_defects.setVisibility(View.VISIBLE);
                    } else {
                        ed_other_defects.setVisibility(View.GONE);
                    }
                }
            });


            //IMAGES
            upload_image_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_1);*/

                    Intent intent = new Intent(c, MainActivity.class);
                    c.startActivity(intent);
                    photoClicked1 = true;
                    isImageCaptured1 = false;
                    capturedBitmap1 = null;

                   /* try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.setPackage("com.apepdcl.gis.app");
                        Activity activity = (Activity) c;
                        File photoFile = createImageFile1();
                        Uri photoURI = FileProvider.getUriForFile(activity, "com.apepdcl.gis_survey.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        activity.startActivityForResult(intent,  CAMERA_RESULT_CODE_IMAGE_1);

                    } catch (Exception e) {
                        e.printStackTrace();
                        showInstallDialog();
                    }*/


//                    try {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File photoFile = createImageFile1();
//                        if (photoFile != null) {
//                            Uri photoURI = FileProvider.getUriForFile(c, "com.apepdcl.gis_survey.fileprovider", photoFile);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        }
//                        c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
            });

            upload_image_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // openImageDialog(CAMERA_RESULT_CODE_IMAGE_2,GALLERY_RESULT_CODE_IMAGE_2);
                    /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_2);*/

                    Intent intent = new Intent(c, MainActivity.class);
                    c.startActivity(intent);
                    photoClicked2 = true;
                    isImageCaptured2 = false;
                    capturedBitmap2 = null;

                   /* try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.setPackage("com.apepdcl.gis.app");
                        Activity activity = (Activity) c;
                        File photoFile = createImageFile2();
                        Uri photoURI = FileProvider.getUriForFile(activity, "com.apepdcl.gis_survey.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        activity.startActivityForResult(intent,  CAMERA_RESULT_CODE_IMAGE_2);

                    } catch (Exception e) {
                        e.printStackTrace();
                        showInstallDialog();
                    }*/

//                    try {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File photoFile = createImageFile2();
//                        if (photoFile != null) {
//                            Uri photoURI = FileProvider.getUriForFile(c, "com.apepdcl.gis_survey.fileprovider", photoFile);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        }
//                        c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_2);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            });

            upload_image_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //openImageDialog(CAMERA_RESULT_CODE_IMAGE_3,GALLERY_RESULT_CODE_IMAGE_3);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_3);

                }
            });
            // System.out.println("CAME HERE");

            //MULTI CKT
            ch_oh_33kv_vlckts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_33kv_vlckts.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_33kv_vlckts.setVisibility(View.GONE);
                        noof_33kvCkts = 0;

                    }
                }
            });

            ch_oh_11kv_vlckts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_11kv_vlckts.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_11kv_vlckts.setVisibility(View.GONE);
                        noof_11kvCkts = 0;

                    }
                }
            });

            ch_oh_lt_vlckts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_lt_vlckts.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_lt_vlckts.setVisibility(View.GONE);
                        noof_ltCkts = 0;
                    }
                }
            });


            multiCircuitListeners33KV();

            multiCircuitListeners11KV();

            multiCircuitListenersLT();


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CAUGHT AN EXCEPTION : " + e.getMessage());
        }
    }

    private void showInstallDialog() {
        new AlertDialog.Builder(c)
                .setTitle("Open Camera Not Installed")
                .setMessage("To continue, please install the Open Camera app.")
                .setPositiveButton("Install", (dialog, which) -> {
                    Activity activity = (Activity) c;
                    try {
                        // Try opening Play Store directly
                        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.apepdcl.gis.app")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        // Fallback to browser
                        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.apepdcl.gis.app")));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private double getDistanceBetweenPoles(String polLat, String polLon) {
        //  System.out.println("INSIDE CURRENT LOCATION LAT : "+x);
        //  System.out.println("INSIDE CURRENT LOCATION LON : "+y);
        // System.out.println("INSIDE POLE LOCATION LAT : "+polLat);
        // System.out.println("INSIDE POLE LOCATION LON : "+polLon);

        Location currentStartPoint = new Location("locationA");
        currentStartPoint.setLatitude(Double.parseDouble(x));
        currentStartPoint.setLongitude(Double.parseDouble(y));

        Location currentEndPoint = new Location("locationA");
        currentEndPoint.setLatitude(Double.parseDouble(polLat));
        currentEndPoint.setLongitude(Double.parseDouble(polLon));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        return currentDistance;
    }

    private void multiCircuitListeners11KV() {

        sp_oh_no_of_11_feeders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_no_of_feeders = sp_oh_no_of_11_feeders.getSelectedItem().toString();
                if (str_no_of_feeders.equalsIgnoreCase("Select")) {
                    main_layout_feeder_1.setVisibility(View.GONE);
                    main_layout_feeder_2.setVisibility(View.GONE);
                    main_layout_feeder_3.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("1")) {
                    main_layout_feeder_1.setVisibility(View.VISIBLE);
                    main_layout_feeder_2.setVisibility(View.GONE);
                    main_layout_feeder_3.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("2")) {
                    main_layout_feeder_1.setVisibility(View.VISIBLE);
                    main_layout_feeder_2.setVisibility(View.VISIBLE);
                    main_layout_feeder_3.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("3")) {
                    main_layout_feeder_1.setVisibility(View.VISIBLE);
                    main_layout_feeder_2.setVisibility(View.VISIBLE);
                    main_layout_feeder_3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //FEEDER 1
        List<String> list_feeder_2 = new ArrayList<>();
        list_feeder_2.add("Select");
        List<String> list_feeder_1 = new ArrayList<>();
        list_feeder_1.add("Select");
        List<String> list_feeder_3 = new ArrayList<>();
        list_feeder_3.add("Select");
        //  list_feeder_1.addAll(PMIDTRMasterDownloadActivity.filter11kv);

        //NEW START

        ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
        //  ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < PMIDTRMasterDownloadActivity.list11kv.size(); j++) {
            if (!feederCode.equals(PMIDTRMasterDownloadActivity.list11kv.get(j).getMC_FEEDER_ID())) {
                double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.list11kv.get(j).getGLAT(), PMIDTRMasterDownloadActivity.list11kv.get(j).getGLON());
                // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                if (distance <= 100) {
                    filteredList.add(PMIDTRMasterDownloadActivity.list11kv.get(j));
                }
            }


        }

        List<String> combinedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            combinedList.add(filteredList.get(i).getSHOW_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(combinedList);
        combinedList.clear();
        combinedList.addAll(set_feeder_id_1);

        if (!combinedList.isEmpty()) {
            for (int i = 0; i < combinedList.size(); i++) {
                list_feeder_1.add(combinedList.get(i));
                list_feeder_2.add(combinedList.get(i));
                list_feeder_3.add(combinedList.get(i));
            }
        }


        //NEW END


        list_feeder_1.add("Other than List");
        list_feeder_1.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
        sp_oh_11_feeder_1.setAdapter(adapter_feeder_1);
        sp_oh_11_feeder_1.setSelection(0, true);

        //FEEDER 2

        //  list_feeder_2.addAll(PMIDTRMasterDownloadActivity.filter11kv);


        list_feeder_2.add("Other than List");
        list_feeder_2.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_11_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_11_feeder_2.setSelection(0, true);

        //FEEDER 3

        // list_feeder_3.addAll(PMIDTRMasterDownloadActivity.filter11kv);
        list_feeder_3.add("Other than List");
        list_feeder_3.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_3);
        sp_oh_11_feeder_3.setAdapter(adapter_feeder_3);
        sp_oh_11_feeder_3.setSelection(0, true);

        sp_oh_11_feeder_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_11_feeder_1.setVisibility(View.VISIBLE);
                    feeder_1_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_1_ns.setVisibility(View.GONE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList11kv);
                    ed_oh_11_feeder_1.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_1.setThreshold(5);*/



                    ed_oh_11_feeder_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_1);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_1_pole.setAdapter(adapter);
                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_1_pole.setAdapter(adapter);
                    layout_oh_11_feeder_1.setVisibility(View.GONE);
                    ed_oh_11_feeder_1_ns.setVisibility(View.GONE);
                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_11_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_11.setVisibility(View.GONE);
                    ed_oh_11_feeder_1_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList11kv);
                    ed_oh_11_feeder_1_ns.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_1_ns.setThreshold(5);*/



                    ed_oh_11_feeder_1_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_1_ns);
                        }
                    });

                } else {
                    layout_oh_11_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_1_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_11_feeder_1.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    // System.out.println("SELECTED FEEDER 11KV 1 :"+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.list11kv.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.list11kv.get(j).getGLAT(), PMIDTRMasterDownloadActivity.list11kv.get(j).getGLON());
                            // System.out.println("DISTANCE 11KV FEEDER 1 : "+distance);
                            // System.out.println("DISTANCE 11KV FEEDER 1 Pole : "+PMIDTRMasterDownloadActivity.list11kv.get(j).getMC_POLE_NO());
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.list11kv.get(j).getMC_POLE_NO());
                            }
                        }

                    }
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    list.add("Not in list(Enter Pole number)");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_1_pole.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_oh_11_feeder_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_11_feeder_2.setVisibility(View.VISIBLE);
                    feeder_2_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_2_ns.setVisibility(View.GONE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList11kv);
                    ed_oh_11_feeder_2.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_2.setThreshold(5);*/



                    ed_oh_11_feeder_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_2);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_2_pole.setAdapter(adapter);
                } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_11_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_11.setVisibility(View.GONE);
                    ed_oh_11_feeder_2_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList11kv);
                    ed_oh_11_feeder_2_ns.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_2_ns.setThreshold(5);*/


                    ed_oh_11_feeder_2_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_2_ns);
                        }
                    });

                } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_11_feeder_2.setVisibility(View.GONE);
                    ed_oh_11_feeder_2_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_2_pole.setAdapter(adapter);
                } else {
                    layout_oh_11_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_2_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_11_feeder_2.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.list11kv.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.list11kv.get(j).getGLAT(), PMIDTRMasterDownloadActivity.list11kv.get(j).getGLON());
                            //System.out.println("DISTANCE 11KV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.list11kv.get(j).getMC_POLE_NO());
                            }
                        }

                    }
                   /* if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    list.add("Not in list(Enter Pole number)");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_2_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_11_feeder_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_11_feeder_3.setVisibility(View.VISIBLE);
                    feeder_3_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_3_ns.setVisibility(View.GONE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList11kv);
                    ed_oh_11_feeder_3.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_3.setThreshold(5);*/



                    ed_oh_11_feeder_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_3);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_3_pole.setAdapter(adapter);
                } else if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_11_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_11.setVisibility(View.GONE);
                    ed_oh_11_feeder_3_ns.setVisibility(View.VISIBLE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList11kv);
                    ed_oh_11_feeder_3_ns.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_3_ns.setThreshold(5);*/



                    ed_oh_11_feeder_3_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_3_ns);
                        }
                    });

                } else if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_11_feeder_3.setVisibility(View.GONE);
                    ed_oh_11_feeder_3_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_3_pole.setAdapter(adapter);
                } else {
                    layout_oh_11_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_3_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_11_feeder_2.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.list11kv.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.list11kv.get(j).getGLAT(), PMIDTRMasterDownloadActivity.list11kv.get(j).getGLON());
                            //System.out.println("DISTANCE 11KV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.list11kv.get(j).getMC_POLE_NO());
                            }
                        }

                    }
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    list.add("Not in list(Enter Pole number)");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_3_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        get_oh_11_feeder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_11_feeder_1.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 1 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_1.getText().toString().equals(ed_oh_11_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_1.getText().toString().equals(ed_oh_11_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get11KVFeeder1Pole();
                }
            }
        });

        get_oh_11_feeder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_11_feeder_2.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 2 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_2.getText().toString().equals(ed_oh_11_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_2.getText().toString().equals(ed_oh_11_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get11KVFeeder2Pole();
                }
            }
        });

        get_oh_11_feeder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_11_feeder_3.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 3 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_3.getText().toString().equals(ed_oh_11_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_3.getText().toString().equals(ed_oh_11_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get11KVFeeder3Pole();
                }
            }
        });

        sp_oh_11_feeder_1_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_11_pole_1.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_11_pole_1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_11_feeder_2_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_11_pole_2.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_11_pole_2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_11_feeder_3_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_11_pole_3.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_11_pole_3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void multiCircuitListeners33KV() {

        sp_oh_no_of_33_feeders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_no_of_feeders = sp_oh_no_of_33_feeders.getSelectedItem().toString();
                if (str_no_of_feeders.equalsIgnoreCase("Select")) {
                    main_layout_feeder_1_33.setVisibility(View.GONE);
                    main_layout_feeder_2_33.setVisibility(View.GONE);
                    main_layout_feeder_3_33.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("1")) {
                    main_layout_feeder_1_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_33.setVisibility(View.GONE);
                    main_layout_feeder_3_33.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("2")) {
                    main_layout_feeder_1_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_3_33.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("3")) {
                    main_layout_feeder_1_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_3_33.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //FEEDER 1
        List<String> list_feeder_1 = new ArrayList<>();
        list_feeder_1.add("Select");
        List<String> list_feeder_2 = new ArrayList<>();
        list_feeder_2.add("Select");
        List<String> list_feeder_3 = new ArrayList<>();
        list_feeder_3.add("Select");

        //NEW START
        ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
        //  ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < PMIDTRMasterDownloadActivity.list33kv.size(); j++) {
            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.list33kv.get(j).getGLAT(), PMIDTRMasterDownloadActivity.list33kv.get(j).getGLON());
            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
            if (distance <= 100) {
                filteredList.add(PMIDTRMasterDownloadActivity.list33kv.get(j));
            }
        }

        List<String> combinedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            combinedList.add(filteredList.get(i).getSHOW_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(combinedList);
        combinedList.clear();
        combinedList.addAll(set_feeder_id_1);

        if (!combinedList.isEmpty()) {
            for (int i = 0; i < combinedList.size(); i++) {
                list_feeder_1.add(combinedList.get(i));
                list_feeder_2.add(combinedList.get(i));
                list_feeder_3.add(combinedList.get(i));
            }
        }

        //NEW END

        //list_feeder_1.addAll(PMIDTRMasterDownloadActivity.filter33kv);
        list_feeder_1.add("Other than List");
        list_feeder_1.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
        sp_oh_33_feeder_1.setAdapter(adapter_feeder_1);
        sp_oh_33_feeder_1.setSelection(0, true);

        //FEEDER 2

        //list_feeder_2.addAll(PMIDTRMasterDownloadActivity.filter33kv);
        list_feeder_2.add("Other than List");
        list_feeder_2.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_33_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_33_feeder_2.setSelection(0, true);

        //FEEDER 3

        //list_feeder_3.addAll(PMIDTRMasterDownloadActivity.filter33kv);
        list_feeder_3.add("Other than List");
        list_feeder_3.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_3);
        sp_oh_33_feeder_3.setAdapter(adapter_feeder_3);
        sp_oh_33_feeder_3.setSelection(0, true);

        sp_oh_33_feeder_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_33_feeder_1.setVisibility(View.VISIBLE);
                    feeder_1_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_1_ns.setVisibility(View.GONE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList33kv);
                    ed_oh_33_feeder_1.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_1.setThreshold(5);*/



                    ed_oh_33_feeder_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_1);
                        }
                    });


                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_1_pole.setAdapter(adapter);
                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_33_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_33.setVisibility(View.GONE);
                    ed_oh_33_feeder_1_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList33kv);
                    ed_oh_33_feeder_1_ns.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_1_ns.setThreshold(5);*/

                    /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList33kv);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    ed_oh_33_feeder_1_ns.setAdapter(adapter);*/

                    //Optional Parameters


                    ed_oh_33_feeder_1_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_1_ns);
                        }
                    });


                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_1_pole.setAdapter(adapter);
                    layout_oh_33_feeder_1.setVisibility(View.GONE);
                    ed_oh_33_feeder_1_ns.setVisibility(View.GONE);
                } else {
                    layout_oh_33_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_1_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_33_feeder_1.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.list33kv.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.list33kv.get(j).getGLAT(), PMIDTRMasterDownloadActivity.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 1 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.list33kv.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                   /* if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_1_pole.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_33_feeder_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_33_feeder_2.setVisibility(View.VISIBLE);
                    feeder_2_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_2_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList33kv);
                    ed_oh_33_feeder_2.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_2.setThreshold(5);*/



                    ed_oh_33_feeder_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_2);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_2_pole.setAdapter(adapter);
                } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_33_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_33.setVisibility(View.GONE);
                    ed_oh_33_feeder_2_ns.setVisibility(View.VISIBLE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList33kv);
                    ed_oh_33_feeder_2_ns.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_2_ns.setThreshold(5);*/



                    ed_oh_33_feeder_2_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_2_ns);
                        }
                    });

                } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_33_feeder_2.setVisibility(View.GONE);
                    ed_oh_33_feeder_2_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_2_pole.setAdapter(adapter);
                } else {
                    layout_oh_33_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_2_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_33_feeder_2.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.list33kv.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.list33kv.get(j).getGLAT(), PMIDTRMasterDownloadActivity.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.list33kv.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_2_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_33_feeder_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_33_feeder_3.setVisibility(View.VISIBLE);
                    feeder_3_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_3_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList33kv);
                    ed_oh_33_feeder_3.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_3.setThreshold(5);*/



                    ed_oh_33_feeder_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_3);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_3_pole.setAdapter(adapter);
                } else if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_33_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_33.setVisibility(View.GONE);
                    ed_oh_33_feeder_3_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederList33kv);
                    ed_oh_33_feeder_3_ns.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_3_ns.setThreshold(5);*/



                    ed_oh_33_feeder_3_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_3_ns);
                        }
                    });

                } else if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_33_feeder_3.setVisibility(View.GONE);
                    ed_oh_33_feeder_3_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_3_pole.setAdapter(adapter);
                } else {
                    layout_oh_33_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_3_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_33_feeder_3.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.list33kv.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.list33kv.get(j).getGLAT(), PMIDTRMasterDownloadActivity.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.list33kv.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_3_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        get_oh_33_feeder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_33_feeder_1.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 1 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_1.getText().toString().equals(ed_oh_33_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_1.getText().toString().equals(ed_oh_33_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get33KVFeeder1Pole();
                }
            }
        });

        get_oh_33_feeder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_33_feeder_2.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 2 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_2.getText().toString().equals(ed_oh_33_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_2.getText().toString().equals(ed_oh_33_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get33KVFeeder2Pole();
                }
            }
        });

        get_oh_33_feeder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_33_feeder_3.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 3 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_3.getText().toString().equals(ed_oh_33_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_3.getText().toString().equals(ed_oh_33_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get33KVFeeder3Pole();
                }
            }
        });

        sp_oh_33_feeder_1_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_33_pole_1.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_33_pole_1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_33_feeder_2_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_33_pole_2.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_33_pole_2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_33_feeder_3_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_33_pole_3.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_33_pole_3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void open11KVFeederMasterDialog(TextView edt_text) {
        Dialog payDialog = new Dialog(c);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog);
        payDialog.getWindow().setLayout(width,height/2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT 11KV FEEDER !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" From Same Section")){
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> feedersList = new ArrayList<>();
                    List<LoginDataResponse> loginDataResponses = new PMIArcGISDatabase(c).getUserMasterData();
                    String office_id = loginDataResponses.get(0).getOFFICE_ID();
                    // System.out.println("OFFICE ID : "+office_id);
                    for (int i = 0; i < PMIDTRMasterDownloadActivity.feederMasterList11KV.size(); i++) {
                        if (PMIDTRMasterDownloadActivity.feederMasterList11KV.get(i).getSEC_ID().equals(office_id)){
                            //  System.out.println("SEC ID : "+DTRSelectActivity.feederMasterList11KV.get(i).getSEC_ID());
                            feedersList.add(PMIDTRMasterDownloadActivity.feederMasterList11KV.get(i).getFEEDER_ID()
                                    +"~"+PMIDTRMasterDownloadActivity.feederMasterList11KV.get(i).getFEEDER_NAME());
                        }
                    }
                    searchableSpinner.setSpinnerListItems(feedersList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                        }
                    });

                }else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> searchList = new ArrayList<>(PMIDTRMasterDownloadActivity.feederList11kv);
                    if (searchList.contains(feederCode + "~" + feederName)) {
                        searchList.remove(feederCode + "~" + feederName);
                    }

                    searchableSpinner.setSpinnerListItems(searchList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
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

    private void open33KVFeederMasterDialog(TextView edt_text) {
        Dialog payDialog = new Dialog(c);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog);
        payDialog.getWindow().setLayout(width,height/2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT 33KV FEEDER !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" From Same Section")){
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> feedersList = new ArrayList<>();
                    for (int i = 0; i < PMIDTRMasterDownloadActivity.feederMasterList33KV.size(); i++) {
                        if (PMIDTRMasterDownloadActivity.feederMasterList33KV.get(i).getSEC_ID().equals("SAME")){
                            feedersList.add(PMIDTRMasterDownloadActivity.feederMasterList33KV.get(i).getFEEDER_ID()
                                    +"~"+PMIDTRMasterDownloadActivity.feederMasterList33KV.get(i).getFEEDER_NAME());
                        }
                    }
                    searchableSpinner.setSpinnerListItems(feedersList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                        }
                    });

                }else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    searchableSpinner.setSpinnerListItems(PMIDTRMasterDownloadActivity.feederList33kv);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
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

    private void multiCircuitListenersLT() {

        sp_oh_no_of_lt_feeders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_no_of_feeders = sp_oh_no_of_lt_feeders.getSelectedItem().toString();
                if (str_no_of_feeders.equalsIgnoreCase("Select")) {
                    main_layout_feeder_1_lt.setVisibility(View.GONE);
                    main_layout_feeder_2_lt.setVisibility(View.GONE);
                    main_layout_feeder_3_lt.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("1")) {
                    main_layout_feeder_1_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_lt.setVisibility(View.GONE);
                    main_layout_feeder_3_lt.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("2")) {
                    main_layout_feeder_1_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_3_lt.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("3")) {
                    main_layout_feeder_1_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_3_lt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //FEEDER 1
        List<String> list_feeder_1 = new ArrayList<>();
        list_feeder_1.add("Select");
        List<String> list_feeder_2 = new ArrayList<>();
        list_feeder_2.add("Select");
        List<String> list_feeder_3 = new ArrayList<>();
        list_feeder_3.add("Select");

        // list_feeder_1.addAll(PMIDTRMasterDownloadActivity.filterLT);

        //NEW START
        ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
        //ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < PMIDTRMasterDownloadActivity.listLT.size(); j++) {
            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.listLT.get(j).getGLAT(), PMIDTRMasterDownloadActivity.listLT.get(j).getGLON());
            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
            if (distance <= 100) {
                filteredList.add(PMIDTRMasterDownloadActivity.listLT.get(j));
            }
        }

        List<String> combinedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            combinedList.add(filteredList.get(i).getMC_FEEDER_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(combinedList);
        combinedList.clear();
        combinedList.addAll(set_feeder_id_1);

        if (!combinedList.isEmpty()) {
            for (int i = 0; i < combinedList.size(); i++) {
                list_feeder_1.add(combinedList.get(i));
                list_feeder_2.add(combinedList.get(i));
                list_feeder_3.add(combinedList.get(i));
            }
        }

        //NEW END

        list_feeder_1.add("LT CKT Not Surveyed");
        ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
        sp_oh_lt_feeder_1.setAdapter(adapter_feeder_1);
        sp_oh_lt_feeder_1.setSelection(0, true);

        //FEEDER 2

        // list_feeder_2.addAll(PMIDTRMasterDownloadActivity.filterLT);
        list_feeder_2.add("LT CKT Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_lt_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_lt_feeder_2.setSelection(0, true);

        //FEEDER 3

        // list_feeder_3.addAll(PMIDTRMasterDownloadActivity.filterLT);
        list_feeder_3.add("LT CKT Not Surveyed");
        ArrayAdapter<String> adapter_feeder_3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_3);
        sp_oh_lt_feeder_3.setAdapter(adapter_feeder_3);
        sp_oh_lt_feeder_3.setSelection(0, true);

        sp_oh_lt_feeder_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_lt_feeder_1.setVisibility(View.VISIBLE);
                    feeder_1_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_1_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederListLT);
                    ed_oh_lt_feeder_1.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_1.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMIDTRMasterDownloadActivity.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_1.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_1_pole.setAdapter(adapter);
                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                    layout_oh_lt_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_lt.setVisibility(View.GONE);
                    ed_oh_lt_feeder_1_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederListLT);
                    ed_oh_lt_feeder_1_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_1_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMIDTRMasterDownloadActivity.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_1_ns.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_1_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_1_pole.setAdapter(adapter);
                    layout_oh_lt_feeder_1.setVisibility(View.GONE);
                    ed_oh_lt_feeder_1_ns.setVisibility(View.GONE);
                } else {
                    layout_oh_lt_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_1_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_lt_feeder_1.getSelectedItem().toString();
                    /*String [] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[1];*/
                    //System.out.println("selectedFeeder 1 : "+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.listLT.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.listLT.get(j).getGLAT(), PMIDTRMasterDownloadActivity.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 1 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.listLT.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                   /* if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_1_pole.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_lt_feeder_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_lt_feeder_2.setVisibility(View.VISIBLE);
                    feeder_2_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_2_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederListLT);
                    ed_oh_lt_feeder_2.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_2.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMIDTRMasterDownloadActivity.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_2.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_2_pole.setAdapter(adapter);
                } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                    layout_oh_lt_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_lt.setVisibility(View.GONE);
                    ed_oh_lt_feeder_2_ns.setVisibility(View.VISIBLE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederListLT);
                    ed_oh_lt_feeder_2_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_2_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMIDTRMasterDownloadActivity.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_2_ns.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_2_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });


                } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_lt_feeder_2.setVisibility(View.GONE);
                    ed_oh_lt_feeder_2_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_2_pole.setAdapter(adapter);
                } else {
                    layout_oh_lt_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_2_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_lt_feeder_2.getSelectedItem().toString();
                    /*String [] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[1];*/
                    //System.out.println("selectedFeeder 2 : "+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.listLT.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.listLT.get(j).getGLAT(), PMIDTRMasterDownloadActivity.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.listLT.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_2_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_lt_feeder_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_lt_feeder_3.setVisibility(View.VISIBLE);
                    feeder_3_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_3_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederListLT);
                    ed_oh_lt_feeder_3.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_3.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMIDTRMasterDownloadActivity.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_3.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_3_pole.setAdapter(adapter);
                } else if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                    layout_oh_lt_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_lt.setVisibility(View.GONE);
                    ed_oh_lt_feeder_3_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMIDTRMasterDownloadActivity.feederListLT);
                    ed_oh_lt_feeder_3_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_3_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMIDTRMasterDownloadActivity.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_3_ns.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_3_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                } else if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_lt_feeder_3.setVisibility(View.GONE);
                    ed_oh_lt_feeder_3_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_3_pole.setAdapter(adapter);
                } else {
                    layout_oh_lt_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_3_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_lt_feeder_3.getSelectedItem().toString();
                    /*String [] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[1];*/
                    //System.out.println("selectedFeeder 3 : "+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMIDTRMasterDownloadActivity.listLT.size(); j++) {
                        if (selectedFeeder.equals(PMIDTRMasterDownloadActivity.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(PMIDTRMasterDownloadActivity.listLT.get(j).getGLAT(), PMIDTRMasterDownloadActivity.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMIDTRMasterDownloadActivity.listLT.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_3_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        get_oh_lt_feeder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_lt_feeder_1.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 1 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_1.getText().toString().equals(ed_oh_lt_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_1.getText().toString().equals(ed_oh_lt_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    //  getltKVFeeder1Pole();
                }
            }
        });

        get_oh_lt_feeder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_lt_feeder_2.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 2 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_2.getText().toString().equals(ed_oh_lt_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_2.getText().toString().equals(ed_oh_lt_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    //   getltKVFeeder2Pole();
                }
            }
        });

        get_oh_lt_feeder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_lt_feeder_3.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 3 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_3.getText().toString().equals(ed_oh_lt_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_3.getText().toString().equals(ed_oh_lt_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    //  getltKVFeeder3Pole();
                }
            }
        });

        sp_oh_lt_feeder_1_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_lt_pole_1.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_lt_pole_1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_lt_feeder_2_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_lt_pole_2.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_lt_pole_2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_lt_feeder_3_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_lt_pole_3.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_lt_pole_3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void get33KVFeeder1Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_33_feeder_1.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            // arcGISDatabase.clearMultiCktData();
                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("33KV").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                List<String> list = new ArrayList<>();
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 1 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLONG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);

                                    }
                                }
                                list.add("Not in list(Enter Pole number)");

                                if (mapList.size() > 0) {
                                    // loadMultiVoltageMap("get","33",mapList);
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_1_pole.setAdapter(adapter);
                            } else {

                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_1_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get33KVFeeder2Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_33_feeder_2.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("33KV").getAsJsonArray();
                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 2 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLONG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    //  loadMultiVoltageMap("get","33",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_2_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_2_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get33KVFeeder3Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_33_feeder_3.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("33KV").getAsJsonArray();
                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 3 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLONG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }
                                }

                                if (mapList.size() > 0) {
                                    //  loadMultiVoltageMap("get","33",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_3_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_3_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get11KVFeeder1Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_11_feeder_1.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            // arcGISDatabase.clearMultiCktData();

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("11KV").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 1 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLOG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }

                                }
                                if (mapList.size() > 0) {
                                    // loadMultiVoltageMap("get","11",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_1_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_1_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get11KVFeeder2Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_11_feeder_2.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("11KV").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 2 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLOG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    // loadMultiVoltageMap("get","11",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_2_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_2_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get11KVFeeder3Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_11_feeder_3.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("11KV").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 3 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLOG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    // loadMultiVoltageMap("get","11",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_3_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_3_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openImageDialog(int CAMERA, int GALLERY) {
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(c, R.style.MyDialogTheme);
        builder1.setTitle("Upload Photo");
        builder1.setMessage("Please choose an option");
        builder1.setCancelable(true);
        builder1.setPositiveButton(Html.fromHtml("<font color='#14B457'>Camera</font>"), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
               /* intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);*/
                c.startActivityForResult(intent, CAMERA);

                /*int permission = ActivityCompat.checkSelfPermission(c, Manifest.permission.CAMERA);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    verifyStoragePermissions(c, CAMERA_REQUEST_CODE_IMAGE_1);

                } else {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                    c.startActivityForResult(intent, CAMERA_REQUEST_CODE_IMAGE_1);
                }*/

            }
        });
        builder1.setNegativeButton(Html.fromHtml("<font color='#14B457'>Gallery</font>"), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


                if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(c, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_RESULT_CODE_IMAGE_1);
//                    return;
                } else {
                    launchGalleryIntent(GALLERY);
                }
            }
        });

        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void launchGalleryIntent(int RequestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        c.startActivityForResult(Intent.createChooser(intent, "Select Picture"), RequestCode);
    }


    private class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        try {

            switch (v.getId()) {
                case R.id.btn_repeat_pole:
                    Animation animation = AnimationUtils.loadAnimation(c, R.anim.bounce);
                    btn_repeat_pole.startAnimation(animation);
                    image_1_clicked = false;
                    image_2_clicked = false;
                    image_3_clicked = false;
                    repeatPoleClicked = true;
                    //re_pole();
                    repeatPoleNew();
                    break;

                case R.id.btn_clear:
                    sp_poleheight_KEY = "N";
                    deligate.IsSetData("add Record Not Sucess", "1");
                    callout.dismiss();
                    image_1_clicked = false;
                    image_2_clicked = false;
                    image_3_clicked = false;
                    repeatPoleClicked = false;
                    graphicsOverlay.getGraphics().remove(graphic);
                    dismiss();
                    break;
                case R.id.btn_topclear:
                    callout.dismiss();
                    image_1_clicked = false;
                    image_2_clicked = false;
                    image_3_clicked = false;
                    repeatPoleClicked = false;
                    graphicsOverlay.getGraphics().remove(graphic);
                    dismiss();
                    break;
                case R.id.btn_add:
                    currentgraphicsOverlay.getGraphics().remove(currentGraphic);

                    if (ed_pole_no.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Pole No", Toast.LENGTH_LONG).show();
                    } else if (ed_frompole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter From Pole ", Toast.LENGTH_LONG).show();
                    } else {
                        if ("Overhead".equalsIgnoreCase(lineType)) {
                            if (defect_selected.equalsIgnoreCase("Yes")) {
                                validateDefects();
                            } else {
                                overHeadData();
                            }

                        } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                            underGroundData();
                        } else if ("AB Cable/Covered Conductor".equalsIgnoreCase(lineType)) {
                            abCableData();
                        }
                    }

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void re_pole() {

        sp_poleheight_KEY = "R";

        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM ARCGIS_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM ARCGIS_TABLE_REPEAT)-1");
        if (surveyVoList.size() > 0) {


            for (PMISurveyVo surveyVo : surveyVoList) {


              /*  abc_vo.setSurvaytype(Survey_type_string);

                sp_Survey_type*/

                if (surveyVo.getSurvaytype().equals("Existing")) {
                    sp_Survey_type.setSelection(1);
                } else if (surveyVo.getSurvaytype().equals("JHC Scheme")) {
                    sp_Survey_type.setSelection(2);
                }


                if (surveyVo.getLineType().equals("Overhead")) {
                    sp_lineType.setSelection(0);
                } else if (surveyVo.getLineType().equals("UnderGround")) {
                    sp_lineType.setSelection(1);
                }


                if (surveyVo.getVoltage_level().equals("11KV")) {

                    sp_oh_voltage_level.setSelection(0);
                } else if (surveyVo.getVoltage_level().equals("6.3 KV")) {

                    sp_oh_voltage_level.setSelection(1);

                }


                if (surveyVo.getIsEndPole().equals("N")) {

                    sp_isendpole.setSelection(0);
                } else {

                    sp_isendpole.setSelection(1);
                }

                String s = surveyVo.getPole_type();
                List<String> psccList = new ArrayList<>();

                psccList.add(s);
                psccList.add("Select");
                psccList.add("SS BAY");
                psccList.add("PSCC");
                psccList.add("SPUN");
                psccList.add("RAIL");
                psccList.add("RS JOIST");
                psccList.add("TOWER");
                psccList.add("TUBULAR POLE");
                List<String> newList = psccList.stream().distinct().collect(Collectors.toList());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, newList);
                sp_poletype.setAdapter(arrayAdapter);


                String s_poleheight = surveyVo.getPole_support();
                List<String> getPole_height = new ArrayList<>();
                getPole_height.add(s_poleheight);
                ArrayAdapter<String> adapter_h = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, getPole_height);
                sp_polesupport.setAdapter(adapter_h);

                String sp_conductorsizee = surveyVo.getConductor_size();
                List<String> sp_sp_conductorsize_ll = new ArrayList<>();
                sp_sp_conductorsize_ll.add(sp_conductorsizee);
                ArrayAdapter<String> adapter_conductorsize = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, sp_sp_conductorsize_ll);
                sp_conductorsize.setAdapter(adapter_conductorsize);


                String taping_point = surveyVo.getTapping_point();
                List<String> sp_taping_point_ll = new ArrayList<>();
                sp_taping_point_ll.add(taping_point);
                ArrayAdapter<String> adapter_sp_taping_point_ll = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, sp_taping_point_ll);
                sp_tapingpoint.setAdapter(adapter_sp_taping_point_ll);


            }

        }


    }

    private void repeatPoleNew() {
        sp_poleheight_KEY = "R";
        repeatPoleClicked = true;
        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM ARCGIS_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM ARCGIS_TABLE_REPEAT)-1");
        if (surveyVoList.size() > 0) {
            for (PMISurveyVo surveyVo : surveyVoList) {

                //POLE HEIGHT
                poleHeightRepeat = surveyVo.getPole_height();

                if (LoginActivity.isJHC) {
                    //Pole Survey Type
                    String pst = surveyVo.getPOLE_SURVEY_TYPE();
                    // System.out.println("POLE SURVEY TYPE : " + pst);
                    List<String> list_pst = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_survey_type)));
                    list_pst.remove(pst.equalsIgnoreCase("JHC") ? "HC Phase 1" : pst);
                    list_pst.remove("Select");
                    list_pst.add(0,pst.equalsIgnoreCase("JHC") ? "HC Phase 1" : pst);
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_pole_survey_type.setAdapter(adapter_pst);
                    sp_pole_survey_type.setSelection(0, true);

                    //Pole Survey Type
                    String lst = surveyVo.getLINE_SURVEY_TYPE();
                    // System.out.println("LINE SURVEY TYPE : " + lst);
                    List<String> list_lst = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_survey_type)));
                    list_lst.remove(lst.equalsIgnoreCase("JHC")?"HC Phase 1" : lst);
                    list_lst.remove("Select");
                    list_lst.add(0,lst.equalsIgnoreCase("JHC")?"HC Phase 1" : lst);
                    ArrayAdapter<String> adapter_lst = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_lst);
                    sp_line_survey_type.setAdapter(adapter_lst);
                    sp_line_survey_type.setSelection(0, true);
                }

                //LINE TYPE
                String lineType = surveyVo.getLineType();
                List<String> list1 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.line_type)));
                list1.remove(lineType);
                list1.add(0, lineType);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list1);
                sp_lineType.setAdapter(adapter1);
                sp_lineType.setSelection(0, true);

                //OVERHEAD
                if (lineType.equalsIgnoreCase("OverHead")) {
                    //VOLTAGE LEVEL
                    String lineConfig = surveyVo.getVoltage_level();
                    List<String> list2 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.voltage_leavel_oh)));
                    list2.remove(lineConfig);
                    list2.add(0, lineConfig);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list2);
                    sp_oh_voltage_level.setAdapter(adapter2);
                    sp_oh_voltage_level.setSelection(0, true);

                    //IS END POLE
                    String isEndPole = surveyVo.getIsEndPole();
                    List<String> list3 = new ArrayList<>();
                    if (isEndPole.equals("N")) {
                        list3.add("No");
                        list3.add("Yes");
                    } else {
                        list3.add("Yes");
                        list3.add("No");
                    }

                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list3);
                    sp_isendpole.setAdapter(adapter3);
                    sp_isendpole.setSelection(0, true);

                    //POLE TYPE
                    String poleType = surveyVo.getPole_type();
                    List<String> list9 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_type)));
                    list9.remove("Select");
                    list9.remove(poleType);
                    list9.add(0, poleType);
                    ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list9);
                    sp_poletype.setAdapter(adapter9);
                    // sp_oh_pole_type.setSelection(0,true);

                    //POLE SUPPORT
                    String poleSupport = surveyVo.getPole_support();
                    List<String> list_poleSupport = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_support)));
                    list_poleSupport.remove(poleSupport);
                    list_poleSupport.add(0, poleSupport);
                    ArrayAdapter<String> adapter_poleSupport = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_poleSupport);
                    sp_polesupport.setAdapter(adapter_poleSupport);
                    sp_polesupport.setSelection(0, true);

                    //TAPPING POINT
                    String tp = surveyVo.getTapping_point();
                    List<String> list12 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.taping_point)));
                    list12.remove("Select");
                    list12.remove(tp);
                    list12.add(0, tp);
                    ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list12);
                    sp_tapingpoint.setAdapter(adapter12);
                    sp_tapingpoint.setSelection(0, true);


                    if (tp.equalsIgnoreCase("Yes")) {

                        //NO OF TAPPINGS
                        String no_of_t = surveyVo.getNoofTappings();
                        List<String> list_no_of_t = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.nofof_tappings)));
                        list_no_of_t.remove("Select");
                        list_no_of_t.remove(no_of_t);
                        list_no_of_t.add(0, no_of_t);
                        ArrayAdapter<String> adapter_no_of_t = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_no_of_t);
                        sp_nooftappings.setAdapter(adapter_no_of_t);
                        sp_nooftappings.setSelection(0, true);

                        //TAPING LOCATION NAME

                        not = surveyVo.getTappingLocation();
                        edt_tappinglocation.setText(not);
                    }


                    //CONDUCTOR SIZE
                    String conductorSize = surveyVo.getConductor_size();
                    List<String> list4 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.conductor_size_11)));
                    list4.remove("Select");
                    list4.remove(conductorSize);
                    list4.add(0, conductorSize);
                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list4);
                    sp_conductorsize.setAdapter(adapter4);
                    sp_conductorsize.setSelection(0, true);

                    //PIN POINT
                    String pinPoint_r = surveyVo.getPinpoint();
                    List<String> list10 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pinpoint)));
                    list10.remove(pinPoint_r);
                    list10.add(0, pinPoint_r);
                    ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list10);
                    sp_pinpoint.setAdapter(adapter10);
                    sp_pinpoint.setSelection(0, true);

                    //CUT POINT
                    if (pinPoint_r.equals("Cutpoint")) {
                        //POLE STRUCTURE
                        String poleStructure = surveyVo.getPoleStructure();
                        List<String> listpoleStructure = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_structure)));
                        listpoleStructure.remove(poleStructure);
                        listpoleStructure.add(0, poleStructure);
                        ArrayAdapter<String> adapterpoleStructure = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, listpoleStructure);
                        sp__polestructure.setAdapter(adapterpoleStructure);
                        sp__polestructure.setSelection(0, true);
                    }

                    //Is Multi Circuit
                    String isMultiCircuit = surveyVo.getIsMultiCircuit();
                    // System.out.println("isMultiCircuit : " + isMultiCircuit);

                    List<String> list5 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.is_multi_circuit_11kv)));
                    list5.remove("Select");
                    list5.remove(isMultiCircuit);
                    list5.add(0, isMultiCircuit);
                    ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list5);
                    sp_ismulticircute.setAdapter(adapter5);
                    sp_ismulticircute.setSelection(0, true);

                    //No Of Circuits
                    if (isMultiCircuit.equalsIgnoreCase("Yes")) {
                        String noOfCircuits = surveyVo.getNoofCircuits();
                        // System.out.println("NO OF CIRCUITS : "+noOfCircuits);
                        List<String> list6 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.noof_circuit_overhead)));
                        list6.remove(noOfCircuits);
                        list6.add(0, noOfCircuits);
                        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list6);
                        sp_noofcircuit.setAdapter(adapter6);
                        sp_noofcircuit.setSelection(0, true);
                    }

                    //POLE CONCRETE
                    String pole_concrete = surveyVo.getPOLE_CONCRETE();
                    List<String> list_pole_concrete = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_concrete)));
                    list_pole_concrete.remove(pole_concrete);
                    list_pole_concrete.remove("Select");
                    list_pole_concrete.add(0, pole_concrete);
                    ArrayAdapter<String> adapter_pole_concrete = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_pole_concrete);
                    sp_pole_concrete.setAdapter(adapter_pole_concrete);
                    sp_pole_concrete.setSelection(0, true);

                }

                //UNDER GROUND
                if (lineType.equalsIgnoreCase("UnderGround")) {
                    //IS END POINT
                    String isEndPole = surveyVo.getIsEndPole();
                    List<String> list3 = new ArrayList<>();
                    if (isEndPole.equals("N")) {
                        list3.add("No");
                        list3.add("Yes");
                    } else {
                        list3.add("Yes");
                        list3.add("No");
                    }
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list3);
                    sp_undergisendpole.setAdapter(adapter3);
                    sp_undergisendpole.setSelection(0, true);

                    //NO OF CABLES
                    String noc = surveyVo.getNoofCircuits();
                    List<String> list2 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.noof_circuit)));
                    list2.remove(noc);
                    list2.remove("Select");
                    list2.add(0, noc);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list2);
                    sp_noofcircuit_underg.setAdapter(adapter2);
                    sp_noofcircuit_underg.setSelection(0, true);

                    //TAPPING POINT
                    String tp = surveyVo.getTapping_point();
                    List<String> list12 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.taping_point)));
                    list12.remove("Select");
                    list12.remove(tp);
                    list12.add(0, tp);
                    ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list12);
                    sp_ugtapingpoint.setAdapter(adapter12);
                    sp_ugtapingpoint.setSelection(0, true);


                    if (tp.equalsIgnoreCase("Yes")) {
                        //NO OF TAPPINGS
                        String no_of_t = surveyVo.getNoofTappings();
                        List<String> list_no_of_t = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.nofof_tappings)));
                        list_no_of_t.remove("Select");
                        list_no_of_t.remove(no_of_t);
                        list_no_of_t.add(0, no_of_t);
                        ArrayAdapter<String> adapter_no_of_t = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_no_of_t);
                        sp_ugnooftappings.setAdapter(adapter_no_of_t);
                        sp_ugnooftappings.setSelection(0, true);

                        //TAPING LOCATION NAME
                        not = surveyVo.getTappingLocation();
                        edt_ugtappinglocation.setText(not);
                    }


                    //CABLE SIZE
                    String cable_size = surveyVo.getConductor_size();
                    //NO Of Cables
                    String CABLE_TX = "CABLE";
                    List<String> list1_cable_size = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX + "'" + " ORDER BY CKT_VALUE");
                    //  List<String> list1_cable_size = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.taping_point)));
                    list1_cable_size.remove("Select");
                    list1_cable_size.remove(cable_size);
                    list1_cable_size.add(0, cable_size);
                    ArrayAdapter<String> adapter_cable_size = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list1_cable_size);
                    sp_cablesize.setAdapter(adapter_cable_size);
                    sp_cablesize.setSelection(0, true);

                }
            }
        }
    }

    private void validateDefects() {

        if (!ch_pole_defects.isChecked() && !ch_conductors.isChecked() && !ch_ab_switch_defects.isChecked()
                && !ch_insulators.isChecked() && !ch_top_cleat.isChecked() && !ch_restringing.isChecked() &&
                !ch_cross_arm.isChecked() && !ch_strut_pole.isChecked() && !ch_stray_wire.isChecked() &&
                !ch_tree_clearance.isChecked() &&
                !ch_ht_clearance.isChecked() && !ch_int_pole.isChecked() && !ch_other_defects.isChecked()) {
            Toast.makeText(c, "Please Select at least 1 Defect", Toast.LENGTH_SHORT).show();
            return;

        }


        if (ch_pole_defects.isChecked()) {
            if (sp_pole_defects.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Pole Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_conductors.isChecked()) {
            if (sp_conductors.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Conductor Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_ab_switch_defects.isChecked()) {
            if (sp_ab_switch_defects.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select AB Switch Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_insulators.isChecked()) {
            if (sp_insulators.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Insulators Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_cross_arm.isChecked()) {
            if (sp_cross_arm.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Cross Arm Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_top_cleat.isChecked()) {
            if (sp_top_cleat.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Top Cleat Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_ab_switch_defects.isChecked()) {
            if (sp_ab_switch_defects.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select  Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_strut_pole.isChecked()) {
            if (sp_strut_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Strut Pole Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_stray_wire.isChecked()) {
            if (sp_stray_wire.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Stay Wire Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_ht_clearance.isChecked()) {
            if (sp_ht_clearance.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select HT/LT Clearance Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_int_pole.isChecked()) {
            if (sp_int_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Intermediate Pole Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //  ,,ch_other_defects;
        if (ch_restringing.isChecked()) {
            if (sp_restringing.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Restringing Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_tree_clearance.isChecked()) {
            if (sp_tree_clearance.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Tree Clearance Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_other_defects.isChecked()) {
            if (ed_other_defects.getText().toString().trim().isEmpty()) {
                Toast.makeText(c, "Please Enter Other Defects", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (!image_1_clicked && !image_2_clicked && !image_3_clicked) {
            Toast.makeText(c, "Upload at least 1 photo", Toast.LENGTH_SHORT).show();
            return;
        }

        overHeadData();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            switch (adapterView.getId()) {
                case R.id.sp_ug_is_dtr_avalabile:
                    ug_is_dtr_avalabileStr = sp_ug_is_dtr_avalabile.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)) {
                        lay_ug_is_dtr_avalable.setVisibility(View.VISIBLE);
                    } else {
                        lay_ug_is_dtr_avalable.setVisibility(View.GONE);
                        sp_ug_phase_dtr_pole.setSelection(0);
                        sp_ug_capacity_dtr_pole.setSelection(0);
                        sp_ug_type_of_load.setSelection(0);
                        sp_ug_dtr_location_type.setSelection(0);
                        sp_ug_is_lt_ckts_available.setSelection(0);
                        sp_ug_no_of_lt_circuits.setSelection(0);
                        sp_ug_ab_switch_dtr_pole.setSelection(0);
                        auto_ug_make_dtr_pole.setText("");
                        ed_ug_serial_number_dtr_pole.setText("");
                        ed_ug_location_name_dtr_pole.setText("");
                    }
                    break;
                case R.id.sp_oh_is_dtr_avalabile:
                    oh_is_dtr_avalabileStr = sp_oh_is_dtr_avalabile.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)) {
                        lay_oh_is_dtr_avalable.setVisibility(View.VISIBLE);

                        if (LoginActivity.isJHC) {
                            lyt_oh_dtr_survey_type.setVisibility(View.VISIBLE);
                        }


                        if (!selected_dtr_code.equals("Select")) {
                            List<PMIDTRMaster_vo> dtrMasterData = arcGISDatabase.getDTRMasterData();
                            PMIDTRMaster_vo dtrData = new PMIDTRMaster_vo();
                            for (int j = 0; j < dtrMasterData.size(); j++) {
                                if (selected_dtr_code.equalsIgnoreCase(dtrMasterData.get(j).getDTR_DTR_STRUCTURE_CODE())) {
                                    dtrData = dtrMasterData.get(j);
                                }
                            }

                            auto_oh_make_dtr_pole.setText(dtrData.getDTR_MAKER());
                            ed_oh_serial_number_dtr_pole.setText(dtrData.getDTR_SLNO());
                            ed_oh_location_name_dtr_pole.setText(dtrData.getDTR_LOCATION());
                            List<String> oh_phase_I = new ArrayList<>();
                            if (dtrData.getDTR_PHASE().equalsIgnoreCase("3")) {
                                oh_phase_I.add("III");
                                oh_phase_I.add("II");
                                oh_phase_I.add("I");
                            }
                            if (dtrData.getDTR_PHASE().equalsIgnoreCase("2")) {
                                oh_phase_I.add("II");
                                oh_phase_I.add("I");
                                oh_phase_I.add("III");
                            }
                            if (dtrData.getDTR_PHASE().equalsIgnoreCase("1")) {
                                oh_phase_I.add("I");
                                oh_phase_I.add("II");
                                oh_phase_I.add("III");
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase_I);
                            sp_oh_phase_dtr_pole.setAdapter(adapter);
                        }


                    } else {
                        lay_oh_is_dtr_avalable.setVisibility(View.GONE);
                        if (LoginActivity.isJHC) {
                            lyt_oh_dtr_survey_type.setVisibility(View.GONE);
                        }
                        sp_oh_phase_dtr_pole.setSelection(0);
                        sp_oh_capacity_dtr_pole.setSelection(0);
                        sp_oh_type_of_load.setSelection(0);
                        sp_oh_dtr_location_type.setSelection(0);
                        sp_oh_is_lt_ckts_available.setSelection(0);
                        sp_oh_no_of_lt_circuits.setSelection(0);
                        sp_oh_ab_switch_dtr_pole.setSelection(0);
                        auto_oh_make_dtr_pole.setText("");
                        ed_oh_serial_number_dtr_pole.setText("");
                        ed_oh_location_name_dtr_pole.setText("");
                    }
                    break;
                case R.id.sp_oh_type_of_load:
                    sp_oh_type_of_loadStr = sp_oh_type_of_load.getSelectedItem().toString();
                    sp_oh_dtr_location_type.setSelection(0);
                    if ("Agl".equalsIgnoreCase(sp_oh_type_of_loadStr)) {
                        List<String> aglcList = new ArrayList<>();
                        aglcList.add("Select");
                        aglcList.add("Aqua");
                        aglcList.add("Non-Aqua");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, aglcList);
                        sp_oh_dtr_location_type.setAdapter(adapter);
                    } else if ("Industrial".equalsIgnoreCase(sp_oh_type_of_loadStr)) {
                        List<String> inducList = new ArrayList<>();
                        inducList.add("Select");
                        inducList.add("Dedicated");
                        inducList.add("Non dedicated");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, inducList);
                        sp_oh_dtr_location_type.setAdapter(adapter);

                    } else if ("Commercial".equalsIgnoreCase(sp_oh_type_of_loadStr) || "Domestic".equalsIgnoreCase(sp_oh_type_of_loadStr) || "Mixed".equalsIgnoreCase(sp_oh_type_of_loadStr)) {
                        List<String> comercialList = new ArrayList<>();
                        comercialList.add("Select");
                        comercialList.add("Field DTR");
                        comercialList.add("Apartment");
                        comercialList.add("Group House");
                        comercialList.add("Commercial Complex");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, comercialList);
                        sp_oh_dtr_location_type.setAdapter(adapter);
                    } else {
                        List<String> selectcList = new ArrayList<>();
                        selectcList.add("Select");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, selectcList);
                        sp_oh_dtr_location_type.setAdapter(adapter);
                    }
                    break;

                case R.id.sp_ug_type_of_load:
                    sp_ug_type_of_loadStr = sp_ug_type_of_load.getSelectedItem().toString();
                    sp_ug_dtr_location_type.setSelection(0);
                    if ("Agl".equalsIgnoreCase(sp_ug_type_of_loadStr)) {
                        List<String> aglcList = new ArrayList<>();
                        aglcList.add("Select");
                        aglcList.add("Aqua");
                        aglcList.add("Non-Aqua");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, aglcList);
                        sp_ug_dtr_location_type.setAdapter(adapter);
                    } else if ("Industrial".equalsIgnoreCase(sp_ug_type_of_loadStr)) {
                        List<String> inducList = new ArrayList<>();
                        inducList.add("Select");
                        inducList.add("Dedicated");
                        inducList.add("Non dedicated");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, inducList);
                        sp_ug_dtr_location_type.setAdapter(adapter);

                    } else if ("Commercial".equalsIgnoreCase(sp_ug_type_of_loadStr) || "Domestic".equalsIgnoreCase(sp_ug_type_of_loadStr) || "Mixed".equalsIgnoreCase(sp_ug_type_of_loadStr)) {
                        List<String> comercialList = new ArrayList<>();
                        comercialList.add("Select");
                        comercialList.add("Field DTR");
                        comercialList.add("Apartment");
                        comercialList.add("Group House");
                        comercialList.add("Commercial Complex");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, comercialList);
                        sp_ug_dtr_location_type.setAdapter(adapter);
                    } else {
                        List<String> selectcList = new ArrayList<>();
                        selectcList.add("Select");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, selectcList);
                        sp_ug_dtr_location_type.setAdapter(adapter);
                    }
                    break;

                case R.id.sp_oh_other_cables:
                    if (sp_oh_other_cables.getSelectedItem().toString().equals("Yes")) {
                        edt_oh_other_cables.setVisibility(View.VISIBLE);
                    } else {
                        edt_oh_other_cables.setVisibility(View.GONE);
                    }
                    break;

                case R.id.sp_abc_type_of_load:
                    sp_abc_type_of_loadStr = sp_abc_type_of_load.getSelectedItem().toString();
                    sp_abc_dtr_location_type.setSelection(0);
                    if ("Agl".equalsIgnoreCase(sp_abc_type_of_loadStr)) {
                        List<String> aglcList = new ArrayList<>();
                        aglcList.add("Select");
                        aglcList.add("Aqua");
                        aglcList.add("Non-Aqua");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, aglcList);
                        sp_abc_dtr_location_type.setAdapter(adapter);
                    } else if ("Industrial".equalsIgnoreCase(sp_abc_type_of_loadStr)) {
                        List<String> inducList = new ArrayList<>();
                        inducList.add("Select");
                        inducList.add("Dedicated");
                        inducList.add("Non dedicated");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, inducList);
                        sp_abc_dtr_location_type.setAdapter(adapter);

                    } else if ("Commercial".equalsIgnoreCase(sp_abc_type_of_loadStr) || "Domestic".equalsIgnoreCase(sp_abc_type_of_loadStr) || "Mixed".equalsIgnoreCase(sp_abc_type_of_loadStr)) {
                        List<String> comercialList = new ArrayList<>();
                        comercialList.add("Select");
                        comercialList.add("Field DTR");
                        comercialList.add("Apartment");
                        comercialList.add("Group House");
                        comercialList.add("Commercial Complex");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, comercialList);
                        sp_abc_dtr_location_type.setAdapter(adapter);
                    } else {
                        List<String> selectcList = new ArrayList<>();
                        selectcList.add("Select");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, selectcList);
                        sp_abc_dtr_location_type.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_oh_is_lt_ckts_available:
                    oh_is_lt_ckts_availableStr = sp_oh_is_lt_ckts_available.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr)) {
                        lay_oh_no_of_lt_circuits.setVisibility(View.VISIBLE);
                        if (LoginActivity.isJHC) {
                            lyt_oh_lt_survey_type.setVisibility(View.VISIBLE);
                        }
                    } else {
                        lay_oh_no_of_lt_circuits.setVisibility(View.GONE);
                        if (LoginActivity.isJHC) {
                            lyt_oh_lt_survey_type.setVisibility(View.GONE);
                        }
                        sp_oh_no_of_lt_circuits.setSelection(0);
                    }
                    break;
                case R.id.sp_ug_is_lt_ckts_available:
                    ug_is_lt_ckts_availableStr = sp_ug_is_lt_ckts_available.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ug_is_lt_ckts_availableStr)) {
                        lay_ug_no_of_lt_circuits.setVisibility(View.VISIBLE);
                    } else {
                        lay_ug_no_of_lt_circuits.setVisibility(View.GONE);
                        sp_ug_no_of_lt_circuits.setSelection(0);
                    }
                    break;
                case R.id.sp_abc_is_lt_ckts_available:
                    abc_is_lt_ckts_availableStr = sp_abc_is_lt_ckts_available.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(abc_is_lt_ckts_availableStr)) {
                        lay_abc_no_of_lt_circuits.setVisibility(View.VISIBLE);
                    } else {
                        lay_abc_no_of_lt_circuits.setVisibility(View.GONE);
                        sp_abc_no_of_lt_circuits.setSelection(0);
                    }
                    break;
                case R.id.sp_poletype:
                    poleType = sp_poletype.getSelectedItem().toString();
                    /*if (sp_poleheight_KEY.equals("N")) {
                        if ("PSCC".equalsIgnoreCase(poleType)) {
                            List<String> psccList = new ArrayList<>();
                            psccList.add("Select");
                            psccList.add("8.0");
                            psccList.add("9.1");
                            psccList.add("11");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, psccList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("SPUN".equalsIgnoreCase(poleType)) {
                            List<String> spunList = new ArrayList<>();
                            spunList.add("Select");
                            spunList.add("9.5");
                            spunList.add("11");
                            spunList.add("12.5");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, spunList);
                            sp_poleheight.setAdapter(adapter);

                        } else if ("RAIL".equalsIgnoreCase(poleType)) {
                            List<String> railList = new ArrayList<>();
                            railList.add("Select");
                            railList.add("9.5");
                            railList.add("11");
                            railList.add("12.5");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, railList);
                            sp_poleheight.setAdapter(adapter);

                        } else if ("RS JOIST".equalsIgnoreCase(poleType)) {
                            List<String> rssJoistList = new ArrayList<>();
                            rssJoistList.add("Select");
                            *//*rssJoistList.add("8");*//*
                            rssJoistList.add("9.1");
                            rssJoistList.add("11");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rssJoistList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("TOWER".equalsIgnoreCase(poleType)) {
                            List<String> towertList = new ArrayList<>();
                            towertList.add("Select");
                            towertList.add("M+3");
                            towertList.add("M+6");
                            towertList.add("M+9");
                            towertList.add("M+12");
                            towertList.add("R+6");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, towertList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("Tubular Pole".equalsIgnoreCase(poleType)) {

                            List<String> tubularList = new ArrayList<>();
                            tubularList.add("Select");
                            tubularList.add("9.1");
                            tubularList.add("11");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, tubularList);
                            sp_poleheight.setAdapter(adapter);
                        }
                    }      else if (sp_poleheight_KEY.equals("R")) {

                        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM ARCGIS_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM ARCGIS_TABLE_REPEAT)-1");
                        if (surveyVoList.size() > 0) {

                            for (SurveyVo surveyVo : surveyVoList) {

                                String str_key = surveyVo.getPole_height();

                                List<String> psccList = new ArrayList<>();

                                List<String> rssJoistList = new ArrayList<>();
                                rssJoistList.add(str_key);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rssJoistList);
                                sp_poleheight.setAdapter(adapter);





                            }




                        }
                    }*/

                    if (repeatPoleClicked) {
                        if ("PSCC".equalsIgnoreCase(poleType)) {
                            List<String> psccList = new ArrayList<>();
                            // psccList.add("Select");
                            psccList.add("8.0");
                            psccList.add("9.1");
                            psccList.add("11");
                            psccList.remove(poleHeightRepeat);
                            psccList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, psccList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("SPUN".equalsIgnoreCase(poleType)) {
                            List<String> spunList = new ArrayList<>();
                            // spunList.add("Select");
                            spunList.add("9.5");
                            spunList.add("11");
                            spunList.add("12.5");
                            spunList.remove(poleHeightRepeat);
                            spunList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, spunList);
                            sp_poleheight.setAdapter(adapter);

                        } else if ("RAIL".equalsIgnoreCase(poleType)) {
                            List<String> railList = new ArrayList<>();
                            // railList.add("Select");
                            railList.add("9.5");
                            railList.add("11");
                            railList.add("12.5");
                            railList.remove(poleHeightRepeat);
                            railList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, railList);
                            sp_poleheight.setAdapter(adapter);

                        } else if ("RS JOIST".equalsIgnoreCase(poleType)) {
                            List<String> rssJoistList = new ArrayList<>();
                            // rssJoistList.add("Select");
                            /*rssJoistList.add("8");*/
                            rssJoistList.add("9.1");
                            rssJoistList.add("11");
                            rssJoistList.remove(poleHeightRepeat);
                            rssJoistList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rssJoistList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("TOWER".equalsIgnoreCase(poleType)) {
                            List<String> towertList = new ArrayList<>();
                            //  towertList.add("Select");
                            towertList.add("M+3");
                            towertList.add("M+6");
                            towertList.add("M+9");
                            towertList.add("M+12");
                            towertList.add("R+6");
                            towertList.remove(poleHeightRepeat);
                            towertList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, towertList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("Tubular Pole".equalsIgnoreCase(poleType)) {

                            List<String> tubularList = new ArrayList<>();
                            // tubularList.add("Select");
                            tubularList.add("9.1");
                            tubularList.add("11");
                            tubularList.remove(poleHeightRepeat);
                            tubularList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, tubularList);
                            sp_poleheight.setAdapter(adapter);
                        }
                    } else {
                        if ("PSCC".equalsIgnoreCase(poleType)) {
                            List<String> psccList = new ArrayList<>();
                            psccList.add("Select");
                            psccList.add("8.0");
                            psccList.add("9.1");
                            psccList.add("11");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, psccList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("SPUN".equalsIgnoreCase(poleType)) {
                            List<String> spunList = new ArrayList<>();
                            spunList.add("Select");
                            spunList.add("9.5");
                            spunList.add("11");
                            spunList.add("12.5");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, spunList);
                            sp_poleheight.setAdapter(adapter);

                        } else if ("RAIL".equalsIgnoreCase(poleType)) {
                            List<String> railList = new ArrayList<>();
                            railList.add("Select");
                            railList.add("9.5");
                            railList.add("11");
                            railList.add("12.5");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, railList);
                            sp_poleheight.setAdapter(adapter);

                        } else if ("RS JOIST".equalsIgnoreCase(poleType)) {
                            List<String> rssJoistList = new ArrayList<>();
                            rssJoistList.add("Select");
                            /*rssJoistList.add("8");*/
                            rssJoistList.add("9.1");
                            rssJoistList.add("11");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rssJoistList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("TOWER".equalsIgnoreCase(poleType)) {
                            List<String> towertList = new ArrayList<>();
                            towertList.add("Select");
                            towertList.add("M+3");
                            towertList.add("M+6");
                            towertList.add("M+9");
                            towertList.add("M+12");
                            towertList.add("R+6");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, towertList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("Tubular Pole".equalsIgnoreCase(poleType)) {

                            List<String> tubularList = new ArrayList<>();
                            tubularList.add("Select");
                            tubularList.add("9.1");
                            tubularList.add("11");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, tubularList);
                            sp_poleheight.setAdapter(adapter);
                        }
                    }

                    break;
                case R.id.sp_poleheight:
                    poleHeight = sp_poleheight.getSelectedItem().toString();
                    repeatPoleClicked = false;
                    break;
                case R.id.sp_polesupport:
                    poleSupport = sp_polesupport.getSelectedItem().toString();
                    break;
                case R.id.sp_tapingpoint:
                    taping_Point = sp_tapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(taping_Point)) {
                        lyt_tapping.setVisibility(View.VISIBLE);
                        if (repeatPoleClicked) {
                            edt_tappinglocation.setText(not);
                        }
                    } else {
                        lyt_tapping.setVisibility(View.GONE);
                        nooftappings = "";
                        sp_nooftappings.setSelection(0);
                        edt_tappinglocation.setText("");
                    }
                    break;
                case R.id.sp_conductorsize:
                    conductorsize = sp_conductorsize.getSelectedItem().toString();
                    break;
                case R.id.sp_pinpoint:
                    pinpoint = sp_pinpoint.getSelectedItem().toString();
                    if ("Cutpoint".equalsIgnoreCase(pinpoint)) {
                        lyt_polestructure.setVisibility(View.VISIBLE);
                    } else {
                        lyt_polestructure.setVisibility(View.GONE);
                        polestructure = "";
                        sp__polestructure.setSelection(0);
                    }
                    break;

                case R.id.sp_poleconfiguration:
                    poleconfiguration = sp_poleconfiguration.getSelectedItem().toString();
                    /*if ("SS-Bay".equalsIgnoreCase(poleconfiguration)) {
                        lyt_ssname.setVisibility(View.VISIBLE);
                        lay_dtr_pole_main.setVisibility(View.GONE);
                        //List<String> sunStationName=arcGISDatabase.getFeederId(String.format("SELECT DISTINCT EHT_SS_NAME FROM SSMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'",feederCode));
                        List<String> sunStationName = arcGISDatabase.getSSNAMEList();
                        sunStationName.add("Select");
                        Collections.reverse(sunStationName);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, sunStationName);
                        sp_ssname.setAdapter(adapter);

                    } else */
                    ssname = "";
                    sp_oh_line_capacitor.setSelection(0);
                    sp_oh_auto_reclosure.setSelection(0);
                    sp_oh_no_of_ab_switches.setSelection(0);
                    clearTextAfterSelectedOtherFiled_OH();

                    /*if (poleconfiguration.equals("Line-Pole") || poleconfiguration.equals("Select")) {
                        images_layout.setVisibility(View.GONE);
                    } else {
                        images_layout.setVisibility(View.VISIBLE);
                    }*/

                    if ("DTR Pole".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_dtr_pole_main.setVisibility(View.VISIBLE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                        dtrsectionList_oh = arcGISDatabase.getAll_DtrStructureSubStationMaster();
                        dtrsectionList_oh.add("Select");
                        Collections.reverse(dtrsectionList_oh);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                                android.R.layout.simple_spinner_dropdown_item, dtrsectionList_oh);
                        sp_oh_section_name_dtr_pole.setAdapter(adapter);
                    } else if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.VISIBLE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                        List<String> oh_ht_servicenoList1 = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
                        oh_ht_servicenoList1.add("Select");
                        Collections.reverse(oh_ht_servicenoList1);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_ht_servicenoList1);
                        auto_oh_ht_service_number.setAdapter(adapterMake_ug);
                        auto_oh_ht_service_number.setThreshold(1);

                        // Toast.makeText(c,"Size"+oh_ht_servicenoList.size(),Toast.LENGTH_LONG).show();
                    } else if ("Line Capacitor".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.VISIBLE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                    } else if ("Auto Reclosure".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.VISIBLE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                    } else if ("AB-Pole".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_no_of_ab_switches.setVisibility(View.VISIBLE);
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                    } else {
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        ssname = "";
                        auto_oh_ht_service_number.setText("");
                    }

                    break;

                case R.id.sp__polestructure:
                    polestructure = sp__polestructure.getSelectedItem().toString();
                    break;
                case R.id.sp_ismulticircute:
                    ismulticircute = sp_ismulticircute.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ismulticircute)) {
                        lyt_circuit.setVisibility(View.VISIBLE);
                        lyt_diffvoltage.setVisibility(View.GONE);

                        sp_circutetype.setSelection(0);
                        sp_noofcircuit.setSelection(0);
                        sp_diffvoltage.setSelection(0);

                    } else {
                        sp_circutetype.setSelection(0);
                        lyt_circuit.setVisibility(View.GONE);
                        lyt_diffvoltage.setVisibility(View.GONE);
                        lyt_oh_multivoltage.setVisibility(View.GONE);
                        sp_noofcircuit.setSelection(0);
                        sp_diffvoltage.setSelection(0);
                        noofcircuit = "";
                        circutetype = "";
                    }
                    break;
                case R.id.sp_noofcircuit:
                    noofcircuit = sp_noofcircuit.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(noofcircuit)) {
                        try {
                            lyt_oh_multivoltage.setVisibility(View.VISIBLE);
                            List<String> deffvoltageListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + noofcircuit + "'" + " ORDER BY CKT_VALUE");
                            deffvoltageListDatabe.add("Select");
                            Collections.reverse(deffvoltageListDatabe);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, deffvoltageListDatabe);
                            sp_diffvoltage.setAdapter(adapter);
                        } catch (Exception e) {
                        }
                    } else {
                        lyt_oh_multivoltage.setVisibility(View.GONE);
                    }

                    /*if ("2".equalsIgnoreCase(noofcircuit)) {
                        List<String> twoCircuits = new ArrayList<>();
                        twoCircuits.add("Select");
                        twoCircuits.add("33kv");
                        twoCircuits.add("6.3kv");
                        twoCircuits.add("LT");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, twoCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("3".equalsIgnoreCase(noofcircuit)) {
                        List<String> threeCircuits = new ArrayList<>();
                        threeCircuits.add("Select");
                        threeCircuits.add("(33 kv-1)+(11 kv-1)");
                        threeCircuits.add("33 kv-2");
                        threeCircuits.add("(33 kv-1)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, threeCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("4".equalsIgnoreCase(noofcircuit)) {
                        List<String> fourCircuits = new ArrayList<>();
                        fourCircuits.add("Select");
                        fourCircuits.add("11 kv-2/33kv-1");
                        fourCircuits.add("11 kv- 1/33kv-2");
                        fourCircuits.add("33 kv-1/11 kv-1+LT-1");
                        fourCircuits.add("33 kv-3");
                        fourCircuits.add("33 kv-2+LT-1");
                        fourCircuits.add("11 kv-2+LT-1");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, fourCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("5".equalsIgnoreCase(noofcircuit)) {
                        List<String> fiveCircuits = new ArrayList<>();
                        fiveCircuits.add("Select");
                        fiveCircuits.add("(11 kv-2)+(33 kv-1)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, fiveCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    }*/

                    break;
                case R.id.sp_circutetype:
                    circutetype = sp_circutetype.getSelectedItem().toString();
                    if ("Multi Voltage".equalsIgnoreCase(circutetype)) {
                        lyt_diffvoltage.setVisibility(View.GONE);
                        sp_diffvoltage.setSelection(0);
                    } else {
                        lyt_diffvoltage.setVisibility(View.GONE);
                        sp_diffvoltage.setSelection(0);
                        diffvoltage = "";
                    }
                    break;
                case R.id.sp_diffvoltage:
                    diffvoltage = sp_diffvoltage.getSelectedItem().toString();

                    break;
                case R.id.sp_isendpole:
                    isendpole = sp_isendpole.getSelectedItem().toString();
                    if ("No".equalsIgnoreCase(isendpole)) {
                        endPole = "N";
                    } else {
                        endPole = "Y";
                    }
                    break;

                case R.id.sp_lineType:
                    lineType = sp_lineType.getSelectedItem().toString();
                    if ("Overhead".equalsIgnoreCase(lineType)) {
                        lyt_overhead.setVisibility(View.VISIBLE);
                        lyt_underground.setVisibility(View.GONE);
                        lay_ab_cable_main.setVisibility(View.GONE);
                        clearAllDataFileds();
                    } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                        lyt_underground.setVisibility(View.VISIBLE);
                        lyt_overhead.setVisibility(View.GONE);
                        lay_ab_cable_main.setVisibility(View.GONE);
                        clearAllDataFileds();
                    } else if ("AB Cable/Covered Conductor".equalsIgnoreCase(lineType)) {
                        lyt_underground.setVisibility(View.GONE);
                        lyt_overhead.setVisibility(View.GONE);
                        lay_ab_cable_main.setVisibility(View.VISIBLE);
                        clearAllDataFileds();
                    }
                    break;

                case R.id.sp_cablesize:
                    cablesize = sp_cablesize.getSelectedItem().toString();

                    break;
                case R.id.sp_noofcircuit_underg:
                    noofcircuit_underg = sp_noofcircuit_underg.getSelectedItem().toString();

                    break;


                case R.id.sp_straight_rmu:
                    straight_rmu = sp_straight_rmu.getSelectedItem().toString();
                    sp_rmu.setSelection(0);
                    if ("RMU".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.VISIBLE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Structure".equalsIgnoreCase(straight_rmu)) {
                        lyt_underpolestructure.setVisibility(View.VISIBLE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lyt_rmu.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Pole".equalsIgnoreCase(straight_rmu)) {
                        lyt_ugpole.setVisibility(View.VISIBLE);
                        lyt_rmu.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("HT Service".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.VISIBLE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        List<String> ug_ht_servicenoList = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
                        ug_ht_servicenoList.add("Select");
                        Collections.reverse(ug_ht_servicenoList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_ht_servicenoList);
                        auto_ug_ht_service_number.setAdapter(adapterMake_ug);
                        auto_ug_ht_service_number.setThreshold(1);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Line Capacitor".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.VISIBLE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Auto Reclosure".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.VISIBLE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("DTR".equalsIgnoreCase(straight_rmu)) {
                        clearSelecteAfterChangeTextFiledsDTR();
                        lay_ug_dtr_pole_main.setVisibility(View.VISIBLE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        dtr_ug_sectionList = arcGISDatabase.getAll_DtrStructureSubStationMaster();
                        dtr_ug_sectionList.add("Select");
                        Collections.reverse(dtr_ug_sectionList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                                android.R.layout.simple_spinner_dropdown_item, dtr_ug_sectionList);
                        sp_ug_section_name_dtr_pole.setAdapter(adapter);

                    } else if ("AB-Pole".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.VISIBLE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else {
                        clearSelecteAfterChangeTextFiledsDTR();
                        sp_rmu.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        rmu = "";
                        //nooftappings="";
                    }
                    break;

                case R.id.sp_rmu:
                    rmu = sp_rmu.getSelectedItem().toString();

                    break;
                case R.id.sp__underpolestructure:
                    underpolestructure = sp__underpolestructure.getSelectedItem().toString();

                    break;
                case R.id.sp_ugpoletype:
                    ugpoletype = sp_ugpoletype.getSelectedItem().toString();
                    sp_ugpoleheight.setSelection(0);
                    if ("PSCC".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugpsccList = new ArrayList<>();
                        ugpsccList.add("Select");
                        ugpsccList.add("8.0");
                        ugpsccList.add("9.1");
                        ugpsccList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugspunList = new ArrayList<>();
                        ugspunList.add("Select");
                        ugspunList.add("9.5");
                        ugspunList.add("11");
                        ugspunList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugrailList = new ArrayList<>();
                        ugrailList.add("Select");
                        ugrailList.add("9.5");
                        ugrailList.add("11");
                        ugrailList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugrailList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugrssJoistList = new ArrayList<>();
                        ugrssJoistList.add("Select");
                        /*rssJoistList.add("8");*/
                        ugrssJoistList.add("9.1");
                        ugrssJoistList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugrssJoistList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("TOWER".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugtowertList = new ArrayList<>();
                        ugtowertList.add("Select");
                        ugtowertList.add("M+3");
                        ugtowertList.add("M+6");
                        ugtowertList.add("M+9");
                        ugtowertList.add("M+12");
                        ugtowertList.add("R+6");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugtowertList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("Tubular Pole".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugtubularList = new ArrayList<>();
                        ugtubularList.add("Select");
                        ugtubularList.add("9.1");
                        ugtubularList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugtubularList);
                        sp_ugpoleheight.setAdapter(adapter);
                    }

                    break;

                case R.id.sp_ugpoleheight:
                    ugpoleheight = sp_ugpoleheight.getSelectedItem().toString();

                    break;

                case R.id.sp_ugpolesupport:
                    ugpolesupport = sp_ugpolesupport.getSelectedItem().toString();

                    break;


                case R.id.sp_ugtapingpoint:
                    ugtapingpoint = sp_ugtapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ugtapingpoint)) {
                        lyt_ugtapping.setVisibility(View.VISIBLE);
                        if (repeatPoleClicked) {
                            edt_ugtappinglocation.setText(not);
                        }
                    } else {
                        lyt_ugtapping.setVisibility(View.GONE);
                        ugnooftappings = "";
                        sp_ugnooftappings.setSelection(0);
                        edt_ugtappinglocation.setText("");
                    }

                    break;
                case R.id.sp_ugnooftappings:
                    ugnooftappings = sp_ugnooftappings.getSelectedItem().toString();

                    break;


                case R.id.sp_nooftappings:
                    nooftappings = sp_nooftappings.getSelectedItem().toString();
                    break;

                case R.id.sp_undergisendpole:
                    if ("No".equalsIgnoreCase(sp_undergisendpole.getSelectedItem().toString())) {
                        undergisendpole = "N";
                    } else {
                        undergisendpole = "Y";
                    }
                    break;

                case R.id.sp_ug_section_name_dtr_pole:
                    dtr_pole_ug_section_name = sp_ug_section_name_dtr_pole.getSelectedItem().toString();
                    if ("Select".equalsIgnoreCase(dtr_pole_ug_section_name)) {

                    } else {
                        dtr_ug_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_ug_section_name));
                        dtr_ug_structureList.add("Select");
                        Collections.reverse(dtr_ug_structureList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, dtr_ug_structureList);
                        auto_ug_dtr_structure_dtr_pole.setAdapter(adapterMake_ug);
                        auto_ug_dtr_structure_dtr_pole.setThreshold(1);
                    }
                    break;

                case R.id.sp_oh_section_name_dtr_pole:
                    dtr_pole_oh_section_name = sp_oh_section_name_dtr_pole.getSelectedItem().toString();
                    if ("Select".equalsIgnoreCase(dtr_pole_ug_section_name)) {

                    } else {
                        // dtr_ug_structureList=arcGISDatabase.getAll_DtrStructureCodeMaster(ug_section_id_spilt[0]);
                        dtr_oh_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_oh_section_name));
                        // dtr_oh_structureList.add("Select");
                        Collections.reverse(dtr_oh_structureList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, dtr_oh_structureList);
                        auto_oh_dtr_structure_dtr_pole.setAdapter(adapterMake_ug);
                        auto_oh_dtr_structure_dtr_pole.setThreshold(1);

                        /*auto_oh_dtr_structure_dtr_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selected_dtr_code = dtr_oh_structureList.get(position);
                                if (selected_dtr_code.equalsIgnoreCase("Select")){
                                    selected_dtr_code = "Select";
                                }else {
                                    selected_dtr_code = selected_dtr_code.split("~")[0];

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                        auto_oh_dtr_structure_dtr_pole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                selected_dtr_code = dtr_oh_structureList.get(position);

                               /* String[] arrOfStr = selected_dtr_code.split("~", 2);
                                selected_dtr_code = arrOfStr[0];*/
                                selected_dtr_code = selected_dtr_code.split("~")[0];
                                //  Toast.makeText(c, "SELECTED DTR : "+selected_dtr_code, Toast.LENGTH_SHORT).show();
                                //  System.out.println("SELECTED DTR : "+selected_dtr_code);
                            }
                        });


                    }
                    break;
                case R.id.sp_ug_phase_dtr_pole:
                    sp_ug_phase_dtr_pole_str = sp_ug_phase_dtr_pole.getSelectedItem().toString();

                    if ("I".equalsIgnoreCase(sp_ug_phase_dtr_pole_str)) {
                        List<String> ug_phase1 = new ArrayList<>();
                        ug_phase1.add("Select");
                        ug_phase1.add("5 kva");
                        ug_phase1.add("10 kva");
                        ug_phase1.add("15 kva");
                        ug_phase1.add("16 kva");
                        ug_phase1.add("25 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_phase1);
                        sp_ug_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("II".equalsIgnoreCase(sp_ug_phase_dtr_pole_str)) {
                        List<String> ug_phase2 = new ArrayList<>();
                        ug_phase2.add("Select");
                        ug_phase2.add("10 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_phase2);
                        sp_ug_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("III".equalsIgnoreCase(sp_ug_phase_dtr_pole_str)) {
                        List<String> ug_phase3 = new ArrayList<>();
                        ug_phase3.add("Select");
                        ug_phase3.add("16 kva");
                        ug_phase3.add("25 kva");
                        ug_phase3.add("40 kva");
                        ug_phase3.add("50 kva");
                        ug_phase3.add("63 kva");
                        ug_phase3.add("75 kva");
                        ug_phase3.add("100 kva");
                        ug_phase3.add("160 kva");
                        ug_phase3.add("200 kva");
                        ug_phase3.add("250 kva");
                        ug_phase3.add("315 kva");
                        ug_phase3.add("500 kva");
                        ug_phase3.add("1000 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_phase3);
                        sp_ug_capacity_dtr_pole.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_oh_voltage_level:
                    sp_oh_voltage_level_str = sp_oh_voltage_level.getSelectedItem().toString();
                    if ("6.3 KV".equalsIgnoreCase(sp_oh_voltage_level_str)) {
                        List<String> oh_phase_I = new ArrayList<>();
                        oh_phase_I.add("Select");
                        oh_phase_I.add("I");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase_I);
                        sp_oh_phase_dtr_pole.setAdapter(adapter);
                    }
                    break;

                case R.id.sp_oh_phase_dtr_pole:
                    sp_oh_phase_dtr_pole_str = sp_oh_phase_dtr_pole.getSelectedItem().toString();

                    List<PMIDTRMaster_vo> dtrMasterData = arcGISDatabase.getDTRMasterData();
                    PMIDTRMaster_vo dtrData = new PMIDTRMaster_vo();
                    for (int j = 0; j < dtrMasterData.size(); j++) {
                        if (selected_dtr_code.equalsIgnoreCase(dtrMasterData.get(j).getDTR_DTR_STRUCTURE_CODE())) {
                            dtrData = dtrMasterData.get(j);
                        }
                    }

                    if ("I".equalsIgnoreCase(sp_oh_phase_dtr_pole_str)) {
                        String cap = dtrData.getDTR_CAPACITY() + " kva";
                        List<String> oh_phase1 = new ArrayList<>();
                        oh_phase1.add("5 kva");
                        oh_phase1.add("10 kva");
                        oh_phase1.add("15 kva");
                        oh_phase1.add("16 kva");
                        oh_phase1.add("25 kva");

                        if (dtrData.getDTR_PHASE().equals("1")) {
                            oh_phase1.remove(cap);
                            oh_phase1.add(0, cap);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase1);
                        sp_oh_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("II".equalsIgnoreCase(sp_oh_phase_dtr_pole_str)) {
                        List<String> oh_phase2 = new ArrayList<>();
                        oh_phase2.add("10 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase2);
                        sp_oh_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("III".equalsIgnoreCase(sp_oh_phase_dtr_pole_str)) {
                        String cap = dtrData.getDTR_CAPACITY() + " kva";
                        List<String> oh_phase3 = new ArrayList<>();
                        oh_phase3.add("16 kva");
                        oh_phase3.add("25 kva");
                        oh_phase3.add("40 kva");
                        oh_phase3.add("50 kva");
                        oh_phase3.add("63 kva");
                        oh_phase3.add("75 kva");
                        oh_phase3.add("100 kva");
                        oh_phase3.add("160 kva");
                        oh_phase3.add("200 kva");
                        oh_phase3.add("250 kva");
                        oh_phase3.add("315 kva");
                        oh_phase3.add("500 kva");
                        oh_phase3.add("1000 kva");
                        if (dtrData.getDTR_PHASE().equals("3")) {
                            oh_phase3.remove(cap);
                            oh_phase3.add(0, cap);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase3);
                        sp_oh_capacity_dtr_pole.setAdapter(adapter);
                    }
                    break;

                //ab cable spinner start
                case R.id.sp_abc_poletype:
                    sp_abc_pole_type_str = sp_abc_poletype.getSelectedItem().toString();
                    sp_abc_poleheight.setSelection(0);
                    if ("PSCC".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> psccList_abc = new ArrayList<>();
                        psccList_abc.add("Select");
                        psccList_abc.add("8.0");
                        psccList_abc.add("9.1");
                        psccList_abc.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, psccList_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> spunList_abc = new ArrayList<>();
                        spunList_abc.add("Select");
                        spunList_abc.add("9.5");
                        spunList_abc.add("11");
                        spunList_abc.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, spunList_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("RAIL".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> rail_abc = new ArrayList<>();
                        rail_abc.add("Select");
                        rail_abc.add("9.5");
                        rail_abc.add("11");
                        rail_abc.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rail_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("RS JOIST".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> rs_joist_abc = new ArrayList<>();
                        rs_joist_abc.add("Select");
                        rs_joist_abc.add("9.1");
                        rs_joist_abc.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rs_joist_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("TOWER".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> tower_abc = new ArrayList<>();
                        tower_abc.add("Select");
                        tower_abc.add("M+3");
                        tower_abc.add("M+6");
                        tower_abc.add("M+9");
                        tower_abc.add("M+12");
                        tower_abc.add("R+6");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, tower_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("Tubular Pole".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> truble_pole_abc = new ArrayList<>();
                        truble_pole_abc.add("Select");
                        truble_pole_abc.add("9.1");
                        truble_pole_abc.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, truble_pole_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    }
                case R.id.sp_abc_tapingpoint:
                    abc_tapingpoint = sp_abc_tapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(abc_tapingpoint)) {
                        lyt_abc_tapping.setVisibility(View.VISIBLE);
                    } else {
                        lyt_abc_tapping.setVisibility(View.GONE);
                        abc_nooftappings = "";
                        sp_abc_nooftappings.setSelection(0);
                        edt_abc_tappinglocation.setText("");
                    }

                    break;
                case R.id.sp_abc_dergisendpole:
                    if ("No".equalsIgnoreCase(sp_abc_dergisendpole.getSelectedItem().toString())) {
                        abc_isendpole = "N";
                    } else {
                        abc_isendpole = "Y";
                    }
                    break;
                case R.id.sp_abc_noofcircuit:
                    abc_noofcircuitStr = sp_abc_noofcircuit.getSelectedItem().toString();

                    break;
                case R.id.sp_abc_straight_rmu:
                    abc_straight_rmuStr = sp_abc_straight_rmu.getSelectedItem().toString();
                    sp_abc_rmu.setSelection(0);
                    if ("RMU".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.VISIBLE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Structure".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_derpolestructure.setVisibility(View.VISIBLE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Pole".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_pole.setVisibility(View.VISIBLE);
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("HT Service".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.VISIBLE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        List<String> abc_ht_servicenoList = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
                        abc_ht_servicenoList.add("Select");
                        Collections.reverse(abc_ht_servicenoList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_ht_servicenoList);
                        auto_abc_ht_service_number.setAdapter(adapterMake_ug);
                        auto_abc_ht_service_number.setThreshold(1);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Line Capacitor".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.VISIBLE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Auto Reclosure".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.VISIBLE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("DTR".equalsIgnoreCase(abc_straight_rmuStr)) {
                        clearSelecteAfterChangeTextFiledsDTR();
                        lay_abc_dtr_pole_main.setVisibility(View.VISIBLE);
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        ArrayList<String> dtr_abc_sectionList = arcGISDatabase.getAll_DtrStructureSubStationMaster();
                        dtr_abc_sectionList.add("Select");
                        Collections.reverse(dtr_abc_sectionList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                                android.R.layout.simple_spinner_dropdown_item, dtr_abc_sectionList);
                        sp_abc_section_name_dtr_pole.setAdapter(adapter);

                    } else if ("AB-Pole".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.VISIBLE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else {
                        sp_abc_rmu.setSelection(0);
                        sp_abc_derpolestructure.setSelection(0);
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        rmu = "";
                        //nooftappings="";
                    }
                    break;
                case R.id.sp_abc_rmu:
                    sp_abc_rmuStr = sp_abc_rmu.getSelectedItem().toString();
                    break;
                case R.id.sp_abc_derpolestructure:
                    sp_abc_derpolestructureStr = sp_abc_derpolestructure.getSelectedItem().toString();
                    break;
                case R.id.sp_abc_polesupport:
                    sp_abc_polesupportStr = sp_abc_polesupport.getSelectedItem().toString();
                    break;

                case R.id.sp_abc_section_name_dtr_pole:
                    dtr_pole_abc_section_name = sp_abc_section_name_dtr_pole.getSelectedItem().toString();
                    if ("Select".equalsIgnoreCase(dtr_pole_abc_section_name)) {

                    } else {
                        List<String> dtr_abc_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_abc_section_name));
                        dtr_abc_structureList.add("Select");
                        Collections.reverse(dtr_abc_structureList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, dtr_abc_structureList);
                        auto_abc_dtr_structure_dtr_pole.setAdapter(adapterMake_ug);
                        auto_abc_dtr_structure_dtr_pole.setThreshold(1);
                    }
                    break;
                case R.id.sp_abc_phase_dtr_pole:
                    sp_abc_phase_dtr_pole_str = sp_abc_phase_dtr_pole.getSelectedItem().toString();
                    if ("I".equalsIgnoreCase(sp_abc_phase_dtr_pole_str)) {
                        List<String> abc_phase1 = new ArrayList<>();
                        abc_phase1.add("Select");
                        abc_phase1.add("5 kva");
                        abc_phase1.add("10 kva");
                        abc_phase1.add("15 kva");
                        abc_phase1.add("16 kva");
                        abc_phase1.add("25 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_phase1);
                        sp_abc_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("II".equalsIgnoreCase(sp_abc_phase_dtr_pole_str)) {
                        List<String> abc_phase2 = new ArrayList<>();
                        abc_phase2.add("Select");
                        abc_phase2.add("10 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_phase2);
                        sp_abc_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("III".equalsIgnoreCase(sp_abc_phase_dtr_pole_str)) {
                        List<String> abc_phase3 = new ArrayList<>();
                        abc_phase3.add("Select");
                        abc_phase3.add("16 kva");
                        abc_phase3.add("25 kva");
                        abc_phase3.add("40 kva");
                        abc_phase3.add("50 kva");
                        abc_phase3.add("63 kva");
                        abc_phase3.add("75 kva");
                        abc_phase3.add("100 kva");
                        abc_phase3.add("160 kva");
                        abc_phase3.add("200 kva");
                        abc_phase3.add("250 kva");
                        abc_phase3.add("315 kva");
                        abc_phase3.add("500 kva");
                        abc_phase3.add("1000 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_phase3);
                        sp_abc_capacity_dtr_pole.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_abc_nooftappings:
                    abc_nooftappings = sp_abc_nooftappings.getSelectedItem().toString();

                    break;
                case R.id.sp_abc_cablesize:
                    abc_cablesizeStr = sp_abc_cablesize.getSelectedItem().toString();

                    break;
                case R.id.sp_abc_poleheight:
                    abc_poleheightStr = sp_abc_poleheight.getSelectedItem().toString();

                    break;
                case R.id.sp_Survey_type:
                    Survey_type_string = sp_Survey_type.getSelectedItem().toString();

                    break;

                case R.id.sp_oh_dtr_survey_type:
                    if (sp_oh_dtr_survey_type.getSelectedItem().toString().equalsIgnoreCase("JHC")) {
                        List<String> list_pst = new ArrayList<>();
                        list_pst.add("HC Phase 1");
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_oh_lt_survey_type.setAdapter(adapter_pst);
                        sp_oh_lt_survey_type.setSelection(0, true);
                    } else {
                        List<String> list_pst = new ArrayList<>();
                        list_pst.add("Select");
                        list_pst.add("EXISTING");
                        list_pst.add("HC Phase 1");
                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_pst);
                        sp_oh_lt_survey_type.setAdapter(adapter_pst);
                        sp_oh_lt_survey_type.setSelection(0, true);
                    }
                    break;


                case R.id.sp_pole_concrete:
                    pole_concrete = sp_pole_concrete.getSelectedItem().toString();

                    break;
                default:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAllDataFileds() {
        straight_rmu = "";
        sp_straight_rmu.setSelection(0);
        //Over Head
        sp_conductorsize.setSelection(0);
        sp_poletype.setSelection(0);
        sp_polesupport.setSelection(0);
        sp_poleconfiguration.setSelection(0);
        sp_poleheight.setSelection(0);
        sp_oh_section_name_dtr_pole.setSelection(0);
        sp_oh_ab_switch_dtr_pole.setSelection(0);
        sp_oh_phase_dtr_pole.setSelection(0);
        sp_oh_phase_dtr_pole_str = "";
        sp_oh_dtr_structure_type_dtr_pole.setSelection(0);
        sp_oh_line_capacitor.setSelection(0);
        sp_oh_capacity_dtr_pole.setSelection(0);
        auto_oh_dtr_structure_dtr_pole.setText("");
        auto_oh_dtr_structure_dtr_pole.setText("");
        ed_oh_location_name_dtr_pole.setText("");
        ed_oh_serial_number_dtr_pole.setText("");
        sp_oh_no_of_lt_circuits.setSelection(0);
        edt_polepaintingcode.setText("");
        edt_tappinglocation.setText("");
        edt_polepaintingcode.setText("");
        edt_remarks_overhead.setText("");
        edt_ugtappinglocation.setText("");
        edt_remarks_underground.setText("");
        edt_ugpolepaintingcode.setText("");

        //Under Ground
        sp_ug_section_name_dtr_pole.setSelection(0);
        sp_ug_ab_switch_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole_str = "";
        sp_ug_dtr_structure_type_dtr_pole.setSelection(0);
        sp_ug_line_capacitor.setSelection(0);
        sp_ug_capacity_dtr_pole.setSelection(0);
        auto_ug_dtr_structure_dtr_pole.setText("");
        auto_ug_dtr_structure_dtr_pole.setText("");
        sp_rmu.setSelection(0);
        sp__underpolestructure.setSelection(0);
        ed_ug_location_name_dtr_pole.setText("");
        sp_noofcircuit_underg.setSelection(0);
        ed_ug_serial_number_dtr_pole.setText("");
        sp_ug_no_of_lt_circuits.setSelection(0);

        sp_ugpoleheight.setSelection(0);
        sp_oh_no_of_lt_circuits.setSelection(0);
        sp_oh_is_lt_ckts_available.setSelection(0);
        sp_oh_dtr_location_type.setSelection(0);
        sp_oh_type_of_load.setSelection(0);
        sp_noofcircuit.setSelection(0);
        sp_diffvoltage.setSelection(0);
        sp_ismulticircute.setSelection(0);
        // sp_Survey_type.setSelection(0);
    }

    private void clearTextAfterSelectedOtherFiled_OH() {
        sp_oh_section_name_dtr_pole.setSelection(0);
        sp_oh_ab_switch_dtr_pole.setSelection(0);
        sp_oh_phase_dtr_pole.setSelection(0);
        sp_oh_phase_dtr_pole_str = "";
        sp_oh_dtr_structure_type_dtr_pole.setSelection(0);
        sp_oh_line_capacitor.setSelection(0);
        sp_oh_capacity_dtr_pole.setSelection(0);
        auto_oh_dtr_structure_dtr_pole.setText("");
        auto_oh_dtr_structure_dtr_pole.setText("");
        auto_oh_make_dtr_pole.setText("");
        ed_oh_location_name_dtr_pole.setText("");
        ed_oh_serial_number_dtr_pole.setText("");
        sp_oh_no_of_lt_circuits.setSelection(0);
        sp_oh_is_lt_ckts_available.setSelection(0);
        sp_oh_dtr_location_type.setSelection(0);
        sp_oh_type_of_load.setSelection(0);
        auto_oh_ht_service_number.setText("");
        sp_oh_auto_reclosure.setSelection(0);
        //  sp_Survey_type.setSelection(0);
    }

    private void clearSelecteAfterChangeTextFiledsDTR() {
        sp_ug_section_name_dtr_pole.setSelection(0);
        sp_ug_ab_switch_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole_str = "";
        sp_ug_dtr_structure_type_dtr_pole.setSelection(0);
        sp_ug_line_capacitor.setSelection(0);
        sp_ug_capacity_dtr_pole.setSelection(0);
        auto_ug_dtr_structure_dtr_pole.setText("");
        auto_ug_dtr_structure_dtr_pole.setText("");
        sp_rmu.setSelection(0);
        sp__underpolestructure.setSelection(0);
        auto_ug_make_dtr_pole.setText("");
        ed_ug_location_name_dtr_pole.setText("");
        ed_ug_serial_number_dtr_pole.setText("");
        sp_ug_no_of_lt_circuits.setSelection(0);
        sp_ug_no_of_ab_switches.setSelection(0);
        sp_ug_is_lt_ckts_available.setSelection(0);
        sp_ug_dtr_location_type.setSelection(0);
        sp_ug_type_of_load.setSelection(0);
        auto_ug_ht_service_number.setText("");
        sp_ug_auto_reclosure.setSelection(0);


        //AB CABLE
        sp_abc_section_name_dtr_pole.setSelection(0);
        sp_abc_ab_switch_dtr_pole.setSelection(0);
        sp_abc_phase_dtr_pole.setSelection(0);
        sp_abc_phase_dtr_pole_str = "";
        sp_abc_dtr_structure_type_dtr_pole.setSelection(0);
        sp_abc_line_capacitor.setSelection(0);
        sp_abc_capacity_dtr_pole.setSelection(0);
        auto_abc_dtr_structure_dtr_pole.setText("");
        auto_abc_dtr_structure_dtr_pole.setText("");
        sp_abc_rmu.setSelection(0);
        sp_abc_derpolestructure.setSelection(0);
        auto_abc_make_dtr_pole.setText("");
        ed_abc_location_name_dtr_pole.setText("");
        ed_abc_serial_number_dtr_pole.setText("");
        sp_abc_no_of_lt_circuits.setSelection(0);
        sp_abc_no_of_ab_switches.setSelection(0);
        sp_abc_is_lt_ckts_available.setSelection(0);
        sp_abc_dtr_location_type.setSelection(0);
        sp_abc_type_of_load.setSelection(0);
        auto_abc_ht_service_number.setText("");
        sp_abc_auto_reclosure.setSelection(0);
        //   sp_Survey_type.setSelection(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void overHeadData() {
        try {

            if (LoginActivity.isJHC && sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Pole Survey Type", Toast.LENGTH_LONG).show();
            } else if (LoginActivity.isJHC && sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Line Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(Survey_type_string)) {
                Toast.makeText(c, "Select Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleType)) {
                Toast.makeText(c, "Select Pole Type", Toast.LENGTH_LONG).show();
            }

            /*else if ("TOWER".equalsIgnoreCase(poleType) && lastPoleDistance >200) {
                Toast.makeText(c, "Abnormal Distance\nDistance below 200 meters\nis allowed for TOWER", Toast.LENGTH_LONG).show();
            }

            else if (!"TOWER".equalsIgnoreCase(poleType) && lastPoleDistance >150) {
                Toast.makeText(c, "Abnormal Distance\nDistance below 150 meters\nis allowed", Toast.LENGTH_LONG).show();
            }*/

            else if ("Select".equalsIgnoreCase(poleHeight)) {
                Toast.makeText(c, "Select Pole Height", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(taping_Point)) {
                Toast.makeText(c, "Select Taping_Point ", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(taping_Point) && "Select".equalsIgnoreCase(sp_nooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(taping_Point) && edt_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(conductorsize)) {
                Toast.makeText(c, "Select ConductorSize", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleconfiguration)) {
                Toast.makeText(c, "Select PoleConfiguration", Toast.LENGTH_LONG).show();
            }

           /* else if (!"SS BAY".equalsIgnoreCase(poleconfiguration) && isFirstPole) {
                Toast.makeText(c, "First Pole should be SS Bay", Toast.LENGTH_LONG).show();
            }

            else if ("SS BAY".equalsIgnoreCase(poleconfiguration) && ! isFirstPole) {
                Toast.makeText(c, "Only First Pole can be SS Bay", Toast.LENGTH_LONG).show();
            }*/

            else if ("Select".equalsIgnoreCase(ismulticircute)) {
                Toast.makeText(c, "Select is Multi Circuit", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(noofcircuit)) {
                Toast.makeText(c, "Select No.Of Circuits", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Circuit Type", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Multi Voltage".equalsIgnoreCase(circutetype) && "Select".equalsIgnoreCase(sp_diffvoltage.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Diff Voltage", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(sp_noofcircuit.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Circuits", Toast.LENGTH_LONG).show();
            }

            //NEW MULTI CKT


            else if (validate33KVVoltageCircuit()) {

            } else if (validate11KVVoltageCircuit()) {

            } else if (validateLTVoltageCircuit()) {

            } else if (validateNoOfCircuits()) {
                Toast.makeText(c, "No of circuits and selected circuits are not matching", Toast.LENGTH_SHORT).show();
            }

        /*    else if (validate33KVSamePole()){
                Toast.makeText(c,"This Pole is already mapped with another multi circuit",Toast.LENGTH_SHORT).show();
            }*/

            //CROSSING

            else if (ch_crossing.isChecked() && !ch_oh_6_3_crossing.isChecked() && !ch_oh_11kv_crossing.isChecked() && !ch_oh_road_crossing.isChecked() &&
                    !ch_oh_33kv_crossing.isChecked() && !ch_oh_lt_crossing.isChecked() && !ch_oh_tc_crossing.isChecked() &&
                    !ch_oh_tm_crossing.isChecked()) {
                //"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
                Toast.makeText(c, "Select Any One Of Crossing", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_6_3_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_6_3_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 6.3KV Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_6_3_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_6_3_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 6.3KV Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_11kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_11kv_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 11kv Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_11kv_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_11kv_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 11kv Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_33kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_33kv_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 33kv Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_33kv_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_33kv_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 33kv Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_lt_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_lt_crossing.getText().toString())) {
                Toast.makeText(c, "Enter DTR Code of LT Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_lt_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_lt_crossing.getText().toString())) {
                Toast.makeText(c, "Enter DTR Code of LT Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_tc_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tc_crossing.getText().toString())) {
                Toast.makeText(c, "Enter No. of Tele Communication Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_tc_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_tc_crossing.getText().toString())) {
                Toast.makeText(c, "Enter No. of Tele Communication Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_tm_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tm_crossing.getText().toString())) {
                Toast.makeText(c, "Enter No. of Transmission Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_tm_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_tm_crossing.getText().toString())) {
                Toast.makeText(c, "Enter No. of Transmission Crossings", Toast.LENGTH_SHORT).show();
            } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && edt_oh_other_cables.getText().toString().isEmpty()) {
                Toast.makeText(c, "No. of Other Cables", Toast.LENGTH_SHORT).show();
            } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && Integer.parseInt(edt_oh_other_cables.getText().toString()) == 0) {
                Toast.makeText(c, "No. of Other Cables should be greater than 0", Toast.LENGTH_SHORT).show();
            } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && Integer.parseInt(edt_oh_other_cables.getText().toString()) > 20) {
                Toast.makeText(c, "No. of Other Cables should not be greater than 20", Toast.LENGTH_SHORT).show();
            } else if (!poleconfiguration.equalsIgnoreCase("Line-Pole") && !image_1_clicked && !image_2_clicked && !image_3_clicked) {
                Toast.makeText(c, "Upload at least 1 photo", Toast.LENGTH_SHORT).show();
            }
            else if (edt_polepaintingcode.getText().toString().isEmpty()){
                Toast.makeText(c, "Please Enter Pole Painting Code", Toast.LENGTH_SHORT).show();
            }


            //JHC
            else if (LoginActivity.isJHC && sp_oh_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Yes") && sp_oh_lt_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select LT Survey Type", Toast.LENGTH_SHORT).show();
            } else if (LoginActivity.isJHC && sp_oh_is_dtr_avalabile.getSelectedItem().toString().equalsIgnoreCase("Yes") && sp_oh_dtr_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select DTR Survey Type", Toast.LENGTH_SHORT).show();
            }else if ("Select".equalsIgnoreCase(sp_pole_concrete.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Concrete", Toast.LENGTH_LONG).show();
            } else {
                if ("DTR Pole".equalsIgnoreCase(poleconfiguration)) {
                    if (sp_oh_section_name_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Section Name ", Toast.LENGTH_LONG).show();
                    } else if (auto_oh_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    } else if (!arcGISDatabase.checkValueDTRCodeMethod(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                    } else if (arcGISDatabase.checkValueDTRCodeLoacalSave(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                    } else if (sp_oh_is_dtr_avalabile.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Is DTR Available", Toast.LENGTH_LONG).show();
                    }
                    else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && auto_oh_make_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Make ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && !makeArrayList.contains(auto_oh_make_dtr_pole.getText().toString())) {
                        Toast.makeText(c, "Enter Make Not Match ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && ed_oh_serial_number_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Serial Number ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && ed_oh_location_name_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Is OH LT ckts available  ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && "Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr) && sp_oh_no_of_lt_circuits.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select No Of LT Circuit ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_ab_switch_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select AB Switch ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                    try {
                        if (auto_oh_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkValueMethod(auto_oh_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {
                            overHeadDataValidation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                    }
                } else if ("Line Capacitor".equalsIgnoreCase(poleconfiguration)) {
                    if ("Select".equalsIgnoreCase(sp_oh_line_capacitor.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Line Capacitor ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else if ("Auto Reclosure".equalsIgnoreCase(poleconfiguration)) {
                    if ("Select".equalsIgnoreCase(sp_oh_auto_reclosure.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Auto Reclosure ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else if ("AB-Pole".equalsIgnoreCase(poleconfiguration)) {
                    if ("Select".equalsIgnoreCase(sp_oh_no_of_ab_switches.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select No Of AB Switches ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else {
                    overHeadDataValidation();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validate33KVSamePole() {
        if (sp_ismulticircute.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_33kv_vlckts.isChecked()) {
                if (!"Select".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())) {
                    if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("1")) {
                        String enteredFeederID_33_1 = sp_oh_33_feeder_1.getSelectedItem().toString();
                        String enteredPole_33_1 = sp_oh_33_feeder_1_pole.getSelectedItem().toString();
                        PMI11kvArcGISDatabase database = new PMI11kvArcGISDatabase(c);
                        ArrayList<PMISurveyVo> list = database.getSurveyData("");
                        //  System.out.println("SURVEYED SIZE : "+list.size());

                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getMC_33KV_FEEDER_ID_1().equals(enteredFeederID_33_1) && list.get(i).getMC_33KV_POLE_1().equals(enteredPole_33_1)) {
                                    Toast.makeText(c, "This Pole is already mapped with another multi circuit", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                                if (list.get(i).getMC_33KV_FEEDER_ID_2().equals(enteredFeederID_33_1) && list.get(i).getMC_33KV_POLE_2().equals(enteredPole_33_1)) {
                                    Toast.makeText(c, "This Pole is already mapped with another multi circuit", Toast.LENGTH_SHORT).show();
                                    return true;
                                }

                                if (list.get(i).getMC_33KV_FEEDER_ID_3().equals(enteredFeederID_33_1) && list.get(i).getMC_33KV_POLE_3().equals(enteredPole_33_1)) {
                                    Toast.makeText(c, "This Pole is already mapped with another multi circuit", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        }

                        /*int result = database.getSamePole(enteredFeederID_33_1.split("~")[0],enteredPole_33_1);
                        System.out.println("SAME POLE RESULT : "+result);*/


                    }
                }
            }
        }

        return false;

    }

    private boolean validateNoOfCircuits() {

        if (sp_ismulticircute.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
            int selectedCkts = Integer.parseInt(sp_noofcircuit.getSelectedItem().toString()) - 1;


            if (!"Select".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString()) && ch_oh_33kv_vlckts.isChecked()) {
                noof_33kvCkts = Integer.parseInt(sp_oh_no_of_33_feeders.getSelectedItem().toString());
            }

            if (!"Select".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString()) && ch_oh_11kv_vlckts.isChecked()) {
                noof_11kvCkts = Integer.parseInt(sp_oh_no_of_11_feeders.getSelectedItem().toString());
            }

            if (!"Select".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString()) && ch_oh_lt_vlckts.isChecked()) {
                noof_ltCkts = Integer.parseInt(sp_oh_no_of_lt_feeders.getSelectedItem().toString());
            }

            int totalCKTS = noof_33kvCkts + noof_11kvCkts + noof_ltCkts;

            if (selectedCkts != totalCKTS) {
                return true;
            }


        }


        return false;
    }

    private boolean validate33KVVoltageCircuit() {

        if (sp_ismulticircute.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_33kv_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_1_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 1 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_1.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 1 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                    if (!(sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_33_pole_1.getText().toString().isEmpty() || ed_oh_33_pole_1.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                }

                if ("2".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 2 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_2_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 2 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_2.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 2 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_33_pole_2.getText().toString().isEmpty() || ed_oh_33_pole_2.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("3".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 3 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_3_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 3 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_3.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 3 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_33_pole_3.getText().toString().isEmpty() || ed_oh_33_pole_3.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

            }
        }


        return false;
    }

    private boolean validate11KVVoltageCircuit() {

        if (sp_ismulticircute.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_11kv_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_1_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 1 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_1.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 1 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_11_pole_1.getText().toString().isEmpty() || ed_oh_11_pole_1.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("2".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 2 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_2_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 2 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_2.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 2 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_11_pole_2.getText().toString().isEmpty() || ed_oh_11_pole_2.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("3".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 3 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_3_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 3 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_3.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 3 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_11_pole_3.getText().toString().isEmpty() || ed_oh_11_pole_3.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                    }


                }

            }
        }


        return false;
    }

    private boolean validateLTVoltageCircuit() {

        if (sp_ismulticircute.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_lt_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                ) {

                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                        if (ed_oh_lt_feeder_1_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed LT CKT 1", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_lt_feeder_1.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 1 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                    if (!(sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed"))) {
                        try {
                            if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_lt_pole_1.getText().toString().isEmpty() || ed_oh_lt_pole_1.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("2".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 2 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                        if (ed_oh_lt_feeder_2_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed LT CKT 2", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_lt_feeder_2.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 2 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed"))) {
                        try {
                            if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_lt_pole_2.getText().toString().isEmpty() || ed_oh_lt_pole_2.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("3".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 3 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                        if (ed_oh_lt_feeder_3_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed LT CKT 3", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_lt_feeder_3.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 3 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed"))) {
                        try {
                            if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_lt_pole_3.getText().toString().isEmpty() || ed_oh_lt_pole_3.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

            }
        }


        return false;
    }

    private void overHeadDataValidation() {

        PMISurveyVo surveyVo = new PMISurveyVo();
        if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {

            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();
            } else {
                if (x.equalsIgnoreCase("0.0") && y.equalsIgnoreCase("0.0")) {
                    Toast.makeText(c, "Gps Loading .....!!", Toast.LENGTH_LONG).show();
                } else {

                    List<String> woId = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT WO_ID FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    List<String> isFeederType = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));

                    //JHC PHASE 1
                    if (LoginActivity.isJHC) {
                        // surveyVo.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                        //  surveyVo.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());

                        if (sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                            surveyVo.setPOLE_SURVEY_TYPE("JHC");
                        }else {
                            surveyVo.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                        }

                        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                            surveyVo.setLINE_SURVEY_TYPE("JHC");
                        }else {
                            surveyVo.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
                        }



                        if (sp_oh_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                            if (sp_oh_lt_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                                surveyVo.setLT_SURVEY_TYPE("JHC");
                            }else {
                                surveyVo.setLT_SURVEY_TYPE(sp_oh_lt_survey_type.getSelectedItem().toString());
                            }

                        } else {
                            surveyVo.setLT_SURVEY_TYPE("");
                        }

                        if (sp_oh_is_dtr_avalabile.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                            if (sp_oh_dtr_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                                surveyVo.setDTR_SURVEY_TYPE("JHC");
                            }else {
                                surveyVo.setDTR_SURVEY_TYPE(sp_oh_dtr_survey_type.getSelectedItem().toString());
                            }

                        } else {
                            surveyVo.setDTR_SURVEY_TYPE("");
                        }

                    } else {
                        surveyVo.setPOLE_SURVEY_TYPE("");
                        surveyVo.setLINE_SURVEY_TYPE("");
                        surveyVo.setSurvaytype(Survey_type_string);
                        surveyVo.setLT_SURVEY_TYPE("");
                        surveyVo.setDTR_SURVEY_TYPE("");
                    }


                    mapData.put("POLE_TYPE", poleType);
                    surveyVo.setFeeder_id(feederCode);
                    surveyVo.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                    surveyVo.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                    surveyVo.setLineType(lineType);
                    if (!"Select".equalsIgnoreCase(noofcircuit)) {
                        surveyVo.setNoofCircuits(noofcircuit);
                    } else {
                        surveyVo.setNoofCircuits("");
                    }

                    if (!"Select".equalsIgnoreCase(poleType)) {
                        surveyVo.setPole_type(poleType);
                    } else {
                        surveyVo.setPole_type("");
                    }
                    if (!"Select".equalsIgnoreCase(poleHeight)) {
                        surveyVo.setPole_height(poleHeight);
                    } else {
                        surveyVo.setPole_height("");
                    }
                    //surveyVo.setNoof_circuits(noofCircuits);
                    surveyVo.setPole_support(poleSupport);

                    surveyVo.setTapping_point(taping_Point);
                    if (!"Select".equalsIgnoreCase(conductorsize)) {
                        surveyVo.setConductor_size(conductorsize);
                    } else {
                        surveyVo.setConductor_size("");
                    }
                    surveyVo.setPinpoint(pinpoint);
                    if ("Cutpoint".equalsIgnoreCase(pinpoint)) {
                        surveyVo.setPoleStructure(polestructure);
                    } else {
                        surveyVo.setPoleStructure("");
                    }


                    if (!"Select".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setPole_configuration(poleconfiguration);
                    } else {
                        surveyVo.setPole_configuration("");
                    }
                    surveyVo.setPole_paintingcode(edt_polepaintingcode.getText().toString());
                    surveyVo.setLat(x);
                    surveyVo.setLan(y);
                    surveyVo.setPastSurveyFlag("N");

                    //MULTI CKT START

                    if ("Yes".equalsIgnoreCase(ismulticircute)) {

                        surveyVo.setIsMultiCircuit(ismulticircute);
                        surveyVo.setNO_OF33KV_CKTS("" + noof_33kvCkts);
                        surveyVo.setNO_OF11KV_CKTS("" + noof_11kvCkts);
                        surveyVo.setNO_OF_LT_CKTS("" + noof_ltCkts);

                        if (ch_oh_33kv_vlckts.isChecked()) {
                            if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("1")) {
                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                    surveyVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());
                                }

                                surveyVo.setMC_33KV_FEEDER_ID_2("N");
                                surveyVo.setMC_33KV_POLE_2("N");
                                surveyVo.setMC_33KV_FEEDER_ID_3("N");
                                surveyVo.setMC_33KV_POLE_3("N");

                            }

                            if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("2")) {
                                //FEEDER 1
                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                    surveyVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_33KV_FEEDER_ID_2(sp_oh_33_feeder_2.getSelectedItem().toString());
                                }


                                if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_33KV_POLE_2(ed_oh_33_pole_2.getText().toString());
                                    surveyVo.setMC_33KV_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyVo.setMC_33KV_POLE_2(sp_oh_33_feeder_2_pole.getSelectedItem().toString());
                                }

                                surveyVo.setMC_33KV_FEEDER_ID_3("N");
                                surveyVo.setMC_33KV_POLE_3("N");

                            }


                            if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("3")) {

                                //FEEDER 1
                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                    surveyVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_33KV_FEEDER_ID_2(sp_oh_33_feeder_2.getSelectedItem().toString());
                                }


                                if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_33KV_POLE_2(ed_oh_33_pole_2.getText().toString());
                                    surveyVo.setMC_33KV_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyVo.setMC_33KV_POLE_2(sp_oh_33_feeder_2_pole.getSelectedItem().toString());
                                }

                                //FEEDER 3
                                if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_3(ed_oh_33_feeder_3.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_FEEDER_ID_3(ed_oh_33_feeder_3_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_33KV_FEEDER_ID_3(sp_oh_33_feeder_3.getSelectedItem().toString());
                                }

                                if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_33KV_POLE_3("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_33KV_POLE_3(ed_oh_33_pole_3.getText().toString());
                                    surveyVo.setMC_33KV_NOT_IN_LIST_3("Y");
                                } else {
                                    surveyVo.setMC_33KV_POLE_3(sp_oh_33_feeder_3_pole.getSelectedItem().toString());
                                }

                            }
                        } else {
                            surveyVo.setMC_33KV_FEEDER_ID_1("N");
                            surveyVo.setMC_33KV_POLE_1("N");
                            surveyVo.setMC_33KV_FEEDER_ID_2("N");
                            surveyVo.setMC_33KV_POLE_2("N");
                            surveyVo.setMC_33KV_FEEDER_ID_3("N");
                            surveyVo.setMC_33KV_POLE_3("N");
                        }

                        //11KV FEEDER
                        if (ch_oh_11kv_vlckts.isChecked()) {
                            if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("1")) {
                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                }


                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                    surveyVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                }

                                surveyVo.setMC_11KV_FEEDER_ID_2("N");
                                surveyVo.setMC_11KV_POLE_2("N");
                                surveyVo.setMC_11KV_FEEDER_ID_3("N");
                                surveyVo.setMC_11KV_POLE_3("N");

                            }

                            if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("2")) {
                                //FEEDER 1
                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                    surveyVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_11KV_FEEDER_ID_2(sp_oh_11_feeder_2.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_11KV_POLE_2(ed_oh_11_pole_2.getText().toString());
                                    surveyVo.setMC_11KV_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyVo.setMC_11KV_POLE_2(sp_oh_11_feeder_2_pole.getSelectedItem().toString());
                                }

                                surveyVo.setMC_11KV_FEEDER_ID_3("N");
                                surveyVo.setMC_11KV_POLE_3("N");

                            }

                            if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("3")) {

                                //FEEDER 1
                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                    surveyVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_11KV_FEEDER_ID_2(sp_oh_11_feeder_2.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_11KV_POLE_2(ed_oh_11_pole_2.getText().toString());
                                    surveyVo.setMC_11KV_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyVo.setMC_11KV_POLE_2(sp_oh_11_feeder_2_pole.getSelectedItem().toString());
                                }

                                //FEEDER 3
                                if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_3(ed_oh_11_feeder_3.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_FEEDER_ID_3(ed_oh_11_feeder_3_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_11KV_FEEDER_ID_3(sp_oh_11_feeder_3.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyVo.setMC_11KV_POLE_3("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_11KV_POLE_3(ed_oh_11_pole_3.getText().toString());
                                    surveyVo.setMC_11KV_NOT_IN_LIST_3("Y");
                                } else {
                                    surveyVo.setMC_11KV_POLE_3(sp_oh_11_feeder_3_pole.getSelectedItem().toString());
                                }

                            }
                        } else {
                            surveyVo.setMC_11KV_FEEDER_ID_1("N");
                            surveyVo.setMC_11KV_POLE_1("N");
                            surveyVo.setMC_11KV_FEEDER_ID_2("N");
                            surveyVo.setMC_11KV_POLE_2("N");
                            surveyVo.setMC_11KV_FEEDER_ID_3("N");
                            surveyVo.setMC_11KV_POLE_3("N");

                        }

                        //LT FEEDER
                        if (ch_oh_lt_vlckts.isChecked()) {
                            if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("1")) {
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                    surveyVo.setMC_LT_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                }

                                surveyVo.setMC_LT_FEEDER_ID_2("N");
                                surveyVo.setMC_LT_POLE_2("N");
                                surveyVo.setMC_LT_FEEDER_ID_3("N");
                                surveyVo.setMC_LT_POLE_3("N");

                            }

                            if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("2")) {
                                //FEEDER 1
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                }
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                    surveyVo.setMC_LT_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_LT_FEEDER_ID_2(sp_oh_lt_feeder_2.getSelectedItem().toString());
                                }

                                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_LT_POLE_2(ed_oh_lt_pole_2.getText().toString());
                                    surveyVo.setMC_LT_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyVo.setMC_LT_POLE_2(sp_oh_lt_feeder_2_pole.getSelectedItem().toString());
                                }

                                surveyVo.setMC_LT_FEEDER_ID_3("N");
                                surveyVo.setMC_LT_POLE_3("N");

                            }

                            if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("3")) {

                                //FEEDER 1
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                }
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                    surveyVo.setMC_LT_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_LT_FEEDER_ID_2(sp_oh_lt_feeder_2.getSelectedItem().toString());
                                }


                                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_LT_POLE_2(ed_oh_lt_pole_2.getText().toString());
                                    surveyVo.setMC_LT_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyVo.setMC_LT_POLE_2(sp_oh_lt_feeder_2_pole.getSelectedItem().toString());
                                }

                                //FEEDER 3
                                if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                    surveyVo.setMC_LT_FEEDER_ID_3(ed_oh_lt_feeder_3.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_FEEDER_ID_3(ed_oh_lt_feeder_3_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyVo.setMC_LT_FEEDER_ID_3(sp_oh_lt_feeder_3.getSelectedItem().toString());
                                }

                                if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyVo.setMC_LT_POLE_3("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyVo.setMC_LT_POLE_3(ed_oh_lt_pole_3.getText().toString());
                                    surveyVo.setMC_LT_NOT_IN_LIST_3("Y");
                                } else {
                                    surveyVo.setMC_LT_POLE_3(sp_oh_lt_feeder_3_pole.getSelectedItem().toString());
                                }

                            }
                        } else {
                            surveyVo.setMC_LT_FEEDER_ID_1("N");
                            surveyVo.setMC_LT_POLE_1("N");
                            surveyVo.setMC_LT_FEEDER_ID_2("N");
                            surveyVo.setMC_LT_POLE_2("N");
                            surveyVo.setMC_LT_FEEDER_ID_3("N");
                            surveyVo.setMC_LT_POLE_3("N");
                        }


                    } else if ("No".equalsIgnoreCase(ismulticircute)) {
                        surveyVo.setIsMultiCircuit(ismulticircute);
                        surveyVo.setNoofCircuits("1");
                        surveyVo.setCircuitType("");

                        surveyVo.setNO_OF33KV_CKTS("");
                        surveyVo.setNO_OF11KV_CKTS("");
                        surveyVo.setNO_OF_LT_CKTS("");

                        surveyVo.setMC_33KV_FEEDER_ID_1("N");
                        surveyVo.setMC_33KV_POLE_1("N");
                        surveyVo.setMC_33KV_FEEDER_ID_2("N");
                        surveyVo.setMC_33KV_POLE_2("N");
                        surveyVo.setMC_33KV_FEEDER_ID_3("N");
                        surveyVo.setMC_33KV_POLE_3("N");

                        surveyVo.setMC_11KV_FEEDER_ID_1("N");
                        surveyVo.setMC_11KV_POLE_1("N");
                        surveyVo.setMC_11KV_FEEDER_ID_2("N");
                        surveyVo.setMC_11KV_POLE_2("N");
                        surveyVo.setMC_11KV_FEEDER_ID_3("N");
                        surveyVo.setMC_11KV_POLE_3("N");

                        surveyVo.setMC_LT_FEEDER_ID_1("N");
                        surveyVo.setMC_LT_POLE_1("N");
                        surveyVo.setMC_LT_FEEDER_ID_2("N");
                        surveyVo.setMC_LT_POLE_2("N");
                        surveyVo.setMC_LT_FEEDER_ID_3("N");
                        surveyVo.setMC_LT_POLE_3("N");
                    }


                    if (!"Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                        surveyVo.setCircuitType(circutetype);
                    } else {
                        surveyVo.setCircuitType("");
                    }

                    //MULTI CKT END


                    //CROSSING START
                    /*if (ch_crossing.isChecked()){
                        surveyVo.setCROSSING_TYPE(sp_crossing.getSelectedItem().toString());
                    }else {
                        surveyVo.setCROSSING_TYPE("");
                    }*/

                    if (ch_crossing.isChecked()) {
                        surveyVo.setCROSSING_TYPE(ch_oh_road_crossing.isChecked() ? sp_crossing.getSelectedItem().toString() : "");
                        surveyVo.setCROSS_6_3KV_ID(ch_oh_6_3_crossing.isChecked() ? edt_oh_6_3_crossing.getText().toString().split("~")[0] : "");
                        surveyVo.setCROSS_11KV_ID(ch_oh_11kv_crossing.isChecked() ? edt_oh_11kv_crossing.getText().toString().split("~")[0] : "");
                        surveyVo.setCROSS_33KV_ID(ch_oh_33kv_crossing.isChecked() ? edt_oh_33kv_crossing.getText().toString().split("~")[0] : "");
                        surveyVo.setCROSS_LT_CKT(ch_oh_lt_crossing.isChecked() ? edt_oh_lt_crossing.getText().toString().split("~")[0] : "");
                        surveyVo.setCROSS_COMMUN(ch_oh_tc_crossing.isChecked() ? edt_oh_tc_crossing.getText().toString() : "");
                        surveyVo.setCROSS_EHT(ch_oh_tm_crossing.isChecked() ? edt_oh_tm_crossing.getText().toString() : "");
                        surveyVo.setCROSS_REMARKS(ed_crossing_remarks.getText().toString().isEmpty() ? "" : ed_crossing_remarks.getText().toString());
                    } else {
                        surveyVo.setCROSSING_TYPE("");
                        surveyVo.setCROSS_6_3KV_ID("");
                        surveyVo.setCROSS_11KV_ID("");
                        surveyVo.setCROSS_33KV_ID("");
                        surveyVo.setCROSS_LT_CKT("");
                        surveyVo.setCROSS_COMMUN("");
                        surveyVo.setCROSS_EHT("");
                        surveyVo.setCROSS_REMARKS("");
                    }

                    //CROSSING END


                    /*if (!"Select".equalsIgnoreCase(diffvoltage)) {
                        surveyVo.setDiffVoltage(diffvoltage);
                    } else {
                        surveyVo.setDiffVoltage("");
                    }*/

                    surveyVo.setDiffVoltage("");
                    surveyVo.setIsEndPole(endPole);
                    if (!"Select".equalsIgnoreCase(ssname)) {
                        surveyVo.setSs_name(ssname);
                    } else {
                        surveyVo.setSs_name("");
                    }
                    if ("DTR Pole".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setDTR_AVAILABLE(oh_is_dtr_avalabileStr);
                        if (oh_is_dtr_avalabileStr.equalsIgnoreCase("Yes")) {
                            surveyVo.setDtr_phase(sp_oh_phase_dtr_pole.getSelectedItem().toString());
                            surveyVo.setDtr_capacity(sp_oh_capacity_dtr_pole.getSelectedItem().toString());
                            surveyVo.setDtr_ab_switch(sp_oh_ab_switch_dtr_pole.getSelectedItem().toString());
                            surveyVo.setDtr_make(auto_oh_make_dtr_pole.getText().toString());
                            surveyVo.setDtr_structure(sp_oh_dtr_structure_type_dtr_pole.getSelectedItem().toString());
                            surveyVo.setDtr_scno(ed_oh_serial_number_dtr_pole.getText().toString());
                            surveyVo.setDtr_location_name(ed_oh_location_name_dtr_pole.getText().toString());
                            if ("Yes".equalsIgnoreCase(sp_oh_is_lt_ckts_available.getSelectedItem().toString())) {
                                surveyVo.setDtr_no_of_circuit(sp_oh_no_of_lt_circuits.getSelectedItem().toString());
                            } else {
                                surveyVo.setDtr_no_of_circuit("");
                            }
                            surveyVo.setDtr_location_type(sp_oh_dtr_location_type.getSelectedItem().toString());
                            surveyVo.setIs_ltckt_avalable(sp_oh_is_lt_ckts_available.getSelectedItem().toString());
                            surveyVo.setType_of_load(sp_oh_type_of_load.getSelectedItem().toString());
                            surveyVo.setHt_service_number("");
                            surveyVo.setPc_line_capacitor("");
                            surveyVo.setPc_auto_reclosure("");
                        }

                        try {
                            surveyVo.setDtr_code(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0]);
                        } catch (Exception e) {
                            surveyVo.setDtr_code("");
                            Toast.makeText(c, "DTR Structure code Should not be null", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        surveyVo.setDTR_AVAILABLE("");
                        surveyVo.setDtr_phase("");
                        surveyVo.setDtr_capacity("");
                        surveyVo.setDtr_ab_switch("");
                        surveyVo.setDtr_make("");
                        surveyVo.setDtr_structure("");
                        surveyVo.setDtr_scno("");
                        surveyVo.setDtr_code("");
                        surveyVo.setDtr_location_name("");
                        surveyVo.setDtr_no_of_circuit("");
                        surveyVo.setDtr_location_type("");
                        surveyVo.setIs_ltckt_avalable("");
                        surveyVo.setType_of_load("");
                    }

                    if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                        try {
                            String[] auto_oh_ht_service_numberStrArry = auto_oh_ht_service_number.getText().toString().split("~");
                            surveyVo.setHt_service_number(auto_oh_ht_service_numberStrArry[0]);
                            surveyVo.setPc_line_capacitor("");
                            surveyVo.setPc_auto_reclosure("");
                            surveyVo.setNo_of_ab_switches("");
                        } catch (Exception e) {
                            surveyVo.setHt_service_number("");
                            surveyVo.setPc_line_capacitor("");
                            surveyVo.setPc_auto_reclosure("");
                            surveyVo.setNo_of_ab_switches("");
                        }
                    } else if ("Line Capacitor".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setPc_line_capacitor(sp_oh_line_capacitor.getSelectedItem().toString());
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_auto_reclosure("");
                        surveyVo.setNo_of_ab_switches("");
                    } else if ("Auto Reclosure".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setPc_auto_reclosure(sp_oh_auto_reclosure.getSelectedItem().toString());
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_line_capacitor("");
                        surveyVo.setNo_of_ab_switches("");
                    } else if ("AB-Pole".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setPc_auto_reclosure("");
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_line_capacitor("");
                        surveyVo.setNo_of_ab_switches(sp_oh_no_of_ab_switches.getSelectedItem().toString());
                    } else {
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_line_capacitor("");
                        surveyVo.setPc_auto_reclosure("");
                        surveyVo.setNo_of_ab_switches("");
                    }


                    surveyVo.setVoltage_level(sp_oh_voltage_level.getSelectedItem().toString());
                    //surveyVo.setDiffVoltage(diffvoltage);
                    surveyVo.setSsId(ssId);
                    surveyVo.setNoofTappings(nooftappings);
                    surveyVo.setTappingLocation(edt_tappinglocation.getText().toString());
                    surveyVo.setRemarks(edt_remarks_overhead.getText().toString());
                    surveyVo.setWoId(woId.get(0));
                    surveyVo.setIsFeederType(isFeederType.get(0));
                    /*SURVEY_TYPE*/


                    //NEW DEFECTS AND CROSSING

                    if (defect_selected.equalsIgnoreCase("Yes")) {
                        surveyVo.setDEFFECTS_AVAILABLE("YES");
                        surveyVo.setCROSSARM_DEFECTS(ch_cross_arm.isChecked() ? sp_cross_arm.getSelectedItem().toString() : "");
                        surveyVo.setTOPCLEAT_DEFECTS(ch_top_cleat.isChecked() ? sp_top_cleat.getSelectedItem().toString() : "");
                        surveyVo.setSTRUT_DEFECTS(ch_strut_pole.isChecked() ? sp_strut_pole.getSelectedItem().toString() : "");
                        surveyVo.setSTRAY_DEFECTS(ch_stray_wire.isChecked() ? sp_stray_wire.getSelectedItem().toString() : "");
                        surveyVo.setHTLT_CLEARANCE(ch_ht_clearance.isChecked() ? sp_ht_clearance.getSelectedItem().toString() : "");
                        surveyVo.setINTERMEDIATE_POLE(ch_int_pole.isChecked() ? sp_int_pole.getSelectedItem().toString() : "");
                        surveyVo.setRESTRINGING(ch_restringing.isChecked() ? sp_restringing.getSelectedItem().toString() : "");
                        surveyVo.setTREE_CLEARANCE(ch_tree_clearance.isChecked() ? sp_tree_clearance.getSelectedItem().toString() : "");
                        surveyVo.setOTHER_DEFFECTS(ch_other_defects.isChecked() ? ed_other_defects.getText().toString().trim() : "");
                        surveyVo.setPOLE_DEFECTS(ch_pole_defects.isChecked() ? sp_pole_defects.getSelectedItem().toString() : "");
                        surveyVo.setCONDUCTOR_DEFECTS(ch_conductors.isChecked() ? sp_conductors.getSelectedItem().toString() : "");
                        surveyVo.setAB_SWITCH_DEFECTS(ch_ab_switch_defects.isChecked() ? sp_ab_switch_defects.getSelectedItem().toString() : "");
                        surveyVo.setINSULATOR_DEFECTS(ch_insulators.isChecked() ? sp_insulators.getSelectedItem().toString() : "");

                    } else {
                        surveyVo.setDEFFECTS_AVAILABLE("NO");
                        surveyVo.setCROSSARM_DEFECTS("");
                        surveyVo.setTOPCLEAT_DEFECTS("");
                        surveyVo.setSTRUT_DEFECTS("");
                        surveyVo.setSTRAY_DEFECTS("");
                        surveyVo.setHTLT_CLEARANCE("");
                        surveyVo.setINTERMEDIATE_POLE("");
                        surveyVo.setRESTRINGING("");
                        surveyVo.setTREE_CLEARANCE("");
                        surveyVo.setOTHER_DEFFECTS("");
                        surveyVo.setPOLE_DEFECTS("");
                        surveyVo.setCONDUCTOR_DEFECTS("");
                        surveyVo.setAB_SWITCH_DEFECTS("");
                        surveyVo.setINSULATOR_DEFECTS("");
                    }


                    //IMAGES START

                    surveyVo.setIMAGE_1(image_1_clicked ? image_1_base64 : "N");
                    surveyVo.setIMAGE_2(image_2_clicked ? image_2_base64 : "N");
                    surveyVo.setIMAGE_3(image_3_clicked ? image_3_base64 : "N");

                    //IMAGES END

                    //OTHER CABLES
                    if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                        surveyVo.setNUM_OTHER_CABLES(edt_oh_other_cables.getText().toString());
                    } else {
                        surveyVo.setNUM_OTHER_CABLES("");
                    }


                    surveyVo.setPOLE_CONCRETE(sp_pole_concrete.getSelectedItem().toString());
                    surveyVo.setCURRENT_LAT(""+CURRENT_LAT);
                    surveyVo.setCURRENT_LONG(""+CURRENT_LONG);

                    //CHECK MULTI CIRCUIT DUPLICATE START

                    if ("Yes".equalsIgnoreCase(ismulticircute)){
                        List<PMISurveyVo> checkList = arcGISDatabase.checkMultiCktForLT();
                        if (!checkList.isEmpty()){
                            for (int i = 0; i < checkList.size(); i++) {
                                //33KV
                                if (ch_oh_33kv_vlckts.isChecked()){
                                    //FEEDER 1
                                    if (!surveyVo.getMC_33KV_FEEDER_ID_1().equals("N")){
                                        if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyVo.getMC_33KV_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyVo.getMC_33KV_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyVo.getMC_33KV_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 2
                                    if (!surveyVo.getMC_33KV_FEEDER_ID_2().equals("N")){
                                        if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyVo.getMC_33KV_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyVo.getMC_33KV_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyVo.getMC_33KV_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 3
                                    if (!surveyVo.getMC_33KV_FEEDER_ID_3().equals("N")){
                                        if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyVo.getMC_33KV_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyVo.getMC_33KV_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyVo.getMC_33KV_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                }

                                //11KV
                                if (ch_oh_11kv_vlckts.isChecked()){
                                    //FEEDER 1
                                    if (!surveyVo.getMC_11KV_FEEDER_ID_1().equals("N")){
                                        if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyVo.getMC_11KV_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyVo.getMC_11KV_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyVo.getMC_11KV_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 2
                                    if (!surveyVo.getMC_11KV_FEEDER_ID_2().equals("N")){
                                        if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyVo.getMC_11KV_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyVo.getMC_11KV_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyVo.getMC_11KV_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 3
                                    if (!surveyVo.getMC_11KV_FEEDER_ID_3().equals("N")){
                                        if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyVo.getMC_11KV_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyVo.getMC_11KV_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyVo.getMC_11KV_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                }

                                //LT
                                if (ch_oh_lt_vlckts.isChecked()){
                                    //FEEDER 1
                                    if (!surveyVo.getMC_LT_FEEDER_ID_1().equals("N")){
                                        if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyVo.getMC_LT_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyVo.getMC_LT_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyVo.getMC_LT_POLE_1())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 2
                                    if (!surveyVo.getMC_LT_FEEDER_ID_2().equals("N")){
                                        if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyVo.getMC_LT_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyVo.getMC_LT_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyVo.getMC_LT_POLE_2())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 3
                                    if (!surveyVo.getMC_LT_FEEDER_ID_3().equals("N")){
                                        if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyVo.getMC_LT_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyVo.getMC_LT_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyVo.getMC_LT_POLE_3())){
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                }



                            }
                        }
                    }

                    //CHECK MULTI CIRCUIT DUPLICATE END


                    if (from.equals("int")) {
                        //Toast.makeText(c, "FROM INT", Toast.LENGTH_SHORT).show();
                        callAddApi(surveyVo, "OverHead");
                    } else {
                        boolean normal_status = arcGISDatabase.addSurveyData(surveyVo);
                        boolean repeat_status = arcGISDatabase.addSurveyDatarepeat(surveyVo);
                        boolean image_status = arcGISDatabase.addImageData(surveyVo);
                        // System.out.println("normal_status : "+normal_status);
                        //  System.out.println("repeat_status : "+repeat_status);
                        //  System.out.println("image_status : "+image_status);
                        if ("YES".equalsIgnoreCase(isendpole)) {
                            if (poleconfiguration.equals("DTR Pole")) {
                                PMIArcGISMapActivity11kv.isDTREndPole = true;
                            } else {
                                Intent intent = new Intent(c, PMIHomeActivity.class);
                                c.startActivity(intent);
                                c.finish();
                            }

                        }
                        if ("Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr)) {
                            PMIArcGISMapActivity11kv.isDTRSurvey = true;
                            PMIArcGISMapActivity11kv.noOfSwitch = sp_oh_no_of_lt_circuits.getSelectedItem().toString();
                        } else {
                            PMIArcGISMapActivity11kv.isDTRSurvey = false;
                        }
                        image_1_clicked = false;
                        image_2_clicked = false;
                        image_3_clicked = false;
                        repeatPoleClicked = false;
                        if (surveyType.equalsIgnoreCase("Spur Line")){
                            PMIArcGISMapActivity11kv.SPUR_FIRST_POLE = false;
                        }

                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);
                    }


                }
            }
        }
    }

    public void underGroundData() {
        try {
          /*  if ("Select".equalsIgnoreCase(sp_ug_voltage_level.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Voltage Level", Toast.LENGTH_LONG).show();
            }else*/

            if ("Select".equalsIgnoreCase(Survey_type_string)) {
                Toast.makeText(c, "Select Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_noofcircuit_underg.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Cables", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(straight_rmu)) {
                Toast.makeText(c, "Select Location Type", Toast.LENGTH_LONG).show();
            }
            else if ("RMU".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_rmu.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU Type", Toast.LENGTH_LONG).show();
            }

            else if ("RMU".equalsIgnoreCase(straight_rmu) && ed_rmu_serial_number.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter RMU Serial Number ", Toast.LENGTH_LONG).show();
            }

            else if ("RMU".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_rmu_make.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU Make ", Toast.LENGTH_LONG).show();
            }


            else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoletype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Type ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoleheight.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Height ", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_ugtapingpoint.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Tapping Point ", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ugtapingpoint) && "Select".equalsIgnoreCase(sp_ugnooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ugtapingpoint) && "".equalsIgnoreCase(sp_ugnooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ugtapingpoint) && edt_ugtappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(cablesize)) {
                Toast.makeText(c, "Select Cable Size ", Toast.LENGTH_LONG).show();
            } /*else if ("Select".equalsIgnoreCase(sp_pole_concrete.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Concrete", Toast.LENGTH_LONG).show();
            }*/ else {

                if ("DTR".equalsIgnoreCase(straight_rmu)) {
                    if (sp_ug_section_name_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Section Name ", Toast.LENGTH_LONG).show();
                    } else if (auto_ug_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    } else if (!arcGISDatabase.checkValueDTRCodeMethod(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                    } else if (arcGISDatabase.checkValueDTRCodeLoacalSave(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                    } else if (sp_ug_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_ug_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_ug_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && auto_ug_make_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Make ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && !makeArrayList.contains(auto_ug_make_dtr_pole.getText().toString())) {
                        Toast.makeText(c, "Enter Make Not Match ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && ed_ug_serial_number_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Serial Number ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && ed_ug_location_name_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_ug_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_ug_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_ug_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Is OH LT ckts available  ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && "Yes".equalsIgnoreCase(ug_is_lt_ckts_availableStr) && sp_ug_no_of_lt_circuits.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select No Of LT Circuit ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_ug_ab_switch_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select AB Switch ", Toast.LENGTH_LONG).show();
                    } else {
                        underGroundDataValidation();
                    }
                } else if ("HT Service".equalsIgnoreCase(straight_rmu)) {
                    try {
                        if (auto_ug_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkValueMethod(auto_ug_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {
                            underGroundDataValidation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                    }
                } else if ("Line Capacitor".equalsIgnoreCase(straight_rmu)) {
                    if ("Select".equalsIgnoreCase(sp_ug_line_capacitor.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Line Capacitor ", Toast.LENGTH_LONG).show();
                    } else {
                        underGroundDataValidation();
                    }
                } else if ("Auto Reclosure".equalsIgnoreCase(straight_rmu)) {
                    if ("Select".equalsIgnoreCase(sp_ug_auto_reclosure.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Auto Reclosure ", Toast.LENGTH_LONG).show();
                    } else {
                        underGroundDataValidation();
                    }
                } else if ("AB-Pole".equalsIgnoreCase(straight_rmu)) {
                    if ("Select".equalsIgnoreCase(sp_ug_no_of_ab_switches.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select No Of AB Switches ", Toast.LENGTH_LONG).show();
                    } else {
                        underGroundDataValidation();
                    }
                } else {
                    underGroundDataValidation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void underGroundDataValidation() {
        if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();
            } else {
                if (x.equalsIgnoreCase("0.0") && y.equalsIgnoreCase("0.0")) {
                    Toast.makeText(c, "Gps Loading .....!!", Toast.LENGTH_LONG).show();
                } else {
                    List<String> woId = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT WO_ID FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    List<String> isFeederType = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    PMISurveyVo underGroundsurveyVo = new PMISurveyVo();

                    //JHC PHASE 1
                    if (LoginActivity.isJHC) {
                        //  underGroundsurveyVo.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                        //  underGroundsurveyVo.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
                        if (sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                            underGroundsurveyVo.setPOLE_SURVEY_TYPE("JHC");
                        }else {
                            underGroundsurveyVo.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                        }

                        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                            underGroundsurveyVo.setLINE_SURVEY_TYPE("JHC");
                        }else {
                            underGroundsurveyVo.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
                        }
                    } else {
                        underGroundsurveyVo.setPOLE_SURVEY_TYPE("");
                        underGroundsurveyVo.setLINE_SURVEY_TYPE("");
                        underGroundsurveyVo.setSurvaytype(Survey_type_string);
                    }

                    underGroundsurveyVo.setFeeder_id(feederCode);
                    underGroundsurveyVo.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                    underGroundsurveyVo.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                    underGroundsurveyVo.setLineType(lineType);

                    underGroundsurveyVo.setLat(x);
                    underGroundsurveyVo.setLan(y);
                    underGroundsurveyVo.setPastSurveyFlag("N");
                    underGroundsurveyVo.setStjoint_rmu(straight_rmu);//c

                    underGroundsurveyVo.setIsEndPole(undergisendpole);//c
                    underGroundsurveyVo.setRemarks(edt_remarks_underground.getText().toString());
                    if (!"Select".equalsIgnoreCase(rmu)) {
                        underGroundsurveyVo.setRmu_type(rmu);//c
                        if ("RMU".equalsIgnoreCase(straight_rmu)){
                            underGroundsurveyVo.setRMU_MAKE(sp_rmu_make.getSelectedItem().toString().equalsIgnoreCase("Select") ? "" :sp_rmu_make.getSelectedItem().toString());
                            underGroundsurveyVo.setRMU_SL_NO(ed_rmu_serial_number.getText().toString());
                        }
                        else {
                            underGroundsurveyVo.setRMU_MAKE("");
                            underGroundsurveyVo.setRMU_SL_NO("");
                        }

                    } else {
                        underGroundsurveyVo.setRmu_type("");//c
                        underGroundsurveyVo.setRMU_MAKE("");
                        underGroundsurveyVo.setRMU_SL_NO("");
                    }


                    if ("Structure".equalsIgnoreCase(straight_rmu)) {
                        underGroundsurveyVo.setPoleStructure(underpolestructure);
                    } else {
                        underGroundsurveyVo.setPoleStructure("");
                    }
                    if (!"Select".equalsIgnoreCase(noofcircuit_underg)) {
                        underGroundsurveyVo.setNoofCircuits(noofcircuit_underg);//c
                    } else {
                        underGroundsurveyVo.setNoofCircuits("");//c
                    }

                    if (!"Select".equalsIgnoreCase(cablesize)) {
                        underGroundsurveyVo.setConductor_size(cablesize);//c
                    } else {
                        underGroundsurveyVo.setConductor_size("");//c
                    }
                    if (!"Select".equalsIgnoreCase(ugpoletype)) {
                        underGroundsurveyVo.setPole_type(ugpoletype);//C
                    } else {
                        underGroundsurveyVo.setPole_type("");//C
                    }
                    if (!"Select".equalsIgnoreCase(ugpoleheight)) {
                        underGroundsurveyVo.setPole_height(ugpoleheight);//C
                    } else {
                        underGroundsurveyVo.setPole_height("");//C
                    }
                    if (!"Select".equalsIgnoreCase(ugpolesupport)) {
                        underGroundsurveyVo.setPole_support(ugpolesupport);//C
                    } else {
                        underGroundsurveyVo.setPole_support("");//C
                    }
                    underGroundsurveyVo.setSs_name(ugssname);
                    if (!"Select".equalsIgnoreCase(ugtapingpoint)) {
                        underGroundsurveyVo.setTapping_point(ugtapingpoint);//c
                    } else {
                        underGroundsurveyVo.setTapping_point("");//c
                    }
                    if (!"Select".equalsIgnoreCase(ugnooftappings)) {
                        underGroundsurveyVo.setNoofTappings(ugnooftappings);//c
                    } else {
                        underGroundsurveyVo.setNoofTappings("");//c
                    }
                    underGroundsurveyVo.setTappingLocation(edt_ugtappinglocation.getText().toString());//c
                    underGroundsurveyVo.setPole_paintingcode(edt_ugpolepaintingcode.getText().toString());//c
                    underGroundsurveyVo.setVoltage_level(sp_ug_voltage_level.getSelectedItem().toString());//c
                    if ("DTR".equalsIgnoreCase(straight_rmu)) {
                        underGroundsurveyVo.setDtr_phase(sp_ug_phase_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVo.setDTR_AVAILABLE(sp_ug_is_dtr_avalabile.getSelectedItem().toString());//C
                        underGroundsurveyVo.setDtr_capacity(sp_ug_capacity_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVo.setDtr_ab_switch(sp_ug_ab_switch_dtr_pole.getSelectedItem().toString());//c
                        underGroundsurveyVo.setDtr_make(auto_ug_make_dtr_pole.getText().toString());///c
                        underGroundsurveyVo.setDtr_structure(sp_ug_dtr_structure_type_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVo.setDtr_scno(ed_ug_serial_number_dtr_pole.getText().toString());//c
                        try {
                            underGroundsurveyVo.setDtr_code(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0]);//C
                        } catch (Exception e) {
                            underGroundsurveyVo.setDtr_code("");//C
                            Toast.makeText(c, "DTR Structure code Should not be null", Toast.LENGTH_LONG).show();
                        }
                        underGroundsurveyVo.setDtr_location_name(ed_ug_location_name_dtr_pole.getText().toString());///c
                        if ("Yes".equalsIgnoreCase(sp_ug_is_lt_ckts_available.getSelectedItem().toString())) {
                            underGroundsurveyVo.setDtr_no_of_circuit(sp_ug_no_of_lt_circuits.getSelectedItem().toString());//c
                        } else {
                            underGroundsurveyVo.setDtr_no_of_circuit("");//c
                        }
                        underGroundsurveyVo.setDtr_location_type(sp_ug_dtr_location_type.getSelectedItem().toString());
                        underGroundsurveyVo.setIs_ltckt_avalable(sp_ug_is_lt_ckts_available.getSelectedItem().toString());
                        underGroundsurveyVo.setType_of_load(sp_ug_type_of_load.getSelectedItem().toString());
                    } else {
                        underGroundsurveyVo.setDtr_phase("");
                        underGroundsurveyVo.setDTR_AVAILABLE("");
                        underGroundsurveyVo.setDtr_capacity("");
                        underGroundsurveyVo.setDtr_ab_switch("");
                        underGroundsurveyVo.setDtr_make("");
                        underGroundsurveyVo.setDtr_structure("");
                        underGroundsurveyVo.setDtr_scno("");
                        underGroundsurveyVo.setDtr_code("");
                        underGroundsurveyVo.setDtr_location_name("");
                        underGroundsurveyVo.setDtr_no_of_circuit("");
                        underGroundsurveyVo.setDtr_location_type("");
                        underGroundsurveyVo.setIs_ltckt_avalable("");
                        underGroundsurveyVo.setType_of_load("");

                    }


                    //sp_ug_struct_stay_detailes       -PENDING FILED
                    if ("Select".equalsIgnoreCase(sp_ug_line_capacitor.getSelectedItem().toString())) {
                        underGroundsurveyVo.setPc_line_capacitor("");
                    } else {
                        underGroundsurveyVo.setPc_line_capacitor(sp_ug_line_capacitor.getSelectedItem().toString());//c
                    }
                    if ("Select".equalsIgnoreCase(sp_ug_auto_reclosure.getSelectedItem().toString())) {
                        underGroundsurveyVo.setPc_auto_reclosure("");
                    } else {
                        underGroundsurveyVo.setPc_auto_reclosure(sp_ug_auto_reclosure.getSelectedItem().toString());//c
                    }
                    if ("AB-Pole".equalsIgnoreCase(straight_rmu)) {
                        underGroundsurveyVo.setNo_of_ab_switches(sp_ug_no_of_ab_switches.getSelectedItem().toString());
                    } else {
                        underGroundsurveyVo.setNo_of_ab_switches("");
                    }
                    try {
                        underGroundsurveyVo.setHt_service_number(auto_ug_ht_service_number.getText().toString().split("~")[0]);//c
                    } catch (Exception e) {
                        underGroundsurveyVo.setHt_service_number("");//c
                    }
                    underGroundsurveyVo.setWoId(woId.get(0));
                    underGroundsurveyVo.setIsFeederType(isFeederType.get(0));


                    //NEW
                    underGroundsurveyVo.setDEFFECTS_AVAILABLE("NO");
                    underGroundsurveyVo.setCROSSARM_DEFECTS("");
                    underGroundsurveyVo.setTOPCLEAT_DEFECTS("");
                    underGroundsurveyVo.setSTRUT_DEFECTS("");
                    underGroundsurveyVo.setSTRAY_DEFECTS("");
                    underGroundsurveyVo.setHTLT_CLEARANCE("");
                    underGroundsurveyVo.setINTERMEDIATE_POLE("");
                    underGroundsurveyVo.setRESTRINGING("");
                    underGroundsurveyVo.setTREE_CLEARANCE("");
                    underGroundsurveyVo.setOTHER_DEFFECTS("");
                    underGroundsurveyVo.setPOLE_DEFECTS("");
                    underGroundsurveyVo.setCONDUCTOR_DEFECTS("");
                    underGroundsurveyVo.setAB_SWITCH_DEFECTS("");
                    underGroundsurveyVo.setINSULATOR_DEFECTS("");
                    underGroundsurveyVo.setCROSSING_TYPE("");

                    //IMAGES START

                    underGroundsurveyVo.setIMAGE_1(image_1_clicked ? image_1_base64 : "N");
                    underGroundsurveyVo.setIMAGE_2(image_2_clicked ? image_2_base64 : "N");
                    underGroundsurveyVo.setIMAGE_3(image_3_clicked ? image_3_base64 : "N");

                    //IMAGES END

                    underGroundsurveyVo.setPOLE_CONCRETE("");
                    underGroundsurveyVo.setCURRENT_LAT(""+CURRENT_LAT);
                    underGroundsurveyVo.setCURRENT_LONG(""+CURRENT_LONG);

                    if (from.equals("int")) {

                        // Toast.makeText(c, "FROM INT", Toast.LENGTH_SHORT).show();
                        callAddApi(underGroundsurveyVo, "UnderGround");
                    } else {
                        boolean normal_status = arcGISDatabase.addSurveyData(underGroundsurveyVo);
                        boolean repeat_status = arcGISDatabase.addSurveyDatarepeat(underGroundsurveyVo);

                        // System.out.println("normal_status : "+normal_status);
                        // System.out.println("repeat_status : "+repeat_status);

                        if ("Y".equalsIgnoreCase(undergisendpole)) {
                            Intent intent = new Intent(c, PMIHomeActivity.class);
                            c.startActivity(intent);
                            c.finish();
                        }

                        if ("Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr)) {
                            PMIArcGISMapActivity11kv.isDTRSurvey = true;
                            PMIArcGISMapActivity11kv.noOfSwitch = sp_oh_no_of_lt_circuits.getSelectedItem().toString();
                        } else {
                            PMIArcGISMapActivity11kv.isDTRSurvey = false;
                        }
                        image_1_clicked = false;
                        image_2_clicked = false;
                        image_3_clicked = false;
                        repeatPoleClicked = false;
                        if (surveyType.equalsIgnoreCase("Spur Line")){
                            PMIArcGISMapActivity11kv.SPUR_FIRST_POLE = false;
                        }
                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);
                    }
                }
            }
        }
    }


    public void abCableData() {
        try {

            if ("Select".equalsIgnoreCase(Survey_type_string)) {
                Toast.makeText(c, "Select Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_abc_noofcircuit.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Cables", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(abc_straight_rmuStr)) {
                Toast.makeText(c, "Select Location Type", Toast.LENGTH_LONG).show();
            } else if ("RMU".equalsIgnoreCase(abc_straight_rmuStr) && "Select".equalsIgnoreCase(sp_abc_rmu.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(abc_straight_rmuStr) && "Select".equalsIgnoreCase(sp_abc_poletype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Type ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(abc_straight_rmuStr) && "Select".equalsIgnoreCase(sp_abc_poleheight.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Height ", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_abc_tapingpoint.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Tapping Point ", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(abc_tapingpoint) && "Select".equalsIgnoreCase(sp_abc_nooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(abc_tapingpoint) && edt_abc_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(abc_cablesizeStr)) {
                Toast.makeText(c, "Select Cable Size ", Toast.LENGTH_LONG).show();
            } /*else if ("Select".equalsIgnoreCase(sp_pole_concrete.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Concrete", Toast.LENGTH_LONG).show();
            }*/ else {
                if ("DTR".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if (sp_abc_section_name_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Section Name ", Toast.LENGTH_LONG).show();
                    } else if (auto_abc_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    } else if (!arcGISDatabase.checkValueDTRCodeMethod(auto_abc_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                    } else if (arcGISDatabase.checkValueDTRCodeLoacalSave(auto_abc_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                    } else if (auto_abc_make_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Make ", Toast.LENGTH_LONG).show();
                    } else if (!makeArrayList.contains(auto_abc_make_dtr_pole.getText().toString())) {
                        Toast.makeText(c, "Enter Make Not Match ", Toast.LENGTH_LONG).show();
                    } else if (ed_abc_serial_number_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Serial Number ", Toast.LENGTH_LONG).show();
                    } else if (ed_abc_location_name_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Is OH LT ckts available  ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(abc_is_lt_ckts_availableStr) && sp_abc_no_of_lt_circuits.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select No Of LT Circuit ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_ab_switch_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select AB Switch ", Toast.LENGTH_LONG).show();
                    } else {
                        abCableCoverdConductorValidation();
                    }
                } else if ("HT Service".equalsIgnoreCase(abc_straight_rmuStr)) {
                    try {
                        if (auto_abc_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkValueMethod(auto_abc_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {
                            abCableCoverdConductorValidation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                    }
                } else if ("Line Capacitor".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if ("Select".equalsIgnoreCase(sp_abc_line_capacitor.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Line Capacitor ", Toast.LENGTH_LONG).show();
                    } else {
                        abCableCoverdConductorValidation();
                    }
                } else if ("Auto Reclosure".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if ("Select".equalsIgnoreCase(sp_abc_auto_reclosure.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Auto Reclosure ", Toast.LENGTH_LONG).show();
                    } else {
                        abCableCoverdConductorValidation();
                    }
                } else if ("AB-Pole".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if ("Select".equalsIgnoreCase(sp_abc_no_of_ab_switches.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select No Of AB Switches ", Toast.LENGTH_LONG).show();
                    } else {
                        abCableCoverdConductorValidation();
                    }
                } else {
                    abCableCoverdConductorValidation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void abCableCoverdConductorValidation() {
        if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();
            } else {
                if (x.equalsIgnoreCase("0.0") && y.equalsIgnoreCase("0.0")) {
                    Toast.makeText(c, "Gps Loading .....!!", Toast.LENGTH_LONG).show();
                } else {
                    List<String> woId = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT WO_ID FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    List<String> isFeederType = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    PMISurveyVo abc_vo = new PMISurveyVo();

                    //JHC PHASE 1
                    if (LoginActivity.isJHC) {
                        //  abc_vo.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                        // abc_vo.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());

                        if (sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                            abc_vo.setPOLE_SURVEY_TYPE("JHC");
                        }else {
                            abc_vo.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                        }

                        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                            abc_vo.setLINE_SURVEY_TYPE("JHC");
                        }else {
                            abc_vo.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
                        }
                    } else {
                        abc_vo.setPOLE_SURVEY_TYPE("");
                        abc_vo.setLINE_SURVEY_TYPE("");
                        abc_vo.setSurvaytype(Survey_type_string);
                    }

                    abc_vo.setFeeder_id(feederCode);
                    abc_vo.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                    abc_vo.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                    abc_vo.setLineType(lineType);

                    abc_vo.setLat(x);
                    abc_vo.setLan(y);
                    abc_vo.setPastSurveyFlag("N");
                    abc_vo.setStjoint_rmu(abc_straight_rmuStr);//c


                    abc_vo.setIsEndPole(abc_isendpole);//cabc_isendpole
                    abc_vo.setRemarks(edt_abc_remarks.getText().toString());
                    if (!"Select".equalsIgnoreCase(sp_abc_rmuStr)) {
                        abc_vo.setRmu_type(sp_abc_rmuStr);//c
                    } else {
                        abc_vo.setRmu_type("");//c
                    }


                    if ("Structure".equalsIgnoreCase(abc_straight_rmuStr)) {
                        abc_vo.setPoleStructure(sp_abc_derpolestructureStr);
                    } else {
                        abc_vo.setPoleStructure("");
                    }
                    if (!"Select".equalsIgnoreCase(abc_noofcircuitStr)) {
                        abc_vo.setNoofCircuits(abc_noofcircuitStr);//c
                    } else {
                        abc_vo.setNoofCircuits("");//c
                    }

                    if (!"Select".equalsIgnoreCase(abc_cablesizeStr)) {
                        abc_vo.setConductor_size(abc_cablesizeStr);//c
                    } else {
                        abc_vo.setConductor_size("");//c
                    }
                    if (!"Select".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        abc_vo.setPole_type(sp_abc_pole_type_str);//C
                    } else {
                        abc_vo.setPole_type("");//C
                    }
                    if (!"Select".equalsIgnoreCase(abc_poleheightStr)) {
                        abc_vo.setPole_height(abc_poleheightStr);//C
                    } else {
                        abc_vo.setPole_height("");//C
                    }
                    if (!"Select".equalsIgnoreCase(sp_abc_polesupportStr)) {
                        abc_vo.setPole_support(sp_abc_polesupportStr);//C
                    } else {
                        abc_vo.setPole_support("");//C
                    }

                    if (!"Select".equalsIgnoreCase(abc_tapingpoint)) {
                        abc_vo.setTapping_point(abc_tapingpoint);//c
                    } else {
                        abc_vo.setTapping_point("");//c
                    }
                    if (!"Select".equalsIgnoreCase(abc_nooftappings)) {
                        abc_vo.setNoofTappings(abc_nooftappings);//c
                    } else {
                        abc_vo.setNoofTappings("");//c
                    }

                    if ("DTR".equalsIgnoreCase(abc_straight_rmuStr)) {
                        abc_vo.setDtr_phase(sp_abc_phase_dtr_pole.getSelectedItem().toString());//C
                        abc_vo.setDTR_AVAILABLE("");//C
                        abc_vo.setDtr_capacity(sp_abc_capacity_dtr_pole.getSelectedItem().toString());//C
                        abc_vo.setDtr_ab_switch(sp_abc_ab_switch_dtr_pole.getSelectedItem().toString());//c
                        abc_vo.setDtr_make(auto_abc_make_dtr_pole.getText().toString());///c
                        abc_vo.setDtr_structure(sp_abc_dtr_structure_type_dtr_pole.getSelectedItem().toString());//C
                        abc_vo.setDtr_scno(ed_abc_serial_number_dtr_pole.getText().toString());//c
                        try {
                            abc_vo.setDtr_code(auto_abc_dtr_structure_dtr_pole.getText().toString().split("~")[0]);//C
                        } catch (Exception e) {
                            abc_vo.setDtr_code("");//C
                            Toast.makeText(c, "DTR Structure code Should not be null", Toast.LENGTH_LONG).show();
                        }

                        abc_vo.setDtr_location_name(ed_abc_location_name_dtr_pole.getText().toString());///c
                        if ("Yes".equalsIgnoreCase(sp_abc_is_lt_ckts_available.getSelectedItem().toString())) {
                            abc_vo.setDtr_no_of_circuit(sp_abc_no_of_lt_circuits.getSelectedItem().toString());//c
                        } else {
                            abc_vo.setDtr_no_of_circuit("");//c
                        }

                        abc_vo.setDtr_location_type(sp_abc_dtr_location_type.getSelectedItem().toString());
                        abc_vo.setIs_ltckt_avalable(sp_abc_is_lt_ckts_available.getSelectedItem().toString());
                        abc_vo.setType_of_load(sp_abc_type_of_load.getSelectedItem().toString());

                    } else {
                        abc_vo.setDtr_phase("");
                        abc_vo.setDTR_AVAILABLE("");
                        abc_vo.setDtr_capacity("");
                        abc_vo.setDtr_ab_switch("");
                        abc_vo.setDtr_make("");
                        abc_vo.setDtr_structure("");
                        abc_vo.setDtr_scno("");
                        abc_vo.setDtr_code("");
                        abc_vo.setDtr_location_name("");
                        abc_vo.setDtr_no_of_circuit("");
                        abc_vo.setDtr_location_type("");
                        abc_vo.setIs_ltckt_avalable("");
                        abc_vo.setType_of_load("");
                    }


                    //sp_ug_struct_stay_detailes       -PENDING FILED
                    if ("Select".equalsIgnoreCase(sp_abc_line_capacitor.getSelectedItem().toString())) {
                        abc_vo.setPc_line_capacitor("");
                    } else {
                        abc_vo.setPc_line_capacitor(sp_abc_line_capacitor.getSelectedItem().toString());//c
                    }
                    if ("Select".equalsIgnoreCase(sp_abc_auto_reclosure.getSelectedItem().toString())) {
                        abc_vo.setPc_auto_reclosure("");
                    } else {
                        abc_vo.setPc_auto_reclosure(sp_abc_auto_reclosure.getSelectedItem().toString());//c
                    }
                    if ("AB-Pole".equalsIgnoreCase(abc_straight_rmuStr)) {
                        abc_vo.setNo_of_ab_switches(sp_abc_no_of_ab_switches.getSelectedItem().toString());
                    } else {
                        abc_vo.setNo_of_ab_switches("");
                    }
                    abc_vo.setTappingLocation(edt_abc_tappinglocation.getText().toString());//c
                    abc_vo.setPole_paintingcode(edt_abc_polepaintingcode.getText().toString());//c
                    abc_vo.setVoltage_level(sp_abc_voltage_level.getSelectedItem().toString());//c
                    try {
                        abc_vo.setHt_service_number(auto_abc_ht_service_number.getText().toString().split("~")[0]);//c
                    } catch (Exception e) {
                        abc_vo.setHt_service_number("");//c
                    }
                    abc_vo.setSs_name("");//ugssname is no filed

                    abc_vo.setWoId(woId.get(0));
                    abc_vo.setIsFeederType(isFeederType.get(0));


                    //NEW
                    abc_vo.setDEFFECTS_AVAILABLE("NO");
                    abc_vo.setCROSSARM_DEFECTS("");
                    abc_vo.setTOPCLEAT_DEFECTS("");
                    abc_vo.setSTRUT_DEFECTS("");
                    abc_vo.setSTRAY_DEFECTS("");
                    abc_vo.setHTLT_CLEARANCE("");
                    abc_vo.setINTERMEDIATE_POLE("");
                    abc_vo.setRESTRINGING("");
                    abc_vo.setTREE_CLEARANCE("");
                    abc_vo.setOTHER_DEFFECTS("");
                    abc_vo.setPOLE_DEFECTS("");
                    abc_vo.setCONDUCTOR_DEFECTS("");
                    abc_vo.setAB_SWITCH_DEFECTS("");
                    abc_vo.setINSULATOR_DEFECTS("");
                    abc_vo.setCROSSING_TYPE("");


                    //IMAGES START

                    abc_vo.setIMAGE_1(image_1_clicked ? image_1_base64 : "N");
                    abc_vo.setIMAGE_2(image_2_clicked ? image_2_base64 : "N");
                    abc_vo.setIMAGE_3(image_3_clicked ? image_3_base64 : "N");

                    //IMAGES END

                    //POLE CONCRETE
                    abc_vo.setPOLE_CONCRETE("");
                    abc_vo.setCURRENT_LAT(""+CURRENT_LAT);
                    abc_vo.setCURRENT_LONG(""+CURRENT_LONG);

                    if (from.equals("int")) {
                        //Toast.makeText(c, "FROM INT", Toast.LENGTH_SHORT).show();
                        callAddApi(abc_vo, "abc");
                    } else {


                        arcGISDatabase.addSurveyData(abc_vo);
                        arcGISDatabase.addSurveyDatarepeat(abc_vo);
                        //  System.out.println("normal_status : "+normal_status);
                        //  System.out.println("repeat_status : "+repeat_status);
                        if ("Y".equalsIgnoreCase(abc_isendpole)) {
                            Intent intent = new Intent(c, PMIHomeActivity.class);
                            c.startActivity(intent);
                            c.finish();
                        }

                        if ("Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr)) {
                            PMIArcGISMapActivity11kv.isDTRSurvey = true;
                            PMIArcGISMapActivity11kv.noOfSwitch = sp_oh_no_of_lt_circuits.getSelectedItem().toString();
                        } else {
                            PMIArcGISMapActivity11kv.isDTRSurvey = false;
                        }
                        image_1_clicked = false;
                        image_2_clicked = false;
                        image_3_clicked = false;
                        repeatPoleClicked = false;
                        if (surveyType.equalsIgnoreCase("Spur Line")){
                            PMIArcGISMapActivity11kv.SPUR_FIRST_POLE = false;
                        }
                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);
                    }
                }
            }
        }
    }


    private void callAddApi(PMISurveyVo surveyVo, String type) {
        callout.dismiss();
        progressDialog.show();


        SharedPreferences preferences;
        String userName, sessionId;
        preferences = PreferenceManager.getDefaultSharedPreferences(c);
        userName = preferences.getString(MobileConstants.USERNAME, "");
        sessionId = preferences.getString(MobileConstants.SESSION_ID, "");

        PMISurveyedRequestVo PMISurveyedRequestVo = new PMISurveyedRequestVo();
        List<PMISurveyedRequestDataVo> PMISurveyedRequestDataVosList = new ArrayList<>();
        List<LoginDataResponse> dataResponseList = new PMIArcGISDatabase(c).getUserMasterData();
        PMISurveyedRequestVo.setUSER_ID(userName);
        PMISurveyedRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(c));
        PMISurveyedRequestVo.setSESSION_ID(sessionId);
        PMISurveyedRequestVo.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
        PMISurveyedRequestVo.setIS_SURVEY_COM("N");
        PMISurveyedRequestVo.setFEEDER_ID(PMIPoleSurveyActivity.FEEDER_ID);


        PMISurveyedRequestDataVo requestDataVo = new PMISurveyedRequestDataVo();
        requestDataVo.setFEEDER_ID(CommanUtilits.isChecking(surveyVo.getFeeder_id()));
        requestDataVo.setVOLTAGE_LEVEL(CommanUtilits.isChecking(surveyVo.getVoltage_level()));
           /* requestDataVo.setPOLE_NO(Integer.parseInt(PoleSurveyActivity.pole_no));
            requestDataVo.setFROM_POLE(surveyVo.getFrom_pole());*/
        requestDataVo.setFROM_POLE(Integer.parseInt(PMIPoleSurveyActivity.other_pole));
        requestDataVo.setPOLE_NO(Integer.parseInt(PMIPoleSurveyActivity.pole_no));
        requestDataVo.setPOLE_TYPE(CommanUtilits.isChecking(surveyVo.getPole_type()));
        requestDataVo.setPOLE_HEIGHT(CommanUtilits.isChecking(surveyVo.getPole_height()));
        // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
        requestDataVo.setPOLE_SUPPORT(CommanUtilits.isChecking(surveyVo.getPole_support()));
        requestDataVo.setTAPPING_POINT(CommanUtilits.isChecking(surveyVo.getTapping_point()));
        requestDataVo.setCONDUCTOR_SIZE(CommanUtilits.isChecking(surveyVo.getConductor_size()));
        requestDataVo.setPOLE_PAINT_CODE(CommanUtilits.isChecking(surveyVo.getPole_paintingcode()));
        requestDataVo.setSEC_CODE(CommanUtilits.isChecking(dataResponseList.get(0).getOFFICE_ID()));
        requestDataVo.setCREATED_BY(CommanUtilits.isChecking(userName));
        requestDataVo.setGLAT(surveyVo.getLat());
        requestDataVo.setGLONG(surveyVo.getLan());
        requestDataVo.setNO_CKT(CommanUtilits.isChecking(surveyVo.getIsMultiCircuit()));
        requestDataVo.setCKT_TYPE(CommanUtilits.isChecking(surveyVo.getCircuitType()));
        requestDataVo.setIME_NO(CommanUtilits.isChecking(CommanUtilits.getIMEI_NO(c)));
        requestDataVo.setPOLE_CONFIG(CommanUtilits.isChecking(surveyVo.getPole_configuration()));
        requestDataVo.setPIN_CUT(CommanUtilits.isChecking(surveyVo.getPinpoint()));
        requestDataVo.setSS_NAME(CommanUtilits.isChecking(surveyVo.getSs_name()));
        requestDataVo.setPOLE_STRUCTURE(CommanUtilits.isChecking(surveyVo.getPoleStructure()));
        requestDataVo.setDIFF_VOLTAGES(CommanUtilits.isChecking(surveyVo.getDiffVoltage()));


        if (surveyVo.getIsEndPole() != null) {
            requestDataVo.setEND_POLE(CommanUtilits.isChecking(surveyVo.getIsEndPole()));
        } else {
            requestDataVo.setEND_POLE("");
        }
        // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
        requestDataVo.setLINE_TYPE(CommanUtilits.isChecking(surveyVo.getLineType()));
        requestDataVo.setCKT_NO(CommanUtilits.isChecking(surveyVo.getNoofCircuits()));
        requestDataVo.setSS_ID(CommanUtilits.isChecking(surveyVo.getSsId()));
        requestDataVo.setSTJOINT_RMU(CommanUtilits.isChecking(surveyVo.getStjoint_rmu()));

        requestDataVo.setRMU_TYPE(CommanUtilits.isChecking(surveyVo.getRmu_type()));
        requestDataVo.setRMU_MAKE(CommanUtilits.isChecking(surveyVo.getRMU_MAKE()));
        requestDataVo.setRMU_SLNO(CommanUtilits.isChecking(surveyVo.getRMU_SL_NO()));


        requestDataVo.setTAPPING_POINTS(CommanUtilits.isChecking(surveyVo.getNoofTappings()));
        requestDataVo.setTAPPING_LOCNAME(CommanUtilits.isChecking(surveyVo.getTappingLocation()));
        requestDataVo.setREMARKS(CommanUtilits.isChecking(surveyVo.getRemarks()));

        //IS DTR AVAILABLE
        requestDataVo.setDTR_AVAILABLE(CommanUtilits.isChecking(surveyVo.getDTR_AVAILABLE()));
        //New fields added
        requestDataVo.setDTR_PHASE(CommanUtilits.isChecking(surveyVo.getDtr_phase()));
        requestDataVo.setDTR_CAPACITY(CommanUtilits.isChecking(surveyVo.getDtr_capacity()));
        requestDataVo.setDTR_CODE(CommanUtilits.isChecking(surveyVo.getDtr_code()));
        requestDataVo.setDTR_STRUCTURE(CommanUtilits.isChecking(surveyVo.getDtr_structure()));
        requestDataVo.setDTR_MAKE(CommanUtilits.isChecking(surveyVo.getDtr_make()));
        requestDataVo.setDTR_AB(CommanUtilits.isChecking(surveyVo.getDtr_ab_switch()));
        requestDataVo.setDTR_LOC_NAME(CommanUtilits.isChecking(surveyVo.getDtr_location_name()));
        requestDataVo.setDTR_SNO(CommanUtilits.isChecking(surveyVo.getDtr_scno()));
        requestDataVo.setNO_LT_CKT(CommanUtilits.isChecking(surveyVo.getDtr_no_of_circuit()));
        requestDataVo.setHT_SCNO(CommanUtilits.isChecking(surveyVo.getHt_service_number()));
        requestDataVo.setCAPACITY_RATING(CommanUtilits.isChecking(surveyVo.getPc_line_capacitor()));
        requestDataVo.setRECLOSER_TYPE(CommanUtilits.isChecking(surveyVo.getPc_auto_reclosure()));

        requestDataVo.setTYPE_OF_LOAD(CommanUtilits.isChecking(surveyVo.getType_of_load()));
        requestDataVo.setDTR_LOCATION_TYPE(CommanUtilits.isChecking(surveyVo.getDtr_location_type()));
        requestDataVo.setIS_LTCKT_AVAILABLE(CommanUtilits.isChecking(surveyVo.getIs_ltckt_avalable()));
        requestDataVo.setNO_OF_AB_SWITCHES(CommanUtilits.isChecking(surveyVo.getNo_of_ab_switches()));
        requestDataVo.setWO_ID(surveyVo.getWoId());
        requestDataVo.setIS_FEEDER_TYPE(surveyVo.getIsFeederType());


        //NEW
        requestDataVo.setDEFFECTS_AVAILABLE(surveyVo.getDEFFECTS_AVAILABLE());
        requestDataVo.setPOLE_DEFECTS(surveyVo.getPOLE_DEFECTS());
        requestDataVo.setCONDUCTOR_DEFECTS(surveyVo.getCONDUCTOR_DEFECTS());
        requestDataVo.setAB_SWITCH_DEFECTS(surveyVo.getAB_SWITCH_DEFECTS());
        requestDataVo.setINSULATOR_DEFECTS(surveyVo.getINSULATOR_DEFECTS());
        requestDataVo.setCROSSARM_DEFECTS(surveyVo.getCROSSARM_DEFECTS());
        requestDataVo.setTOPCLEAT_DEFECTS(surveyVo.getTOPCLEAT_DEFECTS());
        requestDataVo.setSTRUT_DEFECTS(surveyVo.getSTRUT_DEFECTS());
        requestDataVo.setSTRAY_DEFECTS(surveyVo.getSTRAY_DEFECTS());
        requestDataVo.setHTLT_CLEARANCE(surveyVo.getHTLT_CLEARANCE());
        requestDataVo.setINTERMEDIATE_POLE(surveyVo.getINTERMEDIATE_POLE());
        requestDataVo.setRESTRINGING(surveyVo.getRESTRINGING());
        requestDataVo.setTREE_CLEARANCE(surveyVo.getTREE_CLEARANCE());
        requestDataVo.setOTHER_DEFFECTS(surveyVo.getOTHER_DEFFECTS());


        //MULTI CKT AND OTHER CABLES
        if (surveyVo.getLineType().equalsIgnoreCase("OverHead")) {


            //CROSSINGS
            requestDataVo.setCROSSING_TYPE(surveyVo.getCROSSING_TYPE());
            requestDataVo.setCROSS_6_3KV_ID(surveyVo.getCROSS_6_3KV_ID());
            requestDataVo.setCROSS_11KV_ID(surveyVo.getCROSS_11KV_ID());
            requestDataVo.setCROSS_33KV_ID(surveyVo.getCROSS_33KV_ID());
            requestDataVo.setCROSS_LT_CKT(surveyVo.getCROSS_LT_CKT());
            requestDataVo.setCROSS_COMMUN(surveyVo.getCROSS_COMMUN());
            requestDataVo.setCROSS_EHT(surveyVo.getCROSS_EHT());
            requestDataVo.setCROSS_REMARKS(surveyVo.getCROSS_REMARKS());

            //OTHER CABLES
            if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                requestDataVo.setNUM_OTHER_CABLES(edt_oh_other_cables.getText().toString());
            } else {
                requestDataVo.setNUM_OTHER_CABLES("");
            }

            if (surveyVo.getIsMultiCircuit().equalsIgnoreCase("Yes")) {

                requestDataVo.setNO_33KV("" + noof_33kvCkts);
                requestDataVo.setNO_11KV("" + noof_11kvCkts);
                requestDataVo.setNO_LT("" + noof_ltCkts);

                ArrayList<PMIMultiRequestVO> mc_list = new ArrayList<>();
                if (!surveyVo.getMC_33KV_FEEDER_ID_1().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("33KV");
                    String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_1();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_1());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_1());
                    mc_list.add(vo);
                }
                if (!surveyVo.getMC_33KV_FEEDER_ID_2().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("33KV");
                    String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_2();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_2());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_2());
                    mc_list.add(vo);
                }
                if (!surveyVo.getMC_33KV_FEEDER_ID_3().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("33KV");
                    String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_3();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_3());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_3());
                    mc_list.add(vo);
                }

                if (!surveyVo.getMC_11KV_FEEDER_ID_1().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("11KV");
                    String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_1();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_1());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_1());
                    mc_list.add(vo);
                }
                if (!surveyVo.getMC_11KV_FEEDER_ID_2().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("11KV");
                    String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_2();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_2());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_2());
                    mc_list.add(vo);
                }
                if (!surveyVo.getMC_11KV_FEEDER_ID_3().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("11KV");
                    String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_2();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_3());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_3());
                    mc_list.add(vo);
                }

                if (!surveyVo.getMC_LT_FEEDER_ID_1().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("LT");
                    String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_1();
                       /* String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_1());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_1());
                    mc_list.add(vo);
                }
                if (!surveyVo.getMC_LT_FEEDER_ID_2().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("LT");
                    String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_2();
                        /*String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_2());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_2());
                    mc_list.add(vo);
                }
                if (!surveyVo.getMC_LT_FEEDER_ID_3().equals("N")) {
                    PMIMultiRequestVO vo = new PMIMultiRequestVO();
                    vo.setVOLTAGE_LEVEL("LT");
                    String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_2();
                       /* String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                    vo.setFEEDER_ID(selectedFeeder);
                    vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_3());
                    vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_3());
                    mc_list.add(vo);
                }

                requestDataVo.setMC(mc_list);

            } else {
                requestDataVo.setNO_33KV("");
                requestDataVo.setNO_11KV("");
                requestDataVo.setNO_LT("");
            }
        } else {
            requestDataVo.setNO_33KV("");
            requestDataVo.setNO_11KV("");
            requestDataVo.setNO_LT("");
        }


        //IMAGES START

       /* requestDataVo.setIMAGE_1(image_1_clicked ? ConvertImage.convertBase64(image_1_base64) : "N");
        requestDataVo.setIMAGE_2(image_2_clicked ? ConvertImage.convertBase64(image_2_base64) : "N");
        requestDataVo.setIMAGE_3(image_3_clicked ? ConvertImage.convertBase64(image_3_base64) : "N");*/

        //IMAGES END

        //JHC PHASE 1
        if (LoginActivity.isJHC) {
            // requestDataVo.setSURVEY_TYPE(surveyVo.getLINE_SURVEY_TYPE());
            // requestDataVo.setPOLE_SURVEY_TYPE(surveyVo.getPOLE_SURVEY_TYPE());

            if (sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                requestDataVo.setPOLE_SURVEY_TYPE("JHC");
            }else {
                requestDataVo.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
            }

            if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                requestDataVo.setPOLE_SURVEY_TYPE("JHC");
            }else {
                requestDataVo.setPOLE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
            }


            if (type.equalsIgnoreCase("OverHead")) {
                //  requestDataVo.setLT_SURVEY_TYPE(surveyVo.getLT_SURVEY_TYPE());
                //  requestDataVo.setDTR_PRO_EXT(surveyVo.getDTR_SURVEY_TYPE());

                if (sp_oh_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                    if (sp_oh_lt_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                        requestDataVo.setLT_SURVEY_TYPE("JHC");
                    }else {
                        requestDataVo.setLT_SURVEY_TYPE(sp_oh_lt_survey_type.getSelectedItem().toString());
                    }

                } else {
                    requestDataVo.setLT_SURVEY_TYPE("");
                }

                if (sp_oh_is_dtr_avalabile.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                    if (sp_oh_dtr_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")){
                        requestDataVo.setDTR_PRO_EXT("JHC");
                    }else {
                        requestDataVo.setDTR_PRO_EXT(sp_oh_dtr_survey_type.getSelectedItem().toString());
                    }

                } else {
                    requestDataVo.setDTR_PRO_EXT("");
                }

            }else {
                requestDataVo.setLT_SURVEY_TYPE("");
                requestDataVo.setDTR_PRO_EXT("");
            }


        } else {
            requestDataVo.setSURVEY_TYPE(surveyVo.getSurvaytype());
            requestDataVo.setLT_SURVEY_TYPE("");
            requestDataVo.setDTR_PRO_EXT("");
        }

        requestDataVo.setPOLE_CONCRETE(surveyVo.getPOLE_CONCRETE());

        requestDataVo.setCURRENT_LAT(surveyVo.getCURRENT_LAT());
        requestDataVo.setCURRENT_LONG(surveyVo.getCURRENT_LONG());

        PMISurveyedRequestDataVosList.add(requestDataVo);


        PMISurveyedRequestVo.setSURVEY_DATA(PMISurveyedRequestDataVosList);

        Gson gson = new Gson();
        String json = gson.toJson(PMISurveyedRequestVo);

        Log.v("DATA===>>", "" + json);
        //  writeToFile(json);
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<PMISuveyedResponseVo> responseVoCall = retrofitService.inlinePole(PMISurveyedRequestVo);
        responseVoCall.enqueue(new Callback<PMISuveyedResponseVo>() {
            @Override
            public void onResponse(Call<PMISuveyedResponseVo> call, Response<PMISuveyedResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    PMISuveyedResponseVo response_vo = response.body();
                    progressDialog.dismiss();
                    if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                        arcGISDatabase.clearSurveyedData();
                        Intent intent = new Intent(c, PMIHomeActivity.class);
                        c.startActivity(intent);
                        c.finish();
                        Toast.makeText(c, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(c, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(c, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMISuveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(c, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private File createImageFile1() throws IOException {
        // Create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create a file for the image
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a path to the file (optional)
        currentPhotoPath_1 = image.getAbsolutePath();
        return image;
    }

    private File createImageFile2() throws IOException {
        // Create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create a file for the image
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a path to the file (optional)
        currentPhotoPath_2 = image.getAbsolutePath();
        return image;
    }
}