package com.apepdcl.gis_survey.PMI.new_screens;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.METER_SURVEY.ApiClient;
import com.apepdcl.gis_survey.METER_SURVEY.ImageAPIServiceMeter;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.AIResponseModel;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 04-08-2025.
 */
public class Transmit11KV extends AppCompatActivity {

    RelativeLayout logout_layout;
    RelativeLayout profile_layout;
    String selectedImagePath = null;

    TextView transmit_data_11, poles_count, verify_offline_images_11;

    String shortURl = "";
    LinearLayout send_data_layout_11;
    private ArcGISDatabase arcGISDatabase;
    private RetrofitServicePMI retrofitServicePmi;
    ArrayList<PostingModel> postingList;
    PmiDatabase pmiDatabase;
    int poles_count_11;
    int offline_poles_count_11;
    int online_poles_count_11;

    int transmittedImagesCountPole = 0;
    int pendingImagesCountPole = 0;
    int sent11KVPoles = 0;
    TextView online_images_count;
    TextView offline_images_count;
    ArrayList<PostingModel> verifyOfflineImagesList;
    LinearLayout layout_verified_images;
    RecyclerView rv_verified_images;
    ArrayList<PostingModel> transmitList;
    int transmitPolesCount = 0;

    String SURVEY_TYPE = "";
    private final Object imageUploadLock = new Object();
    private int totalImagesToUpload = 0;
    private int completedImages = 0;
    private
    boolean transmissionStarted = false;
    private final Map<String, String> uploadedImageMap = new HashMap<>();

    private final Object offlineImageUploadLock = new Object();

    private int offlineTotalImageUploads = 0;
    private int offlineCompletedImageUploads = 0;
    private boolean offlineTransmissionStarted = false;

