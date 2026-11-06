package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.FdrParticularsModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.LoginRequestVo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToBeIntegratedActivity extends AppCompatActivity implements ToBeIntegratedAdapter.OnItemClickListener {

    GifImageView loader;
    RecyclerView to_be_integrated_rv;
    private RetrofitServiceEstimates retrofitService;
    ArrayList<PurposeModel> list;
    RelativeLayout back_layout, close_layout,c_1;
    private SharedPreferences preferences;
    private String userId = "";
    private String INTG_STATUS = "";
    private TextView tv_description_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_integrated);

        getSupportActionBar().hide();
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        loader = findViewById(R.id.loader);
        c_1 = findViewById(R.id.c_1);
        to_be_integrated_rv = findViewById(R.id.to_be_integrated_rv);
        tv_description_text = findViewById(R.id.tv_description_text);
        list = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = preferences.getString(MobileConstants.USERNAME, "");
        INTG_STATUS=getIntent().getStringExtra("INTG_STATUS");

        //NEW CHANGES ON LT

        if (ConnectivityChecking.checkConnection(ToBeIntegratedActivity.this)) {
            getPurposeData();
        } else {
            CommanUtilits.internetDialog(ToBeIntegratedActivity.this);
        }

        //getPurposeData();
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            c_1.setVisibility(View.GONE);
            tv_description_text.setVisibility(View.GONE);

        }else {
            if("P".equalsIgnoreCase(INTG_STATUS) || "R".equalsIgnoreCase(INTG_STATUS)){
                c_1.setVisibility(View.GONE);
            }else{
                c_1.setVisibility(View.GONE);
            }
        }



    }

    private void getPurposeData() {
        loader.setVisibility(View.VISIBLE);
        list.clear();
        //loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

       if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
           dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getUSERID());
       }else {
           dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
       }

        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        dtrDetailsReqestVo.addProperty("STATUS", 5);
        dtrDetailsReqestVo.addProperty("INTG_STATUS", INTG_STATUS);

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.estimateswithpolecntsforIntg(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);

                    // Check if the STATUS field exists and is not null
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();

                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();

                                PurposeModel model = new PurposeModel();
                                model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                                model.setEST_NATURE(object.has("EST_NATURE") && !object.get("EST_NATURE").isJsonNull() ? object.get("EST_NATURE").getAsString() : "");
                                model.setEST_NO(object.has("EST_NO") && !object.get("EST_NO").isJsonNull() ? object.get("EST_NO").getAsString() : "");
                                model.setREG_COUNT(object.has("REG_COUNT") && !object.get("REG_COUNT").isJsonNull() ? object.get("REG_COUNT").getAsString() : "");
                                model.setSEC_ID(object.has("SEC_ID") && !object.get("SEC_ID").isJsonNull() ? object.get("SEC_ID").getAsString() : "");
                                model.setSTATUS(object.has("STATUS") && !object.get("STATUS").isJsonNull() ? object.get("STATUS").getAsString() : "");
                                model.setPURPOSE(object.has("PURPOSE") && !object.get("PURPOSE").isJsonNull() ? object.get("PURPOSE").getAsString() : "");
                                model.setLINE_33KV(object.has("LINE_33KV") && !object.get("LINE_33KV").isJsonNull() ? object.get("LINE_33KV").getAsString() : "");
                                model.setLINE_11KV(object.has("LINE_11KV") && !object.get("LINE_11KV").isJsonNull() ? object.get("LINE_11KV").getAsString() : "");
                                model.setLINE_LT(object.has("LINE_LT") && !object.get("LINE_LT").isJsonNull() ? object.get("LINE_LT").getAsString() : "");
                                model.setDTR(object.has("DTR") && !object.get("DTR").isJsonNull() ? object.get("DTR").getAsString() : "");
                                model.setSS(object.has("SS") && !object.get("SS").isJsonNull() ? object.get("SS").getAsString() : "");
                                model.setINTG_REMARKS(object.has("INTG_REMARKS") && !object.get("INTG_REMARKS").isJsonNull() ? object.get("INTG_REMARKS").getAsString() : "");
                                model.setINTG_STATUS(object.has("INTG_STATUS") && !object.get("INTG_STATUS").isJsonNull() ? object.get("INTG_STATUS").getAsString() : "");


                                model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");

                                model.setCOUNT_33KV(object.get("COUNT_33KV").getAsInt());
                                model.setCOUNT_11KV(object.get("COUNT_11KV").getAsInt());
                                model.setCOUNT_LT(object.get("COUNT_LT").getAsInt());

                                model.setNUM_33KV(object.get("NUM_33KV").getAsInt());
                                model.setNUM_11KV(object.get("NUM_11KV").getAsInt());
                                model.setNUM_LT(object.get("NUM_LT").getAsInt());

                                if (object.has("FDR_PARTICULARS") && object.get("FDR_PARTICULARS").isJsonArray()) {
                                    JsonArray fdrArray = object.getAsJsonArray("FDR_PARTICULARS");
                                    List<FdrParticularsModel> fdrList = new ArrayList<>();

                                    for (int j = 0; j < fdrArray.size(); j++) {
                                        JsonObject fdrObject = fdrArray.get(j).getAsJsonObject();
                                        FdrParticularsModel fdrModel = new FdrParticularsModel();

                                        fdrModel.setFEEDER(fdrObject.has("FEEDER") && !fdrObject.get("FEEDER").isJsonNull() ? fdrObject.get("FEEDER").getAsString() : null);
                                        // fdrModel.setFEEDER_NAME(fdrObject.has("FEEDER_NAME") && !fdrObject.get("FEEDER_NAME").isJsonNull() ? fdrObject.get("FEEDER_NAME").getAsString() : "");
                                        fdrModel.setVOLTAGE_LEVEL(fdrObject.has("VOLTAGE_LEVEL") && !fdrObject.get("VOLTAGE_LEVEL").isJsonNull() ? fdrObject.get("VOLTAGE_LEVEL").getAsString() : "");
                                        fdrModel.setSURVEY_TYPE(fdrObject.has("SURVEY_TYPE") && !fdrObject.get("SURVEY_TYPE").isJsonNull() ? fdrObject.get("SURVEY_TYPE").getAsString() : "");
                                        fdrModel.setSS_NAME(fdrObject.has("SS_NAME") && !fdrObject.get("SS_NAME").isJsonNull() ? fdrObject.get("SS_NAME").getAsString() : "");
                                        fdrModel.setFDR_INDEX(Integer.parseInt(fdrObject.has("FDR_INDEX") && !fdrObject.get("FDR_INDEX").isJsonNull() ? fdrObject.get("FDR_INDEX").getAsString() : ""));
                                        fdrModel.setFDR_COUNT(Integer.parseInt(fdrObject.has("FDR_COUNT") && !fdrObject.get("FDR_COUNT").isJsonNull() ? fdrObject.get("FDR_COUNT").getAsString() : ""));

                                        fdrList.add(fdrModel);
                                    }
                                    model.setFDR_PARTICULARS(fdrList);
                                }

                                list.add(model);


                            }

                            setUpToBeInteGratedSurvey();

                        } else {
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            openDialogForSuccessMsg(errorMessage, "","","",false);
                        }

                    } else {

                        // Handle case where STATUS is missing or null
                        openDialogForSuccessMsg("Invalid response from server: STATUS is missing or null", "","","",false);
                        Toast.makeText(ToBeIntegratedActivity.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    openDialogForSuccessMsg("Server Busy. Try Again.", "","","",false);
                    // Toast.makeText(ToBeIntegratedActivity.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                openDialogForSuccessMsg("Try Again", "","","",false);
                //Toast.makeText(ToBeIntegratedActivity.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setUpToBeInteGratedSurvey() {
        to_be_integrated_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ToBeIntegratedActivity.this);
        to_be_integrated_rv.setLayoutManager(layoutManager);
        to_be_integrated_rv.setFocusable(false);
        ToBeIntegratedAdapter adapter = new ToBeIntegratedAdapter(list, ToBeIntegratedActivity.this, this,INTG_STATUS);
        to_be_integrated_rv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intentPf = new Intent(ToBeIntegratedActivity.this, SurveyList.class);
        startActivity(intentPf);
        finish();
        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
    }

    //31-12-24 chandra sekhar changes
    //added EMP_ID and SEC_ID

    private void callWorkOrderDataToAssets(String GIS_ID,String EST_NO,String WORK_ORDER) {
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        loader.setVisibility(View.VISIBLE);

        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        entry.addProperty("EMP_ID", userId);
        entry.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        entry.addProperty("STATUS", 4);

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.workOrderStatusToAssets(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                //  Log.v("RESPONSE===>>", "" + response.body());
                //  System.out.println("COMPLETE ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), GIS_ID,EST_NO,WORK_ORDER,true);
                        Toast.makeText(ToBeIntegratedActivity.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                    } else {
                        String errMsg = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                : "Something went wrong";

                        openDialogForSuccessMsg(errMsg, GIS_ID,EST_NO,WORK_ORDER,false);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }


    private void openDialogForSuccessMsg(String msg, String GIS_ID, String EST_NO, String WORK_ORDER,boolean valid) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView imageView =view.findViewById(R.id.iv_icon);

        if (valid){
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.suc));
        }else {
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        }

        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));



        // 🔹 Title
        title.setText("Pay Attention");

        // 🔹 Build Header + Message
        StringBuilder builder = new StringBuilder();

