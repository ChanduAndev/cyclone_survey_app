package com.apepdcl.gis_survey.estimates.sou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class Edit33Home extends AppCompatActivity implements EditLocationAdapter.OnItemClickListener {
    String EST_NO, GIS_ID, FEEDER_ID, FEEDER_NAME, FEEDER_NO, FROM, WORK_ORDER, EST_NATURE, DESCRIPTION, MAX_LENGTH;
    RelativeLayout iv_back, close_layout;
    TextView est_no, gis_id, work_order, home_heading, tv_est_nature, tv_purpose,tv_action_remarks,gis_id_wo_rdss,tv_feeders_wo_rdss;
    LinearLayout layout_gis_id_details_wo_rdss,layout_gis_details_est_wo;
    RecyclerView edit_rv;
    EditLocationAdapter adapter;
    ArrayList<NewModel> list, list33, listLt;
    ArrayList<NewModel> editPolesList33, editPolesList11, editPolesListLT;
    ArcGISDatabaseEstimates databaseEstimates;

    public static boolean IS_RESUMED;
    PurposeModel purposeModel;
    public static String SELECTED_LT_CKT = "";
    public static String SELECTED_DTR_LOCATION = "";
    private String RDSS_TYPE="",FEEDERS="";


    @Override
    protected void onResume() {
        super.onResume();
        if (IS_RESUMED) {
            list.clear();
            list33.clear();
            listLt.clear();
            editPolesList33.clear();
            editPolesList11.clear();
            editPolesListLT.clear();
            editPolesList33 = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
            editPolesList11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);
            editPolesListLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);
            setRecyclerView();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_33_home);
        getSupportActionBar().hide();

        //INFO
        TextView header_2 = findViewById(R.id.header_2);
        TextView header_1 = findViewById(R.id.header_1);
        TextView desc_2 = findViewById(R.id.new_bay_desc);
        TextView desc_1 = findViewById(R.id.line_extension_desc);
        layout_gis_details_est_wo = findViewById(R.id.layout_gis_details_est_wo);
        layout_gis_id_details_wo_rdss = findViewById(R.id.layout_gis_id_details_wo_rdss);


        System.out.println("INFO-->" + SurveyList.infoList.size());

        for (int i = 0; i < SurveyList.infoList.size(); i++) {
            // System.out.println("FEATURE: " + SurveyList.infoList.get(i).getFEATURE());
            //System.out.println("FEATURE_SUB_TYPE: " + SurveyList.infoList.get(i).getFEATURE_SUB_TYPE());

            if ("EDIT OPTION".equalsIgnoreCase(SurveyList.infoList.get(i).getFEATURE().trim()) &&
                    "Update Location".equalsIgnoreCase(SurveyList.infoList.get(i).getFEATURE_SUB_TYPE().trim())) {
                header_1.setText(SurveyList.infoList.get(i).getFEATURE_SUB_TYPE());
                //System.out.println("INFO1-->" + SurveyList.infoList.get(i).getFEATURE_SUB_TYPE());
                // System.out.println("INFO2-->" + SurveyList.infoList.get(i).getDESCRIPTION());
                desc_1.setText(SurveyList.infoList.get(i).getDESCRIPTION());
            }

            if (SurveyList.infoList.get(i).getFEATURE().equalsIgnoreCase("EDIT OPTION")
                    && SurveyList.infoList.get(i).getFEATURE_SUB_TYPE().equalsIgnoreCase("Update Attributes")) {
                header_2.setText(SurveyList.infoList.get(i).getFEATURE_SUB_TYPE());
                desc_2.setText(SurveyList.infoList.get(i).getDESCRIPTION());
            }
        }


        IS_RESUMED = false;
        databaseEstimates = new ArcGISDatabaseEstimates(this);
        list = new ArrayList<>();
        list33 = new ArrayList<>();
        listLt = new ArrayList<>();


        EST_NO = getIntent().getStringExtra("EST_NO");
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        FEEDER_ID = getIntent().getStringExtra("FEEDER_ID");
        FEEDER_NAME = getIntent().getStringExtra("FEEDER_NAME");
        FEEDER_NO = getIntent().getStringExtra("FEEDER_NO");
        FROM = getIntent().getStringExtra("FROM");
        WORK_ORDER = getIntent().getStringExtra("WORK_ORDER");
        SELECTED_LT_CKT = getIntent().getStringExtra("SELECTED_LT_CKT");
        EST_NATURE = getIntent().getStringExtra("EST_NATURE");
        DESCRIPTION = getIntent().getStringExtra("PURPOSE");
        MAX_LENGTH = getIntent().getStringExtra("MAX_LENGTH");
        if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)){
            FEEDERS = getIntent().getStringExtra("FEEDERS");
            RDSS_TYPE = getIntent().getStringExtra("RDSS_TYPE");
        }

        purposeModel = (PurposeModel) getIntent().getSerializableExtra("Model");
        SELECTED_DTR_LOCATION = getIntent().getStringExtra("SELECTED_DTR_LOCATION");
        editPolesList33 = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
        editPolesList11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);
        editPolesListLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);

        //  System.out.println("33kv SIZE : " + editPolesList33.size());
        //  System.out.println("11kv SIZE : " + editPolesList11.size());
        //  System.out.println("LT SIZE : " + editPolesListLT.size());
        //  System.out.println("SELECTED FEEDER ID : " + FEEDER_ID);


        iv_back = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        edit_rv = findViewById(R.id.edit_rv);

        est_no = findViewById(R.id.est_no);
        gis_id = findViewById(R.id.gis_id);
        work_order = findViewById(R.id.wo_no);
        home_heading = findViewById(R.id.home_heading);
        tv_est_nature = findViewById(R.id.tv_est_nature);
        tv_purpose = findViewById(R.id.tv_purpose);
        tv_action_remarks = findViewById(R.id.tv_action_remarks);
        gis_id_wo_rdss = findViewById(R.id.gis_id_wo_rdss);
        tv_feeders_wo_rdss = findViewById(R.id.tv_feeders_wo_rdss);

        est_no.setText(EST_NO);
        gis_id.setText(GIS_ID);
        tv_est_nature.setText(EST_NATURE);
        tv_purpose.setText(DESCRIPTION);

        home_heading.setText(FROM + " Locations");
        work_order.setText(WORK_ORDER);

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            layout_gis_id_details_wo_rdss.setVisibility(View.GONE);
            layout_gis_details_est_wo.setVisibility(View.VISIBLE);
            work_order.setVisibility(View.GONE);
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            layout_gis_id_details_wo_rdss.setVisibility(View.GONE);
            layout_gis_details_est_wo.setVisibility(View.VISIBLE);
            est_no.setText("DTR Code: " + EST_NO);
            tv_action_remarks.setText(EstimatesHome.ACTION_REMARKS);
            tv_action_remarks.setVisibility(View.VISIBLE);
            tv_est_nature.setVisibility(View.GONE);
        } else if(LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")){
            work_order.setVisibility(View.VISIBLE);
            layout_gis_id_details_wo_rdss.setVisibility(View.GONE);
            layout_gis_details_est_wo.setVisibility(View.VISIBLE);
        }else{
            gis_id_wo_rdss.setText(GIS_ID);
            tv_feeders_wo_rdss.setText(GIS_ID);
            layout_gis_id_details_wo_rdss.setVisibility(View.VISIBLE);
            layout_gis_details_est_wo.setVisibility(View.GONE);
        }


        iv_back.setOnClickListener(v -> onBackPressed());
        close_layout.setOnClickListener(v -> onBackPressed());

        setRecyclerView();


    }

    private void setRecyclerView() {
        edit_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        edit_rv.setLayoutManager(layoutManager);
        edit_rv.setFocusable(false);
        if (FROM.equalsIgnoreCase("33KV")) {
            list33.clear();
            for (int i = 0; i < editPolesList33.size(); i++) {
                if (editPolesList33.get(i).getFEEDER_ID().equalsIgnoreCase(FEEDER_ID)) {
                    list33.add(editPolesList33.get(i));
                }
            }

            adapter = new EditLocationAdapter(list33, this, GIS_ID, this);


        }
        if (FROM.equalsIgnoreCase("LT")) {
            listLt.clear();
            for (int i = 0; i < editPolesListLT.size(); i++) {
                if (editPolesListLT.get(i).getLT_AVAILABLE().equalsIgnoreCase(SELECTED_LT_CKT)) {
                    listLt.add(editPolesListLT.get(i));
                }

            }
            adapter = new EditLocationAdapter(listLt, this, GIS_ID, this);
        }
        if (FROM.equalsIgnoreCase("11KV")) {
            list.clear();
            for (int i = 0; i < editPolesList11.size(); i++) {
                if (editPolesList11.get(i).getFEEDER_ID().equalsIgnoreCase(FEEDER_ID)) {
                    list.add(editPolesList11.get(i));
                }
            }
            adapter = new EditLocationAdapter(list, this, GIS_ID, this);

        }

        edit_rv.setAdapter(adapter);
    }

