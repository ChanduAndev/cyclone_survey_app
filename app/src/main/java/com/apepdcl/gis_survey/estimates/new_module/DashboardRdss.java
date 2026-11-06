package com.apepdcl.gis_survey.estimates.new_module;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.Profile;
import com.apepdcl.gis_survey.WebGis;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.BannerModel;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRdss extends AppCompatActivity {
    RetrofitServiceEstimates retrofitService;
    CardView rdssCard_new, web_gis_rdss;
    ImageSlider imageSlider;
    ImageView profile_layout_new;
    public static ArrayList<BannerModel> bannerList;
    GifImageView loader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_rdss_new);
        rdssCard_new = findViewById(R.id.rdssCard_new);
        web_gis_rdss = findViewById(R.id.web_gis_rdss);
        imageSlider = findViewById(R.id.image_slider);
        profile_layout_new = findViewById(R.id.profile_layout_new);
        loader = findViewById(R.id.loader_new_rdss);
        bannerList = new ArrayList<>();
        getBannerImagesApi();
        LoginActivity.SURVEY_FROM = "WO_RDSS";
        rdssCard_new.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardRdss.this, SurveyList.class);
            startActivity(intent);
        });

        web_gis_rdss.setOnClickListener(v -> {
            openWebGis();
        });

        profile_layout_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPf = new Intent(DashboardRdss.this, Profile.class);
                startActivity(intentPf);
                overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
            }
        });
    }

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

            Intent intent = new Intent(DashboardRdss.this, WebGis.class);
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

    private void getBannerImagesApi() {
        loader.setVisibility(View.VISIBLE);
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
                loader.setVisibility(View.GONE);
                //loader_layout.setVisibility(View.GONE);
                //main_ui_layout.setVisibility(View.VISIBLE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();



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
                CommanUtilits.netWorkError(DashboardRdss.this);
                loader.setVisibility(View.GONE);
            }
        });


    }

}
