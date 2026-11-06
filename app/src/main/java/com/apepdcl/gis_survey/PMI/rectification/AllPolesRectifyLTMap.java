package com.apepdcl.gis_survey.PMI.rectification;


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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.PmiMap;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.models.AIResponseModel;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;
import com.apepdcl.gis_survey.PMI.models.PoleModelLT;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.PMI.new_screens.ImageRetrofitClient;
import com.apepdcl.gis_survey.PMI.new_screens.ImageRetrofitService;
import com.apepdcl.gis_survey.PMI.new_screens.TFLiteObjectDetector;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
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
 * Created by SOUBHAGYA SWAIN on 18-03-2026.
 */
public class AllPolesRectifyLTMap extends AppCompatActivity {

    String ID, NAME;
    RelativeLayout back_layout, current_location_layout;
    MapView mv;
    GpsTraker gpsTraker, currentLocationGpsTracker;
    LocationDisplay mLocationDisplay;
    GraphicsOverlay mGraphicsOverlay;
    ArcGISMap map;
    TextView name_value, id_value, id_tv, name_tv;
    PointCollection polylinePoints;
    GraphicsOverlay graphicsOverlay;
    private Point ptWgs84;
    Graphic graphic;

    RelativeLayout reload_map_layout;
    ProgressDialog progressDialog;
    TFLiteObjectDetector detector;

    ArrayList<PoleModelLT> filteredPolesListLT;
    ArrayList<PoleModel33KV> filteredPolesList33;
    ArrayList<PoleModel11KV> filteredPolesList11;
    private RetrofitServicePMI retrofitServicePmi;
    ArrayList<PostingModel> postingModelList;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mVoltageProcessingRunnable;
    private static final long PROCESS_DELAY_MS = 100;
    String SELECTED_VOLTAGE;
    String CURRENT_SELECTED_POLE_NUMBER;
    public static Bitmap capturedBitmapPMI1 = null;
    public static boolean isImageCapturedPMI1 = false;
    public static boolean photoClickedPMI1 = false;

