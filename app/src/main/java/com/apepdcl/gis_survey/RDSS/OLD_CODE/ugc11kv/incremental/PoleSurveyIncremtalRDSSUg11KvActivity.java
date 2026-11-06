package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.incremental;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity.FeederMasterRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyResponseDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.TruncDataRequestVoRDSSUg11Kv;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoleSurveyIncremtalRDSSUg11KvActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Button btn_polenext, btn_taping;
    private Spinner sp_loadmap, sp_tappingpoint;
    private String loadMap, ssName, feederName, feederCode, tappingpoint, maxCount, userName, sessionId, work_order;
    private TextView tv_surveyedpole;
    // private TextInputEditText edt_frompole,edt_topole;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private int surveyedCount = 0;
    private ProgressDialog progressDialog;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private List<String> poleNumList;
    private LinearLayout lyt_tapingpoint, lyt_surveytype;
    private int flag = 0;
    private SharedPreferences preferences;
    private JsonObject responseObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polesurvey_incremntal_rdss_ug_11kv);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11KV UG Cable Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(PoleSurveyIncremtalRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            sp_loadmap = (Spinner) findViewById(R.id.sp_loadmap);
            sp_tappingpoint = (Spinner) findViewById(R.id.sp_tappingpoint);
            tv_surveyedpole = findViewById(R.id.tv_surveyedpole);
            lyt_tapingpoint = (LinearLayout) findViewById(R.id.lyt_tapingpoint);
            lyt_surveytype = (LinearLayout) findViewById(R.id.lyt_surveytype);
            btn_polenext = (Button) findViewById(R.id.btn_polenext);
            btn_taping = (Button) findViewById(R.id.btn_taping);
            lyt_tapingpoint.setVisibility(View.GONE);
            btn_taping.setVisibility(View.GONE);
            btn_polenext.setOnClickListener(this);
            btn_taping.setOnClickListener(this);
            //  getSupportActionBar().setTitle("33Kv LineSurvey");
            Intent intent = getIntent();
            if (intent != null) {
                ssName = intent.getStringExtra("SSNAME");
                feederName = intent.getStringExtra("FEEDER_NAME");
                feederCode = intent.getStringExtra("FEEDER_CODE");
                maxCount = intent.getStringExtra("MAX_COUNT");
                work_order = intent.getStringExtra("WORKORDER");
            }
            gisjhcLayOutFeedrs();
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(PoleSurveyIncremtalRDSSUg11KvActivity.this);
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
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(PoleSurveyIncremtalRDSSUg11KvActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                break;

            case R.id.sp_tappingpoint:
                tappingpoint = sp_tappingpoint.getSelectedItem().toString();
                break;


            default:

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    public void getTappingPointData() {
        try {
            progressDialog.show();
            LastSurveyRequestVoRDSSUg11Kv lastSurveyRequestVoRDSSUg11Kv = new LastSurveyRequestVoRDSSUg11Kv();
            //lastSurveyRequestVo.setFEEDER_ID(feederCode);
            lastSurveyRequestVoRDSSUg11Kv.setFEEDER_ID(feederCode);
            lastSurveyRequestVoRDSSUg11Kv.setUSER_ID(userName);
            lastSurveyRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            lastSurveyRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            lastSurveyRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(PoleSurveyIncremtalRDSSUg11KvActivity.this));

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
                                Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "No Tapping Poles Found", Toast.LENGTH_SHORT).show();

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
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PoleSurveyIncremtalRDSSUg11KvActivity.this,
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
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PoleSurveyIncremtalRDSSUg11KvActivity.this);
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
                                Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
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
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(PoleSurveyIncremtalRDSSUg11KvActivity.this)) {
                    getData();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_taping:
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(PoleSurveyIncremtalRDSSUg11KvActivity.this)) {
                    if (!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                        getTapingPolesData();
                    } else {
                        Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "Select Tapping Point", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void getData() {
        if ("Select".equals(loadMap)) {
            Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "Select Survey Type", Toast.LENGTH_LONG).show();
        }/*else if("Select".equals(loadMap)){
            Toast.makeText(PoleSurveyActivity.this,"Select Survey Type",Toast.LENGTH_LONG).show();
        }*/ else {

            if ("Through Line".equals(loadMap)) {
                Intent intent = new Intent(PoleSurveyIncremtalRDSSUg11KvActivity.this, ArcGISMapIncremntalRDSSUg11KvActivity.class);
                intent.putExtra("SSNAME", ssName);
                intent.putExtra("FEEDER_NAME", feederName);
                intent.putExtra("FEEDER_CODE", feederCode);
                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString().trim());
                intent.putExtra("WORKORDER", work_order);
                startActivity(intent);
                finish();
            } else if ("Spur Line".equalsIgnoreCase(loadMap)) {
                // if(!"Select".equalsIgnoreCase(sp_tappingpoint.getSelectedItem().toString())) {
                getTappingPointData();
               /* }else {
                  Toast.makeText(PoleSurveyActivity.this,"Select Tapping Point",Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }

    public void getTapingPolesData() {
        try {
            progressDialog.show();
            TruncDataRequestVoRDSSUg11Kv truncDataRequestVoRDSSUg11Kv = new TruncDataRequestVoRDSSUg11Kv();
            truncDataRequestVoRDSSUg11Kv.setFEEDER_ID(feederCode);
            truncDataRequestVoRDSSUg11Kv.setUSER_ID(userName);
            truncDataRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            truncDataRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            truncDataRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(PoleSurveyIncremtalRDSSUg11KvActivity.this));

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
                                Intent intent = new Intent(PoleSurveyIncremtalRDSSUg11KvActivity.this, ArcGISMapIncremntalRDSSUg11KvActivity.class);
                                intent.putExtra("SSNAME", ssName);
                                intent.putExtra("FEEDER_NAME", feederName);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                                intent.putExtra("TAPPING_POINT", tappingpoint);
                                intent.putExtra("SURVEY_TYPE", loadMap);
                                intent.putExtra("WORKORDER", work_order);
                                intent.putExtra("responseObj", String.valueOf(responseObj));
                                startActivity(intent);
                                startActivity(intent);
                                finish();
                            } else {
                                List<LastSurveyResponseDataVoRDSSUg11Kv> masterResponseDataVos = response_vo.getRESPONSE();
                                //arcGISDatabase.clearSurveyedData();
                                arcGISDatabaseRDSSUg11Kv.clearIncrementalSurveyedData();
                                for (LastSurveyResponseDataVoRDSSUg11Kv vo : masterResponseDataVos) {
                                    IncrementalSurveyVoRDSSUg11Kv surveyVo = new IncrementalSurveyVoRDSSUg11Kv();
                                    surveyVo.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVo.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVo.setLat(vo.getGLAT());
                                    surveyVo.setLan(vo.getGLONG());
                                    surveyVo.setPastSurveyFlag("Y");
                                    arcGISDatabaseRDSSUg11Kv.addIncrementalSurveyData(surveyVo);
                                }
                                Intent intent = new Intent(PoleSurveyIncremtalRDSSUg11KvActivity.this, ArcGISMapIncremntalRDSSUg11KvActivity.class);
                                intent.putExtra("SSNAME", ssName);
                                intent.putExtra("FEEDER_NAME", feederName);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", tv_surveyedpole.getText().toString());
                                intent.putExtra("TAPPING_POINT", tappingpoint);
                                intent.putExtra("SURVEY_TYPE", loadMap);
                                intent.putExtra("WORKORDER", work_order);
                                intent.putExtra("responseObj", String.valueOf(responseObj));
                                startActivity(intent);
                                finish();

                            }
                        } else {
                            // Toast.makeText(PoleSurveyActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), PoleSurveyActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PoleSurveyIncremtalRDSSUg11KvActivity.this);
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
                                Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gisjhcLayOutFeedrs() {
        try {
            if (ConnectivityCheckingRDSSUg11Kv.checkConnection(PoleSurveyIncremtalRDSSUg11KvActivity.this)) {

                progressDialog.show();
                arcGISDatabaseRDSSUg11Kv.clearCOM_TYPE_SUBTYPE_VER();
                JsonObject postParam = new JsonObject();
                postParam.addProperty("FEEDER_ID", feederCode);
                retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
                Call<JsonObject> responseVoCall = retrofitServiceRDSSUg11Kv.gisjhcLayOutFeedrs(postParam);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                                JsonObject jsonObject = response.body();
                                if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {


                                    JSONObject jSONObject = new JSONObject(String.valueOf(jsonObject));
                                    JSONArray jArray = jSONObject.getJSONArray("Apps");
                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject childrenObject = jArray.getJSONObject(i);
                                        COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv feederDataVo = new COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv();
                                        feederDataVo.setLAYOUT_NAME(childrenObject.getString("LAYOUT_NAME"));
                                        feederDataVo.setFEEDER_ID(childrenObject.getString("FEEDER_ID"));
                                        feederDataVo.setLAYOUT_ID(childrenObject.getString("LAYOUT_ID"));
                                        arcGISDatabaseRDSSUg11Kv.addCOM_TYPE_SUBTYPE_VER(feederDataVo);
                                    }


                                } else {
                                    Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(PoleSurveyIncremtalRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