//    private void showCustomEditDialog(NewModel model) {
//
//        BottomSheetDialog dialog = new BottomSheetDialog(this);
//        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_options, null);
//        dialog.setContentView(dialogView);
//       // dialog.show();
//
//
//       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_edit_options, null);
//        builder.setView(dialogView);
//
//        AlertDialog dialog = builder.create();*/
//
//        // Find views
//        RelativeLayout optionAll = dialogView.findViewById(R.id.option_all);
//        RelativeLayout optionPole = dialogView.findViewById(R.id.option_pole);
//        RelativeLayout optionDTR = dialogView.findViewById(R.id.option_source);
//        RelativeLayout optionSourcePole = dialogView.findViewById(R.id.option_source_pole);
//        RelativeLayout option_boq = dialogView.findViewById(R.id.option_boq);
//        RelativeLayout option_httvr = dialogView.findViewById(R.id.option_httvr);
//        RelativeLayout pole_config = dialogView.findViewById(R.id.option_pole_config);
//
//        if (model.getPOLE_CONFIG().equalsIgnoreCase("RMU") || model.getPOLE_CONFIG().equalsIgnoreCase("UG Cable Route Marker")) {
//            optionPole.setVisibility(View.GONE);
//        }
//        if ((FROM.equalsIgnoreCase("33KV") || FROM.equalsIgnoreCase("11KV")) && model.getPOLE_CONFIG().equalsIgnoreCase("UG Cable Route Marker")) {
//            option_boq.setVisibility(View.GONE);
//        }
//
//
//
//
//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
//            if (FROM.equalsIgnoreCase("11KV")
//                    && model.getPOLE_CONFIG().equalsIgnoreCase("DTR POLE")
//            ) {
//                optionDTR.setVisibility(View.VISIBLE);
//
//            }
//        } else {
//            if (FROM.equalsIgnoreCase("11KV") && model.getPOLE_CONFIG().equalsIgnoreCase("DTR POLE")) {
//                optionDTR.setVisibility(View.VISIBLE);
//            }
//
//        }
//
//        if (FROM.equalsIgnoreCase("11KV") && model.getPOLE_CONFIG().equalsIgnoreCase("HT SERVICE-POLE")) {
//            option_httvr.setVisibility(View.VISIBLE);
//        }
//
//
//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")){
//            optionAll.setVisibility(View.GONE);
//            option_httvr.setVisibility(View.GONE);
//            pole_config.setVisibility(View.GONE);
//        }
//
//        // Handle clicks
//        optionAll.setOnClickListener(v -> {
//            dialog.dismiss();
//            editAllAttributes(model);
//
//        });
//
//        optionPole.setOnClickListener(v -> {
//            dialog.dismiss();
//            editPoleAttributes(model);
//
//        });
//
//        optionDTR.setOnClickListener(v -> {
//            dialog.dismiss();
//            editDTRParticulars(model);
//
//        });
//
//        optionSourcePole.setOnClickListener(v -> {
//            dialog.dismiss();
//            editSourceLocation(model);
//        });
//
//        option_boq.setOnClickListener(v -> {
//            dialog.dismiss();
//            editBOQDetails(model);
//        });
//
//        option_httvr.setOnClickListener(v -> {
//            dialog.dismiss();
//            editHttvrPoleDetails(model);
//        });
//
//        pole_config.setOnClickListener(v->{
//            editPoleConfigDetails(model);
//            dialog.dismiss();
//        });
//
//        dialog.show();
//    }

    private void showCustomEditDialog(NewModel model) {

        BottomSheetDialog dialog = new BottomSheetDialog(this);

        View dialogView = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_edit_options, null);

        dialog.setContentView(dialogView);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
        }

        ImageView ivClose = dialogView.findViewById(R.id.iv_close);

        RelativeLayout optionAll =
                dialogView.findViewById(R.id.option_all);

        RelativeLayout optionPole =
                dialogView.findViewById(R.id.option_pole);

        RelativeLayout optionDTR =
                dialogView.findViewById(R.id.option_source);

        RelativeLayout optionSourcePole =
                dialogView.findViewById(R.id.option_source_pole);

        RelativeLayout option_boq =
                dialogView.findViewById(R.id.option_boq);

        RelativeLayout option_httvr =
                dialogView.findViewById(R.id.option_httvr);

        RelativeLayout pole_config =
                dialogView.findViewById(R.id.option_pole_config);


        // Close
        ivClose.setOnClickListener(v -> dialog.dismiss());


        // Your existing visibility logic
        if (model.getPOLE_CONFIG().equalsIgnoreCase("RMU")
                || model.getPOLE_CONFIG()
                .equalsIgnoreCase("UG Cable Route Marker")) {

            optionPole.setVisibility(View.GONE);
        }

        if ((FROM.equalsIgnoreCase("33KV")
                || FROM.equalsIgnoreCase("11KV"))
                && model.getPOLE_CONFIG()
                .equalsIgnoreCase("UG Cable Route Marker")) {

            option_boq.setVisibility(View.GONE);
        }


        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {

            if (FROM.equalsIgnoreCase("11KV")
                    && model.getPOLE_CONFIG()
                    .equalsIgnoreCase("DTR POLE")) {

                optionDTR.setVisibility(View.VISIBLE);
            }

        } else {

            if (FROM.equalsIgnoreCase("11KV")
                    && model.getPOLE_CONFIG()
                    .equalsIgnoreCase("DTR POLE")) {

                optionDTR.setVisibility(View.VISIBLE);
            }
        }


        if (FROM.equalsIgnoreCase("11KV")
                && model.getPOLE_CONFIG()
                .equalsIgnoreCase("HT SERVICE-POLE")) {

            option_httvr.setVisibility(View.VISIBLE);
        }


        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {

            optionAll.setVisibility(View.GONE);
            option_httvr.setVisibility(View.GONE);
            pole_config.setVisibility(View.GONE);
        }


        // Clicks
        optionAll.setOnClickListener(v -> {
            dialog.dismiss();
            editAllAttributes(model);
        });

        optionPole.setOnClickListener(v -> {
            dialog.dismiss();
            editPoleAttributes(model);
        });

        optionDTR.setOnClickListener(v -> {
            dialog.dismiss();
            editDTRParticulars(model);
        });

        optionSourcePole.setOnClickListener(v -> {
            dialog.dismiss();
            editSourceLocation(model);
        });

        option_boq.setOnClickListener(v -> {
            dialog.dismiss();
            editBOQDetails(model);
        });

        option_httvr.setOnClickListener(v -> {
            dialog.dismiss();
            editHttvrPoleDetails(model);
        });

        pole_config.setOnClickListener(v -> {
            dialog.dismiss();
            editPoleConfigDetails(model);
        });

        dialog.show();
    }
    private void editPoleConfigDetails(NewModel model){
        if (FROM.equalsIgnoreCase("33KV")) {
            //NEW 33KV LINE EXTENSION
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;


            // System.out.println("33KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("33KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("33KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
            Intent intent = new Intent(this, EditPoleConfigDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "POLE_CONFIG");


            startActivity(intent);
        }
        else if (FROM.equalsIgnoreCase("11KV")) {
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";

            // System.out.println("11KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("11KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("11KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            Intent intent = new Intent(this, EditPoleConfigDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "POLE_CONFIG");
            startActivity(intent);
        }
        else {
            WorkInvolmentsActivity.IS_LT_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";

            System.out.println("BABJI_RAISED_ISSUE_LT-->" + WorkInvolmentsActivity.SELECTED_LT_CKT);
            System.out.println("BABJI_RAISED_ISSUE_LT-->" + model.getLT_AVAILABLE());
            Intent intent = new Intent(this, EditPoleConfigDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", "");
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "POLE_CONFIG");

            startActivity(intent);
        }

    }

    private void editHttvrPoleDetails(NewModel model) {
        if (FROM.equalsIgnoreCase("33KV")) {
            //NEW 33KV LINE EXTENSION
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;


            // System.out.println("33KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("33KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("33KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
            Intent intent = new Intent(this, EditPoleDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "HTTVR");


            startActivity(intent);
        }
        else if (FROM.equalsIgnoreCase("11KV")) {
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";

            // System.out.println("11KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("11KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("11KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            Intent intent = new Intent(this, EditPoleDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "HTTVR");
            startActivity(intent);
        }
    }

    private void editBOQDetails(NewModel model) {
        if (FROM.equalsIgnoreCase("33KV")) {
            //NEW 33KV LINE EXTENSION
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;


            // System.out.println("33KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("33KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("33KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
            Intent intent = new Intent(this, EditBOQDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "BOQ_DETAILS");


            startActivity(intent);
        }
        else if (FROM.equalsIgnoreCase("11KV")) {
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";

            // System.out.println("11KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("11KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("11KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            Intent intent = new Intent(this, EditBOQDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "BOQ_DETAILS");
            startActivity(intent);
        }
        else {
            WorkInvolmentsActivity.IS_LT_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";

            System.out.println("BABJI_RAISED_ISSUE_LT-->" + WorkInvolmentsActivity.SELECTED_LT_CKT);
            System.out.println("BABJI_RAISED_ISSUE_LT-->" + model.getLT_AVAILABLE());
            Intent intent = new Intent(this, EditBOQDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", "");
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "BOQ_DETAILS");

            startActivity(intent);
        }
    }

    // Example methods
    private void editAllAttributes(NewModel model) {
        if (FROM.equalsIgnoreCase("33KV")) {
            //NEW 33KV LINE EXTENSION
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;

            // System.out.println("33KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("33KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("33KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
            Intent intent = new Intent(this, EstimatesMap.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            startActivity(intent);
        } else if (FROM.equalsIgnoreCase("11KV")) {
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";
            Intent intent = new Intent(this, EstimatesMap.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                intent.putExtra("FEEDERS", FEEDERS);
                intent.putExtra("RDSS_TYPE", RDSS_TYPE);
            }
            startActivity(intent);
        } else {
            WorkInvolmentsActivity.IS_LT_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
            Intent intent = new Intent(this, EstimatesMap.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", "");
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            startActivity(intent);
        }

    }

    private void editPoleAttributes(NewModel model) {
        if (FROM.equalsIgnoreCase("33KV")) {
            //NEW 33KV LINE EXTENSION
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;

            // System.out.println("33KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("33KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("33KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
            Intent intent = new Intent(this, EditPoleDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "POLE");

            startActivity(intent);
        } else if (FROM.equalsIgnoreCase("11KV")) {
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";

            // System.out.println("11KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("11KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("11KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            Intent intent = new Intent(this, EditPoleDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "POLE");
            if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
                intent.putExtra("FEEDERS", FEEDERS);
                intent.putExtra("RDSS_TYPE", RDSS_TYPE);
            }

            startActivity(intent);
        } else {
            WorkInvolmentsActivity.IS_LT_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
            Intent intent = new Intent(this, EditPoleDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", "");
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "POLE");
            startActivity(intent);
        }

//        //NEW 33KV LINE EXTENSION
//        WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
//        WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;
//
//        // System.out.println("33KV CLICKED FEEDER : " + FEEDER_NO);
//        //  System.out.println("33KV CLICKED FEEDER ID : " + FEEDER_ID);
//        //  System.out.println("33KV CLICKED FEEDER NAME : " + FEEDER_NAME);
//
//        WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
//        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
//        Intent intent = new Intent(this, EditPoleDetailsActivity.class);
//        intent.putExtra("EST_NO", EST_NO);
//        intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
//        intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
//        intent.putExtra("GIS_ID", GIS_ID);
//        intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
//        intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
//        intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
//        intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
//        intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
//        intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
//        intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
//        intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
//        intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
//        intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
//        intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
//        intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
//        intent.putExtra("FEEDER_NAME", FEEDER_NAME);
//        intent.putExtra("Model", purposeModel);
//        intent.putExtra("IS_EDIT", true);
//        intent.putExtra("DATA_MODEL", model);
//
//        startActivity(intent);

    }

    private void editSourceLocation(NewModel model) {
        if (FROM.equalsIgnoreCase("33KV")) {
            //NEW 33KV LINE EXTENSION
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;


            // System.out.println("33KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("33KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("33KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            WorkInvolmentsActivity.IS_33_KV_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "33KV";
            Intent intent = new Intent(this, EditPoleDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "SOURCE_POLE");


            startActivity(intent);
        } else if (FROM.equalsIgnoreCase("11KV")) {
            WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
            WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";

            // System.out.println("11KV CLICKED FEEDER : " + FEEDER_NO);
            //  System.out.println("11KV CLICKED FEEDER ID : " + FEEDER_ID);
            //  System.out.println("11KV CLICKED FEEDER NAME : " + FEEDER_NAME);

            Intent intent = new Intent(this, EditPoleDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", FEEDER_NAME);
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "SOURCE_POLE");
            startActivity(intent);
        } else {
            WorkInvolmentsActivity.IS_LT_SELECTED = true;
            WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";


            System.out.println("BABJI_RAISED_ISSUE_LT-->" + WorkInvolmentsActivity.SELECTED_LT_CKT);
            System.out.println("BABJI_RAISED_ISSUE_LT-->" + model.getLT_AVAILABLE());
            Intent intent = new Intent(this, EditPoleDetailsActivity.class);
            intent.putExtra("EST_NO", EST_NO);
            intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
            intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
            intent.putExtra("GIS_ID", GIS_ID);
            intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
            intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
            intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
            intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
            intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
            intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
            intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
            intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
            intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
            intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
            intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
            intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
            intent.putExtra("FEEDER_NAME", "");
            intent.putExtra("Model", purposeModel);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("DATA_MODEL", model);
            intent.putExtra("TYPE", "SOURCE_POLE");

            startActivity(intent);
        }

    }

    private void editDTRParticulars(NewModel model) {
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            if (model.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole") && (model.getDTR_SURVEY_TYPE().equalsIgnoreCase("Existing")
                    || model.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED") ||
                    model.getDTR_SURVEY_TYPE().equalsIgnoreCase("Enhancement"))) {
                Intent intent = new Intent(this, EditDtr.class);
                intent.putExtra("DATA_MODEL", model);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, EditProposedDTRActivity.class);
                intent.putExtra("DATA_MODEL", model);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, EditDtr.class);
            intent.putExtra("DATA_MODEL", model);
            startActivity(intent);
        }


    }


    @Override
    public void onItemClick(int position, NewModel model) {
        if (FROM.equalsIgnoreCase("33KV")) {
            showCustomEditDialog(model);
        }
        if (FROM.equalsIgnoreCase("LT")) {
            showCustomEditDialog(model);


        }
        if (FROM.equalsIgnoreCase("11KV")) {
            showCustomEditDialog(model);

        }
    }

    private void showDialogBoxDTR(NewModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_box_for_dtr_edit, null);
        builder.setView(dialogView);

        RadioGroup radioGroup = dialogView.findViewById(R.id.rg_edit_dtr_type);
        RadioButton onlyDtr = dialogView.findViewById(R.id.rg_only_dtr);
        RadioButton nonDTR = dialogView.findViewById(R.id.rg_non_dtr);
        Button btnApply = dialogView.findViewById(R.id.btn_apply);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        btnApply.setOnClickListener(v -> {
            if (onlyDtr.isChecked()) {
                Intent intent = new Intent(this, EditDtr.class);
                intent.putExtra("DATA_MODEL", model);
                startActivity(intent);
            } else if (nonDTR.isChecked()) {
                WorkInvolmentsActivity.CLICKED_FEEDER = FEEDER_NO;
                WorkInvolmentsActivity.CLICKED_FEEDER_ID = FEEDER_ID;
                WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "11KV";

                // System.out.println("11KV CLICKED FEEDER : " + FEEDER_NO);
                //  System.out.println("11KV CLICKED FEEDER ID : " + FEEDER_ID);
                //  System.out.println("11KV CLICKED FEEDER NAME : " + FEEDER_NAME);

                Intent intent = new Intent(this, EstimatesMap.class);
                intent.putExtra("EST_NO", EST_NO);
                intent.putExtra("EST_NATURE", getIntent().getStringExtra("EST_NATURE"));
                intent.putExtra("PURPOSE", getIntent().getStringExtra("PURPOSE"));
                intent.putExtra("GIS_ID", GIS_ID);
                intent.putExtra("WORK_ORDER", getIntent().getStringExtra("WORK_ORDER"));
                intent.putExtra("33KV", getIntent().getBooleanExtra("33KV", false));
                intent.putExtra("11KV", getIntent().getBooleanExtra("11KV", false));
                intent.putExtra("DTR", getIntent().getBooleanExtra("DTR", false));
                intent.putExtra("LT", getIntent().getBooleanExtra("LT", false));
                intent.putExtra("METERS", getIntent().getBooleanExtra("METERS", false));
                intent.putExtra("MAX_LENGTH", getIntent().getStringExtra("MAX_LENGTH"));
                intent.putExtra("REG_COUNT", getIntent().getStringExtra("REG_COUNT"));
                intent.putExtra("BTN_VALUE", getIntent().getStringExtra("BTN_VALUE"));
                intent.putExtra("Feeder_type33KV", getIntent().getStringExtra("Feeder_type33KV"));
                intent.putExtra("Feeder_type11KV", getIntent().getStringExtra("Feeder_type11KV"));
                intent.putExtra("FDR_INDEX_POS", getIntent().getStringExtra("FDR_INDEX_POS"));
                intent.putExtra("FEEDER_NAME", FEEDER_NAME);
                intent.putExtra("Model", purposeModel);
                intent.putExtra("IS_EDIT", true);
                intent.putExtra("DATA_MODEL", model);

                startActivity(intent);
            }
            dialog.dismiss();
        });
    }

}
