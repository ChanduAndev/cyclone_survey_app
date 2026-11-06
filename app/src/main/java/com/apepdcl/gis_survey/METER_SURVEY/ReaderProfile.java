package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Profile;
import com.apepdcl.gis_survey.activitys.LoginActivity;

/**
 * Created by SOUBHAGYA SWAIN on 15-06-2026.
 */
public class ReaderProfile extends AppCompatActivity {

    TextView role, section, user_id, name;
    private String NAME = "";
    RelativeLayout help, download_manual, logout, back_layout, close_layout;
    private SharedPreferences preferences;
    MeterSurveyDatabase meterSurveyDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_profile);
        getSupportActionBar().hide();

        role = findViewById(R.id.role);
        name = findViewById(R.id.name);
        section = findViewById(R.id.section);
        user_id = findViewById(R.id.user_id);
        help = findViewById(R.id.help);
        download_manual = findViewById(R.id.download_manual);
        logout = findViewById(R.id.logout);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        meterSurveyDatabase = new MeterSurveyDatabase(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(ReaderProfile.this);

        user_id.setText(preferences.getString(MeterConstants.METER_USER_ID,""));
        name.setText(preferences.getString(MeterConstants.METER_SECTION_NAME,""));
        role.setText(preferences.getString(MeterConstants.METER_OFFICE_ID,""));

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

        download_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Uri webpage = Uri.parse(RetrofitClientInstance.web_url_help +"34344.pdf");
                Uri webpage = Uri.parse("https://drive.google.com/drive/folders/15jpEqzaFnFUA6hzjsoYzujtvIp1lQlia");
                Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(myIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogoutDialog();
            }
        });
    }

    private void openLogoutDialog() {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.log_dialog);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView log_info = payDialog.findViewById(R.id.log_info);

        if (!meterSurveyDatabase.getAllMeterSurveyData().isEmpty()){
           // int totalPoleCount=meterSurveyDatabase.getPoleCountData().size();
            log_info.setText("Please Transmit Meter Survey data before Logging Out. All Surveyed data will be deleted if you logout ");
        }


        payDialog.show();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
                MeterSurveyDatabase database = new MeterSurveyDatabase(ReaderProfile.this);
                database.deleteAllPostingData();
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().commit();
                Intent intent = new Intent(ReaderProfile.this, LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });
    }
}
