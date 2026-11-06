package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Profile;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.BannerModel;
import com.apepdcl.gis_survey.estimates.models.DialogDataModel;
import com.apepdcl.gis_survey.estimates.models.InfoModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.CustomDialog;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyList extends AppCompatActivity {

    RelativeLayout back_layout, close_layout, profile_layout, loader_layout;
    GifImageView loader;
    TextView tv_pending, tv_process, tv_completed, tv_total, tv_count_to_be_integrated, tv_integrate_data_to_existing_gis_data, tv_pending_txt, tv_admin_approval, tv_rejected_admin, rejected_by_admin, admin_approval;
    TextView start_survey, tv_continue, tv_transmit, tv_complete, tv_approve, home_heading, tv_progress_bar;
    ScrollView main_layout;
    LinearLayout no_data, layout_approve, layout_pending_to_be_integrated, rejected_by_admin_layout_new, pending_for_admin_approval_layout_new;

    CardView layout_integrated, approvalCard, pending_work_card;
    LinearLayout tv_approve_layout;
    RetrofitServiceEstimates retrofitService;
    int count_pending = 0;
    int count_total = 0;
    int count_under_process = 0;
    int count_completed = 0;
    int count_to_be_integrated = 0;
    int count_approved = 0;
    int count_of_approved_admin = 0;
    int count_of_rejected_admin = 0;
    private ProgressDialog progressDialog;


    ArcGISDatabaseEstimates databaseEstimates;
    ArrayList<NewModel> allData;
    JsonArray jsonArray;

    //LOCATIONS EDIT
    TextView edit_survey;
    LinearLayout layout_edit_survey, layout_tv_transmit, layout_tv_complete;
    TextView to_be_integrated_layout_new;
    public static boolean IS_EDIT = false;
    public static String EDIT_ESTIMATE_NO;

    public static String EDIT_GIS_ID;

    ArrayList<NewModel> editData;
    ArrayList<EditModel> onlyLatLongData;

    //NEW
    public static ArrayList<InfoModel> infoList;
    private boolean isDialogShown = false;
    public static List<String> offline;
    public static List<String> online;
    public static ArrayList<BannerModel> bannerList;
    public static ArrayList<DialogDataModel> dialogDataModels;
    ImageSlider imageSlider;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_survey_list);
        setContentView(R.layout.activity_survey_list_new);
        getSupportActionBar().hide();

        //NEW
        infoList = new ArrayList<>();

        //LOCATIONS EDIT
        //  edit_survey = findViewById(R.id.edit_survey);

        layout_edit_survey = findViewById(R.id.layout_edit_survey);
        layout_tv_transmit = findViewById(R.id.layout_tv_transmit);
        layout_tv_complete = findViewById(R.id.layout_tv_complete);
        imageSlider = findViewById(R.id.image_slider);
        IS_EDIT = false;
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            offline = new ArrayList<>();
            online = new ArrayList<>();
            bannerList = new ArrayList<>();
            dialogDataModels = new ArrayList<>();

        }
        progressDialog = new ProgressDialog(SurveyList.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        back_layout = findViewById(R.id.back_layout);
        profile_layout = findViewById(R.id.profile_layout);
        loader = findViewById(R.id.loader);
        tv_pending = findViewById(R.id.tv_pending);
        tv_process = findViewById(R.id.tv_process);
        tv_completed = findViewById(R.id.tv_completed);
        // tv_approved = findViewById(R.id.tv_approved);
        main_layout = findViewById(R.id.main_layout);
        no_data = findViewById(R.id.no_data);
        tv_total = findViewById(R.id.tv_total);
        start_survey = findViewById(R.id.start_survey);
        tv_continue = findViewById(R.id.tv_continue);
//        tv_transmit = findViewById(R.id.tv_transmit);
//        tv_complete = findViewById(R.id.tv_complete);
        //tv_approve = findViewById(R.id.tv_approve);
        home_heading = findViewById(R.id.home_heading);
        tv_pending_txt = findViewById(R.id.tv_pending_txt);
        //sub_tv_complete = findViewById(R.id.sub_tv_complete);
        tv_count_to_be_integrated = findViewById(R.id.tv_count_to_be_integrated);
        tv_admin_approval = findViewById(R.id.tv_admin_approval);
        tv_rejected_admin = findViewById(R.id.tv_rejected_admin);
        layout_integrated = findViewById(R.id.layout_integrated);
        tv_integrate_data_to_existing_gis_data = findViewById(R.id.tv_integrate_data_to_existing_gis_data);
        tv_progress_bar = findViewById(R.id.tv_progress_bar);
        loader_layout = findViewById(R.id.loader_layout);
        // layout_approve = findViewById(R.id.layout_approve);
        rejected_by_admin = findViewById(R.id.rejected_by_admin);
        admin_approval = findViewById(R.id.admin_approval);
        layout_pending_to_be_integrated = findViewById(R.id.layout_pending_to_be_integrated);
        approvalCard = findViewById(R.id.approvalCard);
        pending_work_card = findViewById(R.id.pending_work_card);

        pending_for_admin_approval_layout_new = findViewById(R.id.pending_for_admin_approval_layout_new);
        rejected_by_admin_layout_new = findViewById(R.id.rejected_by_admin_layout_new);
        to_be_integrated_layout_new = findViewById(R.id.to_be_integrated_layout_new);
        tv_approve_layout = findViewById(R.id.tv_approve_layout);


        databaseEstimates = new ArcGISDatabaseEstimates(this);
        allData = databaseEstimates.getCurrentSurveyData();

        //LOCATION EDIT
        editData = databaseEstimates.getSpecificCurrentSurveyData();
        onlyLatLongData = databaseEstimates.getEditLocationsData();

//        if (allData.isEmpty() && onlyLatLongData.isEmpty()) {
//            tv_transmit.setBackground(getResources().getDrawable(R.drawable.disable_border));
//            tv_continue.setBackground(getResources().getDrawable(R.drawable.adapter_border));
//
//        } else {
//            tv_transmit.setBackground(getResources().getDrawable(R.drawable.adapter_border));
//            tv_continue.setBackground(getResources().getDrawable(R.drawable.disable_border));
//        }


        main_layout.setVisibility(View.GONE);
        // getEstimatesCount();
        //NEW
        if (ConnectivityChecking.checkConnection(SurveyList.this)) {
            getInformation();
        } else {
            CommanUtilits.internetDialog(SurveyList.this);
        }
        if (ConnectivityChecking.checkConnection(SurveyList.this)) {
            getEstimatesCountDB();
        } else {
            CommanUtilits.internetDialog(SurveyList.this);
        }
        //getInformation();
        //getEstimatesCountDB();

        if ("ESTIMATES".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            tv_pending_txt.setText("Total  Estimates");
            home_heading.setText("Estimates");
            //sub_tv_complete.setText("Survey Completed");
            layout_integrated.setVisibility(View.GONE);
            back_layout.setVisibility(View.VISIBLE);
            layout_pending_to_be_integrated.setVisibility(View.GONE);
        } else if ("SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            tv_pending_txt.setText("Total pending Solarization of SC ST");
            home_heading.setText("Solarization of SC ST");
            // sub_tv_complete.setText("Survey Completed");
            layout_integrated.setVisibility(View.GONE);
            back_layout.setVisibility(View.VISIBLE);
            layout_pending_to_be_integrated.setVisibility(View.GONE);
            //layout_approve.setVisibility(View.VISIBLE);
        } else if ("WORK_ORDER".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            home_heading.setText("Work Order");
            // sub_tv_complete.setText("Integrated to existing GIS data");
            tv_pending_txt.setText("Total Work Orders");
            layout_integrated.setVisibility(View.VISIBLE);
            layout_pending_to_be_integrated.setVisibility(View.VISIBLE);
            back_layout.setVisibility(View.VISIBLE);
        }

        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            home_heading.setText("RDSS Asset Survey");
            // sub_tv_complete.setText("Integrated to existing GIS data");
            tv_pending_txt.setText("Total Pending");
            approvalCard.setVisibility(View.GONE);
            layout_integrated.setVisibility(View.VISIBLE);
            profile_layout.setVisibility(View.VISIBLE);
            back_layout.setVisibility(View.GONE);
            getBannerImagesApi();
        } else {
            approvalCard.setVisibility(View.VISIBLE);
        }


        //IMAGE SLIDER
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideList = new ArrayList<>();

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {

        } else {
            for (int i = 0; i < DashBoard.bannerList.size(); i++) {
                SlideModel m1 = new SlideModel(DashBoard.bannerList.get(i).getIMAGE_URL(), ScaleTypes.FIT);
                slideList.add(m1);

            }
        }


      /*  SlideModel m1 = new SlideModel(R.drawable.image1, ScaleTypes.FIT);
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

//        close_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(SurveyList.this, Profile.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        pending_work_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count_pending != 0) {
                    if (allData.isEmpty() && onlyLatLongData.isEmpty()) {
                        Intent intentPf = new Intent(SurveyList.this, EstimatesHome.class);
                        intentPf.putExtra("type", "1");
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    } else {
                        Toast.makeText(SurveyList.this, "PLEASE TRANSMIT PENDING SURVEYED DATA BEFORE STARTING A NEW SURVEY", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SurveyList.this, "NO PENDING SURVEYS", Toast.LENGTH_SHORT).show();
                }


            }
        });

        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allData.isEmpty() && onlyLatLongData.isEmpty()) {

                    if (count_under_process == 0) {
                        Toast.makeText(SurveyList.this, "There are no survey to Continue", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentPf = new Intent(SurveyList.this, EstimatesHome.class);
                        intentPf.putExtra("type", "2");
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    }


                } else {

                    Toast.makeText(SurveyList.this, "PLEASE TRANSMIT DATA", Toast.LENGTH_SHORT).show();
                }


            }
        });

        layout_tv_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allData.isEmpty() && onlyLatLongData.isEmpty()) {
                    Toast.makeText(SurveyList.this, "You don't have any Surveyed data to transmit", Toast.LENGTH_SHORT).show();
                } else {

                    String surveyData;
                    if (allData.isEmpty()) {
                        surveyData = onlyLatLongData.get(0).getEDIT_TYPE();
                    } else {
                        surveyData = allData.get(0).getSURVEY_FROM();
                    }

                    if (surveyData.equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        Intent intentPf = new Intent(SurveyList.this, TransmitSurvey.class);
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    } else {
                        String msg = "";
                        if (surveyData.equalsIgnoreCase("WORK_ORDER")) {
                            msg = "Work Order";
                        } else if (surveyData.equalsIgnoreCase("SOLAR_DPR")) {
                            msg = "Solarization of SC ST ";
                        } else {
                            msg = "Estimates";
                        }
                        openDialogForTransmit(msg);
                        //Toast.makeText(SurveyList.this, "Please Transmit Pending data from" + " " + surveyData, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        layout_tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allData.isEmpty() && onlyLatLongData.isEmpty()) {
                    if (count_under_process == 0) {
                        Toast.makeText(SurveyList.this, "There are no survey to Complete", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentPf = new Intent(SurveyList.this, CompleteSurvey.class);
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    }


                } else {
                    String surveyData = "";

                    if (allData != null && !allData.isEmpty()) {
                        surveyData = allData.get(0).getSURVEY_FROM();

                        if (surveyData != null && surveyData.equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                            openCompleteDialog("");
                        } else {
                            openCompleteDialog(surveyData);
                        }
                    } else {
                        openCompleteDialog(""); // or show message/log
                    }
                }
            }
        });

        //NEW CHANGES ON LT
        tv_approve_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES") || LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    System.out.println("count_completed_in_click:-->" + count_completed);
                    if (count_to_be_integrated == 0) {
                        Toast.makeText(SurveyList.this, "There are no survey to Approve", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentPf = new Intent(SurveyList.this, ApproveSurvey.class);
                        intentPf.putExtra("STATUS", count_completed);
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    }
                } else {
                    if (count_completed == 0) {
                        Toast.makeText(SurveyList.this, "There are no survey to Approve", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentPf = new Intent(SurveyList.this, ApproveSurvey.class);
                        intentPf.putExtra("STATUS", count_to_be_integrated);
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    }
                }
            }
        });
//NEW CHANGES ON LT
        to_be_integrated_layout_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count_to_be_integrated == 0) {
                    Toast.makeText(SurveyList.this, "There are no survey to integrate", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentPf = new Intent(SurveyList.this, ToBeIntegratedActivity.class);
                    intentPf.putExtra("INTG_STATUS", "A");
                    startActivity(intentPf);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                }
            }
        });

        pending_for_admin_approval_layout_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count_of_approved_admin == 0) {
                    Toast.makeText(SurveyList.this, "There are no survey to pending from admin", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentPf = new Intent(SurveyList.this, ToBeIntegratedActivity.class);
                    intentPf.putExtra("INTG_STATUS", "P");
                    startActivity(intentPf);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                }
            }
        });

        rejected_by_admin_layout_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count_of_rejected_admin == 0) {
                    Toast.makeText(SurveyList.this, "There are no survey to rejected by admin", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentPf = new Intent(SurveyList.this, ToBeIntegratedActivity.class);
                    intentPf.putExtra("INTG_STATUS", "R");
                    startActivity(intentPf);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                }
            }
        });


        //LOCATIONS EDIT
        int total_edit = 0;
        for (int i = 0; i < editData.size(); i++) {
            if (editData.get(i).getSURVEY_FROM().equals(LoginActivity.SURVEY_FROM)) {
                total_edit++;
            }
        }

        for (int i = 0; i < onlyLatLongData.size(); i++) {
            if (onlyLatLongData.get(i).getEDIT_TYPE().equals(LoginActivity.SURVEY_FROM)) {
                total_edit++;
            }
        }

        //tv_approved.setText("" + total_edit);

        layout_edit_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editData.isEmpty() && onlyLatLongData.isEmpty()) {
                    if (count_under_process == 0) {
                        Toast.makeText(SurveyList.this, "There are no survey to Update", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentPf = new Intent(SurveyList.this, EstimatesHome.class);
                        intentPf.putExtra("type", "2");
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    }
                } else {

                    String surveyData;
                    if (editData.isEmpty()) {
                        surveyData = onlyLatLongData.get(0).getEDIT_TYPE();
                    } else {
                        surveyData = editData.get(0).getSURVEY_FROM();
                    }

                    if (surveyData.equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                        IS_EDIT = true;
                        if (editData.isEmpty()) {
                            EDIT_GIS_ID = onlyLatLongData.get(0).getEDIT_GIS_ID();
                            EDIT_ESTIMATE_NO = onlyLatLongData.get(0).getEDIT_EST_NO();
                        } else {
                            EDIT_ESTIMATE_NO = editData.get(0).getEST_NO();
                            EDIT_GIS_ID = editData.get(0).getGIS_ID();
                        }
                        Intent intentPf = new Intent(SurveyList.this, EstimatesHome.class);
                        intentPf.putExtra("type", "2");
                        startActivity(intentPf);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    } else {
                        String msg = "";
                        if (surveyData.equalsIgnoreCase("WORK_ORDER")) {
                            msg = "Work Order";
                        } else {
                            msg = "Estimates";
                        }
                        openDialogForTransmit(msg);
                        //Toast.makeText(SurveyList.this, "Please Transmit Pending data from" + " " + surveyData, Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


    }

    private void openCompleteDialog(String surveyData) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        // 🔹 Title
        title.setText("TRANSMIT DATA!!");

        // 🔹 Message
        if (surveyData == null || surveyData.isEmpty()) {
            message.setText("Before completing the survey, please transmit the pending data.");
        } else {
            message.setText("Before completing the survey, please transmit the pending data from " + surveyData);
        }

        // 🔹 Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("OKAY");
        cancelBtn.setText("CANCEL");

        // OK Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            // transmitPendingData();
        });

        // CANCEL Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

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

//    private void openCompleteDialog(String surveyData) {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("TRANSMIT DATA!!");
//        if (surveyData.isEmpty()) {
//            builder.setMessage("Before completing the survey, please transmit the pending data.");
//        } else {
//            builder.setMessage("Before completing the survey, please transmit the pending data From " + " " + surveyData);
//        }
//        builder.setCancelable(false);
//
//        builder.setPositiveButton("OKAY", (dialog, which) -> {
//            dialog.dismiss();
//            // transmitPendingData();
//
//        });
//
//        builder.setNegativeButton("CANCEL", (dialog, which) -> {
//            dialog.cancel();
//
//        });
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }

    private void openDialogForTransmit(String surveyType) {

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

        tv_est_no.setText(getColoredText("Est No: ", "", this));
        tv_gis_id.setText(getColoredText("GIS ID: ", "", this));
        //ImageView icon = view.findViewById(R.id.icon); // optional (if present in XML)

        // 🔹 Title
        title.setText("Pay Attention!!");

        // 🔹 Icon (optional)


        // 🔹 Message
        message.setText("Please transmit pending data from " + surveyType);

        // 🔹 Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("YES");
        cancelBtn.setText("NO");

        // YES Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            onBackPressed();
        });

        // NO Click
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
//    private void openDialogForTransmit(String surveyType) {
//        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
//                .setTitle("Pay Attention!!")
//                .setIcon(R.drawable.app_logo)
//                .setMessage("Please Transmit Pending data from" + "  " + surveyType)
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        onBackPressed();
//                        dialog.dismiss();
//
//                    }
//                })
//                .setNegativeButton(android.R.string.no, null)
//                .show();
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        }
//    }


    private void openDialogForTransmitSuccess(String msg, String status) {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Pay Attention!!")
                .setIcon(R.drawable.app_logo)
                .setMessage(msg)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (status.equalsIgnoreCase("Success")) {
                            Intent intent = new Intent(SurveyList.this, CompleteSurvey.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                        } else {
                            onBackPressed();
                        }

                        dialog.dismiss();
                    }
                })
                .show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        }
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
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            finish();

        } else {
            Intent intentPf = new Intent(SurveyList.this, DashBoard.class);
            startActivity(intentPf);
            finish();
            overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
        }

    }

    private void openDialogForVersion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SurveyList.this);

        alertDialogBuilder.setTitle("A new Version is available!");
        //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
        alertDialogBuilder.setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                        "Please Download latest MOBILE APP APKs"))
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

    //NEW CHANGES ON LT
    @SuppressLint("SetTextI18n")
    private void getEstimatesCountDB() {
        loader_layout.setVisibility(View.VISIBLE);
        String survey_from = LoginActivity.SURVEY_FROM;
        if (survey_from != null) {
            if (survey_from.equalsIgnoreCase("ESTIMATES")) {
                tv_progress_bar.setText("Downloading Estimates Data");
            } else if (survey_from.equalsIgnoreCase("SOLAR_DPR")) {
                tv_progress_bar.setText("Downloading Solarization of SC ST  Data");
            } else {
                tv_progress_bar.setText("Downloading Work order Data");
            }
        }


        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        //dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        // dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
        } else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }
        dtrDetailsReqestVo.addProperty("USER_ID", loginDataResponses.get(0).getUSERID());

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getEstimateCount(dtrDetailsReqestVo);
        System.out.println("ACCESS_TOKEN---->" + loginDataResponses.get(0).getACCESS_TOKEN());
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
                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {

                            if (dialogDataModels != null && dialogDataModels.isEmpty()) {

                                var model = dialogDataModels.get(0);

                                if (model.getIS_ENABLED() != null && !model.getIS_ENABLED().equalsIgnoreCase("false")) {
                                    CustomDialog.showCustomDialog(SurveyList.this, DashBoard.dialogDataModels);
                                } else {

                                }

                            }
                        }



                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                if (object.has("STATUS") && !object.get("STATUS").isJsonNull()) {
                                    String statusVal = object.get("STATUS").getAsString();
                                    String integratedStatus = "";
                                    if (object.has("INTG_STATUS") && !object.get("INTG_STATUS").isJsonNull()) {
                                        integratedStatus = object.get("INTG_STATUS").getAsString();
                                    }

                                    int count = object.get("COUNT").getAsInt();

                                    switch (statusVal) {
                                        case "1":
                                            count_pending = count;
                                            break;
                                        case "2":
                                            count_under_process = count;
                                            break;
                                        case "3":
                                            count_approved = count;
                                            break;
                                        case "5":
                                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER") || LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                                                if (!integratedStatus.isEmpty()) {
                                                    if (integratedStatus.equalsIgnoreCase("A")) {
                                                        count_to_be_integrated = count;
                                                    } else if (integratedStatus.equalsIgnoreCase("R")) {
                                                        count_of_rejected_admin = count;
                                                    } else {
                                                        count_of_approved_admin = count;
                                                    }
                                                } else {
                                                    count_of_approved_admin = count;
                                                }
                                            } else {
                                                count_to_be_integrated = count;
                                            }

//                                            count_to_be_integrated = count;
                                            break;
                                        case "4":
                                            count_completed = count;
                                            break;
                                    }
                                }

                            }


                            main_layout.setVisibility(View.VISIBLE);
                            tv_pending.setText("" + count_pending);
                            tv_process.setText("" + count_under_process);
                            tv_total.setText("" + (count_pending + count_under_process + count_approved + count_completed + count_to_be_integrated + count_of_rejected_admin + count_of_approved_admin));

                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES") ||
                                    LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                                tv_completed.setText("" + count_to_be_integrated);
                            } else {
                                tv_completed.setText("" + count_completed);
                            }


                            System.out.println("count_completed:-->" + count_completed);
                            tv_count_to_be_integrated.setText("" + count_to_be_integrated);
                            tv_admin_approval.setText("" + count_of_approved_admin);
                            tv_rejected_admin.setText("" + count_of_rejected_admin);


                        } else {
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(SurveyList.this, errorMessage, Toast.LENGTH_SHORT).show();

                            if (errorMessage.contains("update to Version")) {
                                openDialogForVersion();
                            }
                            count_pending = 0;
                            count_total = 0;
                            count_approved = 0;
                            count_under_process = 0;
                            count_completed = 0;
                            main_layout.setVisibility(View.VISIBLE);
                            tv_pending.setText("" + count_pending);
                            tv_process.setText("" + count_under_process);
                            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                                tv_completed.setText("" + count_completed);
                            }
                            tv_completed.setText("" + count_completed);
                            //tv_approved.setText("" + count_approved);
                            tv_total.setText("" + count_total);
                            tv_count_to_be_integrated.setText("" + count_of_approved_admin);
                        }

                    } else {

                        // Handle case where STATUS is missing or null
                        Toast.makeText(SurveyList.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(SurveyList.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                loader_layout.setVisibility(View.GONE);
                Toast.makeText(SurveyList.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void getInformation() {
        infoList.clear();
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getInfo();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {

                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                InfoModel model = new InfoModel();
                                model.setDESCRIPTION(object.has("DESCRIPTION") && !object.get("DESCRIPTION").isJsonNull() ? object.get("DESCRIPTION").getAsString() : "");
                                model.setFEATURE(object.has("FEATURE") && !object.get("FEATURE").isJsonNull() ? object.get("FEATURE").getAsString() : "");
                                model.setFEATURE_SUB_TYPE(object.has("FEATURE_SUB_TYPE") && !object.get("FEATURE_SUB_TYPE").isJsonNull() ? object.get("FEATURE_SUB_TYPE").getAsString() : "");
                                infoList.add(model);
                            }

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    private void getDialogDataApi() {
        if (isDialogShown) return; // Prevent multiple calls
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getHelpPoints();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    dialogDataModels.clear();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                DialogDataModel model = new DialogDataModel();
                                model.setID(object.has("ID") && !object.get("ID").isJsonNull() ? object.get("ID").getAsString() : "");
                                model.setDESCRIPTION(object.has("DESCRIPTION") && !object.get("DESCRIPTION").isJsonNull() ? object.get("DESCRIPTION").getAsString() : "");
                                model.setRECORD_STATUS(object.has("RECORD_STATUS") && !object.get("RECORD_STATUS").isJsonNull() ? object.get("RECORD_STATUS").getAsString() : "");
                                model.setIS_ENABLED(object.has("IS_ENABLED") && !object.get("IS_ENABLED").isJsonNull() ? object.get("IS_ENABLED").getAsString() : "");
                                dialogDataModels.add(model);
                            }

                            if (dialogDataModels.get(0).getIS_ENABLED() != null) {
                                if (dialogDataModels.get(0).getIS_ENABLED().equalsIgnoreCase("false")) {



                                } else {
                                    if (!isDialogShown) {
                                        isDialogShown = true; // mark it shown
                                        CustomDialog.showCustomDialog(SurveyList.this, dialogDataModels);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getBannerImagesApi() {
        loader_layout.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getBannerImages();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                //loader_layout.setVisibility(View.GONE);
                //main_ui_layout.setVisibility(View.VISIBLE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (ConnectivityChecking.checkConnection(SurveyList.this)) {
                            getFeederLoadsPolesAPI();
                            //getDialogDataApi();
                        } else {
                            CommanUtilits.internetDialog(SurveyList.this);
                        }


                        //getDialogDataApi();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            bannerList.clear();
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                BannerModel model = new BannerModel();
                                model.setIMAGE_URL(object.has("IMAGE_URL") && !object.get("IMAGE_URL").isJsonNull() ? object.get("IMAGE_URL").getAsString() : "");
                                model.setSTATUS(object.has("STATUS") && !object.get("STATUS").isJsonNull() ? object.get("STATUS").getAsString() : "");
                                model.setID(object.has("ID") && !object.get("ID").isJsonNull() ? object.get("ID").getAsString() : "");
                                model.setDESCRIPTION(object.has("DESCRIPTION") && !object.get("DESCRIPTION").isJsonNull() ? object.get("DESCRIPTION").getAsString() : "");
                                bannerList.add(model);

                            }


                            ArrayList<SlideModel> slideList = new ArrayList<>();
                            for (int i = 0; i < bannerList.size(); i++) {
                                SlideModel m1 = new SlideModel(bannerList.get(i).getIMAGE_URL(), ScaleTypes.FIT);
                                slideList.add(m1);

                            }
                            imageSlider.setImageList(slideList);

                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                CommanUtilits.netWorkError(SurveyList.this);
                loader_layout.setVisibility(View.GONE);
            }
        });


    }

    private void getFeederLoadsPolesAPI() {
        loader_layout.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance("").create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getFeederLoadsPoles();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                if (response != null && response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    offline.clear();
                    online.clear();

                    if (surveyedResponseVo.has("STATUS") && surveyedResponseVo.get("STATUS").getAsBoolean()) {
                        JsonObject dataObj = surveyedResponseVo.getAsJsonObject("DATA");

                        // Offline list
                        JsonArray offlineArray = dataObj.getAsJsonArray("offline");
                        for (int i = 0; i < offlineArray.size(); i++) {
                            offline.add(offlineArray.get(i).getAsString());
                        }
                        offline.add(0, "Select"); // add at first position

                        // Online list
                        JsonArray onlineArray = dataObj.getAsJsonArray("online");
                        for (int i = 0; i < onlineArray.size(); i++) {
                            online.add(onlineArray.get(i).getAsString());
                        }
                        online.add(0, "Select");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout.setVisibility(View.GONE);
                // Handle failure
            }
        });
    }
}