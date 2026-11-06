package com.apepdcl.gis_survey.estimates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.models.AllDtsWoRdss;
import com.apepdcl.gis_survey.estimates.models.ConductorTypeModel;
import com.apepdcl.gis_survey.estimates.models.DTRCapacities;
import com.apepdcl.gis_survey.estimates.models.DTRFeederMaster;
import com.apepdcl.gis_survey.estimates.models.DTRMakeMaster;
import com.apepdcl.gis_survey.estimates.models.DTRTypesModel;
import com.apepdcl.gis_survey.estimates.models.DTR_HT_MaterialModel;
import com.apepdcl.gis_survey.estimates.models.FdrParticularsModel;
import com.apepdcl.gis_survey.estimates.models.LTLineConfigModel;
import com.apepdcl.gis_survey.estimates.models.MappedDtrs;
import com.apepdcl.gis_survey.estimates.models.NEW_NEAR_BY_33_KV;
import com.apepdcl.gis_survey.estimates.models.UnMappedDTRs;
import com.apepdcl.gis_survey.estimates.models.Master11kvModel;
import com.apepdcl.gis_survey.estimates.models.Master33kvModel;
import com.apepdcl.gis_survey.estimates.models.MaterialMasterModel;
import com.apepdcl.gis_survey.estimates.models.Near33kvModel;
import com.apepdcl.gis_survey.estimates.models.NearBy11KVSSModel;
import com.apepdcl.gis_survey.estimates.models.NearBy11kvModel;
import com.apepdcl.gis_survey.estimates.models.NearBy33KVSSModel;
import com.apepdcl.gis_survey.estimates.models.PoleDistanceModel;
import com.apepdcl.gis_survey.estimates.models.PoleHeightModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.LocationDetails;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.CustomDialog;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstimatesHome extends AppCompatActivity implements PurposeAdapter.OnItemClickListener {

    ImageView back_layout;
   // RelativeLayout profile_layout;
    private SharedPreferences sharedPreferences;
    private RetrofitServiceEstimates retrofitService;
    ArrayList<PurposeModel> list;
    ArrayList<PoleHeightModel> poleHeightModels;
    ArrayList<ConductorTypeModel> conductorTypeModels;
    ArrayList<DTR_HT_MaterialModel> dtrHtMaterialModels;
    ArrayList<NewModel> newModels;
    ArrayList<DTRTypesModel> dtrTypesModels;
    ArrayList<MaterialMasterModel> materialMasterModels;
    GifImageView loader;
    RecyclerView purpose_rv;

    public static String ACTION = "", ACTION_REMARKS = "", DTR_NAME = "", DTR_CAPACITY = "", DTR_PHASE = "";
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private ProgressDialog progressDialog;
    private List<LoginDataResponse> loginDataResponses;
    private ArcGISDatabase database;
    ArcGISDatabaseEstimates databaseEstimates;
    private double currentLat, currentLog;
    LinearLayout main_ui_layout;
    RelativeLayout loader_layout;
    JsonArray same_33_kv_array;
    private TextView tv_progress_bar;
    public static ArrayList<DTRFeederMaster> dtrFeederMasters;
    private TextView home_heading;
    public static ArrayList<String> other_list_33kv;
    public static ArrayList<String> same_list_33kv;
    public static ArrayList<String> other_list_11kv;
    public static ArrayList<String> same_list_11kv;
    public static ArrayList<String> dtr;
    public static ArrayList<String> allDtrs;
    public static ArrayList<String> Mapped_DTR;
    public static ArrayList<String> all_dtrs_rdss;
    public static ArrayList<String> nearBy11kvSameSec;
    public static ArrayList<String> total11kvSameSec;
    public static ArrayList<String> nearBy11kvOtherSec;
    public static ArrayList<String> total11kvOtherSec;
    public static ArrayList<String> nearBy33kvSS;
    public static ArrayList<String> nearBy11kvSS;
    public static ArrayList<String> totalFeeders33kvSS;
    public static ArrayList<String> totalFeeders11kvSS;
    public static ArrayList<String> nearBy33KVFeeders;

    public static ArrayList<String> NEW_nearBy33kvSS;
    public static ArrayList<String> NEW_nearBy11kvSS;
    public static ArrayList<String> NEW_total33kvSS;
    public static ArrayList<String> NEW_total11kvSS;


    public static ArrayList<NearBy11kvModel> nearBy11kvModels;
    public static ArrayList<Near33kvModel> nearBy33kvModels;
    public static ArrayList<Near33kvModel> nearBy33kvModelsSS;
    public static ArrayList<NearBy11kvModel> nearBy11kvModelsSS;
    public static ArrayList<Master11kvModel> master11kvModels;
    public static ArrayList<Master33kvModel> master33kvModels;

    //New FeederMaster
    public static ArrayList<String> total33kvSSNames;
    public static ArrayList<String> total11kvSSNames;
    public static ArrayList<UnMappedDTRs> UN_MAPPED_DTR_WITH_FEEDERS;
    public static ArrayList<MappedDtrs> MAPPED_DTR_WITH_FEEDERS;
    public static ArrayList<AllDtsWoRdss> ALL_DTRS_WO_RDSS;


    public static ArrayList<NearBy33KVSSModel> NEAR_BY_33KV_SS_MODELS;
    public static ArrayList<NearBy33KVSSModel> NEAR_BY_33KV_SS_MODELS_PROPOSED;
    public static ArrayList<NearBy33KVSSModel> NEAR_BY_33KV_SS_MODELS_EXISTING;
    public static ArrayList<String> total33kvSSNamesExisting = new ArrayList<>();
    public static ArrayList<String> total33kvSSNamesProposed = new ArrayList<>();

    public static ArrayList<NearBy33KVSSModel> TOTAL_33KV_SS_MODELS_EXISTING;
    public static ArrayList<NearBy33KVSSModel> TOTAL_33KV_SS_MODELS_PROPOSED;
    public static ArrayList<String> NEW_total33kvSS_EXISTING;
    public static ArrayList<String> NEW_total33kvSS_PROPOSED;
    public static ArrayList<NearBy11KVSSModel> TOTAL_11KV_SS_MODELS_PROPOSED;


    public static ArrayList<String> NEW_total11kvSS_PROPOSED;



    public static ArrayList<NearBy11KVSSModel> NEAR_BY_11KV_SS_MODELS;
    public static ArrayList<NearBy33KVSSModel> TOTAL_33KV_SS_MODELS;
    public static ArrayList<NearBy11KVSSModel> TOTAL_11KV_SS_MODELS;

    public static ArrayList<NEW_NEAR_BY_33_KV> NEW_NEAR_BY_33_KV;

    public static ArrayList<DTRFeederMaster> TOTAL_DTR_MODELS = new ArrayList<>();
    public static ArrayList<DTRFeederMaster> NEAR_BY_50_DTR_MODELS = new ArrayList<>();
    public static ArrayList<DTRFeederMaster> NEAR_BY_100_DTR_MODELS = new ArrayList<>();
    public static ArrayList<DTRFeederMaster> NEAR_BY_200_DTR_MODELS = new ArrayList<>();
    public static ArrayList<DTRFeederMaster> NEAR_BY_500_DTR_MODELS = new ArrayList<>();
    public static ArrayList<DTRFeederMaster> NEAR_BY_1000_DTR_MODELS = new ArrayList<>();
    public static ArrayList<String> totalDTRNames = new ArrayList<>();
    public static ArrayList<String> nearby50DTRNames = new ArrayList<>();
    public static ArrayList<String> nearby100DTRNames = new ArrayList<>();
    public static ArrayList<String> nearby200DTRNames = new ArrayList<>();
    public static ArrayList<String> nearby500DTRNames = new ArrayList<>();
    public static ArrayList<String> nearby1000DTRNames = new ArrayList<>();


    double DTR_50_Distance = 0.05;  // 50 meters
    double DTR_100_Distance = 0.10;  // 100 meters
    double DTR_200_Distance = 0.20;  // 200 meters
    double DTR_500_Distance = 0.50;  // 500 meters
    double DTR_1000_Distance = 1.00;  // 1000 meters (1 KM) ✅


    boolean isLTCompleted = false;
    boolean is11Completed = false;
    boolean is33Completed = false;
    String ESTIMATION_NUMBER;
    String GIS_ID = "";

    public static boolean isFreshSurvey = true;

    public static String Feeder_type33KV;
    public static String Feeder_type11KV;
    private LinearLayout layout_user_info;

    String type;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_estimates_home);
        getWindow().setStatusBarColor(Color.parseColor("#2B7CEE"));
        getWindow().setNavigationBarColor(Color.parseColor("#2B7CEE"));
        getSupportActionBar().hide();


        type = getIntent().getStringExtra("type");
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_progress, null);
//        TextView mainMessage = dialogView.findViewById(R.id.progress_main_message);
//        TextView secondaryMessage = dialogView.findViewById(R.id.progress_secondary_message);
        progressDialog = new ProgressDialog(EstimatesHome.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        home_heading = findViewById(R.id.home_heading);
        layout_user_info = findViewById(R.id.layout_user_info);
//        progressDialog.show();
//        progressDialog.setContentView(dialogView);
//        mainMessage.setText("Please Wait...");
//        secondaryMessage.setText("Downloading Master Data");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermissions();
        database = new ArcGISDatabase(this);
        databaseEstimates = new ArcGISDatabaseEstimates(this);
        other_list_33kv = new ArrayList<>();
        same_list_33kv = new ArrayList<>();
        other_list_11kv = new ArrayList<>();
        same_list_11kv = new ArrayList<>();
        dtr = new ArrayList<>();
        allDtrs = new ArrayList<>();
        Mapped_DTR = new ArrayList<>();
        all_dtrs_rdss = new ArrayList<>();
        nearBy11kvSameSec = new ArrayList<>();
        total11kvSameSec = new ArrayList<>();
        nearBy11kvOtherSec = new ArrayList<>();
        total11kvOtherSec = new ArrayList<>();
        nearBy33kvSS = new ArrayList<>();
        nearBy11kvSS = new ArrayList<>();
        NEW_nearBy11kvSS = new ArrayList<>();
        NEW_nearBy33kvSS = new ArrayList<>();
        NEW_total33kvSS = new ArrayList<>();
        NEW_total33kvSS_EXISTING = new ArrayList<>();
        NEW_total33kvSS_PROPOSED = new ArrayList<>();
        NEW_total11kvSS_PROPOSED = new ArrayList<>();
        NEW_total11kvSS = new ArrayList<>();
        totalFeeders33kvSS = new ArrayList<>();
        totalFeeders11kvSS = new ArrayList<>();
        nearBy33KVFeeders = new ArrayList<>();
        nearBy11kvModels = new ArrayList<>();
        nearBy33kvModels = new ArrayList<>();
        nearBy33kvModelsSS = new ArrayList<>();
        nearBy11kvModelsSS = new ArrayList<>();
        dtrFeederMasters = new ArrayList<>();
        master11kvModels = new ArrayList<>();
        master33kvModels = new ArrayList<>();
        //New FeederMaster
        total33kvSSNames = new ArrayList<>();
        total11kvSSNames = new ArrayList<>();

        total33kvSSNamesExisting = new ArrayList<>();
        total33kvSSNamesProposed = new ArrayList<>();
        NEAR_BY_33KV_SS_MODELS = new ArrayList<>();
        NEAR_BY_33KV_SS_MODELS_PROPOSED = new ArrayList<>();
        NEAR_BY_33KV_SS_MODELS_EXISTING = new ArrayList<>();

        TOTAL_33KV_SS_MODELS_EXISTING = new ArrayList<>();
        TOTAL_33KV_SS_MODELS_PROPOSED = new ArrayList<>();


        NEAR_BY_11KV_SS_MODELS = new ArrayList<>();
        NEW_NEAR_BY_33_KV = new ArrayList<>();
        TOTAL_11KV_SS_MODELS = new ArrayList<>();
        TOTAL_11KV_SS_MODELS_PROPOSED = new ArrayList<>();
        TOTAL_33KV_SS_MODELS = new ArrayList<>();
        loginDataResponses = database.getUserMasterData();
        UN_MAPPED_DTR_WITH_FEEDERS = new ArrayList<>();
        MAPPED_DTR_WITH_FEEDERS = new ArrayList<>();
        ALL_DTRS_WO_RDSS = new ArrayList<>();


        back_layout = findViewById(R.id.back_layout);
       // profile_layout = findViewById(R.id.profile_layout);
        loader = findViewById(R.id.loader);
        purpose_rv = findViewById(R.id.purpose_rv);
        tv_progress_bar = findViewById(R.id.tv_progress_bar);
        loader_layout = findViewById(R.id.loader_layout);
        main_ui_layout = findViewById(R.id.main_ui_layout);
        layout_user_info.setVisibility(View.VISIBLE);
        if ("ESTIMATES".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            home_heading.setText("Estimates");
        } else if ("SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            home_heading.setText("Solarization of SC ST ");
        } else if ("WORK_ORDER".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
            home_heading.setText("Work Order");
        }else if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
            home_heading.setText("RDSS Asset Survey");
            layout_user_info.setVisibility(View.GONE);
        }


        //getFeederMasterAPI(currentLat,currentLog);

        list = new ArrayList<>();
        poleHeightModels = new ArrayList<>();
        conductorTypeModels = new ArrayList<>();
        dtrTypesModels = new ArrayList<>();
        materialMasterModels = new ArrayList<>();
        dtrHtMaterialModels = new ArrayList<>();
        newModels = new ArrayList<>();

        if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
            getEstimateswithpolecnts();
        } else {
            CommanUtilits.internetDialog(EstimatesHome.this);
        }

        // getEstimateswithpolecnts();
        //  getPurposeData();

        // getPoleTypeData();
        //getManualPurposeData();

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       /* profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        // CustomDialog.showCustomDialog(EstimatesHome.this);

    }

    private void getLatLongs() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLat = location.getLatitude();
                            currentLog = location.getLongitude();
                            //  getFeederMasterAPI(currentLat, currentLog);
                            // Log.d("GETLOCATION------>", "onSuccess: " + "LAT" + currentLat + "LNG" + currentLog);

                            // Proceed with your logic, e.g., navigate to the next screen with lat/long
                            //navigateNextScreen(model, latitude, longitude);
                        } else {
                            // getFeederMasterAPI(0.0, 0.0);
                            Toast.makeText(EstimatesHome.this, "Unable to retrieve location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {

        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Location permissions are required to proceed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupLocationServices() {
        purpose_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EstimatesHome.this);
        purpose_rv.setLayoutManager(layoutManager);
        purpose_rv.setFocusable(false);
        PurposeAdapter adapter = new PurposeAdapter(list, EstimatesHome.this, EstimatesHome.this);
        purpose_rv.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_to_bottom);
    }


    private void getThirtythreekvfeedermaster(String sec_id, String est_no, PurposeModel model) {
        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading 33kV Feeder Master");
        main_ui_layout.setVisibility(View.GONE);



        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getThirtythreekvfeedermaster();

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {

                        String status = surveyedResponseVo.get("STATUS").getAsString();

                        nearBy33kvModels.clear();
                        total33kvSSNames.clear();
                        total33kvSSNamesProposed.clear();
                        total33kvSSNamesExisting.clear();
                        NEAR_BY_33KV_SS_MODELS.clear();
                        NEAR_BY_33KV_SS_MODELS_PROPOSED.clear();
                        NEAR_BY_33KV_SS_MODELS_EXISTING.clear();

                        TOTAL_33KV_SS_MODELS_PROPOSED.clear();
                        TOTAL_33KV_SS_MODELS_EXISTING.clear();
                        TOTAL_33KV_SS_MODELS.clear();
                        NEW_total33kvSS.clear();
                        NEW_total33kvSS_EXISTING.clear();
                        NEW_total33kvSS_PROPOSED.clear();
                        NEW_NEAR_BY_33_KV.clear();

                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");

                            double Eht_Distance = object.has("EHT SS_DISTANCE") ? object.get("EHT SS_DISTANCE").getAsDouble() / 1000.0 : 0.0;


                            //JsonArray dtr_array = object.getAsJsonArray("DTR");
                            JsonArray EHT_SS_MASTER = object.getAsJsonArray("EHT_SS_MASTER");
                            JsonArray FEEDER_33KV = object.getAsJsonArray("33KV_FEEDER_MASTER");


//

                            //--------------------------------START 33KV--------------------------------

                            for (int i = 0; i < EHT_SS_MASTER.size(); i++) {

                                JsonObject jsonObject1 = EHT_SS_MASTER.get(i).getAsJsonObject();
                                String SS_TYPE="";
                                if (!"SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                    SS_TYPE = jsonObject1.get("SS_TYPE").getAsString();
                                }

                                NearBy33KVSSModel model1 = new NearBy33KVSSModel();
                                model1.setEHT_SS_NAME(jsonObject1.get("EHT_SS_NAME").getAsString());
                                model1.setEHT_SS_CODE(jsonObject1.get("EHT_SS_CODE").getAsString());
                                model1.setLATITUDE(jsonObject1.get("LATITUDE").getAsString());
                                model1.setLONGITUDE(jsonObject1.get("LONGITUDE").getAsString());
                                if (!"SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                    model1.setSS_TYPE(jsonObject1.get("SS_TYPE").getAsString());
                                }


                                String ehtSsName = jsonObject1.get("EHT_SS_NAME").getAsString();
                                String ehtSscode = jsonObject1.get("EHT_SS_CODE").getAsString();
                                String feederData = ehtSsName + "~" + ehtSscode;
                                NEW_total33kvSS.add(feederData);

                                if ("EXISTING".equalsIgnoreCase(SS_TYPE)) {
                                    NEW_total33kvSS_EXISTING.add(feederData);
                                } else {
                                    NEW_total33kvSS_PROPOSED.add(feederData);
                                }

                                TOTAL_33KV_SS_MODELS.add(model1);

                            }


                            Set<String> addedCodes = new HashSet<>();

                            for (int i = 0; i < FEEDER_33KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_33KV.get(i).getAsJsonObject();
                                String ehtSsCode = jsonObject1.get("EHT_SS_CODE").getAsString();

                                if (!addedCodes.contains(ehtSsCode)) {
                                    NEW_NEAR_BY_33_KV model1 = new NEW_NEAR_BY_33_KV();
                                    model1.setDIVISION_ID(jsonObject1.get("DIVISION_ID").getAsString());
                                    model1.setEHT_SS_NAME(jsonObject1.get("EHT_SS_NAME").getAsString());
                                    model1.setFEEDER_NAME_33KV(jsonObject1.get("FEEDER_NAME_33KV").getAsString());
                                    model1.setFEEDER_CODE_33KV(jsonObject1.get("FEEDER_CODE_33KV").getAsString());
                                    model1.setEHT_SS_NAME(jsonObject1.get("EHT_SS_NAME").getAsString());
                                    model1.setEHT_SS_CODE(ehtSsCode);
                                    model1.setPOLES(jsonObject1.get("POLES").getAsString());

                                    NEW_NEAR_BY_33_KV.add(model1);
                                    addedCodes.add(ehtSsCode);
                                }
                            }

                            HashSet<String> uniqueSubStationsFor33KV = new HashSet<>();
                            HashSet<String> uniqueExisting = new HashSet<>();
                            HashSet<String> uniqueProposed = new HashSet<>();

                            for (int i = 0; i < EHT_SS_MASTER.size(); i++) {
                                JsonObject jsonObject1 = EHT_SS_MASTER.get(i).getAsJsonObject();
                                String ssType="";
                                if (!"SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                    ssType = jsonObject1.get("SS_TYPE").getAsString();
                                }

                                NearBy33KVSSModel model1 = new NearBy33KVSSModel();
                                model1.setEHT_SS_NAME(jsonObject1.get("EHT_SS_NAME").getAsString());
                                model1.setEHT_SS_CODE(jsonObject1.get("EHT_SS_CODE").getAsString());
                                model1.setLATITUDE(jsonObject1.get("LATITUDE").getAsString());
                                model1.setLONGITUDE(jsonObject1.get("LONGITUDE").getAsString());
                                if (!"SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                    model1.setSS_TYPE(jsonObject1.get("SS_TYPE").getAsString());
                                }




                                String EHT_SS_NAME = jsonObject1.get("EHT_SS_NAME").getAsString();
                                String EHT_SS_CODE = jsonObject1.get("EHT_SS_CODE").getAsString();
                                double LONGITUDE = jsonObject1.get("LONGITUDE").getAsDouble();
                                double LATITUDE = jsonObject1.get("LATITUDE").getAsDouble();


                                double distance = haversine(currentLat, currentLog, LATITUDE, LONGITUDE);

                                String subStationEntry = EHT_SS_NAME + "~" + EHT_SS_CODE;
                                if (distance <= Eht_Distance && uniqueSubStationsFor33KV.add(subStationEntry)) {
                                    total33kvSSNames.add(subStationEntry);
                                    if ("EXISTING".equalsIgnoreCase(ssType)) {
                                        total33kvSSNamesExisting.add(subStationEntry);

                                    } else {
                                        total33kvSSNamesProposed.add(subStationEntry);

                                    }
                                    NEAR_BY_33KV_SS_MODELS.add(model1);
                                    if ("EXISTING".equalsIgnoreCase(ssType)
                                            && uniqueExisting.add(subStationEntry)) {
                                        NEAR_BY_33KV_SS_MODELS_EXISTING.add(model1);

                                    } else if ("PROPOSED".equalsIgnoreCase(ssType)
                                            && uniqueProposed.add(subStationEntry)) {
                                        NEAR_BY_33KV_SS_MODELS_PROPOSED.add(model1);


                                    }
                                }
                            }


                            for (int i = 0; i < FEEDER_33KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_33KV.get(i).getAsJsonObject();
                                Master33kvModel model1 = new Master33kvModel();

                                model1.setEHT_SS_CODE(jsonObject1.get("EHT_SS_CODE").getAsString());
                                model1.setFEEDER_CODE_33KV(jsonObject1.get("FEEDER_CODE_33KV").getAsString());
                                model1.setPOLES(jsonObject1.get("POLES").getAsString());

                                String POLE = jsonObject1.has("POLES") && !jsonObject1.get("POLES").isJsonNull()
                                        ? jsonObject1.get("POLES").getAsString()
                                        : "";

                                if ("0".equalsIgnoreCase(POLE)) {
                                    model1.setFEEDER_CODE_33KV(jsonObject1.get("FEEDER_CODE_33KV").getAsString());
                                    model1.setFEEDER_NAME_33KV(jsonObject1.get("FEEDER_NAME_33KV").getAsString());

                                }

                                master33kvModels.add(model1);
                            }


                            for (int i = 0; i < FEEDER_33KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_33KV.get(i).getAsJsonObject();
                                other_list_33kv.add(jsonObject1.get("FEEDER_NAME_33KV").getAsString() + "~" + jsonObject1.get("FEEDER_CODE_33KV").getAsString());
                                String ehtSsName = jsonObject1.get("EHT_SS_NAME").getAsString();
                                String ehtSscode = jsonObject1.get("EHT_SS_CODE").getAsString();
                                int polesCount = jsonObject1.has("POLES") ? Integer.parseInt(jsonObject1.get("POLES").getAsString()) : 0;
                                if (!totalFeeders33kvSS.contains(ehtSsName)) {
                                    Near33kvModel model = new Near33kvModel();
                                    model.setEHT_SS_NAME(ehtSsName);
                                    model.setFEEDER_NAME_33KV(jsonObject1.get("FEEDER_NAME_33KV").getAsString());
                                    model.setFEEDER_CODE_33KV(jsonObject1.get("FEEDER_CODE_33KV").getAsString());
                                    model.setEHT_SS_CODE(jsonObject1.get("EHT_SS_CODE").getAsString());
                                    model.setPOLES(jsonObject1.get("POLES").getAsString());
                                    nearBy33kvModelsSS.add(model);
                                    totalFeeders33kvSS.add(ehtSsName + "~" + ehtSscode);
                                }
                            }


                            for (int i = 0; i < FEEDER_33KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_33KV.get(i).getAsJsonObject();
                                String ehtSsName = jsonObject1.get("EHT_SS_NAME").getAsString();
                                String ehtSscode = jsonObject1.get("EHT_SS_CODE").getAsString();

                                String combinedKey = ehtSsName + "~" + ehtSscode;

                                same_list_33kv.add(jsonObject1.get("FEEDER_NAME_33KV").getAsString() + "~" + jsonObject1.get("FEEDER_CODE_33KV").getAsString());

                                if (!total33kvSSNames.contains(combinedKey)) {
                                    Near33kvModel model = new Near33kvModel();
                                    model.setEHT_SS_NAME(ehtSsName);
                                    nearBy33kvModelsSS.add(model);
                                    // total33kvSSNames.add(combinedKey);
                                }
                            }

                            //--------------------------------END 33KV--------------------------------
                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {

                                getElevenkvfeedermaster(model.getSEC_ID(), model.getEST_NO(), model);
                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }

                            // getElevenkvfeedermaster(model.getSEC_ID(), model.getEST_NO(), model);


                        } else {
                            progressDialog.cancel();
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(EstimatesHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(EstimatesHome.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EstimatesHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.cancel();

            }
        });
    }


    private void getElevenkvfeedermaster(String sec_id, String est_no, PurposeModel model) {
        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading 11kV Feeder Master");
        main_ui_layout.setVisibility(View.GONE);



        HashMap<String, Object> dataMap = new HashMap<>();

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getElevenkvfeedermaster();

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {

                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        nearBy11kvModels.clear();
                        TOTAL_11KV_SS_MODELS.clear();
                        NEW_total11kvSS.clear();
                        NEAR_BY_11KV_SS_MODELS.clear();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            //getProposedFeederMaster(sec_id,est_no,model);
                            if (!"SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                getProposedFeederMaster(sec_id,est_no,model);
                            }

                            JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");


                            double SS_Distance = object.has("33/11kV SS_DISTANCE") ? object.get("33/11kV SS_DISTANCE").getAsDouble() / 1000.0 : 0.0;

                            //JsonArray dtr_array = object.getAsJsonArray("DTR");

                            // JsonArray EHT_SS_MASTER = object.getAsJsonArray("EHT_SS_MASTER");
                            //JsonArray FEEDER_33KV = object.getAsJsonArray("33KV_FEEDER_MASTER");


                            JsonArray FEEDER_11KV = object.getAsJsonArray("11KV_FEEDER_MASTER");
                            JsonArray SS_MATSER = object.getAsJsonArray("SS_MATSER");
                            for (int i = 0; i < SS_MATSER.size(); i++) {
                                JsonObject jsonObject1 = SS_MATSER.get(i).getAsJsonObject();
                                NearBy11KVSSModel model1 = new NearBy11KVSSModel();
                                model1.setSUB_STATION_NAME(jsonObject1.get("SUB_STATION_NAME").getAsString());
                                model1.setSUB_STATION_ID(jsonObject1.get("SUB_STATION_ID").getAsString());
                                model1.setLATITUDE(jsonObject1.get("LATITUDE").getAsString());
                                model1.setLONGITUDE(jsonObject1.get("LONGITUDE").getAsString());
                                model1.setCIR_NAME(jsonObject1.get("CIR_NAME").getAsString());
                                model1.setDIV_NAME(jsonObject1.get("DIV_NAME").getAsString());
                                model1.setSUB_NAME(jsonObject1.get("SUB_NAME").getAsString());
                                model1.setSEC_NAME(jsonObject1.get("SEC_NAME").getAsString());

                                String ehtSsName = jsonObject1.get("SUB_STATION_NAME").getAsString();
                                String ehtSscode = jsonObject1.get("SUB_STATION_ID").getAsString();
                                String feederData = ehtSsName + "~" + ehtSscode;
                                NEW_total11kvSS.add(feederData);
                                TOTAL_11KV_SS_MODELS.add(model1);
                            }
                            for (int i = 0; i < FEEDER_11KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_11KV.get(i).getAsJsonObject();
                                if (loginDataResponses.get(0).getOFFICE_ID().equalsIgnoreCase(jsonObject1.get("SEC_ID").getAsString())) {
                                    total11kvSameSec.add(jsonObject1.get("FEEDER_NAME").getAsString() + "~" + jsonObject1.get("FEEDER_ID").getAsString());
                                } else {
                                    total11kvOtherSec.add(jsonObject1.get("FEEDER_NAME").getAsString() + "~" + jsonObject1.get("FEEDER_ID").getAsString());
                                }
                            }
                            total11kvSSNames.clear();
                            HashSet<String> uniqueSubStationsFor11KV = new HashSet<>();
                            for (int i = 0; i < SS_MATSER.size(); i++) {
                                JsonObject jsonObject1 = SS_MATSER.get(i).getAsJsonObject();


                                NearBy11KVSSModel model1 = new NearBy11KVSSModel();
                                model1.setSUB_STATION_NAME(jsonObject1.get("SUB_STATION_NAME").getAsString());
                                model1.setSUB_STATION_ID(jsonObject1.get("SUB_STATION_ID").getAsString());
                                model1.setLATITUDE(jsonObject1.get("LATITUDE").getAsString());
                                model1.setLONGITUDE(jsonObject1.get("LONGITUDE").getAsString());
                                model1.setSEC_ID(jsonObject1.get("SEC_ID").getAsString());

                                String SUB_STATION_NAME = jsonObject1.get("SUB_STATION_NAME").getAsString();
                                String SUB_STATION_ID = jsonObject1.get("SUB_STATION_ID").getAsString();
                                double LONGITUDE = jsonObject1.get("LONGITUDE").getAsDouble();
                                double LATITUDE = jsonObject1.get("LATITUDE").getAsDouble();
                                model1.setCIR_NAME(jsonObject1.get("CIR_NAME").getAsString());
                                model1.setDIV_NAME(jsonObject1.get("DIV_NAME").getAsString());
                                model1.setSUB_NAME(jsonObject1.get("SUB_NAME").getAsString());
                                model1.setSEC_NAME(jsonObject1.get("SEC_NAME").getAsString());

                                double distance = haversine(currentLat, currentLog, LATITUDE, LONGITUDE);
                                String subStationEntry = SUB_STATION_NAME + "~" + SUB_STATION_ID;
                                if (distance <= SS_Distance && uniqueSubStationsFor11KV.add(subStationEntry)) {
                                    total11kvSSNames.add(subStationEntry);
                                    NEAR_BY_11KV_SS_MODELS.add(model1);
                                }
                            }

                            for (int i = 0; i < FEEDER_11KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_11KV.get(i).getAsJsonObject();
                                Master11kvModel model1 = new Master11kvModel();

                                model1.setSUB_STATION_ID(jsonObject1.get("SUB_STATION_ID").getAsString());
                                model1.setSUB_STATION_NAME(jsonObject1.get("SUB_STATION_NAME").getAsString());
                                model1.setPOLES(jsonObject1.get("POLES").getAsString());

                                String POLE = jsonObject1.has("POLES") && !jsonObject1.get("POLES").isJsonNull()
                                        ? jsonObject1.get("POLES").getAsString()
                                        : "";

                                if ("0".equalsIgnoreCase(POLE)) {
                                    model1.setFEEDER_ID(jsonObject1.get("FEEDER_ID").getAsString());
                                    model1.setFEEDER_NAME(jsonObject1.get("FEEDER_NAME").getAsString());

                                }

                                master11kvModels.add(model1);
                            }


                            for (int i = 0; i < FEEDER_11KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_11KV.get(i).getAsJsonObject();
                                same_list_11kv.add(jsonObject1.get("FEEDER_NAME").getAsString() + "~" + jsonObject1.get("FEEDER_ID").getAsString());
                                NearBy11kvModel model = new NearBy11kvModel();
                                model.setSEC_ID(jsonObject1.get("SEC_ID").getAsString());
                                model.setFEEDER_ID(jsonObject1.get("FEEDER_ID").getAsString());
                                model.setFEEDER_NAME(jsonObject1.get("FEEDER_NAME").getAsString());
                                nearBy11kvModels.add(model);

                            }


                            for (int i = 0; i < FEEDER_11KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_11KV.get(i).getAsJsonObject();
                                String ehtSsName = jsonObject1.get("SUB_STATION_NAME").getAsString();
                                String ehtSscode = jsonObject1.get("SUB_STATION_ID").getAsString();
                                String combinedKey = ehtSsName + "~" + ehtSscode;
                                String feederData = jsonObject1.get("FEEDER_NAME").getAsString() + "~" + jsonObject1.get("FEEDER_ID").getAsString();

                                if (!same_list_11kv.contains(feederData)) {
                                    same_list_11kv.add(feederData);
                                }

                                if (!total11kvSSNames.contains(combinedKey)) {
                                    NearBy11kvModel model = new NearBy11kvModel();
                                    model.setSUB_STATION_NAME(ehtSsName);
                                    nearBy11kvModelsSS.add(model);
                                    //total11kvSSNames.add(combinedKey);
                                }

                                if (loginDataResponses.get(0).getOFFICE_ID().equalsIgnoreCase(jsonObject1.get("SEC_ID").getAsString())) {
                                    if (!nearBy11kvSameSec.contains(feederData)) {
                                        nearBy11kvSameSec.add(feederData);
                                    }
                                } else {
                                    if (!nearBy11kvOtherSec.contains(feederData)) {
                                        nearBy11kvOtherSec.add(feederData);
                                    }
                                }
                            }

                            for (int i = 0; i < FEEDER_11KV.size(); i++) {
                                JsonObject jsonObject1 = FEEDER_11KV.get(i).getAsJsonObject();
                                other_list_11kv.add(jsonObject1.get("FEEDER_NAME").getAsString() + "~" + jsonObject1.get("FEEDER_ID").getAsString());
                                String ehtSsName = jsonObject1.get("SUB_STATION_NAME").getAsString();
                                String ehtSscode = jsonObject1.get("SUB_STATION_ID").getAsString();
                                int polesCount = jsonObject1.has("POLES") ? Integer.parseInt(jsonObject1.get("POLES").getAsString()) : 0;

                                if (!totalFeeders11kvSS.contains(ehtSsName)) {
                                    NearBy11kvModel model = new NearBy11kvModel();
                                    model.setSUB_STATION_NAME(ehtSsName);
                                    model.setFEEDER_NAME(jsonObject1.get("FEEDER_NAME").getAsString());
                                    model.setSUB_STATION_ID(jsonObject1.get("SUB_STATION_ID").getAsString());
                                    model.setFEEDER_ID(jsonObject1.get("FEEDER_ID").getAsString());
                                    model.setPOLES(jsonObject1.get("POLES").getAsString());
                                    nearBy11kvModelsSS.add(model);
                                    totalFeeders11kvSS.add(ehtSsName + "~" + ehtSscode);
                                }
                            }


                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){

                                    JsonObject jsonObject=new JsonObject();
                                    JsonArray searchOptions = new JsonArray();
                                    jsonObject.add("SEARCH_OPTIONS", searchOptions);
                                    jsonObject.addProperty("GIS_ID", model.getGIS_ID());
                                    if (model.getSTATUS().equalsIgnoreCase("1")){
                                        getDtrmaster(model.getSEC_ID(), model.getEST_NO(), model);
                                    }else {
                                        getDtrMasterForMulSec(jsonObject,model);
                                    }

                                }else {
                                    getDtrmaster(model.getSEC_ID(), model.getEST_NO(), model);
                                }

                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }


                        } else {
                            progressDialog.cancel();
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(EstimatesHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(EstimatesHome.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EstimatesHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.cancel();

            }
        });
    }

    private void getProposedFeederMaster(String sec_id, String est_no, PurposeModel model) {
        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading 33kV Feeder Master");
        main_ui_layout.setVisibility(View.GONE);

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getProposedFeederMaster();

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        NEW_total33kvSS_EXISTING.clear();
                        NEW_total33kvSS_PROPOSED.clear();
                        NEW_total11kvSS_PROPOSED.clear();



                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");


                            //JsonArray dtr_array = object.getAsJsonArray("DTR");
                            JsonArray EHT_SS_MASTER = object.getAsJsonArray("EHT_SS_MASTER");
                            JsonArray FEEDER_33KV = object.getAsJsonArray("33KV_FEEDER_MASTER");


//

                            //--------------------------------START 33KV--------------------------------

                            for (int i = 0; i < EHT_SS_MASTER.size(); i++) {

                                JsonObject jsonObject1 = EHT_SS_MASTER.get(i).getAsJsonObject();
                                String SS_TYPE="";
                                if (!"SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                    SS_TYPE = jsonObject1.get("SS_TYPE").getAsString();
                                }

                                NearBy33KVSSModel model1 = new NearBy33KVSSModel();
                                model1.setEHT_SS_NAME(jsonObject1.get("EHT_SS_NAME").getAsString());
                                model1.setEHT_SS_CODE(jsonObject1.get("EHT_SS_CODE").getAsString());
                                model1.setLATITUDE(jsonObject1.get("LATITUDE").getAsString());
                                model1.setLONGITUDE(jsonObject1.get("LONGITUDE").getAsString());
                                if ("SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                    model1.setSS_TYPE(jsonObject1.get("SS_TYPE").getAsString());
                                }


                                String ehtSsName = jsonObject1.get("EHT_SS_NAME").getAsString();
                                String ehtSscode = jsonObject1.get("EHT_SS_CODE").getAsString();
                                String feederData = ehtSsName + "~" + ehtSscode;
                                if ("EXISTING".equalsIgnoreCase(SS_TYPE)) {
                                    NEW_total33kvSS_EXISTING.add(feederData);
                                } else {
                                    NEW_total33kvSS_PROPOSED.add(feederData);
                                }
                            }


                            JsonArray SS_MATSER = object.getAsJsonArray("SS_MATSER");
                            for (int i = 0; i < SS_MATSER.size(); i++) {
                                JsonObject jsonObject1 = SS_MATSER.get(i).getAsJsonObject();
                                NearBy11KVSSModel model1 = new NearBy11KVSSModel();
                                model1.setSUB_STATION_NAME(jsonObject1.get("SUB_STATION_NAME").getAsString());
                                model1.setSUB_STATION_ID(jsonObject1.get("SUB_STATION_ID").getAsString());
                                model1.setLATITUDE(jsonObject1.get("LATITUDE").getAsString());
                                model1.setLONGITUDE(jsonObject1.get("LONGITUDE").getAsString());
                                model1.setCIR_NAME(jsonObject1.get("CIR_NAME").getAsString());
                                model1.setDIV_NAME(jsonObject1.get("DIV_NAME").getAsString());
                                model1.setSUB_NAME(jsonObject1.get("SUB_NAME").getAsString());
                                model1.setSEC_NAME(jsonObject1.get("SEC_NAME").getAsString());

                                String ehtSsName = jsonObject1.get("SUB_STATION_NAME").getAsString();
                                String ehtSscode = jsonObject1.get("SUB_STATION_ID").getAsString();
                                String feederData = ehtSsName + "~" + ehtSscode;
                                NEW_total11kvSS_PROPOSED.add(feederData);

                                TOTAL_11KV_SS_MODELS_PROPOSED.add(model1);
                            }

                            Set<String> addedCodes = new HashSet<>();


                            HashSet<String> uniqueSubStationsFor33KV = new HashSet<>();
                            HashSet<String> uniqueExisting = new HashSet<>();
                            HashSet<String> uniqueProposed = new HashSet<>();

                            for (int i = 0; i < EHT_SS_MASTER.size(); i++) {
                                JsonObject jsonObject1 = EHT_SS_MASTER.get(i).getAsJsonObject();
                                String ssType="";
                                if (!"SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                    ssType = jsonObject1.get("SS_TYPE").getAsString();
                                }
                                NearBy33KVSSModel model1 = new NearBy33KVSSModel();
                                model1.setEHT_SS_NAME(jsonObject1.get("EHT_SS_NAME").getAsString());
                                model1.setEHT_SS_CODE(jsonObject1.get("EHT_SS_CODE").getAsString());
                                model1.setLATITUDE(jsonObject1.get("LATITUDE").getAsString());
                                model1.setLONGITUDE(jsonObject1.get("LONGITUDE").getAsString());
                                if (!"SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
                                    model1.setSS_TYPE(jsonObject1.get("SS_TYPE").getAsString());
                                }



                                String EHT_SS_NAME = jsonObject1.get("EHT_SS_NAME").getAsString();
                                String EHT_SS_CODE = jsonObject1.get("EHT_SS_CODE").getAsString();
                                double LONGITUDE = jsonObject1.get("LONGITUDE").getAsDouble();
                                double LATITUDE = jsonObject1.get("LATITUDE").getAsDouble();


                                double distance = haversine(currentLat, currentLog, LATITUDE, LONGITUDE);
                                String subStationEntry = EHT_SS_NAME + "~" + EHT_SS_CODE;
                                if ("EXISTING".equalsIgnoreCase(ssType)
                                        && uniqueExisting.add(subStationEntry)) {
                                    NEAR_BY_33KV_SS_MODELS_EXISTING.add(model1);

                                } else if ("PROPOSED".equalsIgnoreCase(ssType)
                                        && uniqueProposed.add(subStationEntry)) {
                                    NEAR_BY_33KV_SS_MODELS_PROPOSED.add(model1);


                                }
                            }
                        } else {
                            progressDialog.cancel();
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(EstimatesHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Handle case where STATUS is missing or null
                        Toast.makeText(EstimatesHome.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EstimatesHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.cancel();

            }
        });
    }


    private void getDtrmaster(String sec_id, String est_no, PurposeModel model) {

        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading DTR Master");
        main_ui_layout.setVisibility(View.GONE);

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("OFFICE_ID", sec_id);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(dataMap).getAsJsonObject();

        retrofitService = RetrofitClientInstanceEstimates
                .getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN())
                .create(RetrofitServiceEstimates.class);

        Call<JsonObject> responseVoCall = retrofitService.getdtrmaster(jsonObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);

                if (response.body() == null) {
                    Toast.makeText(EstimatesHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 🔹 CLEAR LISTS
                TOTAL_DTR_MODELS.clear();
                NEAR_BY_50_DTR_MODELS.clear();
                NEAR_BY_100_DTR_MODELS.clear();
                NEAR_BY_200_DTR_MODELS.clear();
                NEAR_BY_500_DTR_MODELS.clear();
                NEAR_BY_1000_DTR_MODELS.clear();
                totalDTRNames.clear();
                nearby50DTRNames.clear();
                nearby100DTRNames.clear();
                nearby200DTRNames.clear();
                nearby500DTRNames.clear();
                nearby1000DTRNames.clear();

                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(EstimatesHome.this,
                            "Invalid response from server: STATUS missing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(EstimatesHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                JsonArray dtr_array = object.getAsJsonArray("DTR");

                for (int i = 0; i < dtr_array.size(); i++) {

                    JsonObject jsonObject1 = dtr_array.get(i).getAsJsonObject();

                    String isMapped = jsonObject1.has("MAPPED") && !jsonObject1.get("MAPPED").isJsonNull()
                            ? jsonObject1.get("MAPPED").getAsString()
                            : "";

                    if (!"Yes".equalsIgnoreCase(isMapped)) {
                        UnMappedDTRs unMappedDTRs = new UnMappedDTRs();
                        unMappedDTRs.setFEEDER_ID(jsonObject1.get("FEEDER_ID").getAsString());
                        unMappedDTRs.setNO_OF_LT_CKTS(jsonObject1.get("NO_OF_LT_CKTS").getAsString());
                        unMappedDTRs.setDTR_STRUCTURE_CODE(
                                jsonObject1.get("DTR_STRUCTURE_CODE").getAsString() + "~" +
                                        (jsonObject1.get("DTR_LOCATION").isJsonNull() ? "" :
                                                jsonObject1.get("DTR_LOCATION").getAsString()));
                        unMappedDTRs.setWO_MAPPED(jsonObject1.get("WO_MAPPED").getAsString());
                        UN_MAPPED_DTR_WITH_FEEDERS.add(unMappedDTRs);
                    } else {
                        MappedDtrs mappedDTRs = new MappedDtrs();
                        mappedDTRs.setFEEDER_ID(jsonObject1.get("FEEDER_ID").getAsString());
                        mappedDTRs.setNO_OF_LT_CKTS(jsonObject1.get("NO_OF_LT_CKTS").getAsString());
                        mappedDTRs.setDTR_STRUCTURE_CODE(
                                jsonObject1.get("DTR_STRUCTURE_CODE").getAsString() + "~" +
                                        (jsonObject1.get("DTR_LOCATION").isJsonNull() ? "" :
                                                jsonObject1.get("DTR_LOCATION").getAsString()));
                        MAPPED_DTR_WITH_FEEDERS.add(mappedDTRs);
                    }
                    allDtrs.add(jsonObject1.get("DTR_STRUCTURE_CODE").getAsString() + "~" + (jsonObject1.get("DTR_LOCATION").isJsonNull() ? "" : jsonObject1.get("DTR_LOCATION").getAsString()));

                    // 🔹 BUILD MASTER OBJECT ONCE
                    DTRFeederMaster master = new DTRFeederMaster();

                    master.setDTR_STRUCTURE_CODE(jsonObject1.get("DTR_STRUCTURE_CODE").getAsString());
                    master.setDTR_LOCATION(jsonObject1.get("DTR_LOCATION").isJsonNull() ? "" :
                            jsonObject1.get("DTR_LOCATION").getAsString());
                    master.setCAPACITY(jsonObject1.has("CAPACITY") && !jsonObject1.get("CAPACITY").isJsonNull()
                            ? jsonObject1.get("CAPACITY").getAsString() : "");
                    master.setPHASE(jsonObject1.has("PHASE") && !jsonObject1.get("PHASE").isJsonNull()
                            ? jsonObject1.get("PHASE").getAsString() : "");
                    master.setIS_MAPPED(isMapped);
                    master.setDTR_MAKE(jsonObject1.has("DTR_MAKE") && !jsonObject1.get("DTR_MAKE").isJsonNull()
                            ? jsonObject1.get("DTR_MAKE").getAsString() : "");
                    master.setDTR_SLNO(jsonObject1.has("DTR_SLNO") && !jsonObject1.get("DTR_SLNO").isJsonNull()
                            ? jsonObject1.get("DTR_SLNO").getAsString() : "");
                    master.setNO_OF_LT_CKTS(jsonObject1.has("NO_OF_LT_CKTS") && !jsonObject1.get("NO_OF_LT_CKTS").isJsonNull()
                            ? jsonObject1.get("NO_OF_LT_CKTS").getAsString() : "");
                    master.setMAPPED_LOCATION_NO(jsonObject1.has("MAPPED_LOCATION_NO") && !jsonObject1.get("MAPPED_LOCATION_NO").isJsonNull()
                            ? jsonObject1.get("MAPPED_LOCATION_NO").getAsString() : "");
                    master.setFEEDER_ID(jsonObject1.get("FEEDER_ID").getAsString());
                    master.setFEEDER_NAME(jsonObject1.get("FEEDER_NAME").getAsString());
                    master.setGLAT(jsonObject1.has("GLAT") && !jsonObject1.get("GLAT").isJsonNull()
                            ? jsonObject1.get("GLAT").getAsString() : "");
                    master.setGLONG(jsonObject1.has("GLONG") && !jsonObject1.get("GLONG").isJsonNull()
                            ? jsonObject1.get("GLONG").getAsString() : "");

                    double latitude = 0.0;
                    double longitude = 0.0;

                    if (jsonObject1.has("GLAT") && !jsonObject1.get("GLAT").isJsonNull()
                            && jsonObject1.has("GLONG") && !jsonObject1.get("GLONG").isJsonNull()) {

                        longitude = jsonObject1.get("GLAT").getAsDouble();
                        latitude = jsonObject1.get("GLONG").getAsDouble();
                    }

                    // 🔹 TOTAL DTR
                    TOTAL_DTR_MODELS.add(master);
                    String displayText = master.getDTR_STRUCTURE_CODE() + "~" + master.getDTR_LOCATION();
                    totalDTRNames.add(displayText);

                    // 🔹 NEARBY DTR
                    if (!master.getGLAT().isEmpty() && !master.getGLONG().isEmpty()) {
                        double dtrLat = Double.parseDouble(master.getGLAT());
                        double dtrLng = Double.parseDouble(master.getGLONG());
                        double distance = haversine(currentLat, currentLog, latitude, longitude);


                        if (distance <= DTR_50_Distance) {
                            NEAR_BY_50_DTR_MODELS.add(master);
                            nearby50DTRNames.add(displayText);
                        }

                        if (distance <= DTR_100_Distance) {
                            NEAR_BY_100_DTR_MODELS.add(master);
                            nearby100DTRNames.add(displayText);
                        }

                        if (distance <= DTR_200_Distance) {
                            NEAR_BY_200_DTR_MODELS.add(master);
                            nearby200DTRNames.add(displayText);
                        }

                        if (distance <= DTR_500_Distance) {
                            NEAR_BY_500_DTR_MODELS.add(master);
                            nearby500DTRNames.add(displayText);
                        }

                        if (distance <= DTR_1000_Distance) {
                            NEAR_BY_1000_DTR_MODELS.add(master);
                            nearby1000DTRNames.add(displayText);
                        }
                    }

                    dtrFeederMasters.add(master);
                    databaseEstimates.addFeederMasterDtrs(master);
                }

                if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                    getPoleTypeData(est_no, model);
                } else {
                    CommanUtilits.internetDialog(EstimatesHome.this);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.cancel();
            }
        });
    }

    private void getDtrMasterForMulSec(JsonObject request,PurposeModel model) {

        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading DTR Master");
        main_ui_layout.setVisibility(View.GONE);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance("").create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.dtrmasterRdss(request);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                main_ui_layout.setVisibility(View.GONE);
                loader_layout.setVisibility(View.VISIBLE);
                EstimatesHome.totalDTRNames.clear();
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonObject objectData = surveyedResponseVo.getAsJsonObject("DATA");
                            JsonArray arrayData = objectData.getAsJsonArray("DTR");
                            Executors.newSingleThreadExecutor().execute(() -> {

                                for (int i = 0; i < arrayData.size(); i++) {
                                    JsonObject object = arrayData.get(i).getAsJsonObject();
                                    AllDtsWoRdss master = new AllDtsWoRdss();
                                    DTRFeederMaster master1 = new DTRFeederMaster();
                                    master.setDTR_STRUCTURE_CODE(object.get("DTR_STRUCTURE_CODE").getAsString());
                                    master.setDTR_LOCATION(object.get("DTR_LOCATION").isJsonNull() ? "" :
                                            object.get("DTR_LOCATION").getAsString());
                                    master.setCAPACITY(object.has("CAPACITY") && !object.get("CAPACITY").isJsonNull()
                                            ? object.get("CAPACITY").getAsString() : "");
                                    master.setPHASE(object.has("PHASE") && !object.get("PHASE").isJsonNull()
                                            ? object.get("PHASE").getAsString() : "");
                                    master.setDTR_MAKE(object.has("DTR_MAKE") && !object.get("DTR_MAKE").isJsonNull()
                                            ? object.get("DTR_MAKE").getAsString() : "");
                                    master.setNO_OF_LT_CKTS(object.has("NO_OF_LT_CKTS") && !object.get("NO_OF_LT_CKTS").isJsonNull()
                                            ? object.get("NO_OF_LT_CKTS").getAsString() : "");
                                    master.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull()
                                            ? object.get("FEEDER_ID").getAsString() : "");
                                    master.setFEEDER_NAME(object.has("FEEDER_NAME") && !object.get("FEEDER_NAME").isJsonNull()
                                            ? object.get("FEEDER_NAME").getAsString() : "");

                                    master.setGLAT(object.has("GLAT") && !object.get("GLAT").isJsonNull()
                                            ? object.get("GLAT").getAsString() : "");
                                    master.setGLONG(object.has("GLONG") && !object.get("GLONG").isJsonNull()
                                            ? object.get("GLONG").getAsString() : "");

                                    String isMapped = object.has("MAPPED") && !object.get("MAPPED").isJsonNull()
                                            ? object.get("MAPPED").getAsString()
                                            : "";
                                    master.setIS_MAPPED(isMapped);

                                    master1.setDTR_STRUCTURE_CODE(object.get("DTR_STRUCTURE_CODE").getAsString());
                                    master1.setDTR_LOCATION(object.get("DTR_LOCATION").isJsonNull() ? "" :
                                            object.get("DTR_LOCATION").getAsString());
                                    master1.setCAPACITY(object.has("CAPACITY") && !object.get("CAPACITY").isJsonNull()
                                            ? object.get("CAPACITY").getAsString() : "");
                                    master1.setPHASE(object.has("PHASE") && !object.get("PHASE").isJsonNull()
                                            ? object.get("PHASE").getAsString() : "");
                                    master1.setDTR_MAKE(object.has("DTR_MAKE") && !object.get("DTR_MAKE").isJsonNull()
                                            ? object.get("DTR_MAKE").getAsString() : "");
                                    master1.setNO_OF_LT_CKTS(object.has("NO_OF_LT_CKTS") && !object.get("NO_OF_LT_CKTS").isJsonNull()
                                            ? object.get("NO_OF_LT_CKTS").getAsString() : "");
                                    master1.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull()
                                            ? object.get("FEEDER_ID").getAsString() : "");
                                    master1.setFEEDER_NAME(object.has("FEEDER_NAME") && !object.get("FEEDER_NAME").isJsonNull()
                                            ? object.get("FEEDER_NAME").getAsString() : "");

                                    master1.setGLAT(object.has("GLAT") && !object.get("GLAT").isJsonNull()
                                            ? object.get("GLAT").getAsString() : "");
                                    master1.setGLONG(object.has("GLONG") && !object.get("GLONG").isJsonNull()
                                            ? object.get("GLONG").getAsString() : "");
                                    isMapped = object.has("MAPPED") && !object.get("MAPPED").isJsonNull()
                                            ? object.get("MAPPED").getAsString()
                                            : "";
                                    master1.setIS_MAPPED(isMapped);

                                    double latitude = 0.0;
                                    double longitude = 0.0;


                                    if (!"Yes".equalsIgnoreCase(isMapped)) {
                                        UnMappedDTRs unMappedDTRs = new UnMappedDTRs();
                                        unMappedDTRs.setFEEDER_ID(object.get("FEEDER_ID").getAsString());
                                        unMappedDTRs.setNO_OF_LT_CKTS(object.get("NO_OF_LT_CKTS").getAsString());
                                        unMappedDTRs.setDTR_STRUCTURE_CODE(
                                                object.get("DTR_STRUCTURE_CODE").getAsString() + "~" +
                                                        (object.get("DTR_LOCATION").isJsonNull() ? "" :
                                                                object.get("DTR_LOCATION").getAsString()));
                                        unMappedDTRs.setWO_MAPPED(object.get("WO_MAPPED").getAsString());
                                        UN_MAPPED_DTR_WITH_FEEDERS.add(unMappedDTRs);
                                    }

                                    if (object.has("GLAT") && !object.get("GLAT").isJsonNull()
                                            && object.has("GLONG") && !object.get("GLONG").isJsonNull()) {

                                        longitude = object.get("GLAT").getAsDouble();
                                        latitude = object.get("GLONG").getAsDouble();
                                    }
                                    EstimatesHome.all_dtrs_rdss.add(object.get("DTR_STRUCTURE_CODE").getAsString() + "~" + (object.get("DTR_LOCATION").isJsonNull() ? "" : object.get("DTR_LOCATION").getAsString()));
                                    String displayText = master.getDTR_STRUCTURE_CODE() + "~" + master.getDTR_LOCATION();
                                    EstimatesHome.totalDTRNames.add(displayText);
                                    EstimatesHome.dtrFeederMasters.add(master1);
                                    EstimatesHome.ALL_DTRS_WO_RDSS.add(master);
                                    //dtrFeederMasters.add(master1);
                                    long start = System.currentTimeMillis();

                                    databaseEstimates.addFeederMasterDtrs(master1);
                                    long end = System.currentTimeMillis();
                                }

                                runOnUiThread(() -> {
                                    if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {

                                        getPoleTypeData(model.getEST_NO(), model);
                                    } else {
                                        CommanUtilits.internetDialog(EstimatesHome.this);
                                    }

                                });

                            });

                        }
                    }
                } else {

                    Toast.makeText(EstimatesHome.this,
                            "Server Busy Try Again",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout.setVisibility(View.GONE);

                Toast.makeText(EstimatesHome.this,
                        "Try Again " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void clickContinueBtnAction(PurposeModel model, int position) {
        boolean is33kvSelected = false;
        boolean is11kvSelected = false;
        boolean isLtSelected = false;
        int FEEDER_COUNT = 0;
        int FEEDER_COUNT_33 = 0;

        if (list.get(position).getLINE_33KV().equals("Y")) {
            is33kvSelected = true;
        }
        if (list.get(position).getLINE_11KV().equals("Y")) {
            is11kvSelected = true;
        }

        if (list.get(position).getLINE_LT().equals("Y")) {
            isLtSelected = true;
        }

        //LINE EXTENSION CHECK
        if (model.getSTATUS().equalsIgnoreCase("2")) {
            if (model.getLINE_33KV().equalsIgnoreCase("Y")) {
                List<FdrParticularsModel> fdrList = model.getFDR_PARTICULARS();
                if (fdrList != null && !fdrList.isEmpty()) {
                    for (int i = 0; i < fdrList.size(); i++) {
                        if (fdrList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 1) {
                                FEEDER_COUNT_33 = 1;
                            }
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 2) {
                                FEEDER_COUNT_33 = 2;
                            }
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 3) {
                                FEEDER_COUNT_33 = 3;
                            }
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 4) {
                                FEEDER_COUNT_33 = 4;
                            }
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 5) {
                                FEEDER_COUNT_33 = 5;
                            }
                        }
                    }
                }
            }

            if (model.getLINE_11KV().equalsIgnoreCase("Y")) {
                List<FdrParticularsModel> fdrList = model.getFDR_PARTICULARS();
                if (fdrList != null && !fdrList.isEmpty()) {
                    for (int i = 0; i < fdrList.size(); i++) {
                        if (fdrList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 1) {
                                FEEDER_COUNT = 1;
                            }
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 2) {
                                FEEDER_COUNT = 2;
                            }
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 3) {
                                FEEDER_COUNT = 3;
                            }
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 4) {
                                FEEDER_COUNT = 4;
                            }
                            if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 5) {
                                FEEDER_COUNT = 5;
                            }
                        }
                    }
                }
            }

        }

        //MY CODE
        if (model.getSTATUS().equalsIgnoreCase("2")) {
            isFreshSurvey = false;
            String voltageLevel33KV = "33KV";
            String voltageLevel11KV = "11KV";

            // Initialize variables
            Feeder_type33KV = null;
            Feeder_type11KV = null;

            if (model.getFDR_PARTICULARS() != null) {
                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                    String voltageLevel = model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL();

                    if (voltageLevel.equalsIgnoreCase(voltageLevel33KV)) {

                        Feeder_type33KV = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();

                    }

                    if (voltageLevel.equalsIgnoreCase(voltageLevel11KV) && model.getLINE_11KV().equalsIgnoreCase("Y")) {

                        if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                            Feeder_type11KV = model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE();
                        }


                    }
                }
            }

            // Log.d("FEEDER_SURVEY_11KV", "Feeder 1: " + feeder_name1_11kv + ", Feeder 2: " + feeder_name2_11kv);

            //  Toast.makeText(this, feeder_name1_11kv, Toast.LENGTH_SHORT).show();

        } else {
            isFreshSurvey = true;
        }


        databaseEstimates.updateSelectStatusALL(list.get(position).getEST_NO(), is33kvSelected, is11kvSelected, isLtSelected, FEEDER_COUNT, FEEDER_COUNT_33, list.get(position).getGIS_ID());
    }

    private void getEstimateswithpolecnts() {
        loader_layout.setVisibility(View.VISIBLE);
        String survey_from = LoginActivity.SURVEY_FROM;
        if (survey_from != null) {
            if (survey_from.equalsIgnoreCase("ESTIMATES")) {
                tv_progress_bar.setText("Downloading Estimates Data");
            } else {
                tv_progress_bar.setText("Downloading Work order Data");
            }
        }
        main_ui_layout.setVisibility(View.GONE);

        //loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        // dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        // dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        dtrDetailsReqestVo.addProperty("STATUS", type);
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
        }else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }
        dtrDetailsReqestVo.addProperty("USER_ID", loginDataResponses.get(0).getUSERID());

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.estimateswithpolecnts(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);
                //loader.setVisibility(View.GONE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);

                    // Check if the STATUS field exists and is not null
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();

                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            getLatLongs();

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();

                                if (!object.get("STATUS").isJsonNull()) {
                                    if (object.get("STATUS").getAsString().equals(type)) {

                                        if (SurveyList.IS_EDIT) {
                                            if ("ESTIMATES".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                                if (object.get("EST_NO").getAsString().equalsIgnoreCase(SurveyList.EDIT_ESTIMATE_NO)) {
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
                                                    model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                                    model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                                    model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");
                                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                        model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                                        model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                                    }


                                                    model.setCOUNT_33KV(object.get("COUNT_33KV").getAsInt());
                                                    model.setCOUNT_11KV(object.get("COUNT_11KV").getAsInt());
                                                    model.setCOUNT_LT(object.get("COUNT_LT").getAsInt());

                                                    model.setNUM_33KV(object.get("NUM_33KV").getAsInt());
                                                    model.setNUM_11KV(object.get("NUM_11KV").getAsInt());
                                                    model.setNUM_LT(object.get("NUM_LT").getAsInt());


                                                    model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                                    model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");

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

                                                            fdrList.add(fdrModel);
                                                        }
                                                        model.setFDR_PARTICULARS(fdrList);
                                                    }

                                                    list.add(model);
                                                }
                                            } else {
                                                //20-02-2025 Chandu Changes
                                                if (object.get("GIS_ID").getAsString().equalsIgnoreCase(SurveyList.EDIT_GIS_ID)) {
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
                                                    model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                                    model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                                    model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");
                                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                        model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                                        model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                                    }


                                                    model.setCOUNT_33KV(object.get("COUNT_33KV").getAsInt());
                                                    model.setCOUNT_11KV(object.get("COUNT_11KV").getAsInt());
                                                    model.setCOUNT_LT(object.get("COUNT_LT").getAsInt());

                                                    model.setNUM_33KV(object.get("NUM_33KV").getAsInt());
                                                    model.setNUM_11KV(object.get("NUM_11KV").getAsInt());
                                                    model.setNUM_LT(object.get("NUM_LT").getAsInt());


                                                    model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                                    model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");

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

                                                            fdrList.add(fdrModel);
                                                        }
                                                        model.setFDR_PARTICULARS(fdrList);
                                                    }

                                                    list.add(model);
                                                }
                                            }
                                        } else {
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
                                            model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                            model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");
                                            model.setRE_SURVEY(object.has("RE_SURVEY") && !object.get("RE_SURVEY").isJsonNull() ? object.get("RE_SURVEY").getAsString() : "");
                                            model.setREVISED(object.has("REVISED") && !object.get("REVISED").isJsonNull() ? object.get("REVISED").getAsString() : "");
                                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                                model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                            }

                                            model.setCOUNT_33KV(object.get("COUNT_33KV").getAsInt());
                                            model.setCOUNT_11KV(object.get("COUNT_11KV").getAsInt());
                                            model.setCOUNT_LT(object.get("COUNT_LT").getAsInt());

                                            model.setNUM_33KV(object.get("NUM_33KV").getAsInt());
                                            model.setNUM_11KV(object.get("NUM_11KV").getAsInt());
                                            model.setNUM_LT(object.get("NUM_LT").getAsInt());

                                            model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                            model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");

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

                                                    fdrList.add(fdrModel);
                                                }
                                                model.setFDR_PARTICULARS(fdrList);
                                            }

                                            list.add(model);
                                        }


                                    }
                                }


                            }


                            setupLocationServices();

                        } else {
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(EstimatesHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // Handle case where STATUS is missing or null
                        Toast.makeText(EstimatesHome.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EstimatesHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(EstimatesHome.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void getSingleEstimateWithFdrs(String GIS_ID, PurposeModel model, int pos) {
        loader_layout.setVisibility(View.VISIBLE);
        String survey_from = LoginActivity.SURVEY_FROM;
        if (survey_from != null) {
            if (survey_from.equalsIgnoreCase("ESTIMATES")) {
                tv_progress_bar.setText("Downloading Estimates Data");
            } else if (survey_from.equalsIgnoreCase("SOLAR_DPR")) {
                tv_progress_bar.setText("Downloading Solar DPR Data");
            } else {
                tv_progress_bar.setText("Downloading Work order Data");
            }
        }
        main_ui_layout.setVisibility(View.GONE);

        //loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        // dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        // dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        dtrDetailsReqestVo.addProperty("GIS_ID", GIS_ID);
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
        }else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.singleEstimateWithFdrs(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);
                //loader.setVisibility(View.GONE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);

                    // Check if the STATUS field exists and is not null
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();

                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            getLatLongs();

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();

                                if (!object.get("STATUS").isJsonNull()) {
                                    if (object.get("STATUS").getAsString().equals(type)) {

                                        if (SurveyList.IS_EDIT) {
                                            if ("ESTIMATES".equalsIgnoreCase(LoginActivity.SURVEY_FROM) || "SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                                if (object.get("EST_NO").getAsString().equalsIgnoreCase(SurveyList.EDIT_ESTIMATE_NO)) {
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
                                                    model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                                    model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                                    model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");
                                                    model.setEST_TYPE(object.has("EST_TYPE") && !object.get("EST_TYPE").isJsonNull() ? object.get("EST_TYPE").getAsString() : "");
                                                    model.setCOUNT_33KV(object.get("COUNT_33KV").getAsInt());
                                                    model.setCOUNT_11KV(object.get("COUNT_11KV").getAsInt());
                                                    model.setCOUNT_LT(object.get("COUNT_LT").getAsInt());
                                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                        model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                                        model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                                    }

                                                    model.setNUM_33KV(object.get("NUM_33KV").getAsInt());
                                                    model.setNUM_11KV(object.get("NUM_11KV").getAsInt());
                                                    model.setNUM_LT(object.get("NUM_LT").getAsInt());


                                                    model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                                    model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");

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
                                                            fdrList.add(fdrModel);
                                                        }
                                                        model.setFDR_PARTICULARS(fdrList);
                                                    }

                                                    list.add(model);
                                                    //NEW API CODE
                                                    clickContinueBtnAction(model, pos);

                                                    if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                                        getThirtythreekvfeedermaster(model.getSEC_ID(), model.getEST_NO(), model);
                                                    } else {
                                                        CommanUtilits.internetDialog(EstimatesHome.this);
                                                    }
                                                    // getThirtythreekvfeedermaster(model.getSEC_ID(), model.getEST_NO(), model);
                                                    //  getNewFeederMasterAPI(model.getSEC_ID(), model.getEST_NO(), model);
                                                }
                                            } else {
                                                //20-02-2025 Chandu Changes
                                                if (object.get("GIS_ID").getAsString().equalsIgnoreCase(SurveyList.EDIT_GIS_ID)) {
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
                                                    model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                                    model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                                    model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");
                                                    model.setEST_TYPE(object.has("EST_TYPE") && !object.get("EST_TYPE").isJsonNull() ? object.get("EST_TYPE").getAsString() : "");
                                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                        model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                                        model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                                    }
                                                    model.setCOUNT_33KV(object.get("COUNT_33KV").getAsInt());
                                                    model.setCOUNT_11KV(object.get("COUNT_11KV").getAsInt());
                                                    model.setCOUNT_LT(object.get("COUNT_LT").getAsInt());

                                                    model.setNUM_33KV(object.get("NUM_33KV").getAsInt());
                                                    model.setNUM_11KV(object.get("NUM_11KV").getAsInt());
                                                    model.setNUM_LT(object.get("NUM_LT").getAsInt());


                                                    model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                                    model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");

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

                                                            fdrList.add(fdrModel);
                                                        }
                                                        model.setFDR_PARTICULARS(fdrList);
                                                    }

                                                    list.add(model);
                                                    clickContinueBtnAction(model, pos);
                                                    if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                                        getThirtythreekvfeedermaster(model.getSEC_ID(), model.getEST_NO(), model);
                                                    } else {
                                                        CommanUtilits.internetDialog(EstimatesHome.this);
                                                    }
                                                    // getThirtythreekvfeedermaster(model.getSEC_ID(), model.getEST_NO(), model);
                                                }
                                            }
                                        } else {
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
                                            model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                            model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");
                                            model.setEST_TYPE(object.has("EST_TYPE") && !object.get("EST_TYPE").isJsonNull() ? object.get("EST_TYPE").getAsString() : "");
                                            model.setRE_SURVEY(object.has("RE_SURVEY") && !object.get("RE_SURVEY").isJsonNull() ? object.get("RE_SURVEY").getAsString() : "");
                                            model.setREVISED(object.has("REVISED") && !object.get("REVISED").isJsonNull() ? object.get("REVISED").getAsString() : "");
                                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");
                                                model.setRDSS_TYPE(object.has("RDSS_TYPE") && !object.get("RDSS_TYPE").isJsonNull() ? object.get("RDSS_TYPE").getAsString() : "");
                                            }
                                            model.setCOUNT_33KV(object.get("COUNT_33KV").getAsInt());
                                            model.setCOUNT_11KV(object.get("COUNT_11KV").getAsInt());
                                            model.setCOUNT_LT(object.get("COUNT_LT").getAsInt());

                                            model.setNUM_33KV(object.get("NUM_33KV").getAsInt());
                                            model.setNUM_11KV(object.get("NUM_11KV").getAsInt());
                                            model.setNUM_LT(object.get("NUM_LT").getAsInt());

                                            model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                            model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");

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

                                                    fdrList.add(fdrModel);
                                                }
                                                model.setFDR_PARTICULARS(fdrList);
                                            }

                                            list.add(model);

                                            //NEW API CODE
                                            clickContinueBtnAction(model, pos);
                                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                                getThirtythreekvfeedermaster(model.getSEC_ID(), model.getEST_NO(), model);
                                            } else {
                                                CommanUtilits.internetDialog(EstimatesHome.this);
                                            }
                                            //getThirtythreekvfeedermaster(model.getSEC_ID(), model.getEST_NO(), model);
                                            //  getNewFeederMasterAPI(model.getSEC_ID(), model.getEST_NO(), model);
                                        }


                                    }
                                }


                            }

                            //  setupLocationServices();
                            //  clickContinueBtnAction(model,pos);

                        } else {
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(EstimatesHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // Handle case where STATUS is missing or null
                        Toast.makeText(EstimatesHome.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EstimatesHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(EstimatesHome.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getEstimationData(String est_no, PurposeModel model) {
        ESTIMATION_NUMBER = est_no;
        GIS_ID = model.getGIS_ID();
        // Log.d("TAG", "ESTIMATION_NUMBER: " + ESTIMATION_NUMBER);
        loader_layout.setVisibility(View.VISIBLE);
        if ("SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            tv_progress_bar.setText("Downloading Solar Dpr Pole/Location Data");
        } else {
            tv_progress_bar.setText("Downloading Estimates Pole/Location Data");
        }

        main_ui_layout.setVisibility(View.GONE);
        //progressDialog.show();
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        String EST_NO = est_no;
        String GIS_ID = model.getGIS_ID();
        String estKey = "\"" + est_no + "\"";
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("EST_NO", EST_NO);
            jsonObject.addProperty("GIS_ID", model.getGIS_ID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.d("JsonObject--->", "getEstimationData: " + jsonObject);
        String jsonString = jsonObject.toString();
        Call<JsonObject> responseVoCall = retrofitService.getEstData(jsonObject);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                //progressDialog.hide();
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (!SurveyList.IS_EDIT) {
                        boolean clearStatus = databaseEstimates.clearEstimatesTable();
                        if (clearStatus) {
                            // Log.v("DB==", "Table cleared successfully");
                        } else {
                            // Log.v("DB==", "Failed to clear table");
                        }
                    }
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonObject dataObject = surveyedResponseVo.getAsJsonObject("DATA");
                            if ("WORK_ORDER".equalsIgnoreCase(LoginActivity.SURVEY_FROM) || LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                                if (dataObject.has(GIS_ID)) {

                                    JsonObject estDataObject = dataObject.getAsJsonObject(GIS_ID);
                                    //  Log.e("getEstimationData--->", "Estimation data: " + est_no);
                                    // Log.d("SIZE OF DATA", "LT: " + estDataObject.getAsJsonArray("LT").size());
                                    // Log.d("SIZE OF DATA", "11KV: " + estDataObject.getAsJsonArray("11KV").size());
                                    // Log.d("SIZE OF DATA", "33KV: " + estDataObject.getAsJsonArray("33KV").size());
                                    // Log.d("SIZE OF DATA", "LOC: " + estDataObject.getAsJsonArray("LOC").size());

                                    int kv33 = estDataObject.getAsJsonArray("33KV").size();
                                    int kv11 = estDataObject.getAsJsonArray("11KV").size();
                                    int lt = estDataObject.getAsJsonArray("LT").size();
                                    if (kv33 == 0) {
                                        boolean update33KV = databaseEstimates.update33kvSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                        // Log.d("33KV_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                    }
                                    if (kv11 == 0) {
                                        boolean update33KV = databaseEstimates.update11kvSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                        // Log.d("11KV_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                    }
                                    if (lt == 0) {
                                        boolean update33KV = databaseEstimates.updateLTSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                        // Log.d("LT_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                    }


                                    processArrayLT(estDataObject.getAsJsonArray("LT"), "LT", estDataObject.getAsJsonArray("LOC"), model);
                                    processArray33KV(estDataObject.getAsJsonArray("33KV"), "33KV", estDataObject.getAsJsonArray("LOC"), model);
                                    processArray11KV(estDataObject.getAsJsonArray("11KV"), "11KV", estDataObject.getAsJsonArray("LOC"), model);
                                    //processArray11KV(estDataObject.getAsJsonArray("11KV"), "11KV", estDataObject.getAsJsonArray("LOC"), model);
                                    processArraySOLARDPR(estDataObject.getAsJsonArray("SOLARDPR"), "11KV", estDataObject.getAsJsonArray("LOC"), model);
                                    // processArrayLOC(estDataObject.getAsJsonArray("LOC"), "33KV", estDataObject.getAsJsonArray("LOC"), model);





                                    if (model.getSTATUS().equalsIgnoreCase("1")) {

                                        Intent intent = new Intent(EstimatesHome.this, PrepareEstimate.class);
                                        intent.putExtra("EST_NO", model.getEST_NO());
                                        intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                        intent.putExtra("PURPOSE", model.getPURPOSE());
                                        intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                        intent.putExtra("GIS_ID", model.getGIS_ID());
                                        intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                        intent.putExtra("is33kvSelected", model.getLINE_33KV());
                                        intent.putExtra("is11kvSelected", model.getLINE_11KV());
                                        intent.putExtra("isLtSelected", model.getLINE_LT());
                                        intent.putExtra("isDTRSelected", model.getDTR());
                                        intent.putExtra("isSSSelected", model.getSS());
                                        intent.putExtra("isMeterSelected", model.getMETERS());
                                        intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                        intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                        intent.putExtra("FROM", "ESTIMATE_HOME");
                                        intent.putExtra("WO_OLD", model.getWO_OLD());
                                        intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                            intent.putExtra("FEEDERS", model.getFEEDERS());
                                            intent.putExtra("RDSS_TYPE", model.getRDSS_TYPE());
                                        }
                                        intent.putExtra("Model", model);
                                        ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(model.getEST_NO(), "ALL", true, GIS_ID);
                                        //  Log.d("TAG", "TOTAL DATA SIZE: " + polesData.size());
                                        // Log.d("TAG", "EST NO: " + model.getEST_NO());
                                        intent.putExtra("Model", model);
                                        startActivity(intent);
                                    } else if (model.getSTATUS().equalsIgnoreCase("2")) {

                                        if (model.getMETERS().equalsIgnoreCase("Y")) {
                                            Intent intent = new Intent(EstimatesHome.this, WorkInvolmentsActivity.class);
                                            intent.putExtra("EST_NO", model.getEST_NO());
                                            intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                            intent.putExtra("PURPOSE", model.getPURPOSE());
                                            intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                            intent.putExtra("GIS_ID", model.getGIS_ID());
                                            intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                            intent.putExtra("is33kvSelected", "Y".equalsIgnoreCase(model.getLINE_33KV()));
                                            intent.putExtra("is11kvSelected", "Y".equalsIgnoreCase(model.getLINE_11KV()));
                                            intent.putExtra("isLtSelected", "Y".equalsIgnoreCase(model.getLINE_LT()));
                                            intent.putExtra("isDTRSelected", model.getDTR());
                                            intent.putExtra("isSSSelected", model.getSS());
                                            intent.putExtra("isMeterSelected", "Y".equalsIgnoreCase(model.getMETERS()));
                                            intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                            intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                            intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                            intent.putExtra("FROM", "ESTIMATE_HOME");
                                            intent.putExtra("WO_OLD", model.getWO_OLD());
                                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                intent.putExtra("FEEDERS", model.getFEEDERS());
                                            }
                                            intent.putExtra("Model", model);
                                            startActivity(intent);

                                        } else {
                                            int maxFdrIndex11KV = -1;
                                            if (model.getFDR_PARTICULARS() != null) {
                                                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                                                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                                                        int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
                                                        if (currentFdrIndex > maxFdrIndex11KV) {
                                                            maxFdrIndex11KV = currentFdrIndex;
                                                        }
                                                    }
                                                }
                                            }
                                            Intent intent = new Intent(EstimatesHome.this, WorkInvolmentsActivity.class);
                                            intent.putExtra("EST_NO", model.getEST_NO());
                                            intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                            intent.putExtra("PURPOSE", model.getPURPOSE());
                                            intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                            intent.putExtra("GIS_ID", model.getGIS_ID());
                                            intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                            intent.putExtra("is33kvSelected", "Y".equalsIgnoreCase(model.getLINE_33KV()));
                                            intent.putExtra("is11kvSelected", "Y".equalsIgnoreCase(model.getLINE_11KV()));
                                            intent.putExtra("isLtSelected", "Y".equalsIgnoreCase(model.getLINE_LT()));
                                            intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                            intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                            intent.putExtra("FROM", "ESTIMATE_HOME");
                                            intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                            intent.putExtra("Feeder_type33KV", Feeder_type33KV);
                                            intent.putExtra("Feeder_type11KV", Feeder_type11KV);
                                            intent.putExtra("WO_OLD", model.getWO_OLD());
                                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                intent.putExtra("FEEDERS", model.getFEEDERS());
                                                intent.putExtra("RDSS_TYPE", model.getRDSS_TYPE());
                                            }
                                            intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
                                            ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(model.getEST_NO(), "ALL", true, GIS_ID);
                                            // Log.d("TAG", "TOTAL DATA SIZE: " + polesData.size());
                                            //Log.d("TAG", "EST NO: " + model.getEST_NO());
                                            intent.putExtra("Model", model);
                                            startActivity(intent);
                                        }

                                    }


                                    //  processArray(estDataObject.getAsJsonArray("LOC"));


                                } else {
                                    // Log.e("getEstimationData", "Estimation data not found for: " + est_no);

                                }
                            } else if ("SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                                if (dataObject.has(GIS_ID)) {

                                    JsonObject estDataObject = dataObject.getAsJsonObject(GIS_ID);
                                    //  Log.e("getEstimationData--->", "Estimation data: " + est_no);
                                    // Log.d("SIZE OF DATA", "LT: " + estDataObject.getAsJsonArray("LT").size());
                                    // Log.d("SIZE OF DATA", "11KV: " + estDataObject.getAsJsonArray("11KV").size());
                                    // Log.d("SIZE OF DATA", "33KV: " + estDataObject.getAsJsonArray("33KV").size());
                                    // Log.d("SIZE OF DATA", "LOC: " + estDataObject.getAsJsonArray("LOC").size());

                                    int kv33 = estDataObject.getAsJsonArray("33KV").size();
                                    int kv11 = estDataObject.getAsJsonArray("11KV").size();
                                    int lt = estDataObject.getAsJsonArray("LT").size();
                                    if (kv33 == 0) {
                                        boolean update33KV = databaseEstimates.update33kvSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                        // Log.d("33KV_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                    }
                                    if (kv11 == 0) {
                                        boolean update33KV = databaseEstimates.update11kvSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                        // Log.d("11KV_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                    }
                                    if (lt == 0) {
                                        boolean update33KV = databaseEstimates.updateLTSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                        // Log.d("LT_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                    }


                                    processArrayLT(estDataObject.getAsJsonArray("LT"), "LT", estDataObject.getAsJsonArray("LOC"), model);
                                    processArray33KV(estDataObject.getAsJsonArray("33KV"), "33KV", estDataObject.getAsJsonArray("LOC"), model);
                                    processArray11KV(estDataObject.getAsJsonArray("11KV"), "11KV", estDataObject.getAsJsonArray("LOC"), model);
                                    processArraySOLARDPR(estDataObject.getAsJsonArray("SOLARDPR"), "11KV", estDataObject.getAsJsonArray("LOC"), model);

                                    // processArrayLOC(estDataObject.getAsJsonArray("LOC"), "33KV", estDataObject.getAsJsonArray("LOC"), model);





                                    if (model.getSTATUS().equalsIgnoreCase("1")) {

                                        Intent intent = new Intent(EstimatesHome.this, PrepareEstimate.class);
                                        intent.putExtra("EST_NO", model.getEST_NO());
                                        intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                        intent.putExtra("PURPOSE", model.getPURPOSE());
                                        intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                        intent.putExtra("GIS_ID", model.getGIS_ID());
                                        intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                        intent.putExtra("is33kvSelected", model.getLINE_33KV());
                                        intent.putExtra("is11kvSelected", model.getLINE_11KV());
                                        intent.putExtra("isLtSelected", model.getLINE_LT());
                                        intent.putExtra("isDTRSelected", model.getDTR());
                                        intent.putExtra("isSSSelected", model.getSS());
                                        intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                        intent.putExtra("isMeterSelected", model.getMETERS());
                                        intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                        intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                        intent.putExtra("FROM", "ESTIMATE_HOME");
                                        intent.putExtra("WO_OLD", model.getWO_OLD());
                                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                            intent.putExtra("FEEDERS", model.getFEEDERS());
                                            intent.putExtra("RDSS_TYPE", model.getRDSS_TYPE());
                                        }
                                        intent.putExtra("Model", model);
                                        ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(model.getEST_NO(), "ALL", true, GIS_ID);
                                        //  Log.d("TAG", "TOTAL DATA SIZE: " + polesData.size());
                                        // Log.d("TAG", "EST NO: " + model.getEST_NO());
                                        intent.putExtra("Model", model);
                                        startActivity(intent);
                                    } else if (model.getSTATUS().equalsIgnoreCase("2")) {

                                        if (model.getMETERS().equalsIgnoreCase("Y")) {
                                            Intent intent = new Intent(EstimatesHome.this, WorkInvolmentsActivity.class);
                                            intent.putExtra("EST_NO", model.getEST_NO());
                                            intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                            intent.putExtra("PURPOSE", model.getPURPOSE());
                                            intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                            intent.putExtra("GIS_ID", model.getGIS_ID());
                                            intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                            intent.putExtra("is33kvSelected", "Y".equalsIgnoreCase(model.getLINE_33KV()));
                                            intent.putExtra("is11kvSelected", "Y".equalsIgnoreCase(model.getLINE_11KV()));
                                            intent.putExtra("isLtSelected", "Y".equalsIgnoreCase(model.getLINE_LT()));
                                            intent.putExtra("isDTRSelected", model.getDTR());
                                            intent.putExtra("isSSSelected", model.getSS());
                                            intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                            intent.putExtra("isMeterSelected", "Y".equalsIgnoreCase(model.getMETERS()));
                                            intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                            intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                            intent.putExtra("FROM", "ESTIMATE_HOME");
                                            intent.putExtra("WO_OLD", model.getWO_OLD());
                                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                intent.putExtra("FEEDERS", model.getFEEDERS());
                                                intent.putExtra("RDSS_TYPE", model.getRDSS_TYPE());
                                            }
                                            intent.putExtra("Model", model);
                                            startActivity(intent);

                                        } else {
                                            int maxFdrIndex11KV = -1;
                                            if (model.getFDR_PARTICULARS() != null) {
                                                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                                                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                                                        int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
                                                        if (currentFdrIndex > maxFdrIndex11KV) {
                                                            maxFdrIndex11KV = currentFdrIndex;
                                                        }
                                                    }
                                                }
                                            }
                                            Intent intent = new Intent(EstimatesHome.this, WorkInvolmentsActivity.class);
                                            intent.putExtra("EST_NO", model.getEST_NO());
                                            intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                            intent.putExtra("PURPOSE", model.getPURPOSE());
                                            intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                            intent.putExtra("GIS_ID", model.getGIS_ID());
                                            intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                            intent.putExtra("is33kvSelected", "Y".equalsIgnoreCase(model.getLINE_33KV()));
                                            intent.putExtra("is11kvSelected", "Y".equalsIgnoreCase(model.getLINE_11KV()));
                                            intent.putExtra("isLtSelected", "Y".equalsIgnoreCase(model.getLINE_LT()));
                                            intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                            intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                            intent.putExtra("FROM", "ESTIMATE_HOME");
                                            intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                            intent.putExtra("Feeder_type33KV", Feeder_type33KV);
                                            intent.putExtra("Feeder_type11KV", Feeder_type11KV);
                                            intent.putExtra("WO_OLD", model.getWO_OLD());
                                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                                intent.putExtra("FEEDERS", model.getFEEDERS());
                                                intent.putExtra("RDSS_TYPE", model.getRDSS_TYPE());
                                            }
                                            intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
                                            ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(model.getEST_NO(), "ALL", true, GIS_ID);
                                            // Log.d("TAG", "TOTAL DATA SIZE: " + polesData.size());
                                            //Log.d("TAG", "EST NO: " + model.getEST_NO());
                                            intent.putExtra("Model", model);
                                            startActivity(intent);
                                        }

                                    }


                                    //  processArray(estDataObject.getAsJsonArray("LOC"));


                                }
                            } else if (dataObject.has(est_no)) {
                                JsonObject estDataObject = dataObject.getAsJsonObject(est_no);
                                //  Log.e("getEstimationData--->", "Estimation data: " + est_no);
                                // Log.d("SIZE OF DATA", "LT: " + estDataObject.getAsJsonArray("LT").size());
                                // Log.d("SIZE OF DATA", "11KV: " + estDataObject.getAsJsonArray("11KV").size());
                                // Log.d("SIZE OF DATA", "33KV: " + estDataObject.getAsJsonArray("33KV").size());
                                // Log.d("SIZE OF DATA", "LOC: " + estDataObject.getAsJsonArray("LOC").size());

                                int kv33 = estDataObject.getAsJsonArray("33KV").size();
                                int kv11 = estDataObject.getAsJsonArray("11KV").size();
                                int lt = estDataObject.getAsJsonArray("LT").size();
                                if (kv33 == 0) {
                                    boolean update33KV = databaseEstimates.update33kvSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                    // Log.d("33KV_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                }
                                if (kv11 == 0) {
                                    boolean update33KV = databaseEstimates.update11kvSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                    // Log.d("11KV_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                }
                                if (lt == 0) {
                                    boolean update33KV = databaseEstimates.updateLTSurveyforReSurveyStatusNEW(EST_NO, false, GIS_ID);
                                    // Log.d("LT_COMPLETE_STATUS-->", "onResponse: " + update33KV);
                                }


                                processArrayLT(estDataObject.getAsJsonArray("LT"), "LT", estDataObject.getAsJsonArray("LOC"), model);
                                processArray33KV(estDataObject.getAsJsonArray("33KV"), "33KV", estDataObject.getAsJsonArray("LOC"), model);
                                processArray11KV(estDataObject.getAsJsonArray("11KV"), "11KV", estDataObject.getAsJsonArray("LOC"), model);
                                processArraySOLARDPR(estDataObject.getAsJsonArray("SOLARDPR"), "11KV", estDataObject.getAsJsonArray("LOC"), model);



                                if (model.getSTATUS().equalsIgnoreCase("1")) {

                                    Intent intent = new Intent(EstimatesHome.this, PrepareEstimate.class);
                                    intent.putExtra("EST_NO", model.getEST_NO());
                                    intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                    intent.putExtra("PURPOSE", model.getPURPOSE());
                                    intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                    intent.putExtra("GIS_ID", model.getGIS_ID());
                                    intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                    intent.putExtra("is33kvSelected", model.getLINE_33KV());
                                    intent.putExtra("is11kvSelected", model.getLINE_11KV());
                                    intent.putExtra("isLtSelected", model.getLINE_LT());
                                    intent.putExtra("isDTRSelected", model.getDTR());
                                    intent.putExtra("isSSSelected", model.getSS());
                                    intent.putExtra("isMeterSelected", model.getMETERS());
                                    intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                    intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                    intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                        intent.putExtra("FEEDERS", model.getFEEDERS());
                                        intent.putExtra("RDSS_TYPE", model.getRDSS_TYPE());
                                    }
                                    intent.putExtra("FROM", "ESTIMATE_HOME");
                                    intent.putExtra("Model", model);
                                    ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(model.getEST_NO(), "ALL", true, GIS_ID);
                                    //  Log.d("TAG", "TOTAL DATA SIZE: " + polesData.size());
                                    // Log.d("TAG", "EST NO: " + model.getEST_NO());
                                    intent.putExtra("Model", model);
                                    startActivity(intent);
                                } else if (model.getSTATUS().equalsIgnoreCase("2")) {

                                    if (model.getMETERS().equalsIgnoreCase("Y")) {
                                        Intent intent = new Intent(EstimatesHome.this, WorkInvolmentsActivity.class);
                                        intent.putExtra("EST_NO", model.getEST_NO());
                                        intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                        intent.putExtra("PURPOSE", model.getPURPOSE());
                                        intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                        intent.putExtra("GIS_ID", model.getGIS_ID());
                                        intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                        intent.putExtra("is33kvSelected", "Y".equalsIgnoreCase(model.getLINE_33KV()));
                                        intent.putExtra("is11kvSelected", "Y".equalsIgnoreCase(model.getLINE_11KV()));
                                        intent.putExtra("isLtSelected", "Y".equalsIgnoreCase(model.getLINE_LT()));
                                        intent.putExtra("isDTRSelected", model.getDTR());
                                        intent.putExtra("isSSSelected", model.getSS());
                                        intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                        intent.putExtra("isMeterSelected", "Y".equalsIgnoreCase(model.getMETERS()));
                                        intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                        intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                        intent.putExtra("FROM", "ESTIMATE_HOME");
                                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                            intent.putExtra("FEEDERS", model.getFEEDERS());
                                            intent.putExtra("RDSS_TYPE", model.getRDSS_TYPE());
                                        }
                                        intent.putExtra("Model", model);
                                        startActivity(intent);

                                    } else {
                                        int maxFdrIndex11KV = -1;
                                        if (model.getFDR_PARTICULARS() != null) {
                                            for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                                                if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                                                    int currentFdrIndex = model.getFDR_PARTICULARS().get(i).getFDR_INDEX();
                                                    if (currentFdrIndex > maxFdrIndex11KV) {
                                                        maxFdrIndex11KV = currentFdrIndex;
                                                    }
                                                }
                                            }
                                        }
                                        Intent intent = new Intent(EstimatesHome.this, WorkInvolmentsActivity.class);
                                        intent.putExtra("EST_NO", model.getEST_NO());
                                        intent.putExtra("EST_NATURE", model.getEST_NATURE());
                                        intent.putExtra("PURPOSE", model.getPURPOSE());
                                        intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
                                        intent.putExtra("GIS_ID", model.getGIS_ID());
                                        intent.putExtra("REG_COUNT", model.getREG_COUNT());
                                        intent.putExtra("is33kvSelected", "Y".equalsIgnoreCase(model.getLINE_33KV()));
                                        intent.putExtra("is11kvSelected", "Y".equalsIgnoreCase(model.getLINE_11KV()));
                                        intent.putExtra("isLtSelected", "Y".equalsIgnoreCase(model.getLINE_LT()));
                                        intent.putExtra("MAX_LENGTH", model.getMAX_LENGTH());
                                        intent.putExtra("TOWER_LENGTH", model.getMAX_LENGTH());
                                        intent.putExtra("FROM", "ESTIMATE_HOME");
                                        intent.putExtra("EST_TYPE", model.getEST_TYPE());
                                        intent.putExtra("Feeder_type33KV", Feeder_type33KV);
                                        intent.putExtra("Feeder_type11KV", Feeder_type11KV);
                                        intent.putExtra("LASTFDRINDEX", String.valueOf(maxFdrIndex11KV));
                                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                            intent.putExtra("FEEDERS", model.getFEEDERS());
                                            intent.putExtra("RDSS_TYPE", model.getRDSS_TYPE());
                                        }
                                        ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(model.getEST_NO(), "ALL", true, GIS_ID);
                                        // Log.d("TAG", "TOTAL DATA SIZE: " + polesData.size());
                                        //Log.d("TAG", "EST NO: " + model.getEST_NO());
                                        intent.putExtra("Model", model);
                                        startActivity(intent);
                                    }

                                }


                                //  processArray(estDataObject.getAsJsonArray("LOC"));


                            } else {
                                // Log.e("getEstimationData", "Estimation data not found for: " + est_no);

                            }
                            // JsonObject jsonObject = dataObject.getAsJsonObject("VZ1-0-00347");


                        }
                    } else {
                        Toast.makeText(EstimatesHome.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                } else {
                    Toast.makeText(EstimatesHome.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void getPoleTypeData(String est_no, PurposeModel model) {

        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading Material Master");
        main_ui_layout.setVisibility(View.GONE);
        //loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getPoleTypes();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                //loader.setVisibility(View.GONE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    boolean clearStatus = databaseEstimates.clearPoleHeightTable();
                    if (clearStatus) {
                        // Log.v("DB==", "Table cleared successfully");
                    } else {
                        // Log.v("DB==", "Failed to clear table");
                    }
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");


                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                PoleHeightModel model = new PoleHeightModel();
                                model.setMATERIALPART1(object.has("MATERIALPART1") && !object.get("MATERIALPART1").isJsonNull() ? object.get("MATERIALPART1").getAsString() : "");
                                model.setMATERIALCODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull() ? object.get("MATERIALCODE").getAsString() : "");
                                model.setGROUPDESC1(object.has("GROUPDESC1") && !object.get("GROUPDESC1").isJsonNull() ? object.get("GROUPDESC1").getAsString() : "");
                                model.setMATERIAL_TYPE(object.has("MATERIAL_TYPE") && !object.get("MATERIAL_TYPE").isJsonNull() ? object.get("MATERIAL_TYPE").getAsString() : "");
                                model.setGROUPCODE(object.has("GROUPCODE") && !object.get("GROUPCODE").isJsonNull() ? object.get("GROUPCODE").getAsString() : "");
                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull() ? object.get("UOM").getAsString() : "");
                                model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                model.setPOLE_HEIGHT(object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : "");
                                poleHeightModels.add(model);
                                boolean Status = databaseEstimates.addPoleHeightData(model);
//                                if (Status) {
//                                    Toast.makeText(EstimatesHome.this, "Data Added Success Fully", Toast.LENGTH_SHORT).show();
//                                }
                            }

                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {

                                getConductorTypeData(est_no, model);
                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }
                            // getConductorTypeData(est_no, model);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void getConductorTypeData(String est_no, PurposeModel model) {

        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading Material Master");
        main_ui_layout.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);


        Call<JsonObject> responseVoCall = retrofitService.getCableTypes();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                //loader.setVisibility(View.GONE);
                if (response != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    boolean clearStatus = databaseEstimates.clearConductorTypeTable();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            getHTDTRMaterails(est_no, model);
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");


                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                ConductorTypeModel model = new ConductorTypeModel();
                                model.setMATERIALPART1(object.has("MATERIALPART1") && !object.get("MATERIALPART1").isJsonNull() ? object.get("MATERIALPART1").getAsString() : "");
                                model.setMATERIALCODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull() ? object.get("MATERIALCODE").getAsString() : "");
                                model.setGROUPDESC1(object.has("GROUPDESC1") && !object.get("GROUPDESC1").isJsonNull() ? object.get("GROUPDESC1").getAsString() : "");
                                model.setMATERIAL_TYPE(object.has("MATERIAL_TYPE") && !object.get("MATERIAL_TYPE").isJsonNull() ? object.get("MATERIAL_TYPE").getAsString() : "");
                                model.setGROUPCODE(object.has("GROUPCODE") && !object.get("GROUPCODE").isJsonNull() ? object.get("GROUPCODE").getAsString() : "");
                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull() ? object.get("UOM").getAsString() : "");
                                model.setCABLE_TYPE(object.has("CABLE_TYPE") && !object.get("CABLE_TYPE").isJsonNull() ? object.get("CABLE_TYPE").getAsString() : "");
                                model.setCORE(object.has("CORE") && !object.get("CORE").isJsonNull() ? object.get("CORE").getAsString() : "");
                                model.setCABLE_SIZE(object.has("CABLE_SIZE") && !object.get("CABLE_SIZE").isJsonNull() ? object.get("CABLE_SIZE").getAsString() : "");
                                model.setVOLTAGE_LEVEL(object.has("VOLTAGE_LEVEL") && !object.get("VOLTAGE_LEVEL").isJsonNull() ? object.get("VOLTAGE_LEVEL").getAsString() : "");
                                model.setHT_11KV(object.has("HT_11KV") && !object.get("HT_11KV").isJsonNull() ? object.get("HT_11KV").getAsString() : "");
                                model.setHT_33KV(object.has("HT_33KV") && !object.get("HT_33KV").isJsonNull() ? object.get("HT_33KV").getAsString() : "");
                                model.setLT(object.has("LT") && !object.get("LT").isJsonNull() ? object.get("LT").getAsString() : "");
                                conductorTypeModels.add(model);
                                boolean Status = databaseEstimates.addConductorTypeData(model);
//                                if (Status) {
//                                    Toast.makeText(EstimatesHome.this, "Data Added Success Fully", Toast.LENGTH_SHORT).show();
//                                }
                            }
                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {

                                getDTRTypeData(est_no, model);
                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }
                            // getDTRTypeData(est_no, model);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void getHTDTRMaterails(String est_no, PurposeModel model) {
        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading HT & DTR Material Master");
        main_ui_layout.setVisibility(View.GONE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getHTWiseMaterials();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);
                //loader.setVisibility(View.GONE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    boolean clearStatus = databaseEstimates.clearDTR_HT();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");


                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                DTR_HT_MaterialModel model = new DTR_HT_MaterialModel();
                                model.setMATERIALCODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull() ? object.get("MATERIALCODE").getAsString() : "");
                                model.setMATERAIL_DESC(object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull() ? object.get("MATERAIL_DESC").getAsString() : "");
                                model.setGROUPCODE(object.has("GROUPCODE") && !object.get("GROUPCODE").isJsonNull() ? object.get("GROUPCODE").getAsString() : "");
                                model.setPHASE(object.has("PHASE") && !object.get("PHASE").isJsonNull() ? object.get("PHASE").getAsString() : "");
                                model.setCAPACITY(object.has("CAPACITY") && !object.get("CAPACITY").isJsonNull() ? object.get("CAPACITY").getAsString() : "");
                                model.setMATERIAL_TYPE(object.has("MATERIAL_TYPE") && !object.get("MATERIAL_TYPE").isJsonNull() ? object.get("MATERIAL_TYPE").getAsString() : "");
                                model.setGROUPDESC1(object.has("GROUPDESC1") && !object.get("GROUPDESC1").isJsonNull() ? object.get("GROUPDESC1").getAsString() : "");
                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull() ? object.get("UOM").getAsString() : "");
                                model.setId(object.has("id") && !object.get("id").isJsonNull() ? object.get("id").getAsString() : "");
                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull() ? object.get("SURVEY_CODE").getAsString() : "");

                                dtrHtMaterialModels.add(model);
                                boolean Status = databaseEstimates.addDTR_HTMaterialData(model);
//                                if (Status) {
//                                    Toast.makeText(EstimatesHome.this, "Data Added Success Fully", Toast.LENGTH_SHORT).show();
//                                }
                            }
                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                getDTRCapacities(est_no, model);
                                getDTRTypeData(est_no, model);
                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }
                            // getDTRTypeData(est_no, model);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }


    private void getDTRCapacities(String est_no, PurposeModel purposeModel) {

        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading DTR Phase & Capacity Details");
        main_ui_layout.setVisibility(View.GONE);
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("MODULE",LoginActivity.SURVEY_FROM);

        List<LoginDataResponse> loginDataResponses =
                new ArcGISDatabase(this).getUserMasterData();

        retrofitService = RetrofitClientInstanceEstimates
                .getRetrofitInstance(
                        loginDataResponses.get(0).getACCESS_TOKEN()
                )
                .create(RetrofitServiceEstimates.class);

        Call<JsonObject> responseVoCall = retrofitService.getCapacities(jsonObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(
                    Call<JsonObject> call,
                    Response<JsonObject> response) {

                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);

                if (!response.isSuccessful() || response.body() == null) {

                    return;
                }

                JsonObject surveyedResponseVo = response.body();

                try {

                    // STATUS
                    if (!surveyedResponseVo.has("STATUS")
                            || surveyedResponseVo.get("STATUS").isJsonNull()) {
                        return;
                    }

                    boolean status =
                            surveyedResponseVo.get("STATUS").getAsBoolean();

                    if (!status) {

                        return;
                    }


                    boolean clearStatus = databaseEstimates.clearDTR_Capacities();


                    if (!surveyedResponseVo.has("DATA")
                            || surveyedResponseVo.get("DATA").isJsonNull()) {
                        return;
                    }

                    JsonObject dataObject =
                            surveyedResponseVo.getAsJsonObject("DATA");


                    JsonArray threePhaseArray =
                            dataObject.getAsJsonArray("III");


                    JsonArray singlePhaseArray =
                            dataObject.getAsJsonArray("I");


                    ArrayList<DTRCapacities> dtrCapacitiesList =
                            new ArrayList<>();


                    if (threePhaseArray != null) {

                        for (int i = 0;
                             i < threePhaseArray.size();
                             i++) {

                            String capacity =
                                    threePhaseArray
                                            .get(i)
                                            .getAsString();

                            DTRCapacities dtrModel =
                                    new DTRCapacities();

                            dtrModel.setPhase("III");
                            dtrModel.setCapacity(capacity);

                            dtrCapacitiesList.add(dtrModel);


                            //Log.e("DTR_CAPACITY========>", "III => " + capacity);
                            databaseEstimates.addDtrPhaseCapacities(dtrModel);

                        }
                    }


                    if (singlePhaseArray != null) {

                        for (int i = 0;
                             i < singlePhaseArray.size();
                             i++) {

                            String capacity =
                                    singlePhaseArray
                                            .get(i)
                                            .getAsString();

                            DTRCapacities dtrModel =
                                    new DTRCapacities();

                            dtrModel.setPhase("I");
                            dtrModel.setCapacity(capacity);

                            dtrCapacitiesList.add(dtrModel);



                            databaseEstimates.addDtrPhaseCapacities(dtrModel);
                        }
                    }


                    // ============================
                    // NEXT API
                    // ============================

                    if (ConnectivityChecking.checkConnection(
                            EstimatesHome.this)) {

                        getDTRTypeData(est_no, purposeModel);

                    } else {

                        CommanUtilits.internetDialog(
                                EstimatesHome.this);
                    }

                } catch (Exception e) {


                }
            }

            @Override
            public void onFailure(
                    Call<JsonObject> call,
                    Throwable t) {

                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);

            }
        });
    }


    private void getDTRTypeData(String est_no, PurposeModel model) {

        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading Material Master");
        main_ui_layout.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getDTRTypes();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                //  loader.setVisibility(View.GONE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    boolean clearStatus = databaseEstimates.clearDTRTypeTable();
                    if (clearStatus) {
                        //  Log.v("DB==", "Table cleared successfully");
                    } else {
                        //  Log.v("DB==", "Failed to clear table");
                    }
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                DTRTypesModel model = new DTRTypesModel();
                                model.setMATERIALPART1(object.has("MATERIALPART1") && !object.get("MATERIALPART1").isJsonNull() ? object.get("MATERIALPART1").getAsString() : "");
                                model.setMATERIALCODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull() ? object.get("MATERIALCODE").getAsString() : "");
                                model.setGROUPDESC1(object.has("GROUPDESC1") && !object.get("GROUPDESC1").isJsonNull() ? object.get("GROUPDESC1").getAsString() : "");
                                model.setMATERIAL_TYPE(object.has("MATERIAL_TYPE") && !object.get("MATERIAL_TYPE").isJsonNull() ? object.get("MATERIAL_TYPE").getAsString() : "");
                                model.setGROUPCODE(object.has("GROUPCODE") && !object.get("GROUPCODE").isJsonNull() ? object.get("GROUPCODE").getAsString() : "");
                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull() ? object.get("UOM").getAsString() : "");
                                model.setCAPACITY(object.has("CAPACITY") && !object.get("CAPACITY").isJsonNull() ? object.get("CAPACITY").getAsString() : "");
                                model.setPHASE(object.has("PHASE") && !object.get("PHASE").isJsonNull() ? object.get("PHASE").getAsString() : "");

                                dtrTypesModels.add(model);
                                boolean Status = databaseEstimates.addDTRTypeData(model);
//                                if (Status) {
//                                    Toast.makeText(EstimatesHome.this, "Data Added Success Fully", Toast.LENGTH_SHORT).show();
//                                }
                            }

                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                getMaterialMasterData(est_no, model);
                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }

                            //getMaterialMasterData(est_no, model);

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }


    private void getPoleHeightValidateAPI(String est_no, PurposeModel model) {
        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading Poles Heights");
        main_ui_layout.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getPoleDistanceValidation();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    boolean clearStatus = databaseEstimates.clearPoleDetails();
                    if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                        dtrMakeMasters(est_no, model);
                    } else {
                        CommanUtilits.internetDialog(EstimatesHome.this);
                    }
                    //dtrMakeMasters(est_no, model);
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                PoleDistanceModel model = new PoleDistanceModel();
                                model.setHEIGHT(object.has("HEIGHT") && !object.get("HEIGHT").isJsonNull() ? object.get("HEIGHT").getAsString() : "");
                                model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                model.setMAX_DISTANCE(object.has("MAX_DISTANCE") && !object.get("MAX_DISTANCE").isJsonNull() ? object.get("MAX_DISTANCE").getAsInt() : 0);
                                model.setVOLTAGE(object.has("VOLTAGE") && !object.get("VOLTAGE").isJsonNull() ? object.get("VOLTAGE").getAsString() : "");
                                model.setCROSSING(object.has("CROSSING") && !object.get("CROSSING").isJsonNull() ? object.get("CROSSING").getAsString() : "");
                                model.setLINE_TYPE(object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : "");
                                boolean Status = databaseEstimates.addPoleDistanceDetails(model);

                            }

                            // getLineConfigData(est_no, model);

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void getLineConfigData(String est_no, PurposeModel model) {
        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading LT line configuration Master");
        main_ui_layout.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getLTLineConfigData();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    boolean clearStatus = databaseEstimates.clearLTLineConfig();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                LTLineConfigModel model = new LTLineConfigModel();
                                model.setPHASE(object.has("PHASE") && !object.get("PHASE").isJsonNull() ? object.get("PHASE").getAsString() : "");
                                model.setLINE_CONFIG(object.has("LINE_CONFIG") && !object.get("LINE_CONFIG").isJsonNull() ? object.get("LINE_CONFIG").getAsString() : "");
                                model.setFACTOR(object.has("FACTOR") && !object.get("FACTOR").isJsonNull() ? object.get("FACTOR").getAsString() : "");
                                model.setTYPE(object.has("TYPE") && !object.get("TYPE").isJsonNull() ? object.get("TYPE").getAsString() : "");
                                boolean Status = databaseEstimates.addLTLineConfigDetails(model);
                            }



                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                getEstimationData(est_no, model);
                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }

//                            getEstimationData(est_no, model);

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void dtrMakeMasters(String est_no, PurposeModel model) {
        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading DTR make master");
        main_ui_layout.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.dtrMakeMasters();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    boolean clearStatus = databaseEstimates.clearLTLineConfig();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                DTRMakeMaster model = new DTRMakeMaster();
                                model.setDTR_MAKE(object.has("DTR_MAKE") && !object.get("DTR_MAKE").isJsonNull() ? object.get("DTR_MAKE").getAsString() : "");
                                model.setDTR_NAME(object.has("DTR_NAME") && !object.get("DTR_NAME").isJsonNull() ? object.get("DTR_NAME").getAsString() : "");
                                model.setID(object.has("ID") && !object.get("ID").isJsonNull() ? object.get("ID").getAsString() : "");
                                boolean Status = databaseEstimates.addDTRMakeMaster(model);
                                //Toast.makeText(EstimatesHome.this, "Make master fetched ", Toast.LENGTH_SHORT).show();
                            }

                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                getLineConfigData(est_no, model);
                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }



                            //getEstimationData(est_no, model);

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void getMaterialMasterData(String est_no, PurposeModel model) {
        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading Material Master");
        main_ui_layout.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getMaterialMaster();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.VISIBLE);
                main_ui_layout.setVisibility(View.GONE);
                //loader.setVisibility(View.GONE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    boolean clearStatus = databaseEstimates.clearMaterialMasterTable();
                    if (clearStatus) {
                        //  Log.v("DB==", "Table cleared successfully");
                    } else {
                        //  Log.v("DB==", "Failed to clear table");
                    }
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
                                getPoleHeightValidateAPI(est_no, model);
                            } else {
                                CommanUtilits.internetDialog(EstimatesHome.this);
                            }

                            // getPoleHeightValidateAPI(est_no, model);
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                MaterialMasterModel model = new MaterialMasterModel();
                                model.setMATERIALPART1(object.has("MATERIALPART1") && !object.get("MATERIALPART1").isJsonNull() ? object.get("MATERIALPART1").getAsString() : "");
                                model.setMATERIALCODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull() ? object.get("MATERIALCODE").getAsString() : "");
                                model.setGROUPDESC1(object.has("GROUPDESC1") && !object.get("GROUPDESC1").isJsonNull() ? object.get("GROUPDESC1").getAsString() : "");
                                model.setMATERIAL_TYPE(object.has("MATERIAL_TYPE") && !object.get("MATERIAL_TYPE").isJsonNull() ? object.get("MATERIAL_TYPE").getAsString() : "");
                                model.setGROUPCODE(object.has("GROUPCODE") && !object.get("GROUPCODE").isJsonNull() ? object.get("GROUPCODE").getAsString() : "");
                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull() ? object.get("UOM").getAsString() : "");
                                model.setVOLTAGE_LEVEL(object.has("VOLTAGE_LEVEL") && !object.get("VOLTAGE_LEVEL").isJsonNull() ? object.get("VOLTAGE_LEVEL").getAsString() : "");
                                model.setHT_11KV(object.has("HT_11KV") && !object.get("HT_11KV").isJsonNull() ? object.get("HT_11KV").getAsString() : "");
                                model.setHT_33KV(object.has("HT_33KV") && !object.get("HT_33KV").isJsonNull() ? object.get("HT_33KV").getAsString() : "");
                                model.setLT(object.has("LT") && !object.get("LT").isJsonNull() ? object.get("LT").getAsString() : "");
                                model.setMATERIAL(object.has("MATERIAL") && !object.get("MATERIAL").isJsonNull() ? object.get("MATERIAL").getAsString() : "");
                                model.setPOLE_TYPE(object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : "");
                                model.setPOLE_HEIGHT(object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : "");
                                model.setMT_FLAG(object.has("MT_FLAG") && !object.get("MT_FLAG").isJsonNull() ? object.get("MT_FLAG").getAsString() : "");
                                materialMasterModels.add(model);
                                boolean Status = databaseEstimates.addMaterialMaster(model);
//                                if (Status) {
//                                    Toast.makeText(EstimatesHome.this, "Data Added Success Fully", Toast.LENGTH_SHORT).show();
//                                }
                            }

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void processArrayLT(JsonArray jsonArray, String voltage_level, JsonArray locationArray, PurposeModel purposeModel) {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                NewModel model = new NewModel();
                model.setCONDUCTOR_SAP_CODE(object.has("CONDUCTOR_SAP_CODE") && !object.get("CONDUCTOR_SAP_CODE").isJsonNull() ? object.get("CONDUCTOR_SAP_CODE").getAsString() : "");
                model.setCONDUCTOR_DESC(object.has("CONDUCTOR_DESC") && !object.get("CONDUCTOR_DESC").isJsonNull() ? object.get("CONDUCTOR_DESC").getAsString() : "");
                model.setLOCATION_ID(object.has("LOCATION_ID") && !object.get("LOCATION_ID").isJsonNull() ? object.get("LOCATION_ID").getAsString() : "");
                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull() ? object.get("POLE_CONFIG").getAsString() : "");
                model.setLINE_SURVEY_TYPE(object.has("LINE_SURVEY_TYPE") && !object.get("LINE_SURVEY_TYPE").isJsonNull() ? object.get("LINE_SURVEY_TYPE").getAsString() : "");
                model.setVOLTAGE_LEVEL(voltage_level);
                model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                model.setEST_NO(ESTIMATION_NUMBER);
                model.setIS_EXISTING_LOCATION_NEW((object.has("IS_EXISTING_LOCATION_NEW") && !object.get("IS_EXISTING_LOCATION_NEW").isJsonNull() ? object.get("IS_EXISTING_LOCATION_NEW").getAsString() : ""));
                model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                model.setCONDUCTOR_SIZE(object.has("CONDUCTOR_SIZE") && !object.get("CONDUCTOR_SIZE").isJsonNull() ? object.get("CONDUCTOR_SIZE").getAsString() : "");
                model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));

                model.setPOLE_SURVEY_TYPE(object.has("POLE_SURVEY_TYPE") && !object.get("POLE_SURVEY_TYPE").isJsonNull() ? object.get("POLE_SURVEY_TYPE").getAsString() : "");
                model.setNUMB_8M_STRUTS(object.has("NUMB_8M_STRUTS") && !object.get("NUMB_8M_STRUTS").isJsonNull() ? object.get("NUMB_8M_STRUTS").getAsInt() : 0);
                model.setNUMB_9M_STRUTS(object.has("NUMB_9M_STRUTS") && !object.get("NUMB_9M_STRUTS").isJsonNull() ? object.get("NUMB_9M_STRUTS").getAsInt() : 0);
                model.setNUMB_STAYS(object.has("NUMB_STAYS") && !object.get("NUMB_STAYS").isJsonNull() ? object.get("NUMB_STAYS").getAsInt() : 0);
                model.setLINE_TYPE((object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : ""));
                model.setPOLE_DESC((object.has("POLE_DESC") && !object.get("POLE_DESC").isJsonNull() ? object.get("POLE_DESC").getAsString() : ""));
                model.setPOLE_TYPE((object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : ""));
                model.setPOLE_SUPPORT((object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : ""));
                model.setPOLE_HEIGHT((object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : ""));
                model.setPOLE_STRUCTURE((object.has("POLE_STRUCTURE") && !object.get("POLE_STRUCTURE").isJsonNull() ? object.get("POLE_STRUCTURE").getAsString() : ""));
                model.setPOLE_CONCRETE((object.has("POLE_CONCRETE") && !object.get("POLE_CONCRETE").isJsonNull() ? object.get("POLE_CONCRETE").getAsString() : ""));
                model.setPIN_CUT((object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : ""));

                model.setIS_NEW_SURVEY("N");

                //LOCATIONS EDIT
                model.setREF_ID((object.has("ID") && !object.get("ID").isJsonNull() ? object.get("ID").getAsString() : ""));
                model.setIS_TRANSMITTED_DATA_UPDATED("N");
                model.setIS_TRANSMITTED_LOCATION("Y");
                model.setREF_VOLTAGE((object.has("REF_VOLTAGE") && !object.get("REF_VOLTAGE").isJsonNull() ? object.get("REF_VOLTAGE").getAsString() : ""));

                //INTERMEDIATE POLE
                model.setEXT_ID((object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : ""));
                model.setEXT_LOCATION((object.has("EXT_LOCATION") && !object.get("EXT_LOCATION").isJsonNull() ? object.get("EXT_LOCATION").getAsString() : ""));

                //NEW LT CHANGES
                model.setLT_AVAILABLE((object.has("LT_CKT_CODE") && !object.get("LT_CKT_CODE").isJsonNull() ? object.get("LT_CKT_CODE").getAsString() : ""));
                model.setDTR_STRUCTURE_CODE((object.has("DTR_CODE") && !object.get("DTR_CODE").isJsonNull() ? object.get("DTR_CODE").getAsString() : ""));

                model.setLTCKT_LOC_NO_NUMBER(
                        object.has("LTCKT_LOC_NO") &&
                                !object.get("LTCKT_LOC_NO").isJsonNull()
                                ? object.get("LTCKT_LOC_NO").getAsString()
                                : ""
                );

                model.setLTCKT_SOURCE_NO_NUMBER(
                        object.has("LTCKT_SOURCE_NO") &&
                                !object.get("LTCKT_SOURCE_NO").isJsonNull()
                                ? object.get("LTCKT_SOURCE_NO").getAsString()
                                : ""
                );


                newModels.add(model);
                if (!SurveyList.IS_EDIT) {
                    boolean Status = databaseEstimates.addNewSurveyData(model);
                }
            }

        }

        if (voltage_level.equalsIgnoreCase("LT")) {
            isLTCompleted = true;
            // Log.d("Completion", "LT processing completed");
        }
        if (voltage_level.equalsIgnoreCase("11KV")) {
            is11Completed = true;

        }
        if (voltage_level.equalsIgnoreCase("33KV")) {
            is33Completed = true;
            // Log.d("Completion", "33KV processing completed");
        }


    }

    private void processArray33KV(JsonArray jsonArray, String voltage_level, JsonArray locationArray, PurposeModel purposeModel) {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                NewModel model = new NewModel();
                model.setAB_DESC(object.has("AB_DESC") && !object.get("AB_DESC").isJsonNull() ? object.get("AB_DESC").getAsString() : "");
                model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                model.setCONDUCTOR_DESC(object.has("CONDUCTOR_DESC") && !object.get("CONDUCTOR_DESC").isJsonNull() ? object.get("CONDUCTOR_DESC").getAsString() : "");
                model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                model.setLINE_SURVEY_TYPE(object.has("LINE_SURVEY_TYPE") && !object.get("LINE_SURVEY_TYPE").isJsonNull() ? object.get("LINE_SURVEY_TYPE").getAsString() : "");
                model.setFEEDER_NAME(object.has("FEEDER_NAME") && !object.get("FEEDER_NAME").isJsonNull() ? object.get("FEEDER_NAME").getAsString() : "");
                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull() ? object.get("POLE_CONFIG").getAsString() : "");
                model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                model.setEST_NO(ESTIMATION_NUMBER);
                model.setIS_EXISTING_LOCATION_NEW((object.has("IS_EXISTING_LOCATION_NEW") && !object.get("IS_EXISTING_LOCATION_NEW").isJsonNull() ? object.get("IS_EXISTING_LOCATION_NEW").getAsString() : ""));
                model.setLOCATION_ID(object.has("LOCATION_ID") && !object.get("LOCATION_ID").isJsonNull() ? object.get("LOCATION_ID").getAsString() : "");
                model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                model.setVOLTAGE_LEVEL(voltage_level);
                model.setCONDUCTOR_SIZE(object.has("CONDUCTOR_SIZE") && !object.get("CONDUCTOR_SIZE").isJsonNull() ? object.get("CONDUCTOR_SIZE").getAsString() : "");
                model.setAB_SWITCH(object.has("AB_SWITCH") && !object.get("AB_SWITCH").isJsonNull() ? object.get("AB_SWITCH").getAsString() : "");
                model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));
                model.setCONDUCTOR_SAP_CODE(object.has("CONDUCTOR_SAP_CODE") && !object.get("CONDUCTOR_SAP_CODE").isJsonNull() ? object.get("CONDUCTOR_SAP_CODE").getAsString() : "");

                model.setPOLE_SURVEY_TYPE(object.has("POLE_SURVEY_TYPE") && !object.get("POLE_SURVEY_TYPE").isJsonNull() ? object.get("POLE_SURVEY_TYPE").getAsString() : "");
                model.setNUMB_8M_STRUTS(object.has("NUMB_8M_STRUTS") && !object.get("NUMB_8M_STRUTS").isJsonNull() ? object.get("NUMB_8M_STRUTS").getAsInt() : 0);
                model.setNUMB_9M_STRUTS(object.has("NUMB_9M_STRUTS") && !object.get("NUMB_9M_STRUTS").isJsonNull() ? object.get("NUMB_9M_STRUTS").getAsInt() : 0);
                model.setNUMB_STAYS(object.has("NUMB_STAYS") && !object.get("NUMB_STAYS").isJsonNull() ? object.get("NUMB_STAYS").getAsInt() : 0);
                model.setLINE_TYPE((object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : ""));
                model.setPOLE_DESC((object.has("POLE_DESC") && !object.get("POLE_DESC").isJsonNull() ? object.get("POLE_DESC").getAsString() : ""));
                model.setPOLE_TYPE((object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : ""));
                model.setPOLE_SUPPORT((object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : ""));
                model.setPOLE_HEIGHT((object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : ""));

                model.setCOUNT_V_XARM((object.has("COUNT_V_XARM") && !object.get("COUNT_V_XARM").isJsonNull() ? object.get("COUNT_V_XARM").getAsInt() : 0));
                model.setCOUNT_TOP_CLEAT((object.has("COUNT_TOP_CLEAT") && !object.get("COUNT_TOP_CLEAT").isJsonNull() ? object.get("COUNT_TOP_CLEAT").getAsInt() : 0));
                model.setCOUNT_TAPPING_XARM((object.has("COUNT_TAPPING_XARM") && !object.get("COUNT_TAPPING_XARM").isJsonNull() ? object.get("COUNT_TAPPING_XARM").getAsInt() : 0));
                model.setCOUNT_CHANNEL((object.has("COUNT_CHANNEL") && !object.get("COUNT_CHANNEL").isJsonNull() ? object.get("COUNT_CHANNEL").getAsInt() : 0));
                model.setCOUNT_SIDE_ARM((object.has("COUNT_SIDE_ARM") && !object.get("COUNT_SIDE_ARM").isJsonNull() ? object.get("COUNT_SIDE_ARM").getAsInt() : 0));
                model.setCOUNT_PIN_INS((object.has("COUNT_PIN_INS") && !object.get("COUNT_PIN_INS").isJsonNull() ? object.get("COUNT_PIN_INS").getAsInt() : 0));
                model.setCOUNT_DISC_INS((object.has("COUNT_DISC_INS") && !object.get("COUNT_DISC_INS").isJsonNull() ? object.get("COUNT_DISC_INS").getAsInt() : 0));
                model.setCOUNT_METALS_PARTS((object.has("COUNT_METALS_PARTS") && !object.get("COUNT_METALS_PARTS").isJsonNull() ? object.get("COUNT_METALS_PARTS").getAsInt() : 0));
                model.setVXARM_CODE((object.has("VXARM_CODE") && !object.get("VXARM_CODE").isJsonNull() ? object.get("VXARM_CODE").getAsString() : ""));
                model.setVXARM_DESC((object.has("VXARM_DESC") && !object.get("VXARM_DESC").isJsonNull() ? object.get("VXARM_DESC").getAsString() : ""));
                model.setTOPCLEAT_DESC((object.has("TOPCLEAT_DESC") && !object.get("TOPCLEAT_DESC").isJsonNull() ? object.get("TOPCLEAT_DESC").getAsString() : ""));
                model.setTOPCLEAT_CODE((object.has("TOPCLEAT_CODE") && !object.get("TOPCLEAT_CODE").isJsonNull() ? object.get("TOPCLEAT_CODE").getAsString() : ""));
                model.setTAPPINGXARM_CODE((object.has("TAPPINGXARM_CODE") && !object.get("TAPPINGXARM_CODE").isJsonNull() ? object.get("TAPPINGXARM_CODE").getAsString() : ""));
                model.setTAPPINGXARM_DESC((object.has("TAPPINGXARM_DESC") && !object.get("TAPPINGXARM_DESC").isJsonNull() ? object.get("TAPPINGXARM_DESC").getAsString() : ""));
                model.setCHANNEL_CODE((object.has("CHANNEL_CODE") && !object.get("CHANNEL_CODE").isJsonNull() ? object.get("CHANNEL_CODE").getAsString() : ""));
                model.setCHANNEL_DESC((object.has("CHANNEL_DESC") && !object.get("CHANNEL_DESC").isJsonNull() ? object.get("CHANNEL_DESC").getAsString() : ""));
                model.setSIDEARM_CODE((object.has("SIDEARM_CODE") && !object.get("SIDEARM_CODE").isJsonNull() ? object.get("SIDEARM_CODE").getAsString() : ""));
                model.setSIDEARM_DESC((object.has("SIDEARM_DESC") && !object.get("SIDEARM_DESC").isJsonNull() ? object.get("CHANNEL_DESC").getAsString() : ""));
                model.setPIN_INS_CODE((object.has("PIN_INS_CODE") && !object.get("PIN_INS_CODE").isJsonNull() ? object.get("PIN_INS_CODE").getAsString() : ""));
                model.setPIN_INS_DESC((object.has("PIN_INS_DESC") && !object.get("PIN_INS_DESC").isJsonNull() ? object.get("PIN_INS_DESC").getAsString() : ""));
                model.setDISC_INS_CODE((object.has("DISC_INS_CODE") && !object.get("DISC_INS_CODE").isJsonNull() ? object.get("DISC_INS_CODE").getAsString() : ""));
                model.setDISC_INS_DESC((object.has("DISC_INS_DESC") && !object.get("DISC_INS_DESC").isJsonNull() ? object.get("DISC_INS_DESC").getAsString() : ""));
                model.setMETAL_PARTS_CODE((object.has("METAL_PARTS_CODE") && !object.get("METAL_PARTS_CODE").isJsonNull() ? object.get("METAL_PARTS_CODE").getAsString() : ""));
                model.setMETAL_PARTS_DESC((object.has("METAL_PARTS_DESC") && !object.get("METAL_PARTS_DESC").isJsonNull() ? object.get("METAL_PARTS_DESC").getAsString() : ""));
                model.setMETAL_PARTS_DESC((object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : ""));
                model.setMETAL_PARTS_DESC((object.has("END_POLE") && !object.get("END_POLE").isJsonNull() ? object.get("END_POLE").getAsString() : ""));
                model.setPOLE_STRUCTURE((object.has("POLE_STRUCTURE") && !object.get("POLE_STRUCTURE").isJsonNull() ? object.get("POLE_STRUCTURE").getAsString() : ""));
                model.setPOLE_CONCRETE((object.has("POLE_CONCRETE") && !object.get("POLE_CONCRETE").isJsonNull() ? object.get("POLE_CONCRETE").getAsString() : ""));
                model.setPIN_CUT((object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : ""));
                model.setHTSC_NO((object.has("HTSC_NO") && !object.get("HTSC_NO").isJsonNull() ? object.get("HTSC_NO").getAsString() : ""));
                model.setIS_NEW_SURVEY("N");


                //LOCATIONS EDIT
                model.setREF_ID((object.has("ID") && !object.get("ID").isJsonNull() ? object.get("ID").getAsString() : ""));
                model.setIS_TRANSMITTED_DATA_UPDATED("N");
                model.setIS_TRANSMITTED_LOCATION("Y");
                model.setREF_VOLTAGE((object.has("REF_VOLTAGE") && !object.get("REF_VOLTAGE").isJsonNull() ? object.get("REF_VOLTAGE").getAsString() : ""));

                //INTERMEDIATE POLE
                model.setEXT_ID((object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : ""));
                model.setEXT_LOCATION((object.has("EXT_LOCATION") && !object.get("EXT_LOCATION").isJsonNull() ? object.get("EXT_LOCATION").getAsString() : ""));

                model.setFDR_LOC_NO_NUMBER(
                        object.has("FDR_LOC_NO") &&
                                !object.get("FDR_LOC_NO").isJsonNull()
                                ? object.get("FDR_LOC_NO").getAsString()
                                : ""
                );

                model.setFDR_SOURCE_NO_NUMBER(
                        object.has("FDR_SOURCE_NO") &&
                                !object.get("FDR_SOURCE_NO").isJsonNull()
                                ? object.get("FDR_SOURCE_NO").getAsString()
                                : ""
                );

                newModels.add(model);
                if (!SurveyList.IS_EDIT) {
                    boolean Status = databaseEstimates.addNewSurveyData(model);
                }


            }

        }

        if (voltage_level.equalsIgnoreCase("LT")) {
            isLTCompleted = true;
            // Log.d("Completion", "LT processing completed");
        }
        if (voltage_level.equalsIgnoreCase("11KV")) {
            is11Completed = true;

        }
        if (voltage_level.equalsIgnoreCase("33KV")) {
            is33Completed = true;
            //Log.d("Completion", "33KV processing completed");
        }


    }

    private void processArray11KV(JsonArray jsonArray, String voltage_level, JsonArray locationArray, PurposeModel purposeModel) {

        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading Estimates Data, Feeder & DTR Master Data");
        main_ui_layout.setVisibility(View.GONE);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                NewModel model = new NewModel();
                model.setAB_DESC(object.has("AB_DESC") && !object.get("AB_DESC").isJsonNull() ? object.get("AB_DESC").getAsString() : "");
                model.setPOLE_NO(object.has("POLE_NO") && !object.get("POLE_NO").isJsonNull() ? object.get("POLE_NO").getAsString() : "");
                model.setLINE_SURVEY_TYPE(object.has("LINE_SURVEY_TYPE") && !object.get("LINE_SURVEY_TYPE").isJsonNull() ? object.get("LINE_SURVEY_TYPE").getAsString() : "");
                model.setLONGITUDE((object.has("GLONG") && !object.get("GLONG").isJsonNull() ? object.get("GLONG").getAsString() : ""));
                model.setDTR_DESC((object.has("DTR_DESC") && !object.get("DTR_DESC").isJsonNull() ? object.get("DTR_DESC").getAsString() : ""));
                model.setLOCATION_ID(object.has("LOCATION_ID") && !object.get("LOCATION_ID").isJsonNull() ? object.get("LOCATION_ID").getAsString() : "");
                model.setIS_EXISTING_LOCATION_NEW((object.has("IS_EXISTING_LOCATION_NEW") && !object.get("IS_EXISTING_LOCATION_NEW").isJsonNull() ? object.get("IS_EXISTING_LOCATION_NEW").getAsString() : ""));
                model.setLATITUDE((object.has("GLAT") && !object.get("GLAT").isJsonNull() ? object.get("GLAT").getAsString() : ""));
                model.setDTR_CAPACITY((object.has("DTR_CAPACITY") && !object.get("DTR_CAPACITY").isJsonNull() ? object.get("DTR_CAPACITY").getAsString() : ""));
                model.setDTR_SURVEY_TYPE((object.has("DTR_SURVEY_TYPE") && !object.get("DTR_SURVEY_TYPE").isJsonNull() ? object.get("DTR_SURVEY_TYPE").getAsString() : ""));
                model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                model.setFEEDER_NAME(object.has("FEEDER_NAME") && !object.get("FEEDER_NAME").isJsonNull() ? object.get("FEEDER_NAME").getAsString() : "");
                model.setCONDUCTOR_SAP_CODE(object.has("CONDUCTOR_SAP_CODE") && !object.get("CONDUCTOR_SAP_CODE").isJsonNull() ? object.get("CONDUCTOR_SAP_CODE").getAsString() : "");
                model.setVOLTAGE_LEVEL(voltage_level);
                model.setDTR_SAP_CODE(object.has("DTR_SAP_CODE") && !object.get("DTR_SAP_CODE").isJsonNull() ? object.get("DTR_SAP_CODE").getAsString() : "");
                model.setFROM_POLE(object.has("FROM_POLE") && !object.get("FROM_POLE").isJsonNull() ? object.get("FROM_POLE").getAsString() : "");
                model.setCONDUCTOR_SIZE(object.has("CONDUCTOR_SIZE") && !object.get("CONDUCTOR_SIZE").isJsonNull() ? object.get("CONDUCTOR_SIZE").getAsString() : "");
                model.setDTR_PHASE(object.has("DTR_PHASE") && !object.get("DTR_PHASE").isJsonNull() ? object.get("DTR_PHASE").getAsString() : "");
                model.setCONDUCTOR_DESC(object.has("CONDUCTOR_DESC") && !object.get("CONDUCTOR_DESC").isJsonNull() ? object.get("CONDUCTOR_DESC").getAsString() : "");
                model.setFEEDER_ID(object.has("FEEDER_ID") && !object.get("FEEDER_ID").isJsonNull() ? object.get("FEEDER_ID").getAsString() : "");
                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull() ? object.get("POLE_CONFIG").getAsString() : "");
                model.setDTR_MOUNTING(object.has("DTR_MOUNTING") && !object.get("DTR_MOUNTING").isJsonNull() ? object.get("DTR_MOUNTING").getAsString() : "");
                model.setEST_NO(ESTIMATION_NUMBER);
                model.setAB_SWITCH(object.has("AB_SWITCH") && !object.get("AB_SWITCH").isJsonNull() ? object.get("AB_SWITCH").getAsString() : "");
                model.setLT_FUSE_DESC(object.has("LT_FUSE_DESC") && !object.get("LT_FUSE_DESC").isJsonNull() ? object.get("LT_FUSE_DESC").getAsString() : "");
                model.setLT_FUSE_CODE(object.has("LT_FUSE_DESC") && !object.get("LT_FUSE_DESC").isJsonNull() ? object.get("LT_FUSE_DESC").getAsString() : "");
                model.setHG_FUSE_DESC(object.has("HG_FUSE_DESC") && !object.get("HG_FUSE_DESC").isJsonNull() ? object.get("HG_FUSE_DESC").getAsString() : "");
                model.setHG_FUSE_CODE(object.has("HG_FUSE_CODE") && !object.get("HG_FUSE_CODE").isJsonNull() ? object.get("HG_FUSE_CODE").getAsString() : "");
                model.setDTR_AB_DESC(object.has("DTR_AB_DESC") && !object.get("DTR_AB_DESC").isJsonNull() ? object.get("DTR_AB_DESC").getAsString() : "");
                model.setDTR_AB_CODE(object.has("DTR_AB_CODE") && !object.get("DTR_AB_CODE").isJsonNull() ? object.get("DTR_AB_CODE").getAsString() : "");
                model.setPOLE_SURVEY_TYPE(object.has("POLE_SURVEY_TYPE") && !object.get("POLE_SURVEY_TYPE").isJsonNull() ? object.get("POLE_SURVEY_TYPE").getAsString() : "");
                model.setNUMB_8M_STRUTS(object.has("NUMB_8M_STRUTS") && !object.get("NUMB_8M_STRUTS").isJsonNull() ? object.get("NUMB_8M_STRUTS").getAsInt() : 0);
                model.setNUMB_9M_STRUTS(object.has("NUMB_9M_STRUTS") && !object.get("NUMB_9M_STRUTS").isJsonNull() ? object.get("NUMB_9M_STRUTS").getAsInt() : 0);
                model.setNUMB_STAYS(object.has("NUMB_STAYS") && !object.get("NUMB_STAYS").isJsonNull() ? object.get("NUMB_STAYS").getAsInt() : 0);
                model.setLINE_TYPE((object.has("LINE_TYPE") && !object.get("LINE_TYPE").isJsonNull() ? object.get("LINE_TYPE").getAsString() : ""));
                model.setPOLE_DESC((object.has("POLE_DESC") && !object.get("POLE_DESC").isJsonNull() ? object.get("POLE_DESC").getAsString() : ""));
                model.setPOLE_TYPE((object.has("POLE_TYPE") && !object.get("POLE_TYPE").isJsonNull() ? object.get("POLE_TYPE").getAsString() : ""));
                model.setPOLE_SUPPORT((object.has("POLE_SUPPORT") && !object.get("POLE_SUPPORT").isJsonNull() ? object.get("POLE_SUPPORT").getAsString() : ""));
                model.setPOLE_HEIGHT((object.has("POLE_HEIGHT") && !object.get("POLE_HEIGHT").isJsonNull() ? object.get("POLE_HEIGHT").getAsString() : ""));
                model.setGI_DESC((object.has("GI_DESC") && !object.get("GI_DESC").isJsonNull() ? object.get("GI_DESC").getAsString() : ""));
                model.setGI_SAP_CODE((object.has("GI_SAP_CODE") && !object.get("GI_SAP_CODE").isJsonNull() ? object.get("GI_SAP_CODE").getAsString() : ""));
                model.setCI_DESC((object.has("CI_DESC") && !object.get("CI_DESC").isJsonNull() ? object.get("CI_DESC").getAsString() : ""));
                model.setCI_SAP_CODE((object.has("CI_SAP_CODE") && !object.get("CI_SAP_CODE").isJsonNull() ? object.get("CI_SAP_CODE").getAsString() : ""));
                model.setCOUNT_CI((object.has("COUNT_CI") && !object.get("COUNT_CI").isJsonNull() ? object.get("COUNT_CI").getAsInt() : 0));
                model.setCOUNT_GI((object.has("COUNT_GI") && !object.get("COUNT_GI").isJsonNull() ? object.get("COUNT_GI").getAsInt() : 0));

                model.setCOUNT_V_XARM((object.has("COUNT_V_XARM") && !object.get("COUNT_V_XARM").isJsonNull() ? object.get("COUNT_V_XARM").getAsInt() : 0));
                model.setCOUNT_TOP_CLEAT((object.has("COUNT_TOP_CLEAT") && !object.get("COUNT_TOP_CLEAT").isJsonNull() ? object.get("COUNT_TOP_CLEAT").getAsInt() : 0));
                model.setCOUNT_TAPPING_XARM((object.has("COUNT_TAPPING_XARM") && !object.get("COUNT_TAPPING_XARM").isJsonNull() ? object.get("COUNT_TAPPING_XARM").getAsInt() : 0));
                model.setCOUNT_CHANNEL((object.has("COUNT_CHANNEL") && !object.get("COUNT_CHANNEL").isJsonNull() ? object.get("COUNT_CHANNEL").getAsInt() : 0));
                model.setCOUNT_SIDE_ARM((object.has("COUNT_SIDE_ARM") && !object.get("COUNT_SIDE_ARM").isJsonNull() ? object.get("COUNT_SIDE_ARM").getAsInt() : 0));
                model.setCOUNT_PIN_INS((object.has("COUNT_PIN_INS") && !object.get("COUNT_PIN_INS").isJsonNull() ? object.get("COUNT_PIN_INS").getAsInt() : 0));
                model.setCOUNT_DISC_INS((object.has("COUNT_DISC_INS") && !object.get("COUNT_DISC_INS").isJsonNull() ? object.get("COUNT_DISC_INS").getAsInt() : 0));
                model.setCOUNT_METALS_PARTS((object.has("COUNT_METALS_PARTS") && !object.get("COUNT_METALS_PARTS").isJsonNull() ? object.get("COUNT_METALS_PARTS").getAsInt() : 0));
                model.setVXARM_CODE((object.has("VXARM_CODE") && !object.get("VXARM_CODE").isJsonNull() ? object.get("VXARM_CODE").getAsString() : ""));
                model.setVXARM_DESC((object.has("VXARM_DESC") && !object.get("VXARM_DESC").isJsonNull() ? object.get("VXARM_DESC").getAsString() : ""));
                model.setTOPCLEAT_DESC((object.has("TOPCLEAT_DESC") && !object.get("TOPCLEAT_DESC").isJsonNull() ? object.get("TOPCLEAT_DESC").getAsString() : ""));
                model.setTOPCLEAT_CODE((object.has("TOPCLEAT_CODE") && !object.get("TOPCLEAT_CODE").isJsonNull() ? object.get("TOPCLEAT_CODE").getAsString() : ""));
                model.setTAPPINGXARM_CODE((object.has("TAPPINGXARM_CODE") && !object.get("TAPPINGXARM_CODE").isJsonNull() ? object.get("TAPPINGXARM_CODE").getAsString() : ""));
                model.setTAPPINGXARM_DESC((object.has("TAPPINGXARM_DESC") && !object.get("TAPPINGXARM_DESC").isJsonNull() ? object.get("TAPPINGXARM_DESC").getAsString() : ""));
                model.setCHANNEL_CODE((object.has("CHANNEL_CODE") && !object.get("CHANNEL_CODE").isJsonNull() ? object.get("CHANNEL_CODE").getAsString() : ""));
                model.setCHANNEL_DESC((object.has("CHANNEL_DESC") && !object.get("CHANNEL_DESC").isJsonNull() ? object.get("CHANNEL_DESC").getAsString() : ""));
                model.setSIDEARM_CODE((object.has("SIDEARM_CODE") && !object.get("SIDEARM_CODE").isJsonNull() ? object.get("SIDEARM_CODE").getAsString() : ""));
                model.setSIDEARM_DESC((object.has("SIDEARM_DESC") && !object.get("SIDEARM_DESC").isJsonNull() ? object.get("CHANNEL_DESC").getAsString() : ""));
                model.setPIN_INS_CODE((object.has("PIN_INS_CODE") && !object.get("PIN_INS_CODE").isJsonNull() ? object.get("PIN_INS_CODE").getAsString() : ""));
                model.setPIN_INS_DESC((object.has("PIN_INS_DESC") && !object.get("PIN_INS_DESC").isJsonNull() ? object.get("PIN_INS_DESC").getAsString() : ""));
                model.setDISC_INS_CODE((object.has("DISC_INS_CODE") && !object.get("DISC_INS_CODE").isJsonNull() ? object.get("DISC_INS_CODE").getAsString() : ""));
                model.setDISC_INS_DESC((object.has("DISC_INS_DESC") && !object.get("DISC_INS_DESC").isJsonNull() ? object.get("DISC_INS_DESC").getAsString() : ""));
                model.setMETAL_PARTS_CODE((object.has("METAL_PARTS_CODE") && !object.get("METAL_PARTS_CODE").isJsonNull() ? object.get("METAL_PARTS_CODE").getAsString() : ""));
                model.setMETAL_PARTS_DESC((object.has("METAL_PARTS_DESC") && !object.get("METAL_PARTS_DESC").isJsonNull() ? object.get("METAL_PARTS_DESC").getAsString() : ""));
                model.setPOLE_STRUCTURE((object.has("POLE_STRUCTURE") && !object.get("POLE_STRUCTURE").isJsonNull() ? object.get("POLE_STRUCTURE").getAsString() : ""));
                model.setPOLE_CONCRETE((object.has("POLE_CONCRETE") && !object.get("POLE_CONCRETE").isJsonNull() ? object.get("POLE_CONCRETE").getAsString() : ""));
                model.setPIN_CUT((object.has("PIN_CUT") && !object.get("PIN_CUT").isJsonNull() ? object.get("PIN_CUT").getAsString() : ""));
                model.setDTR_STRUCTURE_CODE((object.has("DTR_CODE") && !object.get("DTR_CODE").isJsonNull() ? object.get("DTR_CODE").getAsString() : ""));
                model.setDTR_SHIFTED((object.has("DTR_SHIFTED") && !object.get("DTR_SHIFTED").isJsonNull() ? object.get("DTR_SHIFTED").getAsString() : ""));
                model.setDTR_ID((object.has("DTR_ID") && !object.get("DTR_ID").isJsonNull() ? object.get("DTR_ID").getAsString() : ""));

                model.setIS_NEW_SURVEY("N");

                //LOCATIONS EDIT
                model.setREF_ID((object.has("ID") && !object.get("ID").isJsonNull() ? object.get("ID").getAsString() : ""));
                model.setIS_TRANSMITTED_DATA_UPDATED("N");
                model.setIS_TRANSMITTED_LOCATION("Y");
                model.setREF_VOLTAGE((object.has("REF_VOLTAGE") && !object.get("REF_VOLTAGE").isJsonNull() ? object.get("REF_VOLTAGE").getAsString() : ""));

                //INTERMEDIATE POLE
                model.setEXT_ID((object.has("EXT_ID") && !object.get("EXT_ID").isJsonNull() ? object.get("EXT_ID").getAsString() : ""));
                model.setEXT_LOCATION((object.has("EXT_LOCATION") && !object.get("EXT_LOCATION").isJsonNull() ? object.get("EXT_LOCATION").getAsString() : ""));
                //NEW LT CHANGES
                model.setLT_AVAILABLE((object.has("LT_AVAILABLE") && !object.get("LT_AVAILABLE").isJsonNull() ? object.get("LT_AVAILABLE").getAsString() : ""));
                model.setCOUNT_CKT((object.has("COUNT_CKT") && !object.get("COUNT_CKT").isJsonNull() ? object.get("COUNT_CKT").getAsInt() : 0));
                model.setDTR_LOCATION((object.has("DTR_LOCATION") && !object.get("DTR_LOCATION").isJsonNull() ? object.get("DTR_LOCATION").getAsString() : ""));

                model.setFDR_LOC_NO_NUMBER(
                        object.has("FDR_LOC_NO") &&
                                !object.get("FDR_LOC_NO").isJsonNull()
                                ? object.get("FDR_LOC_NO").getAsString()
                                : ""
                );

                model.setFDR_SOURCE_NO_NUMBER(
                        object.has("FDR_SOURCE_NO") &&
                                !object.get("FDR_SOURCE_NO").isJsonNull()
                                ? object.get("FDR_SOURCE_NO").getAsString()
                                : ""
                );

                newModels.add(model);
                if (!SurveyList.IS_EDIT) {
                    boolean Status = databaseEstimates.addNewSurveyData(model);
                }
            }

        }

        if (voltage_level.equalsIgnoreCase("LT")) {
            isLTCompleted = true;
            // Log.d("Completion", "LT processing completed");
        }
        if (voltage_level.equalsIgnoreCase("11KV")) {
            is11Completed = true;

        }
        if (voltage_level.equalsIgnoreCase("33KV")) {
            is33Completed = true;
            // Log.d("Completion", "33KV processing completed");
        }



    }

    private void processArraySOLARDPR(JsonArray jsonArray, String voltage_level, JsonArray locationArray, PurposeModel purposeModel) {

        loader_layout.setVisibility(View.VISIBLE);
        tv_progress_bar.setText("Downloading Estimates Data, Feeder & DTR Master Data");
        main_ui_layout.setVisibility(View.GONE);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                NewModel model = new NewModel();
                model.setEST_NO(object.has("EST_NO") && !object.get("EST_NO").isJsonNull() ? object.get("EST_NO").getAsString() : "");
                model.setGIS_ID((object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : ""));
                ACTION = object.has("ACTION") && !object.get("ACTION").isJsonNull() ? object.get("ACTION").getAsString() : "";
                ACTION_REMARKS = object.has("ACTION_REMARKS") && !object.get("ACTION_REMARKS").isJsonNull() ? object.get("ACTION_REMARKS").getAsString() : "";
                DTR_NAME = object.has("DTR_NAME") && !object.get("DTR_NAME").isJsonNull() ? object.get("DTR_NAME").getAsString() : "";
                DTR_PHASE = object.has("DTR_PHASE") && !object.get("DTR_PHASE").isJsonNull() ? object.get("DTR_PHASE").getAsString() : "";
                DTR_CAPACITY = object.has("DTR_CAPACITY") && !object.get("DTR_CAPACITY").isJsonNull() ? object.get("DTR_CAPACITY").getAsString() : "";
                newModels.add(model);
                if (!SurveyList.IS_EDIT) {
                    boolean Status = databaseEstimates.addNewSurveyData(model);
                }
            }

        }

        if (voltage_level.equalsIgnoreCase("LT")) {
            isLTCompleted = true;
            // Log.d("Completion", "LT processing completed");
        }
        if (voltage_level.equalsIgnoreCase("11KV")) {
            is11Completed = true;

        }
        if (voltage_level.equalsIgnoreCase("33KV")) {
            is33Completed = true;
            // Log.d("Completion", "33KV processing completed");
        }




    }





    @Override
    public void onItemClick(int position, PurposeModel model) {

        if (ConnectivityChecking.checkConnection(EstimatesHome.this)) {
            getSingleEstimateWithFdrs(model.getGIS_ID(), model, position);
        } else {
            CommanUtilits.internetDialog(EstimatesHome.this);
        }
        //NEW API CODE
        // getSingleEstimateWithFdrs(model.getGIS_ID(), model, position);

        //getFeederMasterAPI(model.getSEC_ID(), model.getEST_NO(), model);


    }

    @Override
    public void onStartClick(int position, PurposeModel model) {
//        getFeederMasterAPI();

    }

    //NEW DTR CHANGES BY CHANDU
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


}