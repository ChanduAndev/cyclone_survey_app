package com.apepdcl.gis_survey.PMI;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.METER_SURVEY.ApiClient;
import com.apepdcl.gis_survey.METER_SURVEY.ImageAPIServiceMeter;
import com.apepdcl.gis_survey.PMI.models.AIResponseModel;
import com.apepdcl.gis_survey.PMI.models.DefectGroupModel;
import com.apepdcl.gis_survey.PMI.models.DefectsModel;

import com.apepdcl.gis_survey.PMI.models.ImageModel;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;
import com.apepdcl.gis_survey.PMI.models.PoleModelLT;
import com.apepdcl.gis_survey.PMI.models.PostingModel;


import com.apepdcl.gis_survey.PMI.new_screens.ImageRetrofitClient;
import com.apepdcl.gis_survey.PMI.new_screens.ImageRetrofitService;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.PMI.new_screens.Select11KVFeeder;
import com.apepdcl.gis_survey.PMI.new_screens.Select33KVFeeder;
import com.apepdcl.gis_survey.PMI.new_screens.SelectLTCircuit;
import com.apepdcl.gis_survey.PMI.new_screens.TFLiteObjectDetector;
import com.apepdcl.gis_survey.PMI.new_screens.YoloTFLiteDetector;
import com.apepdcl.gis_survey.PMI.rectification.Select11KVFeederRectify;
import com.apepdcl.gis_survey.PMI.rectification.Select33KVFeederRectify;
import com.apepdcl.gis_survey.PMI.rectification.SelectLTCircuitRectify;
import com.apepdcl.gis_survey.PMI.rectification.TransmitLoadingBottomSheet;
import com.apepdcl.gis_survey.estimates.sou.SpinnerAdapter;
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

import net.sourceforge.app.MainActivity;

import com.apepdcl.gis.app.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by SOUBHAGYA SWAIN on 20-03-2025.
 */
public class PmiMap extends AppCompatActivity implements OnImageActionListener {

    String ID, NAME;
    private BottomSheetDialog transmitDialog;
    private String aiImageShortUrl = "", aiDTRShortUrl = "";

    String shortURl = "";
    ImageView back_layout, current_location_layout;
    MapView mv;
    GpsTraker gpsTraker, currentLocationGpsTracker;
    LocationDisplay mLocationDisplay;
    GraphicsOverlay mGraphicsOverlay;
    ArcGISMap map;

    Uri imageUri;

    TextView name_tv, name_value;
    TextView id_tv, id_value;
    PointCollection polylinePoints;
    GraphicsOverlay graphicsOverlay;
    private Point ptWgs84;
    Graphic graphic;
    public static float gpsAccurancy = 0;

    ArrayList<PoleModel33KV> filteredPolesList33;
    ArrayList<PoleModel11KV> filteredPolesList11;
    ArrayList<PoleModelLT> filteredPolesListLT;

    RelativeLayout reload_map_layout, reload_locations_layout;
    PmiDatabase pmiDatabase;
    int OFFLINE_DISTANCE = 0, ONLINE_DISTANCE = 0;


    //DEFECT MODULE

    private TextView submitButton;

    String selectedSubEffect;


    //NEW WITH LIST VIEW
    ListView list_defects;

    LinearLayout typeLayout;
    int warning = 0;
    int warning_1 = 0;
    private TextView tv_question;


    private HashMap<CheckBox, ArrayList<CheckBox>> typeToSubTypeMap = new HashMap<>();
    private HashMap<CheckBox, ImageView> subTypeToImageMap = new HashMap<>();
    private HashMap<Integer, ImageView> requestCodeToImageViewMap = new HashMap<>();
    private HashMap<CheckBox, String> subTypeToCycloneMap = new HashMap<>();
    private int imageRequestCode = 1000;
    String CURRENT_IMAGE_PATH;

    ArrayList<ImageModel> imageList;


    //NEW IMAGE OPEN CAMERA
    public static Bitmap capturedBitmapPMI1 = null;
    public static boolean isImageCapturedPMI1 = false;
    public static boolean photoClickedPMI1 = false;

    public static Bitmap capturedBitmapPMI2 = null;
    public static boolean isImageCapturedPMI2 = false;
    public static boolean photoClickedPMI2 = false;


    public int currentRequestCode;
    public String current_selected_sub_id;
    private static final String IMAGE_DIRECTORY = "/PMI_Images";
    private static final String IMAGE_PREFIX = "PMI_IMG_";
    private static final String IMAGE_EXTENSION = ".jpg";

    String SELECTED_VOLTAGE, FROM;

    TFLiteObjectDetector detector;
    private HashMap<Integer, TextView> requestCodeToTextViewMap = new HashMap<>();
    private HashMap<Integer, RadioGroup> requestCodeToRadioGroupMap = new HashMap<>();
    ProgressDialog progressDialog;

    ArrayList<PostingModel> postingModelList;
    String CURRENT_SELECTED_POLE_NUMBER;
    String CURRENT_SELECTED_POLE_ID;

    private RetrofitServicePMI retrofitServicePmi;

    YoloTFLiteDetector plinthDetector;

    // Add these class variables
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mVoltageProcessingRunnable;
    private static final long PROCESS_DELAY_MS = 100; // Small delay to batch operations

    Dialog aiDialog;

    //MAIN IMAGE
    public static Bitmap mainImageBitmapPMI = null;
    public static boolean isMainImagePMICaptured = false;
    public static boolean mainPhotoClickedPMI = false;

    //New Pmi changes
    ArrayList<AIResponseModel> aiListMain, aiListMainDTR;

    LinearLayout capture_Image_layout, taken_image_layout, all_defects_layout, layout_question;
    TextView tv_yes, tv_no;
    private boolean isDefective = false;
    LinearLayout dtr_capture_Image_layout, dtr_taken_image_layout;
    ImageView imgPreview, dtrimgPreview;
    String MAIN_IMAGE_PATH = "", DTR_MAIN_IMAGE_PATH = "";
    RecyclerView rvDefectGroups;

    private DefectsModel selectedDefect;
    private String currentPhotoPath = "";
    // DefectTypeAdapter defectTypeAdapter;

    DefectGroupAdapter defectGroupAdapter;

    DefectTypeAdapter defectTypeAdapter;

    ArrayList<DefectsModel> selectedChipDefects;

    Dialog newDefectDialog;

    View view_others;
    View view_top_cleat;
    View view_cross_arm;
    TextView ai_others;
    TextView ai_top_cleat;
    TextView ai_cross_arm;
    TextView ai_pole;
    RelativeLayout ai_others_layout;
    RelativeLayout ai_top_cleat_layout;
    RelativeLayout ai_cross_arm_layout;
    RelativeLayout ai_pole_layout;

    View dtrview_others;
    View dtrview_top_cleat;
    View dtrview_cross_arm;
    TextView dtrai_others;
    TextView dtrai_top_cleat;
    TextView dtrai_cross_arm;
    TextView dtrai_pole;
    RelativeLayout dtrai_others_layout;
    RelativeLayout dtrai_top_cleat_layout;
    RelativeLayout dtrai_cross_arm_layout;
    RelativeLayout dtrai_pole_layout;

    public static Bitmap mainImageBitmapPMIDTR = null;
    public static boolean isMainImagePMICapturedDTR = false;
    public static boolean mainPhotoClickedPMIDTR = false;

    String DTR_POLE_HEIGHT = "";
    String SELECTED_GROUP_NAME = "";
    String SELECTED_POLE_CONFIG = "";

    String SELECTED_POLE_STRUCTURE = "";

    boolean IS_DTR_POLE = false;
    boolean IS_DTR_AI_IMAGE_VERIFIED = false;
    private TextView home_heading, feeder_name;

    private int totalImagesToUpload = 0;
    private int completedImages = 0;
    private boolean transmissionStarted = false;
    private final Object imageUploadLock = new Object();
    private final Map<String, String> uploadedImageMap = new HashMap<>();

