package com.apepdcl.gis_survey.PMI.new_screens;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.PMI.PMIWorkInvolments;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.PMI.PmiHome;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.DTR;
import com.apepdcl.gis_survey.PMI.models.DefectGroupModel;
import com.apepdcl.gis_survey.PMI.models.DefectRVModel;
import com.apepdcl.gis_survey.PMI.models.DefectsModel;
import com.apepdcl.gis_survey.PMI.models.EHT_SS_MASTER;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_11KV;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_33KV;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.PMI.models.SS_MASTER_11KV;
import com.apepdcl.gis_survey.PMI.rectification.Select11KVFeederRectify;
import com.apepdcl.gis_survey.PMI.rectification.Select33KVFeederRectify;
import com.apepdcl.gis_survey.PMI.rectification.SelectLTCircuitRectify;
import com.apepdcl.gis_survey.PMI.rectification.SelectLTPoleRectify;
import com.apepdcl.gis_survey.Profile;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 02-08-2025.
 */
public class PMINewHome extends AppCompatActivity {


    ArrayList<SlideModel> slideList;
    GifImageView loader;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;

    LinearLayout tt_transmit_data, tt_start_survey;
    LinearLayout el_transmit_data, el_start_survey;
    LinearLayout lt_transmit_data, lt_start_survey;

    public static ArrayList<FEEDER_MASTER_33KV> feederMaster33;
    public static ArrayList<FEEDER_MASTER_11KV> feederMaster11;
    public static ArrayList<DTR> dtrMaster;

    private ArcGISDatabase arcGISDatabase;
    private RetrofitServicePMI retrofitServicePmi;
    ArrayList<PostingModel> postingList;
    PmiDatabase pmiDatabase;

    public static ArrayList<DefectsModel> allDefectsList;
    public static ArrayList<DefectGroupModel> defectsGroupListLT, defectsGroupList11, defectsGroupList33;
    public static ArrayList<DefectRVModel> defectRVList;
    public static ArrayList<String> defectsTypeList;

    LinearLayout tt_rectify, el_rectify, lt_rectify;
    private SharedPreferences preferences;
    boolean dataAvailable = false;
    String dataAvailableText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmi_home_new);
        getSupportActionBar().hide();
        preferences = PreferenceManager.getDefaultSharedPreferences(PMINewHome.this);

        pmiDatabase = new PmiDatabase(this);
        postingList = new ArrayList<>();


        tt_rectify = findViewById(R.id.tt_rectify);
        el_rectify = findViewById(R.id.el_rectify);
        lt_rectify = findViewById(R.id.lt_rectify);


        tt_transmit_data = findViewById(R.id.tt_transmit_data);
        tt_start_survey = findViewById(R.id.tt_start_survey);
        el_transmit_data = findViewById(R.id.el_transmit_data);
        el_start_survey = findViewById(R.id.el_start_survey);
        lt_transmit_data = findViewById(R.id.lt_transmit_data);
        lt_start_survey = findViewById(R.id.lt_start_survey);

        slideList = new ArrayList<>();
        loader = findViewById(R.id.loader);
        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);

        feederMaster33 = new ArrayList<>();
        feederMaster11 = new ArrayList<>();
        dtrMaster = new ArrayList<>();
        defectsGroupListLT = new ArrayList<>();
        defectsGroupList11 = new ArrayList<>();
        defectsGroupList33 = new ArrayList<>();
        defectRVList = new ArrayList<>();
        allDefectsList = new ArrayList<>();
        defectsTypeList = new ArrayList<>();
        arcGISDatabase = new ArcGISDatabase(this);

        loadDefectsMaster();


        el_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postingList.clear();
                postingList = pmiDatabase.getPmiPostingData();
                if (!postingList.isEmpty()) {
                    int poles_count_11 = 0;

                    for (int i = 0; i < postingList.size(); i++) {
                        if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV")) {
                            poles_count_11++;
                        }
                    }

                    if (poles_count_11 > 0) {
                        Toast.makeText(PMINewHome.this, "PLEASE TRANSMIT PENDING DATA", Toast.LENGTH_SHORT).show();

                    } else {
                        //check11KVData();
                        Intent intent = new Intent(PMINewHome.this, Select11KVFeeder.class);
                        startActivity(intent);

                    }

                } else {
                    //check11KVData();
                    Intent intent = new Intent(PMINewHome.this, Select11KVFeeder.class);
                    startActivity(intent);

                }

            }
        });

        el_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postingList.clear();
                postingList = pmiDatabase.getPmiPostingData();
                check11KVTransmitData();
            }
        });

        tt_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postingList.clear();
                postingList = pmiDatabase.getPmiPostingData();
                if (!postingList.isEmpty()) {
                    int poles_count_33 = 0;

                    for (int i = 0; i < postingList.size(); i++) {
                        if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                            poles_count_33++;
                        }
                    }

                    if (poles_count_33 > 0) {
                        Toast.makeText(PMINewHome.this, "PLEASE TRANSMIT PENDING DATA", Toast.LENGTH_SHORT).show();
                    } else {
                       // check33KVData();
                        Intent intent = new Intent(PMINewHome.this, Select33KVFeeder.class);
                        startActivity(intent);
                    }

                } else {
                    //check33KVData();
                    Intent intent = new Intent(PMINewHome.this, Select33KVFeeder.class);
                    startActivity(intent);
                }

            }
        });

        tt_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postingList.clear();
                postingList = pmiDatabase.getPmiPostingData();
                check33KVTransmitData();
            }
        });

        lt_start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postingList.clear();
                postingList = pmiDatabase.getPmiPostingData();
                if (!postingList.isEmpty()) {
                    int poles_count_33 = 0;

                    for (int i = 0; i < postingList.size(); i++) {
                        if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                            poles_count_33++;
                        }
                    }

                    if (poles_count_33 > 0) {
                        Toast.makeText(PMINewHome.this, "PLEASE TRANSMIT PENDING DATA", Toast.LENGTH_SHORT).show();

                    } else {
                        //checkLTData();
                        Intent intent = new Intent(PMINewHome.this, SelectLTCircuit.class);
                        startActivity(intent);

                    }

                } else {
                    Intent intent = new Intent(PMINewHome.this, SelectLTCircuit.class);
                    startActivity(intent);
                    //checkLTData();
                }

            }
        });

        lt_transmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postingList.clear();
                postingList = pmiDatabase.getPmiPostingData();
                checkLTTransmitData();
            }
        });

        lt_rectify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                postingList.clear();
