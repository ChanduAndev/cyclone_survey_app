package com.apepdcl.gis_survey.pmi_old.module_33kv.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.model.PMIFeederMasterModel;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiCktVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.ArcGISDatabase33KV;
import com.apepdcl.gis_survey.pmi_old.module_33kv.PMIRetrofitClientInstance33kv;
import com.apepdcl.gis_survey.pmi_old.module_33kv.RetrofitService33kv;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMISSMasterVo;
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

public class PMISurveyHome33KV extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button btn_next;
    private Spinner sp_ssname, sp_feedername;
    private String ssName, feederName, feederId, maxCount, sessionId, feederCode, ssCode, subName, feedeNameList;
    private ArcGISDatabase33KV arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitService33kv retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    //MULTI CKT REQUIRED
    LinearLayout lyt_is_multi_circuit;
    Spinner sp_is_multi_circuit;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmisurvey_home33_kv);
        getSupportActionBar().hide();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("33kV Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lyt_is_multi_circuit = findViewById(R.id.lyt_is_multi_circuit);
        sp_is_multi_circuit = findViewById(R.id.sp_is_multi_circuit);
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

        sp_ssname=findViewById(R.id.sp_ssname);
        sp_feedername=findViewById(R.id.sp_feedername);
        sp_ssname.setOnItemSelectedListener(this);
        sp_feedername.setOnItemSelectedListener(this);
        arcGISDatabase=new ArcGISDatabase33KV(this);
        ehtName=new ArrayList<>();
        //ehtName.add("Select");
        ehtName=arcGISDatabase.getEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);
        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(PMISurveyHome33KV.this,
                android.R.layout.simple_spinner_dropdown_item,ehtName);
        sp_ssname.setAdapter(ehtSSadapter);



        btn_next=findViewById(R.id.btn_next);
        progressDialog=new ProgressDialog(PMISurveyHome33KV.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("Select".equalsIgnoreCase(ssName)){
                    Toast.makeText(PMISurveyHome33KV.this,"Select SS Name",Toast.LENGTH_LONG).show();
                }else if("Select".equalsIgnoreCase(feederName)){
                    Toast.makeText(PMISurveyHome33KV.this,"Select Feeder Name",Toast.LENGTH_LONG).show();
                }else if ("Select".equalsIgnoreCase(sp_is_multi_circuit.getSelectedItem().toString())) {
                    Toast.makeText(PMISurveyHome33KV.this, "Select Multi Circuit required", Toast.LENGTH_SHORT).show();
                }else {
                    if (ConnectivityChecking.checkConnection(PMISurveyHome33KV.this)) {
                        //  getSurveyData();
                        getMasterFeederData();

                    }else {
                        Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void getMasterFeederData() {
        progressDialog.show();
        List<LoginDataResponse> loginDataResponses = new PMIArcGISDatabase(PMISurveyHome33KV.this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
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
                               /* String dtr_location = data.get("DTR_LOCATION").isJsonNull() ? "" :  data.get("DTR_LOCATION").getAsString();
                                feederListLT.add(data.get("DTR_STRUCTURE_CODE").getAsString() + "~" +dtr_location);*/

                                String s_code = data.get("DTR_STRUCTURE_CODE").isJsonNull() ? "" : data.get("DTR_STRUCTURE_CODE").getAsString();
                                String s_loc = data.get("DTR_LOCATION").isJsonNull() ? "" : data.get("DTR_LOCATION").getAsString();
                                feederListLT.add(s_code + "~" +s_loc);
                            }

                            if ("Yes".equalsIgnoreCase(sp_is_multi_circuit.getSelectedItem().toString())){
                                getMultiCktData();
                            }else {
                                getSurveyData();
                            }





                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(PMISurveyHome33KV.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMISurveyHome33KV.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PMISurveyHome33KV.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
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
                        feederNameList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                        feederNameList.add("Select");
                        Collections.reverse(feederNameList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMISurveyHome33KV.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername.setAdapter(adapter);
                    } else {
                        feederNameList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMISurveyHome33KV.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_feedername:
                    feederName = sp_feedername.getSelectedItem().toString();

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
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMISurveyHome33KV.this));
            requestVo.setSESSION_ID(preferences.getString(MobileConstants.SESSION_ID, ""));
            requestVo.setVERSION(MobileConstants.APP_VERSION_33_KV);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            Log.v("DATA===>>", "" + json);
            retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
            Call<PMILastSurveyResponseSSDataVo> responseVoCall = retrofitService.getLastSurveyData(requestVo);
            responseVoCall.enqueue(new Callback<PMILastSurveyResponseSSDataVo>() {
                @Override
                public void onResponse(Call<PMILastSurveyResponseSSDataVo> call, Response<PMILastSurveyResponseSSDataVo> response) {
                    progressDialog.dismiss();
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
                                Intent intent = new Intent(PMISurveyHome33KV.this, PMIPoleSurveyActivity33KV.class);
                                intent.putExtra("SSNAME",subName);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
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
                                    arcGISDatabase.addSurveyData(surveyVo);
                                }
                                Intent intent = new Intent(PMISurveyHome33KV.this, PMIPoleSurveyActivity33KV.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("FEEDER_NAME",feedeNameList);
                                intent.putExtra("FEEDER_CODE",feederCode);
                                intent.putExtra("MAX_COUNT",response_vo.getSESSION_ID());
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();

                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), MainActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMISurveyHome33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstants.APK_URL);
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
                                Toast.makeText(PMISurveyHome33KV.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(PMISurveyHome33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMILastSurveyResponseSSDataVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PMISurveyHome33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getMultiCktData() {

        String[] feederDta = feederName.split("-");
        feederCode = feederDta[0];

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("FEEDER_ID", feederCode);
        dtrDetailsReqestVo.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PMISurveyHome33KV.this));
        dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.APP_VERSION_33_KV);


        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("MULTRESPONSE==", "" + surveyedResponseVo);
                    // System.out.println("MULTI CKT RESPONSE : "+response.body());
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            list11kv.clear();
                            list33kv.clear();
                            listLT.clear();
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
                        getSurveyData();
                        //  Toast.makeText(this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMISurveyHome33KV.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PMISurveyHome33KV.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
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
        getSurveyData();
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