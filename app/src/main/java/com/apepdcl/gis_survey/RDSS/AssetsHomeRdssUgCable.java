package com.apepdcl.gis_survey.RDSS;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.models.EHT_SS_MASTER;
import com.apepdcl.gis_survey.PMI.models.EHT_SS_MASTER_ALL;
import com.apepdcl.gis_survey.PMI.models.FeedersData;
import com.apepdcl.gis_survey.PMI.models.Feeders_11KV;
import com.apepdcl.gis_survey.PMI.models.Feeders_33KV;
import com.apepdcl.gis_survey.PMI.models.SS_MASTER_11KV;
import com.apepdcl.gis_survey.PMI.models.SS_MASTER_ALL;
import com.apepdcl.gis_survey.RDSS.edit.EditOptionsRdss;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import com.apepdcl.gis.app.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 19-05-2025.
 */
public class AssetsHomeRdssUgCable extends AppCompatActivity {

    RelativeLayout logout_layout, profile_layout;
    LinearLayout loader;

    LinearLayout main_layout;
    Spinner sp_voltage_level;

    //33KV
    LinearLayout main_layout_33;
    TextView sub_station_name_33;
    Button btn_next_33, btn_edit_survey, btn_edit_survey_11, btn_view_survey, btn_view_survey_11;
    private String button_type = "";
    ProgressBar loader_33;

    //11KV
    LinearLayout main_layout_11;
    TextView sub_station_name_11;
    Button btn_next_11;
    ProgressBar loader_11;

    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat, currentLog;

    ArrayList<UGPostingModel> ugPostingList = new ArrayList<>();


    private RetrofitServiceRDSS retrofitServiceRDSS;
    ArrayList<EHT_SS_MASTER> ssMasters33;

    public static ArrayList<EHT_SS_MASTER> ehtSsMasters;
    public static ArrayList<EHT_SS_MASTER_ALL> ehtSsMasterAlls;
    public static ArrayList<SS_MASTER_ALL> ssMasterAlls;
    public static ArrayList<SS_MASTER_11KV> ssMaster11_new;

    public static ArrayList<Feeders_33KV> feeders33KV;

    public static ArrayList<Feeders_11KV> feeders11KV;

    public static ArrayList<String> nearBy33kvSSNames;
    public static ArrayList<String> total33kvSSNames;


    public static ArrayList<String> nearBy11kvSSNames;
    public static ArrayList<String> total11kvSSNames;


    ArrayList<SS_MASTER_11KV> ssMaster11;

    public static String SELECTED_VOLTAGE;
    public static String selectedSSName33;
    public static String selectedSSCode33;
    public static String selectedSSCode11;
    public static String selectedSSName11;

    String ID, NAME;

    LinearLayout transmit_layout;
    TextView poles_33kv, poles_11kv;
    Button btn_transmit;
    ProgressBar loader_transmit;
    ArrayList<UGPostingModel> tempList;
    ArrayList<UGPostingModel> postingList;
    RdssUGDatabase database;
    ProgressDialog progressDialog;