//                postingList = pmiDatabase.getPmiPostingData();
//
//                if (!postingList.isEmpty()) {
//                    int poles_count_33 = 0;
//
//                    for (int i = 0; i < postingList.size(); i++) {
//                        if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
//                            poles_count_33++;
//                        }
//                    }
//
//                    if (poles_count_33 > 0) {
//                        Toast.makeText(PMINewHome.this, "PLEASE TRANSMIT PENDING DATA", Toast.LENGTH_SHORT).show();
//                    } else {
//                        checkLTDataForRectify();
//                    }
//
//                } else {
//                    checkLTDataForRectify();
//                }


            }
        });

        el_rectify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                postingList.clear();
//                postingList = pmiDatabase.getPmiPostingData();
//
//                if (!postingList.isEmpty()) {
//                    int poles_count_11 = 0;
//
//                    for (int i = 0; i < postingList.size(); i++) {
//                        if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV")) {
//                            poles_count_11++;
//                        }
//                    }
//
//                    if (poles_count_11 > 0) {
//                        Toast.makeText(PMINewHome.this, "PLEASE TRANSMIT PENDING DATA", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        check11KVDataForRectify();
//
//                    }
//
//                } else {
//                    check11KVDataForRectify();
//
//                }
            }
        });

        tt_rectify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                postingList.clear();
//                postingList = pmiDatabase.getPmiPostingData();
//                if (!postingList.isEmpty()) {
//                    int poles_count_33 = 0;
//
//                    for (int i = 0; i < postingList.size(); i++) {
//                        if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
//                            poles_count_33++;
//                        }
//                    }
//
//                    if (poles_count_33 > 0) {
//                        Toast.makeText(PMINewHome.this, "PLEASE TRANSMIT PENDING DATA", Toast.LENGTH_SHORT).show();
//                    } else {
//                        check33KVDataForRectify();
//                    }
//
//                } else {
//                    check33KVDataForRectify();
//                }
            }
        });

        ImageSlider imageSlider = findViewById(R.id.image_slider);

        ArrayList<SlideModel> slideList = new ArrayList<>();

        slideList.add(new SlideModel(R.drawable.b1, ScaleTypes.FIT));
        slideList.add(new SlideModel(R.drawable.b2, ScaleTypes.FIT));
        imageSlider.setImageList(slideList);


