package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.RdssUGDatabase;
import com.apepdcl.gis_survey.RDSS.RetrofitClientInstanceRDSS;
import com.apepdcl.gis_survey.RDSS.RetrofitServiceRDSS;
import com.apepdcl.gis_survey.RDSS.UGPostingModel;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.lt_edit_screens.OfflineEditLTAssetActivity;
import com.apepdcl.gis_survey.module_11KV.ArcGISDatabase;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitClientInstance;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;
import com.apepdcl.gis_survey.module_11KV.edit_screens.OfflineEdit11KVAssetActivity;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.module_33KV.edit_screens.OfflineEdit33KVAssetActivity;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditLocationsMap extends AppCompatActivity {
    MapView mv;
    GpsTraker gpsTraker;
    GraphicsOverlay mGraphicsOverlay;
    Graphic graphic;
    Callout mCallout;
    TextView home_heading;

    ArcGISMap map;
    private Point ptWgs84;
    PointCollection polylinePoints;
    LocationDisplay mLocationDisplay;
    NewModel surveyVo;
    SurveyVo modelAsset;
    SurveyDataVo listLT;
    com.apepdcl.gis_survey.module_33KV.vo.SurveyVo model33KV;
    ArrayList<UGPostingModel> postingModel = new ArrayList<>();

    ArcGISDatabaseEstimates databaseEstimates;
    ArcGISDatabase arcGISDatabase;
    com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase arcGISDatabaseLT;
    RdssUGDatabase database;

    com.apepdcl.gis_survey.module_33KV.ArcGISDatabase arcGISDatabase33;


    String GIS_ID, FROM = "", FEEDER_ID = "", POLE_NO = "", ID = "";
    private ProgressDialog progressDialog;
    private com.apepdcl.gis_survey.retrofitservice.RetrofitService retrofitService;

    RelativeLayout back_layout, current_location_layout;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        getSupportActionBar().hide();
        progressDialog = CommanUtilits.progressDialog(this);
        FROM = getIntent().getStringExtra("FROM");


        //System.out.println("FROMMMMMM->"+FROM);
        assert FROM != null;
        if (!FROM.isEmpty()) {
            if (FROM.equalsIgnoreCase("ASSETMAPPING")) {
                modelAsset = (SurveyVo) getIntent().getSerializableExtra("data");
                POLE_NO = getIntent().getStringExtra("POLE_NO");
                FEEDER_ID = getIntent().getStringExtra("FEEDER_ID");
                ID = getIntent().getStringExtra("ID");
            } else if (FROM.equalsIgnoreCase("ASSETMAPPING_33KV")) {
                model33KV = (com.apepdcl.gis_survey.module_33KV.vo.SurveyVo) getIntent().getSerializableExtra("data");
                POLE_NO = getIntent().getStringExtra("POLE_NO");
                FEEDER_ID = getIntent().getStringExtra("FEEDER_ID");
                ID = getIntent().getStringExtra("ID");
            } else if (FROM.equalsIgnoreCase("ASSETMAPPING_LT")) {
                listLT = (SurveyDataVo) getIntent().getSerializableExtra("data");
                POLE_NO = getIntent().getStringExtra("POLE_NO");
                FEEDER_ID = getIntent().getStringExtra("FEEDER_ID");
                ID = getIntent().getStringExtra("ID");
            } else if (FROM.equalsIgnoreCase("RDSS")) {
                postingModel = (ArrayList<UGPostingModel>) getIntent().getSerializableExtra("UG_MODEL");
                ID = getIntent().getStringExtra("ID");
            } else {
                surveyVo = (NewModel) getIntent().getSerializableExtra("data");
                GIS_ID = getIntent().getStringExtra("GIS_ID");
                POLE_NO = getIntent().getStringExtra("POLE_NO");
            }

        } else {

        }


        // surveyVo = (NewModel) getIntent().getSerializableExtra("data");

        databaseEstimates = new ArcGISDatabaseEstimates(this);
        arcGISDatabase = new ArcGISDatabase(this);
        arcGISDatabase33 = new com.apepdcl.gis_survey.module_33KV.ArcGISDatabase(this);
        database = new RdssUGDatabase(this);
        arcGISDatabaseLT = new com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase(this);

        mv = findViewById(R.id.map1);
        back_layout = findViewById(R.id.back_layout);
        current_location_layout = findViewById(R.id.close_layout);
        home_heading = findViewById(R.id.home_heading);

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
            Toast.makeText(EditLocationsMap.this, message, Toast.LENGTH_LONG).show();

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

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            home_heading.setText("ESTIMATES");
        } else {
            home_heading.setText("WORK ORDER");
        }


        mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                int x = (int) e.getX();
                int y = (int) e.getY();
                android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
                Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
                Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());
                double CURRENT_LAT = myWGS84Point.getX();
                double CURRENT_LON = myWGS84Point.getY();
                double distance = getDistanceBetweenPoles(CURRENT_LAT, CURRENT_LON);
                System.out.println("CHECK_FROM---------->>>>>::" + FROM);
                if ("ASSETMAPPING".equalsIgnoreCase(FROM)) {
                    handleAssetMappingTap(distance, myWGS84Point);
                } else if ("ASSETMAPPING_33KV".equalsIgnoreCase(FROM)) {
                    handleAssetMappingTap(distance, myWGS84Point);
                } else if ("ASSETMAPPING_LT".equalsIgnoreCase(FROM)) {
                    handleAssetMappingTap(distance, myWGS84Point);
                } else if ("RDSS".equalsIgnoreCase(FROM)) {
                    handleAssetMappingTap(distance, myWGS84Point);
                } else if ("OFFLINE_LT".equalsIgnoreCase(FROM)) {
                    handleAssetMappingTapOffline(distance, myWGS84Point);
                } else if ("OFFLINE_11KV".equalsIgnoreCase(FROM)) {
                    handleAssetMappingTapOffline11KV(distance, myWGS84Point);
                } else if ("OFFLINE_33KV".equalsIgnoreCase(FROM)) {
                    handleAssetMappingTapOffline33KV(distance, myWGS84Point);
                } else {
                    handleDefaultTap(distance, e, myWGS84Point);
                }


                return true;
            }
        });

    }

    private double getDistanceBetweenPoles(double polLat, double polLon) {
//        System.out.println("CURRENT LOCATION LAT : " + polLat);
//        System.out.println("CURRENT LOCATION LON : " + polLon);
//        System.out.println("OLD POLE LOCATION LAT : " + surveyVo.getLATITUDE());
//        System.out.println("OLD POLE LOCATION LON : " + surveyVo.getLONGITUDE());
        Location currentStartPoint = new Location("locationF");
        currentStartPoint.setLatitude(polLon);
        currentStartPoint.setLongitude(polLat);

        Location currentEndPoint = new Location("locationF");
        if (!OfflineEditLTAssetActivity.POLE_NO.isEmpty()) {
            currentEndPoint.setLatitude(Double.parseDouble(OfflineEditLTAssetActivity.LAN));
            currentEndPoint.setLongitude(Double.parseDouble(OfflineEditLTAssetActivity.LAT));
        } else if (!OfflineEdit11KVAssetActivity.POLE_NO.isEmpty()) {
            currentEndPoint.setLatitude(Double.parseDouble(OfflineEdit11KVAssetActivity.LAN));
            currentEndPoint.setLongitude(Double.parseDouble(OfflineEdit11KVAssetActivity.LAT));
        } else if (!OfflineEdit33KVAssetActivity.POLE_NO.isEmpty()) {
            currentEndPoint.setLatitude(Double.parseDouble(OfflineEdit33KVAssetActivity.LAN));
            currentEndPoint.setLongitude(Double.parseDouble(OfflineEdit33KVAssetActivity.LAT));
        } else {
            if (FROM.equalsIgnoreCase("ASSETMAPPING")) {
                currentEndPoint.setLatitude(Double.parseDouble(modelAsset.getLan()));
                currentEndPoint.setLongitude(Double.parseDouble(modelAsset.getLat()));
            } else if (FROM.equalsIgnoreCase("ASSETMAPPING_33KV")) {
                currentEndPoint.setLatitude(Double.parseDouble(model33KV.getLan()));
                currentEndPoint.setLongitude(Double.parseDouble(model33KV.getLat()));
            } else if (FROM.equalsIgnoreCase("RDSS")) {
                for (int i = 0; i < postingModel.size(); i++) {
                    currentEndPoint.setLatitude(Double.parseDouble(postingModel.get(i).getLON()));
                    currentEndPoint.setLongitude(Double.parseDouble(postingModel.get(i).getLAT()));

                }
            } else if (FROM.equalsIgnoreCase("ASSETMAPPING_LT")) {
                currentEndPoint.setLatitude(Double.parseDouble(listLT.getLAN()));
                currentEndPoint.setLongitude(Double.parseDouble(listLT.getLAT()));
            } else {
                currentEndPoint.setLatitude(Double.parseDouble(surveyVo.getLONGITUDE()));
                currentEndPoint.setLongitude(Double.parseDouble(surveyVo.getLATITUDE()));
            }
        }
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        //  System.out.println("POLE LOCATION DISTANCE : " + currentDistance);
        return currentDistance;
    }


    private void showSuccessDialog() {

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

        // Set Data
        title.setText("LOCATION CAPTURED!!");
        message.setText("New Location Captured Successfully.");

        tv_est_no.setText(getColoredText("Est No: ", surveyVo.getEST_NO(), this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

        // You can change icon to success if you have one
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.suc));
        // fallback: use warning if no success icon

        // Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("OKAY");

        // Click
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            onBackPressed();
        });

        dialog.setCancelable(false);

        // Style
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            dialog.getWindow().setLayout(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
        }

        dialog.show();
    }
