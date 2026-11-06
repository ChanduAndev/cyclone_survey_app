package com.apepdcl.gis_survey;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.RDSS.RdssHome;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.BannerModel;
import com.apepdcl.gis_survey.estimates.models.DialogDataModel;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.estimates.sou.WebActivity;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.CustomDialog;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoard extends AppCompatActivity {
    // RelativeLayout asset_mapping, estimates, sasa, jhc, work_order, pmi, pmi_old, rdss;
    LinearLayout asset_mapping, work_order, pmi_old, rdss, profile_grid, solar_dpr, profile_new_grid;
    CardView asset_mappingCard, work_orderCard, pmi_oldCard, rdssCard, profile_gridCard, estimatesCard;

    CardView estimates;
    GifImageView loader;
    // RelativeLayout logout_layout;
    ImageView profile_layout;
    private RetrofitServiceEstimates retrofitService;
    public static ArrayList<BannerModel> bannerList;
    public static ArrayList<DialogDataModel> dialogDataModels;

    boolean dataAvailable = false;
    String dataAvailableText;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    ImageSlider imageSlider;

    private boolean isDialogShown = false;

    public static List<String> offline;
    public static List<String> online;

    public static boolean IS_RDSS_USER = false;

    // private TextView tvContent;
    @Override
    protected void onResume() {
        super.onResume();
        if (ConnectivityChecking.checkConnection(DashBoard.this)) {
            getBannerImagesApi();
        } else {
            CommanUtilits.internetDialog(DashBoard.this);
        }
        // getBannerImagesApi();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_new);
        getWindow().setStatusBarColor(Color.parseColor("#2B7CEE"));

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        // getSupportActionBar().hide();
        preferences = PreferenceManager.getDefaultSharedPreferences(DashBoard.this);
        LoginActivity.IS_33KV_USER = preferences.getBoolean(CommonNames.IS_33KV_USER, false);

        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();


        bannerList = new ArrayList<>();
        dialogDataModels = new ArrayList<>();
        offline = new ArrayList<>();
        online = new ArrayList<>();
        imageSlider = findViewById(R.id.image_slider);
       /* tvContent = findViewById(R.id.tvContent);
        tvContent.setText(
                "Use RDSS module to survey additional infra for solarization of SC ST —                                                                                                              "
        );
        tvContent.post(() -> tvContent.setSelected(true));*/
//        if (ConnectivityChecking.checkConnection(DashBoard.this)) {
//            getBannerImagesApi();
//        } else {
//            CommanUtilits.internetDialog(DashBoard.this);
//        }

        //getBannerImagesApi();
        //IMAGE SLIDER

//        SlideModel m1 = new SlideModel(R.drawable.image1, ScaleTypes.FIT);
//        SlideModel m3 = new SlideModel(R.drawable.b1, ScaleTypes.FIT);
//        SlideModel m4 = new SlideModel(R.drawable.b2, ScaleTypes.FIT);
//        slideList.add(m4);
//        slideList.add(m3);
//        slideList.add(m1);


        // imageSlider.setImageList(slideList);

        asset_mapping = findViewById(R.id.asset_mapping);
        estimates = findViewById(R.id.estimatesCard);
        // sasa = findViewById(R.id.sasa);
        // jhc = findViewById(R.id.jhc);
        work_order = findViewById(R.id.work_order);
        // pmi = findViewById(R.id.pmi);
        pmi_old = findViewById(R.id.pmi_old);
        rdss = findViewById(R.id.rdss);
        profile_grid = findViewById(R.id.profile_grid);
        solar_dpr = findViewById(R.id.solar);
        profile_new_grid = findViewById(R.id.profile_new_grid);


        asset_mappingCard = findViewById(R.id.asset_mappingCard);
        estimates = findViewById(R.id.estimatesCard);
        pmi_oldCard = findViewById(R.id.pmi_oldCard);
        work_orderCard = findViewById(R.id.work_orderCard);
        rdssCard = findViewById(R.id.rdssCard);
        estimatesCard = findViewById(R.id.estimatesCard);

        loader = findViewById(R.id.loader);
        //logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);


        asset_mappingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_RDSS_USER = false;
                Intent intentPf = new Intent(DashBoard.this, HomeActivity.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

/*
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAppDialog();

            }
        });*/

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(DashBoard.this, Profile.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });


        profile_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showVoltageDialog();
                openWebGis();

//                Intent intentPf = new Intent(DashBoard.this, Profile.class);
//                startActivity(intentPf);
//                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        profile_new_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showVoltageDialog();
                //openWebGis();

                Intent intentPf = new Intent(DashBoard.this, Profile.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        work_orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(DashBoard.this)) {
                    //   Intent intent = new Intent(DashBoard.this, EstimatesDashboardActivity.class);
                    LoginActivity.SURVEY_FROM = "WORK_ORDER";
                    Intent intent = new Intent(DashBoard.this, SurveyList.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                } else {
                    CommanUtilits.internetDialog(DashBoard.this);
                    // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }
            }
        });

        estimatesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.IS_33KV_USER) {
                    if (ConnectivityChecking.checkConnection(DashBoard.this)) {
                        LoginActivity.SURVEY_FROM = "ESTIMATES";
                        //   Intent intent = new Intent(DashBoard.this, EstimatesDashboardActivity.class);
                        Intent intent = new Intent(DashBoard.this, SurveyList.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    } else {
                        CommanUtilits.internetDialog(DashBoard.this);
                        //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }
                } else {
                    LoginActivity.SURVEY_FROM = "ESTIMATES";
                    //  Intent intent = new Intent(DashBoard.this, EstimatesDashboardActivity.class);
                    Intent intent = new Intent(DashBoard.this, SurveyList.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);


//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashBoard.this);
//                    alertDialogBuilder.setTitle("You are not Authorized.");
//                    alertDialogBuilder
//                            .setCancelable(false)
//                            .setPositiveButton("OKAY",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    })
//
//                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//                                }
//                            });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
                }
            }
        });


        solar_dpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.IS_33KV_USER) {
                    if (ConnectivityChecking.checkConnection(DashBoard.this)) {
                        LoginActivity.SURVEY_FROM = "SOLAR_DPR";
                        //   Intent intent = new Intent(DashBoard.this, EstimatesDashboardActivity.class);
                        Intent intent = new Intent(DashBoard.this, SurveyList.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                    } else {
                        CommanUtilits.internetDialog(DashBoard.this);
                        //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }
                } else {
                    LoginActivity.SURVEY_FROM = "SOLAR_DPR";
                    //  Intent intent = new Intent(DashBoard.this, EstimatesDashboardActivity.class);
                    Intent intent = new Intent(DashBoard.this, SurveyList.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);


//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashBoard.this);
//                    alertDialogBuilder.setTitle("You are not Authorized.");
//                    alertDialogBuilder
//                            .setCancelable(false)
//                            .setPositiveButton("OKAY",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    })
//
//                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//
//                                }
//                            });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
                }
            }
        });



        /*pmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, PmiHome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });*/

        pmi_oldCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intentPf = new Intent(DashBoard.this, PMIHomeActivity.class);
