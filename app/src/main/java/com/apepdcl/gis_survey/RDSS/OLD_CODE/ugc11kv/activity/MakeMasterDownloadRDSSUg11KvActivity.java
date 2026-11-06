package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DTRMasterRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.MakeMasterResponseMainVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.MakeMaster_voRDSSUg11Kv;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeMasterDownloadRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_clear, btn_next;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private String ssName,feederName,feederCode,maxCount;
    private CheckBox acheck_dtr;
    private CheckBox acheck_feeder_service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.make1_master_activity_rdss_ug_11kv);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("DTR Make Master");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(MakeMasterDownloadRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();
                }
            });
            acheck_dtr = (CheckBox) findViewById(R.id.acheck_dtr);
            acheck_dtr.setChecked(true);
            acheck_feeder_service = (CheckBox) findViewById(R.id.acheck_feeder_service);
            acheck_feeder_service.setChecked(true);
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(MakeMasterDownloadRDSSUg11KvActivity.this);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            btn_clear = (Button) findViewById(R.id.btn_clear);
            btn_next = (Button) findViewById(R.id.btn_next);
            btn_next.setOnClickListener(this);
            btn_clear.setOnClickListener(this);
            Intent intent = getIntent();
            if (intent != null) {
                ssName = intent.getStringExtra("SSNAME");
                feederName = intent.getStringExtra("FEEDER_NAME");
                feederCode = intent.getStringExtra("FEEDER_CODE");
                maxCount = intent.getStringExtra("MAX_COUNT");
            }
        }catch (Exception e){}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                clearMethod();
                break;
            case R.id.btn_next:
                makeMasetrRequestMethod();
                break;
        }
    }

    private void makeMasetrRequestMethod() {
        if (ConnectivityCheckingRDSSUg11Kv.checkConnection(MakeMasterDownloadRDSSUg11KvActivity.this)) {
            progressDialog.show();
            DTRMasterRequestVoRDSSUg11Kv requestVo = new DTRMasterRequestVoRDSSUg11Kv();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(MakeMasterDownloadRDSSUg11KvActivity.this));
            requestVo.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
           // Gson gson = new Gson();
           // String json = gson.toJson(requestVo);

           // Log.v("DATA===>>", "" + json);
            makeMasterMehod(requestVo);
        } else {
            Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
        }
    }


    public void makeMasterMehod(DTRMasterRequestVoRDSSUg11Kv requestVo) {
        try {
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<MakeMasterResponseMainVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getMakeListMasterData(requestVo);
            responseVoCall.enqueue(new Callback<MakeMasterResponseMainVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<MakeMasterResponseMainVoRDSSUg11Kv> call, Response<MakeMasterResponseMainVoRDSSUg11Kv> response) {
                    if (response.body() != null) {
                        MakeMasterResponseMainVoRDSSUg11Kv responseMain = response.body();
                        if ("TRUE".equalsIgnoreCase(responseMain.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearMakeMaster();
                            List<MakeMaster_voRDSSUg11Kv> dataResponse = responseMain.getRESPONSE();
                            for (int i = 0; i < dataResponse.size(); i++) {
                                arcGISDatabaseRDSSUg11Kv.addMakeMasterData(dataResponse.get(i));
                            }
                           // List< MULTIVOLTAGEMaster_vo > s= arcGISDatabase.getMultiVoltageMasterData();
                           // Toast.makeText(MakeMasterDownloadActivity.this,"size"+s.size() ,Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                            Intent intent=new Intent(MakeMasterDownloadRDSSUg11KvActivity.this, MultiVoltageMasterDownloadRDSSUg11KvActivity.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME",feederName);
                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("MAX_COUNT",maxCount);
                            startActivity(intent);
                        } else {
                            if (responseMain.getAPK() != null) {
                                if (responseMain.getAPK().contains("http")) {
                                       /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(loginDataResponse.getAPK(), LoginActivity.this);
                                        apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MakeMasterDownloadRDSSUg11KvActivity.this);
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
                                Toast.makeText(MakeMasterDownloadRDSSUg11KvActivity.this, responseMain.getERR_MSG(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MakeMasterDownloadRDSSUg11KvActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MakeMasterResponseMainVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MakeMasterDownloadRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearMethod() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MakeMasterDownloadRDSSUg11KvActivity.this);
            alertDialogBuilder.setTitle("Clear!");
            alertDialogBuilder
                    .setMessage("Are you sure you want clear ..? ")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    arcGISDatabaseRDSSUg11Kv.clearFeederServiceMasterData();
                                    arcGISDatabaseRDSSUg11Kv.clearDTRMasterData();
                                    arcGISDatabaseRDSSUg11Kv.clearMultivoltageeMasterData();
                                    arcGISDatabaseRDSSUg11Kv.clearMakeMaster();
                                    Intent intent = new Intent(MakeMasterDownloadRDSSUg11KvActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
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
}
