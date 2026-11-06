package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveBOQActivity extends AppCompatActivity {
    GifImageView loader;
    private RetrofitServiceEstimates retrofitService;
    private RelativeLayout back_layout, close_layout, main_layout;
    private LinearLayout layout_btns;
    private TextView est_no, work_order, tv_error_msg, tv_description, gis_id, est_nature, voltage,estimate_no_tv;
    private RecyclerView rv_completed_boq;
    ArrayList<BoqMaterialModel> boqModel;
    private Button btn_approve, btn_cancel;
    private String GIS_ID = "", EST_NO, DESCRIPTION, WORK_ORDER, RE_SURVEY = "", REVISED = "", EST_NATURE = "";
    private boolean isApproveClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_boqactivity);
        loader = findViewById(R.id.loader);
        rv_completed_boq = findViewById(R.id.rv_completed_boq);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_approve = findViewById(R.id.btn_approve);
        boqModel = new ArrayList<>();
        loader = findViewById(R.id.loader);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        main_layout = findViewById(R.id.main_layout);
        layout_btns = findViewById(R.id.layout_btns);
        est_no = findViewById(R.id.est_no);
        estimate_no_tv = findViewById(R.id.estimate_no_tv);
        work_order = findViewById(R.id.work_order);
        voltage = findViewById(R.id.voltage);
        tv_description = findViewById(R.id.tv_description);
        tv_error_msg = findViewById(R.id.tv_error_msg);
        gis_id = findViewById(R.id.gis_id);
        est_nature = findViewById(R.id.est_nature);
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        System.out.println("GIS_ID_APPROVE" + GIS_ID);
        EST_NO = getIntent().getStringExtra("EST_NO");
        DESCRIPTION = getIntent().getStringExtra("DESCRIPTION");
        WORK_ORDER = getIntent().getStringExtra("WORK_ORDER");
        REVISED = getIntent().getStringExtra("REVISED");
        RE_SURVEY = getIntent().getStringExtra("RE_SURVEY");
        EST_NATURE = getIntent().getStringExtra("EST_NATURE");
        gis_id.setText(GIS_ID);

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
            est_no.setText(WORK_ORDER);
            estimate_no_tv.setText("Work Order");
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            est_no.setText("BoQ details for DTR Code :" + " " + EST_NO);
            est_nature.setVisibility(View.GONE);
            voltage.setText("CODE");
        } else {
            est_no.setText(EST_NO);
        }


        tv_description.setText(DESCRIPTION);
        est_nature.setText(EST_NATURE);
        estDataBoQDetailsAPI(GIS_ID, EST_NO);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
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

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isApproveClick = true;
                if (isApproveClick) {
                    btn_approve.setClickable(false);
                    btn_approve.setEnabled(false);
                    btn_approve.setBackgroundTintList(ContextCompat.getColorStateList(ApproveBOQActivity.this, R.color.colorPrimary1));

                }
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {

                    System.out.println("RE_SURVEY_STATUS: " + RE_SURVEY);
                    System.out.println("REVISED_STATUS: " + REVISED);
                    String msg = "";
                    if (RE_SURVEY.equalsIgnoreCase("Y") && REVISED.equalsIgnoreCase("Y")) {
                        msg = "As Estimate is revised, Only BOQ will be updated in SAP. Edit the Estimate in SAP if quantities to be changed.";
                    } else if (RE_SURVEY.equalsIgnoreCase("Y")) {
                        msg = "For GIS Re-survey, Only BOQ will be updated in SAP. Edit the Estimate in SAP if quantities to be changed.";
                    } else if (REVISED.equalsIgnoreCase("Y")) {
                        msg = "As Estimate is revised, Only BOQ will be updated in SAP. Edit the Estimate in SAP if quantities to be changed.";
                    } else {
                        msg = "An estimate will be prepared in SAP";
                    }
                    openWarningDialogForEstimates(GIS_ID, msg);
                } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    openWarningDialogForEstimates(GIS_ID, "Are you sure you want to approve this survey?");
                } else {
                    openWarningDialogForWorkOrder(GIS_ID);
                }
            }
        });


    }

    private void openWarningDialogForWorkOrder(String GIS_ID) {
       /* androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Approve Survey!!");
        builder.setCancelable(false);
        builder.setMessage("The material BOQ will be transmitted to SAP ");
        builder.setPositiveButton("YES", (dialog, which) -> {
            dialog.dismiss();
            callSurveyCompleteAPI(GIS_ID);


        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            onBackPressed();
            dialog.cancel();

        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();*/


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_to_be_integrated, null);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.setContentView(dialogView);
        dialog.show();
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);
        title.setText("Approve Survey!!");
        message.setText("The material BOQ will be transmitted to SAP ");

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callSurveyCompleteAPI(GIS_ID);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openWarningDialogForEstimates(String GIS_ID, String msg) {
        /*androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Approve Survey!!");
        builder.setCancelable(false);
        //builder.setMessage("An estimate will be prepared in SAP");
        builder.setMessage(msg);
        builder.setPositiveButton("YES", (dialog, which) -> {
            dialog.dismiss();
            callSurveyCompleteAPI(GIS_ID);

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            onBackPressed();
            dialog.cancel();

        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();*/


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_to_be_integrated, null);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.setContentView(dialogView);
        dialog.show();
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);
        title.setText("Approve Survey!!");
        message.setText(msg);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callSurveyCompleteAPI(GIS_ID);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void callSurveyCompleteAPI(String GIS_ID) {
        loader.setVisibility(View.VISIBLE);
        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            entry.addProperty("EST_NO", EST_NO);
        }
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
                // System.out.println("COMPLETE ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), true);
                        //Toast.makeText(ApproveSurvey.this, "Survey Approved Successfully..", Toast.LENGTH_LONG).show();
                        //getPurposeData();
                    } else {
                        String errMsg = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                : "Something went wrong";
                        //25-02-2025 Chandu Changes
                        openDialogForSuccessMsg(errMsg, false);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void openDialogForSuccessMsg(String msg, String GIS_ID, String EST_NO, String WORK_ORDER) {

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
        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        // 🔹 Title
        title.setText("Details");

        // 🔹 Build Header + Message
        StringBuilder builder = new StringBuilder();

        if (GIS_ID != null && !GIS_ID.isEmpty()) {
            builder.append("<b>GIS ID:</b> ").append(GIS_ID).append("<br>");
        }

        if (EST_NO != null && !EST_NO.isEmpty()) {
            builder.append("<b>EST No:</b> ").append(EST_NO).append("<br>");
        }

        if (WORK_ORDER != null && !WORK_ORDER.isEmpty()) {
            builder.append("<b>Work Order:</b> ").append(WORK_ORDER).append("<br><br>");
        }

        // 🔴 Message Handling
        if (msg != null && msg.contains("newly  proposed")) {
            builder.append("<b><font color='red'>Invalid Survey</font></b><br><br>")
                    .append(msg);
        } else {
            builder.append(msg);
        }

        message.setText(HtmlCompat.fromHtml(builder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        // 🔹 Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("Close");

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

//    private void openDialogForSuccessMsg(String msg, String GIS_ID, String EST_NO, String WORK_ORDER) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//
//        // 🔹 Create a vertical layout for the title section
//        LinearLayout titleLayout = new LinearLayout(this);
//        titleLayout.setOrientation(LinearLayout.VERTICAL);
//        titleLayout.setPadding(50, 40, 50, 20);
//        titleLayout.setGravity(Gravity.START);
//        titleLayout.setGravity(Gravity.CENTER_HORIZONTAL);
//
//        // 🔹 Common layout params for all title text views
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        params.setMargins(0, 10, 0, 0);
//
//        if (!GIS_ID.isEmpty()) {
//            TextView tvGis = new TextView(this);
//            tvGis.setText("GIS ID: " + GIS_ID);
//            tvGis.setTextSize(14);
//            tvGis.setTextColor(Color.parseColor("#000000"));
//            tvGis.setLayoutParams(params);
//            titleLayout.addView(tvGis);
//        }
//
//        if (!EST_NO.isEmpty()) {
//            TextView tvEst = new TextView(this);
//            tvEst.setText("EST No: " + EST_NO);
//            tvEst.setTextSize(14);
//            tvEst.setTextColor(Color.parseColor("#333333"));
//            tvEst.setLayoutParams(params);
//            titleLayout.addView(tvEst);
//        }
//
//        if (!WORK_ORDER.isEmpty()) {
//            TextView tvWork = new TextView(this);
//            tvWork.setText("Work Order: " + WORK_ORDER);
//            tvWork.setTextSize(14);
//            tvWork.setTextColor(Color.parseColor("#333333"));
//            tvWork.setLayoutParams(params);
//            titleLayout.addView(tvWork);
//        }
//
//        // 🔹 Apply the custom title layout if there’s at least one value
//        if (titleLayout.getChildCount() > 0) {
//            builder.setCustomTitle(titleLayout);
//        }
//
//        // 🔹 Handle message content
//        if (msg.contains("newly  proposed")) {
//            String errMsg = msg;
//            String formattedMsg = "<b><font color='red'>Invalid Survey</font></b><br></br>" + errMsg;
//            builder.setMessage(HtmlCompat.fromHtml(formattedMsg, HtmlCompat.FROM_HTML_MODE_LEGACY));
//        } else {
//            builder.setMessage(msg);
//        }
//
//        // 🔹 Button setup
//        builder.setCancelable(false);
//        builder.setPositiveButton("Close", (dialog, which) -> onBackPressed());
//
//        // 🔹 Show dialog
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }

    private void openDialogForSuccessMsg(String msg, boolean valid) {
        /*androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("Close", (dialog, which) -> {
            Intent intent = new Intent(ApproveBOQActivity.this, ApproveSurvey.class);
            startActivity(intent);
            finish();


        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();*/


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_success, null);
        //Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.setContentView(dialogView);
        dialog.show();
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);
        TextView tv_est_no = dialogView.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = dialogView.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = dialogView.findViewById(R.id.sheet_image);
        title.setText("Pay Attention!!");
        message.setText(msg);

        if (valid) {
            iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.suc));
        } else {
            iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        }

        tv_est_no.setText(getColoredText("Est No: ", EST_NO, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ApproveBOQActivity.this, ApproveSurvey.class);
                startActivity(intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ApproveBOQActivity.this, ApproveSurvey.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void estDataBoQDetailsAPI(String GIS_ID, String EST_NO) {
        loader.setVisibility(View.VISIBLE);
        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            entry.addProperty("EST_NO", EST_NO);
        }
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        Call<JsonObject> responseVoCall;
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            responseVoCall = retrofitService.estLengthDetails(entry);
        } else {
            responseVoCall = retrofitService.estBoqData(entry);
        }
        // responseVoCall = retrofitService.estBoqData(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);

                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            main_layout.setVisibility(View.VISIBLE);
                            layout_btns.setVisibility(View.VISIBLE);
                            tv_error_msg.setVisibility(View.GONE);
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                BoqMaterialModel model = new BoqMaterialModel();
                                model.setMATERIAL_DESCRIPTION(object.has("MATERIAL_DESCRIPTION") && !object.get("MATERIAL_DESCRIPTION").isJsonNull() ? object.get("MATERIAL_DESCRIPTION").getAsString() : "");
                                model.setUNIT(object.has("UNIT") && !object.get("UNIT").isJsonNull() ? object.get("UNIT").getAsString() : "");
                                model.setSAP_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull() ? object.get("SAP_CODE").getAsString() : "");
                                model.setQUANTITY(object.has("QUANTITY") && !object.get("QUANTITY").isJsonNull() ? object.get("QUANTITY").getAsString() : "");
                                model.setDATA_ID(object.has("DATA_ID") && !object.get("DATA_ID").isJsonNull() ? object.get("DATA_ID").getAsString() : "");
                                model.setESTIMATE_NO(object.has("ESTIMATE_NO") && !object.get("ESTIMATE_NO").isJsonNull() ? object.get("ESTIMATE_NO").getAsString() : "");
                                model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                                model.setSURVEY_TYPE(object.has("SURVEY_TYPE") && !object.get("SURVEY_TYPE").isJsonNull() ? object.get("SURVEY_TYPE").getAsString() : "");
                                boqModel.add(model);

                            }
                            setBoqMaterialAdapter();

                        } else {
                            main_layout.setVisibility(View.GONE);
                            layout_btns.setVisibility(View.GONE);
                            tv_error_msg.setVisibility(View.VISIBLE);
                            tv_error_msg.setText(surveyedResponseVo.get("ERR_MSG").getAsString());
                        }
                    } else {
                        openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), GIS_ID, EST_NO, WORK_ORDER);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });

    }

    private void setBoqMaterialAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ApproveBOQActivity.this);
        rv_completed_boq.setLayoutManager(layoutManager);
        rv_completed_boq.setFocusable(false);
        CompletedBOQAdapter adapter = new CompletedBOQAdapter(boqModel, ApproveBOQActivity.this);
        rv_completed_boq.setAdapter(adapter);
        rv_completed_boq.setNestedScrollingEnabled(false);
    }

}