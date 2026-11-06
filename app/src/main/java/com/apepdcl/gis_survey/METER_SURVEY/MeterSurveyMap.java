package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.bumptech.glide.Glide;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by SOUBHAGYA SWAIN on 08-06-2026.
 */
public class MeterSurveyMap extends AppCompatActivity implements ServicesAdapter.AdapterListener {
    boolean isNoSelected = false;
    ServicesAdapter adapter;
    Dialog meterSurveyDialog;
    private Uri imageUri;
    private Uri imageUri1;
    private ImageView captured_image2;
    ActivityResultLauncher<Uri> cameraLauncher;
    ActivityResultLauncher<Uri> cameraLauncher1;
    private int selectedPosition = -1;
    RelativeLayout reload_map_layout, reload_map_layout_loc;
    public String selectedPoleLat = "", selectedPoleLng = "", selectedPoleId = "", selectedPoleNo = "";

    public static double selectedLat, selectedLong;
    public static double currentLat, currentLong, gpsAccuracy;
    TextView name_value, id_value;
    public static boolean IS_LOCATION_CHANGED, LOCATION_TAP;
    RelativeLayout back_layout, current_location_layout;
    MapView mv;
    GpsTraker gpsTraker;
    LocationDisplay mLocationDisplay;
    GraphicsOverlay mGraphicsOverlay;
    ArcGISMap map;
    PointCollection polylinePoints;
    GraphicsOverlay graphicsOverlay;
    private Point ptWgs84;
    Graphic graphic;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mVoltageProcessingRunnable;
    private static final long PROCESS_DELAY_MS = 100;
    ProgressDialog progressDialog;

    AssignedDtrModel dtrDetails;

    MeterSurveyDatabase database;
    public ArrayList<ServiceData> serviceList;

