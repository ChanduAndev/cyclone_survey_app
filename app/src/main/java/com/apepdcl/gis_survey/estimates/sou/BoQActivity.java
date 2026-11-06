package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoQActivity extends AppCompatActivity {
    private RecyclerView rv_completed_boq;
    ArrayList<BoqMaterialModel> boqModel;
    private Button btn_complete, btn_cancel;
    private String GIS_ID, EST_NO, DESCRIPTION, WORK_ORDER, EST_NATURE,FEEDERS="";
    GifImageView loader;
    private RetrofitServiceEstimates retrofitService;
    ArrayList<PurposeModel> list;
    private RelativeLayout back_layout, close_layout, main_layout;
    private LinearLayout sap_33_material_layout;
    private ScrollView boq_solar;
    private LinearLayout layout_btns,layout_materials_details,layout_gis_id_details_wo_rdss;
    MaterialCardView layout_gis_id_details;
    private TextView est_no, tv_error_msg, tv_description, gis_id, est_nature, voltage, est_no_wo,gis_id_wo_rdss,tv_est_feeders,estimate_no_tv;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_qactivity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")){
//            setContentView(R.layout.boq_details_solar_dpr);
//        }else {
//            setContentView(R.layout.activity_bo_qactivity);
//        }

        rv_completed_boq = findViewById(R.id.rv_completed_boq);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_complete = findViewById(R.id.btn_complete);
        boqModel = new ArrayList<>();
        list = new ArrayList<>();
        loader = findViewById(R.id.loader);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        main_layout = findViewById(R.id.main_layout);
        sap_33_material_layout = findViewById(R.id.sap_33_material_layout);
        //boq_solar = findViewById(R.id.boq_solar);
        layout_btns = findViewById(R.id.layout_btns);
        est_no = findViewById(R.id.est_no);
        estimate_no_tv = findViewById(R.id.estimate_no_tv);
        voltage = findViewById(R.id.voltage);
        gis_id = findViewById(R.id.gis_id);
        est_nature = findViewById(R.id.est_nature);
        tv_description = findViewById(R.id.tv_description);
        tv_error_msg = findViewById(R.id.tv_error_msg);
        layout_materials_details = findViewById(R.id.layout_materials_details);

        layout_gis_id_details_wo_rdss = findViewById(R.id.layout_gis_id_details_wo_rdss);
        layout_gis_id_details = findViewById(R.id.layout_gis_id_details);
        gis_id_wo_rdss = findViewById(R.id.gis_id_wo_rdss);
        tv_est_feeders = findViewById(R.id.tv_est_feeders);

        GIS_ID = getIntent().getStringExtra("GIS_ID");
        EST_NO = getIntent().getStringExtra("EST_NO");
        WORK_ORDER = getIntent().getStringExtra("WORK_ORDER");
        DESCRIPTION = getIntent().getStringExtra("DESCRIPTION");
        EST_NATURE = getIntent().getStringExtra("EST_NATURE");
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            FEEDERS=getIntent().getStringExtra("FEEDERS");
        }
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
            est_no.setText(WORK_ORDER);
            estimate_no_tv.setText("Work Order");
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            est_no.setText("BoQ details for DTR Code:" + " " + EST_NO);
        } else {
            est_no.setText(EST_NO);
        }
        layout_materials_details.setVisibility(View.VISIBLE);
        tv_description.setText(DESCRIPTION);
        est_nature.setText(EST_NATURE);
        estDataBoQDetailsAPI(GIS_ID, EST_NO);
        gis_id.setText(GIS_ID);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCompleteDialog(GIS_ID);
                //callSurveyCompleteAPI(GIS_ID);
            }
        });

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            est_no.setVisibility(View.VISIBLE);
            est_nature.setVisibility(View.GONE);
            est_no.setText("DTR Code : " + EST_NO);
            voltage.setText("CODE");
        }

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            layout_materials_details.setVisibility(View.GONE);
            layout_gis_id_details_wo_rdss.setVisibility(View.VISIBLE);
            layout_gis_id_details.setVisibility(View.GONE);
            tv_error_msg.setVisibility(View.GONE);
            gis_id_wo_rdss.setText("GIS ID: "+GIS_ID);
            tv_est_feeders.setText("Feeders : "+FEEDERS);
        }

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        ViewCompat.setOnApplyWindowInsetsListener(layout_btns, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    bottomInset
            );
            return insets;
        });

        ViewCompat.requestApplyInsets(layout_btns);


    }


    private void openCompleteDialog(String GIS_ID) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_to_be_integrated, null);
        dialog.setContentView(dialogView);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.show();
        View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackground(null); // remove default white background
        }
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);
        title.setText("Complete Survey!!");
        message.setText("Once Completed, Survey can't be modified. Verify the GIS Map and Materials (BOQ) before updating the completion.");

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

    private void estDataBoQDetailsAPI(String GIS_ID, String EST_NO) {
        loader.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
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

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {

                            main_layout.setVisibility(View.VISIBLE);
                            sap_33_material_layout.setVisibility(View.VISIBLE);
                            layout_btns.setVisibility(View.VISIBLE);
                            tv_error_msg.setVisibility(View.GONE);
//                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")){
//                                boq_solar.setVisibility(View.VISIBLE);
//                                layout_btns.setVisibility(View.GONE);
//                                tv_error_msg.setVisibility(View.GONE);
//                            }
//                            else {
//                                main_layout.setVisibility(View.VISIBLE);
//                                sap_33_material_layout.setVisibility(View.VISIBLE);
//                                layout_btns.setVisibility(View.VISIBLE);
//                                tv_error_msg.setVisibility(View.GONE);
//
//                            }

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
//                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
//                                for (int i = 0; i < array.size(); i++) {
//                                    JsonObject object = array.get(i).getAsJsonObject();
//                                    BoqMaterialModel model = new BoqMaterialModel();
//                                    model.setCIR_NAME(object.has("CIR_NAME") && !object.get("CIR_NAME").isJsonNull() ? object.get("CIR_NAME").getAsString() : "");
//                                    model.setDTR16(object.has("DTR16") && !object.get("DTR16").isJsonNull() ? object.get("DTR16").getAsString() : "");
//                                    model.setDTR25(object.has("DTR25") && !object.get("DTR25").isJsonNull() ? object.get("DTR25").getAsString() : "");
//                                    model.setDTR40(object.has("DTR40") && !object.get("DTR40").isJsonNull() ? object.get("DTR40").getAsString() : "");
//                                    model.setDTR63(object.has("DTR63") && !object.get("DTR63").isJsonNull() ? object.get("DTR63").getAsString() : "");
//                                    model.setDTR100(object.has("DTR100") && !object.get("DTR100").isJsonNull() ? object.get("DTR100").getAsString() : "");
//                                    model.setDTR160(object.has("DTR160") && !object.get("DTR160").isJsonNull() ? object.get("DTR160").getAsString() : "");
//                                    model.setLN63(object.has("LN63") && !object.get("LN63").isJsonNull() ? object.get("LN63").getAsString() : "");
//                                    model.setLC6311(object.has("LC6311") && !object.get("LC6311").isJsonNull() ? object.get("LC6311").getAsString() : "");
//                                    model.setLN11(object.has("LN11") && !object.get("LN11").isJsonNull() ? object.get("LN11").getAsString() : "");
//                                    model.setLCLT3W5W(object.has("LCLT3W5W") && !object.get("LCLT3W5W").isJsonNull() ? object.get("LCLT3W5W").getAsString() : "");
//                                    model.setLNLT3W(object.has("LNLT3W") && !object.get("LNLT3W").isJsonNull() ? object.get("LNLT3W").getAsString() : "");
//                                    model.setLNLT4W(object.has("LNLT4W") && !object.get("LNLT4W").isJsonNull() ? object.get("LNLT4W").getAsString() : "");
//                                    model.setLNLT5W(object.has("LNLT5W") && !object.get("LNLT5W").isJsonNull() ? object.get("LNLT5W").getAsString() : "");
//                                    boqModel.add(model);
//
//                                }
//
//                            }
//                            else {
//                                for (int i = 0; i < array.size(); i++) {
//                                    JsonObject object = array.get(i).getAsJsonObject();
//                                    BoqMaterialModel model = new BoqMaterialModel();
//                                    model.setMATERIAL_DESCRIPTION(object.has("MATERIAL_DESCRIPTION") && !object.get("MATERIAL_DESCRIPTION").isJsonNull() ? object.get("MATERIAL_DESCRIPTION").getAsString() : "");
//                                    model.setUNIT(object.has("UNIT") && !object.get("UNIT").isJsonNull() ? object.get("UNIT").getAsString() : "");
//                                    model.setSAP_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull() ? object.get("SAP_CODE").getAsString() : "");
//                                    model.setQUANTITY(object.has("QUANTITY") && !object.get("QUANTITY").isJsonNull() ? object.get("QUANTITY").getAsString() : "");
//                                    model.setDATA_ID(object.has("DATA_ID") && !object.get("DATA_ID").isJsonNull() ? object.get("DATA_ID").getAsString() : "");
//                                    model.setESTIMATE_NO(object.has("ESTIMATE_NO") && !object.get("ESTIMATE_NO").isJsonNull() ? object.get("ESTIMATE_NO").getAsString() : "");
//                                    model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
//                                    model.setSURVEY_TYPE(object.has("SURVEY_TYPE") && !object.get("SURVEY_TYPE").isJsonNull() ? object.get("SURVEY_TYPE").getAsString() : "");
//                                    boqModel.add(model);
//
//                                }
//                            }

                            setBoqMaterialAdapter();

                        } else {
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
                                main_layout.setVisibility(View.GONE);
                                layout_btns.setVisibility(View.GONE);
                                tv_error_msg.setText(surveyedResponseVo.get("ERR_MSG").getAsString());
                                tv_error_msg.setVisibility(View.GONE);
                                main_layout.setVisibility(View.VISIBLE);
                            }else {
                                main_layout.setVisibility(View.GONE);
                                layout_btns.setVisibility(View.GONE);
                                tv_error_msg.setText(surveyedResponseVo.get("ERR_MSG").getAsString());
                                tv_error_msg.setVisibility(View.VISIBLE);
                                main_layout.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), GIS_ID, EST_NO, WORK_ORDER);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
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
            builder.append("<b>Est No:</b> ").append(EST_NO).append("<br>");
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
//            tvEst.setText("Est No: " + EST_NO);
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
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }


    private void callSurveyCompleteAPI(String GIS_ID) {
        loader.setVisibility(View.VISIBLE);
        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        entry.addProperty("STATUS", 5);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(entry);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        // Log.v("DATA===>>", "" + jsonArray);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.completeSurvey(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                //  Log.v("RESPONSE===>>", "" + response.body());
                //  System.out.println("COMPLETE ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        //openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString());
                        openDialogForCompleteSuccess(
                                surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                        ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                        : "Please Contact GIS Team"
                        );
                        //Toast.makeText(CompleteSurvey.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_LONG).show();
                    } else {
                        openDialogForCompleteSuccess(
                                surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                        ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                        : "Please Contact GIS Team"
                        );
                        //openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString());
                        // Toast.makeText(CompleteSurvey.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void openDialogForCompleteSuccess(String msg) {
       /* new AlertDialog.Builder(BoQActivity.this)
                .setTitle("Pay Attention!!")
                .setIcon(R.drawable.app_logo)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentPf = new Intent(BoQActivity.this, CompleteSurvey.class);
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                        dialog.dismiss();
                    }
                })
                .show();*/



        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_success, null);
        dialog.setContentView(dialogView);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.show();
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);
        title.setText("Pay Attention!!");
        message.setText(msg);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intentPf = new Intent(BoQActivity.this, CompleteSurvey.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(BoQActivity.this, CompleteSurvey.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                dialog.dismiss();
            }
        });

    }


    private void setBoqMaterialAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BoQActivity.this);
        rv_completed_boq.setLayoutManager(layoutManager);
        rv_completed_boq.setFocusable(false);
        CompletedBOQAdapter adapter = new CompletedBOQAdapter(boqModel, BoQActivity.this);
        rv_completed_boq.setAdapter(adapter);
        rv_completed_boq.setNestedScrollingEnabled(false);
    }
}