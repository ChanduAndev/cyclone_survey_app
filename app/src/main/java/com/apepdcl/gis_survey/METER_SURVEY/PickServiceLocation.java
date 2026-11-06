package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
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
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import java.util.List;

/**
 * Created by SOUBHAGYA SWAIN on 09-06-2026.
 */
public class PickServiceLocation extends AppCompatActivity {

    TextView name_value;
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

    int SERVICE_POSITION ;

    PolesModel selectedPole;

    String selectedServiceNumber;
    MeterSurveyDatabase database;




    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_service_location);
        getSupportActionBar().hide();

        SERVICE_POSITION = getIntent().getIntExtra("SERVICE_POSITION", -1);
        selectedServiceNumber = getIntent().getStringExtra("SERVICE_NUMBER");
        selectedPole = (PolesModel) getIntent().getSerializableExtra("POLE_DATA");

        database = new MeterSurveyDatabase(this);

        back_layout = findViewById(R.id.back_layout);
        current_location_layout = findViewById(R.id.close_layout);
        mv = findViewById(R.id.map1);
        name_value = findViewById(R.id.name_value);
        name_value.setText(selectedServiceNumber);


        gpsTraker = new GpsTraker(this);
        mGraphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(mGraphicsOverlay);


        map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
        mv.setMap(map);
        mLocationDisplay = mv.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }


            String message = String.format("Error in DataSourceStatusChangedListener: %s",
                    dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
            Toast.makeText(PickServiceLocation.this, message, Toast.LENGTH_LONG).show();

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

        mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (vibrator != null) {
                   // vibrator.vibrate(300);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    }else {
                        vibrator.vibrate(300);
                    }

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


                }
                boolean isPoleFound = false;

                String poleLat = selectedPole.getPole_latitude();
                String poleLon = selectedPole.getPole_longitude();

                double distance = getDistanceBetweenPoles33New(poleLat, poleLon, latitude, longitude);

                if (distance <= 100.0) {
                    isPoleFound = true;
                }

                if (!isPoleFound) {
                    openAlertDialog("Tap exactly (or within 100 meters radius) on the required tapping location");
                } else {
                    //selectPoleNumberBottomSheet(e, mMapView, latitude, longitude, selectedPole);
                    MeterSurveyForm.IS_LOCATION_CHANGED = true;
                    ///REVERSE LAT LONG AS GIS FOLLOWS LONG LAT FORMAT
                    MeterSurveyForm.selectedLat = longitude;
                    MeterSurveyForm.selectedLong = latitude;

                    GpsTraker currentGpsTraker = new GpsTraker(PickServiceLocation.this);
                    MeterSurveyForm.currentLat = currentGpsTraker.getLongitude();
                    MeterSurveyForm.currentLong = currentGpsTraker.getLatitude();
                    MeterSurveyForm.gpsAccuracy = currentGpsTraker.getLocation().getAccuracy();
                    onBackPressed();
                }


                return true;
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
                new AlertDialog.Builder(PickServiceLocation.this)
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
                    Toast.makeText(PickServiceLocation.this, message, Toast.LENGTH_LONG).show();

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
                    loadPoleLocation();

                    //loadAllPolesOnMap(MeterConstants.selectedPolesList);
                    List<MeterPostingModel> serviceList = database.getSinglePoleMeterSurveyData(selectedPole.getPole_id());
                    System.out.println("SOUBHAGYA_POLE_DETILS:::"+selectedPole.getPole_id());
                    if (!serviceList.isEmpty()){
                        loadServiceNumbersOnMap(serviceList);

                    }

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
                                Double.parseDouble(selectedPole.getPole_latitude()),
                                Double.parseDouble(selectedPole.getPole_longitude())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.RED,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(selectedPole.getPole_latitude()),
                                Double.parseDouble(selectedPole.getPole_longitude()),
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
                                selectedPole.getPole_no(),
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
                        System.out.println("SERVICE NUMBERS");
                        System.out.println("LATITUDE: " + serviceList.get(i).getLATITUDE());
                        System.out.println("LONGITUDE: " + serviceList.get(i).getLONGITUDE());

                        polylinePoints.add(new Point(
                                Double.parseDouble(serviceList.get(i).getLATITUDE()),
                                Double.parseDouble(serviceList.get(i).getLONGITUDE())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.BLUE,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(serviceList.get(i).getLATITUDE()),
                                Double.parseDouble(serviceList.get(i).getLONGITUDE()),
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


}
