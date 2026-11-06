package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.incremental;

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
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.FeederMasterActivityUG33KV;

import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ApkDowanloadInstalUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalLastSurveyRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyResponseDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyResponseSSDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SSMasterVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncrementalTopTenActivityUG33KV extends AppCompatActivity{
    private Button btn_next;
    //private Spinner sp_ssname,sp_feedername,sp_workorder;
    private String ssName,feederName,feederId,maxCount,sessionId,feederCode,ssCode,subName,feedeNameList,work_order,feerdeCode,feeder_Name;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<String> feederNameList;
    private List<String> workOrderList;
    private List<String> ehtName;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private List<IncrementalFeederDataVoUG33KV> incrementalFeederDataVoUG33KVList =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incrementaltopten_ugc_33kv);
        //sp_ssname=findViewById(R.id.sp_ssname);
       // sp_feedername=findViewById(R.id.sp_feedername);
       // sp_workorder=findViewById(R.id.sp_workorder);
        getSupportActionBar().setTitle("33Kv IncrementalLineSurvey");
      //  sp_ssname.setOnItemSelectedListener(this);
       // sp_feedername.setOnItemSelectedListener(this);
       // sp_workorder.setOnItemSelectedListener(this);
        arcGISDatabase=new ArcGISDatabaseUG33KV(this);
        incrementalFeederDataVoUG33KVList =new ArrayList<>();
        incrementalFeederDataVoUG33KVList =arcGISDatabase.getIncrementalFeerData();
