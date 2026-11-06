package com.apepdcl.gis_survey.PMI.rectification;


import static com.apepdcl.gis_survey.PMI.rectification.Select11KVFeederRectify.aiPoleList11;
import static com.apepdcl.gis_survey.PMI.rectification.Select11KVFeederRectify.nonAiPoleList11;
import static com.apepdcl.gis_survey.PMI.rectification.Select33KVFeederRectify.aiPoleList33;
import static com.apepdcl.gis_survey.PMI.rectification.Select33KVFeederRectify.nonAiPoleList33;
import static com.apepdcl.gis_survey.PMI.rectification.SelectLTCircuitRectify.aiPoleListLT;
import static com.apepdcl.gis_survey.PMI.rectification.SelectLTCircuitRectify.nonAiPoleListLT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.AIResponseModel;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;
import com.apepdcl.gis_survey.PMI.models.PoleModelLT;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.PMI.new_screens.TFLiteObjectDetector;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by SOUBHAGYA SWAIN on 17-03-2026.
 */
public class SelectLTPoleRectify extends AppCompatActivity implements RVPolesAdapterRectifyLT.OnItemClickListener, NonAiAdapterLT.OnNonAiItemClickListener {

    ProgressDialog progressDialog;
    RecyclerView rv_lt, rv_lt_non_ai;
    TextView tv_error_non_ai, tv_error_ai;

    private String FROM = "", DTR_IMAGE = "", POLE_IMG_AI = "", NON_AI_IMAGE = "", CONDUTOR_IMAGE = "";
    ImageView dtr_image, pole_image_ai, non_ai_image, conductor_image;
    RelativeLayout logout_layout;
    RelativeLayout profile_layout;

    public static ArrayList<PoleModelLT> polesListLT;
    public static ArrayList<PoleModelLT> nearByListLT;

    public static ArrayList<PoleModelLT> nearByPolesList;

    double nearByDistance = 200000;

    String selectedDtrCode="", selectedLTCircuitCode="", selectedDTRLocation="";

    TextView ss_name, feeder_name, feeder_id;
    public static final int GALLERY_RESULT_CODE_IMAGE_1 = 4;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat, currentLog;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    PoleModelLT modelLT;

    public static ArrayList<PoleModelLT> reportListLT;
    TextView total_defects, rectified, not_rectified, not_verified;

    public static boolean isDataChanged = false;