//        if (GIS_ID != null && !GIS_ID.isEmpty()) {
//            builder.append("<b>GIS ID:</b> ").append(GIS_ID).append("<br>");
//        }
//
//        if (EST_NO != null && !EST_NO.isEmpty()) {
//            builder.append("<b>EST No:</b> ").append(EST_NO).append("<br>");
//        }
//
//        if (WORK_ORDER != null && !WORK_ORDER.isEmpty()) {
//            builder.append("<b>Work Order:</b> ").append(WORK_ORDER).append("<br><br>");
//        }

        // 🔴 Message Handling
        if (msg != null && msg.toLowerCase().contains("newly  proposed")) {
            builder.append("<font color='#D32F2F'>⚠ ").append(msg).append("</font>");
        } else {
            builder.append(msg);
        }

        message.setText(HtmlCompat.fromHtml(builder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        // 🔹 Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("Okay");

        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                Intent intent=new Intent(this, SurveyList.class);
                startActivity(intent);
            }else {
                Intent intent=new Intent(this, DashBoard.class);
                startActivity(intent);
            }


        });

        dialog.setCancelable(false);

        // Transparent background + full width
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        dialog.show();
    }



    @Override
    public void onItemClick(int position, PurposeModel model) {
        //24-12-24 chandu changes
      //  openWarningDialogForWorkOrder(model.getGIS_ID(),model.getEST_NO(),model.getWORK_ORDER());

        if ("R".equalsIgnoreCase(model.getINTG_STATUS())){
            updateStatusToResurveyRejected(model.getGIS_ID(),model.getEST_NO(),model.getWORK_ORDER());
        }else if ("A".equalsIgnoreCase(model.getINTG_STATUS())){
            openWarningDialogForWorkOrderNew(model.getGIS_ID(),model.getEST_NO(),model.getWORK_ORDER());
        }

    }
    private void updateStatusToResurveyRejected(String GIS_ID,String EST_NO,String WORK_ORDER){
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        loader.setVisibility(View.VISIBLE);

        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        entry.addProperty("EST_NO", EST_NO);
    // ✅ Create array
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(entry);

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.updateStatusToResurveyRejected(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                //  Log.v("RESPONSE===>>", "" + response.body());
                //  System.out.println("COMPLETE ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), GIS_ID,EST_NO,WORK_ORDER,true);
                        Toast.makeText(ToBeIntegratedActivity.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                    } else {
                        String errMsg = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                : "Something went wrong";

                        openDialogForSuccessMsg(errMsg, GIS_ID,EST_NO,WORK_ORDER,true);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void openWarningDialogForWorkOrderNew(String GIS_ID,String EST_NO,String WORK_ORDER) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_to_be_integrated, null);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.show();
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callWorkOrderDataToAssets(GIS_ID,EST_NO,WORK_ORDER);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}