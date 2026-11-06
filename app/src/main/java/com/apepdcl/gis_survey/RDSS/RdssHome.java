package com.apepdcl.gis_survey.RDSS;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys.FeederMasterActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;

import java.util.List;

/**
 * Created by SOUBHAGYA SWAIN on 19-05-2025.
 */
public class RdssHome extends AppCompatActivity {

    RelativeLayout logout_layout,profile_layout,layout_solar,layout_work_order_rdss;
    RelativeLayout ug_cables,oh_cables;
    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdss_home_new_ui);
        getSupportActionBar().hide();
        preferences = PreferenceManager.getDefaultSharedPreferences(RdssHome.this);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);
        oh_cables = findViewById(R.id.oh_cables);
        ug_cables = findViewById(R.id.ug_cables);
        layout_solar = findViewById(R.id.layout_solar);
        layout_work_order_rdss = findViewById(R.id.layout_work_order_rdss);




        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ug_cables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(RdssHome.this, UGCablesHome.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });
        layout_solar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.IS_33KV_USER) {
                    if (ConnectivityChecking.checkConnection(RdssHome.this)) {
                        LoginActivity.SURVEY_FROM = "SOLAR_DPR";
                        //   Intent intent = new Intent(DashBoard.this, EstimatesDashboardActivity.class);
                        Intent intent = new Intent(RdssHome.this, SurveyList.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    } else {
                        CommanUtilits.internetDialog(RdssHome.this);
                        //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }
                } else {
                    LoginActivity.SURVEY_FROM = "SOLAR_DPR";
                    //  Intent intent = new Intent(DashBoard.this, EstimatesDashboardActivity.class);
                    Intent intent = new Intent(RdssHome.this, SurveyList.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);

                }
            }
        });

        layout_work_order_rdss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(RdssHome.this)) {
                    LoginActivity.SURVEY_FROM = "WO_RDSS";
                    Intent intent = new Intent(RdssHome.this, SurveyList.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                } else {
                    CommanUtilits.internetDialog(RdssHome.this);
                    //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });

        oh_cables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(RdssHome.this, OHLineHome.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        String user_id = preferences.getString(MobileConstants.USERNAME, "");
        String role = loginDataResponses.get(0).getROLE();

        System.out.println("ROLE_OF_USER"+user_id);

        switch (role.toUpperCase()) {
            case "ADE":
                System.out.println("USER_ID:" + user_id);
                disableViews(oh_cables);
                break;

            case "UGC":
                disableViews(oh_cables);
                break;

//            case "GIS VENDOR":
//                disableViews(ug_cables);
//                break;

        }
    }

    private void disableViews(View... views) {
        for (View view : views) {
            view.setClickable(false);
            view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_view));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_to_bottom);
    }
}
