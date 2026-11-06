package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;

import com.apepdcl.gis_survey.METER_SURVEY.ApiClient;
import com.apepdcl.gis_survey.METER_SURVEY.ImageAPIServiceMeter;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.models.WorkInvolvementModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransmitSurvey extends AppCompatActivity {

    RelativeLayout back_layout, close_layout;
    GifImageView loader;
    ArcGISDatabaseEstimates databaseEstimates;
    ArrayList<NewModel> allData;

    List<WorkInvolvementModel> workInvolvementModels;
    ArrayList<MaterialPostingModel> materialData;
    RecyclerView purpose_rv;
    TextView poles_33kv, est_no, description, poles_11kv, poles_lt, transmit, work_order, gis_id, gis_id_wo_rdss, feeders_wo_rdss, poles_33kv_wo_rdss, poles_11kv_wo_rdss, poles_lt_wo_rdss, transmit_images;
    JsonArray jsonArray;
    RetrofitServiceEstimates retrofitService;
    private LinearLayout loader_layout;
    private TextView tv_loader_text;

    String E_EST_NO = "", E_GIS_ID = "", E_WORK_ORDER = "";


    //LOCATIONS EDIT
    LinearLayout everything_main_layout, on_going_main_layout, on_going_main_layout_wo_rdss;
    TextView poles_33kv_everything, poles_11kv_everything, poles_lt_everything, transmit_everything;
    TextView work_order_everything, gis_id_everything, est_no_everything, description_everything;

    LinearLayout only_lat_long_main_layout;
    TextView poles_33kv_only_lat_long, poles_11kv_only_lat_long, poles_lt_only_lat_long, transmit_only_lat_long;
    TextView work_order_only_lat_long, gis_id_only_lat_long, est_no_only_lat_long, description_only_lat_long;
    ArrayList<EditModel> onlyLatLongData;
    TextView transmit_all;
    ArrayList<NewModel> transmittedEditedData;
    ArrayList<MaterialPostingModel> ongoingMaterialData;
    ArrayList<MaterialPostingModel> transmittedMaterialData;

    //SOLAR
    private LinearLayout layout_dtr_details;
    private TextView tv_dtr_phase, tv_dtr_capacity, tv_dtr_location_name, tv_action_remarks;

    int transmittedImagesCount, actualImagesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit_survey);
        getSupportActionBar().hide();


        //LOCATIONS EDIT
        on_going_main_layout = findViewById(R.id.on_going_main_layout);
        loader_layout = findViewById(R.id.loader_layout);
        tv_loader_text = findViewById(R.id.tv_loader_text);
        everything_main_layout = findViewById(R.id.everything_main_layout);
        poles_33kv_everything = findViewById(R.id.poles_33kv_everything);
        poles_11kv_everything = findViewById(R.id.poles_11kv_everything);
        poles_lt_everything = findViewById(R.id.poles_lt_everything);
        gis_id_everything = findViewById(R.id.gis_id_everything);
        work_order_everything = findViewById(R.id.work_order_everything);
        est_no_everything = findViewById(R.id.est_no_everything);
        description_everything = findViewById(R.id.description_everything);
        transmit_everything = findViewById(R.id.transmit_everything);
        only_lat_long_main_layout = findViewById(R.id.only_lat_long_main_layout);
        poles_33kv_only_lat_long = findViewById(R.id.poles_33kv_only_lat_long);
        poles_11kv_only_lat_long = findViewById(R.id.poles_11kv_only_lat_long);
        poles_lt_only_lat_long = findViewById(R.id.poles_lt_only_lat_long);
        work_order_only_lat_long = findViewById(R.id.work_order_only_lat_long);
        gis_id_only_lat_long = findViewById(R.id.gis_id_only_lat_long);
        est_no_only_lat_long = findViewById(R.id.est_no_only_lat_long);
        description_only_lat_long = findViewById(R.id.description_only_lat_long);
        transmit_only_lat_long = findViewById(R.id.transmit_only_lat_long);
        transmit_all = findViewById(R.id.transmit_all);
        on_going_main_layout_wo_rdss = findViewById(R.id.on_going_main_layout_wo_rdss);


        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        loader = findViewById(R.id.loader);
        purpose_rv = findViewById(R.id.purpose_rv);
        est_no = findViewById(R.id.est_no);
        description = findViewById(R.id.description);
        work_order = findViewById(R.id.work_order);
        gis_id = findViewById(R.id.gis_id);
        poles_33kv = findViewById(R.id.poles_33kv);
        poles_11kv = findViewById(R.id.poles_11kv);
        poles_lt = findViewById(R.id.poles_lt);
        transmit = findViewById(R.id.transmit);
        gis_id_wo_rdss = findViewById(R.id.gis_id_wo_rdss);
        feeders_wo_rdss = findViewById(R.id.feeders_wo_rdss);
        poles_33kv_wo_rdss = findViewById(R.id.poles_33kv_wo_rdss);
        poles_11kv_wo_rdss = findViewById(R.id.poles_11kv_wo_rdss);
        poles_lt_wo_rdss = findViewById(R.id.poles_lt_wo_rdss);
        transmit_images = findViewById(R.id.transmit_images);

        //SOLAR CASTING
        tv_dtr_location_name = findViewById(R.id.tv_dtr_location_name);
        tv_dtr_capacity = findViewById(R.id.tv_dtr_capacity);
        tv_dtr_phase = findViewById(R.id.tv_dtr_phase);
        layout_dtr_details = findViewById(R.id.layout_dtr_details);
        tv_action_remarks = findViewById(R.id.tv_action_remarks);

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

        ongoingMaterialData = new ArrayList<>();
        transmittedMaterialData = new ArrayList<>();
        onlyLatLongData = new ArrayList<>();
        allData = new ArrayList<>();
        transmittedEditedData = new ArrayList<>();
        workInvolvementModels = new ArrayList<>();

        /*databaseEstimates = new ArcGISDatabaseEstimates(this);
        allData = databaseEstimates.getCurrentSurveyData();
        materialData = databaseEstimates.getMaterialData(allData.get(0).getEST_NO());*/

        databaseEstimates = new ArcGISDatabaseEstimates(this);
        allData = databaseEstimates.getLocalSurveyedData();
        transmittedEditedData = databaseEstimates.getTransmittedSurveyedData();

        for (int i = 0; i < transmittedEditedData.size(); i++) {
            E_EST_NO = transmittedEditedData.get(i).getEST_NO();
            E_GIS_ID = transmittedEditedData.get(i).getGIS_ID();
            if (transmittedEditedData.get(i) != null &&
                    transmittedEditedData.get(i).getWORK_ORDER() != null) {

                E_WORK_ORDER = transmittedEditedData.get(i).getWORK_ORDER();
            }

        }

        System.out.println("ON GOING DATA SIZE : " + allData.size());
        System.out.println("TRANSMITTED DATA SIZE : " + transmittedEditedData.size() + " EST " + E_EST_NO);


        if (!LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            if (!allData.isEmpty()) {
                on_going_main_layout.setVisibility(View.VISIBLE);
                on_going_main_layout_wo_rdss.setVisibility(View.GONE);
                ongoingMaterialData = databaseEstimates.getOngoingMaterialData(allData.get(0).getEST_NO(), allData.get(0).getGIS_ID());
                // System.out.println("ON GOING MATERIAL DATA SIZE : " + ongoingMaterialData.size());


                gis_id.setText("GIS ID: " + allData.get(0).getGIS_ID());
                if (allData.get(0).getPURPOSE() != null) {
                    description.setText("" + allData.get(0).getPURPOSE());

                }
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                    work_order.setVisibility(View.GONE);
                    est_no.setText("Est No: " + allData.get(0).getEST_NO());
                } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                    work_order.setVisibility(View.GONE);
                    est_no.setVisibility(View.VISIBLE);
                    est_no.setText("DTR Code: " + allData.get(0).getEST_NO());
                    tv_dtr_phase.setText(EstimatesHome.DTR_PHASE);
                    tv_dtr_capacity.setText(EstimatesHome.DTR_CAPACITY);
                    tv_dtr_location_name.setText(EstimatesHome.DTR_NAME);
                    if (!EstimatesHome.ACTION.isEmpty()) {
                        layout_dtr_details.setVisibility(View.VISIBLE);
                    }

                    tv_action_remarks.setVisibility(View.VISIBLE);
                    tv_action_remarks.setText(EstimatesHome.ACTION_REMARKS);
                } else {
                    work_order.setVisibility(View.VISIBLE);
                    est_no.setText("Est No: " + allData.get(0).getEST_NO());
                    work_order.setText("Wo No: " + allData.get(0).getWORK_ORDER());
                }
            }
        } else {
            if (!allData.isEmpty()) {
                on_going_main_layout_wo_rdss.setVisibility(View.VISIBLE);
                on_going_main_layout.setVisibility(View.GONE);
                ongoingMaterialData = databaseEstimates.getOngoingMaterialData(allData.get(0).getEST_NO(), allData.get(0).getGIS_ID());
                transmit_all.setVisibility(View.GONE);

                gis_id_wo_rdss.setText("GIS ID: " + allData.get(0).getGIS_ID());
                if (allData.get(0).getPURPOSE() != null) {
                    feeders_wo_rdss.setText("" + WorkInvolmentsActivity.FEEDERS);

                }

            }
        }

        int count_33kv = 0;
        int count_11kv = 0;
        int count_lt = 0;
        /*est_no.setText("EST No: " + allData.get(0).getEST_NO());
        gis_id.setText("GIS ID: " + allData.get(0).getGIS_ID());
        //description.setText("GIS ID: " + allData.get(0).getPur());
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")){
            work_order.setVisibility(View.GONE);
        }else {
            work_order.setVisibility(View.VISIBLE);
            work_order.setText("WO No: " +allData.get(0).getWORK_ORDER());
        }*/
        for (int i = 0; i < allData.size(); i++) {
            if (allData.get(i).getVOLTAGE_LEVEL().equals("33KV") && allData.get(i).getIS_TRANSMITTED_LOCATION().equals("N")) {
                count_33kv++;
            }
            if (allData.get(i).getVOLTAGE_LEVEL().equals("11KV") && allData.get(i).getIS_TRANSMITTED_LOCATION().equals("N")) {
                count_11kv++;
            }
            if (allData.get(i).getVOLTAGE_LEVEL().equals("6.3KV") && allData.get(i).getIS_TRANSMITTED_LOCATION().equals("N")) {
                count_11kv++;
            }
            if (allData.get(i).getVOLTAGE_LEVEL().equals("LT") && allData.get(i).getIS_TRANSMITTED_LOCATION().equals("N")) {
                count_lt++;
            }
        }

        if (!LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            poles_33kv.setText("" + count_33kv);
            poles_11kv.setText("" + count_11kv);
            poles_lt.setText("" + count_lt);
        } else {
            poles_33kv_wo_rdss.setText("" + count_33kv);
            poles_11kv_wo_rdss.setText("" + count_11kv);
            poles_lt_wo_rdss.setText("" + count_lt);
        }

        boolean isTransmit_images = false;
