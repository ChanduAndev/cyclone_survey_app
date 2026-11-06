package com.apepdcl.gis_survey.PMI.rectification;


import static com.apepdcl.gis_survey.PMI.rectification.Select11KVFeederRectify.aiPoleList11;
import static com.apepdcl.gis_survey.PMI.rectification.Select11KVFeederRectify.nonAiPoleList11;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.AIResponseModel;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.models.PoleModelLT;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.PMI.new_screens.TFLiteObjectDetector;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.sourceforge.app.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 23-03-2026.
 */
public class Select11PoleRectify extends AppCompatActivity implements RVPolesAdapterRectify11.OnItemClickListener, NonAiAdapter11.OnNonAiItemClickListener {

    String selectedSSCode11, selectedFeederId11, selectedFeederName11;

    ProgressDialog progressDialog;
    RecyclerView rv_33, rv_11_non_ai;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;
    public static ArrayList<PoleModel11KV> polesList;
    public static ArrayList<PoleModel11KV> nearByPolesList;
    double nearByDistance = 200000;
    public static final int GALLERY_RESULT_CODE_IMAGE_1 = 5;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat, currentLog;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    ImageView dtr_image, pole_image_ai, non_ai_image, conductor_image;

    PoleModel11KV modelLT;

    public static ArrayList<PoleModel11KV> reportListLT;
    TextView total_defects, rectified, not_rectified, not_verified, tv_error_non_ai, tv_error_ai;
    private RetrofitServicePMI retrofitServicePmi;
    private String FROM = "", DTR_IMAGE = "", POLE_IMG_AI = "", NON_AI_IMAGE = "", CONDUTOR_IMAGE = "";

    public static Bitmap capturedBitmapPMI1 = null;
    public static boolean isImageCapturedPMI1 = false;
    public static boolean photoClickedPMI1 = false;
    TFLiteObjectDetector detector;
    public static boolean isDataChanged = false;

