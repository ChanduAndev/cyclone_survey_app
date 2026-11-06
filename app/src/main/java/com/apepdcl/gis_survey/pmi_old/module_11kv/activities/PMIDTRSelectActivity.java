package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIUpdatedDTRVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.model.PMIFeederMasterModel;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitService;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRDetailsResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIGis11KvSurveyDTRVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiCktVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedDataReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedResponseVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMIDTRSelectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Spinner sp_dtrcode, sp_circuit,sp_dtrcode_lt;
    private Button btn_next;
    private List<PMILTCktsResponseDataVo> ltCktsResponseDataVos;
    private PMIArcGISDatabase arcGISDatabase;
    private List<String> dtrCodeList,dtrCodeListLt;
    private List<String> completedDtrCodeList;
    private List<String> ltCircuitList;
    private String dtrCode, ltCircuit, sessionId;
    private SharedPreferences sharedPreferences;
    private PMIRetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String dtr_Code, dtrLocation;
    private CheckBox ch_dtrsurvey, ch_ltsurvey;
    private LinearLayout lyt_dtrselect, lyt_ltcircuitselect,lyt_dtrselect_lt;
    private int flag = 0;
    private int alert_flag = 0;
    List<String> ltCktsFeederId=null;

    boolean isDTRSurveyed = false;

    //MULTI CIRCUIT
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

    //UPDATED DTR DATA
    public static PMIUpdatedDTRVO dtrvo;
    public static PMIUpdatedDTRVO not_updated;

    //MULTI CKT REQUIRED
    LinearLayout lyt_is_multi_circuit;
    Spinner sp_is_multi_circuit;

    //COMPLETE SURVEY
    Spinner sp_complete_dtrcode;
    Button complete_survey;
    String complete_dtr_code;
    String alertDesc;
    boolean isOnlyDTR = false;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmidtrselect);

        try {
            setContentView(R.layout.activity_dtrselection);
            sp_complete_dtrcode = findViewById(R.id.sp_complete_dtrcode);
            complete_survey = findViewById(R.id.complete_survey);
            sp_dtrcode_lt = findViewById(R.id.sp_dtrcode_lt);
            lyt_dtrselect_lt = findViewById(R.id.lyt_dtrselect_lt);


            sp_dtrcode = findViewById(R.id.sp_dtrcode);
            sp_circuit = findViewById(R.id.sp_circuit);
            ch_dtrsurvey = findViewById(R.id.ch_dtrsurvey);
            ch_ltsurvey = findViewById(R.id.ch_ltsurvey);
            btn_next = findViewById(R.id.btn_next);
            lyt_dtrselect = findViewById(R.id.lyt_dtrselect);
            lyt_ltcircuitselect = findViewById(R.id.lyt_ltcircuitselect);
            lyt_is_multi_circuit = findViewById(R.id.lyt_is_multi_circuit);
            sp_is_multi_circuit = findViewById(R.id.sp_is_multi_circuit);
            lyt_dtrselect.setVisibility(View.GONE);
            lyt_ltcircuitselect.setVisibility(View.GONE);
            lyt_is_multi_circuit.setVisibility(View.GONE);
            sp_dtrcode.setOnItemSelectedListener(this);
            sp_circuit.setOnItemSelectedListener(this);
            sp_complete_dtrcode.setOnItemSelectedListener(this);
            sp_dtrcode_lt.setOnItemSelectedListener(this);
            ch_dtrsurvey.setOnCheckedChangeListener(this);
            ch_ltsurvey.setOnCheckedChangeListener(this);
            btn_next.setOnClickListener(this);
            complete_survey.setOnClickListener(this);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            ltCktsFeederId=new ArrayList<>();
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

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("LT Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(PMIDTRSelectActivity.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            // setSupportActionBar(toolbar);
            arcGISDatabase = new PMIArcGISDatabase(this);
            ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData("");
            dtrCodeList = arcGISDatabase.getDTRCode();


            dtrCodeList.add("Select");
            Collections.reverse(dtrCodeList);

            dtrCodeListLt = arcGISDatabase.getDTRCodeLT();
            dtrCodeListLt.add("Select");
            Collections.reverse(dtrCodeListLt);

            ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(PMIDTRSelectActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, dtrCodeList);
            sp_dtrcode.setAdapter(ehtSSadapter);

            ArrayAdapter<String> ehtSSadapterLT = new ArrayAdapter<String>(PMIDTRSelectActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, dtrCodeListLt);
            sp_dtrcode_lt.setAdapter(ehtSSadapterLT);


          /*  completedDtrCodeList = new ArrayList<>();
            completedDtrCodeList.add("Select");
            for (int i = 0; i < HomeActivity.list.size(); i++) {
                if (HomeActivity.list.get(i).getIS_LTCKT_AVAILABLE().equals("YES")){
                    completedDtrCodeList.add(HomeActivity.list.get(i).getDTR_CODE()+"-"+HomeActivity.list.get(i).getDTR_LOCATION());
                }
            }
            Collections.reverse(completedDtrCodeList);*/

            ArrayAdapter<String> completeAdapter = new ArrayAdapter<String>(PMIDTRSelectActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, dtrCodeList);
            sp_complete_dtrcode.setAdapter(completeAdapter);


            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PMIDTRSelectActivity.this);
            sessionId = sharedPreferences.getString(MobileConstants.SESSION_ID, "");



        } catch (Exception e) {
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.sp_dtrcode_lt:

                dtrCode = sp_dtrcode_lt.getSelectedItem().toString();
                ltCircuitList = new ArrayList<>();
                if (!"Select".equalsIgnoreCase(dtrCode)) {
                    // ltCktsFeederId = arcGISDatabase.getDTRMasterData("");
                    //ltCktsFeederId = arcGISDatabase.getDTRMasterData(String.format("SELECT * FROM DTR_TABLE WHERE DTR_CODE=%s",dtrCode));

                    //Collections.reverse(feederNameList);
                    if (dtrCode.contains("-")) {
                        String[] dtrcode = dtrCode.split("-");
                        String dtr_Code1 = dtrcode[0]; // 004
                        System.out.println("DTR CODE IN DTR : "+dtr_Code1);
                        // String ssName = poleNum[1];
                        //ltCktsFeederId = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_ID FROM DTR_TABLE WHERE DTR_CODE='%s'",dtr_Code1));
                        //  ltCircuitList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT LT_CKTCODE FROM DTR_TABLE WHERE DTR_CODE='%s'", dtr_Code1));
                        ltCircuitList = arcGISDatabase.getComplaint("SELECT DISTINCT LT_CKTCODE FROM DTR_TABLE WHERE DTR_CODE='"+dtr_Code1+"' AND LT_CKTCODE IS NOT NULL");
                        ltCircuitList.add("Select");
                        Collections.reverse(ltCircuitList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMIDTRSelectActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                        sp_circuit.setAdapter(adapter);

                    } else {
                        //ltCircuitList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT LT_CKTCODE FROM DTR_TABLE WHERE DTR_CODE='%s'", dtrCode));
                        ltCircuitList = arcGISDatabase.getComplaint("SELECT DISTINCT LT_CKTCODE FROM DTR_TABLE WHERE DTR_CODE='"+dtrCode+"' AND LT_CKTCODE IS NOT NULL");
                        ltCircuitList.add("Select");
                        Collections.reverse(ltCircuitList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMIDTRSelectActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                        sp_circuit.setAdapter(adapter);
                    }

                } else {
                    ltCircuitList.add("Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMIDTRSelectActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                    sp_circuit.setAdapter(adapter);
                }

                break;

            case R.id.sp_dtrcode:
                dtrCode = sp_dtrcode.getSelectedItem().toString();
                break;

            case R.id.sp_circuit:
                ltCircuit = sp_circuit.getSelectedItem().toString();
                break;

            case R.id.sp_complete_dtrcode:
                complete_dtr_code = sp_complete_dtrcode.getSelectedItem().toString();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:


                if (ch_dtrsurvey.isChecked() || ch_ltsurvey.isChecked()) {
                    if (flag == 1) {
                        if ("Select".equalsIgnoreCase(dtrCode)) {
                            Toast.makeText(PMIDTRSelectActivity.this, "Select DTR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] dtrcode = dtrCode.split("-");
                            dtr_Code = dtrcode[0];
                            dtrLocation = dtrcode[1];
                          /*  Intent intent = new Intent(DTRSelectActivity.this, DTRGISMapActivity.class);
                            intent.putExtra("DTRCODE",dtr_Code);
                            // intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            startActivity(intent);
                            finish();*/
                            getDTRDetails(dtr_Code, dtrLocation);
                        }

                    } else if (flag == 2) {

                        if ("Select".equalsIgnoreCase(dtrCode)) {
                            Toast.makeText(PMIDTRSelectActivity.this, "Select DTR Code", Toast.LENGTH_SHORT).show();
                        } else if ("Select".equalsIgnoreCase(ltCircuit)) {
                            Toast.makeText(PMIDTRSelectActivity.this, "Select LT Circuit", Toast.LENGTH_SHORT).show();
                        } else if ("Select".equalsIgnoreCase(sp_is_multi_circuit.getSelectedItem().toString())) {
                            Toast.makeText(PMIDTRSelectActivity.this, "Select Multi Circuit required", Toast.LENGTH_SHORT).show();
                        }else {



                            if (ConnectivityChecking.checkConnection(PMIDTRSelectActivity.this)) {
                                String[] dtrcode = dtrCode.split("-");
                                String selected_dtr_Code = dtrcode[0];

                                for (int i = 0; i < PMIHomeActivity.list.size(); i++) {
                                    if (PMIHomeActivity.list.get(i).getDTR_CODE().equals(selected_dtr_Code)
                                            && PMIHomeActivity.list.get(i).getIS_LTCKT_AVAILABLE().equals("NO")){
                                        Toast.makeText(PMIDTRSelectActivity.this, "LT Circuit doesn't available for the DTR", Toast.LENGTH_SHORT).show();
                                        break;
                                    }else {
                                        checkDTRSurvey(selected_dtr_Code,"lt");
                                        break;
                                    }
                                }




                            } else {
                                Toast.makeText(PMIDTRSelectActivity.this, MobileConstants.INTERNET, Toast.LENGTH_SHORT).show();
                            }
                        }







                    }
                } else {
                    Toast.makeText(PMIDTRSelectActivity.this, "Please Select SurveyType", Toast.LENGTH_SHORT).show();
                }


                /*Intent intent=new Intent(DTRSelectActivity.this,LineTypeActivity.class);
                startActivity(intent);
                finish();*/
                break;

            case R.id.complete_survey:

                if ("Select".equalsIgnoreCase(complete_dtr_code)) {
                    Toast.makeText(PMIDTRSelectActivity.this, "Select DTR Code", Toast.LENGTH_SHORT).show();
                } else {
                    alertDesc = "Is  Survey for all poles of the DTR completed (for all Circuits ) ?";
                    isOnlyDTR = false;
                    String[] dtrcode = complete_dtr_code.split("-");
                    complete_dtr_code = dtrcode[0];
                    System.out.println("COMPLETED DTR CODE : "+complete_dtr_code);

                    for (int i = 0; i < PMIHomeActivity.list.size(); i++) {
                        System.out.println("DTR CODE : "+PMIHomeActivity.list.get(i).getDTR_CODE());
                        System.out.println("LTCKT_AVAILABLE : "+PMIHomeActivity.list.get(i).getIS_LTCKT_AVAILABLE());
                        if (PMIHomeActivity.list.get(i).getDTR_CODE().equals(complete_dtr_code)
                                && PMIHomeActivity.list.get(i).getIS_LTCKT_AVAILABLE().equals("NO")){
                            alertDesc = "Is  Survey for the DTR completed?";
                            isOnlyDTR = true;

                        }
                    }

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRSelectActivity.this);
                    alertDialogBuilder.setTitle("Alert!!!");
                    alertDialogBuilder
                            .setMessage(Html.fromHtml(alertDesc ))
                            .setCancelable(false)
                            .setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            updateSurveyStatus(complete_dtr_code);
                                        }
                                    })

                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    progressDialog.dismiss();

                                    dialog.cancel();

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
                break;

        }
    }

    private void updateSurveyStatus(String complete_dtr_code) {
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", complete_dtr_code);
        dtrDetailsReqestVo.addProperty("USER_ID", preferences.getString(MobileConstants.USERNAME, ""));
        //  dtrDetailsReqestVo.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(DTRSelectActivity.this));
        //  dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.APP_VERSION);

        progressDialog.show();
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.updateSurveyStatus(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("DTRDataRESPONSE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            Toast.makeText(PMIDTRSelectActivity.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                            finish();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRSelectActivity.this);
                        alertDialogBuilder.setTitle( isOnlyDTR ? "Alert!!" : "No of Poles Surveyed = 0");
                        alertDialogBuilder
                                .setMessage(Html.fromHtml(surveyedResponseVo.get("ERR_MSG").toString() ))
                                .setCancelable(false)
                                .setPositiveButton("OKAY",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        })

                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //  Toast.makeText(DTRSelectActivity.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIDTRSelectActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMIDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkDTRSurvey(String selected_dtr_code, String type) {
        progressDialog.show();
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", selected_dtr_code);
        dtrDetailsReqestVo.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PMIDTRSelectActivity.this));
        dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.APP_VERSION);
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.checkDTRSurvey(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("DTRDataRESPONSE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        if (type.equals("lt")){
                            getMasterFeederData();

                        }else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRSelectActivity.this);

                            alertDialogBuilder.setTitle("Alert!!!");
                            //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                            alertDialogBuilder
                                    .setMessage(Html.fromHtml("Survey for this DTR has been completed. Do you want to update the existing Survey?" ))
                                    .setCancelable(false)
                                    .setPositiveButton("YES",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    getUpdatedDTRSurveyData(selected_dtr_code);
                                                   /* Intent intent = new Intent(DTRSelectActivity.this, DTRSurveyActivity.class);
                                                    intent.putExtra("DTRCODE", dtr_Code);
                                                    intent.putExtra("DTR_LOCATION", dtrLocation);
                                                    intent.putExtra("type", "updated");
                                                    startActivity(intent);*/

                                                }
                                            })

                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            progressDialog.dismiss();
                                            dialog.cancel();

                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }

                    } else {
                        if (type.equals("lt")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRSelectActivity.this);

                            alertDialogBuilder.setTitle("Alert!!!");
                            //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                            alertDialogBuilder
                                    .setMessage(Html.fromHtml(" Please  Complete  DTR Survey to Start LT Survey" ))
                                    .setCancelable(false)
                                    .setPositiveButton("OKAY",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    progressDialog.dismiss();
                                                }
                                            })

                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            progressDialog.dismiss();

                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                        else {
                            progressDialog.dismiss();
                            Intent intent = new Intent(PMIDTRSelectActivity.this, PMIDTRSurveyActivity.class);
                            intent.putExtra("DTRCODE", dtr_Code);
                            intent.putExtra("DTR_LOCATION", dtrLocation);
                            intent.putExtra("type", "new");
                            startActivity(intent);
                        }

                    }


                } else {
                    Toast.makeText(PMIDTRSelectActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMIDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getUpdatedDTRSurveyData(String selected_dtr_code) {

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", selected_dtr_code);
        dtrDetailsReqestVo.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PMIDTRSelectActivity.this));
        dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.APP_VERSION);
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.updatedDTRDetails(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("DTRDataRESPONSE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        JsonObject responseObj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                        dtrvo = new PMIUpdatedDTRVO();
                        dtrvo.setSEC_CODE(responseObj.get("SEC_CODE").getAsString());
                        dtrvo.setFEEDER_ID(responseObj.get("FEEDER_ID").getAsString());
                        dtrvo.setDTR_MAKE(responseObj.get("DTR_MAKE").getAsString());
                        dtrvo.setDTR_SL_NO(responseObj.get("DTR_SL_NO").getAsString());
                        dtrvo.setDTR_STRUC_CODE(responseObj.get("DTR_STRUC_CODE").getAsString());
                        dtrvo.setDTR_LOAD_TYPE(responseObj.get("DTR_LOAD_TYPE").getAsString());
                        dtrvo.setDTR_PHASE(responseObj.get("DTR_PHASE").getAsString());
                        dtrvo.setDTR_CAPACITY(responseObj.get("DTR_CAPACITY").getAsString());
                        dtrvo.setDTR_LOCATION(responseObj.get("DTR_LOCATION").getAsString());
                        dtrvo.setDTR_STRUC_TYPE(responseObj.get("DTR_STRUC_TYPE").getAsString());
                        dtrvo.setRECORD_STATUS(responseObj.get("RECORD_STATUS").isJsonNull()? "" :responseObj.get("RECORD_STATUS").getAsString());

                        dtrvo.setNO_OF_LTCKT(responseObj.get("NO_OF_LTCKT").isJsonNull()? "" :responseObj.get("NO_OF_LTCKT").getAsString());
                        dtrvo.setDTR_ABSWITCH(responseObj.get("DTR_ABSWITCH").isJsonNull()? "" :responseObj.get("DTR_ABSWITCH").getAsString());
                        dtrvo.setDTR_HGFUSE(responseObj.get("DTR_HGFUSE").isJsonNull()? "" :responseObj.get("DTR_HGFUSE").getAsString());
                        dtrvo.setABSWITCH_TO_HGFUSE_BUSBAR(responseObj.get("ABSWITCH_TO_HGFUSE_BUSBAR").isJsonNull()? "" :responseObj.get("ABSWITCH_TO_HGFUSE_BUSBAR").getAsString());
                        dtrvo.setABS_COUNDUCTOR_SIZE(responseObj.get("ABS_COUNDUCTOR_SIZE").isJsonNull()? "" :responseObj.get("ABS_COUNDUCTOR_SIZE").getAsString());
                        dtrvo.setHGFUSE_TO_DTR_BUSBAR(responseObj.get("HGFUSE_TO_DTR_BUSBAR").isJsonNull()? "" :responseObj.get("HGFUSE_TO_DTR_BUSBAR").getAsString());
                        dtrvo.setHGF_COUNDUCTOR_SIZE(responseObj.get("HGF_COUNDUCTOR_SIZE").isJsonNull()? "" :responseObj.get("HGF_COUNDUCTOR_SIZE").getAsString());
                        dtrvo.setEARTH_PITS(responseObj.get("EARTH_PITS").isJsonNull()? "" :responseObj.get("EARTH_PITS").getAsString());
                        dtrvo.setCI_PIPES(responseObj.get("CI_PIPES").isJsonNull()? "" :responseObj.get("CI_PIPES").getAsString());
                        dtrvo.setGI_PIPES(responseObj.get("GI_PIPES").isJsonNull()? "" :responseObj.get("GI_PIPES").getAsString());
                        dtrvo.setNO_OF_DTR_METERS(responseObj.get("NO_OF_DTR_METERS").isJsonNull()? "" :responseObj.get("NO_OF_DTR_METERS").getAsString());
                        dtrvo.setMTR1_SL_NO(responseObj.get("MTR1_SL_NO").isJsonNull()? "" :responseObj.get("MTR1_SL_NO").getAsString());
                        dtrvo.setMTR1_MAKE(responseObj.get("MTR1_MAKE").isJsonNull()? "" :responseObj.get("MTR1_MAKE").getAsString());
                        dtrvo.setMTR1_CAPACITY(responseObj.get("MTR1_CAPACITY").isJsonNull()? "" :responseObj.get("MTR1_CAPACITY").getAsString());
                        dtrvo.setMTR1_TYPE(responseObj.get("MTR1_TYPE").isJsonNull()? "" :responseObj.get("MTR1_TYPE").getAsString());
                        dtrvo.setMTR2_SL_NO(responseObj.get("MTR2_SL_NO").isJsonNull()? "" :responseObj.get("MTR2_SL_NO").getAsString());
                        dtrvo.setMTR2_MAKE(responseObj.get("MTR2_MAKE").isJsonNull()? "" :responseObj.get("MTR2_MAKE").getAsString());
                        dtrvo.setMTR2_CAPACITY(responseObj.get("MTR2_CAPACITY").isJsonNull()? "" :responseObj.get("MTR2_CAPACITY").getAsString());
                        dtrvo.setMTR2_TYPE(responseObj.get("MTR2_TYPE").isJsonNull()? "" :responseObj.get("MTR2_TYPE").getAsString());
                        dtrvo.setMTR3_SL_NO(responseObj.get("MTR3_SL_NO").isJsonNull()? "" :responseObj.get("MTR3_SL_NO").getAsString());

                        dtrvo.setMTR3_MAKE(responseObj.get("MTR3_MAKE").isJsonNull()? "" :responseObj.get("MTR3_MAKE").getAsString());
                        dtrvo.setMTR3_CAPACITY(responseObj.get("MTR3_CAPACITY").isJsonNull()? "" :responseObj.get("MTR3_CAPACITY").getAsString());
                        dtrvo.setMTR3_TYPE(responseObj.get("MTR3_TYPE").isJsonNull()? "" :responseObj.get("MTR3_TYPE").getAsString());
                        dtrvo.setMODEM1_AVL(responseObj.get("MODEM1_AVL").isJsonNull()? "" :responseObj.get("MODEM1_AVL").getAsString());
                        dtrvo.setMODEM1_SLNO(responseObj.get("MODEM1_SLNO").isJsonNull()? "" :responseObj.get("MODEM1_SLNO").getAsString());
                        dtrvo.setMODEM1_MAKE(responseObj.get("MODEM1_MAKE").isJsonNull()? "" :responseObj.get("MODEM1_MAKE").getAsString());
                        dtrvo.setDTR_POLE(responseObj.get("DTR_POLE").isJsonNull()? "" :responseObj.get("DTR_POLE").getAsString());
                        dtrvo.setPIN(responseObj.get("PIN").isJsonNull()? "" :responseObj.get("PIN").getAsString());
                        dtrvo.setCUT(responseObj.get("CUT").isJsonNull()? "" :responseObj.get("CUT").getAsString());
                        dtrvo.setLT_FUSE(responseObj.get("LT_FUSE").isJsonNull()? "" :responseObj.get("LT_FUSE").getAsString());
                        dtrvo.setLT_BUS_BAR(responseObj.get("LT_BUS_BAR").isJsonNull()? "" :responseObj.get("LT_BUS_BAR").getAsString());
                        dtrvo.setLT_BUSBAR_CONDUTORSIZE(responseObj.get("LT_BUSBAR_CONDUTORSIZE").isJsonNull()? "" :responseObj.get("LT_BUSBAR_CONDUTORSIZE").getAsString());
                        dtrvo.setLT_BUSBAR_CABLESIZE(responseObj.get("LT_BUSBAR_CABLESIZE").isJsonNull()? "" :responseObj.get("LT_BUSBAR_CABLESIZE").getAsString());
                        dtrvo.setGLAT(responseObj.get("GLAT").isJsonNull()? "" :responseObj.get("GLAT").getAsString());
                        dtrvo.setGLONG(responseObj.get("GLONG").isJsonNull()? "" :responseObj.get("GLONG").getAsString());
                        dtrvo.setSURVEY_STATUS(responseObj.get("SURVEY_STATUS").isJsonNull()? "" :responseObj.get("SURVEY_STATUS").getAsString());
                        dtrvo.setSURVEY_DATE(responseObj.get("SURVEY_DATE").isJsonNull()? "" :responseObj.get("SURVEY_DATE").getAsString());
                        dtrvo.setDTR_POLE_SUPPORT(responseObj.get("DTR_POLE_SUPPORT").isJsonNull()? "" :responseObj.get("DTR_POLE_SUPPORT").getAsString());
                        dtrvo.setNOOF_STURT(responseObj.get("NOOF_STURT").isJsonNull()? "" :responseObj.get("NOOF_STURT").getAsString());
                        dtrvo.setNOOF_STAY(responseObj.get("NOOF_STAY").isJsonNull()? "" :responseObj.get("NOOF_STAY").getAsString());
                        dtrvo.setDTR_ABSWTICH_TYPE(responseObj.get("DTR_ABSWTICH_TYPE").isJsonNull()? "" :responseObj.get("DTR_ABSWTICH_TYPE").getAsString());
                        dtrvo.setNOOF_PININSULATORS(responseObj.get("NOOF_PININSULATORS").isJsonNull()? "" :responseObj.get("NOOF_PININSULATORS").getAsString());
                        dtrvo.setNOOF_DISCINSULATORS(responseObj.get("NOOF_DISCINSULATORS").isJsonNull()? "" :responseObj.get("NOOF_DISCINSULATORS").getAsString());
                        dtrvo.setDTR_LANDMARK(responseObj.get("DTR_LANDMARK").isJsonNull()? "" :responseObj.get("DTR_LANDMARK").getAsString());

                        dtrvo.setMODEM2_AVL(responseObj.get("MODEM2_AVL").isJsonNull()? "" :responseObj.get("MODEM2_AVL").getAsString());
                        dtrvo.setMODEM2_SLNO(responseObj.get("MODEM2_SLNO").isJsonNull()? "" :responseObj.get("MODEM2_SLNO").getAsString());
                        dtrvo.setMODEM2_MAKE(responseObj.get("MODEM2_MAKE").isJsonNull()? "" :responseObj.get("MODEM2_MAKE").getAsString());
                        dtrvo.setMODEM3_AVL(responseObj.get("MODEM3_AVL").isJsonNull()? "" :responseObj.get("MODEM3_AVL").getAsString());
                        dtrvo.setMODEM3_SLNO(responseObj.get("MODEM3_SLNO").isJsonNull()? "" :responseObj.get("MODEM3_SLNO").getAsString());
                        dtrvo.setMODEM3_MAKE(responseObj.get("MODEM3_MAKE").isJsonNull()? "" :responseObj.get("MODEM3_MAKE").getAsString());
                        dtrvo.setDTR_MFG_DT(responseObj.get("DTR_MFG_DT").isJsonNull()? "" :responseObj.get("DTR_MFG_DT").getAsString());
                        dtrvo.setDTR_FENCING(responseObj.get("DTR_FENCING").isJsonNull()? "" :responseObj.get("DTR_FENCING").getAsString());
                        dtrvo.setNUM_EARTH(responseObj.get("NUM_EARTH").isJsonNull()? "" :responseObj.get("NUM_EARTH").getAsString());
                        dtrvo.setNUM_LT_BUSHINGS(responseObj.get("NUM_LT_BUSHINGS").isJsonNull()? "" :responseObj.get("NUM_LT_BUSHINGS").getAsString());
                        dtrvo.setNUM_HT_BUSHINGS(responseObj.get("NUM_HT_BUSHINGS").isJsonNull()? "" :responseObj.get("NUM_HT_BUSHINGS").getAsString());
                        dtrvo.setNUM_PG_LUG(responseObj.get("NUM_PG_LUG").isJsonNull()? "" :responseObj.get("NUM_PG_LUG").getAsString());
                        dtrvo.setNUM_ARRESTER(responseObj.get("NUM_ARRESTER").isJsonNull()? "" :responseObj.get("NUM_ARRESTER").getAsString());
                        dtrvo.setOIL_LEAK(responseObj.get("OIL_LEAK").isJsonNull()? "" :responseObj.get("OIL_LEAK").getAsString());
                        progressDialog.dismiss();
                        Intent intent = new Intent(PMIDTRSelectActivity.this, PMIDTRSurveyActivity.class);
                        intent.putExtra("DTRCODE", dtr_Code);
                        intent.putExtra("DTR_LOCATION", dtrLocation);
                        intent.putExtra("type", "updated");
                        startActivity(intent);

                    } else {
                        Toast.makeText(PMIDTRSelectActivity.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(PMIDTRSelectActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMIDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getMasterFeederData() {
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
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
                                String s_code = data.get("DTR_STRUCTURE_CODE").isJsonNull() ? "" : data.get("DTR_STRUCTURE_CODE").getAsString();
                                String s_loc = data.get("DTR_LOCATION").isJsonNull() ? "" : data.get("DTR_LOCATION").getAsString();
                                feederListLT.add(s_code + "~" +s_loc);
                            }

                            if (sp_is_multi_circuit.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                                getMultiCktData();
                            }else {
                                getSurveyedData();
                            }





                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(PMIDTRSelectActivity.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIDTRSelectActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMIDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMultiCktData() {

        String dtr_code = dtrCode;
        if (dtrCode.contains("-")) {
            String[] dtrcode = dtrCode.split("-");
            dtr_code = dtrcode[0]; // 004
        }

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", dtr_code);
        dtrDetailsReqestVo.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PMIDTRSelectActivity.this));
        dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.APP_VERSION);


        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
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
                            JsonArray el_kv_array = responseObj.get("_11kv").getAsJsonArray();
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

                            JsonArray tt_kv_array = responseObj.get("_33kv").getAsJsonArray();
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

                            JsonArray lt_kv_array = responseObj.get("lt").getAsJsonArray();
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
                        getSurveyedData();
                        //  Toast.makeText(DTRSelectActivity.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIDTRSelectActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMIDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterLTFeeders() {
        // List<String> originalListIdFeeder1 = new ArrayList<>();
        List<String> originalListNameFeeder1 = new ArrayList<>();

        for(int i =0; i < PMIDTRSelectActivity.listLT.size(); i++) {
            //originalListIdFeeder1.add(DTRSelectActivity.listLT.get(i).getMC_FEEDER_ID());
            originalListNameFeeder1.add(PMIDTRSelectActivity.listLT.get(i).getMC_FEEDER_NAME());
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
        getSurveyedData();
    }

    private void filter11KvFeeders() {
        /*List<String> originalListIdFeeder1 = new ArrayList<>();
        List<String> originalListNameFeeder1 = new ArrayList<>();

        for(int i =0; i < DTRSelectActivity.list11kv.size(); i++) {
            originalListIdFeeder1.add(DTRSelectActivity.list11kv.get(i).getMC_FEEDER_ID());
            originalListNameFeeder1.add(DTRSelectActivity.list11kv.get(i).getMC_FEEDER_NAME());
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

        for(int i =0; i < DTRSelectActivity.list33kv.size(); i++) {
            originalListIdFeeder1.add(DTRSelectActivity.list33kv.get(i).getMC_FEEDER_ID());
            originalListNameFeeder1.add(DTRSelectActivity.list33kv.get(i).getMC_FEEDER_NAME());
        }
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

    private void getDTRDetails(String dtr_code, String dtrLocation) {
       /* DTRDetailsReqestVo dtrDetailsReqestVo=new DTRDetailsReqestVo();
        dtrDetailsReqestVo.setDTR_CODE(dtr_code);
        dtrDetailsReqestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(DTRSelectActivity.this));
        dtrDetailsReqestVo.setVERSION(MobileConstants.APP_VERSION);*/
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", dtr_code);
        dtrDetailsReqestVo.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PMIDTRSelectActivity.this));
        dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.APP_VERSION);

        progressDialog.show();
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getDTRData(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("DTRDataRESPONSE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonObject responseObj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            arcGISDatabase.clearDTRDetailsData();
                            PMIDTRDetailsResponseVo dtrDetailsResponseVo = new PMIDTRDetailsResponseVo();
                            dtrDetailsResponseVo.setDTR_CAPACITY(responseObj.get("DTR_CAPACITY").getAsString());
                            dtrDetailsResponseVo.setDTR_CODE(responseObj.get("DTR_CODE").getAsString());
                            dtrDetailsResponseVo.setDTR_MAKE(responseObj.get("DTR_MAKE").getAsString());
                            dtrDetailsResponseVo.setDTR_PHASE(responseObj.get("DTR_PHASE").getAsString());
                            dtrDetailsResponseVo.setDTR_SNO(responseObj.get("DTR_SNO").getAsString());
                            dtrDetailsResponseVo.setTYPE_OF_LOAD(responseObj.get("TYPE_OF_LOAD").getAsString());
                            dtrDetailsResponseVo.setGLAT(responseObj.get("GLAT").getAsString());
                            dtrDetailsResponseVo.setGLONG(responseObj.get("GLONG").getAsString());
                            arcGISDatabase.addUDTRDetailsData(dtrDetailsResponseVo);

                            not_updated = new PMIUpdatedDTRVO();
                            not_updated.setDTR_MAKE(responseObj.get("DTR_MAKE").isJsonNull() ? "" : responseObj.get("DTR_MAKE").getAsString());
                            not_updated.setDTR_SL_NO(responseObj.get("DTR_SNO").isJsonNull() ? "" : responseObj.get("DTR_SNO").getAsString());
                            not_updated.setDTR_POLE_SUPPORT(responseObj.get("POLE_SUPPORT").isJsonNull() ? "" : responseObj.get("POLE_SUPPORT").getAsString());
                            not_updated.setNO_OF_LTCKT(responseObj.get("NO_LT_CKT").isJsonNull() ? "" : responseObj.get("NO_LT_CKT").getAsString());
                            not_updated.setDTR_CAPACITY(responseObj.get("DTR_CAPACITY").isJsonNull() ? "" : responseObj.get("DTR_CAPACITY").getAsString());
                            not_updated.setDTR_ABSWITCH(responseObj.get("DTR_AB").isJsonNull() ? "" : responseObj.get("DTR_AB").getAsString());
                            not_updated.setDTR_STRUC_TYPE(responseObj.get("DTR_STRUCTURE").isJsonNull() ? "" : responseObj.get("DTR_STRUCTURE").getAsString());

                            if (responseObj.get("PIN_CUT").isJsonNull()){
                                not_updated.setDTR_POLE("");
                            }else {
                                if (responseObj.get("PIN_CUT").getAsString().equals("PINPOINT")){
                                    not_updated.setDTR_POLE("PIN");
                                }else {
                                    not_updated.setDTR_POLE("CUT");
                                }
                            }



                            checkDTRSurvey(dtr_code,"dtr");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(PMIDTRSelectActivity.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIDTRSelectActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMIDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getSurveyedData() {
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        PMISurveyedDataReqVo surveyedDataReqVo = new PMISurveyedDataReqVo();
        if (dtrCode.contains("-")) {
            String[] dtrcode = dtrCode.split("-");
            dtr_Code = dtrcode[0]; // 004
            // String ssName = poleNum[1];
            surveyedDataReqVo.setDTR_STRUCTURE(dtr_Code);
        } else {
            surveyedDataReqVo.setDTR_STRUCTURE(dtrCode);
        }

        surveyedDataReqVo.setLT_CKTCODE(ltCircuit);
        surveyedDataReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIDTRSelectActivity.this));
        surveyedDataReqVo.setVERSION(MobileConstants.APP_VERSION);
        surveyedDataReqVo.setSESSION_ID(sessionId);
        surveyedDataReqVo.setSEC_CODE(loginDataResponses.get(0).getSEC_CODE());
        Gson gson=new Gson();
        String s=gson.toJson(surveyedDataReqVo);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<PMISurveyedResponseVo> responseVoCall = retrofitService.getsurveyedData(surveyedDataReqVo);
        responseVoCall.enqueue(new Callback<PMISurveyedResponseVo>() {
            @Override
            public void onResponse(Call<PMISurveyedResponseVo> call, Response<PMISurveyedResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    PMISurveyedResponseVo surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.getSTATUS())) {
                        // System.out.println("COMING DATA : "+surveyedResponseVo.getERR_MSG());
                        arcGISDatabase.clearSurveyData();
                        if ("NO_DATA".equalsIgnoreCase(surveyedResponseVo.getERR_MSG())) {
                            //   System.out.println("COMING DATA INSIDE: "+surveyedResponseVo.getERR_MSG());
                            PMIGis11KvSurveyDTRVo kvSurveyDTRVo=surveyedResponseVo.getDTR_DATA();
                            Intent intent = new Intent(PMIDTRSelectActivity.this, PMILineTypeActivity.class);
                            intent.putExtra("DTRCODE", dtr_Code);
                            intent.putExtra("CIRCUIT_CODE", ltCircuit);
                            intent.putExtra("GLAT", kvSurveyDTRVo.getGLAT());
                            intent.putExtra("GLONG", kvSurveyDTRVo.getGLONG());
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            startActivity(intent);
                            finish();
                        } else {
                            // arcGISDatabase.clearSurveyData();
                            List<PMISurveyedResponseDataVo> surveyedResponseDataVoList = surveyedResponseVo.getRESPONSE();
                            PMIGis11KvSurveyDTRVo surveyDTRVo=surveyedResponseVo.getDTR_DATA();
                            for (PMISurveyedResponseDataVo vo : surveyedResponseDataVoList) {
                                PMISurveyDataVo surveyVo = new PMISurveyDataVo();
                                surveyVo.setPOLE_NO(Integer.parseInt(vo.getPOLE_NO()));
                                surveyVo.setFEEDER_ID(vo.getFEEDER_ID());
                                surveyVo.setLAT(vo.getGLAT());
                                surveyVo.setLAN(vo.getGLONG());
                                surveyVo.setSURVEY_FLAG("Y");
                                arcGISDatabase.addSurveyData(surveyVo);
                            }
                            Intent intent = new Intent(PMIDTRSelectActivity.this, PMILineTypeActivity.class);
                            intent.putExtra("DTRCODE", dtr_Code);
                            intent.putExtra("CIRCUIT_CODE", ltCircuit);
                            intent.putExtra("GLAT", surveyDTRVo.getGLAT());
                            intent.putExtra("GLONG", surveyDTRVo.getGLONG());
                            intent.putExtra("MAX_COUNT", surveyedResponseVo.getSESSION_ID());
                            startActivity(intent);
                            startActivity(intent);
                            finish();
                        }


                    } else {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIDTRSelectActivity.this);
                        alertDialogBuilder.setTitle("Alert!!!");
                        alertDialogBuilder
                                .setMessage(Html.fromHtml(surveyedResponseVo.getERR_MSG() ))
                                .setCancelable(false)
                                .setPositiveButton("Okay",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                progressDialog.dismiss();
                                                dialog.cancel();
                                            }
                                        })

                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        progressDialog.dismiss();
                                        dialog.cancel();

                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                        Toast.makeText(PMIDTRSelectActivity.this, surveyedResponseVo.getERR_MSG().toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIDTRSelectActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMISurveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PMIDTRSelectActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PMIDTRSelectActivity.this, PMIHomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ch_dtrsurvey:
                if (ch_dtrsurvey.isChecked()) {
                    lyt_dtrselect_lt.setVisibility(View.GONE);
                    lyt_dtrselect.setVisibility(View.VISIBLE);
                    flag = 1;
                    ch_ltsurvey.setChecked(false);
                    // lyt_dtrselect.setVisibility(View.VISIBLE);
                    lyt_ltcircuitselect.setVisibility(View.GONE);
                    lyt_is_multi_circuit.setVisibility(View.GONE);
                   /* dtrCodeList.clear();
                    dtrCodeList.add("Select");
                    dtrCodeList = arcGISDatabase.getDTRCode();
                    Collections.reverse(dtrCodeList);*/
                }
                break;
            case R.id.ch_ltsurvey:
                if (ch_ltsurvey.isChecked()) {
                    lyt_dtrselect_lt.setVisibility(View.VISIBLE);
                    lyt_dtrselect.setVisibility(View.GONE);
                    flag = 2;
                    ch_dtrsurvey.setChecked(false);
                    lyt_ltcircuitselect.setVisibility(View.VISIBLE);
                    // lyt_dtrselect.setVisibility(View.VISIBLE);
                    lyt_is_multi_circuit.setVisibility(View.VISIBLE);
                    /*dtrCodeList.clear();
                    dtrCodeList.add("Select");
                    for (int i = 0; i < HomeActivity.list.size(); i++) {
                        if (HomeActivity.list.get(i).getIS_LTCKT_AVAILABLE().equals("YES")){
                            dtrCodeList.add(HomeActivity.list.get(i).getDTR_CODE()+"-"+HomeActivity.list.get(i).getDTR_LOCATION());
                        }
                    }
                    Collections.reverse(dtrCodeList);*/
                }
                break;


        }
    }
}