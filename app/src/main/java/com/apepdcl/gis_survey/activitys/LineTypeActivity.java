package com.apepdcl.gis_survey.activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.lt_edit_screens.EditLT;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.apepdcl.gis_survey.vo.SurveyedResponseDataVo;
import com.apepdcl.gis_survey.vo.SurveyedResponseVo;
import com.apepdcl.gis_survey.vo.TappingPointResponseDataVo;
import com.apepdcl.gis_survey.vo.TappingPointResponseVo;
import com.apepdcl.gis_survey.vo.TappingPointktsReqVo;
import com.apepdcl.gis_survey.vo.TappingPointsDataReqVo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineTypeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner sp_linetype, sp_tappingpoint;
    private LinearLayout lyt_tapingpoint;
    private TextView btn_polenext, btn_taping, btn_edit_survey_lt;
    private String lineType, tappingPoint, dtrCode, ltCircuit, maxCount;
    private TextView tv_surveyedpole;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String sessionId, gLat, gLong, feederId;
    private ArcGISDatabase arcGISDatabase;
    private List<String> poleNumList;
    GpsTraker gpsTraker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_linetype);

            sp_linetype = findViewById(R.id.sp_linetype);
            sp_tappingpoint = findViewById(R.id.sp_tappingpoint);
            btn_polenext = findViewById(R.id.btn_polenext);
            btn_taping = findViewById(R.id.btn_taping);
            btn_edit_survey_lt = findViewById(R.id.btn_edit_survey_lt);
            lyt_tapingpoint = findViewById(R.id.lyt_tapingpoint);
            tv_surveyedpole = findViewById(R.id.tv_surveyedpole);
            lyt_tapingpoint.setVisibility(View.GONE);
            btn_taping.setVisibility(View.GONE);
            arcGISDatabase = new ArcGISDatabase(LineTypeActivity.this);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            sp_linetype.setOnItemSelectedListener(this);
            sp_tappingpoint.setOnItemSelectedListener(this);
            btn_polenext.setOnClickListener(this);
            btn_taping.setOnClickListener(this);
            btn_edit_survey_lt.setOnClickListener(this);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LineTypeActivity.this);
            sessionId = sharedPreferences.getString(MobileConstants.SESSION_ID, "");
            Intent intent = getIntent();
            dtrCode = intent.getStringExtra("DTRCODE");
            ltCircuit = intent.getStringExtra("CIRCUIT_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            gLat = intent.getStringExtra("GLAT");
            gLong = intent.getStringExtra("GLONG");
            gpsTraker = new GpsTraker(LineTypeActivity.this);
            gpsTraker.getLocation();

            if ("NO_DATA".equalsIgnoreCase(maxCount)) {
                tv_surveyedpole.setText("0");
                btn_edit_survey_lt.setVisibility(View.GONE);
            } else {
                tv_surveyedpole.setText(maxCount);
                btn_edit_survey_lt.setVisibility(View.VISIBLE);
            }
            poleNumList = new ArrayList<>();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("LT Survey ");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(LineTypeActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            // setSupportActionBar(toolbar);
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_linetype:
                lineType = sp_linetype.getSelectedItem().toString();
                if ("Through Line".equalsIgnoreCase(lineType)) {
                    btn_polenext.setVisibility(View.VISIBLE);
                    lyt_tapingpoint.setVisibility(View.GONE);
                    btn_taping.setVisibility(View.GONE);
                    sp_tappingpoint.setSelection(0);

                }/*else if("Spur Line".equalsIgnoreCase(lineType)){
                    lyt_tapingpoint.setVisibility(View.VISIBLE);
                    btn_taping.setVisibility(View.VISIBLE);
                    btn_polenext.setVisibility(View.GONE);
                }*/
                break;
            case R.id.sp_tappingpoint:
                tappingPoint = sp_tappingpoint.getSelectedItem().toString();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_polenext:
                if ("Select".equalsIgnoreCase(lineType)) {
                    Toast.makeText(LineTypeActivity.this, "Select LineType", Toast.LENGTH_SHORT).show();
                } else if ("Through Line".equalsIgnoreCase(lineType)) {
                    Intent intent = new Intent(LineTypeActivity.this, ArcGISMapActivity.class);
                    intent.putExtra("DTRCODE", dtrCode);
                    intent.putExtra("CIRCUIT_CODE", ltCircuit);
                    intent.putExtra("MAX_COUNT", maxCount);
                    intent.putExtra("GLAT", gLat);
                    intent.putExtra("GLONG", gLong);
                    startActivity(intent);
                    finish();
                } else if ("Spur Line".equalsIgnoreCase(lineType)) {
                    if (ConnectivityChecking.checkConnection(LineTypeActivity.this)) {
                        getTappingPoleNumbers();
                    } else {
                        Toast.makeText(LineTypeActivity.this, MobileConstants.INTERNET, Toast.LENGTH_SHORT).show();

                    }

                }

                break;

            case R.id.btn_taping:
                if ("Select".equalsIgnoreCase(tappingPoint)) {
                    Toast.makeText(LineTypeActivity.this, "Select Taping Point", Toast.LENGTH_SHORT).show();
                } else {
                    if (ConnectivityChecking.checkConnection(LineTypeActivity.this)) {
                        getTappingData();
                    } else {
                        Toast.makeText(LineTypeActivity.this, MobileConstants.INTERNET, Toast.LENGTH_SHORT).show();

                    }
                }

                break;

            case R.id.btn_edit_survey_lt:
                showPoleDialog();
                break;
        }
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
//                            ArrayList<SurveyDataVo> surveyVosList = arcGISDatabase.getSurveyDataByDistance(gpsTraker.getLatitude(), gpsTraker.getLongitude());
//                            System.out.println("LIST_SIZE --> " + surveyVosList.size());
//
//                            if (!surveyVosList.isEmpty()) {
//                                Intent intentEdit = new Intent(LineTypeActivity.this, EditLT.class);
//                                intentEdit.putExtra("data", surveyVosList);
//                                intentEdit.putExtra("CIRCUIT_CODE", ltCircuit);
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
//            ArrayList<SurveyDataVo> surveyVosList = arcGISDatabase.getSurveyDataByPoleNo(Integer.parseInt(etPoleNo.getText().toString().trim()));
//            System.out.println("LIST_SIZE-->" + surveyVosList.size());
//            if (!surveyVosList.isEmpty()) {
//                Intent intentEdit = new Intent(LineTypeActivity.this, EditLT.class);
//                intentEdit.putExtra("data", surveyVosList);
//                intentEdit.putExtra("CIRCUIT_CODE", ltCircuit);
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

    private void showPoleDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater()
                .inflate(R.layout.new_dialog_asset, null);

        bottomSheetDialog.setContentView(dialogView);

        EditText etPoleNo = dialogView.findViewById(R.id.etPoleNo);
        TextView btnLoadNearby = dialogView.findViewById(R.id.btnLoadNearby);
        TextView search = dialogView.findViewById(R.id.btnSearch);

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
                        1001);
                return;
            }

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {

                        if (location != null) {

                            ArrayList<SurveyDataVo> surveyVosList =
                                    arcGISDatabase.getSurveyDataByDistance(
                                            gpsTraker.getLatitude(),
                                            gpsTraker.getLongitude()
                                    );

                            if (!surveyVosList.isEmpty()) {

                                Intent intentEdit =
                                        new Intent(LineTypeActivity.this, EditLT.class);

                                intentEdit.putExtra("data", surveyVosList);
                                intentEdit.putExtra("CIRCUIT_CODE", ltCircuit);
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
                        Log.e("FakeGPS", "Error getting location: " + e.getMessage());
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

            ArrayList<SurveyDataVo> surveyVosList =
                    arcGISDatabase.getSurveyDataByPoleNo(Integer.parseInt(poleInput));

            if (!surveyVosList.isEmpty()) {

                Intent intentEdit =
                        new Intent(LineTypeActivity.this, EditLT.class);

                intentEdit.putExtra("data", surveyVosList);
                intentEdit.putExtra("CIRCUIT_CODE", ltCircuit);
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
                search.performClick();
            }
            return true;
        });
    }

    public void getTappingPoleNumbers() {
        progressDialog.show();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        TappingPointktsReqVo surveyedDataReqVo = new TappingPointktsReqVo();
        surveyedDataReqVo.setDTR_STRUCTURE(dtrCode);
        surveyedDataReqVo.setLT_CKTCODE(ltCircuit);
        surveyedDataReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(LineTypeActivity.this));
        surveyedDataReqVo.setVERSION(MobileConstants.APP_VERSION);
        surveyedDataReqVo.setSESSION_ID(sessionId);
        surveyedDataReqVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
        Gson gson = new Gson();
        String json = gson.toJson(surveyedDataReqVo);
        // Log.v("DATA===>>", "" + json);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<TappingPointResponseVo> responseVoCall = retrofitService.getTappingPointData(surveyedDataReqVo);
        responseVoCall.enqueue(new Callback<TappingPointResponseVo>() {
            @Override
            public void onResponse(Call<TappingPointResponseVo> call, Response<TappingPointResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    TappingPointResponseVo tappingPointResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(tappingPointResponseVo.getSTATUS())) {

                        if ("NO_DATA".equalsIgnoreCase(tappingPointResponseVo.getERR_MSG())) {
                            Toast.makeText(LineTypeActivity.this, "No Tapping Poles Found", Toast.LENGTH_SHORT).show();

                        } else {
                            List<TappingPointResponseDataVo> masterResponseDataVos = tappingPointResponseVo.getRESPONSE();
                            poleNumList.clear();
                            for (TappingPointResponseDataVo vo : masterResponseDataVos) {
                                poleNumList.add(vo.getPOLE_NO() + "-" + vo.getTAPPING_LOCNAME());
                            }
                            lyt_tapingpoint.setVisibility(View.VISIBLE);
                            btn_taping.setVisibility(View.VISIBLE);
                            btn_polenext.setVisibility(View.GONE);
                            poleNumList.add("Select");
                            Collections.reverse(poleNumList);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(LineTypeActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, poleNumList);
                            sp_tappingpoint.setAdapter(adapter);
                        }


                    } else {
                        Toast.makeText(LineTypeActivity.this, tappingPointResponseVo.getERR_MSG().toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(LineTypeActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TappingPointResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LineTypeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getTappingData() {

        progressDialog.show();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        TappingPointsDataReqVo tappingPointsDataReqVo = new TappingPointsDataReqVo();
        tappingPointsDataReqVo.setDTR_STRUCTURE(dtrCode);
        tappingPointsDataReqVo.setLT_CKTCODE(ltCircuit);
        tappingPointsDataReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(LineTypeActivity.this));
        tappingPointsDataReqVo.setVERSION(MobileConstants.APP_VERSION);
        tappingPointsDataReqVo.setSESSION_ID(sessionId);
        tappingPointsDataReqVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
        if (tappingPoint.contains("-")) {
            String[] poleNum = tappingPoint.split("-");
            String tapingPoleNum = poleNum[0]; // 004
            // String ssName = poleNum[1];
            tappingPointsDataReqVo.setPOLE_NO(Integer.parseInt(tapingPoleNum));
        } else {
            tappingPointsDataReqVo.setPOLE_NO(Integer.parseInt(tappingPoint));
        }

        Gson gson = new Gson();
        String json = gson.toJson(tappingPointsDataReqVo);
        //  Log.v("DATA===>>", "" + json);


        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<SurveyedResponseVo> responseVoCall = retrofitService.getTappingData(tappingPointsDataReqVo);
        responseVoCall.enqueue(new Callback<SurveyedResponseVo>() {
            @Override
            public void onResponse(Call<SurveyedResponseVo> call, Response<SurveyedResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    SurveyedResponseVo surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.getSTATUS())) {

                        if ("NO_DATA".equalsIgnoreCase(surveyedResponseVo.getERR_MSG())) {
                            Intent intent = new Intent(LineTypeActivity.this, ArcGISMapActivity.class);
                            intent.putExtra("DTRCODE", dtrCode);
                            intent.putExtra("CIRCUIT_CODE", ltCircuit);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("GLAT", gLat);
                            intent.putExtra("GLONG", gLong);
                            startActivity(intent);
                            finish();
                        } else {
                            arcGISDatabase.clearSurveyData();
                            List<SurveyedResponseDataVo> surveyedResponseDataVoList = surveyedResponseVo.getRESPONSE();
                            for (SurveyedResponseDataVo vo : surveyedResponseDataVoList) {
                                SurveyDataVo surveyVo = new SurveyDataVo();
                                surveyVo.setPOLE_NO(Integer.parseInt(vo.getPOLE_NO()));
                                surveyVo.setFEEDER_ID(vo.getFEEDER_ID());
                                surveyVo.setLAT(vo.getGLAT());
                                surveyVo.setLAN(vo.getGLONG());
                                surveyVo.setSURVEY_FLAG("Y");
                                arcGISDatabase.addSurveyData(surveyVo);
                            }
                            Intent intent = new Intent(LineTypeActivity.this, ArcGISMapActivity.class);
                            intent.putExtra("DTRCODE", dtrCode);
                            intent.putExtra("CIRCUIT_CODE", ltCircuit);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("GLAT", gLat);
                            intent.putExtra("GLONG", gLong);
                            startActivity(intent);
                            finish();
                        }


                    } else {
                        Toast.makeText(LineTypeActivity.this, surveyedResponseVo.getERR_MSG().toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(LineTypeActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SurveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LineTypeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LineTypeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
