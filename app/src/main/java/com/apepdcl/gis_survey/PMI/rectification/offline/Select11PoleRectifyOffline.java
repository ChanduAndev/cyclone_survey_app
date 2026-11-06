package com.apepdcl.gis_survey.PMI.rectification.offline;

import static com.apepdcl.gis_survey.PMI.rectification.Select11KVFeederRectify.aiPoleList11;
import static com.apepdcl.gis_survey.PMI.rectification.Select11KVFeederRectify.nonAiPoleList11;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.PMI.models.AIResponseModel;
import com.apepdcl.gis_survey.PMI.models.OfflineDataFor11;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.PMI.new_screens.TFLiteObjectDetector;
import com.apepdcl.gis_survey.PMI.rectification.NonAiAdapter11;
import com.apepdcl.gis_survey.PMI.rectification.RVPolesAdapterRectify11;
import com.apepdcl.gis_survey.PMI.rectification.RectifyMap11;
import com.apepdcl.gis_survey.PMI.rectification.Select11PoleRectify;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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

public class Select11PoleRectifyOffline extends AppCompatActivity implements NonAiAdapterOffline.OnNonAiItemClickListener, AiAdapterOffline.OnNonAiItemClickListener {
    PmiDatabase pmiDatabase;
    RecyclerView rv_11_ai, rv_11_non_ai;
    ArrayList<OfflineDataFor11> aiListNew = new ArrayList<>();
    ArrayList<OfflineDataFor11> nearByPolesList = new ArrayList<>();
    ArrayList<OfflineDataFor11> nonAiList = new ArrayList<>();
    NonAiAdapterOffline nonAiAdapterOffline;
    AiAdapterOffline aiAdapterOffline;
    double nearByDistance = 150;
    String SELECTED_POLE_NO = "", SELECTED_FEEDER_ID = "";
    public static String SELECTED_VOLTAGE_LEVEL = "";
    public static Bitmap capturedBitmapPMI1 = null;
    public static boolean isImageCapturedPMI1 = false;
    public static boolean photoClickedPMI1 = false;
    OfflineDataFor11 modelLT;
    private ArrayList<String> selectedSubDefectTypes;
    String subDefectType = "", sub_def = "";
    ;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat, currentLog;
    int OFFLINE_DISTANCE = 0, ONLINE_DISTANCE = 0;

