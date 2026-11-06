package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.Constants.MobileConstants;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys.DTRMasterDownloadActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys.FeederMasterActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.utilits.CommanUtilits;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LastSurveyRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LastSurveyResponseDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SSMasterVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SurveyVo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.apepdcl.gis.app.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
 private Button btn_next;
 private Spinner sp_ssname1,sp_feedername1;
 private String ssName,feederName,feederId,maxCount,sessionId,feederCode,ssCode,subName,feedeNameList;
 private ArcGISDatabase arcGISDatabase;
 private List<String> feederNameList;
 private List<String> ehtName;
 private RetrofitService retrofitService;
 private ProgressDialog progressDialog;
 private SharedPreferences preferences;
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main_11kv_oh_rdss);
  Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
  toolbar.setTitle("RDSS 11KV Survey");
  toolbar.setTitleTextColor(Color.WHITE);
  toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View v) {
    Intent playIntent = new Intent(MainActivity.this, FeederMasterActivity.class); //Intent.ACTION_VIEW);
    startActivity(playIntent);
    finish();

   }
  });
  progressDialog = CommanUtilits.progressDialog(this);
  sp_ssname1=findViewById(R.id.sp_ssname1);
  sp_feedername1=findViewById(R.id.sp_feedername1);
//        getSupportActionBar().setTitle("11KV LineSurvey");
  sp_ssname1.setOnItemSelectedListener(this);
  sp_feedername1.setOnItemSelectedListener(this);
  arcGISDatabase=new ArcGISDatabase(this);
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
  ehtName=arcGISDatabase.getEHTSSNAME();
  ehtName.add("Select");
  Collections.reverse(ehtName);

  ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(MainActivity.this,
          android.R.layout.simple_spinner_dropdown_item,ehtName);
  sp_ssname1.setAdapter(ehtSSadapter);



  btn_next=(Button)findViewById(R.id.btn_next);
  preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
  sessionId=preferences.getString(MobileConstants.SESSION_ID,"");
  if(sessionId ==null || sessionId.isEmpty()){
   Intent intent=new Intent(MainActivity.this, LoginActivity.class);
   startActivity(intent);
   finish();
  }
  btn_next.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
    if("Select".equals(ssName)){
     Toast.makeText(MainActivity.this,"Select SS Name",Toast.LENGTH_LONG).show();
    }else if("Select".equalsIgnoreCase(feederName)){
     Toast.makeText(MainActivity.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
    }else {
     if (ConnectivityChecking.checkConnection(MainActivity.this)) {
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

    case R.id.sp_ssname1:
     feederNameList = new ArrayList<>();
     ssName = sp_ssname1.getSelectedItem().toString();
     if (!"Select".equalsIgnoreCase(ssName)) {


      String[] ss = ssName.split("-");
      ssCode = ss[0]; // 004
      subName= ss[1];
      //Collections.reverse(feederNameList);
      feederNameList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
      feederNameList.add("Select");
      Collections.reverse(feederNameList);
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
              android.R.layout.simple_spinner_dropdown_item, feederNameList);
      sp_feedername1.setAdapter(adapter);
     } else {
      feederNameList.add("Select");
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
              android.R.layout.simple_spinner_dropdown_item, feederNameList);
      sp_feedername1.setAdapter(adapter);
     }
     break;
    case R.id.sp_feedername1:
     feederName = sp_feedername1.getSelectedItem().toString();
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
   progressDialog.show();
   String[] feederDta = feederName.split("-");
   feederCode = feederDta[0];
   feedeNameList=feederDta[1];
   LastSurveyRequestVo requestVo = new LastSurveyRequestVo();
   requestVo.setFEEDER_ID(feederCode);
   requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(MainActivity.this));
   requestVo.setSESSION_ID(sessionId);
   requestVo.setVERSION(MobileConstants.APP_VERSION);
   Gson gson = new Gson();
   String json = gson.toJson(requestVo);

   Log.v("DATA===>>", "" + json);
   retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
   Call<LastSurveyResponseSSDataVo> responseVoCall = retrofitService.getLastSurveyData(requestVo);
   responseVoCall.enqueue(new Callback<LastSurveyResponseSSDataVo>() {
    @Override
    public void onResponse(Call<LastSurveyResponseSSDataVo> call, Response<LastSurveyResponseSSDataVo> response) {

     if (response.body() != null) {
      Log.v("DATA===>>", "" + json);
      LastSurveyResponseSSDataVo response_vo = response.body();
      Gson gson = new Gson();
      String json = gson.toJson(response_vo);
      // progressDialog.dismiss();
      if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
       arcGISDatabase.clearSurveyedData();
       arcGISDatabase.clearssMasterData();
       List<SSMasterVo> ssList = response_vo.getSS_MASTER();
       if (ssList != null && ssList.size() > 0) {
        for (SSMasterVo ssMasterVo : ssList) {
         arcGISDatabase.addSSMasterData(ssMasterVo);
        }
       }

       if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
        Intent intent = new Intent(MainActivity.this, DTRMasterDownloadActivity.class);
        intent.putExtra("SSNAME",subName);
        intent.putExtra("FEEDER_NAME",feedeNameList);
        intent.putExtra("FEEDER_CODE",feederCode);
        intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
        startActivity(intent);
        finish();
       } else {
        arcGISDatabase.clearSurveyedData();
        List<LastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
        for (LastSurveyResponseDataVo vo : masterResponseDataVos) {
         SurveyVo surveyVo = new SurveyVo();
         surveyVo.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
         surveyVo.setFeeder_id(vo.getFEEDER_ID());
         surveyVo.setLat(vo.getGLAT());
         surveyVo.setLan(vo.getGLONG());
         surveyVo.setPastSurveyFlag("Y");
         arcGISDatabase.addSurveyData(surveyVo);
        }
        Intent intent = new Intent(MainActivity.this, DTRMasterDownloadActivity.class);
        intent.putExtra("SSNAME", subName);
        intent.putExtra("FEEDER_NAME",feedeNameList);
        intent.putExtra("FEEDER_CODE",feederCode);
        intent.putExtra("MAX_COUNT",response_vo.getSESSION_ID());
        startActivity(intent);
        finish();
       }
       progressDialog.dismiss();
      } else {
       // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

       if (response_vo.getAPK() != null) {
        if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), MainActivity.this);
                                apkDowanloadInstal.execute();*/
         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
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
        Toast.makeText(MainActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

       }
       progressDialog.dismiss();
      }
     } else {
      Toast.makeText(MainActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
     }
    }

    @Override
    public void onFailure(Call<LastSurveyResponseSSDataVo> call, Throwable t) {
     progressDialog.dismiss();
     //  progressDialog.dismiss();
     Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_LONG).show();
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
