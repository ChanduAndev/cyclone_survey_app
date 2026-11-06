package com.apepdcl.gis_survey.RDSS;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.models.EHT_SS_MASTER_ALL;
import com.apepdcl.gis_survey.PMI.models.FeedersData;
import com.apepdcl.gis_survey.PMI.models.SS_MASTER_ALL;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;

import com.apepdcl.gis.app.R;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by SOUBHAGYA SWAIN on 19-05-2025.
 */
public class UGCableProposedMap extends AppCompatActivity {

    String ID, NAME, VOLTAGE_LEVEL = "";
    RelativeLayout back_layout, current_location_layout;
    MapView mv;
    GpsTraker gpsTraker, currentLocationGpsTracker;
    LocationDisplay mLocationDisplay;
    GraphicsOverlay mGraphicsOverlay;
    ArcGISMap map;
    public static float gpsAccurcy = 0;

    TextView name_tv, name_value;
    TextView id_tv, id_value;
    PointCollection polylinePoints;
    GraphicsOverlay graphicsOverlay;
    private Point ptWgs84;
    Graphic graphic;

    ArrayList<UGPostingModel> filteredPolesList33;
    ArrayList<UGPostingModel> filteredPolesList11;

    ArrayList<UGPostingModel> polesList33;
    ArrayList<UGPostingModel> polesList11;

    RdssUGDatabase database;
    private SharedPreferences preferences;
    RelativeLayout reload_map_layout;
    private boolean isDialogShown = false;

    private double latitude, longitude;
    private TextView home_heading, tv_feeder_name_new;
    private LinearLayout layout_no_of_feeders_33kv_new, layout_no_of_feeders_11kv_new, layout_no_of_feeders_lt_new;


    @Override
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ug_proposed_map);
        getSupportActionBar().hide();

        database = new RdssUGDatabase(this);

        ID = getIntent().getStringExtra("ID");
        NAME = getIntent().getStringExtra("NAME");
        VOLTAGE_LEVEL = getIntent().getStringExtra("VOLTAGE_LEVEL");
        preferences = PreferenceManager.getDefaultSharedPreferences(UGCableProposedMap.this);

        filteredPolesList33 = new ArrayList<>();
        filteredPolesList11 = new ArrayList<>();

        back_layout = findViewById(R.id.back_layout);
        current_location_layout = findViewById(R.id.close_layout);
        mv = findViewById(R.id.map1);
        name_tv = findViewById(R.id.name_tv);
        name_value = findViewById(R.id.name_value);
        id_tv = findViewById(R.id.id_tv);
        id_value = findViewById(R.id.id_value);
        home_heading = findViewById(R.id.home_heading);
        tv_feeder_name_new = findViewById(R.id.tv_feeder_name_new);

        name_value.setText(NAME);
        id_value.setText(ID);
        reload_map_layout = findViewById(R.id.reload_map_layout);
        gpsTraker = new GpsTraker(this);
        mGraphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(mGraphicsOverlay);

        System.out.println("SELCTED_VOLTAGE_LEVEL--->" + VOLTAGE_LEVEL);
        tv_feeder_name_new.setText(NAME + " (" + ID + ")");
        if (VOLTAGE_LEVEL.equalsIgnoreCase("33KV")) {
            home_heading.setText("EHT SS Trench Survey");
        } else {
            home_heading.setText("33/11kv SS Trench Survey");
        }


        map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
        mv.setMap(map);
        mLocationDisplay = mv.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }


            String message = String.format("Error in DataSourceStatusChangedListener: %s",
                    dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
            Toast.makeText(UGCableProposedMap.this, message, Toast.LENGTH_LONG).show();

        });
        // mLocationDisplay.stop();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();


        mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                latitude = 0.0;
                longitude = 0.0;
                gpsAccurcy = gpsTraker.getLocation().getAccuracy();
                // Convert screen touch point to map point
                android.graphics.Point screenPoint = new android.graphics.Point(
                        Math.round(e.getX()),
                        Math.round(e.getY())
                );
                Point mapPoint =
                        mv.screenToLocation(screenPoint);
                if (mapPoint != null) {
                    Point wgs84Point =
                            (Point)
                                    GeometryEngine
                                            .project(mapPoint,
                                                    SpatialReferences.getWgs84());
                    latitude = wgs84Point.getY();
                    longitude = wgs84Point.getX();
                    System.out.println("GGRWUIYIOYFGO::::1111 " + latitude + " " + longitude + " "+ gpsAccurcy);

                }
                openAttributeDialog(e, mMapView, VOLTAGE_LEVEL,String.valueOf(gpsAccurcy));
                return true;
            }

        });


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
                if (ConnectivityChecking.checkConnection(UGCableProposedMap.this)) {
                    gpsTraker.getLocation();
                    map = new ArcGISMap(
                            Basemap.Type.IMAGERY_WITH_LABELS,
                            gpsTraker.getLatitude(),
                            gpsTraker.getLongitude(),
                            20
                    );
                    mv.setMap(map);
                } else {
                    CommanUtilits.internetDialog(UGCableProposedMap.this);

                }
            }
        });


    }

    private void repeatPole(Spinner spinner, Spinner spinnerDepth, Spinner spinner33kv,
                            Spinner spinner11kv, Spinner spinnerLt, String width, String depth,
                            String no_of_33kv, String no_of_11kv, String no_of_lt, String UTILITY_TYPE,
                            String UTILITY, CheckBox checkBox, Spinner sp_utility_type_rdss, EditText et_utility,
                            ArrayList<FeedersData> feedersData, TextView new_sp_sub_station_name_33_feeder_1,
                            TextView new_sp_sub_station_name_33_feeder_2, TextView new_sp_sub_station_name_33_feeder_3,
                            TextView new_sp_sub_station_name_33_feeder_4, TextView new_sp_sub_station_name_33_feeder_5,
                            TextView new_sp_sub_station_name_33_feeder_6, TextView new_sp_sub_station_name_33_feeder_7,
                            TextView new_sp_sub_station_name_33_feeder_8, TextView new_sp_sub_station_name_33_feeder_9,
                            TextView new_sp_sub_station_name_33_feeder_10, TextView new_et_feeder_no_1_33kv, TextView new_et_feeder_no_2_33kv,
                            TextView new_et_feeder_no_3_33kv, TextView new_et_feeder_no_4_33kv, TextView new_et_feeder_no_5_33kv, TextView new_et_feeder_no_6_33kv,
                            TextView new_et_feeder_no_7_33kv, TextView new_et_feeder_no_8_33kv, TextView new_et_feeder_no_9_33kv, TextView new_et_feeder_no_10_33kv,


                            TextView new_sp_sub_station_name_11_feeder_1, TextView new_sp_sub_station_name_11_feeder_2, TextView new_sp_sub_station_name_11_feeder_3,
                            TextView new_sp_sub_station_name_11_feeder_4, TextView new_sp_sub_station_name_11_feeder_5,
                            TextView new_sp_sub_station_name_11_feeder_6, TextView new_sp_sub_station_name_11_feeder_7,
                            TextView new_sp_sub_station_name_11_feeder_8, TextView new_sp_sub_station_name_11_feeder_9,
                            TextView new_sp_sub_station_name_11_feeder_10, TextView new_et_feeder_no_1_11kv, TextView new_et_feeder_no_2_11kv,
                            TextView new_et_feeder_no_3_11kv, TextView new_et_feeder_no_4_11kv, TextView new_et_feeder_no_5_11kv, TextView new_et_feeder_no_6_11kv,
                            TextView new_et_feeder_no_7_11kv, TextView new_et_feeder_no_8_11kv, TextView new_et_feeder_no_9_11kv, TextView new_et_feeder_no_10_11kv,
                            TextView thirty_kv, TextView eleven_kv, TextView lt_feeder, LinearLayout layout_no_of_feeders_33kv, LinearLayout layout_no_of_feeders_11kv, LinearLayout layout_no_of_feeders_lt
    ) {

        // ---- Width spinner setup ----
        List<String> width_list = new ArrayList<>();
        if (width.equalsIgnoreCase("0.6")) {
            width_list.add("0.6");
            width_list.add("1");
        } else {
            width_list.add("1");
            width_list.add("0.6");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, width_list);
        spinner.setAdapter(adapter);

        // ---- Depth spinner setup ----
        List<String> depth_list = new ArrayList<>();
        if (depth.equalsIgnoreCase("0.9")) {
            depth_list.add("0.9");
            depth_list.add("1");
            depth_list.add("1.25");
            depth_list.add("1.5");
        } else if (depth.equalsIgnoreCase("1")) {
            depth_list.add("1");
            depth_list.add("1.25");
            depth_list.add("1.5");
            depth_list.add("0.9");
        } else if (depth.equalsIgnoreCase("1.25")) {
            depth_list.add("1.25");
            depth_list.add("1.5");
            depth_list.add("0.9");
            depth_list.add("1");
        } else if (depth.equalsIgnoreCase("1.5")) {
            depth_list.add("1.5");
            depth_list.add("1.25");
            depth_list.add("0.9");
            depth_list.add("1");
        }

        ArrayAdapter<String> adapterDepth = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, depth_list);
        spinnerDepth.setAdapter(adapterDepth);


        List<String> no_of_33kv_list = new ArrayList<>();
        int no_of_33KV_count = Integer.parseInt(String.valueOf(no_of_33kv));
        int no_of_11kv_count = Integer.parseInt(String.valueOf(no_of_11kv));
        int no_of_lt_count = Integer.parseInt(String.valueOf(no_of_lt));