//    private void showSuccessDialog() {
//        new AlertDialog.Builder(EditLocationsMap.this)
//                .setTitle("LOCATION CAPTURED!!")
//                .setCancelable(false)
//                .setMessage("New Location Captured Successfully.")
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.cancel();
//                        onBackPressed();
//
//
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//    }


    private void setCurrentLocation() {
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (!gps_enabled && !network_enabled) {
                new AlertDialog.Builder(EditLocationsMap.this)
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
                    Toast.makeText(EditLocationsMap.this, message, Toast.LENGTH_LONG).show();

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

        try {
            if (!FROM.isEmpty()) {
                if (FROM.equalsIgnoreCase("ASSETMAPPING")) {
                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));

                    ptWgs84 = new Point(Double.parseDouble(modelAsset.getLat()), Double.parseDouble(modelAsset.getLan()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    map.setInitialViewpoint(vp);
                    mv.setMap(map);
                    mGraphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(mGraphicsOverlay);
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);


                    Graphic graphic = new Graphic(ptWgs84, symbol);
                    mGraphicsOverlay.getGraphics().add(graphic);


                    TextSymbol textSymbol = new TextSymbol(
                            12,
                            String.valueOf(modelAsset.getPole_no()),
                            Color.CYAN,
                            TextSymbol.HorizontalAlignment.RIGHT,
                            TextSymbol.VerticalAlignment.TOP
                    );

                    Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);

                    mGraphicsOverlay.getGraphics().add(labelGraphic);

                    mGraphicsOverlay.setLabelsEnabled(true);

                    TextView calloutContent = new TextView(getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();
                    calloutContent.setText(String.valueOf(modelAsset.getPole_no()));
                    //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                    mCallout = mv.getCallout();
                    mCallout.setLocation(ptWgs84);
                    mCallout.setContent(calloutContent);
                    mCallout.show();
                    // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());


                    //Display Text

                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                    // create a text symbol for elevation mode
                    TextSymbol absoluteText = new TextSymbol(15, String.valueOf(modelAsset.getPole_no()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                            TextSymbol.VerticalAlignment.MIDDLE);
                    absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                    mv.getGraphicsOverlays().add(absoluteOverlay);
                } else if (FROM.equalsIgnoreCase("ASSETMAPPING_33KV")) {
                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));

                    ptWgs84 = new Point(Double.parseDouble(model33KV.getLat()), Double.parseDouble(model33KV.getLan()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    map.setInitialViewpoint(vp);
                    mv.setMap(map);
                    mGraphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(mGraphicsOverlay);
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);

                    Graphic graphic = new Graphic(ptWgs84, symbol);
                    mGraphicsOverlay.getGraphics().add(graphic);


                    TextSymbol textSymbol = new TextSymbol(
                            12,
                            String.valueOf(model33KV.getPole_no()),
                            Color.CYAN,
                            TextSymbol.HorizontalAlignment.RIGHT,
                            TextSymbol.VerticalAlignment.TOP
                    );

                    Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);

                    mGraphicsOverlay.getGraphics().add(labelGraphic);

                    mGraphicsOverlay.setLabelsEnabled(true);

                    TextView calloutContent = new TextView(getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();
                    calloutContent.setText(String.valueOf(model33KV.getPole_no()));
                    //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                    mCallout = mv.getCallout();
                    mCallout.setLocation(ptWgs84);
                    mCallout.setContent(calloutContent);
                    mCallout.show();
                    // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());


                    //Display Text

                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                    // create a text symbol for elevation mode
                    TextSymbol absoluteText = new TextSymbol(15, String.valueOf(model33KV.getPole_no()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                            TextSymbol.VerticalAlignment.MIDDLE);
                    absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                    mv.getGraphicsOverlays().add(absoluteOverlay);
                } else if (FROM.equalsIgnoreCase("RDSS")) {
                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
                    for (int i = 0; i < postingModel.size(); i++) {
                        ptWgs84 = new Point(Double.parseDouble(postingModel.get(i).getLAT()), Double.parseDouble(postingModel.get(i).getLON()), 414, SpatialReferences.getWgs84());
                        Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                        map.setInitialViewpoint(vp);
                        mv.setMap(map);
                        mGraphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(mGraphicsOverlay);
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);


                        Graphic graphic = new Graphic(ptWgs84, symbol);
                        mGraphicsOverlay.getGraphics().add(graphic);


                        TextSymbol textSymbol = new TextSymbol(
                                12,
                                String.valueOf(postingModel.get(i).getLOCATION_NO()),
                                Color.CYAN,
                                TextSymbol.HorizontalAlignment.RIGHT,
                                TextSymbol.VerticalAlignment.TOP
                        );

                        Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);

                        mGraphicsOverlay.getGraphics().add(labelGraphic);

                        mGraphicsOverlay.setLabelsEnabled(true);

                        TextView calloutContent = new TextView(getApplicationContext());
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine();
                        calloutContent.setText(String.valueOf(postingModel.get(i).getLOCATION_NO()));
                        //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                        mCallout = mv.getCallout();
                        mCallout.setLocation(ptWgs84);
                        mCallout.setContent(calloutContent);
                        mCallout.show();
                        // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());


                        //Display Text

                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                        // create a text symbol for elevation mode
                        TextSymbol absoluteText = new TextSymbol(15, String.valueOf(postingModel.get(i).getLOCATION_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE);
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }


                } else if (FROM.equalsIgnoreCase("ASSETMAPPING_LT")) {
                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));

                    ptWgs84 = new Point(Double.parseDouble(listLT.getLAT()), Double.parseDouble(listLT.getLAN()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    map.setInitialViewpoint(vp);
                    mv.setMap(map);
                    mGraphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(mGraphicsOverlay);
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);


                    Graphic graphic = new Graphic(ptWgs84, symbol);
                    mGraphicsOverlay.getGraphics().add(graphic);


                    TextSymbol textSymbol = new TextSymbol(
                            12,
                            String.valueOf(listLT.getPOLE_NO()),
                            Color.CYAN,
                            TextSymbol.HorizontalAlignment.RIGHT,
                            TextSymbol.VerticalAlignment.TOP
                    );

                    Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);

                    mGraphicsOverlay.getGraphics().add(labelGraphic);

                    mGraphicsOverlay.setLabelsEnabled(true);

                    TextView calloutContent = new TextView(getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();
                    calloutContent.setText(String.valueOf(listLT.getPOLE_NO()));
                    //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                    mCallout = mv.getCallout();
                    mCallout.setLocation(ptWgs84);
                    mCallout.setContent(calloutContent);
                    mCallout.show();
                    // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());


                    //Display Text

                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                    // create a text symbol for elevation mode
                    TextSymbol absoluteText = new TextSymbol(15, String.valueOf(listLT.getPOLE_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                            TextSymbol.VerticalAlignment.MIDDLE);
                    absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                    mv.getGraphicsOverlays().add(absoluteOverlay);
                } else if (FROM.equalsIgnoreCase("OFFLINE_LT")) {
                    if (!OfflineEditLTAssetActivity.POLE_NO.isEmpty()) {
                        System.out.println("CHECK----->>>>:::::");
                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                        //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));

                        //ptWgs84 = new Point(Double.parseDouble(listLT.getLAT()), Double.parseDouble(listLT.getLAN()), 414, SpatialReferences.getWgs84());
                        ptWgs84 = new Point(
                                Double.parseDouble(OfflineEditLTAssetActivity.LAT),
                                Double.parseDouble(OfflineEditLTAssetActivity.LAN),
                                414,
                                SpatialReferences.getWgs84()
                        );

                        Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                        map.setInitialViewpoint(vp);
                        mv.setMap(map);
                        mGraphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(mGraphicsOverlay);
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);


                        Graphic graphic = new Graphic(ptWgs84, symbol);
                        mGraphicsOverlay.getGraphics().add(graphic);


                        TextSymbol textSymbol = new TextSymbol(
                                12,
                                String.valueOf(OfflineEditLTAssetActivity.POLE_NO),
                                Color.CYAN,
                                TextSymbol.HorizontalAlignment.RIGHT,
                                TextSymbol.VerticalAlignment.TOP
                        );

                        Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);

                        mGraphicsOverlay.getGraphics().add(labelGraphic);

                        mGraphicsOverlay.setLabelsEnabled(true);

                        TextView calloutContent = new TextView(getApplicationContext());
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine();
                        calloutContent.setText(String.valueOf(OfflineEditLTAssetActivity.POLE_NO));
                        //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                        mCallout = mv.getCallout();
                        mCallout.setLocation(ptWgs84);
                        mCallout.setContent(calloutContent);
                        mCallout.show();
                        // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());


                        //Display Text

                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                        // create a text symbol for elevation mode
                        TextSymbol absoluteText = new TextSymbol(15, String.valueOf(OfflineEditLTAssetActivity.POLE_NO), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE);
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }
                } else if (FROM.equalsIgnoreCase("OFFLINE_11KV")) {
                    if (!OfflineEdit11KVAssetActivity.POLE_NO.isEmpty()) {
                        System.out.println("CHECK----->>>>:::::");
                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                        //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));

                        //ptWgs84 = new Point(Double.parseDouble(listLT.getLAT()), Double.parseDouble(listLT.getLAN()), 414, SpatialReferences.getWgs84());
                        ptWgs84 = new Point(
                                Double.parseDouble(OfflineEdit11KVAssetActivity.LAT),
                                Double.parseDouble(OfflineEdit11KVAssetActivity.LAN),
                                414,
                                SpatialReferences.getWgs84()
                        );

                        Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                        map.setInitialViewpoint(vp);
                        mv.setMap(map);
                        mGraphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(mGraphicsOverlay);
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);


                        Graphic graphic = new Graphic(ptWgs84, symbol);
                        mGraphicsOverlay.getGraphics().add(graphic);


                        TextSymbol textSymbol = new TextSymbol(
                                12,
                                String.valueOf(OfflineEdit11KVAssetActivity.POLE_NO),
                                Color.CYAN,
                                TextSymbol.HorizontalAlignment.RIGHT,
                                TextSymbol.VerticalAlignment.TOP
                        );

                        Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);

                        mGraphicsOverlay.getGraphics().add(labelGraphic);

                        mGraphicsOverlay.setLabelsEnabled(true);

                        TextView calloutContent = new TextView(getApplicationContext());
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine();
                        calloutContent.setText(String.valueOf(OfflineEdit11KVAssetActivity.POLE_NO));
                        //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                        mCallout = mv.getCallout();
                        mCallout.setLocation(ptWgs84);
                        mCallout.setContent(calloutContent);
                        mCallout.show();
                        // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());


                        //Display Text

                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                        // create a text symbol for elevation mode
                        TextSymbol absoluteText = new TextSymbol(15, String.valueOf(OfflineEdit11KVAssetActivity.POLE_NO), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE);
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }
                } else if (FROM.equalsIgnoreCase("OFFLINE_33KV")) {
                    if (!OfflineEdit33KVAssetActivity.POLE_NO.isEmpty()) {
                        System.out.println("CHECK----->>>>:::::");
                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                        //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));

                        //ptWgs84 = new Point(Double.parseDouble(listLT.getLAT()), Double.parseDouble(listLT.getLAN()), 414, SpatialReferences.getWgs84());
                        ptWgs84 = new Point(
                                Double.parseDouble(OfflineEdit33KVAssetActivity.LAT),
                                Double.parseDouble(OfflineEdit33KVAssetActivity.LAN),
                                414,
                                SpatialReferences.getWgs84()
                        );

                        Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                        map.setInitialViewpoint(vp);
                        mv.setMap(map);
                        mGraphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(mGraphicsOverlay);
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);


                        Graphic graphic = new Graphic(ptWgs84, symbol);
                        mGraphicsOverlay.getGraphics().add(graphic);


                        TextSymbol textSymbol = new TextSymbol(
                                12,
                                String.valueOf(OfflineEdit33KVAssetActivity.POLE_NO),
                                Color.CYAN,
                                TextSymbol.HorizontalAlignment.RIGHT,
                                TextSymbol.VerticalAlignment.TOP
                        );

                        Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);

                        mGraphicsOverlay.getGraphics().add(labelGraphic);

                        mGraphicsOverlay.setLabelsEnabled(true);

                        TextView calloutContent = new TextView(getApplicationContext());
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine();
                        calloutContent.setText(String.valueOf(OfflineEdit33KVAssetActivity.POLE_NO));
                        //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                        mCallout = mv.getCallout();
                        mCallout.setLocation(ptWgs84);
                        mCallout.setContent(calloutContent);
                        mCallout.show();
                        // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());


                        //Display Text

                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                        // create a text symbol for elevation mode
                        TextSymbol absoluteText = new TextSymbol(15, String.valueOf(OfflineEdit33KVAssetActivity.POLE_NO),
                                Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE);
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);
                    }
                } else {

                    // 1️⃣ Get nearby poles from DB
                    ArrayList<NewModel> DBMODEL =
                            databaseEstimates.getSurveyDataByDistance(
                                    Double.parseDouble(surveyVo.getLATITUDE()),
                                    Double.parseDouble(surveyVo.getLONGITUDE()),
                                    surveyVo.getFEEDER_ID(),
                                    surveyVo.getVOLTAGE_LEVEL(),   // or SURVEY_TYPE if required
                                    200
                            );

                    System.out.println("BHAVYANSH -> " + DBMODEL.size());

                    if (!DBMODEL.isEmpty()) {
                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                        for (NewModel nearBy : DBMODEL) {
                            ptWgs84 = new Point(Double.parseDouble(nearBy.getLATITUDE()), Double.parseDouble(nearBy.getLONGITUDE()), 414, SpatialReferences.getWgs84());
                            Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                            map.setInitialViewpoint(vp);
                            mv.setMap(map);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
                            Graphic graphic = new Graphic(ptWgs84, symbol);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            TextSymbol textSymbol = new TextSymbol(
                                    12,
                                    nearBy.getPOLE_NO(),
                                    Color.CYAN,
                                    TextSymbol.HorizontalAlignment.RIGHT,
                                    TextSymbol.VerticalAlignment.TOP
                            );

                            Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);
                            mGraphicsOverlay.getGraphics().add(labelGraphic);
                            mGraphicsOverlay.setLabelsEnabled(true);

                        }
                    }


                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));

                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    map.setInitialViewpoint(vp);
                    mv.setMap(map);
                    mGraphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(mGraphicsOverlay);
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);


                    Graphic graphic = new Graphic(ptWgs84, symbol);
                    mGraphicsOverlay.getGraphics().add(graphic);


                    TextSymbol textSymbol = new TextSymbol(
                            12,
                            surveyVo.getPOLE_NO(),
                            Color.CYAN,
                            TextSymbol.HorizontalAlignment.RIGHT,
                            TextSymbol.VerticalAlignment.TOP
                    );

                    Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);

                    mGraphicsOverlay.getGraphics().add(labelGraphic);

                    mGraphicsOverlay.setLabelsEnabled(true);

                    TextView calloutContent = new TextView(getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();
                    calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                    //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                    mCallout = mv.getCallout();
                    mCallout.setLocation(ptWgs84);
                    mCallout.setContent(calloutContent);
                    mCallout.show();
                    // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());


                    //Display Text

                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                    // create a text symbol for elevation mode
                    TextSymbol absoluteText = new TextSymbol(15, String.valueOf(surveyVo.getPOLE_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                            TextSymbol.VerticalAlignment.MIDDLE);
                    absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                    mv.getGraphicsOverlays().add(absoluteOverlay);
                }
//                else {
//
//                    //ArrayList<NewModel> DBMODEL=databaseEstimates.getSurveyDataByDistance(Double.parseDouble(surveyVo.getLATITUDE()),Double.parseDouble(surveyVo.getLONGITUDE()),surveyVo.getFEEDER_ID(),surveyVo.getVOLTAGE_LEVEL(),30);
//
//
//                    Toast.makeText(this, surveyVo.getPOLE_NO(), Toast.LENGTH_SHORT).show();
//                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//                    //   polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
//
//                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE()), 414, SpatialReferences.getWgs84());
//                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
//                    map.setInitialViewpoint(vp);
//                    mv.setMap(map);
//                    mGraphicsOverlay = new GraphicsOverlay();
//                    mv.getGraphicsOverlays().add(mGraphicsOverlay);
//                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
//
//
//                    Graphic graphic = new Graphic(ptWgs84, symbol);
//                    mGraphicsOverlay.getGraphics().add(graphic);
//
//
//                    TextSymbol textSymbol = new TextSymbol(
//                            12,
//                            surveyVo.getPOLE_NO(),
//                            Color.CYAN,
//                            TextSymbol.HorizontalAlignment.RIGHT,
//                            TextSymbol.VerticalAlignment.TOP
//                    );
//
//                    Graphic labelGraphic = new Graphic(ptWgs84, textSymbol);
//
//                    mGraphicsOverlay.getGraphics().add(labelGraphic);
//
//                    mGraphicsOverlay.setLabelsEnabled(true);
//
//                    TextView calloutContent = new TextView(getApplicationContext());
//                    calloutContent.setTextColor(Color.BLACK);
//                    calloutContent.setSingleLine();
//                    calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
//                    //calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
//                    mCallout = mv.getCallout();
//                    mCallout.setLocation(ptWgs84);
//                    mCallout.setContent(calloutContent);
//                    mCallout.show();
//                    // System.out.println("CALLOUT POLE NO : " + surveyVo.getPOLE_NO());
//
//
//                    //Display Text
//
//                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
//                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
//                    // create a text symbol for elevation mode
//                    TextSymbol absoluteText = new TextSymbol(15, String.valueOf(surveyVo.getPOLE_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
//                            TextSymbol.VerticalAlignment.MIDDLE);
//                    absoluteText.setOffsetY(20);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
//                    mv.getGraphicsOverlays().add(absoluteOverlay);
//                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void handleAssetMappingTap(double distance, Point tappedPoint) {
        if (FROM.equalsIgnoreCase("ASSETMAPPING")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                if (FROM.equalsIgnoreCase("OFFLINE_11KV")) {
                                    modelAsset.setLat(String.valueOf(tappedPoint.getX()));
                                    modelAsset.setLan(String.valueOf(tappedPoint.getY()));
                                    modelAsset.setPole_no(Integer.parseInt(OfflineEdit11KVAssetActivity.POLE_NO));
                                    modelAsset.setFeeder_id(OfflineEdit11KVAssetActivity.FEEDER_ID_ASSET);
                                    modelAsset.setID(OfflineEdit11KVAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES");
                                } else {
                                    modelAsset.setLat(String.valueOf(tappedPoint.getX()));
                                    modelAsset.setLan(String.valueOf(tappedPoint.getY()));
                                    modelAsset.setPole_no(Integer.parseInt(POLE_NO));
                                    modelAsset.setID(ID);
                                    modelAsset.setFeeder_id(FEEDER_ID);
                                }
                                arcGISDatabase.addSurveyDataLatLan(modelAsset);
                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEdit11KVAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);

                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }

        } else if (FROM.equalsIgnoreCase("ASSETMAPPING_33KV")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING 33KV")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationLTAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                System.out.println("TRIGGERING_TIMES" + FROM + " " + OfflineEdit33KVAssetActivity.ID);
                                if (FROM.equalsIgnoreCase("OFFLINE_33KV")) {
                                    model33KV.setLat(String.valueOf(tappedPoint.getX()));
                                    model33KV.setLan(String.valueOf(tappedPoint.getY()));
                                    model33KV.setPole_no(Integer.parseInt(OfflineEdit33KVAssetActivity.POLE_NO));
                                    model33KV.setFeeder_id(OfflineEdit33KVAssetActivity.FEEDER_ID_ASSET);
                                    model33KV.setID(OfflineEdit33KVAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES_33KV" + OfflineEdit33KVAssetActivity.ID);
                                } else {
                                    model33KV.setLat(String.valueOf(tappedPoint.getX()));
                                    model33KV.setLan(String.valueOf(tappedPoint.getY()));
                                    model33KV.setPole_no(Integer.parseInt(POLE_NO));
                                    model33KV.setID(ID);
                                    model33KV.setFeeder_id(FEEDER_ID);
                                }
                                arcGISDatabase33.addSurveyDataLatLan(model33KV);
                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEdit33KVAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);
                                //CommanUtilits.internetDialog(EditLocationsMap.this);
                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }

        } else if (FROM.equalsIgnoreCase("RDSS")) {
            new AlertDialog.Builder(EditLocationsMap.this)
                    .setTitle("RDSS")
                    .setMessage("Capture asset location here?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        // Example: just drop marker and store coordinates
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                        Graphic graphic = new Graphic(tappedPoint, symbol);
                        mGraphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().clear();
                        mv.getGraphicsOverlays().add(mGraphicsOverlay);
                        mGraphicsOverlay.getGraphics().add(graphic);
                        System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                        if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                            updatePoleLocationRdss(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                        } else {
                            System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
//                            UGPostingModel model = new UGPostingModel();
//                            model.setLAT(String.valueOf(tappedPoint.getX()));
//                            model.setLAT(String.valueOf(tappedPoint.getY()));
//                            postingModel.add(model);
//                            database.updateRdssPostingDynamic(model);
                            CommanUtilits.internetDialog(EditLocationsMap.this);
                        }


                        //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .setNegativeButton("CANCEL", null)
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        } else if (FROM.equalsIgnoreCase("ASSETMAPPING_LT")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationLTAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                if (FROM.equalsIgnoreCase("OFFLINE_LT")) {
                                    listLT.setLAT(String.valueOf(tappedPoint.getX()));
                                    listLT.setLAN(String.valueOf(tappedPoint.getY()));
                                    listLT.setPOLE_NO(Integer.parseInt(OfflineEditLTAssetActivity.POLE_NO));
                                    listLT.setFEEDER_ID(OfflineEditLTAssetActivity.FEEDER_ID_ASSET);
                                    listLT.setID(OfflineEditLTAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES");
                                } else {
                                    listLT.setLAT(String.valueOf(tappedPoint.getX()));
                                    listLT.setLAN(String.valueOf(tappedPoint.getY()));
                                    listLT.setPOLE_NO(Integer.parseInt(POLE_NO));
                                    listLT.setID(ID);
                                    listLT.setFEEDER_ID(FEEDER_ID);
                                }
                                arcGISDatabaseLT.addSurveyDataLatLan(listLT);
                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEditLTAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);
                                //CommanUtilits.internetDialog(EditLocationsMap.this);
                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }

        } else if (FROM.equalsIgnoreCase("OFFLINE_LT")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING LT")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationLTAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                System.out.println("TRIGGERING_TIMES" + FROM + " " + OfflineEditLTAssetActivity.ID);
                                if (FROM.equalsIgnoreCase("OFFLINE_LT")) {
                                    listLT.setLAT(String.valueOf(tappedPoint.getX()));
                                    listLT.setLAN(String.valueOf(tappedPoint.getY()));
                                    listLT.setPOLE_NO(Integer.parseInt(OfflineEditLTAssetActivity.POLE_NO));
                                    listLT.setFEEDER_ID(OfflineEditLTAssetActivity.FEEDER_ID_ASSET);
                                    listLT.setID(OfflineEditLTAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES");
                                } else {
                                    listLT.setLAT(String.valueOf(tappedPoint.getX()));
                                    listLT.setLAN(String.valueOf(tappedPoint.getY()));
                                    listLT.setPOLE_NO(Integer.parseInt(POLE_NO));
                                    listLT.setID(ID);
                                    listLT.setFEEDER_ID(FEEDER_ID);
                                }
                                arcGISDatabaseLT.addSurveyDataLatLan(listLT);
                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEditLTAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);
                                //CommanUtilits.internetDialog(EditLocationsMap.this);
                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        } else if (FROM.equalsIgnoreCase("OFFLINE_11KV")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING 11KV")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationLTAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                System.out.println("TRIGGERING_TIMES" + FROM + " " + OfflineEdit11KVAssetActivity.ID);
                                if (FROM.equalsIgnoreCase("OFFLINE_11KV")) {
                                    modelAsset.setLat(String.valueOf(tappedPoint.getX()));
                                    modelAsset.setLan(String.valueOf(tappedPoint.getY()));
                                    modelAsset.setPole_no(Integer.parseInt(OfflineEdit11KVAssetActivity.POLE_NO));
                                    modelAsset.setFeeder_id(OfflineEdit11KVAssetActivity.FEEDER_ID_ASSET);
                                    modelAsset.setID(OfflineEdit11KVAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES");
                                } else {
                                    modelAsset.setLat(String.valueOf(tappedPoint.getX()));
                                    modelAsset.setLan(String.valueOf(tappedPoint.getY()));
                                    modelAsset.setPole_no(Integer.parseInt(POLE_NO));
                                    modelAsset.setID(ID);
                                    modelAsset.setFeeder_id(FEEDER_ID);
                                }
                                arcGISDatabase.addSurveyDataLatLan(modelAsset);
                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEdit11KVAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);
                                //CommanUtilits.internetDialog(EditLocationsMap.this);
                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        } else if (FROM.equalsIgnoreCase("OFFLINE_33KV")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING 11KV")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationLTAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                System.out.println("TRIGGERING_TIMES" + FROM + " " + OfflineEdit33KVAssetActivity.ID);
                                if (FROM.equalsIgnoreCase("OFFLINE_33KV")) {
                                    model33KV.setLat(String.valueOf(tappedPoint.getX()));
                                    model33KV.setLan(String.valueOf(tappedPoint.getY()));
                                    model33KV.setPole_no(Integer.parseInt(OfflineEdit33KVAssetActivity.POLE_NO));
                                    model33KV.setFeeder_id(OfflineEdit33KVAssetActivity.FEEDER_ID_ASSET);
                                    model33KV.setID(OfflineEdit33KVAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES");
                                } else {
                                    model33KV.setLat(String.valueOf(tappedPoint.getX()));
                                    model33KV.setLan(String.valueOf(tappedPoint.getY()));
                                    model33KV.setPole_no(Integer.parseInt(POLE_NO));
                                    model33KV.setID(ID);
                                    model33KV.setFeeder_id(FEEDER_ID);
                                }
                                arcGISDatabase33.addSurveyDataLatLan(model33KV);
                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEdit33KVAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);
                                //CommanUtilits.internetDialog(EditLocationsMap.this);
                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        }

    }

    private void handleAssetMappingTapOffline(double distance, Point tappedPoint) {
        if (FROM.equalsIgnoreCase("OFFLINE_LT")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING LT")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationLTAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                System.out.println("TRIGGERING_TIMES" + FROM + " " + OfflineEditLTAssetActivity.ID);
                                if (FROM.equalsIgnoreCase("OFFLINE_LT")) {
                                    listLT.setLAT(String.valueOf(tappedPoint.getX()));
                                    listLT.setLAN(String.valueOf(tappedPoint.getY()));
                                    listLT.setPOLE_NO(Integer.parseInt(OfflineEditLTAssetActivity.POLE_NO));
                                    listLT.setFEEDER_ID(OfflineEditLTAssetActivity.FEEDER_ID_ASSET);
                                    listLT.setID(OfflineEditLTAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES");
                                } else {
                                    listLT.setLAT(String.valueOf(tappedPoint.getX()));
                                    listLT.setLAN(String.valueOf(tappedPoint.getY()));
                                    listLT.setPOLE_NO(Integer.parseInt(POLE_NO));
                                    listLT.setID(ID);
                                    listLT.setFEEDER_ID(FEEDER_ID);
                                }
                                arcGISDatabaseLT.addSurveyDataLatLan(listLT);
                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEditLTAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);
                                //CommanUtilits.internetDialog(EditLocationsMap.this);
                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        }


    }

    private void handleAssetMappingTapOffline11KV(double distance, Point tappedPoint) {
        if (FROM.equalsIgnoreCase("OFFLINE_11KV")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING 11KV")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationLTAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                System.out.println("TRIGGERING_TIMES" + FROM + " " + OfflineEdit11KVAssetActivity.ID);
                                if (FROM.equalsIgnoreCase("OFFLINE_11KV")) {
                                    modelAsset.setLat(String.valueOf(tappedPoint.getX()));
                                    modelAsset.setLan(String.valueOf(tappedPoint.getY()));
                                    modelAsset.setPole_no(Integer.parseInt(OfflineEdit11KVAssetActivity.POLE_NO));
                                    modelAsset.setFeeder_id(OfflineEdit11KVAssetActivity.FEEDER_ID_ASSET);
                                    modelAsset.setID(OfflineEdit11KVAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES");
                                } else {
                                    modelAsset.setLat(String.valueOf(tappedPoint.getX()));
                                    modelAsset.setLan(String.valueOf(tappedPoint.getY()));
                                    modelAsset.setPole_no(Integer.parseInt(POLE_NO));
                                    modelAsset.setID(ID);
                                    modelAsset.setFeeder_id(FEEDER_ID);
                                }
                                arcGISDatabase.addSurveyDataLatLan(modelAsset);

                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEdit11KVAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);
                                //CommanUtilits.internetDialog(EditLocationsMap.this);
                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        }


    }


    private void handleAssetMappingTapOffline33KV(double distance, Point tappedPoint) {
        if (FROM.equalsIgnoreCase("OFFLINE_33KV")) {
            if (distance > 90) {
                int dist = (int) distance;
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ABNORMAL DISTANCE!!")
                        .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                        .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(EditLocationsMap.this)
                        .setTitle("ASSET MAPPING 33KV")
                        .setMessage("Capture asset location here?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            // Example: just drop marker and store coordinates
                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                            Graphic graphic = new Graphic(tappedPoint, symbol);
                            mGraphicsOverlay = new GraphicsOverlay();
                            mv.getGraphicsOverlays().clear();
                            mv.getGraphicsOverlays().add(mGraphicsOverlay);
                            mGraphicsOverlay.getGraphics().add(graphic);
                            System.out.println("LATTITUDE LONGITUDE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));

                            if (ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
                                updatePoleLocationLTAsset(String.valueOf(tappedPoint.getX()), String.valueOf(tappedPoint.getY()));
                            } else {
                                System.out.println("LAT_LNG_OFFLINE" + String.valueOf(tappedPoint.getX() + "   " + String.valueOf(tappedPoint.getY())));
                                System.out.println("TRIGGERING_TIMES" + FROM + " " + OfflineEdit33KVAssetActivity.ID);
                                if (FROM.equalsIgnoreCase("OFFLINE_33KV")) {
                                    model33KV.setLat(String.valueOf(tappedPoint.getX()));
                                    model33KV.setLan(String.valueOf(tappedPoint.getY()));
                                    model33KV.setPole_no(Integer.parseInt(OfflineEdit33KVAssetActivity.POLE_NO));
                                    model33KV.setFeeder_id(OfflineEdit33KVAssetActivity.FEEDER_ID_ASSET);
                                    model33KV.setID(OfflineEdit33KVAssetActivity.ID);
                                    System.out.println("TRIGGERING_TIMES");
                                } else {
                                    model33KV.setLat(String.valueOf(tappedPoint.getX()));
                                    model33KV.setLan(String.valueOf(tappedPoint.getY()));
                                    model33KV.setPole_no(Integer.parseInt(POLE_NO));
                                    model33KV.setID(ID);
                                    model33KV.setFeeder_id(FEEDER_ID);
                                }
                                arcGISDatabase33.addSurveyDataLatLan(model33KV);

                                Intent intentEdit = new Intent(EditLocationsMap.this, OfflineEdit33KVAssetActivity.class);
                                intentEdit.putExtra("FEEDER_ID", FEEDER_ID);
                                startActivity(intentEdit);
                                //CommanUtilits.internetDialog(EditLocationsMap.this);
                            }


                            //Toast.makeText(this, "Asset location captured successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", null)
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
            }
        }


    }


    private void handleDefaultTap(double distance, MotionEvent e, Point myWGS84Point) {
        if (distance > 90) {
            int dist = (int) distance;
            new AlertDialog.Builder(EditLocationsMap.this)
                    .setTitle("ABNORMAL DISTANCE!!")
                    .setMessage("DISTANCE: " + dist + " meters.\nNew location cannot be farther than 90 meters from old location.")
                    .setPositiveButton("OKAY", (dialog, which) -> dialog.dismiss())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
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

            // Set Data
            title.setText("CONFIRMATION!!");
            message.setText("Are you sure you want to capture this location for Location no? " + POLE_NO);

            tv_est_no.setText(getColoredText("Est No: ", surveyVo.getEST_NO(), this));
            tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));

            iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.question));

            // Buttons
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);

            okBtn.setText("YES");
            cancelBtn.setText("NO");

            // YES Click
            okBtn.setOnClickListener(v -> {

                Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
                map.setInitialViewpoint(vp);
                mv.setMap(map);

                mGraphicsOverlay = new GraphicsOverlay();
                mv.getGraphicsOverlays().clear();
                mv.getGraphicsOverlays().add(mGraphicsOverlay);

                SimpleMarkerSymbol symbol =
                        new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 12);

                Graphic graphic = new Graphic(myWGS84Point, symbol);
                mGraphicsOverlay.getGraphics().add(graphic);

                EditModel model = new EditModel();
                model.setEDIT_EST_NO(surveyVo.getEST_NO());
                model.setEDIT_LOCATION_ID(surveyVo.getLOCATION_ID());
                model.setEDIT_POLE_NO(surveyVo.getPOLE_NO());
                model.setEDIT_VOLTAGE_LEVEL(surveyVo.getVOLTAGE_LEVEL());
                model.setEDIT_GIS_ID(GIS_ID);
                model.setEDIT_TYPE(LoginActivity.SURVEY_FROM);
                model.setEDIT_POLE_LAT(String.valueOf(myWGS84Point.getY()));
                model.setEDIT_POLE_LON(String.valueOf(myWGS84Point.getX()));

                if (surveyVo.getIS_NEW_SURVEY().equalsIgnoreCase("Y")) {
                    databaseEstimates.updateOnlyLocationBeforeTransmit(
                            surveyVo.getEST_NO(),
                            surveyVo.getPOLE_NO(),
                            String.valueOf(myWGS84Point.getX()),
                            String.valueOf(myWGS84Point.getY()),
                            GIS_ID
                    );
                } else {
                    databaseEstimates.insertOrUpdateEditLocationsData(model);
                }

                Edit33Home.IS_RESUMED = true;

                dialog.dismiss();
                showSuccessDialog();
            });

            // NO Click
            cancelBtn.setOnClickListener(v -> dialog.dismiss());

            dialog.setCancelable(false);

            // Style (same as your previous dialog)
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                dialog.getWindow().setLayout(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                );
            }

            dialog.show();
