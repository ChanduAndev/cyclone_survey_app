package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.newsurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewSurveyFeederActivityUG33KV extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_next;
    private Spinner sp_cmpssname,sp_cmpfeedername;
    private String ssName,feederName,feederId,maxCount,sessionId;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedsuervey_feeder_ugc_33kv);
        sp_cmpssname=findViewById(R.id.sp_cmpssname);
        sp_cmpfeedername=findViewById(R.id.sp_cmpfeedername);

//        getSupportActionBar().setTitle("RDSS 33kV UG Cable Survey");
        sp_cmpssname.setOnItemSelectedListener(this);
        sp_cmpfeedername.setOnItemSelectedListener(this);
        arcGISDatabase=new ArcGISDatabaseUG33KV(this);
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
        ehtName=arcGISDatabase.getCmpEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(NewSurveyFeederActivityUG33KV.this,
                android.R.layout.simple_spinner_item,ehtName);
        sp_cmpssname.setAdapter(ehtSSadapter);



        btn_next=(Button)findViewById(R.id.btn_next);
        progressDialog=new ProgressDialog(NewSurveyFeederActivityUG33KV.this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId=preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS,"");
        if(sessionId ==null || sessionId.isEmpty()){
            Intent intent=new Intent(NewSurveyFeederActivityUG33KV.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if("Select".equals(ssName)){
                    Toast.makeText(NewSurveyFeederActivityUG33KV.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(NewSurveyFeederActivityUG33KV.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else {
                    if (ConnectivityCheckingUG33KV.checkConnection(NewSurveyFeederActivityUG33KV.this)) {
                       // getSurveyData();

                       // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName));

                        String[] feederDta = feederName.split("-");
                        String feederCode = feederDta[0];
                        /*Intent intent = new Intent(NewSurveyFeederActivity.this, CompletedMapActivity.class);
                        intent.putExtra("FEEDER_CODE",feederCode);
                        startActivity(intent);
                        finish();*/
                        getSurveyData(feederCode);

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
        switch (adapterView.getId()){
            case R.id.sp_cmpssname:
                feederNameList=new ArrayList<>();
                ssName=sp_cmpssname.getSelectedItem().toString();
                if(!"Select".equalsIgnoreCase(ssName)) {


                    String[] ss = ssName.split("-");
                    String ssCode = ss[0]; // 004
                    //String ssName = ss[1];
                    //Collections.reverse(feederNameList);
                    feederNameList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM CMPEHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                    feederNameList.add("Select");
                    Collections.reverse(feederNameList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewSurveyFeederActivityUG33KV.this,
                            android.R.layout.simple_spinner_item, feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);
                    //Collections.reverse(feederNameList);
                    /*feederNameList = arcGISDatabase.getComplaint(String.format("SELECT FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_NAME='%s'",ssName));
                    feederNameList.add("Select");
                    Collections.reverse(feederNameList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompetedSurveyFeederActivity.this,
                            android.R.layout.simple_spinner_item,feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);*/
                }else {
                    feederNameList.add("Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewSurveyFeederActivityUG33KV.this,
                            android.R.layout.simple_spinner_item,feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);
                }
                break;
            case R.id.sp_cmpfeedername:
                feederName=sp_cmpfeedername.getSelectedItem().toString();

                break;

                default:

                    break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewSurveyFeederActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
    public void getSurveyData(String feederCode){
        try {
            if (ConnectivityCheckingUG33KV.checkConnection(NewSurveyFeederActivityUG33KV.this)) {

                progressDialog.show();

                JsonObject postParam = new JsonObject();
                //postParam.addProperty("order",yourArray) ;
                postParam.addProperty("FEEDER_ID", feederCode);
                postParam.addProperty("IMEI_NO", CommanUtilitsUG33KV.getIMEI_NO(NewSurveyFeederActivityUG33KV.this));
                postParam.addProperty("SESSION_ID", sessionId);
                postParam.addProperty("VERSION", MobileConstantsUG33KV.APP_VERSION);

                retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
                Call<JsonObject> responseVoCall = retrofitServiceUG33KV.getCompletedData(postParam);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        if (response.body() != null) {
                            JsonObject jsonObject = response.body();


                            if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                                arcGISDatabase.clearSurveyedData();
                                JsonObject responseObj = jsonObject.getAsJsonObject("RESPONSE");
                                responseObj.entrySet();

                                List<String> keys = new ArrayList<>();
                                Set<Map.Entry<String, JsonElement>> entries = responseObj.entrySet();//will return members of your object
                                for (Map.Entry<String, JsonElement> entry : entries) {
                                    keys.add(entry.getKey());
                                    System.out.println(entry.getKey());
                                }
                                Log.v("KEYS", "" + keys.size());
                                for (String s : keys) {

                                        JsonArray elements = responseObj.getAsJsonArray(s);
                                    if(s.equalsIgnoreCase("2")) {
                                        for (int i = 0; elements.size() > i; i++) {
                                            JsonObject data = elements.get(i).getAsJsonObject();
                                            //Double  lat=data.get("lat").getAsDouble();
                                            //  Double  lon=data.get("long").getAsDouble();
                                            Log.v("DATA==", "" + data);
                                            // ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                                            SurveyVoUG33KV surveyVoUG33KV = new SurveyVoUG33KV();
                                            surveyVoUG33KV.setPole_no(Integer.parseInt(data.get("poleno").getAsString()));
//                                        surveyVo.setFeeder_id(data.get("feederid").getAsString());
                                            surveyVoUG33KV.setLat(data.get("lat").getAsString());
                                            surveyVoUG33KV.setLan(data.get("long").getAsString());
                                            surveyVoUG33KV.setPastSurveyFlag("Y");
                                            arcGISDatabase.addSurveyData(surveyVoUG33KV);


                                            //add a new graphic with a new point geometry

                                        }
                                    }

                                }
                                Intent intent=new Intent(NewSurveyFeederActivityUG33KV.this, NewSurveyArcGISMapActivityUG33KV.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(NewSurveyFeederActivityUG33KV.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                                if("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())){
                                    Intent intent=new Intent(NewSurveyFeederActivityUG33KV.this, FeederMasterActivityUG33KV.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } else {
                            Toast.makeText(NewSurveyFeederActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        //  progressDialog.dismiss();
                        Toast.makeText(NewSurveyFeederActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    }

