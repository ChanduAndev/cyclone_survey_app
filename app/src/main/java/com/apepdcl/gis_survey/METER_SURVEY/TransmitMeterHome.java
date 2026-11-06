package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.models.AIResponseModel;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 10-06-2026.
 */
public class TransmitMeterHome extends AppCompatActivity {

    ImageView back_layout;
    TextView transmit_all,tv_no_of_services,tv_dtr_location_name,tv_dtr_code;
    MeterSurveyDatabase meterSurveyDatabase;

    List<MeterPostingModel> allMeterSurveyData;

    TextView tv_no_of_images, transmit_images;

    int pendingImagesCount = 0;
    int transmittedImagesCount = 0;

    int pendingImagesCountPole = 0;
    int transmittedImagesCountPole = 0;

    RetrofitServiceMeter retrofitServiceMeter;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transmit_meter_home);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);

        meterSurveyDatabase = new MeterSurveyDatabase(this);
        transmit_all = findViewById(R.id.transmit_all);
        tv_no_of_services = findViewById(R.id.tv_no_of_services);
        tv_dtr_location_name = findViewById(R.id.tv_dtr_location_name);
        tv_dtr_code = findViewById(R.id.tv_dtr_code);
        back_layout = findViewById(R.id.back_layout);
        transmit_images = findViewById(R.id.transmit_images);
        tv_no_of_images = findViewById(R.id.tv_no_of_images);
        back_layout.setOnClickListener(view -> onBackPressed());

        allMeterSurveyData =  meterSurveyDatabase.getAllMeterSurveyData();

        tv_no_of_services.setText(""+allMeterSurveyData.size());
        tv_dtr_code.setText(allMeterSurveyData.get(0).getDTR_CODE());
        tv_dtr_location_name.setText(allMeterSurveyData.get(0).getDTR_NAME());

        for (int i = 0; i < allMeterSurveyData.size(); i++) {
            if (allMeterSurveyData.get(i).getIS_IMAGE_TRANSMITTED().equalsIgnoreCase("N")){
                pendingImagesCount++;
            }
        }
        tv_no_of_images.setText(""+pendingImagesCount);
        if (pendingImagesCount>0){
            transmit_images.setVisibility(View.VISIBLE);
            transmit_all.setVisibility(View.GONE);
        }else {
            transmit_images.setVisibility(View.GONE);
            transmit_all.setVisibility(View.VISIBLE);
        }

        transmit_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getDbmsAccessTokenPole();
                //callCountTransmitDataAPI();
                setCountTransmitDataAPI();
            }
        });

        transmit_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transmit_images.post(() -> getDbmsAccessToken());
            }
        });
    }


    @SuppressLint("HardwareIds")
    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }

    private void setCountTransmitDataAPI() {
        progressDialog.show();

        List<PoleCountModel> freshPostingData = meterSurveyDatabase.getPoleCountData();

        System.out.println("TRANSMIT_DATA_IS::" + freshPostingData.size());

        if (freshPostingData.isEmpty()) {
            callTransmitDataAPI();
            return;
        }

        boolean isImageAvailable = false;
        pendingImagesCountPole = 0;

        for (PoleCountModel model : freshPostingData) {
            if (!TextUtils.isEmpty(model.getIMAGE())) {
                isImageAvailable = true;
                pendingImagesCountPole++;
            }
        }

        if (isImageAvailable) {
            getDbmsAccessTokenPole();
        } else {
            // No images available, proceed with transmit API
            callTransmitDataAPI();
        }
    }