    String CURRENT_IMAGE_PATH;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_poles_rectify_map_lt);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);
        postingModelList = new ArrayList<>();
        filteredPolesListLT = new ArrayList<>();
        filteredPolesList33 = new ArrayList<>();
        filteredPolesList11 = new ArrayList<>();

        detector = new TFLiteObjectDetector(this);

        ID = getIntent().getStringExtra("LT_CKT_CODE");
        NAME = getIntent().getStringExtra("DTR_CODE");
        SELECTED_VOLTAGE = getIntent().getStringExtra("SELECTED_VOLTAGE");

        back_layout = findViewById(R.id.back_layout);
        reload_map_layout = findViewById(R.id.reload_map_layout);
        current_location_layout = findViewById(R.id.close_layout);
        mv = findViewById(R.id.map1);

        name_value = findViewById(R.id.name_value);
        id_value = findViewById(R.id.id_value);
        name_tv = findViewById(R.id.name_tv);
        id_tv = findViewById(R.id.id_tv);
        name_value.setText(NAME);
        id_value.setText(ID);
        if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
            name_tv.setText("DTR CODE:");
            id_tv.setText("LT CKT CODE:");
        }

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
            Toast.makeText(AllPolesRectifyLTMap.this, message, Toast.LENGTH_LONG).show();

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
                if (ConnectivityChecking.checkConnection(AllPolesRectifyLTMap.this)) {
                    gpsTraker.getLocation();

                    // Just update the viewpoint, do not create a new map
                    Point newLocation = new Point(
                            gpsTraker.getLongitude(),
                            gpsTraker.getLatitude(),
                            SpatialReferences.getWgs84()
                    );
                    mv.setViewpointAsync(new Viewpoint(newLocation, 1000)); // 1000 is scale
                } else {
                    CommanUtilits.internetDialog(AllPolesRectifyLTMap.this);
                }
            }
        });

        mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
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
                if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                    for (int i = 0; i < filteredPolesList33.size(); i++) {

                        String poleLat = filteredPolesList33.get(i).getGLAT();
                        String poleLon = filteredPolesList33.get(i).getGLONG();

                        double distance = getDistanceBetweenPolesLT(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );

                        if (distance <= 20.0) {
                            isPoleFound = true;
                            selectPoleNumber(e, mMapView, latitude, longitude);
                            break;
                        }
                    }
                }
                if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {

                    for (int i = 0; i < filteredPolesList11.size(); i++) {

                        String poleLat = filteredPolesList11.get(i).getGLAT();
                        String poleLon = filteredPolesList11.get(i).getGLONG();

                        double distance = getDistanceBetweenPolesLT(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );


                        if (distance <= 20.0) {
                            isPoleFound = true;
                            selectPoleNumber(e, mMapView, latitude, longitude);
                            break;
                        }
                    }

                }
                if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {

                    for (int i = 0; i < filteredPolesListLT.size(); i++) {

                        String poleLat = filteredPolesListLT.get(i).getGLAT();
                        String poleLon = filteredPolesListLT.get(i).getGLONG();

                        double distance = getDistanceBetweenPolesLT(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );

                        if (distance <= 20.0) {
                            isPoleFound = true;
                            selectPoleNumber(e, mMapView, latitude, longitude);
                            break;
                        }
                    }
                }
                if (!isPoleFound) {
                    openAlertDialog("Tap exactly (or within 20 meters radius) on the required tapping location");
                }



                return true;
            }

        });


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
        try {
            if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                process33KVDataWithHandler();
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
                process11KVDataWithHandler();
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
                processLTDataWithHandler();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void process33KVDataWithHandler() {
        // Similar pattern for LT
        mHandler.post(new Runnable() {
            private int currentIndex = 0;
            private List<PoleModel33KV> tempFilteredList = new ArrayList<>();
            private GpsTraker tempGpsTracker = new GpsTraker(AllPolesRectifyLTMap.this);
            private double currentLat = tempGpsTracker.getLatitude();
            private double currentLon = tempGpsTracker.getLongitude();

            @Override
            public void run() {
                try {
                    int chunkSize = 50;
                    int endIndex = Math.min(currentIndex + chunkSize, Select33KVFeederRectify.polesList33.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        double distance = getDistanceBetweenPolesNormal(
                                Select33KVFeederRectify.polesList33.get(i).getGLONG(),
                                Select33KVFeederRectify.polesList33.get(i).getGLAT(),
                                currentLat, currentLon
                        );
                        if (distance <= 20000000) {
                            tempFilteredList.add(Select33KVFeederRectify.polesList33.get(i));
                        }
                    }

                    currentIndex = endIndex;

                    if (currentIndex < Select33KVFeederRectify.polesList33.size()) {
                        mHandler.post(this);
                    } else {
                        updateUIWithFiltered33Data(tempFilteredList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void process11KVDataWithHandler() {
        // Similar pattern for LT
        mHandler.post(new Runnable() {
            private int currentIndex = 0;
            private List<PoleModel11KV> tempFilteredList = new ArrayList<>();
            private GpsTraker tempGpsTracker = new GpsTraker(AllPolesRectifyLTMap.this);
            private double currentLat = tempGpsTracker.getLatitude();
            private double currentLon = tempGpsTracker.getLongitude();

            @Override
            public void run() {
                try {
                    int chunkSize = 50;
                    int endIndex = Math.min(currentIndex + chunkSize, Select11KVFeederRectify.polesList11.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        double distance = getDistanceBetweenPolesNormal(
                                Select11KVFeederRectify.polesList11.get(i).getGLONG(),
                                Select11KVFeederRectify.polesList11.get(i).getGLAT(),
                                currentLat, currentLon
                        );
                        if (distance <= 20000000) {
                            tempFilteredList.add(Select11KVFeederRectify.polesList11.get(i));
                        }
                    }

                    currentIndex = endIndex;

                    if (currentIndex < Select11KVFeederRectify.polesList11.size()) {
                        mHandler.post(this);
                    } else {
                        updateUIWithFiltered11Data(tempFilteredList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processLTDataWithHandler() {
        // Similar pattern for LT
        mHandler.post(new Runnable() {
            private int currentIndex = 0;
            private List<PoleModelLT> tempFilteredList = new ArrayList<>();
            private GpsTraker tempGpsTracker = new GpsTraker(AllPolesRectifyLTMap.this);
            private double currentLat = tempGpsTracker.getLatitude();
            private double currentLon = tempGpsTracker.getLongitude();

            @Override
            public void run() {
                try {
                    int chunkSize = 50;
                    int endIndex = Math.min(currentIndex + chunkSize, SelectLTCircuitRectify.polesListLT.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        double distance = getDistanceBetweenPolesNormal(
                                SelectLTCircuitRectify.polesListLT.get(i).getGLONG(),
                                SelectLTCircuitRectify.polesListLT.get(i).getGLAT(),
                                currentLat, currentLon
                        );
                        if (distance <= 20000000) {
                            tempFilteredList.add(SelectLTCircuitRectify.polesListLT.get(i));
                        }
                    }

                    currentIndex = endIndex;

                    if (currentIndex < SelectLTCircuitRectify.polesListLT.size()) {
                        mHandler.post(this);
                    } else {
                        updateUIWithFilteredLTData(tempFilteredList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUIWithFiltered33Data(List<PoleModel33KV> filteredList) {
        filteredPolesList33.clear();
        filteredPolesList33.addAll(filteredList);
        mv.getGraphicsOverlays().clear();

        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        Runnable uiUpdateRunnable = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                try {
                    int chunkSize = 10;
                    int endIndex = Math.min(currentIndex + chunkSize, filteredPolesList33.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        polylinePoints.add(new Point(
                                Double.parseDouble(filteredPolesList33.get(i).getGLAT()),
                                Double.parseDouble(filteredPolesList33.get(i).getPOLE_NO())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.RED,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(filteredPolesList33.get(i).getGLAT()),
                                Double.parseDouble(filteredPolesList33.get(i).getGLONG()),
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
                                filteredPolesList33.get(i).getPOLE_NO(),
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    currentIndex = endIndex;

                    if (currentIndex < filteredPolesList33.size()) {
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
    private void updateUIWithFiltered11Data(List<PoleModel11KV> filteredList) {
        filteredPolesList11.clear();
        filteredPolesList11.addAll(filteredList);
        mv.getGraphicsOverlays().clear();

        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        Runnable uiUpdateRunnable = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                try {
                    int chunkSize = 10;
                    int endIndex = Math.min(currentIndex + chunkSize, filteredPolesList11.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        polylinePoints.add(new Point(
                                Double.parseDouble(filteredPolesList11.get(i).getGLAT()),
                                Double.parseDouble(filteredPolesList11.get(i).getPOLE_NO())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.RED,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(filteredPolesList11.get(i).getGLAT()),
                                Double.parseDouble(filteredPolesList11.get(i).getGLONG()),
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
                                filteredPolesList11.get(i).getPOLE_NO(),
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    currentIndex = endIndex;

                    if (currentIndex < filteredPolesList11.size()) {
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
    private void updateUIWithFilteredLTData(List<PoleModelLT> filteredList) {
        filteredPolesListLT.clear();
        filteredPolesListLT.addAll(filteredList);
        mv.getGraphicsOverlays().clear();

        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        Runnable uiUpdateRunnable = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                try {
                    int chunkSize = 10;
                    int endIndex = Math.min(currentIndex + chunkSize, filteredPolesListLT.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        polylinePoints.add(new Point(
                                Double.parseDouble(filteredPolesListLT.get(i).getGLAT()),
                                Double.parseDouble(filteredPolesListLT.get(i).getPOLE_NO())
                        ));

                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                                SimpleMarkerSymbol.Style.CIRCLE,
                                Color.RED,
                                12
                        );

                        ptWgs84 = new Point(
                                Double.parseDouble(filteredPolesListLT.get(i).getGLAT()),
                                Double.parseDouble(filteredPolesListLT.get(i).getGLONG()),
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
                                filteredPolesListLT.get(i).getPOLE_NO(),
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }

                    currentIndex = endIndex;

                    if (currentIndex < filteredPolesListLT.size()) {
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
                new AlertDialog.Builder(AllPolesRectifyLTMap.this)
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
                    Toast.makeText(AllPolesRectifyLTMap.this, message, Toast.LENGTH_LONG).show();

                });
                // mLocationDisplay.stop();
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            poleLocation.setLatitude(Double.parseDouble(poleLon));   // ✅ LAT
            poleLocation.setLongitude(Double.parseDouble(poleLat)); // ✅ LON

          //  System.out.println("currentLat :"+currentLat);
          //  System.out.println("currentLon :"+currentLon);
          //  System.out.println("poleLat :"+poleLon);
          //  System.out.println("poleLon :"+poleLat);
           // System.out.println("DISTANCE : " + poleLocation.distanceTo(currentLocation));

            return poleLocation.distanceTo(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE; // safety fallback
        }
    }

    private void selectPoleNumber(MotionEvent e, MapView mMapView,
                                  double lat, double lon) {

        currentLocationGpsTracker = new GpsTraker(this);

        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        payDialog.setContentView(R.layout.pole_dialog);
        payDialog.getWindow().setLayout(width, (int) (height * 0.6));
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Spinner sp_pole_number = payDialog.findViewById(R.id.sp_pole_number);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);

        List<String> poleList = new ArrayList<>();
        poleList.add("Select");

        double MAX_DISTANCE_METERS = 20; // ✅ 100 meters filter

        if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {

            for (int i = 0; i < filteredPolesList33.size(); i++) {

                String poleLat = filteredPolesList33.get(i).getGLAT();
                String poleLon = filteredPolesList33.get(i).getGLONG();

                double distance = getDistanceBetweenPolesLT(
                        poleLat,
                        poleLon,
                        lat,
                        lon
                );

                if (distance <= MAX_DISTANCE_METERS) {
                    poleList.add(filteredPolesList33.get(i).getPOLE_NO());
                }
            }
        }

        if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {

            for (int i = 0; i < filteredPolesList11.size(); i++) {

                String poleLat = filteredPolesList11.get(i).getGLAT();
                String poleLon = filteredPolesList11.get(i).getGLONG();

                double distance = getDistanceBetweenPolesLT(
                        poleLat,
                        poleLon,
                        lat,
                        lon
                );


                if (distance <= MAX_DISTANCE_METERS) {
                    poleList.add(filteredPolesList11.get(i).getPOLE_NO());
                }
            }
        }

        if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {

            for (int i = 0; i < filteredPolesListLT.size(); i++) {

                String poleLat = filteredPolesListLT.get(i).getGLAT();
                String poleLon = filteredPolesListLT.get(i).getGLONG();

                double distance = getDistanceBetweenPolesLT(
                        poleLat,
                        poleLon,
                        lat,
                        lon
                );

                if (distance <= MAX_DISTANCE_METERS) {
                    poleList.add(filteredPolesListLT.get(i).getPOLE_NO());
                }
            }
        }

        // Adapter
        ArrayAdapter<String> adapter_pst =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        poleList);

        sp_pole_number.setAdapter(adapter_pst);
        sp_pole_number.setSelection(0, true);

        payDialog.show();

        back_to_home.setOnClickListener(view -> {

            if (sp_pole_number.getSelectedItem().toString()
                    .equalsIgnoreCase("Select")) {

                Toast.makeText(this,
                        "Please Select Pole Number",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            payDialog.dismiss();

            CURRENT_SELECTED_POLE_NUMBER = sp_pole_number.getSelectedItem().toString();

            Toast.makeText(this, "CURRENT_SELECTED_POLE_NUMBER "+CURRENT_SELECTED_POLE_NUMBER, Toast.LENGTH_SHORT).show();

           // openDefectDialog(CURRENT_SELECTED_POLE_NUMBER);

            openCamera();
        });
    }

    private void openCamera() {
        photoClickedPMI1 = true;
        isImageCapturedPMI1 = false;
        capturedBitmapPMI1 = null;
        Intent intentOpenCamera = new Intent(AllPolesRectifyLTMap.this, MainActivity.class);
        startActivity(intentOpenCamera);
    }

    private void openAlertDialog(String msg) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Pay Attention")
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("Okay", (dialog, which) -> {
                    dialog.cancel();
                })

                .show();


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
        if (ConnectivityChecking.checkConnection(AllPolesRectifyLTMap.this)){
            uploadImageToServer(safeBitmap);
        }else {
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
                Toast.makeText(this, "Invalid image path", Toast.LENGTH_SHORT).show();
                return;
            }

            if (progressDialog != null) progressDialog.show();

            File file = new File(CURRENT_IMAGE_PATH);
            if (!file.exists()) {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
                if (progressDialog != null) progressDialog.dismiss();
                return;
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            ImageRetrofitService service = ImageRetrofitClient.getRetrofitInstance().create(ImageRetrofitService.class);



            Call<JsonObject> call = service.uploadImage(imagePart, false);



            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    // if (progressDialog != null) progressDialog.dismiss();

                    if (!response.isSuccessful() || response.body() == null) {
                        if (progressDialog != null) progressDialog.dismiss();
                        Toast.makeText(AllPolesRectifyLTMap.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JsonObject object = response.body();
                    if (object.has("status") && object.get("status").getAsString().equalsIgnoreCase("done")) {
                        handleSuccessfulUpload(object, bitmap);
                    } else {
                        if (progressDialog != null) progressDialog.dismiss();
                        // String reason = object.has("reason") ? object.get("reason").getAsString() : "Unknown error";
                        String reason = "Unknown error";

                        if (object.has("reason") && !object.get("reason").isJsonNull()) {
                            reason = object.get("reason").getAsString();
                        }

                        Toast.makeText(AllPolesRectifyLTMap.this, reason, Toast.LENGTH_SHORT).show();
                        showDetectionDialog(reason, bitmap);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (progressDialog != null) progressDialog.dismiss();
                    Toast.makeText(AllPolesRectifyLTMap.this, "Upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (progressDialog != null) progressDialog.dismiss();
            Toast.makeText(this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSuccessfulUpload(JsonObject object, Bitmap bitmap) {

        String selectedPoleID = "";
        String selectedFeederID = "";
        String selectedDTRCode = "";
        String selectedLtCktCode = "";



        if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
            for (int i = 0; i < filteredPolesList33.size(); i++) {
                if (CURRENT_SELECTED_POLE_NUMBER.equalsIgnoreCase(filteredPolesList33.get(i).getPOLE_NO())) {

                    selectedPoleID = filteredPolesList33.get(i).getPOLE_ID();
                    selectedFeederID = filteredPolesList33.get(i).getFEEDER_ID();

                    break;
                }
            }
        } else if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
            for (PoleModel11KV pole : filteredPolesList11) {
                if (CURRENT_SELECTED_POLE_NUMBER.equalsIgnoreCase(pole.getPOLE_NO())) {
                    selectedPoleID = pole.getPOLE_ID();
                    selectedFeederID = pole.getFEEDER_ID();

                    break;
                }
            }
        } else if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
            for (PoleModelLT pole : filteredPolesListLT) {
                if (CURRENT_SELECTED_POLE_NUMBER.equalsIgnoreCase(pole.getPOLE_NO())) {

                    selectedPoleID = pole.getPOLE_ID();
                    selectedDTRCode = pole.getDTR_CODE();
                    selectedLtCktCode = pole.getLT_CKT_CODE();

                    break;
                }
            }
        }

        // Build new PostingModel
        PostingModel postingModel = new PostingModel();
        postingModel.setID(Long.toString(createRandomUID()));

        postingModel.setVOLTAGE_LEVEL(SELECTED_VOLTAGE);
        postingModel.setFROM("RECTIFY");
        postingModel.setPOLE_NO(CURRENT_SELECTED_POLE_NUMBER);
        postingModel.setPOLE_ID(selectedPoleID);
        postingModel.setFEEDER_ID(selectedFeederID);
        postingModel.setDTR_CODE(selectedDTRCode);
        postingModel.setLT_CKT_CODE(selectedLtCktCode);
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
                        if (!labelClass.contains("Straight")) {
                            resultSummary.append(labelClass).append("\n");
                        }
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
            resultSummary.append(clearance.get("status").getAsString()).append("\n");
            System.out.println("PLINTH_HEIGHT_PX------>" + clearance.get("plinth_height_px").getAsDouble());
        }

        if (object.has("vegetation_clearance")) {
            JsonObject voltageObj = object.getAsJsonObject("vegetation_clearance");
            AIResponseModel ai = new AIResponseModel();
            ai.setAI_RESPONSE(object.get("vegetation_clearance").getAsString());
            ai.setVD_DEPTH(voltageObj.has("decision_depth")? voltageObj.get("decision_depth").isJsonNull() ? "" : voltageObj.get("decision_depth").getAsString() : "");
            ai.setVC_REASON(voltageObj.has("reason")? voltageObj.get("reason").isJsonNull() ? "" : voltageObj.get("reason").getAsString() : "");
            aiList.add(ai);
        }


        postingModel.setAiResponseModel(aiList);
        transmitPoles(postingModel);

       /* PmiDatabase pmiDatabase = new PmiDatabase(RectifyMapLT.this);
        pmiDatabase.insertOrUpdatePmiPostingForRectify(postingModel);
        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();*/


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


    private void transmitPoles(PostingModel model) {

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
        //  requestObject.addProperty("DEFECT_SUB_TYPE_ID", model.getDEFECT_SUB_TYPE_ID());
        //  requestObject.addProperty("IMAGE", postingList.get(i).getIMAGE());
        // Log.v("PMI DATA IMAGE", postingList.get(i).getIMAGE());
        requestObject.addProperty("IMAGE", ConvertImage.convertBase64(model.getIMAGE()));
        // requestObject.addProperty("IMAGE", "N");
        // requestObject.addProperty("RECT_REMARKS", model.getRECT_REMARKS());
        requestObject.addProperty("USERID", userId);
        requestObject.addProperty("USERNAME", loginDataResponses.get(0).getROLE());
        requestObject.addProperty("SURVEY_MODE", model.getIMAGE_TYPE());


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
        Log.v("PMI_DATA===>>", "" + json);


        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.transmitDataNewRectify(array);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            Toast.makeText(AllPolesRectifyLTMap.this, "Data transmitted successfully", Toast.LENGTH_SHORT).show();
                           // onBackPressed();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(AllPolesRectifyLTMap.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();


                    }


                } else {

                    Toast.makeText(AllPolesRectifyLTMap.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AllPolesRectifyLTMap.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

}