//        if (no_of_33KV_count > 0) {
//            thirty_kv.setSelected(true);
//            layout_no_of_feeders_33kv.setVisibility(View.VISIBLE);
//            layout_no_of_feeders_11kv.setVisibility(View.VISIBLE);
//            layout_no_of_feeders_lt.setVisibility(View.VISIBLE);
//
//        }
//        if (no_of_11kv_count > 0) {
//            eleven_kv.setSelected(true);
//            layout_no_of_feeders_33kv.setVisibility(View.VISIBLE);
//            layout_no_of_feeders_11kv.setVisibility(View.VISIBLE);
//            layout_no_of_feeders_lt.setVisibility(View.VISIBLE);
//        }
//        if (no_of_lt_count > 0) {
//            lt_feeder.setSelected(true);
//            layout_no_of_feeders_33kv.setVisibility(View.VISIBLE);
//            layout_no_of_feeders_11kv.setVisibility(View.VISIBLE);
//            layout_no_of_feeders_lt.setVisibility(View.VISIBLE);
//        }

        no_of_33kv_list.add(no_of_33kv);

        for (int i = 0; i <= 10; i++) {
            String value = String.valueOf(i);
            if (!value.equalsIgnoreCase(no_of_33kv)) {
                no_of_33kv_list.add(value);
            }
        }

        ArrayAdapter<String> adapter33kv = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, no_of_33kv_list);
        spinner33kv.setAdapter(adapter33kv);


        List<String> no_of_11kv_list = new ArrayList<>();

        no_of_11kv_list.add(no_of_11kv);

        for (int i = 0; i <= 10; i++) {
            String value = String.valueOf(i);
            if (!value.equalsIgnoreCase(no_of_11kv)) {
                no_of_11kv_list.add(value);
            }
        }

        ArrayAdapter<String> adapter11kv = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, no_of_11kv_list);
        spinner11kv.setAdapter(adapter11kv);


        List<String> no_of_lt_list = new ArrayList<>();

        no_of_lt_list.add(no_of_lt);

        for (int i = 0; i <= 5; i++) {
            String value = String.valueOf(i);
            if (!value.equalsIgnoreCase(no_of_lt)) {
                no_of_lt_list.add(value);
            }
        }

        ArrayAdapter<String> adapterLT = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, no_of_lt_list);
        spinnerLt.setAdapter(adapterLT);

        if (UTILITY_TYPE != null && !UTILITY_TYPE.isEmpty()) {
            checkBox.setChecked(true);

            List<String> allUtilityTypes = Arrays.asList(
                    "None",
                    "Water pipelines",
                    "Sewage pipelines",
                    "Gas pipelines",
                    "Culvert",
                    "NH crossing",
                    "Railway crossing",
                    "Iron ore pipelines",
                    "Road crossing",
                    "220kv/132KV cable crossings"
            );

            List<String> utility_type_list = new ArrayList<>();
            // Add the selected type first
            utility_type_list.add(UTILITY_TYPE);

            // Add the rest (excluding the selected one)
            for (String type : allUtilityTypes) {
                if (!type.equalsIgnoreCase(UTILITY_TYPE)) {
                    utility_type_list.add(type);
                }
            }

            ArrayAdapter<String> adapterUtilityType = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, utility_type_list);
            sp_utility_type_rdss.setAdapter(adapterUtilityType);

            if (!UTILITY.isEmpty()) {
                et_utility.setText(UTILITY);

            }


        }

        if (feedersData != null && !feedersData.isEmpty()) {
            // Step 1: Filter 33KV feeders
            List<FeedersData> feeders33kv = new ArrayList<>();
            for (FeedersData feeder : feedersData) {
                if (feeder.getVoltage_level() != null &&
                        feeder.getVoltage_level().equalsIgnoreCase("33KV")) {
                    feeders33kv.add(feeder);
                }
            }

            // Step 2: Count 33KV feeders
            int count33kv = feeders33kv.size();
            Log.d("FEEDER_33KV_COUNT", "Total 33KV Feeders: " + count33kv);

            for (int i = 0; i < count33kv; i++) {
                FeedersData feeder = feeders33kv.get(i);
                String formatted = feeder.getFeeder_name() + "~" + feeder.getFeeder_id();
                String formattedSS = feeder.getSS_NAME() + "~" + feeder.getSS_ID();

                switch (i + 1) {
                    case 1:
                        new_sp_sub_station_name_33_feeder_1.setText(formattedSS);
                        new_et_feeder_no_1_33kv.setText(formatted);
                        break;
                    case 2:
                        new_sp_sub_station_name_33_feeder_2.setText(formattedSS);
                        new_et_feeder_no_2_33kv.setText(formatted);
                        break;
                    case 3:
                        new_sp_sub_station_name_33_feeder_3.setText(formattedSS);
                        new_et_feeder_no_3_33kv.setText(formatted);
                        break;
                    case 4:
                        new_sp_sub_station_name_33_feeder_4.setText(formattedSS);
                        new_et_feeder_no_4_33kv.setText(formatted);
                        break;
                    case 5:
                        new_sp_sub_station_name_33_feeder_5.setText(formattedSS);
                        new_et_feeder_no_5_33kv.setText(formatted);
                        break;
                    case 6:
                        new_sp_sub_station_name_33_feeder_6.setText(formattedSS);
                        new_et_feeder_no_6_33kv.setText(formatted);
                        break;
                    case 7:
                        new_sp_sub_station_name_33_feeder_7.setText(formattedSS);
                        new_et_feeder_no_7_33kv.setText(formatted);
                        break;
                    case 8:
                        new_sp_sub_station_name_33_feeder_8.setText(formattedSS);
                        new_et_feeder_no_8_33kv.setText(formatted);
                        break;
                    case 9:
                        new_sp_sub_station_name_33_feeder_9.setText(formattedSS);
                        new_et_feeder_no_9_33kv.setText(formatted);
                        break;
                    case 10:
                        new_sp_sub_station_name_33_feeder_10.setText(formattedSS);
                        new_et_feeder_no_10_33kv.setText(formatted);
                        break;
                }
            }


            // 11KV Repeat Feeders

            List<FeedersData> feeders11kv = new ArrayList<>();
            for (FeedersData feeder : feedersData) {
                if (feeder.getVoltage_level() != null &&
                        feeder.getVoltage_level().equalsIgnoreCase("11KV")) {
                    feeders11kv.add(feeder);
                }
            }

            // Step 2: Count 11KV feeders
            int count11kv = feeders11kv.size();
            Log.d("FEEDER_11KV_COUNT", "Total 11KV Feeders: " + count11kv);

            for (int i = 0; i < count11kv; i++) {
                FeedersData feeder = feeders11kv.get(i);
                String formatted = feeder.getFeeder_name() + "~" + feeder.getFeeder_id();
                String formattedSS = feeder.getSS_NAME() + "~" + feeder.getSS_ID();


                switch (i + 1) {
                    case 1:
                        new_sp_sub_station_name_11_feeder_1.setText(formattedSS);
                        new_et_feeder_no_1_11kv.setText(formatted);
                        Log.d("FEEDER_11KV_DETAILS", "SUBSTATIONS " + formattedSS + "Feeder" + formatted);
                        break;
                    case 2:
                        new_sp_sub_station_name_11_feeder_2.setText(formattedSS);
                        new_et_feeder_no_2_11kv.setText(formatted);
                        break;
                    case 3:
                        new_sp_sub_station_name_11_feeder_3.setText(formattedSS);
                        new_et_feeder_no_3_11kv.setText(formatted);
                        break;
                    case 4:
                        new_sp_sub_station_name_11_feeder_4.setText(formattedSS);
                        new_et_feeder_no_4_11kv.setText(formatted);
                        break;
                    case 5:
                        new_sp_sub_station_name_11_feeder_5.setText(formattedSS);
                        new_et_feeder_no_5_11kv.setText(formatted);
                        break;
                    case 6:
                        new_sp_sub_station_name_11_feeder_6.setText(formattedSS);
                        new_et_feeder_no_6_11kv.setText(formatted);
                        break;
                    case 7:
                        new_sp_sub_station_name_11_feeder_7.setText(formattedSS);
                        new_et_feeder_no_7_11kv.setText(formatted);
                        break;
                    case 8:
                        new_sp_sub_station_name_11_feeder_8.setText(formattedSS);
                        new_et_feeder_no_8_11kv.setText(formatted);
                        break;
                    case 9:
                        new_sp_sub_station_name_11_feeder_9.setText(formattedSS);
                        new_et_feeder_no_9_11kv.setText(formatted);
                        break;
                    case 10:
                        new_sp_sub_station_name_11_feeder_10.setText(formattedSS);
                        new_et_feeder_no_10_11kv.setText(formatted);
                        break;
                }
            }


        }

        if (!new_sp_sub_station_name_33_feeder_1.getText().toString().isEmpty()) {
            new_et_feeder_no_1_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_2.getText().toString().isEmpty()) {
            new_et_feeder_no_2_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_3.getText().toString().isEmpty()) {
            new_et_feeder_no_3_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_4.getText().toString().isEmpty()) {
            new_et_feeder_no_4_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_5.getText().toString().isEmpty()) {
            new_et_feeder_no_5_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_6.getText().toString().isEmpty()) {
            new_et_feeder_no_6_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_7.getText().toString().isEmpty()) {
            new_et_feeder_no_7_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_8.getText().toString().isEmpty()) {
            new_et_feeder_no_8_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_9.getText().toString().isEmpty()) {
            new_et_feeder_no_9_33kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_33_feeder_10.getText().toString().isEmpty()) {
            new_et_feeder_no_10_33kv.setVisibility(View.VISIBLE);
        }

        if (!new_sp_sub_station_name_11_feeder_1.getText().toString().isEmpty()) {
            new_et_feeder_no_1_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_2.getText().toString().isEmpty()) {
            new_et_feeder_no_2_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_3.getText().toString().isEmpty()) {
            new_et_feeder_no_3_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_4.getText().toString().isEmpty()) {
            new_et_feeder_no_4_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_5.getText().toString().isEmpty()) {
            new_et_feeder_no_5_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_6.getText().toString().isEmpty()) {
            new_et_feeder_no_6_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_7.getText().toString().isEmpty()) {
            new_et_feeder_no_7_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_8.getText().toString().isEmpty()) {
            new_et_feeder_no_8_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_9.getText().toString().isEmpty()) {
            new_et_feeder_no_9_11kv.setVisibility(View.VISIBLE);
        }
        if (!new_sp_sub_station_name_11_feeder_10.getText().toString().isEmpty()) {
            new_et_feeder_no_10_11kv.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onBackPressed() {
        Intent playIntent = new Intent(UGCableProposedMap.this, UGCablesHome.class); //Intent.ACTION_VIEW);
        startActivity(playIntent);
        finish();
    }

    private String selectedSSCode33_1 = "", selectedSSName33_1 = "", selectedSSCode11_1 = "", selectedSSName11_1 = "";
    private String selectedSSCode33_2 = "", selectedSSName33_2 = "", selectedSSCode11_2 = "", selectedSSName11_2 = "";
    private String selectedSSCode33_3 = "", selectedSSName33_3 = "", selectedSSCode11_3 = "", selectedSSName11_3 = "";
    private String selectedSSCode33_4 = "", selectedSSName33_4 = "", selectedSSCode11_4 = "", selectedSSName11_4 = "";
    private String selectedSSCode33_5 = "", selectedSSName33_5 = "", selectedSSCode11_5 = "", selectedSSName11_5 = "";
    private String selectedSSCode33_6 = "", selectedSSName33_6 = "", selectedSSCode11_6 = "", selectedSSName11_6 = "";
    private String selectedSSCode33_7 = "", selectedSSName33_7 = "", selectedSSCode11_7 = "", selectedSSName11_7 = "";
    private String selectedSSCode33_8 = "", selectedSSName33_8 = "", selectedSSCode11_8 = "", selectedSSName11_8 = "";
    private String selectedSSCode33_9 = "", selectedSSName33_9 = "", selectedSSCode11_9 = "", selectedSSName11_9 = "";
    private String selectedSSCode33_10 = "", selectedSSName33_10 = "", selectedSSCode11_10 = "", selectedSSName11_10 = "";

    private void openAttributeDialog(MotionEvent e, MapView mMapView, String VOLTAGE_LEVEL, String gpSAccuracy) {

        ArrayList<UGPostingModel> totalList = database.getUGPostingData();
        System.out.println("LIST SIZE TOTAL : " + totalList.size());
        System.out.println("SELECTED_VOLTAGE : " + AssetsHomeRdssUgCable.SELECTED_VOLTAGE);
        System.out.println("SELECTED_ID : " + ID);
        ArrayList<UGPostingModel> currentList = new ArrayList<>();

        for (int i = 0; i < totalList.size(); i++) {
            if (totalList.get(i).getVOLTAGE().equalsIgnoreCase(AssetsHomeRdssUgCable.SELECTED_VOLTAGE)) {
                if (totalList.get(i).getSS_ID().equalsIgnoreCase(ID)) {
                    currentList.add(totalList.get(i));
                }
            }
        }

        System.out.println("LIST SIZE CURRENT : " + currentList.size());


        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.ug_attribute_dialog);
        payDialog.getWindow().setLayout(width, height);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        RelativeLayout back_layout = payDialog.findViewById(R.id.back_layout);
        RelativeLayout close_layout = payDialog.findViewById(R.id.close_layout);
        TextView pole_no = payDialog.findViewById(R.id.pole_no);
        TextView from_pole = payDialog.findViewById(R.id.from_pole);
        LinearLayout layout_from_pole = payDialog.findViewById(R.id.layout_source_location);
        LinearLayout layout_rmu_type_rdss = payDialog.findViewById(R.id.layout_rmu_type_rdss);
        LinearLayout layout_dtr_capacity_rdss = payDialog.findViewById(R.id.layout_dtr_capacity_rdss);
        LinearLayout layout_ht_sc_no_rdss = payDialog.findViewById(R.id.layout_ht_sc_no_rdss);
        LinearLayout layout_utility_type_rdss = payDialog.findViewById(R.id.layout_utility_type_rdss);
        Spinner sp_conductor_size_33 = payDialog.findViewById(R.id.sp_conductor_size_33);
        Spinner sp_conductor_size_11 = payDialog.findViewById(R.id.sp_conductor_size_11);
        CheckBox checkbox_gi_pipes = payDialog.findViewById(R.id.checkbox_gi_pipes);
        CheckBox checkbox_dwc_pipes = payDialog.findViewById(R.id.checkbox_dwc_pipes);
        TextView gi_pipe = payDialog.findViewById(R.id.gi_pipe);
        TextView dwc_pipe = payDialog.findViewById(R.id.dwc_pipe);
        TextView[] no_of_cables_33kv = new TextView[10];
        TextView[] no_of_cables_11kv = new TextView[10];
        TextView[] no_of_cables_lt = new TextView[10];
        LinearLayout[] layout_dtr_details_lt = new LinearLayout[10];
        Spinner[] sp_dtr_capacity_lt = new Spinner[10];
        Spinner[] sp_cable_size_lt = new Spinner[10];
        Spinner[] sp_dtr_phase_lt = new Spinner[10];
        EditText[] et_dtr_str_code = new EditText[10];
        EditText[] et_dtr_loc_name = new EditText[10];
        EditText[] remarks_33kv = new EditText[10];
        EditText[] remarks_11kv = new EditText[10];
        EditText[] et_remarks_lt = new EditText[10];
        AtomicReference<String> protectionType = new AtomicReference<>("");

        AtomicReference<String> selectedCount_33kv = new AtomicReference<>("");
        AtomicReference<String> selectedCount_11kv = new AtomicReference<>("");
        AtomicReference<String> selectedCount_lt = new AtomicReference<>("");


        TextView thirty_kv = payDialog.findViewById(R.id.thirty_kv);
        TextView eleven_kv = payDialog.findViewById(R.id.eleven_kv);
        TextView lt_feeder = payDialog.findViewById(R.id.lt_feeder);
        LinearLayout layout_no_of_feeders_33kv_new = payDialog.findViewById(R.id.layout_no_of_feeders_33kv_new);
        LinearLayout layout_no_of_feeders_11kv_new = payDialog.findViewById(R.id.layout_no_of_feeders_11kv_new);
        LinearLayout layout_no_of_feeders_lt_new = payDialog.findViewById(R.id.layout_no_of_feeders_lt_new);

        boolean selected_33kv = false;
        boolean selected_11kv = false;
        boolean selected_lt = false;

        checkbox_gi_pipes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox_dwc_pipes.setChecked(false);
                }
            }
        });
        checkbox_dwc_pipes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkbox_gi_pipes.setChecked(false);
                }
            }
        });

        if (VOLTAGE_LEVEL.equalsIgnoreCase("33KV")) {
            sp_conductor_size_33.setVisibility(View.GONE);
        } else {
            sp_conductor_size_11.setVisibility(View.GONE);
        }


        //Repeat Pole
        //TextView btn_repeat_pole = payDialog.findViewById(R.id.btn_repeat_pole);
        RelativeLayout layout_repeat = payDialog.findViewById(R.id.layout_repeat);


        //feeder selection new
        LinearLayout new_layout_11kv_1 = payDialog.findViewById(R.id.new_layout_11kv_1);
        LinearLayout new_layout_11kv_2 = payDialog.findViewById(R.id.new_layout_11kv_2);
        LinearLayout new_layout_11kv_3 = payDialog.findViewById(R.id.new_layout_11kv_3);
        LinearLayout new_layout_11kv_4 = payDialog.findViewById(R.id.new_layout_11kv_4);
        LinearLayout new_layout_11kv_5 = payDialog.findViewById(R.id.new_layout_11kv_5);

        LinearLayout new_layout_11kv_6 = payDialog.findViewById(R.id.new_layout_11kv_6);
        LinearLayout new_layout_11kv_7 = payDialog.findViewById(R.id.new_layout_11kv_7);
        LinearLayout new_layout_11kv_8 = payDialog.findViewById(R.id.new_layout_11kv_8);
        LinearLayout new_layout_11kv_9 = payDialog.findViewById(R.id.new_layout_11kv_9);
        LinearLayout new_layout_11kv_10 = payDialog.findViewById(R.id.new_layout_11kv_10);


        LinearLayout new_layout_33kv_1 = payDialog.findViewById(R.id.new_layout_33kv_1);
        LinearLayout new_layout_33kv_2 = payDialog.findViewById(R.id.new_layout_33kv_2);
        LinearLayout new_layout_33kv_3 = payDialog.findViewById(R.id.new_layout_33kv_3);
        LinearLayout new_layout_33kv_4 = payDialog.findViewById(R.id.new_layout_33kv_4);
        LinearLayout new_layout_33kv_5 = payDialog.findViewById(R.id.new_layout_33kv_5);

        LinearLayout new_layout_33kv_6 = payDialog.findViewById(R.id.new_layout_33kv_6);
        LinearLayout new_layout_33kv_7 = payDialog.findViewById(R.id.new_layout_33kv_7);
        LinearLayout new_layout_33kv_8 = payDialog.findViewById(R.id.new_layout_33kv_8);
        LinearLayout new_layout_33kv_9 = payDialog.findViewById(R.id.new_layout_33kv_9);
        LinearLayout new_layout_33kv_10 = payDialog.findViewById(R.id.new_layout_33kv_10);


        TextView new_sp_sub_station_name_11_feeder_1 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_1);
        TextView new_sp_sub_station_name_11_feeder_2 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_2);
        TextView new_sp_sub_station_name_11_feeder_3 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_3);
        TextView new_sp_sub_station_name_11_feeder_4 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_4);
        TextView new_sp_sub_station_name_11_feeder_5 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_5);

        TextView new_sp_sub_station_name_11_feeder_6 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_6);
        TextView new_sp_sub_station_name_11_feeder_7 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_7);
        TextView new_sp_sub_station_name_11_feeder_8 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_8);
        TextView new_sp_sub_station_name_11_feeder_9 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_9);
        TextView new_sp_sub_station_name_11_feeder_10 = payDialog.findViewById(R.id.new_sp_sub_station_name_11_feeder_10);


        TextView new_et_feeder_no_1_11kv = payDialog.findViewById(R.id.new_et_feeder_no_1_11kv);
        TextView new_et_feeder_no_2_11kv = payDialog.findViewById(R.id.new_et_feeder_no_2_11kv);
        TextView new_et_feeder_no_3_11kv = payDialog.findViewById(R.id.new_et_feeder_no_3_11kv);
        TextView new_et_feeder_no_4_11kv = payDialog.findViewById(R.id.new_et_feeder_no_4_11kv);
        TextView new_et_feeder_no_5_11kv = payDialog.findViewById(R.id.new_et_feeder_no_5_11kv);

        TextView new_et_feeder_no_6_11kv = payDialog.findViewById(R.id.new_et_feeder_no_6_11kv);
        TextView new_et_feeder_no_7_11kv = payDialog.findViewById(R.id.new_et_feeder_no_7_11kv);
        TextView new_et_feeder_no_8_11kv = payDialog.findViewById(R.id.new_et_feeder_no_8_11kv);
        TextView new_et_feeder_no_9_11kv = payDialog.findViewById(R.id.new_et_feeder_no_9_11kv);
        TextView new_et_feeder_no_10_11kv = payDialog.findViewById(R.id.new_et_feeder_no_10_11kv);


        // 33kv

        TextView new_sp_sub_station_name_33_feeder_1 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_1);
        TextView new_sp_sub_station_name_33_feeder_2 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_2);
        TextView new_sp_sub_station_name_33_feeder_3 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_3);
        TextView new_sp_sub_station_name_33_feeder_4 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_4);
        TextView new_sp_sub_station_name_33_feeder_5 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_5);
        TextView new_sp_sub_station_name_33_feeder_6 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_6);
        TextView new_sp_sub_station_name_33_feeder_7 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_7);
        TextView new_sp_sub_station_name_33_feeder_8 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_8);
        TextView new_sp_sub_station_name_33_feeder_9 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_9);
        TextView new_sp_sub_station_name_33_feeder_10 = payDialog.findViewById(R.id.new_sp_sub_station_name_33_feeder_10);


        TextView new_et_feeder_no_1_33kv = payDialog.findViewById(R.id.new_et_feeder_no_1_33kv);
        TextView new_et_feeder_no_2_33kv = payDialog.findViewById(R.id.new_et_feeder_no_2_33kv);
        TextView new_et_feeder_no_3_33kv = payDialog.findViewById(R.id.new_et_feeder_no_3_33kv);
        TextView new_et_feeder_no_4_33kv = payDialog.findViewById(R.id.new_et_feeder_no_4_33kv);
        TextView new_et_feeder_no_5_33kv = payDialog.findViewById(R.id.new_et_feeder_no_5_33kv);
        TextView new_et_feeder_no_6_33kv = payDialog.findViewById(R.id.new_et_feeder_no_6_33kv);
        TextView new_et_feeder_no_7_33kv = payDialog.findViewById(R.id.new_et_feeder_no_7_33kv);
        TextView new_et_feeder_no_8_33kv = payDialog.findViewById(R.id.new_et_feeder_no_8_33kv);
        TextView new_et_feeder_no_9_33kv = payDialog.findViewById(R.id.new_et_feeder_no_9_33kv);
        TextView new_et_feeder_no_10_33kv = payDialog.findViewById(R.id.new_et_feeder_no_10_33kv);

        Spinner sp_cable_size_1 = payDialog.findViewById(R.id.sp_cable_size_1);
        Spinner sp_cable_size_2 = payDialog.findViewById(R.id.sp_cable_size_2);
        Spinner sp_cable_size_3 = payDialog.findViewById(R.id.sp_cable_size_3);
        Spinner sp_cable_size_4 = payDialog.findViewById(R.id.sp_cable_size_4);
        Spinner sp_cable_size_5 = payDialog.findViewById(R.id.sp_cable_size_5);
        Spinner sp_cable_size_6 = payDialog.findViewById(R.id.sp_cable_size_6);
        Spinner sp_cable_size_7 = payDialog.findViewById(R.id.sp_cable_size_7);
        Spinner sp_cable_size_8 = payDialog.findViewById(R.id.sp_cable_size_8);
        Spinner sp_cable_size_9 = payDialog.findViewById(R.id.sp_cable_size_9);
        Spinner sp_cable_size_10 = payDialog.findViewById(R.id.sp_cable_size_10);

        Spinner sp_cable_size_1_11kv = payDialog.findViewById(R.id.sp_cable_size_1_11kv);
        Spinner sp_cable_size_2_11kv = payDialog.findViewById(R.id.sp_cable_size_2_11kv);
        Spinner sp_cable_size_3_11kv = payDialog.findViewById(R.id.sp_cable_size_3_11kv);
        Spinner sp_cable_size_4_11kv = payDialog.findViewById(R.id.sp_cable_size_4_11kv);
        Spinner sp_cable_size_5_11kv = payDialog.findViewById(R.id.sp_cable_size_5_11kv);
        Spinner sp_cable_size_6_11kv = payDialog.findViewById(R.id.sp_cable_size_6_11kv);
        Spinner sp_cable_size_7_11kv = payDialog.findViewById(R.id.sp_cable_size_7_11kv);
        Spinner sp_cable_size_8_11kv = payDialog.findViewById(R.id.sp_cable_size_8_11kv);
        Spinner sp_cable_size_9_11kv = payDialog.findViewById(R.id.sp_cable_size_9_11kv);
        Spinner sp_cable_size_10_11kv = payDialog.findViewById(R.id.sp_cable_size_10_11kv);


        new_sp_sub_station_name_33_feeder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_1, new_et_feeder_no_1_33kv, "1");

