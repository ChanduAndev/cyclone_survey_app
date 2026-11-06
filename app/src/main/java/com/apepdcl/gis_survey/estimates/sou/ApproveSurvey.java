package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.FdrParticularsModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveSurvey extends AppCompatActivity implements ApproveAdapter.OnItemClickListener {

    RelativeLayout back_layout, close_layout;
    GifImageView loader;
    RecyclerView purpose_rv;
    String count = "";
    private RetrofitServiceEstimates retrofitService;
    ArrayList<PurposeModel> list;
    private LinearLayout loader_layout;
    private TextView tv_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_survey);
        getSupportActionBar().hide();

        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        loader = findViewById(R.id.loader);
        purpose_rv = findViewById(R.id.purpose_rv);
        loader_layout = findViewById(R.id.loader_layout);
        tv_progress_bar = findViewById(R.id.tv_progress_bar);
        list = new ArrayList<>();

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES") || LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {

            if (ConnectivityChecking.checkConnection(ApproveSurvey.this)) {
                getApproveSurveyData();
            } else {
                CommanUtilits.internetDialog(ApproveSurvey.this);
            }
            //getApproveSurveyData();
        } else {

            if (ConnectivityChecking.checkConnection(ApproveSurvey.this)) {
                workorderIntegarteAndApprovaldata();
            } else {
                CommanUtilits.internetDialog(ApproveSurvey.this);
            }

            //workorderIntegarteAndApprovaldata();
        }

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
    }


    private void setupCompleteSurvey() {
        purpose_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ApproveSurvey.this);
        purpose_rv.setLayoutManager(layoutManager);
        purpose_rv.setFocusable(false);
        ApproveAdapter adapter = new ApproveAdapter(list, ApproveSurvey.this, this);
        purpose_rv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intentPf = new Intent(ApproveSurvey.this, SurveyList.class);
        startActivity(intentPf);
        finish();
        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
    }

    @Override
    public void onItemClick(int position, PurposeModel model) {
        Intent intent = new Intent(ApproveSurvey.this, ApproveBOQActivity.class);
        intent.putExtra("GIS_ID", model.getGIS_ID());
        intent.putExtra("EST_NO", model.getEST_NO());
        intent.putExtra("DESCRIPTION", model.getPURPOSE());
        intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
        intent.putExtra("RE_SURVEY", model.getRE_SURVEY());
        intent.putExtra("REVISED", model.getREVISED());
        intent.putExtra("EST_NATURE", model.getEST_NATURE());
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
        //openCompleteDialog(model.getGIS_ID());
    }

    private void openCompleteDialog(String GIS_ID) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Approve Survey!!");
        builder.setMessage("Are you sure you want to Approve this survey?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            dialog.dismiss();
            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                openWarningDialogForEstimates(GIS_ID);
            } else {
                openWarningDialogForWorkOrder(GIS_ID);
            }

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();

        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void openWarningDialogForWorkOrder(String GIS_ID) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.suc));
        tv_est_no.setText(getColoredText("Est No: ", "", this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

        // Title & Message
        title.setText("Approve Survey!!");
        message.setText("The material BOQ will be transmitted to SAP.");

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("YES");
        cancelBtn.setText("Cancel");

        // YES Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            callSurveyCompleteAPI(GIS_ID);
        });

        // CANCEL Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);

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

//    private void openWarningDialogForWorkOrder(String GIS_ID) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("Approve Survey!!");
//        builder.setMessage("The material BOQ will be transmitted to SAP ");
//        builder.setPositiveButton("YES", (dialog, which) -> {
//            dialog.dismiss();
//            callSurveyCompleteAPI(GIS_ID);
//
//
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            dialog.cancel();
//
//        });
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }

    private void openWarningDialogForEstimates(String GIS_ID) {

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
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        tv_est_no.setText(getColoredText("Est No: ", "", this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.question));
        // Title & Message
        title.setText("Approve Survey!!");
        message.setText("An estimate will be prepared in SAP.");

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("YES");
        cancelBtn.setText("Cancel");

        // YES Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            callSurveyCompleteAPI(GIS_ID);
        });

        // CANCEL Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(true);

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
//    private void openWarningDialogForEstimates(String GIS_ID) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("Approve Survey!!");
//        builder.setMessage("An estimate will be prepared in SAP");
//        builder.setPositiveButton("YES", (dialog, which) -> {
//            dialog.dismiss();
//            callSurveyCompleteAPI(GIS_ID);
//
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            dialog.cancel();
//
//        });
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }

    private void openDialogForSuccessMsg(String msg,boolean valid) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        // Title (optional)
        title.setText("Message");

        if (valid){
            iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.suc));
        }else {
            iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        }
        // Message
        message.setText(msg);

        // Show only Close button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("Close");

        // Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            onBackPressed();
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

