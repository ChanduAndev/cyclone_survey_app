package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.IntermediateMapActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.model.PMIIntLocations;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitService;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITruncDataRequestVo;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMIPoleSurveyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Button btn_polenext,btn_taping;
    private Spinner sp_loadmap,sp_tappingpoint;
    private String loadMap,ssName,feederName,feederCode,tappingpoint,maxCount,userName,sessionId;
    private TextView tv_surveyedpole;
    // private TextInputEditText edt_frompole,edt_topole;
    private PMI11kvArcGISDatabase arcGISDatabase;
    private List<PMISurveyVo> surveyVoList;
    private int surveyedCount=0;
    private ProgressDialog progressDialog;
    private PMIRetrofitService retrofitService;
    private List<String> poleNumList;
    private LinearLayout lyt_tapingpoint,lyt_surveytype;
    private int flag=0;
    private SharedPreferences preferences;

    //New Button 26-12-2023
    Button btn_add_location;
    public static String result_pole,pole_no,other_pole;
    public static String FEEDER_ID;
    public static ArrayList<String> from_lat,from_lon;
    public static ArrayList<PMIIntLocations> poleLocations;
    public static String from_pole_lat,from_pole_lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmipole_survey);

        getSupportActionBar().hide();
        try {
            Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle("11Kv Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();

                }
            });
            from_lat = new ArrayList<>();
            from_lon = new ArrayList<>();
            poleLocations = new ArrayList<>();
            progressDialog = CommanUtilits.progressDialog(this);
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

            arcGISDatabase = new PMI11kvArcGISDatabase(PMIPoleSurveyActivity.this);
            surveyVoList = arcGISDatabase.getSurveyData("");
            surveyedCount = arcGISDatabase.getObjectId();
            poleNumList = new ArrayList<>();

            // tv_surveyedpole.setText(String.valueOf(surveyedCount));
            if (maxCount == null) {
                tv_surveyedpole.setText("0");
            } else {
                tv_surveyedpole.setText(maxCount);
            }

            sp_loadmap.setOnItemSelectedListener(this);
            sp_tappingpoint.setOnItemSelectedListener(this);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstants.USERNAME, "");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(PMIPoleSurveyActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_polenext:
                if (ConnectivityChecking.checkConnection(PMIPoleSurveyActivity.this)) {
                    loadMap = "Through Line";
                    getData();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_taping:
                if (ConnectivityChecking.checkConnection(PMIPoleSurveyActivity.this)) {
                    if(!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                        getTapingPolesData();
                    }else {
                        Toast.makeText(PMIPoleSurveyActivity.this,"Select Tapping Point",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_add_location:
                // System.out.println(" Calling add button");
                Intent intent=new Intent(PMIPoleSurveyActivity.this,PMIPoleSurveyTappingLocationActivity.class);
                intent.putExtra("SSNAME", ssName);
                intent.putExtra("FEEDER_NAME",feederName);
                intent.putExtra("FEEDER_CODE",feederCode);
                intent.putExtra("MAX_COUNT",maxCount);
                startActivity(intent);

                break;
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
    public void onNothingSelected(AdapterView<?> parent) {
        btn_add_location.setVisibility(View.GONE);
    }

    public void getData(){
        if("Select".equals(loadMap)){
            Toast.makeText(PMIPoleSurveyActivity.this,"Select Survey Type",Toast.LENGTH_LONG).show();
        }/*else if("Select".equals(loadMap)){
            Toast.makeText(PoleSurveyActivity.this,"Select Survey Type",Toast.LENGTH_LONG).show();
        }*/else {

            if ("Through Line".equals(loadMap)) {
                Intent intent = new Intent(PMIPoleSurveyActivity.this, PMIArcGISMapActivity11kv.class);
                intent.putExtra("SSNAME", ssName);
                intent.putExtra("FEEDER_NAME", feederName);
                intent.putExtra("FEEDER_CODE", feederCode);
                intent.putExtra("SURVEY_TYPE", loadMap);
                intent.putExtra("MAX_COUNT",tv_surveyedpole.getText().toString().trim());
                startActivity(intent);
                finish();
            } else if("Spur Line".equalsIgnoreCase(loadMap)){
                // if(!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
               // getTappingPointData();
               /* }else {
                  Toast.makeText(PoleSurveyActivity.this,"Select Tapping Point",Toast.LENGTH_SHORT).show();
                }*/
            }else if ("Intermediate/Inline Pole".equalsIgnoreCase(loadMap)){

                openIntermediatePopUp();
            }
        }
    }
    public void getTapingPolesData(){
        try {
            progressDialog.show();
            PMITruncDataRequestVo truncDataRequestVo = new PMITruncDataRequestVo();
            truncDataRequestVo.setFEEDER_ID(feederCode);
            truncDataRequestVo.setUSER_ID(userName);
            truncDataRequestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            truncDataRequestVo.setSESSION_ID(sessionId);
            truncDataRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIPoleSurveyActivity.this));

            //String ehtSS = tappingpoint;
            if (tappingpoint.contains("-")) {
                String[] poleNum = tappingpoint.split("-");
                String tapingPoleNum = poleNum[0]; // 004
                tappingpoint = poleNum[0]; // 004
                String ssName = poleNum[1];
                truncDataRequestVo.setTRUCK_POLE_NO(Integer.parseInt(tapingPoleNum));
            } else {
                truncDataRequestVo.setTRUCK_POLE_NO(Integer.parseInt(tappingpoint));
            }


            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
            Call<PMILastSurveyResponseVo> responseVoCall = retrofitService.getTrncPoleData(truncDataRequestVo);
            responseVoCall.enqueue(new Callback<PMILastSurveyResponseVo>() {
                @Override
                public void onResponse(Call<PMILastSurveyResponseVo> call, Response<PMILastSurveyResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        PMILastSurveyResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            System.out.println("TAPPING POINT : "+tappingpoint);
                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Toast.makeText(PMIPoleSurveyActivity.this, ""+response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PMIPoleSurveyActivity.this, PMIArcGISMapActivity11kv.class);
                                intent.putExtra("SSNAME", ssName);
                                intent.putExtra("FEEDER_NAME", feederName);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                                intent.putExtra("TAPPING_POINT", tappingpoint);
                                intent.putExtra("SURVEY_TYPE", loadMap);
                                startActivity(intent);
                                finish();
                            } else {
                                List<PMILastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                                Collections.reverse(masterResponseDataVos);
                                arcGISDatabase.clearSurveyedData();
                                for (PMILastSurveyResponseDataVo vo : masterResponseDataVos) {
                                    PMISurveyVo surveyVo = new PMISurveyVo();
                                    surveyVo.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVo.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVo.setLat(vo.getGLAT());
                                    surveyVo.setLan(vo.getGLONG());
                                    surveyVo.setPastSurveyFlag("Y");
                                    arcGISDatabase.addSurveyData(surveyVo);
                                }
                                Intent intent = new Intent(PMIPoleSurveyActivity.this, PMIArcGISMapActivity11kv.class);
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
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIPoleSurveyActivity.this);
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
                                Toast.makeText(PMIPoleSurveyActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(PMIPoleSurveyActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMILastSurveyResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PMIPoleSurveyActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void getTappingPointData(){
//        try {
//            progressDialog.show();
//            PMILastSurveyRequestVo lastSurveyRequestVo = new PMILastSurveyRequestVo();
//            //lastSurveyRequestVo.setFEEDER_ID(feederCode);
//            lastSurveyRequestVo.setFEEDER_ID(feederCode);
//            lastSurveyRequestVo.setUSER_ID(userName);
//            lastSurveyRequestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
//            lastSurveyRequestVo.setSESSION_ID(sessionId);
//            lastSurveyRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIPoleSurveyActivity.this));
//
//            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
//            Call<PMILastSurveyResponseVo> responseVoCall = retrofitService.getTappingData(lastSurveyRequestVo);
//            responseVoCall.enqueue(new Callback<PMILastSurveyResponseVo>() {
//                @Override
//                public void onResponse(Call<PMILastSurveyResponseVo> call, Response<PMILastSurveyResponseVo> response) {
//
//                    if (response.body() != null) {
//                        poleNumList.clear();
//                        PMILastSurveyResponseVo response_vo = response.body();
//                        // progressDialog.dismiss();
//                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
//                            // arcGISDatabase.clearSurveyedData();
//                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
//                                Toast.makeText(PMIPoleSurveyActivity.this, "No Tapping Poles Found", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                List<PMILastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
//                                for (PMILastSurveyResponseDataVo vo : masterResponseDataVos) {
//                                    poleNumList.add(vo.getPOLE_NO() + "-" + vo.getTAPPING_LOCNAME());
//                                }
//                                btn_polenext.setVisibility(View.GONE);
//                                // lyt_surveytype.setVisibility(View.GONE);
//                                btn_taping.setVisibility(View.VISIBLE);
//                                lyt_tapingpoint.setVisibility(View.VISIBLE);
//                                poleNumList.add("Select");
//                                Collections.reverse(poleNumList);
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMIPoleSurveyActivity.this,
//                                        android.R.layout.simple_spinner_dropdown_item, poleNumList);
//                                sp_tappingpoint.setAdapter(adapter);
//                            }
//                            progressDialog.dismiss();
//                        } else {
//                            // Toast.makeText(PoleSurveyActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
//
//                            if (response_vo.getAPK() != null) {
//                                if (response_vo.getAPK().contains("http")) {
//                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), PoleSurveyActivity.this);
//                                apkDowanloadInstal.execute();*/
//                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIPoleSurveyActivity.this);
//                                    alertDialogBuilder.setTitle("A new Version is available!");
//                                    alertDialogBuilder
//                                            .setMessage("Before upgrade,please trasmit the data to server ")
//                                            .setCancelable(false)
//                                            .setPositiveButton("Ok",
//                                                    new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//
//                                                        }
//                                                    })
//
//                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//
//                                                }
//                                            });
//                                    AlertDialog alertDialog = alertDialogBuilder.create();
//                                    alertDialog.show();
//                                }
//                            } else {
//                                Toast.makeText(PMIPoleSurveyActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
//
//                            }
//                            progressDialog.dismiss();
//                        }
//                    } else {
//                        progressDialog.dismiss();
//                        Toast.makeText(PMIPoleSurveyActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<PMILastSurveyResponseVo> call, Throwable t) {
//                    progressDialog.dismiss();
//                    //  progressDialog.dismiss();
//                    Toast.makeText(PMIPoleSurveyActivity.this, "Try Again", Toast.LENGTH_LONG).show();
//                }
//            });
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

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
                    Toast.makeText(PMIPoleSurveyActivity.this, "Please Enter Pole 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pole_2.getText().toString().isEmpty()){
                    Toast.makeText(PMIPoleSurveyActivity.this, "Please Enter Pole 2", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (pole_1.getText().toString().equals(pole_2.getText().toString())){
                    Toast.makeText(PMIPoleSurveyActivity.this, "Pole numbers should not be same", Toast.LENGTH_SHORT).show();
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
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
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

                            PMIIntLocations locations = new PMIIntLocations();
                            locations.setPole(object.get("POLE_NO1").getAsString());
                            locations.setLat(object.get("GLAT1").getAsString());
                            locations.setLon(object.get("GLOG1").getAsString());
                            poleLocations.add(locations);
                            PMIIntLocations locations1 = new PMIIntLocations();
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
                            Intent intent = new Intent(PMIPoleSurveyActivity.this, IntermediateMapActivity.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME", feederName);
                            intent.putExtra("FEEDER_CODE", feederCode);
                            intent.putExtra("MAX_COUNT",tv_surveyedpole.getText().toString().trim());
                            intent.putExtra("POLE_1", pole1);
                            intent.putExtra("POLE_2", pole2);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(PMIPoleSurveyActivity.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PMIPoleSurveyActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMIPoleSurveyActivity.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });

    }
}