//        ImageSlider imageSlider = findViewById(R.id.image_slider);
//        if (DashBoard.bannerList != null && !DashBoard.bannerList.isEmpty()) {
//            for (int i = 0; i < DashBoard.bannerList.size(); i++) {
//                SlideModel m1 = new SlideModel(DashBoard.bannerList.get(i).getIMAGE_URL(), ScaleTypes.FIT);
//                slideList.add(m1);
//
//            }
//        }
//
//
//        imageSlider.setImageList(slideList);


        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(PMINewHome.this, Profile.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                //openLogoutDialog();
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

         if (pmiDatabase.getCurrentPmiPostingData().size() > 0) {
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
                }else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear().commit();
                    Intent intent = new Intent(PMINewHome.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    payDialog.cancel();
                }


            }
        });
    }

    private void openConfirmLogoutDialogBox() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(PMINewHome.this);
        alertDialogBuilder.setTitle(dataAvailableText);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OKAY",
                        (dialog, id) -> {
                            //userLogout();
                            com.apepdcl.gis_survey.module_11KV.ArcGISDatabase db11kv = new com.apepdcl.gis_survey.module_11KV.ArcGISDatabase(PMINewHome.this);
                            pmiDatabase.deleteAllPmiData();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear().commit();
                            Intent intent = new Intent(PMINewHome.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.cancel();


                        })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void checkLTTransmitData() {
        if (!postingList.isEmpty()) {
            int poles_count_33 = 0;

            for (int i = 0; i < postingList.size(); i++) {
                if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                    poles_count_33++;
                }
            }

            if (poles_count_33 > 0) {
                Intent intent = new Intent(PMINewHome.this, TransmitLT.class);
                startActivity(intent);

            } else {
                Toast.makeText(PMINewHome.this, "NO DATA TO TRANSMIT", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(PMINewHome.this, "NO DATA TO TRANSMIT", Toast.LENGTH_SHORT).show();
        }
    }


    private void check33KVTransmitData() {
        if (!postingList.isEmpty()) {
            int poles_count_33 = 0;
            for (int i = 0; i < postingList.size(); i++) {
                if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                    poles_count_33++;
                }
            }

            if (poles_count_33 > 0) {
                Intent intent = new Intent(PMINewHome.this, Transmit33KV.class);
                startActivity(intent);

            } else {
                Toast.makeText(PMINewHome.this, "NO DATA TO TRANSMIT", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(PMINewHome.this, "NO DATA TO TRANSMIT", Toast.LENGTH_SHORT).show();
        }
    }

    private void check11KVTransmitData() {
        if (!postingList.isEmpty()) {
            int poles_count_11 = 0;

            for (int i = 0; i < postingList.size(); i++) {
                if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV")) {
                    poles_count_11++;
                }
            }

            if (poles_count_11 > 0) {
                Intent intent = new Intent(PMINewHome.this, Transmit11KV.class);
                startActivity(intent);

            } else {
                Toast.makeText(PMINewHome.this, "NO DATA TO TRANSMIT", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(PMINewHome.this, "NO DATA TO TRANSMIT", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkLTData() {
        loader.setVisibility(View.VISIBLE);
        dtrMaster.clear();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("USERID", userId);
        // dtrDetailsReqestVo.addProperty("USERID", "1073477");

        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.userWiseDefectList(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();
                            JsonArray arrayLT = dataObject.getAsJsonArray("DTR");
                            for (int i = 0; i < arrayLT.size(); i++) {
                                DTR model = new DTR();
                                // model.setSECTION_NAME(arrayLT.get(i).getAsJsonObject().get("SECTION_NAME").getAsString());
                                model.setDTR_STRUCTURE_CODE(arrayLT.get(i).getAsJsonObject().get("DTR_CODE").getAsString());
                                //  model.setNO_OF_LT_CKTS(arrayLT.get(i).getAsJsonObject().get("NO_OF_LT_CKTS").getAsString());
                                model.setDTR_LOCATION(arrayLT.get(i).getAsJsonObject().get("DTR_LOCATION").getAsString());
                                // model.setFEEDER_NAME(arrayLT.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                model.setFEEDER_ID(arrayLT.get(i).getAsJsonObject().get("LT_CKT_CODE").getAsString());
                                // model.setSECTION_ID(arrayLT.get(i).getAsJsonObject().get("SECTION_ID").getAsString());
                                // model.setMAPPED(arrayLT.get(i).getAsJsonObject().get("MAPPED").getAsString());
                                dtrMaster.add(model);
                            }

                            if (dtrMaster.isEmpty()) {
                                Toast.makeText(PMINewHome.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PMINewHome.this, SelectLTCircuit.class);
                                startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(PMINewHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PMINewHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                //CommanUtilits.netWorkError(PMINewHome.this);
                //Toast.makeText(PMINewHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void check11KVData() {
        loader.setVisibility(View.VISIBLE);
        feederMaster11.clear();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("USERID", userId);

        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.userWiseDefectList(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();

                            JsonArray arrayFeeder11 = dataObject.getAsJsonArray("11KV_FEEDER_MASTER");
                            for (int i = 0; i < arrayFeeder11.size(); i++) {
                                FEEDER_MASTER_11KV model = new FEEDER_MASTER_11KV();
                                model.setSEC_ID(arrayFeeder11.get(i).getAsJsonObject().get("SEC_ID").getAsString());
                                model.setSEC_NAME(arrayFeeder11.get(i).getAsJsonObject().get("SEC_NAME").getAsString());
                                model.setSUB_STATION_NAME(arrayFeeder11.get(i).getAsJsonObject().get("SUB_STATION_NAME").getAsString());
                                model.setSUB_STATION_ID(arrayFeeder11.get(i).getAsJsonObject().get("SUB_STATION_ID").getAsString());
                                model.setFEEDER_NAME(arrayFeeder11.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                model.setFEEDER_ID(arrayFeeder11.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setPOLES(arrayFeeder11.get(i).getAsJsonObject().get("POLES").getAsString());
                                feederMaster11.add(model);
                            }

                            if (feederMaster11.isEmpty()) {
                                Toast.makeText(PMINewHome.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PMINewHome.this, Select11KVFeeder.class);
                                startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(PMINewHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PMINewHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(PMINewHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void check33KVData() {
        loader.setVisibility(View.VISIBLE);
        feederMaster33.clear();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("USERID", userId);

        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.userWiseDefectList(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();

                            JsonArray arrayFeeder33 = dataObject.getAsJsonArray("33KV_FEEDER_MASTER");
                            for (int i = 0; i < arrayFeeder33.size(); i++) {
                                FEEDER_MASTER_33KV model = new FEEDER_MASTER_33KV();
                                model.setEHT_SS_CODE(arrayFeeder33.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(arrayFeeder33.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                model.setFEEDER_CODE_33KV(arrayFeeder33.get(i).getAsJsonObject().get("FEEDER_CODE_33KV").getAsString());
                                model.setFEEDER_NAME_33KV(arrayFeeder33.get(i).getAsJsonObject().get("FEEDER_NAME_33KV").getAsString());
                                model.setDIVISION_ID(arrayFeeder33.get(i).getAsJsonObject().get("DIVISION_ID").getAsString());
                                model.setPOLES(arrayFeeder33.get(i).getAsJsonObject().get("POLES").getAsString());
                                feederMaster33.add(model);
                            }

                            if (feederMaster33.isEmpty()) {
                                Toast.makeText(PMINewHome.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PMINewHome.this, Select33KVFeeder.class);
                                startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(PMINewHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PMINewHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(PMINewHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadDefectsMaster() {
        allDefectsList.clear();
        defectsTypeList.clear();
        defectRVList.clear();
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Map<String, String> map = new HashMap<>();
        map.put("VERSION", getResources().getString(R.string.displayVersion));

        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(map).getAsJsonObject();
        Call<JsonObject> responseVoCall = retrofitServicePmi.getDefectMasterNew(jsonObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        String OFFLINE_DISTANCE = "", ONLINE_DISTANCE = "";

                        try {
                            OFFLINE_DISTANCE = surveyedResponseVo.get("OFFLINE_DISTANCE").getAsString();
                            ONLINE_DISTANCE = surveyedResponseVo.get("ONLINE_DISTANCE").getAsString();
                            pmiDatabase.insertDistanceCheck(OFFLINE_DISTANCE, ONLINE_DISTANCE);
                            JsonArray array33 = surveyedResponseVo.getAsJsonArray("DATA");
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < array33.size(); i++) {
                                DefectsModel model = new DefectsModel();
                                model.setDEFECT_ID(array33.get(i).getAsJsonObject().get("DEFECT_ID").isJsonNull() ? "" : "" + array33.get(i).getAsJsonObject().get("DEFECT_ID").getAsInt());
                                model.setDEFECT_TYPE(array33.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
                                model.setDEFECT_SUB_TYPE(array33.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").getAsString());
                                model.setDEFECT_SUB_TYPE_ID(array33.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE_ID").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE_ID").getAsString());
                                model.setVOLTAGE_LEVEL(array33.get(i).getAsJsonObject().get("VOLTAGE_LEVEL").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("VOLTAGE_LEVEL").getAsString());
                                list.add(array33.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
                                model.setAI(array33.get(i).getAsJsonObject().get("AI").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("AI").getAsString());
                                model.setPHOTO(array33.get(i).getAsJsonObject().get("PHOTO").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("PHOTO").getAsString());
                                model.setGRP_ID(array33.get(i).getAsJsonObject().get("GRP_ID").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("GRP_ID").getAsString());
                                model.setDEFECT_GROUP(array33.get(i).getAsJsonObject().get("DEFECT_GROUP").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_GROUP").getAsString());
                                allDefectsList.add(model);
                            }


                            // defectsGroupList = prepareGroupedDefects(allDefectsList);
                            ArrayList<DefectsModel> tempLTDefectsList = new ArrayList<>();
                            ArrayList<DefectsModel> temp11DefectsList = new ArrayList<>();
                            ArrayList<DefectsModel> temp33DefectsList = new ArrayList<>();
                            for (int i = 0; i < allDefectsList.size(); i++) {
                                if (allDefectsList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                                    tempLTDefectsList.add(allDefectsList.get(i));
                                }
                                if (allDefectsList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV")) {
                                    temp11DefectsList.add(allDefectsList.get(i));
                                }
                                if (allDefectsList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                                    temp33DefectsList.add(allDefectsList.get(i));
                                }
                            }


                            ArrayList<DefectGroupModel> tempListLt = prepareGroupedDefects(tempLTDefectsList);
                            for (int i = 0; i < tempListLt.size(); i++) {
                                DefectGroupModel defectGroupModel = new DefectGroupModel(tempListLt.get(i).getDefectGroup(), tempListLt.get(i).getDefects());
                                defectGroupModel.setImage("");
                                defectGroupModel.setImageSelected(false);
                                // defectGroupModel.setPhotoNeeded(tempListLt.get(i).getDefects().get(0).getAI().equalsIgnoreCase("N"));
                                // defectGroupModel.setPhotoNeeded(defectGroupModel.getDefects().get(0).getAI().equalsIgnoreCase("N"));

                                for (int j = 0; j < tempListLt.get(i).getDefects().size(); j++) {
                                    if (tempListLt.get(i).getDefects().get(j).getAI().equalsIgnoreCase("N")) {
                                        defectGroupModel.setPhotoNeeded(true);
                                        break;
                                    }
                                }
                                defectsGroupListLT.add(defectGroupModel);
                            }

                            ArrayList<DefectGroupModel> tempList11 = prepareGroupedDefects(temp11DefectsList);
                            for (int i = 0; i < tempList11.size(); i++) {
                                DefectGroupModel defectGroupModel = new DefectGroupModel(tempList11.get(i).getDefectGroup(), tempList11.get(i).getDefects());
                                defectGroupModel.setImage("");
                                defectGroupModel.setImageSelected(false);
                                // defectGroupModel.setPhotoNeeded(tempList11.get(i).getDefects().get(0).getAI().equalsIgnoreCase("N"));
                                for (int j = 0; j < tempList11.get(i).getDefects().size(); j++) {
                                    if (tempList11.get(i).getDefects().get(j).getAI().equalsIgnoreCase("N")) {
                                        defectGroupModel.setPhotoNeeded(true);
                                        break;
                                    }
                                }
                                defectsGroupList11.add(defectGroupModel);
                            }

                            ArrayList<DefectGroupModel> tempList33 = prepareGroupedDefects(temp33DefectsList);
                            for (int i = 0; i < tempList33.size(); i++) {
                                DefectGroupModel defectGroupModel = new DefectGroupModel(tempList33.get(i).getDefectGroup(), tempList33.get(i).getDefects());
                                defectGroupModel.setImage("");
                                defectGroupModel.setImageSelected(false);
                                //defectGroupModel.setPhotoNeeded(tempList33.get(i).getDefects().get(0).getAI().equalsIgnoreCase("N"));
                                for (int j = 0; j < tempList33.get(i).getDefects().size(); j++) {
                                    if (tempList33.get(i).getDefects().get(j).getAI().equalsIgnoreCase("N")) {
                                        defectGroupModel.setPhotoNeeded(true);
                                        break;
                                    }
                                }
                                defectsGroupList33.add(defectGroupModel);
                            }

                            /*Gson gson = new Gson();
                            String json = gson.toJson(defectsGroupList);
                            Log.v("PMI DATA===>>", "" + json);*/


                            defectsTypeList = new ArrayList<>(new LinkedHashSet<>(list));
                            // System.out.println("DEFECT TYPES : " + defectsTypeList);
                            for (int i = 0; i < defectsTypeList.size(); i++) {
                                DefectRVModel rvModel = new DefectRVModel();
                                rvModel.setDefect(defectsTypeList.get(i));
                                rvModel.setDefectSelected(false);
                                rvModel.setSelectedRadioButtonId(-1);
                                ArrayList<DefectsModel> listDefectsModels = new ArrayList<>();
                                for (int j = 0; j < allDefectsList.size(); j++) {
                                    if (defectsTypeList.get(i).equalsIgnoreCase(allDefectsList.get(j).getDEFECT_TYPE())) {
                                        DefectsModel defectsModel = new DefectsModel();
                                        defectsModel.setDEFECT_ID(allDefectsList.get(j).getDEFECT_ID());
                                        defectsModel.setDEFECT_TYPE(allDefectsList.get(j).getDEFECT_TYPE());
                                        defectsModel.setDEFECT_SUB_TYPE(allDefectsList.get(j).getDEFECT_SUB_TYPE());
                                        defectsModel.setDEFECT_SUB_TYPE_ID(allDefectsList.get(j).getDEFECT_SUB_TYPE_ID());
                                        defectsModel.setVOLTAGE_LEVEL(allDefectsList.get(j).getVOLTAGE_LEVEL());
                                        defectsModel.setImagePath("");
                                        defectsModel.setImage("N");
                                        defectsModel.setImageSelected(false);
                                        defectsModel.setSelected(false);
                                        listDefectsModels.add(defectsModel);
                                    }
                                }
                                rvModel.setDefectsModelArrayList(listDefectsModels);
                                defectRVList.add(rvModel);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        openAlertDialog(surveyedResponseVo.get("ERR_MSG").toString());
                        //Toast.makeText(PMINewHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(PMINewHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(PMINewHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void openAlertDialog(String message) {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        // Set Data
        title.setText("Pay Attention!!");
        msg.setText(message);

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        okBtn.setText("Okay");

        // Click action
        okBtn.setOnClickListener(v -> {
            onBackPressed();
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }
//    private void openAlertDialog(String error_msg) {
//        new AlertDialog.Builder(this)
//                .setTitle("Pay attention")
//                .setCancelable(false)
//                .setMessage(error_msg)
//                .setPositiveButton("Okay", (dialog, which) -> {
//                    onBackPressed();
//                    dialog.cancel();
//                })
//                .show();
//
//
//    }

    private void checkLTDataForRectify() {
        loader.setVisibility(View.VISIBLE);
        dtrMaster.clear();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("USERID", userId);
        // dtrDetailsReqestVo.addProperty("USERID", "1073477");

        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.userWiseDefectListRectify(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();
                            JsonArray arrayLT = dataObject.getAsJsonArray("DTR");
                            for (int i = 0; i < arrayLT.size(); i++) {
                                DTR model = new DTR();
                                // model.setSECTION_NAME(arrayLT.get(i).getAsJsonObject().get("SECTION_NAME").getAsString());
                                model.setDTR_STRUCTURE_CODE(arrayLT.get(i).getAsJsonObject().get("DTR_CODE").getAsString());
                                //  model.setNO_OF_LT_CKTS(arrayLT.get(i).getAsJsonObject().get("NO_OF_LT_CKTS").getAsString());
                                model.setDTR_LOCATION(arrayLT.get(i).getAsJsonObject().get("DTR_LOCATION").getAsString());
                                // model.setFEEDER_NAME(arrayLT.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                JsonObject obj = arrayLT.get(i).getAsJsonObject();

                                if (obj.has("LT_CKT_CODE") && !obj.get("LT_CKT_CODE").isJsonNull()) {
                                    model.setFEEDER_ID(obj.get("LT_CKT_CODE").getAsString());
                                } else {
                                    model.setFEEDER_ID(""); // or null (your choice)
                                }
                                // model.setSECTION_ID(arrayLT.get(i).getAsJsonObject().get("SECTION_ID").getAsString());
                                // model.setMAPPED(arrayLT.get(i).getAsJsonObject().get("MAPPED").getAsString());
                                dtrMaster.add(model);
                            }

                            if (dtrMaster.isEmpty()) {
                                Toast.makeText(PMINewHome.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PMINewHome.this, SelectLTCircuitRectify.class);
                                startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(PMINewHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PMINewHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                CommanUtilits.netWorkError(PMINewHome.this);
                //Toast.makeText(PMINewHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void check11KVDataForRectify() {
        loader.setVisibility(View.VISIBLE);
        feederMaster11.clear();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("USERID", userId);

        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.userWiseDefectListRectify(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();

                            JsonArray arrayFeeder11 = dataObject.getAsJsonArray("11KV_FEEDER_MASTER");
                            for (int i = 0; i < arrayFeeder11.size(); i++) {
                                FEEDER_MASTER_11KV model = new FEEDER_MASTER_11KV();
                                model.setSEC_ID(arrayFeeder11.get(i).getAsJsonObject().get("SEC_ID").getAsString());
                                model.setSEC_NAME(arrayFeeder11.get(i).getAsJsonObject().get("SEC_NAME").getAsString());
                                model.setSUB_STATION_NAME(arrayFeeder11.get(i).getAsJsonObject().get("SUB_STATION_NAME").getAsString());
                                model.setSUB_STATION_ID(arrayFeeder11.get(i).getAsJsonObject().get("SUB_STATION_ID").getAsString());
                                model.setFEEDER_NAME(arrayFeeder11.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                model.setFEEDER_ID(arrayFeeder11.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setPOLES(arrayFeeder11.get(i).getAsJsonObject().get("POLES").getAsString());
                                feederMaster11.add(model);
                            }
                            if (feederMaster11.isEmpty()) {
                                Toast.makeText(PMINewHome.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PMINewHome.this, Select11KVFeederRectify.class);
                                startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(PMINewHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PMINewHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(PMINewHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void check33KVDataForRectify() {
        loader.setVisibility(View.VISIBLE);
        feederMaster33.clear();
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("USERID", userId);

        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.userWiseDefectListRectify(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();

                            JsonArray arrayFeeder33 = dataObject.getAsJsonArray("33KV_FEEDER_MASTER");
                            for (int i = 0; i < arrayFeeder33.size(); i++) {
                                FEEDER_MASTER_33KV model = new FEEDER_MASTER_33KV();
                                model.setEHT_SS_CODE(arrayFeeder33.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(arrayFeeder33.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                model.setFEEDER_CODE_33KV(arrayFeeder33.get(i).getAsJsonObject().get("FEEDER_CODE_33KV").getAsString());
                                model.setFEEDER_NAME_33KV(arrayFeeder33.get(i).getAsJsonObject().get("FEEDER_NAME_33KV").getAsString());
                                model.setDIVISION_ID(arrayFeeder33.get(i).getAsJsonObject().get("DIVISION_ID").getAsString());
                                model.setPOLES(arrayFeeder33.get(i).getAsJsonObject().get("POLES").getAsString());
                                feederMaster33.add(model);
                            }

                            if (feederMaster33.isEmpty()) {
                                Toast.makeText(PMINewHome.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PMINewHome.this, Select33KVFeederRectify.class);
                                startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(PMINewHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(PMINewHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(PMINewHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private ArrayList<DefectGroupModel> prepareGroupedDefects(ArrayList<DefectsModel> allDefectsList) {

        Map<String, ArrayList<DefectsModel>> map = new LinkedHashMap<>();

        for (DefectsModel defect : allDefectsList) {

            String group = defect.getDEFECT_GROUP();

            if (!map.containsKey(group)) {
                map.put(group, new ArrayList<>());
            }

            map.get(group).add(defect);
        }

        ArrayList<DefectGroupModel> result = new ArrayList<>();

        for (Map.Entry<String, ArrayList<DefectsModel>> entry : map.entrySet()) {
            // Log.d("GROUP - ",entry.getKey());
            result.add(new DefectGroupModel(entry.getKey(), entry.getValue()));
        }

        return result;
    }
}