//
            }
        });

        new_sp_sub_station_name_33_feeder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_2, new_et_feeder_no_2_33kv, "2");
            }
        });


        new_sp_sub_station_name_33_feeder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_3, new_et_feeder_no_3_33kv, "3");

            }
        });

        new_sp_sub_station_name_33_feeder_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_4, new_et_feeder_no_4_33kv, "4");

            }
        });


        new_sp_sub_station_name_33_feeder_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_5, new_et_feeder_no_5_33kv, "5");

            }
        });

        new_sp_sub_station_name_33_feeder_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_6, new_et_feeder_no_6_33kv, "6");

            }
        });

        new_sp_sub_station_name_33_feeder_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_7, new_et_feeder_no_7_33kv, "7");

            }
        });

        new_sp_sub_station_name_33_feeder_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_8, new_et_feeder_no_8_33kv, "8");

            }
        });

        new_sp_sub_station_name_33_feeder_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_9, new_et_feeder_no_9_33kv, "9");

            }
        });

        new_sp_sub_station_name_33_feeder_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open33KVSSNearByDialog(new_sp_sub_station_name_33_feeder_10, new_et_feeder_no_10_33kv, "10");

            }
        });


        new_et_feeder_no_1_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_1.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 1", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_1_33kv, selectedSSCode33_1);
                }
            }
        });


        new_et_feeder_no_2_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_2.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 2", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_2_33kv, selectedSSCode33_2);
                }
            }
        });


        new_et_feeder_no_3_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_3.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 3", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_3_33kv, selectedSSCode33_3);
                }
            }
        });

        new_et_feeder_no_4_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_4.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 4", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_4_33kv, selectedSSCode33_4);
                }
            }
        });

        new_et_feeder_no_5_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_5.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 5", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_5_33kv, selectedSSCode33_5);
                }
            }
        });

        new_et_feeder_no_6_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_6.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 6", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_6_33kv, selectedSSCode33_6);
                }
            }
        });

        new_et_feeder_no_7_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_7.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 7", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_7_33kv, selectedSSCode33_7);
                }
            }
        });

        new_et_feeder_no_8_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_8.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 8", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_8_33kv, selectedSSCode33_8);
                }
            }
        });

        new_et_feeder_no_9_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_9.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 9", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_9_33kv, selectedSSCode33_9);
                }
            }
        });

        new_et_feeder_no_10_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_33_feeder_10.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select EHT SS Name For Feeder 10", Toast.LENGTH_SHORT).show();
                } else {
                    open33KVFeederMasterDialogNew(new_et_feeder_no_10_33kv, selectedSSCode33_10);
                }
            }
        });


        // 11KV SS SELECTION

        new_sp_sub_station_name_11_feeder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_1, new_et_feeder_no_1_11kv, "1");

            }
        });

        new_sp_sub_station_name_11_feeder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_2, new_et_feeder_no_2_11kv, "2");

            }
        });

        new_sp_sub_station_name_11_feeder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_3, new_et_feeder_no_3_11kv, "3");

            }
        });

        new_sp_sub_station_name_11_feeder_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_4, new_et_feeder_no_4_11kv, "4");

            }
        });

        new_sp_sub_station_name_11_feeder_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_5, new_et_feeder_no_5_11kv, "5");
//
            }
        });

        new_sp_sub_station_name_11_feeder_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_6, new_et_feeder_no_6_11kv, "6");
//
            }
        });

        new_sp_sub_station_name_11_feeder_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_7, new_et_feeder_no_7_11kv, "7");
//
            }
        });

        new_sp_sub_station_name_11_feeder_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_8, new_et_feeder_no_8_11kv, "8");
//
            }
        });

        new_sp_sub_station_name_11_feeder_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_9, new_et_feeder_no_9_11kv, "9");
//
            }
        });

        new_sp_sub_station_name_11_feeder_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open11KVSSNearByDialog(new_sp_sub_station_name_11_feeder_10, new_et_feeder_no_10_11kv, "10");