//                //Intent intentPf = new Intent(DashBoard.this, PmiHome.class);
//                startActivity(intentPf);
//                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);

                Intent intentPf = new Intent(DashBoard.this, PMINewHome.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        rdssCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_RDSS_USER = true;
                Intent intentPf = new Intent(DashBoard.this, RdssHome.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });

        String user_id = preferences.getString(MobileConstants.USERNAME, "");
        String role = loginDataResponses.get(0).getROLE();



        switch (role.toUpperCase()) {
            case "ADE":

                if (user_id.contains("ADE-COMM")) {
                    disableCard(asset_mappingCard, work_orderCard, pmi_oldCard);
                } else if (user_id.contains("ADE-O")) {
                    disableCard(asset_mappingCard, work_orderCard, estimatesCard);
                } else if (user_id.contains("ADE-UGC")) {
                    disableCard(asset_mappingCard, work_orderCard, pmi_oldCard, estimatesCard);
                }
                break;
            case "ADEC":
                if (user_id.contains("ADE-CON")) {
                    disableCard(asset_mappingCard, work_orderCard, pmi_oldCard, estimatesCard);
                }

            case "AEOP":
            case "AE":
                //disableViews(rdss);
                break;

            case "DE":
                disableCard(asset_mappingCard, estimatesCard, work_orderCard);
                break;

            case "UGC":
                disableCard(asset_mappingCard, work_orderCard, pmi_oldCard, estimatesCard);

            case "GIS VENDOR":
                disableCard(pmi_oldCard, estimatesCard, work_orderCard);
                break;

            case "ALM":
                disableCard(rdssCard, estimatesCard, work_orderCard);


            default:
                disableCard(estimatesCard, work_orderCard);
                break;
        }


        String device_id = getUID(this);



        editor = preferences.edit();
        editor.putString(MobileConstants.DEVICE_ID, device_id);
        editor.commit();





        /*sasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPf = new Intent(DashBoard.this, SasaHome.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });*/


    }

    @SuppressLint("HardwareIds")
    public static String getUID(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }

    


    private void disableCard(CardView... cards) {

        for (CardView card : cards) {

            if (card == null) {
                continue; // skip null safely
            }

            card.setEnabled(false);
            card.setClickable(false);

            int disabledColor = ContextCompat.getColor(this, R.color.disabledColor);
            card.setCardBackgroundColor(disabledColor);

            //card.setStrokeColor(ContextCompat.getColor(this, R.color.disabledColor));

            updateTextColor(card, R.color.ui_gray_color);
        }
    }

    private void updateTextColor(View view, int colorRes) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(this, colorRes));
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                updateTextColor(group.getChildAt(i), colorRes);
            }
        }
    }