//    private void setCountTransmitDataAPI() {
//        progressDialog.show();
//        List<PoleCountModel> freshPostingData = meterSurveyDatabase.getPoleCountData();
//
//
//        System.out.println("TRANSMIT_DATA_IS::"+freshPostingData.size());
//        if (freshPostingData.isEmpty()){
//            callTransmitDataAPI();
//            return;
//        }
//
//        boolean isImageAvailable = false;
//        for (int i = 0; i < freshPostingData.size(); i++) {
//            if (!freshPostingData.get(i).getIMAGE().isEmpty()){
//                isImageAvailable = true;
//                pendingImagesCountPole++;
//            }
//        }
//        if (isImageAvailable){
//            getDbmsAccessTokenPole();
//        }
//
//    }
    private void callCountTransmitDataAPI() {

        List<PoleCountModel> freshPostingData = meterSurveyDatabase.getPoleCountData();


        Gson gson = new Gson();
        String json = gson.toJson(freshPostingData);
        Log.e("COUNT_DATA", json);
        JsonArray array = new JsonArray();
       // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        for (int i = 0; i < freshPostingData.size(); i++) {
            PoleCountModel model = freshPostingData.get(i);
            JsonObject requestObject = new JsonObject();
            //requestObject.addProperty("APP_VERSION", MeterConstants.METER_APP_VERSION);
            requestObject.addProperty("POLE_ID", model.getCOUNT_POLE_ID());
            requestObject.addProperty("POLE_NO", model.getCOUNT_POLE_NO());
            requestObject.addProperty("COUNT", model.getCOUNT_TOTAL());
            requestObject.addProperty("REC_ID", model.getREC_ID());
            requestObject.addProperty("IMAGE", model.getIMAGE());

           // requestObject.addProperty("DEVICE_ID", getDeviceID(TransmitMeterHome.this));
            //requestObject.addProperty("CREATED_BY", sharedPreferences.getString(MeterConstants.METER_USER_ID,""));

            array.add(requestObject);

        }

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.transmitPoleCountData(array);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



                if (response.body() == null) {
                    progressDialog.dismiss();
                    Toast.makeText(TransmitMeterHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }



                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(TransmitMeterHome.this,
                            "Invalid response from server: STATUS missing",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(TransmitMeterHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    openAlertDialog(errorMessage);
                    return;
                }


                meterSurveyDatabase.deleteCountData();
                callTransmitDataAPI();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
    private void callTransmitDataAPI() {
       // progressDialog.show();
        List<MeterPostingModel> freshPostingData = meterSurveyDatabase.getAllMeterSurveyData();
        Gson gson = new Gson();
        String json = gson.toJson(freshPostingData);
        Log.e("TRANSMIT_DATA", json);
        JsonArray array = new JsonArray();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        for (int i = 0; i < freshPostingData.size(); i++) {
                MeterPostingModel model = freshPostingData.get(i);
                JsonObject requestObject = new JsonObject();
                requestObject.addProperty("APP_VERSION", MeterConstants.METER_APP_VERSION);
                requestObject.addProperty("GPS_ACCURACY",model.getGPS_ACCURACY());
                requestObject.addProperty("REF_ID", model.getREF_ID());
                requestObject.addProperty("SERVICE_NUMBER", model.getSERVICE_NUMBER());
                requestObject.addProperty("SC_NAME", model.getSC_NAME());
                requestObject.addProperty("UKSCNO", model.getUKSC_NO());
                requestObject.addProperty("POLE_ID", model.getPOLE_ID());
                requestObject.addProperty("POLE_NO", model.getPOLE_NO());
                requestObject.addProperty("DTR_NAME", model.getDTR_NAME());
                requestObject.addProperty("DTR_CODE", model.getDTR_CODE());
                requestObject.addProperty("LT_CKT_CODE", model.getLT_CKT_CODE());
                requestObject.addProperty("LATITUDE", model.getLATITUDE());
                requestObject.addProperty("LONGITUDE", model.getLONGITUDE());
                requestObject.addProperty("CURRENT_LAT", model.getCURRENT_LAT());
                requestObject.addProperty("CURRENT_LONG", model.getCURRENT_LON());
                requestObject.addProperty("IMAGE", model.getIMAGE());
                requestObject.addProperty("REMARKS", model.getREMARKS());
                requestObject.addProperty("SURVEY_MODE", model.getSURVEY_MODE());
                requestObject.addProperty("DEVICE_ID", getDeviceID(TransmitMeterHome.this));
                requestObject.addProperty("CREATED_BY", sharedPreferences.getString(MeterConstants.METER_USER_ID,""));
                requestObject.addProperty("SECTION_ID", sharedPreferences.getString(MeterConstants.METER_OFFICE_ID,""));

                array.add(requestObject);

        }

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.transmitData(array);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



                if (response.body() == null) {
                    progressDialog.dismiss();
                    Toast.makeText(TransmitMeterHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }



                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(TransmitMeterHome.this,
                            "Invalid response from server: STATUS missing",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(TransmitMeterHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    openAlertDialog(errorMessage);
                    return;
                }

                progressDialog.dismiss();
                meterSurveyDatabase.deleteAllPostingData();
                Toast.makeText(TransmitMeterHome.this, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TransmitMeterHome.this, MeterSurveyHome.class);
                startActivity(intent);
                finish();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void openAlertDialog(String message){
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
        LinearLayout estimate_details=view.findViewById(R.id.estimate_details);
        estimate_details.setVisibility(View.GONE);
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
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

    private void getDbmsAccessToken() {
        progressDialog.show();
        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.getImageAccessToken();

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



                if (response.body() == null) {
                    progressDialog.dismiss();
                    Toast.makeText(TransmitMeterHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }



                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(TransmitMeterHome.this, "Invalid response from server: STATUS missing", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(TransmitMeterHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                String accessToken = surveyedResponseVo.get("DATA").getAsString();
                setOfflineImagesData(accessToken);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void getDbmsAccessTokenPole() {
        progressDialog.show();
        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.getImageAccessToken();

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



                if (response.body() == null) {
                    progressDialog.dismiss();
                    Toast.makeText(TransmitMeterHome.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }



                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(TransmitMeterHome.this, "Invalid response from server: STATUS missing", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(TransmitMeterHome.this, errorMessage, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                String accessToken = surveyedResponseVo.get("DATA").getAsString();
                setOfflineImagesDataPole(accessToken);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setOfflineImagesData(String accessToken) {
        List<MeterPostingModel> imagePostingData = meterSurveyDatabase.getAllMeterSurveyData();
        for (int i = 0; i < imagePostingData.size(); i++) {
            if (imagePostingData.get(i).getIS_IMAGE_TRANSMITTED().equalsIgnoreCase("N")) {
                callImageSavingAPI(imagePostingData.get(i), accessToken);
            }
        }
    }

    private void setOfflineImagesDataPole(String accessToken) {
        List<PoleCountModel> imagePostingData = meterSurveyDatabase.getPoleCountData();
        for (int i = 0; i < imagePostingData.size(); i++) {
            if (imagePostingData.get(i).getIS_IMAGE_TRANSMITTED().equalsIgnoreCase("N")) {

                callImageSavingAPIPole(imagePostingData.get(i), accessToken);
            }
        }
    }
    public void callImageSavingAPIPole(PoleCountModel imagePostingModel, String accessToken) {

        try {
            Uri imageUri = Uri.parse(imagePostingModel.getIMAGE());
            File file = getFileFromUri(TransmitMeterHome.this, imageUri);

            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("upload_file", file.getName(), fileRequestBody);
            RequestBody tokenBody = RequestBody.create(MultipartBody.FORM, accessToken);
            RequestBody accountBody = RequestBody.create(MultipartBody.FORM, "2");
            RequestBody folderBody = RequestBody.create(MultipartBody.FORM, "okm:root/GIS_SrvCns_Images");

            ImageAPIServiceMeter apiService = ApiClient.getClient().create(ImageAPIServiceMeter.class);

            apiService.uploadImage(tokenBody, accountBody, folderBody, imagePart).enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("Image Response", response.body().toString());
                    if (response.isSuccessful()) {

                        JsonObject resObject = response.body();
                        JsonArray jsonArray = resObject.getAsJsonArray("data");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            String short_code = object.get("short_url").getAsString();
                            //  String download_code = object.get("download_url").getAsString();
                            Log.d("SHORT CODE", short_code);
                            //Log.d("UPLOAD", download_code);
                            meterSurveyDatabase.updateImageShortCodePoleCount(imagePostingModel.getCOUNT_POLE_ID(),short_code);
                            transmittedImagesCountPole++;

                            if (transmittedImagesCountPole==pendingImagesCountPole){
                                callCountTransmitDataAPI();
                            }



                        }

                    } else {
                        progressDialog.dismiss();
                        Log.e("UPLOAD", "Error Code : " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("UPLOAD", "Failed : " + t.getMessage());
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    public void callImageSavingAPI(MeterPostingModel imagePostingModel, String accessToken) {

        try {
            Uri imageUri = Uri.parse(imagePostingModel.getIMAGE());
            File file = getFileFromUri(TransmitMeterHome.this, imageUri);

            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("upload_file", file.getName(), fileRequestBody);
            RequestBody tokenBody = RequestBody.create(MultipartBody.FORM, accessToken);
            RequestBody accountBody = RequestBody.create(MultipartBody.FORM, "2");
            RequestBody folderBody = RequestBody.create(MultipartBody.FORM, "okm:root/GIS_SrvCns_Images");

            ImageAPIServiceMeter apiService = ApiClient.getClient().create(ImageAPIServiceMeter.class);

            apiService.uploadImage(tokenBody, accountBody, folderBody, imagePart).enqueue(new Callback<JsonObject>() {

                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d("Image Response", response.body().toString());
                            if (response.isSuccessful()) {

                                JsonObject resObject = response.body();
                                JsonArray jsonArray = resObject.getAsJsonArray("data");
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject object = jsonArray.get(i).getAsJsonObject();
                                    String short_code = object.get("short_url").getAsString();
                                  //  String download_code = object.get("download_url").getAsString();
                                    Log.d("SHORT CODE", short_code);
                                    //Log.d("UPLOAD", download_code);
                                    transmittedImagesCount++;
                                    meterSurveyDatabase.updateImageShortCode(imagePostingModel.getREF_ID(),short_code);

                                    if (transmittedImagesCount==pendingImagesCount){
                                        progressDialog.dismiss();
                                        tv_no_of_images.setText("0");
                                        transmit_images.setVisibility(View.GONE);
                                        transmit_all.setVisibility(View.VISIBLE);
                                        Toast.makeText(TransmitMeterHome.this, "All images transmitted. Now transmit service number data.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } else {
                                progressDialog.dismiss();
                                Log.e("UPLOAD", "Error Code : " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.e("UPLOAD", "Failed : " + t.getMessage());
                        }
                    });

        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    public File getFileFromUri(Context context, Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getCacheDir(), "upload_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return file;
    }
}
