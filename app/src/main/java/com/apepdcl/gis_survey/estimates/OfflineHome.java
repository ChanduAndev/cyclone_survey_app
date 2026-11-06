package com.apepdcl.gis_survey.estimates;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.OfflineLTActivity;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class OfflineHome extends AppCompatActivity {

    String EST_NO, GIS_ID;
    ArcGISDatabaseEstimates databaseEstimates;

    TextView est_no, purpose;

    LinearLayout main_layout_33;
    TextView no_locations_33, ss_name_33, feeder_name_33, survey_type_33, continue_survey_33;

    LinearLayout main_layout_11;
    LinearLayout main_layout_11_1;
    TextView no_locations_11_1, ss_name_11_1, feeder_name_11_1, survey_type_11_1, continue_survey_11_1;

    LinearLayout main_layout_11_2;
    TextView no_locations_11_2, ss_name_11_2, feeder_name_11_2, survey_type_11_2, continue_survey_11_2;

    LinearLayout main_layout_11_3;
    TextView no_locations_11_3, ss_name_11_3, feeder_name_11_3, survey_type_11_3, continue_survey_11_3;

    LinearLayout main_layout_11_4;
    TextView no_locations_11_4, ss_name_11_4, feeder_name_11_4, survey_type_11_4, continue_survey_11_4;

    LinearLayout main_layout_11_5;
    TextView no_locations_11_5, ss_name_11_5, feeder_name_11_5, survey_type_11_5, continue_survey_11_5;

    LinearLayout main_layout_11_6;
    TextView no_locations_11_6, ss_name_11_6, feeder_name_11_6, survey_type_11_6, continue_survey_11_6;

    LinearLayout main_layout_11_7;
    TextView no_locations_11_7, ss_name_11_7, feeder_name_11_7, survey_type_11_7, continue_survey_11_7;

    LinearLayout main_layout_11_8;
    TextView no_locations_11_8, ss_name_11_8, feeder_name_11_8, survey_type_11_8, continue_survey_11_8;

    LinearLayout main_layout_11_9;
    TextView no_locations_11_9, ss_name_11_9, feeder_name_11_9, survey_type_11_9, continue_survey_11_9;

    LinearLayout main_layout_11_10;
    TextView no_locations_11_10, ss_name_11_10, feeder_name_11_10, survey_type_11_10, continue_survey_11_10;



    LinearLayout main_layout_lt;
    TextView no_locations_lt, continue_survey_lt;
    RelativeLayout iv_back, close_layout;

    ArrayList<NewModel> totalPoles33;
    ArrayList<NewModel> totalPoles11;
    ArrayList<NewModel> totalPolesLT;


    private PurposeModel model;
    private boolean is33KVSelected = false;
    private boolean is11KVSelected = false;
    private boolean isDTRSelected = false;
    private boolean isLTSelected = false;
    private boolean isMeters = false;
    private boolean is33KVSelectedSurvey = false;
    private boolean is11KVSelectedSurvey = false;
    private boolean isLTSelectedSurvey = false;

    public int LOCATION_COUNT_11KV_FEEDER_1 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_2 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_3 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_4 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_5 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_6 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_7 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_8 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_9 = 0;
    public int LOCATION_COUNT_11KV_FEEDER_10 = 0;


    Button btn_complete_survey;

    //NEW 33KV LINE EXTENSION STARTS
    LinearLayout main_layout_33_1;
    TextView no_locations_33_1, ss_name_33_1, feeder_name_33_1, survey_type_33_1, continue_survey_33_1;

    LinearLayout main_layout_33_2;
    TextView no_locations_33_2, ss_name_33_2, feeder_name_33_2, survey_type_33_2, continue_survey_33_2;

    LinearLayout main_layout_33_3;
    TextView no_locations_33_3, ss_name_33_3, feeder_name_33_3, survey_type_33_3, continue_survey_33_3;

    LinearLayout main_layout_33_4;
    TextView no_locations_33_4, ss_name_33_4, feeder_name_33_4, survey_type_33_4, continue_survey_33_4;

    LinearLayout main_layout_33_5;
    TextView no_locations_33_5, ss_name_33_5, feeder_name_33_5, survey_type_33_5, continue_survey_33_5;

    public int LOCATION_COUNT_33KV_FEEDER_1 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_2 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_3 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_4 = 0;
    public int LOCATION_COUNT_33KV_FEEDER_5 = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_home);
        getSupportActionBar().hide();

        databaseEstimates = new ArcGISDatabaseEstimates(this);
        model = WorkInvolmentsActivity.offlineModel;

        EST_NO = getIntent().getStringExtra("EST_NO");
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        WorkInvolmentsActivity.IS_LT_SELECTED_NEW = getIntent().getBooleanExtra("LT", false);


        iv_back = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        btn_complete_survey = findViewById(R.id.btn_complete_survey);

        est_no = findViewById(R.id.est_no);
        purpose = findViewById(R.id.purpose);

        main_layout_33 = findViewById(R.id.main_layout_33);
        no_locations_33 = findViewById(R.id.no_locations_33);
        ss_name_33 = findViewById(R.id.ss_name_33);
        feeder_name_33 = findViewById(R.id.feeder_name_33);
        survey_type_33 = findViewById(R.id.survey_type_33);
        continue_survey_33 = findViewById(R.id.continue_survey_33);

        main_layout_11 = findViewById(R.id.main_layout_11);

        main_layout_11_1 = findViewById(R.id.main_layout_11_1);
        no_locations_11_1 = findViewById(R.id.no_locations_11_1);
        ss_name_11_1 = findViewById(R.id.ss_name_11_1);
        feeder_name_11_1 = findViewById(R.id.feeder_name_11_1);
        survey_type_11_1 = findViewById(R.id.survey_type_11_1);
        continue_survey_11_1 = findViewById(R.id.continue_survey_11_1);

        main_layout_11_2 = findViewById(R.id.main_layout_11_2);
        no_locations_11_2 = findViewById(R.id.no_locations_11_2);
        ss_name_11_2 = findViewById(R.id.ss_name_11_2);
        feeder_name_11_2 = findViewById(R.id.feeder_name_11_2);
        survey_type_11_2 = findViewById(R.id.survey_type_11_2);
        continue_survey_11_2 = findViewById(R.id.continue_survey_11_2);

        main_layout_11_3 = findViewById(R.id.main_layout_11_3);
        no_locations_11_3 = findViewById(R.id.no_locations_11_3);
        ss_name_11_3 = findViewById(R.id.ss_name_11_3);
        feeder_name_11_3 = findViewById(R.id.feeder_name_11_3);
        survey_type_11_3 = findViewById(R.id.survey_type_11_3);
        continue_survey_11_3 = findViewById(R.id.continue_survey_11_3);

        main_layout_11_4 = findViewById(R.id.main_layout_11_4);
        no_locations_11_4 = findViewById(R.id.no_locations_11_4);
        ss_name_11_4 = findViewById(R.id.ss_name_11_4);
        feeder_name_11_4 = findViewById(R.id.feeder_name_11_4);
        survey_type_11_4 = findViewById(R.id.survey_type_11_4);
        continue_survey_11_4 = findViewById(R.id.continue_survey_11_4);

        main_layout_11_5 = findViewById(R.id.main_layout_11_5);
        main_layout_11_6 = findViewById(R.id.main_layout_11_6);
        main_layout_11_7 = findViewById(R.id.main_layout_11_7);
        main_layout_11_8 = findViewById(R.id.main_layout_11_8);
        main_layout_11_9 = findViewById(R.id.main_layout_11_9);
        main_layout_11_10 = findViewById(R.id.main_layout_11_10);

        no_locations_11_5 = findViewById(R.id.no_locations_11_5);
        no_locations_11_6 = findViewById(R.id.no_locations_11_6);
        no_locations_11_7 = findViewById(R.id.no_locations_11_7);
        no_locations_11_8 = findViewById(R.id.no_locations_11_8);
        no_locations_11_9 = findViewById(R.id.no_locations_11_9);
        no_locations_11_10 = findViewById(R.id.no_locations_11_10);


        ss_name_11_5 = findViewById(R.id.ss_name_11_5);
        ss_name_11_6 = findViewById(R.id.ss_name_11_6);
        ss_name_11_7 = findViewById(R.id.ss_name_11_7);
        ss_name_11_8 = findViewById(R.id.ss_name_11_8);
        ss_name_11_9 = findViewById(R.id.ss_name_11_9);
        ss_name_11_10 = findViewById(R.id.ss_name_11_10);


        feeder_name_11_5 = findViewById(R.id.feeder_name_11_5);
        feeder_name_11_6 = findViewById(R.id.feeder_name_11_6);
        feeder_name_11_7 = findViewById(R.id.feeder_name_11_7);
        feeder_name_11_8 = findViewById(R.id.feeder_name_11_8);
        feeder_name_11_9 = findViewById(R.id.feeder_name_11_9);
        feeder_name_11_10 = findViewById(R.id.feeder_name_11_10);

        survey_type_11_5 = findViewById(R.id.survey_type_11_5);
        survey_type_11_6 = findViewById(R.id.survey_type_11_6);
        survey_type_11_7 = findViewById(R.id.survey_type_11_7);
        survey_type_11_8 = findViewById(R.id.survey_type_11_8);
        survey_type_11_9 = findViewById(R.id.survey_type_11_9);
        survey_type_11_10 = findViewById(R.id.survey_type_11_10);

        continue_survey_11_5 = findViewById(R.id.continue_survey_11_5);
        continue_survey_11_6 = findViewById(R.id.continue_survey_11_6);
        continue_survey_11_7 = findViewById(R.id.continue_survey_11_7);
        continue_survey_11_8 = findViewById(R.id.continue_survey_11_8);
        continue_survey_11_9 = findViewById(R.id.continue_survey_11_9);
        continue_survey_11_10 = findViewById(R.id.continue_survey_11_10);

        //NEW 33KV LINE EXTENSION START
        main_layout_33_1 = findViewById(R.id.main_layout_33_1);
        no_locations_33_1 = findViewById(R.id.no_locations_33_1);
        ss_name_33_1 = findViewById(R.id.ss_name_33_1);
        feeder_name_33_1 = findViewById(R.id.feeder_name_33_1);
        survey_type_33_1 = findViewById(R.id.survey_type_33_1);
        continue_survey_33_1 = findViewById(R.id.continue_survey_33_1);

        main_layout_33_2 = findViewById(R.id.main_layout_33_2);
        no_locations_33_2 = findViewById(R.id.no_locations_33_2);
        ss_name_33_2 = findViewById(R.id.ss_name_33_2);
        feeder_name_33_2 = findViewById(R.id.feeder_name_33_2);
        survey_type_33_2 = findViewById(R.id.survey_type_33_2);
        continue_survey_33_2 = findViewById(R.id.continue_survey_33_2);

        main_layout_33_3 = findViewById(R.id.main_layout_33_3);
        no_locations_33_3 = findViewById(R.id.no_locations_33_3);
        ss_name_33_3 = findViewById(R.id.ss_name_33_3);
        feeder_name_33_3 = findViewById(R.id.feeder_name_33_3);
        survey_type_33_3 = findViewById(R.id.survey_type_33_3);
        continue_survey_33_3 = findViewById(R.id.continue_survey_33_3);

        main_layout_33_4 = findViewById(R.id.main_layout_33_4);
        no_locations_33_4 = findViewById(R.id.no_locations_33_4);
        ss_name_33_4 = findViewById(R.id.ss_name_33_4);
        feeder_name_33_4 = findViewById(R.id.feeder_name_33_4);
        survey_type_33_4 = findViewById(R.id.survey_type_33_4);
        continue_survey_33_4 = findViewById(R.id.continue_survey_33_4);

        main_layout_33_5 = findViewById(R.id.main_layout_33_5);
        no_locations_33_5 = findViewById(R.id.no_locations_33_5);
        ss_name_33_5 = findViewById(R.id.ss_name_33_5);
        feeder_name_33_5 = findViewById(R.id.feeder_name_33_5);
        survey_type_33_5 = findViewById(R.id.survey_type_33_5);
        continue_survey_33_5 = findViewById(R.id.continue_survey_33_5);


        main_layout_lt = findViewById(R.id.main_layout_lt);
        no_locations_lt = findViewById(R.id.no_locations_lt);
        continue_survey_lt = findViewById(R.id.continue_survey_lt);

        est_no.setText(EST_NO);
        // purpose.setText(PURPOSE);

        is33KVSelectedSurvey = databaseEstimates.get33kvSurveyStatusNEW(EST_NO, GIS_ID);
        is11KVSelectedSurvey = databaseEstimates.get11kvSurveyStatusNEW(EST_NO, GIS_ID);
        isLTSelectedSurvey = databaseEstimates.getLTSurveyStatusNEW(EST_NO, GIS_ID);


        //NEW BY SOUBHAGYA START
        totalPoles33 = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
        totalPoles11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);
        totalPolesLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);

        //  System.out.println("11 KV TOTAL POLES : " + totalPoles11.size());

        getSelectedEstimateData();


        //NEW BY SOUBHAGYA END

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_complete_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(OfflineHome.this, SurveyList.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();
            }
        });

       /* continue_survey_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is33KVSelectedSurvey) {
                    *//*showAlertDialog(is33KVSelected, is11KVSelected, isLTSelected, 1, "");
                    Toast.makeText(WorkInvolmentsActivity.this, "Survey Completed", Toast.LENGTH_SHORT).show();*//*
                    WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
                    onBackPressed();
                } else {
                    WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
                    // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "33KV", 1, "");
                    onBackPressed();
                }
            }
        });*/


        //NEW 33KV LINE EXTENSION STARTS HERE

        continue_survey_33_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
                WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
                WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 1";
                WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id1_33kv;
                onBackPressed();
            }
        });


        continue_survey_33_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LOCATION_COUNT_33KV_FEEDER_1 > 0) {
                    WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 2";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id2_33kv;
                    onBackPressed();
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 33KV Feeder 1 Survey First", Toast.LENGTH_SHORT).show();
                }

            }
        });


        continue_survey_33_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LOCATION_COUNT_33KV_FEEDER_2 > 0) {
                    WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 3";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id3_33kv;
                    onBackPressed();
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 33KV Feeder 2 Survey First", Toast.LENGTH_SHORT).show();
                }

            }
        });

        continue_survey_33_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LOCATION_COUNT_33KV_FEEDER_3 > 0) {
                    WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 4";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id4_33kv;
                    onBackPressed();
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 33KV Feeder 3 Survey First", Toast.LENGTH_SHORT).show();
                }

            }
        });

        continue_survey_33_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LOCATION_COUNT_33KV_FEEDER_4 > 0) {
                    WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_33kv;
                    onBackPressed();
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 33KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //NEW 33KV LINE EXTENSION ENDS HERE

        continue_survey_11_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(WorkInvolmentsActivity.this, "CLICKED 1 "+is33KVSelected, Toast.LENGTH_SHORT).show();
                // Toast.makeText(WorkInvolmentsActivity.this, "CLICKED 2 "+is33KVSelectedSurvey, Toast.LENGTH_SHORT).show();
                if (is33KVSelected) {
                    if (LOCATION_COUNT_33KV_FEEDER_1 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 1 Survey", Toast.LENGTH_SHORT).show();
                    } else if (WorkInvolmentsActivity.FEEDER_2_SELECTED_33 && LOCATION_COUNT_33KV_FEEDER_2 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 2 Survey", Toast.LENGTH_SHORT).show();
                    } else if (WorkInvolmentsActivity.FEEDER_3_SELECTED_33 && LOCATION_COUNT_33KV_FEEDER_3 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 3 Survey", Toast.LENGTH_SHORT).show();
                    } else if (WorkInvolmentsActivity.FEEDER_4_SELECTED_33 && LOCATION_COUNT_33KV_FEEDER_4 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 4 Survey", Toast.LENGTH_SHORT).show();
                    } else if (WorkInvolmentsActivity.FEEDER_5_SELECTED_33 && LOCATION_COUNT_33KV_FEEDER_5 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 5 Survey", Toast.LENGTH_SHORT).show();
                    } else {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 1";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id1_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 1, feeder_id1_11kv);
                    }
                } else {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 1";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id1_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    System.out.println("BTN_VALUE_IS_OFFLINE-->" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);
                    onBackPressed();
                    // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 1, feeder_id1_11kv);
                }

            }
        });
        continue_survey_11_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_1 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 2";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id2_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 2, feeder_id2_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete Feeder 1 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_1 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 2";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id2_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        //navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 2, feeder_id2_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 1 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (LOCATION_COUNT_11KV_FEEDER_1 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 2";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id2_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    //navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 2, feeder_id2_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 1 Survey First", Toast.LENGTH_SHORT).show();
                }


            }
        });
        continue_survey_11_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_2 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 3";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id3_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            //  navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 3, feeder_id3_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete Feeder 2 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_2 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 3";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id3_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        //navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 3, feeder_id3_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete Feeder 2 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (LOCATION_COUNT_11KV_FEEDER_2 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 3";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id3_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    //navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 3, feeder_id3_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 2 Survey First", Toast.LENGTH_SHORT).show();
                }

            }
        });
        continue_survey_11_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_3 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 4";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id4_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 4, feeder_id4_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete Feeder 3 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_3 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 4";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id4_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        //navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 4, feeder_id4_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 3 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/


                if (LOCATION_COUNT_11KV_FEEDER_3 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 4";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id4_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    //navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 4, feeder_id4_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 3 Survey First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        continue_survey_11_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        continue_survey_11_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (LOCATION_COUNT_11KV_FEEDER_5 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 6";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id6_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 5 Survey First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        continue_survey_11_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (LOCATION_COUNT_11KV_FEEDER_6 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 7";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id7_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 6 Survey First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        continue_survey_11_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (LOCATION_COUNT_11KV_FEEDER_7 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 8";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id8_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 7 Survey First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        continue_survey_11_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (LOCATION_COUNT_11KV_FEEDER_8 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 9";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id9_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 8 Survey First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        continue_survey_11_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (is33KVSelected) {
                    if (!is33KVSelectedSurvey) {
                        Toast.makeText(OfflineHome.this, "Start with 33 kV Survey ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                            WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                            WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                            onBackPressed();
                            // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                        } else {
                            Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (LOCATION_COUNT_11KV_FEEDER_4 > 0) {
                        WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 5";
                        WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id5_11kv;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                        onBackPressed();
                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                    } else {
                        Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 4 Survey First", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if (LOCATION_COUNT_11KV_FEEDER_9 > 0) {
                    WorkInvolmentsActivity.CLICKED_FEEDER = "FEEDER 10";
                    WorkInvolmentsActivity.CLICKED_FEEDER_ID = WorkInvolmentsActivity.feeder_id10_11kv;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
                    onBackPressed();
                    // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "11KV", 5, feeder_id5_11kv);
                } else {
                    Toast.makeText(OfflineHome.this, "Please Complete 11KV Feeder 9 Survey First", Toast.LENGTH_SHORT).show();
                }
            }
        });


        continue_survey_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is33KVSelected) {
                    if (LOCATION_COUNT_33KV_FEEDER_1 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 1 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WorkInvolmentsActivity.FEEDER_2_SELECTED_33 && LOCATION_COUNT_33KV_FEEDER_2 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 2 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WorkInvolmentsActivity.FEEDER_3_SELECTED_33 && LOCATION_COUNT_33KV_FEEDER_3 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 3 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WorkInvolmentsActivity.FEEDER_4_SELECTED_33 && LOCATION_COUNT_33KV_FEEDER_4 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 4 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WorkInvolmentsActivity.FEEDER_5_SELECTED_33 && LOCATION_COUNT_33KV_FEEDER_5 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 33kv Feeder 5 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (is11KVSelected) {

                    if (WorkInvolmentsActivity.FEEDER_1_SELECTED && LOCATION_COUNT_11KV_FEEDER_1 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 1 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WorkInvolmentsActivity.FEEDER_2_SELECTED && LOCATION_COUNT_11KV_FEEDER_2 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 2 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WorkInvolmentsActivity.FEEDER_3_SELECTED && LOCATION_COUNT_11KV_FEEDER_3 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 3 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WorkInvolmentsActivity.FEEDER_4_SELECTED && LOCATION_COUNT_11KV_FEEDER_4 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 4 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WorkInvolmentsActivity.FEEDER_5_SELECTED && LOCATION_COUNT_11KV_FEEDER_5 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 5 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (WorkInvolmentsActivity.FEEDER_5_SELECTED && LOCATION_COUNT_11KV_FEEDER_6 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 6 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (WorkInvolmentsActivity.FEEDER_5_SELECTED && LOCATION_COUNT_11KV_FEEDER_7 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 7 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (WorkInvolmentsActivity.FEEDER_5_SELECTED && LOCATION_COUNT_11KV_FEEDER_8 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 8 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (WorkInvolmentsActivity.FEEDER_5_SELECTED && LOCATION_COUNT_11KV_FEEDER_9 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 9 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (WorkInvolmentsActivity.FEEDER_5_SELECTED && LOCATION_COUNT_11KV_FEEDER_10 == 0) {
                        Toast.makeText(OfflineHome.this, "Please Complete 11kv Feeder 10 Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    /*if (!is11KVSelectedSurvey) {
                        Toast.makeText(WorkInvolmentsActivity.this, "Start with 11 kV Survey", Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                }

                if (isLTSelected) {
                    if (model.getLINE_11KV().equals("Y") && model.getLINE_LT().equals("Y")) {
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
                        System.out.println("SELECTED_LT_WITH_11KV" + is11KVSelected + " " + "33KV" + is33KVSelected);
                        WorkInvolmentsActivity.IS_LT_SELECTED = true;

                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
                        Intent intent = new Intent(OfflineHome.this, OfflineLTActivity.class);
                        intent.putExtra("EST_NO", EST_NO);
                        intent.putExtra("GIS_ID", GIS_ID);
                        intent.putExtra("EST_NATURE", model.getEST_NATURE());
                        intent.putExtra("PURPOSE", model.getPURPOSE());
                        intent.putExtra("wo_no", model.getWORK_ORDER());
                        intent.putExtra("REG_COUNT", model.getREG_COUNT());
                        intent.putExtra("REG_COUNT", model.getREG_COUNT());
                        intent.putExtra("Model", model);
                        intent.putExtra("FROM", "OFFLINE_LT_WITH_11KV");

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(model);
                        Log.d("OFFLINE_HOME_MODEL", "onCreate: " + json);
                        startActivity(intent);
                    } else if (model.getLINE_LT().equals("Y") && model.getLINE_11KV().isEmpty() || model.getLINE_11KV() == null) {
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
                        System.out.println("SELECTED_LT_WITH_11KV" + is11KVSelected + " " + "33KV" + is33KVSelected);
                        WorkInvolmentsActivity.IS_LT_SELECTED = true;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
                        Intent intent = new Intent(OfflineHome.this, OfflineLTActivity.class);
                        intent.putExtra("EST_NO", EST_NO);
                        intent.putExtra("GIS_ID", GIS_ID);
                        intent.putExtra("EST_NATURE", model.getEST_NATURE());
                        intent.putExtra("PURPOSE", model.getPURPOSE());
                        intent.putExtra("wo_no", model.getWORK_ORDER());
                        intent.putExtra("REG_COUNT", model.getREG_COUNT());
                        intent.putExtra("REG_COUNT", model.getREG_COUNT());
                        intent.putExtra("Model", model);
                        intent.putExtra("FROM", "OFFLINE_ONLY_LT");

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(model);
                        Log.d("OFFLINE_HOME_MODEL", "onCreate: " + json);
                        startActivity(intent);
                    }


//                    if (isLTSelectedSurvey) {
//
//
//                        //  onBackPressed();
//                    } else {
//                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                        String json = gson.toJson(model);
//                        Log.d("OFFLINE_HOME_MODEL", "onCreate: " + json);
//
////                        WorkInvolmentsActivity.IS_LT_SELECTED = true;
////                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
////                        System.out.println("SELECTED_VOLTAGE_LEVEL_LT"+is11KVSelected + " "+ "33KV"+is33KVSelected+ " "+ "LT"+isLTSelected);
////                        Intent intent = new Intent(OfflineHome.this, OfflineLTActivity.class);
////                        intent.putExtra("EST_NO",EST_NO);
////                        intent.putExtra("GIS_ID",GIS_ID);
////                        intent.putExtra("EST_NATURE",model.getEST_NATURE());
////                        intent.putExtra("PURPOSE",model.getPURPOSE());
////                        intent.putExtra("wo_no",model.getWORK_ORDER());
////                        intent.putExtra("REG_COUNT",model.getREG_COUNT());
////                        intent.putExtra("REG_COUNT",model.getREG_COUNT());
////                        intent.putExtra("Model",model);
////
////                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
////                        String json = gson.toJson(model);
////                        Log.d("OFFLINE_HOME_MODEL", "onCreate: " + json);
////
////                        startActivity(intent);
//                        //onBackPressed();
//                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "")
//                    }
                } else {
                    WorkInvolmentsActivity.IS_LT_SELECTED = true;
                    WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
                    onBackPressed();
                    //  navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (OfflineLTActivity.OFFLINE_LT) {
            System.out.println("OFFLINE_LT_TAG" + true);
            onBackPressed();
            OfflineLTActivity.OFFLINE_LT = false; // Reset after, but be cautious
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
            WorkInvolmentsActivity.IS_LT_SELECTED = true;

        }
    }

    private void getSelectedEstimateData() {
        try {

            if (model.getLINE_LT().equalsIgnoreCase("Y")) {
                isLTSelected = true;
                main_layout_lt.setVisibility(View.VISIBLE);
                no_locations_lt.setText("" + totalPolesLT.size());


                if (totalPolesLT.isEmpty()) {
                    continue_survey_lt.setText("START SURVEY");
                } else {
                    continue_survey_lt.setText("CONTINUE SURVEY");
                }

            }

            /*if (model.getLINE_33KV().equalsIgnoreCase("Y")) {
                main_layout_33.setVisibility(View.VISIBLE);
                is33KVSelected = true;
                no_locations_33.setText("" + totalPoles33.size());
                if (totalPoles33.isEmpty()) {
                    continue_survey_33.setText("START SURVEY");
                } else {
                    continue_survey_33.setText("CONTINUE SURVEY");
                }

                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                        ss_name_33.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                        feeder_name_33.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                        survey_type_33.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                    }
                }
            }*/


            //NEW 33KV LINE EXTENSION STARTS

            if (model.getLINE_33KV().equalsIgnoreCase("Y")) {
                main_layout_33.setVisibility(View.VISIBLE);
                is33KVSelected = true;

                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 1) {
                            WorkInvolmentsActivity.FEEDER_1_SELECTED_33 = true;
                            WorkInvolmentsActivity.FEEDER_1_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder1SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_1.setVisibility(View.VISIBLE);
                            ss_name_33_1.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_33_1.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_33_1.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());


                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_33_1.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_1.setText(WorkInvolmentsActivity.feeder_name1_33kv);
                            }


                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 2) {
                            WorkInvolmentsActivity.FEEDER_2_SELECTED_33 = true;
                            WorkInvolmentsActivity.FEEDER_2_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder2SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_2.setVisibility(View.VISIBLE);
                            ss_name_33_2.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_33_2.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_33_2.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_33_2.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_2.setText(WorkInvolmentsActivity.feeder_name2_33kv);
                            }


                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 3) {
                            WorkInvolmentsActivity.FEEDER_3_SELECTED_33 = true;
                            WorkInvolmentsActivity.FEEDER_3_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder3SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_3.setVisibility(View.VISIBLE);
                            ss_name_33_3.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            //  feeder_name_33_3.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_33_3.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_33_3.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_3.setText(WorkInvolmentsActivity.feeder_name3_33kv);
                            }


                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 4) {
                            WorkInvolmentsActivity.FEEDER_4_SELECTED_33 = true;
                            WorkInvolmentsActivity.FEEDER_4_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder4SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_4.setVisibility(View.VISIBLE);
                            ss_name_33_4.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_33_4.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_33_4.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_33_4.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_4.setText(WorkInvolmentsActivity.feeder_name4_33kv);
                            }


                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 5) {
                            WorkInvolmentsActivity.FEEDER_5_SELECTED_33 = true;
                            WorkInvolmentsActivity.FEEDER_5_SURVEY_COMPLETED_33 = databaseEstimates.getFeeder5SurveyStatusNEW33(EST_NO, GIS_ID);
                            main_layout_33_5.setVisibility(View.VISIBLE);
                            ss_name_33_5.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_33_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_33_5.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_33_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_33_5.setText(WorkInvolmentsActivity.feeder_name5_33kv);
                            }


                        }


                    }
                }

                for (int i = 0; i < totalPoles33.size(); i++) {
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_1++;

                    }
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_2++;

                    }
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_3++;

                    }
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_4++;

                    }
                    if (totalPoles33.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_33kv)) {
                        LOCATION_COUNT_33KV_FEEDER_5++;

                    }
                }
            }

            no_locations_33_1.setText("" + LOCATION_COUNT_33KV_FEEDER_1);
            no_locations_33_2.setText("" + LOCATION_COUNT_33KV_FEEDER_2);
            no_locations_33_3.setText("" + LOCATION_COUNT_33KV_FEEDER_3);
            no_locations_33_4.setText("" + LOCATION_COUNT_33KV_FEEDER_4);
            no_locations_33_5.setText("" + LOCATION_COUNT_33KV_FEEDER_5);

            if (LOCATION_COUNT_33KV_FEEDER_1 == 0) {
                continue_survey_33_1.setText("START SURVEY");
            } else {
                continue_survey_33_1.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_33KV_FEEDER_2 == 0) {
                continue_survey_33_2.setText("START SURVEY");
            } else {
                continue_survey_33_2.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_33KV_FEEDER_3 == 0) {
                continue_survey_33_3.setText("START SURVEY");
            } else {
                continue_survey_33_3.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_33KV_FEEDER_4 == 0) {
                continue_survey_33_4.setText("START SURVEY");
            } else {
                continue_survey_33_4.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_33KV_FEEDER_5 == 0) {
                continue_survey_33_5.setText("START SURVEY");
            } else {
                continue_survey_33_5.setText("CONTINUE SURVEY");
            }

            //NEW 33KV LINE EXTENSION ENDS

            if (model.getLINE_11KV().equalsIgnoreCase("Y")) {
                main_layout_11.setVisibility(View.VISIBLE);
                is11KVSelected = true;

                for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
                    if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 1) {
                            WorkInvolmentsActivity.FEEDER_1_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_1_SURVEY_COMPLETED = databaseEstimates.getFeeder1SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_1.setVisibility(View.VISIBLE);
                            ss_name_11_1.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_1.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_1.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());


                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_1.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_1.setText(WorkInvolmentsActivity.feeder_name1_11kv);
                            }


                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 2) {
                            WorkInvolmentsActivity.FEEDER_2_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_2_SURVEY_COMPLETED = databaseEstimates.getFeeder2SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_2.setVisibility(View.VISIBLE);
                            ss_name_11_2.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_2.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_2.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_2.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_2.setText(WorkInvolmentsActivity.feeder_name2_11kv);
                            }


                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 3) {
                            WorkInvolmentsActivity.FEEDER_3_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_3_SURVEY_COMPLETED = databaseEstimates.getFeeder3SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_3.setVisibility(View.VISIBLE);
                            ss_name_11_3.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_3.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_3.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_3.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_3.setText(WorkInvolmentsActivity.feeder_name3_11kv);
                            }


                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 4) {
                            WorkInvolmentsActivity.FEEDER_4_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_4_SURVEY_COMPLETED = databaseEstimates.getFeeder4SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_4.setVisibility(View.VISIBLE);
                            ss_name_11_4.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            //feeder_name_11_4.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_4.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_4.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_4.setText(WorkInvolmentsActivity.feeder_name4_11kv);
                            }
                        }
                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 5) {
                            WorkInvolmentsActivity.FEEDER_5_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_5_SURVEY_COMPLETED = databaseEstimates.getFeeder5SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_5.setVisibility(View.VISIBLE);
                            ss_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_5.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_5.setText(WorkInvolmentsActivity.feeder_name5_11kv);
                            }
                        }

                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 6) {
                            WorkInvolmentsActivity.FEEDER_6_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_6_SURVEY_COMPLETED = databaseEstimates.getFeeder6SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_6.setVisibility(View.VISIBLE);
                            ss_name_11_6.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_6.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_6.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_6.setText(WorkInvolmentsActivity.feeder_name6_11kv);
                            }
                        }

                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 7) {
                            WorkInvolmentsActivity.FEEDER_7_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_7_SURVEY_COMPLETED = databaseEstimates.getFeeder7SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_7.setVisibility(View.VISIBLE);
                            ss_name_11_7.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_7.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_7.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_7.setText(WorkInvolmentsActivity.feeder_name7_11kv);
                            }
                        }

                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 8) {
                            WorkInvolmentsActivity.FEEDER_8_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_8_SURVEY_COMPLETED = databaseEstimates.getFeeder8SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_8.setVisibility(View.VISIBLE);
                            ss_name_11_8.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_8.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_8.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_8.setText(WorkInvolmentsActivity.feeder_name8_11kv);
                            }
                        }

                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 9) {
                            WorkInvolmentsActivity.FEEDER_9_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_9_SURVEY_COMPLETED = databaseEstimates.getFeeder9SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_9.setVisibility(View.VISIBLE);
                            ss_name_11_9.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_9.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_9.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_9.setText(WorkInvolmentsActivity.feeder_name9_11kv);
                            }
                        }

                        if (model.getFDR_PARTICULARS().get(i).getFDR_INDEX() == 10) {
                            WorkInvolmentsActivity.FEEDER_10_SELECTED = true;
                            WorkInvolmentsActivity.FEEDER_10_SURVEY_COMPLETED = databaseEstimates.getFeeder10SurveyStatusNEW(EST_NO, GIS_ID);
                            main_layout_11_10.setVisibility(View.VISIBLE);
                            ss_name_11_10.setText(model.getFDR_PARTICULARS().get(i).getSS_NAME());
                            // feeder_name_11_5.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            survey_type_11_10.setText(model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE());

                            if (model.getFDR_PARTICULARS().get(i).getSURVEY_TYPE().equalsIgnoreCase("LINE EXTENSION")) {
                                feeder_name_11_10.setText(model.getFDR_PARTICULARS().get(i).getFEEDER());
                            } else {
                                feeder_name_11_10.setText(WorkInvolmentsActivity.feeder_name10_11kv);
                            }
                        }
                    }
                }

                for (int i = 0; i < totalPoles11.size(); i++) {
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id1_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_1++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id2_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_2++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id3_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_3++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id4_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_4++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id5_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_5++;

                    }


                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id6_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_6++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id7_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_7++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id8_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_8++;

                    }
                    if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id9_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_9++;

                    }if (totalPoles11.get(i).getFEEDER_ID().equalsIgnoreCase(WorkInvolmentsActivity.feeder_id10_11kv)) {
                        LOCATION_COUNT_11KV_FEEDER_10++;

                    }
                }
            }


            no_locations_11_1.setText("" + LOCATION_COUNT_11KV_FEEDER_1);
            no_locations_11_2.setText("" + LOCATION_COUNT_11KV_FEEDER_2);
            no_locations_11_3.setText("" + LOCATION_COUNT_11KV_FEEDER_3);
            no_locations_11_4.setText("" + LOCATION_COUNT_11KV_FEEDER_4);
            no_locations_11_5.setText("" + LOCATION_COUNT_11KV_FEEDER_5);

            no_locations_11_6.setText("" + LOCATION_COUNT_11KV_FEEDER_6);
            no_locations_11_7.setText("" + LOCATION_COUNT_11KV_FEEDER_7);
            no_locations_11_8.setText("" + LOCATION_COUNT_11KV_FEEDER_8);
            no_locations_11_9.setText("" + LOCATION_COUNT_11KV_FEEDER_9);
            no_locations_11_10.setText("" + LOCATION_COUNT_11KV_FEEDER_10);

            if (LOCATION_COUNT_11KV_FEEDER_1 == 0) {
                continue_survey_11_1.setText("START SURVEY");
            } else {
                continue_survey_11_1.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_11KV_FEEDER_2 == 0) {
                continue_survey_11_2.setText("START SURVEY");
            } else {
                continue_survey_11_2.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_11KV_FEEDER_3 == 0) {
                continue_survey_11_3.setText("START SURVEY");
            } else {
                continue_survey_11_3.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_11KV_FEEDER_4 == 0) {
                continue_survey_11_4.setText("START SURVEY");
            } else {
                continue_survey_11_4.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_11KV_FEEDER_5 == 0) {
                continue_survey_11_5.setText("START SURVEY");
            } else {
                continue_survey_11_5.setText("CONTINUE SURVEY");
            }


            if (LOCATION_COUNT_11KV_FEEDER_6 == 0) {
                continue_survey_11_6.setText("START SURVEY");
            } else {
                continue_survey_11_6.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_11KV_FEEDER_7 == 0) {
                continue_survey_11_7.setText("START SURVEY");
            } else {
                continue_survey_11_7.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_11KV_FEEDER_8 == 0) {
                continue_survey_11_8.setText("START SURVEY");
            } else {
                continue_survey_11_8.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_11KV_FEEDER_9 == 0) {
                continue_survey_11_9.setText("START SURVEY");
            } else {
                continue_survey_11_9.setText("CONTINUE SURVEY");
            }

            if (LOCATION_COUNT_11KV_FEEDER_10 == 0) {
                continue_survey_11_10.setText("START SURVEY");
            } else {
                continue_survey_11_10.setText("CONTINUE SURVEY");
            }






        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}