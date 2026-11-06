package com.apepdcl.gis_survey.estimates.new_module;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.OfflineHome;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.sou.DtrMeterModel;
import com.apepdcl.gis_survey.estimates.sou.LocationDetails;
import com.apepdcl.gis_survey.estimates.sou.MaterialModel;
import com.apepdcl.gis_survey.estimates.sou.MaterialModelPole;
import com.apepdcl.gis_survey.estimates.sou.SpinnerAdapter;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.CustomDialog;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstimatesMap extends AppCompatActivity {

    ArcGISDatabaseEstimates databaseEstimates;
    ArrayList<NewModel> list33KV;
    ArrayList<NewModel> list11KV;
    ArrayList<NewModel> listLT;
    ArrayList<NewModel> listMeters;
    ArrayList<NewModel> listAll;
    String EST_NO, TYPE, GIS_ID, REG_COUNT, EST_NATURE, PURPOSE, BTN_VALUE, wo_no, EST_TYPE = "", FEEDERS = "", RDSS_TYPE = "";
    RelativeLayout back_layout, current_location_layout;
    MapView mv;
    GpsTraker gpsTraker, currentLocationGpsTracker;
    public static float gpsAccurcy = 0;
    GraphicsOverlay mGraphicsOverlay;
    public static GraphicsOverlay graphicsOverlay;
    ArcGISMap map;
    private Point ptWgs84;
    PointCollection polylinePoints;
    LocationDisplay mLocationDisplay;
    public static Graphic graphic;
    TextView calloutContent, home_heading;
    public static Callout mCallout;
    int called = 0;
    ArrayList<NewModel> statusList;
    private ProgressDialog progressDialog;
    private TextView est_no, work_order, feeder_id, est_nature;

    public static TextView btn_complete_survey;
    Button btn_transmit_data;
    boolean surveyStatus, is33kVStatus;
    boolean isComplted33kVSurvey = false;
    boolean isComplted11kVSurvey = false;
    boolean isCompltedLTSurvey = false;
    boolean isCompltedMetersSurvey = false;
    RetrofitServiceEstimates retrofitService;
    private JsonArray jsonArray;
    private ArcGISDatabaseEstimates arcGISDatabase;

    //SimpleMarkerSymbol.Style style;
    String labelText = "";
    private PurposeModel model;
    ArrayList<NewModel> existingList33KV;

    // PictureMarkerSymbol symbol;
    SimpleMarkerSymbol symbol;
    Bitmap drawableBitmap;
    double lastPoleDistance, currentDistance;
    private List<SurveyDataVo> surveyVoList;
    boolean isFirstPole = false;
    private String surveyType;

    private boolean is33KVSelected = false;
    private boolean is11KVSelected = false;
    private boolean isDTRSelected = false;
    private boolean isLTSelected = false;
    private boolean is33KV11KVSSSelected = false;
    private boolean isMETERSelected = false;
    private boolean isCompletedAllSurvey = false;

    private boolean is33KVSelectedSurvey = false;
    private boolean is11KVSelectedSurvey = false;
    private boolean isDTRSelectedSurvey = false;
    private boolean isLTSelectedSurvey = false;
    private boolean is33kVResurvey = false;
    private String from = "", max_length;
    private int TOWER_LENGTH;
    private int selectedFdrIndex = 0;
    public static String Feeder_type33KV = "", Feeder_type11KV = "";
    public static String selectedExiPoleNo = "";
    public static String selectedExiPoleNoID = "";
    public String btnClick = "";

    public static String TAPPING_POINT;
    public static boolean SPUR_FIRST_POLE = true;
    private int i, count, maxSurveyCount, poleCountCheck;

    String selectedFeederId;
    int lableColor = Color.BLACK;
    int latX, longiY;
    boolean isTappedMap = false;
    private LinearLayout layout_line_survey_type;

    //MATERIAL NEW
    public static ArrayList<MaterialModel> materialList33, materialList11, materialListLT;
    public static ArrayList<MaterialModelPole> materialList11Pole, materialList33Pole;
    public static ArrayList<DtrMeterModel> dtrMeterModels;

    //NEW ADDED BY SOUBHAGYA
    TextView tap_load_data, tap_select;
    ArrayList<NewModel> tempList11KV;
    ArrayList<NewModel> tempList33KV;
    ArrayList<NewModel> tempListLT;
    ArrayList<NewModel> locationFilteredData33kv;
    ArrayList<NewModel> locationFilteredData11kv;
    ArrayList<NewModel> locationFilteredDataLT;
    ArrayList<NewModel> allLocationFilteredDataLT;
    boolean firstTap33 = false;
    boolean secondTap33 = false;
    boolean firstTap11 = false;
    boolean secondTap11 = false;
    boolean firstTapLT = false;

    boolean IS_FEEDER_SURVEY_COMPLETED_1 = false;
    boolean IS_FEEDER_SURVEY_COMPLETED_2 = false;
    boolean IS_FEEDER_SURVEY_COMPLETED_3 = false;
    boolean IS_FEEDER_SURVEY_COMPLETED_4 = false;
    boolean IS_FEEDER_SURVEY_COMPLETED_5 = false;

    public int LOCATION_COUNT_11KV_FEEDER_1 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_2 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_3 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_4 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_5 = 0;

    public int LOCATION_COUNT_11KV_FEEDER_6 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_7 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_8 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_9 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_10 = 0;

    public static boolean IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
    public static boolean IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
    public static boolean IS_CURRENT_LOCATION_OTHER_FEEDER = false;
    public static boolean IS_CURRENT_LOCATION_SELECTED = false;
    RelativeLayout reload_layout, reload_map_layout;

    public static String LINE_EXT_POLE_TYPE = "";
    public static String LINE_EXT_POLE_CONFIG = "";
    public static String LINE_EXT_POLE_HEIGHT = "";
    public static String LINE_EXT_POLE_SUPPORT = "";
    public static String LINE_EXT_PIN_CUT = "";
    public static String LINE_EXT_POLE_STRUCTURE = "";
    public static String LINE_EXT_POLE_CONCRETE = "";
    public static String LINE_EXT_DTR_STRUCTURE_CODE = "";
    public static String LINE_EXT_HT_SERVICE_NO = "";
    public static String LINE_EXT_LT_CIRCUIT_AVAILABLE = "";
    public static String LINE_EXT_LT_CIRCUIT_COUNT = "";
    public static String LINE_EXT_DTR_MAKE = "";
    public static String LINE_EXT_DTR_SLNO = "";
    public static String LINE_EXT_DTR_PHASE = "";
    public static String LINE_EXT_DTR_CAPACITY = "";
    public static String LINE_EXT_DTR_LOC_NAME = "";
    public static String LINE_EXT_DTR_LATTITUDE = "";
    public static String LINE_EXT_DTR_LONGITUDE = "";
    public static String LINE_EXT_DTR_LOCATION_NO = "";
    public static String LINE_EXT_VOLTAGE_LEVEL = "";

    public static boolean IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
    //23-12-24 Chandu Changes
    public static boolean IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;

    //06-1-2025 INTERMEDIATE_POLE
    public static boolean IS_INTERMEDIATE_POLE = false;
    public static String INT_FIRST_POLE, INT_FIRST_POLE_LAT, INT_FIRST_POLE_LON, INT_FIRST_POLE_ID, INT_FIRST_POLE_TYPE, INT_FIRST_POLE_HEIGHT, INT_FIRST_LINE_TYPE, INT_FIRST_POLE_SUPPORT, INT_FIRST_PIN_CUT, INT_FIRST_POLE_CONCREATE, INT_FIRST_POLE_STRUCTURE, INT_FIRST_POLE_CONFIG, INT_FIRST_POLE_DTR_ID = "", INT_FIRST_POLE_DTR_CODE = "", INT_FIRST_DTR_LOCATION = "", INT_FIRST_DTR_AVAILABLE = "", INT_FIRST_DTR_LT_CKT_COUNT = "", INT_FIRST_DTR_MAKE = "", INT_FIRST_DTR_SLNO = "", INT_FIRST_DTR_PHASE = "", INT_FIRST_DTR_CAPACITY = "", INT_FIRST_DTR_SHIFTED = "";
    public static String INT_SECOND_POLE, INT_SECOND_POLE_LAT, INT_SECOND_POLE_LON, INT_SECOND_POLE_ID, INT_SECOND_LINE_TYPE, INT_SECOND_POLE_TYPE, INT_SECOND_POLE_HEIGHT, INT_SECOND_POLE_SUPPORT, INT_SECOND_PIN_CUT, INT_SECOND_POLE_CONCREATE, INT_SECOND_POLE_STRUCTURE, INT_SECOND_POLE_CONFIG, INT_SECOND_POLE_DTR_ID = "", INT_SECOND_POLE_DTR_CODE = "", INT_SECOND_DTR_LOCATION = "", INT_SECOND_DTR_AVAILABLE = "", INT_SECOND_DTR_LT_CKT_COUNT = "", INT_SECOND_DTR_MAKE = "", INT_SECOND_DTR_SLNO = "", INT_SECOND_DTR_PHASE = "", INT_SECOND_DTR_CAPACITY = "", INT_SECOND_DTR_SHIFTED = "";

    public static boolean IS_SAME_EXISTING_FEEDER = false;

    public static boolean LT_POLE_SURVEY_TYPE = false;

    public static String SELECTED_LOCATION_TYPE = "";
    public static String SELECTED_LOCATION_NUMBER = "";


    //LOCATIONS EDIT
    boolean IS_EDIT;
    NewModel DATA_MODEL;
    String selectedSSCode33KV = "";
    String selectedSSCode11KV = "";
    public static String SELECTED_FEEDER_ID = "";

    //NEW CODE FOR 33KV LINE EXTENSION
    public int LOCATION_COUNT_33KV_FEEDER_1 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_2 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_3 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_4 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_5 = 0;

    public static boolean IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33 = false;
    public static boolean IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11 = false;

    //NEW LT CHANGES
    public static boolean IS_CURRENT_LOCATION_INTERMEDIATE_POLE_LT = false;
    public static boolean IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
    public static boolean IS_CURRENT_LOCATION_LINE_EXTENSION_LT = false;

    public static boolean POLE_SURVEY_TYPE_11KV = false;


    public static String LINE_EXT_POLE_FIRST_CONFIG = "";

    ImageView iconMinimize, iconMaximize;
    TextView tv_legend, tv_show_legend, tv_legend_est_info, tv_show_legend_est_info, tv_action_remarks, tv_scrolling;
    LinearLayout legendsLayout, layout_display_est_info, layout_est_feeder_details;

    private static double ONLINE_DISTANCE_LIMIT = 500;
    private static double OFFLINE_DISTANCE_LIMIT = 2000;
    private double existingPoleDataDistance = 0.0;
    private boolean isAlertShown = false;

    private Spinner sp_line_survey_type;

    double MAX_DISTANCE_METERS = 20.0;


    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimates_map);
        getSupportActionBar().hide();

        //NEW LT CHANGES
        locationFilteredDataLT = new ArrayList<>();
        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_LT = false;
        IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
        IS_CURRENT_LOCATION_LINE_EXTENSION_LT = false;

        iconMaximize = findViewById(R.id.icon_maximize);
        iconMinimize = findViewById(R.id.icon_minimize);
        legendsLayout = findViewById(R.id.legends_layout);
        layout_est_feeder_details = findViewById(R.id.layout_est_feeder_details);
        tv_legend = findViewById(R.id.tv_legend);
        tv_show_legend = findViewById(R.id.tv_show_legend);
        tv_legend_est_info = findViewById(R.id.tv_legend_est_info);
        tv_show_legend_est_info = findViewById(R.id.tv_show_legend_est_info);
        layout_display_est_info = findViewById(R.id.layout_display_est_info);
        sp_line_survey_type = findViewById(R.id.sp_line_survey_type);
        layout_line_survey_type = findViewById(R.id.layout_line_survey_type);
        tv_action_remarks = findViewById(R.id.tv_action_remarks);
        tv_scrolling = findViewById(R.id.tv_scrolling);

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            tv_show_legend.setText("Show Solar Details");

        }

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        tv_show_legend.startAnimation(pulse);
        tv_show_legend_est_info.startAnimation(pulse);


//        iconMinimize.setOnClickListener(v -> {
//            layout_display_est_info.setVisibility(View.GONE);
//            iconMinimize.setVisibility(View.GONE);
//            iconMaximize.setVisibility(View.VISIBLE);
//        });


        tv_legend_est_info.setOnClickListener(v -> {
            tv_legend_est_info.clearAnimation();
            tv_show_legend_est_info.startAnimation(pulse);
            layout_display_est_info.setVisibility(View.GONE);
            tv_legend_est_info.setVisibility(View.GONE);
            tv_show_legend_est_info.setVisibility(View.VISIBLE);
        });

        tv_show_legend_est_info.setOnClickListener(v -> {

            tv_show_legend_est_info.clearAnimation();
            tv_legend_est_info.startAnimation(pulse);

            layout_display_est_info.setVisibility(View.VISIBLE);
            tv_legend_est_info.setVisibility(View.VISIBLE);
            tv_show_legend_est_info.setVisibility(View.GONE);
        });


        tv_legend.setOnClickListener(v -> {
            tv_legend.clearAnimation();
            tv_show_legend.startAnimation(pulse);
            tv_legend.setVisibility(View.GONE);
            layout_est_feeder_details.setVisibility(View.GONE);
            tv_show_legend.setVisibility(View.VISIBLE);

        });

        tv_show_legend.setOnClickListener(v -> {
            tv_show_legend.clearAnimation();
            tv_legend.startAnimation(pulse);
            tv_legend.setVisibility(View.VISIBLE);

            layout_est_feeder_details.setVisibility(View.VISIBLE);
            tv_show_legend.setVisibility(View.GONE);
        });

//        iconMaximize.setOnClickListener(v -> {
//            layout_display_est_info.setVisibility(View.VISIBLE);
//            iconMinimize.setVisibility(View.VISIBLE);
//            iconMaximize.setVisibility(View.GONE);
//        });

        //LOCATIONS EDIT
        IS_EDIT = getIntent().getBooleanExtra("IS_EDIT", false);
        if (IS_EDIT) {
            DATA_MODEL = (NewModel) getIntent().getSerializableExtra("DATA_MODEL");
        }


        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11 = false;
        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33 = false;
        IS_INTERMEDIATE_POLE = false;

        IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
        IS_CURRENT_LOCATION_SELECTED = false;
        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
        //23-12-24 Chandu Changes
        IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;

        tap_load_data = findViewById(R.id.tap_load_data);
        tap_select = findViewById(R.id.tap_select);
        reload_layout = findViewById(R.id.reload_layout);
        reload_map_layout = findViewById(R.id.reload_map_layout);
        est_no = findViewById(R.id.est_no);
        work_order = findViewById(R.id.wo_no);
        feeder_id = findViewById(R.id.feeder_id);
        est_nature = findViewById(R.id.est_nature);

        locationFilteredData33kv = new ArrayList<>();
        locationFilteredData11kv = new ArrayList<>();

        //MATERIAL NEW
        materialList33 = new ArrayList<>();
        materialList11 = new ArrayList<>();
        materialListLT = new ArrayList<>();
        materialList11Pole = new ArrayList<>();
        materialList33Pole = new ArrayList<>();
        dtrMeterModels = new ArrayList<>();

        loadMaterialMaster();

        progressDialog = new ProgressDialog(EstimatesMap.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        EST_NO = getIntent().getStringExtra("EST_NO");
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        EST_NATURE = getIntent().getStringExtra("EST_NATURE");
        PURPOSE = getIntent().getStringExtra("PURPOSE");
        wo_no = getIntent().getStringExtra("WORK_ORDER");
        REG_COUNT = getIntent().getStringExtra("REG_COUNT");
        BTN_VALUE = getIntent().getStringExtra("BTN_VALUE");
        from = getIntent().getStringExtra("FROM");
        btnClick = getIntent().getStringExtra("BTN_VALUE");
        model = (PurposeModel) getIntent().getSerializableExtra("Model");

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            FEEDERS = getIntent().getStringExtra("FEEDERS");
            RDSS_TYPE = getIntent().getStringExtra("RDSS_TYPE");
        }


//RECENT CHANGES BY CHANDU
        System.out.println("KNOWING_FROM: " + from);

        if (from != null) {
            if (from.equals("OFFLINE_LT")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(model);
                //    Log.d("OFFLINE_LT_MODEL_MAP", "onCreate: " + json);


            } else {
                selectedFdrIndex = Integer.parseInt(getIntent().getStringExtra("FDR_INDEX_POS"));
            }


        }


        //  System.out.println("SELECT FEEDER INDEX:" + selectedFdrIndex);
        max_length = getIntent().getStringExtra("MAX_LENGTH");

        is33KVSelected = getIntent().getBooleanExtra("33KV", false);
        is11KVSelected = getIntent().getBooleanExtra("11KV", false);
        isDTRSelected = getIntent().getBooleanExtra("DTR", false);
        is33KV11KVSSSelected = getIntent().getBooleanExtra("33/11kvSS", false);
        isMETERSelected = getIntent().getBooleanExtra("METERS", false);
        isLTSelected = getIntent().getBooleanExtra("LT", false);
        Feeder_type33KV = getIntent().getStringExtra("Feeder_type33KV");
        Feeder_type11KV = getIntent().getStringExtra("Feeder_type11KV");


        databaseEstimates = new ArcGISDatabaseEstimates(this);
        back_layout = findViewById(R.id.back_layout);
        current_location_layout = findViewById(R.id.close_layout);
        mv = findViewById(R.id.map1);
        btn_complete_survey = findViewById(R.id.btn_complete_survey);
        btn_transmit_data = findViewById(R.id.btn_transmit_data);
        home_heading = findViewById(R.id.home_heading);
        // System.out.println("FROM " + from);
        //  System.out.println("CLICKED FEEDER " + WorkInvolmentsActivity.CLICKED_FEEDER);


        if (from != null) {
            if (from.equalsIgnoreCase("33kvResurvey")) {
                BTN_VALUE = getIntent().getStringExtra("BTN_VALUE");
                //  btn_complete_survey.setText("Complete 33kV Survey");
                wo_no = getIntent().getStringExtra("WORK_ORDER");
                EST_NATURE = getIntent().getStringExtra("EST_NATURE");
                EST_TYPE = getIntent().getStringExtra("EST_TYPE");

                model = (PurposeModel) getIntent().getSerializableExtra("Model");
                //  Log.d("33kvResurveyMaxLength", "Resurvey: " + max_length);
                isComplted33kVSurvey = databaseEstimates.update33kvSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                //  Log.d("33KVCOMPLETESTATUS", "onCreate: " + isComplted33kVSurvey);
            } else if (from.equalsIgnoreCase("11kvResurvey")) {
                BTN_VALUE = getIntent().getStringExtra("BTN_VALUE");
                wo_no = getIntent().getStringExtra("WORK_ORDER");
                EST_NATURE = getIntent().getStringExtra("EST_NATURE");
                EST_TYPE = getIntent().getStringExtra("EST_TYPE");


                model = (PurposeModel) getIntent().getSerializableExtra("Model");
                isComplted11kVSurvey = databaseEstimates.update11kvSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
            } else if (from.equalsIgnoreCase("LTResurvey")) {
                BTN_VALUE = getIntent().getStringExtra("BTN_VALUE");
                wo_no = getIntent().getStringExtra("WORK_ORDER");
                EST_NATURE = getIntent().getStringExtra("EST_NATURE");
                EST_TYPE = getIntent().getStringExtra("EST_TYPE");
                model = (PurposeModel) getIntent().getSerializableExtra("Model");
                //  btn_complete_survey.setText("Complete LT Survey");
                isComplted11kVSurvey = databaseEstimates.updateLTSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
            } else if (from.equalsIgnoreCase("WI")) {
                EST_TYPE = getIntent().getStringExtra("EST_TYPE");
                BTN_VALUE = getIntent().getStringExtra("BTN_VALUE");
            }
        } else {

        }

        // System.out.println("CURRENT_SURVEY_TYPE : " + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);

        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
            home_heading.setText("33 kV  Survey");
        }
        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
            home_heading.setText("11 kV  Survey");
        }
        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")) {
            home_heading.setText("LT  Survey");
        }

        if (BTN_VALUE.equalsIgnoreCase("LT")) {
            if (!WorkInvolmentsActivity.SELECTED_LT_CKT.isEmpty()) {
                feeder_id.setVisibility(View.VISIBLE);
                feeder_id.setText("Circuit Name :" + " " + WorkInvolmentsActivity.SELECTED_LT_CKT);
            }

        }

        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")) {
            layout_line_survey_type.setVisibility(View.GONE);
        }


        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE != null) {
            System.out.println("CURRENT_SURVEY_TYPE_IS1" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);
            String clickedFeeder = WorkInvolmentsActivity.CLICKED_FEEDER;
            int feederIndex = -1;

            if (clickedFeeder != null && clickedFeeder.startsWith("FEEDER")) {
                try {
                    feederIndex = Integer.parseInt(clickedFeeder.split(" ")[1]);
                } catch (NumberFormatException ignored) {
                }
            }

            if (feederIndex >= 1 && feederIndex <= 5) {
                String feederType = "", feederId = "", feederName = "";

                if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                    System.out.println("CURRENT_SURVEY_TYPE_IS2" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);
                    feederType = getFieldValue("FEEDER_TYPE_11KV_" + feederIndex);
                    feederId = getFieldValue("feeder_id" + feederIndex + "_11kv");
                    feederName = getFieldValue("feeder_name" + feederIndex + "_11kv");
                } else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                    System.out.println("CURRENT_SURVEY_TYPE_IS3" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);
                    feederType = getFieldValue("FEEDER_TYPE_33KV_" + feederIndex);
                    feederId = getFieldValue("feeder_id" + feederIndex + "_33kv");
                    feederName = getFieldValue("feeder_name" + feederIndex + "_33kv");
                }

                System.out.println("FEEDER_TYPE--" + feederType);
                System.out.println("FEEDER_INDEX--" + feederIndex);
                System.out.println("FEEDER_ID--" + feederId);

                if (feederId.contains("-33KV") || feederId.contains("-11KV")) {
                    if (!feederName.isEmpty()) {
                        feeder_id.setVisibility(View.VISIBLE);
                        feeder_id.setText("Feeder Name: " + feederName);

                    }
                } else {
                    if (!feederName.isEmpty()) {
                        feeder_id.setVisibility(View.VISIBLE);
                        feeder_id.setText("Feeder Name: " + feederName + " (" + feederId + ")");

                    }
                }

            }
        }


        work_order.setText("Wo No: " + wo_no);
        est_nature.setText("Est Nature: " + EST_NATURE);

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            work_order.setVisibility(View.GONE);
            est_no.setText("Est No: " + EST_NO);
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            work_order.setVisibility(View.VISIBLE);
            est_no.setVisibility(View.VISIBLE);
            work_order.setText("DTR Phase : " + EstimatesHome.DTR_PHASE);
            est_no.setText("DTR Code: " + EST_NO);
            feeder_id.setText("DTR Name: " + EstimatesHome.DTR_NAME);
            feeder_id.setVisibility(View.VISIBLE);
            est_nature.setVisibility(View.VISIBLE);
            est_nature.setText("DTR Capacity : " + EstimatesHome.DTR_CAPACITY);
            tv_action_remarks.setVisibility(View.VISIBLE);
            tv_action_remarks.setText(EstimatesHome.ACTION_REMARKS);

        } else {
            work_order.setVisibility(View.VISIBLE);
            if (!EST_NO.isEmpty()) {
                est_no.setText("Est No: " + EST_NO);
            } else {
                est_no.setVisibility(View.GONE);
            }
        }

        //14-12-24 Chandra sekhar Changes-
        isComplted33kVSurvey = databaseEstimates.get33kvSurveyStatusNEW(EST_NO, GIS_ID);
        isComplted11kVSurvey = databaseEstimates.get11kvSurveyStatusNEW(EST_NO, GIS_ID);
        isCompltedLTSurvey = databaseEstimates.getLTSurveyStatusNEW(EST_NO, GIS_ID);
        isCompltedMetersSurvey = databaseEstimates.getMetersSurveyStatusNEW(EST_NO, GIS_ID);

        // Log.d("------STATUS----->", "onCreate: " + "33KV" + isComplted33kVSurvey + " " + "11kv" + " " + isComplted11kVSurvey + "LT" + isCompltedLTSurvey + "METERS" + isCompltedMetersSurvey);

        if (isComplted33kVSurvey && isComplted11kVSurvey && isCompltedLTSurvey) {
            isCompletedAllSurvey = true;


        } else {
            isCompletedAllSurvey = false;
        }

        if (is33KVSelected) {
            if (!isComplted33kVSurvey) {
                //  btn_complete_survey.setText("Complete 33kV Survey");

            }
        }
        if (is11KVSelected) {
            if (is33KVSelected && isComplted33kVSurvey) {

            } else {
                if (!is33KVSelected) {

                }
            }
        }

        if (isLTSelected) {
            if (is33KVSelected && !isComplted33kVSurvey) {
                // btn_complete_survey.setText("Complete 33kV Survey");
            } else if (is11KVSelected && !isComplted11kVSurvey) {

            } else if (!isCompltedLTSurvey) {
                //  btn_complete_survey.setText("Complete LT Survey");
            } else if (is33KVSelected && isComplted33kVSurvey && is11KVSelected && isComplted11kVSurvey && isCompltedLTSurvey) {
                //  btn_complete_survey.setText("TRANSMIT DATA");
            }
        }


        if (isLTSelected) {
            if (isCompltedLTSurvey) {
                //home_heading.setText("Completed");

            } else {
                //  home_heading.setText("LT  Survey");

            }
        }

        if (is33KVSelected) {
            if (!isComplted33kVSurvey) {
                // home_heading.setText("33 kV  Survey");
            }
        }
        if (is11KVSelected) {
            if (is33KVSelected && isComplted33kVSurvey) {
                //  home_heading.setText("11 kV  Survey");
            } else {
                if (!is33KVSelected) {
                    //   home_heading.setText("11 kV  Survey");
                }
            }

        }
        if (isLTSelected) {
            if (is33KVSelected && isComplted33kVSurvey && is11KVSelected && isComplted11kVSurvey) {
                //  home_heading.setText("LT  Survey");
            } else if (!isComplted33kVSurvey && is33KVSelected) {
                //  home_heading.setText("33 kV  Survey");

            } else if (!isComplted11kVSurvey && is11KVSelected) {
                //  home_heading.setText("11 kV  Survey");

            } else {
                //  home_heading.setText("LT  Survey");
            }
        }


        statusList = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);
        if (statusList.isEmpty()) {
            if (!databaseEstimates.isSurveyExistsNEW(EST_NO, GIS_ID)) {
                databaseEstimates.addSurveyStatusNew(EST_NO, GIS_ID);
            }
        } else {
            surveyStatus = databaseEstimates.getSurveyStatusNEW(EST_NO, GIS_ID);
            /*if (surveyStatus) {
                btn_transmit_data.setVisibility(View.VISIBLE);
                btn_complete_survey.setVisibility(View.GONE);
            } else {
                btn_transmit_data.setVisibility(View.GONE);
               btn_complete_survey.setVisibility(View.VISIBLE);
            }*/
        }


        btn_complete_survey.setOnClickListener(v -> {
        /*    if (btn_complete_survey.getText().toString().equalsIgnoreCase("Complete 33kV Survey")) {
                isComplted33kVSurvey = true;
                open33kvCompleteSurveyDialog();
            }*/

            //NEW CODE FOR 33KV LINE EXTENSION START
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                openFeederCompleteSurveyDialog33("FEEDER 1");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                openFeederCompleteSurveyDialog33("FEEDER 2");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                openFeederCompleteSurveyDialog33("FEEDER 3");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                openFeederCompleteSurveyDialog33("FEEDER 4");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                isComplted33kVSurvey = true;
                open33kvCompleteSurveyDialog();

            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                openFeederCompleteSurveyDialog("FEEDER 1");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                openFeederCompleteSurveyDialog("FEEDER 2");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                openFeederCompleteSurveyDialog("FEEDER 3");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                openFeederCompleteSurveyDialog("FEEDER 4");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                isComplted11kVSurvey = true;
                openFeederCompleteSurveyDialog("FEEDER 5");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 6") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                isComplted11kVSurvey = true;
                openFeederCompleteSurveyDialog("FEEDER 6");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 7") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                isComplted11kVSurvey = true;
                openFeederCompleteSurveyDialog("FEEDER 7");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 8") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                isComplted11kVSurvey = true;
                openFeederCompleteSurveyDialog("FEEDER 8");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 9") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                isComplted11kVSurvey = true;
                openFeederCompleteSurveyDialog("FEEDER 9");
            } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 10") && WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                isComplted11kVSurvey = true;
                openFeederCompleteSurveyDialog("FEEDER 10");
            } else if (btn_complete_survey.getText().toString().equalsIgnoreCase("Complete LT Survey")) {
                isCompltedLTSurvey = true;
                openLTCompleteSurveyDialog();
            }
//            else if (btn_complete_survey.getText().toString().equalsIgnoreCase("Complete Meters Survey")) {
//                isCompltedMetersSurvey = true;
//                openMeterCompleteSurveyDialog();
//            }


            else if (btn_complete_survey.getText().toString().equalsIgnoreCase("TRANSMIT DATA")) {
                transmitImageData();

            }
            //openCompleteSurveyDialog();
        });

        btn_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransmitSurveyDialog();
            }
        });


        gpsTraker = new GpsTraker(this);
        mGraphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(mGraphicsOverlay);

        gpsTraker.getLocation();
        map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
        mv.setMap(map);
        mLocationDisplay = mv.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }


            String message = String.format("Error in DataSourceStatusChangedListener: %s",
                    dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
            Toast.makeText(EstimatesMap.this, message, Toast.LENGTH_LONG).show();

        });
        // mLocationDisplay.stop();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        current_location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentLocation();
            }
        });

        reload_map_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                    gpsTraker.getLocation();

                    // Just update the viewpoint, do not create a new map
                    Point newLocation = new Point(
                            gpsTraker.getLongitude(),
                            gpsTraker.getLatitude(),
                            SpatialReferences.getWgs84()
                    );
                    mv.setViewpointAsync(new Viewpoint(newLocation, 1000)); // 1000 is scale
                } else {
                    CommanUtilits.internetDialog(EstimatesMap.this);
                }
            }
        });


        reload_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NEW CODE FOR OFFLINE
                // clearMapGraphics();
                if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                    loadExistingPoles33();

                }
                if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                    loadExistingPoles11();
                }
                if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT") && WorkInvolmentsActivity.IS_LT_FIRST_POLE) {
                    loadExistingPolesLT();
                }
            }
        });

        tempList33KV = new ArrayList<>();
        tempList11KV = new ArrayList<>();
        tempListLT = new ArrayList<>();

        tempList33KV = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
        tempList11KV = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);
        tempListLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);

        //NEW CODE FOR 33KV LINE EXTENSION START


        if (tempList33KV != null) {
            for (int i = 0; i < tempList33KV.size(); i++) {
                if (tempList33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_1++;


                }
                if (tempList33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_2++;

                }
                if (tempList33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_3++;

                }
                if (tempList33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_4++;

                }
                if (tempList33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_5++;

                }
            }
        } else {
            loadExistingPoles33();
        }


        //NEW CODE FOR 33KV LINE EXTENSION END


        for (int i = 0; i < tempList11KV.size(); i++) {
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_1++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_2++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_3++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_4++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_5++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id6_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_6++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id7_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_7++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id8_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_8++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id9_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_9++;

            }
            if (tempList11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id10_11kv)) {
                LOCATION_COUNT_11KV_FEEDER_10++;

            }
        }


        //NEW CODE FOR 33KV LINE EXTENSION
        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_1) {
                    if (LOCATION_COUNT_33KV_FEEDER_1 == 0) {
                        System.out.println("STEP2-------->" + LOCATION_COUNT_33KV_FEEDER_1);
                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles33();
                        reload_layout.setVisibility(View.VISIBLE);
                    }
                    System.out.println("STEP3-------->" + LOCATION_COUNT_33KV_FEEDER_1);
                }

            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_2) {
                    if (LOCATION_COUNT_33KV_FEEDER_2 == 0) {
                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles33();
                        reload_layout.setVisibility(View.VISIBLE);

                    }
                }

            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_3) {
                    if (LOCATION_COUNT_33KV_FEEDER_3 == 0) {

                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles33();
                        reload_layout.setVisibility(View.VISIBLE);

                    }
                }

            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_4) {
                    if (LOCATION_COUNT_33KV_FEEDER_4 == 0) {

                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles33();
                        reload_layout.setVisibility(View.VISIBLE);

                    }
                }

            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_5) {
                    if (LOCATION_COUNT_33KV_FEEDER_5 == 0) {

                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles33();
                        reload_layout.setVisibility(View.VISIBLE);
                    }
                }

            }
        }


        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_1) {
                    if (LOCATION_COUNT_11KV_FEEDER_1 == 0) {
                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);
                        /*if (firstTap11) {
                            if (secondTap11) {
                                tap_load_data.setVisibility(View.GONE);
                                tap_select.setVisibility(View.GONE);
                            } else {
                                tap_select.setVisibility(View.VISIBLE);
                                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                anim.setDuration(1000); //You can manage the blinking time with this parameter
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                tap_select.startAnimation(anim);
                            }
                        } else {
                            tap_load_data.setVisibility(View.VISIBLE);
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(1000); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            tap_load_data.startAnimation(anim);
                        }*/
                    }
                }

            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_2) {
                    if (LOCATION_COUNT_11KV_FEEDER_2 == 0) {

                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);

                       /* if (firstTap11) {
                            if (secondTap11) {
                                tap_load_data.setVisibility(View.GONE);
                                tap_select.setVisibility(View.GONE);
                            } else {
                                tap_select.setVisibility(View.VISIBLE);
                                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                anim.setDuration(1000); //You can manage the blinking time with this parameter
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                tap_select.startAnimation(anim);
                            }
                        } else {
                            tap_load_data.setVisibility(View.VISIBLE);
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(1000); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            tap_load_data.startAnimation(anim);
                        }*/
                    }
                }

            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_3) {
                    if (LOCATION_COUNT_11KV_FEEDER_3 == 0) {

                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);


                       /* if (firstTap11) {
                            if (secondTap11) {
                                tap_load_data.setVisibility(View.GONE);
                                tap_select.setVisibility(View.GONE);
                            } else {
                                tap_select.setVisibility(View.VISIBLE);
                                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                anim.setDuration(1000); //You can manage the blinking time with this parameter
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                tap_select.startAnimation(anim);
                            }
                        } else {
                            tap_load_data.setVisibility(View.VISIBLE);
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(1000); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            tap_load_data.startAnimation(anim);
                        }*/
                    }
                }

            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_4) {
                    if (LOCATION_COUNT_11KV_FEEDER_4 == 0) {

                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);

                        /*if (firstTap11) {
                            if (secondTap11) {
                                tap_load_data.setVisibility(View.GONE);
                                tap_select.setVisibility(View.GONE);
                            } else {
                                tap_select.setVisibility(View.VISIBLE);
                                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                anim.setDuration(1000); //You can manage the blinking time with this parameter
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                tap_select.startAnimation(anim);
                            }
                        } else {
                            tap_load_data.setVisibility(View.VISIBLE);
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(1000); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            tap_load_data.startAnimation(anim);
                        }*/
                    }
                }

            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_5) {
                    if (LOCATION_COUNT_11KV_FEEDER_5 == 0) {

                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);

                        /*if (firstTap11) {
                            if (secondTap11) {
                                tap_load_data.setVisibility(View.GONE);
                                tap_select.setVisibility(View.GONE);
                            } else {
                                tap_select.setVisibility(View.VISIBLE);
                                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                anim.setDuration(1000); //You can manage the blinking time with this parameter
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                tap_select.startAnimation(anim);
                            }
                        } else {
                            tap_load_data.setVisibility(View.VISIBLE);
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(1000); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            tap_load_data.startAnimation(anim);
                        }*/
                    }
                }

            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 6")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_6) {
                    if (LOCATION_COUNT_11KV_FEEDER_6 == 0) {
                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);
            /*if (firstTap11) {
                if (secondTap11) {
                    tap_load_data.setVisibility(View.GONE);
                    tap_select.setVisibility(View.GONE);
                } else {
                    tap_select.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    tap_select.startAnimation(anim);
                }
            } else {
                tap_load_data.setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                tap_load_data.startAnimation(anim);
            }*/
                    }
                }
            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 7")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_7) {
                    if (LOCATION_COUNT_11KV_FEEDER_7 == 0) {
                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);
            /*if (firstTap11) {
                if (secondTap11) {
                    tap_load_data.setVisibility(View.GONE);
                    tap_select.setVisibility(View.GONE);
                } else {
                    tap_select.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    tap_select.startAnimation(anim);
                }
            } else {
                tap_load_data.setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                tap_load_data.startAnimation(anim);
            }*/
                    }
                }
            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 8")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_8) {
                    if (LOCATION_COUNT_11KV_FEEDER_8 == 0) {
                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);
            /*if (firstTap11) {
                if (secondTap11) {
                    tap_load_data.setVisibility(View.GONE);
                    tap_select.setVisibility(View.GONE);
                } else {
                    tap_select.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    tap_select.startAnimation(anim);
                }
            } else {
                tap_load_data.setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                tap_load_data.startAnimation(anim);
            }*/
                    }
                }
            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 9")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_9) {
                    if (LOCATION_COUNT_11KV_FEEDER_9 == 0) {
                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);
            /*if (firstTap11) {
                if (secondTap11) {
                    tap_load_data.setVisibility(View.GONE);
                    tap_select.setVisibility(View.GONE);
                } else {
                    tap_select.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    tap_select.startAnimation(anim);
                }
            } else {
                tap_load_data.setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                tap_load_data.startAnimation(anim);
            }*/
                    }
                }
            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 10")) {
                if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_10) {
                    if (LOCATION_COUNT_11KV_FEEDER_10 == 0) {
                        tap_load_data.setVisibility(View.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(1000); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        tap_load_data.startAnimation(anim);
                        loadExistingPoles11();
                        reload_layout.setVisibility(View.VISIBLE);
            /*if (firstTap11) {
                if (secondTap11) {
                    tap_load_data.setVisibility(View.GONE);
                    tap_select.setVisibility(View.GONE);
                } else {
                    tap_select.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    tap_select.startAnimation(anim);
                }
            } else {
                tap_load_data.setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                tap_load_data.startAnimation(anim);
            }*/
                    }
                }
            }
        }


        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT") && WorkInvolmentsActivity.IS_LT_FIRST_POLE && WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY) {
            if (WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY) {
                reload_layout.setVisibility(View.VISIBLE);
            } else {
                reload_layout.setVisibility(View.GONE);
            }
            //loadExistingPolesLT();
        }


        mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                if (isMapActivityDestroyed || mv == null || mv.getMap() == null) {
                    return true;
                }

                double latitude = 0.0;
                double longitude = 0.0;

                Location currentLocation = gpsTraker != null ? gpsTraker.getLocation() : null;
                if (currentLocation != null) {
                    gpsAccurcy = currentLocation.getAccuracy();
                }

                // Convert screen touch point to map point.
                android.graphics.Point screenPoint = new android.graphics.Point(
                        Math.round(e.getX()),
                        Math.round(e.getY())
                );

                Point mapPoint = mv.screenToLocation(screenPoint);
                if (mapPoint == null) {
                    return super.onSingleTapConfirmed(e);
                }

                try {
                    Point wgs84Point = (Point) GeometryEngine.project(
                            mapPoint,
                            SpatialReferences.getWgs84()
                    );

                    if (wgs84Point == null || Double.isNaN(wgs84Point.getX()) ||
                            Double.isNaN(wgs84Point.getY()) ||
                            Double.isInfinite(wgs84Point.getX()) ||
                            Double.isInfinite(wgs84Point.getY())) {
                        return super.onSingleTapConfirmed(e);
                    }

                    latitude = wgs84Point.getY();
                    longitude = wgs84Point.getX();
                } catch (Exception ex) {
                    Log.e("EstimatesMap", "Unable to convert tapped map point", ex);
                    return super.onSingleTapConfirmed(e);
                }
                if (IS_EDIT) {
                    openSurveyScreen(e, mMapView);
                    return true;
                }

                if (secondTap33) {
                    tap_select.setVisibility(View.GONE);

                }


                latX = (int) e.getX();
                longiY = (int) e.getY();
                list33KV = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
                list11KV = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);


                //NEW CODE FOR 33KV LINE EXTENSION START

                if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                    //NEW INTERMEDIATE POLE LOGIC
                    // Find minimum distance
                    double finalDistance = Double.MAX_VALUE;

                    for (int j = 0; j < locationFilteredData33kv.size(); j++) {

                        String poleLat = locationFilteredData33kv.get(j).getLATITUDE();
                        String poleLon = locationFilteredData33kv.get(j).getLONGITUDE();

                        double distance = getDistanceBetweenPoles33New(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );

                        if (distance < finalDistance) {
                            finalDistance = distance;
                        }
                    }
                    //FEEDER 1
                    if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_1) {
                            if (LOCATION_COUNT_33KV_FEEDER_1 == 0) {
                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                    } else {
                                        //openSurveyScreen(e, mMapView);
                                        if (IS_CURRENT_LOCATION_SELECTED) {
                                            if (IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
                                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                            } else {
                                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                                openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                            }

                                        } else {
                                            openSurveyLocationDialog("33KV", e, mMapView, "Existing");

                                            if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33) {
                                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);

                                            } else {

                                                //openExistingSurveyDialog11(e, mMapView);
                                                //New Alert Dialog Working in Progress

                                                openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                            }
                                        }
                                    }

                                } else {
                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                        //openProposedIntermediateDialog33(mMapView, e,latitude, longitude);
                                    } else {

                                        //New Alert Dialog Working in Progress
                                        //openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                    }

                                }
                            }
                        } else {
                            //NEW CODE CHANGES BY SOUBHAGYA ON 24-06-2025
                            if (LOCATION_COUNT_33KV_FEEDER_1 == 0) {
                                // showDialogWithSpinners33KV(mMapView, e);
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                // openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog_33KV_PROPOSED(e, mMapView);
                            }

                            // openSurveyScreen(e, mMapView);
                        }
                    }

                    //FEEDER 2
                    if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_2) {
                            if (LOCATION_COUNT_33KV_FEEDER_2 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                    } else {
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                        // openProposedIntermediateDialog33(mMapView, e);
                                    } else {
                                        //New Alert Dialog Working in Progress
                                        //  openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        //openExistingSurveyDialog(e, mMapView);
                                        openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                    }
                                }
                            }
                        } else {
                            //NEW CODE CHANGES BY SOUBHAGYA ON 24-06-2025
                            if (LOCATION_COUNT_33KV_FEEDER_2 == 0) {
                                // showDialogWithSpinners33KV(mMapView, e);
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                //openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog_33KV_PROPOSED(e, mMapView);
                            }
                            // openExistingSurveyDialog_33KV_PROPOSED(e, mMapView);
                            // openSurveyScreen(e, mMapView);
                        }
                    }

                    //FEEDER 3
                    if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_3) {
                            if (LOCATION_COUNT_33KV_FEEDER_3 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        //openSurveyScreen(e, mMapView);
                                        if (IS_CURRENT_LOCATION_SELECTED) {
                                            if (IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
                                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                            } else {
                                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                                openSurveyScreen(e, mMapView);
                                            }

                                        } else {
                                            //  openExistingSurveyDialog11(e, mMapView);

                                            if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33) {
                                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);

                                            } else {

                                                //openExistingSurveyDialog11(e, mMapView);
                                                //New Alert Dialog Working in Progress

                                                openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                            }
                                        }
                                    }

                                } else {
                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        // openProposedIntermediateDialog33(mMapView, e);
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                    } else {

                                        //New Alert Dialog Working in Progress
                                        //   openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openExistingSurveyDialog(e, mMapView);
                                    }
                                }
                            }
                        } else {
                            //NEW CODE CHANGES BY SOUBHAGYA ON 24-06-2025
                            if (LOCATION_COUNT_33KV_FEEDER_3 == 0) {
                                // showDialogWithSpinners33KV(mMapView, e);
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                // openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                            }
                            //  openExistingSurveyDialog_33KV_PROPOSED(e, mMapView);
                            // openSurveyScreen(e, mMapView);
                        }
                    }

                    //FEEDER 4
                    if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_4) {
                            if (LOCATION_COUNT_33KV_FEEDER_4 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                        // openProposedIntermediateDialog33(mMapView, e);
                                    } else {
                                        //New Alert Dialog Working in Progress
                                        //openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                                    }
                                }
                            }
                        } else {
                            //NEW CODE CHANGES BY SOUBHAGYA ON 24-06-2025
                            if (LOCATION_COUNT_33KV_FEEDER_4 == 0) {
                                // showDialogWithSpinners33KV(mMapView, e);
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                //   openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                            }

                            // openExistingSurveyDialog_33KV_PROPOSED(e, mMapView);
                            // openSurveyScreen(e, mMapView);
                        }
                    }

                    //FEEDER 5
                    if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_5) {
                            if (LOCATION_COUNT_33KV_FEEDER_5 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_33) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);

                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        //  openProposedIntermediateDialog33(mMapView, e);
                                        checkIntermediatePoleAndProceed33kv(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        //New Alert Dialog Working in Progress
                                        //openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyLocationDialog("33KV", e, mMapView, "Existing");
                                    }
                                }
                            }
                        } else {

                            //NEW CODE CHANGES BY SOUBHAGYA ON 24-06-2025
                            if (LOCATION_COUNT_33KV_FEEDER_5 == 0) {
                                // showDialogWithSpinners33KV(mMapView, e);
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                //openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyLocationDialog("33KV", e, mMapView, "Proposed");
                            }
                            // openExistingSurveyDialog_33KV_PROPOSED(e, mMapView);
                            //  openSurveyScreen(e, mMapView);
                        }
                    }

                }

                //NEW CODE FOR 33KV LINE EXTENSION END


                else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                    //NEW INTERMEDIATE POLE LOGIC
                    double finalDistance = Double.MAX_VALUE;


                    for (int j = 0; j < locationFilteredData11kv.size(); j++) {

                        String poleLat = locationFilteredData11kv.get(j).getLATITUDE();
                        String poleLon = locationFilteredData11kv.get(j).getLONGITUDE();

                        double distance = getDistanceBetweenPoles33New(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );

                        if (distance < finalDistance) {
                            finalDistance = distance;
                        }
                    }
                    if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_1) {
                            if (LOCATION_COUNT_11KV_FEEDER_1 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);

                                /*if (firstTap11 && !secondTap11) {
                                    showDialogWithSpinners11KV(mMapView, e);
                                } else {
                                    loadExistingPoles11();
                                }*/
                            } else {
                                // openSurveyScreen(e, mMapView);

                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    //  openExistingSurveyDialog11(e, mMapView);

                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);

                                    } else {

                                        //openExistingSurveyDialog11(e, mMapView);
                                        //New Alert Dialog Working in Progress

                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                    }
                                }

                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_1 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }


                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_2) {
                            if (LOCATION_COUNT_11KV_FEEDER_2 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                /*if (firstTap11 && !secondTap11) {
                                    showDialogWithSpinners11KV(mMapView, e);
                                } else {
                                    loadExistingPoles11();
                                }*/
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    // openExistingSurveyDialog11(e, mMapView);
                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                        // openExistingSurveyDialog11(e, mMapView);
                                    }
                                }
                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_2 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                // openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_3) {
                            if (LOCATION_COUNT_11KV_FEEDER_3 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                /*if (firstTap11 && !secondTap11) {
                                    showDialogWithSpinners11KV(mMapView, e);
                                } else {
                                    loadExistingPoles11();
                                }*/
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    // openExistingSurveyDialog11(e, mMapView);
                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                        //  openExistingSurveyDialog11(e, mMapView);
                                    }
                                }
                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_3 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //  openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_4) {
                            if (LOCATION_COUNT_11KV_FEEDER_4 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                /*if (firstTap11 && !secondTap11) {
                                    showDialogWithSpinners11KV(mMapView, e);
                                } else {
                                    loadExistingPoles11();
                                }*/
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    // openExistingSurveyDialog11(e, mMapView);

                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                        //openExistingSurveyDialog11(e, mMapView);
                                    }
                                }
                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_4 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_5) {
                            if (LOCATION_COUNT_11KV_FEEDER_5 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                /*if (firstTap11 && !secondTap11) {
                                    showDialogWithSpinners11KV(mMapView, e);
                                } else {
                                    loadExistingPoles11();
                                }*/
                            } else {
                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    //  openExistingSurveyDialog11(e, mMapView);

                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                        //openExistingSurveyDialog11(e, mMapView);
                                    }
                                }
                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_5 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 6")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_6) {
                            if (LOCATION_COUNT_11KV_FEEDER_6 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);

                            } else {
                                // openSurveyScreen(e, mMapView);

                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    //  openExistingSurveyDialog11(e, mMapView);

                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {

                                        //openExistingSurveyDialog11(e, mMapView);
                                        //New Alert Dialog Working in Progress

                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                    }
                                }

                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_6 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 7")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_7) {
                            if (LOCATION_COUNT_11KV_FEEDER_7 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
            /*if (firstTap11 && !secondTap11) {
                showDialogWithSpinners11KV(mMapView, e);
            } else {
                loadExistingPoles11();
            }*/
                            } else {
                                // openSurveyScreen(e, mMapView);

                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    //  openExistingSurveyDialog11(e, mMapView);

                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {

                                        //openExistingSurveyDialog11(e, mMapView);
                                        //New Alert Dialog Working in Progress

                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                    }
                                }

                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_7 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 8")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_8) {
                            if (LOCATION_COUNT_11KV_FEEDER_8 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
            /*if (firstTap11 && !secondTap11) {
                showDialogWithSpinners11KV(mMapView, e);
            } else {
                loadExistingPoles11();
            }*/
                            } else {
                                // openSurveyScreen(e, mMapView);

                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    //  openExistingSurveyDialog11(e, mMapView);

                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {

                                        //openExistingSurveyDialog11(e, mMapView);
                                        //New Alert Dialog Working in Progress

                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                    }
                                }

                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_8 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 9")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_9) {
                            if (LOCATION_COUNT_11KV_FEEDER_9 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                            } else {
                                // openSurveyScreen(e, mMapView);

                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    //  openExistingSurveyDialog11(e, mMapView);

                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {

                                        //openExistingSurveyDialog11(e, mMapView);
                                        //New Alert Dialog Working in Progress

                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                    }
                                }

                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_9 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    } else if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 10")) {
                        if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_10) {
                            if (LOCATION_COUNT_11KV_FEEDER_10 == 0) {
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                            } else {
                                // openSurveyScreen(e, mMapView);

                                if (IS_CURRENT_LOCATION_SELECTED) {
                                    if (IS_CURRENT_LOCATION_LINE_EXTENSION_11) {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {
                                        EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                        openSurveyScreen(e, mMapView);
                                    }

                                } else {
                                    //  openExistingSurveyDialog11(e, mMapView);

                                    if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11) {
                                        checkIntermediatePoleAndProceed(finalDistance, mMapView, e, latitude, longitude);
                                    } else {

                                        //openExistingSurveyDialog11(e, mMapView);
                                        //New Alert Dialog Working in Progress

                                        openSurveyLocationDialog("11KV", e, mMapView, "Existing");
                                    }
                                }

                            }
                        } else {
                            //OLD CODE
                            //openExistingSurveyDialog11_PROPOSED(e, mMapView);

                            //NEW CODE
                            if (LOCATION_COUNT_11KV_FEEDER_10 == 0) {
                                IS_CURRENT_LOCATION_SELECTED = true;
                                IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                openSurveyScreen(e, mMapView);
                            } else {
                                openSurveyLocationDialog("11KV", e, mMapView, "Proposed");
                                //openExistingSurveyDialog11_PROPOSED(e, mMapView);
                            }
                        }
                    }






                    /*else {
                        if (isLTSelected && isComplted11kVSurvey) {

                            openSurveyScreen(e, mMapView);

                        }
                    }*/

                } else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")) {
                    //NEW INTERMEDIATE POLE LOGIC
                    double finalDistance = Double.MAX_VALUE;


                    for (int j = 0; j < locationFilteredDataLT.size(); j++) {

                        String poleLat = locationFilteredDataLT.get(j).getLATITUDE();
                        String poleLon = locationFilteredDataLT.get(j).getLONGITUDE();

                        double distance = getDistanceBetweenPoles33New(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );

                        if (distance < finalDistance) {
                            finalDistance = distance;
                        }
                    }
                    // openSurveyScreen(e, mMapView);

                    //NEW CHANGES BY CHANDU v3.7
                    System.out.println("IS_LT_EXISTING_SURVEY " + WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY);
                    if (WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY) {
                        System.out.println("EXECUTED_STATEMENT_MAP");
                        if (WorkInvolmentsActivity.IS_LT_FIRST_POLE) {
                            if (!WorkInvolmentsActivity.LT_FIRST_POLE_EXISTING) {
                                System.out.println("EXECUTED_STATEMENT_MAP 1");
                                EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                checkIntermediatePoleAndProceedLt(finalDistance, mMapView, e, latitude, longitude);

                            } else {
                                System.out.println("EXECUTED_STATEMENT_MAP 2" + true);
                                //New Alert Dialog Working in Progress
                                openSurveyLocationDialog("LT", e, mMapView, "Existing");
                                // openExistingSurveyDialogForLT(e, mMapView);


                            }
                            //showDialogWithSpinnersLTForFirstPole(mMapView, e);

                        } else {
                            if (IS_CURRENT_LOCATION_SELECTED) {
                                if (IS_CURRENT_LOCATION_LINE_EXTENSION_LT) {
                                    EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                    checkIntermediatePoleAndProceedLt(finalDistance, mMapView, e, latitude, longitude);

                                    //showDialogWithSpinners33KV(mMapView, e);
                                } else {
                                    EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                    openSurveyScreen(e, mMapView);
                                }

                            } else {
                                if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_LT) {
                                    EstimatesMap.SELECTED_LOCATION_TYPE = "New Location";
                                    checkIntermediatePoleAndProceedLt(finalDistance, mMapView, e, latitude, longitude);
                                    // openProposedIntermediateDialog33(mMapView, e);
                                } else {
                                    // openExistingSurveyDialogForLT(e, mMapView);
                                    openSurveyLocationDialog("LT", e, mMapView, "Existing");


                                    // openExistingSurveyDialog(e, mMapView);
                                }

                            }
                        }

                    } else {
                        boolean showDialog = true;
                        ArrayList<NewModel> temList_LT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);
                        ArrayList<NewModel> temList_11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);

                        if (temList_11 != null && !temList_11.isEmpty()) {
                            for (NewModel model : temList_11) {
                                if ("Proposed".equalsIgnoreCase(model.getDTR_SURVEY_TYPE()) &&
                                        model.getDTR_STRUCTURE_CODE().equalsIgnoreCase(
                                                WorkInvolmentsActivity.SELECTED_LT_CKT.split("_LTCKT_")[0])) {
                                    showDialog = false; // if condition matches → don't show distance alert
                                    break;
                                }
                            }
                            System.out.println("RESPONSEE-->" + showDialog);
                            if (!showDialog) {
                                int x = (int) e.getX();
                                int y = (int) e.getY();
                                android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
                                Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
                                Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());
                                Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
                                map.setInitialViewpoint(vp);
                                mv.setMap(map);

                                graphicsOverlay = new GraphicsOverlay();
                                mv.getGraphicsOverlays().add(graphicsOverlay);

                                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                        SimpleMarkerSymbol.Style.CROSS, Color.RED, 12);
                                graphic = new Graphic(myWGS84Point, symbol);
                                graphicsOverlay.getGraphics().add(graphic);

                                boolean isFirstPole = true;
                                for (NewModel model : temList_LT) {
                                    if (model.getLT_AVAILABLE() != null &&
                                            WorkInvolmentsActivity.SELECTED_LT_CKT.equalsIgnoreCase(model.getLT_AVAILABLE())) {
                                        isFirstPole = false;
                                        break;
                                    }
                                }
                                if (isFirstPole) {
                                    double DTR_LON = 0;
                                    double DTR_LAT = 0;

                                    for (int j = 0; j < temList_11.size(); j++) {
                                        if (WorkInvolmentsActivity.SELECTED_LT_CKT.split("_LTCKT_")[0]
                                                .equalsIgnoreCase(
                                                        temList_11.get(j).getDTR_STRUCTURE_CODE())) {
                                            String lat = temList_11.get(j).getLATITUDE();
                                            String log = temList_11.get(j).getLONGITUDE();
                                            DTR_LAT = Double.parseDouble(lat);
                                            DTR_LON = Double.parseDouble(log);
                                        }
                                    }

                                    System.out.println("DTR FIRST POLE DISTANCE : " + DTR_LON);

                                    Location currentStartPoint = new Location("locationT");
                                    currentStartPoint.setLatitude(DTR_LON); // ⚠️ LAT = Y
                                    currentStartPoint.setLongitude(DTR_LAT); // ⚠️ LON = X

                                    Location currentEndPoint = new Location("locationT");
                                    currentEndPoint.setLatitude(myWGS84Point.getY());
                                    currentEndPoint.setLongitude(myWGS84Point.getX());

                                    double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
                                    System.out.println("DTR FIRST POLE DISTANCE : " + currentDistance);

                                    if (currentDistance >= 3) {
                                        showDistanceWarningForTowerDialog((float) currentDistance, 3);
                                    } else {
                                        openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                                    }
                                } else {
                                    openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                                }
                                // openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                            } else {
                                // 🔽 your distance + first pole logic
                                int x = (int) e.getX();
                                int y = (int) e.getY();
                                android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
                                Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
                                Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());
                                Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
                                map.setInitialViewpoint(vp);
                                mv.setMap(map);

                                graphicsOverlay = new GraphicsOverlay();
                                mv.getGraphicsOverlays().add(graphicsOverlay);

                                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                        SimpleMarkerSymbol.Style.CROSS, Color.RED, 12);
                                graphic = new Graphic(myWGS84Point, symbol);
                                graphicsOverlay.getGraphics().add(graphic);

                                boolean isFirstPole = true;
                                for (NewModel model : temList_LT) {
                                    if (model.getLT_AVAILABLE() != null &&
                                            WorkInvolmentsActivity.SELECTED_LT_CKT.equalsIgnoreCase(model.getLT_AVAILABLE())) {
                                        isFirstPole = false;
                                        break;
                                    }
                                }


                                System.out.println("RESPONSEE2-->" + isFirstPole);
                                if (isFirstPole) {
                                    double DTR_LON = 0;
                                    double DTR_LAT = 0;

                                    for (int j = 0; j < WorkInvolmentsActivity.existingDtrDetails.size(); j++) {
                                        if (WorkInvolmentsActivity.SELECTED_LT_CKT.split("_LTCKT_")[0]
                                                .equalsIgnoreCase(
                                                        WorkInvolmentsActivity.existingDtrDetails.get(j).getDTR_STRUCTURE_CODE())) {
                                            String lat = WorkInvolmentsActivity.existingDtrDetails.get(j).getLATITUDE();
                                            String log = WorkInvolmentsActivity.existingDtrDetails.get(j).getLONGITUDE();
                                            DTR_LAT = Double.parseDouble(lat);
                                            DTR_LON = Double.parseDouble(log);
                                        }
                                    }

                                    Location currentStartPoint = new Location("locationT");
                                    currentStartPoint.setLatitude(DTR_LON); // ⚠️ LAT = Y
                                    currentStartPoint.setLongitude(DTR_LAT); // ⚠️ LON = X

                                    Location currentEndPoint = new Location("locationT");
                                    currentEndPoint.setLatitude(myWGS84Point.getY());
                                    currentEndPoint.setLongitude(myWGS84Point.getX());

                                    double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
                                    System.out.println("DTR FIRST POLE DISTANCE : " + currentDistance);

                                    if (!EST_TYPE.equalsIgnoreCase("PV")
                                            && !EST_TYPE.equalsIgnoreCase("NP")) {
                                        if (currentDistance >= 3 && showDialog) {
                                            showDistanceWarningForTowerDialog((float) currentDistance, 3);
                                        } else {
                                            openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                                        }
                                    } else {
                                        openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                                    }
                                } else {
                                    openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                                }
                            }

                        } else {
                            if (WorkInvolmentsActivity.ONLY_LT_WITH_PROPOSED) {
                                //System.out.println("ONLY_LT_WITH_PROPOSED-STEP1");

                                ArrayList<NewModel> temListLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);
                                //System.out.println("ONLY_LT_WITH_PROPOSED-STEP2" + temListLT.size());
                                if (temListLT.isEmpty()) {
                                    showDialog = true;
                                } else {
                                    boolean matchFound = false;
                                    for (NewModel model : temListLT) {
                                        if (model.getLT_AVAILABLE() != null &&
                                                WorkInvolmentsActivity.SELECTED_LT_CKT.equalsIgnoreCase(model.getLT_AVAILABLE())) {
                                            matchFound = true;
                                            break;
                                        }
                                    }
                                    showDialog = !matchFound;
                                }


                                int x = (int) e.getX();
                                int y = (int) e.getY();
                                android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
                                Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
                                Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());
                                Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
                                map.setInitialViewpoint(vp);
                                mv.setMap(map);
                                graphicsOverlay = new GraphicsOverlay();
                                mv.getGraphicsOverlays().add(graphicsOverlay);
                                // SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.GREEN, 12);
                                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 12); //size 12, style of circle
                                graphic = new Graphic(myWGS84Point, symbol);
                                graphicsOverlay.getGraphics().add(graphic);

                                boolean isFirstPole = true;
                                for (NewModel model : temListLT) {
                                    if (model.getLT_AVAILABLE() != null &&
                                            WorkInvolmentsActivity.SELECTED_LT_CKT.equalsIgnoreCase(model.getLT_AVAILABLE())) {
                                        isFirstPole = false;
                                        break;
                                    }
                                }

                                if (isFirstPole) {
                                    double DTR_LON = 0;
                                    double DTR_LAT = 0;

                                    for (int j = 0; j < WorkInvolmentsActivity.existingDtrDetails.size(); j++) {
                                        if (WorkInvolmentsActivity.SELECTED_LT_CKT.split("_LTCKT_")[0]
                                                .equalsIgnoreCase(WorkInvolmentsActivity.existingDtrDetails.get(j).getDTR_STRUCTURE_CODE())) {
                                            String lat = WorkInvolmentsActivity.existingDtrDetails.get(j).getLATITUDE();
                                            String log = WorkInvolmentsActivity.existingDtrDetails.get(j).getLONGITUDE();
                                            DTR_LAT = Double.parseDouble(lat);
                                            DTR_LON = Double.parseDouble(log);
                                        }
                                    }

                                    Location currentStartPoint = new Location("locationT");
                                    currentStartPoint.setLatitude(DTR_LON);
                                    currentStartPoint.setLongitude(DTR_LAT);

                                    Location currentEndPoint = new Location("locationT");
                                    currentEndPoint.setLatitude(myWGS84Point.getY());
                                    currentEndPoint.setLongitude(myWGS84Point.getX());

                                    double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
                                    System.out.println("DTR FIRST POLE DISTANCE : " + currentDistance);
                                    if (!EST_TYPE.equalsIgnoreCase("PV")
                                            && !EST_TYPE.equalsIgnoreCase("NP")) {
                                        if (currentDistance >= 3 && showDialog) {
                                            showDistanceWarningForTowerDialog((float) currentDistance, 3);
                                        } else {
                                            openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                                            //   openExistingSurveyDialogForLT_PROPOSED(e, mMapView);
                                        }
                                    } else {
                                        openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                                    }


                                } else {
                                    openSurveyLocationDialog("LT", e, mMapView, "Proposed");
                                    // openExistingSurveyDialogForLT_PROPOSED(e, mMapView);
                                }


                            } else {
                                // openExistingSurveyDialogForLT_PROPOSED(e, mMapView);
                                openSurveyLocationDialog("LT", e, mMapView, "Proposed");

                                System.out.println("ONLY_LT_WITH_PROPOSED" + WorkInvolmentsActivity.ONLY_LT_WITH_PROPOSED);
                            }
                        }

                    }


                }


                return true;

            }

        });


        //NEW CODE FOR 33KV LINE EXTENSION START


        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                // btn_complete_survey.setText("Complete FEEDER 1 Survey");
                if (LOCATION_COUNT_33KV_FEEDER_1 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 1 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                if (LOCATION_COUNT_33KV_FEEDER_2 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 2 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }

            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                if (LOCATION_COUNT_33KV_FEEDER_3 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 3 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                if (LOCATION_COUNT_33KV_FEEDER_4 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 4 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                if (LOCATION_COUNT_33KV_FEEDER_5 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete 33kV Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
        }
        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
            System.out.println("SELECTED_SURVEY" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                // btn_complete_survey.setText("Complete FEEDER 1 Survey");
                if (LOCATION_COUNT_11KV_FEEDER_1 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 1 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                if (LOCATION_COUNT_11KV_FEEDER_2 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 2 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }

            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                if (LOCATION_COUNT_11KV_FEEDER_3 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 3 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 4 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                if (LOCATION_COUNT_11KV_FEEDER_5 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete 11kV Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 6")) {
                // btn_complete_survey.setText("Complete FEEDER 6 Survey");
                if (LOCATION_COUNT_11KV_FEEDER_6 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 6 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 7")) {
                // btn_complete_survey.setText("Complete FEEDER 7 Survey");
                if (LOCATION_COUNT_11KV_FEEDER_7 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 7 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 8")) {
                // btn_complete_survey.setText("Complete FEEDER 8 Survey");
                if (LOCATION_COUNT_11KV_FEEDER_8 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 8 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 9")) {
                // btn_complete_survey.setText("Complete FEEDER 9 Survey");
                if (LOCATION_COUNT_11KV_FEEDER_9 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 9 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 10")) {
                // btn_complete_survey.setText("Complete FEEDER 10 Survey");
                if (LOCATION_COUNT_11KV_FEEDER_10 > 0) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete Feeder 10 Survey");
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }
        }


        if (WorkInvolmentsActivity.IS_LT_SELECTED) {
            if (tempListLT.size() > 0) {
                if (!WorkInvolmentsActivity.IS_LT_FIRST_POLE) {
                    btn_complete_survey.setVisibility(View.VISIBLE);
                    btn_complete_survey.setText("Complete LT Survey");
                }
            }

        }

        setLoadingMapData(sp_line_survey_type);


        //NEW CODE FOR 33KV LINE EXTENSION END
        tv_scrolling.setSelected(true);
        if (BTN_VALUE.equalsIgnoreCase("LT")) {
            if (!WorkInvolmentsActivity.SELECTED_LT_CKT.isEmpty()) {
                feeder_id.setVisibility(View.VISIBLE);
                feeder_id.setText("Circuit Name :" + " " + WorkInvolmentsActivity.SELECTED_LT_CKT);
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                    tv_scrolling.setText("GIS_ID: " + GIS_ID + " " + "FEEDERS: " + FEEDERS);
                } else {
                    tv_scrolling.setText("Est No: " + EST_NO + " " + "GIS_ID: " + GIS_ID + " " + "Est Nature: " + EST_NATURE + " " + "Feeder Id: " + "Circuit Name :" + " " + WorkInvolmentsActivity.SELECTED_LT_CKT);
                }

            }

        } else {
            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE != null) {
                System.out.println("TESTING_FEEDER:::::::::--->" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);
                String clickedFeeder = WorkInvolmentsActivity.CLICKED_FEEDER;
                int feederIndex = -1;

                if (clickedFeeder != null && clickedFeeder.startsWith("FEEDER")) {
                    try {
                        feederIndex = Integer.parseInt(clickedFeeder.split(" ")[1]);
                    } catch (NumberFormatException ignored) {
                    }
                }

                if (feederIndex >= 1 && feederIndex <= 5) {
                    String feederType = "", feederId = "", feederName = "";

                    if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                        System.out.println("TESTING_FEEDER:::::::::--->" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);
                        feederType = getFieldValue("FEEDER_TYPE_11KV_" + feederIndex);
                        feederId = getFieldValue("feeder_id" + feederIndex + "_11kv");
                        feederName = getFieldValue("feeder_name" + feederIndex + "_11kv");
                    } else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                        System.out.println("CURRENT_SURVEY_TYPE_IS3" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);
                        feederType = getFieldValue("FEEDER_TYPE_33KV_" + feederIndex);
                        feederId = getFieldValue("feeder_id" + feederIndex + "_33kv");
                        feederName = getFieldValue("feeder_name" + feederIndex + "_33kv");
                    }

                    System.out.println("TESTING_FEEDER:::::::::--->" + feederType);
                    System.out.println("TESTING_FEEDER:::::::::--->" + feederIndex);
                    System.out.println("TESTING_FEEDER:::::::::--->" + feederId);

                    if (feederId.contains("-33KV") || feederId.contains("-11KV")) {
                        if (!feederName.isEmpty()) {
                            feeder_id.setVisibility(View.VISIBLE);
                            feeder_id.setText("Feeder Name: " + feederName);

                        }
                    } else {
                        if (!feederName.isEmpty()) {
                            feeder_id.setVisibility(View.VISIBLE);
                            feeder_id.setText("Feeder Name: " + feederName + " (" + feederId + ")");

                        }
                    }


//                if ("LINE EXTENSION".equalsIgnoreCase(feederType)) {
//                    if (!feederName.isEmpty()) {
//                        feeder_id.setVisibility(View.VISIBLE);
//                        feeder_id.setText("Feeder Name: " + feederName + " (" + feederId + ")");
//
//                    }
//                } else {
//                    if (!feederName.isEmpty()) {
//                        feeder_id.setVisibility(View.VISIBLE);
//                        feeder_id.setText("Feeder Name: " + feederName);
//
//                    }
//                }
                }
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                    tv_scrolling.setText("GIS_ID: " + GIS_ID + " " + "FEEDERS: " + FEEDERS);
                } else {
                    tv_scrolling.setText("Est No: " + EST_NO + " " + "GIS_ID: " + GIS_ID + " " + "Est Nature: " + EST_NATURE + " " + "Feeder Id: " + "Circuit Name :" + " " + WorkInvolmentsActivity.SELECTED_LT_CKT);
                }
//                tv_scrolling.setText("Est No: " + EST_NO + " " + "GIS_ID: " + GIS_ID + " " + "Est Nature: " + EST_NATURE + " " + " " + feeder_id.getText().toString().trim());
            }
        }


    }

    private void setLoadingMapData(Spinner spinner) {
        List<String> data;
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            data = ConnectivityChecking.checkConnection(EstimatesMap.this)
                    ? SurveyList.online
                    : SurveyList.offline;
        } else {
            data = ConnectivityChecking.checkConnection(EstimatesMap.this)
                    ? DashBoard.online
                    : DashBoard.offline;
        }


        // Add " meters" to each value for display
        List<String> displayData = new ArrayList<>();
        for (String value : data) {
            displayData.add(value + " meters");
        }


        SpinnerAdapter<String> adapterPoleConfig11kv = new SpinnerAdapter<>(
                EstimatesMap.this, displayData);
        spinner.setAdapter(adapterPoleConfig11kv);

        // For Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                //System.out.println("SPINNER_VALUE_DEFALULT" + selectedValue);

                if (!selectedValue.equalsIgnoreCase("Select meters")) {
                    try {
                        double distanceValue = Double.parseDouble(selectedValue.replace(" meters", ""));

                        if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                            ONLINE_DISTANCE_LIMIT = distanceValue;
                        } else {
                            OFFLINE_DISTANCE_LIMIT = distanceValue;
                        }

                        existingPoleDataDistance = ConnectivityChecking.checkConnection(EstimatesMap.this)
                                ? ONLINE_DISTANCE_LIMIT
                                : OFFLINE_DISTANCE_LIMIT;

                        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                            clearMapGraphics();
                            loadExistingPoles33();
                        }
                        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                            clearMapGraphics();
                            loadExistingPoles11();
                        }


                    } catch (NumberFormatException e) {
                        Toast.makeText(EstimatesMap.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    OFFLINE_DISTANCE_LIMIT = 2000;
                    ONLINE_DISTANCE_LIMIT = 500;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDistanceWarningForTowerDialog(float distance, int maxDistance) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        // Title
        title.setText("Warning!");

        int roundedDistance = Math.round(distance);

        // 🔴 First line (Bold + Red)
        SpannableString spannable1 =
                new SpannableString("Length from source pole = " + roundedDistance + "m\n");

        spannable1.setSpan(new StyleSpan(Typeface.BOLD), 0, spannable1.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable1.setSpan(new ForegroundColorSpan(Color.RED), 0, spannable1.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // ✍️ Second line (Italic)
        SpannableString spannable2 =
                new SpannableString("Distance of the first location from DTR should be less than "
                        + maxDistance + " m");

        spannable2.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannable2.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Combine
        CharSequence finalMessage = TextUtils.concat(spannable1, spannable2);

        message.setText(finalMessage);

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        okBtn.setText("OK");

        // Click
        okBtn.setOnClickListener(v -> {
            graphicsOverlay.getGraphics().remove(graphic);
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background + full width
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        dialog.show();
    }
//    private void showDistanceWarningForTowerDialog(float distance, int maxDistance) {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EstimatesMap.this);
//
//        builder.setTitle("Warning!");
//        int roundedDistance = Math.round(distance);
//
//        SpannableString spannable1 = new SpannableString("Length from source pole = " + roundedDistance + "m\n");
//        spannable1.setSpan(new StyleSpan(Typeface.BOLD), 0, spannable1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable1.setSpan(new ForegroundColorSpan(Color.RED), 0, spannable1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        //SpannableString spannable2 = new SpannableString("Span length  for TOWER should be below" + " " + maxDistance + "M");
//        SpannableString spannable2 = new SpannableString("Distance of the First location from DTR location should be less than 3 meters" + " " + maxDistance + "M");
//        spannable2.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannable2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        CharSequence message = TextUtils.concat(spannable1, spannable2);
//
//        builder.setMessage(message);
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                graphicsOverlay.getGraphics().remove(graphic);
//                dialog.dismiss();
//
//
//            }
//        });
//
//        android.app.AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }


    private int getPolesCount(String ltCktCode) {
        //  Log.d("Debug", "Selected LT CKT " + ltCktCode);
        int totalPolesLT = 0;
        ArrayList<NewModel> allPoles = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);


        for (int i = 0; i < allPoles.size(); i++) {
            // Log.d("Debug", "Selected LT CKT " + ltCktCode);
            if (allPoles.get(i).getLT_AVAILABLE().equalsIgnoreCase(ltCktCode)) {
                totalPolesLT++;
            }
        }

        return totalPolesLT;
    }

    int countOfLTpoles = 0;

//    private void loadExistingPolesLT() {
//        locationFilteredDataLT.clear();
//        currentLocationGpsTracker = new GpsTraker(this);
//        tap_load_data.setVisibility(View.GONE);
//        tap_select.setVisibility(View.VISIBLE);
//        ArrayList<NewModel> tempList = WorkInvolmentsActivity.existingPolesList;
//
//        System.out.println("LT EXT SIZE IN MAP : " + tempList.size());
//        ArrayList<NewModel> data33kv = new ArrayList<>();
//        ArrayList<NewModel> totalLT = new ArrayList<>();
//
//        for (int j = 0; j < tempList.size(); j++) {
//            if (tempList.get(j).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
//                data33kv.add(tempList.get(j));
//            }
//        }
//
//
//        System.out.println("LT EXT SIZE IN MAP FILTERED: " + data33kv.size());
//
//        if (data33kv.size() > 0) {
//
//            Map<String, NewModel> poleDataMap = new HashMap<>();
//            ArrayList<String> poleNumbers = new ArrayList<>();
//
//           /* android.graphics.Point clickedPoint = new android.graphics.Point(latX, longiY);
//            Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
//            Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());*/
//
//            ArrayList<NewModel> filteredData33kv = new ArrayList<>();
//
//            String extFeederID = WorkInvolmentsActivity.SELECTED_LT_CKT;
//
//
//            System.out.println("EXT FEEDER ID : " + extFeederID);
//
//            // Filter poles within 2 km
//            for (NewModel model : data33kv) {
//                if (extFeederID.equalsIgnoreCase(model.getLT_AVAILABLE())) {
//                    double distance = getDistanceBetweenPolesLT(
//                            model.getLATITUDE(),
//                            model.getLONGITUDE(),
//                            currentLocationGpsTracker.getLongitude(),
//                            currentLocationGpsTracker.getLatitude()
//                            /*myWGS84Point.getX(),
//                            myWGS84Point.getY()*/
//                    );
//
//                    if (distance <= 500000) {
//                        poleNumbers.add(model.getPOLE_NO());
//                        poleDataMap.put(model.getPOLE_NO(), model);
//                        filteredData33kv.add(model);
//                        locationFilteredDataLT.add(model);
//                    }
//                } else {

    /// /                    double distance = getDistanceBetweenPolesLT(
    /// /                            model.getLATITUDE(),
    /// /                            model.getLONGITUDE(),
    /// /
    /// /
    /// /                            currentLocationGpsTracker.getLongitude(),
    /// /                            currentLocationGpsTracker.getLatitude()
    /// /                            /*myWGS84Point.getX(),
    /// /                            myWGS84Point.getY()*/
    /// /                    );
    /// /
    /// /                    if (distance <= 500) {
    /// /                        poleNumbers.add(model.getPOLE_NO());
    /// /                        poleDataMap.put(model.getPOLE_NO(), model);
    /// /                        filteredData33kv.add(model);
    /// /                        locationFilteredDataLT.add(model);
    /// /                    }
//                }
//
//            }
//            countOfLTpoles = locationFilteredDataLT.size();
//
//            System.out.println("PoleCountLT--> : " + countOfLTpoles);
//
//            if (countOfLTpoles > 0) {
//                openAlertDialog(countOfLTpoles, "LT");
//            }
//
//            if (!filteredData33kv.isEmpty()) {
//
//                // firstTap11 = true;
//                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//                for (NewModel surveyVo : filteredData33kv) {
//                    ptWgs84 = new Point(
//                            Double.parseDouble(surveyVo.getLATITUDE()),
//                            Double.parseDouble(surveyVo.getLONGITUDE()),
//                            414,
//                            SpatialReferences.getWgs84()
//                    );
//                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
//                    map.setInitialViewpoint(vp);
//                    mv.setMap(map);
//                    graphicsOverlay = new GraphicsOverlay();
//                    mv.getGraphicsOverlays().add(graphicsOverlay);
//                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 5);
//                    String labelText = "";
//                    int labelColor = Color.RED;
//
//                    if (surveyVo.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Existing")) {
//                        if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")) {
//                            labelText = "33/11kV SS";
//                            labelColor = Color.CYAN;
//                        }
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")) {
//                        labelText = "33/11kV SS";
//                        labelColor = Color.CYAN;
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("EHT SS-Bay")) {
//                        labelText = "EHT";
//                        labelColor = Color.BLUE;
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("HT Service-Pole")) {
//                        labelText = "11kV HT Service";
//                        labelColor = this.getResources().getColor(R.color.red);
//                    }
//
//                    Graphic graphic = new Graphic(ptWgs84, symbol);
//                    graphicsOverlay.getGraphics().add(graphic);
//
//                    if (!labelText.isEmpty()) {
//                        TextSymbol textSymbol = new TextSymbol(
//                                12,
//                                labelText,
//                                labelColor,
//                                TextSymbol.HorizontalAlignment.RIGHT,
//                                TextSymbol.VerticalAlignment.TOP
//                        );
//
//                        Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);
//                        graphicsOverlay.getGraphics().add(labelGraphic);
//                    }
//
//                    graphicsOverlay.setLabelsEnabled(true);
//
//                    calloutContent = new TextView(getApplicationContext());
//                    calloutContent.setTextColor(Color.BLACK);
//                    calloutContent.setSingleLine();
//                    calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
//                    mCallout = mv.getCallout();
//                    mCallout.setLocation(ptWgs84);
//                    mCallout.setContent(calloutContent);
//                    mCallout.show();
//
//                    //Display Text
//
//                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
//                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
//                    // create a text symbol for elevation mode
//                    TextSymbol absoluteText = new TextSymbol(15, surveyVo.getLT_AVAILABLE() + "-" + String.valueOf(surveyVo.getPOLE_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
//                            TextSymbol.VerticalAlignment.TOP);
//                    absoluteText.setOffsetY(20);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
//                    mv.getGraphicsOverlays().add(absoluteOverlay);
//
//                }
//                // Add polyline functionality
//                for (NewModel surveyVo : filteredData33kv) {
//                    PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//                    for (int i = 0; i < filteredData33kv.size(); i++) {
//                        if (surveyVo.getFROM_POLE().equalsIgnoreCase(filteredData33kv.get(i).getPOLE_NO())) {
//                            polylinePoints.add(new Point(Double.parseDouble(filteredData33kv.get(i).getLATITUDE()), Double.parseDouble(filteredData33kv.get(i).getLONGITUDE())));
//                        }
//                    }
//
//                    polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
//                    Polyline polyline = new Polyline(polylinePoints);
//
//                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2.0f);
//                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
//                    mGraphicsOverlay.getGraphics().add(polylineGraphic);
//
//                    if (polylinePoints.size() >= 2) {
//                        Point startPoint = polylinePoints.get(0);
//                        Point endPoint = polylinePoints.get(1);
//
//                        double midpointX = (startPoint.getX() + endPoint.getX()) / 2;
//                        double midpointY = (startPoint.getY() + endPoint.getY()) / 2;
//
//                        Point midpoint = new Point(midpointX, midpointY, SpatialReferences.getWgs84());
//
//                        TextSymbol textSymbol = new TextSymbol(12, "LT", Color.WHITE, TextSymbol.HorizontalAlignment.RIGHT, TextSymbol.VerticalAlignment.TOP);
//                        Graphic labelGraphic = new Graphic(midpoint, textSymbol);
//                        mGraphicsOverlay.getGraphics().add(labelGraphic);
//                    }
//                }
//            }
//
//
//        }
//    }
    private void loadExistingPolesLT() {

        // 🔵 CLEAR OLD DATA
        locationFilteredDataLT.clear();
        currentLocationGpsTracker = new GpsTraker(this);

        ArrayList<NewModel> tempList = WorkInvolmentsActivity.existingPolesList;
        ArrayList<NewModel> dataLT = new ArrayList<>();

        // 🔵 FILTER LT ONLY (BACKGROUND SAFE)
        for (NewModel model : tempList) {
            if ("LT".equalsIgnoreCase(model.getVOLTAGE_LEVEL())) {
                dataLT.add(model);
            }
        }

        if (dataLT.isEmpty()) return;

        // 🔵 RUN HEAVY WORK IN BACKGROUND THREAD
        new Thread(() -> {

            Map<String, NewModel> poleDataMap = new HashMap<>();
            ArrayList<String> poleNumbers = new ArrayList<>();
            ArrayList<NewModel> filteredDataLT = new ArrayList<>();

            String extFeederID = WorkInvolmentsActivity.SELECTED_LT_CKT;

            for (NewModel model : dataLT) {

                if (extFeederID.equalsIgnoreCase(model.getLT_AVAILABLE())) {

                    double distance = getDistanceBetweenPolesLT(
                            model.getLATITUDE(),
                            model.getLONGITUDE(),
                            currentLocationGpsTracker.getLongitude(),
                            currentLocationGpsTracker.getLatitude()
                    );

                    if (distance <= 500000) {
                        poleNumbers.add(model.getPOLE_NO());
                        poleDataMap.put(model.getPOLE_NO(), model);
                        filteredDataLT.add(model);
                        locationFilteredDataLT.add(model);
                    }
                }
            }
            final int poleCount = filteredDataLT.size();

            countOfLTpoles = locationFilteredDataLT.size();

            // 🔵 UI WORK ONLY
            runOnUiThread(() -> {

                tap_load_data.setVisibility(View.GONE);
                tap_select.setVisibility(View.VISIBLE);

                if (poleCount > 0) {
                    openAlertDialog(poleCount, "LT");
                    //openAlertDialog(countOfLTpoles, "LT");

                    // ⬇⬇ MOVE ALL MAP/UI LOGIC INTO METHODS (CLEAN)
                    drawPolesOnMapLT(filteredDataLT);
                    drawPolylinesLT(filteredDataLT);

                } else {
                    Toast.makeText(
                            this,
                            "Existing LT locations not found nearby.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

        }).start();
    }

    private void drawPolesOnMapLT(List<NewModel> poles) {

        graphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(graphicsOverlay);

        // -------- SET VIEWPOINT ONCE --------
        NewModel first = poles.get(0);
        Point vpPoint = new Point(
                Double.parseDouble(first.getLATITUDE()),
                Double.parseDouble(first.getLONGITUDE()),
                SpatialReferences.getWgs84()
        );

        map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
        mv.setMap(map);

        for (NewModel surveyVo : poles) {

            Point pt = new Point(
                    Double.parseDouble(surveyVo.getLATITUDE()),
                    Double.parseDouble(surveyVo.getLONGITUDE()),
                    SpatialReferences.getWgs84()
            );

            // -------- MARKER --------
            SimpleMarkerSymbol symbol =
                    new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 5);
            graphicsOverlay.getGraphics().add(new Graphic(pt, symbol));

            // -------- POLE NUMBER --------
            TextSymbol poleNoSymbol = new TextSymbol(
                    12,
                    surveyVo.getPOLE_NO(),
                    Color.WHITE,
                    TextSymbol.HorizontalAlignment.LEFT,
                    TextSymbol.VerticalAlignment.BOTTOM
            );
            poleNoSymbol.setOffsetY(10);
            graphicsOverlay.getGraphics().add(new Graphic(pt, poleNoSymbol));

            // -------- CONFIG LABEL --------
            if ("LT Service-Pole".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) {
                TextSymbol cfg = new TextSymbol(
                        12,
                        "LT Service",
                        Color.RED,
                        TextSymbol.HorizontalAlignment.RIGHT,
                        TextSymbol.VerticalAlignment.TOP
                );
                graphicsOverlay.getGraphics().add(new Graphic(pt, cfg));
            }

            // -------- CALLOUT --------
            TextView calloutContent = new TextView(this);
            calloutContent.setText(surveyVo.getPOLE_NO());

            Callout callout = mv.getCallout();
            callout.setLocation(pt);
            callout.setContent(calloutContent);
            callout.show();
        }
    }

    private void drawPolylinesLT(List<NewModel> poles) {

        if (poles == null || poles.size() < 2) return;

        if (mGraphicsOverlay == null) {
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
        }

        for (NewModel surveyVo : poles) {

            PointCollection polylinePoints =
                    new PointCollection(SpatialReferences.getWgs84());

            // -------- FROM POLE --------
            if (surveyVo.getFROM_POLE() != null) {
                for (NewModel model : poles) {
                    if (surveyVo.getFROM_POLE().equalsIgnoreCase(model.getPOLE_NO())) {

                        polylinePoints.add(new Point(
                                Double.parseDouble(model.getLATITUDE()),
                                Double.parseDouble(model.getLONGITUDE()),
                                SpatialReferences.getWgs84()
                        ));
                        break;
                    }
                }
            }

            // -------- TO POLE --------
            polylinePoints.add(new Point(
                    Double.parseDouble(surveyVo.getLATITUDE()),
                    Double.parseDouble(surveyVo.getLONGITUDE()),
                    SpatialReferences.getWgs84()
            ));

            if (polylinePoints.size() < 2) continue;

            // -------- DRAW LINE --------
            Polyline polyline = new Polyline(polylinePoints);
            SimpleLineSymbol lineSymbol =
                    new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2f);

            mGraphicsOverlay.getGraphics().add(
                    new Graphic(polyline, lineSymbol)
            );

            // -------- MIDPOINT --------
            Point start = polylinePoints.get(0);
            Point end = polylinePoints.get(1);

            Point midpoint = new Point(
                    (start.getX() + end.getX()) / 2,
                    (start.getY() + end.getY()) / 2,
                    SpatialReferences.getWgs84()
            );

            // -------- LABEL TEXT --------
            String ltCkt = surveyVo.getLT_AVAILABLE() == null
                    ? ""
                    : surveyVo.getLT_AVAILABLE();

            String poleNo = surveyVo.getPOLE_NO();

            String labelText;
            if ("DTR POLE".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) {
                labelText = ltCkt + "-" + poleNo + "-DTR";
            } else {
                labelText = ltCkt + "-" + poleNo;
            }

            // -------- DRAW LABEL --------
            TextSymbol label = new TextSymbol(
                    12,
                    labelText,
                    Color.WHITE,
                    TextSymbol.HorizontalAlignment.CENTER,
                    TextSymbol.VerticalAlignment.MIDDLE
            );

            mGraphicsOverlay.getGraphics().add(
                    new Graphic(midpoint, label)
            );
        }
    }


    int countOfLTExistingPoles = 0;

//    private void loadExistingPolesLTForOthers() {
//        locationFilteredDataLT.clear();
//        currentLocationGpsTracker = new GpsTraker(this);
//        tap_load_data.setVisibility(View.GONE);
//        tap_select.setVisibility(View.VISIBLE);
//        ArrayList<NewModel> tempList = WorkInvolmentsActivity.existingPolesList;
//        System.out.println("LT EXT SIZE IN MAP OTHERS: " + tempList.size());
//        ArrayList<NewModel> data33kv = new ArrayList<>();
//
//        for (int j = 0; j < tempList.size(); j++) {
//            if (tempList.get(j).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
//                data33kv.add(tempList.get(j));
//            }
//        }
//
//        System.out.println("LT EXT SIZE IN MAP FILTERED OTHERS: " + data33kv.size());
//
//        IS_CURRENT_LOCATION_LINE_EXTENSION_LT = true;
//
//        if (data33kv.size() > 0) {
//
//            Map<String, NewModel> poleDataMap = new HashMap<>();
//            ArrayList<String> poleNumbers = new ArrayList<>();
//
//           /* android.graphics.Point clickedPoint = new android.graphics.Point(latX, longiY);
//            Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
//            Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());*/
//
//            ArrayList<NewModel> filteredData33kv = new ArrayList<>();
//
//            String extFeederID = WorkInvolmentsActivity.SELECTED_LT_CKT;
//
//
//            System.out.println("EXT FEEDER ID OTHERS : " + extFeederID);
//
//            // Filter poles within 2 km
//            for (NewModel model : data33kv) {
//
//                if (extFeederID.equalsIgnoreCase(model.getLT_AVAILABLE())) {
//                    double distance = getDistanceBetweenPolesLT(
//                            model.getLATITUDE(),
//                            model.getLONGITUDE(),
//
//
//                            currentLocationGpsTracker.getLongitude(),
//                            currentLocationGpsTracker.getLatitude()
//                            /*myWGS84Point.getX(),
//                            myWGS84Point.getY()*/
//                    );
//
//                    if (distance <= 500000) {
//                        poleNumbers.add(model.getPOLE_NO());
//                        poleDataMap.put(model.getPOLE_NO(), model);
//                        filteredData33kv.add(model);
//                        locationFilteredDataLT.add(model);
//                    }
//                }
//
//            }
//
//            countOfLTExistingPoles = locationFilteredDataLT.size();
//            // System.out.println("HAHAHAHAHA-->" + countOfLTExistingPoles);
//            openAlertDialogExisting(countOfLTExistingPoles, "LT");
//
//            if (!filteredData33kv.isEmpty()) {
//
//                // firstTap11 = true;
//                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//                for (NewModel surveyVo : filteredData33kv) {
//                    ptWgs84 = new Point(
//                            Double.parseDouble(surveyVo.getLATITUDE()),
//                            Double.parseDouble(surveyVo.getLONGITUDE()),
//                            414,
//                            SpatialReferences.getWgs84()
//                    );
//                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
//                    map.setInitialViewpoint(vp);
//                    mv.setMap(map);
//                    graphicsOverlay = new GraphicsOverlay();
//                    mv.getGraphicsOverlays().add(graphicsOverlay);
//
//                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 5);
//                    String labelText = "";
//                    int labelColor = Color.RED;
//
//                    if (surveyVo.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Existing")) {
//                        if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")) {
//                            labelText = "33/11kV SS";
//                            labelColor = Color.CYAN;
//                        }
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")) {
//                        labelText = "33/11kV SS";
//                        labelColor = Color.CYAN;
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("EHT SS-Bay")) {
//                        labelText = "EHT";
//                        labelColor = Color.BLUE;
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("HT Service-Pole")) {
//                        labelText = "11kV HT Service";
//                        labelColor = this.getResources().getColor(R.color.red);
//                    }
//
//                    Graphic graphic = new Graphic(ptWgs84, symbol);
//                    graphicsOverlay.getGraphics().add(graphic);
//
//                    if (!labelText.isEmpty()) {
//                        TextSymbol textSymbol = new TextSymbol(
//                                12,
//                                labelText,
//                                labelColor,
//                                TextSymbol.HorizontalAlignment.RIGHT,
//                                TextSymbol.VerticalAlignment.TOP
//                        );
//
//                        Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);
//                        graphicsOverlay.getGraphics().add(labelGraphic);
//                    }
//
//                    graphicsOverlay.setLabelsEnabled(true);
//
//                    calloutContent = new TextView(getApplicationContext());
//                    calloutContent.setTextColor(Color.BLACK);
//                    calloutContent.setSingleLine();
//                    calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
//                    mCallout = mv.getCallout();
//                    mCallout.setLocation(ptWgs84);
//                    mCallout.setContent(calloutContent);
//                    mCallout.show();
//
//                    //Display Text
//
//                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
//                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
//                    // create a text symbol for elevation mode
//                    TextSymbol absoluteText = new TextSymbol(15, surveyVo.getLT_AVAILABLE() + "-" + String.valueOf(surveyVo.getPOLE_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
//                            TextSymbol.VerticalAlignment.TOP);
//                    absoluteText.setOffsetY(20);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
//                    mv.getGraphicsOverlays().add(absoluteOverlay);
//
//                }
//                // Add polyline functionality
//                for (NewModel surveyVo : filteredData33kv) {
//                    PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//                    for (int i = 0; i < filteredData33kv.size(); i++) {
//                        if (surveyVo.getFROM_POLE().equalsIgnoreCase(filteredData33kv.get(i).getPOLE_NO())) {
//                            polylinePoints.add(new Point(Double.parseDouble(filteredData33kv.get(i).getLATITUDE()), Double.parseDouble(filteredData33kv.get(i).getLONGITUDE())));
//                        }
//                    }
//
//                    polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
//                    Polyline polyline = new Polyline(polylinePoints);
//
//                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2.0f);
//                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
//                    mGraphicsOverlay.getGraphics().add(polylineGraphic);
//
//                    if (polylinePoints.size() >= 2) {
//                        Point startPoint = polylinePoints.get(0);
//                        Point endPoint = polylinePoints.get(1);
//
//                        double midpointX = (startPoint.getX() + endPoint.getX()) / 2;
//                        double midpointY = (startPoint.getY() + endPoint.getY()) / 2;
//
//                        Point midpoint = new Point(midpointX, midpointY, SpatialReferences.getWgs84());
//
//                        TextSymbol textSymbol = new TextSymbol(12, "LT", Color.WHITE, TextSymbol.HorizontalAlignment.RIGHT, TextSymbol.VerticalAlignment.TOP);
//                        Graphic labelGraphic = new Graphic(midpoint, textSymbol);
//                        mGraphicsOverlay.getGraphics().add(labelGraphic);
//                    }
//                }
//            }
//
//
//        }
//    }

    private void loadExistingPolesLTForOthers() {

        locationFilteredDataLT.clear();
        currentLocationGpsTracker = new GpsTraker(this);

        ArrayList<NewModel> tempList = WorkInvolmentsActivity.existingPolesList;
        ArrayList<NewModel> dataLT = new ArrayList<>();

        // ---------------- FILTER LT ----------------
        for (NewModel model : tempList) {
            if ("LT".equalsIgnoreCase(model.getVOLTAGE_LEVEL())) {
                dataLT.add(model);
            }
        }

        if (dataLT.isEmpty()) {
            Toast.makeText(this, "No LT poles found", Toast.LENGTH_SHORT).show();
            return;
        }

        // ---------------- FEEDER (LT CKT) ----------------
        String extFeederID = WorkInvolmentsActivity.SELECTED_LT_CKT;

        if (extFeederID == null || extFeederID.trim().isEmpty()) {
            Toast.makeText(this, "LT Circuit not selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // ---------------- BACKGROUND WORK ----------------
        new Thread(() -> {

            ArrayList<NewModel> filteredDataLT = new ArrayList<>();

            for (NewModel model : dataLT) {

                if (!extFeederID.equalsIgnoreCase(model.getLT_AVAILABLE())) continue;

                double distance = getDistanceBetweenPolesLT(
                        model.getLATITUDE(),
                        model.getLONGITUDE(),
                        currentLocationGpsTracker.getLongitude(),
                        currentLocationGpsTracker.getLatitude()
                );

                if (distance <= 500000) {
                    filteredDataLT.add(model);
                    locationFilteredDataLT.add(model);
                }
            }
            final int poleCount = filteredDataLT.size();
            countOfLTExistingPoles = filteredDataLT.size();

            // ---------------- UI ----------------
            runOnUiThread(() -> {

                tap_load_data.setVisibility(View.GONE);
                tap_select.setVisibility(View.VISIBLE);

                if (poleCount > 0) {
                    openAlertDialogExisting(poleCount, "LT");
                } else {
                    Toast.makeText(
                            this,
                            "Existing locations not found nearby 200 meters.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }


                IS_CURRENT_LOCATION_LINE_EXTENSION_LT = true;
                firstTapLT = true;   // if you have a similar flag like 11kV

                drawPolesOnMapLT(filteredDataLT);
                drawPolylinesLT(filteredDataLT);
            });

        }).start();
    }


    private void openProposedIntermediateDialog11(MapView mapView, MotionEvent motionEvent, double lat, double lon) {
        double MAX_DISTANCE_METERS = 20.0;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());

        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.proposed_intermediate_dialog);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Spinner intPol1 = payDialog.findViewById(R.id.sp_intermediate_poll_1);
        Spinner intPol2 = payDialog.findViewById(R.id.sp_intermediate_poll_2);
        TextView submit = payDialog.findViewById(R.id.submit);

        // Map to hold pole numbers and their corresponding NewModel data
        Map<String, NewModel> poleDataMap = new HashMap<>();
        ArrayList<String> intPoleData = new ArrayList<>();
        intPoleData.add("Select");

        ArrayList<String> poleNumbers = new ArrayList<>();
        poleNumbers.add("Select");

        for (int j = 0; j < locationFilteredData11kv.size(); j++) {

            String poleLat = locationFilteredData11kv.get(j).getLATITUDE();
            String poleLon = locationFilteredData11kv.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,
                    lon
            );

            if (distance <= MAX_DISTANCE_METERS) {
                String poleNo = locationFilteredData11kv.get(j).getPOLE_NO();
                poleNumbers.add(poleNo);
                intPoleData.add(poleNo);
                poleDataMap.put(poleNo, locationFilteredData11kv.get(j));
            }
        }

//        for (int j = 0; j < locationFilteredData11kv.size(); j++) {
//            intPoleData.add(locationFilteredData11kv.get(j).getPOLE_NO());
//        }


        SpinnerAdapter<String> intPoleAdapter1 = new SpinnerAdapter<>(this, intPoleData);
        //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intPol1.setAdapter(intPoleAdapter1);


        intPol1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String selPole = intPol1.getSelectedItem().toString();
                    ArrayList<String> toPoleNumbers = new ArrayList<>();
                    toPoleNumbers.add("Select");
                    for (int j = 0; j < locationFilteredData11kv.size(); j++) {
                        if (selPole.equalsIgnoreCase(locationFilteredData11kv.get(j).getFROM_POLE())) {
                            toPoleNumbers.add(locationFilteredData11kv.get(j).getPOLE_NO());
                        }
                        if (selPole.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {
                            toPoleNumbers.add(locationFilteredData11kv.get(j).getFROM_POLE());

                        }
                    }

                    SpinnerAdapter<String> intPoleAdapter2 = new SpinnerAdapter<>(EstimatesMap.this, toPoleNumbers);
                    //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intPol2.setAdapter(intPoleAdapter2);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String intermediatePoll1 = "";
                String intermediatePoll2 = "";

                if (intPol1 != null && intPol1.getSelectedItem() != null) {
                    if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        intermediatePoll1 = intPol1.getSelectedItem().toString();
                    }
                }

                if (intPol2 != null && intPol2.getSelectedItem() != null) {
                    if (!intPol2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        intermediatePoll2 = intPol2.getSelectedItem().toString();
                    }
                }

                String sourceLocationPoll1 = "", sourceLocationPoll2 = "";

                if (intermediatePoll1.equalsIgnoreCase("Select") || intermediatePoll2.equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Invalid Pole numbers", Toast.LENGTH_SHORT).show();
                } else if (intermediatePoll1.equalsIgnoreCase(intermediatePoll2)) {
                    Toast.makeText(EstimatesMap.this, "You have selected the same poles", Toast.LENGTH_SHORT).show();
                } else if (intPol2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Intermediate poll 2", Toast.LENGTH_SHORT).show();

                } else if (intermediatePoll1.equalsIgnoreCase(intermediatePoll2)) {
                    Toast.makeText(EstimatesMap.this, "You have selected the same poles", Toast.LENGTH_SHORT).show();
                } else {
                    IS_CURRENT_LOCATION_SELECTED = true;

                    IS_INTERMEDIATE_POLE = true;
                    IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                    IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                    IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                    String latitude = "";
                    String longitude = "";
                    for (int j = 0; j < locationFilteredData11kv.size(); j++) {
                        if (intermediatePoll1.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {
                            sourceLocationPoll1 = locationFilteredData11kv.get(j).getFROM_POLE();
                        }

                        if (intermediatePoll2.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {
                            sourceLocationPoll2 = locationFilteredData11kv.get(j).getFROM_POLE();
                        }
                    }
                    boolean isMatched = false;
                    if (intermediatePoll1.equalsIgnoreCase(sourceLocationPoll2) || intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                        isMatched = true;
                        if (intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                            INT_FIRST_POLE = intermediatePoll2;
                            INT_SECOND_POLE = intermediatePoll1;
                        } else {
                            INT_FIRST_POLE = intermediatePoll1;
                            INT_SECOND_POLE = intermediatePoll2;
                        }

                        for (int j = 0; j < locationFilteredData11kv.size(); j++) {
                            if (INT_FIRST_POLE.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {
                                INT_FIRST_POLE_LAT = locationFilteredData11kv.get(j).getLATITUDE();
                                INT_FIRST_POLE_LON = locationFilteredData11kv.get(j).getLONGITUDE();
                                INT_FIRST_POLE_ID = locationFilteredData11kv.get(j).getREF_ID();
                                INT_FIRST_LINE_TYPE = locationFilteredData11kv.get(j).getLINE_TYPE();
                                INT_FIRST_POLE_TYPE = locationFilteredData11kv.get(j).getPOLE_TYPE();
                                INT_FIRST_POLE_HEIGHT = locationFilteredData11kv.get(j).getPOLE_HEIGHT();
                                INT_FIRST_POLE_SUPPORT = locationFilteredData11kv.get(j).getPOLE_SUPPORT();
                                INT_FIRST_PIN_CUT = locationFilteredData11kv.get(j).getPIN_CUT();
                                INT_FIRST_POLE_CONCREATE = locationFilteredData11kv.get(j).getPOLE_CONCRETE();
                                INT_FIRST_POLE_STRUCTURE = locationFilteredData11kv.get(j).getPOLE_STRUCTURE();
                                INT_FIRST_POLE_CONFIG = locationFilteredData11kv.get(j).getPOLE_CONFIG();
                                INT_FIRST_POLE_DTR_CODE = locationFilteredData11kv.get(j).getDTR_STRUCTURE_CODE();
                                INT_FIRST_POLE_DTR_ID = locationFilteredData11kv.get(j).getDTR_ID();
                                INT_FIRST_DTR_LOCATION = locationFilteredData11kv.get(j).getDTR_LOCATION();
                                INT_FIRST_DTR_AVAILABLE = locationFilteredData11kv.get(j).getLT_AVAILABLE();
                                INT_FIRST_DTR_MAKE = locationFilteredData11kv.get(j).getDTR_MAKE();
                                INT_FIRST_DTR_SLNO = locationFilteredData11kv.get(j).getDTR_SNO();
                                INT_FIRST_DTR_PHASE = locationFilteredData11kv.get(j).getDTR_PHASE();
                                INT_FIRST_DTR_CAPACITY = locationFilteredData11kv.get(j).getDTR_CAPACITY();
                                INT_FIRST_DTR_SHIFTED = locationFilteredData11kv.get(j).getDTR_SHIFTED();
                            }
                            if (INT_SECOND_POLE.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {
                                INT_SECOND_POLE_LAT = locationFilteredData11kv.get(j).getLATITUDE();
                                INT_SECOND_POLE_LON = locationFilteredData11kv.get(j).getLONGITUDE();
                                INT_SECOND_POLE_ID = locationFilteredData11kv.get(j).getREF_ID();
                                INT_SECOND_LINE_TYPE = locationFilteredData11kv.get(j).getLINE_TYPE();
                                INT_SECOND_POLE_TYPE = locationFilteredData11kv.get(j).getPOLE_TYPE();
                                INT_SECOND_POLE_HEIGHT = locationFilteredData11kv.get(j).getPOLE_HEIGHT();
                                INT_SECOND_POLE_SUPPORT = locationFilteredData11kv.get(j).getPOLE_SUPPORT();
                                INT_SECOND_PIN_CUT = locationFilteredData11kv.get(j).getPIN_CUT();
                                INT_SECOND_POLE_CONCREATE = locationFilteredData11kv.get(j).getPOLE_CONCRETE();
                                INT_SECOND_POLE_STRUCTURE = locationFilteredData11kv.get(j).getPOLE_STRUCTURE();
                                INT_SECOND_POLE_CONFIG = locationFilteredData11kv.get(j).getPOLE_CONFIG();
                                INT_SECOND_POLE_DTR_CODE = locationFilteredData11kv.get(j).getDTR_STRUCTURE_CODE();
                                INT_SECOND_POLE_DTR_ID = locationFilteredData11kv.get(j).getDTR_ID();
                                INT_SECOND_DTR_LOCATION = locationFilteredData11kv.get(j).getDTR_LOCATION();
                                INT_SECOND_DTR_AVAILABLE = locationFilteredData11kv.get(j).getLT_AVAILABLE();
                                INT_SECOND_DTR_LT_CKT_COUNT = String.valueOf(locationFilteredData11kv.get(j).getCOUNT_CKT());
                                INT_SECOND_DTR_MAKE = locationFilteredData11kv.get(j).getDTR_MAKE();
                                INT_SECOND_DTR_SLNO = locationFilteredData11kv.get(j).getDTR_SNO();
                                INT_SECOND_DTR_PHASE = locationFilteredData11kv.get(j).getDTR_PHASE();
                                INT_SECOND_DTR_CAPACITY = locationFilteredData11kv.get(j).getDTR_CAPACITY();
                                INT_SECOND_DTR_SHIFTED = locationFilteredData11kv.get(j).getDTR_SHIFTED();
                            }
                        }
                    }
                    if (isMatched) {
                        String latitude1 = String.valueOf(myWGS84Point.getX());
                        String longitude2 = String.valueOf(myWGS84Point.getY());
                        openSurveyDialog11KV(mapView, motionEvent, latitude1, longitude2);
                        payDialog.dismiss();
                    } else {
                        Toast.makeText(EstimatesMap.this, "Invalid Poles", Toast.LENGTH_SHORT).show();

                    }


                }


            }
        });

        payDialog.show();
    }

    private void openProposedIntermediateDialogLT(MapView mapView, MotionEvent motionEvent, double lat, double lon) {

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());

        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.proposed_intermediate_dialog);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Spinner intPol1 = payDialog.findViewById(R.id.sp_intermediate_poll_1);
        Spinner intPol2 = payDialog.findViewById(R.id.sp_intermediate_poll_2);
        TextView submit = payDialog.findViewById(R.id.submit);

        ArrayList<String> intPoleData = new ArrayList<>();

        intPoleData.add("Select");

        for (int j = 0; j < locationFilteredDataLT.size(); j++) {
            intPoleData.add(locationFilteredDataLT.get(j).getPOLE_NO());
        }


        SpinnerAdapter<String> intPoleAdapter1 = new SpinnerAdapter<>(this, intPoleData);
        //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intPol1.setAdapter(intPoleAdapter1);


        intPol1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String selPole = intPol1.getSelectedItem().toString();
                    ArrayList<String> toPoleNumbers = new ArrayList<>();
                    toPoleNumbers.add("Select");
                    for (int j = 0; j < locationFilteredDataLT.size(); j++) {
                        if (selPole.equalsIgnoreCase(locationFilteredDataLT.get(j).getFROM_POLE())) {
                            toPoleNumbers.add(locationFilteredDataLT.get(j).getPOLE_NO());
                        }
                        if (selPole.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                            toPoleNumbers.add(locationFilteredDataLT.get(j).getFROM_POLE());

                        }
                    }

                    SpinnerAdapter<String> intPoleAdapter2 = new SpinnerAdapter<>(EstimatesMap.this, toPoleNumbers);
                    //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intPol2.setAdapter(intPoleAdapter2);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String intermediatePoll1 = "";
                String intermediatePoll2 = "";

                if (intPol1 != null && intPol1.getSelectedItem() != null) {
                    if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        intermediatePoll1 = intPol1.getSelectedItem().toString();
                    }
                }

                if (intPol2 != null && intPol2.getSelectedItem() != null) {
                    if (!intPol2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        intermediatePoll2 = intPol2.getSelectedItem().toString();
                    }
                }


                String sourceLocationPoll1 = "", sourceLocationPoll2 = "";

                if (intermediatePoll1.equalsIgnoreCase("Select") || intermediatePoll2.equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Invalid Pole numbers", Toast.LENGTH_SHORT).show();
                }
                if (intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Intermediate poll 1", Toast.LENGTH_SHORT).show();
                } else if (intPol2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Intermediate poll 2", Toast.LENGTH_SHORT).show();

                } else if (intermediatePoll1.equalsIgnoreCase(intermediatePoll2)) {
                    Toast.makeText(EstimatesMap.this, "You have selected the same poles", Toast.LENGTH_SHORT).show();
                } else {
                    IS_CURRENT_LOCATION_SELECTED = true;

                    IS_INTERMEDIATE_POLE = true;
                    IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                    IS_CURRENT_LOCATION_LINE_EXTENSION_LT = false;
                    IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                    String latitude = "";
                    String longitude = "";
                    for (int j = 0; j < locationFilteredDataLT.size(); j++) {
                        if (intermediatePoll1.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                            sourceLocationPoll1 = locationFilteredDataLT.get(j).getFROM_POLE();
                        }

                        if (intermediatePoll2.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                            sourceLocationPoll2 = locationFilteredDataLT.get(j).getFROM_POLE();
                        }
                    }
                    boolean isMatched = false;
                    if (intermediatePoll1.equalsIgnoreCase(sourceLocationPoll2) || intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                        isMatched = true;
                        if (intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                            INT_FIRST_POLE = intermediatePoll2;
                            INT_SECOND_POLE = intermediatePoll1;
                        } else {
                            INT_FIRST_POLE = intermediatePoll1;
                            INT_SECOND_POLE = intermediatePoll2;
                        }

                        for (int j = 0; j < locationFilteredDataLT.size(); j++) {
                            if (INT_FIRST_POLE.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                INT_FIRST_POLE_LAT = locationFilteredDataLT.get(j).getLATITUDE();
                                INT_FIRST_POLE_LON = locationFilteredDataLT.get(j).getLONGITUDE();
                                INT_FIRST_POLE_ID = locationFilteredDataLT.get(j).getREF_ID();
                                INT_FIRST_LINE_TYPE = locationFilteredDataLT.get(j).getLINE_TYPE();
                                INT_FIRST_POLE_TYPE = locationFilteredDataLT.get(j).getPOLE_TYPE();
                                INT_FIRST_POLE_HEIGHT = locationFilteredDataLT.get(j).getPOLE_HEIGHT();
                                INT_FIRST_POLE_SUPPORT = locationFilteredDataLT.get(j).getPOLE_SUPPORT();

                                INT_FIRST_PIN_CUT = locationFilteredDataLT.get(j).getPIN_CUT();
                                INT_FIRST_POLE_CONCREATE = locationFilteredDataLT.get(j).getPOLE_CONCRETE();
                                INT_FIRST_POLE_STRUCTURE = locationFilteredDataLT.get(j).getPOLE_STRUCTURE();
                                INT_FIRST_POLE_CONFIG = locationFilteredDataLT.get(j).getPOLE_CONFIG();


                            }
                            if (INT_SECOND_POLE.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                INT_SECOND_POLE_LAT = locationFilteredDataLT.get(j).getLATITUDE();
                                INT_SECOND_POLE_LON = locationFilteredDataLT.get(j).getLONGITUDE();
                                INT_SECOND_POLE_ID = locationFilteredDataLT.get(j).getREF_ID();
                                INT_SECOND_LINE_TYPE = locationFilteredDataLT.get(j).getLINE_TYPE();
                                INT_SECOND_POLE_TYPE = locationFilteredDataLT.get(j).getPOLE_TYPE();
                                INT_SECOND_POLE_HEIGHT = locationFilteredDataLT.get(j).getPOLE_HEIGHT();
                                INT_SECOND_POLE_SUPPORT = locationFilteredDataLT.get(j).getPOLE_SUPPORT();
                                INT_SECOND_PIN_CUT = locationFilteredDataLT.get(j).getPIN_CUT();
                                INT_SECOND_POLE_CONCREATE = locationFilteredDataLT.get(j).getPOLE_CONCRETE();
                                INT_SECOND_POLE_STRUCTURE = locationFilteredDataLT.get(j).getPOLE_STRUCTURE();
                                INT_SECOND_POLE_CONFIG = locationFilteredDataLT.get(j).getPOLE_CONFIG();


                            }
                        }
                    }
                    if (isMatched) {
                        String latitude1 = String.valueOf(myWGS84Point.getX());
                        String longitude2 = String.valueOf(myWGS84Point.getY());
                        openSurveyDialogLT(mapView, motionEvent, latitude1, longitude2);
                        payDialog.dismiss();
                    } else {
                        Toast.makeText(EstimatesMap.this, "Invalid Poles", Toast.LENGTH_SHORT).show();

                    }


                }


            }
        });

        payDialog.show();
    }

    private void openProposedIntermediateDialog33(MapView mapView, MotionEvent motionEvent, double lat, double lon) {
        double MAX_DISTANCE_METERS = 20.0;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());

        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.proposed_intermediate_dialog);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Spinner intPol1 = payDialog.findViewById(R.id.sp_intermediate_poll_1);
        Spinner intPol2 = payDialog.findViewById(R.id.sp_intermediate_poll_2);
        TextView submit = payDialog.findViewById(R.id.submit);

        Map<String, NewModel> intPolData = new HashMap<>();
        ArrayList<String> intPoleData = new ArrayList<>();

        intPoleData.add("Select");

        for (int j = 0; j < locationFilteredData33kv.size(); j++) {

            String poleLat = locationFilteredData33kv.get(j).getLATITUDE();
            String poleLon = locationFilteredData33kv.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,
                    lon
            );

            System.out.println("DISTANCE---->::::" + distance);

            if (distance <= MAX_DISTANCE_METERS) {
                intPoleData.add(locationFilteredData33kv.get(j).getPOLE_NO());
                intPolData.put(
                        locationFilteredData33kv.get(j).getPOLE_NO(),
                        locationFilteredData33kv.get(j)
                );
            }
        }


        SpinnerAdapter<String> intPoleAdapter1 = new SpinnerAdapter<>(this, intPoleData);
        //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intPol1.setAdapter(intPoleAdapter1);


        intPol1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String selPole = intPol1.getSelectedItem().toString();
                    ArrayList<String> toPoleNumbers = new ArrayList<>();
                    toPoleNumbers.add("Select");
                    for (int j = 0; j < locationFilteredData33kv.size(); j++) {
                        if (selPole.equalsIgnoreCase(locationFilteredData33kv.get(j).getFROM_POLE())) {
                            toPoleNumbers.add(locationFilteredData33kv.get(j).getPOLE_NO());
                        }
                        if (selPole.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                            toPoleNumbers.add(locationFilteredData33kv.get(j).getFROM_POLE());

                        }
                    }

                    SpinnerAdapter<String> intPoleAdapter2 = new SpinnerAdapter<>(EstimatesMap.this, toPoleNumbers);
                    //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intPol2.setAdapter(intPoleAdapter2);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String intermediatePoll1 = "";
                String intermediatePoll2 = "";

                if (intPol1 != null && intPol1.getSelectedItem() != null) {
                    if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        intermediatePoll1 = intPol1.getSelectedItem().toString();
                    }
                }

                if (intPol2 != null && intPol2.getSelectedItem() != null) {
                    if (!intPol2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        intermediatePoll2 = intPol2.getSelectedItem().toString();
                    }
                }


                String sourceLocationPoll1 = "", sourceLocationPoll2 = "";

                if (intermediatePoll1.equalsIgnoreCase("Select") || intermediatePoll2.equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Invalid Pole numbers", Toast.LENGTH_SHORT).show();
                }
                if (intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Intermediate poll 1", Toast.LENGTH_SHORT).show();
                } else if (intPol2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Intermediate poll 2", Toast.LENGTH_SHORT).show();

                } else if (intermediatePoll1.equalsIgnoreCase(intermediatePoll2)) {
                    Toast.makeText(EstimatesMap.this, "You have selected the same poles", Toast.LENGTH_SHORT).show();
                } else {
                    IS_CURRENT_LOCATION_SELECTED = true;

                    IS_INTERMEDIATE_POLE = true;
                    IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                    IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                    IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                    String latitude = "";
                    String longitude = "";
                    for (int j = 0; j < locationFilteredData33kv.size(); j++) {
                        if (intermediatePoll1.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                            sourceLocationPoll1 = locationFilteredData33kv.get(j).getFROM_POLE();
                        }

                        if (intermediatePoll2.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                            sourceLocationPoll2 = locationFilteredData33kv.get(j).getFROM_POLE();
                        }
                    }
                    boolean isMatched = false;
                    if (intermediatePoll1.equalsIgnoreCase(sourceLocationPoll2) || intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                        isMatched = true;
                        if (intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                            INT_FIRST_POLE = intermediatePoll2;
                            INT_SECOND_POLE = intermediatePoll1;
                        } else {
                            INT_FIRST_POLE = intermediatePoll1;
                            INT_SECOND_POLE = intermediatePoll2;
                        }

                        for (int j = 0; j < locationFilteredData33kv.size(); j++) {
                            if (INT_FIRST_POLE.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                                INT_FIRST_POLE_LAT = locationFilteredData33kv.get(j).getLATITUDE();
                                INT_FIRST_POLE_LON = locationFilteredData33kv.get(j).getLONGITUDE();
                                INT_FIRST_POLE_ID = locationFilteredData33kv.get(j).getREF_ID();
                                INT_FIRST_LINE_TYPE = locationFilteredData33kv.get(j).getLINE_TYPE();
                                INT_FIRST_POLE_TYPE = locationFilteredData33kv.get(j).getPOLE_TYPE();
                                INT_FIRST_POLE_HEIGHT = locationFilteredData33kv.get(j).getPOLE_HEIGHT();
                                INT_FIRST_POLE_SUPPORT = locationFilteredData33kv.get(j).getPOLE_SUPPORT();
                                INT_FIRST_PIN_CUT = locationFilteredData33kv.get(j).getPIN_CUT();
                                INT_FIRST_POLE_CONCREATE = locationFilteredData33kv.get(j).getPOLE_CONCRETE();
                                INT_FIRST_POLE_STRUCTURE = locationFilteredData33kv.get(j).getPOLE_STRUCTURE();
                                INT_FIRST_POLE_CONFIG = locationFilteredData33kv.get(j).getPOLE_CONFIG();
                            }
                            if (INT_SECOND_POLE.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                                INT_SECOND_POLE_LAT = locationFilteredData33kv.get(j).getLATITUDE();
                                INT_SECOND_POLE_LON = locationFilteredData33kv.get(j).getLONGITUDE();
                                INT_SECOND_POLE_ID = locationFilteredData33kv.get(j).getREF_ID();
                                INT_SECOND_LINE_TYPE = locationFilteredData33kv.get(j).getLINE_TYPE();
                                INT_SECOND_POLE_TYPE = locationFilteredData33kv.get(j).getPOLE_TYPE();
                                INT_SECOND_POLE_HEIGHT = locationFilteredData33kv.get(j).getPOLE_HEIGHT();
                                INT_SECOND_POLE_SUPPORT = locationFilteredData33kv.get(j).getPOLE_SUPPORT();
                                INT_SECOND_PIN_CUT = locationFilteredData33kv.get(j).getPIN_CUT();
                                INT_SECOND_POLE_CONCREATE = locationFilteredData33kv.get(j).getPOLE_CONCRETE();
                                INT_SECOND_POLE_STRUCTURE = locationFilteredData33kv.get(j).getPOLE_STRUCTURE();
                                INT_SECOND_POLE_CONFIG = locationFilteredData33kv.get(j).getPOLE_CONFIG();
                            }
                        }
                    }
                    if (isMatched) {
                        String latitude1 = String.valueOf(myWGS84Point.getX());
                        String longitude2 = String.valueOf(myWGS84Point.getY());
                        openSurveyDialog33KV(mapView, motionEvent, latitude1, longitude2);
                        payDialog.dismiss();
                    } else {
                        Toast.makeText(EstimatesMap.this, "Invalid Poles", Toast.LENGTH_SHORT).show();

                    }


                }


            }
        });

        payDialog.show();
    }

    //28-01-2025 New Change UI

    private void openMoreInfoDialogIntermediatePole() {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.more_info_dialog_11);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);

        TextView header_2 = payDialog.findViewById(R.id.header_2);
        TextView header_1 = payDialog.findViewById(R.id.header_1);
        TextView desc_2 = payDialog.findViewById(R.id.new_bay_desc);
        TextView desc_1 = payDialog.findViewById(R.id.line_extension_desc);


        for (int i = 0; i < SurveyList.infoList.size(); i++) {
            if (SurveyList.infoList.get(i).getFEATURE().equalsIgnoreCase("INTERMEDIATE POLE")) {
                header_1.setVisibility(View.VISIBLE);
                desc_1.setVisibility(View.VISIBLE);
                header_1.setText(SurveyList.infoList.get(i).getFEATURE());
                desc_1.setText(SurveyList.infoList.get(i).getDESCRIPTION());
            }
        }

        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

    }

    private void openMoreInfoDialogIsSameFeeder() {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.more_info_dialog_11);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);

        TextView header_2 = payDialog.findViewById(R.id.header_2);
        TextView header_1 = payDialog.findViewById(R.id.header_1);
        TextView desc_2 = payDialog.findViewById(R.id.new_bay_desc);
        TextView desc_1 = payDialog.findViewById(R.id.line_extension_desc);


        for (int i = 0; i < SurveyList.infoList.size(); i++) {
            if (SurveyList.infoList.get(i).getFEATURE().equalsIgnoreCase("EXISTING FEEDER SURVEY")
                    && SurveyList.infoList.get(i).getFEATURE_SUB_TYPE().equalsIgnoreCase("OTHER FEEDER")) {

                header_1.setVisibility(View.VISIBLE);
                desc_1.setVisibility(View.VISIBLE);

                header_1.setText(SurveyList.infoList.get(i).getFEATURE_SUB_TYPE());
                desc_1.setText(SurveyList.infoList.get(i).getDESCRIPTION());
            }

            if (SurveyList.infoList.get(i).getFEATURE().equalsIgnoreCase("EXISTING FEEDER SURVEY")
                    && SurveyList.infoList.get(i).getFEATURE_SUB_TYPE().equalsIgnoreCase("SAME FEEDER")) {

                header_2.setVisibility(View.VISIBLE);
                desc_2.setVisibility(View.VISIBLE);

                header_2.setText(SurveyList.infoList.get(i).getFEATURE_SUB_TYPE());
                desc_2.setText(SurveyList.infoList.get(i).getDESCRIPTION());
            }
        }

        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

    }

    private void openExistingSurveyDialogForLT(MotionEvent motionEvent, MapView mapView) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.existing_dialog);
        payDialog.getWindow().setLayout(width, (int) (height * 0.6));
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        TextView tv_same_dtr = payDialog.findViewById(R.id.tv_same_dtr);
        tv_same_dtr.setText("Is Location belongs to same DTR?*");
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView log_info = payDialog.findViewById(R.id.log_info);
        Spinner sp_survey_type = payDialog.findViewById(R.id.sp_is_location_exists);
        Spinner sp_same_feeder_location = payDialog.findViewById(R.id.sp_same_feeder_location);
        LinearLayout layout_same_other = payDialog.findViewById(R.id.layout_same_other);
        LinearLayout layout_voltage_level = payDialog.findViewById(R.id.layout_voltage_level);
        Spinner sp_layout_voltage_level = payDialog.findViewById(R.id.sp_layout_voltage_level);
        CheckBox ch_int_pole_proposed = payDialog.findViewById(R.id.ch_int_pole_proposed);
        RelativeLayout ch_int_pole_proposed_layout = payDialog.findViewById(R.id.ch_int_pole_proposed_layout);
        ImageView more_info_int_pole = payDialog.findViewById(R.id.more_info_int_pole);

        ImageView more_info_is_same_feeder = payDialog.findViewById(R.id.more_info_is_same_feeder);

//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
//            List<String> lineSurveyType = new ArrayList<>();
//            lineSurveyType.add("PROPOSED");
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lineSurveyType);
//            sp_survey_type.setAdapter(adapter);
//        }
        more_info_is_same_feeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIsSameFeeder();
            }
        });

        more_info_int_pole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIntermediatePole();
            }
        });


        payDialog.show();

        sp_layout_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    back_to_home.setText("Select EHT SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    back_to_home.setText("Select 33kV/11kV SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    back_to_home.setText("Select DTR Structure code");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_survey_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    log_info.setText("After tapping on SUBMIT button all existing locations nearby 500 meters from your current location will be loaded on map. Then please Tap on a Pole number on map to select existing locaton.");
                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                    log_info.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed_layout.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.GONE);
                    log_info.setText("Tap on the SUBMIT Button to start Survey");
                } else {
                    log_info.setVisibility(View.GONE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    log_info.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_same_feeder_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_same_feeder_location.getSelectedItem().toString().equals("No")) {
                    layout_voltage_level.setVisibility(View.VISIBLE);
                } else {
                    layout_voltage_level.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_survey_type.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING") && sp_same_feeder_location.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location belongs to same DTR", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING") && sp_same_feeder_location.getSelectedItem().toString().equals("No")) {
                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(EstimatesMap.this, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    payDialog.dismiss();
                    IS_CURRENT_LOCATION_SELECTED = true;
                    if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                        loadExistingPolesLTForOthers();
                        //23-12-24 Chandu Changes
                        IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                    } else {
                        if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {
                            open33KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {

                            open11KVSSNearByDialog(motionEvent, mapView);

                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {

                            openDTRs(motionEvent, mapView);
                        }


//                        layout_voltage_level.setVisibility(View.VISIBLE);
//                        open33KVSSNearByDialog(motionEvent, mapView);
//                        IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
//                        IS_CURRENT_LOCATION_OTHER_FEEDER = true;
//                        //23-12-24 Chandu Changes
//                        IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
//                        //openSurveyScreen(motionEvent, mapView);
                    }

                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {

                    if (ch_int_pole_proposed.isChecked()) {
                        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_LT = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_LT = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                        payDialog.dismiss();
                        //  openSurveyScreen(motionEvent, mapView);
                        // loadExistingPoles33ForOthers();
                        loadExistingPolesLTForOthers();

                    } else {
                        IS_CURRENT_LOCATION_SELECTED = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_LT = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        LT_POLE_SURVEY_TYPE = false;
                        openSurveyScreen(motionEvent, mapView);
                    }


                } else {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void openExistingSurveyDialog(MotionEvent motionEvent, MapView mapView) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.existing_dialog);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, (int) (height * 0.6));
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView log_info = payDialog.findViewById(R.id.log_info);
        Spinner sp_survey_type = payDialog.findViewById(R.id.sp_is_location_exists);
        Spinner sp_same_feeder_location = payDialog.findViewById(R.id.sp_same_feeder_location);
        LinearLayout layout_same_other = payDialog.findViewById(R.id.layout_same_other);
        LinearLayout layout_voltage_level = payDialog.findViewById(R.id.layout_voltage_level);
        Spinner sp_layout_voltage_level = payDialog.findViewById(R.id.sp_layout_voltage_level);
        CheckBox ch_int_pole_proposed = payDialog.findViewById(R.id.ch_int_pole_proposed);
        RelativeLayout ch_int_pole_proposed_layout = payDialog.findViewById(R.id.ch_int_pole_proposed_layout);
        ImageView more_info_int_pole = payDialog.findViewById(R.id.more_info_int_pole);
        List<String> voltage_level = new ArrayList<>();
        voltage_level.clear();
        voltage_level.add("Select");
        voltage_level.add("33KV");
        voltage_level.add("11KV");
        voltage_level.add("LT");
        SpinnerAdapter<String> adapterPoleSupport = new SpinnerAdapter<>(this, voltage_level);
        sp_layout_voltage_level.setAdapter(adapterPoleSupport);

//-------------------------------LINE SURVEY TYPE---------------------------------
        List<String> locationSurveyType = new ArrayList<>();
        locationSurveyType.clear();
        locationSurveyType.add("Select");
        locationSurveyType.add("PROPOSED");
        locationSurveyType.add("EXISTING");
        SpinnerAdapter<String> adapterLineSurveyType = new SpinnerAdapter<>(this, locationSurveyType);
        sp_survey_type.setAdapter(adapterLineSurveyType);

//-------------------------------SAME FEEDER YES OR NO---------------------------------
        List<String> sameFeederYesORNo = new ArrayList<>();
        sameFeederYesORNo.clear();
        sameFeederYesORNo.add("Select");
        sameFeederYesORNo.add("Yes");
        sameFeederYesORNo.add("No");

        SpinnerAdapter<String> sameFeederAdapter = new SpinnerAdapter<>(this, sameFeederYesORNo);
        sp_same_feeder_location.setAdapter(sameFeederAdapter);


        ImageView more_info_is_same_feeder = payDialog.findViewById(R.id.more_info_is_same_feeder);
        more_info_is_same_feeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIsSameFeeder();
            }
        });

        more_info_int_pole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIntermediatePole();
            }
        });


        payDialog.show();

        sp_survey_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    log_info.setText("After tapping on SUBMIT button all existing locations nearby 500 meters from your current location will be loaded on map. Then please Tap on a Pole number on map to select existing locaton.");
                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                    log_info.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed_layout.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.GONE);
                    log_info.setText("Tap on the SUBMIT Button to start Survey");
                } else {
                    log_info.setVisibility(View.GONE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    log_info.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_same_feeder_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_same_feeder_location.getSelectedItem().toString().equals("No")) {
                    layout_voltage_level.setVisibility(View.VISIBLE);
                } else {
                    layout_voltage_level.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_layout_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    back_to_home.setText("Select EHT SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    back_to_home.setText("Select 33kV/11kV SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    back_to_home.setText("Select DTR Structure code");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ch_int_pole_proposed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    CustomDialog.showMessageDialog(EstimatesMap.this, "Pay attention",
                            "Are you sure want to proceed with intermediate Pole?", EST_NO, GIS_ID, 2, false, "Yes", "Cancel", ch_int_pole_proposed);

                    //openAlertDialogForIntermediatePoleCheckProposed(ch_int_pole_proposed);
                }
            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_survey_type.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING") && sp_same_feeder_location.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location belongs to same feeder", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING") && sp_same_feeder_location.getSelectedItem().toString().equals("No")) {
                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(EstimatesMap.this, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    payDialog.dismiss();
                    IS_CURRENT_LOCATION_SELECTED = true;
                    if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                        loadExistingPoles33ForOthers();
                        //23-12-24 Chandu Changes
                        IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                    } else {
                        if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {

                            open33KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {

                            open11KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {

                            openDTRs(motionEvent, mapView);
                        }


//                        layout_voltage_level.setVisibility(View.VISIBLE);
//                        open33KVSSNearByDialog(motionEvent, mapView);
//                        IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
//                        IS_CURRENT_LOCATION_OTHER_FEEDER = true;
//                        //23-12-24 Chandu Changes
//                        IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
//                        //openSurveyScreen(motionEvent, mapView);
                    }

                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {

                    if (ch_int_pole_proposed.isChecked()) {
                        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33 = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                        payDialog.dismiss();
                        //  openSurveyScreen(motionEvent, mapView);
                        loadExistingPoles33ForOthers();

                    } else {
                        IS_CURRENT_LOCATION_SELECTED = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        openSurveyScreen(motionEvent, mapView);
                    }


                } else {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private void openExistingSurveyDialog_33KV_PROPOSED(MotionEvent motionEvent, MapView mapView) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.existing_dialog);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, (int) (height * 0.6));
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView log_info = payDialog.findViewById(R.id.log_info);
        Spinner sp_survey_type = payDialog.findViewById(R.id.sp_is_location_exists);
        Spinner sp_same_feeder_location = payDialog.findViewById(R.id.sp_same_feeder_location);
        LinearLayout layout_same_other = payDialog.findViewById(R.id.layout_same_other);
        LinearLayout layout_voltage_level = payDialog.findViewById(R.id.layout_voltage_level);
        Spinner sp_layout_voltage_level = payDialog.findViewById(R.id.sp_layout_voltage_level);
        CheckBox ch_int_pole_proposed = payDialog.findViewById(R.id.ch_int_pole_proposed);
        RelativeLayout ch_int_pole_proposed_layout = payDialog.findViewById(R.id.ch_int_pole_proposed_layout);
        ImageView more_info_int_pole = payDialog.findViewById(R.id.more_info_int_pole);

        ImageView more_info_is_same_feeder = payDialog.findViewById(R.id.more_info_is_same_feeder);
        more_info_is_same_feeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIsSameFeeder();
            }
        });

//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
//            List<String> lineSurveyType = new ArrayList<>();
//            lineSurveyType.add("PROPOSED");
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lineSurveyType);
//            sp_survey_type.setAdapter(adapter);
//        }

        more_info_int_pole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIntermediatePole();
            }
        });


        payDialog.show();

        sp_survey_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.VISIBLE);
                    // log_info.setText("After tapping on SUBMIT button all existing locations nearby 500 meters from your current location will be loaded on map. Then please Tap on a Pole number on map to select existing locaton.");
                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                    log_info.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    layout_same_other.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.GONE);
                    log_info.setText("Tap on the SUBMIT Button to start Survey");
                } else {
                    log_info.setVisibility(View.GONE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.GONE);
                    log_info.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_layout_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    back_to_home.setText("Select EHT SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    back_to_home.setText("Select 33kV/11kV SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    back_to_home.setText("Select DTR Structure code");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_survey_type.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(EstimatesMap.this, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    payDialog.dismiss();
                    IS_CURRENT_LOCATION_SELECTED = true;
                    if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                        loadExistingPoles33ForOthers();
                        //23-12-24 Chandu Changes
                        IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                    } else {
                        if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {

                            open33KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {

                            open11KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {

                            openDTRs(motionEvent, mapView);
                        }


//                        layout_voltage_level.setVisibility(View.VISIBLE);
//                        open33KVSSNearByDialog(motionEvent, mapView);
//                        IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
//                        IS_CURRENT_LOCATION_OTHER_FEEDER = true;
//                        //23-12-24 Chandu Changes
//                        IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
//                        //openSurveyScreen(motionEvent, mapView);
                    }

                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {

                    if (ch_int_pole_proposed.isChecked()) {
                        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33 = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                        payDialog.dismiss();
                        //  openSurveyScreen(motionEvent, mapView);
                        loadExistingPoles33ForOthers();

                    } else {
                        IS_CURRENT_LOCATION_SELECTED = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        openSurveyScreen(motionEvent, mapView);
                    }


                } else {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private void open33KVSSNearByDialog(MotionEvent e, MapView mapView) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog_estimates);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT EHT SS !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" Nearby EHT SS")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(EstimatesMap.this);
                    searchableSpinner.setWindowTitle("SELECT EHT SS");
                    ArrayList<String> feedersList = new ArrayList<>();
                    List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(EstimatesMap.this).getUserMasterData();
                    String office_id = loginDataResponses.get(0).getDIV_ID();
                    System.out.println("OFFICE ID IN ESTIMATES : " + office_id);
                    for (int i = 0; i < EstimatesHome.NEW_NEAR_BY_33_KV.size(); i++) {
                        System.out.println("SEC ID IN ESTIMATES : " + EstimatesHome.NEW_NEAR_BY_33_KV.get(i).getDIVISION_ID());
                        if (EstimatesHome.NEW_NEAR_BY_33_KV.get(i).getDIVISION_ID().equals(office_id)) {
                            System.out.println("SAME SECTION DATA : " + EstimatesHome.NEW_NEAR_BY_33_KV.get(i).getEHT_SS_CODE());
                            feedersList.add(EstimatesHome.NEW_NEAR_BY_33_KV.get(i).getEHT_SS_NAME()
                                    + "~" + EstimatesHome.NEW_NEAR_BY_33_KV.get(i).getEHT_SS_CODE());
                        }
                    }
                    searchableSpinner.setSpinnerListItems(feedersList);
                    // searchableSpinner.setSpinnerListItems(EstimatesHome.NEW_nearBy33kvSS);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {

                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            // edt_text.setText(selectedString);
                            selectedSSCode33KV = selectedString.split("~")[1];
                            open33KVFeederMasterDialogNew(e, mapView);
                        }
                    });

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(EstimatesMap.this);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    searchableSpinner.setSpinnerListItems(EstimatesHome.NEW_total33kvSS);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            selectedSSCode33KV = selectedString.split("~")[1];
                            open33KVFeederMasterDialogNew(e, mapView);
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

    private void open33KVFeederMasterDialogNew(MotionEvent e, MapView mapView) {
        SearchableSpinner searchableSpinner = new SearchableSpinner(EstimatesMap.this);
        searchableSpinner.setWindowTitle("SELECT FEEDER");
        ArrayList<String> filteredList = new ArrayList<>();
        for (int i = 0; i < EstimatesHome.nearBy33kvModelsSS.size(); i++) {
            if (selectedSSCode33KV.equals(EstimatesHome.nearBy33kvModelsSS.get(i).getEHT_SS_CODE())) {
                filteredList.add(EstimatesHome.nearBy33kvModelsSS.get(i).getFEEDER_CODE_33KV() + "~" + EstimatesHome.nearBy33kvModelsSS.get(i).getFEEDER_NAME_33KV());
                System.out.println("FEEDERS_DATA_33KV--->" + EstimatesHome.nearBy33kvModelsSS.get(i).getFEEDER_CODE_33KV() + "~" + EstimatesHome.nearBy33kvModelsSS.get(i).getFEEDER_NAME_33KV());
            }
        }
        searchableSpinner.setSpinnerListItems(filteredList);
        searchableSpinner.show();
        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, String selectedString) {
                if (selectedString.split("~")[0].equalsIgnoreCase(WorkInvolmentsActivity.CLICKED_FEEDER_ID)) {
                    openAlertDialogForOtherFeeder("Cannot put other feeder as current survey feeder");
                } else {
                    SELECTED_FEEDER_ID = selectedString;
                    IS_SAME_EXISTING_FEEDER = true;
                    LT_POLE_SURVEY_TYPE = true;
                    POLE_SURVEY_TYPE_11KV = true;
                    openSurveyScreen(e, mapView);
                }


            }
        });
    }

    private void open11KVSSNearByDialog(MotionEvent motionEvent, MapView mapView) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog_estimates_11kv);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
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
                    SearchableSpinner searchableSpinner = new SearchableSpinner(EstimatesMap.this);
                    searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                    ArrayList<String> feedersList = new ArrayList<>();
                    List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(EstimatesMap.this).getUserMasterData();
                    String office_id = loginDataResponses.get(0).getOFFICE_ID();
                    System.out.println("OFFICE ID IN ESTIMATES : " + office_id);
                    for (int i = 0; i < EstimatesHome.NEAR_BY_11KV_SS_MODELS.size(); i++) {
                        System.out.println("SEC ID IN ESTIMATES : " + EstimatesHome.NEAR_BY_11KV_SS_MODELS.get(i).getSEC_ID());
                        if (EstimatesHome.NEAR_BY_11KV_SS_MODELS.get(i).getSEC_ID().equals(office_id)) {
                            System.out.println("SAME SECTION DATA : " + EstimatesHome.NEAR_BY_11KV_SS_MODELS.get(i).getSUB_STATION_ID() + " " + feedersList.size());
                            feedersList.add(EstimatesHome.NEAR_BY_11KV_SS_MODELS.get(i).getSUB_STATION_NAME()
                                    + "~" + EstimatesHome.NEAR_BY_11KV_SS_MODELS.get(i).getSUB_STATION_ID());
                        }
                    }
                    searchableSpinner.setSpinnerListItems(feedersList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            selectedSSCode11KV = selectedString.split("~")[1];
                            open11KVFeederMasterDialogNew(motionEvent, mapView);
                        }
                    });

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(EstimatesMap.this);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    searchableSpinner.setSpinnerListItems(EstimatesHome.NEW_total11kvSS);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            //  edt_text.setText(selectedString);
                            selectedSSCode11KV = selectedString.split("~")[1];
                            open11KVFeederMasterDialogNew(motionEvent, mapView);
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


    private void open11KVFeederMasterDialogNew(MotionEvent motionEvent, MapView mapView) {
        SearchableSpinner searchableSpinner = new SearchableSpinner(EstimatesMap.this);
        searchableSpinner.setWindowTitle("SELECT FEEDER");
        ArrayList<String> filteredList = new ArrayList<>();
        for (int i = 0; i < EstimatesHome.nearBy11kvModelsSS.size(); i++) {
            if (selectedSSCode11KV.equals(EstimatesHome.nearBy11kvModelsSS.get(i).getSUB_STATION_ID())) {
                filteredList.add(EstimatesHome.nearBy11kvModelsSS.get(i).getFEEDER_ID() + "~" + EstimatesHome.nearBy11kvModelsSS.get(i).getFEEDER_NAME());
            }
        }
        searchableSpinner.setSpinnerListItems(filteredList);
        searchableSpinner.show();
        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, String selectedString) {
                SELECTED_FEEDER_ID = selectedString;
                IS_SAME_EXISTING_FEEDER = true;
                LT_POLE_SURVEY_TYPE = true;
                POLE_SURVEY_TYPE_11KV = true;
                openSurveyScreen(motionEvent, mapView);

                //  edt_text.setText(selectedString);

            }
        });
    }


    private void openSurveyLocationDialog(String voltageLeval, MotionEvent motionEvent, MapView mapView, String feederType) {
        boolean matchedFeeder = false;
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.location_type_dialog);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, (int) (height * 0.6));
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Spinner spinner = payDialog.findViewById(R.id.sp_is_location_exists);
        Spinner sp_exsting_connecting_point = payDialog.findViewById(R.id.sp_exsting_connecting_point);
        TextView submit = payDialog.findViewById(R.id.btn_submit);
        TextView log_info = payDialog.findViewById(R.id.log_info);
        LinearLayout layout = payDialog.findViewById(R.id.layout_existing_connecting_point);
        LinearLayout pole_survey_layout = payDialog.findViewById(R.id.pole_survey_type);
        Spinner sp_location_survey_type = payDialog.findViewById(R.id.sp_location_survey_type);
        Spinner sp_same_feeder_location = payDialog.findViewById(R.id.sp_same_feeder_location);
        LinearLayout layout_same_other = payDialog.findViewById(R.id.layout_same_other);
        LinearLayout layout_voltage_level = payDialog.findViewById(R.id.layout_voltage_level);
        Spinner sp_layout_voltage_level = payDialog.findViewById(R.id.sp_layout_voltage_level);
        RelativeLayout ch_int_pole_proposed_layout = payDialog.findViewById(R.id.ch_int_pole_proposed_layout);
        CheckBox ch_int_pole_proposed = payDialog.findViewById(R.id.ch_int_pole_proposed);
        List<String> voltage_level = new ArrayList<>();
        voltage_level.clear();
        voltage_level.add("Select");
        voltage_level.add("33KV");
        voltage_level.add("11KV");
        voltage_level.add("LT");
        SpinnerAdapter<String> adapterPoleSupport = new SpinnerAdapter<>(this, voltage_level);
        sp_layout_voltage_level.setAdapter(adapterPoleSupport);

        //-------------------------------LINE SURVEY TYPE---------------------------------
        List<String> locationSurveyType = new ArrayList<>();
        locationSurveyType.clear();
        locationSurveyType.add("Select");
        locationSurveyType.add("PROPOSED");
        locationSurveyType.add("EXISTING");

        SpinnerAdapter<String> adapterLineSurveyType = new SpinnerAdapter<>(this, locationSurveyType);
        spinner.setAdapter(adapterLineSurveyType);

        //-------------------------------SAME FEEDER YES OR NO---------------------------------
        List<String> sameFeederYesORNo = new ArrayList<>();
        sameFeederYesORNo.clear();
        sameFeederYesORNo.add("Select");
        sameFeederYesORNo.add("Yes");
        sameFeederYesORNo.add("No");

        SpinnerAdapter<String> sameFeederAdapter = new SpinnerAdapter<>(this, sameFeederYesORNo);
        sp_same_feeder_location.setAdapter(sameFeederAdapter);


        //-------------------------------LINE SURVEY TYPE2---------------------------------
        List<String> locationType = new ArrayList<>();
        locationType.clear();
        locationType.add("Select");
        locationType.add("PROPOSED");
        locationType.add("EXISTING");

        SpinnerAdapter<String> adapterlocationType = new SpinnerAdapter<>(this, locationType);
        sp_location_survey_type.setAdapter(adapterlocationType);

        sp_same_feeder_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_same_feeder_location.getSelectedItem().toString().equals("No")) {
                    layout_voltage_level.setVisibility(View.VISIBLE);
                } else {
                    layout_voltage_level.setVisibility(View.GONE);
                    submit.setText("Submit");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ch_int_pole_proposed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CustomDialog.showMessageDialog(EstimatesMap.this, "Pay attention",
                            "Are you sure want to proceed with intermediate Pole?", EST_NO, GIS_ID, 2, false, "Yes", "Cancel", ch_int_pole_proposed);
                    // openAlertDialogForIntermediatePoleCheckProposed(ch_int_pole_proposed);
                } else {

                }
            }
        });

        sp_location_survey_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!sp_location_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    if (sp_location_survey_type.getSelectedItem().toString().equalsIgnoreCase("EXISTING")) {
                        log_info.setVisibility(View.VISIBLE);
                        layout_same_other.setVisibility(View.GONE);

                        if (feederType.equalsIgnoreCase("Proposed")) {
                            layout_same_other.setVisibility(View.GONE);
                            layout_voltage_level.setVisibility(View.VISIBLE);
                        } else {
                            layout_same_other.setVisibility(View.VISIBLE);
                        }

                        ch_int_pole_proposed_layout.setVisibility(View.GONE);
                        ch_int_pole_proposed.setVisibility(View.GONE);
                        log_info.setText("After tapping on SUBMIT button all existing locations nearby 500 meters from your current location will be loaded on map. Then please Tap on a Pole number on map to select existing locaton.");
                    } else if (sp_location_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                        log_info.setVisibility(View.VISIBLE);
                        layout_same_other.setVisibility(View.GONE);
                        if (feederType.equalsIgnoreCase("Proposed")) {
                            ch_int_pole_proposed_layout.setVisibility(View.GONE);
                            ch_int_pole_proposed.setVisibility(View.GONE);
                        } else {
                            ch_int_pole_proposed_layout.setVisibility(View.VISIBLE);
                            ch_int_pole_proposed.setVisibility(View.VISIBLE);
                        }
                        layout_voltage_level.setVisibility(View.GONE);
                        log_info.setText("Tap on the SUBMIT Button to start Survey");
                        submit.setText("Submit");
                    } else {
                        log_info.setVisibility(View.GONE);
                        layout_same_other.setVisibility(View.GONE);
                        ch_int_pole_proposed_layout.setVisibility(View.GONE);
                        ch_int_pole_proposed.setVisibility(View.GONE);
                        log_info.setText("");
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_exsting_connecting_point.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_exsting_connecting_point.getSelectedItem() != null) {
                    SELECTED_LOCATION_NUMBER = sp_exsting_connecting_point.getSelectedItem().toString().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> ugpsccList = new ArrayList<>();
        ugpsccList.add("New Location");

        boolean showMappedLocation = false;

        if (voltageLeval.equalsIgnoreCase("11KV")) {

            if (is33KVSelected && list33KV != null && !list33KV.isEmpty()) {
                showMappedLocation = true;
            }
            // 11KV feeder logic: need 2 different feeder_ids (not clicked)
            int feedersWithPoles = 0;
            Set<String> uniqueFeederIds = new HashSet<>();

            for (int j = 0; j < list11KV.size(); j++) {
                String feederId = list11KV.get(j).getFEEDER_ID();
                uniqueFeederIds.add(feederId);

                int poleCount = Integer.parseInt(list11KV.get(j).getPOLE_NO());
                if (poleCount > 0) feedersWithPoles++;

                if (!WorkInvolmentsActivity.CLICKED_FEEDER_ID.equalsIgnoreCase(feederId)) {
                    showMappedLocation = true;
                }
            }

            if (isLTSelected && listLT != null && !listLT.isEmpty()) {
                showMappedLocation = true;
            }

            if (showMappedLocation) {
                ugpsccList.add("Mapped Location");
            }

        } else if (voltageLeval.equalsIgnoreCase("LT")) {
            if (
                    (is33KVSelected && list33KV != null && !list33KV.isEmpty()) ||
                            (is11KVSelected && list11KV != null && !list11KV.isEmpty())
            ) {
                showMappedLocation = true;

            }
            if (showMappedLocation) {
                ugpsccList.add("Mapped Location");
            }

        } else if (voltageLeval.equalsIgnoreCase("33KV")) {
            ugpsccList.add("Mapped Location");
        }


        SpinnerAdapter<String> adapter = new SpinnerAdapter<>(EstimatesMap.this, ugpsccList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem() != null) {
                    SELECTED_LOCATION_TYPE = spinner.getSelectedItem().toString().trim();
                }
                if (SELECTED_LOCATION_TYPE.equalsIgnoreCase("Mapped Location")) {
                    layout.setVisibility(View.VISIBLE);
                    setMappedPoleLocations(sp_exsting_connecting_point);
                    pole_survey_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.GONE);
                    layout_same_other.setVisibility(View.GONE);
                } else {
                    layout.setVisibility(View.GONE);
                    pole_survey_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_layout_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    submit.setText("Select EHT SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    submit.setText("Select 33kV/11kV SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    submit.setText("Select DTR Structure code");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SELECTED_LOCATION_TYPE.equalsIgnoreCase("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please select Location type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (SELECTED_LOCATION_TYPE.equalsIgnoreCase("New Location")) {
                    if (sp_location_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(EstimatesMap.this, "Please select Location type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //NEW CODE

                if (spinner.getSelectedItem().toString().equalsIgnoreCase("New Location")) {
                    if (feederType.equalsIgnoreCase("Proposed")) {
                        if (sp_location_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                            if (sp_layout_voltage_level.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(EstimatesMap.this, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                    } else {
                        if (sp_location_survey_type.getSelectedItem().toString().equals("EXISTING") && sp_same_feeder_location.getSelectedItem().toString().equals("Select")) {
                            Toast.makeText(EstimatesMap.this, "Please Select Location belongs to same feeder", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (sp_location_survey_type.getSelectedItem().toString().equals("EXISTING") && sp_same_feeder_location.getSelectedItem().toString().equals("No")) {
                            if (sp_layout_voltage_level.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(EstimatesMap.this, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    }
                }


                if (SELECTED_LOCATION_TYPE.equalsIgnoreCase("Mapped Location")) {
                    if (SELECTED_LOCATION_NUMBER.equalsIgnoreCase("Select")) {
                        Toast.makeText(EstimatesMap.this, "Please select Mapped Location number", Toast.LENGTH_SHORT).show();
                    } else {
                        payDialog.dismiss();
                        openSurveyScreen(motionEvent, mapView);
                    }
                    return;
                }

                // New Location Logic
                switch (voltageLeval.toUpperCase()) {
                    case "11KV":
                        if (feederType.equalsIgnoreCase("Existing")) {
                            if (sp_location_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                                payDialog.dismiss();
                                IS_CURRENT_LOCATION_SELECTED = true;
                                if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                                    loadExistingPoles11ForOthers();
                                } else {
                                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {
                                        open33KVSSNearByDialog(motionEvent, mapView);
                                    } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {
                                        open11KVSSNearByDialog(motionEvent, mapView);
                                    } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {
                                        openDTRs(motionEvent, mapView);
                                    }
                                }

                            } else if (sp_location_survey_type.getSelectedItem().toString().equals("PROPOSED")) {

                                if (ch_int_pole_proposed.isChecked()) {
                                    IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11 = true;
                                    IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                    IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                    IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                    payDialog.dismiss();
                                    payDialog.dismiss();
                                    loadExistingPoles11ForOthers();

                                } else {
                                    IS_CURRENT_LOCATION_SELECTED = true;
                                    IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                                    IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                    IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                    openSurveyScreen(motionEvent, mapView);
                                    payDialog.dismiss();

                                }

                            } else {
                                Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                            }
                            //openExistingSurveyDialog11(motionEvent, mapView);
                            payDialog.dismiss();
                        } else {
                            if (sp_location_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                                openSurveyScreen(motionEvent, mapView);
                            } else {
                                if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {
                                    open33KVSSNearByDialog(motionEvent, mapView);
                                } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {
                                    open11KVSSNearByDialog(motionEvent, mapView);
                                } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {
                                    openDTRs(motionEvent, mapView);
                                }
                            }


                            payDialog.dismiss();
                        }
                        break;

                    case "33KV":
                        if (feederType.equalsIgnoreCase("Existing")) {
                            if (sp_location_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                                payDialog.dismiss();
                                IS_CURRENT_LOCATION_SELECTED = true;
                                if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                                    loadExistingPoles33ForOthers();
                                } else {
                                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {
                                        open33KVSSNearByDialog(motionEvent, mapView);
                                    } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {
                                        open11KVSSNearByDialog(motionEvent, mapView);
                                    } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {
                                        openDTRs(motionEvent, mapView);
                                    }
                                }
                            } else if (sp_location_survey_type.getSelectedItem().toString().equals("PROPOSED")) {

                                if (ch_int_pole_proposed.isChecked()) {
                                    IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33 = true;
                                    IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                                    IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                                    IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                    payDialog.dismiss();
                                    payDialog.dismiss();
                                    loadExistingPoles33ForOthers();

                                } else {
                                    IS_CURRENT_LOCATION_SELECTED = true;
                                    IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                                    IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                                    IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                    openSurveyScreen(motionEvent, mapView);
                                    payDialog.dismiss();

                                }

                            } else {
                                openSurveyScreen(motionEvent, mapView);
                                //openExistingSurveyDialog_33KV_PROPOSED(motionEvent, mapView);
                                payDialog.dismiss();
                            }

                        } else {
                            if (sp_location_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                                openSurveyScreen(motionEvent, mapView);
                            } else {
                                if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {
                                    open33KVSSNearByDialog(motionEvent, mapView);
                                } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {
                                    open11KVSSNearByDialog(motionEvent, mapView);
                                } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {
                                    openDTRs(motionEvent, mapView);
                                }
                            }


                            payDialog.dismiss();
                        }

                        break;

                    case "LT":
                        if (feederType.equalsIgnoreCase("Existing")) {
                            if (sp_location_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                                payDialog.dismiss();
                                IS_CURRENT_LOCATION_SELECTED = true;
                                if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                                    loadExistingPolesLTForOthers();
                                    //23-12-24 Chandu Changes
                                    IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                                } else {
                                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {
                                        open33KVSSNearByDialog(motionEvent, mapView);
                                    } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {

                                        open11KVSSNearByDialog(motionEvent, mapView);

                                    } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {

                                        openDTRs(motionEvent, mapView);
                                    }

                                }

                            } else if (sp_location_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                                if (ch_int_pole_proposed.isChecked()) {
                                    IS_CURRENT_LOCATION_INTERMEDIATE_POLE_LT = true;
                                    IS_CURRENT_LOCATION_LINE_EXTENSION_LT = false;
                                    IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                                    IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                                    payDialog.dismiss();
                                    loadExistingPolesLTForOthers();

                                } else {
                                    openSurveyScreen(motionEvent, mapView);
                                    payDialog.dismiss();
                                }
                            }
                        } else {
                            if (sp_location_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                                openSurveyScreen(motionEvent, mapView);
                            } else {
                                if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {
                                    open33KVSSNearByDialog(motionEvent, mapView);
                                } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {
                                    open11KVSSNearByDialog(motionEvent, mapView);
                                } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {
                                    openDTRs(motionEvent, mapView);
                                }
                            }
                            payDialog.dismiss();
                        }
                        break;
                }
            }
        });


        payDialog.show();
    }

    //NEW CODE
    private void setMappedPoleLocations(Spinner sp_exsting_connecting_point) {
        Set<Integer> set_pst = new LinkedHashSet<>();
        //NEW CODE CHANGES v3.8
        System.out.println("BTN_VALUE_MAP" + BTN_VALUE);
        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE != null) {
            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                for (int i = 0; i < list33KV.size(); i++) {
                    try {
                        System.out.println("CLICKED_FEEDER_ID" + WorkInvolmentsActivity.CLICKED_FEEDER_ID);
                        System.out.println("CLICKED_FEEDER_ID--->" + list33KV.get(i).getFEEDER_ID());
                        if (!list33KV.get(i).getFEEDER_ID().equals(WorkInvolmentsActivity.CLICKED_FEEDER_ID)) {
                            int poleNumber = Integer.parseInt(list33KV.get(i).getPOLE_NO());
                            set_pst.add(poleNumber);
                        }

                    } catch (NumberFormatException e) {
                        // Handle the case where POLE_NO is not a valid integer (optional)
                        e.printStackTrace();
                    }

                }
            } else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                // Add 11KV poles excluding the clicked feeder
                for (int i = 0; i < list11KV.size(); i++) {
                    try {
                        System.out.println("CLICKED_FEEDER_ID: " + WorkInvolmentsActivity.CLICKED_FEEDER_ID);
                        System.out.println("FEEDER_ID: " + list11KV.get(i).getFEEDER_ID());
                        if (!list11KV.get(i).getFEEDER_ID().equals(WorkInvolmentsActivity.CLICKED_FEEDER_ID)) {
                            int poleNumber = Integer.parseInt(list11KV.get(i).getPOLE_NO());
                            set_pst.add(poleNumber);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                // Additionally, add all 33KV poles
                for (int i = 0; i < list33KV.size(); i++) {
                    try {
                        int poleNumber = Integer.parseInt(list33KV.get(i).getPOLE_NO());
                        set_pst.add(poleNumber);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < listLT.size(); i++) {
                    try {
                        int poleNumber = Integer.parseInt(listLT.get(i).getPOLE_NO());
                        set_pst.add(poleNumber);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else {


                //Recently Added this code as per sir suggestion 08-07-2025
                for (int i = 0; i < listLT.size(); i++) {
                    try {
                        System.out.println("CLICKED_FEEDER_ID_LT: " + WorkInvolmentsActivity.SELECTED_LT_CKT);
                        System.out.println("BTN_CLICKED_LT: " + BTN_VALUE);
                        System.out.println("FEEDER_ID_LT: " + listLT.get(i).getLT_AVAILABLE());
                        if (!listLT.get(i).getLT_AVAILABLE().equals(WorkInvolmentsActivity.SELECTED_LT_CKT)) {
                            int poleNumber = Integer.parseInt(listLT.get(i).getPOLE_NO());
                            set_pst.add(poleNumber);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < list11KV.size(); i++) {
                    try {
                        System.out.println("CLICKED_FEEDER_ID: " + WorkInvolmentsActivity.CLICKED_FEEDER_ID);
                        System.out.println("FEEDER_ID: " + list11KV.get(i).getFEEDER_ID());
                        int poleNumber = Integer.parseInt(list11KV.get(i).getPOLE_NO());
                        set_pst.add(poleNumber);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                // Additionally, add all 33KV poles
                for (int i = 0; i < list33KV.size(); i++) {
                    try {
                        int poleNumber = Integer.parseInt(list33KV.get(i).getPOLE_NO());
                        set_pst.add(poleNumber);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        List<Integer> list_pst_integers = new ArrayList<>(set_pst);
        Collections.sort(list_pst_integers);
        List<String> list_pst = new ArrayList<>();
        list_pst.add("Select");
        for (int num : list_pst_integers) {
            list_pst.add(String.valueOf(num));
        }


        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(EstimatesMap.this, R.layout.spinner_items_list, list_pst);
        sp_exsting_connecting_point.setAdapter(adapter_pst);
    }


    //28-01-2025 New Change UI
    private void openExistingSurveyDialog11(MotionEvent motionEvent, MapView mapView) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.existing_dialog);
        payDialog.getWindow().setLayout(width, (int) (height * 0.6));
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView log_info = payDialog.findViewById(R.id.log_info);
        Spinner sp_survey_type = payDialog.findViewById(R.id.sp_is_location_exists);
        Spinner sp_same_feeder_location = payDialog.findViewById(R.id.sp_same_feeder_location);
        LinearLayout layout_same_other = payDialog.findViewById(R.id.layout_same_other);
        LinearLayout layout_voltage_level = payDialog.findViewById(R.id.layout_voltage_level);
        Spinner sp_layout_voltage_level = payDialog.findViewById(R.id.sp_layout_voltage_level);
        CheckBox ch_int_pole_proposed = payDialog.findViewById(R.id.ch_int_pole_proposed);
        RelativeLayout ch_int_pole_proposed_layout = payDialog.findViewById(R.id.ch_int_pole_proposed_layout);

        ImageView more_info_int_pole = payDialog.findViewById(R.id.more_info_int_pole);
        ImageView more_info_is_same_feeder = payDialog.findViewById(R.id.more_info_is_same_feeder);
        more_info_is_same_feeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIsSameFeeder();
            }
        });

//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
//            List<String> lineSurveyType = new ArrayList<>();
//            lineSurveyType.add("PROPOSED");
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lineSurveyType);
//            sp_survey_type.setAdapter(adapter);
//        }

        more_info_int_pole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIntermediatePole();
            }
        });

        payDialog.show();

        sp_same_feeder_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_same_feeder_location.getSelectedItem().toString().equals("No")) {
                    layout_voltage_level.setVisibility(View.VISIBLE);
                } else {
                    layout_voltage_level.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_layout_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    back_to_home.setText("Select EHT SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    back_to_home.setText("Select 33kV/11kV SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    back_to_home.setText("Select DTR Structure code");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_survey_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    log_info.setText("After tapping on SUBMIT button all existing locations nearby 500 meters from your current location will be loaded on map. Then please Tap on a Pole number on map to select existing locaton.");
                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.VISIBLE);
                    ch_int_pole_proposed.setVisibility(View.VISIBLE);
                    log_info.setText("Tap on the SUBMIT Button to start Survey");
                } else {
                    log_info.setVisibility(View.GONE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    log_info.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_survey_type.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING") && sp_same_feeder_location.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location belongs to same feeder", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING") && sp_same_feeder_location.getSelectedItem().toString().equals("No")) {
                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(EstimatesMap.this, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    payDialog.dismiss();
                    IS_CURRENT_LOCATION_SELECTED = true;
                    if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                        loadExistingPoles11ForOthers();
                    } else {

                        if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {
                            open33KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {
                            open11KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {
                            openDTRs(motionEvent, mapView);
                        }

//                        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
//                        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
//                        IS_CURRENT_LOCATION_OTHER_FEEDER = true;
//                        open11KVSSNearByDialog(motionEvent, mapView);

                        // openSurveyScreen(motionEvent, mapView);
                    }

                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {

                    if (ch_int_pole_proposed.isChecked()) {
                        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11 = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        payDialog.dismiss();
                        loadExistingPoles11ForOthers();

                    } else {
                        IS_CURRENT_LOCATION_SELECTED = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        openSurveyScreen(motionEvent, mapView);
                    }

                } else {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void openExistingSurveyDialog11_PROPOSED(MotionEvent motionEvent, MapView mapView) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.existing_dialog);
        payDialog.getWindow().setLayout(width, (int) (height * 0.6));
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView log_info = payDialog.findViewById(R.id.log_info);
        Spinner sp_survey_type = payDialog.findViewById(R.id.sp_is_location_exists);
        Spinner sp_same_feeder_location = payDialog.findViewById(R.id.sp_same_feeder_location);
        LinearLayout layout_same_other = payDialog.findViewById(R.id.layout_same_other);
        LinearLayout layout_voltage_level = payDialog.findViewById(R.id.layout_voltage_level);
        Spinner sp_layout_voltage_level = payDialog.findViewById(R.id.sp_layout_voltage_level);
        CheckBox ch_int_pole_proposed = payDialog.findViewById(R.id.ch_int_pole_proposed);
        RelativeLayout ch_int_pole_proposed_layout = payDialog.findViewById(R.id.ch_int_pole_proposed_layout);

        ImageView more_info_int_pole = payDialog.findViewById(R.id.more_info_int_pole);
        ImageView more_info_is_same_feeder = payDialog.findViewById(R.id.more_info_is_same_feeder);
        more_info_is_same_feeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIsSameFeeder();
            }
        });

//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
//            List<String> lineSurveyType = new ArrayList<>();
//            lineSurveyType.add("PROPOSED");
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lineSurveyType);
//            sp_survey_type.setAdapter(adapter);
//        }


        more_info_int_pole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIntermediatePole();
            }
        });

        payDialog.show();


        sp_survey_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.VISIBLE);
                    // log_info.setText("After tapping on SUBMIT button all existing locations nearby 500 meters from your current location will be loaded on map. Then please Tap on a Pole number on map to select existing locaton.");
                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.GONE);
                    log_info.setText("Tap on the SUBMIT Button to start Survey");
                } else {
                    log_info.setVisibility(View.GONE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.GONE);
                    log_info.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_layout_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    back_to_home.setText("Select EHT SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    back_to_home.setText("Select 33kV/11kV SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    back_to_home.setText("Select DTR Structure code");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_survey_type.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(EstimatesMap.this, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    payDialog.dismiss();
                    IS_CURRENT_LOCATION_SELECTED = true;
                    if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                        loadExistingPoles11ForOthers();
                    } else {

                        if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {

                            open33KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {

                            open11KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {
                            openDTRs(motionEvent, mapView);
                        }

//                        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
//                        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
//                        IS_CURRENT_LOCATION_OTHER_FEEDER = true;
//                        open11KVSSNearByDialog(motionEvent, mapView);

                        // openSurveyScreen(motionEvent, mapView);
                    }

                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {

                    if (ch_int_pole_proposed.isChecked()) {
                        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11 = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        payDialog.dismiss();
                        loadExistingPoles11ForOthers();

                    } else {
                        IS_CURRENT_LOCATION_SELECTED = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        openSurveyScreen(motionEvent, mapView);
                    }

                } else {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void openExistingSurveyDialogForLT_PROPOSED(MotionEvent motionEvent, MapView mapView) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.existing_dialog);
        payDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        payDialog.getWindow().setLayout(width, (int) (height * 0.6));
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        TextView tv_same_dtr = payDialog.findViewById(R.id.tv_same_dtr);
        tv_same_dtr.setText("Is Location belongs to same DTR?*");
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView log_info = payDialog.findViewById(R.id.log_info);
        Spinner sp_survey_type = payDialog.findViewById(R.id.sp_is_location_exists);
        Spinner sp_same_feeder_location = payDialog.findViewById(R.id.sp_same_feeder_location);
        LinearLayout layout_same_other = payDialog.findViewById(R.id.layout_same_other);
        LinearLayout layout_voltage_level = payDialog.findViewById(R.id.layout_voltage_level);
        Spinner sp_layout_voltage_level = payDialog.findViewById(R.id.sp_layout_voltage_level);
        CheckBox ch_int_pole_proposed = payDialog.findViewById(R.id.ch_int_pole_proposed);
        RelativeLayout ch_int_pole_proposed_layout = payDialog.findViewById(R.id.ch_int_pole_proposed_layout);
        ImageView more_info_int_pole = payDialog.findViewById(R.id.more_info_int_pole);

        ImageView more_info_is_same_feeder = payDialog.findViewById(R.id.more_info_is_same_feeder);

//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
//            List<String> lineSurveyType = new ArrayList<>();
//            lineSurveyType.add("PROPOSED");
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lineSurveyType);
//            sp_survey_type.setAdapter(adapter);
//        }
        more_info_is_same_feeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIsSameFeeder();
            }
        });

        more_info_int_pole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreInfoDialogIntermediatePole();
            }
        });


        payDialog.show();

        sp_survey_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.VISIBLE);
                    // log_info.setText("After tapping on SUBMIT button all existing locations nearby 500 meters from your current location will be loaded on map. Then please Tap on a Pole number on map to select existing locaton.");
                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {
                    log_info.setVisibility(View.VISIBLE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.GONE);
                    log_info.setText("Tap on the SUBMIT Button to start Survey");

                } else {
                    log_info.setVisibility(View.GONE);
                    layout_same_other.setVisibility(View.GONE);
                    ch_int_pole_proposed_layout.setVisibility(View.GONE);
                    ch_int_pole_proposed.setVisibility(View.GONE);
                    layout_voltage_level.setVisibility(View.GONE);
                    log_info.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_layout_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    back_to_home.setText("Select EHT SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    back_to_home.setText("Select 33kV/11kV SS");
                } else if (sp_layout_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    back_to_home.setText("Select DTR Structure code");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_survey_type.getSelectedItem().toString().equals("Select")) {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    if (sp_layout_voltage_level.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(EstimatesMap.this, "Please Select Voltage level", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (sp_survey_type.getSelectedItem().toString().equals("EXISTING")) {
                    payDialog.dismiss();
                    IS_CURRENT_LOCATION_SELECTED = true;
                    if (sp_same_feeder_location.getSelectedItem().toString().equals("Yes")) {
                        loadExistingPoles11ForOthers();
                    } else {

                        if (sp_layout_voltage_level.getSelectedItem().toString().equals("33KV")) {

                            open33KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("11KV")) {

                            open11KVSSNearByDialog(motionEvent, mapView);
                        } else if (sp_layout_voltage_level.getSelectedItem().toString().equals("LT")) {

                            openDTRs(motionEvent, mapView);
                        }

//                        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
//                        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
//                        IS_CURRENT_LOCATION_OTHER_FEEDER = true;
//                        open11KVSSNearByDialog(motionEvent, mapView);

                        // openSurveyScreen(motionEvent, mapView);
                    }

                } else if (sp_survey_type.getSelectedItem().toString().equals("PROPOSED")) {

                    if (ch_int_pole_proposed.isChecked()) {
                        IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11 = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                        IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        payDialog.dismiss();
                        loadExistingPoles11ForOthers();

                    } else {
                        IS_CURRENT_LOCATION_SELECTED = true;
                        IS_CURRENT_LOCATION_LINE_EXTENSION_LT = false;
                        IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                        IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        payDialog.dismiss();
                        openSurveyScreen(motionEvent, mapView);
                    }

                } else {
                    Toast.makeText(EstimatesMap.this, "Please Select Location Survey type", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void openDTRs(MotionEvent motionEvent, MapView mapView) {
        SearchableSpinner searchableSpinner = new SearchableSpinner(EstimatesMap.this);
        searchableSpinner.setWindowTitle("SELECT DTR CODE");
        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            searchableSpinner.setSpinnerListItems(EstimatesHome.all_dtrs_rdss);
        } else {
            searchableSpinner.setSpinnerListItems(EstimatesHome.allDtrs);
        }

        searchableSpinner.show();
        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, String selectedString) {
                SELECTED_FEEDER_ID = selectedString;
                IS_SAME_EXISTING_FEEDER = true;
                LT_POLE_SURVEY_TYPE = true;
                POLE_SURVEY_TYPE_11KV = true;
                openSurveyScreen(motionEvent, mapView);
            }
        });
    }

    int countOF33KVPoles = 0;

    private void loadExistingPoles33ForOthers() {

        locationFilteredData33kv.clear();
        currentLocationGpsTracker = new GpsTraker(this);

        ArrayList<NewModel> tempList = WorkInvolmentsActivity.existingPolesList;
        ArrayList<NewModel> data33kv = new ArrayList<>();

        // ---------------- FILTER 33KV ----------------
        for (NewModel model : tempList) {
            if ("33KV".equalsIgnoreCase(model.getVOLTAGE_LEVEL())) {
                data33kv.add(model);
            }
        }

        if (data33kv.isEmpty()) {
            Toast.makeText(this, "No 33KV poles found", Toast.LENGTH_SHORT).show();
            return;
        }

        // ---------------- FEEDER SELECTION ----------------
        String extFeederID = "";

        switch (WorkInvolmentsActivity.CLICKED_FEEDER) {
            case "FEEDER 1":
                extFeederID = WorkInvolmentsActivity.feeder_id1_33kv;
                break;
            case "FEEDER 2":
                extFeederID = WorkInvolmentsActivity.feeder_id2_33kv;
                break;
            case "FEEDER 3":
                extFeederID = WorkInvolmentsActivity.feeder_id3_33kv;
                break;
            case "FEEDER 4":
                extFeederID = WorkInvolmentsActivity.feeder_id4_33kv;
                break;
            case "FEEDER 5":
                extFeederID = WorkInvolmentsActivity.feeder_id5_33kv;
                break;
        }

        if (extFeederID == null || extFeederID.trim().isEmpty()) {
            Toast.makeText(this, "Feeder not matched", Toast.LENGTH_SHORT).show();
            return;
        }

        // ---------------- FILTER BY FEEDER + DISTANCE ----------------
        ArrayList<NewModel> filteredData33kv = new ArrayList<>();

        for (NewModel model : data33kv) {

            if (!extFeederID.equalsIgnoreCase(model.getFEEDER_ID())) {
                continue;
            }

            double distance = getDistanceBetweenPoles(
                    model.getLATITUDE(),
                    model.getLONGITUDE(),
                    currentLocationGpsTracker.getLongitude(),
                    currentLocationGpsTracker.getLatitude()
            );

            if (distance <= existingPoleDataDistance) {
                filteredData33kv.add(model);
                locationFilteredData33kv.add(model);
            }
        }

        countOF33KVPoles = filteredData33kv.size();

        // ---------------- UI ----------------
        runOnUiThread(() -> {

            tap_load_data.setVisibility(View.GONE);
            tap_select.setVisibility(View.VISIBLE);

            if (filteredData33kv.isEmpty()) {
                Toast.makeText(
                        this,
                        "Existing locations not found nearby 200 meters.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            openAlertDialogExisting(countOF33KVPoles, "33kV");

            IS_CURRENT_LOCATION_LINE_EXTENSION_33 = true;
            firstTap33 = true;

            drawPolesOnMap33(filteredData33kv);
            drawPolylines33(filteredData33kv);
        });
    }


//    private void openFeederCompleteSurveyDialog(String Feeder_no) {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Complete Survey!!")
//                .setMessage("Are you sure you want to complete the 11kV " + Feeder_no + " Survey?")
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        boolean update11kVSurveyStatusNEW = false;
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 1")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder1Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 2")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder2Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 3")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder3Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 4")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder4Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 5")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder5Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 6")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder6Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 7")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder7Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 8")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder8Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 9")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder9Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 10")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder10Status(EST_NO, true, GIS_ID);
//                        }
//
//                        if (update11kVSurveyStatusNEW) {
//                            boolean update11kVSurveyStatusAll = databaseEstimates.update11kvSurveyStatusNEW(EST_NO, GIS_ID);
//                            Toast.makeText(EstimatesMap.this, Feeder_no + " Survey Completed Successfully..", Toast.LENGTH_SHORT).show();
//
//                            Intent intent;
//                            if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
//                                intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
//                            } else {
//                                intent = new Intent(EstimatesMap.this, OfflineHome.class);
//                            }
//
//                            //Intent intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
//                            intent.putExtra("EST_NO", EST_NO);
//                            intent.putExtra("GIS_ID", GIS_ID);
//                            intent.putExtra("33KV", is33KVSelected);
//                            intent.putExtra("11KV", is11KVSelected);
//                            intent.putExtra("LT", isLTSelected);
//                            intent.putExtra("MAX_LENGTH", max_length);
//                            intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
//                            intent.putExtra("WORK_ORDER", wo_no);
//                            intent.putExtra("EST_NATURE", EST_NATURE);
//                            intent.putExtra("EST_TYPE", EST_TYPE);
//
//                            //28-01-2025 New Change
//                            intent.putExtra("PURPOSE", PURPOSE);
//                            int maxFdrIndex11KV = -1;
//                            for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
//                                if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
//                                    int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
//                                    if (currentFdrIndex > maxFdrIndex11KV) {
//                                        maxFdrIndex11KV = currentFdrIndex;
//                                    }
//                                }
//                            }
//                            intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
//                            intent.putExtra("Model", model);
//                            intent.putExtra("FROM", "11KVComplete");
//                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                            String json = gson.toJson(model);
//
//
//                            //    Log.d("MODELCHECK", "onCreate: " + json);
//
//                            startActivity(intent);
//                        } else {
//                            dialog.cancel();
//                            Toast.makeText(EstimatesMap.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        }
//    }

    private void openFeederCompleteSurveyDialog(String Feeder_no) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.question));
        // Set Title & Message
        title.setText("Complete Survey!!");
        message.setText("Are you sure you want to complete the 11kV " + Feeder_no + " Survey?");

        // Show both buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("YES");
        cancelBtn.setText("NO");

        // YES Click
        okBtn.setOnClickListener(v -> {

            boolean update11kVSurveyStatusNEW = false;

            if (Feeder_no.equalsIgnoreCase("FEEDER 1")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder1Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 2")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder2Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 3")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder3Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 4")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder4Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 5")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder5Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 6")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder6Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 7")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder7Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 8")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder8Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 9")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder9Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 10")) {
                update11kVSurveyStatusNEW = databaseEstimates.update11KVFeeder10Status(EST_NO, true, GIS_ID);
            }

            if (update11kVSurveyStatusNEW) {

                databaseEstimates.update11kvSurveyStatusNEW(EST_NO, GIS_ID);

                Toast.makeText(EstimatesMap.this,
                        Feeder_no + " Survey Completed Successfully..",
                        Toast.LENGTH_SHORT).show();

                Intent intent;

                if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                    intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
                } else {
                    intent = new Intent(EstimatesMap.this, OfflineHome.class);
                }

                intent.putExtra("EST_NO", EST_NO);
                intent.putExtra("GIS_ID", GIS_ID);
                intent.putExtra("33KV", is33KVSelected);
                intent.putExtra("11KV", is11KVSelected);
                intent.putExtra("LT", isLTSelected);
                intent.putExtra("MAX_LENGTH", max_length);
                intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
                intent.putExtra("WORK_ORDER", wo_no);
                intent.putExtra("EST_NATURE", EST_NATURE);
                intent.putExtra("EST_TYPE", EST_TYPE);
                intent.putExtra("PURPOSE", PURPOSE);
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                    intent.putExtra("FEEDERS", FEEDERS);
                    intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                }

                int maxFdrIndex11KV = -1;
                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                        int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
                        if (currentFdrIndex > maxFdrIndex11KV) {
                            maxFdrIndex11KV = currentFdrIndex;
                        }
                    }
                }

                intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
                intent.putExtra("Model", model);
                intent.putExtra("FROM", "11KVComplete");

                startActivity(intent);
                dialog.dismiss();

            } else {
                Toast.makeText(EstimatesMap.this,
                        "Something Went Wrong..",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // NO Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

    private void openFeederCompleteSurveyDialog33(String Feeder_no) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.question));
        // Set Title & Message
        title.setText("Complete Survey!!");
        message.setText("Are you sure you want to complete the 33kV " + Feeder_no + " Survey?");

        // Show both buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("YES");
        cancelBtn.setText("NO");

        // YES Click
        okBtn.setOnClickListener(v -> {

            boolean updateStatus = false;

            if (Feeder_no.equalsIgnoreCase("FEEDER 1")) {
                updateStatus = databaseEstimates.update33KVFeeder1Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 2")) {
                updateStatus = databaseEstimates.update33KVFeeder2Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 3")) {
                updateStatus = databaseEstimates.update33KVFeeder3Status(EST_NO, true, GIS_ID);
            }
            if (Feeder_no.equalsIgnoreCase("FEEDER 4")) {
                updateStatus = databaseEstimates.update33KVFeeder4Status(EST_NO, true, GIS_ID);
            }

            if (updateStatus) {

                databaseEstimates.update33kvSurveyStatusNEW(EST_NO, GIS_ID);

                Toast.makeText(EstimatesMap.this,
                        Feeder_no + " Survey Completed Successfully..",
                        Toast.LENGTH_SHORT).show();

                Intent intent;

                if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                    intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
                } else {
                    intent = new Intent(EstimatesMap.this, OfflineHome.class);
                }

                intent.putExtra("EST_NO", EST_NO);
                intent.putExtra("GIS_ID", GIS_ID);
                intent.putExtra("33KV", is33KVSelected);
                intent.putExtra("11KV", is11KVSelected);
                intent.putExtra("LT", isLTSelected);
                intent.putExtra("MAX_LENGTH", max_length);
                intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
                intent.putExtra("WORK_ORDER", wo_no);
                intent.putExtra("EST_NATURE", EST_NATURE);
                intent.putExtra("EST_TYPE", EST_TYPE);
                intent.putExtra("PURPOSE", PURPOSE);

                int maxFdrIndex11KV = -1;
                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                        int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
                        if (currentFdrIndex > maxFdrIndex11KV) {
                            maxFdrIndex11KV = currentFdrIndex;
                        }
                    }
                }

                intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
                intent.putExtra("Model", model);
                intent.putExtra("FROM", "11KVComplete");

                startActivity(intent);
                dialog.dismiss();

            } else {
                Toast.makeText(EstimatesMap.this,
                        "Something Went Wrong..",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // NO Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

//    private void openFeederCompleteSurveyDialog33(String Feeder_no) {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Complete Survey!!")
//                .setMessage("Are you sure you want to complete the 33kV " + Feeder_no + " Survey?")
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        boolean update11kVSurveyStatusNEW = false;
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 1")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update33KVFeeder1Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 2")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update33KVFeeder2Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 3")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update33KVFeeder3Status(EST_NO, true, GIS_ID);
//                        }
//                        if (Feeder_no.equalsIgnoreCase("FEEDER 4")) {
//                            update11kVSurveyStatusNEW = databaseEstimates.update33KVFeeder4Status(EST_NO, true, GIS_ID);
//                        }
//
//                        if (update11kVSurveyStatusNEW) {
//                            boolean update11kVSurveyStatusAll = databaseEstimates.update33kvSurveyStatusNEW(EST_NO, GIS_ID);
//                            Toast.makeText(EstimatesMap.this, Feeder_no + " Survey Completed Successfully..", Toast.LENGTH_SHORT).show();
//
//                            Intent intent;
//                            if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
//                                intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
//                            } else {
//                                intent = new Intent(EstimatesMap.this, OfflineHome.class);
//                            }
//
//                            //Intent intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
//                            intent.putExtra("EST_NO", EST_NO);
//                            intent.putExtra("GIS_ID", GIS_ID);
//                            intent.putExtra("33KV", is33KVSelected);
//                            intent.putExtra("11KV", is11KVSelected);
//                            intent.putExtra("LT", isLTSelected);
//                            intent.putExtra("MAX_LENGTH", max_length);
//                            intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
//                            intent.putExtra("WORK_ORDER", wo_no);
//                            intent.putExtra("EST_NATURE", EST_NATURE);
//                            intent.putExtra("EST_TYPE", EST_TYPE);
//
//
//                            int maxFdrIndex11KV = -1;
//                            for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
//                                if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
//                                    int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
//                                    if (currentFdrIndex > maxFdrIndex11KV) {
//                                        maxFdrIndex11KV = currentFdrIndex;
//                                    }
//                                }
//                            }
//                            intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
//                            intent.putExtra("Model", model);
//                            intent.putExtra("FROM", "11KVComplete");
//
//                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                            String json = gson.toJson(model);
//
//
//                            //    Log.d("MODELCHECK", "onCreate: " + json);
//
//                            startActivity(intent);
//                        } else {
//                            dialog.cancel();
//                            Toast.makeText(EstimatesMap.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        }
//    }


    // NEW CODE FOR OFFLINE
    // ArcGIS GraphicsOverlay collections must only be changed on the main/UI thread.
    public void clearMapGraphics() {
        if (isMapActivityDestroyed) return;

        if (Looper.myLooper() != Looper.getMainLooper()) {
            mainHandler.post(this::clearMapGraphics);
            return;
        }

        if (mv == null || mv.getGraphicsOverlays() == null) return;

        try {
            // Work on a copy so the renderer is not affected by collection changes.
            List<GraphicsOverlay> overlays = new ArrayList<>(mv.getGraphicsOverlays());
            for (GraphicsOverlay overlay : overlays) {
                if (overlay != null && overlay.getGraphics() != null) {
                    overlay.getGraphics().clear();
                }
            }
        } catch (Exception e) {
            Log.e("EstimatesMap", "Error while clearing map graphics", e);
        }
    }

    private void drawPolylines11(List<NewModel> poles) {

        if (poles == null || poles.size() < 2) return;

        if (mGraphicsOverlay == null) {
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
        }

        for (NewModel surveyVo : poles) {

            PointCollection polylinePoints =
                    new PointCollection(SpatialReferences.getWgs84());

            // -------- FROM POLE --------
            if (surveyVo.getFROM_POLE() != null) {
                for (NewModel model : poles) {
                    if (surveyVo.getFROM_POLE().equalsIgnoreCase(model.getPOLE_NO())) {

                        polylinePoints.add(new Point(
                                Double.parseDouble(model.getLATITUDE()),
                                Double.parseDouble(model.getLONGITUDE()),
                                SpatialReferences.getWgs84()
                        ));
                        break;
                    }
                }
            }

            // -------- TO POLE --------
            polylinePoints.add(new Point(
                    Double.parseDouble(surveyVo.getLATITUDE()),
                    Double.parseDouble(surveyVo.getLONGITUDE()),
                    SpatialReferences.getWgs84()
            ));

            if (polylinePoints.size() < 2) continue;

            // -------- DRAW LINE --------
            Polyline polyline = new Polyline(polylinePoints);
            SimpleLineSymbol lineSymbol =
                    new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2f);

            mGraphicsOverlay.getGraphics().add(
                    new Graphic(polyline, lineSymbol)
            );

            // -------- MIDPOINT --------
            Point start = polylinePoints.get(0);
            Point end = polylinePoints.get(1);

            Point midpoint = new Point(
                    (start.getX() + end.getX()) / 2,
                    (start.getY() + end.getY()) / 2,
                    SpatialReferences.getWgs84()
            );

            // -------- LABEL TEXT --------
            String feederId = surveyVo.getFEEDER_ID() == null ? "" : surveyVo.getFEEDER_ID();
            String poleNo = surveyVo.getPOLE_NO();

            String labelText;
            if ("DTR POLE".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) {
                labelText = feederId + "-" + poleNo + "-DTR";
            } else {
                labelText = feederId + "-" + poleNo;
            }

            // -------- DRAW LABEL --------
            TextSymbol label = new TextSymbol(
                    12,
                    labelText,
                    Color.WHITE,
                    TextSymbol.HorizontalAlignment.CENTER,
                    TextSymbol.VerticalAlignment.MIDDLE
            );

            mGraphicsOverlay.getGraphics().add(
                    new Graphic(midpoint, label)
            );
        }
    }


    private void drawPolesOnMap11(List<NewModel> poles) {

        graphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(graphicsOverlay);

        // -------- SET VIEWPOINT ONCE --------
        NewModel first = poles.get(0);
        Point vpPoint = new Point(
                Double.parseDouble(first.getLATITUDE()),
                Double.parseDouble(first.getLONGITUDE()),
                SpatialReferences.getWgs84()
        );

        map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
        mv.setMap(map);

        for (NewModel surveyVo : poles) {

            Point pt = new Point(
                    Double.parseDouble(surveyVo.getLATITUDE()),
                    Double.parseDouble(surveyVo.getLONGITUDE()),
                    SpatialReferences.getWgs84()
            );

            // -------- MARKER --------
            SimpleMarkerSymbol symbol =
                    new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 5);
            graphicsOverlay.getGraphics().add(new Graphic(pt, symbol));

            // -------- POLE NUMBER --------
            TextSymbol poleNoSymbol = new TextSymbol(
                    12,
                    surveyVo.getPOLE_NO(),
                    Color.WHITE,
                    TextSymbol.HorizontalAlignment.LEFT,
                    TextSymbol.VerticalAlignment.BOTTOM
            );
            poleNoSymbol.setOffsetY(10);
            graphicsOverlay.getGraphics().add(new Graphic(pt, poleNoSymbol));

            // -------- CONFIG LABEL --------
            if ("HT Service-Pole".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) {
                TextSymbol cfg = new TextSymbol(
                        12,
                        "11kV HT Service",
                        Color.RED,
                        TextSymbol.HorizontalAlignment.RIGHT,
                        TextSymbol.VerticalAlignment.TOP
                );
                graphicsOverlay.getGraphics().add(new Graphic(pt, cfg));
            }

            // -------- CALLOUT --------
            TextView calloutContent = new TextView(this);
            calloutContent.setText(surveyVo.getPOLE_NO());

            Callout callout = mv.getCallout();
            callout.setLocation(pt);
            callout.setContent(calloutContent);
            callout.show();
        }
    }


    private void drawPolylines33(List<NewModel> poles) {

        if (poles == null || poles.size() < 2) return;

        if (mGraphicsOverlay == null) {
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
        }

        for (NewModel surveyVo : poles) {

            PointCollection polylinePoints =
                    new PointCollection(SpatialReferences.getWgs84());

            // -------- FROM POLE --------
            if (surveyVo.getFROM_POLE() != null) {
                for (NewModel model : poles) {
                    if (surveyVo.getFROM_POLE().equalsIgnoreCase(model.getPOLE_NO())) {

                        polylinePoints.add(new Point(
                                Double.parseDouble(model.getLATITUDE()),
                                Double.parseDouble(model.getLONGITUDE()),
                                SpatialReferences.getWgs84()
                        ));
                        break;
                    }
                }
            }

            // -------- TO POLE --------
            polylinePoints.add(new Point(
                    Double.parseDouble(surveyVo.getLATITUDE()),
                    Double.parseDouble(surveyVo.getLONGITUDE()),
                    SpatialReferences.getWgs84()
            ));

            if (polylinePoints.size() < 2) continue;

            // -------- DRAW LINE --------
            Polyline polyline = new Polyline(polylinePoints);
            SimpleLineSymbol lineSymbol =
                    new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2f);

            mGraphicsOverlay.getGraphics().add(
                    new Graphic(polyline, lineSymbol)
            );

            // -------- MIDPOINT --------
            Point start = polylinePoints.get(0);
            Point end = polylinePoints.get(1);

            Point midpoint = new Point(
                    (start.getX() + end.getX()) / 2,
                    (start.getY() + end.getY()) / 2,
                    SpatialReferences.getWgs84()
            );

            // -------- 33KV LABEL --------
            TextSymbol kvLabel = new TextSymbol(
                    12,
                    "33KV",
                    Color.RED,
                    TextSymbol.HorizontalAlignment.CENTER,
                    TextSymbol.VerticalAlignment.MIDDLE
            );

            mGraphicsOverlay.getGraphics().add(
                    new Graphic(midpoint, kvLabel)
            );

            // -------- FEEDER-POLE LABEL --------
            TextSymbol feederLabel = new TextSymbol(
                    15,
                    surveyVo.getFEEDER_ID() + "-" + surveyVo.getPOLE_NO(),
                    Color.WHITE,
                    TextSymbol.HorizontalAlignment.LEFT,
                    TextSymbol.VerticalAlignment.TOP
            );
            feederLabel.setOffsetY(20);

            mGraphicsOverlay.getGraphics().add(
                    new Graphic(midpoint, feederLabel)
            );
        }
    }


    private void drawPolesOnMap33(List<NewModel> filteredData33kv) {

        graphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(graphicsOverlay);

        for (NewModel surveyVo : filteredData33kv) {

            Point ptWgs84 = new Point(
                    Double.parseDouble(surveyVo.getLATITUDE()),
                    Double.parseDouble(surveyVo.getLONGITUDE()),
                    SpatialReferences.getWgs84()
            );

            // -------- MARKER --------
            SimpleMarkerSymbol symbol =
                    new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 5);

            graphicsOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));

            // -------- CONFIG LABEL --------
            if ("33/11kv SS-Bay".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) {
                TextSymbol ssLabel = new TextSymbol(
                        12,
                        "33/11kV SS",
                        Color.CYAN,
                        TextSymbol.HorizontalAlignment.RIGHT,
                        TextSymbol.VerticalAlignment.TOP
                );
                graphicsOverlay.getGraphics().add(new Graphic(ptWgs84, ssLabel));
            }

            // -------- POLE NUMBER --------
            TextSymbol poleNoSymbol = new TextSymbol(
                    12,
                    surveyVo.getPOLE_NO(),
                    Color.WHITE,
                    TextSymbol.HorizontalAlignment.LEFT,
                    TextSymbol.VerticalAlignment.BOTTOM
            );
            poleNoSymbol.setOffsetY(10);

            graphicsOverlay.getGraphics().add(new Graphic(ptWgs84, poleNoSymbol));

            // -------- CALLOUT --------
            TextView calloutContent = new TextView(this);
            calloutContent.setText(surveyVo.getPOLE_NO());

            Callout callout = mv.getCallout();
            callout.setLocation(ptWgs84);
            callout.setContent(calloutContent);
            callout.show();
        }
    }


    int countOf33kvExistingPoles = 0;

    private void loadExistingPoles33() {
        currentLocationGpsTracker = new GpsTraker(this);
        locationFilteredData33kv.clear();
        //tap_load_data.setVisibility(View.GONE);
        // tap_select.setVisibility(View.VISIBLE);
        ArrayList<NewModel> tempList = WorkInvolmentsActivity.existingPolesList;

        ArrayList<NewModel> data33kv = new ArrayList<>();

        for (int j = 0; j < tempList.size(); j++) {
            if (tempList.get(j).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                data33kv.add(tempList.get(j));
            }
        }
        runOnUiThread(this::handle33KvFeederUI);
        if (data33kv.size() > 0) {

            Map<String, NewModel> poleDataMap = new HashMap<>();
            ArrayList<String> poleNumbers = new ArrayList<>();


            String extFeederID = "";

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                extFeederID = WorkInvolmentsActivity.feeder_id1_33kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                extFeederID = WorkInvolmentsActivity.feeder_id2_33kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                extFeederID = WorkInvolmentsActivity.feeder_id3_33kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                extFeederID = WorkInvolmentsActivity.feeder_id4_33kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                extFeederID = WorkInvolmentsActivity.feeder_id5_33kv;
            }


            ArrayList<NewModel> filteredData33kv = new ArrayList<>();
            // Filter poles within 2 km
            for (NewModel model : data33kv) {

                if (extFeederID.equalsIgnoreCase(model.getFEEDER_ID())) {
                    double distance = getDistanceBetweenPoles33(
                            model.getLATITUDE(),
                            model.getLONGITUDE(),
                        /*myWGS84Point.getX(),
                        myWGS84Point.getY()*/
                            currentLocationGpsTracker.getLongitude(),
                            currentLocationGpsTracker.getLatitude()
                    );

                    if (distance <= existingPoleDataDistance) {
                        poleNumbers.add(model.getPOLE_NO());
                        poleDataMap.put(model.getPOLE_NO(), model);
                        filteredData33kv.add(model);
                        locationFilteredData33kv.add(model);
                    }
                }


            }
            countOf33kvExistingPoles = locationFilteredData33kv.size();

            if (countOf33kvExistingPoles > 0) {
                openAlertDialog(countOf33kvExistingPoles, "33kV");
            }

            // UI work
            runOnUiThread(() -> {
                tap_load_data.setVisibility(View.GONE);
                tap_select.setVisibility(View.VISIBLE);

                handle33KvFeederUI();

                if (!filteredData33kv.isEmpty()) {
                    openAlertDialog(filteredData33kv.size(), "33kV");
                    drawPolesOnMap33(filteredData33kv);
                    drawPolylines33(filteredData33kv);
                } else {
                    Toast.makeText(this,
                            "Existing locations not found nearby 500 meters.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            if (!filteredData33kv.isEmpty()) {
                firstTap33 = true;

                // Collect graphics locally on the background thread first
                List<Graphic> newGraphics = new ArrayList<>();

                for (NewModel surveyVo : filteredData33kv) {
                    PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    for (int i = 0; i < filteredData33kv.size(); i++) {
                        if (surveyVo.getFROM_POLE().equalsIgnoreCase(filteredData33kv.get(i).getPOLE_NO())) {
                            polylinePoints.add(new Point(Double.parseDouble(filteredData33kv.get(i).getLATITUDE()), Double.parseDouble(filteredData33kv.get(i).getLONGITUDE())));
                        }
                    }
                    polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
                    Polyline polyline = new Polyline(polylinePoints);

                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2.0f);
                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                    newGraphics.add(polylineGraphic);

                    if (polylinePoints.size() >= 2) {
                        Point startPoint = polylinePoints.get(0);
                        Point endPoint = polylinePoints.get(1);

                        double midpointX = (startPoint.getX() + endPoint.getX()) / 2;
                        double midpointY = (startPoint.getY() + endPoint.getY()) / 2;

                        Point midpoint = new Point(midpointX, midpointY, SpatialReferences.getWgs84());

                        TextSymbol textSymbol = new TextSymbol(12, "33KV", Color.RED, TextSymbol.HorizontalAlignment.RIGHT, TextSymbol.VerticalAlignment.TOP);

                        Graphic labelGraphic = new Graphic(midpoint, textSymbol);
                        newGraphics.add(labelGraphic);
                    }
                }

                // Only touch the GraphicsOverlay on the main thread
                if (!newGraphics.isEmpty()) {
                    runOnUiThread(() -> mGraphicsOverlay.getGraphics().addAll(newGraphics));
                }
            }

//            if (!filteredData33kv.isEmpty()) {
//                firstTap33 = true;
//                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//
//                for (NewModel surveyVo : filteredData33kv) {
//                    PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//                    // polylinePoints.add(new Point(Double.parseDouble(surveyVo.getFROM_POLE_LATITUDE()), Double.parseDouble(surveyVo.getFROM_POLE_LONGITUDE())));
//
//                    for (int i = 0; i < filteredData33kv.size(); i++) {
//                        if (surveyVo.getFROM_POLE().equalsIgnoreCase(filteredData33kv.get(i).getPOLE_NO())) {
//                            polylinePoints.add(new Point(Double.parseDouble(filteredData33kv.get(i).getLATITUDE()), Double.parseDouble(filteredData33kv.get(i).getLONGITUDE())));
//                        }
//                    }
//
//
//                    polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
//                    Polyline polyline = new Polyline(polylinePoints);
//
//                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2.0f);
//                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
//                    mGraphicsOverlay.getGraphics().add(polylineGraphic);
//
//                    if (polylinePoints.size() >= 2) {
//                        Point startPoint = polylinePoints.get(0);
//                        Point endPoint = polylinePoints.get(1);
//
//                        double midpointX = (startPoint.getX() + endPoint.getX()) / 2;
//                        double midpointY = (startPoint.getY() + endPoint.getY()) / 2;
//
//                        Point midpoint = new Point(midpointX, midpointY, SpatialReferences.getWgs84());
//
//                        TextSymbol textSymbol = new TextSymbol(12, "33KV", Color.RED, TextSymbol.HorizontalAlignment.RIGHT, TextSymbol.VerticalAlignment.TOP);
//
//                        Graphic labelGraphic = new Graphic(midpoint, textSymbol);
//                        mGraphicsOverlay.getGraphics().add(labelGraphic);
//                    }
//
//
//                }
//            }


        }
    }

    int countOf11kvpoles = 0;

    private void loadExistingPoles11() {
        locationFilteredData11kv.clear();
        currentLocationGpsTracker = new GpsTraker(this);
        //tap_load_data.setVisibility(View.GONE);
        //tap_select.setVisibility(View.VISIBLE);
        ArrayList<NewModel> tempList = WorkInvolmentsActivity.existingPolesList;
        //  System.out.println("11 KV EXT SIZE IN MAP : " + tempList.size());
        ArrayList<NewModel> data33kv = new ArrayList<>();

        for (int j = 0; j < tempList.size(); j++) {
            if (tempList.get(j).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV") || tempList.get(j).getVOLTAGE_LEVEL().equalsIgnoreCase("6.3 KV")) {
                data33kv.add(tempList.get(j));
            }
        }


        if (data33kv.size() > 0) {

            Map<String, NewModel> poleDataMap = new HashMap<>();
            ArrayList<String> poleNumbers = new ArrayList<>();

           /* android.graphics.Point clickedPoint = new android.graphics.Point(latX, longiY);
            Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
            Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());*/

            ArrayList<NewModel> filteredData33kv = new ArrayList<>();

            String extFeederID = "";

            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                extFeederID = WorkInvolmentsActivity.feeder_id1_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                extFeederID = WorkInvolmentsActivity.feeder_id2_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                extFeederID = WorkInvolmentsActivity.feeder_id3_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                extFeederID = WorkInvolmentsActivity.feeder_id4_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                extFeederID = WorkInvolmentsActivity.feeder_id5_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 6")) {
                extFeederID = WorkInvolmentsActivity.feeder_id6_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 7")) {
                extFeederID = WorkInvolmentsActivity.feeder_id7_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 8")) {
                extFeederID = WorkInvolmentsActivity.feeder_id8_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 9")) {
                extFeederID = WorkInvolmentsActivity.feeder_id9_11kv;
            }
            if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 10")) {
                extFeederID = WorkInvolmentsActivity.feeder_id10_11kv;
            }


            //  System.out.println("EXT FEEDER ID : " + extFeederID);

            // Filter poles within 2 km
            for (NewModel model : data33kv) {

                if (extFeederID.equalsIgnoreCase(model.getFEEDER_ID())) {
                    double distance = getDistanceBetweenPoles11(
                            model.getLATITUDE(),
                            model.getLONGITUDE(),


                            currentLocationGpsTracker.getLongitude(),
                            currentLocationGpsTracker.getLatitude()
                            /*myWGS84Point.getX(),
                            myWGS84Point.getY()*/
                    );

                    if (distance <= existingPoleDataDistance) {
                        //System.out.println("CURRENT LAT AND CURRENT LNG" + " "+ currentLocationGpsTracker.getLongitude()+ " " +currentLocationGpsTracker.getLatitude());

                        poleNumbers.add(model.getPOLE_NO());
                        poleDataMap.put(model.getPOLE_NO(), model);
                        filteredData33kv.add(model);
                        locationFilteredData11kv.add(model);
                    }
                }

            }
            countOf11kvpoles = locationFilteredData11kv.size();

            // 🔵 UI WORK ONLY
            runOnUiThread(() -> {

                tap_load_data.setVisibility(View.GONE);
                tap_select.setVisibility(View.VISIBLE);

                handle11KvFeederUI();

                if (!filteredData33kv.isEmpty()) {
                    openAlertDialog(countOf11kvpoles, "11kV");
                    drawPolesOnMap11(filteredData33kv);
                    drawPolylines11(filteredData33kv);
                } else {
                    Toast.makeText(this,
                            "Existing locations not found nearby 500 meters.",
                            Toast.LENGTH_SHORT).show();
                }
            });

//            if (countOf11kvpoles > 0) {
//                openAlertDialog(countOf11kvpoles, "11kV");
//            }


//            if (!filteredData33kv.isEmpty()) {
//
//                firstTap11 = true;
//                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//                for (NewModel surveyVo : filteredData33kv) {
//                    ptWgs84 = new Point(
//                            Double.parseDouble(surveyVo.getLATITUDE()),
//                            Double.parseDouble(surveyVo.getLONGITUDE()),
//                            414,
//                            SpatialReferences.getWgs84()
//                    );
//                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
//                    map.setInitialViewpoint(vp);
//                    mv.setMap(map);
//                    graphicsOverlay = new GraphicsOverlay();
//                    mv.getGraphicsOverlays().add(graphicsOverlay);
//
//                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 5);
//                    String labelText = "";
//                    int labelColor = Color.RED;
//
//                    if (surveyVo.getIS_EXISTING_LOCATION_NEW().equalsIgnoreCase("Existing")) {
//                        if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")) {
//                            labelText = "33/11kV SS";
//                            labelColor = Color.CYAN;
//                        }
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")) {
//                        labelText = "33/11kV SS";
//                        labelColor = Color.CYAN;
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("EHT SS-Bay")) {
//                        labelText = "EHT";
//                        labelColor = Color.BLUE;
//                    } else if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("HT Service-Pole")) {
//                        labelText = "11kV HT Service";
//                        labelColor = this.getResources().getColor(R.color.red);
//                    }
//
//                    Graphic graphic = new Graphic(ptWgs84, symbol);
//                    graphicsOverlay.getGraphics().add(graphic);
//
//                    if (!labelText.isEmpty()) {
//                        TextSymbol textSymbol = new TextSymbol(
//                                12,
//                                labelText,
//                                labelColor,
//                                TextSymbol.HorizontalAlignment.RIGHT,
//                                TextSymbol.VerticalAlignment.TOP
//                        );
//
//                        Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);
//                        graphicsOverlay.getGraphics().add(labelGraphic);
//                    }
//
//                    graphicsOverlay.setLabelsEnabled(true);
//
//                    calloutContent = new TextView(getApplicationContext());
//                    calloutContent.setTextColor(Color.BLACK);
//                    calloutContent.setSingleLine();
//                    calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
//                    mCallout = mv.getCallout();
//                    mCallout.setLocation(ptWgs84);
//                    mCallout.setContent(calloutContent);
//                    mCallout.show();
//
//                    //Display Text
//
//                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
//                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
//                    TextSymbol absoluteText;
//                    if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR POLE")) {
//                        absoluteText = new TextSymbol(15, surveyVo.getFEEDER_ID() + "-" + String.valueOf(surveyVo.getPOLE_NO()) + "-" + surveyVo.getPOLE_CONFIG(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
//                                TextSymbol.VerticalAlignment.TOP);
//                    } else {
//                        absoluteText = new TextSymbol(15, surveyVo.getFEEDER_ID() + "-" + String.valueOf(surveyVo.getPOLE_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
//                                TextSymbol.VerticalAlignment.TOP);
//                    }
//
//                    // create a text symbol for elevation mode
//
//                    absoluteText.setOffsetY(20);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
//                    mv.getGraphicsOverlays().add(absoluteOverlay);
//
//                }
//                // Add polyline functionality
//                for (NewModel surveyVo : filteredData33kv) {
//                    PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//                    for (int i = 0; i < filteredData33kv.size(); i++) {
//                        if (surveyVo.getFROM_POLE().equalsIgnoreCase(filteredData33kv.get(i).getPOLE_NO())) {
//                            polylinePoints.add(new Point(Double.parseDouble(filteredData33kv.get(i).getLATITUDE()), Double.parseDouble(filteredData33kv.get(i).getLONGITUDE())));
//                        }
//                    }
//
//                    polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
//                    Polyline polyline = new Polyline(polylinePoints);
//
//                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2.0f);
//                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
//                    mGraphicsOverlay.getGraphics().add(polylineGraphic);
//
//                    if (polylinePoints.size() >= 2) {
//                        Point startPoint = polylinePoints.get(0);
//                        Point endPoint = polylinePoints.get(1);
//
//                        double midpointX = (startPoint.getX() + endPoint.getX()) / 2;
//                        double midpointY = (startPoint.getY() + endPoint.getY()) / 2;
//
//                        Point midpoint = new Point(midpointX, midpointY, SpatialReferences.getWgs84());
//
//                        TextSymbol textSymbol = new TextSymbol(12, "11KV", Color.WHITE, TextSymbol.HorizontalAlignment.RIGHT, TextSymbol.VerticalAlignment.TOP);
//                        Graphic labelGraphic = new Graphic(midpoint, textSymbol);
//                        mGraphicsOverlay.getGraphics().add(labelGraphic);
//                    }
//                }
//            }


        }
    }

    int countOf11KVExistingPoles = 0;

    private void loadExistingPoles11ForOthers() {

        locationFilteredData11kv.clear();
        currentLocationGpsTracker = new GpsTraker(this);

        ArrayList<NewModel> tempList = WorkInvolmentsActivity.existingPolesList;
        ArrayList<NewModel> data11kv = new ArrayList<>();

        // ---------------- FILTER 11KV ----------------
        for (NewModel model : tempList) {
            if ("11 KV".equalsIgnoreCase(model.getVOLTAGE_LEVEL()) || "6.3 KV".equalsIgnoreCase(model.getVOLTAGE_LEVEL())) {
                data11kv.add(model);
            }
        }

        if (data11kv.isEmpty()) {
            Toast.makeText(this, "No 11KV poles found", Toast.LENGTH_SHORT).show();
            return;
        }

        // ---------------- FEEDER SELECTION ----------------
        String extFeederID = "";

        switch (WorkInvolmentsActivity.CLICKED_FEEDER) {
            case "FEEDER 1":
                extFeederID = WorkInvolmentsActivity.feeder_id1_11kv;
                break;
            case "FEEDER 2":
                extFeederID = WorkInvolmentsActivity.feeder_id2_11kv;
                break;
            case "FEEDER 3":
                extFeederID = WorkInvolmentsActivity.feeder_id3_11kv;
                break;
            case "FEEDER 4":
                extFeederID = WorkInvolmentsActivity.feeder_id4_11kv;
                break;
            case "FEEDER 5":
                extFeederID = WorkInvolmentsActivity.feeder_id5_11kv;
                break;
            case "FEEDER 6":
                extFeederID = WorkInvolmentsActivity.feeder_id6_11kv;
                break;
            case "FEEDER 7":
                extFeederID = WorkInvolmentsActivity.feeder_id7_11kv;
                break;
            case "FEEDER 8":
                extFeederID = WorkInvolmentsActivity.feeder_id8_11kv;
                break;
            case "FEEDER 9":
                extFeederID = WorkInvolmentsActivity.feeder_id9_11kv;
                break;
            case "FEEDER 10":
                extFeederID = WorkInvolmentsActivity.feeder_id10_11kv;
                break;

        }

        if (extFeederID == null || extFeederID.trim().isEmpty()) {
            Toast.makeText(this, "Feeder not matched", Toast.LENGTH_SHORT).show();
            return;
        }

        // ---------------- FILTER BY FEEDER + DISTANCE ----------------
        ArrayList<NewModel> filteredData11kv = new ArrayList<>();

        for (NewModel model : data11kv) {

            if (!extFeederID.equalsIgnoreCase(model.getFEEDER_ID())) continue;

            double distance = getDistanceBetweenPoles11(
                    model.getLATITUDE(),
                    model.getLONGITUDE(),
                    currentLocationGpsTracker.getLongitude(),
                    currentLocationGpsTracker.getLatitude()
            );

            if (distance <= existingPoleDataDistance) {
                filteredData11kv.add(model);
                locationFilteredData11kv.add(model);
            }
        }

        countOf11KVExistingPoles = filteredData11kv.size();

        // ---------------- UI ----------------
        runOnUiThread(() -> {

            tap_load_data.setVisibility(View.GONE);
            tap_select.setVisibility(View.VISIBLE);

            if (filteredData11kv.isEmpty()) {
                Toast.makeText(
                        this,
                        "Existing locations not found nearby 200 meters.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            openAlertDialogExisting(countOf11KVExistingPoles, "11kV");

            IS_CURRENT_LOCATION_LINE_EXTENSION_11 = true;
            firstTap11 = true;

            drawPolesOnMap11(filteredData11kv);
            drawPolylines11(filteredData11kv);
        });
    }


    private void openSurveyScreen(MotionEvent e, MapView mapView) {

        // Load survey list
        listAll = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);

        // Convert tap to map point
        int x = (int) e.getX();
        int y = (int) e.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());

        // Setup map
        Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
        map.setInitialViewpoint(vp);
        mv.setMap(map);

        graphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(graphicsOverlay);

        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 12);
        graphic = new Graphic(myWGS84Point, symbol);
        graphicsOverlay.getGraphics().add(graphic);

        // Callout text view
        TextView calloutContent = new TextView(getApplicationContext());
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setSingleLine();
        calloutContent.setFocusable(true);

        // ---------------- SAFE POLE NUMBER LOGIC ----------------
        int maxPoleNumber = 0;

        if (listAll != null && !listAll.isEmpty()) {
            for (int i = 0; i < listAll.size(); i++) {
                String poleStr = listAll.get(i).getPOLE_NO();

                if (poleStr != null && poleStr.matches("\\d+")) {
                    int currentPole = Integer.parseInt(poleStr);
                    if (currentPole > maxPoleNumber) {
                        maxPoleNumber = currentPole;
                    }
                }
            }
        }

        maxPoleNumber++; // Next pole
        calloutContent.setText(String.valueOf(maxPoleNumber));
        // ---------------------------------------------------------

        gpsTraker.getLocation();

        // Distance from current location
        Location currentStartPoint = new Location("locA");
        currentStartPoint.setLatitude(gpsTraker.getLatitude());
        currentStartPoint.setLongitude(gpsTraker.getLongitude());

        Location currentEndPoint = new Location("locA");
        currentEndPoint.setLatitude(myWGS84Point.getY());
        currentEndPoint.setLongitude(myWGS84Point.getX());

        currentDistance = currentStartPoint.distanceTo(currentEndPoint);

        // ---------------- SAFE DISTANCE FOR LAST POLE ----------------
        lastPoleDistance = 0;

        if (listAll != null && !listAll.isEmpty()) {

            String lastLatStr = listAll.get(listAll.size() - 1).getLATITUDE();
            String lastLonStr = listAll.get(listAll.size() - 1).getLONGITUDE();

            boolean validLastLatLon =
                    lastLatStr != null && lastLonStr != null &&
                            lastLatStr.matches("-?\\d+(\\.\\d+)?") &&
                            lastLonStr.matches("-?\\d+(\\.\\d+)?");

            if (BTN_VALUE.equalsIgnoreCase("LT")) {

                if (listLT == null || listLT.isEmpty()) {
                    double DTR_LAT = 0;
                    double DTR_LON = 0;

                    for (int j = 0; j < WorkInvolmentsActivity.existingDtrDetails.size(); j++) {
                        if (WorkInvolmentsActivity.SELECTED_LT_CKT.split("_LTCKT_")[0]
                                .equalsIgnoreCase(WorkInvolmentsActivity.existingDtrDetails.get(j).getDTR_STRUCTURE_CODE())) {

                            String lat = WorkInvolmentsActivity.existingDtrDetails.get(j).getLATITUDE();
                            String lon = WorkInvolmentsActivity.existingDtrDetails.get(j).getLONGITUDE();

                            if (lat != null && lon != null &&
                                    lat.matches("-?\\d+(\\.\\d+)?") &&
                                    lon.matches("-?\\d+(\\.\\d+)?")) {

                                DTR_LAT = Double.parseDouble(lat);
                                DTR_LON = Double.parseDouble(lon);
                            }
                        }
                    }

                    Location start = new Location("LT");
                    start.setLatitude(DTR_LAT);
                    start.setLongitude(DTR_LON);

                    Location end = new Location("LT");
                    end.setLatitude(myWGS84Point.getY());
                    end.setLongitude(myWGS84Point.getX());

                    lastPoleDistance = start.distanceTo(end);

                } else if (validLastLatLon) {

                    Location start = new Location("LT_EXIST");
                    start.setLatitude(Double.parseDouble(lastLatStr));
                    start.setLongitude(Double.parseDouble(lastLonStr));

                    Location end = new Location("LT_EXIST");
                    end.setLatitude(myWGS84Point.getY());
                    end.setLongitude(myWGS84Point.getX());

                    lastPoleDistance = start.distanceTo(end);
                }
            } else {

                if (validLastLatLon) {

                    Location start = new Location("HT");
                    start.setLatitude(Double.parseDouble(lastLatStr));
                    start.setLongitude(Double.parseDouble(lastLonStr));

                    Location end = new Location("HT");
                    end.setLatitude(myWGS84Point.getY());
                    end.setLongitude(myWGS84Point.getX());

                    lastPoleDistance = start.distanceTo(end);
                }
            }
        }
        // ------------------------------------------------------------

        // Show callout
        mCallout = mapView.getCallout();
        mCallout.setLocation(myWGS84Point);
        mCallout.setContent(calloutContent);
        mCallout.show();

        // Move to next activity
        Intent intent = new Intent(EstimatesMap.this, LocationDetails.class);
        intent.putExtra("poleLat", String.valueOf(myWGS84Point.getX()));
        intent.putExtra("poleLon", String.valueOf(myWGS84Point.getY()));
        intent.putExtra("currentLat", gpsTraker.getLatitude());
        intent.putExtra("currentLon", gpsTraker.getLongitude());
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("isEdit", false);
        intent.putExtra("is33KVSelected", is33KVSelected);
        intent.putExtra("is11KVSelected", is11KVSelected);
        intent.putExtra("isDTRSelected", isDTRSelected);
        intent.putExtra("isLTSelected", isLTSelected);
        intent.putExtra("lastPoleDistance", lastPoleDistance);
        intent.putExtra("currentDistance", currentDistance);
        intent.putExtra("BTN_VALUE", BTN_VALUE);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(selectedFdrIndex));
        intent.putExtra("Feeder_type33KV", Feeder_type33KV);
        intent.putExtra("Feeder_type11KV", Feeder_type11KV);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("IS_EDIT", IS_EDIT);
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            intent.putExtra("FEEDERS", FEEDERS);
            intent.putExtra("RDSS_TYPE", RDSS_TYPE);
        }

        if (IS_EDIT) {
            intent.putExtra("DATA_MODEL", DATA_MODEL);
        }

        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
    }


    private void showDialogWithSpinners11KV(MapView mapView, MotionEvent motionEvent, double lat, double lon, boolean intermediate) {
        //double MAX_DISTANCE_METERS = 20.0; // same as 33KV
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());

        CheckBox ch_int_pole_33kv;
        LinearLayout layout_intermediate_poll, layout_tapping_point;
        TextView tv_desc, tv_desc_for_int;

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_with_spinners, null);
        Spinner spinner1 = dialogView.findViewById(R.id.sp_tapping_point);

        Spinner intPol1 = dialogView.findViewById(R.id.sp_intermediate_poll_1);
        Spinner intPol2 = dialogView.findViewById(R.id.sp_intermediate_poll_2);

        layout_intermediate_poll = dialogView.findViewById(R.id.layout_intermediate_poll);
        layout_tapping_point = dialogView.findViewById(R.id.layout_tapping_point);
        ch_int_pole_33kv = dialogView.findViewById(R.id.ch_int_pole_33kv);
        tv_desc = dialogView.findViewById(R.id.tv_desc);
        tv_desc_for_int = dialogView.findViewById(R.id.tv_desc_for_int);


        if (LOCATION_COUNT_11KV_FEEDER_1 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_11KV_FEEDER_2 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_11KV_FEEDER_3 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_11KV_FEEDER_4 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_11KV_FEEDER_5 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_11KV_FEEDER_6 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 6")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }

        if (LOCATION_COUNT_11KV_FEEDER_7 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 7")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }

        if (LOCATION_COUNT_11KV_FEEDER_8 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 8")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }

        if (LOCATION_COUNT_11KV_FEEDER_9 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 9")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }

        if (LOCATION_COUNT_11KV_FEEDER_10 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 10")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }

        if (intermediate) {
            ch_int_pole_33kv.setChecked(true);
        } else {
            ch_int_pole_33kv.setVisibility(View.GONE);
        }

        ch_int_pole_33kv.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
//                ch_int_pole_33kv.setChecked(true);
//                tv_desc_for_int.setVisibility(View.VISIBLE);
//                tv_desc.setVisibility(View.GONE);
//                layout_intermediate_poll.setVisibility(View.VISIBLE);
//                layout_tapping_point.setVisibility(View.GONE);
                openAlertDialogForIntermediatePoleCheckExisting(ch_int_pole_33kv, tv_desc_for_int, tv_desc, layout_intermediate_poll, layout_tapping_point);
            } else {
                ch_int_pole_33kv.setChecked(false);
                tv_desc_for_int.setVisibility(View.GONE);
                tv_desc.setVisibility(View.VISIBLE);
                layout_intermediate_poll.setVisibility(View.GONE);
                layout_tapping_point.setVisibility(View.VISIBLE);
            }

        });


        // Map to hold pole numbers and their corresponding NewModel data
        Map<String, NewModel> poleDataMap = new HashMap<>();
        ArrayList<String> intPoleData = new ArrayList<>();
        intPoleData.add("Select");

        ArrayList<String> poleNumbers = new ArrayList<>();
        poleNumbers.add("Select");


        for (int j = 0; j < locationFilteredData11kv.size(); j++) {

            String poleLat = locationFilteredData11kv.get(j).getLATITUDE();
            String poleLon = locationFilteredData11kv.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,
                    lon
            );

            if (distance <= MAX_DISTANCE_METERS) {
                String poleNo = locationFilteredData11kv.get(j).getPOLE_NO();
                poleNumbers.add(poleNo);
                intPoleData.add(poleNo);
                poleDataMap.put(poleNo, locationFilteredData11kv.get(j));
            }
        }

        if (poleNumbers.size() == 1) {
            Toast.makeText(this, "No poles found within 100 meters", Toast.LENGTH_SHORT).show();
            return;
        }
//        for (int j = 0; j < locationFilteredData11kv.size(); j++) {
//            poleNumbers.add(locationFilteredData11kv.get(j).getPOLE_NO());
//        }


        SpinnerAdapter<String> adapter = new SpinnerAdapter<>(this, poleNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

//        for (int j = 0; j < locationFilteredData11kv.size(); j++) {
//            intPoleData.add(locationFilteredData11kv.get(j).getPOLE_NO());
//        }


        SpinnerAdapter<String> intPoleAdapter1 = new SpinnerAdapter<>(this, intPoleData);
        //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intPol1.setAdapter(intPoleAdapter1);

        intPol1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String selPole = intPol1.getSelectedItem().toString();
                    ArrayList<String> toPoleNumbers = new ArrayList<>();
                    toPoleNumbers.add("Select");
                    for (int j = 0; j < locationFilteredData11kv.size(); j++) {
                        if (selPole.equalsIgnoreCase(locationFilteredData11kv.get(j).getFROM_POLE())) {
                            toPoleNumbers.add(locationFilteredData11kv.get(j).getPOLE_NO());
                        }
                        if (selPole.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {
                            toPoleNumbers.add(locationFilteredData11kv.get(j).getFROM_POLE());

                        }
                    }

                    SpinnerAdapter<String> intPoleAdapter2 = new SpinnerAdapter<>(EstimatesMap.this, toPoleNumbers);
                    //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intPol2.setAdapter(intPoleAdapter2);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder

//                .setTitle("Mapping Of Existing Pole/Location (Tapping Location) as First Location")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("OKAY", (dialog, which) -> {

                    if (ch_int_pole_33kv.isChecked()) {
                        IS_INTERMEDIATE_POLE = true;

                        Object item1 = intPol1.getSelectedItem();
                        Object item2 = intPol2.getSelectedItem();

//                        String intermediatePoll1 = intPol1.getSelectedItem().toString();
//                        String intermediatePoll2 = intPol2.getSelectedItem().toString();
//
                        String sourceLocationPoll1 = "", sourceLocationPoll2 = "";


                        String intermediatePoll1 = (item1 != null) ? item1.toString() : "";
                        String intermediatePoll2 = (item2 != null) ? item2.toString() : "";

                        if (intermediatePoll1.isEmpty() || intermediatePoll2.isEmpty() ||
                                intermediatePoll1.equalsIgnoreCase("Select") || intermediatePoll2.equalsIgnoreCase("Select")) {
                            Toast.makeText(this, "Invalid Pole Number", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (intermediatePoll1.equalsIgnoreCase(intermediatePoll2)) {
                            Toast.makeText(this, "You have selected the same poles", Toast.LENGTH_SHORT).show();
                        } else {
                            String latitude = "";
                            String longitude = "";
                            for (int j = 0; j < locationFilteredData11kv.size(); j++) {
                                if (intermediatePoll1.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {

                                    sourceLocationPoll1 = locationFilteredData11kv.get(j).getFROM_POLE();
                                    //      System.out.println("sourcePole33KV" + sourceLocationPoll1);

                                    //23-12-24 Chandu Changes
                                    IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                }

                                if (intermediatePoll2.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {

                                    sourceLocationPoll2 = locationFilteredData11kv.get(j).getFROM_POLE();
                                    //      System.out.println("sourcePole33KV" + sourceLocationPoll2);
                                    //      System.out.println("PIN_POINT_33KV" + LINE_EXT_POLE_STRUCTURE);
                                    //23-12-24 Chandu Changes
                                    IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                                }
                            }
                            boolean isMatched = false;
                            if (intermediatePoll1.equalsIgnoreCase(sourceLocationPoll2) || intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                                isMatched = true;
                                if (intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                                    INT_FIRST_POLE = intermediatePoll2;
                                    INT_SECOND_POLE = intermediatePoll1;
                                } else {
                                    INT_FIRST_POLE = intermediatePoll1;
                                    INT_SECOND_POLE = intermediatePoll2;
                                }

                                for (int j = 0; j < locationFilteredData11kv.size(); j++) {
                                    if (INT_FIRST_POLE.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {
                                        INT_FIRST_POLE_LAT = locationFilteredData11kv.get(j).getLATITUDE();
                                        INT_FIRST_POLE_LON = locationFilteredData11kv.get(j).getLONGITUDE();
                                        INT_FIRST_POLE_ID = locationFilteredData11kv.get(j).getREF_ID();
                                        INT_FIRST_LINE_TYPE = locationFilteredData11kv.get(j).getLINE_TYPE();
                                        INT_FIRST_POLE_TYPE = locationFilteredData11kv.get(j).getPOLE_TYPE();
                                        INT_FIRST_POLE_HEIGHT = locationFilteredData11kv.get(j).getPOLE_HEIGHT();
                                        INT_FIRST_POLE_SUPPORT = locationFilteredData11kv.get(j).getPOLE_SUPPORT();
                                        INT_FIRST_PIN_CUT = locationFilteredData11kv.get(j).getPIN_CUT();
                                        INT_FIRST_POLE_CONCREATE = locationFilteredData11kv.get(j).getPOLE_CONCRETE();
                                        INT_FIRST_POLE_STRUCTURE = locationFilteredData11kv.get(j).getPOLE_STRUCTURE();
                                        INT_FIRST_POLE_CONFIG = locationFilteredData11kv.get(j).getPOLE_CONFIG();
                                        INT_FIRST_POLE_DTR_CODE = locationFilteredData11kv.get(j).getDTR_STRUCTURE_CODE();
                                        INT_FIRST_POLE_DTR_ID = locationFilteredData11kv.get(j).getDTR_ID();
                                        INT_FIRST_DTR_LOCATION = locationFilteredData11kv.get(j).getDTR_LOCATION();
                                        INT_FIRST_DTR_AVAILABLE = locationFilteredData11kv.get(j).getLT_AVAILABLE();
                                        INT_FIRST_DTR_LT_CKT_COUNT = String.valueOf(locationFilteredData11kv.get(j).getCOUNT_CKT());
                                        INT_FIRST_DTR_MAKE = locationFilteredData11kv.get(j).getDTR_MAKE();
                                        INT_FIRST_DTR_SLNO = locationFilteredData11kv.get(j).getDTR_SNO();
                                        INT_FIRST_DTR_PHASE = locationFilteredData11kv.get(j).getDTR_PHASE();
                                        INT_FIRST_DTR_CAPACITY = locationFilteredData11kv.get(j).getDTR_CAPACITY();
                                        INT_FIRST_DTR_SHIFTED = locationFilteredData11kv.get(j).getDTR_SHIFTED();
                                    }
                                    if (INT_SECOND_POLE.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {
                                        INT_SECOND_POLE_LAT = locationFilteredData11kv.get(j).getLATITUDE();
                                        INT_SECOND_POLE_LON = locationFilteredData11kv.get(j).getLONGITUDE();
                                        INT_SECOND_POLE_ID = locationFilteredData11kv.get(j).getREF_ID();
                                        INT_SECOND_LINE_TYPE = locationFilteredData11kv.get(j).getLINE_TYPE();
                                        INT_SECOND_POLE_TYPE = locationFilteredData11kv.get(j).getPOLE_TYPE();
                                        INT_SECOND_POLE_HEIGHT = locationFilteredData11kv.get(j).getPOLE_HEIGHT();
                                        INT_SECOND_POLE_SUPPORT = locationFilteredData11kv.get(j).getPOLE_SUPPORT();
                                        INT_SECOND_PIN_CUT = locationFilteredData11kv.get(j).getPIN_CUT();
                                        INT_SECOND_POLE_CONCREATE = locationFilteredData11kv.get(j).getPOLE_CONCRETE();
                                        INT_SECOND_POLE_STRUCTURE = locationFilteredData11kv.get(j).getPOLE_STRUCTURE();
                                        INT_SECOND_POLE_CONFIG = locationFilteredData11kv.get(j).getPOLE_CONFIG();
                                        INT_SECOND_POLE_DTR_CODE = locationFilteredData11kv.get(j).getDTR_STRUCTURE_CODE();
                                        INT_SECOND_POLE_DTR_ID = locationFilteredData11kv.get(j).getDTR_ID();
                                        INT_SECOND_DTR_LOCATION = locationFilteredData11kv.get(j).getDTR_LOCATION();
                                        INT_SECOND_DTR_AVAILABLE = locationFilteredData11kv.get(j).getLT_AVAILABLE();
                                        INT_SECOND_DTR_LT_CKT_COUNT = String.valueOf(locationFilteredData11kv.get(j).getCOUNT_CKT());
                                        INT_SECOND_DTR_MAKE = locationFilteredData11kv.get(j).getDTR_MAKE();
                                        INT_SECOND_DTR_SLNO = locationFilteredData11kv.get(j).getDTR_SNO();
                                        INT_SECOND_DTR_PHASE = locationFilteredData11kv.get(j).getDTR_PHASE();
                                        INT_SECOND_DTR_CAPACITY = locationFilteredData11kv.get(j).getDTR_CAPACITY();
                                        INT_SECOND_DTR_SHIFTED = locationFilteredData11kv.get(j).getDTR_SHIFTED();
                                    }
                                }
                            }
                            if (isMatched) {
                                String latitude1 = String.valueOf(myWGS84Point.getX());
                                String longitude2 = String.valueOf(myWGS84Point.getY());
                                openSurveyDialog11KV(mapView, motionEvent, latitude1, longitude2);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Invalid Poles", Toast.LENGTH_SHORT).show();

                            }

                        }

                    } else {
                        IS_INTERMEDIATE_POLE = false;
                        selectedExiPoleNo = spinner1.getSelectedItem().toString();
                        if (selectedExiPoleNo.equalsIgnoreCase("Select")) {
                            Toast.makeText(this, "Please Select a Pole Number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tap_select.setVisibility(View.GONE);
                        secondTap11 = true;

                        // Toast.makeText(this, selectedExiPoleNo, Toast.LENGTH_SHORT).show();
                        String latitude = "";
                        String longitude = "";
                        ;
                        for (int j = 0; j < locationFilteredData11kv.size(); j++) {
                            if (selectedExiPoleNo.equalsIgnoreCase(locationFilteredData11kv.get(j).getPOLE_NO())) {

                                //NEW
                                selectedExiPoleNoID = locationFilteredData11kv.get(j).getREF_ID();

                                latitude = locationFilteredData11kv.get(j).getLATITUDE();
                                longitude = locationFilteredData11kv.get(j).getLONGITUDE();
                                LINE_EXT_POLE_TYPE = locationFilteredData11kv.get(j).getPOLE_TYPE();
                                LINE_EXT_POLE_CONFIG = locationFilteredData11kv.get(j).getPOLE_CONFIG();
                                LINE_EXT_POLE_HEIGHT = locationFilteredData11kv.get(j).getPOLE_HEIGHT();
                                LINE_EXT_POLE_SUPPORT = locationFilteredData11kv.get(j).getPOLE_SUPPORT();
                                LINE_EXT_PIN_CUT = locationFilteredData11kv.get(j).getPIN_CUT();
                                LINE_EXT_POLE_STRUCTURE = locationFilteredData11kv.get(j).getPOLE_STRUCTURE();
                                LINE_EXT_POLE_CONCRETE = locationFilteredData11kv.get(j).getPOLE_CONCRETE();
                                LINE_EXT_POLE_FIRST_CONFIG = locationFilteredData11kv.get(0).getPOLE_CONFIG();
                                LINE_EXT_DTR_STRUCTURE_CODE = locationFilteredData11kv.get(j).getDTR_STRUCTURE_CODE();
                                LINE_EXT_HT_SERVICE_NO = locationFilteredData11kv.get(j).getHTSC_NO();
                                LINE_EXT_LT_CIRCUIT_AVAILABLE = locationFilteredData11kv.get(j).getLT_AVAILABLE();
                                LINE_EXT_LT_CIRCUIT_COUNT = String.valueOf(locationFilteredData11kv.get(j).getCOUNT_CKT());
                                LINE_EXT_DTR_MAKE = locationFilteredData11kv.get(j).getDTR_MAKE();
                                LINE_EXT_DTR_SLNO = locationFilteredData11kv.get(j).getDTR_SNO();
                                LINE_EXT_DTR_PHASE = locationFilteredData11kv.get(j).getDTR_PHASE();
                                LINE_EXT_DTR_CAPACITY = locationFilteredData11kv.get(j).getDTR_CAPACITY();
                                LINE_EXT_DTR_LOC_NAME = locationFilteredData11kv.get(j).getDTR_LOCATION();
                                LINE_EXT_DTR_LATTITUDE = latitude;
                                LINE_EXT_DTR_LONGITUDE = longitude;
                                LINE_EXT_DTR_LOCATION_NO = locationFilteredData11kv.get(j).getPOLE_NO();
                                LINE_EXT_VOLTAGE_LEVEL = locationFilteredData11kv.get(j).getVOLTAGE_LEVEL();

                                IS_LINE_EXTENSION_11_DATA_AVAILABLE = true;


                                System.out.println("LINE_EXT_DTR_STRUCTURE_CODE : " + LINE_EXT_DTR_LOCATION_NO);
                                System.out.println("LINE_EXT_POLE_CONCRETE:" + LINE_EXT_POLE_CONCRETE);
                                System.out.println("LINE_EXT_VOLTAGE_LEVEL:" + LINE_EXT_VOLTAGE_LEVEL);


                                //                                System.out.println("LINE_EXT_POLE_CONFIG : " + LINE_EXT_POLE_CONFIG);
                                //                                System.out.println("LINE_EXT_POLE_HEIGHT : " + LINE_EXT_POLE_HEIGHT);
                                //                                System.out.println("LINE_EXT_POLE_SUPPORT : " + LINE_EXT_POLE_SUPPORT);
                                //                                System.out.println("LINE_EXT_PIN_CUT : " + LINE_EXT_PIN_CUT);
                                //                                System.out.println("LINE_EXT_POLE_STRUCTURE 11KV : " + LINE_EXT_POLE_STRUCTURE);
                                //                                System.out.println("LINE_EXT_POLE_CONCRETE 11KV : " + LINE_EXT_POLE_CONCRETE);

                            }
                        }

                        double distance11 = getDistanceBetweenPoles33(String.valueOf(myWGS84Point.getY()), String.valueOf(myWGS84Point.getX()),
                                Double.parseDouble(longitude), Double.parseDouble(latitude));

                        System.out.println("DISTANCE-->" + distance11);
                        if (distance11 > 500) {
                            openAlertDialogForTappingDistance("11KV", mapView, motionEvent, latitude, longitude);
                        } else {
                            openSurveyDialog11KV(mapView, motionEvent, latitude, longitude);
                        }

                        // openSurveyDialog11KV(mapView, motionEvent, latitude, longitude);
                    }


                })
                .setNegativeButton("Cancel", null);

        if (intermediate) {
            ch_int_pole_33kv.post(() -> {
                ch_int_pole_33kv.setChecked(true);
                tv_desc_for_int.setVisibility(View.VISIBLE);
                tv_desc.setVisibility(View.GONE);
                layout_intermediate_poll.setVisibility(View.VISIBLE);
                layout_tapping_point.setVisibility(View.GONE);

            });
        }

        builder.show();
    }

    private void showDialogWithSpinnersLTForFirstPole(
            MapView mapView,
            MotionEvent motionEvent,
            double lat,
            double lon
    ) {

        double MAX_DISTANCE_METERS = 20.0;

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(
                myWebMercatorPoint,
                SpatialReferences.getWgs84()
        );

        CheckBox ch_int_pole_33kv;
        LinearLayout layout_intermediate_poll, layout_tapping_point;
        TextView tv_desc_for_int, tv_desc;

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_with_spinners, null);

        Spinner spinner1 = dialogView.findViewById(R.id.sp_tapping_point);
        Spinner intPol1 = dialogView.findViewById(R.id.sp_intermediate_poll_1);
        Spinner intPol2 = dialogView.findViewById(R.id.sp_intermediate_poll_2);

        layout_intermediate_poll = dialogView.findViewById(R.id.layout_intermediate_poll);
        layout_tapping_point = dialogView.findViewById(R.id.layout_tapping_point);
        ch_int_pole_33kv = dialogView.findViewById(R.id.ch_int_pole_33kv);
        tv_desc = dialogView.findViewById(R.id.tv_desc);
        tv_desc_for_int = dialogView.findViewById(R.id.tv_desc_for_int);

        ch_int_pole_33kv.setVisibility(View.VISIBLE);

        ch_int_pole_33kv.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                openAlertDialogForIntermediatePoleCheckExisting(
                        ch_int_pole_33kv,
                        tv_desc_for_int,
                        tv_desc,
                        layout_intermediate_poll,
                        layout_tapping_point
                );
            } else {
                ch_int_pole_33kv.setChecked(false);
                tv_desc_for_int.setVisibility(View.GONE);
                tv_desc.setVisibility(View.VISIBLE);
                layout_intermediate_poll.setVisibility(View.GONE);
                layout_tapping_point.setVisibility(View.VISIBLE);
            }
        });

        /* ---------- INTERMEDIATE POLE DATA ---------- */

        Map<String, NewModel> intPolData = new HashMap<>();
        ArrayList<String> intPoleData = new ArrayList<>();
        intPoleData.add("Select");

        for (int j = 0; j < locationFilteredDataLT.size(); j++) {

            String poleLat = locationFilteredDataLT.get(j).getLATITUDE();
            String poleLon = locationFilteredDataLT.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,
                    lon
            );

            if (distance <= MAX_DISTANCE_METERS) {
                intPoleData.add(locationFilteredDataLT.get(j).getPOLE_NO());
                intPolData.put(
                        locationFilteredDataLT.get(j).getPOLE_NO(),
                        locationFilteredDataLT.get(j)
                );
            }
        }

        SpinnerAdapter<String> intPoleAdapter1 = new SpinnerAdapter<>(
                this,
                intPoleData
        );
        intPoleAdapter1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        intPol1.setAdapter(intPoleAdapter1);

        intPol1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {

                    String selPole = intPol1.getSelectedItem().toString();
                    ArrayList<String> toPoleNumbers = new ArrayList<>();
                    toPoleNumbers.add("Select");

                    for (int j = 0; j < locationFilteredDataLT.size(); j++) {

                        if (selPole.equalsIgnoreCase(locationFilteredDataLT.get(j).getFROM_POLE())) {
                            toPoleNumbers.add(locationFilteredDataLT.get(j).getPOLE_NO());
                        }

                        if (selPole.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                            toPoleNumbers.add(locationFilteredDataLT.get(j).getFROM_POLE());
                        }
                    }

                    SpinnerAdapter<String> intPoleAdapter2 = new SpinnerAdapter<>(
                            EstimatesMap.this,
                            toPoleNumbers
                    );
                    intPoleAdapter2.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );
                    intPol2.setAdapter(intPoleAdapter2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* ---------- TAPPING POLE DATA ---------- */

        Map<String, NewModel> poleDataMap = new HashMap<>();
        ArrayList<String> poleNumbers = new ArrayList<>();
        poleNumbers.add("Select");

        for (int j = 0; j < locationFilteredDataLT.size(); j++) {

            String poleLat = locationFilteredDataLT.get(j).getLATITUDE();
            String poleLon = locationFilteredDataLT.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,
                    lon
            );

            if (distance <= MAX_DISTANCE_METERS) {
                poleNumbers.add(locationFilteredDataLT.get(j).getPOLE_NO());
                poleDataMap.put(
                        locationFilteredDataLT.get(j).getPOLE_NO(),
                        locationFilteredDataLT.get(j)
                );
            }
        }

        SpinnerAdapter<String> adapter = new SpinnerAdapter<>(
                this,
                poleNumbers
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner1.setAdapter(adapter);

        /* ---------- DIALOG ---------- */

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("OKAY", (dialog, which) -> {

                    if (ch_int_pole_33kv.isChecked()) {

                        String intermediatePoll1 = intPol1.getSelectedItem().toString();
                        String intermediatePoll2 = intPol2.getSelectedItem().toString();

                        if (intermediatePoll1.equalsIgnoreCase("Select")
                                || intermediatePoll2.equalsIgnoreCase("Select")) {
                            Toast.makeText(this, "Invalid Pole numbers", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (intermediatePoll1.equalsIgnoreCase(intermediatePoll2)) {
                            Toast.makeText(this, "You have selected the same poles", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        IS_INTERMEDIATE_POLE = true;
                        IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;

                        String sourceLocationPoll1 = "", sourceLocationPoll2 = "";

                        for (int j = 0; j < locationFilteredDataLT.size(); j++) {

                            if (intermediatePoll1.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                sourceLocationPoll1 = locationFilteredDataLT.get(j).getFROM_POLE();
                            }

                            if (intermediatePoll2.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                sourceLocationPoll2 = locationFilteredDataLT.get(j).getFROM_POLE();
                            }
                        }

                        boolean isMatched = false;

                        if (intermediatePoll1.equalsIgnoreCase(sourceLocationPoll2)
                                || intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {

                            isMatched = true;

                            if (intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                                INT_FIRST_POLE = intermediatePoll2;
                                INT_SECOND_POLE = intermediatePoll1;
                            } else {
                                INT_FIRST_POLE = intermediatePoll1;
                                INT_SECOND_POLE = intermediatePoll2;
                            }
                        }

                        if (!isMatched) {
                            Toast.makeText(this, "Invalid Poles", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (int j = 0; j < locationFilteredDataLT.size(); j++) {

                            if (INT_FIRST_POLE.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                INT_FIRST_POLE_LAT = locationFilteredDataLT.get(j).getLATITUDE();
                                INT_FIRST_POLE_LON = locationFilteredDataLT.get(j).getLONGITUDE();
                            }

                            if (INT_SECOND_POLE.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                INT_SECOND_POLE_LAT = locationFilteredDataLT.get(j).getLATITUDE();
                                INT_SECOND_POLE_LON = locationFilteredDataLT.get(j).getLONGITUDE();
                            }
                        }

                        String latitude1 = String.valueOf(myWGS84Point.getX());
                        String longitude2 = String.valueOf(myWGS84Point.getY());
                        openSurveyDialogLT(mapView, motionEvent, latitude1, longitude2);
                        dialog.dismiss();

                    } else {

                        IS_INTERMEDIATE_POLE = false;
                        selectedExiPoleNo = spinner1.getSelectedItem().toString();

                        if (selectedExiPoleNo.equalsIgnoreCase("Select")) {
                            Toast.makeText(this, "Please Select a Pole Number", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        tap_select.setVisibility(View.GONE);
                        secondTap11 = true;

                        String latitude = "";
                        String longitude = "";

                        for (int j = 0; j < locationFilteredDataLT.size(); j++) {

                            if (selectedExiPoleNo.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {

                                selectedExiPoleNoID = locationFilteredDataLT.get(j).getREF_ID();
                                latitude = locationFilteredDataLT.get(j).getLATITUDE();
                                longitude = locationFilteredDataLT.get(j).getLONGITUDE();

                                LINE_EXT_POLE_TYPE = locationFilteredDataLT.get(j).getPOLE_TYPE();
                                LINE_EXT_POLE_CONFIG = locationFilteredDataLT.get(j).getPOLE_CONFIG();
                                LINE_EXT_POLE_HEIGHT = locationFilteredDataLT.get(j).getPOLE_HEIGHT();
                                LINE_EXT_POLE_SUPPORT = locationFilteredDataLT.get(j).getPOLE_SUPPORT();
                                LINE_EXT_PIN_CUT = locationFilteredDataLT.get(j).getPIN_CUT();

                                IS_LINE_EXTENSION_LT_DATA_AVAILABLE = true;
                            }
                        }

                        openSurveyDialogLT(mapView, motionEvent, latitude, longitude);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    //NEW INTERMEDIATE POLE LOGIC
    private void showDialogWithSpinnersLT(MapView mapView, MotionEvent motionEvent, double lat, double lon, boolean intermediate) {
        //double MAX_DISTANCE_METERS = 20.0;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());

        CheckBox ch_int_pole_33kv;
        LinearLayout layout_intermediate_poll, layout_tapping_point;
        TextView tv_desc_for_int, tv_desc;


        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_with_spinners, null);
        Spinner spinner1 = dialogView.findViewById(R.id.sp_tapping_point);
        Spinner intPol1 = dialogView.findViewById(R.id.sp_intermediate_poll_1);
        Spinner intPol2 = dialogView.findViewById(R.id.sp_intermediate_poll_2);


        layout_intermediate_poll = dialogView.findViewById(R.id.layout_intermediate_poll);
        layout_tapping_point = dialogView.findViewById(R.id.layout_tapping_point);
        ch_int_pole_33kv = dialogView.findViewById(R.id.ch_int_pole_33kv);
        tv_desc = dialogView.findViewById(R.id.tv_desc);
        tv_desc_for_int = dialogView.findViewById(R.id.tv_desc_for_int);


        /*if (LOCATION_COUNT_33KV_FEEDER_1 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_33KV_FEEDER_2 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_33KV_FEEDER_3 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_33KV_FEEDER_4 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_33KV_FEEDER_5 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }*/

        if (intermediate) {
            ch_int_pole_33kv.setChecked(true);
        } else {
            ch_int_pole_33kv.setVisibility(View.GONE);
        }

        ch_int_pole_33kv.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
//                ch_int_pole_33kv.setChecked(true);
//                tv_desc_for_int.setVisibility(View.VISIBLE);
//                tv_desc.setVisibility(View.GONE);
//                layout_intermediate_poll.setVisibility(View.VISIBLE);
//                layout_tapping_point.setVisibility(View.GONE);
                openAlertDialogForIntermediatePoleCheckExisting(ch_int_pole_33kv, tv_desc_for_int, tv_desc, layout_intermediate_poll, layout_tapping_point);

            } else {
                ch_int_pole_33kv.setChecked(false);
                tv_desc_for_int.setVisibility(View.GONE);
                tv_desc.setVisibility(View.VISIBLE);
                layout_intermediate_poll.setVisibility(View.GONE);
                layout_tapping_point.setVisibility(View.VISIBLE);
            }

        });


        Map<String, NewModel> intPolData = new HashMap<>();
        ArrayList<String> intPoleData = new ArrayList<>();

        intPoleData.add("Select");

//        for (int j = 0; j < locationFilteredDataLT.size(); j++) {
//            intPoleData.add(locationFilteredDataLT.get(j).getPOLE_NO());
//        }

        for (int j = 0; j < locationFilteredDataLT.size(); j++) {

            String poleLat = locationFilteredDataLT.get(j).getLATITUDE();
            String poleLon = locationFilteredDataLT.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,
                    lon
            );

            if (distance <= MAX_DISTANCE_METERS) {
                intPoleData.add(locationFilteredDataLT.get(j).getPOLE_NO());
                intPolData.put(
                        locationFilteredDataLT.get(j).getPOLE_NO(),
                        locationFilteredDataLT.get(j)
                );
            }
        }


        SpinnerAdapter<String> intPoleAdapter1 = new SpinnerAdapter<>(this, intPoleData);
        //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intPol1.setAdapter(intPoleAdapter1);


        intPol1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String selPole = intPol1.getSelectedItem().toString();
                    ArrayList<String> toPoleNumbers = new ArrayList<>();
                    toPoleNumbers.add("Select");
                    for (int j = 0; j < locationFilteredDataLT.size(); j++) {
                        if (selPole.equalsIgnoreCase(locationFilteredDataLT.get(j).getFROM_POLE())) {
                            toPoleNumbers.add(locationFilteredDataLT.get(j).getPOLE_NO());
                        }
                        if (selPole.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                            toPoleNumbers.add(locationFilteredDataLT.get(j).getFROM_POLE());

                        }
                    }

                    SpinnerAdapter<String> intPoleAdapter2 = new SpinnerAdapter<>(EstimatesMap.this, toPoleNumbers);
                    //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intPol2.setAdapter(intPoleAdapter2);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


      /*  ArrayAdapter<String> intPoleAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, intPoleData);
        intPoleAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intPol2.setAdapter(intPoleAdapter2);*/


        // Map to hold pole numbers and their corresponding NewModel data
        Map<String, NewModel> poleDataMap = new HashMap<>();
        ArrayList<String> poleNumbers = new ArrayList<>();
        poleNumbers.add("Select");


//        for (int j = 0; j < locationFilteredDataLT.size(); j++) {
//            poleNumbers.add(locationFilteredDataLT.get(j).getPOLE_NO());
//        }

        for (int j = 0; j < locationFilteredDataLT.size(); j++) {

            String poleLat = locationFilteredDataLT.get(j).getLATITUDE();
            String poleLon = locationFilteredDataLT.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,   // clicked latitude
                    lon    // clicked longitude
            );

            if (distance <= MAX_DISTANCE_METERS) {
                poleNumbers.add(locationFilteredDataLT.get(j).getPOLE_NO());
                poleDataMap.put(
                        locationFilteredDataLT.get(j).getPOLE_NO(),
                        locationFilteredDataLT.get(j)
                );
            }
        }


        SpinnerAdapter<String> adapter = new SpinnerAdapter<>(this, poleNumbers);
        spinner1.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
//                .setTitle("Mapping Of Existing Pole/Location (Tapping Location) as First Location")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("OKAY", (dialog, which) -> {

                    if (ch_int_pole_33kv.isChecked()) {

                        String intermediatePoll1 =
                                intPol1 != null && intPol1.getSelectedItem() != null
                                        ? intPol1.getSelectedItem().toString()
                                        : "Select";

                        String intermediatePoll2 =
                                intPol2 != null && intPol2.getSelectedItem() != null
                                        ? intPol2.getSelectedItem().toString()
                                        : "Select";
                        String sourceLocationPoll1 = "", sourceLocationPoll2 = "";

                        if (intermediatePoll1.equalsIgnoreCase("Select") || intermediatePoll2.equalsIgnoreCase("Select")) {
                            Toast.makeText(this, "Invalid Pole numbers", Toast.LENGTH_SHORT).show();
                        } else if (intermediatePoll1.equalsIgnoreCase(intermediatePoll2)) {
                            Toast.makeText(this, "You have selected the same poles", Toast.LENGTH_SHORT).show();
                        } else {
                            IS_INTERMEDIATE_POLE = true;
                            IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                            String latitude = "";
                            String longitude = "";
                            for (int j = 0; j < locationFilteredDataLT.size(); j++) {
                                if (intermediatePoll1.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                    // latitude = locationFilteredData33kv.get(j).getLATITUDE();
                                    // longitude = locationFilteredData33kv.get(j).getLONGITUDE();
                                    sourceLocationPoll1 = locationFilteredDataLT.get(j).getFROM_POLE();
                                    //  System.out.println("sourcePole33KV" + sourceLocationPoll1);
                                    //System.out.println("PIN_POINT_33KV" + LINE_EXT_POLE_STRUCTURE);
                                    //23-12-24 Chandu Changes
                                    IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                                }

                                if (intermediatePoll2.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                    //  latitude = locationFilteredData33kv.get(j).getLATITUDE();
                                    //  longitude = locationFilteredData33kv.get(j).getLONGITUDE();
                                    sourceLocationPoll2 = locationFilteredDataLT.get(j).getFROM_POLE();
                                    //  System.out.println("sourcePole33KV" + sourceLocationPoll2);
                                    //  System.out.println("PIN_POINT_33KV" + LINE_EXT_POLE_STRUCTURE);
                                    //23-12-24 Chandu Changes
                                    IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                                }
                            }
                            boolean isMatched = false;
                            if (intermediatePoll1.equalsIgnoreCase(sourceLocationPoll2) || intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                                isMatched = true;
                                if (intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                                    INT_FIRST_POLE = intermediatePoll2;
                                    INT_SECOND_POLE = intermediatePoll1;
                                } else {
                                    INT_FIRST_POLE = intermediatePoll1;
                                    INT_SECOND_POLE = intermediatePoll2;
                                }

                                for (int j = 0; j < locationFilteredDataLT.size(); j++) {
                                    if (INT_FIRST_POLE.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                        INT_FIRST_POLE_LAT = locationFilteredDataLT.get(j).getLATITUDE();
                                        INT_FIRST_POLE_LON = locationFilteredDataLT.get(j).getLONGITUDE();
                                        INT_FIRST_POLE_ID = locationFilteredDataLT.get(j).getREF_ID();
                                        INT_FIRST_LINE_TYPE = locationFilteredDataLT.get(j).getLINE_TYPE();
                                        INT_FIRST_POLE_TYPE = locationFilteredDataLT.get(j).getPOLE_TYPE();
                                        INT_FIRST_POLE_HEIGHT = locationFilteredDataLT.get(j).getPOLE_HEIGHT();
                                        INT_FIRST_POLE_SUPPORT = locationFilteredDataLT.get(j).getPOLE_SUPPORT();

                                        INT_FIRST_PIN_CUT = locationFilteredDataLT.get(j).getPIN_CUT();
                                        INT_FIRST_POLE_CONCREATE = locationFilteredDataLT.get(j).getPOLE_CONCRETE();
                                        INT_FIRST_POLE_STRUCTURE = locationFilteredDataLT.get(j).getPOLE_STRUCTURE();
                                        INT_FIRST_POLE_CONFIG = locationFilteredDataLT.get(j).getPOLE_CONFIG();
                                    }
                                    if (INT_SECOND_POLE.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {
                                        INT_SECOND_POLE_LAT = locationFilteredDataLT.get(j).getLATITUDE();
                                        INT_SECOND_POLE_LON = locationFilteredDataLT.get(j).getLONGITUDE();
                                        INT_SECOND_POLE_ID = locationFilteredDataLT.get(j).getREF_ID();
                                        INT_SECOND_LINE_TYPE = locationFilteredDataLT.get(j).getLINE_TYPE();
                                        INT_SECOND_POLE_TYPE = locationFilteredDataLT.get(j).getPOLE_TYPE();
                                        INT_SECOND_POLE_HEIGHT = locationFilteredDataLT.get(j).getPOLE_HEIGHT();
                                        INT_SECOND_POLE_SUPPORT = locationFilteredDataLT.get(j).getPOLE_SUPPORT();

                                        INT_SECOND_PIN_CUT = locationFilteredDataLT.get(j).getPIN_CUT();
                                        INT_SECOND_POLE_CONCREATE = locationFilteredDataLT.get(j).getPOLE_CONCRETE();
                                        INT_SECOND_POLE_STRUCTURE = locationFilteredDataLT.get(j).getPOLE_STRUCTURE();
                                        INT_FIRST_POLE_CONFIG = locationFilteredDataLT.get(j).getPOLE_CONFIG();
                                    }
                                }
                            }
                            if (isMatched) {
                                String latitude1 = String.valueOf(myWGS84Point.getX());
                                String longitude2 = String.valueOf(myWGS84Point.getY());
                                openSurveyDialogLT(mapView, motionEvent, latitude1, longitude2);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Invalid Poles", Toast.LENGTH_SHORT).show();

                            }


                        }


                    } else {
                        IS_INTERMEDIATE_POLE = false;
                        selectedExiPoleNo = spinner1.getSelectedItem().toString();

                        if (selectedExiPoleNo.equalsIgnoreCase("Select")) {
                            Toast.makeText(this, "Please Select a Pole Number", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            tap_select.setVisibility(View.GONE);
                            secondTap33 = true;
                            // Toast.makeText(this, selectedExiPoleNo, Toast.LENGTH_SHORT).show();
                            String latitude = "";
                            String longitude = "";
                            ;

                            for (int j = 0; j < locationFilteredDataLT.size(); j++) {
                                if (selectedExiPoleNo.equalsIgnoreCase(locationFilteredDataLT.get(j).getPOLE_NO())) {

                                    //NEW
                                    selectedExiPoleNoID = locationFilteredDataLT.get(j).getREF_ID();

                                    latitude = locationFilteredDataLT.get(j).getLATITUDE();
                                    longitude = locationFilteredDataLT.get(j).getLONGITUDE();
                                    LINE_EXT_POLE_TYPE = locationFilteredDataLT.get(j).getPOLE_TYPE();
                                    LINE_EXT_POLE_CONFIG = locationFilteredDataLT.get(j).getPOLE_CONFIG();
                                    LINE_EXT_POLE_HEIGHT = locationFilteredDataLT.get(j).getPOLE_HEIGHT();
                                    LINE_EXT_POLE_SUPPORT = locationFilteredDataLT.get(j).getPOLE_SUPPORT();
                                    LINE_EXT_PIN_CUT = locationFilteredDataLT.get(j).getPIN_CUT();

                                    IS_LINE_EXTENSION_LT_DATA_AVAILABLE = true;
                                }
                            }

                            double distance11 = getDistanceBetweenPoles33(String.valueOf(myWGS84Point.getY()), String.valueOf(myWGS84Point.getX()),
                                    Double.parseDouble(longitude), Double.parseDouble(latitude));

                            System.out.println("DISTANCE-->" + distance11);
                            if (distance11 > 500) {
                                openAlertDialogForTappingDistance("LT", mapView, motionEvent, latitude, longitude);
                            } else {
                                openSurveyDialogLT(mapView, motionEvent, latitude, longitude);
                            }

                            // openSurveyDialogLT(mapView, motionEvent, latitude, longitude);
                            dialog.dismiss();
                        }
                    }


                })
                .setNegativeButton("Cancel", null);

        if (intermediate) {
            ch_int_pole_33kv.post(() -> {
                ch_int_pole_33kv.setChecked(true);
                tv_desc_for_int.setVisibility(View.VISIBLE);
                tv_desc.setVisibility(View.GONE);
                layout_intermediate_poll.setVisibility(View.VISIBLE);
                layout_tapping_point.setVisibility(View.GONE);

            });
        }

        builder.show();
    }

    //NEW INTERMEDIATE POLE LOGIC
    private void showDialogWithSpinners33KV(MapView mapView, MotionEvent motionEvent, double lat, double lon, boolean intermediate) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());

        CheckBox ch_int_pole_33kv;
        LinearLayout layout_intermediate_poll, layout_tapping_point;
        TextView tv_desc_for_int, tv_desc;


        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_with_spinners, null);
        Spinner spinner1 = dialogView.findViewById(R.id.sp_tapping_point);
        Spinner intPol1 = dialogView.findViewById(R.id.sp_intermediate_poll_1);
        Spinner intPol2 = dialogView.findViewById(R.id.sp_intermediate_poll_2);


        layout_intermediate_poll = dialogView.findViewById(R.id.layout_intermediate_poll);
        layout_tapping_point = dialogView.findViewById(R.id.layout_tapping_point);
        ch_int_pole_33kv = dialogView.findViewById(R.id.ch_int_pole_33kv);
        tv_desc = dialogView.findViewById(R.id.tv_desc);
        tv_desc_for_int = dialogView.findViewById(R.id.tv_desc_for_int);


        if (LOCATION_COUNT_33KV_FEEDER_1 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_33KV_FEEDER_2 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_33KV_FEEDER_3 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_33KV_FEEDER_4 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }
        if (LOCATION_COUNT_33KV_FEEDER_5 == 0 && WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
            ch_int_pole_33kv.setVisibility(View.VISIBLE);
        }

        if (intermediate) {
            ch_int_pole_33kv.setChecked(true);
        } else {
            ch_int_pole_33kv.setVisibility(View.GONE);
        }

        ch_int_pole_33kv.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
//                ch_int_pole_33kv.setChecked(true);
//                tv_desc_for_int.setVisibility(View.VISIBLE);
//                tv_desc.setVisibility(View.GONE);
//                layout_intermediate_poll.setVisibility(View.VISIBLE);
//                layout_tapping_point.setVisibility(View.GONE);
                openAlertDialogForIntermediatePoleCheckExisting(ch_int_pole_33kv, tv_desc_for_int, tv_desc, layout_intermediate_poll, layout_tapping_point);
            } else {
                ch_int_pole_33kv.setChecked(false);
                tv_desc_for_int.setVisibility(View.GONE);
                tv_desc.setVisibility(View.VISIBLE);
                layout_intermediate_poll.setVisibility(View.GONE);
                layout_tapping_point.setVisibility(View.VISIBLE);
            }

        });


        Map<String, NewModel> intPolData = new HashMap<>();
        ArrayList<String> intPoleData = new ArrayList<>();

        intPoleData.add("Select");

        for (int j = 0; j < locationFilteredData33kv.size(); j++) {

            String poleLat = locationFilteredData33kv.get(j).getLATITUDE();
            String poleLon = locationFilteredData33kv.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,
                    lon
            );

            System.out.println("DISTANCE---->::::" + distance);

            if (distance <= MAX_DISTANCE_METERS) {
                intPoleData.add(locationFilteredData33kv.get(j).getPOLE_NO());
                intPolData.put(
                        locationFilteredData33kv.get(j).getPOLE_NO(),
                        locationFilteredData33kv.get(j)
                );
            }
        }


//        for (int j = 0; j < locationFilteredData33kv.size(); j++) {
//            intPoleData.add(locationFilteredData33kv.get(j).getPOLE_NO());
//        }


        SpinnerAdapter<String> intPoleAdapter1 = new SpinnerAdapter<>(this, intPoleData);
        ////intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intPol1.setAdapter(intPoleAdapter1);


        intPol1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!intPol1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String selPole = intPol1.getSelectedItem().toString();
                    ArrayList<String> toPoleNumbers = new ArrayList<>();
                    toPoleNumbers.add("Select");
                    for (int j = 0; j < locationFilteredData33kv.size(); j++) {
                        if (selPole.equalsIgnoreCase(locationFilteredData33kv.get(j).getFROM_POLE())) {
                            toPoleNumbers.add(locationFilteredData33kv.get(j).getPOLE_NO());
                        }
                        if (selPole.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                            toPoleNumbers.add(locationFilteredData33kv.get(j).getFROM_POLE());

                        }
                    }

                    SpinnerAdapter<String> intPoleAdapter2 = new SpinnerAdapter<>(EstimatesMap.this, toPoleNumbers);
                    //intPoleAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intPol2.setAdapter(intPoleAdapter2);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


      /*  ArrayAdapter<String> intPoleAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, intPoleData);
        intPoleAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intPol2.setAdapter(intPoleAdapter2);*/


        // Map to hold pole numbers and their corresponding NewModel data
        Map<String, NewModel> poleDataMap = new HashMap<>();
        ArrayList<String> poleNumbers = new ArrayList<>();
        poleNumbers.add("Select");

        for (int j = 0; j < locationFilteredData33kv.size(); j++) {

            String poleLat = locationFilteredData33kv.get(j).getLATITUDE();
            String poleLon = locationFilteredData33kv.get(j).getLONGITUDE();

            double distance = getDistanceBetweenPoles33New(
                    poleLat,
                    poleLon,
                    lat,   // clicked latitude
                    lon    // clicked longitude
            );

            if (distance <= MAX_DISTANCE_METERS) {
                poleNumbers.add(locationFilteredData33kv.get(j).getPOLE_NO());
                poleDataMap.put(
                        locationFilteredData33kv.get(j).getPOLE_NO(),
                        locationFilteredData33kv.get(j)
                );
            }
        }


//        for (int j = 0; j < locationFilteredData33kv.size(); j++) {
//            poleNumbers.add(locationFilteredData33kv.get(j).getPOLE_NO());
//        }


        SpinnerAdapter<String> adapter = new SpinnerAdapter<>(this, poleNumbers);
        spinner1.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
//                .setTitle("Mapping Of Existing Pole/Location (Tapping Location) as First Location")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("OKAY", (dialog, which) -> {

                    if (ch_int_pole_33kv.isChecked()) {
                        String intermediatePoll1 =
                                intPol1 != null && intPol1.getSelectedItem() != null
                                        ? intPol1.getSelectedItem().toString()
                                        : "Select";

                        String intermediatePoll2 =
                                intPol2 != null && intPol2.getSelectedItem() != null
                                        ? intPol2.getSelectedItem().toString()
                                        : "Select";

                        String sourceLocationPoll1 = "", sourceLocationPoll2 = "";

                        if (intermediatePoll1.equalsIgnoreCase("Select") || intermediatePoll2.equalsIgnoreCase("Select")) {
                            Toast.makeText(this, "Invalid Pole numbers", Toast.LENGTH_SHORT).show();
                        } else if (intermediatePoll1.equalsIgnoreCase(intermediatePoll2)) {
                            Toast.makeText(this, "You have selected the same poles", Toast.LENGTH_SHORT).show();
                        } else {
                            IS_INTERMEDIATE_POLE = true;
                            IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                            String latitude = "";
                            String longitude = "";
                            for (int j = 0; j < locationFilteredData33kv.size(); j++) {
                                if (intermediatePoll1.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                                    // latitude = locationFilteredData33kv.get(j).getLATITUDE();
                                    // longitude = locationFilteredData33kv.get(j).getLONGITUDE();
                                    sourceLocationPoll1 = locationFilteredData33kv.get(j).getFROM_POLE();
                                    //  System.out.println("sourcePole33KV" + sourceLocationPoll1);
                                    //System.out.println("PIN_POINT_33KV" + LINE_EXT_POLE_STRUCTURE);
                                    //23-12-24 Chandu Changes
                                    IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                                }

                                if (intermediatePoll2.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                                    //  latitude = locationFilteredData33kv.get(j).getLATITUDE();
                                    //  longitude = locationFilteredData33kv.get(j).getLONGITUDE();
                                    sourceLocationPoll2 = locationFilteredData33kv.get(j).getFROM_POLE();
                                    //  System.out.println("sourcePole33KV" + sourceLocationPoll2);
                                    //  System.out.println("PIN_POINT_33KV" + LINE_EXT_POLE_STRUCTURE);
                                    //23-12-24 Chandu Changes
                                    IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                                }
                            }
                            boolean isMatched = false;
                            if (intermediatePoll1.equalsIgnoreCase(sourceLocationPoll2) || intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                                isMatched = true;
                                if (intermediatePoll2.equalsIgnoreCase(sourceLocationPoll1)) {
                                    INT_FIRST_POLE = intermediatePoll2;
                                    INT_SECOND_POLE = intermediatePoll1;
                                } else {
                                    INT_FIRST_POLE = intermediatePoll1;
                                    INT_SECOND_POLE = intermediatePoll2;
                                }

                                for (int j = 0; j < locationFilteredData33kv.size(); j++) {
                                    if (INT_FIRST_POLE.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                                        INT_FIRST_POLE_LAT = locationFilteredData33kv.get(j).getLATITUDE();
                                        INT_FIRST_POLE_LON = locationFilteredData33kv.get(j).getLONGITUDE();
                                        INT_FIRST_POLE_ID = locationFilteredData33kv.get(j).getREF_ID();
                                        INT_FIRST_LINE_TYPE = locationFilteredData33kv.get(j).getLINE_TYPE();
                                        INT_FIRST_POLE_TYPE = locationFilteredData33kv.get(j).getPOLE_TYPE();
                                        INT_FIRST_POLE_HEIGHT = locationFilteredData33kv.get(j).getPOLE_HEIGHT();
                                        INT_FIRST_POLE_SUPPORT = locationFilteredData33kv.get(j).getPOLE_SUPPORT();

                                        INT_FIRST_PIN_CUT = locationFilteredData33kv.get(j).getPIN_CUT();
                                        INT_FIRST_POLE_CONCREATE = locationFilteredData33kv.get(j).getPOLE_CONCRETE();
                                        INT_FIRST_POLE_STRUCTURE = locationFilteredData33kv.get(j).getPOLE_STRUCTURE();
                                        INT_FIRST_POLE_CONFIG = locationFilteredData33kv.get(j).getPOLE_CONFIG();

                                    }
                                    if (INT_SECOND_POLE.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {
                                        INT_SECOND_POLE_LAT = locationFilteredData33kv.get(j).getLATITUDE();
                                        INT_SECOND_POLE_LON = locationFilteredData33kv.get(j).getLONGITUDE();
                                        INT_SECOND_POLE_ID = locationFilteredData33kv.get(j).getREF_ID();
                                        INT_SECOND_LINE_TYPE = locationFilteredData33kv.get(j).getLINE_TYPE();
                                        INT_SECOND_POLE_TYPE = locationFilteredData33kv.get(j).getPOLE_TYPE();
                                        INT_SECOND_POLE_HEIGHT = locationFilteredData33kv.get(j).getPOLE_HEIGHT();
                                        INT_SECOND_POLE_SUPPORT = locationFilteredData33kv.get(j).getPOLE_SUPPORT();

                                        INT_SECOND_PIN_CUT = locationFilteredData33kv.get(j).getPIN_CUT();
                                        INT_SECOND_POLE_CONCREATE = locationFilteredData33kv.get(j).getPOLE_CONCRETE();
                                        INT_SECOND_POLE_STRUCTURE = locationFilteredData33kv.get(j).getPOLE_STRUCTURE();
                                        INT_FIRST_POLE_CONFIG = locationFilteredData33kv.get(j).getPOLE_CONFIG();
                                    }
                                }
                            }
                            if (isMatched) {
                                String latitude1 = String.valueOf(myWGS84Point.getX());
                                String longitude2 = String.valueOf(myWGS84Point.getY());
                                openSurveyDialog33KV(mapView, motionEvent, latitude1, longitude2);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Invalid Poles", Toast.LENGTH_SHORT).show();

                            }


                        }


                    } else {
                        IS_INTERMEDIATE_POLE = false;
                        selectedExiPoleNo = spinner1.getSelectedItem().toString();

                        if (selectedExiPoleNo.equalsIgnoreCase("Select")) {
                            Toast.makeText(this, "Please Select a Pole Number", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            tap_select.setVisibility(View.GONE);
                            secondTap33 = true;
                            // Toast.makeText(this, selectedExiPoleNo, Toast.LENGTH_SHORT).show();
                            String latitude = "";
                            String longitude = "";
                            ;
                            for (int j = 0; j < locationFilteredData33kv.size(); j++) {
                                if (selectedExiPoleNo.equalsIgnoreCase(locationFilteredData33kv.get(j).getPOLE_NO())) {

                                    //NEW
                                    selectedExiPoleNoID = locationFilteredData33kv.get(j).getREF_ID();

                                    latitude = locationFilteredData33kv.get(j).getLATITUDE();
                                    longitude = locationFilteredData33kv.get(j).getLONGITUDE();
                                    LINE_EXT_POLE_TYPE = locationFilteredData33kv.get(j).getPOLE_TYPE();
                                    LINE_EXT_POLE_CONFIG = locationFilteredData33kv.get(j).getPOLE_CONFIG();
                                    LINE_EXT_POLE_HEIGHT = locationFilteredData33kv.get(j).getPOLE_HEIGHT();
                                    LINE_EXT_POLE_SUPPORT = locationFilteredData33kv.get(j).getPOLE_SUPPORT();
                                    LINE_EXT_PIN_CUT = locationFilteredData33kv.get(j).getPIN_CUT();
                                    LINE_EXT_POLE_STRUCTURE = locationFilteredData33kv.get(j).getPOLE_STRUCTURE();
                                    LINE_EXT_POLE_CONCRETE = locationFilteredData33kv.get(j).getPOLE_CONCRETE();
                                    LINE_EXT_POLE_FIRST_CONFIG = locationFilteredData33kv.get(0).getPOLE_CONFIG();


//                                    System.out.println("LINE_EXT_POLE_HEIGHT_33KV:" + LINE_EXT_POLE_HEIGHT);
//                                    System.out.println("LINE_EXT_POLE_TYPE_33KV:" + LINE_EXT_POLE_TYPE);
//                                    System.out.println("LINE_EXT_POLE_CONCRETE:" + LINE_EXT_POLE_CONCRETE);
                                    //23-12-24 Chandu Changes
                                    IS_LINE_EXTENSION_33_DATA_AVAILABLE = true;
                                }
                            }

                            double distance11 = getDistanceBetweenPoles33(String.valueOf(myWGS84Point.getY()), String.valueOf(myWGS84Point.getX()), Double.parseDouble(longitude), Double.parseDouble(latitude));

                            System.out.println("DISTANCE-->" + distance11);
                            if (distance11 > 500) {
                                openAlertDialogForTappingDistance("33KV", mapView, motionEvent, latitude, longitude);
                            } else {
                                openSurveyDialog33KV(mapView, motionEvent, latitude, longitude);
                            }


                            dialog.dismiss();
                        }
                    }


                })
                .setNegativeButton("Cancel", null);

        if (intermediate) {
            ch_int_pole_33kv.post(() -> {
                ch_int_pole_33kv.setChecked(true);
                tv_desc_for_int.setVisibility(View.VISIBLE);
                tv_desc.setVisibility(View.GONE);
                layout_intermediate_poll.setVisibility(View.VISIBLE);
                layout_tapping_point.setVisibility(View.GONE);

            });
        }

        builder.show();
    }

    private void openAlertDialogForTappingDistance(String voltageLevel, MapView mapView, MotionEvent e, String lattitude, String longitude) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Pay attention")
                .setCancelable(false)
                .setMessage("Clicked location on the map is 500 meters far away from selected Tapping location. Please re-check")
                .setPositiveButton("Okay", (dialogInterface, which) -> {
                    if (voltageLevel.equalsIgnoreCase("33KV")) {
                        openSurveyDialog33KV(mapView, e, lattitude, longitude);
                    } else if (voltageLevel.equalsIgnoreCase("11KV")) {
                        openSurveyDialog11KV(mapView, e, lattitude, longitude);
                    } else {
                        openSurveyDialogLT(mapView, e, lattitude, longitude);
                    }


                    dialogInterface.cancel();
                })
                .show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        }


    }

    private void openSurveyDialogLT(MapView mapView, MotionEvent e, String lattitude, String longitude) {

        listAll = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);

        int x = (int) e.getX();
        int y = (int) e.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);

        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(
                myWebMercatorPoint,
                SpatialReferences.getWgs84()
        );

        Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
        map.setInitialViewpoint(vp);
        mv.setMap(map);

        graphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(graphicsOverlay);

        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CROSS,
                Color.RED,
                12
        );

        graphic = new Graphic(myWGS84Point, symbol);
        graphicsOverlay.getGraphics().add(graphic);

        // ---------------------- CALLOUT ----------------------
        TextView calloutContent = new TextView(getApplicationContext());
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setSingleLine();
        calloutContent.setFocusable(true);

        if (listAll == null || listAll.isEmpty()) {

            calloutContent.setText("1");

        } else {

            int maxPoleNumber = 0;

            for (int i = 0; i < listAll.size(); i++) {

                String poleStr = listAll.get(i).getPOLE_NO();

                if (poleStr != null && !poleStr.trim().isEmpty()) {
                    try {
                        int currentPole = Integer.parseInt(poleStr.trim());
                        if (currentPole > maxPoleNumber) {
                            maxPoleNumber = currentPole;
                        }
                    } catch (NumberFormatException ex) {
                        Log.e("POLE_PARSE_ERROR", "Invalid POLE_NO: " + poleStr);
                    }
                }
            }

            maxPoleNumber = (maxPoleNumber == 0) ? 1 : (maxPoleNumber + 1);
            calloutContent.setText(String.valueOf(maxPoleNumber));
        }

        // ---------------------- GPS LOCATION ----------------------
        gpsTraker.getLocation();

        double currLat = gpsTraker.getLatitude();
        double currLon = gpsTraker.getLongitude();

        Location currentStartPoint = new Location("locationA");
        currentStartPoint.setLatitude(currLat);
        currentStartPoint.setLongitude(currLon);

        Location currentEndPoint = new Location("locationA");
        currentEndPoint.setLatitude(myWGS84Point.getY());
        currentEndPoint.setLongitude(myWGS84Point.getX());

        currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        //INTERMEDIATE POLE DISTANCE LOGIC

//        if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_LT){
//            boolean distance = isThirdPoleWithinLimit(Double.parseDouble(INT_FIRST_POLE_LAT), Double.parseDouble(INT_FIRST_POLE_LON), Double.parseDouble(INT_SECOND_POLE_LAT), Double.parseDouble(INT_SECOND_POLE_LON), Double.parseDouble(lattitude), Double.parseDouble(longitude));
//            System.out.println("DISTNCE_OF_3POLE---->" + distance);
//
//            if (!distance) {
//                openDistanceAlertDialog(mapView, e);
//                return;
//            }
//        }


        // ---------------------- LAST POLE DISTANCE ----------------------
        if (listAll == null || listAll.isEmpty()) {

            lastPoleDistance = 0;

        } else {

            String latStr = listAll.get(listAll.size() - 1).getLATITUDE();
            String lonStr = listAll.get(listAll.size() - 1).getLONGITUDE();

            if (latStr != null && lonStr != null) {

                try {

                    Location lastPoleStartPoint = new Location("locationB");
                    lastPoleStartPoint.setLatitude(Double.parseDouble(latStr));
                    lastPoleStartPoint.setLongitude(Double.parseDouble(lonStr));

                    Location lastPoleEndPoint = new Location("locationB");
                    lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                    lastPoleEndPoint.setLongitude(myWGS84Point.getX());

                    lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);

                } catch (Exception e1) {
                    lastPoleDistance = 0;
                    Log.e("LAST_POLE_DIST_ERR", e1.toString());
                }

            } else {
                lastPoleDistance = 0;
            }
        }

        // ---------------------- CALLOUT SHOW ----------------------
        mCallout = mapView.getCallout();
        mCallout.setLocation(myWGS84Point);
        mCallout.setContent(calloutContent);
        mCallout.show();

        // ---------------------- INTENT ----------------------
        Intent intent = new Intent(EstimatesMap.this, LocationDetails.class);

        intent.putExtra("poleLat", String.valueOf(myWGS84Point.getX()));
        intent.putExtra("poleLon", String.valueOf(myWGS84Point.getY()));

        intent.putExtra("currentLat", currLat);
        intent.putExtra("currentLon", currLon);

        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("isEdit", false);
        intent.putExtra("is33KVSelected", is33KVSelected);
        intent.putExtra("is11KVSelected", is11KVSelected);
        intent.putExtra("isDTRSelected", isDTRSelected);
        intent.putExtra("isLTSelected", isLTSelected);
        intent.putExtra("lastPoleDistance", lastPoleDistance);
        intent.putExtra("currentDistance", currentDistance);
        intent.putExtra("isFirstPole", isFirstPole);
        intent.putExtra("max_length", max_length);
        intent.putExtra("isMeterSelected", isMETERSelected);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("REG_COUNT", REG_COUNT);
        intent.putExtra("EXI_LAT", lattitude);
        intent.putExtra("EXI_LONG", longitude);
        intent.putExtra("BTN_VALUE", BTN_VALUE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(selectedFdrIndex));
        intent.putExtra("IS_EDIT", IS_EDIT);
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            intent.putExtra("FEEDERS", FEEDERS);
            intent.putExtra("RDSS_TYPE", RDSS_TYPE);
        }

        if (IS_EDIT) {
            intent.putExtra("DATA_MODEL", DATA_MODEL);
        }

        startActivity(intent);
        overridePendingTransition(
                R.anim.activity_slide_from_bottom,
                R.anim.activity_stay
        );
    }


    private void openSurveyDialog11KV(MapView mapView, MotionEvent e, String lattitude, String longitude) {

        listAll = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);
        System.out.println("SIZE_OF_LIST----->::" + listAll.size());

        int x = (int) e.getX();
        int y = (int) e.getY();

        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());

        Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
        map.setInitialViewpoint(vp);
        mv.setMap(map);

        graphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(graphicsOverlay);

        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 12);
        graphic = new Graphic(myWGS84Point, symbol);
        graphicsOverlay.getGraphics().add(graphic);

        // create a textview for the callout
        TextView calloutContent = new TextView(getApplicationContext());
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setSingleLine();
        calloutContent.setFocusable(true);

        // ---------- SAFE MAX POLE NUMBER LOGIC ----------
        int maxPoleNumber = 0;

        if (listAll != null && !listAll.isEmpty()) {
            for (int i = 0; i < listAll.size(); i++) {

                String poleStr = listAll.get(i).getPOLE_NO();

                // accept only numeric pole numbers
                if (poleStr != null && poleStr.matches("\\d+")) {
                    int currentPoleNo = Integer.parseInt(poleStr);

                    if (currentPoleNo > maxPoleNumber) {
                        maxPoleNumber = currentPoleNo;
                    }
                }
            }
        }

        maxPoleNumber++;  // increment
        calloutContent.setText(String.valueOf(maxPoleNumber));
        // -------------------------------------------------

        gpsTraker.getLocation();

        // DISTANCE COMMON
        Location currentStartPoint = new Location("locationA");
        currentStartPoint.setLatitude(gpsTraker.getLatitude());
        currentStartPoint.setLongitude(gpsTraker.getLongitude());

        Location currentEndPoint = new Location("locationA");
        currentEndPoint.setLatitude(myWGS84Point.getY());
        currentEndPoint.setLongitude(myWGS84Point.getX());
        currentDistance = currentStartPoint.distanceTo(currentEndPoint);

//INTERMEDIATE POLE DISTANCE LOGIC
//        if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11){
//            boolean distance = isThirdPoleWithinLimit(Double.parseDouble(INT_FIRST_POLE_LAT), Double.parseDouble(INT_FIRST_POLE_LON), Double.parseDouble(INT_SECOND_POLE_LAT), Double.parseDouble(INT_SECOND_POLE_LON), Double.parseDouble(lattitude), Double.parseDouble(longitude));
//            System.out.println("DISTNCE_OF_3POLE---->" + distance);
//
//            if (!distance) {
//                openDistanceAlertDialog(mapView, e);
//                return;
//            }
//        }


        // distance from last pole
        if (listAll == null || listAll.size() == 0) {

            lastPoleDistance = 0;

        } else {

            String lastLatStr = listAll.get(listAll.size() - 1).getLATITUDE();
            String lastLonStr = listAll.get(listAll.size() - 1).getLONGITUDE();

            if (lastLatStr != null && lastLonStr != null &&
                    !lastLatStr.isEmpty() && !lastLonStr.isEmpty()) {

                Location lastPoleStartPoint = new Location("locationB");
                lastPoleStartPoint.setLatitude(Double.parseDouble(lastLatStr));
                lastPoleStartPoint.setLongitude(Double.parseDouble(lastLonStr));

                Location lastPoleEndPoint = new Location("locationB");
                lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                lastPoleEndPoint.setLongitude(myWGS84Point.getX());

                lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);
            } else {
                lastPoleDistance = 0;
            }
        }

        // format coordinates to 4 decimal places
        mCallout = mapView.getCallout();
        mCallout.setLocation(myWGS84Point);
        mCallout.setContent(calloutContent);
        mCallout.show();

        gpsTraker.getLocation();

        Intent intent = new Intent(EstimatesMap.this, LocationDetails.class);
        intent.putExtra("poleLat", String.valueOf(myWGS84Point.getX()));
        intent.putExtra("poleLon", String.valueOf(myWGS84Point.getY()));
        intent.putExtra("currentLat", gpsTraker.getLatitude());
        intent.putExtra("currentLon", gpsTraker.getLongitude());

        System.out.println("CURRENT_LAT_LNGS_MAP" + gpsTraker.getLatitude() + gpsTraker.getLongitude());

        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("isEdit", false);
        intent.putExtra("is33KVSelected", is33KVSelected);
        intent.putExtra("is11KVSelected", is11KVSelected);
        intent.putExtra("isDTRSelected", isDTRSelected);
        intent.putExtra("isLTSelected", isLTSelected);
        intent.putExtra("lastPoleDistance", lastPoleDistance);
        intent.putExtra("currentDistance", currentDistance);
        intent.putExtra("isFirstPole", isFirstPole);
        intent.putExtra("max_length", max_length);
        intent.putExtra("isMeterSelected", isMETERSelected);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("REG_COUNT", REG_COUNT);
        intent.putExtra("EXI_LAT", lattitude);
        intent.putExtra("EXI_LONG", longitude);
        intent.putExtra("BTN_VALUE", BTN_VALUE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(selectedFdrIndex));
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            intent.putExtra("FEEDERS", FEEDERS);
            intent.putExtra("RDSS_TYPE", RDSS_TYPE);
        }
        intent.putExtra("IS_EDIT", IS_EDIT);
        if (IS_EDIT) {
            intent.putExtra("DATA_MODEL", DATA_MODEL);
        }

        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
    }


    private void openSurveyDialog33KV(MapView mapView, MotionEvent e, String lattitude, String longitude) {

        listAll = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);

        int x = (int) e.getX();
        int y = (int) e.getY();
        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);

        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
        Point myWGS84Point = (Point) GeometryEngine.project(
                myWebMercatorPoint,
                SpatialReferences.getWgs84()
        );

        Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
        map.setInitialViewpoint(vp);
        mv.setMap(map);

        graphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(graphicsOverlay);

        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CROSS,
                Color.RED,
                12
        );

        graphic = new Graphic(myWGS84Point, symbol);
        graphicsOverlay.getGraphics().add(graphic);

        // ---------------------- CALLOUT ----------------------
        TextView calloutContent = new TextView(getApplicationContext());
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setSingleLine();
        calloutContent.setFocusable(true);

        if (listAll == null || listAll.isEmpty()) {

            calloutContent.setText("1");

        } else {

            int maxPoleNumber = 0;

            for (int i = 0; i < listAll.size(); i++) {

                String poleStr = listAll.get(i).getPOLE_NO();

                if (poleStr != null && !poleStr.trim().isEmpty()) {
                    try {
                        int currentPole = Integer.parseInt(poleStr.trim());
                        if (currentPole > maxPoleNumber) {
                            maxPoleNumber = currentPole;
                        }
                    } catch (NumberFormatException ex) {
                        Log.e("POLE_PARSE_ERROR_33KV", "Invalid POLE_NO: " + poleStr);
                    }
                }
            }

            maxPoleNumber = (maxPoleNumber == 0) ? 1 : (maxPoleNumber + 1);
            calloutContent.setText(String.valueOf(maxPoleNumber));
        }

        // ---------------------- GPS LOCATION ----------------------
        gpsTraker.getLocation();

        double currLat = gpsTraker.getLatitude();
        double currLon = gpsTraker.getLongitude();

        Location currentStartPoint = new Location("locationA");
        currentStartPoint.setLatitude(currLat);
        currentStartPoint.setLongitude(currLon);

        Location currentEndPoint = new Location("locationA");
        currentEndPoint.setLatitude(myWGS84Point.getY());
        currentEndPoint.setLongitude(myWGS84Point.getX());

        currentDistance = currentStartPoint.distanceTo(currentEndPoint);
//INTERMEDIATE POLE DISTANCE LOGIC
//        if (IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33){
//            boolean distance = isThirdPoleWithinLimit(Double.parseDouble(INT_FIRST_POLE_LAT), Double.parseDouble(INT_FIRST_POLE_LON), Double.parseDouble(INT_SECOND_POLE_LAT), Double.parseDouble(INT_SECOND_POLE_LON), Double.parseDouble(lattitude), Double.parseDouble(longitude));
//            System.out.println("DISTNCE_OF_3POLE---->" + distance);
//
//            if (!distance) {
//                openDistanceAlertDialog(mapView, e);
//                return;
//            }
//        }


        // ---------------------- LAST POLE DISTANCE ----------------------
        if (listAll == null || listAll.isEmpty()) {

            lastPoleDistance = 0;

        } else {

            String latStr = listAll.get(listAll.size() - 1).getLATITUDE();
            String lonStr = listAll.get(listAll.size() - 1).getLONGITUDE();

            if (latStr != null && lonStr != null) {

                try {

                    Location lastPoleStartPoint = new Location("locationB");
                    lastPoleStartPoint.setLatitude(Double.parseDouble(latStr));
                    lastPoleStartPoint.setLongitude(Double.parseDouble(lonStr));

                    Location lastPoleEndPoint = new Location("locationB");
                    lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                    lastPoleEndPoint.setLongitude(myWGS84Point.getX());

                    lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);

                } catch (Exception ex) {
                    lastPoleDistance = 0;
                    Log.e("LAST_POLE_DIST_33KV_ERR", ex.toString());
                }

            } else {
                lastPoleDistance = 0;
            }
        }

        // ---------------------- CALLOUT SHOW ----------------------
        mCallout = mapView.getCallout();
        mCallout.setLocation(myWGS84Point);
        mCallout.setContent(calloutContent);
        mCallout.show();

        // ---------------------- INTENT ----------------------
        Intent intent = new Intent(EstimatesMap.this, LocationDetails.class);

        intent.putExtra("poleLat", String.valueOf(myWGS84Point.getX()));
        intent.putExtra("poleLon", String.valueOf(myWGS84Point.getY()));

        intent.putExtra("currentLat", currLat);
        intent.putExtra("currentLon", currLon);

        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("isEdit", false);
        intent.putExtra("is33KVSelected", is33KVSelected);
        intent.putExtra("is11KVSelected", is11KVSelected);
        intent.putExtra("isDTRSelected", isDTRSelected);
        intent.putExtra("isLTSelected", isLTSelected);
        intent.putExtra("lastPoleDistance", lastPoleDistance);
        intent.putExtra("currentDistance", currentDistance);
        intent.putExtra("isFirstPole", isFirstPole);
        intent.putExtra("max_length", max_length);
        intent.putExtra("isMeterSelected", isMETERSelected);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("REG_COUNT", REG_COUNT);
        intent.putExtra("EXI_LAT", lattitude);
        intent.putExtra("EXI_LONG", longitude);
        intent.putExtra("BTN_VALUE", BTN_VALUE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(selectedFdrIndex));
        intent.putExtra("IS_EDIT", IS_EDIT);

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            intent.putExtra("FEEDERS", FEEDERS);
            intent.putExtra("RDSS_TYPE", RDSS_TYPE);
        }

        if (IS_EDIT) {
            intent.putExtra("DATA_MODEL", DATA_MODEL);
        }

        startActivity(intent);
        overridePendingTransition(
                R.anim.activity_slide_from_bottom,
                R.anim.activity_stay
        );
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

    public boolean isThirdPoleWithinLimit(
            double pole1Lat, double pole1Lon,
            double pole2Lat, double pole2Lon,
            double pole3Lat, double pole3Lon
    ) {
        double distanceFromPole1 =
                getDistanceBetweenTwoPoles(pole1Lat, pole1Lon, pole3Lat, pole3Lon);

        double distanceFromPole2 =
                getDistanceBetweenTwoPoles(pole2Lat, pole2Lon, pole3Lat, pole3Lon);

        double totalDistance = distanceFromPole1 + distanceFromPole2;

        System.out.println("Pole 1 → Pole 3 : " + distanceFromPole1 + " meters");
        System.out.println("Pole 2 → Pole 3 : " + distanceFromPole2 + " meters");
        System.out.println("Total distance : " + totalDistance + " meters");

        return totalDistance <= 150;
    }


//    public boolean isThirdPoleWithinLimit(
//            double pole1Lat, double pole1Lon,
//            double pole2Lat, double pole2Lon,
//            double pole3Lat, double pole3Lon
//    ) {
//        double distanceFromPole1 =
//                getDistanceBetweenTwoPoles(pole1Lat, pole1Lon, pole3Lat, pole3Lon);
//
//        double distanceFromPole2 =
//                getDistanceBetweenTwoPoles(pole2Lat, pole2Lon, pole3Lat, pole3Lon);
//
//        System.out.println("Pole 1 → Pole 3 : " + distanceFromPole1 + " meters");
//        System.out.println("Pole 2 → Pole 3 : " + distanceFromPole2 + " meters");
//
//        return distanceFromPole1 <= 80 && distanceFromPole2 <= 80;
//    }


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


    private double getDistanceBetweenPoles(String polLat, String polLon, double fromLat, double fromLon) {
//        System.out.println("CURRENT LOCATION LAT : " + fromLat);
//        System.out.println("CURRENT LOCATION LON : " + fromLon);
//        System.out.println("POLE LOCATION LAT : " + polLat);
//        System.out.println("POLE LOCATION LON : " + polLon);
        Location currentStartPoint = new Location("locationE");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationE");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        // System.out.println("POLE LOCATION DISTANCE : " + currentDistance);
        return currentDistance;
    }

    private double getDistanceBetweenPoles11(String polLat, String polLon, double fromLat, double fromLon) {
//        System.out.println("CURRENT LOCATION LAT 11: " + fromLat);
//        System.out.println("CURRENT LOCATION LON 11: " + fromLon);
//        System.out.println("POLE LOCATION LAT 11: " + polLat);
//        System.out.println("POLE LOCATION LON 11: " + polLon);
        Location currentStartPoint = new Location("locationF");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationF");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        //   System.out.println("POLE LOCATION DISTANCE 11: " + currentDistance);
        return currentDistance;
    }

    private double getDistanceBetweenPolesLT(String polLat, String polLon, double fromLat, double fromLon) {
//        System.out.println("CURRENT LOCATION LAT 11: " + fromLat);
//        System.out.println("CURRENT LOCATION LON 11: " + fromLon);
//        System.out.println("POLE LOCATION LAT 11: " + polLat);
//        System.out.println("POLE LOCATION LON 11: " + polLon);
        Location currentStartPoint = new Location("locationN");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationN");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        // System.out.println("POLE LOCATION DISTANCE LT: " + currentDistance);
        return currentDistance;
    }

    private double getDistanceBetweenPoles33New(
            String poleLat,
            String poleLon,
            double currentLat,
            double currentLon
    ) {
        try {
            Location currentLocation = new Location("current");
            currentLocation.setLatitude(currentLat);   // ✅ LAT
            currentLocation.setLongitude(currentLon); // ✅ LON

            Location poleLocation = new Location("pole");
            poleLocation.setLatitude(Double.parseDouble(poleLon));   // ✅ LAT
            poleLocation.setLongitude(Double.parseDouble(poleLat)); // ✅ LON

            // distance in METERS
            return poleLocation.distanceTo(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE; // safety fallback
        }
    }


    private void transmitImageData() {
        progressDialog.show();
        ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);

        for (NewModel model : polesData) {
            model.setGIS_ID(GIS_ID);
            model.setIMEI_NO("");
            model.setFEEDER_ID("");
            model.setFEEDER_NAME("");
        }

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(polesData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
            // System.out.println(jsonArray);
        }
        String json = gson.toJson(polesData);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        //  Log.v("DATA===>>", "" + json);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingData(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.cancel();
                //  Log.v("RESPONSE===>>", "" + response.body());
                // System.out.println("ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        Toast.makeText(EstimatesMap.this, "Data Transmitted Successfully..", Toast.LENGTH_SHORT).show();
                        onBackPressed();
//                        boolean surveyStatusNEW = databaseEstimates.deleteSurveyStatusNEW(EST_NO);
//                        boolean newSurveyData = databaseEstimates.deleteNewSurveyData(EST_NO);
//                        if (surveyStatusNEW && newSurveyData) {
//
//                            onBackPressed();
//                        } else {
//                            Toast.makeText(EstimatesMap.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        Toast.makeText(EstimatesMap.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void openTransmitSurveyDialog() {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        // Set Data
        title.setText("Transmit Data!!");
        message.setText("Are you sure you want to transmit the Survey data?");

        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.question));

        // Buttons visible
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("YES");
        cancelBtn.setText("NO");

        // Click actions
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            // transmitPendingData();
        });

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background + full width
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            dialog.getWindow().setLayout(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
        }

        dialog.show();
    }
//    private void openTransmitSurveyDialog() {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("TRANSMIT DATA!!")
//                .setMessage("Are you sure you want to transmit the Survey data?")
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("YES", (dialogInterface, which) -> {
//                    transmitImageData();
//
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        }
//    }

    private void transmitPendingData() {
        //loader.setVisibility(View.VISIBLE);
        //transmit_all.setVisibility(View.GONE);
        ArrayList<NewModel> allData = databaseEstimates.getLocalSurveyedData();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(allData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
            // System.out.println(jsonArray);
        }
        String json = gson.toJson(allData);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingDataNew(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // loader.setVisibility(View.GONE);
                // transmit_all.setVisibility(View.GONE);
                //  System.out.println("ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {


//                        if (materialData.isEmpty()) {
//                            openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
//                            //Toast.makeText(TransmitSurvey.this, "Data Transmitted Successfully..", Toast.LENGTH_SHORT).show();
////                            Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
////                            startActivity(intent);
////                            overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
//                        } else {
//                            transmitMaterialData();
//                        }


                    } else {
                        //openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                        //openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                        if (allData != null && !allData.isEmpty()) {

//                            openDialogForSuccessMsg(
//                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
//                                    allData.get(0).getGIS_ID(),
//                                    allData.get(0).getEST_NO(),
//                                    allData.get(0).getWORK_ORDER() == null
//                                            ? ""
//                                            : allData.get(0).getWORK_ORDER()
//                            );


                        } else {
                            // list is empty → handle safely
//                            openDialogForSuccessMsg(
//                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
//                                    "",
//                                    "",
//                                    ""
//                            );
                        }
                        //  Toast.makeText(TransmitSurvey.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //transmit_all.setVisibility(View.VISIBLE);
            }
        });

    }


    private void open33kvCompleteSurveyDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Complete Survey!!")
                .setMessage("Are you sure you want to complete the 33kV Survey?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Log.d("ESTIMATES_NO--->", "onClick: " + EST_NO);
                        databaseEstimates.update33KVFeeder5Status(GIS_ID, true, GIS_ID);
                        boolean update33kVSurveyStatusNEW = databaseEstimates.update33kvSurveyStatusNEW(EST_NO, GIS_ID);
                        if (update33kVSurveyStatusNEW) {
                            Toast.makeText(EstimatesMap.this, "33kV Survey Completed Successfully..", Toast.LENGTH_SHORT).show();
                            Intent intent;
                            if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                                intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
                            } else {
                                intent = new Intent(EstimatesMap.this, OfflineHome.class);
                            }

                            //Intent intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
                            intent.putExtra("EST_NO", EST_NO);
                            intent.putExtra("GIS_ID", GIS_ID);
                            intent.putExtra("33KV", is33KVSelected);
                            intent.putExtra("11KV", is11KVSelected);
                            intent.putExtra("LT", isLTSelected);
                            intent.putExtra("MAX_LENGTH", max_length);
                            intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);

                            intent.putExtra("WORK_ORDER", wo_no);
                            intent.putExtra("EST_NATURE", EST_NATURE);
                            intent.putExtra("EST_TYPE", EST_TYPE);
                            intent.putExtra("PURPOSE", PURPOSE);

                            int maxFdrIndex11KV = -1;
                            for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                                if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                                    int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
                                    if (currentFdrIndex > maxFdrIndex11KV) {
                                        maxFdrIndex11KV = currentFdrIndex;
                                    }
                                }
                            }
                            intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
                            intent.putExtra("Model", model);
                            intent.putExtra("FROM", "11KVComplete");

                            startActivity(intent);
                            //  Log.d("EstimatesHomeMaxLength", "onClick: " + max_length);
                        } else {
                            dialog.cancel();
                            Toast.makeText(EstimatesMap.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
                        }

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        }
    }

    private void open11kvCompleteSurveyDialog() {
        new AlertDialog.Builder(EstimatesMap.this)
                .setTitle("Complete Survey!!")
                .setMessage("Are you sure you want to complete the 11kV Survey?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseEstimates.update11KVFeeder5Status(EST_NO, true, GIS_ID);
                        boolean update11kVSurveyStatusNEW = databaseEstimates.update11kvSurveyStatusNEW(EST_NO, GIS_ID);

                        if (update11kVSurveyStatusNEW) {
                            Toast.makeText(EstimatesMap.this, "11kV Survey Completed Successfully..", Toast.LENGTH_SHORT).show();

                            Intent intent;
                            if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                                intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
                            } else {
                                intent = new Intent(EstimatesMap.this, OfflineHome.class);
                            }

                            // Intent intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
                            intent.putExtra("EST_NO", EST_NO);
                            intent.putExtra("GIS_ID", GIS_ID);
                            intent.putExtra("33KV", is33KVSelected);
                            intent.putExtra("11KV", is11KVSelected);
                            intent.putExtra("LT", isLTSelected);
                            intent.putExtra("MAX_LENGTH", max_length);
                            intent.putExtra("EST_NATURE", EST_NATURE);
                            intent.putExtra("EST_TYPE", EST_TYPE);
                            intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
                            intent.putExtra("PURPOSE", PURPOSE);

                            int maxFdrIndex11KV = -1;
                            for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                                if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                                    int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
                                    if (currentFdrIndex > maxFdrIndex11KV) {
                                        maxFdrIndex11KV = currentFdrIndex;
                                    }
                                }
                            }
                            intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
                            intent.putExtra("Model", model);
                            intent.putExtra("FROM", "11KVComplete");

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String json = gson.toJson(model);


                            //     Log.d("MODELCHECK", "onCreate: " + json);

                            startActivity(intent);
                        } else {
                            dialog.cancel();
                            Toast.makeText(EstimatesMap.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
                        }

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void openLTCompleteSurveyDialog() {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        // Title & Message
        title.setText("Complete Survey!!");
        message.setText("Are you sure you want to complete the LT Survey?");
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.question));
        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("YES");
        cancelBtn.setText("NO");

        // YES Click
        okBtn.setOnClickListener(v -> {

            boolean updateLTSurveyStatusNEW =
                    databaseEstimates.updateLTSurveyStatusNEW(EST_NO, GIS_ID);

            if (updateLTSurveyStatusNEW) {

                Toast.makeText(EstimatesMap.this,
                        "LT Survey Completed Successfully..",
                        Toast.LENGTH_SHORT).show();

                Intent intent;

                if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                    intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
                } else {
                    intent = new Intent(EstimatesMap.this, OfflineHome.class);
                }

                intent.putExtra("EST_NO", EST_NO);
                intent.putExtra("GIS_ID", GIS_ID);
                intent.putExtra("33KV", is33KVSelected);
                intent.putExtra("11KV", is11KVSelected);
                intent.putExtra("LT", isLTSelected);
                intent.putExtra("MAX_LENGTH", max_length);
                intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
                intent.putExtra("WORK_ORDER", wo_no);
                intent.putExtra("EST_NATURE", EST_NATURE);
                intent.putExtra("EST_TYPE", EST_TYPE);
                intent.putExtra("PURPOSE", PURPOSE);
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                    intent.putExtra("FEEDERS", FEEDERS);
                    intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                }

                if (model != null) {

                    int maxFdrIndex11KV = -1;

                    for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                        if (model.getFDR_PARTICULARS().get(i)
                                .getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {

                            int currentFdrIndex =
                                    model.getFDR_PARTICULARS().get(i).getFDR_INDEX();

                            if (currentFdrIndex > maxFdrIndex11KV) {
                                maxFdrIndex11KV = currentFdrIndex;
                            }
                        }
                    }

                    intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
                    intent.putExtra("Model", model);
                    intent.putExtra("FROM", "11KVComplete");

                } else {
                    intent.putExtra("Model", model);
                    intent.putExtra("FROM", "11KVComplete");
                }

                startActivity(intent);
                dialog.dismiss();

            } else {
                Toast.makeText(EstimatesMap.this,
                        "Something Went Wrong..",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // NO Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

//    private void openLTCompleteSurveyDialog() {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Complete Survey!!")
//                .setMessage("Are you sure you want to complete the LT Survey?")
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        boolean updateLTSurveyStatusNEW = databaseEstimates.updateLTSurveyStatusNEW(EST_NO, GIS_ID);
//                        if (updateLTSurveyStatusNEW) {
//                            Toast.makeText(EstimatesMap.this, "LT Survey Completed Successfully..", Toast.LENGTH_SHORT).show();
//
//                            Intent intent;
//                            if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
//                                intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
//                            } else {
//                                intent = new Intent(EstimatesMap.this, OfflineHome.class);
//                            }
//
//                            // Intent intent = new Intent(EstimatesMap.this, WorkInvolmentsActivity.class);
//                            intent.putExtra("EST_NO", EST_NO);
//                            intent.putExtra("GIS_ID", GIS_ID);
//                            intent.putExtra("33KV", is33KVSelected);
//                            intent.putExtra("11KV", is11KVSelected);
//                            intent.putExtra("LT", isLTSelected);
//                            intent.putExtra("MAX_LENGTH", max_length);
//                            intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
//                            intent.putExtra("WORK_ORDER", wo_no);
//                            intent.putExtra("EST_NATURE", EST_NATURE);
//                            intent.putExtra("EST_TYPE", EST_TYPE);
//                            if (model != null) {
//                                int maxFdrIndex11KV = -1;
//                                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
//                                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
//                                        int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
//                                        if (currentFdrIndex > maxFdrIndex11KV) {
//                                            maxFdrIndex11KV = currentFdrIndex;
//                                        }
//                                    }
//                                }
//                                intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
//                                intent.putExtra("Model", model);
//                                intent.putExtra("FROM", "11KVComplete");
//                                startActivity(intent);
//                            } else {
//                                // intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
//                                intent.putExtra("Model", model);
//                                intent.putExtra("FROM", "11KVComplete");
//                                startActivity(intent);
//                            }
//
//
//                            // Log.d("MAX_LENGTH---->", "onClick: " + max_length);
//                        } else {
//                            dialog.cancel();
//                            Toast.makeText(EstimatesMap.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        }
//    }

    HashMap<String, Graphic> markerMap = new HashMap<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private volatile boolean isMapActivityDestroyed = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        try {
            list33KV = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
            list11KV = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);
            listLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);

            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                home_heading.setText("33 kV  Survey");
            }
            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                home_heading.setText("11 kV  Survey");
            }
            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")) {
                home_heading.setText("LT  Survey");
            }

            System.out.println("CURRENT_SURVEY_INFO" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);


            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")) {
                if (listLT != null && !listLT.isEmpty()) {
                    if (!WorkInvolmentsActivity.IS_LT_FIRST_POLE) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete LT Survey");
                    }
                } else {
                    btn_complete_survey.setVisibility(View.GONE);
                }
            }


            for (int i = 0; i < list11KV.size(); i++) {
                // System.out.println("FEEDER ID DB " + list11KV.get(i).getFEEDER_ID());
                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_1++;

                }
                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_2++;

                }
                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_3++;

                }
                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_4++;

                }
                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_5++;

                }

                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id6_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_6++;
                }

                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id7_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_7++;
                }

                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id8_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_8++;
                }

                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id9_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_9++;
                }

                if (list11KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id10_11kv)) {
                    LOCATION_COUNT_11KV_FEEDER_10++;
                }
            }

            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                    // btn_complete_survey.setText("Complete FEEDER 1 Survey");
                    if (LOCATION_COUNT_11KV_FEEDER_1 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 1 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                    if (LOCATION_COUNT_11KV_FEEDER_2 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 2 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }

                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                    if (LOCATION_COUNT_11KV_FEEDER_3 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 3 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                    if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 4 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                    if (LOCATION_COUNT_11KV_FEEDER_5 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete 11kV Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 6")) {
                    // btn_complete_survey.setText("Complete FEEDER 6 Survey");
                    if (LOCATION_COUNT_11KV_FEEDER_6 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 6 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }

                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 7")) {
                    // btn_complete_survey.setText("Complete FEEDER 7 Survey");
                    if (LOCATION_COUNT_11KV_FEEDER_7 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 7 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }

                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 8")) {
                    // btn_complete_survey.setText("Complete FEEDER 8 Survey");
                    if (LOCATION_COUNT_11KV_FEEDER_8 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 8 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }

                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 9")) {
                    // btn_complete_survey.setText("Complete FEEDER 9 Survey");
                    if (LOCATION_COUNT_11KV_FEEDER_9 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 9 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }

                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 10")) {
                    // btn_complete_survey.setText("Complete FEEDER 10 Survey");
                    if (LOCATION_COUNT_11KV_FEEDER_10 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete feeder 10 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
            }


            //NEW CODE FOR 33KV LINE EXTENSION STARTS

            for (int i = 0; i < list33KV.size(); i++) {
                //   System.out.println("FEEDER ID DB " + list11KV.get(i).getFEEDER_ID());
                if (list33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_1++;

                }
                if (list33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_2++;

                }
                if (list33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_3++;

                }
                if (list33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_4++;

                }
                if (list33KV.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_33kv)) {
                    LOCATION_COUNT_33KV_FEEDER_5++;

                }
            }

            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 1")) {
                    // btn_complete_survey.setText("Complete FEEDER 1 Survey");
                    if (LOCATION_COUNT_33KV_FEEDER_1 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete FEEDER 1 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 2")) {
                    if (LOCATION_COUNT_33KV_FEEDER_2 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete FEEDER 2 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }

                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 3")) {
                    if (LOCATION_COUNT_33KV_FEEDER_3 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete FEEDER 3 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 4")) {
                    if (LOCATION_COUNT_33KV_FEEDER_4 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete FEEDER 4 Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
                if (WorkInvolmentsActivity.CLICKED_FEEDER.equalsIgnoreCase("FEEDER 5")) {
                    if (LOCATION_COUNT_33KV_FEEDER_5 > 0) {
                        btn_complete_survey.setVisibility(View.VISIBLE);
                        btn_complete_survey.setText("Complete 33kV Survey");
                        reload_layout.setVisibility(View.GONE);
                    } else {
                        btn_complete_survey.setVisibility(View.GONE);
                    }
                }
            }

            //NEW CODE FOR 33KV LINE EXTENSION ENDS


            //NEW CODE FOR 33KV LINE EXTENSION START

            if (!list33KV.isEmpty()) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                ArrayList<NewModel> list33KVFeeder1 = new ArrayList<>();
                ArrayList<NewModel> list33KVFeeder2 = new ArrayList<>();
                ArrayList<NewModel> list33KVFeeder3 = new ArrayList<>();
                ArrayList<NewModel> list33KVFeeder4 = new ArrayList<>();
                ArrayList<NewModel> list33KVFeeder5 = new ArrayList<>();

                //UPDATED CODE

                if (WorkInvolmentsActivity.FEEDER_1_SELECTED_33) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feederList = new ArrayList<>();

                        for (NewModel model : list33KV) {
                            if (model.getFEEDER_ID()
                                    .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_33kv)) {
                                feederList.add(model);
                            }
                        }

                        if (feederList.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feederList) {

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.RED,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.CYAN;
                            System.out.println("POLE_CONFIG----->" + surveyVo.getPOLE_CONFIG());

                            switch (surveyVo.getPOLE_CONFIG()) {

                                case "33/11kv SS-Bay":
                                    labelText = "33/11kV SS-33kV Bay";
                                    break;

                                case "33KV/11KV SS-33KV BAY":
                                    labelText = "33kV/11kV SS-33kV Bay";
                                    break;

                                case "33/11KV SS":
                                    labelText = "33/11KV SS";
                                    break;

                                case "HT Service-Pole":
                                    labelText = "33kV HT Service/Meter";
                                    break;

                                case "EHT SS-Bay":
                                    labelText = "EHT SS-33kV Bay";
                                    break;
                            }

                            // marker
                            pointGraphics.add(new Graphic(point, symbol));

                            // config label
                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // 🔴 POLE NUMBER LABEL (ABSOLUTE)
                            TextSymbol poleNoText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            poleNoText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, poleNoText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feederList) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feederList) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE()),
                                            SpatialReferences.getWgs84()
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);

                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.SOLID,
                                                Color.BLUE,
                                                2f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                // midpoint 33KV label
                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "33KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();

                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // set viewpoint once
                            NewModel first = feederList.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }


                if (WorkInvolmentsActivity.FEEDER_2_SELECTED_33) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder2List = new ArrayList<>();

                        for (NewModel model : list33KV) {
                            if (model.getFEEDER_ID()
                                    .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_33kv)) {
                                feeder2List.add(model);
                            }
                        }

                        if (feeder2List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder2List) {

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(
                                            surveyVo.getIS_EXISTING_LOCATION_NEW()
                                    );

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.RED,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.CYAN;

                            switch (surveyVo.getPOLE_CONFIG()) {
                                case "33/11kv SS-Bay":
                                    labelText = "33/11kV SS-33kV Bay";
                                    break;

                                case "33/11KV SS":
                                    labelText = "33/11KV SS";
                                    break;

                                case "HT Service-Pole":
                                    labelText = "33kV HT Service/Meter";
                                    break;

                                case "EHT SS-Bay":
                                    labelText = "EHT SS-33kV Bay";
                                    break;
                            }

                            // Marker
                            pointGraphics.add(new Graphic(point, symbol));

                            // Equipment / config label
                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // 🔴 POLE NUMBER LABEL (ABSOLUTE)
                            TextSymbol poleNoText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            poleNoText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, poleNoText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder2List) {

                            PointCollection pc =
                                    new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder2List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE()),
                                            SpatialReferences.getWgs84()
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);

                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.SOLID,
                                                Color.BLUE,
                                                2f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                // Midpoint 33KV label
                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "33KV",
                                        Color.RED,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feeder2Overlay = new GraphicsOverlay();

                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feeder2Overlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feeder2Overlay.getGraphics().addAll(pointGraphics);
                            feeder2Overlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);

                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder2List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }


                // FEEDER 3
                if (WorkInvolmentsActivity.FEEDER_3_SELECTED_33) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder3List = new ArrayList<>();

                        for (NewModel model : list33KV) {
                            if (model.getFEEDER_ID()
                                    .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_33kv)) {
                                feeder3List.add(model);
                            }
                        }

                        if (feeder3List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder3List) {

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(
                                            surveyVo.getIS_EXISTING_LOCATION_NEW()
                                    );

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.RED,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.CYAN;

                            switch (surveyVo.getPOLE_CONFIG()) {
                                case "33/11kv SS-Bay":
                                    labelText = "33/11kV SS-33kV Bay";
                                    break;

                                case "33/11KV SS":
                                    labelText = "33/11KV SS";
                                    break;

                                case "HT Service-Pole":
                                    labelText = "33kV HT Service/Meter";
                                    break;

                                case "EHT SS-Bay":
                                    labelText = "EHT SS-33kV Bay";
                                    break;
                            }

                            // Marker
                            pointGraphics.add(new Graphic(point, symbol));

                            // Equipment / config label
                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // 🔴 POLE NUMBER LABEL (ABSOLUTE)
                            TextSymbol poleNoText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            poleNoText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, poleNoText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder3List) {

                            PointCollection pc =
                                    new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder3List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE()),
                                            SpatialReferences.getWgs84()
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);

                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.SOLID,
                                                Color.BLUE,
                                                2f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                // midpoint "33KV" label
                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "33KV",
                                        Color.RED,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feeder3Overlay = new GraphicsOverlay();

                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feeder3Overlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feeder3Overlay.getGraphics().addAll(pointGraphics);
                            feeder3Overlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);

                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder3List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }


                // FEEDER 4
                if (WorkInvolmentsActivity.FEEDER_4_SELECTED_33) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder4List = new ArrayList<>();

                        for (NewModel model : list33KV) {
                            if (model.getFEEDER_ID()
                                    .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_33kv)) {
                                feeder4List.add(model);
                            }
                        }

                        if (feeder4List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder4List) {

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(
                                            surveyVo.getIS_EXISTING_LOCATION_NEW()
                                    );

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.RED,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.CYAN;

                            switch (surveyVo.getPOLE_CONFIG()) {
                                case "33/11kv SS-Bay":
                                    labelText = "33/11kV SS-33kV Bay";
                                    break;
                                case "33/11KV SS":
                                    labelText = "33/11KV SS";
                                    break;
                                case "HT Service-Pole":
                                    labelText = "33kV HT Service/Meter";
                                    break;
                                case "EHT SS-Bay":
                                    labelText = "EHT SS-33kV Bay";
                                    break;
                            }

                            // Marker
                            pointGraphics.add(new Graphic(point, symbol));

                            // Equipment / config label
                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // 🔴 POLE NUMBER LABEL (ABSOLUTE)
                            TextSymbol poleNoText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            poleNoText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, poleNoText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder4List) {

                            PointCollection pc =
                                    new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder4List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE()),
                                            SpatialReferences.getWgs84()
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);

                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.SOLID,
                                                Color.BLUE,
                                                2f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                // midpoint "33KV" label
                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "33KV",
                                        Color.RED,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feeder4Overlay = new GraphicsOverlay();

                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feeder4Overlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feeder4Overlay.getGraphics().addAll(pointGraphics);
                            feeder4Overlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);

                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder4List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }


                // FEEDER 5
                if (WorkInvolmentsActivity.FEEDER_5_SELECTED_33) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder5List = new ArrayList<>();

                        for (NewModel model : list33KV) {
                            if (model.getFEEDER_ID()
                                    .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_33kv)) {
                                feeder5List.add(model);
                            }
                        }

                        if (feeder5List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder5List) {

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(
                                            surveyVo.getIS_EXISTING_LOCATION_NEW()
                                    );

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.RED,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.CYAN;

                            switch (surveyVo.getPOLE_CONFIG()) {
                                case "33/11kv SS-Bay":
                                    labelText = "33/11kV SS-33kV Bay";
                                    break;
                                case "33/11KV SS":
                                    labelText = "33/11KV SS";
                                    break;
                                case "HT Service-Pole":
                                    labelText = "33kV HT Service/Meter";
                                    break;
                                case "EHT SS-Bay":
                                    labelText = "EHT SS-33kV Bay";
                                    break;
                            }

                            // Marker
                            pointGraphics.add(new Graphic(point, symbol));

                            // Equipment / config label
                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // 🔴 POLE NUMBER LABEL (ABSOLUTE)
                            TextSymbol poleNoText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            poleNoText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, poleNoText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder5List) {

                            PointCollection pc =
                                    new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder5List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE()),
                                            SpatialReferences.getWgs84()
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);

                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.SOLID,
                                                Color.BLUE,
                                                2f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                // midpoint "33KV" label
                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "33KV",
                                        Color.RED,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feeder5Overlay = new GraphicsOverlay();

                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feeder5Overlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feeder5Overlay.getGraphics().addAll(pointGraphics);
                            feeder5Overlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);

                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder5List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }


            }

            //NEW CODE FOR 33KV LINE EXTENSION END


            //     Log.d("MAP ACT", "list11KV: " + list11KV.size());
            if (list11KV.size() > 0) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                ArrayList<NewModel> list11KVFeeder1 = new ArrayList<>();
                ArrayList<NewModel> list11KVFeeder2 = new ArrayList<>();
                ArrayList<NewModel> list11KVFeeder3 = new ArrayList<>();
                ArrayList<NewModel> list11KVFeeder4 = new ArrayList<>();
                ArrayList<NewModel> list11KVFeeder5 = new ArrayList<>();

                //UPDATED CODE FOR 11KV

                // FEEDER 1 – 11KV
                if (WorkInvolmentsActivity.FEEDER_1_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // absolute text
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }

                // FEEDER 2 – 11KV
                if (WorkInvolmentsActivity.FEEDER_2_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // absolute label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }

                // FEEDER 3 – 11KV
                if (WorkInvolmentsActivity.FEEDER_3_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // absolute elevation label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }

                // FEEDER 4 – 11KV
                if (WorkInvolmentsActivity.FEEDER_4_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // absolute elevation label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }

                // FEEDER 5 – 11KV
                if (WorkInvolmentsActivity.FEEDER_5_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // Absolute (elevation) label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }
                if (WorkInvolmentsActivity.FEEDER_6_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id6_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id6_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // Absolute (elevation) label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }
                if (WorkInvolmentsActivity.FEEDER_7_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id7_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id7_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // Absolute (elevation) label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }
                if (WorkInvolmentsActivity.FEEDER_8_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id8_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id8_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // Absolute (elevation) label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }
                if (WorkInvolmentsActivity.FEEDER_9_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();
                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id9_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id9_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // Absolute (elevation) label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }
                if (WorkInvolmentsActivity.FEEDER_10_SELECTED) {

                    executor.execute(() -> {

                        // 1️⃣ FILTER FEEDER (BACKGROUND THREAD)
                        List<NewModel> feeder11List = new ArrayList<>();

                        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id10_11kv)) {
                                    // Check latitude and longitude
                                    if (TextUtils.isEmpty(model.getLATITUDE()) ||
                                            TextUtils.isEmpty(model.getLONGITUDE())) {
                                        continue;
                                    }
                                    String poleLat = model.getLATITUDE();
                                    String poleLon = model.getLONGITUDE();


                                    // Calculate distance between USER LOCATION and POLE LOCATION
                                    double distance = getDistanceBetweenPoles33New(
                                            String.valueOf(poleLat),
                                            String.valueOf(poleLon),
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );
                                    // Add only poles within 500 meters
                                    if (distance <= 500) {
                                        feeder11List.add(model);
                                    }
                                    //feeder11List.add(model);
                                }

                            }
                        } else {
                            for (NewModel model : list11KV) {
                                if (model.getFEEDER_ID()
                                        .equalsIgnoreCase(WorkInvolmentsActivity.feeder_id10_11kv)) {
                                    feeder11List.add(model);
                                }
                            }
                        }

                        if (feeder11List.isEmpty()) return;

                        // 2️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                        List<Graphic> pointGraphics = new ArrayList<>();
                        List<Graphic> labelGraphics = new ArrayList<>();
                        List<Graphic> absoluteGraphics = new ArrayList<>();
                        List<Graphic> lineGraphics = new ArrayList<>();

                        for (NewModel surveyVo : feeder11List) {

                            if ("From 33".equalsIgnoreCase(surveyVo.getPOLE_CONFIG())) continue;

                            Point point = new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            boolean isExisting =
                                    "Existing".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                    isExisting
                                            ? SimpleMarkerSymbol.Style.CIRCLE
                                            : SimpleMarkerSymbol.Style.CROSS,
                                    Color.BLUE,
                                    12
                            );

                            String labelText = "";
                            int labelColor = Color.BLUE;

                            labelColor = switch (surveyVo.getPOLE_CONFIG()) {
                                case "SS BAY" -> {
                                    labelText = "11kV SS-Bay";
                                    yield Color.CYAN;
                                }
                                case "DTR Pole" -> {
                                    labelText = "DTR";
                                    yield Color.BLUE;
                                }
                                case "HT Service-Pole" -> {
                                    labelText = "11kV HTTVR Meter";
                                    yield Color.CYAN;
                                }
                                default -> labelColor;
                            };

                            pointGraphics.add(new Graphic(point, symbol));

                            if (!labelText.isEmpty()) {
                                TextSymbol textSymbol = new TextSymbol(
                                        12,
                                        labelText,
                                        labelColor,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );
                                labelGraphics.add(new Graphic(point, textSymbol));
                            }

                            // Absolute (elevation) label
                            TextSymbol absText = new TextSymbol(
                                    15,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")
                                            ? "DTR-" + surveyVo.getPOLE_NO()
                                            : surveyVo.getPOLE_NO(),
                                    Color.WHITE,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );
                            absText.setOffsetY(20);

                            absoluteGraphics.add(new Graphic(point, absText));
                        }

                        // 3️⃣ CREATE POLYLINES (BACKGROUND THREAD)
                        for (NewModel surveyVo : feeder11List) {

                            PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                            for (NewModel m : feeder11List) {
                                if (surveyVo.getFROM_POLE()
                                        .equalsIgnoreCase(m.getPOLE_NO())) {

                                    pc.add(new Point(
                                            Double.parseDouble(m.getLATITUDE()),
                                            Double.parseDouble(m.getLONGITUDE())
                                    ));
                                }
                            }

                            pc.add(new Point(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE())
                            ));

                            if (pc.size() >= 2) {

                                Polyline polyline = new Polyline(pc);
                                SimpleLineSymbol lineSymbol =
                                        new SimpleLineSymbol(
                                                SimpleLineSymbol.Style.DASH_DOT_DOT,
                                                Color.RED,
                                                6f
                                        );

                                lineGraphics.add(new Graphic(polyline, lineSymbol));

                                Point start = pc.get(0);
                                Point end = pc.get(1);

                                Point midpoint = new Point(
                                        (start.getX() + end.getX()) / 2,
                                        (start.getY() + end.getY()) / 2,
                                        SpatialReferences.getWgs84()
                                );

                                TextSymbol kvLabel = new TextSymbol(
                                        12,
                                        "11KV",
                                        Color.GREEN,
                                        TextSymbol.HorizontalAlignment.RIGHT,
                                        TextSymbol.VerticalAlignment.TOP
                                );

                                lineGraphics.add(new Graphic(midpoint, kvLabel));
                            }
                        }

                        // 4️⃣ UPDATE UI (MAIN THREAD ONLY)
                        mainHandler.post(() -> {

                            GraphicsOverlay feederOverlay = new GraphicsOverlay();
                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                            absoluteOverlay.getSceneProperties()
                                    .setSurfacePlacement(
                                            LayerSceneProperties.SurfacePlacement.ABSOLUTE
                                    );

                            mv.getGraphicsOverlays().add(feederOverlay);
                            mv.getGraphicsOverlays().add(absoluteOverlay);

                            feederOverlay.getGraphics().addAll(pointGraphics);
                            feederOverlay.getGraphics().addAll(labelGraphics);
                            absoluteOverlay.getGraphics().addAll(absoluteGraphics);
                            mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                            // Set viewpoint ONCE
                            NewModel first = feeder11List.get(0);
                            Point vpPoint = new Point(
                                    Double.parseDouble(first.getLATITUDE()),
                                    Double.parseDouble(first.getLONGITUDE()),
                                    SpatialReferences.getWgs84()
                            );

                            map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                            mv.setMap(map);
                        });
                    });
                }


            }


            //EXT POLES LOAD 11KV


            //  Log.d("MAP ACT", "listLT: " + listLT.size());
//
            if (listLT != null && !listLT.isEmpty()) {

                executor.execute(() -> {

                    // 1️⃣ PREPARE GRAPHICS (BACKGROUND THREAD)
                    List<Graphic> poleGraphics = new ArrayList<>();
                    List<Graphic> poleLabels = new ArrayList<>();
                    List<Graphic> absoluteLabels = new ArrayList<>();
                    List<Graphic> lineGraphics = new ArrayList<>();

                    for (NewModel surveyVo : listLT) {

                        Point point = new Point(
                                Double.parseDouble(surveyVo.getLATITUDE()),
                                Double.parseDouble(surveyVo.getLONGITUDE()),
                                SpatialReferences.getWgs84()
                        );

                        boolean isExisting =
                                "Yes".equalsIgnoreCase(surveyVo.getIS_EXISTING_LOCATION_NEW());

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                isExisting ? SimpleMarkerSymbol.Style.CIRCLE
                                        : SimpleMarkerSymbol.Style.CROSS,
                                Color.CYAN,
                                17
                        );

                        poleGraphics.add(new Graphic(point, symbol));

                        // Pole label
                        TextSymbol poleText = new TextSymbol(
                                15,
                                surveyVo.getPOLE_NO(),
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        poleText.setOffsetY(20);
                        absoluteLabels.add(new Graphic(point, poleText));
                    }

                    // 2️⃣ CREATE LT POLYLINES (BACKGROUND THREAD)
                    for (NewModel surveyVo : listLT) {

                        PointCollection pc = new PointCollection(SpatialReferences.getWgs84());

                        // From LT pole
                        for (NewModel lt : listLT) {
                            if (surveyVo.getFROM_POLE().equalsIgnoreCase(lt.getPOLE_NO())) {
                                pc.add(new Point(
                                        Double.parseDouble(lt.getLATITUDE()),
                                        Double.parseDouble(lt.getLONGITUDE())
                                ));
                            }
                        }

                        // From 11KV pole
                        for (NewModel kv : list11KV) {
                            if (surveyVo.getFROM_POLE().equalsIgnoreCase(kv.getPOLE_NO())) {
                                pc.add(new Point(
                                        Double.parseDouble(kv.getLATITUDE()),
                                        Double.parseDouble(kv.getLONGITUDE())
                                ));
                            }
                        }

                        pc.add(new Point(
                                Double.parseDouble(surveyVo.getLATITUDE()),
                                Double.parseDouble(surveyVo.getLONGITUDE())
                        ));

                        if (pc.size() >= 2) {

                            Polyline polyline = new Polyline(pc);
                            SimpleLineSymbol lineSymbol =
                                    new SimpleLineSymbol(
                                            SimpleLineSymbol.Style.DOT,
                                            Color.CYAN,
                                            4f
                                    );

                            lineGraphics.add(new Graphic(polyline, lineSymbol));

                            // Midpoint label
                            Point start = pc.get(0);
                            Point end = pc.get(1);

                            Point midpoint = new Point(
                                    (start.getX() + end.getX()) / 2,
                                    (start.getY() + end.getY()) / 2,
                                    SpatialReferences.getWgs84()
                            );

                            TextSymbol ltLabel = new TextSymbol(
                                    12,
                                    "LT",
                                    Color.CYAN,
                                    TextSymbol.HorizontalAlignment.LEFT,
                                    TextSymbol.VerticalAlignment.TOP
                            );

                            lineGraphics.add(new Graphic(midpoint, ltLabel));
                        }
                    }

                    // 3️⃣ UPDATE UI (MAIN THREAD ONLY)
                    mainHandler.post(() -> {

                        GraphicsOverlay poleOverlay = new GraphicsOverlay();
                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties()
                                .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);

                        mv.getGraphicsOverlays().add(poleOverlay);
                        mv.getGraphicsOverlays().add(absoluteOverlay);

                        poleOverlay.getGraphics().addAll(poleGraphics);
                        absoluteOverlay.getGraphics().addAll(absoluteLabels);
                        mGraphicsOverlay.getGraphics().addAll(lineGraphics);

                        // Set viewpoint ONCE
                        NewModel first = listLT.get(0);
                        Point vpPoint = new Point(
                                Double.parseDouble(first.getLATITUDE()),
                                Double.parseDouble(first.getLONGITUDE()),
                                SpatialReferences.getWgs84()
                        );

                        map.setInitialViewpoint(new Viewpoint(vpPoint, 7500));
                        mv.setMap(map);
                    });
                });
            }


            listAll = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);


            //UPDATED CODE FOR LT
            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")
                    && WorkInvolmentsActivity.IS_LT_FIRST_POLE
                    && !WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY) {

                executor.execute(() -> {

                    // 1️⃣ FIND MATCHING DTR (BACKGROUND THREAD)
                    NewModel matchedDtr = null;

                    String selectedCode =
                            WorkInvolmentsActivity.SELECTED_LT_CKT.split("_LTCKT_")[0];

                    for (NewModel dtr : WorkInvolmentsActivity.existingDtrDetails) {
                        if (selectedCode.equalsIgnoreCase(dtr.getDTR_STRUCTURE_CODE())) {
                            matchedDtr = dtr;
                            break;
                        }
                    }

                    if (matchedDtr == null) return;

                    double lat = Double.parseDouble(matchedDtr.getLATITUDE());
                    double lon = Double.parseDouble(matchedDtr.getLONGITUDE());

                    Point dtrPoint = new Point(lat, lon, SpatialReferences.getWgs84());

                    SimpleMarkerSymbol dtrSymbol =
                            new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, Color.RED, 17);

                    Graphic dtrGraphic = new Graphic(dtrPoint, dtrSymbol);

                    TextSymbol dtrLabel = new TextSymbol(
                            15,
                            "DTR-" + matchedDtr.getDTR_STRUCTURE_CODE(),
                            Color.WHITE,
                            TextSymbol.HorizontalAlignment.LEFT,
                            TextSymbol.VerticalAlignment.MIDDLE
                    );
                    dtrLabel.setOffsetY(20);

                    Graphic dtrTextGraphic = new Graphic(dtrPoint, dtrLabel);

                    // 2️⃣ UPDATE UI (MAIN THREAD ONLY)
                    mainHandler.post(() -> {

                        // -------- BASE OVERLAY --------
                        GraphicsOverlay dtrOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(dtrOverlay);

                        Graphic baseDtrGraphic = new Graphic(
                                dtrPoint,
                                dtrGraphic.getSymbol()
                        );

                        dtrOverlay.getGraphics().add(baseDtrGraphic);
                        dtrOverlay.setLabelsEnabled(true);

                        // -------- ABSOLUTE TEXT OVERLAY --------
                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties()
                                .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);

                        Graphic absolutePointGraphic = new Graphic(
                                dtrPoint,
                                dtrGraphic.getSymbol()
                        );

                        absoluteOverlay.getGraphics().add(absolutePointGraphic);
                        absoluteOverlay.getGraphics().add(dtrTextGraphic);

                        mv.getGraphicsOverlays().add(absoluteOverlay);

                        // -------- SET VIEWPOINT ONCE --------
                        map.setInitialViewpoint(new Viewpoint(dtrPoint, 7500));
                        mv.setMap(map);
                    });

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showAlertDialog(Point point) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Are you want to delete or Edit the Pole");
//        builder.setPositiveButton("Edit", (dialog, which) -> {
//            SurveyDialog greeting = new SurveyDialog(EstimatesMap.this, String.valueOf(point.getX()),
//                    String.valueOf(point.getY()), mCallout, graphicsOverlay, graphic, gpsTraker.getLatitude(), gpsTraker.getLongitude(),
//                    EST_NO, is33KVSelected, is11KVSelected, isDTRSelected, isLTSelected, true, lastPoleDistance, currentDistance, false, max_length);
//            greeting.show();
//
//        });
        ArrayList<NewModel> editList = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);
        NewModel lastItem = editList.get(editList.size() - 1);
        builder.setNegativeButton("Delete", (dialog, which) -> {

            databaseEstimates.deletePoleNumber(lastItem.getPOLE_NO(), EST_NO, lastItem.getVOLTAGE_LEVEL(), GIS_ID);
            this.finish();


        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setCurrentLocation() {
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled && !network_enabled) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("GPS Alert")
                        .setMessage("Please Enable GPS")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
                }

            } else {

                /* ** ADD ** */
                mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                    if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                        return;
                    }


                    String message = String.format("Error in DataSourceStatusChangedListener: %s",
                            dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(EstimatesMap.this, message, Toast.LENGTH_LONG).show();

                });
                // mLocationDisplay.stop();
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (IS_EDIT) {
            super.onBackPressed();
        } else {
            Intent playIntent = new Intent(EstimatesMap.this, SurveyList.class); //Intent.ACTION_VIEW);
            startActivity(playIntent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        if (mv != null) {
            mv.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isMapActivityDestroyed = true;
        mainHandler.removeCallbacksAndMessages(null);
        executor.shutdownNow();

        if (mLocationDisplay != null) {
            mLocationDisplay.stop();
        }

        if (mv != null) {
            try {
                mv.dispose();
            } catch (Exception e) {
                Log.w("EstimatesMap", "Error disposing MapView", e);
            }
        }

        super.onDestroy();
    }


    private void handle11KvFeederUI() {

        String clickedFeeder = WorkInvolmentsActivity.CLICKED_FEEDER;
        if (clickedFeeder == null) return;

        String feederId = getFeederId(clickedFeeder);
        boolean matchFound = false;

        if (feederId != null && list11KV != null) {
            for (NewModel model : list11KV) {
                if (model.getFEEDER_ID() != null &&
                        feederId.equalsIgnoreCase(model.getFEEDER_ID())) {
                    matchFound = true;
                    break;
                }
            }
        }

        boolean showReload = false;

        switch (clickedFeeder) {
            case "FEEDER 1":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_1;
                break;
            case "FEEDER 2":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_2;
                break;
            case "FEEDER 3":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_3;
                break;
            case "FEEDER 4":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_4;
                break;
            case "FEEDER 5":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_5;
                break;

            case "FEEDER 6":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_6;
                break;
            case "FEEDER 7":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_7;
                break;

            case "FEEDER 8":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_8;
                break;

            case "FEEDER 9":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_9;
                break;
            case "FEEDER 10":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_11_10;
                break;
        }

        reload_layout.setVisibility(
                showReload && !matchFound ? View.VISIBLE : View.GONE
        );
    }

    private void handle33KvFeederUI() {

        String clickedFeeder = WorkInvolmentsActivity.CLICKED_FEEDER;
        String feederId = getFeederId33kv(clickedFeeder);

        boolean matchFound = false;

        if (feederId != null && list33KV != null) {
            for (NewModel model : list33KV) {
                if (model.getFEEDER_ID() != null &&
                        feederId.equalsIgnoreCase(model.getFEEDER_ID())) {
                    matchFound = true;
                    break;
                }
            }
        }

        boolean showReload = false;

        switch (clickedFeeder) {
            case "FEEDER 1":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_33_1;
                break;
            case "FEEDER 2":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_33_2;
                break;
            case "FEEDER 3":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_33_3;
                break;
            case "FEEDER 4":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_33_4;
                break;
            case "FEEDER 5":
                showReload = WorkInvolmentsActivity.IS_LINE_EXTENSION_33_5;
                break;
        }

        reload_layout.setVisibility(
                showReload && !matchFound ? View.VISIBLE : View.GONE
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
        isMapActivityDestroyed = false;

        if (mv != null) {
            mv.resume();
        }

        // UI-safe call
        setLoadingMapData(sp_line_survey_type);

        // IMPORTANT: ArcGIS overlays must never be modified from the executor thread.
        clearMapGraphics();

        executor.execute(() -> {
            if (isMapActivityDestroyed) return;

            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE == null) return;

            String clickedFeeder = WorkInvolmentsActivity.CLICKED_FEEDER;
            int feederIndex = -1;

            if (clickedFeeder != null && clickedFeeder.startsWith("FEEDER")) {
                try {
                    feederIndex = Integer.parseInt(clickedFeeder.split(" ")[1]);
                } catch (NumberFormatException ignored) {
                }
            }

            if (feederIndex < 1 || feederIndex > 5) return;

            String feederType = "";
            String feederId = "";
            String feederName = "";
            String feederText = "";
            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {

                feederType = getFieldValue("FEEDER_TYPE_11KV_" + feederIndex);
                feederId = getFieldValue("feeder_id" + feederIndex + "_11kv");
                feederName = getFieldValue("feeder_name" + feederIndex + "_11kv");

            } else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {

                feederType = getFieldValue("FEEDER_TYPE_33KV_" + feederIndex);
                feederId = getFieldValue("feeder_id" + feederIndex + "_33kv");
                feederName = getFieldValue("feeder_name" + feederIndex + "_33kv");
            }

            final String finalFeederId = feederId;
            final String finalFeederName = feederName;

            // 🔹 UI UPDATES ONLY HERE
            runOnUiThread(() -> {

                if (!TextUtils.isEmpty(finalFeederName)) {

                    feeder_id.setVisibility(View.VISIBLE);

                    if (finalFeederId.contains("-33KV") || finalFeederId.contains("-11KV")) {
                        feeder_id.setText("Feeder Name: " + finalFeederName);
                    } else {
                        feeder_id.setText(
                                "Feeder Name: " + finalFeederName + " (" + finalFeederId + ")"
                        );
                    }

                }
            });
        });


        //OLD CODE OFFLINE
//        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
//            if (list33KV != null && !list33KV.isEmpty()) {
//                String clickedFeeder = WorkInvolmentsActivity.CLICKED_FEEDER;
//
//                System.out.println("CLICKED_FEEDER_33KV" + clickedFeeder);
//                String feederId = getFeederId33kv(clickedFeeder);
//                boolean matchFound = false;
//
//                for (NewModel model : list33KV) {
//                    if (feederId != null &&
//                            model.getFEEDER_ID() != null &&
//                            feederId.equalsIgnoreCase(model.getFEEDER_ID())) {
//                        matchFound = true;
//                        break;
//                    }
//                }
//                if (!matchFound) {
//                    loadExistingPoles33();
//                }
//                if (clickedFeeder.equalsIgnoreCase("FEEDER 1")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_1) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                } else if (clickedFeeder.equalsIgnoreCase("FEEDER 2")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_2) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                } else if (clickedFeeder.equalsIgnoreCase("FEEDER 3")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_3) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                } else if (clickedFeeder.equalsIgnoreCase("FEEDER 4")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_4) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                } else if (clickedFeeder.equalsIgnoreCase("FEEDER 5")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_33_5) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                }
//
//
//                //  reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//            } else {
//                loadExistingPoles33();
//            }
//
//
//        }

        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {

            // If list is empty, load in background and return
            if (list33KV == null || list33KV.isEmpty()) {
                executor.execute(this::loadExistingPoles33);

            } else {
                // 2️⃣ Data already available → handle UI immediately
                runOnUiThread(this::handle33KvFeederUI);
            }

        }


        //OLD CODE OFFLINE
//        else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
//            if (list11KV != null && !list11KV.isEmpty()) {
//                String clickedFeeder = WorkInvolmentsActivity.CLICKED_FEEDER;
//                String feederId = getFeederId(clickedFeeder);
//                boolean matchFound = false;
//
//                for (NewModel model : list11KV) {
//                    if (feederId != null &&
//                            model.getFEEDER_ID() != null &&
//                            feederId.equalsIgnoreCase(model.getFEEDER_ID())) {
//                        matchFound = true;
//                        break;
//                    }
//                }
//                if (!matchFound) {
//                    loadExistingPoles11();
//                }
//                if (clickedFeeder.equalsIgnoreCase("FEEDER 1")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_1) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                } else if (clickedFeeder.equalsIgnoreCase("FEEDER 2")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_2) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                } else if (clickedFeeder.equalsIgnoreCase("FEEDER 3")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_3) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                } else if (clickedFeeder.equalsIgnoreCase("FEEDER 4")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_4) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                } else if (clickedFeeder.equalsIgnoreCase("FEEDER 5")) {
//                    if (WorkInvolmentsActivity.IS_LINE_EXTENSION_11_5) {
//                        reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//                    }
//                }
//
//                // reload_layout.setVisibility(matchFound ? View.GONE : View.VISIBLE);
//            } else {
//                loadExistingPoles11();
//            }
//        }

        //NEW CODE FOR OFFLINE
        //NEW CODE FOR OFFLINE
        else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {

            // If list is empty, load in background and return
            if (list11KV == null || list11KV.isEmpty()) {
                executor.execute(this::loadExistingPoles11);

            } else {
                handle11KvFeederUI();
            }


        }


//OLD CODE FOR OFFLINE
//        existingPoleDataDistance = ConnectivityChecking.checkConnection(EstimatesMap.this)
//                ? ONLINE_DISTANCE_LIMIT
//                : OFFLINE_DISTANCE_LIMIT;
//
//        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT") &&
//                WorkInvolmentsActivity.IS_LT_FIRST_POLE && WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY) {
//            if (WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY) {
//                reload_layout.setVisibility(View.VISIBLE);
//            } else {
//                reload_layout.setVisibility(View.GONE);
//            }
//            loadExistingPolesLT();
//        }
//
//
//        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")) {
//            if (!WorkInvolmentsActivity.SELECTED_LT_CKT.isEmpty()) {
//                feeder_id.setVisibility(View.VISIBLE);
//                feeder_id.setText("Circuit Name :" + " " + WorkInvolmentsActivity.SELECTED_LT_CKT);
//            }
//        }

        //NEW CODE FOR OFFLINE
        existingPoleDataDistance = ConnectivityChecking.checkConnection(EstimatesMap.this)
                ? ONLINE_DISTANCE_LIMIT
                : OFFLINE_DISTANCE_LIMIT;

        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")
                && WorkInvolmentsActivity.IS_LT_FIRST_POLE
                && WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY) {

            // UI update on main thread
            reload_layout.setVisibility(
                    WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY
                            ? View.VISIBLE
                            : View.GONE
            );


            // 🔹 LOAD DATA IN BACKGROUND (IMPORTANT)
            if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")
                    && WorkInvolmentsActivity.IS_LT_FIRST_POLE) {

                executor.execute(this::loadExistingPolesLT);
            }
        }

// ---------------------------------------------

        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")) {

            if (!TextUtils.isEmpty(WorkInvolmentsActivity.SELECTED_LT_CKT)) {

                feeder_id.setVisibility(View.VISIBLE);
                feeder_id.setText(
                        "Circuit Name : " + WorkInvolmentsActivity.SELECTED_LT_CKT
                );
            }
        }

//        ArrayList<NewModel> list_model = databaseEstimates.getCurrentSurveyData();
//        if (!list_model.isEmpty()){
//            btn_transmit_data.setVisibility(View.GONE);
//        }

    }

    private String getFeederId33kv(String clickedFeeder) {
        switch (clickedFeeder.toUpperCase()) {
            case "FEEDER 1":
                return WorkInvolmentsActivity.feeder_id1_33kv;
            case "FEEDER 2":
                return WorkInvolmentsActivity.feeder_id2_33kv;
            case "FEEDER 3":
                return WorkInvolmentsActivity.feeder_id3_33kv;
            case "FEEDER 4":
                return WorkInvolmentsActivity.feeder_id4_33kv;
            case "FEEDER 5":
                return WorkInvolmentsActivity.feeder_id5_33kv;
            default:
                return null;
        }
    }

    private String getFeederId(String clickedFeeder) {
        switch (clickedFeeder.toUpperCase()) {
            case "FEEDER 1":
                return WorkInvolmentsActivity.feeder_id1_11kv;
            case "FEEDER 2":
                return WorkInvolmentsActivity.feeder_id2_11kv;
            case "FEEDER 3":
                return WorkInvolmentsActivity.feeder_id3_11kv;
            case "FEEDER 4":
                return WorkInvolmentsActivity.feeder_id4_11kv;
            case "FEEDER 5":
                return WorkInvolmentsActivity.feeder_id5_11kv;
            default:
                return null;
        }
    }


    private void loadMaterialMaster() {
        materialList33.clear();
        materialList11.clear();
        materialListLT.clear();
        dtrMeterModels.clear();
        materialList11Pole.clear();
        materialList33Pole.clear();
        load33MaterialMaster();
        load11MaterialMaster();
        loadLTMaterialMaster();

    }

    private void load33MaterialMaster() {
        JsonObject entry = new JsonObject();
        entry.addProperty("survey_type", "33");
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getMaterials(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //   System.out.println("33KV MATERIALS RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {

                        JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                        for (int j = 0; j < array.size(); j++) {
                            JsonObject object = array.get(j).getAsJsonObject();
                            MaterialModel model = new MaterialModel();
                            model.setMATERIAL_CODE(object.get("MATERIALCODE").getAsString());
                            model.setSURVEY_CODE(object.get("SURVEY_CODE").getAsString());
                            model.setSURVEY_TYPE(object.get("SURVEY_TYPE").getAsString());
                            model.setPOLE_CONFIG(object.get("POLE_CONFIG").getAsString());
                            model.setMATERIAL_DESC(object.get("MATERAIL_DESC").getAsString());
                            model.setUOM(object.get("UOM").getAsString());
                            model.setDATA_ID(object.get("DATA_ID").getAsString());
                            model.setMAX(object.get("MAX").getAsString());
                            model.setDATA_TYPE(object.get("DATA_TYPE").getAsString());
                            model.setQTY("0");
                            materialList33.add(model);

                            MaterialModelPole modelPole = new MaterialModelPole();
                            modelPole.setMATERIAL_CODE(object.get("MATERIALCODE").getAsString());
                            modelPole.setSURVEY_CODE(object.get("SURVEY_CODE").getAsString());
                            modelPole.setSURVEY_TYPE(object.get("SURVEY_TYPE").getAsString());
                            modelPole.setPOLE_CONFIG(object.get("POLE_CONFIG").getAsString());
                            modelPole.setMATERIAL_DESC(object.get("MATERAIL_DESC").getAsString());
                            modelPole.setUOM(object.get("UOM").getAsString());
                            modelPole.setDATA_ID(object.get("DATA_ID").getAsString());
                            modelPole.setMAX(object.get("MAX").getAsString());
                            modelPole.setDATA_TYPE(object.get("DATA_TYPE").getAsString());
                            modelPole.setQTY("0");
                            materialList33Pole.add(modelPole);
                        }

                    } else {
                        Toast.makeText(EstimatesMap.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void load11MaterialMaster() {
        JsonObject entry = new JsonObject();
        entry.addProperty("survey_type", "11");

        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getMaterials(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //   System.out.println("33KV MATERIALS RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {

                        JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                        for (int j = 0; j < array.size(); j++) {
                            JsonObject object = array.get(j).getAsJsonObject();

                            MaterialModel model = new MaterialModel();
                            model.setMATERIAL_CODE(object.get("MATERIALCODE").getAsString());
                            model.setSURVEY_CODE(object.get("SURVEY_CODE").getAsString());
                            model.setSURVEY_TYPE(object.get("SURVEY_TYPE").getAsString());
                            model.setPOLE_CONFIG(object.get("POLE_CONFIG").getAsString());
                            model.setMATERIAL_DESC(object.get("MATERAIL_DESC").getAsString());
                            model.setUOM(object.get("UOM").getAsString());
                            model.setDATA_ID(object.get("DATA_ID").getAsString());
                            model.setMAX(object.get("MAX").getAsString());
                            model.setDATA_TYPE(object.get("DATA_TYPE").getAsString());
                            model.setQTY("0");
                            materialList11.add(model);

                            MaterialModelPole modelPole = new MaterialModelPole();
                            modelPole.setMATERIAL_CODE(object.get("MATERIALCODE").getAsString());
                            modelPole.setSURVEY_CODE(object.get("SURVEY_CODE").getAsString());
                            modelPole.setSURVEY_TYPE(object.get("SURVEY_TYPE").getAsString());
                            modelPole.setPOLE_CONFIG(object.get("POLE_CONFIG").getAsString());
                            modelPole.setMATERIAL_DESC(object.get("MATERAIL_DESC").getAsString());
                            modelPole.setUOM(object.get("UOM").getAsString());
                            modelPole.setDATA_ID(object.get("DATA_ID").getAsString());
                            modelPole.setMAX(object.get("MAX").getAsString());
                            modelPole.setDATA_TYPE(object.get("DATA_TYPE").getAsString());
                            modelPole.setQTY("0");

                            materialList11Pole.add(modelPole);

                        }

                    } else {
                        Toast.makeText(EstimatesMap.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void loadLTMaterialMaster() {
        JsonObject entry = new JsonObject();
        entry.addProperty("survey_type", "LT");
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getMaterials(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //    System.out.println("33KV MATERIALS RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {

                        JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                        for (int j = 0; j < array.size(); j++) {
                            JsonObject object = array.get(j).getAsJsonObject();
                            MaterialModel model = new MaterialModel();
                            model.setMATERIAL_CODE(object.get("MATERIALCODE").getAsString());
                            model.setSURVEY_CODE(object.get("SURVEY_CODE").getAsString());
                            model.setSURVEY_TYPE(object.get("SURVEY_TYPE").getAsString());
                            model.setPOLE_CONFIG(object.get("POLE_CONFIG").getAsString());
                            model.setMATERIAL_DESC(object.get("MATERAIL_DESC").getAsString());
                            model.setUOM(object.get("UOM").getAsString());
                            model.setDATA_ID(object.get("DATA_ID").getAsString());
                            model.setMAX(object.get("MAX").getAsString());
                            model.setDATA_TYPE(object.get("DATA_TYPE").getAsString());
                            model.setQTY("0");
                            materialListLT.add(model);

                            DtrMeterModel dtrMeterModel = new DtrMeterModel();
                            dtrMeterModel.setMATERIAL_CODE(object.get("MATERIALCODE").getAsString());
                            dtrMeterModel.setSURVEY_CODE(object.get("SURVEY_CODE").getAsString());
                            dtrMeterModel.setSURVEY_TYPE(object.get("SURVEY_TYPE").getAsString());
                            dtrMeterModel.setPOLE_CONFIG(object.get("POLE_CONFIG").getAsString());
                            dtrMeterModel.setMATERIAL_DESC(object.get("MATERAIL_DESC").getAsString());
                            dtrMeterModel.setUOM(object.get("UOM").getAsString());
                            dtrMeterModel.setDATA_ID(object.get("DATA_ID").getAsString());
                            dtrMeterModel.setMAX(object.get("MAX").getAsString());
                            dtrMeterModel.setDATA_TYPE(object.get("DATA_TYPE").getAsString());
                            dtrMeterModel.setQTY("0");
                            dtrMeterModels.add(dtrMeterModel);
                        }

                    } else {
                        Toast.makeText(EstimatesMap.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void openDistanceAlertDialog(MapView mMapView, MotionEvent e) {
        new AlertDialog.Builder(this)
                .setTitle("Pay Attention")
                .setMessage("Intermediate Pole must be within a total distance of 150 meters from Pole 1 and Pole 2.")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                            EstimatesMap.IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
                            IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
                            EstimatesMap.IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33 = false;
                        } else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                            EstimatesMap.IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
                            IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
                            EstimatesMap.POLE_SURVEY_TYPE_11KV = false;
                            EstimatesMap.IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11 = false;
                        } else if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("LT")) {
                            EstimatesMap.IS_CURRENT_LOCATION_INTERMEDIATE_POLE_LT = false;
                            IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
                        }
                        EstimatesMap.IS_CURRENT_LOCATION_SELECTED = false;
                        EstimatesMap.IS_CURRENT_LOCATION_OTHER_FEEDER = false;
                        EstimatesMap.IS_SAME_EXISTING_FEEDER = false;
//                        EstimatesMap.SELECTED_FEEDER_ID = "";
//                        EstimatesMap.SELECTED_LOCATION_TYPE = "";
//                        EstimatesMap.SELECTED_LOCATION_NUMBER = "";
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private static SpannableString getColoredText(String label, String value, Context context) {

        SpannableString spannable = new SpannableString(label + value);

        spannable.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.ui_gray_color)),
                0,
                label.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannable.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.ui_black_color)),
                label.length(),
                label.length() + value.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Value bold
        spannable.setSpan(
                new StyleSpan(Typeface.BOLD),
                label.length(),
                label.length() + value.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return spannable;
    }

    private void openAlertDialogForIntermediatePoleCheckExisting(
            CheckBox checkBox,
            TextView tv_desc_for_int,
            TextView tv_desc,
            LinearLayout layout_intermediate_poll,
            LinearLayout layout_tapping_point) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);


        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        // Set Title & Message
        title.setText("Pay Attention");
        message.setText("Are you sure want to proceed with intermediate Pole?");
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.question));
        // Show both buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Yes");
        cancelBtn.setText("Cancel");

        // YES Click
        okBtn.setOnClickListener(v -> {
            checkBox.setChecked(true);
            tv_desc_for_int.setVisibility(View.VISIBLE);
            tv_desc.setVisibility(View.GONE);
            layout_intermediate_poll.setVisibility(View.VISIBLE);
            layout_tapping_point.setVisibility(View.GONE);
            dialog.dismiss();
        });

        // CANCEL Click
        cancelBtn.setOnClickListener(v -> {
            checkBox.setChecked(false);
            tv_desc_for_int.setVisibility(View.GONE);
            tv_desc.setVisibility(View.VISIBLE);
            layout_intermediate_poll.setVisibility(View.GONE);
            layout_tapping_point.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }
//    private void openAlertDialogForIntermediatePoleCheckExisting(CheckBox checkBox, TextView tv_desc_for_int, TextView tv_desc, LinearLayout layout_intermediate_poll, LinearLayout layout_tapping_point) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Pay Attention");
//        builder.setMessage("Are you sure want to proceed with intermediate Pole?");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                checkBox.setChecked(true);
//                tv_desc_for_int.setVisibility(View.VISIBLE);
//                tv_desc.setVisibility(View.GONE);
//                layout_intermediate_poll.setVisibility(View.VISIBLE);
//                layout_tapping_point.setVisibility(View.GONE);
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            checkBox.setChecked(false);
//            tv_desc_for_int.setVisibility(View.GONE);
//            tv_desc.setVisibility(View.VISIBLE);
//            layout_intermediate_poll.setVisibility(View.GONE);
//            layout_tapping_point.setVisibility(View.VISIBLE);
//            dialog.dismiss();
//        });
//        AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }


    private void openAlertDialogForIntermediatePoleCheckProposed(CheckBox checkBox) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pay Attention");
        builder.setMessage("Are you sure want to proceed with intermediate Pole?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkBox.setChecked(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            checkBox.setChecked(false);
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.show();
    }

    private void openAlertDialog(int count, String voltage_level) {
        if (isAlertShown) return;

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_pole_count, null);

        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        TextView btnOkay = dialogView.findViewById(R.id.btn_okay);
        Spinner spinner = dialogView.findViewById(R.id.sp_line_survey_type);
        LinearLayout layout_line_survey_type = dialogView.findViewById(R.id.layout_line_survey_type);

        setLoadingMapData(spinner);

        tvTitle.setText(voltage_level + " Feeder Loaded successfully");
        tvMessage.setText("Total number of poles loaded : " + count);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        if (voltage_level.equalsIgnoreCase("LT")) {
            layout_line_survey_type.setVisibility(View.GONE);
        } else {
            layout_line_survey_type.setVisibility(View.VISIBLE);
        }

        btnOkay.setOnClickListener(v -> {
            String selectedValue = spinner.getSelectedItem().toString().replace(" meters", "");
            if (voltage_level.equalsIgnoreCase("LT")) {
                try {
                    double distanceValue = Double.parseDouble(selectedValue);

                    if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                        ONLINE_DISTANCE_LIMIT = distanceValue;
                    } else {
                        OFFLINE_DISTANCE_LIMIT = distanceValue;
                    }

                    existingPoleDataDistance = ConnectivityChecking.checkConnection(EstimatesMap.this)
                            ? ONLINE_DISTANCE_LIMIT
                            : OFFLINE_DISTANCE_LIMIT;

                    if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                        clearMapGraphics();
                        loadExistingPoles33();
                    }
                    if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                        clearMapGraphics();
                        loadExistingPoles11();
                    }


                } catch (NumberFormatException e) {
                    // Toast.makeText(EstimatesMap.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                }

            } else {
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select meters")) {
                    Toast.makeText(this, "Please select the distance that you want", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        double distanceValue = Double.parseDouble(selectedValue);

                        if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                            ONLINE_DISTANCE_LIMIT = distanceValue;
                        } else {
                            OFFLINE_DISTANCE_LIMIT = distanceValue;
                        }

                        existingPoleDataDistance = ConnectivityChecking.checkConnection(EstimatesMap.this)
                                ? ONLINE_DISTANCE_LIMIT
                                : OFFLINE_DISTANCE_LIMIT;

                        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                            clearMapGraphics();
                            loadExistingPoles33();
                        }
                        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                            clearMapGraphics();
                            loadExistingPoles11();
                        }


                    } catch (NumberFormatException e) {
                        Toast.makeText(EstimatesMap.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            dialog.dismiss();


        });
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.show();
        isAlertShown = true;
    }

    private void openAlertDialogExisting(int count, String voltage_level) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_pole_count, null);

        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        TextView btnOkay = dialogView.findViewById(R.id.btn_okay);
        Spinner spinner = dialogView.findViewById(R.id.sp_line_survey_type);
        LinearLayout linearLayout = dialogView.findViewById(R.id.layout_line_survey_type);

        setLoadingMapData(spinner);

        tvTitle.setText(voltage_level + " Feeder Loaded successfully");
        tvMessage.setText("Total number of poles loaded : " + count);
//        if (!voltage_level.equalsIgnoreCase("LT")) {
//            tvMessage.setText("Total number of poles loaded : " + count);
//        }else {
//            tvMessage.setText("Total number of poles loaded : " + count);
//        }

        if (voltage_level.equalsIgnoreCase("LT")) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }


        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        btnOkay.setOnClickListener(v -> {
            String selectedValue = spinner.getSelectedItem().toString().replace(" meters", "");
            System.out.println("SELECTED_METERS-->" + selectedValue);

            if (!voltage_level.equalsIgnoreCase("LT")) {
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(this, "Please select the distance that you want", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        double distanceValue = Double.parseDouble(selectedValue);

                        if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                            ONLINE_DISTANCE_LIMIT = distanceValue;
                        } else {
                            OFFLINE_DISTANCE_LIMIT = distanceValue;
                        }

                        existingPoleDataDistance = ConnectivityChecking.checkConnection(EstimatesMap.this)
                                ? ONLINE_DISTANCE_LIMIT
                                : OFFLINE_DISTANCE_LIMIT;

                        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                            clearMapGraphics();
                            loadExistingPoles33();
                        }
                        if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                            clearMapGraphics();
                            loadExistingPoles11();
                        }


                    } catch (NumberFormatException e) {
                        Toast.makeText(EstimatesMap.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                try {
                    double distanceValue = Double.parseDouble(selectedValue);

                    if (ConnectivityChecking.checkConnection(EstimatesMap.this)) {
                        ONLINE_DISTANCE_LIMIT = distanceValue;
                    } else {
                        OFFLINE_DISTANCE_LIMIT = distanceValue;
                    }

                    existingPoleDataDistance = ConnectivityChecking.checkConnection(EstimatesMap.this)
                            ? ONLINE_DISTANCE_LIMIT
                            : OFFLINE_DISTANCE_LIMIT;

                    if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("33KV")) {
                        clearMapGraphics();
                        loadExistingPoles33();
                    }
                    if (WorkInvolmentsActivity.CURRENT_SURVEY_TYPE.equalsIgnoreCase("11KV")) {
                        clearMapGraphics();
                        loadExistingPoles11();
                    }


                } catch (NumberFormatException e) {
                    // Toast.makeText(EstimatesMap.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }

            dialog.dismiss();

        });
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.show();
        isAlertShown = true;


    }

    private void openAlertDialog(String msg) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        // Set Data
        title.setText("Pay Attention");
        message.setText(msg);

        // Hide extra fields if not needed
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

        // Set icon
        iv_icon.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.warning));

        // Button setup
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("OKAY");

        // Click action
        okBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );

        }

        dialog.show();
    }

    private void openAlertDialogForOtherFeeder(String msg) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        // Set Data
        title.setText("Pay Attention");
        message.setText(msg);

        // Hide extra fields if not needed
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

        // Set icon
        iv_icon.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.warning));

        // Button setup
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("OKAY");

        // Click action
        okBtn.setOnClickListener(v -> {
            EstimatesMap.IS_CURRENT_LOCATION_LINE_EXTENSION_33 = false;
            EstimatesMap.IS_CURRENT_LOCATION_LINE_EXTENSION_11 = false;
            EstimatesMap.IS_CURRENT_LOCATION_SELECTED = false;
            EstimatesMap.IS_CURRENT_LOCATION_OTHER_FEEDER = false;
            IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
            IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
            IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
            EstimatesMap.IS_SAME_EXISTING_FEEDER = false;
            EstimatesMap.POLE_SURVEY_TYPE_11KV = false;
            EstimatesMap.SELECTED_FEEDER_ID = "";
            EstimatesMap.SELECTED_LOCATION_TYPE = "";
            EstimatesMap.SELECTED_LOCATION_NUMBER = "";
            EstimatesMap.IS_CURRENT_LOCATION_INTERMEDIATE_POLE_11 = false;
            EstimatesMap.IS_CURRENT_LOCATION_INTERMEDIATE_POLE_33 = false;
            EstimatesMap.IS_CURRENT_LOCATION_INTERMEDIATE_POLE_LT = false;
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
        }

        dialog.show();
    }
//    private void openAlertDialog(String msg) {
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Pay Attention")
//                .setCancelable(false)
//                .setMessage(msg)
//                .setPositiveButton("Okay", (dialogInterface, which) -> {
//                    dialogInterface.cancel();
//                })
//
//                .show();
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        }
//    }

    private String getFieldValue(String fieldName) {
        try {
            Field field = WorkInvolmentsActivity.class.getField(fieldName);
            Object value = field.get(null);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    //NEW INTERMEDIATE POLE LOGIC
    private void checkIntermediatePoleAndProceed(
            double finalDistance,
            MapView mMapView,
            MotionEvent e,
            double latitude,
            double longitude
    ) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        // Set Data
        title.setText("Pay Attention");
        message.setText("Is It Intermediate Pole?");

        // Hide unused fields
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

        // Set icon
        iv_icon.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.question)
        );

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("No");
        cancelBtn.setText("Yes");

        // YES Click
        cancelBtn.setOnClickListener(v -> {

            EstimatesMap.IS_INTERMEDIATE_POLE = true;

            MAX_DISTANCE_METERS = 200.0;

            if (finalDistance <= MAX_DISTANCE_METERS) {

                showDialogWithSpinners11KV(
                        mMapView,
                        e,
                        latitude,
                        longitude,
                        true
                );

            } else {

                openAlertDialog(
                        "Tap exactly (or within 200 meters radius) on the required tapping location"
                );
            }

            dialog.dismiss();
        });

        // CANCEL Click
        okBtn.setOnClickListener(v -> {

            EstimatesMap.IS_INTERMEDIATE_POLE = false;

            MAX_DISTANCE_METERS = 20.0;

            if (finalDistance <= MAX_DISTANCE_METERS) {

                showDialogWithSpinners11KV(
                        mMapView,
                        e,
                        latitude,
                        longitude,
                        false
                );

            } else {

                openAlertDialog(
                        "Tap exactly (or within 20 meters radius) on the required tapping location"
                );
            }

            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int width = (int) (displayMetrics.widthPixels * 0.90);
            dialog.getWindow().setLayout(
                    width,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        dialog.show();
    }

    //NEW INTERMEDIATE POLE LOGIC
    private void checkIntermediatePoleAndProceed33kv(
            double finalDistance,
            MapView mMapView,
            MotionEvent e,
            double latitude,
            double longitude
    ) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        // Set Data
        title.setText("Pay Attention");
        message.setText("Is It Intermediate Pole?");

        // Hide unused fields
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

        // Set icon
        iv_icon.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.question)
        );

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("No");
        cancelBtn.setText("Yes");

        // YES Click
        cancelBtn.setOnClickListener(v -> {

            EstimatesMap.IS_INTERMEDIATE_POLE = true;

            MAX_DISTANCE_METERS = 200.0;

            if (finalDistance <= MAX_DISTANCE_METERS) {

                showDialogWithSpinners33KV(
                        mMapView,
                        e,
                        latitude,
                        longitude,
                        true
                );

            } else {

                openAlertDialog(
                        "Tap exactly (or within 200 meters radius) on the required tapping location"
                );
            }

            dialog.dismiss();
        });

        // CANCEL Click
        okBtn.setOnClickListener(v -> {

            EstimatesMap.IS_INTERMEDIATE_POLE = false;

            MAX_DISTANCE_METERS = 20.0;

            if (finalDistance <= MAX_DISTANCE_METERS) {

                showDialogWithSpinners33KV(
                        mMapView,
                        e,
                        latitude,
                        longitude,
                        false
                );

            } else {

                openAlertDialog(
                        "Tap exactly (or within 20 meters radius) on the required tapping location"
                );
            }

            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int width = (int) (displayMetrics.widthPixels * 0.90);
            dialog.getWindow().setLayout(
                    width,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

        }

        dialog.show();
    }


    //NEW INTERMEDIATE POLE LOGIC
    private void checkIntermediatePoleAndProceedLt(
            double finalDistance,
            MapView mMapView,
            MotionEvent e,
            double latitude,
            double longitude
    ) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        // Set Data
        title.setText("Pay Attention");
        message.setText("Is It Intermediate Pole?");

        // Hide unused fields
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

        // Set icon
        iv_icon.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.question)
        );

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("No");
        cancelBtn.setText("Yes");

        // YES Click
        cancelBtn.setOnClickListener(v -> {

            EstimatesMap.IS_INTERMEDIATE_POLE = true;

            MAX_DISTANCE_METERS = 200.0;

            if (finalDistance <= MAX_DISTANCE_METERS) {

                showDialogWithSpinnersLT(
                        mMapView,
                        e,
                        latitude,
                        longitude,
                        true
                );

            } else {

                openAlertDialog(
                        "Tap exactly (or within 200 meters radius) on the required tapping location"
                );
            }

            dialog.dismiss();
        });

        // CANCEL Click
        okBtn.setOnClickListener(v -> {

            EstimatesMap.IS_INTERMEDIATE_POLE = false;

            MAX_DISTANCE_METERS = 20.0;

            if (finalDistance <= MAX_DISTANCE_METERS) {

                showDialogWithSpinnersLT(
                        mMapView,
                        e,
                        latitude,
                        longitude,
                        false
                );

            } else {

                openAlertDialog(
                        "Tap exactly (or within 20 meters radius) on the required tapping location"
                );
            }

            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        dialog.show();
    }
}
