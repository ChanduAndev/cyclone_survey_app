package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;

import com.apepdcl.gis_survey.Profile;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.MainRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.incremental.MainIncementalRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.AppDataRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.UtilitesRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederMasterRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederMasterResponseDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederMasterResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalFeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalFeederMasterResponseDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalFeederMasterResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeederMasterRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
    private LinearLayout btn_survey, btn_trasmitdata, btn_completedsurvey, btn_profile;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private ProgressDialog progressDialog;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private List<IncrementalSurveyVoRDSSUg11Kv> incrementalSurveyVoRDSSUg11KvList;
    private SharedPreferences preferences;
    private String userName, sessionId;
    private List<LoginDataResponseRDSSUg11Kv> dataResponses;
    private ImageButton img_logout;
    private int feederMasterflag = 0;
    private ImageView imv_imags;
    private static final long GET_DATA_INTERVAL = 6000;
    private Animation animation2;
    private int value;
    //private int images[] = {R.drawable.image1, R.drawable.image2};
    private int images[] = {R.drawable.image2};
    private Handler hand = new Handler();
    private Button btn_incremtal_gissurvey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_feedermaster_rdss_ug_11kv);
            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            btn_survey = (LinearLayout) findViewById(R.id.btn_survey);
            btn_trasmitdata = (LinearLayout) findViewById(R.id.btn_trasmitdata);
            btn_completedsurvey = (LinearLayout) findViewById(R.id.btn_completedsurvey);
            btn_profile = (LinearLayout) findViewById(R.id.btn_profile);
            imv_imags = (ImageView) findViewById(R.id.imv_imags);
            btn_survey.setOnClickListener(this);
            btn_trasmitdata.setOnClickListener(this);
            btn_completedsurvey.setOnClickListener(this);
            btn_profile.setOnClickListener(this);
            btn_incremtal_gissurvey = (Button) findViewById(R.id.btn_incremtal_gissurvey);
            btn_incremtal_gissurvey.setOnClickListener(this);
            hand.postDelayed(run, GET_DATA_INTERVAL);
            animation2 = AnimationUtils.loadAnimation(FeederMasterRDSSUg11KvActivity.this, R.anim.from_middle);
            animation2.setAnimationListener(this);

            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(this);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11KV UG Cable Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            incrementalSurveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            if (Build.VERSION.SDK_INT >= 23 && !AppDataRDSSUg11Kv.getInstance().isPermission()) {
                UtilitesRDSSUg11Kv.isStoragePermissionGranted(FeederMasterRDSSUg11KvActivity.this);
            }
            preferences = PreferenceManager.getDefaultSharedPreferences(FeederMasterRDSSUg11KvActivity.this);
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intent = new Intent(FeederMasterRDSSUg11KvActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            dataResponses = arcGISDatabaseRDSSUg11Kv.getUserMasterData();
            List<IncrementalSurveyVoRDSSUg11Kv> surveyVosList = arcGISDatabaseRDSSUg11Kv.getIncrementalSurveyData("");
            List<String> oh_ht_servicenoList1 = arcGISDatabaseRDSSUg11Kv.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE");
            ArrayList<String> dtrsectionList_oh = arcGISDatabaseRDSSUg11Kv.getIncrementalAll_DtrStructureSubStationMaster();
            setSupportActionBar(toolbar);

        /*    btn_survey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ConnectivityChecking.checkConnection(FeederMasterActivity.this)) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            System.out.println("Btn Survey :"+1);
                            if (AppData.getInstance().isPermission()) {
                                validate();
                                // getFeederMasterData();
                            } else {
                                if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                    Utilites.isStoragePermissionGranted(FeederMasterActivity.this);
                                }
                            }
                        } else {
                            validate();
                            // getFeederMasterData();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }
                }
            });*/


        } catch (Exception e) {
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_survey:
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(FeederMasterRDSSUg11KvActivity.this)) {
                  /*  if (Build.VERSION.SDK_INT >= 23) {
                        if (AppDataRDSSUg11Kv.getInstance().isPermission()) {
                            validate();
                           // getFeederMasterData();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppDataRDSSUg11Kv.getInstance().isPermission()) {
                                UtilitesRDSSUg11Kv.isStoragePermissionGranted(FeederMasterRDSSUg11KvActivity.this);
                            }
                        }
                    } else {
                        validate();
                       // getFeederMasterData();
                    }*/
                    validate();
                } else {
                    CommanUtilits.internetDialog(FeederMasterRDSSUg11KvActivity.this);
                    //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_trasmitdata:
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(FeederMasterRDSSUg11KvActivity.this)) {
                    valiateTransmit();
                } else {
                    CommanUtilits.internetDialog(FeederMasterRDSSUg11KvActivity.this);
                    // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_completedsurvey:
              /*  if (ConnectivityChecking.checkConnection(FeederMasterActivity.this)) {
                    feederMasterflag = 1;
                    getCMPFeederMasterData();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }*/

                Intent intent = new Intent(FeederMasterRDSSUg11KvActivity.this, FeederKetchHomeRDSSUg11KvActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_profile:
                Intent in = new Intent(FeederMasterRDSSUg11KvActivity.this, Profile.class);
                startActivity(in);
                // finish();
                break;
            case R.id.btn_incremtal_gissurvey:
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(FeederMasterRDSSUg11KvActivity.this)) {
                   /* if (Build.VERSION.SDK_INT >= 23) {
                        if (AppDataRDSSUg11Kv.getInstance().isPermission()) {
                            validateIncremntal();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppDataRDSSUg11Kv.getInstance().isPermission()) {
                                UtilitesRDSSUg11Kv.isStoragePermissionGranted(FeederMasterRDSSUg11KvActivity.this);
                            }
                        }
                    } else {
                        validateIncremntal();
                    }*/
                    validateIncremntal();
                } else {
                    CommanUtilits.internetDialog(FeederMasterRDSSUg11KvActivity.this);
                    //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void getFeederMasterData() {
        try {
            System.out.println("Btn Survey 2:" + 2);
            progressDialog.show();
            FeederMasterRequestVoRDSSUg11Kv feederMasterRequestVoRDSSUg11Kv = new FeederMasterRequestVoRDSSUg11Kv();
            feederMasterRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(FeederMasterRDSSUg11KvActivity.this));
            feederMasterRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            feederMasterRequestVoRDSSUg11Kv.setUSER_ID(preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, ""));
            feederMasterRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            Log.v("DATA===>>", "" + feederMasterRequestVoRDSSUg11Kv.getSESSION_ID());
            /*Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);
            Log.v("DATA===>>", "" + json);*/
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<FeederMasterResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getFeederMaterData(feederMasterRequestVoRDSSUg11Kv);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVoRDSSUg11Kv> call, Response<FeederMasterResponseVoRDSSUg11Kv> response) {

                    if (response.body() != null) {
                        Log.v("DATARES===>>", "" + response);
                        FeederMasterResponseVoRDSSUg11Kv response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearFeederData();

                            List<FeederMasterResponseDataVoRDSSUg11Kv> masterResponseDataVos = response_vo.getRESPONSE();
                            for (FeederMasterResponseDataVoRDSSUg11Kv vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                FeederDataVoRDSSUg11Kv feederDataVoRDSSUg11Kv = new FeederDataVoRDSSUg11Kv();
                                feederDataVoRDSSUg11Kv.setEHT_SS_CODE(ssCode);
                                feederDataVoRDSSUg11Kv.setEHT_SS_NAME(ssName);
                                feederDataVoRDSSUg11Kv.setFEEDER_CODE_33KV(feederCode);
                                feederDataVoRDSSUg11Kv.setFEEDER_NAME_33KV(feedERName);

                                feederDataVoRDSSUg11Kv.setWO_ID(vo.getWO_ID());
                                feederDataVoRDSSUg11Kv.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
                                arcGISDatabaseRDSSUg11Kv.addFeederData(feederDataVoRDSSUg11Kv);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            if (feederMasterflag == 1) {
                                feederMasterflag = 0;
                                Intent intent = new Intent(FeederMasterRDSSUg11KvActivity.this, CompetedSurveyFeederRDSSUg11KvActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Intent intent = new Intent(FeederMasterRDSSUg11KvActivity.this, MainRDSSUg11KvActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss();
                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeederMasterRDSSUg11KvActivity.this);

                                    alertDialogBuilder.setTitle("A New Version Is Available!");
                                    //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                    alertDialogBuilder

                                            .setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                                                    "Please Download latest app"))
                                            //.setMessage("Before upgrade,please trasmit the data to server ")
                                            /*  .setMessage("Before upgrade,please transmit the data to server \n " +
                                                      "Please Click to Download latest app \n "+link1+" Check this link out</a>")*/
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            String url = MobileConstantsRDSSUg11Kv.APK_URL;
                                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                                            i.setData(Uri.parse(url));
                                                            startActivity(i);

                                                        }
                                                    })

                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                }
                            } else {
                                Toast.makeText(FeederMasterRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(FeederMasterRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(FeederMasterRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* public void getCMPFeederMasterData() {
         try {
             progressDialog.show();
             FeederMasterRequestVo feederMasterRequestVo = new FeederMasterRequestVo();
             feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(FeederMasterActivity.this));
             feederMasterRequestVo.setSESSION_ID(sessionId);
             feederMasterRequestVo.setUSER_ID(preferences.getString(MobileConstants.USERNAME, ""));
             feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION);
             *//*Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);
            Log.v("DATA===>>", "" + json);*//*
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<FeederMasterResponseVo> responseVoCall = retrofitService.getCmpFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVo> call, Response<FeederMasterResponseVo> response) {

                    if (response.body() != null) {
                        FeederMasterResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearFeederData();

                            List<FeederMasterResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (FeederMasterResponseDataVo vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                FeederDataVo feederDataVo = new FeederDataVo();
                                feederDataVo.setEHT_SS_CODE(ssCode);
                                feederDataVo.setEHT_SS_NAME(ssName);
                                feederDataVo.setFEEDER_CODE_33KV(feederCode);
                                feederDataVo.setFEEDER_NAME_33KV(feedERName);
                                arcGISDatabase.addFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            if (feederMasterflag == 1) {
                                feederMasterflag = 0;
                                Intent intent = new Intent(FeederMasterActivity.this, CompetedSurveyFeederActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Intent intent = new Intent(FeederMasterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss();
                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               *//* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*//*

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeederMasterActivity.this);
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
                                Toast.makeText(FeederMasterActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(FeederMasterActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(FeederMasterActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public void getFeederMasterDataIncremental() {
        try {
            progressDialog.show();
            FeederMasterRequestVoRDSSUg11Kv feederMasterRequestVoRDSSUg11Kv = new FeederMasterRequestVoRDSSUg11Kv();
            feederMasterRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(FeederMasterRDSSUg11KvActivity.this));
            feederMasterRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            feederMasterRequestVoRDSSUg11Kv.setUSER_ID(preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, ""));
            feederMasterRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVoRDSSUg11Kv);
            Log.v("DATA===>>", "" + json);
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<IncrementalFeederMasterResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getIncrementalFeederMaterData(feederMasterRequestVoRDSSUg11Kv);
            responseVoCall.enqueue(new Callback<IncrementalFeederMasterResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<IncrementalFeederMasterResponseVoRDSSUg11Kv> call, Response<IncrementalFeederMasterResponseVoRDSSUg11Kv> response) {

                    if (response.body() != null) {
                        IncrementalFeederMasterResponseVoRDSSUg11Kv response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearIncrementalEhtMaster();

                            List<IncrementalFeederMasterResponseDataVoRDSSUg11Kv> masterResponseDataVos = response_vo.getRESPONSE();
                            for (IncrementalFeederMasterResponseDataVoRDSSUg11Kv vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                IncrementalFeederDataVoRDSSUg11Kv incrementalFeederDataVoRDSSUg11Kv = new IncrementalFeederDataVoRDSSUg11Kv();
                                incrementalFeederDataVoRDSSUg11Kv.setEHT_SS_CODE(ssCode);
                                incrementalFeederDataVoRDSSUg11Kv.setEHT_SS_NAME(ssName);
                                incrementalFeederDataVoRDSSUg11Kv.setFEEDER_CODE_33KV(feederCode);
                                incrementalFeederDataVoRDSSUg11Kv.setFEEDER_NAME_33KV(feedERName);
                                incrementalFeederDataVoRDSSUg11Kv.setWORKORDER_ID(vo.getWO_ID());
                                incrementalFeederDataVoRDSSUg11Kv.setSTATUS(vo.getSTATUS());
                                incrementalFeederDataVoRDSSUg11Kv.setFROM_POLE(vo.getTAPPING_POLE_NO_11KV());
                                incrementalFeederDataVoRDSSUg11Kv.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
                                arcGISDatabaseRDSSUg11Kv.addIncrementFeederData(incrementalFeederDataVoRDSSUg11Kv);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            if (feederMasterflag == 1) {
                                feederMasterflag = 0;
                                Intent intent = new Intent(FeederMasterRDSSUg11KvActivity.this, CompetedSurveyFeederRDSSUg11KvActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Intent intent = new Intent(FeederMasterRDSSUg11KvActivity.this, MainIncementalRDSSUg11KvActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss();
                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/
                                    String link1 = "Html.fromHtml(\"your message,<a href=\"http://www.google.com\">link</a>";
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeederMasterRDSSUg11KvActivity.this);

                                    alertDialogBuilder.setTitle("A New Version Is Available!");
                                    //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                    alertDialogBuilder

                                            .setMessage(Html.fromHtml("Please Download latest app\n " +
                                                    ""))
                                            //.setMessage("Before upgrade,please trasmit the data to server ")
                                            /*  .setMessage("Before upgrade,please transmit the data to server \n " +
                                                      "Please Click to Download latest app \n "+link1+" Check this link out</a>")*/
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            String url = MobileConstantsRDSSUg11Kv.APK_URL;
                                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                                            i.setData(Uri.parse(url));
                                                            startActivity(i);

                                                        }
                                                    })

                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                }

                            } else {
                                Toast.makeText(FeederMasterRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(FeederMasterRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<IncrementalFeederMasterResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(FeederMasterRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* @Override
     public void onRequestPermissionsResult ( int requestCode, String[] permissions,
                                              int[] grantResults){
         try {
             if (grantResults.length > 0
                     && grantResults[0] == PackageManager.PERMISSION_GRANTED
                     && grantResults[1] == PackageManager.PERMISSION_GRANTED
                     && grantResults[2] == PackageManager.PERMISSION_GRANTED
                     && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                 *//*&& grantResults[0]== PackageManager.PERMISSION_GRANTED
                && grantResults[0]== PackageManager.PERMISSION_GRANTED
                && grantResults[0]== PackageManager.PERMISSION_GRANTED*//*
                AppData.getInstance().setPermission(true);
            } else {
                AppData.getInstance().setPermission(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public void validateIncremntal() {
        try {
            if (surveyVoRDSSUg11KvList.size() > 0 || incrementalSurveyVoRDSSUg11KvList.size() > 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Please transmit the data before  survey start");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })

                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                getFeederMasterDataIncremental();
            }
        } catch (Exception e) {
        }
    }

    public void validate() {
        try {
            if (surveyVoRDSSUg11KvList.size() > 0 || incrementalSurveyVoRDSSUg11KvList.size() > 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Please transmit the data before  survey start");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })

                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                getFeederMasterData();
            }
        } catch (Exception e) {
        }
    }

    public void valiateTransmit() {
       /* if (surveyVoList.size() > 0 || incrementalSurveyVoList..size() > 0 )  {
            Intent intent = new Intent(FeederMasterActivity.this, TransmitDataActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(FeederMasterActivity.this, "No Records to Transmit", Toast.LENGTH_SHORT).show();
        }*/
        Intent intent = new Intent(FeederMasterRDSSUg11KvActivity.this, TransmitHomeRDSSUg11KvActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            Random rand = new Random();
            value = rand.nextInt(images.length);
            imv_imags.setBackgroundResource(images[value]);
            imv_imags.clearAnimation();
            imv_imags.setAnimation(animation2);
            imv_imags.startAnimation(animation2);
            hand.postDelayed(run, GET_DATA_INTERVAL);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Are you sure you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                       // System.exit(0);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
    }


 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
           *//* case R.id.item_profile:
                Intent intentPf = new Intent(FeederMasterRDSSUg11KvActivity.this, ProfileActivityRDSSUg11KvActivity.class);
                startActivity(intentPf);
                finish();
                return true;
            case R.id.item_help:
                Intent in = new Intent(FeederMasterRDSSUg11KvActivity.this, ProfileActivityRDSSUg11KvActivity.class);
                startActivity(in);
                finish();
                return true;*//*
            case R.id.item_logout:
                logoutMethod();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void logoutMethod() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure you want to clear all the data?");
        alertDialogBuilder
                .setMessage("Click yes to Logout!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    SharedPreferences.Editor edit = preferences.edit();
                                    /*edit.clear();
                                    edit.commit();*/
                                    edit.putString(MobileConstantsRDSSUg11Kv.USERNAME, "");
                                    edit.putString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
                                    edit.putInt("DATABASE_VERSION", 0);
                                    edit.apply();
                                    edit.commit();
                                    arcGISDatabaseRDSSUg11Kv.clearData();

                                    Intent intent = new Intent(FeederMasterRDSSUg11KvActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    Toast.makeText(FeederMasterRDSSUg11KvActivity.this, "Session expired please logout and login again", Toast.LENGTH_LONG).show();
                                }
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
}
