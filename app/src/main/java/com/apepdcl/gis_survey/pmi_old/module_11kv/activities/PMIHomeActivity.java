package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis_survey.CommonNames;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.Profile;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.CmpDTRSelectActivity;
import com.apepdcl.gis_survey.activitys.DTRSelectActivity;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.activitys.TransMitSurveyActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIFeederMasterRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.PMIRetrofitService;
import com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.pmi_old.module_11kv.transmit.PMITransmitHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRDetailsResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRSurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederMasterResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederMasterResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalSurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsResponseDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.ArcGISDatabase33KV;
import com.apepdcl.gis_survey.pmi_old.module_33kv.PMIRetrofitClientInstance33kv;
import com.apepdcl.gis_survey.pmi_old.module_33kv.RetrofitService33kv;
import com.apepdcl.gis_survey.pmi_old.module_33kv.activities.NoRecordsFoundScreen33KVPMI;
import com.apepdcl.gis_survey.pmi_old.module_33kv.activities.PMICompletedSurveyActivity33KV;
import com.apepdcl.gis_survey.pmi_old.module_33kv.activities.PMISurveyHome33KV;
import com.apepdcl.gis_survey.pmi_old.module_33kv.activities.PMITransmitDataHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMIFeederDataVo;
import com.apepdcl.gis_survey.runtimeperimissions.AppData;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.runtimeperimissions.Utilites;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMIHomeActivity extends AppCompatActivity {
    ArrayList<SlideModel> slideList;
    GifImageView loader;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;
    TextView home_heading;

    boolean dataAvailable = false;
    String dataAvailableText;


    //LT
    LinearLayout lt_start_survey, lt_transmit_data, lt_view_map;
    private List<PMISurveyDataVo> surveyDataVosList;
    private List<PMISurveyDataVo> imageListLT;
    private List<PMIDTRDetailsResponseVo> dtrDetailsResponseVoList = null;
    private List<PMIDTRSurveyVo> dtrSurveyVoList = null;
    private List<LoginDataResponse> loginDataResponses;
    private PMIArcGISDatabase arcGISDatabase;
    private PMIRetrofitService retrofitService;
    private String userName;
    private SharedPreferences preferences;
    int imageCount = 0;

    //11KV
    LinearLayout el_view_map, el_transmit_data, el_start_survey, el_report;

    private List<PMISurveyVo> surveyVoList11, imageList11;
    int imageCount11 = 0;

    //33KV
    LinearLayout tt_view_map, tt_transmit_data, tt_start_survey, tt_report;
    private List<PMISurveyVo> surveyVoList;
    private List<PMIIncrementalSurveyVo> incrementalSurveyVoList;
    private ArcGISDatabase33KV db_33KV;

    //CKT AVAILABLE
    public static ArrayList<PMILTCktsResponseDataVo> list;
    ArrayList<PMILTCktsResponseDataVo> ltSurveyRequestDataVosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmihome);
        getSupportActionBar().hide();
        //list = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(PMIHomeActivity.this);
        home_heading = findViewById(R.id.home_heading);

        LoginActivity.isJHC = preferences.getBoolean(CommonNames.IS_JHC, false);
        LoginActivity.IS_33KV_USER = preferences.getBoolean(CommonNames.IS_33KV_USER, false);

        if (LoginActivity.isJHC) {
            home_heading.setText("HC PHASE 1");
        }


        ImageSlider imageSlider = findViewById(R.id.image_slider);
        slideList = new ArrayList<>();
        SlideModel m1 = new SlideModel(R.drawable.image1, ScaleTypes.FIT);
        SlideModel m2 = new SlideModel(R.drawable.image2, ScaleTypes.FIT);
        SlideModel m3 = new SlideModel(R.drawable.b1, ScaleTypes.FIT);
        SlideModel m4 = new SlideModel(R.drawable.b2, ScaleTypes.FIT);
        slideList.add(m4);
        slideList.add(m3);
        slideList.add(m1);
        imageSlider.setImageList(slideList);

        lt_start_survey = findViewById(R.id.lt_start_survey);
        lt_transmit_data = findViewById(R.id.lt_transmit_data);
        lt_view_map = findViewById(R.id.lt_view_map);

        el_view_map = findViewById(R.id.el_view_map);
        el_transmit_data = findViewById(R.id.el_transmit_data);
        el_start_survey = findViewById(R.id.el_start_survey);
        el_report = findViewById(R.id.el_report);

        tt_view_map = findViewById(R.id.tt_view_map);
        tt_transmit_data = findViewById(R.id.tt_transmit_data);
        tt_start_survey = findViewById(R.id.tt_start_survey);
        tt_report = findViewById(R.id.tt_report);

        loader = findViewById(R.id.loader);
        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);

        //LT
        preferences = PreferenceManager.getDefaultSharedPreferences(PMIHomeActivity.this);
        userName = preferences.getString(MobileConstants.USERNAME, "");
        arcGISDatabase = new PMIArcGISDatabase(this);
        loginDataResponses = arcGISDatabase.getUserMasterData();
        surveyDataVosList = arcGISDatabase.getSurveyData("SELECT * FROM SURVEY_TABLE WHERE SURVEY_FLAG='N'");
        imageListLT = arcGISDatabase.getLTImageData("");
        dtrDetailsResponseVoList = new ArrayList<>();
        dtrSurveyVoList = new ArrayList<>();
        dtrDetailsResponseVoList = arcGISDatabase.getDTRDetailsData();
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
        surveyVoList11 = new PMI11kvArcGISDatabase(this).getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
        imageList11 = new PMI11kvArcGISDatabase(this).getImageData("");
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
        lt_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(PMIHomeActivity.this)) {
                    if (surveyDataVosList.size() > 0 || dtrSurveyVoList.size() > 0 || imageCount > 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIHomeActivity.this);
                        alertDialogBuilder.setTitle("Please transmit the data before  survey start");
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        })

                                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        getGisltckt();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), MobileConstants.INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });

        lt_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTrans = new Intent(PMIHomeActivity.this, TransMitSurveyActivity.class);
                startActivity(intentTrans);
            }
        });

        lt_view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(PMIHomeActivity.this)) {

                    getCompletedDtrs();
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });

        el_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(PMIHomeActivity.this)) {
                    Intent intent = new Intent(PMIHomeActivity.this, PMIHome11KV.class);
                    startActivity(intent);
                    /*if (Build.VERSION.SDK_INT >= 23) {
                        if (AppData.getInstance().isPermission()) {
                            Intent intent = new Intent(PMIHomeActivity.this, PMIHome11KV.class);
                            startActivity(intent);

                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                Utilites.isStoragePermissionGranted(PMIHomeActivity.this);
                            }
                        }
                    } else {
                        Intent intent = new Intent(PMIHomeActivity.this, PMIHome11KV.class);
                        startActivity(intent);
                    }*/
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });
        el_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PMIHomeActivity.this, PMITransmitHomeActivity.class);
                startActivity(intent);
            }
        });

        el_view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(PMIHomeActivity.this)) {
                    Intent intent = new Intent(PMIHomeActivity.this, PMIFeederKetchHomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });

        el_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(PMIHomeActivity.this)) {
                    Intent intent = new Intent(PMIHomeActivity.this, PMIReport11KV.class);
                    startActivity(intent);
                    /*if (Build.VERSION.SDK_INT >= 23) {
                        if (AppData.getInstance().isPermission()) {
                            Intent intent = new Intent(PMIHomeActivity.this, PMIReport11KV.class);
                            startActivity(intent);

                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                Utilites.isStoragePermissionGranted(PMIHomeActivity.this);
                            }
                        }
                    } else {
                        Intent intent = new Intent(PMIHomeActivity.this, PMIReport11KV.class);
                        startActivity(intent);
                    }*/
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });

        tt_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectivityChecking.checkConnection(PMIHomeActivity.this)) {
                    validate();
                    /*if (Build.VERSION.SDK_INT >= 23) {
                        if (AppData.getInstance().isPermission()) {
                            validate();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                Utilites.isStoragePermissionGranted(PMIHomeActivity.this);

                            }
                        }

                    } else {
                        validate();
                    }*/

                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

            }
        });

        //33KV
        db_33KV = new ArcGISDatabase33KV(this);
        surveyVoList = db_33KV.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
        incrementalSurveyVoList = db_33KV.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");


        tt_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectivityChecking.checkConnection(PMIHomeActivity.this)) {
                    validate();
                    /*if (Build.VERSION.SDK_INT >= 23) {
                        if (AppData.getInstance().isPermission()) {
                            validate();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                Utilites.isStoragePermissionGranted(PMIHomeActivity.this);

                            }
                        }

                    } else {
                        validate();
                    }*/

                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

            }
        });

        tt_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (surveyVoList.size() > 0 || incrementalSurveyVoList.size() > 0) {
                    Intent intentPf = new Intent(PMIHomeActivity.this, PMITransmitDataHomeActivity.class);
                    startActivity(intentPf);
                } else {
                    Toast.makeText(PMIHomeActivity.this, "No data to transmit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tt_view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.IS_33KV_USER) {
                    if (ConnectivityChecking.checkConnection(PMIHomeActivity.this)) {
                        loadFeederDataData();
                    } else {
                        Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIHomeActivity.this);
                    alertDialogBuilder.setTitle("You are not Authorized.");
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OKAY",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
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


            }
        });


        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogoutDialog();
            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(PMIHomeActivity.this, Profile.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });
    }

    public void validate() {
        if (surveyVoList.size() > 0 || incrementalSurveyVoList.size() > 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Please transmit the data before starting survey");
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OKAY",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })

                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            getFeederMasterData();
        }
    }


    public void getFeederMasterData() {
        try {
            loader.setVisibility(View.VISIBLE);
            PMIFeederMasterRequestVo feederMasterRequestVo = new PMIFeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(preferences.getString(MobileConstants.USERNAME, ""));
            feederMasterRequestVo.setSESSION_ID(preferences.getString(MobileConstants.SESSION_ID, ""));
            feederMasterRequestVo.setUSER_ID(loginDataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION_33_KV);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);

            Log.v("DATA===>>", "" + json);
            // retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            RetrofitService33kv rs = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
            Call<PMIFeederMasterResponseVo> responseVoCall = rs.getFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<PMIFeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<PMIFeederMasterResponseVo> call, Response<PMIFeederMasterResponseVo> response) {
                    loader.setVisibility(View.GONE);
                    if (response.body() != null) {
                        PMIFeederMasterResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            db_33KV.clearFeederData();

                            List<PMIFeederMasterResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (PMIFeederMasterResponseDataVo vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                PMIFeederDataVo feederDataVo = new PMIFeederDataVo();
                                feederDataVo.setEHT_SS_CODE(ssCode);
                                feederDataVo.setEHT_SS_NAME(ssName);
                                feederDataVo.setFEEDER_CODE_33KV(feederCode);
                                feederDataVo.setFEEDER_NAME_33KV(feedERName);
                                feederDataVo.setWOID(vo.getWO_ID());
                                feederDataVo.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
                                db_33KV.addFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            Intent intent = new Intent(PMIHomeActivity.this, PMISurveyHome33KV.class);
                            startActivity(intent);
                            // finish();


                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIHomeActivity.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstants.APK_URL);
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            startActivity(intent);
                                                        }
                                                    })

                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Intent intent= new Intent(PMIHomeActivity.this, NoRecordsFoundScreen33KVPMI.class);
                                startActivity(intent);
                                //Toast.makeText(PMIHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(PMIHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMIFeederMasterResponseVo> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    //  progressDialog.dismiss();
                    Toast.makeText(PMIHomeActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            //log_info.setAnimation(AnimationUtils.loadAnimation(HomeActivity.this, R.anim.blink));
        } else if (surveyVoList11.size() > 0 || imageCount11 > 0) {
            dataAvailable = true;
            log_info.setText("Please Transmit 11KV Survey data before Logging out. All surveyed data will be deleted if you log out.");
            dataAvailableText = "If You Log out you will lose " + surveyVoList11.size() + " Poles of 11KV Survey.";
            // log_info.setAnimation(AnimationUtils.loadAnimation(HomeActivity.this, R.anim.blink));
        } else if (surveyVoList.size() > 0 || incrementalSurveyVoList.size() > 0) {
            dataAvailable = true;
            log_info.setText("Please Transmit 33KV Survey data before Logging out. All surveyed data will be deleted if you log out.");
            dataAvailableText = "If You Log out you will lose " + surveyVoList.size() + " Poles of 33KV Survey.";
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
                    PMI11kvArcGISDatabase db11kv = new PMI11kvArcGISDatabase(PMIHomeActivity.this);
                    arcGISDatabase.clearData();
                    db11kv.clearData();
                    db_33KV.clearData();

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear().commit();
                    Intent intent = new Intent(PMIHomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void openConfirmLogoutDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIHomeActivity.this);
        alertDialogBuilder.setTitle(dataAvailableText);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OKAY",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PMI11kvArcGISDatabase db11kv = new PMI11kvArcGISDatabase(PMIHomeActivity.this);
                                arcGISDatabase.clearData();
                                db11kv.clearData();
                                db_33KV.clearData();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear().commit();

                                dialog.cancel();

                                Intent intent = new Intent(PMIHomeActivity.this, LoginActivity.class);
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

    public void loadFeederDataData() {
        try {
            loader.setVisibility(View.VISIBLE);
            PMIFeederMasterRequestVo feederMasterRequestVo = new PMIFeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIHomeActivity.this));
            feederMasterRequestVo.setSESSION_ID(preferences.getString(MobileConstants.SESSION_ID, ""));
            feederMasterRequestVo.setUSER_ID(loginDataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION_33_KV);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);

            Log.v("DATA===>>", "" + json);
            RetrofitService33kv rs = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
            Call<PMIFeederMasterResponseVo> responseVoCall = rs.getCmpFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<PMIFeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<PMIFeederMasterResponseVo> call, Response<PMIFeederMasterResponseVo> response) {
                    loader.setVisibility(View.GONE);
                    if (response.body() != null) {
                        PMIFeederMasterResponseVo response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);

                        Log.v("DATA===>>", "" + json);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            db_33KV.cmpClearFeederData();

                            List<PMIFeederMasterResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (PMIFeederMasterResponseDataVo vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                PMIFeederDataVo feederDataVo = new PMIFeederDataVo();
                                feederDataVo.setEHT_SS_CODE(ssCode);
                                feederDataVo.setEHT_SS_NAME(ssName);
                                feederDataVo.setFEEDER_CODE_33KV(feederCode);
                                feederDataVo.setFEEDER_NAME_33KV(feedERName);
                                db_33KV.addCmpFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                            Intent intent = new Intent(PMIHomeActivity.this, PMICompletedSurveyActivity33KV.class);
                            startActivity(intent);
                            finish();
                           /* Intent intent = new Intent(FeederMasterActivity.this, NewSurveyFeederActivity.class);
                            startActivity(intent);
                            finish();*/


                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIHomeActivity.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Download",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Uri uri = Uri.parse(MobileConstants.APK_URL);
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            startActivity(intent);
                                                        }
                                                    })

                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(PMIHomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(PMIHomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMIFeederMasterResponseVo> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    //  progressDialog.dismiss();
                    Toast.makeText(PMIHomeActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void getCompletedDtrs() {

        loader.setVisibility(View.VISIBLE);
        PMILTCktsReqVo ltCktsReqVo = new PMILTCktsReqVo();
        ltCktsReqVo.setASSIGNED_TO(userName);
        // ltCktsReqVo.setASSIGNED_TO("1078667");
        ltCktsReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIHomeActivity.this));
        ltCktsReqVo.setVERSION(MobileConstants.APP_VERSION);
        ltCktsReqVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());

        Gson gson = new Gson();
        String str = gson.toJson(ltCktsReqVo);
        Log.v("DATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<PMILTCktsResponseVo> responseVoCall = retrofitService.getGisltckt(ltCktsReqVo);
        responseVoCall.enqueue(new Callback<PMILTCktsResponseVo>() {
            @Override
            public void onResponse(Call<PMILTCktsResponseVo> call, Response<PMILTCktsResponseVo> response) {
                loader.setVisibility(View.GONE);
                if (response.body() != null) {
                    PMILTCktsResponseVo ltCktsResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(ltCktsResponseVo.getSTATUS())) {
                        arcGISDatabase.clearCmpDTRData();
                        List<PMILTCktsResponseDataVo> ltSurveyRequestDataVosList = ltCktsResponseVo.getRESPONSE();
                        for (PMILTCktsResponseDataVo surveyRequestDataVo : ltSurveyRequestDataVosList) {
                            arcGISDatabase.addCmpDTRData(surveyRequestDataVo);
                        }
                        Intent intent = new Intent(PMIHomeActivity.this, CmpDTRSelectActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(PMIHomeActivity.this, ltCktsResponseVo.getERR_MSG().toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIHomeActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMILTCktsResponseVo> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(PMIHomeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });
    }

    public void getGisltckt() {
        loader.setVisibility(View.VISIBLE);
        PMILTCktsReqVo ltCktsReqVo = new PMILTCktsReqVo();
        ltCktsReqVo.setASSIGNED_TO(userName);
        // ltCktsReqVo.setASSIGNED_TO("1078667");
        ltCktsReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(PMIHomeActivity.this));
        ltCktsReqVo.setVERSION(MobileConstants.APP_VERSION);
        ltCktsReqVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());

        Gson gson = new Gson();
        String str = gson.toJson(ltCktsReqVo);
        Log.v("DATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(PMIRetrofitService.class);
        Call<PMILTCktsResponseVo> responseVoCall = retrofitService.getGisltckt(ltCktsReqVo);
        responseVoCall.enqueue(new Callback<PMILTCktsResponseVo>() {
            @Override
            public void onResponse(Call<PMILTCktsResponseVo> call, Response<PMILTCktsResponseVo> response) {
                loader.setVisibility(View.GONE);
                if (response.body() != null) {
                    PMILTCktsResponseVo ltCktsResponseVo = response.body();

                 /*   Gson gson=new Gson();
                    String str=gson.toJson(ltCktsResponseVo);
                    Log.v("GSL===>>", "" + str);*/
                    if (MobileConstants.TRUE.equalsIgnoreCase(ltCktsResponseVo.getSTATUS())) {
                        //OLD
                        arcGISDatabase.clearDTRData();
                        List<PMILTCktsResponseDataVo> ltSurveyRequestDataVosList = ltCktsResponseVo.getRESPONSE();
                        list = new ArrayList<>(ltCktsResponseVo.getRESPONSE());
                        for (PMILTCktsResponseDataVo surveyRequestDataVo : ltSurveyRequestDataVosList) {
                            System.out.println("GIS LTCKT RESPONSE : " + surveyRequestDataVo.getLT_CKTCODE());
                            arcGISDatabase.addDTRData(surveyRequestDataVo);
                        }
                        Intent intent = new Intent(PMIHomeActivity.this, DTRSelectActivity.class);
                        startActivity(intent);

                        //NEW
                       /* Gson gson=new Gson();
                        String str=gson.toJson(ltCktsResponseVo);
                        Log.v("GSL===>>", "" + str);
                        arcGISDatabase.clearDTRData();
                        list = new ArrayList<>();
                        ltSurveyRequestDataVosList = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(str);
                            JSONArray array = new JSONArray(object.getString("RESPONSE"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                LTCktsResponseDataVo surveyRequestDataVo = new LTCktsResponseDataVo();
                                surveyRequestDataVo.setFEEDER_ID(jsonObject.getString("FEEDER_ID"));
                                surveyRequestDataVo.setDTR_CODE(jsonObject.getString("DTR_CODE"));
                                surveyRequestDataVo.setDTR_PHASE(jsonObject.getString("DTR_PHASE"));
                                surveyRequestDataVo.setLT_CKTCODE(jsonObject.getString("LT_CKT_CODE"));
                                surveyRequestDataVo.setDTR_LOCATION(jsonObject.getString("DTR_LOCATION"));
                                surveyRequestDataVo.setIS_LTCKT_AVAILABLE(jsonObject.getString("IS_LTCKT_AVAILABLE"));
                                System.out.println("GIS LTCKT RESPONSE : "+jsonObject.getString("LT_CKT_CODE"));
                                arcGISDatabase.addDTRData(surveyRequestDataVo);
                                ltSurveyRequestDataVosList.add(surveyRequestDataVo);

                            }

                            Intent intent=new Intent(HomeActivity.this, DTRSelectActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                    } else {

                        if (ltCktsResponseVo.getAPK() != null) {
                            if (ltCktsResponseVo.getAPK().contains("http")) {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PMIHomeActivity.this);

                                alertDialogBuilder.setTitle("A new Version is available!");
                                //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                alertDialogBuilder

                                        .setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                                                "Please Download latest app,<a href=\"https://gisappstore.apeasternpower.com/GISSurveyLT.apk\"></a>"))
                                        //.setMessage("Before upgrade,please trasmit the data to server ")
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

                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        } else {
                            Toast.makeText(PMIHomeActivity.this, ltCktsResponseVo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                        }
                        //Toast.makeText(LTSurveyHomeActivity.this,ltCktsResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PMIHomeActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMILTCktsResponseVo> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(PMIHomeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });
    }


}