    // LOCAL IMAGE PATH -> DMS SHORT CODE
    private final Map<String, String> offlineUploadedShortCodeMap = new HashMap<>();
    private String aiImageShortUrl = "", aiDTRShortUrl = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmi_transmit_11);
        getSupportActionBar().hide();

        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);
        transmit_data_11 = findViewById(R.id.transmit_data_11);
        poles_count = findViewById(R.id.poles_count);
        send_data_layout_11 = findViewById(R.id.send_data_layout);
        verify_offline_images_11 = findViewById(R.id.verify_offline_images_11);
        offline_images_count = findViewById(R.id.offline_images_count);
        online_images_count = findViewById(R.id.online_images_count);
        layout_verified_images = findViewById(R.id.layout_verified_images);
        rv_verified_images = findViewById(R.id.rv_verified_images);
        poles_count_11 = 0;
        offline_poles_count_11 = 0;
        sent11KVPoles = 0;

        verifyOfflineImagesList = new ArrayList<>();
        transmitList = new ArrayList<>();

        pmiDatabase = new PmiDatabase(this);
        postingList = pmiDatabase.getPmiPostingData();
        arcGISDatabase = new ArcGISDatabase(this);


        for (int i = 0; i < postingList.size(); i++) {
            if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV")) {
                poles_count_11++;
            }
        }
        poles_count.setText(String.valueOf(poles_count_11));
        //logPostingList();
        // pmiDatabase.deletePmiPostingData();


        for (int i = 0; i < postingList.size(); i++) {
            if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV")) {
                if (postingList.get(i).getIMAGE_TYPE().equalsIgnoreCase("OFFLINE")) {
                    String imagePath = postingList.get(i).getAI_IMAGE();
                    String dtrImage = postingList.get(i).getDTR_IMAGE();
                    if ((TextUtils.isEmpty(imagePath)) && (TextUtils.isEmpty(dtrImage))) {
                        Log.e("OFFLINE_IMAGE_DEBUG", "BOTH IMAGE AND DTR_IMAGE EMPTY" + " | POLE_NO=" + postingList.get(i).getIMAGE() + " | POLE_ID=" + postingList.get(i).getPOLE_ID());
                        continue;
                    }

                }

            }
        }

        for (int i = 0; i < postingList.size(); i++) {
            if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV")) {
                if (postingList.get(i).getIMAGE_TYPE().equalsIgnoreCase("OFFLINE")) {
                    offline_poles_count_11++;

                } else {
                    online_poles_count_11++;
                }

            }
        }

        offline_images_count.setText("" + offline_poles_count_11);
        online_images_count.setText("" + online_poles_count_11);

        if (offline_poles_count_11 > 0) {
            transmit_data_11.setVisibility(View.GONE);
            verify_offline_images_11.setVisibility(View.VISIBLE);
        } else {
            transmit_data_11.setVisibility(View.VISIBLE);
            verify_offline_images_11.setVisibility(View.GONE);
        }

        verify_offline_images_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sent11KVPoles = 0;
                transmit_data_11.setVisibility(View.GONE);
                send_data_layout_11.setVisibility(View.VISIBLE);
                verify_offline_images_11.setVisibility(View.GONE);
                //verify_offline_images_11.post(() -> setVerifyOfflineImagesData());
                prepareDirectTransmitData();

            }
        });

        transmit_data_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transmit_data_11.setVisibility(View.GONE);
                send_data_layout_11.setVisibility(View.VISIBLE);
                send_data_layout_11.post(() -> setTransmitData());
            }
        });


        logout_layout.setOnClickListener(v -> onBackPressed());
        profile_layout.setOnClickListener(v -> onBackPressed());
    }


    //NEW One
    private void setVerifyOfflineImagesData() {

        pendingImagesCountPole = 0;
        sent11KVPoles = 0;

        transmitList.clear();
        verifyOfflineImagesList.clear();

        // Count only image-verification requests, not all database records.
        offline_poles_count_11 = 0;

        for (int i = 0; i < postingList.size(); i++) {

            PostingModel postingModel = postingList.get(i);

            if (postingModel == null) {
                continue;
            }

            if (!"11 KV".equalsIgnoreCase(
                    postingModel.getVOLTAGE_LEVEL())) {
                continue;
            }

            String surveyType = postingModel.getFROM();
            String aiFlag = postingModel.getAI_FLAG();
            String imageType = postingModel.getIMAGE_TYPE();

            String aiImage = postingModel.getAI_IMAGE();
            String dtrImage = postingModel.getDTR_IMAGE();

            boolean isRectify =
                    "RECTIFY".equalsIgnoreCase(surveyType);

            boolean isAI =
                    "AI".equalsIgnoreCase(aiFlag);

            Log.d("OFFLINE_IMAGE_DEBUG",
                    "INDEX=" + i
                            + " | POLE_NO=" + postingModel.getPOLE_NO()
                            + " | SURVEY_TYPE=" + surveyType
                            + " | AI_FLAG=" + aiFlag
                            + " | IMAGE_TYPE=" + imageType
                            + " | AI_IMAGE=" + aiImage
                            + " | IMAGE=" + postingModel.getIMAGE());

            /*
             * RECTIFY:
             * NON AI records must be transmitted directly.
             * They must not be sent to the AI verification API.
             */
            if (isRectify && !isAI) {

                if (!containsPostingModel(transmitList, postingModel)) {
                    transmitList.add(postingModel);
                }

                Log.d("RECTIFY_TRANSMIT",
                        "NON AI record added directly | POLE_NO="
                                + postingModel.getPOLE_NO());

                continue;
            }

            /*
             * Verify only AI records with an offline AI image.
             */
            if ("OFFLINE".equalsIgnoreCase(imageType)
                    && isAI
                    && (!TextUtils.isEmpty(aiImage)
                    || !TextUtils.isEmpty(dtrImage))) {

                pendingImagesCountPole++;

                // Each call to verifySingleImage counts as one verification.
                offline_poles_count_11++;

                verifyImage(postingModel);

                continue;
            }

            /*
             * Records that do not require AI verification must still
             * be included in transmission.
             */
            if (!containsPostingModel(transmitList, postingModel)) {
                transmitList.add(postingModel);
            }
        }

        Log.d("OFFLINE_IMAGE_DEBUG",
                "VERIFY STARTED"
                        + " | verificationRequests="
                        + offline_poles_count_11
                        + " | transmitList="
                        + transmitList.size());
    }


    private void prepareDirectTransmitData() {

        transmitList.clear();
        verifyOfflineImagesList.clear();
        pendingImagesCountPole = 0;
        offline_poles_count_11 = 0;
        sent11KVPoles = 0;

        for (PostingModel postingModel : postingList) {

            if (postingModel == null) {
                continue;
            }

            if (!"11 KV".equalsIgnoreCase(postingModel.getVOLTAGE_LEVEL())) {
                continue;
            }

            offline_poles_count_11++;

            if (!containsPostingModel(transmitList, postingModel)) {
                transmitList.add(postingModel);
            }

            Log.d("DIRECT_TRANSMIT",
                    "ADDED"
                            + " | POLE_NO=" + postingModel.getPOLE_NO()
                            + " | IMAGE_TYPE=" + postingModel.getIMAGE_TYPE()
                            + " | AI_FLAG=" + postingModel.getAI_FLAG()
                            + " | IMAGE=" + postingModel.getIMAGE()
                            + " | AI_IMAGE=" + postingModel.getAI_IMAGE()
                            + " | DTR_IMAGE=" + postingModel.getDTR_IMAGE());
        }

        Log.d("DIRECT_TRANSMIT",
                "PREPARED | transmitList=" + transmitList.size()
                        + " | poles=" + offline_poles_count_11);

        if (transmitList.isEmpty()) {
            Log.e("DIRECT_TRANSMIT", "TRANSMIT LIST EMPTY - nothing to send");
            Toast.makeText(Transmit11KV.this,
                    "No 11 KV data to transmit", Toast.LENGTH_SHORT).show();
            transmit_data_11.setVisibility(View.VISIBLE);
            send_data_layout_11.setVisibility(View.GONE);
            return;
        }

        // Now go straight to DMS token + image upload + transmit.
        setTransmitData();
    }




    private void getDmsTokenApi() {
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getDmsToken();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (!response.isSuccessful() || response.body() == null) {
                        Log.e("DMS_TOKEN", "DMS TOKEN FAILED" + " | CODE=" + response.code());
                        return;
                    }
                    JsonObject responseObject = response.body();
                    if (!responseObject.has("STATUS")) {
                        return;
                    }
                    if (MobileConstants.TRUE.equalsIgnoreCase(responseObject.get("STATUS").getAsString())) {
                        String accessToken = responseObject.get("DATA").getAsString();
                        setOfflineImagesData(accessToken, transmitList);
                    } else {
                        Log.e("DMS_TOKEN", "STATUS FALSE");
                    }
                } catch (Exception e) {
                    Log.e("DMS_TOKEN", "TOKEN RESPONSE ERROR", e);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("DMS_TOKEN", "TOKEN API FAILED", t);
            }
        });
    }

    private File getFileFromUri(Context context, String imagePath) {

        if (TextUtils.isEmpty(imagePath)) {
            return null;
        }

        try {

            // Case 1: Normal file path
            if (imagePath.startsWith("/")) {

                File file = new File(imagePath);

                if (file.exists() && file.isFile()) {
                    return file;
                }

                Log.e("IMAGE_UPLOAD", "File does not exist: " + imagePath);
                return null;
            }

            // Case 2: Content URI
            Uri uri = Uri.parse(imagePath);

            if ("content".equalsIgnoreCase(uri.getScheme())) {

                String fileName = "upload_" + System.currentTimeMillis() + ".jpg";

                File outputFile = new File(
                        context.getCacheDir(),
                        fileName
                );

                try (InputStream inputStream =
                             context.getContentResolver().openInputStream(uri);
                     OutputStream outputStream =
                             new FileOutputStream(outputFile)) {

                    if (inputStream == null) {
                        Log.e("IMAGE_UPLOAD", "InputStream is null: " + uri);
                        return null;
                    }

                    byte[] buffer = new byte[8192];
                    int length;

                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                }

                return outputFile;
            }

            // Case 3: file:// URI
            if ("file".equalsIgnoreCase(uri.getScheme())) {

                File file = new File(uri.getPath());

                if (file.exists() && file.isFile()) {
                    return file;
                }

                Log.e("IMAGE_UPLOAD", "File URI does not exist: " + uri);
                return null;
            }

            Log.e("IMAGE_UPLOAD", "Unsupported image URI: " + imagePath);

        } catch (Exception e) {
            Log.e("IMAGE_UPLOAD", "Error reading image: " + imagePath, e);
        }

        return null;
    }


    private void setOfflineImagesData(String accessToken, ArrayList<PostingModel> transmitList) {
        transmissionStarted = false;
        completedImages = 0;
        totalImagesToUpload = 0;
        uploadedImageMap.clear();
        callImageSavingAPI(transmitList, accessToken);
        //callImageSavingAPI(postingList, accessToken);
    }

    private void setTransmitData() {

        if (transmitList == null || transmitList.isEmpty()) {
            Log.e("TRANSMIT", "setTransmitData called with EMPTY transmitList");
            Toast.makeText(Transmit11KV.this,
                    "No data to transmit", Toast.LENGTH_SHORT).show();
            transmit_data_11.setVisibility(View.VISIBLE);
            send_data_layout_11.setVisibility(View.GONE);
            return;
        }

        getDmsTokenApi();
    }

    private void markOfflineImageUploadCompleted() {

        synchronized (imageUploadLock) {

            completedImages++;

            Log.d(
                    "IMAGE_UPLOAD",
                    "COMPLETED=" + completedImages
                            + "/" + totalImagesToUpload
            );

            if (completedImages >= totalImagesToUpload
                    && !transmissionStarted) {

                transmissionStarted = true;

                Log.d(
                        "IMAGE_UPLOAD",
                        "ALL IMAGE UPLOADS COMPLETED"
                );

                runOnUiThread(() -> {


                    updateOfflineImageShortCodes(transmitList);

                    logFinalOfflineImages();


                    Log.d(
                            "OFFLINE_MAP_CHECK",
                            "======================================"
                    );

                    for (Map.Entry<String, String> entry
                            : offlineUploadedShortCodeMap.entrySet()) {

                        Log.d(
                                "OFFLINE_MAP_CHECK",
                                "LOCAL=" + entry.getKey()
                                        + " | SHORT=" + entry.getValue()
                        );
                    }


                    transmitPoles11KV(transmitList);
                });
            }
        }
    }

