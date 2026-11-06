package com.apepdcl.gis_survey.estimates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.estimates.models.WorkInvolvementModel;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.LocationDetails;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstimatesDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<SlideModel> slideList;
    private LinearLayout layout_start_survey, layout_data_transmit_estimate;
    ArcGISDatabaseEstimates databaseEstimates;
    private boolean is33KVSelected;
    private boolean is11KVSelected;
    private boolean isDTRSelected;
    private boolean isLTSelected;
    private boolean isComplted33kVSurvey;
    private boolean isComplted11kVSurvey;
    private boolean isCompltedLTSurvey;
    private String EST_NO, GIS_ID, FROM;
    private ProgressDialog progressDialog;
    private JsonArray jsonArray;
    RetrofitServiceEstimates retrofitService;
    RelativeLayout back_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimates_dashboard);
        layout_start_survey = findViewById(R.id.layout_start_survey);
        layout_data_transmit_estimate = findViewById(R.id.layout_data_transmit_estimate);
        back_layout = findViewById(R.id.back_layout);
        progressDialog = new ProgressDialog(EstimatesDashboardActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        databaseEstimates = new ArcGISDatabaseEstimates(this);
        EST_NO = getIntent().getStringExtra("EST_NO");
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        is33KVSelected = getIntent().getBooleanExtra("33KV", false);
        is11KVSelected = getIntent().getBooleanExtra("11KV", false);
        isDTRSelected = getIntent().getBooleanExtra("DTR", false);
        isLTSelected = getIntent().getBooleanExtra("LT", false);


        layout_start_survey.setOnClickListener(this);
        layout_data_transmit_estimate.setOnClickListener(this);

        ImageSlider imageSlider = findViewById(R.id.image_slider_estimate);
        slideList = new ArrayList<>();

        for (int i = 0; i < DashBoard.bannerList.size(); i++) {
            SlideModel m1 = new SlideModel(DashBoard.bannerList.get(i).getIMAGE_URL(), ScaleTypes.FIT);
            slideList.add(m1);

        }


        /*SlideModel m1 = new SlideModel(R.drawable.image1, ScaleTypes.FIT);
        SlideModel m2 = new SlideModel(R.drawable.image2, ScaleTypes.FIT);
        SlideModel m3 = new SlideModel(R.drawable.b1, ScaleTypes.FIT);
        SlideModel m4 = new SlideModel(R.drawable.b2, ScaleTypes.FIT);
        slideList.add(m4);
        slideList.add(m3);
        slideList.add(m1);*/
        imageSlider.setImageList(slideList);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<WorkInvolvementModel> workModel = databaseEstimates.getWorkInvolvementData(EST_NO,GIS_ID);

//        ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true);
//
//        for (int i = 0; i < polesData.size(); i++) {
//            if (polesData.get(i).isNEW_DATA_ADDED().equalsIgnoreCase("Yes")) {
//                Toast.makeText(this, "new Data Is there", Toast.LENGTH_SHORT).show();
//            }
//
//        }


        Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
        String json1 = gson1.toJson(workModel);
        // Log.d("WORK_MODELS----->", "Fetched Data: " + json1);


        isComplted33kVSurvey = databaseEstimates.get33kvSurveyStatusNEW(EST_NO, GIS_ID);
        isComplted11kVSurvey = databaseEstimates.get11kvSurveyStatusNEW(EST_NO, GIS_ID);
        isCompltedLTSurvey = databaseEstimates.getLTSurveyStatusNEW(EST_NO, GIS_ID);

        //  Log.d("COMPLETE------>", "onCreate: " + isComplted33kVSurvey);


    }

    private void openAlertDialog(boolean newData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title")
                .setMessage("Are you sure you want to transmit")
                .setPositiveButton("Yes", (dialog, id) -> {
                    if (EST_NO != null) {

                        if (ConnectivityChecking.checkConnection(EstimatesDashboardActivity.this)) {
                            transmitImageData();
                        } else {
                            ConnectivityChecking.checkConnection(EstimatesDashboardActivity.this);
                        }

                    } else {
                        transmitImageData1(true);
                    }
                    dialog.dismiss();

                })
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openAlertDialogForWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String warningMessage = "<b><font color='#FF0000'>Data Pending for transmission.</font></b>";
        builder.setTitle(Html.fromHtml(warningMessage))
                .setMessage("Please transmit the data and continue the survey")
                .setPositiveButton("Okay", (dialog, id) -> {
                    dialog.dismiss();

                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void transmitImageData() {
        progressDialog.show();
        ArrayList<NewModel> polesData = databaseEstimates.getNewSurveyData(EST_NO, "ALL", true,GIS_ID);
        for (NewModel model : polesData) {
            model.setGIS_ID(GIS_ID);
            model.setIMEI_NO("");

        }
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(polesData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
            //  System.out.println(jsonArray);
        }
        String json = gson.toJson(polesData);

        // Log.v("DATA===>>", "" + json);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingData(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.cancel();
                //  Log.v("RESPONSE===>>", "" + response.body());
                //  System.out.println("ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        Toast.makeText(EstimatesDashboardActivity.this, "Data Transmitted Successfully..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EstimatesDashboardActivity.this, EstimatesDashboardActivity.class);
                        startActivity(intent);
//                        boolean surveyStatusNEW = databaseEstimates.deleteSurveyStatusNEW(EST_NO);
//                        boolean newSurveyData = databaseEstimates.deleteNewSurveyData(EST_NO);
//                        if (surveyStatusNEW && newSurveyData) {
//
//                            onBackPressed();
//                        } else {
//                            Toast.makeText(EstimatesDashboardActivity.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        openDialogForSuccessMsg("Failed Please try again",GIS_ID,EST_NO,"");
                        //Toast.makeText(EstimatesDashboardActivity.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void openDialogForSuccessMsg(String msg, String GIS_ID, String EST_NO, String WORK_ORDER) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        // 🔹 Create a vertical layout for the title section
        LinearLayout titleLayout = new LinearLayout(this);
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setPadding(50, 40, 50, 20);
        titleLayout.setGravity(Gravity.START);
        titleLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        // 🔹 Common layout params for all title text views
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 0);

        if (!GIS_ID.isEmpty()) {
            TextView tvGis = new TextView(this);
            tvGis.setText("GIS ID: " + GIS_ID);
            tvGis.setTextSize(14);
            tvGis.setTextColor(Color.parseColor("#000000"));
            tvGis.setLayoutParams(params);
            titleLayout.addView(tvGis);
        }

        if (!EST_NO.isEmpty()) {
            TextView tvEst = new TextView(this);
            tvEst.setText("EST No: " + EST_NO);
            tvEst.setTextSize(14);
            tvEst.setTextColor(Color.parseColor("#333333"));
            tvEst.setLayoutParams(params);
            titleLayout.addView(tvEst);
        }

        if (!WORK_ORDER.isEmpty()) {
            TextView tvWork = new TextView(this);
            tvWork.setText("Work Order: " + WORK_ORDER);
            tvWork.setTextSize(14);
            tvWork.setTextColor(Color.parseColor("#333333"));
            tvWork.setLayoutParams(params);
            titleLayout.addView(tvWork);
        }

        // 🔹 Apply the custom title layout if there’s at least one value
        if (titleLayout.getChildCount() > 0) {
            builder.setCustomTitle(titleLayout);
        }

        // 🔹 Handle message content
        if (msg.contains("newly  proposed")) {
            String errMsg = msg;
            String formattedMsg = "<b><font color='red'>Invalid Survey</font></b><br></br>" + errMsg;
            builder.setMessage(HtmlCompat.fromHtml(formattedMsg, HtmlCompat.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setMessage(msg);
        }

        // 🔹 Button setup
        builder.setCancelable(false);
        builder.setPositiveButton("Close", (dialog, which) -> onBackPressed());

        // 🔹 Show dialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void transmitImageData1(boolean newData) {
        progressDialog.show();
        ArrayList<NewModel> polesData = databaseEstimates.getAllSurveyData();
//        ArrayList<NewModel> filteredData = new ArrayList<>();
//        for (NewModel data : polesData) {
//            if (data.isNEW_DATA_ADDED().equalsIgnoreCase("Yes")) {
//                filteredData.add(data);
//            }
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String json = gson.toJson(data);
//            Log.d("RemainingDATA---->", "onCreate: " + json);
//        }

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(polesData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
            //  System.out.println(jsonArray);
        }
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        String json = gson.toJson(polesData);

        //   Log.v("DATA===>>", "" + json);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingData(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.cancel();
                //  Log.v("RESPONSE===>>", "" + response.body());
                // System.out.println("ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        Toast.makeText(EstimatesDashboardActivity.this, "Data Transmitted Successfully..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EstimatesDashboardActivity.this, EstimatesDashboardActivity.class);
                        startActivity(intent);
                        boolean surveyStatusNEW = databaseEstimates.deleteALLSurveyStatusNEW();
                        boolean newSurveyData = databaseEstimates.deleteAllNewSurveyData();
                        if (surveyStatusNEW && newSurveyData) {
                            //Toast.makeText(EstimatesDashboardActivity.this, "Deleted Succesfull", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(EstimatesDashboardActivity.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == layout_start_survey) {
            ArrayList<NewModel> polesData = databaseEstimates.getAllSurveyData();

            boolean hasNewData = false;
            for (NewModel pole : polesData) {
                String newDataAdded = pole.isNEW_DATA_ADDED();
                if (newDataAdded != null && newDataAdded.equalsIgnoreCase("Yes")) {
                    hasNewData = true;
                    break;
                }
            }

            if (hasNewData) {
                openAlertDialogForWarning();
            } else {
                startActivity(new Intent(EstimatesDashboardActivity.this, EstimatesHome.class));
            }
        }

        if (v == layout_data_transmit_estimate) {
            ArrayList<NewModel> polesData = databaseEstimates.getAllSurveyData();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(polesData);
            //  Log.d("Debugg", "onCreate: " + json);
            for (int i = 0; i < polesData.size(); i++) {
                String newDataAdded = polesData.get(i).isNEW_DATA_ADDED();

                if (newDataAdded != null && newDataAdded.equalsIgnoreCase("Yes")) {
                    openAlertDialog(true);
                    // Log.d("DATATATATATA---->>", "onClick: " + newDataAdded);
                }
            }


//            boolean isReadyForTransmission = true;
//
//            if (is33KVSelected && !isComplted33kVSurvey) {
//                isReadyForTransmission = false;
//            }
//
//            if (is11KVSelected && !isComplted11kVSurvey) {
//                isReadyForTransmission = false;
//            }
//
//            if (isLTSelected && !isCompltedLTSurvey) {
//                isReadyForTransmission = false;
//            }
//
//            if (isReadyForTransmission) {
//                openAlertDialog();
//            }
        }

    }
}