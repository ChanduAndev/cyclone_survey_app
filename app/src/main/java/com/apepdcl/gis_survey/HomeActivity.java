package com.apepdcl.gis_survey;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.activitys.CmpDTRSelectActivity;
import com.apepdcl.gis_survey.activitys.DTRSelectActivity;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.activitys.TransMitSurveyActivity;
import com.apepdcl.gis_survey.estimates.models.DialogDataModel;
import com.apepdcl.gis_survey.module_11KV.Home11KV;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.module_11KV.transmit.TransmitHomeActivity;
import com.apepdcl.gis_survey.module_11KV.view_feeder_sketch.FeederKetchHomeActivity;
import com.apepdcl.gis_survey.module_33KV.CompletedSurveyActivity33KV;
import com.apepdcl.gis_survey.module_33KV.SurveyHome33KV;
import com.apepdcl.gis_survey.module_33KV.TransmitDataHomeActivity;
import com.apepdcl.gis_survey.module_33KV.vo.FeederDataVo;
import com.apepdcl.gis_survey.module_33KV.vo.FeederMasterRequestVo;
import com.apepdcl.gis_survey.module_33KV.vo.FeederMasterResponseDataVo;
import com.apepdcl.gis_survey.module_33KV.vo.FeederMasterResponseVo;
import com.apepdcl.gis_survey.module_33KV.vo.IncrementalSurveyVo;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.CustomDialog;
import com.apepdcl.gis_survey.vo.DTRDetailsResponseVo;
import com.apepdcl.gis_survey.vo.DTRSurveyVo;
import com.apepdcl.gis_survey.vo.LTCktsReqVo;
import com.apepdcl.gis_survey.vo.LTCktsResponseDataVo;
import com.apepdcl.gis_survey.vo.LTCktsResponseVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;

import com.apepdcl.gis.app.R;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    ArrayList<SlideModel> slideList;
    GifImageView loader;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;
    TextView home_heading;

    boolean dataAvailable = false;
    String dataAvailableText;


    //LT
    LinearLayout lt_start_survey, lt_transmit_data, lt_view_map;
    private List<SurveyDataVo> surveyDataVosList, surveyVoList_LAT_LAN, surveyVoListEditPoleDetails, surveyVoList_Source_pole;
    private List<SurveyDataVo> imageListLT;
    private List<DTRDetailsResponseVo> dtrDetailsResponseVoList = null;
    private List<DTRSurveyVo> dtrSurveyVoList = null;
    private List<LoginDataResponse> loginDataResponses;
    private ArcGISDatabase arcGISDatabase;
    private RetrofitService retrofitService;
    private String userName;
    private SharedPreferences preferences;
    int imageCount = 0;

    //11KV
    LinearLayout el_view_map, el_transmit_data, el_start_survey;

    private List<SurveyVo> surveyVoList11, imageList11;
    int imageCount11 = 0;

    //33KV
    LinearLayout tt_view_map, tt_transmit_data, tt_start_survey;
    private List<com.apepdcl.gis_survey.module_33KV.vo.SurveyVo> surveyVoList,surveyVosEditLatLan33,surveyVosEditSourcePole33,surveyVosEditPoleDetails33;
    private List<IncrementalSurveyVo> incrementalSurveyVoList;
    private com.apepdcl.gis_survey.module_33KV.ArcGISDatabase db_33KV;

    //CKT AVAILABLE
    public static ArrayList<LTCktsResponseDataVo> list;
    ArrayList<LTCktsResponseDataVo> ltSurveyRequestDataVosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        //list = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        home_heading = findViewById(R.id.home_heading);

        LoginActivity.isJHC = preferences.getBoolean(CommonNames.IS_JHC, false);
        LoginActivity.IS_33KV_USER = preferences.getBoolean(CommonNames.IS_33KV_USER, false);

        if (LoginActivity.isJHC) {
            home_heading.setText("HC PHASE 1");
        }


        ImageSlider imageSlider = findViewById(R.id.image_slider);
        slideList = new ArrayList<>();

        if (DashBoard.bannerList != null && !DashBoard.bannerList.isEmpty()) {
            for (int i = 0; i < DashBoard.bannerList.size(); i++) {
                SlideModel m1 = new SlideModel(DashBoard.bannerList.get(i).getIMAGE_URL(), ScaleTypes.FIT);
                slideList.add(m1);
            }
        } else {
            Log.e("BannerError", "bannerList is null or empty");
        }