/*
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("11KVPole Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PoleSurveyActivity.this, SurveyDataActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });*/



      /*  ehtName=new ArrayList<>();
        //ehtName.add("Select");
        ehtName=arcGISDatabase.getIncrementalEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(IncrementalTopTenActivity.this,
                android.R.layout.simple_spinner_dropdown_item,ehtName);
        sp_ssname.setAdapter(ehtSSadapter);*/



        btn_next=(Button)findViewById(R.id.btn_next);
        progressDialog=new ProgressDialog(IncrementalTopTenActivityUG33KV.this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId=preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS,"");

        Intent intentData = getIntent();
        ssName = intentData.getStringExtra("SSNAME");
        feederName = intentData.getStringExtra("FEEDER_NAME");
        feederCode = intentData.getStringExtra("FEEDER_CODE");
       // maxCount = intentData.getStringExtra("MAX_COUNT");
        work_order = intentData.getStringExtra("WORKORDER");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(IncrementalTopTenActivityUG33KV.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if("Select".equals(ssName)){
                    Toast.makeText(IncrementalTopTenActivity.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(IncrementalTopTenActivity.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(work_order)){
                    Toast.makeText(IncrementalTopTenActivity.this,"Select Work Order",Toast.LENGTH_LONG).show();
                }else {*/
                    if (ConnectivityCheckingUG33KV.checkConnection(IncrementalTopTenActivityUG33KV.this)) {
                        getSurveyData();
                    }else {
                        Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }


                    /*Intent intent = new Intent(MainActivity.this, PoleSurveyActivity.class);
                    intent.putExtra("SSNAME", ssName);
                    intent.putExtra("FEEDER_NAME", feederName);
                    startActivity(intent);
                    finish();*/
               // }
            }
        });

    }
   /* @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            switch (adapterView.getId()){

                case R.id.sp_ssname:
                    feederNameList = new ArrayList<>();
                    ssName = sp_ssname.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(ssName)) {
                        String[] ss = ssName.split("-");
                        ssCode = ss[0]; // 004
                        subName= ss[1];
                        //Collections.reverse(feederNameList);
                        feederNameList = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                        feederNameList.add("Select");
                        Collections.reverse(feederNameList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalTopTenActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername.setAdapter(adapter);
                    } else {
                        feederNameList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalTopTenActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_feedername:
                    workOrderList = new ArrayList<>();
                    feederName = sp_feedername.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(feederName)) {
                        String[] ss = feederName.split("-");
                        feerdeCode = ss[0]; // 004
                        feeder_Name= ss[1];
                        //Collections.reverse(feederNameList);
                        workOrderList = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT WORKORDER_ID FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feerdeCode));
                        workOrderList.add("Select");
                        Collections.reverse(workOrderList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalTopTenActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, workOrderList);
                        sp_workorder.setAdapter(adapter);
                    } else {
                        workOrderList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalTopTenActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, workOrderList);
                        sp_workorder.setAdapter(adapter);
                    }


                    break;
                case R.id.sp_workorder:
                    work_order = sp_workorder.getSelectedItem().toString();

                    break;


                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }*/

   /* @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }*/
    public void getSurveyData(){

        // FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV
        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName ));
        try {
            /*List<String> workOrderList= arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT WO_ID FROM WO_STATUS_TABLE"));
            List<String> workOrderStatus= arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT WO_STATUS FROM WO_STATUS_TABLE"));
            if(workOrderList.size()>0 && workOrderStatus.size()>0){
                if(workOrderStatus.get(0) !=null && "N".equalsIgnoreCase(workOrderStatus.get(0))){
                    getTopTen();
                }else {
                    if(workOrderStatus.size()>0) {
                        String erMessege = "Complete" + workOrderList.get(0) + "Survey";
                        Toast.makeText(IncrementalFeederActivity.this, erMessege, Toast.LENGTH_LONG).show();
                    }else {
                        getTopTen();
                    }
                }


            }else {
                getTopTen();
            }*/
            getTopTen();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IncrementalTopTenActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
     public void getTopTen(){
         progressDialog.show();
        /* String[] feederDta = feederName.split("-");
         feederCode = feederDta[0];
         feedeNameList=feederDta[1];*/

         List<String>   poleNumber = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT FROM_POLE FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", work_order));
         IncrementalLastSurveyRequestVoUG33KV requestVo = new IncrementalLastSurveyRequestVoUG33KV();
         requestVo.setFEEDER_ID(feederCode);
         requestVo.setPOLE_NO(poleNumber.get(0));
         //requestVo.setFEEDER_ID(work_order);
         requestVo.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(IncrementalTopTenActivityUG33KV.this));
         requestVo.setSESSION_ID(sessionId);
         requestVo.setVERSION(MobileConstantsUG33KV.APP_VERSION);
         requestVo.setWO_ID(work_order);
         Gson gson = new Gson();
         String json = gson.toJson(requestVo);



         Log.v("REQDATATOP===>>", "" + json);
         retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
         Call<LastSurveyResponseSSDataVoUG33KV> responseVoCall = retrofitServiceUG33KV.getIncrementalLastSurveyData(requestVo);
         responseVoCall.enqueue(new Callback<LastSurveyResponseSSDataVoUG33KV>() {
             @Override
             public void onResponse(Call<LastSurveyResponseSSDataVoUG33KV> call, Response<LastSurveyResponseSSDataVoUG33KV> response) {
                 progressDialog.dismiss();
                 if (response.body() != null) {
                     LastSurveyResponseSSDataVoUG33KV response_vo = response.body();
                     Gson gson = new Gson();
                     String json = gson.toJson(response_vo);
                     Log.v("RESDATATOP===>>", "" + json);
                     // progressDialog.dismiss();
                     if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                         arcGISDatabase.clearSurveyedData();
                         arcGISDatabase.clearssMasterData();
                        // arcGISDatabase.clearWOStatusMasterData();
                         arcGISDatabase.clearIncrementalssMasterData();

                        /* WoStatusMasterVo woStatusMasterVo=new WoStatusMasterVo();
                         woStatusMasterVo.setFEEDER_ID(feederCode);
                         woStatusMasterVo.setWO_ID(work_order);
                         woStatusMasterVo.setWO_STATUS("N");
                         arcGISDatabase.addWoStatusMasterData(woStatusMasterVo);*/
                         List<SSMasterVoUG33KV> ssList = response_vo.getSS_MASTER();
                         if (ssList != null && ssList.size() > 0) {
                             for (SSMasterVoUG33KV ssMasterVoUG33KV : ssList) {
                                 arcGISDatabase.addIncrementalSSMasterData(ssMasterVoUG33KV);
                             }
                         }

                         if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                             Intent intent = new Intent(IncrementalTopTenActivityUG33KV.this, IncrementalPoleSurveyActivityUG33KV.class);
                             intent.putExtra("SSNAME",ssName);
                             intent.putExtra("FEEDER_NAME",feederName);
                             intent.putExtra("FEEDER_CODE",feederCode);
                             intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                             intent.putExtra("WORKORDER", work_order);
                             startActivity(intent);
                             finish();
                         } else {
                             arcGISDatabase.clearIncrementalSurveyedData();
                             List<LastSurveyResponseDataVoUG33KV> masterResponseDataVos = response_vo.getRESPONSE();
                             for (LastSurveyResponseDataVoUG33KV vo : masterResponseDataVos) {
                                 IncrementalSurveyVoUG33KV incrementalSurveyVoUG33KV = new IncrementalSurveyVoUG33KV();
                                 incrementalSurveyVoUG33KV.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                 incrementalSurveyVoUG33KV.setFeeder_id(vo.getFEEDER_ID());
                                 incrementalSurveyVoUG33KV.setLat(vo.getGLAT());
                                 incrementalSurveyVoUG33KV.setLan(vo.getGLONG());
                                 incrementalSurveyVoUG33KV.setWorkOrder(vo.getWO_ID());
                                 incrementalSurveyVoUG33KV.setPastSurveyFlag("Y");
                                 arcGISDatabase.addIncrementalSurveyData(incrementalSurveyVoUG33KV);
                             }
                             Intent intent = new Intent(IncrementalTopTenActivityUG33KV.this, IncrementalPoleSurveyActivityUG33KV.class);
                             intent.putExtra("SSNAME", ssName);
                             intent.putExtra("FEEDER_NAME",feederName);
                             intent.putExtra("FEEDER_CODE",feederCode);
                             intent.putExtra("MAX_COUNT",response_vo.getSESSION_ID());
                             intent.putExtra("WORKORDER", work_order);
                             startActivity(intent);
                             finish();
                         }
                     } else {
                         // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                         if (response_vo.getAPK() != null) {
                             if (response_vo.getAPK().contains("http")) {
                                 ApkDowanloadInstalUG33KV apkDowanloadInstalUG33KV = new ApkDowanloadInstalUG33KV(response_vo.getAPK(), IncrementalTopTenActivityUG33KV.this);
                                apkDowanloadInstalUG33KV.execute();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IncrementalTopTenActivityUG33KV.this);
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
                            Toast.makeText(IncrementalTopTenActivityUG33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(IncrementalTopTenActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LastSurveyResponseSSDataVoUG33KV> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(IncrementalTopTenActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }


   /* public void getWorkOrderStatus(){
        progressDialog.show();
        String[] feederDta = feederName.split("-");
        feederCode = feederDta[0];
        feedeNameList=feederDta[1];

        // List<String>   poleNumber = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT FROM_POLE FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", work_order));
       *//* IncrementalLastSurveyRequestVo requestVo = new IncrementalLastSurveyRequestVo();
        requestVo.setFEEDER_ID(feederCode);
        requestVo.setPOLE_NO(poleNumber.get(0));
        //requestVo.setFEEDER_ID(work_order);
        requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(IncrementalFeederActivity.this));
        requestVo.setSESSION_ID(sessionId);
        requestVo.setVERSION(MobileConstants.APP_VERSION);
        Gson gson = new Gson();
        String json = gson.toJson(requestVo);*//*

        JsonObject postParam = new JsonObject();
        //postParam.addProperty("order",yourArray) ;
        postParam.addProperty("FEEDER_ID", feederCode);
        // postParam.addProperty("FEEDER_ID", "032111106");
        postParam.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(IncrementalTopTenActivity.this));
        postParam.addProperty("SESSION_ID", sessionId);
        postParam.addProperty("VERSION", MobileConstants.APP_VERSION);
        postParam.addProperty("WORK_ORDERID",work_order);



        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getIncrementalWorkOrderStatusData(postParam);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.isSuccessful()) {
                    JsonObject jsonObject=response.body();

                    // progressDialog.dismiss();
                    if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                       // arcGISDatabase.clearSurveyedData();
                        //arcGISDatabase.clearssMasterData();
                        arcGISDatabase.clearWOStatusMasterData();
                        //arcGISDatabase.clearIncrementalssMasterData();
                        WoStatusMasterVo woStatusMasterVo=new WoStatusMasterVo();
                        woStatusMasterVo.setFEEDER_ID(feederCode);
                        woStatusMasterVo.setWO_ID(work_order);
                        woStatusMasterVo.setWO_STATUS("N");
                        arcGISDatabase.addWoStatusMasterData(woStatusMasterVo);
                        Intent intent = new Intent(IncrementalTopTenActivity.this, IncrementalPoleSurveyActivity.class);
                        intent.putExtra("SSNAME",subName);
                        intent.putExtra("FEEDER_NAME",feedeNameList);
                        intent.putExtra("FEEDER_CODE",feederCode);
                       // intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                        intent.putExtra("WORKORDER", work_order);
                        startActivity(intent);
                        finish();

                        *//*if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {

                        } else {
                            arcGISDatabase.clearIncrementalSurveyedData();
                            List<LastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (LastSurveyResponseDataVo vo : masterResponseDataVos) {
                                IncrementalSurveyVo  incrementalSurveyVo= new IncrementalSurveyVo();
                                incrementalSurveyVo.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                incrementalSurveyVo.setFeeder_id(vo.getFEEDER_ID());
                                incrementalSurveyVo.setLat(vo.getGLAT());
                                incrementalSurveyVo.setLan(vo.getGLONG());
                                incrementalSurveyVo.setPastSurveyFlag("Y");
                                arcGISDatabase.addIncrementalSurveyData(incrementalSurveyVo);
                            }
                            Intent intent = new Intent(IncrementalFeederActivity.this, IncrementalPoleSurveyActivity.class);
                            intent.putExtra("SSNAME", subName);
                            intent.putExtra("FEEDER_NAME",feedeNameList);
                            intent.putExtra("FEEDER_CODE",feederCode);
                            intent.putExtra("MAX_COUNT",response_vo.getSESSION_ID());
                            intent.putExtra("WORKORDER", work_order);
                            startActivity(intent);
                            finish();
                        }*//*
                    } else {
                        // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                        JsonElement nullableText = jsonObject.get("woId");
                        String text = (nullableText instanceof JsonNull) ? "" : nullableText.getAsString();
                        if (text != null) {
                            if (text.contains("http")) {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IncrementalTopTenActivity.this);
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
                            Toast.makeText(IncrementalTopTenActivity.this,jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(IncrementalTopTenActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(IncrementalTopTenActivity.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }
*/
}
