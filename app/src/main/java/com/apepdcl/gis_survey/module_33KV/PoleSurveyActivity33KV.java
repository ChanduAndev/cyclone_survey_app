package com.apepdcl.gis_survey.module_33KV;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.PoleSurveyActivity;
import com.apepdcl.gis_survey.module_11KV.edit_screens.Edit11KV;
import com.apepdcl.gis_survey.module_11KV.models.IntLocations;
import com.apepdcl.gis_survey.module_33KV.edit_screens.Edit33KV;
import com.apepdcl.gis_survey.module_33KV.vo.LastSurveyRequestVo;
import com.apepdcl.gis_survey.module_33KV.vo.LastSurveyResponseDataVo;
import com.apepdcl.gis_survey.module_33KV.vo.LastSurveyResponseVo;
import com.apepdcl.gis_survey.module_33KV.vo.SurveyVo;
import com.apepdcl.gis_survey.module_33KV.vo.TruncDataRequestVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoleSurveyActivity33KV extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Button btn_polenext, btn_taping, btn_pole_edit;
    private Spinner sp_loadmap, sp_tappingpoint;
    private String loadMap, ssName, feederName, feederCode, tappingpoint, maxCount, userName, sessionId;
    private TextView tv_surveyedpole;
    // private TextInputEditText edt_frompole,edt_topole;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyVo> surveyVoList;
    private int surveyedCount = 0;
    private ProgressDialog progressDialog;
    private RetrofitService retrofitService;
    private List<String> poleNumList;
    private LinearLayout lyt_tapingpoint, lyt_surveytype;
    private int flag = 0;
    private SharedPreferences preferences;
    TextView add_taping_point;
    GpsTraker gpsTraker;

    //INTERMEDIATE POLE
    public static String result_pole, pole_no, other_pole;
    public static String FEEDER_ID;
    public static ArrayList<String> from_lat, from_lon;
    public static ArrayList<IntLocations> poleLocations;
    public static String from_pole_lat, from_pole_lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pole_survey_activity33_kv);
        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("33kV Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            from_lat = new ArrayList<>();
            from_lon = new ArrayList<>();
            poleLocations = new ArrayList<>();
            sp_loadmap = (Spinner) findViewById(R.id.sp_loadmap);
            sp_tappingpoint = (Spinner) findViewById(R.id.sp_tappingpoint);
            tv_surveyedpole = findViewById(R.id.tv_surveyedpole);
            lyt_tapingpoint = (LinearLayout) findViewById(R.id.lyt_tapingpoint);
            lyt_surveytype = (LinearLayout) findViewById(R.id.lyt_surveytype);
            btn_polenext = (Button) findViewById(R.id.btn_polenext);
            btn_pole_edit = (Button) findViewById(R.id.btn_pole_edit);
            btn_taping = (Button) findViewById(R.id.btn_taping);
            add_taping_point = findViewById(R.id.add_taping_point);
            lyt_tapingpoint.setVisibility(View.GONE);
            btn_taping.setVisibility(View.GONE);
            btn_polenext.setOnClickListener(this);
            btn_taping.setOnClickListener(this);
            btn_pole_edit.setOnClickListener(this);
            Intent intent = getIntent();
            ssName = intent.getStringExtra("SSNAME");
            feederName = intent.getStringExtra("FEEDER_NAME");
            feederCode = intent.getStringExtra("FEEDER_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            gpsTraker = new GpsTraker(PoleSurveyActivity33KV.this);
            gpsTraker.getLocation();

            progressDialog = new ProgressDialog(PoleSurveyActivity33KV.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);

            arcGISDatabase = new ArcGISDatabase(PoleSurveyActivity33KV.this);
            surveyVoList = arcGISDatabase.getSurveyData("");
            surveyedCount = arcGISDatabase.getObjectId();
            poleNumList = new ArrayList<>();

            // tv_surveyedpole.setText(String.valueOf(surveyedCount));
            if (maxCount == null) {
                tv_surveyedpole.setText("0");
            } else {
                tv_surveyedpole.setText(maxCount);
            }
            if (!surveyVoList.isEmpty()) {
                btn_pole_edit.setVisibility(View.VISIBLE);
            }


            sp_loadmap.setOnItemSelectedListener(this);
            sp_tappingpoint.setOnItemSelectedListener(this);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstants.USERNAME, "");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(PoleSurveyActivity33KV.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }

            add_taping_point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PoleSurveyActivity33KV.this, AddTappingPoint33KV.class);
                    intent.putExtra("SSNAME", ssName);
                    intent.putExtra("FEEDER_NAME", feederName);
                    intent.putExtra("FEEDER_CODE", feederCode);
                    intent.putExtra("MAX_COUNT", maxCount);
                    startActivity(intent);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_polenext:
                if (ConnectivityChecking.checkConnection(PoleSurveyActivity33KV.this)) {
                    getData();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.btn_taping:
                if (ConnectivityChecking.checkConnection(PoleSurveyActivity33KV.this)) {
                    if (!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                        getTapingPolesData();
                    } else {
                        Toast.makeText(PoleSurveyActivity33KV.this, "Select Tapping Point", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_pole_edit:
                showPoleDialog();
                ;
                break;

        }
    }
    private void showPoleDialog() {

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(this);

        View dialogView = getLayoutInflater().inflate(R.layout.new_dialog_asset, null);
        bottomSheetDialog.setContentView(dialogView);

        EditText etPoleNo = dialogView.findViewById(R.id.etPoleNo);
        Button btnLoadNearby = dialogView.findViewById(R.id.btnLoadNearby);
        Button search = dialogView.findViewById(R.id.btnSearch);

        bottomSheetDialog.show();

        btnLoadNearby.setOnClickListener(v -> {

            FusedLocationProviderClient fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        1001
                );
                return;
            }

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {

                        if (location != null) {

                            ArrayList<SurveyVo> surveyVosList =
                                    arcGISDatabase.getSurveyDataByDistance(
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );

                            if (!surveyVosList.isEmpty()) {

                                Intent intentEdit =
                                        new Intent(PoleSurveyActivity33KV.this, Edit33KV.class);

                                intentEdit.putExtra("data", surveyVosList);
                                startActivity(intentEdit);

                            } else {
                                Toast.makeText(this,
                                        "No poles found",
                                        Toast.LENGTH_SHORT).show();
                            }

                            bottomSheetDialog.dismiss();

                        } else {
                            Toast.makeText(this,
                                    "Unable to get current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this,
                                "Failed to get location",
                                Toast.LENGTH_SHORT).show();
                    });
        });

        search.setOnClickListener(v -> {

            String poleInput = etPoleNo.getText().toString().trim();

            if (poleInput.isEmpty()) {
                etPoleNo.setError("Enter Pole Number");
                return;
            }

            ArrayList<SurveyVo> surveyVosList =
                    arcGISDatabase.getSurveyDataByPoleNo(Integer.parseInt(poleInput));

            if (!surveyVosList.isEmpty()) {

                Intent intentEdit =
                        new Intent(PoleSurveyActivity33KV.this, Edit33KV.class);

                intentEdit.putExtra("data", surveyVosList);
                startActivity(intentEdit);

            } else {
                Toast.makeText(this,
                        "Invalid Pole Number",
                        Toast.LENGTH_SHORT).show();
            }

            bottomSheetDialog.dismiss();
        });

        etPoleNo.setOnEditorActionListener((v, actionId, event) -> {

            String poleNo = etPoleNo.getText().toString().trim();

            if (!poleNo.isEmpty()) {
                search.performClick(); // trigger same search
            }

            return true;
        });
    }