    @Override
    protected void onResume() {
        super.onResume();

        // System.out.println("ON RESUME CALLED : "+isDataChanged);

        if (isDataChanged) {
            isDataChanged = false;
            int rectifiedCount = 0;
            int notRectifiedCount = 0;
            int notVerifiedCount = 0;

            for (int i = 0; i < reportListLT.size(); i++) {
                PoleModelLT pole = reportListLT.get(i);
                String isRectified = pole.getIS_RECTIFIED();
                String aiPending = pole.getAI_PENDING();

                // System.out.println("ON RESUME CALLED IS_RECTIFIED : "+isRectified);
                // System.out.println("ON RESUME CALLED AI_PENDING: "+aiPending);

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

            ArrayList<PoleModelLT> tempList = new ArrayList<>();
            for (int i = 0; i < polesListLT.size(); i++) {
                if (polesListLT.get(i).getIS_RECTIFIED().equalsIgnoreCase("N") && polesListLT.get(i).getAI_PENDING().equalsIgnoreCase("N")) {
                    tempList.add(polesListLT.get(i));
                }
            }


        }

        for (int i = 0; i < aiPoleListLT.size(); i++) {
            if ("P".equalsIgnoreCase(aiPoleListLT.get(i).getGRP_ID()) ||
                    "N".equalsIgnoreCase(aiPoleListLT.get(i).getGRP_ID()) ||
                    "T".equalsIgnoreCase(aiPoleListLT.get(i).getGRP_ID())
            ) {
                POLE_IMG_AI = aiPoleListLT.get(i).getIMG_URL();
            }else if ("D".equalsIgnoreCase(aiPoleListLT.get(i).getGRP_ID())){
                DTR_IMAGE = aiPoleListLT.get(i).getIMG_URL();
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
        for (int i = 0; i < nonAiPoleListLT.size(); i++) {
            if ("P".equalsIgnoreCase(nonAiPoleListLT.get(i).getGRP_ID())) {
                NON_AI_IMAGE = nonAiPoleListLT.get(i).getIMG_URL();
            } else if ("C".equalsIgnoreCase(nonAiPoleListLT.get(i).getGRP_ID())) {
                CONDUTOR_IMAGE = nonAiPoleListLT.get(i).getIMG_URL();
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lt_pole_rectify);
        getSupportActionBar().hide();
        isDataChanged = false;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        nearByListLT = new ArrayList<>();
        polesListLT = new ArrayList<>();
        nearByPolesList = new ArrayList<>();
        for (int i = 0; i < SelectLTCircuitRectify.polesListLT.size(); i++) {
            if (SelectLTCircuitRectify.polesListLT.get(i).getIS_RECTIFIED().equalsIgnoreCase("N") && SelectLTCircuitRectify.polesListLT.get(i).getAI_PENDING().equalsIgnoreCase("N")) {
                polesListLT.add(SelectLTCircuitRectify.polesListLT.get(i));
            }
        }
        //polesListLT = SelectLTCircuitRectify.polesListLT;
        reportListLT = SelectLTCircuitRectify.polesListLT;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLatLongs();

        rv_lt = findViewById(R.id.rv_lt);
        tv_error_ai = findViewById(R.id.tv_error_ai);
        tv_error_non_ai = findViewById(R.id.tv_error_non_ai);
        rv_lt_non_ai = findViewById(R.id.rv_lt_non_ai);
        dtr_image = findViewById(R.id.dtr_image);
        pole_image_ai = findViewById(R.id.pole_image);
        non_ai_image = findViewById(R.id.non_ai_image);
        conductor_image = findViewById(R.id.conductor_image);
        logout_layout = findViewById(R.id.logout_layout);
        profile_layout = findViewById(R.id.profile_layout);
//        ss_name = findViewById(R.id.ss_name);
//        feeder_name = findViewById(R.id.feeder_name);
//        feeder_id = findViewById(R.id.feeder_id);

        total_defects = findViewById(R.id.total_defects);
        rectified = findViewById(R.id.rectified);
        not_rectified = findViewById(R.id.not_rectified);
        not_verified = findViewById(R.id.not_verified);


        selectedDtrCode = getIntent().getStringExtra("DTR_CODE");
        selectedLTCircuitCode = getIntent().getStringExtra("LT_CKT_CODE");
        selectedDTRLocation = getIntent().getStringExtra("DTR_LOCATION");

//        if (!selectedDTRLocation.isEmpty()){
//            ss_name.setText(selectedDTRLocation);
//        }
//
//        if (!selectedDtrCode.isEmpty()){
//            feeder_name.setText(selectedDtrCode);
//        }
//
//        if (!selectedLTCircuitCode.isEmpty()){
//            feeder_id.setText(selectedLTCircuitCode);
//        }




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


        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imagePickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Uri imageUri = result.getData().getData();
                                try {
                                    Bitmap bitmap;

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
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
                                                // String CURRENT_IMAGE_PATH = getRealPathFromURI(imageUri);
                                                File file = uriToFile(this, imageUri);
                                                PostingModel postingModel = new PostingModel();
                                                postingModel.setID(Long.toString(createRandomUID()));

                                                postingModel.setVOLTAGE_LEVEL("LT");
                                                postingModel.setFROM("RECTIFY");
                                                postingModel.setAI_RESPONSE(result1.status);
                                                postingModel.setPOLE_NO(modelLT.getPOLE_NO());
                                                postingModel.setPOLE_ID(modelLT.getPOLE_ID());
                                                postingModel.setFEEDER_ID("");
                                                postingModel.setDTR_CODE(modelLT.getDTR_CODE());
                                                postingModel.setLT_CKT_CODE(modelLT.getLT_CKT_CODE());
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

                                                PmiDatabase pmiDatabase = new PmiDatabase(SelectLTPoleRectify.this);
                                                pmiDatabase.insertOrUpdatePmiPostingForRectify(postingModel);
                                                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();

                                                modelLT.setAI_PENDING("Y");

                                                for (int i = 0; i < polesListLT.size(); i++) {
                                                    if (polesListLT.get(i).getPOLE_ID().equals(modelLT.getPOLE_ID()) && polesListLT.get(i).getDEFECT_GROUP().equals(modelLT.getDEFECT_GROUP())) {
                                                        // System.out.println("DEBUG MATCH FOUND in polesListLT at index: " + i);
                                                        polesListLT.set(i, modelLT);
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
                                                // onResume();

                                                //onBackPressed();
                                            }
                                        }
                                    } else {
                                        // String CURRENT_IMAGE_PATH = getRealPathFromURI(imageUri);
                                        File file = uriToFile(this, imageUri);
                                        PostingModel postingModel = new PostingModel();
                                        postingModel.setID(Long.toString(createRandomUID()));

                                        postingModel.setVOLTAGE_LEVEL("LT");
                                        postingModel.setFROM("RECTIFY");
                                        postingModel.setAI_RESPONSE(result1.status);
                                        postingModel.setPOLE_NO(modelLT.getPOLE_NO());
                                        postingModel.setPOLE_ID(modelLT.getPOLE_ID());
                                        postingModel.setFEEDER_ID("");
                                        postingModel.setDTR_CODE(modelLT.getDTR_CODE());
                                        postingModel.setLT_CKT_CODE(modelLT.getLT_CKT_CODE());
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

                                        PmiDatabase pmiDatabase = new PmiDatabase(SelectLTPoleRectify.this);
                                        pmiDatabase.insertOrUpdatePmiPostingForRectify(postingModel);
                                        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();

                                        modelLT.setAI_PENDING("Y");

                                        for (int i = 0; i < polesListLT.size(); i++) {
                                            if (polesListLT.get(i).getPOLE_ID().equals(modelLT.getPOLE_ID()) && polesListLT.get(i).getDEFECT_GROUP().equals(modelLT.getDEFECT_GROUP())) {
                                                // System.out.println("DEBUG MATCH FOUND in polesListLT at index: " + i);
                                                polesListLT.set(i, modelLT);
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
                                        // onResume();

                                        //onBackPressed();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
    }

    @Override
    public void onItemClick(PoleModelLT model) {

        if (ConnectivityChecking.checkConnection(SelectLTPoleRectify.this)) {
            Intent intent = new Intent(SelectLTPoleRectify.this, RectifyMapLT.class);
            intent.putExtra("LT_CKT_CODE", selectedLTCircuitCode);
            intent.putExtra("DTR_CODE", selectedDtrCode);
            intent.putExtra("DTR_LOCATION", selectedDTRLocation);
            intent.putExtra("MODEL", model);
            startActivity(intent);
        } else {
            launchGalleryIntent(GALLERY_RESULT_CODE_IMAGE_1, model);
        }

    }

    private void launchGalleryIntent(int RequestCode, PoleModelLT model) {

        /*
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RequestCode);
        */


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

                            for (int i = 0; i < aiPoleListLT.size(); i++) {
                                double distance = getDistanceBetweenPolesNormal(
                                        aiPoleListLT.get(i).getGLONG(),
                                        aiPoleListLT.get(i).getGLAT(),
                                        currentLat, currentLog
                                );

                                int myInt = (int) Math.round(distance);
                                aiPoleListLT.get(i).setDISTANCE(String.valueOf(myInt));
                                if (distance < nearByDistance) {
                                    nearByPolesList.add(aiPoleListLT.get(i));
                                }
                            }

                            for (int i = 0; i < nonAiPoleListLT.size(); i++) {
                                double distance = getDistanceBetweenPolesNormal(
                                        nonAiPoleListLT.get(i).getGLONG(),
                                        nonAiPoleListLT.get(i).getGLAT(),
                                        currentLat, currentLog
                                );

                                int myInt = (int) Math.round(distance);
                                nonAiPoleListLT.get(i).setDISTANCE(String.valueOf(myInt));
                                if (distance < nearByDistance) {
                                    nearByPolesList.add(nonAiPoleListLT.get(i));
                                }
                            }

                            if (aiPoleListLT.isEmpty()) {
                                tv_error_ai.setVisibility(View.VISIBLE);
                                rv_lt.setVisibility(View.GONE);
                            } else {
                                rv_lt.setVisibility(View.VISIBLE);
                                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(SelectLTPoleRectify.this);
                                rv_lt.setLayoutManager(layoutManager1);
                                rv_lt.setFocusable(false);
                                RVPolesAdapterRectifyLT adapter1 = new RVPolesAdapterRectifyLT(SelectLTPoleRectify.this, aiPoleListLT, SelectLTPoleRectify.this);
                                rv_lt.setAdapter(adapter1);
                                rv_lt.setNestedScrollingEnabled(true);
                                tv_error_ai.setVisibility(View.GONE);
                            }


                            if (nonAiPoleListLT.isEmpty()) {
                                tv_error_non_ai.setVisibility(View.VISIBLE);
                                rv_lt_non_ai.setVisibility(View.GONE);
                            } else {
                                rv_lt_non_ai.setVisibility(View.VISIBLE);
                                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(SelectLTPoleRectify.this);
                                rv_lt_non_ai.setLayoutManager(layoutManager2);
                                rv_lt_non_ai.setFocusable(false);
                                NonAiAdapterLT adapter2 = new NonAiAdapterLT(SelectLTPoleRectify.this, nonAiPoleListLT, SelectLTPoleRectify.this);
                                rv_lt_non_ai.setAdapter(adapter2);
                                rv_lt_non_ai.setNestedScrollingEnabled(true);
                                tv_error_non_ai.setVisibility(View.GONE);
                            }

                        } else {
                            // getFeederMasterAPI(0.0, 0.0);
                            Toast.makeText(SelectLTPoleRectify.this, "Unable to retrieve location.", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        Intent playIntent = new Intent(SelectLTPoleRectify.this, PMINewHome.class); //Intent.ACTION_VIEW);
        startActivity(playIntent);
        finish();
    }

    @Override
    public void onNonAiItemClick(PoleModelLT model) {

        if (ConnectivityChecking.checkConnection(SelectLTPoleRectify.this)) {
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

            launchGalleryIntent(GALLERY_RESULT_CODE_IMAGE_1, model);
        }
    }

    private void showDefectSubTypeBottomSheet(
            PoleModelLT model,
            String defectSubType) {

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(SelectLTPoleRectify.this);

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
                        SelectLTPoleRectify.this
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
                        SelectLTPoleRectify.this,
                        "Please select at least one defect",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // You can pass selected defects to next screen
            Intent intent = new Intent(
                    SelectLTPoleRectify.this,
                    RectifyMapLT.class
            );

            intent.putExtra("LT_CKT_CODE", selectedLTCircuitCode);
            intent.putExtra("DTR_CODE", selectedDtrCode);
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

    private void proceedWithPoleClick(PoleModelLT model) {


        if (ConnectivityChecking.checkConnection(
                SelectLTPoleRectify.this)) {

            Intent intent = new Intent(
                    SelectLTPoleRectify.this,
                    RectifyMapLT.class
            );

            intent.putExtra("LT_CKT_CODE", selectedLTCircuitCode);
            intent.putExtra("DTR_CODE", selectedDtrCode);
            intent.putExtra("MODEL", model);
            intent.putExtra("MODEL", model);

            startActivity(intent);

        } else {

            launchGalleryIntent(
                    GALLERY_RESULT_CODE_IMAGE_1,
                    model
            );
        }
    }
}