    PolesModel selectedPole = null;
    MeterSurveyDatabase meterSurveyDatabase;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_survey_map);
        getSupportActionBar().hide();

        dtrDetails = (AssignedDtrModel) getIntent().getSerializableExtra("DTR_DATA");
        database = new MeterSurveyDatabase(MeterSurveyMap.this);
        meterSurveyDatabase = new MeterSurveyDatabase(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);

        back_layout = findViewById(R.id.back_layout);
        reload_map_layout = findViewById(R.id.reload_map_layout);
        reload_map_layout_loc = findViewById(R.id.reload_map_layout_loc);
        current_location_layout = findViewById(R.id.close_layout);
        mv = findViewById(R.id.map1);
        name_value = findViewById(R.id.name_value);
        id_value = findViewById(R.id.id_value);
        name_value.setText(dtrDetails.getDTR_LOCATION());
        id_value.setText(dtrDetails.getDTR_CODE());

        gpsTraker = new GpsTraker(this);
        mGraphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(mGraphicsOverlay);
        serviceList = new ArrayList<>();


        cameraLauncher1 = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {

                        try {
                            CommanUtilits.compressImage(MeterSurveyMap.this, imageUri1);

                            Glide.with(MeterSurveyMap.this)
                                    .load(imageUri1)
                                    .into(captured_image2);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result &&
                            selectedPosition >= 0 &&
                            selectedPosition < serviceList.size()) {

                        try {
                            serviceList.get(selectedPosition).setImageUri(CommanUtilits.compressImage(MeterSurveyMap.this, imageUri).toString());

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }


                        if (adapter != null) {
                            adapter.notifyItemChanged(selectedPosition);
                        }
                    }
                });
        serviceList = new ArrayList<>();
        map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
        mv.setMap(map);
        mLocationDisplay = mv.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }


            String message = String.format("Error in DataSourceStatusChangedListener: %s",
                    dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
            Toast.makeText(MeterSurveyMap.this, message, Toast.LENGTH_LONG).show();

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

        if (!MeterConstants.selectedPolesList.isEmpty()) {
            reload_map_layout_loc.setVisibility(View.VISIBLE);
        } else {
            reload_map_layout_loc.setVisibility(View.GONE);
        }

        reload_map_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(MeterSurveyMap.this)) {
                    gpsTraker.getLocation();

                    // Just update the viewpoint, do not create a new map
                    Point newLocation = new Point(
                            gpsTraker.getLongitude(),
                            gpsTraker.getLatitude(),
                            SpatialReferences.getWgs84()
                    );
                    mv.setViewpointAsync(new Viewpoint(newLocation, 1000)); // 1000 is scale
                } else {
                    CommanUtilits.internetDialog(MeterSurveyMap.this);
                }
            }
        });

        reload_map_layout_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDTRLocation();
                loadAllPolesOnMap(MeterConstants.selectedPolesList);
                List<MeterPostingModel> serviceList = database.getOnlyLatLonData(dtrDetails.getDTR_CODE());
                if (!serviceList.isEmpty()) {
                    loadServiceNumbersOnMap(serviceList);
                }
                if (!MeterConstants.alreadyMappedList.isEmpty()) {
                    loadServiceNumbersOnMap(MeterConstants.alreadyMappedList);
                }
            }
        });


        mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (vibrator != null) {
                    // Vibrate for 300 milliseconds
                    vibrator.vibrate(300);
                }
                double latitude = 0.0;
                double longitude = 0.0;

                // Convert screen touch point to map point
                android.graphics.Point screenPoint = new android.graphics.Point(
                        Math.round(e.getX()),
                        Math.round(e.getY())
                );
                com.esri.arcgisruntime.geometry.Point mapPoint =
                        mv.screenToLocation(screenPoint);
                if (mapPoint != null) {
                    com.esri.arcgisruntime.geometry.Point wgs84Point =
                            (com.esri.arcgisruntime.geometry.Point)
                                    com.esri.arcgisruntime.geometry.GeometryEngine
                                            .project(mapPoint,
                                                    com.esri.arcgisruntime.geometry.SpatialReferences.getWgs84());
                    latitude = wgs84Point.getY();
                    longitude = wgs84Point.getX();

                    if (LOCATION_TAP) {
                        IS_LOCATION_CHANGED = true;
                        ///REVERSE LAT LONG AS GIS FOLLOWS LONG LAT FORMAT
//                        selectedLat = longitude;
//                        selectedLong = latitude;

                        boolean isPoleFound = false;

                        String poleLat = selectedPoleLat;
                        String poleLon = selectedPoleLng;

                        double distance = getDistanceBetweenPoles33New(poleLat, poleLon, latitude, longitude);

                        System.out.println("DISTANCE----->::" + distance);
                        if (distance <= 100.0) {
                            isPoleFound = true;
                        }

                        if (!isPoleFound) {
                            openAlertDialog("Tap exactly (or within 100 meters radius) on the required tapping location");
                        } else {
                            GpsTraker currentGpsTraker = new GpsTraker(MeterSurveyMap.this);
                            currentLat = currentGpsTraker.getLongitude();
                            currentLong = currentGpsTraker.getLatitude();
                            gpsAccuracy = currentGpsTraker.getLocation().getAccuracy();
                            if (IS_LOCATION_CHANGED) {
                                IS_LOCATION_CHANGED = false;
                                serviceList.get(selectedPosition).setLongitude(latitude);
                                serviceList.get(selectedPosition).setLatitude(longitude);
                                serviceList.get(selectedPosition).setCurrent_lat(currentLat);
                                serviceList.get(selectedPosition).setCurrent_lon(currentLong);
                                serviceList.get(selectedPosition).setGps_accuracy(gpsAccuracy);
                                serviceList.get(selectedPosition).setLocationCaptured(true);
                                adapter.notifyItemChanged(selectedPosition);
                            }
                            openMeterSurveyForm(dtrDetails.getREC_ID());
                        }


                    } else {
                        boolean isPoleFound = false;
                        boolean currentLocationCheck = false;

                        double minDistance = Double.MAX_VALUE;
                        System.out.println("DTR_POLES_COUNT-->:::: " + MeterConstants.selectedPolesList.size());
                        if (MeterConstants.selectedPolesList.isEmpty()) {

                            double currentDistance = getCurrentLocationDistance(
                                    gpsTraker.getLatitude(),
                                    gpsTraker.getLongitude(),
                                    latitude,
                                    longitude
                            );

                            if (currentDistance <= 100.0) {

                                // No poles available, but user tapped within 100m of current location
                                openMeterSurveyForm(dtrDetails.getREC_ID());

                            } else {

                                openAlertDialog(
                                        "Tap exactly (or within 100 meters radius) on the required tapping location"
                                );
                            }

                            return true;
                        } else {
                            for (PolesModel pole : MeterConstants.selectedPolesList) {

                                double poleDistance = getDistanceBetweenPoles33New(
                                        pole.getPole_latitude(),
                                        pole.getPole_longitude(),
                                        latitude,
                                        longitude
                                );

                                if (poleDistance < minDistance) {
                                    minDistance = poleDistance;
                                    selectedPole = pole;
                                }
                            }
                        }

                        if (minDistance <= 100.0) {
                            isPoleFound = true;
                        }
                        double currentDistance = getCurrentLocationDistance(
                                gpsTraker.getLatitude(),
                                gpsTraker.getLongitude(),
                                latitude,
                                longitude
                        );
                        if (currentDistance <= 100.0) {
                            currentLocationCheck = true;
                        }

                        if (!isPoleFound) {
                            openAlertDialog(
                                    "Tap exactly (or within 100 meters radius) on the required tapping location"
                            );

                        } else if (!currentLocationCheck) {
                            openAlertDialog(
                                    "Tap exactly (or within 100 meters radius) on the required tapping location"
                            );
                        } else {

                            Log.d("SELECTED_POLE",
                                    "Pole No: " + selectedPole.getPole_no() +
                                            " Distance: " + minDistance);
                            openMeterSurveyForm(dtrDetails.getREC_ID());

                        }
                    }

                }


                return true;
            }

        });

    }


    private double getCurrentLocationDistance(
            double poleLat,
            double poleLon,
            double currentLat,
            double currentLon
    ) {
        try {
            Location currentLocation = new Location("current");
            currentLocation.setLatitude(currentLat);   // ✅ LAT
            currentLocation.setLongitude(currentLon); // ✅ LON

            Location poleLocation = new Location("pole");
            poleLocation.setLatitude(poleLat);   // ✅ LAT
            poleLocation.setLongitude(poleLon); // ✅ LON

            Log.d("currentLat", "" + currentLat);
            Log.d("currentLon", "" + currentLon);
            Log.d("poleLon", "" + poleLon);
            Log.d("poleLat", "" + poleLat);
            Log.d("distance", "" + poleLocation.distanceTo(currentLocation));

            // distance in METERS
            return poleLocation.distanceTo(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE; // safety fallback
        }
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
        LinearLayout estimate_details = view.findViewById(R.id.estimate_details);
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

    private void openAlertDialogExceed(String message) {
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
        LinearLayout estimate_details = view.findViewById(R.id.estimate_details);
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

    private double getDistanceBetweenPoles33New(
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
            poleLocation.setLatitude(Double.parseDouble(poleLon));   // ✅ LAT
            poleLocation.setLongitude(Double.parseDouble(poleLat)); // ✅ LON

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
                new AlertDialog.Builder(MeterSurveyMap.this)
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
                    Toast.makeText(MeterSurveyMap.this, message, Toast.LENGTH_LONG).show();

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

            @Override
            public void run() {
                try {
                    if (LOCATION_TAP) {
                        loadPoleLocation();

                        //loadAllPolesOnMap(MeterConstants.selectedPolesList);
                        List<MeterPostingModel> serviceList = database.getSinglePoleMeterSurveyData(selectedPoleId);
                        System.out.println("SOUBHAGYA_POLE_DETILS:::" + serviceList.size());
                        if (!serviceList.isEmpty()) {
                            loadServiceNumbersOnMap(serviceList);

                        }
                    } else {
                        if (MeterConstants.alreadyMappedList.isEmpty()) {
                            loadDTRLocation();
                            loadAllPolesOnMap(MeterConstants.selectedPolesList);
                            List<MeterPostingModel> serviceList = database.getOnlyLatLonData(dtrDetails.getDTR_CODE());
                            if (!serviceList.isEmpty()) {
                                loadServiceNumbersOnMap(serviceList);
                            }
                        } else {
                            loadServiceNumbersOnMap(MeterConstants.alreadyMappedList);
                        }
                    }


//                    if (!MeterConstants.alreadyMappedList.isEmpty()){
//                        loadServiceNumbersOnMap(MeterConstants.alreadyMappedList);
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadPoleLocation() {

        mv.getGraphicsOverlays().clear();

        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        Runnable uiUpdateRunnable = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                try {
                    int chunkSize = 10;
                    int endIndex = Math.min(currentIndex + chunkSize, 1);

                    for (int i = currentIndex; i < endIndex; i++) {
                        polylinePoints.add(new Point(
                                Double.parseDouble(selectedPoleLat),
                                Double.parseDouble(selectedPoleLng)
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.RED,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(selectedPoleLat),
                                Double.parseDouble(selectedPoleLng),
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
                                selectedPoleNo,
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    currentIndex = endIndex;

                    if (currentIndex < 1) {
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

    private void loadServiceNumbersOnMap(List<MeterPostingModel> serviceList) {

        System.out.println("Service List Size: " + serviceList.size());

        mv.getGraphicsOverlays().clear();

        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        Runnable uiUpdateRunnable = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                try {
                    int chunkSize = 10;
                    int endIndex = Math.min(currentIndex + chunkSize, serviceList.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        //   System.out.println("SERVICE NUMBERS");
                        //   System.out.println("LATITUDE: " + serviceList.get(i).getLATITUDE());
                        //   System.out.println("LONGITUDE: " + serviceList.get(i).getLONGITUDE());

                        polylinePoints.add(new Point(
                                Double.parseDouble(serviceList.get(i).getLONGITUDE()),
                                Double.parseDouble(serviceList.get(i).getLATITUDE())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.BLUE,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(serviceList.get(i).getLONGITUDE()),
                                Double.parseDouble(serviceList.get(i).getLATITUDE()),
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
                                serviceList.get(i).getSERVICE_NUMBER(),
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    currentIndex = endIndex;

                    if (currentIndex < serviceList.size()) {
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


    private void loadDTRLocation() {

        mv.getGraphicsOverlays().clear();

        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        Runnable uiUpdateRunnable = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                try {
                    int chunkSize = 10;
                    int endIndex = Math.min(currentIndex + chunkSize, 1);

                    for (int i = currentIndex; i < endIndex; i++) {
                        polylinePoints.add(new Point(
                                Double.parseDouble(dtrDetails.getDTR_GLONG()),
                                Double.parseDouble(dtrDetails.getDTR_GLAT())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.SQUARE,
                                Color.RED,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(dtrDetails.getDTR_GLONG()),
                                Double.parseDouble(dtrDetails.getDTR_GLAT()),
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
                                "DTR-" + dtrDetails.getDTR_CODE(),
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    currentIndex = endIndex;

                    if (currentIndex < 1) {
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

    private void loadAllPolesOnMap(List<PolesModel> filteredList) {
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
                        //   System.out.println("ALL POLES");
                        //   System.out.println("LATTITUDE: " + filteredList.get(i).getPole_latitude());
                        //   System.out.println("LONGITUDE: " + filteredList.get(i).getPole_longitude());

                        polylinePoints.add(new Point(
                                Double.parseDouble(filteredList.get(i).getPole_latitude()),
                                Double.parseDouble(filteredList.get(i).getPole_longitude())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.RED,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(filteredList.get(i).getPole_latitude()),
                                Double.parseDouble(filteredList.get(i).getPole_longitude()),
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

                        String ltCkt = filteredList.get(i).getLt_ckt_code();
                        char lastChar = ltCkt.charAt(ltCkt.length() - 1);
                        String poleNo = lastChar + "-" + filteredList.get(i).getPole_no();

                        TextSymbol absoluteText = new TextSymbol(
                                15,
                                poleNo,
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


    private void openMeterSurveyForm(String rec_id) {
        meterSurveyDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        meterSurveyDialog.setContentView(R.layout.meter_survey_form);
        meterSurveyDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            meterSurveyDialog.getWindow().setDecorFitsSystemWindows(false);
        }
        meterSurveyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        meterSurveyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        meterSurveyDialog.show();
        LOCATION_TAP = false;

        ImageView back_layout = meterSurveyDialog.findViewById(R.id.back_layout);
        TextView pole_no = meterSurveyDialog.findViewById(R.id.pole_no);
        TextView pole_height = meterSurveyDialog.findViewById(R.id.pole_height);
        TextView pole_type = meterSurveyDialog.findViewById(R.id.pole_type);
        Spinner sp_no_of_services = meterSurveyDialog.findViewById(R.id.sp_no_of_services);
        RecyclerView services_rv = meterSurveyDialog.findViewById(R.id.services_rv);
        TextView submitButton = meterSurveyDialog.findViewById(R.id.submitButton);
        LinearLayout pole_details_layout = meterSurveyDialog.findViewById(R.id.pole_details_layout);
        captured_image2 = meterSurveyDialog.findViewById(R.id.captured_image);
        TextView btn_capture_image_new = meterSurveyDialog.findViewById(R.id.btn_capture_image_new);
        LinearLayout layoutChangeImage = meterSurveyDialog.findViewById(R.id.layoutChangeImage);
        List<PoleCountModel> freshPostingData = meterSurveyDatabase.getPoleCountData();

        if (selectedPole != null) {
            pole_details_layout.setVisibility(View.VISIBLE);
            pole_no.setText("Pole No: " + selectedPole.getPole_no());
            pole_height.setText(selectedPole.getPole_height());
            pole_type.setText(selectedPole.getPole_type());
            boolean showPoleImage = false;
            System.out.println("POLE_IMAGE_AVAILABLE--->" + selectedPole.getPOLE_IMG_AVAILABLE());
            System.out.println("POLE_IMAGE_AVAILABLE_LOOP--->" + freshPostingData.size());

            if ("Y".equalsIgnoreCase(selectedPole.getPOLE_IMG_AVAILABLE())) {
               // layoutChangeImage.setVisibility(View.GONE);
                System.out.println("POLE_IMAGE_AVAILABLE_LOOP_IF--->" + selectedPole.getPOLE_IMG_AVAILABLE());

            } else if ("N".equalsIgnoreCase(selectedPole.getPOLE_IMG_AVAILABLE())) {
                if (!freshPostingData.isEmpty()) {
                    for (PoleCountModel model : freshPostingData) {
                        System.out.println("POLE_IMAGE_AVAILABLE_IN--->" + model.getCOUNT_POLE_NO() + " " + selectedPole.getPole_no());
                        if (model.getCOUNT_POLE_NO().equalsIgnoreCase(selectedPole.getPole_no())
                                && "N".equalsIgnoreCase(model.getIS_IMAGE_TRANSMITTED())) {
                            System.out.println("POLE_IMAGE_AVAILABLE_IN--->" + model.getCOUNT_POLE_NO() + " " + selectedPole.getPole_no() + " "+model.getIS_IMAGE_TRANSMITTED_PC_LOC());
                            showPoleImage = true;
                            break;
                        }
                    }
                } else {
                    showPoleImage = true;
                }


               // layoutChangeImage.setVisibility(showPoleImage ? View.VISIBLE : View.GONE);
            }
        }

        if (!serviceList.isEmpty()) {
            sp_no_of_services.setSelection(serviceList.size());
        }
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceList.clear();
                meterSurveyDialog.dismiss();
            }
        });

        btn_capture_image_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(
                        MediaStore.Images.Media.TITLE,
                        "MeterImage_" + System.currentTimeMillis()
                );

                imageUri1 = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                );
                cameraLauncher1.launch(imageUri1);
            }
        });

        if (imageUri1 != null) {
            Glide.with(this)
                    .load(imageUri1)
                    .into(captured_image2);
        } else {
            captured_image2.setImageResource(R.drawable.outline_add_a_photo_24);
        }

        sp_no_of_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_no_of_services.getSelectedItem().toString().equals("Select")) {
                    int selectedServices = Integer.parseInt(sp_no_of_services.getSelectedItem().toString());

                    if (isNoSelected) {
                        int currentSize = selectedServices - serviceList.size();

                        if (currentSize > 0) {

                            for (int j = 0; j < currentSize; j++) {
                                serviceList.add(new ServiceData());
                            }

                        } else if (currentSize < 0) {

                            int removeCount = Math.abs(currentSize);

                            boolean hasEnteredData = false;

                            // Check rows that are going to be removed
                            for (int k = serviceList.size() - removeCount;
                                 k < serviceList.size();
                                 k++) {

                                ServiceData data = serviceList.get(k);

                                if (data != null &&
                                        (
                                                (data.getServiceNumber() != null &&
                                                        !data.getServiceNumber().trim().isEmpty())
                                                        ||
                                                        (data.getImageUri() != null &&
                                                                !data.getImageUri().trim().isEmpty())
                                                        ||
                                                        data.getLatitude() != 0
                                                        ||
                                                        data.getLongitude() != 0
                                        )) {

                                    hasEnteredData = true;
                                    break;
                                }
                            }

                            if (hasEnteredData) {

                                openAlertDialogExceed(
                                        "Service details have already been entered. The number of services cannot be reduced."
                                );

                                // Reset spinner back to current count
                                sp_no_of_services.post(() -> {
                                    ArrayAdapter adapter =
                                            (ArrayAdapter) sp_no_of_services.getAdapter();

                                    int pos = adapter.getPosition(
                                            String.valueOf(serviceList.size()));

                                    if (pos >= 0) {
                                        sp_no_of_services.setSelection(pos);
                                    }
                                });

                                return;
                            }

                            // Safe to remove
                            for (int k = 0; k < removeCount; k++) {
                                serviceList.remove(serviceList.size() - 1);
                            }
                        }
                    } else {
                        for (int j = 0; j < selectedServices; j++) {
                            serviceList.add(new ServiceData());
                        }
                    }

                    services_rv.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MeterSurveyMap.this);
                    services_rv.setLayoutManager(layoutManager);
                    services_rv.setFocusable(false);
                    adapter = new ServicesAdapter(serviceList,
                            new ServicesAdapter.AdapterListener() {

                                @Override
                                public void onCaptureImage(int position) {

                                    selectedPosition = position;

                                    ContentValues values = new ContentValues();
                                    values.put(
                                            MediaStore.Images.Media.TITLE,
                                            "MeterImage_" + System.currentTimeMillis()
                                    );

                                    imageUri = getContentResolver().insert(
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values
                                    );

                                    try {
                                        cameraLauncher.launch(imageUri);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onPickLocation(int position) {

                                    selectedPosition = position;

                                    IS_LOCATION_CHANGED = false;
                                    LOCATION_TAP = true;
                                    if (selectedPole != null) {
                                        selectedPoleLat = selectedPole.getPole_latitude();
                                        selectedPoleLng = selectedPole.getPole_longitude();
                                        selectedPoleId = selectedPole.getPole_id();
                                        selectedPoleNo = selectedPole.getPole_no();
                                    }


                                    Log.e("SELECTED_POLE",
                                            selectedPoleId + " - " + selectedPoleNo);

                                    meterSurveyDialog.dismiss();
                                }

                                @Override
                                public void onSelectServiceNumber(int position) {

                                    ArrayList<ServiceNumbersModel> serviceNumbers =
                                            new ArrayList<>(MeterConstants.serviceNumbersList);

                                    showServiceSearchBottomSheet(
                                            position,
                                            false,
                                            serviceNumbers
                                    );
                                }
                            },
                            MeterSurveyMap.this);

                    services_rv.setAdapter(adapter);
                    isNoSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateAllServices()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(serviceList);
                    Log.e("SERVICE_DATA", json);
                    // Send to API
                    LOCATION_TAP = false;

                    MeterSurveyDatabase database = new MeterSurveyDatabase(MeterSurveyMap.this);
                    for (int i = 0; i < serviceList.size(); i++) {
                        MeterPostingModel data = new MeterPostingModel();
                        data.setREF_ID(System.currentTimeMillis() + "_" + i);
                        if (selectedPole != null) {
                            data.setPOLE_ID(selectedPole.getPole_id());
                            data.setPOLE_NO(selectedPole.getPole_no());
                            data.setDTR_CODE(selectedPole.getDtr_code());
                            data.setDTR_NAME(selectedPole.getDtr_name());
                            data.setLT_CKT_CODE(selectedPole.getLt_ckt_code());
                        }
                        data.setSERVICE_NUMBER(serviceList.get(i).getServiceNumber());
                        data.setLATITUDE(String.valueOf(serviceList.get(i).getLatitude()));
                        data.setLONGITUDE(String.valueOf(serviceList.get(i).getLongitude()));
                        data.setCURRENT_LAT(String.valueOf(serviceList.get(i).getCurrent_lat()));
                        data.setCURRENT_LON(String.valueOf(serviceList.get(i).getCurrent_lon()));
                        data.setGPS_ACCURACY(String.valueOf(serviceList.get(i).getGps_accuracy()));
                        //data.setIMAGE(serviceList.get(i).getImageUri());
                        data.setIMAGE("");
                        data.setIS_IMAGE_TRANSMITTED("Y");
                        data.setUKSC_NO(serviceList.get(i).getUkscNumber());
                        data.setSC_NAME(serviceList.get(i).getServiceName());
                        data.setREMARKS("");
                        System.out.println("LATTITUDE--LONGITUDE" + String.valueOf(serviceList.get(i).getLatitude()) + " " + String.valueOf(serviceList.get(i).getLongitude()));
                        System.out.println("LATTITUDE--LONGITUDE" + String.valueOf(serviceList.get(i).getCurrent_lat()) + " " + String.valueOf(serviceList.get(i).getCurrent_lon()));

                        if (ConnectivityChecking.checkConnection(MeterSurveyMap.this)) {
                            data.setSURVEY_MODE("ONLINE");
                        } else {
                            data.setSURVEY_MODE("OFFLINE");
                        }
                        database.addMeterSurveyData(data);
                    }

                    PoleCountModel model = new PoleCountModel();
                    if (selectedPole != null) {
                        model.setCOUNT_POLE_ID(selectedPole.getPole_id());
                        model.setCOUNT_POLE_NO(selectedPole.getPole_no());
                        model.setIMAGE(imageUri1 != null ? imageUri1.toString() : null);
                        if (imageUri1 != null) {
                            model.setIS_IMAGE_TRANSMITTED_PC_LOC("Y");
                        } else {
                            model.setIS_IMAGE_TRANSMITTED_PC_LOC("Y");
                        }
                        //model.setIMAGE(String.valueOf(imageUri1));
                        model.setIS_IMAGE_TRANSMITTED("Y");
                    }
                    model.setREC_ID(rec_id);
                    model.setCOUNT_TOTAL("" + serviceList.size());
                    database.insertOrUpdateCountData(model);

                    Toast.makeText(MeterSurveyMap.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                    serviceList.clear();
                    model = null;
                    imageUri1 = null;
                    meterSurveyDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onCaptureImage(int position) {
        selectedPosition = position;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MeterImage_" + System.currentTimeMillis());
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        cameraLauncher.launch(imageUri);
    }


    @Override
    public void onPickLocation(int position) {
        selectedPosition = position;
        IS_LOCATION_CHANGED = false;
        LOCATION_TAP = true;
        meterSurveyDialog.dismiss();

    }

    @Override
    public void onSelectServiceNumber(int position) {
        ArrayList<ServiceNumbersModel> serviceNumbersModelArrayList = new ArrayList<>(MeterConstants.serviceNumbersList);
        showServiceSearchBottomSheet(position, false, serviceNumbersModelArrayList);
    }

    @SuppressLint("MissingInflatedId")
    private void showServiceSearchBottomSheet(int adapterPosition, boolean isAreaFilter, ArrayList<ServiceNumbersModel> serviceNumbersModelArrayList) {

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_service_search, null);
        dialog.setContentView(view);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);


        EditText search_service_number = view.findViewById(R.id.search_service_number);
        EditText search_consumer_name = view.findViewById(R.id.search_consumer_name);
        EditText search_meter_number = view.findViewById(R.id.search_meter_number);
        TextView label_search_by = view.findViewById(R.id.label_search_by);
        TextView or_label_search_by = view.findViewById(R.id.or_label_search_by);
        ImageView back_layout_search = view.findViewById(R.id.back_layout_search);

        RecyclerView recyclerView = view.findViewById(R.id.rvResults);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MeterSurveyMap.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);

        SearchServicesAdapter searchServicesAdapter = new SearchServicesAdapter(serviceNumbersModelArrayList, adapterPosition, dialog);
        recyclerView.setAdapter(searchServicesAdapter);

        if (isAreaFilter) {
            radioGroup.setVisibility(View.GONE);
            label_search_by.setVisibility(View.GONE);
            or_label_search_by.setText("Area Name: " + serviceNumbersModelArrayList.get(0).getAREA_NAME() + "\nArea Code: " + serviceNumbersModelArrayList.get(0).getAREA_CODE());
        }

        back_layout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        search_service_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = search_service_number.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    ArrayList<ServiceNumbersModel> tempList = new ArrayList<>();
                    tempList.clear();
                    for (int j = 0; j < serviceNumbersModelArrayList.size(); j++) {
                        if (serviceNumbersModelArrayList.get(j).getSERVICE_NUMBER().toLowerCase().contains(searchText.toLowerCase())) {
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                } else {
                    searchServicesAdapter.updateList(serviceNumbersModelArrayList);
                }
            }
        });

        search_consumer_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = search_consumer_name.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    ArrayList<ServiceNumbersModel> tempList = new ArrayList<>();
                    tempList.clear();
                    for (int j = 0; j < serviceNumbersModelArrayList.size(); j++) {
                        if (serviceNumbersModelArrayList.get(j).getCONSUMER_NAME().toLowerCase().contains(searchText.toLowerCase())) {
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                } else {
                    searchServicesAdapter.updateList(serviceNumbersModelArrayList);
                }
            }
        });

        search_meter_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = search_meter_number.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    ArrayList<ServiceNumbersModel> tempList = new ArrayList<>();
                    tempList.clear();
                    for (int j = 0; j < serviceNumbersModelArrayList.size(); j++) {
                        if (serviceNumbersModelArrayList.get(j).getMETER_NO().toLowerCase().contains(searchText.toLowerCase())) {
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                } else {
                    searchServicesAdapter.updateList(serviceNumbersModelArrayList);
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String selected = "";
                switch (checkedId) {
                    case R.id.radioName:
                        selected = "Name";
                        break;
                    case R.id.radioMeterNo:
                        selected = "Meter No.";
                        break;
                    case R.id.radioAreaCode:
                        selected = "Area Code";
                        dialog.dismiss();
                        Set<String> uniqueAreaCodes = new HashSet<>();
                        for (ServiceNumbersModel item : serviceNumbersModelArrayList) {
                            uniqueAreaCodes.add(item.getAREA_CODE());
                        }
                        ArrayList<String> arList = new ArrayList<>();
                        arList.addAll(uniqueAreaCodes);

                        SearchableSpinner searchableSpinner = new SearchableSpinner(MeterSurveyMap.this);
                        searchableSpinner.setWindowTitle("Select Area Code");
                        searchableSpinner.setSpinnerListItems(arList);
                        searchableSpinner.show();
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                String selectedAreaCode = selectedString;
                                ArrayList<ServiceNumbersModel> serList = new ArrayList<>();
                                for (int i = 0; i < serviceNumbersModelArrayList.size(); i++) {
                                    if (serviceNumbersModelArrayList.get(i).getAREA_CODE().equals(selectedAreaCode)) {
                                        serList.add(serviceNumbersModelArrayList.get(i));
                                    }
                                }

                                showServiceSearchBottomSheet(adapterPosition, true, serList);
                            }
                        });
                        break;
                    case R.id.radioAreaName:
                        selected = "Area Name";
                        dialog.dismiss();
                        Set<String> uniqueAreaNames = new HashSet<>();
                        for (ServiceNumbersModel item : serviceNumbersModelArrayList) {
                            uniqueAreaNames.add(item.getAREA_NAME());
                        }
                        ArrayList<String> arNameList = new ArrayList<>();
                        arNameList.addAll(uniqueAreaNames);

                        SearchableSpinner searchableSpinnerName = new SearchableSpinner(MeterSurveyMap.this);
                        searchableSpinnerName.setWindowTitle("Select Area Name");
                        searchableSpinnerName.setSpinnerListItems(arNameList);
                        searchableSpinnerName.show();
                        searchableSpinnerName.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                String selectedAreaName = selectedString;
                                ArrayList<ServiceNumbersModel> serListName = new ArrayList<>();
                                for (int i = 0; i < serviceNumbersModelArrayList.size(); i++) {
                                    if (serviceNumbersModelArrayList.get(i).getAREA_NAME().equals(selectedAreaName)) {
                                        serListName.add(serviceNumbersModelArrayList.get(i));
                                    }
                                }

                                showServiceSearchBottomSheet(adapterPosition, true, serListName);
                            }
                        });
                        break;
                }
                //Toast.makeText(MeterSurveyForm.this, "Selected: " + selected, Toast.LENGTH_SHORT).show();
            }
        });


        dialog.setOnShowListener(dialogInterface -> {
            View bottomSheet = dialog.findViewById(
                    R.id.design_bottom_sheet
            );

            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(R.drawable.bg_bottom_sheet_rounded);

                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                behavior.setDraggable(false);
                behavior.setSkipCollapsed(true);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


            }
        });
        dialog.show();
    }

    public class SearchServicesAdapter extends RecyclerView.Adapter<SearchServicesAdapter.MyViewHolder> {

        ArrayList<ServiceNumbersModel> list;
        int adapterPosition;
        BottomSheetDialog dialog;

        public void updateList(ArrayList<ServiceNumbersModel> newList) {
            this.list = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        public SearchServicesAdapter(ArrayList<ServiceNumbersModel> list, int adapterPosition, BottomSheetDialog dialog) {
            this.list = list;
            this.adapterPosition = adapterPosition;
            this.dialog = dialog;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MeterSurveyMap.this).inflate(R.layout.service_search_adapter, parent, false);
            return new SearchServicesAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchServicesAdapter.MyViewHolder holder, int position) {
            holder.search_tv_service_number.setText(list.get(position).getSERVICE_NUMBER());
            holder.search_tv_consumer_name.setText(list.get(position).getCONSUMER_NAME());
            holder.search_tv_meter_number.setText("Meter No: " + list.get(position).getMETER_NO());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ///CURRENT LIST CHECK
                    for (int i = 0; i < serviceList.size(); i++) {
                        if (serviceList.get(i).getServiceNumber().equalsIgnoreCase(list.get(position).getSERVICE_NUMBER())) {
                            Toast.makeText(MeterSurveyMap.this, "Service Number Already added", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    ///DB CHECK
                    MeterSurveyDatabase database = new MeterSurveyDatabase(MeterSurveyMap.this);
                    List<MeterPostingModel> allData = database.getAllMeterSurveyData();
                    for (int i = 0; i < allData.size(); i++) {
                        if (allData.get(i).getSERVICE_NUMBER().equalsIgnoreCase(list.get(position).getSERVICE_NUMBER())) {
                            Toast.makeText(MeterSurveyMap.this, "Service Number Already added", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    serviceList.get(adapterPosition).setServiceNumber(list.get(position).getSERVICE_NUMBER());
                    serviceList.get(adapterPosition).setServiceName(list.get(position).getCONSUMER_NAME());
                    serviceList.get(adapterPosition).setUkscNumber(list.get(position).getUKSC_NUMBER());
                    adapter.notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView search_tv_service_number, search_tv_consumer_name, search_tv_meter_number;
            ImageView search_forward_icon;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                search_tv_service_number = itemView.findViewById(R.id.search_tv_service_number);
                search_tv_consumer_name = itemView.findViewById(R.id.search_tv_consumer_name);
                search_forward_icon = itemView.findViewById(R.id.search_forward_icon);
                search_tv_meter_number = itemView.findViewById(R.id.search_tv_meter_number);
            }
        }
    }


    private boolean validateAllServices() {

        for (int i = 0; i < serviceList.size(); i++) {
            ServiceData item = serviceList.get(i);
            if (item.getServiceNumber().trim().isEmpty()) {
                Toast.makeText(this, "Enter Service Number for item " + (i + 1), Toast.LENGTH_SHORT).show();
                return false;
            }

           /* if (item.getImageUri().trim().isEmpty()) {
                Toast.makeText(this, "Capture image for item " + (i + 1), Toast.LENGTH_SHORT).show();
                return false;
            }*/

            if (!item.isLocationCaptured()) {
                Toast.makeText(this, "Pick location for item " + (i + 1), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
}
