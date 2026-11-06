package com.apepdcl.gis_survey.estimates;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.models.FdrParticularsModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.Edit33Home;
import com.apepdcl.gis_survey.estimates.sou.EditModel;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.estimates.sou.TransmitSurvey;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkInvolmentsActivity extends AppCompatActivity implements LtSurveyAdapter.OnItemClickListener, OnlyLtSurveyAdapter.OnlyOnItemClickListener {
    private Button btn_33kv_survey, btn_11kv_survey, btn_lt_survey, btn_survey, btn_meter_survey;
    private TextView btn_edit_work_involments, btn_transmit_survey;
    private boolean is33KVSelected = false;
    private boolean is11KVSelected = false;
    private boolean isDTRSelected = false;
    private boolean isLTSelected = false;

    public static boolean IS_LT_SELECTED_NEW = false;
    private boolean isMeters = false;
    public static int TOWER_LENGTH;
    String EST_NO = "", TYPE, GIS_ID, EST_NATURE, PURPOSE, max_length, REG_COUNT, wo_no, EST_TYPE = "", RDSS_TYPE;
    public static String FEEDERS = "";
    private boolean is33KVSelectedSurvey = false;
    private boolean is11KVSelectedSurvey = false;
    private boolean isLTSelectedSurvey = false;
    private boolean is33 = false;
    private boolean is11 = false;
    ArcGISDatabaseEstimates databaseEstimates;
    private double currentLat, currentLog;
    ImageView iv_back;
    boolean isCompltedMetersSurvey = false;
    public static List<String> htServiceListModels;
    TextView tv_no_of_surved_poles_33kv, tv_no_of_surved_poles_11kv, tv_no_of_surved_poles_lt;

    private PurposeModel tempModel;
    private PurposeModel model;
    ArrayList<PurposeModel> list;
    String from = "";
    private ProgressDialog progressDialog;
    private JsonArray jsonArray;
    RetrofitServiceEstimates retrofitService;
    LinearLayout  layout_main_body_work;
    RelativeLayout loader_layout_work;
    ScrollView main_layout_involvements;
    private TextView tv_progress_bar;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    //public static ArrayList<NewModel> newModels;

    //NEW DESIGN
    LinearLayout count_33, count_11, count_lt;
    TextView poles_33kv, poles_11kv, poles_lt;
    private String feeder33kVSurveyType, feeder11kVSurveyType;


    public static String Feeder_type33KV;
    public static String Feeder_type11KV;
    public static String maxFdrCount11KV;
    public static String feeder1Name11kv;
    public static String feeder2Name11kv;
    String ESTIMATION_NUMBER;

    public static ArrayList<NewModel> existingPolesList;
    public static ArrayList<NewModel> existingPolesListTotalSection;
    TextView est_no, purpose, gis_id, home_heading, work_order, est_nature;

    LinearLayout main_layout_33;
    TextView no_locations_33, ss_name_33, feeder_name_33, survey_type_33, continue_survey_33;

    LinearLayout main_layout_11;
    LinearLayout main_layout_11_1;
    TextView no_locations_11_1, ss_name_11_1, feeder_name_11_1, survey_type_11_1, continue_survey_11_1;

    LinearLayout main_layout_11_2;
    TextView no_locations_11_2, ss_name_11_2, feeder_name_11_2, survey_type_11_2, continue_survey_11_2;

    LinearLayout main_layout_11_3;
    TextView no_locations_11_3, ss_name_11_3, feeder_name_11_3, survey_type_11_3, continue_survey_11_3;

    LinearLayout main_layout_11_4;
    TextView no_locations_11_4, ss_name_11_4, feeder_name_11_4, survey_type_11_4, continue_survey_11_4;

    LinearLayout main_layout_11_5;
    TextView no_locations_11_5, ss_name_11_5, feeder_name_11_5, survey_type_11_5, continue_survey_11_5;

    // Feeder 6
    LinearLayout main_layout_11_6;
    TextView no_locations_11_6, ss_name_11_6, feeder_name_11_6, survey_type_11_6, continue_survey_11_6, edit_survey_11_6;

    // Feeder 7
    LinearLayout main_layout_11_7;
    TextView no_locations_11_7, ss_name_11_7, feeder_name_11_7, survey_type_11_7, continue_survey_11_7, edit_survey_11_7;

    // Feeder 8
    LinearLayout main_layout_11_8;
    TextView no_locations_11_8, ss_name_11_8, feeder_name_11_8, survey_type_11_8, continue_survey_11_8, edit_survey_11_8;

    // Feeder 9
    LinearLayout main_layout_11_9;
    TextView no_locations_11_9, ss_name_11_9, feeder_name_11_9, survey_type_11_9, continue_survey_11_9, edit_survey_11_9;

    // Feeder 10
    LinearLayout main_layout_11_10;
    TextView no_locations_11_10, ss_name_11_10, feeder_name_11_10, survey_type_11_10, continue_survey_11_10, edit_survey_11_10;

    LinearLayout main_layout_lt;
    TextView no_locations_lt, continue_survey_lt;
    LinearLayout  layout_gis_id_details_wo_rdss;
    MaterialCardView layout_est_details;

    public static ArrayList<NewModel> existingDtrDetails;
    TextView gis_id_wo_rdss, feeders_wo_rdss, tv_scheme_type, description_wo_rdss;

    //LINE EXTENSION
    public static boolean IS_LINE_EXTENSION_33 = false;
    public static boolean IS_LINE_EXTENSION_11_1 = false;
    public static boolean IS_LINE_EXTENSION_11_2 = false;
    public static boolean IS_LINE_EXTENSION_11_3 = false;
    public static boolean IS_LINE_EXTENSION_11_4 = false;
    public static boolean IS_LINE_EXTENSION_11_5 = false;


    public static boolean IS_LINE_EXTENSION_11_6 = false;
    public static boolean IS_LINE_EXTENSION_11_7 = false;
    public static boolean IS_LINE_EXTENSION_11_8 = false;
    public static boolean IS_LINE_EXTENSION_11_9 = false;
    public static boolean IS_LINE_EXTENSION_11_10 = false;

    public static String FEEDER_ID_33;
    public static String FEEDER_NAME_33;
    public static String feeder_name1_11kv;
    public static String feeder_name2_11kv;
    public static String feeder_name3_11kv;
    public static String feeder_name4_11kv;
    public static String feeder_name5_11kv;

    public static String feeder_name6_11kv;
    public static String feeder_name7_11kv;
    public static String feeder_name8_11kv;
    public static String feeder_name9_11kv;
    public static String feeder_name10_11kv;

    public static String feeder_id1_11kv;
    public static String feeder_id2_11kv;
    public static String feeder_id3_11kv;
    public static String feeder_id4_11kv;
    public static String feeder_id5_11kv;

    public static String feeder_id6_11kv;
    public static String feeder_id7_11kv;
    public static String feeder_id8_11kv;
    public static String feeder_id9_11kv;
    public static String feeder_id10_11kv;


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

    public static boolean FEEDER_1_SELECTED = false;
    public static boolean FEEDER_2_SELECTED = false;
    public static boolean FEEDER_3_SELECTED = false;
    public static boolean FEEDER_4_SELECTED = false;
    public static boolean FEEDER_5_SELECTED = false;


    public static boolean FEEDER_6_SELECTED = false;
    public static boolean FEEDER_7_SELECTED = false;
    public static boolean FEEDER_8_SELECTED = false;
    public static boolean FEEDER_9_SELECTED = false;
    public static boolean FEEDER_10_SELECTED = false;

    public static String CLICKED_FEEDER = "";
    public static String CLICKED_FEEDER_ID = "";

    public static boolean FEEDER_1_SURVEY_COMPLETED = false;
    public static boolean FEEDER_2_SURVEY_COMPLETED = false;
    public static boolean FEEDER_3_SURVEY_COMPLETED = false;
    public static boolean FEEDER_4_SURVEY_COMPLETED = false;
    public static boolean FEEDER_5_SURVEY_COMPLETED = false;


    public static boolean FEEDER_6_SURVEY_COMPLETED = false;
    public static boolean FEEDER_7_SURVEY_COMPLETED = false;
    public static boolean FEEDER_8_SURVEY_COMPLETED = false;
    public static boolean FEEDER_9_SURVEY_COMPLETED = false;
    public static boolean FEEDER_10_SURVEY_COMPLETED = false;

    public static boolean IS_33_KV_SELECTED = false;
    public static boolean IS_LT_SELECTED = false;

    public static String FEEDER_TYPE_11KV_1 = "";
    public static String FEEDER_TYPE_11KV_2 = "";
    public static String FEEDER_TYPE_11KV_3 = "";
    public static String FEEDER_TYPE_11KV_4 = "";
    public static String FEEDER_TYPE_11KV_5 = "";

    public static String FEEDER_TYPE_11KV_6 = "";
    public static String FEEDER_TYPE_11KV_7 = "";
    public static String FEEDER_TYPE_11KV_8 = "";
    public static String FEEDER_TYPE_11KV_9 = "";
    public static String FEEDER_TYPE_11KV_10 = "";

    public static String FEEDER_TYPE_33KV_1 = "";
    public static String FEEDER_TYPE_33KV_2 = "";
    public static String FEEDER_TYPE_33KV_3 = "";
    public static String FEEDER_TYPE_33KV_4 = "";
    public static String FEEDER_TYPE_33KV_5 = "";


    ArrayList<NewModel> totalPoles33;
    ArrayList<NewModel> totalPoles11;
    ArrayList<NewModel> totalPolesLT;

    public static String CURRENT_SURVEY_TYPE = "";
    public static PurposeModel offlineModel;

    //EDIT LOCATIONS NEW
    TextView edit_survey_33, edit_survey_11_1, edit_survey_11_2, edit_survey_11_3, edit_survey_11_4, edit_survey_11_5, edit_survey_lt;

    //NEW CODE FOR 33KV LINE EXTENSION START

    LinearLayout main_layout_33_1;
    TextView no_locations_33_1, ss_name_33_1, feeder_name_33_1, survey_type_33_1, continue_survey_33_1;

    LinearLayout main_layout_33_2;
    TextView no_locations_33_2, ss_name_33_2, feeder_name_33_2, survey_type_33_2, continue_survey_33_2;

    LinearLayout main_layout_33_3;
    TextView no_locations_33_3, ss_name_33_3, feeder_name_33_3, survey_type_33_3, continue_survey_33_3;

    LinearLayout main_layout_33_4;
    TextView no_locations_33_4, ss_name_33_4, feeder_name_33_4, survey_type_33_4, continue_survey_33_4;

    LinearLayout main_layout_33_5;
    TextView no_locations_33_5, ss_name_33_5, feeder_name_33_5, survey_type_33_5, continue_survey_33_5;

    TextView edit_survey_33_1, edit_survey_33_2, edit_survey_33_3, edit_survey_33_4, edit_survey_33_5;

    public static String feeder_name1_33kv;
    public static String feeder_name2_33kv;
    public static String feeder_name3_33kv;
    public static String feeder_name4_33kv;
    public static String feeder_name5_33kv;

    public static String feeder_id1_33kv;
    public static String feeder_id2_33kv;
    public static String feeder_id3_33kv;
    public static String feeder_id4_33kv;
    public static String feeder_id5_33kv;

    public static boolean IS_LINE_EXTENSION_33_1 = false;
    public static boolean IS_LINE_EXTENSION_33_2 = false;
    public static boolean IS_LINE_EXTENSION_33_3 = false;
    public static boolean IS_LINE_EXTENSION_33_4 = false;
    public static boolean IS_LINE_EXTENSION_33_5 = false;

    public static boolean FEEDER_1_SELECTED_33 = false;
    public static boolean FEEDER_2_SELECTED_33 = false;
    public static boolean FEEDER_3_SELECTED_33 = false;
    public static boolean FEEDER_4_SELECTED_33 = false;
    public static boolean FEEDER_5_SELECTED_33 = false;

    public static boolean FEEDER_1_SURVEY_COMPLETED_33 = false;
    public static boolean FEEDER_2_SURVEY_COMPLETED_33 = false;
    public static boolean FEEDER_3_SURVEY_COMPLETED_33 = false;
    public static boolean FEEDER_4_SURVEY_COMPLETED_33 = false;
    public static boolean FEEDER_5_SURVEY_COMPLETED_33 = false;

    public int LOCATION_COUNT_33KV_FEEDER_1 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_2 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_3 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_4 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_5 = 0;

    //NEW DTR CHANGES BY CHANDU
//---------------------START------------------
    public static double SELECTED_SS_LAT_33KV_1;
    public static double SELECTED_SS_LAT_33KV_2;
    public static double SELECTED_SS_LAT_33KV_3;
    public static double SELECTED_SS_LAT_33KV_4;
    public static double SELECTED_SS_LAT_33KV_5;

    public static double SELECTED_SS_LNG_33KV_1;
    public static double SELECTED_SS_LNG_33KV_2;
    public static double SELECTED_SS_LNG_33KV_3;
    public static double SELECTED_SS_LNG_33KV_4;
    public static double SELECTED_SS_LNG_33KV_5;

    public static double SELECTED_SS_LAT_11KV_1;
    public static double SELECTED_SS_LAT_11KV_2;
    public static double SELECTED_SS_LAT_11KV_3;
    public static double SELECTED_SS_LAT_11KV_4;
    public static double SELECTED_SS_LAT_11KV_5;

    public static double SELECTED_SS_LAT_11KV_6;
    public static double SELECTED_SS_LAT_11KV_7;
    public static double SELECTED_SS_LAT_11KV_8;
    public static double SELECTED_SS_LAT_11KV_9;
    public static double SELECTED_SS_LAT_11KV_10;

    public static double SELECTED_SS_LNG_11KV_1;
    public static double SELECTED_SS_LNG_11KV_2;
    public static double SELECTED_SS_LNG_11KV_3;
    public static double SELECTED_SS_LNG_11KV_4;
    public static double SELECTED_SS_LNG_11KV_5;


    public static double SELECTED_SS_LNG_11KV_6;
    public static double SELECTED_SS_LNG_11KV_7;
    public static double SELECTED_SS_LNG_11KV_8;
    public static double SELECTED_SS_LNG_11KV_9;
    public static double SELECTED_SS_LNG_11KV_10;
    //---------------------END------------------

    public static String WO_OLD = "";

    //NEW LT CHANGES
    TextView process_lt_survey, no_dtr_lt, tv_no_dtr;
    public static String SELECTED_DTR;
    public static String SELECTED_DTR_LOCATION;
    public static String SELECTED_LT_CKT;
    public static boolean IS_LT_FIRST_POLE = false;
    public static boolean IS_LT_EXISTING_SURVEY = false;
    public static boolean LT_POLE_SURVEY_TYPE = false;
    //NEW CHANGES BY CHANDU v3.7
    public static boolean LT_FIRST_POLE_EXISTING = false;

    public static boolean ONLY_LT_WITH_PROPOSED = false;

    public static String SELECTED_DTR_ID = "";
    TextView tv_dtr_phase, tv_dtr_capacity, tv_dtr_location_name, tv_action_remarks;
    LinearLayout layout_dtr_details;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_involments);


        //NEW LT CHANGES
        process_lt_survey = findViewById(R.id.process_lt_survey);
        no_dtr_lt = findViewById(R.id.no_dtr_lt);
        tv_no_dtr = findViewById(R.id.tv_no_dtr);
        tv_dtr_phase = findViewById(R.id.tv_dtr_phase);
        tv_dtr_capacity = findViewById(R.id.tv_dtr_capacity);
        tv_dtr_location_name = findViewById(R.id.tv_dtr_location_name);
        layout_dtr_details = findViewById(R.id.layout_dtr_details);
        tv_action_remarks = findViewById(R.id.tv_action_remarks);

        layout_est_details = findViewById(R.id.layout_est_details);
        layout_gis_id_details_wo_rdss = findViewById(R.id.layout_gis_id_details_wo_rdss);

        SELECTED_DTR = "";
        SELECTED_DTR_LOCATION = "";
        SELECTED_LT_CKT = "";
        IS_LT_EXISTING_SURVEY = false;

        IS_LINE_EXTENSION_33 = false;
        IS_LINE_EXTENSION_11_1 = false;
        IS_LINE_EXTENSION_11_2 = false;
        IS_LINE_EXTENSION_11_3 = false;
        IS_LINE_EXTENSION_11_4 = false;
        IS_LINE_EXTENSION_11_5 = false;

        IS_LINE_EXTENSION_11_6 = false;
        IS_LINE_EXTENSION_11_7 = false;
        IS_LINE_EXTENSION_11_8 = false;
        IS_LINE_EXTENSION_11_9 = false;
        IS_LINE_EXTENSION_11_10 = false;
        CLICKED_FEEDER = "";
        CURRENT_SURVEY_TYPE = "";
        IS_33_KV_SELECTED = false;
        IS_LT_SELECTED = false;
        IS_LT_FIRST_POLE = false;
        LT_FIRST_POLE_EXISTING = false;
        databaseEstimates = new ArcGISDatabaseEstimates(this);
        existingPolesList = new ArrayList<>();
        offlineModel = new PurposeModel();
        htServiceListModels = new ArrayList<>();
        existingPolesListTotalSection = new ArrayList<>();
        existingDtrDetails = new ArrayList<>();

        //NEW CODE FOR 33KV LINE EXTENSION START
        IS_LINE_EXTENSION_33_1 = false;
        IS_LINE_EXTENSION_33_2 = false;
        IS_LINE_EXTENSION_33_3 = false;
        IS_LINE_EXTENSION_33_4 = false;
        IS_LINE_EXTENSION_33_5 = false;
        FEEDER_1_SELECTED_33 = false;
        FEEDER_2_SELECTED_33 = false;
        FEEDER_3_SELECTED_33 = false;
        FEEDER_4_SELECTED_33 = false;
        FEEDER_5_SELECTED_33 = false;

        //NEW CODE FOR 11KV
        FEEDER_1_SELECTED = false;
        FEEDER_2_SELECTED = false;
        FEEDER_3_SELECTED = false;
        FEEDER_4_SELECTED = false;
        FEEDER_5_SELECTED = false;
        FEEDER_6_SELECTED = false;
        FEEDER_7_SELECTED = false;
        FEEDER_8_SELECTED = false;
        FEEDER_9_SELECTED = false;
        FEEDER_10_SELECTED = false;

        edit_survey_33_1 = findViewById(R.id.edit_survey_33_1);
        edit_survey_33_2 = findViewById(R.id.edit_survey_33_2);
        edit_survey_33_3 = findViewById(R.id.edit_survey_33_3);
        edit_survey_33_4 = findViewById(R.id.edit_survey_33_4);
        edit_survey_33_5 = findViewById(R.id.edit_survey_33_5);

        main_layout_33_1 = findViewById(R.id.main_layout_33_1);
        no_locations_33_1 = findViewById(R.id.no_locations_33_1);
        ss_name_33_1 = findViewById(R.id.ss_name_33_1);
        feeder_name_33_1 = findViewById(R.id.feeder_name_33_1);
        survey_type_33_1 = findViewById(R.id.survey_type_33_1);
        continue_survey_33_1 = findViewById(R.id.continue_survey_33_1);

        main_layout_33_2 = findViewById(R.id.main_layout_33_2);
        no_locations_33_2 = findViewById(R.id.no_locations_33_2);
        ss_name_33_2 = findViewById(R.id.ss_name_33_2);
        feeder_name_33_2 = findViewById(R.id.feeder_name_33_2);
        survey_type_33_2 = findViewById(R.id.survey_type_33_2);
        continue_survey_33_2 = findViewById(R.id.continue_survey_33_2);

        main_layout_33_3 = findViewById(R.id.main_layout_33_3);
        no_locations_33_3 = findViewById(R.id.no_locations_33_3);
        ss_name_33_3 = findViewById(R.id.ss_name_33_3);
        feeder_name_33_3 = findViewById(R.id.feeder_name_33_3);
        survey_type_33_3 = findViewById(R.id.survey_type_33_3);
        continue_survey_33_3 = findViewById(R.id.continue_survey_33_3);

        main_layout_33_4 = findViewById(R.id.main_layout_33_4);
        no_locations_33_4 = findViewById(R.id.no_locations_33_4);
        ss_name_33_4 = findViewById(R.id.ss_name_33_4);
        feeder_name_33_4 = findViewById(R.id.feeder_name_33_4);
        survey_type_33_4 = findViewById(R.id.survey_type_33_4);
        continue_survey_33_4 = findViewById(R.id.continue_survey_33_4);

        main_layout_33_5 = findViewById(R.id.main_layout_33_5);
        no_locations_33_5 = findViewById(R.id.no_locations_33_5);
        ss_name_33_5 = findViewById(R.id.ss_name_33_5);
        feeder_name_33_5 = findViewById(R.id.feeder_name_33_5);
        survey_type_33_5 = findViewById(R.id.survey_type_33_5);
        continue_survey_33_5 = findViewById(R.id.continue_survey_33_5);

        //NEW CODE FOR 33KV LINE EXTENSION END


        //EDIT LOCATIONS STARTS
        edit_survey_33 = findViewById(R.id.edit_survey_33);
        edit_survey_11_1 = findViewById(R.id.edit_survey_11_1);
        edit_survey_11_2 = findViewById(R.id.edit_survey_11_2);
        edit_survey_11_3 = findViewById(R.id.edit_survey_11_3);
        edit_survey_11_4 = findViewById(R.id.edit_survey_11_4);
        edit_survey_11_5 = findViewById(R.id.edit_survey_11_5);
        edit_survey_lt = findViewById(R.id.edit_survey_lt);

        //EDIT LOCATIONS ENDS

        est_no = findViewById(R.id.est_no);
        purpose = findViewById(R.id.purpose);
        est_nature = findViewById(R.id.est_nature);
        gis_id = findViewById(R.id.gis_id);
        home_heading = findViewById(R.id.home_heading);
        work_order = findViewById(R.id.tv_work_order_number);

        main_layout_33 = findViewById(R.id.main_layout_33);
        no_locations_33 = findViewById(R.id.no_locations_33);
        ss_name_33 = findViewById(R.id.ss_name_33);
        feeder_name_33 = findViewById(R.id.feeder_name_33);
        survey_type_33 = findViewById(R.id.survey_type_33);
        continue_survey_33 = findViewById(R.id.continue_survey_33);

        main_layout_11 = findViewById(R.id.main_layout_11);

        main_layout_11_1 = findViewById(R.id.main_layout_11_1);
        no_locations_11_1 = findViewById(R.id.no_locations_11_1);
        ss_name_11_1 = findViewById(R.id.ss_name_11_1);
        feeder_name_11_1 = findViewById(R.id.feeder_name_11_1);
        survey_type_11_1 = findViewById(R.id.survey_type_11_1);
        continue_survey_11_1 = findViewById(R.id.continue_survey_11_1);

        main_layout_11_2 = findViewById(R.id.main_layout_11_2);
        no_locations_11_2 = findViewById(R.id.no_locations_11_2);
        ss_name_11_2 = findViewById(R.id.ss_name_11_2);
        feeder_name_11_2 = findViewById(R.id.feeder_name_11_2);
        survey_type_11_2 = findViewById(R.id.survey_type_11_2);
        continue_survey_11_2 = findViewById(R.id.continue_survey_11_2);

        main_layout_11_3 = findViewById(R.id.main_layout_11_3);
        no_locations_11_3 = findViewById(R.id.no_locations_11_3);
        ss_name_11_3 = findViewById(R.id.ss_name_11_3);
        feeder_name_11_3 = findViewById(R.id.feeder_name_11_3);
        survey_type_11_3 = findViewById(R.id.survey_type_11_3);
        continue_survey_11_3 = findViewById(R.id.continue_survey_11_3);

        main_layout_11_4 = findViewById(R.id.main_layout_11_4);
        no_locations_11_4 = findViewById(R.id.no_locations_11_4);
        ss_name_11_4 = findViewById(R.id.ss_name_11_4);
        feeder_name_11_4 = findViewById(R.id.feeder_name_11_4);
        survey_type_11_4 = findViewById(R.id.survey_type_11_4);
        continue_survey_11_4 = findViewById(R.id.continue_survey_11_4);

        main_layout_11_5 = findViewById(R.id.main_layout_11_5);
        no_locations_11_5 = findViewById(R.id.no_locations_11_5);
        ss_name_11_5 = findViewById(R.id.ss_name_11_5);
        feeder_name_11_5 = findViewById(R.id.feeder_name_11_5);
        survey_type_11_5 = findViewById(R.id.survey_type_11_5);
        continue_survey_11_5 = findViewById(R.id.continue_survey_11_5);

        // Feeder 6
        main_layout_11_6 = findViewById(R.id.main_layout_11_6);
        no_locations_11_6 = findViewById(R.id.no_locations_11_6);
        ss_name_11_6 = findViewById(R.id.ss_name_11_6);
        feeder_name_11_6 = findViewById(R.id.feeder_name_11_6);
        survey_type_11_6 = findViewById(R.id.survey_type_11_6);
        continue_survey_11_6 = findViewById(R.id.continue_survey_11_6);