//        for (int i = 0; i < allData.size(); i++) {
//            if (allData.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")){
//                isTransmit_images=true;
//
//            }
//
//        }
//        if (isTransmit_images){
//            transmit_images.setVisibility(View.GONE);
//            transmit_all.setVisibility(View.VISIBLE);
//        }

        transmit_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transmit_images.post(() -> getDbmsAccessToken());
            }
        });
        transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isLtRequired = false;
                boolean isLtPresent = false;
                String poleNo = "";

                for (int i = 0; i < allData.size(); i++) {
                    String poleConfig = allData.get(i).getPOLE_CONFIG();
                    String surveyType = allData.get(i).getDTR_SURVEY_TYPE();
                    String ltAvailable = allData.get(i).getLT_AVAILABLE();

                    if ("DTR Pole".equalsIgnoreCase(poleConfig) &&
                            "Proposed".equalsIgnoreCase(surveyType)) {

                        System.out.println("DTR_SURVEY_TYPE_TEST: " + surveyType);
                        System.out.println("DTR_SURVEY_TYPE_TEST1: " + ltAvailable);
                        System.out.println("DTR_SURVEY_TYPE_TEST2: " + ltAvailable);

                        poleNo = allData.get(i).getPOLE_NO();

                        if (WorkInvolmentsActivity.IS_LT_SELECTED_NEW) {
                            isLtPresent = true;
                        }

                        if ("Yes".equalsIgnoreCase(ltAvailable) &&
                                !WorkInvolmentsActivity.IS_LT_SELECTED_NEW) {
                            isLtRequired = true;
                        }
                    } else {
                        System.out.println("Skipping pole: POLE_CONFIG = " + poleConfig + ", DTR_SURVEY_TYPE = " + surveyType);
                    }
                }

                if (isLtRequired && !isLtPresent) {
                    openAlertForWaringLT(poleNo);
                } else {
                    openCompleteDialog();
                }


                //openCompleteDialog();
            }
        });


        //LOCATIONS EDIT EVERYTHING
        int ev_count_33 = 0;
        int ev_count_11 = 0;
        int ev_count_LT = 0;

        for (int i = 0; i < transmittedEditedData.size(); i++) {
            if (transmittedEditedData.get(i).getVOLTAGE_LEVEL().equals("33KV") && transmittedEditedData.get(i).getIS_TRANSMITTED_LOCATION().equals("Y")) {
                ev_count_33++;
            }
            if (transmittedEditedData.get(i).getVOLTAGE_LEVEL().equals("11KV") && transmittedEditedData.get(i).getIS_TRANSMITTED_LOCATION().equals("Y")) {
                ev_count_11++;
            }
            if (transmittedEditedData.get(i).getVOLTAGE_LEVEL().equals("6.3KV") && transmittedEditedData.get(i).getIS_TRANSMITTED_LOCATION().equals("Y")) {
                ev_count_11++;
            }
            if (transmittedEditedData.get(i).getVOLTAGE_LEVEL().equals("LT") && transmittedEditedData.get(i).getIS_TRANSMITTED_LOCATION().equals("Y")) {
                ev_count_LT++;
            }
        }

        if (ev_count_33 > 0 || ev_count_11 > 0 || ev_count_LT > 0) {
            transmittedMaterialData = databaseEstimates.getTransmittedMaterialData(transmittedEditedData.get(0).getEST_NO(), transmittedEditedData.get(0).getGIS_ID());
            // System.out.println("TRANSMITTED MATERIAL DATA SIZE : " + transmittedMaterialData.size());
            everything_main_layout.setVisibility(View.VISIBLE);
            poles_33kv_everything.setText("" + ev_count_33);
            poles_11kv_everything.setText("" + ev_count_11);
            poles_lt_everything.setText("" + ev_count_LT);

            est_no_everything.setText("EST No: " + transmittedEditedData.get(0).getEST_NO());
            gis_id_everything.setText("GIS ID: " + transmittedEditedData.get(0).getGIS_ID());
            description_everything.setText("" + transmittedEditedData.get(0).getPURPOSE());
            description_only_lat_long.setText("" + transmittedEditedData.get(0).getPURPOSE());
            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                work_order_everything.setVisibility(View.GONE);
                work_order_only_lat_long.setVisibility(View.GONE);
            } else {
                work_order_everything.setVisibility(View.VISIBLE);
                work_order_only_lat_long.setVisibility(View.VISIBLE);
                work_order_everything.setText("Wo No: " + transmittedEditedData.get(0).getWORK_ORDER());
                work_order_only_lat_long.setText("Wo No: " + transmittedEditedData.get(0).getWORK_ORDER());
            }

        }


        //LOCATIONS EDIT ONLY LAT AND LONG
        onlyLatLongData = databaseEstimates.getEditLocationsData();
        int ll_count_33 = 0;
        int ll_count_11 = 0;
        int ll_count_LT = 0;

        for (int i = 0; i < onlyLatLongData.size(); i++) {
            if (onlyLatLongData.get(i).getEDIT_VOLTAGE_LEVEL().equals("33KV")) {
                ll_count_33++;
            }
            if (onlyLatLongData.get(i).getEDIT_VOLTAGE_LEVEL().equals("11KV")) {
                ll_count_11++;
            }
            if (onlyLatLongData.get(i).getEDIT_VOLTAGE_LEVEL().equals("LT")) {
                ll_count_LT++;
            }
        }

        if (ll_count_33 > 0 || ll_count_11 > 0 || ll_count_LT > 0) {
            only_lat_long_main_layout.setVisibility(View.VISIBLE);
            poles_33kv_only_lat_long.setText("" + ll_count_33);
            poles_11kv_only_lat_long.setText("" + ll_count_11);
            poles_lt_only_lat_long.setText("" + ll_count_LT);
            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
                est_no_only_lat_long.setText("DTR Code: " + onlyLatLongData.get(0).getEDIT_EST_NO());
                gis_id_only_lat_long.setText("GIS ID: " + onlyLatLongData.get(0).getEDIT_GIS_ID());
                tv_dtr_phase.setText(EstimatesHome.DTR_PHASE);
                tv_dtr_capacity.setText(EstimatesHome.DTR_CAPACITY);
                tv_dtr_location_name.setText(EstimatesHome.DTR_NAME);
                layout_dtr_details.setVisibility(View.VISIBLE);
                tv_action_remarks.setVisibility(View.VISIBLE);
                tv_action_remarks.setText(EstimatesHome.ACTION_REMARKS);
            } else {
                est_no_only_lat_long.setText("EST No: " + onlyLatLongData.get(0).getEDIT_EST_NO());
                gis_id_only_lat_long.setText("GIS ID: " + onlyLatLongData.get(0).getEDIT_GIS_ID());
            }


            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                work_order_only_lat_long.setVisibility(View.GONE);
            } else {
                //  work_order_everything.setVisibility(View.VISIBLE);
                //  work_order_everything.setText("WO No: " +allData.get(0).getWORK_ORDER());
            }

        }


        transmit_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transmit_all.setVisibility(View.GONE);
                boolean isLtRequired = false;
                boolean isLtPresent = false;
                String poleNo = "";
                String transmitPoleConfig = "";
                String transmitSurveyType = "";
                String ltAvailableTransmit = "";


                if (!transmittedEditedData.isEmpty()) {
                    for (int j = 0; j < transmittedEditedData.size(); j++) {
                        transmitPoleConfig = transmittedEditedData.get(j).getPOLE_CONFIG();
                        transmitSurveyType = transmittedEditedData.get(j).getDTR_SURVEY_TYPE();
                        ltAvailableTransmit = transmittedEditedData.get(j).getLT_AVAILABLE();
                        if ("DTR Pole".equalsIgnoreCase(transmitPoleConfig) &&
                                "Proposed".equalsIgnoreCase(transmitSurveyType)) {
                            poleNo = transmittedEditedData.get(j).getPOLE_NO();

                            if (WorkInvolmentsActivity.IS_LT_SELECTED_NEW) {
                                isLtPresent = true;
                            }

                            if ("Yes".equalsIgnoreCase(ltAvailableTransmit) &&
                                    !WorkInvolmentsActivity.IS_LT_SELECTED_NEW) {
                                isLtRequired = true;
                            }
                        } else {


                        }
                    }
                } else {
                    if (!allData.isEmpty()) {
                        for (int i = 0; i < allData.size(); i++) {
                            String poleConfig = allData.get(i).getPOLE_CONFIG();
                            String surveyType = allData.get(i).getDTR_SURVEY_TYPE();
                            String ltAvailable = allData.get(i).getLT_AVAILABLE();
                            System.out.println("CASE 2:");

                            if ("DTR Pole".equalsIgnoreCase(poleConfig) &&
                                    "Proposed".equalsIgnoreCase(surveyType)) {
                                System.out.println("CASE 5:");
                                System.out.println("DTR_SURVEY_TYPE_TEST: " + surveyType);
                                System.out.println("DTR_SURVEY_TYPE_TEST1: " + ltAvailable);
                                System.out.println("DTR_SURVEY_TYPE_TEST2: " + ltAvailable);

                                poleNo = allData.get(i).getPOLE_NO();

                                if (WorkInvolmentsActivity.IS_LT_SELECTED_NEW) {
                                    isLtPresent = true;
                                }

                                if ("Yes".equalsIgnoreCase(ltAvailable) &&
                                        !WorkInvolmentsActivity.IS_LT_SELECTED_NEW) {
                                    isLtRequired = true;
                                }
                            } else {
                                System.out.println("CASE 6:");
                                System.out.println("Skipping pole: POLE_CONFIG = " + poleConfig + ", DTR_SURVEY_TYPE = " + surveyType);
                            }
                        }
                    }
                }

                if (isLtRequired && !isLtPresent) {
                    openAlertForWaringLT(poleNo);

                } else {

                    openCompleteDialogForAll();
                }
            }
        });


