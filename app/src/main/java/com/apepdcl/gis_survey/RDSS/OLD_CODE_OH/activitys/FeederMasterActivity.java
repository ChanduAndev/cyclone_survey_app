package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys;

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

import com.apepdcl.gis_survey.Profile;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.Constants.MobileConstants;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.MainActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.incremetal.MainIncementalActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.AppData;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.Utilites;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.utilits.CommanUtilits;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederMasterRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederMasterResponseDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederMasterResponseVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalFeederDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalFeederMasterResponseDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalFeederMasterResponseVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalSurveyVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginDataResponse;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SurveyVo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeederMasterActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
    private LinearLayout btn_survey, btn_trasmitdata, btn_completedsurvey, btn_profile;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyVo> surveyVoList;
    private List<IncrementalSurveyVo> incrementalSurveyVoList;
    private SharedPreferences preferences;
    private String userName, sessionId;
    private List<LoginDataResponse> dataResponses;
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
            setContentView(R.layout.activity_feedermaster_11kv_oh_rdss);
            progressDialog = CommanUtilits.progressDialog(this);
            btn_survey = (LinearLayout) findViewById(R.id.btn_survey);
            btn_trasmitdata = (LinearLayout) findViewById(R.id.btn_trasmitdata);
            btn_completedsurvey = (LinearLayout) findViewById(R.id.btn_completedsurvey);
            btn_profile = (LinearLayout) findViewById(R.id.btn_profile);
            imv_imags = (ImageView) findViewById(R.id.imv_imags);
            btn_survey.setOnClickListener(this);
            btn_trasmitdata.setOnClickListener(this);
            btn_completedsurvey.setOnClickListener(this);
            btn_profile.setOnClickListener(this);
            btn_incremtal_gissurvey=(Button)findViewById(R.id.btn_incremtal_gissurvey);
            btn_incremtal_gissurvey.setOnClickListener(this);
            hand.postDelayed(run, GET_DATA_INTERVAL);
            animation2 = AnimationUtils.loadAnimation(FeederMasterActivity.this, R.anim.from_middle);
            animation2.setAnimationListener(this);

            arcGISDatabase = new ArcGISDatabase(this);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("  RDSS 11KV Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            incrementalSurveyVoList=arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                Utilites.isStoragePermissionGranted(FeederMasterActivity.this);
            }
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intent = new Intent(FeederMasterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            userName = preferences.getString(MobileConstants.USERNAME, "");
            dataResponses = arcGISDatabase.getUserMasterData();
            List<IncrementalSurveyVo> surveyVosList= arcGISDatabase.getIncrementalSurveyData("");
            List<String> oh_ht_servicenoList1 = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE");
            ArrayList<String>  dtrsectionList_oh = arcGISDatabase.getIncrementalAll_DtrStructureSubStationMaster();
            setSupportActionBar(toolbar);



        }catch (Exception e){}
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_survey:
                if (ConnectivityChecking.checkConnection(FeederMasterActivity.this)) {
                    validate();
                    /*if (Build.VERSION.SDK_INT >= 23) {
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
                    }*/
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_trasmitdata:
                if (ConnectivityChecking.checkConnection(FeederMasterActivity.this)) {
                    valiateTransmit();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_completedsurvey:
              /*  if (ConnectivityChecking.checkConnection(FeederMasterActivity.this)) {
                    feederMasterflag = 1;
                    getCMPFeederMasterData();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }*/

                Intent intent = new Intent(FeederMasterActivity.this, FeederKetchHomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_profile:
                Intent in = new Intent(FeederMasterActivity.this, Profile.class);
                startActivity(in);
                break;
            case R.id.btn_incremtal_gissurvey:
               if (ConnectivityChecking.checkConnection(FeederMasterActivity.this)) {
                    /*if (Build.VERSION.SDK_INT >= 23) {
                        if (AppData.getInstance().isPermission()) {
                            validateIncremntal();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                Utilites.isStoragePermissionGranted(FeederMasterActivity.this);
                            }
                        }
                    } else {
                        validateIncremntal();
                    }*/

                   validateIncremntal();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void getFeederMasterData() {
        try {
            progressDialog.show();
            FeederMasterRequestVo feederMasterRequestVo = new FeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(FeederMasterActivity.this));
            feederMasterRequestVo.setSESSION_ID(sessionId);
            feederMasterRequestVo.setUSER_ID(preferences.getString(MobileConstants.USERNAME, ""));
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION);
            /*Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);
            Log.v("DATA===>>", "" + json);*/
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<FeederMasterResponseVo> responseVoCall = retrofitService.getFeederMaterData(feederMasterRequestVo);
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

                                feederDataVo.setWO_ID(vo.getWO_ID());
                                feederDataVo.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
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

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeederMasterActivity.this);

                                    alertDialogBuilder.setTitle("A new Version is available!");
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
                                                            String url = MobileConstants.APK_URL;
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
                                    ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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
            FeederMasterRequestVo feederMasterRequestVo = new FeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(FeederMasterActivity.this));
            feederMasterRequestVo.setSESSION_ID(sessionId);
            feederMasterRequestVo.setUSER_ID(preferences.getString(MobileConstants.USERNAME, ""));
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);
            Log.v("DATA===>>", "" + json);
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<IncrementalFeederMasterResponseVo> responseVoCall = retrofitService.getIncrementalFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<IncrementalFeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<IncrementalFeederMasterResponseVo> call, Response<IncrementalFeederMasterResponseVo> response) {

                    if (response.body() != null) {
                        IncrementalFeederMasterResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearIncrementalEhtMaster();

                            List<IncrementalFeederMasterResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (IncrementalFeederMasterResponseDataVo vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                IncrementalFeederDataVo incrementalFeederDataVo = new IncrementalFeederDataVo();
                                incrementalFeederDataVo.setEHT_SS_CODE(ssCode);
                                incrementalFeederDataVo.setEHT_SS_NAME(ssName);
                                incrementalFeederDataVo.setFEEDER_CODE_33KV(feederCode);
                                incrementalFeederDataVo.setFEEDER_NAME_33KV(feedERName);
                                incrementalFeederDataVo.setWORKORDER_ID(vo.getWO_ID());
                                incrementalFeederDataVo.setSTATUS(vo.getSTATUS());
                                incrementalFeederDataVo.setFROM_POLE(vo.getTAPPING_POLE_NO_11KV());
                                incrementalFeederDataVo.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
                                arcGISDatabase.addIncrementFeederData(incrementalFeederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            if (feederMasterflag == 1) {
                                feederMasterflag = 0;
                                Intent intent = new Intent(FeederMasterActivity.this, CompetedSurveyFeederActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Intent intent = new Intent(FeederMasterActivity.this, MainIncementalActivity.class);
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
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeederMasterActivity.this);

                                    alertDialogBuilder.setTitle("A new Version is available!");
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
                                                            String url = MobileConstants.APK_URL;
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
                                    ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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
                public void onFailure(Call<IncrementalFeederMasterResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(FeederMasterActivity.this, "Try Again", Toast.LENGTH_LONG).show();
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
           if (surveyVoList.size() > 0 ||incrementalSurveyVoList.size()>0) {
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
            if (surveyVoList.size() > 0 ||incrementalSurveyVoList.size()>0) {
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
        Intent intent = new Intent(FeederMasterActivity.this, TransmitHomeActivity.class);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            /*case R.id.item_profile:
                Intent intentPf = new Intent(FeederMasterActivity.this, ProfileActivityActivity.class);
                startActivity(intentPf);
                finish();
                return true;
            case R.id.item_help:
                Intent in = new Intent(FeederMasterActivity.this, ProfileActivityActivity.class);
                startActivity(in);
                finish();
                return true;*/
            case R.id.item_logout:
                Intent intentPf = new Intent(FeederMasterActivity.this, Profile.class);
                startActivity(intentPf);
                return true;
            case R.id.item_ver:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                                    edit.clear();
                                    edit.commit();
                                    arcGISDatabase.clearData();

                                    Intent intent = new Intent(FeederMasterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    Toast.makeText(FeederMasterActivity.this, "Session expired please logout and login again", Toast.LENGTH_LONG).show();
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