//            new AlertDialog.Builder(EditLocationsMap.this)
//                    .setTitle("CONFIRMATION!!")
//                    .setMessage("Are you sure you want to capture this location?")
//                    .setPositiveButton("YES", (dialog, which) -> {
//                        Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
//                        map.setInitialViewpoint(vp);
//                        mv.setMap(map);
//
//                        mGraphicsOverlay = new GraphicsOverlay();
//                        mv.getGraphicsOverlays().clear();
//                        mv.getGraphicsOverlays().add(mGraphicsOverlay);
//
//                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 12);
//                        Graphic graphic = new Graphic(myWGS84Point, symbol);
//                        mGraphicsOverlay.getGraphics().add(graphic);
//
//                        EditModel model = new EditModel();
//                        model.setEDIT_EST_NO(surveyVo.getEST_NO());
//                        model.setEDIT_LOCATION_ID(surveyVo.getLOCATION_ID());
//                        model.setEDIT_POLE_NO(surveyVo.getPOLE_NO());
//                        model.setEDIT_VOLTAGE_LEVEL(surveyVo.getVOLTAGE_LEVEL());
//                        model.setEDIT_GIS_ID(GIS_ID);
//                        model.setEDIT_TYPE(LoginActivity.SURVEY_FROM);
//                        model.setEDIT_POLE_LAT(String.valueOf(myWGS84Point.getY()));
//                        model.setEDIT_POLE_LON(String.valueOf(myWGS84Point.getX()));
//
//                        if (surveyVo.getIS_NEW_SURVEY().equalsIgnoreCase("Y")) {
//                            databaseEstimates.updateOnlyLocationBeforeTransmit(
//                                    surveyVo.getEST_NO(),
//                                    surveyVo.getPOLE_NO(),
//                                    String.valueOf(myWGS84Point.getX()),
//                                    String.valueOf(myWGS84Point.getY()),
//                                    GIS_ID
//                            );
//                        } else {
//                            databaseEstimates.insertOrUpdateEditLocationsData(model);
//                        }
//
//                        Edit33Home.IS_RESUMED = true;
//                        dialog.dismiss();
//                        showSuccessDialog();
//                    })
//                    .setNegativeButton(android.R.string.no, null)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
        }


    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if (!OfflineEditLTAssetActivity.FROM_TYPE.isEmpty()) {
            FROM = OfflineEditLTAssetActivity.FROM_TYPE;
            POLE_NO = OfflineEditLTAssetActivity.POLE_NO;
            FEEDER_ID = OfflineEditLTAssetActivity.FEEDER_ID_ASSET;
        } else if (!OfflineEdit11KVAssetActivity.FROM_TYPE.isEmpty()) {
            FROM = OfflineEdit11KVAssetActivity.FROM_TYPE;
            POLE_NO = OfflineEdit11KVAssetActivity.POLE_NO;
            FEEDER_ID = OfflineEdit11KVAssetActivity.FEEDER_ID_ASSET;
            ID = OfflineEdit11KVAssetActivity.ID;
        } else if (!OfflineEdit33KVAssetActivity.FROM_TYPE.isEmpty()) {
            FROM = OfflineEdit33KVAssetActivity.FROM_TYPE;
            POLE_NO = OfflineEdit33KVAssetActivity.POLE_NO;
            FEEDER_ID = OfflineEdit33KVAssetActivity.FEEDER_ID_ASSET;
            ID = OfflineEdit33KVAssetActivity.ID;
        }

        System.out.println("FROM_RESTART--->" + FROM);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onResume() {
        super.onResume();
        //POLE_NO = OfflineEditLTAssetActivity.POLE_NO;
        // FEEDER_ID = OfflineEditLTAssetActivity.FEEDER_ID_ASSET;

    }

    private void updatePoleLocationAsset(String GLAT, String GLONG) {
        if (!ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog (if you already have a reusable one, use that)
        progressDialog = new ProgressDialog(EditLocationsMap.this);
        progressDialog.setMessage("Fetching pole data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObject postParam = new JsonObject();
        postParam.addProperty("FEEDER_ID", FEEDER_ID);
        postParam.addProperty("GLONG", GLONG);
        postParam.addProperty("GLAT", GLAT);
        postParam.addProperty("MODE", "LOCATION");
        postParam.addProperty("ID", ID);

        RetrofitService retrofitService =
                RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = retrofitService.updatePoleDetailsByMode(postParam);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(EditLocationsMap.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    String status = jsonObject.has("STATUS") ? jsonObject.get("STATUS").getAsString() : "FALSE";

                    if ("TRUE".equalsIgnoreCase(status)) {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditLocationsMap.this, errorMsg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        //JsonArray responseArray = jsonObject.getAsJsonArray("RESPONSE");


                    } else {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditLocationsMap.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditLocationsMap.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditLocationsMap.this, "Network error, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void updatePoleLocationLTAsset(String GLAT, String GLONG) {
        if (!ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog (if you already have a reusable one, use that)
        progressDialog = new ProgressDialog(EditLocationsMap.this);
        progressDialog.setMessage("Fetching pole data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObject postParam = new JsonObject();
        postParam.addProperty("FEEDER_ID", FEEDER_ID);
        postParam.addProperty("GLONG", GLONG);
        postParam.addProperty("GLAT", GLAT);
        postParam.addProperty("MODE", "LOCATION");
        postParam.addProperty("ID", ID);

        retrofitService = com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance.getRetrofitInstance().create(com.apepdcl.gis_survey.retrofitservice.RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.updatePoleDetailsByMode(postParam);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(EditLocationsMap.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    String status = jsonObject.has("STATUS") ? jsonObject.get("STATUS").getAsString() : "FALSE";

                    if ("TRUE".equalsIgnoreCase(status)) {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditLocationsMap.this, errorMsg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        //JsonArray responseArray = jsonObject.getAsJsonArray("RESPONSE");


                    } else {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditLocationsMap.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditLocationsMap.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditLocationsMap.this, "Network error, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void updatePoleLocationAsset33KV(String GLAT, String GLONG) {
        if (!ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog (if you already have a reusable one, use that)
        progressDialog = new ProgressDialog(EditLocationsMap.this);
        progressDialog.setMessage("Fetching pole data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObject postParam = new JsonObject();
        postParam.addProperty("FEEDER_ID", FEEDER_ID);
        postParam.addProperty("GLONG", GLONG);
        postParam.addProperty("GLAT", GLAT);
        postParam.addProperty("MODE", "LOCATION");
        postParam.addProperty("ID", ID);

        com.apepdcl.gis_survey.module_33KV.RetrofitService retrofitService =
                com.apepdcl.gis_survey.module_33KV.RetrofitClientInstance.getRetrofitInstance().create(com.apepdcl.gis_survey.module_33KV.RetrofitService.class);
        Call<JsonObject> call = retrofitService.updatePoleDetailsByMode(postParam);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(EditLocationsMap.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    String status = jsonObject.has("STATUS") ? jsonObject.get("STATUS").getAsString() : "FALSE";

                    if ("TRUE".equalsIgnoreCase(status)) {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditLocationsMap.this, errorMsg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        //JsonArray responseArray = jsonObject.getAsJsonArray("RESPONSE");


                    } else {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditLocationsMap.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditLocationsMap.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditLocationsMap.this, "Network error, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePoleLocationRdss(String GLAT, String GLONG) {
        if (!ConnectivityChecking.checkConnection(EditLocationsMap.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog (if you already have a reusable one, use that)
        progressDialog = new ProgressDialog(EditLocationsMap.this);
        progressDialog.setMessage("Fetching pole data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObject postParam = new JsonObject();
        // postParam.addProperty("FEEDER_ID", FEEDER_ID);
        postParam.addProperty("LON", GLONG);
        postParam.addProperty("LAT", GLAT);
        postParam.addProperty("MODE", "LOCATION");
        postParam.addProperty("ID", ID);

        RetrofitServiceRDSS retrofitServiceRDSS = RetrofitClientInstanceRDSS.getRetrofitInstance().create(RetrofitServiceRDSS.class);
        Call<JsonObject> responseVoCall = retrofitServiceRDSS.getEditSurveyLocation(postParam);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(EditLocationsMap.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    String status = jsonObject.has("STATUS") ? jsonObject.get("STATUS").getAsString() : "FALSE";

                    if ("TRUE".equalsIgnoreCase(status)) {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditLocationsMap.this, errorMsg, Toast.LENGTH_SHORT).show();
                        getOnBackPressedDispatcher().onBackPressed();
                        ;
                        //JsonArray responseArray = jsonObject.getAsJsonArray("RESPONSE");


                    } else {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditLocationsMap.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditLocationsMap.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditLocationsMap.this, "Network error, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }


}
