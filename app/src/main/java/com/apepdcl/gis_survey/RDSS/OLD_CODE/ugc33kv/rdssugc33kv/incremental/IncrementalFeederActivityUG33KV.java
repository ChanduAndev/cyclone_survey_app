package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.incremental;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.FeederMasterActivityUG33KV;

import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.WoStatusMasterVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncrementalFeederActivityUG33KV extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_next;
    private Spinner sp_ssname,sp_feedername,sp_workorder;
    private String ssName,feederName,feederId,maxCount,sessionId,feederCode,ssCode,subName,feedeNameList,work_order,feerdeCode,feeder_Name;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<String> feederNameList;
    private List<String> workOrderList;
    private List<String> ehtName;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private List<IncrementalFeederDataVoUG33KV> incrementalFeederDataVoUG33KVList =null;
    private TextView tv_survey_wo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incrementafeeder_ugc_33kv);
        sp_ssname=findViewById(R.id.sp_ssname);
        sp_feedername=findViewById(R.id.sp_feedername);
        sp_workorder=findViewById(R.id.sp_workorder);
        tv_survey_wo=findViewById(R.id.tv_survey_wo);
        getSupportActionBar().setTitle("33Kv IncrementalLineSurvey");
        sp_ssname.setOnItemSelectedListener(this);
        sp_feedername.setOnItemSelectedListener(this);
        sp_workorder.setOnItemSelectedListener(this);
        arcGISDatabase=new ArcGISDatabaseUG33KV(this);
        incrementalFeederDataVoUG33KVList =new ArrayList<>();
        incrementalFeederDataVoUG33KVList =arcGISDatabase.getIncrementalFeerData();
        tv_survey_wo.setVisibility(View.GONE);
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



        ehtName=new ArrayList<>();
        //ehtName.add("Select");
        ehtName=arcGISDatabase.getIncrementalEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(IncrementalFeederActivityUG33KV.this,
                android.R.layout.simple_spinner_dropdown_item,ehtName);
        sp_ssname.setAdapter(ehtSSadapter);



        btn_next=(Button)findViewById(R.id.btn_next);
        progressDialog=new ProgressDialog(IncrementalFeederActivityUG33KV.this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId=preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS,"");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(IncrementalFeederActivityUG33KV.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("Select".equals(ssName)){
                    Toast.makeText(IncrementalFeederActivityUG33KV.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(IncrementalFeederActivityUG33KV.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(work_order)){
                    Toast.makeText(IncrementalFeederActivityUG33KV.this,"Select Work Order",Toast.LENGTH_LONG).show();
                }else {
                    if (ConnectivityCheckingUG33KV.checkConnection(IncrementalFeederActivityUG33KV.this)) {
                        getSurveyData();
                    }else {
                        Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }


                    /*Intent intent = new Intent(MainActivity.this, PoleSurveyActivity.class);
                    intent.putExtra("SSNAME", ssName);
                    intent.putExtra("FEEDER_NAME", feederName);
                    startActivity(intent);
                    finish();*/
                }
            }
        });

    }
    @Override
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalFeederActivityUG33KV.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername.setAdapter(adapter);
                    } else {
                        feederNameList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalFeederActivityUG33KV.this,
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalFeederActivityUG33KV.this,
                                android.R.layout.simple_spinner_dropdown_item, workOrderList);
                        sp_workorder.setAdapter(adapter);
                    } else {
                        workOrderList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalFeederActivityUG33KV.this,
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
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
            //getTopTen();
            getWorkOrderStatus();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IncrementalFeederActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
    /* public void getTopTen(){
         progressDialog.show();
         String[] feederDta = feederName.split("-");
         feederCode = feederDta[0];
         feedeNameList=feederDta[1];

         List<String>   poleNumber = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT FROM_POLE FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", work_order));
         IncrementalLastSurveyRequestVo requestVo = new IncrementalLastSurveyRequestVo();
         requestVo.setFEEDER_ID(feederCode);
         requestVo.setPOLE_NO(poleNumber.get(0));
         //requestVo.setFEEDER_ID(work_order);
         requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(IncrementalFeederActivity.this));
         requestVo.setSESSION_ID(sessionId);
         requestVo.setVERSION(MobileConstants.APP_VERSION);
         Gson gson = new Gson();
         String json = gson.toJson(requestVo);



         Log.v("REQDATATOP===>>", "" + json);
         retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
         Call<LastSurveyResponseSSDataVo> responseVoCall = retrofitService.getIncrementalLastSurveyData(requestVo);
         responseVoCall.enqueue(new Callback<LastSurveyResponseSSDataVo>() {
             @Override
             public void onResponse(Call<LastSurveyResponseSSDataVo> call, Response<LastSurveyResponseSSDataVo> response) {
                 progressDialog.dismiss();
                 if (response.body() != null) {
                     LastSurveyResponseSSDataVo response_vo = response.body();
                     Gson gson = new Gson();
                     String json = gson.toJson(response_vo);
                     Log.v("RESDATATOP===>>", "" + json);
                     // progressDialog.dismiss();
                     if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                         arcGISDatabase.clearSurveyedData();
                         arcGISDatabase.clearssMasterData();
                         arcGISDatabase.clearWOStatusMasterData();
                         arcGISDatabase.clearIncrementalssMasterData();

                         WoStatusMasterVo woStatusMasterVo=new WoStatusMasterVo();
                         woStatusMasterVo.setFEEDER_ID(feederCode);
                         woStatusMasterVo.setWO_ID(work_order);
                         woStatusMasterVo.setWO_STATUS("N");
                         arcGISDatabase.addWoStatusMasterData(woStatusMasterVo);
                         List<SSMasterVo> ssList = response_vo.getSS_MASTER();
                         if (ssList != null && ssList.size() > 0) {
                             for (SSMasterVo ssMasterVo : ssList) {
                                 arcGISDatabase.addIncrementalSSMasterData(ssMasterVo);
                             }
                         }

                         if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                             Intent intent = new Intent(IncrementalFeederActivity.this, IncrementalPoleSurveyActivity.class);
                             intent.putExtra("SSNAME",subName);
                             intent.putExtra("FEEDER_NAME",feedeNameList);
                             intent.putExtra("FEEDER_CODE",feederCode);
                             intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                             intent.putExtra("WORKORDER", work_order);
                             startActivity(intent);
                             finish();
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
                         }
                     } else {
                         // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                         if (response_vo.getAPK() != null) {
                             if (response_vo.getAPK().contains("http")) {
                                *//* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), MainActivity.this);
                                apkDowanloadInstal.execute();*//*
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IncrementalFeederActivity.this);
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
                            Toast.makeText(IncrementalFeederActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(IncrementalFeederActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LastSurveyResponseSSDataVo> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(IncrementalFeederActivity.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }*/
    public void getWorkOrderStatus(){
        progressDialog.show();
        String[] feederDta = feederName.split("-");
        feederCode = feederDta[0];
        feedeNameList=feederDta[1];

        // List<String>   poleNumber = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT FROM_POLE FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", work_order));
       /* IncrementalLastSurveyRequestVo requestVo = new IncrementalLastSurveyRequestVo();
        requestVo.setFEEDER_ID(feederCode);
        requestVo.setPOLE_NO(poleNumber.get(0));
        //requestVo.setFEEDER_ID(work_order);
        requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(IncrementalFeederActivity.this));
        requestVo.setSESSION_ID(sessionId);
        requestVo.setVERSION(MobileConstants.APP_VERSION);
        Gson gson = new Gson();
        String json = gson.toJson(requestVo);*/

        JsonObject postParam = new JsonObject();
        //postParam.addProperty("order",yourArray) ;
        postParam.addProperty("FEEDER_ID", feederCode);
        // postParam.addProperty("FEEDER_ID", "032111106");
        postParam.addProperty("IMEI_NO", CommanUtilitsUG33KV.getIMEI_NO(IncrementalFeederActivityUG33KV.this));
        postParam.addProperty("SESSION_ID", sessionId);
        postParam.addProperty("VERSION", MobileConstantsUG33KV.APP_VERSION);
        postParam.addProperty("WORK_ORDERID",work_order);



        retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
        Call<JsonObject> responseVoCall = retrofitServiceUG33KV.getIncrementalWorkOrderStatusData(postParam);
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
                       /* "FEEDER_ID": "2021-01-04 03:01:24",
                                "WO_ID": "1234567896",
                                "WO_STATUS": "Y",
                                "UPDATED_DATE": "2021-01-04 03:01:24",*/

                       JsonObject dataObj=jsonObject.get("RESPONSE").getAsJsonObject();
                        WoStatusMasterVoUG33KV woStatusMasterVoUG33KV =new WoStatusMasterVoUG33KV();

                        if(feederCode.equalsIgnoreCase(dataObj.get("FEEDER_ID").getAsString())){
                            if(work_order.equalsIgnoreCase(dataObj.get("WO_ID").getAsString())) {
                                woStatusMasterVoUG33KV.setFEEDER_ID(dataObj.get("FEEDER_ID").getAsString());
                                woStatusMasterVoUG33KV.setWO_ID(dataObj.get("WO_ID").getAsString());
                                woStatusMasterVoUG33KV.setWO_STATUS(dataObj.get("WO_STATUS").getAsString());
                                arcGISDatabase.addWoStatusMasterData(woStatusMasterVoUG33KV);
                                Intent intent = new Intent(IncrementalFeederActivityUG33KV.this, IncrementalTopTenActivityUG33KV.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("FEEDER_NAME", feedeNameList);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                // intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                intent.putExtra("WORKORDER", work_order);
                                startActivity(intent);
                                finish();
                            }else {
                                String s="Please select workorder"+" '"+dataObj.get("WO_ID").getAsString()+" '"+ "survey";

                                //Toast.makeText(IncrementalFeederActivity.this,s,Toast.LENGTH_LONG).show();
                                tv_survey_wo.setVisibility(View.VISIBLE);
                                tv_survey_wo.setText(s);
                                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                anim.setDuration(1000); //You can manage the blinking time with this parameter
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                tv_survey_wo.startAnimation(anim);
                            }
                        }else {
                            String s="Feeder mapping missmatch"+"" +dataObj.get("FEEDER_ID").getAsString()+""+ "contact admin";
                            //Toast.makeText(IncrementalFeederActivity.this,s,Toast.LENGTH_LONG).show();
                            tv_survey_wo.setVisibility(View.VISIBLE);
                            tv_survey_wo.setText(s);
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(1000); //You can manage the blinking time with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            tv_survey_wo.startAnimation(anim);
                        }


                        /*if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {

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
                        }*/
                    } else {
                        arcGISDatabase.clearWOStatusMasterData();
                        if("V001".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())){
                            WoStatusMasterVoUG33KV woStatusMasterVoUG33KV =new WoStatusMasterVoUG33KV();
                            woStatusMasterVoUG33KV.setFEEDER_ID(feederCode);
                            woStatusMasterVoUG33KV.setWO_ID(work_order);
                            woStatusMasterVoUG33KV.setWO_STATUS("N");
                            arcGISDatabase.addWoStatusMasterData(woStatusMasterVoUG33KV);
                            Intent intent = new Intent(IncrementalFeederActivityUG33KV.this, IncrementalTopTenActivityUG33KV.class);
                            intent.putExtra("SSNAME", subName);
                            intent.putExtra("FEEDER_NAME", feedeNameList);
                            intent.putExtra("FEEDER_CODE", feederCode);
                            // intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                            intent.putExtra("WORKORDER", work_order);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(IncrementalFeederActivityUG33KV.this,jsonObject.get("ERR_MSG").getAsString(),Toast.LENGTH_SHORT).show();
                        }
                        // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                        /*JsonElement nullableText = jsonObject.get("APK");
                        String text = (nullableText instanceof JsonNull) ? "" : nullableText.getAsString();
                        if (text != null) {
                            if (text.contains("http")) {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IncrementalFeederActivity.this);
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
                            Toast.makeText(IncrementalFeederActivity.this,jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                        }*/
                    }
                } else {
                    Toast.makeText(IncrementalFeederActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(IncrementalFeederActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

}