//    private void openDialogForSuccessMsg(String msg) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setMessage(msg);
//        builder.setCancelable(false);
//        builder.setPositiveButton("Close", (dialog, which) -> {
//            dialog.dismiss();
//            onBackPressed();
//
//
//        });
//
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }


    private void callSurveyCompleteAPI(String GIS_ID) {
        loader.setVisibility(View.VISIBLE);

        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        entry.addProperty("STATUS", 3);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(entry);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        //  Log.v("DATA===>>", "" + jsonArray);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.approveSurvey(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                //  Log.v("RESPONSE===>>", "" + response.body());
                //   System.out.println("COMPLETE ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(),true);
                        //Toast.makeText(ApproveSurvey.this, "Survey Approved Successfully..", Toast.LENGTH_LONG).show();
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES") || LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                            getApproveSurveyData();
                        } else {
                            workorderIntegarteAndApprovaldata();
                        }
                    } else {
                        openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(),false);
                        //Toast.makeText(ApproveSurvey.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void getApproveSurveyData() {
        loader_layout.setVisibility(View.VISIBLE);
        String survey_from = LoginActivity.SURVEY_FROM;
        if (survey_from != null) {
            if (survey_from.equalsIgnoreCase("ESTIMATES")) {
                tv_progress_bar.setText("Downloading Estimates Data");
            } else {
                tv_progress_bar.setText("Downloading Work order Data");
            }
        }
        list.clear();
        //loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        //dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        //  dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
        }else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }
        dtrDetailsReqestVo.addProperty("STATUS", 5);

        dtrDetailsReqestVo.addProperty("USER_ID", loginDataResponses.get(0).getUSERID());

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.estimateswithpolecnts(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);

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

                                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES") || LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                                    if (object.get("STATUS").getAsString().equals("5")) {
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


                                        model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                        model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                        model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                        model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                        model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");
                                        model.setRE_SURVEY(object.has("RE_SURVEY") && !object.get("RE_SURVEY").isJsonNull() ? object.get("RE_SURVEY").getAsString() : "");
                                        model.setREVISED(object.has("REVISED") && !object.get("REVISED").isJsonNull() ? object.get("REVISED").getAsString() : "");
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
                                } else {
                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                                        if (!object.get("LINE_LT").isJsonNull() && "Y".equals(object.get("LINE_LT").getAsString()) &&
                                                (object.get("LINE_33KV").isJsonNull() || "N".equals(object.get("LINE_33KV").getAsString())) &&
                                                (object.get("LINE_11KV").isJsonNull() || "N".equals(object.get("LINE_11KV").getAsString()))) {
                                            if (object.get("STATUS").getAsString().equals("5")) {
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


                                                model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                                model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                                model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");


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
                                        } else {
                                            if (object.get("STATUS").getAsString().equals("4")) {
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
                                                model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");


                                                model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                                model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");

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
                                        }
                                    }
                                }


                            }

                            setupCompleteSurvey();

                        } else {
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(ApproveSurvey.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // Handle case where STATUS is missing or null
                        Toast.makeText(ApproveSurvey.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ApproveSurvey.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(ApproveSurvey.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void workorderIntegarteAndApprovaldata() {
        loader_layout.setVisibility(View.VISIBLE);
        String survey_from = LoginActivity.SURVEY_FROM;
        if (survey_from != null) {
            if (survey_from.equalsIgnoreCase("ESTIMATES")) {
                tv_progress_bar.setText("Downloading Estimates Data");
            } else {
                tv_progress_bar.setText("Downloading Work order Data");
            }
        }
        list.clear();
        //loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        //dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        //  dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
        }else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }
        dtrDetailsReqestVo.addProperty("STATUS", 4);

        dtrDetailsReqestVo.addProperty("USER_ID", loginDataResponses.get(0).getUSERID());


        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.estimateswithpolecnts(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);

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


                                model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");


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

                            setupCompleteSurvey();

                        } else {
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(ApproveSurvey.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // Handle case where STATUS is missing or null
                        Toast.makeText(ApproveSurvey.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ApproveSurvey.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(ApproveSurvey.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}