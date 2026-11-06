package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

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

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIFeederMasterRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMILoginDataResponse;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofit11kv;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitClientInstance11KV;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederMasterResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederMasterResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalSurveyVo11;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMIFeederDataVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMIFeederKetchHomeActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_loadfeeder,btn_incremental_loadfeeder;
    private PMI11kvArcGISDatabase arcGISDatabase;
    private List<PMISurveyVo> surveyVoList;
    private List<PMIIncrementalSurveyVo11> incrementalSurveyVoList;
    private PMIRetrofit11kv retrofitService;
    private ProgressDialog progressDialog;
    private String SurveyFlag = "";
    private SharedPreferences preferences;
    private String userName, sessionId;
    private List<PMILoginDataResponse> dataResponseList;
    private List<PMIFeederDataVo> feederDataVos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmifeeder_ketch_home);
        getSupportActionBar().hide();

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11Kv Load Feeder");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(PMIFeederKetchHomeActivity.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });

            progressDialog = CommanUtilits.progressDialog(this);

            btn_loadfeeder = (Button) findViewById(R.id.btn_loadfeeder);
            btn_incremental_loadfeeder= (Button) findViewById(R.id.btn_incremental_loadfeeder);
            btn_loadfeeder.setOnClickListener(this);
            btn_incremental_loadfeeder.setOnClickListener(this);
            arcGISDatabase = new PMI11kvArcGISDatabase(this);
            surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            incrementalSurveyVoList=arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            //getSupportActionBar().setTitle("11Kv LineSurvey Test");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstants.USERNAME, "");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(PMIFeederKetchHomeActivity.this, LoginActivity.class);
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
                if (ConnectivityChecking.checkConnection(PMIFeederKetchHomeActivity.this)) {

                    getCMPFeederMasterData();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }


                break;

            case R.id.btn_incremental_loadfeeder:

                if (ConnectivityChecking.checkConnection(PMIFeederKetchHomeActivity.this)) {

                    getIncrementalCMPFeederMasterData();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
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
            PMIFeederMasterRequestVo feederMasterRequestVo = new PMIFeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIFeederKetchHomeActivity.this));
            feederMasterRequestVo.setSESSION_ID(sessionId);
            feederMasterRequestVo.setUSER_ID(preferences.getString(MobileConstants.USERNAME, ""));
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);
            Log.v("DATA===>>", "" + json);
            retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMIFeederMasterResponseVo> responseVoCall = retrofitService.getFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<PMIFeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<PMIFeederMasterResponseVo> call, Response<PMIFeederMasterResponseVo> response) {

                    if (response.body() != null) {
                        PMIFeederMasterResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearFeederData();

                            List<PMIFeederMasterResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (PMIFeederMasterResponseDataVo vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                PMIFeederDataVo feederDataVo = new PMIFeederDataVo();
                                feederDataVo.setEHT_SS_CODE(ssCode);
                                feederDataVo.setEHT_SS_NAME(ssName);
                                feederDataVo.setFEEDER_CODE_33KV(feederCode);
                                feederDataVo.setFEEDER_NAME_33KV(feedERName);
                                arcGISDatabase.addFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                            Intent intent = new Intent(PMIFeederKetchHomeActivity.this, PMICompetedSurveyFeederActivity.class);
                            startActivity(intent);
                            finish();


                            progressDialog.dismiss();
                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(PMIFeederKetchHomeActivity.this);
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
                                Toast.makeText(PMIFeederKetchHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PMIFeederKetchHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMIFeederMasterResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PMIFeederKetchHomeActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getIncrementalCMPFeederMasterData() {
        try {
            progressDialog.show();
            PMIFeederMasterRequestVo feederMasterRequestVo = new PMIFeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIFeederKetchHomeActivity.this));
            feederMasterRequestVo.setSESSION_ID(sessionId);
            feederMasterRequestVo.setUSER_ID(preferences.getString(MobileConstants.USERNAME, ""));
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            /*Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);
            Log.v("DATA===>>", "" + json);*/
            retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMIFeederMasterResponseVo> responseVoCall = retrofitService.getCmpFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<PMIFeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<PMIFeederMasterResponseVo> call, Response<PMIFeederMasterResponseVo> response) {

                    if (response.body() != null) {
                        PMIFeederMasterResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearCmpFeederData();

                            List<PMIFeederMasterResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (PMIFeederMasterResponseDataVo vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                PMIFeederDataVo feederDataVo = new PMIFeederDataVo();
                                feederDataVo.setEHT_SS_CODE(ssCode);
                                feederDataVo.setEHT_SS_NAME(ssName);
                                feederDataVo.setFEEDER_CODE_33KV(feederCode);
                                feederDataVo.setFEEDER_NAME_33KV(feedERName);
                                arcGISDatabase.addCmpFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                            /*Intent intent = new Intent(FeederKetchHomeActivity.this, IncrementalCompetedFeederActivity.class);
                            startActivity(intent);
                            finish();*/


                            progressDialog.dismiss();
                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(PMIFeederKetchHomeActivity.this);
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
                                Toast.makeText(PMIFeederKetchHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PMIFeederKetchHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMIFeederMasterResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PMIFeederKetchHomeActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}