//        transmit_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openCompleteDialogForAll();
//            }
//        });

    }

    private void openCompleteDialogForAll() {
       /* androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("TRANSMIT DATA!!");
        builder.setMessage("Are you sure you want to transmit survey data?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            dialog.dismiss();
            if (allData.isEmpty()) {
                if (transmittedEditedData.isEmpty()) {
                    if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                        transmitLatLongData();
                    } else {
                        CommanUtilits.internetDialog(TransmitSurvey.this);
                    }
                    //transmitLatLongData();
                } else {
                    if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                        transmitTransmittedData();
                    } else {
                        CommanUtilits.internetDialog(TransmitSurvey.this);
                    }
                    //transmitTransmittedData();
                }
            } else {
                if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                    transmitOngoingData();
                } else {
                    CommanUtilits.internetDialog(TransmitSurvey.this);
                }
                // transmitOngoingData();
            }


        });

        builder.setNegativeButton("NO", (dialog, which) -> {
            transmit_all.setVisibility(View.VISIBLE);
            dialog.cancel();

        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();*/


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_to_be_integrated, null);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.setContentView(dialogView);
        dialog.show();
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);
        title.setText("Transmit Data!!");
        message.setText("Are you sure you want to transmit survey data?");

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (allData.isEmpty()) {
                    if (transmittedEditedData.isEmpty()) {
                        if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                            transmitLatLongData();
                        } else {
                            CommanUtilits.internetDialog(TransmitSurvey.this);
                        }
                        //transmitLatLongData();
                    } else {
                        if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                            transmitTransmittedData("");
                        } else {
                            CommanUtilits.internetDialog(TransmitSurvey.this);
                        }
                        //transmitTransmittedData();
                    }
                } else {
                    if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                        transmitOngoingData();
                    } else {
                        CommanUtilits.internetDialog(TransmitSurvey.this);
                    }
                    // transmitOngoingData();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openCompleteDialog() {
        /*androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("TRANSMIT DATA!!");
        builder.setMessage("Are you sure you want to transmit survey data?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            dialog.dismiss();
            transmitPendingData();

        });

        builder.setNegativeButton("NO", (dialog, which) -> {
            dialog.cancel();

        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();*/

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_to_be_integrated, null);
        dialog.setContentView(dialogView);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.show();
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);
        title.setText("TRANSMIT DATA!!");
        message.setText("Are you sure you want to transmit survey data?");

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                transmitPendingData();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //Proposed DTR at Location Number ?? is having LT Circuit " but LT Work Involvemet is not selected. Change Work Involvements and update LT Survey before trasmitting the data

    private void openAlertForWaringLT(String poleNo) {

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

        tv_est_no.setText(getColoredText("Est No: ", allData.get(0).getEST_NO(), this));
        tv_gis_id.setText(getColoredText("GIS ID: ", allData.get(0).getGIS_ID(), this));
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        // Title & Message
        title.setText("Pay Attention");
        message.setText("Proposed DTR at Location Number " + poleNo +
                " is having LT circuit but LT work involvement is not selected.\n\n" +
                "Please change work involvement and update LT survey before transmitting the data.");
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Update LT Work Involvement");
        cancelBtn.setText("Cancel");

        // OK Click
        okBtn.setOnClickListener(v -> {
            updateLTWorkInvolment(allData.get(0).getGIS_ID());
            dialog.dismiss();
        });

        // CANCEL Click
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);

        // Transparent background + full width
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            dialog.getWindow().setLayout(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
        }

        dialog.show();
    }