    boolean isAiClick = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_11_pole_rectify);
        rv_11_ai = findViewById(R.id.rv_33);
        rv_11_non_ai = findViewById(R.id.rv_11_non_ai);
        SELECTED_POLE_NO = getIntent().getStringExtra("SELECTED_POLE_NO");
        SELECTED_FEEDER_ID = getIntent().getStringExtra("SELECTED_FEEDER_ID");
        SELECTED_VOLTAGE_LEVEL = getIntent().getStringExtra("SELECTED_VOLTAGE_LEVEL");
        //System.out.println("SELECTED_VOLTAGE_LEVEL" + SELECTED_VOLTAGE_LEVEL);
        pmiDatabase = new PmiDatabase(Select11PoleRectifyOffline.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        int[] distanceData = pmiDatabase.getPmiDistanceCheckData();
//        OFFLINE_DISTANCE = distanceData[0];
//        ONLINE_DISTANCE = distanceData[1];
//        nearByDistance = OFFLINE_DISTANCE;
        getLatLongs();
    }

    private void setNonAiAdapter() {
        System.out.println("SIZE_OF_NON_AI " + nonAiList.size());
        rv_11_non_ai.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Select11PoleRectifyOffline.this);
        rv_11_non_ai.setLayoutManager(layoutManager);
        rv_11_non_ai.setNestedScrollingEnabled(true);
        rv_11_non_ai.setHasFixedSize(false);
        nonAiAdapterOffline = new NonAiAdapterOffline(Select11PoleRectifyOffline.this, nonAiList, Select11PoleRectifyOffline.this);
        rv_11_non_ai.setAdapter(nonAiAdapterOffline);
        nonAiAdapterOffline.notifyDataSetChanged();

    }


    private void setAiAdapter() {
        rv_11_ai.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Select11PoleRectifyOffline.this);
        rv_11_ai.setLayoutManager(layoutManager);
        rv_11_ai.setNestedScrollingEnabled(false);
        rv_11_ai.setHasFixedSize(false);
        aiAdapterOffline = new AiAdapterOffline(Select11PoleRectifyOffline.this, aiListNew, Select11PoleRectifyOffline.this);
        rv_11_ai.setAdapter(aiAdapterOffline);
        aiAdapterOffline.notifyDataSetChanged();

    }


    @Override
    public void onNonAiItemClick(OfflineDataFor11 model) {

        isAiClick = false;
        String distanceStr = model.getDISTANCE();

        if (distanceStr == null || distanceStr.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Distance not available. Please try again.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        try {
            double distance = Double.parseDouble(distanceStr.trim());

            if (distance > 150) {
                openAlertDialog("You are approx. " + distance + " meters away from the pole location. Please go near (or within 20 meters radius) to the pole to capture image");
                return;
            }
            selectedSubDefectTypes = null;
            subDefectType = "";

            modelLT = model;

            String defectSubType = model.getDEFECT_SUB_TYPE();

            Log.d(
                    "PMI_RECTIFY",
                    "Clicked DEFECT_SUB_TYPE = " + defectSubType
            );

            if (defectSubType != null && defectSubType.contains(",")) {


                showDefectSubTypeBottomSheet(
                        model,
                        defectSubType
                );

            } else {

                openCamera();
            }
        } catch (NumberFormatException e) {

            Log.e(
                    "PMI_RECTIFY",
                    "Invalid distance = " + distanceStr,
                    e
            );
        }


    }

    private void openAlertDialog(String msg) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        // Hide unnecessary views
        tv_est_no.setVisibility(View.GONE);
        tv_gis_id.setVisibility(View.GONE);

        // Set warning icon
        iv_icon.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.warning)
        );

        // Set dialog data
        title.setText("Pay Attention");
        message.setText(msg);

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        okBtn.setText("Okay");

        // OK button action
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            onBackPressed();
        });

        // Prevent dismissing by tapping outside or back button
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
        }

        dialog.show();
    }

    private void showDefectSubTypeBottomSheet(
            OfflineDataFor11 model,
            String defectSubType) {

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(Select11PoleRectifyOffline.this);

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
                        Select11PoleRectifyOffline.this
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
                        Select11PoleRectifyOffline.this,
                        "Please select at least one defect",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            selectedSubDefectTypes = selectedDefects;
            openCamera();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    @Override
    public void onAiItemClick(OfflineDataFor11 model) {
        isAiClick = true;
        String distanceStr = model.getDISTANCE();

        if (distanceStr == null || distanceStr.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Distance not available. Please try again.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }
        try {
            double distance = Double.parseDouble(distanceStr.trim());

            if (distance > 150) {
                openAlertDialog("You are approx. " + distance + " meters away from the pole location. Please go near (or within 20 meters radius) to the pole to capture image");
                return;
            }
            // Clear previous selection
            selectedSubDefectTypes = null;
            subDefectType = "";

            // Set the newly clicked model
            modelLT = model;

            String defectSubType = model.getDEFECT_SUB_TYPE();

            if (defectSubType != null && defectSubType.contains(",")) {

                // More than one defect subtype
                showDefectSubTypeBottomSheet(model, defectSubType);

            } else {
                // Single defect
                openCamera();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectedSubDefectTypes != null && !selectedSubDefectTypes.isEmpty()) {
            subDefectType = TextUtils.join(", ", selectedSubDefectTypes);
        }
        sub_def = subDefectType;
        nonAiList = pmiDatabase.getOfflineDataPoleNoWise(SELECTED_FEEDER_ID, SELECTED_POLE_NO, "NON AI");
        aiListNew = pmiDatabase.getOfflineDataPoleNoWise(SELECTED_FEEDER_ID, SELECTED_POLE_NO, "AI");

        //getLatLongs();
//        if (!nonAiList.isEmpty()) {
//            setNonAiAdapter();
//        }
//
//        if (!aiListNew.isEmpty()) {
//            setAiAdapter();
//        }
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
                    System.out.println("SELECTED_VOLTAGE_LEVEL=====" + SELECTED_VOLTAGE_LEVEL);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error reading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
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

            postingModel.setVOLTAGE_LEVEL(SELECTED_VOLTAGE_LEVEL);
            postingModel.setFROM("RECTIFY");

            postingModel.setPOLE_NO(
                    modelLT.getPOLE_NO()
            );

            postingModel.setPOLE_ID(
                    modelLT.getPOLE_ID()
            );
            System.out.println("POLE_ID::::::: " + modelLT.getPOLE_ID());

            postingModel.setFEEDER_ID(
                    modelLT.getFEEDER_ID()
            );

            postingModel.setDTR_CODE("");
            postingModel.setLT_CKT_CODE("");
            postingModel.setDEFECT_SUB_TYPE(modelLT.getDEFECT_SUB_TYPE());

            if (subDefectType.isEmpty()) {
                postingModel.setDEFECT_SUB_TYPE(modelLT.getDEFECT_SUB_TYPE());
            } else {
                postingModel.setDEFECT_SUB_TYPE(subDefectType);
            }
            if (isAiClick) {
                if ("D".equalsIgnoreCase(modelLT.getGRP_ID())) {
                    postingModel.setDTR_IMAGE(imageFile.getAbsolutePath());
                    System.out.println("GRP_ID::::::: " + modelLT.getGRP_ID() + " DTR_IMAGE" + imageFile.getAbsolutePath());
                } else if ("P".equalsIgnoreCase(modelLT.getGRP_ID())
                        || "T".equalsIgnoreCase(modelLT.getGRP_ID())
                        || "N".equalsIgnoreCase(modelLT.getGRP_ID())) {

                    postingModel.setAI_IMAGE(imageFile.getAbsolutePath());
                }

            } else {
                postingModel.setIMAGE(imageFile.getAbsolutePath());
            }

            System.out.println("AI_OR_NON_AI::::" + isAiClick);


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
                    System.out.println("RESPONSE " + result1.status);

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
                    new PmiDatabase(Select11PoleRectifyOffline.this);

            pmiDatabase.insertOrUpdatePmiPostingForRectify(
                    postingModel
            );
            pmiDatabase.checkPostingTable();

            if (selectedSubDefectTypes != null
                    && !selectedSubDefectTypes.isEmpty()) {

                for (String defect : selectedSubDefectTypes) {

                    if (defect != null && !defect.trim().isEmpty()) {

                        boolean updated =
                                pmiDatabase.updateCompleteSurveyToY(
                                        modelLT.getFEEDER_ID(),
                                        modelLT.getPOLE_ID(),
                                        defect.trim(),
                                        modelLT.getAI_FLAG()
                                );

                        Log.d(
                                "PMI_RECTIFY",
                                "Updated defect = "
                                        + defect
                                        + " | result = "
                                        + updated
                        );
                    }
                }

            } else {

                boolean updated =
                        pmiDatabase.updateCompleteSurveyToY(
                                modelLT.getFEEDER_ID(),
                                modelLT.getPOLE_ID(),
                                modelLT.getDEFECT_SUB_TYPE().trim(),
                                modelLT.getAI_FLAG()
                        );

                Log.d(
                        "PMI_RECTIFY",
                        "Updated defect = "
                                + modelLT.getDEFECT_SUB_TYPE()
                                + " | result = "
                                + updated
                );
            }

            nonAiList = pmiDatabase.getOfflineDataPoleNoWise(SELECTED_FEEDER_ID, SELECTED_POLE_NO, "NON AI");
            aiListNew = pmiDatabase.getOfflineDataPoleNoWise(SELECTED_FEEDER_ID, SELECTED_POLE_NO, "AI");

            getLatLongs();
            if (!nonAiList.isEmpty()) {
                setNonAiAdapter();
            }

            if (!aiListNew.isEmpty()) {
                setAiAdapter();
            }
            if (nonAiList.isEmpty() && aiListNew.isEmpty()) {
                Intent intent = new Intent(Select11PoleRectifyOffline.this, OfflineRectifySurvey.class);
                intent.putExtra("SELECTED_FEEDER_ID", SELECTED_FEEDER_ID);
                intent.putExtra("SELECTED_VOLTAGE_LEVEL", SELECTED_VOLTAGE_LEVEL);
                startActivity(intent);
            }

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

    private long createRandomUID() {
        UUID uuid = UUID.randomUUID();
        // System.out.println("UUID: " + uuid);
        long randomNumber = uuid.getLeastSignificantBits();
        randomNumber = Math.abs(randomNumber);
        //System.out.println("Random Number: " + randomNumber);
        return randomNumber;
    }

    private void openCamera() {
        photoClickedPMI1 = true;
        isImageCapturedPMI1 = false;
        capturedBitmapPMI1 = null;
        Intent intentOpenCamera = new Intent(Select11PoleRectifyOffline.this, MainActivity.class);
        intentOpenCamera.putExtra("SELECTED_VOLTAGE_LEVEL", SELECTED_VOLTAGE_LEVEL);
        startActivity(intentOpenCamera);


    }

    private void getLatLongs() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {

                        if (location == null) {

                            Toast.makeText(
                                    Select11PoleRectifyOffline.this,
                                    "Unable to retrieve location.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        currentLat = location.getLatitude();
                        currentLog = location.getLongitude();

                        Log.d("PMI_DISTANCE",
                                "CURRENT LAT = " + currentLat
                                        + " | CURRENT LON = " + currentLog);

                        // Prevent duplicate entries
                        nearByPolesList.clear();

                        // =========================
                        // AI POLES
                        // =========================
                        for (int i = 0; i < aiListNew.size(); i++) {

                            OfflineDataFor11 model = aiListNew.get(i);

                            double distance =
                                    getDistanceBetweenPolesNormal(
                                            model.getGLONG(),
                                            model.getGLAT(),
                                            currentLat,
                                            currentLog
                                    );

                            int myInt = (int) Math.round(distance);

                            model.setDISTANCE(
                                    String.valueOf(myInt)
                            );

                            Log.d("PMI_DISTANCE",
                                    "AI POLE = " + model.getPOLE_NO()
                                            + " | DISTANCE = " + myInt);

                            if (distance < nearByDistance) {
                                nearByPolesList.add(model);
                            }
                        }

                        // =========================
                        // NON AI POLES
                        // =========================
                        for (int i = 0; i < nonAiList.size(); i++) {

                            OfflineDataFor11 model = nonAiList.get(i);

                            double distance =
                                    getDistanceBetweenPolesNormal(
                                            model.getGLONG(),
                                            model.getGLAT(),
                                            currentLat,
                                            currentLog
                                    );

                            int myInt = (int) Math.round(distance);

                            model.setDISTANCE(
                                    String.valueOf(myInt)
                            );

                            Log.d("PMI_DISTANCE",
                                    "NON AI POLE = " + model.getPOLE_NO()
                                            + " | DISTANCE = " + myInt);

                            if (distance < nearByDistance) {
                                nearByPolesList.add(model);
                            }
                        }

                        Log.d("PMI_DISTANCE",
                                "AI COUNT = " + aiListNew.size()
                                        + " | NON AI COUNT = " + nonAiList.size());

                        // IMPORTANT:
                        // Set adapters AFTER distance is calculated
                        if (!nonAiList.isEmpty()) {
                            setNonAiAdapter();
                        }

                        if (!aiListNew.isEmpty()) {
                            setAiAdapter();
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
        //System.out.println("CURRENT LOCATION LAT DB : " + currentLon);
        //System.out.println("CURRENT LOCATION LON DB: " + currentLat);
        //System.out.println("POLE LOCATION LAT DB: " + poleLat);
        //System.out.println("POLE LOCATION LON DB: " + poleLon);
        try {
            Location currentLocation = new Location("current");
            currentLocation.setLatitude(currentLat);   // ✅ LAT
            currentLocation.setLongitude(currentLon); // ✅ LON

            Location poleLocation = new Location("pole");
            poleLocation.setLatitude(Double.parseDouble(poleLat));   // ✅ LAT
            poleLocation.setLongitude(Double.parseDouble(poleLon)); // ✅ LON
            return poleLocation.distanceTo(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE; // safety fallback
        }
    }
}
