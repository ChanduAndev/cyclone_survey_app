package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIFeederMasterRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofit11kv;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederMasterResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederMasterResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMIFeederDataVo;
import com.apepdcl.gis_survey.utilits.CommanUtilits;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMIReport11KV extends AppCompatActivity {
    private List<PMISurveyVo> surveyVoList,imageList;
    private PMI11kvArcGISDatabase arcGISDatabase;
    private SharedPreferences preferences;
    private String userName,sessionId;
    private PMIRetrofit11kv retrofitService;
    private int feederMasterflag = 0;
    int imageCount=0;
    TextView no_data;
    GifImageView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report11_kv);
        getSupportActionBar().hide();
        no_data = findViewById(R.id.no_data);
        loader = findViewById(R.id.loader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Defects Noticed Under PMI-11KV");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PMIReport11KV.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        preferences= PreferenceManager.getDefaultSharedPreferences(PMIReport11KV.this);
        sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
        userName = preferences.getString(MobileConstants.USERNAME, "");
        arcGISDatabase = new PMI11kvArcGISDatabase(this);

        getFeederMasterData();
    }
    public void getFeederMasterData() {
        try {
            PMIFeederMasterRequestVo feederMasterRequestVo = new PMIFeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(this));
            feederMasterRequestVo.setSESSION_ID(sessionId);
            feederMasterRequestVo.setUSER_ID(preferences.getString(MobileConstants.USERNAME, ""));
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            /*Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);
            Log.v("DATA===>>", "" + json);*/
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMIFeederMasterResponseVo> responseVoCall = retrofitService.getDefectFeederMasterData(feederMasterRequestVo);
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

                                feederDataVo.setWOID(vo.getWO_ID());
                                feederDataVo.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
                                arcGISDatabase.addFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            if (feederMasterflag == 1) {
                                feederMasterflag = 0;
                                Intent intent = new Intent(PMIReport11KV.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Intent intent = new Intent(PMIReport11KV.this, PMISelectFeederReport.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIReport11KV.this);

                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                    alertDialogBuilder

                                            .setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                                                    "Please Download latest Mobile App Apk form OMS in GIS Tab,"))
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
                                Toast.makeText(PMIReport11KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                                loader.setVisibility(View.GONE);
                                no_data.setVisibility(View.VISIBLE);

                            }

                        }
                    } else {

                        Toast.makeText(PMIReport11KV.this, "Server Busy", Toast.LENGTH_LONG).show();
                        loader.setVisibility(View.GONE);
                        // no_data.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<PMIFeederMasterResponseVo> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PMIReport11KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}