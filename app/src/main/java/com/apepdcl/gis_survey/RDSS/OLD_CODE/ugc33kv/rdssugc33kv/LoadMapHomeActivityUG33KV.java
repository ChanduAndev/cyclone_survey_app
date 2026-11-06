package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.CompetedSurveyFeederActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.FeederMasterActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.IncrementalCompetedSurveyFeederActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederMasterRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederMasterResponseDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederMasterResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginDataResponseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;

import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadMapHomeActivityUG33KV extends AppCompatActivity implements View.OnClickListener {
    // private TextView tv_escount;
    private Button btn_loadfeedermap,btn_load_incrementalmap;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<SurveyVoUG33KV> surveyVoUG33KVList;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private String SurveyFlag="";
    private SharedPreferences preferences;
    private String userName,sessionId;
    private List<LoginDataResponseUG33KV> dataResponseList;
    private List<FeederDataVoUG33KV> feederDataVoUG33KVS;
    private List<IncrementalSurveyVoUG33KV> incrementalSurveyVoUG33KVList;
    private List<LoginDataResponse> dataResponses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmaphome_ugc_33kv);
        try {

            btn_loadfeedermap = (Button) findViewById(R.id.btn_loadfeedermap);
            btn_load_incrementalmap= (Button) findViewById(R.id.btn_load_incrementalmap);
            btn_loadfeedermap.setOnClickListener(this);
            btn_load_incrementalmap.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabaseUG33KV(this);
            dataResponses=arcGISDatabase.getUserMasterData();
            progressDialog=new ProgressDialog(LoadMapHomeActivityUG33KV.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);
            getSupportActionBar().setTitle("RDSS 33kV UG Cable Survey");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstantsUG33KV.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(LoadMapHomeActivityUG33KV.this, LoginActivity.class);
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
            case R.id.btn_loadfeedermap:
                if (ConnectivityCheckingUG33KV.checkConnection(LoadMapHomeActivityUG33KV.this)) {

                    loadFeederDataData();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_load_incrementalmap:
                if (ConnectivityCheckingUG33KV.checkConnection(LoadMapHomeActivityUG33KV.this)) {

                    loadIncrementalFeederDataData();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoadMapHomeActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
    public void loadFeederDataData(){
        try {
            progressDialog.show();
            FeederMasterRequestVoUG33KV feederMasterRequestVoUG33KV = new FeederMasterRequestVoUG33KV();
            feederMasterRequestVoUG33KV.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(LoadMapHomeActivityUG33KV.this));
            feederMasterRequestVoUG33KV.setSESSION_ID(sessionId);
            feederMasterRequestVoUG33KV.setUSER_ID(dataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVoUG33KV.setVERSION(MobileConstantsUG33KV.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVoUG33KV);


            Log.v("DATA===>>", "" + json);
            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<FeederMasterResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.getCmpFeederMaterData(feederMasterRequestVoUG33KV);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVoUG33KV>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVoUG33KV> call, Response<FeederMasterResponseVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        FeederMasterResponseVoUG33KV response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);

                        Log.v("DATA===>>", "" + json);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.cmpClearFeederData();

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
                                arcGISDatabase.addCmpFeederData(feederDataVoUG33KV);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                            Intent intent = new Intent(LoadMapHomeActivityUG33KV.this, CompetedSurveyFeederActivityUG33KV.class);
                            startActivity(intent);
                            finish();
                           /* Intent intent = new Intent(FeederMasterActivity.this, NewSurveyFeederActivity.class);
                            startActivity(intent);
                            finish();*/


                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadMapHomeActivityUG33KV.this);
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
                                Toast.makeText(LoadMapHomeActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(LoadMapHomeActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(LoadMapHomeActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public void loadIncrementalFeederDataData(){
        try {
            progressDialog.show();
            FeederMasterRequestVoUG33KV feederMasterRequestVoUG33KV = new FeederMasterRequestVoUG33KV();
            feederMasterRequestVoUG33KV.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(LoadMapHomeActivityUG33KV.this));
            feederMasterRequestVoUG33KV.setSESSION_ID(sessionId);
            feederMasterRequestVoUG33KV.setUSER_ID(dataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVoUG33KV.setVERSION(MobileConstantsUG33KV.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVoUG33KV);

            Log.v("DATA===>>", "" + json);
            retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
            Call<FeederMasterResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.getIncrementalCmpFeederMaterData(feederMasterRequestVoUG33KV);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVoUG33KV>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVoUG33KV> call, Response<FeederMasterResponseVoUG33KV> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        FeederMasterResponseVoUG33KV response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);

                        Log.v("DATA===>>", "" + json);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.cmpClearFeederData();

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
                                arcGISDatabase.addCmpFeederData(feederDataVoUG33KV);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                            Intent intent = new Intent(LoadMapHomeActivityUG33KV.this, IncrementalCompetedSurveyFeederActivityUG33KV.class);
                            startActivity(intent);
                            finish();
                           /* Intent intent = new Intent(FeederMasterActivity.this, NewSurveyFeederActivity.class);
                            startActivity(intent);
                            finish();*/

                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadMapHomeActivityUG33KV.this);
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
                                Toast.makeText(LoadMapHomeActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(LoadMapHomeActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVoUG33KV> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(LoadMapHomeActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }
    }
}



