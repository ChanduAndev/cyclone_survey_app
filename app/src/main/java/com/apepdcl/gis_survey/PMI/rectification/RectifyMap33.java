package com.apepdcl.gis_survey.PMI.rectification;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.PmiDatabase;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.AIResponseModel;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.PMI.new_screens.ImageRetrofitClient;
import com.apepdcl.gis_survey.PMI.new_screens.ImageRetrofitService;
import com.apepdcl.gis_survey.PMI.new_screens.TFLiteObjectDetector;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.sourceforge.app.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 23-03-2026.
 */
public class RectifyMap33 extends AppCompatActivity {

    String ID, NAME;
    RelativeLayout back_layout, current_location_layout;
    MapView mv;
    GpsTraker gpsTraker, currentLocationGpsTracker;
    LocationDisplay mLocationDisplay;
    GraphicsOverlay mGraphicsOverlay;
    ArcGISMap map;
    TextView name_value, id_value,damaged_type_33kv;
    PointCollection polylinePoints;
    GraphicsOverlay graphicsOverlay;
    private Point ptWgs84;
    Graphic graphic;

    RelativeLayout reload_map_layout;
    ProgressDialog progressDialog;
    TFLiteObjectDetector detector;

    PoleModel33KV model;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mVoltageProcessingRunnable;
    private static final long PROCESS_DELAY_MS = 100;

    public static Bitmap capturedBitmapPMI1 = null;
    public static boolean isImageCapturedPMI1 = false;
    public static boolean photoClickedPMI1 = false;

    String CURRENT_IMAGE_PATH;
    private RetrofitServicePMI retrofitServicePmi;

    Dialog aiDialog;
    TextView tv_pole_number;
    int warning = 0;
    private ArrayList<String> selectedSubDefectTypes;
    String subDefectType = "";
    PmiDatabase pmiDatabase;
    int OFFLINE_DISTANCE=150000,ONLINE_DISTANCE=500000000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify_map_33);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);

        ID = getIntent().getStringExtra("FEEDER_ID");
        NAME = getIntent().getStringExtra("FEEDER_NAME");
        model = (PoleModel33KV) getIntent().getSerializableExtra("MODEL");
        selectedSubDefectTypes=getIntent().getStringArrayListExtra("SELECTED_SUB_DEFECT_TYPE");


        System.out.println("SELECTED_SUB_DEFECT_TYPE    :"+selectedSubDefectTypes);

        if (selectedSubDefectTypes != null && !selectedSubDefectTypes.isEmpty()) {
            subDefectType = TextUtils.join(", ", selectedSubDefectTypes);
        }else {
            subDefectType=getIntent().getStringExtra("SELECTED_SUB_DEFECT_TYPE");
        }
        System.out.println("SELECTED_SUB_DEFECT_TYPE______    :"+subDefectType);

        detector = new TFLiteObjectDetector(this);

        back_layout = findViewById(R.id.back_layout);
        reload_map_layout = findViewById(R.id.reload_map_layout);
        current_location_layout = findViewById(R.id.close_layout);
        mv = findViewById(R.id.map1);
        tv_pole_number = findViewById(R.id.tv_pole_number);

        name_value = findViewById(R.id.name_value);
        id_value = findViewById(R.id.id_value);
        damaged_type_33kv = findViewById(R.id.damaged_type_33kv);
        name_value.setText(NAME);
        id_value.setText(ID);
        tv_pole_number.setText("" + model.getPOLE_NO());

        gpsTraker = new GpsTraker(this);
        mGraphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(mGraphicsOverlay);
        damaged_type_33kv.setText(subDefectType);


        map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
        mv.setMap(map);
        mLocationDisplay = mv.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }


            String message = String.format("Error in DataSourceStatusChangedListener: %s",
                    dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
            Toast.makeText(RectifyMap33.this, message, Toast.LENGTH_LONG).show();

        });
        // mLocationDisplay.stop();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        current_location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentLocation();
            }
        });

        reload_map_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmDialog();
            }
        });
//        pmiDatabase =new PmiDatabase(RectifyMap33.this);
//        int [] distanceData=pmiDatabase.getPmiDistanceCheckData();
//        OFFLINE_DISTANCE=distanceData[0];
//        ONLINE_DISTANCE=distanceData[1];
    }

    private void openConfirmDialog() {

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

        // Hide unused fields
        tv_est_no.setVisibility(View.GONE);
        tv_gis_id.setVisibility(View.GONE);

        // Set icon
        iv_icon.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.warning)
        );

        // Set dialog content
        title.setText("Confirmation");
        message.setText("Pole Number: " + model.getPOLE_NO()
                + "\n Do you want to proceed?");

        // Configure buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

        okBtn.setText("Proceed");
        cancelBtn.setText("Cancel");

        // Proceed button
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            openCamera();
        });

        // Cancel button
        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
            onBackPressed();
        });

        // Disable dismiss on outside touch
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // Dialog styling
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );

            dialog.getWindow().getAttributes().windowAnimations =
                    R.style.DialogAnimation;

            // Optional: Set width similar to previous dialog
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width = displayMetrics.widthPixels;
            dialog.getWindow().setLayout(
                    (int) (width * 0.90),  // 90% screen width
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        dialog.show();
    }

