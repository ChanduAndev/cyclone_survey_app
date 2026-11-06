package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederMasterRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederMasterResponseDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederMasterResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeederKetchHomeRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener {
    //private TextView tv_escount;
    private Button btn_loadfeeder,btn_incremental_loadfeeder;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private List<IncrementalSurveyVoRDSSUg11Kv> incrementalSurveyVoRDSSUg11KvList;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private ProgressDialog progressDialog;
    private String SurveyFlag = "";
    private SharedPreferences preferences;
    private String userName, sessionId;
    private List<LoginDataResponseRDSSUg11Kv> dataResponseList;
    private List<FeederDataVoRDSSUg11Kv> feederDataVoRDSSUg11Kvs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_feedersketchhome_rdss_ug_11kv);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11KV UG Cable  Load Feeder");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(FeederKetchHomeRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });

            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);

            btn_loadfeeder = (Button) findViewById(R.id.btn_loadfeeder);
            btn_incremental_loadfeeder= (Button) findViewById(R.id.btn_incremental_loadfeeder);
            btn_loadfeeder.setOnClickListener(this);
            btn_incremental_loadfeeder.setOnClickListener(this);
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(this);
            surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            incrementalSurveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            //getSupportActionBar().setTitle("11Kv LineSurvey");
            preferences = PreferenceManager.getDefaultSharedPreferences(FeederKetchHomeRDSSUg11KvActivity.this);
            userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(FeederKetchHomeRDSSUg11KvActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loadfeeder:
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(FeederKetchHomeRDSSUg11KvActivity.this)) {

                    getCMPFeederMasterData();
                } else {
                    CommanUtilits.internetDialog(FeederKetchHomeRDSSUg11KvActivity.this);
                    //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }


                break;

            case R.id.btn_incremental_loadfeeder:

                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(FeederKetchHomeRDSSUg11KvActivity.this)) {

                    getIncrementalCMPFeederMasterData();
                } else {
                    CommanUtilits.internetDialog(FeederKetchHomeRDSSUg11KvActivity.this);
                    //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(TransmitDataActivity.this, FeederMasterActivity.class);
        startActivity(intent);
        finish();*/
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void getCMPFeederMasterData() {
        try {
            progressDialog.show();
            FeederMasterRequestVoRDSSUg11Kv feederMasterRequestVoRDSSUg11Kv = new FeederMasterRequestVoRDSSUg11Kv();
            feederMasterRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(FeederKetchHomeRDSSUg11KvActivity.this));
            feederMasterRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            feederMasterRequestVoRDSSUg11Kv.setUSER_ID(preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, ""));
            feederMasterRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVoRDSSUg11Kv);
            Log.v("DATA===>>", "" + json);
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<FeederMasterResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getFeederMaterData(feederMasterRequestVoRDSSUg11Kv);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVoRDSSUg11Kv> call, Response<FeederMasterResponseVoRDSSUg11Kv> response) {

                    if (response.body() != null) {
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
                                arcGISDatabaseRDSSUg11Kv.addFeederData(feederDataVoRDSSUg11Kv);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                                Intent intent = new Intent(FeederKetchHomeRDSSUg11KvActivity.this, CompetedSurveyFeederRDSSUg11KvActivity.class);
                                startActivity(intent);
                                finish();


                            progressDialog.dismiss();
                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(FeederKetchHomeRDSSUg11KvActivity.this);
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
                                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(FeederKetchHomeRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(FeederKetchHomeRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(FeederKetchHomeRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getIncrementalCMPFeederMasterData() {
        try {
            progressDialog.show();
            FeederMasterRequestVoRDSSUg11Kv feederMasterRequestVoRDSSUg11Kv = new FeederMasterRequestVoRDSSUg11Kv();
            feederMasterRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(FeederKetchHomeRDSSUg11KvActivity.this));
            feederMasterRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            feederMasterRequestVoRDSSUg11Kv.setUSER_ID(preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, ""));
            feederMasterRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            /*Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);
            Log.v("DATA===>>", "" + json);*/
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<FeederMasterResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getCmpFeederMaterData(feederMasterRequestVoRDSSUg11Kv);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVoRDSSUg11Kv> call, Response<FeederMasterResponseVoRDSSUg11Kv> response) {

                    if (response.body() != null) {
                        FeederMasterResponseVoRDSSUg11Kv response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearCmpFeederData();

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
                                arcGISDatabaseRDSSUg11Kv.addCmpFeederData(feederDataVoRDSSUg11Kv);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                            Intent intent = new Intent(FeederKetchHomeRDSSUg11KvActivity.this, IncrementalCompetedFeederRDSSUg11KvActivity.class);
                            startActivity(intent);
                            finish();


                            progressDialog.dismiss();
                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(FeederKetchHomeRDSSUg11KvActivity.this);
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
                                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(FeederKetchHomeRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(FeederKetchHomeRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(FeederKetchHomeRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