// Feeder 7
        main_layout_11_7 = findViewById(R.id.main_layout_11_7);
        no_locations_11_7 = findViewById(R.id.no_locations_11_7);
        ss_name_11_7 = findViewById(R.id.ss_name_11_7);
        feeder_name_11_7 = findViewById(R.id.feeder_name_11_7);
        survey_type_11_7 = findViewById(R.id.survey_type_11_7);
        continue_survey_11_7 = findViewById(R.id.continue_survey_11_7);

// Feeder 8
        main_layout_11_8 = findViewById(R.id.main_layout_11_8);
        no_locations_11_8 = findViewById(R.id.no_locations_11_8);
        ss_name_11_8 = findViewById(R.id.ss_name_11_8);
        feeder_name_11_8 = findViewById(R.id.feeder_name_11_8);
        survey_type_11_8 = findViewById(R.id.survey_type_11_8);
        continue_survey_11_8 = findViewById(R.id.continue_survey_11_8);

// Feeder 9
        main_layout_11_9 = findViewById(R.id.main_layout_11_9);
        no_locations_11_9 = findViewById(R.id.no_locations_11_9);
        ss_name_11_9 = findViewById(R.id.ss_name_11_9);
        feeder_name_11_9 = findViewById(R.id.feeder_name_11_9);
        survey_type_11_9 = findViewById(R.id.survey_type_11_9);
        continue_survey_11_9 = findViewById(R.id.continue_survey_11_9);

