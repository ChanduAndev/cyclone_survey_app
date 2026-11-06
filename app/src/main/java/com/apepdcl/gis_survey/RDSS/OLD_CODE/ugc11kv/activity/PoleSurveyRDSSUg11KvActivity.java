package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IntLocationsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyResponseDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.TruncDataRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoleSurveyRDSSUg11KvActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Button btn_polenext,btn_taping;
    private Spinner sp_loadmap,sp_tappingpoint;
    private String loadMap,ssName,feederName,feederCode,tappingpoint,maxCount,userName,sessionId;
    private TextView tv_surveyedpole;
    // private TextInputEditText edt_frompole,edt_topole;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private int surveyedCount=0;
    private ProgressDialog progressDialog;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private List<String> poleNumList;
    private LinearLayout lyt_tapingpoint,lyt_surveytype;
    private int flag=0;
    private SharedPreferences preferences;

    //New Button 11-01-2024
    Button btn_add_location;

    public static String result_pole,pole_no,other_pole;
    public static String FEEDER_ID;
    public static ArrayList<String> from_lat,from_lon;
    public static ArrayList<IntLocationsRDSSUg11Kv> poleLocations;
    public static String from_pole_lat,from_pole_lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polesurvey_rdss_ug_11kv);
        try {
            Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle("11KV UG Cable Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(PoleSurveyRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });

            from_lat = new ArrayList<>();
            from_lon = new ArrayList<>();
            poleLocations = new ArrayList<>();

            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            sp_loadmap = (Spinner) findViewById(R.id.sp_loadmap);
            sp_tappingpoint = (Spinner) findViewById(R.id.sp_tappingpoint);
            tv_surveyedpole = findViewById(R.id.tv_surveyedpole);
            lyt_tapingpoint = (LinearLayout) findViewById(R.id.lyt_tapingpoint);
            lyt_surveytype = (LinearLayout) findViewById(R.id.lyt_surveytype);
            btn_polenext = (Button) findViewById(R.id.btn_polenext);
            btn_taping = (Button) findViewById(R.id.btn_taping);
            btn_add_location = (Button) findViewById(R.id.btn_add_location);

            lyt_tapingpoint.setVisibility(View.GONE);
            btn_taping.setVisibility(View.GONE);
            btn_polenext.setOnClickListener(this);
            btn_taping.setOnClickListener(this);
            btn_add_location.setOnClickListener(this);
          //  getSupportActionBar().setTitle("33Kv LineSurvey");
            Intent intent = getIntent();
            if (intent!=null) {
                ssName = intent.getStringExtra("SSNAME");
                feederName = intent.getStringExtra("FEEDER_NAME");
                feederCode = intent.getStringExtra("FEEDER_CODE");
                maxCount = intent.getStringExtra("MAX_COUNT");
            }

            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(PoleSurveyRDSSUg11KvActivity.this);
            surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getSurveyData("");
            surveyedCount = arcGISDatabaseRDSSUg11Kv.getObjectId();
            poleNumList = new ArrayList<>();

            // tv_surveyedpole.setText(String.valueOf(surveyedCount));
            if (maxCount == null) {
                tv_surveyedpole.setText("0");
            } else {
                tv_surveyedpole.setText(maxCount);
            }

            sp_loadmap.setOnItemSelectedListener(this);
            sp_tappingpoint.setOnItemSelectedListener(this);
            preferences = PreferenceManager.getDefaultSharedPreferences(PoleSurveyRDSSUg11KvActivity.this);
            userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(PoleSurveyRDSSUg11KvActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }





    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.sp_loadmap:
                loadMap=sp_loadmap.getSelectedItem().toString();
                if("Through Line".equalsIgnoreCase(loadMap)){
                    lyt_tapingpoint.setVisibility(View.GONE);
                    btn_polenext.setVisibility(View.VISIBLE);
                    // lyt_surveytype.setVisibility(View.GONE);
                    btn_taping.setVisibility(View.GONE);
                    btn_add_location.setVisibility(View.GONE);
                    sp_tappingpoint.setSelection(0);
                }else if ("Spur Line".equalsIgnoreCase(loadMap)){
                    btn_add_location.setVisibility(View.VISIBLE);
                }else {
                    btn_add_location.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_tappingpoint:
                tappingpoint=sp_tappingpoint.getSelectedItem().toString();
                break;


            default:

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        btn_add_location.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(PoleSurveyActivity.this, FeederMasterActivity.class);
        startActivity(intent);
        finish();*/
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Are you sure you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void getTappingPointData(){
        try {
            progressDialog.show();
            LastSurveyRequestVoRDSSUg11Kv lastSurveyRequestVoRDSSUg11Kv = new LastSurveyRequestVoRDSSUg11Kv();
            //lastSurveyRequestVo.setFEEDER_ID(feederCode);
            lastSurveyRequestVoRDSSUg11Kv.setFEEDER_ID(feederCode);
            lastSurveyRequestVoRDSSUg11Kv.setUSER_ID(userName);
            lastSurveyRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            lastSurveyRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            lastSurveyRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(PoleSurveyRDSSUg11KvActivity.this));

            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<LastSurveyResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getTappingData(lastSurveyRequestVoRDSSUg11Kv);
            responseVoCall.enqueue(new Callback<LastSurveyResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<LastSurveyResponseVoRDSSUg11Kv> call, Response<LastSurveyResponseVoRDSSUg11Kv> response) {

                    if (response.body() != null) {
                        poleNumList.clear();
                        LastSurveyResponseVoRDSSUg11Kv response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            // arcGISDatabase.clearSurveyedData();
                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "No Tapping Poles Found", Toast.LENGTH_SHORT).show();

                            } else {
                                List<LastSurveyResponseDataVoRDSSUg11Kv> masterResponseDataVos = response_vo.getRESPONSE();
                                for (LastSurveyResponseDataVoRDSSUg11Kv vo : masterResponseDataVos) {
                                    poleNumList.add(vo.getPOLE_NO() + "-" + vo.getTAPPING_LOCNAME());
                                }
                                btn_polenext.setVisibility(View.GONE);
                                // lyt_surveytype.setVisibility(View.GONE);
                                btn_taping.setVisibility(View.VISIBLE);
                                lyt_tapingpoint.setVisibility(View.VISIBLE);
                                poleNumList.add("Select");
                                Collections.reverse(poleNumList);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PoleSurveyRDSSUg11KvActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item, poleNumList);
                                sp_tappingpoint.setAdapter(adapter);
                            }
                            progressDialog.dismiss();
                        } else {
                            // Toast.makeText(PoleSurveyActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), PoleSurveyActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PoleSurveyRDSSUg11KvActivity.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please trasmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    })

                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_polenext:
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(PoleSurveyRDSSUg11KvActivity.this)) {
                    getData();
                }else {
                    CommanUtilits.internetDialog(PoleSurveyRDSSUg11KvActivity.this);
                    //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_taping:
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(PoleSurveyRDSSUg11KvActivity.this)) {
                    if(!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                        getTapingPolesData();
                    }else {
                        Toast.makeText(PoleSurveyRDSSUg11KvActivity.this,"Select Tapping Point",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_add_location:
                System.out.println(" Calling add button");
                Intent intent=new Intent(PoleSurveyRDSSUg11KvActivity.this, PoleSurveyTappingLocationRDSSUg11KvActivity.class);
                intent.putExtra("SSNAME", ssName);
                intent.putExtra("FEEDER_NAME",feederName);
                intent.putExtra("FEEDER_CODE",feederCode);
                intent.putExtra("MAX_COUNT",maxCount);
                startActivity(intent);

                break;
        }
    }
    public void getData(){
        if("Select".equals(loadMap)){
            Toast.makeText(PoleSurveyRDSSUg11KvActivity.this,"Select Survey Type",Toast.LENGTH_LONG).show();
        }/*else if("Select".equals(loadMap)){
            Toast.makeText(PoleSurveyActivity.this,"Select Survey Type",Toast.LENGTH_LONG).show();
        }*/else {

            if ("Through Line".equals(loadMap)) {
                Intent intent = new Intent(PoleSurveyRDSSUg11KvActivity.this, ArcGISMapRDSSUg11KvActivity.class);
                intent.putExtra("SSNAME", ssName);
                intent.putExtra("FEEDER_NAME", feederName);
                intent.putExtra("FEEDER_CODE", feederCode);
                intent.putExtra("MAX_COUNT",tv_surveyedpole.getText().toString().trim());
                startActivity(intent);
                finish();
            } else if("Spur Line".equalsIgnoreCase(loadMap)){
                // if(!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                getTappingPointData();
               /* }else {
                  Toast.makeText(PoleSurveyActivity.this,"Select Tapping Point",Toast.LENGTH_SHORT).show();
                }*/
            }
            else if ("Intermediate/Inline Pole".equalsIgnoreCase(loadMap)){

                openIntermediatePopUp();
            }
        }
    }

    private void openIntermediatePopUp() {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.intermediate_pole_dialog);
        payDialog.getWindow().setLayout(width,height/2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView proceed_button = payDialog.findViewById(R.id.submit);
        EditText pole_1 = payDialog.findViewById(R.id.pole_1);
        EditText pole_2 = payDialog.findViewById(R.id.pole_2);

        payDialog.show();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pole_1.getText().toString().isEmpty()){
                    Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Please Enter Pole 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pole_2.getText().toString().isEmpty()){
                    Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Please Enter Pole 2", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (pole_1.getText().toString().equals(pole_2.getText().toString())){
                    Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Pole numbers should not be same", Toast.LENGTH_SHORT).show();
                    return;
                }



                callCheckPoleApi(pole_1.getText().toString().trim(),pole_2.getText().toString().trim());

                payDialog.dismiss();

            }
        });
    }

    private void callCheckPoleApi(String pole1, String pole2) {
        progressDialog.show();
        JsonObject postParam = new JsonObject();
        postParam.addProperty("POLE_NO",pole1) ;
        postParam.addProperty("FROM_POLE", pole2);
        postParam.addProperty("FEEDER_ID", feederCode);
        retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
        Call<JsonObject> responseVoCall = retrofitServiceRDSSUg11Kv.checkInlinePole(postParam);
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

                            IntLocationsRDSSUg11Kv locations = new IntLocationsRDSSUg11Kv();
                            locations.setPole(object.get("POLE_NO1").getAsString());
                            locations.setLat(object.get("GLAT1").getAsString());
                            locations.setLon(object.get("GLOG1").getAsString());
                            poleLocations.add(locations);
                            IntLocationsRDSSUg11Kv locations1 = new IntLocationsRDSSUg11Kv();
                            locations1.setPole(object.get("POLE_NO2").getAsString());
                            locations1.setLat(object.get("GLAT2").getAsString());
                            locations1.setLon(object.get("GLOG2").getAsString());
                            poleLocations.add(locations1);

                            from_pole_lat = object.get("FROM_LAT").getAsString();
                            from_pole_lon = object.get("FROM_LONG").getAsString();


                            if (result_pole.equals(pole1)){
                                pole_no = pole2;
                                other_pole = pole1;
                            }else {
                                pole_no = pole1;
                                other_pole = pole2;
                            }
                            Intent intent = new Intent(PoleSurveyRDSSUg11KvActivity.this, IntermediateMapRDSSUg11KvActivity.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME", feederName);
                            intent.putExtra("FEEDER_CODE", feederCode);
                            intent.putExtra("MAX_COUNT",tv_surveyedpole.getText().toString().trim());
                            intent.putExtra("POLE_1", pole1);
                            intent.putExtra("POLE_2", pole2);
                            intent.putExtra("SURVEY_TYPE", loadMap);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getTapingPolesData(){
        try {
            progressDialog.show();
            TruncDataRequestVoRDSSUg11Kv truncDataRequestVoRDSSUg11Kv = new TruncDataRequestVoRDSSUg11Kv();
            truncDataRequestVoRDSSUg11Kv.setFEEDER_ID(feederCode);
            truncDataRequestVoRDSSUg11Kv.setUSER_ID(userName);
            truncDataRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            truncDataRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            truncDataRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(PoleSurveyRDSSUg11KvActivity.this));

            //String ehtSS = tappingpoint;
            if (tappingpoint.contains("-")) {
                String[] poleNum = tappingpoint.split("-");
                String tapingPoleNum = poleNum[0]; // 004
                String ssName = poleNum[1];
                truncDataRequestVoRDSSUg11Kv.setTRUCK_POLE_NO(Integer.parseInt(tapingPoleNum));
            } else {
                truncDataRequestVoRDSSUg11Kv.setTRUCK_POLE_NO(Integer.parseInt(tappingpoint));
            }


            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<LastSurveyResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getTrncPoleData(truncDataRequestVoRDSSUg11Kv);
            responseVoCall.enqueue(new Callback<LastSurveyResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<LastSurveyResponseVoRDSSUg11Kv> call, Response<LastSurveyResponseVoRDSSUg11Kv> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        LastSurveyResponseVoRDSSUg11Kv response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Intent intent = new Intent(PoleSurveyRDSSUg11KvActivity.this, ArcGISMapRDSSUg11KvActivity.class);
                                intent.putExtra("SSNAME", ssName);
                                intent.putExtra("FEEDER_NAME", feederName);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                                intent.putExtra("TAPPING_POINT", tappingpoint);
                                intent.putExtra("SURVEY_TYPE", loadMap);
                                startActivity(intent);
                                startActivity(intent);
                                finish();
                            } else {
                                List<LastSurveyResponseDataVoRDSSUg11Kv> masterResponseDataVos = response_vo.getRESPONSE();
                                arcGISDatabaseRDSSUg11Kv.clearSurveyedData();
                                for (LastSurveyResponseDataVoRDSSUg11Kv vo : masterResponseDataVos) {
                                    SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv = new SurveyVoRDSSUg11Kv();
                                    surveyVoRDSSUg11Kv.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVoRDSSUg11Kv.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVoRDSSUg11Kv.setLat(vo.getGLAT());
                                    surveyVoRDSSUg11Kv.setLan(vo.getGLONG());
                                    surveyVoRDSSUg11Kv.setPastSurveyFlag("Y");
                                    arcGISDatabaseRDSSUg11Kv.addSurveyData(surveyVoRDSSUg11Kv);
                                }
                                Intent intent = new Intent(PoleSurveyRDSSUg11KvActivity.this, ArcGISMapRDSSUg11KvActivity.class);
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
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PoleSurveyRDSSUg11KvActivity.this);
                                    alertDialogBuilder.setTitle("A new Version Is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    })

                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PoleSurveyRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
