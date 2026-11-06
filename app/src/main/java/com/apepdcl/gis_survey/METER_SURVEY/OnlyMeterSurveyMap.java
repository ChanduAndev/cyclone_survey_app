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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by SOUBHAGYA SWAIN on 16-06-2026.
 */
public class OnlyMeterSurveyMap extends AppCompatActivity {

    TextView name_value, id_value;
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

    RelativeLayout reload_map_layout;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mVoltageProcessingRunnable;
    private static final long PROCESS_DELAY_MS = 100;
    ProgressDialog progressDialog;

    AssignedDtrModel dtrDetails;

    MeterSurveyDatabase database;
    boolean isImageCaptured,isServiceNumberSelected;
    TextView select_service_number;
    Uri imageUri;

    double tapped_latitude,tapped_longitude;
    Dialog surveyDialog;

    String selected_UKSC_NO, selected_SC_NAME;
    ActivityResultLauncher<Uri>  cameraLauncher;

    ImageView captured_image;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.only_meter_survey_map);
        getSupportActionBar().hide();

        dtrDetails = (AssignedDtrModel) getIntent().getSerializableExtra("DTR_DATA");
        database = new MeterSurveyDatabase(OnlyMeterSurveyMap.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);

        back_layout = findViewById(R.id.back_layout);
        reload_map_layout = findViewById(R.id.reload_map_layout);
        current_location_layout = findViewById(R.id.close_layout);
        mv = findViewById(R.id.map1);
        name_value = findViewById(R.id.name_value);
        id_value = findViewById(R.id.id_value);

        name_value.setText(dtrDetails.getDTR_LOCATION());
        id_value.setText(dtrDetails.getDTR_CODE());

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
            Toast.makeText(OnlyMeterSurveyMap.this, message, Toast.LENGTH_LONG).show();

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

            }
        });

       cameraLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.TakePicture(),
                        result -> {
                            if (result) {
                                isImageCaptured = true;
                                try {
                                    captured_image.setImageURI(CommanUtilits.compressImage(OnlyMeterSurveyMap.this,imageUri));
                                } catch (Exception e) {
                                    e.printStackTrace();
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


                }
                boolean isPoleFound = false;
                double distance = getDistanceBetweenPoles33New(
                        gpsTraker.getLatitude(),
                        gpsTraker.getLongitude(),
                        latitude,
                        longitude
                );

                if (distance <= 100.0) {
                    isPoleFound = true;
                }

                if (!isPoleFound) {
                    openAlertDialog("Tap exactly (or within 100 meters radius) on from the current location");
                } else {
                    tapped_longitude = longitude;
                    tapped_latitude = latitude;
                    openSurveyFormDialog();
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
        // Similar pattern for LT
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    loadDTRLocation();
                    List<MeterPostingModel> serviceList = database.getOnlyLatLonData(dtrDetails.getDTR_CODE());
                    if (!serviceList.isEmpty()){
                        loadServiceNumbersOnMap(serviceList);
                    }
                    //System.out.println("LOCATIONS_SIZE::::::"+MeterConstants.alreadyMappedList.size());
                    if (MeterConstants.alreadyMappedList.isEmpty()) {
                        loadDTRLocation();
                        loadServiceNumbersOnMap(MeterConstants.alreadyMappedList);

                    } else {
                        loadServiceNumbersOnMap(MeterConstants.alreadyMappedList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void loadServiceNumbersOnMap(List<MeterPostingModel> serviceList) {

        //System.out.println("Service List Size: " + serviceList.size());

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

//    private void loadServiceNumbersOnMap(List<MeterPostingModel> serviceList) {
//
//          System.out.println("Service List Size: " + serviceList.size());
//
//        mv.getGraphicsOverlays().clear();
//
//        polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//        Runnable uiUpdateRunnable = new Runnable() {
//            private int currentIndex = 0;
//
//            @Override
//            public void run() {
//                try {
//                    int chunkSize = 10;
//                    int endIndex = Math.min(currentIndex + chunkSize, serviceList.size());
//
//                    for (int i = currentIndex; i < endIndex; i++) {
//                        //   System.out.println("SERVICE NUMBERS");
//                        //   System.out.println("LATITUDE: " + serviceList.get(i).getLATITUDE());
//                        //   System.out.println("LONGITUDE: " + serviceList.get(i).getLONGITUDE());
//
//                        polylinePoints.add(new Point(
//                                Double.parseDouble(serviceList.get(i).getLATITUDE()),
//                                Double.parseDouble(serviceList.get(i).getLONGITUDE())
//                        ));
//
//                        graphicsOverlay = new GraphicsOverlay();
//                        mv.getGraphicsOverlays().add(graphicsOverlay);
//
//                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
//                                SimpleMarkerSymbol.Style.CIRCLE,
//                                Color.BLUE,
//                                12
//                        );
//
//                        ptWgs84 = new Point(
//                                Double.parseDouble(serviceList.get(i).getLATITUDE()),
//                                Double.parseDouble(serviceList.get(i).getLONGITUDE()),
//                                414,
//                                SpatialReferences.getWgs84()
//                        );
//
//                        graphic = new Graphic(ptWgs84, symbol);
//                        graphicsOverlay.getGraphics().add(graphic);
//
//                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
//                        absoluteOverlay.getSceneProperties().setSurfacePlacement(
//                                LayerSceneProperties.SurfacePlacement.ABSOLUTE
//                        );
//                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
//
//                        TextSymbol absoluteText = new TextSymbol(
//                                15,
//                                serviceList.get(i).getSERVICE_NUMBER(),
//                                Color.WHITE,
//                                TextSymbol.HorizontalAlignment.LEFT,
//                                TextSymbol.VerticalAlignment.MIDDLE
//                        );
//                        absoluteText.setOffsetY(20);
//                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
//                        mv.getGraphicsOverlays().add(absoluteOverlay);
//                    }
//
//                    currentIndex = endIndex;
//
//                    if (currentIndex < serviceList.size()) {
//                        mHandler.postDelayed(this, 16);
//                    } else {
//                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        mHandler.post(uiUpdateRunnable);
//    }


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
                                "DTR-"+dtrDetails.getDTR_CODE(),
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

    private void openSurveyFormDialog() {
        isImageCaptured = false;
        isServiceNumberSelected = false;
        surveyDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        surveyDialog.setContentView(R.layout.only_meter_survey_form_dialog);
        surveyDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        surveyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        surveyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        surveyDialog.setCancelable(false);
        ImageView back_layout = surveyDialog.findViewById(R.id.back_layout);
        select_service_number = surveyDialog.findViewById(R.id.select_service_number);
        TextView btn_capture_image = surveyDialog.findViewById(R.id.btn_capture_image);
        TextView submitButton = surveyDialog.findViewById(R.id.submitButton);
        captured_image = surveyDialog.findViewById(R.id.captured_image);

        select_service_number.setText("");
        select_service_number.setHint("Select Service Number");



        btn_capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "MeterImage_" + System.currentTimeMillis());
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                cameraLauncher.launch(imageUri);
            }
        });

        select_service_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ServiceNumbersModel> serviceNumbersModelArrayList = new ArrayList<>(MeterConstants.serviceNumbersList);
                showServiceSearchBottomSheet( false, serviceNumbersModelArrayList);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isServiceNumberSelected){
                    Toast.makeText(OnlyMeterSurveyMap.this, "Please Select Service Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*if (!isImageCaptured){
                    Toast.makeText(OnlyMeterSurveyMap.this, "Please Capture Meter Image", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                GpsTraker currentTracker = new GpsTraker(OnlyMeterSurveyMap.this);
                MeterSurveyDatabase database = new MeterSurveyDatabase(OnlyMeterSurveyMap.this);
                MeterPostingModel data = new MeterPostingModel();
                data.setREF_ID(System.currentTimeMillis()+"_1");
                data.setPOLE_ID("");
                data.setPOLE_NO("");
                data.setSERVICE_NUMBER(select_service_number.getText().toString());
                data.setDTR_CODE(dtrDetails.getDTR_CODE());
                data.setDTR_NAME(dtrDetails.getDTR_LOCATION());
                data.setLT_CKT_CODE("");
                data.setLATITUDE(String.valueOf(tapped_longitude));
                data.setLONGITUDE(String.valueOf(tapped_latitude));
                data.setCURRENT_LAT(String.valueOf(currentTracker.getLongitude()));
                data.setCURRENT_LON(String.valueOf(currentTracker.getLatitude()));
                data.setGPS_ACCURACY(String.valueOf(currentTracker.getLocation().getAccuracy()));
                data.setIMAGE("");
                data.setIS_IMAGE_TRANSMITTED("Y");
                data.setUKSC_NO(selected_UKSC_NO);
                data.setSC_NAME(selected_SC_NAME);
                data.setREMARKS("");
                if (ConnectivityChecking.checkConnection(OnlyMeterSurveyMap.this)) {
                    data.setSURVEY_MODE("ONLINE");
                }else {
                    data.setSURVEY_MODE("OFFLINE");
                }
                database.addMeterSurveyData(data);
                Toast.makeText(OnlyMeterSurveyMap.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                surveyDialog.dismiss();
            }
        });


        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyDialog.dismiss();
            }
        });

        surveyDialog.show();
    }

    @SuppressLint("MissingInflatedId")
    private void showServiceSearchBottomSheet(boolean isAreaFilter, ArrayList<ServiceNumbersModel> serviceNumbersModelArrayList) {

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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OnlyMeterSurveyMap.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);

        SearchServicesAdapter searchServicesAdapter = new SearchServicesAdapter(serviceNumbersModelArrayList, dialog);
        recyclerView.setAdapter(searchServicesAdapter);

        if (isAreaFilter){
            radioGroup.setVisibility(View.GONE);
            label_search_by.setVisibility(View.GONE);
            or_label_search_by.setText("Area Name: "+serviceNumbersModelArrayList.get(0).getAREA_NAME()+"\nArea Code: "+serviceNumbersModelArrayList.get(0).getAREA_CODE());
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
                        if (serviceNumbersModelArrayList.get(j).getSERVICE_NUMBER().toLowerCase().contains(searchText.toLowerCase())){
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                }else {
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
                        if (serviceNumbersModelArrayList.get(j).getCONSUMER_NAME().toLowerCase().contains(searchText.toLowerCase())){
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                }else {
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
                        if (serviceNumbersModelArrayList.get(j).getMETER_NO().toLowerCase().contains(searchText.toLowerCase())){
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                }
                else {
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
                        for(ServiceNumbersModel item : serviceNumbersModelArrayList){
                            uniqueAreaCodes.add(item.getAREA_CODE());
                        }
                        ArrayList<String> arList = new ArrayList<>();
                        arList.addAll(uniqueAreaCodes);

                        SearchableSpinner searchableSpinner = new SearchableSpinner(OnlyMeterSurveyMap.this);
                        searchableSpinner.setWindowTitle("Select Area Code");
                        searchableSpinner.setSpinnerListItems(arList);
                        searchableSpinner.show();
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                String selectedAreaCode = selectedString;
                                ArrayList<ServiceNumbersModel> serList = new ArrayList<>();
                                for (int i = 0; i < serviceNumbersModelArrayList.size(); i++) {
                                    if (serviceNumbersModelArrayList.get(i).getAREA_CODE().equals(selectedAreaCode)){
                                        serList.add(serviceNumbersModelArrayList.get(i));
                                    }
                                }

                                showServiceSearchBottomSheet(true, serList);
                            }
                        });
                        break;
                    case R.id.radioAreaName:
                        selected = "Area Name";
                        dialog.dismiss();
                        Set<String> uniqueAreaNames = new HashSet<>();
                        for(ServiceNumbersModel item : serviceNumbersModelArrayList){
                            uniqueAreaNames.add(item.getAREA_NAME());
                        }
                        ArrayList<String> arNameList = new ArrayList<>();
                        arNameList.addAll(uniqueAreaNames);

                        SearchableSpinner searchableSpinnerName = new SearchableSpinner(OnlyMeterSurveyMap.this);
                        searchableSpinnerName.setWindowTitle("Select Area Name");
                        searchableSpinnerName.setSpinnerListItems(arNameList);
                        searchableSpinnerName.show();
                        searchableSpinnerName.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                String selectedAreaName = selectedString;
                                ArrayList<ServiceNumbersModel> serListName = new ArrayList<>();
                                for (int i = 0; i < serviceNumbersModelArrayList.size(); i++) {
                                    if (serviceNumbersModelArrayList.get(i).getAREA_NAME().equals(selectedAreaName)){
                                        serListName.add(serviceNumbersModelArrayList.get(i));
                                    }
                                }

                                showServiceSearchBottomSheet(true, serListName);
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
        BottomSheetDialog dialog;

        public void updateList(ArrayList<ServiceNumbersModel> newList){
            this.list = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        public SearchServicesAdapter(ArrayList<ServiceNumbersModel> list,  BottomSheetDialog dialog) {
            this.list = list;
            this.dialog = dialog;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(OnlyMeterSurveyMap.this).inflate(R.layout.service_search_adapter, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.search_tv_service_number.setText(list.get(position).getSERVICE_NUMBER());
            holder.search_tv_consumer_name.setText(list.get(position).getCONSUMER_NAME());
            holder.search_tv_meter_number.setText("Meter No: "+list.get(position).getMETER_NO());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ///DB CHECK
                    MeterSurveyDatabase database = new MeterSurveyDatabase(OnlyMeterSurveyMap.this);
                    List<MeterPostingModel> allData = database.getAllMeterSurveyData();
                    for (int i = 0; i < allData.size(); i++) {
                        if (allData.get(i).getSERVICE_NUMBER().equalsIgnoreCase(list.get(position).getSERVICE_NUMBER())){
                            Toast.makeText(OnlyMeterSurveyMap.this, "Service Number Already added", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    isServiceNumberSelected = true;
                    select_service_number.setText(list.get(position).getSERVICE_NUMBER());
                    selected_UKSC_NO = list.get(position).getUKSC_NUMBER();
                    selected_SC_NAME = list.get(position).getCONSUMER_NAME();

                    //Toast.makeText(OnlyMeterSurveyMap.this, "Service Number Already added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    //surveyDialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView search_tv_service_number,search_tv_consumer_name,search_tv_meter_number;
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
                new AlertDialog.Builder(OnlyMeterSurveyMap.this)
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
                    Toast.makeText(OnlyMeterSurveyMap.this, message, Toast.LENGTH_LONG).show();

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

            Log.d("currentLat",""+currentLat);
            Log.d("currentLon",""+currentLon);
            Log.d("poleLon",""+poleLon);
            Log.d("poleLat",""+poleLat);
            Log.d("distance",""+poleLocation.distanceTo(currentLocation));

            // distance in METERS
            return poleLocation.distanceTo(currentLocation);

        } catch (Exception e) {
            e.printStackTrace();
            return Double.MAX_VALUE; // safety fallback
        }
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
}
