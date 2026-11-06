package com.apepdcl.gis_survey;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.RdssUGDatabase;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.module_33KV.vo.IncrementalSurveyVo;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.DTRSurveyVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.LoginRequestVo;
import com.apepdcl.gis_survey.vo.LoginResponseVo;
import com.apepdcl.gis_survey.vo.SurveyDataVo;

import com.apepdcl.gis.app.R;
import com.google.gson.JsonObject;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    TextView role, section, user_id, name;
    private String NAME = "";
    RelativeLayout help, download_manual, logout, back_layout, close_layout;
    RetrofitServiceEstimates retrofitService;
    private ArcGISDatabase arcGISDatabase;
    ArcGISDatabaseEstimates databaseEstimates;
    PmiDatabase pmiDatabase;
    GifImageView loader;


    private SharedPreferences preferences;
    private List<LoginDataResponse> loginDataResponses;

    boolean dataAvailable = false;
    String dataAvailableText;

    //LT
    private List<DTRSurveyVo> dtrSurveyVoList;
    private List<SurveyDataVo> surveyDataVosList;
    private List<SurveyDataVo> imageListLT;
    int imageCount = 0;

    //11KV
    private List<SurveyVo> surveyVoList11, imageList11;
    int imageCount11 = 0;

    //33KV
    private List<com.apepdcl.gis_survey.module_33KV.vo.SurveyVo> surveyVoList;
    private List<IncrementalSurveyVo> incrementalSurveyVoList;
    private com.apepdcl.gis_survey.module_33KV.ArcGISDatabase db_33KV;
    private ArcGISDatabaseUG33KV databaseUG33KV;
    RdssUGDatabase database;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
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
        loader = findViewById(R.id.loader);


        arcGISDatabase = new ArcGISDatabase(Profile.this);
        databaseEstimates = new ArcGISDatabaseEstimates(Profile.this);
        pmiDatabase = new PmiDatabase(Profile.this);

        preferences = PreferenceManager.getDefaultSharedPreferences(Profile.this);
        loginDataResponses = arcGISDatabase.getUserMasterData();

        user_id.setText(preferences.getString(MobileConstants.USERNAME, ""));
        System.out.println("LOGIN-DETAILS:::::"+loginDataResponses.get(0).getNAME());
        if (loginDataResponses.get(0).getSECTION_NAME().isEmpty() || loginDataResponses.get(0).getSECTION_NAME().equalsIgnoreCase("")){
            role.setText(loginDataResponses.get(0).getNAME());
        }else {
            role.setText(loginDataResponses.get(0).getSECTION_NAME());
        }
        if (loginDataResponses.get(0).getROLE().isEmpty() || loginDataResponses.get(0).getROLE().equalsIgnoreCase("")){
            section.setText(loginDataResponses.get(0).getNAME());
        }else {
            section.setText(loginDataResponses.get(0).getROLE());
        }

        if (loginDataResponses.get(0).getNAME() != null) {
            NAME = loginDataResponses.get(0).getNAME();
        }

        if (loginDataResponses.get(0).getROLE().equalsIgnoreCase("JLMGRADE-II") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("ALM") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("FM-GR-I") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("JLM") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("JLI") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("L.I") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("LM") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("Line Inspector") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("SLI") ||
                loginDataResponses.get(0).getROLE().equalsIgnoreCase("FM")
        ) {
            if (NAME.isEmpty()) {
                name.setVisibility(View.GONE);
            } else {
                name.setVisibility(View.VISIBLE);
                name.setText("Name:" + " " + NAME);
            }


        }

        //LT
        surveyDataVosList = arcGISDatabase.getSurveyData("SELECT * FROM SURVEY_TABLE WHERE SURVEY_FLAG='N'");
        imageListLT = arcGISDatabase.getLTImageData("");
        dtrSurveyVoList = arcGISDatabase.getDTRSurveyData();
        for (int i = 0; i < imageListLT.size(); i++) {
            if (!imageListLT.get(i).getIMAGE_1().equals("N")) {
                imageCount++;
            }
            if (!imageListLT.get(i).getIMAGE_2().equals("N")) {
                imageCount++;
            }
            if (!imageListLT.get(i).getIMAGE_3().equals("N")) {
                imageCount++;
            }
        }

        //11KV
        surveyVoList11 = new com.apepdcl.gis_survey.module_11KV.ArcGISDatabase(this).getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
        imageList11 = new com.apepdcl.gis_survey.module_11KV.ArcGISDatabase(this).getImageData("");
        for (int i = 0; i < imageList11.size(); i++) {
            if (!imageList11.get(i).getIMAGE_1().equals("N")) {
                imageCount11++;
            }
            if (!imageList11.get(i).getIMAGE_2().equals("N")) {
                imageCount11++;
            }
            if (!imageList11.get(i).getIMAGE_3().equals("N")) {
                imageCount11++;
            }
        }

        //33KV
        db_33KV = new com.apepdcl.gis_survey.module_33KV.ArcGISDatabase(this);
        databaseUG33KV = new ArcGISDatabaseUG33KV(this);
        database = new RdssUGDatabase(this);
        surveyVoList = db_33KV.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
        incrementalSurveyVoList = db_33KV.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_stay, R.anim.activity_slide_to_bottom);
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

        if (surveyDataVosList.size() > 0 || dtrSurveyVoList.size() > 0 || imageCount > 0) {
            dataAvailable = true;
            if (surveyDataVosList.size() > 0) {
                dataAvailableText = "If You Log out you will lose " + surveyDataVosList.size() + " Poles of LT Survey.";
            }
            if (dtrSurveyVoList.size() > 0) {
                dataAvailableText = "If You Log out you will lose DTR Survey data.";
            }
            log_info.setText("Please Transmit LT Survey data before Logging out. All surveyed data will be deleted if you log out.");
        } else if (surveyVoList11.size() > 0 || imageCount11 > 0) {
            dataAvailable = true;
            dataAvailableText = "If You Log out you will lose " + surveyVoList11.size() + " Poles of 11KV Survey.";
            log_info.setText("Please Transmit 11KV Survey data before Logging out. All surveyed data will be deleted if you log out.");
        } else if (surveyVoList.size() > 0 || incrementalSurveyVoList.size() > 0) {
            dataAvailable = true;
            dataAvailableText = "If You Log out you will lose " + surveyVoList.size() + " Poles of 33KV Survey.";
            log_info.setText("Please Transmit 33KV Survey data before Logging out. All surveyed data will be deleted if you log out.");
        } else if (databaseEstimates.getCurrentSurveyData().size() > 0) {
            dataAvailable = true;
            dataAvailableText = "If You Log out you will lose " + databaseEstimates.getCurrentSurveyData().size() + " Locations of Estimates.";
            log_info.setText("Please Transmit Estimates data before Logging out. All surveyed data will be deleted if you log out.");
        } else if (pmiDatabase.getCurrentPmiPostingData().size() > 0) {
            dataAvailable = true;
            dataAvailableText = "If You Log out you will lose " + pmiDatabase.getCurrentPmiPostingData().size() + " Locations of PMI.";
            log_info.setText("Please Transmit PMI data before Logging out. All surveyed data will be deleted if you log out.");
        } else {
            log_info.setVisibility(View.GONE);
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
                if (dataAvailable) {
                    openConfirmLogoutDialogBox();
                } else {
                    com.apepdcl.gis_survey.module_11KV.ArcGISDatabase db11kv = new com.apepdcl.gis_survey.module_11KV.ArcGISDatabase(Profile.this);
                    arcGISDatabase.clearData();
                    db11kv.clearData();
                    db_33KV.clearData();
                    databaseUG33KV.clearData();
                    database.clearData();
                    databaseEstimates.deleteAllDataFromDatabase();
                    pmiDatabase.deleteAllPmiData();

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear().commit();
                    Intent intent = new Intent(Profile.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    //userLogout();

                }


            }
        });
    }

    private void openConfirmLogoutDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);
        alertDialogBuilder.setTitle(dataAvailableText);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OKAY",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //userLogout();
                                dialog.cancel();
                                com.apepdcl.gis_survey.module_11KV.ArcGISDatabase db11kv = new com.apepdcl.gis_survey.module_11KV.ArcGISDatabase(Profile.this);
                                arcGISDatabase.clearData();
                                databaseEstimates.clearMaterialTable();
                                db11kv.clearData();
                                db_33KV.clearData();
                                databaseEstimates.deleteAllDataFromDatabase();
                                pmiDatabase.deleteAllPmiData();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear().commit();
                                Intent intent = new Intent(Profile.this, LoginActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void userLogout() {
        loader.setVisibility(View.VISIBLE);
        JsonObject userData = new JsonObject();
        String user_id = preferences.getString(MobileConstants.USERNAME, "");
        userData.addProperty("USER_ID", user_id);
        try {
            retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance("").create(RetrofitServiceEstimates.class);
            Call<LoginResponseVo> responseVoCall = retrofitService.logout(userData);
            responseVoCall.enqueue(new Callback<LoginResponseVo>() {
                @Override
                public void onResponse(Call<LoginResponseVo> call, Response<LoginResponseVo> response) {
                    loader.setVisibility(View.GONE);
                    if (response.body() != null) {
                        LoginResponseVo loginDataResponse = response.body();
                        if ("TRUE".equalsIgnoreCase(loginDataResponse.getSTATUS())) {
                            com.apepdcl.gis_survey.module_11KV.ArcGISDatabase db11kv = new com.apepdcl.gis_survey.module_11KV.ArcGISDatabase(Profile.this);
                            arcGISDatabase.clearData();
                            db11kv.clearData();
                            db_33KV.clearData();
                            databaseUG33KV.clearData();
                            database.clearData();
                            databaseEstimates.deleteAllDataFromDatabase();
                            pmiDatabase.deleteAllPmiData();

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear().commit();
                            Intent intent = new Intent(Profile.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Profile.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<LoginResponseVo> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {

        }
    }
}