    @Override
    protected void onResume() {
        super.onResume();

        //System.out.println("ON RESUME CALLED : "+isDataChanged);
        if (isImageCapturedPMI1) {
            Uri imageUri = getLastImageUri(this);

            if (imageUri != null) {
                try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap == null) {
                        Toast.makeText(this, "Failed to decode captured image.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    showImageConfirmDialog(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error reading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (isDataChanged) {
            isDataChanged = false;
            int rectifiedCount = 0;
            int notRectifiedCount = 0;
            int notVerifiedCount = 0;

            for (int i = 0; i < reportListLT.size(); i++) {
                PoleModel11KV pole = reportListLT.get(i);
                String isRectified = pole.getIS_RECTIFIED();
                String aiPending = pole.getAI_PENDING();


                if (isRectified.equalsIgnoreCase("Y")) {
                    rectifiedCount++;
                } else if (aiPending.equalsIgnoreCase("Y")) {
                    notVerifiedCount++;   // IS_RECTIFIED = N, AI_PENDING = Y
                } else {
                    notRectifiedCount++;  // IS_RECTIFIED = N, AI_PENDING = N
                }
            }

            total_defects.setText(String.valueOf(reportListLT.size()));
            rectified.setText(String.valueOf(rectifiedCount));
            not_rectified.setText(String.valueOf(notRectifiedCount));
            not_verified.setText(String.valueOf(notVerifiedCount));

            ArrayList<PoleModel11KV> tempList = new ArrayList<>();
            for (int i = 0; i < polesList.size(); i++) {
                if (polesList.get(i).getIS_RECTIFIED().equalsIgnoreCase("N") && polesList.get(i).getAI_PENDING().equalsIgnoreCase("N")) {
                    tempList.add(polesList.get(i));
                }
            }
        }

        for (int i = 0; i < aiPoleList11.size(); i++) {
            if ("P".equalsIgnoreCase(aiPoleList11.get(i).getGRP_ID()) ||
                    "N".equalsIgnoreCase(aiPoleList11.get(i).getGRP_ID()) ||
                    "T".equalsIgnoreCase(aiPoleList11.get(i).getGRP_ID())
            ) {
                POLE_IMG_AI = aiPoleList11.get(i).getIMG_URL();
            }else if ("D".equalsIgnoreCase(aiPoleList11.get(i).getGRP_ID())){
                DTR_IMAGE = aiPoleList11.get(i).getIMG_URL();
            }

        }

        if (!DTR_IMAGE.isEmpty()) {
            dtr_image.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(DTR_IMAGE)
                    .placeholder(R.drawable.loader_new)
                    .error(R.drawable.loader_new)
                    .into(dtr_image);
        } else {
            dtr_image.setVisibility(View.GONE);
        }

        if (!POLE_IMG_AI.isEmpty()) {
            pole_image_ai.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(POLE_IMG_AI)
                    .placeholder(R.drawable.loader_new)
                    .error(R.drawable.loader_new)
                    .into(pole_image_ai);

        } else {
            pole_image_ai.setVisibility(View.GONE);
        }


        //NON AI
        for (int i = 0; i < nonAiPoleList11.size(); i++) {
            if ("P".equalsIgnoreCase(nonAiPoleList11.get(i).getGRP_ID())) {
                NON_AI_IMAGE = nonAiPoleList11.get(i).getIMG_URL();
            } else if ("C".equalsIgnoreCase(nonAiPoleList11.get(i).getGRP_ID())) {
                CONDUTOR_IMAGE = nonAiPoleList11.get(i).getIMG_URL();
            }

        }


        if (!CONDUTOR_IMAGE.isEmpty()) {
            conductor_image.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(CONDUTOR_IMAGE)
                    .placeholder(R.drawable.loader_new)
                    .error(R.drawable.loader_new)
                    .into(conductor_image);
        } else {
            conductor_image.setVisibility(View.GONE);
        }

        if (!NON_AI_IMAGE.isEmpty()) {

            non_ai_image.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(NON_AI_IMAGE)
                    .placeholder(R.drawable.loader_new)
                    .error(R.drawable.loader_new)
                    .into(non_ai_image);

        } else {
            non_ai_image.setVisibility(View.GONE);
        }


    }

    private void showImageConfirmDialog(Bitmap bitmap) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Image");

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(20, 20, 20, 20);

        builder.setView(imageView);

        builder.setPositiveButton("Proceed", (dialog, which) -> {
            dialog.dismiss();
            processCapturedImage(bitmap);   // ✅ continue
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            // ❌ Stop here – do nothing
            isImageCapturedPMI1 = false;
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void processCapturedImage(Bitmap bitmap) {

        if (bitmap == null) {
            Toast.makeText(this, "Image is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (modelLT == null) {
            Toast.makeText(this, "Pole data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            File imageFile = saveBitmapToLocalFile(bitmap);

            if (imageFile == null || !imageFile.exists()) {
                Toast.makeText(
                        this,
                        "Failed to save image",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }



            PostingModel postingModel = new PostingModel();

            postingModel.setID(
                    Long.toString(createRandomUID())
            );

            postingModel.setVOLTAGE_LEVEL("11 KV");
            postingModel.setFROM("RECTIFY");

            postingModel.setPOLE_NO(
                    modelLT.getPOLE_NO()
            );

            postingModel.setPOLE_ID(
                    modelLT.getPOLE_ID()
            );

            postingModel.setFEEDER_ID(
                    modelLT.getFEEDER_ID()
            );

            postingModel.setDTR_CODE("");
            postingModel.setLT_CKT_CODE("");
            postingModel.setDEFECT_SUB_TYPE(modelLT.getDEFECT_SUB_TYPE());

            // Local image path
            postingModel.setIMAGE(imageFile.getAbsolutePath());

            postingModel.setIMAGE_TYPE("OFFLINE");


            postingModel.setCURRENT_LONG(String.valueOf(currentLog));

            postingModel.setCURRENT_LAT(String.valueOf(currentLat));

            postingModel.setDEFECT_TYPE(modelLT.getDEFECT_TYPE());
            postingModel.setAI_FLAG(modelLT.getAI_FLAG());
            postingModel.setGRP_ID(modelLT.getGRP_ID());


            if ("P".equalsIgnoreCase(modelLT.getGRP_ID())) {

                postingModel.setDEFECT_GROUP("Pole related");

            } else if ("D".equalsIgnoreCase(modelLT.getGRP_ID())) {

                postingModel.setDEFECT_GROUP("DTR related");

            } else if ("C".equalsIgnoreCase(modelLT.getGRP_ID())) {

                postingModel.setDEFECT_GROUP("Conductor related");

            } else if ("T".equalsIgnoreCase(modelLT.getGRP_ID())) {

                postingModel.setDEFECT_GROUP("Tree clearance");

            } else if ("N".equalsIgnoreCase(modelLT.getGRP_ID())) {

                postingModel.setDEFECT_GROUP("Non defective");
            }

            Bitmap safeBitmap =
                    bitmap.copy(Bitmap.Config.ARGB_8888, false);

            TFLiteObjectDetector detector =
                    new TFLiteObjectDetector(this);

            TFLiteObjectDetector.Result result1 =
                    detector.annotate(safeBitmap);

            if (result1 == null) {
                Toast.makeText(
                        this,
                        "AI response is empty",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if ("P".equalsIgnoreCase(modelLT.getGRP_ID())
                    || "D".equalsIgnoreCase(modelLT.getGRP_ID())) {

                if (!"VALID".equalsIgnoreCase(result1.status)) {

                    Toast.makeText(
                            this,
                            result1.status,
                            Toast.LENGTH_SHORT
                    ).show();

                    showDetectionDialog(
                            result1.status,
                            safeBitmap
                    );

                    return;
                }
            }

            postingModel.setAI_RESPONSE(
                    result1.status
            );

            ArrayList<AIResponseModel> aiList =
                    new ArrayList<>();

            AIResponseModel aiResponseModel =
                    new AIResponseModel();

            aiResponseModel.setAI_RESPONSE("");
            aiResponseModel.setLEAN_ANGLE("");
            aiResponseModel.setPOSITION("");
            aiResponseModel.setVOLTAGE("");
            aiResponseModel.setPOSTITION_XARM("");

            aiList.add(aiResponseModel);

            postingModel.setAiResponseModel(aiList);

            PmiDatabase pmiDatabase =
                    new PmiDatabase(Select11PoleRectify.this);

            pmiDatabase.insertOrUpdatePmiPostingForRectify(
                    postingModel
            );

            modelLT.setAI_PENDING("Y");



            for (int i = 0; i < polesList.size(); i++) {

                if (polesList.get(i).getPOLE_ID()
                        .equals(modelLT.getPOLE_ID())
                        &&
                        polesList.get(i).getDEFECT_GROUP()
                                .equals(modelLT.getDEFECT_GROUP())) {

                    polesList.set(i, modelLT);
                    break;
                }
            }

            for (int i = 0; i < reportListLT.size(); i++) {

                if (reportListLT.get(i).getPOLE_ID()
                        .equals(modelLT.getPOLE_ID())
                        &&
                        reportListLT.get(i).getDEFECT_GROUP()
                                .equals(modelLT.getDEFECT_GROUP())) {

                    reportListLT.set(i, modelLT);
                    break;
                }
            }

            isDataChanged = true;

            isImageCapturedPMI1 = false;

            Toast.makeText(
                    this,
                    "Saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private File saveBitmapToLocalFile(Bitmap bitmap) {

        File directory = new File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "PMI_RECTIFY"
        );

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return null;
            }
        }

        String fileName =
                "PMI_RECTIFY_" +
                        new SimpleDateFormat(
                                "yyyyMMdd_HHmmss",
                                Locale.getDefault()
                        ).format(new Date()) +
                        ".jpg";

        File imageFile = new File(directory, fileName);

        try (FileOutputStream fos =
                     new FileOutputStream(imageFile)) {

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    fos
            );

            fos.flush();

            return imageFile;

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }
    private Uri getLastImageUri(Context context) {
        Uri imageUri = null;
        String[] projection = {MediaStore.Images.Media._ID};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = null;
        try {
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver cr = context.getContentResolver();
            cursor = cr.query(images, projection, null, null, sortOrder);

            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                long id = cursor.getLong(idColumn);
                imageUri = Uri.withAppendedPath(images, String.valueOf(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return imageUri;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_11_pole_rectify);
        getSupportActionBar().hide();
        selectedFeederId11 = getIntent().getStringExtra("FEEDER_ID");
        selectedFeederName11 = getIntent().getStringExtra("FEEDER_NAME");
        selectedSSCode11 = getIntent().getStringExtra("SS_CODE");
        isDataChanged = false;
        total_defects = findViewById(R.id.total_defects);
        rectified = findViewById(R.id.rectified);
        not_rectified = findViewById(R.id.not_rectified);
        not_verified = findViewById(R.id.not_verified);
        tv_error_non_ai = findViewById(R.id.tv_error_non_ai);
        tv_error_ai = findViewById(R.id.tv_error_ai);
        dtr_image = findViewById(R.id.dtr_image);
        pole_image_ai = findViewById(R.id.pole_image);
        non_ai_image = findViewById(R.id.non_ai_image);
        conductor_image = findViewById(R.id.conductor_image);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        nearByPolesList = new ArrayList<>();
        polesList = new ArrayList<>();
        detector = new TFLiteObjectDetector(this);
        reportListLT = Select11KVFeederRectify.polesList11;
        for (int i = 0; i < Select11KVFeederRectify.polesList11.size(); i++) {
            if (Select11KVFeederRectify.polesList11.get(i).getIS_RECTIFIED().equalsIgnoreCase("N") && Select11KVFeederRectify.polesList11.get(i).getAI_PENDING().equalsIgnoreCase("N")) {
                polesList.add(Select11KVFeederRectify.polesList11.get(i));
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLatLongs();

        rv_33 = findViewById(R.id.rv_33);
        rv_11_non_ai = findViewById(R.id.rv_11_non_ai);
        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);
        logout_layout.setOnClickListener(v -> onBackPressed());
        profile_layout.setOnClickListener(v -> onBackPressed());

        int rectifiedCount = 0;
        int notRectifiedCount = 0;
        int notVerifiedCount = 0;

        for (int i = 0; i < reportListLT.size(); i++) {
            if (reportListLT.get(i).getIS_RECTIFIED().equalsIgnoreCase("Y")) {
                rectifiedCount++;
            }
        }

        for (int i = 0; i < reportListLT.size(); i++) {
            if (reportListLT.get(i).getIS_RECTIFIED().equalsIgnoreCase("N") && reportListLT.get(i).getAI_PENDING().equalsIgnoreCase("Y")) {
                notVerifiedCount++;
            } else {
                if (reportListLT.get(i).getIS_RECTIFIED().equalsIgnoreCase("N")) {
                    notRectifiedCount++;
                }
            }
        }


        total_defects.setText(String.valueOf(reportListLT.size()));
        rectified.setText(String.valueOf(rectifiedCount));
        not_rectified.setText(String.valueOf(notRectifiedCount));
        not_verified.setText(String.valueOf(notVerifiedCount));

        imagePickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                                Uri imageUri = result.getData().getData();

                                try {
                                    Bitmap bitmap;

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        ImageDecoder.Source source =
                                                ImageDecoder.createSource(getContentResolver(), imageUri);
                                        bitmap = ImageDecoder.decodeBitmap(source);
                                    } else {
                                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                    }

                                    Bitmap safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                                    TFLiteObjectDetector detector = new TFLiteObjectDetector(this);
                                    TFLiteObjectDetector.Result result1 = detector.annotate(safeBitmap);
                                    if (modelLT.getDEFECT_GROUP().equalsIgnoreCase("POLE_DEFECTS") || modelLT.getDEFECT_GROUP().equalsIgnoreCase("DTR_DEFECTS")) {
                                        if (!result1.status.equals("VALID")) {
                                            Toast.makeText(this, result1.status, Toast.LENGTH_SHORT).show();
                                            showDetectionDialog(result1.status, safeBitmap);
                                        } else {
                                            {
                                                //String CURRENT_IMAGE_PATH = getRealPathFromURI(imageUri);
                                                File file = uriToFile(this, imageUri);
                                                PostingModel postingModel = new PostingModel();
                                                postingModel.setID(Long.toString(createRandomUID()));

                                                postingModel.setVOLTAGE_LEVEL("11 KV");
                                                postingModel.setFROM("RECTIFY");
                                                postingModel.setAI_RESPONSE(result1.status);
                                                postingModel.setPOLE_NO(modelLT.getPOLE_NO());
                                                postingModel.setPOLE_ID(modelLT.getPOLE_ID());

                                                postingModel.setFEEDER_ID(modelLT.getFEEDER_ID());
                                                postingModel.setDTR_CODE("");
                                                postingModel.setLT_CKT_CODE("");
                                                //postingModel.setIMAGE(CURRENT_IMAGE_PATH);
                                                postingModel.setIMAGE(file.getAbsolutePath());
                                                postingModel.setIMAGE_TYPE("OFFLINE");
                                                postingModel.setCURRENT_LONG(String.valueOf(currentLog));
                                                postingModel.setCURRENT_LAT(String.valueOf(currentLat));
                                                // postingModel.setRECT_ID(modelLT.getRECT_ID());
                                                postingModel.setDEFECT_GROUP(modelLT.getDEFECT_GROUP());
                                                postingModel.setDEFECT_TYPE(modelLT.getDEFECT_TYPE());

                                                ArrayList<AIResponseModel> aiList = new ArrayList<>();

                                                AIResponseModel aiResponseModel = new AIResponseModel();
                                                aiResponseModel.setAI_RESPONSE("");
                                                aiResponseModel.setLEAN_ANGLE("");
                                                aiResponseModel.setPOSITION("");
                                                aiResponseModel.setVOLTAGE("");
                                                aiResponseModel.setPOSTITION_XARM("");
                                                aiList.add(aiResponseModel);
                                                postingModel.setAiResponseModel(aiList);

                                                PmiDatabase pmiDatabase = new PmiDatabase(Select11PoleRectify.this);
                                                pmiDatabase.insertOrUpdatePmiPostingForRectify(postingModel);
                                                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();

                                                modelLT.setAI_PENDING("Y");

                                                for (int i = 0; i < polesList.size(); i++) {
                                                    if (polesList.get(i).getPOLE_ID().equals(modelLT.getPOLE_ID()) && polesList.get(i).getDEFECT_GROUP().equals(modelLT.getDEFECT_GROUP())) {
                                                        // System.out.println("DEBUG MATCH FOUND in polesListLT at index: " + i);
                                                        polesList.set(i, modelLT);
                                                        break;
                                                    }
                                                }

                                                for (int i = 0; i < reportListLT.size(); i++) {
                                                    if (reportListLT.get(i).getPOLE_ID().equals(modelLT.getPOLE_ID()) && reportListLT.get(i).getDEFECT_GROUP().equals(modelLT.getDEFECT_GROUP())) {
                                                        // System.out.println("DEBUG MATCH FOUND in reportListLT at index: " + i);
                                                        reportListLT.set(i, modelLT);
                                                        break;
                                                    }
                                                }
                                                isDataChanged = true;

                                                //onBackPressed();
                                            }
                                        }
                                    } else {
                                        //String CURRENT_IMAGE_PATH = getRealPathFromURI(imageUri);
                                        File file = uriToFile(this, imageUri);
                                        PostingModel postingModel = new PostingModel();
                                        postingModel.setID(Long.toString(createRandomUID()));

                                        postingModel.setVOLTAGE_LEVEL("11 KV");
                                        postingModel.setFROM("RECTIFY");
                                        postingModel.setAI_RESPONSE(result1.status);
                                        postingModel.setPOLE_NO(modelLT.getPOLE_NO());
                                        postingModel.setPOLE_ID(modelLT.getPOLE_ID());

                                        postingModel.setFEEDER_ID(modelLT.getFEEDER_ID());
                                        postingModel.setDTR_CODE("");
                                        postingModel.setLT_CKT_CODE("");
                                        //postingModel.setIMAGE(CURRENT_IMAGE_PATH);
                                        postingModel.setIMAGE(file.getAbsolutePath());
                                        postingModel.setIMAGE_TYPE("OFFLINE");
                                        postingModel.setCURRENT_LONG(String.valueOf(currentLog));
                                        postingModel.setCURRENT_LAT(String.valueOf(currentLat));
                                        // postingModel.setRECT_ID(modelLT.getRECT_ID());
                                        postingModel.setDEFECT_GROUP(modelLT.getDEFECT_GROUP());
                                        postingModel.setDEFECT_TYPE(modelLT.getDEFECT_TYPE());

                                        ArrayList<AIResponseModel> aiList = new ArrayList<>();

                                        AIResponseModel aiResponseModel = new AIResponseModel();
                                        aiResponseModel.setAI_RESPONSE("");
                                        aiResponseModel.setLEAN_ANGLE("");
                                        aiResponseModel.setPOSITION("");
                                        aiResponseModel.setVOLTAGE("");
                                        aiResponseModel.setPOSTITION_XARM("");
                                        aiList.add(aiResponseModel);
                                        postingModel.setAiResponseModel(aiList);

                                        PmiDatabase pmiDatabase = new PmiDatabase(Select11PoleRectify.this);
                                        pmiDatabase.insertOrUpdatePmiPostingForRectify(postingModel);
                                        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();

                                        modelLT.setAI_PENDING("Y");

                                        for (int i = 0; i < polesList.size(); i++) {
                                            if (polesList.get(i).getPOLE_ID().equals(modelLT.getPOLE_ID()) && polesList.get(i).getDEFECT_GROUP().equals(modelLT.getDEFECT_GROUP())) {
                                                // System.out.println("DEBUG MATCH FOUND in polesListLT at index: " + i);
                                                polesList.set(i, modelLT);
                                                break;
                                            }
                                        }

                                        for (int i = 0; i < reportListLT.size(); i++) {
                                            if (reportListLT.get(i).getPOLE_ID().equals(modelLT.getPOLE_ID()) && reportListLT.get(i).getDEFECT_GROUP().equals(modelLT.getDEFECT_GROUP())) {
                                                //System.out.println("DEBUG MATCH FOUND in reportListLT at index: " + i);
                                                reportListLT.set(i, modelLT);
                                                break;
                                            }
                                        }
                                        isDataChanged = true;

                                        //onBackPressed();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );


    }


    private void getLatLongs() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLat = location.getLatitude();
                            currentLog = location.getLongitude();

                            for (int i = 0; i < aiPoleList11.size(); i++) {
                                double distance = getDistanceBetweenPolesNormal(
                                        aiPoleList11.get(i).getGLONG(),
                                        aiPoleList11.get(i).getGLAT(),
                                        currentLat, currentLog
                                );

                                int myInt = (int) Math.round(distance);
                                aiPoleList11.get(i).setDISTANCE(String.valueOf(myInt));
                                if (distance < nearByDistance) {
                                    nearByPolesList.add(aiPoleList11.get(i));
                                }
                            }

                            for (int i = 0; i < nonAiPoleList11.size(); i++) {
                                double distance = getDistanceBetweenPolesNormal(
                                        nonAiPoleList11.get(i).getGLONG(),
                                        nonAiPoleList11.get(i).getGLAT(),
                                        currentLat, currentLog
                                );

                                int myInt = (int) Math.round(distance);
                                nonAiPoleList11.get(i).setDISTANCE(String.valueOf(myInt));
                                if (distance < nearByDistance) {
                                    nearByPolesList.add(nonAiPoleList11.get(i));
                                }
                            }
                            System.out.println("COUNT_OF_AI_POLES ON_CREATE" + aiPoleList11.size());
                            System.out.println("COUNT_OF_NON_AI_POLES ON_CREATE" + nonAiPoleList11.size());

                            if (aiPoleList11.isEmpty()) {
                                tv_error_ai.setVisibility(View.VISIBLE);
                                rv_33.setVisibility(View.GONE);
                            } else {
                                rv_33.setVisibility(View.VISIBLE);
                                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(Select11PoleRectify.this);
                                rv_33.setLayoutManager(layoutManager1);
                                rv_33.setFocusable(false);
                                RVPolesAdapterRectify11 adapter1 = new RVPolesAdapterRectify11(Select11PoleRectify.this, aiPoleList11, Select11PoleRectify.this);
                                rv_33.setAdapter(adapter1);
                                rv_33.setNestedScrollingEnabled(true);
                                tv_error_ai.setVisibility(View.GONE);
                            }


                            if (nonAiPoleList11.isEmpty()) {
                                tv_error_non_ai.setVisibility(View.VISIBLE);
                                rv_11_non_ai.setVisibility(View.GONE);
                            } else {
                                rv_11_non_ai.setVisibility(View.VISIBLE);
                                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Select11PoleRectify.this);
                                rv_11_non_ai.setLayoutManager(layoutManager2);
                                rv_11_non_ai.setFocusable(false);
                                NonAiAdapter11 adapter2 = new NonAiAdapter11(Select11PoleRectify.this, nonAiPoleList11, Select11PoleRectify.this);
                                rv_11_non_ai.setAdapter(adapter2);
                                rv_11_non_ai.setNestedScrollingEnabled(true);
                                tv_error_non_ai.setVisibility(View.GONE);
                            }


                        } else {
                            // getFeederMasterAPI(0.0, 0.0);
                            Toast.makeText(Select11PoleRectify.this, "Unable to retrieve location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private double getDistanceBetweenPolesNormal(
            String poleLat,
            String poleLon,
            double currentLat,
            double currentLon
    ) {
        try {
            Location currentLocation = new Location("current");
            currentLocation.setLatitude(currentLat);   // ✅ LAT
            currentLocation.setLongitude(currentLon); // ✅ LON

            Location poleLocation = new Location("pole");
            poleLocation.setLatitude(Double.parseDouble(poleLat));   // ✅ LAT
            poleLocation.setLongitude(Double.parseDouble(poleLon)); // ✅ LON

            // System.out.println("currentLat NORMAL :"+currentLat);
            // System.out.println("currentLon NORMAL :"+currentLon);
            // System.out.println("poleLat NORMAL :"+poleLat);
            //  System.out.println("poleLon NORMAL :"+poleLon);
            //  System.out.println("DISTANCE NORMAL :" + poleLocation.distanceTo(currentLocation));

            return poleLocation.distanceTo(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE; // safety fallback
        }
    }

    @Override
    public void onItemClick(PoleModel11KV model) {
        if (ConnectivityChecking.checkConnection(Select11PoleRectify.this)) {
//            Intent intent = new Intent(Select11PoleRectify.this, RectifyMap11.class);
//            intent.putExtra("FEEDER_ID",  selectedFeederId11);
//            intent.putExtra("FEEDER_NAME", selectedFeederName11);
//            intent.putExtra("SS_CODE", selectedSSCode11);
//            intent.putExtra("MODEL", model);
//            startActivity(intent);

            String defectSubType = model.getDEFECT_SUB_TYPE();

            if (defectSubType != null && defectSubType.contains(",")) {

                // More than one defect subtype
                showDefectSubTypeBottomSheet(model, defectSubType);

            } else {

                // Single defect subtype
                proceedWithPoleClick(model);
            }
        } else {
            modelLT = model;
            openCamera();
            //launchGalleryIntent(GALLERY_RESULT_CODE_IMAGE_1, model);
        }
    }

    private void openCamera() {
        photoClickedPMI1 = true;
        isImageCapturedPMI1 = false;
        capturedBitmapPMI1 = null;
        Intent intentOpenCamera = new Intent(Select11PoleRectify.this, MainActivity.class);
        startActivity(intentOpenCamera);


    }

    private void launchGalleryIntent(int RequestCode, PoleModel11KV model) {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RequestCode);*/


        modelLT = model;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private long createRandomUID() {
        UUID uuid = UUID.randomUUID();
        // System.out.println("UUID: " + uuid);
        long randomNumber = uuid.getLeastSignificantBits();
        randomNumber = Math.abs(randomNumber);
        //System.out.println("Random Number: " + randomNumber);
        return randomNumber;
    }

    public String getRealPathFromURI(Uri uri) {
        String result = null;

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                result = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        return result;
    }

    private void showDetectionDialog(String status, Bitmap safeBitmap) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.detect_dialog);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        RelativeLayout back_layout = payDialog.findViewById(R.id.back_layout);
        RelativeLayout close_layout = payDialog.findViewById(R.id.close_layout);
        ImageView selected_image = payDialog.findViewById(R.id.selected_image);
        TextView image_desc = payDialog.findViewById(R.id.image_desc);
        Button okayButton = payDialog.findViewById(R.id.okayButton);

        image_desc.setText(status);
        selected_image.setImageBitmap(safeBitmap);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        payDialog.show();
    }

    public File uriToFile(Context context, Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getCacheDir(), "upload_image.jpg");

        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int length;

        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();

        return file;
    }

    @Override
    public void onBackPressed() {
        Intent playIntent = new Intent(Select11PoleRectify.this, PMINewHome.class); //Intent.ACTION_VIEW);
        startActivity(playIntent);
        finish();
    }

    private void showDefectSubTypeBottomSheet(
            PoleModel11KV model,
            String defectSubType) {

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(Select11PoleRectify.this);

        View view = getLayoutInflater().inflate(
                R.layout.bottom_sheet_defect_sub_type,
                null
        );

        LinearLayout checkboxContainer =
                view.findViewById(R.id.checkboxContainer);

        Button btnContinue =
                view.findViewById(R.id.btnContinue);

        // Split:
        // "BOTTOM RUSTED,SHORT POLE"
        String[] defectTypes = defectSubType.split(",");

        List<CheckBox> checkBoxes = new ArrayList<>();

        for (String defect : defectTypes) {

            defect = defect.trim();

            if (!defect.isEmpty()) {

                CheckBox checkBox = new CheckBox(
                        Select11PoleRectify.this
                );

                checkBox.setText(defect);
                checkBox.setTextSize(16);
                checkBox.setPadding(8, 8, 8, 8);

                checkboxContainer.addView(checkBox);

                checkBoxes.add(checkBox);
            }
        }

        btnContinue.setOnClickListener(v -> {

            ArrayList<String> selectedDefects = new ArrayList<>();

            for (CheckBox checkBox : checkBoxes) {

                if (checkBox.isChecked()) {
                    selectedDefects.add(
                            checkBox.getText().toString()
                    );
                }
            }

            if (selectedDefects.isEmpty()) {

                Toast.makeText(
                        Select11PoleRectify.this,
                        "Please select at least one defect",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // You can pass selected defects to next screen
            Intent intent = new Intent(
                    Select11PoleRectify.this,
                    RectifyMap11.class
            );

            intent.putExtra("FEEDER_ID", selectedFeederId11);
            intent.putExtra("FEEDER_NAME", selectedFeederName11);
            intent.putExtra("SS_CODE", selectedSSCode11);
            intent.putExtra("MODEL", model);

            // Selected defect sub-types
            intent.putStringArrayListExtra(
                    "SELECTED_SUB_DEFECT_TYPE",
                    selectedDefects
            );

            bottomSheetDialog.dismiss();

            startActivity(intent);
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void proceedWithPoleClick(PoleModel11KV model) {

        if (ConnectivityChecking.checkConnection(
                Select11PoleRectify.this)) {

            Intent intent = new Intent(
                    Select11PoleRectify.this,
                    RectifyMap11.class
            );

            intent.putExtra("FEEDER_ID", selectedFeederId11);
            intent.putExtra("FEEDER_NAME", selectedFeederName11);
            intent.putExtra("SS_CODE", selectedSSCode11);
            intent.putExtra("MODEL", model);

            startActivity(intent);

        } else {
            modelLT = model;
            openCamera();
          //  launchGalleryIntent(GALLERY_RESULT_CODE_IMAGE_1, model);
        }
    }

    @Override
    public void onNonAiItemClick(PoleModel11KV model) {
        if (ConnectivityChecking.checkConnection(Select11PoleRectify.this)) {
//            Intent intent = new Intent(Select11PoleRectify.this, RectifyMap11.class);
//            intent.putExtra("FEEDER_ID",  selectedFeederId11);
//            intent.putExtra("FEEDER_NAME", selectedFeederName11);
//            intent.putExtra("SS_CODE", selectedSSCode11);
//            intent.putExtra("MODEL", model);
//            startActivity(intent);

            String defectSubType = model.getDEFECT_SUB_TYPE();

            if (defectSubType != null && defectSubType.contains(",")) {

                // More than one defect subtype
                showDefectSubTypeBottomSheet(model, defectSubType);

            } else {

                // Single defect subtype
                proceedWithPoleClick(model);
            }
        } else {
            modelLT = model;
            openCamera();
           // launchGalleryIntent(GALLERY_RESULT_CODE_IMAGE_1, model);
        }
    }

}