//    private void openAlertForWaringLT(String poleNo) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Pay attention");
//        builder.setCancelable(false);
//        builder.setMessage("Proposed DTR at Location Number " + poleNo +
//                " is having LT Circuit but LT Work Involvement is not selected. " +
//                "Change Work Involvements and update LT Survey before transmitting the data.");
//
//        builder.setPositiveButton("Update LT Work Involvement", (dialog, which) -> {
//            updateLTWorkInvolment(allData.get(0).getGIS_ID());
//            dialog.dismiss();
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
//        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
//    }


    private void updateLTWorkInvolment(String GIS_ID) {
        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        retrofitService = RetrofitClientInstanceEstimates
                .getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN())
                .create(RetrofitServiceEstimates.class);

        Call<JsonObject> responseVoCall = retrofitService.updateLTWorkInvolvement(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful() && response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") &&
                            MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        WorkInvolmentsActivity.IS_LT_SELECTED_NEW = true;
                        openDialogForSuccessMsgLT(
                                surveyedResponseVo.get("ERR_MSG").getAsString(),
                                allData.get(0).getGIS_ID(),
                                allData.get(0).getEST_NO(),
                                allData.get(0).getWORK_ORDER() == null
                                        ? ""
                                        : allData.get(0).getWORK_ORDER());


                    } else {
                        openDialogForSuccessMsgLT(
                                surveyedResponseVo.get("ERR_MSG").getAsString(),
                                allData.get(0).getGIS_ID(),
                                allData.get(0).getEST_NO(),
                                allData.get(0).getWORK_ORDER() == null
                                        ? ""
                                        : allData.get(0).getWORK_ORDER());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


            }
        });
    }

    private void transmitPendingData() {
        allData = databaseEstimates.getCurrentSurveyData();
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
            loader.setVisibility(View.VISIBLE);
            tv_loader_text.setText("2/2 Poles data is being transmitted");
        } else {
            loader.setVisibility(View.VISIBLE);
        }

        transmit_all.setVisibility(View.GONE);
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(allData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
            // System.out.println(jsonArray);
        }
        String json = gson.toJson(allData);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingDataNew(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                    loader_layout.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                    //tv_loader_text.setText("2/2 Poles data is being transmitted");
                } else {
                    loader.setVisibility(View.GONE);
                }
                transmit_all.setVisibility(View.GONE);
                //  System.out.println("ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        boolean surveyStatusNEW = databaseEstimates.deleteALLSurveyStatusNEW();
                        boolean newSurveyData = databaseEstimates.deleteAllNewSurveyData();


                        if (materialData != null) {
                            if (materialData.isEmpty()) {
                                openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                                //Toast.makeText(TransmitSurvey.this, "Data Transmitted Successfully..", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                            } else {
                                transmitMaterialData(surveyedResponseVo.get("ERR_MSG").getAsString());
                            }
                        } else {
                            openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                        }


                    } else {
                        //openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                        //openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                        if (allData != null && !allData.isEmpty()) {

                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    allData.get(0).getGIS_ID(),
                                    allData.get(0).getEST_NO(),
                                    allData.get(0).getWORK_ORDER() == null
                                            ? ""
                                            : allData.get(0).getWORK_ORDER()
                            );


                        } else {
                            // list is empty → handle safely
                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    "",
                                    "",
                                    ""
                            );
                        }
                        //  Toast.makeText(TransmitSurvey.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                transmit_all.setVisibility(View.VISIBLE);
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                    loader_layout.setVisibility(View.GONE);
                    //tv_loader_text.setText("2/2 Poles data is being transmitted");
                } else {
                    loader.setVisibility(View.GONE);
                }
            }
        });

    }

    private void openDialogForTransmit(String msg) {
      /*  new AlertDialog.Builder(TransmitSurvey.this)
                .setTitle("Pay Attention!!")
                .setIcon(R.drawable.app_logo)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                        dialog.dismiss();

                    }
                })
                .show();*/

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_success, null);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.setContentView(dialogView);
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
                Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });
    }

    private void transmitMaterialData(String message) {
        loader.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(materialData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
            System.out.println("MATERIALS_DATA_TRANSMIT:" + jsonArray);
        }
        String json = gson.toJson(allData);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingMaterialNew(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                //  System.out.println("MATERIAL DATA RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        boolean surveyStatusNEW = databaseEstimates.clearMaterialTable();
                        openDialogForTransmit(message);
                        // Toast.makeText(TransmitSurvey.this, "Data Transmitted Successfully..", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);

                    } else {
                        //openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                        //openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                        if (allData != null && !allData.isEmpty()) {

                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    allData.get(0).getGIS_ID(),
                                    allData.get(0).getEST_NO(),
                                    allData.get(0).getWORK_ORDER() == null
                                            ? ""
                                            : allData.get(0).getWORK_ORDER()
                            );

                        } else {
                            // list is empty → handle safely
                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    "",
                                    "",
                                    ""
                            );
                        }
                        //Toast.makeText(TransmitSurvey.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }


    private void transmitLatLongData() {
        loader.setVisibility(View.VISIBLE);
        transmit_all.setVisibility(View.GONE);
        jsonArray = new JsonArray();

        for (int i = 0; i < onlyLatLongData.size(); i++) {
            JsonObject object = new JsonObject();
            object.addProperty("EST_NO", onlyLatLongData.get(i).getEDIT_EST_NO());
            object.addProperty("GIS_ID", onlyLatLongData.get(i).getEDIT_GIS_ID());
            object.addProperty("GLAT", onlyLatLongData.get(i).getEDIT_POLE_LON());
            object.addProperty("GLONG", onlyLatLongData.get(i).getEDIT_POLE_LAT());
            object.addProperty("LOCATION_NO", onlyLatLongData.get(i).getEDIT_POLE_NO());
            object.addProperty("LOCATION_Id", onlyLatLongData.get(i).getEDIT_LOCATION_ID());
            jsonArray.add(object);
        }
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.updateLocations(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                transmit_all.setVisibility(View.VISIBLE);
                //  System.out.println("UPDATE LOCATIONS RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        boolean surveyStatusNEW = databaseEstimates.deleteEditLocationsData();
                        openSuccessDialogForLatLong();

                    } else {
                        //Toast.makeText(TransmitSurvey.this, "Failed Please try again", Toast.LENGTH_LONG).show();
                        // openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                        if (allData != null && !allData.isEmpty()) {

                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    allData.get(0).getGIS_ID(),
                                    allData.get(0).getEST_NO(),
                                    allData.get(0).getWORK_ORDER() == null
                                            ? ""
                                            : allData.get(0).getWORK_ORDER()
                            );

                        } else {
                            // list is empty → handle safely
                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    "",
                                    "",
                                    ""
                            );
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                transmit_all.setVisibility(View.VISIBLE);
                Toast.makeText(TransmitSurvey.this, "Failed Please try again", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void openSuccessDialogForLatLong() {

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
        tv_est_no.setText(getColoredText("Est No: ", onlyLatLongData.get(0).getEDIT_EST_NO(), this));
        tv_gis_id.setText(getColoredText("GIS ID: ", onlyLatLongData.get(0).getEDIT_GIS_ID(), this));
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.suc));
        // Title & Message
        title.setText("Successfully Transmitted!!");
        message.setText("Data transmitted successfully!!");

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("OKAY");

        // Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();

            Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
            startActivity(intent);
            finish();
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
//    private void openSuccessDialogForLatLong() {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        builder.setTitle("SUCCESSFULLY TRANSMITTED!!");
//        builder.setMessage("Data Transmitted Successfully!!");
//        builder.setCancelable(false);
//        builder.setPositiveButton("OKAY", (dialog, which) -> {
//            dialog.dismiss();
//            Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
//            startActivity(intent);
//            finish();
//
//        });
//
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
//        dialog.show();
//    }

    private void transmitTransmittedData(String message) {
        loader.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(transmittedEditedData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
            // System.out.println("ESTIMATES TRANSMITTED REQUEST: ");
            // System.out.println(jsonArray);
        }
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingTransmittedData(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                //   System.out.println("ESTIMATES RESPONSE TRANSMITTED: " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        if (allData.isEmpty()) {
                            boolean surveyStatusNEW = databaseEstimates.deleteALLSurveyStatusNEW();
                            boolean newSurveyData = databaseEstimates.deleteAllNewSurveyData();
                            //  System.out.println("ESTIMATES RESPONSE TRANSMITTED DELETE: " + surveyStatusNEW);
                            //  System.out.println("ESTIMATES RESPONSE TRANSMITTED DELETE: " + newSurveyData);
                        } else {
                            boolean status = databaseEstimates.deleteOnlyTransmittedData();
                            // System.out.println("ESTIMATES RESPONSE TRANSMITTED DELETE ELSE : " + status);
                        }


                        if (transmittedMaterialData.isEmpty()) {
                            // openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());

                            if (onlyLatLongData.isEmpty()) {
                                //openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                                //openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                                if (transmittedEditedData != null && !transmittedEditedData.isEmpty()) {

                                    openDialogForSuccessMsg(
                                            message,
                                            E_GIS_ID,
                                            E_EST_NO,
                                            E_WORK_ORDER

                                    );

                                } else {
                                    // list is empty → handle safely
                                    openDialogForSuccessMsg(
                                            surveyedResponseVo.get("ERR_MSG").getAsString(),
                                            E_GIS_ID,
                                            E_EST_NO,
                                            E_WORK_ORDER
                                    );
                                }
                            } else {
                                if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                                    transmitLatLongData();
                                } else {
                                    CommanUtilits.internetDialog(TransmitSurvey.this);
                                }

                                //transmitLatLongData();
                            }

                        } else {
                            if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                                transmitMaterialDataForTransmitted();
                            } else {
                                CommanUtilits.internetDialog(TransmitSurvey.this);
                            }
                            //transmitMaterialDataForTransmitted();
                        }


                    } else {
                        // openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                        //openDialogForFailure(surveyedResponseVo.get("ERR_MSG").getAsString());
                        //  Toast.makeText(TransmitSurvey.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                        if (allData != null && !allData.isEmpty()) {

                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    allData.get(0).getGIS_ID(),
                                    allData.get(0).getEST_NO(),
                                    allData.get(0).getWORK_ORDER() == null
                                            ? ""
                                            : allData.get(0).getWORK_ORDER()
                            );

                        } else {
                            // list is empty → handle safely
                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    "",
                                    "",
                                    ""
                            );
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });

    }


    private void transmitMaterialDataForTransmitted() {
        loader.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(transmittedMaterialData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();
            //  System.out.println("TRANSMITTED MATERIAL DATA: ");
            // System.out.println(jsonArray);
        }
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingMaterialTransmitted(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                // System.out.println("MATERIAL DATA RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {

                        if (ongoingMaterialData.isEmpty()) {
                            boolean surveyStatusNEW = databaseEstimates.clearMaterialTable();
                        } else {
                            databaseEstimates.deleteTransmittedMaterialData(transmittedMaterialData.get(0).getESTIMATE_NO(), allData.get(0).getGIS_ID());
                        }


                        if (onlyLatLongData.isEmpty()) {
                            //openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                            //openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID() == null ? null : "", allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                            if (transmittedEditedData != null && !transmittedEditedData.isEmpty()) {

                                openDialogForSuccessMsg(
                                        surveyedResponseVo.get("ERR_MSG").getAsString(),
                                        transmittedEditedData.get(0).getGIS_ID(),
                                        transmittedEditedData.get(0).getEST_NO(),
                                        transmittedEditedData.get(0).getWORK_ORDER() == null
                                                ? ""
                                                : transmittedEditedData.get(0).getWORK_ORDER()
                                );

                            } else {
                                // list is empty → handle safely
                                openDialogForSuccessMsg(
                                        surveyedResponseVo.get("ERR_MSG").getAsString(),
                                        allData.get(0).getGIS_ID(),
                                        allData.get(0).getEST_NO(),
                                        allData.get(0).getWORK_ORDER() == null
                                                ? ""
                                                : allData.get(0).getWORK_ORDER()
                                );
                            }

                        } else {
                            transmitLatLongData();
                        }

                        //  openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                    } else {
                        if (allData != null && !allData.isEmpty()) {

                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    allData.get(0).getGIS_ID(),
                                    allData.get(0).getEST_NO(),
                                    allData.get(0).getWORK_ORDER() == null
                                            ? ""
                                            : allData.get(0).getWORK_ORDER()
                            );

                        } else {
                            // list is empty → handle safely
                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    "",
                                    "",
                                    ""
                            );
                        }
                        // openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                        // openDialogForFailure(surveyedResponseVo.get("ERR_MSG").getAsString());

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }



    private void transmitOngoingData() {
        loader.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        allData = databaseEstimates.getCurrentSurveyData();
        JsonElement jsonElement = gson.toJsonTree(allData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();

        }
        String json = gson.toJson(allData);

        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingDataNew(jsonArray);

        responseVoCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                // System.out.println("ESTIMATES RESPONSE ONGOING: " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        if (transmittedEditedData.isEmpty()) {
                            boolean surveyStatusNEW = databaseEstimates.deleteALLSurveyStatusNEW();
                            boolean newSurveyData = databaseEstimates.deleteAllNewSurveyData();
                        } else {
                            databaseEstimates.deleteOnlySurveyedData();
                        }


                        if (ongoingMaterialData.isEmpty()) {
                            //  openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());

                            //NEW EDIT LOCATIONS START

                            if (transmittedEditedData.isEmpty()) {
                                if (onlyLatLongData.isEmpty()) {
                                    openDialogForTransmit(surveyedResponseVo.get("ERR_MSG").getAsString());
                                } else {
                                    transmitLatLongData();
                                }

                            } else {
                                transmitTransmittedData(surveyedResponseVo.get("ERR_MSG").getAsString());
                            }

                            //NEW EDIT LOCATIONS END
                        } else {
                            transmitMaterialDataForOngoing(surveyedResponseVo.get("ERR_MSG").getAsString());
                        }


                    } else {
                        //openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");
                        if (allData != null && !allData.isEmpty()) {

                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    allData.get(0).getGIS_ID(),
                                    allData.get(0).getEST_NO(),
                                    allData.get(0).getWORK_ORDER() == null
                                            ? ""
                                            : allData.get(0).getWORK_ORDER()
                            );

                        } else {
                            // list is empty → handle safely
                            openDialogForSuccessMsg(
                                    surveyedResponseVo.get("ERR_MSG").getAsString(),
                                    "",
                                    "",
                                    ""
                            );
                        }
                        //openDialogForFailure(surveyedResponseVo.get("ERR_MSG").getAsString());
                        //  Toast.makeText(TransmitSurvey.this, "Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });

    }

    private void transmitMaterialDataForOngoing(String message) {
        loader.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(ongoingMaterialData);
        if (jsonElement.isJsonArray()) {
            jsonArray = jsonElement.getAsJsonArray();

        }
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.postingMaterialNew(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                // System.out.println("MATERIAL DATA RESPONSE ONGOING: " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {

                        if (transmittedMaterialData.isEmpty()) {
                            boolean surveyStatusNEW = databaseEstimates.clearMaterialTable();
                        } else {
                            databaseEstimates.deleteOngoingMaterialData(ongoingMaterialData.get(0).getESTIMATE_NO(), allData.get(0).getGIS_ID());
                        }


                        //NEW EDIT LOCATIONS START

                        if (transmittedEditedData.isEmpty()) {
                            if (onlyLatLongData.isEmpty()) {
                                openDialogForTransmit(message);
                            } else {
                                if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                                    transmitLatLongData();
                                } else {
                                    CommanUtilits.internetDialog(TransmitSurvey.this);
                                }


                                //transmitLatLongData();
                            }
                        } else {
                            if (ConnectivityChecking.checkConnection(TransmitSurvey.this)) {
                                transmitTransmittedData(message);
                            } else {
                                CommanUtilits.internetDialog(TransmitSurvey.this);
                            }

                            // transmitTransmittedData();
                        }

                        //NEW EDIT LOCATIONS END


                    } else {
                        openDialogForSuccessMsg(
                                surveyedResponseVo.get("ERR_MSG").getAsString(),
                                allData.get(0).getGIS_ID(),
                                allData.get(0).getEST_NO(),
                                allData.get(0).getWORK_ORDER() == null
                                        ? ""
                                        : allData.get(0).getWORK_ORDER()
                        );
                        // openDialogForSuccessMsg(surveyedResponseVo.get("ERR_MSG").getAsString(), allData.get(0).getGIS_ID(), allData.get(0).getEST_NO(), allData.get(0).getWORK_ORDER() == null ? null : "");

                        //openDialogForFailure(surveyedResponseVo.get("ERR_MSG").getAsString());

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
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
        title.setText("Pay Attention");

        // 🔹 Build Header + Message
        StringBuilder builder = new StringBuilder();



        // 🔴 Message Styling
        if (msg != null && msg.toLowerCase().contains("newly  proposed")) {
            builder.append("<font color='#D32F2F'><b>⚠ ")
                    .append(msg)
                    .append("</b></font>");
        } else {
            builder.append(msg);
        }

        message.setText(HtmlCompat.fromHtml(builder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        // 🔹 Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("CLOSE");

        okBtn.setOnClickListener(v -> {
            dialog.dismiss();

            Intent intent = new Intent(TransmitSurvey.this, SurveyList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
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


    private void openDialogForSuccessMsgLT(String msg, String GIS_ID, String EST_NO, String WORK_ORDER) {

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
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.suc));
        // 🔹 Title
        title.setText("Pay Attention");

        // 🔹 Build Header + Message
        StringBuilder builder = new StringBuilder();



        // 🔴 Message Styling
        if (msg != null && msg.toLowerCase().contains("newly  proposed")) {
            builder.append("<font color='#D32F2F'><b>⚠ ")
                    .append(msg)
                    .append("</b></font>");
        } else {
            builder.append(msg);
        }

        message.setText(HtmlCompat.fromHtml(builder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        // 🔹 Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("CONTINUE");

        // Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            openCompleteDialogForAll();
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



    private TextView createHeaderRow(String label, String value) {
        TextView tv = new TextView(this);
        tv.setText(label + " : " + value);
        tv.setTextSize(14);
        tv.setTextColor(Color.parseColor("#444444"));
        tv.setPadding(0, 5, 0, 5);
        return tv;
    }

    private void getDbmsAccessToken() {
        System.out.println("FLOW-OF-APIs STEP1");
        loader.setVisibility(View.VISIBLE);
        loader_layout.setVisibility(View.GONE);
        tv_loader_text.setText("1/2 images data is getting transmitted");
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance("").create(RetrofitServiceEstimates.class);

        Call<JsonObject> responseVoCall = retrofitService.getImageAccessToken();

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() == null) {
                    loader.setVisibility(View.VISIBLE);
                    loader_layout.setVisibility(View.GONE);
                    Toast.makeText(TransmitSurvey.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(TransmitSurvey.this, "Invalid response from server: STATUS missing", Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                    loader_layout.setVisibility(View.GONE);
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(TransmitSurvey.this, errorMessage, Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.VISIBLE);
                    loader_layout.setVisibility(View.GONE);
                    return;
                }

                if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                    String accessToken = surveyedResponseVo.get("DATA").getAsString();
                    setOfflineImagesData(accessToken);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                loader_layout.setVisibility(View.GONE);
            }
        });
    }

    private void setOfflineImagesData(String accessToken) {
        loader.setVisibility(View.VISIBLE);
        List<NewModel> imagePostingData = databaseEstimates.getCurrentSurveyData();
       // System.out.println("LIST_SIZE::::" + imagePostingData.size());


        for (int i = 0; i < imagePostingData.size(); i++) {
            if(imagePostingData.get(i).getIS_IMAGE_TRANSMITTED() !=null){

            if (  imagePostingData.get(i).getIS_IMAGE_TRANSMITTED().equalsIgnoreCase("N")) {
                actualImagesCount++;
            }
            if (imagePostingData.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR POLE")) {
                actualImagesCount++;
            }
            if (imagePostingData.get(i).getPOLE_CONFIG().equalsIgnoreCase("HT Service-Pole")) {
                actualImagesCount++;
            }

           // System.out.println("POLE_CONFIG------->" + imagePostingData.get(i).getPOLE_CONFIG());
            }

        }

//        for (int i = 0; i < imagePostingData.size(); i++) {
//            if (imagePostingData.get(i).getIS_IMAGE_TRANSMITTED().equalsIgnoreCase("N")) {
//                callImageSavingAPI(imagePostingData.get(i), accessToken);
//            }else {
//                transmitPendingData();
//            }
//        }

        boolean hasPendingImages = false;

        for (NewModel model : imagePostingData) {
            if(model.getIS_IMAGE_TRANSMITTED() !=null){


            if ("N".equalsIgnoreCase(model.getIS_IMAGE_TRANSMITTED())) {
                hasPendingImages = true;
                callImageSavingAPI(model, accessToken);
            }
            }
        }

        if (!hasPendingImages) {
            // No images to upload, transmit data immediately
            transmitPendingData();
        }
    }

    public void callImageSavingAPI(NewModel postingModel, String accessToken) {
        //System.out.println("FLOW-OF-APIs STEP2" + postingModel.getPOLE_CONFIG());
        loader.setVisibility(View.VISIBLE);
        tv_loader_text.setText("1/2 images data is getting transmitted");

        try {

            ArrayList<String> imageList = new ArrayList<>();
            ArrayList<String> imageColumns = new ArrayList<>();

            // Always upload IMAGE_1
            if (postingModel.getIMAGE_1() != null && !postingModel.getIMAGE_1().isEmpty()) {
                imageList.add(postingModel.getIMAGE_1());
                imageColumns.add(ArcGISDatabaseEstimates.IMAGE_1);
            }

            // Upload IMAGE_2 only for these pole configurations
            if (postingModel.getPOLE_CONFIG() != null &&
                    (postingModel.getPOLE_CONFIG().equalsIgnoreCase("DTR POLE")
                            || postingModel.getPOLE_CONFIG().equalsIgnoreCase("HT Service-Pole")
                            || postingModel.getPOLE_CONFIG().equalsIgnoreCase("HT Service"))) {

                if (postingModel.getIMAGE_2() != null && !postingModel.getIMAGE_2().isEmpty()) {
                    imageList.add(postingModel.getIMAGE_2());
                    imageColumns.add(ArcGISDatabaseEstimates.IMAGE_2);
                }
            }

            for (int i = 0; i < imageList.size(); i++) {

                String imagePath = imageList.get(i);
                final String imageColumn = imageColumns.get(i);

                Uri imageUri = Uri.parse(imagePath);
                //Log.d("URI----------->>>>", imageUri.toString());

                File file = getFileFromUri(TransmitSurvey.this, imageUri);

                RequestBody fileRequestBody =
                        RequestBody.create(MediaType.parse("image/*"), file);

                MultipartBody.Part imagePart =
                        MultipartBody.Part.createFormData(
                                "upload_file",
                                file.getName(),
                                fileRequestBody
                        );

                RequestBody tokenBody =
                        RequestBody.create(MultipartBody.FORM, accessToken);

                RequestBody accountBody =
                        RequestBody.create(MultipartBody.FORM, "2");

                RequestBody folderBody =
                        RequestBody.create(
                                MultipartBody.FORM,
                                "okm:root/GIS_rdssAsset_Images"
                        );
                ImageAPIServiceMeter apiService =
                        ApiClient.getClient().create(ImageAPIServiceMeter.class);
                apiService.uploadImage(tokenBody, accountBody, folderBody, imagePart)
                        .enqueue(new Callback<JsonObject>() {

                            @Override
                            public void onResponse(Call<JsonObject> call,
                                                   Response<JsonObject> response) {

                                loader.setVisibility(View.GONE);
                                loader_layout.setVisibility(View.GONE);
                                if (response.isSuccessful() && response.body() != null) {

                                    Log.d("Image Response", response.body().toString());

                                    JsonArray jsonArray =
                                            response.body().getAsJsonArray("data");

                                    for (int j = 0; j < jsonArray.size(); j++) {

                                        JsonObject object =
                                                jsonArray.get(j).getAsJsonObject();

                                        String shortCode =
                                                object.get("short_url").getAsString();

                                       // Log.d("SHORT CODE", shortCode);
                                       // System.out.println("FLOW-OF-APIs STEP3" + shortCode);
                                        transmittedImagesCount++;
                                        databaseEstimates.updateImageShortCode(
                                                postingModel.getLOCATION_ID(),
                                                imageColumn,
                                                shortCode
                                        );

                                    }

                                    transmit_images.setVisibility(View.GONE);
                                    transmit_all.setVisibility(View.VISIBLE);
//                                    if (actualImagesCount == transmittedImagesCount) {
//                                        transmitPendingData();
//                                        transmit_images.setVisibility(View.GONE);
//                                        transmit_all.setVisibility(View.VISIBLE);
//                                    }


                                } else {

                                    Log.e("UPLOAD",
                                            "Error Code : " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call,
                                                  Throwable t) {

                                loader.setVisibility(View.GONE);
                                loader_layout.setVisibility(View.GONE);

                                Log.e("UPLOAD",
                                        "Failed : " + t.getMessage());
                            }
                        });
            }

        } catch (Exception e) {

            loader.setVisibility(View.GONE);
            loader_layout.setVisibility(View.GONE);

            e.printStackTrace();
        }
    }


    public File getFileFromUri(Context context, Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getCacheDir(), "upload_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return file;
    }
}