// Feeder 10
        main_layout_11_10 = findViewById(R.id.main_layout_11_10);
        no_locations_11_10 = findViewById(R.id.no_locations_11_10);
        ss_name_11_10 = findViewById(R.id.ss_name_11_10);
        feeder_name_11_10 = findViewById(R.id.feeder_name_11_10);
        survey_type_11_10 = findViewById(R.id.survey_type_11_10);
        continue_survey_11_10 = findViewById(R.id.continue_survey_11_10);


        // Optional: Edit survey buttons for feeders 6-10
        edit_survey_11_6 = findViewById(R.id.edit_survey_11_6);
        edit_survey_11_7 = findViewById(R.id.edit_survey_11_7);
        edit_survey_11_8 = findViewById(R.id.edit_survey_11_8);
        edit_survey_11_9 = findViewById(R.id.edit_survey_11_9);
        edit_survey_11_10 = findViewById(R.id.edit_survey_11_10);

        main_layout_lt = findViewById(R.id.main_layout_lt);
        no_locations_lt = findViewById(R.id.no_locations_lt);
        continue_survey_lt = findViewById(R.id.continue_survey_lt);
        gis_id_wo_rdss = findViewById(R.id.gis_id_wo_rdss);
        feeders_wo_rdss = findViewById(R.id.feeders_wo_rdss);
        tv_scheme_type = findViewById(R.id.tv_scheme_type);
        description_wo_rdss = findViewById(R.id.description_wo_rdss);


        //poles_33kv = findViewById(R.id.poles_33kv);
        // poles_11kv = findViewById(R.id.poles_11kv);
        //  poles_lt = findViewById(R.id.poles_lt);

        btn_33kv_survey = findViewById(R.id.btn_33kv_survey);
        btn_11kv_survey = findViewById(R.id.btn_11kv_survey_1);
        btn_lt_survey = findViewById(R.id.btn_lt_survey);
        btn_meter_survey = findViewById(R.id.btn_meter_survey);
        loader_layout_work = findViewById(R.id.loader_layout_work);
        layout_main_body_work = findViewById(R.id.layout_main_body_work);
        main_layout_involvements = findViewById(R.id.main_layout_involvements);
        btn_survey = findViewById(R.id.btn_survey);
        tv_no_of_surved_poles_33kv = findViewById(R.id.tv_no_of_surved_poles_33kv);
        tv_no_of_surved_poles_11kv = findViewById(R.id.tv_no_of_surved_poles_11kv);
        btn_edit_work_involments = findViewById(R.id.btn_edit_work_involments);
        btn_transmit_survey = findViewById(R.id.btn_transmit_survey);
        iv_back = findViewById(R.id.back_layout);
        // close_layout = findViewById(R.id.close_layout);
        tv_progress_bar = findViewById(R.id.tv_progress_bar_work);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        progressDialog = new ProgressDialog(WorkInvolmentsActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        //newModels = new ArrayList<>();
        list = new ArrayList<>();
        iv_back.setOnClickListener(v -> onBackPressed());
//        close_layout.setOnClickListener(v -> onBackPressed());
        model = new PurposeModel();

        EST_NO = getIntent().getStringExtra("EST_NO");
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        EST_NATURE = getIntent().getStringExtra("EST_NATURE");
        PURPOSE = getIntent().getStringExtra("PURPOSE");
        wo_no = getIntent().getStringExtra("WORK_ORDER");
        max_length = getIntent().getStringExtra("MAX_LENGTH");
        REG_COUNT = getIntent().getStringExtra("REG_COUNT");
        maxFdrCount11KV = getIntent().getStringExtra("LASTFDRINDEX");
        WO_OLD = getIntent().getStringExtra("WO_OLD");

        //model = (PurposeModel) getIntent().getSerializableExtra("Model");
        ArrayList<NewModel> polesDataAll = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);


        purpose.setText(PURPOSE);
        est_nature.setText("" + EST_NATURE);
        gis_id.setText("" + GIS_ID);
        work_order.setText("" + wo_no);

        if ("ESTIMATES".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            work_order.setVisibility(View.GONE);
            est_no.setText("" + EST_NO);
        } else if ("SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            work_order.setVisibility(View.GONE);
            est_no.setVisibility(View.VISIBLE);
            est_no.setText("" + EST_NO);
            est_nature.setVisibility(View.GONE);
            layout_dtr_details.setVisibility(View.VISIBLE);
            tv_dtr_phase.setText(EstimatesHome.DTR_PHASE);
            tv_dtr_capacity.setText(EstimatesHome.DTR_CAPACITY);
            tv_dtr_location_name.setText(EstimatesHome.DTR_NAME);
            tv_action_remarks.setText(EstimatesHome.ACTION_REMARKS);
            tv_action_remarks.setVisibility(View.VISIBLE);
        } else {
            work_order.setVisibility(View.VISIBLE);
            if (!EST_NO.isEmpty()) {
                est_no.setText("" + EST_NO);
            } else {
                est_no.setVisibility(View.GONE);
            }
        }


        is33KVSelectedSurvey = databaseEstimates.get33kvSurveyStatusNEW(EST_NO, GIS_ID);
        is11KVSelectedSurvey = databaseEstimates.get11kvSurveyStatusNEW(EST_NO, GIS_ID);
        isLTSelectedSurvey = databaseEstimates.getLTSurveyStatusNEW(EST_NO, GIS_ID);


        //NEW BY SOUBHAGYA START
        totalPoles33 = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
        totalPoles11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);
        totalPolesLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);


        //getSelectedEstimateData();

        //NEW API CODE

        if (ConnectivityChecking.checkConnection(WorkInvolmentsActivity.this)) {
            singleEstimateWithFdrs();
        } else {
            CommanUtilits.internetDialog(WorkInvolmentsActivity.this);
        }
        //singleEstimateWithFdrs();


        //NEW BY SOUBHAGYA END


        is33KVSelected = getIntent().getBooleanExtra("33KV", false);
        is11KVSelected = getIntent().getBooleanExtra("11KV", false);
        isDTRSelected = getIntent().getBooleanExtra("DTR", false);
        isLTSelected = getIntent().getBooleanExtra("LT", false);
        IS_LT_SELECTED_NEW = getIntent().getBooleanExtra("LT", false);
        isMeters = getIntent().getBooleanExtra("METERS", false);
        from = getIntent().getStringExtra("FROM");

        if (from != null) {
            if (from.equalsIgnoreCase("ESTIMATE_HOME")) {
                is33KVSelected = getIntent().getBooleanExtra("is33kvSelected", false);
                is11KVSelected = getIntent().getBooleanExtra("is11kvSelected", false);
                isLTSelected = getIntent().getBooleanExtra("isLtSelected", false);
                IS_LT_SELECTED_NEW = getIntent().getBooleanExtra("isLtSelected", false);
                isMeters = getIntent().getBooleanExtra("isMeterSelected", false);
                REG_COUNT = getIntent().getStringExtra("REG_COUNT");
                Feeder_type33KV = getIntent().getStringExtra("Feeder_type33KV");
                Feeder_type11KV = getIntent().getStringExtra("Feeder_type11KV");
                maxFdrCount11KV = getIntent().getStringExtra("LASTFDRINDEX");
                feeder33kVSurveyType = EstimatesHome.Feeder_type33KV;
                feeder11kVSurveyType = EstimatesHome.Feeder_type11KV;
                EST_TYPE = getIntent().getStringExtra("EST_TYPE");



            }
            if (from.equalsIgnoreCase("PREPARE_ESTIMATES")) {
                is33KVSelected = getIntent().getBooleanExtra("33KV", false);
                is11KVSelected = getIntent().getBooleanExtra("11KV", false);
                isLTSelected = getIntent().getBooleanExtra("LT", false);
                IS_LT_SELECTED_NEW = getIntent().getBooleanExtra("LT", false);
                isDTRSelected = getIntent().getBooleanExtra("DTR", false);
                isMeters = getIntent().getBooleanExtra("METERS", false);
                // model = (PurposeModel) getIntent().getSerializableExtra("Model");
                feeder33kVSurveyType = getIntent().getStringExtra("Feeder_type33KV");
                feeder11kVSurveyType = getIntent().getStringExtra("Feeder_type11KV");
                Feeder_type33KV = getIntent().getStringExtra("Feeder_type33KV");
                Feeder_type11KV = getIntent().getStringExtra("Feeder_type11KV");
                wo_no = getIntent().getStringExtra("WORK_ORDER");
                EST_TYPE = getIntent().getStringExtra("EST_TYPE");


            }
            if (from.equalsIgnoreCase("ESTIMATE_MAP")) {
                is33KVSelected = getIntent().getBooleanExtra("33KV", false);
                is11KVSelected = getIntent().getBooleanExtra("11KV", false);
                isLTSelected = getIntent().getBooleanExtra("LT", false);
                IS_LT_SELECTED_NEW = getIntent().getBooleanExtra("LT", false);
                isDTRSelected = getIntent().getBooleanExtra("DTR", false);
                isMeters = getIntent().getBooleanExtra("METERS", false);
                feeder33kVSurveyType = EstimatesMap.Feeder_type33KV;
                feeder11kVSurveyType = EstimatesMap.Feeder_type11KV;
            }

            if (from.equalsIgnoreCase("11KVComplete")) {
                maxFdrCount11KV = getIntent().getStringExtra("LASTFDRINDEX");
                wo_no = (getIntent().getStringExtra("WORK_ORDER"));
                IS_LT_SELECTED_NEW = getIntent().getBooleanExtra("LT", false);

                //  model = (PurposeModel) getIntent().getSerializableExtra("Model");


            }


        }


        btn_transmit_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<NewModel> allData = databaseEstimates.getCurrentSurveyData();
                ArrayList<EditModel> onlyLatLongData = databaseEstimates.getEditLocationsData();
                if (allData.isEmpty() && onlyLatLongData.isEmpty()) {
                    Toast.makeText(WorkInvolmentsActivity.this, "You don't have any Surveyed data to transmit", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentPf = new Intent(WorkInvolmentsActivity.this, TransmitSurvey.class);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intentPf.putExtra("FEEDERS", FEEDERS);
                    }
                    startActivity(intentPf);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                }

            }
        });


        btn_edit_work_involments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   getPurposeData();

                if (ConnectivityChecking.checkConnection(WorkInvolmentsActivity.this)) {
                    getSingleEstimateWithFdrs();
                } else {
                    CommanUtilits.internetDialog(WorkInvolmentsActivity.this);
                }


            }
        });

        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            layout_gis_id_details_wo_rdss.setVisibility(View.VISIBLE);
            layout_est_details.setVisibility(View.GONE);
            FEEDERS = getIntent().getStringExtra("FEEDERS");
            RDSS_TYPE = getIntent().getStringExtra("RDSS_TYPE");
            feeders_wo_rdss.setText(FEEDERS);
            gis_id_wo_rdss.setText(GIS_ID);
            if (RDSS_TYPE.equalsIgnoreCase("BIF")) {
                tv_scheme_type.setText("BIFURCATION");
            } else if (RDSS_TYPE.equalsIgnoreCase("AGL")) {
                tv_scheme_type.setText("SEGREGATION");
            } else if (RDSS_TYPE.equalsIgnoreCase("AGL_BIF")) {
                tv_scheme_type.setText("BIFURCATION, SEGREGATION");

            }
            description_wo_rdss.setText(PURPOSE);




        } else {
            layout_gis_id_details_wo_rdss.setVisibility(View.GONE);
            layout_est_details.setVisibility(View.VISIBLE);
        }

        //03-01-2025 Chandu Changes
        continue_survey_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is33KVSelectedSurvey) {
                    showAlertDialog(is33KVSelected, is11KVSelected, isLTSelected, 1, "");
                    Toast.makeText(WorkInvolmentsActivity.this, "Survey Completed", Toast.LENGTH_SHORT).show();
                } else {
                    IS_33_KV_SELECTED = true;
                    CURRENT_SURVEY_TYPE = "33KV";
                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                        if (!feeder_name_33.getText().toString().isEmpty()) {

                            //htServiceListFor33KV();
                        }
                    } else {
                        navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 1, "");

                    }

                }
            }
        });

        continue_survey_11_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is33KVSelected) {
                    /*if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    }*/
                    if (FEEDER_1_SELECTED_33 && !FEEDER_1_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 1 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (FEEDER_2_SELECTED_33 && !FEEDER_2_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 2 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (FEEDER_3_SELECTED_33 && !FEEDER_3_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 3 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (FEEDER_4_SELECTED_33 && !FEEDER_4_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 4 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (FEEDER_5_SELECTED_33 && !FEEDER_5_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 5 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        CLICKED_FEEDER = "FEEDER 1";
                        CLICKED_FEEDER_ID = feeder_id1_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_1.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id1_11kv, 1, "Continue", feeder_name1_11kv);
                            } else if (survey_type_11_1.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_1.getText().toString().trim().split("~")[1], 1, "Continue", feeder_name1_11kv);
                            }
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 1, feeder_id1_11kv);

                        }
                    }
                } else {
                    CLICKED_FEEDER = "FEEDER 1";
                    CLICKED_FEEDER_ID = feeder_id1_11kv;
                    CURRENT_SURVEY_TYPE = "11KV";

                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                        if (survey_type_11_1.getText().toString().equalsIgnoreCase("Proposed")) {
                            htServiceListFor11KV(feeder_id1_11kv, 1, "Continue", feeder_name1_11kv);
                        } else if (survey_type_11_1.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                            htServiceListFor11KV(feeder_name_11_1.getText().toString().trim().split("~")[1], 1, "Continue", feeder_name1_11kv);
                        }

                    } else {
                        navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 1, feeder_id1_11kv);

                    }
                }

            }
        });
        continue_survey_11_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/ false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_1 > 0) {
                            CLICKED_FEEDER = "FEEDER 2";
                            CLICKED_FEEDER_ID = feeder_id2_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_2.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id2_11kv, 2, "Continue", feeder_name2_11kv);
                                } else if (survey_type_11_2.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_2.getText().toString().trim().split("~")[1], 2, "Continue", feeder_name2_11kv);
                                }

                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 1, feeder_id2_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 1 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_1 > 0) {
                        CLICKED_FEEDER = "FEEDER 2";
                        CLICKED_FEEDER_ID = feeder_id2_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                            if (survey_type_11_2.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id2_11kv, 2, "Continue", feeder_name2_11kv);
                            } else if (survey_type_11_2.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_2.getText().toString().trim().split("~")[1], 2, "Continue", feeder_name2_11kv);
                            }

                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 2, feeder_id2_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 1 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        continue_survey_11_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/ false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_2 > 0) {
                            CLICKED_FEEDER = "FEEDER 3";
                            CLICKED_FEEDER_ID = feeder_id3_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_3.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id3_11kv, 3, "Continue", feeder_name3_11kv);
                                } else if (survey_type_11_3.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_3.getText().toString().trim().split("~")[1], 3, "Continue", feeder_name3_11kv);
                                }

                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 3, feeder_id3_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 2 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_2 > 0) {
                        CLICKED_FEEDER = "FEEDER 3";
                        CLICKED_FEEDER_ID = feeder_id3_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_3.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id3_11kv, 3, "Continue", feeder_name3_11kv);
                            } else if (survey_type_11_3.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_3.getText().toString().trim().split("~")[1], 3, "Continue", feeder_name3_11kv);
                            }
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 3, feeder_id3_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 2 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }

                /*if (LOCATION_COUNT_11KV_FEEDER_2 > 0){
                    CLICKED_FEEDER = "FEEDER 3";
                    navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 1, feeder_id1_11kv);
                }else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 2 Survey First", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        continue_survey_11_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_3 > 0) {
                            CLICKED_FEEDER = "FEEDER 4";
                            CLICKED_FEEDER_ID = feeder_id4_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_4.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id4_11kv, 4, "Continue", feeder_name4_11kv);
                                } else if (survey_type_11_4.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_4.getText().toString().trim().split("~")[1], 4, "Continue", feeder_name4_11kv);
                                }

                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 4, feeder_id4_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 3 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_3 > 0) {
                        CLICKED_FEEDER = "FEEDER 4";
                        CLICKED_FEEDER_ID = feeder_id4_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_4.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id4_11kv, 4, "Continue", feeder_name4_11kv);
                            } else if (survey_type_11_4.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_4.getText().toString().trim().split("~")[1], 4, "Continue", feeder_name4_11kv);
                            }


                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 4, feeder_id4_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 3 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }


                /*if (LOCATION_COUNT_11KV_FEEDER_3 > 0){
                    CLICKED_FEEDER = "FEEDER 4";
                    navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 1, feeder_id1_11kv);
                }else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 3 Survey First", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        continue_survey_11_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/ false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                            CLICKED_FEEDER = "FEEDER 5";
                            CLICKED_FEEDER_ID = feeder_id5_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_5.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id5_11kv, 5, "Continue", feeder_name5_11kv);
                                } else if (survey_type_11_5.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_5.getText().toString().trim().split("~")[1], 5, "Continue", feeder_name5_11kv);
                                }


                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                        CLICKED_FEEDER = "FEEDER 5";
                        CLICKED_FEEDER_ID = feeder_id5_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_5.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id5_11kv, 5, "Continue", feeder_name5_11kv);
                            } else if (survey_type_11_5.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_5.getText().toString().trim().split("~")[1], 5, "Continue", feeder_name5_11kv);
                            }
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }

               /* if (LOCATION_COUNT_11KV_FEEDER_4 > 0){
                    CLICKED_FEEDER = "FEEDER 5";
                    navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 1, feeder_id1_11kv);
                }else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        continue_survey_11_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/ false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_5 > 0) {
                            CLICKED_FEEDER = "FEEDER 6";
                            CLICKED_FEEDER_ID = feeder_id6_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_6.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id6_11kv, 6, "Continue", feeder_name6_11kv);
                                } else if (survey_type_11_6.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_6.getText().toString().trim().split("~")[1], 6, "Continue", feeder_name6_11kv);
                                }

                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 6, feeder_id6_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 5 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_5 > 0) {
                        CLICKED_FEEDER = "FEEDER 6";
                        CLICKED_FEEDER_ID = feeder_id6_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_6.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id6_11kv, 6, "Continue", feeder_name6_11kv);
                            } else if (survey_type_11_6.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_6.getText().toString().trim().split("~")[1], 6, "Continue", feeder_name6_11kv);
                            }
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 6, feeder_id6_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 5 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        continue_survey_11_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/ false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_6 > 0) {
                            CLICKED_FEEDER = "FEEDER 7";
                            CLICKED_FEEDER_ID = feeder_id7_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_7.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id7_11kv, 7, "Continue", feeder_name7_11kv);
                                } else if (survey_type_11_7.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_7.getText().toString().trim().split("~")[1], 7, "Continue", feeder_name7_11kv);
                                }

                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 7, feeder_id7_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 6 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_6 > 0) {
                        CLICKED_FEEDER = "FEEDER 7";
                        CLICKED_FEEDER_ID = feeder_id7_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_7.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id7_11kv, 7, "Continue", feeder_name7_11kv);
                            } else if (survey_type_11_7.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_7.getText().toString().trim().split("~")[1], 7, "Continue", feeder_name7_11kv);
                            }
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 7, feeder_id7_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 6 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        continue_survey_11_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/ false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_7 > 0) {
                            CLICKED_FEEDER = "FEEDER 8";
                            CLICKED_FEEDER_ID = feeder_id8_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_8.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id8_11kv, 8, "Continue", feeder_name8_11kv);
                                } else if (survey_type_11_8.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_8.getText().toString().trim().split("~")[1], 8, "Continue", feeder_name8_11kv);
                                }

                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 8, feeder_id8_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 7 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_7 > 0) {
                        CLICKED_FEEDER = "FEEDER 8";
                        CLICKED_FEEDER_ID = feeder_id8_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_8.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id8_11kv, 8, "Continue", feeder_name8_11kv);
                            } else if (survey_type_11_8.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_8.getText().toString().trim().split("~")[1], 8, "Continue", feeder_name8_11kv);
                            }
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 8, feeder_id8_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 7 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        continue_survey_11_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/ false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_8 > 0) {
                            CLICKED_FEEDER = "FEEDER 9";
                            CLICKED_FEEDER_ID = feeder_id9_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_9.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id9_11kv, 9, "Continue", feeder_name9_11kv);
                                } else if (survey_type_11_9.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_9.getText().toString().trim().split("~")[1], 9, "Continue", feeder_name9_11kv);
                                }

                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 9, feeder_id9_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 8 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_8 > 0) {
                        CLICKED_FEEDER = "FEEDER 9";
                        CLICKED_FEEDER_ID = feeder_id9_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_9.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id9_11kv, 9, "Continue", feeder_name9_11kv);
                            } else if (survey_type_11_9.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_9.getText().toString().trim().split("~")[1], 9, "Continue", feeder_name9_11kv);
                            }
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 9, feeder_id9_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 8 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        continue_survey_11_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*is33KVSelected*/ false) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_9 > 0) {
                            CLICKED_FEEDER = "FEEDER 10";
                            CLICKED_FEEDER_ID = feeder_id10_11kv;
                            CURRENT_SURVEY_TYPE = "11KV";
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                if (survey_type_11_10.getText().toString().equalsIgnoreCase("Proposed")) {
                                    htServiceListFor11KV(feeder_id10_11kv, 10, "Continue", feeder_name10_11kv);
                                } else if (survey_type_11_10.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                    htServiceListFor11KV(feeder_name_11_10.getText().toString().trim().split("~")[1], 10, "Continue", feeder_name10_11kv);
                                }

                            } else {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 10, feeder_id10_11kv);

                            }
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 9 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_9 > 0) {
                        CLICKED_FEEDER = "FEEDER 10";
                        CLICKED_FEEDER_ID = feeder_id10_11kv;
                        CURRENT_SURVEY_TYPE = "11KV";
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                            if (survey_type_11_10.getText().toString().equalsIgnoreCase("Proposed")) {
                                htServiceListFor11KV(feeder_id10_11kv, 10, "Continue", feeder_name10_11kv);
                            } else if (survey_type_11_10.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                                htServiceListFor11KV(feeder_name_11_10.getText().toString().trim().split("~")[1], 10, "Continue", feeder_name10_11kv);
                            }
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 10, feeder_id10_11kv);

                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 9 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        continue_survey_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is33KVSelected) {
                   /* if (!is33KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 33 kV Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }*/


                    //NEW 33KV LINE EXTENSION ADDED
                    if (FEEDER_1_SELECTED_33 && !FEEDER_1_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 1 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_2_SELECTED_33 && !FEEDER_2_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 2 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_3_SELECTED_33 && !FEEDER_3_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 3 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_4_SELECTED_33 && !FEEDER_4_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 4 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_5_SELECTED_33 && !FEEDER_5_SURVEY_COMPLETED_33) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 5 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                if (is11KVSelected) {

                    if (FEEDER_1_SELECTED && !FEEDER_1_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 1 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_2_SELECTED && !FEEDER_2_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 2 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_3_SELECTED && !FEEDER_3_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 3 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_4_SELECTED && !FEEDER_4_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 4 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_5_SELECTED && !FEEDER_5_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 5 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_6_SELECTED && !FEEDER_6_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 6 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_7_SELECTED && !FEEDER_7_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 7 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_8_SELECTED && !FEEDER_8_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 8 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_9_SELECTED && !FEEDER_9_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 9 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FEEDER_10_SELECTED && !FEEDER_10_SURVEY_COMPLETED) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 10 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    /*if (!is11KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 11 kV Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                }

                if (isLTSelected) {
                    if (isLTSelectedSurvey) {
                        showLTResurveyAlertDialog(is33KVSelected, is11KVSelected, isLTSelected, 1, "");
                        Toast.makeText(WorkInvolmentsActivity.this, "Survey Completed ", Toast.LENGTH_SHORT).show();
                    } else {
                        IS_LT_SELECTED = true;
                        CURRENT_SURVEY_TYPE = "LT";
                        navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");
                    }
                } else {
                    IS_LT_SELECTED = true;
                    CURRENT_SURVEY_TYPE = "LT";
                    navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");
                }
            }
        });

        //03-01-2025 Chandu Changes
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            home_heading.setText("Estimates Details");
            work_order.setVisibility(View.GONE);
            btn_edit_work_involments.setVisibility(View.VISIBLE);
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            home_heading.setText("Solarization of SC ST");
            work_order.setVisibility(View.GONE);
            btn_edit_work_involments.setVisibility(View.VISIBLE);
        } else if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            home_heading.setText("RDSS Asset Survey");
        } else {
            home_heading.setText("Work Order Details");
            work_order.setVisibility(View.VISIBLE);


        }


        //LOCATION EDIT

        edit_survey_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                intent.putExtra("EST_NO", EST_NO);
                intent.putExtra("GIS_ID", GIS_ID);
                intent.putExtra("FEEDER_ID", FEEDER_ID_33);
                intent.putExtra("FEEDER_NAME", FEEDER_NAME_33);
                intent.putExtra("FEEDER_NO", "FEEDER 1");
                intent.putExtra("FROM", "33KV");


                //FOR MAP
                intent.putExtra("EST_NATURE", EST_NATURE);
                intent.putExtra("PURPOSE", PURPOSE);
                intent.putExtra("WORK_ORDER", wo_no);
                intent.putExtra("33KV", is33KVSelected);
                intent.putExtra("11KV", is11KVSelected);
                intent.putExtra("DTR", isDTRSelected);
                intent.putExtra("LT", isLTSelected);
                intent.putExtra("METERS", isMeters);
                intent.putExtra("MAX_LENGTH", max_length);
                intent.putExtra("REG_COUNT", REG_COUNT);
                intent.putExtra("BTN_VALUE", "33KV");
                intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                intent.putExtra("FDR_INDEX_POS", String.valueOf(1));
                intent.putExtra("Model", model);


                startActivity(intent);

            }
        });


        edit_survey_11_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor11KV(feeder_id1_11kv, 1, "EDIT", feeder_name1_11kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id1_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name1_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 1");
                    intent.putExtra("FROM", "11KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(1));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }

            }
        });

        edit_survey_11_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor11KV(feeder_id2_11kv, 2, "EDIT", feeder_name2_11kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id2_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name2_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 2");
                    intent.putExtra("FROM", "11KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(2));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }


            }
        });


        edit_survey_11_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor11KV(feeder_id3_11kv, 3, "EDIT", feeder_name3_11kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id3_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name3_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 3");
                    intent.putExtra("FROM", "11KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(3));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }


            }
        });

        edit_survey_11_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor11KV(feeder_id4_11kv, 4, "EDIT", feeder_name4_11kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id4_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name4_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 4");
                    intent.putExtra("FROM", "11KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(4));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }
            }
        });

        edit_survey_11_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor11KV(feeder_id5_11kv, 5, "EDIT", feeder_name5_11kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id5_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name5_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 5");
                    intent.putExtra("FROM", "11KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(5));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }

            }
        });
        edit_survey_11_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                    htServiceListFor11KV(
                            feeder_id6_11kv,
                            6,
                            "EDIT",
                            feeder_name6_11kv
                    );

                } else {

                    Intent intent = new Intent(
                            WorkInvolmentsActivity.this,
                            Edit33Home.class
                    );

                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id6_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name6_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 6");
                    intent.putExtra("FROM", "11KV");

                    // FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(6));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }
            }
        });
        edit_survey_11_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                    htServiceListFor11KV(
                            feeder_id7_11kv,
                            7,
                            "EDIT",
                            feeder_name7_11kv
                    );

                } else {

                    Intent intent = new Intent(
                            WorkInvolmentsActivity.this,
                            Edit33Home.class
                    );

                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id7_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name7_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 7");
                    intent.putExtra("FROM", "11KV");

                    // FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(7));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }
            }
        });
        edit_survey_11_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                    htServiceListFor11KV(
                            feeder_id8_11kv,
                            8,
                            "EDIT",
                            feeder_name8_11kv
                    );

                } else {

                    Intent intent = new Intent(
                            WorkInvolmentsActivity.this,
                            Edit33Home.class
                    );

                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id8_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name8_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 8");
                    intent.putExtra("FROM", "11KV");

                    // FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(8));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }
            }
        });
        edit_survey_11_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                    htServiceListFor11KV(
                            feeder_id9_11kv,
                            9,
                            "EDIT",
                            feeder_name9_11kv
                    );

                } else {

                    Intent intent = new Intent(
                            WorkInvolmentsActivity.this,
                            Edit33Home.class
                    );

                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id9_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name9_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 9");
                    intent.putExtra("FROM", "11KV");

                    // FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(9));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }
            }
        });
        edit_survey_11_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                    htServiceListFor11KV(
                            feeder_id10_11kv,
                            10,
                            "EDIT",
                            feeder_name10_11kv
                    );

                } else {

                    Intent intent = new Intent(
                            WorkInvolmentsActivity.this,
                            Edit33Home.class
                    );

                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id10_11kv);
                    intent.putExtra("FEEDER_NAME", feeder_name10_11kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 10");
                    intent.putExtra("FROM", "11KV");

                    // FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "11KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(10));
                    intent.putExtra("Model", model);
                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        intent.putExtra("FEEDERS", FEEDERS);
                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                    }
                    startActivity(intent);
                }
            }
        });


        edit_survey_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                intent.putExtra("EST_NO", EST_NO);
                intent.putExtra("GIS_ID", GIS_ID);
                intent.putExtra("FEEDER_ID", "LT");
                intent.putExtra("FEEDER_NAME", "LT");
                intent.putExtra("FEEDER_NO", "1");
                intent.putExtra("FROM", "LT");

                //FOR MAP
                intent.putExtra("EST_NATURE", EST_NATURE);
                intent.putExtra("PURPOSE", PURPOSE);
                intent.putExtra("WORK_ORDER", wo_no);
                intent.putExtra("33KV", is33KVSelected);
                intent.putExtra("11KV", is11KVSelected);
                intent.putExtra("DTR", isDTRSelected);
                intent.putExtra("LT", isLTSelected);
                intent.putExtra("METERS", isMeters);
                intent.putExtra("MAX_LENGTH", max_length);
                intent.putExtra("REG_COUNT", REG_COUNT);
                intent.putExtra("BTN_VALUE", "LT");
                intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                intent.putExtra("FDR_INDEX_POS", String.valueOf(1));
                intent.putExtra("Model", model);
                if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                    intent.putExtra("FEEDERS", FEEDERS);
                    intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                }
                startActivity(intent);
            }
        });


        //NEW CODE FOR 33KV LINE EXTENSION START
        continue_survey_33_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_33_KV_SELECTED = true;
                CURRENT_SURVEY_TYPE = "33KV";
                CLICKED_FEEDER = "FEEDER 1";
                CLICKED_FEEDER_ID = feeder_id1_33kv;
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                    if (survey_type_33_1.getText().toString().equalsIgnoreCase("Proposed")) {
                        htServiceListFor33KV(feeder_id1_33kv, 1, "Continue", feeder_id1_33kv);
                    } else if (survey_type_33_1.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                        htServiceListFor33KV(feeder_name_33_1.getText().toString().trim().split("~")[1], 1, "Continue", feeder_id1_33kv);
                    } else {
                        navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 1, feeder_id1_33kv);

                    }


                } else {
                    navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 1, feeder_id1_33kv);

                }

            }
        });

        continue_survey_33_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LOCATION_COUNT_33KV_FEEDER_1 > 0) {
                    IS_33_KV_SELECTED = true;
                    CURRENT_SURVEY_TYPE = "33KV";
                    CLICKED_FEEDER = "FEEDER 2";
                    CLICKED_FEEDER_ID = feeder_id2_33kv;
                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                        if (survey_type_33_2.getText().toString().equalsIgnoreCase("Proposed")) {
                            htServiceListFor33KV(feeder_id2_33kv, 2, "Continue", feeder_id2_33kv);
                        } else if (survey_type_33_2.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                            htServiceListFor33KV(feeder_name_33_2.getText().toString().trim().split("~")[1], 2, "Continue", feeder_id2_33kv);
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 2, feeder_id2_33kv);

                        }

                    } else {
                        navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 2, feeder_id2_33kv);

                    }
                } else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 1 Survey First", Toast.LENGTH_SHORT).show();
                }


            }
        });

        continue_survey_33_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LOCATION_COUNT_33KV_FEEDER_2 > 0) {
                    IS_33_KV_SELECTED = true;
                    CURRENT_SURVEY_TYPE = "33KV";
                    CLICKED_FEEDER = "FEEDER 3";
                    CLICKED_FEEDER_ID = feeder_id3_33kv;
                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                        if (survey_type_33_3.getText().toString().equalsIgnoreCase("Proposed")) {
                            htServiceListFor33KV(feeder_id3_33kv, 3, "Continue", feeder_id3_33kv);
                        } else if (survey_type_33_3.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                            htServiceListFor33KV(feeder_name_33_3.getText().toString().trim().split("~")[1], 3, "Continue", feeder_id3_33kv);
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 3, feeder_id3_33kv);

                        }

                    } else {
                        navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 3, feeder_id3_33kv);

                    }
                } else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 2 Survey First", Toast.LENGTH_SHORT).show();
                }


            }
        });

        continue_survey_33_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LOCATION_COUNT_33KV_FEEDER_3 > 0) {
                    IS_33_KV_SELECTED = true;
                    CURRENT_SURVEY_TYPE = "33KV";
                    CLICKED_FEEDER = "FEEDER 4";
                    CLICKED_FEEDER_ID = feeder_id4_33kv;
                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                        if (survey_type_33_4.getText().toString().equalsIgnoreCase("Proposed")) {
                            htServiceListFor33KV(feeder_id4_33kv, 4, "Continue", feeder_id4_33kv);
                        } else if (survey_type_33_4.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                            htServiceListFor33KV(feeder_name_33_4.getText().toString().trim().split("~")[1], 4, "Continue", feeder_id4_33kv);
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 4, feeder_id4_33kv);

                        }

                    } else {
                        navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 4, feeder_id4_33kv);

                    }
                } else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 3 Survey First", Toast.LENGTH_SHORT).show();
                }


            }
        });

        continue_survey_33_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LOCATION_COUNT_33KV_FEEDER_4 > 0) {
                    IS_33_KV_SELECTED = true;
                    CURRENT_SURVEY_TYPE = "33KV";
                    CLICKED_FEEDER = "FEEDER 5";
                    CLICKED_FEEDER_ID = feeder_id5_33kv;
                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                        if (survey_type_33_5.getText().toString().equalsIgnoreCase("Proposed")) {
                            htServiceListFor33KV(feeder_id5_33kv, 5, "Continue", feeder_id5_33kv);
                        } else if (survey_type_33_5.getText().toString().equalsIgnoreCase("LINE EXTENSION")) {
                            htServiceListFor33KV(feeder_name_33_5.getText().toString().trim().split("~")[1], 5, "Continue", feeder_id5_33kv);
                        } else {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 5, feeder_id5_33kv);

                        }

                    } else {
                        navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 5, feeder_id5_33kv);

                    }
                } else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Please Complete Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                }


            }
        });

        edit_survey_33_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor33KV(feeder_id1_33kv, 1, "EDIT", feeder_name1_33kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id1_33kv);
                    intent.putExtra("FEEDER_NAME", feeder_name1_33kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 1");
                    intent.putExtra("FROM", "33KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "33KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(1));
                    intent.putExtra("Model", model);

                    startActivity(intent);
                }


            }
        });

        edit_survey_33_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor33KV(feeder_id2_33kv, 2, "EDIT", feeder_name2_33kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id2_33kv);
                    intent.putExtra("FEEDER_NAME", feeder_name2_33kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 2");
                    intent.putExtra("FROM", "33KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "33KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(2));
                    intent.putExtra("Model", model);

                    startActivity(intent);
                }


            }
        });

        edit_survey_33_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor33KV(feeder_id3_33kv, 3, "EDIT", feeder_name3_33kv);

                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id3_33kv);
                    intent.putExtra("FEEDER_NAME", feeder_name3_33kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 3");
                    intent.putExtra("FROM", "33KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "33KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(3));
                    intent.putExtra("Model", model);

                    startActivity(intent);
                }

            }
        });

        edit_survey_33_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor33KV(feeder_id4_33kv, 4, "EDIT", feeder_name4_33kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id4_33kv);
                    intent.putExtra("FEEDER_NAME", feeder_name4_33kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 4");
                    intent.putExtra("FROM", "33KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "33KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(4));
                    intent.putExtra("Model", model);

                    startActivity(intent);
                }


            }
        });

        edit_survey_33_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    htServiceListFor33KV(feeder_id5_33kv, 5, "EDIT", feeder_name5_33kv);
                } else {
                    Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
                    intent.putExtra("EST_NO", EST_NO);
                    intent.putExtra("GIS_ID", GIS_ID);
                    intent.putExtra("FEEDER_ID", feeder_id5_33kv);
                    intent.putExtra("FEEDER_NAME", feeder_name5_33kv);
                    intent.putExtra("FEEDER_NO", "FEEDER 5");
                    intent.putExtra("FROM", "33KV");

                    //FOR MAP
                    intent.putExtra("EST_NATURE", EST_NATURE);
                    intent.putExtra("PURPOSE", PURPOSE);
                    intent.putExtra("WORK_ORDER", wo_no);
                    intent.putExtra("33KV", is33KVSelected);
                    intent.putExtra("11KV", is11KVSelected);
                    intent.putExtra("DTR", isDTRSelected);
                    intent.putExtra("LT", isLTSelected);
                    intent.putExtra("METERS", isMeters);
                    intent.putExtra("MAX_LENGTH", max_length);
                    intent.putExtra("REG_COUNT", REG_COUNT);
                    intent.putExtra("BTN_VALUE", "33KV");
                    intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
                    intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
                    intent.putExtra("FDR_INDEX_POS", String.valueOf(5));
                    intent.putExtra("Model", model);

                    startActivity(intent);
                }

            }
        });


    }

    private void navigationToEdit33Home(String feeder_id, String feeder_name, String feeder_no, int fdrIndex) {

        Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("FEEDER_ID", feeder_id);
        intent.putExtra("FEEDER_NAME", feeder_name);
        intent.putExtra("FEEDER_NO", feeder_no);
        intent.putExtra("FROM", "33KV");



        //FOR MAP
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("33KV", is33KVSelected);
        intent.putExtra("11KV", is11KVSelected);
        intent.putExtra("DTR", isDTRSelected);
        intent.putExtra("LT", isLTSelected);
        intent.putExtra("METERS", isMeters);
        intent.putExtra("MAX_LENGTH", max_length);
        intent.putExtra("REG_COUNT", REG_COUNT);
        intent.putExtra("BTN_VALUE", "33KV");
        intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
        intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(fdrIndex));
        intent.putExtra("Model", model);

        startActivity(intent);
    }

    private void navigationToEdit11Home(String feeder_id, String feeder_name, String feeder_no, int fdrIndex) {
        Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("FEEDER_ID", feeder_id);
        intent.putExtra("FEEDER_NAME", feeder_name);
        intent.putExtra("FEEDER_NO", feeder_no);
        intent.putExtra("FROM", "11KV");

        //FOR MAP
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("33KV", is33KVSelected);
        intent.putExtra("11KV", is11KVSelected);
        intent.putExtra("DTR", isDTRSelected);
        intent.putExtra("LT", isLTSelected);
        intent.putExtra("METERS", isMeters);
        intent.putExtra("MAX_LENGTH", max_length);
        intent.putExtra("REG_COUNT", REG_COUNT);
        intent.putExtra("BTN_VALUE", "11KV");
        intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
        intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(fdrIndex));
        intent.putExtra("Model", model);

        startActivity(intent);
    }


    private void htServiceListFor33KV(String feeder_id, int count, String BTNTYPE, String feeder_name) {
        //progressDialog.show();
        loader_layout_work.setVisibility(View.VISIBLE);
        JsonObject entry = new JsonObject();
        entry.addProperty("FEEDERID", feeder_id);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.htServiceList33KV(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //progressDialog.hide();
                loader_layout_work.setVisibility(View.GONE);
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                htServiceListModels.add(object.get("HT_SCNO").getAsString() + "~" + object.get("HTSC_NAME").getAsString());
                                //navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", count, feeder_id);

                            }
                            if (BTNTYPE.equalsIgnoreCase("Continue")) {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", count, feeder_id);
                            } else {
                                navigationToEdit33Home(feeder_id, feeder_name, "FEEDER " + count, count);
                            }

                        } else {
                            if (BTNTYPE.equalsIgnoreCase("Continue")) {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", count, feeder_id);
                            } else {
                                navigationToEdit33Home(feeder_id, feeder_name, "FEEDER " + count, count);
                            }
                            //navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", count, feeder_id);

                        }
                    } else {
                        if (BTNTYPE.equalsIgnoreCase("Continue")) {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", count, feeder_id);
                        } else {
                            navigationToEdit33Home(feeder_id, feeder_name, "FEEDER " + count, count);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout_work.setVisibility(View.GONE);
                // progressDialog.hide();
            }
        });

    }


    private void htServiceListFor11KV(String feeder_id, int count, String btnType, String feeder_name) {
        //progressDialog.show();
        loader_layout_work.setVisibility(View.VISIBLE);
        JsonObject entry = new JsonObject();
        entry.addProperty("FEEDERID", feeder_id);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.htServiceList11KV(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // progressDialog.hide();
                loader_layout_work.setVisibility(View.GONE);
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                htServiceListModels.add(object.get("CONSUMER_NUMBER").getAsString() + "~" + object.get("CONSUMER_NAME").getAsString());
                            }
                            if (btnType.equalsIgnoreCase("Continue")) {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", count, feeder_id);
                            } else {
                                navigationToEdit11Home(feeder_id, feeder_name, "FEEDER " + count, count);
                            }

                        } else {
                            if (btnType.equalsIgnoreCase("Continue")) {
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", count, feeder_id);
                            } else {
                                navigationToEdit11Home(feeder_id, feeder_name, "FEEDER " + count, count);
                            }
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", count, feeder_id);

                        }
                    } else {
                        if (btnType.equalsIgnoreCase("Continue")) {
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", count, feeder_id);
                        } else {
                            navigationToEdit11Home(feeder_id, feeder_name, "FEEDER " + count, count);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //progressDialog.hide();
                loader_layout_work.setVisibility(View.GONE);
            }
        });

    }


    private void singleEstimateWithFdrs() {
        //progressDialog.show();
        loader_layout_work.setVisibility(View.VISIBLE);
        layout_main_body_work.setVisibility(View.GONE);
        main_layout_involvements.setVisibility(View.GONE);
        // loader_layout_work.setVisibility(View.VISIBLE);
        // tv_progress_bar.setText("Downloading Estimates Data, Feeder & DTR Master Data");


        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        // dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        // dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
        } else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }
        dtrDetailsReqestVo.addProperty("GIS_ID", GIS_ID);

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.singleEstimateWithFdrs(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //progressDialog.cancel();
                loader_layout_work.setVisibility(View.GONE);
                layout_main_body_work.setVisibility(View.VISIBLE);
                main_layout_involvements.setVisibility(View.VISIBLE);
                // loader_layout_work.setVisibility(View.GONE);

                //loader.setVisibility(View.GONE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //  Log.v("SELECTED==", "" + surveyedResponseVo);

                    // Check if the STATUS field exists and is not null
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();

                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            //getLatLongs();

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();

                                if (object.get("GIS_ID").getAsString().equalsIgnoreCase(GIS_ID)) {
                                    model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                                    model.setEST_NATURE(object.has("EST_NATURE") && !object.get("EST_NATURE").isJsonNull() ? object.get("EST_NATURE").getAsString() : "");
                                    model.setEST_NO(object.has("EST_NO") && !object.get("EST_NO").isJsonNull() ? object.get("EST_NO").getAsString() : "");
                                    model.setREG_COUNT(object.has("REG_COUNT") && !object.get("REG_COUNT").isJsonNull() ? object.get("REG_COUNT").getAsString() : "");
                                    model.setSEC_ID(object.has("SEC_ID") && !object.get("SEC_ID").isJsonNull() ? object.get("SEC_ID").getAsString() : "");
                                    model.setSTATUS(object.has("STATUS") && !object.get("STATUS").isJsonNull() ? object.get("STATUS").getAsString() : "");
                                    model.setPURPOSE(object.has("PURPOSE") && !object.get("PURPOSE").isJsonNull() ? object.get("PURPOSE").getAsString() : "");
                                    model.setLINE_33KV(object.has("LINE_33KV") && !object.get("LINE_33KV").isJsonNull() ? object.get("LINE_33KV").getAsString() : "");
                                    model.setLINE_11KV(object.has("LINE_11KV") && !object.get("LINE_11KV").isJsonNull() ? object.get("LINE_11KV").getAsString() : "");
                                    model.setLINE_LT(object.has("LINE_LT") && !object.get("LINE_LT").isJsonNull() ? object.get("LINE_LT").getAsString() : "");
                                    model.setDTR(object.has("DTR") && !object.get("DTR").isJsonNull() ? object.get("DTR").getAsString() : "");
                                    model.setSS(object.has("SS") && !object.get("SS").isJsonNull() ? object.get("SS").getAsString() : "");


                                    model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                    model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                    model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                    model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                    model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");
                                    model.setEST_TYPE(object.has("EST_TYPE") && !object.get("EST_TYPE").isJsonNull() ? object.get("EST_TYPE").getAsString() : "");
                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                                        model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                        model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                    }

                                    model.setMODIFY_WI(object.has("MODIFY_WI") && !object.get("MODIFY_WI").isJsonNull() ? object.get("MODIFY_WI").getAsString() : "");

                                    TOWER_LENGTH = Integer.parseInt(object.get("TOWER_LENGTH").getAsString());
                                    if (object.has("FDR_PARTICULARS") && object.get("FDR_PARTICULARS").isJsonArray()) {
                                        JsonArray fdrArray = object.getAsJsonArray("FDR_PARTICULARS");
                                        List<FdrParticularsModel> fdrList = new ArrayList<>();

                                        for (int j = 0; j < fdrArray.size(); j++) {
                                            JsonObject fdrObject = fdrArray.get(j).getAsJsonObject();
                                            FdrParticularsModel fdrModel = new FdrParticularsModel();

                                            fdrModel.setFEEDER(fdrObject.has("FEEDER") && !fdrObject.get("FEEDER").isJsonNull() ? fdrObject.get("FEEDER").getAsString() : null);
                                            // fdrModel.setFEEDER_NAME(fdrObject.has("FEEDER_NAME") && !fdrObject.get("FEEDER_NAME").isJsonNull() ? fdrObject.get("FEEDER_NAME").getAsString() : "");
                                            fdrModel.setVOLTAGE_LEVEL(fdrObject.has("VOLTAGE_LEVEL") && !fdrObject.get("VOLTAGE_LEVEL").isJsonNull() ? fdrObject.get("VOLTAGE_LEVEL").getAsString() : "");
                                            fdrModel.setSURVEY_TYPE(fdrObject.has("SURVEY_TYPE") && !fdrObject.get("SURVEY_TYPE").isJsonNull() ? fdrObject.get("SURVEY_TYPE").getAsString() : "");
                                            fdrModel.setSS_NAME(fdrObject.has("SS_NAME") && !fdrObject.get("SS_NAME").isJsonNull() ? fdrObject.get("SS_NAME").getAsString() : "");
                                            fdrModel.setFDR_INDEX(Integer.parseInt(fdrObject.has("FDR_INDEX") && !fdrObject.get("FDR_INDEX").isJsonNull() ? fdrObject.get("FDR_INDEX").getAsString() : ""));
                                            fdrModel.setFDR_COUNT(Integer.parseInt(fdrObject.has("FDR_COUNT") && !fdrObject.get("FDR_COUNT").isJsonNull() ? fdrObject.get("FDR_COUNT").getAsString() : ""));
                                            fdrModel.setLT_CKT_CODE(fdrObject.has("LT_CKT_CODE") && !fdrObject.get("LT_CKT_CODE").isJsonNull() ? fdrObject.get("LT_CKT_CODE").getAsString() : null);
                                            fdrModel.setLATITUDE(fdrObject.has("LATITUDE") && !fdrObject.get("LATITUDE").isJsonNull() ? fdrObject.get("LATITUDE").getAsString() : "");
                                            fdrModel.setLONGITUDE(fdrObject.has("LONGITUDE") && !fdrObject.get("LONGITUDE").isJsonNull() ? fdrObject.get("LONGITUDE").getAsString() : "");
                                            fdrModel.setPOLE_COUNT(fdrObject.has("POLE_COUNT") && !fdrObject.get("POLE_COUNT").isJsonNull() ? fdrObject.get("POLE_COUNT").getAsInt() : 0);

                                            fdrList.add(fdrModel);
                                        }
                                        model.setFDR_PARTICULARS(fdrList);
                                    }

                                    setViewsData();


                                }


                            }


                        } else {
                            //  loader_layout_work.setVisibility(View.GONE);
                            // layout_main_body_work.setVisibility(View.VISIBLE);
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(WorkInvolmentsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(WorkInvolmentsActivity.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //progressDialog.cancel();
                loader_layout_work.setVisibility(View.GONE);
                layout_main_body_work.setVisibility(View.VISIBLE);
                Toast.makeText(WorkInvolmentsActivity.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setViewsData() {
        offlineModel = model;
        try {
            if (("WORK_ORDER".equalsIgnoreCase(LoginActivity.SURVEY_FROM) || "WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM))) {
                boolean shouldShowButton = false;

                String woOld = model.getWO_OLD();
                String modifyWi = model.getMODIFY_WI();

                if (woOld != null && woOld.equalsIgnoreCase("YES")) {
                    shouldShowButton = true;
                }

                if (modifyWi != null && modifyWi.equalsIgnoreCase("Y")) {
                    shouldShowButton = true;
                }

                btn_edit_work_involments.setVisibility(shouldShowButton ? View.VISIBLE : View.GONE);
            }

            if (model.getLINE_LT().equalsIgnoreCase("Y")) {
                isLTSelected = true;
                main_layout_lt.setVisibility(View.VISIBLE);
                no_locations_lt.setText("" + totalPolesLT.size());
                if (totalPolesLT.isEmpty()) {
                    continue_survey_lt.setText("Start Survey");
                } else {
                    continue_survey_lt.setText("Continue Survey");
                    edit_survey_lt.setVisibility(View.VISIBLE);
                }

                //NEW LT CHANGES
                if (totalPolesLT.isEmpty()) {
                    process_lt_survey.setText("Start Survey");
                } else {
                    process_lt_survey.setText("Continue Survey");
                }

                int dtr_nos = 0; // sum of COUNT_CKT from poles
                int ltFdrCount = 0; // FDR_COUNT from LT level in FDR_PARTICULARS

                tv_no_dtr.setText("No of LT Circuits :");

                if ((model.getLINE_11KV().equalsIgnoreCase("Y") && model.getLINE_LT().equalsIgnoreCase("Y")) ||
                        (model.getLINE_33KV().equalsIgnoreCase("Y") && model.getLINE_11KV().equalsIgnoreCase("Y") &&
                                model.getLINE_LT().equalsIgnoreCase("Y"))) {

                    // Get FDR_COUNT from LT voltage level, if available
                    for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                        if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                            ltFdrCount = model.getFDR_PARTICULARS().get(i).getFDR_COUNT();
                            break;
                        }
                    }

                    // Get COUNT_CKT from poles where DTR Pole + LT Available
                    for (int j = 0; j < totalPoles11.size(); j++) {
                        if (totalPoles11.get(j).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole") && totalPoles11.get(j).getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed") &&
                                totalPoles11.get(j).getLT_AVAILABLE().equalsIgnoreCase("Yes")) {
                            dtr_nos += totalPoles11.get(j).getCOUNT_CKT();
                        }
                    }

                    // Set the total count (FDR_COUNT + COUNT_CKT)
                    int totalCircuits = ltFdrCount + dtr_nos;
                    no_dtr_lt.setText(String.valueOf(totalCircuits));

                } else {
                    // If LT line is not required based on condition, show only LT FDR_COUNT
                    for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                        if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                            no_dtr_lt.setText("" + model.getFDR_PARTICULARS().get(i).getFDR_COUNT());
                            break;
                        }
                    }
                }


                if (model.getLINE_11KV().equalsIgnoreCase("Y") || model.getLINE_33KV().equalsIgnoreCase("Y")) {
                    getExistingPoleDataNew();
                } else if (model.getLINE_LT().equalsIgnoreCase("Y")) {
                    getExistingPoleDataNew();
                    // getSectionWiseLTCircuits();
                }


//                if (model.getLINE_11KV().equalsIgnoreCase("Y") || model.getLINE_33KV().equalsIgnoreCase("Y")) {
//                    getExistingPoleDataNew();
//                } else {
//                    getExistingPoleDataNew();
//                    // getSectionWiseLTCircuits();
//                }


                process_lt_survey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (is33KVSelected) {
                            //NEW 33KV LINE EXTENSION ADDED
                            if (FEEDER_1_SELECTED_33 && !FEEDER_1_SURVEY_COMPLETED_33) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 1 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_2_SELECTED_33 && !FEEDER_2_SURVEY_COMPLETED_33) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 2 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_3_SELECTED_33 && !FEEDER_3_SURVEY_COMPLETED_33) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 3 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_4_SELECTED_33 && !FEEDER_4_SURVEY_COMPLETED_33) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 4 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_5_SELECTED_33 && !FEEDER_5_SURVEY_COMPLETED_33) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 33kv Feeder 5 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (is11KVSelected) {

                            if (FEEDER_1_SELECTED && !FEEDER_1_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 1 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_2_SELECTED && !FEEDER_2_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 2 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_3_SELECTED && !FEEDER_3_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 3 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_4_SELECTED && !FEEDER_4_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 4 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_5_SELECTED && !FEEDER_5_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 5 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_6_SELECTED && !FEEDER_6_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 6 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_7_SELECTED && !FEEDER_7_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 7 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_8_SELECTED && !FEEDER_8_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 8 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_9_SELECTED && !FEEDER_9_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 9 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (FEEDER_10_SELECTED && !FEEDER_10_SURVEY_COMPLETED) {
                                Toast.makeText(WorkInvolmentsActivity.this, "Please Complete 11kv Feeder 10 Survey", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }


                        if (model.getLINE_11KV().equalsIgnoreCase("Y") || model.getLINE_33KV().equalsIgnoreCase("Y")) {

                            //  openLTSurveyDialog();
                            //openOnlyLTSurveyDialog(model.getFDR_PARTICULARS());

                            openNewLTSurveyDialog(model.getFDR_PARTICULARS());
                        } else {
                            openOnlyLTSurveyDialog(model.getFDR_PARTICULARS());
                        }

                    }
                });


            }


            //NEW CODE FOR 33KV LINE EXTENSION START

            if (model.getLINE_33KV().equalsIgnoreCase("Y")) {
                main_layout_33.setVisibility(View.VISIBLE);
                is33KVSelected = true;

                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 1) {
                            FEEDER_1_SELECTED_33 = true;
                            FEEDER_1_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder1SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_1.setVisibility(View.VISIBLE);

                            SELECTED_SS_LAT_33KV_1 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_33KV_1 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            ss_name_33_1.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_33_1.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_33_1.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_33_1.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_33_1.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_33_1.setTextColor(Color.parseColor("#4685F3"));
                            }


                            feeder_name1_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id1_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_33_1 = true;
                                feeder_name_33_1.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_1.setText(feeder_name1_33kv);
                            }


                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 2) {
                            FEEDER_2_SELECTED_33 = true;
                            FEEDER_2_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder2SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_2.setVisibility(View.VISIBLE);
                            ss_name_33_2.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_33KV_2 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_33KV_2 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_33_2.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_33_2.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_33_2.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_33_2.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_33_2.setTextColor(Color.parseColor("#4685F3"));
                            }

                            feeder_name2_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id2_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_33_2 = true;
                                feeder_name_33_2.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_2.setText(feeder_name2_33kv);
                            }

                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 3) {
                            FEEDER_3_SELECTED_33 = true;
                            FEEDER_3_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder3SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_3.setVisibility(View.VISIBLE);
                            ss_name_33_3.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_33KV_3 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_33KV_3 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_33_3.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_33_3.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_33_3.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_33_3.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_33_3.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name3_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id3_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_33_3 = true;
                                feeder_name_33_3.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_3.setText(feeder_name3_33kv);
                            }

                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 4) {
                            FEEDER_4_SELECTED_33 = true;
                            FEEDER_4_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder4SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_4.setVisibility(View.VISIBLE);
                            ss_name_33_4.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_33KV_4 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_33KV_4 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_33_4.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_33_4.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_33_4.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_33_4.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_33_4.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name4_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id4_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_33_4 = true;
                                feeder_name_33_4.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_4.setText(feeder_name4_33kv);
                            }

                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 5) {
                            FEEDER_5_SELECTED_33 = true;
                            FEEDER_5_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder5SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_5.setVisibility(View.VISIBLE);
                            ss_name_33_5.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_33KV_5 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_33KV_5 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_33_5.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_33_5.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_33_5.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_33_5.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_33_5.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name5_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id5_33kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_33_5 = true;
                                feeder_name_33_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_5.setText(feeder_name5_33kv);
                            }

                        }


                    }
                }

                for (int i = 0; i < totalPoles33.size(); i++) {
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id1_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_1++;

                    }
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id2_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_2++;

                    }
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id3_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_3++;

                    }
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id4_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_4++;

                    }
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id5_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_5++;

                    }
                }
            } else {
                main_layout_33.setVisibility(View.GONE);
                is33KVSelected = false;
            }


            no_locations_33_1.setText("" + LOCATION_COUNT_33KV_FEEDER_1);
            no_locations_33_2.setText("" + LOCATION_COUNT_33KV_FEEDER_2);
            no_locations_33_3.setText("" + LOCATION_COUNT_33KV_FEEDER_3);
            no_locations_33_4.setText("" + LOCATION_COUNT_33KV_FEEDER_4);
            no_locations_33_5.setText("" + LOCATION_COUNT_33KV_FEEDER_5);


            if (LOCATION_COUNT_33KV_FEEDER_1 == 0) {
                continue_survey_33_1.setText("Start Survey");
            } else {
                continue_survey_33_1.setText("Continue Survey");
                // edit_survey_33_1.setVisibility(View.VISIBLE);

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_33_1.setVisibility(View.GONE);
                } else {
                    edit_survey_33_1.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_33KV_FEEDER_2 == 0) {
                continue_survey_33_2.setText("Start Survey");
            } else {
                continue_survey_33_2.setText("Continue Survey");
                //edit_survey_33_2.setVisibility(View.VISIBLE);

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_33_2.setVisibility(View.GONE);
                } else {
                    edit_survey_33_2.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_33KV_FEEDER_3 == 0) {
                continue_survey_33_3.setText("Start Survey");
            } else {
                continue_survey_33_3.setText("Continue Survey");
                // edit_survey_33_3.setVisibility(View.VISIBLE);
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_33_3.setVisibility(View.GONE);
                } else {
                    edit_survey_33_3.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_33KV_FEEDER_4 == 0) {
                continue_survey_33_4.setText("Start Survey");
            } else {
                continue_survey_33_4.setText("Continue Survey");
                //edit_survey_33_4.setVisibility(View.VISIBLE);
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_33_4.setVisibility(View.GONE);
                } else {
                    edit_survey_33_4.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_33KV_FEEDER_5 == 0) {
                continue_survey_33_5.setText("Start Survey");
            } else {
                continue_survey_33_5.setText("Continue Survey");
                // edit_survey_33_5.setVisibility(View.VISIBLE);

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_33_5.setVisibility(View.GONE);
                } else {
                    edit_survey_33_5.setVisibility(View.VISIBLE);
                }
            }

            //NEW CODE FOR 33KV LINE EXTENSION END


            if (model.getLINE_11KV().equalsIgnoreCase("Y")) {
                main_layout_11.setVisibility(View.VISIBLE);
                is11KVSelected = true;

                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                        int index = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();

                        if (index == 1) {
                            FEEDER_1_SELECTED = true;
                            FEEDER_1_SURVEY_COMPLETED = databaseEstimates.getFeeder1SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_1.setVisibility(View.VISIBLE);

                            SELECTED_SS_LAT_11KV_1 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_1 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            ss_name_11_1.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            survey_type_11_1.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_11_1.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_11_1.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_11_1.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_11_1.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_11_1.setTextColor(Color.parseColor("#4685F3"));
                            }

                            feeder_name1_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id1_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_1 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_1 = true;
                                feeder_name_11_1.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_1.setText(feeder_name1_11kv);
                            }


                        }
                        if (index == 2) {
                            FEEDER_2_SELECTED = true;
                            FEEDER_2_SURVEY_COMPLETED = databaseEstimates.getFeeder2SurveyStatusNEW(EST_NO, GIS_ID);


                            main_layout_11_2.setVisibility(View.VISIBLE);
                            ss_name_11_2.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            SELECTED_SS_LAT_11KV_2 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_2 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            survey_type_11_2.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_11_2.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_11_2.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_11_2.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_11_2.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_11_2.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name2_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id2_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_2 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_2 = true;
                                feeder_name_11_2.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_2.setText(feeder_name2_11kv);
                            }

                        }
                        if (index == 3) {
                            FEEDER_3_SELECTED = true;
                            FEEDER_3_SURVEY_COMPLETED = databaseEstimates.getFeeder3SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_3.setVisibility(View.VISIBLE);
                            ss_name_11_3.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_11KV_3 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_3 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            survey_type_11_3.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_11_3.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_11_3.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_11_3.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_11_3.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_11_3.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name3_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id3_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_3 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_3 = true;
                                feeder_name_11_3.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_3.setText(feeder_name3_11kv);
                            }

                        }
                        if (index == 4) {
                            FEEDER_4_SELECTED = true;
                            FEEDER_4_SURVEY_COMPLETED = databaseEstimates.getFeeder4SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_4.setVisibility(View.VISIBLE);
                            ss_name_11_4.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_11KV_4 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_4 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());
                            survey_type_11_4.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_11_4.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_11_4.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_11_4.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_11_4.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_11_4.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name4_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id4_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_4 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_4 = true;
                                feeder_name_11_4.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_4.setText(feeder_name4_11kv);
                            }

                        }
                        if (index == 5) {
                            FEEDER_5_SELECTED = true;
                            FEEDER_5_SURVEY_COMPLETED = databaseEstimates.getFeeder5SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_5.setVisibility(View.VISIBLE);
                            ss_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_11KV_5 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_5 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());
                            survey_type_11_5.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_11_5.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_11_5.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_11_5.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_11_5.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_11_5.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name5_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id5_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_5 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_5 = true;
                                feeder_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_5.setText(feeder_name5_11kv);
                            }

                        }

                        if (index == 6) {
                            FEEDER_6_SELECTED = true;
                            FEEDER_6_SURVEY_COMPLETED = databaseEstimates.getFeeder6SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_6.setVisibility(View.VISIBLE);

                            SELECTED_SS_LAT_11KV_6 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_6 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            ss_name_11_6.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            survey_type_11_6.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_11_6.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_11_6.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_11_6.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_11_6.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_11_6.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name6_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id6_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_6 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_6 = true;
                                feeder_name_11_6.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_6.setText(feeder_name6_11kv);
                            }
                        }
                        if (index == 7) {
                            FEEDER_7_SELECTED = true;
                            FEEDER_7_SURVEY_COMPLETED = databaseEstimates.getFeeder7SurveyStatusNEW(EST_NO, GIS_ID);


                            main_layout_11_7.setVisibility(View.VISIBLE);
                            ss_name_11_7.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            SELECTED_SS_LAT_11KV_7 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_7 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            survey_type_11_7.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_11_7.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_11_7.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_11_7.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_11_7.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_11_7.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name7_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id7_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_7 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_7 = true;
                                feeder_name_11_7.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_7.setText(feeder_name7_11kv);
                            }
                        }
                        if (index == 8) {
                            FEEDER_8_SELECTED = true;
                            FEEDER_8_SURVEY_COMPLETED = databaseEstimates.getFeeder8SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_8.setVisibility(View.VISIBLE);
                            ss_name_11_8.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_11KV_8 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_8 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());

                            survey_type_11_8.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            feeder_name8_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id8_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_8 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_8 = true;
                                feeder_name_11_8.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_8.setText(feeder_name8_11kv);
                            }
                        }
                        if (index == 9) {
                            FEEDER_9_SELECTED = true;
                            FEEDER_9_SURVEY_COMPLETED = databaseEstimates.getFeeder9SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_9.setVisibility(View.VISIBLE);
                            ss_name_11_9.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_11KV_9 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_9 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());
                            survey_type_11_9.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            feeder_name9_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id9_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_9 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_9 = true;
                                feeder_name_11_9.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_9.setText(feeder_name9_11kv);
                            }
                        }
                        if (index == 10) {
                            FEEDER_10_SELECTED = true;
                            FEEDER_10_SURVEY_COMPLETED = databaseEstimates.getFeeder10SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_10.setVisibility(View.VISIBLE);
                            ss_name_11_10.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());

                            SELECTED_SS_LAT_11KV_10 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLATITUDE());
                            SELECTED_SS_LNG_11KV_10 = Double.parseDouble(model.getFDR_PARTICULARS().get(i).getLONGITUDE());
                            survey_type_11_10.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());
                            String surveyType = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                            survey_type_11_10.setText(surveyType);
                            if ("Proposed".equalsIgnoreCase(surveyType)) {
                                survey_type_11_10.setBackgroundResource(R.drawable.bg_extension);
                                survey_type_11_10.setTextColor(Color.parseColor("#CA9345"));
                            } else if ("Line Extension".equalsIgnoreCase(surveyType)) {
                                survey_type_11_10.setBackgroundResource(R.drawable.bg_extension_line);
                                survey_type_11_10.setTextColor(Color.parseColor("#4685F3"));
                            }
                            feeder_name10_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[0];
                            feeder_id10_11kv = model.getFDR_PARTICULARS().get(i).getFEEDER().split("~")[1];

                            FEEDER_TYPE_11KV_10 = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                IS_LINE_EXTENSION_11_10 = true;
                                feeder_name_11_10.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_10.setText(feeder_name10_11kv);
                            }
                        }


                    }
                }

                for (int i = 0; i < totalPoles11.size(); i++) {
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id1_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_1++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id2_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_2++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id3_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_3++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id4_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_4++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id5_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_5++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id6_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_6++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id7_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_7++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id8_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_8++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id9_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_9++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(feeder_id10_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_10++;

                    }

                }
            } else {
                main_layout_11.setVisibility(View.GONE);
                is11KVSelected = false;
            }


            no_locations_11_1.setText("" + LOCATION_COUNT_11KV_FEEDER_1);
            no_locations_11_2.setText("" + LOCATION_COUNT_11KV_FEEDER_2);
            no_locations_11_3.setText("" + LOCATION_COUNT_11KV_FEEDER_3);
            no_locations_11_4.setText("" + LOCATION_COUNT_11KV_FEEDER_4);
            no_locations_11_5.setText("" + LOCATION_COUNT_11KV_FEEDER_5);

            no_locations_11_6.setText("" + LOCATION_COUNT_11KV_FEEDER_6);
            no_locations_11_7.setText("" + LOCATION_COUNT_11KV_FEEDER_7);
            no_locations_11_8.setText("" + LOCATION_COUNT_11KV_FEEDER_8);
            no_locations_11_9.setText("" + LOCATION_COUNT_11KV_FEEDER_9);
            no_locations_11_10.setText("" + LOCATION_COUNT_11KV_FEEDER_10);


            if (LOCATION_COUNT_11KV_FEEDER_1 == 0) {
                continue_survey_11_1.setText("Start Survey");
            } else {
                continue_survey_11_1.setText("Continue Survey");
                // edit_survey_11_1.setVisibility(View.VISIBLE);

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_1.setVisibility(View.VISIBLE);
                } else {
                    edit_survey_11_1.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_11KV_FEEDER_2 == 0) {
                continue_survey_11_2.setText("Start Survey");
            } else {
                continue_survey_11_2.setText("Continue Survey");
                // edit_survey_11_2.setVisibility(View.VISIBLE);

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_2.setVisibility(View.GONE);
                } else {
                    edit_survey_11_2.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_11KV_FEEDER_3 == 0) {
                continue_survey_11_3.setText("Start Survey");
            } else {
                continue_survey_11_3.setText("Continue Survey");
                // edit_survey_11_3.setVisibility(View.VISIBLE);

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_3.setVisibility(View.GONE);
                } else {
                    edit_survey_11_3.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_11KV_FEEDER_4 == 0) {
                continue_survey_11_4.setText("Start Survey");
            } else {
                continue_survey_11_4.setText("Continue Survey");
                // edit_survey_11_4.setVisibility(View.VISIBLE);

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_4.setVisibility(View.GONE);
                } else {
                    edit_survey_11_4.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_11KV_FEEDER_5 == 0) {
                continue_survey_11_5.setText("Start Survey");
            } else {
                continue_survey_11_5.setText("Continue Survey");
                // edit_survey_11_5.setVisibility(View.VISIBLE);

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_5.setVisibility(View.GONE);
                } else {
                    edit_survey_11_5.setVisibility(View.VISIBLE);
                }
            }
            if (LOCATION_COUNT_11KV_FEEDER_6 == 0) {
                continue_survey_11_6.setText("Start Survey");
            } else {
                continue_survey_11_6.setText("Continue Survey");

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_6.setVisibility(View.GONE);
                } else {
                    edit_survey_11_6.setVisibility(View.VISIBLE);
                }
            }

            if (LOCATION_COUNT_11KV_FEEDER_7 == 0) {
                continue_survey_11_7.setText("Start Survey");
            } else {
                continue_survey_11_7.setText("Continue Survey");

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_7.setVisibility(View.GONE);
                } else {
                    edit_survey_11_7.setVisibility(View.VISIBLE);
                }
            }
            if (LOCATION_COUNT_11KV_FEEDER_8 == 0) {
                continue_survey_11_8.setText("Start Survey");
            } else {
                continue_survey_11_8.setText("Continue Survey");

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_8.setVisibility(View.GONE);
                } else {
                    edit_survey_11_8.setVisibility(View.VISIBLE);
                }
            }
            if (LOCATION_COUNT_11KV_FEEDER_9 == 0) {
                continue_survey_11_9.setText("Start Survey");
            } else {
                continue_survey_11_9.setText("Continue Survey");

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_9.setVisibility(View.GONE);
                } else {
                    edit_survey_11_9.setVisibility(View.VISIBLE);
                }
            }
            if (LOCATION_COUNT_11KV_FEEDER_10 == 0) {
                continue_survey_11_10.setText("Start Survey");
            } else {
                continue_survey_11_10.setText("Continue Survey");

                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    edit_survey_11_10.setVisibility(View.GONE);
                } else {
                    edit_survey_11_10.setVisibility(View.VISIBLE);
                }
            }


            if (IS_LINE_EXTENSION_33 ||
                    IS_LINE_EXTENSION_33_1 ||
                    IS_LINE_EXTENSION_33_2 ||
                    IS_LINE_EXTENSION_33_3 ||
                    IS_LINE_EXTENSION_33_4 ||
                    IS_LINE_EXTENSION_33_5 ||
                    IS_LINE_EXTENSION_11_1 ||
                    IS_LINE_EXTENSION_11_2 ||
                    IS_LINE_EXTENSION_11_3 ||
                    IS_LINE_EXTENSION_11_4 ||
                    IS_LINE_EXTENSION_11_5 ||
                    IS_LINE_EXTENSION_11_6 ||
                    IS_LINE_EXTENSION_11_7 ||
                    IS_LINE_EXTENSION_11_8 ||
                    IS_LINE_EXTENSION_11_9 ||
                    IS_LINE_EXTENSION_11_10
            ) {
                getExistingPoleDataNew();
                //getSectionWiseLTCircuits();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openNewLTSurveyDialog(List<FdrParticularsModel> ltParticularsTotal) {
        List<FdrParticularsModel> ltParticulars = new ArrayList<>();

        for (int i = 0; i < ltParticularsTotal.size(); i++) {
            if (ltParticularsTotal.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                ltParticulars.add(ltParticularsTotal.get(i));
            }
        }



        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        View view = getLayoutInflater().inflate(R.layout.lt_new_survey_dialog, null);


        LinearLayout mainLayout = view.findViewById(R.id.main_layout_proposed); // Or any other outer layout
        mainLayout.setPadding(0, 0, 0, 100); // Add enough bottom padding

        payDialog.setContentView(view);
        payDialog.getWindow().setLayout(width, height);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        RelativeLayout back_layout = payDialog.findViewById(R.id.back_layout);
        RelativeLayout close_layout = payDialog.findViewById(R.id.close_layout);
        LinearLayout main_layout_proposed = payDialog.findViewById(R.id.main_layout_proposed);
        LinearLayout main_layout_existing = payDialog.findViewById(R.id.main_layout_existing);
        RecyclerView dialog_rv = payDialog.findViewById(R.id.dialog_rv);
        TextView tv_error_msg = payDialog.findViewById(R.id.tv_error_msg);

        if (!ltParticulars.isEmpty()) {
            main_layout_existing.setVisibility(View.VISIBLE);
        }


        dialog_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WorkInvolmentsActivity.this);
        dialog_rv.setLayoutManager(layoutManager);
        dialog_rv.setFocusable(false);
        OnlyLtSurveyAdapter adapter = new OnlyLtSurveyAdapter(EST_NO, GIS_ID, ltParticulars, WorkInvolmentsActivity.this, WorkInvolmentsActivity.this);
        dialog_rv.setAdapter(adapter);

// Setup proposed LT survey list
        RecyclerView dialog_rv_new = payDialog.findViewById(R.id.dialog_rv_new);


        ArrayList<NewModel> dtrList = new ArrayList<>();
        HashSet<String> uniqueDtrCodes = new HashSet<>();

        for (int i = 0; i < totalPoles11.size(); i++) {

            NewModel model = totalPoles11.get(i);

            if (model.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole") &&
                    model.getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed") &&
                    model.getLT_AVAILABLE().equalsIgnoreCase("Yes")) {

                String dtrCode = model.getDTR_STRUCTURE_CODE();

                // ✅ Add only if not already added
                if (!uniqueDtrCodes.contains(dtrCode)) {
                    uniqueDtrCodes.add(dtrCode);
                    dtrList.add(model);
                }
            }
        }

//// Collect DTR list
//        ArrayList<NewModel> dtrList = new ArrayList<>();
//        dtrList.clear();
//        for (int i = 0; i < totalPoles11.size(); i++) {
//            if (totalPoles11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
//                if (totalPoles11.get(i).getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed")) {
//                    if (totalPoles11.get(i).getLT_AVAILABLE().equalsIgnoreCase("Yes")) {
//                        dtrList.add(totalPoles11.get(i));
//                    }
//                }
//            }
//        }

        if (!dtrList.isEmpty()) {
            main_layout_proposed.setVisibility(View.VISIBLE);
            dialog_rv_new.setVisibility(View.VISIBLE);
            tv_error_msg.setVisibility(View.GONE);
        } else {
            tv_error_msg.setVisibility(View.GONE);
            dialog_rv_new.setVisibility(View.GONE);
            main_layout_proposed.setVisibility(View.GONE);
        }

        if (main_layout_proposed.getVisibility() == View.GONE && main_layout_existing.getVisibility() == View.GONE) {
            tv_error_msg.setVisibility(View.VISIBLE);
        }

// RecyclerView Padding Fix
        dialog_rv_new.setHasFixedSize(true);
        dialog_rv_new.setClipToPadding(false); // Important!
        dialog_rv_new.setPadding(0, 0, 0, 150); // Bottom padding to avoid overlap

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(WorkInvolmentsActivity.this);
        dialog_rv_new.setLayoutManager(layoutManager1);
        dialog_rv_new.setFocusable(false);
        LtSurveyAdapter adapter1 = new LtSurveyAdapter(dtrList, WorkInvolmentsActivity.this, WorkInvolmentsActivity.this);
        dialog_rv_new.setAdapter(adapter1);

// Finally, show dialog
        payDialog.show();


        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });
    }

    private void openOnlyLTSurveyDialog(List<FdrParticularsModel> ltParticularsTotal) {
        List<FdrParticularsModel> ltParticulars = new ArrayList<>();

        for (int i = 0; i < ltParticularsTotal.size(); i++) {
            if (ltParticularsTotal.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                ltParticulars.add(ltParticularsTotal.get(i));
            }
        }




        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.lt_survey_dialog);
        payDialog.getWindow().setLayout(width, height);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        RelativeLayout back_layout = payDialog.findViewById(R.id.back_layout);
        RelativeLayout close_layout = payDialog.findViewById(R.id.close_layout);

        RecyclerView dialog_rv = payDialog.findViewById(R.id.dialog_rv);
        dialog_rv.setVisibility(View.VISIBLE);

        dialog_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WorkInvolmentsActivity.this);
        dialog_rv.setLayoutManager(layoutManager);
        dialog_rv.setFocusable(false);
        OnlyLtSurveyAdapter adapter = new OnlyLtSurveyAdapter(EST_NO, GIS_ID, ltParticulars, WorkInvolmentsActivity.this, WorkInvolmentsActivity.this);
        dialog_rv.setAdapter(adapter);

        payDialog.show();

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });
    }

    private void openLTSurveyDialog() {

        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.lt_survey_dialog);
        payDialog.getWindow().setLayout(width, height);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        RelativeLayout back_layout = payDialog.findViewById(R.id.back_layout);
        RelativeLayout close_layout = payDialog.findViewById(R.id.close_layout);


        RecyclerView dialog_rv = payDialog.findViewById(R.id.dialog_rv);
        TextView tv_error_msg = payDialog.findViewById(R.id.tv_error_msg);
        ArrayList<NewModel> dtrList = new ArrayList<>();
        dtrList.clear();
        for (int i = 0; i < totalPoles11.size(); i++) {

            if (totalPoles11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                if (totalPoles11.get(i).getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed")) {
                    if (totalPoles11.get(i).getLT_AVAILABLE().equalsIgnoreCase("Yes")) {
                        dtrList.add(totalPoles11.get(i));
                    }
                }

            }
        }

        if (!dtrList.isEmpty()) {
            dialog_rv.setVisibility(View.VISIBLE);
            tv_error_msg.setVisibility(View.GONE);
        } else {
            tv_error_msg.setVisibility(View.VISIBLE);
            dialog_rv.setVisibility(View.GONE);
        }

        dialog_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WorkInvolmentsActivity.this);
        dialog_rv.setLayoutManager(layoutManager);
        dialog_rv.setFocusable(false);
        LtSurveyAdapter adapter = new LtSurveyAdapter(dtrList, WorkInvolmentsActivity.this, WorkInvolmentsActivity.this);
        dialog_rv.setAdapter(adapter);


        payDialog.show();

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

    }


    private void handleSurveyClick(int feederIndex, String feederName) {
        if (is33KVSelected) {
            if (!is33KVSelectedSurvey) {
                Toast.makeText(this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
            } else {
                if (is11KVSelectedSurvey) {
                    show11kVResurveyAlertDialog(is33KVSelected, is11KVSelected, isLTSelected, feederIndex, feederName);
                    Toast.makeText(this, "Survey Completed for Feeder " + feederIndex, Toast.LENGTH_SHORT).show();
                } else {
                    navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", feederIndex, feederName);
                }
            }
        } else {
            if (is11KVSelectedSurvey) {
                show11kVResurveyAlertDialog(is33KVSelected, is11KVSelected, isLTSelected, feederIndex, feederName);
                Toast.makeText(this, "Survey Completed for Feeder " + feederIndex, Toast.LENGTH_SHORT).show();
            } else {
                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", feederIndex, feederName);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        super.onBackPressed();
        Intent intent = new Intent(WorkInvolmentsActivity.this, SurveyList.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_to_bottom);
    }


    private void getPurposeData() {
        progressDialog.show();
        loader_layout_work.setVisibility(View.VISIBLE);
        // tv_progress_bar.setText("Downloading Estimates Data, Feeder & DTR Master Data");
        //  layout_main_body_work.setVisibility(View.GONE);

        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        //dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        // dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        dtrDetailsReqestVo.addProperty("ACCESS_TOKEN", loginDataResponses.get(0).getACCESS_TOKEN());
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
        } else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }
        dtrDetailsReqestVo.addProperty("GIS_ID", GIS_ID);

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance("").create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.singleEstimateWithFdrs(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout_work.setVisibility(View.GONE);
                progressDialog.hide();
                //  layout_main_body_work.setVisibility(View.VISIBLE);
                //loader.setVisibility(View.GONE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);

                    // Check if the STATUS field exists and is not null
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();

                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            //getLatLongs();

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                PurposeModel model = new PurposeModel();

                                model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                                model.setEST_NATURE(object.has("EST_NATURE") && !object.get("EST_NATURE").isJsonNull() ? object.get("EST_NATURE").getAsString() : "");
                                model.setEST_NO(object.has("EST_NO") && !object.get("EST_NO").isJsonNull() ? object.get("EST_NO").getAsString() : "");
                                model.setREG_COUNT(object.has("REG_COUNT") && !object.get("REG_COUNT").isJsonNull() ? object.get("REG_COUNT").getAsString() : "");
                                model.setSEC_ID(object.has("SEC_ID") && !object.get("SEC_ID").isJsonNull() ? object.get("SEC_ID").getAsString() : "");
                                model.setSTATUS(object.has("STATUS") && !object.get("STATUS").isJsonNull() ? object.get("STATUS").getAsString() : "");
                                model.setPURPOSE(object.has("PURPOSE") && !object.get("PURPOSE").isJsonNull() ? object.get("PURPOSE").getAsString() : "");
                                model.setLINE_33KV(object.has("LINE_33KV") && !object.get("LINE_33KV").isJsonNull() ? object.get("LINE_33KV").getAsString() : "");
                                model.setLINE_11KV(object.has("LINE_11KV") && !object.get("LINE_11KV").isJsonNull() ? object.get("LINE_11KV").getAsString() : "");
                                model.setLINE_LT(object.has("LINE_LT") && !object.get("LINE_LT").isJsonNull() ? object.get("LINE_LT").getAsString() : "");
                                model.setDTR(object.has("DTR") && !object.get("DTR").isJsonNull() ? object.get("DTR").getAsString() : "");
                                model.setSS(object.has("SS") && !object.get("SS").isJsonNull() ? object.get("SS").getAsString() : "");
                                if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                    model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                    model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                }


                                model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");

                                if (object.has("FDR_PARTICULARS") && object.get("FDR_PARTICULARS").isJsonArray()) {
                                    JsonArray fdrArray = object.getAsJsonArray("FDR_PARTICULARS");
                                    List<FdrParticularsModel> fdrList = new ArrayList<>();

                                    for (int j = 0; j < fdrArray.size(); j++) {
                                        JsonObject fdrObject = fdrArray.get(j).getAsJsonObject();
                                        FdrParticularsModel fdrModel = new FdrParticularsModel();

                                        fdrModel.setFEEDER(fdrObject.has("FEEDER") && !fdrObject.get("FEEDER").isJsonNull() ? fdrObject.get("FEEDER").getAsString() : null);
                                        // fdrModel.setFEEDER_NAME(fdrObject.has("FEEDER_NAME") && !fdrObject.get("FEEDER_NAME").isJsonNull() ? fdrObject.get("FEEDER_NAME").getAsString() : "");
                                        fdrModel.setVOLTAGE_LEVEL(fdrObject.has("VOLTAGE_LEVEL") && !fdrObject.get("VOLTAGE_LEVEL").isJsonNull() ? fdrObject.get("VOLTAGE_LEVEL").getAsString() : "");
                                        fdrModel.setSURVEY_TYPE(fdrObject.has("SURVEY_TYPE") && !fdrObject.get("SURVEY_TYPE").isJsonNull() ? fdrObject.get("SURVEY_TYPE").getAsString() : "");
                                        fdrModel.setSS_NAME(fdrObject.has("SS_NAME") && !fdrObject.get("SS_NAME").isJsonNull() ? fdrObject.get("SS_NAME").getAsString() : "");
                                        fdrModel.setFDR_INDEX(Integer.parseInt(fdrObject.has("FDR_INDEX") && !fdrObject.get("FDR_INDEX").isJsonNull() ? fdrObject.get("FDR_INDEX").getAsString() : ""));
                                        fdrModel.setFDR_COUNT(Integer.parseInt(fdrObject.has("FDR_COUNT") && !fdrObject.get("FDR_COUNT").isJsonNull() ? fdrObject.get("FDR_COUNT").getAsString() : ""));
                                        fdrModel.setLT_CKT_CODE(fdrObject.has("LT_CKT_CODE") && !fdrObject.get("LT_CKT_CODE").isJsonNull() ? fdrObject.get("LT_CKT_CODE").getAsString() : "");

                                        fdrList.add(fdrModel);
                                    }
                                    model.setFDR_PARTICULARS(fdrList);
                                }

                                list.add(model);


                            }

                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getEST_NO().equals(EST_NO)) {
                                    tempModel = list.get(j);
                                }
                            }

                            if (tempModel.getLINE_LT().equalsIgnoreCase("Y")) {
                                if (tempModel.getLINE_33KV().equalsIgnoreCase("Y") || tempModel.getLINE_11KV().equalsIgnoreCase("Y")) {
                                    Intent intent = new Intent(WorkInvolmentsActivity.this, PrepareEstimate.class);
                                    intent.putExtra("EST_NO", EST_NO);
                                    intent.putExtra("EST_NATURE", EST_NATURE);
                                    intent.putExtra("PURPOSE", PURPOSE);
                                    intent.putExtra("WORK_ORDER", wo_no);
                                    intent.putExtra("GIS_ID", GIS_ID);
                                    intent.putExtra("is33kvSelected", is33KVSelected);
                                    intent.putExtra("is11kvSelected", is11KVSelected);
                                    intent.putExtra("isLtSelected", isLTSelected);
                                    intent.putExtra("isLtSelected", isLTSelected);
                                    intent.putExtra("isMeterSelected", isMeters);
                                    intent.putExtra("MAX_LENGTH", max_length);
                                    intent.putExtra("EST_TYPE", EST_TYPE);
                                    intent.putExtra("FROM", "WORKINVOLVMENTS");
                                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                        intent.putExtra("FEEDERS", FEEDERS);
                                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                                    }
                                    ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);

                                    intent.putExtra("Model", tempModel);
                                    startActivity(intent);
                                } else {
                                    //ONLY LT
                                    Intent intent = new Intent(WorkInvolmentsActivity.this, PrepareEstimate.class);
                                    intent.putExtra("EST_NO", EST_NO);
                                    intent.putExtra("EST_NATURE", EST_NATURE);
                                    intent.putExtra("PURPOSE", PURPOSE);
                                    intent.putExtra("WORK_ORDER", wo_no);
                                    intent.putExtra("GIS_ID", GIS_ID);
                                    intent.putExtra("is33kvSelected", is33KVSelected);
                                    intent.putExtra("is11kvSelected", is11KVSelected);
                                    intent.putExtra("isLtSelected", isLTSelected);
                                    intent.putExtra("isLtSelected", isLTSelected);
                                    intent.putExtra("isMeterSelected", isMeters);
                                    intent.putExtra("MAX_LENGTH", max_length);
                                    intent.putExtra("EST_TYPE", EST_TYPE);
                                    intent.putExtra("FROM", "WORKINVOLVMENTS");
                                    if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                        intent.putExtra("FEEDERS", FEEDERS);
                                        intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                                    }
                                    ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);

                                    intent.putExtra("Model", tempModel);
                                    startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(WorkInvolmentsActivity.this, PrepareEstimate.class);
                                intent.putExtra("EST_NO", EST_NO);
                                intent.putExtra("EST_NATURE", EST_NATURE);
                                intent.putExtra("PURPOSE", PURPOSE);
                                intent.putExtra("WORK_ORDER", wo_no);
                                intent.putExtra("GIS_ID", GIS_ID);
                                intent.putExtra("is33kvSelected", is33KVSelected);
                                intent.putExtra("is11kvSelected", is11KVSelected);
                                intent.putExtra("isLtSelected", isLTSelected);
                                intent.putExtra("isLtSelected", isLTSelected);
                                intent.putExtra("isMeterSelected", isMeters);
                                intent.putExtra("MAX_LENGTH", max_length);
                                intent.putExtra("EST_TYPE", EST_TYPE);
                                intent.putExtra("FROM", "WORKINVOLVMENTS");
                                if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                    intent.putExtra("FEEDERS", FEEDERS);
                                    intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                                }
                                ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);

                                intent.putExtra("Model", tempModel);
                                startActivity(intent);
                            }


                        } else {
                            loader_layout_work.setVisibility(View.GONE);
                            // layout_main_body_work.setVisibility(View.VISIBLE);
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(WorkInvolmentsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(WorkInvolmentsActivity.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //loader.setVisibility(View.GONE);
                Toast.makeText(WorkInvolmentsActivity.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //NEW API CODE
    private void getSingleEstimateWithFdrs() {
        loader_layout_work.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        //dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        // dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
        } else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }
        dtrDetailsReqestVo.addProperty("GIS_ID", GIS_ID);

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.singleEstimateWithFdrs(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout_work.setVisibility(View.GONE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);

                    // Check if the STATUS field exists and is not null
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();

                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            //getLatLongs();

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                PurposeModel model = new PurposeModel();

                                model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                                model.setEST_NATURE(object.has("EST_NATURE") && !object.get("EST_NATURE").isJsonNull() ? object.get("EST_NATURE").getAsString() : "");
                                model.setEST_NO(object.has("EST_NO") && !object.get("EST_NO").isJsonNull() ? object.get("EST_NO").getAsString() : "");
                                model.setREG_COUNT(object.has("REG_COUNT") && !object.get("REG_COUNT").isJsonNull() ? object.get("REG_COUNT").getAsString() : "");
                                model.setSEC_ID(object.has("SEC_ID") && !object.get("SEC_ID").isJsonNull() ? object.get("SEC_ID").getAsString() : "");
                                model.setSTATUS(object.has("STATUS") && !object.get("STATUS").isJsonNull() ? object.get("STATUS").getAsString() : "");
                                model.setPURPOSE(object.has("PURPOSE") && !object.get("PURPOSE").isJsonNull() ? object.get("PURPOSE").getAsString() : "");
                                model.setLINE_33KV(object.has("LINE_33KV") && !object.get("LINE_33KV").isJsonNull() ? object.get("LINE_33KV").getAsString() : "");
                                model.setLINE_11KV(object.has("LINE_11KV") && !object.get("LINE_11KV").isJsonNull() ? object.get("LINE_11KV").getAsString() : "");
                                model.setLINE_LT(object.has("LINE_LT") && !object.get("LINE_LT").isJsonNull() ? object.get("LINE_LT").getAsString() : "");
                                model.setDTR(object.has("DTR") && !object.get("DTR").isJsonNull() ? object.get("DTR").getAsString() : "");
                                model.setSS(object.has("SS") && !object.get("SS").isJsonNull() ? object.get("SS").getAsString() : "");
                                if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                    model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                    model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                }


                                model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");

                                if (object.has("FDR_PARTICULARS") && object.get("FDR_PARTICULARS").isJsonArray()) {
                                    JsonArray fdrArray = object.getAsJsonArray("FDR_PARTICULARS");
                                    List<FdrParticularsModel> fdrList = new ArrayList<>();

                                    for (int j = 0; j < fdrArray.size(); j++) {
                                        JsonObject fdrObject = fdrArray.get(j).getAsJsonObject();
                                        FdrParticularsModel fdrModel = new FdrParticularsModel();

                                        fdrModel.setFEEDER(fdrObject.has("FEEDER") && !fdrObject.get("FEEDER").isJsonNull() ? fdrObject.get("FEEDER").getAsString() : null);
                                        // fdrModel.setFEEDER_NAME(fdrObject.has("FEEDER_NAME") && !fdrObject.get("FEEDER_NAME").isJsonNull() ? fdrObject.get("FEEDER_NAME").getAsString() : "");
                                        fdrModel.setVOLTAGE_LEVEL(fdrObject.has("VOLTAGE_LEVEL") && !fdrObject.get("VOLTAGE_LEVEL").isJsonNull() ? fdrObject.get("VOLTAGE_LEVEL").getAsString() : "");
                                        fdrModel.setSURVEY_TYPE(fdrObject.has("SURVEY_TYPE") && !fdrObject.get("SURVEY_TYPE").isJsonNull() ? fdrObject.get("SURVEY_TYPE").getAsString() : "");
                                        fdrModel.setSS_NAME(fdrObject.has("SS_NAME") && !fdrObject.get("SS_NAME").isJsonNull() ? fdrObject.get("SS_NAME").getAsString() : "");
                                        fdrModel.setFDR_INDEX(Integer.parseInt(fdrObject.has("FDR_INDEX") && !fdrObject.get("FDR_INDEX").isJsonNull() ? fdrObject.get("FDR_INDEX").getAsString() : ""));
                                        fdrModel.setFDR_COUNT(Integer.parseInt(fdrObject.has("FDR_COUNT") && !fdrObject.get("FDR_COUNT").isJsonNull() ? fdrObject.get("FDR_COUNT").getAsString() : ""));
                                        fdrModel.setLATITUDE(fdrObject.has("LATITUDE") && !fdrObject.get("LATITUDE").isJsonNull() ? fdrObject.get("LATITUDE").getAsString() : "");
                                        fdrModel.setLONGITUDE(fdrObject.has("LONGITUDE") && !fdrObject.get("LONGITUDE").isJsonNull() ? fdrObject.get("LONGITUDE").getAsString() : "");
                                        fdrModel.setLT_CKT_CODE(fdrObject.has("LT_CKT_CODE") && !fdrObject.get("LT_CKT_CODE").isJsonNull() ? fdrObject.get("LT_CKT_CODE").getAsString() : null);
                                        fdrList.add(fdrModel);


                                    }

                                    model.setFDR_PARTICULARS(fdrList);
                                }

                                list.add(model);


                            }

                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getEST_NO().equals(EST_NO)) {
                                    tempModel = list.get(j);
                                }
                            }

                            Intent intent = new Intent(WorkInvolmentsActivity.this, PrepareEstimate.class);
                            intent.putExtra("EST_NO", EST_NO);
                            intent.putExtra("EST_NATURE", EST_NATURE);
                            intent.putExtra("PURPOSE", PURPOSE);
                            intent.putExtra("WORK_ORDER", wo_no);
                            intent.putExtra("GIS_ID", GIS_ID);
                            intent.putExtra("is33kvSelected", is33KVSelected);
                            intent.putExtra("is11kvSelected", is11KVSelected);
                            intent.putExtra("isLtSelected", isLTSelected);
                            intent.putExtra("isLtSelected", isLTSelected);
                            intent.putExtra("isMeterSelected", isMeters);
                            intent.putExtra("MAX_LENGTH", max_length);
                            intent.putExtra("FROM", "WORKINVOLVMENTS");
                            if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                intent.putExtra("FEEDERS", FEEDERS);
                                intent.putExtra("RDSS_TYPE", RDSS_TYPE);
                            }
                            intent.putExtra("EST_NATURE", EST_NATURE);
                            intent.putExtra("EST_TYPE", EST_TYPE);
                            ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);

                            intent.putExtra("Model", tempModel);
                            startActivity(intent);

                        } else {
                            loader_layout_work.setVisibility(View.GONE);
                            // layout_main_body_work.setVisibility(View.VISIBLE);
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(WorkInvolmentsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(WorkInvolmentsActivity.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(WorkInvolmentsActivity.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //loader.setVisibility(View.GONE);
                loader_layout_work.setVisibility(View.GONE);
                Toast.makeText(WorkInvolmentsActivity.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getExistingPoleDataNew() {
        loader_layout_work.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Loading existing location data");
        main_layout_involvements.setVisibility(View.GONE);
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("Est_No", EST_NO);
            jsonObject.addProperty("GIS_ID", GIS_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObject.toString();
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getExistingPoleData(jsonObject);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                loader_layout_work.setVisibility(View.GONE);
                main_layout_involvements.setVisibility(View.VISIBLE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    existingPolesList.clear();
                    existingDtrDetails.clear();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonObject dataObject = surveyedResponseVo.getAsJsonObject("DATA");

                            JsonArray dtr_dtls = dataObject.getAsJsonArray("DTR_DTLS");
                            if (dtr_dtls != null) {
                                for (int i = 0; i < dtr_dtls.size(); i++) {
                                    JsonObject object = dtr_dtls.get(i).getAsJsonObject();
                                    NewModel dtr_model = new NewModel();
                                    dtr_model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                                    dtr_model.setDTR_STRUCTURE_CODE(object.has("DTR_CODE") && !object.get("DTR_CODE").isJsonNull() ? object.get("DTR_CODE").getAsString() : "");
                                    dtr_model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                                    dtr_model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull() ? object.get("POLE_CONFIG").getAsString() : "");
                                    dtr_model.setLATITUDE(object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : "");
                                    dtr_model.setLONGITUDE(object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : "");
                                    existingDtrDetails.add(dtr_model);
                                }
                            }


                            JsonArray array33kv = dataObject.getAsJsonArray("33KV");
                            if (array33kv != null) {
                                for (int i = 0; i < array33kv.size(); i++) {
                                    JsonObject object = array33kv.get(i).getAsJsonObject();
                                    NewModel model = new NewModel();

                                    model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                                    model.setREF_ID(object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : "");
                                    model.setVOLTAGE_LEVEL("33KV");
                                    model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                                    model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                                    model.setLINE_TYPE(object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : "");
                                    model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull() ? object.get("POLE_CONFIG").getAsString() : "");
                                    model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                    model.setPOLE_HEIGHT(object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : "");
                                    model.setPOLE_SUPPORT(object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : "");
                                    model.setPIN_CUT(object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : "");
                                    model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                                    model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));
                                    model.setPOLE_STRUCTURE((object.has("POLE_STRUCTURE") && !object.get("POLE_STRUCTURE").isJsonNull() ? object.get("POLE_STRUCTURE").getAsString() : ""));
                                    model.setPOLE_CONCRETE((object.has("POLE_CONCRETE") && !object.get("POLE_CONCRETE").isJsonNull() ? object.get("POLE_CONCRETE").getAsString() : ""));
                                    model.setHTSC_NO((object.has("HT_SCNO") && !object.get("HT_SCNO").isJsonNull() ? object.get("HT_SCNO").getAsString() : ""));
                                    model.setIS_EXISTING_LOCATION_NEW("Existing");
                                    existingPolesList.add(model);


                                }

                            }

                            JsonArray array11kv = dataObject.getAsJsonArray("11KV");
                            if (array11kv != null) {
                                for (int i = 0; i < array11kv.size(); i++) {
                                    JsonObject object = array11kv.get(i).getAsJsonObject();
                                    NewModel model = new NewModel();

                                    model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                                    model.setREF_ID(object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : "");
                                    model.setVOLTAGE_LEVEL(object.has("VOLTAGE_LEVEL") && !object.get("VOLTAGE_LEVEL").isJsonNull() ? object.get("VOLTAGE_LEVEL").getAsString() : "");
                                    //model.setVOLTAGE_LEVEL("11KV");
                                    model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                                    model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                                    model.setLINE_TYPE(object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : "");
                                    model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull() ? object.get("POLE_CONFIG").getAsString() : "");
                                    model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                    model.setPOLE_HEIGHT(object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : "");
                                    model.setPOLE_SUPPORT(object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : "");
                                    model.setPIN_CUT(object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : "");
                                    model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                                    model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));
                                    model.setPOLE_STRUCTURE((object.has("POLE_STRUCTURE") && !object.get("POLE_STRUCTURE").isJsonNull() ? object.get("POLE_STRUCTURE").getAsString() : ""));
                                    model.setPOLE_CONCRETE((object.has("POLE_CONCRETE") && !object.get("POLE_CONCRETE").isJsonNull() ? object.get("POLE_CONCRETE").getAsString() : ""));
                                    model.setDTR_STRUCTURE_CODE((object.has("DTR_CODE") && !object.get("DTR_CODE").isJsonNull() ? object.get("DTR_CODE").getAsString() : ""));
                                    model.setHTSC_NO((object.has("HT_SCNO") && !object.get("HT_SCNO").isJsonNull() ? object.get("HT_SCNO").getAsString() : ""));
                                    model.setLT_AVAILABLE((object.has("IS_LTCKT_AVAILABLE") && !object.get("IS_LTCKT_AVAILABLE").isJsonNull() ? object.get("IS_LTCKT_AVAILABLE").getAsString() : ""));
                                    model.setDTR_PHASE((object.has("DTR_PHASE") && !object.get("DTR_PHASE").isJsonNull() ? object.get("DTR_PHASE").getAsString() : ""));
                                    model.setDTR_CAPACITY((object.has("DTR_CAPACITY") && !object.get("DTR_CAPACITY").isJsonNull() ? object.get("DTR_CAPACITY").getAsString() : ""));
                                    model.setDTR_SNO((object.has("DTR_SNO") && !object.get("DTR_SNO").isJsonNull() ? object.get("DTR_SNO").getAsString() : ""));
                                    model.setDTR_MAKE((object.has("DTR_MAKE") && !object.get("DTR_MAKE").isJsonNull() ? object.get("DTR_MAKE").getAsString() : ""));
                                    model.setDTR_LOCATION((object.has("DTR_LOC_NAME") && !object.get("DTR_LOC_NAME").isJsonNull() ? object.get("DTR_LOC_NAME").getAsString() : ""));
                                    model.setCOUNT_CKT((object.has("CKT_NO") && !object.get("CKT_NO").isJsonNull() && !object.get("CKT_NO").getAsString().isEmpty()) ? Integer.parseInt(object.get("CKT_NO").getAsString()) : 0
                                    );


                                    model.setIS_EXISTING_LOCATION_NEW("Existing");

                                    existingPolesList.add(model);
                                }

                            }

                            JsonArray arrayLT = dataObject.getAsJsonArray("LT");
                            if (arrayLT != null) {
                                for (int i = 0; i < arrayLT.size(); i++) {
                                    JsonObject object = arrayLT.get(i).getAsJsonObject();
                                    NewModel model = new NewModel();

                                    model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                                    model.setREF_ID(object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : "");
                                    model.setVOLTAGE_LEVEL("LT");
                                    model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                                    model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                                    model.setLINE_TYPE(object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : "");
                                    model.setPOLE_CONFIG(object.has("LINE_CONFIG") && !object.get("LINE_CONFIG").isJsonNull() ? object.get("LINE_CONFIG").getAsString() : "");
                                    model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                    model.setPOLE_HEIGHT(object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : "");
                                    model.setPOLE_SUPPORT(object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : "");
                                    model.setPIN_CUT(object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : "");
                                    model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                                    model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));
                                    model.setDTR_STRUCTURE_CODE((object.has("DTR_STRUCTURE") && !object.get("DTR_STRUCTURE").isJsonNull() ? object.get("DTR_STRUCTURE").getAsString() : ""));
                                    model.setLT_AVAILABLE((object.has("LT_CKT_CODE") && !object.get("LT_CKT_CODE").isJsonNull() ? object.get("LT_CKT_CODE").getAsString() : ""));
                                    model.setIS_EXISTING_LOCATION_NEW("Existing");

                                    existingPolesList.add(model);
                                }


                            }


                        }
                    } else {
                        if (!response.message().isEmpty()) {
                            Toast.makeText(WorkInvolmentsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                        loader_layout_work.setVisibility(View.GONE);
                    }
                } else {
                    main_layout_involvements.setVisibility(View.VISIBLE);
                    loader_layout_work.setVisibility(View.GONE);
                    Toast.makeText(WorkInvolmentsActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.hide();
                main_layout_involvements.setVisibility(View.VISIBLE);

            }
        });


    }



    private void getExistingPoleDataNewForLT(NewModel model, String DTR_CODE, String selectedCKT) {
        // progressDialog.show();
        loader_layout_work.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("DTR_CODE", DTR_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonString = jsonObject.toString();
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getExistingPoleDataForLT(jsonObject);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // progressDialog.hide();
                loader_layout_work.setVisibility(View.GONE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonObject dataObject = surveyedResponseVo.getAsJsonObject("DATA");

                            JsonArray array33kv = dataObject.getAsJsonArray("33KV");
                            if (array33kv != null) {
                                for (int i = 0; i < array33kv.size(); i++) {
                                    JsonObject object = array33kv.get(i).getAsJsonObject();
                                    NewModel model = new NewModel();

                                    model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                                    model.setREF_ID(object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : "");
                                    model.setVOLTAGE_LEVEL("33KV");
                                    model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                                    model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                                    model.setLINE_TYPE(object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : "");
                                    model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull() ? object.get("POLE_CONFIG").getAsString() : "");
                                    model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                    model.setPOLE_HEIGHT(object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : "");
                                    model.setPOLE_SUPPORT(object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : "");
                                    model.setPIN_CUT(object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : "");
                                    model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                                    model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));
                                    model.setPOLE_STRUCTURE((object.has("POLE_STRUCTURE") && !object.get("POLE_STRUCTURE").isJsonNull() ? object.get("POLE_STRUCTURE").getAsString() : ""));
                                    model.setPOLE_CONCRETE((object.has("POLE_CONCRETE") && !object.get("POLE_CONCRETE").isJsonNull() ? object.get("POLE_CONCRETE").getAsString() : ""));
                                    model.setIS_EXISTING_LOCATION_NEW("Existing");
                                    existingPolesList.add(model);


                                }

                            }

                            JsonArray array11kv = dataObject.getAsJsonArray("11KV");
                            if (array11kv != null) {
                                for (int i = 0; i < array11kv.size(); i++) {
                                    JsonObject object = array11kv.get(i).getAsJsonObject();
                                    NewModel model = new NewModel();

                                    model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                                    model.setREF_ID(object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : "");
                                    model.setVOLTAGE_LEVEL("11KV");
                                    model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                                    model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                                    model.setLINE_TYPE(object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : "");
                                    model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull() ? object.get("POLE_CONFIG").getAsString() : "");
                                    model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                    model.setPOLE_HEIGHT(object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : "");
                                    model.setPOLE_SUPPORT(object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : "");
                                    model.setPIN_CUT(object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : "");
                                    model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                                    model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));
                                    model.setPOLE_STRUCTURE((object.has("POLE_STRUCTURE") && !object.get("POLE_STRUCTURE").isJsonNull() ? object.get("POLE_STRUCTURE").getAsString() : ""));
                                    model.setPOLE_CONCRETE((object.has("POLE_CONCRETE") && !object.get("POLE_CONCRETE").isJsonNull() ? object.get("POLE_CONCRETE").getAsString() : ""));


                                    model.setIS_EXISTING_LOCATION_NEW("Existing");

                                    existingPolesList.add(model);
                                }

                            }


                            JsonArray arrayLT = dataObject.getAsJsonArray("LT");
                            if (arrayLT != null) {
                                for (int i = 0; i < arrayLT.size(); i++) {
                                    JsonObject object = arrayLT.get(i).getAsJsonObject();
                                    NewModel model = new NewModel();

                                    model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                                    model.setREF_ID(object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : "");
                                    model.setVOLTAGE_LEVEL("LT");
                                    model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                                    model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                                    model.setLINE_TYPE(object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : "");
                                    model.setPOLE_CONFIG(object.has("LINE_CONFIG") && !object.get("LINE_CONFIG").isJsonNull() ? object.get("LINE_CONFIG").getAsString() : "");
                                    model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                    model.setPOLE_HEIGHT(object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : "");
                                    model.setPOLE_SUPPORT(object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : "");
                                    model.setPIN_CUT(object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : "");
                                    model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                                    model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));
                                    model.setDTR_STRUCTURE_CODE((object.has("DTR_STRUCTURE") && !object.get("DTR_STRUCTURE").isJsonNull() ? object.get("DTR_STRUCTURE").getAsString() : ""));
                                    model.setLT_AVAILABLE((object.has("LT_CKT_CODE") && !object.get("LT_CKT_CODE").isJsonNull() ? object.get("LT_CKT_CODE").getAsString() : ""));
                                    model.setIS_EXISTING_LOCATION_NEW("Existing");
                                    existingPolesList.add(model);
                                }

                            }
                            IS_LT_SELECTED = true;
                            CURRENT_SURVEY_TYPE = "LT";
                            SELECTED_DTR = model.getDTR_STRUCTURE_CODE();
                            SELECTED_DTR_LOCATION = model.getDTR_LOCATION();
                            SELECTED_LT_CKT = selectedCKT;
                            int polesCount = getPolesCount(SELECTED_LT_CKT);
                            if (polesCount == 0) {
                                IS_LT_FIRST_POLE = true;
                            } else {
                                IS_LT_FIRST_POLE = false;
                            }
                            for (int i = 0; i < existingPolesList.size(); i++) {
                                if (selectedCKT.equals(existingPolesList.get(i).getLT_AVAILABLE())) {
                                    IS_LT_EXISTING_SURVEY = true;
                                    LT_FIRST_POLE_EXISTING = false;
                                    break;
                                } else {
                                    IS_LT_EXISTING_SURVEY = true;
                                    LT_FIRST_POLE_EXISTING = true;
                                }
                            }
                            navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");


                        } else {
                            //NEW CHANGES BY CHANDU v3.7
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                                IS_LT_SELECTED = true;
                                CURRENT_SURVEY_TYPE = "LT";
                                SELECTED_DTR = model.getDTR_STRUCTURE_CODE();
                                SELECTED_DTR_LOCATION = model.getDTR_LOCATION();
                                SELECTED_LT_CKT = selectedCKT;

                                int polesCount = getPolesCount(SELECTED_LT_CKT);
                                if (polesCount == 0) {
                                    IS_LT_FIRST_POLE = true;
                                } else {
                                    IS_LT_FIRST_POLE = false;
                                }
                                IS_LT_EXISTING_SURVEY = false;
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");

                            } else {

                                IS_LT_SELECTED = true;
                                CURRENT_SURVEY_TYPE = "LT";
                                SELECTED_DTR = model.getDTR_STRUCTURE_CODE();
                                SELECTED_DTR_LOCATION = model.getDTR_LOCATION();
                                SELECTED_LT_CKT = selectedCKT;

                                int polesCount = getPolesCount(SELECTED_LT_CKT);
                                if (polesCount == 0) {
                                    IS_LT_FIRST_POLE = true;
                                } else {
                                    IS_LT_FIRST_POLE = false;
                                }
                                IS_LT_EXISTING_SURVEY = true;
                                LT_FIRST_POLE_EXISTING = true;
//                                IS_LT_EXISTING_SURVEY = false;
//                                LT_POLE_SURVEY_TYPE = true;
                                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");

                            }
                        }
                    } else {
                        if (!response.message().isEmpty()) {
                            Toast.makeText(WorkInvolmentsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                        loader_layout_work.setVisibility(View.GONE);
                        //progressDialog.hide();
                    }
                } else {
                    Toast.makeText(WorkInvolmentsActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //progressDialog.hide();
                loader_layout_work.setVisibility(View.GONE);

            }
        });


    }

    //
    private void openDialogForSuccessMsg(String msg) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle("LT not surveyed");
        builder.setCancelable(false);
        builder.setPositiveButton("Close", (dialog, which) -> {
            dialog.dismiss();

        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showAlertDialogForSurveyApprove(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {

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

        // Title
        title.setText("Please pay attention!");

        // 🔴 HTML formatted warning
        String warningMessage =
                "<b><font color='#FF0000'>Once the survey is approved, GIS Survey can't be modified and it will be final.</font></b>";

        message.setText(HtmlCompat.fromHtml(warningMessage, HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Next");
        cancelBtn.setText("Cancel");

        // NEXT Click
        okBtn.setOnClickListener(v -> {
            showAlertDialogForSurveyApprove2(is33kvSelected, is11kvSelected, isLtSelected);
            dialog.dismiss();
        });

        // CANCEL Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);

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
//    private void showAlertDialogForSurveyApprove(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Please pay attention!");
//
//        String warningMessage = "<b><font color='#FF0000'>Once the survey is approved , GIS Survey can't be modified and it will be final.</font></b>";
//
//        builder.setMessage(Html.fromHtml(warningMessage));
//        builder.setPositiveButton("Next", (dialog, which) -> {
//            showAlertDialogForSurveyApprove2(is33KVSelected, is11KVSelected, isLTSelected);
//            dialog.cancel();
//
//
//        });
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            dialog.cancel();
//
//        });
//
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void showAlertDialogForSurveyApprove2(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {

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

        // Title & Message
        title.setText("Warning");
        message.setText("An estimate will be prepared in SAP with the GIS data. Do you want to proceed?");

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Confirm");
        cancelBtn.setText("Cancel");

        // CONFIRM Click
        okBtn.setOnClickListener(v -> {
            showAlertDialogForSurveyApprove3(is33kvSelected, is11kvSelected, isLtSelected);
            dialog.dismiss();
        });

        // CANCEL Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);

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
//    private void showAlertDialogForSurveyApprove2(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("Warning");
//        builder.setMessage("An estimate will be prepared in SAP with the GIS data. Do you want to proceed?");
//        builder.setPositiveButton("Confirm", (dialog, which) -> {
//            showAlertDialogForSurveyApprove3(is33KVSelected, is11KVSelected, isLTSelected);
//            dialog.dismiss();
//
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            dialog.cancel();
//
//        });
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void showAlertDialogForSurveyApprove3(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {

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

        // 🔴 Title with red color (HTML)
        String warningTitle = "<font color='#FF0000'><b>Warning</b></font>";
        title.setText(HtmlCompat.fromHtml(warningTitle, HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Message
        message.setText("You can no longer modify the GIS survey, and any pending data has also been transmitted.");

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Yes");
        cancelBtn.setText("Cancel");

        // YES Click
        okBtn.setOnClickListener(v -> {
            transmitImageData(false);
            dialog.dismiss();
        });

        // CANCEL Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);

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
//    private void showAlertDialogForSurveyApprove3(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        String warningMessage = "<b><font color='#FF0000'>Warning</font></b>";
//        builder.setTitle(Html.fromHtml(warningMessage));
//        builder.setMessage("You can no longer modify the GIS survey, and any pending data has also been transmitted.");
//        builder.setPositiveButton("Yes", (dialog, which) -> {
//            transmitImageData(false);
//            dialog.dismiss();
//
//        });
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            dialog.cancel();
//
//        });
//
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void showAlertDialog(boolean is33kvSelected, boolean is11kvSelected,
                                 boolean isLtSelected, int fdrIndexPos, String feederName) {

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

        // Title & Message
        title.setText("Alert");
        message.setText("33 kV survey completed. Do you want to re-survey?");

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Yes");
        cancelBtn.setText("Close");

        // YES Click
        okBtn.setOnClickListener(v -> {
            re_survey33kV(false, is33kvSelected, is11kvSelected, isLtSelected, "33KV", "");
            IS_33_KV_SELECTED = true;
            CURRENT_SURVEY_TYPE = "33KV";
            dialog.dismiss();
        });

        // CLOSE Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);

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
//    private void showAlertDialog(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected, int fdrIndexPos, String feederName) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("Alert");
//        builder.setMessage("33 kV Survey Completed Do you want to re-survey");
//        builder.setPositiveButton("Yes", (dialog, which) -> {
//            re_survey33kV(false, is33kvSelected, is11kvSelected, isLtSelected, "33KV", "");
//            IS_33_KV_SELECTED = true;
//            CURRENT_SURVEY_TYPE = "33KV";
//            dialog.dismiss();
//
//        });
//
//        builder.setNegativeButton("Close", (dialog, which) -> {
//            dialog.cancel();
//
//        });
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void show11kVResurveyAlertDialog(boolean is33kvSelected, boolean is11kvSelected,
                                             boolean isLtSelected, int fdrIndexPos, String feederName) {

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

        // Title & Message
        title.setText("Alert");
        message.setText("11 kV survey completed. Do you want to re-survey?");

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Yes");
        cancelBtn.setText("Close");

        // YES Click
        okBtn.setOnClickListener(v -> {
            re_survey11kV(false, is33kvSelected, is11kvSelected, isLtSelected,
                    "11KV", fdrIndexPos, feederName);
            dialog.dismiss();
        });

        // CLOSE Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);

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
//    private void show11kVResurveyAlertDialog(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected, int fdrIndexPos, String feederName) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("Alert");
//        builder.setMessage("11 kV Survey Completed Do you want to re-survey");
//        builder.setPositiveButton("Yes", (dialog, which) -> {
//            re_survey11kV(false, is33kvSelected, is11kvSelected, isLtSelected, "11KV", fdrIndexPos, feederName);
//            dialog.dismiss();
//
//
//        });
//
//        builder.setNegativeButton("Close", (dialog, which) -> {
//            dialog.cancel();
//
//
//        });
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void showLTResurveyAlertDialog(boolean is33kvSelected, boolean is11kvSelected,
                                           boolean isLtSelected, int fdrIndexPos, String feederName) {

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

        // Title & Message
        title.setText("Alert");
        message.setText("LT survey completed. Do you want to re-survey?");

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Yes");
        cancelBtn.setText("Close");

        // YES Click
        okBtn.setOnClickListener(v -> {
            IS_LT_SELECTED = true;
            CURRENT_SURVEY_TYPE = "LT";
            re_surveyLT(false, is33kvSelected, is11kvSelected, isLtSelected, "LT", "");
            dialog.dismiss();
        });

        // CLOSE Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);

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
//    private void showLTResurveyAlertDialog(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected, int fdrIndexPos, String feederName) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("Alert");
//        builder.setMessage("LT Survey Completed Do you want to re-survey");
//        builder.setPositiveButton("Yes", (dialog, which) -> {
//            IS_LT_SELECTED = true;
//            CURRENT_SURVEY_TYPE = "LT";
//            re_surveyLT(false, is33kvSelected, is11kvSelected, isLtSelected, "LT", "");
//            dialog.dismiss();
//        });
//        builder.setNegativeButton("Close", (dialog, which) -> {
//            dialog.cancel();
//        });
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }


    private void navigateNextScreen(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected, boolean isMeterSelected, String btn_value, int fdrIndexPos, String feederName) {
        Intent intent = new Intent(this, EstimatesMap.class);
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("33KV", is33kvSelected);
        intent.putExtra("11KV", is11kvSelected);
        intent.putExtra("DTR", isDTRSelected);
        intent.putExtra("LT", isLtSelected);
        intent.putExtra("METERS", isMeters);
        intent.putExtra("MAX_LENGTH", max_length);
        intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
        intent.putExtra("FROM", "WI");
        intent.putExtra("REG_COUNT", REG_COUNT);
        intent.putExtra("EST_TYPE", EST_TYPE);
        intent.putExtra("BTN_VALUE", btn_value);
        intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
        intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(fdrIndexPos));
        intent.putExtra("FEEDER_NAME", feederName);
        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            intent.putExtra("FEEDERS", FEEDERS);
            intent.putExtra("RDSS_TYPE", RDSS_TYPE);
        }
        intent.putExtra("Model", model);
        intent.putExtra("IS_EDIT", false);

        startActivity(intent);
    }


    private void navigateDashboardScreen(boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {
        Intent intent = new Intent(this, EstimatesDashboardActivity.class);
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("REG_COUNT", REG_COUNT);
        intent.putExtra("33KV", is33kvSelected);
        intent.putExtra("11KV", is11kvSelected);
        intent.putExtra("DTR", isDTRSelected);
        intent.putExtra("LT", isLtSelected);
        intent.putExtra("FROM", "ApproveSurvey");
        startActivity(intent);
    }


    private void re_survey33kV(boolean isCompleted33kVSurvey, boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected, String btn_value, String feederName) {
        Intent intent = new Intent(this, EstimatesMap.class);
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("33KV", is33kvSelected);
        intent.putExtra("11KV", is11kvSelected);
        intent.putExtra("DTR", isDTRSelected);
        intent.putExtra("LT", isLtSelected);
        intent.putExtra("FROM", "33kvResurvey");
        intent.putExtra("MAX_LENGTH", max_length);
        intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
        intent.putExtra("BTN_VALUE", btn_value);
        intent.putExtra("EST_TYPE", EST_TYPE);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(1));
        intent.putExtra("FEEDER_NAME", feederName);
        intent.putExtra("IS33KVCOMPLETED", isCompleted33kVSurvey);
        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            intent.putExtra("FEEDERS", FEEDERS);
            intent.putExtra("RDSS_TYPE", RDSS_TYPE);
        }
        intent.putExtra("Model", model);
        intent.putExtra("IS_EDIT", false);
        startActivity(intent);
    }

    private void re_survey11kV(boolean isCompleted11kVSurvey, boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected, String btn_value, int fdrIndexPos, String feederName) {
        Intent intent = new Intent(this, EstimatesMap.class);
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("33KV", is33kvSelected);
        intent.putExtra("11KV", is11kvSelected);
        intent.putExtra("DTR", isDTRSelected);
        intent.putExtra("LT", isLtSelected);
        intent.putExtra("FROM", "11kvResurvey");
        intent.putExtra("MAX_LENGTH", max_length);
        intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
        intent.putExtra("BTN_VALUE", btn_value);
        intent.putExtra("EST_TYPE", EST_TYPE);
        intent.putExtra("Feeder_type33KV", Feeder_type33KV);
        intent.putExtra("Feeder_type11KV", Feeder_type11KV);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(fdrIndexPos));
        intent.putExtra("FEEDER_NAME", feederName);
        intent.putExtra("IS11KVCOMPLETED", isCompleted11kVSurvey);
        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            intent.putExtra("FEEDERS", FEEDERS);
            intent.putExtra("RDSS_TYPE", RDSS_TYPE);
        }
        intent.putExtra("Model", model);
        intent.putExtra("IS_EDIT", false);
        startActivity(intent);
    }

    private void re_surveyLT(boolean isCompletedLTSurvey, boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected, String btn_value, String feederName) {
        Intent intent = new Intent(this, EstimatesMap.class);
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("33KV", is33kvSelected);
        intent.putExtra("11KV", is11kvSelected);
        intent.putExtra("DTR", isDTRSelected);
        intent.putExtra("LT", isLtSelected);
        intent.putExtra("EST_TYPE", EST_TYPE);
        intent.putExtra("FROM", "LTResurvey");
        intent.putExtra("MAX_LENGTH", max_length);
        intent.putExtra("TOWER_LENGTH", WorkInvolmentsActivity.TOWER_LENGTH);
        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            intent.putExtra("FEEDERS", FEEDERS);
            intent.putExtra("RDSS_TYPE", RDSS_TYPE);
        }
        intent.putExtra("BTN_VALUE", btn_value);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(1));
        intent.putExtra("FEEDER_NAME", feederName);
        intent.putExtra("ISLTCOMPLETED", isCompletedLTSurvey);
        intent.putExtra("Model", model);
        intent.putExtra("IS_EDIT", false);
        startActivity(intent);
    }

    private void transmitImageData(boolean isExistingData) {
        //progressDialog.show();
        loader_layout_work.setVisibility(View.VISIBLE);
        ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true, GIS_ID);
        for (NewModel model : polesData) {
            model.setGIS_ID(GIS_ID);
            model.setIMEI_NO("");

        }

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(polesData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
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
                // progressDialog.cancel();
                loader_layout_work.setVisibility(View.GONE);

                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        if (isExistingData) {
                            showAlertDialogForSurveyApprove(is33KVSelected, is11KVSelected, isLTSelected);

                        } else {
                            approveSurvey(GIS_ID, 3);
                        }


                        Toast.makeText(WorkInvolmentsActivity.this, "Data Transmitted Successfully..", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(WorkInvolmentsActivity.this, EstimatesDashboardActivity.class);
//                        startActivity(intent);


                        boolean surveyStatusNEW = databaseEstimates.deleteSurveyStatusNEW(EST_NO, GIS_ID);
                        boolean newSurveyData = databaseEstimates.deleteNewSurveyData(EST_NO, GIS_ID);
                        if (surveyStatusNEW && newSurveyData) {

                            onBackPressed();
                        } else {
                            Toast.makeText(WorkInvolmentsActivity.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout_work.setVisibility(View.GONE);

            }
        });

    }


    private void approveSurvey(String GIS_ID, int STATUS) {
        //progressDialog.show();
        loader_layout_work.setVisibility(View.VISIBLE);
        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            entry.addProperty("EST_NO", EST_NO);
        }
        entry.addProperty("STATUS", STATUS);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(entry);

        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        // Log.v("DATA===>>", "" + jsonArray);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.approveSurvey(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout_work.setVisibility(View.GONE);

                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {

                        // Toast.makeText(WorkInvolmentsActivity.this, "Data Transmitted Successfully..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WorkInvolmentsActivity.this, EstimatesDashboardActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(WorkInvolmentsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout_work.setVisibility(View.GONE);
            }
        });

    }

    //LT with 11KV
    @Override
    public void onItemClick(NewModel model, String selectedCKT) {
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES") || LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            if (model.getDTR_SURVEY_TYPE().equalsIgnoreCase("Existing")
                    || model.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED")) {
                if (ConnectivityChecking.checkConnection(WorkInvolmentsActivity.this)) {
                    getExistingPoleDataNewForLT(model, model.getDTR_STRUCTURE_CODE(), selectedCKT);

                } else {
                    CommanUtilits.internetDialog(WorkInvolmentsActivity.this);
                }
                //getExistingPoleDataNewForLT(model, model.getDTR_STRUCTURE_CODE(), selectedCKT);
            } else {
                IS_LT_SELECTED = true;
                CURRENT_SURVEY_TYPE = "LT";
                SELECTED_DTR = model.getDTR_STRUCTURE_CODE();
                SELECTED_DTR_LOCATION = model.getDTR_LOCATION();
                SELECTED_LT_CKT = selectedCKT;
                SELECTED_DTR_ID = model.getDTR_ID();

                IS_LT_FIRST_POLE = false;
                IS_LT_EXISTING_SURVEY = false;

                //ONLY_LT_WITH_PROPOSED = true;
                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");
            }
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER") || LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            if (ConnectivityChecking.checkConnection(WorkInvolmentsActivity.this)) {
                getExistingPoleDataNewForLT(model, model.getDTR_STRUCTURE_CODE(), selectedCKT);

            } else {
                CommanUtilits.internetDialog(WorkInvolmentsActivity.this);
            }
            //  getExistingPoleDataNewForLT(model, model.getDTR_STRUCTURE_CODE(), selectedCKT);
        } else {
            //
        }
    }

    //LT with 11KV
    @Override
    public void onEditItemClick(NewModel model, String selectedCKT) {
        if (!selectedCKT.isEmpty()) {
            Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("FEEDER_ID", model.getFEEDER_ID());
            intent.putExtra("FEEDER_NAME", model.getFEEDER_NAME());
            intent.putExtra("FEEDER_NO", "");
            intent.putExtra("FROM", "LT");
            intent.putExtra("SELECTED_LT_CKT", selectedCKT);

            //FOR MAP
            intent.putExtra("EST_NATURE", EST_NATURE);
            intent.putExtra("PURPOSE", PURPOSE);
            intent.putExtra("WORK_ORDER", wo_no);
            intent.putExtra("33KV", is33KVSelected);
            intent.putExtra("11KV", is11KVSelected);
            intent.putExtra("DTR", isDTRSelected);
            intent.putExtra("LT", isLTSelected);
            intent.putExtra("METERS", isMeters);
            intent.putExtra("MAX_LENGTH", max_length);
            intent.putExtra("REG_COUNT", REG_COUNT);
            intent.putExtra("BTN_VALUE", "LT");
            intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
            intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
            intent.putExtra("FDR_INDEX_POS", String.valueOf(1));
            intent.putExtra("SELECTED_DTR_NAME", WorkInvolmentsActivity.SELECTED_DTR_LOCATION);

            startActivity(intent);
        } else {
        }
    }


    //ONLY LT
    @Override
    public void onItemClick(FdrParticularsModel model) {
        IS_LT_SELECTED = true;
        CURRENT_SURVEY_TYPE = "LT";
        SELECTED_DTR = model.getFEEDER().split("~")[0];
        SELECTED_DTR_LOCATION = model.getFEEDER().split("~")[1];
        SELECTED_LT_CKT = model.getLT_CKT_CODE();

        int polesCount = getPolesCount(SELECTED_LT_CKT);
        if (polesCount == 0) {
            IS_LT_FIRST_POLE = true;
        } else {
            IS_LT_FIRST_POLE = false;
        }
        if (model.getSURVEY_TYPE().equalsIgnoreCase("PROPOSED")) {
            IS_LT_EXISTING_SURVEY = false;
        } else {
            IS_LT_EXISTING_SURVEY = true;
        }


        int LT_CKT_POLE_COUNT = 0;


        if (!existingPolesList.isEmpty()) {
            for (NewModel pole : existingPolesList) {
                String ltAvailable = pole.getLT_AVAILABLE();
                if (ltAvailable != null && !ltAvailable.isEmpty() && ltAvailable.equals(SELECTED_LT_CKT)) {
                    LT_CKT_POLE_COUNT++;
                }
            }
        }

        if (model.getLT_CKT_CODE() != null) {
            if (LT_CKT_POLE_COUNT > 0) {

                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");
            } else {
                //Temparoty solution on 26-06-2025
                IS_LT_EXISTING_SURVEY = false;
                ONLY_LT_WITH_PROPOSED = true;
                navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");
                // openDialogForSuccessMsg("The " + SELECTED_LT_CKT + " Circuit has not been surveyed.");
            }
        } else {
            openDialogForSuccessMsg("LT_CKT_CODE is null please contact with GIS Team", GIS_ID, EST_NO, "");
            // openAlertDialog("LT_CKT_CODE is null please contact with GIS Team");
        }


    }

    //ONLY LT
    @Override
    public void onEditItemClick(FdrParticularsModel model) {
        Intent intent = new Intent(WorkInvolmentsActivity.this, Edit33Home.class);
        intent.putExtra("EST_NO", EST_NO);
        intent.putExtra("GIS_ID", GIS_ID);
        intent.putExtra("FEEDER_ID", model.getFEEDER().split("~")[0]);
        intent.putExtra("FEEDER_NAME", model.getFEEDER().split("~")[1]);
        intent.putExtra("FEEDER_NO", model.getFDR_INDEX());
        intent.putExtra("FROM", "LT");
        intent.putExtra("SELECTED_LT_CKT", model.getLT_CKT_CODE());
        intent.putExtra("SELECTED_DTR_LOCATION", model.getFEEDER().split("~")[1]);


        //FOR MAP
        intent.putExtra("EST_NATURE", EST_NATURE);
        intent.putExtra("PURPOSE", PURPOSE);
        intent.putExtra("WORK_ORDER", wo_no);
        intent.putExtra("33KV", is33KVSelected);
        intent.putExtra("11KV", is11KVSelected);
        intent.putExtra("DTR", isDTRSelected);
        intent.putExtra("LT", isLTSelected);
        intent.putExtra("METERS", isMeters);
        intent.putExtra("MAX_LENGTH", max_length);
        intent.putExtra("REG_COUNT", REG_COUNT);
        intent.putExtra("BTN_VALUE", "LT");
        intent.putExtra("Feeder_type33KV", feeder33kVSurveyType);
        intent.putExtra("Feeder_type11KV", feeder11kVSurveyType);
        intent.putExtra("FDR_INDEX_POS", String.valueOf(1));
        // intent.putExtra("Model", model);

        startActivity(intent);
    }

    private int getPolesCount(String ltCktCode) {
        int totalPolesLT = 0;
        ArrayList<NewModel> allPoles = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);


        for (int i = 0; i < allPoles.size(); i++) {
            if (allPoles.get(i).getLT_AVAILABLE().equalsIgnoreCase(ltCktCode)) {
                totalPolesLT++;
            }
        }

        return totalPolesLT;
    }


    private void openDialogForSuccessMsg(String msg, String GIS_ID, String EST_NO, String WORK_ORDER) {

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

        // Title
        title.setText("Details");

        // 🔹 Build GIS / EST / WORK ORDER block
        StringBuilder infoBuilder = new StringBuilder();

        if (GIS_ID != null && !GIS_ID.isEmpty()) {
            infoBuilder.append("<b>GIS ID:</b> ").append(GIS_ID).append("<br>");
        }

        if (EST_NO != null && !EST_NO.isEmpty()) {
            infoBuilder.append("<b>EST No:</b> ").append(EST_NO).append("<br>");
        }

        if (WORK_ORDER != null && !WORK_ORDER.isEmpty()) {
            infoBuilder.append("<b>Work Order:</b> ").append(WORK_ORDER).append("<br><br>");
        }

        // 🔹 Message Handling
        String finalMessage;

        if (msg != null && msg.contains("newly  proposed")) {
            String formattedMsg =
                    "<b><font color='red'>Invalid Survey</font></b><br><br>" + msg;
            finalMessage = infoBuilder.toString() + formattedMsg;
        } else {
            finalMessage = infoBuilder.toString() + msg;
        }

        message.setText(HtmlCompat.fromHtml(finalMessage, HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        okBtn.setText("Close");

        // Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            onBackPressed();
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


}