package com.apepdcl.gis_survey.incremental;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.DTRGISMapActivity;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.DTRDetailsResponseVo;
import com.apepdcl.gis_survey.vo.IncrementalSurveyedDataReqVo;
import com.apepdcl.gis_survey.vo.LTCktsResponseDataVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.WoStatusMasterVo;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.apepdcl.gis_survey.HomeActivity;

public class IncrementalDTRSelectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Spinner sp_dtrcode,sp_circuit,sp_lt_workorder;
    private Button btn_next;
    private  List<LTCktsResponseDataVo> ltCktsResponseDataVos;
    private ArcGISDatabase arcGISDatabase;
    private List<String> dtrCodeList;
    private List<String> ltCircuitList;
    private List<String> workOrderList;
    private String dtrCode,ltCircuit,sessionId;
    private SharedPreferences sharedPreferences;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String dtr_Code,dtrLocation,lt_workorder;
    private CheckBox ch_dtrsurvey,ch_ltsurvey;
    private LinearLayout lyt_dtrselect,lyt_ltcircuitselect,lyt_lt_workorder,lyt_dtr_workorder;
    private int flag=0;
    private TextView tv_survey_wo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_inctremental_dtrselection);
            sp_dtrcode =findViewById(R.id.sp_dtrcode);
            sp_circuit =findViewById(R.id.sp_circuit);
            sp_lt_workorder=findViewById(R.id.sp_lt_workorder);
            ch_dtrsurvey=findViewById(R.id.ch_dtrsurvey);
            ch_ltsurvey=findViewById(R.id.ch_ltsurvey);
            btn_next = findViewById(R.id.btn_next);
            tv_survey_wo= findViewById(R.id.tv_survey_wo);
            lyt_dtrselect=findViewById(R.id.lyt_dtrselect);
            lyt_ltcircuitselect=findViewById(R.id.lyt_ltcircuitselect);
            lyt_lt_workorder=findViewById(R.id.lyt_lt_workorder);
            //lyt_dtr_workorder=findViewById(R.id.lyt_dtr_workorder);
            lyt_dtrselect.setVisibility(View.GONE);
            lyt_ltcircuitselect.setVisibility(View.GONE);
            lyt_lt_workorder.setVisibility(View.GONE);
            tv_survey_wo.setVisibility(View.GONE);
           // lyt_dtr_workorder.setVisibility(View.GONE);
            sp_dtrcode.setOnItemSelectedListener(this);
            sp_circuit.setOnItemSelectedListener(this);
            sp_lt_workorder.setOnItemSelectedListener(this);
            ch_dtrsurvey.setOnCheckedChangeListener(this);
            ch_ltsurvey.setOnCheckedChangeListener(this);
            btn_next.setOnClickListener(this);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Incremental GISLT Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(IncrementalDTRSelectActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            // setSupportActionBar(toolbar);
            arcGISDatabase = new ArcGISDatabase(this);
            ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData("");
            dtrCodeList = arcGISDatabase.getIncrementalDTRCode();
            dtrCodeList.add("Select");
            Collections.reverse(dtrCodeList);

            ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(IncrementalDTRSelectActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, dtrCodeList);
            sp_dtrcode.setAdapter(ehtSSadapter);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(IncrementalDTRSelectActivity.this);
            sessionId = sharedPreferences.getString(MobileConstants.SESSION_ID, "");
        }catch (Exception e){

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.sp_dtrcode:
                dtrCode=sp_dtrcode.getSelectedItem().toString();
                ltCircuitList=new ArrayList<>();
                if (!"Select".equalsIgnoreCase(dtrCode)) {

                    //Collections.reverse(feederNameList);
                    if (dtrCode.contains("-")) {
                        String[] dtrcode = dtrCode.split("-");
                        String dtr_Code1 = dtrcode[0]; // 004
                        // String ssName = poleNum[1];
                        ltCircuitList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT LT_CKTCODE FROM INCREMENTAL_DTR_TABLE WHERE DTR_CODE='%s'", dtr_Code1));
                        ltCircuitList.add("Select");
                        Collections.reverse(ltCircuitList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalDTRSelectActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                        sp_circuit.setAdapter(adapter);

                    } else {
                        ltCircuitList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT LT_CKTCODE FROM INCREMENTAL_DTR_TABLE WHERE DTR_CODE='%s'", dtrCode));
                        ltCircuitList.add("Select");
                        Collections.reverse(ltCircuitList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalDTRSelectActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                        sp_circuit.setAdapter(adapter);
                    }

                } else {
                    ltCircuitList.add("Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalDTRSelectActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                    sp_circuit.setAdapter(adapter);
                }
                break;

            case R.id.sp_circuit:
                ltCircuit=sp_circuit.getSelectedItem().toString();
                workOrderList=new ArrayList<>();
                if(!"Select".equalsIgnoreCase(ltCircuit)) {

                    workOrderList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT WORK_ORDERID FROM INCREMENTAL_DTR_TABLE WHERE LT_CKTCODE='%s'", ltCircuit.trim()));
                    workOrderList.add("Select");
                    Collections.reverse(workOrderList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalDTRSelectActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, workOrderList);
                    sp_lt_workorder.setAdapter(adapter);
                }else {
                    workOrderList.add("Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalDTRSelectActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, workOrderList);
                    sp_lt_workorder.setAdapter(adapter);
                }

                break;

            case R.id.sp_lt_workorder:
                lt_workorder=sp_lt_workorder.getSelectedItem().toString();

                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                if(ch_dtrsurvey.isChecked() || ch_ltsurvey.isChecked()){
                    if(flag==1){
                        if("Select".equalsIgnoreCase(dtrCode)){
                            Toast.makeText(IncrementalDTRSelectActivity.this,"Select DTR Code",Toast.LENGTH_SHORT).show();
                        }else {
                            String[] dtrcode = dtrCode.split("-");
                            dtr_Code= dtrcode[0];
                            dtrLocation=dtrcode[1];
                          /*  Intent intent = new Intent(DTRSelectActivity.this, DTRGISMapActivity.class);
                            intent.putExtra("DTRCODE",dtr_Code);
                            // intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            startActivity(intent);
                            finish();*/
                            getIncrementalDTRDetails(dtr_Code,dtrLocation);
                        }

                    }else if(flag==2){
                        if("Select".equalsIgnoreCase(dtrCode)){
                            Toast.makeText(IncrementalDTRSelectActivity.this,"Select DTR Code",Toast.LENGTH_SHORT).show();
                        }else if("Select".equalsIgnoreCase(ltCircuit)){
                            Toast.makeText(IncrementalDTRSelectActivity.this,"Select LT Circuit",Toast.LENGTH_SHORT).show();
                        }else {
                            if(ConnectivityChecking.checkConnection(IncrementalDTRSelectActivity.this)){
                               // incrementalSurveyedData();
                                getWorkOrderStatus();
                            }else {
                                Toast.makeText(IncrementalDTRSelectActivity.this,MobileConstants.INTERNET,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else {
                    Toast.makeText(IncrementalDTRSelectActivity.this,"Please Selecty SurveyType",Toast.LENGTH_SHORT).show();
                }


                /*Intent intent=new Intent(DTRSelectActivity.this,LineTypeActivity.class);
                startActivity(intent);
                finish();*/
                break;

        }
    }

    private void getIncrementalDTRDetails(String dtr_code, String dtrLocation) {
       /* DTRDetailsReqestVo dtrDetailsReqestVo=new DTRDetailsReqestVo();
        dtrDetailsReqestVo.setDTR_CODE(dtr_code);
        dtrDetailsReqestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(DTRSelectActivity.this));
        dtrDetailsReqestVo.setVERSION(MobileConstants.APP_VERSION);*/
        JsonObject dtrDetailsReqestVo=new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE",dtr_code);
        dtrDetailsReqestVo.addProperty("IMEI_NO",CommanUtilits.getIMEI_NO(IncrementalDTRSelectActivity.this));
        dtrDetailsReqestVo.addProperty("VERSION",MobileConstants.APP_VERSION);

        progressDialog.show();
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getDTRData(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if(response.body() !=null) {

                    JsonObject surveyedResponseVo= response.body();
                  //  Log.v("DTRDataRESPONSE==",""+surveyedResponseVo);
                    if(MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonObject responseObj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            arcGISDatabase.clearIncrementalDTRDetailsData();
                            DTRDetailsResponseVo dtrDetailsResponseVo = new DTRDetailsResponseVo();
                            dtrDetailsResponseVo.setDTR_CAPACITY(responseObj.get("DTR_CAPACITY").getAsString());
                            dtrDetailsResponseVo.setDTR_CODE(responseObj.get("DTR_CODE").getAsString());
                            dtrDetailsResponseVo.setDTR_MAKE(responseObj.get("DTR_MAKE").getAsString());
                            dtrDetailsResponseVo.setDTR_PHASE(responseObj.get("DTR_PHASE").getAsString());
                            dtrDetailsResponseVo.setDTR_SNO(responseObj.get("DTR_SNO").getAsString());
                            dtrDetailsResponseVo.setTYPE_OF_LOAD(responseObj.get("TYPE_OF_LOAD").getAsString());

                            arcGISDatabase.addIncrementalDTRDetailsData(dtrDetailsResponseVo);

                            Intent intent = new Intent(IncrementalDTRSelectActivity.this, DTRGISMapActivity.class);
                            intent.putExtra("DTRCODE", dtr_Code);
                       /* intent.putExtra("DTR_LOCATION",dtrLocation);
                        intent.putExtra("DTR_SNO",surveyedResponseVo.getDTR_SNO());
                        intent.putExtra("DTR_MAKE",surveyedResponseVo.getDTR_MAKE());
                        intent.putExtra("DTR_CAPACITY",surveyedResponseVo.getDTR_CAPACITY());
                        intent.putExtra("DTR_PHASE",surveyedResponseVo.getDTR_PHASE());
                        intent.putExtra("TYPE_OF_LOAD",surveyedResponseVo.getTYPE_OF_LOAD());*/
                            // intent.putExtra("MAX_COUNT", surveyedResponseVo.get("SESSION_ID").getAsString());
                            startActivity(intent);
                            finish();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(IncrementalDTRSelectActivity.this,surveyedResponseVo.get("ERR_MSG").toString(),Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(IncrementalDTRSelectActivity.this,"Server Busy Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(IncrementalDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }




//Needd To Call WorkOrder Status
   /* public void incrementalSurveyedData(){
        progressDialog.show();
        List<LoginDataResponse> loginDataResponses=arcGISDatabase.getUserMasterData();
        IncrementalSurveyedDataReqVo surveyedDataReqVo=new IncrementalSurveyedDataReqVo();
        List<String>  poleNumber = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT TAPPING_POLE FROM INCREMENTAL_DTR_TABLE WHERE WORK_ORDERID='%s'", lt_workorder));
        if (dtrCode.contains("-")) {
            String[] dtrcode = dtrCode.split("-");
            dtr_Code= dtrcode[0]; // 004
            // String ssName = poleNum[1];
            surveyedDataReqVo.setDTR_STRUCTURE(dtr_Code);
        } else {
            surveyedDataReqVo.setDTR_STRUCTURE(dtrCode);
        }

        surveyedDataReqVo.setLT_CKTCODE(ltCircuit);
        surveyedDataReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(IncrementalDTRSelectActivity.this));
        surveyedDataReqVo.setVERSION(MobileConstants.APP_VERSION);
        surveyedDataReqVo.setSESSION_ID(sessionId);
        surveyedDataReqVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
        surveyedDataReqVo.setPOLE_NO(poleNumber.get(0));
        surveyedDataReqVo.setWO_ID(lt_workorder);

        Gson gson=new Gson();
        String str=gson.toJson(surveyedDataReqVo);
        Log.v("incrementalDATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<SurveyedResponseVo> responseVoCall = retrofitService.getIncrementalsurveyedData(surveyedDataReqVo);
        responseVoCall.enqueue(new Callback<SurveyedResponseVo>() {
            @Override
            public void onResponse(Call<SurveyedResponseVo> call, Response<SurveyedResponseVo> response) {
                progressDialog.dismiss();
                if(response.body() !=null) {
                    SurveyedResponseVo surveyedResponseVo= response.body();
                    Gson gson=new Gson();
                    String str=gson.toJson(surveyedResponseVo);
                    Log.v("SurveyedResponseVo===>>", "" + str);
                    if(MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.getSTATUS())){

                        if ("NO_DATA".equalsIgnoreCase(surveyedResponseVo.getERR_MSG())) {
                            Intent intent = new Intent(IncrementalDTRSelectActivity.this, IncrementalLineTypeActivity.class);
                            intent.putExtra("DTRCODE",dtr_Code);
                            intent.putExtra("CIRCUIT_CODE",ltCircuit);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("WO_ID",lt_workorder);
                            startActivity(intent);
                            finish();
                        } else {
                            arcGISDatabase.clearIncrementalSurveyData();
                            List<SurveyedResponseDataVo> surveyedResponseDataVoList=surveyedResponseVo.getRESPONSE();
                            for (SurveyedResponseDataVo vo : surveyedResponseDataVoList) {
                                IncrementalSurveyDataVo surveyVo = new IncrementalSurveyDataVo();
                                surveyVo.setPOLE_NO(Integer.parseInt(vo.getPOLE_NO()));
                                surveyVo.setFEEDER_ID(vo.getFEEDER_ID());
                                surveyVo.setLAT(vo.getGLAT());
                                surveyVo.setLAN(vo.getGLONG());
                                surveyVo.setSURVEY_FLAG("Y");
                                arcGISDatabase.addIncrementalSurveyData(surveyVo);
                            }
                            Intent intent = new Intent(IncrementalDTRSelectActivity.this, IncrementalLineTypeActivity.class);
                            intent.putExtra("DTRCODE",dtr_Code);
                            intent.putExtra("CIRCUIT_CODE",ltCircuit);
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            intent.putExtra("WO_ID",lt_workorder);
                            startActivity(intent);
                            startActivity(intent);
                            finish();
                        }



                    }else {
                        Toast.makeText(IncrementalDTRSelectActivity.this,surveyedResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(IncrementalDTRSelectActivity.this,"Server Busy Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SurveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(IncrementalDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }*/

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(IncrementalDTRSelectActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.ch_dtrsurvey:
                if(ch_dtrsurvey.isChecked()){
                    flag=1;
                    ch_ltsurvey.setChecked(false);
                    lyt_dtrselect.setVisibility(View.VISIBLE);
                    ///lyt_dtr_workorder.setVisibility(View.VISIBLE);
                    lyt_ltcircuitselect.setVisibility(View.GONE);
                    lyt_lt_workorder.setVisibility(View.GONE);
                }
                break;
            case R.id.ch_ltsurvey:
                if(ch_ltsurvey.isChecked()){
                    flag=2;
                    ch_dtrsurvey.setChecked(false);
                    lyt_ltcircuitselect.setVisibility(View.VISIBLE);
                    lyt_dtrselect.setVisibility(View.VISIBLE);
                    lyt_lt_workorder.setVisibility(View.VISIBLE);
                }
                break;



        }
    }





    public void getWorkOrderStatus(){
        progressDialog.show();
        List<LoginDataResponse> loginDataResponses=arcGISDatabase.getUserMasterData();
        IncrementalSurveyedDataReqVo surveyedDataReqVo=new IncrementalSurveyedDataReqVo();
        List<String>  feederId = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_ID FROM INCREMENTAL_DTR_TABLE WHERE LT_CKTCODE='%s'", ltCircuit));
        if (dtrCode.contains("-")) {
            String[] dtrcode = dtrCode.split("-");
            dtr_Code= dtrcode[0]; // 004
            // String ssName = poleNum[1];
            surveyedDataReqVo.setDTR_STRUCTURE(dtr_Code);
        } else {
            surveyedDataReqVo.setDTR_STRUCTURE(dtrCode);
        }

        List<LoginDataResponse> userMasterData=arcGISDatabase.getUserMasterData();
        JsonObject postParam = new JsonObject();
        //postParam.addProperty("order",yourArray) ;
        postParam.addProperty("DTR_STRUCTURE", dtr_Code);
         postParam.addProperty("FEEDER_ID", feederId.get(0));
        postParam.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(IncrementalDTRSelectActivity.this));
        postParam.addProperty("LT_CKTCODE", ltCircuit);
        postParam.addProperty("SEC_CODE", userMasterData.get(0).getSEC_CODE());
        postParam.addProperty("SESSION_ID", sessionId);
        postParam.addProperty("VERSION", MobileConstants.APP_VERSION);
        postParam.addProperty("WO_ID",lt_workorder);



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

                        arcGISDatabase.clearWOStatusMasterData();


                        JsonObject dataObj=jsonObject.get("RESPONSE").getAsJsonObject();
                        WoStatusMasterVo woStatusMasterVo=new WoStatusMasterVo();

                         if(ltCircuit.equalsIgnoreCase(dataObj.get("LT_DTR_CKT_CODE").getAsString())){
                            if(lt_workorder.equalsIgnoreCase(dataObj.get("WO_ID").getAsString())) {
                                woStatusMasterVo.setFEEDER_ID(dataObj.get("FEEDER_ID").getAsString());
                                woStatusMasterVo.setWO_ID(dataObj.get("WO_ID").getAsString());
                                woStatusMasterVo.setWO_STATUS(dataObj.get("WO_STATUS").getAsString());
                                arcGISDatabase.addWoStatusMasterData(woStatusMasterVo);
                                Intent intent = new Intent(IncrementalDTRSelectActivity.this, IncrementalTopTenActivity.class);
                                //intent.putExtra("SSNAME", subName);
                               // intent.putExtra("FEEDER_NAME", feedeNameList);
                                intent.putExtra("FEEDER_CODE", feederId.get(0));
                                intent.putExtra("DTRCODE",dtr_Code);
                                intent.putExtra("CIRCUIT_CODE",ltCircuit);
                                // intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                intent.putExtra("WORKORDER", lt_workorder);
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
                        arcGISDatabase.clearWOStatusMasterData();
                        if("V001".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())){
                            WoStatusMasterVo woStatusMasterVo=new WoStatusMasterVo();
                            woStatusMasterVo.setFEEDER_ID(feederId.get(0));
                            woStatusMasterVo.setWO_ID(lt_workorder);
                            woStatusMasterVo.setLT_CKT(ltCircuit);
                            woStatusMasterVo.setDTR_STRCODE(dtr_Code);
                            woStatusMasterVo.setWO_STATUS("N");
                            arcGISDatabase.addWoStatusMasterData(woStatusMasterVo);
                            Intent intent = new Intent(IncrementalDTRSelectActivity.this, IncrementalTopTenActivity.class);
                            //intent.putExtra("SSNAME", subName);
                           // intent.putExtra("FEEDER_NAME", feedeNameList);
                            intent.putExtra("FEEDER_CODE", feederId.get(0));
                            intent.putExtra("DTRCODE",dtr_Code);
                            intent.putExtra("CIRCUIT_CODE",ltCircuit);
                            // intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                            intent.putExtra("WORKORDER", lt_workorder);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(IncrementalDTRSelectActivity.this,jsonObject.get("ERR_MSG").getAsString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(IncrementalDTRSelectActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(IncrementalDTRSelectActivity.this, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
