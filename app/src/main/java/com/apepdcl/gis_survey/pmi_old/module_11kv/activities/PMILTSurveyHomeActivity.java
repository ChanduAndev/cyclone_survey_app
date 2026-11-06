package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitService;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRDetailsResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRSurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyDataVo;
import com.apepdcl.gis_survey.runtimeperimissions.AppData;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.runtimeperimissions.Utilites;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMILTSurveyHomeActivity extends AppCompatActivity  implements View.OnClickListener{
    private Button btn_ltsurvey,btn_incremental_ltsurvey,btn_11kv_survey;
    private List<PMISurveyDataVo> surveyDataVosList;
    private List<PMIDTRDetailsResponseVo> dtrDetailsResponseVoList=null;
    private List<PMIDTRSurveyVo> dtrSurveyVoList=null;
    private List<LoginDataResponse> loginDataResponses;
    private PMIArcGISDatabase arcGISDatabase;
    private PMIRetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String userName;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmiltsurvey_home);
        btn_ltsurvey=findViewById(R.id.btn_ltsurvey);
        btn_11kv_survey=findViewById(R.id.btn_11kv_survey);
        btn_incremental_ltsurvey=findViewById(R.id.btn_incremental_ltsurvey);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("11KV/LT GIS Survey ");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PMILTSurveyHomeActivity.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        preferences= PreferenceManager.getDefaultSharedPreferences(PMILTSurveyHomeActivity.this);
        userName=preferences.getString(MobileConstants.USERNAME,"");
        btn_ltsurvey.setOnClickListener(this);
        btn_11kv_survey.setOnClickListener(this);
        btn_incremental_ltsurvey.setOnClickListener(this);
        arcGISDatabase=new PMIArcGISDatabase(this);
        loginDataResponses=arcGISDatabase.getUserMasterData();
        surveyDataVosList=arcGISDatabase.getSurveyData("SELECT * FROM SURVEY_TABLE WHERE SURVEY_FLAG='N'");
        dtrDetailsResponseVoList=new ArrayList<>();
        dtrSurveyVoList=new ArrayList<>();
        dtrDetailsResponseVoList=arcGISDatabase.getDTRDetailsData();
        dtrSurveyVoList=arcGISDatabase.getDTRSurveyData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_ltsurvey:
                if(ConnectivityChecking.checkConnection(PMILTSurveyHomeActivity.this)){
                    if(surveyDataVosList.size()>0 ||dtrSurveyVoList.size()>0) {
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
                        getGisltckt();

                    }

                }else {
                    Toast.makeText(getApplicationContext(), MobileConstants.INTERNET , Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_11kv_survey:

                if (ConnectivityChecking.checkConnection(PMILTSurveyHomeActivity.this)) {
                    Intent intent = new Intent(PMILTSurveyHomeActivity.this, PMIHome11KV.class);
                    startActivity(intent);
                    /*if (Build.VERSION.SDK_INT >= 23) {
                        if (AppData.getInstance().isPermission()) {
                            Intent intent = new Intent(PMILTSurveyHomeActivity.this, PMIHome11KV.class);
                            startActivity(intent);

                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                Utilites.isStoragePermissionGranted(PMILTSurveyHomeActivity.this);
                            }
                        }
                    } else {
                        Intent intent = new Intent(PMILTSurveyHomeActivity.this, PMIHome11KV.class);
                        startActivity(intent);
                    }*/
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_incremental_ltsurvey:

                getIncrementalGisltckt();
                break;

            default:

                break;
        }

    }

    public void getGisltckt(){
        progressDialog.show();
        PMILTCktsReqVo ltCktsReqVo=new PMILTCktsReqVo();
        ltCktsReqVo.setASSIGNED_TO(userName);
        // ltCktsReqVo.setASSIGNED_TO("1078667");
        ltCktsReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMILTSurveyHomeActivity.this));
        ltCktsReqVo.setVERSION(MobileConstants.APP_VERSION);
        ltCktsReqVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());

        Gson gson=new Gson();
        String str=gson.toJson(ltCktsReqVo);
        Log.v("DATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<PMILTCktsResponseVo> responseVoCall = retrofitService.getGisltckt(ltCktsReqVo);
        responseVoCall.enqueue(new Callback<PMILTCktsResponseVo>() {
            @Override
            public void onResponse(Call<PMILTCktsResponseVo> call, Response<PMILTCktsResponseVo> response) {
                progressDialog.dismiss();
                if(response.body() !=null) {
                    PMILTCktsResponseVo ltCktsResponseVo = response.body();
                    Gson gson=new Gson();
                    String str=gson.toJson(ltCktsResponseVo);
                    Log.v("DATA===>>", "" + str);
                    if(MobileConstants.TRUE.equalsIgnoreCase(ltCktsResponseVo.getSTATUS())){
                        arcGISDatabase.clearDTRData();
                        List<PMILTCktsResponseDataVo> ltSurveyRequestDataVosList=ltCktsResponseVo.getRESPONSE();
                        for (PMILTCktsResponseDataVo surveyRequestDataVo:ltSurveyRequestDataVosList){
                            arcGISDatabase.addDTRData(surveyRequestDataVo);
                        }
                        Intent intent=new Intent(PMILTSurveyHomeActivity.this, PMIDTRSelectActivity.class);
                        startActivity(intent);
                        finish();

                    }else {

                        if (ltCktsResponseVo.getAPK() != null) {
                            if (ltCktsResponseVo.getAPK().contains("http")) {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMILTSurveyHomeActivity.this);

                                alertDialogBuilder.setTitle("A new Version is available!");
                                //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                alertDialogBuilder

                                        .setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                                                "Please Download latest app,<a href=\"https://gisappstore.apeasternpower.com/GISSurveyLT.apk\"></a>"))
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
                            Toast.makeText(PMILTSurveyHomeActivity.this, ltCktsResponseVo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                        }
                        //Toast.makeText(LTSurveyHomeActivity.this,ltCktsResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(PMILTSurveyHomeActivity.this,"Server Busy Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMILTCktsResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMILTSurveyHomeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });
    }

    public void getIncrementalGisltckt(){
        progressDialog.show();
        PMILTCktsReqVo ltCktsReqVo=new PMILTCktsReqVo();
        //ltCktsReqVo.setASSIGNED_TO(userName);
        ltCktsReqVo.setASSIGNED_TO("1071749");
        ltCktsReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMILTSurveyHomeActivity.this));
        ltCktsReqVo.setVERSION(MobileConstants.APP_VERSION);
        //ltCktsReqVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());
        ltCktsReqVo.setSECTION_ID("1000-25");

        Gson gson=new Gson();
        String str=gson.toJson(ltCktsReqVo);
        Log.v("DATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getIncrementalGisltckt(ltCktsReqVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if(response.body() !=null && response.isSuccessful()) {
                    JsonObject ltCktsResponseVo = response.body();
                    if("TRUE".equalsIgnoreCase(ltCktsResponseVo.get("STATUS").getAsString())){
                        arcGISDatabase.clearIncrementalDTRData();

                        JsonArray jsonArray=ltCktsResponseVo.get("RESPONSE").getAsJsonArray();
                        for(int i=0;i<jsonArray.size();i++){
                            JsonObject jsonObject=jsonArray.get(i).getAsJsonObject();
                            PMILTCktsDataVo ltCktsDataVo=new PMILTCktsDataVo();
                            ltCktsDataVo.setDTR_CODE(jsonObject.get("DTR_CODE").getAsString());
                            ltCktsDataVo.setDTR_LOCATION(jsonObject.get("DTR_LOCATION").getAsString());
                            ltCktsDataVo.setDTR_PHASE(jsonObject.get("DTR_PHASE").getAsString());
                            ltCktsDataVo.setLT_CKTCODE(jsonObject.get("LT_CKT_CODE").getAsString());
                            ltCktsDataVo.setFEEDER_ID(jsonObject.get("FEEDER_ID").getAsString());
                            ltCktsDataVo.setSTATUS(jsonObject.get("STATUS").getAsString());
                            ltCktsDataVo.setWO_ID(jsonObject.get("WO_ID").getAsString());
                            ltCktsDataVo.setTAPPING_POLE(jsonObject.get("TAPPING_POLE_NO_LT").getAsString());
                            arcGISDatabase.addDIncrementalDTRData(ltCktsDataVo);
                        }
                        Intent intent=new Intent(PMILTSurveyHomeActivity.this,PMIIncrementalDTRSelectActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(PMILTSurveyHomeActivity.this,ltCktsResponseVo.get("ERR_MSG").getAsString(),Toast.LENGTH_SHORT).show();
                    }
                   /* if(MobileConstants.TRUE.equalsIgnoreCase(ltCktsResponseVo.getSTATUS())){
                        arcGISDatabase.clearIncrementalDTRData();
                       *//* List<LTCktsResponseDataVo> ltSurveyRequestDataVosList=ltCktsResponseVo.getRESPONSE();
                        for (LTCktsResponseDataVo surveyRequestDataVo:ltSurveyRequestDataVosList){
                            arcGISDatabase.addDIncrementalDTRData(surveyRequestDataVo);
                        }*//*
                        Intent intent=new Intent(LTSurveyHomeActivity.this,IncrementalDTRSelectActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(LTSurveyHomeActivity.this,ltCktsResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }*/


                }else {
                    Toast.makeText(PMILTSurveyHomeActivity.this,"Server Busy Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMILTSurveyHomeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}