//    private void markOfflineImageUploadCompleted() {
//        synchronized (imageUploadLock) {
//            completedImages++;
//            Log.d("IMAGE_UPLOAD", "COMPLETED=" + completedImages + "/" + totalImagesToUpload);
//            if (completedImages >= totalImagesToUpload && !transmissionStarted) {
//                transmissionStarted = true;
//                Log.d("IMAGE_UPLOAD", "ALL IMAGE UPLOADS COMPLETED");
//                runOnUiThread(() -> {
//                    logFinalOfflineImages();
//                    transmitPoles11KV(transmitList);
//                    Log.d("OFFLINE_MAP_CHECK",
//                            "======================================");
//
//                    for (Map.Entry<String, String> entry
//                            : offlineUploadedShortCodeMap.entrySet()) {
//
//                        Log.d(
//                                "OFFLINE_MAP_CHECK",
//                                "LOCAL=" + entry.getKey()
//                                        + " | SHORT=" + entry.getValue()
//                        );
//                    }
//                });
//            }
//        }
//    }

    private String getUploadedShortCode(String imagePath) {

        if (TextUtils.isEmpty(imagePath)) {
            return "";
        }

        String path = imagePath.trim();

        String shortCode = offlineUploadedShortCodeMap.get(path);

        if (!TextUtils.isEmpty(shortCode)) {
            return shortCode;
        }

        // Already a short code or upload was not found
        return path;
    }

    private void updateOfflineImageShortCodes(
            List<PostingModel> postingModels) {

        if (postingModels == null || postingModels.isEmpty()) {
            return;
        }

        for (PostingModel model : postingModels) {

            if (model == null) {
                continue;
            }

            String oldImage = model.getIMAGE();

            if (!TextUtils.isEmpty(oldImage)) {

                String newImage = getUploadedShortCode(oldImage);

                if (!oldImage.equals(newImage)) {

                    Log.d(
                            "AI_IMAGE_FIX",
                            "IMAGE | ID=" + model.getID()
                                    + " | TYPE=" + model.getDEFECT_TYPE()
                                    + " | OLD=" + oldImage
                                    + " | NEW=" + newImage
                    );

                    model.setIMAGE(newImage);
                }
            }


            String oldAiImage = model.getAI_IMAGE();

            if (!TextUtils.isEmpty(oldAiImage)) {

                String newAiImage = getUploadedShortCode(oldAiImage);

                if (!oldAiImage.equals(newAiImage)) {

                    Log.d(
                            "AI_IMAGE_FIX",
                            "AI_IMAGE | ID=" + model.getID()
                                    + " | TYPE=" + model.getDEFECT_TYPE()
                                    + " | OLD=" + oldAiImage
                                    + " | NEW=" + newAiImage
                    );

                    model.setAI_IMAGE(newAiImage);
                    aiImageShortUrl = newAiImage;
                }
            }


            String oldDtrImage = model.getDTR_IMAGE();

            if (!TextUtils.isEmpty(oldDtrImage)) {

                String newDtrImage = getUploadedShortCode(oldDtrImage);

                if (!oldDtrImage.equals(newDtrImage)) {

                    Log.d(
                            "AI_IMAGE_FIX",
                            "DTR_IMAGE | ID=" + model.getID()
                                    + " | TYPE=" + model.getDEFECT_TYPE()
                                    + " | OLD=" + oldDtrImage
                                    + " | NEW=" + newDtrImage
                    );

                    model.setDTR_IMAGE(newDtrImage);
                    aiDTRShortUrl = newDtrImage;
                }
            }
        }
    }

    private void logFinalOfflineImages() {

        for (PostingModel model : transmitList) {
            if (model == null) {
                continue;
            }
            Log.d("FINAL_OFFLINE_IMAGES", "ID=" + model.getID() + " | POLE=" + model.getPOLE_NO() + " | TYPE=" + model.getDEFECT_TYPE() + " | SUBTYPE=" + model.getDEFECT_SUB_TYPE() + " | IMAGE=" + model.getIMAGE() + " | AI_IMAGE=" + model.getAI_IMAGE() + " | DTR_IMAGE=" + model.getDTR_IMAGE());
        }

    }

    public void callImageSavingAPI(ArrayList<PostingModel> postingModel, String accessToken) {
        try {
            Map<String, ArrayList<PostingModel>> imageModelMap = new LinkedHashMap<>();
            Set<String> dtrImagePaths = new HashSet<>();
            for (PostingModel model : postingModel) {
                if (model == null) {
                    continue;
                }
                String imagePath = model.getIMAGE();
                if (!TextUtils.isEmpty(imagePath)) {
                    ArrayList<PostingModel> models = imageModelMap.get(imagePath);
                    if (models == null) {
                        models = new ArrayList<>();
                        imageModelMap.put(imagePath, models);
                    }
                    if (!models.contains(model)) {
                        models.add(model);
                    }
                    Log.d("DEFECT_IMAGE_MAP", "NORMAL" + " | GROUP=" + model.getDEFECT_GROUP() + " | TYPE=" + model.getDEFECT_TYPE() + " | SUBTYPE=" + model.getDEFECT_SUB_TYPE() + " | PATH=" + imagePath);
                }
                String aiImagePath = model.getAI_IMAGE();
                if (!TextUtils.isEmpty(aiImagePath)) {
                    ArrayList<PostingModel> models = imageModelMap.get(aiImagePath);
                    if (models == null) {
                        models = new ArrayList<>();
                        imageModelMap.put(aiImagePath, models);
                    }
                    if (!models.contains(model)) {
                        models.add(model);
                    }
                    Log.d("AI_IMAGE_MAP", "GROUP=" + model.getDEFECT_GROUP() + " | TYPE=" + model.getDEFECT_TYPE() + " | PATH=" + aiImagePath);
                }
                String dtrImagePath = model.getDTR_IMAGE();
                if (!TextUtils.isEmpty(dtrImagePath)) {
                    ArrayList<PostingModel> models = imageModelMap.get(dtrImagePath);
                    if (models == null) {
                        models = new ArrayList<>();
                        imageModelMap.put(dtrImagePath, models);
                    }
                    if (!models.contains(model)) {
                        models.add(model);
                    }
                    dtrImagePaths.add(dtrImagePath);
                    Log.d("DTR_IMAGE_MAP", "GROUP=" + model.getDEFECT_GROUP() + " | TYPE=" + model.getDEFECT_TYPE() + " | PATH=" + dtrImagePath);
                }
            }
            totalImagesToUpload = imageModelMap.size();
            completedImages = 0;
            transmissionStarted = false;
            Log.d("IMAGE_UPLOAD", "TOTAL UNIQUE IMAGES = " + totalImagesToUpload);
            if (totalImagesToUpload == 0) {
                Log.d("IMAGE_UPLOAD", "NO IMAGES TO UPLOAD");
                transmitPoles11KV(transmitList);
                Log.d("OFFLINE_MAP_CHECK",
                        "======================================");

                for (Map.Entry<String, String> entry
                        : offlineUploadedShortCodeMap.entrySet()) {

                    Log.d(
                            "OFFLINE_MAP_CHECK",
                            "LOCAL=" + entry.getKey()
                                    + " | SHORT=" + entry.getValue()
                    );
                }
                return;
            }
            for (Map.Entry<String, ArrayList<PostingModel>> entry : imageModelMap.entrySet()) {
                final String imagePath = entry.getKey();
                final ArrayList<PostingModel> models = entry.getValue();
                final boolean isDtrImage = dtrImagePaths.contains(imagePath);
                Log.d("IMAGE_UPLOAD", "STARTING" + " | DTR=" + isDtrImage + " | PATH=" + imagePath + " | MODELS=" + models.size());
                File file = getFileFromUri(Transmit11KV.this, imagePath); /* * Missing file must still complete counter. */
                if (file == null || !file.exists() || !file.isFile()) {
                    Log.e("IMAGE_UPLOAD", "FILE NOT FOUND" + " | PATH=" + imagePath);
                    markOfflineImageUploadCompleted();
                    continue;
                }
                RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("upload_file", file.getName(), fileRequestBody);
                RequestBody tokenBody = RequestBody.create(MultipartBody.FORM, accessToken);
                RequestBody accountBody = RequestBody.create(MultipartBody.FORM, "2");
                RequestBody folderBody = RequestBody.create(MultipartBody.FORM, "okm:root/EPCCBDOCREP/GIS_11KV/PMI");
                ImageAPIServiceMeter apiService = ApiClient.getClient().create(ImageAPIServiceMeter.class);
                apiService.uploadImage(tokenBody, accountBody, folderBody, imagePart).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            if (!response.isSuccessful() || response.body() == null) {
                                Log.e("IMAGE_UPLOAD", "HTTP ERROR=" + response.code() + " | PATH=" + imagePath);
                                return;
                            }
                            JsonObject responseObject = response.body();
                            Log.d("IMAGE_UPLOAD_RESPONSE", imagePath + " -> " + responseObject);
                            if (!responseObject.has("data") || responseObject.get("data").isJsonNull() || !responseObject.get("data").isJsonArray()) {
                                Log.e("IMAGE_UPLOAD", "DATA NOT FOUND" + " | PATH=" + imagePath);
                                return;
                            }
                            JsonArray jsonArray = responseObject.getAsJsonArray("data");
                            if (jsonArray.size() == 0) {
                                Log.e("IMAGE_UPLOAD", "DATA EMPTY" + " | PATH=" + imagePath);
                                return;
                            }
                            JsonObject object = jsonArray.get(0).getAsJsonObject();
                            if (!object.has("short_url") || object.get("short_url").isJsonNull()) {
                                Log.e("IMAGE_UPLOAD", "SHORT URL NOT FOUND" + " | PATH=" + imagePath);
                                return;
                            }
                            String shortCode = object.get("short_url").getAsString();
                            Log.d("SHORT_CODE", "LOCAL=" + imagePath + " | SHORT=" + shortCode);
                            uploadedImageMap.put(imagePath, shortCode);
                            synchronized (imageUploadLock) {
                                for (PostingModel model : models) {
                                    if (model == null) {
                                        continue;
                                    }
                                    if (imagePath.equals(model.getIMAGE())) {
                                        model.setIMAGE(shortCode);
                                        Log.d("OFFLINE_IMAGE_UPDATED", "NORMAL" + " | ID=" + model.getPOLE_NO() + " | TYPE=" + model.getDEFECT_TYPE() + " | OLD=" + imagePath + " | NEW=" + shortCode);
                                    }
                                    if (imagePath.equals(model.getAI_IMAGE())) {
                                        model.setAI_IMAGE(shortCode);
                                        Log.d("OFFLINE_IMAGE_UPDATED", "AI" + " | ID=" + model.getPOLE_NO() + " | TYPE=" + model.getDEFECT_TYPE() + " | OLD=" + imagePath + " | NEW=" + shortCode);
                                    }
                                    if (imagePath.equals(model.getDTR_IMAGE())) {
                                        model.setDTR_IMAGE(shortCode);
                                        Log.d("OFFLINE_IMAGE_UPDATED", "DTR" + " | ID=" + model.getPOLE_NO() + " | TYPE=" + model.getDEFECT_TYPE() + " | OLD=" + imagePath + " | NEW=" + shortCode);
                                    }
                                }
                            }
                            synchronized (offlineImageUploadLock) {

                                offlineUploadedShortCodeMap.put(
                                        imagePath.trim(),
                                        shortCode.trim()
                                );
                            }
                        } catch (Exception e) {
                            Log.e("IMAGE_UPLOAD", "RESPONSE ERROR" + " | PATH=" + imagePath, e);
                        } finally {
                            markOfflineImageUploadCompleted();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("IMAGE_UPLOAD", "UPLOAD FAILED" + " | PATH=" + imagePath, t);
                        markOfflineImageUploadCompleted();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("IMAGE_UPLOAD", "UPLOAD EXCEPTION", e);
        }
    }

    private void processAIResponse(
            PostingModel selectedPostingModel,
            String selectedImagePath,
            String imageType,
            JsonObject object) {

        try {

            if (selectedPostingModel == null) {
                Log.e("VERIFY_IMAGE", "selectedPostingModel is NULL");
                return;
            }

            Log.d(
                    "PROCESS_AI_RESPONSE",
                    "START"
                            + " | POLE_NO=" + selectedPostingModel.getPOLE_NO()
                            + " | POLE_ID=" + selectedPostingModel.getPOLE_ID()
                            + " | TYPE=" + imageType
                            + " | IMAGE_PATH=" + selectedImagePath
            );

            PostingModel model =
                    createBasePostingModel(
                            selectedPostingModel,
                            selectedImagePath
                    );

            ArrayList<AIResponseModel> aiList =
                    new ArrayList<>();

            String status =
                    object.has("status")
                            && !object.get("status").isJsonNull()
                            ? object.get("status").getAsString()
                            : "";

            Log.d(
                    "PROCESS_AI_RESPONSE",
                    "STATUS"
                            + " | POLE_NO="
                            + selectedPostingModel.getPOLE_NO()
                            + " | TYPE=" + imageType
                            + " | STATUS=" + status
            );

            /*
             * ============================================================
             * SUCCESS
             * ============================================================
             */
            if ("done".equalsIgnoreCase(status)) {

                /*
                 * --------------------------------------------------------
                 * POLE LEAN DETECTIONS
                 * --------------------------------------------------------
                 */
                if (object.has("pole_lean_detections")
                        && !object.get("pole_lean_detections").isJsonNull()
                        && object.get("pole_lean_detections").isJsonArray()) {

                    JsonArray poleLeanDetections =
                            object.getAsJsonArray("pole_lean_detections");

                    for (JsonElement element : poleLeanDetections) {

                        if (element == null
                                || element.isJsonNull()
                                || !element.isJsonObject()) {
                            continue;
                        }

                        JsonObject detection =
                                element.getAsJsonObject();

                        AIResponseModel ai =
                                new AIResponseModel();

                        ai.setAI_RESPONSE(
                                getJsonString(
                                        detection,
                                        "class_name"
                                )
                        );

                        ai.setLEAN_ANGLE(
                                getJsonString(
                                        detection,
                                        "angle_deg"
                                )
                        );

                        ai.setPOSITION("pole_0");

                        ai.setVOLTAGE("");

                        ai.setPOSTITION_XARM(
                                getJsonString(
                                        detection,
                                        "arm_id"
                                )
                        );

                        ai.setPOLE_BBOX(
                                getJsonArrayAsString(
                                        detection,
                                        "bbox"
                                )
                        );

                        /*
                         * Internal type only.
                         *
                         * Final RESPONSE[].IMAGE is generated
                         * during transmit from posting.getAI_IMAGE().
                         */
                        ai.setIMAGE(imageType);

                        aiList.add(ai);

                        Log.d(
                                "AI_RESPONSE_ITEM",
                                "POLE_LEAN"
                                        + " | POLE_NO="
                                        + selectedPostingModel.getPOLE_NO()
                                        + " | TYPE=" + imageType
                                        + " | CLASS="
                                        + ai.getAI_RESPONSE()
                        );
                    }
                }


                /*
                 * --------------------------------------------------------
                 * CROPS
                 * --------------------------------------------------------
                 */
                if (object.has("crops")
                        && !object.get("crops").isJsonNull()
                        && object.get("crops").isJsonArray()) {

                    JsonArray crops =
                            object.getAsJsonArray("crops");

                    for (JsonElement element : crops) {

                        if (element == null
                                || element.isJsonNull()
                                || !element.isJsonObject()) {
                            continue;
                        }

                        JsonObject cropObj =
                                element.getAsJsonObject();

                        /*
                         * ------------------------------------------------
                         * CROP WITH DETECTIONS
                         * ------------------------------------------------
                         */
                        if (cropObj.has("detections")
                                && !cropObj.get("detections").isJsonNull()
                                && cropObj.get("detections").isJsonArray()) {

                            JsonArray detections =
                                    cropObj.getAsJsonArray("detections");

                            for (JsonElement detElement : detections) {

                                if (detElement == null
                                        || detElement.isJsonNull()
                                        || !detElement.isJsonObject()) {
                                    continue;
                                }

                                JsonObject detection =
                                        detElement.getAsJsonObject();

                                AIResponseModel ai =
                                        new AIResponseModel();

                                String labelClass = "";

                                /*
                                 * LABEL
                                 */
                                if (detection.has("labels")
                                        && !detection.get("labels").isJsonNull()
                                        && detection.get("labels").isJsonArray()) {

                                    JsonArray labels =
                                            detection.getAsJsonArray("labels");

                                    if (labels.size() > 0
                                            && !labels.get(0).isJsonNull()
                                            && labels.get(0).isJsonObject()) {

                                        JsonObject label =
                                                labels.get(0).getAsJsonObject();

                                        labelClass =
                                                getJsonString(
                                                        label,
                                                        "class"
                                                );
                                    }
                                }

                                /*
                                 * VOLTAGE
                                 */
                                String voltage = "";

                                if (detection.has("voltage")
                                        && !detection.get("voltage").isJsonNull()
                                        && detection.get("voltage").isJsonObject()) {

                                    JsonObject voltageObject =
                                            detection.getAsJsonObject("voltage");

                                    voltage =
                                            getJsonString(
                                                    voltageObject,
                                                    "class"
                                            );
                                }

                                ai.setAI_RESPONSE(labelClass);

                                ai.setLEAN_ANGLE("");

                                ai.setPOSITION(
                                        getJsonString(
                                                cropObj,
                                                "pole_id"
                                        )
                                );

                                ai.setVOLTAGE(voltage);

                                ai.setPOSTITION_XARM(
                                        getJsonString(
                                                detection,
                                                "arm_id"
                                        )
                                );

                                ai.setCROP_BBOX(
                                        getJsonArrayAsString(
                                                detection,
                                                "bbox"
                                        )
                                );

                                /*
                                 * Internal P/D marker.
                                 */
                                ai.setIMAGE(imageType);

                                aiList.add(ai);

                                Log.d(
                                        "AI_RESPONSE_ITEM",
                                        "CROP"
                                                + " | POLE_NO="
                                                + selectedPostingModel.getPOLE_NO()
                                                + " | TYPE=" + imageType
                                                + " | CLASS="
                                                + ai.getAI_RESPONSE()
                                                + " | POSITION="
                                                + ai.getPOSITION()
                                );
                            }

                        }

                        /*
                         * ------------------------------------------------
                         * REJECTED CROP
                         * ------------------------------------------------
                         */
                        else if (cropObj.has("status")
                                && "rejected_crop".equalsIgnoreCase(
                                getJsonString(
                                        cropObj,
                                        "status"
                                )
                        )) {

                            AIResponseModel ai =
                                    new AIResponseModel();

                            String reason =
                                    getJsonString(
                                            cropObj,
                                            "reason"
                                    );

                            if (TextUtils.isEmpty(reason)) {
                                reason = "REJECTED";
                            }

                            ai.setAI_RESPONSE(reason);

                            ai.setLEAN_ANGLE("");

                            ai.setPOSITION(
                                    getJsonString(
                                            cropObj,
                                            "pole_id"
                                    )
                            );

                            ai.setVOLTAGE("");

                            ai.setPOSTITION_XARM("");

                            ai.setCROP_BBOX("");

                            /*
                             * Internal P/D marker.
                             */
                            ai.setIMAGE(imageType);

                            aiList.add(ai);

                            Log.d(
                                    "AI_RESPONSE_ITEM",
                                    "REJECTED_CROP"
                                            + " | POLE_NO="
                                            + selectedPostingModel.getPOLE_NO()
                                            + " | TYPE=" + imageType
                                            + " | REASON=" + reason
                            );
                        }
                    }
                }


                /*
                 * --------------------------------------------------------
                 * CLEARANCE RESULT
                 * --------------------------------------------------------
                 *
                 * This is normally associated with DTR/plinth processing.
                 */
                if (object.has("clearance_result")
                        && !object.get("clearance_result").isJsonNull()
                        && object.get("clearance_result").isJsonObject()) {

                    JsonObject clearance =
                            object.getAsJsonObject("clearance_result");

                    AIResponseModel ai =
                            new AIResponseModel();

                    ai.setAI_RESPONSE(
                            getJsonString(
                                    clearance,
                                    "status"
                            )
                    );

                    ai.setPOLE_RATIO(
                            getJsonString(
                                    clearance,
                                    "pole_height_px"
                            )
                    );

                    ai.setPLINTH_RATIO(
                            getJsonString(
                                    clearance,
                                    "plinth_height_px"
                            )
                    );

                    /*
                     * Internal P/D marker.
                     */
                    ai.setIMAGE(imageType);

                    aiList.add(ai);

                    Log.d(
                            "AI_RESPONSE_ITEM",
                            "CLEARANCE"
                                    + " | POLE_NO="
                                    + selectedPostingModel.getPOLE_NO()
                                    + " | TYPE=" + imageType
                                    + " | STATUS="
                                    + ai.getAI_RESPONSE()
                    );
                }


                /*
                 * --------------------------------------------------------
                 * VEGETATION CLEARANCE
                 * --------------------------------------------------------
                 */
                if (object.has("vegetation_clearance")
                        && !object.get("vegetation_clearance").isJsonNull()
                        && object.get("vegetation_clearance").isJsonObject()) {

                    JsonObject vegetation =
                            object.getAsJsonObject(
                                    "vegetation_clearance"
                            );

                    AIResponseModel ai =
                            new AIResponseModel();

                    ai.setAI_RESPONSE(
                            getJsonString(
                                    vegetation,
                                    "reason"
                            )
                    );

                    ai.setVD_DEPTH(
                            getJsonString(
                                    vegetation,
                                    "decision_depth"
                            )
                    );

                    ai.setVC_REASON(
                            getJsonString(
                                    vegetation,
                                    "reason"
                            )
                    );

                    /*
                     * Internal P/D marker.
                     */
                    ai.setIMAGE(imageType);

                    aiList.add(ai);

                    Log.d(
                            "AI_RESPONSE_ITEM",
                            "VEGETATION"
                                    + " | POLE_NO="
                                    + selectedPostingModel.getPOLE_NO()
                                    + " | TYPE=" + imageType
                                    + " | RESPONSE="
                                    + ai.getAI_RESPONSE()
                    );
                }


                /*
                 * --------------------------------------------------------
                 * SAVE SUCCESS
                 * --------------------------------------------------------
                 */

                model.setAiResponseModel(aiList);

                /*
                 * VERY IMPORTANT:
                 *
                 * Keep the common AI image of this pole.
                 *
                 * createBasePostingModel() should also copy:
                 *
                 * model.setAI_IMAGE(source.getAI_IMAGE());
                 */
                model.setAI_IMAGE(
                        selectedPostingModel.getAI_IMAGE()
                );

                /*
                 * Keep DTR image also if this is a DTR pole.
                 */
                if (!TextUtils.isEmpty(
                        selectedPostingModel.getDTR_IMAGE())) {

                    model.setDTR_IMAGE(
                            selectedPostingModel.getDTR_IMAGE()
                    );
                }

                /*
                 * For the AI_IMAGE API response,
                 * the model image remains the AI image.
                 *
                 * For DTR API response, model's DTR_IMAGE
                 * remains the DTR image.
                 */

                Log.d(
                        "PROCESS_AI_RESPONSE",
                        "SUCCESS"
                                + " | POLE_NO="
                                + selectedPostingModel.getPOLE_NO()
                                + " | TYPE=" + imageType
                                + " | AI_IMAGE="
                                + model.getAI_IMAGE()
                                + " | DTR_IMAGE="
                                + model.getDTR_IMAGE()
                                + " | RESPONSE_COUNT="
                                + aiList.size()
                );

                /*
                 * Original offline record can be deleted after
                 * successful processing.
                 */
                pmiDatabase.deleteOfflineImage(
                        selectedPostingModel.getID()
                );

                selectedPostingModel.setIMAGE_TYPE("ONLINE");

                /*
                 * Keep the original posting in verification list.
                 */
                if (!containsPostingModel(
                        verifyOfflineImagesList,
                        selectedPostingModel)) {

                    verifyOfflineImagesList.add(
                            selectedPostingModel
                    );
                }

                /*
                 * Add generated model to transmit list.
                 */
                addToTransmitListIfAbsent(model);

            }


            /*
             * ============================================================
             * AI FAILED / NO POLES
             * ============================================================
             */
            else {

                String reason;

                if ("no_poles".equalsIgnoreCase(status)) {

                    reason = "NO POLES DETECTED";

                } else {

                    reason =
                            getJsonString(
                                    object,
                                    "reason"
                            );

                    if (TextUtils.isEmpty(reason)) {
                        reason = "AI PROCESSING FAILED";
                    }
                }

                /*
                 * Update original model.
                 */
                selectedPostingModel.setAI_RESPONSE(reason);

                selectedPostingModel.setIMAGE_TYPE("OFFLINE");

                /*
                 * Update generated model.
                 */
                model.setAI_RESPONSE(reason);

                model.setIMAGE_TYPE("OFFLINE");

                /*
                 * Keep the pole's AI image.
                 */
                model.setAI_IMAGE(
                        selectedPostingModel.getAI_IMAGE()
                );

                /*
                 * Keep DTR image if present.
                 */
                if (!TextUtils.isEmpty(
                        selectedPostingModel.getDTR_IMAGE())) {

                    model.setDTR_IMAGE(
                            selectedPostingModel.getDTR_IMAGE()
                    );
                }

                AIResponseModel ai =
                        new AIResponseModel();

                ai.setAI_RESPONSE(reason);

                /*
                 * Internal P/D marker.
                 */
                ai.setIMAGE(imageType);

                aiList.add(ai);

                model.setAiResponseModel(aiList);

                Log.e(
                        "PROCESS_AI_RESPONSE",
                        "AI PROCESSING FAILED"
                                + " | POLE_NO="
                                + selectedPostingModel.getPOLE_NO()
                                + " | TYPE=" + imageType
                                + " | REASON=" + reason
                );

                addToTransmitListIfAbsent(model);

                if (!containsPostingModel(
                        verifyOfflineImagesList,
                        selectedPostingModel)) {

                    verifyOfflineImagesList.add(
                            selectedPostingModel
                    );
                }
            }

        } catch (Exception e) {

            Log.e(
                    "PROCESS_AI_RESPONSE",
                    "ERROR"
                            + " | POLE_NO="
                            + (selectedPostingModel != null
                            ? selectedPostingModel.getPOLE_NO()
                            : "NULL")
                            + " | TYPE=" + imageType,
                    e
            );
        }
    }

    private void verifySingleImage(
            PostingModel selectedPostingModel,
            String selectedImagePath,
            String imageType) {

        if (TextUtils.isEmpty(selectedImagePath)) {
            return;
        }

        File file = new File(selectedImagePath);

        if (!file.exists() || !file.isFile()) {
            Log.e(
                    "VERIFY_IMAGE",
                    "FILE INVALID"
                            + " | POLE_NO=" + selectedPostingModel.getPOLE_NO()
                            + " | TYPE=" + imageType
                            + " | PATH=" + selectedImagePath
            );

            sent11KVPoles++;
            checkOfflineVerificationCompleted();
            return;
        }

        Log.d(
                "VERIFY_IMAGE",
                "UPLOADING"
                        + " | POLE_NO=" + selectedPostingModel.getPOLE_NO()
                        + " | TYPE=" + imageType
                        + " | PATH=" + selectedImagePath
        );

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        MultipartBody.Part imagePart =
                MultipartBody.Part.createFormData(
                        "file",
                        file.getName(),
                        requestFile
                );

        ImageRetrofitService service =
                ImageRetrofitClient
                        .getRetrofitInstance()
                        .create(ImageRetrofitService.class);

        Call<JsonObject> call;

        if ("D".equalsIgnoreCase(imageType)) {
            call = service.uploadImageDTRPlinthClearance(
                    imagePart,
                    "9.1"
            );
        } else {
            call = service.uploadImage(
                    imagePart,
                    false
            );
        }

        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(
                    Call<JsonObject> call,
                    Response<JsonObject> response) {

                try {

                    if (!response.isSuccessful()
                            || response.body() == null) {

                        Log.e(
                                "VERIFY_IMAGE",
                                "AI UPLOAD FAILED"
                                        + " | POLE_NO="
                                        + selectedPostingModel.getPOLE_NO()
                                        + " | TYPE=" + imageType
                                        + " | CODE=" + response.code()
                        );

                        return;
                    }

                    JsonObject object = response.body();

                    Log.d(
                            "VERIFY_IMAGE_RESPONSE",
                            "POLE_NO="
                                    + selectedPostingModel.getPOLE_NO()
                                    + " | TYPE=" + imageType
                                    + " | PATH=" + selectedImagePath
                                    + " -> " + object
                    );
                    // to_be_verified_with_ai_count++;
                    processAIResponse(
                            selectedPostingModel,
                            selectedImagePath,
                            imageType,
                            object
                    );

                } catch (Exception e) {

                    Log.e(
                            "VERIFY_IMAGE",
                            "AI RESPONSE PROCESSING ERROR"
                                    + " | POLE_NO="
                                    + selectedPostingModel.getPOLE_NO()
                                    + " | TYPE=" + imageType,
                            e
                    );

                } finally {
                    sent11KVPoles++;
                    checkOfflineVerificationCompleted();
                }
            }

            @Override
            public void onFailure(
                    Call<JsonObject> call,
                    Throwable t) {

                Log.e(
                        "VERIFY_IMAGE",
                        "AI API FAILED"
                                + " | POLE_NO="
                                + selectedPostingModel.getPOLE_NO()
                                + " | TYPE=" + imageType
                                + " | PATH=" + selectedImagePath,
                        t
                );

                sent11KVPoles++;
                checkOfflineVerificationCompleted();
            }
        });
    }

    private void verifyImage(PostingModel selectedPostingModel) {
        if (selectedPostingModel == null) {
            Log.e("VERIFY_IMAGE", "selectedPostingModel is NULL");
            return;
        }
        String aiImage = selectedPostingModel.getAI_IMAGE();
        String dtrImage = selectedPostingModel.getDTR_IMAGE();

        boolean hasAIImage = !TextUtils.isEmpty(aiImage);
        boolean hasDTRImage = !TextUtils.isEmpty(dtrImage);

        String image_type;
        String selectedImagePath = null;
        if (!TextUtils.isEmpty(aiImage)) {
            selectedImagePath = aiImage;
            image_type = "P";
        } else if (!TextUtils.isEmpty(dtrImage)) {
            selectedImagePath = dtrImage;
            image_type = "D";
        } else {
            image_type = "";
        }
        if (!hasAIImage && !hasDTRImage) {
            Log.e(
                    "VERIFY_IMAGE",
                    "AI_IMAGE AND DTR_IMAGE BOTH EMPTY"
                            + " | POLE_NO=" + selectedPostingModel.getPOLE_NO()
                            + " | POLE_ID=" + selectedPostingModel.getPOLE_ID()
            );

            sent11KVPoles++;
            checkOfflineVerificationCompleted();
            return;
        }

        // DTR pole:
        // 1. Send AI_IMAGE to normal AI API
        // 2. Send DTR_IMAGE to DTR/plinth API
        if (hasAIImage) {

            verifySingleImage(
                    selectedPostingModel,
                    aiImage,
                    "P"
            );
        }

        if (hasDTRImage) {

            verifySingleImage(
                    selectedPostingModel,
                    dtrImage,
                    "D"
            );
        }

    }


    //NEW One
    private void checkOfflineVerificationCompleted() {

        Log.d("OFFLINE_VERIFY",
                "transmitList=" + transmitList.size()
                        + " | expectedVerifications="
                        + offline_poles_count_11
                        + " | completedVerifications="
                        + sent11KVPoles);

        if (sent11KVPoles >= offline_poles_count_11) {

            runOnUiThread(() -> {
                verify_offline_images_11.setVisibility(View.GONE);
                transmit_data_11.setVisibility(View.VISIBLE);
                send_data_layout_11.setVisibility(View.GONE);

                showVerifyOfflineImagesData();
            });
        }
    }
    //OLD One

