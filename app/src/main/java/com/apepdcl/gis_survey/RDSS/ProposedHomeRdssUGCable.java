package com.apepdcl.gis_survey.RDSS;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity.FeederMasterRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.FeederMasterActivityUG33KV;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import com.apepdcl.gis.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SOUBHAGYA SWAIN on 23-05-2025.
 */
public class ProposedHomeRdssUGCable extends AppCompatActivity {

    RelativeLayout logout_layout, profile_layout;
    RelativeLayout survey_11_kv, survey_33_kv;
    ArrayList<SlideModel> slideList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposed_home_rdss_ug_cable);
        getSupportActionBar().hide();
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);

        survey_11_kv = findViewById(R.id.survey_11_kv);
        survey_33_kv = findViewById(R.id.survey_33_kv);

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        slideList = new ArrayList<>();

        if (DashBoard.bannerList != null && !DashBoard.bannerList.isEmpty()) {
            for (int i = 0; i < DashBoard.bannerList.size(); i++) {
                SlideModel m1 = new SlideModel(DashBoard.bannerList.get(i).getIMAGE_URL(), ScaleTypes.FIT);
                slideList.add(m1);
            }
        } else {
            //Log.e("BannerList", "bannerList is NULL or EMPTY");
        }


        /*SlideModel m1 = new SlideModel(R.drawable.image1,ScaleTypes.FIT);
        SlideModel m2 = new SlideModel(R.drawable.image2,ScaleTypes.FIT);
        SlideModel m3 = new SlideModel(R.drawable.b1,ScaleTypes.FIT);
        SlideModel m4 = new SlideModel(R.drawable.b2,ScaleTypes.FIT);
        slideList.add(m4);
        slideList.add(m3);
        slideList.add(m1);*/
        imageSlider.setImageList(slideList);

        survey_11_kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPf = new Intent(ProposedHomeRdssUGCable.this, FeederMasterRDSSUg11KvActivity.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        survey_33_kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPf = new Intent(ProposedHomeRdssUGCable.this, FeederMasterActivityUG33KV.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });


        logout_layout.setOnClickListener(new View.OnClickListener() {
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

        String role = loginDataResponses.get(0).getROLE();



        switch (role.toUpperCase()) {
            case "ADE":
                disableViews(survey_11_kv);
                break;


            case "GIS VENDOR":
                disableViews(survey_33_kv);
                break;

        }

    }

    private void disableViews(View... views) {
        for (View view : views) {
            view.setClickable(false);
            view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary1));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_to_bottom);
    }
}
