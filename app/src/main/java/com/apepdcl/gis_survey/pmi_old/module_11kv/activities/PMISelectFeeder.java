package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofit11kv;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitClientInstance11KV;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMISSMasterVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMISelectFeeder extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button btn_next;
    private Spinner sp_ssname1, sp_feedername1;
    private String ssName, feederName, feederId, maxCount, sessionId, feederCode, ssCode, subName, feedeNameList;
    private PMI11kvArcGISDatabase arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private PMIRetrofit11kv retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;


    //MULTI CKT REQUIRED
    LinearLayout lyt_is_multi_circuit;
    Spinner sp_is_multi_circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmiselect_feeder);
        getSupportActionBar().hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("11kV Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = CommanUtilits.progressDialog(this);
        sp_ssname1 = findViewById(R.id.sp_ssname1);
        sp_feedername1 = findViewById(R.id.sp_feedername1);
        lyt_is_multi_circuit = findViewById(R.id.lyt_is_multi_circuit);
        sp_is_multi_circuit = findViewById(R.id.sp_is_multi_circuit);
//        getSupportActionBar().setTitle("11Kv LineSurvey Test");
        sp_ssname1.setOnItemSelectedListener(this);
        sp_feedername1.setOnItemSelectedListener(this);
        arcGISDatabase = new PMI11kvArcGISDatabase(this);

        ehtName = new ArrayList<>();
        //ehtName.add("Select");
        ehtName = arcGISDatabase.getEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(PMISelectFeeder.this,
                android.R.layout.simple_spinner_dropdown_item, ehtName);
        sp_ssname1.setAdapter(ehtSSadapter);
        btn_next = (Button) findViewById(R.id.btn_next);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
        if (sessionId == null || sessionId.isEmpty()) {
            Intent intent = new Intent(PMISelectFeeder.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("Select".equals(ssName)){
                    Toast.makeText(PMISelectFeeder.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(PMISelectFeeder.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else if ("Select".equalsIgnoreCase(sp_is_multi_circuit.getSelectedItem().toString())) {
                    Toast.makeText(PMISelectFeeder.this, "Select Multi Circuit required", Toast.LENGTH_SHORT).show();
                }else {
                    if (ConnectivityChecking.checkConnection(PMISelectFeeder.this)) {
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMISelectFeeder.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername1.setAdapter(adapter);
                    } else {
                        feederNameList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMISelectFeeder.this,
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
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getSurveyData(){
        // FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV
        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName ));
        try {
            progressDialog.show();
            String[] feederDta = feederName.split("-");
            feederCode = feederDta[0];
            feedeNameList=feederDta[1];
            PMILastSurveyRequestVo requestVo = new PMILastSurveyRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMISelectFeeder.this));
            requestVo.setSESSION_ID(sessionId);
            requestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);
            retrofitService = PMIRetrofitClientInstance11KV.getRetrofitInstance().create(PMIRetrofit11kv.class);
            Call<PMILastSurveyResponseSSDataVo> responseVoCall = retrofitService.getLastSurveyData(requestVo);
            responseVoCall.enqueue(new Callback<PMILastSurveyResponseSSDataVo>() {
                @Override
                public void onResponse(Call<PMILastSurveyResponseSSDataVo> call, Response<PMILastSurveyResponseSSDataVo> response) {

                    if (response.body() != null) {
                        Log.v("DATA===>>", "" + json);
                        PMILastSurveyResponseSSDataVo response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearSurveyedData();
                            arcGISDatabase.clearssMasterData();
                            List<PMISSMasterVo> ssList = response_vo.getSS_MASTER();
                            if (ssList != null && ssList.size() > 0) {
                                for (PMISSMasterVo ssMasterVo : ssList) {
                                    arcGISDatabase.addSSMasterData(ssMasterVo);
                                }
                            }

                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Intent intent = new Intent(PMISelectFeeder.this, PMIDTRMasterDownloadActivity.class);
                                intent.putExtra("SSNAME",subName);
                                intent.putExtra("SSID",ssCode);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                intent.putExtra("MULTI_CKT", sp_is_multi_circuit.getSelectedItem().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                arcGISDatabase.clearSurveyedData();
                                List<PMILastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                                for (PMILastSurveyResponseDataVo vo : masterResponseDataVos) {
                                    PMISurveyVo surveyVo = new PMISurveyVo();
                                    surveyVo.setPole_no(Integer.parseInt(vo.getPOLE_NO()));
                                    surveyVo.setFeeder_id(vo.getFEEDER_ID());
                                    surveyVo.setLat(vo.getGLAT());
                                    surveyVo.setLan(vo.getGLONG());
                                    surveyVo.setPastSurveyFlag("Y");
                                    surveyVo.setPole_configuration(vo.getPOLE_CONFIG());
                                    arcGISDatabase.addSurveyData(surveyVo);
                                }
                                Intent intent = new Intent(PMISelectFeeder.this, PMIDTRMasterDownloadActivity.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("SSID",ssCode);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("MAX_COUNT",response_vo.getSESSION_ID());
                                intent.putExtra("MULTI_CKT", sp_is_multi_circuit.getSelectedItem().toString());
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
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMISelectFeeder.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade, please transmit the data to server ")
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
                                Toast.makeText(PMISelectFeeder.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(PMISelectFeeder.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMILastSurveyResponseSSDataVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PMISelectFeeder.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}