//
            }
        });

        new_et_feeder_no_1_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_1.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV Name For Feeder 1", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_1_11kv, selectedSSCode11_1);
                }
            }
        });

        new_et_feeder_no_2_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_2.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV Name For Feeder 2", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_2_11kv, selectedSSCode11_2);
                }
            }
        });

        new_et_feeder_no_3_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_3.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV Name For Feeder 3", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_3_11kv, selectedSSCode11_3);
                }
            }
        });

        new_et_feeder_no_4_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_4.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV SS Name For Feeder 4", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_4_11kv, selectedSSCode11_4);
                }
            }
        });

        new_et_feeder_no_5_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_5.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV SS Name For Feeder 5", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_5_11kv, selectedSSCode11_5);
                }
            }
        });

        new_et_feeder_no_6_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_6.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV SS Name For Feeder 6", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_6_11kv, selectedSSCode11_6);
                }
            }
        });

        new_et_feeder_no_7_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_7.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV SS Name For Feeder 7", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_7_11kv, selectedSSCode11_7);
                }
            }
        });

        new_et_feeder_no_8_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_8.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV SS Name For Feeder 8", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_8_11kv, selectedSSCode11_8);
                }
            }
        });

        new_et_feeder_no_9_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_9.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV SS Name For Feeder 9", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_9_11kv, selectedSSCode11_9);
                }
            }
        });

        new_et_feeder_no_10_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_sp_sub_station_name_11_feeder_10.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select 33/11KV SS Name For Feeder 10", Toast.LENGTH_SHORT).show();
                } else {
                    open11KVFeederMasterDialogNew(new_et_feeder_no_10_11kv, selectedSSCode11_10);
                }
            }
        });


        EditText et_utility = payDialog.findViewById(R.id.et_utility);
        EditText et_ht_sc_rdss = payDialog.findViewById(R.id.et_ht_sc_rdss);
        EditText et_location_remarks = payDialog.findViewById(R.id.et_location_remarks);

        CheckBox ch_utilities = payDialog.findViewById(R.id.ch_utilities);


        Spinner sp_width = payDialog.findViewById(R.id.sp_width);
        Spinner sp_depth = payDialog.findViewById(R.id.sp_depth);
        Spinner sp_no_of_33 = payDialog.findViewById(R.id.sp_no_of_33);
        Spinner sp_no_of_11 = payDialog.findViewById(R.id.sp_no_of_11);
        Spinner sp_no_of_lt = payDialog.findViewById(R.id.sp_no_of_lt);

        Spinner sp_location_type = payDialog.findViewById(R.id.sp_location_type);
        Spinner sp_rmu_type_rdss = payDialog.findViewById(R.id.sp_rmu_type_rdss);
        Spinner sp_dtr_capacity_rdss = payDialog.findViewById(R.id.sp_dtr_capacity_rdss);
        Spinner sp_utility_type_rdss = payDialog.findViewById(R.id.sp_utility_type_rdss);

        LinearLayout mainLayout = payDialog.findViewById(R.id.mainLayout); // Or any other outer layout
        mainLayout.setPadding(0, 0, 0, 100);

        ch_utilities.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layout_utility_type_rdss.setVisibility(View.VISIBLE);
            } else {
                layout_utility_type_rdss.setVisibility(View.GONE);
            }
        });

        for (int i = 0; i < 10; i++) {
            int idx = i + 0;
            no_of_cables_33kv[i] = payDialog.findViewById(getResId("tv_cable_33kv_" + idx));
            no_of_cables_11kv[i] = payDialog.findViewById(getResId("tv_cable_11kv_" + idx));
            no_of_cables_lt[i] = payDialog.findViewById(getResId("tv_cable_lt_" + idx));

        }

        for (int i = 0; i < 10; i++) {
            int idx = i + 1;
            layout_dtr_details_lt[i] = payDialog.findViewById(getResId("layout_dtr_details_lt_" + idx));
            sp_dtr_capacity_lt[i] = payDialog.findViewById(getResId("sp_dtr_capacity_lt_" + idx));
            sp_dtr_phase_lt[i] = payDialog.findViewById(getResId("sp_dtr_phase_lt_" + idx));
            et_dtr_str_code[i] = payDialog.findViewById(getResId("et_dtr_str_code_" + idx));
            et_dtr_loc_name[i] = payDialog.findViewById(getResId("et_dtr_loc_name_" + idx));
            sp_cable_size_lt[i] = payDialog.findViewById(getResId("sp_cable_size_lt_" + idx));
            remarks_33kv[i] = payDialog.findViewById(getResId("remarks_33kv_" + idx));
            remarks_11kv[i] = payDialog.findViewById(getResId("remarks_11kv_" + idx));
            et_remarks_lt[i] = payDialog.findViewById(getResId("et_remarks_lt_" + idx));
        }

        for (int i = 0; i < 10; i++) {
            no_of_cables_33kv[i].setOnClickListener(v -> {
                // Reset all TextViews
                for (int j = 0; j < 10; j++) {
                    no_of_cables_33kv[j].setBackground(null);
                    no_of_cables_33kv[j].setTextColor(
                            Color.parseColor("#8A94A6")
                    );
                }
                v.setBackgroundResource(R.drawable.bg_selected_page);
                ((TextView) v).setTextColor(Color.WHITE);

                selectedCount_33kv.set(((TextView) v).getText().toString());


                String selectedCount = selectedCount_33kv.get();
                switch (selectedCount) {
                    case "0":
                        new_layout_33kv_1.setVisibility(View.GONE);
                        new_layout_33kv_2.setVisibility(View.GONE);
                        new_layout_33kv_3.setVisibility(View.GONE);
                        new_layout_33kv_4.setVisibility(View.GONE);
                        new_layout_33kv_5.setVisibility(View.GONE);
                        new_layout_33kv_6.setVisibility(View.GONE);
                        new_layout_33kv_7.setVisibility(View.GONE);
                        new_layout_33kv_8.setVisibility(View.GONE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);


                        break;
                    case "1":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.GONE);
                        new_layout_33kv_3.setVisibility(View.GONE);
                        new_layout_33kv_4.setVisibility(View.GONE);
                        new_layout_33kv_5.setVisibility(View.GONE);
                        new_layout_33kv_6.setVisibility(View.GONE);
                        new_layout_33kv_7.setVisibility(View.GONE);
                        new_layout_33kv_8.setVisibility(View.GONE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);

                        break;
                    case "2":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.GONE);
                        new_layout_33kv_4.setVisibility(View.GONE);
                        new_layout_33kv_5.setVisibility(View.GONE);
                        new_layout_33kv_6.setVisibility(View.GONE);
                        new_layout_33kv_7.setVisibility(View.GONE);
                        new_layout_33kv_8.setVisibility(View.GONE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);
                        break;
                    case "3":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.VISIBLE);
                        new_layout_33kv_4.setVisibility(View.GONE);
                        new_layout_33kv_5.setVisibility(View.GONE);
                        new_layout_33kv_6.setVisibility(View.GONE);
                        new_layout_33kv_7.setVisibility(View.GONE);
                        new_layout_33kv_8.setVisibility(View.GONE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);
                        break;
                    case "4":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.VISIBLE);
                        new_layout_33kv_4.setVisibility(View.VISIBLE);
                        new_layout_33kv_5.setVisibility(View.GONE);
                        new_layout_33kv_6.setVisibility(View.GONE);
                        new_layout_33kv_7.setVisibility(View.GONE);
                        new_layout_33kv_8.setVisibility(View.GONE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);
                        break;
                    case "5":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.VISIBLE);
                        new_layout_33kv_4.setVisibility(View.VISIBLE);
                        new_layout_33kv_5.setVisibility(View.VISIBLE);
                        new_layout_33kv_6.setVisibility(View.GONE);
                        new_layout_33kv_7.setVisibility(View.GONE);
                        new_layout_33kv_8.setVisibility(View.GONE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);
                        break;
                    case "6":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.VISIBLE);
                        new_layout_33kv_4.setVisibility(View.VISIBLE);
                        new_layout_33kv_5.setVisibility(View.VISIBLE);
                        new_layout_33kv_6.setVisibility(View.VISIBLE);
                        new_layout_33kv_7.setVisibility(View.GONE);
                        new_layout_33kv_8.setVisibility(View.GONE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);
                        break;
                    case "7":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.VISIBLE);
                        new_layout_33kv_4.setVisibility(View.VISIBLE);
                        new_layout_33kv_5.setVisibility(View.VISIBLE);
                        new_layout_33kv_6.setVisibility(View.VISIBLE);
                        new_layout_33kv_7.setVisibility(View.VISIBLE);
                        new_layout_33kv_8.setVisibility(View.GONE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);
                        break;
                    case "8":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.VISIBLE);
                        new_layout_33kv_4.setVisibility(View.VISIBLE);
                        new_layout_33kv_5.setVisibility(View.VISIBLE);
                        new_layout_33kv_6.setVisibility(View.VISIBLE);
                        new_layout_33kv_7.setVisibility(View.VISIBLE);
                        new_layout_33kv_8.setVisibility(View.VISIBLE);
                        new_layout_33kv_9.setVisibility(View.GONE);
                        new_layout_33kv_10.setVisibility(View.GONE);
                        break;

                    case "9":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.VISIBLE);
                        new_layout_33kv_4.setVisibility(View.VISIBLE);
                        new_layout_33kv_5.setVisibility(View.VISIBLE);
                        new_layout_33kv_6.setVisibility(View.VISIBLE);
                        new_layout_33kv_7.setVisibility(View.VISIBLE);
                        new_layout_33kv_8.setVisibility(View.VISIBLE);
                        new_layout_33kv_9.setVisibility(View.VISIBLE);
                        new_layout_33kv_10.setVisibility(View.GONE);
                        break;

                    case "10":
                        new_layout_33kv_1.setVisibility(View.VISIBLE);
                        new_layout_33kv_2.setVisibility(View.VISIBLE);
                        new_layout_33kv_3.setVisibility(View.VISIBLE);
                        new_layout_33kv_4.setVisibility(View.VISIBLE);
                        new_layout_33kv_5.setVisibility(View.VISIBLE);
                        new_layout_33kv_6.setVisibility(View.VISIBLE);
                        new_layout_33kv_7.setVisibility(View.VISIBLE);
                        new_layout_33kv_8.setVisibility(View.VISIBLE);
                        new_layout_33kv_9.setVisibility(View.VISIBLE);
                        new_layout_33kv_10.setVisibility(View.VISIBLE);
                        break;

                }

            });

        }

        for (int i = 0; i < 10; i++) {
            no_of_cables_11kv[i].setOnClickListener(v -> {
                // Reset all TextViews
                for (int j = 0; j < 10; j++) {
                    no_of_cables_11kv[j].setBackground(null);
                    no_of_cables_11kv[j].setTextColor(
                            Color.parseColor("#8A94A6")
                    );
                }
                v.setBackgroundResource(R.drawable.bg_selected_page);
                ((TextView) v).setTextColor(Color.WHITE);

                selectedCount_11kv.set(((TextView) v).getText().toString());


                String selectedCount = selectedCount_11kv.get();
                switch (selectedCount) {
                    case "0":
                        new_layout_11kv_1.setVisibility(View.GONE);
                        new_layout_11kv_2.setVisibility(View.GONE);
                        new_layout_11kv_3.setVisibility(View.GONE);
                        new_layout_11kv_4.setVisibility(View.GONE);
                        new_layout_11kv_5.setVisibility(View.GONE);
                        new_layout_11kv_6.setVisibility(View.GONE);
                        new_layout_11kv_7.setVisibility(View.GONE);
                        new_layout_11kv_8.setVisibility(View.GONE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);

                        break;
                    case "1":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.GONE);
                        new_layout_11kv_3.setVisibility(View.GONE);
                        new_layout_11kv_4.setVisibility(View.GONE);
                        new_layout_11kv_5.setVisibility(View.GONE);
                        new_layout_11kv_6.setVisibility(View.GONE);
                        new_layout_11kv_7.setVisibility(View.GONE);
                        new_layout_11kv_8.setVisibility(View.GONE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);

                        break;
                    case "2":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.GONE);
                        new_layout_11kv_4.setVisibility(View.GONE);
                        new_layout_11kv_5.setVisibility(View.GONE);
                        new_layout_11kv_6.setVisibility(View.GONE);
                        new_layout_11kv_7.setVisibility(View.GONE);
                        new_layout_11kv_8.setVisibility(View.GONE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);
                        break;
                    case "3":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.VISIBLE);
                        new_layout_11kv_4.setVisibility(View.GONE);
                        new_layout_11kv_5.setVisibility(View.GONE);
                        new_layout_11kv_6.setVisibility(View.GONE);
                        new_layout_11kv_7.setVisibility(View.GONE);
                        new_layout_11kv_8.setVisibility(View.GONE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);
                        break;
                    case "4":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.VISIBLE);
                        new_layout_11kv_4.setVisibility(View.VISIBLE);
                        new_layout_11kv_5.setVisibility(View.GONE);
                        new_layout_11kv_6.setVisibility(View.GONE);
                        new_layout_11kv_7.setVisibility(View.GONE);
                        new_layout_11kv_8.setVisibility(View.GONE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);
                        break;
                    case "5":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.VISIBLE);
                        new_layout_11kv_4.setVisibility(View.VISIBLE);
                        new_layout_11kv_5.setVisibility(View.VISIBLE);
                        new_layout_11kv_6.setVisibility(View.GONE);
                        new_layout_11kv_7.setVisibility(View.GONE);
                        new_layout_11kv_8.setVisibility(View.GONE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);
                        break;
                    case "6":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.VISIBLE);
                        new_layout_11kv_4.setVisibility(View.VISIBLE);
                        new_layout_11kv_5.setVisibility(View.VISIBLE);
                        new_layout_11kv_6.setVisibility(View.VISIBLE);
                        new_layout_11kv_7.setVisibility(View.GONE);
                        new_layout_11kv_8.setVisibility(View.GONE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);
                        break;
                    case "7":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.VISIBLE);
                        new_layout_11kv_4.setVisibility(View.VISIBLE);
                        new_layout_11kv_5.setVisibility(View.VISIBLE);
                        new_layout_11kv_6.setVisibility(View.VISIBLE);
                        new_layout_11kv_7.setVisibility(View.VISIBLE);
                        new_layout_11kv_8.setVisibility(View.GONE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);
                        break;
                    case "8":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.VISIBLE);
                        new_layout_11kv_4.setVisibility(View.VISIBLE);
                        new_layout_11kv_5.setVisibility(View.VISIBLE);
                        new_layout_11kv_6.setVisibility(View.VISIBLE);
                        new_layout_11kv_7.setVisibility(View.VISIBLE);
                        new_layout_11kv_8.setVisibility(View.VISIBLE);
                        new_layout_11kv_9.setVisibility(View.GONE);
                        new_layout_11kv_10.setVisibility(View.GONE);
                        break;
                    case "9":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.VISIBLE);
                        new_layout_11kv_4.setVisibility(View.VISIBLE);
                        new_layout_11kv_5.setVisibility(View.VISIBLE);
                        new_layout_11kv_6.setVisibility(View.VISIBLE);
                        new_layout_11kv_7.setVisibility(View.VISIBLE);
                        new_layout_11kv_8.setVisibility(View.VISIBLE);
                        new_layout_11kv_9.setVisibility(View.VISIBLE);
                        new_layout_11kv_10.setVisibility(View.GONE);
                        break;
                    case "10":
                        new_layout_11kv_1.setVisibility(View.VISIBLE);
                        new_layout_11kv_2.setVisibility(View.VISIBLE);
                        new_layout_11kv_3.setVisibility(View.VISIBLE);
                        new_layout_11kv_4.setVisibility(View.VISIBLE);
                        new_layout_11kv_5.setVisibility(View.VISIBLE);
                        new_layout_11kv_6.setVisibility(View.VISIBLE);
                        new_layout_11kv_7.setVisibility(View.VISIBLE);
                        new_layout_11kv_8.setVisibility(View.VISIBLE);
                        new_layout_11kv_9.setVisibility(View.VISIBLE);
                        new_layout_11kv_10.setVisibility(View.VISIBLE);
                        break;

                }


            });

        }

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            no_of_cables_lt[i].setOnClickListener(v -> {
                // Reset all TextViews
                for (int j = 0; j < 10; j++) {
                    no_of_cables_lt[j].setBackground(null);
                    no_of_cables_lt[j].setTextColor(
                            Color.parseColor("#8A94A6")
                    );

                }
                v.setBackgroundResource(R.drawable.bg_selected_page);
                ((TextView) v).setTextColor(Color.WHITE);

                selectedCount_lt.set(((TextView) v).getText().toString());
                String selectedCount = selectedCount_lt.get();
                int count = Integer.parseInt(selectedCount);

                System.out.println("SELECTED_COUNT_OF_LT---->" + count);

                // Hide all layouts
                for (int j = 0; j < 10; j++) {
                    if (layout_dtr_details_lt[j] != null) {
                        layout_dtr_details_lt[j].setVisibility(View.GONE);
                    }
                }

                for (int j = 0; j < count; j++) {
                    layout_dtr_details_lt[j].setVisibility(View.VISIBLE);
                }
            });

        }

        sp_location_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = sp_location_type.getSelectedItem().toString();


                layout_rmu_type_rdss.setVisibility(View.GONE);
                layout_dtr_capacity_rdss.setVisibility(View.GONE);

                if (!selectedItem.equalsIgnoreCase("Select")) {
                    if (selectedItem.equalsIgnoreCase("RMU")) {
                        layout_rmu_type_rdss.setVisibility(View.VISIBLE);
                        et_location_remarks.setVisibility(View.VISIBLE);
                        layout_dtr_capacity_rdss.setVisibility(View.GONE);
                        layout_ht_sc_no_rdss.setVisibility(View.GONE);

                    } else if (selectedItem.equalsIgnoreCase("DTR")) {
                        layout_dtr_capacity_rdss.setVisibility(View.VISIBLE);
                        et_location_remarks.setVisibility(View.VISIBLE);
                        layout_rmu_type_rdss.setVisibility(View.GONE);
                        layout_ht_sc_no_rdss.setVisibility(View.GONE);
                    } else if (selectedItem.equalsIgnoreCase("HT Service")) {
                        layout_dtr_capacity_rdss.setVisibility(View.GONE);
                        layout_rmu_type_rdss.setVisibility(View.GONE);
                        layout_ht_sc_no_rdss.setVisibility(View.VISIBLE);
                        et_location_remarks.setVisibility(View.VISIBLE);
                    } else {
                        et_location_remarks.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: hide everything when nothing is selected
                layout_rmu_type_rdss.setVisibility(View.GONE);
                layout_dtr_capacity_rdss.setVisibility(View.GONE);
            }
        });


        dwc_pipe.setOnClickListener(v -> {
            dwc_pipe.setSelected(true);
            gi_pipe.setSelected(false);
            protectionType.set("DWC Pipe");
        });

        gi_pipe.setOnClickListener(v -> {
            dwc_pipe.setSelected(false);
            gi_pipe.setSelected(true);
            protectionType.set("Gi Pipe");
        });

        thirty_kv.setOnClickListener(v -> {

            thirty_kv.setSelected(true);
            eleven_kv.setSelected(false);
            lt_feeder.setSelected(false);

            layout_no_of_feeders_33kv_new.setVisibility(View.VISIBLE);
            layout_no_of_feeders_11kv_new.setVisibility(View.GONE);
            layout_no_of_feeders_lt_new.setVisibility(View.GONE);

            for (int i = 0; i < 10; i++) {
                layout_dtr_details_lt[i].setVisibility(View.GONE);
            }

            String selectedCount_33 = selectedCount_33kv.get();
            switch (selectedCount_33) {
                case "0":
                    new_layout_33kv_1.setVisibility(View.GONE);
                    new_layout_33kv_2.setVisibility(View.GONE);
                    new_layout_33kv_3.setVisibility(View.GONE);
                    new_layout_33kv_4.setVisibility(View.GONE);
                    new_layout_33kv_5.setVisibility(View.GONE);
                    new_layout_33kv_6.setVisibility(View.GONE);
                    new_layout_33kv_7.setVisibility(View.GONE);
                    new_layout_33kv_8.setVisibility(View.GONE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);


                    break;
                case "1":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.GONE);
                    new_layout_33kv_3.setVisibility(View.GONE);
                    new_layout_33kv_4.setVisibility(View.GONE);
                    new_layout_33kv_5.setVisibility(View.GONE);
                    new_layout_33kv_6.setVisibility(View.GONE);
                    new_layout_33kv_7.setVisibility(View.GONE);
                    new_layout_33kv_8.setVisibility(View.GONE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);

                    break;
                case "2":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.GONE);
                    new_layout_33kv_4.setVisibility(View.GONE);
                    new_layout_33kv_5.setVisibility(View.GONE);
                    new_layout_33kv_6.setVisibility(View.GONE);
                    new_layout_33kv_7.setVisibility(View.GONE);
                    new_layout_33kv_8.setVisibility(View.GONE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);
                    break;
                case "3":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.VISIBLE);
                    new_layout_33kv_4.setVisibility(View.GONE);
                    new_layout_33kv_5.setVisibility(View.GONE);
                    new_layout_33kv_6.setVisibility(View.GONE);
                    new_layout_33kv_7.setVisibility(View.GONE);
                    new_layout_33kv_8.setVisibility(View.GONE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);
                    break;
                case "4":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.VISIBLE);
                    new_layout_33kv_4.setVisibility(View.VISIBLE);
                    new_layout_33kv_5.setVisibility(View.GONE);
                    new_layout_33kv_6.setVisibility(View.GONE);
                    new_layout_33kv_7.setVisibility(View.GONE);
                    new_layout_33kv_8.setVisibility(View.GONE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);
                    break;
                case "5":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.VISIBLE);
                    new_layout_33kv_4.setVisibility(View.VISIBLE);
                    new_layout_33kv_5.setVisibility(View.VISIBLE);
                    new_layout_33kv_6.setVisibility(View.GONE);
                    new_layout_33kv_7.setVisibility(View.GONE);
                    new_layout_33kv_8.setVisibility(View.GONE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);
                    break;
                case "6":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.VISIBLE);
                    new_layout_33kv_4.setVisibility(View.VISIBLE);
                    new_layout_33kv_5.setVisibility(View.VISIBLE);
                    new_layout_33kv_6.setVisibility(View.VISIBLE);
                    new_layout_33kv_7.setVisibility(View.GONE);
                    new_layout_33kv_8.setVisibility(View.GONE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);
                    break;
                case "7":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.VISIBLE);
                    new_layout_33kv_4.setVisibility(View.VISIBLE);
                    new_layout_33kv_5.setVisibility(View.VISIBLE);
                    new_layout_33kv_6.setVisibility(View.VISIBLE);
                    new_layout_33kv_7.setVisibility(View.VISIBLE);
                    new_layout_33kv_8.setVisibility(View.GONE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);
                    break;
                case "8":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.VISIBLE);
                    new_layout_33kv_4.setVisibility(View.VISIBLE);
                    new_layout_33kv_5.setVisibility(View.VISIBLE);
                    new_layout_33kv_6.setVisibility(View.VISIBLE);
                    new_layout_33kv_7.setVisibility(View.VISIBLE);
                    new_layout_33kv_8.setVisibility(View.VISIBLE);
                    new_layout_33kv_9.setVisibility(View.GONE);
                    new_layout_33kv_10.setVisibility(View.GONE);
                    break;

                case "9":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.VISIBLE);
                    new_layout_33kv_4.setVisibility(View.VISIBLE);
                    new_layout_33kv_5.setVisibility(View.VISIBLE);
                    new_layout_33kv_6.setVisibility(View.VISIBLE);
                    new_layout_33kv_7.setVisibility(View.VISIBLE);
                    new_layout_33kv_8.setVisibility(View.VISIBLE);
                    new_layout_33kv_9.setVisibility(View.VISIBLE);
                    new_layout_33kv_10.setVisibility(View.GONE);
                    break;

                case "10":
                    new_layout_33kv_1.setVisibility(View.VISIBLE);
                    new_layout_33kv_2.setVisibility(View.VISIBLE);
                    new_layout_33kv_3.setVisibility(View.VISIBLE);
                    new_layout_33kv_4.setVisibility(View.VISIBLE);
                    new_layout_33kv_5.setVisibility(View.VISIBLE);
                    new_layout_33kv_6.setVisibility(View.VISIBLE);
                    new_layout_33kv_7.setVisibility(View.VISIBLE);
                    new_layout_33kv_8.setVisibility(View.VISIBLE);
                    new_layout_33kv_9.setVisibility(View.VISIBLE);
                    new_layout_33kv_10.setVisibility(View.VISIBLE);
                    break;

            }
            new_layout_11kv_1.setVisibility(View.GONE);
            new_layout_11kv_2.setVisibility(View.GONE);
            new_layout_11kv_3.setVisibility(View.GONE);
            new_layout_11kv_4.setVisibility(View.GONE);
            new_layout_11kv_5.setVisibility(View.GONE);
            new_layout_11kv_6.setVisibility(View.GONE);
            new_layout_11kv_7.setVisibility(View.GONE);
            new_layout_11kv_8.setVisibility(View.GONE);
            new_layout_11kv_9.setVisibility(View.GONE);
            new_layout_11kv_10.setVisibility(View.GONE);
        });
        eleven_kv.setOnClickListener(v -> {

            eleven_kv.setSelected(true);
            thirty_kv.setSelected(false);
            lt_feeder.setSelected(false);

            layout_no_of_feeders_33kv_new.setVisibility(View.GONE);
            layout_no_of_feeders_11kv_new.setVisibility(View.VISIBLE);
            layout_no_of_feeders_lt_new.setVisibility(View.GONE);


            for (int i = 0; i < 10; i++) {
                layout_dtr_details_lt[i].setVisibility(View.GONE);
            }
            String selectedCount_11 = sp_no_of_11.getSelectedItem().toString();
            switch (selectedCount_11) {

                case "0":
                    new_layout_11kv_1.setVisibility(View.GONE);
                    new_layout_11kv_2.setVisibility(View.GONE);
                    new_layout_11kv_3.setVisibility(View.GONE);
                    new_layout_11kv_4.setVisibility(View.GONE);
                    new_layout_11kv_5.setVisibility(View.GONE);
                    new_layout_11kv_6.setVisibility(View.GONE);
                    new_layout_11kv_7.setVisibility(View.GONE);
                    new_layout_11kv_8.setVisibility(View.GONE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);

                    break;
                case "1":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.GONE);
                    new_layout_11kv_3.setVisibility(View.GONE);
                    new_layout_11kv_4.setVisibility(View.GONE);
                    new_layout_11kv_5.setVisibility(View.GONE);
                    new_layout_11kv_6.setVisibility(View.GONE);
                    new_layout_11kv_7.setVisibility(View.GONE);
                    new_layout_11kv_8.setVisibility(View.GONE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);

                    break;
                case "2":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.GONE);
                    new_layout_11kv_4.setVisibility(View.GONE);
                    new_layout_11kv_5.setVisibility(View.GONE);
                    new_layout_11kv_6.setVisibility(View.GONE);
                    new_layout_11kv_7.setVisibility(View.GONE);
                    new_layout_11kv_8.setVisibility(View.GONE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);
                    break;
                case "3":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.VISIBLE);
                    new_layout_11kv_4.setVisibility(View.GONE);
                    new_layout_11kv_5.setVisibility(View.GONE);
                    new_layout_11kv_6.setVisibility(View.GONE);
                    new_layout_11kv_7.setVisibility(View.GONE);
                    new_layout_11kv_8.setVisibility(View.GONE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);
                    break;
                case "4":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.VISIBLE);
                    new_layout_11kv_4.setVisibility(View.VISIBLE);
                    new_layout_11kv_5.setVisibility(View.GONE);
                    new_layout_11kv_6.setVisibility(View.GONE);
                    new_layout_11kv_7.setVisibility(View.GONE);
                    new_layout_11kv_8.setVisibility(View.GONE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);
                    break;
                case "5":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.VISIBLE);
                    new_layout_11kv_4.setVisibility(View.VISIBLE);
                    new_layout_11kv_5.setVisibility(View.VISIBLE);
                    new_layout_11kv_6.setVisibility(View.GONE);
                    new_layout_11kv_7.setVisibility(View.GONE);
                    new_layout_11kv_8.setVisibility(View.GONE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);
                    break;
                case "6":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.VISIBLE);
                    new_layout_11kv_4.setVisibility(View.VISIBLE);
                    new_layout_11kv_5.setVisibility(View.VISIBLE);
                    new_layout_11kv_6.setVisibility(View.VISIBLE);
                    new_layout_11kv_7.setVisibility(View.GONE);
                    new_layout_11kv_8.setVisibility(View.GONE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);
                    break;
                case "7":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.VISIBLE);
                    new_layout_11kv_4.setVisibility(View.VISIBLE);
                    new_layout_11kv_5.setVisibility(View.VISIBLE);
                    new_layout_11kv_6.setVisibility(View.VISIBLE);
                    new_layout_11kv_7.setVisibility(View.VISIBLE);
                    new_layout_11kv_8.setVisibility(View.GONE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);
                    break;
                case "8":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.VISIBLE);
                    new_layout_11kv_4.setVisibility(View.VISIBLE);
                    new_layout_11kv_5.setVisibility(View.VISIBLE);
                    new_layout_11kv_6.setVisibility(View.VISIBLE);
                    new_layout_11kv_7.setVisibility(View.VISIBLE);
                    new_layout_11kv_8.setVisibility(View.VISIBLE);
                    new_layout_11kv_9.setVisibility(View.GONE);
                    new_layout_11kv_10.setVisibility(View.GONE);
                    break;
                case "9":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.VISIBLE);
                    new_layout_11kv_4.setVisibility(View.VISIBLE);
                    new_layout_11kv_5.setVisibility(View.VISIBLE);
                    new_layout_11kv_6.setVisibility(View.VISIBLE);
                    new_layout_11kv_7.setVisibility(View.VISIBLE);
                    new_layout_11kv_8.setVisibility(View.VISIBLE);
                    new_layout_11kv_9.setVisibility(View.VISIBLE);
                    new_layout_11kv_10.setVisibility(View.GONE);
                    break;
                case "10":
                    new_layout_11kv_1.setVisibility(View.VISIBLE);
                    new_layout_11kv_2.setVisibility(View.VISIBLE);
                    new_layout_11kv_3.setVisibility(View.VISIBLE);
                    new_layout_11kv_4.setVisibility(View.VISIBLE);
                    new_layout_11kv_5.setVisibility(View.VISIBLE);
                    new_layout_11kv_6.setVisibility(View.VISIBLE);
                    new_layout_11kv_7.setVisibility(View.VISIBLE);
                    new_layout_11kv_8.setVisibility(View.VISIBLE);
                    new_layout_11kv_9.setVisibility(View.VISIBLE);
                    new_layout_11kv_10.setVisibility(View.VISIBLE);
                    break;

            }

            new_layout_33kv_1.setVisibility(View.GONE);
            new_layout_33kv_2.setVisibility(View.GONE);
            new_layout_33kv_3.setVisibility(View.GONE);
            new_layout_33kv_4.setVisibility(View.GONE);
            new_layout_33kv_5.setVisibility(View.GONE);
            new_layout_33kv_6.setVisibility(View.GONE);
            new_layout_33kv_7.setVisibility(View.GONE);
            new_layout_33kv_8.setVisibility(View.GONE);
            new_layout_33kv_9.setVisibility(View.GONE);
            new_layout_33kv_10.setVisibility(View.GONE);


        });
        lt_feeder.setOnClickListener(v -> {

            lt_feeder.setSelected(true);
            eleven_kv.setSelected(false);
            thirty_kv.setSelected(false);

            layout_no_of_feeders_33kv_new.setVisibility(View.GONE);
            layout_no_of_feeders_11kv_new.setVisibility(View.GONE);
            layout_no_of_feeders_lt_new.setVisibility(View.VISIBLE);


            new_layout_33kv_1.setVisibility(View.GONE);
            new_layout_33kv_2.setVisibility(View.GONE);
            new_layout_33kv_3.setVisibility(View.GONE);
            new_layout_33kv_4.setVisibility(View.GONE);
            new_layout_33kv_5.setVisibility(View.GONE);
            new_layout_33kv_6.setVisibility(View.GONE);
            new_layout_33kv_7.setVisibility(View.GONE);
            new_layout_33kv_8.setVisibility(View.GONE);
            new_layout_33kv_9.setVisibility(View.GONE);
            new_layout_33kv_10.setVisibility(View.GONE);

            new_layout_11kv_1.setVisibility(View.GONE);
            new_layout_11kv_2.setVisibility(View.GONE);
            new_layout_11kv_3.setVisibility(View.GONE);
            new_layout_11kv_4.setVisibility(View.GONE);
            new_layout_11kv_5.setVisibility(View.GONE);
            new_layout_11kv_6.setVisibility(View.GONE);
            new_layout_11kv_7.setVisibility(View.GONE);
            new_layout_11kv_8.setVisibility(View.GONE);
            new_layout_11kv_9.setVisibility(View.GONE);
            new_layout_11kv_10.setVisibility(View.GONE);

        });


        layout_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UGPostingModel lastPosting = database.getLastPosting();
                System.out.println("LAST_POLE_DATA" + lastPosting.getUTILITY_TYPE());
                String WIDTH = lastPosting.getWIDTH();
                String DEPTH = lastPosting.getDEPTH();
                String CABLE_33KV = lastPosting.getCABLE_33KV();
                String CABLE_11KV = lastPosting.getCABLE_11KV();
                String CABLE_LT = lastPosting.getCABLE_LT();
                String UTILITY_TYPE = lastPosting.getUTILITY_TYPE();
                String UTILITY = lastPosting.getUTILITY();
                ArrayList<FeedersData> feedersList = lastPosting.getFeedersData();
                if (feedersList != null && !feedersList.isEmpty()) {
                    for (FeedersData feeder : feedersList) {
                        Log.d("FEEDER--->", "Name: " + feeder.getFeeder_name() + ", ID: " + feeder.getFeeder_id() + ", VOLTAGE_LEVEL: " + feeder.getVoltage_level());
                    }
                }

                repeatPole(sp_width, sp_depth, sp_no_of_33, sp_no_of_11, sp_no_of_lt, WIDTH, DEPTH, CABLE_33KV, CABLE_11KV, CABLE_LT, UTILITY_TYPE, UTILITY, ch_utilities, sp_utility_type_rdss, et_utility, feedersList, new_sp_sub_station_name_33_feeder_1, new_sp_sub_station_name_33_feeder_2, new_sp_sub_station_name_33_feeder_3,
                        new_sp_sub_station_name_33_feeder_4, new_sp_sub_station_name_33_feeder_5, new_sp_sub_station_name_33_feeder_6, new_sp_sub_station_name_33_feeder_7, new_sp_sub_station_name_33_feeder_8, new_sp_sub_station_name_33_feeder_9, new_sp_sub_station_name_33_feeder_10, new_et_feeder_no_1_33kv, new_et_feeder_no_2_33kv, new_et_feeder_no_3_33kv,
                        new_et_feeder_no_4_33kv, new_et_feeder_no_5_33kv, new_et_feeder_no_6_33kv, new_et_feeder_no_7_33kv, new_et_feeder_no_8_33kv, new_et_feeder_no_9_33kv, new_et_feeder_no_10_33kv, new_sp_sub_station_name_11_feeder_1, new_sp_sub_station_name_11_feeder_2, new_sp_sub_station_name_11_feeder_3,
                        new_sp_sub_station_name_11_feeder_4, new_sp_sub_station_name_11_feeder_5, new_sp_sub_station_name_11_feeder_6, new_sp_sub_station_name_11_feeder_7, new_sp_sub_station_name_11_feeder_8, new_sp_sub_station_name_11_feeder_9, new_sp_sub_station_name_11_feeder_10, new_et_feeder_no_1_11kv, new_et_feeder_no_2_11kv, new_et_feeder_no_3_11kv,
                        new_et_feeder_no_4_11kv, new_et_feeder_no_5_11kv, new_et_feeder_no_6_11kv, new_et_feeder_no_7_11kv, new_et_feeder_no_8_11kv, new_et_feeder_no_9_11kv, new_et_feeder_no_10_11kv, thirty_kv, eleven_kv, lt_feeder, layout_no_of_feeders_33kv_new, layout_no_of_feeders_11kv_new, layout_no_of_feeders_lt_new);


            }
        });

        Button submitButton = payDialog.findViewById(R.id.submitButton);

        if (currentList.isEmpty()) {
            pole_no.setText("1");
            from_pole.setText("0");
        } else {
            pole_no.setText(String.valueOf(currentList.size() + 1));
            from_pole.setText(String.valueOf(currentList.size()));
        }
        //NEW
        layout_from_pole.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(this, from_pole);

            Set<String> uniqueLocations = new LinkedHashSet<>();

            uniqueLocations.add("0");

            double userLat = latitude;
            double userLon = longitude;

            if (VOLTAGE_LEVEL.equalsIgnoreCase("33KV")) {

                for (UGPostingModel pole : filteredPolesList33) {

                    try {

                        double distance = getDistanceBetweenPoles33(
                                pole.getLON(),
                                pole.getLAT(),
                                userLat,
                                userLon
                        );

                        if (distance <= 100) {
                            uniqueLocations.add(pole.getLOCATION_NO());
                        }
                        System.out.println("GGRWUIYIOYFGO ::::33KV:::: == " + uniqueLocations);


                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            } else if (VOLTAGE_LEVEL.equalsIgnoreCase("11KV")) {
                for (UGPostingModel pole : filteredPolesList11) {

                    try {

                        double distance = getDistanceBetweenPoles33(
                                pole.getLON(),
                                pole.getLAT(),
                                userLat,
                                userLon
                        );

                        if (distance <= 100) {
                            uniqueLocations.add(pole.getLOCATION_NO());
                        }
                        System.out.println("GGRWUIYIOYFGO ::::11KV:::: == " + uniqueLocations);


                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }


            for (String location : uniqueLocations) {
                popupMenu.getMenu().add(location);
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                from_pole.setText(item.getTitle());
                return true;
            });

            popupMenu.show();
        });

        payDialog.show();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select Location Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("RMU")
                        && sp_rmu_type_rdss.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select RMU Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("RMU")
                        && et_location_remarks.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Enter Location remarks", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("DTR")
                        && et_location_remarks.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Enter Location remarks", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("DTR")
                        && sp_dtr_capacity_rdss.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select DTR Capacity", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("HT Service")
                        && et_ht_sc_rdss.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Enter  HT Service Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("HT Service")
                        && et_location_remarks.getText().toString().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Enter Location remarks", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (sp_width.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select Width", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_depth.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select Depth", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedCount_33kv.get().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select No of 33KV Cables", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedCount_11kv.get().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select No of 11KV Cables", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedCount_lt.get().isEmpty()) {
                    Toast.makeText(UGCableProposedMap.this, "Please Select No of LT Cables", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (ch_utilities.isChecked()) {
                    if (sp_utility_type_rdss.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(UGCableProposedMap.this, "Please Select Utility type", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (et_utility.getText().toString().equalsIgnoreCase("") || et_utility.getText().toString().isEmpty()) {
                        Toast.makeText(UGCableProposedMap.this, "Please Enter Utility", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                String selectedValue = selectedCount_33kv.get();

                int count;
                try {
                    count = Integer.parseInt(selectedValue);
                } catch (NumberFormatException e) {
                    count = 0;
                }

                for (int i = 1; i <= count; i++) {
                    TextView feederEditText = null;
                    String cableSize = "";

                    switch (i) {
                        case 1:
                            feederEditText = new_sp_sub_station_name_33_feeder_1;
                            cableSize = sp_cable_size_1.getSelectedItem().toString();
                            break;
                        case 2:
                            feederEditText = new_sp_sub_station_name_33_feeder_2;
                            cableSize = sp_cable_size_2.getSelectedItem().toString();
                            break;
                        case 3:
                            feederEditText = new_sp_sub_station_name_33_feeder_3;
                            cableSize = sp_cable_size_3.getSelectedItem().toString();
                            break;
                        case 4:
                            feederEditText = new_sp_sub_station_name_33_feeder_4;
                            cableSize = sp_cable_size_4.getSelectedItem().toString();
                            break;
                        case 5:
                            feederEditText = new_sp_sub_station_name_33_feeder_5;
                            cableSize = sp_cable_size_5.getSelectedItem().toString();
                            break;

                        case 6:
                            feederEditText = new_sp_sub_station_name_33_feeder_6;
                            cableSize = sp_cable_size_6.getSelectedItem().toString();
                            break;

                        case 7:
                            feederEditText = new_sp_sub_station_name_33_feeder_7;
                            cableSize = sp_cable_size_7.getSelectedItem().toString();
                            break;

                        case 8:
                            feederEditText = new_sp_sub_station_name_33_feeder_8;
                            cableSize = sp_cable_size_8.getSelectedItem().toString();
                            break;


                        case 9:
                            feederEditText = new_sp_sub_station_name_33_feeder_9;
                            cableSize = sp_cable_size_9.getSelectedItem().toString();
                            break;


                        case 10:
                            feederEditText = new_sp_sub_station_name_33_feeder_10;
                            cableSize = sp_cable_size_10.getSelectedItem().toString();
                            break;
                    }

                    if (feederEditText == null || feederEditText.getText().toString().trim().isEmpty()) {
                        Toast.makeText(
                                UGCableProposedMap.this,
                                "Please Select EHT SS Name " + i,
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    if (cableSize.equals("Select")) {

                        Toast.makeText(
                                UGCableProposedMap.this,
                                "Please Select Cable Size " + i,
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }
                }


                String selectedValue11 = selectedCount_11kv.get();
                int count11;

                try {
                    count = Integer.parseInt(selectedValue);
                } catch (NumberFormatException e) {
                    count = 0;
                }

                for (int i = 1; i <= count; i++) {
                    TextView feederEditText = null;


                    switch (i) {
                        case 1:
                            feederEditText = new_et_feeder_no_1_33kv;
                            break;
                        case 2:
                            feederEditText = new_et_feeder_no_2_33kv;
                            break;
                        case 3:
                            feederEditText = new_et_feeder_no_3_33kv;
                            break;
                        case 4:
                            feederEditText = new_et_feeder_no_4_33kv;
                            break;
                        case 5:
                            feederEditText = new_et_feeder_no_5_33kv;
                            break;
                        case 6:
                            feederEditText = new_et_feeder_no_6_33kv;
                            break;
                        case 7:
                            feederEditText = new_et_feeder_no_7_33kv;
                            break;
                        case 8:
                            feederEditText = new_et_feeder_no_8_33kv;
                            break;
                        case 9:
                            feederEditText = new_et_feeder_no_9_33kv;
                            break;
                        case 10:
                            feederEditText = new_et_feeder_no_10_33kv;
                            break;
                    }

                    if (feederEditText != null && feederEditText.getText().toString().trim().isEmpty()) {
                        Toast.makeText(
                                UGCableProposedMap.this,
                                "Please Select 33KV Feeder Name " + i,
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                }


                try {
                    count11 = Integer.parseInt(selectedValue11);
                } catch (NumberFormatException e) {
                    count11 = 0;
                }

                TextView[] ssNames = {
                        new_sp_sub_station_name_11_feeder_1,
                        new_sp_sub_station_name_11_feeder_2,
                        new_sp_sub_station_name_11_feeder_3,
                        new_sp_sub_station_name_11_feeder_4,
                        new_sp_sub_station_name_11_feeder_5,
                        new_sp_sub_station_name_11_feeder_6,
                        new_sp_sub_station_name_11_feeder_7,
                        new_sp_sub_station_name_11_feeder_8,
                        new_sp_sub_station_name_11_feeder_9,
                        new_sp_sub_station_name_11_feeder_10
                };

                TextView[] feederNames = {
                        new_et_feeder_no_1_11kv,
                        new_et_feeder_no_2_11kv,
                        new_et_feeder_no_3_11kv,
                        new_et_feeder_no_4_11kv,
                        new_et_feeder_no_5_11kv,
                        new_et_feeder_no_6_11kv,
                        new_et_feeder_no_7_11kv,
                        new_et_feeder_no_8_11kv,
                        new_et_feeder_no_9_11kv,
                        new_et_feeder_no_10_11kv
                };

                Spinner[] cableSpinners11 = {
                        sp_cable_size_1_11kv,
                        sp_cable_size_2_11kv,
                        sp_cable_size_3_11kv,
                        sp_cable_size_4_11kv,
                        sp_cable_size_5_11kv,
                        sp_cable_size_6_11kv,
                        sp_cable_size_7_11kv,
                        sp_cable_size_8_11kv,
                        sp_cable_size_9_11kv,
                        sp_cable_size_10_11kv
                };

                for (int i = 0; i < count11; i++) {
                    if (ssNames[i].getText().toString().trim().isEmpty()) {
                        Toast.makeText(
                                UGCableProposedMap.this,
                                "Please Select 33/11KV SS Name " + (i + 1),
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    if (feederNames[i].getText().toString().trim().isEmpty()) {
                        Toast.makeText(
                                UGCableProposedMap.this,
                                "Please Select 11KV Feeder Name " + (i + 1),
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    String cableSize = cableSpinners11[i]
                            .getSelectedItem()
                            .toString();

                    if (cableSize.equals("Select")) {

                        Toast.makeText(
                                UGCableProposedMap.this,
                                "Please Select Cable Size " + (i + 1),
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }


                }

                if (!selectedCount_lt.get().isEmpty()) {
                    int selectedLTCount = Integer.parseInt(selectedCount_lt.get());

                    for (int i = 0; i < selectedLTCount; i++) {

                        if (sp_cable_size_lt[i].getSelectedItem().toString().equalsIgnoreCase("Select")) {

                            Toast.makeText(
                                    UGCableProposedMap.this,
                                    "Please Select Cable Size " + (i + 1),
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (sp_dtr_phase_lt[i].getSelectedItem().toString().equalsIgnoreCase("Select")) {

                            Toast.makeText(
                                    UGCableProposedMap.this,
                                    "Please Select DTR Phase " + (i + 1),
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (sp_dtr_capacity_lt[i].getSelectedItem().toString().equalsIgnoreCase("Select")) {

                            Toast.makeText(
                                    UGCableProposedMap.this,
                                    "Please Select DTR Capacity " + (i + 1),
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (et_dtr_loc_name[i].getText().toString().isEmpty()) {
                            Toast.makeText(
                                    UGCableProposedMap.this,
                                    "Please Enter DTR Location Name " + (i + 1),
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }
                    }
                }


                List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(UGCableProposedMap.this).getUserMasterData();
                String role = loginDataResponses.get(0).getROLE();


                int x = (int) e.getX();
                int y = (int) e.getY();
                android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
                Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
                Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());
                double CURRENT_LAT = myWGS84Point.getX();
                double CURRENT_LON = myWGS84Point.getY();
                String user_id = preferences.getString(MobileConstants.USERNAME, "");

                int total_cables = Integer.parseInt(selectedCount_33kv.get()) +
                        Integer.parseInt(selectedCount_11kv.get()) +
                        Integer.parseInt(selectedCount_lt.get());

                System.out.println("------>>>>>>>");
                UGPostingModel model = new UGPostingModel();
                model.setLOCATION_ID(String.valueOf(createRandomUID()));
                model.setLOCATION_NO(pole_no.getText().toString()); // Update index as per the actual table schema
                model.setSOURCE_LOCATION(from_pole.getText().toString());        // Update index as per the actual table schema
                model.setSS_NAME(NAME);        // Update index as per the actual table schema
                model.setSS_ID(ID);        // Update index as per the actual table schema
                model.setSS_TYPE(AssetsHomeRdssUgCable.SELECTED_VOLTAGE);        // Update index as per the actual table schema
                model.setCREATED_BY(user_id);        // Update index as per the actual table schema
                model.setWIDTH(sp_width.getSelectedItem().toString());        // Update index as per the actual table schema
                model.setDEPTH(sp_depth.getSelectedItem().toString());        // Update index as per the actual table schema
                model.setUTILITY(et_utility.getText().toString());        // Update index as per the actual table schema
                model.setCOUNT_CABLES(String.valueOf(total_cables));        // Update index as per the actual table schema
                model.setCABLE_33KV(selectedCount_33kv.get());        // Update index as per the actual table schema
                model.setCABLE_11KV(selectedCount_11kv.get());        // Update index as per the actual table schema
                model.setCABLE_LT(selectedCount_lt.get());        // Update index as per the actual table schema
                model.setLAT(String.valueOf(CURRENT_LAT));        // Update index as per the actual table schema
                model.setLON(String.valueOf(CURRENT_LON));        // Update index as per the actual table schema
                model.setVOLTAGE(AssetsHomeRdssUgCable.SELECTED_VOLTAGE);
                model.setGPS_ACCURACY(gpSAccuracy);
//                if (VOLTAGE_LEVEL.equalsIgnoreCase("33KV")) {
//                    model.setCABLE_SIZE(sp_conductor_size_33.getSelectedItem().toString().trim());
//                } else {
//                    model.setCABLE_SIZE(sp_conductor_size_11.getSelectedItem().toString().trim());
//                }


                if (protectionType.get().equalsIgnoreCase("Gi Pipe")) {
                    model.setPROTECTION_PIPE("GI Pipe");
                }
                if (protectionType.get().equalsIgnoreCase("DWC Pipe")) {
                    model.setPROTECTION_PIPE("DWC Pipe");
                }


                System.out.println("GAGAGAGAGAGAG::::--> " + gpSAccuracy);

                model.setLOCATION_TYPE(sp_location_type.getSelectedItem().toString().trim());
                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("HT Service")) {
                    model.setHT_SCNO(et_ht_sc_rdss.getText().toString().trim());
                    model.setLOCATION_REMARKS(et_location_remarks.getText().toString().trim());
                }
                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("RMU")) {
                    model.setRMU_TYPE(sp_rmu_type_rdss.getSelectedItem().toString().trim());
                    model.setLOCATION_REMARKS(et_location_remarks.getText().toString().trim());
                }
                if (sp_location_type.getSelectedItem().toString().equalsIgnoreCase("DTR")) {
                    model.setDTR_CAPACITY(sp_dtr_capacity_rdss.getSelectedItem().toString().trim());
                    model.setLOCATION_REMARKS(et_location_remarks.getText().toString().trim());
                }
                if (layout_utility_type_rdss.getVisibility() == View.VISIBLE) {
                    model.setUTILITY_TYPE(sp_utility_type_rdss.getSelectedItem().toString().trim());
                }


                //NEW CODE FEEDERS START


                // FEEDERS DATA
                ArrayList<FeedersData> feedersList = new ArrayList<>();

                try {
                    int feederCount = Integer.parseInt(selectedCount_11kv.get());
                    System.out.println("Creating " + feederCount + " feeders");

                    for (int i = 1; i <= feederCount; i++) {
                        FeedersData feeder = new FeedersData();
                        feeder.setVoltage_level("11KV");
                        feeder.setFDR_INDEX(i);

                        String cableSize = switch (i) {
                            case 1 -> sp_cable_size_1_11kv.getSelectedItem().toString();
                            case 2 -> sp_cable_size_2_11kv.getSelectedItem().toString();
                            case 3 -> sp_cable_size_3_11kv.getSelectedItem().toString();
                            case 4 -> sp_cable_size_4_11kv.getSelectedItem().toString();
                            case 5 -> sp_cable_size_5_11kv.getSelectedItem().toString();
                            case 6 -> sp_cable_size_6_11kv.getSelectedItem().toString();
                            case 7 -> sp_cable_size_7_11kv.getSelectedItem().toString();
                            case 8 -> sp_cable_size_8_11kv.getSelectedItem().toString();
                            case 9 -> sp_cable_size_9_11kv.getSelectedItem().toString();
                            case 10 -> sp_cable_size_10_11kv.getSelectedItem().toString();
                            default -> "";
                        };

                        String ssname = switch (i) {
                            case 1 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_1.getText().toString(), 0);
                            case 2 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_2.getText().toString(), 0);
                            case 3 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_3.getText().toString(), 0);
                            case 4 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_4.getText().toString(), 0);
                            case 5 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_5.getText().toString(), 0);
                            case 6 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_6.getText().toString(), 0);
                            case 7 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_7.getText().toString(), 0);
                            case 8 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_8.getText().toString(), 0);
                            case 9 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_9.getText().toString(), 0);
                            case 10 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_10.getText().toString(), 0);
                            default -> "";
                        };
                        String feederName = switch (i) {
                            case 1 ->
                                    getSplitValue(new_et_feeder_no_1_11kv.getText().toString(), 0);
                            case 2 ->
                                    getSplitValue(new_et_feeder_no_2_11kv.getText().toString(), 0);
                            case 3 ->
                                    getSplitValue(new_et_feeder_no_3_11kv.getText().toString(), 0);
                            case 4 ->
                                    getSplitValue(new_et_feeder_no_4_11kv.getText().toString(), 0);
                            case 5 ->
                                    getSplitValue(new_et_feeder_no_5_11kv.getText().toString(), 0);
                            case 6 ->
                                    getSplitValue(new_et_feeder_no_6_11kv.getText().toString(), 0);
                            case 7 ->
                                    getSplitValue(new_et_feeder_no_7_11kv.getText().toString(), 0);
                            case 8 ->
                                    getSplitValue(new_et_feeder_no_8_11kv.getText().toString(), 0);
                            case 9 ->
                                    getSplitValue(new_et_feeder_no_9_11kv.getText().toString(), 0);
                            case 10 ->
                                    getSplitValue(new_et_feeder_no_10_11kv.getText().toString(), 0);
                            default -> "";
                        };

                        String ssId = switch (i) {
                            case 1 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_1.getText().toString(), 1);
                            case 2 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_2.getText().toString(), 1);
                            case 3 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_3.getText().toString(), 1);
                            case 4 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_4.getText().toString(), 1);
                            case 5 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_5.getText().toString(), 1);
                            case 6 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_6.getText().toString(), 1);
                            case 7 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_7.getText().toString(), 1);
                            case 8 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_8.getText().toString(), 1);
                            case 9 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_9.getText().toString(), 1);
                            case 10 ->
                                    getSplitValue(new_sp_sub_station_name_11_feeder_10.getText().toString(), 1);
                            default -> "";
                        };


                        String feeder_id = switch (i) {
                            case 1 ->
                                    getSplitValue(new_et_feeder_no_1_11kv.getText().toString(), 1);
                            case 2 ->
                                    getSplitValue(new_et_feeder_no_2_11kv.getText().toString(), 1);
                            case 3 ->
                                    getSplitValue(new_et_feeder_no_3_11kv.getText().toString(), 1);
                            case 4 ->
                                    getSplitValue(new_et_feeder_no_4_11kv.getText().toString(), 1);
                            case 5 ->
                                    getSplitValue(new_et_feeder_no_5_11kv.getText().toString(), 1);
                            case 6 ->
                                    getSplitValue(new_et_feeder_no_6_11kv.getText().toString(), 1);
                            case 7 ->
                                    getSplitValue(new_et_feeder_no_7_11kv.getText().toString(), 1);
                            case 8 ->
                                    getSplitValue(new_et_feeder_no_8_11kv.getText().toString(), 1);
                            case 9 ->
                                    getSplitValue(new_et_feeder_no_9_11kv.getText().toString(), 1);
                            case 10 ->
                                    getSplitValue(new_et_feeder_no_10_11kv.getText().toString(), 1);
                            default -> "";
                        };

                        feeder.setFeeder_name(feederName);
                        feeder.setFeeder_id(feeder_id);
                        feeder.setSS_NAME(ssname);
                        feeder.setSS_ID(ssId);
                        feeder.setCABLE_SIZE(cableSize);
                        feeder.setREMARKS(
                                remarks_11kv[i - 1].getText().toString().trim()
                        );
                        feedersList.add(feeder);

                        System.out.println("Added feeder " + i + ": " + feederName);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing feeder count: " + e.getMessage());
                }


                try {
                    int feederCount = Integer.parseInt(selectedCount_33kv.get());
                    System.out.println("Creating " + feederCount + " feeders");

                    for (int i = 1; i <= feederCount; i++) {
                        FeedersData feeder = new FeedersData();
                        feeder.setVoltage_level("33KV");
                        feeder.setFDR_INDEX(i);

                        String cableSize = switch (i) {
                            case 1 -> sp_cable_size_1.getSelectedItem().toString();
                            case 2 -> sp_cable_size_2.getSelectedItem().toString();
                            case 3 -> sp_cable_size_3.getSelectedItem().toString();
                            case 4 -> sp_cable_size_4.getSelectedItem().toString();
                            case 5 -> sp_cable_size_5.getSelectedItem().toString();
                            case 6 -> sp_cable_size_6.getSelectedItem().toString();
                            case 7 -> sp_cable_size_7.getSelectedItem().toString();
                            case 8 -> sp_cable_size_8.getSelectedItem().toString();
                            case 9 -> sp_cable_size_9.getSelectedItem().toString();
                            case 10 -> sp_cable_size_10.getSelectedItem().toString();
                            default -> "";
                        };


                        String feederName = switch (i) {
                            case 1 ->
                                    getSplitValue(new_et_feeder_no_1_33kv.getText().toString(), 0);
                            case 2 ->
                                    getSplitValue(new_et_feeder_no_2_33kv.getText().toString(), 0);
                            case 3 ->
                                    getSplitValue(new_et_feeder_no_3_33kv.getText().toString(), 0);
                            case 4 ->
                                    getSplitValue(new_et_feeder_no_4_33kv.getText().toString(), 0);
                            case 5 ->
                                    getSplitValue(new_et_feeder_no_5_33kv.getText().toString(), 0);
                            case 6 ->
                                    getSplitValue(new_et_feeder_no_6_33kv.getText().toString(), 0);
                            case 7 ->
                                    getSplitValue(new_et_feeder_no_7_33kv.getText().toString(), 0);
                            case 8 ->
                                    getSplitValue(new_et_feeder_no_8_33kv.getText().toString(), 0);
                            case 9 ->
                                    getSplitValue(new_et_feeder_no_9_33kv.getText().toString(), 0);
                            case 10 ->
                                    getSplitValue(new_et_feeder_no_10_33kv.getText().toString(), 0);
                            default -> "";
                        };

                        String ssName = switch (i) {
                            case 1 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_1.getText().toString(), 0);
                            case 2 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_2.getText().toString(), 0);
                            case 3 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_3.getText().toString(), 0);
                            case 4 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_4.getText().toString(), 0);
                            case 5 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_5.getText().toString(), 0);
                            case 6 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_6.getText().toString(), 0);
                            case 7 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_7.getText().toString(), 0);
                            case 8 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_8.getText().toString(), 0);
                            case 9 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_9.getText().toString(), 0);
                            case 10 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_10.getText().toString(), 0);
                            default -> "";
                        };


                        String feeder_id = switch (i) {
                            case 1 ->
                                    getSplitValue(new_et_feeder_no_1_33kv.getText().toString(), 1);
                            case 2 ->
                                    getSplitValue(new_et_feeder_no_2_33kv.getText().toString(), 1);
                            case 3 ->
                                    getSplitValue(new_et_feeder_no_3_33kv.getText().toString(), 1);
                            case 4 ->
                                    getSplitValue(new_et_feeder_no_4_33kv.getText().toString(), 1);
                            case 5 ->
                                    getSplitValue(new_et_feeder_no_5_33kv.getText().toString(), 1);
                            case 6 ->
                                    getSplitValue(new_et_feeder_no_6_33kv.getText().toString(), 1);
                            case 7 ->
                                    getSplitValue(new_et_feeder_no_7_33kv.getText().toString(), 1);
                            case 8 ->
                                    getSplitValue(new_et_feeder_no_8_33kv.getText().toString(), 1);
                            case 9 ->
                                    getSplitValue(new_et_feeder_no_9_33kv.getText().toString(), 1);
                            case 10 ->
                                    getSplitValue(new_et_feeder_no_10_33kv.getText().toString(), 1);
                            default -> "";
                        };

                        String ssID = switch (i) {
                            case 1 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_1.getText().toString(), 1);
                            case 2 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_2.getText().toString(), 1);
                            case 3 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_3.getText().toString(), 1);
                            case 4 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_4.getText().toString(), 1);
                            case 5 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_5.getText().toString(), 1);
                            case 6 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_6.getText().toString(), 1);
                            case 7 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_7.getText().toString(), 1);
                            case 8 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_8.getText().toString(), 1);
                            case 9 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_9.getText().toString(), 1);
                            case 10 ->
                                    getSplitValue(new_sp_sub_station_name_33_feeder_10.getText().toString(), 1);
                            default -> "";
                        };

                        feeder.setFeeder_name(feederName);
                        feeder.setFeeder_id(feeder_id);
                        feeder.setSS_ID(ssID);
                        feeder.setSS_NAME(ssName);
                        feeder.setCABLE_SIZE(cableSize);
                        feeder.setREMARKS(
                                remarks_33kv[i - 1].getText().toString().trim()
                        );
                        feedersList.add(feeder);

                        System.out.println("Added feeder " + i + ": " + feeder);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing feeder count: " + e.getMessage());
                }

                try {
                    int feederCount = Integer.parseInt(selectedCount_lt.get());
                    System.out.println("Creating " + feederCount + " LT feeders");

                    for (int i = 1; i <= feederCount; i++) {

                        int index = i - 1;

                        FeedersData feeder = new FeedersData();
                        feeder.setVoltage_level("LT");
                        feeder.setFDR_INDEX(i);

                        String cableSize = sp_cable_size_lt[index]
                                .getSelectedItem().toString();

                        feeder.setDTR_PHASE(
                                sp_dtr_phase_lt[index]
                                        .getSelectedItem().toString());

                        feeder.setDTR_CAPACITY(
                                sp_dtr_capacity_lt[index]
                                        .getSelectedItem().toString());

                        feeder.setDTR_CODE(
                                et_dtr_str_code[index]
                                        .getText().toString().trim());

                        feeder.setDTR_LOCATION(
                                et_dtr_loc_name[index]
                                        .getText().toString().trim());

                        feeder.setCABLE_SIZE(cableSize);

                        feeder.setREMARKS(
                                et_remarks_lt[index]
                                        .getText().toString().trim());

                        feedersList.add(feeder);

                        System.out.println("Added LT feeder " + i + ": " + feeder);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Error parsing LT feeder count: " + e.getMessage());
                }

                model.setFeedersData(feedersList);
                System.out.println("Total feeders prepared: " + feedersList.size());


                //NEW CODE FEEDERS END


                model.setIS_FRESH_SURVEY("Y");

                System.out.println("CURRENT_LATTITUDE" + String.valueOf(CURRENT_LAT));
                System.out.println("CURRENT_LONGITUDE" + String.valueOf(CURRENT_LON));

                if (from_pole.getText().toString().equalsIgnoreCase("0")) {
                    model.setFROM_LAT(String.valueOf(CURRENT_LAT));
                    model.setFROM_LON(String.valueOf(CURRENT_LON));
                } else {
                    ArrayList<UGPostingModel> allData = database.getUGPostingData();
                    for (int i = 0; i < allData.size(); i++) {
                        if (allData.get(i).getLOCATION_NO().equalsIgnoreCase(from_pole.getText().toString()) && allData.get(i).getSS_ID().equalsIgnoreCase(ID)) {
                            model.setFROM_LAT(allData.get(i).getLAT());
                            model.setFROM_LON(allData.get(i).getLON());
                        }
                    }
                }

                database.insertRdssPosting(model);
                Toast.makeText(UGCableProposedMap.this, "Location Added Successfully", Toast.LENGTH_SHORT).show();

                payDialog.dismiss();
            }
        });


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

    private int getResId(String name) {
        return getResources().getIdentifier(name, "id", getPackageName());
    }

    private String getSplitValue(String text, int index) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] parts = text.trim().split("~");

        return parts.length > index ? parts[index] : "";
    }

    private void open11KVFeederMasterDialogNew(TextView edt_text, String selectedSSCode) {
        ArrayList<String> filteredList = new ArrayList<>();
        HashMap<String, Integer> feederPoleMap = new HashMap<>();

        for (int i = 0; i < AssetsHomeRdssUgCable.feeders11KV.size(); i++) {
            if (selectedSSCode.equals(AssetsHomeRdssUgCable.feeders11KV.get(i).getSUB_STATION_ID())) {
                int polesCount = Integer.parseInt(AssetsHomeRdssUgCable.feeders11KV.get(i).getPOLES().trim());
                String feederInfo = AssetsHomeRdssUgCable.feeders11KV.get(i).getFEEDER_NAME() + "~" +
                        AssetsHomeRdssUgCable.feeders11KV.get(i).getFEEDER_ID();

                filteredList.add(feederInfo);
                feederPoleMap.put(feederInfo, polesCount);
            }
        }

        if (filteredList.isEmpty()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("No Feeders Found!")
                    .setMessage("No feeders are available for selection.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            SearchableSpinner searchableSpinner = new SearchableSpinner(UGCableProposedMap.this);
            searchableSpinner.setWindowTitle("SELECT FEEDER");

            searchableSpinner.setSpinnerListItems(filteredList);
            searchableSpinner.show();
            searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
                int polesCount = feederPoleMap.get(selectedString);

                edt_text.setText(selectedString);

//                if (polesCount == 0) {
//                    new androidx.appcompat.app.AlertDialog.Builder(UGCableProposedMap.this)
//                            .setTitle("Pay Attention!")
//                            .setMessage("Survey is not initiated for the feeder")
//                            .setPositiveButton("OK", (dialog, which) -> {
//                                edt_text.setText("");
//                                dialog.dismiss();
//                            })
//                            .show();
//                } else {
//                    edt_text.setText(selectedString);
//                }
            });
        }
    }

    private void open33KVFeederMasterDialogNew(TextView edt_text, String selectedSSCode) {
        ArrayList<String> filteredList = new ArrayList<>();
        HashMap<String, Integer> feederPoleMap = new HashMap<>();

        System.out.println("SS_CODE_SELECTED" + selectedSSCode);

        for (int i = 0; i < AssetsHomeRdssUgCable.feeders33KV.size(); i++) {
            if (selectedSSCode.equals(AssetsHomeRdssUgCable.feeders33KV.get(i).getEHT_SS_CODE())) {
                int polesCount = Integer.parseInt(AssetsHomeRdssUgCable.feeders33KV.get(i).getPOLES().trim());
                String feederInfo = AssetsHomeRdssUgCable.feeders33KV.get(i).getFEEDER_NAME_33KV() + "~" +
                        AssetsHomeRdssUgCable.feeders33KV.get(i).getFEEDER_CODE_33KV();

                filteredList.add(feederInfo);
                feederPoleMap.put(feederInfo, polesCount);
            }
        }

        if (filteredList.isEmpty()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("No Feeders Found!")
                    .setMessage("No feeders are available for selection.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            SearchableSpinner searchableSpinner = new SearchableSpinner(UGCableProposedMap.this);
            searchableSpinner.setWindowTitle("SELECT FEEDER");

            searchableSpinner.setSpinnerListItems(filteredList);
            searchableSpinner.show();
            searchableSpinner.setOnItemSelectListener((position, selectedString) -> {
                int polesCount = feederPoleMap.get(selectedString);
                edt_text.setText(selectedString);

//                if (polesCount == 0) {
//                    new androidx.appcompat.app.AlertDialog.Builder(UGCableProposedMap.this)
//                            .setTitle("Pay Attention!")
//                            .setMessage("Survey is not initiated for the feeder")
//                            .setPositiveButton("OK", (dialog, which) -> {
//                                edt_text.setText("");
//                                dialog.dismiss();
//                            })
//                            .show();
//                } else {
//                    edt_text.setText(selectedString);
//                }
            });
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            if (AssetsHomeRdssUgCable.SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                ArrayList<UGPostingModel> allPolesList33 = database.getUGPostingData();
                polesList33 = new ArrayList<>();
                polesList33.clear();
                for (int i = 0; i < allPolesList33.size(); i++) {
                    if (allPolesList33.get(i).getVOLTAGE().equalsIgnoreCase("33KV") && allPolesList33.get(i).getSS_ID().equalsIgnoreCase(ID)) {
                        polesList33.add(allPolesList33.get(i));
                    }
                }

                if (!polesList33.isEmpty()) {
                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                    filteredPolesList33.clear();
                    currentLocationGpsTracker = new GpsTraker(this);
                    for (int i = 0; i < polesList33.size(); i++) {
                        double distance = getDistanceBetweenPoles33(polesList33.get(i).getLON(), polesList33.get(i).getLAT(), currentLocationGpsTracker.getLatitude(), currentLocationGpsTracker.getLongitude());
                        if (distance <= 5000) {
                            filteredPolesList33.add(polesList33.get(i));
                        }
                    }
                    if (!isDialogShown) {
                        isDialogShown = true;
                        openAlertDialog("locations nearby 5km will be loaded on the map");
                    }


                    // Toast.makeText(this, "locations nearby 2km will be loaded on the map", Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < filteredPolesList33.size(); i++) {
                        polylinePoints.add(new Point(Double.parseDouble(filteredPolesList33.get(i).getLAT()), Double.parseDouble(filteredPolesList33.get(i).getLON())));
                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);
                        // SimpleMarkerSymbol symbol = null;
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12); //size 12, style of circle


                        ptWgs84 = new Point(Double.parseDouble(filteredPolesList33.get(i).getLAT()), Double.parseDouble(filteredPolesList33.get(i).getLON()), 414, SpatialReferences.getWgs84());
                        graphic = new Graphic(ptWgs84, symbol);
                        graphicsOverlay.getGraphics().add(graphic);

                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                        // create a text symbol for elevation mode
                        TextSymbol absoluteText = new TextSymbol(15, filteredPolesList33.get(i).getLOCATION_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE);
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    for (UGPostingModel surveyVo : filteredPolesList33) {
                        PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                        // polylinePoints.add(new Point(Double.parseDouble(surveyVo.getFROM_POLE_LATITUDE()), Double.parseDouble(surveyVo.getFROM_POLE_LONGITUDE())));

                        for (int i = 0; i < filteredPolesList33.size(); i++) {
                            if (surveyVo.getSOURCE_LOCATION().equalsIgnoreCase(filteredPolesList33.get(i).getLOCATION_NO())) {
                                polylinePoints.add(new Point(Double.parseDouble(filteredPolesList33.get(i).getLAT()), Double.parseDouble(filteredPolesList33.get(i).getLON())));
                            }
                        }


                        polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLAT()), Double.parseDouble(surveyVo.getLON())));
                        Polyline polyline = new Polyline(polylinePoints);

                        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f);
                        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                        mGraphicsOverlay.getGraphics().add(polylineGraphic);


                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());


                    }
                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                }


            }

            if (AssetsHomeRdssUgCable.SELECTED_VOLTAGE.equalsIgnoreCase("11KV")) {
                ArrayList<UGPostingModel> allPolesList11 = database.getUGPostingData();
                polesList11 = new ArrayList<>();
                polesList11.clear();
                for (int i = 0; i < allPolesList11.size(); i++) {
                    if (allPolesList11.get(i).getVOLTAGE().equalsIgnoreCase("11KV") && allPolesList11.get(i).getSS_ID().equalsIgnoreCase(ID)) {
                        polesList11.add(allPolesList11.get(i));
                    }
                }

                if (!polesList11.isEmpty()) {
                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    filteredPolesList11.clear();
                    currentLocationGpsTracker = new GpsTraker(this);
                    for (int i = 0; i < polesList11.size(); i++) {
                        double distance = getDistanceBetweenPoles11(polesList11.get(i).getLON(), polesList11.get(i).getLAT(), currentLocationGpsTracker.getLatitude(), currentLocationGpsTracker.getLongitude());
                        if (distance <= 5000) {
                            filteredPolesList11.add(polesList11.get(i));
                        }
                    }
                    if (!isDialogShown) {
                        isDialogShown = true;
                        openAlertDialog("locations nearby 5km will be loaded on the map");
                    }
                    //   Toast.makeText(this, "locations nearby 2km will be loaded on the map", Toast.LENGTH_SHORT).show();


                    for (int i = 0; i < filteredPolesList11.size(); i++) {
                        polylinePoints.add(new Point(Double.parseDouble(filteredPolesList11.get(i).getLAT()), Double.parseDouble(filteredPolesList11.get(i).getLON())));
                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);
                        // SimpleMarkerSymbol symbol = null;
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12); //size 12, style of circle


                        ptWgs84 = new Point(Double.parseDouble(filteredPolesList11.get(i).getLAT()), Double.parseDouble(filteredPolesList11.get(i).getLON()), 414, SpatialReferences.getWgs84());
                        graphic = new Graphic(ptWgs84, symbol);
                        graphicsOverlay.getGraphics().add(graphic);

                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                        // create a text symbol for elevation mode
                        TextSymbol absoluteText = new TextSymbol(15, filteredPolesList11.get(i).getLOCATION_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE);
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    for (UGPostingModel surveyVo : filteredPolesList11) {
                        PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                        // polylinePoints.add(new Point(Double.parseDouble(surveyVo.getFROM_POLE_LATITUDE()), Double.parseDouble(surveyVo.getFROM_POLE_LONGITUDE())));

                        for (int i = 0; i < filteredPolesList11.size(); i++) {
                            if (surveyVo.getSOURCE_LOCATION().equalsIgnoreCase(filteredPolesList11.get(i).getLOCATION_NO())) {
                                polylinePoints.add(new Point(Double.parseDouble(filteredPolesList11.get(i).getLAT()), Double.parseDouble(filteredPolesList11.get(i).getLON())));
                            }
                        }


                        polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLAT()), Double.parseDouble(surveyVo.getLON())));
                        Polyline polyline = new Polyline(polylinePoints);

                        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f);
                        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                        mGraphicsOverlay.getGraphics().add(polylineGraphic);


                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());


                    }

                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                }


            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void openAlertDialog(String message) {
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
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        // Set Data
        title.setText("Pay Attention!!");
        msg.setText(message);

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        okBtn.setText("Okay");

        // Click action
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }


