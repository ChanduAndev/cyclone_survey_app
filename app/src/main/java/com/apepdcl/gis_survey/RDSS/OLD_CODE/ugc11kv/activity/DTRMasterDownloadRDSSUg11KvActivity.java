package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DTRMasterRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DTRMasterResponseMainVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DTRMaster_voRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DTRMasterDownloadRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_clear, btn_next;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private String loadMap,ssName,feederName,feederCode,tappingpoint,maxCount,userName,sessionId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.dtr_master_activity_rdss_ug_11kv);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("DTR Master");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(DTRMasterDownloadRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(DTRMasterDownloadRDSSUg11KvActivity.this);
            preferences = PreferenceManager.getDefaultSharedPreferences(DTRMasterDownloadRDSSUg11KvActivity.this);
          //  sessionId = preferences.getString(MobileConstants.SEC_CODE, "");
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

            gisjhcLayOutFeedrs();
        }catch (Exception e){}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                clearMethod();
                break;
            case R.id.btn_next:
               /* Intent intent=new Intent(DTRMasterDownloadActivity.this,MakeMasterDownloadActivity.class);
                startActivity(intent);
                finish();*/
                dtrMasetrRequestMethod();
                break;
        }
    }

    private void dtrMasetrRequestMethod() {
        if (ConnectivityCheckingRDSSUg11Kv.checkConnection(DTRMasterDownloadRDSSUg11KvActivity.this)) {
            progressDialog.show();
            DTRMasterRequestVoRDSSUg11Kv requestVo = new DTRMasterRequestVoRDSSUg11Kv();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(DTRMasterDownloadRDSSUg11KvActivity.this));
            requestVo.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);
            dtrMasterMehod(requestVo);
        } else {
            CommanUtilits.internetDialog(DTRMasterDownloadRDSSUg11KvActivity.this);
           // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
        }
    }


    public void dtrMasterMehod(DTRMasterRequestVoRDSSUg11Kv requestVo) {
        Log.v("ss===>>", "" + requestVo.toString());
        try {
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<DTRMasterResponseMainVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getDTRMasterData(requestVo);
            responseVoCall.enqueue(new Callback<DTRMasterResponseMainVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<DTRMasterResponseMainVoRDSSUg11Kv> call, Response<DTRMasterResponseMainVoRDSSUg11Kv> response) {
                    if (response.body() != null) {
                        DTRMasterResponseMainVoRDSSUg11Kv responseMain = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(responseMain);

                        Log.v("DATAmin===>>", "" + json);
                        if ("TRUE".equalsIgnoreCase(responseMain.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearDTRMasterData();
                            List<DTRMaster_voRDSSUg11Kv> dataResponse = responseMain.getRESPONSE();
                            for (int i = 0; i < dataResponse.size(); i++) {
                                arcGISDatabaseRDSSUg11Kv.addDTRMasterData(dataResponse.get(i));
                            }
                            progressDialog.dismiss();
                            Intent intent=new Intent(DTRMasterDownloadRDSSUg11KvActivity.this, FeederServiceMasterDownloadRDSSUg11KvActivity.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME",feederName);
                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("MAX_COUNT",maxCount);
                            startActivity(intent);
                            finish();
                        } else {
                            if (responseMain.getAPK() != null) {
                                if (responseMain.getAPK().contains("http")) {
                                       /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(loginDataResponse.getAPK(), LoginActivity.this);
                                        apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DTRMasterDownloadRDSSUg11KvActivity.this);
                                    alertDialogBuilder.setTitle("A New Version Is Available!");
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
                                Toast.makeText(DTRMasterDownloadRDSSUg11KvActivity.this, responseMain.getERR_MSG(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(DTRMasterDownloadRDSSUg11KvActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DTRMasterResponseMainVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(DTRMasterDownloadRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearMethod() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DTRMasterDownloadRDSSUg11KvActivity.this);
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
                                    Intent intent = new Intent(DTRMasterDownloadRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class);
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

    public void gisjhcLayOutFeedrs() {
        try {
            if (ConnectivityCheckingRDSSUg11Kv.checkConnection(DTRMasterDownloadRDSSUg11KvActivity.this)) {
                arcGISDatabaseRDSSUg11Kv.clearCOM_TYPE_SUBTYPE_VER();
                progressDialog.show();

                JsonObject postParam = new JsonObject();
               // postParam.addProperty("FEEDER_ID", "01135102");
                postParam.addProperty("FEEDER_ID", feederCode);

                Log.v("postParam===>>", "" + postParam);

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
                                    JSONArray jArray = jSONObject.getJSONArray("RESPONSE");
                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject childrenObject = jArray.getJSONObject(i);
                                        COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv feederDataVo = new COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv();
                                        feederDataVo.setLAYOUT_NAME(childrenObject.getString("LAYOUT_NAME"));
                                        feederDataVo.setFEEDER_ID(childrenObject.getString("FEEDER_ID"));
                                        feederDataVo.setLAYOUT_ID(childrenObject.getString("LAYOUT_ID"));
                                        arcGISDatabaseRDSSUg11Kv.addCOM_TYPE_SUBTYPE_VER(feederDataVo);
                                    }

                                    progressDialog.dismiss();


                                } else {
                                    Toast.makeText(DTRMasterDownloadRDSSUg11KvActivity.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                                    if ("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())) {
                                        progressDialog.dismiss();
                                    }
                                    progressDialog.dismiss();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(DTRMasterDownloadRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(DTRMasterDownloadRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                CommanUtilits.internetDialog(DTRMasterDownloadRDSSUg11KvActivity.this);
               // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
