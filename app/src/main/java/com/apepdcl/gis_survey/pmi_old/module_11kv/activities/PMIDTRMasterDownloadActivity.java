package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.model.PMIFeederMasterModel;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofit11kv;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitClientInstance11KV;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRMasterRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRMaster_vo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederServiceMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederServiceMasterResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMULTIVOLTAGEMaster_vo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMakeMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMakeMaster_vo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiCktVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiVoltageMasterResponseMainVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMIDTRMasterDownloadActivity extends AppCompatActivity {

    private PMIRetrofit11kv retrofitService;
    private SharedPreferences preferences;
    private PMI11kvArcGISDatabase arcGISDatabase;
    private String ssName, feederName, feederCode, maxCount, userName, sessionId;

    //MULTI CIRCUIT
    String IS_MULTI_CKT;
    String SSID;
    public static ArrayList<PMIMultiCktVO> list11kv;
    public static ArrayList<PMIMultiCktVO> list33kv;
    public static ArrayList<PMIMultiCktVO> listLT;
    public static ArrayList<String> filter33kv;
    public static ArrayList<String> filter11kv;
    public static ArrayList<String> filterLT;

    //FEEDER MASTER
    public static ArrayList<String> feederList33kv;
    public static ArrayList<String> feederList11kv;
    public static ArrayList<String> feederListLT;
    public static ArrayList<PMIFeederMasterModel> feederMasterList11KV;
    public static ArrayList<PMIFeederMasterModel> feederMasterList33KV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmidtrmaster_download);
        getSupportActionBar().hide();

        list11kv = new ArrayList<>();
        list33kv = new ArrayList<>();
        listLT = new ArrayList<>();
        filter33kv = new ArrayList<>();
        filter11kv = new ArrayList<>();
        filterLT = new ArrayList<>();
        feederList33kv = new ArrayList<>();
        feederList11kv = new ArrayList<>();
        feederListLT = new ArrayList<>();
        feederMasterList11KV = new ArrayList<>();
        feederMasterList33KV = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DTR Master");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        arcGISDatabase = new PMI11kvArcGISDatabase(PMIDTRMasterDownloadActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = getIntent();
        if (intent != null) {
            ssName = intent.getStringExtra("SSNAME");
            SSID = intent.getStringExtra("SSID");
            feederName = intent.getStringExtra("FEEDER_NAME");
            feederCode = intent.getStringExtra("FEEDER_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            IS_MULTI_CKT = intent.getStringExtra("MULTI_CKT");
        }

        dtrMasterRequestMethod();
    }
    private void dtrMasterRequestMethod() {
        if (ConnectivityChecking.checkConnection(PMIDTRMasterDownloadActivity.this)) {

            PMIDTRMasterRequestVo requestVo = new PMIDTRMasterRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIDTRMasterDownloadActivity.this));
            requestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);
            dtrMasterResponseMethod(requestVo);
        } else {
            Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
        }
    }
    public void dtrMasterResponseMethod(PMIDTRMasterRequestVo requestVo) {
        Log.v("ss===>>", "" + requestVo.toString());
        try {
            retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMIDTRMasterResponseMainVo> responseVoCall = retrofitService.getDTRMasterData(requestVo);
            responseVoCall.enqueue(new Callback<PMIDTRMasterResponseMainVo>() {
                @Override
                public void onResponse(Call<PMIDTRMasterResponseMainVo> call, Response<PMIDTRMasterResponseMainVo> response) {
                    if (response.body() != null) {
                        PMIDTRMasterResponseMainVo responseMain = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(responseMain);

                        Log.v("DATAmin===>>", "" + json);
                        if ("TRUE".equalsIgnoreCase(responseMain.getSTATUS())) {
                            arcGISDatabase.clearDTRMasterData();
                            List<PMIDTRMaster_vo> dataResponse = responseMain.getRESPONSE();
                            for (int i = 0; i < dataResponse.size(); i++) {
                                arcGISDatabase.addDTRMasterData(dataResponse.get(i));
                            }
                            feederMasterRequestMethod();
                           /* Intent intent=new Intent(DTRMasterDownloadActivity.this,FeederServiceMasterDownloadActivity.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME",feederName);
                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("MAX_COUNT",maxCount);
                            startActivity(intent);
                            finish();*/
                        } else {
                            if (responseMain.getAPK() != null) {
                                if (responseMain.getAPK().contains("http")) {
                                       /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(loginDataResponse.getAPK(), LoginActivity.this);
                                        apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRMasterDownloadActivity.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
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
                                Toast.makeText(PMIDTRMasterDownloadActivity.this, responseMain.getERR_MSG(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {

                        Toast.makeText(PMIDTRMasterDownloadActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMIDTRMasterResponseMainVo> call, Throwable t) {

                    Toast.makeText(PMIDTRMasterDownloadActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void feederMasterRequestMethod() {
        if (ConnectivityChecking.checkConnection(this)) {

            PMIDTRMasterRequestVo requestVo = new PMIDTRMasterRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(this));
            requestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);
            feederMasterResponseMethod(requestVo);
        } else {
            Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
        }
    }

    public void feederMasterResponseMethod(PMIDTRMasterRequestVo requestVo) {
        try {
            retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMIFeederServiceMasterResponseMainVo> responseVoCall = retrofitService.getFeederServiceMasterData(requestVo);
            responseVoCall.enqueue(new Callback<PMIFeederServiceMasterResponseMainVo>() {
                @Override
                public void onResponse(Call<PMIFeederServiceMasterResponseMainVo> call, Response<PMIFeederServiceMasterResponseMainVo> response) {
                    if (response.body() != null) {
                        PMIFeederServiceMasterResponseMainVo responseMain = response.body();
                        if ("TRUE".equalsIgnoreCase(responseMain.getSTATUS())) {
                            arcGISDatabase.clearFeederServiceMasterData();
                            List<PMIFeederServiceMasterResponseVo> dataResponse = responseMain.getRESPONSE();
                            for (int i = 0; i < dataResponse.size(); i++) {
                                arcGISDatabase.addFeederServiceMasterData(dataResponse.get(i));
                            }
                            makeMasterRequestMethod();

                           /* Intent intent=new Intent(FeederServiceMasterDownloadActivity.this,MakeMasterDownloadActivity.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME",feederName);
                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("MAX_COUNT",maxCount);
                            startActivity(intent);
                            finish();*/
                        } else {
                            if (responseMain.getAPK() != null) {
                                if (responseMain.getAPK().contains("http")) {
                                       /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(loginDataResponse.getAPK(), LoginActivity.this);
                                        apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRMasterDownloadActivity.this);
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
                                Toast.makeText(PMIDTRMasterDownloadActivity.this, responseMain.getERR_MSG(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {

                        Toast.makeText(PMIDTRMasterDownloadActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMIFeederServiceMasterResponseMainVo> call, Throwable t) {

                    Toast.makeText(PMIDTRMasterDownloadActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeMasterRequestMethod() {
        if (ConnectivityChecking.checkConnection(this)) {

            PMIDTRMasterRequestVo requestVo = new PMIDTRMasterRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(this));
            requestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            // Gson gson = new Gson();
            // String json = gson.toJson(requestVo);

            // Log.v("DATA===>>", "" + json);
            makeMasterResponseMethod(requestVo);
        } else {
            Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
        }
    }

    public void makeMasterResponseMethod(PMIDTRMasterRequestVo requestVo) {
        try {
            retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMIMakeMasterResponseMainVo> responseVoCall = retrofitService.getMakeListMasterData(requestVo);
            responseVoCall.enqueue(new Callback<PMIMakeMasterResponseMainVo>() {
                @Override
                public void onResponse(Call<PMIMakeMasterResponseMainVo> call, Response<PMIMakeMasterResponseMainVo> response) {
                    if (response.body() != null) {
                        PMIMakeMasterResponseMainVo responseMain = response.body();
                        if ("TRUE".equalsIgnoreCase(responseMain.getSTATUS())) {
                            arcGISDatabase.clearMakeMaster();
                            List<PMIMakeMaster_vo> dataResponse = responseMain.getRESPONSE();
                            for (int i = 0; i < dataResponse.size(); i++) {
                                arcGISDatabase.addMakeMasterData(dataResponse.get(i));
                            }
                            // List< MULTIVOLTAGEMaster_vo > s= arcGISDatabase.getMultiVoltageMasterData();
                            // Toast.makeText(MakeMasterDownloadActivity.this,"size"+s.size() ,Toast.LENGTH_LONG).show();

                            multiVoltageMasterRequestMethod();
                          /*  Intent intent=new Intent(MakeMasterDownloadActivity.this,MultiVoltageMasterDownloadActivity.class);
                            intent.putExtra("SSNAME", ssName);
                            intent.putExtra("FEEDER_NAME",feederName);
                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("MAX_COUNT",maxCount);
                            startActivity(intent);*/
                        } else {
                            if (responseMain.getAPK() != null) {
                                if (responseMain.getAPK().contains("http")) {
                                       /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(loginDataResponse.getAPK(), LoginActivity.this);
                                        apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRMasterDownloadActivity.this);
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
                                Toast.makeText(PMIDTRMasterDownloadActivity.this, responseMain.getERR_MSG(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {

                        Toast.makeText(PMIDTRMasterDownloadActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMIMakeMasterResponseMainVo> call, Throwable t) {
                    Toast.makeText(PMIDTRMasterDownloadActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void multiVoltageMasterRequestMethod() {
        if (ConnectivityChecking.checkConnection(this)) {
            PMIDTRMasterRequestVo requestVo = new PMIDTRMasterRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(this));
            requestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            //Gson gson = new Gson();
            //  String json = gson.toJson(requestVo);

            // Log.v("DATA===>>", "" + json);
            multiVoltageMasterResponseMethod(requestVo);
        } else {
            Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
        }
    }

    public void multiVoltageMasterResponseMethod(PMIDTRMasterRequestVo requestVo) {
        try {
            retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMIMultiVoltageMasterResponseMainVo> responseVoCall = retrofitService.getMultiVoltageMasterData(requestVo);
            responseVoCall.enqueue(new Callback<PMIMultiVoltageMasterResponseMainVo>() {
                @Override
                public void onResponse(Call<PMIMultiVoltageMasterResponseMainVo> call, Response<PMIMultiVoltageMasterResponseMainVo> response) {
                    if (response.body() != null) {
                        PMIMultiVoltageMasterResponseMainVo responseMain = response.body();
                        /*Gson gson = new Gson();
                         String json = gson.toJson(responseMain);

                         Log.v("DATASSS===>>", "" + json);*/
                        if ("TRUE".equalsIgnoreCase(responseMain.getSTATUS())) {
                            arcGISDatabase.clearMultivoltageeMasterData();
                            List<PMIMULTIVOLTAGEMaster_vo> dataResponse = responseMain.getRESPONSE();
                            for (int i = 0; i < dataResponse.size(); i++) {
                                arcGISDatabase.addMultiVoltageMasterData(dataResponse.get(i));
                            }
                            //List< MULTIVOLTAGEMaster_vo > s= arcGISDatabase.getMultiVoltageMasterData();
                            // Toast.makeText(MultiVoltageMasterDownloadActivity.this,"size"+s.size() ,Toast.LENGTH_LONG).show();

                            getMasterFeederData();



                            //getMultiCktData();
                            // goToNextScreen();
                        } else {
                            if (responseMain.getAPK() != null) {
                                if (responseMain.getAPK().contains("http")) {
                                       /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(loginDataResponse.getAPK(), LoginActivity.this);
                                        apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRMasterDownloadActivity.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
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
                                Toast.makeText(PMIDTRMasterDownloadActivity.this, responseMain.getERR_MSG(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        Toast.makeText(PMIDTRMasterDownloadActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMIMultiVoltageMasterResponseMainVo> call, Throwable t) {
                    Toast.makeText(PMIDTRMasterDownloadActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMasterFeederData() {
        List<LoginDataResponse> loginDataResponses = new PMIArcGISDatabase(PMIDTRMasterDownloadActivity.this).getUserMasterData();

        Gson gson = new Gson();
        String json = gson.toJson(loginDataResponses);
        Log.v("DATA===>>", "" + json);

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getFeederMaster(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            feederList33kv.clear();
                            feederList11kv.clear();
                            feederListLT.clear();
                            feederMasterList11KV.clear();
                            feederMasterList33KV.clear();
                            JsonObject responseObj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            // arcGISDatabase.clearMultiCktData();
                            JsonArray el_kv_array = responseObj.get("11KV").getAsJsonArray();
                            for (int i = 0; i < el_kv_array.size(); i++) {
                                JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                feederList11kv.add(data.get("FEEDER_ID").getAsString() + "~" +data.get("FEEDER_NAME").getAsString());
                                PMIFeederMasterModel model = new PMIFeederMasterModel();
                                model.setSEC_ID(data.get("SEC_ID").getAsString());
                                model.setFEEDER_ID(data.get("FEEDER_ID").getAsString());
                                model.setFEEDER_NAME(data.get("FEEDER_NAME").getAsString());
                                feederMasterList11KV.add(model);
                            }

                            JsonArray tt_kv_array = responseObj.get("33KV").getAsJsonArray();
                            for (int i = 0; i < tt_kv_array.size(); i++) {
                                JsonObject data = tt_kv_array.get(i).getAsJsonObject();
                                feederList33kv.add(data.get("FEEDER_CODE_33KV").getAsString() + "~" +data.get("FEEDER_NAME_33KV").getAsString());
                                PMIFeederMasterModel model = new PMIFeederMasterModel();
                                model.setSEC_ID(data.get("SEC_OWNER").getAsString());
                                model.setFEEDER_ID(data.get("FEEDER_CODE_33KV").getAsString());
                                model.setFEEDER_NAME(data.get("FEEDER_NAME_33KV").getAsString());
                                feederMasterList33KV.add(model);
                            }

                            JsonArray lt_kv_array = responseObj.get("LT").getAsJsonArray();
                            for (int i = 0; i < lt_kv_array.size(); i++) {
                                JsonObject data = lt_kv_array.get(i).getAsJsonObject();
                              /*  String dtr_location = data.get("DTR_LOCATION").isJsonNull() ? "" :  data.get("DTR_LOCATION").getAsString();
                                feederListLT.add(data.get("DTR_STRUCTURE_CODE").getAsString() + "~" +dtr_location);*/

                                String s_code = data.get("DTR_STRUCTURE_CODE").isJsonNull() ? "" : data.get("DTR_STRUCTURE_CODE").getAsString();
                                String s_loc = data.get("DTR_LOCATION").isJsonNull() ? "" : data.get("DTR_LOCATION").getAsString();
                                feederListLT.add(s_code + "~" +s_loc);
                            }

                            if (IS_MULTI_CKT.equalsIgnoreCase("Yes")){
                                getMultiCktData();
                            }else {
                                goToNextScreen();
                            }





                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(PMIDTRMasterDownloadActivity.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIDTRMasterDownloadActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PMIDTRMasterDownloadActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMultiCktData() {



        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("SSID", SSID);
        dtrDetailsReqestVo.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PMIDTRMasterDownloadActivity.this));
        dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.APP_VERSION);


        retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("MULTRESPONSE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            list11kv.clear();
                            list33kv.clear();
                            JsonObject responseObj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            // arcGISDatabase.clearMultiCktData();
                            JsonArray el_kv_array = responseObj.get("11KV").getAsJsonArray();
                            for (int i = 0; i < el_kv_array.size(); i++) {
                                JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                PMIMultiCktVO multiCktVO = new PMIMultiCktVO();
                                multiCktVO.setVOLTAGE_LEVEL(data.get("VOLTAGE_LEVEL").getAsString());
                                multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                multiCktVO.setMC_FEEDER_NAME(data.get("FEEDER_NAME").getAsString());
                                multiCktVO.setMC_POLE_TYPE(data.get("POLE_TYPE").getAsString());
                                multiCktVO.setMC_POLE_HEIGHT(data.get("POLE_HEIGHT").getAsString());
                                multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                multiCktVO.setGLON(data.get("GLONG").getAsString());
                                multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                multiCktVO.setSHOW_NAME(data.get("FEEDER_ID").getAsString() +"~"+data.get("FEEDER_NAME").getAsString());
                                list11kv.add(multiCktVO);
                            }

                            JsonArray tt_kv_array = responseObj.get("33KV").getAsJsonArray();
                            for (int i = 0; i < tt_kv_array.size(); i++) {
                                JsonObject data = tt_kv_array.get(i).getAsJsonObject();
                                PMIMultiCktVO multiCktVO = new PMIMultiCktVO();
                                multiCktVO.setVOLTAGE_LEVEL(data.get("VOLTAGE_LEVEL").getAsString());
                                multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                multiCktVO.setMC_FEEDER_NAME(data.get("FEEDER_NAME_33KV").getAsString());
                                multiCktVO.setMC_POLE_TYPE(data.get("POLE_TYPE").getAsString());
                                multiCktVO.setMC_POLE_HEIGHT(data.get("POLE_HEIGHT").getAsString());
                                multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                multiCktVO.setGLON(data.get("GLONG").getAsString());
                                multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                multiCktVO.setSHOW_NAME(data.get("FEEDER_ID").getAsString() +"~"+data.get("FEEDER_NAME_33KV").getAsString());
                                list33kv.add(multiCktVO);
                            }

                            JsonArray lt_kv_array = responseObj.get("LT").getAsJsonArray();
                            for (int i = 0; i < lt_kv_array.size(); i++) {
                                JsonObject data = lt_kv_array.get(i).getAsJsonObject();
                                PMIMultiCktVO multiCktVO = new PMIMultiCktVO();
                                multiCktVO.setVOLTAGE_LEVEL("LT");
                                multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                multiCktVO.setMC_FEEDER_NAME(data.get("LT_CKT_CODE").getAsString());
                                multiCktVO.setMC_POLE_TYPE(data.get("POLE_TYPE").getAsString());
                                multiCktVO.setMC_POLE_HEIGHT(data.get("POLE_HEIGHT").getAsString());
                                multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                multiCktVO.setGLON(data.get("GLONG").getAsString());
                                multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                listLT.add(multiCktVO);
                            }

                            filter33KvFeeders();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        goToNextScreen();
                        //  Toast.makeText(this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIDTRMasterDownloadActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PMIDTRMasterDownloadActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterLTFeeders() {
        // List<String> originalListIdFeeder1 = new ArrayList<>();
        List<String> originalListNameFeeder1 = new ArrayList<>();

        for(int i =0; i < listLT.size(); i++) {
            //originalListIdFeeder1.add(listLT.get(i).getMC_FEEDER_ID());
            originalListNameFeeder1.add(listLT.get(i).getMC_FEEDER_NAME());
        }
        /*Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(originalListIdFeeder1);
        originalListIdFeeder1.clear();
        originalListIdFeeder1.addAll(set_feeder_id_1);*/

        Set<String> set_feeder_name_1 = new LinkedHashSet<>();
        set_feeder_name_1.addAll(originalListNameFeeder1);
        originalListNameFeeder1.clear();
        originalListNameFeeder1.addAll(set_feeder_name_1);

        for (int i = 0; i < originalListNameFeeder1.size(); i++) {
            filterLT.add(originalListNameFeeder1.get(i));
        }
        goToNextScreen();
    }

    private void goToNextScreen() {
        Intent intent=new Intent(PMIDTRMasterDownloadActivity.this,PMIPoleSurveyActivity.class);
        intent.putExtra("SSNAME", ssName);
        intent.putExtra("FEEDER_NAME",feederName);
        intent.putExtra("FEEDER_CODE",feederCode);
        intent.putExtra("MAX_COUNT",maxCount);
        startActivity(intent);
    }

    private void filter11KvFeeders() {
        /*List<String> originalListIdFeeder1 = new ArrayList<>();
        List<String> originalListNameFeeder1 = new ArrayList<>();

        for(int i =0; i < list11kv.size(); i++) {
            originalListIdFeeder1.add(list11kv.get(i).getMC_FEEDER_ID());
            originalListNameFeeder1.add(list11kv.get(i).getMC_FEEDER_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(originalListIdFeeder1);
        originalListIdFeeder1.clear();
        originalListIdFeeder1.addAll(set_feeder_id_1);

        Set<String> set_feeder_name_1 = new LinkedHashSet<>();
        set_feeder_name_1.addAll(originalListNameFeeder1);
        originalListNameFeeder1.clear();
        originalListNameFeeder1.addAll(set_feeder_name_1);

        for (int i = 0; i < originalListIdFeeder1.size(); i++) {
            filter11kv.add(originalListIdFeeder1.get(i) + "~" + originalListNameFeeder1.get(i));
        }*/

        List<String> combinedList = new ArrayList<>();
        for(int i =0; i < list11kv.size(); i++) {
            combinedList.add(list11kv.get(i).getSHOW_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(combinedList);
        combinedList.clear();
        combinedList.addAll(set_feeder_id_1);

        if (!combinedList.isEmpty()){
            for (int i = 0; i < combinedList.size(); i++) {
                filter11kv.add(combinedList.get(i));
            }
        }


        filterLTFeeders();
    }

    private void filter33KvFeeders() {

        /*List<String> originalListIdFeeder1 = new ArrayList<>();
        List<String> originalListNameFeeder1 = new ArrayList<>();

        for(int i =0; i < list33kv.size(); i++) {
            originalListIdFeeder1.add(list33kv.get(i).getMC_FEEDER_ID());
            originalListNameFeeder1.add(list33kv.get(i).getMC_FEEDER_NAME());
        }

        System.out.println("SIZE 33KV WHOLE : "+list33kv.size());
        System.out.println("SIZE NAME WHOLE : "+originalListNameFeeder1.size());
        System.out.println("SIZE ID WHOLE : "+originalListIdFeeder1.size());

        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(originalListIdFeeder1);
        originalListIdFeeder1.clear();
        originalListIdFeeder1.addAll(set_feeder_id_1);

        Set<String> set_feeder_name_1 = new LinkedHashSet<>();
        set_feeder_name_1.addAll(originalListNameFeeder1);
        originalListNameFeeder1.clear();
        originalListNameFeeder1.addAll(set_feeder_name_1);

        if (!originalListIdFeeder1.isEmpty()){
            for (int i = 0; i < originalListIdFeeder1.size(); i++) {
                filter33kv.add(originalListIdFeeder1.get(i) + "~" + originalListNameFeeder1.get(i));
            }
        }*/

        List<String> combinedList = new ArrayList<>();
        for(int i =0; i < list33kv.size(); i++) {
            combinedList.add(list33kv.get(i).getSHOW_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(combinedList);
        combinedList.clear();
        combinedList.addAll(set_feeder_id_1);

        if (!combinedList.isEmpty()){
            for (int i = 0; i < combinedList.size(); i++) {
                filter33kv.add(combinedList.get(i));
            }
        }


        filter11KvFeeders();
    }






}