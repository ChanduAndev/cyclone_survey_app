package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.LoadMapHomeActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.MainActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.incremental.IncrementalFeederActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.runtimeperimissions.AppDataUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.runtimeperimissions.UtilitesUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederMasterRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederMasterResponseDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederMasterResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederMasterResponseDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederMasterResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginDataResponseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FeederMasterActivityUG33KV extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
    private LinearLayout btn_survey,btn_trasmitdata,btn_completedsurvey,btn_profile;
    Button btn_incremental_survey;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private PMIArcGISDatabase arcGISDatabase1;
    private List<SurveyVoUG33KV> surveyVoUG33KVList;
    private List<IncrementalSurveyVoUG33KV> incrementalSurveyVoUG33KVList;
    private SharedPreferences preferences;
    private String userName,sessionId;
    private List<LoginDataResponse> loginDataResponses;
    private ImageButton img_logout;
    private int feederMasterflag=0;
    private ImageView imv_imags;
    private static final long GET_DATA_INTERVAL = 6000;
    private Animation animation2;
    private int value;
    private int images[] = {R.drawable.image1,R.drawable.image2};
    private Handler hand = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedermaster_ugc_33kv);
        btn_survey=(LinearLayout)findViewById(R.id.btn_survey);
        btn_trasmitdata=(LinearLayout)findViewById(R.id.btn_trasmitdata);
        btn_completedsurvey=(LinearLayout)findViewById(R.id.btn_completedsurvey);
        btn_profile=(LinearLayout)findViewById(R.id.btn_profile);
        btn_incremental_survey=(Button)findViewById(R.id.btn_incremental_survey);
        img_logout=(ImageButton)findViewById(R.id.img_logout);
        imv_imags=(ImageView)findViewById(R.id.imv_imags);
        btn_survey.setOnClickListener(this);
        btn_trasmitdata.setOnClickListener(this);
        btn_completedsurvey.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        btn_incremental_survey.setOnClickListener(this);
        hand.postDelayed(run, GET_DATA_INTERVAL);
        animation2 = AnimationUtils.loadAnimation(FeederMasterActivityUG33KV.this, R.anim.from_middle);
        animation2.setAnimationListener(this);

        arcGISDatabase=new ArcGISDatabaseUG33KV(this);
        loginDataResponses = arcGISDatabase.getUserMasterData();
        progressDialog=new ProgressDialog(FeederMasterActivityUG33KV.this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
//        getSupportActionBar().setTitle("RDSS 33kV UG Cable Survey");
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("RDSS 33kV UG Cable Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        surveyVoUG33KVList =arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
        incrementalSurveyVoUG33KVList =arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
        if (Build.VERSION.SDK_INT >= 23 && !AppDataUG33KV.getInstance().isPermission()) {
            UtilitesUG33KV.isStoragePermissionGranted(FeederMasterActivityUG33KV.this);
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId=preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS,"");
        //sessionId=preferences.getString(MobileConstants.SESSION_ID,"");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(FeederMasterActivityUG33KV.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        userName=preferences.getString(MobileConstantsUG33KV.USERNAME,"");
        img_logout.setVisibility(View.VISIBLE);
        img_logout.setOnClickListener(this);
        //img_logout.setImageResource(R.drawable.logout);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_survey:
                System.out.println("STEP_1--->");
                if(ConnectivityCheckingUG33KV.checkConnection(FeederMasterActivityUG33KV.this)) {
                    validate();
//                    if (Build.VERSION.SDK_INT >= 23) {
//                        if (AppDataUG33KV.getInstance().isPermission()) {
//                            System.out.println("STEP_2--->");
//                            validate();
//                        } else {
//                            System.out.println("STEP_3--->");
//                            if (!AppDataUG33KV.getInstance().isPermission()) {
//                                UtilitesUG33KV.isStoragePermissionGranted(FeederMasterActivityUG33KV.this);
//                                System.out.println("STEP_4--->");
//                                validate();
//
//
//                            }
//                        }
//
//                    } else {
//                        validate();
//                        System.out.println("STEP_5--->");
//                    }

                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.btn_trasmitdata:
               /* if (Build.VERSION.SDK_INT >= 23) {
                    if (AppData.getInstance().isPermission()) {*/
               /* if (ConnectivityChecking.checkConnection(FeederMasterActivity.this)) {
                    valiateTransmit();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }*/

                if(surveyVoUG33KVList.size()>0 || incrementalSurveyVoUG33KVList.size()>0){
                    Intent intentPf=new Intent(FeederMasterActivityUG33KV.this, TransmitDataHomeActivityUG33KV.class);
                    startActivity(intentPf);
                    finish();
                }else {
                    Toast.makeText(FeederMasterActivityUG33KV.this,"No data to transmit",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_completedsurvey:
                if (ConnectivityCheckingUG33KV.checkConnection(FeederMasterActivityUG33KV.this)) {
                    feederMasterflag=1;
                    getCmpFeederMasterData();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
              /*  Intent intent=new Intent(FeederMasterActivity.this,CompetedSurveyFeederActivity.class);
                        startActivity(intent);
                        finish();*/
                break;
            case R.id.btn_profile:

                Intent intentPf=new Intent(FeederMasterActivityUG33KV.this,ProfileActivityActivityUG33KV.class);
                startActivity(intentPf);
                finish();
                break;

            case R.id.btn_incremental_survey:

                if(ConnectivityCheckingUG33KV.checkConnection(FeederMasterActivityUG33KV.this)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (AppDataUG33KV.getInstance().isPermission()) {
                            validateIncrement();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppDataUG33KV.getInstance().isPermission()) {
                                UtilitesUG33KV.isStoragePermissionGranted(FeederMasterActivityUG33KV.this);

                            }
                        }

                    } else {
                        validateIncrement();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.img_logout:
                try {
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

                                                Intent intent = new Intent(FeederMasterActivityUG33KV.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } catch (NullPointerException e) {
                                                e.printStackTrace();
                                                Toast.makeText(FeederMasterActivityUG33KV.this, "Session expired please logout and login again", Toast.LENGTH_LONG).show();
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
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

        }
    }

    public void getCmpFeederMasterData(){

        Intent intent = new Intent(FeederMasterActivityUG33KV.this, LoadMapHomeActivityUG33KV.class);
        startActivity(intent);
        finish();
      /*  try {
            progressDialog.show();
            FeederMasterRequestVo feederMasterRequestVo = new FeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(FeederMasterActivity.this));
            feederMasterRequestVo.setSESSION_ID(sessionId);
            feederMasterRequestVo.setUSER_ID(dataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);

            Log.v("DATA===>>", "" + json);
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<FeederMasterResponseVo> responseVoCall = retrofitService.getCmpFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVo> call, Response<FeederMasterResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        FeederMasterResponseVo response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);

                        Log.v("DATA===>>", "" + json);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.cmpClearFeederData();

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
                                arcGISDatabase.addCmpFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                            Intent intent = new Intent(FeederMasterActivity.this, CompetedSurveyFeederActivity.class);
                            startActivity(intent);
                            finish();
                           *//* Intent intent = new Intent(FeederMasterActivity.this, NewSurveyFeederActivity.class);
                            startActivity(intent);
                            finish();*//*


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
                        }
                    } else {
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
        }catch (Exception e){
            e.printStackTrace();

        }*/
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




    public void validate(){
        if(surveyVoUG33KVList.size()>0 || incrementalSurveyVoUG33KVList.size()>0) {
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
        }else {
            getFeederMasterData();
        }
    }
    public void validateIncrement(){
        getIncrementalFeederMasterData();

      /*  if(incrementalSurveyVoList.size()>0) {
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
        }else {
            getIncrementalFeederMasterData();
        }*/
    }

    public void valiateTransmit(){
        if(incrementalSurveyVoUG33KVList.size()>0) {
            // Intent intent = new Intent(FeederMasterActivity.this, TransmitDataActivity.class);
            Intent intent = new Intent(FeederMasterActivityUG33KV.this, IncrementalTransmitDataActivityUG33KV.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(FeederMasterActivityUG33KV.this,"No Records to Transmit",Toast.LENGTH_SHORT).show();
        }
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

            // Set height = 200dp
            ViewGroup.LayoutParams params = imv_imags.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    200,
                    getResources().getDisplayMetrics()
            );
            imv_imags.setLayoutParams(params);

            // Image change animation
            Random rand = new Random();
            value = rand.nextInt(images.length);

            imv_imags.setBackgroundResource(images[value]);
            imv_imags.clearAnimation();
            imv_imags.setAnimation(animation2);
            imv_imags.startAnimation(animation2);

            hand.postDelayed(run, GET_DATA_INTERVAL);
        }
    };

//    Runnable run = new Runnable() {
//        @Override
//        public void run() {
//            Random rand = new Random();
//            value = rand.nextInt(images.length);
//            imv_imags.setBackgroundResource(images[value]);
//            imv_imags.clearAnimation();
//            imv_imags.setAnimation(animation2);
//            imv_imags.startAnimation(animation2);
//            hand.postDelayed(run, GET_DATA_INTERVAL);
//        }
//    };

    public void getFeederMasterData(){
        try {
            progressDialog.show();
            FeederMasterRequestVoUG33KV feederMasterRequestVoUG33KV = new FeederMasterRequestVoUG33KV();
            feederMasterRequestVoUG33KV.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(FeederMasterActivityUG33KV.this));
            feederMasterRequestVoUG33KV.setSESSION_ID(sessionId);
            feederMasterRequestVoUG33KV.setUSER_ID(loginDataResponses.get(0).getOFFICE_ID());
//            if (dataResponses != null && !dataResponses.isEmpty()) {
//                feederMasterRequestVoUG33KV.setUSER_ID(dataResponses.get(0).getOFFICE_ID());
//            } else {
//                // Handle the case gracefully
//                Log.e("FeederMasterActivity", "dataResponses is empty or null");
//                feederMasterRequestVoUG33KV.setUSER_ID(""); // or show error message, or skip setting it
//            }

            //feederMasterRequestVoUG33KV.setUSER_ID(dataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVoUG33KV.setVERSION(MobileConstantsUG33KV.APP_VERSION);


            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVoUG33KV);

            Log.v("DATA===>>", "" + json);
            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<FeederMasterResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.getFeederMaterData(feederMasterRequestVoUG33KV);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVoUG33KV>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVoUG33KV> call, Response<FeederMasterResponseVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        FeederMasterResponseVoUG33KV response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearFeederData();

                            List<FeederMasterResponseDataVoUG33KV> masterResponseDataVos = response_vo.getRESPONSE();
                            for (FeederMasterResponseDataVoUG33KV vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                FeederDataVoUG33KV feederDataVoUG33KV = new FeederDataVoUG33KV();
                                feederDataVoUG33KV.setEHT_SS_CODE(ssCode);
                                feederDataVoUG33KV.setEHT_SS_NAME(ssName);
                                feederDataVoUG33KV.setFEEDER_CODE_33KV(feederCode);
                                feederDataVoUG33KV.setFEEDER_NAME_33KV(feedERName);
                                feederDataVoUG33KV.setWOID(vo.getWO_ID());
                                feederDataVoUG33KV.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
                                arcGISDatabase.addFeederData(feederDataVoUG33KV);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            Intent intent = new Intent(FeederMasterActivityUG33KV.this, MainActivityUG33KV.class);
                            startActivity(intent);
                            finish();



                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeederMasterActivityUG33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstantsUG33KV.APK_DOWNLOAD_LINK);
                                                            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
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
                                Toast.makeText(FeederMasterActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(FeederMasterActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(FeederMasterActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void getIncrementalFeederMasterData(){
        try {
            progressDialog.show();
            FeederMasterRequestVoUG33KV feederMasterRequestVoUG33KV = new FeederMasterRequestVoUG33KV();
            feederMasterRequestVoUG33KV.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(FeederMasterActivityUG33KV.this));
            feederMasterRequestVoUG33KV.setSESSION_ID(sessionId);
            feederMasterRequestVoUG33KV.setUSER_ID(loginDataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVoUG33KV.setVERSION(MobileConstantsUG33KV.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVoUG33KV);

            Log.v("DATA===>>", "" + json);
            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<IncrementalFeederMasterResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.getIncrementalFeederData(feederMasterRequestVoUG33KV);
            responseVoCall.enqueue(new Callback<IncrementalFeederMasterResponseVoUG33KV>() {
                @Override
                public void onResponse(Call<IncrementalFeederMasterResponseVoUG33KV> call, Response<IncrementalFeederMasterResponseVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        IncrementalFeederMasterResponseVoUG33KV response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearIncrementalFeederData();

                            List<IncrementalFeederMasterResponseDataVoUG33KV> masterResponseDataVos = response_vo.getRESPONSE();
                            Gson gson = new Gson();
                            String json = gson.toJson(masterResponseDataVos);

                            Log.v("DATA222===>>", "" + json);
                            for (IncrementalFeederMasterResponseDataVoUG33KV vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                IncrementalFeederDataVoUG33KV incrementalFeederDataVoUG33KV = new IncrementalFeederDataVoUG33KV();
                                incrementalFeederDataVoUG33KV.setEHT_SS_CODE(ssCode);
                                incrementalFeederDataVoUG33KV.setEHT_SS_NAME(ssName);
                                incrementalFeederDataVoUG33KV.setFEEDER_CODE_33KV(feederCode);
                                incrementalFeederDataVoUG33KV.setFEEDER_NAME_33KV(feedERName);
                                incrementalFeederDataVoUG33KV.setWORKORDER_ID(vo.getWORK_ORDER());
                                incrementalFeederDataVoUG33KV.setSTATUS(vo.getWORK_STATUS());
                                incrementalFeederDataVoUG33KV.setFROM_POLE(vo.getFROM_POLE());
                                arcGISDatabase.addIncrementalFeederData(incrementalFeederDataVoUG33KV);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            Intent intent = new Intent(FeederMasterActivityUG33KV.this, IncrementalFeederActivityUG33KV.class);
                            startActivity(intent);
                            finish();

                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FeederMasterActivityUG33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstantsUG33KV.APK_DOWNLOAD_LINK);
                                                            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
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
                                Toast.makeText(FeederMasterActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(FeederMasterActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<IncrementalFeederMasterResponseVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(FeederMasterActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