    GpsTraker gpsTraker;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_home);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        database = new RdssUGDatabase(this);
        gpsTraker = new GpsTraker(AssetsHomeRdssUgCable.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLatLongs();
        tempList = database.getUGPostingData();
        postingList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.get(i).getIS_FRESH_SURVEY().equalsIgnoreCase("Y")) {
                postingList.add(tempList.get(i));
            }
        }

        System.out.println("SURVEY_DATA_SIZE:" + tempList.size());
        gpsTraker.getLocation();

        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);

        main_layout = findViewById(R.id.main_layout);
        sp_voltage_level = findViewById(R.id.sp_voltage_level);
        loader = findViewById(R.id.loader);

        main_layout_33 = findViewById(R.id.main_layout_33);
        sub_station_name_33 = findViewById(R.id.sub_station_name_33);
        btn_next_33 = findViewById(R.id.btn_next_33);
        btn_edit_survey = findViewById(R.id.btn_edit_survey);
        btn_edit_survey_11 = findViewById(R.id.btn_edit_survey_11);
        loader_33 = findViewById(R.id.loader_33);
        btn_view_survey = findViewById(R.id.btn_view_survey);
        btn_view_survey_11 = findViewById(R.id.btn_view_survey_11);

        main_layout_11 = findViewById(R.id.main_layout_11);
        sub_station_name_11 = findViewById(R.id.sub_station_name_11);
        btn_next_11 = findViewById(R.id.btn_next_11);
        loader_11 = findViewById(R.id.loader_11);

        ssMasters33 = new ArrayList<>();
        ssMaster11 = new ArrayList<>();
        ehtSsMasters = new ArrayList<>();
        feeders33KV = new ArrayList<>();
        feeders11KV = new ArrayList<>();
        ssMaster11_new = new ArrayList<>();
        ehtSsMasterAlls = new ArrayList<>();
        ssMasterAlls = new ArrayList<>();


        total33kvSSNames = new ArrayList<>();
        nearBy33kvSSNames = new ArrayList<>();
        nearBy11kvSSNames = new ArrayList<>();
        total11kvSSNames = new ArrayList<>();


        //TRANSMIT
        transmit_layout = findViewById(R.id.transmit_layout);
        poles_33kv = findViewById(R.id.poles_33kv);
        poles_11kv = findViewById(R.id.poles_11kv);
        btn_transmit = findViewById(R.id.btn_transmit);
        loader_transmit = findViewById(R.id.loader_transmit);

        loadFeederMasterData();


        sp_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SELECTED_VOLTAGE = sp_voltage_level.getSelectedItem().toString();
                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("EHT SS 33KV")) {
                    SELECTED_VOLTAGE = "33KV";
                    main_layout_33.setVisibility(View.VISIBLE);
                    main_layout_11.setVisibility(View.GONE);
                    //main_layout_lt.setVisibility(View.GONE);
                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33/11KV SS")) {
                    SELECTED_VOLTAGE = "11KV";
                    main_layout_33.setVisibility(View.GONE);
                    main_layout_11.setVisibility(View.VISIBLE);
                    // main_layout_lt.setVisibility(View.GONE);

                }/*else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")){
                    main_layout_33.setVisibility(View.GONE);
                    main_layout_11.setVisibility(View.GONE);
                   // main_layout_lt.setVisibility(View.VISIBLE);
                }*/ else {
                    main_layout_33.setVisibility(View.GONE);
                    main_layout_11.setVisibility(View.GONE);
                    // main_layout_lt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sub_station_name_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  open33KVSSNearByDialog(sub_station_name_33);

                ArrayList<String> allList = new ArrayList<>();
                for (int i = 0; i < ssMasters33.size(); i++) {
                    allList.add(ssMasters33.get(i).getEHT_SS_NAME() + "~" + ssMasters33.get(i).getEHT_SS_CODE());
                }

                SearchableSpinner searchableSpinner = new SearchableSpinner(AssetsHomeRdssUgCable.this);
                searchableSpinner.setWindowTitle("SELECT EHT SS");
                searchableSpinner.setSpinnerListItems(allList);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        sub_station_name_33.setText(selectedString);
                        selectedSSCode33 = selectedString.split("~")[1];
                        selectedSSName33 = selectedString.split("~")[0];

                        ID = selectedSSCode33;
                        NAME = selectedSSName33;
                    }
                });
            }
        });

        btn_next_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sub_station_name_33.getText().toString().isEmpty()) {
                    Toast.makeText(AssetsHomeRdssUgCable.this, "Please Select EHT SS", Toast.LENGTH_SHORT).show();
                    return;
                }

                button_type = "Next_33";
                // E20

                get33KVPolesData();
            }
        });

        btn_edit_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sub_station_name_33.getText().toString().isEmpty()) {
                    Toast.makeText(AssetsHomeRdssUgCable.this, "Please Select EHT SS", Toast.LENGTH_SHORT).show();
                    return;
                }
                button_type = "Edit";
                get33KVPolesData();
            }
        });


        sub_station_name_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   open11KVSSNearByDialog(sub_station_name_11);

                ArrayList<String> allList = new ArrayList<>();

                for (int i = 0; i < ssMaster11.size(); i++) {
                    allList.add(ssMaster11.get(i).getSUB_STATION_NAME() + "~" + ssMaster11.get(i).getSUB_STATION_ID());
                }

                SearchableSpinner searchableSpinner = new SearchableSpinner(AssetsHomeRdssUgCable.this);
                searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                searchableSpinner.setSpinnerListItems(allList);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        sub_station_name_11.setText(selectedString);
                        selectedSSCode11 = selectedString.split("~")[1];
                        selectedSSName11 = selectedString.split("~")[0];

                        ID = selectedSSCode11;
                        NAME = selectedSSName11;
                    }
                });
            }
        });

        btn_next_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sub_station_name_11.getText().toString().isEmpty()) {
                    Toast.makeText(AssetsHomeRdssUgCable.this, "Please Select 33/11KV SS", Toast.LENGTH_SHORT).show();
                    return;
                }
                button_type = "Next_11";
               /* Intent intentPf = new Intent(ProposedHome.this, UGCableProposedMap.class);
                intentPf.putExtra("ID",ID);
                intentPf.putExtra("NAME",NAME);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);*/
                // get11KVPolesData();
                get33KVPolesData();
            }
        });

        btn_edit_survey_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sub_station_name_11.getText().toString().isEmpty()) {
                    Toast.makeText(AssetsHomeRdssUgCable.this, "Please Select 33/11KV SS", Toast.LENGTH_SHORT).show();
                    return;
                }

                button_type = "Edit";
               /* Intent intentPf = new Intent(ProposedHome.this, UGCableProposedMap.class);
                intentPf.putExtra("ID",ID);
                intentPf.putExtra("NAME",NAME);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);*/
                // get11KVPolesData();
                get33KVPolesData();
            }
        });

        btn_view_survey_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sub_station_name_11.getText().toString().isEmpty()) {
                    Toast.makeText(AssetsHomeRdssUgCable.this, "Please Select 33/11KV SS", Toast.LENGTH_SHORT).show();
                    return;
                }

                button_type = "VIEW_MAP_11";
                get33KVPolesData();
            }
        });

        btn_view_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sub_station_name_33.getText().toString().isEmpty()) {
                    Toast.makeText(AssetsHomeRdssUgCable.this, "Please Select EHT SS", Toast.LENGTH_SHORT).show();
                    return;
                }

                button_type = "VIEW_MAP_33";

                get33KVPolesData();


            }
        });

        btn_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postingList.isEmpty()) {
                    Toast.makeText(AssetsHomeRdssUgCable.this, "There are No Poles for transmit", Toast.LENGTH_SHORT).show();
                    return;
                }
                transmitPoles();
            }
        });

        setTransmitData();

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void get33KVPolesData() {
        progressDialog.show();

        JsonObject object = new JsonObject();
        object.addProperty("SS_ID", ID);

        retrofitServiceRDSS = RetrofitClientInstanceRDSS.getRetrofitInstance().create(RetrofitServiceRDSS.class);
        Call<JsonObject> responseVoCall = retrofitServiceRDSS.getTrenchSurvey(object);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            progressDialog.dismiss();
                            loader.setVisibility(View.GONE);
                            database.deleteSSData(ID);
                            JsonArray array33 = surveyedResponseVo.get("DATA").getAsJsonArray();

                            for (int i = 0; i < array33.size(); i++) {
                             /*   EHT_SS_MASTER model = new EHT_SS_MASTER();
                                model.setEHT_SS_CODE(array33.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(array33.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                model.setLONGITUDE(array33.get(i).getAsJsonObject().get("LONGITUDE").getAsString());
                                model.setLATITUDE(array33.get(i).getAsJsonObject().get("LATITUDE").getAsString());
                                ssMasters33.add(model);*/

                                UGPostingModel model = new UGPostingModel();
                                model.setLOCATION_ID(array33.get(i).getAsJsonObject().get("LOCATION_ID").getAsString());
                                model.setLOCATION_NO(array33.get(i).getAsJsonObject().get("LOCATION_NO").getAsString()); // Update index as per the actual table schema
                                model.setSOURCE_LOCATION(array33.get(i).getAsJsonObject().get("SOURCE_LOCATION").getAsString());        // Update index as per the actual table schema
                                model.setSS_NAME(array33.get(i).getAsJsonObject().get("SS_NAME").getAsString());        // Update index as per the actual table schema
                                model.setSS_ID(array33.get(i).getAsJsonObject().get("SS_ID").getAsString());        // Update index as per the actual table schema
                                model.setSS_TYPE(array33.get(i).getAsJsonObject().get("SS_TYPE").getAsString());        // Update index as per the actual table schema
                                model.setCREATED_BY(array33.get(i).getAsJsonObject().get("CREATED_BY").getAsString());        // Update index as per the actual table schema
                                model.setWIDTH(array33.get(i).getAsJsonObject().get("WIDTH").getAsString());        // Update index as per the actual table schema
                                model.setDEPTH(array33.get(i).getAsJsonObject().get("DEPTH").getAsString());
                                JsonObject jsonObject = array33.get(i).getAsJsonObject();
                                if (jsonObject.has("UTILITY") && !jsonObject.get("UTILITY").isJsonNull()) {
                                    model.setUTILITY(jsonObject.get("UTILITY").getAsString());
                                } else {
                                    model.setUTILITY(""); // or null, depending on your model
                                }
                                // Update index as per the actual table schema
                                //model.setUTILITY(array33.get(i).getAsJsonObject().get("UTILITY").getAsString());        // Update index as per the actual table schema
                                model.setCOUNT_CABLES(array33.get(i).getAsJsonObject().get("COUNT_CABLES").getAsString());        // Update index as per the actual table schema
                                model.setCABLE_33KV(array33.get(i).getAsJsonObject().get("CABLE_33KV").getAsString());        // Update index as per the actual table schema
                                model.setCABLE_11KV(array33.get(i).getAsJsonObject().get("CABLE_11KV").getAsString());        // Update index as per the actual table schema
                                model.setCABLE_LT(array33.get(i).getAsJsonObject().get("CABLE_LT").getAsString());        // Update index as per the actual table schema
                                model.setLAT(array33.get(i).getAsJsonObject().get("LAT").getAsString());        // Update index as per the actual table schema
                                model.setLON(array33.get(i).getAsJsonObject().get("LON").getAsString());


                                if (jsonObject.has("LOCATION_TYPE") && !jsonObject.get("LOCATION_TYPE").isJsonNull()) {
                                    model.setLOCATION_TYPE(jsonObject.get("LOCATION_TYPE").getAsString());
                                } else {
                                    model.setLOCATION_TYPE("");
                                }

                                if (jsonObject.has("LOCATION_REMARKS") && !jsonObject.get("LOCATION_REMARKS").isJsonNull()) {
                                    model.setLOCATION_REMARKS(jsonObject.get("LOCATION_REMARKS").getAsString());
                                } else {
                                    model.setLOCATION_REMARKS("");
                                }

                                if (jsonObject.has("RMU_TYPE") && !jsonObject.get("RMU_TYPE").isJsonNull()) {
                                    model.setRMU_TYPE(jsonObject.get("RMU_TYPE").getAsString());
                                } else {
                                    model.setRMU_TYPE("");
                                }

                                if (jsonObject.has("DTR_CAPACITY") && !jsonObject.get("DTR_CAPACITY").isJsonNull()) {
                                    model.setDTR_CAPACITY(jsonObject.get("DTR_CAPACITY").getAsString());
                                } else {
                                    model.setDTR_CAPACITY("");
                                }
                                if (jsonObject.has("HT_SCNO") && !jsonObject.get("HT_SCNO").isJsonNull()) {
                                    model.setHT_SCNO(jsonObject.get("HT_SCNO").getAsString());
                                } else {
                                    model.setHT_SCNO("");
                                }

                                if (jsonObject.has("UTILITY_TYPE") && !jsonObject.get("UTILITY_TYPE").isJsonNull()) {
                                    model.setUTILITY_TYPE(jsonObject.get("UTILITY_TYPE").getAsString());
                                } else {
                                    model.setUTILITY_TYPE("");
                                }

                                if (jsonObject.has("PROTECTION_PIPE") && !jsonObject.get("PROTECTION_PIPE").isJsonNull()) {
                                    model.setPROTECTION_PIPE(jsonObject.get("PROTECTION_PIPE").getAsString());
                                } else {
                                    model.setPROTECTION_PIPE("");
                                }

                                if (jsonObject.has("CABLE_SIZE") && !jsonObject.get("CABLE_SIZE").isJsonNull()) {
                                    model.setCABLE_SIZE(jsonObject.get("CABLE_SIZE").getAsString());
                                } else {
                                    model.setCABLE_SIZE("");
                                }

                                if (jsonObject.has("FEEDERS") && jsonObject.get("FEEDERS").isJsonArray()) {
                                    JsonArray feedersArray = jsonObject.getAsJsonArray("FEEDERS");

                                    ArrayList<FeedersData> feedersData = new ArrayList<>();
                                    for (int j = 0; j < feedersArray.size(); j++) {
                                        JsonObject feederObj = feedersArray.get(j).getAsJsonObject();

                                        FeedersData feeder = new FeedersData();
                                        //feeder.setID(feederObj.has("ID") && !feederObj.get("ID").isJsonNull() ? feederObj.get("ID").getAsString() : "");
                                        //feeder.setLOCATION_ID(feederObj.has("LOCATION_ID") && !feederObj.get("LOCATION_ID").isJsonNull() ? feederObj.get("LOCATION_ID").getAsString() : "");
                                        feeder.setVoltage_level(feederObj.has("VOLTAGE_LEVEL") && !feederObj.get("VOLTAGE_LEVEL").isJsonNull() ? feederObj.get("VOLTAGE_LEVEL").getAsString() : "");
                                        feeder.setFeeder_id(feederObj.has("FEEDER_ID") && !feederObj.get("FEEDER_ID").isJsonNull() ? feederObj.get("FEEDER_ID").getAsString() : "");
                                        feeder.setFeeder_name(feederObj.has("FEEDER_NAME") && !feederObj.get("FEEDER_NAME").isJsonNull() ? feederObj.get("FEEDER_NAME").getAsString() : "");
                                        feeder.setSS_NAME(feederObj.has("SS_NAME") && !feederObj.get("SS_NAME").isJsonNull() ? feederObj.get("SS_NAME").getAsString() : "");
                                        feeder.setSS_ID(feederObj.has("SS_ID") && !feederObj.get("SS_ID").isJsonNull() ? feederObj.get("SS_ID").getAsString() : "");
                                        feeder.setFDR_INDEX(feederObj.has("FDR_INDEX") && !feederObj.get("FDR_INDEX").isJsonNull() ? feederObj.get("FDR_INDEX").getAsInt() : 0);


                                        feedersData.add(feeder);
                                    }

                                    // ✅ Set feeders list to model
                                    model.setFeedersData(feedersData);

                                } else {
                                    // No feeders
                                    model.setFeedersData(new ArrayList<>());
                                }


                                model.setVOLTAGE(SELECTED_VOLTAGE);


                                model.setIS_FRESH_SURVEY("N");
                                ugPostingList.add(model);
                                database.insertRdssPosting(model);
                            }
                            System.out.println("BUTTON_TYPE--->" + button_type);

                            if (button_type.equalsIgnoreCase("Next_33")) {
                                Intent intentPf = new Intent(AssetsHomeRdssUgCable.this, UGCableProposedMap.class);
                                intentPf.putExtra("ID", ID);
                                intentPf.putExtra("NAME", NAME);
                                intentPf.putExtra("VOLTAGE_LEVEL", "33KV");
                                startActivity(intentPf);
                                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                            } else if (button_type.equalsIgnoreCase("Next_11")) {
                                Intent intentPf = new Intent(AssetsHomeRdssUgCable.this, UGCableProposedMap.class);
                                intentPf.putExtra("ID", ID);
                                intentPf.putExtra("NAME", NAME);
                                intentPf.putExtra("VOLTAGE_LEVEL", "11KV");
                                startActivity(intentPf);
                                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                            } else if (button_type.equalsIgnoreCase("VIEW_MAP_33")) {
                                Intent intent = new Intent(AssetsHomeRdssUgCable.this, ViewMapRdssActivity.class);
                                intent.putExtra("DATA_MODEL", ugPostingList);
                                intent.putExtra("ID", ID);
                                intent.putExtra("VOLTAGE", "33KV");
                                startActivity(intent);
                            } else if (button_type.equalsIgnoreCase("VIEW_MAP_11")) {
                                Intent intent = new Intent(AssetsHomeRdssUgCable.this, ViewMapRdssActivity.class);
                                intent.putExtra("DATA_MODEL", ugPostingList);
                                intent.putExtra("ID", ID);
                                intent.putExtra("VOLTAGE", "11KV");
                                startActivity(intent);
                            } else if (button_type.equalsIgnoreCase("Edit")) {
                                showPoleDialog(ugPostingList);
//                                Intent intentPf = new Intent(AssetsHomeRdssUgCable.this, EditOptionsRdss.class);
//                                intentPf.putExtra("ID", ID);
//                                intentPf.putExtra("NAME", NAME);
//                                intentPf.putExtra("UG_MODEL", ugPostingList);
//                                startActivity(intentPf);
//                                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();
                        if (button_type.equalsIgnoreCase("Next_33")) {
                            Intent intentPf = new Intent(AssetsHomeRdssUgCable.this, UGCableProposedMap.class);
                            intentPf.putExtra("ID", ID);
                            intentPf.putExtra("NAME", NAME);
                            intentPf.putExtra("VOLTAGE_LEVEL", "33KV");
                            startActivity(intentPf);
                            overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                        } else if (button_type.equalsIgnoreCase("Next_11")) {
                            Intent intentPf = new Intent(AssetsHomeRdssUgCable.this, UGCableProposedMap.class);
                            intentPf.putExtra("ID", ID);
                            intentPf.putExtra("NAME", NAME);
                            intentPf.putExtra("VOLTAGE_LEVEL", "11KV");
                            startActivity(intentPf);
                            overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                        } else {
                            Toast.makeText(AssetsHomeRdssUgCable.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                        }

                    }


                } else {
                    progressDialog.dismiss();
                    if (button_type.equalsIgnoreCase("Next_33")) {
                        Intent intentPf = new Intent(AssetsHomeRdssUgCable.this, UGCableProposedMap.class);
                        intentPf.putExtra("ID", ID);
                        intentPf.putExtra("NAME", NAME);
                        intentPf.putExtra("VOLTAGE_LEVEL", "33KV");
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    } else if (button_type.equalsIgnoreCase("Next_11")) {
                        Intent intentPf = new Intent(AssetsHomeRdssUgCable.this, UGCableProposedMap.class);
                        intentPf.putExtra("ID", ID);
                        intentPf.putExtra("NAME", NAME);
                        intentPf.putExtra("VOLTAGE_LEVEL", "11KV");
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    } else {
                        Toast.makeText(AssetsHomeRdssUgCable.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                progressDialog.dismiss();
                CommanUtilits.netWorkError(AssetsHomeRdssUgCable.this);
            }
        });

    }

    private void showPoleDialog(ArrayList<UGPostingModel> ugPostingList) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_dialog_asset, null);
        builder.setView(dialogView);

        EditText etPoleNo = dialogView.findViewById(R.id.etPoleNo);
        TextView btnLoadNearby = dialogView.findViewById(R.id.btnLoadNearby);
        TextView search = dialogView.findViewById(R.id.btnSearch);

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        btnLoadNearby.setOnClickListener(v -> {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request permissions if not granted
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1001
                );
                return;
            }

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            //ArrayList<SurveyVo> surveyVosList = arcGISDatabase.getSurveyDataByDistance(lat, lon);
                            ArrayList<UGPostingModel> surveyVosList = database.getUGPostingDataByDistance(gpsTraker.getLatitude(), gpsTraker.getLongitude(), ID);
                            System.out.println("LIST_SIZE --> " + ugPostingList.size());
                            System.out.println("LIST_SIZE --> " + ID);

                            if (!surveyVosList.isEmpty()) {
                                Intent intentEdit = new Intent(AssetsHomeRdssUgCable.this, EditOptionsRdss.class);
                                intentEdit.putExtra("UG_MODEL", ugPostingList);
                                startActivity(intentEdit);
                            } else {
                                Toast.makeText(this, "No poles found", Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        } else {
                            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FakeGPS", "Error getting location: " + e.getMessage());
                        Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show();
                    });
        });


        search.setOnClickListener(v -> {
            ArrayList<UGPostingModel> surveyVosList = database.getUGPostingDataByLocationNoAndSsId(etPoleNo.getText().toString(), ID);
            System.out.println("LIST_SIZE-->" + surveyVosList.size());
            System.out.println("LIST_SIZE --> " + ID);
            if (!surveyVosList.isEmpty()) {
                Intent intentEdit = new Intent(AssetsHomeRdssUgCable.this, EditOptionsRdss.class);
                intentEdit.putExtra("UG_MODEL", surveyVosList);
                startActivity(intentEdit);
            } else {
                Toast.makeText(this, "Invalid Pole Number", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();

        });


        etPoleNo.setOnEditorActionListener((v, actionId, event) -> {
            String poleNo = etPoleNo.getText().toString().trim();
            if (!poleNo.isEmpty()) {
                Toast.makeText(this, "Searching pole: " + poleNo, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                // Your search code here
            }
            return true;
        });
    }

    private void setTransmitData() {
        if (!postingList.isEmpty()) {
            int poles_count_33 = 0;
            int poles_count_11 = 0;

            for (int i = 0; i < postingList.size(); i++) {
                if (postingList.get(i).getVOLTAGE().equalsIgnoreCase("33KV")) {
                    poles_count_33++;
                }
                if (postingList.get(i).getVOLTAGE().equalsIgnoreCase("11KV")) {
                    poles_count_11++;
                }

            }

            poles_33kv.setText("" + postingList.size());
            //poles_11kv.setText(""+poles_count_11);
        }
    }

    private void loadFeederMasterData() {
        loader.setVisibility(View.VISIBLE);

        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("DIV_ID", loginDataResponses.get(0).getDIV_ID());

        retrofitServiceRDSS = RetrofitClientInstanceRDSS.getRetrofitInstance().create(RetrofitServiceRDSS.class);
        Call<JsonObject> responseVoCall = retrofitServiceRDSS.getFeederMaster(requestObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            main_layout.setVisibility(View.VISIBLE);
                            transmit_layout.setVisibility(View.VISIBLE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();
                            double Eht_Distance;
                            double SS_Distance;

                            if (dataObject.has("EHT SS_DISTANCE") && !dataObject.get("EHT SS_DISTANCE").isJsonNull()) {
                                Eht_Distance = dataObject.get("EHT SS_DISTANCE").getAsDouble() / 1000.0;
                            } else {
                                Eht_Distance = 3000.0 / 1000.0;
                            }
                            if (dataObject.has("33/11kV SS_DISTANCE") && !dataObject.get("33/11kV SS_DISTANCE").isJsonNull()) {
                                SS_Distance = dataObject.get("33/11kV SS_DISTANCE").getAsDouble() / 1000.0;
                            } else {
                                SS_Distance = 3000.0;
                            }

                            // double SS_Distance = object.has("33/11kV SS_DISTANCE") ? object.get("33/11kV SS_DISTANCE").getAsDouble() / 1000.0 : 0.0;

                            // double Eht_Distance = dataObject.has("EHT SS_DISTANCE") ? dataObject.get("EHT SS_DISTANCE").getAsDouble() / 1000.0 : 0.0;


                            JsonArray array33 = dataObject.getAsJsonArray("EHT_SS_MASTER_33KV");
                            for (int i = 0; i < array33.size(); i++) {
                                EHT_SS_MASTER model = new EHT_SS_MASTER();
                                model.setEHT_SS_CODE(array33.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(array33.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                model.setLONGITUDE(array33.get(i).getAsJsonObject().get("LONGITUDE").getAsString());
                                model.setLATITUDE(array33.get(i).getAsJsonObject().get("LATITUDE").getAsString());
                                ssMasters33.add(model);
                                ehtSsMasters.add(model);
                            }

                            JsonArray ehtSSMasterNew = dataObject.getAsJsonArray("EHT_SS_MASTER_ALL");
                            HashSet<String> uniqueSubStationsFor33KV = new HashSet<>();
                            for (int i = 0; i < ehtSSMasterNew.size(); i++) {
                                JsonObject item = ehtSSMasterNew.get(i).getAsJsonObject();
                                EHT_SS_MASTER_ALL model = new EHT_SS_MASTER_ALL();
                                model.setEHT_SS_CODE(item.get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(item.get("EHT_SS_NAME").getAsString());
                                model.setLONGITUDE(item.get("LONGITUDE").getAsString());
                                model.setLATITUDE(item.get("LATITUDE").getAsString());

                                String EHT_SS_NAME = item.get("EHT_SS_NAME").getAsString();
                                String EHT_SS_CODE = item.get("EHT_SS_CODE").getAsString();
                                double LONGITUDE = item.get("LONGITUDE").getAsDouble();
                                double LATITUDE = item.get("LATITUDE").getAsDouble();


                                double distance = haversine(currentLat, currentLog, LATITUDE, LONGITUDE);


                                String subStationEntry = EHT_SS_NAME + "~" + EHT_SS_CODE;


                                total33kvSSNames.add(subStationEntry);

                                // ✅ Add to nearby list only if distance <= limit and unique
                                if (distance <= Eht_Distance && uniqueSubStationsFor33KV.add(subStationEntry)) {
                                    nearBy33kvSSNames.add(subStationEntry);
                                }

                                ehtSsMasterAlls.add(model);
                            }


                            JsonArray ssMasterNew = dataObject.getAsJsonArray("SS_MATSER_ALL");
                            HashSet<String> uniqueSubStationsFor11KV = new HashSet<>();
                            for (int i = 0; i < ssMasterNew.size(); i++) {
                                JsonObject item = ssMasterNew.get(i).getAsJsonObject();
                                SS_MASTER_ALL model = new SS_MASTER_ALL();
                                model.setSUB_STATION_ID(item.get("SUB_STATION_ID").getAsString());
                                model.setSUB_STATION_NAME(item.get("SUB_STATION_NAME").getAsString());
                                model.setLONGITUDE(item.get("LONGITUDE").getAsString());
                                model.setLATITUDE(item.get("LATITUDE").getAsString());


                                String SUB_STATION_NAME = item.get("SUB_STATION_NAME").getAsString();
                                String SUB_STATION_ID = item.get("SUB_STATION_ID").getAsString();
                                double LONGITUDE = item.get("LONGITUDE").getAsDouble();
                                double LATITUDE = item.get("LATITUDE").getAsDouble();


                                double distance = haversine(currentLat, currentLog, LATITUDE, LONGITUDE);


                                // ✅ Add to nearby list only if distance <= limit and unique
                                String subStationEntry = SUB_STATION_NAME + "~" + SUB_STATION_ID;
                                total11kvSSNames.add(subStationEntry);
                                if (distance <= SS_Distance && uniqueSubStationsFor11KV.add(subStationEntry)) {
                                    nearBy11kvSSNames.add(subStationEntry);

                                }
                                ssMasterAlls.add(model);

                                // System.out.println("SS_MASTER_LIST" + ssMasterAlls.size());

                            }


                            JsonArray feeder33kv = dataObject.getAsJsonArray("33KV_FEEDER_MASTER");
                            for (int i = 0; i < feeder33kv.size(); i++) {
                                Feeders_33KV model = new Feeders_33KV();
                                model.setEHT_SS_CODE(feeder33kv.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(feeder33kv.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                model.setFEEDER_CODE_33KV(feeder33kv.get(i).getAsJsonObject().get("FEEDER_CODE_33KV").getAsString());
                                model.setFEEDER_NAME_33KV(feeder33kv.get(i).getAsJsonObject().get("FEEDER_NAME_33KV").getAsString());
                                model.setDIVISION_ID(feeder33kv.get(i).getAsJsonObject().get("DIVISION_ID").getAsString());
                                model.setPOLES(feeder33kv.get(i).getAsJsonObject().get("POLES").getAsString());
                                feeders33KV.add(model);

                            }


                            JsonArray array11 = dataObject.getAsJsonArray("SS_MATSER_11KV");
                            for (int i = 0; i < array11.size(); i++) {
                                SS_MASTER_11KV model = new SS_MASTER_11KV();
                                model.setSEC_ID(array11.get(i).getAsJsonObject().get("SEC_ID").getAsString());
                                model.setSEC_NAME(array11.get(i).getAsJsonObject().get("SEC_NAME").getAsString());
                                model.setSUB_STATION_NAME(array11.get(i).getAsJsonObject().get("SUB_STATION_NAME").getAsString());
                                model.setSUB_STATION_ID(array11.get(i).getAsJsonObject().get("SUB_STATION_ID").getAsString());
                                model.setLONGITUDE(array11.get(i).getAsJsonObject().get("LONGITUDE").getAsString());
                                model.setLATITUDE(array11.get(i).getAsJsonObject().get("LATITUDE").getAsString());
                                ssMaster11.add(model);
                                ssMaster11_new.add(model);
                            }


                            JsonArray feeder11 = dataObject.getAsJsonArray("11KV_FEEDER_MASTER");
                            for (int i = 0; i < feeder11.size(); i++) {
                                Feeders_11KV model = new Feeders_11KV();
                                model.setSEC_ID(feeder11.get(i).getAsJsonObject().get("SEC_ID").getAsString());
                                model.setSEC_NAME(feeder11.get(i).getAsJsonObject().get("SEC_NAME").getAsString());
                                model.setSUB_STATION_NAME(feeder11.get(i).getAsJsonObject().get("SUB_STATION_NAME").getAsString());
                                model.setSUB_STATION_ID(feeder11.get(i).getAsJsonObject().get("SUB_STATION_ID").getAsString());
                                model.setFEEDER_NAME(feeder11.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                model.setFEEDER_ID(feeder11.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setPOLES(feeder11.get(i).getAsJsonObject().get("POLES").getAsString());
                                feeders11KV.add(model);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        transmit_layout.setVisibility(View.VISIBLE);
                        Toast.makeText(AssetsHomeRdssUgCable.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    transmit_layout.setVisibility(View.VISIBLE);
                    Toast.makeText(AssetsHomeRdssUgCable.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                transmit_layout.setVisibility(View.VISIBLE);
                Toast.makeText(AssetsHomeRdssUgCable.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void open33KVSSNearByDialog(TextView edt_text) {
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

        ArrayList<String> nearList = new ArrayList<>();
        ArrayList<String> allList = new ArrayList<>();

        for (int i = 0; i < ssMasters33.size(); i++) {
            allList.add(ssMasters33.get(i).getEHT_SS_NAME() + "~" + ssMasters33.get(i).getEHT_SS_CODE());
        }

        for (int i = 0; i < ssMasters33.size(); i++) {
            double distance = getDistanceBetweenPoles33(ssMasters33.get(i).getLATITUDE(), ssMasters33.get(i).getLONGITUDE(), currentLat, currentLog);
            if (distance <= 20000) {
                nearList.add(ssMasters33.get(i).getEHT_SS_NAME() + "~" + ssMasters33.get(i).getEHT_SS_CODE());
            }
        }


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" Nearby EHT SS")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(AssetsHomeRdssUgCable.this);
                    searchableSpinner.setWindowTitle("SELECT EHT SS");
                    searchableSpinner.setSpinnerListItems(nearList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            selectedSSCode33 = selectedString.split("~")[1];
                            selectedSSName33 = selectedString.split("~")[0];

                            ID = selectedSSCode33;
                            NAME = selectedSSName33;
                        }
                    });

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(AssetsHomeRdssUgCable.this);
                    searchableSpinner.setWindowTitle("SELECT EHT SS");
                    searchableSpinner.setSpinnerListItems(allList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            selectedSSCode33 = selectedString.split("~")[1];
                            selectedSSName33 = selectedString.split("~")[0];

                            ID = selectedSSCode33;
                            NAME = selectedSSName33;
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

    private void open11KVSSNearByDialog(TextView edt_text) {
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

        ArrayList<String> nearList = new ArrayList<>();
        ArrayList<String> allList = new ArrayList<>();

        for (int i = 0; i < ssMaster11.size(); i++) {
            allList.add(ssMaster11.get(i).getSUB_STATION_NAME() + "~" + ssMaster11.get(i).getSUB_STATION_ID());
        }

        for (int i = 0; i < ssMaster11.size(); i++) {
            double distance = getDistanceBetweenPoles11(ssMaster11.get(i).getLATITUDE(), ssMaster11.get(i).getLONGITUDE(), currentLat, currentLog);
            if (distance <= 20000) {
                nearList.add(ssMaster11.get(i).getSUB_STATION_NAME() + "~" + ssMaster11.get(i).getSUB_STATION_ID());
            }
        }


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" Nearby 33/11KV SS")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(AssetsHomeRdssUgCable.this);
                    searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                    searchableSpinner.setSpinnerListItems(nearList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            selectedSSCode11 = selectedString.split("~")[1];
                            selectedSSName11 = selectedString.split("~")[0];

                            ID = selectedSSCode11;
                            NAME = selectedSSName11;
                        }
                    });

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(AssetsHomeRdssUgCable.this);
                    searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                    searchableSpinner.setSpinnerListItems(allList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            selectedSSCode11 = selectedString.split("~")[1];
                            selectedSSName11 = selectedString.split("~")[0];

                            ID = selectedSSCode11;
                            NAME = selectedSSName11;
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
                            Toast.makeText(AssetsHomeRdssUgCable.this, "Unable to retrieve location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private double getDistanceBetweenPoles33(String polLat, String polLon, double fromLat, double fromLon) {
        //System.out.println("CURRENT LOCATION LAT : " + fromLat);
        //System.out.println("CURRENT LOCATION LON : " + fromLon);
        //System.out.println("POLE LOCATION LAT : " + polLat);
        //System.out.println("POLE LOCATION LON : " + polLon);
        Location currentStartPoint = new Location("locationM");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationM");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        // System.out.println("POLE LOCATION DISTANCE : " + currentDistance);
        return currentDistance;
    }

    private double getDistanceBetweenPoles11(String polLat, String polLon, double fromLat, double fromLon) {
        //System.out.println("CURRENT LOCATION LAT : " + fromLat);
        //System.out.println("CURRENT LOCATION LON : " + fromLon);
        //System.out.println("POLE LOCATION LAT : " + polLat);
        //System.out.println("POLE LOCATION LON : " + polLon);
        Location currentStartPoint = new Location("locationN");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationN");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        // System.out.println("POLE LOCATION DISTANCE : " + currentDistance);
        return currentDistance;
    }

    //OLD CODE BEFORE FEEDERS
//    private void transmitPoles() {
//
//        btn_transmit.setVisibility(View.GONE);
//        loader_transmit.setVisibility(View.VISIBLE);
//
//        JsonArray array = new JsonArray();
//        for (int i = 0; i < postingList.size(); i++) {
//            JsonObject requestObject = new JsonObject();
//            requestObject.addProperty("LOCATION_ID", postingList.get(i).getLOCATION_ID());
//            requestObject.addProperty("LOCATION_NO", postingList.get(i).getLOCATION_NO());
//            requestObject.addProperty("SOURCE_LOCATION", postingList.get(i).getSOURCE_LOCATION());
//            requestObject.addProperty("SS_NAME", postingList.get(i).getSS_NAME());
//            requestObject.addProperty("SS_ID", postingList.get(i).getSS_ID());
//            requestObject.addProperty("SS_TYPE", postingList.get(i).getSS_TYPE());
//            requestObject.addProperty("CREATED_BY", postingList.get(i).getCREATED_BY());
//            requestObject.addProperty("WIDTH", postingList.get(i).getWIDTH());
//            requestObject.addProperty("DEPTH", postingList.get(i).getDEPTH());
//            requestObject.addProperty("UTILITY", postingList.get(i).getUTILITY());
//            requestObject.addProperty("COUNT_CABLES", postingList.get(i).getCOUNT_CABLES());
//            requestObject.addProperty("CABLE_33KV", postingList.get(i).getCABLE_33KV());
//            requestObject.addProperty("CABLE_11KV", postingList.get(i).getCABLE_11KV());
//            requestObject.addProperty("CABLE_LT", postingList.get(i).getCABLE_LT());
//            requestObject.addProperty("LAT", postingList.get(i).getLAT());
//            requestObject.addProperty("LON", postingList.get(i).getLON());
//            requestObject.addProperty("LOCATION_TYPE", postingList.get(i).getLOCATION_TYPE());
//            requestObject.addProperty("RMU_TYPE", postingList.get(i).getRMU_TYPE());
//            requestObject.addProperty("DTR_CAPACITY", postingList.get(i).getDTR_CAPACITY());
//            requestObject.addProperty("HT_SCNO", postingList.get(i).getHT_SCNO());
//            requestObject.addProperty("UTILITY_TYPE", postingList.get(i).getUTILITY_TYPE());
//            requestObject.addProperty("LOCATION_REMARKS", postingList.get(i).getLOCATION_REMARKS());
//
//
//            array.add(requestObject);
//        }
//
//        Gson gson = new Gson();
//        String json = gson.toJson(array);
//        Log.v("POSTING DATA===>>", "" + json);
//
//
//        retrofitServiceRDSS = RetrofitClientInstanceRDSS.getRetrofitInstance().create(RetrofitServiceRDSS.class);
//        Call<JsonObject> responseVoCall = retrofitServiceRDSS.getUGPostingData(array);
//
//
//        responseVoCall.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                if (response.body() != null) {
//
//                    JsonObject surveyedResponseVo = response.body();
//                    // Log.v("FEEDER==", "" + surveyedResponseVo);
//                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
//                        try {
//                            loader_transmit.setVisibility(View.GONE);
//                            postingList.clear();
//                            database.deletePmiPostingData();
//                            setTransmitData();
//
//                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AssetsHomeRdssUgCable.this);
//                            builder.setTitle("SUCCESSFULLY TRANSMITTED!!");
//                            builder.setMessage("Data Transmitted Successfully!!");
//                            builder.setCancelable(false);
//                            builder.setPositiveButton("OKAY", (dialog, which) -> {
//                                dialog.dismiss();
//                                Intent intent = new Intent(AssetsHomeRdssUgCable.this, UGCablesHome.class);
//                                startActivity(intent);
//                                finish();
//
//                            });
//                            database.clearData();
//                            androidx.appcompat.app.AlertDialog dialog = builder.create();
//                            dialog.show();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        btn_transmit.setVisibility(View.VISIBLE);
//                        loader_transmit.setVisibility(View.GONE);
//                        Toast.makeText(AssetsHomeRdssUgCable.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } else {
//                    btn_transmit.setVisibility(View.VISIBLE);
//                    loader_transmit.setVisibility(View.GONE);
//                    Toast.makeText(AssetsHomeRdssUgCable.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                btn_transmit.setVisibility(View.VISIBLE);
//                loader_transmit.setVisibility(View.GONE);
//                Toast.makeText(AssetsHomeRdssUgCable.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }

    //NEW AFTER FEEDERS CODE
    private void transmitPoles() {
        btn_transmit.setVisibility(View.GONE);
        loader_transmit.setVisibility(View.VISIBLE);

        JsonArray array = new JsonArray();

        for (int i = 0; i < postingList.size(); i++) {
            UGPostingModel model = postingList.get(i);

            JsonObject requestObject = new JsonObject();
            requestObject.addProperty("LOCATION_ID", model.getLOCATION_ID());
            requestObject.addProperty("LOCATION_NO", model.getLOCATION_NO());
            requestObject.addProperty("SOURCE_LOCATION", model.getSOURCE_LOCATION());
            requestObject.addProperty("SS_NAME", model.getSS_NAME());
            requestObject.addProperty("SS_ID", model.getSS_ID());
            requestObject.addProperty("SS_TYPE", model.getSS_TYPE());
            requestObject.addProperty("CREATED_BY", model.getCREATED_BY());
            requestObject.addProperty("WIDTH", model.getWIDTH());
            requestObject.addProperty("DEPTH", model.getDEPTH());
            requestObject.addProperty("UTILITY", model.getUTILITY());
            requestObject.addProperty("COUNT_CABLES", model.getCOUNT_CABLES());
            requestObject.addProperty("CABLE_33KV", model.getCABLE_33KV());
            requestObject.addProperty("CABLE_11KV", model.getCABLE_11KV());
            requestObject.addProperty("CABLE_LT", model.getCABLE_LT());
            requestObject.addProperty("LAT", model.getLAT());
            requestObject.addProperty("LON", model.getLON());
            requestObject.addProperty("LOCATION_TYPE", model.getLOCATION_TYPE());
            requestObject.addProperty("RMU_TYPE", model.getRMU_TYPE());
            requestObject.addProperty("DTR_CAPACITY", model.getDTR_CAPACITY());
            requestObject.addProperty("HT_SCNO", model.getHT_SCNO());
            requestObject.addProperty("UTILITY_TYPE", model.getUTILITY_TYPE());
            requestObject.addProperty("LOCATION_REMARKS", model.getLOCATION_REMARKS());

            // ✅ Add Feeders_Data Array
            JsonArray feedersArray = new JsonArray();
            if (model.getFeedersData() != null && !model.getFeedersData().isEmpty()) {
                for (FeedersData feeder : model.getFeedersData()) {
                    JsonObject feederObj = new JsonObject();
                    feederObj.addProperty("FEEDER_ID", feeder.getFeeder_id());
                    feederObj.addProperty("FEEDER_NAME", feeder.getFeeder_name());
                    feederObj.addProperty("VOLTAGE_LEVEL", feeder.getVoltage_level());
                    feederObj.addProperty("FDR_INDEX", feeder.getFDR_INDEX());
                    feederObj.addProperty("CABLE_SIZE",feeder.getCABLE_SIZE());
                    feederObj.addProperty("SS_NAME",feeder.getSS_NAME());
                    feederObj.addProperty("DTR_CODE",feeder.getDTR_CODE());
                    feederObj.addProperty("REMARKS",feeder.getREMARKS());
                    feederObj.addProperty("DTR_LOCATION",feeder.getDTR_LOCATION());
                    feederObj.addProperty("DTR_PHASE",feeder.getDTR_PHASE());
                    feederObj.addProperty("DTR_CAPACITY",feeder.getDTR_CAPACITY());
                    feedersArray.add(feederObj);
                }
            }

            requestObject.add("FEEDERS", feedersArray);
            //requestObject.addProperty("CABLE_SIZE", model.getCABLE_SIZE());
            requestObject.addProperty("PROTECTION_PIPE", model.getPROTECTION_PIPE());
            requestObject.addProperty("GPS_ACCURACY", model.getGPS_ACCURACY());

            array.add(requestObject);
        }

        Gson gson = new Gson();
        String json = gson.toJson(array);
        Log.v("POSTING DATA===>>", "" + json);

        RetrofitServiceRDSS retrofitServiceRDSS =
                RetrofitClientInstanceRDSS.getRetrofitInstance().create(RetrofitServiceRDSS.class);
        Call<JsonObject> responseVoCall = retrofitServiceRDSS.getUGPostingData(array);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader_transmit.setVisibility(View.GONE);
                            postingList.clear();
                            database.deletePmiPostingData();
                            setTransmitData();
                            openAlertDialog("Data Transmitted Successfully!!");

//                            new AlertDialog.Builder(AssetsHomeRdssUgCable.this)
//                                    .setTitle("Successfully transmitted!!")
//                                    .setMessage("Data Transmitted Successfully!!")
//                                    .setCancelable(false)
//                                    .setPositiveButton("OKAY", (dialog, which) -> {
//                                        dialog.dismiss();
//                                        startActivity(new Intent(AssetsHomeRdssUgCable.this, UGCablesHome.class));
//                                        finish();
//                                    })
//                                    .show();

                            database.clearData();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        btn_transmit.setVisibility(View.VISIBLE);
                        loader_transmit.setVisibility(View.GONE);
                        Toast.makeText(AssetsHomeRdssUgCable.this,
                                surveyedResponseVo.get("ERR_MSG").toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    btn_transmit.setVisibility(View.VISIBLE);
                    loader_transmit.setVisibility(View.GONE);
                    Toast.makeText(AssetsHomeRdssUgCable.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                btn_transmit.setVisibility(View.VISIBLE);
                loader_transmit.setVisibility(View.GONE);
                Toast.makeText(AssetsHomeRdssUgCable.this, "Try Again " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_to_bottom);
    }

    private void openAlertDialog(String message){
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.suc));
        // Set Data
        title.setText("Successfully transmitted!!");
        msg.setText(message);

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        okBtn.setText("Okay");

        // Click action
        okBtn.setOnClickListener(v -> {
            startActivity(new Intent(AssetsHomeRdssUgCable.this, UGCablesHome.class));
            finish();
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

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
