package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.incremetal;

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

import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys.FeederMasterActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.utilits.CommanUtilits;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.DTRMasterRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.MakeMasterResponseMainVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.MakeMaster_vo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeMasterDownloadIncremntalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_clear, btn_next;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private ArcGISDatabase arcGISDatabase;
    private String ssName,feederName,feederCode,maxCount,work_order;
    private CheckBox acheck_dtr;
    private CheckBox acheck_feeder_service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.make1_master_activity_incremtal_11kv_oh_rdss);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("DTR Make Master");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(MakeMasterDownloadIncremntalActivity.this, FeederMasterActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();
                }
            });
            acheck_dtr = (CheckBox) findViewById(R.id.acheck_dtr);
            acheck_dtr.setChecked(true);
            acheck_feeder_service = (CheckBox) findViewById(R.id.acheck_feeder_service);
            acheck_feeder_service.setChecked(true);
            arcGISDatabase = new ArcGISDatabase(MakeMasterDownloadIncremntalActivity.this);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            progressDialog = CommanUtilits.progressDialog(this);
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
                work_order = intent.getStringExtra("WORKORDER");
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
        if (ConnectivityChecking.checkConnection(MakeMasterDownloadIncremntalActivity.this)) {
            progressDialog.show();
            DTRMasterRequestVo requestVo = new DTRMasterRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(MakeMasterDownloadIncremntalActivity.this));
            requestVo.setVERSION(MobileConstants.APP_VERSION);
           // Gson gson = new Gson();
           // String json = gson.toJson(requestVo);

           // Log.v("DATA===>>", "" + json);
            makeMasterMehod(requestVo);
        } else {
            Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
        }
    }


    public void makeMasterMehod(DTRMasterRequestVo requestVo) {
        try {
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<MakeMasterResponseMainVo> responseVoCall = retrofitService.getMakeListMasterData(requestVo);
            responseVoCall.enqueue(new Callback<MakeMasterResponseMainVo>() {
                @Override
                public void onResponse(Call<MakeMasterResponseMainVo> call, Response<MakeMasterResponseMainVo> response) {
                    if (response.body() != null) {
                        MakeMasterResponseMainVo responseMain = response.body();
                        if ("TRUE".equalsIgnoreCase(responseMain.getSTATUS())) {
                            arcGISDatabase.clearIncrementalMakeMaster();
                            List<MakeMaster_vo> dataResponse = responseMain.getRESPONSE();
                            for (int i = 0; i < dataResponse.size(); i++) {
                                arcGISDatabase.addIncrementalMakeMasterData(dataResponse.get(i));
                            }
                           // List< MULTIVOLTAGEMaster_vo > s= arcGISDatabase.getMultiVoltageMasterData();
                           // Toast.makeText(MakeMasterDownloadActivity.this,"size"+s.size() ,Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                            Intent intent=new Intent(MakeMasterDownloadIncremntalActivity.this,MultiVoltageMasterDownloadIncrementalActivity.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME",feederName);
                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("MAX_COUNT",maxCount);
                            intent.putExtra("WORKORDER", work_order);
                            startActivity(intent);
                            finish();
                        } else {
                            if (responseMain.getAPK() != null) {
                                if (responseMain.getAPK().contains("http")) {
                                       /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(loginDataResponse.getAPK(), LoginActivity.this);
                                        apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MakeMasterDownloadIncremntalActivity.this);
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
                                Toast.makeText(MakeMasterDownloadIncremntalActivity.this, responseMain.getERR_MSG(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MakeMasterDownloadIncremntalActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MakeMasterResponseMainVo> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MakeMasterDownloadIncremntalActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearMethod() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MakeMasterDownloadIncremntalActivity.this);
            alertDialogBuilder.setTitle("Clear!");
            alertDialogBuilder
                    .setMessage("Are you sure you want clear ..? ")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    arcGISDatabase.clearFeederServiceMasterData();
                                    arcGISDatabase.clearDTRMasterData();
                                    arcGISDatabase.clearMultivoltageeMasterData();
                                    arcGISDatabase.clearMakeMaster();
                                    Intent intent = new Intent(MakeMasterDownloadIncremntalActivity.this, LoginActivity.class);
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