    double CYCLONE_POLE_LONG, CYCLONE_POLE_LAT;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmi_map);
        getSupportActionBar().hide();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);
        postingModelList = new ArrayList<>();

        detector = new TFLiteObjectDetector(this);
        try {
            plinthDetector = new YoloTFLiteDetector(
                    getAssets(),
                    "dtrplinth.tflite"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ID = getIntent().getStringExtra("ID");
        NAME = getIntent().getStringExtra("NAME");
        SELECTED_VOLTAGE = getIntent().getStringExtra("SELECTED_VOLTAGE");
        FROM = getIntent().getStringExtra("FROM");


        filteredPolesList33 = new ArrayList<>();
        filteredPolesList11 = new ArrayList<>();
        filteredPolesListLT = new ArrayList<>();
        //New Pmi changes
        selectedChipDefects = new ArrayList<>();

        imageList = new ArrayList<>();


        back_layout = findViewById(R.id.back_layout);
        reload_map_layout = findViewById(R.id.reload_map_layout);
        reload_locations_layout = findViewById(R.id.reload_locations_layout);
        current_location_layout = findViewById(R.id.current_location_layout);
        mv = findViewById(R.id.map1);
        name_tv = findViewById(R.id.name_tv);
        name_value = findViewById(R.id.name_value);
        id_tv = findViewById(R.id.id_tv);
        id_value = findViewById(R.id.id_value);
        home_heading = findViewById(R.id.home_heading);
        feeder_name = findViewById(R.id.feeder_name);
        name_value.setText(NAME);
        id_value.setText(ID);
        if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
            name_tv.setText("DTR CODE:");
            id_tv.setText("LT CKT CODE:");
        }


        if ("SURVEY".equalsIgnoreCase(FROM)) {
            if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                home_heading.setText("33kV Cyclone Damage Inspection");
                feeder_name.setText(NAME + " (" + ID + ")");
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
                home_heading.setText("11kV Cyclone Damage Inspection");
                feeder_name.setText(NAME + " (" + ID + ")");
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
                home_heading.setText("LT Cyclone Damage Inspection");
                feeder_name.setText(NAME + " (" + ID + ")");
            }
        } else {
            if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                home_heading.setText("33kV Cyclone Rectification");
                feeder_name.setText(NAME + " (" + ID + ")");
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
                home_heading.setText("11kV 33kV Cyclone Rectification");
                feeder_name.setText(NAME + " (" + ID + ")");
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
                home_heading.setText("LT 33kV Cyclone Rectification");
                feeder_name.setText(NAME + " (" + ID + ")");
            }
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
            Toast.makeText(PmiMap.this, message, Toast.LENGTH_LONG).show();

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
        pmiDatabase = new PmiDatabase(PmiMap.this);
        int[] distanceData = pmiDatabase.getPmiDistanceCheckData();
        OFFLINE_DISTANCE = distanceData[0];
        ONLINE_DISTANCE = distanceData[1];


        mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                double latitude = 0.0;
                double longitude = 0.0;
                gpsAccurancy = gpsTraker.getLocation().getAccuracy();


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

                        double distance = getDistanceBetweenPoles33New(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );

                        if (distance <= 10.0) {
                            isPoleFound = true;
                            //selectPoleNumber(e, mMapView, latitude, longitude);
                            //break;
                        }
                    }
                }
                if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {

                    for (int i = 0; i < filteredPolesList11.size(); i++) {

                        String poleLat = filteredPolesList11.get(i).getGLAT();
                        String poleLon = filteredPolesList11.get(i).getGLONG();

                        double distance = getDistanceBetweenPoles33New(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );


                        if (distance <= 10.0) {
                            isPoleFound = true;
                            //selectPoleNumber(e, mMapView, latitude, longitude);
                            //break;
                        }
                    }

                }
                if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {

                    for (int i = 0; i < filteredPolesListLT.size(); i++) {

                        String poleLat = filteredPolesListLT.get(i).getGLAT();
                        String poleLon = filteredPolesListLT.get(i).getGLONG();

                        double distance = getDistanceBetweenPoles33New(
                                poleLat,
                                poleLon,
                                latitude,
                                longitude
                        );

                        if (distance <= 10.0) {
                            isPoleFound = true;
                            // selectPoleNumber(e, mMapView, latitude, longitude);
                            //break;
                        }
                    }
                }
                if (!isPoleFound) {
                    String poleLat = "" + gpsTraker.getLongitude();
                    String poleLon = "" + gpsTraker.getLatitude();

                    double distance = getDistanceBetweenPoles33New(
                            poleLat,
                            poleLon,
                            latitude,
                            longitude
                    );
                    System.out.println("DISTANCE::::--->" + distance);

                    if (distance <= 100.0) {

                        openAlertDialogForNewPole("Is this Pole Location Not Surveyed in GIS", latitude, longitude);
                    } else {

                        openAlertDialog("Tap exactly (or within 10 meters radius) on the required tapping location");
                    }


                } else {
                    selectPoleNumberBottomSheet(e, mMapView, latitude, longitude);
                }


                return true;
            }

        });

        reload_map_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityChecking.checkConnection(PmiMap.this)) {
                    gpsTraker.getLocation();

                    // Just update the viewpoint, do not create a new map
                    Point newLocation = new Point(
                            gpsTraker.getLongitude(),
                            gpsTraker.getLatitude(),
                            SpatialReferences.getWgs84()
                    );
                    mv.setViewpointAsync(new Viewpoint(newLocation, 1000)); // 1000 is scale
                } else {
                    CommanUtilits.internetDialog(PmiMap.this);
                }
            }
        });

        reload_locations_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        // Select list based on FROM
        List<PoleModel33KV> polesList;

        if ("SURVEY".equalsIgnoreCase(FROM)) {

            polesList = Select33KVFeeder.polesList33;

        } else if ("RECT".equalsIgnoreCase(FROM)) {

            polesList = Select33KVFeederRectify.polesList33;

        } else {
            return;
        }
        Log.d("POLE_DEBUG====", "FROM: " + FROM);
        Log.d("POLE_DEBUG====", "Poles list is null: " + (polesList == null));
        Log.d("POLE_DEBUG====", "Poles list size: " +
                (polesList == null ? 0 : polesList.size()));
        mHandler.post(new Runnable() {

            private int currentIndex = 0;

            private List<PoleModel33KV> tempFilteredList = new ArrayList<>();

            private PointCollection tempPolylinePoints =
                    new PointCollection(SpatialReferences.getWgs84());

            private GpsTraker tempGpsTracker = new GpsTraker(PmiMap.this);

            private double currentLat = tempGpsTracker.getLatitude();
            private double currentLon = tempGpsTracker.getLongitude();

            // Check connectivity only once
            private int radius = ConnectivityChecking.checkConnection(PmiMap.this)
                    ? ONLINE_DISTANCE
                    : OFFLINE_DISTANCE;

            @Override
            public void run() {

                try {

                    int chunkSize = 50;

                    int endIndex = Math.min(
                            currentIndex + chunkSize,
                            polesList.size()
                    );

                    for (int i = currentIndex; i < endIndex; i++) {

                        double distance = getDistanceBetweenPoles33(
                                polesList.get(i).getGLONG(),
                                polesList.get(i).getGLAT(),
                                currentLat,
                                currentLon
                        );

                        if (distance <= radius) {
                            tempFilteredList.add(polesList.get(i));
                        }
                    }

                    currentIndex = endIndex;

                    if (currentIndex < polesList.size()) {

                        mHandler.post(this);

                    } else {

                        updateUIWithFiltered33KVData(tempFilteredList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void updateUIWithFiltered33KVData(List<PoleModel33KV> filteredList) {
        // Clear existing data
        filteredPolesList33.clear();
        filteredPolesList33.addAll(filteredList);

        // Clear existing overlays
        mv.getGraphicsOverlays().clear();

        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        // Create a separate runnable for UI updates
        Runnable uiUpdateRunnable = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                try {
                    // Process 10 poles at a time for UI updates
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

                        String labelText = "";
                        String poleNo = filteredPolesList33.get(i).getPOLE_NO();
                        String poleConfig = filteredPolesList33.get(i).getPOLE_CONFIG();
                        String poleType = filteredPolesList33.get(i).getPOLE_TYPE();
                        if ("TOWER".equalsIgnoreCase(poleType)) {
                            labelText = poleNo + " - " + poleConfig + " - " + poleType;
                        } else {
                            labelText = poleNo + " - " + poleConfig;
                        }

                        if (poleConfig != null) {
                            String configUpper = poleConfig.toUpperCase();

                            if (configUpper.contains("HT S")) {
                                labelText = poleNo + " - HT";
                            } else if (configUpper.contains("AB")) {
                                labelText = poleNo + " - AB";
                            } else {
                                if ("TOWER".equalsIgnoreCase(poleType)) {
                                    labelText = poleNo + " - " + poleType;
                                } else {
                                    labelText = poleNo;
                                }

                            }
                        }


                        TextSymbol absoluteText = new TextSymbol(
                                15,
                                labelText,
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
                        // Continue with next chunk
                        mHandler.postDelayed(this, 16); // ~60 FPS
                    } else {
                        // All UI updates complete
                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                        // hideLoading();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Start UI updates
        mHandler.post(uiUpdateRunnable);
    }

//    private void process11KVDataWithHandler() {
//        // Similar pattern for 11KV
//        mHandler.post(new Runnable() {
//            private int currentIndex = 0;
//            private List<PoleModel11KV> tempFilteredList = new ArrayList<>();
//            private GpsTraker tempGpsTracker = new GpsTraker(PmiMap.this);
//            private double currentLat = tempGpsTracker.getLatitude();
//            private double currentLon = tempGpsTracker.getLongitude();
//
//            @Override
//            public void run() {
//                try {
//                    int chunkSize = 50;
//                    int endIndex = Math.min(currentIndex + chunkSize, Select11KVFeeder.polesList11.size());
//
//                    for (int i = currentIndex; i < endIndex; i++) {
//                        double distance = getDistanceBetweenPoles11(
//                                Select11KVFeeder.polesList11.get(i).getGLONG(),
//                                Select11KVFeeder.polesList11.get(i).getGLAT(),
//                                currentLat, currentLon
//                        );
//                        int radius = 0;
//                        if (ConnectivityChecking.checkConnection(PmiMap.this)) {
//                            // ONLINE
//                            radius = ONLINE_DISTANCE;   // 500 meters
//                        } else {
//                            // OFFLINE
//                            radius = OFFLINE_DISTANCE;  // 1000 meters
//                        }
//
//                        if (distance <= radius) {
//                            tempFilteredList.add(Select11KVFeeder.polesList11.get(i));
//                        }
//

    /// /                        if (distance <= 2000) {
    /// /                            tempFilteredList.add(Select11KVFeeder.polesList11.get(i));
    /// /                        }
//                    }
//
//                    currentIndex = endIndex;
//
//                    if (currentIndex < Select11KVFeeder.polesList11.size()) {
//                        mHandler.post(this);
//                    } else {
//                        updateUIWithFiltered11KVData(tempFilteredList);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
    private void process11KVDataWithHandler() {

        // Select list based on FROM
        List<PoleModel11KV> polesList;

        if ("SURVEY".equalsIgnoreCase(FROM)) {
            polesList = Select11KVFeeder.polesList11;

        } else if ("RECT".equalsIgnoreCase(FROM)) {
            polesList = Select11KVFeederRectify.polesList11;

        } else {
            return;
        }
        Log.d("POLE_DEBUG", "FROM: " + FROM);
        Log.d("POLE_DEBUG", "Poles list is null: " + (polesList == null));
        Log.d("POLE_DEBUG", "Poles list size: " +
                (polesList == null ? 0 : polesList.size()));
        mHandler.post(new Runnable() {

            private int currentIndex = 0;
            private List<PoleModel11KV> tempFilteredList = new ArrayList<>();

            private GpsTraker tempGpsTracker = new GpsTraker(PmiMap.this);
            private double currentLat = tempGpsTracker.getLatitude();
            private double currentLon = tempGpsTracker.getLongitude();

            @Override
            public void run() {
                try {

                    int chunkSize = 50;

                    int endIndex = Math.min(
                            currentIndex + chunkSize,
                            polesList.size()
                    );

                    for (int i = currentIndex; i < endIndex; i++) {

                        double distance = getDistanceBetweenPoles11(
                                polesList.get(i).getGLONG(),
                                polesList.get(i).getGLAT(),
                                currentLat,
                                currentLon
                        );

                        int radius;

                        if (ConnectivityChecking.checkConnection(PmiMap.this)) {
                            radius = ONLINE_DISTANCE;
                        } else {
                            radius = OFFLINE_DISTANCE;
                        }

                        if (distance <= radius) {
                            tempFilteredList.add(polesList.get(i));
                        }
                    }

                    currentIndex = endIndex;

                    if (currentIndex < polesList.size()) {
                        mHandler.post(this);
                    } else {
                        updateUIWithFiltered11KVData(tempFilteredList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUIWithFiltered11KVData(List<PoleModel11KV> filteredList) {
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

                        String labelText = "";
                        String poleNo = filteredPolesList11.get(i).getPOLE_NO();
                        String poleConfig = filteredPolesList11.get(i).getPOLE_CONFIG();
                        String poleType = filteredPolesList11.get(i).getPOLE_TYPE();
                        if ("TOWER".equalsIgnoreCase(poleType)) {
                            labelText = poleNo + " - " + poleConfig + " - " + poleType;
                        } else {
                            labelText = poleNo + " - " + poleConfig;
                        }

                        if (poleConfig != null) {
                            String configUpper = poleConfig.toUpperCase();

                            if (configUpper.contains("DTR")) {
                                labelText = poleNo + " - DTR";
                            } else if (configUpper.contains("HT S")) {
                                labelText = poleNo + " - HT";
                            } else if (configUpper.contains("AB")) {
                                labelText = poleNo + " - AB";
                            } else {
                                if ("TOWER".equalsIgnoreCase(poleType)) {
                                    labelText = poleNo + " - " + poleType;
                                } else {
                                    labelText = poleNo;
                                }

                            }
                        }

                        TextSymbol absoluteText = new TextSymbol(
                                15,
                                labelText,
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);

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

    private void processLTDataWithHandler() {
        // Similar pattern for LT


        // Select list based on FROM
        List<PoleModelLT> polesList;

        if ("SURVEY".equalsIgnoreCase(FROM)) {

            polesList = SelectLTCircuit.polesListLT;

        } else if ("RECT".equalsIgnoreCase(FROM)) {

            polesList = SelectLTCircuitRectify.polesListLT;

        } else {
            return;
        }


        mHandler.post(new Runnable() {
            private int currentIndex = 0;
            private List<PoleModelLT> tempFilteredList = new ArrayList<>();
            private GpsTraker tempGpsTracker = new GpsTraker(PmiMap.this);
            private double currentLat = tempGpsTracker.getLatitude();
            private double currentLon = tempGpsTracker.getLongitude();

            @Override
            public void run() {
                try {
                    int chunkSize = 50;
                    int endIndex = Math.min(currentIndex + chunkSize, polesList.size());

                    for (int i = currentIndex; i < endIndex; i++) {
                        double distance = getDistanceBetweenPolesLT(
                                polesList.get(i).getGLONG(),
                                polesList.get(i).getGLAT(),
                                currentLat, currentLon
                        );
                        if (distance <= 20000000) {
                            tempFilteredList.add(polesList.get(i));
                        }
                    }

                    currentIndex = endIndex;

                    if (currentIndex < polesList.size()) {
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

                        String labelText = "";
                        String poleNo = filteredPolesListLT.get(i).getPOLE_NO();
                        String poleType = filteredPolesListLT.get(i).getPOLE_TYPE();
                        if ("TOWER".equalsIgnoreCase(poleType)) {
                            labelText = poleNo + " - " + poleType;
                        } else {
                            labelText = poleNo;
                        }
                        TextSymbol absoluteText = new TextSymbol(
                                15,
                                labelText,
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


    // Clean up when activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void openTransmitBottomSheet(boolean isTransmit) {


        if (isTransmit) {

            if (transmitDialog == null) {
                transmitDialog = new BottomSheetDialog(this);
                View view = getLayoutInflater()
                        .inflate(R.layout.bottom_sheet_loader_transmit, null);
                transmitDialog.setContentView(view);
                View parent = (View) view.getParent();
                parent.setBackgroundResource(android.R.color.transparent);
                transmitDialog.setCancelable(false);
            }

            transmitDialog.show();

        } else {
            if (transmitDialog != null && transmitDialog.isShowing()) {
                transmitDialog.dismiss();
            }
        }
    }


    private void selectPoleNumberBottomSheet(MotionEvent e, MapView mMapView,
                                             double lat, double lon) {

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(this);

        View view = getLayoutInflater()
                .inflate(R.layout.pole_bottom_sheet, null);

        bottomSheetDialog.setContentView(view);
        View parent = (View) view.getParent();
        parent.setBackgroundResource(android.R.color.transparent);

        Spinner sp_pole_number = view.findViewById(R.id.sp_pole_number);
        TextView back_to_home = view.findViewById(R.id.back_to_home);
        TextView name_value = view.findViewById(R.id.name_value);
        TextView id_value = view.findViewById(R.id.id_value);
        name_value.setText(NAME);
        id_value.setText(ID);

        List<String> poleList = new ArrayList<>();
        poleList.add("Select");

        double MAX_DISTANCE_METERS = 20.0;

        if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {

            for (int i = 0; i < filteredPolesList33.size(); i++) {

                String poleLat = filteredPolesList33.get(i).getGLONG();
                String poleLon = filteredPolesList33.get(i).getGLAT();

                double distance = getDistanceBetweenPoles33(
                        poleLat, poleLon, lat, lon);

                if (distance <= MAX_DISTANCE_METERS) {
                    poleList.add(filteredPolesList33.get(i).getPOLE_NO());
                }
            }
        }

        if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {

            for (int i = 0; i < filteredPolesList11.size(); i++) {

                String poleLat = filteredPolesList11.get(i).getGLONG();
                String poleLon = filteredPolesList11.get(i).getGLAT();

                double distance = getDistanceBetweenPoles33(
                        poleLat, poleLon, lat, lon);

                if (distance <= MAX_DISTANCE_METERS) {
                    poleList.add(filteredPolesList11.get(i).getPOLE_NO());
                }
            }
        }

        if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {

            for (int i = 0; i < filteredPolesListLT.size(); i++) {

                String poleLat = filteredPolesListLT.get(i).getGLONG();
                String poleLon = filteredPolesListLT.get(i).getGLAT();

                double distance = getDistanceBetweenPoles33(
                        poleLat, poleLon, lat, lon);

                if (distance <= MAX_DISTANCE_METERS) {
                    poleList.add(filteredPolesListLT.get(i).getPOLE_NO());
                }
            }
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        poleList);

        sp_pole_number.setAdapter(adapter);
        sp_pole_number.setSelection(0, true);

        back_to_home.setOnClickListener(v -> {

            if (sp_pole_number.getSelectedItem().toString()
                    .equalsIgnoreCase("Select")) {

                Toast.makeText(this,
                        "Please Select Pole Number",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            bottomSheetDialog.dismiss();

            CURRENT_SELECTED_POLE_NUMBER =
                    sp_pole_number.getSelectedItem().toString();

            PoleModel33KV selectedPoleDetails = new PoleModel33KV();

            // 🔹 Select pole details based on voltage level
            if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                for (int i = 0; i < filteredPolesList33.size(); i++) {
                    if (CURRENT_SELECTED_POLE_NUMBER.equalsIgnoreCase(filteredPolesList33.get(i).getPOLE_NO())) {
                        selectedPoleDetails.setPOLE_TYPE(filteredPolesList33.get(i).getPOLE_TYPE());
                        selectedPoleDetails.setPOLE_CONFIG(filteredPolesList33.get(i).getPOLE_CONFIG());
                        selectedPoleDetails.setPOLE_HEIGHT(filteredPolesList33.get(i).getPOLE_HEIGHT());
                        selectedPoleDetails.setPOLE_SUPPORT(filteredPolesList33.get(i).getPOLE_SUPPORT());
                        selectedPoleDetails.setPOLE_ID(filteredPolesList33.get(i).getPOLE_ID());
                        selectedPoleDetails.setPOLE_STRUCTURE(filteredPolesList33.get(i).getPOLE_STRUCTURE());
                        // CURRENT_SELECTED_POLE_ID = filteredPolesList33.get(i).getPOLE_ID();
                        // latti = filteredPolesList33.get(i).getGLAT();
                        // longi = filteredPolesList33.get(i).getGLONG();
                        break;
                    }
                }
            } else if ("11 KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                for (PoleModel11KV pole : filteredPolesList11) {
                    if (CURRENT_SELECTED_POLE_NUMBER.equalsIgnoreCase(pole.getPOLE_NO())) {
                        selectedPoleDetails.setPOLE_TYPE(pole.getPOLE_TYPE());
                        selectedPoleDetails.setPOLE_CONFIG(pole.getPOLE_CONFIG());
                        selectedPoleDetails.setPOLE_HEIGHT(pole.getPOLE_HEIGHT());
                        selectedPoleDetails.setPOLE_SUPPORT(pole.getPOLE_SUPPORT());
                        selectedPoleDetails.setPOLE_ID(pole.getPOLE_ID());
                        selectedPoleDetails.setPOLE_STRUCTURE(pole.getPOLE_STRUCTURE());
                        // CURRENT_SELECTED_POLE_ID = pole.getPOLE_ID();
                        // latti = pole.getGLAT();
                        // longi = pole.getGLONG();

                        break;
                    }
                }
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
                for (PoleModelLT pole : filteredPolesListLT) {
                    if (CURRENT_SELECTED_POLE_NUMBER.equalsIgnoreCase(pole.getPOLE_NO())) {
                        selectedPoleDetails.setPOLE_TYPE(pole.getPOLE_TYPE());
                        selectedPoleDetails.setPOLE_CONFIG(pole.getPOLE_CONFIG());
                        selectedPoleDetails.setPOLE_HEIGHT(pole.getPOLE_HEIGHT());
                        selectedPoleDetails.setPOLE_SUPPORT(pole.getPOLE_SUPPORT());
                        selectedPoleDetails.setPOLE_ID(pole.getPOLE_ID());
                        selectedPoleDetails.setPOLE_STRUCTURE(pole.getPOLE_STRUCTURE());
                        // CURRENT_SELECTED_POLE_ID = pole.getPOLE_ID();
                        //latti = pole.getGLAT();
                        // longi = pole.getGLONG();
                        break;
                    }
                }
            }
            if (selectedPoleDetails.getPOLE_CONFIG().contains("SS Bay")) {
                String msg = "Please Select other than SS Bay.";
                new AlertDialog.Builder(PmiMap.this)
                        .setTitle("SS Bay detected!!")
                        .setMessage(msg)
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .show();
            } else {
                openDefectDialogNew(CURRENT_SELECTED_POLE_NUMBER);
            }

            //openDefectDialog(CURRENT_SELECTED_POLE_NUMBER);
        });

        bottomSheetDialog.show();


    }

    private void openDefectDialogNew(String selectedPole) {
        SELECTED_POLE_CONFIG = "";
        SELECTED_POLE_STRUCTURE = "";
        aiListMain = new ArrayList<>();
        aiListMainDTR = new ArrayList<>();
        postingModelList.clear();
        newDefectDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        newDefectDialog.setContentView(R.layout.defect_dialog_new);
        newDefectDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            newDefectDialog.getWindow().setDecorFitsSystemWindows(false);
        }
        newDefectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        newDefectDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        newDefectDialog.show();

        submitButton = newDefectDialog.findViewById(R.id.submitButton);
        ImageView back_layout = newDefectDialog.findViewById(R.id.back_layout);

        TextView pole_no = newDefectDialog.findViewById(R.id.pole_no);
        TextView pole_config = newDefectDialog.findViewById(R.id.pole_config);
        TextView pole_type = newDefectDialog.findViewById(R.id.pole_type);
        TextView pole_height = newDefectDialog.findViewById(R.id.pole_height);
        TextView pole_support = newDefectDialog.findViewById(R.id.pole_support);
        TextView home_heading = newDefectDialog.findViewById(R.id.home_heading);
        TextView tv_question = newDefectDialog.findViewById(R.id.tv_question);

        TextView tv_youtube_view = newDefectDialog.findViewById(R.id.tv_youtube_view);
        TextView tv_dtr_pic_info = newDefectDialog.findViewById(R.id.tv_dtr_pic_info);
        LinearLayout layout_user_guide = newDefectDialog.findViewById(R.id.layout_user_guide);
        LinearLayout layout_pole_image = newDefectDialog.findViewById(R.id.layout_pole_image);
        LinearLayout layout_youtube = newDefectDialog.findViewById(R.id.layout_youtube);
        LinearLayout layout_dtr_image = newDefectDialog.findViewById(R.id.layout_dtr_image);
        ImageView dtr_image = newDefectDialog.findViewById(R.id.dtr_image);
        ImageView iv_pole_demo = newDefectDialog.findViewById(R.id.iv_pole_demo);

        rvDefectGroups = newDefectDialog.findViewById(R.id.rvDefectGroups);
        all_defects_layout = newDefectDialog.findViewById(R.id.all_defects_layout);
        layout_question = newDefectDialog.findViewById(R.id.layout_question);
        tv_no = newDefectDialog.findViewById(R.id.tv_no);
        tv_yes = newDefectDialog.findViewById(R.id.tv_yes);
        capture_Image_layout = newDefectDialog.findViewById(R.id.capture_Image_layout);
        taken_image_layout = newDefectDialog.findViewById(R.id.taken_image_layout);
        LinearLayout layoutChangeImage = newDefectDialog.findViewById(R.id.layoutChangeImage);
        imgPreview = newDefectDialog.findViewById(R.id.imgPreview);
        ImageView fullImage = newDefectDialog.findViewById(R.id.fullImage);

        LinearLayout dtr_image_layout = newDefectDialog.findViewById(R.id.dtr_image_layout);
        dtr_capture_Image_layout = newDefectDialog.findViewById(R.id.dtr_capture_Image_layout);
        dtr_taken_image_layout = newDefectDialog.findViewById(R.id.dtr_taken_image_layout);
        LinearLayout dtrlayoutChangeImage = newDefectDialog.findViewById(R.id.dtrlayoutChangeImage);
        dtrimgPreview = newDefectDialog.findViewById(R.id.dtrimgPreview);
        ImageView dtrfullImage = newDefectDialog.findViewById(R.id.dtrfullImage);

        dtrai_pole_layout = newDefectDialog.findViewById(R.id.dtrai_pole_layout);
        dtrai_cross_arm_layout = newDefectDialog.findViewById(R.id.dtrai_cross_arm_layout);
        dtrai_top_cleat_layout = newDefectDialog.findViewById(R.id.dtrai_top_cleat_layout);
        dtrai_others_layout = newDefectDialog.findViewById(R.id.dtrai_others_layout);
        dtrai_pole = newDefectDialog.findViewById(R.id.dtrai_pole);
        dtrai_cross_arm = newDefectDialog.findViewById(R.id.dtrai_cross_arm);
        dtrai_top_cleat = newDefectDialog.findViewById(R.id.dtrai_top_cleat);
        dtrai_others = newDefectDialog.findViewById(R.id.dtrai_others);
        dtrview_cross_arm = newDefectDialog.findViewById(R.id.dtrview_cross_arm);
        dtrview_top_cleat = newDefectDialog.findViewById(R.id.dtrview_top_cleat);
        dtrview_others = newDefectDialog.findViewById(R.id.dtrview_others);

        ai_pole_layout = newDefectDialog.findViewById(R.id.ai_pole_layout);
        ai_cross_arm_layout = newDefectDialog.findViewById(R.id.ai_cross_arm_layout);
        ai_top_cleat_layout = newDefectDialog.findViewById(R.id.ai_top_cleat_layout);
        ai_others_layout = newDefectDialog.findViewById(R.id.ai_others_layout);
        ai_pole = newDefectDialog.findViewById(R.id.ai_pole);
        ai_cross_arm = newDefectDialog.findViewById(R.id.ai_cross_arm);
        ai_top_cleat = newDefectDialog.findViewById(R.id.ai_top_cleat);
        ai_others = newDefectDialog.findViewById(R.id.ai_others);
        view_cross_arm = newDefectDialog.findViewById(R.id.view_cross_arm);
        view_top_cleat = newDefectDialog.findViewById(R.id.view_top_cleat);
        view_others = newDefectDialog.findViewById(R.id.view_others);


        if ("SURVEY".equalsIgnoreCase(FROM)) {
            if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                home_heading.setText("33kV Cyclone Damage Inspection");
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
                home_heading.setText("11kV Cyclone Damage Inspection");
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
                home_heading.setText("LT Cyclone Damage Inspection");
            }
            tv_question.setText("Is the selected location damaged?");
        } else {
            if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                home_heading.setText("33kV Cyclone Rectification");
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
                home_heading.setText("11kV Cyclone Rectification");
            } else if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
                home_heading.setText("LT Cyclone Rectification");
            }
            tv_question.setText("Is the selected location damaged and Rectified?");
        }

        iv_pole_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create ImageView
                ImageView imageView = new ImageView(v.getContext());
                imageView.setImageDrawable(iv_pole_demo.getDrawable()); // set same image
                imageView.setAdjustViewBounds(true);

                // Create AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(imageView);
                builder.setPositiveButton("Close", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dtr_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create ImageView
                ImageView imageView = new ImageView(v.getContext());
                imageView.setImageDrawable(dtr_image.getDrawable()); // set same image
                imageView.setAdjustViewBounds(true);

                // Create AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(imageView);
                builder.setPositiveButton("Close", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        layout_youtube.setOnClickListener(v -> {
            String url = "https://youtube.com/shorts/5s7BUjzlcIM?si=9y8DvWeeaavX3vAz";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage("com.google.android.youtube"); // Try YouTube app

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Fallback to browser if YouTube app not installed
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(webIntent);
            }
        });


        fullImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap myBitmap = BitmapFactory.decodeFile(MAIN_IMAGE_PATH);
                BitmapCache.put(FullScreenImageActivity.EXTRA_BITMAP_KEY, myBitmap);
                Intent intent = new Intent(PmiMap.this, FullScreenImageActivity.class);
                startActivity(intent);
            }
        });


        capture_Image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double currentLat = gpsTraker.getLatitude();
                double currentLon = gpsTraker.getLongitude();

                double radius = ConnectivityChecking.checkConnection(PmiMap.this)
                        ? ONLINE_DISTANCE : OFFLINE_DISTANCE;

                double distance = -1.0;
                boolean poleFound = false;

                if ("11 KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                    if ("SURVEY".equalsIgnoreCase(FROM)) {
                        for (int i = 0; i < Select11KVFeeder.polesList11.size(); i++) {

                            if (String.valueOf(CURRENT_SELECTED_POLE_NUMBER)
                                    .equalsIgnoreCase(
                                            String.valueOf(
                                                    Select11KVFeeder.polesList11
                                                            .get(i).getPOLE_NO()
                                            )
                                    )) {

                                distance = getDistanceBetweenPoles11(
                                        Select11KVFeeder.polesList11.get(i).getGLONG(),
                                        Select11KVFeeder.polesList11.get(i).getGLAT(),
                                        currentLat,
                                        currentLon
                                );

                                poleFound = true;
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < Select11KVFeederRectify.polesList11.size(); i++) {

                            if (String.valueOf(CURRENT_SELECTED_POLE_NUMBER)
                                    .equalsIgnoreCase(
                                            String.valueOf(
                                                    Select11KVFeederRectify.polesList11
                                                            .get(i).getPOLE_NO()
                                            )
                                    )) {

                                distance = getDistanceBetweenPoles11(
                                        Select11KVFeederRectify.polesList11.get(i).getGLONG(),
                                        Select11KVFeederRectify.polesList11.get(i).getGLAT(),
                                        currentLat,
                                        currentLon
                                );

                                poleFound = true;
                                break;
                            }
                        }
                    }


                } else if ("33KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {

                    if ("SURVEY".equalsIgnoreCase(FROM)) {
                        for (int i = 0; i < Select33KVFeeder.polesList33.size(); i++) {

                            if (String.valueOf(CURRENT_SELECTED_POLE_NUMBER)
                                    .equalsIgnoreCase(
                                            String.valueOf(
                                                    Select33KVFeeder.polesList33
                                                            .get(i).getPOLE_NO()
                                            )
                                    )) {

                                distance = getDistanceBetweenPoles33(
                                        Select33KVFeeder.polesList33.get(i).getGLONG(),
                                        Select33KVFeeder.polesList33.get(i).getGLAT(),
                                        currentLat,
                                        currentLon
                                );

                                poleFound = true;
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < Select33KVFeederRectify.polesList33.size(); i++) {

                            if (String.valueOf(CURRENT_SELECTED_POLE_NUMBER)
                                    .equalsIgnoreCase(
                                            String.valueOf(
                                                    Select33KVFeederRectify.polesList33
                                                            .get(i).getPOLE_NO()
                                            )
                                    )) {

                                distance = getDistanceBetweenPoles33(
                                        Select33KVFeederRectify.polesList33.get(i).getGLONG(),
                                        Select33KVFeederRectify.polesList33.get(i).getGLAT(),
                                        currentLat,
                                        currentLon
                                );

                                poleFound = true;
                                break;
                            }
                        }
                    }

                } else {

                    if ("SURVEY".equalsIgnoreCase(FROM)) {
                        for (int i = 0; i < SelectLTCircuit.polesListLT.size(); i++) {

                            if (String.valueOf(CURRENT_SELECTED_POLE_NUMBER)
                                    .equalsIgnoreCase(
                                            String.valueOf(
                                                    SelectLTCircuit.polesListLT
                                                            .get(i).getPOLE_NO()
                                            )
                                    )) {

                                distance = getDistanceBetweenPolesLT(
                                        SelectLTCircuit.polesListLT.get(i).getGLONG(),
                                        SelectLTCircuit.polesListLT.get(i).getGLAT(),
                                        currentLat,
                                        currentLon
                                );

                                poleFound = true;
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < SelectLTCircuit.polesListLT.size(); i++) {

                            if (String.valueOf(CURRENT_SELECTED_POLE_NUMBER)
                                    .equalsIgnoreCase(
                                            String.valueOf(
                                                    SelectLTCircuit.polesListLT
                                                            .get(i).getPOLE_NO()
                                            )
                                    )) {

                                distance = getDistanceBetweenPolesLT(
                                        SelectLTCircuit.polesListLT.get(i).getGLONG(),
                                        SelectLTCircuit.polesListLT.get(i).getGLAT(),
                                        currentLat,
                                        currentLon
                                );

                                poleFound = true;
                                break;
                            }
                        }
                    }
                }

                System.out.println("CURRENT SELECTED POLE: "
                        + CURRENT_SELECTED_POLE_NUMBER);

                System.out.println("CURRENT GPS LAT: " + currentLat);
                System.out.println("CURRENT GPS LNG: " + currentLon);
                System.out.println("DISTANCE CHECK: " + distance);
                System.out.println("ALLOWED RADIUS: " + radius);

                openCameraForMainImage();

//                if (!poleFound) {
//
//                    openAlertDialog(
//                            "Selected pole not found in the voltage-level pole list"
//                    );
//
//                } else if (distance >= 0 && distance <= radius) {
//
//                    // Pole is within the allowed radius
//                    openCameraForMainImage();
//
//                } else {
//
//                    openAlertDialog(
//                            "The Selected Pole Location is very far from your capturing pole location"
//                    );
//                }
            }
        });


        layoutChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraForMainImage();
            }
        });

        dtrfullImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap myBitmap = BitmapFactory.decodeFile(DTR_MAIN_IMAGE_PATH);
                BitmapCache.put(FullScreenImageActivity.EXTRA_BITMAP_KEY, myBitmap);
                Intent intent = new Intent(PmiMap.this, FullScreenImageActivity.class);
                startActivity(intent);
            }
        });

        dtr_capture_Image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double currentLat = gpsTraker.getLatitude();
                double currentLon = gpsTraker.getLongitude();

                double distance = 0.0;
                double radius = 0.0;
                boolean isWithinRadius = false;
                if (ConnectivityChecking.checkConnection(PmiMap.this)) {
                    radius = ONLINE_DISTANCE;
                } else {
                    radius = OFFLINE_DISTANCE;
                }
                if ("11 KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                    for (int i = 0; i < Select11KVFeeder.polesList11.size(); i++) {
                        distance = getDistanceBetweenPoles11(
                                Select11KVFeeder.polesList11.get(i).getGLONG(),
                                Select11KVFeeder.polesList11.get(i).getGLAT(),
                                currentLat, currentLon
                        );
                    }
                    if (distance <= radius) {
                        openCameraForMainImageDTR();
                    } else {
                        openAlertDialog("The Selected Pole Location is very far from your capturing pole location");

                    }
                }


                //openCameraForMainImageDTR();
            }
        });

        dtrlayoutChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraForMainImageDTR();
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDefective = true;
                all_defects_layout.setVisibility(View.VISIBLE);
                setDefectGroupAdapter(true);
            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDefective = false;
                all_defects_layout.setVisibility(View.VISIBLE);
                setDefectGroupAdapter(false);
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectedDefect = null;
                newDefectDialog.dismiss();
                // Clear selected chip list
                selectedChipDefects.clear();
                IS_DTR_POLE = false;
                IS_DTR_AI_IMAGE_VERIFIED = false;
                DTR_MAIN_IMAGE_PATH = "";
                SELECTED_GROUP_NAME = "";


                // Reset all master defects
                for (DefectsModel defect : PMINewHome.allDefectsList) {
                    defect.setSelected(false);
                    defect.setImageSelected(false);
                    defect.setImagePath("");
                }

                // Refresh UI
                if (defectGroupAdapter != null) {
                    defectGroupAdapter.notifyDataSetChanged();
                }

                if (defectTypeAdapter != null) {
                    defectTypeAdapter.notifyDataSetChanged();
                }

                for (DefectGroupModel group : PMINewHome.defectsGroupListLT) {
                    group.setExpanded(false);
                    group.setImageSelected(false);
                    group.setImage("");
                }
                for (DefectGroupModel group : PMINewHome.defectsGroupList11) {
                    group.setExpanded(false);
                    group.setImageSelected(false);
                    group.setImage("");
                }
                for (DefectGroupModel group : PMINewHome.defectsGroupList33) {
                    group.setExpanded(false);
                    group.setImageSelected(false);
                    group.setImage("");
                }


                newDefectDialog.dismiss();
            }
        });


        pole_no.setText("Location #" + selectedPole);

        PoleModel33KV selectedPoleDetails = new PoleModel33KV();

        // 🔹 Select pole details based on voltage level
        if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
            for (int i = 0; i < filteredPolesList33.size(); i++) {
                if (selectedPole.equalsIgnoreCase(filteredPolesList33.get(i).getPOLE_NO())) {
                    selectedPoleDetails.setPOLE_TYPE(filteredPolesList33.get(i).getPOLE_TYPE());
                    selectedPoleDetails.setPOLE_CONFIG(filteredPolesList33.get(i).getPOLE_CONFIG());
                    selectedPoleDetails.setPOLE_HEIGHT(filteredPolesList33.get(i).getPOLE_HEIGHT());
                    selectedPoleDetails.setPOLE_SUPPORT(filteredPolesList33.get(i).getPOLE_SUPPORT());
                    selectedPoleDetails.setPOLE_ID(filteredPolesList33.get(i).getPOLE_ID());
                    CURRENT_SELECTED_POLE_ID = filteredPolesList33.get(i).getPOLE_ID();
                    SELECTED_POLE_STRUCTURE = filteredPolesList33.get(i).getPOLE_STRUCTURE();
                    latti = filteredPolesList33.get(i).getGLAT();
                    longi = filteredPolesList33.get(i).getGLONG();
                    break;
                }
            }
        } else if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
            for (PoleModel11KV pole : filteredPolesList11) {
                if (selectedPole.equalsIgnoreCase(pole.getPOLE_NO())) {
                    selectedPoleDetails.setPOLE_TYPE(pole.getPOLE_TYPE());
                    selectedPoleDetails.setPOLE_CONFIG(pole.getPOLE_CONFIG());
                    selectedPoleDetails.setPOLE_HEIGHT(pole.getPOLE_HEIGHT());
                    selectedPoleDetails.setPOLE_SUPPORT(pole.getPOLE_SUPPORT());
                    selectedPoleDetails.setPOLE_ID(pole.getPOLE_ID());
                    CURRENT_SELECTED_POLE_ID = pole.getPOLE_ID();
                    SELECTED_POLE_STRUCTURE = pole.getPOLE_STRUCTURE();
                    latti = pole.getGLAT();
                    longi = pole.getGLONG();

                    break;
                }
            }
        } else if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
            for (PoleModelLT pole : filteredPolesListLT) {
                if (selectedPole.equalsIgnoreCase(pole.getPOLE_NO())) {
                    selectedPoleDetails.setPOLE_TYPE(pole.getPOLE_TYPE());
                    selectedPoleDetails.setPOLE_CONFIG(pole.getPOLE_CONFIG());
                    selectedPoleDetails.setPOLE_HEIGHT(pole.getPOLE_HEIGHT());
                    selectedPoleDetails.setPOLE_SUPPORT(pole.getPOLE_SUPPORT());
                    selectedPoleDetails.setPOLE_ID(pole.getPOLE_ID());
                    CURRENT_SELECTED_POLE_ID = pole.getPOLE_ID();
                    SELECTED_POLE_STRUCTURE = pole.getPOLE_STRUCTURE();
                    latti = pole.getGLAT();
                    longi = pole.getGLONG();
                    break;
                }
            }
        }

        pole_config.setText(selectedPole.contains("Pole") ? selectedPole : selectedPoleDetails.getPOLE_CONFIG());
        pole_type.setText(selectedPoleDetails.getPOLE_TYPE());
        pole_height.setText(selectedPoleDetails.getPOLE_HEIGHT());
        pole_support.setText(selectedPoleDetails.getPOLE_SUPPORT());
        DTR_POLE_HEIGHT = selectedPoleDetails.getPOLE_HEIGHT();
        SELECTED_POLE_CONFIG = selectedPole.contains("Pole") ? selectedPole : selectedPoleDetails.getPOLE_CONFIG();


        //System.out.println("GGSGGSGGSGGSGGSGGS"+SELECTED_POLE_STRUCTURE);
        if (!TextUtils.isEmpty(selectedPoleDetails.getPOLE_CONFIG())) {

            String poleConfig = selectedPoleDetails.getPOLE_CONFIG();
            String poleType = selectedPoleDetails.getPOLE_TYPE();

            if (poleConfig.equalsIgnoreCase("DTR Pole")) {
                layout_dtr_image.setVisibility(View.VISIBLE);
                layout_pole_image.setVisibility(View.VISIBLE);
            } else {
                layout_pole_image.setVisibility(View.VISIBLE);
            }

            if (poleConfig.equalsIgnoreCase("SS Bay")) {
                layout_user_guide.setVisibility(View.GONE);
            }

            if ("TOWER".equalsIgnoreCase(poleType)) {
                layout_user_guide.setVisibility(View.GONE);
            }

            IS_DTR_POLE = poleConfig.equalsIgnoreCase("DTR POLE");

        } else {
            layout_pole_image.setVisibility(View.VISIBLE);
            IS_DTR_POLE = false;
        }

