package com.apepdcl.gis_survey.RDSS;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys.FeederMasterActivity;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;

import java.util.List;

/**
 * Created by SOUBHAGYA SWAIN on 23-06-2026.
 */
public class OHLineHome extends AppCompatActivity {
    RelativeLayout profile_layout;
    RelativeLayout proposed, assets;
    private SharedPreferences preferences;
    ImageView iv_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oh_line_home);
        getSupportActionBar().hide();
        preferences = PreferenceManager.getDefaultSharedPreferences(OHLineHome.this);

        iv_back = findViewById(R.id.iv_back);
        profile_layout = findViewById(R.id.profile_layout);
        assets = findViewById(R.id.assets);
        proposed = findViewById(R.id.proposed);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        proposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intentPf = new Intent(UGCablesHome.this, FeederMasterRDSSUg11KvActivity.class);
                Intent intentPf = new Intent(OHLineHome.this, FeederMasterActivity.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(OHLineHome.this, HomeActivity.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        /*List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
//        String role = loginDataResponses.get(0).getUSERID();
        String role = loginDataResponses.get(0).getROLE();
        System.out.println("ROLE IN UG CABLES-->: " + role);

        String user_id = preferences.getString(MobileConstants.USERNAME, "");
        if (role.equalsIgnoreCase("UGC")) {
            disableViews(proposed);
            System.out.println("USER_ID:" + user_id);
        } else if (role.equalsIgnoreCase("ADE")) {
            disableViews(assets);
        } else if (role.equalsIgnoreCase("GIS VENDOR")) {
            disableViews(assets);
        }*/


//        if (!role.equalsIgnoreCase("ADE-UGC/CORP")) {
//            assets.setClickable(false);
//            assets.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary1));
//        }

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