//    private void openAlertDialog(String msg) {
//        new android.app.AlertDialog.Builder(this)
//                .setTitle("Pay attention")
//                .setIcon(R.drawable.app_logo)
//                .setCancelable(false)
//                .setMessage(msg)
//                .setPositiveButton("Okay", (dialog, which) -> {
//                    dialog.cancel();
//                })
//
//                .show();
//
//
//    }

    private void setCurrentLocation() {
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ignored) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled && !network_enabled) {
                new AlertDialog.Builder(UGCableProposedMap.this)
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

            } else {

                /* ** ADD ** */
                mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                    if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                        return;
                    }


                    String message = String.format("Error in DataSourceStatusChangedListener: %s",
                            dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(UGCableProposedMap.this, message, Toast.LENGTH_LONG).show();

                });
                // mLocationDisplay.stop();
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getDistanceBetweenPoles33(String polLat, String polLon, double fromLat, double fromLon) {
        System.out.println("CURRENT LOCATION LAT 33: " + fromLat);
        System.out.println("CURRENT LOCATION LON 33: " + fromLon);
        System.out.println("POLE LOCATION LAT 33: " + polLat);
        System.out.println("POLE LOCATION LON 33: " + polLon);
        Location currentStartPoint = new Location("locationM");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationM");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        System.out.println("POLE LOCATION DISTANCE 33: " + currentDistance);
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


    private long createRandomUID() {
        UUID uuid = UUID.randomUUID();
        // System.out.println("UUID: " + uuid);
        long randomNumber = uuid.getLeastSignificantBits();
        randomNumber = Math.abs(randomNumber);
        //System.out.println("Random Number: " + randomNumber);
        return randomNumber;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv.resume();
    }

    //NEW DTR CHANGES BY CHANDU
    public static double SELECTED_LATTITUDE_33KV = 0.0;
    public static double SELECTED_LONGITUDE_33KV = 0.0;
    public static double SELECTED_LATTITUDE_11KV = 0.0;
    public static double SELECTED_LONGITUDE_11KV = 0.0;

    private void open33KVSSNearByDialog(TextView edt_text, TextView tv_feeder_name, String number) {
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


        payDialog.show();

        back_to_home.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = payDialog.findViewById(selectedId);
            String selectedRadioButton = radioButton.getText().toString();
            payDialog.dismiss();
            if (selectedRadioButton.equals(" Nearby EHT SS")) {
                SearchableSpinner searchableSpinner = new SearchableSpinner(UGCableProposedMap.this);
                searchableSpinner.setWindowTitle("SELECT EHT SS");
                ArrayList<String> nearBy33KVList = new ArrayList<>();
                final HashMap<String, EHT_SS_MASTER_ALL> modelMap = new HashMap<>();

                for (EHT_SS_MASTER_ALL model : AssetsHomeRdssUgCable.ehtSsMasterAlls) {
                    String displayText = model.getEHT_SS_NAME() + "~" + model.getEHT_SS_CODE();
                    nearBy33KVList.add(displayText);
                    modelMap.put(displayText, model);
                }
                searchableSpinner.setSpinnerListItems(AssetsHomeRdssUgCable.nearBy33kvSSNames);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        edt_text.setText(selectedString);
                        switch (number) {
                            case "1":
                                selectedSSCode33_1 = selectedString.split("~")[1];
                                break;
                            case "2":
                                selectedSSCode33_2 = selectedString.split("~")[1];
                                break;
                            case "3":
                                selectedSSCode33_3 = selectedString.split("~")[1];
                                break;
                            case "4":
                                selectedSSCode33_4 = selectedString.split("~")[1];
                                break;
                            case "5":
                                selectedSSCode33_5 = selectedString.split("~")[1];
                                break;
                            case "6":
                                selectedSSCode33_6 = selectedString.split("~")[1];
                                break;
                            case "7":
                                selectedSSCode33_7 = selectedString.split("~")[1];
                                break;
                            case "8":
                                selectedSSCode33_8 = selectedString.split("~")[1];
                                break;
                            case "9":
                                selectedSSCode33_9 = selectedString.split("~")[1];
                                break;
                            case "10":
                                selectedSSCode33_10 = selectedString.split("~")[1];
                                break;
                        }
                        // selectedSSCode33_1 = selectedString.split("~")[1];
                        EHT_SS_MASTER_ALL selectedModel = modelMap.get(selectedString);
                        if (!selectedString.isEmpty()) {
                            tv_feeder_name.setVisibility(View.VISIBLE);
                        }
                        if (selectedModel != null) {
                            String latitude = selectedModel.getLATITUDE();
                            String longitude = selectedModel.getLONGITUDE();
                            SELECTED_LATTITUDE_33KV = Double.parseDouble(latitude);
                            SELECTED_LONGITUDE_33KV = Double.parseDouble(longitude);
                            tv_feeder_name.setText("");


                        }
                    }
                });

            } else {
                SearchableSpinner searchableSpinner = new SearchableSpinner(UGCableProposedMap.this);
                searchableSpinner.setWindowTitle("SELECT FEEDER");
                ArrayList<String> nearBy33KVList = new ArrayList<>();
                final HashMap<String, EHT_SS_MASTER_ALL> modelMap = new HashMap<>();

                for (EHT_SS_MASTER_ALL model : AssetsHomeRdssUgCable.ehtSsMasterAlls) {
                    String displayText = model.getEHT_SS_NAME() + "~" + model.getEHT_SS_CODE();
                    nearBy33KVList.add(displayText);
                    modelMap.put(displayText, model);


                }

                searchableSpinner.setSpinnerListItems(AssetsHomeRdssUgCable.total33kvSSNames);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        edt_text.setText(selectedString);
                        switch (number) {
                            case "1":
                                selectedSSCode33_1 = selectedString.split("~")[1];
                                break;
                            case "2":
                                selectedSSCode33_2 = selectedString.split("~")[1];
                                break;
                            case "3":
                                selectedSSCode33_3 = selectedString.split("~")[1];
                                break;
                            case "4":
                                selectedSSCode33_4 = selectedString.split("~")[1];
                                break;
                            case "5":
                                selectedSSCode33_5 = selectedString.split("~")[1];
                                break;
                            case "6":
                                selectedSSCode33_6 = selectedString.split("~")[1];
                                break;
                            case "7":
                                selectedSSCode33_7 = selectedString.split("~")[1];
                                break;
                            case "8":
                                selectedSSCode33_8 = selectedString.split("~")[1];
                                break;
                            case "9":
                                selectedSSCode33_9 = selectedString.split("~")[1];
                                break;
                            case "10":
                                selectedSSCode33_10 = selectedString.split("~")[1];
                                break;
                        }
                        // selectedSSCode33_1 = selectedString.split("~")[1];
                        EHT_SS_MASTER_ALL selectedModel = modelMap.get(selectedString);
                        if (selectedModel != null) {
                            String latitude = selectedModel.getLATITUDE();
                            String longitude = selectedModel.getLONGITUDE();

                            SELECTED_LATTITUDE_33KV = Double.parseDouble(latitude);
                            SELECTED_LONGITUDE_33KV = Double.parseDouble(longitude);
                            tv_feeder_name.setText("");

                        }

                        if (!selectedString.isEmpty()) {
                            tv_feeder_name.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });


    }

    private void open11KVSSNearByDialog(TextView edt_text, TextView tv_feeder_name, String number) {
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


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" Nearby 33/11KV SS")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(UGCableProposedMap.this);
                    searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                    ArrayList<String> nearBy11KVList = new ArrayList<>();
                    final HashMap<String, SS_MASTER_ALL> modelMap = new HashMap<>();

                    for (SS_MASTER_ALL model : AssetsHomeRdssUgCable.ssMasterAlls) {
                        String displayText = model.getSUB_STATION_NAME() + "~" + model.getSUB_STATION_ID();
                        nearBy11KVList.add(displayText);
                        modelMap.put(displayText, model);
                    }
                    searchableSpinner.setSpinnerListItems(AssetsHomeRdssUgCable.nearBy11kvSSNames);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            switch (number) {
                                case "1":
                                    selectedSSCode11_1 = selectedString.split("~")[1];
                                    break;
                                case "2":
                                    selectedSSCode11_2 = selectedString.split("~")[1];
                                    break;
                                case "3":
                                    selectedSSCode11_3 = selectedString.split("~")[1];
                                    break;
                                case "4":
                                    selectedSSCode11_4 = selectedString.split("~")[1];
                                    break;
                                case "5":
                                    selectedSSCode11_5 = selectedString.split("~")[1];
                                    break;
                                case "6":
                                    selectedSSCode11_6 = selectedString.split("~")[1];
                                    break;
                                case "7":
                                    selectedSSCode11_7 = selectedString.split("~")[1];
                                    break;
                                case "8":
                                    selectedSSCode11_8 = selectedString.split("~")[1];
                                    break;
                                case "9":
                                    selectedSSCode11_9 = selectedString.split("~")[1];
                                    break;
                                case "10":
                                    selectedSSCode11_10 = selectedString.split("~")[1];
                                    break;


                            }
                            SS_MASTER_ALL selectedModel = modelMap.get(selectedString);
                            if (selectedModel != null) {
                                String latitude = selectedModel.getLATITUDE();
                                String longitude = selectedModel.getLONGITUDE();
                                SELECTED_LATTITUDE_11KV = Double.parseDouble(latitude);
                                SELECTED_LONGITUDE_11KV = Double.parseDouble(longitude);
                            }
                            if (!selectedString.isEmpty()) {
                                tv_feeder_name.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(UGCableProposedMap.this);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> nearBy11KVList = new ArrayList<>();
                    final HashMap<String, SS_MASTER_ALL> modelMap = new HashMap<>();

                    for (SS_MASTER_ALL model : AssetsHomeRdssUgCable.ssMasterAlls) {
                        String displayText = model.getSUB_STATION_NAME() + "~" + model.getSUB_STATION_ID();
                        nearBy11KVList.add(displayText);
                        modelMap.put(displayText, model);
                    }
                    searchableSpinner.setSpinnerListItems(AssetsHomeRdssUgCable.total11kvSSNames);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            switch (number) {
                                case "1":
                                    selectedSSCode11_1 = selectedString.split("~")[1];
                                    break;
                                case "2":
                                    selectedSSCode11_2 = selectedString.split("~")[1];
                                    break;
                                case "3":
                                    selectedSSCode11_3 = selectedString.split("~")[1];
                                    break;
                                case "4":
                                    selectedSSCode11_4 = selectedString.split("~")[1];
                                    break;
                                case "5":
                                    selectedSSCode11_5 = selectedString.split("~")[1];
                                    break;
                                case "6":
                                    selectedSSCode11_6 = selectedString.split("~")[1];
                                    break;
                                case "7":
                                    selectedSSCode11_7 = selectedString.split("~")[1];
                                    break;
                                case "8":
                                    selectedSSCode11_8 = selectedString.split("~")[1];
                                    break;
                                case "9":
                                    selectedSSCode11_9 = selectedString.split("~")[1];
                                    break;
                                case "10":
                                    selectedSSCode11_10 = selectedString.split("~")[1];
                                    break;
                            }

                            //selectedSSCode = selectedString.split("~")[1];
                            SS_MASTER_ALL selectedModel = modelMap.get(selectedString);
                            if (selectedModel != null) {
                                String latitude = selectedModel.getLATITUDE();
                                String longitude = selectedModel.getLONGITUDE();
                                SELECTED_LATTITUDE_11KV = Double.parseDouble(latitude);
                                SELECTED_LONGITUDE_11KV = Double.parseDouble(longitude);
                            }
                            if (!selectedString.isEmpty()) {
                                tv_feeder_name.setVisibility(View.VISIBLE);
                            }


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
}