//        if (!selectedPoleDetails.getPOLE_CONFIG().isEmpty()){
//            if (selectedPoleDetails.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
//                layout_dtr_image.setVisibility(View.VISIBLE);
//                layout_pole_image.setVisibility(View.VISIBLE);
//            } else {
//                layout_pole_image.setVisibility(View.VISIBLE);
//            }
//
//            if (selectedPoleDetails.getPOLE_CONFIG().equalsIgnoreCase("SS Bay")) {
//                layout_user_guide.setVisibility(View.GONE);
//                layout_user_guide.setVisibility(View.GONE);
//            }
//            if (selectedPoleDetails.getPOLE_TYPE().equalsIgnoreCase("TOWER")) {
//                layout_user_guide.setVisibility(View.GONE);
//
//            }
//            if (selectedPoleDetails.getPOLE_CONFIG().equalsIgnoreCase("DTR POLE")) {
//                IS_DTR_POLE = true;
//            } else {
//                IS_DTR_POLE = false;
//            }
//        }else {
//            layout_pole_image.setVisibility(View.VISIBLE);
//        }

        if (IS_DTR_POLE) {
            dtr_image_layout.setVisibility(View.GONE);
        } else {
            dtr_image_layout.setVisibility(View.GONE);
        }

        if (ConnectivityChecking.checkConnection(PmiMap.this)) {
            submitButton.setText("Transmit Data");
        } else {
            submitButton.setText("SUBMIT");
        }

    }


    private void setDefectGroupAdapter(boolean isDefective) {

        ArrayList<DefectGroupModel> tempList = new ArrayList<>();

        if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {

            for (DefectGroupModel group : PMINewHome.defectsGroupListLT) {

                if (isDefective) {
                    if (!"Non defective".equalsIgnoreCase(group.getDefectGroup())) {
                        tempList.add(group);
                    }
                } else {
                    if ("Non defective".equalsIgnoreCase(group.getDefectGroup())) {
                        tempList.add(group);
                    }
                }
            }

        } else if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {

            for (DefectGroupModel group : PMINewHome.defectsGroupList11) {

                String defectGroup = group.getDefectGroup();

                if (isDefective) {

                    if ("Non defective".equalsIgnoreCase(defectGroup)) {
                        continue;
                    }
                    if ("DTR related".equalsIgnoreCase(defectGroup)) {
                        if ("DTR Pole".equalsIgnoreCase(SELECTED_POLE_CONFIG)) {
                            tempList.add(group);
                        }
                    } else {
                        tempList.add(group);
                    }

                } else {

                    // NO -> Only Non defective
                    if ("Non defective".equalsIgnoreCase(defectGroup)) {
                        tempList.add(group);
                    }
                }
            }

        } else if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {

            for (DefectGroupModel group : PMINewHome.defectsGroupList33) {

                if (isDefective) {
                    // YES -> Do NOT show Non defective
                    if (!"Non defective".equalsIgnoreCase(group.getDefectGroup())) {
                        tempList.add(group);
                    }
                } else {
                    // NO -> ONLY Non defective
                    if ("Non defective".equalsIgnoreCase(group.getDefectGroup())) {
                        tempList.add(group);
                    }
                }
            }
        }

        defectGroupAdapter = new DefectGroupAdapter(
                this,
                tempList,
                PmiMap.this
        );

        rvDefectGroups.setLayoutManager(new LinearLayoutManager(this));
        rvDefectGroups.setAdapter(defectGroupAdapter);
    }

    private boolean validateSelectedDefectGroups() {

        // No defect selected at all
        if (selectedChipDefects == null || selectedChipDefects.isEmpty()) {
            Toast.makeText(
                    this,
                    "Please select at least one Damage",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        /*
         * Check every selected subgroup/type.
         *
         * Replace `PMINewHome.allDefectsList` below with the list that
         * contains ALL your defects if needed.
         */

        HashSet<String> selectedSubGroups = new HashSet<>();

        // Get all subgroups/types which contain selected defects
        for (DefectsModel defect : PMINewHome.allDefectsList) {

            if (defect == null) {
                continue;
            }

            // This means the subgroup/type is selected/opened
            if (defect.isSelected()) {

                String defectType = defect.getDEFECT_TYPE();

                if (defectType != null && !defectType.trim().isEmpty()) {
                    selectedSubGroups.add(defectType.trim());
                }
            }
        }

        // Check each selected subgroup
        for (String defectType : selectedSubGroups) {

            boolean hasDefect = false;

            for (DefectsModel selectedDefect : selectedChipDefects) {

                if (selectedDefect == null) {
                    continue;
                }

                String selectedType = selectedDefect.getDEFECT_TYPE();

                if (selectedType != null
                        && selectedType.equalsIgnoreCase(defectType)) {

                    hasDefect = true;
                    break;
                }
            }

            if (!hasDefect) {
                Toast.makeText(
                        this,
                        "Please select at least one sub Damage from " + defectType,
                        Toast.LENGTH_SHORT
                ).show();

                return false;
            }
        }

        return true;
    }

    private void openCameraForMainImage() {
        Intent intentOpenCamera = new Intent(PmiMap.this, MainActivity.class);
        startActivity(intentOpenCamera);
        isMainImagePMICaptured = false;
        mainImageBitmapPMI = null;
        mainPhotoClickedPMI = true;
    }

    //New Pmi changes
    private void openCameraForMainImageDTR() {
        Intent intentOpenCamera = new Intent(PmiMap.this, MainActivity.class);
        startActivity(intentOpenCamera);
        isMainImagePMICapturedDTR = false;
        mainImageBitmapPMIDTR = null;
        mainPhotoClickedPMIDTR = true;
    }


    private String selectedHeight = "", selectedPoleType = "";
    private String latti = "", longi = "";


    private void showDistanceAlertDialog(Dialog payDialog, boolean internet) {
        String msg = internet
                ? "The selected location is 200 meters away from your current location"
                : "The selected location is 500 meters away from your current location";
        new AlertDialog.Builder(PmiMap.this)
                .setTitle("Location Warning")
                .setMessage(msg)
                .setPositiveButton("Okay", (dialog, which) -> {
                    dialog.dismiss();
                    payDialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    //==========================


    private long createRandomUID() {
        UUID uuid = UUID.randomUUID();
        // System.out.println("UUID: " + uuid);
        long randomNumber = uuid.getLeastSignificantBits();
        randomNumber = Math.abs(randomNumber);
        //System.out.println("Random Number: " + randomNumber);
        return randomNumber;
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

    private void openAlertDialogForNewPole(String message, double latitude, double longitude) {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.not_surveyed_dialog, null);
        dialog.setContentView(view);

        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView title = view.findViewById(R.id.title);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView btn_submit = view.findViewById(R.id.btn_submit);
        Spinner sp_pole_type_cyclone = view.findViewById(R.id.sp_pole_type_cyclone);
        LinearLayout layout_pole_type_cyclone = view.findViewById(R.id.layout_pole_type_cyclone);
        title.setText(message);
        List<String> poleConfig = new ArrayList<>();
        poleConfig.add("Select");
        poleConfig.add("Line-Pole");
        if (!"LT".equalsIgnoreCase(SELECTED_VOLTAGE)) {
            poleConfig.add("AB-Pole");
        }
        if ("11 KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
            poleConfig.add("DTR Pole");
            poleConfig.add("HT-Service Pole");
        }
        if ("33KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
            poleConfig.add("HT-Service Pole");
        }

        SpinnerAdapter<String> adapterPoleConfig =
                new SpinnerAdapter<>(PmiMap.this, poleConfig);

        sp_pole_type_cyclone.setAdapter(adapterPoleConfig);

        btn_submit.setOnClickListener(v -> {
            if (sp_pole_type_cyclone.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(this, "Please Select Pole Type", Toast.LENGTH_SHORT).show();
                return;
            } else {
                CYCLONE_POLE_LAT = latitude;
                CYCLONE_POLE_LONG = longitude;
                CURRENT_SELECTED_POLE_NUMBER = createPoleNumber();
                openDefectDialogNew(sp_pole_type_cyclone.getSelectedItem().toString());
                //transmitDataCyclone(latitude,longitude);
                dialog.dismiss();

            }

        });

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
        okBtn.setText("Yes");
        cancelBtn.setText("No");
        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Click action
        okBtn.setOnClickListener(v -> {
            layout_pole_type_cyclone.setVisibility(View.VISIBLE);

        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.90), // 90% width
                    ViewGroup.LayoutParams.WRAP_CONTENT                 // Auto height
            );
        }

        dialog.show();
    }

    private void transmitDataCyclone(double latitude, double longitude) {
        TransmitLoadingBottomSheet.show(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        String device_id = preferences.getString(MobileConstants.DEVICE_ID, "");
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonArray array = new JsonArray();

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("APP_VERSION", MobileConstants.CYCLONE_SURVEY);
        requestObject.addProperty("APP_VERSION2", getResources().getString(R.string.displayVersion));
        requestObject.addProperty("GPS_ACCURACY", gpsAccurancy);
        requestObject.addProperty("ID", createRandomUID());
        requestObject.addProperty("DEVICE_ID", device_id);
        requestObject.addProperty("DEFECT_ID", "161");
        requestObject.addProperty("POLE_ID", UUID.randomUUID().toString());
        requestObject.addProperty("POLE_NO", createPoleNumber());
        requestObject.addProperty("VOLTAGE_LEVEL", SELECTED_VOLTAGE);
        requestObject.addProperty("DTR_CODE", SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? NAME : "");
        requestObject.addProperty("LT_CKT_CODE", SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? ID : "");
        requestObject.addProperty("FEEDER_ID", SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? "" : ID);


        requestObject.addProperty("DEFECT_BY_USER", "Y");

        requestObject.addProperty("DEFECT_TYPE", "Pole Fallen");


        requestObject.addProperty("DEFECT_SUB_TYPE", "Pole Fallen");
        requestObject.addProperty("DEFECT_SUB_TYPE_ID", "161");
        requestObject.addProperty("CYCLONE", "NOT SURVEYED");
        requestObject.addProperty("CURRENT_LAT", gpsTraker.getLongitude());
        requestObject.addProperty("CURRENT_LONG", gpsTraker.getLatitude());
        requestObject.addProperty("RECT_REMARKS", "");
        requestObject.addProperty("USERID", userId);
        requestObject.addProperty("USERNAME", loginDataResponses.get(0).getNAME());
        requestObject.addProperty("SURVEY_MODE", "ONLINE");

        requestObject.addProperty("DEFECT_GROUP", "Pole Fallen");

        requestObject.addProperty("POLE_LAT", latitude);
        requestObject.addProperty("POLE_LONG", longitude);


        JsonArray jsonArray = new JsonArray();
        requestObject.add("RESPONSE", jsonArray);
        array.add(requestObject);

        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        // Call<JsonObject> responseVoCall = retrofitServicePmi.transmitData(array);
        Call<JsonObject> responseVoCall = retrofitServicePmi.transmitDataNew(array);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                TransmitLoadingBottomSheet.dismiss();
                openTransmitBottomSheet(false);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            //selectedChipDefects.clear();
                            selectedDefect = null;
                            SELECTED_GROUP_NAME = "";
                            IS_DTR_POLE = false;
                            IS_DTR_AI_IMAGE_VERIFIED = false;
                            DTR_MAIN_IMAGE_PATH = "";

                            PmiDatabase pmiDatabase = new PmiDatabase(PmiMap.this);
                            pmiDatabase.deleteAllPmiData();


                            Toast.makeText(PmiMap.this, "Damage Transmitted successfully", Toast.LENGTH_LONG).show();


                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    } else {


                        PmiDatabase pmiDatabase = new PmiDatabase(PmiMap.this);
                        pmiDatabase.deleteAllPmiData();

                        Toast.makeText(PmiMap.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(
                                PmiMap.this.findViewById(android.R.id.content),
                                surveyedResponseVo.get("ERR_MSG").toString(),
                                Snackbar.LENGTH_SHORT
                        ).show();

                    }


                } else {

                    Toast.makeText(PmiMap.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                openTransmitBottomSheet(false);
                // progressDialog.dismiss();
                Toast.makeText(PmiMap.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


    private String createPoleNumber() {

        StringBuilder result = new StringBuilder("C");
        Random random = new Random();

        // Generate 4 random numbers
        for (int i = 0; i < 4; i++) {
            result.append(random.nextInt(10));
        }

        // Generate 3 random alphabets
        for (int i = 0; i < 3; i++) {
            result.append((char) ('A' + random.nextInt(26)));
        }

        return result.toString();
    }


    private void openAlertDialogForDuplicate(String message) {
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
        cancelBtn.setVisibility(View.VISIBLE);
        okBtn.setText("Update");
        cancelBtn.setText("Cancel");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Click action
        okBtn.setOnClickListener(v -> {
            openDefectDialogNew(CURRENT_SELECTED_POLE_NUMBER);
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

    private void openAlertDialogRetry(String message) {
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
            openCameraForMainImage();
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }


    private void openAlertDialogRetryDTR(String message) {
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
            openCameraForMainImageDTR();
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK /*&& data != null*/) {
            //  Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            Bitmap imageBitmap = BitmapFactory.decodeFile(CURRENT_IMAGE_PATH);

            // Get the correct ImageView from the mapping
            ImageView imageView = requestCodeToImageViewMap.get(requestCode);
            if (imageView != null) {
                imageView.setImageBitmap(imageBitmap);
            }
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
                new AlertDialog.Builder(PmiMap.this)
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
                    Toast.makeText(PmiMap.this, message, Toast.LENGTH_LONG).show();

                });
                // mLocationDisplay.stop();
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private double getDistanceBetweenPoles33(
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

    private double getDistanceBetweenPoles11(
            String poleLat,
            String poleLon,
            double currentLat,
            double currentLon
    ) {
        try {
            //System.out.println("CURRENT LOCATION LAT 11KV : " + currentLat);
            //System.out.println("CURRENT LOCATION LON 11KV : " + currentLon);
            //System.out.println("POLE LOCATION LAT 11KV : " + poleLat);
            //System.out.println("POLE LOCATION LON 11KV : " + poleLon);

            Location poleLocation = new Location("pole");
            poleLocation.setLatitude(Double.parseDouble(poleLat));   // ✅ LAT
            poleLocation.setLongitude(Double.parseDouble(poleLon)); // ✅ LON

            Location currentLocation = new Location("current");
            currentLocation.setLatitude(currentLat);   // ✅ LAT
            currentLocation.setLongitude(currentLon); // ✅ LON

            double distanceInMeters = poleLocation.distanceTo(currentLocation);


            return distanceInMeters;

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
            poleLocation.setLatitude(Double.parseDouble(poleLat));   // ✅ LAT
            poleLocation.setLongitude(Double.parseDouble(poleLon)); // ✅ LON

            // distance in METERS
            return poleLocation.distanceTo(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE; // safety fallback
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent playIntent = new Intent(PmiMap.this, PMINewHome.class); //Intent.ACTION_VIEW);
        startActivity(playIntent);
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mv.pause();
    }

    //New Pmi changes
    @Override
    protected void onResume() {
        super.onResume();
        mv.resume();

        if (isImageCapturedPMI1) {
            imageUri = getLastImageUri(this);

            if (imageUri != null) {
                try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap == null) {
                        Toast.makeText(this, "Failed to decode captured image.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                   /* if (selectedDefect != null) {
                        currentPhotoPath = getLastImagePath(this);
                        selectedDefect.setImageSelected(true);
                        isImageCapturedPMI1 = false;
                        selectedDefect.setImagePath(currentPhotoPath);
                        defectTypeAdapter.refreshData();
                        defectTypeAdapter.notifyDataSetChanged();
                        Log.d("IMAGE_PATH", selectedDefect.getImagePath());
                        Log.d("IMAGE_SELECTED", String.valueOf(selectedDefect.isImageSelected()));
                    }*/

                   /* if (selectedDefect != null) {

                        for (DefectsModel model : PMINewHome.allDefectsList) {

                            if (model.getDEFECT_SUB_TYPE_ID().equalsIgnoreCase(selectedDefect.getDEFECT_SUB_TYPE_ID())) {
                                currentPhotoPath = getLastImagePath(this);
                                isImageCapturedPMI1 = false;
                                model.setSelected(true);
                                model.setImageSelected(true);
                                model.setImagePath(currentPhotoPath);
                                break;
                            }
                        }

                        defectGroupAdapter.notifyDataSetChanged();
                    }*/


                    if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
                        for (DefectGroupModel model : PMINewHome.defectsGroupListLT) {

                            if (model.getDefectGroup().equalsIgnoreCase(SELECTED_GROUP_NAME)) {
                                currentPhotoPath = getLastImagePath(this);
                                isImageCapturedPMI1 = false;
                                model.setImageSelected(true);
                                model.setImage(currentPhotoPath);
                                break;
                            }
                        }
                    }
                    if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
                        for (DefectGroupModel model : PMINewHome.defectsGroupList11) {

                            if (model.getDefectGroup().equalsIgnoreCase(SELECTED_GROUP_NAME)) {
                                currentPhotoPath = getLastImagePath(this);
                                isImageCapturedPMI1 = false;
                                model.setImageSelected(true);
                                model.setImage(currentPhotoPath);
                                break;
                            }
                        }
                    }
                    if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                        for (DefectGroupModel model : PMINewHome.defectsGroupList33) {

                            if (model.getDefectGroup().equalsIgnoreCase(SELECTED_GROUP_NAME)) {
                                currentPhotoPath = getLastImagePath(this);
                                isImageCapturedPMI1 = false;
                                model.setImageSelected(true);
                                model.setImage(currentPhotoPath);
                                break;
                            }
                        }
                    }

                    defectGroupAdapter.notifyDataSetChanged();

                    //  showImageConfirmDialog(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error reading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (isMainImagePMICaptured) {
            Uri imageUri = getLastImageUri(this);

            if (imageUri != null) {
                try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap == null) {
                        Toast.makeText(this, "Failed to decode captured image.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    showImageConfirmDialogMain(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error reading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (isMainImagePMICapturedDTR) {
            Uri imageUri = getLastImageUri(this);

            if (imageUri != null) {
                try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap == null) {
                        Toast.makeText(this, "Failed to decode captured image.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    showImageConfirmDialogMainDTR(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error reading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void processMainCapturedImage(Bitmap bitmap) {
        Bitmap safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        isMainImagePMICaptured = false;

        aiDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        aiDialog.setContentView(R.layout.ai_verification);
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
        uploadMainImageToServer(safeBitmap);
    }

    //New Pmi changes
    private void processMainCapturedImageDTR(Bitmap bitmap) {
        Bitmap safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        isMainImagePMICapturedDTR = false;


        aiDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        aiDialog.setContentView(R.layout.ai_verification);
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
        uploadMainImageToServerDTR(safeBitmap);
    }

    private void uploadMainImageToServerDTR(Bitmap bitmap) {

        {
            try {
                String DTR_MAIN_IMAGE_PATH_TEMP = getLastImagePath(this);
                if (DTR_MAIN_IMAGE_PATH_TEMP == null || DTR_MAIN_IMAGE_PATH_TEMP.isEmpty()) {
                    aiDialog.dismiss();
                    Toast.makeText(this, "Invalid image path", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if (progressDialog != null) progressDialog.show();

                File file = new File(DTR_MAIN_IMAGE_PATH_TEMP);
                if (!file.exists()) {
                    Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
                    //if (progressDialog != null) progressDialog.dismiss();
                    aiDialog.dismiss();
                    return;
                }

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                ImageRetrofitService service = ImageRetrofitClient.getRetrofitInstance().create(ImageRetrofitService.class);
                Call<JsonObject> call = service.uploadImageDTRPlinthClearance(imagePart, DTR_POLE_HEIGHT);
                //  Call<JsonObject> call = service.uploadImage(imagePart, false);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //if (progressDialog != null) progressDialog.dismiss();
                        aiDialog.dismiss();
                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(PmiMap.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JsonObject object = response.body();
                        if (object.has("status") && object.get("status").getAsString().equalsIgnoreCase("done")) {
                            DTR_MAIN_IMAGE_PATH = getLastImagePath(PmiMap.this);
                            handleSuccessfulUploadForMainImageDTR(object, bitmap);
                            IS_DTR_AI_IMAGE_VERIFIED = true;
                        } else {
                            if (object.has("status") && object.get("status").getAsString().equalsIgnoreCase("no_plinth_detected")) {
                                aiListMainDTR.clear();
                                DTR_MAIN_IMAGE_PATH = getLastImagePath(PmiMap.this);
                                IS_DTR_AI_IMAGE_VERIFIED = true;
                                dtr_capture_Image_layout.setVisibility(View.GONE);
                                dtr_taken_image_layout.setVisibility(View.VISIBLE);
                                if (object.has("status") && !object.get("status").getAsString().equalsIgnoreCase("no_plinth_detected")) {
                                    dtrimgPreview.setImageBitmap(bitmap);
                                }

                                //dtrai_pole.setText("Normal");
                                dtrai_pole_layout.setVisibility(View.GONE);
                                openAlertDialogRetryDTR("Plinth not detected in image, Capture again");
                            } else {
                                if (object.has("status") && object.get("status").getAsString().equalsIgnoreCase("no_poles")) {
                                    String reason = "No poles detected";
                                    Toast.makeText(PmiMap.this, reason, Toast.LENGTH_SHORT).show();
                                    showDetectionDialog(reason, bitmap);
                                } else {
                                    if (object.has("reason") && !object.get("reason").isJsonNull()) {
                                        String reason = object.has("reason") ? object.get("reason").getAsString() : "Unknown error";
                                        // String reason = "Unknown error";

                                        if (object.has("reason") && !object.get("reason").isJsonNull()) {
                                            reason = object.get("reason").getAsString();
                                        }

                                        Toast.makeText(PmiMap.this, reason, Toast.LENGTH_SHORT).show();
                                        showDetectionDialog(reason, bitmap);
                                    } else {
                                        String reason = "Unknown error";
                                        Toast.makeText(PmiMap.this, reason, Toast.LENGTH_SHORT).show();
                                        showDetectionDialog(reason, bitmap);
                                    }
                                }
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //if (progressDialog != null) progressDialog.dismiss();
                        aiDialog.dismiss();
                        Toast.makeText(PmiMap.this, "Upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                // if (progressDialog != null) progressDialog.dismiss();
                aiDialog.dismiss();
                Toast.makeText(this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    //New Pmi changes
    private void uploadMainImageToServer(Bitmap bitmap) {

        {
            try {
                String MAIN_IMAGE_PATH_TEMP = getLastImagePath(this);
                if (MAIN_IMAGE_PATH_TEMP == null || MAIN_IMAGE_PATH_TEMP.isEmpty()) {
                    aiDialog.dismiss();
                    Toast.makeText(this, "Invalid image path", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if (progressDialog != null) progressDialog.show();

                File file = new File(MAIN_IMAGE_PATH_TEMP);
                if (!file.exists()) {
                    Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
                    //if (progressDialog != null) progressDialog.dismiss();
                    aiDialog.dismiss();
                    return;
                }

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                ImageRetrofitService service = ImageRetrofitClient.getRetrofitInstance().create(ImageRetrofitService.class);
                Call<JsonObject> call = service.uploadImage(imagePart, false);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //if (progressDialog != null) progressDialog.dismiss();
                        aiDialog.dismiss();
                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(PmiMap.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JsonObject object = response.body();
                        if (object.has("status") && (object.get("status").getAsString().equalsIgnoreCase("done") || object.get("status").getAsString().equalsIgnoreCase("done_with_visibility_warning"))) {
                            if (object.get("status").getAsString().equalsIgnoreCase("done")) {
                                MAIN_IMAGE_PATH = getLastImagePath(PmiMap.this);
                                handleSuccessfulUploadForMainImage(object, bitmap);
                            } else {
                                warning++;
                                if (warning == 2) {
                                    MAIN_IMAGE_PATH = getLastImagePath(PmiMap.this);
                                    handleSuccessfulUploadForMainImage(object, bitmap);
                                } else {
                                    openAlertDialogRetry("Pole Base not visible, Capture again");
                                    //openAlertDialog("Pole Base not visible, Capture again");
                                    //Toast.makeText(PmiMap.this, "Pole not visible, Capture again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            if (object.has("status") && object.get("status").getAsString().equalsIgnoreCase("no_poles")) {
                                String reason = "No poles detected";
                                Toast.makeText(PmiMap.this, reason, Toast.LENGTH_SHORT).show();
                                showDetectionDialog(reason, bitmap);
                            } else {
                                if (object.has("reason") && !object.get("reason").isJsonNull()) {
                                    String reason = object.has("reason") ? object.get("reason").getAsString() : "Unknown error";
                                    // String reason = "Unknown error";

                                    if (object.has("reason") && !object.get("reason").isJsonNull()) {
                                        reason = object.get("reason").getAsString();
                                    }

                                    Toast.makeText(PmiMap.this, reason, Toast.LENGTH_SHORT).show();
                                    showDetectionDialog(reason, bitmap);
                                } else {
                                    String reason = "Unknown error";
                                    Toast.makeText(PmiMap.this, reason, Toast.LENGTH_SHORT).show();
                                    showDetectionDialog(reason, bitmap);
                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //if (progressDialog != null) progressDialog.dismiss();
                        aiDialog.dismiss();
                        Toast.makeText(PmiMap.this, "Upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                // if (progressDialog != null) progressDialog.dismiss();
                aiDialog.dismiss();
                Toast.makeText(this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    //New Pmi changes
    private void handleSuccessfulUploadForMainImageDTR(JsonObject object, Bitmap bitmap) {
        ArrayList<String> resultList = new ArrayList<>();
        aiListMainDTR.clear();
        StringBuilder resultSummary = new StringBuilder();

        if (object.has("pole_lean_detections")) {
            JsonArray poles = object.getAsJsonArray("pole_lean_detections");
            for (JsonElement element : poles) {
                JsonObject detection = element.getAsJsonObject();
                AIResponseModel ai = new AIResponseModel();

                resultSummary.append(detection.get("class_name").getAsString()).append("\n");
                resultList.add(detection.get("class_name").getAsString());
                ai.setAI_RESPONSE(detection.get("class_name").getAsString());
                ai.setLEAN_ANGLE(String.valueOf(detection.get("angle_deg").getAsDouble()));
                ai.setPOSITION("pole_0");
                ai.setPOSTITION_XARM(detection.has("arm_id") ? detection.get("arm_id").getAsString() : "");
                ai.setVOLTAGE("");
                ai.setIMAGE("D");

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

                aiListMainDTR.add(ai);
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
                        ai.setLEAN_ANGLE(String.valueOf(detection.get("angle_deg").getAsDouble()));
                        // Extract label class
                        String labelClass = "";
                        if (detection.has("labels")) {
                            JsonArray labels = detection.getAsJsonArray("labels");
                            if (labels.size() > 0) {
                                JsonObject labelObj = labels.get(0).getAsJsonObject();
                                if (labelObj.has("class")) {
                                    labelClass = labelObj.get("class").getAsString();
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
                        ai.setLEAN_ANGLE(String.valueOf(detection.get("angle_deg").getAsDouble()));
                        ai.setPOSITION(crop.has("pole_id") ? crop.get("pole_id").getAsString() : "pole_0");
                        ai.setVOLTAGE(voltage);
                        ai.setPOSTITION_XARM(detection.has("arm_id") ? detection.get("arm_id").getAsString() : "");
                        ai.setIMAGE("D");
                        aiListMainDTR.add(ai);

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
            ai.setVOLTAGE("11KV");
            ai.setIMAGE("D");
            ai.setPOLE_RATIO(clearance.has("pole_height_px") ? String.valueOf(clearance.get("pole_height_px").getAsDouble()) : "-1");
            ai.setPLINTH_RATIO(clearance.has("plinth_height_px") ? String.valueOf(clearance.get("plinth_height_px").getAsDouble()) : "-1");
            aiListMainDTR.add(ai);
            resultSummary.append(clearance.get("status").getAsString()).append("\n");
            resultList.add(clearance.get("status").getAsString());
            System.out.println("PLINTH_HEIGHT_PX------>" + clearance.get("plinth_height_px").getAsDouble());
        }

        if (object.has("vegetation_clearance")) {
            JsonObject voltageObj = object.getAsJsonObject("vegetation_clearance");
            AIResponseModel ai = new AIResponseModel();
            String reason = voltageObj.get("reason").getAsString();
            resultSummary.append(voltageObj.get("reason").getAsString()).append("\n");
            resultList.add(voltageObj.get("reason").getAsString() + " (" + voltageObj.get("risk").getAsString() + ")");

            if (reason.equalsIgnoreCase("conductor not detected")) {
                warning_1++;

                if (warning_1 == 2) {
                    // Continue processing on the second attempt
                } else {
                    openAlertDialogRetry("Capture Image Again");
                    return;
                }
            }

            ai.setAI_RESPONSE(voltageObj.get("reason").getAsString());
            ai.setIMAGE("D");
            ai.setVD_DEPTH(voltageObj.has("decision_depth") ? voltageObj.get("decision_depth").isJsonNull() ? "" : voltageObj.get("decision_depth").getAsString() : "");
            ai.setVC_REASON(voltageObj.has("reason") ? voltageObj.get("reason").isJsonNull() ? "" : voltageObj.get("reason").getAsString() : "");
            aiListMainDTR.add(ai);
        }

        dtr_capture_Image_layout.setVisibility(View.GONE);
        dtr_taken_image_layout.setVisibility(View.VISIBLE);
        dtrimgPreview.setImageBitmap(bitmap);


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
                    dtrai_pole.setText("Straight");
                } else if (item.contains("Leaned")) {
                    dtrai_pole.setText("Leaned");
                } else {
                    dtrai_pole.setText(item);
                }
            }

            // Cross Arm
            else if (item.contains("CrossArm")) {
                hasCrossArm = true;
                matched = true;

                if (item.contains("Straight")) {
                    dtrai_cross_arm.setText("Straight");
                } else if (item.contains("Tilted")) {
                    dtrai_cross_arm.setText("Tilted");
                } else {
                    dtrai_cross_arm.setText(item);
                }
            }

            // Top Cleat
            else if (item.contains("TopCleat")) {
                hasTopCleat = true;
                matched = true;

                if (item.contains("Straight")) {
                    dtrai_top_cleat.setText("Straight");
                } else if (item.contains("Tilted")) {
                    dtrai_top_cleat.setText("Tilted");
                } else {
                    dtrai_top_cleat.setText(item);
                }
            }

            // Others
            if (!matched) {
                otherItems.add(item);
            }
        }


        dtrai_pole_layout.setVisibility(hasPole ? View.VISIBLE : View.GONE);
        dtrai_cross_arm_layout.setVisibility(hasCrossArm ? View.VISIBLE : View.GONE);
        dtrai_top_cleat_layout.setVisibility(hasTopCleat ? View.VISIBLE : View.GONE);


        dtrview_cross_arm.setVisibility(hasCrossArm ? View.VISIBLE : View.GONE);
        dtrview_top_cleat.setVisibility(hasTopCleat ? View.VISIBLE : View.GONE);

        if (!otherItems.isEmpty()) {
            dtrai_others_layout.setVisibility(View.VISIBLE);
            dtrview_others.setVisibility(View.GONE);
            dtrai_others.setText(TextUtils.join(", ", otherItems).replace("_", " "));
        } else {
            dtrai_others_layout.setVisibility(View.GONE);
            dtrview_others.setVisibility(View.GONE);
        }


    }

    //New Pmi changes
    private void handleSuccessfulUploadForMainImage(JsonObject object, Bitmap bitmap) {
        ArrayList<String> resultList = new ArrayList<>();
        aiListMain.clear();
        StringBuilder resultSummary = new StringBuilder();

        if (object.has("pole_lean_detections")) {
            JsonArray poles = object.getAsJsonArray("pole_lean_detections");
            for (JsonElement element : poles) {
                JsonObject detection = element.getAsJsonObject();
                AIResponseModel ai = new AIResponseModel();

                resultSummary.append(detection.get("class_name").getAsString()).append("\n");
                resultList.add(detection.get("class_name").getAsString());
                ai.setAI_RESPONSE(detection.get("class_name").getAsString());
                ai.setLEAN_ANGLE(detection.has("angle_deg") && !detection.get("angle_deg").isJsonNull() ? detection.get("angle_deg").getAsString() : "");
                ai.setPOSITION("pole_0");
                ai.setPOSTITION_XARM(detection.has("arm_id") ? detection.get("arm_id").getAsString() : "");
                ai.setVOLTAGE("");
                ai.setIMAGE("P");

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

                aiListMain.add(ai);
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
                        ai.setLEAN_ANGLE(detection.has("angle_deg") && !detection.get("angle_deg").isJsonNull() ? detection.get("angle_deg").getAsString() : "");
                        ai.setPOSITION(crop.has("pole_id") ? crop.get("pole_id").getAsString() : "pole_0");
                        ai.setVOLTAGE(voltage);
                        ai.setPOSTITION_XARM(detection.has("arm_id") ? detection.get("arm_id").getAsString() : "");
                        ai.setIMAGE("P");
                        aiListMain.add(ai);

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
            ai.setVOLTAGE("11KV");
            ai.setIMAGE("P");
            ai.setPOLE_RATIO(clearance.has("pole_height_px") ? String.valueOf(clearance.get("pole_height_px").getAsDouble()) : "-1");
            ai.setPLINTH_RATIO(clearance.has("plinth_height_px") ? String.valueOf(clearance.get("plinth_height_px").getAsDouble()) : "-1");
            aiListMain.add(ai);
            resultSummary.append(clearance.get("status").getAsString()).append("\n");
            resultList.add(clearance.get("status").getAsString());
            System.out.println("PLINTH_HEIGHT_PX------>" + clearance.get("plinth_height_px").getAsDouble());
        }

        if (object.has("vegetation_clearance")) {
            JsonObject voltageObj = object.getAsJsonObject("vegetation_clearance");
            AIResponseModel ai = new AIResponseModel();
            String reason = voltageObj.get("reason").getAsString();
            resultSummary.append(voltageObj.get("reason").getAsString()).append("\n");
            resultList.add(voltageObj.get("reason").getAsString() + " (" + voltageObj.get("risk").getAsString() + ")");

            if (reason.equalsIgnoreCase("conductor not detected")) {
                warning_1++;

                if (warning_1 == 2) {
                    // Continue processing on the second attempt
                } else {
                    openAlertDialogRetry("Capture Image Again");
                    return;
                }
            }
            ai.setAI_RESPONSE(voltageObj.get("reason").getAsString());
            ai.setIMAGE("P");
            ai.setVD_DEPTH(voltageObj.has("decision_depth") ? voltageObj.get("decision_depth").isJsonNull() ? "" : voltageObj.get("decision_depth").getAsString() : "");
            ai.setVC_REASON(voltageObj.has("reason") ? voltageObj.get("reason").isJsonNull() ? "" : voltageObj.get("reason").getAsString() : "");
            aiListMain.add(ai);
        }

        capture_Image_layout.setVisibility(View.GONE);
        taken_image_layout.setVisibility(View.VISIBLE);
        layout_question.setVisibility(View.VISIBLE);

        all_defects_layout.setVisibility(View.GONE);
        imgPreview.setImageBitmap(bitmap);
        ArrayList<DefectGroupModel> tempList = new ArrayList<>();
        if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
            tempList.addAll(PMINewHome.defectsGroupListLT);
        }
        if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
            for (DefectGroupModel group : PMINewHome.defectsGroupList11) {

                if ("DTR related".equalsIgnoreCase(group.getDefectGroup())) {
                    if ("DTR Pole".equalsIgnoreCase(SELECTED_POLE_CONFIG)) {
                        tempList.add(group);
                    }

                } else {
                    tempList.add(group);
                }
            }
        }
        if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
            tempList.addAll(PMINewHome.defectsGroupList33);
        }
        defectGroupAdapter = new DefectGroupAdapter(this, tempList, PmiMap.this);
        rvDefectGroups.setLayoutManager(new LinearLayoutManager(this));
        rvDefectGroups.setAdapter(defectGroupAdapter);

        boolean hasPole = false;
        boolean hasCrossArm = false;
        boolean hasTopCleat = false;

        ArrayList<String> otherItems = new ArrayList<>();

        for (String item : resultList) {

            boolean matched = false;

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
            } else if (item.contains("CrossArm")) {
                hasCrossArm = true;
                matched = true;

                if (item.contains("Straight")) {
                    ai_cross_arm.setText("Straight");
                } else if (item.contains("Tilted")) {
                    ai_cross_arm.setText("Tilted");
                } else {
                    ai_cross_arm.setText(item);
                }
            } else if (item.contains("TopCleat")) {
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


    }

    //New Pmi changes
    private void validateAndSubmit() {
//        if (IS_DTR_POLE && !IS_DTR_AI_IMAGE_VERIFIED) {
//            Toast.makeText(this, "Please upload DTR Image", Toast.LENGTH_SHORT).show();
//            return;
//        }
        Log.d("SELECTED_DEFECTS", new Gson().toJson(selectedChipDefects));

        if (!validateSelectedDefectGroups()) {
            return;
        }

        if ("RECT".equalsIgnoreCase(FROM) &&
                (mainImageBitmapPMI == null || mainImageBitmapPMI.isRecycled())) {

            Toast.makeText(this, "Please Capture Image", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<DefectsModel> selectedList = new ArrayList<>();

        for (DefectsModel defect : selectedChipDefects) {
            if ("Y".equalsIgnoreCase(defect.getPHOTO()) && "N".equalsIgnoreCase(defect.getAI())) {

                if ("LT".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                    for (int i = 0; i < PMINewHome.defectsGroupListLT.size(); i++) {
                        if (PMINewHome.defectsGroupListLT.get(i).getDefectGroup().equalsIgnoreCase(defect.getDEFECT_GROUP())) {
                            if (!PMINewHome.defectsGroupListLT.get(i).isImageSelected()) {
                                Toast.makeText(this, "Please capture image for " + defect.getDEFECT_GROUP(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }

                if ("11 KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                    for (int i = 0; i < PMINewHome.defectsGroupList11.size(); i++) {
                        if (PMINewHome.defectsGroupList11.get(i).getDefectGroup().equalsIgnoreCase(defect.getDEFECT_GROUP())) {
                            if (!PMINewHome.defectsGroupList11.get(i).isImageSelected()) {
                                Toast.makeText(this, "Please capture image for " + defect.getDEFECT_GROUP(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }

                if ("33KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                    for (int i = 0; i < PMINewHome.defectsGroupList33.size(); i++) {
                        if (PMINewHome.defectsGroupList33.get(i).getDefectGroup().equalsIgnoreCase(defect.getDEFECT_GROUP())) {
                            if (!PMINewHome.defectsGroupList33.get(i).isImageSelected()) {
                                Toast.makeText(this, "Please capture image for " + defect.getDEFECT_GROUP(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }


            }

            selectedList.add(defect);
        }

        for (int j = 0; j < selectedChipDefects.size(); j++) {
            DefectsModel defect = selectedChipDefects.get(j);
            if ("LT".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                for (int i = 0; i < PMINewHome.defectsGroupListLT.size(); i++) {
                    if (PMINewHome.defectsGroupListLT.get(i).getDefectGroup().equalsIgnoreCase(defect.getDEFECT_GROUP())) {
                        if (PMINewHome.defectsGroupListLT.get(i).isImageSelected()) {
                            selectedChipDefects.get(j).setImagePath(PMINewHome.defectsGroupListLT.get(i).getImage());
                        }
                    }
                }
            }
            if ("11 KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                for (int i = 0; i < PMINewHome.defectsGroupList11.size(); i++) {
                    if (PMINewHome.defectsGroupList11.get(i).getDefectGroup().equalsIgnoreCase(defect.getDEFECT_GROUP())) {
                        if (PMINewHome.defectsGroupList11.get(i).isImageSelected()) {
                            selectedChipDefects.get(j).setImagePath(PMINewHome.defectsGroupList11.get(i).getImage());
                        }
                    }
                }
            }
            if ("33KV".equalsIgnoreCase(SELECTED_VOLTAGE)) {
                for (int i = 0; i < PMINewHome.defectsGroupList33.size(); i++) {
                    if (PMINewHome.defectsGroupList33.get(i).getDefectGroup().equalsIgnoreCase(defect.getDEFECT_GROUP())) {
                        if (PMINewHome.defectsGroupList33.get(i).isImageSelected()) {
                            selectedChipDefects.get(j).setImagePath(PMINewHome.defectsGroupList33.get(i).getImage());
                        }
                    }
                }
            }
        }

        Log.d("SELECTED_DEFECTS_FIL", new Gson().toJson(selectedList));

        // Toast.makeText(this, "Total selected: " + selectedList.size(), Toast.LENGTH_SHORT).show();


        if (isDefective) {
            boolean hasDTRpole = false;
            if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV") && IS_DTR_POLE) {
                for (DefectsModel defect : selectedChipDefects) {
                    if (defect.getDEFECT_GROUP().equalsIgnoreCase("DTR related")) {
                        hasDTRpole = true;
                        break;
                    }
                }
            }
            if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV") && IS_DTR_POLE && !hasDTRpole) {
                PostingModel postingModel = new PostingModel();
                postingModel.setID(Long.toString(createRandomUID()));
                postingModel.setDEFECT_SUB_TYPE_ID("141");
                postingModel.setDEFECT_ID("141");
                postingModel.setDEFECT_TYPE("DTR Mounting / Structure Defects");
                postingModel.setDEFECT_SUB_TYPE("PLINTH HEIGHT NOT SUFFICIENT");
                postingModel.setDEFECT_BY_USER("N");
                postingModel.setVOLTAGE_LEVEL(SELECTED_VOLTAGE);
                postingModel.setPOLE_NO(CURRENT_SELECTED_POLE_NUMBER);
                postingModel.setPOLE_ID(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? UUID.randomUUID().toString() : CURRENT_SELECTED_POLE_ID);
                postingModel.setFEEDER_ID(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? "" : ID);
                postingModel.setDTR_CODE(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? NAME : "");
                postingModel.setLT_CKT_CODE(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? ID : "");
                postingModel.setDTR_IMAGE(DTR_MAIN_IMAGE_PATH);
                postingModel.setIMAGE(MAIN_IMAGE_PATH);
                postingModel.setAiResponseModel(aiListMainDTR);
                postingModel.setPOLE_CONFIG(SELECTED_POLE_CONFIG);
                postingModel.setCYCLONE(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? "NOT SURVEYED" : "");
                if (ConnectivityChecking.checkConnection(PmiMap.this)) {
                    postingModel.setIMAGE_TYPE("ONLINE");
                } else {
                    postingModel.setIMAGE_TYPE("OFFLINE");
                }

                postingModel.setCURRENT_LONG(String.valueOf(gpsTraker.getLongitude()));
                postingModel.setCURRENT_LAT(String.valueOf(gpsTraker.getLatitude()));

                postingModel.setPOLE_LAT(String.valueOf(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? CYCLONE_POLE_LAT : ""));
                postingModel.setPOLE_LONG(String.valueOf(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? CYCLONE_POLE_LONG : ""));

                postingModel.setDEFECT_GROUP("DTR related");

                postingModelList.add(postingModel);
            }

            for (DefectsModel defect : selectedChipDefects) {
                // Safety: don't add Non defective here
                if ("Non defective".equalsIgnoreCase(defect.getDEFECT_GROUP())) {
                    continue;
                }
                PostingModel postingModel = new PostingModel();
                postingModel.setID(Long.toString(createRandomUID()));
                postingModel.setDEFECT_SUB_TYPE_ID(defect.getDEFECT_SUB_TYPE_ID());
                postingModel.setDEFECT_ID(defect.getDEFECT_ID());
                postingModel.setDEFECT_TYPE(defect.getDEFECT_TYPE());
                postingModel.setDEFECT_SUB_TYPE(defect.getDEFECT_SUB_TYPE());
                postingModel.setVOLTAGE_LEVEL(SELECTED_VOLTAGE);
                postingModel.setPOLE_NO(CURRENT_SELECTED_POLE_NUMBER);
                postingModel.setPOLE_ID(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? UUID.randomUUID().toString() : CURRENT_SELECTED_POLE_ID);
                postingModel.setFEEDER_ID(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? "" : ID);
                postingModel.setDTR_CODE(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? NAME : "");
                postingModel.setLT_CKT_CODE(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? ID : "");
                postingModel.setPOLE_CONFIG(SELECTED_POLE_CONFIG);
                postingModel.setPOLE_LAT(String.valueOf(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? CYCLONE_POLE_LAT : ""));
                postingModel.setPOLE_LONG(String.valueOf(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? CYCLONE_POLE_LONG : ""));
                postingModel.setCYCLONE(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? "NOT SURVEYED" : "");
                if ("Y".equalsIgnoreCase(defect.getAI())) {
                    postingModel.setAI_FLAG("AI");
                } else {
                    postingModel.setAI_FLAG("NON AI");
                }
                postingModel.setGRP_ID(defect.getGRP_ID());

                if ("Y".equalsIgnoreCase(defect.getPHOTO()) &&
                        "N".equalsIgnoreCase(defect.getAI())) {
                    postingModel.setIMAGE(defect.getImagePath());
                    if ((defect.getDEFECT_GROUP().equalsIgnoreCase("DTR related") &&
                            defect.getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV") && IS_DTR_POLE)) {
                        postingModel.setAiResponseModel(aiListMainDTR);
                        postingModel.setAiResponseModel(aiListMain);
                        postingModel.setAI_IMAGE(MAIN_IMAGE_PATH);
                    } else {
                        postingModel.setAI_IMAGE(MAIN_IMAGE_PATH);
                        postingModel.setAiResponseModel(aiListMain);
                    }
                } else {
                    if (defect.getDEFECT_GROUP().equalsIgnoreCase("DTR related") &&
                            defect.getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV") && IS_DTR_POLE) {
                        postingModel.setDTR_IMAGE(DTR_MAIN_IMAGE_PATH);
                        postingModel.setAiResponseModel(aiListMainDTR);
                        postingModel.setDEFECT_BY_USER("Y");
                    } else {
                        postingModel.setIMAGE(MAIN_IMAGE_PATH);
                        postingModel.setAI_IMAGE(MAIN_IMAGE_PATH);
                        postingModel.setAiResponseModel(aiListMain);
                    }

                }

                if (ConnectivityChecking.checkConnection(PmiMap.this)) {
                    postingModel.setIMAGE_TYPE("ONLINE");
                } else {
                    postingModel.setIMAGE_TYPE("OFFLINE");
                }

                postingModel.setCURRENT_LONG(String.valueOf(gpsTraker.getLongitude()));
                postingModel.setCURRENT_LAT(String.valueOf(gpsTraker.getLatitude()));

                postingModel.setPOLE_LAT(String.valueOf(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? CYCLONE_POLE_LAT : ""));
                postingModel.setPOLE_LONG(String.valueOf(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? CYCLONE_POLE_LONG : ""));

                postingModel.setDEFECT_GROUP(defect.getDEFECT_GROUP());

                postingModelList.add(postingModel);
            }
        } else {
            for (DefectsModel defect : selectedChipDefects) {

                // Only process Non defective model
                if (!"Non defective".equalsIgnoreCase(defect.getDEFECT_GROUP())) {
                    continue;
                }

                PostingModel postingModel = new PostingModel();

                postingModel.setID(Long.toString(createRandomUID()));

                // Take everything from DefectsModel
                postingModel.setDEFECT_SUB_TYPE_ID(defect.getDEFECT_SUB_TYPE_ID());

                postingModel.setDEFECT_ID(defect.getDEFECT_ID());

                postingModel.setDEFECT_TYPE(defect.getDEFECT_TYPE());

                postingModel.setDEFECT_SUB_TYPE(defect.getDEFECT_SUB_TYPE());

                postingModel.setVOLTAGE_LEVEL(SELECTED_VOLTAGE);

                postingModel.setPOLE_NO(CURRENT_SELECTED_POLE_NUMBER);

                // postingModel.setPOLE_ID(CURRENT_SELECTED_POLE_ID);
                postingModel.setPOLE_ID(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? UUID.randomUUID().toString() : CURRENT_SELECTED_POLE_ID);


                postingModel.setFEEDER_ID(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? "" : ID);

                postingModel.setDTR_CODE(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? NAME : "");

                postingModel.setLT_CKT_CODE(SELECTED_VOLTAGE.equalsIgnoreCase("LT") ? ID : "");

                // Image from model
                if ("Y".equalsIgnoreCase(defect.getPHOTO())) {
                    postingModel.setIMAGE(defect.getImagePath());
                }

                postingModel.setDEFECT_BY_USER("Y");

                postingModel.setIMAGE_TYPE(
                        ConnectivityChecking.checkConnection(PmiMap.this)
                                ? "ONLINE"
                                : "OFFLINE"
                );

                postingModel.setCURRENT_LONG(
                        String.valueOf(gpsTraker.getLongitude())
                );

                postingModel.setCURRENT_LAT(
                        String.valueOf(gpsTraker.getLatitude())
                );

                postingModel.setPOLE_LAT(String.valueOf(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? CYCLONE_POLE_LAT : ""));
                postingModel.setPOLE_LONG(String.valueOf(CURRENT_SELECTED_POLE_NUMBER.startsWith("C") ? CYCLONE_POLE_LONG : ""));

                // From model
                postingModel.setDEFECT_GROUP(
                        defect.getDEFECT_GROUP()
                );

                if (defect.getDEFECT_GROUP().equalsIgnoreCase("DTR related") && defect.getVOLTAGE_LEVEL().equalsIgnoreCase("11 KV") && IS_DTR_POLE) {
                    postingModel.setDTR_IMAGE(DTR_MAIN_IMAGE_PATH);
                    postingModel.setAiResponseModel(aiListMainDTR);
                    postingModel.setDEFECT_BY_USER("Y");
                } else {
                    postingModel.setIMAGE(MAIN_IMAGE_PATH);
                    postingModel.setAiResponseModel(aiListMain);
                }

                postingModelList.add(postingModel);
            }
        }


        Log.d("SELECTED_POSTING", new Gson().toJson(postingModelList));

        if (ConnectivityChecking.checkConnection(PmiMap.this)) {
            getDmsTokenApi(submitButton);
            // transmitPolesNew(postingModelList);
        } else {
            PmiDatabase pmiDatabase = new PmiDatabase(PmiMap.this);
            for (int i = 0; i < postingModelList.size(); i++) {
                pmiDatabase.insertOrUpdatePmiPosting(postingModelList.get(i));
            }
            IS_DTR_POLE = false;
            DTR_MAIN_IMAGE_PATH = "";
            SELECTED_GROUP_NAME = "";
            IS_DTR_AI_IMAGE_VERIFIED = false;
            selectedDefect = null;
            postingModelList.clear();
            newDefectDialog.dismiss();
            selectedChipDefects.clear();
            for (DefectsModel defect : PMINewHome.allDefectsList) {
                defect.setSelected(false);
                defect.setImageSelected(false);
                defect.setImagePath("");
            }
            if (defectGroupAdapter != null) {
                defectGroupAdapter.notifyDataSetChanged();
            }

            if (defectTypeAdapter != null) {
                defectTypeAdapter.notifyDataSetChanged();
            }

            for (DefectGroupModel group : PMINewHome.defectsGroupListLT) {
                group.setExpanded(false);
                group.setImageSelected(false);
                group.setImage("");
            }
            for (DefectGroupModel group : PMINewHome.defectsGroupList11) {
                group.setExpanded(false);
                group.setImageSelected(false);
                group.setImage("");
            }
            for (DefectGroupModel group : PMINewHome.defectsGroupList33) {
                group.setExpanded(false);
                group.setImageSelected(false);
                group.setImage("");
            }


            Toast.makeText(PmiMap.this, "Damage Saved successfully", Toast.LENGTH_LONG).show();
        }

    }

    private void getDmsTokenApi(TextView submitButton) {
        openTransmitBottomSheet(true);
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getDmsToken();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                openTransmitBottomSheet(false);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                        String accessToken = surveyedResponseVo.get("DATA").getAsString();
                        setOfflineImagesData(accessToken, postingModelList, submitButton);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void setOfflineImagesData(
            String accessToken,
            List<PostingModel> transmitList,
            TextView submitButton) {
        openTransmitBottomSheet(true);

        totalImagesToUpload = 0;
        completedImages = 0;
        transmissionStarted = false;

        uploadedImageMap.clear();

        for (PostingModel model : transmitList) {

            if (!TextUtils.isEmpty(model.getIMAGE())) {
                totalImagesToUpload++;
            }

            if (IS_DTR_POLE &&
                    !TextUtils.isEmpty(model.getDTR_IMAGE())) {
                totalImagesToUpload++;
            }
        }

        Log.d(
                "IMAGE_UPLOAD",
                "TOTAL UPLOADS = " + totalImagesToUpload
        );

        if (totalImagesToUpload == 0) {
            transmitPolesNew(transmitList);
            return;
        }

        for (PostingModel model : transmitList) {

            Log.d(
                    "BEFORE_IMAGE_UPLOAD",
                    "GROUP = " + model.getDEFECT_GROUP()
                            + " | TYPE = " + model.getDEFECT_TYPE()
                            + " | SUBTYPE = " + model.getDEFECT_SUB_TYPE()
                            + " | IMAGE = " + model.getIMAGE()
                            + " | DTR_IMAGE = " + model.getDTR_IMAGE()
            );
        }

// Call only ONCE, outside the loop
        callImageSavingAPI(
                postingModelList,
                accessToken,
                submitButton
        );
    }

    public void callImageSavingAPI(
            ArrayList<PostingModel> postingModelList,
            String accessToken,
            TextView submitButton) {

        try {

            openTransmitBottomSheet(true);


            Map<String, ArrayList<PostingModel>> imageModelMap =
                    new LinkedHashMap<>();


            Set<String> dtrImagePaths =
                    new HashSet<>();


            for (PostingModel model : postingModelList) {

                if (model == null) {
                    continue;
                }


                String imagePath = model.getIMAGE();

                if (!TextUtils.isEmpty(imagePath)) {

                    ArrayList<PostingModel> models =
                            imageModelMap.get(imagePath);

                    if (models == null) {

                        models = new ArrayList<>();

                        imageModelMap.put(
                                imagePath,
                                models
                        );
                    }

                    /*
                     * Avoid adding same model twice
                     */
                    if (!models.contains(model)) {

                        models.add(model);
                    }

                    Log.d(
                            "DEFECT_IMAGE_MAP",
                            model.getDEFECT_GROUP()
                                    + " | "
                                    + model.getDEFECT_SUB_TYPE()
                                    + " → "
                                    + imagePath
                    );
                }


                String aiImagePath = model.getAI_IMAGE();

                if (!TextUtils.isEmpty(aiImagePath)) {

                    ArrayList<PostingModel> models =
                            imageModelMap.get(aiImagePath);

                    if (models == null) {

                        models = new ArrayList<>();

                        imageModelMap.put(
                                aiImagePath,
                                models
                        );
                    }

                    /*
                     * Avoid adding same model twice
                     */
                    if (!models.contains(model)) {

                        models.add(model);
                    }

                    Log.d(
                            "AI_IMAGE---->",
                            model.getDEFECT_GROUP()
                                    + " | "
                                    + model.getDEFECT_SUB_TYPE()
                                    + " → "
                                    + aiImagePath
                    );
                }


                String dtrImagePath =
                        model.getDTR_IMAGE();

                if (IS_DTR_POLE
                        && !TextUtils.isEmpty(dtrImagePath)) {

                    ArrayList<PostingModel> models =
                            imageModelMap.get(dtrImagePath);

                    if (models == null) {

                        models = new ArrayList<>();

                        imageModelMap.put(
                                dtrImagePath,
                                models
                        );
                    }

                    if (!models.contains(model)) {

                        models.add(model);
                    }


                    dtrImagePaths.add(dtrImagePath);

                    Log.d(
                            "DTR_IMAGE_MAP",
                            model.getDEFECT_GROUP()
                                    + " | "
                                    + model.getDEFECT_SUB_TYPE()
                                    + " → "
                                    + dtrImagePath
                    );
                }
            }


            Log.d(
                    "IMAGE_UPLOAD",
                    "========================================"
            );

            Log.d(
                    "IMAGE_UPLOAD",
                    "TOTAL MODELS = "
                            + postingModelList.size()
            );

            Log.d(
                    "IMAGE_UPLOAD",
                    "UNIQUE IMAGES = "
                            + imageModelMap.size()
            );

            for (Map.Entry<String, ArrayList<PostingModel>> entry
                    : imageModelMap.entrySet()) {

                Log.d(
                        "IMAGE_UPLOAD",
                        "IMAGE = "
                                + entry.getKey()
                                + " | MODELS = "
                                + entry.getValue().size()
                );

                for (PostingModel model : entry.getValue()) {

                    Log.d(
                            "IMAGE_UPLOAD",
                            "    "
                                    + model.getDEFECT_GROUP()
                                    + " / "
                                    + model.getDEFECT_SUB_TYPE()
                    );
                }
            }

            Log.d(
                    "IMAGE_UPLOAD",
                    "========================================"
            );


            totalImagesToUpload =
                    imageModelMap.size();

            completedImages = 0;


            if (totalImagesToUpload == 0) {

                Log.d(
                        "IMAGE_UPLOAD",
                        "No images to upload"
                );

                imageUploadCompleted();
                return;
            }


            for (Map.Entry<String, ArrayList<PostingModel>> entry
                    : imageModelMap.entrySet()) {

                final String imagePath =
                        entry.getKey();

                final ArrayList<PostingModel> models =
                        entry.getValue();

                final boolean isDtrImage =
                        dtrImagePaths.contains(imagePath);


                Log.d(
                        "IMAGE_UPLOAD",
                        "STARTING UPLOAD"
                                + " | DTR = "
                                + isDtrImage
                                + " | PATH = "
                                + imagePath
                );


                File file =
                        getFileFromUri(
                                PmiMap.this,
                                imagePath
                        );


                if (file == null || !file.exists()) {

                    Log.e(
                            "IMAGE_UPLOAD",
                            "FILE NOT FOUND = "
                                    + imagePath
                    );

                    markImageUploadCompleted();

                    continue;
                }


                RequestBody fileRequestBody =
                        RequestBody.create(
                                MediaType.parse("image/*"),
                                file
                        );


                MultipartBody.Part imagePart =
                        MultipartBody.Part.createFormData(
                                "upload_file",
                                file.getName(),
                                fileRequestBody
                        );


                RequestBody tokenBody =
                        RequestBody.create(
                                MultipartBody.FORM,
                                accessToken
                        );


                RequestBody accountBody =
                        RequestBody.create(
                                MultipartBody.FORM,
                                "2"
                        );


                RequestBody folderBody =
                        RequestBody.create(
                                MultipartBody.FORM,
                                "okm:root/EPCCBDOCREP/GIS_11KV/PMI"
                        );


                ImageAPIServiceMeter apiService =
                        ApiClient.getClient()
                                .create(ImageAPIServiceMeter.class);


                apiService.uploadImage(
                        tokenBody,
                        accountBody,
                        folderBody,
                        imagePart
                ).enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(
                            Call<JsonObject> call,
                            Response<JsonObject> response) {

                        try {

                            if (!response.isSuccessful()) {

                                Log.e(
                                        "IMAGE_UPLOAD",
                                        "HTTP ERROR = "
                                                + response.code()
                                                + " | "
                                                + imagePath
                                );

                                return;
                            }


                            if (response.body() == null) {

                                Log.e(
                                        "IMAGE_UPLOAD",
                                        "EMPTY RESPONSE = "
                                                + imagePath
                                );

                                return;
                            }


                            JsonObject responseObject =
                                    response.body();


                            Log.d(
                                    "IMAGE_UPLOAD_RESPONSE",
                                    imagePath
                                            + " → "
                                            + responseObject
                            );


                            if (!responseObject.has("data")
                                    || responseObject
                                    .get("data")
                                    .isJsonNull()) {

                                Log.e(
                                        "IMAGE_UPLOAD",
                                        "DATA NOT FOUND = "
                                                + imagePath
                                );

                                return;
                            }


                            JsonArray jsonArray =
                                    responseObject
                                            .getAsJsonArray("data");


                            if (jsonArray.size() == 0) {

                                Log.e(
                                        "IMAGE_UPLOAD",
                                        "DATA EMPTY = "
                                                + imagePath
                                );

                                return;
                            }


                            JsonObject object =
                                    jsonArray
                                            .get(0)
                                            .getAsJsonObject();


                            if (!object.has("short_url")
                                    || object
                                    .get("short_url")
                                    .isJsonNull()) {

                                Log.e(
                                        "IMAGE_UPLOAD",
                                        "SHORT URL NOT FOUND = "
                                                + imagePath
                                );

                                return;
                            }


                            String shortCode =
                                    object
                                            .get("short_url")
                                            .getAsString();


                            Log.d(
                                    "SHORT_CODE",
                                    imagePath
                                            + " → "
                                            + shortCode
                            );


                            synchronized (imageUploadLock) {

                                for (PostingModel model : models) {

                                    if (model == null) {
                                        continue;
                                    }

                                    // Normal image
                                    if (imagePath.equals(model.getIMAGE())) {

                                        model.setIMAGE(shortCode);


                                        Log.d(
                                                "IMAGE_UPDATED",
                                                "NORMAL"
                                                        + " | TYPE=" + model.getDEFECT_TYPE()
                                                        + " | OLD=" + imagePath
                                                        + " | NEW=" + model.getIMAGE()
                                        );
                                    }


                                    if (imagePath.equals(model.getAI_IMAGE())) {

                                        model.setAI_IMAGE(shortCode);
                                        aiImageShortUrl = shortCode;

                                        Log.d(
                                                "IMAGE_UPDATED",
                                                "NORMAL"
                                                        + " | TYPE=" + model.getDEFECT_TYPE()
                                                        + " | OLD=" + imagePath
                                                        + " | AI_IMAGE-------:::=" + model.getAI_IMAGE()
                                                        + " | AI_IMAGE-------:::=" + aiImageShortUrl);
                                    }

                                    // DTR image
                                    if (imagePath.equals(model.getDTR_IMAGE())) {

                                        model.setDTR_IMAGE(shortCode);
                                        aiDTRShortUrl = shortCode;

                                        Log.d(
                                                "IMAGE_UPDATED",
                                                "DTR"
                                                        + " | TYPE=" + model.getDEFECT_TYPE()
                                                        + " | OLD=" + imagePath
                                                        + " | AI_DTR_IMAGE-------:::=" + model.getDTR_IMAGE()
                                                        + " | AI_DTR_IMAGE-------:::=" + aiDTRShortUrl
                                        );
                                    }
                                }
                            }


                        } catch (Exception e) {

                            Log.e(
                                    "IMAGE_UPLOAD",
                                    "Response parsing error",
                                    e
                            );

                        } finally {

                            markImageUploadCompleted();
                        }
                    }


                    @Override
                    public void onFailure(
                            Call<JsonObject> call,
                            Throwable t) {

                        Log.e(
                                "IMAGE_UPLOAD",
                                "UPLOAD FAILED = "
                                        + imagePath,
                                t
                        );

                        markImageUploadCompleted();
                    }
                });
            }


        } catch (Exception e) {

            Log.e(
                    "IMAGE_UPLOAD",
                    "UPLOAD EXCEPTION",
                    e
            );
        }
    }


    private void imageUploadCompleted() {
        openTransmitBottomSheet(true);
        synchronized (imageUploadLock) {

            completedImages++;

            if (completedImages >= totalImagesToUpload
                    && !transmissionStarted) {

                transmissionStarted = true;

                updateAllPostingModels();

                runOnUiThread(() -> {

                    Log.d(
                            "IMAGE_UPLOAD",
                            "Calling transmitPolesNew() ONCE"
                    );

                    Log.d("BEFORE_TRANSMIT", "==============================");

                    for (PostingModel model : postingModelList) {

                        Log.d(
                                "BEFORE_TRANSMIT",
                                "ID=" + model.getID()
                                        + " | TYPE=" + model.getDEFECT_TYPE()
                                        + " | IMAGE=" + model.getIMAGE()
                                        + " | DTR_IMAGE=" + model.getDTR_IMAGE()
                        );
                    }

                    Log.d("BEFORE_TRANSMIT", "==============================");

                    transmitPolesNew(
                            postingModelList
                    );
                });
            }
        }
    }

    private void updateAllPostingModels() {

        for (PostingModel model : postingModelList) {

            String imageValue = model.getIMAGE();

            if (TextUtils.isEmpty(imageValue)) {
                continue;
            }

            String[] localImages =
                    imageValue.split(",");

            ArrayList<String> finalShortCodes =
                    new ArrayList<>();

            for (String localImage : localImages) {

                localImage = localImage.trim();

                String shortCode =
                        uploadedImageMap.get(localImage);

                if (!TextUtils.isEmpty(shortCode)) {

                    finalShortCodes.add(shortCode);

                } else {

                    // If it is already a short code,
                    // keep it as it is.
                    finalShortCodes.add(localImage);
                }
            }

            String finalImageValue =
                    TextUtils.join(
                            ",",
                            finalShortCodes
                    );

            model.setIMAGE(finalImageValue);

            Log.d(
                    "FINAL_IMAGE_UPDATE",
                    "ID = " +
                            model.getID() +
                            " IMAGE = " +
                            model.getIMAGE()
            );
        }
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

    private void showImageConfirmDialogMain(Bitmap bitmap) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Image");

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(20, 20, 20, 20);

        builder.setView(imageView);

        builder.setPositiveButton("Proceed", (dialog, which) -> {
            dialog.dismiss();
            if (false) {
                processMainCapturedImage(bitmap);
            } else {
                Bitmap safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                isMainImagePMICaptured = false;
                MAIN_IMAGE_PATH = getLastImagePath(this);
                capture_Image_layout.setVisibility(View.GONE);
                taken_image_layout.setVisibility(View.VISIBLE);
                //all_defects_layout.setVisibility(View.VISIBLE);
                layout_question.setVisibility(View.VISIBLE);
                imgPreview.setImageBitmap(safeBitmap);
                ArrayList<DefectGroupModel> tempList = new ArrayList<>();
                if (SELECTED_VOLTAGE.equalsIgnoreCase("LT")) {
                    tempList.addAll(PMINewHome.defectsGroupListLT);
                }
                if (SELECTED_VOLTAGE.equalsIgnoreCase("11 KV")) {
                    for (DefectGroupModel group : PMINewHome.defectsGroupList11) {

                        if ("DTR related".equalsIgnoreCase(group.getDefectGroup())) {
                            if ("DTR Pole".equalsIgnoreCase(SELECTED_POLE_CONFIG)) {
                                tempList.add(group);
                            }

                        } else {
                            tempList.add(group);
                        }
                    }
                }
                if (SELECTED_VOLTAGE.equalsIgnoreCase("33KV")) {
                    tempList.addAll(PMINewHome.defectsGroupList33);
                }
                defectGroupAdapter = new DefectGroupAdapter(this, tempList, PmiMap.this);
                rvDefectGroups.setLayoutManager(new LinearLayoutManager(this));
                rvDefectGroups.setAdapter(defectGroupAdapter);
            }

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            mainImageBitmapPMI = null;
            dialog.dismiss();
            // ❌ Stop here – do nothing
            //isMainImagePMICaptured = false;

        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
    }

    private void showImageConfirmDialogMainDTR(Bitmap bitmap) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Image");

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(20, 20, 20, 20);

        builder.setView(imageView);

        builder.setPositiveButton("Proceed", (dialog, which) -> {
            dialog.dismiss();
            Bitmap safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
            isMainImagePMICapturedDTR = false;
            DTR_MAIN_IMAGE_PATH = getLastImagePath(this);
            dtr_capture_Image_layout.setVisibility(View.GONE);
            dtr_taken_image_layout.setVisibility(View.VISIBLE);
            dtrimgPreview.setImageBitmap(safeBitmap);
            IS_DTR_AI_IMAGE_VERIFIED = true;
//            if (ConnectivityChecking.checkConnection(PmiMap.this)) {
//                if (SELECTED_POLE_STRUCTURE.equalsIgnoreCase("SINGLE-POLE") || SELECTED_POLE_STRUCTURE.isEmpty()) {
//                    processMainCapturedImageDTR(bitmap);
//                } else {
//                    Bitmap safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
//                    isMainImagePMICapturedDTR = false;
//                    DTR_MAIN_IMAGE_PATH = getLastImagePath(this);
//                    dtr_capture_Image_layout.setVisibility(View.GONE);
//                    dtr_taken_image_layout.setVisibility(View.VISIBLE);
//                    dtrimgPreview.setImageBitmap(safeBitmap);
//                    IS_DTR_AI_IMAGE_VERIFIED = true;
//                }
//
//            }
//            else {
//                Bitmap safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
//                isMainImagePMICapturedDTR = false;
//                DTR_MAIN_IMAGE_PATH = getLastImagePath(this);
//                dtr_capture_Image_layout.setVisibility(View.GONE);
//                dtr_taken_image_layout.setVisibility(View.VISIBLE);
//                dtrimgPreview.setImageBitmap(safeBitmap);
//                IS_DTR_AI_IMAGE_VERIFIED = true;
//
//            }

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            // ❌ Stop here – do nothing
            isMainImagePMICapturedDTR = false;
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
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

    private void transmitPolesNew(List<PostingModel> transmitList) {
        openTransmitBottomSheet(true);
        Map<String, String> defectTypeImageMap = new HashMap<>();

        for (PostingModel model : transmitList) {

            String defectType = model.getDEFECT_TYPE();
            String imagePath = model.getIMAGE();

            if (TextUtils.isEmpty(defectType)) {
                continue;
            }

            if (!defectTypeImageMap.containsKey(defectType)
                    && !TextUtils.isEmpty(imagePath)) {

                defectTypeImageMap.put(defectType, imagePath);

                Log.d(
                        "DEFECT_IMAGE_MAP",
                        defectType + " → " + imagePath
                );
            }
        }

        PmiDatabase pmiDatabase = new PmiDatabase(PmiMap.this);
        for (int i = 0; i < postingModelList.size(); i++) {
            pmiDatabase.insertOrUpdatePmiPosting(postingModelList.get(i));
        }
        TransmitLoadingBottomSheet.show(this);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String userId = preferences.getString(MobileConstants.SESSION_ID, "");
        String device_id = preferences.getString(MobileConstants.DEVICE_ID, "");
        String mobile_no = preferences.getString(MobileConstants.MOBILE_NUMBER, "");
        System.out.println("GGSGGSGSG::::" + preferences.getString(MobileConstants.SESSION_ID, "") + " " + loginDataResponses.get(0).getNAME());
        JsonArray array = new JsonArray();
        for (int i = 0; i < transmitList.size(); i++) {

            PostingModel model = transmitList.get(i);
            JsonObject requestObject = new JsonObject();
            requestObject.addProperty("APP_VERSION", MobileConstants.CYCLONE_SURVEY);
            requestObject.addProperty("APP_VERSION2", getResources().getString(R.string.displayVersion));
            requestObject.addProperty("GPS_ACCURACY", gpsAccurancy);
            requestObject.addProperty("ID", model.getID());
            requestObject.addProperty("DEVICE_ID", device_id);
            requestObject.addProperty("DEFECT_ID", model.getDEFECT_ID());
            requestObject.addProperty("POLE_ID", model.getPOLE_ID());
            requestObject.addProperty("POLE_NO", model.getPOLE_NO());
            requestObject.addProperty("VOLTAGE_LEVEL", model.getVOLTAGE_LEVEL());
            requestObject.addProperty("DTR_CODE", model.getDTR_CODE());
            requestObject.addProperty("LT_CKT_CODE", model.getLT_CKT_CODE());
            requestObject.addProperty("FEEDER_ID", model.getFEEDER_ID());
            requestObject.addProperty("DEFECT_BY_USER", model.getDEFECT_BY_USER());
            requestObject.addProperty("POLE_CONFIG", model.getPOLE_CONFIG());
            if ("RECT".equalsIgnoreCase(FROM)) {
                requestObject.addProperty("RECTIFIED", "Y");
            }


            requestObject.addProperty("DEFECT_TYPE", model.getDEFECT_TYPE());
            String commonImage;

            if ("DTR related".equalsIgnoreCase(model.getDEFECT_GROUP())
                    || "DTR Mounting / Structure Defects"
                    .equalsIgnoreCase(model.getDEFECT_TYPE())) {

                commonImage = model.getDTR_IMAGE();

                Log.d(
                        "FINAL_SHORT_PATH",
                        "DTR"
                                + " | DEFECT_TYPE = "
                                + model.getDEFECT_TYPE()
                                + " | DTR_IMAGE = "
                                + commonImage
                );

            } else {
                commonImage = model.getIMAGE();

                Log.d(
                        "FINAL_SHORT_PATH",
                        "NORMAL"
                                + " | DEFECT_TYPE = "
                                + model.getDEFECT_TYPE()
                                + " | IMAGE = "
                                + commonImage
                );
            }

            requestObject.addProperty(
                    "IMG_SHORT_PATH",
                    TextUtils.isEmpty(commonImage)
                            ? ""
                            : commonImage
            );

            requestObject.addProperty("DEFECT_SUB_TYPE", model.getDEFECT_SUB_TYPE());
            requestObject.addProperty("DEFECT_SUB_TYPE_ID", model.getDEFECT_SUB_TYPE_ID());
            requestObject.addProperty("CYCLONE", model.getCYCLONE());
            requestObject.addProperty("CURRENT_LAT", model.getCURRENT_LAT());
            requestObject.addProperty("CURRENT_LONG", model.getCURRENT_LONG());
            requestObject.addProperty("POLE_LAT", model.getPOLE_LAT());
            requestObject.addProperty("POLE_LONG", model.getPOLE_LONG());

            //  requestObject.addProperty("IMAGE", postingList.get(i).getIMAGE());
            // Log.v("PMI DATA IMAGE", postingList.get(i).getIMAGE());
            //requestObject.addProperty("IMG_SHORT_PATH", model.getIMAGE());
            //requestObject.addProperty("IMAGE", ConvertImage.convertBase64(model.getIMAGE()));
            // requestObject.addProperty("IMAGE", "N");
            requestObject.addProperty("RECT_REMARKS", model.getRECT_REMARKS());
            if (loginDataResponses.get(0).getROLE().equalsIgnoreCase("AEOP")) {
                requestObject.addProperty("USERID", loginDataResponses.get(0).getUSERID());
            } else {
                requestObject.addProperty("USERID", userId);
            }


            if (loginDataResponses.get(0).getROLE().equalsIgnoreCase("AEOP")) {
                requestObject.addProperty("USERNAME", loginDataResponses.get(0).getROLE());
            } else {
                requestObject.addProperty("USERNAME", loginDataResponses.get(0).getNAME());
            }
            //requestObject.addProperty("USERNAME", loginDataResponses.get(0).getROLE());
            requestObject.addProperty("SURVEY_MODE", "ONLINE");

            requestObject.addProperty("DEFECT_GROUP", model.getDEFECT_GROUP());


            /*requestObject.addProperty("AI_RESPONSE", model.getAI_RESPONSE());
            requestObject.addProperty("POSITION", model.getPOSITION());
            requestObject.addProperty("VOLTAGE", model.getVOLTAGE());
            requestObject.addProperty("LEAN_ANGLE", model.getLEAN_ANGLE());
            requestObject.addProperty("POSTITION_XARM",model.getPOSTITION_XARM());*/

            JsonArray jsonArray = new JsonArray();

            if (model.getAiResponseModel() != null && !model.getAiResponseModel().isEmpty()) {
                for (AIResponseModel aiResponseModel : model.getAiResponseModel()) {
                    JsonObject resObject = new JsonObject();
                    resObject.addProperty("AI_RESPONSE", aiResponseModel.getAI_RESPONSE());
                    resObject.addProperty("POSITION", aiResponseModel.getPOSITION());
                    resObject.addProperty("VOLTAGE", aiResponseModel.getVOLTAGE());
                    resObject.addProperty("LEAN_ANGLE", aiResponseModel.getLEAN_ANGLE());
                    resObject.addProperty("POSTITION_XARM", aiResponseModel.getPOSTITION_XARM());
                    resObject.addProperty("POLE_RATIO", aiResponseModel.getPOLE_RATIO());
                    resObject.addProperty("PLINTH_RATIO", aiResponseModel.getPLINTH_RATIO());
                    resObject.addProperty("POLE_BBOX", aiResponseModel.getPOLE_BBOX());
                    resObject.addProperty("CROP_BBOX", aiResponseModel.getCROP_BBOX());
                    resObject.addProperty("VD_DEPTH", aiResponseModel.getVD_DEPTH());
                    resObject.addProperty("VC_REASON", aiResponseModel.getVC_REASON());
                    resObject.addProperty("IMAGE", aiResponseModel.getIMAGE().equalsIgnoreCase("D") ? aiDTRShortUrl : aiImageShortUrl);


                    jsonArray.add(resObject);
                }
            }

            requestObject.add("RESPONSE", jsonArray);
            array.add(requestObject);

        }


        System.out.println("TOTAL DEFECTS : " + array.size());
        /*Gson gson = new Gson();
        String json = gson.toJson(array);
        Log.v("PMI DATA===>>", "" + json);*/


        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        // Call<JsonObject> responseVoCall = retrofitServicePmi.transmitData(array);
        Call<JsonObject> responseVoCall;
        if ("RECT".equalsIgnoreCase(FROM)) {
            responseVoCall = retrofitServicePmi.transmitDataNewRect(array);
        } else {
            responseVoCall = retrofitServicePmi.transmitDataNew(array);
        }


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                TransmitLoadingBottomSheet.dismiss();
                openTransmitBottomSheet(false);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            //selectedChipDefects.clear();
                            selectedDefect = null;
                            SELECTED_GROUP_NAME = "";
                            IS_DTR_POLE = false;
                            IS_DTR_AI_IMAGE_VERIFIED = false;
                            DTR_MAIN_IMAGE_PATH = "";
                            MAIN_IMAGE_PATH = "";
                            transmitList.clear();
                            newDefectDialog.dismiss();


                            // Clear selected chip list
                            selectedChipDefects.clear();

                            // Reset all master defects
                            for (DefectsModel defect : PMINewHome.allDefectsList) {
                                defect.setSelected(false);
                                defect.setImageSelected(false);
                                defect.setImagePath("");
                            }

                            // Refresh UI
                            if (defectGroupAdapter != null) {
                                defectGroupAdapter.notifyDataSetChanged();
                            }

                            if (defectTypeAdapter != null) {
                                defectTypeAdapter.notifyDataSetChanged();
                            }

                            for (DefectGroupModel group : PMINewHome.defectsGroupListLT) {
                                group.setExpanded(false);
                                group.setImageSelected(false);
                                group.setImage("");
                            }
                            for (DefectGroupModel group : PMINewHome.defectsGroupList11) {
                                group.setExpanded(false);
                                group.setImageSelected(false);
                                group.setImage("");
                            }
                            for (DefectGroupModel group : PMINewHome.defectsGroupList33) {
                                group.setExpanded(false);
                                group.setImageSelected(false);
                                group.setImage("");
                            }

                            PmiDatabase pmiDatabase = new PmiDatabase(PmiMap.this);
                            pmiDatabase.deleteAllPmiData();


                            Toast.makeText(PmiMap.this, "Damage Transmitted successfully", Toast.LENGTH_LONG).show();


                        } catch (Exception e) {
                            transmitList.clear();
                            e.printStackTrace();
                        }
                    } else {
                        transmitList.clear();
                        postingModelList.clear();
                        PmiDatabase pmiDatabase = new PmiDatabase(PmiMap.this);
                        pmiDatabase.deleteAllPmiData();
                        newDefectDialog.dismiss();
                        Toast.makeText(PmiMap.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(
                                PmiMap.this.findViewById(android.R.id.content),
                                surveyedResponseVo.get("ERR_MSG").toString(),
                                Snackbar.LENGTH_SHORT
                        ).show();

                    }


                } else {

                    Toast.makeText(PmiMap.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                openTransmitBottomSheet(false);
                // progressDialog.dismiss();
                Toast.makeText(PmiMap.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onCaptureClick(int position, DefectsModel defect, String groupName) {
        SELECTED_GROUP_NAME = groupName;
        selectedDefect = defect;
        Intent intentOpenCamera = new Intent(PmiMap.this, MainActivity.class);
        startActivity(intentOpenCamera);
        photoClickedPMI1 = true;
        isImageCapturedPMI1 = false;
        capturedBitmapPMI1 = null;
        currentRequestCode = position;
        //imageView.setTag(requestCode);
        current_selected_sub_id = defect.getDEFECT_SUB_TYPE_ID();

    }

    @Override
    public void onRetakeClick(int position, DefectsModel defect, String groupName) {
        selectedDefect = defect;
        SELECTED_GROUP_NAME = groupName;
        Intent intentOpenCamera = new Intent(PmiMap.this, MainActivity.class);
        startActivity(intentOpenCamera);
        photoClickedPMI1 = true;
        isImageCapturedPMI1 = false;
        capturedBitmapPMI1 = null;
        currentRequestCode = position;
        //imageView.setTag(requestCode);
        current_selected_sub_id = defect.getDEFECT_SUB_TYPE_ID();
    }


    public class DefectGroupAdapter extends RecyclerView.Adapter<DefectGroupAdapter.ViewHolder> {

        private Context context;
        private ArrayList<DefectGroupModel> groupList;
        private OnImageActionListener listener;


        public DefectGroupAdapter(Context context, ArrayList<DefectGroupModel> groupList, OnImageActionListener listener) {
            this.context = context;
            this.groupList = groupList;
            this.listener = listener;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvGroup;
            ImageView ivArrow;
            RecyclerView rvChild;
            LinearLayout expandableLayout;
            LinearLayout captureLayout;

            LinearLayout taken_image_layout, layoutChangeImage;
            ImageView imgPreview, fullImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tvGroup = itemView.findViewById(R.id.tvGroup);
                ivArrow = itemView.findViewById(R.id.ivArrow);
                rvChild = itemView.findViewById(R.id.rvChild);
                expandableLayout = itemView.findViewById(R.id.expandableLayout);
                captureLayout = itemView.findViewById(R.id.captureLayout);
                taken_image_layout = itemView.findViewById(R.id.taken_image_layout);
                layoutChangeImage = itemView.findViewById(R.id.layoutChangeImage);
                imgPreview = itemView.findViewById(R.id.imgPreview);
                fullImage = itemView.findViewById(R.id.fullImage);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_defect_group, parent, false);
            return new DefectGroupAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            DefectGroupModel group = groupList.get(position);

            holder.tvGroup.setText(group.getDefectGroup());

            holder.expandableLayout.setVisibility(
                    group.isExpanded() ? View.VISIBLE : View.GONE
            );

            holder.ivArrow.setRotation(group.isExpanded() ? 180 : 0);

            holder.itemView.setOnClickListener(v -> {
                group.setExpanded(!group.isExpanded());
                notifyItemChanged(position);
            });

            List<DefectsModel> defectsList = group.getDefects();

            List<DefectsModel> uniqueList = new ArrayList<>(
                    defectsList.stream()
                            .collect(Collectors.toMap(
                                    DefectsModel::getDEFECT_TYPE,
                                    d -> d,
                                    (existing, replacement) -> existing,
                                    LinkedHashMap::new  // preserves insertion order
                            ))
                            .values()
            );

            holder.rvChild.setLayoutManager(new LinearLayoutManager(context));
            defectTypeAdapter = new DefectTypeAdapter(context, uniqueList);
            holder.rvChild.setAdapter(defectTypeAdapter);

            holder.captureLayout.setOnClickListener(v -> {
                listener.onCaptureClick(position, uniqueList.get(0), group.getDefectGroup());
            });

            holder.layoutChangeImage.setOnClickListener(v -> {
                listener.onRetakeClick(position, uniqueList.get(0), group.getDefectGroup());
            });

            updateImageUIParent(holder, group);

        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        private void updateImageUIParent(ViewHolder holder, DefectGroupModel defect) {

            Log.d("UI_STATE 2",
                    "selected=" + defect.isPhotoNeeded()
                            + " imageSelected=" + defect.isImageSelected()
                            + " path=" + defect.getImage());


            boolean needsPhoto = defect.isPhotoNeeded();

            if (!needsPhoto) {
                holder.captureLayout.setVisibility(View.GONE);
                holder.taken_image_layout.setVisibility(View.GONE);
                return;
            }

            if (defect.isImageSelected()
                    && defect.getImage() != null
                    && !defect.getImage().isEmpty()) {

                holder.captureLayout.setVisibility(View.GONE);
                holder.taken_image_layout.setVisibility(View.VISIBLE);

                Bitmap bitmap = BitmapFactory.decodeFile(defect.getImage());

                holder.imgPreview.setImageBitmap(bitmap);

            } else {
                holder.captureLayout.setVisibility(View.GONE);
                holder.taken_image_layout.setVisibility(View.GONE);
            }
        }

    }


    public class DefectTypeAdapter extends RecyclerView.Adapter<DefectTypeAdapter.ViewHolder> {

        private Context context;
        private List<DefectsModel> defects;


        public DefectTypeAdapter(Context context, List<DefectsModel> defects) {
            this.context = context;
            this.defects = defects;

        }


        class ViewHolder extends RecyclerView.ViewHolder {

            CheckBox checkBox;
            ChipGroup chipGroup;
            // LinearLayout captureLayout;
            LinearLayout chipContainer;

            //  LinearLayout taken_image_layout, layoutChangeImage;
            // ImageView imgPreview, fullImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.checkboxDefect);
                chipGroup = itemView.findViewById(R.id.chipGroup);
                // captureLayout = itemView.findViewById(R.id.captureLayout);
                chipContainer = itemView.findViewById(R.id.chipContainer);
                // taken_image_layout = itemView.findViewById(R.id.taken_image_layout);
                // layoutChangeImage = itemView.findViewById(R.id.layoutChangeImage);
                // imgPreview = itemView.findViewById(R.id.imgPreview);
                // fullImage = itemView.findViewById(R.id.fullImage);

            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_defect_child, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            DefectsModel defect = defects.get(position);

            holder.checkBox.setText(defect.getDEFECT_TYPE());

            // 1. Remove listener before setting checked state to avoid false triggers
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(defect.isSelected());

            updateImageUI(holder, defect);

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                defect.setSelected(isChecked);

                if (isChecked) {
                    holder.chipContainer.setVisibility(View.VISIBLE);

                    ArrayList<DefectsModel> allSubDefects = new ArrayList<>();
                    for (int i = 0; i < PMINewHome.allDefectsList.size(); i++) {
                        if (defect.getDEFECT_TYPE().equals(PMINewHome.allDefectsList.get(i).getDEFECT_TYPE())) {
                            allSubDefects.add(PMINewHome.allDefectsList.get(i));
                        }
                    }
                    List<DefectsModel> uniqueList = new ArrayList<>(
                            allSubDefects.stream()
                                    .collect(Collectors.toMap(
                                            DefectsModel::getDEFECT_SUB_TYPE,
                                            d -> d,
                                            (existing, replacement) -> existing,
                                            LinkedHashMap::new  // preserves insertion order
                                    ))
                                    .values()
                    );
                    System.out.println("Sub defects Size isChecked : " + uniqueList.size());
                    addSubDefectChipNew(holder.chipGroup, uniqueList);

                } else {
                    holder.chipContainer.setVisibility(View.GONE);
                    holder.chipGroup.removeAllViews();

                    // Reset image state
                    defect.setImageSelected(false);
                    defect.setImagePath("");

                    // Remove all selected chips for this defect type
                    Iterator<DefectsModel> iterator = selectedChipDefects.iterator();

                    while (iterator.hasNext()) {
                        DefectsModel selected = iterator.next();

                        if (selected.getDEFECT_TYPE()
                                .equals(defect.getDEFECT_TYPE())) {
                            iterator.remove();
                        }
                    }

                    // Hide image UI
                    //  holder.captureLayout.setVisibility(View.GONE);
                    // holder.taken_image_layout.setVisibility(View.GONE);

                    // Optional: clear preview image
                    // holder.imgPreview.setImageDrawable(null);
                }

                updateImageUI(holder, defect);
            });


          /*  holder.captureLayout.setOnClickListener(v -> {
                listener.onCaptureClick(position, defect);
            });

            holder.layoutChangeImage.setOnClickListener(v -> {
                listener.onRetakeClick(position, defect);
            });*/

        }

        public void refreshData() {
            notifyDataSetChanged();
        }

        private void addSubDefectChipOld(ChipGroup chipGroup, List<DefectsModel> defect) {

            chipGroup.removeAllViews();

            for (int i = 0; i < defect.size(); i++) {
                Chip chip = new Chip(new ContextThemeWrapper(context,
                        com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));

                chip.setText(defect.get(i).getDEFECT_SUB_TYPE());
                chip.setCheckedIconVisible(false);

                int[][] states = new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                };
                int[] colors = new int[]{
                        Color.parseColor("#2F80ED"),  // checked
                        Color.parseColor("#AAAAAA")   // unchecked
                };

                chip.setChipBackgroundColor(new ColorStateList(states, colors));
                chip.setTextColor(Color.WHITE);
                chip.setCheckable(true);
                chip.setClickable(true);


                chipGroup.addView(chip);


            }


        }

        private void addSubDefectChipNew(ChipGroup chipGroup,
                                         List<DefectsModel> defectList) {

            chipGroup.removeAllViews();

            for (int i = 0; i < defectList.size(); i++) {

                DefectsModel currentDefect = defectList.get(i);

                Chip chip = new Chip(new ContextThemeWrapper(
                        context,
                        com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
                ));

                chip.setText(currentDefect.getDEFECT_SUB_TYPE());
                chip.setCheckedIconVisible(false);
                chip.setCheckable(true);
                chip.setClickable(true);

                int[][] states = new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                };

                int[] colors = new int[]{
                        Color.parseColor("#2F80ED"),   // checked
                        Color.parseColor("#AAAAAA")    // unchecked
                };

                chip.setChipBackgroundColor(new ColorStateList(states, colors));
                chip.setTextColor(Color.WHITE);

                // Restore checked state if already selected
                boolean alreadySelected = false;
                for (DefectsModel selected : selectedChipDefects) {
                    if (selected.getDEFECT_SUB_TYPE_ID().equalsIgnoreCase(currentDefect.getDEFECT_SUB_TYPE_ID())) {
                        alreadySelected = true;
                        break;
                    }
                }

                chip.setChecked(alreadySelected);

                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (isChecked) {

                        boolean exists = false;

                        for (DefectsModel selected : selectedChipDefects) {
                            if (selected.getDEFECT_SUB_TYPE_ID().equalsIgnoreCase(currentDefect.getDEFECT_SUB_TYPE_ID())) {
                                exists = true;
                                break;
                            }
                        }

                        if (!exists) {
                            selectedChipDefects.add(currentDefect);
                        }

                    } else {

                        Iterator<DefectsModel> iterator =
                                selectedChipDefects.iterator();

                        while (iterator.hasNext()) {
                            DefectsModel selected = iterator.next();

                            if (selected.getDEFECT_SUB_TYPE_ID().equalsIgnoreCase(currentDefect.getDEFECT_SUB_TYPE_ID())) {

                                iterator.remove();
                                break;
                            }
                        }
                    }

                    Log.d("SELECTED_CHIPS",
                            "count=" + selectedChipDefects.size());
                });

                chipGroup.addView(chip);
            }
        }

        private void updateImageUI(ViewHolder holder, DefectsModel defect) {

            Log.d("UI_STATE",
                    "selected=" + defect.isSelected()
                            + " imageSelected=" + defect.isImageSelected()
                            + " path=" + defect.getImagePath());

            if (!defect.isSelected()) {
                // holder.captureLayout.setVisibility(View.GONE);
                // holder.taken_image_layout.setVisibility(View.GONE);
                holder.chipContainer.setVisibility(View.GONE);
                holder.chipGroup.removeAllViews();
                return;
            }

            // show chip container if selected
            holder.chipContainer.setVisibility(View.VISIBLE);
            ArrayList<DefectsModel> allSubDefects = new ArrayList<>();
            for (int i = 0; i < PMINewHome.allDefectsList.size(); i++) {
                if (defect.getDEFECT_TYPE().equals(PMINewHome.allDefectsList.get(i).getDEFECT_TYPE())) {
                    allSubDefects.add(PMINewHome.allDefectsList.get(i));
                }
            }
            List<DefectsModel> uniqueList = new ArrayList<>(
                    allSubDefects.stream()
                            .collect(Collectors.toMap(
                                    DefectsModel::getDEFECT_SUB_TYPE,
                                    d -> d,
                                    (existing, replacement) -> existing,
                                    LinkedHashMap::new  // preserves insertion order
                            ))
                            .values()
            );
            System.out.println("Sub defects Size isChecked : " + uniqueList.size());
            addSubDefectChipNew(holder.chipGroup, uniqueList);

           /* boolean needsPhoto =
                    "Y".equalsIgnoreCase(defect.getPHOTO()) &&
                            "N".equalsIgnoreCase(defect.getAI());

            if (!needsPhoto) {
                holder.captureLayout.setVisibility(View.GONE);
                holder.taken_image_layout.setVisibility(View.GONE);
                return;
            }

            if (defect.isImageSelected()
                    && defect.getImagePath() != null
                    && !defect.getImagePath().isEmpty()) {

                holder.captureLayout.setVisibility(View.GONE);
                holder.taken_image_layout.setVisibility(View.VISIBLE);

                Bitmap bitmap = BitmapFactory.decodeFile(defect.getImagePath());

                holder.imgPreview.setImageBitmap(bitmap);

            } else {
                holder.captureLayout.setVisibility(View.VISIBLE);
                holder.taken_image_layout.setVisibility(View.GONE);
            }*/
        }

        public List<DefectsModel> getDefects() {
            return defects;
        }

        @Override
        public int getItemCount() {
            return defects.size();
        }
    }

    private void markImageUploadCompleted() {

        synchronized (imageUploadLock) {

            completedImages++;

            Log.d(
                    "IMAGE_UPLOAD",
                    "IMAGE COMPLETED = "
                            + completedImages
                            + " / "
                            + totalImagesToUpload
            );

            if (completedImages >= totalImagesToUpload) {

                Log.d(
                        "IMAGE_UPLOAD",
                        "ALL UNIQUE IMAGES UPLOADED"
                );

                imageUploadCompleted();
            }
        }
    }

}

