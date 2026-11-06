package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.ImpDataRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.popup.CustomAllDialogsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.GpsTrakerRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
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

import java.util.ArrayList;
import java.util.List;

public class IntermediateMapRDSSUg11KvActivity extends AppCompatActivity implements ImpDataRDSSUg11Kv, View.OnClickListener{

    private MapView mv;
    ArcGISMap map;
    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay;
    private GraphicsOverlay mGraphicsOverlay;
    private GraphicsOverlay currentLocmGraphicsOverla;
    Graphic graphic;
    Graphic currentLocGraphic;
    private Callout mCallout;
    GpsTrakerRDSSUg11Kv gpsTrakerRDSSUg11Kv;
    private String ssName, feederName, feederCode, maxCount, tappingPoint, surveyType, sessionId;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private Point ptWgs84;
    private int i, count, maxSurveyCount, poleCountCheck;
    private SharedPreferences preferences;
    PointCollection polylinePoints;
    private ImageButton img_logout;
    private LocationDisplay mLocationDisplay;
    TextView calloutContent;

    String pole_1,pole_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate_map);
//        getSupportActionBar().hide();
        mv = (MapView) findViewById(R.id.map1);
        img_logout = (ImageButton) findViewById(R.id.img_logout);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11KV UG Cable Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(IntermediateMapRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            gpsTrakerRDSSUg11Kv = new GpsTrakerRDSSUg11Kv(IntermediateMapRDSSUg11KvActivity.this);
            Intent intent = getIntent();
            /*ssName = intent.getStringExtra("SSNAME");
            feederName = intent.getStringExtra("FEEDER_NAME");
            feederCode = intent.getStringExtra("FEEDER_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            surveyType = intent.getStringExtra("SURVEY_TYPE");*/

            ssName = intent.getStringExtra("SSNAME");
            feederName = intent.getStringExtra("FEEDER_NAME");
            feederCode = intent.getStringExtra("FEEDER_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            pole_1 = intent.getStringExtra("POLE_1");
            pole_2 = intent.getStringExtra("POLE_2");
            surveyType = intent.getStringExtra("SURVEY_TYPE");

            gpsTrakerRDSSUg11Kv.getLocation();

            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(IntermediateMapRDSSUg11KvActivity.this);
            surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getData();
            count = arcGISDatabaseRDSSUg11Kv.getObjectId();
            poleCountCheck = arcGISDatabaseRDSSUg11Kv.getObjectIdCount();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);

            currentLocmGraphicsOverla = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(currentLocmGraphicsOverla);

            img_logout.setVisibility(View.VISIBLE);
            img_logout.setOnClickListener(this);
            maxSurveyCount = Integer.parseInt(maxCount);


            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTrakerRDSSUg11Kv.getLatitude(), gpsTrakerRDSSUg11Kv.getLongitude(), 20);
            mv.setMap(map);

            mLocationDisplay = mv.getLocationDisplay();

            preferences = PreferenceManager.getDefaultSharedPreferences(IntermediateMapRDSSUg11KvActivity.this);
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(IntermediateMapRDSSUg11KvActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }


            mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }


                String message = String.format("Error in DataSourceStatusChangedListener: %s",
                        dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(IntermediateMapRDSSUg11KvActivity.this, message, Toast.LENGTH_LONG).show();

            });
            // mLocationDisplay.stop();
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            mLocationDisplay.startAsync();


            mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    try {
                        int x = (int) e.getX();
                        int y = (int) e.getY();
                        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
                        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
                        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint,
                                SpatialReferences.getWgs84());


                        // Point point = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                        Viewpoint vp = new Viewpoint(myWGS84Point, 7500);

                        // set initial map extent
                        map.setInitialViewpoint(vp);

                        // set the map to be displayed in the mapview
                        mv.setMap(map);

                        //mv.removeView(mv);
                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        //[DocRef: Name=Point graphic with symbol, Category=Fundamentals, Topic=Symbols and Renderers]
                        //create a simple marker symbol
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,
                                12); //size 12, style of circle

                        //add a new graphic with a new point geometry
                        // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                        graphic = new Graphic(myWGS84Point, symbol);
                        graph_objects.add(graphic);
                        graphicsOverlay.getGraphics().add(graphic);
                        //graphicsOverlay.getGraphics().remove(graphic);

                        // create a textview for the callout
                        TextView calloutContent = new TextView(IntermediateMapRDSSUg11KvActivity.this);
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine();

                        // format coordinates to 4 decimal places


                        poleCountCheck = arcGISDatabaseRDSSUg11Kv.getObjectIdCount();
                        count = arcGISDatabaseRDSSUg11Kv.getObjectId();
                        if (poleCountCheck == 0) {
                            int polenum = maxSurveyCount + 1;
                            calloutContent.setText(String.valueOf(polenum));
                        } else {
                            calloutContent.setText(String.valueOf(count + 1));
                        }
                        // get callout, set content and show
                        mCallout = mMapView.getCallout();
                        mCallout.setLocation(myWGS84Point);
                        mCallout.setContent(calloutContent);
                        mCallout.show();


                        CustomAllDialogsRDSSUg11Kv greeting = new CustomAllDialogsRDSSUg11Kv("int", IntermediateMapRDSSUg11KvActivity.this, String.valueOf(myWGS84Point.getX()), String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, feederName, feederCode, maxSurveyCount, tappingPoint, surveyType, currentLocmGraphicsOverla, currentLocGraphic, ssName);
                        greeting.deligate = IntermediateMapRDSSUg11KvActivity.this;
                        greeting.show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return true;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getData();
            count = arcGISDatabaseRDSSUg11Kv.getObjectId();
            if (surveyVoRDSSUg11KvList.size() > 0) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                /*for (SurveyVo surveyVo : surveyVoList) {
                   // ptWgs84 = new Point(Double.parseDouble(surveyVo.getLat()), Double.parseDouble(surveyVo.getLan()), 414, SpatialReferences.getWgs84());
                    ptWgs84 = new Point(Double.parseDouble(PoleSurveyActivity.poleLocations.get(i).getLat()), Double.parseDouble(PoleSurveyActivity.poleLocations.get(i).getLon()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    String polenum = String.valueOf(surveyVo.getPole_no());
                    // set initial map extent
                    map.setInitialViewpoint(vp);

                    // set the map to be displayed in the mapview
                    mv.setMap(map);

                    //mv.removeView(mv);
                    graphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(graphicsOverlay);

                    //[DocRef: Name=Point graphic with symbol, Category=Fundamentals, Topic=Symbols and Renderers]
                    //create a simple marker symbol
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,
                            12); //size 12, style of circle

                    //add a new graphic with a new point geometry
                    // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                    graphic = new Graphic(ptWgs84, symbol);
                    graph_objects.add(graphic);
                    graphicsOverlay.getGraphics().add(graphic);


                    // create a textview for the callout
                    calloutContent = new TextView(getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();

                    // format coordinates to 4 decimal places
                    //calloutContent.setText(String.valueOf(surveyVo.getPole_no()));
                    calloutContent.setText(polenum);

                    //calloutContent.setText("");

                    // get callout, set content and show
                    mCallout = mv.getCallout();
                    mCallout.setLocation(ptWgs84);
                    mCallout.setContent(calloutContent);
                    mCallout.show();

                }*/

                for (int i = 0; i< PoleSurveyRDSSUg11KvActivity.poleLocations.size(); i++) {
                    ptWgs84 = new Point(Double.parseDouble(PoleSurveyRDSSUg11KvActivity.poleLocations.get(i).getLat()), Double.parseDouble(PoleSurveyRDSSUg11KvActivity.poleLocations.get(i).getLon()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    String polenum = String.valueOf(PoleSurveyRDSSUg11KvActivity.poleLocations.get(i).getPole());
                    // set initial map extent
                    map.setInitialViewpoint(vp);

                    // set the map to be displayed in the mapview
                    mv.setMap(map);

                    //mv.removeView(mv);
                    graphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(graphicsOverlay);

                    //[DocRef: Name=Point graphic with symbol, Category=Fundamentals, Topic=Symbols and Renderers]
                    //create a simple marker symbol
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12); //size 12, style of circle

                    //add a new graphic with a new point geometry
                    // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                    graphic = new Graphic(ptWgs84, symbol);
                    graph_objects.add(graphic);
                    graphicsOverlay.getGraphics().add(graphic);

                    //POLE NUMBER ON MAP
                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                    // create a text symbol for elevation mode
                    TextSymbol absoluteText = new TextSymbol(15, polenum, Color.WHITE, TextSymbol.HorizontalAlignment.CENTER,
                            TextSymbol.VerticalAlignment.MIDDLE);  absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                    mv.getGraphicsOverlays().add(absoluteOverlay);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logout:
                try {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {
                    }

                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception ex) {
                    }

                    if (!gps_enabled && !network_enabled) {
                        new AlertDialog.Builder(IntermediateMapRDSSUg11KvActivity.this)
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
                            Toast.makeText(IntermediateMapRDSSUg11KvActivity.this, message, Toast.LENGTH_LONG).show();

                        });
                        // mLocationDisplay.stop();
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        mLocationDisplay.startAsync();
                    }

                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void IsSetData(String data, String postion) {
        if (postion.equals("0")) {

        } else {

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IntermediateMapRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv.resume();
    }
}