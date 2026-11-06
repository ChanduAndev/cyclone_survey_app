package com.apepdcl.gis_survey.estimates.sou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.LtSurveyAdapter;
import com.apepdcl.gis_survey.estimates.OnlyLtSurveyAdapter;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.models.FdrParticularsModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class OfflineLTActivity extends AppCompatActivity {
    ArcGISDatabaseEstimates databaseEstimates;
    private RecyclerView rv_lt_ckt, rv_lt_ckt_proposed;
    ArrayList<NewModel> totalPoles33;
    ArrayList<NewModel> totalPoles11;
    ArrayList<NewModel> totalPolesLT;
    String EST_NO = "", GIS_ID = "", EST_NATURE = "", PURPOSE = "", WORK_ORDER = "", max_length = "", REG_COUNT = "", from = "";
    private boolean is33KVSelected = false;
    private boolean is11KVSelected = false;
    private boolean isDTRSelected = false;
    private boolean isLTSelected = false;
    private boolean isMeters = false;
    public static boolean OFFLINE_LT = false;
    private PurposeModel model;
    public static boolean IS_OFFLINE = false;

    private RelativeLayout back_layout, close_layout;

    private TextView tv_error_msg_dtr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_ltactivity);

        rv_lt_ckt = findViewById(R.id.rv_lt_ckt);
        tv_error_msg_dtr = findViewById(R.id.tv_error_msg_dtr);
        rv_lt_ckt_proposed = findViewById(R.id.rv_lt_ckt_proposed);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        close_layout.setOnClickListener(v -> onBackPressed());

        back_layout.setOnClickListener(v -> onBackPressed());
        EST_NO = getIntent().getStringExtra("EST_NO");
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        model = (PurposeModel) getIntent().getSerializableExtra("Model");
        from = getIntent().getStringExtra("FROM");
        databaseEstimates = new ArcGISDatabaseEstimates(this);
        IS_OFFLINE = true;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(model);
        Log.d("OFFLINE_LT_MODEL", "onCreate: " + json);

        System.out.println("HHAHHAHHAHHAHA" + WorkInvolmentsActivity.CURRENT_SURVEY_TYPE);

        is33KVSelected = getIntent().getBooleanExtra("33KV", false);
        is11KVSelected = getIntent().getBooleanExtra("11KV", false);
        isDTRSelected = getIntent().getBooleanExtra("DTR", false);
        isLTSelected = getIntent().getBooleanExtra("LT", false);
        isMeters = getIntent().getBooleanExtra("METERS", false);

        //NEW BY SOUBHAGYA START
        totalPoles33 = databaseEstimates.getNewSurveyData(EST_NO, "33KV", false, GIS_ID);
        totalPoles11 = databaseEstimates.getNewSurveyData(EST_NO, "11KV", false, GIS_ID);
        totalPolesLT = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);


        if (from.equals("OFFLINE_LT_WITH_11KV")) {
            System.out.println("HAHAHAHAHAHHA---->");
            boolean isMatched = false;
            for (int i = 0; i < totalPoles11.size(); i++) {
                if (totalPoles11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR POLE") &&
                        totalPoles11.get(i).getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed") &&
                        totalPoles11.get(i).getLT_AVAILABLE().equalsIgnoreCase("Yes")
                ) {
                    System.out.println("HAHAHAHAHAHHA---->"+isMatched);
                    isMatched = true;
                }
            }
            if (isMatched) {
                System.out.println("HAHAHAHAHAHHA---->111"+isMatched);
                setLTWith11kvAdapter();
                setOnlyLTAdapter();
                rv_lt_ckt_proposed.setVisibility(View.VISIBLE);
                tv_error_msg_dtr.setVisibility(View.GONE);
            } else {
                System.out.println("HAHAHAHAHAHHA---->222"+isMatched);
                rv_lt_ckt_proposed.setVisibility(View.GONE);
                tv_error_msg_dtr.setVisibility(View.VISIBLE);
            }

        } else if (from.equals("OFFLINE_ONLY_LT")) {
            System.out.println("HAHAHAHAHAHHA----> OFFLINE_ONLY_LT");
            rv_lt_ckt.setVisibility(View.VISIBLE);
            setOnlyLTAdapter();
        }


    }

    private void setOnlyLTAdapter() {
        rv_lt_ckt.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OfflineLTActivity.this);
        rv_lt_ckt.setLayoutManager(layoutManager);
        rv_lt_ckt.setFocusable(false);

        List<FdrParticularsModel> list = new ArrayList<>();
        for (int i = 0; i < model.getFDR_PARTICULARS().size(); i++) {
            if (model.getFDR_PARTICULARS().get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                list.add(model.getFDR_PARTICULARS().get(i));
            }
        }
        OnlyLtSurveyAdapter adapter = new OnlyLtSurveyAdapter(EST_NO, GIS_ID, list, OfflineLTActivity.this, new OnlyLtSurveyAdapter.OnlyOnItemClickListener() {
            @Override
            public void onItemClick(FdrParticularsModel model) {
                WorkInvolmentsActivity.IS_LT_SELECTED = true;
                WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
                WorkInvolmentsActivity.SELECTED_DTR = model.getFEEDER().split("~")[0];
                WorkInvolmentsActivity.SELECTED_DTR_LOCATION = model.getFEEDER().split("~")[1];
                WorkInvolmentsActivity.SELECTED_LT_CKT = model.getLT_CKT_CODE();
                System.out.println("SELECTED_DTR ONLY LT" + WorkInvolmentsActivity.SELECTED_DTR);
                System.out.println("SELECTED_LT_CKT ONLY LT" + WorkInvolmentsActivity.SELECTED_LT_CKT);
                int polesCount = getPolesCount(WorkInvolmentsActivity.SELECTED_LT_CKT);
                if (polesCount == 0) {
                    WorkInvolmentsActivity.IS_LT_FIRST_POLE = true;
                } else {
                    WorkInvolmentsActivity.IS_LT_FIRST_POLE = false;
                }

                if (model.getSURVEY_TYPE().equalsIgnoreCase("PROPOSED")) {
                    WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY = false;
                } else {
                    WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY = true;
                }

                // WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY = true;
                OFFLINE_LT = true;


                int LT_CKT_POLE_COUNT = 0;

                System.out.println("existingPolesList size: " + WorkInvolmentsActivity.existingPolesList.size());

                if (!WorkInvolmentsActivity.existingPolesList.isEmpty()) {
                    for (NewModel pole : WorkInvolmentsActivity.existingPolesList) {
                        String ltAvailable = pole.getLT_AVAILABLE();
                        if (ltAvailable != null && !ltAvailable.isEmpty() && ltAvailable.equals(WorkInvolmentsActivity.SELECTED_LT_CKT)) {
                            LT_CKT_POLE_COUNT++;
                        }
                    }
                }

                if (LT_CKT_POLE_COUNT > 0) {
                    WorkInvolmentsActivity.ONLY_LT_WITH_PROPOSED = false;
                } else {
                    WorkInvolmentsActivity.ONLY_LT_WITH_PROPOSED = true;
                }

                onBackPressed();

            }

            @Override
            public void onEditItemClick(FdrParticularsModel model) {

            }
        });
        rv_lt_ckt.setAdapter(adapter);
    }


    private void setLTWith11kvAdapter() {
        ArrayList<NewModel> dtrList = new ArrayList<>();
        for (int i = 0; i < totalPoles11.size(); i++) {
            if (totalPoles11.get(i).getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                rv_lt_ckt_proposed.setVisibility(View.VISIBLE);
                if (totalPoles11.get(i).getLT_AVAILABLE().equalsIgnoreCase("Yes") && totalPoles11.get(i).getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed")) {
                    dtrList.add(totalPoles11.get(i));
                }
            } else {
                rv_lt_ckt_proposed.setVisibility(View.VISIBLE);
            }
        }

        System.out.println("DTR_LIST_CKT:" + dtrList);
        rv_lt_ckt_proposed.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OfflineLTActivity.this);
        rv_lt_ckt_proposed.setLayoutManager(layoutManager);
        rv_lt_ckt_proposed.setFocusable(false);
        LtSurveyAdapter adapter = new LtSurveyAdapter(dtrList, OfflineLTActivity.this, new LtSurveyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewModel model, String selectedCKT) {
                if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
                    if (model.getDTR_SURVEY_TYPE().equalsIgnoreCase("Existing") || model.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED")) {
                        // getExistingPoleDataNewForLT(model, model.getDTR_STRUCTURE_CODE(), selectedCKT);
                    } else {
                        System.out.println("SELECTED_DTR " + WorkInvolmentsActivity.SELECTED_DTR);
                        System.out.println("SELECTED_LT_CKT " + WorkInvolmentsActivity.SELECTED_LT_CKT);
                        WorkInvolmentsActivity.IS_LT_SELECTED = true;
                        WorkInvolmentsActivity.CURRENT_SURVEY_TYPE = "LT";
                        WorkInvolmentsActivity.SELECTED_DTR = model.getDTR_STRUCTURE_CODE();
                        WorkInvolmentsActivity.SELECTED_DTR_LOCATION = model.getDTR_LOCATION();
                        WorkInvolmentsActivity.SELECTED_LT_CKT = selectedCKT;
                        System.out.println("SELECTED_LT_CKT " + WorkInvolmentsActivity.SELECTED_LT_CKT);
                        WorkInvolmentsActivity.IS_LT_FIRST_POLE = false;
                        WorkInvolmentsActivity.IS_LT_EXISTING_SURVEY = false;
                        WorkInvolmentsActivity.SELECTED_LT_CKT = selectedCKT;
                        OFFLINE_LT = true;
                        onBackPressed();
                        // navigateNextScreen(is33KVSelected, is11KVSelected, isLTSelected, isMeters, "LT", 0, "");
                    }
                } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")) {
                    //getExistingPoleDataNewForLT(model, model.getDTR_STRUCTURE_CODE(), selectedCKT);
                } else {
                    //
                }
            }

            @Override
            public void onEditItemClick(NewModel model, String selectedCKT) {

            }
        });
        rv_lt_ckt_proposed.setAdapter(adapter);
    }

    private int getPolesCount(String ltCktCode) {
        //  Log.d("Debug", "Selected LT CKT " + ltCktCode);
        int totalPolesLT = 0;
        ArrayList<NewModel> allPoles = databaseEstimates.getNewSurveyData(EST_NO, "LT", false, GIS_ID);


        for (int i = 0; i < allPoles.size(); i++) {
            // Log.d("Debug", "Selected LT CKT " + ltCktCode);
            if (allPoles.get(i).getLT_AVAILABLE().equalsIgnoreCase(ltCktCode)) {
                totalPolesLT++;
            }
        }

        return totalPolesLT;
    }


}