//        for (int i = 0; i < DashBoard.bannerList.size(); i++) {
//            SlideModel m1 = new SlideModel(DashBoard.bannerList.get(i).getIMAGE_URL(), ScaleTypes.FIT);
//            slideList.add(m1);
//
//        }

        /*SlideModel m1 = new SlideModel(R.drawable.image1,ScaleTypes.FIT);
        SlideModel m2 = new SlideModel(R.drawable.image2,ScaleTypes.FIT);
        SlideModel m3 = new SlideModel(R.drawable.b1,ScaleTypes.FIT);
        SlideModel m4 = new SlideModel(R.drawable.b2,ScaleTypes.FIT);
        slideList.add(m4);
        slideList.add(m3);
        slideList.add(m1);*/
        imageSlider.setImageList(slideList);

        lt_start_survey = findViewById(R.id.lt_start_survey);
        lt_transmit_data = findViewById(R.id.lt_transmit_data);
        lt_view_map = findViewById(R.id.lt_view_map);

        el_view_map = findViewById(R.id.el_view_map);
        el_transmit_data = findViewById(R.id.el_transmit_data);
        el_start_survey = findViewById(R.id.el_start_survey);

        tt_view_map = findViewById(R.id.tt_view_map);
        tt_transmit_data = findViewById(R.id.tt_transmit_data);
        tt_start_survey = findViewById(R.id.tt_start_survey);

        loader = findViewById(R.id.loader);
        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);

        //LT
        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        userName = preferences.getString(MobileConstants.USERNAME, "");
        arcGISDatabase = new ArcGISDatabase(this);
        loginDataResponses = arcGISDatabase.getUserMasterData();
        surveyVoList_LAT_LAN = arcGISDatabase.getAllSurveyDataLatLan();
        surveyVoListEditPoleDetails = arcGISDatabase.getEditPoleSurveyData();
        surveyVoList_Source_pole = arcGISDatabase.getEditSourcePoleSurveyData();
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


        lt_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(HomeActivity.this)) {
                    if (surveyDataVosList.size() > 0 || dtrSurveyVoList.size() > 0 || imageCount > 0 || surveyVoList_LAT_LAN.size() > 0 || surveyVoListEditPoleDetails.size() > 0 || surveyVoList_Source_pole.size() > 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
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
                    Toast.makeText(HomeActivity.this, MobileConstants.INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });

        lt_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTrans = new Intent(HomeActivity.this, TransMitSurveyActivity.class);
                startActivity(intentTrans);
            }
        });

        lt_view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(HomeActivity.this)) {
                    getCompletedDtrs();
                } else {
                    CommanUtilits.internetDialog(HomeActivity.this);
                    // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });

        el_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(HomeActivity.this)) {
                    Intent intent = new Intent(HomeActivity.this, Home11KV.class);
                    startActivity(intent);
                    /*if (Build.VERSION.SDK_INT >= 23) {
                        if (AppData.getInstance().isPermission()) {
                            Intent intent = new Intent(HomeActivity.this, Home11KV.class);
                            startActivity(intent);

                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                Utilites.isStoragePermissionGranted(HomeActivity.this);
                            }
                        }
                    } else {
                        Intent intent = new Intent(HomeActivity.this, Home11KV.class);
                        startActivity(intent);
                    }*/
                } else {
                    CommanUtilits.internetDialog(HomeActivity.this);
                    //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });

        el_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TransmitHomeActivity.class);
                startActivity(intent);
            }
        });

        el_view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(HomeActivity.this)) {
                    Intent intent = new Intent(HomeActivity.this, FeederKetchHomeActivity.class);
                    startActivity(intent);
                } else {
                    CommanUtilits.internetDialog(HomeActivity.this);
                    // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });

        //33KV
        db_33KV = new com.apepdcl.gis_survey.module_33KV.ArcGISDatabase(this);
        surveyVoList = db_33KV.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
        incrementalSurveyVoList = db_33KV.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
        surveyVosEditLatLan33=db_33KV.getAllEditSurveyLatLan();
        surveyVosEditSourcePole33=db_33KV.getEditSourcePoleSurveyData();
        surveyVosEditPoleDetails33=db_33KV.getEditPoleDetailsSurveyData();

        tt_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LoginActivity.IS_33KV_USER) {
                    if (ConnectivityChecking.checkConnection(HomeActivity.this)) {
                        validate();
                        /*if (Build.VERSION.SDK_INT >= 23) {
                            if (AppData.getInstance().isPermission()) {
                                validate();
                            } else {
                                if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                    Utilites.isStoragePermissionGranted(HomeActivity.this);

                                }
                            }

                        } else {
                            validate();
                        }*/

                    } else {
                        CommanUtilits.internetDialog(HomeActivity.this);
                        // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
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

        tt_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.IS_33KV_USER) {
                    if (surveyVoList.size() > 0 || incrementalSurveyVoList.size() > 0 || surveyVosEditLatLan33.size() > 0 || surveyVosEditSourcePole33.size()>0 || surveyVosEditPoleDetails33.size()> 0) {
                        Intent intentPf = new Intent(HomeActivity.this, TransmitDataHomeActivity.class);
                        startActivity(intentPf);
                    } else {
                        Toast.makeText(HomeActivity.this, "No data to transmit", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
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

        tt_view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.IS_33KV_USER) {
                    if (ConnectivityChecking.checkConnection(HomeActivity.this)) {
                        loadFeederDataData();
                    } else {
                        CommanUtilits.internetDialog(HomeActivity.this);
                        //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
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
                Intent intentPf = new Intent(HomeActivity.this, Profile.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        if (DashBoard.dialogDataModels != null && !DashBoard.dialogDataModels.isEmpty()) {
            DialogDataModel firstModel = DashBoard.dialogDataModels.get(0);

            if (firstModel.getIS_ENABLED() != null) {
                if (firstModel.getIS_ENABLED().equalsIgnoreCase("false")) {

                } else {
                    CustomDialog.showCustomDialog(HomeActivity.this, DashBoard.dialogDataModels);
                }
            }
        } else {
            Log.e("DialogError", "dialogDataModels is null or empty");
        }





    }

    public void validate() {
        if (surveyVoList.size() > 0 || incrementalSurveyVoList.size() > 0 ||surveyVosEditLatLan33.size() > 0 || surveyVosEditSourcePole33.size()>0 || surveyVosEditPoleDetails33.size()> 0) {
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
            FeederMasterRequestVo feederMasterRequestVo = new FeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(HomeActivity.this));
            feederMasterRequestVo.setSESSION_ID(preferences.getString(MobileConstants.SESSION_ID, ""));
            feederMasterRequestVo.setUSER_ID(loginDataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION_33_KV);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);

            //  Log.v("DATA===>>", "" + json);
            // retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            com.apepdcl.gis_survey.module_33KV.RetrofitService rs = com.apepdcl.gis_survey.module_33KV.RetrofitClientInstance.getRetrofitInstance().create(com.apepdcl.gis_survey.module_33KV.RetrofitService.class);
            Call<FeederMasterResponseVo> responseVoCall = rs.getFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVo> call, Response<FeederMasterResponseVo> response) {
                    loader.setVisibility(View.GONE);
                    if (response.body() != null) {
                        FeederMasterResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            db_33KV.clearFeederData();

                            List<FeederMasterResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (FeederMasterResponseDataVo vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                FeederDataVo feederDataVo = new FeederDataVo();
                                feederDataVo.setEHT_SS_CODE(ssCode);
                                feederDataVo.setEHT_SS_NAME(ssName);
                                feederDataVo.setFEEDER_CODE_33KV(feederCode);
                                feederDataVo.setFEEDER_NAME_33KV(feedERName);
                                feederDataVo.setWOID(vo.getWO_ID());
                                feederDataVo.setIS_FEEDER_TYPE(vo.getIS_FEEDER_TYPE());
                                db_33KV.addFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }
                            Intent intent = new Intent(HomeActivity.this, SurveyHome33KV.class);
                            startActivity(intent);
                            // finish();


                        } else {
                            //Toast.makeText(FeederMasterActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();
                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                                    alertDialogBuilder.setTitle("A New Version Is Available!");
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
                                Toast.makeText(HomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVo> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    CommanUtilits.netWorkError(HomeActivity.this);
                    //Toast.makeText(HomeActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getGisltckt() {
        loader.setVisibility(View.VISIBLE);
        LTCktsReqVo ltCktsReqVo = new LTCktsReqVo();
        ltCktsReqVo.setASSIGNED_TO(userName);
        // ltCktsReqVo.setASSIGNED_TO("1078667");
        ltCktsReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(HomeActivity.this));
        ltCktsReqVo.setVERSION(MobileConstants.APP_VERSION);
        ltCktsReqVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());

        Gson gson = new Gson();
        String str = gson.toJson(ltCktsReqVo);
        //  Log.v("DATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<LTCktsResponseVo> responseVoCall = retrofitService.getGisltckt(ltCktsReqVo);
        responseVoCall.enqueue(new Callback<LTCktsResponseVo>() {
            @Override
            public void onResponse(Call<LTCktsResponseVo> call, Response<LTCktsResponseVo> response) {
                loader.setVisibility(View.GONE);
                if (response.body() != null) {
                    LTCktsResponseVo ltCktsResponseVo = response.body();

                 /*   Gson gson=new Gson();
                    String str=gson.toJson(ltCktsResponseVo);
                    Log.v("GSL===>>", "" + str);*/
                    if (MobileConstants.TRUE.equalsIgnoreCase(ltCktsResponseVo.getSTATUS())) {
                        //OLD
                        arcGISDatabase.clearDTRData();
                        List<LTCktsResponseDataVo> ltSurveyRequestDataVosList = ltCktsResponseVo.getRESPONSE();
                        list = new ArrayList<>(ltCktsResponseVo.getRESPONSE());
                        for (LTCktsResponseDataVo surveyRequestDataVo : ltSurveyRequestDataVosList) {
                            // System.out.println("GIS LTCKT RESPONSE : "+surveyRequestDataVo.getLT_CKTCODE());
                            arcGISDatabase.addDTRData(surveyRequestDataVo);
                        }
                        Intent intent = new Intent(HomeActivity.this, DTRSelectActivity.class);
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

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                                alertDialogBuilder.setTitle("A New Version Is Available!");
                                //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                alertDialogBuilder

                                        .setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                                                "Please Download latest app <a href=\"https://gisappstore.apeasternpower.com/GISSurveyLT.apk\"></a>"))
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

                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, ltCktsResponseVo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                        }
                        //Toast.makeText(LTSurveyHomeActivity.this,ltCktsResponseVo.getERR_MSG().toString(),Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(HomeActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LTCktsResponseVo> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getCompletedDtrs() {

        loader.setVisibility(View.VISIBLE);
        LTCktsReqVo ltCktsReqVo = new LTCktsReqVo();
        ltCktsReqVo.setASSIGNED_TO(userName);
        // ltCktsReqVo.setASSIGNED_TO("1078667");
        ltCktsReqVo.setIMEI_NO(CommanUtilits.getIMEI_NO(HomeActivity.this));
        ltCktsReqVo.setVERSION(MobileConstants.APP_VERSION);
        ltCktsReqVo.setSECTION_ID(loginDataResponses.get(0).getOFFICE_ID());

        Gson gson = new Gson();
        String str = gson.toJson(ltCktsReqVo);
        //  Log.v("DATA===>>", "" + str);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<LTCktsResponseVo> responseVoCall = retrofitService.getGisltckt(ltCktsReqVo);
        responseVoCall.enqueue(new Callback<LTCktsResponseVo>() {
            @Override
            public void onResponse(Call<LTCktsResponseVo> call, Response<LTCktsResponseVo> response) {
                loader.setVisibility(View.GONE);
                if (response.body() != null) {
                    LTCktsResponseVo ltCktsResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(ltCktsResponseVo.getSTATUS())) {
                        arcGISDatabase.clearCmpDTRData();
                        List<LTCktsResponseDataVo> ltSurveyRequestDataVosList = ltCktsResponseVo.getRESPONSE();
                        for (LTCktsResponseDataVo surveyRequestDataVo : ltSurveyRequestDataVosList) {
                            arcGISDatabase.addCmpDTRData(surveyRequestDataVo);
                        }
                        Intent intent = new Intent(HomeActivity.this, CmpDTRSelectActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(HomeActivity.this, ltCktsResponseVo.getERR_MSG().toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(HomeActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LTCktsResponseVo> call, Throwable t) {
                loader.setVisibility(View.GONE);
                CommanUtilits.netWorkError(HomeActivity.this);
                //Toast.makeText(HomeActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
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
                    com.apepdcl.gis_survey.module_11KV.ArcGISDatabase db11kv = new com.apepdcl.gis_survey.module_11KV.ArcGISDatabase(HomeActivity.this);
                    arcGISDatabase.clearData();
                    db11kv.clearData();
                    db_33KV.clearData();

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear().commit();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void openConfirmLogoutDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setTitle(dataAvailableText);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OKAY",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                com.apepdcl.gis_survey.module_11KV.ArcGISDatabase db11kv = new com.apepdcl.gis_survey.module_11KV.ArcGISDatabase(HomeActivity.this);
                                arcGISDatabase.clearData();
                                db11kv.clearData();
                                db_33KV.clearData();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear().commit();

                                dialog.cancel();

                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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
            FeederMasterRequestVo feederMasterRequestVo = new FeederMasterRequestVo();
            feederMasterRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(HomeActivity.this));
            feederMasterRequestVo.setSESSION_ID(preferences.getString(MobileConstants.SESSION_ID, ""));
            feederMasterRequestVo.setUSER_ID(loginDataResponses.get(0).getOFFICE_ID());
            feederMasterRequestVo.setVERSION(MobileConstants.APP_VERSION_33_KV);
            Gson gson = new Gson();
            String json = gson.toJson(feederMasterRequestVo);

            // Log.v("DATA===>>", "" + json);
            com.apepdcl.gis_survey.module_33KV.RetrofitService rs = com.apepdcl.gis_survey.module_33KV.RetrofitClientInstance.getRetrofitInstance().create(com.apepdcl.gis_survey.module_33KV.RetrofitService.class);
            Call<FeederMasterResponseVo> responseVoCall = rs.getCmpFeederMaterData(feederMasterRequestVo);
            responseVoCall.enqueue(new Callback<FeederMasterResponseVo>() {
                @Override
                public void onResponse(Call<FeederMasterResponseVo> call, Response<FeederMasterResponseVo> response) {
                    loader.setVisibility(View.GONE);
                    if (response.body() != null) {
                        FeederMasterResponseVo response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);

                        // Log.v("DATA===>>", "" + json);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            db_33KV.cmpClearFeederData();

                            List<FeederMasterResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                            for (FeederMasterResponseDataVo vo : masterResponseDataVos) {
                                String ehtSS = vo.getEHT_SS();
                                String[] ss = ehtSS.split("#");
                                String ssCode = ss[0]; // 004
                                String ssName = ss[1];
                                String ehtFeeder = vo.getEHT_FEDER();
                                String[] feeder = ehtFeeder.split("#");
                                String feederCode = feeder[0]; // 004
                                String feedERName = feeder[1];

                                FeederDataVo feederDataVo = new FeederDataVo();
                                feederDataVo.setEHT_SS_CODE(ssCode);
                                feederDataVo.setEHT_SS_NAME(ssName);
                                feederDataVo.setFEEDER_CODE_33KV(feederCode);
                                feederDataVo.setFEEDER_NAME_33KV(feedERName);
                                db_33KV.addCmpFeederData(feederDataVo);
                                // arcGISDatabase.addFeederMasterData(vo);
                            }

                            Intent intent = new Intent(HomeActivity.this, CompletedSurveyActivity33KV.class);
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

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                                    alertDialogBuilder.setTitle("A New Version is Available!");
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
                                Toast.makeText(HomeActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FeederMasterResponseVo> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    //  progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Try Again", Toast.LENGTH_LONG).show();
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
}