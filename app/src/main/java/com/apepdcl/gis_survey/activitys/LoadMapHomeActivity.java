package com.apepdcl.gis_survey.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.incremental.IncrementalCmpDTRSelectActivity;
import com.apepdcl.gis_survey.module_11KV.view_feeder_sketch.FeederKetchHomeActivity;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LTCktsReqVo;
import com.apepdcl.gis_survey.vo.LTCktsResponseDataVo;
import com.apepdcl.gis_survey.vo.LTCktsResponseVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadMapHomeActivity extends AppCompatActivity implements View.OnClickListener {
 private Button btn_load_ltfeedermap,btn_load_ltincrementalmap,btn_load_11kv_feedermap;
    private ProgressDialog progressDialog;
    private String userName;
    private SharedPreferences preferences;
    private ArcGISDatabase arcGISDatabase;
    private List<LoginDataResponse> loginDataResponses;
    private RetrofitService retrofitService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmaphome);
        btn_load_ltfeedermap=findViewById(R.id.btn_load_ltfeedermap);
        btn_load_ltincrementalmap=findViewById(R.id.btn_load_ltincrementalmap);
        btn_load_11kv_feedermap=findViewById(R.id.btn_load_11kv_feedermap);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        arcGISDatabase=new ArcGISDatabase(LoadMapHomeActivity.this);
        preferences= PreferenceManager.getDefaultSharedPreferences(LoadMapHomeActivity.this);
        userName=preferences.getString(MobileConstants.USERNAME,"");
        loginDataResponses=arcGISDatabase.getUserMasterData();
        btn_load_ltfeedermap.setOnClickListener(this);
        btn_load_ltincrementalmap.setOnClickListener(this);
        btn_load_11kv_feedermap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_load_ltfeedermap:
                if (ConnectivityChecking.checkConnection(LoadMapHomeActivity.this)) {

                    getCompletedDtrs();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_load_ltincrementalmap:
                if (ConnectivityChecking.checkConnection(LoadMapHomeActivity.this)) {
                    getIncrementalCompletedDtrs();
                    //loadIncrementalFeederDataData();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_load_11kv_feedermap:
                if (ConnectivityChecking.checkConnection(LoadMapHomeActivity.this)) {
                    Intent intent = new Intent(LoadMapHomeActivity.this, FeederKetchHomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoadMapHomeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public  void getCompletedDtrs(){

        progressDialog.show();
        LTCktsReqVo ltCktsReqVo=new LTCktsReqVo();
        ltCktsReqVo.setASSIGNED_TO(userName);
        // ltCktsReqVo.setASSIGNED_TO("1078667");
        ltCktsReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(LoadMapHomeActivity.this));
        ltCktsReqVo.setVERSION(MobileConstants.APP_VERSION);
        ltCktsReqVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());

        Gson gson=new Gson();
        String str=gson.toJson(ltCktsReqVo);
        //Log.v("DATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<LTCktsResponseVo> responseVoCall = retrofitService.getGisltckt(ltCktsReqVo);
        responseVoCall.enqueue(new Callback<LTCktsResponseVo>() {
            @Override
            public void onResponse(Call<LTCktsResponseVo> call, Response<LTCktsResponseVo> response) {
                progressDialog.dismiss();
                if(response.body() !=null) {
                    LTCktsResponseVo ltCktsResponseVo = response.body();
                    if(MobileConstants.TRUE.equalsIgnoreCase(ltCktsResponseVo.getSTATUS())){
                        arcGISDatabase.clearCmpDTRData();
                        List<LTCktsResponseDataVo> ltSurveyRequestDataVosList=ltCktsResponseVo.getRESPONSE();
                        for (LTCktsResponseDataVo surveyRequestDataVo:ltSurveyRequestDataVosList){
                            arcGISDatabase.addCmpDTRData(surveyRequestDataVo);
                        }
                        Intent intent=new Intent(LoadMapHomeActivity.this,CmpDTRSelectActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(LoadMapHomeActivity.this,ltCktsResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(LoadMapHomeActivity.this,"Server Busy Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LTCktsResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoadMapHomeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public  void getIncrementalCompletedDtrs(){

        progressDialog.show();
        LTCktsReqVo ltCktsReqVo=new LTCktsReqVo();
        ltCktsReqVo.setASSIGNED_TO(userName);
        // ltCktsReqVo.setASSIGNED_TO("1078667");
        ltCktsReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(LoadMapHomeActivity.this));
        ltCktsReqVo.setVERSION(MobileConstants.APP_VERSION);
        ltCktsReqVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());

        Gson gson=new Gson();
        String str=gson.toJson(ltCktsReqVo);
       // Log.v("DATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getIncrementalMapGisltckt(ltCktsReqVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if(response.body() !=null) {
                    JsonObject jsonObject=response.body();
                    if("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())){
                        arcGISDatabase.clearCmpDTRData();
                        JsonArray jsonArray=jsonObject.get("RESPONSE").getAsJsonArray();
                        for(int i=0;i<jsonArray.size();i++){
                            LTCktsResponseDataVo  ltCktsResponseDataVo=new LTCktsResponseDataVo();
                            JsonObject dataObj=jsonArray.get(i).getAsJsonObject();
                            ltCktsResponseDataVo.setDTR_CODE(dataObj.get("DTR_CODE").getAsString());
                            ltCktsResponseDataVo.setDTR_PHASE(dataObj.get("DTR_PHASE").getAsString());
                            ltCktsResponseDataVo.setDTR_LOCATION(dataObj.get("DTR_LOCATION").getAsString());
                            ltCktsResponseDataVo.setLT_CKTCODE(dataObj.get("LT_CKT_CODE").getAsString());
                            ltCktsResponseDataVo.setFEEDER_ID(dataObj.get("FEEDER_ID").getAsString());
                            arcGISDatabase.addCmpDTRData(ltCktsResponseDataVo);
                        }
                        Intent intent=new Intent(LoadMapHomeActivity.this, IncrementalCmpDTRSelectActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(LoadMapHomeActivity.this,jsonObject.get("ERR_MSG").getAsString(),Toast.LENGTH_SHORT);
                    }
                  /*  LTCktsResponseVo ltCktsResponseVo = response.body();
                    if(MobileConstants.TRUE.equalsIgnoreCase(ltCktsResponseVo.getSTATUS())){
                        arcGISDatabase.clearCmpDTRData();
                        List<LTCktsResponseDataVo> ltSurveyRequestDataVosList=ltCktsResponseVo.getRESPONSE();
                        for (LTCktsResponseDataVo surveyRequestDataVo:ltSurveyRequestDataVosList){
                            arcGISDatabase.addCmpDTRData(surveyRequestDataVo);
                        }
                        Intent intent=new Intent(LoadMapHomeActivity.this, IncrementalCmpDTRSelectActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(LoadMapHomeActivity.this,ltCktsResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }*/


                }else {
                    Toast.makeText(LoadMapHomeActivity.this,"Server Busy Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoadMapHomeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}



