package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 10-06-2026.
 */
public class ContinueDtrList extends AppCompatActivity {

    ProgressDialog progressDialog;
    ImageView back_layout;
    LinearLayout loader_layout;

    private RetrofitServiceMeter retrofitServiceMeter;

    MeterSurveyDatabase database;

    List<MeterPostingModel> meterPostingModelList;

    TextView tv_no_of_services,tv_dtr_location_name,tv_dtr_code, continue_survey;

    private RetrofitServiceEstimates retrofitService;

    AssignedDtrModel dtrFeederMaster;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continue_dtr_list);
        getSupportActionBar().hide();
        database = new MeterSurveyDatabase(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);

        dtrFeederMaster = new AssignedDtrModel();
        back_layout = findViewById(R.id.back_layout);
        loader_layout = findViewById(R.id.loader_layout);
        tv_no_of_services = findViewById(R.id.tv_no_of_services);
        tv_dtr_location_name = findViewById(R.id.tv_dtr_location_name);
        tv_dtr_code = findViewById(R.id.tv_dtr_code);
        continue_survey = findViewById(R.id.continue_survey);
        back_layout.setOnClickListener(view -> onBackPressed());
        meterPostingModelList = database.getAllMeterSurveyData();

        tv_no_of_services.setText(""+meterPostingModelList.size());
        tv_dtr_code.setText(meterPostingModelList.get(0).getDTR_CODE());
        tv_dtr_location_name.setText(meterPostingModelList.get(0).getDTR_NAME());

        for (int i = 0; i <meterPostingModelList.size() ; i++) {
            System.out.println("DTR_ID_IS:::::"+meterPostingModelList.get(i).getDTR_CODE());
        }



        getPendingDTRList();
        //getStaticPendingDTRList("103");


        continue_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dtrFeederMaster.getIS_LTCKT_AVAILABLE().equals("NO")){
                    getServiceNumbersForApartmentDTR();
                }
                else {
                    getPolesForDTR();
                }

            }
        });



    }
    private void getServiceNumbersForApartmentDTR() {

        progressDialog.show();
        MeterConstants.allServiceNumbers.clear();
        MeterConstants.serviceNumbersList.clear();

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("SECTION_ID", dtrFeederMaster.getSECTION_ID());
        //dtrDetailsReqestVo.addProperty("DTR_CODE", dtrFeederMaster.getDTR_CODE());

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getServiceConsumersData(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();
                            ArrayList<ServiceNumbersModel> serviceNumbersList = new ArrayList<>();
                            for (int i = 0; i < dataArray.size(); i++) {
                                MeterConstants.allServiceNumbers.add(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
                                ServiceNumbersModel model = new ServiceNumbersModel();
//                                model.setSERVICE_NUMBER(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
//                                model.setAREA_CODE(dataArray.get(i).getAsJsonObject().get("AREA_CODE").getAsString());
//                                model.setAREA_NAME(dataArray.get(i).getAsJsonObject().get("AREA_NAME").getAsString());
//                                model.setCONSUMER_NAME(dataArray.get(i).getAsJsonObject().get("CONS_NAME").getAsString());
//                                model.setUKSC_NUMBER(dataArray.get(i).getAsJsonObject().get("UKSCNO").getAsString());
//                                model.setMETER_NO(dataArray.get(i).getAsJsonObject().get("METER_NO").getAsString());

                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                model.setSERVICE_NUMBER(
                                        obj.has("CONS_NO") && !obj.get("CONS_NO").isJsonNull()
                                                ? obj.get("CONS_NO").getAsString()
                                                : "");

                                model.setAREA_CODE(
                                        obj.has("AREA_CODE") && !obj.get("AREA_CODE").isJsonNull()
                                                ? obj.get("AREA_CODE").getAsString()
                                                : "");

                                model.setAREA_NAME(
                                        obj.has("AREA_NAME") && !obj.get("AREA_NAME").isJsonNull()
                                                ? obj.get("AREA_NAME").getAsString()
                                                : "");

                                model.setCONSUMER_NAME(
                                        obj.has("CONS_NAME") && !obj.get("CONS_NAME").isJsonNull()
                                                ? obj.get("CONS_NAME").getAsString()
                                                : "");

                                model.setUKSC_NUMBER(
                                        obj.has("UKSCNO") && !obj.get("UKSCNO").isJsonNull()
                                                ? obj.get("UKSCNO").getAsString()
                                                : "");

                                model.setMETER_NO(
                                        obj.has("METER_NO") && !obj.get("METER_NO").isJsonNull()
                                                ? obj.get("METER_NO").getAsString()
                                                : "");
                                serviceNumbersList.add(model);
                            }

                            MeterConstants.serviceNumbersList.addAll(serviceNumbersList);
                            Intent intent = new Intent(ContinueDtrList.this, OnlyMeterSurveyMap.class);
                            intent.putExtra("DTR_DATA", dtrFeederMaster );
                            startActivity(intent);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(ContinueDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(ContinueDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ContinueDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getPolesForDTR() {
        progressDialog.show();
        MeterConstants.selectedPolesList.clear();

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", meterPostingModelList.get(0).getDTR_CODE());

        /*retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getFeederMasterNew(dtrDetailsReqestVo);*/


        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getDTrPoleData(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();
                            ArrayList<PolesModel> polesList = new ArrayList<>();
                            for (int i = 0; i < dataArray.size(); i++) {
                                PolesModel pole1 = new PolesModel();
                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                pole1.setPole_id(
                                        obj.has("EXT_ID") && !obj.get("EXT_ID").isJsonNull()
                                                ? obj.get("EXT_ID").getAsString()
                                                : "");

                                pole1.setPole_no(
                                        obj.has("POLE_NO") && !obj.get("POLE_NO").isJsonNull()
                                                ? obj.get("POLE_NO").getAsString()
                                                : "");

                                pole1.setPole_type(
                                        obj.has("POLE_TYPE") && !obj.get("POLE_TYPE").isJsonNull()
                                                ? obj.get("POLE_TYPE").getAsString()
                                                : "");

                                pole1.setPole_height(
                                        obj.has("POLE_HEIGHT") && !obj.get("POLE_HEIGHT").isJsonNull()
                                                ? obj.get("POLE_HEIGHT").getAsString()
                                                : "");

                                pole1.setPole_latitude(
                                        obj.has("GLAT") && !obj.get("GLAT").isJsonNull()
                                                ? obj.get("GLAT").getAsString()
                                                : "");

                                pole1.setPole_longitude(
                                        obj.has("GLONG") && !obj.get("GLONG").isJsonNull()
                                                ? obj.get("GLONG").getAsString()
                                                : "");

                                pole1.setLt_ckt_code(
                                        obj.has("LT_CKT_CODE") && !obj.get("LT_CKT_CODE").isJsonNull()
                                                ? obj.get("LT_CKT_CODE").getAsString()
                                                : "");
                                pole1.setDtr_code(meterPostingModelList.get(0).getDTR_CODE());
                                pole1.setDtr_name(meterPostingModelList.get(0).getDTR_NAME());
                                polesList.add(pole1);
                            }
                            MeterConstants.selectedPolesList.addAll(polesList);
                            getServiceNumbersForDTR();

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ContinueDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ContinueDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ContinueDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getServiceNumbersForDTR() {
        // progressDialog.show();
        MeterConstants.allServiceNumbers.clear();
        MeterConstants.serviceNumbersList.clear();

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("SECTION_ID", dtrFeederMaster.getSECTION_ID());
        //dtrDetailsReqestVo.addProperty("DTR_CODE", meterPostingModelList.get(0).getDTR_CODE());

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getServiceConsumersData(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                   // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();
                            ArrayList<ServiceNumbersModel> serviceNumbersList = new ArrayList<>();
                            for (int i = 0; i < dataArray.size(); i++) {
                                MeterConstants.allServiceNumbers.add(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
                                ServiceNumbersModel model = new ServiceNumbersModel();
//                                model.setSERVICE_NUMBER(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
//                                model.setAREA_CODE(dataArray.get(i).getAsJsonObject().get("AREA_CODE").getAsString());
//                                model.setAREA_NAME(dataArray.get(i).getAsJsonObject().get("AREA_NAME").getAsString());
//                                model.setCONSUMER_NAME(dataArray.get(i).getAsJsonObject().get("CONS_NAME").getAsString());
//                                model.setUKSC_NUMBER(dataArray.get(i).getAsJsonObject().get("UKSCNO").getAsString());
//                                model.setMETER_NO(dataArray.get(i).getAsJsonObject().get("METER_NO").getAsString());
                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                model.setSERVICE_NUMBER(
                                        obj.has("CONS_NO") && !obj.get("CONS_NO").isJsonNull()
                                                ? obj.get("CONS_NO").getAsString()
                                                : "");

                                model.setAREA_CODE(
                                        obj.has("AREA_CODE") && !obj.get("AREA_CODE").isJsonNull()
                                                ? obj.get("AREA_CODE").getAsString()
                                                : "");

                                model.setAREA_NAME(
                                        obj.has("AREA_NAME") && !obj.get("AREA_NAME").isJsonNull()
                                                ? obj.get("AREA_NAME").getAsString()
                                                : "");

                                model.setCONSUMER_NAME(
                                        obj.has("CONS_NAME") && !obj.get("CONS_NAME").isJsonNull()
                                                ? obj.get("CONS_NAME").getAsString()
                                                : "");

                                model.setUKSC_NUMBER(
                                        obj.has("UKSCNO") && !obj.get("UKSCNO").isJsonNull()
                                                ? obj.get("UKSCNO").getAsString()
                                                : "");

                                model.setMETER_NO(
                                        obj.has("METER_NO") && !obj.get("METER_NO").isJsonNull()
                                                ? obj.get("METER_NO").getAsString()
                                                : "");
                                serviceNumbersList.add(model);
                            }

                            MeterConstants.serviceNumbersList.addAll(serviceNumbersList);
                            Intent intent = new Intent(ContinueDtrList.this, MeterSurveyMap.class);
                            intent.putExtra("DTR_DATA", dtrFeederMaster );
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(ContinueDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(ContinueDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ContinueDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }



    private void getPendingDTRList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("MOBILE_NO", sharedPreferences.getString(MeterConstants.METER_MOBILE_NO,""));
        dataMap.put("VERSION", MobileConstants.CONSUMERS);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(dataMap).getAsJsonObject();
        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getAssignedDTrs(jsonObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



                if (response.body() == null) {
                    Toast.makeText(ContinueDtrList.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(ContinueDtrList.this, "Invalid response from server: STATUS missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(ContinueDtrList.this, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                JsonArray dtr_array = surveyedResponseVo.getAsJsonArray("DATA");

                for (int i = 0; i < dtr_array.size(); i++) {

                    JsonObject jsonObject1 = dtr_array.get(i).getAsJsonObject();
                    if (jsonObject1.get("DTR_CODE").getAsString().equals(meterPostingModelList.get(0).getDTR_CODE())) {
                        String isMapped = jsonObject1.has("IS_LTCKT_AVAILABLE") && !jsonObject1.get("IS_LTCKT_AVAILABLE").isJsonNull()
                                ? jsonObject1.get("IS_LTCKT_AVAILABLE").getAsString()
                                : "";

                        dtrFeederMaster.setREC_ID(jsonObject1.get("REC_ID").isJsonNull() ? "" : jsonObject1.get("REC_ID").getAsString());
                        dtrFeederMaster.setUSERID(jsonObject1.get("USERID").isJsonNull() ? "" : jsonObject1.get("USERID").getAsString());
                        dtrFeederMaster.setUSER_NAME(jsonObject1.get("USER_NAME").isJsonNull() ? "" : jsonObject1.get("USER_NAME").getAsString());
                        dtrFeederMaster.setROLE(jsonObject1.get("ROLE").isJsonNull() ? "" : jsonObject1.get("ROLE").getAsString());
                        dtrFeederMaster.setDTR_CODE(jsonObject1.get("DTR_CODE").isJsonNull() ? "" : jsonObject1.get("DTR_CODE").getAsString());
                        dtrFeederMaster.setDTR_LOCATION(jsonObject1.get("DTR_LOCATION").isJsonNull() ? "" : jsonObject1.get("DTR_LOCATION").getAsString());
                        dtrFeederMaster.setDTR_GLAT(jsonObject1.get("DTR_GLAT").isJsonNull() ? "" : jsonObject1.get("DTR_GLAT").getAsString());
                        dtrFeederMaster.setDTR_GLONG(jsonObject1.get("DTR_GLONG").isJsonNull() ? "" : jsonObject1.get("DTR_GLONG").getAsString());
                        dtrFeederMaster.setIS_LTCKT_AVAILABLE(jsonObject1.get("IS_LTCKT_AVAILABLE").isJsonNull() ? "" : jsonObject1.get("IS_LTCKT_AVAILABLE").getAsString());
                        dtrFeederMaster.setCREATED_DT(jsonObject1.get("CREATED_DT").isJsonNull() ? "" : jsonObject1.get("CREATED_DT").getAsString());
                        dtrFeederMaster.setSURVEY_COMPLETED(jsonObject1.get("SURVEY_COMPLETED").isJsonNull() ? "" : jsonObject1.get("SURVEY_COMPLETED").getAsString());
                        dtrFeederMaster.setCOMPLETION_DT(jsonObject1.get("COMPLETION_DT").isJsonNull() ? "" : jsonObject1.get("COMPLETION_DT").getAsString());
                        dtrFeederMaster.setNO_OF_SCS(jsonObject1.get("NO_OF_SCS").isJsonNull() ? "" : jsonObject1.get("NO_OF_SCS").getAsString());
                        dtrFeederMaster.setSECTION_NAME(jsonObject1.get("SECTION_NAME").isJsonNull() ? "" : jsonObject1.get("SECTION_NAME").getAsString());
                        dtrFeederMaster.setUNMAPPED_POLES(jsonObject1.get("UNMAPPED_POLES").isJsonNull() ? "" : jsonObject1.get("UNMAPPED_POLES").getAsString());
                        dtrFeederMaster.setUNMAPPED_SERVICES(jsonObject1.get("UNMAPPED_SERVICES").isJsonNull() ? "" : jsonObject1.get("UNMAPPED_SERVICES").getAsString());
                        dtrFeederMaster.setLOAD_TYPE_ASGNED(jsonObject1.get("LOAD_TYPE_ASGNED").isJsonNull() ? "" : jsonObject1.get("LOAD_TYPE_ASGNED").getAsString());
                        dtrFeederMaster.setLOAD_TYPE_SURVEYED(jsonObject1.get("LOAD_TYPE_SURVEYED").isJsonNull() ? "" : jsonObject1.get("LOAD_TYPE_SURVEYED").getAsString());
                        dtrFeederMaster.setSECTION_ID(jsonObject1.get("SECTION_ID").isJsonNull() ? "" : jsonObject1.get("SECTION_ID").getAsString());
                        dtrFeederMaster.setMOBILE_NO(jsonObject1.get("MOBILE_NO").isJsonNull() ? "" : jsonObject1.get("MOBILE_NO").getAsString());

                        break;
                    }



                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout.setVisibility(View.GONE);
            }
        });
    }
}
