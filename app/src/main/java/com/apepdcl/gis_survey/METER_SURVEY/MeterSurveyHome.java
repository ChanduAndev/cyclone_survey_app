package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 06-06-2026.
 */
public class MeterSurveyHome extends AppCompatActivity {


    ArrayList<SlideModel> slideList;
    ImageView profile_layout;

    TextView tv_total, tv_start_survey, tv_process, tv_continue, tv_transmit, tv_complete, tv_completed;

    MeterSurveyDatabase meterSurveyDatabase;

    private RetrofitServiceMeter retrofitServiceMeter;

    int totalCount = 0, completedCount = 0, inProgressCount = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_survey_home);
        meterSurveyDatabase = new MeterSurveyDatabase(this);
        profile_layout = findViewById(R.id.profile_layout);
        ImageSlider imageSlider = findViewById(R.id.image_slider_estimate);
        slideList = new ArrayList<>();
        tv_total = findViewById(R.id.tv_total);
        tv_start_survey = findViewById(R.id.tv_start_survey);
        tv_process = findViewById(R.id.tv_process);
        tv_continue = findViewById(R.id.edit_survey);
        tv_transmit = findViewById(R.id.tv_transmit);
        tv_complete = findViewById(R.id.tv_complete);
        tv_completed = findViewById(R.id.tv_completed);
        SlideModel m1 = new SlideModel(R.drawable.image1, ScaleTypes.FIT);
        SlideModel m2 = new SlideModel(R.drawable.image2, ScaleTypes.FIT);
        SlideModel m3 = new SlideModel(R.drawable.b1, ScaleTypes.FIT);
        SlideModel m4 = new SlideModel(R.drawable.b2, ScaleTypes.FIT);
        slideList.add(m4);
        slideList.add(m3);
        slideList.add(m1);
        imageSlider.setImageList(slideList);

        getSurveyCounts();

        if (meterSurveyDatabase.getAllMeterSurveyData().isEmpty()){
            tv_continue.setText("  Start  ");
        }else{

        }

        tv_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (meterSurveyDatabase.getAllMeterSurveyData().isEmpty()){
                    Intent intent = new Intent(MeterSurveyHome.this, PendingDtrList.class);
                    startActivity(intent);

                }else {
                    openAlertDialog("Please Transmit the pending data");
                   // Toast.makeText(MeterSurveyHome.this, "Please Transmit the pending data", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tv_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (meterSurveyDatabase.getAllMeterSurveyData().isEmpty()){
                    Toast.makeText(MeterSurveyHome.this, "No Data to Transmit", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MeterSurveyHome.this, TransmitMeterHome.class);
                    startActivity(intent);
                }
            }
        });

        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (meterSurveyDatabase.getAllMeterSurveyData().isEmpty()){
                    Intent intent = new Intent(MeterSurveyHome.this, PendingDtrList.class);
                    startActivity(intent);

                }else {
                    openAlertDialog("Please Transmit the pending data");
                    Toast.makeText(MeterSurveyHome.this, "Please Transmit the pending data", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MeterSurveyHome.this, ContinueDtrList.class);
//                    startActivity(intent);
                }
            }
        });

        tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeterSurveyHome.this, ToBeComplete.class);
                startActivity(intent);
            }
        });

        tv_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeterSurveyHome.this, CompletedSurvey.class);
                startActivity(intent);
            }
        });
        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeterSurveyHome.this, ReaderProfile.class);
                startActivity(intent);
            }
        });
    }

    private void openAlertDialog(String message) {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        LinearLayout estimate_details = view.findViewById(R.id.estimate_details);
        estimate_details.setVisibility(View.GONE);
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        // Set Data
        title.setText("Pay Attention!!");
        msg.setText(message);

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        okBtn.setText("Okay");

        // Click action
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }
    private void getSurveyCounts() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("MOBILE_NO", sharedPreferences.getString(MeterConstants.METER_MOBILE_NO,""));
        dataMap.put("VERSION", MobileConstants.CONSUMERS);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(dataMap).getAsJsonObject();
        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.getUserWiseCounts(jsonObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



                if (response.body() == null) {
                    Toast.makeText(MeterSurveyHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }



                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(MeterSurveyHome.this,
                            "Invalid response from server: STATUS missing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    if (errorMessage.contains("update to Version")) {
                        openDialogForVersion();
                    }
                    Toast.makeText(MeterSurveyHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                JsonArray dtr_array = surveyedResponseVo.getAsJsonArray("DATA");

                for (int i = 0; i < dtr_array.size(); i++) {

                    JsonObject jsonObject1 = dtr_array.get(i).getAsJsonObject();

                    if (jsonObject1.get("STATUS").getAsString().equals("I")) {
                        inProgressCount = jsonObject1.get("COUNTS").getAsInt();
                    }


                    if (jsonObject1.get("STATUS").getAsString().equals("Y")) {
                        completedCount = jsonObject1.get("COUNTS").getAsInt();
                    }

                    totalCount = inProgressCount+completedCount;


                }

                tv_total.setText(""+totalCount);
                tv_completed.setText(""+completedCount);
                tv_process.setText(""+inProgressCount);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void openDialogForVersion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeterSurveyHome.this);

        alertDialogBuilder.setTitle("A new Version is available!");
        //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
        alertDialogBuilder.setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                        "Please Download latest Mobile App Apk"))
                //.setMessage("Before upgrade,please transmit the data to server ")
                /*  .setMessage("Before upgrade,please transmit the data to server \n " +
                          "Please Click to Download latest app \n "+link1+" Check this link out</a>")*/
                .setCancelable(false)
                .setPositiveButton("Download",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url = MobileConstants.APK_URL;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);

                            }
                        })

                .setNegativeButton("Transmit Data", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (meterSurveyDatabase.getAllMeterSurveyData().isEmpty()){
            tv_continue.setText("  Start  ");
        }else {
            tv_continue.setText("Continue");
        }


    }
}