//    private void showPoleDialog() {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//
//        // Inflate the custom layout
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.new_dialog_asset, null);
//        builder.setView(dialogView);
//
//        EditText etPoleNo = dialogView.findViewById(R.id.etPoleNo);
//        Button btnLoadNearby = dialogView.findViewById(R.id.btnLoadNearby);
//        Button search = dialogView.findViewById(R.id.btnSearch);
//
//        android.app.AlertDialog dialog = builder.create();
//        dialog.show();
//
//        btnLoadNearby.setOnClickListener(v -> {
//            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // Request permissions if not granted
//                ActivityCompat.requestPermissions(
//                        this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                        1001
//                );
//                return;
//            }
//
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(location -> {
//                        if (location != null) {
//                            double lat = location.getLatitude();
//                            double lon = location.getLongitude();
//
//                            //ArrayList<SurveyVo> surveyVosList = arcGISDatabase.getSurveyDataByDistance(lat, lon);
//                            ArrayList<SurveyVo> surveyVosList = arcGISDatabase.getSurveyDataByDistance(gpsTraker.getLatitude(), gpsTraker.getLongitude());
//                            System.out.println("LIST_SIZE_33KV--> " + surveyVosList.size());
//
//                            if (!surveyVosList.isEmpty()) {
//                                Intent intentEdit = new Intent(PoleSurveyActivity33KV.this, Edit33KV.class);
//                                intentEdit.putExtra("data", surveyVosList);
//                                startActivity(intentEdit);
//                            } else {
//                                Toast.makeText(this, "No poles found", Toast.LENGTH_SHORT).show();
//                            }
//
//                            dialog.dismiss();
//                        } else {
//                            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e("FakeGPS", "Error getting location: " + e.getMessage());
//                        Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show();
//                    });
//        });
//
//
//        search.setOnClickListener(v -> {
//            ArrayList<SurveyVo> surveyVosList = arcGISDatabase.getSurveyDataByPoleNo(Integer.parseInt(etPoleNo.getText().toString().trim()));
//            System.out.println("LIST_SIZE_33KV-->" + surveyVosList.size());
//            if (!surveyVosList.isEmpty()) {
//                Intent intentEdit = new Intent(PoleSurveyActivity33KV.this, Edit33KV.class);
//                intentEdit.putExtra("data", surveyVosList);
//
//                startActivity(intentEdit);
//            } else {
//                Toast.makeText(this, "Invalid Pole Number", Toast.LENGTH_SHORT).show();
//            }
//            dialog.dismiss();
//
//        });
//
//
//        etPoleNo.setOnEditorActionListener((v, actionId, event) -> {
//            String poleNo = etPoleNo.getText().toString().trim();
//            if (!poleNo.isEmpty()) {
//                Toast.makeText(this, "Searching pole: " + poleNo, Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//                // Your search code here
//            }
//            return true;
//        });
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.sp_loadmap:
                loadMap = sp_loadmap.getSelectedItem().toString();
                if ("Through Line".equalsIgnoreCase(loadMap)) {
                    lyt_tapingpoint.setVisibility(View.GONE);
                    btn_polenext.setVisibility(View.VISIBLE);
                    // lyt_surveytype.setVisibility(View.GONE);
                    btn_taping.setVisibility(View.GONE);
                    sp_tappingpoint.setSelection(0);
                }
                if ("Spur Line".equalsIgnoreCase(loadMap)) {
                    add_taping_point.setVisibility(View.VISIBLE);
                } else {
                    add_taping_point.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_tappingpoint:
                lyt_tapingpoint.setVisibility(View.VISIBLE);
                tappingpoint = sp_tappingpoint.getSelectedItem().toString();
                break;


            default:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void getTappingPointData() {
        try {
            LastSurveyRequestVo lastSurveyRequestVo = new LastSurveyRequestVo();
            //lastSurveyRequestVo.setFEEDER_ID(feederCode);
            lastSurveyRequestVo.setFEEDER_ID(feederCode);
            lastSurveyRequestVo.setUSER_ID(userName);
            lastSurveyRequestVo.setVERSION(MobileConstants.APP_VERSION_33_KV);
            lastSurveyRequestVo.setSESSION_ID(sessionId);
            lastSurveyRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PoleSurveyActivity33KV.this));

            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<LastSurveyResponseVo> responseVoCall = retrofitService.getTappingData(lastSurveyRequestVo);
            responseVoCall.enqueue(new Callback<LastSurveyResponseVo>() {
                @Override
                public void onResponse(Call<LastSurveyResponseVo> call, Response<LastSurveyResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        LastSurveyResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            // arcGISDatabase.clearSurveyedData();
                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Toast.makeText(PoleSurveyActivity33KV.this, "No Tapping Poles Found", Toast.LENGTH_SHORT).show();

                            } else {
                                List<LastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                                for (LastSurveyResponseDataVo vo : masterResponseDataVos) {
                                    poleNumList.add(vo.getPOLE_NO() + "-" + vo.getTAPPING_LOCNAME());
                                }
                                btn_polenext.setVisibility(View.GONE);
                                // lyt_surveytype.setVisibility(View.GONE);
                                btn_taping.setVisibility(View.VISIBLE);
                                lyt_tapingpoint.setVisibility(View.VISIBLE);
                                poleNumList.add("Select");
                                Collections.reverse(poleNumList);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PoleSurveyActivity33KV.this,
                                        android.R.layout.simple_spinner_dropdown_item, poleNumList);
                                sp_tappingpoint.setAdapter(adapter);
                            }
                        } else {
                            // Toast.makeText(PoleSurveyActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), PoleSurveyActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PoleSurveyActivity33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstants.APK_URL);
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            startActivity(intent);
                                                        }
                                                    })

                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(PoleSurveyActivity33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(PoleSurveyActivity33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PoleSurveyActivity33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData() {
        if ("Select".equals(loadMap)) {
            Toast.makeText(PoleSurveyActivity33KV.this, "Select Survey Type", Toast.LENGTH_LONG).show();
        }/*else if("Select".equals(loadMap)){
            Toast.makeText(PoleSurveyActivity.this,"Select Survey Type",Toast.LENGTH_LONG).show();
        }*/ else {

            if ("Through Line".equals(loadMap)) {
                Intent intent = new Intent(PoleSurveyActivity33KV.this, ArcGISMapActivity33KV.class);
                intent.putExtra("SSNAME", ssName);
                intent.putExtra("FEEDER_NAME", feederName);
                intent.putExtra("FEEDER_CODE", feederCode);
                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                intent.putExtra("SURVEY_TYPE", loadMap);
                startActivity(intent);
                finish();
            } else if ("Spur Line".equalsIgnoreCase(loadMap)) {
                // if(!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                getTappingPointData();
               /* }else {
                  Toast.makeText(PoleSurveyActivity.this,"Select Tapping Point",Toast.LENGTH_SHORT).show();
                }*/
            } else if ("Intermediate/Inline Pole".equalsIgnoreCase(loadMap)) {

                openIntermediatePopUp();
            }
        }
    }


    public void getTapingPolesData() {
        try {

            TruncDataRequestVo truncDataRequestVo = new TruncDataRequestVo();
            truncDataRequestVo.setFEEDER_ID(feederCode);
            truncDataRequestVo.setUSER_ID(userName);
            truncDataRequestVo.setVERSION(MobileConstants.APP_VERSION_33_KV);
            truncDataRequestVo.setSESSION_ID(sessionId);
            truncDataRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PoleSurveyActivity33KV.this));

            //String ehtSS = tappingpoint;
            if (tappingpoint.contains("-")) {
                String[] poleNum = tappingpoint.split("-");
                String tapingPoleNum = poleNum[0]; // 004
                String ssName = poleNum[1];
                truncDataRequestVo.setTRUCK_POLE_NO(Integer.parseInt(tapingPoleNum));
            } else {
                truncDataRequestVo.setTRUCK_POLE_NO(Integer.parseInt(tappingpoint));
            }


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<LastSurveyResponseVo> responseVoCall = retrofitService.getTrncPoleData(truncDataRequestVo);
            responseVoCall.enqueue(new Callback<LastSurveyResponseVo>() {
                @Override
                public void onResponse(Call<LastSurveyResponseVo> call, Response<LastSurveyResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        LastSurveyResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Intent intent = new Intent(PoleSurveyActivity33KV.this, ArcGISMapActivity33KV.class);
                                intent.putExtra("SSNAME", ssName);
                                intent.putExtra("FEEDER_NAME", feederName);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                                intent.putExtra("TAPPING_POINT", tappingpoint);
                                intent.putExtra("SURVEY_TYPE", loadMap);
                                startActivity(intent);
                                finish();
                            } else {
                                List<LastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                                arcGISDatabase.clearSurveyedData();
                                for (LastSurveyResponseDataVo vo : masterResponseDataVos) {
                                    SurveyVo surveyVo = new SurveyVo();
                                    surveyVo.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVo.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVo.setLat(vo.getGLAT());
                                    surveyVo.setLan(vo.getGLONG());
                                    surveyVo.setPastSurveyFlag("Y");
                                    arcGISDatabase.addSurveyData(surveyVo);
                                }
                                Intent intent = new Intent(PoleSurveyActivity33KV.this, ArcGISMapActivity33KV.class);
                                intent.putExtra("SSNAME", ssName);
                                intent.putExtra("FEEDER_NAME", feederName);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                                intent.putExtra("TAPPING_POINT", tappingpoint);
                                intent.putExtra("SURVEY_TYPE", loadMap);
                                startActivity(intent);
                                finish();

                            }
                        } else {
                            // Toast.makeText(PoleSurveyActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), PoleSurveyActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PoleSurveyActivity33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstants.APK_URL);
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            startActivity(intent);
                                                        }
                                                    })

                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(PoleSurveyActivity33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(PoleSurveyActivity33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PoleSurveyActivity33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openIntermediatePopUp() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.intermediate_pole_dialog, null);
        bottomSheetDialog.setContentView(view);

        TextView cancel_button = view.findViewById(R.id.dialog_cancel);
        TextView proceed_button = view.findViewById(R.id.submit);
        EditText pole_1 = view.findViewById(R.id.pole_1);
        EditText pole_2 = view.findViewById(R.id.pole_2);

        bottomSheetDialog.show();

        cancel_button.setOnClickListener(v -> bottomSheetDialog.dismiss());

        proceed_button.setOnClickListener(v -> {

            String pole1 = pole_1.getText().toString().trim();
            String pole2 = pole_2.getText().toString().trim();

            if (pole1.isEmpty()) {
                pole_1.setError("Please Enter Pole 1");
                return;
            }

            if (pole2.isEmpty()) {
                pole_2.setError("Please Enter Pole 2");
                return;
            }

            if (pole1.equals(pole2)) {
                Toast.makeText(PoleSurveyActivity33KV.this,
                        "Pole numbers should not be same",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            callCheckPoleApi(pole1, pole2);
            bottomSheetDialog.dismiss();
        });
    }
//    private void openIntermediatePopUp() {
//        Dialog payDialog = new Dialog(this);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        payDialog.setContentView(R.layout.intermediate_pole_dialog);
//        payDialog.getWindow().setLayout(width, height / 2);
//        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//
//        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
//        TextView proceed_button = payDialog.findViewById(R.id.submit);
//        EditText pole_1 = payDialog.findViewById(R.id.pole_1);
//        EditText pole_2 = payDialog.findViewById(R.id.pole_2);
//
//        payDialog.show();
//
//        cancel_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                payDialog.dismiss();
//            }
//        });
//
//        proceed_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (pole_1.getText().toString().isEmpty()) {
//                    Toast.makeText(PoleSurveyActivity33KV.this, "Please Enter Pole 1", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (pole_2.getText().toString().isEmpty()) {
//                    Toast.makeText(PoleSurveyActivity33KV.this, "Please Enter Pole 2", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//                if (pole_1.getText().toString().equals(pole_2.getText().toString())) {
//                    Toast.makeText(PoleSurveyActivity33KV.this, "Pole numbers should not be same", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//                callCheckPoleApi(pole_1.getText().toString().trim(), pole_2.getText().toString().trim());
//
//                payDialog.dismiss();
//
//            }
//        });
//    }

    private void callCheckPoleApi(String pole1, String pole2) {
        progressDialog.show();
        JsonObject postParam = new JsonObject();
        postParam.addProperty("POLE_NO", pole1);
        postParam.addProperty("FROM_POLE", pole2);
        postParam.addProperty("FEEDER_ID", feederCode);
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.checkInlinePole(postParam);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() != null) {
                        // System.out.println("CHECK RESPONSE : "+response.body());
                        JsonObject jsonObject = response.body();
                        if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                            progressDialog.dismiss();
                            JsonObject object = jsonObject.get("RESPONSE").getAsJsonObject();
                            result_pole = object.get("FROM_POLE").getAsString();
                            from_lat.add(object.get("GLAT1").getAsString());
                            from_lat.add(object.get("GLAT2").getAsString());
                            from_lon.add(object.get("GLOG1").getAsString());
                            from_lon.add(object.get("GLOG2").getAsString());

                            FEEDER_ID = feederCode;

                            IntLocations locations = new IntLocations();
                            locations.setPole(object.get("POLE_NO1").getAsString());
                            locations.setLat(object.get("GLAT1").getAsString());
                            locations.setLon(object.get("GLOG1").getAsString());
                            poleLocations.add(locations);
                            IntLocations locations1 = new IntLocations();
                            locations1.setPole(object.get("POLE_NO2").getAsString());
                            locations1.setLat(object.get("GLAT2").getAsString());
                            locations1.setLon(object.get("GLOG2").getAsString());
                            poleLocations.add(locations1);

                            from_pole_lat = object.get("FROM_LAT").getAsString();
                            from_pole_lon = object.get("FROM_LONG").getAsString();

                            arcGISDatabase.clearSurveyedData();


                            SurveyVo surveyVo1 = new SurveyVo();
                            surveyVo1.setPole_no(Integer.parseInt(object.get("POLE_NO2").getAsString()));
                            surveyVo1.setFeeder_id(FEEDER_ID);
                            surveyVo1.setLat(object.get("GLAT2").getAsString());
                            surveyVo1.setLan(object.get("GLOG2").getAsString());
                            surveyVo1.setPastSurveyFlag("Y");
                            arcGISDatabase.addSurveyData(surveyVo1);

                            SurveyVo surveyVo = new SurveyVo();
                            surveyVo.setPole_no(Integer.parseInt(object.get("POLE_NO1").getAsString()));
                            surveyVo.setFeeder_id(FEEDER_ID);
                            surveyVo.setLat(object.get("GLAT1").getAsString());
                            surveyVo.setLan(object.get("GLOG1").getAsString());
                            surveyVo.setPastSurveyFlag("Y");
                            arcGISDatabase.addSurveyData(surveyVo);


                            if (result_pole.equals(pole1)) {
                                pole_no = pole2;
                                other_pole = pole1;
                            } else {
                                pole_no = pole1;
                                other_pole = pole2;
                            }
                            Intent intent = new Intent(PoleSurveyActivity33KV.this, ArcGISMapActivity33KV.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME", feederName);
                            intent.putExtra("FEEDER_CODE", feederCode);
                            intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                            intent.putExtra("TAPPING_POINT", tappingpoint);
                            intent.putExtra("SURVEY_TYPE", loadMap);


                           /* intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME", feederName);
                            intent.putExtra("FEEDER_CODE", feederCode);
                            intent.putExtra("MAX_COUNT",tv_surveyedpole.getText().toString().trim());*/
                            intent.putExtra("POLE_1", pole1);
                            intent.putExtra("POLE_2", pole2);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(PoleSurveyActivity33KV.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PoleSurveyActivity33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PoleSurveyActivity33KV.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });

    }


}