//    private void disableViews(View... views) {
//        for (View view : views) {
//            view.setClickable(false);
//            view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
//
//        }
//    }

    private Dialog payDialog;

    private void exitAppDialog() {

        if (isFinishing() || isDestroyed()) {
            return;
        }

        payDialog = new Dialog(this);
        payDialog.setContentView(R.layout.close_dialog);
        payDialog.setCancelable(true);

        Window window = payDialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
            window.setWindowAnimations(R.style.DialogAnimation);
        }

        TextView cancelButton = payDialog.findViewById(R.id.dialog_cancel);
        TextView backToHome = payDialog.findViewById(R.id.back_to_home);
        TextView logInfo = payDialog.findViewById(R.id.log_info);

        cancelButton.setOnClickListener(v -> {
            if (payDialog != null && payDialog.isShowing()) {
                payDialog.dismiss();
            }
        });

        backToHome.setOnClickListener(v -> {
            dismissExitDialog();
            finishAffinity();
        });

        payDialog.setOnDismissListener(dialog -> payDialog = null);

        payDialog.show();

        // Set dialog size after showing it.
        window = payDialog.getWindow();

        if (window != null) {
            int height = getResources().getDisplayMetrics().heightPixels;
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    height / 2
            );
        }
    }

    private void dismissExitDialog() {
        if (payDialog != null && payDialog.isShowing()) {
            payDialog.dismiss();
        }
        payDialog = null;
    }

    @Override
    protected void onDestroy() {
        dismissExitDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitAppDialog();
    }

    private void getBannerImagesApi() {
        // loader_layout.setVisibility(View.VISIBLE);
        // tv_progress_bar.setText("Downloading Material Master");
        // main_ui_layout.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getBannerImages();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //loader_layout.setVisibility(View.GONE);
                //main_ui_layout.setVisibility(View.VISIBLE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (ConnectivityChecking.checkConnection(DashBoard.this)) {
                            getDialogDataApi();
                        } else {
                            CommanUtilits.internetDialog(DashBoard.this);
                        }


                        //getDialogDataApi();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            bannerList.clear();
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                BannerModel model = new BannerModel();
                                model.setIMAGE_URL(object.has("IMAGE_URL") && !object.get("IMAGE_URL").isJsonNull() ? object.get("IMAGE_URL").getAsString() : "");
                                model.setSTATUS(object.has("STATUS") && !object.get("STATUS").isJsonNull() ? object.get("STATUS").getAsString() : "");
                                model.setID(object.has("ID") && !object.get("ID").isJsonNull() ? object.get("ID").getAsString() : "");
                                model.setDESCRIPTION(object.has("DESCRIPTION") && !object.get("DESCRIPTION").isJsonNull() ? object.get("DESCRIPTION").getAsString() : "");
                                bannerList.add(model);

                            }


                            ArrayList<SlideModel> slideList = new ArrayList<>();
                            for (int i = 0; i < bannerList.size(); i++) {
                                SlideModel m1 = new SlideModel(bannerList.get(i).getIMAGE_URL(), ScaleTypes.FIT);
                                slideList.add(m1);

                            }
                            imageSlider.setImageList(slideList);

                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                CommanUtilits.netWorkError(DashBoard.this);
            }
        });


    }

    private void getFeederLoadsPolesAPI() {
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance("").create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getFeederLoadsPoles();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response != null && response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    offline.clear();
                    online.clear();

                    if (surveyedResponseVo.has("STATUS") && surveyedResponseVo.get("STATUS").getAsBoolean()) {
                        JsonObject dataObj = surveyedResponseVo.getAsJsonObject("DATA");

                        // Offline list
                        JsonArray offlineArray = dataObj.getAsJsonArray("offline");
                        for (int i = 0; i < offlineArray.size(); i++) {
                            offline.add(offlineArray.get(i).getAsString());
                        }
                        offline.add(0, "Select"); // add at first position

                        // Online list
                        JsonArray onlineArray = dataObj.getAsJsonArray("online");
                        for (int i = 0; i < onlineArray.size(); i++) {
                            online.add(onlineArray.get(i).getAsString());
                        }
                        online.add(0, "Select");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle failure
            }
        });
    }


    private void showVoltageDialog() {
        // Inflate custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_for_web_gis, null);

        Spinner spVoltage = dialogView.findViewById(R.id.sp_voltage_level_new);
        String URL11KV = "https://gisweb.apeasternpower.com/elevenKvGisMap2025";
        String URL33KV = "https://gisweb.apeasternpower.com/thirtyThreeKVPage";

        // Options
        String[] options = {"Select", "33KV", "11KV"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVoltage.setAdapter(adapter);

        // Build AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String selected = spVoltage.getSelectedItem().toString();
                    if (!selected.equals("Select")) {
                        Intent intent = new Intent(this, WebActivity.class);
                        if (selected.equalsIgnoreCase("33KV")) {
                            intent.putExtra("URL", URL33KV);
                        } else {
                            intent.putExtra("URL", URL11KV);
                        }
                        startActivity(intent);
                    } else {
                        Toast.makeText(DashBoard.this, "Please Select Voltage", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

//    private void openWebGis() {
//        Dialog payDialog = new Dialog(this);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        payDialog.setContentView(R.layout.web_gis_dialog);
//        payDialog.getWindow().setLayout(width, height / 2);
//        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//
//        final String[] voltage_level = {""};
//        MaterialCardView web_11 = payDialog.findViewById(R.id.web_11);
//        MaterialCardView web_33 = payDialog.findViewById(R.id.web_33);
//        MaterialCardView web_lT = payDialog.findViewById(R.id.web_LT);
//        TextView tv_continue=payDialog.findViewById(R.id.tv_continue);
//
//
//        web_11.setOnClickListener(v -> {
//            voltage_level[0] = "11KV";
//
//        });
//
//        web_33.setOnClickListener(v -> {
//            voltage_level[0] = "33KV";
//
//        });
//
//        web_lT.setOnClickListener(v -> {
//            voltage_level[0] = "LT";
//
//        });
//
//        tv_continue.setOnClickListener(v -> {
//
//            if (voltage_level[0].isEmpty()) {
//                Toast.makeText(this, "Please select voltage level", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            payDialog.dismiss();
//
//            Intent intent = new Intent(DashBoard.this, WebGis.class);
//            intent.putExtra("VOLTAGE_LEVEL", voltage_level[0]);
//            startActivity(intent);
//        });
//
//
//        payDialog.show();
//    }

    private void openWebGis() {
        Dialog payDialog = new Dialog(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        payDialog.setContentView(R.layout.web_gis_dialog);
        payDialog.getWindow().setLayout((int) (width * 0.9), height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        final String[] voltage_level = {""};

        // Cards
        MaterialCardView web_11 = payDialog.findViewById(R.id.web_11);
        MaterialCardView web_33 = payDialog.findViewById(R.id.web_33);
        MaterialCardView web_LT = payDialog.findViewById(R.id.web_LT);

        // RadioButtons
        RadioButton radio_11 = payDialog.findViewById(R.id.radio_11);
        RadioButton radio_33 = payDialog.findViewById(R.id.radio_33);
        RadioButton radio_LT = payDialog.findViewById(R.id.radio_LT);

        TextView tv_continue = payDialog.findViewById(R.id.tv_continue);

        // Click listeners
        web_11.setOnClickListener(v -> {
            voltage_level[0] = "11KV";
            selectItem(web_11, radio_11,
                    web_33, radio_33,
                    web_LT, radio_LT);
        });

        web_33.setOnClickListener(v -> {
            voltage_level[0] = "33KV";
            selectItem(web_33, radio_33,
                    web_11, radio_11,
                    web_LT, radio_LT);
        });

        web_LT.setOnClickListener(v -> {
            voltage_level[0] = "LT";
            selectItem(web_LT, radio_LT,
                    web_11, radio_11,
                    web_33, radio_33);
        });

        radio_11.setOnClickListener(v -> {
            voltage_level[0] = "11KV";
            selectItem(web_11, radio_11,
                    web_33, radio_33,
                    web_LT, radio_LT);
        });

        radio_33.setOnClickListener(v -> {
            voltage_level[0] = "33KV";
            selectItem(web_33, radio_33,
                    web_11, radio_11,
                    web_LT, radio_LT);
        });

        radio_LT.setOnClickListener(v -> {
            voltage_level[0] = "LT";
            selectItem(web_LT, radio_LT,
                    web_11, radio_11,
                    web_33, radio_33);
        });

        tv_continue.setOnClickListener(v -> {
            if (voltage_level[0].isEmpty()) {
                Toast.makeText(this, "Please select voltage level", Toast.LENGTH_SHORT).show();
                return;
            }

            payDialog.dismiss();

            Intent intent = new Intent(DashBoard.this, WebGis.class);
            intent.putExtra("VOLTAGE_LEVEL", voltage_level[0]);
            startActivity(intent);
        });

        payDialog.show();
    }


    private void selectItem(MaterialCardView selectedCard, RadioButton selectedRadio,
                            MaterialCardView card2, RadioButton radio2,
                            MaterialCardView card3, RadioButton radio3) {

        // Reset all
        reset(card2, radio2);
        reset(card3, radio3);

        // Select current
        selectedCard.setStrokeWidth(4);
        selectedCard.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        selectedCard.setCardElevation(8f);

        selectedRadio.setChecked(true);
    }

    private void reset(MaterialCardView card, RadioButton radio) {
        card.setStrokeWidth(1);
        card.setStrokeColor(getResources().getColor(android.R.color.darker_gray));
        card.setCardElevation(2f);

        radio.setChecked(false);
    }

    private void getDialogDataApi() {
        if (isDialogShown) return; // Prevent multiple calls
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getHelpPoints();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                getFeederLoadsPolesAPI();
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    dialogDataModels.clear();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                DialogDataModel model = new DialogDataModel();
                                model.setID(object.has("ID") && !object.get("ID").isJsonNull() ? object.get("ID").getAsString() : "");
                                model.setDESCRIPTION(object.has("DESCRIPTION") && !object.get("DESCRIPTION").isJsonNull() ? object.get("DESCRIPTION").getAsString() : "");
                                model.setRECORD_STATUS(object.has("RECORD_STATUS") && !object.get("RECORD_STATUS").isJsonNull() ? object.get("RECORD_STATUS").getAsString() : "");
                                model.setIS_ENABLED(object.has("IS_ENABLED") && !object.get("IS_ENABLED").isJsonNull() ? object.get("IS_ENABLED").getAsString() : "");
                                dialogDataModels.add(model);
                            }

                            if (dialogDataModels.get(0).getIS_ENABLED() != null) {
                                if (dialogDataModels.get(0).getIS_ENABLED().equalsIgnoreCase("false")) {



                                } else {
                                    if (!isDialogShown) {
                                        isDialogShown = true; // mark it shown
                                        CustomDialog.showCustomDialog(DashBoard.this, dialogDataModels);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}