//    private void checkOfflineVerificationCompleted() {
//        Log.d("OFFLINE_VERIFY",   " | transmitList=" + transmitList.size()  + " | transmitPolesCount=" + offline_poles_count_11 +  " | completed=" + completedImages + " | sent11KVPoles=" + sent11KVPoles);
//
//        if (sent11KVPoles >= offline_poles_count_11) {
//            runOnUiThread(() -> {
//                verify_offline_images_11.setVisibility(View.GONE);
//                transmit_data_11.setVisibility(View.VISIBLE);
//                send_data_layout_11.setVisibility(View.GONE);
//                showVerifyOfflineImagesData();
//            });
//        }
//    }

    private PostingModel createBasePostingModel(PostingModel source, String imagePath) {
        PostingModel model = new PostingModel();
        model.setID(Long.toString(createRandomUID()));
        model.setDEFECT_SUB_TYPE_ID(source.getDEFECT_SUB_TYPE_ID());
        model.setDEFECT_ID(source.getDEFECT_ID());
        model.setDEFECT_TYPE(source.getDEFECT_TYPE());
        model.setDEFECT_SUB_TYPE(source.getDEFECT_SUB_TYPE());
        model.setCYCLONE(source.getCYCLONE());
        model.setCURRENT_LAT(source.getCURRENT_LAT());
        model.setCURRENT_LONG(source.getCURRENT_LONG());
        model.setVOLTAGE_LEVEL(source.getVOLTAGE_LEVEL());
        model.setPOLE_NO(source.getPOLE_NO());
        model.setPOLE_ID(source.getPOLE_ID());
        model.setFEEDER_ID(source.getFEEDER_ID());
        model.setDTR_CODE(source.getDTR_CODE());
        model.setLT_CKT_CODE(source.getLT_CKT_CODE());
        model.setFROM(source.getFROM());
        if ("D".equalsIgnoreCase(source.getGRP_ID())) {
            model.setDTR_IMAGE(imagePath);
        } else {
            model.setAI_IMAGE(imagePath);
            model.setIMAGE(source.getIMAGE());
        }
        model.setIMAGE_TYPE("OFFLINE");
        model.setDEFECT_GROUP(source.getDEFECT_GROUP());
        model.setAI_FLAG(source.getAI_FLAG());
        model.setGRP_ID(source.getGRP_ID());
        return model;
    }

    private long createRandomUID() {
        UUID uuid = UUID.randomUUID();
        // System.out.println("UUID: " + uuid);
        long randomNumber = uuid.getLeastSignificantBits();
        randomNumber = Math.abs(randomNumber);
        //System.out.println("Random Number: " + randomNumber);
        return randomNumber;
    }

    private void addToTransmitListIfAbsent(PostingModel model) {
        if (model == null) {
            return;
        }
        if (!containsPostingModel(transmitList, model)) {
            transmitList.add(model);
        }
    }

    private boolean containsPostingModel(List<PostingModel> list, PostingModel target) {
        if (list == null || target == null) {
            return false;
        }
        for (PostingModel item : list) {
            if (item == null) {
                continue;
            }
            if (!TextUtils.isEmpty(item.getID()) && !TextUtils.isEmpty(target.getID()) && item.getID().equals(target.getID())) {
                return true;
            }
        }
        return false;
    }

    private String getJsonString(JsonObject object, String key) {
        if (object == null || !object.has(key) || object.get(key).isJsonNull()) {
            return "";
        }
        try {
            return object.get(key).getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    private String getJsonArrayAsString(JsonObject object, String key) {
        if (object == null || !object.has(key) || object.get(key).isJsonNull() || !object.get(key).isJsonArray()) {
            return "";
        }
        try {
            JsonArray array = object.getAsJsonArray(key);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < array.size(); i++) {
                if (i > 0) {
                    builder.append(",");
                }
                builder.append(array.get(i).getAsString());
            }
            return builder.toString();
        } catch (Exception e) {
            return "";
        }
    }


    private void transmitPoles11KV(List<PostingModel> model) {
        if (model == null || model.isEmpty()) {
            Log.e("TRANSMIT", "TRANSMIT LIST EMPTY");
            return;
        }
        Map<String, String> defectTypeImageMap = new HashMap<>();
        for (PostingModel model1 : model) {
            if (model1 == null) {
                continue;
            }
            String defectType = model1.getDEFECT_TYPE();
            String imagePath = model1.getIMAGE();
            if (TextUtils.isEmpty(defectType)) {
                continue;
            }
            if (!defectTypeImageMap.containsKey(defectType) && !TextUtils.isEmpty(imagePath)) {
                defectTypeImageMap.put(defectType, imagePath);
            }
        }
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        String deviceId = preferences.getString(MobileConstants.DEVICE_ID, "");
        JsonArray array = new JsonArray();
        SURVEY_TYPE = model.get(0).getFROM();
        Log.d("TRANSMIT", "SURVEY_TYPE=" + SURVEY_TYPE);
        for (PostingModel posting : model) {
            if (posting == null) {
                continue;
            }
            JsonObject requestObject = new JsonObject();
            requestObject.addProperty("APP_VERSION", MobileConstants.CYCLONE_SURVEY);
            requestObject.addProperty("APP_VERSION2", getResources().getString(R.string.displayVersion));
            requestObject.addProperty("GPS_ACCURACY", PmiMap.gpsAccurancy);
            requestObject.addProperty("DEVICE_ID", deviceId);
            requestObject.addProperty("ID", posting.getID());
            requestObject.addProperty("DEFECT_ID", posting.getDEFECT_ID());
            requestObject.addProperty("POLE_ID", posting.getPOLE_ID());
            requestObject.addProperty("POLE_NO", posting.getPOLE_NO());
            requestObject.addProperty("VOLTAGE_LEVEL", posting.getVOLTAGE_LEVEL());
            requestObject.addProperty("DTR_CODE", posting.getDTR_CODE());
            requestObject.addProperty("LT_CKT_CODE", posting.getLT_CKT_CODE());
            requestObject.addProperty("FEEDER_ID", posting.getFEEDER_ID());
            requestObject.addProperty("DEFECT_TYPE", posting.getDEFECT_TYPE());
            requestObject.addProperty("DEFECT_SUB_TYPE", posting.getDEFECT_SUB_TYPE());
            requestObject.addProperty("DEFECT_SUB_TYPE_ID", posting.getDEFECT_SUB_TYPE_ID());
            requestObject.addProperty("CYCLONE", posting.getCYCLONE());
            requestObject.addProperty("CURRENT_LAT", posting.getCURRENT_LAT());
            requestObject.addProperty("CURRENT_LONG", posting.getCURRENT_LONG());
            requestObject.addProperty("DEFECT_BY_USER", posting.getDEFECT_BY_USER());
            requestObject.addProperty("POLE_LAT", posting.getPOLE_LAT());
            requestObject.addProperty("POLE_LONG", posting.getPOLE_LONG());
            requestObject.addProperty("POLE_CONFIG", posting.getPOLE_CONFIG());
            if (loginDataResponses.get(0).getROLE().equalsIgnoreCase("AEOP")){
                requestObject.addProperty("USERID", loginDataResponses.get(0).getUSERID());
            }else {
                requestObject.addProperty("USERID", userId);
            }


            if (loginDataResponses.get(0).getROLE().equalsIgnoreCase("AEOP")){
                requestObject.addProperty("USERNAME", loginDataResponses.get(0).getROLE());
            }else {
                requestObject.addProperty("USERNAME", loginDataResponses.get(0).getNAME());
            }

            if ("RECTIFY".equalsIgnoreCase(SURVEY_TYPE)) {
                String grpId = "";
                String aiFlag = "NON AI";
                String defectGroup = posting.getDEFECT_GROUP();
                String defectType = posting.getDEFECT_TYPE();
                if ("DTR related".equalsIgnoreCase(defectGroup)) {
                    grpId = "D";
                } else if ("Conductor related".equalsIgnoreCase(defectGroup)) {
                    grpId = "C";
                } else if ("Pole related".equalsIgnoreCase(defectGroup)) {
                    grpId = "P";
                } else if ("Tree clearance".equalsIgnoreCase(defectGroup)) {
                    grpId = "T";
                } else if ("Non defective".equalsIgnoreCase(defectGroup)) {
                    grpId = "N";
                }
                if (!TextUtils.isEmpty(defectType)) {
                    String[] defectTypes = defectType.split(",");
                    for (String type : defectTypes) {
                        type = type.trim();
                        if ("Crossarm Tilt".equalsIgnoreCase(type) || "Pole Lean".equalsIgnoreCase(type) || "Top Cleat Tilt".equalsIgnoreCase(type) || "Tree Clearance".equalsIgnoreCase(type) || "DTR Mounting / Structure Defects".equalsIgnoreCase(type) || "Non defective Pole".equalsIgnoreCase(type)) {
                            aiFlag = "AI";
                            break;
                        }
                    }
                }
                requestObject.addProperty("AI_FLAG", aiFlag);
                requestObject.addProperty("GRP_ID", grpId);
            }
            requestObject.addProperty("RECT_REMARKS", posting.getRECT_REMARKS());
            requestObject.addProperty("USERID", userId);
            requestObject.addProperty("USERNAME", loginDataResponses.get(0).getROLE());
            requestObject.addProperty("SURVEY_MODE", posting.getIMAGE_TYPE());
            requestObject.addProperty("DEFECT_GROUP", posting.getDEFECT_GROUP());

            String commonImage;
            if ("DTR related".equalsIgnoreCase(posting.getDEFECT_GROUP()) || "DTR Mounting / Structure Damaged".equalsIgnoreCase(posting.getDEFECT_TYPE())) {
                commonImage = posting.getDTR_IMAGE();
                Log.d("FINAL_SHORT_PATH", "DTR" + " | TYPE=" + posting.getDEFECT_TYPE() + " | DTR_IMAGE=" + commonImage);
            } else {
                commonImage = posting.getIMAGE();
                Log.d("FINAL_SHORT_PATH", "NORMAL" + " | TYPE=" + posting.getDEFECT_TYPE() + " | IMAGE=" + commonImage);
            }
            requestObject.addProperty("IMG_SHORT_PATH", TextUtils.isEmpty(commonImage) ? "" : commonImage);
            JsonArray jsonArray = new JsonArray();
            ArrayList<AIResponseModel> aiResponseList = posting.getAiResponseModel();

            if (aiResponseList != null && !aiResponseList.isEmpty()) {
                for (AIResponseModel ai : aiResponseList) {
                    if (ai == null) {
                        continue;
                    }
                    JsonObject resObject = new JsonObject();
                    resObject.addProperty("AI_RESPONSE", ai.getAI_RESPONSE());
                    resObject.addProperty("POSITION", ai.getPOSITION());
                    resObject.addProperty("VOLTAGE", ai.getVOLTAGE());
                    resObject.addProperty("LEAN_ANGLE", ai.getLEAN_ANGLE());
                    resObject.addProperty("POSTITION_XARM", ai.getPOSTITION_XARM());
                    resObject.addProperty("POLE_RATIO", ai.getPOLE_RATIO());
                    resObject.addProperty("PLINTH_RATIO", ai.getPLINTH_RATIO());
                    resObject.addProperty("POLE_BBOX", ai.getPOLE_BBOX());
                    resObject.addProperty("CROP_BBOX", ai.getCROP_BBOX());
                    resObject.addProperty("VD_DEPTH", ai.getVD_DEPTH());
                    resObject.addProperty("VC_REASON", ai.getVC_REASON());
                    System.out.println("IMAGE_TYPEEEE::" + ai.getIMAGE() + " " + aiDTRShortUrl + " " + aiImageShortUrl);
                    System.out.println(
                            "IMAGE_TYPEEEE::" + ai.getIMAGE()
                                    + " | AI_IMAGE=" + posting.getAI_IMAGE()
                                    + " | DTR_IMAGE=" + posting.getDTR_IMAGE()
                    );

                    String poleImage = posting.getAI_IMAGE();
                    String dtrImage = posting.getDTR_IMAGE();

                    String responseImage = !TextUtils.isEmpty(poleImage)
                            ? poleImage
                            : dtrImage;

                    resObject.addProperty("IMAGE", responseImage);
                    // resObject.addProperty("IMAGE",ai.getIMAGE().equalsIgnoreCase("D")?aiDTRShortUrl:aiImageShortUrl);
                    jsonArray.add(resObject);
                }
            }
            requestObject.add("RESPONSE", jsonArray);
            array.add(requestObject);
        }
        Log.d("TRANSMIT", "TOTAL DEFECTS=" + array.size());
        Gson gson = new Gson();
        Log.d("PMI_DATA", gson.toJson(array));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = ("RECTIFY".equalsIgnoreCase(SURVEY_TYPE)) ? retrofitServicePmi.transmitDataNewRectify(array) : retrofitServicePmi.transmitDataNew(array);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(Transmit11KV.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                        transmit_data_11.setVisibility(View.VISIBLE);
                        send_data_layout_11.setVisibility(View.GONE);
                        return;
                    }
                    JsonObject responseObject = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(responseObject.get("STATUS").getAsString())) {
                        String errorMessage = "Transmission Success";
                        Toast.makeText(Transmit11KV.this, errorMessage, Toast.LENGTH_SHORT).show();
                        pmiDatabase.deleteSingleData("11 KV");
                        postingList.clear();
                        transmitList.clear();
                        send_data_layout_11.setVisibility(View.GONE);
                        poles_count.setText("0");
                        //openSuccessDialog();
                        pmiDatabase.deleteAllPmiData();
                        getOnBackPressedDispatcher().onBackPressed();

                    } else {
                        String errorMessage = responseObject.has("ERR_MSG") ? responseObject.get("ERR_MSG").toString() : "Transmission failed";
                        Toast.makeText(Transmit11KV.this, errorMessage, Toast.LENGTH_SHORT).show();
                        transmit_data_11.setVisibility(View.VISIBLE);
                        send_data_layout_11.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e("TRANSMIT", "TRANSMIT RESPONSE ERROR", e);
                    transmit_data_11.setVisibility(View.VISIBLE);
                    send_data_layout_11.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TRANSMIT", "TRANSMIT API FAILED", t);
                Toast.makeText(Transmit11KV.this, "Try Again " + t.getMessage(), Toast.LENGTH_LONG).show();
                transmit_data_11.setVisibility(View.VISIBLE);
                send_data_layout_11.setVisibility(View.GONE);
            }
        });
    }

    private void openSuccessDialog() {

        if (isFinishing() || isDestroyed()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Transmit11KV.this);
        builder.setTitle("Success");
        builder.setMessage("Data Transmitted Successfully");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();

            Intent intent = new Intent(Transmit11KV.this, PMINewHome.class);
            startActivity(intent);
            finish();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
//    private void openSuccessDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Success");
//        builder.setMessage("Data Transmitted Successfully");
//        builder.setCancelable(false);
//        builder.setPositiveButton("OK", (dialog, which) -> {
//            dialog.dismiss();
//            Intent intent = new Intent(Transmit11KV.this, PMINewHome.class);
//            startActivity(intent);
//            finish();
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    public void showVerifyOfflineImagesData() {
        // ArrayList<PostingModel> ofl = pmiDatabase.getPmiPostingData();
        // ArrayList<PostingModel> ofl = new ArrayList<>(postingList);
       /* ArrayList<PostingModel> sendOfl = new ArrayList<>();
        for (int i = 0; i < transmitList.size(); i++) {
            if (transmitList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV")) {
                if (!transmitList.get(i).getAI_RESPONSE().contains("Straight")) {
                    sendOfl.add(transmitList.get(i));
                }

            }
        }*/
        layout_verified_images.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        rv_verified_images.setLayoutManager(layoutManager1);
        rv_verified_images.setFocusable(false);
        VerifiedImagesAdapter adapter1 = new VerifiedImagesAdapter(transmitList, this);
        rv_verified_images.setAdapter(adapter1);
        rv_verified_images.setNestedScrollingEnabled(false);
    }


}
