package com.apepdcl.gis_survey.module_11KV;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LTSurveyHomeActivity;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitClientInstance;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;
import com.apepdcl.gis_survey.module_11KV.models.FeederDataVo;
import com.apepdcl.gis_survey.module_11KV.models.FeederMasterRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.FeederMasterResponseDataVo;
import com.apepdcl.gis_survey.module_11KV.models.FeederMasterResponseVo;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.utilits.CommanUtilits;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home11KV extends AppCompatActivity {

    private List<SurveyVo> surveyVoList,imageList,surveyVoEditLatLan, surveyVoListSourcePole, surveyVoListEditPoleAttributes;
    private ArcGISDatabase arcGISDatabase;
    private SharedPreferences preferences;
    private String userName,sessionId;
    private RetrofitService retrofitService;
    private int feederMasterflag = 0;
    int imageCount=0;
    TextView no_data;
    GifImageView loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home11_kv);
        getSupportActionBar().hide();
        no_data = findViewById(R.id.no_data);
        loader = findViewById(R.id.loader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("11KV Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(Home11KV.this, HomeActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });

        preferences= PreferenceManager.getDefaultSharedPreferences(Home11KV.this);
        sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
        userName = preferences.getString(MobileConstants.USERNAME, "");
        arcGISDatabase = new ArcGISDatabase(this);
        surveyVoEditLatLan = arcGISDatabase.getEditLatLanData();
        surveyVoListSourcePole = arcGISDatabase.getEditSourcePoleSurveyData();
        surveyVoListEditPoleAttributes = arcGISDatabase.getEditPoleSurveyData();
        surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
        imageList = arcGISDatabase.getImageData("");

       // System.out.println("IMAGE SIZE : "+imageList.size());

        for (int i = 0; i < imageList.size(); i++) {
            if (!imageList.get(i).getIMAGE_1().equals("N")){
                imageCount++;
            }
            if (!imageList.get(i).getIMAGE_2().equals("N")){
                imageCount++;
            }
            if (!imageList.get(i).getIMAGE_3().equals("N")){
                imageCount++;
            }
        }

        try {
            if (surveyVoList.size() > 0 || imageCount > 0 || surveyVoEditLatLan.size()>0 || surveyVoListSourcePole.size()>0 || surveyVoListEditPoleAttributes.size()>0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Please transmit the data before  survey start");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                })

                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();

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

    public void getFeederMasterData() {
        try {
            FeederMasterRequestVo feederMasterRequestVo = new FeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(this));
            feederMasterRequestVo.setSESSION_ID(sessionId);
            feederMasterRequestVo.setUSER_ID(preferences.getString(MobileConstants.USERNAME, ""));
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
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
                                String allow_survey=vo.getALLOW_SURVEY();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                FeederDataVo feederDataVo = new FeederDataVo();
                                feederDataVo.setEHT_SS_CODE(ssCode);
                                feederDataVo.setEHT_SS_NAME(ssName);
                                feederDataVo.setFEEDER_CODE_33KV(feederCode);
                                feederDataVo.setFEEDER_NAME_33KV(feedERName);
                                feederDataVo.setALLOW_SURVEY(allow_survey);

                                feederDataVo.setWO_ID(vo.getWO_ID());
                                feederDataVo.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
                                arcGISDatabase.addFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            if (feederMasterflag == 1) {
                                feederMasterflag = 0;
                                Intent intent = new Intent(Home11KV.this, LTSurveyHomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Intent intent = new Intent(Home11KV.this, SelectFeeder.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home11KV.this);

                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                    alertDialogBuilder

                                            .setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                                                    "Please Download latest Mobile App Apk"))
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

                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                }
                            } else {
                                Toast.makeText(Home11KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                                loader.setVisibility(View.GONE);
                                no_data.setVisibility(View.VISIBLE);

                            }

                        }
                    } else {

                        Toast.makeText(Home11KV.this, "Server Busy", Toast.LENGTH_LONG).show();
                        loader.setVisibility(View.GONE);
                       // no_data.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVo> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(Home11KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}