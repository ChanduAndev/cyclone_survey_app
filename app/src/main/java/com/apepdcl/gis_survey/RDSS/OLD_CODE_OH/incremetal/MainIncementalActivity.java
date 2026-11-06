package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.incremetal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalFeederDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.WoStatusMasterVo;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainIncementalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_next;
    private Spinner sp_ssname1,sp_feedername1,sp_workorder;
    private String ssName,feederName,feederId,maxCount,sessionId,feederCode,ssCode,subName,feedeNameList,workorder,feerdeCode,feeder_Name;
    private ArcGISDatabase arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private List<String> workOrderList;
    private List<IncrementalFeederDataVo> incrementalFeederDataVoList=null;
    private TextView tv_survey_wo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_incremtal_11kv_oh_rdss);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("11Kv Incremental Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(MainIncementalActivity.this, FeederMasterActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        progressDialog = CommanUtilits.progressDialog(this);
        sp_ssname1=findViewById(R.id.sp_ssname1);
        sp_feedername1=findViewById(R.id.sp_feedername1);
        sp_workorder=findViewById(R.id.sp_workorder);
        tv_survey_wo=findViewById(R.id.tv_survey_wo);
        tv_survey_wo.setVisibility(View.GONE);
//        getSupportActionBar().setTitle("11KV LineSurvey");
        sp_ssname1.setOnItemSelectedListener(this);
        sp_feedername1.setOnItemSelectedListener(this);
        sp_workorder.setOnItemSelectedListener(this);
        arcGISDatabase=new ArcGISDatabase(this);

        incrementalFeederDataVoList=new ArrayList<>();
        incrementalFeederDataVoList=arcGISDatabase.getIncrementalFeerData();

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

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(MainIncementalActivity.this,
                android.R.layout.simple_spinner_dropdown_item,ehtName);
        sp_ssname1.setAdapter(ehtSSadapter);



        btn_next=(Button)findViewById(R.id.btn_next);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId=preferences.getString(MobileConstants.SESSION_ID,"");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(MainIncementalActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("Select".equals(ssName)){
                    Toast.makeText(MainIncementalActivity.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(MainIncementalActivity.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else {
                    if (ConnectivityChecking.checkConnection(MainIncementalActivity.this)) {
                        getWorkOrderStatus();
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

                case R.id.sp_ssname1:
                    feederNameList = new ArrayList<>();
                    ssName = sp_ssname1.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(ssName)) {


                        String[] ss = ssName.split("-");
                        ssCode = ss[0]; // 004
                        subName= ss[1];
                        //Collections.reverse(feederNameList);
                        feederNameList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM INCREMENTAL_EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                        feederNameList.add("Select");
                        Collections.reverse(feederNameList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainIncementalActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername1.setAdapter(adapter);
                    } else {
                        feederNameList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainIncementalActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername1.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_feedername1:
                    feederName = sp_feedername1.getSelectedItem().toString();

                    workOrderList = new ArrayList<>();
                    if (!"Select".equalsIgnoreCase(feederName)) {
                        String[] ss = feederName.split("-");
                        feerdeCode = ss[0]; // 004
                        feeder_Name= ss[1];
                        //Collections.reverse(feederNameList);
                        workOrderList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT WORKORDER_ID FROM INCREMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feerdeCode));
                        workOrderList.add("Select");
                        Collections.reverse(workOrderList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainIncementalActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, workOrderList);
                        sp_workorder.setAdapter(adapter);
                    } else {
                        workOrderList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainIncementalActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, workOrderList);
                        sp_workorder.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_workorder:
                    workorder = sp_workorder.getSelectedItem().toString();
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
    /*public void getSurveyData(){
        // FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV
        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName ));
        try {
            progressDialog.show();
            String[] feederDta = feederName.split("-");
            List<String>   poleNumber = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FROM_POLE FROM INCREMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", workorder));
            feederCode = feederDta[0];
            feedeNameList=feederDta[1];
            IncrementalLastSurveyRequestVo requestVo = new IncrementalLastSurveyRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(MainIncementalActivity.this));
            requestVo.setSESSION_ID(sessionId);
           // requestVo.setPOLE_NO(incrementalFeederDataVoList.get(0).getFROM_POLE());
            requestVo.setPOLE_NO(poleNumber.get(0));

            requestVo.setVERSION(MobileConstants.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<LastSurveyResponseSSDataVo> responseVoCall = retrofitService.getIncrementalLastSurveyData(requestVo);
            responseVoCall.enqueue(new Callback<LastSurveyResponseSSDataVo>() {
                @Override
                public void onResponse(Call<LastSurveyResponseSSDataVo> call, Response<LastSurveyResponseSSDataVo> response) {

                    if (response.body() != null) {

                        LastSurveyResponseSSDataVo response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);
                        Log.v("LastSurveyDATA===>>", "" + json);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearIncrementalSurveyedData();
                            arcGISDatabase.clearssIncrementalMasterData();
                            List<SSMasterVo> ssList = response_vo.getSS_MASTER();
                            if (ssList != null && ssList.size() > 0) {
                                for (SSMasterVo ssMasterVo : ssList) {
                                    arcGISDatabase.addIncrementalSSMasterData(ssMasterVo);
                                }
                            }

                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Intent intent = new Intent(MainIncementalActivity.this, DTRMasterDownloadIncrementaActivity.class);
                                intent.putExtra("SSNAME",subName);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                intent.putExtra("WORKORDER", workorder);
                                startActivity(intent);
                                finish();
                            } else {
                                arcGISDatabase.clearIncrementalSurveyedData();
                                List<LastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                                for (LastSurveyResponseDataVo vo : masterResponseDataVos) {
                                    IncrementalSurveyVo surveyVo = new IncrementalSurveyVo();
                                    surveyVo.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVo.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVo.setLat(vo.getGLAT());
                                    surveyVo.setLan(vo.getGLONG());
                                    surveyVo.setPastSurveyFlag("Y");
                                    arcGISDatabase.addIncrementalSurveyData(surveyVo);
                                }
                                Intent intent = new Intent(MainIncementalActivity.this, DTRMasterDownloadIncrementaActivity.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("MAX_COUNT",response_vo.getSESSION_ID());
                                intent.putExtra("WORKORDER", workorder);
                                startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss();
                        } else {
                            // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               *//* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), MainActivity.this);
                                apkDowanloadInstal.execute();*//*
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainIncementalActivity.this);
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
                                Toast.makeText(MainIncementalActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(MainIncementalActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseSSDataVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(MainIncementalActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/





      public void getWorkOrderStatus(){
        // FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV
        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName ));
        try {
            progressDialog.show();
            String[] feederDta = feederName.split("-");
            List<String>   poleNumber = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FROM_POLE FROM INCREMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", workorder));
            feederCode = feederDta[0];
            feedeNameList=feederDta[1];
            /*IncrementalLastSurveyRequestVo requestVo = new IncrementalLastSurveyRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(MainIncementalActivity.this));
            requestVo.setSESSION_ID(sessionId);
           // requestVo.setPOLE_NO(incrementalFeederDataVoList.get(0).getFROM_POLE());
            requestVo.setPOLE_NO(poleNumber.get(0));

            requestVo.setVERSION(MobileConstants.APP_VERSION);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);*/


            JsonObject postParam = new JsonObject();
            //postParam.addProperty("order",yourArray) ;
            postParam.addProperty("FEEDER_ID", feederCode);
            // postParam.addProperty("FEEDER_ID", "032111106");
            postParam.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(MainIncementalActivity.this));
            postParam.addProperty("SESSION_ID", sessionId);
            postParam.addProperty("VERSION", MobileConstants.APP_VERSION);
            postParam.addProperty("WO_ID",workorder);

            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<JsonObject> responseVoCall = retrofitService.getWorkOrderStatus(postParam);
            responseVoCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();
                    if (response.body() != null && response.isSuccessful()) {
                        JsonObject jsonObject=response.body();
                        // progressDialog.dismiss();

                        if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                            arcGISDatabase.clearWoStatus();
                            JsonObject dataObj=jsonObject.get("RESPONSE").getAsJsonObject();
                            WoStatusMasterVo woStatusMasterVo=new WoStatusMasterVo();
                            if(feederCode.equalsIgnoreCase(dataObj.get("FEEDER_ID").getAsString())){
                                if(workorder.equalsIgnoreCase(dataObj.get("WO_ID").getAsString())) {
                                    woStatusMasterVo.setFEEDER_ID(dataObj.get("FEEDER_ID").getAsString());
                                    woStatusMasterVo.setWO_ID(dataObj.get("WO_ID").getAsString());
                                    woStatusMasterVo.setWO_STATUS(dataObj.get("WO_STATUS").getAsString());
                                    arcGISDatabase.addWoStatusMasterData(woStatusMasterVo);
                                    Intent intent = new Intent(MainIncementalActivity.this, TopTenDatalActivity.class);
                                    intent.putExtra("SSNAME",subName);
                                    intent.putExtra("FEEDER_NAME",feedeNameList);
                                    intent.putExtra("FEEDER_CODE",feederCode);
                                    intent.putExtra("WORKORDER", workorder);
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



                        } else {
                            arcGISDatabase.clearWoStatus();
                            if("V001".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())){
                                WoStatusMasterVo woStatusMasterVo=new WoStatusMasterVo();
                                woStatusMasterVo.setFEEDER_ID(feederCode);
                                woStatusMasterVo.setWO_ID(workorder);
                                woStatusMasterVo.setWO_STATUS("N");
                                arcGISDatabase.addWoStatusMasterData(woStatusMasterVo);
                                Intent intent = new Intent(MainIncementalActivity.this, TopTenDatalActivity.class);
                                intent.putExtra("SSNAME",subName);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("WORKORDER", workorder);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(MainIncementalActivity.this,jsonObject.get("ERR_MSG").getAsString(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        Toast.makeText(MainIncementalActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(MainIncementalActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }






    @Override
    public void onBackPressed() {
      /*  Intent intent = new Intent(MainActivity.this, FeederMasterActivity.class);
        startActivity(intent);
        finish();*/
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