//    private void openConfirmDialog() {
//        Dialog payDialog = new Dialog(this);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        payDialog.setContentView(R.layout.rectify_confirm_dialog);
//        payDialog.getWindow().setLayout(width, height / 4);
//        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        TextView cancel_button = payDialog.findViewById(R.id.cancel_button);
//        TextView proceed_button = payDialog.findViewById(R.id.proceed_button);
//        TextView tv_pole_number_dialog = payDialog.findViewById(R.id.tv_pole_number_dialog);
//        tv_pole_number_dialog.setText("Pole Number: "+model.getPOLE_NO());
//
//
//
//
//        cancel_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                payDialog.dismiss();
//                onBackPressed();
//            }
//        });
//
//        proceed_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                payDialog.dismiss();
//                openCamera();
//            }
//        });
//
//
//
//        payDialog.show();
//
//    }


    private void openCamera() {
        photoClickedPMI1 = true;
        isImageCapturedPMI1 = false;
        capturedBitmapPMI1 = null;
        Intent intentOpenCamera = new Intent(RectifyMap33.this, MainActivity.class);
        startActivity(intentOpenCamera);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!hasFocus) {
            // Remove pending tasks if window loses focus
            if (mVoltageProcessingRunnable != null) {
                mHandler.removeCallbacks(mVoltageProcessingRunnable);
            }
            return;
        }

        // Cancel any pending processing
        if (mVoltageProcessingRunnable != null) {
            mHandler.removeCallbacks(mVoltageProcessingRunnable);
        }

        // Schedule processing with a small delay to avoid ANR
        mVoltageProcessingRunnable = new Runnable() {
            @Override
            public void run() {
                processVoltageDataOnUiThread();
            }
        };
        mHandler.postDelayed(mVoltageProcessingRunnable, PROCESS_DELAY_MS);
    }

    private void processVoltageDataOnUiThread() {
        // Similar pattern for LT
        mHandler.post(new Runnable() {
            private GpsTraker tempGpsTracker = new GpsTraker(RectifyMap33.this);
            private double currentLat = tempGpsTracker.getLatitude();
            private double currentLon = tempGpsTracker.getLongitude();
            private int maxDistance = 0;

            @Override
            public void run() {
                try {
                    //loadPoleOnMap();
                    loadAllPolesOnMap(Select33PoleRectify.nearByPolesList);

                    double distance = getDistanceBetweenPolesLT(
                            model.getGLONG(),
                            model.getGLAT(),
                            currentLat, currentLon
                    );
                    if (ConnectivityChecking.checkConnection(RectifyMap33.this)) {
                        maxDistance = ONLINE_DISTANCE;
                    } else {
                        maxDistance = OFFLINE_DISTANCE;
                    }
                    if (distance <= maxDistance) {
                        reload_map_layout.setVisibility(View.VISIBLE);
                    } else {
                        reload_map_layout.setVisibility(View.GONE);
                        int myInt = (int) Math.round(distance);
                        openAlertDialog("You are approx. " + myInt + " meters away from the pole location. Please go near (or within 20 meters radius) to the pole to capture image");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    //    private void openAlertDialog(String msg) {
//        new android.app.AlertDialog.Builder(this)
//                .setTitle("Pay Attention")
//                .setCancelable(false)
//                .setMessage(msg)
//                .setPositiveButton("Okay", (dialog, which) -> {
//                    dialog.cancel();
//                    onBackPressed();
//                })
//
//                .show();
//
//
//    }
    private void loadPoleOnMap() {

        mv.getGraphicsOverlays().clear();
        polylinePoints = new PointCollection(SpatialReferences.getWgs84());
        polylinePoints.add(new Point(
                Double.parseDouble(model.getGLAT()),
                Double.parseDouble(model.getPOLE_NO())
        ));

        graphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(graphicsOverlay);

        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE,
                Color.RED,
                12
        );

        ptWgs84 = new Point(
                Double.parseDouble(model.getGLAT()),
                Double.parseDouble(model.getGLONG()),
                414,
                SpatialReferences.getWgs84()
        );

        graphic = new Graphic(ptWgs84, symbol);
        graphicsOverlay.getGraphics().add(graphic);

        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
        absoluteOverlay.getSceneProperties().setSurfacePlacement(
                LayerSceneProperties.SurfacePlacement.ABSOLUTE
        );
        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));

        TextSymbol absoluteText = new TextSymbol(
                15,
                model.getPOLE_NO(),
                Color.WHITE,
                TextSymbol.HorizontalAlignment.LEFT,
                TextSymbol.VerticalAlignment.MIDDLE
        );
        absoluteText.setOffsetY(20);
        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
        mv.getGraphicsOverlays().add(absoluteOverlay);

    }

    private void loadAllPolesOnMap(List<PoleModel33KV> filteredList) {
        // filteredPolesListLT.clear();
        // filteredPolesListLT.addAll(filteredList);
        mv.getGraphicsOverlays().clear();

        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        Runnable uiUpdateRunnable = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                try {
                    int chunkSize = 10;
                    int endIndex = Math.min(currentIndex + chunkSize, filteredList.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        polylinePoints.add(new Point(
                                Double.parseDouble(filteredList.get(i).getGLAT()),
                                Double.parseDouble(filteredList.get(i).getGLONG())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.RED,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(filteredList.get(i).getGLAT()),
                                Double.parseDouble(filteredList.get(i).getGLONG()),
                                414,
                                SpatialReferences.getWgs84()
                        );

                        graphic = new Graphic(ptWgs84, symbol);
                        graphicsOverlay.getGraphics().add(graphic);

                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties().setSurfacePlacement(
                                LayerSceneProperties.SurfacePlacement.ABSOLUTE
                        );
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));

                        TextSymbol absoluteText = new TextSymbol(
                                15,
                                filteredList.get(i).getPOLE_NO(),
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    currentIndex = endIndex;

                    if (currentIndex < filteredList.size()) {
                        mHandler.postDelayed(this, 16);
                    } else {
                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mHandler.post(uiUpdateRunnable);
    }

    private double getDistanceBetweenPolesLT(
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

            // distance in METERS
            return poleLocation.distanceTo(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE; // safety fallback
        }
    }

    private void setCurrentLocation() {
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ignored) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled && !network_enabled) {
                new AlertDialog.Builder(RectifyMap33.this)
                        .setTitle("GPS Alert")
                        .setMessage("Please Enable GPS")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            } else {

                /* ** ADD ** */
                mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                    if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                        return;
                    }


                    String message = String.format("Error in DataSourceStatusChangedListener: %s",
                            dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(RectifyMap33.this, message, Toast.LENGTH_LONG).show();

                });
                // mLocationDisplay.stop();
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mv.resume();

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
        Bitmap safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        isImageCapturedPMI1 = false;
        CURRENT_IMAGE_PATH = getLastImagePath(this);
        if (ConnectivityChecking.checkConnection(RectifyMap33.this)) {
            aiDialog = new Dialog(this);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            aiDialog.setContentView(R.layout.ai_verification);
            aiDialog.setCancelable(false);
            aiDialog.getWindow().setLayout(width, 1500);
            aiDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            aiDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            View scanLine = aiDialog.findViewById(R.id.scanLine);
            FrameLayout frame = aiDialog.findViewById(R.id.frame);
            ImageView taken_image = aiDialog.findViewById(R.id.taken_image);
            Button cancel_button = aiDialog.findViewById(R.id.cancel_button);
            taken_image.setImageBitmap(safeBitmap);
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aiDialog.dismiss();
                }
            });


            frame.post(() -> {
                float heightL = frame.getHeight();

                ObjectAnimator animator = ObjectAnimator.ofFloat(
                        scanLine,
                        "translationY",
                        0f,
                        heightL - 10
                );

                animator.setDuration(2000);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.start();
            });
            aiDialog.show();
            transmitPoleWithoutAIResponse();

//            if (model != null) {
//
//                String grpId = model.getGRP_ID();
//                String aiFlag = model.getAI_FLAG();
//
//                if (aiFlag == null || aiFlag.trim().isEmpty()) {
//
//                    new AlertDialog.Builder(RectifyMap33.this)
//                            .setTitle("Alert")
//                            .setMessage("AI Flag is not available. Please Contact Admin.")
//                            .setPositiveButton("OK", null)
//                            .show();
//
//                }
//
//                else if ("AI".equalsIgnoreCase(aiFlag)
//                        && ("P".equalsIgnoreCase(grpId)
//                        || "D".equalsIgnoreCase(grpId)
//                        || "T".equalsIgnoreCase(grpId)
//                        || "C".equalsIgnoreCase(grpId)
//                        || "N".equalsIgnoreCase(grpId))) {
//
//                    uploadImageToServer(bitmap);
//
//                } else if ("NON AI".equalsIgnoreCase(aiFlag)) {
//
//                    transmitPoleWithoutAIResponse();
//                }
//            }

        } else {
            Toast.makeText(this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            /*TFLiteObjectDetector.Result result = detector.annotate(safeBitmap);
            if (!result.status.equals("VALID")) {
                Toast.makeText(this, result.status, Toast.LENGTH_SHORT).show();
                showDetectionDialog(result.status, safeBitmap);
            }else {
                PostingModel postingModel = new PostingModel();
                postingModel.setID(Long.toString(createRandomUID()));

                postingModel.setVOLTAGE_LEVEL("LT");
                postingModel.setFROM("RECTIFY");
                postingModel.setAI_RESPONSE(result.status);
                postingModel.setPOLE_NO(model.getPOLE_NO());
                postingModel.setPOLE_ID(model.getPOLE_ID());
                postingModel.setFEEDER_ID("");
                postingModel.setDTR_CODE(model.getDTR_CODE());
                postingModel.setLT_CKT_CODE(model.getLT_CKT_CODE());
                postingModel.setIMAGE(CURRENT_IMAGE_PATH);
                postingModel.setIMAGE_TYPE("OFFLINE");
                postingModel.setCURRENT_LONG(String.valueOf(gpsTraker.getLongitude()));
                postingModel.setCURRENT_LAT(String.valueOf(gpsTraker.getLatitude()));

                postingModel.setRECT_ID(model.getRECT_ID());

                ArrayList<AIResponseModel> aiList = new ArrayList<>();

                AIResponseModel aiResponseModel = new AIResponseModel();
                aiResponseModel.setAI_RESPONSE("");
                aiResponseModel.setLEAN_ANGLE("");
                aiResponseModel.setPOSITION("");
                aiResponseModel.setVOLTAGE("");
                aiResponseModel.setPOSTITION_XARM("");
                aiList.add(aiResponseModel);
                postingModel.setAiResponseModel(aiList);

                PmiDatabase pmiDatabase = new PmiDatabase(RectifyMapLT.this);
                pmiDatabase.insertOrUpdatePmiPostingForRectify(postingModel);
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }*/
        }
    }

    private void transmitPoleWithoutAIResponse() {
        aiDialog.dismiss();
        PostingModel postingModel = new PostingModel();
        postingModel.setID(Long.toString(createRandomUID()));

        postingModel.setVOLTAGE_LEVEL("33KV");
        postingModel.setFROM("RECTIFY");
        postingModel.setPOLE_NO(model.getPOLE_NO());
        postingModel.setPOLE_ID(model.getPOLE_ID());
        postingModel.setFEEDER_ID(model.getFEEDER_ID());
        postingModel.setDTR_CODE("");
        postingModel.setLT_CKT_CODE("");
        postingModel.setIMAGE(CURRENT_IMAGE_PATH);
        postingModel.setIMAGE_TYPE("ONLINE");
        postingModel.setCURRENT_LONG(String.valueOf(gpsTraker.getLongitude()));
        postingModel.setCURRENT_LAT(String.valueOf(gpsTraker.getLatitude()));
        ArrayList<AIResponseModel> aiList = new ArrayList<>();
        postingModel.setAiResponseModel(aiList);
        transmitPolesLT(postingModel, model);
    }

    private String getLastImagePath(Context context) {
        Uri imageUri = null;
        String imageFilePath = null;

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
        };

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = null;
        try {
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver cr = context.getContentResolver();
            cursor = cr.query(images, projection, null, null, sortOrder);

            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                imageUri = ContentUris.withAppendedId(images, id);

                // Use this stream to read the image
                InputStream inputStream = cr.openInputStream(imageUri);

                // Create a temporary file in the app's cache directory
                File tempFile = new File(context.getCacheDir(), name);
                FileOutputStream outputStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                imageFilePath = tempFile.getAbsolutePath(); // Here's your usable file path
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

// Use imageFilePath as needed (e.g., upload, native processing)
        return imageFilePath;
    }

    private void uploadImageToServer(Bitmap bitmap) {
        try {
            if (CURRENT_IMAGE_PATH == null || CURRENT_IMAGE_PATH.isEmpty()) {
                aiDialog.dismiss();
                Toast.makeText(this, "Invalid image path", Toast.LENGTH_SHORT).show();
                return;
            }

            // if (progressDialog != null) progressDialog.show();

            File file = new File(CURRENT_IMAGE_PATH);
            if (!file.exists()) {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
                //if (progressDialog != null) progressDialog.dismiss();
                aiDialog.dismiss();
                return;
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            ImageRetrofitService service = ImageRetrofitClient.getRetrofitInstance().create(ImageRetrofitService.class);


            //Call<JsonObject> call = service.uploadImage(imagePart, false);
            //Call<JsonObject> call = model.getDEFECT_GROUP().equalsIgnoreCase("DTR_DEFECTS") ? service.uploadImageDTRPlinthClearance(imagePart, model.getPOLE_HEIGHT()) : service.uploadImage(imagePart, false);
            Call<JsonObject> call = "D".equalsIgnoreCase(model.getGRP_ID()) ? service.uploadImageDTRPlinthClearance(imagePart, model.getPOLE_HEIGHT()) : service.uploadImage(imagePart, false);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    // if (progressDialog != null) progressDialog.dismiss();

                    if (!response.isSuccessful() || response.body() == null) {
                        //if (progressDialog != null) progressDialog.dismiss();
                        aiDialog.dismiss();
                        Toast.makeText(RectifyMap33.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JsonObject object = response.body();
                    if (object.has("status") && (object.get("status").getAsString().equalsIgnoreCase("done") || object.get("status").getAsString().equalsIgnoreCase("done_with_visibility_warning"))) {
                        if (object.get("status").getAsString().equalsIgnoreCase("done")) {
                            handleSuccessfulUpload(object, bitmap);
                        } else {
                            warning++;
                            if (warning == 2) {
                                handleSuccessfulUpload(object, bitmap);
                            } else {
                                Toast.makeText(RectifyMap33.this, "Pole not visible, Capture again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        //if (progressDialog != null) progressDialog.dismiss();
                        aiDialog.dismiss();
                        // String reason = object.has("reason") ? object.get("reason").getAsString() : "Unknown error";
                        String reason = "Unknown error";

                        if (object.has("reason") && !object.get("reason").isJsonNull()) {
                            reason = object.get("reason").getAsString();
                        }

                        Toast.makeText(RectifyMap33.this, reason, Toast.LENGTH_SHORT).show();
                        showDetectionDialog(reason, bitmap);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    //if (progressDialog != null) progressDialog.dismiss();
                    aiDialog.dismiss();
                    Toast.makeText(RectifyMap33.this, "Upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            // if (progressDialog != null) progressDialog.dismiss();
            aiDialog.dismiss();
            Toast.makeText(this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSuccessfulUpload(JsonObject object, Bitmap bitmap) {

        ArrayList<String> resultList = new ArrayList<>();

        // Build new PostingModel
        PostingModel postingModel = new PostingModel();
        postingModel.setID(Long.toString(createRandomUID()));

        postingModel.setVOLTAGE_LEVEL("33KV");
        postingModel.setFROM("RECTIFY");
        postingModel.setPOLE_NO(model.getPOLE_NO());
        postingModel.setPOLE_ID(model.getPOLE_ID());
        postingModel.setFEEDER_ID(model.getFEEDER_ID());
        postingModel.setDTR_CODE("");
        postingModel.setLT_CKT_CODE("");
        postingModel.setIMAGE(CURRENT_IMAGE_PATH);
        postingModel.setIMAGE_TYPE("ONLINE");
        postingModel.setCURRENT_LONG(String.valueOf(gpsTraker.getLongitude()));
        postingModel.setCURRENT_LAT(String.valueOf(gpsTraker.getLatitude()));


        // Process AI detections
        ArrayList<AIResponseModel> aiList = new ArrayList<>();
        StringBuilder resultSummary = new StringBuilder();

        if (object.has("pole_lean_detections")) {
            JsonArray poles = object.getAsJsonArray("pole_lean_detections");
            for (JsonElement element : poles) {
                JsonObject detection = element.getAsJsonObject();
                AIResponseModel ai = new AIResponseModel();
                resultSummary.append(detection.get("class_name").getAsString()).append(", ");
                resultList.add(detection.get("class_name").getAsString());
                ai.setAI_RESPONSE(detection.get("class_name").getAsString());
                ai.setLEAN_ANGLE(String.valueOf(detection.get("angle_deg").getAsDouble()));
                ai.setPOSITION("pole_0");
                ai.setPOSTITION_XARM(detection.has("arm_id") ? detection.get("arm_id").getAsString() : "");
                ai.setVOLTAGE("");

                if (detection.has("bbox")) {
                    JsonArray bboxArray = detection.getAsJsonArray("bbox");

                    // Convert to comma-separated string
                    StringBuilder bboxBuilder = new StringBuilder();
                    for (int i = 0; i < bboxArray.size(); i++) {
                        bboxBuilder.append(bboxArray.get(i).getAsString());
                        if (i < bboxArray.size() - 1) bboxBuilder.append(",");
                    }

                    ai.setPOLE_BBOX(bboxBuilder.toString());
                } else {
                    ai.setPOLE_BBOX("");
                }

                aiList.add(ai);
            }
        }


        if (object.has("crops")) {
            JsonArray crops = object.getAsJsonArray("crops");

            for (JsonElement cropElement : crops) {
                JsonObject crop = cropElement.getAsJsonObject();

                if (crop.has("detections") && crop.get("detections").isJsonArray()) {
                    JsonArray detections = crop.getAsJsonArray("detections");

                    for (JsonElement detElement : detections) {
                        JsonObject detection = detElement.getAsJsonObject();
                        AIResponseModel ai = new AIResponseModel();

                        // Extract label class
                        String labelClass = "";
                        if (detection.has("labels")) {
                            JsonArray labels = detection.getAsJsonArray("labels");
                            if (labels.size() > 0) {
                                JsonObject labelObj = labels.get(0).getAsJsonObject();
                                if (labelObj.has("class")) {
                                    labelClass = labelObj.get("class").getAsString();
                                    resultSummary.append(labelClass).append(", ");
                                    resultList.add(labelClass);
                                }
                            }
                        }

                        // Extract voltage
                        String voltage = "";
                        if (detection.has("voltage")) {
                            JsonObject voltageObj = detection.getAsJsonObject("voltage");
                            if (voltageObj.has("class")) {
                                voltage = voltageObj.get("class").getAsString();
                            }
                        }


                        if (detection.has("bbox") && detection.get("bbox").isJsonArray()) {
                            JsonArray bboxArray = detection.getAsJsonArray("bbox");
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < bboxArray.size(); i++) {
                                if (i > 0) builder.append(",");
                                builder.append(bboxArray.get(i).getAsInt());
                            }
                            ai.setCROP_BBOX(builder.toString());
                        } else {
                            ai.setCROP_BBOX("");
                        }

                        ai.setAI_RESPONSE(labelClass);
                        ai.setLEAN_ANGLE("");
                        ai.setPOSITION(crop.has("pole_id") ? crop.get("pole_id").getAsString() : "pole_0");
                        ai.setVOLTAGE(voltage);
                        ai.setPOSTITION_XARM(detection.has("arm_id") ? detection.get("arm_id").getAsString() : "");

                        aiList.add(ai);

                        // Result summary
                       /* if (!labelClass.contains("Straight")) {
                            resultSummary.append(labelClass).append("\n");
                        }*/
                    }
                }
            }
        }


        if (object.has("clearance_result")) {
            JsonObject clearance = object.getAsJsonObject("clearance_result");

            AIResponseModel ai = new AIResponseModel();

            // Required fields
            ai.setAI_RESPONSE(clearance.get("status").getAsString());
            ai.setPOSITION("0");
            ai.setLEAN_ANGLE("");
            ai.setPOSTITION_XARM("");
            ai.setVOLTAGE("11 KV");
            ai.setPOLE_RATIO(clearance.has("pole_height_px") ? String.valueOf(clearance.get("pole_height_px").getAsDouble()) : "-1");
            ai.setPLINTH_RATIO(clearance.has("plinth_height_px") ? String.valueOf(clearance.get("plinth_height_px").getAsDouble()) : "-1");
            aiList.add(ai);
            resultSummary.append(clearance.get("status").getAsString()).append(", ");
            resultList.add(clearance.get("status").getAsString());
            System.out.println("PLINTH_HEIGHT_PX------>" + clearance.get("plinth_height_px").getAsDouble());
        }

        if (object.has("vegetation_clearance")) {
            JsonObject voltageObj = object.getAsJsonObject("vegetation_clearance");
            AIResponseModel ai = new AIResponseModel();
            ai.setAI_RESPONSE(voltageObj.get("reason").getAsString());
            ai.setVD_DEPTH(voltageObj.has("decision_depth") ? voltageObj.get("decision_depth").isJsonNull() ? "" : voltageObj.get("decision_depth").getAsString() : "");
            ai.setVC_REASON(voltageObj.has("reason") ? voltageObj.get("reason").isJsonNull() ? "" : voltageObj.get("reason").getAsString() : "");
            aiList.add(ai);
        }


        postingModel.setAiResponseModel(aiList);
        // transmitPolesLT(postingModel,model);
        aiDialog.dismiss();
        confirmBeforePosting(postingModel, model, resultSummary.toString(), bitmap, resultList);

       /* PmiDatabase pmiDatabase = new PmiDatabase(RectifyMapLT.this);
        pmiDatabase.insertOrUpdatePmiPostingForRectify(postingModel);
        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();*/


    }

    private void confirmBeforePosting(PostingModel selectedPostingModel, PoleModel33KV selectedModel, String resultSummary, Bitmap takenImage, ArrayList<String> resultList) {
        {
            Dialog payDialog = new Dialog(this);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            payDialog.setContentView(R.layout.rectify_confirm_posting_dialog);
            payDialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            payDialog.setCancelable(false);
            TextView cancel_button = payDialog.findViewById(R.id.cancel_button);
            TextView proceed_button = payDialog.findViewById(R.id.proceed_button);
            ImageView taken_image = payDialog.findViewById(R.id.taken_image);

            RelativeLayout ai_pole_layout = payDialog.findViewById(R.id.ai_pole_layout);
            RelativeLayout ai_cross_arm_layout = payDialog.findViewById(R.id.ai_cross_arm_layout);
            RelativeLayout ai_top_cleat_layout = payDialog.findViewById(R.id.ai_top_cleat_layout);
            RelativeLayout ai_others_layout = payDialog.findViewById(R.id.ai_others_layout);
            TextView ai_pole = payDialog.findViewById(R.id.ai_pole);
            TextView ai_cross_arm = payDialog.findViewById(R.id.ai_cross_arm);
            TextView ai_top_cleat = payDialog.findViewById(R.id.ai_top_cleat);
            TextView ai_others = payDialog.findViewById(R.id.ai_others);
            View view_cross_arm = payDialog.findViewById(R.id.view_cross_arm);
            View view_top_cleat = payDialog.findViewById(R.id.view_top_cleat);
            View view_others = payDialog.findViewById(R.id.view_others);

            boolean hasPole = false;
            boolean hasCrossArm = false;
            boolean hasTopCleat = false;

            ArrayList<String> otherItems = new ArrayList<>();

            for (String item : resultList) {

                boolean matched = false;

                // Pole
                if (item.contains("Pole")) {
                    hasPole = true;
                    matched = true;

                    if (item.contains("Straight")) {
                        ai_pole.setText("Straight");
                    } else if (item.contains("Leaned")) {
                        ai_pole.setText("Leaned");
                    } else {
                        ai_pole.setText(item);
                    }
                }

                // Cross Arm
                else if (item.contains("CrossArm")) {
                    hasCrossArm = true;
                    matched = true;

                    if (item.contains("Straight")) {
                        ai_cross_arm.setText("Straight");
                    } else if (item.contains("Tilted")) {
                        ai_cross_arm.setText("Tilted");
                    } else {
                        ai_cross_arm.setText(item);
                    }
                }

                // Top Cleat
                else if (item.contains("TopCleat")) {
                    hasTopCleat = true;
                    matched = true;

                    if (item.contains("Straight")) {
                        ai_top_cleat.setText("Straight");
                    } else if (item.contains("Tilted")) {
                        ai_top_cleat.setText("Tilted");
                    } else {
                        ai_top_cleat.setText(item);
                    }
                }

                // Others
                if (!matched) {
                    otherItems.add(item);
                }
            }


            ai_pole_layout.setVisibility(hasPole ? View.VISIBLE : View.GONE);
            ai_cross_arm_layout.setVisibility(hasCrossArm ? View.VISIBLE : View.GONE);
            ai_top_cleat_layout.setVisibility(hasTopCleat ? View.VISIBLE : View.GONE);


            view_cross_arm.setVisibility(hasCrossArm ? View.VISIBLE : View.GONE);
            view_top_cleat.setVisibility(hasTopCleat ? View.VISIBLE : View.GONE);

            if (!otherItems.isEmpty()) {
                ai_others_layout.setVisibility(View.VISIBLE);
                view_others.setVisibility(View.VISIBLE);
                ai_others.setText(TextUtils.join(", ", otherItems).replace("_", " "));
            } else {
                ai_others_layout.setVisibility(View.GONE);
                view_others.setVisibility(View.GONE);
            }


            taken_image.setImageBitmap(takenImage);


            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payDialog.dismiss();
                }
            });

            proceed_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payDialog.dismiss();
                    transmitPolesLT(selectedPostingModel, selectedModel);
                }
            });


            payDialog.show();
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


    private void transmitPolesLT(PostingModel model, PoleModel33KV originalModel) {

        // progressDialog.show();
        TransmitLoadingBottomSheet.show(this);
        ArcGISDatabase arcGISDatabase = new ArcGISDatabase(this);
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonArray array = new JsonArray();


        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("APP_VERSION", MobileConstants.PMI_VERSION);
        requestObject.addProperty("ID", model.getID());
        // requestObject.addProperty("DEFECT_ID", model.getDEFECT_ID());
        requestObject.addProperty("POLE_ID", model.getPOLE_ID());
        requestObject.addProperty("POLE_NO", model.getPOLE_NO());
        requestObject.addProperty("VOLTAGE_LEVEL", model.getVOLTAGE_LEVEL());
        requestObject.addProperty("DTR_CODE", model.getDTR_CODE());
        requestObject.addProperty("LT_CKT_CODE", model.getLT_CKT_CODE());
        requestObject.addProperty("FEEDER_ID", model.getFEEDER_ID());
        // requestObject.addProperty("DEFECT_TYPE", model.getDEFECT_TYPE());
        // requestObject.addProperty("DEFECT_SUB_TYPE", model.getDEFECT_SUB_TYPE());
        // requestObject.addProperty("DEFECT_SUB_TYPE_ID", model.getDEFECT_SUB_TYPE_ID());
        // requestObject.addProperty("IMAGE", postingList.get(i).getIMAGE());
        // Log.v("PMI DATA IMAGE", postingList.get(i).getIMAGE());
        requestObject.addProperty("IMAGE", ConvertImage.convertBase64(model.getIMAGE()));
        // requestObject.addProperty("IMAGE", "N");
        // requestObject.addProperty("RECT_REMARKS", model.getRECT_REMARKS());
        requestObject.addProperty("USERID", userId);
        requestObject.addProperty("USERNAME", loginDataResponses.get(0).getROLE());
        requestObject.addProperty("SURVEY_MODE", model.getIMAGE_TYPE());
        requestObject.addProperty("DEFECT_TYPE", originalModel.getDEFECT_TYPE());
        requestObject.addProperty("DEFECT_GROUP", originalModel.getDEFECT_GROUP());
        requestObject.addProperty("GRP_ID", originalModel.getGRP_ID());
        requestObject.addProperty("AI_FLAG", originalModel.getAI_FLAG());
        if (subDefectType.isEmpty()){
            requestObject.addProperty("DEFECT_SUB_TYPE", originalModel.getDEFECT_SUB_TYPE());
        }else {
            requestObject.addProperty("DEFECT_SUB_TYPE", subDefectType);
        }

        JsonArray jsonArray = new JsonArray();
        for (int j = 0; j < model.getAiResponseModel().size(); j++) {
            AIResponseModel aiResponseModel = model.getAiResponseModel().get(j);
            JsonObject resObject = new JsonObject();
            resObject.addProperty("AI_RESPONSE", aiResponseModel.getAI_RESPONSE());
            resObject.addProperty("POSITION", aiResponseModel.getPOSITION());
            resObject.addProperty("VOLTAGE", aiResponseModel.getVOLTAGE());
            resObject.addProperty("LEAN_ANGLE", aiResponseModel.getLEAN_ANGLE());
            resObject.addProperty("POSTITION_XARM", aiResponseModel.getPOSTITION_XARM());
            resObject.addProperty("VD_DEPTH", aiResponseModel.getVD_DEPTH());
            resObject.addProperty("VC_REASON", aiResponseModel.getVC_REASON());
            jsonArray.add(resObject);
        }

        requestObject.add("RESPONSE", jsonArray);
        array.add(requestObject);


        Gson gson = new Gson();
        String json = gson.toJson(array);
        Log.v("PMI_DATA===>>", json);


        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.transmitDataNewRectify(array);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // progressDialog.dismiss();
                // aiDialog.dismiss();
                TransmitLoadingBottomSheet.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            originalModel.setIS_RECTIFIED("Y");
                            for (int i = 0; i < Select33PoleRectify.polesList.size(); i++) {
                                if (Select33PoleRectify.polesList.get(i).getPOLE_ID().equals(originalModel.getPOLE_ID()) && Select33PoleRectify.polesList.get(i).getDEFECT_GROUP().equals(originalModel.getDEFECT_GROUP())) {
                                    Select33PoleRectify.polesList.set(i, originalModel);
                                    break;
                                }
                            }

                            for (int i = 0; i < Select33PoleRectify.reportListLT.size(); i++) {
                                if (Select33PoleRectify.reportListLT.get(i).getPOLE_ID().equals(originalModel.getPOLE_ID()) && Select33PoleRectify.reportListLT.get(i).getDEFECT_GROUP().equals(originalModel.getDEFECT_GROUP())) {
                                    Select33PoleRectify.reportListLT.set(i, originalModel);
                                    break;
                                }
                            }


                            Select33PoleRectify.isDataChanged = true;

                            Toast.makeText(RectifyMap33.this, "Data transmitted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(RectifyMap33.this,Select33KVFeederRectify.class);
                            startActivity(intent);
                            finish();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(RectifyMap33.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();


                    }


                } else {

                    Toast.makeText(RectifyMap33.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // progressDialog.dismiss();
                //aiDialog.dismiss();
                TransmitLoadingBottomSheet.dismiss();
                Toast.makeText(RectifyMap33.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


}
